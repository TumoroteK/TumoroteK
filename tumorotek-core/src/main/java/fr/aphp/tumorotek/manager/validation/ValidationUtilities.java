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
package fr.aphp.tumorotek.manager.validation;

import java.util.Calendar;
import java.util.Date;

import org.springframework.validation.Errors;

/**
 * Classe finale contenant les outils associes à la validation.
 *  - les expressions regulieres
 *  -
 *
 *  Date : 13/10/2009
 *
 * @author mathieu
 * @version 2.0
 *
 */
public final class ValidationUtilities
{
   //	public static final String MOTREGEXP = 
   //			"[a-z_A-Z0-9'öÖôÔàâéèêôùûçîïÀÂÉÈÔÙÛÇ\\s\\.+\\-:();?]+";
   public static final String MOTREGEXP = "[\\p{L}|\\p{P}|\\p{N}|\\p{Z}|\\p{M}|\\+\\-@/<=>$%°~]+";

   public static final String MOTNOPUNCTREGEXP = "[\\p{L}|\\p{N}|\\p{Z}|\\p{M}|\\+\\-@/<=>$%°~]+";

   public static final String CODEREGEXP = "[a-zA-Z0-9_\\s\\.\\-\\+\\/]+";

   public static final String ONLYSPACESREGEXP = "\\s*";

   public static final String SEXEREGEXP = "F|M|(Ind)";

   public static final String PATIENT_ETAT_REGEXP = "V|D|(Inconnu)";

   public static final String COMBINAISON_OPERATEUR_REGEXP = "\\+|\\-";

   public static final String GROUPEMENT_OPERATEUR_REGEXP = "(and)|(or)";

   public static final String DATATYPE_REGEXP = "(alphanum)|(text)|(date)" + "|(bool)|(item)";

   public static final String MAILREGEXP = "[a-zA-Z0-9_.+\\-@]+";

   public static final String HTTPREGEXP = "(https?:\\/\\/){0,1}[a-zA-Z0-9_\\s\\.\\-\\/\\?=\\+&%]+";

   public static final String PATHREGEXP = "[:a-zA-Z0-9_\\.\\-\\/\\s]+";

   public static final String FILENAMEREGEXP = "[a-zA-Z0-9_\\.\\-]+";

   public static final String PASSWORDREGEXP = "^.*(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[\\W]).*$";

   public static final String IPREGEXP =
      "^\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b$";

   private ValidationUtilities(){}

   /**
    * Medthode généraliste de qui compare deux dates assignées aux objets au 
    * coeur de TK. Cette méthode tient compte du fait que les dates peuvent 
    * être de type Date ou Calendar et doivent pouvoir se comparer 
    * malgré leur type. Si l'objetBindingError est null alors execute la 
    * comparaison des dates est renvoie false si la comparaison ne renvoie
    * pas le résultat attendu.
    * @param date Date ou Calendar à comparer
    * @param champ champ correspondant a la date (pour Errors) 
    * @param dateRef Date ou Calendar de référence
    * @param prefixe initiant le message d'erreur
    * @param mid partie interm du message d'erreur localisé
    * @param suffixe pour compléter le message d'erreur
    * @param errs
    * @param before 
    * @return false si check -> error
    */
   public static boolean checkWithDate(final Object date, final String champ, final Object dateRef, final String prefixe,
      final String mid, final String suffixe, final Errors errs, final boolean before){

      Date date1 = new Date();
      Date date2 = new Date();

      // prepare les dates a la comparaison.
      // si deux calendar dont un a heure-min-ss non renseignée alors on 
      // met tout a O.
      if(date instanceof Calendar && dateRef instanceof Calendar){
         if(((Calendar) date).get(Calendar.HOUR) == 0 && ((Calendar) date).get(Calendar.HOUR_OF_DAY) == 0
            && ((Calendar) date).get(Calendar.MINUTE) == 0 && ((Calendar) date).get(Calendar.SECOND) == 0){
            if(((Calendar) dateRef).get(Calendar.HOUR) != 0 || ((Calendar) dateRef).get(Calendar.HOUR_OF_DAY) != 0
               || ((Calendar) dateRef).get(Calendar.MINUTE) != 0 || ((Calendar) dateRef).get(Calendar.SECOND) != 0){
               ((Calendar) dateRef).set(Calendar.HOUR, 0);
               ((Calendar) dateRef).set(Calendar.HOUR_OF_DAY, 0);
               ((Calendar) dateRef).set(Calendar.MINUTE, 0);
               ((Calendar) dateRef).set(Calendar.SECOND, 0);
            }
         }else if(((Calendar) dateRef).get(Calendar.HOUR) == 0 && ((Calendar) dateRef).get(Calendar.HOUR_OF_DAY) == 0
            && ((Calendar) dateRef).get(Calendar.MINUTE) == 0 && ((Calendar) dateRef).get(Calendar.SECOND) == 0){
            ((Calendar) date).set(Calendar.HOUR, 0);
            ((Calendar) date).set(Calendar.HOUR_OF_DAY, 0);
            ((Calendar) date).set(Calendar.MINUTE, 0);
            ((Calendar) date).set(Calendar.SECOND, 0);
         }
      }

      if(date instanceof Calendar){
         // passe les hh:mm:ss a null pour la comparaison
         if(before && dateRef instanceof Date){
            ((Calendar) date).set(Calendar.HOUR, 0);
            ((Calendar) date).set(Calendar.HOUR_OF_DAY, 0);
            ((Calendar) date).set(Calendar.MINUTE, 0);
            ((Calendar) date).set(Calendar.SECOND, 0);
         }
         date1 = ((Calendar) date).getTime();
      }else{
         date1 = (Date) date;
      }
      if(dateRef instanceof Calendar){
         // passe les hh:mm:ss a null pour la comparaison
         if(!before && date instanceof Date){
            ((Calendar) dateRef).set(Calendar.HOUR, 0);
            ((Calendar) dateRef).set(Calendar.HOUR_OF_DAY, 0);
            ((Calendar) dateRef).set(Calendar.MINUTE, 0);
            ((Calendar) dateRef).set(Calendar.SECOND, 0);
         }
         date2 = ((Calendar) dateRef).getTime();
      }else{
         date2 = (Date) dateRef;
      }

      if(!date1.equals(date2)){
         if(before){
            if(!date1.before(date2)){
               if(errs != null){
                  errs.rejectValue(champ, prefixe + "." + mid + ".sup" + suffixe);
               }else{
                  return false;
               }
            }
         }else{
            if(!date1.after(date2)){
               if(errs != null){
                  errs.rejectValue(champ, prefixe + "." + mid + ".inf" + suffixe);
               }else{
                  return false;
               }
            }
         }
      }
      return true;
   }
}
