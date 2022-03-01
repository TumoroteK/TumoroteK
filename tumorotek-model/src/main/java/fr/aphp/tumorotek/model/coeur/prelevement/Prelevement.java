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
package fr.aphp.tumorotek.model.coeur.prelevement;

import java.io.Serializable;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKDelegateObject;
import fr.aphp.tumorotek.model.TKDelegetableObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.AbstractPrelevementDelegate;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utils.Utils;

/**
 *
 * Objet persistant mappant la table PRELEVEMENT.
 * Classe créée le 11/09/09.
 *
 *
 * @author Maxime Gousseau
 * @version 2.3
 *
 */
@Entity
@Table(name = "PRELEVEMENT")
//@NamedQueries(value = {@NamedQuery(name = "Prelevement.findByCode", query = "SELECT p FROM Prelevement p WHERE p.code like ?1"),
//   @NamedQuery(name = "Prelevement.findByCodeOrNumLaboWithBanque",
//      query = "SELECT p FROM Prelevement p WHERE (p.code like ?1 " + "OR p.numeroLabo like ?1) " + "AND p.banque = ?2"),
//   @NamedQuery(name = "Prelevement.findByCodeInPlateforme",
//      query = "SELECT p FROM Prelevement p WHERE p.code like ?1 " + "AND p.banque.plateforme = ?2"),
//   @NamedQuery(name = "Prelevement" + ".findByCodeOrNumLaboWithBanqueReturnIds",
//      query = "SELECT p.prelevementId FROM Prelevement p " + "WHERE (p.code like ?1 " + "OR p.numeroLabo like ?1) "
//         + "AND p.banque = ?2"),
//   @NamedQuery(name = "Prelevement.findByConsentDateAfterDate", query = "SELECT p FROM Prelevement p WHERE p.consentDate >= ?1"),
//   @NamedQuery(name = "Prelevement.findByDatePrelevementAfterDate",
//      query = "SELECT p FROM Prelevement p " + "WHERE p.datePrelevement >= ?1"),
//   @NamedQuery(name = "Prelevement" + ".findByDatePrelevementAfterDateWithBanque",
//      query = "SELECT p FROM Prelevement p " + "WHERE p.datePrelevement >= ?1 " + "AND p.banque = ?2"),
//   //		@NamedQuery(name = "Prelevement.findByDateCongelationAfterDate", 
//   //			query = "SELECT p FROM Prelevement p " 
//   @NamedQuery(name = "Prelevement.findByNature", query = "SELECT p FROM Prelevement p WHERE p.nature = ?1"),
//   @NamedQuery(name = "Prelevement.findByPrelevementType",
//      query = "SELECT p FROM Prelevement p " + "WHERE p.prelevementType = ?1"),
//   @NamedQuery(name = "Prelevement.findByConsentType", query = "SELECT p FROM Prelevement p " + "WHERE p.consentType = ?1"),
//   @NamedQuery(name = "Prelevement.findByExcludedIdCodes",
//      query = "SELECT p.code FROM Prelevement p " + "WHERE p.prelevementId != ?1 and p.banque = ?2"),
//   @NamedQuery(name = "Prelevement.findByNdaLike", query = "SELECT p FROM Prelevement p WHERE p.patientNda like ?1"),
//   @NamedQuery(name = "Prelevement.findByBanqueSelectCode", query = "SELECT p.code FROM Prelevement p " + "WHERE p.banque = ?1"),
//   @NamedQuery(name = "Prelevement.findByBanqueSelectNda",
//      query = "SELECT p.patientNda FROM Prelevement p " + "WHERE p.banque = ?1 AND p.patientNda is not null "
//         + "ORDER BY p.patientNda"),
//   @NamedQuery(name = "Prelevement.findByMaladieLibelleLike",
//      query = "SELECT p FROM Prelevement p " + "WHERE p.maladie.libelle like ?1"),
//   @NamedQuery(name = "Prelevement.findByMaladieAndBanque",
//      query = "SELECT p FROM Prelevement p " + "WHERE p.maladie= ?1 and p.banque = ?2 " + "ORDER BY p.datePrelevement"),
//   @NamedQuery(name = "Prelevement.findByMaladieAndOtherBanques",
//      query = "SELECT p FROM Prelevement p " + "WHERE p.maladie= ?1 and p.banque != ?2 " + "ORDER BY p.datePrelevement"),
//   @NamedQuery(name = "Prelevement.findByBanques",
//      query = "SELECT p FROM Prelevement p WHERE p.banque in (?1) " + "ORDER BY p.banque, p.datePrelevement"),
//   @NamedQuery(name = "Prelevement.findByPatient",
//      query = "SELECT p FROM Prelevement p " + "join p.maladie m " + "WHERE m.patient = ?1"),
//   @NamedQuery(name = "Prelevement.findByNumberEchantillons",
//      query = "SELECT p FROM Prelevement p " + "WHERE (select count(e) From Echantillon e " + "WHERE e.prelevement=p) = ?1"),
//   @NamedQuery(name = "Prelevement.findByMaladieAndNature",
//      query = "SELECT p FROM Prelevement p " + "WHERE p.maladie = ?1 " + "AND p.nature.nom like ?2 "
//         + "AND p.datePrelevement = ?3"),
//   @NamedQuery(name = "Prelevement.findCountEclConsentByDates",
//      query = "SELECT count(distinct p) FROM Prelevement p, " + "Operation o " + "WHERE p.prelevementId = o.objetId "
//         + "AND o.entite.nom = 'Prelevement' " + "AND o.operationType.nom = 'Creation' " + "AND p.consentType in (?1) "
//         + "AND o.date >= ?2 AND o.date <= ?3 " + "AND p.banque in (?4) "),
//   @NamedQuery(name = "Prelevement.findAssociatePrelsOfType",
//      query = "SELECT p FROM Prelevement p " + "WHERE p.maladie = ?1 " + "AND p.nature in (?2) " + "AND p.banque in (?3)"),
//   @NamedQuery(name = "Prelevement.findByOrganeByDates",
//      query = "SELECT distinct p FROM Prelevement p, " + "Operation o " + "JOIN p.echantillons e " + "JOIN e.codesAssignes c "
//         + "WHERE p.prelevementId = o.objetId " + "AND o.entite.nom = 'Prelevement' " + "AND o.operationType.nom = 'Creation' "
//         + "AND (c.code in (?1) " + "OR c.libelle in (?1)) " + "AND c.isOrgane = 1 " + "AND o.date >= ?2 AND o.date <= ?3 "
//         + "AND p.banque in (?4) "),
//   @NamedQuery(name = "Prelevement.findByOrganeByDatesConsent",
//      query = "SELECT distinct p FROM Prelevement p, " + "Operation o " + "JOIN p.echantillons e " + "JOIN e.codesAssignes c "
//         + "WHERE p.prelevementId = o.objetId " + "AND o.entite.nom = 'Prelevement' " + "AND o.operationType.nom = 'Creation' "
//         + "AND (c.code in (?1) " + "OR c.libelle in (?1)) " + "AND o.date >= ?2 AND o.date <= ?3 " + "AND c.isOrgane = 1 "
//         + "AND p.banque in (?4) " + "AND p.consentType in (?5)"),
//   @NamedQuery(name = "Prelevement.findByPatientNomReturnIds",
//      query = "SELECT p.prelevementId FROM Prelevement p " + "JOIN p.maladie as m " + "JOIN m.patient as pat "
//         + "WHERE (pat.nom like ?1 OR pat.nip like ?1) AND p.banque = ?2"),
//   @NamedQuery(name = "Prelevement.findByIdInList", query = "SELECT p FROM Prelevement p " + "WHERE p.prelevementId in (?1)"),
//   @NamedQuery(name = "Prelevement.findByBanquesAllIds",
//      query = "SELECT p.prelevementId FROM Prelevement p " + "WHERE p.banque in (?1)"),
//   @NamedQuery(name = "Prelevement.findByEchantillonId",
//      query = "SELECT e.prelevement FROM Echantillon e " + "WHERE e.echantillonId = ?1"),
//   @NamedQuery(name = "Prelevement.findByCodesAndBanquesInList",
//      query = "SELECT p FROM Prelevement p " + "WHERE (p.code in (?1) OR p.numeroLabo in (?1)) AND p.banque in (?2)"),
//   @NamedQuery(name = "Prelevement.findByComDiag",
//      query = "SELECT p FROM Prelevement p " + "JOIN p.delegate s " + "WHERE s.libelle like ?1 AND p.banque in (?2)"),
//   @NamedQuery(name = "Prelevement.findByPatientNomOrNipInList",
//      query = "SELECT p.prelevementId FROM Prelevement p " + "JOIN p.maladie as m " + "JOIN m.patient as pat "
//         + "WHERE (pat.nom in (?1) or pat.nip in (?1)) " + "AND p.banque in (?2)"),
//   @NamedQuery(name = "Prelevement" + ".findByCodeOrNumLaboInListWithBanque",
//      query = "SELECT p.prelevementId FROM Prelevement p " + "WHERE (p.code in (?1) " + "OR p.numeroLabo in (?1)) "
//         + "AND p.banque in (?2)"),
//   @NamedQuery(name = "Prelevement.findByEtablissementNom",
//      query = "SELECT DISTINCT e FROM Prelevement e WHERE " + "e.servicePreleveur.etablissement.nom like ?1 "
//         + "AND e.banque in (?2)"), // + " UNION SELECT DISTINCT e FROM Prelevement e WHERE "
//        // + "e.preleveur.etablissement.nom like ?1 " + "AND e.banque in (?2)"),
//   @NamedQuery(name = "Prelevement.findByEtablissementVide",
//      query = "SELECT DISTINCT e FROM Prelevement e " + "LEFT OUTER JOIN e.preleveur as p " + "WHERE e.servicePreleveur is null "
//         + "AND (p is null OR " + "p.etablissement is null) " + "AND e.banque in (?1)"),
//   @NamedQuery(name = "Prelevement.findByPreleveur", query = "SELECT e FROM Prelevement e " + "WHERE e.preleveur = (?1)"),
//   @NamedQuery(name = "Prelevement.findCountCreatedByCollaborateur",
//      query = "SELECT count(p) FROM Prelevement p, Operation o " + "WHERE p.prelevementId = o.objetId "
//         + "and p.preleveur = (?1) " + "AND o.operationType.nom = 'Creation' " + "AND o.entite.nom = 'Prelevement'"),
//   @NamedQuery(name = "Prelevement.findCountByService",
//      query = "SELECT count(p) FROM Prelevement p " + "WHERE p.servicePreleveur = (?1)"),
//   @NamedQuery(name = "Prelevement.findCountByPreleveur",
//      query = "SELECT count(p) FROM Prelevement p" + " WHERE p.preleveur = ?1"),
//   @NamedQuery(name = "Prelevement.findByOperateur", query = "SELECT e FROM Prelevement e " + "WHERE e.operateur = (?1)"),
//   @NamedQuery(name = "Prelevement.findByService", query = "SELECT e FROM Prelevement e " + "WHERE e.servicePreleveur = (?1)"),
//   @NamedQuery(name = "Prelevement.findByPatientAndBanques",
//      query = "SELECT e FROM Prelevement e " + "WHERE e.maladie.patient = ?1 AND e.banque in (?2)"),
//   @NamedQuery(name = "Prelevement.findByEtablissementLaboInter", 
//   		query = "SELECT DISTINCT e FROM Prelevement e JOIN e.laboInters l where l.service.etablissement = (?1) AND e.banque in (?2)"),
//   @NamedQuery(name = "Prelevement.findByServiceLaboInter", 
//		query = "SELECT DISTINCT e FROM Prelevement e JOIN e.laboInters l where l.service = (?1) AND e.banque in (?2)"), 
//   @NamedQuery(name = "Prelevement.findByCollaborateurLaboInter", 
//		query = "SELECT DISTINCT e FROM Prelevement e JOIN e.laboInters l where l.collaborateur = (?1) AND e.banque in (?2)")})
public class Prelevement extends TKDelegetableObject<Prelevement> implements TKAnnotableObject, Serializable
{

