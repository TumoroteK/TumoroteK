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
package fr.aphp.tumorotek.param;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.SystemPropertyUtils;

/**
 * Classe utilitaire permettant la lecture des properties du fichier de configuration tumorotek.properties
 * @author GCH
 *
 */
public final class TumorotekProperties
{

   private static final Log log = LogFactory.getLog(TumorotekProperties.class);

   public static final String TUMO_PROPERTIES_FILENAME = "tumorotek.properties";
   public static final String TUMO_PROPERTIES_DIR = System.getProperty("catalina.base") + "/conf/Catalina/localhost/";
   public static final String TUMO_PROPERTIES_PATH = TUMO_PROPERTIES_DIR + TUMO_PROPERTIES_FILENAME;

   private static final Properties TUMOROTEK_PROPERTIES = new Properties();
   private static final String PLACEHOLDER_START_DELIMITER = "${";
   private static final String PLACEHOLDER_END_DELIMITER = "}";

   static{
      if(System.getProperty("catalina.base") != null){
         try{
            final EncodedResource resource =
               new EncodedResource(new FileSystemResource(TUMO_PROPERTIES_PATH), StandardCharsets.UTF_8);
            PropertiesLoaderUtils.fillProperties(TUMOROTEK_PROPERTIES, resource);
         }catch(final IOException e){
            log.error("Erreur à l'initialisation", e);
         }
      }
   }

   /**
    * Constructeur privé
    */
   private TumorotekProperties(){}

   /**
    * Renvoie la valeur de la clé spécifiée dans le fichier tumorotek.properties (null si la clé n'est pas trouvée)
    * @param key
    * @return
    */
   protected static String getValue(final String key){

      String res = null;

      if(TUMOROTEK_PROPERTIES.containsKey(key)){
         final String value = SystemPropertyUtils.resolvePlaceholders(TUMOROTEK_PROPERTIES.getProperty(key), true);
         final PropertyPlaceholderHelper pph =
            new PropertyPlaceholderHelper(PLACEHOLDER_START_DELIMITER, PLACEHOLDER_END_DELIMITER, "#", false);
         res = pph.replacePlaceholders(value, TUMOROTEK_PROPERTIES);
      }

      return res;
   }

   /**
    * Recherche une clé dans le fichier tumorotek.properties
    * @param key clé recherchée
    * @return true si la clé existe dans le fichier tumorotek.properties, false sinon
    */
   public static boolean containsKey(final String key){
      return TUMOROTEK_PROPERTIES.containsKey(key);
   }

   /**
    * Recharge le fichier tumorotek.properties
    * @throws IOException
    */
   public static void reload() throws IOException{
      PropertiesLoaderUtils.fillProperties(TUMOROTEK_PROPERTIES,
         new EncodedResource(new FileSystemResource(TUMO_PROPERTIES_PATH), StandardCharsets.UTF_8));
   }

}
