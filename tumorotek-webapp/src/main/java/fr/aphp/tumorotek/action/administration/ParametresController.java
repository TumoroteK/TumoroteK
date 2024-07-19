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

import static org.apache.camel.util.StringHelper.sanitize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Fileupload;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.component.TexteModale;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.ParametreDecorator;
import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.manager.administration.ParametresManager;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.param.EParametreType;
import fr.aphp.tumorotek.param.EParametreValeurParDefaut;
import fr.aphp.tumorotek.utils.MessagesUtils;
import fr.aphp.tumorotek.utils.TKStringUtils;
import fr.aphp.tumorotek.webapp.general.ConnexionUtils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

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
   
   // Liste de parametreDecorator représentant les paramètres de la plateforme avec les éléments nécessaires à la gestion de la modification.
   private List<ParametreDecorator> parametreDecorators = new ArrayList<>();
   
   // attribut alimenté quand l'utilisateur passe en mode édit sur un parametreDecorator.
   // cela bloque le fait de pouvoir éditer un autre parametreDecorator
   private ParametreDecorator parametreDecoratorInModeEdit = null;

   private ParametresManager parametresManager;

   private Plateforme plateforme;
   
   // Message de bienvenue affiché dans l'interface utilisateur.
   private String welcomeMessage;

   // Image de bienvenue affichée dans l'interface utilisateur.
   private AImage welcomeImage;

   // Titre de la plateforme
   private String plateformeTitle;

   // Indicateur indiquant si une image de bienvenue existe.
   private boolean welcomeImageExists;

   // Indicateur indiquant si un message de bienvenue spécifique a été défini par l'administrateur.
   private boolean specificWelcomeMessageExists;

   private String imageUpdateError = "error.params.image.update";

   //      **************************** | Mise en place |  ******************************************

   /**
    * Mise en place:
    * Charge la liste des paramètres de la plateforme, trie la liste par groupe puis par code,
    * initialise les maps de gestion du mode édition et des valeurs originales, init les variables.
    */
   @Init
   public void init(){
      // Initialise la plateforme
      plateforme = SessionUtils.getPlateforme(Sessions.getCurrent().getAttributes());
      // Initialise le gestionnaire des paramètres
      parametresManager = ManagerLocator.getManager(ParametresManager.class);
      
      // Initialise parametreDecorators
      List<ParametreDTO> parametreList = SessionUtils.getParametresPlateforme(Sessions.getCurrent().getAttributes());
      // Trie la liste des paramètres
      Collections.sort(parametreList, new ParametreComparator());
      parametreDecorators.clear();
      // Création des decorators :
      for(ParametreDTO parametre : parametreList){
         parametreDecorators.add(new ParametreDecorator(parametre));
      }
      
      initWelcomeMessage();
      initWelcomeImage();

      // Construit et initialise le titre de la plateforme
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
   @NotifyChange({"welcomeMessage", "specificWelcomeMessageExists"})
   public void reinitWelcomeMessage(){

      String messageHeader = Labels.getLabel("params.modale.welcomeMessage.reinitialisation.titre");
      String messageBody = Labels.getLabel("params.modale.welcomeMessage.reinitialisation.message");
 
      boolean isOk = MessagesUtils.openQuestionModal(messageHeader, messageBody);

      if(isOk){

         final boolean reinitialized = parametresManager.reinitMessageAccueil();

         if(reinitialized){
            welcomeMessage = Labels.getLabel("login.welcome");
            specificWelcomeMessageExists = false;
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
   @NotifyChange({"welcomeMessage", "specificWelcomeMessageExists"})
   public void modifyWelcomeMessage(){

      EventListener<Event> onCloseListener = event -> {
         if(null != event.getData()){
            String newMessage = (String) event.getData();
            boolean saved = parametresManager.saveMessageAccueil(newMessage);
            if(saved){
               welcomeMessage = newMessage;
               specificWelcomeMessageExists = true;
            }else{
               displayError(Labels.getLabel("error.params.message.update"));

            }
         }
      };

      TexteModale.show(Labels.getLabel("params.modale.welcomeMessage.titre"), Labels.getLabel("params.message.accueil"),
         ConnexionUtils.buildWelcomeMessageToDisplay(true), 5, true, onCloseListener);

   }

   //      **************************** | Event Listeners : Plateforme Params |  ******************************************
   /**
    * Commande pour réinitialiser un paramètre à sa valeur par défaut.
    * Cette méthode est appelée lorsqu'un utilisateur clique sur le bouton Réinitialiser.
    *
    * @param parametreDecorator Le parametreDecorator associé au paramètre à réinitialiser.
    */
   @Command
   @NotifyChange({"parametreDecorators"})
   public void reinitParametre(@BindingParam("parametreDecorator") ParametreDecorator parametreDecorator) {

      //gère le cas où un autre paramètre serait en mode édition
      manageMultiEdition();
      
      // Titre & Corps du message de la question pour la modale de confirmation
      String questionTitle = Labels.getLabel("params.modale.reinitialisation.titre");
      String questionBody = buildMessageConfirmationReinit(parametreDecorator);
      // Ouvre une modale de confirmation et obtient la réponse de l'utilisateur
      boolean isConfirmed = MessagesUtils.openQuestionModal(questionTitle, questionBody);
      if (isConfirmed) {
         // Supprime le paramètre de la base de données
         parametresManager.removeByPlateformeIdAndCodeManager(
            plateforme.getPlateformeId(), parametreDecorator.getCode());
         
         // Récupère et définit la valeur par défaut du paramètre
         String defaultValue = EParametreValeurParDefaut.getDefaultValeurByCode(parametreDecorator.getCode());
         parametreDecorator.getParametre().setValeur(defaultValue);
         parametreDecorator.setDefaultValeur(true);//permet de rafraichissement du bouton Réinitialiser
      }
   }
   
   /**
    * Active le mode édition pour un paramètre donné (objet parametreDecorator)
    *
    * @param parametreDecorator Le parametreDecorator associé au paramètre à éditer.
    */
   @Command
   @NotifyChange("parametreDecorators")
   public void editParametre(@BindingParam("parametreDecorator") ParametreDecorator parametreDecorator){
   
      //il n'est pas possible d'éditer 2 lignes en même temps => si une édition est en cours, on la gère :
      manageMultiEdition();
      enterEditMode(parametreDecorator);
   }

   /**
    * Annule les modifications effectuées lors de l'édition d'un paramètre.
    *
    * @param parametreDecorator Le parametreDecorator associé au paramètre à éditer.
    */
   @Command
   @NotifyChange("parametreDecorators")
   public void cancelEdit(@BindingParam("parametreDecorator") ParametreDecorator parametreDecorator){
      doCancelEdit(parametreDecorator);
   }
   
   /**
    * Commande pour enregistrer les modifications apportées à un paramètre.
    * Cette méthode est appelée lorsqu'un utilisateur clique sur le bouton de sauvegarde.
    *
    * @param parametreDecorator Le parametreDecorator associé au paramètre à éditer.
    */
   @Command
   @NotifyChange("parametreDecorators")
   public void saveParametre(@BindingParam("parametreDecorator") ParametreDecorator parametreDecorator){
      doSaveParametre(parametreDecorator);
   }


   // construit le message de confirmation dans la langue adéquate : ce message contient 
   //  - la valeur par défaut (Oui / Non internationalisé dans le cas d'un booléen)
   //  - le libellé internationalisé du champ
   private String buildMessageConfirmationReinit(ParametreDecorator parametreDecorator) {
      String defaultValue = EParametreValeurParDefaut.getDefaultValeurByCode(parametreDecorator.getCode());
      if(parametreDecorator.getType().equals(EParametreType.BOOLEAN.getType())) {
         defaultValue = Labels.getLabel(new StringBuilder("general.checkbox.").append(defaultValue).toString());
      }
      return ObjectTypesFormatters.getLabel("params.modale.reinitialisation.message",
         new String[] { defaultValue, parametreDecorator.getLibelleI18n() });
   }
   

   //construit le message à afficher quand une modification est en cours au moment où l'utilisateur clique sur Modifier.
   // ce message contient :
   //  - le libellé internationalisé du champ en cours de modification
   //  - la nouvelle valeur pour le champ en cours de saisie au moment du clic
   private String buildMessageModificationEnCours(ParametreDecorator parametreDecorator) {
      String newValeur = parametreDecorator.getNewValeur();
      if(parametreDecorator.getType().equals(EParametreType.BOOLEAN.getType())) {
         newValeur = Labels.getLabel(new StringBuilder("general.checkbox.").append(newValeur).toString());
      }
      return ObjectTypesFormatters.getLabel("params.modale.modification.encours.message",
         new String[] { parametreDecorator.getLibelleI18n(), newValeur });
   }
   
   private void doCancelEdit(ParametreDecorator parametreDecorator) {
      exitEditMode(parametreDecorator);
   }
   
   //gère le passage dans le mode édition 
   private void enterEditMode(ParametreDecorator parametreDecorator) {
      parametreDecorator.setEditMode(true);
      parametreDecoratorInModeEdit = parametreDecorator;
   }
   //gère la sortie du mode édition
   private void exitEditMode(ParametreDecorator parametreDecorator) {
      //réinitialisation du parametreDecorator
      //NB : le champ defaultValeur n'est pas géré ici car la réinitialisation n'est pas triviale.
      //     Par conséquent, elle n'est faite que lorsque la valeur à changer.
      parametreDecorator.setNewValeur(null);
      parametreDecorator.setEditMode(false);
      parametreDecoratorInModeEdit = null;
   }

   //gère le cas où l'utilisateur clique sur le bouton Modifier ou Réinitialiser d'un paramètre alors qu'un autre 
   //est en mode édition.
   private void manageMultiEdition() {
      if(parametreDecoratorInModeEdit != null) {
         //si l'utilisateur a modifié la valeur 
         if(parametreDecoratorInModeEdit.getNewValeur() != null) {
            boolean isConfirmed = MessagesUtils.openQuestionModal(
               Labels.getLabel("params.modale.modification.encours.titre"),
               buildMessageModificationEnCours(parametreDecoratorInModeEdit));

            if(isConfirmed) {
               doSaveParametre(parametreDecoratorInModeEdit);
            }
            else {
               doCancelEdit(parametreDecoratorInModeEdit);
            }
         }
         else {
            exitEditMode(parametreDecoratorInModeEdit);
         }
      }
   }
   
   //méthode qui gère la sauvegarde d'une nouvelle valeur
   private void doSaveParametre(ParametreDecorator parametreDecorator) {
      //Vérification que la valeur a été modifiée avant de faire les actions : 
      //NB : newValeur n'est valorisé que si l'utilisateur a changé la valeur
      if(parametreDecorator.getNewValeur() != null) {
         // Vérifie si la valeur du paramètre est égale à sa valeur par défaut, 
         // si oui, suppression de la valeur spécifique
         // si non, sauvegarde de la nouvelle valeur
         boolean isDefaultValue = EParametreValeurParDefaut.isDefaultValue(parametreDecorator.getCode(), parametreDecorator.getNewValeur());
         if (isDefaultValue) {
            parametresManager.removeByPlateformeIdAndCodeManager(plateforme.getPlateformeId(), parametreDecorator.getCode());
         }
         else {
            // Sauvegarde le paramètre avec la nouvelle valeur
            // alimentation de la nouvelle valeur dans le champ parametre du parametreDecorator que si la sauvegarde
            // se passe bien. On passe donc par un clone :
            ParametreDTO parametreASauvegarder = parametreDecorator.getParametre().clone();
            parametreASauvegarder.setValeur(parametreDecorator.getNewValeur());
            parametresManager.createOrUpdateObject(plateforme.getPlateformeId(), parametreASauvegarder);
         }
         //reporte la nouvelle valeur dans l'objet Parametre du parametreDecorator
         parametreDecorator.populateParametre();
         // rafraichit la valeur defaultValue
         parametreDecorator.setDefaultValeur(isDefaultValue);
      }
      // dans tous les cas, on sort du mode édition pour ce paramètre
      exitEditMode(parametreDecorator);
   }
   
   
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
      welcomeMessage = parametresManager.getMessageAccueilSpecifique(false);

      if(TKStringUtils.isEmptyOrBlank(welcomeMessage)){
         welcomeMessage = Labels.getLabel("login.welcome");
         specificWelcomeMessageExists = false;
      } else {
         specificWelcomeMessageExists = true;
      }
   }

   /**
    * Construit la chaîne de titre de la plateforme pour l'affichage.
    *
    * @return La chaîne de titre de la plateforme.
    */
   private String buildPlateformString(){

      String plateformName = plateforme.getNom();

      String translatedTitle = Labels.getLabel("params.plateforme.title");

      return String.format("%s %s", translatedTitle, plateformName);

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

   public List<ParametreDecorator> getParametreDecorators(){
      return parametreDecorators;
   }

   public void setParametreDecorators(List<ParametreDecorator> parametreDecorators){
      this.parametreDecorators = parametreDecorators;
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

   public boolean isWelcomeImageExists(){
      return welcomeImageExists;
   }

   public void setWelcomeImageExists(boolean welcomeImageExists){
      this.welcomeImageExists = welcomeImageExists;
   }

   public boolean isSpecificWelcomeMessageExists(){
      return specificWelcomeMessageExists;
   }

   public void setSpecificWelcomeMessageExists(boolean specificWelcomeMessageExists){
      this.specificWelcomeMessageExists = specificWelcomeMessageExists;
   }
}
