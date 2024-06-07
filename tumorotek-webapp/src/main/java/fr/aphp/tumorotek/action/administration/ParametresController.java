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
package fr.aphp.tumorotek.action.administration;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.component.TexteModale;
import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.manager.administration.ParametresManager;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.utils.MessagesUtils;
import fr.aphp.tumorotek.utils.TKStringUtils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Fileupload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.camel.util.StringHelper.sanitize;

/**
 * Contrôleur gérant la configuration des paramètres de la plateforme + paramètres communs.
 *
 * @author GCH
 */
public class ParametresController
{

   private static final Logger logger = LoggerFactory.getLogger(ParametresController.class);

   private static final long serialVersionUID = -2450763003941018231L;

   /** Formats image acceptés */
   private static final MediaType[] AUTHORIZED_IMAGE_MEDIA_TYPES =
      new MediaType[] {MediaType.IMAGE_GIF, MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG};

   // Liste de paramètres représentant les parameters de la plateforme.
   public List<ParametreDTO> parameterList = new ArrayList<>();

   final private Map<ParametreDTO, Boolean> editModeMap = new HashMap<>();

   // Map stockant les valeurs originales de chaque paramètre avant toute modification.
   private Map<String, ParametreDTO> originalValuesMap = new HashMap<>();

   private ParametresManager parametresManager;

   // Message de bienvenue affiché dans l'interface utilisateur.
   private String welcomeMessage;

   // Image de bienvenue affichée dans l'interface utilisateur.
   private AImage welcomeImage;

   // Titre de la plateforme
   private String plateformeTitle;

   // Indicateur indiquant si une image de bienvenue existe.
   private boolean welcomeImageExists;

   // Indicateur indiquant si un message de bienvenue existe.
   private boolean welcomeMessageExists;

   private String imageUpdateError = "error.params.image.update";

   //      **************************** | Mise en place |  ******************************************

   /**
    * Mise en place:
    * Charge la liste des paramètres de la plateforme, trie la liste par groupe puis par code,
    * initialise les maps de gestion du mode édition et des valeurs originales, init les variables.
    */
   @Init
   public void init(){
      // Initialise parameterList et parametresManager
      parameterList = SessionUtils.getParametresPlateforme(Sessions.getCurrent().getAttributes());
      Collections.sort(parameterList, new ParametreComparator());
      parametresManager = ManagerLocator.getManager(ParametresManager.class);

      // Initialise les maps editModeMap et originalValuesMap
      for(ParametreDTO parameter : parameterList){
         editModeMap.put(parameter, false);
         originalValuesMap.put(parameter.getCode(),
            new ParametreDTO(parameter.getCode(), parameter.getValeur(), parameter.getType(), parameter.getGroupe()));
      }
      initWelcomeMessage();
      initWelcomeImage();
      plateformeTitle = buildPlateformString();
   }

   //      ****************************** | Event Listeners : App Params | *************************************************

   /**
    * Modifie l'image de bienvenue en fonction du média téléchargé.
    * Si le média est d'un type d'image autorisé, il est enregistré et l'image de bienvenue est mise à jour.
    * Affiche des messages d'erreur en cas de problème avec le média ou son type.
    */

   @Command
   @NotifyChange({"welcomeImage", "welcomeImageExists"})
   public void modifyWelcomeImage(){
      // Récupérer le média téléchargé
      final Media uploadedMedia = Fileupload.get();
      if(uploadedMedia == null){
         return;
      }

      // Vérifier le type du média téléchargé
      boolean mediaTypeValid = isMediaTypeValid(uploadedMedia);

      // Traiter en fonction de la validité du type de média
      if(mediaTypeValid){
         // Enregistrer le média téléchargé comme image de bienvenue
         saveWelcomeImage(uploadedMedia);
      }else{
         // Afficher un message d'erreur pour un type de média invalide
         displayError(Labels.getLabel("error.params.image.format"));
      }
   }

   /**
    * Supprime l'image de bienvenue (logo) après confirmation de l'utilisateur.
    * Si l'utilisateur annule (clic sur "Annuler"), aucune action n'est effectuée.
    */

   @Command
   @NotifyChange({"welcomeImage", "welcomeImageExists"})
   public void deleteWelcomeImage(){
      // Récupère le titre et le corps de la question localisés pour la boîte de dialogue de confirmation
      String questionTitle = Labels.getLabel("question.title.logo.delete");
      String questionBody = Labels.getLabel("question.body.logo.delete");
      // Affiche une boîte de dialogue modale et attend la confirmation de l'utilisateur
      boolean isOk = MessagesUtils.openQuestionModal(questionTitle, questionBody);

      if(isOk){
         // Tente de supprimer le logo
         boolean imageWasDeleted = parametresManager.deleteLogo();
         // Si le logo a été supprimé avec succès
         if(imageWasDeleted){
            // Affiche le message de réussite
            String messageBody = Labels.getLabel("success.logo.delete");
            String messageHeader = Labels.getLabel("general.success");
            MessagesUtils.openInfoModal(messageHeader, messageBody);
            welcomeImage = null;
            welcomeImageExists = false;
         }else{
            displayError(Labels.getLabel(imageUpdateError));

         }

      }
   }

