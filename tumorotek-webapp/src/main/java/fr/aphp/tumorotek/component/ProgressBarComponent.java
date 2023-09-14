package fr.aphp.tumorotek.component;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;

import fr.aphp.tumorotek.webapp.general.export.Export;

public class ProgressBarComponent extends GenericForwardComposer<Component>
{

   /**
    * Barre de progression
    *
    */
   private static final long serialVersionUID = 6995294807796646517L;

   private Progressmeter progressBar;

   private Label curr_step;

   private Label curr_;

   private Label type_;

   private Label total_;

   private Label errmes;

   private Export exportThread;

   private String entite = "";

   private String action = "";

   private String counter = "";

   @Override
   public void doAfterCompose(final Component c) throws Exception{
      super.doAfterCompose(c);
   }

   /**
    * Update ProgressBarComponent value
    * @param v current value
    */
   public void setValue(final int v){
      progressBar.setValue(v);
      curr_step.setValue(String.valueOf(v));
   }

   /**
    *
    * @param v
    * @param count
    * @param libelle action
    * @param libelle entite
    */
   public void setDetail(final Integer v, final Integer count, final String action, final String entite, final Exception e){
      if(e == null){
         //			if (v != null) {
         //				curr_.setValue(String.valueOf(v) + " / ");
         //			} else {
         //				curr_.setValue("");
         //			}
         //			if (count != null && v != null) {
         //				total_.setValue(String.valueOf(v) + " / " + String.valueOf(count));
         //			} else {
         //				total_.setValue("");
         //			}
         if(action != null){
            setAction(action);
         }
         if(entite != null){
            setEntite(entite);
         }
         if(count != null && v != null){
            setCounter(String.valueOf(v) + " / " + String.valueOf(count));
         }else{
            setCounter(null);
         }
         // changement action
         if(getAction() != null){
            type_.setValue(Labels.getLabel(getAction()) + (getCounter() != null ? " : " + getCounter() : "")
               + (getEntite() != null && !getEntite().equals("") ? " " + Labels.getLabel(getEntite()) : ""));
         }
      }else{
         curr_.setVisible(false);
         total_.setVisible(false);
         type_.setVisible(false);
         errmes.setValue(e.getMessage());
         errmes.setVisible(true);
      }
   }

   public void onClick$cancel(){
      exportThread.interrupt();
      //		if (exportThread.isAlive()
      //			&& !exportThread.isInterrupted()) {
      // exportThread.closeFromBar();
      //}
      exportThread.getProgressBarComponent().getParent().detach();
   }

   public boolean isError(){
      return errmes.isVisible();
   }

   public void setExportThread(final Export exportThread){
      this.exportThread = exportThread;
   }

   public String getEntite(){
      return entite;
   }

   public void setEntite(final String entite){
      this.entite = entite;
   }

   public String getCounter(){
      return counter;
   }

   public void setCounter(final String counter){
      this.counter = counter;
   }

   public String getAction(){
      return action;
   }

   public void setAction(final String action){
      this.action = action;
   }
}
