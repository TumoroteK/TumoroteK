/**
 * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.patient.ResumePatient;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementEdit;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.Contexte;

/**
 *
 * Controller gérant la fiche formulaire d'un prélèvement sous le 
 * gestionnaire GATSBI.
 * Controller créé le 25/05/2021.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FichePrelevementEditGatsbi extends FichePrelevementEdit {

	private static final long serialVersionUID = 1L;

	private Div gatsbiContainer;

	private List<Listbox> reqListboxes = new ArrayList<Listbox>();
	List<Div> itemDivs  = new ArrayList<Div>();
	List<Div> blockDivs  = new ArrayList<Div>();
	
	private Contexte c;


	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);

		try {
			itemDivs.addAll(GatsbiController.wireItemDivsFromMainComponent(gatsbiContainer));
			blockDivs.addAll(GatsbiController.wireBlockDivsFromMainComponent(gatsbiContainer));

			c = GatsbiController.mockOneContexte();

			GatsbiController.showOrhideItems(itemDivs, blockDivs, c); // TODO replace by collection.contexte
			GatsbiController.switchItemsRequiredOrNot(itemDivs, c, reqListboxes, 
									new ArrayList<Combobox>(), new ArrayList<Div>());

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

		log.debug("Surcharge Gastbi pour conserver sélectivement la "
				+ "contrainte de sélection des listes nature et statut juridique ");
		
		GatsbiController.checkRequiredNonInputComponents(reqListboxes, null, null);
	}

	/**
	 * Processing echoEvent.
	 * Gatsbi surcharge... si aucun champ de formulaire dans la page de 
	 * transfert vers le site de stockage, passe directement à l'échantillon.
	 *
	 * @see onClick$next
	 */
	@Override
	public void onLaterNextStep(){
		
		log.debug("Surcharge Gastbi pour vérifier que la page de transfert des sites intermédiaire est affichée");
		
		// vérifie si au moins un des champs de formulaires est affiché
		boolean oneDivVisible = c.getChampEntites().stream()
			.filter(c -> Arrays.asList(35, 36, 37, 38, 39, 40, 256, 267, 268).contains(c.getChampId()))
			.anyMatch(c -> c.getVisible());

		if (oneDivVisible || c.getSiteInter()) {
			super.onLaterNextStep();
		} else { // aucun formulaire n'est affiché -> passage direct à l'onglet échantillon
			log.debug("Aucun formulaire à affiché dans la page transfert vers le site préleveur...");
	      if(this.prelevement.getPrelevementId() != null){
	         getObjectTabController().switchToMultiEchantillonsEditMode(this.prelevement, new ArrayList<LaboInter>(), new ArrayList<LaboInter>());
	      }else{
	         // si nous sommes dans une action de création, on
	         // appelle la page FicheMultiEchantillons en mode create
	         getObjectTabController().switchToMultiEchantillonsCreateMode(this.prelevement, new ArrayList<LaboInter>());
	      }

	      Clients.clearBusy();
		}
		
	}
}