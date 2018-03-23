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
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 *
 * Objet persistant mappant la table PATIENT.
 * Classe créée le 14/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "PATIENT")
@NamedQueries(value = {@NamedQuery(name = "Patient.findByNip", query = "SELECT p FROM Patient p WHERE p.nip like ?1"),
   @NamedQuery(name = "Patient.findByNipWithExcludedId",
      query = "SELECT p FROM Patient p WHERE p.nip like ?1 " + "AND p.patientId != ?2"),
   @NamedQuery(name = "Patient.findByNipReturnIds",
      query = "SELECT distinct(p.patientId) FROM Patient p " + "JOIN p.maladies m " + "JOIN m.prelevements prlvts "
         + "WHERE p.nip like ?1 " + "AND prlvts.banque in (?2)"),
   @NamedQuery(name = "Patient.findByNom", query = "SELECT p FROM Patient p WHERE p.nom like ?1 " + "OR p.nomNaissance like ?1"),
   @NamedQuery(name = "Patient.findByNomReturnIds",
      query = "SELECT distinct(p.patientId) FROM Patient p " + "JOIN p.maladies m " + "JOIN m.prelevements prlvts "
         + "WHERE (p.nom like ?1 " + "OR p.nomNaissance like ?1) " + "AND prlvts.banque in (?2)"),
   //		@NamedQuery(name = "Patient.findByNomNaissance", 
   //			query = "SELECT p FROM Patient p WHERE p.nomNaissance = ?1"),
   //		@NamedQuery(name = "Patient.findByPrenom", 
   //			query = "SELECT p FROM Patient p WHERE p.prenom = ?1"),
   //		@NamedQuery(name = "Patient.findBySexe", 
   //			query = "SELECT p FROM Patient p WHERE p.sexe = ?1"),
   @NamedQuery(name = "Patient.findByDateNaissance", query = "SELECT p FROM Patient p WHERE p.dateNaissance = ?1"),
   //		@NamedQuery(name = "Patient.findByVilleNaissance", 
   //			query = "SELECT p FROM Patient p WHERE p.villeNaissance = ?1"),
   //		@NamedQuery(name = "Patient.findByPaysNaissance", 
   //			query = "SELECT p FROM Patient p WHERE p.paysNaissance = ?1"),
   //		@NamedQuery(name = "Patient.findByPatientEtat", 
   //			query = "SELECT p FROM Patient p WHERE p.patientEtat = ?1"),
   //		@NamedQuery(name = "Patient.findByDateEtat", 
   //			query = "SELECT p FROM Patient p WHERE p.dateEtat = ?1"),
   //		@NamedQuery(name = "Patient.findByDateDeces", 
   //			query = "SELECT p FROM Patient p WHERE p.dateDeces = ?1"),
   @NamedQuery(name = "Patient.findByEtatIncomplet", query = "SELECT p FROM Patient p WHERE p.etatIncomplet = true"),
   //		@NamedQuery(name = "Patient.findByArchive", 
   //			query = "SELECT p FROM Patient p WHERE p.archive = ?1")
   @NamedQuery(name = "Patient.findAllNips", query = "SELECT p.nip FROM Patient p where p.nip is not null " + "ORDER BY p.nip"),
   @NamedQuery(name = "Patient.findAllNoms", query = "SELECT p.nom FROM Patient p ORDER BY p.nom"),
   @NamedQuery(name = "Patient.findByExcludedId", query = "SELECT p FROM Patient p WHERE p.patientId != ?1" + " and p.nom = ?2"),
   @NamedQuery(name = "Patient.findCountMaladies", query = "SELECT count(m) FROM Maladie m WHERE m.patient = ?1"),
   @NamedQuery(name = "Patient.findCountPrelevementsByBanque",
      query = "SELECT count(p) FROM Prelevement p " + "WHERE p.maladie.patient = ?1 and p.banque = ?2"),
   @NamedQuery(name = "Patient.findCountPrelevements",
      query = "SELECT count(p) FROM Prelevement p " + "WHERE p.maladie.patient = ?1"),
   @NamedQuery(name = "Patient.findCountPrelevedByDatesSaisie",
      query = "SELECT count(distinct p) FROM Patient p, Operation o " + "JOIN p.maladies m " + "JOIN m.prelevements r "
         + "WHERE p.patientId = o.objetId " + "AND o.entite.nom = 'Patient' " + "AND o.operationType.nom = 'Creation' "
         + "AND o.date >= ?1 AND o.date <= ?2 " + "AND r.banque in (?3)"),
   @NamedQuery(name = "Patient.findCountPrelevedByDatesPrel",
      query = "SELECT count(distinct p) FROM Patient p " + "JOIN p.maladies m " + "JOIN m.prelevements r "
         + "WHERE r.datePrelevement >= ?1 " + "AND r.datePrelevement <= ?2 " + "AND r.banque in (?3)"),
   @NamedQuery(name = "Patient.findCountPrelevedByDatesSaisieExt",
      query = "SELECT count(distinct p) FROM Patient p, Operation o " + "JOIN p.maladies m " + "JOIN m.prelevements r "
         + "WHERE p.patientId = o.objetId " + "AND o.entite.nom = 'Patient' " + "AND o.operationType.nom = 'Creation' "
         + "AND o.date >= ?1 AND o.date <= ?2 " + "AND r.banque in (?3) " + "AND (r.servicePreleveur is null "
         + "OR r.servicePreleveur.etablissement not in (?4))"),
   @NamedQuery(name = "Patient.findCountPrelevedByDatesPrelExt",
      query = "SELECT count(distinct p) FROM Patient p " + "JOIN p.maladies m " + "JOIN m.prelevements r "
         + "WHERE r.datePrelevement >= ?1 " + "AND r.datePrelevement <= ?2 " + "AND r.banque in (?3) "
         + "AND (r.servicePreleveur is null " + "OR r.servicePreleveur.etablissement not in (?4))"),
   @NamedQuery(name = "Patient.findByIdInList", query = "SELECT p FROM Patient p " + "WHERE p.patientId in (?1)"),
   @NamedQuery(name = "Patient.findByAllIds", query = "SELECT p.patientId FROM Patient p"),
   @NamedQuery(name = "Patient.findByAllIdsWithBanques",
      query = "SELECT distinct(p.patientId) FROM Patient p " + "JOIN p.maladies m " + "JOIN m.prelevements prlvts "
         + "where prlvts.banque in (?1)"),
   @NamedQuery(name = "Patient.findByNomInList",
      query = "SELECT distinct(p.patientId) FROM Patient p " + "JOIN p.maladies m " + "JOIN m.prelevements prlvts "
         + "WHERE p.nom in (?1) " + "AND prlvts.banque in (?2)"),
   @NamedQuery(name = "Patient.findByNipInList",
      query = "SELECT distinct(p.patientId) FROM Patient p " + "JOIN p.maladies m " + "JOIN m.prelevements prlvts "
         + "WHERE p.nip in (?1) " + "AND prlvts.banque in (?2)"),
   @NamedQuery(name = "Patient.findCountByReferent",
      query = "SELECT count(p) FROM Patient p " + "JOIN p.patientMedecins o " + "WHERE o.pk.collaborateur = ?1")})
