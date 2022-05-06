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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.patient.ResumePatient;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementStatic;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.Parametrage;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.ParametrageDTO;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche static d'un prélèvement GATSBI.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FichePrelevementStaticGatsbi extends FichePrelevementStatic {

	private static final long serialVersionUID = -7612780578022559022L;

	private Div gatsbiContainer;

	private Groupbox groupPrlvt;

	private Contexte c;

	@Override
	public void doAfterCompose(final Component comp) throws Exception {
		super.doAfterCompose(comp);

		c = SessionUtils.getCurrentGatsbiContexteForEntiteId(2);

		List<Div> itemDivs = GatsbiController.wireItemDivsFromMainComponent(c.getContexteType(), gatsbiContainer);
		List<Div> blockDivs = GatsbiController.wireBlockDivsFromMainComponent(c.getContexteType(), gatsbiContainer);

		GatsbiController.showOrhideItems(itemDivs, blockDivs, c); // TODO replace by collection.contexte

		// prelevement specific
		if (groupLaboInter != null) {
			groupLaboInter.setVisible(c != null && c.getSiteInter());
		}
		hideEmptyGroupboxes();
	}

	private void hideEmptyGroupboxes() {
		GatsbiController.hideGroupBoxIfEmpty(groupPrlvt);
		GatsbiController.hideGroupBoxIfEmpty(gridFormPrlvtComp);
	}

	@Override
	protected ResumePatient initResumePatient() {
		return new ResumePatient(groupPatient, true);
	}

	@Override
	protected void enablePatientGroup(boolean b) {
		((Groupbox) this.groupPatient).setOpen(b);
		((Groupbox) this.groupPatient).setClosable(b);
	}

	// TODO: ces deux méthodes sont factorisables avec
	// celles de ListePrelevementGatsbi

	/**
	 * Gatsbi surcharge pour intercaler une modale de sélection des parametrages
	 * proposés par le contexte.
	 * 
	 * @param click event
	 */
	@Override
	public void onClick$addNew() {

		if (!c.getParametrages().isEmpty()) {
			final Map<String, Object> args = new HashMap<String, Object>();
			args.put("contexte", c);
			args.put("parent", self);
			Executions.createComponents("/zuls/gatsbi/SelectParametrageModale.zul", null, args);
		} else { // no parametrages
			super.onClick$addNew();
		}
	}

	/**
	 * Un parametrage a été sélectionné.
	 * 
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void onGetSelectedParametrage(ForwardEvent evt) throws Exception {
		try {
			ResultatInjection inject = null;
			if (((Map<String, Integer>) evt.getOrigin().getData()).get("paramId") != null) {
				ParametrageDTO parametrageDTO = GatsbiController
						.doGastbiParametrage(((Map<String, Integer>) evt.getOrigin().getData()).get("paramId"));

				Consumer<Parametrage> validator = p -> {
					// cong depart OU cong arrivee
					if (p.getDefaultValuesForChampEntiteId(269) != null
							&& p.getDefaultValuesForChampEntiteId(269).contentEquals("1")
							&& p.getDefaultValuesForChampEntiteId(270) != null
							&& p.getDefaultValuesForChampEntiteId(270).contentEquals("1")) {
						throw new TKException("gatsbi.illegal.parametrage.prelevement.cong");
					}
				};

				inject = GatsbiController.injectGatsbiObject(c, parametrageDTO,
						SessionUtils.getCurrentBanque(sessionScope), validator);
			}

			super.onClick$addNew();

			if (inject != null) {
				Events.postEvent("onGatsbiParamSelected", getObjectTabController().getFicheEdit().getSelfComponent(),
						inject);
			}
		} catch (Exception e) {
			Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
}
