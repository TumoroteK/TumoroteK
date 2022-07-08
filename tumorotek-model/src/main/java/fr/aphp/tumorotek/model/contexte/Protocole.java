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
package fr.aphp.tumorotek.model.contexte;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.AbstractPfDependantThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;

/**
 * Objet persistant mappant la table PROTOCOLE.
 * Contexte SeroTK.
 * Classe créée le 09/01/12.
 *
 * @author MAthieu BARTHELEMY
 * @version 2.0.6
 *
 */
@Entity
@Table(name = "PROTOCOLE")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "PROTOCOLE_ID"))})
@GenericGenerator(name = "autoincrement", strategy = "increment")
@NamedQueries(value = {
   @NamedQuery(name = "Protocole.findByNom", query = "SELECT p FROM Protocole p WHERE p.nom like ?1 " + "order by p.nom"),
   @NamedQuery(name = "Protocole.findByExcludedId", query = "SELECT p FROM Protocole p " + "WHERE p.id != ?1"),
   @NamedQuery(name = "Protocole.findByPfOrder", query = "SELECT p FROM Protocole p " + "WHERE p.plateforme = ?1 ORDER BY p.nom"),
   @NamedQuery(name = "Protocole.findByOrder", query = "SELECT p FROM Protocole p ORDER BY p.nom")})
public class Protocole extends AbstractPfDependantThesaurusObject implements Serializable
{

   private static final long serialVersionUID = 54864135646414L;

   private String description;

   private Set<Prelevement> prelevements = new HashSet<>();

   /** Constructeur par défaut. */
   public Protocole(){}

   /**
    * @deprecated Utiliser {@link #getId()}
    * @return
    */
   @Deprecated
   @Transient
   public Integer getProtocoleId(){
      return getId();
   }

   /**
    * @deprecated Utiliser {@link #setId(Integer)}
    * @param pId
    */
   @Deprecated
   public void setProtocoleId(final Integer pId){
      this.setId(pId);
   }

   @Column(name = "DESCRIPTION", nullable = true)
   public String getDescription(){
      return description;
   }

   public void setDescription(final String description){
      this.description = description;
   }

   @ManyToMany(mappedBy = "protocoles", targetEntity = PrelevementSero.class)
   public Set<Prelevement> getPrelevements(){
      return prelevements;
   }

   public void setPrelevements(final Set<Prelevement> prelevements){
      this.prelevements = prelevements;
   }

   @Override
   public String toString(){
      if(this.getNom() != null){
         return "{" + this.getNom() + "}";
      }
      return "{Empty Protocole}";
   }

}
