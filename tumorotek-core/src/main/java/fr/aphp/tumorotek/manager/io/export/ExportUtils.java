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
package fr.aphp.tumorotek.manager.io.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public interface ExportUtils
{

   /**
    * Crée un document excell.
    * @param name Nom du document.
    * @return HSSFWorkbook.
    */
   HSSFWorkbook createExcellWorkBook(String name);

   /**
    * Ajoute une cellule à la ligne passée en paramètre.
    * @param indCell Index de la cellule à rajouter.
    * @param row Ligne courante.
    * @param value Valeur à mettre dans la cellule.
    * @return L'index qui suit la cellule venant d'être rajoutée.
    */
   int addCell(int indCell, HSSFRow row, Object value);

   /**
    * Ajoute une nouvelle ligne au HSSFSheet.
    * @param sheet HSSFSheet.
    * @param indCell Indice de la 1ere cellule de la ligne.
    * @param indRow Numéro de la ligne à rajouter.
    * @param values Valeurs à mettre dans la ligne.
    * @return HSSFSheet.
    */
   HSSFSheet addDataToRow(HSSFSheet sheet, int indCell, int indRow, List<? extends Object> values);

   /**
    * Ajoute les données d'un patient à la ligne passée en paramètre.
    * @param row Ligne où l'on va rajouter les données du patient.
    * @param wb Workbook contenant les styles de date
    * @param indCell Indice de la cellule de départ.
    * @param patient Patient.
    * @param champAnnotations Annotations à faire apparaître.
    * @param isAnnonyme True si l'export est anonyme.
    */
   void addMaladieData(HSSFRow row, HSSFWorkbook wb, int indCell, Maladie maladie, List<ChampAnnotation> champAnnotations,
      boolean isAnnonyme, Utilisateur user);

   /**
    * Ajoute les données d'un prlvt à la ligne passée en paramètre.
    * @param row Ligne où l'on va rajouter les données du prlvt.
    * @param wb Workbook contenant les styles de date
    * @param indCell Indice de la cellule de départ.
    * @param prelevement Prelevement.
    * @param champAnnotations Annotations à faire apparaître.
    * @param isAnnonyme True si l'export est anonyme.
    */
   void addPrelevementData(HSSFRow row, HSSFWorkbook wb, int indCell, Prelevement prelevement,
      List<ChampAnnotation> champAnnotations, boolean isAnnonyme, Utilisateur user);

   /**
    * Ajoute les données d'un échantillon à la ligne passée en paramètre.
    * @param row Ligne où l'on va rajouter les données de l'échantillon.
    * @param wb Workbook contenant les styles de date
    * @param indCell Indice de la cellule de départ.
    * @param echantillon Echantillon.
    * @param champAnnotations Annotations à faire apparaître.
    * @param isAnnonyme True si l'export est anonyme.
    */
   void addEchantillonData(HSSFRow row, HSSFWorkbook wb, int indCell, Echantillon echantillon,
      List<ChampAnnotation> champAnnotations, boolean isAnnonyme, Utilisateur user);

   /**
    * Ajoute les données d'un dérivé à la ligne passée en paramètre.
    * @param row Ligne où l'on va rajouter les données du dérivé.
    * @param wb Workbook contenant les styles de date
    * @param indCell Indice de la cellule de départ.
    * @param derive ProdDerive.
    * @param champAnnotations Annotations à faire apparaître.
    * @param isAnnonyme True si l'export est anonyme.
    */
   void addProdDeriveData(HSSFRow row, HSSFWorkbook wb, int indCell, ProdDerive derive, List<ChampAnnotation> champAnnotations,
      boolean isAnnonyme, Utilisateur user);

   /**
    * Ajoute les données d'une cession à la ligne passée en paramètre.
    * @param row Ligne où l'on va rajouter les données de la cession.
    * @param wb Workbook contenant les styles de date
    * @param indCell Indice de la cellule de départ.
    * @param cession Cession.
    * @param champAnnotations Annotations à faire apparaître.
    */
   void addCessionData(HSSFRow row, HSSFWorkbook wb, int indCell, Cession cession, List<ChampAnnotation> champAnnotations,
      Utilisateur user);

   String dateRenderer(Object date);

   String booleanLitteralFormatter(Boolean b);

   /**
    * Ajoute une ligne de texte au write passé en paramètre.
    * Les valeurs sont ajoutées une à une, séparées par fsep et
    * la ligne est terminée par lsep.
    * @param buf writer
    * @param indx index de la colonne à laquelle débuter.
    * @param values
    * @param fsep separateur de champs
    * @param lsep separateur de fin de ligne
    */
   void addDataToRowCSV(BufferedWriter buf, int index, List<String> values, String fsep, String lsep) throws IOException;

   /**
    * Ajoute une cellule à la ligne passée en paramètre.
    * @param indCell Index de la cellule à rajouter.
    * @param row Ligne courante.
    * @param value Valeur à mettre dans la cellule.
    * @param workbook contenant les cell styles
    * @return L'index qui suit la cellule venant d'être rajoutée.
    */
   int addDateCell(int indCell, HSSFRow row, Object value, HSSFWorkbook wb);
}
