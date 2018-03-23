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
package fr.aphp.tumorotek.model.stockage;

import java.io.Serializable;
import java.util.Comparator;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 *
 * Objet persistant mappant la table CONTENEUR.
 * Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0.10
 *
 */
@Entity
@Table(name = "CONTENEUR")
@NamedQueries(
   value = {
      @NamedQuery(name = "Conteneur.findByBanqueIdWithOrder",
         query = "SELECT c FROM Conteneur c left join c.banques b " + "WHERE b.banqueId = ?1 AND c.archive = 0"
            + "ORDER BY c.nom"),
      @NamedQuery(name = "Conteneur.findByBanqueIdAndCode",
         query = "SELECT c FROM Conteneur c left join c.banques b " + "WHERE b.banqueId = ?1 AND c.archive = 0 "
            + "AND c.code = ?2"),
      @NamedQuery(name = "Conteneur.findByBanqueIdWithExcludedId",
         query = "SELECT c FROM Conteneur c left join c.banques b " + "WHERE b.banqueId = ?1 " + "AND c.conteneurId != ?2"),
      @NamedQuery(name = "Conteneur.findByPlateformeOrigWithOrder",
         query = "SELECT c FROM Conteneur c " + "WHERE c.plateformeOrig = ?1 AND c.archive = 0 " + "ORDER BY c.code"),
      @NamedQuery(name = "Conteneur.findByExcludedId",
         query = "SELECT c FROM Conteneur c " + "WHERE c.conteneurId != ?1 AND c.archive = 0 "),
      @NamedQuery(name = "Conteneur.findByCode", query = "SELECT c FROM Conteneur c WHERE c.code = ?1 AND c.archive = 0"),
      @NamedQuery(name = "Conteneur.findByNom", query = "SELECT c FROM Conteneur c WHERE c.nom = ?1 AND c.archive = 0"),
      @NamedQuery(name = "Conteneur.findByTemp", query = "SELECT c FROM Conteneur c WHERE c.temp = ?1 AND c.archive = 0"),
      @NamedQuery(name = "Conteneur.findByPiece", query = "SELECT c FROM Conteneur c WHERE c.piece = ?1 AND c.archive = 0"),
      @NamedQuery(name = "Conteneur.findByNbrNiv", query = "SELECT c FROM Conteneur c WHERE c.nbrNiv = ?1 AND c.archive = 0"),
      @NamedQuery(name = "Conteneur.findByNbrEnc", query = "SELECT c FROM Conteneur c WHERE c.nbrEnc = ?1 AND c.archive = 0"),
      @NamedQuery(name = "Conteneur.findByDescription",
         query = "SELECT c FROM Conteneur c WHERE c.description = ?1 AND c.archive = 0"),
      @NamedQuery(name = "Conteneur.findByArchive", query = "SELECT c FROM Conteneur c WHERE c.archive = ?1"),
      @NamedQuery(name = "Conteneur.findByConteneurType",
         query = "SELECT c FROM Conteneur c " + "WHERE c.conteneurType = ?1 AND c.archive = 0"),
      /*@NamedQuery(name = "Conteneur.findByService", 
      	query = "SELECT c FROM Conteneur c " 
      		+ "WHERE c.service= ?1 AND c.archive = 0"),*/
      @NamedQuery(name = "Conteneur.findDoublon",
         query = "SELECT c FROM Conteneur c " + "WHERE c.code = ?1 AND c.service= ?2 AND c.archive = 0"),
      @NamedQuery(name = "Conteneur.findByPartage",
         query = "SELECT c FROM Conteneur c " + "JOIN c.conteneurPlateformes p WHERE p.pk.plateforme = ?1 "
            + "AND p.partage = ?2 " + "AND c.archive = 0 ORDER by c.nom"),
      @NamedQuery(name = "Conteneur.findByIdWithFetch",
         query = "SELECT c FROM Conteneur c LEFT JOIN FETCH " + "c.conteneurType LEFT JOIN FETCH c.service "
            + "WHERE c.conteneurId = ?1 AND archive = 0"),
      @NamedQuery(name = "Conteneur.findByService", query = "SELECT c FROM Conteneur c " + "WHERE c.service = ?1"),
      @NamedQuery(name = "Conteneur.findTempForEmplacementId",
         query = "SELECT c.temp FROM Conteneur c " + "WHERE c.conteneurId = get_conteneur(?)")})
public class Conteneur implements TKdataObject, TKFantomableObject, Serializable
{

   private static final long serialVersionUID = 5584298407090931404L;

   private Integer conteneurId;
   private String code;
   private String nom;
   private Float temp;
   private String piece;
   private Integer nbrNiv;
   private Integer nbrEnc;
   private String description;
   private Boolean archive = false;

   private ConteneurType conteneurType;
   private Service service;
   private Plateforme plateformeOrig;

   private Set<Incident> incidents = new HashSet<>();
   private Set<Enceinte> enceintes = new HashSet<>();
   private Set<Banque> banques = new HashSet<>();
   private Set<ConteneurPlateforme> conteneurPlateformes = new HashSet<>();
   private Set<Retour> retours = new HashSet<>();

   /** Constructeur par défaut. */
   public Conteneur(){}

   @Id
   @Column(name = "CONTENEUR_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getConteneurId(){
      return this.conteneurId;
   }

   public void setConteneurId(final Integer id){
      this.conteneurId = id;
   }

   @Column(name = "CODE", nullable = false, length = 5)
   public String getCode(){
      return this.code;
   }

   public void setCode(final String c){
      this.code = c;
   }

   @Column(name = "NOM", nullable = false, length = 50)
   public String getNom(){
      return this.nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "TEMP", nullable = true)
   public Float getTemp(){
      return this.temp;
   }

