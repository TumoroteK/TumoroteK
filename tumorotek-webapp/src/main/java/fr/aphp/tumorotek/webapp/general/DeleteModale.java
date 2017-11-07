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
package fr.aphp.tumorotek.webapp.general;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.constraints.ConstWord;

/**
 * Dessine la fiche modale avertissant l'utilisateur 
 * lors d'une suppression d'objet.
 * Date: 10/11/2010.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class DeleteModale extends GenericForwardComposer<Component> {

	private static final long serialVersionUID = -8704338228969541559L;
	
	private Component parent;
	private String comments;
	
	private Html warnLabel;
	private Image warnImg;
	private Image stopImg;
	private Row commentsRow;
	private Row commentsRowBox;
	private Button delete;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		AnnotateDataBinder binder = new AnnotateDataBinder(comp);	
		binder.loadComponent(comp);
	}
	
	/**
	 * Initialise le composant à partir des paramètres d'affichage.
	 * @param message avertissement affiché à l'utilisateur
	 * @param fantomable indique si le textbox de commentaire doit 
	 * être affiché.
	 * @param deletable inique si l'avertissement seul doit être affiché.
	 * @param component parent ayant demandé la modale.
	 * @param archive si la deletion devient un archivage.
	 */
	public void init(String mess, boolean casc, boolean fantomable, 
					boolean deletable, Component prt, boolean archive) {
		
		warnLabel.setContent(mess);
		setParent(prt);
		
		if (!deletable) { // affiche le stop uniquement
			stopImg.setVisible(true);
		} else {
			warnImg.setVisible(true);
			delete.setVisible(true);
			if (fantomable) { // affiche la ligne dédié au comments
				commentsRow.setVisible(true);
				commentsRowBox.setVisible(true);
			}
		}		
		
		// change le label du bouton delete
		if (archive) {
			delete.setLabel(Labels.getLabel("general.archive"));
		}
	}	

	public void onClick$delete() {	
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
		
		// réalise la suppression
		Events.postEvent("onDeleteTriggered", getParent(), getComments());
	}
	
	public void onClick$cancel() {
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}
	
	/**********************************************************/
	/****************** GETTERS - SETTERS *********************/
	/**********************************************************/
	public String getComments() {
		return comments;
	}

	public void setComments(String cs) {
		this.comments = cs;
	}
	
	public Component getParent() {
		return parent;
	}

	public void setParent(Component pa) {
		this.parent = pa;
	}

	private static ConstWord commentsConstraint = new ConstWord();
	{
		commentsConstraint.setNullable(true);
	}
	
	public ConstWord getCommentsConstraint() {
		return commentsConstraint;
	}
}
