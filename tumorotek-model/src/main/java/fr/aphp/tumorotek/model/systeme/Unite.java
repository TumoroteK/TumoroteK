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
package fr.aphp.tumorotek.model.systeme;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.AbstractThesaurusObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;

/**
 *
 * Objet persistant mappant la table UNITE.
 * Classe créée le 10/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "UNITE")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "UNITE_ID")),
   @AttributeOverride(name = "nom", column = @Column(name = "UNITE", nullable = false, length = 200))})
@GenericGenerator(name = "autoincrement", strategy = "increment")
//@NamedQueries(value = {@NamedQuery(name = "Unite.findByUnite", query = "SELECT u FROM Unite u WHERE u.nom like ?1"),
//   @NamedQuery(name = "Unite.findByTypeWithOrder", query = "SELECT u FROM Unite u WHERE u.type like ?1 " + "ORDER BY u.nom"),
//   @NamedQuery(name = "Unite.findByOrder", query = "SELECT u FROM Unite u ORDER BY u.nom")})
public class Unite extends AbstractThesaurusObject implements java.io.Serializable
{

   private static final long serialVersionUID = 468743654364634L;

//   private Integer uniteId;
//   private String unite;
   private String type;

   private Set<Prelevement> prelevementQuantites = new HashSet<>();
   //private Set<Prelevement> prelevementVolumes = new HashSet<Prelevement>();
   private Set<Echantillon> echantillonQuantites = new HashSet<>();
   //private Set<Echantillon> echantillonVolumes = new HashSet<Echantillon>();
   private Set<ProdDerive> prodDeriveQuantites = new HashSet<>();
   private Set<ProdDerive> prodDeriveConcs = new HashSet<>();
   private Set<ProdDerive> prodDeriveVolumes = new HashSet<>();
   private Set<CederObjet> cederObjetQuantites = new HashSet<>();
   //private Set<CederObjet> cederObjetVolumes = new HashSet<CederObjet>();
   private Set<Transformation> transformationQuantites = new HashSet<>();
   /*private Set<Transformation> transformationVolumes = 
   	new HashSet<Transformation>();*/

   /** Constructeur par défaut. */
   public Unite(){
      type = "masse";
   }

   /** 
    * Constructeur avec paramètres.
    * @param id .
    * @param u .
    * @param t .
    */
   public Unite(final Integer id, final String u, final String t){
      this.setId(id);
      this.setNom(u);
      this.type = t;
   }

   /**
    * @deprecated Utiliser {@link #getId()}
    * @return
    */
   @Deprecated
   @Transient
   public Integer getUniteId(){
      return this.getId();
   }

   public void setUniteId(final Integer id){
      this.setId(id);
   }

   /**
    * @deprecated Utiliser {@link #getNom()}
    * @return
    */
   @Deprecated
   @Transient
   public String getUnite(){
      return this.getNom();
   }

   
   public void setUnite(final String u){
      this.setNom(u);
   }

   @Column(name = "TYPE", nullable = false, length = 15)
   public String getType(){
      return type;
   }

   public void setType(final String t){
      this.type = t;
   }

   @OneToMany(mappedBy = "quantiteUnite")
   public Set<Prelevement> getPrelevementQuantites(){
      return prelevementQuantites;
   }

   public void setPrelevementQuantites(final Set<Prelevement> quantites){
      this.prelevementQuantites = quantites;
   }

   /*@OneToMany(mappedBy = "volumeUnite")
   public Set<Prelevement> getPrelevementVolumes() {
   	return prelevementVolumes;
   }
   
   public void setPrelevementVolumes(Set<Prelevement> volumes) {
   	this.prelevementVolumes = volumes;
   }*/

   @OneToMany(mappedBy = "quantiteUnite")
   public Set<Echantillon> getEchantillonQuantites(){
      return echantillonQuantites;
   }

   public void setEchantillonQuantites(final Set<Echantillon> quantites){
      this.echantillonQuantites = quantites;
   }

   /*@OneToMany(mappedBy = "volumeUnite")
   public Set<Echantillon> getEchantillonVolumes() {
   	return echantillonVolumes;
   }
   
   public void setEchantillonVolumes(Set<Echantillon> volumes) {
   	this.echantillonVolumes = volumes;
   }*/

   @OneToMany(mappedBy = "quantiteUnite")
   public Set<ProdDerive> getProdDeriveQuantites(){
      return prodDeriveQuantites;
   }

   public void setProdDeriveQuantites(final Set<ProdDerive> quantites){
      this.prodDeriveQuantites = quantites;
   }

   @OneToMany(mappedBy = "concUnite")
   public Set<ProdDerive> getProdDeriveConcs(){
      return prodDeriveConcs;
   }

   public void setProdDeriveConcs(final Set<ProdDerive> concs){
      this.prodDeriveConcs = concs;
   }

   @OneToMany(mappedBy = "volumeUnite")
   public Set<ProdDerive> getProdDeriveVolumes(){
      return prodDeriveVolumes;
   }

   public void setProdDeriveVolumes(final Set<ProdDerive> volumes){
      this.prodDeriveVolumes = volumes;
   }

   @OneToMany(mappedBy = "quantiteUnite")
   public Set<CederObjet> getCederObjetQuantites(){
      return cederObjetQuantites;
   }

   public void setCederObjetQuantites(final Set<CederObjet> cQuantites){
      this.cederObjetQuantites = cQuantites;
   }

   /*@OneToMany(mappedBy = "volumeUnite")
   public Set<CederObjet> getCederObjetVolumes() {
   	return cederObjetVolumes;
   }
   
   public void setCederObjetVolumes(Set<CederObjet> cVolumes) {
   	this.cederObjetVolumes = cVolumes;
   }*/

   @OneToMany(mappedBy = "quantiteUnite")
   public Set<Transformation> getTransformationQuantites(){
      return transformationQuantites;
   }

   public void setTransformationQuantites(final Set<Transformation> tQuantites){
      this.transformationQuantites = tQuantites;
   }

   /*@OneToMany(mappedBy = "volumeUnite")
   public Set<Transformation> getTransformationVolumes() {
   	return transformationVolumes;
   }
   
   public void setTransformationVolumes(Set<Transformation> tVolumes) {
   	this.transformationVolumes = tVolumes;
   }*/

   /**
    * 2 unités sont considérées comme égales si elles ont le même type
    * et la même unité.
    * @param obj est l'unité à tester.
    * @return true si les unités sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Unite test = (Unite) obj;
      return ((this.type == test.type || (this.type != null && this.type.equals(test.type)))
         && (this.getNom() == test.getNom() || (this.getNom() != null && this.getNom().equals(test.getNom()))));
   }

   /**
    * Le hashcode est calculé sur les attributs unite et type.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashUnite = 0;
      int hashType = 0;

      if(this.getNom() != null){
         hashUnite = this.getNom().hashCode();
      }
      if(this.type != null){
         hashType = this.type.hashCode();
      }

      hash = 31 * hash + hashUnite;
      hash = 31 * hash + hashType;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.type != null && this.getNom() != null){
         return "{" + this.getNom() + ", " + this.type + "}";
      }
         return "{Empty Unite}";
   }

}
