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
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Modale permettant d'informer l'utilisateur sur le déplacement d'un
 * prélèvement d'une collection à une autre, et de réaliser ce déplacement.
 * Date: 18/02/2011
 *
 * @author Mathieu BARTHELEMY, Julien HUSSON
 * @version 2.2.1
 *
 *          modifié le 21 Septembre 2012 par Julien HUSSON déplacement multiple
 *
 * @since 2.2.1 TK-254 contrainte de contexte afin que les banques disponibles pour
 * un changement de collection soient de même contexte
 *
 */
public class ChangeBanqueModale extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = 5284777010172649451L;

   private Group groupEchans;

   private Grid echantillonsGrid;

   private Group groupDerives;

   private Grid derivesGrid;

   private Label emplWarnLabel;

   private Listbox banquesBox;

   private Grid prlvtsGrid;

   private Prelevement[] prelevements;

   private final List<Echantillon> echans = new ArrayList<>();

   private final List<ProdDerive> derives = new ArrayList<>();

   private List<Banque> banques;

   private MainWindow main;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

   }

   public void init(final Prelevement[] prlvts, final MainWindow mw){
      prelevements = prlvts;
      main = mw;
      prlvtsGrid.setModel(new ListModelList<>(prelevements));
      prlvtsGrid.setRowRenderer(new MyGridRenderer());

      // recherche les échantillons et dérivés issus du prélèvement
      setObjectsFromPrelevement(prlvts[0]);
   }

   private void setObjectsFromPrelevement(final Prelevement p){
      // nettoie
      echans.clear();
      derives.clear();

      final Iterator<TKAnnotableObject> childIt =
         ManagerLocator.getPrelevementManager().getPrelevementChildrenManager(p).iterator();
      TKAnnotableObject next;
      while(childIt.hasNext()){
         next = childIt.next();
         if(next instanceof Echantillon){
            echans.add((Echantillon) next);
         }else{
            derives.add((ProdDerive) next);
         }
      }

      // Initialisation du nombre d'échantillons à afficher sur la page
      StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("fichePrelevement.group.echantillons"));
      sb.append(" (");
      sb.append(echans.size());
      sb.append(")");
      groupEchans.setLabel(sb.toString());
      echantillonsGrid.setModel(new ListModelList<>(echans));

      // Initialisation du nombre de dérivés à afficher sur la page
      sb = new StringBuffer();
      sb.append(Labels.getLabel("fichePrelevement.group.prodDerives"));
      sb.append(" (");
      sb.append(derives.size());
      sb.append(")");
      groupDerives.setLabel(sb.toString());
      derivesGrid.setModel(new ListModelList<>(derives));

      // affiche le warning sur les emplacements ou non
      emplWarnLabel.setVisible(displayEmplWarning());

      // banques
      banques = ManagerLocator.getBanqueManager().findBanqueForSwitchManager(p, SessionUtils.getLoggedUser(sessionScope));
      banquesBox.setModel(new SimpleListModel<>(banques));
      if(!banques.isEmpty()){
         banquesBox.setSelectedItem(banquesBox.getItemAtIndex(0));
      }

   }

   /**
    * Affiche ou non le warning sur les emplacements si un des objets est
    * stocké.
    *
    * @return true si au moins un des objets est stocké.
    */
   private boolean displayEmplWarning(){
      for(int i = 0; i < echans.size(); i++){
         if(echans.get(i).getEmplacement() != null){
            return true;
         }
      }
      for(int j = 0; j < derives.size(); j++){
         if(derives.get(j).getEmplacement() != null){
            return true;
         }
      }

      return false;
   }

   public void onClick$cancel(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$validate(){
      Clients.showBusy(Labels.getLabel("fichePrelevement.switchBanque.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public void onLaterUpdate(){
      Banque dest = null;
      if(banquesBox.getSelectedItem() != null){
         dest = (Banque) banquesBox.getSelectedItem().getValue();
      }else{
         // ferme wait message
         Clients.clearBusy();
         throw new WrongValueException(banquesBox, Labels.getLabel("fichePrelevement.switchBanque" + ".banquesCibles.error"));
      }

      try{
         ManagerLocator.getPrelevementManager().switchBanqueMultiplePrelevementManager(prelevements, dest, true,
            SessionUtils.getLoggedUser(sessionScope));

         // fermeture de la fenêtre
         Events.postEvent(new Event("onClose", self.getRoot()));

         // change de banque vers la banque de destination
         main.updateSelectedBanque(dest);

      }catch(final RuntimeException e){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(AbstractController.handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
         //throw new DoublonFoundException("Prelevement", "transaction");
      }

      // ferme wait message
      Clients.clearBusy();
   }

   public Prelevement[] getPrelevements(){
      return prelevements;
   }

   public void setPrelevements(final Prelevement[] prelevements){
      this.prelevements = prelevements;
   }

   /**
    * Forwarded Event. Récupère le prélèvement pour afficher ses
    * échantillons/dérivés associées
    *
    * @param e
    *            forwardé depuis le table code prel cliquable (event.getData
    *            contient l'objet Prelevement).
    */
   public void onClickPrel(final Event e){
      setObjectsFromPrelevement((Prelevement) e.getData());
   }

}

class MyGridRenderer implements RowRenderer<Prelevement>
{

   @Override
   public void render(final Row row, final Prelevement p, final int index) throws Exception{

      final Label codeLbl = new Label(p.getCode());
      codeLbl.addForward(null, codeLbl.getParent(), "onClickPrel", p);
      codeLbl.setParent(row);
      codeLbl.setClass("formLink");
      new Label(p.getNumeroLabo()).setParent(row);
      new Label(p.getNature() != null ? p.getNature().getNom() : "").setParent(row);
   }
}
