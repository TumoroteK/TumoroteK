package fr.aphp.tumorotek.action.prelevement.bto;

import fr.aphp.tumorotek.action.prelevement.FichePrelevementStatic;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;

public class FichePrelevementStaticBTO extends FichePrelevementStatic
{
   public String getDatePeremptionFormated(){
      if(this.prelevement != null){
         return ObjectTypesFormatters.dateRenderer2(this.prelevement.getDatePeremption());
      }else{
         return null;
      }
   }
}
