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
package fr.aphp.tumorotek.model.cession;

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

import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.cession.delegate.AbstractCessionDelegate;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 *
 * Objet persistant mappant la table CESSION.
 * Classe créée le 15/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.1
 *
 */
@Entity
@Table(name = "CESSION")
@NamedQueries(value = {@NamedQuery(name = "Cession.findByNumero", query = "SELECT c FROM Cession c WHERE c.numero like ?1"),
   @NamedQuery(name = "Cession.findByNumeroWithBanqueReturnIds",
      query = "SELECT c.cessionId FROM Cession c WHERE c.numero like ?1 " + "AND c.banque in (?2)"),
   @NamedQuery(name = "Cession.findByExcludedIdNumeros",
      query = "SELECT c.numero FROM Cession c " + "WHERE c.cessionId != ?1 AND c.banque = ?2 " + "ORDER BY c.numero"),
   @NamedQuery(name = "Cession.findByBanqueSelectNumero",
      query = "SELECT c.numero FROM Cession c " + "WHERE c.banque = ?1 " + "ORDER BY c.numero"),
   @NamedQuery(name = "Cession.findByNumeroInPlateforme",
      query = "SELECT c FROM Cession c WHERE c.numero like ?1 " + "AND c.banque.plateforme = ?2"),
   @NamedQuery(name = "Cession.findByBanqueWithOrder",
      query = "SELECT c FROM Cession c WHERE c.banque = ?1 " + "ORDER BY c.numero"),
   @NamedQuery(name = "Cession.findByCessionStatutAndBanqueReturnIds",
      query = "SELECT c.cessionId FROM Cession c " + "WHERE c.cessionStatut.statut = ?1 " + "AND c.banque in (?2)"),
   @NamedQuery(name = "Cession.findByEtatIncompletAndBanquesReturnIds",
      query = "SELECT c.cessionId FROM Cession c " + "WHERE c.etatIncomplet = ?1 " + "AND c.banque in (?2)"),
   @NamedQuery(name = "Cession.findByDemandeDate", query = "SELECT c FROM Cession c WHERE c.demandeDate = ?1"),
   @NamedQuery(name = "Cession.findByDescription", query = "SELECT c FROM Cession c WHERE c.description = ?1"),
   @NamedQuery(name = "Cession.findByValidationDate", query = "SELECT c FROM Cession c WHERE c.validationDate = ?1"),
   @NamedQuery(name = "Cession.findByDepartDate", query = "SELECT c FROM Cession c WHERE c.departDate = ?1"),
   @NamedQuery(name = "Cession.findByArriveeDate", query = "SELECT c FROM Cession c WHERE c.arriveeDate = ?1"),
   @NamedQuery(name = "Cession.findByObservations", query = "SELECT c FROM Cession c WHERE c.observations = ?1"),
   @NamedQuery(name = "Cession.findByTemperature", query = "SELECT c FROM Cession c WHERE c.temperature = ?1"),
   @NamedQuery(name = "Cession.findByDestructionDate", query = "SELECT c FROM Cession c WHERE c.destructionDate = ?1"),
   @NamedQuery(name = "Cession.findByEtatIncomplet", query = "SELECT c FROM Cession c WHERE c.etatIncomplet = ?1"),
   @NamedQuery(name = "Cession.findByArchive", query = "SELECT c FROM Cession c WHERE c.archive = ?1"),
   @NamedQuery(name = "Cession.findByCessionType", query = "SELECT c FROM Cession c " + "WHERE c.cessionType = ?1"),
   @NamedQuery(name = "Cession.findByCessionExamen", query = "SELECT c FROM Cession c " + "WHERE c.cessionExamen = ?1"),
   @NamedQuery(name = "Cession.findByContrat",
      query = "SELECT c FROM Cession c WHERE c.contrat = ?1 " + "ORDER BY c.departDate ASC"),
   @NamedQuery(name = "Cession.findByDestinataire", query = "SELECT c FROM Cession c " + "WHERE c.destinataire = ?1"),
   @NamedQuery(name = "Cession.findByServiceDest", query = "SELECT c FROM Cession c " + "WHERE c.serviceDest = ?1"),
   @NamedQuery(name = "Cession.findByDemandeur", query = "SELECT c FROM Cession c " + "WHERE c.demandeur= ?1"),
   @NamedQuery(name = "Cession.findByExecutant", query = "SELECT c FROM Cession c " + "WHERE c.executant = ?1"),
   @NamedQuery(name = "Cession.findByTransporteur", query = "SELECT c FROM Cession c " + "WHERE c.transporteur = ?1"),
   @NamedQuery(name = "Cession.findByDestructionMotif", query = "SELECT c FROM Cession c " + "WHERE c.destructionMotif = ?1"),
   @NamedQuery(name = "Cession.findByBanques", query = "SELECT c FROM Cession c WHERE c.banque in (?1) " + "ORDER BY c.numero"),
   @NamedQuery(name = "Cession.findDoublon", query = "SELECT c FROM Cession c " + "WHERE c.numero = ?1 AND c.banque = ?2"),
   @NamedQuery(name = "Cession.findByIdInList", query = "SELECT c FROM Cession c " + "WHERE c.cessionId in (?1)"),
   @NamedQuery(name = "Cession.findByBanquesAllIds", query = "SELECT c.cessionId FROM Cession c " + "WHERE c.banque in (?1)"),
   @NamedQuery(name = "Cession.findCountByExecutant", query = "SELECT count(c) FROM Cession c " + "WHERE c.executant = ?1"),
   @NamedQuery(name = "Cession.findCountByDemandeur", query = "SELECT count(c) FROM Cession c " + "WHERE c.demandeur = ?1"),
   @NamedQuery(name = "Cession.findCountByDestinataire",
      query = "SELECT count(c) FROM Cession c " + "WHERE c.destinataire = ?1"),})
