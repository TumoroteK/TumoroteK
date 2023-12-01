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
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.model.contexte.Plateforme;
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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.component.TexteModale;
import fr.aphp.tumorotek.manager.administration.ParametresManager;

/**
 * @author GCH
 *
 */
public class ParametresController
{

   private static final long serialVersionUID = -2450763003941018231L;

   /** Formats image acceptés */
   private static final MediaType[] AUTHORIZED_IMAGE_MEDIA_TYPES =
      new MediaType[] {MediaType.IMAGE_GIF, MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG};

   public Set<ParametreDTO> parameterList;

   private Map<ParametreDTO, Boolean> editModeMap;

   private Map<ParametreDTO, ParametreDTO> originalValuesMap;

   final ParametresManager parametresManager = ManagerLocator.getManager(ParametresManager.class);

   public String logoPath = "";



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
    * Initialisation du contrôleur.
    */
   @Init
   public void init() {

         // Initialize parameterList with sample data
         parameterList = SessionUtils.getPlatformParameters();

         // Ensure that parameterList is not null
         if (parameterList == null) {
            parameterList = new HashSet<>(); // Or initialize it appropriately
         }

         logoPath = parametresManager.getLogoFile().getAbsolutePath();

         // Initialize the editModeMap and originalValuesMap
         editModeMap = new HashMap<>();
         originalValuesMap = new HashMap<>(); // Initialize originalValuesMap

         for (ParametreDTO parameter : parameterList) {
            editModeMap.put(parameter, false);

            // Ensure that originalValuesMap is not null
            if (originalValuesMap == null) {
               originalValuesMap = new HashMap<>();
            }

            originalValuesMap.put(parameter, new ParametreDTO(parameter.getCode(), parameter.getValeur(), parameter.getType(), parameter.getGroupe()));
         }
      }




   @Command
   @NotifyChange("parameterList")
   public void editParameter(@BindingParam("parameter") ParametreDTO parameter) {
      boolean isEditMode = isEditMode(parameter);

      if (!isEditMode) {
         editModeMap.put(parameter, true);
      } else {
         Plateforme plateforme = SessionUtils.getPlateforme();
         // Save to the database here
         parametresManager.updateValeur(plateforme.getPlateformeId(), parameter.getCode(), parameter.getValeur());

         Set<ParametreDTO> updatedParameters = parametresManager.getParametresByPlateformeId(SessionUtils.getPlateforme().getPlateformeId());
         // update the session
         SessionUtils.setPlatformParameters(updatedParameters);

         editModeMap.put(parameter, false);
      }
   }

   /**
    * Annule les modifications effectuées lors de l'édition d'un paramètre.
    *
    * @param parameter le paramètre à annuler
    */
   @Command
   @NotifyChange("parameterList")
   public void cancelEdit(@BindingParam("parameter") ParametreDTO parameter) {
      // Set edit mode to false to revert the changes
      // Revert to the original values
      ParametreDTO originalValues = originalValuesMap.get(parameter);
      parameter.setCode(originalValues.getCode());
      parameter.setValeur(originalValues.getValeur());

      // Set edit mode to false to cancel the editing
      editModeMap.put(parameter, false);


   }

   /**
    * Gère le téléchargement de fichiers dans le contexte des paramètres.
    *
    * @param parameter le paramètre associé au fichier
    * @param event     l'événement de téléchargement déclenché
    */
   @Command
   @NotifyChange({"parameterList"})
   public void handleFileUpload(@BindingParam("parameter") ParametreDTO parameter,
      @ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) {
      Media media = event.getMedia();

      if (isMediaTypeValide(media)) {
         // Save the file
         boolean isSaved = saveLogo(media);
         String filepath = media.getContentType();
         parameter.setValeur(filepath);
         // Update the image source
      }
   }


//***************************************** Utility Mehtods ******************************************************************

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
      }finally{
         if(null != tmpLogoFile && tmpLogoFile.exists()){
            tmpLogoFile.delete();
         }
      }
      return saved;
   }

}
