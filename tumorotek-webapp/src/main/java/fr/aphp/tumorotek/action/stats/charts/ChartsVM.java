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
package fr.aphp.tumorotek.action.stats.charts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.Scope;
import org.zkoss.bind.annotation.ScopeParam;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Tree;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stats.GraphesModele;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * Backing bean Tableau indicateurs charts.
 *
 * @author Marc DESCHAMPS
 * @version 2.0.13
 */
public class ChartsVM
{

   private List<Banque> banques = new ArrayList<>();

   private Banque selectedBanque;

   private List<Plateforme> plateformes = new ArrayList<>();

   private Plateforme selectedPlateforme;

   private ChartData datas;

   private String patientBlocTitle;

   private String prelevementBlocTitle;

   private String echantillonBlocTitle;

   private String deriveBlocTitle;

   private String cessionBlocTitle;

   private Utilisateur loggeduser;

   private Boolean menuVisible = true;

   private Boolean chartsVisible = false;
   // private Boolean collVisible = false;

   private Boolean patientPfVisible = false;

   private Boolean patientCollVisible = false;

   private Boolean prelPfVisible = false;

   private Boolean prelCollVisible = false;

   private Boolean prelTypeCollVisible = false;

   private Boolean prelEtabCollVisible = false;

   private Boolean prelConsentCollVisible = false;

   // private Boolean subPrelCollVisible = false ;

   private Boolean echanPfVisible = false;

   private Boolean echanCollVisible = false;

   private Boolean echanTypeCollVisible = false;

   private Boolean echanCimCollVisible = false;

   private Boolean echanOrgCollVisible = false;

   // boolean subEchanCollVisible = false; //

   private Boolean derivePfVisible = false;

   private Boolean deriveCollVisible = false;

   private Boolean deriveTypeCollVisible = false;

   // boolean subDeriveCollVisible = false; //

   private Boolean cessionPfVisible = false;

   private Boolean cessionCollVisible = false;

   private Boolean echansCedesCollVisible = false;

   private Boolean derivesCedesCollVisible = false;

   private Boolean cessionTypeCollVisible = false;

   // Boolean subCessionCollVisible = false;

   private ListModel<String> pfModel;

   private final List<Plateforme> pfList = new ArrayList<>(ManagerLocator.getPlateformeManager().findAllObjectsManager());

   private final List<String> pfListString = new ArrayList<>();

   @Wire
   private Selectbox typesSelectbox;

   @Wire
   private Div win1;

   @Wire
   private Div patRowDiv;

   @Wire
   private Div prelRowDiv;

   @Wire
   private Div echanRowDiv;

   @Wire
   private Div deriveRowDiv;

   @Wire
   private Div cessionRowDiv;

   @Wire
   private Datebox db1;

   @Wire
   private Datebox db2;

   private Date date_debut;

   private Date date_fin;

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
      Selectors.wireEventListeners(view, this);

