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
 * Représente une rangée de cellules dans un tableau ou une feuille de calcul.
 *
 * <p>Cette classe contient une liste de cellules, chacune étant un objet {@link DataCell}. Elle permet
 * de regrouper et de gérer les cellules d'une même rangée.</p>
 *
 * <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */
public class CellRow {

    // Liste des cellules contenues dans cette rangée.
    private List<DataCell> listDataCell;

    // Nombre de cellules dans cette rangée.
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

    /**
     * Ajoute une cellule à une position spécifiée dans la rangée.
     * Si nécessaire, des cellules nulles sont ajoutées pour combler l'espace.
     *
     * @param indexColonne la position de la cellule à ajouter.
     * @param dataCell la cellule à ajouter.
     */
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
        if (listDataCell == null || listDataCell.isEmpty()) {
            return "| <empty row> |";
        }

        StringBuilder sb = new StringBuilder();

        // Process each cell in the row
        for (DataCell cell : listDataCell) {
            sb.append("| ");
            sb.append(cell != null ? cell.toString() : "(vide)");
            sb.append(" ");
        }
        sb.append("|");

        return sb.toString();
    }

    public String getHorizontalBorder() {
        if (listDataCell == null) {
            return "+---------------+";
        }

        StringBuilder sb = new StringBuilder();
        for (DataCell cell : listDataCell) {
            sb.append("+");
            int length = 15; // base length for each cell
            if (cell != null && cell.getColspan() > 1) {
                length = length * cell.getColspan();
            }
            for (int i = 0; i < length; i++) {
                sb.append("-");
            }
        }
        sb.append("+");

        return sb.toString();
    }
}