public class Patient extends Object implements TKAnnotableObject, Serializable
{

   private static final long serialVersionUID = -2015746269357055625L;

   private Integer patientId;
   private String nip;
   private String nom;
   private String nomNaissance;
   private String prenom;
   private String sexe;
   private Date dateNaissance;
   private String villeNaissance;
   private String paysNaissance;
   private String patientEtat;
   private Date dateEtat;
   private Date dateDeces;
   private Boolean etatIncomplet;
   private Boolean archive = false;
   //Groupe sanguin passé en annotation
   //	private String groupeSanguin;

   private Set<PatientLien> patientLiens = new HashSet<>();
   private Set<PatientLien> patientLiens2 = new HashSet<>();
   private Set<Maladie> maladies = new HashSet<>();
   private Set<PatientMedecin> patientMedecins = new LinkedHashSet<>();

   /** Constructeur par défaut. */
   public Patient(){}

   @Override
   public String toString(){
      if(this.nom != null){
         if(this.prenom != null){
            return "{" + this.nom + " " + this.prenom + "}";
         }else{
            return "{" + this.nom + " prenom inconnu}";
         }
      }else{
         return "{Empty Patient}";
      }
   }

   @Id
   @Column(name = "PATIENT_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getPatientId(){
      return this.patientId;
   }

   public void setPatientId(final Integer id){
      this.patientId = id;
   }

   @Column(name = "NIP", nullable = true, length = 20)
   public String getNip(){
      return this.nip;
   }

   public void setNip(final String n){
      this.nip = n;
   }

   //Groupe sanguin passé en annotation
   //	@Column(name = "GROUPE_SANGUIN", nullable = true, length = 5)
   //	public String getGroupeSanguin() {
   //		return groupeSanguin;
   //	}

   //	public void setGroupeSanguin(String groupeSanguin) {
   //		this.groupeSanguin = groupeSanguin;
   //	}

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

   @Column(name = "PRENOM", nullable = true, length = 50)
   public String getPrenom(){
      return this.prenom;
   }

   public void setPrenom(final String pren){
      this.prenom = pren;
   }

   @Column(name = "SEXE", nullable = true, length = 3)
   public String getSexe(){
      return this.sexe;
   }

   public void setSexe(final String sex){
      this.sexe = sex;
   }

