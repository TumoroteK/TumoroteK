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
package fr.aphp.tumorotek.model.interfacage;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table BLOC_EXTERNE.
 * Classe créée le 05/10/11.
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
@Entity
@Table(name = "BLOC_EXTERNE")
@NamedQueries(value = {
   @NamedQuery(name = "BlocExterne.findByDossierExterne",
      query = "SELECT b FROM BlocExterne b " + "WHERE b.dossierExterne = ?1 " + "ORDER BY b.ordre"),
   @NamedQuery(name = "BlocExterne.findByDossierExterneAndEntite",
      query = "SELECT b FROM BlocExterne b " + "WHERE b.dossierExterne = ?1 " + "AND b.entiteId = ?2 " + "ORDER BY b.ordre")})
public class BlocExterne implements java.io.Serializable
{

   private static final long serialVersionUID = 1035198779653396102L;

   private Integer blocExterneId;
   private Integer entiteId;
   private Integer ordre;
   private DossierExterne dossierExterne;

   // Transient 
   // @since 2.1 Camel JDBC sgl view 
   private final List<ValeurExterne> valeurs = new ArrayList<>();

   public BlocExterne(){
      super();
   }

   @Id
   @Column(name = "BLOC_EXTERNE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getBlocExterneId(){
      return blocExterneId;
   }

   public void setBlocExterneId(final Integer b){
      this.blocExterneId = b;
   }

   @Column(name = "ENTITE_ID", nullable = false)
   public Integer getEntiteId(){
      return entiteId;
   }

   public void setEntiteId(final Integer e){
      this.entiteId = e;
   }

   @Column(name = "ORDRE", nullable = false)
   public Integer getOrdre(){
      return ordre;
   }

   public void setOrdre(final Integer o){
      this.ordre = o;
   }

   @ManyToOne()
   @JoinColumn(name = "DOSSIER_EXTERNE_ID", nullable = false)
   public DossierExterne getDossierExterne(){
      return dossierExterne;
   }

   public void setDossierExterne(final DossierExterne d){
      this.dossierExterne = d;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final BlocExterne test = (BlocExterne) obj;
      return ((this.dossierExterne == test.dossierExterne
         || (this.dossierExterne != null && this.dossierExterne.equals(test.dossierExterne)))
         && (this.entiteId == test.entiteId || (this.entiteId != null && this.entiteId.equals(test.entiteId)))
         && (this.ordre == test.ordre || (this.ordre != null && this.ordre.equals(test.ordre))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashDoss = 0;
      int hashEnt = 0;
      int hashOrd = 0;

      if(this.dossierExterne != null){
         hashDoss = this.dossierExterne.hashCode();
      }
      if(this.entiteId != null){
         hashEnt = this.entiteId.hashCode();
      }
      if(this.ordre != null){
         hashOrd = this.ordre.hashCode();
      }

      hash = 7 * hash + hashDoss;
      hash = 7 * hash + hashEnt;
      hash = 7 * hash + hashOrd;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.entiteId != null && this.ordre != null){
         return "{" + this.ordre + ", " + this.entiteId + "(Entite) " + dossierExterne.getIdentificationDossier()
            + "(DossierExterne)}";
      }
      return "{Empty BlocExterne}";
   }

   @Transient
   public List<ValeurExterne> getValeurs(){
      return valeurs;
   }
}
