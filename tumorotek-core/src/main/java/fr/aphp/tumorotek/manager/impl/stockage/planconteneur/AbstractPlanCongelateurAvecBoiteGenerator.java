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
import fr.aphp.tumorotek.manager.io.document.detail.table.CellContent;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellRow;
import fr.aphp.tumorotek.manager.io.document.detail.table.DataCell;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe abstraite pour la génération de plans de congélateurs avec boîte.
 *
 * Cette classe étend {@link AbstractPlanCongelateurGenerator} et fournit une
 * implémentation spécifique pour la génération de plans de congélateurs qui
 * incluent des boîtes. Elle est conçue pour être étendue par des classes concrètes
 * qui doivent définir la logique spécifique à la génération des détails du plan.
 *
 */
public abstract class AbstractPlanCongelateurAvecBoiteGenerator extends AbstractPlanCongelateurGenerator {

    protected abstract EnceinteManager getEnceinteManager();

    /**
     * Creates a list of EnceinteEmplacement objects for the highest level of a container.
     * This method maps each position in the container to either an existing Enceinte or an empty placement.
     *
     * @param conteneur The container for which to create the EnceinteEmplacement list
     * @return A list of EnceinteEmplacement objects representing all positions at the highest level
     */
    public List<EnceinteEmplacement> createHighestLevelEnceinteEmplacementList(Conteneur conteneur) {
        List<EnceinteEmplacement> emplacementList = new ArrayList<>();
        List<Enceinte> enceintes = getEnceinteManager()
           .findByConteneurWithOrderManager(conteneur);

        Map<Integer, Enceinte> positionMap = createMapEnceintesByPosition(enceintes);

        for (int pos = 1; pos <= conteneur.getNbrEnc(); pos++) {
            EnceinteEmplacement emplacement = new EnceinteEmplacement();
            emplacement.setEnceinte(positionMap.get(pos));
            emplacementList.add(emplacement);
        }

        return emplacementList;
    }

    protected void createLowestLevelEnceinteList(List<EnceinteEmplacement> parentList,
       List<EnceinteEmplacement> resultList) {
        for (EnceinteEmplacement parentEmplacement : parentList) {
            Enceinte parent = parentEmplacement.getEnceinte();
            if (parent == null) {
                resultList.add(new EnceinteEmplacement());
                continue;
            }

            List<Enceinte> children = getEnceinteManager()
               .findByEnceintePereWithOrderManager(parent);

            int position = 1;
            for (Enceinte child : children) {
                // Fill gaps before current position
                while (position < child.getPosition()) {
                    resultList.add(new EnceinteEmplacement());
                    position++;
                }

                EnceinteEmplacement childEmplacement = new EnceinteEmplacement();
                childEmplacement.setEnceinte(child);
                resultList.add(childEmplacement);
                position++;
            }

            // Fill remaining positions
            while (position <= parent.getNbPlaces()) {
                resultList.add(new EnceinteEmplacement());
                position++;
            }
        }
    }

    private void buildColonneBoitePourUneEnceinte(DataAsTable dataAsTable,int columnIndex,int totalPlaces,Set<Terminale> terminals) {
        List<Terminale> sortedTerminals = new ArrayList<>(terminals);
        Collections.sort(sortedTerminals, Comparator.comparing(Terminale::getPosition));

        int currentPosition = 0;
        for (Terminale terminal : sortedTerminals) {
            // Fill empty spaces
            while (currentPosition < terminal.getPosition() - 1) {
                dataAsTable.addDataCell(new DataCell(EMPTY_CELL_CONTENT),
                   currentPosition, columnIndex);
                currentPosition++;
            }

            // Add terminal box
            DataCell boxCell = new DataCell(
               terminal.getNom(),
               terminal.getAlias(),
               terminal.getCouleur() != null ? terminal.getCouleur().getHexa() : null
            );
            dataAsTable.addDataCell(boxCell, currentPosition, columnIndex);
            currentPosition++;
        }

        while (currentPosition < totalPlaces) {
            dataAsTable.addDataCell(new DataCell(EMPTY_CELL_CONTENT),
               currentPosition, columnIndex);
            currentPosition++;
        }
    }


