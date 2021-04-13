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
package fr.aphp.tumorotek.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Composition;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * @author GCH
 */
public class TexteModale extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = -1418399743820656530L;

   private Label textboxLabel;
   private Textbox textbox;
   private Button cancel;

   /**
    * Affiche une modale permettant de saisir un texte
    * @param titre titre de la modale
    * @param label libellé du champ texte
    * @param defaultValue valeur par défaut du champ texte
    * @param nbLignes nombre de ligne du champ texte
    * @param closable indique si la modale peut être fermée autrement que par le bouton valider
    * @param onCloseListener listener de fermeture de la modale
    */
   public static void show(final String titre, final String label, final String defaultValue, final int nbLignes,
      final boolean closable, final EventListener<Event> onCloseListener){

      final Map<String, Object> args = new HashMap<>();
      args.put("title", titre);
      args.put("closable", closable);
      args.put("label", label);
      args.put("nbLignes", nbLignes);
      args.put("defaultValue", Optional.ofNullable(defaultValue).orElse(""));

      Executions.getCurrent().setAttribute(Composition.PARENT, null);
      Component parent = Executions.createComponents("/zuls/modales/TexteModale.zul", null, args);

      while(parent != null && !(parent instanceof Window)){
         parent = parent.getParent();
      }

      if(parent instanceof Window){
         final Window window = (Window) parent;
         if(onCloseListener != null){
            window.addEventListener(Events.ON_CLOSE, onCloseListener);
         }
         window.doModal();
      }

   }

   @Override
   public void doAfterCompose(Component comp) throws Exception{

      super.doAfterCompose(comp);

      final Window window = (Window) comp;
      final boolean closable = (Boolean)arg.get("closable");

      window.setVisible(false);
      window.setClosable(closable);
      window.setId("textModaleWindow");
      window.setPage(page);
      window.setMaximizable(true);
      window.setSizable(true);
      window.setTitle((String) arg.get("title"));
      window.setBorder("normal");
      window.setWidth("500px");
      window.setPosition("center, top");

      cancel.setVisible(closable);
      
      textboxLabel.setValue((String) arg.get("label"));
      textbox.setRows((Integer)arg.get("nbLignes"));
      textbox.setValue(String.valueOf(arg.get("defaultValue")));

   }

   /**
    * Mets la valeur saisie dans les data de l'event et ferme la popup
    */
   public void onClick$validate(){

      final String valeurSaisie =
         textbox.getValue() != null ? textbox.getValue() : (String)arg.get("defaultValue");
      Events.postEvent(Events.ON_CLOSE, self.getRoot(), valeurSaisie);

   }

   /**
    * Ferme la modale sans renvoyer de données
    */
   public void onClick$cancel(){
      Events.postEvent(Events.ON_CLOSE, self.getRoot(), null);
   }
   
}