   @Column(name = "DATE_NAISSANCE", nullable = true)
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

   @Column(name = "ETAT_INCOMPLET", nullable = true)
   public Boolean getEtatIncomplet(){
      return this.etatIncomplet;
   }

   public void setEtatIncomplet(final Boolean etat){
      this.etatIncomplet = etat;
   }

   @Column(name = "ARCHIVE", nullable = false)
   public Boolean getArchive(){
      return this.archive;
   }

   public void setArchive(final Boolean arch){
      this.archive = arch;
   }

   @OneToMany(mappedBy = "pk.patient1", cascade = {CascadeType.ALL})
   public Set<PatientLien> getPatientLiens(){
      return patientLiens;
   }

   public void setPatientLiens(final Set<PatientLien> patientLs){
      this.patientLiens = patientLs;
   }

   @OneToMany(mappedBy = "pk.patient2", cascade = {CascadeType.ALL})
   public Set<PatientLien> getPatientLiens2(){
      return patientLiens2;
   }

   public void setPatientLiens2(final Set<PatientLien> patientLs2){
      this.patientLiens2 = patientLs2;
   }

   @OneToMany(mappedBy = "patient", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
   @OrderBy("maladieId")
   public Set<Maladie> getMaladies(){
      return maladies;
   }

   public void setMaladies(final Set<Maladie> mals){
      this.maladies = mals;
   }

   @OneToMany(mappedBy = "pk.patient", cascade = {CascadeType.ALL})
   @OrderBy("ordre")
   public Set<PatientMedecin> getPatientMedecins(){
      return patientMedecins;
   }

   public void setPatientMedecins(final Set<PatientMedecin> patientMeds){
      this.patientMedecins = patientMeds;
   }

   /**
    * 2 patients sont considérés comme égaux s'ils ont les mêmes nom,
    * prénom, date de naissance.
    * Ajout verification sur la ville si deux valeurs sont spécifiées.
    * @param obj est le patient à tester.
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
      final Patient test = (Patient) obj;
      // 2 coordonnees sont egales si toutes leurs valeurs le sont
      final boolean eq = (((this.nom != null && this.nom.equalsIgnoreCase(test.nom)) || this.nom == test.nom)
         && ((this.prenom != null && this.prenom.equalsIgnoreCase(test.prenom)) || this.prenom == test.prenom)
         && ((this.dateNaissance != null && this.dateNaissance.equals(test.dateNaissance))
            || this.dateNaissance == test.dateNaissance));

      // verif supp sur la ville de naissance
      if(this.villeNaissance != null && test.villeNaissance != null){
         return eq && this.villeNaissance.equalsIgnoreCase(test.villeNaissance);
      }
      return eq;
   }

   /**
    * Le hashcode est calculé sur les attributs nom, prenom,
    * date et ville de naissance.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;
      int hashPrenom = 0;
      int hashDate = 0;
      //int hashVille = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.prenom != null){
         hashPrenom = this.prenom.hashCode();
      }
      if(this.dateNaissance != null){
         hashDate = this.dateNaissance.hashCode();
      }
      // if (this.villeNaissance != null) {
      //	hashVille = this.villeNaissance.hashCode();
      //}

      hash = 7 * hash + hashNom;
      hash = 7 * hash + hashPrenom;
      hash = 7 * hash + hashDate;
      //hash = 7 * hash + hashVille;

      return hash;
   }

   @Override
   public Patient clone(){
      final Patient clone = new Patient();
      clone.setPatientId(this.patientId);
      clone.setNip(this.nip);
      clone.setNom(this.nom);
      clone.setNomNaissance(this.nomNaissance);
      clone.setPrenom(this.prenom);
      clone.setDateNaissance(this.dateNaissance);
      clone.setSexe(this.sexe);
      clone.setVilleNaissance(this.villeNaissance);
      clone.setPaysNaissance(this.paysNaissance);
      clone.setPatientEtat(this.patientEtat);
      clone.setDateEtat(this.dateEtat);
      clone.setDateDeces(this.dateDeces);
      clone.setEtatIncomplet(this.etatIncomplet);
      clone.setArchive(this.archive);
      //Groupe sanguin passé en annotation
      //		clone.setGroupeSanguin(this.groupeSanguin);

      clone.setMaladies(this.maladies);
      return clone;
   }

   @Override
   public Integer listableObjectId(){
      return this.getPatientId();
   }

   @Override
   public String entiteNom(){
      return "Patient";
   }

   @Override
   @Transient
   public Banque getBanque(){
      return null;
   }

   @Override
   @Transient
   public void setBanque(final Banque b){}

   @Override
   @Transient
   public String getPhantomData(){
      if(getPrenom() != null){
         return getNom() + " " + getPrenom();
      }else{
         return getNom();
      }
   }
}
