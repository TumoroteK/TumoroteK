/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.action.echantillon.gatsbi;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;

import fr.aphp.tumorotek.action.echantillon.AbstractEchantillonDecoratorRowRenderer;
import fr.aphp.tumorotek.action.echantillon.FicheMultiEchantillons;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;

/**
*
* Controller gérant la fiche formulaire multi échantillon sous le gestionnaire
* GATSBI.
*
* @author mathieu BARTHELEMY
* @version 2.3.0-gatsbi
*
*/
public class FicheMultiEchantillonsGatsbi extends FicheMultiEchantillons
{

	private static final long serialVersionUID = 3863329092781960062L;
	
	private Contexte contexte;
	
	private List<Listbox> reqListboxes = new ArrayList<Listbox>();
	private List<Combobox> reqComboboxes = new ArrayList<Combobox>();
	private List<Div> reqDivs = new ArrayList<Div>(); // contient conformite Div et crAnapath
	
	// @wire
	private Groupbox groupEchantillon;
	private Groupbox groupInfosCompEchan;

	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);

		contexte = GatsbiController.initWireAndDisplay(this, 
				3, 
				true, reqListboxes, reqComboboxes, reqDivs,
				groupEchantillon, groupInfosCompEchan);
		
		// affichage conditionnel infos prelevement 
		GatsbiController.initWireAndDisplayForIds(this, 2, "natureDiv");
		
		// inner list
		drawColumnsForEchantillonsToCreate(echantillonsList);
	}
	
	@Override
	protected void setGroupInfosCompEchanOpen(boolean b) {
		((Groupbox) groupInfosCompEchan).setOpen(b);
	}
	
	@Override
	protected void scrollToTop() {
		Clients.scrollIntoView(this.getSelfComponent());
	}
	
	/*********** switch required ******************/

	/**
	 * Surcharge Gastbi pour conserver sélectivement la
	 * contrainte de sélection obligatiure des listes nature et statut juridique 
	 * dans le contexte TK historique
	 */
	@Override
	protected void checkRequiredListboxes() {
		GatsbiController.checkRequiredNonInputComponents(reqListboxes, null, null);
	}

	/**
	 * Plus d'obligation
	 */
	@Override
	public void onSelect$typesBoxEchan() {
	}	
	
	@Override
	protected void prepareCrAnapath() {
		if (crAnapathNomBox.getValue() != null && !crAnapathNomBox.getValue().equals("")) {
			getCrAnapath().setNom(crAnapathNomBox.getValue());
		} else { // empty value, check required
			Div crAnapathDiv = reqDivs.stream().filter(d -> d.getId().equals("crAnapathDiv")).findFirst().orElse(null);
			if (crAnapathDiv != null) { // required value
				throw new WrongValueException(crAnapathDiv, Labels.getLabel("validation.syntax.empty"));
			} else { // emptyallowed
				setCrAnapath(null);
				setAnapathStream(null);
			}
		}
	}
	
	@Override
	public void onClick$addEchantillons(Event event) {
		
		GatsbiController.checkRequiredNonInputComponents(reqListboxes, reqComboboxes, reqDivs);
		
		super.onClick$addEchantillons(event);
	}
	
	/*********** inner lists ******************/
	
	private final AbstractEchantillonDecoratorRowRenderer echanDecoRendererGatsbi = 
			new EchantillonDecoratorRowRendererGatsbi();
	
	public AbstractEchantillonDecoratorRowRenderer getEchanDecoRenderer() {
		return echanDecoRendererGatsbi;
	}
	
	private void drawColumnsForEchantillonsToCreate(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		// icones column, visible si non conformites OU risque est visible
		if (contexte.isChampIdVisible(249) || contexte.isChampIdVisible(243) || contexte.isChampIdVisible(244)) {
			
			int colsize = 0;
			if (contexte.isChampIdVisible(249)) {
				colsize = colsize + 35;
			} 
			if (contexte.isChampIdVisible(243)) {
				colsize = colsize + 35;
			}
			if (contexte.isChampIdVisible(244)) {
				colsize = colsize + 35;
			}
			
			GatsbiController.addColumn(grid, null, String.valueOf(colsize).concat("px"),
					"center", null, null, true);

			// indique au row renderer qu'il doit dessiner les icones
			echanDecoRendererGatsbi.setIconesRendered(true);
		}

		// code prel column, toujours affichée
		GatsbiControllerEchantillon.drawCodeColumn(grid);
		
		// variable columns
		for (Integer chpId : contexte.getChampEntiteInTableauOrdered()) {
			// statut et emplacement toujours affichés
			if (chpId != 55 && chpId != 57) {
				GatsbiControllerEchantillon.addColumnForChpId(chpId, grid);
			}
		}

		// emplacement, toujours affiché
		GatsbiControllerEchantillon.drawEmplacementColumn(grid); 

		// statut, toujours affiché
		GatsbiControllerEchantillon.drawObjetStatutColumn(grid);
		
		// delete col
		GatsbiController.addColumn(grid, null, "35px", "center", null, null, true);
	}
	
	/*********** Paramétrages ******************/
	
	/**
	 * Redirection d'évènement lors du choix d'un paramétrage
	 */
	public void onGetInjectionDossierExterneDone(ForwardEvent e) {
		final ResultatInjection res = (ResultatInjection) e.getOrigin().getData();
		injectEchantillon(res.getEchantillon());
	}
}