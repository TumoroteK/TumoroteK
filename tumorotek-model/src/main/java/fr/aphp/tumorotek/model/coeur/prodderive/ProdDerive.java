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
package fr.aphp.tumorotek.model.coeur.prodderive;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKDelegateObject;
import fr.aphp.tumorotek.model.TKDelegetableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.prodderive.delegate.AbstractProdDeriveDelegate;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utils.Utils;

/**
 *
 * Objet persistant mappant la table PROD_DERIVE.
 * Classe créée le 14/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.2.3-rc1
 *
 */
@Entity
@Table(name = "PROD_DERIVE")
@NamedQueries(value = {@NamedQuery(name = "ProdDerive.findByCode", query = "SELECT p FROM ProdDerive p WHERE p.code like ?1"),
   @NamedQuery(name = "ProdDerive.findByCodeOrLaboWithBanque",
      query = "SELECT p FROM ProdDerive p WHERE " + "(p.code like ?1 OR p.codeLabo like ?1) " + "AND p.banque = ?2"),
   @NamedQuery(name = "ProdDerive.findByCodeInPlateforme",
      query = "SELECT p FROM ProdDerive p WHERE p.code like ?1 " + "AND p.banque.plateforme = ?2"),
   @NamedQuery(name = "ProdDerive.findByCodeOrLaboWithBanqueReturnIds",
      query = "SELECT p.prodDeriveId FROM ProdDerive p WHERE " + "(p.code like ?1 OR p.codeLabo like ?1) " + "AND p.banque = ?2"),
   @NamedQuery(name = "ProdDerive.findByCodeLabo", query = "SELECT p FROM ProdDerive p WHERE p.codeLabo like ?1"),
   @NamedQuery(name = "ProdDerive.findByDateStockAfterDate", query = "SELECT p FROM ProdDerive p WHERE p.dateStock > ?1"),
   @NamedQuery(name = "ProdDerive.findByDateTransformationAfterDate",
      query = "SELECT p FROM ProdDerive p " + "WHERE p.dateTransformation > ?1"),
   @NamedQuery(name = "ProdDerive." + "findByDateTransformationAfterDateWithBanque",
      query = "SELECT p FROM ProdDerive p " + "WHERE p.dateTransformation > ?1 " + "AND p.banque = ?2"),
   @NamedQuery(name = "ProdDerive.findByEtatIncomplet", query = "SELECT p FROM ProdDerive p WHERE p.etatIncomplet = ?1"),
   @NamedQuery(name = "ProdDerive.findByExcludedIdCodes",
      query = "SELECT p.code FROM ProdDerive p " + "WHERE p.prodDeriveId != ?1 and banque = ?2"),
   @NamedQuery(name = "ProdDerive.findByProdType", query = "SELECT p FROM ProdDerive p " + "WHERE p.prodType = ?1"),
   @NamedQuery(name = "ProdDerive.findByObjetStatut", query = "SELECT p FROM ProdDerive p " + "WHERE p.objetStatut = ?1"),
   @NamedQuery(name = "ProdDerive.findByProdQualite", query = "SELECT p FROM ProdDerive p " + "WHERE p.prodQualite = ?1"),
   @NamedQuery(name = "ProdDerive.findByCollaborateur", query = "SELECT p FROM ProdDerive p " + "WHERE p.collaborateur = ?1"),
   @NamedQuery(name = "ProdDerive.findByEmplacement",
      query = "SELECT p FROM ProdDerive p " + "WHERE p.emplacement.terminale = ?1 " + "AND p.emplacement.position = ?2"),
   @NamedQuery(name = "ProdDerive.findByModePrepaDerive", query = "SELECT p FROM ProdDerive p " + "WHERE p.modePrepaDerive = ?1"),
   @NamedQuery(name = "ProdDerive.findByBanqueSelectCode", query = "SELECT p.code FROM ProdDerive p " + "WHERE p.banque = ?1"),
   @NamedQuery(name = "ProdDerive.findByBanqueAndQuantiteSelectCode",
      query = "SELECT p.code FROM ProdDerive p " + "WHERE p.banque = ?1 " + "AND (p.quantite > 0 OR p.quantite IS NULL) "
         + "AND p.objetStatut.statut not in ('EPUISE', 'ENCOURS', 'RESERVE')"),
   @NamedQuery(name = "ProdDerive.findAllCodesByBanqueAndQuantiteNotNullOrInCessionTraitement",
      query = "SELECT p.code FROM ProdDerive p " + "WHERE p.banque = ?1 " + "AND (((p.quantite > 0 OR p.quantite IS NULL) "
         + "AND p.objetStatut.statut not in ('EPUISE', 'ENCOURS', 'RESERVE'))" + "OR ("
         + "p.prodDeriveId in (SELECT c.pk.objetId FROM CederObjet c WHERE c.pk.entite.nom = 'ProdDerive' AND c.pk.cession.cessionType.type = 'Traitement' AND c.statut = 'TRAITEMENT'"
         + ")))"),
   @NamedQuery(name = "ProdDerive.findByBanqueStatutSelectCode",
      query = "SELECT p.code FROM ProdDerive p " + "WHERE p.banque = ?1 AND p.objetStatut = ?2 " + "ORDER BY p.code"),
   @NamedQuery(name = "ProdDerive.findByBanqueInListStatutSelectCode",
      query = "SELECT p.code FROM ProdDerive p " + "WHERE p.banque in (?1) AND p.objetStatut = ?2 " + "ORDER BY p.code"),
   @NamedQuery(name = "ProdDerive.findByTransformation",
      query = "SELECT p FROM ProdDerive p " + "WHERE p.transformation = ?1 ORDER BY p.prodDeriveId"),
   @NamedQuery(name = "ProdDerive.findByTerminale",
      query = "SELECT p FROM ProdDerive p " + "WHERE p.prodDeriveId IN " + "(select empl.objetId FROM Emplacement empl "
         + "WHERE empl.entite = ?1 AND empl.terminale = ?2)"),
   @NamedQuery(name = "ProdDerive.findByBanques",
      query = "SELECT p FROM ProdDerive p WHERE p.banque in (?1) " + "ORDER BY p.banque, p.code"),
   @NamedQuery(name = "ProdDerive.findByTerminaleDirect",
      query = "SELECT p FROM ProdDerive p " + "WHERE p.emplacement.terminale = ?1)"),
   @NamedQuery(name = "ProdDerive.findByParentAndType",
      query = "SELECT p FROM ProdDerive p " + "WHERE p.transformation.objetId = ?1 " + "AND p.transformation.entite = ?2 "
         + "AND p.prodType.nom like ?3"),
   @NamedQuery(name = "ProdDerive.findByEchantillonPatientNomReturnIds",
      query = "SELECT p.prodDeriveId FROM ProdDerive p, Echantillon e " + "JOIN e.prelevement as prlvt "
         + "JOIN prlvt.maladie as m " + "JOIN m.patient as pat " + "WHERE p.transformation.objetId = e.echantillonId "
         + "AND p.transformation.entite.nom = 'Echantillon' " + "AND (pat.nom like ?1 OR pat.nip like ?1)  AND p.banque = ?2"),
   @NamedQuery(name = "ProdDerive.findByPrelevementPatientNomreturnIds",
      query = "SELECT p.prodDeriveId " + "FROM ProdDerive p, Prelevement prlvt " + "JOIN prlvt.maladie as m "
         + "JOIN m.patient as pat " + "WHERE p.transformation.objetId = prlvt.prelevementId "
         + "AND p.transformation.entite.nom = 'Prelevement' " + "AND pat.nom like ?1 AND p.banque = ?2"),
   @NamedQuery(name = "ProdDerive.findByIdInList", query = "SELECT p FROM ProdDerive p " + "WHERE p.prodDeriveId in (?1)"),
   @NamedQuery(name = "ProdDerive.findByBanquesAllIds",
      query = "SELECT p.prodDeriveId FROM ProdDerive p " + "WHERE p.banque in (?1)"),
   @NamedQuery(name = "ProdDerive.findByParent",
      query = "SELECT p FROM ProdDerive p " + "WHERE p.transformation.objetId = ?1 " + "AND p.transformation.entite = ?2 "),
   @NamedQuery(name = "ProdDerive.findByCodeInListWithBanque",
      query = "SELECT p.prodDeriveId, p.code FROM ProdDerive p " + "WHERE p.code in (?1) " + "AND p.banque in (?2)"),
   @NamedQuery(name = "ProdDerive" + ".findByEchantillonPatientNomInListReturnIds",
      query = "SELECT p.prodDeriveId FROM ProdDerive p, Echantillon e " + "JOIN e.prelevement as prlvt "
         + "JOIN prlvt.maladie as m " + "JOIN m.patient as pat " + "WHERE p.transformation.objetId = e.echantillonId "
         + "AND p.transformation.entite.nom = 'Echantillon' " + "AND (pat.nom in (?1) OR pat.nip in (?1)) "
         + "AND p.banque in (?2)"),
   @NamedQuery(name = "ProdDerive" + ".findByPrelevementPatientNomInListreturnIds",
      query = "SELECT p.prodDeriveId " + "FROM ProdDerive p, Prelevement prlvt " + "JOIN prlvt.maladie as m "
         + "JOIN m.patient as pat " + "WHERE p.transformation.objetId = prlvt.prelevementId "
         + "AND p.transformation.entite.nom = 'Prelevement' " + "AND (pat.nom in (?1) OR pat.nip in (?1)) "
         + "AND p.banque in (?2)"),
   @NamedQuery(name = "ProdDerive.findCountCreatedByCollaborateur",
      query = "SELECT count(p) FROM ProdDerive p, Operation o " + "WHERE p.prodDeriveId = o.objetId "
         + "and p.collaborateur = (?1) " + "AND o.operationType.nom = 'Creation' " + "AND o.entite.nom = 'ProdDerive'"),
   @NamedQuery(name = "ProdDerive.findCountByOperateur",
      query = "SELECT count(p) FROM ProdDerive p " + "WHERE p.collaborateur = ?1"),
   @NamedQuery(name = "ProdDerive.findCountByParent",
      query = "SELECT count(p) FROM ProdDerive p " + "WHERE p.transformation.objetId = ?1 " + "AND p.transformation.entite = ?2"),
   @NamedQuery(name = "ProdDerive.findByListCodeWithPlateforme",
      query = "SELECT e FROM ProdDerive e JOIN e.banque bq JOIN bq.plateforme pf WHERE e.code in (?1) AND pf = ?2 "),
   @NamedQuery(name = "ProdDerive.findByBanksAndImpact",
   query = "SELECT e.prodDeriveId FROM ProdDerive e, Retour r " + "WHERE e.prodDeriveId = r.objetId "
      + "and e.banque in (?1)"+ "and r.impact in (?2) ")})
