/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.webapp.general;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import fr.aphp.tumorotek.action.MainWindow;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.13.2
 *
 */
public class MainTabbox extends Tabbox
{

   private static final long serialVersionUID = 1L;

   /**
    * Méthode appelée lors de la création de la Tabbox : le tabpanel des
    * prlvts sera sélectionné par défaut.
    */
   public void onCreate(){
      initHeightAndWidth();

      // TODO à revoir Pas terrible d'initialiser le stockage panel à cause
      // du non affichage au premier clique du terminale
      // au niveau liste echantillon pour voir son emplacement
      final Tabpanel item = (Tabpanel) this.getFellow("stockagePanel");
      this.setSelectedPanel(item);
      getMainWindow().createMacroComponent("/zuls/stockage/Stockage.zul", "winStockages", item);

      initFirstTab();
   }

   /**
    * défini le tab qui sera ouvert à la connexion.
    * Dépend du profil de l'utilisateur.
    * @since 2.0.13.2
    */
   private void initFirstTab(){
      // prelevementPanel default
      String panelName = "prelevementPanel";

      // if not available
      // walks through other panels by order
      if(!getMainWindow().getAvailableTabsNames().contains("prelevementTab")){
         if(getMainWindow().getAvailableTabsNames().contains("patientTab")){
            panelName = "patientPanel";
         }else if(getMainWindow().getAvailableTabsNames().contains("echantillonTab")){
            panelName = "echantillonPanel";
         }else if(getMainWindow().getAvailableTabsNames().contains("deriveTab")){
            panelName = "derivePanel";
         }else if(getMainWindow().getAvailableTabsNames().contains("cessionTab")){
            panelName = "cessionPanel";
         }else if(getMainWindow().getAvailableTabsNames().contains("stockageTab")){
            panelName = "stockagePanel";
         }else if(getMainWindow().getAvailableTabsNames().contains("rechercheTab")){
            panelName = "recherchePanel";
         }
      }

      final Tabpanel item2 = (Tabpanel) this.getFellow(panelName);
      this.setSelectedPanel(item2);

      onSelect();
   }

   public void onSelect(){

      final Tabpanel item = getSelectedPanel();
      this.getDesktop().setBookmark(item.getId());

      switch(item.getId()){
         case "patientPanel":// sélection du panel PATIENT
            getMainWindow().createMacroComponent("/zuls/patient/Patient.zul", "winPatient", item);
            break;
         case "prelevementPanel":// sélection du panel PRELEVEMENT
            getMainWindow().createMacroComponent("/zuls/prelevement/Prelevement.zul", "winPrelevement", item);
            break;
         case "echantillonPanel":// sélection du panel ECHANTILLON
            getMainWindow().createMacroComponent("/zuls/echantillon/Echantillon.zul", "winEchantillon", item);
            break;
         case "derivePanel":// sélection du panel PRODDERIVE
            getMainWindow().createMacroComponent("/zuls/prodderive/ProdDerive.zul", "winProdDerive", item);
            break;
         case "cessionPanel":// sélection du panel CESSION
            getMainWindow().createMacroComponent("/zuls/cession/Cession.zul", "winCession", item);
            break;
         case "stockagePanel":// sélection du panel STOCKAGE
            getMainWindow().createMacroComponent("/zuls/stockage/Stockage.zul", "winStockages", item);
            break;
         case "recherchePanel":// sélection du panel RECHERCHE
            getMainWindow().createMacroComponent("/zuls/io/RechercheComplexe.zul", "winRechercheComplexe", item);
            break;
         case "administrationPanel":// sélection du panel ADMINISTRATION
            getMainWindow().createMacroComponent("/zuls/administration/Administration.zul", "winAdministration", item);
            break;
         case "accueilPanel":// sélection du panel ACCUEIL
            getMainWindow().createMacroComponent("/zuls/main/Accueil.zul", "winAccueil", item);
            break;
         case "statPanel":// sélection du panel ACCUEIL
            getMainWindow().createMacroComponent(
               //"/zuls/stats/stats.zul",
               "/zuls/stats/charts.zul", "winStats", item);
            break;
         default:
            break;
      }
   }

   public MainWindow getMainWindow(){
      return (MainWindow) this.getPage().getFellow("mainWin").getAttributeOrFellow("mainWin$composer", true);
   }

   public void onClientInfo(){
      final Tabpanel item = (Tabpanel) this.getFellow("prelevementPanel");
      item.setHeight(getMainWindow().getPanelHeight() + "px");
   }

   /**
    * Cette méthode initialise la taille de tous les tabpanels.
    */
   public void initHeightAndWidth(){
      // Panel Accueil
      final Tabpanel itemAccueil = (Tabpanel) this.getFellow("accueilPanel");
      itemAccueil.setHeight(getMainWindow().getTabPanelsHeight() + "px");
      // Panel patient
      final Tabpanel itemPatient = (Tabpanel) this.getFellow("patientPanel");
      itemPatient.setHeight(getMainWindow().getTabPanelsHeight() + "px");
      // Panel Prlvt
      final Tabpanel itemPrlvt = (Tabpanel) this.getFellow("prelevementPanel");
      itemPrlvt.setHeight(getMainWindow().getTabPanelsHeight() + "px");
      // Panel Echantillon
      final Tabpanel itemEchantillon = (Tabpanel) this.getFellow("echantillonPanel");
      itemEchantillon.setHeight(getMainWindow().getTabPanelsHeight() + "px");
      // Panel Derivé
      final Tabpanel itemDerive = (Tabpanel) this.getFellow("derivePanel");
      itemDerive.setHeight(getMainWindow().getTabPanelsHeight() + "px");
      // Panel Cession
      final Tabpanel itemCession = (Tabpanel) this.getFellow("cessionPanel");
      itemCession.setHeight(getMainWindow().getTabPanelsHeight() + "px");
      // Panel Stockage
      final Tabpanel itemStockage = (Tabpanel) this.getFellow("stockagePanel");
      itemStockage.setHeight(getMainWindow().getTabPanelsHeight() + "px");
      // Panel Recherche
      final Tabpanel itemRecherche = (Tabpanel) this.getFellow("recherchePanel");
      itemRecherche.setHeight(getMainWindow().getTabPanelsHeight() + "px");
      // Panel Administration
      final Tabpanel itemAdministration = (Tabpanel) this.getFellow("administrationPanel");
      itemAdministration.setHeight(getMainWindow().getTabPanelsHeight() + "px");
      // Panel Statistiques
      final Tabpanel itemStats = (Tabpanel) this.getFellow("statPanel");
      itemStats.setHeight(getMainWindow().getTabPanelsHeight() + "px");
   }

   public void createCessionMacroComponent(final Tabpanel item){
      if(!item.hasFellow("winCession")){
         final Component comp = Executions.createComponents("/zuls/cession/Cession.zul", item, null);
         comp.setId("winCession");
      }
   }
}
