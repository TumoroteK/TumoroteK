/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
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
package fr.aphp.tumorotek.manager.impl.stockage.planconteneur;

import fr.aphp.tumorotek.manager.io.document.DataAsTable;
import fr.aphp.tumorotek.manager.io.document.detail.table.AlignmentType;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellContent;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellRow;
import fr.aphp.tumorotek.manager.io.document.detail.table.DataCell;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe abstraite pour la génération de plans de congélateurs avec boîte.
 *
 * Cette classe étend {@link AbstractPlanCongelateurGenerator} et fournit une
 * implémentation spécifique pour la génération de plans de congélateurs qui
 * incluent des boîtes. Elle est conçue pour être étendue par des classes concrètes
 * qui doivent définir la logique spécifique à la génération des détails du plan.
 *
 */
public abstract class AbstractPlanCongelateurAvecBoiteGenerator extends AbstractPlanCongelateurGenerator
{
   /**
    * Libellé utilisé pour représenter un emplacement de boîte vide dans le plan.
    * Cette constante est utilisée lors de l'affichage des positions sans boîte assignée.
    */
   public static final String LIBELLE_EMPLACEMENT_BOITE_VIDE = "(vide)";

   /**
    * Liste des listes d'emplacements d'enceintes organisée par niveau.
    * Chaque niveau contient une liste d'EnceinteEmplacement représentant la structure
    * physique du congélateur à ce niveau spécifique.
    */
   protected List<List<EnceinteEmplacement>> enceinteHierarchyByNiveau;

   /**
    * Nombre de lignes d'en-tête dans le plan.
    * Correspond au nombre de niveaux dans la hiérarchie des enceintes,
    * utilisé pour structurer l'affichage du plan.
    */
   protected int enceinteRowsNumber;

   protected int totalEnceinteRowsNumber;

   protected abstract EnceinteManager getEnceinteManager();

   /**
    * Cette méthode construit un tableau de données détaillé à partir d'un conteneur donné.
    *
    * @param conteneur Le conteneur dont les détails doivent être extraits pour construire le tableau.
    * @return Un objet DataAsTable contenant les informations organisées en lignes et colonnes.
    */

   @Override
   public DataAsTable buildDetailPlan(Conteneur conteneur){
      // Création d'une nouvelle instance de DataAsTable pour stocker les données organisées.
      DataAsTable dataAsTable = new DataAsTable();
      totalEnceinteRowsNumber = conteneur.getNbrNiv() - 1;
      // Construction de la hiérarchie des enceintes à partir du conteneur fourni et assignation à listListEnceinteEmplacementParNiveau
      buildEnceinteHierarchy(conteneur);


      debugPrintHierarchy();

      HandleTerminales(dataAsTable);

      handleEncintes(dataAsTable);

      return dataAsTable; // Retourne le tableau finalisé avec toutes les données ajoutées correctement structurées.
   }

   private void handleEncintes(DataAsTable dataAsTable){
      // Ajout des lignes d'en-tête pour chaque niveau d'enceintes, en commençant par le plus bas niveau.
      for(int level = enceinteRowsNumber - 1; level >= 0; level--){
         System.out.println("\nCreating a CellRow for level " + level);
         List<EnceinteEmplacement> currentLevel = enceinteHierarchyByNiveau.get(level);
         CellRow positionCellRow = new CellRow();

         for(EnceinteEmplacement emp : currentLevel){
            if(emp != null && emp.getEnceinte() != null){
               int colspan = emp.getColumnSpan();
               System.out.printf(" Adding cell: Nom=%s, Colspan=%d", emp.getEnceinte().getNom(), colspan);
               CellContent enceinteCellContent =
                  new CellContent(emp.getEnceinte().getNom(), emp.getEnceinte().getAlias(), true, true);

               DataCell cell = new DataCell(enceinteCellContent,
                  emp.getEnceinte().getCouleur() != null ? emp.getEnceinte().getCouleur().getHexa() : null, colspan, true,
                  AlignmentType.CENTER);

               positionCellRow.addDataCell(cell);
            }else{
               // Create a "(vide)" cell instead of null
               DataCell emptyCell = new DataCell(LIBELLE_EMPLACEMENT_BOITE_VIDE);
               // Should we set colspan for empty cells? Depends on your requirements
               emptyCell.setColspan(1);
               positionCellRow.addDataCell(emptyCell);
            }
         }
         dataAsTable.getListCellRow().add(0, positionCellRow);
      }
   }

