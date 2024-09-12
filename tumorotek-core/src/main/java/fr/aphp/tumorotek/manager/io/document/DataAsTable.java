/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 *
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 *
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
 *
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
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/

package fr.aphp.tumorotek.manager.io.document;

import fr.aphp.tumorotek.manager.io.document.detail.table.CellRow;

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
 */

public class DataAsTable implements DocumentData {
    private List<CellRow> listCellRow;

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
        if (cellRow != null){
            this.listCellRow.add(cellRow);

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DataAsTable[\n");
        for (CellRow cellRow : listCellRow) {
            sb.append(cellRow.toString()).append("\n");
        }
        sb.append("]");
        return sb.toString();
    }
}