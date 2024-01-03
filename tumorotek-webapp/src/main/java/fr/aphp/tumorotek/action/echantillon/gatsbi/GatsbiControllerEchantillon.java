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
package fr.aphp.tumorotek.action.echantillon.gatsbi;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.echantillon.EchantillonRowRenderer;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.gatsbi.RowRendererGatsbi;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Gatsbi controller regroupant les fonctionalités de modification dynamique de
 * l'interface spécifique aux échantillons
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class GatsbiControllerEchantillon
{

   public static void addColumnForChpId(final Integer chpId, final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      addColumnForChpId(chpId, grid, false);
   }
   
   public static void addColumnForChpId(final Integer chpId, final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      switch(chpId){
         case 53: // collaborateur
            drawOperateurColumn(grid, bloquerTri);
            break;
         case 54:
            // "code" toujours rendu par défaut
            break;
         case 56: // date stock
            drawDateStockColumn(grid, bloquerTri);
            break;
         case 58: // type
            drawEchantillonTypeColumn(grid, bloquerTri);
            break;
         case 60: // lateralite
            drawLateraliteColumn(grid, bloquerTri);
            break;
         case 61: // quantite (chpid=62 quantite_init & chpid=63 quantite unite, ignore)
            drawQuantiteColumn(grid, bloquerTri);
            break;
         case 67: // delai cgl
            drawDelaiCglColumn(grid, bloquerTri);
            break;
         case 68: // qualite
            drawQualiteColumn(grid, bloquerTri);
            break;
         case 69: // tumoral
            drawTumoralColumn(grid, bloquerTri);
            break;
         case 70: // preparation
            drawModePrepaColumn(grid, bloquerTri);
            break;
         case 72: // sterile
            drawSterileColumn(grid, bloquerTri);
            break;
         case 229: // codes organes
            drawCodesOrganeColumn(grid, bloquerTri);
            break;
         case 230: // codes lesionnels
            drawCodesLesionnelColumn(grid, bloquerTri);
            break;
         case 243: // conforme traitement -> rendu sous la forme d'une icône
            break;
         case 244: // conforme cession -> rendu sous la forme d'une icône
            break;
         case 255: // cr anapath
            drawCrAnapathColumn(grid, bloquerTri);
            break;
         default:
            break;
      }
   }

   // code, colonne toujours affichée
   public static Column drawCodeColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "general.code", null, null, null, "auto(code)", true, bloquerTri);
   }

   public static Column drawBanqueColumn(final Grid grid, final boolean visible, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Entite.Banque", null, null, null, "auto(code)", visible, bloquerTri);
   }

   // patient, colonne toujours affichée
   public static Column drawPatientColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "prelevement.patient", null, null, null, "auto(maladie.patient.nom)", false, bloquerTri);
   }

   // nb dérivés toujours affichée
   public static Column drawNbDerivesColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      final Column nbProdDerivesColumn = GatsbiController.addColumn(grid, "derives.nb", null, null, null, "auto", false, bloquerTri);//TG-199 : avant dernier param (visible) passé à false pour être cohénrent avec anapath' et séro
      nbProdDerivesColumn.setId("nbProdDerivesColumn");
      return nbProdDerivesColumn;
   }

   // nb cessions
   public static Column drawNbCessionsColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      final Column nbCessionsColumn = GatsbiController.addColumn(grid, "cession.nb", null, null, null, "auto", false, bloquerTri);//TG-199 : avant dernier param (visible) passé à false pour être cohénrent avec anapath' et séro
      nbCessionsColumn.setId("nbCessionsColumn");
      return nbCessionsColumn;
   }

   // collaborateur
   public static Column drawOperateurColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Echantillon.Collaborateur", null, null, null,
         "auto(collaborateur.nomAndPrenom)", true, bloquerTri);
   }

   public static Column drawObjetStatutColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Echantillon.ObjetStatut", null, null, null, "auto(objetStatut.statut)", true, bloquerTri);
   }

   public static Column drawDateStockColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Echantillon.DateStock", null, null, null, "auto(dateStock)", true, bloquerTri);
   }

   public static Column drawEmplacementColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Echantillon.Emplacement", null, null, null,
         "none", true);//TG-199 : tri passé à none pour être cohérent avec les contextes anapath' et séro
   }

   public static Column drawEchantillonTypeColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{

      return GatsbiController.addColumn(grid, "Champ.Echantillon.EchantillonType.Type", null, null, null,
         "auto(echantillonType.nom)", true, bloquerTri);
   }

   public static Column drawLateraliteColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Echantillon.Lateralite", null, null, null, "auto(lateralite)", true, bloquerTri);
   }

   public static Column drawQuantiteColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "ficheEchantillon.quantiteLabel", null, null, null, "none", true, bloquerTri);//TG-199 : tri passé à none pour être cohérent avec les contextes anapath' et séro:
   }

   public static Column drawDelaiCglColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Echantillon.DelaiCgl", null, null, null, "auto(delaiCgl)", true, bloquerTri);
   }

   public static Column drawQualiteColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Echantillon.EchanQualite", null, null, null, "auto(echanQualite.nom)", true, bloquerTri);
   }

   public static Column drawTumoralColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Echantillon.Tumoral", null, null, null, "auto(tumoral)", true, bloquerTri);
   }

   public static Column drawModePrepaColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Echantillon.ModePrepa", null, null, null, "auto(modePrepa.nom)", true, bloquerTri);
   }

   public static Column drawSterileColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Echantillon.Sterile", null, null, null, "auto(sterile)", true, bloquerTri);
   }

   public static Column drawCodesOrganeColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Echantillon.Organe", null, null, null, "auto", true, bloquerTri);
   }

   public static Column drawCodesLesionnelColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "ficheEchantillon.codeLesionelLabel", null, null, null, "auto", true, bloquerTri);
   }

   public static Column drawCrAnapathColumn(final Grid grid, final boolean bloquerTri)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "ficheEchantillon.crAnapathLabel", null, null, null, "auto(crAnapath.nom)", true, bloquerTri);
   }

   /**
    * Détermine la largeur de la colonnes des îcones à partir d'une largeur de base,
    * en fonction des champs d'informations affichés par gatsbi.
    * Null si la largeur finale = 0.
    * @param baseWidth
    * @param contexte
    * @return largeur sous la forme 00px
    */
   public static String getIconesColWidthFrom(int baseWidth, final Contexte contexte){
      if(contexte.isChampIdVisible(249)){ // risque
         baseWidth += 20;
      }
      if(contexte.isChampIdVisible(243)){ // non conf traitement
         baseWidth += 20;
      }
      if(contexte.isChampIdVisible(244)){ // non conf cession
         baseWidth += 20;
      }

      if(baseWidth == 0){
         return null;
      }

      return String.valueOf(baseWidth).concat("px");
   }

   /**
    * Applique la methode de rendering correspondant au champEntité id passé en
    * paramètre pour un objet Echantillon
    * @param chpId
    * @param row
    * @param echantillon
    * @param anonyme true/false
    * @param access stockage true/false
    * @throws NoSuchMethodException
    * @throws InvocationTargetException
    * @throws IllegalAccessException
    * @throws ParseException
    */
   public static void applyEchantillonChpRender(final Integer chpId, final Row row, final Echantillon echan,
      final boolean anonyme, final boolean accessStockage)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{

      switch(chpId){
         case 53: // collaborateur
            TKSelectObjectRenderer.renderCollaborateurProperty(row, echan, "collaborateur");
            break;
         case 54:
            // "code" toujours rendu par défaut
            break;
         case 55: // statut - normalement jamais transmis mais systématiquement affiché (TG-199)
            break;
         case 56: // date stock
            TKSelectObjectRenderer.renderDateProperty(row, echan, "dateStock");
            break;
         case 57: // emplacement - normalement jamais transmis mais systématiquement affiché (TG-199)
            break;            
         case 58: // type
            TKSelectObjectRenderer.renderThesObjectProperty(row, echan, "echantillonType");
            break;
         case 60: // lateralite
            EchantillonRowRenderer.renderLateralite(row, echan);
            break;
         case 61: // quantite (chpid=62 quantite_init & chpid=63 quantite unite, ignore)
            EchantillonRowRenderer.renderQuantite(row, echan);
            break;
         case 67: // delai cgl
            EchantillonRowRenderer.renderDelaiCgl(row, echan);
            break;
         case 68: // qualite
            TKSelectObjectRenderer.renderThesObjectProperty(row, echan, "echanQualite");
            break;
         case 69: // tumoral
            TKSelectObjectRenderer.renderBoolProperty(row, echan, "tumoral");
            break;
         case 70: // preparation
            TKSelectObjectRenderer.renderThesObjectProperty(row, echan, "modePrepa");
            break;
         case 72: // sterile
            TKSelectObjectRenderer.renderBoolProperty(row, echan, "sterile");
            break;
         case 229: // codes organes
            EchantillonRowRenderer.renderCodeAssignes(row,
               ManagerLocator.getCodeAssigneManager().findCodesOrganeByEchantillonManager(echan));
            break;
         case 230: // codes lesionnels
            EchantillonRowRenderer.renderCodeAssignes(row,
               ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager(echan));
            break;
         case 243: // conforme traitement -> rendu sous la forme d'une icône (chpId=261 raisons, ignore)
            break;
         case 244: // conforme cession -> rendu sous la forme d'une icône (chpId=262 raisons, ignore)
            break;
         case 255: // cr anapath
            EchantillonRowRenderer.renderCrAnapath(row, echan, anonyme);
            break;
         default:
            break;
      }
   }

   /**
    * Applique la methode de rendering correspondant au champEntité id passé en
    * paramètre pour un EchantillonDTO (decorator)
    * @param chpId
    * @param row
    * @param echantillonDTO
    * @param anonyme true/false
    * @param access stockage true/false
    * @throws NoSuchMethodException
    * @throws InvocationTargetException
    * @throws IllegalAccessException
    * @throws ParseException
    */
   public static void applyEchantillonDecoratorChpRender(final Integer chpId, final Row row, final EchantillonDTO deco,
      final boolean anonyme, final boolean accessStockage)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{

      switch(chpId){
         case 53: // collaborateur
            TKSelectObjectRenderer.renderAlphanumPropertyAsStringNoFormat(row, deco, "collaborateurNomAndPrenom");
            break;
         case 54:
            // "code" toujours rendu par défaut
            break;
         case 55: // statut - normalement jamais transmis mais systématiquement affiché (TG-199)
            break;
         case 56: // date stock
            TKSelectObjectRenderer.renderDateProperty(row, deco, "dateStockage");
            break;
         case 57: // emplacement - normalement jamais transmis mais systématiquement affiché (TG-199)
            break;
         case 58: // type
            TKSelectObjectRenderer.renderThesObjectProperty(row, deco.getEchantillon(), "echantillonType");
            break;
         case 60: // lateralite
            EchantillonRowRenderer.renderLateralite(row, deco.getEchantillon());
            break;
         case 61: // quantite (chpid=62 quantite_init & chpid=63 quantite unite, ignore)
            TKSelectObjectRenderer.renderAlphanumPropertyAsStringNoFormat(row, deco, "quantite");
            break;
         case 67: // delai cgl
            EchantillonRowRenderer.renderDelaiCgl(row, deco.getEchantillon());
            break;
         case 68: // qualite
            TKSelectObjectRenderer.renderThesObjectProperty(row, deco.getEchantillon(), "echanQualite");
            break;
         case 69: // tumoral
            TKSelectObjectRenderer.renderBoolProperty(row, deco.getEchantillon(), "tumoral");
            break;
         case 70: // preparation
            TKSelectObjectRenderer.renderThesObjectProperty(row, deco.getEchantillon(), "modePrepa");
            break;
         case 72: // sterile
            TKSelectObjectRenderer.renderBoolProperty(row, deco.getEchantillon(), "sterile");
            break;
         case 229: // codes organes
            EchantillonRowRenderer.renderCodeAssignes(row, deco.getCodesOrgsToCreateOrEdit());
            break;
         case 230: // codes lesionnels
            EchantillonRowRenderer.renderCodeAssignes(row, deco.getCodesLesToCreateOrEdit());
            break;
         case 243: // conforme traitement -> rendu sous la forme d'une icône (chpId=261 raisons, ignore)
            break;
         case 244: // conforme cession -> rendu sous la forme d'une icône (chpId=262 raisons, ignore)
            break;
         case 255: // cr anapath
            EchantillonRowRenderer.renderCrAnapath(row, deco.getEchantillon(), true); // non clickable
            break;
         default:
            break;
      }
   }

   // /!\ cette méthode dessine les colonnes pour des grids qui s'appuient sur des renderers différents EchantillonRowRendererGatsbi et EchantillonDecoratorRowRendererGatsbi 
   // => il y a un risque d'incohérence entre les entêtes et les lignes d'autant plus que cette methode se focalise sur des tableaux embarqués 
   // alors que EchantillonRowRendererGatsbi est également utilisé pour afficher le tableau de l'onglet Echantillon.
   // il aurait mieux fallu définir une méthode d'affichage des colonnes par Renderer...
   /**
    * Polymorphismes s'appliquent sur  deletable et force affichage emplacement et statut de stockage
    * car cette méthode est appelée pour rendre des inner lists de l'onglet Echantillon (échantillons
    * en création dans FicheMultiEchantillon) et de l'onglet Prélèvement.
    * @param contexte
    * @param grid
    * @param rowRenderer
    * @param deletable si true, affiche une dernière colonne avec un bouton delete
    * @param creerColonneTtesCollections si true, la colonne "toutes collections" sera créée et visible ou non selon la valeur du @param ttesCollections.
    * @throws ClassNotFoundException
    * @throws InstantiationException
    * @throws IllegalAccessException
    */
   public static void drawColumnsForEchantillons(final Contexte contexte, final Grid grid, final RowRendererGatsbi rowRenderer,
      final boolean deletable, final boolean creerColonneTtesCollections, final boolean ttesCollections)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{

      //dans le cas des listes embarquées, le tri n'est pas autorisé (même règle de gestion que pour les contextes anapath' et sérologie) :
      boolean bloquerTri = true;
      
      // icones column, visible si non conformites OU risque est visible
      if(contexte.isChampIdVisible(249) || contexte.isChampIdVisible(243) || contexte.isChampIdVisible(244)){

         int colsize = 0;
         if(contexte.isChampIdVisible(249)){
            colsize = colsize + 35;
         }
         if(contexte.isChampIdVisible(243)){
            colsize = colsize + 35;
         }
         if(contexte.isChampIdVisible(244)){
            colsize = colsize + 35;
         }

         GatsbiController.addColumn(grid, null, String.valueOf(colsize).concat("px"), "center", null, null, true, bloquerTri);

         // indique au row renderer qu'il doit dessiner les icones
         rowRenderer.setIconesRendered(true);
      }

      // code echan column, toujours affichée
      GatsbiControllerEchantillon.drawCodeColumn(grid, bloquerTri);
      
      // dans certains cas, le fait de créer la colonne "ttes collections" invisible peut créer un décalage 
      //(ex dans ficheMultiEchantillons inner list car elle n'est jamais affichée - utilisation d'un renderer différent des autres cas...)
      if (creerColonneTtesCollections) { 
         GatsbiControllerEchantillon.drawBanqueColumn(grid, ttesCollections, bloquerTri);
      }

      // variable columns
      for(final Integer chpId : contexte.getChampEntiteInTableauOrdered()){
         // statut et emplacement toujours affichés
         GatsbiControllerEchantillon.addColumnForChpId(chpId, grid, bloquerTri);
      }

      // TG-199 :
      // emplacement et statut sont toujours affichés
      GatsbiControllerEchantillon.drawObjetStatutColumn(grid, bloquerTri);
      GatsbiControllerEchantillon.drawEmplacementColumn(grid, bloquerTri);

      // TG-199 : pour les listes échantillons embarquées dans un écran, on ne crée pas les colonnes pour nb cessions ou nb dérivés. 
      // le renderer ne les génèrera pas car l'attribut 
      
      // delete col
      if(deletable){
         GatsbiController.addColumn(grid, null, "35px", "center", null, null, true, bloquerTri);
      }
   }

   public static List<Collaborateur> filterOperateursFromContexte(List<Collaborateur> allCollaborateur) {
      Contexte contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(3);
      return GatsbiController.filterExistingListModel(contexte, allCollaborateur, 53);
   }

}
