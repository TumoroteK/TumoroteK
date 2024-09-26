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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Classe abstraite pour la génération de plans de congélateurs avec boîte.
 * <p>
 * Cette classe étend {@link AbstractPlanCongelateurGenerator} et fournit une
 * implémentation spécifique pour la génération de plans de congélateurs qui
 * sans boîtes. Elle est conçue pour être étendue par des classes concrètes
 * qui doivent définir la logique spécifique à la génération des détails du plan.
 */

public abstract class AbstractPlanCongelateurSansBoiteGenerator extends AbstractPlanCongelateurGenerator {

    // Constante représentant le contenu d'une cellule vide
    private static final String EMPTY_CELL_CONTENT = "vide";

    // Méthode abstraite pour récupérer le gestionnaire d'enceinte
    protected abstract EnceinteManager getEnceinteManager();

    /**
     * Ajoute des cellules vides à une ligne de cellules jusqu'à un certain niveau,
     * puis ajoute une cellule de données.
     *
     * @param datacell La cellule de données à ajouter.
     * @param nbCellToAdd Le nombre de cellules vides à ajouter avant la cellule de données.
     * @return La ligne de cellules complétée.
     */
    private CellRow fillRowWithCells(DataCell datacell, int nbCellToAdd) {
        // Crée une nouvelle instance de CellRow pour contenir les cellules.

        CellRow cellRow = new CellRow();
        // Ajouter des cellules vides en fonction du niveau spécifié
        for (int i = 0; i < nbCellToAdd; i++) {
            DataCell dataCell = new DataCell(new CellContent(null));
            cellRow.addDataCell(dataCell);
        }
        // Ajouter la cellule de données passée en paramètre
        cellRow.addDataCell(datacell);

        return cellRow;
    }

    /**
     * Vérifie si l'enceinte donnée est la dernière sous-enceinte par rapport à son enceinte parent.
     *
     * @param enceinte L'enceinte dont il faut vérifier si elle est la dernière sous-enceinte.
     * @param lastEnceinteCache Cache pour éviter des vérifications redondantes sur les enceintes parentes.
     *
     * @return true si c'est la dernière enceinte, sinon false.
     */
    public boolean isLastEncienteByPere(final Enceinte enceinte, Map<Enceinte, Boolean> lastEnceinteCache) {
        // Récupérer l'enceinte parent
        Enceinte pere = enceinte.getEnceintePere();
        if (pere != null) {
            // Vérifier si déjà mis en cache.Nous vérifions si le résultat a déjà été calculé pour cet parent particulier dans notre cache.
            // Cela permet d'éviter des appels redondants aux méthodes coûteuses lorsqu'elles ont déjà été exécutées auparavant.
            if (lastEnceinteCache.containsKey(pere)) {
                return lastEnceinteCache.get(pere);
            } else {
                Boolean isLast = getEnceinteManager().checkLastEnceinte(enceinte);
                lastEnceinteCache.put(pere, isLast);
                return isLast;
            }
        }
        // Si aucun père n'existe, retourner false
        return false;
    }

    /**
     * Crée une Map de position associant des positions d'enceintes à leurs objets respectifs.
     * Cela facilite la gestion et l'accès aux enceintes en fonction de leurs positions.
     * @param enceintes La liste d'enceintes à mapper.
     * @return Une map associant les positions aux enceintes.
     */

