/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.model.contexte.gatsbi.ContexteType;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Gatsbi controller regroupant les fonctionalités de modification dynamique de
 * l'interface spécifique aux prélèvements
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class GatsbiControllerPrelevement {

   public static void applyPatientContext(Component groupPatient, boolean edit) {
      
      List<Div> items = GatsbiController.wireItemDivsFromMainComponent(ContexteType.PATIENT, groupPatient);
      GatsbiController.showOrhideItems(items, null, SessionUtils.getCurrentGatsbiContexteForEntiteId(1));
      
      Div ndaDiv = (Div) groupPatient.getFellowIfAny("ndaDiv");      
      if (ndaDiv != null && ndaDiv.isVisible()) {
         // identifiantDiv partage la ligne avec ndaDiv
         ((Div) ndaDiv.getPreviousSibling()).setSclass("item item-mid");
      }
   }
   
   public static void applyPatientNdaRequired(Div ndaDiv) {  
      if (ndaDiv.isVisible()) {
         if (SessionUtils.getCurrentGatsbiContexteForEntiteId(2).isChampIdRequired(44)) {
            ((ConstCode) ((Textbox) ndaDiv.getLastChild()).getConstraint()).setNullable(false);         
            applyPatientNdaRequiredLabel(ndaDiv);
         } else {
            removePatientNdaRequired(ndaDiv);
         }
      }
   }
   
   public static void applyPatientNdaRequiredLabel(Div ndaDiv) { 
      if (ndaDiv.isVisible() && SessionUtils.getCurrentGatsbiContexteForEntiteId(2).isChampIdRequired(44)
           && !ndaDiv.getSclass().contains("item-required")) {
           ndaDiv.setSclass(ndaDiv.getSclass().concat(" item-required"));
      }
   }
   
   public static void removePatientNdaRequired(Div ndaDiv) {  
      ((ConstCode) ((Textbox) ndaDiv.getLastChild()).getConstraint()).setNullable(true);
   }
}