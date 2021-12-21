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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table EMPLACEMENT.
 * Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "EMPLACEMENT")
//@NamedQueries(value = {
//   @NamedQuery(name = "Emplacement.findByTerminaleWithOrder",
//      query = "SELECT e FROM Emplacement e " + "WHERE e.terminale = ?1 " + "ORDER BY e.position"),
//   @NamedQuery(name = "Emplacement.findByTerminaleAndVide",
//      query = "SELECT e FROM Emplacement e " + "WHERE e.terminale = ?1 " + "AND e.vide = ?2 "
//         + "ORDER BY e.position, e.entite.entiteId, e.objetId"),
//   @NamedQuery(name = "Emplacement.findByCountTerminaleAndVide",
//      query = "SELECT count(e) FROM Emplacement e " + "WHERE e.terminale = ?1 " + "AND e.vide = ?2"),
//   @NamedQuery(name = "Emplacement.findByObjetIdEntite",
//      query = "SELECT e FROM Emplacement e " + "WHERE e.objetId = ?1 " + "AND e.entite = ?2"),
//   @NamedQuery(name = "Emplacement.findByTerminaleAndPosition",
//      query = "SELECT e FROM Emplacement e " + "WHERE e.terminale = ?1 " + "AND e.position = ?2"),
//   @NamedQuery(name = "Emplacement.findByExcludedIdTerminale",
//      query = "SELECT e FROM Emplacement e " + "WHERE e.emplacementId != ?1 " + "AND e.terminale = ?2"),
//   @NamedQuery(name = "Emplacement.findByObjetId", query = "SELECT e FROM Emplacement e WHERE e.objetId = ?1"),
//   @NamedQuery(name = "Emplacement.findByEntite", query = "SELECT e FROM Emplacement e WHERE e.entite = ?1"),
//   @NamedQuery(name = "Emplacement.findByVide", query = "SELECT e FROM Emplacement e WHERE e.vide = ?1"),
//   @NamedQuery(name = "Emplacement.findByAdrp", query = "SELECT e FROM Emplacement e WHERE e.adrp = ?1"),
//   @NamedQuery(name = "Emplacement.findDouplon", query = "SELECT e FROM Emplacement e WHERE e.adrl = ?1"),
//   @NamedQuery(name = "Emplacement.findByIdWithFetch",
//      query = "SELECT e FROM Emplacement e LEFT JOIN FETCH " + "e.terminale WHERE e.emplacementId = ?1")
//   // @NamedQuery(name = "Emplacement.getAdrl", 
//   //	query = "SELECT function('get_adrl', 1)")
//})
public class Emplacement implements Serializable
{

   private static final long serialVersionUID = -2139186761577452739L;

   private Integer emplacementId;
   private Terminale terminale;
   private Integer position;
   private Integer objetId;
   private Entite entite;
   private Boolean vide = true;
   private String adrp;
   private String adrl;

   //private Set<Retour> retours = new HashSet<Retour>();
   private Set<ProdDerive> prodDerives = new HashSet<>();
   private Set<Echantillon> echantillons = new HashSet<>();

   /** Constructeur par défaut. */
   public Emplacement(){}

   @Id
   @Column(name = "EMPLACEMENT_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getEmplacementId(){
      return this.emplacementId;
   }

   public void setEmplacementId(final Integer id){
      this.emplacementId = id;
   }

   @ManyToOne
   // @ManyToOne(cascade = {CascadeType.PERSIST })
   @JoinColumn(name = "TERMINALE_ID", nullable = false)
   public Terminale getTerminale(){
      return this.terminale;
   }

   public void setTerminale(final Terminale term){
      this.terminale = term;
   }

   @Column(name = "POSITION", nullable = false)
   public Integer getPosition(){
      return this.position;
   }

   public void setPosition(final Integer pl){
      this.position = pl;
   }

   @Column(name = "OBJET_ID", nullable = true)
   public Integer getObjetId(){
      return this.objetId;
   }

   public void setObjetId(final Integer objet){
      this.objetId = objet;
   }

   @ManyToOne
   //@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
   @JoinColumn(name = "ENTITE_ID", nullable = true)
   public Entite getEntite(){
      return this.entite;
   }

   public void setEntite(final Entite ent){
      this.entite = ent;
   }

   @Column(name = "VIDE", nullable = true)
   public Boolean getVide(){
      return this.vide;
   }

   public void setVide(final Boolean v){
      this.vide = v;
   }

   @Column(name = "ADRP", nullable = true, length = 25)
   public String getAdrp(){
      return this.adrp;
   }

   public void setAdrp(final String adr){
      this.adrp = adr;
   }

   @Column(name = "ADRL", nullable = true, length = 50)
   public String getAdrl(){
      return this.adrl;
   }

   public void setAdrl(final String adr){
      this.adrl = adr;
   }

   //	@OneToMany(mappedBy = "oldEmplacement", cascade = { CascadeType.REMOVE })
   //	public Set<Retour> getRetours() {
   //		return retours;
   //	}
   //
   //	public void setRetours(Set<Retour> rets) {
   //		this.retours = rets;
   //	}

   @OneToMany(mappedBy = "emplacement")
   public Set<ProdDerive> getProdDerives(){
      return prodDerives;
   }

   public void setProdDerives(final Set<ProdDerive> prodDers){
      this.prodDerives = prodDers;
   }

   @OneToMany(mappedBy = "emplacement")
   public Set<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final Set<Echantillon> echans){
      this.echantillons = echans;
   }

   /**
    * 2 emplacements sont considérés comme égaux s'ils ont les mêmes
    * adresses logiques.
    * @param obj est l'emplacement à tester.
    * @return true si les emplacements sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Emplacement test = (Emplacement) obj;
      return ((this.position == test.position || (this.position != null && this.position.equals(test.position)))
         && (this.terminale == test.terminale || (this.terminale != null && this.terminale.equals(test.terminale))));
   }

   /**
    * Le hashcode est calculé sur les attributs adrl.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashPosition = 0;
      int hashTerminae = 0;

      if(this.position != null){
         hashPosition = this.position.hashCode();
      }
      if(this.terminale != null){
         hashTerminae = this.terminale.hashCode();
      }

      hash = 31 * hash + hashPosition;
      hash = 31 * hash + hashTerminae;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.position != null && this.terminale != null){
         return "{" + this.position + " " + this.terminale.toString() + "}";
      }
      return "{Empty Emplacement}";
   }

   /**
    * Cree un clone de l'objet.
    * @return clone Emplacement.
    */
   @Override
   public Emplacement clone(){
      final Emplacement clone = new Emplacement();

      clone.setEmplacementId(this.emplacementId);
      clone.setTerminale(this.terminale);
      clone.setPosition(this.position);
      clone.setObjetId(this.objetId);
      clone.setEntite(this.entite);
      clone.setVide(this.vide);
      clone.setAdrl(this.adrl);
      clone.setAdrp(this.adrp);

      return clone;
   }
}
