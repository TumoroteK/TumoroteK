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

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.cession.ContratController;
import fr.aphp.tumorotek.action.code.CodesController;
import fr.aphp.tumorotek.action.contexte.BanqueController;
import fr.aphp.tumorotek.action.contexte.CollaborationsController;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.imports.ImportController;
import fr.aphp.tumorotek.action.impression.ImpressionController;
import fr.aphp.tumorotek.action.stockage.TerminaleTypeController;
import fr.aphp.tumorotek.action.thesaurus.ThesaurusController;
import fr.aphp.tumorotek.action.utilisateur.ProfilController;
import fr.aphp.tumorotek.action.utilisateur.UtilisateurController;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * @version 2.1
 */
public class AdministrationController extends AbstractObjectTabController
{

   private static final long serialVersionUID = -464747901091918362L;
   //private Borderlayout mainBorder;
   private Tabbox adminTabbox;
   // liste contenant les noms des tabs
   private final List<String> availableTabsNames = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      adminTabbox.setHeight(getMainWindow().getPanelHeight() + "px");

      initAvailableTabsNames();
   }

   @Override
   public void reset(){
      initAvailableTabsNames();

      // reset du contrat
      getContratController().getFicheCombine().drawActionsForContrats();
      getContratController().getFicheCombine().clearData();
      getContratController().getListe().initObjectsBox();

      // reset des collaborateurs
      getCollaborationsController().drawActionsForCollaborations();

      // reset des comptes
      getUtilisateurController().getFicheCombine().drawActionsForUtilisateur();
      getUtilisateurController().getFicheCombine().clearData();

      // reset des profils
      getProfilController().getFicheCombine().drawActionsForProfil();
      getProfilController().getFicheCombine().clearData();

      // reset de l'impression
      getImpressionController().getFicheCombine().drawActionsForTemplate();
      getImpressionController().getFicheCombine().clearData();
      getImpressionController().getListe().initObjectsBox();

      // reset des thésaurus
      getThesaurusController().getFicheCombine().drawActionsForThesaurus();
      getThesaurusController().getFicheCombine().clearData();

      // reset des terminaleType
      getTerminaleTypeController().getFicheCombine().drawActionsForFormat();
      getTerminaleTypeController().getFicheCombine().clearData();

      // reset de l'import
      getImportController().getListe().initObjectsBox();
      getImportController().getFicheCombine().drawActionsForImport();
      getImportController().getFicheCombine().clearData();

      // reset des codes
      getCodesController().clearData();
   }

   /**
    * Initialise les tabs qui sont disponibles pour l'utilisateur (en
    * fonction de ses droits).
    */
   public void initAvailableTabsNames(){

      availableTabsNames.clear();

      // Tab Collaborateur
      if(!getDroitOnAction("Collaborateur", "Consultation")){
         final Tab tab = (Tab) adminTabbox.getFellow("collaborationsTab");
         tab.setDisabled(true);
      }else{
         availableTabsNames.add("collaborationsTab");
      }

      // Tab Contrat
      if(!getDroitOnAction("Cession", "Consultation")){
         final Tab tab = (Tab) adminTabbox.getFellow("contratsTab");
         tab.setDisabled(true);
      }else{
         availableTabsNames.add("contratsTab");
      }

      // Tab Import
      if(!canCurrentUserImport() || SessionUtils.getSelectedBanques(sessionScope).size() != 1){
         final Tab tab = (Tab) adminTabbox.getFellow("importsTab");
         tab.setDisabled(true);
      }else{
         availableTabsNames.add("importsTab");
      }

      // Tab Impression
      if(!(Boolean) sessionScope.get("Admin") || SessionUtils.getSelectedBanques(sessionScope).isEmpty()){
         final Tab tab = (Tab) adminTabbox.getFellow("impressionTab");
         tab.setDisabled(true);
      }else{
         availableTabsNames.add("impressionTab");
      }

      // Tab Imprimantes
      if(!sessionScope.containsKey("AdminPF")){
         final Tab tabImp = (Tab) adminTabbox.getFellow("imprimantesTab");
         tabImp.setDisabled(true);
      }else{
         availableTabsNames.add("imprimantesTab");
      }

      // Tab Outils
      if(!sessionScope.containsKey("AdminPF")){
         final Tab tabImp = (Tab) adminTabbox.getFellow("outilsTab");
         tabImp.setDisabled(true);
      }else{
         availableTabsNames.add("outilsTab");
      }

      // Tab historique
      if(!(Boolean) sessionScope.get("Admin")){
         final Tab tabImp = (Tab) adminTabbox.getFellow("historiqueTab");
         tabImp.setDisabled(true);
      }else{
         availableTabsNames.add("historiqueTab");
      }
      /*Tab tab = (Tab) adminTabbox.getFellow("imprimantesTab");
      tab.setDisabled(true);*/

      // Tab Plateforme
      if(!SessionUtils.getLoggedUser(sessionScope).isSuperAdmin() && !sessionScope.containsKey("AdminPF")){
         final Tab tabPf = (Tab) adminTabbox.getFellow("plateformesTab");
         tabPf.setDisabled(true);
         tabPf.setVisible(false);
      }else{
         final Tab tabPf = (Tab) adminTabbox.getFellow("plateformesTab");
         tabPf.setVisible(true);
         availableTabsNames.add("plateformesTab");
      }

      if(!SessionUtils.getLoggedUser(sessionScope).isSuperAdmin() && !sessionScope.containsKey("AdminPF")){
         final Tab tabPf = (Tab) adminTabbox.getFellow("parametresTab");
         tabPf.setDisabled(true);
         tabPf.setVisible(false);
      }else{
         final Tab tabPf = (Tab) adminTabbox.getFellow("parametresTab");
         tabPf.setVisible(true);
         availableTabsNames.add("parametresTab");
      }
      
      availableTabsNames.add("annotationsTab");
      availableTabsNames.add("banquesTab");
      availableTabsNames.add("utilisateursTab");
      availableTabsNames.add("profilsTab");
      //availableTabsNames.add("impressionTab");
      availableTabsNames.add("thesaurusTab");
      availableTabsNames.add("terminaleTypeTab");
      availableTabsNames.add("transporteursTab");
      availableTabsNames.add("reportsTab");
      availableTabsNames.add("numerotationTab");
      availableTabsNames.add("codesTab");

      unblockAllPanels();

   }

   private boolean canCurrentUserImport(){
      if((Boolean) sessionScope.get("Admin")){
         return true;
      }

      final List<ProfilUtilisateur> profs = ManagerLocator.getProfilUtilisateurManager()
         .findByUtilisateurBanqueManager(SessionUtils.getLoggedUser(sessionScope), SessionUtils.getCurrentBanque(sessionScope));

      if(!profs.isEmpty()){
         return hasAllCreationRights(profs.get(0).getProfil());
      }
      return false;
   }

   /**
     * Méthode qui rend actif tous les Tabs.
     */
   public void unblockAllPanels(){
      for(int i = 0; i < availableTabsNames.size(); i++){
         final Tab tab = (Tab) adminTabbox.getFellow(availableTabsNames.get(i));
         tab.setDisabled(false);
      }
   }

   @Override
   public FicheAnnotation getFicheAnnotation(){
      return null;
   }

   @Override
   public AbstractFicheEditController getFicheEdit(){
      return null;
   }

   @Override
   public AbstractFicheModifMultiController getFicheModifMulti(){
      return null;
   }

   @Override
   public AbstractFicheStaticController getFicheStatic(){
      return null;
   }

   @Override
   public AbstractListeController2 getListe(){
      return null;
   }

   @Override
   public AbstractFicheCombineController getFicheCombine(){
      return null;
   }

   public BanqueController getBanqueController(){
      return (BanqueController) self.getFellow("adminTabbox").getFellow("banquePanel").getFellow("banqueMacro")
         .getFellow("winBanque").getAttributeOrFellow("winBanque$composer", true);
   }

   /**
    * Méthode récupérant le controller du panel des protocolesExt.
    * @return ContratController classe gérant le panel 
    * des ProtocoleExt.
    */
   public ContratController getContratController(){

      return (ContratController) self.getFellow("adminTabbox").getFellow("contratPanel").getFellow("contratMacro")
         .getFellow("winContrat").getAttributeOrFellow("winContrat$composer", true);

   }

   /**
    * Méthode récupérant le controller du panel des collaborations.
    * @return ContratController classe gérant le panel 
    * des ProtocoleExt.
    */
   public CollaborationsController getCollaborationsController(){

      return (CollaborationsController) self.getFellow("adminTabbox").getFellow("collaborationsPanel")
         .getFellow("collaborationsMacro").getFellow("winCollaborations")
         .getAttributeOrFellow("winCollaborations$composer", true);

   }

   /**
    * Méthode récupérant le controller du panel des profils.
    * @return ContratController classe gérant le panel 
    * des ProtocoleExt.
    */
   public ProfilController getProfilController(){

      return (ProfilController) self.getFellow("adminTabbox").getFellow("profilsPanel").getFellow("profilsMacro")
         .getFellow("winProfil").getAttributeOrFellow("winProfil$composer", true);

   }

   /**
    * Méthode récupérant le controller du panel des utilisateurs.
    * @return ContratController classe gérant le panel 
    * des ProtocoleExt.
    */
   public UtilisateurController getUtilisateurController(){
      if(self.getFellow("adminTabbox").getFellow("utilisateursPanel").getFellowIfAny("utilisateursMacro") != null){
         return (UtilisateurController) self.getFellow("adminTabbox").getFellow("utilisateursPanel")
            .getFellow("utilisateursMacro").getFellow("winUtilisateur").getAttributeOrFellow("winUtilisateur$composer", true);
      }
      return (UtilisateurController) self.getFellow("adminTabbox").getFellow("utilisateursPanel").getFellow("winUtilisateur")
         .getAttributeOrFellow("winUtilisateur$composer", true);
   }

   /**
    * Méthode sélectionnant le controller du panel des utilisateurs
    * et qui sélectionne un utilisateur.
    */
   public void selectUtilisateurInController(final Utilisateur user){
      final UtilisateurController controller =
         (UtilisateurController) UtilisateurController.backToMe(getMainWindow(), adminTabbox);
      controller.selectUtilisateurInListe(user);
   }

   /**
    * Méthode sélectionnant le controller du panel des profils
    * et qui sélectionne un profil.
    * @since 2.1
    */
   public void selectProfilInController(final Profil profil){
      final ProfilController controller = (ProfilController) ProfilController.backToMe(getMainWindow(), adminTabbox);
      controller.selectProfilInListe(profil);
   }

   /**
    * Méthode sélectionnant le controller du panel des banques
    * et qui sélectionne une banques.
    */
   public void selectBanqueInController(final Banque banque){
      final BanqueController controller = (BanqueController) BanqueController.backToMe(getMainWindow(), adminTabbox);
      controller.selectBanqueInListe(banque);
   }

   /**
    * Méthode récupérant le controller du panel des impressions.
    * @return ImpressionController classe gérant le panel 
    * des Impressions.
    */
   public ImpressionController getImpressionController(){

      return (ImpressionController) self.getFellow("adminTabbox").getFellow("impressionsPanel").getFellow("impressionsMacro")
         .getFellow("winTemplate").getAttributeOrFellow("winTemplate$composer", true);

   }

   /**
    * Méthode récupérant le controller du panel des thésaurus.
    * @return ThesaurusController classe gérant le panel 
    * des Thesaurus.
    */
   public ThesaurusController getThesaurusController(){

      return (ThesaurusController) self.getFellow("adminTabbox").getFellow("thesaurusPanel").getFellow("thesaurusMacro")
         .getFellow("winThesaurus").getAttributeOrFellow("winThesaurus$composer", true);

   }

   /**
    * Méthode récupérant le controller du panel des utilisateurs.
    * @return TerminaleTypeController classe gérant le panel 
    * des TerminaleTypes.
    */
   public TerminaleTypeController getTerminaleTypeController(){

      return (TerminaleTypeController) self.getFellow("adminTabbox").getFellow("terminaleTypePanel")
         .getFellow("terminaleTypeMacro").getFellow("winTerminaleType").getAttributeOrFellow("winTerminaleType$composer", true);

   }

   /**
    * Méthode récupérant le controller du panel des codifications.
    * @return CodeController classe gérant le panel 
    */
   public CodesController getCodesController(){

      return (CodesController) self.getFellow("adminTabbox").getFellow("codesPanel").getFellow("codesMacro").getFellow("winCodes")
         .getAttributeOrFellow("winCodes$composer", true);

   }

   /**
    * Méthode récupérant le controller du panel des utilisateurs.
    * @return TerminaleTypeController classe gérant le panel 
    * des TerminaleTypes.
    */
   public ImportController getImportController(){

      return (ImportController) self.getFellow("adminTabbox").getFellow("importsPanel").getFellow("importsMacro")
         .getFellow("winImportTemplate").getAttributeOrFellow("winImportTemplate$composer", true);

   }

   /**
    * Méthode qui teste si le macrocomponent d'un panel a déja été
    * chargé.
    * @param panelName Nom du Tabpanel.
    * @param macroName Id du MacroComponent.
    * @return True si le MacroComponent a été chargé.
    */
   public boolean isFullfilledComponent(final String panelName, final String macroName){
      final Tabpanels panels = adminTabbox.getTabpanels();
      final Tabpanel panel = (Tabpanel) panels.getFellow(panelName);

      return panel.hasFellow(macroName);
   }

   /**
    * Sélection d'un panel. Cette méthode n'est utilisée que pour les
    * panels quidoivent être accédés à partir d'autres panels.
    * 
    * @version 2.1
    */
   public void onSelect$adminTabbox(){
      final Tabpanel item = adminTabbox.getSelectedPanel();

      // sélection du panel Banque
      if(item != null && item.getId().equals("banquePanel")){
         getMainWindow().createMacroComponent("/zuls/contexte/Banque.zul", "winBanque", item);
      }

      // sélection du panel Utilisateur
      if(item != null && item.getId().equals("utilisateursPanel")){
         getMainWindow().createMacroComponent("/zuls/utilisateur/Utilisateur.zul", "winUtilisateur", item);
      }

      // @since 2.1
      // sélection du panel Profil
      if(item != null && item.getId().equals("profilsPanel")){
         getMainWindow().createMacroComponent("/zuls/utilisateur/Profil.zul", "winProfil", item);
      }
   }

   @Override
   public TKdataObject loadById(final Integer id){
      return null;
   }
}
