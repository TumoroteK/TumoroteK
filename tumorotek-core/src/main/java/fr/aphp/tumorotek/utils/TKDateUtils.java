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

import java.util.Calendar;

/**
 * Classe utilitaire pour gérer les opérations liées au temps.
 */
public class TKDateUtils
{

   public static final long UNDEFINED_VALUE_IN_MILLISECONDS = -1;
   public static final float UNDEFINED_VALUE_IN_MINUTES = -1;
   
   private TKDateUtils(){
   }

   /**
    * Convertit une durée spécifiée en millisecondes en minutes.
    *
    * @param milliseconds La durée à convertir, spécifiée en millisecondes.
    * @return La durée équivalente en minutes. Si la durée en entrée est négative, cette méthode renvoie -1.
    */
      public static float convertMillisecondsToMinutes(long milliseconds) {
         if (milliseconds >= 0) {
            return (float) milliseconds / (60 * 1000);
         }
         return UNDEFINED_VALUE_IN_MINUTES;
      }

   /**
    * Indique si la date passée en paramètre est non null et correspond à une heure (et non à un jour) 
    * TK considère que l'heure minuit (00:00:00) correspond à une date pour un jour.
    * @param date L'objet Calendar représentant la date à vérifier.
    * @return true si la date est non null et l'heure associée est différente de 00:00:00.
    */
   public static boolean isDateNonNullWithHeureSignificative(Calendar date) {
      return date != null && (date.get(Calendar.HOUR_OF_DAY) != 0 || date.get(Calendar.MINUTE) != 0 || date.get(Calendar.SECOND) != 0);
   }
}
