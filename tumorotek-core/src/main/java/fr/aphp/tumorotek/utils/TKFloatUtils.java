/**
 * Copyright ou © ou Copr. SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * méthode pour corriger un problème de calcul des quantités à partir d'un volume et d'une concentration incorrect (mauvais arrondi) 
 * (bug TK-903)
 * @since 2.3.1.7
 * @author chuet
 */
public class TKFloatUtils
{
   /**
    * ajoute float1 à float2
    * @param float1
    * @param float2
    * @return
    */
   public static Float add(Float float1, Float float2) {
      if(float1 == null || float2 == null) {
         return null;
      }
      
      BigDecimal float1AsBigDecimal = transformInBigDecimal(float1);
      BigDecimal float2AsBigDecimal = transformInBigDecimal(float2);

      return float1AsBigDecimal.add(float2AsBigDecimal).floatValue();
   }

   /**
    * soustrait float2 de float1
    * @param float1
    * @param float2
    * @return
    */
   public static Float subtract(Float float1, Float float2) {
      if(float1 == null || float2 == null) {
         return null;
      }
      
      BigDecimal float1AsBigDecimal = transformInBigDecimal(float1);
      BigDecimal float2AsBigDecimal = transformInBigDecimal(float2);

      return float1AsBigDecimal.subtract(float2AsBigDecimal).floatValue();
   } 
   
   /**
    * multiplie float1 par float2 en arrondissant à 3 chiffres après la virgule
    * @param float1
    * @param float2
    * @return
    */
   public static Float multiply(Float float1, Float float2) {
      if(float1 == null || float2 == null) {
         return null;
      }
      
      BigDecimal float1AsBigDecimal = transformInBigDecimal(float1);
      BigDecimal float2AsBigDecimal = transformInBigDecimal(float2);
      
      return float1AsBigDecimal.multiply(float2AsBigDecimal).setScale(3, RoundingMode.HALF_UP).floatValue();
   }
   
   /**
    * divise float1 par float2 en arrondissant à 3 chiffres après la virgule
    * @param float1
    * @param float2
    * @return
    */
   public static Float divide(Float float1, Float float2) {
      if(float1 == null || float2 == null) {
         return null;
      }
      
      BigDecimal float1AsBigDecimal = transformInBigDecimal(float1);
      BigDecimal float2AsBigDecimal = transformInBigDecimal(float2);

      return float1AsBigDecimal.divide(float2AsBigDecimal, 3, RoundingMode.HALF_UP).floatValue();
   }
   
   /**
    * renvoie le BigDecimal associé au float passé en paramètre
    * @param param
    * @return
    */
   public static BigDecimal transformInBigDecimal(Float param) {
      if(param == null) {
         return null;
      }
      
      return  new BigDecimal(String.valueOf(param));
   }
}

