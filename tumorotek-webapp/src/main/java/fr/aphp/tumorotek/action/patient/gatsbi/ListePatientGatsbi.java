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

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import fr.aphp.tumorotek.action.patient.ListePatient;
import fr.aphp.tumorotek.action.prelevement.gatsbi.exception.GatsbiException;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @version 2.3.0-gatsbi
 * @author Mathieu BARTHELEMY
 *
 */
public class ListePatientGatsbi extends ListePatient
{

   private static final long serialVersionUID = 1L;

   private Contexte contexte;
   
   private boolean firstOrganeCode = false;

   public ListePatientGatsbi(){
      
      contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(1);
      
      firstOrganeCode = SessionUtils
         .getCurrentGatsbiContexteForEntiteId(3).isChampInTableau(229);
      
      setListObjectsRenderer(new PatientRowRendererGatsbi(true, firstOrganeCode));
   }

   public void onCheckAll$gridColumns(){
      onCheck$checkAll();
   }

   @Override
   protected void drawColumnsForVisibleChampEntites()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{

      // check box first column, toujours affichée
      final Checkbox cbox = new Checkbox();
      cbox.setId("checkAll");
      cbox.addForward("onCheck", objectsListGrid.getColumns(), "onCheckAll");
      GatsbiController.addColumn(objectsListGrid, null, "40px", null, cbox, null, true);

      // ,om
      GatsbiController.addColumn(objectsListGrid, "general.code", null, null, null, "auto(code)", true);

      // variable columns
      for(final Integer chpId : contexte.getChampEntiteInTableauOrdered()){
         addColumnForChpId(chpId);
      }
      
      // nb prels
      GatsbiController.addColumn(objectsListGrid, "patient.nbPrelevements", 
                                             null, null, null, "auto", true);
      
      // premier code organe
      GatsbiController.addColumn(objectsListGrid, "Champ.Echantillon.Organe", 
                                    null, null, null, null, firstOrganeCode);
   }

   private void addColumnForChpId(final Integer chpId)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      switch(chpId){
         case 2: // nip
            GatsbiController.addColumn(objectsListGrid, "Champ.Patient.Nip", null, null, null, null,
               true);
            break;
         case 3: // nom
            GatsbiController.addColumn(objectsListGrid, "Champ.Patient.Nom", null, null, null, "auto(nom)",
               true);
            break;
         case 4: // nom naissance
            GatsbiController.addColumn(objectsListGrid, "Champ.Patient.NomNaissance", null, null, null, null,
               true);
            break;
         case 5: // prenom
            GatsbiController.addColumn(objectsListGrid, "Champ.Patient.Prenom", null, null, null, null,
               true);
            break;
         case 6: // sexe
            GatsbiController.addColumn(objectsListGrid, "Champ.Patient.Sexe", null, null, null, "auto(sexe)",
               true);
            break;
         case 7: // date naissance
            GatsbiController.addColumn(objectsListGrid, "Champ.Patient.DateNaissance", null, null, null, "auto(dateNaissance)",
               true);
            break;
         case 8: // ville naissance
            GatsbiController.addColumn(objectsListGrid, "Champ.Patient.VilleNaissance", null, null, null, null,
               true);
            break;           
         case 9: // pays naissance
            GatsbiController.addColumn(objectsListGrid, "Champ.Patient.PaysNaissance", null, null, null, null,
               true);
            break;
         case 10: // patient état
            GatsbiController.addColumn(objectsListGrid, "Champ.Patient.PatientEtat", null, null, null, null,
               true);
            break;
         case 11: // date état
            GatsbiController.addColumn(objectsListGrid, "Champ.Patient.DateEtat", null, null, null, "auto(dateEtat)",
               true);
            break;
         case 12: // date décès
            GatsbiController.addColumn(objectsListGrid, "Champ.Patient.DateDeces", null, null, null, "auto(dateDeces)",
               true);
            break;
         case 227: // medecins
            GatsbiController.addColumn(objectsListGrid, "patient.medecins", null, null, null, null,
               true);
            break;       
         default:
            break;
      }
   }

   /**
    * Gatsbi surcharge pour intercaler une modale de sélection des parametrages
    * proposés par le contexte.
    *
    * @param click event
    */
   @Override
   public void onClick$addNew(final Event event) throws Exception{
      GatsbiController.addNewObjectForContext(contexte, self, e -> {
         try{
            super.onClick$addNew(e);
         }catch(final Exception ex){
            Messagebox.show(handleExceptionMessage(ex), "Error", Messagebox.OK, Messagebox.ERROR);
         }
      }, event, null);
   }

   /**
    * Un parametrage a été sélectionné.
    *
    * @param param
    * @throws Exception
    */
   public void onGetSelectedParametrage(final ForwardEvent evt) throws Exception{

      try{
         GatsbiController.getSelectedParametrageFromSelectEvent(contexte, SessionUtils.getCurrentBanque(sessionScope),
            getObjectTabController(), null, () -> {
               try{
                  super.onClick$addNew(null);
               }catch(final Exception ex){
                  Messagebox.show(handleExceptionMessage(ex), "Error", Messagebox.OK, Messagebox.ERROR);
               }
            }, evt);
      }catch(final GatsbiException e){
         Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }
}
