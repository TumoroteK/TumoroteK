/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;

import fr.aphp.tumorotek.action.patient.ResumePatient;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementStatic;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.Contexte;

/**
 *
 * Controller gérant la fiche static d'un prélèvement.
 * CONTEXTE SEROTK
 * Controller créé le 25/05/2021.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FichePrelevementStaticGatsbi extends FichePrelevementStatic {

   private static final long serialVersionUID = -7612780578022559022L;
   
   private Div gatsbiContainer;
   

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      
      List<Div> itemDivs = GatsbiController.wireItemDivsFromMainComponent(gatsbiContainer);
      List<Div> blockDivs = GatsbiController.wireBlockDivsFromMainComponent(gatsbiContainer);

      Contexte c = GatsbiController.mockOneContexte();
      
      GatsbiController.showOrhideItems(itemDivs, blockDivs, c); // TODO replace by collection.contexte
      
   // prelevement specific
      groupLaboInter.setVisible(c.getSiteInter());
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
