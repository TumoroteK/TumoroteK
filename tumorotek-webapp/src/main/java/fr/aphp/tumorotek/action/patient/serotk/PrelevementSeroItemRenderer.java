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
package fr.aphp.tumorotek.action.patient.serotk;

import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import fr.aphp.tumorotek.action.patient.PrelevementItemRenderer;
import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;

/** 
 * PrelevementRenderer affiche dans le listitem
 * les membres de Prelevement pour le contexte Serotheque
 * Le membre otherConsultBanks recoit la liste de banque dont l'acces
 * en consultation des prélèvements est autorisé.
 * 
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 17/04/2010
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class PrelevementSeroItemRenderer extends PrelevementItemRenderer {

	/**
	 * Constructeur.
	 */
	public PrelevementSeroItemRenderer() {
	}

	@Override
	public void render(Listitem li, Prelevement data, int index) {
        
		Prelevement prel = data;
		
		Listcell rCell = new Listcell();
		Hlayout icones = PrelevementUtils.drawListIcones(prel);
		rCell.appendChild(icones);
		rCell.setParent(li);
		
		new Listcell(ObjectTypesFormatters
				.dateRenderer2(prel.getDatePrelevement())).setParent(li); 
		Listcell codeCell = new Listcell(prel.getCode());
		codeCell.setParent(li);
		if (isAccessible()
				|| (getOtherConsultBanks() != null 
						&& getOtherConsultBanks().contains(prel.getBanque()))) {
			codeCell.addForward(null, li.getParent(), 
											"onClickPrelevementCode", prel);
			codeCell.setClass("formLink");
		} 
		if (getOtherConsultBanks() != null) { // mode otherBank prelevement
			new Listcell(prel.getBanque().getNom()).setParent(li);
		}
		if (prel.getNature() != null) {
			new Listcell(prel.getNature().getNature()).setParent(li);
		} else {
			new Listcell().setParent(li);
		}
		
		// type
		if (prel.getPrelevementType() != null) {
			new Listcell(prel.getPrelevementType().getType()).setParent(li);
		} else {
			new Listcell().setParent(li);
		}
		
		// protocoles : liste des protocoles
		if (prel.getDelegate() != null) {
			ObjectTypesFormatters
			.drawProtocolesLabel(((PrelevementSero) prel.getDelegate())
												.getProtocoles(), null, li);
		} else {
			new Listcell().setParent(li);
		}
		
		if (prel.getConsentType() != null) {
			new Listcell(prel.getConsentType().getType()).setParent(li);
		} else {
			new Listcell().setParent(li);
		}
        new Listcell(PrelevementUtils
        				.getNbEchanRestantsSurTotalEtStockes(prel))
        	.setParent(li);
        
		switch (PrelevementUtils.getNbEchanRestants(prel)) {
			case 0:
				li.setStyle("background-color : #FF8678");
				break;
			case 1:
				li.setStyle("background-color : #FFCB6B");
				break;
			default:
				break;
		}
	}
}

