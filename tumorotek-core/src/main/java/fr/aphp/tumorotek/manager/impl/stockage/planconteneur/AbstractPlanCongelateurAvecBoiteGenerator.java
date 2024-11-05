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
public abstract class AbstractPlanCongelateurAvecBoiteGenerator extends AbstractPlanCongelateurGenerator
{

   protected abstract EnceinteManager getEnceinteManager();

   public static final String LIBELLE_EMPLACEMENT_BOITE_VIDE = "(vide)";
   //remplacer DataAsTable par DocumentData
   public DataAsTable buildDetailPlan(Conteneur conteneur){
      DataAsTable dataAsTable = new DataAsTable();

      //logique de la construction du détail :
      //récupération des enceintes par niveau et par ordre.
      //* préparation de la création des rows en passant par une liste de listes qui correspondra aux lignes des entêtes :
      // - gestion des trous avec la notion Emplacement qui contient une enceinte null s'il s'agit de trou et qui est null si correspond dans le tableau à une "cellule" sans donnée
      // - Tous les emplacements d'un niveau sont mis dans une liste
      // - Les liste des emplacements par niveau sont mis dans la liste de x listes qui correspond aux x lignes des entêtes
      //* exploitation de la liste de liste :
      // - 1ere lecture de la dernière liste correspondant aux enceintes contenant les boîtes pour créer les CellRow correspondant aux boîtes
      // - 2e lecture de la dernière liste pour ajouter en 1ere position la CellRow correspondant et alimenter les colspan de tous les emplacements parents
      // - lecture de la liste de liste d'emplacement en remontant les niveaux pour ajouter toujours en 1ere position la CellRow correspondant

      //////////////////////////////////////////////////////////////////////////////////////
      //Code de la partie ligne des "boîtes" : les CellRow sont alimentées au fur et à mesure de la lecture de la liste des emplacements de plus bas niveau
      List<EnceinteEmplacement> listEnceinteEmplacementPlusBasNiveau = listListEnceinteEmplacementParNiveau.get(nbLigneEntete-1);
      int nbEnceinteEmplacement = listEnceinteEmplacementPlusBasNiveau.size();
      for(int i=0; i< nbEnceinteEmplacement; i++) {
         EnceinteEmplacement enceinteEmplacement = listEnceinteEmplacementPlusBasNiveau.get(i);
         if(enceinteEmplacement != null) {
            Enceinte enceinteContenantLesBoites = enceinteEmplacement.getEnceinte();
            if(enceinteContenantLesBoites != null) {
               buildColonneBoitePourUneEnceinte(dataAsTable, i, enceinteContenantLesBoites.getNbPlaces(), enceinteContenantLesBoites.getTerminales());
            }
         }
      }
      //si la ou les dernières enceintes sont vide, on ajoute null aux listes pour être cohérent avec les autres emplacement d'une enceinte vide
      int nbEnceinteEmplacementPlusBasNiveau = listEnceinteEmplacementPlusBasNiveau.size();
      for(CellRow cellRow : dataAsTable.getListCellRow()) {
         while(cellRow.getNbDataCell()<nbEnceinteEmplacementPlusBasNiveau) {
            cellRow.addDataCell(null);
         }
      }

      //////////////////////////////////////////////////////////////////////////////////////

      //Affichage du résultat pour validation du traitement !!!
      dataAsTable.write();

      return dataAsTable;
   }



   //NB : DataAsTable.addDataCell() appelée ci-dessous se charge de créer la CellRow si celle-ci n'existe pas
   private void buildColonneBoitePourUneEnceinte(DataAsTable dataAsTable, int indexColonne, int nbPlace, Set<Terminale> setTerminales) {
      List<Terminale> listTerminaleATraiter = new ArrayList<Terminale>(setTerminales);
      Collections.sort(listTerminaleATraiter, Comparator.comparing(Terminale::getPosition));
      int nbTerminalesATraiter = listTerminaleATraiter.size();
      //j est l'index des places du conteneur
      int j = 0;
      for(int i=0; i<nbTerminalesATraiter; i++) {
         Terminale terminaleATraiter = listTerminaleATraiter.get(i);
         //ajout des boîtes vides en cas de trous :
         while(j<terminaleATraiter.getPosition()-1) {
            dataAsTable.addDataCell(new DataCell(LIBELLE_EMPLACEMENT_BOITE_VIDE), j, indexColonne);
            j++;
         }
         //ajout de la cellule correspondant à la boîte :
         // /!\ ils manquent les parenthèses autour de l'alias
         DataCell cellBoite = new DataCell(
            terminaleATraiter.getNom(),
            terminaleATraiter.getAlias(),
            terminaleATraiter.getCouleur() == null ? null : terminaleATraiter.getCouleur().getHexa());
         dataAsTable.addDataCell(cellBoite, j, indexColonne);
         j++;
      }
      //Gestion des trous en dernière position
      while(j<nbPlace) {
         dataAsTable.addDataCell(new DataCell(LIBELLE_EMPLACEMENT_BOITE_VIDE), j, indexColonne);
         j++;
      }
   }


}
