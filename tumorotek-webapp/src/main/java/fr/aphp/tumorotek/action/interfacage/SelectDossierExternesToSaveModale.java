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
package fr.aphp.tumorotek.action.interfacage;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 */
public class SelectDossierExternesToSaveModale implements Serializable
{

   // private final Log log = LogFactory.getLog(SelectDossierExternesToSaveModale.class);

   private static final long serialVersionUID = 5225330380328203307L;

   private Emetteur emetteur;

   private final ListModelList<DossierDecorator> dossiersDecorators = new ListModelList<>();

   private final Map<DossierExterne, String> dosErr = new HashMap<>();

   private MainWindow mainWindow;

   // flag pour savoir si des dossiers ont été sauvés -> rafraichir la page
   private boolean firstTimeSync = true;

   private Boolean dossiersSaved = false;

   @WireVariable
   private Session _sess;

   //   @AfterCompose
   //   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
   //      Selectors.wireComponents(view, this, false);
   //   }

   @Init
   public void init(@ExecutionArgParam("emetteur") final Emetteur emet, @ExecutionArgParam("mainWindow") final MainWindow _mW){
      this.emetteur = emet;
      this.mainWindow = _mW;
      dossiersDecorators.setMultiple(true);

      searchForDossierExternes();
   }

   public String getTitle(){
      return ObjectTypesFormatters.getLabel("interfacage.DossierExterne.children.tranmis",
         new String[] {emetteur.getIdentification()});
   }

   /**
    * Méthode initialisant les dossiers externes.
    * en recherchant les dossiers envoyés par un emetteur précis, qui n'ont pas
    * d'entite id spécifiée (= dossiers parents).
    */
   public void searchForDossierExternes(){

      dossiersDecorators.clear();

      // recherche tous les dossiers prélèvement primaires
      // entite id is null
      // en retirant  de la liste les dossiers à synchroniser manuellement
      // = ceux correspondant à un prélèvement en banque
      final Collection<DossierExterne> dossiersPrimaires = CollectionUtils
         .subtract(ManagerLocator.getDossierExterneManager().findByEmetteurAndEntiteNullManager(emetteur), ManagerLocator
            .getInjectionManager().findExistingPrelevementByEmetteurManager(emetteur, SessionUtils.getCurrentPlateforme()));

      // decoration pour affichage
      dossiersDecorators
         .addAll(dossiersPrimaires.stream().map(d -> new DossierDecorator(d, dosErr.get(d))).collect(Collectors.toList()));

      // synchronisation silencieuse des dossiers dérivés enfants dont
      // le prélèvement parent existe déja en base
      if(firstTimeSync){
         dossiersSaved =
            ManagerLocator.getInjectionManager().synchronizeDeriveChildrenManager(emetteur, SessionUtils.getCurrentPlateforme(),
               SessionUtils.getLoggedUser(_sess.getAttributes()), SessionUtils.getSystemBaseDir());
         firstTimeSync = false;
      }
   }

   @Command
   @NotifyChange("dossiersExternes")
   public void save(@ContextParam(ContextType.VIEW) final Window win){

      dosErr.clear();

      for(final DossierDecorator dos : dossiersDecorators.getSelection()){
         try{
            ManagerLocator.getInjectionManager().saveDossierAndChildrenManager(dos.getDossierExterne(),
               SessionUtils.getCurrentBanque(_sess.getAttributes()), SessionUtils.getLoggedUser(_sess.getAttributes()),
               SessionUtils.getSystemBaseDir());
            dossiersSaved = true;
         }catch(final Exception e){
            dosErr.put(dos.getDossierExterne(), AbstractController.handleExceptionMessage(e));
         }
      }

      if(!dosErr.isEmpty()){
         // update
         searchForDossierExternes();
      }else{
         close(win);
      }
   }

