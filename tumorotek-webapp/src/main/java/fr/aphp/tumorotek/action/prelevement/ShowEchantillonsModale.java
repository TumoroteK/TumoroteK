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

import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Affiche une fenêtre demandant à l'utilisateur s'il souhaite
 * voir les échantillons d'un prélèvement après l'avoir modifié.
 * @author Pierre Ventadour.
 * Le 07/07/2011.
 *
 */
public class ShowEchantillonsModale extends GenericForwardComposer<Window>
{

   private static final long serialVersionUID = 3487687657729735018L;

   private List<Echantillon> echantillons = new ArrayList<>();

   private Prelevement prelevement;

   private String oldPrefixe;

   private MainWindow main;

   private Page pg;

   private Radio modifAuto;

   private Radio modifManuelle;

   public void init(final List<Echantillon> echans, final Prelevement prlvt, final String oPrefixe, final MainWindow mw,
      final Page p){
      this.echantillons = echans;
      this.main = mw;
      this.pg = p;
      this.prelevement = prlvt;
      this.oldPrefixe = oPrefixe;
   }

   public void onClick$cancel(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$validate(){
      if(modifAuto.isChecked()){
         Clients.clearBusy();
         Events.echoEvent("onLaterUpdate", self, null);
      }else if(modifManuelle.isChecked()){
         // on passe sur l'onglet échantillons
         final EchantillonController tabController = (EchantillonController) EchantillonController.backToMe(main, pg);
         // on remplit la liste des échantillons
         tabController.getListe().setListObjects(echantillons);
         tabController.getListe().setCurrentObject(null);
         tabController.clearStaticFiche();
         tabController.getListe().getBinder().loadAttribute(tabController.getListe().getObjectsListGrid(), "model");
         tabController.switchToOnlyListeMode();

         // fermeture de la fenêtre
         Events.postEvent(new Event("onClose", self.getRoot()));
      }else{
         // fermeture de la fenêtre
         Events.postEvent(new Event("onClose", self.getRoot()));
      }
   }

   public void onLaterUpdate(){
      // Maj des échantillons
      final List<Echantillon> resultats = ManagerLocator.getEchantillonManager().updateCodeEchantillonsManager(echantillons,
         oldPrefixe, prelevement.getCode(), SessionUtils.getLoggedUser(sessionScope));

      // ferme wait message
      Clients.clearBusy();

      // on passe sur l'onglet échantillons
      final EchantillonController tabController = (EchantillonController) EchantillonController.backToMe(main, pg);
      // on remplit la liste des échantillons
      tabController.getListe().setListObjects(resultats);
      tabController.getListe().setCurrentObject(null);
      tabController.clearStaticFiche();
      tabController.getListe().getBinder().loadAttribute(tabController.getListe().getObjectsListGrid(), "model");
      tabController.switchToOnlyListeMode();

      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public List<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final List<Echantillon> e){
      this.echantillons = e;
   }

   public Prelevement getPrelevement(){
      return prelevement;
   }

   public void setPrelevement(final Prelevement p){
      this.prelevement = p;
   }

   public String getOldPrefixe(){
      return oldPrefixe;
   }

   public void setOldPrefixe(final String o){
      this.oldPrefixe = o;
   }

}
