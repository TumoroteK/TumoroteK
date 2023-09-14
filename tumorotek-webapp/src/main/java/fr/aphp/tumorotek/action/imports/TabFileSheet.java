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
package fr.aphp.tumorotek.action.imports;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10.6
 *
 */
public class TabFileSheet
{

   private String name;

   private Integer rowCount;

   private Boolean disabled = false;

   public TabFileSheet(final String n, final Integer r, final Boolean d){
      this.name = n;
      this.rowCount = r;
      this.disabled = d;
   }

   public String getName(){
      return name;
   }

   public void setName(final String n){
      this.name = n;
   }

   public Integer getRowCount(){
      return rowCount;
   }

   public void setRowCount(final Integer r){
      this.rowCount = r;
   }

   public Boolean getDisabled(){
      return disabled;
   }

   public void setDisabled(final Boolean disabled){
      this.disabled = disabled;
   }

   public String getNameAndSize(){
      return ObjectTypesFormatters.getLabel("import.choose.sheet.display", new String[] {name, rowCount.toString()});
   }

   @Override
   public boolean equals(final Object arg0){
      if(arg0 != null){
         final TabFileSheet tb = (TabFileSheet) arg0;
         return (getName() == null && tb.getName() == null) || (getName() != null && getName().equals(tb.getName()));
      }

      return false;
   }

   @Override
   public int hashCode(){
      return super.hashCode();
   }
}
