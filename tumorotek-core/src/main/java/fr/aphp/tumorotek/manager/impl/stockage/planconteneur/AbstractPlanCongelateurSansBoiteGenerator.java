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


    protected EnceinteManager enceinteManager;

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
     * Construit un DataAsArray basé sur les enceintes d'un conteneur.
     *
     * @param conteneur
     * @param locale
     * @return
     */
    @Override
    public DocumentData buildDetailPlan(Conteneur conteneur, Locale locale) {

        // Initialisation d'un nouvel objet DataAsTable.
        DataAsTable dataAsTable = new DataAsTable();

        // Récupération des enceintes associées au conteneur et tri des enceintes.
        List<Enceinte> encientes = getEnceinteManager().findAllEnceinteByConteneurManager(conteneur);
        //        sortEnceintesRecursively(encientes);

        // Traitement de chaque enceinte.
        for (Enceinte enceinte : encientes) {

            //  Création des attributs de style et du contenu de cellule.
            StylingAttributes stylingAttributes = new StylingAttributes();
            stylingAttributes.setSecondTextInItalic(true);
            stylingAttributes.setBorderLeftColor(enceinte.getCouleur().getHexa());

            CellContent cellContent = new CellContent(
                    enceinte.getPosition() == null ? "vide" : enceinte.getNom(),
                    enceinte.getAlias(),
                    stylingAttributes
            );

            // Création de la cellule de données.
            DataCell dataCell = new DataCell(cellContent, stylingAttributes);

            // Création d'une ligne de cellules pour l'enceinte.
            // La méthode createCellRow détermine combien de DataCell doivent être ajoutées en fonction du niveau de l'enceinte.
            CellRow cellRow = createCellRow(dataCell, getEnceinteManager().getLevelEnceinte(enceinte));

            // Ajout de la ligne de cellules au tableau de données.
            dataAsTable.addCellRow(cellRow);
        }

        // Retour de l'objet DataAsTable complété.
        return dataAsTable;
    }


//    public void sortEnceintesRecursively(List<Enceinte> enceintes) {
//        // sort the list of enceintes at the current level
//        Collections.sort(enceintes, Comparator.comparingInt(Enceinte::getPosition));
//
//        //  sort each sub-enceinte recursively
//        for (Enceinte enceinte : enceintes) {
//            Set<Enceinte> subEnceintes = enceinte.getEnceintes(); // Assume getSubEnceintes() returns the sub-enceintes withot lazy loading problems
//            if (subEnceintes != null) {
//                sortEnceintesRecursively(subEnceintes);
//            }
//        }
//    }

    private CellRow createCellRow(DataCell datacell, int level) {
        List<DataCell> dataCellList = new ArrayList<>();

        for (int i = 0; i < level; i++) {
            if (i == level - 1) {
                dataCellList.add(datacell); // Add the provided datacell at the last position
            } else {
                dataCellList.add(createEmptyDataCell()); // Add empty DataCells for other positions in the row
            }
        }
        return new CellRow(dataCellList);
    }

    private DataCell createEmptyDataCell() {
        // Crée une cellule vide avec un contenu par défaut.
        return new DataCell(new CellContent(""));
    }
}