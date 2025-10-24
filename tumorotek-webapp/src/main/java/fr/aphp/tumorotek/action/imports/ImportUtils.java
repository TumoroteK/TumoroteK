package fr.aphp.tumorotek.action.imports;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.EntiteDecoratorForOneToManyComponent;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.systeme.Entite;

public class ImportUtils
{
   public static String extractChamp(ImportColonne colonne, EContexte templateContexte){
      String champ = "";
      if(colonne.getChamp() != null){
         if(colonne.getChamp().getChampEntite() != null){
            if (templateContexte != EContexte.GATSBI || colonne.getChamp().getChampEntite().getId() != 20) {
               champ = ObjectTypesFormatters.getLabelForChampEntite(colonne.getChamp().getChampEntite());
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

   //TK-538 :
   public static void addEntiteDecoratorToEntitesAssociees(EntiteDecoratorForOneToManyComponent entiteDecorator, List<EntiteDecoratorForOneToManyComponent> listEntiteDecorator, String pathToRespond) {
      if(listEntiteDecorator != null && entiteDecorator != null) {
         listEntiteDecorator.add(entiteDecorator);
      
         if(Path.getComponent(pathToRespond) != null){
            Events.postEvent(new Event("onGetAddedObject", Path.getComponent(pathToRespond), entiteDecorator));
         }
      }
   }
   public static void removeEntiteDecoratorFromEntitesAssociees(EntiteDecoratorForOneToManyComponent entiteDecorator,List<EntiteDecoratorForOneToManyComponent> listEntiteDecorator, String pathToRespond) {
      if(listEntiteDecorator != null && entiteDecorator != null) {
         listEntiteDecorator.remove(entiteDecorator);
         if(Path.getComponent(pathToRespond) != null){
            Events.postEvent(new Event("onGetRemovedObject", Path.getComponent(pathToRespond), entiteDecorator));
         }
      }
   } 
   /**
    * retourne tous les champs d'annotation pouvant être importés. Les champs calculés sont donc exclus
    * @param entite
    * @param banque
    * @return
    */
   public static List<Champ> retrieveAllChampAnnotationForEntite(Entite entite, Banque banque) {
      List<Champ> result = new ArrayList<Champ>();
      
      List<TableAnnotation> listTableAnnotation = ManagerLocator.getTableAnnotationManager()
         .findByEntiteAndBanqueManager(entite, banque);
      List<ChampAnnotation> listChampAnnotation = new ArrayList<>();
      int nbTableAnnotation = listTableAnnotation.size();
      for(int i = 0; i < nbTableAnnotation; i++){
         listChampAnnotation.addAll(ManagerLocator.getChampAnnotationManager().findByTableManager(listTableAnnotation.get(i)));
      }
      // les champs calculés ne sont pas concernés par l'import
      for(int i = 0; i < listChampAnnotation.size(); i++){
         if(!"calcule".equals(listChampAnnotation.get(i).getDataType().getType())){
            result.add(new Champ(listChampAnnotation.get(i)));
         }
      }
      
      return result;
   }

}
