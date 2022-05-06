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
package fr.aphp.tumorotek.model.coeur.echantillon;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import fr.aphp.tumorotek.model.TKDelegateObject;
import fr.aphp.tumorotek.model.TKDelegetableObject;
import fr.aphp.tumorotek.model.TKFileSettableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.delegate.AbstractEchantillonDelegate;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utils.Utils;

/**
 *
 * Objet persistant mappant la table ECHANTILLON.
 * Classe créée le 14/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.1
 *
 */
@Entity
@Table(name = "ECHANTILLON")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries(value = {@NamedQuery(name = "Echantillon.findByCode", query = "SELECT e FROM Echantillon e WHERE e.code like ?1"),
   @NamedQuery(name = "Echantillon.findByCodeWithBanque",
      query = "SELECT e FROM Echantillon e WHERE e.code like ?1 " + "AND e.banque = ?2"),
   @NamedQuery(name = "Echantillon.findByCodeInPlateforme",
      query = "SELECT e FROM Echantillon e WHERE e.code like ?1 " + "AND e.banque.plateforme = ?2"),
   @NamedQuery(name = "Echantillon.findByCodeWithBanqueReturnIds",
      query = "SELECT e.echantillonId FROM Echantillon e " + "WHERE e.code like ?1 " + "AND e.banque = ?2"),
   @NamedQuery(name = "Echantillon.findByDateStockAfterDate", query = "SELECT e FROM Echantillon e WHERE e.dateStock >= ?1"),
   @NamedQuery(name = "Echantillon.findByDateStockAfterDateWithBanque",
      query = "SELECT e FROM Echantillon e WHERE e.dateStock >= ?1 " + "AND e.banque = ?2"),
   @NamedQuery(name = "Echantillon.findByExcludedIdCodes",
      query = "SELECT e.code FROM Echantillon e " + "WHERE e.echantillonId != ?1 AND e.banque = ?2"),
   @NamedQuery(name = "Echantillon.findByObjetStatut", query = "SELECT e FROM Echantillon e " + "WHERE e.objetStatut = ?1"),
   @NamedQuery(name = "Echantillon.findByEchanQualite", query = "SELECT e FROM Echantillon e " + "WHERE e.echanQualite = ?1"),
   @NamedQuery(name = "Echantillon.findByEchantillonType",
      query = "SELECT e FROM Echantillon e " + "WHERE e.echantillonType = ?1"),
   @NamedQuery(name = "Echantillon.findByModePrepa", query = "SELECT e FROM Echantillon e " + "WHERE e.modePrepa = ?1"),
   @NamedQuery(name = "Echantillon.findByPrelevement",
      query = "SELECT e FROM Echantillon e " + "WHERE e.prelevement = ?1 " + "ORDER By e.echantillonId"),
   @NamedQuery(name = "Echantillon.findRestantsByPrelevement",
      query = "SELECT e FROM Echantillon e " + "WHERE e.prelevement = ?1 " + "AND e.quantite > 0"),
   @NamedQuery(name = "Echantillon.findByPrelevementAndStatut",
      query = "SELECT e FROM Echantillon e " + "WHERE e.prelevement = ?1 " + "AND e.objetStatut = ?2"),
   @NamedQuery(name = "Echantillon.findByBanqueSelectCode", query = "SELECT e.code FROM Echantillon e " + "WHERE e.banque = ?1"),
   @NamedQuery(name = "Echantillon.findByBanqueAndQuantiteSelectCode",
      query = "SELECT e.code FROM Echantillon e " + "WHERE e.banque = ?1 " + "AND (quantite > 0 OR quantite IS NULL) "
         + "AND e.objetStatut.statut not in ('EPUISE', 'ENCOURS', 'RESERVE')"),
   @NamedQuery(name = "Echantillon.findAllCodesByBanqueAndQuantiteNotNullOrInCessionTraitement",
      query = "SELECT e.code FROM Echantillon e "
         + "WHERE e.banque = ?1 "
         + "AND (((quantite > 0 OR quantite IS NULL) "
         + "AND e.objetStatut.statut not in ('EPUISE', 'ENCOURS', 'RESERVE'))"
         + "OR ("
               + "e.echantillonId in (SELECT c.pk.objetId FROM CederObjet c WHERE c.pk.entite.nom = 'Echantillon' AND c.pk.cession.cessionType.type = 'Traitement' AND c.statut = 'TRAITEMENT'"
         + ")))"),
   @NamedQuery(name = "Echantillon.findByBanqueStatutSelectCode",
      query = "SELECT e.code FROM Echantillon e " + "WHERE e.banque = ?1 AND e.objetStatut=?2 " + "ORDER BY e.code"),
   @NamedQuery(name = "Echantillon.findByBanqueInListStatutSelectCode",
      query = "SELECT e.code FROM Echantillon e " + "WHERE e.banque in (?1) AND e.objetStatut=?2 " + "ORDER BY e.code"),
   @NamedQuery(name = "Echantillon.findByTerminale",
      query = "SELECT e FROM Echantillon e " + "WHERE e.echantillonId IN " + "(select empl.objetId FROM Emplacement empl "
         + "WHERE empl.entite = ?1 AND empl.terminale = ?2) " + "ORDER BY e.echantillonId"),
   @NamedQuery(name = "Echantillon.findByBanques",
      query = "SELECT e FROM Echantillon e WHERE e.banque in (?1) " + "ORDER BY e.banque, e.code"),
   @NamedQuery(name = "Echantillon.findByTerminaleDirect",
      query = "SELECT e FROM Echantillon e " + "WHERE e.emplacement.terminale = ?1)"),
   @NamedQuery(name = "Echantillon.findByMaladieAndType",
      query = "SELECT e FROM Echantillon e " + "WHERE e.prelevement.maladie = ?1 " + "AND e.echantillonType.nom like ?2 "
         + "AND e.prelevement.datePrelevement = ?3"),
   @NamedQuery(name = "Echantillon.findCountSamplesByDates",
      query = "SELECT count(distinct e) FROM Echantillon e, Operation o " + "WHERE e.echantillonId = o.objetId "
         + "AND o.date >= ?1 AND o.date <= ?2 " + "AND o.operationType.nom = 'Creation' " + "AND o.entite.nom = 'Echantillon' "
         + "AND e.banque in (?3)"),
   @NamedQuery(name = "Echantillon.findCountSamplesByDatesExt",
      query = "SELECT count(distinct e) FROM Echantillon e, Operation o " + "WHERE e.echantillonId = o.objetId "
         + "AND o.date >= ?1 AND o.date <= ?2 " + "AND o.operationType.nom = 'Creation' " + "AND o.entite.nom = 'Echantillon' "
         + "AND e.banque in (?3) " + "AND (e.prelevement.servicePreleveur is null "
         + "OR e.prelevement.servicePreleveur.etablissement not in (?4))"),
   @NamedQuery(name = "Echantillon.findCountEchansByCessTypes",
      query = "SELECT count(distinct e) FROM CederObjet c, " + " Echantillon e " + "WHERE c.pk.objetId = e.echantillonId "
         + "AND c.pk.entite.nom = 'Echantillon' " + "AND c.pk.cession.cessionType = ?1 " + "AND e.banque in (?4) "
         + "AND ((c.pk.cession.validationDate >= ?2 " + "AND c.pk.cession.validationDate <= ?3) "
         + "OR (c.pk.cession.destructionDate >= ?2 " + "AND c.pk.cession.destructionDate <= ?3))"),
   @NamedQuery(name = "Echantillon.findCountByCollaborateur",
      query = "SELECT count(e) FROM Echantillon e " + "WHERE e.collaborateur = (?1)"),
   @NamedQuery(name = "Echantillon.findCountCreatedByCollaborateur",
      query = "SELECT count(e) FROM Echantillon e, Operation o " + "WHERE e.echantillonId = o.objetId "
         + "and e.collaborateur = (?1) " + "AND o.operationType.nom = 'Creation' " + "AND o.entite.nom = 'ProdDerive'"),
   @NamedQuery(name = "Echantillon.findCountByOperateur",
      query = "SELECT count(e) FROM Echantillon e " + "WHERE e.collaborateur = ?1"),
   @NamedQuery(name = "Echantillon.findAssociateEchansOfType",
      query = "SELECT distinct e FROM Echantillon e " + "WHERE (e.prelevement.maladie = ?1 " + "OR e.prelevement = ?4) "
         + "AND e.echantillonType in (?2) " + "AND e.banque in (?3)"),
   @NamedQuery(name = "Echantillon.findByPatientNomReturnIds",
      query = "SELECT e.echantillonId FROM Echantillon e " + "JOIN e.prelevement as p " + "JOIN p.maladie as m "
         + "JOIN m.patient as pat " + "WHERE (pat.nom like ?1 OR pat.nip like ?1) AND e.banque = ?2"),
   @NamedQuery(name = "Echantillon.findByIds", query = "SELECT e FROM Echantillon e " + "WHERE e.echantillonId in (?1)"),
   @NamedQuery(name = "Echantillon.findByBanquesAllIds",
      query = "SELECT e.echantillonId FROM Echantillon e " + "WHERE e.banque in (?1)"),
   @NamedQuery(name = "Echantillon.findCountByPrelevement",
      query = "SELECT count(distinct e) FROM Echantillon e " + "WHERE e.prelevement = ?1"),
   @NamedQuery(name = "Echantillon.findCountRestantsByPrelevement",
      query = "SELECT count(distinct e) FROM Echantillon e " + "WHERE e.prelevement = ?1 " + "AND e.quantite > 0"),
   @NamedQuery(name = "Echantillon" + ".findCountByPrelevementAndStockeReserve",
      query = "SELECT count(distinct e) FROM Echantillon e " + "WHERE e.prelevement = ?1 "
         + "AND (e.objetStatut.statut = 'STOCKE' " + "OR e.objetStatut.statut = 'RESERVE')"),
   @NamedQuery(name = "Echantillon.findByPatientNomOrNipInList",
      query = "SELECT e.echantillonId FROM Echantillon e " + "JOIN e.prelevement as p " + "JOIN p.maladie as m "
         + "JOIN m.patient as pat " + "WHERE (pat.nom in (?1) or pat.nip in (?1)) " + "AND e.banque in (?2)"),
   @NamedQuery(name = "Echantillon" + ".findByCodeInListWithBanque",
      query = "SELECT e.echantillonId, e.code FROM Echantillon e " + "WHERE e.code in (?1) " + "AND e.banque in (?2)"),
   @NamedQuery(name = "Echantillon" + ".findByCollaborateur",
      query = "SELECT e FROM Echantillon e " + "WHERE e.collaborateur = (?1)"),
   @NamedQuery(name = "Echantillon.findByEmplacement",
      query = "SELECT e FROM Echantillon e " + "WHERE e.emplacement.terminale = ?1 " + "AND e.emplacement.position = ?2"),
   @NamedQuery(name = "Echantillon.findByCodeInListWithPlateforme", query= "SELECT e FROM Echantillon e JOIN e.banque bq JOIN bq.plateforme pf WHERE e.code in (?1) AND pf = ?2 ")})
