/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zul.ListitemRenderer;

import fr.aphp.tumorotek.action.patient.gatsbi.GatsbiControllerPatient;
import fr.aphp.tumorotek.action.prelevement.ReferenceurPatient;
import fr.aphp.tumorotek.decorator.gatsbi.PatientItemRendererGatsbi;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 * @see <a href="http://docs.zkoss.org/wiki/Macro_Component">Macro_Component</a>
 * <p>
 */
public class ReferenceurPatientGatsbi extends ReferenceurPatient
{

   private static final long serialVersionUID = 1L;
   
   private Contexte contextePatient;
   
   private PatientItemRendererGatsbi patientRendererGatsbi = 
      new PatientItemRendererGatsbi(true);
   
   @Override
   public void doAfterCompose(Component comp) throws Exception{
      super.doAfterCompose(comp);
       
      contextePatient = SessionUtils.getCurrentGatsbiContexteForEntiteId(1);
      
      Map<Integer, String> widths = new HashMap<Integer, String>();
      widths.put(6, "50px"); // sexe listheader 50px
      
      GatsbiControllerPatient.drawListheadersForPatients(contextePatient, patientsBox, false, widths);  
      
      patientRendererGatsbi.setContexte(contextePatient);
      patientRendererGatsbi.setBanque(SessionUtils.getCurrentBanque(sessionScope));
   }

   public void onClick$goForIt(){

      final String critereValue = nomNipNdaBox.getValue();

      final FichePrelevementEditGatsbi fichePrelevementEdit = 
            (FichePrelevementEditGatsbi) getFichePrelevementEditFromContexte();

      fichePrelevementEdit.getObjectTabController().setPatientSip(null);
      fichePrelevementEdit.openSelectPatientWindow(Path.getPath(self), "onGetPatientFromSelection", 
         false, critereValue, null, contextePatient, SessionUtils.getCurrentBanque(sessionScope));

   }
   
   @Override
   public ListitemRenderer<Patient> getPatientRenderer(){
      return patientRendererGatsbi;
   }
}
