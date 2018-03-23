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
package fr.aphp.tumorotek.action.contexte;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.SimpleListModel;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.component.OneToManyComponent;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class AnnoTablesAssociees extends OneToManyComponent
{

   private static final long serialVersionUID = 1L;

   private Listheader upHeader;
   private Listheader downHeader;

   private Entite entite;
   private List<TableAnnotation> objects = new ArrayList<>();

   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite e){
      this.entite = e;
   }

   @Override
   public List<TableAnnotation> getObjects(){
      return this.objects;
   }

   @Override
   public void updateComponent(){
      getGroupHeader().setLabel(getGroupHeaderValue());
      objectsList.setModel(new SimpleListModel<Object>(getObjects()));
      getBinder().loadComponent(objectsList);
   }

   
   @Override
   public void setObjects(final List<? extends Object> objs){
      this.objects = (List<TableAnnotation>) objs;
      updateComponent();
   }

   @Override
   public void addToListObjects(final Object obj){
      getObjects().add((TableAnnotation) obj);
   }

   @Override
   public void removeFromListObjects(final Object obj){
      getObjects().remove(obj);
   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode();
      upHeader.setVisible(false);
      downHeader.setVisible(false);
   }

   @Override
   public void switchToEditMode(final boolean b){
      super.switchToEditMode(b);
      upHeader.setVisible(true);
      downHeader.setVisible(true);
   }

   /**
    * Lien vers la fiche detaillee de l'objet.
    */
   @Override
   public void onClick$objLinkLabel(final Event event){
      //if (objLinkLabel.getSclass().equals("formLink")) {
      // recupere le TableAnnotation
      //			Object tab = AbstractListeController2
      //							.getBindingData((ForwardEvent) event, false); 

      // ouvre l'onglet TableAnnotation;
      // StockageController controller = 
      //	StockageController.backToMe(getMainWindow(), page);
      // controller
      //	.switchToFicheTableAnnotationMode((TableAnnotation) cont);
      //	}
   }

   @Override
   public String getGroupHeaderValue(){
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("Champ.Banque.Annotations"));
      sb.append(" ");
      sb.append(Labels.getLabel("Entite." + getEntite().getNom()));
      sb.append(" (");
      sb.append(getObjects().size());
      sb.append(")");
      return sb.toString();
   }

   @Override
   public List<? extends Object> findObjectsAddable(){
      // TableAnnotations ajoutables
      final List<TableAnnotation> tabs = ManagerLocator.getTableAnnotationManager().findByEntiteAndPlateformeManager(getEntite(),
         SessionUtils.getPlateforme(sessionScope));
      // retire les TableAnnotations deja assignés
      for(int i = 0; i < getObjects().size(); i++){
         tabs.remove(getObjects().get(i));
      }
      return tabs;
   }

   @Override
   public void drawActionForComponent(){
      //		addObj
      //			.setDisabled(drawActionOnOneButton(nomEntite, nomOperation));
      //
      //		List<String> entites = new ArrayList<String>();
      //		entites.add("Stockage");
      //		setDroitsConsultation(drawConsultationLinks(entites));
      //
      //		// si pas le droit d'accès aux TableAnnotations, on cache le lien
      //		if (!getDroitsConsultation().get("Stockage")) {
      //			objLinkLabel.setSclass("formValue");
      //		} else {
      //			objLinkLabel.setSclass("formLink");
      //		}	
   }

   /**
    * Monte la table selectionnée dans l'ordre de la liste.
    * @param event
    */
   public void onClick$upImage(final Event event){
      // recupere le TableAnnotation
      final Object tab = AbstractListeController2.getBindingData((ForwardEvent) event, false);

      upObject(tab);
      updateComponent();
   }

   /**
    * Effectue l'operation de mouvements des objets au sein de la liste.
    * @param objet a monter d'un cran
    */
   private void upObject(final Object obj){
      final int tabIndex = getObjects().indexOf(obj);
      Object supObjectInList = null;
      if(tabIndex - 1 > -1){
         supObjectInList = getObjects().get(tabIndex - 1);
         getObjects().set(tabIndex, (TableAnnotation) supObjectInList);
         getObjects().set(tabIndex - 1, (TableAnnotation) obj);
      }
   }

   /**
    * Descend la table selectionnée dans l'ordre de la liste.
    * @param event
    */
   public void onClick$downImage(final Event event){
      // recupere le TableAnnotation
      final Object tab = AbstractListeController2.getBindingData((ForwardEvent) event, false);
      final int tabIndex = getObjects().indexOf(tab);
      if(tabIndex + 1 < getObjects().size()){
         upObject(getObjects().get(tabIndex + 1));
      }
      updateComponent();
   }
}
