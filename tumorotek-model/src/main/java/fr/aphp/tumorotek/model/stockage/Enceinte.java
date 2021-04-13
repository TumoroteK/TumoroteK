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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table ENCEINTE.
 * Classe créée le 14/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "ENCEINTE")
@NamedQueries(
   value = {
      @NamedQuery(name = "Enceinte.findByConteneurWithOrder",
         query = "SELECT e FROM Enceinte e WHERE e.conteneur = ?1 " + "ORDER BY e.position"),
      @NamedQuery(name = "Enceinte.findByEnceintePereWithOrder",
         query = "SELECT e FROM Enceinte e WHERE e.enceintePere = ?1 " + "ORDER BY e.position"),
      @NamedQuery(name = "Enceinte.findByConteneurSelectNom",
         query = "SELECT e.nom FROM Enceinte e WHERE e.conteneur = ?1 " + "ORDER BY e.position"),
      @NamedQuery(name = "Enceinte.findByEnceintePereSelectNom",
         query = "SELECT e.nom FROM Enceinte e WHERE e.enceintePere = ?1 " + "ORDER BY e.position"),
      @NamedQuery(name = "Enceinte.findNumberEnceinteFilles",
         query = "SELECT count(e) FROM Enceinte e " + "WHERE e.enceintePere = ?1"),
      @NamedQuery(name = "Enceinte.findByExcludedIdWithConteneur",
         query = "SELECT e FROM Enceinte e " + "WHERE e.enceinteId != ?1 " + "AND e.conteneur = ?2"),
      @NamedQuery(name = "Enceinte.findByTwoExcludedIdsWithConteneur",
         query = "SELECT e FROM Enceinte e " + "WHERE e.enceinteId != ?1 " + "AND e.enceinteId != ?2 " + "AND e.conteneur = ?3"),
      @NamedQuery(name = "Enceinte.findByExcludedIdWithEnceinte",
         query = "SELECT e FROM Enceinte e " + "WHERE e.enceinteId != ?1 " + "AND e.enceintePere = ?2"),
      @NamedQuery(name = "Enceinte.findByTwoExcludedIdsWithEnceinte",
         query = "SELECT e FROM Enceinte e " + "WHERE e.enceinteId != ?1 " + "AND e.enceinteId != ?2 "
            + "AND e.enceintePere = ?3"),
      @NamedQuery(name = "Enceinte.findByEnceintePereAndPosition",
         query = "SELECT e FROM Enceinte e " + "WHERE e.enceintePere = ?1 " + "AND e.position = ?2"),
      @NamedQuery(name = "Enceinte.findByEnceintePereAndPositionExcludedId",
         query = "SELECT e FROM Enceinte e " + "WHERE e.enceintePere = ?1 " + "AND e.position = ?2 " + "AND e.enceinteId != ?3"),
      @NamedQuery(name = "Enceinte.findByConteneurAndPosition",
         query = "SELECT e FROM Enceinte e " + "WHERE e.conteneur = ?1 " + "AND e.position = ?2"),
      @NamedQuery(name = "Enceinte.findByConteneurAndPositionExcludedId",
         query = "SELECT e FROM Enceinte e " + "WHERE e.conteneur = ?1 " + "AND e.position = ?2 " + "AND e.enceinteId != ?3"),
      @NamedQuery(name = "Enceinte.findByNom", query = "SELECT e FROM Enceinte e WHERE e.nom = ?1"),
      @NamedQuery(name = "Enceinte.findByAlias", query = "SELECT e FROM Enceinte e WHERE e.alias = ?1"),
      @NamedQuery(name = "Enceinte.findByNbPlaces", query = "SELECT e FROM Enceinte e WHERE e.nbPlaces = ?1"),
      @NamedQuery(name = "Enceinte.findByArchive", query = "SELECT e FROM Enceinte e WHERE e.archive = ?1"),
      @NamedQuery(name = "Enceinte.findByConteneur", query = "SELECT e FROM Enceinte e " + "WHERE e.conteneur= ?1"),
      @NamedQuery(name = "Enceinte.findByEnceintePere", query = "SELECT e FROM Enceinte e " + "WHERE e.enceintePere= ?1"),
      @NamedQuery(name = "Enceinte.findByConteneurAndNom",
         query = "SELECT e FROM Enceinte e " + "WHERE e.conteneur= ?1 " + "AND e.nom = ?2"),
      @NamedQuery(name = "Enceinte.findByEnceintePereAndNom",
         query = "SELECT e FROM Enceinte e " + "WHERE e.enceintePere= ?1 " + "AND e.nom = ?2"),
      @NamedQuery(name = "Enceinte.findByEntite", query = "SELECT e FROM Enceinte e WHERE e.entite= ?1"),
      @NamedQuery(name = "Enceinte.findByEnceinteType", query = "SELECT e FROM Enceinte e " + "WHERE e.enceinteType= ?1"),
      @NamedQuery(name = "Enceinte.findDoublon",
         query = "SELECT e FROM Enceinte e " + "WHERE e.nom = ?1" + " AND e.conteneur= ?2 AND e.enceintePere= ?3"),
      @NamedQuery(name = "Enceinte.findByIdWithFetch",
         query = "SELECT e FROM Enceinte e LEFT JOIN FETCH " + "e.conteneur LEFT JOIN FETCH e.enceintePere "
            + "LEFT JOIN FETCH e.entite LEFT JOIN FETCH e.enceinteType " + "WHERE e.enceinteId = ?1")})
