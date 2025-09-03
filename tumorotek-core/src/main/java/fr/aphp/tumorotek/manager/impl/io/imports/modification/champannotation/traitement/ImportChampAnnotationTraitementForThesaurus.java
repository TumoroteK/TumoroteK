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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.manager.exception.AbstractImportCellScopeException;
import fr.aphp.tumorotek.manager.exception.ImportDefaultWrongImportValueException;
import fr.aphp.tumorotek.manager.exception.WrongImportValueForThesaurusException;
import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.EAnnotationValeurInfoColonne;
import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.ImportChampAnnotationInfo;
import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.reader.ImportChampAnnotationReaderFactory;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.utils.io.ExcelIllegalContentTypeException;

/**
 * traitement d'import qui correspond à un champ d'annotation dont la valeur est stockée dans le champ ITEM_ID de la table ANNOTATION_VALEUR
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportChampAnnotationTraitementForThesaurus extends AbstractImportChampAnnotationTraitement
{

   private final Logger log = LoggerFactory.getLogger(ImportChampAnnotationTraitementForThesaurus.class);
   
   private static final EAnnotationValeurInfoColonne ANNOTATION_VALEUR__INFO_COLONNE = EAnnotationValeurInfoColonne.INFO_ITEM_ID;

   
   //map qui contient en clé les valeurs autorisées et en valeur l'id de l'item associé à la valeur
   private CaseInsensitiveMap<String, Integer> mapValeursAutoriseesByLibelle;
   private Map<Integer, String> mapValeursAutoriseesByItemId;

   public ImportChampAnnotationTraitementForThesaurus(Integer champAnnotationId, int indexColonne, ImportColonne importColonne, EntityManagerFactory entityManagerFactory) {
      setChampAnnotationId(champAnnotationId);
      setIndexColonneInFile(indexColonne);
      setImportColonne(importColonne);
      setEntityManagerFactory(entityManagerFactory);
      setImportChampAnnotationReader(ImportChampAnnotationReaderFactory.getDefaultReader());
   }
   
   @Override
   protected Object convertCellContent(Object cellContent) throws AbstractImportCellScopeException {
      if(cellContent != null) {
         //vérification que la chaine récupérer est valide
         if(mapValeursAutoriseesByLibelle == null) {
            initMapValeursAutorisees();
         }
         return retrieveItemIdFromLibelle((String)cellContent);
      }
      
      return null;
   }
   
   protected Integer retrieveItemIdFromLibelle(String libelle) throws WrongImportValueForThesaurusException {
      Integer itemIdForLibelle = mapValeursAutoriseesByLibelle.get(libelle);
      if(itemIdForLibelle != null) {
         return itemIdForLibelle;
      }
      throw new WrongImportValueForThesaurusException(getImportColonne(), mapValeursAutoriseesByLibelle.keySet());
   }
   
   @Override
   public EAnnotationValeurInfoColonne getAnnotationValeurInfoColonne() {
      return ANNOTATION_VALEUR__INFO_COLONNE;
   }
   
   @Override
   public String convertValeurAsString(Object valeur) {
      //cas particulier car l'objet transmis est un id (item id) alors que la valeur qui a été lue est une String. Il faut donc faire une conversion "fonctionnelle"
      String libelle = mapValeursAutoriseesByItemId.get((Integer)valeur);
      if(libelle == null) {
         return "";
      }
      return libelle;
   }
   

   protected CaseInsensitiveMap<String, Integer> getMapValeursAutoriseesByLibelle(){
      return mapValeursAutoriseesByLibelle;
   }
   
   private void initMapValeursAutorisees() {
      EntityManager entityManager = null;
      try {
         entityManager = getEntityManagerFactory().createEntityManager();
         TypedQuery<Item> query = entityManager.createNamedQuery("Item.findByChampAnnotationId", Item.class);
         List<Item> listItem = query.setParameter(1, getChampAnnotationId()).getResultList();
         mapValeursAutoriseesByLibelle = new CaseInsensitiveMap<String, Integer>();
         mapValeursAutoriseesByItemId = new HashMap<Integer, String>();
         for(Item item :listItem) {
            mapValeursAutoriseesByLibelle.put(item.getLabel(), item.getItemId());
            mapValeursAutoriseesByItemId.put(item.getItemId(), item.getLabel());
         }  
      }
      finally {
         entityManager.close();
      }
   }
   
}