public class Echantillon extends TKDelegetableObject<Echantillon> implements TKStockableObject, Serializable, TKFileSettableObject
{

   private static final long serialVersionUID = 7561274704258954965L;

   private Integer echantillonId;
   private String code;
   private Calendar dateStock;
   private Float quantite;
   private Float quantiteInit;
   //private Float volume;
   //private Float volumeInit;
   private Float delaiCgl;
   private Boolean tumoral;
   private Boolean sterile;
   private Boolean conformeTraitement;
   private Boolean conformeCession;
   private String lateralite;
   private Boolean etatIncomplet;
   private Boolean archive = false;

   private Banque banque;
   private ObjetStatut objetStatut;
   private EchanQualite echanQualite;
   private ModePrepa modePrepa;
   private Unite quantiteUnite;
   private Collaborateur collaborateur;
   private Emplacement emplacement;
   private Fichier crAnapath;
   private EchantillonType echantillonType;
   private Prelevement prelevement;
   private Set<CodeAssigne> codesAssignes = new HashSet<>();
   private TKDelegateObject<Echantillon> delegate;

   // stream utilise pour enregistre Cr anapath
   private InputStream anapathStream;

   public Echantillon(){}

   @Id
   @Column(name = "ECHANTILLON_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "native",
      parameters = {@Parameter(name = "sequence", value = "echantillonSeq")})
   public Integer getEchantillonId(){
      return this.echantillonId;
   }

