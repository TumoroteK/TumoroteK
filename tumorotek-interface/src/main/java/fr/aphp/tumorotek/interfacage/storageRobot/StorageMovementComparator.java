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
package fr.aphp.tumorotek.interfacage.storageRobot;

import java.util.Comparator;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.1-IRELEC
 *
 */
public class StorageMovementComparator implements Comparator<StorageMovement>
{

   private boolean sortAdrl = true;

   public StorageMovementComparator(final boolean _b){
      this.sortAdrl = _b;
   }

   @Override
   public int compare(final StorageMovement s1, final StorageMovement s2){
      if(sortAdrl){
         return sortLocation(s1.getAdrl(), s2.getAdrl());
      }else{
         return sortLocation(s1.getDestAdrl(), s2.getDestAdrl());
      }
   }

   /**
    * La location peux être soit un entier (position boite tansfert), soit une adresse complète
    * à 4 niveaux congélateur IRELEC : C.R.B.POS.
    * @param l1
    * @param l2
    * @return resultat de la comparaison
    */
   private Integer sortLocation(final String l1, final String l2){

      if(l1 == null && l2 == null){
         return 0;
      }

      if(l1 == null){
         return 1;
      }

      if(l2 == null){
         return -1;
      }

      if(l1.matches("^[0-9]{1,2}$")){ // boite transfert
         return Integer.valueOf(l1).compareTo(Integer.valueOf(l2));
      }else if(l1.split("\\.").length == 4){
         final String[] splitted1 = l1.split("\\.");
         final String[] splitted2 = l2.split("\\.");
         // IRELEC check
         if(splitted1.length != 4 && splitted2.length != 4){
            throw new RuntimeException("storage.robot.emplacement.adrl.incompatible");
         }

         // conteneur
         int i = splitted1[0].compareTo(splitted2[0]);
         if(i != 0){
            return i;
         }

         // rack
         i = Integer.valueOf(splitted1[1]).compareTo(Integer.valueOf(splitted2[1]));
         if(i != 0){
            return i;
         }

         // boite
         i = Integer.valueOf(splitted1[2]).compareTo(Integer.valueOf(splitted2[2]));
         if(i != 0){
            return i;
         }

         // position
         return Integer.compare(Integer.valueOf(splitted1[3]), Integer.valueOf(splitted2[3]));
      }else{ // regular string comparaison par défaut
         return l1.compareTo(l2);
      }
   }
}