   private void HandleTerminales(DataAsTable dataAsTable){
      // Traitement du dernier niveau (enceintes contenant des boites)
      if(!enceinteHierarchyByNiveau.isEmpty()){
         // Récupération de la liste des enceintes au dernier niveau.
         List<EnceinteEmplacement> lastLevel = enceinteHierarchyByNiveau.get(enceinteRowsNumber - 1);
         // Parcours de chaque enceinte au dernier niveau pour ajouter une colonne de boîte dans le tableau.
         for(int i = 0; i < lastLevel.size(); i++){
            EnceinteEmplacement emp = lastLevel.get(i);
            // Vérification que l'emplacement et l'enceinte ne sont pas nuls avant d'ajouter les colonnes.
            if(emp != null && emp.getEnceinte() != null){
               AddTerminalesToDataAsTable(dataAsTable, i, emp.getEnceinte().getNbPlaces(),
                  getEnceinteManager().getTerminalesManager(emp.getEnceinte()));
            }
         }
      }
   }



   /**
    * Cette méthode construit la hiérarchie des enceintes à partir d'un conteneur donné.
    * Elle crée une liste de niveaux d'enceintes et traite chaque niveau jusqu'à atteindre les feuilles
    * La hiérarchie est stockée dans une liste de listes qui représente
    * chaque niveau de l'arborescence des enceintes.
    *
    * @param conteneur Le conteneur à partir duquel les enceintes sont extraites pour construire la hiérarchie.
    */
   protected void buildEnceinteHierarchy(Conteneur conteneur){
      // Initialisation d'une liste pour stocker les enceintes par niveau
      enceinteHierarchyByNiveau = new ArrayList<>();

      // Compteur pour le nombre de lignes d'en-tête, utilisé pour suivre le niveau actuel
      enceinteRowsNumber = 0;

      // Création d'une liste pour le premier niveau (les enceintes directement sous le conteneur)
      List<EnceinteEmplacement> firstLevel = new ArrayList<>();
      // Récupération des enceintes racines associées au conteneur
      List<Enceinte> rootEnceintes = new ArrayList<>(getEnceinteManager().findByConteneurWithOrderManager(conteneur));
      Map<Integer, Enceinte> positionMap = createMapEnceintesByPosition(rootEnceintes);

      // Boucle sur toutes les places disponibles
      for(int i = 1; i <= conteneur.getNbrEnc(); i++){
         Enceinte enceinte = positionMap.get(i); // Récupération de l'enceinte associée à la place i
         // Création des objets EnceinteEmplacement pour chaque enceinte racine
         firstLevel.add(new EnceinteEmplacement(enceinte));
      }

      // Ajout du premier niveau à la liste principale des niveaux d'enceintes
      enceinteHierarchyByNiveau.add(firstLevel);
      // Incrémentation du compteur d'en-tête car nous avons ajouté un nouveau niveau
      enceinteRowsNumber++;

      // Traitement de chaque niveau tant qu'il y a un niveau suivant à traiter
      boolean hasNextLevel = true;
      while(hasNextLevel){
         hasNextLevel = addNextHierarchyLevel(); // Appel à une méthode qui ajoute le prochain niveau hiérarchique
      }
   }
   private void debugPrintHierarchy() {
      System.out.println("\n=== DEBUG: Enceinte Hierarchy Structure ===");

      for (int level = 0; level < enceinteHierarchyByNiveau.size(); level++) {
         System.out.println("\nLevel " + level + ":");
         System.out.println("----------------------------------------");

         List<EnceinteEmplacement> currentLevel = enceinteHierarchyByNiveau.get(level);
         for (int position = 0; position < currentLevel.size(); position++) {
            EnceinteEmplacement emp = currentLevel.get(position);

            StringBuilder sb = new StringBuilder()
               .append("Position ").append(position).append(": ");

            if (emp != null && emp.getEnceinte() != null) {
               Enceinte enceinte = emp.getEnceinte();
               sb.append(String.format(
                  "Nom: %-15s | Alias: %-15s | Position: %-3d | NbPlaces: %-3d | Colspan: %-3d",
                  enceinte.getNom(),
                  enceinte.getAlias() != null ? enceinte.getAlias() : "N/A",
                  enceinte.getPosition(),
                  enceinte.getNbPlaces(),
                  emp.getColumnSpan()
               ));

               // Add color info if available
               if (enceinte.getCouleur() != null) {
                  sb.append(" | Color: ").append(enceinte.getCouleur().getHexa());
               }

               // Add isLastEnceinte info
               sb.append(" | isLast: ").append(emp.isLastEnceinte());
            } else {
               sb.append("(vide)");
            }

            System.out.println(sb.toString());
         }
      }
      System.out.println("\n=== End of Hierarchy Structure ===\n");
   }
   /**
    * Ajoute un nouveau niveau de hiérarchie d'emplacements d'enceintes à la structure existante.
    *
    * Cette méthode vérifie les emplacements d'enceintes au niveau actuel et, pour chaque emplacement parent,
    * elle cherche ses enfants (les enceintes associées). Si des enfants existent, ils sont ajoutés
    * au niveau suivant. La méthode met également à jour la liste contenant tous les niveaux d'emplacements
    * et gère l'alignement des positions lorsque certains parents n'ont pas d'enfants.
    *
    * @return true si des enfants ont été ajoutés au niveau suivant, false sinon.
    */
   private boolean addNextHierarchyLevel() {
      List<EnceinteEmplacement> parentLevelEnceintes = enceinteHierarchyByNiveau.get(enceinteRowsNumber - 1);
      List<EnceinteEmplacement> childLevelEnceintes = new ArrayList<>();
      boolean hasChildEnceintes = false;
      int currentLevelNumber = enceinteRowsNumber - 1;

      for (EnceinteEmplacement parentEmplacement : parentLevelEnceintes) {
         if (parentEmplacement != null && parentEmplacement.getEnceinte() != null) {
            List<Enceinte> childEnceintes = getEnceinteManager()
               .findByEnceintePereWithOrderManager(parentEmplacement.getEnceinte());

            if (!childEnceintes.isEmpty()) {
               hasChildEnceintes = true;
               Map<Integer, Enceinte> positionMap = createMapEnceintesByPosition(childEnceintes);
               int nbPlaces = parentEmplacement.getEnceinte().getNbPlaces(); // Use NbPlaces instead of Position

               // Loop through all available places
               for (int position = 1; position <= nbPlaces; position++) {
                  Enceinte childEnceinte = positionMap.get(position);
                  EnceinteEmplacement childEmplacement = new EnceinteEmplacement(childEnceinte);

                  if (currentLevelNumber == totalEnceinteRowsNumber - 2) { // -2 because we're creating the next level
                     childEmplacement.setIsLastEnceinte(true);
                  }

                  parentEmplacement.addChild(childEmplacement);
                  childLevelEnceintes.add(childEmplacement);
               }
            } else {
               // Add null child to maintain structure
               parentEmplacement.addChild(null);
               childLevelEnceintes.add(null);
            }
         } else {
            childLevelEnceintes.add(null);
         }
      }

      if (hasChildEnceintes) {
         enceinteHierarchyByNiveau.add(childLevelEnceintes);
         enceinteRowsNumber++;
         return true;
      }
      return false;
   }