   public void setEchantillonId(final Integer id){
      this.echantillonId = id;
   }

   @Override
   @Column(name = "CODE", nullable = false, length = 50)
   public String getCode(){
      return this.code;
   }

   @Override
   public void setCode(final String c){
      this.code = c;
   }

   @Override
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DATE_STOCK", nullable = true)
   public Calendar getDateStock(){
      if(dateStock != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(dateStock.getTime());
         return cal;
      }
      return null;
   }

   @Override
   public void setDateStock(final Calendar cal){
      if(cal != null){
         this.dateStock = Calendar.getInstance();
         this.dateStock.setTime(cal.getTime());
      }else{
         this.dateStock = null;
      }
   }

   @Override
   @Column(name = "QUANTITE", nullable = true)
   public Float getQuantite(){
      return Utils.floor(this.quantite, 3);
   }

   @Override
   public void setQuantite(final Float quant){
      this.quantite = Utils.floor(quant, 3);
   }

   @Column(name = "QUANTITE_INIT", nullable = true)
   public Float getQuantiteInit(){
      return Utils.floor(this.quantiteInit, 3);
   }

   public void setQuantiteInit(final Float quant){
      this.quantiteInit = Utils.floor(quant, 3);
   }

   @Column(name = "DELAI_CGL", nullable = true, precision = 9, scale = 2)
   public Float getDelaiCgl(){
      return this.delaiCgl;
   }

