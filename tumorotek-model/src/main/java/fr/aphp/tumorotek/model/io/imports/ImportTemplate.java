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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table IMPORT_TEMPLATE.
 * Classe créée le 24/01/2011.
 *
 * @author Pierre VENATDOUR
 * @version 2.0
 *
 */
@Entity
@Table(name = "IMPORT_TEMPLATE")
@NamedQueries(value = {
   @NamedQuery(name = "ImportTemplate.findByBanqueWithOrder",
      query = "SELECT i FROM ImportTemplate i " + "WHERE i.banque = ?1 ORDER BY i.nom"),
   @NamedQuery(name = "ImportTemplate.findByExcludedId",
      query = "SELECT i FROM ImportTemplate i " + "WHERE i.importTemplateId != ?1")})
public class ImportTemplate implements java.io.Serializable, TKdataObject
{

   private static final long serialVersionUID = -319499964642320756L;

   private Integer importTemplateId;
   private Banque banque;
   private String nom;
   private String description;
   private Boolean isEditable;
   private Entite deriveParentEntite;
   private Boolean isUpdate = false;

   private Set<Entite> entites = new HashSet<>();
   private Set<ImportHistorique> importHistoriques = new HashSet<>();

   public ImportTemplate(){

   }

   @Id
   @Column(name = "IMPORT_TEMPLATE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getImportTemplateId(){
      return importTemplateId;
   }

   public void setImportTemplateId(final Integer id){
      this.importTemplateId = id;
   }

   @ManyToOne
   @JoinColumn(name = "BANQUE_ID", nullable = false)
   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }

   @Column(name = "NOM", nullable = false, length = 50)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "DESCRIPTION", nullable = true, length = 250)
   public String getDescription(){
      return description;
   }

   public void setDescription(final String d){
      this.description = d;
   }

   @Column(name = "IS_EDITABLE", nullable = true)
   public Boolean getIsEditable(){
      return isEditable;
   }

   public void setIsEditable(final Boolean e){
      this.isEditable = e;
   }

   @ManyToOne
   @JoinColumn(name = "DERIVE_PARENT_ENTITE_ID", nullable = true)
   public Entite getDeriveParentEntite(){
      return this.deriveParentEntite;
   }

   public void setDeriveParentEntite(final Entite e){
      this.deriveParentEntite = e;
   }

   @ManyToMany(mappedBy = "importTemplates", targetEntity = Entite.class)
   public Set<Entite> getEntites(){
      return entites;
   }

   public void setEntites(final Set<Entite> e){
      this.entites = e;
   }

   @OneToMany(mappedBy = "importTemplate", cascade = CascadeType.REMOVE)
   public Set<ImportHistorique> getImportHistoriques(){
      return importHistoriques;
   }

   public void setImportHistoriques(final Set<ImportHistorique> historiques){
      this.importHistoriques = historiques;
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
      final ImportTemplate test = (ImportTemplate) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
         && (this.banque == test.banque || (this.banque != null && this.banque.equals(test.banque))));
   }

   /**
    * Le hashcode est calculé sur l'attribut nom et la reference
    * vers la banque.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      int hash = 7;
      int hashNom = 0;
      int hashBank = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.banque != null){
         hashBank = this.banque.hashCode();
      }

      hash = 31 * hash + hashNom;
      hash = 31 * hash + hashBank;

      return hash;
   }

   @Override
   public ImportTemplate clone(){
      final ImportTemplate clone = new ImportTemplate();

      clone.setImportTemplateId(this.importTemplateId);
      clone.setBanque(this.banque);
      clone.setNom(this.nom);
      clone.setDescription(this.description);
      clone.setIsEditable(this.isEditable);
      clone.setEntites(this.entites);
      clone.setImportHistoriques(this.importHistoriques);
      clone.setDeriveParentEntite(this.getDeriveParentEntite());
      clone.setIsUpdate(getIsUpdate());

      return clone;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.nom != null && this.banque != null){
         return "{" + this.nom + ", " + banque.getNom() + "(Banque)}";
      }else{
         return "{Empty ImportTemplate}";
      }
   }

   @Override
   public Integer listableObjectId(){
      return this.importTemplateId;
   }
}
