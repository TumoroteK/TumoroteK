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
package fr.aphp.tumorotek.action.contexte;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.administration.AdministrationController;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.xml.CoupleValeur;
import fr.aphp.tumorotek.manager.impl.xml.EnteteListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneDeuxColonnesParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.LigneListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.ListeElement;
import fr.aphp.tumorotek.manager.impl.xml.Paragraphe;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @version 2.2.1
 * @author Mathieu BARTHELEMY
 *
 */
public class FichePlateforme extends AbstractFicheCombineController
{

	private final Log log = LogFactory.getLog(FichePlateforme.class);

	private static final long serialVersionUID = -2844832651429510018L;

	// Static Components pour le mode static.
	private Label nomLabel;
	private Label aliasLabel;
	private Label respLabel;
	private Menubar menuBar;

	// Editable components : mode d'édition ou de création.
	private Label nomRequired;
	private Textbox nomBox;
	private Textbox aliasBox;
	private Div respBoxDiv;
	private Combobox collabBox;
	private Column deleteAdminColumn;
	private Row rowAddAdminTitle;
	private Row rowAddAdmin;
	private Listbox usersBox;
	private Button addAdminButton;
	private Grid adminsGrid;

	// conteneurs
	private Group groupConteneurs;
	private Div conteneursAssocies;

	// Objets Principaux.
	private Plateforme plateforme;
	private List<String> nomsAndPrenoms = new ArrayList<>();
	private List<Collaborateur> collaborateurs = new ArrayList<>();
	private Collaborateur selectedCollaborateur;
	private List<Utilisateur> administrateurs = new ArrayList<>();
	private List<Utilisateur> users = new ArrayList<>();
	private Utilisateur selectedUser;
	private List<Banque> banques = new ArrayList<>();
	private List<ConteneurDecorator> conteneurs = new ArrayList<>();
	private final List<ConteneurDecorator> copyConteneurs = new ArrayList<>();

	private String hautPage;
	private String piedPage;

