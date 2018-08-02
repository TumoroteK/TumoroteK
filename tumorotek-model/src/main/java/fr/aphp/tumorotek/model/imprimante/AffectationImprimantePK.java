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
package fr.aphp.tumorotek.model.imprimante;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Embedded Id pour la table AFFECTATION_IMPRIMANTE.
 * Classe créée le 21/03/11.
 *
 * @author Pierre VENTADOUR
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 * hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.0
 *
 */
@Embeddable
public class AffectationImprimantePK implements Serializable
{

   private static final long serialVersionUID = 2865113820064733177L;

   private Utilisateur utilisateur;
   private Banque banque;
   private Imprimante imprimante;

   public AffectationImprimantePK(){

   }

   public AffectationImprimantePK(final Utilisateur u, final Banque b, final Imprimante i){
      this.utilisateur = u;
      this.banque = b;
      this.imprimante = i;
   }

   @ManyToOne(targetEntity = Utilisateur.class)
   public Utilisateur getUtilisateur(){
      return utilisateur;
   }

   public void setUtilisateur(final Utilisateur u){
      this.utilisateur = u;
   }

   @ManyToOne(targetEntity = Banque.class)
   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }

   @ManyToOne(targetEntity = Imprimante.class)
   public Imprimante getImprimante(){
      return imprimante;
   }

   public void setImprimante(final Imprimante i){
      this.imprimante = i;
   }

   /**
    * 2 PKs sont considérés comme égales si elles sont composees 
    * des mêmes clés.
    * @param obj est la PK à tester.
    * @return true si les PK sont egales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final AffectationImprimantePK test = (AffectationImprimantePK) obj;
      return ((this.utilisateur == test.utilisateur || (this.utilisateur != null && this.utilisateur.equals(test.utilisateur)))
         && (this.banque == test.banque || (this.banque != null && this.banque.equals(test.banque)))
         && (this.imprimante == test.imprimante || (this.imprimante != null && this.imprimante.equals(test.imprimante))));
   }

   /**
    * Le hashcode est calculé sur les clés.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashUtilisateur = 0;
      int hashBanque = 0;
      int hashImprimante = 0;
      final int hashModele = 0;

      if(this.utilisateur != null){
         hashUtilisateur = this.utilisateur.hashCode();
      }
      if(this.banque != null){
         hashBanque = this.banque.hashCode();
      }
      if(this.imprimante != null){
         hashImprimante = this.imprimante.hashCode();
      }

      hash = 7 * hash + hashUtilisateur;
      hash = 7 * hash + hashBanque;
      hash = 7 * hash + hashImprimante;
      hash = 7 * hash + hashModele;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.imprimante != null && this.utilisateur != null && this.banque != null){
         return "{" + utilisateur.toString() + " (Utilisateur), " + banque.toString() + " (Banque), " + imprimante.toString()
            + " (Imprimante)}";
      }
      return "{Empty AffectationImprimantePK}";
   }
}
