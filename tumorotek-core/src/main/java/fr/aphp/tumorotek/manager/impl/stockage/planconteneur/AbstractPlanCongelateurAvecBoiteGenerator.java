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
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Classe abstraite pour la génération de plans de congélateurs avec boîte.
 *
 * Cette classe étend {@link AbstractPlanCongelateurGenerator} et fournit une
 * implémentation spécifique pour la génération de plans de congélateurs qui
 * incluent des boîtes. Elle est conçue pour être étendue par des classes concrètes
 * qui doivent définir la logique spécifique à la génération des détails du plan.
 *
 * @author C.H.
 */
public abstract class AbstractPlanCongelateurAvecBoiteGenerator extends AbstractPlanCongelateurGenerator
{

   public static final String LIBELLE_EMPLACEMENT_ENCEINTE_VIDE = "vide";
   public static final String LIBELLE_EMPLACEMENT_BOITE_VIDE = "(vide)";


   //liste des enceintes par niveau (chaque niveau correspond à une liste). Elle constituera les lignes d'entête du tableau final
   //cet objet est un objet interne au traitement de génération du DataAsTable


   //remplacer DataAsTable par DocumentData
   @Override
   public DataAsTable buildDetailPlan(Conteneur conteneur){
      List<List<EnceinteEmplacement>> listListEnceinteEmplacementParNiveau = new ArrayList<List<EnceinteEmplacement>>();

      DataAsTable dataAsTable = new DataAsTable();

      //un conteneur contient un nombre de niveaux => nb niveaux enceinte + 1 niveau boîtes
      //un conteneur contient un nombre d'enceintes => nombre d'enceintes sur le niveau 1
      //une enceinte contient un nombre de places => nombre d'emplacements pour les enceintes fille ou les boîtes selon le niveau de l'enceinte

      int nbNiveauDuConteneur = conteneur.getNbrNiv();

      //liste des enceintes par niveau (ligne "header" du tableau correspondant aux enceintes)
      int nbLigneEntete = nbNiveauDuConteneur-1;//on retire le niveau boîtes

      //initialisation du conteneur car la session hibernate est "fermée" donc les getEnceintes ne sont pas accessibles dans cela
      //      conteneur = conteneurManager.findByIdManager(conteneur.getConteneurId());

      //Pour la première ligne, les enceintes seront issues du conteneur
      //Pour les lignes suivantes, elles seront tirées d'une enceinte
      // /!\ getEnceinte() (sur conteneur ou sur enceinte) renvoie un Set qui ne permet pas de trier => on passe à chaque fois par une liste qu'on triera

      //préparation : construction de la liste de liste d'enceintes correspondant à l'entête du document
      //cette liste temporaire sera utilisée pour créer la liste final de CellRow
      //gestion de la première ligne d'enceintes qui est liée au conteneur
      buildListEnceinteEmplacementPour1erNiveauDEntete(conteneur, listListEnceinteEmplacementParNiveau);
      // gestion des lignes suivantes
      for(int i=1;i<nbLigneEntete;i++) {
         //on passe en paramètre la liste du niveau supérieur (c'est à dire la liste des enceintes parents des enceintes à traiter)
         buildListEnceinteEmplacementPourNiveauDEnteteSuivant(listListEnceinteEmplacementParNiveau.get(i-1), listListEnceinteEmplacementParNiveau);
      }
      //fin de la préparation

      //////////////////////////////////////////////////////////////////////////////////////
      //parcours de la dernière ligne de listEnceinteEmplacementParNiveau :
      //-pour construire les lignes correspondant aux terminales
      //-ajouter en position 0 la ligne d'entête correspondant au niveau juste avant les boîtes
      //----
      //1ere lecture de la dernière ligne de listEnceinteParNiveau pour construire le tableau des boîtes
      List<EnceinteEmplacement> listEnceinteEmplacementPlusBasNiveau = listListEnceinteEmplacementParNiveau.get(nbLigneEntete-1);
      int nbEnceinteEmplacement = listEnceinteEmplacementPlusBasNiveau.size();
      for(int i=0; i< nbEnceinteEmplacement; i++) {
         EnceinteEmplacement enceinteEmplacement = listEnceinteEmplacementPlusBasNiveau.get(i);
         if(enceinteEmplacement != null) {
            Enceinte enceinteContenantLesBoites = enceinteEmplacement.getEnceinte();
            if(enceinteContenantLesBoites != null) {
               Set<Terminale> terminales = getEnceinteManager().getTerminalesManager(enceinteContenantLesBoites);
               buildColonneBoitePourUneEnceinte(dataAsTable, i, enceinteContenantLesBoites.getNbPlaces(), terminales);
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



      //2e lecture de la dernière ligne de listEnceinteEmplacementParNiveau pour construire la ligne d'entête correspondant et l'ajouter en position 0
      //et valoriser le nombre d'emplacement d'enceinte de plus bas niveau pour tous les emplacements parents
      CellRow rowDernierNiveauEnceinte = new CellRow();
      dataAsTable.getListCellRow().add(0, rowDernierNiveauEnceinte);
      for(int i=0; i< nbEnceinteEmplacement; i++) {
         EnceinteEmplacement enceinteEmplacement = listEnceinteEmplacementPlusBasNiveau.get(i);
         DataCell dataCellForEnceinteEmplacementPlusBasNiveau = null;
         if(enceinteEmplacement != null) {
            Enceinte enceinte = enceinteEmplacement.getEnceinte();
            if(enceinte == null) {
               dataCellForEnceinteEmplacementPlusBasNiveau = new DataCell(LIBELLE_EMPLACEMENT_ENCEINTE_VIDE, true);
               dataCellForEnceinteEmplacementPlusBasNiveau.setAlignmentType(AlignmentType.CENTER);
            }
            else {

               CellContent cellContent = new CellContent(enceinte.getNom(),createAlias(enceinte.getAlias()), true, true);
               dataCellForEnceinteEmplacementPlusBasNiveau = new DataCell(cellContent,
                       enceinte.getCouleur() == null ? null : enceinte.getCouleur().getHexa(),
                        true);
               dataCellForEnceinteEmplacementPlusBasNiveau.setAlignmentType(AlignmentType.CENTER);
            }
         }
         rowDernierNiveauEnceinte.addDataCell(dataCellForEnceinteEmplacementPlusBasNiveau);
         populateNbEnceintesDernierNiveauPourNiveauxSuperieurs(enceinteEmplacement);
      }
      //--------------------

      //remonter listEnceinteEmplacementParNiveau pour ajouter toujours en position 0 les lignes d'entête des lignes supérieures
      //=> boucle à partir de l'avant dernière car la dernière a déjà été traitée précédemment
      int indexDerniereLigneEntete = nbLigneEntete-1;
      int indexAvantDerniereLigneEntete = indexDerniereLigneEntete-1;
      for(int i = indexAvantDerniereLigneEntete; i>=0 ; i--) {
         CellRow rowNiveauEnceinteATraiter = new CellRow();
         dataAsTable.getListCellRow().add(0, rowNiveauEnceinteATraiter);

         List<EnceinteEmplacement> listEnceinteEmplacementATraiter = listListEnceinteEmplacementParNiveau.get(i);
         int nbEnceinteEmplacementATraiter = listEnceinteEmplacementATraiter.size();
         for(int j = 0; j<nbEnceinteEmplacementATraiter; j++) {
            EnceinteEmplacement enceinteEmplacementATraiter = listEnceinteEmplacementATraiter.get(j);
            DataCell dataCellForEnceinteEmplacement = null;
            if(enceinteEmplacementATraiter != null) {
               Enceinte enceinte = enceinteEmplacementATraiter.getEnceinte();
               if(enceinte == null) {
                  dataCellForEnceinteEmplacement = new DataCell(LIBELLE_EMPLACEMENT_ENCEINTE_VIDE, true);
                  dataCellForEnceinteEmplacement.setAlignmentType(AlignmentType.CENTER);
               }
               else {
                  CellContent enceinteCellContent = new CellContent(enceinte.getNom(), createAlias(enceinte.getAlias()), true, false);
                  dataCellForEnceinteEmplacement = new DataCell(enceinteCellContent,
                     enceinte.getCouleur() == null ? null : enceinte.getCouleur().getHexa(),
                     enceinteEmplacementATraiter.getNbEnceinteDernierNiveau(), true);
                  dataCellForEnceinteEmplacement.setAlignmentType(AlignmentType.CENTER);
               }
            }
            rowNiveauEnceinteATraiter.addDataCell(dataCellForEnceinteEmplacement);
         }
      }


      return dataAsTable;
   }




   private void buildListEnceinteEmplacementPour1erNiveauDEntete(Conteneur conteneur,  List<List<EnceinteEmplacement>> listListEnceinteEmplacementParNiveau) {
      //création de la ligne d'enceinte pour l'entête de 1er niveau et ajout à la liste des enceintes par niveau
      List<EnceinteEmplacement> listEnceintePour1erNiveau = new ArrayList<EnceinteEmplacement>();
      listListEnceinteEmplacementParNiveau.add(listEnceintePour1erNiveau);
      //on est sur la première ligne donc l'emplacement parent est null :
      addlistEnceinteToListEntete(conteneur.getNbrEnc(), getEnceinteManager().findByConteneurWithOrderManager(conteneur), null, listEnceintePour1erNiveau);
   }



   private void buildListEnceinteEmplacementPourNiveauDEnteteSuivant(List<EnceinteEmplacement> listEnceinteEmplacementPourNiveauDEntete,  List<List<EnceinteEmplacement>> listListEnceinteEmplacementParNiveau) {
      //création de la ligne d'enceinte pour l'entête de niveau suivant et ajout à la liste des enceintes par niveau
      List<EnceinteEmplacement> listEnceinteEmplacementPourNiveauDEnteteInferieur = new ArrayList<EnceinteEmplacement>();
      listListEnceinteEmplacementParNiveau.add(listEnceinteEmplacementPourNiveauDEnteteInferieur);

      int nbEnceinteATraiter = listEnceinteEmplacementPourNiveauDEntete.size();
      //parcourt de la liste triée pour gérer les enceintes supprimées
      for(int i=0; i<nbEnceinteATraiter; i++) {
         EnceinteEmplacement enceinteEmplacementParent = listEnceinteEmplacementPourNiveauDEntete.get(i);
         //si l'emplacement est null ou vide, les emplacements des niveaux inférieurs sont mis à null
         if(enceinteEmplacementParent == null || enceinteEmplacementParent.getEnceinte() == null) {
            listEnceinteEmplacementPourNiveauDEnteteInferieur.add(null);
         }
         else {
            //listEnceinteEmplacementPourNiveauDEnteteInferieur passée en paramètre s'incrémente au fur et à mesure du traitement des emplacements "parents"
            addListEnceinteEmplacementEnfant(enceinteEmplacementParent, listEnceinteEmplacementPourNiveauDEnteteInferieur);
         }
      }
   }

   //Ajouter les enceintes "fille" d'une enceinte "père", passée en paramètre, à la ligne d'entête en cours de construction, passée en paramètre
   private void addListEnceinteEmplacementEnfant(EnceinteEmplacement enceinteEmplacementParent, List<EnceinteEmplacement> listEnceinteEmplacementPourUneLigneEnteteACompleter) {
      if(enceinteEmplacementParent == null) {
         listEnceinteEmplacementPourUneLigneEnteteACompleter.add(null);
      }
      else {
         Enceinte enceinteParent = enceinteEmplacementParent.getEnceinte();
         if(enceinteParent == null) {
            listEnceinteEmplacementPourUneLigneEnteteACompleter.add(new EnceinteEmplacement(null, enceinteEmplacementParent));
         }
         else {
            int nbPlace = enceinteParent.getNbPlaces();
            addlistEnceinteToListEntete(nbPlace, getEnceinteManager().findByEnceintePereWithOrderManager(enceinteParent), enceinteEmplacementParent, listEnceinteEmplacementPourUneLigneEnteteACompleter);
         }
      }
   }

   //Prend en compte un set d'enceintes, le trie selon la position et les ajoute à la ligne d'entête en cours de construction, passée en paramètre
   //en gérant les emplacements vide
   private void addlistEnceinteToListEntete(int nbEnceinteAAjouter, List<Enceinte> listEnceinteATraiter, EnceinteEmplacement emplacementParent,List<EnceinteEmplacement> listEnceinteEmplacementPourUneLigneEnteteACompleter) {
      int nbEnceinteATraiter = listEnceinteATraiter.size();
      int j = 0;//correspond à la position - 1 de l'emplacement
      //parcourt de la liste triée pour gérer les enceintes supprimées
      for(int i=0; i<nbEnceinteATraiter; i++) {
         Enceinte enceinteAtraiter = listEnceinteATraiter.get(i);
         //comme la liste est triée, si la position de cette enceinte ne correspond pas à i+1 - la position commence à 1 -,
         //c'est que l'enceinte en position i+1 à été supprimée => ajout de null.
         //il peut y avoir plusieurs emplacements vide à la suite => utilisation d'une boucle
         while(j<enceinteAtraiter.getPosition()-1) {
            listEnceinteEmplacementPourUneLigneEnteteACompleter.add(new EnceinteEmplacement(null, emplacementParent)) ;
            j++;
         }
         listEnceinteEmplacementPourUneLigneEnteteACompleter.add(new EnceinteEmplacement(enceinteAtraiter, emplacementParent));
         j++;
      }
      //Gestion des trous en dernière position
      while(j<nbEnceinteAAjouter) {
         listEnceinteEmplacementPourUneLigneEnteteACompleter.add(new EnceinteEmplacement(null, emplacementParent)) ;
         j++;
      }

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
            dataAsTable.addDataCell(new DataCell(LIBELLE_EMPLACEMENT_BOITE_VIDE, true), j, indexColonne);
            j++;
         }
         //ajout de la cellule correspondant à la boîte :
         DataCell cellBoite = new DataCell(
            terminaleATraiter.getNom(),
            createAlias(terminaleATraiter.getAlias()),
            terminaleATraiter.getCouleur() == null ? null : terminaleATraiter.getCouleur().getHexa(), true);

         dataAsTable.addDataCell(cellBoite, j, indexColonne);
         j++;
      }
      //Gestion des trous en dernière position
      while(j<nbPlace) {
         dataAsTable.addDataCell(new DataCell(LIBELLE_EMPLACEMENT_BOITE_VIDE, true), j, indexColonne);
         j++;
      }
   }




   //on va passer dans chaque emplacement de dernier niveau et on va
   //remonter les parents pour ajouter 1 à son nbEnceinteDernierNiveau
   //les nbEnceinteDernierNiveau vont donc s'incrémenter petit à petit au fur et à mesure de la lecture des enceintes de dernier niveau
   private void populateNbEnceintesDernierNiveauPourNiveauxSuperieurs(EnceinteEmplacement enceinteEmplacementDernierNiveau) {
      if(enceinteEmplacementDernierNiveau != null) {
         EnceinteEmplacement enceinteEmplacementAtraiter = enceinteEmplacementDernierNiveau;
         while(enceinteEmplacementAtraiter != null) {
            enceinteEmplacementAtraiter.increaseNbEnceinteDernierNiveau();
            enceinteEmplacementAtraiter = enceinteEmplacementAtraiter.getEmplacementParent();
         }
      }
   }

}
