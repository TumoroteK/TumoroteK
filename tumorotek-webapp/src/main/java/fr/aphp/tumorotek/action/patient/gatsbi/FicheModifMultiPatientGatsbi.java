package fr.aphp.tumorotek.action.patient.gatsbi;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;

import fr.aphp.tumorotek.action.patient.FicheModifMultiPatient;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;

public class FicheModifMultiPatientGatsbi extends FicheModifMultiPatient
{
   private static final long serialVersionUID = 1L;

   private Contexte c;
   
   private Div dateEtatDiv;

   private Div dateDecesDiv;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      c = GatsbiController.initWireAndDisplay(this, 1, false, null, null, null);
   }

   @Override
   protected List<Object> applyAnyThesaurusRestriction(final List<Object> thObjs, final Integer chpId) throws TKException{
      return GatsbiController.filterExistingListModel(c, thObjs, chpId);
   }

   @Override
   protected Constraint muteAnyRequiredConstraint(final Constraint cstr, final Integer chpId){
      return GatsbiController.muteConstraintFromContexte(cstr, c.isChampIdRequired(chpId));
   }

   @Override
   protected boolean switchAnyRequiredFlag(final Boolean flag, final Integer chpId){
      return c.isChampIdRequired(chpId);
   }
   
   //Pour l'écran Gatsbi, dateDeces et dateEtat ne sont pas des Row mais des Div
   //=> on encapsule le type du composant dans ces méthodes qui seront surchargées dans Gatsbi
   @Override
   protected void setVisibilityOnDateDeces(boolean visible) {
      setVisibilityOnDiv(dateDecesDiv, visible);
   }
   
   @Override
   protected void setVisibilityOnDateEtat(boolean visible) {
      setVisibilityOnDiv(dateEtatDiv, visible);
   }

   private void setVisibilityOnDiv(Div div, boolean visible) {
      div.setVisible(visible);
   }
   //
}
