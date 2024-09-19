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


    private static final String EMPTY_CELL_CONTENT = "vide";

    abstract protected EnceinteManager getEnceinteManager();

    private CellRow addToCellRowWithEmptyPlaces(DataCell datacell, int level) {
        CellRow cellRow = new CellRow();
        for (int i = 0; i < level; i++) {
            cellRow.addDataCell(createEmptyDataCell());
        }
        cellRow.addDataCell(datacell);

        return cellRow;
    }


    public Map<Integer, Enceinte> createPositionMap(List<Enceinte> enceintes){
        if (enceintes == null) {
            throw new IllegalArgumentException("List of enceintes cannot be null");
        }
        Map<Integer, Enceinte> positionMap = new HashMap<>();

        for (Enceinte enceinte : enceintes) {
            positionMap.put(enceinte.getPosition(), enceinte);
        }
        return positionMap;
    }



    private void writeEnceintes(Map<Integer, Enceinte> positionMap, int numberOfPlaces, int niveau, DataAsTable dataAsTable) {

        for (int i = 1; i <= numberOfPlaces; i++) {
            Enceinte enceinte = positionMap.get(i);

            CellRow enceinteCellRow = writeEnceinteToCellRow(enceinte, niveau);
            dataAsTable.addCellRow(enceinteCellRow);

            if (enceinte != null && !getEnceinteManager().checkLastEnceinte(enceinte)) {
                int numberOfPlaces2 = enceinte.getNbPlaces();

                writeEnceintes(getSubEnceintes(enceinte), numberOfPlaces2, niveau + 1, dataAsTable);
            }
        }
    }


    private Map<Integer, Enceinte> getSubEnceintes(Enceinte parentEnceinte) {
        List<Enceinte> subEnceintes = getEnceinteManager().findByEnceintePereWithOrderManager(parentEnceinte);

        return createPositionMap(subEnceintes);

    }

    /**
     * This reporeenst a line that is reasy to be added to DataAsTable
     * *
     * /
     *
     **/
    public CellRow writeEnceinteToCellRow(Enceinte enceinte, int level) {
        DataCell enceinteDataCell;
        if (enceinte != null) {
            // create datacell for the enceintes
            enceinteDataCell = createEnceinteDataCell(enceinte);
        } else {
            enceinteDataCell = createEmptyPositionDataCell();

        }
        // add the empty datacell before it with addToCellRowWithEmptyPlaces
        return addToCellRowWithEmptyPlaces(enceinteDataCell, level);

    }



    public DataCell createEnceinteDataCell(Enceinte enceinte){
        String color = Optional.ofNullable(enceinte.getCouleur())
                .map(Couleur::getHexa)
                .orElse("");
        return new DataCell(createCellContent(enceinte), color);

    }

    public DataCell createEmptyPositionDataCell(){
        return new DataCell(EMPTY_CELL_CONTENT);

    }

    public CellContent createCellContent(Enceinte enceinte){
        return new CellContent(enceinte.getNom(), enceinte.getAlias(), true, false);

    }


    /**
     * Builds a detailed plan representation of a container by organizing its
     * compartments and sub-compartments in a structured manner.
     *
     * @param conteneur The container for which the detail plan is to be built.
     * @return DataAsTable representing the detailed plan structure of the container.
     */
    @Override
    public DocumentData buildDetailPlan(Conteneur conteneur) {
        // represent all the data part
        DataAsTable dataAsTable = new DataAsTable();

        List<Enceinte> enceintes = getEnceinteManager().findByConteneurWithOrderManager(conteneur);
        int numberOfPlaces = conteneur.getNbrNiv();

        Map<Integer, Enceinte> positionMap = createPositionMap(enceintes);
        writeEnceintes(positionMap, numberOfPlaces, 0, dataAsTable);
        return dataAsTable; // Return the final table after recursion
    }




    private DataCell createEmptyDataCell() {
        return new DataCell(new CellContent(""));
    }
}