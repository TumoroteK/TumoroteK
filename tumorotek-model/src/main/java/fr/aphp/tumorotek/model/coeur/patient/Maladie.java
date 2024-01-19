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
package fr.aphp.tumorotek.model.coeur.patient;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKDelegateObject;
import fr.aphp.tumorotek.model.TKDelegetableObject;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.patient.serotk.AbstractMaladieDelegate;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.gatsbi.Visite;

/**
 *
 * Objet persistant mappant la table MALADIE.
 * Classe créée le 14/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.3.0-gatsbi
 *
 */
@Entity
@Table(name = "MALADIE")
@NamedQueries(value = {@NamedQuery(name = "Maladie.findByLibelle", query = "SELECT m FROM Maladie m WHERE m.libelle like ?1"),
   @NamedQuery(name = "Maladie.findByCode", query = "SELECT m FROM Maladie m WHERE m.code like ?1"),
   @NamedQuery(name = "Maladie.findByExcludedId",
      query = "SELECT m FROM Maladie m WHERE m.maladieId != ?1" + " AND m.libelle = ?2"),
   @NamedQuery(name = "Maladie.findByPatientNoSystemNorVisite",
      query = "SELECT m FROM Maladie m WHERE m.patient = ?1 AND m.systemeDefaut = 0 AND m.banque is null "
         + "ORDER BY m.dateDebut, m.dateDiagnostic, m.maladieId"),
   @NamedQuery(name = "Maladie.findByCollaborateurId",
      query = "SELECT m FROM Maladie m LEFT JOIN m.collaborateurs o  WHERE o.collaborateurId = ?1"),
   @NamedQuery(name = "Maladie.findCountByReferent",
      query = "SELECT count(m) FROM Maladie m " + "JOIN m.collaborateurs c WHERE c = ?1"),
   @NamedQuery(name = "Maladie.findByLibelleAndPatient",
      query = "SELECT m FROM Maladie m WHERE m.libelle like ?1 AND m.patient = ?2"),
   @NamedQuery(name = "Maladie.findAllByPatient",
      query = "SELECT m FROM Maladie m WHERE m.patient = ?1 ORDER BY m.dateDebut, m.dateDiagnostic"),
   @NamedQuery(name = "Maladie.findByPatientExcludingVisites",
      query = "SELECT m FROM Maladie m WHERE m.patient = ?1 AND m.banque is null ORDER BY m.dateDebut, m.dateDiagnostic"),
   @NamedQuery(name = "Maladie.findVisites",
      query = "SELECT m FROM Maladie m WHERE m.patient = ?1 and m.banque = ?2 "
         + "ORDER BY m.dateDebut, m.dateDiagnostic, m.maladieId")
})
public class Maladie extends TKDelegetableObject<Maladie> implements TKdataObject, TKFantomableObject, Serializable
{

   private static final long serialVersionUID = 4092522013404060267L;

   private Integer maladieId;

   private Patient patient;

   private String libelle;

   private String code;

   private Date dateDiagnostic;

   private Date dateDebut;

   private Boolean systemeDefaut = false;

   private Set<Prelevement> prelevements = new HashSet<>();

   private Set<Collaborateur> collaborateurs = new HashSet<>();

   private TKDelegateObject<Maladie> delegate;
   
   // @since 2.3.0-gatsbi
   // Transient
   private Visite visite;
   
   // @since 2.3.0-gatsbi
   // les maladies correspondant à des visites ont une référence vers la 
   // collection dans laquelle elles ont été créées.
   private Banque banque;

   /** Constructeur par défaut. */
   public Maladie(){}

   @Override
   public String toString(){
      if(this.libelle != null){
         return "{" + this.libelle + "}";
      }
      return "{Empty Maladie}";
   }

   @Id
   @Column(name = "MALADIE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getMaladieId(){
      return this.maladieId;
   }

   public void setMaladieId(final Integer id){
      this.maladieId = id;
   }

   @ManyToOne(cascade = {CascadeType.REFRESH})
   @JoinColumn(name = "PATIENT_ID", nullable = false)
   public Patient getPatient(){
      return this.patient;
   }

   public void setPatient(final Patient p){
      this.patient = p;
   }

   @Column(name = "LIBELLE", nullable = false, length = 300)
   public String getLibelle(){
      return this.libelle;
   }

   public void setLibelle(final String lib){
      this.libelle = lib;
   }

   @Column(name = "CODE", nullable = true, length = 50)
   public String getCode(){
      return this.code;
   }

   public void setCode(final String c){
      this.code = c;
   }

   @Column(name = "DATE_DIAGNOSTIC", nullable = true)
   public Date getDateDiagnostic(){
      if(dateDiagnostic != null){
         return new Date(dateDiagnostic.getTime());
      }
      return null;
   }