   /**
    * Supprime le message de bienvenue après confirmation de l'utilisateur.
    * Si la suppression échoue, un message d'erreur est affiché.
    *
    */
   @Command
   @NotifyChange({"welcomeMessage", "welcomeMessageExists"})
   public void deleteWelcomeMessage(){

      String messageBody = Labels.getLabel("params.modale.message.delete");
      String messageHeader = Labels.getLabel("params.modale.message.libelle");

      boolean isOk = MessagesUtils.openQuestionModal(messageHeader, messageBody);

      if(isOk){

         final boolean deleted = parametresManager.deleteMessageAccueil();

         if(deleted){
            welcomeMessage = Labels.getLabel("params.message.empty");
            welcomeMessageExists = false;
         }else{
            displayError(Labels.getLabel("error.params.message.update"));

         }

      }

   }

   /**
    * Méthode appelée lors de la modification du message d'accueil.
    * Affiche une boîte de dialogue modale permettant à l'utilisateur
    * de modifier le message d'accueil de l'application.
    */
   @Command
   @NotifyChange({"welcomeMessage", "welcomeMessageExists"})
   public void modifyWelcomeMessage(){

      EventListener<Event> onCloseListener = event -> {
         if(null != event.getData()){
            String newMessage = (String) event.getData();
            boolean saved = parametresManager.saveMessageAccueil(newMessage);
            if(saved){
               welcomeMessage = newMessage;
               welcomeMessageExists = true;
            }else{
               displayError(Labels.getLabel("error.params.message.update"));

            }
         }
      };

      String currentMsg = parametresManager.getMessageAccueil(true);
      TexteModale.show(Labels.getLabel("params.modale.message.titre"), Labels.getLabel("params.modale.message.libelle"),
         currentMsg, 5, true, onCloseListener);

   }

   //      **************************** | Event Listeners : Plateforme Params |  ******************************************

   /**
    * Active le mode édition pour un paramètre donné et enregistre la valeur d'origine du paramètre.
    *
    * @param parameter Le paramètre à éditer.
    */
   @Command
   @NotifyChange("parameterList")
   public void editParameter(@BindingParam("parameter") ParametreDTO parameter){
      boolean isEditMode = isEditMode(parameter);

      if(!isEditMode){
         // Save the original value
         originalValuesMap.put(parameter.getCode(),
            new ParametreDTO(parameter.getCode(), parameter.getValeur(), parameter.getType(), parameter.getGroupe()));
         editModeMap.put(parameter, true);
      }
   }

   /**
    * Annule les modifications effectuées lors de l'édition d'un paramètre.
    *
    * @param parameter le paramètre à annuler
    */
   @Command
   @NotifyChange("parameterList")
   public void cancelEdit(@BindingParam("parameter") ParametreDTO parameter){
      // Rétablit les valeurs originales
      ParametreDTO originalValues = originalValuesMap.get(parameter.getCode());
      parameter.setValeur(originalValues.getValeur());

      // Réinitialiser le mode édition à faux
      editModeMap.put(parameter, false);

   }

   @Command
   @NotifyChange("parameterList")
   public void saveParameter(@BindingParam("parameter") ParametreDTO parameter,
      @ContextParam(ContextType.TRIGGER_EVENT) Event event){

      String selectedValue = parameter.getValeur();
      parameter.setValeur(selectedValue);
      // Enregistrer la valeur mise à jour
      saveParameter(parameter);

      editModeMap.put(parameter, false);
   }

   //      ****************************** | Méthodes utilitaires    | *************************************************

   /**
    * Initialise l'image de bienvenue en récupérant le fichier image depuis le gestionnaire de paramètres.
    * Si le fichier existe, charge l'image à l'aide de la classe AImage.
    * En cas d'erreur lors du chargement de l'image, affiche un message d'erreur.
    */

   private void initWelcomeImage(){
      File logofile = parametresManager.getLogoFile();
      if(logofile.exists()){
         try{
            welcomeImage = new AImage(logofile);
            welcomeImageExists = true;
         }catch(final IOException e){
            logger.error("Unable to load the image file [{}]", sanitize(logofile.getAbsolutePath()), e);
            displayError(Labels.getLabel(imageUpdateError));

         }
      } else {
         welcomeImageExists = false;
      }
   }

   /**
    * Initialise le message d'accueil en récupérant la valeur depuis le gestionnaire de paramètres.
    * Le message est ensuite converti en entités HTML et, s'il est vide, il est remplacé par la valeur de "params.message.empty"
    */
   private void initWelcomeMessage(){
      welcomeMessage = parametresManager.getMessageAccueil(false);

      if(StringUtils.isEmpty(welcomeMessage)){
         welcomeMessage = Labels.getLabel("params.message.empty");
         welcomeMessageExists = false;
      } else {
         welcomeMessageExists = true;

      }
   }

