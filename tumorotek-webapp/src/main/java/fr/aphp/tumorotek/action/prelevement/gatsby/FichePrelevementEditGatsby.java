/**
 * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.action.prelevement.gatsby;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;

import fr.aphp.tumorotek.action.patient.ResumePatient;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementEdit;
import fr.aphp.tumorotek.webapp.gatsby.client.json.Contexte;


public class FichePrelevementEditGatsby extends FichePrelevementEdit {
	
	private static final long serialVersionUID = 1L;
	
	   private Div gatsbyContainer;
	
	 @Override
	   public void doAfterCompose(final Component comp) throws Exception{
	      super.doAfterCompose(comp);
	      
	      List<Div> divs = GatsbyController.wireDivsFromMainComponent(gatsbyContainer);
	    
	      GatsbyController.showOrhideItems(divs, GatsbyController.mockOneContexte()); // TODO replace by collection.contexte
	   }

	@Override
	protected ResumePatient initResumePatient() {
		return new ResumePatient(groupPatient, true);
	}


	@Override
	protected void enablePatientGroup(boolean b) {
		((Groupbox) this.groupPatient).setOpen(b);
		((Groupbox) this.groupPatient).setClosable(b);
	}
}