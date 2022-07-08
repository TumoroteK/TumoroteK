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
package fr.aphp.tumorotek.action.code;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.code.AdicapGroupe;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.tree.TumoTreeModel;
import fr.aphp.tumorotek.webapp.tree.TumoTreeNode;
import fr.aphp.tumorotek.webapp.tree.code.CodeNode;
import fr.aphp.tumorotek.webapp.tree.code.CodeTreeitemRenderer;

/**
 * Component contenant le Tree permettant à l'utilisateur
 * de naviguer dans dans une codification.
 * Classe créée le 25/05/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class CodeBrowserComponent extends GenericForwardComposer<Component>
{

   private final Log log = LogFactory.getLog(CodeBrowserComponent.class);

   private static final long serialVersionUID = 1L;

   private TableCodage codification;

   private Boolean isOrgane;

   private Boolean isMorpho;

   private Tree mainTreeContext;

   private TumoTreeModel ttm;

   private Treeitem currentItem;

   private final CodeTreeitemRenderer renderer = new CodeTreeitemRenderer();

   public void setCodification(final TableCodage codif){
      this.codification = codif;
   }

   public void setIsOrgane(final Boolean isT){
      this.isOrgane = isT;
   }

   public void setIsMorpho(final Boolean isT){
      this.isMorpho = isT;
   }

   public TumoTreeModel getTtm(){
      return ttm;
   }

   /**
    * Renvoie le noeud selectionné dans le tree.
    * @return CodeNode noeud
    */
   public CodeNode getTreeSelectedValue(){
      if(mainTreeContext.getSelectedItem() != null){
         return (CodeNode) mainTreeContext.getSelectedItem().getValue();
      }
      return null;
   }

   /**
    * Renvoie le noeud selectionné dans le tree.
    * @return CodeNode noeud
    */
   public CodeNode getTreeSelectedValueAsParent(){
      if(mainTreeContext.getSelectedItem() != null){
         currentItem = mainTreeContext.getSelectedItem();
         final CodeNode c = (CodeNode) mainTreeContext.getSelectedItem().getValue();
         if(!c.isRoot()){
            return c;
         }
      }
      return null;
   }

   public CodeTreeitemRenderer getRenderer(){
      return renderer;
   }

   public Tree getMainTreeContext(){
      return mainTreeContext;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      getBinder().init(comp, false);
   }

   /**
    * Prepare la liste de CodeNode pour dessiner le Tree component.
    */
   public void populateCodeTreeModel(){

      final List<TumoTreeNode> children = new ArrayList<>();

      List<? extends CodeCommon> codes = new ArrayList<>();

      // root node
      final CodeNode root = new CodeNode();
      root.setCodification(codification);

      if("ADICAP".equals(codification.getNom())){
         final List<AdicapGroupe> dicos = ManagerLocator.getAdicapManager().findDictionnairesManager();
         if(isMorpho){ // D4-D5-D6-D7 UNIQUEMENT
            for(int i = 3; i < 7; i++){
               final CodeNode node = new CodeNode(dicos.get(i), codification);
               children.add(node);
            }
         }else if(isOrgane){ // D3 uniquement
            codes = ManagerLocator.getAdicapManager().findByAdicapGroupeManager(dicos.get(2));
            for(int i = 0; i < codes.size(); i++){
               final CodeNode node = new CodeNode(codes.get(i), codification, true);
               children.add(node);
            }
         }else{
            for(int i = 0; i < dicos.size(); i++){
               final CodeNode node = new CodeNode(dicos.get(i), codification);
               children.add(node);
            }
         }
         root.setChildren((ArrayList<TumoTreeNode>) children);
      }else if("CIM_MASTER".equals(codification.getNom())){
         codes = ManagerLocator.getCimMasterManager().findByCimParentManager(null);
         for(int i = 0; i < codes.size(); i++){
            final CodeNode node = new CodeNode(codes.get(i), codification, null);
            children.add(node);
         }
         root.setChildren((ArrayList<TumoTreeNode>) children);
      }else if("UTILISATEUR".equals(codification.getNom())){
         final CodeNode root2 = new CodeNode();
         final CodeDossier rootDos = new CodeDossier();
         rootDos.setNom("/");
         root2.setDossier(rootDos);
         root2.setRoot(true);
         final ArrayList<TumoTreeNode> rootChildren = new ArrayList<>();
         rootChildren.add(root2);
         root.setChildren(rootChildren);

         List<CodeDossier> dos = new ArrayList<>();

         if(!SessionUtils.getSelectedBanques(sessionScope).isEmpty()){
            dos = ManagerLocator.getCodeDossierManager()
               .findByRootDossierBanqueManager(SessionUtils.getSelectedBanques(sessionScope).get(0), false);

            codes = ManagerLocator.getCodeUtilisateurManager()
               .findByRootDossierManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
         }

         for(int i = 0; i < dos.size(); i++){
            final CodeNode node = new CodeNode(dos.get(i), codification);
            children.add(node);
         }
         for(int i = 0; i < codes.size(); i++){
            final CodeNode node = new CodeNode(codes.get(i), codification, null);
            children.add(node);
         }
         root2.setChildren((ArrayList<TumoTreeNode>) children);
      }else if("FAVORIS".equals(codification.getNom())){
         final CodeNode root2 = new CodeNode();
         final CodeDossier rootDos = new CodeDossier();
         rootDos.setNom("/");
         root2.setDossier(rootDos);
         root2.setRoot(true);
         final ArrayList<TumoTreeNode> rootChildren = new ArrayList<>();
         rootChildren.add(root2);
         root.setChildren(rootChildren);

         List<CodeDossier> dos;
         dos = ManagerLocator.getCodeDossierManager()
            .findByRootDossierBanqueManager(SessionUtils.getSelectedBanques(sessionScope).get(0), true);

         for(int i = 0; i < dos.size(); i++){
            final CodeNode node = new CodeNode(dos.get(i), codification);
            children.add(node);
         }
         codes = ManagerLocator.getCodeSelectManager()
            .findByRootDossierAndBanqueManager(SessionUtils.getSelectedBanques(sessionScope).get(0));

         for(int i = 0; i < codes.size(); i++){
            final CodeNode node = new CodeNode(codes.get(i), codification, null);
            children.add(node);
         }
         root2.setChildren((ArrayList<TumoTreeNode>) children);
      }

      ttm = new TumoTreeModel(root);
      if(mainTreeContext.getModel() != null){
         mainTreeContext.setModel(null);
      }
      mainTreeContext.setModel(ttm);
      // ouvre le root2 node par défaut
      if("UTILISATEUR".equals(codification.getNom()) || "FAVORIS".equals(codification.getNom())){
         final int[] apath = {0};
         ttm.addOpenPath(apath);
      }
   }

   /**
    * Envoie le code selectionné et la codification associé au controller
    * pour qu'ils soient utilisés en code et codification courantes.
    * N'envoie la selection que si le controller n'est pas en mode edition.
    * @param event
    */
   public void onSelect$mainTreeContext(final Event event){
      final CodeNode node = getTreeSelectedValue();

      if(node != null){
         if(!getCodeController().isInEdition()){
            if(node.getCode() != null){
               getCodeController().setCurrTable(codification);
               getCodeController().setCurrCode(node.getCode());
               getCodeController().switchLabelsToCodeOrDossier(true);
            }else if(node.getDossier() != null){
               if(!node.getDossier().getNom().equals("/")){
                  getCodeController().setCurrDossier(node.getDossier());
                  getCodeController().switchLabelsToCodeOrDossier(false);
               }else{
                  getCodeController().setCurrCode(null);
                  getCodeController().setCurrDossier(null);
                  getCodeController().switchLabelsToCodeOrDossier(true);
               }
            }
            currentItem = mainTreeContext.getSelectedItem();
         }
      }else{
         getCodeController().reloadCurrCodeOrDossier();
      }

   }

   private CodesController getCodeController(){
      return ((CodesController) self.getParent().getParent().getParent().getParent().getAttributeOrFellow("winCodes$composer",
         true));
   }

   private AnnotateDataBinder getBinder(){
      return getCodeController().getBinder();
   }

   public void reloadTree(final CodeNode selectedNode, final boolean isParent){
      // recree la liste si objet ajouté au premier niveau
      if(selectedNode == null){
         populateCodeTreeModel();
         getBinder().loadComponent(mainTreeContext);
      }else{
         try{
            currentItem.unload();
            if(isParent){
               mainTreeContext.renderItem(currentItem, selectedNode);
            }else{
               renderer.render(currentItem, selectedNode, 0);
            }
         }catch(final Exception e){
            log.error(e);
         }
      }
   }
}