   public void setTemp(final Float t){
      this.temp = t;
   }

   @Column(name = "PIECE", nullable = true, length = 20)
   public String getPiece(){
      return this.piece;
   }

   public void setPiece(final String p){
      this.piece = p;
   }

   @Column(name = "NBR_NIV", nullable = true)
   public Integer getNbrNiv(){
      return this.nbrNiv;
   }

   public void setNbrNiv(final Integer nbr){
      this.nbrNiv = nbr;
   }

   @Column(name = "NBR_ENC", nullable = true)
   public Integer getNbrEnc(){
      return this.nbrEnc;
   }

   public void setNbrEnc(final Integer nbr){
      this.nbrEnc = nbr;
   }

   @Column(name = "DESCRIPTION", nullable = true, length = 250)
   public String getDescription(){
      return this.description;
   }

   public void setDescription(final String desc){
      this.description = desc;
   }

   @Column(name = "ARCHIVE", nullable = false)
   public Boolean getArchive(){
      return this.archive;
   }

   public void setArchive(final Boolean arch){
      this.archive = arch;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "CONTENEUR_TYPE_ID", nullable = true)
   public ConteneurType getConteneurType(){
      return this.conteneurType;
   }

   public void setConteneurType(final ConteneurType cType){
      this.conteneurType = cType;
   }

   @ManyToOne
   @JoinColumn(name = "SERVICE_ID", nullable = false)
   public Service getService(){
      return this.service;
   }

   public void setService(final Service serv){
      this.service = serv;
   }

   @ManyToOne
   @JoinColumn(name = "PLATEFORME_ORIG_ID", nullable = false)
   public Plateforme getPlateformeOrig(){
      return plateformeOrig;
   }

   public void setPlateformeOrig(final Plateforme plateformeOrig){
      this.plateformeOrig = plateformeOrig;
   }

   @OneToMany(mappedBy = "conteneur", cascade = {CascadeType.REMOVE})
   public Set<Incident> getIncidents(){
      return this.incidents;
   }

   public void setIncidents(final Set<Incident> incids){
      this.incidents = incids;
   }

   @OneToMany(mappedBy = "conteneur")
   @OrderBy("position")
   public Set<Enceinte> getEnceintes(){
      return this.enceintes;
   }

   public void setEnceintes(final Set<Enceinte> encs){
      this.enceintes = encs;
   }

   @ManyToMany(targetEntity = Banque.class, mappedBy = "conteneurs")
   public Set<Banque> getBanques(){
      return this.banques;
   }

   public void setBanques(final Set<Banque> banks){
      this.banques = banks;
   }

   @OneToMany(mappedBy = "pk.conteneur", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
   public Set<ConteneurPlateforme> getConteneurPlateformes(){
      return this.conteneurPlateformes;
   }

   public void setConteneurPlateformes(final Set<ConteneurPlateforme> pfs){
      this.conteneurPlateformes = pfs;
   }

   @OneToMany(mappedBy = "conteneur")
   public Set<Retour> getRetours(){
      return retours;
   }

   public void setRetours(final Set<Retour> rets){
      this.retours = rets;
   }

   /**
    * 2 conteneurs sont considérés comme égaux s'ils ont le même
    * code et la meme plateforme d'origine
    * @param obj est le conteneur à tester.
    * @return true si les conteneurs sont égaux.
    * @since 2.0.10
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Conteneur test = (Conteneur) obj;
      return ((this.plateformeOrig == test.plateformeOrig
         || (this.plateformeOrig != null && this.plateformeOrig.equals(test.plateformeOrig)))
         && (this.code == test.code || (this.code != null && this.code.equals(test.code))));
   }

   /**
    * Le hashcode est calculé sur l'attribut code.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashCode = 0;
      int hashPf = 0;

      if(this.code != null){
         hashCode = this.code.hashCode();
      }
      if(this.plateformeOrig != null){
         hashPf = this.plateformeOrig.hashCode();
      }

      hash = 31 * hash + hashCode;
      hash = 31 * hash + hashPf;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.code != null){
         return "{" + this.code + "}";
      }else{
         return "{Empty Conteneur}";
      }
   }

   /**
    * Cree un clone de l'objet.
    * @return clone Cession.
    */
   @Override
   public Conteneur clone(){
      final Conteneur clone = new Conteneur();

      clone.setConteneurId(this.conteneurId);
      clone.setConteneurType(this.conteneurType);
      clone.setCode(this.code);
      clone.setNom(this.nom);
      clone.setTemp(this.temp);
      clone.setPiece(this.piece);
      clone.setNbrNiv(this.nbrNiv);
      clone.setNbrEnc(this.nbrEnc);
      clone.setDescription(this.description);
      clone.setService(this.service);
      clone.setArchive(this.archive);
      clone.setPlateformeOrig(getPlateformeOrig());

      clone.setBanques(this.banques);
      clone.setConteneurPlateformes(this.conteneurPlateformes);
      clone.setEnceintes(this.enceintes);
      clone.setIncidents(this.incidents);
      clone.setRetours(getRetours());

      return clone;
   }

   @Override
   public String entiteNom(){
      return "Conteneur";
   }

   @Override
   @Transient
   public String getPhantomData(){
      return getNom();
   }

   /**
    * Comparator permettant d'ordonner une liste de conteneurs par leur noms.
    * Date: 02/12/2013
    * 
    * @author Mathieu BARTHELEMY
    * @version 2.0.10
    *
    */
   public static class NomComparator implements Comparator<Conteneur>
   {
      @Override
      public int compare(final Conteneur c1, final Conteneur c2){
         return c1.getNom().compareToIgnoreCase(c2.getNom());
      }
   }

   @Override
   public Integer listableObjectId(){
      return getConteneurId();
   }
}
