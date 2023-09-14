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
package fr.aphp.tumorotek.action.recherche;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.GroupComparator;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Dessine la fiche modale permettant à l'utilisateur de
 * faire une recherche Biocap.
 * Date: 30/09/2011.
 *
 * @since 2.0.13 âge limite passé 20 ans suite demande Camille BOUIN + correction liste services preleveur if empty, aucune restriction
 *
 * @author Pierre VENTADOUR
 * @version 2.0.13
 *
 */
public class FicheRechercheBiocapModale
{

   private ServiceGroupModel groupModel;

   private Component parent;

   private AbstractObjectTabController tabController;

   private final List<Service> services = new ArrayList<>();

   private List<ObjetStatut> statuts = new ArrayList<>();

   private Date dateInf;

   private Date dateSup;

   // since 2.0.13 - < 20 ans
   private final Integer age = 20;

   private ObjetStatut objetStatut = null;

   @Wire
   private Button find;

   @Wire("#fwinBiocapModale")
   private Window fwinBiocapModale;

   @WireVariable
   private Session _sess;

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
      Selectors.wireEventListeners(view, this);

      groupModel = new ServiceGroupModel(getServices(), new ServiceComparator());
      groupModel.setMultiple(true);
      for(int i = 0; i < groupModel.getGroupCount(); i++){
         groupModel.removeOpenGroup(i);
      }
      statuts.addAll(ManagerLocator.getObjetStatutManager().findAllObjectsManager());
   }

   @Init
   public void init(@ExecutionArgParam("parent") final Component p,
      @ExecutionArgParam("controller") final AbstractObjectTabController aotc){
      parent = p;
      tabController = aotc;
   }

   private List<Service> getServices(){
      if(services.isEmpty()){
         // Empty service
         final Service empty = new Service();
         empty.setNom(Labels.getLabel("recherche.biocap.empty.service"));
         empty.setArchive(true);
         final Etablissement etab = new Etablissement();
         etab.setNom(Labels.getLabel("recherche.biocap.empty.service.etablissement"));
         etab.setArchive(true);
         empty.setEtablissement(etab);
         services.add(empty);

         services.addAll(ManagerLocator.getServiceManager().findAllObjectsWithOrderManager());
      }
      return services;
   }

   public ServiceGroupModel getGroupModel(){
      return groupModel;
   }

   @Command("selectGroup")
   public void selectGroup(@BindingParam("data") final Object data){
      if(data instanceof ServiceGroupModel.ServiceInfo){
         final ServiceGroupModel.ServiceInfo groupInfo = (ServiceGroupModel.ServiceInfo) data;
         final int groupIndex = groupInfo.getGroupIndex();
         final int childCount = groupModel.getChildCount(groupIndex);
         final boolean added = groupModel.isSelected(groupInfo);
         for(int childIndex = 0; childIndex < childCount; childIndex++){
            final Service serv = groupModel.getChild(groupIndex, childIndex);
            if(added){
               groupModel.addToSelection(serv);
            }else{
               groupModel.removeFromSelection(serv);
            }
         }
      }
   }

   public class ServiceComparator implements Comparator<Service>, GroupComparator<Service>, Serializable
   {
      private static final long serialVersionUID = 1L;

      @Override
      public int compare(final Service s1, final Service s2){
         return s1.getNom().compareTo(s2.getNom());
      }

      @Override
      public int compareGroup(final Service s1, final Service s2){
         if(s1.getEtablissement().getNom().equals(s2.getEtablissement().getNom())){
            return 0;
         }
         return 1;
      }
   }

   @Command
   public void find(){
      Clients.showBusy(Labels.getLabel("recherche.avancee.en.cours"));
      Events.echoEvent("onLaterFind", find, null);
   }

   @Command
   public void cancel(){
      Events.postEvent("onClose", fwinBiocapModale, null);
   }

   @Listen("onLaterFind=#find")
   public void onLaterFind() throws NamingException{
      final Calendar calInf = Calendar.getInstance();
      calInf.setTime(dateInf);
      final Calendar calSup = Calendar.getInstance();
      calSup.setTime(dateSup);
      // on lance la recherche BIOCAP
      final List<Integer> ids = ManagerLocator.getTraitementQueryManager().findEchantillonsByRequeteBiocapManager(
         SessionUtils.getDbms(), SessionUtils.getSelectedBanques(_sess.getAttributes()), groupModel.getSelectedServices(), calInf,
         calSup, age, objetStatut);

      if(ids.size() > 500){
         tabController.openResultatsWindow(parent.getPage(), ids, parent, "Echantillon", tabController);
         // search history settings
         // createSearchHistory();
      }else{
         final List<Echantillon> values = ManagerLocator.getEchantillonManager().findByIdsInListManager(ids);
         Events.postEvent("onGetObjectFromResearch", parent, values);
      }

      // ferme wait message
      Clients.clearBusy();

      // ferme la fenêtre
      cancel();
   }

   public Date getDateInf(){
      return dateInf;
   }

   public void setDateInf(final Date d){
      this.dateInf = d;
   }

   public Date getDateSup(){
      return dateSup;
   }

   public void setDateSup(final Date d){
      this.dateSup = d;
   }

   public ObjetStatut getObjetStatut(){
      return objetStatut;
   }

   public void setObjetStatut(final ObjetStatut o){
      this.objetStatut = o;
   }

   public List<ObjetStatut> getStatuts(){
      return statuts;
   }

   public void setStatuts(final List<ObjetStatut> s){
      this.statuts = s;
   }
}