   public void setDateDiagnostic(final Date date){
      if(date != null){
         this.dateDiagnostic = new Date(date.getTime());
      }else{
         this.dateDiagnostic = null;
      }
   }

   @Column(name = "DATE_DEBUT", nullable = true)
   public Date getDateDebut(){
      if(dateDebut != null){
         return new Date(dateDebut.getTime());
      }
      return null;
   }

   public void setDateDebut(final Date date){
      if(date != null){
         this.dateDebut = new Date(date.getTime());
      }else{
         this.dateDebut = null;
      }
   }

   @Column(name = "SYSTEME_DEFAUT", nullable = false)
   public Boolean getSystemeDefaut(){
      return systemeDefaut;
   }

   public void setSystemeDefaut(final Boolean sysDefaut){
      this.systemeDefaut = sysDefaut;
   }

   @ManyToOne
   @JoinColumn(name = "BANQUE_ID", nullable = true)
   public Banque getBanque(){
      return banque;
   }

   public void setBanque(Banque banque){
      this.banque = banque;
   }

   @OneToMany(mappedBy = "maladie")
   @OrderBy("datePrelevement")
   public Set<Prelevement> getPrelevements(){
      return prelevements;
   }

   public void setPrelevements(final Set<Prelevement> prelevs){
      this.prelevements = prelevs;
   }

   @ManyToMany(targetEntity = Collaborateur.class, cascade = {CascadeType.MERGE})
   @JoinTable(name = "MALADIE_MEDECIN", joinColumns = @JoinColumn(name = "MALADIE_ID"),
      inverseJoinColumns = @JoinColumn(name = "COLLABORATEUR_ID"))
   public Set<Collaborateur> getCollaborateurs(){
      return collaborateurs;
   }

   public void setCollaborateurs(final Set<Collaborateur> collabs){
      this.collaborateurs = collabs;
   }

   /**
    * 2 maladies sont considérées comme égales si elles ont le même
    * libelle et le même patient et la même date diagnostic.
    * @param obj est la maladie à tester.
    * @return true si les maladie sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Maladie test = (Maladie) obj;
      return (((this.libelle != null && this.libelle.equalsIgnoreCase(test.libelle)) || this.libelle == test.libelle)
         && ((this.patient != null && this.patient.equals(test.patient)) || this.patient == test.patient)
         && ((this.dateDiagnostic != null && this.dateDiagnostic.equals(test.dateDiagnostic))
            || this.dateDiagnostic == test.dateDiagnostic));
   }

   /**
    * Le hashcode est calculé sur les attributs libelle et patient
    * et dateDiagnostic.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashLibelle = 0;
      int hashPatient = 0;
      int hashDateDiag = 0;

      if(this.libelle != null){
         hashLibelle = this.libelle.hashCode();
      }
      if(this.patient != null){
         hashPatient = this.patient.hashCode();
      }
      if(this.dateDiagnostic != null){
         hashDateDiag = this.dateDiagnostic.hashCode();
      }

      hash = 31 * hash + hashLibelle;
      hash = 31 * hash + hashPatient;
      hash = 31 * hash + hashDateDiag;

      return hash;
   }

   @Override
   public Maladie clone(){
      final Maladie clone = new Maladie();
      clone.setMaladieId(this.maladieId);
      clone.setPatient(this.patient);
      clone.setLibelle(this.libelle);
      clone.setCode(this.code);
      clone.setDateDebut(this.dateDebut);
      clone.setDateDiagnostic(this.dateDiagnostic);
      clone.setCollaborateurs(this.collaborateurs);
      clone.setPrelevements(this.prelevements);
      clone.setSystemeDefaut(getSystemeDefaut());

      clone.setDelegate(getDelegate());
      
      clone.setBanque(getBanque());

      return clone;
   }

   @Override
   @Transient
   public String getPhantomData(){
      return getPatient().getPhantomData() + ": " + this.libelle;
   }

   @Override
   public String entiteNom(){
      return "Maladie";
   }

   @Override
   @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "delegator",
      targetEntity = AbstractMaladieDelegate.class)
   public TKDelegateObject<Maladie> getDelegate(){
      return delegate;
   }

   @Override
   public void setDelegate(final TKDelegateObject<Maladie> delegate){
      this.delegate = delegate;
   }

   @Override
   public Integer listableObjectId(){
      return getMaladieId();
   }
   
   @Transient
   public Visite getVisite(){
      return visite;
   }

   @Transient
   public void setVisite(Visite visite){
      this.visite = visite;
   }
   
   @Transient
   public String getMaladieBanqueLibelle() {
      return libelle.concat(banque != null ? " [".concat(banque.getNom().concat("]")) : "");
   }
}