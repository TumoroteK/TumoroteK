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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.TKConstants;
import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.cession.ListeCession;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.echantillon.ListeEchantillon;
import fr.aphp.tumorotek.action.prodderive.ListeProdDerive;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.MultipleDoublonFoundException;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Affiche une fenêtre demandant à l'utilisateur s'il souhaite
 * voir les échantillons d'un prélèvement après l'avoir modifié.
 * @author Pierre Ventadour.
 * Le 07/07/2011.
 *
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

   //permet de faire un callback sur ce composant
   private String openerPath;

   private Radio modifAuto;

   private Radio modifManuelle;

   private Html message;

   private Label title;

   private Label manuelLabel;

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
      final String newPrefixe, final Page page, final MainWindow main, final String openerPath){
      // Initialise l'objet avec les paramètres fournis
      this.echantillons = listEchantillons;
      this.oldPrefixe = oldPrefixe;
      this.derives = listDerives;
      this.main = main;
      this.pg = page;
      this.openerPath = openerPath;
      this.newPrefixe = newPrefixe;
      // Vérifie si la liste d'objets Echantillon est nulle ou vide pour adapter les messages aux dérivés
      if(echantillons == null || echantillons.isEmpty()){
         String titre = Labels.getLabel("message.modification.code.echantillon");
         title.setValue(titre);
         String question = Labels.getLabel("message.modification.code.derives");
         message.setContent(question);
         String manual = Labels.getLabel("message.modification.code.derives.manuel");
         manuelLabel.setValue(manual);
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
      // Si la modification automatique est cochée, effectue une mise à jour et arrête l'affichage du logo "chargement en cours" .
      if(modifAuto.isChecked()){
         Events.echoEvent("onLaterUpdate", self, null);
      }else if(modifManuelle.isChecked()){
         // Si la modification manuelle est cochée :
         // - si la liste des échantillons contient des éléments, passe sur l'onglet "Échantillons" pour afficher ceux-ci
         // - sinon passe sur l'onglet "Dérivés" pour afficher les produits dérivés.
         if(echantillons != null){
            showEchantillons();
         }else{
            showDerives();
         }
         closeModale();
      }
      else{
         // Si ne pas modifier est coché
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
      final EchantillonController echantillonController = (EchantillonController) EchantillonController.backToMe(main, pg);

      echantillonController.getListe().setListObjects(echantillons);
      echantillonController.getListe().setCurrentObject(null);
      echantillonController.clearStaticFiche();

      echantillonController.getListe().getBinder().loadAttribute(echantillonController.getListe().getObjectsListGrid(), "model");

      echantillonController.switchToOnlyListeMode();
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
      closeModale();
      
      Map<String, List<Echantillon>> mapUpdatedAndDoublon = null;
      List<Echantillon> listEchantillonUpdated = new ArrayList<Echantillon>();
      List<ProdDerive> listDeriveFromUpdatedEchantillons = new ArrayList<ProdDerive>();
      List<ProdDerive> listAllUpdatedDerive = new ArrayList<ProdDerive>();
      //une DoublonFoundException peut être envoyée lors de la mise à jour des codes échantillon et/ou lors de celle des codes produit dérivés
      List<DoublonFoundException> listDoublonFoundExceptions = new ArrayList<DoublonFoundException>();

      // Met à jour le code des échantillons et celui des éventuels dérivés issus des échantillons 
      // Si le code échantillon n'a pas été mis à jour à cause d'un doublon, aucun dérivé de cet échantillon ne sera mis à jour
      if(echantillons != null){
         mapUpdatedAndDoublon = ManagerLocator.getEchantillonManager().updateCodeEchantillonsManager(echantillons, oldPrefixe,
            newPrefixe, SessionUtils.getLoggedUser(sessionScope));
         if(mapUpdatedAndDoublon != null) {
            DoublonFoundException doublonFoundExceptionForEchantillon = new DoublonFoundException("Echantillon", "modification", new ArrayList<String>(), null);
            populateDoublonFoundExceptionIfNecessary(doublonFoundExceptionForEchantillon, mapUpdatedAndDoublon);
            if(!doublonFoundExceptionForEchantillon.getCodes().isEmpty()) {
               listDoublonFoundExceptions.add(doublonFoundExceptionForEchantillon);
            }
            //si des échantillons ont été mis à jour, récupération des dérivés de ces échantillons
            if(mapUpdatedAndDoublon.get(TKConstants.MAP_KEY_UPDATED) != null) {
               listEchantillonUpdated = mapUpdatedAndDoublon.get(TKConstants.MAP_KEY_UPDATED);
               listDeriveFromUpdatedEchantillons = getProduitsDeriveFromEchantillons(listEchantillonUpdated);
            }
         }
      }
      // Fusion des listes prodDerivesFromEchantillons et derives qui sont les dérivés issus du prélèvement
      List<ProdDerive> listMergedDerive = Stream.concat(listDeriveFromUpdatedEchantillons.stream(), derives.stream()).collect(Collectors.toList());

      if(!listMergedDerive.isEmpty()){
         // Mise à jour du code des produits dérives et des éventuels dérivés issus de ces dérivés
         Map<String, List<ProdDerive>> mapUpdateAndDoubonForDerive = changeDerivesCode(listMergedDerive);
         if(mapUpdateAndDoubonForDerive != null) {
            DoublonFoundException doublonFoundExceptionForDerive = new DoublonFoundException("ProdDerive", "modification", new ArrayList<String>(), null);
            populateDoublonFoundExceptionIfNecessary(doublonFoundExceptionForDerive, mapUpdateAndDoubonForDerive);
            if(mapUpdateAndDoubonForDerive.get(TKConstants.MAP_KEY_UPDATED) != null) {
               List<ProdDerive> listDeriveUpdatedForFirstIteration = mapUpdateAndDoubonForDerive.get(TKConstants.MAP_KEY_UPDATED);
               listAllUpdatedDerive.addAll(listDeriveUpdatedForFirstIteration);
               //recherche des éventuels dérivés des dérivés mis à jour
               List<ProdDerive> listDeriveFromDerive = getProduitsDeriveFromProduitsDerive(listDeriveUpdatedForFirstIteration);
               //mise à jour des dérivés de dérivés
               Map<String, List<ProdDerive>> mapUpdateAndDoubonForDeriveFromDerive = changeDerivesCode(listDeriveFromDerive);
               populateDoublonFoundExceptionIfNecessary(doublonFoundExceptionForDerive, mapUpdateAndDoubonForDeriveFromDerive);
               if(mapUpdateAndDoubonForDeriveFromDerive != null) {
                  listAllUpdatedDerive.addAll(mapUpdateAndDoubonForDeriveFromDerive.get(TKConstants.MAP_KEY_UPDATED));
               }
            }
            if(!doublonFoundExceptionForDerive.getCodes().isEmpty()) {
               listDoublonFoundExceptions.add(doublonFoundExceptionForDerive);
            }
         }
      }
      
      if(!listDoublonFoundExceptions.isEmpty()) {
         final HashMap<String, Object> map = new HashMap<>();
         map.put("title", Labels.getLabel("error.modification.automatique.doublon"));
         map.put("exception", new MultipleDoublonFoundException(listDoublonFoundExceptions));

         final Window window =
            (Window) Executions.createComponents("/zuls/component/DynamicMultiLineMessageBox.zul", null, map);
         window.doModal();
      }

      refreshUIComponents(listEchantillonUpdated, listAllUpdatedDerive);

      Clients.clearBusy();
   }

   private void refreshUIComponents(List<Echantillon> listEchantillonUpdated, List<ProdDerive> listAllUpdatedDerive){
      //Rafraîchissement dans les listes, de tous les échantillons et dérivés mis à jour ainsi que des cessions rattachées à ces échantillons / produits dérivés
      List<Integer> echantillonIdsToRefreshInUI = null;
      List<Integer> deriveIdsToRefreshInUI = new ArrayList<Integer>();
      List<Integer> cessionIdsToRefreshInUI = new ArrayList<Integer>();

      if(listEchantillonUpdated != null && !listEchantillonUpdated.isEmpty()){
         echantillonIdsToRefreshInUI =
            listEchantillonUpdated.stream().map(Echantillon::getEchantillonId).collect(Collectors.toList());
      }
      if(listAllUpdatedDerive != null && !listAllUpdatedDerive.isEmpty()){
         deriveIdsToRefreshInUI = listAllUpdatedDerive.stream().map(ProdDerive::getProdDeriveId).collect(Collectors.toList());
      }
      final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
      final Entite deriveEntite = ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0);
      final Entite cessionEntite = ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0);

      cessionIdsToRefreshInUI = ManagerLocator.getCorrespondanceIdManager()
         .findTargetIdsFromIdsManager(echantillonIdsToRefreshInUI, echanEntite, cessionEntite, null, false);
      cessionIdsToRefreshInUI.addAll(ManagerLocator.getCorrespondanceIdManager()
         .findTargetIdsFromIdsManager(deriveIdsToRefreshInUI, deriveEntite, cessionEntite, null, false));

      if(getEchantillonController() != null && echantillonIdsToRefreshInUI != null && !echantillonIdsToRefreshInUI.isEmpty()){
         ListeEchantillon listeEchantillonComponent = getEchantillonController().getListe();
         if(listeEchantillonComponent != null){
            listeEchantillonComponent.updateGridByIds(echantillonIdsToRefreshInUI, false, true);
         }
      }
      if(getProdDeriveController() != null && deriveIdsToRefreshInUI != null && !deriveIdsToRefreshInUI.isEmpty()){
         ListeProdDerive listeProdDeriveComponent = getProdDeriveController().getListe();
         if(listeProdDeriveComponent != null){
            listeProdDeriveComponent.updateGridByIds(deriveIdsToRefreshInUI, false, true);
         }
      }
      if(getCessionController() != null && cessionIdsToRefreshInUI != null && !cessionIdsToRefreshInUI.isEmpty()){
         ListeCession listeCessionComponent = getCessionController().getListe();
         if(listeCessionComponent != null){
            listeCessionComponent.updateGridByIds(cessionIdsToRefreshInUI, false, true);
         }
      }

      //refresh de la fiche du composant dont les codes des enfants ont été mis à jour en cascade 
      Component openerWindow = Path.getComponent(openerPath);
      AbstractObjectTabController openerController = (AbstractObjectTabController) openerWindow.getAttribute("$composer");
      if(openerController != null){
         openerController.getFicheStatic().reloadObject();
      }
   }
   
   private <E> void populateDoublonFoundExceptionIfNecessary (DoublonFoundException doublonFoundException, Map<String, List<E>> mapUpdateResult) {
      if(mapUpdateResult != null && mapUpdateResult.get(TKConstants.MAP_KEY_DOUBLON) != null && mapUpdateResult.get(TKConstants.MAP_KEY_DOUBLON).size() > 0) {
         doublonFoundException.getCodes().addAll(mapUpdateResult.get(TKConstants.MAP_KEY_DOUBLON).stream().map( s -> ((TKStockableObject)s).getCode()).collect(Collectors.toList()));            
      }
   }

   /**
    * Fermeture de la fenêtre
    */
   private void closeModale(){
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Modifie le préfixe des codes pour les produits dérivés si aucun produit dérivé n'existe avec le même code .
    * @return une map des produits dérivés mis à jour et ceux non mis à jour à cause d'un doublon
    */
   private Map<String, List<ProdDerive>> changeDerivesCode(List<ProdDerive> produitsDerives) {
      // Vérifie si des "produits dérivés" sont associés à l'échantillon
      if(produitsDerives != null && !produitsDerives.isEmpty()){
         // Met à jour les codes des produits dérivés
         return ManagerLocator.getProdDeriveManager().updateCodeDerivesManager(produitsDerives, oldPrefixe, newPrefixe,
            SessionUtils.getLoggedUser(sessionScope));
         
      }
      return null;
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
      if(listEchantillons != null) {
         for(Echantillon echantillon : listEchantillons){
            List<ProdDerive> produitsDerives = ManagerLocator.getEchantillonManager().getProdDerivesManager(echantillon);
            allProduitsDerives.addAll(produitsDerives);
         }
      }
         
      return allProduitsDerives;

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
   private List<ProdDerive> getProduitsDeriveFromProduitsDerive(List<ProdDerive> listProduitsDerives){
      List<ProdDerive> allProduitsDerives = new ArrayList<>();
      for(ProdDerive prodDerive : listProduitsDerives){
         List<ProdDerive> produitsDerives = ManagerLocator.getProdDeriveManager().getProdDerivesManager(prodDerive);
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