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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table VALEUR_EXTERNE.
 * Classe créée le 05/10/11.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "VALEUR_EXTERNE")
//@NamedQueries(value = {
//   @NamedQuery(name = "ValeurExterne.findByBlocExterne", query = "SELECT v FROM ValeurExterne v " + "WHERE v.blocExterne = ?1"),
//   @NamedQuery(name = "ValeurExterne.findByDossierChampEntiteIdAndBlocEntiteId", 
//   		query = "SELECT v FROM ValeurExterne v JOIN v.blocExterne b join b.dossierExterne d "
//   				+ "WHERE d = ?1  AND v.champEntiteId = ?2 AND b.entiteId = ?3") 
//})
public class ValeurExterne implements java.io.Serializable
{

   private static final long serialVersionUID = -3980792978462083437L;

   private Integer valeurExterneId;
   private String valeur;
   private Integer champEntiteId;
   private Integer champAnnotationId;
   private BlocExterne blocExterne;
   private byte[] contenu;

   public ValeurExterne(){
      super();
   }

   @Id
   @Column(name = "VALEUR_EXTERNE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getValeurExterneId(){
      return valeurExterneId;
   }

   public void setValeurExterneId(final Integer v){
      this.valeurExterneId = v;
   }

   @Column(name = "VALEUR", nullable = true, length = 250)
   public String getValeur(){
      return valeur;
   }

   public void setValeur(final String v){
      this.valeur = v;
   }

   @Column(name = "CHAMP_ENTITE_ID", nullable = true)
   public Integer getChampEntiteId(){
      return champEntiteId;
   }

   public void setChampEntiteId(final Integer c){
      this.champEntiteId = c;
   }

   @Column(name = "CHAMP_ANNOTATION_ID", nullable = true)
   public Integer getChampAnnotationId(){
      return champAnnotationId;
   }

   public void setChampAnnotationId(final Integer c){
      this.champAnnotationId = c;
   }

   @ManyToOne()
   @JoinColumn(name = "BLOC_EXTERNE_ID", nullable = false)
   public BlocExterne getBlocExterne(){
      return blocExterne;
   }

   public void setBlocExterne(final BlocExterne b){
      this.blocExterne = b;
   }

   @Lob
   @Basic(fetch = FetchType.LAZY)
   public byte[] getContenu(){
      return contenu;
   }

   public void setContenu(final byte[] contenu){
      this.contenu = contenu;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ValeurExterne test = (ValeurExterne) obj;
      return ((this.blocExterne == test.blocExterne || (this.blocExterne != null && this.blocExterne.equals(test.blocExterne)))
         && (this.champEntiteId == test.champEntiteId
            || (this.champEntiteId != null && this.champEntiteId.equals(test.champEntiteId)))
         && (this.champAnnotationId == test.champAnnotationId
            || (this.champAnnotationId != null && this.champAnnotationId.equals(test.champAnnotationId))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashBloc = 0;
      int hashChpE = 0;
      int hashChpA = 0;

      if(this.blocExterne != null){
         hashBloc = this.blocExterne.hashCode();
      }
      if(this.champEntiteId != null){
         hashChpE = this.champEntiteId.hashCode();
      }
      if(this.champAnnotationId != null){
         hashChpA = this.champAnnotationId.hashCode();
      }

      hash = 7 * hash + hashBloc;
      hash = 31 * hash + hashChpE;
      hash = 31 * hash + hashChpA;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.blocExterne != null){
         return "{" + this.champEntiteId + "(ChampEntite), " + this.champAnnotationId + "(ChampAnnotation), "
            + blocExterne.toString() + "(BlocExterne)}";
      }else{
         return "{Empty ValeurExterne}";
      }
   }

}
