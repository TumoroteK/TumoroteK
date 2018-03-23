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
 * Objet persistant mappant la table DESTRUCTION_MOTIF.
 * Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "DESTRUCTION_MOTIF")
@NamedQueries(
   value = {@NamedQuery(name = "DestructionMotif.findByMotif", query = "SELECT d FROM DestructionMotif d WHERE d.motif like ?1"),
      @NamedQuery(name = "DestructionMotif.findByExcludedId",
         query = "SELECT d FROM DestructionMotif d " + "WHERE d.destructionMotifId != ?1"),
      @NamedQuery(name = "DestructionMotif.findByOrder",
         query = "SELECT d FROM DestructionMotif d " + "WHERE d.plateforme = ?1 ORDER BY d.motif")})
public class DestructionMotif implements Serializable, TKThesaurusObject
{

   private static final long serialVersionUID = -3784391207102019937L;

   private Integer destructionMotifId;
   private String motif;
   private Plateforme plateforme;

   private Set<Cession> cessions = new HashSet<>();

   /** Constructeur par défaut. */
   public DestructionMotif(){}

   @Id
   @Column(name = "DESTRUCTION_MOTIF_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getDestructionMotifId(){
      return this.destructionMotifId;
   }

   public void setDestructionMotifId(final Integer id){
      this.destructionMotifId = id;
   }

   @Column(name = "MOTIF", nullable = false, length = 200)
   public String getMotif(){
      return this.motif;
   }

   public void setMotif(final String m){
      this.motif = m;
   }

   @OneToMany(mappedBy = "destructionMotif")
   public Set<Cession> getCessions(){
      return this.cessions;
   }

   public void setCessions(final Set<Cession> cess){
      this.cessions = cess;
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
    * 2 destructions sont considérées comme égales si elles ont le même motif 
    * et la même plateforme.
    * @param obj est la destruction à tester.
    * @return true si les destructions sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final DestructionMotif test = (DestructionMotif) obj;
      return ((this.motif == test.motif || (this.motif != null && this.motif.equals(test.motif)))
         && (this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme))));
   }

   /**
    * Le hashcode est calculé sur les attributs motif et plateforme.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      int hash = 7;
      int hashMotif = 0;
      int hashPF = 0;

      if(this.motif != null){
         hashMotif = this.motif.hashCode();
      }
      if(this.plateforme != null){
         hashPF = this.plateforme.hashCode();
      }

      hash = 31 * hash + hashMotif;
      hash = 31 * hash + hashPF;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.motif != null){
         return "{" + this.motif + "}";
      }else{
         return "{Empty DestructionMotif}";
      }
   }

   @Override
   @Transient
   public String getNom(){
      return getMotif();
   }

   @Override
   @Transient
   public Integer getId(){
      return getDestructionMotifId();
   }
}
