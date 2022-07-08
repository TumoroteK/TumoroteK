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
package fr.aphp.tumorotek.action.imprimante;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.etiquettes.MBioBarcodePrinter;
import fr.aphp.tumorotek.action.etiquettes.MBioFileProperties;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.etiquettes.BarcodeFieldDefault;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;

public class ImprimanteModeleModale
{

   //private static final long serialVersionUID = -3694035708012726832L;

   private boolean blockModal = false;

   @Wire("#fwinImprimanteModele")
   private Window fwinImprimanteModele;

   @Wire("#modelesBox")
   private Listbox modelesBox;

   private Boolean disabled = true;

   private boolean isTest = false;

   private List<? extends TKStockableObject> objects;

   private final List<Imprimante> imprimantes = new ArrayList<>();

   private final List<Modele> modeles = new ArrayList<>();

   private Imprimante selectedImprimante = null;

   private Modele selectedModele = null;

   private String selectedRawLang = null;

   private AffectationImprimante affectation = null;

   private List<LigneEtiquette> lignes = null;

   private BarcodeFieldDefault barcodeByZPL = new BarcodeFieldDefault();

   private BarcodeFieldDefault barcodeByJS = new BarcodeFieldDefault(new Float(1.5), new Float(5.0), 7);

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
   }

   @Init
   public void init(@ExecutionArgParam("pf") final Plateforme pf,
      @ExecutionArgParam("objs") final List<? extends TKStockableObject> objs,
      @ExecutionArgParam("affectation") final AffectationImprimante aff,
      @ExecutionArgParam("lignes") final List<LigneEtiquette> _ligs){

      // passe en mode test de modele si une mock affectation est
      // passée en paramètre
      isTest = aff != null && aff.getImprimante() == null;

      imprimantes.addAll(ManagerLocator.getImprimanteManager().findByPlateformeManager(pf));

      if(isTest){ // supprime les imprimantes mbio de la liste
         final List<Imprimante> impsMbio = new ArrayList<>();
         for(final Imprimante imp : imprimantes){
            if(!imp.getImprimanteApi().getNom().equals("tumo")){
               impsMbio.add(imp);
            }
         }
         imprimantes.removeAll(impsMbio);
      }

      disabled = imprimantes.isEmpty();
      modeles.addAll(ManagerLocator.getModeleManager().findByPlateformeManager(pf));
      modeles.add(0, null);

      // imprimantesBox.setModel(new SimpleListModel<Imprimante>(imprimantes));
      // modelesBox.setModel(new SimpleListModel<Modele>(modeles));

      lignes = _ligs;

      setObjects(objs);
      setAffectation(aff);
   }

   /**
    * Si imprimante mbio, invalide liste de sélection du modèle.
    */
   public void onSelect$imprimantesBox(){
      if(getSelectedImprimante().getImprimanteApi().getNom().equals("mbio")){
         setSelectedModele(null);
         modelesBox.setDisabled(true);
      }else{
         modelesBox.setDisabled(false);
      }
   }

   @Command
   public void print(){

      // si on utilise l'API tumo
      if(selectedImprimante != null){

         // impression objets echantillons ou dérivés
         if(getObjects() != null){

            if(selectedImprimante.getImprimanteApi().getNom().equals("tumo")){

               try{
                  int completed = 0;

                  completed = ManagerLocator.getTumoBarcodePrinter().printStockableObjects(getObjects(), 1, selectedImprimante,
                     selectedModele, selectedRawLang, barcodeByZPL);

                  if(completed != 1){
                     if(completed == -1){
                        Messagebox.show(ObjectTypesFormatters.getLabel("validation.erreur.imprimante.non.detectee",
                           new String[] {selectedImprimante.getNom()}), "Error", Messagebox.OK, Messagebox.ERROR);
                     }else{
                        Messagebox.show(Labels.getLabel("validation.erreur.impression"), "Error", Messagebox.OK,
                           Messagebox.ERROR);
                     }
                  }else{
                     Messagebox.show(Labels.getLabel("general.impression.ok"), null,
                        new Messagebox.Button[] {Messagebox.Button.OK}, Messagebox.INFORMATION, new EventListener<ClickEvent>()
                        {
                           @Override
                           public void onEvent(final ClickEvent e){
                              if(e.getButton() != null){
                                 switch(e.getButton().id){
                                    case Messagebox.OK:
                                       Events.postEvent("onClose", fwinImprimanteModele, null);
                                    default: //if the Close button is clicked, e.getButton() returns null
                                 }
                              }
                           }
                        });
                  }
               }catch(final RuntimeException re){
                  Messagebox.show(AbstractController.handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
               }catch(final Exception e){
                  e.printStackTrace();
                  Messagebox.show(AbstractController.handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
               }
            }else if(selectedImprimante.getImprimanteApi().getNom().equals("mbio")){
               // si on utilise l'API mbio
               final MBioFileProperties mBioFile = new MBioFileProperties(selectedImprimante.getMbioPrinter());

               final MBioBarcodePrinter printer = new MBioBarcodePrinter(mBioFile, mBioFile.getConfDir());
               // MBioBarcodePrinter printer = new MBioBarcodePrinter();
               int completed = 0;
               completed = printer.printStockableObject(getObjects(), 1);

               if(completed != 1){
                  Messagebox.show(Labels.getLabel("validation.erreur.impression"), "Error", Messagebox.OK, Messagebox.ERROR);
               }else{
                  Messagebox.show(Labels.getLabel("general.impression.ok"), "OK", Messagebox.OK, Messagebox.INFORMATION);
               }
            }
         }else{ //impression libre
            openEtiquetteWindow(getSelectedModele(), getSelectedImprimante());
         }
      }

   }

   /**********************************************************/
   /****************** GETTERS - SETTERS *********************/
   /**********************************************************/

   public List<Imprimante> getImprimantes(){
      return imprimantes;
   }

   public List<Modele> getModeles(){
      return modeles;
   }

   public List<? extends TKStockableObject> getObjects(){
      return objects;
   }

   public void setObjects(final List<? extends TKStockableObject> obs){
      this.objects = obs;
   }

   public Imprimante getSelectedImprimante(){
      return selectedImprimante;
   }

   @NotifyChange({"isMbio", "selectedModele"})
   public void setSelectedImprimante(final Imprimante i){
      this.selectedImprimante = i;

      // efface le modele si mbio
      if(this.selectedImprimante.getImprimanteApi().getNom().equals("mbio")){
         setSelectedModele(null);
      }
   }

   public Modele getSelectedModele(){
      return selectedModele;
   }

   public void setSelectedModele(final Modele m){
      this.selectedModele = m;
   }

   private void setAffectation(final AffectationImprimante aff){

      if(!disabled){
         setSelectedImprimante(imprimantes.get(0));
      }

      affectation = aff;
      if(aff != null){
         if(aff.getImprimante() != null){
            setSelectedImprimante(aff.getImprimante());
         }
         setSelectedModele(aff.getModele());
      }
   }

   public AffectationImprimante getAffectation(){
      return affectation;
   }

   public Boolean getDisabled(){
      return disabled;
   }

   public void setDisabled(final Boolean d){
      this.disabled = d;
   }

   public void openEtiquetteWindow(Modele modele, final Imprimante imp){

      if(!isBlockModal()){

         setBlockModal(true);

         boolean isDefault = true;
         if(modele != null && modele.getIsDefault() != null){
            isDefault = modele.getIsDefault();
         }
         if(imp != null && imp.getImprimanteApi().getNom().equals("mbio")){
            modele = null;
            isDefault = true;
         }

         //			List<LigneEtiquette> lignes = new ArrayList<LigneEtiquette>();
         //			if (modele != null) {
         //				lignes = ManagerLocator.getLigneEtiquetteManager()
         //						.findByModeleManager(modele);
         //			}

         final HashMap<String, Object> map = new HashMap<>();
         map.put("imprimante", imp);
         map.put("modele", modele);
         map.put("rawLang", selectedRawLang);
         map.put("parent", fwinImprimanteModele);
         map.put("barcodeBy", barcodeByZPL);

         if(isDefault){
            Executions.createComponents("/zuls/etiquettes/FicheEtiquetteModale.zul", null, map);
         }else{
            map.put("lignes", lignes);
            Executions.createComponents("/zuls/etiquettes/FicheEtiquetteDynModale.zul", null, map);
         }

         setBlockModal(false);
      }

   }

   public boolean isBlockModal(){
      return blockModal;
   }

   public void setBlockModal(final boolean b){
      this.blockModal = b;
   }

   /**
    * Renvoie true et disable la liste de modele si l'imprimante choisie et
    * de tyme mbio ou si le modele est en test d'impression.
    * @return
    */
   public boolean getIsMbio(){
      return (getSelectedImprimante() != null && getSelectedImprimante().getImprimanteApi().getNom().equals("mbio") || isTest);
   }

   public String getSelectedRawLang(){
      return selectedRawLang;
   }

   public void setSelectedRawLang(final String s){
      this.selectedRawLang = s;
   }

   public BarcodeFieldDefault getBarcodeByZPL(){
      return barcodeByZPL;
   }

   public void setBarcodeByZPL(final BarcodeFieldDefault b){
      this.barcodeByZPL = b;
   }

   public BarcodeFieldDefault getBarcodeByJS(){
      return barcodeByJS;
   }

   public void setBarcodeByJS(final BarcodeFieldDefault b){
      this.barcodeByJS = b;
   }
}
