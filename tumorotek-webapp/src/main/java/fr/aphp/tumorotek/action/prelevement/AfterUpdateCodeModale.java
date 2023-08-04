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
package fr.aphp.tumorotek.action.prelevement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;
import org.zkoss.zul.Html;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Cette classe gère une fenêtre modale permettant la mise à jour du code des échantillons et produits dérivés
 * après une modification du préfixe de code.
 *
 * La fenêtre modale offre trois options à l'utilisateur :
 * - Mettre à jour automatiquement les échantillons et produits dérivés.
 * - Mettre à jour manuellement les échantillons et produits dérivés.
 * - Fermer simplement la fenêtre sans effectuer de mise à jour.
 *
 * Cette classe étend GenericForwardComposer<Window> pour gérer les événements de la fenêtre modale.
 */
public class AfterUpdateCodeModale extends AbstractController
{

   private static final long serialVersionUID = 3487687657729735018L;

   private List<Echantillon> echantillons = new ArrayList<>();

   private List<ProdDerive> derives = new ArrayList<>();

   private String oldPrefixe;

   private String newPrefixe;

   private MainWindow main;

   private Page pg;

   private String path;

   private Radio modifAuto;

   private Radio modifManuelle;

   private Html message;

   private Label manuelleLabel;

   private Label noModifLabel;

   /**
    * Initialise l'objet avec les paramètres donnés et effectue les actions nécessaires en fonction des données fournies.
    *
    * @param listEchantillons La liste des objets Echantillon à traiter.
    * @param listDerives La liste des objets ProdDerive à traiter.
    * @param oldPrefixe L'ancien préfixe à remplacer.
    * @param newPrefixe Le nouveau préfixe pour remplacer l'ancien.
    * @param page L'objet Page associé à l'opération.
    * @param main L'objet MainWindow associé à l'opération.
    * @param path Le chemin utilisé pour l'initialisation.
    */
   public void init(final List<Echantillon> listEchantillons, List<ProdDerive> listDerives, final String oldPrefixe,
      final String newPrefixe, final Page page, final MainWindow main, final String path){
      // Initialise l'objet avec les paramètres fournis
      this.echantillons = listEchantillons;
      this.oldPrefixe = oldPrefixe;
      this.derives = listDerives;
      this.path = path;
      this.main = main;
      this.pg = page;
      this.newPrefixe = newPrefixe;
      // Vérifie si la liste d'objets Echantillon est nulle ou vide
      if(echantillons == null || echantillons.isEmpty()){

         String question = Labels.getLabel("message.modification.code.derives");
         message.setContent(question);
         String manual = Labels.getLabel("message.modification.code.derives.manuel");
         manuelleLabel.setValue(manual);
         String noModif = Labels.getLabel("message.modification.code.derives.aucun");
         noModifLabel.setValue(noModif);
      }

   }


    /**
    * Gère l'événement de clic sur le bouton "Valider".
    * Si la case "Modification automatique" est cochée, effectue une mise à jour automatique.
    * Si la case "Modification manuelle" est cochée, passe sur l'onglet "Échantillons" de la page de prélevement.
    * Sinon, ferme simplement la fenêtre.
    *
    * Cette méthode prend en charge les événements "onClick" du bouton de validation.
    */
   public void onClick$validate(){

      // Si la modification automatique est cochée, arrête l'affichage du chargement et effectue une mise à jour.
      if(modifAuto.isChecked()){
         String waitingLabel = Labels.getLabel("general.display.wait");
         Clients.showBusy(waitingLabel);
         Events.echoEvent("onLaterUpdate", self, null);

         // Si la modification manuelle est cochée, passe sur l'onglet "Échantillons" de la page de prélevement.
      }else if(modifManuelle.isChecked()){
         gotToFirstChilds();
      }

      // Si ne pas modifier est coché
      else{
         closeModale();
      }
      Clients.clearBusy();
   }

   /**
    * Cette méthode permet de naviguer vers les enfants du premier niveau de l'élément courant.
    * Si des échantillons existent, elle affiche les échantillons et ferme la fenêtre modale.
    * Si aucun échantillon n'existe, elle affiche les dérivés et ferme la fenêtre modale.
    */
   private void gotToFirstChilds(){
      if(echantillons != null){
         showEchantillons();
         // Fermeture de la fenêtre modale.
         closeModale();
         // Si la modification manuelle est cochée, passe sur l'onglet "Dérivé" de la page de prélevement.
      }else{
         showDerives();
         // Fermeture de la fenêtre modale.
         closeModale();
      }
   }

