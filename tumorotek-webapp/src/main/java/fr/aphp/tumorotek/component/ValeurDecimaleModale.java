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
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 * @author GCH
 */
public class ValeurDecimaleModale extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = -1418399743820656530L;
   private static final Float DEFAULT_VALUE = 0f;

   private Label decimalBoxLabel;
   private Decimalbox decimalBox;
   private Button cancelBtn;

   /**
    * Affiche de la popup d'assignation de température
    * @param defaultValue valeur par défaut à afficher dans la popup
    * @param onCloseListener listener de fermeture de la popup
    */
   public static void show(final String titre, final String label, final Float defaultValue, boolean closable,
      final EventListener<Event> onCloseListener){

      final Map<String, Object> args = new HashMap<>();
      args.put("title", titre);
      args.put("label", label);
      args.put("defaultValue", Optional.ofNullable(defaultValue).orElse(DEFAULT_VALUE));
      args.put("closable", closable);
      

      Executions.getCurrent().setAttribute(Composition.PARENT, null);
      Component parent = Executions.createComponents("/zuls/modales/ValeurDecimaleModale.zul", null, args);

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
      boolean closable = (Boolean)arg.get("closable");
      
      window.setVisible(false);
      window.setClosable(closable);
      window.setId("temperatureEventStockageWindow");
      window.setPage(page);
      window.setMaximizable(true);
      window.setSizable(true);
      window.setTitle((String) arg.get("title"));
      window.setBorder("normal");
      window.setWidth("280px");
      window.setPosition("center, top");

      cancelBtn.setVisible(closable);
      
      decimalBoxLabel.setValue((String) arg.get("label"));
      decimalBox.setValue(String.valueOf(arg.get("defaultValue")));

   }

   /**
    * Mets la valeur saisie dans les data de l'event et ferme la popup
    */
   public void onClick$validate(){

      final Float valeurSaisie =
         decimalBox.getValue() != null ? decimalBox.getValue().floatValue() : (Float) arg.get("defaultValue");
      Events.postEvent(Events.ON_CLOSE, self.getRoot(), valeurSaisie);

   }

   /**
    * Ferme la modale sans renvoyer de données
    */
   public void onClick$cancel(){
      Events.postEvent(Events.ON_CLOSE, self.getRoot(), null);
   }
   
}
