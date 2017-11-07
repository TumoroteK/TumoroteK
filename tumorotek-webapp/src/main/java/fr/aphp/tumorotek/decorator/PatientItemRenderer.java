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
package fr.aphp.tumorotek.decorator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.patient.PatientUtils;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Collaborateur;

/** 
 * PatientRenderer affiche dans le listitem
 * les membres de Patient.
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 14/04/2010
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class PatientItemRenderer implements ListitemRenderer<Patient> {
	
	/**
	 * Constructeur.
	 * @param show pour afficher le medecin referents dans la row.
	 */
	public PatientItemRenderer(boolean show) {
		this.showMedecin = show;
	}
	
	private boolean showMedecin = false;
	private boolean isFusion = false;
	
	@Override
	public void render(Listitem li, Patient data, int index) {
        
		Patient pat = data;
		new Listcell(pat.getNip()).setParent(li); 
		new Listcell(pat.getNom()).setParent(li);
        new Listcell(pat.getPrenom()).setParent(li);	        
        new Listcell(PatientUtils.setSexeFromDBValue(pat))
        												.setParent(li);
        new Listcell(ObjectTypesFormatters
        		.dateRenderer2(pat.getDateNaissance())).setParent(li);
//        String dateN = null;
//    	if (pat.getDateNaissance() != null) {
//			Calendar c = Calendar.getInstance();
//			c.setTime(pat.getDateNaissance());
//			dateN = String.valueOf(c.get(Calendar.YEAR));
//		}
//		new Listcell(dateN).setParent(li);
        
        if (isFusion) {
        	 //new Listcell(String.valueOf(getNbPrelevements(pat)))
        	 	//.setParent(li);
        	drawPrelevementsLabelWithPopup(li, pat);
        } 
        
        if (showMedecin) {
        	drawMedecinsLabel(ManagerLocator
					.getPatientManager().getMedecinsManager(pat), li);
        }
        
        if (pat.getPatientId() == null) {
        	li.setStyle("background-color : #e2e9fe");
        } else {
        	li.setStyle("background-color : #beff96");
        }
    }
	
	public Integer getNbPrelevements(Patient pat) {
		Integer nb = 0;
		List<Maladie> maladies = ManagerLocator
			.getMaladieManager()
			.findByPatientNoSystemManager(pat);
		for (int i = 0; i < maladies.size(); i++) {
			nb += ManagerLocator
				.getMaladieManager().getPrelevementsManager(
					maladies.get(i)).size();
		}
		return nb;
	}
	
	/**
	 * Dessine dans un label le ou les libelles avec
	 * l'utilisation d'un tooltip pour afficher la totalité.
	 * @param
	 * @param row Parent
	 */
	private void drawPrelevementsLabelWithPopup(Listitem li, Patient pat) {
		Integer nb = 0;
		List<Maladie> maladies = ManagerLocator
			.getMaladieManager()
			.findByPatientNoSystemManager(pat);
		for (int i = 0; i < maladies.size(); i++) {
			nb += ManagerLocator
				.getMaladieManager().getPrelevementsManager(
					maladies.get(i)).size();
		}
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(nb));
		for (int i = 0; i < maladies.size(); i++) {
			Iterator<Prelevement> it = ManagerLocator
				.getMaladieManager().getPrelevementsManager(
					maladies.get(i)).iterator();
			
			while (it.hasNext()) {
				Prelevement p = it.next();
				if (!values.contains(p.getCode())) {
					values.add(p.getCode());
				}
			}
		}
		
		Listcell cell = new Listcell();
		cell.setParent(li);
		
		if (!values.isEmpty()) {
			Label label1 = new Label(values.get(0));
			label1.setSclass("formValue");
			// dessine le label avec un lien vers popup 
			if (values.size() > 1) {
				Hbox labelAndLinkBox = new Hbox();
				labelAndLinkBox.setSpacing("5px");
				Label moreLabel = new Label("...");
				moreLabel.setClass("formLink");
				Popup malPopUp = new Popup();
				malPopUp.setParent(cell);
				Iterator<String> it = values.iterator();
				// on saute la 1ère valeur contenant le nb de prlvts
				it.next();
				String next;
				Label libelleStaticLabel = null;
				Vbox popupVbox = new Vbox();
				while (it.hasNext()) {
					next = it.next(); 
					libelleStaticLabel = new Label(next);
					libelleStaticLabel.setSclass("formValue");
					
					popupVbox.appendChild(libelleStaticLabel);
				}
				malPopUp.appendChild(popupVbox);
				moreLabel.setTooltip(malPopUp);
				labelAndLinkBox.appendChild(label1);
				labelAndLinkBox.appendChild(moreLabel);
				labelAndLinkBox.setParent(cell);
			} else {
				label1.setParent(cell);
			}
		} else {
			new Label().setParent(cell);
		}
	}

	public boolean isFusion() {
		return isFusion;
	}

	public void setFusion(boolean is) {
		this.isFusion = is;
	}
	
	/**
	 * Dessine dans un label le nom (et prenom) des medecins referents. 
	 * Utilisation d'un tooltip pour afficher la totalité des medecines 
	 * suivant cette même règle.
	 * @param Listiem li
	 * @param Component Parent
	 */
	public static void drawMedecinsLabel(List<Collaborateur> meds, Listitem li) {
		
		if (meds != null && !meds.isEmpty()) {
			
			Label c1Label = new Label(meds.get(0).getNom());
			// dessine le label avec un lien vers popup 
			if (meds.size() > 1) {
				Hlayout labelAndLinkBox = new Hlayout();
				labelAndLinkBox.setSpacing("5px");
				Label moreLabel = new Label("...");
				moreLabel.setClass("formLink");
				
				Popup malPopUp = new Popup();
				malPopUp.setParent(li.getParent().getParent().getParent().getParent().getParent());
				
				Label lab;
				Vbox popupVbox = new Vbox();
				String labStr;
				for (int i = 0; i < meds.size(); i++) {
					labStr = meds.get(i).getNom();
					lab = new Label(labStr);
					lab.setSclass("formValue");
					popupVbox.appendChild(lab);
				}
				
				malPopUp.appendChild(popupVbox);
				moreLabel.setTooltip(malPopUp);
				labelAndLinkBox.appendChild(c1Label);
				labelAndLinkBox.appendChild(moreLabel);
	
				Listcell cell = new Listcell();
				labelAndLinkBox.setParent(cell);
				cell.setParent(li);
			} else {
				Listcell cell = new Listcell();
				c1Label.setParent(cell);
				cell.setParent(li);
			}
		} else {
			Listcell cell = new Listcell();
			cell.setParent(li);
		}
	}
}

