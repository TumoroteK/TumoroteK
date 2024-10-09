/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
 * Ce logiciel est régi par la licence CeCILL soumise au droit français
 * et respectant les principes de diffusion des logiciels libres. Vous
 * pouvez utiliser, modifier et/ou redistribuer ce programme sous les
 * conditions de la licence CeCILL telle que diffusée par le CEA, le
 * CNRS et l'INRIA sur le site "http://www.cecill.info".
 * En contrepartie de l'accessibilité au code source et des droits de
 * copie, de modification et de redistribution accordés par cette
 * licence, il n'est offert aux utilisateurs qu'une garantie limitée.
 * Pour les mêmes raisons, seule une responsabilité restreinte pèse sur
 * l'auteur du programme, le titulaire des droits patrimoniaux et les
 * concédants successifs.
 * <p>
 * A cet égard  l'attention de l'utilisateur est attirée sur les
 * risques associés au chargement,  à l'utilisation,  à la modification
 * et/ou au  développement et à la reproduction du logiciel par
 * l'utilisateur étant donné sa spécificité de logiciel libre, qui peut
 * le rendre complexe à manipuler et qui le réserve donc à des
 * développeurs et des professionnels  avertis possédant  des
 * connaissances  informatiques approfondies.  Les utilisateurs sont
 * donc invités à charger  et  tester  l'adéquation  du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs
 * systèmes et ou de leurs données et, plus généralement, à l'utiliser
 * et l'exploiter dans les mêmes conditions de sécurité.
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.manager.impl.stockage.planconteneur;

import fr.aphp.tumorotek.manager.io.document.DataAsTable;
import fr.aphp.tumorotek.manager.io.document.DocumentData;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellContent;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellRow;
import fr.aphp.tumorotek.manager.io.document.detail.table.DataCell;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.systeme.Couleur;

import java.util.List;
import java.util.Map;

/**
 * Classe abstraite pour la génération de plans de congélateurs avec boîte.
 * <p>
 * Cette classe étend {@link AbstractPlanCongelateurGenerator} et fournit une
 * implémentation spécifique pour la génération de plans de congélateurs qui
 * sans boîtes. Elle est conçue pour être étendue par des classes concrètes
 * qui doivent définir la logique spécifique à la génération des détails du plan.
 * </p>
 * <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */

public abstract class AbstractPlanCongelateurSansBoiteGenerator extends AbstractPlanCongelateurGenerator
{

    private static final int PREMIERE_NIVEAU = 1;


    // Méthode abstraite pour récupérer le gestionnaire d'enceinte
    protected abstract EnceinteManager getEnceinteManager();

    /**
     * Construit une représentation d'un conteneur en indiquant
     * ce qui se trouve à chaque position (enceinte ou vide).
     * Organise les compartiments et sous-compartiments par position.
     *
     * @param conteneur Le conteneur pour lequel le plan détaillé est construit.
     * @return Une instance de DataAsTable représentant la structure du conteneur
     *         et précisant la présence d'une enceinte ou d'un espace vide à chaque position.
     */
    @Override
    public DocumentData buildDetailPlan(Conteneur conteneur){

        int nbNiveauDeEnceinte = conteneur.getNbrNiv() - 1;
        // Initialisation d'une table de données pour stocker la structure du conteneur.
        DataAsTable dataAsTable = new DataAsTable();

        // Récupération des enceintes de niveau 1, ce qui est essentiel pour construire la représentation du conteneur.
        List<Enceinte> enceintesNiveau1 = getEnceinteManager().findByConteneurWithOrderManager(conteneur);

        // Obtenir le nombre max d’enceintes attendues
        int nbrEnceinte = conteneur.getNbrEnc();

        // Création d'une carte pour associer chaque enceinte à sa position, facilitant l'accès rapide lors de l'ajout dans la table.
        Map<Integer, Enceinte> positionMap = createMapEnceintesByPosition(enceintesNiveau1);

        // Remplissage de la table de données avec les enceintes en fonction de leur position, ce qui constitue le cœur de la méthode.
        recursivelyAddEntriesToTable(positionMap,  nbrEnceinte, PREMIERE_NIVEAU, dataAsTable, nbNiveauDeEnceinte);

        // Retourne la table de données construite pour une utilisation ultérieure.
        return dataAsTable;
    }

    /**
     * Ajoute des cellules vides à une ligne de cellules jusqu'à un certain niveau,
     * puis ajoute une cellule de données.
     *
     * @param datacell La cellule de données à ajouter.
     * @param nbCellToAdd Le nombre de cellules vides à ajouter avant la cellule de données.
     * @return La ligne de cellules complétée.
     */
    private CellRow fillRowWithCells(DataCell datacell, int nbCellToAdd){
        // Crée une nouvelle instance de CellRow pour contenir les cellules.

        CellRow cellRow = new CellRow();
        // Ajouter des cellules vides en fonction du niveau spécifié
        for(int i = 0; i < nbCellToAdd; i++){
            DataCell dataCell = new DataCell(new CellContent(null));
            cellRow.addDataCell(dataCell);
        }
        // Ajouter la cellule de données passée en paramètre
        cellRow.addDataCell(datacell);

        return cellRow;
    }



