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
package fr.aphp.tumorotek.modales;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Composition;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

/**
 * @author Mathieu BARTHELEMY
 * @version 2.30-gatsbi
 */
public class DateModale implements Serializable
{

   private static final long serialVersionUID = 1L;

   private static final Date DEFAULT_VALUE = Date.from(Instant.now());
   
   @Wire("#fwinDateModale")
   private Window fwinDateModale;
   
   @Wire("#cancelBtn")
   private Button cancelBtn;
   
   private String title;
   private String label;
   private Date value;
   private Boolean closable = true;
   
   /**
    * Affiche de la popup de choix d'une date
    * @param defaultValue valeur par défaut à afficher dans la popup
    * @param onCloseListener listener de fermeture de la popup
    */
   public static void show(final String titre, final String label, final Date defaultValue, 
                                                   final boolean closable, final Component parent)
   {

      final Map<String, Object> args = new HashMap<>();
      args.put("title", titre);
      args.put("label", label);
      args.put("defaultValue", Optional.ofNullable(defaultValue).orElse(DEFAULT_VALUE));
      args.put("closable", closable);

      Executions.getCurrent().setAttribute(Composition.PARENT, null);
      Executions.createComponents("/zuls/modales/DateModale.zul", parent, args);
   }
   
   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
      
      fwinDateModale.setClosable(closable);
      fwinDateModale.setId("FromDateWindow");
      fwinDateModale.setMaximizable(true);
      fwinDateModale.setSizable(true);
      fwinDateModale.setTitle(title);
      fwinDateModale.setBorder("normal");
      fwinDateModale.setWidth("280px");
      fwinDateModale.setPosition("center, center");

      cancelBtn.setVisible(closable);
   }

   @Init
   public void init(@ExecutionArgParam("title") final String _t, @ExecutionArgParam("label") final String _l,
      @ExecutionArgParam("defaultValue") final Date _v, @ExecutionArgParam("closable") final Boolean _c){
      
      setLabel(_l);
      setValue(_v);
      closable = _c;
      title = _t;
   }

   public Date getValue(){
      return value;
   }

   public void setValue(Date value){
      this.value = value;
   }

   public String getLabel(){
      return label;
   }

   public void setLabel(String _l){
      this.label = _l;
   }

   @Command
   public void validate(){
      cancel();
   }

   @Command
   public void cancel(){
      Events.postEvent("onClose", fwinDateModale, null);
      Events.postEvent("onFromDateProvided", fwinDateModale.getParent(), getValue());
   }
}