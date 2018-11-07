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
package fr.aphp.tumorotek.webapp.init;

import static fr.aphp.tumorotek.param.TkParam.ACTIVE_DIRECTORY_DOMAIN;
import static fr.aphp.tumorotek.param.TkParam.ACTIVE_DIRECTORY_URL;
import static fr.aphp.tumorotek.param.TkParam.CAMEL_CONF_DIR;
import static fr.aphp.tumorotek.param.TkParam.CODES_DATABASE_DIALECT;
import static fr.aphp.tumorotek.param.TkParam.CODES_DATABASE_DRIVER;
import static fr.aphp.tumorotek.param.TkParam.CODES_DATABASE_PASSWORD;
import static fr.aphp.tumorotek.param.TkParam.CODES_DATABASE_URL;
import static fr.aphp.tumorotek.param.TkParam.CODES_DATABASE_USER;
import static fr.aphp.tumorotek.param.TkParam.CONF_DIR;
import static fr.aphp.tumorotek.param.TkParam.FILESYSTEM;
import static fr.aphp.tumorotek.param.TkParam.INTERFACAGES_DATABASE_DIALECT;
import static fr.aphp.tumorotek.param.TkParam.INTERFACAGES_DATABASE_DRIVER;
import static fr.aphp.tumorotek.param.TkParam.INTERFACAGES_DATABASE_PASSWORD;
import static fr.aphp.tumorotek.param.TkParam.INTERFACAGES_DATABASE_URL;
import static fr.aphp.tumorotek.param.TkParam.INTERFACAGES_DATABASE_USER;
import static fr.aphp.tumorotek.param.TkParam.LDAP_AUTHENTICATION;
import static fr.aphp.tumorotek.param.TkParam.LDAP_PASSWORD;
import static fr.aphp.tumorotek.param.TkParam.LDAP_URL;
import static fr.aphp.tumorotek.param.TkParam.LDAP_USER;
import static fr.aphp.tumorotek.param.TkParam.MBIO_CONF_DIR;
import static fr.aphp.tumorotek.param.TkParam.MSG_ACCUEIL;
import static fr.aphp.tumorotek.param.TkParam.PORTAL_PROPERTIES_PATH;
import static fr.aphp.tumorotek.param.TkParam.SGL_MAX_TABLE_SIZE;
import static fr.aphp.tumorotek.param.TkParam.SIP_CONF_DIR;
import static fr.aphp.tumorotek.param.TkParam.SIP_MAX_TABLE_SIZE;
import static fr.aphp.tumorotek.param.TkParam.TK_DATABASE_DIALECT;
import static fr.aphp.tumorotek.param.TkParam.TK_DATABASE_DRIVER;
import static fr.aphp.tumorotek.param.TkParam.TK_DATABASE_PASSWORD;
import static fr.aphp.tumorotek.param.TkParam.TK_DATABASE_URL;
import static fr.aphp.tumorotek.param.TkParam.TK_DATABASE_USER;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.param.TumorotekProperties;

/**
 * Classe permettant de passer les propriétés JNDI dans un fichier properties.
 * Permet la mise à jour de l'application en 2.2.0 sans opérations manuelles supplémentaire.
 * A supprimer quand le déploiement de la version 2.2.0 sera terminé sur tous les sites
 * @author GCH
 *
 */
public class TumorotekPropertiesInitializer
{

   private final Log log = LogFactory.getLog(TumorotekPropertiesInitializer.class);

   /**
    * Ajoute les propriétés nécessaires à tumorotek.properties si elles ne sont pas déjà présentes
    * @throws FileNotFoundException
    */
   public void init() throws IOException{

      if(!TumorotekProperties.containsKey(TkParam.CONF_DIR.getKey())){

         //Vérification de l'existence du fichier tumorotek.properties dans le répertoire de configuration
         final Path tumoPropertiesPath = Paths.get(TumorotekProperties.TUMO_PROPERTIES_PATH);
         if(!tumoPropertiesPath.toFile().exists()){
            throw new FileNotFoundException("Le fichier " + tumoPropertiesPath + " n'existe pas");
         }

         expandTumorotekProperties(tumoPropertiesPath);

         //Recharge en mémoire du fichier de properties mis à jour
         TumorotekProperties.reload();

      }

   }

