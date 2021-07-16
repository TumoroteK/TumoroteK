/**
 * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.action.recherche.gatsbi;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.prelevement.gatsbi.GatsbiController;
import fr.aphp.tumorotek.action.recherche.FicheRechercheAvancee;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.qualite.NonConformite;
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
	
	List<Div> itemDivs = new ArrayList<Div>();
	List<Div> blockDivs = new ArrayList<Div>();

	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);

		itemDivs.addAll(GatsbiController.wireItemDivsFromMainComponent(gatsbiContainer));
		blockDivs.addAll(GatsbiController.wireBlockDivsFromMainComponent(gatsbiContainer));

		GatsbiController.showOrhideItems(itemDivs, blockDivs, SessionUtils.getGatsbiContextes());
		
		// hide group labo Inter
		groupLaboInters.setVisible(SessionUtils.getCurrentGatsbiContexteForEntiteId(2) != null 
				&& SessionUtils.getCurrentGatsbiContexteForEntiteId(2).getSiteInter());
	}
	
	/**
	 * Gatsbi surcharge cette méthode pour restreindre les valeurs 
	 * de thésaurus après leur initialisation.
	 */
	@Override
	protected void applyThesaurusRestrictions() {
		try {
			GatsbiController.appliThesaurusValues(itemDivs, SessionUtils.getGatsbiContextes(), this);
		} catch (Exception e) {
			log.debug(e);
			Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * Gatsbi modifie cette liste et implémente donc le setter
	 * @param risks
	 */
	public void setRisquesModel(ListModelList<Risque> risks) {
		getRisquesModel().clear();
		getRisquesModel().addAll(risks);
	}
	
	/**** Gastbi setter/getter surcharges pour apache's PropertyUtils.g/setProperty ******/
	public List<NonConformite> getNcarrivee() {
		return getNCarrivee();
	}
	
	public void setNcarrivee(List<NonConformite> _ncs) {
		setNCarrivee(_ncs);
	}
}