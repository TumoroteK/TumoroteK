package fr.aphp.tumorotek.action.validation;

import java.util.Calendar;
import java.util.Map.Entry;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.action.utils.ChampUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.ValidationException;
import fr.aphp.tumorotek.manager.impl.validation.workflow.ResultatValidation.Cause;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.utils.Duree;
import fr.aphp.tumorotek.model.validation.CritereValidation;
import fr.aphp.tumorotek.utils.ConversionUtils;

public class ValidationResultatCauseRenderer implements RowRenderer<Entry<CritereValidation, Cause>>
{

   @Override
   /*
    * (non-Javadoc)
    * @see org.zkoss.zul.RowRenderer#render(org.zkoss.zul.Row, java.lang.Object, int)
    */
   public void render(final Row row, final Entry<CritereValidation, Cause> entry, final int index){

      final CritereValidation critere = entry.getKey();
      final Cause cause = entry.getValue();

      final Champ champRacine = ChampUtils.getChampRacine(critere.getChamp());
      final Champ champ = critere.getChamp();
      String datatype = ChampUtils.getChampDataType(champ).getType();
      
      if("calcule".equals(datatype)) {
         datatype = champ.getChampAnnotation().getChampCalcule().getDataType().getType();
      }

      final StringBuilder conditionSb = new StringBuilder(critere.getOperateur().getLabel() + " ");

      if(cause.getValeurReference() != null){

         final String valeurRefFormattee = formatterValeur(cause.getValeurReference(), datatype);
         conditionSb.append(valeurRefFormattee);

         if(critere.getChampRef() != null){

            final String nomChampRef = ChampUtils.getEntiteLieeLibelle(ChampUtils.getChampRacine(critere.getChampRef())) + "."
               + ObjectTypesFormatters.getLabelForChamp(critere.getChampRef());

            conditionSb.append(" (" + nomChampRef + ")");

         }

      }else{
         conditionSb.append("Non renseigné");
      }

      String valeurConstateeFormattee = "Non renseigné";

      if(cause.getValeurConstatee() != null){
         valeurConstateeFormattee = formatterValeur(cause.getValeurConstatee(), datatype);
      }

      new Label(ChampUtils.getEntiteLieeLibelle(champRacine)).setParent(row);
      new Label(ObjectTypesFormatters.getLabelForChamp(champ)).setParent(row);
      new Label(conditionSb.toString()).setParent(row);
      new Label(valeurConstateeFormattee).setParent(row);

   }

   /**
    * Formatte un objet pour l'affichage
    * @param valeur
    * @return
    */
   private String formatterValeur(final Object valeur, final String datatype){

      String valeurFormattee = null;

      if(valeur != null){

         switch(datatype){
            case "alphanum":
            case "thesaurus":
            case "texte":
               valeurFormattee = valeur.toString();
               break;
            case "num":
               Double dValeur = ConversionUtils.convertToDouble(valeur);
               valeurFormattee = ObjectTypesFormatters.numericFormatter(dValeur);
               break;
            case "boolean":
               Boolean bValeur = ConversionUtils.convertToBoolean(valeur);
               valeurFormattee = ObjectTypesFormatters.booleanLitteralFormatter(bValeur);
               break;
            case "date":
            case "datetime":
               Calendar cValeur = ConversionUtils.convertToCalendar(valeur);
               valeurFormattee = ObjectTypesFormatters.dateRenderer2(cValeur);
               break;
            case "duree":
               Duree drValeur = new Duree(Long.valueOf((String)valeur), Duree.SECONDE);
               valeurFormattee = ObjectTypesFormatters.formatDuree(drValeur);
               break;
            default:
               final String msg = "Datatype [" + datatype + "] non prévu pour la validation";
               throw new ValidationException(msg);
         }
         
      }

      return valeurFormattee;

   }

}
