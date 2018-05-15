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
package fr.aphp.tumorotek.action.prodderive.bto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.prodderive.FicheProdDeriveStatic;
import fr.aphp.tumorotek.action.validation.ResultatValidationModale;
import fr.aphp.tumorotek.action.validation.ResultatValidationModale.ResultatValidationModaleData;
import fr.aphp.tumorotek.decorator.CollaborateurDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.validation.workflow.ResultatValidation;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.delegate.AbstractProdDeriveDelegate;
import fr.aphp.tumorotek.model.coeur.prodderive.delegate.ProdDeriveBTO;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.model.validation.Action;
import fr.aphp.tumorotek.model.validation.ActionType;
import fr.aphp.tumorotek.model.validation.DetailValidation;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @author Gille Chapelot
 *
 */
public class FicheProdDeriveStaticBTO extends FicheProdDeriveStatic
{

   private static final long serialVersionUID = 1L;

   private Window fwinProdDeriveStaticBTO;
   private Group validationGroup;
   private Button validate;
   private Button invalidate;
   private Div validationStatus;
   private Label consultValidation;

   private boolean canValidate;
   private boolean canUnvalidate;

   /*
    * (non-Javadoc)
    * @see fr.aphp.tumorotek.action.prodderive.FicheProdDeriveStatic#setObject(fr.aphp.tumorotek.model.TKdataObject)
    */
   @Override
   public void setObject(final TKdataObject e){

      super.setObject(e);

      final AbstractProdDeriveDelegate delegate = getProdDerive().getDelegate();

      boolean validated = false;

      if(delegate != null){

         final DetailValidation detailValidation = ((ProdDeriveBTO) delegate).getDetailValidation();

         validated = detailValidation != null;

         if(validated){
            edit.setDisabled(true);
            getObjectTabController().getFicheAnnotation().disableEdit(true);
            validationStatus.setStyle("background-color: " + detailValidation.getNiveauValidation().getCouleur() + ";");
            consultValidation.addForward(null, fwinProdDeriveStaticBTO, "onConsultValidation");
            consultValidation.setClass("formLink");
         }

      }

      validationGroup.setVisible(validated);
      for(final Row row : validationGroup.getItems()){
         row.setVisible(validated);
      }

   }

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.action.prelevement.FichePrelevementStatic#disableToolBar(boolean)
    */
   @Override
   public void disableToolBar(final boolean b){

      super.disableToolBar(b);

      final AbstractProdDeriveDelegate delegate = getProdDerive().getDelegate();

      final boolean validated = delegate != null && ((ProdDeriveBTO) delegate).getDetailValidation() != null;

      final boolean validateVisible = !validated && isCanValidate();
      final boolean invalidateVisible = validated && isCanUnvalidate();

      validate.setVisible(validateVisible);
      invalidate.setVisible(invalidateVisible);

      if(validateVisible){

         final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(getProdDerive().entiteNom()).get(0);
         final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);

         validate.setDisabled(action == null);

      }

   }

   /**
    * Listener du bouton de dévalidation
    */
   public void onClick$invalidate(){

      final ProdDerive prodDerive = getProdDerive();

      //A ce niveau, le delegate ne peut être nul sinon, les boutons ne sont pas affichés
      //on part donc du principe que le delegate existe
      final ProdDeriveBTO prodDeriveBTO = (ProdDeriveBTO) prodDerive.getDelegate();

      //Mise  jour du produit dérivé (suppression des informations de validation)
      prodDeriveBTO.setDetailValidation(null);

      final OperationType operationType =
         ManagerLocator.getOperationTypeManager().findByNomLikeManager("InvalidationEntite", true).get(0);

      final List<OperationType> listOperationsType = new ArrayList<>();
      listOperationsType.add(operationType);

      ManagerLocator.getProdDeriveManager().updateObjectManager(prodDerive, prodDerive.getBanque(), prodDerive.getProdType(),
         prodDerive.getObjetStatut(), prodDerive.getCollaborateur(), prodDerive.getEmplacement(), prodDerive.getVolumeUnite(),
         prodDerive.getConcUnite(), prodDerive.getQuantiteUnite(), prodDerive.getModePrepaDerive(), prodDerive.getProdQualite(),
         prodDerive.getTransformation(), null, null, null, null, prodDerive.getReservation(),
         SessionUtils.getLoggedUser(sessionScope), false, listOperationsType, null);

      //Mise à jour de la liste de prélèvements
      getObjectTabController().getListe().updateObjectGridList(prodDerive);

      setObject(prodDerive);

   }

   /**
    * Listener du bouton de validation
    */

   public void onClick$validate(){

      final ProdDerive prodDerive = getProdDerive();

      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(prodDerive.entiteNom()).get(0);

      //Exécution de la validation
      final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);
      final ResultatValidation resultatValidation = ManagerLocator.getValidateurManager().validerAction(prodDerive, action);

      //Appel de la popup
      ResultatValidationModale.showValidationResultatPopup(prodDerive.entiteNom(), resultatValidation, false,
         new EventListener<Event>()
         {

            @Override
            public void onEvent(final Event event) throws Exception{

               final ResultatValidationModaleData dataModale = (ResultatValidationModaleData) event.getData();

               //Si la validation du prélèvement est confirmée dans la popup
               if(dataModale != null && dataModale.isValidated()){

                  final AbstractProdDeriveDelegate delegate = prodDerive.getDelegate();
                  ProdDeriveBTO prodDeriveBTO = null;
                  DetailValidation detailValidation = null;

                  if(delegate != null){
                     prodDeriveBTO = (ProdDeriveBTO) delegate;
                     detailValidation = prodDeriveBTO.getDetailValidation();

                     if(detailValidation == null){
                        detailValidation = new DetailValidation();
                        prodDeriveBTO.setDetailValidation(detailValidation);
                     }

                  }else{

                     prodDeriveBTO = new ProdDeriveBTO();
                     prodDeriveBTO.setDelegator(prodDerive);

                     detailValidation = new DetailValidation();
                     prodDeriveBTO.setDetailValidation(detailValidation);

                     prodDerive.setDelegate(prodDeriveBTO);

                  }

                  //Mise à jour du prélèvement
                  detailValidation.setDateValidation(Calendar.getInstance());
                  detailValidation.setNiveauValidation(resultatValidation.getNiveauValidation());
                  detailValidation.setCommentaireValidation(dataModale.getCommentaire());
                  detailValidation.setValideur(SessionUtils.getLoggedUser(sessionScope));

                  final OperationType operationType =
                     ManagerLocator.getOperationTypeManager().findByNomLikeManager("ValidationEntite", true).get(0);

                  final List<OperationType> listOperationsType = new ArrayList<>();
                  listOperationsType.add(operationType);

                  ManagerLocator.getProdDeriveManager().updateObjectManager(prodDerive, prodDerive.getBanque(),
                     prodDerive.getProdType(), prodDerive.getObjetStatut(), prodDerive.getCollaborateur(),
                     prodDerive.getEmplacement(), prodDerive.getVolumeUnite(), prodDerive.getConcUnite(),
                     prodDerive.getQuantiteUnite(), prodDerive.getModePrepaDerive(), prodDerive.getProdQualite(),
                     prodDerive.getTransformation(), null, null, null, null, prodDerive.getReservation(),
                     SessionUtils.getLoggedUser(sessionScope), false, listOperationsType, null);

                  //Mise à jour de la liste de prélèvements
                  getObjectTabController().getListe().updateObjectGridList(prodDerive);

                  setObject(prodDerive);

               }

            }
         });

   }

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.action.controller.AbstractFicheStaticController#drawActionsButtons(java.lang.String)
    */
   @Override
   public void drawActionsButtons(final String nomEntite){

      super.drawActionsButtons(nomEntite);

      Boolean admin = false;

      if(sessionScope.containsKey("AdminPF")){
         admin = (Boolean) sessionScope.get("AdminPF");
      }else if(sessionScope.containsKey("Admin")){
         admin = (Boolean) sessionScope.get("Admin");
      }

      setCanValidate(admin);
      setCanUnvalidate(admin);

   }

   /**
    * Consultation des résultats de validation
    * @param event
    */
   public void onConsultValidation(final Event event){

      final ProdDerive prodDerive = getProdDerive();

      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(prodDerive.entiteNom()).get(0);

      //Exécution de la validation
      final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);
      final ResultatValidation resultatValidation = ManagerLocator.getValidateurManager().validerAction(prodDerive, action);

      //Appel de la popup
      ResultatValidationModale.showReadOnlyValidationResultatPopup(prodDerive.entiteNom(), resultatValidation);

   }

   /**
    * Renvoie le valideur de l'échantillon formaté pour l'affichage si il existe
    * @return
    */
   public String getValideur(){

      String res = null;

      final AbstractProdDeriveDelegate delegate = getProdDerive().getDelegate();

      if(delegate != null){

         final DetailValidation detailValidation = ((ProdDeriveBTO) delegate).getDetailValidation();

         if(detailValidation != null && detailValidation.getValideur() != null){

            final Utilisateur valideur = detailValidation.getValideur();

            if(valideur != null){
               res = valideur.getLogin();

               if(valideur.getCollaborateur() != null){
                  final Collaborateur coll = valideur.getCollaborateur();
                  res = new CollaborateurDecorator(coll).getNomPrenom();
               }
            }
         }
      }

      return res;

   }

   /**
    * Retourne la date de validation de l'échantillon formatée pour l'affichage si elle existe
    * @return
    */
   public String getDateValidationFormatee(){

      String dateFormatee = null;
      final AbstractProdDeriveDelegate delegate = getProdDerive().getDelegate();

      if(delegate != null){

         final ProdDeriveBTO prodDeriveBTO = (ProdDeriveBTO) delegate;

         if(prodDeriveBTO.getDetailValidation() != null){
            dateFormatee = ObjectTypesFormatters.dateRenderer2(prodDeriveBTO.getDetailValidation().getDateValidation());
         }

      }

      return dateFormatee;
   }

   /**
    * @return the canValidate
    */
   public boolean isCanValidate(){
      return canValidate;
   }

   /**
    * @param canValidate the canValidate to set
    */
   public void setCanValidate(final boolean canValidate){
      this.canValidate = canValidate;
   }

   /**
    * @return the canUnvalidate
    */
   public boolean isCanUnvalidate(){
      return canUnvalidate;
   }

   /**
    * @param canUnvalidate the canUnvalidate to set
    */
   public void setCanUnvalidate(final boolean canUnvalidate){
      this.canUnvalidate = canUnvalidate;
   }

}
