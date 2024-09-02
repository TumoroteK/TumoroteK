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

import fr.aphp.tumorotek.dto.SelectableItemDTO;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SelectionModale {

    private String title;
    private String mainLabel;
    private String listHeaderLabel;
    private List<SelectableItemDTO> itemList;
    private int selectedCount = 0;  // This should be an integer
    private String selectedLabel;
    private Consumer<List<SelectableItemDTO>> callback;
    private int max;
    private String dangerStyle;




    @Init
    public void init(@ExecutionArgParam("title") String title,
                     @ExecutionArgParam("mainLabel") String mainLabel,
                     @ExecutionArgParam("listHeaderLabel") String listHeaderLabel,
                     @ExecutionArgParam("itemList") List<SelectableItemDTO> itemList,
                     @ExecutionArgParam("selectedLabel") String selectedLabel,
                     @ExecutionArgParam("max") int max,
                     @ExecutionArgParam("callback") Consumer<List<SelectableItemDTO>> callback) {
        this.title = title;
        this.mainLabel = mainLabel;
        this.listHeaderLabel = listHeaderLabel;
        this.itemList = itemList;
        this.selectedLabel = selectedLabel;
        this.callback = callback;
        this.max = max;
    }

    @Command
    @NotifyChange({"selectedCount"}) // Notify the UI to refresh selectedCount
    public void updateSelectedCount(@ContextParam(ContextType.COMPONENT) Component component) {
        // Get the Listbox component from the context
        Listbox listbox = (Listbox) Selectors.find(component, "#dynamicListbox").get(0);

        // Get the selected items from the Listbox
        Set<Listitem> selectedItems = listbox.getSelectedItems();

        // Update the selectedCount with the number of selected items
        selectedCount = selectedItems.size();


        // Update styles based on selected count
        if (selectedCount > max) {
            dangerStyle = "color: red;";
        }
    }

    @Command
    public void executeCallback(@ContextParam(ContextType.COMPONENT) Component component) {
        if (callback != null) {
            Listbox listbox = (Listbox) Selectors.find(component, "#dynamicListbox").get(0);
            List<SelectableItemDTO> selectedValues = listbox.getSelectedItems().stream()
                    .map(item -> (SelectableItemDTO) item.getValue())
                    .collect(Collectors.toList());
            callback.accept(selectedValues);
        }
        closeModal(component);
    }

    @Command
    public void closeModal(@ContextParam(ContextType.COMPONENT) Component component) {
        // Close the modal window
        Window window = (Window) Selectors.iterable(component, "#win").iterator().next();
        window.detach();
    }

    public String getTitle() {
        return title;
    }

    public String getMainLabel() {
        return mainLabel;
    }

    public String getListHeaderLabel() {
        return listHeaderLabel;
    }

    public List<SelectableItemDTO> getItemList() {
        return itemList;
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public String getSelectedLabel() {
        return selectedLabel;
    }

    public Consumer<List<SelectableItemDTO>> getCallback() {
        return callback;
    }

    public int getMax() {
        return max;
    }

    public String getDangerStyle() {
        return dangerStyle;
    }



}

