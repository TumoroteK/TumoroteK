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
 * Classe qui  étend {@link AbstractPlanCongelateurGenerator} mais reste abstraite 
 * car elle ne gère pas le format de sortie du document (excel, pdf ...)
 * Celui-ci est pris en charge par l'attribut documentProducer défini dans chacune de classes concrètes (classes filles) 
 * 
 * Cette classe gère les spécificités liées à l'affichage du plan AVEC boîtes.
 * Elle n'expose qu'une seule méthode buildDetailPlan() qui contient toute la logique
 * métier pour structurer sous forme de tableau (DataAsTable) les données d'un plan de conteneur avec boîtes
 * Ce tableau sera constitué d'autant de lignes d'entête qu'il y a de niveaux d'enceintes dans le conteneur
 * Toutes les enceintes de plus bas niveau se retrouveront donc sur une même ligne. 
 * Et pour chacune d'elle, ses boîtes seront affichées les unes en dessous des autres
 * Cette classe s'appuiera sur {@link EnceinteEmplacement} qui permet de gérer l'affichage des emplacements sans enceinte
 *   
 * Exemple de structuration des données
 * (Vide)    |R2        |          |          |          |          |          |          |          |          |R3        |          |          |          |          |          |
 * null      |C1-MODIF  |          |          |          |          |C2        |          |(Vide)    |C4        |          |C1        |          |C2        |          |C3        |          |(Vide)    |
 * null      |C1        |C2        |C3        |C4        |C5        |C1        |C2        |null      |C1        |C2        |C1        |C2        |C1        |C2 MODIF  |C1        |C2        |null      |
 * null      |BT1       |BT1       |BT1       |BT1       |BT1       |BT1       |BT1       |null      |BT1       |(Vide)    |BT1       |BT1       |BT1       |BT1       |BT1       |BT1       |null      |
 * null      |BT2       |BT2       |BT2       |BT2       |BT2       |BT2       |BT2       |null      |BT2       |BT2       |BT2       |BT2       |BT2       |BT2       |BT2       |BT2       |null      |
 * null      |(Vide)    |BT3       |BT3       |BT3       |BT3       |BT3       |BT3       |null      |BT3       |BT3       |BT3       |BT3       |BT3       |BT3       |BT3       |BT3       |null      |
 * null      |BT4       |BT4       |BT4       |BT4       |BT4       |BT4       |BT4       |null      |BT4       |BT4       |BT4       |BT4       |BT4       |BT4       |BT4       |BT4       |null      |
 * null      |BT5       |BT5       |BT5       |BT5       |BT5       |BT5       |BT5       |null      |BT5       |BT5       |BT5       |BT5       |BT5       |BT5       |BT5       |BT5       |null      |
 * null      |null      |null      |null      |null      |null      |BT6       |BT6       |null      |BT6       |BT6       |BT6       |BT6       |BT6       |BT6       |BT6       |BT6       |null      |
 * null      |null      |null      |null      |null      |null      |BT7       |BT7       |null      |BT7       |(Vide)    |BT7       |BT7       |BT7       |BT7       |BT7       |BT7       |null      |
 * null      |null      |null      |null      |null      |null      |BT8       |BT8       |null      |BT8       |BT8       |BT8       |BT8       |BT8       |BT8       |BT8       |BT8       |null      |
 * null      |null      |null      |null      |null      |null      |BT9       |BT9       |null      |BT9       |BT9       |BT9       |BT9       |BT9       |BT9       |BT9       |BT9       |null      |
 * null      |null      |null      |null      |null      |null      |BT10      |BT10      |null      |BT10      |BT10      |(Vide)    |BT10      |BT10      |BT10      |BT10      |BT10      |null      |
 *
 * @author C.H.
 */
