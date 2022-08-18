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
import java.util.stream.Collectors;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.patient.PatientRowRenderer;
import fr.aphp.tumorotek.action.patient.PatientUtils;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
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

      for(final Integer chpId : contexte.getChampEntiteInTableauOrdered()){
         applyPatientChpRender(chpId, row, pat);
      }

      renderNbPrels(row, pat);
      
      if (organes) {
         renderFirstCodeOrganeForPatient(row, pat);
      }
   }

   /**
    * Applique la methode de rendering correspondant au champEntité id passé en
    * paramètre
    *
    * @param chpId
    * @throws NoSuchMethodException
    * @throws InvocationTargetException
    * @throws IllegalAccessException
    * @throws ParseException
    */
   private void applyPatientChpRender(final Integer chpId, final Row row, final Patient pat)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{

      switch(chpId){
         case 2: // nip
            renderAnonymisableAndClickableAlphanumProperty(row, pat, "nip", anonyme, "onClickObject", pat);
            break;
         case 3: // nom
            renderAnonymisableAndClickableAlphanumProperty(row, pat, "nom", anonyme, "onClickObject", pat);
            break;
         case 4: // nom naissance
            renderAnonymisableAndClickableAlphanumProperty(row, pat, "nomNaissance", anonyme, null, null);
            break;
         case 5: // prenom
            renderAnonymisableAndClickableAlphanumProperty(row, pat, "prenom", anonyme, null, null);
            break;
         case 6: // sexe
            renderSexe(row, pat);
            break;
         case 7: // date naissance
            renderDateNaissance(row, pat, anonyme);
            break;
         case 8: // ville naissance
            renderAlphanumPropertyAsStringNoFormat(row, pat, "villeNaissance");
            break;
         case 9: // pays naissance
            renderAlphanumPropertyAsStringNoFormat(row, pat, "villeNaissance");
            break;
         case 10: // état
            renderPatientEtat(row, pat);
            break;
         case 11: // date état
            renderDateProperty(row, pat, "dateEtat");
            break;
         case 12: // date décès
            renderDateProperty(row, pat, "dateDeces");
            break;
         case 227: // médecins
            renderMedecinsProperty(row, pat);
            break;

         default:
            break;
      }
   }

   private void renderSexe(Row row, Patient pat){
      if(pat.getSexe() != null){
         new Label(PatientUtils.setSexeFromDBValue(pat)).setParent(row);
      }else{
         new Label().setParent(row);
      }
   }

   private void renderMedecinsProperty(Row row, Patient pat){
      drawListStringLabel(row, 
         ManagerLocator.getPatientManager().getMedecinsManager(pat)
            .stream().map(c -> c.getNomAndPrenom())
            .collect(Collectors.toList()));
   }
   
   @Override
   public boolean areIconesRendered(){
      return false;
   }
}
