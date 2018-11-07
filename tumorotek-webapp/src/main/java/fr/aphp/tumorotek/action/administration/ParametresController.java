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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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
public class ParametresController extends AbstractController
{

   private static final long serialVersionUID = -2450763003941018231L;

   /** Formats image acceptés */
   private static final MediaType[] AUTHORIZED_IMAGE_MEDIA_TYPES =
      new MediaType[] {MediaType.IMAGE_GIF, MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG};

   //Composants ZK
   private Image accueilImg;
   private Label noImgLabel;
   private Html accueilHtml;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      super.doAfterCompose(comp);

      ParametresManager parametresManager = ManagerLocator.getManager(ParametresManager.class);
      
      updateLogo(parametresManager.getLogoFile());

      String accueilMsg = parametresManager.getMessageAccueil(false);

      if(StringUtils.isEmpty(accueilMsg)){
         accueilMsg = Labels.getLabel("params.message.empty");
      }

      accueilHtml.setContent(accueilMsg);

   }

   /**
    * Met à jour le composant contenant le logo, si le nouveau logo est null, remplace l'image par un label
    * @param newLogoFile nouveau logo
    * @throws IOException
    */
   private void updateLogo(final File newLogoFile){

      if(newLogoFile != null && newLogoFile.exists()){

         try{
            final AImage content = new AImage(newLogoFile);
            accueilImg.setContent(content);
            accueilImg.setVisible(true);
            noImgLabel.setVisible(false);
         }catch(final IOException e){
            log.error("Impossible de charger le fichier image [" + newLogoFile.getAbsolutePath() + "]", e);
            Messagebox.show(Labels.getLabel("error.params.image.update"), Labels.getLabel("general.error"), Messagebox.OK,
               Messagebox.ERROR);
         }

      }else{
         accueilImg.setVisible(false);
         noImgLabel.setVisible(true);
      }

   }

   /**
    * Méthode appelés lors du clic sur le bouton "Modifier" de l'image d'accueil
    */
   public void onClick$uploadImageAccueilBtn(){

      final Media uploadedMedia = Fileupload.get();
      final ParametresManager parametresManager = ManagerLocator.getManager(ParametresManager.class);

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
         }catch(IOException e){
            log.error("Erreur lors de la conversion du media en fichier", e);
         }
         finally {
            if(null != tmpLogoFile && tmpLogoFile.exists()) {
               tmpLogoFile.delete();
            }
         }
         
         if(saved) {
            updateLogo(parametresManager.getLogoFile());
         }
         else {
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

      final int confirmation = Messagebox.show("Confirmer la suppression de l'image d'accueil ?",
         "Suppression de l'image d'accueil", Messagebox.YES | Messagebox.NO, Messagebox.EXCLAMATION);

      if(confirmation == Messagebox.YES){

         boolean deleted = ManagerLocator.getManager(ParametresManager.class).deleteLogo();
         
         if(deleted){
            updateLogo(null);
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

      final int confirmation = Messagebox.show("Confirmer la suppression du message d'accueil ?",
         "Suppression du message d'accueil", Messagebox.YES | Messagebox.NO, Messagebox.EXCLAMATION);

      if(confirmation == Messagebox.YES){
         
         boolean deleted = ManagerLocator.getManager(ParametresManager.class).deleteMessageAccueil();
         
         if(deleted){
            accueilHtml.setContent(Labels.getLabel("params.message.empty"));
         }else{
            Messagebox.show(Labels.getLabel("error.params.message.update"), Labels.getLabel("general.error"), Messagebox.OK,
               Messagebox.ERROR);
         }
         
      }

   }

   /**
    * Stocke le nouveau message d'accueil dans le fichier tumorotek.properties
    * @param msgAccueil nouveau message d'accueil
    */
   private void storeNewMsgAccueil(final String msgAccueil){

      ParametresManager paramsMgr = ManagerLocator.getManager(ParametresManager.class);

      final boolean saved = paramsMgr.saveMessageAccueil(msgAccueil);

      if(saved){
         accueilHtml.setContent(paramsMgr.getMessageAccueil(false));
      }else{
         Messagebox.show(Labels.getLabel("error.params.message.update"), Labels.getLabel("general.error"), Messagebox.OK,
            Messagebox.ERROR);
      }

   }

}