public abstract class AbstractPlanCongelateurAvecBoiteGenerator extends AbstractPlanCongelateurGenerator
{
   @Override
   public DataAsTable buildDetailPlan(Conteneur conteneur){
      //Petit rappel des attributs d'un conteneur :
      //un conteneur contient un nombre de niveaux => nb niveaux enceinte + 1 niveau boîtes
      //un conteneur contient un nombre d'enceintes => nombre d'enceintes sur le niveau 1
      //une enceinte contient un nombre de places => nombre d'emplacements pour les enceintes fille ou les boîtes selon le niveau de l'enceinte
      int nbNiveauDuConteneur = conteneur.getNbrNiv();
                  //liste des enceintes par niveau (ligne "header" du tableau correspondant aux enceintes)
      //dans le DataAsTable à générer
      int nbLigneEntete = nbNiveauDuConteneur-1;//on retire le niveau boîtes

      //Le traitement s'appuie sur : 
      // - une étape de préparation consistant à alimenter une liste de listes d'EnceinteEmplacement.
      //   Chaque liste d'EnceinteEmplacement correspond à un niveau de type enceinte dans le conteneur
      //   La liste de listes contiendra donc nbLigneEntete listes.
      // - le traitement de génération du DataAsTable qui se découpera en :
      //     - une première lecture de la liste d'EnceinteEmplacement de plus bas niveau pour créer les lignes de boîtes
      //     - une 2e lecture de la liste d'EnceinteEmplacement pour construire la ligne d'entête correspondant, l'ajouter en position 0
      //       et valoriser le nombre d'emplacements d'enceinte de plus bas niveau pour tous les emplacements parents (colspan)
      //     - lecture des autres listes d'emplacements enceinte contenues dans la liste de liste pour ajouter les CellRow correspondant
      //       en partant par le niveau le plus bas et en remontant.
      
      //liste de listes d'emplacement pour enceintes correspondant à l'entête du document :
      List<List<EnceinteEmplacement>> listListEnceinteEmplacementParNiveau = new ArrayList<List<EnceinteEmplacement>>();
      
      //------- préparation : alimentation de listListEnceinteEmplacementParNiveau
      //cette liste temporaire sera utilisée pour créer la liste final de CellRow
      //gestion de la première ligne d'enceintes qui est liée au conteneur
      buildListEnceinteEmplacementPour1erNiveauDEntete(conteneur, listListEnceinteEmplacementParNiveau);
      // gestion des lignes suivantes qui sont liées à des enceintes
      for(int i=1;i<nbLigneEntete;i++) {
         //on passe en paramètre la liste du niveau supérieur (c'est à dire la liste des enceintes parents des enceintes à traiter)
         buildListEnceinteEmplacementPourNiveauDEnteteSuivant(listListEnceinteEmplacementParNiveau.get(i-1), listListEnceinteEmplacementParNiveau);
      }
      //------- fin de la préparation

      //------- traitement de construction du dataAsTable à partir de la liste de plus bas niveau d'EnceinteEmplacement
      DataAsTable dataAsTable = new DataAsTable();
      List<EnceinteEmplacement> listEnceinteEmplacementPlusBasNiveau = listListEnceinteEmplacementParNiveau.get(nbLigneEntete-1);
      createAllCellRowForBoite(dataAsTable, listEnceinteEmplacementPlusBasNiveau);
      createAllCellRowForEnceinte(dataAsTable, listListEnceinteEmplacementParNiveau, nbLigneEntete);

      return dataAsTable;
   }

   //lecture de la dernière ligne de listListEnceinteEmplacementParNiveau (listEnceinteEmplacementPlusBasNiveau)
   //pour alimenter les lignes du tableau correspondant aux boîtes : les boîtes sont en colonne sous leurs enceintes parents
   private void createAllCellRowForBoite(DataAsTable dataAsTable, List<EnceinteEmplacement> listEnceinteEmplacementPlusBasNiveau){
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
      
      //si la ou les dernières enceintes sont vide, on ajoute null à chaque CellRow
      //pour gérer de la même façon tous les autres emplacement sans enceinte.
      //(tous les CellRow auront donc le même nombre d'éléments)
      int nbEnceinteEmplacementPlusBasNiveau = listEnceinteEmplacementPlusBasNiveau.size();
      for(CellRow cellRow : dataAsTable.getListCellRow()) {
         while(cellRow.getNbDataCell()<nbEnceinteEmplacementPlusBasNiveau) {
            cellRow.addDataCell(null);
         }
      }
   }

