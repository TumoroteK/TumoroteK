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
package fr.aphp.tumorotek.action.stockage;

import org.zkoss.zul.Div;

/**
 * Div container des divs superposées permettant aux instructions CSS
 * d'afficher l'image représentant un puit de stockage et son contenu.
 * Cette Div est l'élement cliquable, droppable... qui recevra les évènements.
 * La structure html - CSS représentée par cette classe est la suivante:
 *  - Div 'main': reçoit les évènements et définit l'aspect du cursor avec setStyle et
 *  	le background + border avec sClass
 *  	- Div 'img': définit la background url de l'image à afficher (couleur du tube)
 *  		-Div 'overlay': définit le calque coloré à appliquer en fonction du
 *  			statut de stockage du tube.
 *  Date: 02/12/2015
 *
 * @since 2.0.13
 * @author Mathieu BARTHELEMY
 * @version 2.0.13
 **/
public class EmplacementDiv extends Div
{

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * Crée la pile des 3 Divs
    * @param int w Width px
    * @param int h Height px
    * @param tString ootltiptext
    * 
    */
   public EmplacementDiv(final int w, final int h, final String tttext){
      // Div main
      super();
      setWidth(String.valueOf(w) + "px");
      setHeight(String.valueOf(h) + "px");
      setTooltiptext(tttext);

      // Div img
      final Div img = new Div();
      img.setWidth(String.valueOf(w) + "px");
      img.setHeight(String.valueOf(h) + "px");
      img.setParent(this);

      // Div overlay (calque objet statut)
      final Div overlay = new Div();
      overlay.setParent(img);
   }

   /**
    * Applique le style directement à la Div 'img' pour 
    * spécifier la background url qui dessine l'image
    * @param bckCss String
    */
   public void setImageStyle(final String bckCss){
      ((Div) getFirstChild()).setStyle(bckCss);
   }

   /**
    * Renvoie le style de la Div 'img' cad 
    * le background url qui dessine l'image
    * @return bckCss String
    */
   public String getImageStyle(){
      return ((Div) getFirstChild()).getStyle();
   }

   /**
    * Applique la classe de style directement à la Div 'overlay'  
    * pour spécifier le calque à appliquer sur l'image
    * @param cssCl String overlay sClass
    */
   public void setOverlay(final String cssCl){
      ((Div) getFirstChild().getFirstChild()).setSclass(cssCl);
   }

   /**
    * Renvoie la classe de style appliquée à la Div 'overlay'  
    * pour spécifier le calque à appliquer sur l'image
    * @retunr cssCl String overlay sClass
    */
   public String getOverlay(){
      return ((Div) getFirstChild().getFirstChild()).getSclass();
   }

}
