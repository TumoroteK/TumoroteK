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
package fr.aphp.tumorotek.model.io.imports;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table IMPORTATION.
 * Classe créée le 08/02/2011.
 *
 * @author Pierre VENATDOUR
 * @version 2.0
 *
 */
@Entity
@Table(name = "IMPORTATION")
@NamedQueries(value = {
   @NamedQuery(name = "Importation.findByHistorique", query = "SELECT i FROM Importation i " + "WHERE i.importHistorique = ?1"),
   @NamedQuery(name = "Importation.findByHistoriqueAndEntite",
      query = "SELECT i FROM Importation i " + "WHERE i.importHistorique = ?1 " + "AND i.entite = ?2 ORDER BY i.objetId"),
   @NamedQuery(name = "Importation.findByEntiteAndObjetId",
      query = "SELECT i FROM Importation i " + "WHERE i.entite = ?1 " + "AND i.objetId = ?2")})
public class Importation implements java.io.Serializable
{

   private static final long serialVersionUID = -3174470852163902048L;

   private Integer importationId;

   private Integer objetId;

   private Entite entite;

   private ImportHistorique importHistorique;

   private Boolean isUpdate = false;

   public Importation(){

   }

   @Id
   @Column(name = "IMPORTATION_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getImportationId(){
      return importationId;
   }

   public void setImportationId(final Integer id){
      this.importationId = id;
   }

   @Column(name = "OBJET_ID", nullable = false)
   public Integer getObjetId(){
      return objetId;
   }

   public void setObjetId(final Integer id){
      this.objetId = id;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "ENTITE_ID", nullable = false)
   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite e){
      this.entite = e;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "IMPORT_HISTORIQUE_ID", nullable = true)
   public ImportHistorique getImportHistorique(){
      return importHistorique;
   }

   public void setImportHistorique(final ImportHistorique iHistorique){
      this.importHistorique = iHistorique;
   }

   @Column(name = "IS_UPDATE", nullable = false)
   public Boolean getIsUpdate(){
      return isUpdate;
   }

   public void setIsUpdate(final Boolean isUpdate){
      this.isUpdate = isUpdate;
   }

   /**
    * 2 templates sont considérées comme égales s'ils ont le même nom
    * et la même reference vers la banque.
    * @param obj à tester.
    * @return true si les objs sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Importation test = (Importation) obj;
      return ((this.objetId == test.objetId || (this.objetId != null && this.objetId.equals(test.objetId)))
         && (this.entite == test.entite || (this.entite != null && this.entite.equals(test.entite)))
         && (this.importHistorique == test.importHistorique
            || (this.importHistorique != null && this.importHistorique.equals(test.importHistorique))));
   }

   /**
    * Le hashcode est calculé sur l'attribut nom et la reference
    * vers la banque.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      int hash = 7;
      int hashObjetId = 0;
      int hashEntite = 0;
      int hashHistorique = 0;

      if(this.objetId != null){
         hashObjetId = this.objetId.hashCode();
      }
      if(this.entite != null){
         hashEntite = this.entite.hashCode();
      }
      if(this.importHistorique != null){
         hashHistorique = this.importHistorique.hashCode();
      }

      hash = 31 * hash + hashObjetId;
      hash = 31 * hash + hashEntite;
      hash = 31 * hash + hashHistorique;

      return hash;
   }

   @Override
   public Importation clone(){
      final Importation clone = new Importation();

      clone.setImportationId(this.importationId);
      clone.setObjetId(this.objetId);
      clone.setEntite(this.entite);
      clone.setImportHistorique(this.importHistorique);

      return clone;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.objetId != null && this.entite != null && this.importHistorique != null){
         return "{" + this.objetId + ", " + this.entite.getNom() + "(Entite) " + this.importHistorique.toString() + "}";
      }else{
         return "{Empty Importation}";
      }
   }

}
