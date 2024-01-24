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
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.component.TexteModale;
import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.manager.administration.ParametresManager;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.utils.TKStringUtils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;

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
public class ParametresController extends AbstractController
{

   private static final long serialVersionUID = -2450763003941018231L;

   /** Formats image acceptés */
   private static final MediaType[] AUTHORIZED_IMAGE_MEDIA_TYPES =
      new MediaType[] {MediaType.IMAGE_GIF, MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG};

   public List<ParametreDTO> parameterList = new ArrayList<>();

   final private Map<ParametreDTO, Boolean> editModeMap = new HashMap<>();

   private Map<String, ParametreDTO> originalValuesMap = new HashMap<>();

   private ParametresManager parametresManager;

   private String accueilMsg;

   private AImage logo;

   private String plateformeTitle;

   /**
    * Mise en place: Initialise le contrôleur en charge de la configuration des paramètres.
    * Charge la liste des paramètres de la plateforme, trie la liste par groupe puis par code,
    * initialise les maps de gestion du mode édition et des valeurs originales.
    */
   @Init
   public void init(){
      // Initialise parameterList et parametresManager
      parameterList = SessionUtils.getParametresPlateforme(Sessions.getCurrent().getAttributes());
      Collections.sort(parameterList, new ParametreComparator());
      parametresManager = ManagerLocator.getManager(ParametresManager.class);

      plateformeTitle = buildPlateformString();

      // Initialise les maps editModeMap et originalValuesMap
      for(ParametreDTO parameter : parameterList){
         editModeMap.put(parameter, false);
         originalValuesMap.put(parameter.getCode(),
            new ParametreDTO(parameter.getCode(), parameter.getValeur(), parameter.getType(), parameter.getGroupe()));
      }

      initWelcomeMessage();
      initLogo();
   }

   private void initLogo(){
      File logofile = parametresManager.getLogoFile();
      if(logofile.exists()){
         try{

            logo = new AImage(logofile);

         }catch(final IOException e){
            log.error("Unable to load the image file [{}]", sanitize(logofile.getAbsolutePath()), e);

            Messagebox.show(Labels.getLabel("error.params.image.update"), Labels.getLabel("general.error"), Messagebox.OK,
               Messagebox.ERROR);
         }

      }
   }