   public void setDelaiCgl(final Float delai){
      this.delaiCgl = delai;
   }

   @Column(name = "TUMORAL", nullable = true)
   public Boolean getTumoral(){
      return this.tumoral;
   }

   public void setTumoral(final Boolean tumo){
      this.tumoral = tumo;
   }

   @Column(name = "STERILE", nullable = true)
   public Boolean getSterile(){
      return this.sterile;
   }

   public void setSterile(final Boolean ster){
      this.sterile = ster;
   }

   @Override
   @Column(name = "CONFORME_TRAITEMENT", nullable = true)
   public Boolean getConformeTraitement(){
      return conformeTraitement;
   }

   public void setConformeTraitement(final Boolean conforme){
      this.conformeTraitement = conforme;
   }

   @Override
   @Column(name = "CONFORME_CESSION", nullable = true)
   public Boolean getConformeCession(){
      return conformeCession;
   }

   public void setConformeCession(final Boolean conforme){
      this.conformeCession = conforme;
   }

   @Column(name = "LATERALITE", nullable = true)
   public String getLateralite(){
      return lateralite;
   }

   public void setLateralite(final String lat){
      this.lateralite = lat;
   }

   @Column(name = "ETAT_INCOMPLET", nullable = true)
   public Boolean getEtatIncomplet(){
      return this.etatIncomplet;
   }

   public void setEtatIncomplet(final Boolean etatI){
      this.etatIncomplet = etatI;
   }

   @Column(name = "ARCHIVE", nullable = false)
   public Boolean getArchive(){
      return this.archive;
   }

   public void setArchive(final Boolean arch){
      this.archive = arch;
   }

   @Override
   @ManyToOne(optional = false)
   @JoinColumn(name = "BANQUE_ID")
   public Banque getBanque(){
      return banque;
   }

   @Override
   public void setBanque(final Banque b){
      this.banque = b;
   }

   @Override
   @ManyToOne
   @JoinColumn(name = "OBJET_STATUT_ID", nullable = false)
   public ObjetStatut getObjetStatut(){
      return this.objetStatut;
   }

   @Override
   public void setObjetStatut(final ObjetStatut objetStat){
      this.objetStatut = objetStat;
   }

   @ManyToOne(cascade = {CascadeType.MERGE})
   @JoinColumn(name = "ECHAN_QUALITE_ID", nullable = true)
   public EchanQualite getEchanQualite(){
      return this.echanQualite;
   }

   public void setEchanQualite(final EchanQualite qualite){
      this.echanQualite = qualite;
   }

   @ManyToOne(cascade = {CascadeType.MERGE})
   @JoinColumn(name = "MODE_PREPA_ID", nullable = true)
   public ModePrepa getModePrepa(){
      return this.modePrepa;
   }

   public void setModePrepa(final ModePrepa mode){
      this.modePrepa = mode;
   }

   @Override
   @ManyToOne(cascade = {CascadeType.MERGE})
   @JoinColumn(name = "QUANTITE_UNITE_ID", nullable = true)
   public Unite getQuantiteUnite(){
      return this.quantiteUnite;
   }

   @Override
   public void setQuantiteUnite(final Unite qUnite){
      this.quantiteUnite = qUnite;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "COLLABORATEUR_ID", nullable = true)
   public Collaborateur getCollaborateur(){
      return this.collaborateur;
   }

   public void setCollaborateur(final Collaborateur c){
      this.collaborateur = c;
   }

   @Override
   @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
   @JoinColumn(name = "EMPLACEMENT_ID", nullable = true)
   public Emplacement getEmplacement(){
      return this.emplacement;
   }

   @Override
   public void setEmplacement(final Emplacement empl){
      this.emplacement = empl;
   }

   @OneToOne
   @JoinColumn(name = "CR_ANAPATH_ID", nullable = true)
   public Fichier getCrAnapath(){
      return this.crAnapath;
   }

   public void setCrAnapath(final Fichier f){
      this.crAnapath = f;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "ECHANTILLON_TYPE_ID", nullable = true)
   public EchantillonType getEchantillonType(){
      return this.echantillonType;
   }

