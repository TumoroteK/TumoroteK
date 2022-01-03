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

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * Objet persistant mappant la table OPERATION.
 * Enregistrement des operations realises par un utilisateur
 * sur un objet pour par souci de tracabilite.
 *
 * Date: 17/09/2009
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.13.1
 *
 */
@Entity
@Table(name = "OPERATION")
//@NamedQueries(value = {
//   @NamedQuery(name = "Operation.findByObjetIdAndEntite",
//      query = "SELECT o FROM Operation o WHERE o.objetId = ?1 AND" + " o.entite = ?2 ORDER BY o.date, o.operationId"),
//   @NamedQuery(name = "Operation.findByObjetIdAndEntiteForHistorique",
//      query = "SELECT o FROM Operation o WHERE o.objetId = ?1 AND" + " o.entite = ?2 AND o.operationType.nom != 'Login' "
//         + "AND o.operationType.nom != 'Logout' " + "ORDER BY o.date, o.operationId"),
//   @NamedQuery(name = "Operation.findByUtilisateur", query = "SELECT o FROM Operation o WHERE o.utilisateur = ?1"),
//   @NamedQuery(name = "Operation.findByExcludedId", query = "SELECT o FROM Operation o " + "WHERE o.operationId != ?1"),
//   @NamedQuery(name = "Operation.findByDate", query = "SELECT o FROM Operation o " + "WHERE o.date = ?1"),
//   @NamedQuery(name = "Operation.findByAfterDate", query = "SELECT o FROM Operation o " + "WHERE o.date >= ?1 order by o.date"),
//   @NamedQuery(name = "Operation.findByBeforeDate", query = "SELECT o FROM Operation o " + "WHERE o.date <= ?1 order by o.date"),
//   @NamedQuery(name = "Operation.findByBetweenDates",
//      query = "SELECT o FROM Operation o " + "WHERE o.date >= ?1 AND o.date <= ?2 order by o.date"),
//   @NamedQuery(name = "Operation.findByObjetIdEntiteAndOperationType",
//      query = "SELECT o FROM Operation o WHERE o.objetId = ?1 AND" + " o.entite = ?2 AND o.operationType = ?3")})
public class Operation implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer operationId;
   private Calendar date;
   private Integer objetId;
   private OperationType operationType;
   private Entite entite;
   private Utilisateur utilisateur;
   private Boolean v1 = false;

   // since 2.0.13.1
   private String identificationDossier;

   /** Constructeur par défaut. */
   public Operation(){}

   @Override
   public String toString(){
      if(this.operationType != null && this.entite != null && this.objetId != null){
         return "{" + this.operationType.getNom() + ", " + this.entite.getNom() + ", " + this.objetId + "}";
      }
      return "{Empty Operation}";
   }

   @Id
   @Column(name = "OPERATION_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "native", parameters = {@Parameter(name = "sequence", value = "opSeq")})
   public Integer getOperationId(){
      return this.operationId;
   }

   public void setOperationId(final Integer id){
      this.operationId = id;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DATE_", nullable = false)
   public Calendar getDate(){
      if(date != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(date.getTime());
         return cal;
      }
      return null;
   }

   public void setDate(final Calendar d){
      if(d != null){
         this.date = Calendar.getInstance();
         this.date.setTime(d.getTime());
      }else{
         this.date = null;
      }
   }

   @Column(name = "OBJET_ID", nullable = false)
   public Integer getObjetId(){
      return this.objetId;
   }

   public void setObjetId(final Integer objId){
      this.objetId = objId;
   }

   @ManyToOne
   @JoinColumn(name = "OPERATION_TYPE_ID", nullable = false)
   public OperationType getOperationType(){
      return this.operationType;
   }

   public void setOperationType(final OperationType opeType){
      this.operationType = opeType;
   }

   @ManyToOne
   @JoinColumn(name = "ENTITE_ID", nullable = false)
   public Entite getEntite(){
      return this.entite;
   }

   public void setEntite(final Entite en){
      this.entite = en;
   }

   @ManyToOne
   @JoinColumn(name = "UTILISATEUR_ID", nullable = true)
   public Utilisateur getUtilisateur(){
      return this.utilisateur;
   }

   public void setUtilisateur(final Utilisateur u){
      this.utilisateur = u;
   }

   public void setV1(final Boolean v){
      this.v1 = v;
   }

   @Column(name = "V1", nullable = false)
   public Boolean getV1(){
      return v1;
   }

   @Transient
   public String getIdentificationDossier(){
      return identificationDossier;
   }

   public void setIdentificationDossier(final String _i){
      this.identificationDossier = _i;
   }

   /**
    * 2 operations sont considerees egales s'ils sont de meme type
    * et si elles sont effectue par le meme utilisateur 
    * sur le meme objet a la meme date.
    * @param obj est l'operation à tester.
    * @return true si les operations sont egales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final Operation test = (Operation) obj;
      return ((this.objetId == test.objetId || (this.objetId != null && this.objetId.equals(test.objetId)))
         && (this.utilisateur == test.utilisateur || (this.utilisateur != null && this.utilisateur.equals(test.utilisateur)))
         && (this.operationType == test.operationType
            || (this.operationType != null && this.operationType.equals(test.operationType)))
         && (this.entite == test.entite || (this.entite != null && this.entite.equals(test.entite)))
         && (this.date == test.date || (this.date != null && this.date.equals(test.date))));
   }

   /**
    * Le hashcode est calculé sur l'attribut nom et la
    * description.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashObjeId = 0;
      int hashUtilisateur = 0;
      int hashOperationType = 0;
      int hashEntite = 0;
      int hashDate = 0;

      if(this.objetId != null){
         hashObjeId = this.objetId.hashCode();
      }
      if(this.utilisateur != null){
         hashUtilisateur = this.utilisateur.hashCode();
      }
      if(this.operationType != null){
         hashOperationType = this.operationType.hashCode();
      }
      if(this.entite != null){
         hashEntite = this.entite.hashCode();
      }
      if(this.date != null){
         hashDate = this.date.hashCode();
      }

      hash = 31 * hash + hashObjeId;
      hash = 31 * hash + hashUtilisateur;
      hash = 31 * hash + hashOperationType;
      hash = 31 * hash + hashEntite;
      hash = 31 * hash + hashDate;

      return hash;

   }
}
