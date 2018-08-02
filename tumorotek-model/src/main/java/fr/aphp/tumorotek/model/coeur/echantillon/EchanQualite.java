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

/**
 *
 * Objet persistant mappant la table ECHAN_QUALITE.
 * Classe créée le 10/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "ECHAN_QUALITE")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "ECHAN_QUALITE_ID")),
   @AttributeOverride(name = "nom", column = @Column(name = "ECHAN_QUALITE", nullable = false, length = 200))})
@GenericGenerator(name = "autoincrement", strategy = "increment")
@NamedQueries(
   value = {@NamedQuery(name = "EchanQualite.findByQualite", query = "SELECT e FROM EchanQualite e " + "WHERE e.nom like ?1"),
      @NamedQuery(name = "EchanQualite.findByEchantillonId",
         query = "SELECT e FROM EchanQualite e " + "left join e.echantillons h " + "WHERE h.echantillonId = ?1"),
      @NamedQuery(name = "EchanQualite.findByExcludedId", query = "SELECT e FROM EchanQualite e " + "WHERE e.id != ?1"),
      @NamedQuery(name = "EchanQualite.findByPfOrder",
         query = "SELECT e FROM EchanQualite e " + "WHERE e.plateforme = ?1 ORDER BY e.nom"),
      @NamedQuery(name = "EchanQualite.findByOrder",
      query = "SELECT e FROM EchanQualite e ORDER BY e.nom")})
public class EchanQualite extends AbstractPfDependantThesaurusObject implements java.io.Serializable
{

   private static final long serialVersionUID = 768431365534341L;

   private Set<Echantillon> echantillons;

   /** Constructeur par défaut. */
   public EchanQualite(){
      echantillons = new HashSet<>();
   }

   /**
    * Constructeur avec paramètres.
    * @param id .
    * @param q .
    */
   public EchanQualite(final Integer id, final String q){
      this.setId(id);
      this.setNom(q);
   }

   /**
    * @deprecated Utiliser {@link #getId()}
    * @return
    */
   @Deprecated
   @Transient
   public Integer getEchanQualiteId(){
      return this.getId();
   }

   /**
    * @deprecated Utiliser {@link #setId(Integer)}
    * @return
    */
   @Deprecated
   public void setEchanQualiteId(final Integer eId){
      this.setId(eId);
   }

   /**
    * @deprecated Utiliser {@link #getNom()}
    * @return
    */
   @Deprecated
   @Transient
   public String getEchanQualite(){
      return this.getNom();
   }

   /**
    * @deprecated Utiliser {@link #setNom(String)}
    * @param q
    */
   @Deprecated
   public void setEchanQualite(final String q){
      this.setNom(q);
   }

   @OneToMany(mappedBy = "echanQualite")
   public Set<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final Set<Echantillon> echants){
      this.echantillons = echants;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.getNom() != null){
         return "{" + this.getNom() + "}";
      }
      return "{Empty EchanQualite}";
   }

}
