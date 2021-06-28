/**
 * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.prelevement.FicheLaboInter;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.Contexte;

/**
 *
 * Controller gérant la fiche formulaire du transfert du 
 * prélèvement vers le site de stockage sous le gestionnaire GATSBI.
 * Controller créé le 25/05/2021.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FicheLaboInterGatsbi extends FicheLaboInter {

	private static final long serialVersionUID = 1L;

	private Div gatsbiContainer;

	private List<Listbox> reqListboxes = new ArrayList<Listbox>();
	
	private Contexte c;

	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);

		c = GatsbiController.mockOneContexte(); // TODO replace by collection.contexte

		try {
			List<Div> itemDivs = GatsbiController.wireItemDivsFromMainComponent(gatsbiContainer);
			List<Div> blockDivs = GatsbiController.wireBlockDivsFromMainComponent(gatsbiContainer);

			GatsbiController.showOrhideItems(itemDivs, blockDivs, c); 
			GatsbiController.switchItemsRequiredOrNot(itemDivs, c, reqListboxes);

			GatsbiController.appliThesaurusValues(itemDivs, c, this);


		} catch (Exception e) {
			Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
			log.debug(e);
		}

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
}