    public Map<Integer, Enceinte> createMapEnceintesByPosition(List<Enceinte> enceintes) {
        // Créer une nouvelle Map vide pour stocker les associations entre positions et enceintes
        Map<Integer, Enceinte> positionMap = new HashMap<>();

        // Vérifier que le paramètre liste n’est pas nul
        if (enceintes != null) {
            for (Enceinte enceinte : enceintes) {
                // Insérer chaque enceinte dans map selon sa position
                positionMap.put(enceinte.getPosition(), enceinte);
            }
        }

        return positionMap;
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

    private void populateDataAsTableWithEnceintes(Map<Integer, Enceinte> positionMap,  Map<Enceinte, Boolean> lastEnceinteCache,
                                                  int numberOfPlaces, int niveau, DataAsTable dataAsTable) {
        // Boucle sur toutes les places disponibles
        for (int i = 1; i <= numberOfPlaces; i++) {

            Enceinte enceinte = positionMap.get(i);

            // Écriture de l'enceinte dans une ligne de cellules
            CellRow enceinteCellRow = createRowAndAddEnceinte(enceinte, niveau);
            dataAsTable.addCellRow(enceinteCellRow);
            // Si l'enceinte n'est pas la dernière, écrire ses sous-enceintes
            if (enceinte != null && !isLastEncienteByPere(enceinte, lastEnceinteCache)) {
                int subEnceintePlacesCount = enceinte.getNbPlaces();
                // Appel récursif pour écrire les sous-enceintes
                populateDataAsTableWithEnceintes(retrieveSubEnceintes(enceinte),lastEnceinteCache,  subEnceintePlacesCount, niveau + 1, dataAsTable);
            }
        }
    }

    /**
     * Récupère les sous-enceintes d'une enceinte parent.
     *
     * @param parentEnceinte L'enceinte parent dont les sous-enceintes doivent être récupérées.
     * @return Une carte des positions des sous-enceintes.
     */
    private Map<Integer, Enceinte> retrieveSubEnceintes(Enceinte parentEnceinte) {
        List<Enceinte> subEnceintes = getEnceinteManager().findByEnceintePereWithOrderManager(parentEnceinte);

        return createMapEnceintesByPosition(subEnceintes);

    }

    /**
     * Écrit une enceinte dans une ligne de cellules en ajoutant des cellules vides
     * selon le niveau.
     *
     * @param enceinte L'enceinte à écrire.
     * @param niveau Le niveau auquel l'enceinte doit être écrite.
     * @return La ligne de cellules créée avec l'enceinte.
     */
    public CellRow createRowAndAddEnceinte(Enceinte enceinte, int niveau) {
        DataCell enceinteDataCell;
        // Si l'enceinte est non nulle, créer une cellule de données pour l'enceinte
        if (enceinte != null) {
            enceinteDataCell = createDataCellFromEnceinte(enceinte);
        } else {
            // Si l'enceinte est nulle, créer une cellule vide
            enceinteDataCell =  new DataCell(EMPTY_CELL_CONTENT);

        }
        // Ajouter des cellules vides avant la cellule d'enceinte en fonction du niveau
        return fillRowWithCells(enceinteDataCell, niveau);

    }


    /**
     * Crée le contenu d'une cellule pour une enceinte donnée.
     *
     * @param enceinte L'enceinte pour laquelle créer le contenu.
     * @return Un objet CellContent contenant les informations de l'enceinte.
     */
    public DataCell createDataCellFromEnceinte(Enceinte enceinte){
       Couleur couleurEnceinte = enceinte.getCouleur();
       String color = (couleurEnceinte != null ? couleurEnceinte.getHexa() : null);
       CellContent cellContent = new CellContent(enceinte.getNom(), enceinte.getAlias(), true, false);
       return new DataCell(cellContent, color);

    }




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
    public DocumentData buildDetailPlan(Conteneur conteneur) {
        // Initialisation d'une table de données pour stocker la structure du conteneur.
        DataAsTable dataAsTable = new DataAsTable();

        // Récupération des enceintes de niveau 1, ce qui est essentiel pour construire la représentation du conteneur.
        List<Enceinte> enceintesNiveau1 = getEnceinteManager().findByConteneurWithOrderManager(conteneur);

        // Obtenir le nombre max d’enceintes attendues (
        int nbrNiveauxEnceinte = conteneur.getNbrEnc();

        // Utiliser un cache pour économiser les appels à checkLastEnceinte
        Map<Enceinte, Boolean> lastEnceinteCache = new HashMap<>();

        // Création d'une carte pour associer chaque enceinte à sa position, facilitant l'accès rapide lors de l'ajout dans la table.
        Map<Integer, Enceinte> positionMap = createMapEnceintesByPosition(enceintesNiveau1);

        // Remplissage de la table de données avec les enceintes en fonction de leur position, ce qui constitue le cœur de la méthode.
        populateDataAsTableWithEnceintes(positionMap,lastEnceinteCache,  nbrNiveauxEnceinte, 0, dataAsTable);


        // Retourne la table de données construite pour une utilisation ultérieure.
        return dataAsTable;
    }



}