public class Cession implements TKAnnotableObject, Serializable
{

   private static final long serialVersionUID = 8712594639435839849L;

   private Integer cessionId;

   private String numero;

   private Date demandeDate;

   private String etudeTitre;

   private String description;

   private Date validationDate;

   private Calendar departDate;

   private Calendar arriveeDate;

   private String observations;

   private Float temperature;

   private Calendar destructionDate;

   private Boolean etatIncomplet;

   private Boolean archive = false;

   // @since 2.1
   private Calendar lastScanCheckDate;

   /** @since 2.2.0 */
   private AbstractCessionDelegate delegate;

   private Banque banque;

   private CessionType cessionType;

   private CessionExamen cessionExamen;

   private Contrat contrat;

   private Collaborateur destinataire;

   private Service serviceDest;

   private Collaborateur demandeur;

   private CessionStatut cessionStatut;

   private Collaborateur executant;

   private Transporteur transporteur;

   private DestructionMotif destructionMotif;

   private Set<Retour> retours = new HashSet<>();

   private Set<CederObjet> cederObjets = new HashSet<>();

   /** Constructeur par défaut. */
   public Cession(){}

   @Id
   @Column(name = "CESSION_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getCessionId(){
      return this.cessionId;
   }

   public void setCessionId(final Integer id){
      this.cessionId = id;
   }

   @Column(name = "NUMERO", nullable = false)
   public String getNumero(){
      return this.numero;
   }

   public void setNumero(final String num){
      this.numero = num;
   }

   @Temporal(TemporalType.DATE)
   @Column(name = "DEMANDE_DATE", nullable = true)
   public Date getDemandeDate(){
      if(demandeDate != null){
         return new Date(demandeDate.getTime());
      }
      return null;
   }

   public void setDemandeDate(final Date date){
      if(date != null){
         this.demandeDate = new Date(date.getTime());
      }else{
         this.demandeDate = null;
      }
   }

   @Column(name = "ETUDE_TITRE", nullable = true, length = 100)
   public String getEtudeTitre(){
      return this.etudeTitre;
   }

   public void setEtudeTitre(final String titre){
      this.etudeTitre = titre;
   }

   @Column(name = "DESCRIPTION", nullable = true)
   //@Lob
   public String getDescription(){
      return this.description;
   }

   public void setDescription(final String desc){
      this.description = desc;
   }