   /**
    * Cette méthode permet de passer à l'onglet "Derive" (produits dérivés).
    * Elle configure le contrôleur "ProdDeriveController" pour afficher les produits dérivés.
    * Récupérer une instance du contrôleur "ProdDeriveController" à partir de la méthode statique "backToMe(main, pg)".
    */
   private void showDerives(){
      // Récupérer une instance du contrôleur "ProdDeriveController" à partir de la méthode statique "backToMe(main, pg)".
      final ProdDeriveController prodDeriveController = (ProdDeriveController) ProdDeriveController.backToMe(main, pg);
      // Définir la liste d'objets avec le contenu de la variable "derives".
      prodDeriveController.getListe().setListObjects(derives);
      // Désélectionner tout objet actuellement sélectionné dans la liste.
      prodDeriveController.getListe().setCurrentObject(null);
      // Détruit la fiche edition et rend visible la fiche statique
      prodDeriveController.clearStaticFiche();
      // Charger l'attribut "model" dans la grille "Grid" associée au contrôleur "ProdDeriveController".
      prodDeriveController.getListe().getBinder().loadAttribute(prodDeriveController.getListe().getObjectsListGrid(), "model");
      // Passer en mode "Liste uniquement" en affichant uniquement la liste des objets sans les détails individuels.
      prodDeriveController.switchToOnlyListeMode();
   }

   /**
    * Cette méthode permet de naviguer vers l'onglet "Échantillons"
    * Elle obtient le contrôleur de l'onglet prélevement et revient à cet onglet s'il existe.
    * Ensuite, elle remplit la liste des échantillons avec la liste fournie et met à jour l'affichage.
    */
   private void showEchantillons(){
      // Voir les commentaires à l'intérieur de la méthode showDerives
      final EchantillonController prelevementController = (EchantillonController) EchantillonController.backToMe(main, pg);

      prelevementController.getListe().setListObjects(echantillons);
      prelevementController.getListe().setCurrentObject(null);
      prelevementController.clearStaticFiche();

      prelevementController.getListe().getBinder().loadAttribute(prelevementController.getListe().getObjectsListGrid(), "model");

      prelevementController.switchToOnlyListeMode();
   }

   /**
    * Si l'utilisateur a choisi de mettre à jour automatiquement les codes en cascade, cette méthode est appelée pour mettre à jour
    * les codes des échantillons et de leurs produits dérivés && dérivés. Elle met à jour les codes des échantillons.
    * Ensuite, elle fusionne les produits dérivés obtenus à partir de ces échantillons avec les produits dérivés déjà présents
    * dans la liste 'derives'.
    * Enfin, elle appelle la méthode 'changeDerivesCode' pour mettre à jour les codes des produits dérivés avec la liste fusionnée.
    * La méthode se termine en fermant le message d'attente (si présent) et en fermant la fenêtre modale.
    */
   public void onLaterUpdate(){
      List<ProdDerive> prodDerivesFromEchantillons = new ArrayList<>();
      // Mettre à jour le code des échantillons
      if(echantillons != null){
         final List<Echantillon> listEchantillonUpdated = ManagerLocator.getEchantillonManager()
            .updateCodeEchantillonsManager(echantillons, oldPrefixe, newPrefixe, SessionUtils.getLoggedUser(sessionScope));
         prodDerivesFromEchantillons = getProduitsDeriveFromEchantillons(listEchantillonUpdated);
         updateEchantillonList(listEchantillonUpdated);
      }
      // Fusionner les listes prodDerivesFromEchantillons et derives
      List<ProdDerive> mergedDerivesList =  Stream.concat(prodDerivesFromEchantillons.stream(),
                                                          derives.stream()).collect(Collectors.toList());
      // Mettre à jour le code des produits dérives
      if(!mergedDerivesList.isEmpty()){
         changeDerivesCode(mergedDerivesList);

         updateDerivesList(mergedDerivesList);
      }
      updateAllControlers();

      closeModale();
   }

