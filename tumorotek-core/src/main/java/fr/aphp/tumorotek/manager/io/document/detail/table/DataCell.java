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
package fr.aphp.tumorotek.manager.io.document.detail.table;

/**
 * La classe représente une cellule de données dans un tableau.
 *
 * <p>Cette classe encapsule les informations nécessaires pour définir le contenu et la présentation d'une cellule </p>
 *
 */
public class DataCell {
    // Le contenu de la cellule, encapsulé dans un objet CellContent
    private CellContent cellContent;

    // La couleur de la bordure gauche de la cellule, spécifiée en code hexadécimal.
    private String hexaColorCodeForLeftBorder;

    // Le nombre de colonnes que la cellule occupe, avec une valeur par défaut de 1.
    private int colspan = 1;

    // Indique si la cellule doit avoir une bordure ou non.
    private boolean withBorder;

    // Le type d'alignement du contenu de la cellule, avec une valeur par défaut.
    private AlignmentType alignmentType = AlignmentType.LEFT;

    public DataCell(CellContent cellContent) {
        this.cellContent = cellContent;
    }


    public DataCell(CellContent cellContent, String hexaColorCodeForLeftBorder, int colspan, boolean withBorder) {
        this.cellContent = cellContent;
        this.hexaColorCodeForLeftBorder = hexaColorCodeForLeftBorder;
        this.colspan = colspan;
        this.withBorder = withBorder;
    }

    public DataCell(String text) {
        this.cellContent = new CellContent(text);
    }

    public DataCell(String text, boolean withBorder) {
        this.cellContent = new CellContent(text);
        this.withBorder = withBorder;
    }

    public DataCell(CellContent cellContent, String hexaColorCodeForLeftBorder) {
        this.cellContent = cellContent;
        this.hexaColorCodeForLeftBorder = hexaColorCodeForLeftBorder;
    }

    public DataCell(CellContent cellContent, String hexaColorCodeForLeftBorder, boolean withBorder) {
        this.cellContent = cellContent;
        this.hexaColorCodeForLeftBorder = hexaColorCodeForLeftBorder;
        this.withBorder = withBorder;
    }


       public DataCell(String text, String complement, String hexaColorCodeForLeftBorder, boolean withBorder) {
        this(new CellContent(text, complement), hexaColorCodeForLeftBorder, withBorder);
    }



    public CellContent getCellContent() {
        return cellContent;
    }

    public void setCellContent(CellContent cellContent) {
        this.cellContent = cellContent;
    }

    public String getHexaColorCodeForLeftBorder(){
        return hexaColorCodeForLeftBorder;
    }

    public void setHexaColorCodeForLeftBorder(String hexaColorCodeForLeftBorder){
        this.hexaColorCodeForLeftBorder = hexaColorCodeForLeftBorder;
    }

    public int getColspan(){
        return colspan;
    }

    public void setColspan(int colspan){
        this.colspan = colspan;
    }

    public boolean isWithBorder(){
        return withBorder;
    }

    public void setWithBorder(boolean withBorder){
        this.withBorder = withBorder;
    }

    public AlignmentType getAlignmentType(){
        return alignmentType;
    }

    public void setAlignmentType(AlignmentType alignmentType){
        this.alignmentType = alignmentType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Vérifie si le contenu de la cellule n'est pas nul
        if (cellContent != null) {
            sb.append(cellContent.toString()); // Ajoute la représentation en chaîne du contenu de la cellule

            // Si la cellule occupe plus d'une colonne, ajoute l'information de colspan
            if (colspan > 1) {
                sb.append(" <colspan=").append(colspan).append(">");
                for (int i = 1; i < colspan; i++) {
                    sb.append("\t"); // Ajoute des tabulations pour chaque colonne supplémentaire
                }
            }

            // Si la couleur de la bordure gauche est spécifiée, l'ajoute à la chaîne
            if (hexaColorCodeForLeftBorder != null) {
                sb.append(" [color=").append(hexaColorCodeForLeftBorder).append("]");
            }
        } else {
            sb.append("(vide)"); // Indique que la cellule est vide si le contenu est nul
        }

        return sb.toString(); // Retourne la représentation finale de la cellule
    }
}