   private void initWelcomeMessage(){
      accueilMsg = parametresManager.getMessageAccueil(false);
      accueilMsg = TKStringUtils.convertHtmlEntities(accueilMsg);

      if(StringUtils.isEmpty(accueilMsg)){
         accueilMsg = Labels.getLabel("params.message.empty");
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
    * Indique si le mode édition est activé pour un paramètre donné.
    *
    * @param parameter Le paramètre à vérifier.
    * @return true si le mode édition est activé, sinon false.
    */
   public boolean isEditMode(ParametreDTO parameter){
      return editModeMap.get(parameter);
   }

   /**
    * Enregistre les modifications apportées à un paramètre dans la session.
    *
    * @param parameter Le paramètre à enregistrer.
    */
   private void saveParameter(ParametreDTO parameter){
      SessionUtils.savePlatformParamsToSession(Sessions.getCurrent().getAttributes());
   }

   /**
    * Stocke le nouveau message d'accueil dans le fichier tumorotek.properties.
    *
    * @param msgAccueil Le nouveau message d'accueil.
    */
   private void storeNewMsgAccueil(final String msgAccueil){

      final boolean saved = parametresManager.saveMessageAccueil(msgAccueil);

      if(saved){
         // TODO: refresh the ui
      }else{
         Messagebox.show(Labels.getLabel("error.params.message.update"), Labels.getLabel("general.error"), Messagebox.OK,
            Messagebox.ERROR);
      }

   }

   //      ****************************** | Event Listeners : App Params | *************************************************

   /**
    * Méthode appelés lors du clic sur le bouton "Modifier" de l'image d'accueil
    */
   public void onClick$uploadImageAccueilBtn(){

      final Media uploadedMedia = Fileupload.get();
      if(uploadedMedia == null){
         return;
      }

      boolean mediaTypeValide = false;
      try{
         final MediaType uploadedMediaType = MediaType.valueOf(uploadedMedia.getContentType());
         mediaTypeValide = Arrays.asList(AUTHORIZED_IMAGE_MEDIA_TYPES).contains(uploadedMediaType);
      }catch(final InvalidMediaTypeException imte){
         log.warn("MediaType inconnu", imte);
      }

      if(mediaTypeValide){

         boolean saved = false;

         File tmpLogoFile = null;
         try{
            tmpLogoFile = File.createTempFile("logo", ".tmp");
            FileUtils.writeByteArrayToFile(tmpLogoFile, uploadedMedia.getByteData());
            saved = parametresManager.saveLogo(tmpLogoFile);
         }catch(final IOException e){
            log.error("Erreur lors de la conversion du media en fichier", e);
         }finally{
            if(null != tmpLogoFile && tmpLogoFile.exists()){
               tmpLogoFile.delete();
            }
         }

         if(saved){
            initLogo();
         }else{
            Messagebox.show(Labels.getLabel("error.params.image.update"), Labels.getLabel("general.error"), Messagebox.OK,
               Messagebox.ERROR);
         }

      }else{
         Messagebox.show(Labels.getLabel("error.params.image.format"), Labels.getLabel("general.error"), Messagebox.OK,
            Messagebox.ERROR);
      }

   }

   /**
    * Méthode appelés lors du clic sur le bouton "Supprimer" de l'image d'accueil
    */
   public void onClick$deleteImageAccueilBtn(){

      final int confirmation =
         Messagebox.show("Confirmer la suppression de l'image d'accueil ?", "Suppression de l'image d'accueil",
            Messagebox.YES | Messagebox.NO, Messagebox.EXCLAMATION);

      if(confirmation == Messagebox.YES){

         final boolean deleted = parametresManager.deleteLogo();

         if(deleted){
            logo = null;
         }else{
            Messagebox.show(Labels.getLabel("error.params.image.update"), Labels.getLabel("general.error"), Messagebox.OK,
               Messagebox.ERROR);
         }

      }

   }

   /**
    * Méthode appelé lors du clic sur le bouton "Modifier" du message d'accueil
    */
   public void onClick$updateMsgAccueilBtn(){

      final EventListener<Event> onCloseListener = event -> {
         if(null != event.getData()){
            storeNewMsgAccueil((String) event.getData());
         }
      };

      final String currentMsg = ManagerLocator.getManager(ParametresManager.class).getMessageAccueil(true);

      TexteModale.show(Labels.getLabel("params.modale.message.titre"), Labels.getLabel("params.modale.message.libelle"),
         currentMsg, 5, true, onCloseListener);

   }

   /**
    * Méthode appelé lors du clic sur le bouton "Supprimer" du message d'accueil
    */
   public void onClick$deleteMsgAccueilBtn(){

      final int confirmation =
         Messagebox.show("Confirmer la suppression du message d'accueil ?", "Suppression du message d'accueil",
            Messagebox.YES | Messagebox.NO, Messagebox.EXCLAMATION);

      if(confirmation == Messagebox.YES){

         final boolean deleted = ManagerLocator.getManager(ParametresManager.class).deleteMessageAccueil();

         if(deleted){
            // todo: Refresh UI
         }else{
            Messagebox.show(Labels.getLabel("error.params.message.update"), Labels.getLabel("general.error"), Messagebox.OK,
               Messagebox.ERROR);
         }

      }

   }

   //      **************************** | Event Listeners : Plateforme Params |  ******************************************

   /**
    * Permet d'éditer un paramètre et gère l'enregistrement des modifications.
    *
    * @param parameter le paramètre à éditer
    */
   @Command
   @NotifyChange("parameterList")
   public void editParameter(@BindingParam("parameter") ParametreDTO parameter){

      boolean isEditMode = isEditMode(parameter);

      if(!isEditMode){
         editModeMap.put(parameter, true);
      }else{

         // Enregistre dans la base de données + Session
         saveParameter(parameter);
      }
      // Réinitialiser le mode édition à faux
      editModeMap.put(parameter, false);
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
      @ContextParam(ContextType.TRIGGER_EVENT) CheckEvent event){
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
   public void cancelEdit(@BindingParam("parameter") ParametreDTO parameter){
      // Rétablit les valeurs originales
      ParametreDTO originalValues = originalValuesMap.get(parameter.getCode());
      parameter.setValeur(originalValues.getValeur());

      // Réinitialiser le mode édition à faux
      editModeMap.put(parameter, false);

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

   public String getAccueilMsg(){
      return accueilMsg;
   }

   public AImage getLogo(){
      return logo;
   }
}
