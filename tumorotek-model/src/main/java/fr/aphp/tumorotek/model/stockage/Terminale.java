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
 * Objet persistant mappant la table TERMINALE.
 * Classe créée le 14/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "TERMINALE")
@NamedQueries(
   value = {
      @NamedQuery(name = "Terminale.findByEnceinteWithOrder",
         query = "SELECT t FROM Terminale t " + "WHERE t.enceinte = ?1 " + "ORDER BY t.position"),
      @NamedQuery(name = "Terminale.findByEnceinteAndNom",
         query = "SELECT t FROM Terminale t " + "WHERE t.enceinte = ?1 " + "AND t.nom = ?2"),
      @NamedQuery(name = "Terminale.findByEnceinteAndPosition",
         query = "SELECT t FROM Terminale t " + "WHERE t.enceinte = ?1 " + "AND t.position = ?2"),
      @NamedQuery(name = "Terminale.findByEnceinteAndPositionExcludedId",
         query = "SELECT t FROM Terminale t " + "WHERE t.enceinte = ?1 " + "AND t.position = ?2 " + "AND t.terminaleId != ?3"),
      @NamedQuery(name = "Terminale.findByExcludedIdEnceinte",
         query = "SELECT t FROM Terminale t " + "WHERE t.terminaleId != ?1 " + "AND t.enceinte = ?2"),
      @NamedQuery(name = "Terminale.findByTwoExcludedIdsWithEnceinte",
         query = "SELECT t FROM Terminale t " + "WHERE t.terminaleId != ?1 " + "AND t.terminaleId != ?2 "
            + "AND t.enceinte = ?3"),
      @NamedQuery(name = "Terminale.findNumberTerminalesForEnceinte",
         query = "SELECT count(t) FROM Terminale t " + "WHERE t.enceinte = ?1"),
      @NamedQuery(name = "Terminale.findByNom", query = "SELECT t FROM Terminale t WHERE t.nom = ?1"),
      @NamedQuery(name = "Terminale.findByAlias", query = "SELECT t FROM Terminale t WHERE t.alias = ?1"),
      @NamedQuery(name = "Terminale.findByArchive", query = "SELECT t FROM Terminale t WHERE t.archive = ?1"),
      @NamedQuery(name = "Terminale.findByTerminaleType", query = "SELECT t FROM Terminale t " + "WHERE t.terminaleType = ?1"),
      @NamedQuery(name = "Terminale.findByEnceinte", query = "SELECT t FROM Terminale t " + "WHERE t.enceinte= ?1"),
      @NamedQuery(name = "Terminale.findByBanque", query = "SELECT t FROM Terminale t WHERE t.banque = ?1"),
      @NamedQuery(name = "Terminale.findByEntite", query = "SELECT t FROM Terminale t WHERE t.entite= ?1"),
      @NamedQuery(name = "Terminale.findByTerminaleNumerotation",
         query = "SELECT t FROM Terminale t " + "WHERE t.terminaleNumerotation = ?1"),
      @NamedQuery(name = "Terminale.findDoublon", query = "SELECT t FROM Terminale t WHERE t.nom = ?1" + " AND t.enceinte = ?2"),
      @NamedQuery(name = "Terminale.findByIdWithFetch",
         query = "SELECT t FROM Terminale t LEFT JOIN FETCH " + "t.terminaleType LEFT JOIN FETCH t.enceinte "
            + "LEFT JOIN FETCH t.banque LEFT JOIN FETCH t.entite " + "LEFT JOIN FETCH t.terminaleNumerotation "
            + "WHERE t.terminaleId = ?1")})
public class Terminale implements TKdataObject, TKFantomableObject, Serializable
{

   private static final long serialVersionUID = 5820184473392520641L;

   private Integer terminaleId;
   private String nom;
   private Integer position;
   private String alias;
   private Boolean archive = false;

   private TerminaleType terminaleType;
   private Enceinte enceinte;
   private Banque banque;
   private Entite entite;
   private TerminaleNumerotation terminaleNumerotation;
   private Couleur couleur;

   private Set<Emplacement> emplacements = new HashSet<>();
   private Set<Incident> incidents = new HashSet<>();

