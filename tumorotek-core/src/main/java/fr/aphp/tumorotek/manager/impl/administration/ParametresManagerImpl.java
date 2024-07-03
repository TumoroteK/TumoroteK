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
package fr.aphp.tumorotek.manager.impl.administration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import fr.aphp.tumorotek.dao.administration.ParametreValeurSpecifiqueDao;
import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.manager.administration.ParametresManager;
import fr.aphp.tumorotek.model.config.ParametreValeurSpecifique;
import fr.aphp.tumorotek.param.EParametreValeurParDefaut;
import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.param.TumorotekProperties;
import fr.aphp.tumorotek.utils.TKStringUtils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

/**

 * Interface du service de gestion des paramètres de l'application et des plateformes.
 * Une plateforme possède des paramètres dont les valeurs par défaut sont définies dans la classe EParametreValeurParDefaut.
 * Lorsque l'utilisateur modifie une valeur par défaut, les modifications sont sauvegardées dans la table "PARAMETRE_VALEUR_SPECIFIQUE".
 * Il existe également deux paramètres partagés par toutes les plateformes.  Ils sont gérés différemment (pour faciliter le déploiement).
 * Il s'agit du message de bienvenue défini dans "tumorotek.properties" et du logo situé dans "catalina/localhost".
 *
 * @author GCH
 */


public class ParametresManagerImpl implements ParametresManager
{

   public static final Logger log = LoggerFactory.getLogger(ParametresManagerImpl.class);

   private ParametreValeurSpecifiqueDao parametreValeurSpecifiqueDao;

   private final static String LOGO_FILEPATH = TumorotekProperties.TUMO_PROPERTIES_DIR + "/logo.png";


   // Dependency injection

   public void setParametreValeurSpecifiqueDao(ParametreValeurSpecifiqueDao parametreValeurSpecifiqueDao){
      this.parametreValeurSpecifiqueDao = parametreValeurSpecifiqueDao;
   }

   /**
    * @see fr.aphp.tumorotek.manager.administration.ParametresManager#getMessageAccueil(boolean)
    */
   @Override
   public String getMessageAccueil(final boolean raw){

      //On utilise un nouveau Properties à chaque appel pour que le message soit mis à jour sans nécessiter
      //un redémarrage de l'application
      final Properties tumoProperties = new Properties();
      try( InputStream is = Files.newInputStream(Paths.get(TumorotekProperties.TUMO_PROPERTIES_PATH)) ){
         tumoProperties.load(is);
      }catch(final IOException e){
         log.error("Erreur lors du chargement du fichier {}", TumorotekProperties.TUMO_PROPERTIES_PATH, e);
      }

      String msgAccueil = tumoProperties.getProperty(TkParam.MSG_ACCUEIL.getKey());

      if(!StringUtils.isEmpty(msgAccueil) && !raw){
         msgAccueil = TKStringUtils.cleanHtmlString(msgAccueil);
         msgAccueil = TKStringUtils.cleanPlaceholders(msgAccueil, "${", "}");
      }

      return msgAccueil;
   }


   /**
    * @see fr.aphp.tumorotek.manager.administration.ParametresManager#saveMessageAccueil(String)
    */
   @Override
   public boolean saveMessageAccueil(final String msgAccueil){

      boolean saved = false;

      final File propertiesFile = new File(TumorotekProperties.TUMO_PROPERTIES_PATH);

      String cleanMsgAccueil = TKStringUtils.cleanHtmlString(msgAccueil);
      cleanMsgAccueil = TKStringUtils.cleanPlaceholders(cleanMsgAccueil, "${", "}");

      final Parameters params = new Parameters();
      final FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
         new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class).configure(
            params.properties().setFile(propertiesFile));

      try{
         final Configuration config = builder.getConfiguration();
         config.setProperty(TkParam.MSG_ACCUEIL.getKey(), cleanMsgAccueil);
         builder.save();
         saved = true;
      }catch(final ConfigurationException e){
         log.error("Echec de la sauvegarde du message d'accueil", e);
      }