   @Temporal(TemporalType.DATE)
   @Column(name = "VALIDATION_DATE", nullable = true)
   public Date getValidationDate(){
      if(validationDate != null){
         return new Date(validationDate.getTime());
      }
      return null;
   }

   public void setValidationDate(final Date date){
      if(date != null){
         this.validationDate = new Date(date.getTime());
      }else{
         this.validationDate = null;
      }
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DEPART_DATE", nullable = true)
   public Calendar getDepartDate(){
      if(departDate != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(departDate.getTime());
         return cal;
      }
      return null;
   }

   public void setDepartDate(final Calendar cal){
      if(cal != null){
         this.departDate = Calendar.getInstance();
         this.departDate.setTime(cal.getTime());
      }else{
         this.departDate = null;
      }
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "ARRIVEE_DATE", nullable = true)
   public Calendar getArriveeDate(){
      if(arriveeDate != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(arriveeDate.getTime());
         return cal;
      }
      return null;
   }

   public void setArriveeDate(final Calendar cal){
      if(cal != null){
         this.arriveeDate = Calendar.getInstance();
         this.arriveeDate.setTime(cal.getTime());
      }else{
         this.arriveeDate = null;
      }
   }

   @Column(name = "OBSERVATIONS", nullable = true, length = 250)
   public String getObservations(){
      return this.observations;
   }

   public void setObservations(final String obs){
      this.observations = obs;
   }

   @Column(name = "TEMPERATURE", nullable = true)
   public Float getTemperature(){
      return this.temperature;
   }

   public void setTemperature(final Float temp){
      this.temperature = temp;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DESTRUCTION_DATE", nullable = true)
   public Calendar getDestructionDate(){
      if(destructionDate != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(destructionDate.getTime());
         return cal;
      }
      return null;
   }

   public void setDestructionDate(final Calendar cal){
      if(cal != null){
         this.destructionDate = Calendar.getInstance();
         this.destructionDate.setTime(cal.getTime());
      }else{
         this.destructionDate = null;
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
      return this.banque;
   }

   @Override
   public void setBanque(final Banque bank){
      this.banque = bank;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST})
   @JoinColumn(name = "CESSION_TYPE_ID", nullable = false)
   public CessionType getCessionType(){
      return this.cessionType;
   }

   public void setCessionType(final CessionType type){
      this.cessionType = type;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST})
   @JoinColumn(name = "CESSION_EXAMEN_ID", nullable = true)
   public CessionExamen getCessionExamen(){
      return this.cessionExamen;
   }

   public void setCessionExamen(final CessionExamen examen){
      this.cessionExamen = examen;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "CONTRAT_ID", nullable = true)
   public Contrat getContrat(){
      return this.contrat;
   }

   public void setContrat(final Contrat m){
      this.contrat = m;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "DESTINATAIRE_ID", nullable = true)
   public Collaborateur getDestinataire(){
      return this.destinataire;
   }

   public void setDestinataire(final Collaborateur dest){
      this.destinataire = dest;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "SERVICE_DEST_ID", nullable = true)
   public Service getServiceDest(){
      return this.serviceDest;
   }

   public void setServiceDest(final Service service){
      this.serviceDest = service;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "DEMANDEUR_ID", nullable = true)
   public Collaborateur getDemandeur(){
      return this.demandeur;
   }

   public void setDemandeur(final Collaborateur dem){
      this.demandeur = dem;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "CESSION_STATUT_ID", nullable = false)
   public CessionStatut getCessionStatut(){
      return this.cessionStatut;
   }

   public void setCessionStatut(final CessionStatut statut){
      this.cessionStatut = statut;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "EXECUTANT_ID", nullable = true)
   public Collaborateur getExecutant(){
      return this.executant;
   }

   public void setExecutant(final Collaborateur exe){
      this.executant = exe;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "TRANSPORTEUR_ID", nullable = true)
   public Transporteur getTransporteur(){
      return this.transporteur;
   }

   public void setTransporteur(final Transporteur transport){
      this.transporteur = transport;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "DESTRUCTION_MOTIF_ID", nullable = true)
   public DestructionMotif getDestructionMotif(){
      return this.destructionMotif;
   }

   public void setDestructionMotif(final DestructionMotif destructMotif){
      this.destructionMotif = destructMotif;
   }

   @OneToMany(mappedBy = "cession", cascade = {CascadeType.REMOVE})
   public Set<Retour> getRetours(){
      return this.retours;
   }

   public void setRetours(final Set<Retour> rets){
      this.retours = rets;
   }

   @OneToMany(mappedBy = "pk.cession", fetch = FetchType.LAZY)
   public Set<CederObjet> getCederObjets(){
      return this.cederObjets;
   }

   public void setCederObjets(final Set<CederObjet> cederObjs){
      this.cederObjets = cederObjs;
   }

   @OneToOne(mappedBy = "delegator", cascade = CascadeType.MERGE, orphanRemoval = true)
   public AbstractCessionDelegate getDelegate(){
      return delegate;
   }

   public void setDelegate(final AbstractCessionDelegate delegate){
      this.delegate = delegate;
   }

   /**
    * 2 cessions sont considérées comme égales si elles ont le même
    * numéro et la même banque.
    * @param obj est la cession à tester.
    * @return true si les cessions sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Cession test = (Cession) obj;
      if(this.numero == null){
         if(test.numero == null){
            if(this.banque == null){
               return (test.banque == null);
            }
            return (this.banque.equals(test.banque));
         }
         return false;
      }else if(this.banque == null){
         if(test.banque == null){
            return (this.numero.equals(test.numero));
         }
         return false;
      }else{
         return (this.numero.equals(test.numero) && this.banque.equals(test.banque));
      }
   }

   /**
    * Le hashcode est calculé sur les attributs numero et banque.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNumero = 0;
      int hashBanque = 0;

      if(this.numero != null){
         hashNumero = this.numero.hashCode();
      }
      if(this.banque != null){
         hashBanque = this.banque.hashCode();
      }

      hash = 31 * hash + hashNumero;
      hash = 31 * hash + hashBanque;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.numero != null){
         return "{" + this.numero + "}";
      }
      return "{Empty Cession}";
   }

   @Override
   public Cession clone(){
      final Cession clone = new Cession();

      clone.setCessionId(this.getCessionId());
      clone.setNumero(this.getNumero());
      clone.setBanque(this.getBanque());
      clone.setCessionType(this.getCessionType());
      clone.setDemandeDate(this.getDemandeDate());
      clone.setCessionExamen(this.getCessionExamen());
      clone.setContrat(this.getContrat());
      clone.setEtudeTitre(this.getEtudeTitre());
      clone.setDestinataire(this.getDestinataire());
      clone.setServiceDest(this.getServiceDest());
      clone.setDescription(this.getDescription());
      clone.setDemandeur(this.getDemandeur());
      clone.setCessionStatut(this.getCessionStatut());
      clone.setValidationDate(this.getValidationDate());
      clone.setExecutant(this.getExecutant());
      clone.setTransporteur(this.getTransporteur());
      clone.setDepartDate(this.getDepartDate());
      clone.setArriveeDate(this.getArriveeDate());
      clone.setObservations(this.getObservations());
      clone.setTemperature(this.getTemperature());
      clone.setDestructionMotif(this.getDestructionMotif());
      clone.setDestructionDate(this.getDestructionDate());
      clone.setEtatIncomplet(this.getEtatIncomplet());
      clone.setArchive(this.getArchive());
      clone.setLastScanCheckDate(this.getLastScanCheckDate());
      clone.setDelegate(this.getDelegate());

      return clone;

   }

   @Override
   public Integer listableObjectId(){
      return this.getCessionId();
   }

   @Override
   public String entiteNom(){
      return "Cession";
   }

   @Override
   @Transient
   public String getPhantomData(){
      return numero.toString();
   }

   @Column(name = "LAST_SCAN_CHECK_DATE", nullable = true)
   public Calendar getLastScanCheckDate(){
      if(lastScanCheckDate != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(lastScanCheckDate.getTime());
         return cal;
      }
      return null;
   }

   public void setLastScanCheckDate(final Calendar cal){
      if(cal != null){
         this.lastScanCheckDate = Calendar.getInstance();
         this.lastScanCheckDate.setTime(cal.getTime());
      }else{
         this.lastScanCheckDate = null;
      }
   }
}
