/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.Arrays;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Column;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.ContexteType;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.gatsbi.RowRendererGatsbiOtherConsultBanks;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Gatsbi controller regroupant les fonctionalités de modification dynamique de
 * l'interface spécifique aux prélèvements
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class GatsbiControllerPrelevement {
   
   /***
    * Utilise un FLAG congColRendered passe à true si la colonne congelation est déja rendue
    * afin d'éviter que cette colonne soit rendue deux fois
    * @param chpId
    * @param grid
    * @param congColRendered flag 
    * @return true si c'est une colonne congelation qui a été rendue
    * @throws ClassNotFoundException
    * @throws InstantiationException
    * @throws IllegalAccessException
    */
   public static boolean addColumnForChpIdAndReturnsIfCongColRendered(final Integer chpId, 
      final Grid grid, final boolean congColRendered)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      switch(chpId){
         case 23:
            // "code" toujours rendu par défaut
            break;
         case 45: // numero labo
            drawNumeroLaboColumn(grid);
            break;
         case 24: // nature
            drawNatureColumn(grid);
            break;
         case 44: // nda
            drawPatientNdaColumn(grid);
            break;
         case 30: // date prelevement
            drawDatePrelColumn(grid);
            break;
         case 31: // prelevement type
            drawPrelevementTypeColumn(grid);
            break;
         case 47: // sterile
            drawSterileColumn(grid);
            break;
         case 249: // risques -> rendu sous la forme d'une icône
            break;
         case 29: // service preleveur
            drawServicePreleveurColumn(grid);
            break;
         case 28: // preleveur
            drawPreleveurColumn(grid);
            break;
         case 32: // condit type
            drawConditTypeColumn(grid);
            break;
         case 34: // condit Nbr
            drawConditNbrColumn(grid);
            break;
         case 33: // condit milieu
            drawConditMilieuColumn(grid);
            break;
         case 26: // consent type
            drawConsentTypeColumn(grid);
            break;
         case 27: // consent date
            drawConsentDateColumn(grid);
            break;
         case 35: // date depart
            drawDateDepartColumn(grid);
            break;
         case 36: // transporteur
            drawTransporteurColumn(grid);
            break;
         case 37: // transport temperature
              drawTransportTempColumn(grid);
            break;
         case 269: // cong depart
            if(!congColRendered){
               drawCongColumn(grid);
               return true;
            }
            break;
         case 38: // date arrivee
            drawDateArriveeColumn(grid);
            break;
         case 39: // operateur
            drawOperateurColumn(grid);
            break;
         case 40: // quantite
            drawQuantiteColumn(grid);
            break;
         case 270: // cong arrivee
            if(!congColRendered){
               drawCongColumn(grid);
               return true;
            }
            break;
         case 256: // conforme arrivee -> rendu sous la forme d'une icône (chpId=257 raisons, ignore)
            break;
         default:
            break;
      }
      
      // ce n'est pas une colonne congélation qui a été rendue
      return false;
   }
   

   public static void applyPatientContext(Component groupPatient, boolean edit) {
      
      List<Div> items = GatsbiController.wireItemDivsFromMainComponent(ContexteType.PATIENT, groupPatient);
      GatsbiController.showOrhideItems(items, null, SessionUtils.getCurrentGatsbiContexteForEntiteId(1));
      
      Div ndaDiv = (Div) groupPatient.getFellowIfAny("ndaDiv");      
      if (ndaDiv != null && ndaDiv.isVisible()) {
         // identifiantDiv partage la ligne avec ndaDiv
         ((Div) ndaDiv.getPreviousSibling()).setSclass("item item-mid");
      }
   }
   
   public static void applyPatientNdaRequired(Div ndaDiv) {  
      if (ndaDiv.isVisible()) {
         if (SessionUtils.getCurrentGatsbiContexteForEntiteId(2).isChampIdRequired(44)) {
            ((ConstCode) ((Textbox) ndaDiv.getLastChild()).getConstraint()).setNullable(false);         
            applyPatientNdaRequiredLabel(ndaDiv);
         } else {
            removePatientNdaRequired(ndaDiv);
         }
      }
   }
   
   public static void applyPatientNdaRequiredLabel(Div ndaDiv) { 
      if (ndaDiv.isVisible() && SessionUtils.getCurrentGatsbiContexteForEntiteId(2).isChampIdRequired(44)
           && !ndaDiv.getSclass().contains("item-required")) {
           ndaDiv.setSclass(ndaDiv.getSclass().concat(" item-required"));
      }
   }
   
   public static void removePatientNdaRequired(Div ndaDiv) {  
      ((ConstCode) ((Textbox) ndaDiv.getLastChild()).getConstraint()).setNullable(true);
   }
   
   public static boolean isSitesIntermPageDisplayed(Contexte contexte) {
      
      if (contexte == null) {
         return true;
      }
      
      if (contexte.getSiteIntermediaire()) {
         return true;
      }
      
      // vérifie si au moins un des champs du formulaire est à afficher
      final boolean oneDivVisible = contexte.getChampEntites().stream()
         .filter(c -> Arrays.asList(35, 36, 37, 38, 39, 40, 256, 267, 268).contains(c.getChampEntiteId()))
         .anyMatch(c -> c.getVisible());

      
      return oneDivVisible;
   }
   
   /**
    * Dessine les colonnes de la grid prelevement et des inner lists de l'onglet Echantillon (échantillons
    * en création dans FicheMultiEchantillon) et de l'onglet Prélèvement.
    * @param contexte
    * @param grid
    * @param rowRenderer
    * @param deletable si true, affiche une dernière colonne avec un bouton delete
    * @param forceEmplacementAndStatut si true, affiche obligatoire les deux colonnes, à la fin.
    * @throws ClassNotFoundException
    * @throws InstantiationException
    * @throws IllegalAccessException
    */
   public static void drawColumnsForPrelevements(final Contexte contexte, final Grid grid, final RowRendererGatsbiOtherConsultBanks rowRenderer,
      final boolean showBank, String nbEchanColId)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{

      // icones column, visible si non conformites OU risque est visible
      if(contexte.isChampIdVisible(249) || contexte.isChampIdVisible(256)){

         int colsize = 0;
         if(contexte.isChampIdVisible(249)){
            colsize = colsize + 35;
         }
         if(contexte.isChampIdVisible(256)){
            colsize = colsize + 35;
         }

         GatsbiController.addColumn(grid, null, String.valueOf(colsize).concat("px"), "center", null, null, true);

         // indique au row renderer qu'il doit dessiner les icones
         rowRenderer.setIconesRendered(true);
      }

      // bug TG-228 : mise en commentaire du code qui pose problème pour annuler la spécificité souhaité
      // avant de revoir tout le mécanisme de génération des tableaux (règles de gestions sur les colonne et les lignes
      // définies une seule fois) : tâche TG-230.
      // date prelevement optionnel mais si demandé doit être avant le code prélèvement dans le cas des inners :
      // /!\ date de prélèvement c'est 30 et non 29 comme renseigné au moment la TG-228 avant la TG (29 c'est service préleveur)
      // => passer par des constantes : tâche TG-229
      // reprendre la règle de gestion spécifique avec la TG-231 dont TG-230 est le prérequis
      //if (contexte.getChampEntiteInTableauOrdered().contains(30)) { 
      //   GatsbiControllerPrelevement.drawDatePrelColumn(grid);
      //}
      
      // code echan column, toujours affichée
      GatsbiControllerPrelevement.drawCodeColumn(grid);
      
      // ttes collection / other banks
      if (showBank) { 
         GatsbiControllerPrelevement.drawBanqueColumn(grid, showBank);
      }

      // variable columns
      boolean congColRendered = false;
      for(final Integer chpId : contexte.getChampEntiteInTableauOrdered()){
         congColRendered = GatsbiControllerPrelevement
            .addColumnForChpIdAndReturnsIfCongColRendered(chpId, grid, congColRendered);
      }
    
      // nb echantillons total / restants, toujours affichés
      drawEchanNbColumn(grid, nbEchanColId);
   }
   
   // code, colonne toujours affichée
   public static Column drawCodeColumn(final Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
      return GatsbiController.addColumn(grid, "general.code", null, null, null, "auto(code)", true);
   }
   
   public static Column drawDatePrelColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "prelevement.datePrelevementCourt", null, null, null,
      "auto(datePrelevement)", true);
   }
   
   public static Column drawBanqueColumn(final Grid grid, final boolean visible)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Entite.Banque", null, null, null, "auto(code)", visible);
   }
   
   private static Column drawNumeroLaboColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.NumeroLabo", null, null, null, "auto(numeroLabo)",
         true);      
   }
   
   private static Column drawNatureColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.Nature.Nature", null, null, null, "auto(nature.nom)",
      true);
   }
   
   private static Column drawPatientNdaColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.PatientNda", null, null, null, "auto(patientNda)",
      true);
   }
   
   private static Column drawPrelevementTypeColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.PrelevementType.Type", null, null, null,
      "auto(prelevementType.nom)", true);
   }

   private static Column drawSterileColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.Sterile", null, null, null, "auto(sterile)", true);
   }

   private static Column drawServicePreleveurColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.ServicePreleveur", null, null, null,
         "auto(servicePreleveur.nom)", true);
   }
   
   private static Column drawPreleveurColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.Preleveur", null, null, null,
         "auto(preleveur.nomAndPrenom)", true);
   }
   
   private static Column drawConditTypeColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.ConditType.Type", null, null, null,
         "auto(conditType.nom)", true);
   }
   
   private static Column drawConditNbrColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.ConditNbr", null, null, null, "auto(conditNbr)", true);
   }
   
   private static Column drawConditMilieuColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.ConditMilieu", null, null, null,
         "auto(conditMilieu.nom)", true);
   }
   
   private static Column drawConsentTypeColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.ConsentType", null, null, null,
         "auto(consentType.nom)", true);
   }
   
   private static Column drawConsentDateColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.ConsentDate", null, null, null, "auto(consentDate)",
         true);
   }
   
   private static Column drawDateDepartColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.DateDepart", null, null, null, "auto(dateDepart)",
         true);
   }
   
   private static Column drawTransporteurColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.Transporteur", null, null, null,
         "auto(transporteur.nom)", true);
   }
   
   private static Column drawTransportTempColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.TransportTemp", null, null, null,
         "auto(transportTemp)", true);
   }
   
   private static Column drawCongColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "prelevement.cong", null, null, null, "auto(transportTemp)", true);
   }
   
   private static Column drawDateArriveeColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.DateArrivee", null, null, null, "auto(dateArrivee)",
         true);
   }
   
   private static Column drawOperateurColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.Operateur", null, null, null, "auto(operateur.nom)",
         true);
   }
   
   private static Column drawQuantiteColumn(Grid grid) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Prelevement.Quantite", null, null, null, "auto(quantite)", true);
   }
   
   public static Column drawEchanNbColumn(Grid grid, String id) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
      final Vbox vbox = new Vbox();
      vbox.setAlign("center");
      final Label nbEch = new Label(Labels.getLabel("prelevement.nbEchantillons"));
      nbEch.setStyle("font-weight : bold;font-family: Verdana, Arial, Helvetica, sans-serif;");
      nbEch.setParent(vbox);
      final Label nbEchRest = new Label(Labels.getLabel("prelevement.restants.total.stockes"));
      nbEchRest.setStyle("font-weight : bold;font-family: Verdana, Arial, Helvetica, sans-serif;");
      nbEchRest.setParent(vbox);
      Column nbEchantillonsColumn = GatsbiController.addColumn(grid, null, "150px", "center", vbox, "auto", true);
      nbEchantillonsColumn.setId(id);
      return nbEchantillonsColumn;
   }
}