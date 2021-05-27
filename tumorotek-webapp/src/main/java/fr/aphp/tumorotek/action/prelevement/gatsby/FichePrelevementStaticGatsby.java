/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.action.prelevement.gatsby;

import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;

import fr.aphp.tumorotek.action.prelevement.FichePrelevementStatic;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;

/**
 *
 * Controller gérant la fiche static d'un prélèvement.
 * CONTEXTE SEROTK
 * Controller créé le 19/01/2012.
 *
 * @author mathieu BARTHELEMY
 * @version 2.0.6
 *
 */
public class FichePrelevementStaticGatsby extends FichePrelevementStatic
{

//   protected Groupbox gridFormPrlvtComp;
//   protected Groupbox groupPatient;
//   protected Div groupLaboInter;
//   protected Groupbox groupEchans;
//   protected Groupbox groupDerivesPrlvt;

   private static final long serialVersionUID = -7612780578022559022L;
   
   private Div codeDiv;
   private Div codeLaboDiv;
   private Div natureDiv;
   private Div nipDiv;
   private Div ndaDiv;
   private Div nomDiv;
   private Div prenomDiv;
   private Div dateNaisDiv;
   private Div sexeDiv;

   
   @Override
   protected void disablePatientGroup() {
	   boolean enable = (this.maladie != null || this.prelevement.equals(new Prelevement()));
	   ((Groupbox) this.groupPatient).setOpen(enable);
	   ((Groupbox) this.groupPatient).setClosable(enable);
   }
}
