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
package fr.aphp.tumorotek.model.io.export;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table COMBINAISON.
 * Classe créée le 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
@Entity
@Table(name = "COMBINAISON")
public class Combinaison
{

   private Integer combinaisonId;
   private String operateur;
   private Champ champ1;
   private Champ champ2;

   public Combinaison(){
      super();
   }

   public Combinaison(final Champ ch1, final String op, final Champ ch2){
      super();
      this.operateur = op;
      this.champ1 = ch1;
      this.champ2 = ch2;
   }

   @Id
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   @Column(name = "COMBINAISON_ID", unique = true, nullable = false)
   public Integer getCombinaisonId(){
      return combinaisonId;
   }

   public void setCombinaisonId(final Integer combId){
      this.combinaisonId = combId;
   }

   @OneToOne(cascade = {CascadeType.ALL})
   @JoinColumn(name = "CHAMP1_ID")
   public Champ getChamp1(){
      return champ1;
   }

   public void setChamp1(final Champ ch1){
      this.champ1 = ch1;
   }

   @OneToOne(cascade = {CascadeType.ALL})
   @JoinColumn(name = "CHAMP2_ID")
   public Champ getChamp2(){
      return champ2;
   }

   public void setChamp2(final Champ ch2){
      this.champ2 = ch2;
   }

   @Column(name = "OPERATEUR", nullable = false)
   public String getOperateur(){
      return operateur;
   }

   public void setOperateur(final String op){
      this.operateur = op;
   }

   /**
    * 2 combinaisons sont considérées comme égales
    * si elles ont le même operateur, le même champ 1 et le même champ 2.
    * @param obj est la combinaison à tester.
    * @return true si les combinaisons sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Combinaison test = (Combinaison) obj;
      if(this.operateur == null){
         if(test.operateur == null){
            if(this.champ1 == null){
               if(test.champ1 == null){
                  if(this.champ2 == null){
                     return (test.champ2 == null);
                  }else{
                     return (this.champ2.equals(test.champ2));
                  }
               }else{
                  return false;
               }
            }else if(this.champ1.equals(test.champ1)){
               if(test.champ2 == null){
                  return (this.champ2 == null);
               }else{
                  return (this.champ2.equals(test.champ2));
               }
            }else{
               return false;
            }
         }else{
            return false;
         }
      }else if(this.operateur.equals(test.operateur)){
         if(this.champ1 == null){
            if(test.champ1 == null){
               if(this.champ2 == null){
                  return (test.champ2 == null);
               }else{
                  return (this.champ2.equals(test.champ2));
               }
            }else{
               return false;
            }
         }else if(this.champ1.equals(test.champ1)){
            if(test.champ2 == null){
               return (this.champ2 == null);
            }else{
               return (this.champ2.equals(test.champ2));
            }
         }else{
            return false;
         }
      }else{
         return false;
      }
   }

   /**
    * Le hashcode est calculé sur les attributs operateur, champ1 et champ2.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashChamp1 = 0;
      int hashChamp2 = 0;
      int hashOperateur = 0;

      if(this.champ1 != null){
         hashChamp1 = this.champ1.hashCode();
      }
      if(this.champ2 != null){
         hashChamp2 = this.champ2.hashCode();
      }
      if(this.operateur != null){
         hashOperateur = this.operateur.hashCode();
      }

      hash = 31 * hash + hashChamp1;
      hash = 31 * hash + hashChamp2;
      hash = 31 * hash + hashOperateur;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(champ1 != null && champ2 != null && operateur != null){
         return champ1.toString() + " " + operateur.toString() + " " + champ2.toString();
      }else if(this.combinaisonId != null){
         return "{" + this.combinaisonId + "}";
      }else{
         return "{Empty Combinaison}";
      }
   }
}
