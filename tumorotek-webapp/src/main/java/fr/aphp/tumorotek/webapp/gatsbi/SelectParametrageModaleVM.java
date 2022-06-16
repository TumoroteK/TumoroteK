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
package fr.aphp.tumorotek.webapp.gatsbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.Parametrage;

/**
 * Date: 21/07/2021
 * @since 2.3.0-gatsbi
 * @version 2.3.0-gatsbi
 * @author Mathieu BARTHELEMY
 *
 */
public class SelectParametrageModaleVM {

	private String entiteLabel;
	private Component parent;
	private TKAnnotableObject parentObj;
	private SimpleListModel<Parametrage> parametrages;

	@Wire
	private Window selectParametrageModale;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
		Selectors.wireComponents(view, this, false);
	}

	@Init
	public void init(@ExecutionArgParam("contexte") Contexte _c, 
			@ExecutionArgParam("parent") final Component _p, 
			@ExecutionArgParam("parentObj") TKAnnotableObject _o){
		List<Parametrage> params = new ArrayList<Parametrage>();
		params.add(0, new Parametrage(null, Labels.getLabel("general.new"), null));
		params.addAll(_c.getParametrages());
		parametrages = new SimpleListModel<Parametrage>(params);
		entiteLabel = Labels.getLabel("Entite.".concat(_c.getContexteType().getType()));
		parent = _p;
		parentObj = _o;
	}

	@Command
	public void onSelectParametrage(@BindingParam("paramId") Integer paramId){
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("paramId", paramId);
		args.put("parentObj", parentObj);
		Events.postEvent("onSelectParametrage", parent, args);
		Events.postEvent("onClose", selectParametrageModale, null);
	}

	public SimpleListModel<Parametrage> getParametrages() {
		return parametrages;
	}
	
	public String getTitle() {
		return Labels.getLabel("gatsbi.parametrages", new String[]{entiteLabel});
	}
}
