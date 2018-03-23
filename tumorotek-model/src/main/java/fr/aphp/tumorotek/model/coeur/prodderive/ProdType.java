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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;

/**
 *
 * Objet persistant mappant la table PROD_TYPE.
 * Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "PROD_TYPE")
@NamedQueries(value = {@NamedQuery(name = "ProdType.findByType", query = "SELECT p FROM ProdType p WHERE p.type like ?1"),
   @NamedQuery(name = "ProdType.findByProdDeriveId",
      query = "SELECT p FROM ProdType p " + "left join p.prodDerives d " + "WHERE d.prodDeriveId = ?1"),
   @NamedQuery(name = "ProdType.findByExcludedId", query = "SELECT p FROM ProdType p " + "WHERE p.prodTypeId != ?1"),
   @NamedQuery(name = "ProdType.findByOrder", query = "SELECT p FROM ProdType p " + "WHERE p.plateforme = ?1 ORDER BY p.type")})
public class ProdType implements Serializable, TKThesaurusObject
{

   private static final long serialVersionUID = -8819167609321186090L;

   private Integer prodTypeId;
   private String type;
   private Plateforme plateforme;

   private Set<ProdDerive> prodDerives;
   private Set<CouleurEntiteType> couleurEntiteTypes = new HashSet<>();

   /** Constructeur par défaut. */
   public ProdType(){
      super();
      prodDerives = new HashSet<>();
   }

   @Id
   @Column(name = "PROD_TYPE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getProdTypeId(){
      return this.prodTypeId;
   }

   public void setProdTypeId(final Integer id){
      this.prodTypeId = id;
   }

   @Column(name = "TYPE", nullable = false, length = 200)
   public String getType(){
      return this.type;
   }

   public void setType(final String t){
      this.type = t;
   }

   @OneToMany(mappedBy = "prodType")
   public Set<ProdDerive> getProdDerives(){
      return prodDerives;
   }

   public void setProdDerives(final Set<ProdDerive> prodDs){
      this.prodDerives = prodDs;
   }

   @OneToMany(mappedBy = "prodType")
   public Set<CouleurEntiteType> getCouleurEntiteTypes(){
      return couleurEntiteTypes;
   }

   public void setCouleurEntiteTypes(final Set<CouleurEntiteType> cTypes){
      this.couleurEntiteTypes = cTypes;
   }

   @Override
   @ManyToOne
   @JoinColumn(name = "PLATEFORME_ID", nullable = false)
   public Plateforme getPlateforme(){
      return plateforme;
   }

   @Override
   public void setPlateforme(final Plateforme pf){
      this.plateforme = pf;
   }

   /**
    * 2 objets sont considérés comme égaux s'ils ont le même type et 
    * la même plateforme.
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
      final ProdType test = (ProdType) obj;
      return ((this.type == test.type || (this.type != null && this.type.equals(test.type)))
         && (this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme))));
   }

   /**
    * Le hashcode est calculé sur l'attribut type et plateforme.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashType = 0;
      int hashPF = 0;

      if(this.type != null){
         hashType = this.type.hashCode();
      }
      if(this.plateforme != null){
         hashPF = this.plateforme.hashCode();
      }

      hash = 31 * hash + hashType;
      hash = 31 * hash + hashPF;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.type != null){
         return "{" + this.type + "}";
      }else{
         return "{Empty ProdType}";
      }
   }

   @Override
   @Transient
   public String getNom(){
      return getType();
   }

   @Override
   @Transient
   public Integer getId(){
      return getProdTypeId();
   }
}
