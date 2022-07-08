package fr.aphp.tumorotek.action.cession.retour;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.Scope;
import org.zkoss.bind.annotation.ScopeParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class DateRetourModale
{

   //private static final long serialVersionUID = -3694035708012726832L;

   private Calendar dateRetour;

   private List<TKStockableObject> objects;

   private List<Retour> retoursToUpdate = new ArrayList<>();

   private List<SimpleCodeDateSortie> simpleCDs = new ArrayList<>();

   private Utilisateur user;

   private AbstractObjectTabController controller;

   @Wire("#fwinDateRetourModale")
   private Window fwinDateRetourModale;

   public DateRetourModale(){}

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
   }

   @Init
   public void init(@ExecutionArgParam("objs") final List<TKStockableObject> objs,
      @ExecutionArgParam("controller") final AbstractObjectTabController ctr,
      @ScopeParam(scopes = Scope.SESSION, value = "User") final Utilisateur u){

      setObjects(objs);
      setUser(u);
      setController(ctr);

   }

   /**
    * Modifie tous les retours avec la date de Retour.
    * Mets à jour la liste de TKStockableObjects
    */
   @Command
   public void validate(@BindingParam("dateRetour") final java.util.Calendar dateRetour){
      try{
         for(final Retour ret : getRetoursToUpdate()){
            ret.setDateRetour(dateRetour);
         }
         ManagerLocator.getRetourManager().updateMultipleObjectManager(getRetoursToUpdate(), null, null, null, null, user);

         // mise à jour liste
         if(controller != null && controller.getListe() != null){
            if(controller instanceof EchantillonController
               && controller.getMainWindow().isFullfilledComponent("echantillonPanel", "winEchantillon")){
               final List<TKdataObject> echans = new ArrayList<>();
               for(int i = 0; i < getObjects().size(); i++){
                  echans.add(ManagerLocator.getEchantillonManager().findByIdManager(getObjects().get(i).listableObjectId()));
               }
               controller.getListe().updateMultiObjectsGridListFromOtherPage(echans);
            }else if(controller instanceof ProdDeriveController
               && controller.getMainWindow().isFullfilledComponent("derivePanel", "winProdDerive")){
               final List<TKdataObject> derives = new ArrayList<>();
               for(int i = 0; i < getObjects().size(); i++){
                  derives.add(ManagerLocator.getProdDeriveManager().findByIdManager(getObjects().get(i).listableObjectId()));
               }
               controller.getListe().updateMultiObjectsGridListFromOtherPage(derives);
            }
         }
      }catch(final Exception e){
         Messagebox.show(AbstractController.handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
      }
      // ferme la fenetre
      cancel();
   }

   @Command
   public void cancel(){
      Events.postEvent("onClose", fwinDateRetourModale, null);
   }

   /**********************************************************/
   /****************** GETTERS - SETTERS *********************/
   /**********************************************************/
   public List<TKStockableObject> getObjects(){
      return objects;
   }

   public void setObjects(final List<TKStockableObject> obs){
      this.objects = obs;
      //		if (this.objects != null) {
      //			Iterator<? extends TKStockableObject> it = objects.iterator();
      //			Retour incomp;
      //			TKStockableObject o;
      //			while (it.hasNext()) {
      //				o = it.next();
      //				incomp = ManagerLocator.getRetourManager()
      //					.findByObjectDateRetourEmptyManager(o.listableObjectId(),
      //							ManagerLocator.getEntiteManager()
      //									.findByNomManager(o.entiteNom()).get(0));
      //				retoursToUpdate.add(incomp);
      //				simpleCDs.add(new SimpleCodeDateSortie(o.getCode(), incomp.getDateSortie()));
      //			}
      //		}
   }

   public List<Retour> getRetoursToUpdate(){
      return retoursToUpdate;
   }

   public void setRetoursToUpdate(final List<Retour> rs){
      this.retoursToUpdate = rs;
   }

   public Calendar getDateRetour(){
      return dateRetour;
   }

   public void setDateRetour(final Calendar d){
      this.dateRetour = d;
   }

   public List<SimpleCodeDateSortie> getSimpleCDs(){
      return simpleCDs;
   }

   public void setSimpleCDs(final List<SimpleCodeDateSortie> ss){
      this.simpleCDs = ss;
   }

   public Utilisateur getUser(){
      return user;
   }

   public void setUser(final Utilisateur user){
      this.user = user;
   }

   public void setController(final AbstractObjectTabController c){
      this.controller = c;
   }

   public AbstractObjectTabController getController(){
      return controller;
   }

   public class SimpleCodeDateSortie
   {

      private String code;

      private Calendar dateSortie;

      public SimpleCodeDateSortie(final String c, final Calendar dateS){
         code = c;
         dateSortie = dateS;
      }

      public String getCode(){
         return code;
      }

      public void setCode(final String code){
         this.code = code;
      }

      public String getDateSortie(){
         return ObjectTypesFormatters.dateRenderer2(dateSortie);
      }

      public void setDateSortie(final Calendar dS){
         this.dateSortie = dS;
      }

   }
}
