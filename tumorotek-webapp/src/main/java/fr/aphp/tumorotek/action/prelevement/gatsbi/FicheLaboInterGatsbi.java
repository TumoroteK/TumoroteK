/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de 
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import fr.aphp.tumorotek.action.prelevement.FicheLaboInter;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;

/**
 *
 * Controller gérant la fiche formulaire du transfert du prélèvement vers le
 * site de stockage sous le gestionnaire GATSBI. Controller créé le 25/05/2021.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FicheLaboInterGatsbi extends FicheLaboInter {

	private static final long serialVersionUID = 1L;

	private Div gatsbiContainer;

	private List<Listbox> reqListboxes = new ArrayList<Listbox>();
	private List<Combobox> reqComboboxes = new ArrayList<Combobox>();
	private List<Div> reqConformeDivs = new ArrayList<Div>();

	private Contexte c;

	@Override
	public void doAfterCompose(final Component comp) throws Exception {
		super.doAfterCompose(comp);

		c = GatsbiController.initWireAndDisplay(this, 2, 
				true, reqListboxes, reqComboboxes, reqConformeDivs);
		
		// labo inter specific
		// Show/hide groupLaboInter
		((Div) gatsbiContainer.getFellowIfAny("groupLaboInter")).setVisible(c.getSiteInter());
	}

	@Override
	public void switchToCreateMode() {

		log.debug("Surcharge Gastbi pour supprimer le bouton ajout de sites intermédiaires si besoin");

		super.switchToCreateMode();

		addLabo.setVisible(c.getSiteInter());

		// scroll up pour se placer en haut de la page
		Clients.scrollIntoView(gatsbiContainer);

	}

	@Override
	public void switchToEditMode() {

		log.debug("Surcharge Gastbi pour supprimer le bouton ajout de sites intermédiaires si besoin");

		super.switchToEditMode();

		addLabo.setVisible(c.getSiteInter());
	}

	@Override
	public void onClick$next() {
		GatsbiController.checkRequiredNonInputComponents(reqListboxes, reqComboboxes, reqConformeDivs);
		super.onClick$next();
	}

	@Override
	public void onClick$validate() {
		GatsbiController.checkRequiredNonInputComponents(reqListboxes, reqComboboxes, reqConformeDivs);
		super.onClick$validate();
	}

	@Override
	public void onClick$create() {
		GatsbiController.checkRequiredNonInputComponents(reqListboxes, reqComboboxes, reqConformeDivs);
		super.onClick$create();
	}

}