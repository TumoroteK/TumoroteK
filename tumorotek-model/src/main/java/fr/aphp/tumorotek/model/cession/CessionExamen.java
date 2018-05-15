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
 * Objet persistant mappant la table CESSION_EXAMEN.
 * Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "CESSION_EXAMEN")
@NamedQueries(
   value = {@NamedQuery(name = "CessionExamen.findByExamen", query = "SELECT c FROM CessionExamen c WHERE c.examen like ?1"),
      @NamedQuery(name = "CessionExamen.findByExamenEn", query = "SELECT c FROM CessionExamen c " + "WHERE c.examenEn like ?1"),
      @NamedQuery(name = "CessionExamen.findByExcludedId",
         query = "SELECT c FROM CessionExamen c " + "WHERE c.cessionExamenId != ?1"),
      @NamedQuery(name = "CessionExamen.findByOrder",
         query = "SELECT c FROM CessionExamen c " + "WHERE c.plateforme = ?1 ORDER BY c.examen")})
public class CessionExamen implements Serializable, TKThesaurusObject
{

   private static final long serialVersionUID = -6437415927205983957L;

   private Integer cessionExamenId;
   private String examen;
   private String examenEn;
   private Plateforme plateforme;

   private Set<Cession> cessions = new HashSet<>();

   public CessionExamen(){}

   @Id
   @Column(name = "CESSION_EXAMEN_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getCessionExamenId(){
      return this.cessionExamenId;
   }

   public void setCessionExamenId(final Integer id){
      this.cessionExamenId = id;
   }

   @Column(name = "EXAMEN", nullable = false, length = 200)
   public String getExamen(){
      return this.examen;
   }

   public void setExamen(final String ex){
      this.examen = ex;
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
    * 2 objets sont considérés comme égaux s'ils ont le même examen et 
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

      final CessionExamen test = (CessionExamen) obj;
      return ((this.examen == test.examen || (this.examen != null && this.examen.equals(test.examen)))
         && (this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme))));
   }

   /**
    * Le hashcode est calculé sur les attributs examen et plateforme.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      int hash = 7;
      int hashExamen = 0;
      int hashPF = 0;

      if(this.examen != null){
         hashExamen = this.examen.hashCode();
      }
      if(this.plateforme != null){
         hashPF = this.plateforme.hashCode();
      }

      hash = 31 * hash + hashExamen;
      hash = 31 * hash + hashPF;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.examen != null){
         return "{" + this.examen + "}";
      }
      return "{Empty CessionExamen}";
   }

   @Override
   @Transient
   public String getNom(){
      return getExamen();
   }

   @Override
   @Transient
   public Integer getId(){
      return getCessionExamenId();
   }
}
