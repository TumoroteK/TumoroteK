package fr.aphp.tumorotek.utils;

import java.util.Calendar;

public class TimeUtils
{
   /**
    * Convertit une durée spécifiée en millisecondes en minutes.
    *
    * @param milliseconds La durée à convertir, spécifiée en millisecondes.
    * @return La durée équivalente en minutes.
    * @throws IllegalArgumentException Si la durée est négative.
    */
      public static float convertMillisecondsToMinutes(long milliseconds) {
         if (milliseconds < 0) {
            throw new IllegalArgumentException("Duration cannot be negative.");
         }
         return (float) milliseconds / (60 * 1000);
      }

   /**
    * Vérifie si une date au format Calendar est valide.
    * Une date est valide si elle n'est pas nulle et que l'une des composantes suivantes n'est pas égale à zéro :
    * l'heure (HOUR_OF_DAY), les minutes (MINUTE) ou les secondes (SECOND).
    * Minuit (00:00:00) return false
    * @param date L'objet Calendar représentant la date à vérifier.
    * @return true si la date est valide, false sinon.
    */
   public static boolean isValidDate(Calendar date) {
      return date != null && (date.get(Calendar.HOUR_OF_DAY) != 0 || date.get(Calendar.MINUTE) != 0 || date.get(Calendar.SECOND) != 0);
   }
}
