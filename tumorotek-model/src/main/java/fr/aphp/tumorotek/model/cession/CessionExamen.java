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
 * Objet persistant mappant la table CESSION_EXAMEN.
 * Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "CESSION_EXAMEN")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "CESSION_EXAMEN_ID")),
   @AttributeOverride(name = "nom", column = @Column(name = "EXAMEN", nullable = false, length = 200))})
@GenericGenerator(name = "autoincrement", strategy = "increment")
@NamedQueries(
   value = {@NamedQuery(name = "CessionExamen.findByExamen", query = "SELECT c FROM CessionExamen c WHERE c.nom like ?1"),
      @NamedQuery(name = "CessionExamen.findByExamenEn", query = "SELECT c FROM CessionExamen c " + "WHERE c.examenEn like ?1"),
      @NamedQuery(name = "CessionExamen.findByExcludedId", query = "SELECT c FROM CessionExamen c " + "WHERE c.id != ?1"),
      @NamedQuery(name = "CessionExamen.findByPfOrder",
         query = "SELECT c FROM CessionExamen c WHERE c.plateforme = ?1 ORDER BY c.nom"),
      @NamedQuery(name = "CessionExamen.findByOrder", query = "FROM CessionExamen c ORDER BY c.nom")})
public class CessionExamen extends AbstractPfDependantThesaurusObject implements Serializable
{

   private static final long serialVersionUID = -6437415927205983957L;

   private String examenEn;

   private Set<Cession> cessions = new HashSet<>();

   /**
    * Constructeur par défaut
    */
   public CessionExamen(){}

   /**
    * @deprecated Utiliser {@link #getId()}
    * @return
    */
   @Deprecated
   @Transient
   public Integer getCessionExamenId(){
      return this.getId();
   }

   /**
    * @deprecated Utiliser {@link #setId(Integer)}
    * @return
    */
   @Deprecated
   public void setCessionExamenId(final Integer id){
      this.setId(id);
   }

   /**
    * @deprecated Utiliser {@link #getNom()}
    * @return
    */
   @Deprecated
   @Transient
   public String getExamen(){
      return this.getNom();
   }

   /**
    * @deprecated Utiliser {@link #setNom(String)}
    * @param ex
    */
   @Deprecated
   public void setExamen(final String ex){
      this.setNom(ex);
   }

   @Column(name = "EXAMEN_EN", nullable = true, length = 50)
   public String getExamenEn(){
      return this.examenEn;
   }

   public void setExamenEn(final String exEn){
      this.examenEn = exEn;
   }

   @OneToMany(mappedBy = "cessionExamen")
   public Set<Cession> getCessions(){
      return this.cessions;
   }

   public void setCessions(final Set<Cession> cess){
      this.cessions = cess;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.getNom() != null){
         return "{" + this.getNom() + "}";
      }
      return "{Empty CessionExamen}";
   }

}