	// TK-252
	private List<Utilisateur> superadmins = new ArrayList<>();

	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);

		setDeletable(true);
		setDeletionMessage("message.deletion.plateforme");
		setCascadable(false);
		setFantomable(true);

		// Initialisation des listes de composants
		setObjLabelsComponents(new Component[] {this.nomLabel, this.aliasLabel, this.respLabel});

		setObjBoxsComponents(new Component[] {this.nomBox, this.aliasBox, this.respBoxDiv, this.deleteAdminColumn, this.rowAddAdmin,
				this.rowAddAdminTitle});

		setRequiredMarks(new Component[] {this.nomRequired});

		drawActionsForPlateforme();

		menuBar.setVisible(false);

		if(winPanel != null){
			winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
		}

		// passe les refrences des group headers
		Executions.createComponents("/zuls/contexte/ConteneursAssocies.zul", conteneursAssocies, null);
		getConteneursAssocies().setGroupHeader(groupConteneurs);
		getConteneursAssocies().setConteneursDeBanque(false);

		// TK-252
		// collecte les superadmins
		superadmins.addAll(ManagerLocator.getUtilisateurManager()
				.findBySuperAndArchiveManager(false, true)); 

		// init des collaborateurs
		collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
		nomsAndPrenoms.clear();
		for(int i = 0; i < collaborateurs.size(); i++){
			nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
		}
		collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));

		getBinder().loadAll();
	}

	@Override
	public void setObject(final TKdataObject obj){
		this.plateforme = (Plateforme) obj;
		conteneurs.clear();

		selectedCollaborateur = this.plateforme.getCollaborateur();
		if(this.plateforme.getCollaborateur() != null && collaborateurs.contains(this.plateforme.getCollaborateur())){
			selectedCollaborateur = this.plateforme.getCollaborateur();
			collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
		}else if(this.plateforme.getCollaborateur() != null){
			collaborateurs.add(this.plateforme.getCollaborateur());
			selectedCollaborateur = this.plateforme.getCollaborateur();
			nomsAndPrenoms.add(this.plateforme.getCollaborateur().getNomAndPrenom());
			collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));
			collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
		}else{
			collabBox.setValue("");
			selectedCollaborateur = null;
		}

		administrateurs = new ArrayList<>();
		banques = new ArrayList<>();
		if(this.plateforme.getPlateformeId() != null){
			// init des admins
			final Iterator<Utilisateur> it = ManagerLocator.getPlateformeManager().getUtilisateursManager(plateforme).iterator();
			while(it.hasNext()){
				administrateurs.add(it.next());
			}

			// init des banques
			banques = ManagerLocator.getBanqueManager().findByPlateformeAndArchiveManager(plateforme, null);

			getConteneursAssocies().setPlateforme(plateforme);
			conteneurs = ConteneurDecorator
					.decorateListe(ManagerLocator.getConteneurManager().findByPlateformeOrigWithOrderManager(plateforme), plateforme);
			conteneurs.addAll(ConteneurDecorator
					.decorateListe(ManagerLocator.getConteneurManager().findByPartageManager(plateforme, true), plateforme));
		}

		getConteneursAssocies().setObjects(conteneurs);
		super.setObject(plateforme);
	}

	@Override
	public void cloneObject(){
		setClone(this.plateforme.clone());
		copyConteneurs.clear();
		for(int i = 0; i < conteneurs.size(); i++){
			copyConteneurs.add(conteneurs.get(i).clone());
		}
	}

	@Override
	public void revertObject(){
		super.revertObject();
		setConteneurs(getCopyConteneurs());
	}

	@Override
	public Plateforme getObject(){
		return this.plateforme;
	}

	@Override
	public TKdataObject getParentObject(){
		return null;
	}

	@Override
	public void setParentObject(final TKdataObject obj){}

	@Override
	public PlateformeController getObjectTabController(){
		return (PlateformeController) super.getObjectTabController();
	}

	@Override
	public void setNewObject(){		
		setObject(new Plateforme());
		super.setNewObject();
	}

	@Override
	public void switchToCreateMode(){
		// Initialisation du mode (listes, valeurs...)
		initEditableMode();

		super.switchToCreateMode();
		
		// impossible d'associer des conteneurs 
		// en mode création
		getConteneursAssocies().switchToEditMode(false);
		
		// ajout par défaut de l'utilisateur courant comme admin PF
		if (!SessionUtils.getLoggedUser(sessionScope).isSuperAdmin()) {
			administrateurs.add(((Utilisateur) SessionUtils.getLoggedUser(sessionScope)));
			
			// suppr de la liste des users possibles
			if (users.contains(SessionUtils.getLoggedUser(sessionScope))) {
				selectedUser = users.get(users.indexOf(SessionUtils.getLoggedUser(sessionScope)));
				onClick$addAdminButton();
			}
		}

		getBinder().loadComponent(self);
	}

	@Override
	public void switchToStaticMode(){
		super.switchToStaticMode(this.plateforme.equals(new Plateforme()));

		// addNewC.setVisible(false);
		// deleteC.setVisible(false);

		if(this.plateforme.getPlateformeId() == null){
			menuBar.setVisible(false);
		}else{
			menuBar.setVisible(true);
		}

		getConteneursAssocies().switchToStaticMode();

		getBinder().loadComponent(self);
	}

	@Override
	public void switchToEditMode(){
		// Initialisation du mode (listes, valeurs...)
		initEditableMode();

		super.switchToEditMode();

		getConteneursAssocies().switchToEditMode(true);

		getBinder().loadComponent(self);
	}

	@Override
	public void setFocusOnElement(){
		nomBox.setFocus(true);
	}

	@Override
	public void setFieldsToUpperCase(){}

	@Override
	public void clearData(){
		clearConstraints();

		selectedCollaborateur = null;
		collabBox.setValue(null);
		administrateurs.clear();
		banques.clear();
		conteneurs.clear();
		getConteneursAssocies().setObjects(conteneurs);

		super.clearData();
	}

	/**
	 * Recupere le controller du composant representant la liste associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche ListePlateforme
	 */
	private ListePlateforme getListePlateforme(){
		return getObjectTabController().getListe();
	}

	@Override
	public void onClick$cancelC(){
		clearData();
	}

	@Override
	public boolean prepareDeleteObject(){
		final boolean isReferenced = ManagerLocator.getPlateformeManager().isReferencedObjectManager(getObject());
		setDeleteMessage(
				ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
		if(isReferenced){
			setDeleteMessage(Labels.getLabel("plateforme.deletion.isReferenced"));
		}
		setDeletable(!isReferenced);
		setFantomable(!isReferenced);
		return false;
	}

	@Override
	public void removeObject(final String comments){
		ManagerLocator.getPlateformeManager()
		.removeObjectManager(getObject(), comments, SessionUtils.getLoggedUser(sessionScope),
				SessionUtils.getSystemBaseDir());
		
		// refresh session logged user if needed
		if (administrateurs.contains(SessionUtils.getLoggedUser(sessionScope))) {
			sessionScope.put("User", ManagerLocator.getUtilisateurManager()
					.findByIdManager(SessionUtils.getLoggedUser(sessionScope).getUtilisateurId()));
		}
	}

	@Override
	public void createNewObject(){

		// on remplit la plateforme en fonction des champs nulls
		setEmptyToNulls();

		// Gestion du collaborateur
		final String selectedNomAndPremon = this.collabBox.getValue().toUpperCase();
		this.collabBox.setValue(selectedNomAndPremon);
		final int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
		if(ind > -1){
			selectedCollaborateur = collaborateurs.get(ind);
		}else{
			selectedCollaborateur = null;
		}

		plateforme = ManagerLocator.getPlateformeManager()
			.createObjectManager(plateforme, selectedCollaborateur, administrateurs,
				SessionUtils.getLoggedUser(sessionScope), SessionUtils.getSystemBaseDir());
		
		// refresh session logged user if needed
		if (administrateurs.contains(SessionUtils.getLoggedUser(sessionScope))) {
			sessionScope.put("User", ManagerLocator.getUtilisateurManager()
					.findByIdManager(SessionUtils.getLoggedUser(sessionScope).getUtilisateurId()));
		}

		// ajout de la banque à la liste
		getObjectTabController().getListe().addToObjectList(plateforme);
		getObjectTabController().getListe().selectFirstObjet();
	}

	@Override
	public String getDeleteWaitLabel(){
		return null;
	}

	@Override
	public void onClick$editC(){
		if(this.plateforme != null){
			switchToEditMode();
		}
	}

	@Override
	public void onClick$revertC(){
		clearConstraints();
		super.onClick$revertC();
	}

	@Override
	public void onClick$validateC(){
		Clients.showBusy(Labels.getLabel("plateforme.creation.encours"));
		Events.echoEvent("onLaterUpdate", self, null);
	}

	public void onLaterUpdate(){
		try{
			updateObject();
			cloneObject();
			switchToStaticMode();

			if(getListePlateforme() != null){
				getListePlateforme().updateObjectGridList(this.plateforme);
			}
			// ferme wait message
			Clients.clearBusy();
		}catch(final RuntimeException re){
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}

	@Override
	public void setEmptyToNulls(){
		if(this.plateforme.getAlias() != null && this.plateforme.getAlias().equals("")){
			this.plateforme.setAlias(null);
		}
	}

	@Override
	public void updateObject(){
		// on remplit la plateforme en fonction des champs nulls
		setEmptyToNulls();

		// Gestion du collaborateur
		final String selectedNomAndPremon = this.collabBox.getValue().toUpperCase();
		this.collabBox.setValue(selectedNomAndPremon);
		final int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
		if(ind > -1){
			selectedCollaborateur = collaborateurs.get(ind);
		}else{
			selectedCollaborateur = null;
		}

		// update de l'objet
		ManagerLocator.getPlateformeManager().updateObjectManager(plateforme, selectedCollaborateur, administrateurs,
				ConteneurDecorator.extractConteneursFromDecos(conteneurs), SessionUtils.getLoggedUser(sessionScope));
	}

	/**
	 * Méthode pour l'initialisation du mode d'édition : récupération du contenu
	 * des listes déroulantes (types, qualités...).
	 */
	public void initEditableMode(){


		// init des users
		final List<Plateforme> pfs =
				ManagerLocator.getUtilisateurManager()
				.getAvailablePlateformesManager(SessionUtils.getLoggedUser(sessionScope));
		if(!pfs.isEmpty()){
			users = ManagerLocator.getUtilisateurManager().findByArchiveManager(false, pfs);
		}
		users.add(0, null);
		// on enlève de cette les admins
		for(int i = 0; i < administrateurs.size(); i++){
			if(users.contains(administrateurs.get(i))){
				users.remove(administrateurs.get(i));
			}
		}
		selectedUser = null;

		menuBar.setVisible(false);
	}

	/**
	 * Méthode vidant tous les messages d'erreurs apparaissant dans
	 * les contraintes de la fiche.
	 */
	public void clearConstraints(){
		Clients.clearWrongValue(nomBox);
		Clients.clearWrongValue(aliasBox);
		Clients.clearWrongValue(collabBox);
		Clients.clearWrongValue(usersBox);
		Clients.clearWrongValue(addAdminButton);
	}

	/**
	 * Rend les boutons d'actions cliquables ou non.
	 */
	public void drawActionsForPlateforme(){
		setCanEdit(true);
		setCanSeeHistorique(true);
		setCanNew(true);
		setCanDelete(true);
	}

	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien
	 * collabAideSaisieUser. Cette méthode va créer une nouvelle
	 * fenêtre contenant l'aide pour la sélection d'un collaborateur.
	 */
	public void onClick$collabAideSaisie(){
		// on récupère le collaborateur actuellement sélectionné
		// pour l'afficher dans la modale
		final List<Object> old = new ArrayList<>();
		if(selectedCollaborateur != null){
			old.add(selectedCollaborateur);
		}

		// ouvre la modale
		openCollaborationsWindow(page, "general.recherche", "select", null, "Collaborateur", null, Path.getPath(self), old);

	}

	/**
	 * Méthode appelée par la fenêtre CollaborationsController quand
	 * l'utilisateur sélectionne un collaborateur.
	 * @param e Event contenant le collaborateur sélectionné.
	 */
	public void onGetObjectFromSelection(final Event e){

		// les collaborateurs peuvent être modifiés dans la fenêtre
		// d'aide => maj de ceux-ci
		nomsAndPrenoms = new ArrayList<>();
		collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
		for(int i = 0; i < collaborateurs.size(); i++){
			nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
		}

		if(this.plateforme.getCollaborateur() != null && !collaborateurs.contains(this.plateforme.getCollaborateur())){
			collaborateurs.add(this.plateforme.getCollaborateur());
			nomsAndPrenoms.add(this.plateforme.getCollaborateur().getNomAndPrenom());
		}
		collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));

		// si un collaborateur a été sélectionné
		if(e.getData() != null){
			final Collaborateur coll = (Collaborateur) e.getData();
			if(nomsAndPrenoms.contains(coll.getNomAndPrenom())){
				final int ind = nomsAndPrenoms.indexOf(coll.getNomAndPrenom());
				selectedCollaborateur = collaborateurs.get(ind);
				collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
			}
		}
	}

	public void onSelect$usersBox(){
		if(selectedUser != null){
			Clients.clearWrongValue(usersBox);
		}
	}

	/**
	 * Ajout d'un admin.
	 */
	public void onClick$addAdminButton(){
		// max 3 admins par plateforme
		if(administrateurs.size() < 3){
			if(selectedUser == null){
				throw new WrongValueException(usersBox, Labels.getLabel("plateforme.add.null.user.error"));
			}
			if(!administrateurs.contains(selectedUser)){
				administrateurs.add(selectedUser);

				users.remove(selectedUser);
				selectedUser = null;

				// maj des composants
				getBinder().loadComponent(adminsGrid);
				getBinder().loadComponent(usersBox);
			}
		}else{
			throw new WrongValueException(addAdminButton, Labels.getLabel("plateforme.error.max.admin"));
		}
	}

	/**
	 * Suppression d'un admin.
	 * @version 2.2.1
	 */
	public void onClick$deleteAdmin(final Event event){
		final Utilisateur adminToDelete = (Utilisateur) event.getData();

		if(administrateurs.contains(adminToDelete)){
			administrateurs.remove(adminToDelete);
		}

		if(!users.contains(adminToDelete)){
			users.add(adminToDelete);
			selectedUser = null;
		}

		// maj des composants
		Clients.clearWrongValue(addAdminButton);
		getBinder().loadComponent(adminsGrid);
		getBinder().loadComponent(usersBox);
	}

	public AdministrationController getAdministrationController(){
		return (AdministrationController) self.getParent().getParent().getParent().getParent().getParent().getParent().getParent()
				.getParent().getParent().getAttributeOrFellow("winAdministration$composer", true);
	}

	/**
	 * Clic sur un admin pour afficher sa fiche.
	 * @version 2.2.1
	 */
	public void onClick$adminLogin(final Event event){
		final Utilisateur adminToShow = (Utilisateur) event.getData();

		getAdministrationController().selectUtilisateurInController(adminToShow);
	}

	/**
	 * Clic sur une banque pour afficher sa fiche.
	 * @version 2.1
	 */
	public void onClick$banqueNom(final Event event){
		// Banque banque = (Banque) AbstractListeController2
		//	.getBindingData((ForwardEvent) event, false);
		// since 2.1 template row rendering
		final Banque banque = (Banque) event.getData();

		getAdministrationController().selectBanqueInController(banque);
	}

	/**
	 * Clic sur le bouton print.
	 */
	public void onClick$print(){
		openHeadersWindow(page, self);
	}

	public void onGetHeaders(final Event event){
		if(event.getData() != null){
			final String[] headers = (String[]) event.getData();
			if(headers[0] != null){
				hautPage = headers[0];
			}else{
				hautPage = null;
			}

			if(headers[1] != null){
				piedPage = headers[1];
			}else{
				piedPage = null;
			}
		}else{
			hautPage = null;
			piedPage = null;
		}
		Clients.showBusy(Labels.getLabel("impression.encours"));
		Events.echoEvent("onLaterPrint", self, null);
	}

	/**
	 * Génère la fiche et la télécharge.
	 */
	public void onLaterPrint(){
		// création du document XML contenant les données à imprimer
		final Document document = createDocumentXML();

		// Transformation du document en fichier
		byte[] dl = null;
		try{
			dl = ManagerLocator.getXmlUtils().creerPdf(document);

		}catch(final Exception e){
			log.error(e);
		}

		// ferme wait message
		Clients.clearBusy();

		// génération du nom du fichier
		final StringBuffer sb = new StringBuffer();
		final Calendar cal = Calendar.getInstance();
		final String date = new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
		sb.append("fiche_plateforme");
		sb.append(date);
		sb.append(".pdf");

		// envoie du fichier à imprimer à l'utilisateur
		if(dl != null){
			Filedownload.save(dl, "application/pdf", sb.toString());
			dl = null;
		}
	}

	/**
	 * Génère le Document JDOM contenant les infos à imprimer.
	 * @return Document JDOM.
	 */
	public Document createDocumentXML(){

		final Document document = ManagerLocator.getXmlUtils().createJDomDocument();
		final Element root = document.getRootElement();

		// ajout de la date en pied de page
		final StringBuffer sb = new StringBuffer();
		final Calendar cal = Calendar.getInstance();
		final String date = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
		sb.append(date);

		if(piedPage != null && piedPage.length() > 0){
			sb.append(" - ");
			sb.append(piedPage);
		}

		if(hautPage == null || hautPage.length() == 0){
			hautPage = " ";
		}

		ManagerLocator.getXmlUtils().addBasDePage(root, sb.toString());
		ManagerLocator.getXmlUtils().addHautDePage(root, hautPage, false, null);

		final Element page1 = ManagerLocator.getXmlUtils().addPage(root,
				ObjectTypesFormatters.getLabel("impression.plateforme.title", new String[] {plateforme.getNom()}));
		addInfosPlateformeToPrint(page1);
		addInfosListeUtilisateurs(page1);
		addInfosListeBanques(page1);
		addInfosConteneursToPrint(page1);

		return document;
	}

	/**
	 * Ajout les infos générales à imprimer.
	 * @param page
	 */
	public void addInfosPlateformeToPrint(final Element page){
		// Nom
		String tmp = "";
		if(plateforme.getNom() != null){
			tmp = plateforme.getNom();
		}else{
			tmp = "-";
		}
		final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("Champ.Plateforme.Nom"), tmp);
		// Alias
		if(plateforme.getAlias() != null){
			tmp = plateforme.getAlias();
		}else{
			tmp = "-";
		}
		final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("Champ.Plateforme.Alias"), tmp);
		final LigneParagraphe li1 = new LigneParagraphe("", new CoupleValeur[] {cp1, cp2});

		// Collaborateur
		if(plateforme.getCollaborateur() != null){
			tmp = plateforme.getCollaborateur().getNomAndPrenom();
		}else{
			tmp = "-";
		}
		final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("Champ.Plateforme.Collaborateur"), tmp);
		final LigneDeuxColonnesParagraphe li2 = new LigneDeuxColonnesParagraphe(cp3);

		final Paragraphe par1 = new Paragraphe(null, new Object[] {li1, li2}, null, null, null);
		ManagerLocator.getXmlUtils().addParagraphe(page, par1);
	}

	/**
	 * Crée le bloc contenant une liste d'admins.
	 */
	public void addInfosListeUtilisateurs(final Element page){
		// Entete
		final String[] listeEntete = new String[3];
		listeEntete[0] = Labels.getLabel("utilisateur.login");
		listeEntete[1] = Labels.getLabel("utilisateur.email");
		listeEntete[2] = Labels.getLabel("utilisateur.collaborateur.small");
		final EnteteListe entetes = new EnteteListe(listeEntete);

		// liste des cédés
		final LigneListe[] liste = new LigneListe[administrateurs.size()];
		for(int i = 0; i < administrateurs.size(); i++){
			final String[] valeurs = new String[3];
			String tmp = "";
			// Login
			valeurs[0] = administrateurs.get(i).getLogin();
			// email
			if(administrateurs.get(i).getEmail() != null){
				tmp = administrateurs.get(i).getEmail();
			}else{
				tmp = "-";
			}
			valeurs[1] = tmp;
			// collaborateur
			if(administrateurs.get(i).getCollaborateur() != null){
				tmp = administrateurs.get(i).getCollaborateur().getNomAndPrenom();
			}else{
				tmp = "-";
			}
			valeurs[2] = tmp;
			final LigneListe ligne = new LigneListe(valeurs);
			liste[i] = ligne;
		}
		ListeElement listeSites = null;
		if(administrateurs.size() > 0){
			listeSites = new ListeElement(null, entetes, liste);
		}

		// ajout du paragraphe
		final StringBuffer sb = new StringBuffer();
		sb.append(Labels.getLabel("plateforme.admins.title"));
		sb.append(" (");
		sb.append(administrateurs.size());
		sb.append(")");
		final Paragraphe par = new Paragraphe(sb.toString(), null, null, null, listeSites);
		ManagerLocator.getXmlUtils().addParagraphe(page, par);
	}

	/**
	 * Crée le bloc contenant une liste d'admins.
	 */
	public void addInfosListeBanques(final Element page){
		// Entete
		final String[] listeEntete = new String[3];
		listeEntete[0] = Labels.getLabel("Champ.Banque.Nom");
		listeEntete[1] = Labels.getLabel("Champ.Banque.Proprietaire");
		listeEntete[2] = Labels.getLabel("Champ.Banque.Collaborateur");
		final EnteteListe entetes = new EnteteListe(listeEntete);

		// liste des cédés
		final LigneListe[] liste = new LigneListe[banques.size()];
		for(int i = 0; i < banques.size(); i++){
			final String[] valeurs = new String[3];
			String tmp = "";
			// Login
			valeurs[0] = banques.get(i).getNom();
			// email
			if(banques.get(i).getProprietaire() != null){
				tmp = banques.get(i).getProprietaire().getNom();
			}else{
				tmp = "-";
			}
			valeurs[1] = tmp;
			// collaborateur
			if(banques.get(i).getCollaborateur() != null){
				tmp = banques.get(i).getCollaborateur().getNomAndPrenom();
			}else{
				tmp = "-";
			}
			valeurs[2] = tmp;
			final LigneListe ligne = new LigneListe(valeurs);
			liste[i] = ligne;
		}
		ListeElement listeSites = null;
		if(banques.size() > 0){
			listeSites = new ListeElement(null, entetes, liste);
		}

		// ajout du paragraphe
		final StringBuffer sb = new StringBuffer();
		sb.append(Labels.getLabel("plateforme.banques.title"));
		sb.append(" (");
		sb.append(banques.size());
		sb.append(")");
		final Paragraphe par = new Paragraphe(sb.toString(), null, null, null, listeSites);
		ManagerLocator.getXmlUtils().addParagraphe(page, par);
	}

	/**
	 * Ajout les infos conteneurs à imprimer.
	 * @param page
	 */
	public void addInfosConteneursToPrint(final Element page){
		// Entete
		final String[] listeEntete = new String[5];
		listeEntete[0] = Labels.getLabel("conteneur.code");
		listeEntete[1] = Labels.getLabel("conteneur.nom");
		listeEntete[2] = Labels.getLabel("conteneur.temp");
		listeEntete[3] = Labels.getLabel("conteneur.service");
		listeEntete[4] = Labels.getLabel("service.etablissement");
		final EnteteListe entetes = new EnteteListe(listeEntete);

		// liste des cédés
		final LigneListe[] liste = new LigneListe[conteneurs.size()];
		for(int i = 0; i < conteneurs.size(); i++){
			final String[] valeurs = new String[5];
			// code
			valeurs[0] = conteneurs.get(i).getConteneur().getCode();
			// nom
			valeurs[1] = conteneurs.get(i).getConteneur().getNom();
			// température
			final StringBuffer sb = new StringBuffer();
			sb.append(conteneurs.get(i).getConteneur().getTemp());
			sb.append("°C");
			valeurs[2] = sb.toString();
			// service
			if(conteneurs.get(i).getConteneur().getService() != null){
				valeurs[3] = conteneurs.get(i).getConteneur().getService().getNom();
			}else{
				valeurs[3] = "-";
			}
			// etablissement
			if(conteneurs.get(i).getConteneur().getService() != null
					&& conteneurs.get(i).getConteneur().getService().getEtablissement() != null){
				valeurs[4] = conteneurs.get(i).getConteneur().getService().getEtablissement().getNom();
			}else{
				valeurs[4] = "-";
			}
			final LigneListe ligne = new LigneListe(valeurs);
			liste[i] = ligne;
		}
		ListeElement listeSites = null;
		if(conteneurs.size() > 0){
			listeSites = new ListeElement(null, entetes, liste);
		}

		// ajout du paragraphe
		final StringBuffer sb = new StringBuffer();
		sb.append(Labels.getLabel("Champ.Banque.Conteneurs"));
		sb.append(" (");
		sb.append(conteneurs.size());
		sb.append(")");
		final Paragraphe par = new Paragraphe(sb.toString(), null, null, null, listeSites);
		ManagerLocator.getXmlUtils().addParagraphe(page, par);
	}

	/*************************************************************************/
	/************************** CONTENEURS************************************/
	/*************************************************************************/
	public List<ConteneurDecorator> getConteneurs(){
		return conteneurs;
	}

	public void setConteneurs(final List<ConteneurDecorator> cts){
		this.conteneurs.clear();
		this.conteneurs.addAll(cts);
	}

	public List<ConteneurDecorator> getCopyConteneurs(){
		return copyConteneurs;
	}

	/**
	 * Renvoie le controller associe au composant permettant la getsion 
	 * des associations one-to-many avec les conteneurs.
	 */
	public ConteneursAssocies getConteneursAssocies(){
		return (ConteneursAssocies) self.getFellow("conteneursAssocies").getFellow("winConteneursAssocies")
				.getAttributeOrFellow("winConteneursAssocies$composer", true);
	}

	/*********************************************************/
	/********************** ACCESSEURS. **********************/
	/*********************************************************/

	public Plateforme getPlateforme(){
		return plateforme;
	}

	public void setPlateforme(final Plateforme p){
		this.plateforme = p;
	}

	public ConstWord getNomConstraint(){
		return ContexteConstraints.getPlateformeNomConstraint();
	}

	public ConstCode getAliasConstraint(){
		return ContexteConstraints.getPlateformeAliasConstraint();
	}

	public String getSClassCollaborateur(){
		if(this.plateforme != null){
			return ObjectTypesFormatters.sClassCollaborateur(this.plateforme.getCollaborateur());
		}
		return "";
	}

	public List<String> getNomsAndPrenoms(){
		return nomsAndPrenoms;
	}

	public void setNomsAndPrenoms(final List<String> n){
		this.nomsAndPrenoms = n;
	}

	public List<Collaborateur> getCollaborateurs(){
		return collaborateurs;
	}

	public void setCollaborateurs(final List<Collaborateur> c){
		this.collaborateurs = c;
	}

	public Collaborateur getSelectedCollaborateur(){
		return selectedCollaborateur;
	}

	public void setSelectedCollaborateur(final Collaborateur s){
		this.selectedCollaborateur = s;
	}

	/**
	 * TK-252
	 * @version 2.2.1
	 * @return superadmins + administrateurs
	 */
	public List<Utilisateur> getAdministrateurs() {
		List<Utilisateur> admins = new ArrayList<Utilisateur>();
		admins.addAll(superadmins);
		admins.addAll(administrateurs);
		return admins;
	}

	public void setAdministrateurs(final List<Utilisateur> admins){
		this.administrateurs = admins;
	}

	public List<Banque> getBanques(){
		return banques;
	}

	public void setBanques(final List<Banque> b){
		this.banques = b;
	}

	public List<Utilisateur> getUsers(){
		return users;
	}

	public void setUsers(final List<Utilisateur> u){
		this.users = u;
	}

	public Utilisateur getSelectedUser(){
		return selectedUser;
	}

	public void setSelectedUser(final Utilisateur s){
		this.selectedUser = s;
	}

	public String getHautPage(){
		return hautPage;
	}

	public void setHautPage(final String p){
		this.hautPage = p;
	}

	public String getPiedPage(){
		return piedPage;
	}

	public void setPiedPage(final String p){
		this.piedPage = p;
	}

}
