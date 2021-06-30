/**
 * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.action.recherche.gatsbi;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;

import fr.aphp.tumorotek.action.prelevement.gatsbi.GatsbiController;
import fr.aphp.tumorotek.action.recherche.FicheRechercheAvancee;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.Contexte;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant le formulaire de recherche avancée 
 * sous le gestionnaire GATSBI.
 * Controller créé le 26/06/2021.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FicheRechercheAvanceeGatsbi extends FicheRechercheAvancee {

	private static final long serialVersionUID = -7186817237148944889L;

	private Grid gatsbiContainer;

	private Contexte c;

	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);

		List<Div> itemDivs = GatsbiController.wireItemDivsFromMainComponent(gatsbiContainer);
		List<Div> blockDivs = GatsbiController.wireBlockDivsFromMainComponent(gatsbiContainer);

		c = GatsbiController.mockOneContexte();

		GatsbiController.showOrhideItems(itemDivs, blockDivs, c); // TODO replace by collection.contexte

		// hide group labo Inter
		groupLaboInters.setVisible(c.getSiteInter());
	}
}
