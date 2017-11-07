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
package fr.aphp.tumorotek.action.echantillon;

import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.utils.TKStockableObjectUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;

/** 
 * EchantillonDecoratorRenderer affiche dans la grid temporaire
 * les membres d'EchantillonDecorator2 lors de l'ajout 
 * multiple dans sous forme de labels.
 * 
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 18/11/2010
 * 
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 */
public class EchantillonDecoratorRowRenderer 
				implements RowRenderer<EchantillonDTO> {
	
	private List<String> usedCodes = null;

	public void setUsedCodes(List<String> o) {
		usedCodes = o;
	}

	@Override
	public void render(Row row, final EchantillonDTO deco, int index)  {
				
		Hlayout icones = TKStockableObjectUtils.drawListIcones(deco.getEchantillon(), deco.getNonConformiteTraitements(), deco.getNonConformiteCessions());
		icones.setParent(row);
		
		// code
		if (deco.isNew() && deco.getAdrlTmp() == null) {
			final Textbox tb = new Textbox();
			tb.setValue(deco.getCode());
			tb.setInplace(true);
			tb.setConstraint(EchantillonConstraints.getCodePrefixConstraint());
			tb.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
	            public void onEvent(Event event) throws Exception {
	            	String old = deco.getCode();
	            	tb.setValue(tb.getValue().toUpperCase());
	            	deco.setCode(tb.getValue());
	            	if (ManagerLocator
	            		.getEchantillonManager()
	            		.findDoublonManager(deco.getEchantillon())) {
	            		String newCode = tb.getValue();
	            		deco.setCode(old);
	            		tb.setValue(old);
	            		throw new WrongValueException(tb, 
	            				ObjectTypesFormatters.getLabel("error.code.used", new String[]{newCode})
	        + " " + new DoublonFoundException("Echantillon", "creation").getMessage());
	            	} else if (usedCodes != null 
	            			&& usedCodes.contains(tb.getValue())) {
	            		deco.setCode(old);
	            		tb.setValue(old);
			            	throw new WrongValueException(tb, 
			            			"Modification impossible! "
	+ new DoublonFoundException("Echantillon", "creation").getMessage());
	            	} else { // changement du code
	            		usedCodes.remove(old);
	            		usedCodes.add(tb.getValue());
	            	}
	            }
	        });
			tb.addEventListener(Events.ON_OK, new EventListener<Event>() {
	            public void onEvent(Event event) throws Exception {
	            	Events.postEvent(Events.ON_MOUSE_OUT, tb, null);
	            }
	        });

			tb.setParent(row);
		} else {
			new Label(deco.getCode()).setParent(row);
		}
		
		// codes organes : liste des codes exportés pour échantillons
		ObjectTypesFormatters
			.drawCodesExpLabel(deco.getCodesOrgsToCreateOrEdit(), row, 
														null, false);
		
		// codes lésionnels : liste des codes exportés pour échantillons
		ObjectTypesFormatters
			.drawCodesExpLabel(deco.getCodesLesToCreateOrEdit(), row, 
															null, false);
		
		// type
		if (deco.getType() != null) {
			new Label(deco.getType()).setParent(row);
		} else {
			new Label().setParent(row);
		}
		
		// quantité
		if (deco.getOnlyQuantiteInit() != null) {
			new Label(deco.getOnlyQuantiteInit()).setParent(row);
		} else {
			new Label().setParent(row);
		}
		
		// emplct
		if (deco.getEmplacementAdrlinMulti() != null) {
			new Label(deco.getEmplacementAdrlinMulti()).setParent(row);
		} else {
			new Label().setParent(row);
		}
		
		// objet statut
		if (deco.getStatut() != null) {
			new Label(Labels.getLabel("Statut." + deco.getStatut().getStatut())).setParent(row);
		} else {
			new Label().setParent(row);
		}
		
		// delete Image
		Image delImg = new Image();
		delImg.setWidth("12px");
		delImg.setHeight("12px");
		delImg.setStyle("cursor:pointer");
		delImg.setSrc("/images/icones/small_delete.png");
		delImg.addForward("onClick", row.getParent(), 
											"onDeleteDeco", deco);
		delImg.setVisible(deco.isNew());
		delImg.setParent(row);
	}
}
