package fr.aphp.tumorotek.action.cession.bto;

import org.zkoss.zul.Div;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.cession.FicheCessionEdit;

/**
 * @version 2.2.0
 */
public class FicheCessionEditBTO extends FicheCessionEdit
{
   private Div arriveeDate;
   private Row rowDemandeur;

   /**
    * Méthode affichant le formulaire du mode sanitaire.
    */
   @Override
   public void switchToSanitaireMode(){
      super.switchToSanitaireMode();
      setVisibleRow(rowDemandeur, true);
   }

   /**
    * Méthode affichant le formulaire du mode recherche.
    */
   @Override
   public void switchToRechercheMode(){
      super.switchToRechercheMode();
      setVisibleRow(rowDemandeur, true);
   }

   /**
    * Méthode affichant le formulaire du mode destruction.
    */
   @Override
   public void switchToDestructionMode(){
      super.switchToDestructionMode();
      setVisibleRow(rowDemandeur, true);
   }

   public void switchToImplantationMode(){
      setVisibleRow(rowSanitaire, false);
      setVisibleRow(rowRecherche1, false);
      setVisibleRow(rowRecherche2, false);
      setVisibleRow(rowDestruction, false);
      setVisibleRow(rowStatutDestruction, false);

      setVisibleRow(rowDescription, false);
      setVisibleRow(rowDestinataire, true);
      setVisibleRow(rowService, true);
      setVisibleRow(rowEtablissement, true);
      setVisibleRow(rowSeparator1, true);
      setVisibleRow(rowLine1, true);
      setVisibleRow(rowSeparator2, true);
      setVisibleRow(rowDateAndStatut, true);
      setVisibleRow(rowLine2, true);
      setVisibleRow(rowExecutant, true);
      setVisibleRow(rowDates, true);
      setVisibleRow(rowTransporteurAndTemp, true);
      setVisibleRow(rowSeparator3, false);
      setVisibleRow(rowDemandeur, false);
   }

   /**
    * Méthode appelée lorsque l'utilisateur sélectionne un type de cession.
    * Le formulaire changera en fonction du type choisi.
    */
   @Override
   public void onSelect$typesBox(){
      final int ind = typesBox.getSelectedIndex();

      if(ind > -1){
         selectedCessionType = types.get(ind);
      }

      if(selectedCessionType.getType().toUpperCase().equals("IMPLANTATION")){
         switchToImplantationMode();
         arriveeDate.setVisible(false);
      }
      super.onSelect$typesBox();
   }

   /**
    * En fonction du type de cession à afficher, certains champs sont masqués
    *
    * @param switchCondition type de cession : SANITAIRE, RECHERCHE, DESTRUCTION ou IMPLANTATION
    * @since 2.2.0
    */
   @Override
   public void switchToCessionTypeToDisplay(final String switchCondition){
      super.switchToCessionTypeToDisplay(switchCondition);
      switch(switchCondition){
         case "IMPLANTATION":
            switchToImplantationMode();
            arriveeDate.setVisible(false);
            break;
      }
   }
}
