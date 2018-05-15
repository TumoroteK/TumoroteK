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
package fr.aphp.tumorotek.action.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.io.utils.RechercheUtilsManager;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Resultat;

/**
 * Classe utilitaire regroupant les methodes optimisées
 * de récupération des objets et
 * d'affichage du module de Recherches complexes.
 * Date: 11/03/2013
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10
 *
 */
public class RechercheUtils
{

  
   /**
    * Charge la matrice (table) des résultats de recherche
    * @param matObjs ??
    * @param matAfs ??
    * @param affichage affichage
    */
   public static void loadMatriceAffichable(final List<List<Object>> matObjs, final List<List<Object>> matAfs,
      final Affichage affichage){
      // On itère la matrice
      final Iterator<List<Object>> itMat = matObjs.iterator();
      while(itMat.hasNext()){
         final List<Object> listeObjets = itMat.next();
         final List<Object> listeStrings = new ArrayList<>();
         // On itère les résultats
         final Iterator<Resultat> itRes = affichage.getResultats().iterator();
         while(itRes.hasNext()){
            final Resultat res = itRes.next();
            /* On récupère l'entité depuis le champ du Resultat. */

            String champValue = "-";
            if(res != null){
               if(res.getChamp() != null){
                  final String champValueTmp = ObjectTypesFormatters.formatObject(RechercheUtilsManager.getChampValueFromObjectList(res.getChamp(), listeObjets));
                  if(!"".equals(champValueTmp)){
                     champValue = champValueTmp;
                  }
               }
            }
            listeStrings.add(champValue);
         }
         matAfs.add(listeStrings);
      }
   }


   /**
    * Construit un tableau EXCEL d'objets
    *
    * @param wb
    * @param matriceAffichable
    * @param affichage
    *
    * @throws Exception
    * @return structure html
    */
   public final static Workbook exportTableExcel(final Workbook wb, final List<List<Object>> matriceAffichable,
      final Affichage affichage){

      final List<Resultat> resultats = new ArrayList<>();
      resultats.addAll(affichage.getResultats());
      // On trie les résultats par position
      if(resultats.size() > 1){
         for(int i = 1; i < resultats.size(); i++){
            for(int j = i - 1; j >= 0; j--){
               if(resultats.get(j + 1).getPosition() < resultats.get(j).getPosition()){
                  final Resultat temp = resultats.get(j + 1);
                  resultats.set(j + 1, resultats.get(j));
                  resultats.set(j, temp);
               }
            }
         }
      }

      final Sheet sheet = wb.createSheet();

      final CellStyle dateStyle = wb.createCellStyle();
      dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

      final CellStyle dateStyleShort = wb.createCellStyle();
      dateStyleShort.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

      short cptRow = 0;

      final Row row0 = sheet.createRow(0);
      for(short i = 0; i < resultats.size(); i++){
         final Cell cell = row0.createCell(i);
         cell.setCellValue(resultats.get(i).getNomColonne().toString());
      }

      final Iterator<List<Object>> itRow = matriceAffichable.iterator();
      while(itRow.hasNext()){
         cptRow++;
         short cptColumn = 0;
         final Row row = sheet.createRow(cptRow);

         final List<Object> rowAffichable = itRow.next();
         final Iterator<Object> itCell = rowAffichable.iterator();
         while(itCell.hasNext()){
            final Cell cell = row.createCell(cptColumn);
            Object oCell = itCell.next();
            if(oCell == null){
               oCell = new String("-");
            }
            cell.setCellValue(oCell.toString());
            cptColumn++;
         }

      }

      return wb;
   }
}
