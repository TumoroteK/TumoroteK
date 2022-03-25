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
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
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

	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);
		
		List<Div> contexteItemDivs = new ArrayList<Div>();
		List<Div> contexteBlockDivs = new ArrayList<Div>();
		
		for (Contexte contexte : SessionUtils.getGatsbiContextes()) {
			contexteItemDivs.clear();
			contexteBlockDivs.clear();
			
			contexteItemDivs.addAll(GatsbiController
				.wireItemDivsFromMainComponent(contexte.getContexteType(), gatsbiContainer));
			contexteBlockDivs.addAll(GatsbiController
				.wireBlockDivsFromMainComponent(contexte.getContexteType(), gatsbiContainer));

			GatsbiController.showOrhideItems(contexteItemDivs, contexteBlockDivs, contexte);
			
			// add itemDiv to all items div list, so that thesaurus restriction applies once
			itemDivs.addAll(contexteItemDivs);
		}
		
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