    /**
     * Cette méthode crée un objet DataAsTable, représentant toutes les enceintes d'un conteneur spécifié.
     * La méthode récupère toutes les enceintes, y compris celles des sous-niveaux, et les organise dans une structure
     * de type tableau.
     *
     <p>Cette méthode effectue les étapes suivantes :</p>
     * <ol>
     *   <li>Récupère toutes les enceintes du conteneur spécifié.</li>
     *   <li>Regroupe les enceintes récupérées par leur niveau à l'aide d'une Map, où la clé est le niveau et la valeur est une liste d'enceintes.</li>
     *   <li>Pour chaque niveau, crée une {@link CellRow} et la remplit avec le nom, l'alias et la couleur de chaque enceinte.</li>
     *   <li>Ajoute chaque {@link CellRow} rempli à l'instance de {@link DataAsTable}.</li>
     * </ol>
     *
     * @param conteneur le conteneur à partir duquel récupérer les enceintes ; ne doit pas être {@code null}.
     * @return un objet {@link DataAsTable} contenant les enceintes regroupées par niveau.
     */


    public DataAsTable createEnceintesSection(Conteneur conteneur) {
        DataAsTable enceintesData = new DataAsTable();

        // Récupérer toutes les enceintes du conteneur, sans se limiter à un seul niveau. La méthode findAllEnceinteByConteneurManager
        // renvoie toutes les enceintes d'un conteneur, y compris celles des sous-niveaux
        List<Enceinte> encientes = getEnceinteManager().findAllEnceinteByConteneurManager(conteneur);

        // Grouper les enceintes par leur niveau. On utilise une Map où la clé représente le niveau de l'enceinte,
        // et la valeur est une liste  contenant toutes les enceintes associées à ce niveau.
        Map<Integer, List<Enceinte>> leveledEncientes = new HashMap<>();

        for (Enceinte enceinte : encientes) {
            // On récupère le niveau de chaque enceinte en appelant la méthode getLevelEnceinte.
            int level = getEnceinteManager().getLevelEnceinte(enceinte);
            // On utilise computeIfAbsent pour ajouter les enceintes à la liste associée à leur niveau.
            leveledEncientes.computeIfAbsent(level, k -> new ArrayList<>()).add(enceinte);
        }

        for (Map.Entry<Integer, List<Enceinte>> entry : leveledEncientes.entrySet()) {
            CellRow levelCellRow = new CellRow();
            List<Enceinte> encientesAtLevel = entry.getValue();
            for (Enceinte enceinte :  encientesAtLevel){
                // Créer un objet CellContent avec le nom de l'enceinte et son alias
                CellContent cellContent = new CellContent(enceinte.getNom(), enceinte.getAlias());
                // Si l'enceinte a une couleur, on récupère son code hexadécimal, sinon on utilise une chaîne vide
                String colorHash = enceinte.getCouleur() != null ? enceinte.getCouleur().getHexa() : null;
                // On crée un DataCell pour l'enceinte, en ajoutant un colspan si l'enceinte a des sous-ensembles
                Integer totalNumberOfPlaces = getEnceinteManager().calculateTotalNbPlaces(enceinte);
                DataCell dataCell = new DataCell(cellContent,  colorHash, totalNumberOfPlaces);
                // Ajouter la cellule à la ligne de cellules
                levelCellRow.addDataCell(dataCell);
            }
            // Ajouter la ligne de cellules à l'objet DataAsTable
            enceintesData.addCellRow(levelCellRow);


        }
        return enceintesData;
    }





    public DataAsTable buildDetailPlan(Conteneur conteneur) {
        DataAsTable dataAsTable = createEnceintesSection(conteneur);
        List<List<EnceinteEmplacement>> levelsList = new ArrayList<>();

        // Create top level
        levelsList.add(createHighestLevelEnceinteEmplacementList(conteneur));

        // Create intermediate levels
        for (int i = 0; i < conteneur.getNbrNiv() - 2; i++) {
            List<EnceinteEmplacement> currentLevel = new ArrayList<>();
            createLowestLevelEnceinteList(levelsList.get(i), currentLevel);
            levelsList.add(currentLevel);
        }

        // Process lowest level boxes
        List<EnceinteEmplacement> lowestLevel = levelsList.get(levelsList.size() - 1);
        for (int i = 0; i < lowestLevel.size(); i++) {
            EnceinteEmplacement emplacement = lowestLevel.get(i);
            if (emplacement != null && emplacement.getEnceinte() != null) {
                Enceinte enceinte = emplacement.getEnceinte();
                buildColonneBoitePourUneEnceinte(
                   dataAsTable,
                   i,
                   enceinte.getNbPlaces(),
                   enceinte.getTerminales()
                );
            }
        }
        return dataAsTable;
    }

   





}