   /**
    * Construit la chaîne de titre de la plateforme pour l'affichage.
    *
    * @return La chaîne de titre de la plateforme.
    */
   private String buildPlateformString(){
      Plateforme plateforme = SessionUtils.getPlateforme(Sessions.getCurrent().getAttributes());

      String plateformName = plateforme.getNom();

      String translatedTitle = Labels.getLabel("params.plateforme.title");

      return String.format("%s %s", translatedTitle, plateformName);

   }

   /**
    * Enregistre les modifications apportées à un paramètre dans la base de données
    *
    * @param parameter Le paramètre à enregistrer.
    */
   private void saveParameter(ParametreDTO parameter){
      // Obtient la plateforme actuelle depuis la session
      Plateforme plateforme = SessionUtils.getPlateforme(Sessions.getCurrent().getAttributes());

      // Met à jour/sauvegarde le paramètre de la plateforme en base de données
      parametresManager.updateValeur(plateforme.getPlateformeId(), parameter.getCode(), parameter.getValeur());
   }

   /**
    * Vérifie si le type de média est valide.
    *
    * @param uploadedMedia le média téléchargé
    * @return true si le type de média est valide, sinon false
    */
   private boolean isMediaTypeValid(Media uploadedMedia){
      boolean mediaTypeValide = false;

      if(uploadedMedia != null){
         try{
            final MediaType uploadedMediaType = MediaType.valueOf(uploadedMedia.getContentType());
            mediaTypeValide = Arrays.asList(AUTHORIZED_IMAGE_MEDIA_TYPES).contains(uploadedMediaType);
         }catch(final InvalidMediaTypeException imte){
            logger.warn("MediaType inconnu", imte);
         }

      }
      return mediaTypeValide;

   }

   /**
    * Enregistre le média téléchargé comme image de bienvenue.
    *
    * @param uploadedMedia Le média téléchargé à enregistrer.
    */
   private void saveWelcomeImage(Media uploadedMedia){
      boolean saved = false;
      File tmpImageFile = null;

      try{
         // Créer un fichier temporaire pour l'image de bienvenue
         tmpImageFile = File.createTempFile("welcomeImage", ".tmp");

         // Écrire les données du média dans le fichier temporaire
         FileUtils.writeByteArrayToFile(tmpImageFile, uploadedMedia.getByteData());

         // Sauvegarder l'image de bienvenue
         saved = parametresManager.saveLogo(tmpImageFile);
         if(saved){
            initWelcomeImage();
         }else{
            // Afficher un message d'erreur en cas d'échec de l'enregistrement
            displayError(imageUpdateError);
         }
      }catch(final IOException e){
         logger.error("Erreur lors de la conversion du média en fichier", e);
      }finally{
         // Supprimer le fichier temporaire après utilisation
         if(tmpImageFile != null && tmpImageFile.exists()){
            tmpImageFile.delete();
         }
      }


   }

   /**
    * Affiche un modal d'erreur avec la clé du message spécifié.
    *
    * @param messageKey La clé du message d'erreur à afficher.
    */
   private void displayError(String messageKey){
      String messageHeader = Labels.getLabel("general.error");
      String messageBody = Labels.getLabel(messageKey);
      MessagesUtils.openErrorModal(messageHeader, messageBody);
   }


   /**
    * Indique si le mode édition est activé pour un paramètre donné.
    *
    * @param parameter Le paramètre à vérifier.
    * @return true si le mode édition est activé, sinon false.
    */
   public boolean isEditMode(ParametreDTO parameter){
      return editModeMap.get(parameter);
   }

   /**
    * Comparateur pour trier les paramètres par groupe puis par code.
    */
   public class ParametreComparator implements Comparator<ParametreDTO>
   {

      @Override
      public int compare(ParametreDTO param1, ParametreDTO param2){
         // Comparer par groupe
         int groupeComparison = param1.getGroupe().compareTo(param2.getGroupe());

         // si c'est la meme groupe, comparer par code
         if(groupeComparison == 0){
            return param1.getCode().compareTo(param2.getCode());
         }

         return groupeComparison;
      }
   }

   public List<ParametreDTO> getParameterList(){
      return parameterList;
   }

   public String getPlateformeTitle(){
      return plateformeTitle;
   }

   public void setPlateformeTitle(String plateformeTitle){
      this.plateformeTitle = plateformeTitle;
   }

   public String getWelcomeMessage(){
      return welcomeMessage;
   }

   public AImage getWelcomeImage(){
      return welcomeImage;
   }

   public void setWelcomeMessage(String welcomeMessage){
      this.welcomeMessage = welcomeMessage;
   }

   public void setWelcomeImage(AImage welcomeImage){
      this.welcomeImage = welcomeImage;
   }


   public void setParameterList(List<ParametreDTO> parameterList){
      this.parameterList = parameterList;
   }

   public boolean isWelcomeImageExists(){
      return welcomeImageExists;
   }

   public void setWelcomeImageExists(boolean welcomeImageExists){
      this.welcomeImageExists = welcomeImageExists;
   }

   public boolean isWelcomeMessageExists(){
      return welcomeMessageExists;
   }

   public void setWelcomeMessageExists(boolean welcomeMessageExists){
      this.welcomeMessageExists = welcomeMessageExists;
   }
}
