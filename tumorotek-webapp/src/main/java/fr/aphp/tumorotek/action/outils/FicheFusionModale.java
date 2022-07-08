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
package fr.aphp.tumorotek.action.outils;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Label;

import fr.aphp.tumorotek.action.constraints.ConstWord;

/**
 * Modale permettant de valider la fusion de patients en rédigeant un
 * commentaire.
 * @author Pierre Ventadour.
 *
 */
public class FicheFusionModale extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = -8520698021023338194L;

   private Component parent;

   private String comments;

   private Label warnLabel;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      final AnnotateDataBinder binder = new AnnotateDataBinder(comp);
      binder.loadComponent(comp);
   }

   /**
    * Initialise le composant à partir des paramètres d'affichage.
    */
   public void init(final String mess, final Component prt){
      warnLabel.setValue(mess);
      setParent(prt);
   }

   public void onClick$fusion(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));

      // réalise la suppression
      Events.postEvent("onFusionTriggered", getParent(), getComments());
   }

   public void onClick$cancel(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**********************************************************/
   /****************** GETTERS - SETTERS *********************/
   /**********************************************************/
   public String getComments(){
      return comments;
   }

   public void setComments(final String cs){
      this.comments = cs;
   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component pa){
      this.parent = pa;
   }

   private static ConstWord commentsConstraint = new ConstWord();
   {
      commentsConstraint.setNullable(true);
   }

   public ConstWord getCommentsConstraint(){
      return commentsConstraint;
   }

}
