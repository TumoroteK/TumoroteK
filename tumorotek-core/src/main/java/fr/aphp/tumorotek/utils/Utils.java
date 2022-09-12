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

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.param.TkParam;

/**
 * Utils class
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.2-diamic
 *
 */
public final class Utils
{

   private Utils(){}

   /**
    * Uilitaire formatant la date systeme courante.
    * @return Date courante
    */
   public static Date getCurrentSystemDate(){
      return Calendar.getInstance().getTime();
   }

   /**
    * Uilitaire formatant la date systeme courante.
    * @return Calendar courante
    */
   public static Calendar getCurrentSystemCalendar(){
      return Calendar.getInstance();
   }

   public static String getCurrentSystemTime(){
      final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

      return dateFormat.format(getCurrentSystemDate());
   }

   /**
    * Méthode générant un code lettre ("AA", "BC"...) en fonction d'un nombre
    * fourni en paramètre.
    * @return Liste de caractères dont le dernier correspond au code à générer
    * pour le nombre fourni.
    */
   public static List<String> createListChars(final int max, Integer prefix, final List<String> out){
      final String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
         "T", "U", "V", "W", "X", "Y", "Z"};
      if(prefix == null){
         int i = 0;
         prefix = 0;
         while(i < max && i < 26){
            out.add(alphabet[i]);
            i++;
         }
      }else{
         int i = 0;
         while(i < max && i < 26){
            out.add(alphabet[prefix] + alphabet[i]);
            i++;
         }
         prefix++;
      }
      if(max < 27){
         return out;
      }
      return createListChars(max - 26, prefix, out);
   }

   /**
    * Ecris le filesystem path pour une banque et/ou le champ annotation.
    * @param bank Banque
    * @param chp ChampAnnotation
    * @param file TKAnnotableObject
    * @return path
    */
   //TK-298 : https://tumorotek.myjetbrains.com/youtrack/issue/TK-298
   public static String writeAnnoFilePath(String baseDir, final Banque bank, final ChampAnnotation chp, final Fichier file){
      if(baseDir == null || !new File(baseDir).exists()){
         throw new RuntimeException("error.filesystem.access");
      }
      if (!baseDir.endsWith(File.separator)) {
         baseDir = baseDir + "/";
      }
      String path = baseDir + "pt_" + bank.getPlateforme().getPlateformeId() + "/" + "coll_" + bank.getBanqueId();

      if(chp != null){
         path = path + "/" + "anno" + "/" + "chp_" + chp.getId() + "/";
      }
      //		
      //		if (obj != null) {
      //			path = path + "/" + obj.entiteNom() + "_";
      //			if (obj.listableObjectId() != null) {
      //				path = path + obj.listableObjectId();
      //			} else {
      //				path = path + 0;
      //			}
      //					
      //		}
      if(file != null && file.getNom() != null){
         path = path + file.getNom();
      }

      return path;
   }

   public static boolean deleteDirectory(final File path){
      if(path.exists()){
         final File[] files = path.listFiles();
         if(null != files){
            for(File file : files){
               if(file.isDirectory()){
                  deleteDirectory(file);
               }else{
                  file.delete();
               }
            }
         }
      }
      return (path.delete());
   }

   /**
    * Arrondi (vers le bas) d'un double avec n éléments après la virgule.
    * @param a La valeur à convertir.
    * @param n Le nombre de décimales à conserver.
    * @return La valeur arrondi à n décimales.
    */
   public static Float floor(final Float a, final int n){
      if(a != null){
         final double p = Math.pow(10.0, n);
         return (float) (Math.floor((a * p) + 0.5) / p);
      }
      return null;
   }

   /**
    * Cette méthode formate un string en fct d'une expression régulière
    * de la forme : >., <., ou [1,3]. 
    * @param value String à formater.
    * @param expReg Expression régulière.
    * @return String formaté.
    */
   public static String formateString(final String value, final String expReg){
      String resultat = "";
      // on vérifie que le paramétrage est ok
      if(value != null && !value.equals("")){
         // on vérifie que le paramétrage est ok
         if(expReg != null && !expReg.equals("")){
            // on garde ce qui se trouve avant un certain caractère
            if(expReg.contains("<")){
               final String car = expReg.substring(1);
               if(value.contains(car)){
                  resultat = value.substring(0, value.indexOf(car));
               }else{
                  resultat = "";
               }
            }else if(expReg.contains(">")){
               // on garde ce qui se trouve après un certain caractère
               final String car = expReg.substring(1);
               final int size = car.length();
               if(value.contains(car)){
                  resultat = value.substring(value.indexOf(car) + size);
               }else{
                  resultat = "";
               }
            }else if(expReg.contains("[")){
               // on conserve entre 2 indices de caractères
               int deb = Integer.parseInt(expReg.substring(1, expReg.indexOf(",")));
               final int fin = Integer.parseInt(expReg.substring(expReg.indexOf(",") + 1, expReg.indexOf("]")));
               if(deb > 0 && fin > 0 && deb < fin){
                  --deb;
                  if(value.length() >= deb){
                     if(value.length() >= fin){
                        resultat = value.substring(deb, fin);
                     }else{
                        resultat = value.substring(deb);
                     }
                  }else{
                     resultat = "";
                  }
               }else{
                  resultat = "";
               }
            }
         }else{
            resultat = value;
         }
      }

      return resultat;
   }

   // PF_1:R1,R2,R3;PF_2:R1,R2;
   public static Hashtable<Integer, List<Integer>> extractAssosPlateformesEmetteursRecepteurs(String value){
      final Hashtable<Integer, List<Integer>> associations = new Hashtable<>();

      if(value != null && !value.equals("")){
         value = value.replace(" ", "");
         String[] pfs = null;
         if(value.contains(":")){
            pfs = value.split(";");
         }else{
            pfs = new String[1];
            pfs[0] = value;
         }

         for(int i = 0; i < pfs.length; i++){
            if(pfs[i].contains(":")){
               final String pf = pfs[i].substring(0, pfs[i].indexOf(":"));
               final String tmp = pfs[i].substring(pfs[i].indexOf(":") + 1);
               String[] ems = null;
               if(tmp.contains(",")){
                  ems = tmp.split(",");
               }else{
                  ems = new String[1];
                  ems[0] = tmp;
               }

               final List<Integer> emsIds = new ArrayList<>();
               for(int j = 0; j < ems.length; j++){
                  emsIds.add(Integer.valueOf(ems[j]));
               }

               if(emsIds.size() > 0){
                  associations.put(Integer.valueOf(pf), emsIds);
               }
            }
         }
      }

      return associations;
   }

   /**
    * Formate un string en upper case.
    * @param value
    * @return
    */
   public static String stringToUpperCase(final String value){
      if(value != null){
         return value.toUpperCase();
      }
      return null;
   }

   /**
    * Formate un string en lower case.
    * @param value
    * @return
    */
   public static String stringToLowerCase(final String value){
      if(value != null){
         return value.toLowerCase();
      }
      return null;
   }

   /**
    * Remplace toutes les virgules d'un string par des points.
    * @param value
    * @return
    */
   public static String replaceCommaByDot(String value){
      if(value != null){
         while(value.contains(",")){
            value = value.replace(',', '.');
         }
         return value;
      }
      return null;
   }
   
   /**
    * Concatene strings
    * @param value
    * @return
    * @version 2.2.2-diamic
    */
   public static String concat(final String sep, final String ... vals){
      if (vals != null) {
    	  return Arrays.stream(vals).collect(Collectors.joining(sep));
      }
      return null;
   }

   /**
    * @see http://stackoverflow.com/questions/1910236/how-can-i-split-an-arraylist-into-several-lists
    * @param list
    * @param targetSize
    * @return
    */
   public static <T extends Object> List<List<T>> split(final List<T> list, final int targetSize){
      final List<List<T>> lists = new ArrayList<>();
      for(int i = 0; i < list.size(); i += targetSize){
         lists.add(list.subList(i, Math.min(i + targetSize, list.size())));
      }
      return lists;
   }

   /**
    * Obtient la ReadableProperty exploitable par introspection PropertyUtils 
    * pour un objet à partir de l'objet ChampEntite.
    * @param chpE
    * @return readable property
    */
   public static String getReadablePropertyFromChampEntite(final ChampEntite chpE){
      String readProp = null;
      if(chpE != null){
         readProp = chpE.getNom().replaceFirst(".", (chpE.getNom().charAt(0) + "").toLowerCase());
         if(readProp.endsWith("Id")){
            readProp = readProp.substring(0, readProp.length() - 2);
         }
      }
      return readProp;
   }

   public static String getDatabaseURL(){
      return TkParam.TK_DATABASE_URL.getValue();
   }

   /**
    * Accède au path spécifié dans le ficher de conf du server.
    * @return database url path
    */
   public static String getDriverClass(){
      return TkParam.TK_DATABASE_DRIVER.getValue();
   }

   public static Boolean isOracleDBMS(){
      return getDatabaseURL().contains("oracle");
   }

   /**
    * 
    * @return usernameDB
    */
   public static String getUsernameDB(){
      return TkParam.TK_DATABASE_USER.getValue();
   }

   /**
    * 
    * @return passwordDB
    */
   public static String getPasswordDB(){
      return TkParam.TK_DATABASE_PASSWORD.getValue();
   }

   /**
    * Récupère le chemin du fichier de configuration de TK
    * @return chemin du fichier de configuration de TK
    */
   public static String getTkTumoPropertiesPath(){
      return TkParam.CONF_DIR.getValue();
   }

}
