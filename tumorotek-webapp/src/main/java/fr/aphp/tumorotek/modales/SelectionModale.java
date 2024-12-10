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

package fr.aphp.tumorotek.modales;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.dto.SelectableItemDTO;

/**
 * Classe SelectionModale - Contrôleur pour la fenêtre modale de sélection d'éléments.
 * Elle est notamment utilisée par ListeStockages pour sélection les conteneurs pour lesquels afficher les plans
 *
 * Cette classe gère l'affichage et les interactions avec une fenêtre modale ZK,
 * permettant à l'utilisateur de sélectionner un ou plusieurs éléments d'une liste.
 *
 * Les annotations @Command et @NotifyChange sont spécifiques à ZK et lient les méthodes aux événements de l'interface utilisateur,
 * tandis que @Init est utilisée pour l'initialisation du ViewModel.
 */
public class SelectionModale
{

   private String title; // Titre de la fenêtre modale

   private String mainLabel; // Texte principal affiché en haut de la fenêtre modale

   private String listHeaderLabel; // Titre de l'en-tête de la liste des éléments sélectionnables

   private List<SelectableItemDTO> itemList; // Liste des éléments sélectionnables

   private int selectedCount = 0; // Nombre d'éléments actuellement sélectionnés

   private String selectedLabel; // Texte affiché pour indiquer le nombre d'éléments sélectionnés

   private Consumer<List<SelectableItemDTO>> callback; // Fonction de rappel à exécuter après la sélection

   private int max; // Nombre maximum d'éléments qui peuvent être sélectionnés

   private String invalideSelectionStyle = ""; // Style appliqué pour indiquer une sélection invalide. Vaut "" si la sélection est correcte

   private boolean boutonValidateDisabled = true;



   /**
    * Initialisation du ViewModel. Cette méthode est appelée automatiquement lors de la création de la fenêtre modale.
    * Elle configure la fenêtre avec les arguments passés lors de l'appel à createComponents.
    */
   @Init
   public void init(@ExecutionArgParam("title") String title, @ExecutionArgParam("mainLabel") String mainLabel,
      @ExecutionArgParam("listHeaderLabel") String listHeaderLabel,
      @ExecutionArgParam("itemList") List<SelectableItemDTO> itemList, @ExecutionArgParam("selectedLabel") String selectedLabel,
      @ExecutionArgParam("max") int max, @ExecutionArgParam("callback") Consumer<List<SelectableItemDTO>> callback){
      this.title = title;
      this.mainLabel = mainLabel;
      this.listHeaderLabel = listHeaderLabel;
      this.itemList = itemList;
      this.selectedLabel = selectedLabel;
      this.callback = callback;
      this.max = max;
   }

   /**
    *  Elle est appelée en réponse à un événement de l'interface utilisateur.
    * Elle met à jour le nombre d'éléments sélectionnés et notifie l'interface utilisateur pour rafraîchir l'affichage.
    */
   @Command
   @NotifyChange({"selectedCount", "invalideSelectionStyle", "boutonValidateDisabled"})
   public void updateSelectedCount(@ContextParam(ContextType.COMPONENT) Component component){
      // Récupération du composant Listbox depuis le contexte
      Listbox listbox = (Listbox) Selectors.find(component, "#dynamicListbox").get(0);

      // Récupération des éléments sélectionnés dans la Listbox
      Set<Listitem> selectedItems = listbox.getSelectedItems();

      // Mise à jour du nombre d'éléments sélectionnés
      selectedCount = selectedItems.size();

      // Mise à jour du style en cas de dépassement du nombre maximum d'éléments sélectionnables
      invalideSelectionStyle = (selectedCount > max ? "font-weight: bold;color: red;" : "");
      
      boutonValidateDisabled = (selectedCount == 0 || selectedCount > max);
   }

   /**
    * Appelée lorsque l'utilisateur confirme sa sélection.
    * Elle exécute la fonction de rappel avec les éléments sélectionnés et ferme la fenêtre modale.
    */
   @Command
   public void executeCallback(@ContextParam(ContextType.COMPONENT) Component component){
      if(callback != null){
         // Récupération de la Listbox et des éléments sélectionnés

         Listbox listbox = (Listbox) Selectors.find(component, "#dynamicListbox").get(0);
         List<SelectableItemDTO> selectedValues =
            listbox.getSelectedItems().stream().map(item -> (SelectableItemDTO) item.getValue()).collect(Collectors.toList());
         // Exécution de la fonction de rappel

         callback.accept(selectedValues);
      }
      // Fermeture de la fenêtre modale

      closeModal(component);
   }

   /**
    * Ferme la fenêtre modale.
    */
   @Command
   public void closeModal(@ContextParam(ContextType.COMPONENT) Component component){
      // Fermeture de la fenêtre modale
      Window window = (Window) Selectors.iterable(component, "#win").iterator().next();
      window.detach();
   }

   public String getTitle(){
      return title;
   }

   public String getMainLabel(){
      return mainLabel;
   }

   public String getListHeaderLabel(){
      return listHeaderLabel;
   }

   public List<SelectableItemDTO> getItemList(){
      return itemList;
   }

   public int getSelectedCount(){
      return selectedCount;
   }

   public String getSelectedLabel(){
      return selectedLabel;
   }

   public Consumer<List<SelectableItemDTO>> getCallback(){
      return callback;
   }

   public int getMax(){
      return max;
   }

   public String getInvalideSelectionStyle(){
      return invalideSelectionStyle;
   }
   
   public void setInvalideSelectionStyle(String invalideSelectionStyle){
      this.invalideSelectionStyle = invalideSelectionStyle;
   }
   
   public boolean isBoutonValidateDisabled(){
      return boutonValidateDisabled;
   }

   public void setBoutonValidateDisabled(boolean boutonValidateDisabled){
      this.boutonValidateDisabled = boutonValidateDisabled;
   }
}

