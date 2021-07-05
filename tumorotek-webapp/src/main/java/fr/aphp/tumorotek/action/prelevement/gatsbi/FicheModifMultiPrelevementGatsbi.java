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
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.prelevement.FicheModifMultiPrelevement;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.Contexte;

/**
 *
 * Controller gérant la fiche de modification multiple de prélèvements 
 * sous le gestionnaire GATSBI.
 * Controller créé le 25/06/2021.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FicheModifMultiPrelevementGatsbi extends FicheModifMultiPrelevement {

	private static final long serialVersionUID = 1L;
	
	private Div gatsbiContainer;

	private Contexte c;
	List<Div> itemDivs  = new ArrayList<Div>();
	List<Div> blockDivs  = new ArrayList<Div>();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		try {
			itemDivs.addAll(GatsbiController.wireItemDivsFromMainComponent(gatsbiContainer));
			blockDivs.addAll(GatsbiController.wireBlockDivsFromMainComponent(gatsbiContainer));

			c = GatsbiController.mockOneContexte();

			GatsbiController.showOrhideItems(itemDivs, blockDivs, c); // TODO replace by collection.contexte
			// GatsbiController.switchItemsRequiredOrNot(itemDivs, c, reqListboxes, 
			//		new ArrayList<Combobox>(), new ArrayList<Div>());

		} catch (Exception e) {
			log.debug(e);
			Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	@Override
	protected List<Object> applyAnyThesaurusRestriction(List<Object> thObjs, Integer chpId) {
		return GatsbiController.filterExistingListModel(c, thObjs, chpId);
	}
	
	@Override
	protected Constraint muteAnyRequiredConstraint(Constraint cstr, Integer chpId) {
		return GatsbiController.muteConstraintFromContexte(cstr, c.isChampIdRequired(chpId));
	}
	
	@Override
	protected boolean switchAnyRequiredFlag(Boolean flag, Integer chpId) {
		return c.isChampIdRequired(chpId);
	}
}