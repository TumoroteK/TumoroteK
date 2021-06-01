/**
 * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.patient.ResumePatient;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementEdit;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.Contexte;


public class FichePrelevementEditGatsbi extends FichePrelevementEdit {

	private static final long serialVersionUID = 1L;

	private Div gatsbiContainer;

	private List<Listbox> reqListboxes = new ArrayList<Listbox>();


	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);

		try {
			List<Div> itemDivs = GatsbiController.wireItemDivsFromMainComponent(gatsbiContainer);
			List<Div> blockDivs = GatsbiController.wireBlockDivsFromMainComponent(gatsbiContainer);
	
			Contexte c = GatsbiController.mockOneContexte();
	
			GatsbiController.showOrhideItems(itemDivs, blockDivs, c); // TODO replace by collection.contexte
			GatsbiController.switchItemsRequiredOrNot(itemDivs, c, reqListboxes);
			
			GatsbiController.appliThesaurusValues(itemDivs, c, this);
		} catch (Exception e) {
            Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
			log.debug(e);
		}

		// prelevement specific
		// groupLaboInter.setVisible(c.getSiteInter());
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

	@Override
	protected void checkRequiredListboxes() {

		for (Listbox lb : reqListboxes) {
			Clients.clearWrongValue(lb);
			if (lb.getSelectedItem() == null) {
				Clients.scrollIntoView(lb);
				throw new WrongValueException(lb, Labels.getLabel("validation.syntax.empty"));
			}
		}
	}
}