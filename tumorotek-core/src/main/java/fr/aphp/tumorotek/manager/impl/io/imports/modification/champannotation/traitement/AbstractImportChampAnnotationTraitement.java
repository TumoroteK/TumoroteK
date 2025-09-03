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
package fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.traitement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManagerFactory;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.manager.exception.AbstractImportCellScopeException;
import fr.aphp.tumorotek.manager.exception.ImportDefaultWrongImportValueException;
import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.ImportChampAnnotationInfo;
import fr.aphp.tumorotek.manager.io.imports.modification.champannotation.ImportChampAnnotationReader;
import fr.aphp.tumorotek.manager.io.imports.modification.champannotation.ImportChampAnnotationTraitement;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.utils.io.ExcelIllegalContentTypeException;

/**
 * implémentation qui contient toute la partie commune à tous les types de champ d'annotation
 * le mécanisme générique s'appuie sur la méthode getAnnotationValeurInfoColonne() qui sera définie dans chaque classe fille
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public abstract class AbstractImportChampAnnotationTraitement implements ImportChampAnnotationTraitement
{
   private final Logger log = LoggerFactory.getLogger(ImportChampAnnotationTraitementForAlphanum.class);

   private ImportChampAnnotationReader importChampAnnotationReader;
   
   private Integer champAnnotationId;

   //index de la colonne dans le fichier d'import (0 pour la 1ere colonne) pour être sur la même base que l'API POI
   private int indexColonneInFile;
   //ImportColonne concerné par le traitement
   private ImportColonne importColonne;
   
   //principalement utilisé pour récupérer les valeurs de thesaurus autorisées
   private EntityManagerFactory entityManagerFactory;

   //requête générique pour les valeurs existantes du champ d'annotation associé à l'instance de la classe pour une liste d'objets "matériel biologique"
   protected final String sqlSelectRequete = "SELECT ANNOTATION_VALEUR_ID, CHAMP_ANNOTATION_ID, OBJET_ID, %s "
      +" FROM ANNOTATION_VALEUR where CHAMP_ANNOTATION_ID = ? and OBJET_ID in (%s)";
   
   //requête générique pour insérer une valeur dans le champ d'annotation associé à l'instance de la classe 
   protected final String sqlInsertRequete = 
      "INSERT into ANNOTATION_VALEUR (CHAMP_ANNOTATION_ID, OBJET_ID, ALPHANUM, TEXTE, ANNO_DATE, BOOL, ITEM_ID, BANQUE_ID) "
                           + "values (?, ?, ?, ?, ?, ?, ?, ?)"; 
   
   //requête générique pour mettre à jour une valeur du champ d'annotation associé à l'instance de la classe pour un objet "matériel biologique"
   private final String sqlUpdateRequete = "UPDATE ANNOTATION_VALEUR set %s = ? WHERE ANNOTATION_VALEUR_ID = ?";

   private List<ImportChampAnnotationInfo> listImportInfo = new ArrayList<ImportChampAnnotationInfo>(); 
   
   @Override
   public List<ImportChampAnnotationInfo> getListImportInfo(){
      return listImportInfo;
   }
   protected Integer getChampAnnotationId(){
      return champAnnotationId;
   }
   protected void setChampAnnotationId(Integer champAnnotationId){
      this.champAnnotationId = champAnnotationId;
   }
   
   protected EntityManagerFactory getEntityManagerFactory(){
      return entityManagerFactory;
   }
   protected void setEntityManagerFactory(EntityManagerFactory entityManagerFactory){
      this.entityManagerFactory = entityManagerFactory;
   }
   
   @Override
   public int getIndexColonneInFile(){
      return indexColonneInFile;
   }
   protected void setIndexColonneInFile(int indexColonneInFile){
      this.indexColonneInFile = indexColonneInFile;
   }
   
   @Override
   public ImportColonne getImportColonne(){
      return importColonne;
   }
   protected void setImportColonne(ImportColonne importColonne){
      this.importColonne = importColonne;
   }
   
   protected ImportChampAnnotationReader getImportChampAnnotationReader(){
      return importChampAnnotationReader;
   }
   protected void setImportChampAnnotationReader(ImportChampAnnotationReader importChampAnnotationReader){
      this.importChampAnnotationReader = importChampAnnotationReader;
   }
   
   @Override
   public void readCellForAnnotationValeur(Cell cell, FormulaEvaluator formulaEvaluator, Integer objetId, int numRow) throws AbstractImportCellScopeException {
      try {
         Object cellContent = importChampAnnotationReader.read(cell, formulaEvaluator);
         //il peut être nécessaire de convertir le contenu de la cellule dans le format attendu par la table ANNOTATION_VALEUR
         //exemple : les nombres stockés dans un champ varchar et les thesaurus pour lesquels il faut l'id associé
         Object valeurAPrendreEnCompte = convertCellContent(cellContent);
         if(cellContent != null) {
            listImportInfo.add(new ImportChampAnnotationInfo(champAnnotationId, objetId, valeurAPrendreEnCompte, numRow));
         }
         else {
            //la cellule est vide, on ne fait rien
//            //cas qui ne doit jamais se produire : sécurisation avec un traitement "basique"
//            throw new DefaultWrongImportValueException(getImportColonne(), "");
         }
      }
      catch (ExcelIllegalContentTypeException excelIllegalContentTypeException) {
         throw new ImportDefaultWrongImportValueException(getImportColonne(), excelIllegalContentTypeException.getTypeAttendu());
      }
   }
 
   //par défaut ne fait rien => renvoie l'objet passé en paramètre
   protected Object convertCellContent(Object cellContent) throws AbstractImportCellScopeException {
      return cellContent;
   }
   
   @Override
   public void prepareSelectCall(PreparedStatement preparedStatement) throws SQLException {
      //le 1er paramètre est champAnnotationId, les suivants sont les objetId
      preparedStatement.setInt(1, champAnnotationId);
      List<ImportChampAnnotationInfo> listChampAnnotationInfoWithoutDoublon = getListImportInfoWithoutDoublon().collect(Collectors.toList());
      int nbObjectId = listChampAnnotationInfoWithoutDoublon.size();
      for (int i = 0; i < nbObjectId; i++) {
         preparedStatement.setInt(i+2, listChampAnnotationInfoWithoutDoublon.get(i).getObjetId());
      }
   }
   
   /**
    * met à jour les ImportChampAnnotationInfo avec les données récupérées en base de données pour le champ en cours de traitement
    */
   @Override
   public void manageSelectResultSet(ResultSet resultSet) throws SQLException {
      if(resultSet != null) {
         while(resultSet.next()) {
            Integer annotationValeurId = resultSet.getInt(1);
            Integer champAnnotationId = resultSet.getInt(2);
            Integer objetId = resultSet.getInt(3);
            
            //récupération de l'objet correspondant dans la liste listImportInfo pour le mettre à jour
            List<ImportChampAnnotationInfo> listFiltree = listImportInfo.stream().filter(importInfo -> importInfo.getChampAnnotationId().equals(champAnnotationId) && importInfo.getObjetId().equals(objetId))
            .collect(Collectors.toList());
            listFiltree.get(0).setOldValeur(resultSet.getObject(4));
            listFiltree.get(0).setAnnotationValeurId(annotationValeurId);
            
         }
         //suppression des cas de demande de mise à jour où la nouvelle valeur est la même que l'ancienne
         listImportInfo.removeIf(importInfo -> importInfo.isUpdate() && importInfo.getNewValeur().equals(importInfo.getOldValeur()));
      }
   }

   @Override
   abstract public String convertValeurAsString(Object valeur); 
   
   /**
    * retourne la position de EAnnotationValeurColonneForData
    * @return
    */
   protected int getPositionInInsertQuery() {
      return getAnnotationValeurInfoColonne().getPositionInInsertQuery();
   }
   
   /**
    * retourne le nom de la colonne associé au traitement
    * @return
    */
   protected String getNomColonne() {
      return getAnnotationValeurInfoColonne().getAnnotationValeurColonneForData().toString();
   }
   
  
   //construction de la requête de SELECT en remplaçant le 1e token avec le nom de la colonne et le deuxième avec autant de ?
   //qu'il y a de champs d'annotation à valoriser pour ce type d'annotation.
   @Override
   public String getSqlSelectRequete(){
      String result = String.format(sqlSelectRequete, getNomColonne(),
         getListImportInfoWithoutDoublon().map(importInfo -> "?").collect(Collectors.joining(", ")));
      log.debug("sqlSelectRequete : {}", result);
      
      return result;
   }

   //par défaut, la liste listImportInfo n'a qu'un élément pour un objet TK et un champ d'annotation
   //mais dans le cas du thesaurusM, il peut y avoir plusieurs éléments dans la liste.
   //cette méthode sera donc surchargée dans ce cas pour supprimer les doublon
   protected Stream<ImportChampAnnotationInfo> getListImportInfoWithoutDoublon() {
      return listImportInfo.stream();
   }
   
   @Override
   public String getSqlInsertRequete(){
      return sqlInsertRequete;
   }

   @Override
   public String getSqlUpdateRequete(){
      return String.format(sqlUpdateRequete, getNomColonne());
   }

   @Override
   public void prepareInsertAndUpdateCall(PreparedStatement pstForInsert, PreparedStatement pstForUpdate, Integer banqueId) throws SQLException {
//      //tout d'abord, suppression des cas de demande de mise à jour où la nouvelle valeur est la même que l'ancienne
//      listImportInfo.removeIf(importInfo -> importInfo.isUpdate() && importInfo.getNewValeur().equals(importInfo.getOldValeur()));
      
      for(ImportChampAnnotationInfo importInfo : listImportInfo) {
         if(importInfo.isUpdate()) {
            pstForUpdate.setObject(1, importInfo.getNewValeur());
            pstForUpdate.setInt(2, importInfo.getAnnotationValeurId());
            
            pstForUpdate.addBatch();
         }
         else {//cas de la création
            prepareInsertCallForThisImportChampAnnotationInfo(importInfo, pstForInsert, banqueId);
         }
      }
   }

   protected void prepareInsertCallForThisImportChampAnnotationInfo(ImportChampAnnotationInfo importInfo, PreparedStatement pstForInsert, Integer banqueId) throws SQLException{
      for (int i = 0; i < 8; i++) {
         pstForInsert.setObject(i + 1, null);
      }
      pstForInsert.setInt(1, importInfo.getChampAnnotationId());
      pstForInsert.setInt(2, importInfo.getObjetId());
      pstForInsert.setObject(getPositionInInsertQuery(), importInfo.getNewValeur());
      pstForInsert.setInt(8, banqueId);
      
      pstForInsert.addBatch();
   }
   
   @Override
   public boolean isEmpty() {
      return listImportInfo.isEmpty();
   }
   
   @Override
   public void clear() {
      listImportInfo.clear();
   }
   
   @Override
   public Set<Integer> retrieveObjetIdTraite() {
      Set<Integer> result = new HashSet<Integer>();
      for(ImportChampAnnotationInfo importChampAnnotationInfo : getListImportInfo()) {
         result.add(importChampAnnotationInfo.getObjetId());
      }
      
      return result;
   }
}
