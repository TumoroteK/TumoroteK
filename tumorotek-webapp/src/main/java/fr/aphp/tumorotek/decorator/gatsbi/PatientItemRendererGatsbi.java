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
package fr.aphp.tumorotek.decorator.gatsbi;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.patient.PatientConstraints;
import fr.aphp.tumorotek.decorator.PatientItemRenderer;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;

/**
 * PatientRenderer affiche dans le listitem
 * les membres de Patient.
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 14/04/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 */
public class PatientItemRendererGatsbi extends PatientItemRenderer
{

   private Banque banque;
   private Contexte contexte;
      
   private Textbox identifiantBox;
   
   private boolean isIdentifiantEditable = false;
      
   public PatientItemRendererGatsbi(boolean showMedecin, boolean _i){
      super(showMedecin);
      this.isIdentifiantEditable = _i;
   }

   @Override
   protected void renderPatient(Listitem li, Patient pat){
      
      //TG-182 : affectation du patient à la banque courante :
      pat.setBanque(banque);
      identifiantBox = renderIdentifiantForBanque(li, pat);
      
      if (contexte.isChampIdVisible(2)) {
         renderNip(li, pat);
      }
      
      if (contexte.isChampIdVisible(3)) {
         renderNom(li, pat);
      }
      
      if (contexte.isChampIdVisible(5)) {
         renderPrenom(li, pat);
      }
      
      if (contexte.isChampIdVisible(6)) {
      renderSexe(li, pat);
      }
      
      if (contexte.isChampIdVisible(7)) {
      renderDateNais(li, pat);  
      }
   }

   private Textbox renderIdentifiantForBanque(Listitem li, Patient pat) {
            
      // textbox pour ajouter un identifiant
      if(isIdentifiantEditable && !pat.hasIdentifiant()){
         // tb.setInplace(true);
        final Textbox tb = new Textbox();
         tb.setAttribute("patient", pat);
         tb.setConstraint(PatientConstraints.getCodeConstraint());
         Listcell cell = new Listcell();
         tb.setParent(cell);
         cell.setParent(li);
         
         return tb;
      }else{ // affichage identifiant
         new Listcell(pat.getIdentifiantAsString()).setParent(li);
      }
      
      return null;
   }

   public Banque getBanque(){
      return banque;
   }

   public void setBanque(Banque banque){
      this.banque = banque;
   }

   public Contexte getContexte(){
      return contexte;
   }

   public void setContexte(Contexte contexte){
      this.contexte = contexte;
      setShowMedecin(contexte.isChampIdVisible(227));
   }

   public Textbox getIdentifiantBox(){
      return identifiantBox;
   }
}