   private static final long serialVersionUID = 6737874055478715763L;

   private Integer prelevementId;
   private String code;
   private Date consentDate;
   private Calendar datePrelevement;
   private Integer conditNbr;
   private Calendar dateDepart;
   private Float transportTemp;
   private Calendar dateArrivee;
   private Float quantite;
   //private Float volume;
   private String patientNda;
   private String numeroLabo;
   private Boolean sterile;
   private Boolean congDepart;
   private Boolean congArrivee;
   private Boolean conformeArrivee;
   private Boolean etatIncomplet;

   private Boolean archive = false;

   private Nature nature;
   private PrelevementType prelevementType;
   private ConditType conditType;
   private ConditMilieu conditMilieu;
   private Banque banque;
   private Collaborateur preleveur;
   private Service servicePreleveur;
   private Transporteur transporteur;
   private Collaborateur operateur;
   private Unite quantiteUnite;
   //private Unite volumeUnite;
   private ConsentType consentType;
   private Maladie maladie;

   private Set<LaboInter> laboInters = new HashSet<>();
   private Set<Echantillon> echantillons = new HashSet<>();
   private Set<Risque> risques = new HashSet<>();

   private TKDelegateObject<Prelevement> delegate;

   public Prelevement(){}

   @Override
   public String toString(){
      if(this.code != null){
         return "{" + this.code + "}";
      }
      return "{Empty Prelevement}";
   }

