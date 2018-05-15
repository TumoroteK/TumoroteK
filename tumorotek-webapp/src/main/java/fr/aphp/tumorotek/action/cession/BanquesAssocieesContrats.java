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
package fr.aphp.tumorotek.action.cession;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.component.OneToManyComponent;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class BanquesAssocieesContrats extends OneToManyComponent<Banque>
{

   private static final long serialVersionUID = 1L;

   private List<Banque> objects = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      objLinkLabel = new Label();
      super.doAfterCompose(comp);
   }

   @Override
   public List<Banque> getObjects(){
      return this.objects;
   }

   
   @Override
   public void setObjects(final List<Banque> objs){
      this.objects = objs;
      updateComponent();
   }

   /**
    * Surcharge pour imposer que la dernière banque ne puisse jamais
    * être éffacée laissant une table orpheline.
    */
   @Override
   public void updateComponent(){
      if(deleteHeader.isVisible()){
         deleteHeader.setVisible(getObjects().size() > 1);
      }
      super.updateComponent();
   }

   @Override
   public void addToListObjects(final Banque obj){
      getObjects().add(obj);
   }

   @Override
   public void removeFromListObjects(final Object obj){
      getObjects().remove(obj);
   }

   @Override
   public String getGroupHeaderValue(){
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("contrat.table.banques"));
      sb.append(" (");
      sb.append(getObjects().size());
      sb.append(")");
      return sb.toString();
   }

   @Override
   public List<Banque> findObjectsAddable(){
      // Banques ajoutables
      final List<Banque> banks = ManagerLocator.getBanqueManager()
         .findByUtilisateurIsAdminManager(SessionUtils.getLoggedUser(sessionScope), SessionUtils.getPlateforme(sessionScope));
      // retire les TableCodages deja assignés
      for(int i = 0; i < getObjects().size(); i++){
         banks.remove(getObjects().get(i));
      }
      return banks;
   }

   @Override
   public void drawActionForComponent(){}

   @Override
   public void onClick$objLinkLabel(final Event event){}

   /**
    * Ajout warning.
    */
   @Override
   public void onClick$deleteImage(final Event event){
      if(Messagebox.show(Labels.getLabel("contrat.banque.remove.warning"), Labels.getLabel("general.warning"),
         Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
         super.onClick$deleteImage(event);
         return;
      }

   }

   /**
    * Surcharge pour ne pas appliquer clear sur la liste.
    */
   @Override
   public void switchToCreateMode(){
      objects = new ArrayList<>();
      getBinder().loadComponent(objectsList);
      switchToEditMode(true);
   }

   /**
    * Surcharge pour imposer que la dernière banque ne puisse jamais
    * être éffacée laissant une table orpheline.
    */
   @Override
   public void switchToEditMode(final boolean b){
      super.switchToEditMode(b);
      deleteHeader.setVisible(getObjects().size() > 1);
   }
}