      if(win1 != null){
         final int height =
            (((MainWindow) view.getPage().getFellow("mainWin").getAttributeOrFellow("mainWin$composer", true)).getPanelHeight())
               - 10;
         win1.setHeight(String.valueOf(height) + "px");
         ((Panel) win1.getFirstChild()
            //.getFirstChild()
            .getFirstChild()).setHeight(String.valueOf(height) + "px");
         ((Tree) win1.getFellow("tree")).setHeight(String.valueOf(height - 250) + "px");
         // ((Div) win1.getFirstChild().getLastChild().getFirstChild())
         ((Div) win1.getFirstChild().getLastChild()).setHeight(String.valueOf(height) + "px");
      }
   }

   public void initPlateformesAndBanques(){

      // on récupère les plateformes accessibles
      plateformes.addAll(ManagerLocator.getUtilisateurManager().getAvailablePlateformesManager(loggeduser));

      if(plateformes.size() > 0){
         selectedPlateforme = plateformes.get(0);
      }else{
         selectedPlateforme = null;
      }

      // init des banques
      banques.addAll(
         ManagerLocator.getUtilisateurManager().getAvailableBanquesByPlateformeManager(loggeduser, selectedPlateforme, true));

      if(banques.size() > 0){
         selectedBanque = banques.get(0);
      }else{
         selectedBanque = null;
      }
   }

   @Command("initPfChange")
   @NotifyChange("banques")
   public void changeBanques(final Event event) throws Exception{
      banques.clear();
      if(selectedPlateforme != null){
         banques.addAll(
            ManagerLocator.getUtilisateurManager().getAvailableBanquesByPlateformeManager(loggeduser, selectedPlateforme, true));
      }

      if(banques.size() > 0){
         selectedBanque = banques.get(0);
      }else{
         selectedBanque = null;
      }
   }

   public ChartsVM(){}

   @Init
   public void init(@ScopeParam(scopes = Scope.SESSION, value = "User") final Utilisateur u){
      loggeduser = u;
      initDate();
      updateDatas();
      for(final Plateforme p : pfList){
         pfListString.add(p.getNom());
      }
      pfModel = new ListModelList<>(pfListString);

      // bloc titles
      setPatientBlocTitle(Labels.getLabel("charts.entite.Patient.pluriel"));
      setPrelevementBlocTitle(Labels.getLabel("charts.entite.Prelevement.pluriel"));
      setEchantillonBlocTitle(Labels.getLabel("charts.entite.Echantillon.pluriel"));
      setDeriveBlocTitle(Labels.getLabel("charts.entite.ProdDerive.pluriel"));
      setCessionBlocTitle(Labels.getLabel("charts.entite.Cession.pluriel"));

      initPlateformesAndBanques();
   }

   @NotifyChange({"datas", "chartsVisible", "prelTypeCollTitle", "prelEtabCollTitle", "prelConsentCollTitle",
      "echanTypeCollTitle", "echanCimCollTitle", "echanOrgCollTitle", "deriveTypeCollTitle", "cessionTypeCollTitle"})
   //		"collVisible",
   //		"subPrelCollVisible",
   //		"subEchanCollVisible",
   //		"subDeriveCollVisible",
   //		"subCessionCollVisible","patientPfVisible",
   //		"patientCollVisible",
   //		"prelPfVisible",
   //		"prelCollVisible",
   //		"prelEtabCollVisible",
   //		"prelConsentCollVisible",
   //		"prelTypeCollVisible",
   //		"echanPfVisible",
   //		"echanCollVisible",
   //		"collVisible",
   //		"subPrelCollVisible",
   //		"subEchanCollVisible",
   //		"subDeriveCollVisible",
   //		"subCessionCollVisible","patientPfVisible",
   //		"patientCollVisible",
   //		"prelPfVisible",
   //		"prelCollVisible",
   //		"prelEtabCollVisible",
   //		"prelConsentCollVisible",
   //		"prelTypeCollVisible",
   //		"echanPfVisible",
   //		"echanCollVisible",
   //		"echanCimCollVisible",
   //		"echanOrgCollVisible",
   //		"echanTypeCollVisible",
   //		"derivePfVisible",
   //		"deriveCollVisible",
   //		"derivesCedesCollVisible",
   //		"cessionPfVisible",
   //		"cessionCollVisible",
   //		"echansCedesCollVisible",
   //		"deriveTypeCollModelTrg",
   //		"cessionTypeCollVisible"})
   @GlobalCommand
   public void updateDatas(){

      final ChartDataTriggers trgs = new ChartDataTriggers();

      trgs.setPatientPfModelTrg(patientPfVisible);
      trgs.setPatientCollModelTrg(patientCollVisible);

      trgs.setPrelevementPfModelTrg(prelPfVisible);
      trgs.setPrelevementCollModelTrg(prelCollVisible);
      trgs.setPrelEtabCollModelTrg(prelEtabCollVisible);
      trgs.setPrelTypeCollModelTrg(prelTypeCollVisible);
      trgs.setPrelConsentCollModelTrg(prelConsentCollVisible);

      trgs.setEchanPfModelTrg(echanPfVisible);
      trgs.setEchanCollModelTrg(echanCollVisible);
      trgs.setEchanCimCollModelTrg(echanCimCollVisible);
      trgs.setEchanOrgCollModelTrg(echanOrgCollVisible);
      trgs.setEchanTypeCollModelTrg(echanTypeCollVisible);

      trgs.setDerivePfModelTrg(derivePfVisible);
      trgs.setDeriveCollModelTrg(deriveCollVisible);
      trgs.setDeriveTypeCollModelTrg(deriveTypeCollVisible);

      trgs.setCessionPfModelTrg(cessionPfVisible);
      trgs.setCessionCollModelTrg(cessionCollVisible);
      trgs.setCessionTypeCollModelTrg(cessionTypeCollVisible);
      trgs.setEchansCedesCollModelTrg(echansCedesCollVisible);
      trgs.setDerivesCedesCollModelTrg(derivesCedesCollVisible);

      // init niveau PFs
      if(loggeduser != null && !loggeduser.isSuperAdmin()){
         datas = new ChartData(date_debut, date_fin, ManagerLocator.getUtilisateurManager().getPlateformesManager(loggeduser),
            trgs, selectedPlateforme != null ? selectedPlateforme.getNom() : null,
            selectedBanque != null ? selectedBanque.getNom() : null);
      }else{
         datas = new ChartData(date_debut, date_fin, new HashSet<Plateforme>(), trgs,
            selectedPlateforme != null ? selectedPlateforme.getNom() : null,
            selectedBanque != null ? selectedBanque.getNom() : null);
      }
   }

   @Command
   public void onDisplayChange() throws ParseException{
      if(db1.getValue() == null){
         date_debut = null;
      }else{
         date_debut = db1.getValue();
      }
      if(db2.getValue() == null){
         date_fin = null;
      }else{
         date_fin = db2.getValue();
      }

      Clients.showBusy(win1, "recalcul...");
      Events.echoEvent("onLaterInit", win1, null);
      chartsVisible = true;

   }

   /**
    * Evènement clic changement de date
    * déclenche la mise à jour des graphes en fonction des dates selectionnées
    * @throws ParseException
    */
   @Command
   public void onDateChange() throws ParseException{
      date_debut = db1.getValue();
      date_fin = db2.getValue();
      // intervalles incomplets
      if(date_debut != null && date_fin == null){
         date_fin = new SimpleDateFormat("yyyy/MM/dd").parse("2900/01/01");
      }else if(date_fin != null && date_debut == null){
         date_debut = new SimpleDateFormat("yyyy/MM/dd").parse("1900/01/01");
      }

      Clients.showBusy(win1, "recalcul...");
      Events.echoEvent("onLaterInit", win1, null);
   }

   @Listen("onLaterInit=#win1")
   public void onLaterInit(){

      BindUtils.postGlobalCommand(null, null, "updateDatas", null);

      Clients.clearBusy(win1);
   }

   /**
    * Evènement clic reset de date
    * déclenche la mise à jour des graphes en remetant les dates larges
    */
   @Command
   public void onReset(){
      initDate();
      db1.setValue(null);
      db2.setValue(null);
      resetTree();
   }

   private void resetTree(){
      patientPfVisible = false;
      patientCollVisible = false;

      prelPfVisible = false;
      prelCollVisible = false;
      prelTypeCollVisible = false;
      prelEtabCollVisible = false;
      prelConsentCollVisible = false;

      echanPfVisible = false;
      echanCollVisible = false;
      echanTypeCollVisible = false;
      echanCimCollVisible = false;
      echanOrgCollVisible = false;

      derivePfVisible = false;
      deriveCollVisible = false;
      deriveTypeCollVisible = false;

      cessionPfVisible = false;
      cessionCollVisible = false;
      echansCedesCollVisible = false;
      derivesCedesCollVisible = false;
      cessionTypeCollVisible = false;
   }

   //	/**
   //	 * Evènement clic changement de plateformes
   //	 * déclenche une mise à jour des différents
   //	 * graphes dépendants
   //	 * @param nom de la plateforme
   //	 */
   //	@Command("pfChange")
   //	@NotifyChange({"datas",
   //			"patientCollModel",
   //			"prelevementCollModel",
   //			"echanCollModel",
   //			"deriveCollModel",
   //			"cessionCollModel",
   //			"patientBlocTitle",
   //			"prelevementBlocTitle",
   //			"echantillonBlocTitle",
   //			"deriveBlocTitle",
   //			"cessionBlocTitle",
   //			"collVisible",
   //			"subPrelCollVisible",
   //			"subEchanCollVisible",
   //			"subDeriveCollVisible",
   //			"subCessionCollVisible"
   //			})
   //	public void onPlateformeChanged(@BindingParam("msg") String message) {
   //
   //		Clients.showBusy(patRowDiv, Labels.getLabel("charts.refresh.message"));
   //		Clients.showBusy(prelRowDiv, Labels.getLabel("charts.refresh.message"));
   //		Clients.showBusy(echanRowDiv, Labels.getLabel("charts.refresh.message"));
   //		Clients.showBusy(deriveRowDiv, Labels.getLabel("charts.refresh.message"));
   //		Clients.showBusy(cessionRowDiv, Labels.getLabel("charts.refresh.message"));
   //
   //		String pfNom = extractCategName(message);
   //
   //		updateBlocTitles(null);
   //
   //		datas.updateCountsAfterPlateformeChange(date_debut, date_fin, pfNom);
   //
   //		Clients.clearBusy(patRowDiv);
   //		Clients.clearBusy(prelRowDiv);
   //		Clients.clearBusy(echanRowDiv);
   //		Clients.clearBusy(deriveRowDiv);
   //		Clients.clearBusy(cessionRowDiv);
   //	}
   //
   //	/**
   //	 * Evènement clic changement de collection ligne prélèvements
   //	 * déclenche une mise à jour des différents
   //	 * graphes dépendants
   //	 * @param nom de la banque sélectionnée
   //	 */
   //	@Command("prelCollChange")
   //	@NotifyChange({"datas",
   //			"prelTypeCollModel",
   //			"prelEtabCollModel",
   //			"prelConsentCollModel",
   //			"prelevementBlocTitle",
   //			"subPrelCollVisible",
   //			"prelTypeCollTitle",
   //			"prelEtabCollTitle",
   //			"prelConsentCollTitle"
   //			})
   //	public void onPrelCollChange(@BindingParam("msg") String message) {
   //		String bName = extractCategName(message);
   //
   //		// currentPrelBanqueName = bName;
   //
   //		datas.updateCountsAfterBanqueChange(date_debut, date_fin, bName, "Prelevement",
   //									bName);
   //
   //		setPrelevementBlocTitle(updateBlocTitle(getPrelevementBlocTitle(), bName));
   //
   //		//subPrelCollVisible = true;
   //	}
   //
   //	/**
   //	 * Evènement clic changement de collection ligne échantillons
   //	 * déclenche une mise à jour et un affichage des différents
   //	 * graphes dépendants
   //	 * @param nom de la banque sélectionnée
   //	 */
   //	@Command("echanCollChange")
   //	@NotifyChange({"datas",
   //			"echanTypeCollModel",
   //			"echanCimCollModel",
   //			"echanOrgCollModel",
   //			"echantillonBlocTitle",
   //			"subEchanCollVisible",
   //			"echanTypeCollTitle",
   //			"echanCimCollTitle",
   //			"echanOrgCollTitle"
   //			})
   //	public void onEchanCollChange(@BindingParam("msg") String message) {
   //		String bName = extractCategName(message);
   //
   //		// currentEchanBanqueName = bName;
   //
   //		datas.updateCountsAfterBanqueChange(date_debut, date_fin, bName, "Echantillon", bName);
   //
   //		setEchantillonBlocTitle(updateBlocTitle(getEchantillonBlocTitle(), bName));
   //
   //		//subEchanCollVisible = true;
   //	}
   //
   //	/**
   //	 * Evènement clic changement de collection ligne dérivés
   //	 * déclenche une mise à jour et un affichage des différents
   //	 * graphes dépendants
   //	 * @param nom de la banque sélectionnée
   //	 */
   //	@Command("deriveCollChange")
   //	@NotifyChange({"datas",
   //			"deriveTypeCollModel",
   //			"deriveBlocTitle",
   //			"subDeriveCollVisible",
   //			"deriveTypeCollTitle"
   //			})
   //	public void onDeriveCollChange(@BindingParam("msg") String message) {
   //		String bName = extractCategName(message);
   //
   //		// currentDeriveBanqueName = bName;
   //
   //		datas.updateCountsAfterBanqueChange(date_debut, date_fin, bName, "ProdDerive",
   //													bName);
   //
   //		setDeriveBlocTitle(updateBlocTitle(getDeriveBlocTitle(), bName));
   //
   //		//subDeriveCollVisible = true;
   //	}
   //
   //	/**
   //	 * Evènement clic changement de collection ligne cessions
   //	 * déclenche une mise à jour et un affichage des différents
   //	 * graphes dépendants
   //	 * @param nom de la banque sélectionnée
   //	 */
   //	@Command("cessionCollChange")
   //	@NotifyChange({"datas",
   //			"cessionTypeCollModel",
   //			"cessionBlocTitle",
   //			"subCessionCollVisible",
   //			"cessionTypeCollTitle"
   //			})
   //	public void onCessionCollChange(@BindingParam("msg") String message) {
   //		String bName = extractCategName(message);
   //
   //		// currentCessionBanqueName = bName;
   //
   //		datas.updateCountsAfterBanqueChange(date_debut, date_fin, bName, "Cession", bName);
   //
   //		setCessionBlocTitle(updateBlocTitle(getCessionBlocTitle(), bName));
   //
   //		//subCessionCollVisible = true;
   //	}
   //
   //	/**
   //	 * outil pour extraire le nom d'une collection depuis un clic sur chart
   //	 * @param message
   //	 * @return
   //	 */
   //	private String extractCategName(String message) {
   //		if (!message.contains(":")) return message;
   //
   //		return message.substring(0, message.lastIndexOf(":")).trim();
   //	}
   //
   //	/**
   //	 * Met à jour les titres de bloc à partir des noms de la
   //	 * plateforme et de la banque actuellement selectionnées
   //	 * @param title Titre de bloc à modifier
   //	 * @param banque courante
   //	 * @return le titre modifié
   //	 */
   //	private String updateBlocTitle(String title, String bkNom) {
   //		if (title.contains(">")) {
   //			title = title.substring(0, title.indexOf(">") - 1);
   //		}
   //
   //		if (getSelectedPlateforme() != null) {
   //			title = title + " > " + getSelectedPlateforme().getNom();
   //			if (bkNom != null) {
   //				title = title + " > " + bkNom;
   //			}
   //		}
   //		return title;
   //	}
   //
   //	private void updateBlocTitles(String bkName) {
   //		setPatientBlocTitle(updateBlocTitle(getPatientBlocTitle(), bkName));
   //		setPrelevementBlocTitle(updateBlocTitle(getPrelevementBlocTitle(), bkName));
   //		setEchantillonBlocTitle(updateBlocTitle(getEchantillonBlocTitle(), bkName));
   //		setDeriveBlocTitle(updateBlocTitle(getDeriveBlocTitle(), bkName));
   //		setCessionBlocTitle(updateBlocTitle(getCessionBlocTitle(), bkName));
   //	}

   private void initDate(){
      date_debut = null;
      date_fin = null;
   }

   public ChartData getDatas(){
      return datas;
   }

   public String getPatientBlocTitle(){
      return patientBlocTitle;
   }

   public void setPatientBlocTitle(final String s){
      this.patientBlocTitle = s;
   }

   public String getPrelevementBlocTitle(){
      return prelevementBlocTitle;
   }

   public void setPrelevementBlocTitle(final String s){
      this.prelevementBlocTitle = s;
   }

   public String getEchantillonBlocTitle(){
      return echantillonBlocTitle;
   }

   public void setEchantillonBlocTitle(final String s){
      this.echantillonBlocTitle = s;
   }

   public String getDeriveBlocTitle(){
      return deriveBlocTitle;
   }

   public void setDeriveBlocTitle(final String s){
      this.deriveBlocTitle = s;
   }

   public String getCessionBlocTitle(){
      return cessionBlocTitle;
   }

   public void setCessionBlocTitle(final String s){
      this.cessionBlocTitle = s;
   }

   public void setPatientPfVisible(final Boolean b){
      patientPfVisible = b;
   }

   public Boolean getPatientPfVisible(){
      return patientPfVisible;
   }

   public void setPatientCollVisible(final Boolean b){
      patientCollVisible = b;
   }

   public Boolean getPatientCollVisible(){
      return patientCollVisible;
   }

   public void setPrelPfVisible(final Boolean b){
      prelPfVisible = b;
   }

   public Boolean getPrelPfVisible(){
      return prelPfVisible;
   }

   public void setPrelCollVisible(final Boolean b){
      prelCollVisible = b;
   }

   public Boolean getPrelCollVisible(){
      return prelCollVisible;
   }

   public void setPrelTypeCollVisible(final Boolean b){
      prelTypeCollVisible = b;
   }

   public Boolean getPrelTypeCollVisible(){
      return prelTypeCollVisible;
   }

   public void setPrelEtabCollVisible(final Boolean b){
      prelEtabCollVisible = b;
   }

   public Boolean getPrelEtabCollVisible(){
      return prelEtabCollVisible;
   }

   public void setPrelConsentCollVisible(final Boolean b){
      prelConsentCollVisible = b;
   }

   public Boolean getPrelConsentCollVisible(){
      return prelConsentCollVisible;
   }

   public void setEchanPfVisible(final Boolean b){
      echanPfVisible = b;
   }

   public Boolean getEchanPfVisible(){
      return echanPfVisible;
   }

   public void setDerivePfVisible(final Boolean b){
      derivePfVisible = b;
   }

   public Boolean getDerivePfVisible(){
      return derivePfVisible;
   }

   public void setCessionPfVisible(final Boolean b){
      cessionPfVisible = b;
   }

   public Boolean getEchanCollVisible(){
      return echanCollVisible;
   }

   public Boolean getEchanTypeCollVisible(){
      return echanTypeCollVisible;
   }

   public Boolean getEchanCimCollVisible(){
      return echanCimCollVisible;
   }

   public Boolean getEchanOrgCollVisible(){
      return echanOrgCollVisible;
   }

   public Boolean getDeriveCollVisible(){
      return deriveCollVisible;
   }

   public Boolean getDeriveTypeCollVisible(){
      return deriveTypeCollVisible;
   }

   public Boolean getDerivesCedesCollVisible(){
      return derivesCedesCollVisible;
   }

   public Boolean getCessionCollVisible(){
      return cessionCollVisible;
   }

   public Boolean getEchansCedesCollVisible(){
      return echansCedesCollVisible;
   }

   public Boolean getCessionTypeCollVisible(){
      return cessionTypeCollVisible;
   }

   public void setEchanCollVisible(final Boolean echanCollVisible){
      this.echanCollVisible = echanCollVisible;
   }

   public void setEchanTypeCollVisible(final Boolean echanTypeCollVisible){
      this.echanTypeCollVisible = echanTypeCollVisible;
   }

   public void setEchanCimCollVisible(final Boolean echanCimCollVisible){
      this.echanCimCollVisible = echanCimCollVisible;
   }

   public void setEchanOrgCollVisible(final Boolean echanOrgCollVisible){
      this.echanOrgCollVisible = echanOrgCollVisible;
   }

   public void setDeriveCollVisible(final Boolean deriveCollVisible){
      this.deriveCollVisible = deriveCollVisible;
   }

   public void setDeriveTypeCollVisible(final Boolean deriveTypeCollVisible){
      this.deriveTypeCollVisible = deriveTypeCollVisible;
   }

   public void setDerivesCedesCollVisible(final Boolean derivesCedesCollVisible){
      this.derivesCedesCollVisible = derivesCedesCollVisible;
   }

   public void setCessionCollVisible(final Boolean cessionCollVisible){
      this.cessionCollVisible = cessionCollVisible;
   }

   public void setEchansCedesCollVisible(final Boolean echansCedesCollVisible){
      this.echansCedesCollVisible = echansCedesCollVisible;
   }

   public void setCessionTypeCollVisible(final Boolean cessionTypeCollVisible){
      this.cessionTypeCollVisible = cessionTypeCollVisible;
   }

   public Boolean getCessionPfVisible(){
      return cessionPfVisible;
   }

   public String getPrelTypeCollTitle(){
      return ObjectTypesFormatters.getLabel("charts.counts.prelevement.types",
         new String[] {getSelectedBanque() != null ? getSelectedBanque().getNom() : ""});
   }

   public String getPrelEtabCollTitle(){
      return ObjectTypesFormatters.getLabel("charts.counts.prelevement.etablissements",
         new String[] {getSelectedBanque() != null ? getSelectedBanque().getNom() : ""});
   }

   public String getPrelConsentCollTitle(){
      return ObjectTypesFormatters.getLabel("charts.counts.prelevement.consents",
         new String[] {getSelectedBanque() != null ? getSelectedBanque().getNom() : ""});
   }

   public String getEchanTypeCollTitle(){
      return ObjectTypesFormatters.getLabel("charts.counts.echantillon.types",
         new String[] {getSelectedBanque() != null ? getSelectedBanque().getNom() : ""});
   }

   public String getEchanCimCollTitle(){
      return ObjectTypesFormatters.getLabel("charts.counts.echantillon.cims",
         new String[] {getSelectedBanque() != null ? getSelectedBanque().getNom() : ""});
   }

   public String getEchanOrgCollTitle(){
      return ObjectTypesFormatters.getLabel("charts.counts.echantillon.organes",
         new String[] {getSelectedBanque() != null ? getSelectedBanque().getNom() : ""});
   }

   public String getDeriveTypeCollTitle(){
      return ObjectTypesFormatters.getLabel("charts.counts.derive.types",
         new String[] {getSelectedBanque() != null ? getSelectedBanque().getNom() : ""});
   }

   public String getCessionTypeCollTitle(){
      return ObjectTypesFormatters.getLabel("charts.counts.cession.types",
         new String[] {getSelectedBanque() != null ? getSelectedBanque().getNom() : ""});
   }

   public GraphesModele getPatientPfModel(){
      return datas.getPatientPfModel();
   }

   public GraphesModele getPrelevementPfModel(){
      return datas.getPrelevementPfModel();
   }

   public GraphesModele getEchanPfModel(){
      return datas.getEchanPfModel();
   }

   public GraphesModele getDerivePfModel(){
      return datas.getDerivePfModel();
   }

   public GraphesModele getCessionPfModel(){
      return datas.getCessionPfModel();
   }

   public GraphesModele getPatientCollModel(){
      return datas.getPatientCollModel();
   }

   public GraphesModele getPrelevementCollModel(){
      return datas.getPrelevementCollModel();
   }

   public GraphesModele getEchanCollModel(){
      return datas.getEchanCollModel();
   }

   public GraphesModele getDeriveCollModel(){
      return datas.getDeriveCollModel();
   }

   public GraphesModele getCessionCollModel(){
      return datas.getCessionCollModel();
   }

   public GraphesModele getPrelTypeCollModel(){
      return datas.getPrelTypeCollModel();
   }

   public GraphesModele getPrelEtabCollModel(){
      return datas.getPrelEtabCollModel();
   }

   public GraphesModele getPrelConsentCollModel(){
      return datas.getPrelConsentCollModel();
   }

   public GraphesModele getEchanTypeCollModel(){
      return datas.getEchanTypeCollModel();
   }

   public GraphesModele getEchanCimCollModel(){
      return datas.getEchanCimCollModel();
   }

   public GraphesModele getEchanOrgCollModel(){
      return datas.getEchanOrgCollModel();
   }

   public GraphesModele getDeriveTypeCollModel(){
      return datas.getDeriveTypeCollModel();
   }

   public GraphesModele getCessionTypeCollModel(){
      return datas.getCessionTypeCollModel();
   }

   public Date getDate_debut(){
      return date_debut;
   }

   public Date getDate_fin(){
      return date_fin;
   }

   @Listen("onClose = div > window")
   public void onZoomChartOut(final Event event){

      event.stopPropagation();
      Events.postEvent("onClick", event.getTarget().getFirstChild().getFellow("zoom"), null);
   }

   public ListModel<String> getPfModel(){
      return pfModel;
   }

   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final List<Banque> b){
      this.banques = b;
   }

   public Banque getSelectedBanque(){
      return selectedBanque;
   }

   public void setSelectedBanque(final Banque selected){
      this.selectedBanque = selected;
   }

   public List<Plateforme> getPlateformes(){
      return plateformes;
   }

   public void setPlateformes(final List<Plateforme> p){
      this.plateformes = p;
   }

   public Plateforme getSelectedPlateforme(){
      return selectedPlateforme;
   }

   public void setSelectedPlateforme(final Plateforme sPlateforme){
      this.selectedPlateforme = sPlateforme;
   }

   public Boolean getMenuVisible(){
      return menuVisible;
   }

   public void setMenuVisible(final Boolean menuVisible){
      this.menuVisible = menuVisible;
   }

   public Boolean getChartsVisible(){
      return chartsVisible;
   }

   public void setChartsVisible(final Boolean chartsVisible){
      this.chartsVisible = chartsVisible;
   }
}
