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
package fr.aphp.tumorotek.manager.io.document.detail.table;


import java.util.ArrayList;
import java.util.List;


/**
 * La classe représente une rangée de cellules dans un tableau ou une feuille de calcul.
 *
 * <p>Elle contient une liste de cellules, chacune étant un objet {@link DataCell}. Cette classe permet de
 * regrouper et gérer les cellules d'une même rangée.</p>
 *
 * <p><b>Exemple d'utilisation :</b></p>
 * <pre>{@code
 * List<DataCell> cells = new ArrayList<>();
 * CellRow row = new CellRow();
 * row.listDataCell = cells;
 * }</pre>
 */
public class CellRow {
    private List<DataCell> listDataCell;

    private int nbDataCell = 0;

    public CellRow() {
        this.listDataCell = new ArrayList<>();
    }


    public CellRow(List<DataCell> listDataCell) {
        this.listDataCell = listDataCell;
    }

    public void addDataCell(DataCell dataCell) {
        listDataCell.add(dataCell);
        nbDataCell++;
    }

    public void addDataCell(int indexColonne, DataCell dataCell) {
        while (nbDataCell < indexColonne) { // "<" car on veut s'arrêter juste avant la colonne concernée
            addDataCell(null);
        }
        addDataCell(dataCell);
    }

    public int getNbDataCell() {
        return nbDataCell;
    }

    /**
     * Ajoute plusieurs cellules à la rangée.
     *
     * @param cells les cellules à ajouter.
     */
    public void addAllDataCells(List<DataCell> cells) {
        listDataCell.addAll(cells);
    }

    public List<DataCell> getListDataCell() {
        return listDataCell;
    }

    public void setListDataCell(List<DataCell> listDataCell) {
        this.listDataCell = listDataCell;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Check if listDataCell is not null and not empty
        if (listDataCell != null && !listDataCell.isEmpty()) {
            // Iterate through each DataCell in the list
            for (DataCell cell : listDataCell) {
                // Ensure cell is not null before accessing its methods
                if (cell != null) {
                    // Add cell content to StringBuilder
                    sb.append(cell.getCellContent()).append(" ");

                    // Check if the cell has colspan greater than 1
                    if (cell.getColspan() > 1) {
                        // Add tabs for each additional colspan
                        for (int i = 1; i < cell.getColspan(); i++) {
                            sb.append("\t");
                        }
                    }

                    sb.append("| ");
                } else {
                    // Handle case where DataCell is null, e.g., append placeholder text or skip.
                    sb.append("empty cell").append("| ");
                }
            }

            // Remove last two characters ("| ") for formatting only if there's content added.
            if (sb.length() >= 2) {
                sb.setLength(sb.length() - 2);
            }

        } else {
            sb.append("[empty row]");
        }

        return sb.toString();
    }
}



