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
package fr.aphp.tumorotek.action.cession;

import java.util.Calendar;
import java.util.Map;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModelMap;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.component.CalendarBox;

public class DateRetourCompleteModale extends GenericForwardComposer<Window> {

		
	private static final long serialVersionUID = 1L;
	
	
	// @Wire("#fwinDateRetourComplete")
	private Window fwinDateRetourComplete;
	// @Wire("#incompEchansRetourLayout")
	private Vlayout incompEchansRetourLayout;
	// @Wire("#incompDerivesRetourLayout")
	private Vlayout incompDerivesRetourLayout;
	
	private Listbox echansRetoursDetailsList;
	private Listbox derivesRetoursDetailsList;
	
	private CalendarBox dateRetourEchansCalBox;
	private CalendarBox dateRetourDerivesCalBox;
	private FicheCessionEdit cessionEditController;
	
	private Map<String,String> echansRetourIncompletes;
	private Map<String,String> derivesRetourIncompletes;
	
//	@AfterCompose
//    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
//        Selectors.wireComponents(view, this, false);
//        if (dateRetourEchans == null) {
//			incompEchansRetourLayout.setVisible(false);
//		}
//		if (dateRetourDerives == null) {
//			incompDerivesRetourLayout.setVisible(false);
//		}
//		fwinDateRetourComplete.doModal();
//    }
    
    public void init(Map<String,String> erInc,
			Map<String,String> dcInc,
			Calendar dateInit, FicheCessionEdit cont) {
    	echansRetourIncompletes = erInc;
    	derivesRetourIncompletes = dcInc;
        cessionEditController = cont;
    	if (echansRetourIncompletes.isEmpty()) {
  			incompEchansRetourLayout.setVisible(false);
  			dateRetourEchansCalBox.setConstraint(null);
  		} else {
  			dateRetourEchansCalBox.setValue(dateInit != null ? dateInit : Calendar.getInstance());
  			echansRetoursDetailsList
  				.setModel(new ListModelMap<String,String>(echansRetourIncompletes));
  		}
  		if (derivesRetourIncompletes.isEmpty()) {
  			incompDerivesRetourLayout.setVisible(false);
  			dateRetourDerivesCalBox.setConstraint(null);
  		} else {
  			dateRetourDerivesCalBox.setValue(dateInit != null ? dateInit : Calendar.getInstance());
  			derivesRetoursDetailsList
				.setModel(new ListModelMap<String,String>(derivesRetourIncompletes));
  		}
  		// fwinDateRetourComplete.doModal();
    }
	
//	@Init
//	public void init(@ExecutionArgParam("cal1") Calendar cal1, 
//			@ExecutionArgParam("cal2") Calendar cal2, 
//			@ExecutionArgParam("controller") FicheCessionEdit cont) {
//		if (cal1 != null) {
//			setDateRetourEchans(cal1.getTime());
//		}
//		if (cal2 != null) {
//			setDateRetourDerives(cal2.getTime());
//		}
//		controller = cont;
//	}
	
	// @Command
//	public void validate() {
//		controller.setDateRetourEchansComplete(dateRetourEchans);
//		controller.setDateRetourDerivesComplete(dateRetourDerives);
//		fwinDateRetourComplete.onClose();
//	}
    
    public void onClick$validate() {
    	cessionEditController.setDateRetourEchansComplete(dateRetourEchansCalBox.getValue());
    	cessionEditController.setDateRetourDerivesComplete(dateRetourDerivesCalBox.getValue());
    	// retour à l'exécution du thread
    	fwinDateRetourComplete.onClose();
    }
	
    public void onClick$cancel() {
    	cessionEditController.setCancelException(new CancelFromModalException());
    	fwinDateRetourComplete.onClose();
    }
    
	/**********************************************************/
	/****************** GETTERS - SETTERS *********************/
	/**********************************************************/

	public FicheCessionEdit getCessionEditController() {
		return cessionEditController;
	}

	public void setCessionEditController(FicheCessionEdit controller) {
		this.cessionEditController = controller;
	}
}