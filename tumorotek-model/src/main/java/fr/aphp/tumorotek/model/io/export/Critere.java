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
 * Objet persistant mappant la table CRITERE.
 * Classe créée le 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
@Entity
@Table(name = "CRITERE")
public class Critere
{

   private Integer critereId;
   private Champ champ;
   private Combinaison combinaison;
   private String operateur;
   private String valeur;

   public Critere(){
      super();
   }

   public Critere(final Champ ch, final String op, final String val){
      super();
      this.champ = ch;
      this.operateur = op;
      this.valeur = val;
   }

   public Critere(final Combinaison comb, final String op, final String val){
      super();
      this.combinaison = comb;
      this.operateur = op;
      this.valeur = val;
   }

   @Id
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   @Column(name = "CRITERE_ID", unique = true, nullable = false)
   public Integer getCritereId(){
      return critereId;
   }

   public void setCritereId(final Integer critId){
      this.critereId = critId;
   }

   @OneToOne
   @JoinColumn(name = "CHAMP_ID")
   public Champ getChamp(){
      return champ;
   }

   public void setChamp(final Champ ch){
      this.champ = ch;
   }

   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "COMBINAISON_ID")
   public Combinaison getCombinaison(){
      return combinaison;
   }

   public void setCombinaison(final Combinaison comb){
      this.combinaison = comb;
   }

   @Column(name = "OPERATEUR", nullable = false)
   public String getOperateur(){
      return operateur;
   }

   public void setOperateur(final String op){
      this.operateur = op;
   }

   @Column(name = "VALEUR", nullable = false)
   public String getValeur(){
      return valeur;
   }

   public void setValeur(final String val){
      this.valeur = val;
   }

   /**
    * 2 criteres sont considérés comme égaux si ils ont le même operateur,
    * le même valeur,le même champ et la même operation.
    * @param obj est le critere à tester.
    * @return true si les criteres sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Critere test = (Critere) obj;
      if(this.operateur == null){
         if(test.operateur == null){
            if(this.valeur == null){
               if(test.valeur == null){
                  if(this.champ == null){
                     if(test.champ == null){
                        if(this.combinaison == null){
                           return (test.combinaison == null);
                        }
                        return (this.combinaison.equals(test.combinaison));
                     }
                     return false;
                  }else if(this.champ.equals(test.champ)){
                     if(this.combinaison == null){
                        return (test.combinaison == null);
                     }
                     return (this.combinaison.equals(test.combinaison));
                  }else{
                     return false;
                  }
               }
               return false;
            }else if(this.valeur.equals(test.valeur)){
               if(this.champ == null){
                  if(test.champ == null){
                     if(this.combinaison == null){
                        return (test.combinaison == null);
                     }
                     return (this.combinaison.equals(test.combinaison));
                  }
                  return false;
               }else if(this.champ.equals(test.champ)){
                  if(this.combinaison == null){
                     return (test.combinaison == null);
                  }
                  return (this.combinaison.equals(test.combinaison));
               }else{
                  return false;
               }
            }else{
               return false;
            }
         }
         return false;
      }else if(this.operateur.equals(test.operateur)){
         if(this.valeur == null){
            if(test.valeur == null){
               if(this.champ == null){
                  if(test.champ == null){
                     if(this.combinaison == null){
                        return (test.combinaison == null);
                     }
                     return (this.combinaison.equals(test.combinaison));
                  }
                  return false;
               }else if(this.champ.equals(test.champ)){
                  if(this.combinaison == null){
                     return (test.combinaison == null);
                  }
                  return (this.combinaison.equals(test.combinaison));
               }else{
                  return false;
               }
            }
            return false;
         }else if(this.valeur.equals(test.valeur)){
            if(this.champ == null){
               if(test.champ == null){
                  if(this.combinaison == null){
                     return (test.combinaison == null);
                  }
                  return (this.combinaison.equals(test.combinaison));
               }
               return false;
            }else if(this.champ.equals(test.champ)){
               if(this.combinaison == null){
                  return (test.combinaison == null);
               }
               return (this.combinaison.equals(test.combinaison));
            }else{
               return false;
            }
         }else{
            return false;
         }
      }else{
         return false;
      }
   }

   /**
    * Le hashcode est calculé sur les attributs operateur,
    * valeur, champ et combinaison.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashOperateur = 0;
      int hashValeur = 0;
      int hashChamp = 0;
      int hashCombinaison = 0;

      if(this.valeur != null){
         hashValeur = this.valeur.hashCode();
      }
      if(this.champ != null){
         hashChamp = this.champ.hashCode();
      }
      if(this.combinaison != null){
         hashCombinaison = this.combinaison.hashCode();
      }
      if(this.operateur != null){
         hashOperateur = this.operateur.hashCode();
      }

      hash = 31 * hash + hashValeur;
      hash = 31 * hash + hashChamp;
      hash = 31 * hash + hashCombinaison;
      hash = 31 * hash + hashOperateur;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.champ != null && this.operateur != null && this.valeur != null){
         return this.champ.toString() + " " + this.operateur + " " + this.valeur;
      }else if(this.combinaison != null && this.operateur != null && this.valeur != null){
         return this.combinaison.toString() + " " + this.operateur + " " + this.valeur;
      }else{
         return "{Empty Critere}";
      }
   }

}
