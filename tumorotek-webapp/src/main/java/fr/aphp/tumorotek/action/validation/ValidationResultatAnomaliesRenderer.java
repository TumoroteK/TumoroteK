package fr.aphp.tumorotek.action.validation;

import java.util.Map.Entry;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.action.utils.ChampUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.validation.CritereValidation;

public class ValidationResultatAnomaliesRenderer implements RowRenderer<Entry<CritereValidation, String>>
{

   @Override
   public void render(Row row, Entry<CritereValidation, String> anomalie, int index) throws Exception{

      CritereValidation critere = anomalie.getKey();
      
      Champ champRacine = ChampUtils.getChampRacine(critere.getChamp());
      
      new Label(ChampUtils.getEntiteLieeLibelle(champRacine)).setParent(row);
      new Label(ObjectTypesFormatters.getLabelForChamp(champRacine)).setParent(row);
      new Label(anomalie.getValue()).setParent(row);
      
   }

}
