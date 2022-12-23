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
package fr.aphp.tumorotek.action.patient.gatsbi;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.patient.PatientRowRenderer;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.RowRendererGatsbi;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Controller gérant le rendu dynamique des lignes du tableau prélèvement sous
 * le gestionnaire GATSBI. Ecris donc toutes les colonnes possibles, mais dans
 * l'ordre spécifié par le contexte Gatsbi.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsi
 */
public class PatientRowRendererGatsbi extends PatientRowRenderer implements RowRendererGatsbi
{

   private final Contexte contexte;
   private Banque curBanque;
   
   // si true, dessine le premier code organe exporté pour un patient 
   private boolean organes = true;

   public PatientRowRendererGatsbi(final boolean select, final boolean _o){
      super(select);
      
      this.organes = _o;

      contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(1);
   }

   @Override
   protected void renderPatient(final Row row, final Patient pat)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{

      // identifiant / clickable
      // TODO toutes collections ? Quel identifiant afficher ? 
      Label identifiant = new Label(pat.hasIdentifiant(curBanque) ? 
         (pat.getIdentifiantAsString(curBanque)) : Labels.getLabel("gatsbi.patient.include"));
      
      Component parent = null; // -> remonte l'évènement jusqu'au ListeController
      identifiant.addForward(null, parent, "onClickObject", pat);
      identifiant.setClass("formLink");    
      identifiant.setParent(row);
      
      for(final Integer chpId : contexte.getChampEntiteInTableauOrdered()){
         GatsbiControllerPatient.applyPatientChpRender(chpId, row, pat, anonyme);
      }

      renderNbPrels(row, pat);
      
      if (organes) {
         renderFirstCodeOrganeForPatient(row, pat);
      }
   }
   
   @Override
   public boolean areIconesRendered(){
      return false;
   }

   public void setCurBanque(Banque curBanque){
      this.curBanque = curBanque;
   }
}
