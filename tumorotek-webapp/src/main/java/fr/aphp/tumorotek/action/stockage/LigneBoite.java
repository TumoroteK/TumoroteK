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
package fr.aphp.tumorotek.action.stockage;

public class LigneBoite
{

   private String nom;
   private Integer nbEmplacements;

   public LigneBoite(){

   }

   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   public Integer getNbEmplacements(){
      return nbEmplacements;
   }

   public void setNbEmplacements(final Integer nb){
      this.nbEmplacements = nb;
   }

   @Override
   public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result + ((nbEmplacements == null) ? 0 : nbEmplacements.hashCode());
      result = prime * result + ((nom == null) ? 0 : nom.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if(obj == null){
         return false;
      }
      if(getClass() != obj.getClass()){
         return false;
      }
      final LigneBoite other = (LigneBoite) obj;
      if(nbEmplacements == null){
         if(other.nbEmplacements != null){
            return false;
         }
      }else if(!nbEmplacements.equals(other.nbEmplacements)){
         return false;
      }
      if(nom == null){
         if(other.nom != null){
            return false;
         }
      }else if(!nom.equals(other.nom)){
         return false;
      }
      return true;
   }

}
