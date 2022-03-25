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
import java.util.Map;
import java.util.function.Consumer;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.prelevement.ListePrelevement;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.Parametrage;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.ParametrageDTO;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ListePrelevementGatsbi extends ListePrelevement {

	private static final long serialVersionUID = 1L;

	private Contexte contexte;

	// wire columns parent
	private Columns gridColumns;

	// flag passe à true si la colonne congelation est déja rendue
	// afin d'éviter que cette colonne soit rendue deux fois
	private boolean congColRendered = false;

	public ListePrelevementGatsbi() {
		setListObjectsRenderer(new PrelevementRowRendererGatsbi(true, false));
	}

	public void onCheckAll$gridColumns() {
		onCheck$checkAll();
	}

	@Override
	protected void drawColumnsForVisibleChampEntites()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(2);

		// check box first column, toujours affichée
		Checkbox cbox = new Checkbox();
		cbox.setId("checkAll");
		cbox.addForward("onCheck", gridColumns, "onCheckAll");
		addColumn(null, "40px", null, cbox, null, true);

		// icones column, visible si non conformites OU risque est visible
		if (contexte.isChampIdVisible(249) || contexte.isChampIdVisible(256)
				|| SessionUtils.getEmetteursInterfacages(sessionScope).size() > 0) {
			addColumn(null, (contexte.isChampIdVisible(249) && contexte.isChampIdVisible(256)) ? "70px" : "35px",
					"center", null, null, true);

			// indique au row renderer qu'il doit dessiner les icones
			getListObjectsRenderer().setIconesRendered(true);
		}

		// code prel column, toujours affichée
		addColumn("general.code", null, null, null, "auto(code)", true);

		// ttes collection
		addColumn("Entite.Banque", null, null, null, "auto(code)", isTtesCollection());

		// patient, colonne toujours affichée
		addColumn("prelevement.patient", null, null, null, "auto(maladie.patient.nom)", true);

		// nip, colonne non visible par défaut
		addColumn("Champ.Patient.Nip", null, null, null, "auto(maladie.patient.nom)", false);

		// maladie, colonne visible si banque définit le niveau
		addColumn("prelevement.maladie", "150px", null, null, "auto(maladie.patient.nom)", getBanqueDefMaladies());

		// variable columns
		for (Integer chpId : contexte.getChampEntiteInTableauOrdered()) {
			addColumnForChpId(chpId);
		}

		// nb echantillons
		Vbox vbox = new Vbox();
		vbox.setAlign("center");
		Label nbEch = new Label(Labels.getLabel("prelevement.nbEchantillons"));
		nbEch.setStyle("font-weight : bold;font-family: Verdana, Arial, Helvetica, sans-serif;");
		nbEch.setParent(vbox);
		Label nbEchRest = new Label(Labels.getLabel("prelevement.restants.total.stockes"));
		nbEchRest.setStyle("font-weight : bold;font-family: Verdana, Arial, Helvetica, sans-serif;");
		nbEchRest.setParent(vbox);
		nbEchantillonsColumn = addColumn(null, null, "center", vbox, "auto", true);
		nbEchantillonsColumn.setId("nbEchantillonsColumn");
	}

	private void addColumnForChpId(Integer chpId)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		switch (chpId) {
		case 23:
			// "code" toujours rendu par défaut
			break;
		case 45: // numero labo
			addColumn("Champ.Prelevement.NumeroLabo", null, null, null, "auto(numeroLabo)", true);
			break;
		case 24: // nature
			addColumn("Champ.Prelevement.Nature.Nature", null, null, null, "auto(nature.nom)", true);
			break;
		case 44: // nda
			addColumn("Champ.Prelevement.PatientNda", null, null, null, "auto(patientNda)", true);
			break;
		case 30: // date prelevement
			addColumn("prelevement.datePrelevementCourt", null, null, null, "auto(datePrelevement)", true);
			break;
		case 31: // prelevement type
			addColumn("Champ.Prelevement.PrelevementType.Type", null, null, null, "auto(prelevementType.nom)", true);
			break;
		case 47: // sterile
			addColumn("Champ.Prelevement.Sterile", null, null, null, "auto(sterile)", true);
			break;
		case 249: // risques -> rendu sous la forme d'une icône
			break;
		case 29: // service preleveur
			addColumn("Champ.Prelevement.ServicePreleveur", null, null, null, "auto(servicePreleveur.nom)", true);
			break;
		case 28: // preleveur
			addColumn("Champ.Prelevement.Preleveur", null, null, null, "auto(preleveur.nomAndPrenom)", true);
			break;
		case 32: // condit type
			addColumn("Champ.Prelevement.ConditType.Type", null, null, null, "auto(conditType.nom)", true);
			break;
		case 34: // condit Nbr
			addColumn("Champ.Prelevement.ConditNbr", null, null, null, "auto(conditNbr)", true);
			break;
		case 33: // condit milieu
			addColumn("Champ.Prelevement.ConditMilieu", null, null, null, "auto(conditMilieu.nom)", true);
			break;
		case 26: // consent type
			addColumn("Champ.Prelevement.ConsentType", null, null, null, "auto(consentType.nom)", true);
			break;
		case 27: // consent date
			addColumn("Champ.Prelevement.ConsentDate", null, null, null, "auto(consentDate)", true);
			break;
		case 35: // date depart
			addColumn("Champ.Prelevement.DateDepart", null, null, null, "auto(dateDepart)", true);
			break;
		case 36: // transporteur
			addColumn("Champ.Prelevement.Transporteur", null, null, null, "auto(transporteur.nom)", true);
			break;
		case 37: // transport temperature
			addColumn("Champ.Prelevement.TransportTemp", null, null, null, "auto(transportTemp)", true);
			break;
		case 269: // cong depart
			if (!congColRendered) {
				addColumn("prelevement.cong", null, null, null, "auto(transportTemp)", true);
				congColRendered = true;
			}
			break;
		case 38: // date arrivee
			addColumn("Champ.Prelevement.DateArrivee", null, null, null, "auto(dateArrivee)", true);
			break;
		case 39: // operateur
			addColumn("Champ.Prelevement.Operateur", null, null, null, "auto(operateur.nom)", true);
			break;
		case 40: // quantite
			addColumn("Champ.Prelevement.Quantite", null, null, null, "auto(quantite)", true);
			break;
		case 270: // cong arrivee
			if (!congColRendered) {
				addColumn("prelevement.cong", null, null, null, "auto(transportTemp)", true);
				congColRendered = true;
			}
			break;
		case 256: // conforme arrivee -> rendu sous la forme d'une icône
			break;
		default:
			break;
		}
	}

	private Column addColumn(String nameKey, String width, String align, Component child, String sort, Boolean visible)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// check box first immutable column
		Column col = new Column();
		col.setLabel(Labels.getLabel(nameKey));
		col.setWidth(width);
		col.setAlign(align);
		if (child != null) {
			child.setParent(col);
		}
		col.setSort(sort);
		col.setVisible(visible);
		col.setParent(gridColumns);

		return col;
	}

	/**
	 * Gatsbi surcharge pour intercaler une modale de sélection des parametrages
	 * proposés par le contexte.
	 * 
	 * @param click event
	 */
	@Override
	public void onClick$addNew(final Event event) throws Exception {
		
		if (!contexte.getParametrages().isEmpty()) {
			final Map<String, Object> args = new HashMap<String, Object>();
			args.put("contexte", contexte);
			args.put("parent", self);
			Executions.createComponents("/zuls/gatsbi/SelectParametrageModale.zul", null, args);
		} else { // no parametrages
			super.onClick$addNew(event);
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

				inject = GatsbiController.injectGatsbiObject(contexte, parametrageDTO,
						SessionUtils.getCurrentBanque(sessionScope), validator);
			}

			super.onClick$addNew(null);

			if (inject != null) {
				Events.postEvent("onGatsbiParamSelected", getObjectTabController().getFicheEdit().getSelfComponent(),
						inject);
			}
		} catch (Exception e) {
			Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
}