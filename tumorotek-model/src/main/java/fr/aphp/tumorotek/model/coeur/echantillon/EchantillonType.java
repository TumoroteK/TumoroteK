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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.AbstractPfDependantThesaurusObject;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;

/**
 *
 * Objet persistant mappant la table ECHANTILLON_TYPE.
 * Classe créée le 10/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "ECHANTILLON_TYPE")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "ECHANTILLON_TYPE_ID")),
   @AttributeOverride(name = "nom", column = @Column(name = "TYPE", nullable = false, length = 200))})
@GenericGenerator(name = "autoincrement", strategy = "increment")
@NamedQueries(
   value = {@NamedQuery(name = "EchantillonType.findByType", query = "SELECT e FROM EchantillonType e WHERE e.nom like ?1"),
      @NamedQuery(name = "EchantillonType.findByIncaCat", query = "SELECT e FROM EchantillonType e " + "WHERE e.incaCat like ?1"),
      @NamedQuery(name = "EchantillonType.findByEchantillonId",
         query = "SELECT e FROM EchantillonType e " + "left join e.echantillons h " + "WHERE h.echantillonId = ?1"),
      @NamedQuery(name = "EchantillonType.findByExcludedId", query = "SELECT e FROM EchantillonType e " + "WHERE e.id != ?1"),
      @NamedQuery(name = "EchantillonType.findByPfOrder",
         query = "SELECT e FROM EchantillonType e " + "WHERE e.plateforme = ?1 ORDER BY e.nom"),
      @NamedQuery(name = "EchantillonType.findByOrder",
      query = "SELECT e FROM EchantillonType e ORDER BY e.nom")})
public class EchantillonType extends AbstractPfDependantThesaurusObject implements Serializable
{

   private static final long serialVersionUID = 47864535434464543L;

   private String incaCat;

   private Set<Echantillon> echantillons;
   private Set<CouleurEntiteType> couleurEntiteTypes = new HashSet<>();

   /** Constructeur. */
   public EchantillonType(){
      echantillons = new HashSet<>();
   }

   /**
    * Constructeur avec paramètres.
    * @param id .
    * @param t .
    * @param inca .
    */
   public EchantillonType(final Integer id, final String t, final String inca){
      this.setId(id);
      this.setNom(t);
      this.incaCat = inca;
   }

   /**
    * @deprecated Utiliser {@link #getId()}
    * @return
    */
   @Deprecated
   @Transient
   public Integer getEchantillonTypeId(){
      return this.getId();
   }

   /**
    * @deprecated Utiliser {@link #setId(Integer)}
    * @return
    */
   @Deprecated
   public void setEchantillonTypeId(final Integer eId){
      this.setId(eId);
   }

   /**
    * @deprecated Utiliser {@link #getNom()}
    * @return
    */
   @Deprecated
   @Transient
   public String getType(){
      return this.getNom();
   }

   /**
    * @deprecated Utiliser {@link #setNom(String)}
    * @param t
    */
   @Deprecated
   public void setType(final String t){
      this.setNom(t);
   }

   @Column(name = "INCA_CAT", nullable = true, length = 10)
   public String getIncaCat(){
      return incaCat;
   }

   public void setIncaCat(final String inca){
      this.incaCat = inca;
   }

   @OneToMany(mappedBy = "echantillonType")
   public Set<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final Set<Echantillon> echants){
      this.echantillons = echants;
   }

   @OneToMany(mappedBy = "echantillonType")
   public Set<CouleurEntiteType> getCouleurEntiteTypes(){
      return couleurEntiteTypes;
   }

   public void setCouleurEntiteTypes(final Set<CouleurEntiteType> cTypes){
      this.couleurEntiteTypes = cTypes;
   }

   /**
    * 2 objets sont considérés comme égaux s'ils ont le même type et
    * la même catégorie inca.
    * @param obj est l'objet à tester.
    * @return true si les objets sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final EchantillonType test = (EchantillonType) obj;
      return ((this.getNom() == test.getNom() || (this.getNom() != null && this.getNom().equals(test.getNom())))
         && (this.incaCat == test.incaCat || (this.incaCat != null && this.incaCat.equals(test.incaCat)))
         && (this.getPlateforme() == test.getPlateforme()
            || (this.getPlateforme() != null && this.getPlateforme().equals(test.getPlateforme()))));
   }

   /**
    * Le hashcode est calculé sur les attributs type et incaCat.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashType = 0;
      int hashIncaCat = 0;
      int hashPF = 0;

      if(this.getNom() != null){
         hashType = this.getNom().hashCode();
      }
      if(this.incaCat != null){
         hashIncaCat = this.incaCat.hashCode();
      }
      if(this.getPlateforme() != null){
         hashPF = this.getPlateforme().hashCode();
      }

      hash = 31 * hash + hashType;
      hash = 31 * hash + hashIncaCat;
      hash = 31 * hash + hashPF;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.getNom() != null){
         return "{" + this.getNom() + "}";
      }
      return "{Empty EchantillonType}";
   }

}
