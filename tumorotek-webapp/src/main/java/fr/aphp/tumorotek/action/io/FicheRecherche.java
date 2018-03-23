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
package fr.aphp.tumorotek.action.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.ObjetsAffichageRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheRecherche extends AbstractFicheCombineController
{

   //private Log log = LogFactory.getLog(FicheRecherche.class);

   private static final long serialVersionUID = 1L;

   /**
    *  Editable components : mode d'édition ou de création.
    */
   private Label intituleLabel;
   private Label requeteLabel;
   private Label affichageLabel;
   // private Label banquesLabel;

   private Label intituleRequired;
   private Textbox intituleBox;

   private Label requeteRequired;
   private Listbox requetesBox;

   private Label affichageRequired;
   private Listbox affichagesBox;

   // private Label banquesRequired;
   // private Listbox banquesBox;

   private List<Requete> requetes;
   private List<Affichage> affichages;
   private List<Banque> banques;

   private List<Banque> banquesDeRecherche;

   private ObjetsAffichageRenderer objAffRenderer;

   private Recherche recherche;

   /**
    *  Gestion des contraintes sur les quantités et volumes.
    *  Variables conservant les valeurs saisies dans les champs. 
    */

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {intituleLabel, requeteLabel, affichageLabel});
      setObjBoxsComponents(new Component[] {intituleBox, requetesBox, affichagesBox});
      setRequiredMarks(new Component[] {intituleRequired, requeteRequired, affichageRequired});

      winPanel.setHeight(getMainWindow().getPanelHeight() - 110 + "px");

      super.switchToStaticMode(true);

      drawActionsForRecherche();

      getBinder().loadAll();
   }

   @Override
   public TKdataObject getObject(){
      return this.recherche;
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.recherche = (Recherche) obj;

      if(this.recherche.getRechercheId() == null){
         banquesDeRecherche = new ArrayList<>();
      }else{
         banquesDeRecherche = ManagerLocator.getRechercheManager().findBanquesManager(recherche);
      }

      super.setObject(obj);
   }

   @Override
   public void setNewObject(){
      setObject(new Recherche());
   }

   @Override
   public void cloneObject(){
      setClone(this.recherche.clone());
   }

   @Override
   public RechercheController getObjectTabController(){
      return (RechercheController) super.getObjectTabController();
   }

   /* Methode comportementales */
   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      this.affichages =
         ManagerLocator.getAffichageManager().findByBanqueManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
      Collections.sort(affichages);
      affichages.add(0, null);
      this.affichagesBox.setModel(new SimpleListModel<>(affichages));

      this.requetes =
         ManagerLocator.getRequeteManager().findByBanqueManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
      Collections.sort(requetes);
      requetes.add(0, null);
      this.requetesBox.setModel(new SimpleListModel<>(requetes));

      final Utilisateur user = (Utilisateur) sessionScope.get("User");
      final Entite entiteRequete = ManagerLocator.getEntiteManager().findByNomManager("Requete").get(0);
      this.banques = ManagerLocator.getBanqueManager().findByEntiteConsultByUtilisateurManager(user, entiteRequete,
         SessionUtils.getPlateforme(sessionScope));
      Collections.sort(banques);
      // this.banquesBox.setModel(new SimpleListModel(banques));

      banquesDeRecherche = new ArrayList<>();

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToStaticMode(){
      final boolean isEmptyObj = this.recherche.equals(new Recherche());
      super.switchToStaticMode(isEmptyObj);

      //On ouvre la liste de recherches
      setOpenListeRecherche(true);

      getBinder().loadComponent(self);
   }

   /**
    * Change mode de la fiche en mode edition.
    */
   @Override
   public void switchToEditMode(){
      super.switchToEditMode();

      this.affichages =
         ManagerLocator.getAffichageManager().findByBanqueManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
      Collections.sort(affichages);
      this.affichagesBox.setModel(new SimpleListModel<>(affichages));

      this.requetes =
         ManagerLocator.getRequeteManager().findByBanqueManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
      Collections.sort(requetes);
      this.requetesBox.setModel(new SimpleListModel<>(requetes));

      final Utilisateur user = (Utilisateur) sessionScope.get("User");
      final Entite entiteRequete = ManagerLocator.getEntiteManager().findByNomManager("Requete").get(0);
      this.banques = ManagerLocator.getBanqueManager().findByEntiteConsultByUtilisateurManager(user, entiteRequete,
         SessionUtils.getPlateforme(sessionScope));
      Collections.sort(banques);
      // this.banquesBox.setModel(new SimpleListModel(banques));

      //On sélectionne l'affichage
      if(recherche.getAffichage() != null){
         //On recherche l'index de l'affichage
         int index = -1;
         for(int i = 0; i < this.affichages.size(); i++){
            if(this.affichages.get(i).equals(recherche.getAffichage())){
               index = i;
               break;
            }
         }
         if(index != -1){
            this.affichagesBox.setSelectedIndex(index);
         }
      }
      //On sélectionne la requête
      if(recherche.getRequete() != null){
         //On recherche l'index de la requete
         int index = -1;
         for(int i = 0; i < this.requetes.size(); i++){
            if(this.requetes.get(i).equals(recherche.getRequete())){
               index = i;
               break;
            }
         }
         if(index != -1){
            this.requetesBox.setSelectedIndex(index);
         }
      }

      //		List<Banque> banks = ManagerLocator.getRechercheManager()
      //				.findBanquesManager(recherche);
      //On sélectionne les banques
      //		if (banks != null) {
      //			//On recherche l'index des banques
      //			List<Integer> index = new ArrayList<Integer>();
      //			for (int j = 0; j < banks.size(); j++) {
      //				Banque collection = banks.get(j);
      //				for (int i = 0; i < this.banques.size(); i++) {
      //					if (this.banques.get(i).equals(collection)) {
      //						index.add(i);
      //						break;
      //					}
      //				}
      //			}
      //			if (index != null) {
      //				Set<Listitem> items = new HashSet<Listitem>();
      //				for (int i = 0; i < index.size(); i++) {
      //					items.add(banquesBox.getItemAtIndex(index.get(i)));
      //				}
      //				this.banquesBox.setSelectedItems(items);
      //			}
      //		}

      getBinder().loadComponent(self);
   }

   @Override
   public void clearData(){
      clearConstraints();
      super.clearData();
   }

   @Override
   public void createNewObject(){
      try{
         setEmptyToNulls();
         /*if (banquesBox.getSelectedItems().size() <= 0) {
         	throw new RuntimeException(Labels
         			.getLabel("recherche.validation.containsBanque"));
         } else {
         	setObject(saveRecherche(SessionUtils
         			.getLoggedUser(sessionScope)));
         }*/
         setObject(saveRecherche(SessionUtils.getLoggedUser(sessionScope)));
      }catch(final Exception e){
         throw new RuntimeException(e.getMessage());
      }
   }

   @Override
   public void onClick$addNewC(){
      switchToCreateMode();
   }

   @Override
   public void onClick$cancelC(){
      clearData();
   }

   @Override
   public void onClick$createC(){
      if(recherche.getAffichage() == null){
         throw new WrongValueException(affichagesBox, Labels.getLabel("validation.syntax.empty"));
      }
      if(recherche.getRequete() == null){
         throw new WrongValueException(requetesBox, Labels.getLabel("validation.syntax.empty"));
      }
      Clients.showBusy(Labels.getLabel("recherche.creation.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   @Override
   public void onLaterCreate(){
      try{
         createNewObject();
         cloneObject();
         disableToolBar(false);

         if(getListeRecherches() != null){
            // ajout de l'affichage à la liste
            getListeRecherches().addToObjectList(this.recherche);
         }
         clearData();
         getObjectTabController().getTopController().resetExecutionController();
         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void onClick$deleteC(){
      if(this.recherche != null){

         if(Messagebox.show(
            ObjectTypesFormatters.getLabel("message.deletion.message",
               new String[] {Labels.getLabel("message.deletion.recherche")}),
            Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){

            // suppression de la recherche
            ManagerLocator.getRechercheManager().removeObjectManager(this.recherche);

            if(getListeRecherches() != null){
               // on enlève la recherche de la liste
               getListeRecherches().removeObjectAndUpdateList(this.recherche);
            }
            clearData();
         }

      }
   }

   @Override
   public void onClick$editC(){
      if(this.recherche != null){
         switchToEditMode();
      }
   }

   @Override
   public void onClick$revertC(){
      revertRecherche();
      clearConstraints();
      super.onClick$revertC();
   }

   @Override
   public void onClick$validateC(){
      if(recherche.getAffichage() == null){
         throw new WrongValueException(affichagesBox, Labels.getLabel("validation.syntax.empty"));
      }
      if(recherche.getRequete() == null){
         throw new WrongValueException(requetesBox, Labels.getLabel("validation.syntax.empty"));
      }

      Clients.showBusy(Labels.getLabel("recherche.creation.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public void onLaterUpdate(){
      try{
         updateObject();
         cloneObject();
         switchToStaticMode();
         setObject(recherche);

         if(getListeRecherches() != null){
            // update de l'objet à la liste
            getListeRecherches().updateObjectGridList(this.recherche);
         }
         // ferme wait message
         Clients.clearBusy();
         switchToStaticMode();
         getObjectTabController().getTopController().resetExecutionController();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   public void onSelect$tabResultats(){
      //On ferme la liste des recherches
      /*setOpenListeRecherche(false);
      //On execute la recherche
      prepareResultats();*/
   }

   @Override
   public void updateObject(){

      try{
         setEmptyToNulls();
         /*if (banquesBox.getSelectedItems().size() <= 0) {
         	throw new RuntimeException(Labels
         			.getLabel("recherche.validation.containsBanque"));
         } else {
         	updateRecherche(SessionUtils.getLoggedUser(sessionScope));
         }*/
         updateRecherche(SessionUtils.getLoggedUser(sessionScope));
      }catch(final Exception e){
         throw new RuntimeException(e.getMessage());
      }
   }

   @Override
   public void setFieldsToUpperCase(){}

   public void revertRecherche(){
      recherche.setAffichage(((Recherche) getClone()).getAffichage());
      recherche.setBanques(((Recherche) getClone()).getBanques());
      recherche.setCreateur(((Recherche) getClone()).getCreateur());
      recherche.setIntitule(((Recherche) getClone()).getIntitule());
      recherche.setRequete(((Recherche) getClone()).getRequete());
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(intituleBox);
   }

   @Override
   public void setEmptyToNulls(){
      if(this.recherche.getIntitule().equals("")){
         this.recherche.setIntitule(null);
      }
   }

   public void onSelect$affichagesBox(){
      this.recherche.setAffichage(affichages.get(affichagesBox.getSelectedIndex()));
      Clients.clearWrongValue(affichagesBox);
   }

   public void onSelect$requetesBox(){
      this.recherche.setRequete(requetes.get(requetesBox.getSelectedIndex()));
      Clients.clearWrongValue(requetesBox);
   }

   protected Recherche saveRecherche(final Utilisateur createur){
      //On récupère les banques sélectionées
      final List<Banque> banks = new ArrayList<>();
      banks.add(SessionUtils.getSelectedBanques(sessionScope).get(0));
      /*Set<Listitem> items = banquesBox.getSelectedItems();
      Iterator<Listitem> it = items.iterator();
      while (it.hasNext()) {
      	banks.add(banques.get(it.next().getIndex()));
      }*/
      ManagerLocator.getRechercheManager().createObjectManager(recherche, recherche.getAffichage(), recherche.getRequete(), banks,
         createur, SessionUtils.getSelectedBanques(sessionScope).get(0));
      return recherche;
   }

   protected void updateRecherche(final Utilisateur createur){
      //On récupère les banques sélectionées
      final List<Banque> banks = new ArrayList<>();
      banks.add(SessionUtils.getSelectedBanques(sessionScope).get(0));
      /*Set<Listitem> items = banquesBox.getSelectedItems();
      Iterator<Listitem> it = items.iterator();
      while (it.hasNext()) {
      	banks.add(banques.get(it.next().getIndex()));
      }*/
      ManagerLocator.getRechercheManager().updateObjectManager(recherche, recherche.getAffichage(), recherche.getRequete(), banks,
         createur, SessionUtils.getSelectedBanques(sessionScope).get(0));

   }

   /*************************************************/
   /** ACCESSEURS **/
   /*************************************************/
   public Listbox getRequetesBox(){
      return requetesBox;
   }

   public Listbox getAffichagesBox(){
      return affichagesBox;
   }

   public List<Requete> getRequetes(){
      return requetes;
   }

   public void setRequetes(final List<Requete> reqs){
      this.requetes = reqs;
   }

   public List<Affichage> getAffichages(){
      return affichages;
   }

   public void setAffichages(final List<Affichage> affs){
      this.affichages = affs;
   }

   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final List<Banque> bs){
      this.banques = bs;
   }

   public List<Banque> getBanquesDeRecherche(){
      return banquesDeRecherche;
   }

   public void setBanquesDeRecherche(final List<Banque> banquesDeRech){
      this.banquesDeRecherche = banquesDeRech;
   }

   public ObjetsAffichageRenderer getObjAffRenderer(){
      return objAffRenderer;
   }

   /**
    * Recupere le controller du composant representant la liste associee
    * a l'entite de domaine a partir de l'evenement.
    * @return controller ListeRecherches
    */
   public ListeRecherches getListeRecherches(){
      return getObjectTabController().getListeRecherches();
   }

   /**
    * Ouvre ou ferme la liste de Recherches.
    */
   private void setOpenListeRecherche(final boolean open){
      //On ferme la liste des recherches
      ((org.zkoss.zul.West) self.getParent().getParent().getParent().getParent().getFellow("listeRegion")).setOpen(open);
   }

   public void drawActionsForRecherche(){
      if(!sessionScope.containsKey("ToutesCollections")){
         drawActionsButtons("Requete");
      }else{
         setCanNew(false);
         setCanEdit(false);
         setCanDelete(false);
      }
   }

   private final ConstWord constr = new ConstWord();
   {
      constr.setNullable(false);
   }

   public ConstWord getConstr(){
      return constr;
   }

   public Recherche getRecherche(){
      return recherche;
   }

   public String getBanquesValue(){
      final StringBuffer sb = new StringBuffer();
      if(banquesDeRecherche != null){
         for(int i = 0; i < banquesDeRecherche.size(); i++){
            sb.append(banquesDeRecherche.get(i).getNom());

            if(i < banquesDeRecherche.size() - 1){
               sb.append(", ");
            }else{
               sb.append(".");
            }
         }
      }
      return sb.toString();
   }

   @Override
   public void setFocusOnElement(){}

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
   public void removeObject(final String comments){}

   @Override
   public void setParentObject(final TKdataObject obj){}
}
