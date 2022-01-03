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

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;

/**
 *
 * Objet persistant mappant la table TABLE_ANNOTATION_TEMPLATE.
 * Classe créée le 30/07/2010.
 *
 * @author Pierre Ventadour
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 * hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.3
 *
 */
@Entity
@Table(name = "TABLE_ANNOTATION_TEMPLATE")
@AssociationOverrides({
   @AssociationOverride(name = "pk.template",
      joinColumns = @JoinColumn(name = "TEMPLATE_ID", referencedColumnName = "TEMPLATE_ID")),
   @AssociationOverride(name = "pk.tableAnnotation",
      joinColumns = @JoinColumn(name = "TABLE_ANNOTATION_ID", referencedColumnName = "TABLE_ANNOTATION_ID"))})
//@NamedQueries(value = {
//   @NamedQuery(name = "TableAnnotationTemplate." + "findByTemplate",
//      query = "SELECT t FROM TableAnnotationTemplate t " + "WHERE t.pk.template = ?1 " + "ORDER BY t.ordre"),
//   @NamedQuery(name = "TableAnnotationTemplate." + "findByExcludedPK",
//      query = "SELECT t FROM TableAnnotationTemplate t " + "WHERE t.pk != ?1")})
public class TableAnnotationTemplate implements Serializable
{

   private static final long serialVersionUID = 7015726441690587506L;

   private Integer ordre;
   private TableAnnotationTemplatePK pk = new TableAnnotationTemplatePK();

   public TableAnnotationTemplate(){

   }

   @Column(name = "ORDRE", nullable = false)
   public Integer getOrdre(){
      return ordre;
   }

   public void setOrdre(final Integer o){
      this.ordre = o;
   }

   @EmbeddedId
   @AttributeOverrides({@AttributeOverride(name = "template", column = @Column(name = "TEMPLATE_ID")),
      @AttributeOverride(name = "tableAnnotation", column = @Column(name = "TABLE_ANNOTATION_ID"))})
   public TableAnnotationTemplatePK getPk(){
      return pk;
   }

   public void setPk(final TableAnnotationTemplatePK p){
      this.pk = p;
   }

   @Transient
   public Template getTemplate(){
      return pk.getTemplate();
   }

   public void setTemplate(final Template t){
      this.pk.setTemplate(t);
   }

   @Transient
   public TableAnnotation getTableAnnotation(){
      return pk.getTableAnnotation();
   }

   public void setTableAnnotation(final TableAnnotation table){
      pk.setTableAnnotation(table);
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final TableAnnotationTemplate test = (TableAnnotationTemplate) obj;
      return ((this.pk == test.pk || (this.pk != null && this.pk.equals(test.pk))));
   }

   @Override
   public int hashCode(){
      int hash = 7;
      int hashPk = 0;

      if(this.pk != null){
         hashPk = this.pk.hashCode();
      }

      hash = 7 * hash + hashPk;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.pk != null){
         return "{" + this.pk.toString() + "}";
      }else{
         return "{Empty TableAnnotationTemplate}";
      }
   }

   /**
    * Cree un clone de l'objet.
    * @return clone CederObjet.
    */
   @Override
   public TableAnnotationTemplate clone(){
      final TableAnnotationTemplate clone = new TableAnnotationTemplate();

      clone.setPk(this.getPk());
      clone.setOrdre(this.ordre);

      return clone;
   }

}
