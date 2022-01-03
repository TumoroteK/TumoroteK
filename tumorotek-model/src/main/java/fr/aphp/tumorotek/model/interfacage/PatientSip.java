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
package fr.aphp.tumorotek.model.interfacage;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.coeur.patient.Patient;

/**
 *
 * Objet persistant mappant la table PATIENT_SIP. Hérite des membres
 * de la classe patient (sauf archive et etat incomplet qui sont
 * donc annotés transient).
 * Classe créée le 14/04/11.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Entity
@Table(name = "PATIENT_SIP")
//@NamedQueries(value = {@NamedQuery(name = "PatientSip.findByNip", query = "SELECT p FROM PatientSip p WHERE p.nip like ?1"),
//   @NamedQuery(name = "PatientSip.findByNom",
//      query = "SELECT p FROM PatientSip p WHERE p.nom like ?1 " + "OR p.nomNaissance like ?1"),
//   @NamedQuery(name = "PatientSip.findCountAll", query = "SELECT count(p) FROM PatientSip p"),
//   @NamedQuery(name = "PatientSip.findFirst",
//      query = "SELECT p FROM PatientSip p " + "where p.dateCreation = (select min(dateCreation) " + "from PatientSip)"),
//   @NamedQuery(name = "PatientSip.findByNumeroSejour",
//      query = "SELECT distinct p FROM PatientSip p " + "JOIN p.sejours s " + "where s.numero like ?1")})
public class PatientSip
{

   private Integer patientSipId;
   private String nip;
   private String nom;
   private String nomNaissance;
   private String prenom;
   private String sexe;
   private Date dateNaissance;
   private String villeNaissance;
   private String paysNaissance;
   private String patientEtat = "V";
   private Date dateEtat;
   private Date dateDeces;
   private Calendar dateCreation;
   private Calendar dateModification;

   private Set<PatientSipSejour> sejours = new HashSet<>();

   // @since 2.0.13.3 dateDeces patient fusionné
   private Date dateDecesP = null;

   /** Constructeur par défaut. */
   public PatientSip(){}

   @Id
   @Column(name = "PATIENT_SIP_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getPatientSipId(){
      return this.patientSipId;
   }

   public void setPatientSipId(final Integer id){
      this.patientSipId = id;
   }

   @Column(name = "NIP", nullable = false, length = 20)
   public String getNip(){
      return this.nip;
   }

   public void setNip(final String n){
      this.nip = n;
   }

   @Column(name = "NOM", nullable = false, length = 50)
   public String getNom(){
      return this.nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "NOM_NAISSANCE", nullable = true, length = 50)
   public String getNomNaissance(){
      return this.nomNaissance;
   }

   public void setNomNaissance(final String naissance){
      this.nomNaissance = naissance;
   }

   @Column(name = "PRENOM", nullable = false, length = 50)
   public String getPrenom(){
      return this.prenom;
   }

   public void setPrenom(final String pren){
      this.prenom = pren;
   }

   @Column(name = "SEXE", nullable = false, length = 3)
   public String getSexe(){
      return this.sexe;
   }

   public void setSexe(final String sex){
      this.sexe = sex;
   }

   @Column(name = "DATE_NAISSANCE", nullable = false)
   public Date getDateNaissance(){
      if(dateNaissance != null){
         return new Date(dateNaissance.getTime());
      }else{
         return null;
      }
   }

   public void setDateNaissance(final Date date){
      if(date != null){
         this.dateNaissance = new Date(date.getTime());
      }else{
         this.dateNaissance = null;
      }
   }

   @Column(name = "VILLE_NAISSANCE", nullable = true, length = 100)
   public String getVilleNaissance(){
      return this.villeNaissance;
   }

   public void setVilleNaissance(final String ville){
      this.villeNaissance = ville;
   }

   @Column(name = "PAYS_NAISSANCE", nullable = true, length = 100)
   public String getPaysNaissance(){
      return this.paysNaissance;
   }

   public void setPaysNaissance(final String pays){
      this.paysNaissance = pays;
   }

   @Column(name = "PATIENT_ETAT", nullable = false, length = 10)
   public String getPatientEtat(){
      return this.patientEtat;
   }

   public void setPatientEtat(final String etat){
      this.patientEtat = etat;
   }

   @Column(name = "DATE_ETAT", nullable = true)
   public Date getDateEtat(){
      if(dateEtat != null){
         return new Date(dateEtat.getTime());
      }else{
         return null;
      }
   }

   public void setDateEtat(final Date date){
      if(date != null){
         this.dateEtat = new Date(date.getTime());
      }else{
         this.dateEtat = null;
      }
   }

   @Column(name = "DATE_DECES", nullable = true)
   public Date getDateDeces(){
      if(dateDeces != null){
         return new Date(dateDeces.getTime());
      }else{
         return null;
      }
   }

   public void setDateDeces(final Date date){
      if(date != null){
         this.dateDeces = new Date(date.getTime());
      }else{
         this.dateDeces = null;
      }
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DATE_CREATION", nullable = false)
   public Calendar getDateCreation(){
      if(dateCreation != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(dateCreation.getTime());
         return cal;
      }else{
         return null;
      }
   }

   public void setDateCreation(final Calendar cal){
      if(cal != null){
         this.dateCreation = Calendar.getInstance();
         this.dateCreation.setTime(cal.getTime());
      }else{
         this.dateCreation = null;
      }
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DATE_MODIFICATION", nullable = true)
   public Calendar getDateModification(){
      if(dateModification != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(dateModification.getTime());
         return cal;
      }else{
         return null;
      }
   }

   public void setDateModification(final Calendar cal){
      if(cal != null){
         this.dateModification = Calendar.getInstance();
         this.dateModification.setTime(cal.getTime());
      }else{
         this.dateModification = null;
      }
   }

   @OneToMany(mappedBy = "patientSip", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
   @OrderBy("dateSejour")
   public Set<PatientSipSejour> getSejours(){
      return sejours;
   }

   public void setSejours(final Set<PatientSipSejour> sjs){
      this.sejours = sjs;
   }

   @Transient
   public Date getDateDecesP(){
      return dateDecesP;
   }

   public void setDateDecesP(final Date _d){
      this.dateDecesP = _d;
   }

   @Override
   public PatientSip clone(){
      final PatientSip clone = new PatientSip();
      clone.setPatientSipId(getPatientSipId());
      clone.setNip(getNip());
      clone.setNom(getNom());
      clone.setNomNaissance(getNomNaissance());
      clone.setPrenom(getPrenom());
      clone.setDateNaissance(getDateNaissance());
      clone.setSexe(getSexe());
      clone.setVilleNaissance(getVilleNaissance());
      clone.setPaysNaissance(getPaysNaissance());
      clone.setPatientEtat(getPatientEtat());
      clone.setDateEtat(getDateEtat());
      clone.setDateDeces(getDateDeces());
      clone.setDateCreation(getDateCreation());
      clone.setDateModification(getDateModification());
      clone.setSejours(getSejours());
      return clone;
   }

   /**
    * 2 patients sip sont considérés comme égaux s'ils ont les mêmes nips.
    * @param obj est le patient sip à tester.
    * @return true si les patients sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final PatientSip test = (PatientSip) obj;
      // 2 coordonnees sont egales si toutes leurs valeurs le sont
      return (this.getNip() != null && getNip().equals(test.getNip())) || this.getNip() == test.getNip();
   }

   /**
    * Le hashcode est calculé sur l'attribut nip.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNip = 0;

      if(getNip() != null){
         hashNip = this.getNip().hashCode();
      }

      hash = 7 * hash + hashNip;

      return hash;
   }

   @Override
   public String toString(){
      if(this.getNip() != null){
         return "{" + this.getNip() + " " + this.getNom() + "}";
      }else{
         return "{Empty PatientSip}";
      }
   }

   /**
    * Extrait le patient TK depuis le patient Sip.
    * @return Patient
    */
   public Patient toPatient(){
      final Patient pat = new Patient();
      pat.setNip(getNip());
      pat.setNom(getNom());
      pat.setPrenom(getPrenom());
      pat.setNomNaissance(getNomNaissance());
      pat.setDateNaissance(getDateNaissance());
      pat.setSexe(getSexe());
      pat.setVilleNaissance(getVilleNaissance());
      pat.setPaysNaissance(getPaysNaissance());
      pat.setPatientEtat(getPatientEtat());
      pat.setDateEtat(getDateEtat());
      pat.setDateDeces(getDateDeces());
      return pat;
   }
}
