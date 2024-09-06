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
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Classe abstraite pour la génération de plans de congélateurs avec boîte.
 *
 * Cette classe étend {@link PlanCongelateurAvecBoiteGenerator} et fournit une
 * implémentation spécifique pour la génération de plans de congélateurs qui
 * sans boîtes. Elle est conçue pour être étendue par des classes concrètes
 * qui doivent définir la logique spécifique à la génération des détails du plan.
 *
 */

public abstract class PlanCongelateurSansBoiteGenerator extends PlanCongelateurAvecBoiteGenerator {

    private ConteneurManager conteneurManager;

    public void setConteneurManager(ConteneurManager conteneurManager) {
        this.conteneurManager = conteneurManager;
    }

    /**
     * La méthode buildDetailPlan construit un DataAsArray basé sur les enceintes d'un conteneur.
     * @param conteneur
     * @param locale
     * @return
     */
    @Override
    public DocumentData buildDetailPlan(Conteneur conteneur, Locale locale) {
        // Initialise une liste pour stocker les lignes de cellules.
        List<CellRow> cellRows = new ArrayList<>();
//        Set<Enceinte> enceintes = conteneurManager.getEnceintesManager(conteneur);
//        List<Enceinte> enceintes2 = conteneurManager.getContainingEnceinteManager(conteneur);

        // Appelle une méthode privée pour remplir cette liste en fonction des enceintes du conteneur.
        buildCellRows(conteneur.getEnceintes(), cellRows, 0, locale);
        // Crée et retourne un DataAsArray à partir des lignes de cellules générées.
        return new DataAsTable(cellRows);
    }

    private void buildCellRows(Set<Enceinte> enceintes, List<CellRow> cellRows, int level, Locale locale) {
        // Trie les enceintes par position pour assurer un affichage ordonné et boucle.
        for (Enceinte enceinte : enceintes.stream()
                .sorted(Comparator.comparingInt(Enceinte::getPosition))
                .collect(Collectors.toList())) {
            // Vérifie si l'enceinte est vide. Si c'est le cas, ajoute une cellule "vide".
            if (isEncienteEmpty(enceinte)) {
                String text = locale.getLanguage().equalsIgnoreCase("en") ? "empty" : "vide";
                CellContent content = new CellContent(text, "", false, false);
                DataCell emptyCell = new DataCell(content);
                cellRows.add(new CellRow(Collections.singletonList(emptyCell)));
            }
            // Crée le contenu de la cellule pour l'enceinte actuelle avec les attributs spécifiés.

            CellContent content = new CellContent(enceinte.getNom(), enceinte.getAlias(), true);
            String borderLeftColor = enceinte.getCouleur().getHexa();
            DataCell dataCell = new DataCell(content, borderLeftColor);

            // Si l'enceinte a des sous-enceintes, traite-les récursivement.
            if (!enceinte.getEnceintes().isEmpty()) {
                List<CellRow> subEncienteRows = new ArrayList<>();
                buildCellRows(enceinte.getEnceintes(), subEncienteRows, level + 1, locale);

                // Crée des cellules vides pour aligner les sous-enceintes à leur niveau.

                List<DataCell> emptyCells = Collections.nCopies(level + 1, createEmptyDataCell());
                // Ajoute les lignes des sous-enceintes à cellRows en fonction du niveau de profondeur.

                for (int i = 0; i < subEncienteRows.size(); i++) {
                    CellRow rowData = new CellRow();
                    if (i == 0) {
                        // For the first row, create a list with an empty cell and a specific data cell
                        rowData.addDataCell(createEmptyDataCell()); // Add empty cell
                        rowData.addDataCell(dataCell); // Add specific data cell
                    } else {
                        // For subsequent rows, add empty cells and a specific data cell
                        rowData.addAllDataCells(emptyCells); // Create a list from emptyCells
                        rowData.addDataCell(dataCell); // Add specific data cell

                    }
                    cellRows.add(rowData); // Add the constructed CellRow to cellRows
                }

            } else {
                // Si l'enceinte n'a pas de sous-enceintes, ajoute simplement la cellule à cellRows.

                cellRows.add(new CellRow(Collections.singletonList(dataCell)));
            }
        }
    }

    // Vérifie si l'enceinte est vide en comparant le nombre de places avec la somme des positions.

    private boolean isEncienteEmpty(Enceinte enceinte) {
        // Get the positions of all sub-enseintes
        Set<Integer> positions = enceinte.getEnceintes().stream()
                .map(Enceinte::getPosition)
                .filter(Objects::nonNull)  // Filter out null positions
                .collect(Collectors.toSet());

        // Calculate the sum of positions
        int nbPlacesSum = positions.stream().reduce(0, Integer::sum);

        // The enceinte is considered empty if the sum of positions is less than the number of places
        return nbPlacesSum < enceinte.getNbPlaces();
    }




    private DataCell createEmptyDataCell() {
        // Crée une cellule vide avec un contenu par défaut.
        return new DataCell(new CellContent("", "", false, false));
    }
}