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

package fr.aphp.tumorotek.action.outils.contexte.fusion;

public class LevenshteinDistance
{
   static int i = 0, j = 0;

   public static double similarityTwoParams(final String a1, final String a2, final String b1, final String b2){

      return (similarity(a1, a2) + similarity(b1, b2)) / 2;
   }

   public static double similarityThreeParams(final String a1, final String a2, final String b1, final String b2, final String c1,
      final String c2){

      return (similarity(a1, a2) + similarity(b1, b2) + similarity(c1, c2)) / 3;
   }

   public static double similarity(String s1, String s2){
      // si une des deux valeurs est vide = max similarity
      if(s1 == null || s2 == null){
         return 1.0;
      }else{
         if(s1.length() < s2.length()){ // s1 should always be bigger
            final String swap = s1;
            s1 = s2;
            s2 = swap;
         }

         final int bigLen = s1.length();

         if(bigLen == 0){
            return 1.0;
         }else{
            return (bigLen - computeEditDistance(s1, s2)) / (double) bigLen;
         }
      }

   }

   public static int computeEditDistance(String s1, String s2){

      //System.out.println("test passage algo computeEditDistance " + j);
      j++;

      s1 = s1.toLowerCase();
      s2 = s2.toLowerCase();

      final int[] costs = new int[s2.length() + 1];
      for(int i = 0; i <= s1.length(); i++){
         int lastValue = i;
         for(int j = 0; j <= s2.length(); j++){
            if(i == 0){
               costs[j] = j;
            }else{
               if(j > 0){
                  int newValue = costs[j - 1];
                  if(s1.charAt(i - 1) != s2.charAt(j - 1)){
                     newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                  }
                  costs[j - 1] = lastValue;
                  lastValue = newValue;
               }
            }
         }
         if(i > 0){
            costs[s2.length()] = lastValue;
         }
      }
      return costs[s2.length()];
   }

}