   private void AddTerminalesToDataAsTable(DataAsTable dataAsTable, int baseColumnIndex, int totalPlaces,
      Set<Terminale> terminals){

      // Calculate actual column index based on previous siblings' colspan
      int actualColumnIndex = calculateActualColumnIndex(baseColumnIndex);

      Map<Integer, Terminale> terminalsByPosition =
         terminals.stream().collect(Collectors.toMap(Terminale::getPosition, terminal -> terminal));

      for(int position = 1; position <= totalPlaces; position++){
         Terminale terminal = terminalsByPosition.get(position);
         int currentColumn = actualColumnIndex + position - 1;

         if(terminal != null){
            String alias =
               terminal.getAlias() == null ? "" : new StringBuilder("(").append(terminal.getAlias()).append(")").toString();
            CellContent terminaleCell = new CellContent(terminal.getNom(), alias, true, false);

            DataCell cell =
               new DataCell(terminaleCell, terminal.getCouleur() == null ? null : terminal.getCouleur().getHexa(), 1, true,
                  AlignmentType.CENTER);

            dataAsTable.addDataCell(cell, position - 1, currentColumn);
         }else{
            DataCell emptyTerminaleCell =
               new DataCell(new CellContent(LIBELLE_EMPLACEMENT_BOITE_VIDE), null, 1, true, AlignmentType.CENTER);
            dataAsTable.addDataCell(emptyTerminaleCell, position - 1, currentColumn);
         }
      }
   }

   private int calculateActualColumnIndex(int baseIndex){
      if(baseIndex == 0)
         return 0;

      // Get the last level (where terminals are)
      List<EnceinteEmplacement> lastLevel = enceinteHierarchyByNiveau.get(enceinteRowsNumber - 1);

      // Sum up colspans of all previous siblings
      int actualIndex = 0;
      for(int i = 0; i < baseIndex; i++){
         EnceinteEmplacement emp = lastLevel.get(i);
         if(emp != null && emp.getEnceinte() != null){
            actualIndex += emp.getEnceinte().getNbPlaces();
         }else{
            actualIndex += 1; // For empty positions
         }
      }

      return actualIndex;
   }
}