    /**
     * Cette méthode remplit un tableau structuré contenant différentes lignes liées aux enceints et sous-en ceints
     * selon leurs emplacements respectifs tout en tenant compte hierarchie ordonnée existante .
     *
     * @param positionMap Une carte des positions des enceintes.
     * @param numberOfPlaces Le nombre de places disponibles dans l'enceinte.
     * @param niveau Le niveau d'imbrication des enceintes.
     * @param dataAsTable La table où les lignes de cellules doivent être ajoutées.
     */

    private void recursivelyAddEntriesToTable(Map<Integer, Enceinte> positionMap,
       int numberOfPlaces, int niveau, DataAsTable dataAsTable, int nbNiveauDeEnceinte){

        int columnIndex = niveau - 1; // Index de colonne basé sur le niveau actuel

        // Boucle sur toutes les places disponibles
        for(int i = 1; i <= numberOfPlaces; i++){

            Enceinte enceinte = positionMap.get(i); // Récupération de l'enceinte associée à la place i

            // Écriture de l'enceinte dans une ligne de cellules
            CellRow enceinteCellRow = addEnceinteToRow(enceinte, columnIndex);
            dataAsTable.addCellRow(enceinteCellRow); // Ajout de la ligne au tableau (dataAsTable)
            int numberOfNiveauATraiter = nbNiveauDeEnceinte - 1; // Calcul du nombre de niveaux restants à traiter
            for(int j = 0; j < numberOfNiveauATraiter; j++){
                if (enceinte != null ){
                    int subEnceintePlacesCount = enceinte.getNbPlaces(); // Récupération du nombre de places pour les sous-enceintes

                    // Appel récursif pour écrire les sous-enceintes
                    recursivelyAddEntriesToTable(retrieveSubEnceintes(enceinte),  subEnceintePlacesCount,
                       niveau + 1, dataAsTable, nbNiveauDeEnceinte - 1);
                }

            }
        }
    }

    /**
     * Récupère les sous-enceintes d'une enceinte parente.
     *
     * Cette méthode utilise le gestionnaire d'enceintes pour récupérer la liste des sous-enceintes
     * d'une enceinte parente donnée, puis crée une carte (Map) des sous-enceintes triées par position.
     *
     * @param parentEnceinte L'enceinte parente dont les sous-enceintes doivent être récupérées.
     * @return Une Map associant les positions des sous-enceintes à leurs objets Enceinte correspondants.
     */
    private Map<Integer, Enceinte> retrieveSubEnceintes(Enceinte parentEnceinte){
        List<Enceinte> subEnceintes = getEnceinteManager().findByEnceintePereWithOrderManager(parentEnceinte);

        return createMapEnceintesByPosition(subEnceintes);

    }

    /**
     * Ajoute une enceinte à une ligne de cellules, en ajoutant des cellules vides
     * avant celle-ci en fonction du nombre spécifié.
     *
     * @param enceinte L'enceinte à ajouter à la ligne.
     * @param numEmptyCellsBefore Le nombre de cellules vides à insérer avant l'enceinte.
     * @return La ligne de cellules créée contenant l'enceinte.
     */
    public CellRow addEnceinteToRow(Enceinte enceinte, int numEmptyCellsBefore){
        DataCell enceinteDataCell;
        // Si l'enceinte est non nulle, créer une cellule de données pour l'enceinte
        if(enceinte != null){
            enceinteDataCell = createDataCellFromEnceinte(enceinte);
        }else{
            // Si l'enceinte est nulle, créer une cellule vide
            enceinteDataCell = new DataCell(EMPTY_CELL_CONTENT);

        }
        // Ajouter des cellules vides avant la cellule d'enceinte en fonction du nombre spécifié
        return fillRowWithCells(enceinteDataCell, numEmptyCellsBefore);

    }

    /**
     * Crée le contenu d'une cellule pour une enceinte donnée.
     *
     * @param enceinte L'enceinte pour laquelle créer le contenu.
     * @return Un objet CellContent contenant les informations de l'enceinte.
     */
    public DataCell createDataCellFromEnceinte(Enceinte enceinte){
        // Récupère la couleur de l'enceinte (si disponible)
        Couleur couleurEnceinte = enceinte.getCouleur();
        String color = (couleurEnceinte != null ? couleurEnceinte.getHexa() : null);

        // Récupère l'alias de l'enceinte (ou une chaîne vide si l'alias est nul)
        String alias = (enceinte.getAlias() != null ? enceinte.getAlias(): "");

        // Crée le contenu de la cellule avec le nom et l'alias de l'enceinte
        CellContent cellContent = new CellContent(enceinte.getNom(), alias, true, false);

        // Retourne l'objet DataCell contenant le contenu de la cellule et la couleur
        return new DataCell(cellContent, color);

    }



}
