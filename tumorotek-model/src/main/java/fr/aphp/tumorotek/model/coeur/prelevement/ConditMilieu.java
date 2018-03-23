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
package fr.aphp.tumorotek.model.coeur.prelevement;

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

/**
 *
 * Objet persistant mappant la table CONDIT_MILIEU.
 * Classe créée le 14/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "CONDIT_MILIEU")
@NamedQueries(
   value = {@NamedQuery(name = "ConditMilieu.findByMilieu", query = "SELECT c FROM ConditMilieu c WHERE c.milieu like ?1"),
      @NamedQuery(name = "ConditMilieu.findByExcludedId",
         query = "SELECT c FROM ConditMilieu c " + "WHERE c.conditMilieuId != ?1"),
      @NamedQuery(name = "ConditMilieu.findByOrder",
         query = "SELECT c FROM ConditMilieu c " + "WHERE c.plateforme = ?1 ORDER BY c.milieu")})
public class ConditMilieu implements Serializable, TKThesaurusObject
{

   private static final long serialVersionUID = -1948372046053283715L;

   private Integer conditMilieuId;
   private String milieu;
   private Plateforme plateforme;

   private Set<Prelevement> prelevements = new HashSet<>();

   /** Constructeur par défaut. */
   public ConditMilieu(){}

   @Override
   public String toString(){
      if(this.milieu != null){
         return "{" + this.milieu + "}";
      }else{
         return "{Empty ConditMilieu}";
      }
   }

   @Id
   @Column(name = "CONDIT_MILIEU_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getConditMilieuId(){
      return this.conditMilieuId;
   }

   public void setConditMilieuId(final Integer id){
      this.conditMilieuId = id;
   }

   @Column(name = "MILIEU", nullable = false, length = 200)
   public String getMilieu(){
      return this.milieu;
   }

   public void setMilieu(final String mil){
      this.milieu = mil;
   }

   @OneToMany(mappedBy = "conditMilieu")
   public Set<Prelevement> getPrelevements(){
      return this.prelevements;
   }

   public void setPrelevements(final Set<Prelevement> prelevs){
      this.prelevements = prelevs;
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
    * 2 objets sont considérés comme égaux s'ils ont le même milieu.
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
      final ConditMilieu test = (ConditMilieu) obj;
      return ((this.milieu == test.milieu || (this.milieu != null && this.milieu.equals(test.milieu)))
         && (this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme))));
   }

   /**
    * Le hashCode est calculé sur l'attribut milieu.
    * @return la valeur du hashCode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashMilieu = 0;
      int hashPF = 0;

      if(this.milieu != null){
         hashMilieu = this.milieu.hashCode();
      }
      if(this.plateforme != null){
         hashPF = this.plateforme.hashCode();
      }

      hash = 31 * hash + hashMilieu;
      hash = 31 * hash + hashPF;

      return hash;

   }

   @Override
   @Transient
   public String getNom(){
      return getMilieu();
   }

   @Override
   @Transient
   public Integer getId(){
      return getConditMilieuId();
   }
}
