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
import fr.aphp.tumorotek.manager.io.document.detail.table.CellRow;
import fr.aphp.tumorotek.manager.io.document.detail.table.DataCell;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;

import java.util.ArrayList;
import java.util.Comparator;
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
   protected int nbLigneEntete;

   protected abstract EnceinteManager getEnceinteManager();


   /**
    * Cette méthode construit un tableau de données détaillé à partir d'un conteneur donné.
    *
    * @param conteneur Le conteneur dont les détails doivent être extraits pour construire le tableau.
    * @return Un objet DataAsTable contenant les informations organisées en lignes et colonnes.
    */

   @Override
   public DataAsTable buildDetailPlan(Conteneur conteneur) {
      // Création d'une nouvelle instance de DataAsTable pour stocker les données organisées.
      DataAsTable dataAsTable = new DataAsTable();
      // Construction de la hiérarchie des enceintes à partir du conteneur fourni et assignation à listListEnceinteEmplacementParNiveau
      buildEnceinteHierarchy(conteneur);

      // Traitement du dernier niveau (enceintes contenant des boites)
      if(!enceinteHierarchyByNiveau.isEmpty()) {
         // Récupération de la liste des enceintes au dernier niveau.
         List<EnceinteEmplacement> lastLevel = enceinteHierarchyByNiveau.get(nbLigneEntete - 1);
         // Parcours de chaque enceinte au dernier niveau pour ajouter une colonne de boîte dans le tableau.
         for(int i = 0; i < lastLevel.size(); i++) {
            EnceinteEmplacement emp = lastLevel.get(i);
            // Vérification que l'emplacement et l'enceinte ne sont pas nuls avant d'ajouter les colonnes.
            if(emp != null && emp.getEnceinte() != null) {
               AddTerminalesToDataAsTable(
                  dataAsTable,
                  i,
                  emp.getEnceinte().getNbPlaces(),
                  getEnceinteManager().getTerminalesManager(emp.getEnceinte())
               );
            }
         }
      }

      // Ajout des lignes d'en-tête pour chaque niveau d'enceintes, en commençant par le plus bas niveau.
      for(int level = nbLigneEntete - 1; level >= 0; level--) {
         List<EnceinteEmplacement> currentLevel = enceinteHierarchyByNiveau.get(level);
         // Création d'une nouvelle ligne d'en-tête pour le niveau actuel.
         CellRow headerRow = new CellRow();
         // Parcours de chaque enceinte à ce niveau pour remplir la ligne d'en-tête.
         for(EnceinteEmplacement emp : currentLevel) {
            // Vérification que l'emplacement et l'enceinte ne sont pas nuls avant de créer une cellule.
            if(emp != null && emp.getEnceinte() != null) {
               DataCell cell = new DataCell(
                  emp.getEnceinte().getNom(),    // Nom de l'enceinte
                  emp.getEnceinte().getAlias(),   // Alias de l'enceinte
                  emp.getEnceinte().getCouleur() != null ?
                     emp.getEnceinte().getCouleur().getHexa() : null  // Couleur hexadécimale si disponible
               );
               // Définition du colspan basé sur le nombre d'enceintes terminales associées à cette enceinte.
               cell.setColspan(emp.getNbEnceinteDernierNiveau());
               // Ajout de la cellule à la ligne d'en-tête courante.
               headerRow.addDataCell(cell);
            } else {
               // Ajout d'une cellule vide si aucune enceinte n'est trouvée.
               headerRow.addDataCell(null);
            }
         }

         // Ajout de la ligne d'en-tête nouvellement créée au début du tableau, garantissant qu'elle est affichée en premier.
         dataAsTable.getListCellRow().add(0, headerRow);
      }

      return dataAsTable; // Retourne le tableau finalisé avec toutes les données ajoutées correctement structurées.
   }



   /**
    * Cette méthode construit la hiérarchie des enceintes à partir d'un conteneur donné.
    * Elle crée une liste de niveaux d'enceintes et traite chaque niveau jusqu'à atteindre les feuilles
    * La hiérarchie est stockée dans une liste de listes qui représente
    * chaque niveau de l'arborescence des enceintes.
    *
    * @param conteneur Le conteneur à partir duquel les enceintes sont extraites pour construire la hiérarchie.
    */
   protected void buildEnceinteHierarchy(Conteneur conteneur) {
      // Initialisation d'une liste pour stocker les enceintes par niveau
      enceinteHierarchyByNiveau = new ArrayList<>();

      // Compteur pour le nombre de lignes d'en-tête, utilisé pour suivre le niveau actuel
      nbLigneEntete = 0;

      // Création d'une liste pour le premier niveau (les enceintes directement sous le conteneur)
      List<EnceinteEmplacement> firstLevel = new ArrayList<>();
      // Récupération des enceintes racines associées au conteneur
      List<Enceinte> rootEnceintes = new ArrayList<>(getEnceinteManager().findByConteneurWithOrderManager(conteneur));
      Map<Integer, Enceinte> positionMap = createMapEnceintesByPosition(rootEnceintes);

      // Boucle sur toutes les places disponibles
      for(int i = 1; i <= conteneur.getNbrEnc(); i++){
         Enceinte enceinte = positionMap.get(i); // Récupération de l'enceinte associée à la place i
         // Création des objets EnceinteEmplacement pour chaque enceinte racine
         EnceinteEmplacement emp = new EnceinteEmplacement(enceinte, null);
         firstLevel.add(emp);
      }

      // Ajout du premier niveau à la liste principale des niveaux d'enceintes
      enceinteHierarchyByNiveau.add(firstLevel);
      // Incrémentation du compteur d'en-tête car nous avons ajouté un nouveau niveau
      nbLigneEntete++;

      // Traitement de chaque niveau tant qu'il y a un niveau suivant à traiter
      boolean hasNextLevel = true;
      while(hasNextLevel) {
         hasNextLevel = addNextHierarchyLevel(); // Appel à une méthode qui ajoute le prochain niveau hiérarchique
      }

      // Mise à jour du nombre d'enceintes au dernier niveau pour chaque emplacement d'enceinte,
      // ce qui permet de fournir une vue complète sur la structure finale après construction.
      updateTerminalCounts();
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
      // Récupération du niveau courant basé sur le nombre de lignes d'entête déjà présents
      List<EnceinteEmplacement> currentLevel = enceinteHierarchyByNiveau.get(nbLigneEntete - 1);

      // Liste pour stocker le prochain niveau d'emplacements
      List<EnceinteEmplacement> nextLevel = new ArrayList<>();

      // Indicateur pour savoir s'il y a des enfants dans le niveau courant
      boolean hasChildren = false;

      // Parcours de chaque emplacement parent dans le niveau courant
      for(EnceinteEmplacement parent : currentLevel) {
         // Vérification que le parent n'est pas nul et qu'il possède une enceinte associée
         if(parent != null && parent.getEnceinte() != null) {
            // Récupération des enceintes enfants à partir de l'enceinte du parent
            List<Enceinte> childEnceintes = getEnceinteManager().findByEnceintePereWithOrderManager(parent.getEnceinte());
            // Si des enceintes enfants existent, on indique qu'il y a des enfants
            if(!childEnceintes.isEmpty()) {
               hasChildren = true; // Mise à jour de l'indicateur
               // Création d'un nouvel emplacement pour chaque enceinte enfant et ajout à nextLevel
               for(Enceinte childEnceinte : childEnceintes) {
                  EnceinteEmplacement childEmp = new EnceinteEmplacement(childEnceinte, parent);
                  nextLevel.add(childEmp); // Ajout de l'emplacement enfant au prochain niveau
               }
            } else {
               // Si aucun enfant n'est trouvé, on ajoute null pour maintenir l'alignement de position avec les parents
               nextLevel.add(null);
            }
         } else {
            nextLevel.add(null);
         }
      }
      // Si des enfants ont été trouvés dans ce niveau, on ajoute le prochain niveau à la liste et incrémente nbLigneEntete
      if(hasChildren) {
         enceinteHierarchyByNiveau.add(nextLevel);  // Ajout du nouveau niveau hiérarchique à la liste globale
         nbLigneEntete++; // Incrémentation du compteur d'en-tête qui représente maintenant un nouveau niveau
         return true; // Indication qu'un nouveau niveau a été ajouté avec succès
      }

      return false;  // Aucun nouveau niveau ajouté car il n'y avait pas d'enfants
   }

   /**
    * Met à jour les compteurs d'enceintes terminales dans la hiérarchie.
    * Parcourt la hiérarchie de bas en haut pour mettre à jour le nombre
    * d'enceintes terminales (nbEnceinteDernierNiveau) pour chaque EnceinteEmplacement.
    */
   private void updateTerminalCounts() {
      // Vérifie si la liste des enceintes au dernier niveau est vide
      if(enceinteHierarchyByNiveau.isEmpty()) {
         return; // Si la liste est vide, il n'y a rien à mettre à jour, donc on sort de la méthode.
      }

      // Commence par récupérer la dernière ligne (dernier niveau) de la liste des emplacements
      List<EnceinteEmplacement> lastLevel = enceinteHierarchyByNiveau.get(nbLigneEntete - 1);

      // Pour chaque enceinte dans le dernier niveau
      for(EnceinteEmplacement emp : lastLevel) {
         // Vérifie que l'enceinte et son emplacement parent ne sont pas nuls avant de procéder
         if(emp != null && emp.getEnceinte() != null) {
            // Incrémente le compteur pour cette enceinte au dernier niveau
            emp.increaseNbEnceinteDernierNiveau();

            // Propagation du compteur vers les parents de l'enceinte
            EnceinteEmplacement parent = emp.getEmplacementParent();
            // Tant qu'il existe un parent, on continue d'incrémenter le compteur vers le haut
            while(parent != null) {
               parent.increaseNbEnceinteDernierNiveau(); // Incrémente le compteur du parent
               parent = parent.getEmplacementParent(); // Passe au parent supérieur
            }
         }
      }
   }





   private void AddTerminalesToDataAsTable(DataAsTable dataAsTable, int columnIndex,
      int totalPlaces, Set<Terminale> terminals) {

      // Create position-based map of terminals
      Map<Integer, Terminale> terminalsByPosition = terminals.stream()
         .collect(Collectors.toMap(
            Terminale::getPosition,
            terminal -> terminal
         ));

      // Iterate through each position
      for (int position = 1; position <= totalPlaces; position++) {
         Terminale terminal = terminalsByPosition.get(position);

         if (terminal != null) {
            // Terminal found at this position - create cell with terminal details
            String alias = terminal.getAlias() != null ?
               "(" + terminal.getAlias() + ")" : "";

            DataCell cell = new DataCell(
               terminal.getNom(),
               alias,
               terminal.getCouleur() == null ? null : terminal.getCouleur().getHexa()
            );
            dataAsTable.addDataCell(cell, position - 1, columnIndex);
         } else {
            // No terminal at this position - add empty cell
            dataAsTable.addDataCell(new DataCell(LIBELLE_EMPLACEMENT_BOITE_VIDE),
               position - 1, columnIndex);
         }
      }
   }
}
