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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.utils.MessagesUtils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import org.apache.commons.io.FileUtils;
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
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.manager.administration.ParametresManager;
import org.zkoss.zul.Radio;


public class ParametresController
{

   public static final Logger logger = LoggerFactory.getLogger(ParametresController.class);

   private static final long serialVersionUID = -2450763003941018231L;

   /** Formats image acceptés */
   private static final MediaType[] AUTHORIZED_IMAGE_MEDIA_TYPES =
      new MediaType[] {MediaType.IMAGE_GIF, MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG};

   public Set<ParametreDTO> parameterList  = new HashSet<>();

   private Map<ParametreDTO, Boolean> editModeMap  = new HashMap<>();

   private Map<String, ParametreDTO> originalValuesMap = new HashMap<>();

   public boolean logoExists = true;

   final ParametresManager parametresManager = ManagerLocator.getManager(ParametresManager.class);

   public Set<ParametreDTO> getParameterList() {
      return parameterList;
   }

   /**
    * Indique si le mode édition est activé pour un paramètre donné.
    *
    * @param parameter le paramètre
    * @return true si le mode édition est activé, sinon false
    */
   public boolean isEditMode(ParametreDTO parameter) {
      return editModeMap.get(parameter);
   }

   /**
    * Initialise le composant en chargeant les paramètres nécessaires.
    */
   @Init
   public void init() {

      // Initialise parameterList
         parameterList = SessionUtils.getPlatformParameters();
         logoExists = parametresManager.isLogoExists();

      // Initialise les maps editModeMap et originalValuesMap
         for (ParametreDTO parameter : parameterList) {
            editModeMap.put(parameter, false);
            originalValuesMap.put(parameter.getCode(), new ParametreDTO(parameter.getCode(), parameter.getValeur(), parameter.getType(), parameter.getGroupe()));
         }
      }


//      ************************************  Event Listeners ****************************************************


   /**
    * Permet d'éditer un paramètre et gère l'enregistrement des modifications.
    *
    * @param parameter le paramètre à éditer
    */
   @Command
   @NotifyChange("parameterList")
   public void editParameter(@BindingParam("parameter") ParametreDTO parameter) {

      boolean isEditMode = isEditMode(parameter);

      if (!isEditMode) {
         editModeMap.put(parameter, true);
      } else {
         // Si c'est un message d'accueil, appelle la méthode pour enregistrer le message
         String messageCode = "params.message.accueil";
         if (parameter.getCode().equals(messageCode)){
            storeNewMsgAccueil(parameter.getValeur());
         } else {
            // Enregistre dans la base de données + Session
            saveParameter(parameter);
         }
         // Réinitialiser le mode édition à faux
         editModeMap.put(parameter, false);
      }
   }

   /**
    * Met à jour la valeur d'un paramètre suite à un événement de sélection (Radio Button).
    *
    * @param parameter le paramètre à mettre à jour
    * @param event     l'événement de sélection déclenché
    */
   @Command
   @NotifyChange("parameterList")
   public void updateParameterValue(@BindingParam("parameter") ParametreDTO parameter,
                                    @ContextParam(ContextType.TRIGGER_EVENT) CheckEvent event) {
      String selectedValue = ((Radio) event.getTarget()).getValue();
      parameter.setValeur(selectedValue);
      saveParameter(parameter);
   }

   /**
    * Annule les modifications effectuées lors de l'édition d'un paramètre.
    *
    * @param parameter le paramètre à annuler
    */
   @Command
   @NotifyChange("parameterList")
   public void cancelEdit(@BindingParam("parameter") ParametreDTO parameter) {
      // Rétablit les valeurs originales
      ParametreDTO originalValues = originalValuesMap.get(parameter.getCode());
      parameter.setValeur(originalValues.getValeur());

      // Réinitialiser le mode édition à faux
      editModeMap.put(parameter, false);


   }

