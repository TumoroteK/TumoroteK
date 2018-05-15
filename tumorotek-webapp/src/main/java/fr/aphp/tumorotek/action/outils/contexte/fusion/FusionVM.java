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
package fr.aphp.tumorotek.action.outils.contexte.fusion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.contexte.DuoEntites;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;

public class FusionVM
{

   @Wire
   private Button launchSearchButton;

   @Wire
   private Window winContexteFusion;

   private String rechValue;

   PatientManager patientManager;

   private final ListModelList<DuoEntites> duoModel = new ListModelList<>();

   private DuoEntites duoSelectedEntites;

   private int idASelected;
   private int idBSelected;

   private String entiteRecherche = "";
   private String critereRecherche = "";

   //taux de base modifié par l'interface
   private int tauxSimilitude = 90;

   public FusionVM(){}

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
      Selectors.wireEventListeners(view, this);

      if(winContexteFusion != null){
         winContexteFusion.setHeight(
            ((MainWindow) view.getPage().getFellow("mainWin").getAttributeOrFellow("mainWin$composer", true)).getPanelHeight() - 5
               + "px");
      }

   }

   //lors passage ZK7, methode directe dans zul min/max
   @Command
   public void setMin(){
      if(tauxSimilitude < 60){
         tauxSimilitude = 60;
      }
   }

   @Command
   public void onDeleted(@BindingParam("toRemove") final DuoEntites duo){
      duoModel.remove(duo);
   }

   @Command
   public void launchResearch(){
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterLaunch", launchSearchButton, null);
   }

   @Listen("onLaterLaunch=#launchSearchButton")
   public void onLaterLaunch(){
      rechercheDoublon();
      Clients.clearBusy();
   }

   public void rechercheDoublon(){
      duoModel.clear();
      final List<TKFantomableObject> lsA = new ArrayList<>();
      final List<TKFantomableObject> lsB = new ArrayList<>();

      if(rechValue != null){
         rechValue = "%" + rechValue + "%";
      }

      switch(entiteRecherche){
         case "Collaborateur":
            lsB.addAll(ManagerLocator.getCollaborateurManager().findAllObjectsManager());
            if(critereRecherche != null && critereRecherche.equals("Ville")){
               lsA.addAll(ManagerLocator.getCollaborateurManager().findByVilleLikeManager(rechValue));
            }else{
               lsA.addAll(ManagerLocator.getCollaborateurManager().findByNomLikeManager(rechValue, false));
            }
            break;
         case "Service":
            lsB.addAll(ManagerLocator.getServiceManager().findAllObjectsManager());
            if(critereRecherche != null && critereRecherche.equals("Ville")){
               lsA.addAll(ManagerLocator.getServiceManager().findByVilleLikeManager(rechValue));
            }else{
               lsA.addAll(ManagerLocator.getServiceManager().findByNomLikeManager(rechValue, false));
            }
            break;
         case "Etablissement":
            lsB.addAll(ManagerLocator.getEtablissementManager().findAllObjectsManager());
            if(critereRecherche != null && critereRecherche.equals("Ville")){
               lsA.addAll(ManagerLocator.getEtablissementManager().findByVilleLikeManager(rechValue));
            }else{
               lsA.addAll(ManagerLocator.getEtablissementManager().findByNomLikeManager(rechValue, false));
            }
            break;
         default:
            break;
      }

      // si recherche sans criteres
      if(lsA.isEmpty()){
         lsA.addAll(lsB);
      }

      // parcoure les listes pour les comparaisons
      for(final TKFantomableObject a : lsA){
         for(final TKFantomableObject b : lsB){
            addDuoIfDegreSimil(a, b);
         }
      }
   }

   private void addDuoIfDegreSimil(final TKFantomableObject a, final TKFantomableObject b){

      double degreSimil;
      final float tauxSimilitudeF = (float) tauxSimilitude / 100;
      if(a != null && b != null && a != b){
         switch(entiteRecherche){
            case "Collaborateur":
               final Collaborateur colla = (Collaborateur) a;
               final Collaborateur collb = (Collaborateur) b;

               if(!colla.getCollaborateurId().equals(collb.getCollaborateurId())){

                  degreSimil = (LevenshteinDistance.similarityTwoParams(colla.getNom(), collb.getNom(), colla.getPrenom(),
                     collb.getPrenom()));

                  if(degreSimil >= tauxSimilitudeF){
                     final DuoEntites duo = new DuoEntites(colla.getCollaborateurId(), collb.getCollaborateurId(),
                        colla.getNom() + " " + ((colla.getPrenom() != null) ? colla.getPrenom() : ""),
                        collb.getNom() + " " + ((collb.getPrenom() != null) ? collb.getPrenom() : ""),
                        colla.getEtablissement() != null ? colla.getEtablissement().getNom() : null,
                        collb.getEtablissement() != null ? collb.getEtablissement().getNom() : null, degreSimil);
                     if(!duoModel.contains(duo)){
                        duoModel.add(duo);
                     }
                  }
               }
               break;
            case "Service":
               final Service serva = (Service) a;
               final Service servb = (Service) b;

               if(!serva.getServiceId().equals(servb.getServiceId())){
                  degreSimil = LevenshteinDistance.similarityTwoParams(serva.getNom(), servb.getNom(),
                     serva.getEtablissement().getNom(), servb.getEtablissement().getNom());
                  if(degreSimil >= tauxSimilitudeF){
                     final DuoEntites duo = new DuoEntites(serva.getServiceId(), servb.getServiceId(), serva.getNom(),
                        servb.getNom(), serva.getEtablissement().getNom(), servb.getEtablissement().getNom(), degreSimil);
                     if(!duoModel.contains(duo)){
                        duoModel.add(duo);
                     }
                  }
               }
               break;
            case "Etablissement":
               final Etablissement etala = (Etablissement) a;
               final Etablissement etalb = (Etablissement) b;

               if(!etala.getEtablissementId().equals(etalb.getEtablissementId())){
                  degreSimil = LevenshteinDistance.similarityTwoParams(etala.getNom(), etalb.getNom(),
                     etala.getCoordonnee() != null ? etala.getCoordonnee().getVille() : null,
                     etalb.getCoordonnee() != null ? etalb.getCoordonnee().getVille() : null);
                  if(degreSimil >= tauxSimilitudeF){
                     final DuoEntites duo = new DuoEntites(etala.getEtablissementId(), etalb.getEtablissementId(), etala.getNom(),
                        etalb.getNom(), degreSimil);
                     if(!duoModel.contains(duo)){
                        duoModel.add(duo);
                     }
                  }
               }
               break;
            default:
               break;
         }
      }

   }

   @Command
   public void onClicked(){

      if(!winContexteFusion.hasFellow("affichageFusion")){
         idASelected = duoSelectedEntites.getIdEntiteA();
         idBSelected = duoSelectedEntites.getIdEntiteB();

         final HashMap<String, Object> map = new HashMap<>();
         map.put("entiteRecherche", entiteRecherche);
         map.put("idASelected", idASelected);
         map.put("idBSelected", idBSelected);
         map.put("duoModel", duoModel);
         map.put("duoEntite", duoSelectedEntites);

         Executions.createComponents("/zuls/outils/contexte/fusion/FormulaireFusion.zul", winContexteFusion, map);

      }
   }

   public ListModelList<DuoEntites> getDuoModel(){
      return duoModel;
   }

   public String getEntiteRecherche(){
      return entiteRecherche;
   }

   public void setEntiteRecherche(final String entiteRecherche){
      this.entiteRecherche = entiteRecherche;
   }

   public int getIdASelected(){
      return idASelected;
   }

   public int getIdBSelected(){
      return idBSelected;
   }

   public void setIdASelected(final int idASelected){
      this.idASelected = idASelected;
   }

   public void setIdBSelected(final int idBSelected){
      this.idBSelected = idBSelected;
   }

   public DuoEntites getDuoSelectedEntites(){
      return duoSelectedEntites;
   }

   public void setDuoSelectedEntites(final DuoEntites duoSelectedEntites){
      this.duoSelectedEntites = duoSelectedEntites;
   }

   public String getCritereRecherche(){
      return critereRecherche;
   }

   public void setCritereRecherche(final String critereRecherche){
      this.critereRecherche = critereRecherche;
   }

   public int getTauxSimilitude(){
      return tauxSimilitude;
   }

   public void setTauxSimilitude(final int tauxSimilitude){
      this.tauxSimilitude = tauxSimilitude;
   }

   public String getRechValue(){
      return rechValue;
   }

   public void setRechValue(final String r){
      this.rechValue = r;
   }

}