public class Enceinte implements TKdataObject, TKFantomableObject, Serializable
{

   private static final long serialVersionUID = -384780298211476975L;

   private Integer enceinteId;
   private String nom;
   private Integer position;
   private String alias;
   private Integer nbPlaces;
   private Boolean archive = false;

   private Conteneur conteneur;
   private Enceinte enceintePere;
   private Entite entite;
   private EnceinteType enceinteType;
   private Couleur couleur;

   private Set<Terminale> terminales = new HashSet<>();
   private Set<Enceinte> enceintes = new HashSet<>();
   private Set<Banque> banques = new HashSet<>();
   private Set<Incident> incidents = new HashSet<>();

   /** Constructeur par défaut. */
   public Enceinte(){}

   @Id
   @Column(name = "ENCEINTE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getEnceinteId(){
      return this.enceinteId;
   }

   public void setEnceinteId(final Integer id){
      this.enceinteId = id;
   }

   @Column(name = "NOM", nullable = false, length = 50)
   public String getNom(){
      return this.nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "POSITION", nullable = false)
   public Integer getPosition(){
      return position;
   }

   public void setPosition(final Integer pos){
      this.position = pos;
   }

   @Column(name = "ALIAS", nullable = true, length = 50)
   public String getAlias(){
      return this.alias;
   }

   public void setAlias(final String al){
      this.alias = al;
   }

   @Column(name = "NB_PLACES", nullable = true)
   public Integer getNbPlaces(){
      return this.nbPlaces;
   }

   public void setNbPlaces(final Integer nb){
      this.nbPlaces = nb;
   }

   @Column(name = "ARCHIVE", nullable = false)
   public Boolean getArchive(){
      return this.archive;
   }

   public void setArchive(final Boolean arch){
      this.archive = arch;
   }

   //@ManyToOne(cascade = {CascadeType.PERSIST })
   @ManyToOne
   @JoinColumn(name = "CONTENEUR_ID", nullable = true)
   public Conteneur getConteneur(){
      return this.conteneur;
   }

   public void setConteneur(final Conteneur c){
      this.conteneur = c;
   }

   //@ManyToOne(cascade = {CascadeType.PERSIST })
   @ManyToOne
   @JoinColumn(name = "ENCEINTE_PERE_ID", nullable = true)
   public Enceinte getEnceintePere(){
      return this.enceintePere;
   }

   public void setEnceintePere(final Enceinte enceinte){
      this.enceintePere = enceinte;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "ENTITE_ID", nullable = true)
   public Entite getEntite(){
      return this.entite;
   }