   public Terminale(){}

   @Id
   @Column(name = "TERMINALE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getTerminaleId(){
      return this.terminaleId;
   }

   public void setTerminaleId(final Integer id){
      this.terminaleId = id;
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
      return this.position;
   }

   public void setPosition(final Integer pl){
      this.position = pl;
   }

   @Column(name = "ALIAS", nullable = true, length = 50)
   public String getAlias(){
      return this.alias;
   }

   public void setAlias(final String al){
      this.alias = al;
   }

   @Column(name = "ARCHIVE", nullable = false)
   public Boolean getArchive(){
      return this.archive;
   }

   public void setArchive(final Boolean arch){
      this.archive = arch;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "TERMINALE_TYPE_ID", nullable = false)
   public TerminaleType getTerminaleType(){
      return this.terminaleType;
   }

   public void setTerminaleType(final TerminaleType type){
      this.terminaleType = type;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST})
   @JoinColumn(name = "ENCEINTE_ID", nullable = false)
   public Enceinte getEnceinte(){
      return this.enceinte;
   }

   public void setEnceinte(final Enceinte enc){
      this.enceinte = enc;
   }

   @ManyToOne
   @JoinColumn(name = "BANQUE_ID", nullable = true)
   public Banque getBanque(){
      return this.banque;
   }

   public void setBanque(final Banque bank){
      this.banque = bank;
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
   @JoinColumn(name = "TERMINALE_NUMEROTATION_ID", nullable = false)
   public TerminaleNumerotation getTerminaleNumerotation(){
      return this.terminaleNumerotation;
   }

   public void setTerminaleNumerotation(final TerminaleNumerotation numerotation){
      this.terminaleNumerotation = numerotation;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "COULEUR_ID", nullable = true)
   public Couleur getCouleur(){
      return couleur;
   }

   public void setCouleur(final Couleur c){
      this.couleur = c;
   }

   @OneToMany(mappedBy = "terminale", cascade = {CascadeType.REMOVE})
   @OrderBy("position")
   public Set<Emplacement> getEmplacements(){
      return this.emplacements;
   }

   public void setEmplacements(final Set<Emplacement> empls){
      this.emplacements = empls;
   }

   @OneToMany(mappedBy = "terminale", cascade = {CascadeType.REMOVE})
   public Set<Incident> getIncidents(){
      return this.incidents;
   }

   public void setIncidents(final Set<Incident> incids){
      this.incidents = incids;
   }

   /**
    * 2 terminales sont considérées comme égales si elles ont les mêmes
    * noms et enceintes.
    * @param obj est la terminale à tester.
    * @return true si les terminales sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Terminale test = (Terminale) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
         && (this.enceinte == test.enceinte || (this.enceinte != null && this.enceinte.equals(test.enceinte))));
   }

   /**
    * Le hashcode est calculé sur les attributs nom et enceinte.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;
      int hashEnceinte = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.enceinte != null){
         hashEnceinte = this.enceinte.hashCode();
      }

      hash = 31 * hash + hashNom;
      hash = 31 * hash + hashEnceinte;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.nom != null){
         return "{" + this.nom + "}";
      }else{
         return "{Empty Terminale}";
      }
   }

   /**
    * Cree un clone de l'objet.
    * @return clone Cession.
    */
   @Override
   public Terminale clone(){
      final Terminale clone = new Terminale();

      clone.setTerminaleId(this.terminaleId);
      clone.setEnceinte(this.enceinte);
      clone.setTerminaleType(this.terminaleType);
      clone.setNom(this.nom);
      clone.setPosition(this.position);
      clone.setAlias(this.alias);
      clone.setBanque(this.banque);
      clone.setEntite(this.entite);
      clone.setArchive(this.archive);
      clone.setTerminaleNumerotation(this.terminaleNumerotation);
      clone.setCouleur(this.couleur);

      clone.setEmplacements(this.emplacements);

      return clone;
   }

   @Override
   public String entiteNom(){
      return "Terminale";
   }

   @Override
   @Transient
   public String getPhantomData(){
      return getNom();
   }

   @Override
   public Integer listableObjectId(){
      return getTerminaleId();
   }
}
