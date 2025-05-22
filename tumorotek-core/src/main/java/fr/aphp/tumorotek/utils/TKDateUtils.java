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
