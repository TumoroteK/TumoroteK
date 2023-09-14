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
package fr.aphp.tumorotek.action.historique;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.param.TumorotekProperties;

public class FicheHistoriqueModale extends AbstractFicheCombineController
{

   private static final long serialVersionUID = 6483086378228201927L;

   private TKdataObject historiqueObject;

   private String title;

   private List<Operation> operations = new ArrayList<>();

   private OperationRenderer operationRenderer = new OperationRenderer();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      // reference vers des boutons non affichés
      editC = new Button();
      validateC = new Button();
      createC = new Button();
      revertC = new Button();
      deleteC = new Button();
      cancelC = new Button();
      addNewC = new Button();

      super.doAfterCompose(comp);

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 10 + "px");
      }

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {});

      setObjBoxsComponents(new Component[] {});

      setRequiredMarks(new Component[] {});

      getBinder().loadAll();
   }

   @Override
   public TKdataObject getObject(){
      return this.historiqueObject;
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.historiqueObject = obj;
      ResourceBundle res = null;
      if(ManagerLocator.getResourceBundleTumo().doesResourceBundleExists(TumorotekProperties.TUMO_PROPERTIES_FILENAME)){
         res = ManagerLocator.getResourceBundleTumo().getResourceBundle(TumorotekProperties.TUMO_PROPERTIES_FILENAME);
      }
      // on récupère la propriété définissant si on doit sauver
      // et afficher les connexions
      String save = null;
      if(null != res && res.containsKey(TkParam.SAUVER_CONNEXION.getKey())){
         save = res.getString(TkParam.SAUVER_CONNEXION.getKey());
      }
      if(save != null && save.equals("true")){
         operations = ManagerLocator.getOperationManager().findByObjectManager(historiqueObject);
      }else{
         operations = ManagerLocator.getOperationManager().findByObjectForHistoriqueManager(historiqueObject);
      }

      if(this.historiqueObject.getClass().getSimpleName().equals("Patient")){
         title = ObjectTypesFormatters.getLabel("historique.title.patient", new String[] {((Patient) historiqueObject).getNom()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Maladie")){
         title =
            ObjectTypesFormatters.getLabel("historique.title.maladie", new String[] {((Maladie) historiqueObject).getLibelle()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Prelevement")){
         title = ObjectTypesFormatters.getLabel("historique.title.prelevement",
            new String[] {((Prelevement) historiqueObject).getCode()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Echantillon")){
         title = ObjectTypesFormatters.getLabel("historique.title.echantillon",
            new String[] {((Echantillon) historiqueObject).getCode()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("ProdDerive")){
         title = ObjectTypesFormatters.getLabel("historique.title.prodDerive",
            new String[] {((ProdDerive) historiqueObject).getCode()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Cession")){
         title = ObjectTypesFormatters.getLabel("historique.title.cession",
            new String[] {((Cession) historiqueObject).getNumero().toString()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Conteneur")){
         title =
            ObjectTypesFormatters.getLabel("historique.title.conteneur", new String[] {((Conteneur) historiqueObject).getNom()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Enceinte")){
         title =
            ObjectTypesFormatters.getLabel("historique.title.enceinte", new String[] {((Enceinte) historiqueObject).getNom()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Terminale")){
         title =
            ObjectTypesFormatters.getLabel("historique.title.terminale", new String[] {((Terminale) historiqueObject).getNom()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Collaborateur")){
         title = ObjectTypesFormatters.getLabel("historique.title.collaborateur",
            new String[] {((Collaborateur) historiqueObject).getNom()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Etablissement")){
         title = ObjectTypesFormatters.getLabel("historique.title.etablissement",
            new String[] {((Etablissement) historiqueObject).getNom()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Service")){
         title = ObjectTypesFormatters.getLabel("historique.title.service", new String[] {((Service) historiqueObject).getNom()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Transporteur")){
         title = ObjectTypesFormatters.getLabel("historique.title.transporteur",
            new String[] {((Transporteur) historiqueObject).getNom()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Contrat")){
         title =
            ObjectTypesFormatters.getLabel("historique.title.contrat", new String[] {((Contrat) historiqueObject).getNumero()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Profil")){
         title = ObjectTypesFormatters.getLabel("historique.title.profil", new String[] {((Profil) historiqueObject).getNom()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Utilisateur")){
         title = ObjectTypesFormatters.getLabel("historique.title.utilisateur",
            new String[] {((Utilisateur) historiqueObject).getLogin()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("Banque")){
         title = ObjectTypesFormatters.getLabel("historique.title.banque", new String[] {((Banque) historiqueObject).getNom()});
      }else if(this.historiqueObject.getClass().getSimpleName().equals("TableAnnotation")){
         title = ObjectTypesFormatters.getLabel("historique.title.tableAnnotation",
            new String[] {((TableAnnotation) historiqueObject).getNom()});
      }

      super.setObject(obj);
   }

   @Override
   public void cloneObject(){}

   @Override
   public void createNewObject(){}

   @Override
   public void onClick$addNewC(){}

   @Override
   public void onClick$editC(){}

   @Override
   public void setEmptyToNulls(){}

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setFocusOnElement(){}

   @Override
   public void switchToStaticMode(){}

   @Override
   public void updateObject(){}

   /*******************************************************/
   /**                  GETTERS - SETTERS                 */
   /*******************************************************/

   public List<Operation> getOperations(){
      return operations;
   }

   public void setOperations(final List<Operation> op){
      this.operations = op;
   }

   public OperationRenderer getOperationRenderer(){
      return operationRenderer;
   }

   public void setOperationRenderer(final OperationRenderer oRenderer){
      this.operationRenderer = oRenderer;
   }

   public String getTitle(){
      return title;
   }

   public void setTitle(final String t){
      this.title = t;
   }

   @Override
   public String getDeleteWaitLabel(){
      return null;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public boolean prepareDeleteObject(){
      return false;
   }

   @Override
   public void removeObject(final String comments){}

   @Override
   public void setParentObject(final TKdataObject obj){}

}