   @Id
   @Column(name = "PRELEVEMENT_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getPrelevementId(){
      return this.prelevementId;
   }

   public void setPrelevementId(final Integer id){
      this.prelevementId = id;
   }

   @Column(name = "CODE", nullable = true, length = 50)
   public String getCode(){
      return this.code;
   }

   public void setCode(final String c){
      this.code = c;
   }

   @Column(name = "CONSENT_DATE", nullable = true)
   public Date getConsentDate(){
      if(consentDate != null){
         return new Date(consentDate.getTime());
      }
      return null;
   }

   public void setConsentDate(final Date date){
      if(date != null){
         this.consentDate = new Date(date.getTime());
      }else{
         this.consentDate = null;
      }
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DATE_PRELEVEMENT", nullable = true)
   public Calendar getDatePrelevement(){
      if(datePrelevement != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(datePrelevement.getTime());
         return cal;
      }
      return null;
   }

   public void setDatePrelevement(final Calendar cal){
      if(cal != null){
         this.datePrelevement = Calendar.getInstance();
         this.datePrelevement.setTime(cal.getTime());
      }else{
         this.datePrelevement = null;
      }
   }

   @Column(name = "CONDIT_NBR", nullable = true)
   public Integer getConditNbr(){
      return this.conditNbr;
   }

   public void setConditNbr(final Integer nbr){
      this.conditNbr = nbr;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DATE_DEPART", nullable = true)
   public Calendar getDateDepart(){
      if(dateDepart != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(dateDepart.getTime());
         return cal;
      }
      return null;
   }

   public void setDateDepart(final Calendar cal){
      if(cal != null){
         this.dateDepart = Calendar.getInstance();
         this.dateDepart.setTime(cal.getTime());
      }else{
         this.dateDepart = null;
      }
   }

   @Column(name = "TRANSPORT_TEMP", nullable = true)
   public Float getTransportTemp(){
      return this.transportTemp;
   }

   public void setTransportTemp(final Float temp){
      this.transportTemp = temp;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DATE_ARRIVEE", nullable = true)
   public Calendar getDateArrivee(){
      if(dateArrivee != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(dateArrivee.getTime());
         return cal;
      }
      return null;
   }

   public void setDateArrivee(final Calendar cal){
      if(cal != null){
         this.dateArrivee = Calendar.getInstance();
         this.dateArrivee.setTime(cal.getTime());
      }else{
         this.dateArrivee = null;
      }
   }

   @Column(name = "QUANTITE", nullable = true)
   public Float getQuantite(){
      return Utils.floor(this.quantite, 3);
   }

   public void setQuantite(final Float quant){
      this.quantite = Utils.floor(quant, 3);
   }

   /*@Column(name = "VOLUME", nullable = true)
   public Float getVolume() {
   	return this.volume;
   }
   
   public void setVolume(Float vol) {
   	this.volume = vol;
   }*/

   @Column(name = "PATIENT_NDA", nullable = true, length = 20)
   public String getPatientNda(){
      return this.patientNda;
   }

   public void setPatientNda(final String nda){
      this.patientNda = nda;
   }

   @Column(name = "NUMERO_LABO", nullable = true, length = 50)
   public String getNumeroLabo(){
      return this.numeroLabo;
   }

   public void setNumeroLabo(final String labo){
      this.numeroLabo = labo;
   }

   @Column(name = "STERILE", nullable = true)
   public Boolean getSterile(){
      return this.sterile;
   }

   public void setSterile(final Boolean ster){
      this.sterile = ster;
   }

   @Column(name = "CONG_DEPART", nullable = true)
   public Boolean getCongDepart(){
      return congDepart;
   }

   public void setCongDepart(final Boolean c){
      this.congDepart = c;
   }

   @Column(name = "CONG_ARRIVEE", nullable = true)
   public Boolean getCongArrivee(){
      return congArrivee;
   }

   public void setCongArrivee(final Boolean c){
      this.congArrivee = c;
   }

   @Column(name = "CONFORME_ARRIVEE", nullable = true)
   public Boolean getConformeArrivee(){
      return conformeArrivee;
   }

   public void setConformeArrivee(final Boolean conforme){
      this.conformeArrivee = conforme;
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

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "NATURE_ID", nullable = false)
   public Nature getNature(){
      return this.nature;
   }

   public void setNature(final Nature n){
      this.nature = n;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "PRELEVEMENT_TYPE_ID", nullable = true)
   public PrelevementType getPrelevementType(){
      return this.prelevementType;
   }

   public void setPrelevementType(final PrelevementType type){
      this.prelevementType = type;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "CONDIT_TYPE_ID", nullable = true)
   public ConditType getConditType(){
      return this.conditType;
   }

   public void setConditType(final ConditType type){
      this.conditType = type;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "CONDIT_MILIEU_ID", nullable = true)
   public ConditMilieu getConditMilieu(){
      return this.conditMilieu;
   }

   public void setConditMilieu(final ConditMilieu milieu){
      this.conditMilieu = milieu;
   }

   @Override
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "BANQUE_ID", nullable = false)
   public Banque getBanque(){
      return this.banque;
   }

   @Override
   public void setBanque(final Banque bank){
      this.banque = bank;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "PRELEVEUR_ID", nullable = true)
   public Collaborateur getPreleveur(){
      return this.preleveur;
   }

   public void setPreleveur(final Collaborateur collaborateur){
      this.preleveur = collaborateur;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "SERVICE_PRELEVEUR_ID", nullable = true)
   public Service getServicePreleveur(){
      return this.servicePreleveur;
   }

   public void setServicePreleveur(final Service service){
      this.servicePreleveur = service;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "TRANSPORTEUR_ID", nullable = true)
   public Transporteur getTransporteur(){
      return this.transporteur;
   }

   public void setTransporteur(final Transporteur transport){
      this.transporteur = transport;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "OPERATEUR_ID", nullable = true)
   public Collaborateur getOperateur(){
      return this.operateur;
   }

   public void setOperateur(final Collaborateur collaborateur){
      this.operateur = collaborateur;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "QUANTITE_UNITE_ID", nullable = true)
   public Unite getQuantiteUnite(){
      return this.quantiteUnite;
   }

   public void setQuantiteUnite(final Unite unite){
      this.quantiteUnite = unite;
   }

   /*@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE })
   @JoinColumn(name = "VOLUME_UNITE_ID", nullable = true)
   public Unite getVolumeUnite() {
   	return this.volumeUnite;
   }
   
   public void setVolumeUnite(Unite unite) {
   	this.volumeUnite = unite;
   }*/

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "CONSENT_TYPE_ID", nullable = false)
   public ConsentType getConsentType(){
      return this.consentType;
   }

   public void setConsentType(final ConsentType type){
      this.consentType = type;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "MALADIE_ID", nullable = true)
   public Maladie getMaladie(){
      return this.maladie;
   }

   public void setMaladie(final Maladie m){
      this.maladie = m;
   }

   @OneToMany(mappedBy = "prelevement", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
   public Set<LaboInter> getLaboInters(){
      return this.laboInters;
   }

   public void setLaboInters(final Set<LaboInter> laboIs){
      this.laboInters = laboIs;
   }

   @OneToMany(mappedBy = "prelevement")
   @OrderBy("echantillonId")
   public Set<Echantillon> getEchantillons(){
      return this.echantillons;
   }

   public void setEchantillons(final Set<Echantillon> echants){
      this.echantillons = echants;
   }

   @ManyToMany(targetEntity = Risque.class, fetch=FetchType.LAZY)
   @JoinTable(name = "PRELEVEMENT_RISQUE", joinColumns = @JoinColumn(name = "PRELEVEMENT_ID"),
      inverseJoinColumns = @JoinColumn(name = "RISQUE_ID"))
   public Set<Risque> getRisques(){
      return this.risques;
   }

   public void setRisques(final Set<Risque> risks){
      this.risques = risks;
   }

   /**
    * 2 prélèvements sont considérés comme égaux s'ils ont le même
    * code et la même banque.
    * @param obj est le prélèvement à tester.
    * @return true si les prélèvements sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Prelevement test = (Prelevement) obj;
      return ((this.code == test.code || (this.code != null && this.code.equals(test.code)))
         && (this.getBanque() == test.getBanque() || (this.getBanque() != null && this.getBanque().equals(test.getBanque()))));
   }

   /**
    * Le hashcode est calculé sur les attributs code et banque.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashCode = 0;
      int hashBanque = 0;

      if(this.code != null){
         hashCode = this.code.hashCode();
      }
      if(this.banque != null){
         hashBanque = this.banque.hashCode();
      }

      hash = 7 * hash + hashCode;
      hash = 7 * hash + hashBanque;

      return hash;

   }

   @Override
   public Prelevement clone(){
      final Prelevement clone = new Prelevement();

      clone.setPrelevementId(this.getPrelevementId());
      clone.setBanque(this.getBanque());
      clone.setCode(this.getCode());
      clone.setNature(this.getNature());
      clone.setMaladie(this.getMaladie());
      clone.setConsentType(this.getConsentType());
      clone.setConsentDate(this.getConsentDate());
      clone.setPreleveur(this.getPreleveur());
      clone.setServicePreleveur(this.getServicePreleveur());
      clone.setDatePrelevement(this.getDatePrelevement());
      clone.setPrelevementType(this.getPrelevementType());
      clone.setConditType(this.getConditType());
      clone.setConditMilieu(this.getConditMilieu());
      clone.setConditNbr(this.getConditNbr());
      clone.setDateDepart(this.getDateDepart());
      clone.setTransporteur(this.getTransporteur());
      clone.setTransportTemp(this.getTransportTemp());
      clone.setDateArrivee(this.getDateArrivee());
      clone.setOperateur(this.getOperateur());
      clone.setQuantite(this.getQuantite());
      clone.setQuantiteUnite(this.getQuantiteUnite());
      /*clone.setVolume(this.getVolume());
      clone.setVolumeUnite(this.getVolumeUnite());*/
      clone.setPatientNda(this.getPatientNda());
      clone.setNumeroLabo(this.getNumeroLabo());
      clone.setSterile(this.getSterile());
      clone.setCongDepart(this.getCongDepart());
      clone.setCongArrivee(this.getCongArrivee());
      clone.setConformeArrivee(this.getConformeArrivee());
      clone.setEtatIncomplet(this.getEtatIncomplet());
      clone.setArchive(this.getArchive());
      clone.setRisques(getRisques());
      
      clone.setDelegate(getDelegate());
      
      return clone;
   }

   @Override
   public Integer listableObjectId(){
      return this.getPrelevementId();
   }

   @Override
   public String entiteNom(){
      return "Prelevement";
   }

   @Override
   @Transient
   public String getPhantomData(){
      return code;
   }

   @Override
   @OneToOne(optional = true, cascade = CascadeType.ALL, mappedBy = "delegator", orphanRemoval = true,
      targetEntity = AbstractPrelevementDelegate.class)
   public TKDelegateObject<Prelevement> getDelegate(){
      return delegate;
   }

   @Transient
   public PrelevementSero getPrelevementSero(){
      if(delegate instanceof PrelevementSero){
         return (PrelevementSero) delegate;
      }
      return null;
   }

   @Override
   public void setDelegate(TKDelegateObject<Prelevement> _d) {
	   this.delegate = _d;
   }
}