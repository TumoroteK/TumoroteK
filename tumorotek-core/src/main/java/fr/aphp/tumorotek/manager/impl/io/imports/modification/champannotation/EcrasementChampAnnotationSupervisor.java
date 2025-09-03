/**
 * Copyright ou © ou Copr. SESAN
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
package fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.aphp.tumorotek.manager.exception.ImportEcrasementDonneeWarningForCellException;
import fr.aphp.tumorotek.manager.io.imports.ImportCellError;
import fr.aphp.tumorotek.manager.io.imports.ImportRowError;
import fr.aphp.tumorotek.manager.io.imports.modification.champannotation.ImportChampAnnotationTraitement;

/**
 * classe qui gère le "mode supervision" c'est-à-dire l'appel de l'import avec détection des demandes de mises à jour de valeurs d'annotation existante
 * cela permet de remonter une alerte à l'utilisateur pour confirmation. Si confirmation, le traitement repasse avec ce mode désactivé
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class EcrasementChampAnnotationSupervisor
{
   private boolean modeSousSurveillance;
   
   private int nbRowTraiteAvant1erWarning = -1;//pour savoir si des rows ont été traitées avant de rencontrer des mises à jour non autorisées

   //dans le cas présent ImportCellError est un warning :
   //map alimentée au fur et à mesure du traitement : elle regroupe les ImportCellError par numéro de ligne traitée en vu de construire les ImportRowError
   private Map<Integer, List<ImportCellError>> mapImportCellWarningByNumRow = new HashMap<Integer, List<ImportCellError>>();
   
   public EcrasementChampAnnotationSupervisor(boolean modeSousSurveillance) {
      this.modeSousSurveillance = modeSousSurveillance;
   }

   /**
    * Analyse les données existantes récupérés par tous les traitements rattachés à l'import (pour rappel un traitement est lié à une colonne de ANNOTATION_VALEUR)
    * La méthode filtre les ImportChampAnnotationInfo correspondant à des mises à jour et construit une liste de ImportRowError (regroupement des erreurs pour une même ligne)
    * @param listImportTraitement
    * @param numLot
    * @return true si il n'y a pas de mise à jour à faire ou si le traitement n'est pas en mode sous surveillance c'est à dire que les mises à jour peuvent être faites
    */
   public List<ImportRowError> checkByLot(List<ImportChampAnnotationTraitement> listImportTraitement, int nbRowTraite) {
      if(modeSousSurveillance) {
         for(ImportChampAnnotationTraitement importTraitement : listImportTraitement) {
            List<ImportChampAnnotationInfo> listImportInfo = importTraitement.getListImportInfo();
            List<ImportChampAnnotationInfo> listImportInfoToUpdate = listImportInfo.stream().filter(importInfo -> importInfo.isUpdate()).collect(Collectors.toList());
            for(ImportChampAnnotationInfo importInfoToUpdate : listImportInfoToUpdate) {
               ImportEcrasementDonneeWarningForCellException importEcrasementDonneeWarning = new ImportEcrasementDonneeWarningForCellException(
                  importTraitement.getImportColonne(), importTraitement.convertValeurAsString(importInfoToUpdate.getOldValeur()), importTraitement.convertValeurAsString(importInfoToUpdate.getNewValeur()));
               ImportCellError warningEcrasement = new ImportCellError(importEcrasementDonneeWarning, importTraitement.getIndexColonneInFile());
               mapImportCellWarningByNumRow.computeIfAbsent(importInfoToUpdate.getNumRow(), k -> new ArrayList<ImportCellError>()).add(warningEcrasement);
            }
         }
         if (mapImportCellWarningByNumRow.size() > 0) {
            List<ImportRowError> listImportRowWarning = new ArrayList<ImportRowError>();
            for(Map.Entry<Integer, List<ImportCellError>> entry : mapImportCellWarningByNumRow.entrySet()) {
               ImportRowError importRowWarning = new ImportRowError(entry.getKey());
               importRowWarning.setListImportCellError(entry.getValue());

               listImportRowWarning.add(importRowWarning);
            }
            
            //on ne valorise le nombre de row traité que lors du traitement du 1er lot avec au moins un warning
            if(nbRowTraiteAvant1erWarning == -1) {
               nbRowTraiteAvant1erWarning = nbRowTraite;
            }
            
            return listImportRowWarning;
         }
         

      }
      
      return new ArrayList<ImportRowError>();

   }
   
   
   public int getNbRowTraiteAvant1erWarning(){
      return nbRowTraiteAvant1erWarning;
   }

}
