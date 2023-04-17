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
package fr.aphp.tumorotek.action.imprimante;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.imprimante.ModeleType;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Classe gérant la création et l'édition des modèles dynamiques. Créée le
 * 15/06/2011.
 *
 * @author Pierre VENTADOUR.
 *
 */
public class FicheModeleDynModale extends AbstractFicheCombineController
{

   private final Logger log = LoggerFactory.getLogger(FicheModeleDynModale.class);

   private static final long serialVersionUID = -7456856301715246461L;

   // Components
   private Listbox modeleTypesBox;

   private Grid lignesGrid;

   // Objets principaux
   private Modele modele;

   private Component parent;

   private List<ModeleType> modeleTypes = new ArrayList<>();

   private ModeleType selectedModeleType;

   private List<LigneEtiquette> ligneEtiquettes = new ArrayList<>();

   private List<LigneEtiquette> ligneEtiquettesToRemove = new ArrayList<>();

   private ListModelList<LigneEtiquetteDecorator> ligneEtiquettesDeco = new ListModelList<>();

   private LigneEtiquetteRowRenderer ligneEtiquetteRowRenderer = new LigneEtiquetteRowRenderer();

   // qrcode
   //	@Wire("radio")
   Checkbox qrcode;
   //	private static Boolean isQRCode = false;
   //	private static final String QR_ACTIVATE = "oui";

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      // if (winPanel != null) {
      //	winPanel.setHeight("90%");
      //}

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {});

      setObjBoxsComponents(new Component[] {});

      setRequiredMarks(new Component[] {});

      getBinder().loadAll();
   }

   /**
    * Initialise la fenêtre : le parent, l'imprimante et passe en mode create
    * ou edit.
    *
    * @param imp
    * @param comp
    */
   public void initImprimanteModale(final Modele mod, final Component comp){
      setParent(comp);
      setObject(mod);
      if(modele.getModeleId() != null){
         switchToEditMode();
      }else{
         switchToCreateMode();
      }
   }

   @Override
   public void cloneObject(){
      setClone(this.modele.clone());
   }

   @Override
   public void onClick$cancelC(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   @Override
   public void onClick$revertC(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   @Override
   public void onClick$createC(){
      // vérifiction qu'un type est sélectionné
      if(selectedModeleType == null){
         throw new WrongValueException(modeleTypesBox, Labels.getLabel("fiche.modele.error.modeleType"));
      }

      Clients.showBusy(Labels.getLabel("fiche.modele.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   @Override
   public void onLaterCreate(){
      try{
         createNewObject();

         // ferme wait message
         Clients.clearBusy();
         // fermeture de la fenetre + message envoyé au parent
         postValidateEvent();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void createNewObject(){
      setEmptyToNulls();
      // QRCode
      modele.setIsQRCode(qrcode.isChecked());
      // le modèle n'est pas par défaut
      modele.setIsDefault(false);
      modele.setPlateforme(SessionUtils.getPlateforme(sessionScope));

      // list des lignes à créer
      ligneEtiquettes = new ArrayList<>();
      // hashtable contenant les champs à créer pour chaque ligne
      final Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> champsForLignes = new Hashtable<>();
      // pour chaque decorator de ligne, on va ajouter la ligne à la
      // liste et ses champs à la hashtable
      for(int i = 0; i < ligneEtiquettesDeco.size(); i++){
         ligneEtiquettes.add(ligneEtiquettesDeco.get(i).getLigneEtiquette());
         champsForLignes.put(ligneEtiquettesDeco.get(i).getLigneEtiquette(), ligneEtiquettesDeco.get(i).getChamps());
      }

      // update de l'objet
      ManagerLocator.getModeleManager().createObjectManager(modele, SessionUtils.getPlateforme(sessionScope), selectedModeleType,
         ligneEtiquettes, champsForLignes);
   }

   @Override
   public void onClick$validateC(){
      // vérifiction qu'un type est sélectionné
      if(selectedModeleType == null){
         throw new WrongValueException(modeleTypesBox, Labels.getLabel("fiche.modele.error.modeleType"));
      }

      Clients.showBusy(Labels.getLabel("fiche.modele.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public void onLaterUpdate(){
      try{
         updateObject();

         // ferme wait message
         Clients.clearBusy();
         // fermeture de la fenetre + message envoyé au parent
         postValidateEvent();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void updateObject(){
      setEmptyToNulls();

      // QRCode
      modele.setIsQRCode(qrcode.isChecked());

      // le modèle n'est pas par défaut
      modele.setIsDefault(false);
      modele.setPlateforme(SessionUtils.getPlateforme(sessionScope));

      // list des lignes à créer
      ligneEtiquettes = new ArrayList<>();
      // hashtable contenant les champs à créer pour chaque ligne
      final Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> champsForLignes = new Hashtable<>();
      // hashtable contenant les champs à supprimer pour chaque ligne
      final Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> champsToRemoveForLignes = new Hashtable<>();
      // pour chaque decorator de ligne, on va ajouter la ligne à la
      // liste et ses champs à la hashtable
      for(int i = 0; i < ligneEtiquettesDeco.size(); i++){
         ligneEtiquettes.add(ligneEtiquettesDeco.get(i).getLigneEtiquette());
         champsForLignes.put(ligneEtiquettesDeco.get(i).getLigneEtiquette(), ligneEtiquettesDeco.get(i).getChamps());
         // si des champs doivent être supprimés
         if(ligneEtiquettesDeco.get(i).getChampsToRemove() != null && ligneEtiquettesDeco.get(i).getChampsToRemove().size() > 0){
            champsToRemoveForLignes.put(ligneEtiquettesDeco.get(i).getLigneEtiquette(),
               ligneEtiquettesDeco.get(i).getChampsToRemove());
         }
      }

      // update de l'objet
      ManagerLocator.getModeleManager().updateObjectManager(modele, SessionUtils.getPlateforme(sessionScope), selectedModeleType,
         ligneEtiquettes, ligneEtiquettesToRemove, champsForLignes, champsToRemoveForLignes);
   }

   /**
    * Envoie un message au controller des imprimantes pour valider la
    * sauvegarde d'un modèle.
    */
   public void postValidateEvent(){
      // si le chemin d'accès à la page est correcte
      if(getParent() != null){
         // on envoie un event à cette page
         Events.postEvent(new Event("onGetModeleDone", getParent(), null));
      }

      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   @Override
   public String getDeleteWaitLabel(){
      return null;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public boolean prepareDeleteObject(){
      return false;
   }

   @Override
   public void removeObject(final String comments){

   }

   @Override
   public void setEmptyToNulls(){
      if(this.modele.getNom().equals("")){
         this.modele.setNom(null);
      }

      this.modele.setTexteLibre(null);
   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setFocusOnElement(){}

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public TKdataObject getObject(){
      return this.modele;
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.modele = (Modele) obj;

      super.setObject(obj);
   }

   @Override
   public void setNewObject(){
      setObject(new Modele());
   }

   @Override
   public void switchToCreateMode(){
      super.switchToCreateMode();
      modele.setIsDefault(false);

      initEditableMode();
      getBinder().loadComponent(self);
   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.modele.equals(new Modele()));
   }

   @Override
   public void switchToEditMode(){
      super.switchToEditMode();

      if(modele.getIsQRCode()){
         qrcode.setChecked(true);
         //setIsQRCode(true);
      }
      initEditableMode();
      getBinder().loadComponent(self);
   }

   public void initEditableMode(){
      // init des modeles types
      modeleTypes = ManagerLocator.getModeleTypeManager().findAllObjectsManager();
      modeleTypes.add(0, null);

      if(this.modele.getModeleId() != null){
         selectedModeleType = this.modele.getModeleType();

         ligneEtiquettes = ManagerLocator.getLigneEtiquetteManager().findByModeleManager(modele);
         ligneEtiquettesDeco.addAll(LigneEtiquetteDecorator.decorateListe(ligneEtiquettes));
      }else{
         selectedModeleType = null;
         ligneEtiquettes = new ArrayList<>();
         ligneEtiquettesDeco.clear();
      }
   }

   public void onSelect$modeleTypesBox(){
      if(selectedModeleType == null){
         throw new WrongValueException(modeleTypesBox, Labels.getLabel("fiche.modele.error.modeleType"));
      }
      Clients.clearWrongValue(modeleTypesBox);
   }

   /**
    * Clic sur l'image pour éditer une ligne.
    *
    * @param event
    */
   public void onClickEditLigne(final ForwardEvent event){
      if(event.getData() != null){
         final LigneEtiquetteDecorator deco = (LigneEtiquetteDecorator) event.getData();
         openLigneEtiquetteWindow(page, self, deco, false, qrcode.isChecked());
      }
   }

   /**
    * Clic sur l'image pour supprimer une ligne.
    *
    * @param event
    */
   public void onClickDeleteLigne(final ForwardEvent event){
      if(event.getData() != null){
         final LigneEtiquetteDecorator deco = (LigneEtiquetteDecorator) event.getData();
         final LigneEtiquette le = deco.getLigneEtiquette();

         if(Messagebox.show(
            ObjectTypesFormatters.getLabel("message.deletion.message",
               new String[] {Labels.getLabel("message.deletion.ligneEtiquette")}),
            Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
            // si la ligne était en base, il faudra la supprimer
            if(le.getLigneEtiquetteId() != null){
               ligneEtiquettesToRemove.add(le);
            }

            // on l'enlève de la liste
            final int idx = ligneEtiquettesDeco.indexOf(deco);
            for(int i = idx + 1; i < ligneEtiquettesDeco.size(); i++){
               ligneEtiquettesDeco.get(i).getLigneEtiquette()
                  .setOrdre(ligneEtiquettesDeco.get(i).getLigneEtiquette().getOrdre() - 1);
            }
            ligneEtiquettesDeco.remove(deco);
            ligneEtiquettes.remove(le);
            final ListModel<LigneEtiquetteDecorator> list = new ListModelList<>(ligneEtiquettesDeco);
            lignesGrid.setModel(list);
            getBinder().loadComponent(lignesGrid);
         }
      }
   }

   /**
    * Clic sur l'image pour monter une ligne.
    *
    * @param event
    */
   public void onClickUpLigne(final ForwardEvent event){
      if(event.getData() != null){
         final LigneEtiquetteDecorator deco = (LigneEtiquetteDecorator) event.getData();
         upObject(deco);
         getBinder().loadAttribute(lignesGrid, "model");
      }
   }

   /**
    * Clic sur l'image pour descendre une ligne.
    *
    * @param event
    */
   public void onClickDownLigne(final ForwardEvent event){
      if(event.getData() != null){
         final LigneEtiquetteDecorator deco = (LigneEtiquetteDecorator) event.getData();
         final int tabIndex = ligneEtiquettesDeco.indexOf(deco);
         if(tabIndex + 1 < ligneEtiquettesDeco.size()){
            upObject(ligneEtiquettesDeco.get(tabIndex + 1));
         }
         getBinder().loadAttribute(lignesGrid, "model");
      }
   }

   /**
    * Effectue l'operation de mouvements des objets au sein de la liste.
    *
    * @param objet
    *            a monter d'un cran
    */
   private void upObject(final LigneEtiquetteDecorator obj){
      final int tabIndex = ligneEtiquettesDeco.indexOf(obj);
      LigneEtiquetteDecorator supObjectInList = null;
      if(tabIndex - 1 > -1){
         supObjectInList = ligneEtiquettesDeco.get(tabIndex - 1);
         supObjectInList.getLigneEtiquette().setOrdre(supObjectInList.getLigneEtiquette().getOrdre() + 1);
         obj.getLigneEtiquette().setOrdre(obj.getLigneEtiquette().getOrdre() - 1);
         ligneEtiquettesDeco.set(tabIndex, supObjectInList);
         ligneEtiquettesDeco.set(tabIndex - 1, obj);
      }
   }

   public void onClick$addLigne(){
      openLigneEtiquetteWindow(page, self, new LigneEtiquetteDecorator(new LigneEtiquette(), null), true, qrcode.isChecked());
   }

   /**
    * Méthode appelé une fois que l'utilisateur a fini de modifier une ligne.
    *
    * @param e
    */
   public void onGetLigneEtiquette(final Event e){
      if(e.getData() != null){
         final LigneEtiquetteDecorator deco = (LigneEtiquetteDecorator) e.getData();

         // si la ligne était déja définie
         if(ligneEtiquettesDeco.contains(deco)){
            // on enlève l'ancienne pour la remplacer par
            // la nouvelle
            final int idx = ligneEtiquettesDeco.indexOf(deco);
            ligneEtiquettesDeco.remove(deco);
            ligneEtiquettesDeco.add(idx, deco);

            ligneEtiquettes.remove(deco.getLigneEtiquette());
            ligneEtiquettes.add(idx, deco.getLigneEtiquette());
         }else{ // sinon on ajoute la ligne
            deco.getLigneEtiquette().setModele(modele);
            deco.getLigneEtiquette().setOrdre(ligneEtiquettesDeco.size() + 1);
            ligneEtiquettesDeco.add(deco);
            ligneEtiquettes.add(deco.getLigneEtiquette());
         }

         getBinder().loadComponent(lignesGrid);
         Clients.scrollIntoView(lignesGrid);
      }
   }

   /**
    * Ouvre une fenêtre permettant de définir une ligne d'étiquettes.
    *
    * @param page
    *            Page.
    * @param comp
    *            Composant.
    * @param deco
    *            Ligne etiquette.
    */
   public void openLigneEtiquetteWindow(final Page page, final Component comp, final LigneEtiquetteDecorator deco,
      final boolean creationMode, final boolean isQRcode){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("ligneEtiquetteWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("fiche.ligne.etiquette.title"));
         win.setBorder("normal");
         win.setWidth("600px");
         win.setHeight("600px");
         win.setClosable(true);

         final HtmlMacroComponent ua = populateFicheLigneEtiquetteModal(win, page, comp, deco, creationMode, isQRcode);
         ua.setVisible(false);

         win.addEventListener("onTimed", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               // progress.detach();
               ua.setVisible(true);
            }
         });

         final Timer timer = new Timer();
         timer.setDelay(500);
         timer.setRepeats(false);
         timer.addForward("onTimer", timer.getParent(), "onTimed");
         win.appendChild(timer);
         timer.start();

         try{
            win.onModal();
            setBlockModal(false);

         }catch(final SuspendNotAllowedException e){
            log.error("An error occurred: {}", e.toString()); 
         }
      }
   }

   private static HtmlMacroComponent populateFicheLigneEtiquetteModal(final Window win, final Page page, final Component comp,
      final LigneEtiquetteDecorator deco, final boolean creationMode, final boolean isQRCode){
      // HtmlMacroComponent contenu dans la fenêtre : il correspond
      // au composant des modèles dynamiques.
      HtmlMacroComponent ua;
      ua = (HtmlMacroComponent) page.getComponentDefinition("ficheLigneEtiquetteModale", false).newInstance(page, null);
      ua.setParent(win);
      ua.setId("openLigneEtiquetteModale");
      ua.applyProperties();
      ua.afterCompose();

      ((FicheLigneEtiquetteModale) ua.getFellow("fwinLigneEtiquetteModale")
         .getAttributeOrFellow("fwinLigneEtiquetteModale$composer", true)).initModale(deco, comp, creationMode, isQRCode);

      return ua;
   }

   public void onClick$testImpression(){
      //		List<LigneEtiquette> tmp = new ArrayList<LigneEtiquette>();
      //		for (int i = 0; i < ligneEtiquettesDeco.size(); i++) {
      //			tmp.add(ligneEtiquettesDeco.get(i).getLigneEtiquette());
      //		}
      //		openEtiquetteWindow(page, true, modele, tmp, null);

      // mock affectation pour sélectionner le modele
      final AffectationImprimante aff = new AffectationImprimante();
      aff.setModele(getModele());

      final List<LigneEtiquette> lignes = new ArrayList<>();
      for(int i = 0; i < ligneEtiquettesDeco.size(); i++){
         lignes.add(ligneEtiquettesDeco.get(i).getLigneEtiquette());
      }

      openImprimanteModeleModale(SessionUtils.getPlateforme(sessionScope), null, aff, lignes);
   }

   //	public void onCheck$qrcode_radiogroup(CheckEvent e) {
   //		if (((Radio) e.getTarget()).getLabel().equals(QR_ACTIVATE)) {
   //			isQRCode = true;
   //		} else {
   //			isQRCode = false;
   //		}
   //	}

   /**********************************************************************/
   /*********************** GETTERS **************************************/
   /**********************************************************************/

   public ConstWord getNomConstraint(){
      return ModeleConstraints.getNomConstraint();
   }

   public Listbox getModeleTypesBox(){
      return modeleTypesBox;
   }

   public void setModeleTypesBox(final Listbox m){
      this.modeleTypesBox = m;
   }

   public Modele getModele(){
      return modele;
   }

   public void setModele(final Modele m){
      this.modele = m;
   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component p){
      this.parent = p;
   }

   public List<ModeleType> getModeleTypes(){
      return modeleTypes;
   }

   public void setModeleTypes(final List<ModeleType> m){
      this.modeleTypes = m;
   }

   public ModeleType getSelectedModeleType(){
      return selectedModeleType;
   }

   public void setSelectedModeleType(final ModeleType s){
      this.selectedModeleType = s;
   }

   public List<LigneEtiquette> getLigneEtiquettes(){
      return ligneEtiquettes;
   }

   public void setLigneEtiquettes(final List<LigneEtiquette> l){
      this.ligneEtiquettes = l;
   }

   public LigneEtiquetteRowRenderer getLigneEtiquetteRowRenderer(){
      return ligneEtiquetteRowRenderer;
   }

   public void setLigneEtiquetteRowRenderer(final LigneEtiquetteRowRenderer l){
      this.ligneEtiquetteRowRenderer = l;
   }

   public List<LigneEtiquetteDecorator> getLigneEtiquettesDeco(){
      return ligneEtiquettesDeco;
   }

   public void setLigneEtiquettesDeco(final ListModelList<LigneEtiquetteDecorator> l){
      this.ligneEtiquettesDeco = l;
   }

   public List<LigneEtiquette> getLigneEtiquettesToRemove(){
      return ligneEtiquettesToRemove;
   }

   public void setLigneEtiquettesToRemove(final List<LigneEtiquette> l){
      this.ligneEtiquettesToRemove = l;
   }

   //	public static Boolean getIsQRCode() {
   //		return isQRCode;
   //	}
   //
   //	public static void setIsQRCode(Boolean isQRCode) {
   //		FicheModeleDynModale.isQRCode = isQRCode;
   //	}

}