   public void setEchantillonType(final EchantillonType type){
      this.echantillonType = type;
   }

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "PRELEVEMENT_ID", nullable = true)
   public Prelevement getPrelevement(){
      return this.prelevement;
   }

   public void setPrelevement(final Prelevement prelev){
      this.prelevement = prelev;
   }

   @OneToMany(mappedBy = "echantillon")
   public Set<CodeAssigne> getCodesAssignes(){
      return codesAssignes;
   }

   public void setCodesAssignes(final Set<CodeAssigne> cs){
      this.codesAssignes = cs;
   }

   @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "delegator",
		      targetEntity = AbstractEchantillonDelegate.class)
   public TKDelegateObject<Echantillon> getDelegate(){
      return delegate;
   }

   /**
    * 2 échantillons sont considérés comme égaux s'ils ont le même
    * code et la même Banque.
    * @param obj est l'échantillon à tester.
    * @return true si les échantillons sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Echantillon test = (Echantillon) obj;
      if(this.code == null){
         if(test.code == null){
            if(this.banque == null){
               return (test.banque == null);
            }
            return (this.banque.equals(test.banque));
         }
         return false;
      }else if(this.banque == null){
         if(test.banque == null){
            return (this.code.equals(test.code));
         }
         return false;
      }else{
         return (this.code.equals(test.code) && this.banque.equals(test.banque));
      }
   }

   /**
    * Le hashcode est calculé sur les attributs code et Banque.
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

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.code != null){
         return "{" + this.code + "}";
      }
      return "{Empty Echantillon}";
   }

   @Override
   public Echantillon clone(){
      final Echantillon clone = new Echantillon();
      clone.setEchantillonId(this.getEchantillonId());
      clone.setBanque(this.getBanque());
      clone.setPrelevement(this.getPrelevement());
      clone.setCollaborateur(this.getCollaborateur());
      clone.setCode(this.getCode());
      clone.setObjetStatut(this.getObjetStatut());
      clone.setDateStock(this.getDateStock());
      clone.setEmplacement(this.getEmplacement());
      clone.setEchantillonType(this.getEchantillonType());
      clone.setQuantite(this.getQuantite());
      clone.setQuantiteInit(this.getQuantiteInit());
      clone.setQuantiteUnite(this.getQuantiteUnite());
      /*clone.setVolume(this.getVolume());
      clone.setVolumeInit(this.getVolumeInit());
      clone.setVolumeUnite(this.getVolumeUnite());*/
      clone.setDelaiCgl(this.getDelaiCgl());
      clone.setEchanQualite(this.getEchanQualite());
      clone.setTumoral(this.getTumoral());
      clone.setModePrepa(this.getModePrepa());
      clone.setCrAnapath(this.getCrAnapath());
      clone.setSterile(this.getSterile());
      clone.setConformeTraitement(this.getConformeTraitement());
      clone.setConformeCession(this.getConformeCession());
      clone.setLateralite(this.getLateralite());
      clone.setEtatIncomplet(this.getEtatIncomplet());
      clone.setArchive(this.getArchive());
      // clone.setCodeOrganes(this.getCodeOrganes());
      // clone.setCodeMorphos(this.getCodeMorphos());
      // clone.setCodeOrganeExport(this.getCodeOrganeExport());
      // clone.setCodeLesExport(this.getCodeLesExport());
      clone.setCodesAssignes(getCodesAssignes());
      clone.setAnapathStream(getAnapathStream());

      clone.setDelegate(getDelegate());
      
      return clone;
   }

   /**
    * Méthode permettant de savoir si l'objet vient d'être créé.
    * @return True si l'objet est nouveau.
    */
   public Boolean newEchantillon(){
      return (this.echantillonId == null);
   }

   @Override
   public Integer listableObjectId(){
      return this.getEchantillonId();
   }

   @Override
   public String entiteNom(){
      return "Echantillon";
   }

   @Transient
   public InputStream getAnapathStream(){
      return anapathStream;
   }

   public void setAnapathStream(final InputStream aS){
      this.anapathStream = aS;
   }

   @Override
   @Transient
   public String getPhantomData(){
      return code;
   }

   @Override
   @Transient
   public TKThesaurusObject getType(){
      return getEchantillonType();
   }

   @Override
   @Transient
   public void setType(final TKThesaurusObject o){
      setEchantillonType((EchantillonType) o);
   }

   @Override
   @Transient
   public Fichier getFile(){
      return getCrAnapath();
   }

   @Override
   @Transient
   public void setFile(final Fichier f){
      setCrAnapath(f);
   }

	@Override
	public void setDelegate(TKDelegateObject<Echantillon> _d) {
		this.delegate = _d;
	}
}
