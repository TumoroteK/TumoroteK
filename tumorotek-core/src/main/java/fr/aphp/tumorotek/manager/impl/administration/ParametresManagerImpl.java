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
import java.util.Properties;
import fr.aphp.tumorotek.dao.administration.ParametreDao;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion de paramètres de l'application
 * @author GCH
 *
 */
public class ParametresManagerImpl implements ParametresManager
{

   public static final Logger log = LoggerFactory.getLogger(ParametresManagerImpl.class);

   private ParametreDao parametreDao;

   private final static String LOGO_FILEPATH = TumorotekProperties.TUMO_PROPERTIES_DIR + "/logo.png";

   public void setParametreDao(ParametreDao parametreDao){
      this.parametreDao = parametreDao;
   }

   /* (non-Javadoc)
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

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.administration.ParametresManager#saveMessageAccueil(java.lang.String)
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

   @Override
   /*
    * (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.administration.ParametresManager#deleteMessageAccueil()
    */ public boolean deleteMessageAccueil(){
      return saveMessageAccueil("");
   }

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.administration.ParametresManager#getLogoFile()
    */
   @Override
   public File getLogoFile(){
      return new File(LOGO_FILEPATH);
   }

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.administration.ParametresManager#saveLogo(java.io.File)
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

   @Override
   /*
    * (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.administration.ParametresManager#deleteLogo()
    */ public boolean deleteLogo(){

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
    * @return Une liste de ParametreDTO.
    */
   public List<ParametreDTO> getParametresByPlateformeId(Integer plateformeId){
      // Crée une liste pour stocker les ParametreDTO
      List<ParametreDTO> listOfParameters = new ArrayList<>();

      // Parcours de l'énumération des paramètres par défaut
      for(EParametreValeurParDefaut param : EParametreValeurParDefaut.values()){
         // Obtient le code du paramètre
         String code = param.getCode();
         // Récupère le ParametreValeurSpecifique depuis la base de données en utilisant l'id de la plateforme et le code
         ParametreValeurSpecifique resultFromDB = findParametresByPlateformeIdAndCode(plateformeId, code);
         // Vérifie si le ParametreValeurSpecifique existe dans la base de données
         if(resultFromDB != null){
            // Si oui, mappe le ParametreValeurSpecifique vers ParametreDTO et l'ajoute à la liste
            ParametreDTO parametreDTO = ParametreDTO.mapFromEntity(resultFromDB);
            listOfParameters.add(parametreDTO);
         }else{
            // Si non, crée un nouveau ParametreDTO avec les valeurs par défaut et l'ajoute à la liste
            listOfParameters.add(new ParametreDTO(code, param.getValeur(), param.getType(), param.getGroupe()));
         }
      }
      // Retourne la liste des ParametreDTO
      return listOfParameters;
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
      List<ParametreValeurSpecifique> parametres = parametreDao.findByPlateformeIdAndCode(plateformID, code);

      if (!parametres.isEmpty()) {
         // Return the first element if the list is not empty
         return parametres.get(0);
      } else {
         // Handle the case where the list is empty (return null or take appropriate action)
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
         parametreDao.updateObject(plateformParametreValeurSpecifique);
      } else {
         // Si le ParametreValeurSpecifique n'existe pas, crée un nouveau ParametreValeurSpecifique
         ParametreValeurSpecifique newParametreValeurSpecifique = new ParametreValeurSpecifique();
         newParametreValeurSpecifique.setPlateformeId(plateformID);
         newParametreValeurSpecifique.setCode(code);
         newParametreValeurSpecifique.setValeur(newValue);
         newParametreValeurSpecifique.setType(paramEnum.getType());
         newParametreValeurSpecifique.setGroupe(paramEnum.getGroupe());
         parametreDao.createObject(newParametreValeurSpecifique);
      }
   }


}
