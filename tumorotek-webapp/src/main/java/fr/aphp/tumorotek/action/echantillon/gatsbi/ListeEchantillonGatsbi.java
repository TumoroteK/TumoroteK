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
package fr.aphp.tumorotek.action.echantillon.gatsbi;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import fr.aphp.tumorotek.action.echantillon.ListeEchantillon;
import fr.aphp.tumorotek.action.prelevement.gatsbi.exception.GatsbiException;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @version 2.3.0-gatsbi 
 * @author Mathieu BARTHELEMY
 *
 */
public class ListeEchantillonGatsbi extends ListeEchantillon {

	private static final long serialVersionUID = 1L;

	private Contexte contexte;

	public ListeEchantillonGatsbi() {
		setListObjectsRenderer(new EchantillonRowRendererGatsbi(true, false));
	}

	public void onCheckAll$gridColumns() {
		onCheck$checkAll();
	}

	@Override
	protected void drawColumnsForVisibleChampEntites()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(3);
		
		// check box first column, toujours affichée
		Checkbox cbox = new Checkbox();
		cbox.setId("checkAll");
		cbox.addForward("onCheck", objectsListGrid.getColumns(), "onCheckAll");
		GatsbiController.addColumn(objectsListGrid, null, "40px", null, cbox, null, true);
		
		// icones column, toujours visible car impact evt de stockage
		GatsbiController.addColumn(objectsListGrid, null, 
			GatsbiControllerEchantillon.getIconesColWidthFrom(30, contexte), "center", null, null, true);

		// code prel column, toujours affichée
		GatsbiControllerEchantillon.drawCodeColumn(objectsListGrid);

		// ttes collection
		GatsbiControllerEchantillon.drawBanqueColumn(objectsListGrid, isTtesCollection());

		// patient, colonne toujours affichée
		GatsbiControllerEchantillon.drawPatientColumn(objectsListGrid);

		// variable columns
		for (Integer chpId : contexte.getChampEntiteInTableauOrdered()) {
			GatsbiControllerEchantillon.addColumnForChpId(chpId, objectsListGrid);
		}

		// nb dérivés
		nbProdDerivesColumn = GatsbiControllerEchantillon.drawNbDerivesColumn(objectsListGrid);


		// nb cessions
		nbCessionsColumn = GatsbiControllerEchantillon.drawNbCessionsColumn(objectsListGrid);
	}

	/**
	 * Gatsbi surcharge pour intercaler une modale de sélection des parametrages
	 * proposés par le contexte.
	 * 
	 * @param click event
	 */
	@Override
	public void onClick$addNew(final Event event) throws Exception {		
		GatsbiController.addNewObjectForContext(contexte, self, 
			e -> {
				try {
					super.onClick$addNew(e);
				} catch (Exception ex) {
					Messagebox.show(handleExceptionMessage(ex), 
							"Error", Messagebox.OK, Messagebox.ERROR);
				}
			}, event);
	}

	/**
	 * Un parametrage a été sélectionné.
	 * 
	 * @param param
	 * @throws Exception
	 */
	public void onGetSelectedParametrage(ForwardEvent evt) throws Exception {

		try {
			
			GatsbiController.getSelectedParametrageFromSelectEvent(contexte, 
				SessionUtils.getCurrentBanque(sessionScope), 
				getObjectTabController(), null, 
				() -> {
					try {
						super.onClick$addNew(null);
					} catch (Exception ex) {
						Messagebox.show(handleExceptionMessage(ex), 
								"Error", Messagebox.OK, Messagebox.ERROR);
					}
				}, evt);	
		} catch (GatsbiException e) {
			Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
}