      return saved;

   }

   /**
    * @see fr.aphp.tumorotek.manager.administration.ParametresManager#deleteMessageAccueil()
    */
   @Override
   public boolean deleteMessageAccueil(){
      return saveMessageAccueil("");
   }

   /**
    * @see fr.aphp.tumorotek.manager.administration.ParametresManager#getLogoFile()
    */
   @Override
   public File getLogoFile(){
      return new File(LOGO_FILEPATH);
   }


   /**
    * @see fr.aphp.tumorotek.manager.administration.ParametresManager#saveLogo(File)
    */
   @Override
   public boolean saveLogo(final File logoFile){

      boolean saved = false;

      final String oldLogoFilePath = LOGO_FILEPATH + ".old";

      final File logo = new File(LOGO_FILEPATH);
      final File oldLogo = new File(oldLogoFilePath);

      if(logo.exists()){
         logo.renameTo(oldLogo);
      }

      try( InputStream is = new ByteArrayInputStream(Files.readAllBytes(logoFile.toPath()));
         FileOutputStream fos = new FileOutputStream(logo) ){

         final byte[] buf = new byte[1024];
         int i = 0;
         while((i = is.read(buf)) != -1){
            fos.write(buf, 0, i);
         }

         saved = true;

      }catch(final IOException e){

         if(logo.exists()){
            logo.delete();
         }

         oldLogo.renameTo(logo);

         log.error("Erreur lors de la sauvegarde du fichier {}", LOGO_FILEPATH, e);

      }finally{

         if(oldLogo.exists()){
            oldLogo.delete();
         }

      }

      return saved;
   }

   /**
    * @see fr.aphp.tumorotek.manager.administration.ParametresManager#deleteLogo()
    */
   @Override
   public boolean deleteLogo(){

      boolean deleted = false;

      final File logoFile = new File(LOGO_FILEPATH);
      if(logoFile.exists()){
         deleted = logoFile.delete();
      }

      return deleted;

   }

   /**
    * Récupère une liste de ParametreDTO en fonction de l'identifiant de la plateforme.
    *
    * @param plateformeId L'identifiant de la plateforme.
    * @return Set de ParametreDTO.
    */
   public List<ParametreDTO> findParametresByPlateformeId(Integer plateformeId) {
      // Récupère les ParametreValeurSpecifique modifiés pour la plateforme donnée
      List<ParametreValeurSpecifique> parametresModifies = parametreValeurSpecifiqueDao.findAllByPlateformeId(plateformeId);

      // Initialise le set qui contiendra les ParametreDTO résultants
      List<ParametreDTO> listOfParametres = new ArrayList<>();


      // Parcours des valeurs par défaut des paramètres
      for (EParametreValeurParDefaut eParametreValeurParDefaut : EParametreValeurParDefaut.values()) {
         String code = eParametreValeurParDefaut.getCode();

         // Vérifie s'il existe un ParametreValeurSpecifique modifié avec le même code
         ParametreValeurSpecifique parametreModifie = findModifiedParamByCode(parametresModifies, code);

         if (parametreModifie != null) {
            // Si oui, mappe-le vers ParametreDTO et ajoute-le au set
            ParametreDTO parametreDTO = ParametreDTO.mapFromEntity(parametreModifie);
            listOfParametres.add(parametreDTO);
         } else {
            // Si aucun ParametreValeurSpecifique modifié n'est trouvé, ajoute les valeurs par défaut au set
            listOfParametres.add(new ParametreDTO(code, eParametreValeurParDefaut.getValeur(), eParametreValeurParDefaut.getType(), eParametreValeurParDefaut.getGroupe()));
         }
      }

      // Retourne le set des ParametreDTO
      return listOfParametres;
   }

   /**
    * Recherche un ParametreValeurSpecifique modifié avec le code donné dans la liste spécifiée.
    *
    * @param modifiedParams Liste des ParametreValeurSpecifique modifiés.
    * @param code           Le code du paramètre à rechercher.
    * @return Le ParametreValeurSpecifique modifié correspondant au code, ou null s'il n'est pas trouvé.
    */
   private ParametreValeurSpecifique findModifiedParamByCode(List<ParametreValeurSpecifique> modifiedParams, String code) {
      // Itère à travers la liste des ParametreValeurSpecifique modifiés et trouve celui avec le code correspondant
      for (ParametreValeurSpecifique param : modifiedParams) {
         if (code.equals(param.getCode())) {
            return param;
         }
      }
      return null; // Retourne null si non trouvé
   }


   /**
    * Recherche un ParametreValeurSpecifique en fonction de l'identifiant de la plateforme et du code.
    *
    * @param plateformID L'identifiant de la plateforme.
    * @param code Le code du ParametreValeurSpecifique.
    * @return Le ParametreValeurSpecifique trouvé.
    */
   @Override
   public ParametreValeurSpecifique findParametresByPlateformeIdAndCode(Integer plateformID, String code) {
      List<ParametreValeurSpecifique> parametres = parametreValeurSpecifiqueDao.findByPlateformeIdAndCode(plateformID, code);

      if (!parametres.isEmpty()) {
         return parametres.get(0);
      } else {
         return null;
      }
   }

   /**
    * Met à jour la valeur d'un ParametreValeurSpecifique en fonction de l'identifiant de la plateforme, du code
    * et de la nouvelle valeur fournie. Crée un nouveau ParametreValeurSpecifique s'il n'existe pas.
    *
    * @param plateformID L'identifiant de la plateforme.
    * @param code Le code du ParametreValeurSpecifique.
    * @param newValue La nouvelle valeur du ParametreValeurSpecifique.
    */
   @Override
   public void updateValeur(Integer plateformID, String code, String newValue){
      // Vérifie si le code existe dans l'énumération
      EParametreValeurParDefaut paramEnum = EParametreValeurParDefaut.findByCode(code);
      if (paramEnum == null) {
         throw new IllegalArgumentException("Invalid code: " + code);
      }
      // Trouve le ParametreValeurSpecifique dans la base de données en utilisant l'id de la plateforme et le code
      ParametreValeurSpecifique plateformParametreValeurSpecifique = findParametresByPlateformeIdAndCode(plateformID, code);
      if (plateformParametreValeurSpecifique != null) {
         // Si le ParametreValeurSpecifique existe, met à jour sa valeur
         plateformParametreValeurSpecifique.setValeur(newValue);
         parametreValeurSpecifiqueDao.updateObject(plateformParametreValeurSpecifique);
      } else {
         // Si le ParametreValeurSpecifique n'existe pas, crée un nouveau ParametreValeurSpecifique
         ParametreValeurSpecifique newParametreValeurSpecifique = new ParametreValeurSpecifique();
         newParametreValeurSpecifique.setPlateformeId(plateformID);
         newParametreValeurSpecifique.setCode(code);
         newParametreValeurSpecifique.setValeur(newValue);
         newParametreValeurSpecifique.setType(paramEnum.getType());
         newParametreValeurSpecifique.setGroupe(paramEnum.getGroupe());
         parametreValeurSpecifiqueDao.createObject(newParametreValeurSpecifique);

      }
   }

   /**
    * Supprime un enregistrement de la table ParametreValeurSpecifique correspondant à l'identifiant de la Plateforme
    * et au code donnés.
    *
    * @param plateformeId l'identifiant de la Plateforme
    * @param code le code de ParametreValeurSpecifique
    * @return boolean indiquant si la suppression a été effectuée
    */
   @Override
   public boolean removeByPlateformeIdAndCodeManager(Integer plateformeId, String code) {
      // Rechercher le paramètre à supprimer
      ParametreValeurSpecifique parametreToDelete = findParametresByPlateformeIdAndCode(plateformeId, code);
      // Si le paramètre est trouvé, on le supprime
      if (parametreToDelete != null) {
         Integer parametreId = parametreToDelete.getParametreValeurSpecifiqueId();
         parametreValeurSpecifiqueDao.removeObject(parametreId);
         return true;
         // Si le paramètre n'est pas trouvé dans la base de données, il n'a pas été supprimé, donc on retourne false
      } else {
         return false;
      }
   }


}
