
package fr.aphp.tumorotek.action.echantillon.bto;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

import fr.aphp.tumorotek.action.echantillon.FicheEchantillonEdit;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

public class FicheEchantillonEditBTO extends FicheEchantillonEdit
{

   /**Selim Trabelsi
    * */

   private static final long serialVersionUID = 1L;
   private Label validEchantillonLabel;
   private Button validateEchantillon;

   private final Echantillon echantillon = new Echantillon();

   public FicheEchantillonEditBTO(){
      super();
   }

   public Label getValidEchantillonLabel(){
      return validEchantillonLabel;
   }

   public void setValidEchantillonLabel(final Label validEchantillonLabel){
      this.validEchantillonLabel = validEchantillonLabel;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

   }

   public void onClick$validateEchantillon(){

      if(echantillon.getSterile() || sterileBox.isChecked()){

         //ManagerLocator.getEchantillonManager().updateEchantillon(echantillon);
      }
   }

   @Override
   public void setObject(final TKdataObject obj){
      super.setObject(obj);

      //echantillon  = (Echantillon) ManagerLocator.getEchantillonManager().findByIdManager(getObject().getEchantillonId()); 

   }

}
