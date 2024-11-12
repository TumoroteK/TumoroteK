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

package fr.aphp.tumorotek.manager.io.document;

import fr.aphp.tumorotek.manager.io.document.detail.table.CellRow;
import fr.aphp.tumorotek.manager.io.document.detail.table.DataCell;

import java.util.ArrayList;
import java.util.List;


/**
 * La classe <code>DataAsArray</code> représente un ensemble de données structurées sous forme de tableau.
 * Elle implémente l'interface <code>DocumentData</code> et contient une liste de lignes de cellules.
 *
 * Cette classe est utilisée pour manipuler des données tabulaires dans le contexte de la gestion des documents.
 *
 * <p>La structure des données est représentée par une liste de <code>CellRow</code>, chaque <code>CellRow</code>
 * représentant une ligne du tableau.</p>
 *
 * <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */

public class DataAsTable implements DocumentData {
    private List<CellRow> listCellRow;

    // Stocke le nombre de cellules par ligne pour éviter d'appeler .size() à chaque ajout de DataCell
    // ce qui permet d'optimiser l'accès à la dernière ligne existante.
    private int nbCellRow = 0;


    public DataAsTable() {
        this.listCellRow = new ArrayList<>();
    }

    public DataAsTable(List<CellRow> listCellRow) {
        this.listCellRow = listCellRow;
    }

    public List<CellRow> getListCellRow() {
        return listCellRow;
    }

    public void setListCellRow(List<CellRow> listCellRow) {
        this.listCellRow = listCellRow;
    }

    public void addCellRow(CellRow cellRow) {
        listCellRow.add(cellRow);
        nbCellRow++;
    }


    /**
     * Ajoute une cellule de données à une ligne spécifique dans le tableau.
     *
     * @param dataCell     La cellule de données à ajouter.
     * @param indexLigne   L'indice de la ligne où la cellule doit être ajoutée.
     * @param indexColonne L'indice de la colonne où la cellule doit être ajoutée.
     * @throws IllegalArgumentException si les indices sont négatifs.
     */
    public void addDataCell(DataCell dataCell, int indexLigne, int indexColonne) {
        // Vérifie que les indices sont non négatifs
        if (indexLigne < 0 || indexColonne < 0) {
            throw new IllegalArgumentException("Indices must be non-negative."); // Lance une exception si les indices sont négatifs
        }
        
        // Ajoute des lignes vides si nécessaire jusqu'à atteindre l'indice de ligne spécifié
        while (nbCellRow <= indexLigne) {
            addCellRow(new CellRow()); // Ajoute une nouvelle ligne vide
        }
        
        // Récupère la ligne concernée à partir de la liste des lignes
        CellRow cellRowConcernee = getListCellRow().get(indexLigne);
        cellRowConcernee.addDataCell(indexColonne, dataCell); // Ajoute la cellule de données à la ligne spécifiée
    }




    public int getNbCellRow() {
        return nbCellRow;
    }

    @Override
    public String toString() {
        // Vérifie si la liste des lignes de cellules est vide ou nulle
        if (listCellRow == null || listCellRow.isEmpty()) {
            return "Tableau vide"; // Retourne un message indiquant que le tableau est vide
        }

        StringBuilder sb = new StringBuilder();
        sb.append("DataAsTable:\n"); // Ajoute l'en-tête pour le tableau

        // Traite chaque ligne
        for (CellRow row : listCellRow) {
            if (row != null) {
                // Ajoute la bordure horizontale de la ligne
                sb.append(row.getHorizontalBorder()).append("\n");
                // Ajoute le contenu de la ligne
                sb.append(row.toString()).append("\n");
            } else {
                sb.append("| <ligne nulle> |\n"); // Indique qu'une ligne est nulle
            }
        }

        // Ajoute la bordure horizontale finale
        if (!listCellRow.isEmpty() && listCellRow.get(listCellRow.size() - 1) != null) {
            sb.append(listCellRow.get(listCellRow.size() - 1).getHorizontalBorder()).append("\n");
        }

        return sb.toString(); // Retourne la représentation finale du tableau
    }
}
