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
import javax.persistence.FetchType;
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
 * @version 2.3
 *
 */
@Entity
@Table(name = "IMPORT_HISTORIQUE")
//@NamedQueries(value = {
//   @NamedQuery(name = "ImportHistorique.findByTemplateWithOrder",
//      query = "SELECT i FROM ImportHistorique i " + "WHERE i.importTemplate = ?1 ORDER BY i.date desc"),
//   @NamedQuery(name = "ImportHistorique.findByExcludedId",
//      query = "SELECT i FROM ImportHistorique i " + "WHERE i.importHistoriqueId != ?1"),
//   @NamedQuery(name = "ImportHistorique.findPrelevementByImportHistorique",
//      query = "SELECT p FROM Prelevement p, Importation i " + " WHERE i.objetId = p.prelevementId" + " AND i.entite.entiteId = 2"
//         + " AND i.importHistorique = ?1" + " ORDER BY p.prelevementId"),})
public class ImportHistorique implements java.io.Serializable, TKdataObject
{

   private static final long serialVersionUID = 7189671254502803140L;

   private Integer importHistoriqueId;
   private ImportTemplate importTemplate;
   private Utilisateur utilisateur;
   private Calendar date;

   private Set<Importation> importations = new HashSet<>();

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

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "IMPORT_TEMPLATE_ID", nullable = false)
   public ImportTemplate getImportTemplate(){
      return importTemplate;
   }

   public void setImportTemplate(final ImportTemplate it){
      this.importTemplate = it;
   }

   @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ImportHistorique test = (ImportHistorique) obj;
      return ((this.importTemplate == test.importTemplate
         || (this.importTemplate != null && this.importTemplate.equals(test.importTemplate)))
         && (this.date == test.date || (this.date != null && this.date.equals(test.date))));
   }

   @Override
   public int hashCode(){
      int hash = 7;
      int hashImportTemplate = 0;
      int hashDate = 0;

      if(this.importTemplate != null){
         hashImportTemplate = this.importTemplate.hashCode();
      }
      if(this.date != null){
         hashDate = this.date.hashCode();
      }

      hash = 31 * hash + hashImportTemplate;
      hash = 31 * hash + hashDate;

      return hash;
   }

   @Override
   public String toString(){
      if(this.importTemplate != null && this.date != null){
         return "{" + this.date + ", " + importTemplate.getNom() + "(ImportTemplate)}";
      }else{
         return "{Empty ImportHistorique}";
      }
   }

   @Override
   public ImportHistorique clone(){
      final ImportHistorique clone = new ImportHistorique();

      clone.setImportHistoriqueId(this.importHistoriqueId);
      clone.setImportTemplate(this.importTemplate);
      clone.setUtilisateur(this.utilisateur);
      clone.setDate(this.date);
      clone.setImportations(this.importations);

      return clone;
   }

   @Override
   public Integer listableObjectId(){
      return this.importHistoriqueId;
   }
}