   /**
    * Ecriture dans le fichier tumorotek.properties
    */
   private void expandTumorotekProperties(final Path tumoPropertiesPath){

      try{

         final Context ctx = new InitialContext();
         final StringBuffer sb = new StringBuffer();

         sb.append(System.lineSeparator());

         sb.append("#######################################" + System.lineSeparator())
            .append("#### VERSION 2.2.0                 ####" + System.lineSeparator())
            .append("#######################################" + System.lineSeparator()).append(System.lineSeparator())
            .append("#Message d'accueil de l'application" + System.lineSeparator())
            .append(MSG_ACCUEIL.getKey() + "=" + System.lineSeparator());

         sb.append(System.lineSeparator());
         sb.append(System.lineSeparator());

         final String tkFileSystem = ((String) ctx.lookup("java:comp/env/tk/tkFileSystem")).replaceAll("\\\\", "/");
         final String tkConfDir = ((String) ctx.lookup("java:comp/env/tk/tkTumoPropertiesSystem")).replaceAll("\\\\", "/");
         final String tkMbioSystem = ((String) ctx.lookup("java:comp/env/tk/tkMbioSystem")).replaceAll("\\\\", "/");
         final String tkSipSystem = ((String) ctx.lookup("java:comp/env/tk/tkSipSystem")).replaceAll("\\\\", "/");
         final String camelConfDir = ((String) ctx.lookup("java:comp/env/interfacage/conf/location")).replaceAll("\\\\", "/");
         final String portalPropertiesPath =
            ((String) ctx.lookup("java:comp/env/tk/tkTumoPropertiesSystem")).replaceAll("\\\\", "/");

         sb.append("#############################" + System.lineSeparator())
            .append("###     CONFIGURATION     ###" + System.lineSeparator())
            .append("#############################" + System.lineSeparator())
            .append(FILESYSTEM.getKey() + "=" + tkFileSystem + System.lineSeparator())
            .append(CONF_DIR.getKey() + "=" + tkConfDir + System.lineSeparator())
            .append(MBIO_CONF_DIR.getKey() + "=" + tkMbioSystem + System.lineSeparator())
            .append(SIP_CONF_DIR.getKey() + "=" + tkSipSystem + System.lineSeparator())
            .append(CAMEL_CONF_DIR.getKey() + "=" + camelConfDir + System.lineSeparator())
            .append(PORTAL_PROPERTIES_PATH.getKey() + "=" + portalPropertiesPath + "portal.properties" + System.lineSeparator());

         sb.append(System.lineSeparator());
         sb.append(System.lineSeparator());

         sb.append("#############################" + System.lineSeparator())
            .append("###     BASE TUMOROTEK    ###" + System.lineSeparator())
            .append("#############################" + System.lineSeparator())
            .append(TK_DATABASE_DIALECT.getKey() + "=" + ctx.lookup("java:comp/env/jdbc/dialect") + System.lineSeparator())
            .append(TK_DATABASE_DRIVER.getKey() + "=" + ctx.lookup("java:comp/env/jdbc/driverClass") + System.lineSeparator())
            .append(TK_DATABASE_URL.getKey() + "=" + ctx.lookup("java:comp/env/jdbc/url") + System.lineSeparator())
            .append(TK_DATABASE_USER.getKey() + "=" + ctx.lookup("java:comp/env/jdbc/user") + System.lineSeparator())
            .append(TK_DATABASE_PASSWORD.getKey() + "=" + ctx.lookup("java:comp/env/jdbc/password") + System.lineSeparator());

         sb.append(System.lineSeparator());
         sb.append(System.lineSeparator());

         sb.append("#############################" + System.lineSeparator())
            .append("###       BASE CODES      ###" + System.lineSeparator())
            .append("#############################" + System.lineSeparator())
            .append(
               CODES_DATABASE_DIALECT.getKey() + "=" + ctx.lookup("java:comp/env/codes/jdbc/dialect") + System.lineSeparator())
            .append(
               CODES_DATABASE_DRIVER.getKey() + "=" + ctx.lookup("java:comp/env/codes/jdbc/driverClass") + System.lineSeparator())
            .append(CODES_DATABASE_URL.getKey() + "=" + ctx.lookup("java:comp/env/codes/jdbc/url") + System.lineSeparator())
            .append(CODES_DATABASE_USER.getKey() + "=" + ctx.lookup("java:comp/env/codes/jdbc/user") + System.lineSeparator())
            .append(
               CODES_DATABASE_PASSWORD.getKey() + "=" + ctx.lookup("java:comp/env/codes/jdbc/password") + System.lineSeparator());

         sb.append(System.lineSeparator());
         sb.append(System.lineSeparator());

         sb.append("#############################" + System.lineSeparator())
            .append("###   BASE INTERFACAGES   ###" + System.lineSeparator())
            .append("#############################" + System.lineSeparator())
            .append(INTERFACAGES_DATABASE_DIALECT.getKey() + "=" + ctx.lookup("java:comp/env/interfacages/jdbc/dialect")
               + System.lineSeparator())
            .append(INTERFACAGES_DATABASE_DRIVER.getKey() + "=" + ctx.lookup("java:comp/env/interfacages/jdbc/driverClass")
               + System.lineSeparator())
            .append(INTERFACAGES_DATABASE_URL.getKey() + "=" + ctx.lookup("java:comp/env/interfacages/jdbc/url")
               + System.lineSeparator())
            .append(INTERFACAGES_DATABASE_USER.getKey() + "=" + ctx.lookup("java:comp/env/interfacages/jdbc/user")
               + System.lineSeparator())
            .append(INTERFACAGES_DATABASE_PASSWORD.getKey() + "=" + ctx.lookup("java:comp/env/interfacages/jdbc/password")
               + System.lineSeparator());

         sb.append(System.lineSeparator());
         sb.append(System.lineSeparator());

         sb.append("#############################" + System.lineSeparator())
            .append("###       RESSOURCES      ###" + System.lineSeparator())
            .append("#############################" + System.lineSeparator())
            .append(SIP_MAX_TABLE_SIZE.getKey() + "=" + ctx.lookup("java:comp/env/interfacage/maxSipTableSize")
               + System.lineSeparator())
            .append(SGL_MAX_TABLE_SIZE.getKey() + "=" + ctx.lookup("java:comp/env/interfacage/maxSglTableSize")
               + System.lineSeparator());

         sb.append(System.lineSeparator());
         sb.append(System.lineSeparator());

         sb.append("#############################" + System.lineSeparator())
            .append("###          LDAP         ###" + System.lineSeparator())
            .append("#############################" + System.lineSeparator())
            .append(LDAP_AUTHENTICATION.getKey() + "=false" + System.lineSeparator())
            .append(LDAP_URL.getKey() + "=" + ctx.lookup("java:comp/env/ldap/url") + System.lineSeparator())
            .append(LDAP_USER.getKey() + "=" + ctx.lookup("java:comp/env/ldap/userDn") + System.lineSeparator())
            .append(LDAP_PASSWORD.getKey() + "=" + ctx.lookup("java:comp/env/ldap/userPass") + System.lineSeparator());

         sb.append(System.lineSeparator());
         sb.append(System.lineSeparator());

         sb.append("#############################" + System.lineSeparator())
            .append("###   ACTIVE DIRECTORY    ###" + System.lineSeparator())
            .append("#############################" + System.lineSeparator())
            .append(ACTIVE_DIRECTORY_DOMAIN.getKey() + "=" + ctx.lookup("java:comp/env/activedirectory/domain")
               + System.lineSeparator())
            .append(
               ACTIVE_DIRECTORY_URL.getKey() + "=" + ctx.lookup("java:comp/env/activedirectory/url") + System.lineSeparator());

         sb.append(System.lineSeparator());

         Files.write(tumoPropertiesPath, sb.toString().getBytes(), StandardOpenOption.APPEND, StandardOpenOption.APPEND);

      }catch(NamingException | IOException e){
         log.error("Erreur lors de l'écriture du fichier " + TumorotekProperties.TUMO_PROPERTIES_PATH, e);
      }

   }

}
