package fr.aphp.tumorotek.component;

import fr.aphp.tumorotek.action.prodderive.ProdDeriveRowRenderer;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.SimpleListModel;

import java.util.List;

/**
 * Cette classe est le contrôleur pour le composant TableauDerives.zul.
 * Elle permet d'afficher un tableau de dérivés
 *
 * Pour utiliser ce composant dans un fichier ZUL, ajoutez la déclaration suivante en haut du fichier ZUL :
 *
 * {@code <?component name="tableauDerives" macroURI="/zuls/component/TableauDerives.zul" inline="true"?>}
 *
 * Vous pouvez ensuite créer un élément et lui passer les attributs suivants :
 *
 * {@code
 * <tableauDerives derives=List<ProdDerive>
 *                  toutesCollections=boolean
 *                  renderer=ProdDeriveRowRenderer/>
 * }
 * L'attribut "toutesCollections" est utilisé pour afficher dynamiquement la colonne "Collection"
 * uniquement si l'utilisateur est en mode "Toutes collections".
 */
public class TableauDerives extends HtmlMacroComponent
{

   @Wire
   private Grid gridDerive;
   @Wire
   private Column toutesColsColumn;

   /**
    * Crée une nouvelle instance et compose le composant.
    */
   public TableauDerives(){

      compose();
   }

   /**
    * Affecter le model à la Grid
    *
    * @param derives La liste des dérivés à afficher
    */
   public void setDerives(List<ProdDerive> derives) {
         SimpleListModel<ProdDerive> derivesListModel = new SimpleListModel<>(derives);
         gridDerive.setModel(derivesListModel);
   }

   /**
    * Définit la visibilité de la colonne "Toutes collections" dans le tableau.
    *
    * @param toutCollections {@code true} pour afficher la colonne,
    *                        {@code false} pour la masquer (type : boolean).
    *
    */
   public void setToutesCollections(boolean toutCollections) {
      toutesColsColumn.setVisible(toutCollections);
   }

   /**
    * Affecter le rowRenderer à la Grid
    *
    * @param renderer ProdDeriveRowRenderer
    */
   public void setRenderer(ProdDeriveRowRenderer renderer) {
      gridDerive.setRowRenderer(renderer);
   }
}
