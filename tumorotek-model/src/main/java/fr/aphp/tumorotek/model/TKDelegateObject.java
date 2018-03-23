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
package fr.aphp.tumorotek.model;

import fr.aphp.tumorotek.model.contexte.Contexte;

/**
 * Interface representant les Entite systeme qui sont
 * déléguées par les entites TK Annotables.
 * Les classes implementant cette interface sont utilisées pour
 * ajouter des champs associées à l'application d'un contexte
 * d'utilisation de TK. Ex: SeroTK.
 * Interface créée le 19/01/12.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public abstract class TKDelegateObject
{

   public abstract Object getDelegator();

   public abstract void setDelegator(Object obj);

   public abstract Contexte getContexte();

   public abstract void setContexte(Contexte con);

   /**
    * Renvoie true si le delegate est vide et doit être 
    * supprimé en passant la property delegate à null à l'objet 
    * déléguant.
    * @return true/false
    */
   public abstract boolean isEmpty();

   @Override
   public String toString(){
      if(getDelegator() != null){
         return getDelegator().toString() + "." + getContexte().getNom();
      }else{
         return "{Empty delegate}";
      }

   }

   /**
    * 2 objets sont considérés comme égaux s'ils ont le delegator.
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

      if(getDelegator() != null){
         return getDelegator().equals(((TKDelegateObject) obj).getDelegator());
      }else{
         return (((TKDelegateObject) obj).getDelegator() == null);
      }
   }

   /**
    * Le hashcode est calculé sur le delegator.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashDelegator = 0;

      if(getDelegator() != null){
         hashDelegator = getDelegator().hashCode();
      }

      hash = 31 * hash + hashDelegator;

      return hash;
   }

}
