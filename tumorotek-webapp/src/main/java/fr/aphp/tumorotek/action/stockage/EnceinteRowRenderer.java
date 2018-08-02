package fr.aphp.tumorotek.action.stockage;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.stockage.Enceinte;

public class EnceinteRowRenderer implements RowRenderer<Enceinte>
{

   private Long nbLibresTotaux = (long) 0;
   private Long nbOccupesTotaux = (long) 0;

   public EnceinteRowRenderer(){
      nbLibresTotaux = (long) 0;
      nbOccupesTotaux = (long) 0;
   }

   @Override
   public void render(final Row row, final Enceinte data, final int index) throws Exception{
      // si l'élément n'est pas null
      if(data != null){
         final Enceinte enc = data;

         // code
         final Label nomLabel = new Label(enc.getNom());
         nomLabel.setClass("formValue");
         nomLabel.setStyle("font-weight : bold;");
         nomLabel.setParent(row);

         // emplacements libres
         final Long nbEmplacementsLibres = ManagerLocator.getEnceinteManager().getNbEmplacementsLibresByPS(enc);
         nbLibresTotaux = nbLibresTotaux + nbEmplacementsLibres;
         final Label libreLabel = new Label(String.valueOf(nbEmplacementsLibres));
         libreLabel.setParent(row);

         // emplacements occupes
         final Long nbEmplacementsOccupes = ManagerLocator.getEnceinteManager().getNbEmplacementsOccupesByPS(enc);
         nbOccupesTotaux = nbOccupesTotaux + nbEmplacementsOccupes;
         final Label occupeLabel = new Label(String.valueOf(nbEmplacementsOccupes));
         occupeLabel.setParent(row);

         // total
         final Long total = nbEmplacementsLibres + nbEmplacementsOccupes;
         final Label totalLabel = new Label(String.valueOf(total));
         totalLabel.setParent(row);

         // pourcentage
         Label pourcLabel = null;
         if(total != 0){
            final Float puiss = nbEmplacementsOccupes.floatValue() * 100;
            final Float pourc = puiss / total.floatValue();
            pourcLabel = new Label(String.valueOf(ObjectTypesFormatters.floor(pourc, 2)) + "%");
         }else{
            pourcLabel = new Label("-");
         }
         pourcLabel.setParent(row);
      }else{
         // sinon on va afficher les données complètes du cont.
         // code
         final Label nomLabel = new Label(Labels.getLabel("conteneur.total"));
         nomLabel.setClass("formValue");
         nomLabel.setStyle("font-weight : bold;");
         nomLabel.setParent(row);

         // emplacements libres
         final Label libreLabel = new Label(String.valueOf(nbLibresTotaux));
         libreLabel.setParent(row);

         // emplacements occupes
         final Label occupeLabel = new Label(String.valueOf(nbOccupesTotaux));
         occupeLabel.setParent(row);

         // total
         final Long total = nbLibresTotaux + nbOccupesTotaux;
         final Label totalLabel = new Label(String.valueOf(total));
         totalLabel.setParent(row);

         // pourcentage
         Label pourcLabel = null;
         if(total != 0){
            final Float puiss = nbOccupesTotaux.floatValue() * 100;
            final Float pourc = puiss / total.floatValue();
            pourcLabel = new Label(String.valueOf(ObjectTypesFormatters.floor(pourc, 2)) + "%");
         }else{
            pourcLabel = new Label("-");
         }
         pourcLabel.setParent(row);
      }
   }

}
