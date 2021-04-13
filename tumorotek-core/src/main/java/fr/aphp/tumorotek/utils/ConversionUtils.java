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
package fr.aphp.tumorotek.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.model.systeme.Fichier;

/**
 * Classe utilitaire permettant la conversion d'instances Object en un type défini
 * @author Gille Chapelot
 *
 */
public abstract class ConversionUtils
{

   /** Formats de date connus */
   private static final Map<String, String> FORMATS_DATE_CONNUS;

   static{
      FORMATS_DATE_CONNUS = new HashMap<>();
      //Formats de date FR
      FORMATS_DATE_CONNUS.put("dd/MM/yyyy HH:mm:ss", "^\\d{2}\\/\\d{2}\\/\\d{4} \\d{2}:\\d{2}:\\d{2}$");
      FORMATS_DATE_CONNUS.put("dd/MM/yyyy HH:mm", "^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}$");
      FORMATS_DATE_CONNUS.put("dd/MM/yyyy", "^\\d{2}/\\d{2}/\\d{4}$");
      //Formats de date EN
      FORMATS_DATE_CONNUS.put("yyyy/MM/dd HH:mm:ss", "^\\d{4}\\/\\d{2}\\/\\d{2} \\d{2}:\\d{2}:\\d{2}$");
      FORMATS_DATE_CONNUS.put("yyyy/MM/dd HH:mm", "^\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}$");
      FORMATS_DATE_CONNUS.put("yyyy/MM/dd", "^\\d{4}/\\d{2}/\\d{2}$");
   }

   /**
    * Constructeur privé
    */
   private ConversionUtils(){}

   /**
    * Convertit un objet dans le type passé en paramètre
    * @param o objet à convertir
    * @param clazz type cible
    * @return
    */
   @SuppressWarnings("unchecked")
   public static <T> T convert(final Object o, final Class<T> clazz){

      T res = null;

      if(clazz.equals(Integer.class)){
         res = (T) convertToInteger(o);
      }else if(clazz.equals(Float.class)){
         res = (T) convertToFloat(o);
      }else if(clazz.equals(Double.class)){
         res = (T) convertToDouble(o);
      }else if(clazz.equals(Boolean.class)){
         res = (T) convertToBoolean(o);
      }else if(clazz.equals(Calendar.class)){
         res = (T) convertToCalendar(o);
      }else if(clazz.equals(Date.class)){
         res = (T) convertToDate(o);
      }else{
         throw new TKException("Conversion non gérée");
      }

      return res;

   }

   /**
    * Convertit un Object en {@link java.lang.Float Float}
    * @param objet
    * @throws TKException si l'objet reçu en entrée ne peut pas être converti en {@link java.lang.Float Float}
    * @return
    */
   public static Float convertToFloat(final Object objet){

      Float res = null;

      if(objet instanceof Float){
         res = (Float) objet;
      }else if(objet instanceof Integer){
         res = ((Integer) objet).floatValue();
      }else if(objet instanceof Double){
         res = ((Double) objet).floatValue();
      }else if(objet instanceof String){
         try{
            res = Float.parseFloat((String) objet);
         }catch(final NumberFormatException nfe){
            throw new TKException("Valeur numérique attendue (valeur obtenue = [" + objet + "])");
         }
      }

      if(res == null){
         throw new TKException("Valeur numérique décimale invalide");
      }

      return res;

   }

   /**
    * Convertit un Object en {@link java.lang.Double Double}
    * @param objet
    * @throws TKException si l'objet reçu en entrée ne peut pas être converti en {@link java.lang.Double Double}
    * @return
    */
   public static Double convertToDouble(final Object objet){

      Double res = null;

      if(objet instanceof Double){
         res = (Double) objet;
      }else if(objet instanceof Integer){
         res = ((Integer) objet).doubleValue();
      }else if(objet instanceof Float){
         res = ((Float) objet).doubleValue();
      }else if(objet instanceof String){
         try{
            res = Double.parseDouble((String) objet);
         }catch(final NumberFormatException nfe){
            throw new TKException("Valeur numérique attendue (valeur obtenue = [" + objet + "])");
         }
      }

      if(res == null){
         throw new TKException("Valeur numérique décimale invalide");
      }

      return res;

   }

