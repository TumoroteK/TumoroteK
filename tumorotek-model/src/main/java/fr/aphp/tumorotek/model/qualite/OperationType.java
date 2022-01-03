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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.utilisateur.DroitObjet;

/**
 * Objet persistant mappant la table OPERATION_TYPE.
 * Enregistrement systeme des types operations realisables
 * par un utilisateur par le biais de l'interface et
 * enregistrables a des fins de tracabilite. Certains types
 * sont sujets a des droits assignes par profil aux utilisateurs.
 *
 * Date: 17/09/2009
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Entity
@Table(name = "OPERATION_TYPE")
//@NamedQueries(
//   value = {@NamedQuery(name = "OperationType.findByNom", query = "SELECT o FROM OperationType o WHERE o.nom like ?1")})
public class OperationType implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer operationTypeId;
   private String nom;
   private Boolean profilable;
   private Set<DroitObjet> droitObjets = new HashSet<>();
   private Set<Operation> operations = new HashSet<>();

   // @since 2.0.13.1
   private Emetteur emetteur;

   public OperationType(){}

   public OperationType(final Integer operationTypeId, final String nom, final Boolean profilable){
      this.operationTypeId = operationTypeId;
      this.nom = nom;
      this.profilable = profilable;
   }

   @Override
   public String toString(){
      if(this.getNom() != null){
         return "{" + this.getNom() + "}";
      }else{
         return "{Empty OperationType}";
      }
   }

   @Id
   @Column(name = "OPERATION_TYPE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getOperationTypeId(){
      return this.operationTypeId;
   }

   public void setOperationTypeId(final Integer opeTypeId){
      this.operationTypeId = opeTypeId;
   }

   @Column(name = "NOM", nullable = false, length = 25)
   public String getNom(){
      return this.nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "PROFILABLE", nullable = false)
   public Boolean getProfilable(){
      return this.profilable;
   }

   public void setProfilable(final Boolean prof){
      this.profilable = prof;
   }

   @OneToMany(mappedBy = "pk.operationType")
   public Set<DroitObjet> getDroitObjets(){
      return this.droitObjets;
   }

   public void setDroitObjets(final Set<DroitObjet> droits){
      this.droitObjets = droits;
   }

   @OneToMany(mappedBy = "operationType")
   public Set<Operation> getOperations(){
      return this.operations;
   }

   public void setOperations(final Set<Operation> ops){
      this.operations = ops;
   }

   @Transient
   public Emetteur getEmetteur(){
      return this.emetteur;
   }

   public void setEmetteur(final Emetteur _e){
      this.emetteur = _e;
   }

   /**
    * 2 types operation sont consideres comme egaux si ils ont le même nom.
    * @param obj est le type à tester.
    * @return true si les types sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final OperationType test = (OperationType) obj;

      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom))));
   }

   /**
    * Le hashcode est calculé sur le nom.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashType = 0;

      if(this.nom != null){
         hashType = this.nom.hashCode();
      }

      hash = 31 * hash + hashType;

      return hash;

   }

}