   //création et alimentation des CellRow pour les Enceintes : 
   private void createAllCellRowForEnceinte(DataAsTable dataAsTable, List<List<EnceinteEmplacement>> listListEnceinteEmplacementParNiveau,
       int nbLigneEntete){
      //- lecture de la dernière ligne de listEnceinteEmplacementParNiveau (listEnceinteEmplacementPlusBasNiveau) 
      //    pour construire la ligne d'entête correspondant, l'ajouter en position 0
      //    et valoriser le nombre d'emplacement d'enceinte de plus bas niveau pour tous les emplacements parents
      CellRow rowDernierNiveauEnceinte = new CellRow();
      dataAsTable.getListCellRow().add(0, rowDernierNiveauEnceinte);
      int indexDerniereLigneEntete = nbLigneEntete-1;
      List<EnceinteEmplacement> listEnceinteEmplacementPlusBasNiveau = listListEnceinteEmplacementParNiveau.get(indexDerniereLigneEntete);
      int nbEnceinteEmplacementPlusBasNiveau = listEnceinteEmplacementPlusBasNiveau.size();
      for(int i=0; i< nbEnceinteEmplacementPlusBasNiveau; i++) {
         EnceinteEmplacement enceinteEmplacement = listEnceinteEmplacementPlusBasNiveau.get(i);
         DataCell dataCellForEnceinteEmplacementPlusBasNiveau = createDataCellForEnceinteEmplacement(enceinteEmplacement);
         //Pour cette ligne, l'alias est mis à la ligne pour faire une rupture visuelle avant l'affichage des boîtes (la ligne sera plus haute)
         if(dataCellForEnceinteEmplacementPlusBasNiveau != null) {
            dataCellForEnceinteEmplacementPlusBasNiveau.getCellContent().setComplementOnAnotherLine(true);
         }
         rowDernierNiveauEnceinte.addDataCell(dataCellForEnceinteEmplacementPlusBasNiveau);
         populateNbEnceintesDernierNiveauPourNiveauxSuperieurs(enceinteEmplacement);
      }
      //--------------------

      //remonte listEnceinteEmplacementParNiveau pour ajouter toujours en position 0 les lignes d'entête des lignes supérieures
      //=> boucle à partir de l'avant dernière car la dernière a déjà été traitée précédemment
      int indexAvantDerniereLigneEntete = indexDerniereLigneEntete-1;
      for(int i = indexAvantDerniereLigneEntete; i>=0 ; i--) {
         CellRow rowNiveauEnceinteATraiter = new CellRow();
         dataAsTable.getListCellRow().add(0, rowNiveauEnceinteATraiter);

         List<EnceinteEmplacement> listEnceinteEmplacementATraiter = listListEnceinteEmplacementParNiveau.get(i);
         int nbEnceinteEmplacementATraiter = listEnceinteEmplacementATraiter.size();
         for(int j = 0; j<nbEnceinteEmplacementATraiter; j++) {
            EnceinteEmplacement enceinteEmplacementATraiter = listEnceinteEmplacementATraiter.get(j);
            DataCell dataCellForEnceinteEmplacement = createDataCellForEnceinteEmplacement(enceinteEmplacementATraiter);
            //Pour ces lignes, on gère les colspans :
            if(dataCellForEnceinteEmplacement != null) {
               dataCellForEnceinteEmplacement.setColspan(enceinteEmplacementATraiter.getNbEnceinteDernierNiveau());
            }
            rowNiveauEnceinteATraiter.addDataCell(dataCellForEnceinteEmplacement);
         }
      }
   }







   //Ajoute un élément (une liste d'EnceinteEmplacement) à la liste listListEnceinteEmplacementParNiveau passée en paramètre
   //Cette liste EnceinteEmplacement correspond à tous les emplacements de 1er niveau dans l'arborescence du conteneur passé en paramètre
   private void buildListEnceinteEmplacementPour1erNiveauDEntete(Conteneur conteneur,  List<List<EnceinteEmplacement>> listListEnceinteEmplacementParNiveau) {
      //création de la ligne d'enceinte pour l'entête de 1er niveau et ajout à la liste des enceintes par niveau
      List<EnceinteEmplacement> listEnceintePour1erNiveau = new ArrayList<EnceinteEmplacement>();
      listListEnceinteEmplacementParNiveau.add(listEnceintePour1erNiveau);
      //on est sur la première ligne donc l'emplacement parent est null :
      addlistEnceinteToListEntete(conteneur.getNbrEnc(), getEnceinteManager().findByConteneurWithOrderManager(conteneur), null, listEnceintePour1erNiveau);
   }

