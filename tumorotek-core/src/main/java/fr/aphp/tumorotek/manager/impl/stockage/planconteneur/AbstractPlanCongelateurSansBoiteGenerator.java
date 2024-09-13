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
import fr.aphp.tumorotek.manager.io.document.StylingAttributes;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellContent;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellRow;
import fr.aphp.tumorotek.manager.io.document.detail.table.DataCell;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.utils.TKStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


/**
 * Classe abstraite pour la génération de plans de congélateurs avec boîte.
 * <p>
 * Cette classe étend {@link AbstractPlanCongelateurGenerator} et fournit une
 * implémentation spécifique pour la génération de plans de congélateurs qui
 * sans boîtes. Elle est conçue pour être étendue par des classes concrètes
 * qui doivent définir la logique spécifique à la génération des détails du plan.
 */

public abstract class AbstractPlanCongelateurSansBoiteGenerator extends AbstractPlanCongelateurGenerator {

    private static final String FILE_PREFIX_MULTIPLE = "plans_conteneurs_sans_boites_";
    private static final String FILE_PREFIX_SINGLE = "plan_conteneur_sans_boites_";
    private static final String EMPTY_CELL_CONTENT = "vide";

    abstract protected EnceinteManager getEnceinteManager();


    @Override
    protected String getFileNamePrefix(List<Conteneur> listConteneurs) {
        if (listConteneurs == null || listConteneurs.isEmpty()) {
            return "";
        }
        String currentDate = TKStringUtils.getCurrentDate(DATE_FORMAT);
        if (listConteneurs.size() > 1) {
            return FILE_PREFIX_MULTIPLE + currentDate;
        }
        return FILE_PREFIX_SINGLE + currentDate;
    }

    /**
     * Builds a detailed plan representation of a container by organizing its
     * compartments and sub-compartments in a structured manner.
     *
     * @param conteneur The container for which the detail plan is to be built.
     * @return DataAsTable representing the detailed plan structure of the container.
     */
    @Override
    public DocumentData buildDetailPlan(Conteneur conteneur, Locale locale) {
        DataAsTable dataAsTable = new DataAsTable();

        List<Enceinte> enceintes = getEnceinteManager().findAllEnceinteByConteneurManager(conteneur);

        // Sort enceintes by position for proper arrangement
        Collections.sort(enceintes, Comparator.comparingInt(Enceinte::getPosition));

        for (Enceinte enceinte : enceintes) {
            int nbPlaces = enceinte.getNbPlaces();

            if (nbPlaces != enceintes.size()) {
                checkIfEmpty(enceinte, enceintes, nbPlaces);
            }

            CellContent cellContent = new CellContent();
            if (enceinte.getPosition() == null) {
                cellContent.setText(EMPTY_CELL_CONTENT);
            } else {
                cellContent.setText(enceinte.getNom());
                cellContent.setComplement(enceinte.getAlias());
                StylingAttributes stylingAttributes = new StylingAttributes();
                stylingAttributes.setSecondTextInItalic(true);
                DataCell dataCell = new DataCell(cellContent, stylingAttributes);

                List<DataCell> dataCells = addToCellRow(dataCell, getEnceinteManager().getLevelEnceinte(enceinte));

                // Write the CellRow for this compartment
                dataAsTable.addCellRow(new CellRow(dataCells));
            }

            // Continue recursively if there are sub-compartments
            if (!getEnceinteManager().checkLastEnceinte(enceinte)) {
                dataAsTable.addListOfCellsRows(buildDetailPlanRecursively(getEnceinteManager().findByEnceintePereWithOrderManager(enceinte), 1));
            }
        }

        return dataAsTable;
    }

    private void checkIfEmpty(Enceinte enceinte, List<Enceinte> enceintes, int nbPlaces) {
        //TODO: implement
    }

    /**
     * Recursively builds a detailed plan representation of sub-compartments within an enclosure.
     *
     * @param sousEnceintes The list of sub-compartments within an enclosure.
     * @param level         The current nesting level of the sub-compartments.
     * @return List of CellRows representing the detailed plan structure of the sub-compartments.
     */
    private List<CellRow>  buildDetailPlanRecursively(List<Enceinte> sousEnceintes, int level) {
        List<CellRow> cellRows = new ArrayList<>();
        StylingAttributes stylingAttributesText = new StylingAttributes();
        StylingAttributes stylingAttributesCell = new StylingAttributes();
        stylingAttributesText.setSecondTextInItalic(true);
        for (Enceinte sousEnceinte : sousEnceintes) {
            CellContent cellContent = new CellContent(sousEnceinte.getNom(), sousEnceinte.getAlias(), stylingAttributesText);
            if (sousEnceinte.getCouleur() != null){
                stylingAttributesCell.setBorderLeftColor(sousEnceinte.getCouleur().getHexa());
            }
            DataCell dataCell = new DataCell(cellContent, stylingAttributesCell);
            List<DataCell> dataCells = addToCellRow(dataCell, level);

            // Write the CellRow for this sub-compartment
            cellRows.add(new CellRow(dataCells));

            // Continue recursively if there are more sub-compartments
            if (!getEnceinteManager().checkLastEnceinte(sousEnceinte)) {
                cellRows.addAll(buildDetailPlanRecursively(getEnceinteManager().findByEnceintePereWithOrderManager(sousEnceinte), level + 1));
            }
        }

        return cellRows;
    }

    /**
     * Adds a given Datacell to a row at a specific level within a structured array representation.
     *
     * @param datacell The Datacell to add to the row.
     * @param level The current nesting level at which the Datacell should be added.
     * @return List of Datacells representing the row with added content at specified level.
     */
    private List<DataCell> addToCellRow(DataCell datacell, int level) {
        List<DataCell> result= new ArrayList<>();
        for(int i=0; i<level; i++){
            result.add(createEmptyDataCell());
        }
        result.add(datacell);
    
        return result;
    }

    private DataCell createEmptyDataCell() {
        return new DataCell(new CellContent(""));
    }
}