   /**
    * Supprime le logo après confirmation de l'utilisateur via une boîte de dialogue modale.
    * Si l'utilisateur confirme la suppression, le logo est supprimé, et un message de réussite est affiché.
    *
    */
   @Command
   @NotifyChange("logoExists")
   public void deleteLogo() {
      // Récupère le titre et le corps de la question localisés pour la boîte de dialogue de confirmation
      String questionTitle= Labels.getLabel("question.title.logo.delete");
      String questionBody= Labels.getLabel("question.body.logo.delete");
      // Affiche une boîte de dialogue modale et attend la confirmation de l'utilisateur
      boolean isOk = MessagesUtils.openQuestionModal(questionTitle, questionBody);
      // Si l'utilisateur confirme la suppression
      if(isOk){
         // Tente de supprimer le logo
      boolean wasDeleted = parametresManager.deleteLogo();
         // Si le logo a été supprimé avec succès
      if (wasDeleted){
         // Affiche le message de réussite
         String messageBody  = Labels.getLabel("success.logo.delete");
         String messageHeader  = Labels.getLabel("general.success");
         MessagesUtils.openInfoModal(messageHeader, messageBody);
      }
         // Met à jour le drapeau pour indiquer si le logo existe
         logoExists = false;
      }
   }

   /**
    * Gère le téléchargement de logo.
    *
    * @param parameter le paramètre associé au fichier
    * @param event     l'événement de téléchargement déclenché
    */
   @Command
   public void handleFileUpload(@BindingParam("parameter") ParametreDTO parameter,
      @ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) {
      Media media = event.getMedia();

      String errorLabel = "general.error";
      if (isMediaTypeValide(media)) {
         // Enregistre le fichier
         boolean isSaved = saveLogo(media);
         if (isSaved){
            String filepath = media.getContentType();
            parameter.setValeur(filepath);
            logoExists = true;
         }
         else {
            // En cas d'échec de l'enregistrement, affiche un message d'erreur
            MessagesUtils.openErrorModal(Labels.getLabel(errorLabel), Labels.getLabel("error.params.image.update"));
         }
      } else {
         // Si le type de média n'est pas valide, affiche un message d'erreur
         MessagesUtils.openErrorModal(Labels.getLabel(errorLabel), Labels.getLabel("error.params.image.format"));

      }
   }


//***************************************** Utility Mehtods ******************************************************************

   /**
    * Enregistre les modifications apportées à un paramètre.
    *
    * @param parameter le paramètre à enregistrer
    */
   private void saveParameter(ParametreDTO parameter){
      Plateforme plateforme = SessionUtils.getPlateforme();
      // Met à jour/ sauvegarde le paramètre de la plateforme en BD
      parametresManager.updateValeur(plateforme.getPlateformeId(), parameter.getCode(), parameter.getValeur());
      Set<ParametreDTO> updatedParameters = parametresManager.getParametresByPlateformeId(SessionUtils.getPlateforme().getPlateformeId());
      // Met à jour les paramètres de la plateforme en session
      SessionUtils.setPlatformParameters(updatedParameters);
   }

   /**
    * Stocke le nouveau message d'accueil dans le fichier tumorotek.properties
    * @param msgAccueil nouveau message d'accueil
    */
   private void storeNewMsgAccueil(final String msgAccueil){

      final boolean saved = parametresManager.saveMessageAccueil(msgAccueil);

      if(!saved){
         Messagebox.show(Labels.getLabel("error.params.message.update"), Labels.getLabel("general.error"), Messagebox.OK,
            Messagebox.ERROR);

      }
   }

   /**
    * Vérifie si le type de média est valide.
    *
    * @param uploadedMedia le média téléchargé
    * @return true si le type de média est valide, sinon false
    */
   private boolean isMediaTypeValide(Media uploadedMedia){
      boolean mediaTypeValide = false;

      if (uploadedMedia != null) {
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
    * Enregistre le logo à partir du média téléchargé.
    *
    * @param uploadedMedia le média téléchargé représentant le logo
    * @return true si l'enregistrement du logo est réussi, sinon false
    */
   private boolean saveLogo(Media uploadedMedia){
      boolean saved = false;

      File tmpLogoFile = null;
      try{
         tmpLogoFile = File.createTempFile("logo", ".tmp");
         FileUtils.writeByteArrayToFile(tmpLogoFile, uploadedMedia.getByteData());
         saved = parametresManager.saveLogo(tmpLogoFile);
      }catch(final IOException e){
         logger.error("Erreur lors de la conversion du media en fichier", e);

      }finally{
         if(null != tmpLogoFile && tmpLogoFile.exists()){
            tmpLogoFile.delete();
         }
      }
      return saved;
   }

   public boolean getLogoExists(){
      return logoExists;
   }
}
