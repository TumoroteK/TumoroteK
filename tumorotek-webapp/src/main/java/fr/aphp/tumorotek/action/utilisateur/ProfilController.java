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
package fr.aphp.tumorotek.action.utilisateur;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.utilisateur.Profil;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class ProfilController extends AbstractObjectTabController {

	private static final long serialVersionUID = -5794997706460158602L;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setStaticEditMode(false);
	}
	
	@Override
	public TKdataObject loadById(Integer id) {
		return ManagerLocator.getProfilManager().findByIdManager(id);
	}
	
	@Override
	public FicheProfil getFicheCombine() {
		return ((FicheProfil) 
				this.self.getFellow("ficheProfil")
				.getFellow("fwinProfil")
				.getAttributeOrFellow("fwinProfil$composer", true));
	}

	@Override
	public ListeProfil getListe() {
		return ((ListeProfil) 
				this.self.getFellow("listeProfil")
				.getFellow("lwinProfil")
				.getAttributeOrFellow("lwinProfil$composer", true));
	}

	@Override
	public FicheAnnotation getFicheAnnotation() {
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
	
	/**
	 * Selectionne le tab et renvoie le controller Profil.
	 * @param page
	 * @return controller tab
	 * @since 2.1
	 */
	public static AbstractObjectTabController backToMe(MainWindow window, 
								Tabbox tabbox) {
		
		ProfilController tabController = null;
		
		if (tabbox != null) {
			// on récupère le panel de l'entite
			Tabpanel tab = (Tabpanel) tabbox.getFellow("profilsPanel");
		
			window.createMacroComponent(
					"/zuls/utilisateur/Profil.zul", 
					"winProfil", tab);
			
			tabController = 
				((ProfilController) tab.getFellow("winProfil")
								.getAttributeOrFellow("winProfil$composer", true));
		
			tabbox.setSelectedPanel(tab);
		}
		
		return tabController;
	}
	
	/**
	 * Selectionne le profil passé en paramètre
	 * @param profil
	 * @since 2.1
	 */
	public void selectProfilInListe(Profil profil) {
		switchToFicheAndListeMode();
		
		if (getFicheCombine() != null) {
			getFicheCombine().setObject(profil);
			getFicheCombine().switchToStaticMode();
		}
		
		// si la liste cachée contient l'objet, on le sélectionne
		if (getListe().getListObjects().contains(profil)) {
			getListe().changeCurrentObject(profil);
		} else {
			getListe().deselectRow();
		}
	}
}
