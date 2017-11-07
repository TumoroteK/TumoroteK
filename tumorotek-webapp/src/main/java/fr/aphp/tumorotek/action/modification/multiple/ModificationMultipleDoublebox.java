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
package fr.aphp.tumorotek.action.modification.multiple;

import java.math.BigDecimal;

import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Decimalbox;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'un
 * Doublebox. Gère la modification d'un champ de type 
 * Integer si isInteger = true et Float si isFloat = true.
 * Classe créée le 30/04/10.
 * 
 * @author Mathieu BARTHELEMY
 *
 */
public class ModificationMultipleDoublebox 
extends AbstractModificationMultipleComponent {

	private static final long serialVersionUID = 4487271130091905174L;
	
	/**
	 * Components specific Datebox.
	 */
	private Decimalbox multiDoublebox;
	private Decimalbox eraseMultiDoublebox;	
	
	private boolean isInteger = false;
	private boolean isFloat = false;

	@Override
	public Object extractValueFromEraserBox() {
		if (eraseMultiDoublebox.getValue() != null) {
			if (isInteger) {
				return new Integer(eraseMultiDoublebox.getValue().intValue());
			} else if (isFloat) {
				return ObjectTypesFormatters
						.floor(eraseMultiDoublebox.getValue().floatValue(), 3);
			}
		}
		return eraseMultiDoublebox.getValue();
	}

	@Override
	public Object extractValueFromMultiBox() {
		if (multiDoublebox.getValue() != null) {
			if (isInteger) {
				return new Integer(multiDoublebox.getValue().intValue());
			} else if (isFloat) {
				return ObjectTypesFormatters
						.floor(multiDoublebox.getValue().floatValue(), 3);
			}
		}
		return multiDoublebox.getValue();
	}
	
	@Override
	public Object formatValue(Object obj) {
		if (obj instanceof String) {
			if (getIsCombined() && obj != null
					&& obj.equals("system.tk.unknownExistingValue")) {
				return obj;
			}
			if (isInteger) {
				return new Integer((String) obj);
			} else if (isFloat) {
				return ObjectTypesFormatters
						.floor(new Float((String) obj), 3);
			} else {
				return new BigDecimal((String) obj);
			}
		} else {
			return obj;
		}
	}

	@Override
	public String formatLocalObject(Object obj) {
		// verifie la presence annotation combine
		if (getIsCombined() && obj != null
				&& obj.equals("system.tk.unknownExistingValue")) {
			return Labels.getLabel("system.tk.unknownExistingValue");
		}
		if (obj != null) { 
			if (!(obj instanceof String)) {
				if (obj instanceof Integer) {
					return obj.toString();
				} else if (obj instanceof Float) {
					return String.valueOf(ObjectTypesFormatters
						.floor((Float) obj, 3)); 
				}
				return ObjectTypesFormatters
							.doubleLitteralFormatter(((BigDecimal) obj).doubleValue()); 
			} else {
				return (String) obj;
			}
		} else {
			return null;
		}
		
		
	}

	@Override
	public void setConstraintsToBoxes(Constraint constr) {
		multiDoublebox.setConstraint(constr);
		eraseMultiDoublebox.setConstraint(constr);
	}

	@Override
	public void setEraserBoxeVisible(boolean visible) {
		eraseMultiDoublebox.setVisible(visible);		
	}
	
	public void setInteger(boolean i) {
		this.isInteger = i;
	}
	
	public void setFloat(boolean f) {
		this.isFloat = f;
	}

	@Override
	public AnnotateDataBinder getBinder() {
		return ((AnnotateDataBinder) self
				.getParent()
				.getAttributeOrFellow("modificationDoublebox", true));
	}

	@Override
	public void passValueToEraserBox() {
		if (getValues()
				.get(multiListBox.getSelectedIndex()) != null) {
			if (isInteger) {
				eraseMultiDoublebox
					.setRawValue(new Double((Integer) getValues()
						.get(multiListBox.getSelectedIndex())));
			} else if (isFloat) {
				eraseMultiDoublebox
				.setRawValue(new Double((Float) getValues()
					.get(multiListBox.getSelectedIndex())));
			} else {
				eraseMultiDoublebox
					.setRawValue(getValues()
						.get(multiListBox.getSelectedIndex()));
			}
		} else {
			eraseMultiDoublebox.setRawValue(null);
		}
	}
	
	@Override
	public void passNullToEraserBox() {
		eraseMultiDoublebox.setRawValue(null);
	}
}
