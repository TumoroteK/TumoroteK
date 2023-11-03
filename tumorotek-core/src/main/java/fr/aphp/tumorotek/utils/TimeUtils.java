package fr.aphp.tumorotek.utils;

import java.util.Calendar;

/**
 * Classe utilitaire pour gérer les opérations liées au temps.
 */
public class TimeUtils
{

   private TimeUtils(){
   }

   /**
    * Convertit une durée spécifiée en millisecondes en minutes.
    *
    * @param milliseconds La durée à convertir, spécifiée en millisecondes.
    * @return La durée équivalente en minutes. Si la durée en entrée est négative, cette méthode renvoie -1.
    */
      public static float convertMillisecondsToMinutes(long milliseconds) {
         if (milliseconds > 0) {
            return (float) milliseconds / (60 * 1000);
         }
         return -1;
      }

   /**
    * Vérifie si une date au format Calendar est valide.
    * Une date est valide si elle n'est pas nulle et que l'une des composantes suivantes n'est pas égale à zéro :
    * l'heure (HOUR_OF_DAY), les minutes (MINUTE) ou les secondes (SECOND).
    * Minuit (00:00:00) return false
    * @param date L'objet Calendar représentant la date à vérifier.
    * @return true si la date est valide, false sinon.
    */
   public static boolean isDateAndTimeValid(Calendar date) {
      return date != null && (date.get(Calendar.HOUR_OF_DAY) != 0 || date.get(Calendar.MINUTE) != 0 || date.get(Calendar.SECOND) != 0);
   }
}
