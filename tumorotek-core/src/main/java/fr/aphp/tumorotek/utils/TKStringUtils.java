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

import org.jsoup.Jsoup;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

/**
 * @author GCH
 *
 */
public final class TKStringUtils
{

   /**
    * Constructeur privé
    */
   private TKStringUtils(){}
   
   /**
    * Echappe, sécurise et nettoie une string HTML
    * @param html string html à traiter
    * @return
    */
   public static String cleanHtmlString(final String html){

      String res = html;
      
      //Etape 1 : Remplacer les < qui ne sont pas des débuts de balise par &lt;
      //Si on ne fait pas cette étape, tout ce qui se trouve entre un < et un éventuel >
      //situé hors d'un balise est considéré comme une balise inconnue et supprimé
      //Par ex. <p>Ce<ci est un t>est</p> rendrait <p>Ceest</p>
      int nbLtReplaced = 0;
      for(int i=0 ; i < html.length() ; i++) {

         if(html.charAt(i) == '<') {

            final int nextGtIndx = html.substring(i).indexOf('>');
            final String tagPotentiel = html.substring(i, i + nextGtIndx + 1);

            int tagNameBeginIndex = 1;
            if(!"</>".equals(tagPotentiel) && tagPotentiel.startsWith("</")) {
               tagNameBeginIndex = 2;
            }
            
            final int tagNameEndIndex;
            if(tagPotentiel.contains(" ")) {
               tagNameEndIndex = tagPotentiel.indexOf(' ');
            }else if(tagPotentiel.endsWith("/>")) {
               tagNameEndIndex = tagPotentiel.indexOf("/>");
            }else {
               tagNameEndIndex = tagPotentiel.indexOf('>');
            }
            
            final String tagName = tagPotentiel.substring(tagNameBeginIndex, tagNameEndIndex);
            
            if(!Tag.isKnownTag(tagName)) {
               final int posCharToReplace = nbLtReplaced*3 + i;
               res = res.substring(0, posCharToReplace) + "&lt;" + res.substring(posCharToReplace+1);
               nbLtReplaced++;
            }

         }

      }
      
      //Etape 2 : Nettoyer le html
      final Whitelist whitelist =
         Whitelist.basic().addAttributes("p", "style").addAttributes("span", "style").addAttributes("a", "style");

      res = Jsoup.clean(res, whitelist);

      return res;

   }
   
   /**
    * Supprime les placeholders délimités par les bornes spécifiées d'une chaîne de caractères
    * @param stringToClean chaîne de caractères à nettoyer
    * @param startDelimiter borne de début des placeholders
    * @param endDelimiter borne de fin des placeholders
    * @return
    */
   public static String cleanPlaceholders(final String stringToClean, final String startDelimiter, final String endDelimiter) {
      
      String cleanString = stringToClean;
      
      int placeholderBeginIdx = stringToClean.indexOf(startDelimiter);
      int placeholderEndIdx = stringToClean.indexOf(endDelimiter);

      //La valeur doit être taritée si elle contient la borne de début avant la borne de fin et que les 2 sont présentes
      boolean dirty = placeholderBeginIdx != -1 && placeholderEndIdx != -1 && placeholderBeginIdx < placeholderEndIdx;

      while(dirty){
         
         final String placeholder = cleanString.substring(placeholderBeginIdx + 2, placeholderEndIdx);
         
       //Si une valeur ne contenant pas de placeholder a été trouvée pour le placeholder, on le remplace, sinon, on le laisse tel quel
         cleanString = cleanString.replace(startDelimiter + placeholder + endDelimiter, placeholder);

         //Calcul de l'indice de fin du placeholder ou de la valeur substituée (qui sert de point de départ pour la recherche du paceholder suivant)
         placeholderEndIdx = placeholderBeginIdx + placeholder.length();

         //Recherche des indices de début et de fin du placeholder suivant
         final int nextPlaceholderBeginIdx = placeholderEndIdx + cleanString.substring(placeholderEndIdx).indexOf(startDelimiter);
         final int nextPlaceholderEndIdx = placeholderEndIdx + cleanString.substring(placeholderEndIdx).indexOf(endDelimiter);

         //Si un autre placeholder est trouvé, on repart dans la boucle, sinon, on sort
         if(nextPlaceholderBeginIdx != -1 && nextPlaceholderEndIdx > nextPlaceholderBeginIdx){
            placeholderBeginIdx = nextPlaceholderBeginIdx;
            placeholderEndIdx = nextPlaceholderEndIdx;
         }else{
            dirty = false;
         }
         
      }
      
      return cleanString;
      
   }
   
}
