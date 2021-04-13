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
package fr.aphp.tumorotek.webapp.general.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.GroupComparator;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Classe permettant à l'utilisateur de restreindre l'export de
 * certaine tables d'annotations au moment d'un export
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10
 *
 */
public class RestrictTablesModale
{

   @Wire("#fwinRestrictTablesModale")
   private Window fwinRestrictTablesModale;
   @WireVariable
   private Session _sess;

   private TableGroupModel groupModel;

   private AbstractListeController2 listeController;

   private List<Integer> restrictedId = new ArrayList<>();
   private Entite entite;
   // FicheRechercheAvancée qui a commandé le résultat
   private Component parent;
   private final List<TableAnnotation> tables = new ArrayList<>();

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);

      groupModel = new TableGroupModel(getTables(), new TableComparator());
      groupModel.setMultiple(true);
      groupModel.setSelection(getTables());

      if(getTables().isEmpty()){
         export();
      }
   }

   @Init
   public void init(@ExecutionArgParam("controller") final AbstractListeController2 m,
      @ExecutionArgParam("parent") final Component p, @ExecutionArgParam("entite") final Entite e){
      setListeController(m);
      setEntite(e);
      setParent(p);
   }

   public TableGroupModel getGroupModel(){
      return groupModel;
   }

   @Command("selectGroup")
   public void selectGroup(@BindingParam("data") final Object data){
      if(data instanceof TableGroupModel.TableInfo){
         final TableGroupModel.TableInfo groupInfo = (TableGroupModel.TableInfo) data;
         final int groupIndex = groupInfo.getGroupIndex();
         final int childCount = groupModel.getChildCount(groupIndex);
         final boolean added = groupModel.isSelected(groupInfo);
         for(int childIndex = 0; childIndex < childCount; childIndex++){
            final TableAnnotation table = groupModel.getChild(groupIndex, childIndex);
            if(added){
               groupModel.addToSelection(table);
            }else{
               groupModel.removeFromSelection(table);
            }
         }
      }
   }

   /**
    * Récupère et filtre les tables en fonction de l'entite concernée par l'export.
    * @version 2.0.10.5 
    */
   private List<TableAnnotation> getTables(){
      if(tables.isEmpty()){
         if(!entite.getNom().equals("Cession")){
            for(final TableAnnotation tab : ManagerLocator.getTableAnnotationManager()
               .findByBanquesManager(SessionUtils.getSelectedBanques(_sess.getAttributes()), true)){
               if(tab.getEntite().getEntiteId() <= getEntite().getEntiteId() && tab.getEntite().getEntiteId() != 5){ // tables cession exclues
                  tables.add(tab);
               }
            }
         }else{
            for(final TableAnnotation tab : ManagerLocator.getTableAnnotationManager()
               .findByBanquesManager(SessionUtils.getSelectedBanques(_sess.getAttributes()), true)){
               if(tab.getEntite().getEntiteId() == 5){ // tables cession 
                  tables.add(tab);
               }
            }
         }
      }

      return tables;
   }

   public class TableComparator implements Comparator<TableAnnotation>, GroupComparator<TableAnnotation>, Serializable
   {
      private static final long serialVersionUID = 1L;

      @Override
      public int compare(final TableAnnotation t1, final TableAnnotation t2){
         return t1.getNom().compareTo(t2.getNom());
      }

      @Override
      public int compareGroup(final TableAnnotation t1, final TableAnnotation t2){
         if(t1.getEntite().getEntiteId().equals(t2.getEntite().getEntiteId())){
            return 0;
         }else{
            return 1;
         }
      }
   }

   public List<Integer> getRestrictedId(){
      return restrictedId;
   }

   public void setRestrictedId(final List<Integer> restrictedId){
      this.restrictedId = restrictedId;
   }

   public AbstractListeController2 getListeController(){
      return listeController;
   }

   public void setListeController(final AbstractListeController2 l){
      this.listeController = l;
   }

   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite e){
      this.entite = e;
   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component p){
      this.parent = p;
   }

   @Command
   public void cancel(){
      Events.postEvent("onClose", fwinRestrictTablesModale, null);
   }

   @Command
   public void export(){
      for(final Object tb : groupModel.getSelection()){
         if(tb instanceof TableAnnotation){
            getRestrictedId().add(((TableAnnotation) tb).getTableAnnotationId());
         }
      }
      // envoie null au controller si la liste est vide
      getListeController().setRestrictedTableIds(!getRestrictedId().isEmpty() ? getRestrictedId() : null);

      // modale ouverte depuis ListeController
      if(parent == null){
    	 Events.postEvent("onClick$exportItem", getListeController().getSelfComponent(), null);
         // getListeController().onLaterExport(true);
      }else{ // modale ouverte depuis Fiche ResultatsModale
         Events.postEvent("onClick$exportItem", getParent(), null);
      }

      // ferme la fenêtre
      cancel();
   }
}