   public void setEntite(final Entite ent){
      this.entite = ent;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "ENCEINTE_TYPE_ID", nullable = false)
   public EnceinteType getEnceinteType(){
      return this.enceinteType;
   }

   public void setEnceinteType(final EnceinteType type){
      this.enceinteType = type;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "COULEUR_ID", nullable = true)
   public Couleur getCouleur(){
      return couleur;
   }

   public void setCouleur(final Couleur c){
      this.couleur = c;
   }

   @OneToMany(mappedBy = "enceinte", cascade = {CascadeType.REMOVE})
   @OrderBy("position")
   public Set<Terminale> getTerminales(){
      return this.terminales;
   }

   public void setTerminales(final Set<Terminale> terms){
      this.terminales = terms;
   }

   @OneToMany(mappedBy = "enceintePere", cascade = {CascadeType.REMOVE})
   @OrderBy("position")
   public Set<Enceinte> getEnceintes(){
      return this.enceintes;
   }

   public void setEnceintes(final Set<Enceinte> encs){
      this.enceintes = encs;
   }

   /*@ManyToMany(
   		cascade = {CascadeType.PERSIST, CascadeType.MERGE },
           mappedBy = "enceintes",
           targetEntity = Banque.class
   )*/
   @ManyToMany(targetEntity = Banque.class)
   @JoinTable(name = "ENCEINTE_BANQUE", joinColumns = @JoinColumn(name = "ENCEINTE_ID"),
      inverseJoinColumns = @JoinColumn(name = "BANQUE_ID"))
   public Set<Banque> getBanques(){
      return this.banques;
   }

   public void setBanques(final Set<Banque> banks){
      this.banques = banks;
   }

   @OneToMany(mappedBy = "enceinte", cascade = {CascadeType.REMOVE})
   public Set<Incident> getIncidents(){
      return this.incidents;
   }

   public void setIncidents(final Set<Incident> incids){
      this.incidents = incids;
   }

   /**
    * 2 enceintes sont considérées comme égales si elles ont le même
    * nom et le même conteneur et le même père.
    * @param obj est l'enceinte à tester.
    * @return true si les enceintes sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Enceinte test = (Enceinte) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
         && (this.conteneur == test.conteneur || (this.conteneur != null && this.conteneur.equals(test.conteneur)))
         && (this.enceintePere == test.enceintePere
            || (this.enceintePere != null && this.enceintePere.equals(test.enceintePere))));
   }

   /**
    * Le hashcode est calculé sur les attributs nom, conteneur et
    * enceintePere.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;
      int hashConteneur = 0;
      int hashPere = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.conteneur != null){
         hashConteneur = this.conteneur.hashCode();
      }
      if(this.enceintePere != null){
         hashPere = this.enceintePere.hashCode();
      }

      hash = 7 * hash + hashNom;
      hash = 7 * hash + hashConteneur;
      hash = 7 * hash + hashPere;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.nom != null){
         return "{" + this.nom + "}";
      }
      return "{Empty Enceinte}";
   }

   /**
    * Cree un clone de l'objet.
    * @return clone Enceinte.
    */
   @Override
   public Enceinte clone(){
      final Enceinte clone = new Enceinte();

      clone.setEnceinteId(this.enceinteId);
      clone.setEnceinteType(this.enceinteType);
      clone.setConteneur(this.conteneur);
      clone.setEnceintePere(this.enceintePere);
      clone.setNom(this.nom);
      clone.setPosition(this.position);
      clone.setAlias(this.alias);
      clone.setNbPlaces(this.nbPlaces);
      clone.setEntite(this.entite);
      clone.setArchive(this.archive);
      clone.setCouleur(this.couleur);

      clone.setBanques(this.banques);
      clone.setEnceintes(this.enceintes);
      clone.setTerminales(this.terminales);

      return clone;
   }

   @Override
   public String entiteNom(){
      return "Enceinte";
   }

   @Override
   @Transient
   public String getPhantomData(){
      return getNom();
   }

   @Override
   public Integer listableObjectId(){
      return getEnceinteId();
   }
}
