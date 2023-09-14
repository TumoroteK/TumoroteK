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
package fr.aphp.tumorotek.model.impression;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;

/**
 *
 * Embedded Id pour la table TABLE_ANNOTATION_TEMPLATE.
 * Classe créée le 30/07/2010.
 *
 * @author Pierre VENTADOUR.
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 * hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.0
 *
 */
@Embeddable
public class TableAnnotationTemplatePK implements Serializable
{

   private static final long serialVersionUID = -2094357295011964584L;

   private Template template;

   private TableAnnotation tableAnnotation;

   public TableAnnotationTemplatePK(){

   }

   public TableAnnotationTemplatePK(final Template temp, final TableAnnotation table){
      this.template = temp;
      this.tableAnnotation = table;
   }

   @ManyToOne(targetEntity = Template.class)
   public Template getTemplate(){
      return template;
   }

   public void setTemplate(final Template t){
      this.template = t;
   }

   @ManyToOne(targetEntity = TableAnnotation.class)
   public TableAnnotation getTableAnnotation(){
      return tableAnnotation;
   }

   public void setTableAnnotation(final TableAnnotation t){
      this.tableAnnotation = t;
   }

   /**
    * 2 PKs sont considérés comme égales si elles sont composees
    * des mêmes clés.
    * @param obj est la PK à tester.
    * @return true si les PK sont egales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final TableAnnotationTemplatePK test = (TableAnnotationTemplatePK) obj;
      return ((this.template == test.template || (this.template != null && this.template.equals(test.template)))
         && (this.tableAnnotation == test.tableAnnotation
            || (this.tableAnnotation != null && this.tableAnnotation.equals(test.tableAnnotation))));
   }

   /**
    * Le hashcode est calculé sur les clés.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      int hash = 7;
      int hashTemplate = 0;
      int hashTable = 0;

      if(this.template != null){
         hashTemplate = this.template.hashCode();
      }
      if(this.tableAnnotation != null){
         hashTable = this.tableAnnotation.hashCode();
      }

      hash = 7 * hash + hashTemplate;
      hash = 7 * hash + hashTable;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.template != null && this.tableAnnotation != null){
         return "{" + template.toString() + " (Template), " + tableAnnotation.toString() + " (TableAnnotation)}";
      }else{
         return "{Empty TableAnnotationTemplatePK}";
      }
   }

}
