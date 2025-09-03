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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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
   @NamedQuery(name = "Importation.findByHistoriqueId", query = "SELECT i FROM Importation i " + "WHERE i.importHistoriqueId = ?1"),
   @NamedQuery(name = "Importation.findByHistoriqueIdAndEntiteId",
      query = "SELECT i FROM Importation i " + "WHERE i.importHistoriqueId = ?1 " + "AND i.entiteId = ?2 ORDER BY i.objetId"),
   @NamedQuery(name = "Importation.findByEntiteIdAndObjetId",
      query = "SELECT i FROM Importation i " + "WHERE i.entiteId = ?1 " + "AND i.objetId = ?2"),
   @NamedQuery(name = "Importation.findByEntiteIdObjetIdAndTypeCode",
   query = "SELECT i FROM Importation i " + "WHERE i.entiteId = ?1 AND i.objetId = ?2 AND i.typeCode = ?3")
   })
public class Importation implements java.io.Serializable
{

   private static final long serialVersionUID = -3174470852163902048L;

   private Integer importationId;

   private Integer objetId;

   private Integer entiteId;

   private Integer importHistoriqueId;
   
   //fait doublon avec le type : utilisation à revoir - le front n'a jamais été fait pour la mise à jour donc code mort dans l'absolu
   private Boolean isUpdate = false;
   
   //TK-538 : défini le type du modèle : création, modification d'annotation ...
   private String typeCode;

   public Importation(){

   }

   @Id
   @Column(name = "IMPORTATION_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "native",
   parameters = {@Parameter(name = "sequence", value = "importationSeq")})
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

   @Column(name = "ENTITE_ID", nullable = false)
   public Integer getEntiteId(){
      return entiteId;
   }

   public void setEntiteId(final Integer id){
      this.entiteId = id;
   }
   
   @Column(name = "IMPORT_HISTORIQUE_ID", nullable = false)
   public Integer getImportHistoriqueId(){
      return importHistoriqueId;
   }

   public void setImportHistoriqueId(final Integer id){
      this.importHistoriqueId = id;
   }
   
   
   @Column(name = "IS_UPDATE", nullable = false)
   public Boolean getIsUpdate(){
      return isUpdate;
   }

   public void setIsUpdate(final Boolean isUpdate){
      this.isUpdate = isUpdate;
   }
   
   @Column(name = "TYPE_CODE", nullable = false)
   public String getTypeCode(){
      return typeCode;
   }

   public void setTypeCode(String typeCode){
      this.typeCode = typeCode;
   }
   
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
         && (this.entiteId == test.entiteId || (this.entiteId != null && this.entiteId.equals(test.entiteId)))
         && (this.importHistoriqueId == test.importHistoriqueId
            || (this.importHistoriqueId != null && this.importHistoriqueId.equals(test.importHistoriqueId))));
   }

   /**
    * Le hashcode est calculé sur les attributs importHistoriqueId, entiteId et objetId caractérisant l'unicité fonctionnelle
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
      if(this.entiteId != null){
         hashEntite = this.entiteId.hashCode();
      }
      if(this.importHistoriqueId != null){
         hashHistorique = this.importHistoriqueId.hashCode();
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
      clone.setEntiteId(this.entiteId);
      clone.setImportHistoriqueId(this.importHistoriqueId);
      clone.setTypeCode(this.getTypeCode());

      return clone;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.objetId != null && this.entiteId != null && this.importHistoriqueId != null){
         return "{" + this.objetId + " (objetId), " + this.entiteId + "(entiteId), " + this.importHistoriqueId + " (importHistoriqueId), " + this.typeCode + " (typeCode) }";
      }else{
         return "{Empty Importation}";
      }
   }

}
