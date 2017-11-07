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

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 * 
 * Controller pour la page sur les collaborations : collaborateurs,
 * services et établissements.
 * Controller créé le 16/12/2009.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class CollaborationsController extends AbstractObjectTabController {

	private static final long serialVersionUID = 3258354045207168273L;
	
	private Div divCollaborateur;
	private Div divService;
	private Div divEtablissement;
	private String from;
	
	// Mode des fiches
	private String mode = "modification";
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		getFicheEtablissement().setObjectTabController(this);
		getFicheService().setObjectTabController(this);
		getFicheCollaborateur().setObjectTabController(this);
		
		drawActionsForCollaborations();
		
		setStaticEditMode(false);
	}
	
	@Override
	public TKdataObject loadById(Integer id) {
		return null;
	}
	
	/**
	 * Recupere le controller du composant representant la fiche associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche FicheCollaborateur
	 */
	public FicheCollaborateur getFicheCollaborateur() {
		return ((FicheCollaborateur) 
				this.self.getFellow("ficheCollaborateur")
				.getFellow("fwinCollaborateur")
				.getAttributeOrFellow("fwinCollaborateur$composer", true));
	}
	
	/**
	 * Recupere le controller du composant representant la fiche associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche FicheEtablissement
	 */
	public FicheEtablissement getFicheEtablissement() {
		
		return ((FicheEtablissement) 
				this.self.getFellow("ficheEtablissement")
				.getFellow("fwinEtablissement")
				.getAttributeOrFellow("fwinEtablissement$composer", true));
	}
	
	/**
	 * Recupere le controller du composant representant la fiche associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche FicheService
	 */
	public FicheService getFicheService() {
		return ((FicheService) 
				this.self.getFellow("ficheService")
				.getFellow("fwinService")
				.getAttributeOrFellow("fwinService$composer", true));
	}
	
	/**
	 * Recupere le controller du composant representant la liste des
	 * collaborations.
	 * @param event Event
	 * @return fiche FicheCollaborateur
	 */
	public ListeCollaborations getListeCollaborations() {
		return ((ListeCollaborations) 
				this.self.getFellow("listeCollaborations")
				.getFellow("lwinCollaborations")
				.getAttributeOrFellow("lwinCollaborations$composer", true));
	}
	
	/**
	 * Passe la liste des collaborations en mode de sélection : l'utilisateur
	 * pourra sélectionner un élément et l'envoyer vers une autre page.
	 * @param type Classe de l'objet à sélectionner.
	 * @param from Path de la page vers laquelle renvoyer l'objet.
	 * @param filtres Liste d'objets (services, établissements...) sur lesquels
	 * @param oldSelection Liste des objets qui étaient sélectionnés.
	 * il faut réaliser un filtre lors de l'ouverture de la fenêtre.
	 */
	public void switchToSelectMode(String type, String _from,
			List<Object> filtres, List<Object> oldSelection) {
		mode = "select";
		from = _from;
		getListeCollaborations().initTree();
		getListeCollaborations().swithToSelectionMode(
				type, from, filtres, oldSelection);
		switchToOnlyListeMode();
	}
	
	@Override
	public void switchToFicheAndListeMode() {
		if (listeRegion.getWidth().equals("100%")) {
			listeRegion.setWidth("45%");
		}
		listeRegion.setOpen(true);
	}
	
	/**
	 * Passe les collaborations en mode d'affichage "details".
	 */
	public void switchToDetailMode() {
		mode = "details";
		getListeCollaborations().initTree();
		getListeCollaborations().switchToDetailMode();
	}
	
	public void switchToOnlyFicheMode() {
		listeRegion.setOpen(false);
	}
	
	/**
	 * Affiche les infos d'un collab dans sa FicheCollaborateur.
	 * @param collab Collaborateur à afficher.
	 */
	public void switchToFicheCollaborateurMode(Collaborateur collab) {
		switchToFicheAndListeMode();
		divCollaborateur.setVisible(true);
		divEtablissement.setVisible(false);
		divService.setVisible(false);
		
		getFicheCollaborateur().setCollaborateur(collab);
		getFicheCollaborateur().switchToStaticMode(getMode());
	}
	
	/**
	 * Affiche la FicheCollaborateur en mode création.
	 * @param service Service du collaborateur.
	 */
	public void switchToFicheCollaborateurCreationMode(Service service) {
		switchToFicheAndListeMode();
		divCollaborateur.setVisible(true);
		divEtablissement.setVisible(false);
		divService.setVisible(false);
		
		getFicheCollaborateur().switchToCreateMode(service);
	}
	
	/**
	 * Affiche les infos d'un étab dans sa FicheEtablissement.
	 * @param collab Etablissement à afficher.
	 */
	public void switchToFicheEtablissementMode(Etablissement etab) {
		switchToFicheAndListeMode();
		divCollaborateur.setVisible(false);
		divEtablissement.setVisible(true);
		divService.setVisible(false);
		
		getFicheEtablissement().setEtablissement(etab);
		getFicheEtablissement().switchToStaticMode(getMode());
	}
	
	/**
	 * Affiche la FicheEtablissement en mode de création.
	 */
	public void switchToFicheEtablissementCreationMode() {
		switchToFicheAndListeMode();
		divCollaborateur.setVisible(false);
		divEtablissement.setVisible(true);
		divService.setVisible(false);
		
		getFicheEtablissement().switchToCreateMode();
	}
	
	/**
	 * Affiche les infos d'un service dans sa FicheService.
	 * @param collab Service à afficher.
	 */
	public void switchToFicheServiceMode(Service serv) {
		switchToFicheAndListeMode();
		divCollaborateur.setVisible(false);
		divEtablissement.setVisible(false);
		divService.setVisible(true);
		
		getFicheService().setService(serv);
		getFicheService().switchToStaticMode(getMode());
	}
	
	/**
	 * Affiche la FicheService en mode création.
	 */
	public void switchToFicheServiceCreationMode(Etablissement etab) {
		switchToFicheAndListeMode();
		divCollaborateur.setVisible(false);
		divEtablissement.setVisible(false);
		divService.setVisible(true);
		
		getFicheService().switchToCreateMode(etab);
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String m) {
		this.mode = m;
	}
	
	public void drawActionsForCollaborations() {
		getFicheCollaborateur().drawActionsForCollaborateur();
		getFicheEtablissement().drawActionsForEtablissement();
		getFicheService().drawActionsForService();
		getFicheCollaborateur().clearData();
		getFicheEtablissement().clearData();
		getFicheService().clearData();
		getListeCollaborations().generateDroits();
	}

	@Override
	public FicheAnnotation getFicheAnnotation() {
		return null;
	}

	@Override
	public AbstractFicheCombineController getFicheCombine() {
		return null;
	}

	@Override
	public AbstractFicheEditController getFicheEdit() {
		return null;
	}

	@Override
	public AbstractFicheModifMultiController getFicheModifMulti() {
		return null;
	}

	@Override
	public AbstractFicheStaticController getFicheStatic() {
		return null;
	}

	@Override
	public AbstractListeController2 getListe() {
		return null;
	}
}