   /**
    * Cette méthode permet de mettre à jour la liste des échantillons
    * en utilisant les IDs des échantillons mis à jour.
    *
    * @param listEchantillon La liste des échantillons mis à jour.
    */
   private void updateEchantillonList( List<Echantillon> listEchantillon){
      List<Integer> idsList = listEchantillon.stream()
         .map(Echantillon::getEchantillonId)
         .collect(Collectors.toList());
      try {
         getEchantillonController().getListe().updateGridByIds(idsList, false, false);

      } catch (NullPointerException ex){

      }
   }

   /**
    * Cette méthode permet de mettre à jour la liste des produits dérivés
    * en utilisant les IDs des produits dérivés mis à jour.
    *
    * @param prodDeriveList La liste des produits dérivés mis à jour.
    */
   private void updateDerivesList(List<ProdDerive> prodDeriveList){
      List<Integer> idsList = prodDeriveList.stream()
         .map(ProdDerive::getProdDeriveId)
         .collect(Collectors.toList());
      if (getProdDeriveController().getListe() != null){
         getProdDeriveController().getListe().updateGridByIds(idsList, false, false);
      }
   }

   /**
    * Met à jour les contrôleurs EchantillonController, ProdDeriveController et PrelevementController en appelant
    * leurs méthodes 'updateRelatedUI' pour rafraîchir leurs interfaces utilisateur respectives.
    * Cette méthode traite les exceptions qui peuvent survenir lors de l'appel de ces méthodes.
    */
   private void updateAllControlers(){
      try {
         // Try the first action
         getProdDeriveController().getFicheEdit().updateRelatedUI();
      } catch (Exception e1) {
         // Log or handle the exception from the first action if needed
         // If an exception occurred, move to the second action
         try {
            getEchantillonController().getFicheEdit().updateRelatedUI();
         } catch (Exception e2 ) {
            // Log or handle the exception from the second action if needed
            // If an exception occurred, move to the third action
            try {
               getPrelevementController().getFicheEdit().updateRelatedUI();
            } catch (Exception e3) {
               // Log or handle the exception from the third action if needed
               // If you need to take additional actions if all three actions fail, you can do it here
            }
         }
      }
   }

   /**
    * Fermeture de la fenêtre
    */
   private void closeModale(){
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Modifie le préfixe des codes pour les produits dérivé.
    */
   private void changeDerivesCode(List<ProdDerive> produitsDerives){
      // Vérifie si des "produits dérivés" sont associés à l'échantillon
      if(produitsDerives != null && !produitsDerives.isEmpty()){
         // Met à jour les codes des produits dérivés
         ManagerLocator.getProdDeriveManager()
            .updateCodeDerivesManager(produitsDerives, oldPrefixe, newPrefixe, SessionUtils.getLoggedUser(sessionScope));
      }
   }

   /**
    * Récupère tous les ProdDerive à partir des Echantillons spécifiés.
    *
    * Cette méthode récupère les ProdDerive associés à chaque Echantillon
    * de la liste donnée en entrée et les fusionne tous dans une seule liste.
    * Le résultat est une liste de tous les ProdDerive obtenus à partir des Echantillons  fournis.
    * @param listEchantillons La liste des Echantillons pour lesquels récupérer les ProdDerive.
    * @return Une liste de tous les ProdDerive obtenus à partir des Echantillons spécifiés.
    */
   private List<ProdDerive> getProduitsDeriveFromEchantillons(List<Echantillon> listEchantillons){
      List<ProdDerive> allProduitsDerives = new ArrayList<>();
      for(Echantillon echantillon : listEchantillons){
         List<ProdDerive> produitsDerives = ManagerLocator.getEchantillonManager().getProdDerivesManager(echantillon);
         allProduitsDerives.addAll(produitsDerives);
      }

      return allProduitsDerives;

   }

   /**
    * Fermeture de la fenêtre modale.
    */
   public void onClick$cancel(){
      // fermeture de la fenêtre
      closeModale();
   }


   public List<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final List<Echantillon> e){
      this.echantillons = e;
   }

   public String getOldPrefixe(){
      return oldPrefixe;
   }

   public void setOldPrefixe(final String o){
      this.oldPrefixe = o;
   }

   public List<ProdDerive> getDerives(){
      return derives;
   }
}