   //Ajoute un élément (une liste d'EnceinteEmplacement) à la liste listListEnceinteEmplacementParNiveau passée en paramètre
   //Cette liste EnceinteEmplacement correspond à tous les emplacements "enfants" des EnceinteEmplacements de listEnceinteEmplacementPourNiveauDEntete passé en paramètre
   private void buildListEnceinteEmplacementPourNiveauDEnteteSuivant(List<EnceinteEmplacement> listEnceinteEmplacementPourNiveauDEntete,  List<List<EnceinteEmplacement>> listListEnceinteEmplacementParNiveau) {
      //création de la ligne d'enceinte pour l'entête de niveau suivant et ajout à la liste des enceintes par niveau
      List<EnceinteEmplacement> listEnceinteEmplacementPourNiveauDEnteteInferieur = new ArrayList<EnceinteEmplacement>();
      listListEnceinteEmplacementParNiveau.add(listEnceinteEmplacementPourNiveauDEnteteInferieur);

      int nbEnceinteATraiter = listEnceinteEmplacementPourNiveauDEntete.size();
      //parcours de la liste triée pour gérer les enceintes "supprimées" (emplacements disponibles) : 
      //listEnceinteEmplacementPourNiveauDEnteteInferieur s'incrémente au fur et à mesure du traitement des emplacements "parents"
      for(int i=0; i<nbEnceinteATraiter; i++) {
         EnceinteEmplacement enceinteEmplacementParent = listEnceinteEmplacementPourNiveauDEntete.get(i);
         Enceinte enceinteParent = enceinteEmplacementParent.getEnceinte();
         if(enceinteParent == null) {
            listEnceinteEmplacementPourNiveauDEnteteInferieur.add(new EnceinteEmplacement(null, enceinteEmplacementParent));
         }
         else {
            int nbPlace = enceinteParent.getNbPlaces();
            addlistEnceinteToListEntete(nbPlace, getEnceinteManager().findByEnceintePereWithOrderManager(enceinteParent), 
               enceinteEmplacementParent, listEnceinteEmplacementPourNiveauDEnteteInferieur);
         }
      }
   }
   
   //Prend en compte une liste d'enceintes (rattachées à la même enceinte "parent")  et les ajoute à la ligne d'entête en cours de construction (passée en paramètre)
   //en gérant les emplacements vide
   //listEnceinteATraiter : liste d'enceintes ayant toutes la même enceinte "parent". Cette liste est triée selon la position des enceintes dans l'enceinte "parent"
   //emplacementParent : EnceinteEmplacement de l'enceinte "parent" des enceintes de listEnceinteATraiter
   //nbEnceinteAAjouter : nombre d'enceintes max pouvant être mises dans l'enceinte parent
   //listEnceinteEmplacementPourUneLigneEnteteACompleter : la liste des enceintes en cours de traitement. Celle-ci correspond à un niveau du conteneur
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
            dataAsTable.addDataCell(new DataCell(EMPTY_POSITION, true), j, indexColonne);
            j++;
         }
         //ajout de la cellule correspondant à la boîte :
         DataCell cellBoite = new DataCell(
            terminaleATraiter.getNom(),
            formatAlias(terminaleATraiter.getAlias()),
            terminaleATraiter.getCouleur() == null ? null : terminaleATraiter.getCouleur().getHexa(), true);

         dataAsTable.addDataCell(cellBoite, j, indexColonne);
         j++;
      }
      //Gestion des trous en dernière position
      while(j<nbPlace) {
         dataAsTable.addDataCell(new DataCell(EMPTY_POSITION, true), j, indexColonne);
         j++;
      }
   }


   private DataCell createDataCellForEnceinteEmplacement(EnceinteEmplacement enceinteEmplacement) {
      DataCell dataCellForEnceinteEmplacementPlusBasNiveau = null;
      if(enceinteEmplacement != null && !enceinteEmplacement.isFictif()) {
         Enceinte enceinte = enceinteEmplacement.getEnceinte();
         if(enceinte == null) {
            dataCellForEnceinteEmplacementPlusBasNiveau = new DataCell(EMPTY_POSITION, true);
         }
         else {

            CellContent cellContent = new CellContent(enceinte.getNom(),formatAlias(enceinte.getAlias()), true);
            dataCellForEnceinteEmplacementPlusBasNiveau = new DataCell(cellContent,
                    enceinte.getCouleur() == null ? null : enceinte.getCouleur().getHexa(),
                     true);
         }
         
         dataCellForEnceinteEmplacementPlusBasNiveau.setAlignmentType(AlignmentType.CENTER);
      }
      
      return dataCellForEnceinteEmplacementPlusBasNiveau;
   }

  
   //passe dans chaque emplacement de dernier niveau et remonte
   //les parents pour ajouter 1 à son nbEnceinteDernierNiveau
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
