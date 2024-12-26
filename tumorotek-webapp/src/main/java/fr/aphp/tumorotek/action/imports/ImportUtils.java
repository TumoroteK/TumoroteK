package fr.aphp.tumorotek.action.imports;

import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.io.export.*;
import fr.aphp.tumorotek.model.io.imports.*;
import fr.aphp.tumorotek.webapp.general.*;
import org.zkoss.util.resource.*;

public class ImportUtils
{
   public static String extractChamp(ImportColonne colonne, EContexte templateContexte){
      String champ = "";
      if(colonne.getChamp() != null){
         if(colonne.getChamp().getChampEntite() != null){
            if (templateContexte != EContexte.GATSBI || colonne.getChamp().getChampEntite().getId() != 20) {
               champ = getLabelForChampEntite(colonne.getChamp().getChampEntite());
            } else { // rendu date debut -> date de visite
               champ = Labels.getLabel("gatsbi.visite.date");
            }
         }else if(colonne.getChamp().getChampDelegue() != null){
            champ = Labels
               .getLabel(colonne.getChamp().getChampDelegue().getILNLabelForChampDelegue(templateContexte));
         }else{
            champ = colonne.getChamp().getChampAnnotation().getNom();
         }
      }else{ // subderive header
         if(colonne.getNom().equals("code.parent")){
            champ = Labels.getLabel("import.colonne.subderive.parent");
         }else if(colonne.getNom().equals("qte.transf")){
            champ = Labels.getLabel("import.colonne.subderive.qte.transf");
         }else if(colonne.getNom().equals("evt.date")){
            champ = Labels.getLabel("import.colonne.subderive.evt.date");
         }
      }
      return champ;

   }

   private static String getLabelForChampEntite(final ChampEntite champ){
      final StringBuffer iProperty = new StringBuffer();
      iProperty.append("Champ.");
      iProperty.append(champ.getEntite().getNom());
      iProperty.append(".");

      String champOk = "";
      // si le nom du champ finit par "Id", on le retire
      if(champ.getNom().endsWith("Id")){
         champOk = champ.getNom().substring(0, champ.getNom().length() - 2);
      }else{
         champOk = champ.getNom();
      }
      iProperty.append(champOk);

      // on ajoute la valeur du champ
      return Labels.getLabel(iProperty.toString());
   }
}
