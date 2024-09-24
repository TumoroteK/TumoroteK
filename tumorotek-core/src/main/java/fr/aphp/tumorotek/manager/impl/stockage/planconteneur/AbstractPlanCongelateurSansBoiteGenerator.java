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
import java.util.Optional;


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
     * @param level Le nombre de cellules vides à ajouter avant la cellule de données.
     * @return La ligne de cellules complétée.
     */
    //Corinne :
    //Lorsqu'on définit une méthode spécifique, il faut faire abstraction de l'appelabt. Ainsi, le paramètre int ne doit pas s'appeler
    //level car ça ne correspond à rien dans cette méthode. level doit plutôt s'appeler nbCellToAdd
    //Par ailleurs, ces cellules à ajouter ne correspondent à rien donc il faudrait mieux ajouter null
    private CellRow fillRowWithCells(DataCell datacell, int level) {
        CellRow cellRow = new CellRow();
        // Ajouter des cellules vides en fonction du niveau spécifié
        for (int i = 0; i < level; i++) {
            cellRow.addDataCell(DataCell.createEmptyDataCell());//Corinne : méthode static incorrecte
        }
        // Ajouter la cellule de données passée en paramètre
        cellRow.addDataCell(datacell);

        return cellRow;
    }


    /**
     * Crée une Map de position associant des positions d'enceintes à leurs objets respectifs.
     * Cela facilite la gestion et l'accès aux enceintes en fonction de leurs positions.
     * @param enceintes La liste d'enceintes à mapper.
     * @return Une map associant les positions aux enceintes.
     */

    public Map<Integer, Enceinte> mapEnceintesByPosition(List<Enceinte> enceintes) {
        Map<Integer, Enceinte> positionMap = new HashMap<>();

        if (enceintes != null) {
            for (Enceinte enceinte : enceintes) {
                positionMap.put(enceinte.getPosition(), enceinte);
            }
        }

        return positionMap;
    }


    /**
     * Écrit les enceintes dans une structure de tableau en fonction de leurs positions
     * et sous-enceintes.
     *
     * @param positionMap Une carte des positions des enceintes.
     * @param numberOfPlaces Le nombre de places disponibles dans l'enceinte.
     * @param level Le niveau d'imbrication des enceintes.
     * @param dataAsTable La table où les lignes de cellules doivent être ajoutées.
     */

    private void writeEnceintes(Map<Integer, Enceinte> positionMap,
                                int numberOfPlaces, int level, DataAsTable dataAsTable) {
        // Boucle sur toutes les places disponibles
        for (int i = 1; i <= numberOfPlaces; i++) {
            Enceinte enceinte = positionMap.get(i);

            // Écriture de l'enceinte dans une ligne de cellules
            //Corinne : cette méthode crée la row et ajoute l'enceinte 
            //le terme insert fait penser à la base de données. Il faudrait plutôt la nommer createRowAndAddEnceinte ou addEnceinteIntoNewRow. 
            // Attention, cette méthode donne des rows de longueur différente. Ce n'est pas "normal" pour un tableau donc ça pourrait générer un bug
            //il faut prévoir de compléter les lignes ...
            CellRow enceinteCellRow = insertEnceinteIntoRow(enceinte, level);
            dataAsTable.addCellRow(enceinteCellRow);

            // Si l'enceinte n'est pas la dernière, écrire ses sous-enceintes
            //Corinne : la méthode checkLastEnceinte est un traitement assez lourd donc dans une boucle, ce n'est pas optimal
            //il faut voir pour s'en passer, par exemple en ayant le nombre de niveaux du conteneur en paramètre
            if (enceinte != null && !getEnceinteManager().checkLastEnceinte(enceinte)) {
                int numberOfPlaces2 = enceinte.getNbPlaces();
                // Appel récursif pour écrire les sous-enceintes
                writeEnceintes(retrieveSubEnceintes(enceinte), numberOfPlaces2, level + 1, dataAsTable);
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

        return mapEnceintesByPosition(subEnceintes);

    }

    //Corinne : l'attribut level n'est pas facile à comprendre :
    //soit il s'agit du niveau de l'enceinte (et non du niveau auquel l'enceinte doit être écrite) : on s'appuie donc 
    //sur une caractéristique métier qu'il faut appeler comme partout dans l'application, donc en français : niveau
    //soit il s'agit de la colonne du tableau dans laquelle l'enceinte doit être écrite. Dans ce cas là, il faut l'appeler indexColonne ou numeroColonne
    //selon que la 1ere valeur est 0 ou 1.
    //cette méthode ressemble beaucoup à CellRow.addDataCell(int indexColonne, DataCell dataCell) proposée pour la partie avec boîte
    //Par contre dans le cas des sans boîtes, il n'y a qu'une valeur par ligne donc il faut prévoir des null après pour que toutes les lignes aient la même size
    //comme c'est attendu dans un tableau. Il faut alors en paramètre le nombre de niveaux du conteneur
    /**
     * Écrit une enceinte dans une ligne de cellules en ajoutant des cellules vides
     * selon le niveau.
     *
     * @param enceinte L'enceinte à écrire.
     * @param level Le niveau auquel l'enceinte doit être écrite.
     * @return La ligne de cellules créée avec l'enceinte.
     */
    public CellRow insertEnceinteIntoRow(Enceinte enceinte, int level) {
        DataCell enceinteDataCell;
        // Si l'enceinte est non nulle, créer une cellule de données pour l'enceinte
        if (enceinte != null) {
            enceinteDataCell = createDataCellFromEnceinte(enceinte);
        } else {
            // Si l'enceinte est nulle, créer une cellule vide
            enceinteDataCell = createEmptyPositionDataCell();

        }
        // Ajouter des cellules vides avant la cellule d'enceinte en fonction du niveau
        return fillRowWithCells(enceinteDataCell, level);

    }


    /**
     * Crée le contenu d'une cellule pour une enceinte donnée.
     *
     * @param enceinte L'enceinte pour laquelle créer le contenu.
     * @return Un objet CellContent contenant les informations de l'enceinte.
     */
    public DataCell createDataCellFromEnceinte(Enceinte enceinte){
        String color = Optional.ofNullable(enceinte.getCouleur())
                .map(Couleur::getHexa)
                .orElse("");//Corinne : il faudrait mieux null que ""....

        return new DataCell(buildCellContentForEnceinte(enceinte), color);

         //Corinne : Optional est une "value-class" pensée pour "typer" les objets "retournés".
         //Il n'est pas approprié dans le cas de l'utilisation présente.
         //Le code ci-dessous hyper basique suffit : 
//       Couleur couleurEnceinte = enceinte.getCouleur();
//       String color = (couleurEnceinte != null ? couleurEnceinte.getHexa() : null);
//       CellContent cellContent = new CellContent(enceinte.getNom(), enceinte.getAlias(), true, false);
//       return new DataCell(cellContent, color);
       
    }

    /**
     * Crée une cellule vide pour une position vide.
     *
     * @return Une cellule de données vide.
     */
    //Corinne : 
    //Nom de méthode pas facilement compréhensible alors que l'implémentation est assez parlante
    //Par conséquent, créer cette méthode complique la lecture du code d'appel
    //Comme elle n'est appelée qu'une seule fois et n'appelle qu'une seule méthode, elle n'est pas nécessaire
    //Si il y a un besoin de créer une méthode pour instancier différents "type" de DataCell, il faut passer par une factory
    //NB : c'est bien d'être passé par une constante, ça semble suffisant pour la "mutualisation" si
    //la création de plusieurs DataCell devait être appelée dans différentes méthodes 
    public DataCell createEmptyPositionDataCell(){
        return new DataCell(EMPTY_CELL_CONTENT);

    }

    /**
     * Crée le contenu d'une cellule pour une enceinte donnée.
     *
     * @param enceinte L'enceinte pour laquelle créer le contenu.
     * @return Un objet CellContent contenant les informations de l'enceinte.
     */
    //Corinne : cette méthode est appelée à un seul endroit et appelle une seule méthode donc ce n'est pas nécessaire de la définir
    //dans ce cas, il vaut mieux privilégier la "simplification de la lecture du code" plutôt que la mutualisation inutile pour le moment.
    public CellContent buildCellContentForEnceinte(Enceinte enceinte){
        return new CellContent(enceinte.getNom(), enceinte.getAlias(), true, false);

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
        // represent all the data part
        DataAsTable dataAsTable = new DataAsTable();

        //Corinne : enceintes de 1er niveau (leur père est le conteneur) => donner un nom plus parlant à la liste d'enceintes
        List<Enceinte> enceintes = getEnceinteManager().findByConteneurWithOrderManager(conteneur);
        //Corinne : la valeur récupérée correspond au nombre de niveaux donc ce serait plus parlant de reprendre
        //la même terminologie : int nbrNiveaux plutôt que numberOfPlaces 
        int numberOfPlaces = conteneur.getNbrNiv();

        //Corinne : j'ai été un peu perturbé par le nom de méthode mapEnceintesByPosition qui fait
        //plus penser à un nom de variable.
        //mapEnceintesByPosition pour la map et createMapEnceintesByPosition pour la méthode me semble moins ambigu
        Map<Integer, Enceinte> positionMap = mapEnceintesByPosition(enceintes);
        //Corinne : write laisse à penser qu'on écrit quelque chose, ce qui n'est pas le cas
        //buildEnceinteRow serait plus parlant
        writeEnceintes(positionMap, numberOfPlaces, 0, dataAsTable);
        return dataAsTable;
    }



}
