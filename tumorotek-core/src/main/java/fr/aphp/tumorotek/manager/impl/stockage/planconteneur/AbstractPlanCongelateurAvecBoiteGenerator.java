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
     * Structure de données principale stockant la hiérarchie des enceintes.
     * Organisée par niveau, chaque niveau contenant une liste d'emplacements.
     * Le premier niveau (index 0) correspond aux enceintes directement sous le conteneur.
     */
   protected List<List<EnceinteEmplacement>> enceinteHierarchyByNiveau;

   /** Nombre de niveaux actuellement dans la hiérarchie */
   protected int currentHierarchyLevel;

    /** Nombre total de niveaux possibles dans la hiérarchie. Déterminé par la configuration du conteneur (getNbrNiv() - 1) */
   protected int totalEnceinteRowsNumber;

   protected abstract EnceinteManager getEnceinteManager();

   /**
    * Construit un plan détaillé du congélateur à partir d'un conteneur donné.
    * Le plan est construit en trois étapes :
    * 1. Construction de la hiérarchie des enceintes
    * 2. Remplissage des colonnes de terminales (niveau le plus bas)
    * 3. Remplissage des en-têtes d'enceintes (de bas en haut)
    *
    * @param conteneur Le conteneur pour lequel construire le plan
    * @return Un tableau de données (DataAsTable) représentant le plan du congélateur
    */
   @Override
   public DataAsTable buildDetailPlan(Conteneur conteneur){
      // Initialisation de la structure de données qui contiendra le plan
      DataAsTable dataAsTable = new DataAsTable();

      // Calcul du nombre de niveaux d'enceintes possibles
      // On soustrait 1 car getNbrNiv() inclut le niveau des terminales
      totalEnceinteRowsNumber = conteneur.getNbrNiv() - 1;

      // Étape 1 : Construction de la hiérarchie complète des enceintes
      // Cette étape est nécessaire avant de pouvoir remplir les données
      buildEnceinteTreeStructure(conteneur);

      // Étape 2 : Remplissage des colonnes de terminales (boîtes)
      // Cette étape doit être effectuée en premier car elle définit la structure de base du tableau
      populateTerminalesColumns(dataAsTable);

      // Étape 3 : Remplissage des en-têtes d'enceintes
      // Cette étape ajoute les en-têtes au-dessus des colonnes de terminales
      populateEnceinteHeaderRows(dataAsTable);

      // Retourne le plan finalisé avec toutes les données structurées
      return dataAsTable;
   }



   /**
    * Cette méthode construit la hiérarchie des enceintes à partir d'un conteneur donné.
    * Elle crée une liste de niveaux d'enceintes et traite chaque niveau jusqu'à atteindre les feuilles
    * La hiérarchie est stockée dans une liste de listes qui représente
    * chaque niveau de l'arborescence des enceintes.
    *
    * @param conteneur Le conteneur à partir duquel les enceintes sont extraites pour construire la hiérarchie.
    */
   protected void buildEnceinteTreeStructure(Conteneur conteneur){
      // Initialisation d'une liste pour stocker les enceintes par niveau
      enceinteHierarchyByNiveau = new ArrayList<>();

      // Compteur pour le nombre de lignes d'en-tête, utilisé pour suivre le niveau actuel
      currentHierarchyLevel = 0;

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
      currentHierarchyLevel++;

      // Traitement de chaque niveau tant qu'il y a un niveau suivant à traiter
      boolean hasNextLevel = true;
      while(hasNextLevel){
         hasNextLevel = addNextHierarchyLevel(); // Appel à une méthode qui ajoute le prochain niveau hiérarchique
      }
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
      // Récupère la liste des enceintes du niveau parent (niveau actuel - 1)
      List<EnceinteEmplacement> parentLevelEnceintes = enceinteHierarchyByNiveau.get(currentHierarchyLevel - 1);
      // Initialise la liste qui contiendra les enceintes enfants du nouveau niveau
      List<EnceinteEmplacement> childLevelEnceintes = new ArrayList<>();
      // Flag pour indiquer si des enfants ont été trouvés
      boolean hasChildEnceintes = false;
      // Calcul du niveau actuel (0-based)
      int currentLevelNumber = currentHierarchyLevel - 1;

      // Parcourt chaque emplacement d'enceinte du niveau parent
      for(EnceinteEmplacement parentEmplacement : parentLevelEnceintes) {
         if(parentEmplacement != null && parentEmplacement.getEnceinte() != null) {
            // Récupère toutes les enceintes enfants de l'enceinte parent actuelle
            List<Enceinte> childEnceintes =
               getEnceinteManager().findByEnceintePereWithOrderManager(parentEmplacement.getEnceinte());

            if(!childEnceintes.isEmpty()) {
               hasChildEnceintes = true;
               // Crée une map des enceintes indexées par leur position
               Map<Integer, Enceinte> positionMap = createMapEnceintesByPosition(childEnceintes);
               // Récupère le nombre total de places disponibles dans l'enceinte parent
               int nbPlaces = parentEmplacement.getEnceinte().getNbPlaces();

               // Parcourt toutes les positions possibles dans l'enceinte parent
               for(int position = 1; position <= nbPlaces; position++){
                  // Récupère l'enceinte enfant à cette position (peut être null)
                  Enceinte childEnceinte = positionMap.get(position);
                  EnceinteEmplacement childEmplacement = new EnceinteEmplacement(childEnceinte);

                  // Si on est au dernier niveau possible, marque l'emplacement comme feuille
                  if(currentLevelNumber == totalEnceinteRowsNumber - 1){
                     childEmplacement.setLeafNode(true);
                  }

                  // Ajoute l'enfant au parent et à la liste du nouveau niveau
                  parentEmplacement.addChild(childEmplacement);
                  childLevelEnceintes.add(childEmplacement);
               }
            } else {
               // Si pas d'enfants, ajoute des emplacements null pour maintenir la structure
               parentEmplacement.addChild(null);
               childLevelEnceintes.add(null);
            }
         } else {
            // Si le parent est null, ajoute un emplacement null pour maintenir la structure
            childLevelEnceintes.add(null);
         }
      }

      // Si des enfants ont été trouvés, ajoute le nouveau niveau à la hiérarchie
      if(hasChildEnceintes) {
         enceinteHierarchyByNiveau.add(childLevelEnceintes);
         currentHierarchyLevel++;
         return true;
      }
      return false;
   }

   /**
    * Construit les en-têtes pour chaque niveau d'enceintes dans le plan du congélateur.
    * Les en-têtes sont construits de bas en haut pour respecter l'ordre d'affichage.
    * Chaque en-tête contient le nom de l'enceinte, son alias et peut s'étendre sur plusieurs colonnes.
    *
    * @param dataAsTable La structure de données qui contiendra les en-têtes formatés
    */
   private void populateEnceinteHeaderRows(DataAsTable dataAsTable){
      // Parcours des niveaux de bas en haut pour construire les en-têtes
      // On commence par le niveau le plus bas (currentHierarchyLevel - 1) jusqu'au niveau 0
      for(int level = currentHierarchyLevel - 1; level >= 0; level--){
         // Récupération des enceintes du niveau courant
         List<EnceinteEmplacement> currentLevel = enceinteHierarchyByNiveau.get(level);
         // Création d'une nouvelle ligne pour ce niveau d'en-têtes
         CellRow positionCellRow = new CellRow();

         // Parcours de chaque emplacement d'enceinte dans le niveau courant
         for(EnceinteEmplacement emp : currentLevel){
            if(emp != null && emp.getEnceinte() != null){
               // Pour les emplacements contenant une enceinte valide
               
               // Calcul du nombre de colonnes que cette enceinte doit occuper
               int colspan = emp.getColumnSpan();
               // Création de l'alias formaté pour l'enceinte
               String alias = createAlias(emp.getEnceinte().getAlias());
               
               // Création du contenu de la cellule avec le nom et l'alias de l'enceinte
               CellContent enceinteCellContent =
                  new CellContent(emp.getEnceinte().getNom(), alias, true, true);

               // Création de la cellule avec mise en forme (couleur, alignement, etc.)
               DataCell cell = new DataCell(enceinteCellContent,
                  emp.getEnceinte().getCouleur() != null ? emp.getEnceinte().getCouleur().getHexa() : null, 
                  colspan, true,
                  AlignmentType.CENTER);

               // Ajout de la cellule à la ligne
               positionCellRow.addDataCell(cell);
            }else{
               // Pour les emplacements vides, création d'une cellule "(vide)"
               DataCell emptyCell = new DataCell(
                  new CellContent(EMPTY_POSITION), 
                  null, // pas de couleur pour les cellules vides
                  1,    // colspan de 1 pour les cellules vides
                  true, 
                  AlignmentType.CENTER
               );
               positionCellRow.addDataCell(emptyCell);
            }
         }
         // Ajout de la ligne d'en-têtes au début du tableau
         // L'insertion en position 0 permet de construire le tableau de haut en bas
         dataAsTable.getListCellRow().add(0, positionCellRow);
      }
   }


  /**
    * Remplit les données des terminales (boîtes) dans le tableau.
    * Cette méthode traite le dernier niveau de la hiérarchie qui contient
    * les enceintes terminales (celles qui contiennent directement les boîtes).
    *
    * @param dataAsTable La structure de données à remplir avec les informations des terminales
    */
   private void populateTerminalesColumns(DataAsTable dataAsTable) {
      // Vérifie qu'il existe au moins un niveau dans la hiérarchie
      if(enceinteHierarchyByNiveau.isEmpty()) {
         return;
      }

      // Récupère le dernier niveau de la hiérarchie (niveau le plus profond)
      List<EnceinteEmplacement> lastLevel = enceinteHierarchyByNiveau.get(currentHierarchyLevel - 1);

      // Parcourt chaque emplacement du dernier niveau
      for(int columnIndex = 0; columnIndex < lastLevel.size(); columnIndex++) {
         EnceinteEmplacement emp = lastLevel.get(columnIndex);

         // Passe les emplacements vides
         if(emp == null || emp.getEnceinte() == null) {
            continue;
         }

         // Récupère toutes les terminales (boîtes) associées à cette enceinte
         Set<Terminale> terminales = getEnceinteManager().getTerminalesManager(emp.getEnceinte());
         int totalPlaces = emp.getEnceinte().getNbPlaces();

         // Crée une map des terminales indexées par leur position pour un accès rapide
         Map<Integer, Terminale> terminalsByPosition = terminales.stream()
            .collect(Collectors.toMap(Terminale::getPosition, terminal -> terminal));

         // Parcourt toutes les positions possibles dans l'enceinte
         for(int position = 0; position < totalPlaces; position++) {
            // Récupère la terminale à la position actuelle (position+1 car les positions commencent à 1)
            Terminale terminal = terminalsByPosition.get(position + 1);

            DataCell cell;
            if(terminal != null) {
               // Si une terminale existe à cette position, crée une cellule avec ses informations
               String alias = createAlias(terminal.getAlias());
               CellContent terminaleCell = new CellContent(terminal.getNom(), alias, true, false);

               cell = new DataCell(
                  terminaleCell,
                  terminal.getCouleur() == null ? null : terminal.getCouleur().getHexa(),
                  1, // colspan de 1 pour les terminales
                  true,
                  AlignmentType.CENTER
               );
            } else {
               // Si pas de terminale à cette position, crée une cellule vide
               cell = new DataCell(
                  new CellContent(EMPTY_POSITION),
                  null, // pas de couleur pour les cellules vides
                  1,    // colspan de 1
                  true,
                  AlignmentType.CENTER
               );
            }

            dataAsTable.addDataCell(cell, position, columnIndex);
         }
      }
   }


}
