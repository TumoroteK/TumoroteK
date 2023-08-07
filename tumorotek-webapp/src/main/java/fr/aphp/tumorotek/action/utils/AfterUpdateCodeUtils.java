package fr.aphp.tumorotek.action.utils;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.prelevement.AfterUpdateCodeModale;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Timer;

import java.util.List;

public class AfterUpdateCodeUtils
{
   /**
    * Crée et initialise une fenêtre modale pour la mise à jour du code.
    * La fenêtre modale permet à l'utilisateur de choisir entre la mise à jour automatique, manuelle ou de ne pas mettre à jour les codes. La fenêtre de *mise à jour du code est initialement masquée et devient visible après un délai.
    * @param listEchantillons Une liste d'échantillons à mettre à jour.
    * @param listDerives  Un objet représentant le prélèvement associé aux échantillons.
    * @param oldPrefixe L'ancien préfixe à utiliser lors de la mise à jour du code.
    * @param newPrefixe Le nouveau préfixe a utilisé lors de la mise à jour du code.
    * @param page Page.
    * @param main MainWindow.
    */

   public static Window openUpdateCodeModale(final List<Echantillon> listEchantillons, List<ProdDerive> listDerives,
      final String oldPrefixe, final String newPrefixe, final Page page, final MainWindow main){
      // Crée une nouvelle fenêtre modale
      final Window win = new Window();
      // Définit les propriétés de la fenêtre modale
      win.setVisible(false);
      win.setId("showAfterUpdateCodeModaleWindow");
      win.setPage(page);
      win.setMaximizable(true);
      win.setSizable(true);
      win.setTitle(Labels.getLabel("general.edit"));
      win.setBorder("normal");
      win.setWidth("400px");
      win.setHeight("325px");
      win.setClosable(true);

      final HtmlMacroComponent macroComponent = initAfterUpdateCodeModale(listEchantillons, listDerives, oldPrefixe,
         newPrefixe,page, main,  win);
      macroComponent.setVisible(false);

      // Ajoute un écouteur d'événements à la fenêtre modale pour rendre les composants visibles après un délai
      win.addEventListener("onTimed", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            macroComponent.setVisible(true);
         }
      });
      // Crée et configure un minuteur pour contrôler la visibilité des composants, si non - la fenêtre est vide
      final Timer timer = new Timer();
      timer.setDelay(500);
      timer.setRepeats(false);
      timer.addForward("onTimer", timer.getParent(), "onTimed");
      win.appendChild(timer);
      timer.start();
      return win;
   }


   /**
    * Crée et initialise une fenêtre modale pour la mise à jour du code.
    * @param listEchantillons Une liste d'échantillons à mettre à jour.
    * @param listDerives /
    * @param oldPrefix L'ancien préfixe à utiliser lors de la mise à jour du code.
    *
    */

   public static HtmlMacroComponent initAfterUpdateCodeModale(final List<Echantillon> listEchantillons, List<ProdDerive> listDerives,
      final String oldPrefix, final String newPrefix, final Page page, final MainWindow mainWindow, final Window window){
      HtmlMacroComponent htmlMacroComponent;
      htmlMacroComponent = (HtmlMacroComponent) page.getComponentDefinition("afterUpdateCodeModale", false).newInstance(page, null);
      htmlMacroComponent.setParent(window);
      htmlMacroComponent.setId("openAfterUpdateCodeModale");
      htmlMacroComponent.applyProperties();
      htmlMacroComponent.afterCompose();

      ((AfterUpdateCodeModale) htmlMacroComponent.getFellow("fwinAfterUpdateCode").getAttributeOrFellow("fwinAfterUpdateCode$composer", true))
         .init(listEchantillons, listDerives, oldPrefix,  newPrefix, page, mainWindow);

      return htmlMacroComponent;
   }
}