   /**
    * Rafraichit l'onglet prélèvement
    */
   @Command
   public void close(@ContextParam(ContextType.VIEW) final Window comp){
      if(dossiersSaved){
         PrelevementController.backToMe(mainWindow, comp.getPage()).reset();
      }
      comp.detach();
   }

   @Command
   @NotifyChange("dossiersExternes")
   public void delete(){

      for(final DossierDecorator dos : dossiersDecorators.getSelection()){
         try{
            ManagerLocator.getDossierExterneManager().removeObjectManager(dos.getDossierExterne());
         }catch(final Exception e){
            AbstractController.handleExceptionMessage(e);
         }
      }

      // update
      searchForDossierExternes();
   }

   public ListModelList<DossierDecorator> getDossiersDecorators(){
      return dossiersDecorators;
   }

   /**
    * Classe decorateur DossierExterne pour y apposer
    * le nombre de dossiers enfants
    * @since 2.2.3-genno
    */
   public class DossierDecorator
   {

      private DossierExterne dossierExterne;

      private String nature = "?";

      private String nom = "";

      private Integer nbChildren;

      private String errMsg;

      public DossierDecorator(final DossierExterne _d, final String _e){
         this.dossierExterne = _d;
         this.errMsg = _e;
         if(_d != null){
            final List<BlocExterne> bPat = ManagerLocator.getBlocExterneManager().findByDossierExterneAndEntiteManager(_d,
               ManagerLocator.getEntiteManager().findByIdManager(1));

            final ValeurExterne nomVal = ManagerLocator.getValeurExterneManager().findByBlocExterneManager(bPat.get(0)).stream()
               .filter(v -> v.getChampEntiteId().equals(3)).findFirst().orElse(null);
            if(nomVal != null){
               setNom(nomVal.getValeur());
            }

            final List<BlocExterne> bPrel = ManagerLocator.getBlocExterneManager().findByDossierExterneAndEntiteManager(_d,
               ManagerLocator.getEntiteManager().findByIdManager(2));

            if(bPrel.size() > 0){
               final ValeurExterne natureVal = ManagerLocator.getValeurExterneManager().findByBlocExterneManager(bPrel.get(0))
                  .stream().filter(v -> v.getChampEntiteId().equals(24)).findFirst().orElse(null);
               if(natureVal != null){
                  setNature(natureVal.getValeur());
               }
            }

            setNbChildren(ManagerLocator.getDossierExterneManager()
               .findChildrenByEmetteurValeurManager(emetteur, 23, _d.getIdentificationDossier()).size());
         }
      }

      public DossierExterne getDossierExterne(){
         return dossierExterne;
      }

      public void setDossierExterne(final DossierExterne _d){
         this.dossierExterne = _d;
      }

      public String getNom(){
         return nom;
      }

      public void setNom(final String _n){
         this.nom = _n;
      }

      public String getNature(){
         return nature;
      }

      public void setNature(final String _n){
         this.nature = _n;
      }

      public Integer getNbChildren(){
         return nbChildren;
      }

      public void setNbChildren(final Integer _c){
         this.nbChildren = _c;
      }

      public String getErrMsg(){
         return errMsg;
      }

      public void setErrMsg(final String _e){
         this.errMsg = _e;
      }

      public String getDateOperationFormatted(){
         if(dossierExterne != null){
            return ObjectTypesFormatters.dateRenderer2(getDossierExterne().getDateOperation());
         }
         return null;
      }

      public Boolean getInError(){
         return StringUtils.hasText(errMsg);
      }

      @Override
      public int hashCode(){
         final int prime = 31;
         int result = 1;
         result = prime * result + ((dossierExterne == null) ? 0 : dossierExterne.hashCode());
         return result;
      }

      @Override
      public boolean equals(final Object obj){
         if(obj == this){
            return true;
         }
         if(obj == null || obj.getClass() != this.getClass()){
            return false;
         }

         final DossierDecorator del = (DossierDecorator) obj;

         return (Objects.equals(dossierExterne, del.getDossierExterne()));
      }
   }
}
