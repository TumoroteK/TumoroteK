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

package fr.aphp.tumorotek.action.stats.charts;

import java.awt.Color;

/**
 * date: 20/01/2014
 *
 * @author Marc DESCHAMPS
 */

public class CategoryDetails implements Comparable<CategoryDetails>
{

   private boolean checkAffichage;

   private String category;

   private Integer value;

   private Integer nonStockValue;

   private Color color;

   public CategoryDetails(){}

   public CategoryDetails(final String cat, final Integer value, final Integer nsv, final boolean b, final Color c){
      setCategory(cat);
      setValue(value);
      setNonStockValue(nsv);
      setCheckAffichage(b);
      setColor(c);
   }

   public void setCategory(final String n){
      this.category = n;
   }

   public void setValue(final Integer i){
      this.value = i;
   }

   public void setCheckAffichage(final Boolean b){
      this.checkAffichage = b;
   }

   public void setColor(final Color c){
      this.color = c;
   }

   public String getCategory(){
      return category;
   }

   public Integer getValue(){
      return value;
   }

   public boolean getCheckAffichage(){
      return checkAffichage;
   }

   public Color getColor(){
      return color;
   }

   public String getHexColor(){
      return ChartColors.toHtmlColor(color);
   }

   public Integer getNonStockValue(){
      return nonStockValue;
   }

   public void setNonStockValue(final Integer s){
      this.nonStockValue = s;
   }

   @Override
   public int compareTo(final CategoryDetails o){
      return getCategory().compareToIgnoreCase(o.getCategory());
   }

}
