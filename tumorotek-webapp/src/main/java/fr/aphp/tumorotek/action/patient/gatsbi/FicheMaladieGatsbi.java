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

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.patient.FicheMaladie;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.ContexteType;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche static d'un prélèvement GATSBI.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FicheMaladieGatsbi extends FicheMaladie
{

   private static final long serialVersionUID = -7612780578022559022L;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      super.doAfterCompose(comp);
      
      // devenu inutile
      setRequiredMarks(new Component[] {});

      GatsbiController.initWireAndDisplay(this, 7, false, null, null, null, new Groupbox[]{});
      
      // Injection contexte prélèvement pour inner list maladies
      // ce contexte peut être (null) non paramétré pour l'étude
      // donc GET le contexte defaut pour le ContexteType Prélèvement
      Contexte prelContexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(2);
      if (prelContexte == null) {
         prelContexte = GatsbiController.getGastbiDefautContexteForType(ContexteType.PRELEVEMENT);
         // TODO PrelevementItemRenderer -> passer en mode Gatsbi
         // prelevementRendererGatsbi.setContexte(prelContexte);
         // prelevementFromOtherBanksRendererGatsbi.setContexte(prelContexte);
         // setPrelevementFromOtherBanksRenderer(prelevementFromOtherBanksRendererGatsbi);
      }
      

      // inner list
      // non deletable
      // ne force pas affichage emplacement et statut stockage en fin de grid
      // GatsbiControllerPrelevement.drawColumnsForPrelevements(prelContexte,
      //   echantillonsGrid, echantillonRendererGatsbi, false, false, getTtesCollections());
   }

   @Override
   public void onClick$addPrelevement(){

      final PrelevementController tabController = (PrelevementController) PrelevementController.backToMe(getMainWindow(), page);

      GatsbiController.addNewObjectForContext(SessionUtils.getCurrentGatsbiContexteForEntiteId(2),
         tabController.getListe().getSelfComponent(), e -> {
            try{
               super.onClick$addPrelevement();
            }catch(final Exception ex){
               Messagebox.show(handleExceptionMessage(ex), "Error", Messagebox.OK, Messagebox.ERROR);
            }
         }, null, this.maladie.getPatient());
   }
   
   public String getVisiteLibelle() {
      return maladie.getLibelle()
         .concat(" - ")
         .concat(ObjectTypesFormatters.dateRenderer2(maladie.getDateDebut()));
   }
   
   @Override
   protected void setIncaLabelStyle() {
   }
   
   @Override
   protected void setRegularLabelStyle(){
   }

   /*********** inner lists ******************/

}