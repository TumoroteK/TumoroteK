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
package fr.aphp.tumorotek.action.code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.component.SmallObjDecorator;
import fr.aphp.tumorotek.component.SmallObjsEditableGrid;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Composer d'une gride permettant la creation modification des
 * codes assignes (organes et lésionnels) en mode éditable.
 * Date: 12/07/2010
 *
 * @author mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class CodeAssigneEditableGrid extends SmallObjsEditableGrid
{

   private final Log log = LogFactory.getLog(CodeAssigneEditableGrid.class);

   private static final long serialVersionUID = 1L;

   private Echantillon echantillon;
   private boolean isOrg = true;
   private boolean isMorpho = false;
   private boolean toCims = false;
   private Banque banque;
   private Button addEmptyCode;
   private Column expCol;
   private Hbox favBox;

   private Combobox bookmarksBox;

   private boolean assistCodeClicked = false;

   // @since 2.1
   // fix codebox.onBlur / onClick problem 
   // due to echoEvent onLaterFindLibelle triggers 
   // this event after validate/cancel
   private boolean findLibelleEchoEd = false;
   private Event validateClickEd = null;
   private Event cancelClickEd = null;

   private static ConstWord codeConstraint = new ConstWord();
   {
      codeConstraint.setNullable(false);
      codeConstraint.setSize(50);
   }
   private static ConstWord libelleConstraint = new ConstWord();
   {
      libelleConstraint.setNullable(true);
      libelleConstraint.setSize(300);
   }

   private Radio exportBoxEachCode;

   public void setEchantillon(final Echantillon e){
      this.echantillon = e;
   }

   /**
    * Appelée par le controller de la fiche lors de la creation.
    * @param o
    */
   public void setIsOrg(final boolean o){
      this.isOrg = o;
      if(isOrg){
         setDeletionMessageKey("message.deletion.codeOrgane");
      }
   }

   public void setIsMorpho(final boolean m){
      this.isMorpho = m;
      if(isMorpho){
         setDeletionMessageKey("message.deletion.codeMorpho");
      }
   }

   public void setToCims(final boolean o){
      this.toCims = o;
      if(toCims){
         expCol.setVisible(false);
         addEmptyCode.setVisible(false);
         favBox.setVisible(false);
      }
   }

   public ConstWord getCodeConstraint(){
      return codeConstraint;
   }

   public ConstWord getLibelleConstraint(){
      return libelleConstraint;
   }

   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque bank){
      this.banque = bank;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      setDeletionMessageKey("message.deletion.codeGeneral");
      getBinder().loadAll();
   }

   /**
    * Cree un nouvel code assigne et l'ajoute a la liste..
    */
   public void addCodeAssigne(){

      revertCurrentObjEdition();

      // empeche changement export
      exportBoxEachCode.setDisabled(true);

      final CodeAssigne newCode = new CodeAssigne();
      newCode.setEchantillon(echantillon);
      newCode.setIsOrgane(isOrg);
      newCode.setIsMorpho(!isOrg);
      newCode.setOrdre(getLastObjectOrdre() + 1);

      setCurrentObjEdited(new CodeAssigneDecorator(newCode));
      if(getObjs().isEmpty()){ // export par defaut
         ((CodeAssigneDecorator) getCurrentObjEdited()).setExport(true);
      }
      getCurrentObjEdited().setEdition(true);
      setBeforeEditObjClone(newCode);
      getObjs().add(getCurrentObjEdited());

      reloadGrid();

      // colore Row
      ((Row) rows.getChildren().get(getObjs().indexOf(getCurrentObjEdited()))).setStyle("background-color: #fddfa9");
   }

   /**
    * Cree un nouvel code assigne a partir d'un code existant
    * et l'ajoute a la liste.
    * @param code existant
    * @param pass le code directement en edition si true;
    */
   public void addCodeAssigne(final CodeCommon code, final boolean switchToEdit){

      revertCurrentObjEdition();

      // empeche changement export
      exportBoxEachCode.setDisabled(true);

      final CodeAssigne newCode = new CodeAssigne();
      newCode.setEchantillon(echantillon);
      newCode.setIsOrgane(isOrg);
      newCode.setIsMorpho(!isOrg);
      newCode.setCodeRefId(code.getCodeId());
      newCode.setCode(code.getCode());
      newCode.setLibelle(code.getLibelle());
      newCode.setTableCodage(ManagerLocator.getCommonUtilsManager().getTableCodageFromCodeCommonManager(code));

      final CodeAssigneDecorator deco = new CodeAssigneDecorator(newCode);
      getObjs().add(deco);

      if(getObjs().isEmpty()){ // export par defaut
         deco.setExport(true);
      }

      if(switchToEdit){
         setCurrentObjEdited(deco);
         getCurrentObjEdited().setEdition(true);
         setBeforeEditObjClone(newCode);
      }

      reloadGrid();
   }

   @Override
   public void onClick$editObj(final Event event) throws IOException{
      // empeche changement export
      exportBoxEachCode.setDisabled(true);
      super.onClick$editObj(event);

   }

   @Override
   public void onClick$deleteObj(final Event event){

      final int codesSize = getObjs().size();
      final CodeAssigneDecorator toBeDeleted =
         (CodeAssigneDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      // suppression
      super.onClick$deleteObj(event);

      // si l'objet a bien été supprimé
      if(getObjs().size() < codesSize){
         // si ce code etait exporté, passe l'export au premier
         // par defaut
         if(toBeDeleted.getExport()){
            if(!getObjs().isEmpty()){
               ((CodeAssigneDecorator) getObjs().get(0)).setExport(true);

               // rétablit la relation oneToone en ajoutant la référence
               // vers échantillon et en la passant a null au code supprime
               //					if (isOrg) {
               //						((CodeAssigne) 
               //							((CodeAssigneDecorator) getObjs().get(0)).getObj())
               //								.setEchanExpOrg(echantillon);
               //						if (getObjToDelete().contains(toBeDeleted)) {
               //							((CodeAssigne) 
               //								((CodeAssigneDecorator) getObjToDelete()
               //									.get(getObjToDelete().indexOf(toBeDeleted)))
               //																	.getObj())
               //													.setEchanExpOrg(null);
               //						}
               //					} else {
               //						((CodeAssigne) 
               //							((CodeAssigneDecorator) getObjs().get(0)).getObj())
               //									.setEchanExpLes(echantillon);
               //						if (getObjToDelete().contains(toBeDeleted)) {
               //							((CodeAssigne) 
               //								((CodeAssigneDecorator) getObjToDelete()
               //									.get(getObjToDelete().indexOf(toBeDeleted)))
               //																	.getObj())
               //													.setEchanExpLes(null);
               //						}
               //					}
            }
         }
      }
   }

   @Override
   public void onClick$validateObj(final Event event){
      validateClickEd = event;
      if(!findLibelleEchoEd){
         // re-active la colonne export
         exportBoxEachCode.setDisabled(false);
         super.onClick$validateObj(event);
         validateClickEd = null;
      }
   }

   @Override
   public void onClick$revertObj(final Event event){
      cancelClickEd = event;
      if(!findLibelleEchoEd){
         // re-active la colonne export
         exportBoxEachCode.setDisabled(false);
         super.onClick$revertObj(event);
         cancelClickEd = null;
      }
   }

   /**
    * Set l'export au decorateur et passe les flags export
    * des autres decorateurs à false.
    * @param event
    */
   public void onCheck$exportBoxEachCode(final Event event){
      // recupere le deco associe au checkbox
      final CodeAssigneDecorator current =
         (CodeAssigneDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      for(int i = 0; i < getObjs().size(); i++){
         ((CodeAssigneDecorator) getObjs().get(i)).setExport(false);
      }
      current.setExport(true);
      reloadGrid();
   }

   public Integer getLastObjectOrdre(){
      if(getObjs().isEmpty()){
         return new Integer(0);
      }else{
         return getObjs().get(getObjs().size() - 1).getOrdre();
      }
   }

   /*************************************************************/
   /*********************** ToolBar. ************************/
   /*************************************************************/
   /**
    * Dessine le contenu de la liste de favoris quand elle est ouverte
    * pour la première fois.
    */
   public void onOpen$bookmarksBox(){
      if(bookmarksBox.getItemCount() == 0){
         drawCodesBookmarksBox(bookmarksBox);
      }
   }

   /**
    * Cree un nouveau code a partir de la selection dans la liste.
    */
   public void onClick$addFromBookmarks(){
      if(bookmarksBox.getSelectedItem() != null){

         final CodeCommon value = (CodeCommon) bookmarksBox.getSelectedItem().getValue();

         final List<CodeAssigne> codesAss = new ArrayList<>();

         if(value != null){
            // bug exportNbIllegal -> supprime code non enregistré 
            revertCurrentObjEdition();
            exportBoxEachCode.setDisabled(false);

            final CodeAssigne newCode = new CodeAssigne();
            newCode.setEchantillon(echantillon);
            newCode.setIsOrgane(isOrg);
            newCode.setIsMorpho(!isOrg);
            newCode.setCodeRefId(value.getCodeId());
            newCode.setCode(value.getCode());
            newCode.setLibelle(value.getLibelle());
            newCode.setTableCodage(ManagerLocator.getCommonUtilsManager().getTableCodageFromCodeCommonManager(value));

            codesAss.add(newCode);

            addCodesAndTranscodesToGrid(codesAss, this);

            reloadGrid();

            //getCodesOrganeController().addCodeAssigne(value, false);
         }
      }
   }

   /**
    * Dessine le contenu des listbox proposant les codes choisis
    * comme favoris par l'utilisateur pour cette banque.
    */
   private void drawCodesBookmarksBox(final Combobox box){

      final Iterator<CodeCommon> codesIt =
         ManagerLocator.getCodeSelectManager().findByRootDossierAndBanqueManager(getBanque()).iterator();

      final Iterator<CodeDossier> dosIt =
         ManagerLocator.getCodeDossierManager().findByRootDossierBanqueManager(getBanque(), true).iterator();

      // dessine les codes
      drawCodesAsItem(codesIt, box);

      // dessine le contenu des dossiers
      while(dosIt.hasNext()){
         drawItemForDossierRecursive(dosIt.next(), box);
      }

   }

   /**
    * Dessine un dossier dans le listbox.
    * Dessine le group, les codes contenus et les dossiers et leur
    * contenu de mnière récursive.
    * @param dos
    * @param box lisbox
    */
   private void drawItemForDossierRecursive(final CodeDossier dos, final Combobox box){

      // ajoute le groupe
      final Comboitem grp = new Comboitem(dos.getNom());
      grp.setDisabled(true);
      grp.setStyle("font-style: italic");
      box.appendChild(grp);

      // dessine les codes en items
      drawCodesAsItem(ManagerLocator.getCodeSelectManager().findCodesFromSelectByDossierManager(dos).iterator(), box);

      // ajoute les dossiers
      final Iterator<CodeDossier> dosIt = ManagerLocator.getCodeDossierManager().findByCodeDossierParentManager(dos).iterator();

      while(dosIt.hasNext()){
         drawItemForDossierRecursive(dosIt.next(), box);
      }
   }

   /**
    * Dessine les items dans le composant a partir d'un iterator de CodeCommon.
    * @param codesIt
    * @param box listBox
    */
   private void drawCodesAsItem(final Iterator<CodeCommon> codesIt, final Combobox box){
      // ajoute les codes
      Comboitem li;
      CodeCommon code;
      while(codesIt.hasNext()){
         code = codesIt.next();
         li = new Comboitem(code.getCode());
         li.setValue(code);
         box.appendChild(li);
      }
   }

   /**
    * Decore la liste de code assignes et ses eventuels transcodes dictés 
    * par la configuration de la banque à la grid dont le controller est passé
    * en paramètre.
    * Place le preimer code export si la liste était vide auparavant
    * @param codesAss
    * @param grid
    */
   private void addCodesAndTranscodesToGrid(final List<CodeAssigne> codesAss, final CodeAssigneEditableGrid grid){

      final List<SmallObjDecorator> codes = CodeAssigneDecorator.decorateListe(codesAss);

      if(grid.getObjs().isEmpty() && !codes.isEmpty()){
         ((CodeAssigneDecorator) codes.get(0)).setExport(true);
      }

      //		// recupere les codifications associées à la banque courante
      //		Iterator<BanqueTableCodage> btcsIt = ManagerLocator.getBanqueManager()
      //			.getBanqueTableCodageByBanqueManager(SessionUtils
      //					.getSelectedBanques(sessionScope).get(0)).iterator();
      //		
      //		List<TableCodage> codifs = new ArrayList<TableCodage>();
      //		while (btcsIt.hasNext()) {
      //			codifs.add(btcsIt.next().getTableCodage());
      //		}

      //		boolean doTranscode = codifs.size() > 1;

      final Iterator<SmallObjDecorator> codestoAddIt = codes.iterator();
      CodeAssigneDecorator next;
      List<CodeCommon> transcodes;
      Iterator<CodeCommon> transcodesIt;
      CodeCommon next2;
      TableCodage trTable;
      CodeAssigne transcodeAss;
      CodeAssigneDecorator transcodeAssDeco;
      while(codestoAddIt.hasNext()){
         next = (CodeAssigneDecorator) codestoAddIt.next();
         next.setEchantillon(echantillon);
         next.setValidated(true);
         if(!grid.getObjs().contains(next)){
            next.setOrdre(grid.getLastObjectOrdre() + 1);
            next.setOrdreInit(grid.getLastObjectOrdre() + 1);
            grid.getObjs().add(next);
         }
         if(!grid.getObjToCreateOrEdit().contains(next)){
            grid.getObjToCreateOrEdit().add(next);
         }

         if(!toCims){ // pas de transcodage si CIMs
            transcodes = ManagerLocator.getTableCodageManager().transcodeManager(
               ManagerLocator.getCommonUtilsManager().findCodeByTableCodageAndIdManager(
                  ((CodeAssigne) next.getObj()).getCodeRefId(), ((CodeAssigne) next.getObj()).getTableCodage()),
               getTablesForBanques(), SessionUtils.getSelectedBanques(sessionScope));
            transcodesIt = transcodes.iterator();
            while(transcodesIt.hasNext()){
               next2 = transcodesIt.next();
               trTable = ManagerLocator.getCommonUtilsManager().getTableCodageFromCodeCommonManager(next2);
               // si la table de transcode est assignee a la banque
               //				if (codifs.contains(trTable)) {
               transcodeAss = new CodeAssigne();
               transcodeAss.setCode(next2.getCode());
               transcodeAss.setLibelle(next2.getLibelle());
               transcodeAss.setCodeRefId(next2.getCodeId());
               transcodeAss.setTableCodage(trTable);
               transcodeAss.setIsOrgane(((CodeAssigne) next.getObj()).getIsOrgane());
               transcodeAss.setIsMorpho(((CodeAssigne) next.getObj()).getIsMorpho());
               transcodeAss.setEchantillon(echantillon);
               transcodeAssDeco = new CodeAssigneDecorator(transcodeAss);
               if(!grid.getObjs().contains(transcodeAssDeco)){
                  transcodeAssDeco.setOrdre(grid.getLastObjectOrdre() + 1);
                  transcodeAssDeco.setOrdreInit(grid.getLastObjectOrdre() + 1);
                  transcodeAssDeco.setValidated(true);
                  grid.getObjs().add(transcodeAssDeco);
               }
               if(!grid.getObjToCreateOrEdit().contains(transcodeAssDeco)){
                  grid.getObjToCreateOrEdit().add(transcodeAssDeco);
               }
            }
         }
      }
   }

   /**
    * Ouvre la modale contenant l'assistant deployant les 
    * codifications non pre-filtrées pour les codes lésionnels.
    */
   public void onClick$codesAssistantButton(){

      // bug exportNbIllegal -> supprime code non enregistré 
      revertCurrentObjEdition();
      exportBoxEachCode.setDisabled(false);
      reloadGrid();

      openCodesModal(page, Path.getPath(self), isMorpho, isOrg, false, toCims);

      // drawCodesBookmarksBox(bookmarksBox);
   }

   /**
    * Evenement renvoyé par l'assistant de sélection des codes.
    * L'assistant renvoie une liste de codes assignes à ajouter
    * à la liste existante.
    * @param event
    */
   
   public void onGetCodesFromAssist(final Event event){
      if(event.getData() != null){
         final List<CodeCommon> res = (List<CodeCommon>) event.getData();

         addCodesAndTranscodesToGrid(handleCodeCommonList(res), this);

         reloadGrid();
      }
   }

   private List<CodeAssigne> handleCodeCommonList(final Collection<CodeCommon> cds){
      final Iterator<CodeCommon> it = cds.iterator();
      CodeCommon selCode;
      CodeAssigne codeAs;
      final List<CodeAssigne> codes = new ArrayList<>();
      while(it.hasNext()){
         selCode = it.next();
         codeAs = new CodeAssigne();
         codeAs.setCodeRefId(selCode.getCodeId());
         codeAs.setCode(selCode.getCode());
         codeAs.setLibelle(selCode.getLibelle());
         codeAs.setTableCodage(ManagerLocator.getCommonUtilsManager().getTableCodageFromCodeCommonManager(selCode));
         codeAs.setIsMorpho(isMorpho);
         codeAs.setIsOrgane(isOrg);
         codes.add(codeAs);
      }
      return codes;
   }

   /**
    * Cree un nouveau code et l'ajoute a la liste..
    */
   public void onClick$addEmptyCode(){
      addCodeAssigne();
   }

   /**
    * Ouvre la modale assistant codes en lui spécifiant l'objet 
    * les paramètres morpho et organe. Si code diagnostic maladie, 
    * passe un paramètre boolean isDiagnostic a true.
    * @param page
    * @param path
    * @param isMorpho
    * @param isOrgane
    * @param isDiagnostic
    */
   public void openCodesModal(final Page page, final String path, final boolean isM, final boolean isOrgane,
      final boolean isDiagnostic){

      if(!assistCodeClicked){

         assistCodeClicked = true;

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("winCodesModal");
         win.setPage(page);
         win.setPosition("left,top");
         win.setHeight((getMainWindow().getWindowAvailableHeight()) + "px");
         win.setWidth((getMainWindow().getWindowAvailableWidth()) + "px");
         win.setMaximizable(true);
         win.setSizable(true);

         String[] titleParam = new String[] {};
         if(isOrgane){
            titleParam = new String[] {"codes organe"};
         }else if(isM){
            titleParam = new String[] {"codes morphologiques"};
         }else if(isDiagnostic){
            titleParam = new String[] {"code diagnostic"};
         }
         win.setTitle(ObjectTypesFormatters.getLabel("code.component.title", titleParam));
         win.setBorder("normal");
         win.setClosable(true);

         final HtmlMacroComponent ua =
            (HtmlMacroComponent) page.getComponentDefinition("codesModale", false).newInstance(page, null);

         ua.setParent(win);
         ua.setId("codesModalMacro");
         ua.applyProperties();
         ua.afterCompose();
         ua.setVisible(false);

         ((CodesController) ua.getFellow("winCodes").getAttributeOrFellow("winCodes$composer", true)).init(path, isOrgane,
            isMorpho, isDiagnostic, toCims);

         win.addEventListener("onTimed", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               ua.setVisible(true);
            }
         });
         final Timer timer = new Timer();
         timer.setDelay(200);
         timer.setRepeats(false);
         timer.addForward("onTimer", timer.getParent(), "onTimed");
         win.appendChild(timer);
         timer.start();
         try{
            win.onModal();
            assistCodeClicked = false;

         }catch(final SuspendNotAllowedException e){
            log.error(e);
         }
      }
   }

   /**
    * Lance une requête de codes vers les classifications une fois le code 
    * renseigné afin de peupler la ligne avec le libelle exact si aucun libelle n'a été 
    * saisi.
    * @param e Event
    * @since 2.0.10
    */
   public void onBlur$codeBox(final Event e){

      final ForwardEvent fe = (ForwardEvent) e;
      final Textbox org = (Textbox) fe.getOrigin().getTarget();

      if(org.getValue() != null && !org.getValue().equals("")){
         Clients.showBusy(e.getTarget().getParent().getParent(), Labels.getLabel("libelle.recherche.encours"));
         findLibelleEchoEd = true;
         Events.echoEvent("onLaterFindLibelle", self, e);
      }
   }

   public void onLaterFindLibelle(final Event e){

      findLibelleEchoEd = false;
      final ForwardEvent fe = (ForwardEvent) e.getData();

      if(cancelClickEd == null){ // find Libelle skip if cancel clicked

         final Textbox org = (Textbox) fe.getOrigin().getTarget();
         final Textbox tb = (Textbox) org.getParent().getNextSibling().getFirstChild();

         if(tb.getValue().equals("")){

            final Set<CodeCommon> codes = new HashSet<>();
            CodeUtils.findCodesInAllTables(org.getValue(), isOrg, isMorpho, codes, true,
               SessionUtils.getSelectedBanques(sessionScope));

            if(!codes.isEmpty()){

               setCurrentObjEdited((CodeAssigneDecorator) AbstractListeController2.getBindingData(fe, false));

               boolean exactCodeFound = false;
               for(final CodeCommon cc : codes){
                  if(cc.getCode().equalsIgnoreCase(org.getValue())){
                     exactCodeFound = true;
                     break;
                  }
               }

               // remove curr code si code completé est retourné par assistant
               // sinon conserve la ligne en mode édition
               if(exactCodeFound){
                  revertCurrentObjEdition();
                  getObjs().remove(getCurrentObjEdited());
                  if(getObjToCreateOrEdit().contains(getCurrentObjEdited())){
                     getObjToCreateOrEdit().remove(getCurrentObjEdited());
                  }
                  // si l'élément existait dans la BDD on l'ajoute à la
                  // liste des éléments à supprimer (il ne sera délété que
                  // lors de la sauvegarde finale)
                  if(getCurrentObjEdited().getObjDbId() != null){
                     getObjToDelete().add(getCurrentObjEdited());
                     if(((CodeAssigneDecorator) getCurrentObjEdited()).getExport() && !getObjs().isEmpty()){
                        // passe l'export au code suivant si il y en a un
                        ((CodeAssigneDecorator) getObjs().get(0)).setExport(true);
                     }
                  }

               }else{ // valide le premier code
                  if(getCurrentObjEdited() != null){
                     getCurrentObjEdited().setEdition(false);
                     getCurrentObjEdited().setValidated(true);
                     // ajout a la liste des éléments a creer ou editer
                     if(!getObjToCreateOrEdit().contains(getCurrentObjEdited())){
                        getObjToCreateOrEdit().add(getCurrentObjEdited());
                     }
                  }
               }
               setBeforeEditObjClone(null);
               setCurrentObjEdited(null);

               addCodesAndTranscodesToGrid(handleCodeCommonList(codes), this);

               //				if (passExport) {
               //					((CodeAssigneDecorator) 
               //						getObjToCreateOrEdit().get(getObjToCreateOrEdit().size() -1))
               //						.setExport(true);
               //				}

               // empeche changement export
               exportBoxEachCode.setDisabled(false);

               reloadGrid();
            }else if(validateClickEd != null){ // validate clicked
               Events.postEvent(validateClickEd);
            }
            //			else { // remove if doublons
            //				for (SmallObjDecorator deco : getObjs()) {
            //					if (((CodeAssigneDecorator) deco).getCode().equals(org.getValue())
            //						&& !deco.getEdition()) {
            //					}
            //				}
            //			}
         }

      }else{
         Events.postEvent(cancelClickEd);
      }

      Clients.clearBusy(fe.getTarget().getParent().getParent());

   }

   public void addCodesFromInjection(final List<CodeAssigne> injectedCodes){

      if(injectedCodes != null){
         final Set<CodeCommon> allCodes = new HashSet<>();
         final Set<CodeCommon> codes = new HashSet<>();

         for(final CodeAssigne ca : injectedCodes){
            codes.clear();
            CodeUtils.findCodesInAllTables(ca.getCode(), isOrg, isMorpho, codes, true,
               SessionUtils.getSelectedBanques(sessionScope));
            if(!codes.isEmpty()){
               allCodes.addAll(codes);
            }else{
               allCodes.add(ca);
            }

         }

         if(!allCodes.isEmpty()){

            setBeforeEditObjClone(null);
            setCurrentObjEdited(null);

            addCodesAndTranscodesToGrid(handleCodeCommonList(allCodes), this);

            //				if (passExport) {
            //					((CodeAssigneDecorator) 
            //						getObjToCreateOrEdit().get(getObjToCreateOrEdit().size() -1))
            //						.setExport(true);
            //				}

            // empeche changement export
            exportBoxEachCode.setDisabled(false);

            reloadGrid();
         }
      }
   }
}
