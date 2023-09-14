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
package fr.aphp.tumorotek.model.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe utilitaire de type durée permettant le calcul de différence de dates et le calcul de nombre de secondes, minutes, heures, jours, mois...
 * <br><strong> Attention : </strong> Ne prends pas en compte les années bisextiles, les mois sont estimés à 30 jours et les années à 365 jours.
 * Classe créée le 20/02/2018
 *
 * @author Answald Bournique
 * @version 2.2.0
 * @since 2.2.0
 */
public class Duree
{

   public static Long MILLISECONDE = 1L;

   public static Long SECONDE = 1000 * MILLISECONDE;

   public static Long MINUTE = SECONDE * 60;

   public static Long HEURE = MINUTE * 60;

   public static Long JOUR = HEURE * 24;

   public static Long SEMAINE = JOUR * 7;

   public static Long MOIS = JOUR * 30;

   public static Long ANNEE = JOUR * 365;

   private Long millisecondes;

   /**
    * Initialise une durée selon l'unité
    * @param temps temps
    * @param unite unité utiliser (Duree.TYPE_SOUHAITE)
    */
   public Duree(final Long temps, final Long unite){
      this.millisecondes = temps * unite;
   }

   /**
    * Initialise une durée selon l'unité
    * @param temps temps
    * @param unite unité utiliser (Duree.TYPE_SOUHAITE)
    */
   public Duree(final Float temps, final Long unite){
      final Float res = temps * new Float(unite);
      this.millisecondes = res.longValue();
   }

   /**
    * Initialise une durée entre une date de début et une date de fin
    * @param dateStart
    * @param dateEnd
    */
   public Duree(final Date dateStart, final Date dateEnd){
      this.millisecondes = dateEnd.getTime() - dateStart.getTime();
   }

   /**
    * Initialise une durée entre une date de début et une date de fin
    * @param dateStart
    * @param dateEnd
    */
   public Duree(final Calendar dateStart, final Calendar dateEnd){
      this.millisecondes = dateEnd.getTimeInMillis() - dateStart.getTimeInMillis();
   }

   /**
    * Retourne la duree selon le type
    * @param unite utiliser les Duree.TYPE
    * @return le temps selon le type
    */
   public Long getTemps(final Long unite){
      return millisecondes / unite;
   }

   /**
    * Initialise la durée selon le type
    * @param temps temps
    * @param unite unité utiliser (Duree.TYPE_SOUHAITE)
    */
   public void setTemps(final Long temps, final Long unite){
      this.millisecondes = temps * unite;
   }

   /**
    * Ajoute du temps
    * @param temps temps
    * @param unite unité utiliser (Duree.TYPE_SOUHAITE)
    */
   public void addTemps(final Long temps, final Long unite){
      if(null != temps){
         this.millisecondes += temps * unite;
      }
   }
}
