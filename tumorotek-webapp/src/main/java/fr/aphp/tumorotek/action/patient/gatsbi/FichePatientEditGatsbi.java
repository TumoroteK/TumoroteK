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

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;

import fr.aphp.tumorotek.action.patient.FichePatientEdit;
import fr.aphp.tumorotek.action.patient.LabelCodeItem;
import fr.aphp.tumorotek.action.patient.PatientUtils;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.gatsbi.PatientIdentifiant;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche formulaire d'un prélèvement sous le gestionnaire
 * GATSBI. Controller créé le 29/08/2022.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FichePatientEditGatsbi extends FichePatientEdit
{

   private static final long serialVersionUID = 1L;

   private final List<Listbox> reqListboxes = new ArrayList<>();

   private Contexte contexte;
   
   private Div ndaDiv;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      contexte = GatsbiController.initWireAndDisplay(this, 1, true, reqListboxes, null, null, new Groupbox[]{});
   }
   
   @Override
   public LabelCodeItem setSexeItemFromDBValue(final Patient pat){
      if(pat.getSexe() == null) {
         if (contexte.isChampIdRequired(6)) { // null mais champ obligatoire -> valeur par défaut
            return PatientUtils.SEXE_IND;
         } else {
            return null;
         }
      } else {
         return super.setSexeItemFromDBValue(pat);
      }
   }
   
   @Override
   public LabelCodeItem setPatientEtatFromValue(final Patient pat){
      if(pat.getPatientEtat() == null) { 
         if (contexte.isChampIdRequired(10)) { // null mais champ obligatoire -> valeur par défaut
            return PatientUtils.ETAT_I;
         } else {
            return null;
         }
      } else {
         return super.setPatientEtatFromValue(pat);
      }
   }
   
   /**
    * Etat peut être null
    */
   @Override
   public void setEmptyToNullEtat() {
      if(this.selectedEtat != null){
         this.patient.setPatientEtat(this.selectedEtat.getCode());
      }
   }
   
   /**
    * Surcharge Gastbi pour conserver sélectivement la
    * contrainte de sélection obligatoire des listes nature et statut juridique
    * dans le contexte TK historique
    */
   @Override
   protected void checkRequiredListboxes(){
      GatsbiController.checkRequiredNonInputComponents(reqListboxes, null, null);
   }
   
   /**
    * Surcharge Gastbi car accord affichage entre date état/décès et état 
    * n'est plus applicable.
    */
   @Override
   protected void accordDateToEtat() {
   }
   
   @Override
   protected void recordDateEtatDeces() {
   }
   
   @Override
   protected void setReferentsGroupOpen(boolean _o) {
      ((Groupbox) referentsGroup).setOpen(_o);
   }
   
   @Override
   public void setEmbedded(Patient pat){
      super.setEmbedded(pat);
      ndaDiv.setVisible(true);
   }
   
   // TODO toutes collections ? Afficher une liste ?
   public String getIdentifiant() {
      return patient.getIdentifiantAsString(SessionUtils.getCurrentBanque(sessionScope));
   }
   
   // TODO toutes collections = non modifiable
   public void setIdentifiant(String identifiant) {
      PatientIdentifiant currPatIdent = patient.getIdentifiant(SessionUtils.getCurrentBanque(sessionScope));
      currPatIdent.setIdentifiant(identifiant);
      
      // met à jour (si nécessaire) la relation patient-banque-identifiant
      patient.addToIdentifiants(currPatIdent);
   }
}