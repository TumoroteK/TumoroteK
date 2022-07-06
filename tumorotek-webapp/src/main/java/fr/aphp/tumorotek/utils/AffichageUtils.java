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
package fr.aphp.tumorotek.utils;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Image;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treerow;

//TK-314
/**
* classe utilitaire qui propose des méthodes pour gérer l'affichage des composants
* @version 2.2.3-fix314 (genno) 
* @author Corinne HUET
*
*/
public class AffichageUtils
{
   public static final String IMG_INTERDIT = "/images/icones/ico_interdit.png";

   /**
    * Ajoute un sens interdit après le contenu existant du Treecell passé en paramètre
    */
   public static void addImageInterditToTreecell(Treecell tc) {
      Image img = new Image(IMG_INTERDIT);
      img.setStyle("vertical-align: middle;");
      tc.appendChild(img);
   }

   
   /**
    * Supprime la première image trouvée au niveau de la première cellule du Treerow passé en paramètre
    */
   public static void removeFirstImageInTreerow(Treerow tr) {
      if (tr!=null) {
         Treecell tc = (Treecell) tr.getFirstChild();
         Component imageASupprimer = null;
         for(Component c : tc.getChildren()) {
             if (c instanceof Image) {
               imageASupprimer = c;
               break;
            }
         }
         if (imageASupprimer != null) {
            tc.removeChild(imageASupprimer);
         }
      }
   }

   /**
    * Met une ligne en jaune.
    */
   public static void colorateRowInYellow(final org.zkoss.zul.Treerow row){
      row.setStyle("background-color : #FDDFA9");
   }

   /**
    * Met une ligne en vert.
    */
   public static void colorateRowInGreen(final org.zkoss.zul.Treerow row){
      row.setStyle("background-color : #90EE90");
   }

   /**
    * Met une ligne en rouge.
    */
   public static void colorateRowInRed(final org.zkoss.zul.Treerow row){
      row.setStyle("background-color : #FEBAB3");
   }

   /**
    * Surligne la row en bleu léger, couleur correspondante à la sélection 
    * dans Tree.
    * Coloration appelée pour surlignée les terminales après recherche.
    * @since 2.1
    * @version 2.1
    * @param row
    */
   public static void colorateRowInLightBlue(final org.zkoss.zul.Treerow row){
      row.setStyle("background-color : #e6f7ff");
   }

   /**
    * supprime la couleur mise sur une ligne.
    */
   public static void unColorateRow(final org.zkoss.zul.Treerow row){
      row.setStyle("background: none");
   }

}