   /**
    * Convertit un Object en {@link java.lang.Integer Integer}
    * @param objet
    * @throws TKException si l'objet reçu en entrée ne peut pas être converti en {@link java.lang.Integer Integer}
    * @return
    */
   public static Integer convertToInteger(final Object objet){

      Integer res = null;

      if(objet instanceof Integer){
         res = (Integer) objet;
      }else if(objet instanceof Float){
         res = ((Float) objet).intValue();
      }else if(objet instanceof Double){
         res = ((Double) objet).intValue();
      }else if(objet instanceof String){
         try{
            res = Integer.parseInt((String) objet);
         }catch(final NumberFormatException nfe){
            throw new TKException("Valeur numérique attendue (valeur obtenue = [" + objet + "])");
         }
      }

      if(res == null){
         throw new TKException("Valeur numérique entière invalide");
      }

      return res;

   }

   /**
    * Convertit un Object en {@link java.lang.Boolean Boolean}
    * @param objet
    * @return
    */
   public static Boolean convertToBoolean(final Object objet){

      Boolean res = null;

      if(objet instanceof Boolean){
         res = (Boolean) objet;
      }else if(objet instanceof Integer){
         res = (Integer) objet == 1;
      }else if(objet instanceof String){
         res = Boolean.valueOf((String) objet);
      }

      if(res == null){
         throw new TKException("Conversion " + objet.getClass() + " en Boolean");
      }

      return res;

   }

   /**
    * Convertit un Object en {@link java.util.Date Date}
    * @param objet
    * @throws TKException si l'objet reçu en entrée ne peut pas être converti en {@link java.util.Date Date}
    * @return
    */
   public static Date convertToDate(final Object objet){

      Date res = null;

      if(objet instanceof Date){
         res = (Date) objet;
      }else if(objet instanceof Calendar){
         res = ((Calendar) objet).getTime();
      }else if(objet instanceof String){

         final DateFormat df = new SimpleDateFormat(getDateFormat((String) objet));

         try{
            res = df.parse((String) objet);
         }catch(final ParseException pe){
            throw new TKException(objet + " n'est pas une date valide");
         }

      }

      if(res == null){
         throw new TKException("Date invalide");
      }

      return res;

   }

   /**
    * Convertit un Object en {@link java.util.Calendar Calendar}
    * @param objet
    * @throws TKException si l'objet reçu en entrée ne peut pas être converti en {@link java.util.Calendar Calendar}
    * @return
    */
   public static Calendar convertToCalendar(final Object objet){

      Calendar res = null;

      if(objet instanceof Date){
         res = Calendar.getInstance();
         res.setTime((Date) objet);
      }else if(objet instanceof Calendar){
         res = (Calendar) objet;
      }else if(objet instanceof String){

         final DateFormat df = new SimpleDateFormat(getDateFormat((String) objet));
         res = Calendar.getInstance();

         try{
            res.setTime(df.parse((String) objet));
         }catch(final ParseException pe){
            throw new TKException(objet + " n'est pas une date valide");
         }

      }

      if(res == null){
         throw new TKException("Date invalide");
      }

      return res;

   }

   /**
    * Retourne le format de date utilisé dans le {@link java.util.String String} passé en paramètre
    * @param dateAsString date sous forme de {@link java.util.String String}
    * @throws TKException si le format de date utilisé dans le String reçu en entré est inconnu
    * @return
    */
   public static String getDateFormat(final String dateAsString){

      String format = null;

      for(final Entry<String, String> entry : FORMATS_DATE_CONNUS.entrySet()){

         if(dateAsString.matches(entry.getValue())){
            format = entry.getKey();
            break;
         }

      }

      if(format == null){
         throw new TKException("Format de la date [" + dateAsString + "] inconnu");
      }

      return format;

   }

   public static String formatToStringValue(Object obj) {

      String value = null;

      if(obj instanceof String){
         value = (String)obj;
      }
      else if(obj instanceof Number){
         value = String.valueOf(obj);
      }
      else if(obj instanceof Boolean){
         if((Boolean) obj){
            value = "Oui";
         }else{
            value = "Non";
         }
      }
      else if(obj instanceof Date){
         final Date date = (Date) obj;
         final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
         value = sdf.format(date);
      }
      else if(obj instanceof Calendar){
         final Calendar tmp = (Calendar) obj;
         SimpleDateFormat sdf = null;
         if(tmp.get(Calendar.HOUR_OF_DAY) > 0 || tmp.get(Calendar.MINUTE) > 0 || tmp.get(Calendar.SECOND) > 0){
            sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
         }else{
            sdf = new SimpleDateFormat("dd/MM/yyyy");
         }
         value = sdf.format(tmp.getTime());
      }
      else if(obj instanceof Fichier){
         value = ((Fichier) obj).getNom();
      }

      return value;

   }

}