public class ProdDerive extends TKDelegetableObject<ProdDerive> implements TKStockableObject, Serializable
{

   private static final long serialVersionUID = 1110628569548421522L;

   private Integer prodDeriveId;
   private String code;
   private String codeLabo;
   private Float volumeInit;
   private Float volume;
   private Float conc;
   private Calendar dateStock;
   private Float quantiteInit;
   private Float quantite;
   private Calendar dateTransformation;
   private Boolean etatIncomplet;
   private Boolean archive = false;
   private Boolean conformeTraitement;
   private Boolean conformeCession;

   private Banque banque;
   private Unite quantiteUnite;
   private Unite concUnite;
   private ProdType prodType;
   private ObjetStatut objetStatut;
   private ProdQualite prodQualite;
   private Collaborateur collaborateur;
   private Emplacement emplacement;
   private Unite volumeUnite;
   private Transformation transformation;
   private ModePrepaDerive modePrepaDerive;

   private TKDelegateObject<ProdDerive> delegate;
   // private AbstractProdDeriveDelegate delegate;

   public ProdDerive(){
      super();
   }

   @Id
   @Column(name = "PROD_DERIVE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getProdDeriveId(){
      return this.prodDeriveId;
   }

   public void setProdDeriveId(final Integer id){
      this.prodDeriveId = id;
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

   @Column(name = "CODE_LABO", nullable = true, length = 50)
   public String getCodeLabo(){
      return this.codeLabo;
   }

   public void setCodeLabo(final String labo){
      this.codeLabo = labo;
   }

   @Column(name = "VOLUME_INIT", nullable = true)
   public Float getVolumeInit(){
      return Utils.floor(this.volumeInit, 3);
   }

   public void setVolumeInit(final Float volumeI){
      this.volumeInit = Utils.floor(volumeI, 3);
   }

   @Column(name = "VOLUME", nullable = true)
   public Float getVolume(){
      return Utils.floor(this.volume, 3);
   }

   public void setVolume(final Float vol){
      this.volume = Utils.floor(vol, 3);
   }

   @Column(name = "CONC", nullable = true)
   public Float getConc(){
      return Utils.floor(this.conc, 3);
   }

   public void setConc(final Float c){
      this.conc = Utils.floor(c, 3);
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

   @Column(name = "QUANTITE_INIT", nullable = true)
   public Float getQuantiteInit(){
      return Utils.floor(this.quantiteInit, 3);
   }

   public void setQuantiteInit(final Float quantiteI){
      this.quantiteInit = Utils.floor(quantiteI, 3);
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

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DATE_TRANSFORMATION", nullable = true)
   public Calendar getDateTransformation(){
      if(dateTransformation != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(dateTransformation.getTime());
         return cal;
      }
      return null;
   }

   public void setDateTransformation(final Calendar cal){
      if(cal != null){
         this.dateTransformation = Calendar.getInstance();
         this.dateTransformation.setTime(cal.getTime());
      }else{
         this.dateTransformation = null;
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

   @Override
   @ManyToOne
   @JoinColumn(name = "BANQUE_ID", nullable = false)
   public Banque getBanque(){
      return banque;
   }

   @Override
   public void setBanque(final Banque b){
      this.banque = b;
   }

   @Override
   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "QUANTITE_UNITE_ID", nullable = true)
   public Unite getQuantiteUnite(){
      return this.quantiteUnite;
   }

   @Override
   public void setQuantiteUnite(final Unite quantiteU){
      this.quantiteUnite = quantiteU;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "CONC_UNITE_ID", nullable = true)
   public Unite getConcUnite(){
      return this.concUnite;
   }

   public void setConcUnite(final Unite concU){
      this.concUnite = concU;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "PROD_TYPE_ID", nullable = false)
   public ProdType getProdType(){
      return this.prodType;
   }

   public void setProdType(final ProdType type){
      this.prodType = type;
   }

   @Override
   @ManyToOne
   @JoinColumn(name = "OBJET_STATUT_ID", nullable = false)
   public ObjetStatut getObjetStatut(){
      return this.objetStatut;
   }

   @Override
   public void setObjetStatut(final ObjetStatut statut){
      this.objetStatut = statut;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "PROD_QUALITE_ID", nullable = true)
   public ProdQualite getProdQualite(){
      return this.prodQualite;
   }

   public void setProdQualite(final ProdQualite qualite){
      this.prodQualite = qualite;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "COLLABORATEUR_ID", nullable = true)
   public Collaborateur getCollaborateur(){
      return this.collaborateur;
   }

   public void setCollaborateur(final Collaborateur collab){
      this.collaborateur = collab;
   }

   //@ManyToOne(fetch = FetchType.LAZY)
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

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "VOLUME_UNITE_ID", nullable = true)
   public Unite getVolumeUnite(){
      return this.volumeUnite;
   }

   public void setVolumeUnite(final Unite volumeU){
      this.volumeUnite = volumeU;
   }

   @ManyToOne
   @JoinColumn(name = "TRANSFORMATION_ID", nullable = true)
   public Transformation getTransformation(){
      return transformation;
   }

   public void setTransformation(final Transformation transfo){
      this.transformation = transfo;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "MODE_PREPA_DERIVE_ID", nullable = true)
   public ModePrepaDerive getModePrepaDerive(){
      return modePrepaDerive;
   }

   public void setModePrepaDerive(final ModePrepaDerive m){
      this.modePrepaDerive = m;
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

   @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "delegator",
		      targetEntity = AbstractProdDeriveDelegate.class)
   // @OneToOne(mappedBy = "delegator", cascade = CascadeType.MERGE, orphanRemoval = true)
   public TKDelegateObject<ProdDerive> getDelegate(){
      return delegate;
   }
   
   @Override
   public void setDelegate(TKDelegateObject<ProdDerive> _d){
      this.delegate = _d;
   }

//   /**
//    * @param delegate the delegate to set
//    */
//   public void setDelegate(AbstractProdDeriveDelegate delegate){
//      this.delegate = delegate;
//   }

   /**
    * 2 produits dérivés sont considérés comme égaux s'ils ont le même code
    * et la même banque.
    * @param obj est le produit dérivé à tester.
    * @return true si les produits dérivés sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ProdDerive test = (ProdDerive) obj;
      return ((this.code == test.code || (this.code != null && this.code.equals(test.code)))
         && (this.banque == test.banque || (this.banque != null && this.banque.equals(test.banque))));
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

      hash = 31 * hash + hashCode;
      hash = 31 * hash + hashBanque;

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
      return "{Empty ProdDerive}";
   }

   @Override
   public ProdDerive clone(){
      final ProdDerive clone = new ProdDerive();

      clone.setProdDeriveId(this.getProdDeriveId());
      clone.setBanque(this.getBanque());
      clone.setProdType(this.getProdType());
      clone.setCode(this.getCode());
      clone.setCodeLabo(this.getCodeLabo());
      clone.setObjetStatut(this.getObjetStatut());
      clone.setCollaborateur(this.getCollaborateur());
      clone.setVolumeInit(this.getVolumeInit());
      clone.setVolume(this.getVolume());
      clone.setConc(this.getConc());
      clone.setDateStock(this.getDateStock());
      clone.setEmplacement(this.getEmplacement());
      clone.setVolumeUnite(this.getVolumeUnite());
      clone.setConcUnite(this.getConcUnite());
      clone.setQuantiteInit(this.getQuantiteInit());
      clone.setQuantite(this.getQuantite());
      clone.setQuantiteUnite(this.getQuantiteUnite());
      clone.setProdQualite(this.getProdQualite());
      clone.setTransformation(this.getTransformation());
      clone.setDateTransformation(this.getDateTransformation());
      clone.setModePrepaDerive(this.getModePrepaDerive());
      clone.setEtatIncomplet(this.getEtatIncomplet());
      clone.setArchive(this.getArchive());
      clone.setConformeTraitement(this.getConformeTraitement());
      clone.setConformeCession(this.getConformeCession());
      
      clone.setDelegate(getDelegate());

      return clone;
   }

   @Override
   public Integer listableObjectId(){
      return this.getProdDeriveId();
   }

   @Override
   public String entiteNom(){
      return "ProdDerive";
   }

   @Override
   @Transient
   public String getPhantomData(){
      return code;
   }

   @Override
   @Transient
   public TKThesaurusObject getType(){
      return getProdType();
   }

   @Override
   @Transient
   public void setType(final TKThesaurusObject o){
      setProdType((ProdType) o);
   }
}