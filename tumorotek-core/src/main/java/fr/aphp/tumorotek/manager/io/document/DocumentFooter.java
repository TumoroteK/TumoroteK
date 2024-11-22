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

/**
 * La classe <code>DocumentFooter</code> représente le pied de page d'un document.
 * Elle contient des données disposées sur trois sections : à gauche, au centre et à droite.
 *
 * <p>Cette classe est utilisée pour structurer et gérer les informations affichées dans
 * le pied de page d'un document généré, que ce soit pour un rapport, une facture, ou tout autre type de document.</p>
 *
 * <ul>
 *   <li><b>leftData</b> : Les données à afficher à gauche dans le pied de page.</li>
 *   <li><b>centerData</b> : Les données à afficher au centre du pied de page.</li>
 *   <li><b>rightData</b> : Les données à afficher à droite dans le pied de page.</li>
 * </ul>
 *
 * <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */

public class DocumentFooter {

    // Données à afficher à gauche dans le pied de page.
    private String leftData;

    // Données à afficher au centre dans le pied de page.
    private String centerData;

    // Données à afficher à droite dans le pied de page.
    private String rightData;



    public DocumentFooter() {
    }

    public DocumentFooter(String leftData, String centerData, String rightData) {
        this.leftData = leftData;
        this.centerData = centerData;
        this.rightData = rightData;
    }


    public String getLeftData() {
        return leftData;
    }

    public void setLeftData(String leftData) {
        this.leftData = leftData;
    }

    public String getCenterData() {
        return centerData;
    }

    public void setCenterData(String centerData) {
        this.centerData = centerData;
    }

    public String getRightData() {
        return rightData;
    }

    public void setRightData(String rightData) {
        this.rightData = rightData;
    }
}
