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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.AbstractPfDependantThesaurusObject;

/**
 *
 * Objet persistant mappant la table RISQUE.
 * Classe créée le 17/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "RISQUE")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "RISQUE_ID"))})
@GenericGenerator(name = "autoincrement", strategy = "increment")
@NamedQueries(value = {@NamedQuery(name = "Risque.findByNom", query = "SELECT r FROM Risque r WHERE r.nom like ?1"),
   @NamedQuery(name = "Risque.findByInfectieux", query = "SELECT r FROM Risque r WHERE r.infectieux = ?1"),
   @NamedQuery(name = "Risque.findByPrelevementId",
      query = "SELECT r FROM Risque r " + "left join r.prelevements p " + "WHERE p.prelevementId = ?1"),
   @NamedQuery(name = "Risque.findByExcludedId", query = "SELECT r FROM Risque r " + "WHERE r.id != ?1"),
   @NamedQuery(name = "Risque.findByPfOrder", query = "SELECT r FROM Risque r " + "WHERE r.plateforme = ?1 ORDER BY r.nom"),
   @NamedQuery(name = "Risque.findByOrder", query = "SELECT r FROM Risque r ORDER BY r.nom")})
public class Risque extends AbstractPfDependantThesaurusObject implements Serializable
{

   private static final long serialVersionUID = 5495762619216759L;

   private Boolean infectieux;
   private Set<Prelevement> prelevements = new HashSet<>();

   /** Constructeur par défaut. */
   public Risque(){}

   /**
    * @deprecated Utiliser {@link #getId()}
    * @return
    */
   @Deprecated
   @Transient
   public Integer getRisqueId(){
      return this.getId();
   }

   /**
    * @deprecated Utiliser {@link #setId(Integer)}
    * @param mId
    */
   @Deprecated
   public void setRisqueId(final Integer id){
      this.setId(id);
   }

   @Column(name = "INFECTIEUX", nullable = false)
   public Boolean getInfectieux(){
      return this.infectieux;
   }

   public void setInfectieux(final Boolean inf){
      this.infectieux = inf;
   }

   @ManyToMany(targetEntity = Prelevement.class)
   @JoinTable(name = "PRELEVEMENT_RISQUE", joinColumns = @JoinColumn(name = "RISQUE_ID"),
      inverseJoinColumns = @JoinColumn(name = "PRELEVEMENT_ID"))
   public Set<Prelevement> getPrelevements(){
      return this.prelevements;
   }

   public void setPrelevements(final Set<Prelevement> prelevs){
      this.prelevements = prelevs;
   }

   @Override
   public String toString(){
      if(this.getNom() != null){
         return "{" + this.getNom() + "}";
      }else{
         return "{Empty Risque}";
      }
   }

}
