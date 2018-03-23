package fr.aphp.tumorotek.action.stockage;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Intbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ChangeTailleEnceinteModale extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = 7050591120146094296L;

   private Component parent;
   private Intbox nbPlacesBox;
   private Enceinte enceinte;
   private Integer nbPlaces;
   private Integer nbPlacesOcc;
   private AnnotateDataBinder binder;

   private String nbPlacesLabel;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      binder = new AnnotateDataBinder(comp);
      binder.loadComponent(comp);
   }

   public void init(final Component comp, final Enceinte obj){
      this.enceinte = obj;
      this.parent = comp;

      nbPlaces = this.enceinte.getNbPlaces();
      nbPlacesOcc = ManagerLocator.getEnceinteManager().getEnceintesManager(enceinte).size()
         + ManagerLocator.getEnceinteManager().getTerminalesManager(enceinte).size();

      nbPlacesLabel = ObjectTypesFormatters.getLabel("enceinte.taille.edit.status",
         new String[] {String.valueOf(nbPlacesOcc), String.valueOf(nbPlaces)});

      binder.loadAll();
   }

   public void onClick$cancel(){
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$validate(){

      if(nbPlaces < nbPlacesOcc){
         throw new WrongValueException(nbPlacesBox, ObjectTypesFormatters.getLabel("enceinte.taille.exception",
            new String[] {enceinte.getNom(), String.valueOf(nbPlacesOcc)}));
      }else{
         if(nbPlaces != enceinte.getNbPlaces()){
            Clients.showBusy(null);
            Events.echoEvent("onLaterUpdate", self, null);
         }else{
            Events.postEvent(new Event("onClose", self.getRoot()));
         }
      }

   }

   public void onLaterUpdate(){
      enceinte = ManagerLocator.getEnceinteManager().updateTailleEnceinteManager(enceinte, nbPlaces - enceinte.getNbPlaces(),
         SessionUtils.getLoggedUser(sessionScope));

      // maj de l'arbre
      Events.postEvent("onGetUpdateTaille", getParent(), enceinte);

      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));

      // ferme wait message
      Clients.clearBusy();
   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component p){
      this.parent = p;
   }

   public Enceinte getEnceinte(){
      return enceinte;
   }

   public void setEnceinte(final Enceinte e){
      this.enceinte = e;
   }

   public Integer getNbPlaces(){
      return nbPlaces;
   }

   public void setNbPlaces(final Integer n){
      this.nbPlaces = n;
   }

   public String getNbPlacesLabel(){
      return nbPlacesLabel;
   }
}
