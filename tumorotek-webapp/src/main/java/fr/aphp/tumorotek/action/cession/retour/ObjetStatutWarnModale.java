package fr.aphp.tumorotek.action.cession.retour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;

public class ObjetStatutWarnModale
{

   @Wire("#fwinObjetStatutWarnModale")
   private Window fwinObjetStatutWarnModale;

   private MainWindow main;

   private List<TKStockableObject> objs = new ArrayList<>();

   private List<Echantillon> echans = new ArrayList<>();

   private List<Echantillon> echansOtherBanks = new ArrayList<>();

   private List<ProdDerive> derives = new ArrayList<>();

   private List<Echantillon> derivesOtherBanks = new ArrayList<>();

   private Banque currBanque;

   private Boolean singleObj;

   private String operation;

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
   }

   @Init
   public void init(@ExecutionArgParam("objs") final List<TKStockableObject> objs, @ExecutionArgParam("banque") final Banque bank,
      @ExecutionArgParam("main") final MainWindow main, @ExecutionArgParam("operation") final String op){

      setMain(main);
      setSingleObj(op != null && op.equals("TRANSFORMATION"));
      setCurrBanque(bank);
      setObjs(objs);
   }

   public String getGeneralWarning(){
      if(getSingleObj()){
         final String out =
            ObjectTypesFormatters.getLabel("message.retour.single.warning", new String[] {getObjs().get(0).getCode()});

         return out;
      }else{
         return Labels.getLabel("message.retours.warning");
      }
   }

   private void setObjs(final List<TKStockableObject> o){

      objs = o;

      final Iterator<TKStockableObject> it = objs.iterator();
      TKStockableObject obj;
      while(it.hasNext()){
         obj = it.next();
         if(obj.entiteNom().equals("Echantillon")){
            if(obj.getBanque().equals(getCurrBanque())){
               getEchans().add((Echantillon) obj);
            }else{
               getEchansOtherBanks().add((Echantillon) obj);
            }
         }else{
            if(obj.getBanque().equals(getCurrBanque())){
               getDerives().add((ProdDerive) obj);
            }else{
               getDerives().add((ProdDerive) obj);
            }
         }
      }
   }

   public List<TKStockableObject> getObjs(){
      return objs;
   }

   public String getHasEchans(){
      return (getSingleObj() || getEchans().isEmpty()) ? "false" : "true";
   }

   public String getEchantillonsWarn(){
      return String.valueOf(getEchans().size()) + " " + Labels.getLabel("cession.echantillons") + " " + getCurrBanque().getNom();
   }

   @Command
   public void selectEchans(){
      Clients.showBusy(Labels.getLabel("general.display.wait"));

      // on met a jour la liste des échantillons avec ceux
      // contenus dans la terminale
      final EchantillonController tabController =
         (EchantillonController) EchantillonController.backToMe(getMain(), fwinObjetStatutWarnModale.getPage());
      tabController.getListe().updateListContent(getEchans());
      tabController.switchToOnlyListeMode();

      Events.postEvent("onClose", fwinObjetStatutWarnModale, null);

      Clients.clearBusy();

   }

   public String getHasEchansOtherBanks(){
      return (getSingleObj() || getEchansOtherBanks().isEmpty()) ? "false" : "true";
   }

   public String getEchantillonsOtherBanksWarn(){
      return String.valueOf(getEchansOtherBanks().size()) + " " + Labels.getLabel("cession.echantillons") + " "
         + Labels.getLabel("message.retour.warning.otherbanks");
   }

   public String getHasDerives(){
      return (getSingleObj() || getDerives().isEmpty()) ? "false" : "true";
   }

   public String getDerivesWarn(){
      return String.valueOf(getDerives().size()) + " " + Labels.getLabel("cession.prodDerive") + " " + getCurrBanque().getNom();
   }

   @Command
   public void selectDerives(){
      Clients.showBusy(Labels.getLabel("general.display.wait"));

      // on met a jour la liste des échantillons avec ceux
      // contenus dans la terminale
      final ProdDeriveController tabController =
         (ProdDeriveController) ProdDeriveController.backToMe(getMain(), fwinObjetStatutWarnModale.getPage());
      tabController.getListe().updateListContent(getDerives());
      tabController.switchToOnlyListeMode();

      Events.postEvent("onClose", fwinObjetStatutWarnModale, null);

      Clients.clearBusy();
   }

   public String getHasDerivesOtherBanks(){
      return (getSingleObj() || getDerivesOtherBanks().isEmpty()) ? "false" : "true";
   }

   public String getDerivesOtherBanksWarn(){
      return String.valueOf(getDerivesOtherBanks().size()) + " " + Labels.getLabel("cession.prodDerive") + " "
         + Labels.getLabel("message.retour.warning.otherbanks");
   }

   public List<Echantillon> getEchans(){
      return echans;
   }

   public void setEchans(final List<Echantillon> e){
      this.echans = e;
   }

   public List<ProdDerive> getDerives(){
      return derives;
   }

   public void setDerives(final List<ProdDerive> d){
      this.derives = d;
   }

   public List<Echantillon> getEchansOtherBanks(){
      return echansOtherBanks;
   }

   public void setEchansOtherBanks(final List<Echantillon> e){
      this.echansOtherBanks = e;
   }

   public List<Echantillon> getDerivesOtherBanks(){
      return derivesOtherBanks;
   }

   public void setDerivesOtherBanks(final List<Echantillon> d){
      this.derivesOtherBanks = d;
   }

   public Banque getCurrBanque(){
      return currBanque;
   }

   public void setCurrBanque(final Banque cB){
      this.currBanque = cB;
   }

   public MainWindow getMain(){
      return main;
   }

   public void setMain(final MainWindow m){
      this.main = m;
   }

   public Boolean getSingleObj(){
      return singleObj;
   }

   public void setSingleObj(final Boolean s){
      this.singleObj = s;
   }

   public String getOperation(){
      return operation;
   }

   public void setOperation(final String operation){
      this.operation = operation;
   }
}
