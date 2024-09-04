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

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Objet persistant mappant la table IMPORT_HISTORIQUE.
 * Classe créée le 08/02/2011.
 *
 * @author Pierre VENATDOUR
 * @version 2.0.10.3
 *
 */
@Entity
@Table(name = "IMPORT_HISTORIQUE")
@NamedQueries(value = {
   @NamedQuery(name = "ImportHistorique.findByTemplateIdAndImportBanqueIdWithOrder",
      query = "SELECT i FROM ImportHistorique i " + "WHERE i.importTemplateId = ?1 and i.importBanqueId = ?2 ORDER BY i.date desc"),
   @NamedQuery(name = "ImportHistorique.findByExcludedId",
      query = "SELECT i FROM ImportHistorique i " + "WHERE i.importHistoriqueId != ?1"),
   @NamedQuery(name = "ImportHistorique.findPrelevementByImportHistorique",
      query = "SELECT p FROM Prelevement p, Importation i " + " WHERE i.objetId = p.prelevementId" + " AND i.entite.entiteId = 2"
         + " AND i.importHistorique = ?1" + " ORDER BY p.prelevementId"),})
public class ImportHistorique implements java.io.Serializable, TKdataObject
{

   private static final long serialVersionUID = 7189671254502803140L;

   private Integer importHistoriqueId;

   //TK-537 : pour casser le lien bidirectionnel qui n'a plus de sens (un importTemplate a des historiques dépendant de la banque en cours)
   //on n'utilise que importTemplateId
   private Integer importTemplateId;

   private Utilisateur utilisateur;

   private Calendar date;

   private Set<Importation> importations = new HashSet<>();
   
   //TK-537 : l'objet Banque n'est pas utile dans cet objet. Mais ce champ sera utilisé pour des requêtes
   private Integer importBanqueId;

   public ImportHistorique(){

   }

   @Id
   @Column(name = "IMPORT_HISTORIQUE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getImportHistoriqueId(){
      return importHistoriqueId;
   }

   public void setImportHistoriqueId(final Integer id){
      this.importHistoriqueId = id;
   }

   @Column(name = "IMPORT_TEMPLATE_ID", nullable = false)
   public Integer getImportTemplateId(){
      return importTemplateId;
   }
   
   public void setImportTemplateId(final Integer importTemplateId){
      this.importTemplateId = importTemplateId;
   }
   
   
   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "UTILISATEUR_ID", nullable = false)
   public Utilisateur getUtilisateur(){
      return utilisateur;
   }

   public void setUtilisateur(final Utilisateur u){
      this.utilisateur = u;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DATE_", nullable = true)
   public Calendar getDate(){
      if(date != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(date.getTime());
         return cal;
      }else{
         return null;
      }
   }

   public void setDate(final Calendar cal){
      if(cal != null){
         this.date = Calendar.getInstance();
         this.date.setTime(cal.getTime());
      }else{
         this.date = null;
      }
   }

   @OneToMany(mappedBy = "importHistorique", cascade = {CascadeType.ALL})
   public Set<Importation> getImportations(){
      return importations;
   }

   public void setImportations(final Set<Importation> i){
      this.importations = i;
   }

   @Column(name = "IMPORT_BANQUE_ID", nullable = false)
   public Integer getImportBanqueId(){
      return importBanqueId;
   }

   public void setImportBanqueId(Integer banqueId){
      this.importBanqueId = banqueId;
   }
   
   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ImportHistorique test = (ImportHistorique) obj;
      return ((this.importTemplateId == test.importTemplateId
         || (this.importTemplateId != null && this.importTemplateId.equals(test.importTemplateId)))
         && (this.date == test.date || (this.date != null && this.date.equals(test.date))));
   }

   @Override
   public int hashCode(){
      int hash = 7;
      int hashDate = 0;

      if(this.date != null){
         hashDate = this.date.hashCode();
      }

      hash = 31 * hash + hashDate;

      return hash;
   }

   @Override
   public String toString(){
      if(this.date != null){
         return "{" + this.date + ", " + importTemplateId + "(ImportTemplateId), "+ importBanqueId +"(ImportBanqueId)}";
      }else{
         return "{Empty ImportHistorique}";
      }
   }

   @Override
   public ImportHistorique clone(){
      final ImportHistorique clone = new ImportHistorique();

      clone.setImportHistoriqueId(this.importHistoriqueId);
      clone.setImportTemplateId(importTemplateId);
      clone.setUtilisateur(this.utilisateur);
      clone.setDate(this.date);
      clone.setImportations(this.importations);
      clone.setImportBanqueId(importBanqueId);

      return clone;
   }

   @Override
   public Integer listableObjectId(){
      return this.importHistoriqueId;
   }
}
