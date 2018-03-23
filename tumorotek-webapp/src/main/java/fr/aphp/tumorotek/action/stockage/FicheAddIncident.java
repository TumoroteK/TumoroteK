package fr.aphp.tumorotek.action.stockage;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;

public class FicheAddIncident extends AbstractFicheCombineController
{

   private static final long serialVersionUID = -3694035708012726832L;

   private Conteneur conteneur;
   private Enceinte enceinte;
   private Terminale terminale;
   private Incident incident;
   private Component parent;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      addNewC = new Button();
      editC = new Button();
      deleteC = new Button();

      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.retour");

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {});

      setObjBoxsComponents(new Component[] {});

      setRequiredMarks(new Component[] {

      });

      winPanel.setHeight("220px");

      getBinder().loadAll();
   }

   public void init(final Conteneur cont, final Component par, final Enceinte enc, final Terminale term){
      this.conteneur = cont;
      this.enceinte = enc;
      this.terminale = term;
      this.parent = par;
      this.incident = new Incident();
      this.incident.setConteneur(conteneur);
      this.incident.setEnceinte(enceinte);
      this.incident.setTerminale(terminale);

      getBinder().loadComponent(self);
   }

   @Override
   public void onClick$cancelC(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   @Override
   public void onOK(){
      onClick$createC();
   }

   @Override
   public void onClick$createC(){
      try{
         // on récupère tous es objets stockés dans le conteneur
         final List<TKStockableObject> objs = new ArrayList<>();
         List<Terminale> terminales = null;
         if(conteneur != null){
            terminales = ManagerLocator.getConteneurManager().getAllTerminalesInArborescenceManager(conteneur);
         }else if(enceinte != null){
            terminales = ManagerLocator.getEnceinteManager().getAllTerminalesInArborescenceManager(enceinte);
         }else if(terminale != null){
            terminales = new ArrayList<>();
            terminales.add(terminale);
         }
         for(int i = 0; i < terminales.size(); i++){
            objs.addAll(ManagerLocator.getTerminaleManager().getEchantillonsManager(terminales.get(i)));
            objs.addAll(ManagerLocator.getTerminaleManager().getProdDerivesManager(terminales.get(i)));
         }

         // s'il y a des objets dans le conteneur
         if(!getObjStatutIncompatibleForRetour(objs, "INCIDENT")){
            // sauvegarde
            ManagerLocator.getIncidentManager().createObjectManager(incident, conteneur, enceinte, terminale);
            // envoie de l'utilisateur modifié
            Events.postEvent("onGetAddedIncident", getParent(), incident);

            // on demande à l'utilisateur s'il souhaite
            // créer des retours
            if(objs.size() > 0 && askForRetoursCreation(objs.size(), null, null, null, incident,
               ManagerLocator.getOperationTypeManager().findByNomLikeManager("Creation", true).get(0))){
               // recherche des emplacements de ces objets
               //					Hashtable<TKStockableObject, Emplacement> emps = 
               //						new Hashtable<TKStockableObject, Emplacement>();
               //					for (int i = 0; i < objs.size(); i++) {
               //						emps.put(objs.get(i), 
               //								ObjectTypesFormatters.getEmplacementAdrl(
               //										objs.get(i)));
               //					}

               // ouverture de la modale
               openRetourFormModale(null, true, null, null, objs, null, null, null, incident, incident.getDate(),
                  incident.getEnceinte() != null ? Labels.getLabel("ficheRetour.observations.incident.enceinte")
                     : incident.getTerminale() != null ? Labels.getLabel("ficheRetour.observations.incident.terminale")
                        : Labels.getLabel("ficheRetour.observations.incident.conteneur"),
                  null);
            }
         }

         // fermeture de la fenêtre
         Events.postEvent(new Event("onClose", self.getRoot()));
      }catch(final RuntimeException re){
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void cloneObject(){}

   @Override
   public void createNewObject(){}

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
   public void removeObject(final String comments){

   }

   @Override
   public void setEmptyToNulls(){
      if(this.incident.getDescription().equals("")){
         this.incident.setDescription(null);
      }
   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setFocusOnElement(){}

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public void switchToStaticMode(){}

   @Override
   public void updateObject(){}

   @Override
   public TKdataObject getObject(){
      return null;
   }

   public Constraint getIncidentConstraint(){
      return StockageConstraints.getIncidentConstraint();
   }

   public Constraint getDescConstraint(){
      return StockageConstraints.getDescConstraint();
   }

   public Conteneur getConteneur(){
      return conteneur;
   }

   public void setConteneur(final Conteneur c){
      this.conteneur = c;
   }

   public Incident getIncident(){
      return incident;
   }

   public void setIncident(final Incident i){
      this.incident = i;
   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component p){
      this.parent = p;
   }

   public String getContenant(){
      if(terminale != null){
         return terminale.getNom();
      }else if(enceinte != null){
         return enceinte.getNom();
      }else if(conteneur != null){
         return conteneur.getNom();
      }
      return "";
   }

}
