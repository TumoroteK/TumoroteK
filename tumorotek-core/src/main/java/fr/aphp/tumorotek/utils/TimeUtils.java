package fr.aphp.tumorotek.utils;

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
}
