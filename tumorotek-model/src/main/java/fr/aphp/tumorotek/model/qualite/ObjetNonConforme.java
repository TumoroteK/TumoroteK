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
package fr.aphp.tumorotek.model.qualite;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table OBJET_NON_CONFORME.
 * Classe créée le 08/11/11.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "OBJET_NON_CONFORME")
//@NamedQueries(value = {
//   @NamedQuery(name = "ObjetNonConforme" + ".findByObjetAndEntite",
//      query = "SELECT o FROM ObjetNonConforme o " + "WHERE o.objetId = ?1 " + "AND o.entite = ?2"),
//   @NamedQuery(name = "ObjetNonConforme.findByObjetEntiteAndType",
//      query = "SELECT o FROM ObjetNonConforme o " + "WHERE o.objetId = ?1 " + "AND o.entite = ?2 "
//         + "AND o.nonConformite.conformiteType = ?3 " + "ORDER BY o.nonConformite.nom"),
//   @NamedQuery(name = "ObjetNonConforme.findByNonConformite",
//      query = "SELECT o FROM ObjetNonConforme o " + "WHERE o.nonConformite = ?1"),
//   @NamedQuery(name = "ObjetNonConforme.findObjetIdsByNonConformites",
//      query = "SELECT distinct o.objetId FROM ObjetNonConforme o " + "LEFT JOIN o.nonConformite n WHERE n in (?1)")})
public class ObjetNonConforme implements java.io.Serializable
{

   private static final long serialVersionUID = 1284782619042960165L;

   private Integer objetNonConformeId;
   private NonConformite nonConformite;
   private Integer objetId;
   private Entite entite;

   public ObjetNonConforme(){
      super();
   }

   @Id
   @Column(name = "OBJET_NON_CONFORME_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   // @GenericGenerator(name = "autoincrement", strategy = "increment")
   @GenericGenerator(name = "autoincrement", strategy = "native",
      parameters = {@Parameter(name = "sequence", value = "objetNonConformeSeq")})
   public Integer getObjetNonConformeId(){
      return objetNonConformeId;
   }

   public void setObjetNonConformeId(final Integer id){
      this.objetNonConformeId = id;
   }

   @ManyToOne()
   @JoinColumn(name = "NON_CONFORMITE_ID", nullable = false)
   public NonConformite getNonConformite(){
      return nonConformite;
   }

   public void setNonConformite(final NonConformite n){
      this.nonConformite = n;
   }

   @Column(name = "OBJET_ID", nullable = false)
   public Integer getObjetId(){
      return objetId;
   }

   public void setObjetId(final Integer id){
      this.objetId = id;
   }

   @ManyToOne()
   @JoinColumn(name = "ENTITE_ID", nullable = false)
   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite e){
      this.entite = e;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ObjetNonConforme test = (ObjetNonConforme) obj;
      return ((this.nonConformite == test.nonConformite
         || (this.nonConformite != null && this.nonConformite.equals(test.nonConformite)))
         && (this.objetId == test.objetId || (this.objetId != null && this.objetId.equals(test.objetId)))
         && (this.entite == test.entite || (this.entite != null && this.entite.equals(test.entite))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashNonConformite = 0;
      int hashbjetId = 0;
      int hashEntite = 0;

      if(this.nonConformite != null){
         hashNonConformite = this.nonConformite.hashCode();
      }
      if(this.objetId != null){
         hashbjetId = this.objetId.hashCode();
      }
      if(this.entite != null){
         hashEntite = this.entite.hashCode();
      }

      hash = 7 * hash + hashNonConformite;
      hash = 8 * hash + hashbjetId;
      hash = 8 * hash + hashEntite;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.objetId != null && this.entite != null && this.nonConformite != null){
         return "{" + this.objetId + ", " + entite.getNom() + "(Entite), " + nonConformite.getNom() + "(NonConformite)}";
      }else{
         return "{Empty ObjetNonConforme}";
      }
   }

   @Override
   public ObjetNonConforme clone(){
      final ObjetNonConforme clone = new ObjetNonConforme();

      clone.setObjetNonConformeId(this.objetNonConformeId);
      clone.setNonConformite(this.nonConformite);
      clone.setObjetId(this.objetId);
      clone.setEntite(this.entite);

      return clone;
   }

}
