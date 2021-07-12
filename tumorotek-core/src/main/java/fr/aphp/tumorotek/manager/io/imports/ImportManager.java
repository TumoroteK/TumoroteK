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
package fr.aphp.tumorotek.manager.io.imports;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import fr.aphp.tumorotek.manager.exception.HeaderException;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.impl.io.imports.DerivesImportBatches;
import fr.aphp.tumorotek.manager.io.TKAnnotableObjectDuo;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.13.2
 */
public interface ImportManager
{

   /**
    * Récupère le contenu d'une cellule.
    * @param cell HSSFCell.
    * @param isDate
    * @param evaluator si Formula
    * @return Le contenu de la cellule.
    * @version 2.0.10.6
    */
   String getCellContent(Cell cell, boolean isDate, FormulaEvaluator evaluator);

   /**
    * Initialise une Hashtable contenant l'indice de chaque colonne en
    * fonction de son header.
    * @param file Fichier à importer.
    * @return Hashtable.
    * @throws HeaderException 
    */
   Hashtable<String, Integer> initColumnsHeadersManager(Row row) throws HeaderException;

   /**
    * Initialise une hashtable contenant pour chaque entité à importer,
    * les ImportColonne correspondantes. Vérifie que pour chaque 
    * ImportColonne, une colonne associée existe bien.
    * @param colonnes Colonnes issues du fichier.
    * @param importTemplate ImportTemplate.
    * @return Hashtable<Entite, List<ImportColonne>>.
    */
   Hashtable<Entite, List<ImportColonne>> initImportColonnesManager(Hashtable<String, Integer> colonnes,
      ImportTemplate importTemplate);

   /**
    * Extrait toutes les valeurs d'un thésaurus et les met dans 1
    * hashtable : <valeur, Objet>.
    * @param champEntite Champ thésaurus.
    * @param banque
    * @return Hashtable.
    */
   Hashtable<String, Object> extractValuesForOneThesaurus(ChampEntite champEntite, Banque banque);

   /**
    * Extrait toutes les valeurs d'un thésaurus et les met dans 1
    * hashtable : <valeur, Objet>.
    * @param champAnnotation Champ thésaurus.
    * @return Hashtable.
    */
   Hashtable<String, Object> extractValuesForOneAnnotationThesaurus(ChampAnnotation champAnnotation, Banque banque);

   /**
    * Extrait toutes les valeurs de tous les thésaurus d'un ImportTemplate
    * et les place dans une Hashtable <Entite|ChampEntite (si non conformites), Valeurs>.
    * @param importTemplate ImportTemplate.
    * @return Hashtable.
    */
   Hashtable<Object, Hashtable<String, Object>> generateThesaurusHashtable(ImportTemplate importTemplate);

   /**
    * Extrait toutes les valeurs de tous les thésaurus d'annotations
    * d'un ImportTemplate et les place dans une 
    * Hashtable <ChampAnnotation, Valeurs>.
    * @param importTemplate ImportTemplate.
    * @return Hashtable.
    */
   Hashtable<ChampAnnotation, Hashtable<String, Object>> generateAnnotationsThesaurusHashtable(ImportTemplate importTemplate);

   /**
    * Set la valeur passée en paramètre à l'objet.
    * @param value Valeur à fixer.
    * @param attibut Attribut à remplir.
    * @param obj Objet.
    */
   void setPropertyValueForObject(Object value, ChampEntite attribut, Object obj, ImportColonne colonne);

   /**
    * Set la valeur passée en paramètre à l'AnnotationValeur.
    * @param value Valeur à fixer.
    * @param annotation ChampAnnotation à remplir.
    * @param annoValeur AnnotationValeur.
    */
   void setPropertyValueForAnnotationValeur(Object value, ChampAnnotation annotation, AnnotationValeur annoValeur,
      ImportColonne colonne);

   /**
    * Récupère la valeur pour la colonne dans le fichier et la set à
    * l'objet.
    * @param obj Objet à enregistrer.
    * @param colonne Colonne actuelle.
    * @param row Ligne actuelle.
    * @param properties Objet contenant les variables globales
    * de l'import.
    */
   void setPropertyForImportColonne(Object obj, ImportColonne colonne, Row row, ImportProperties properties);

   /**
    * Extrait la valeur d'un code et l'assigne en tant que code lésionnel
    * ou code organe à l'échantillon.
    * @param echan Echantillon.
    * @param colonne Colonne actuelle.
    * @param row Ligne actuelle.
    * @param properties Objet contenant les variables globales
    * de l'import.
    * @return True si un code a été assigné.
    */
   boolean setCodeAssigneForEchantillon(Echantillon echan, ImportColonne colonne, Row row, ImportProperties properties);

   /**
    * Extrait les risques (séparés par des ',') et les assigne 
    * au prélèvement.
    * @param prlvt Prelevement.
    * @param colonne Colonne actuelle.
    * @param row Ligne actuelle.
    * @param properties Objet contenant les variables globales
    * de l'import.
    * @return True si des risques ont été assignés.
    */
   boolean setRisquesForPrelevement(Prelevement prlvt, ImportColonne colonne, Row row, ImportProperties properties);

   /**
    * Récupère la ou les valeur(s) (thesaurusM depuis 2.0.13)
    * pour la colonne dans le fichier et la set à l'AnnotationValeur ou 
    * à la liste d'AnnotationValeur.
    * @param colonne Colonne actuelle.
    * @param row Ligne actuelle.
    * @param properties Objet contenant les variables globales
    * de l'import.
    * @return List<AnnotationValeur>
    * @version 2.0.13.2
    */
   List<AnnotationValeur> setPropertyForAnnotationColonne(ImportColonne colonne, Row row, ImportProperties properties);

   /**
    * Set tous les attributs d'un patient.
    * @param row Ligne courante.
    * @param properties properties Objet contenant les variables globales
    * de l'import.
    * @return TKAnnotableObjectDuo duos de patients.
    */
   TKAnnotableObjectDuo setAllPropertiesForPatient(Row row, ImportProperties properties);

   /**
    * Set tous les attributs d'une maladie.
    * @param row Ligne courante.
    * @param properties properties Objet contenant les variables globales
    * de l'import.
    * @param patient Patient de la maladie.
    * @return Maladie.
    */
   Maladie setAllPropertiesForMaladie(Row row, ImportProperties properties, Patient patient);

   /**
    * Set tous les attributs d'un prélèvement.
    * @param row Ligne courante.
    * @param properties properties Objet contenant les variables globales
    * de l'import.
    * @param maladie Maladie du prélèvement.
    * @return TKAnnotableObjectDuo duos de prelevement.
    */
   TKAnnotableObjectDuo setAllPropertiesForPrelevement(Row row, ImportProperties properties, Maladie maladie);

   /**
    * Set tous les attributs d'un échantillon.
    * @param row Ligne courante.
    * @param properties properties Objet contenant les variables globales
    * de l'import.
    * @param prlvt Prélèvement de l'échantillon.
    * @return Echantillon.
    */
   Echantillon setAllPropertiesForEchantillon(Row row, ImportProperties properties, Prelevement prlvt);

   /**
    * Set tous les attributs d'un produit dérivé.
    * @param row Ligne courante.
    * @param properties properties Objet contenant les variables globales
    * de l'import.
    * @return ProdDerive.
    */
   ProdDerive setAllPropertiesForProdDerive(Row row, ImportProperties properties);

   /**
    * Sauvegarde les objets issus de l'import pour la ligne du fichier tabulé 
    * en cours de lecture.
    * @param Row row current row editor
    * @param objects Objets à mettre en base.
    * @param liste importation à laquelle ajoutée objet
    * @param utilisateur
    * @param import properties
    * @param liste des batches de dérivés à créer à la fin de l'import
    * @since 2.0.10.6
    * @param jdbcSuite contenant les ids et statements permettant 
    * la creation des objets en full JDBC
    * @version 2.0.10.6
    * @return 
    */
   void saveObjectsRowManager(Row row, List<Object> objects, List<Importation> importations, Utilisateur utilisateur,
      ImportProperties properties, EchantillonJdbcSuite jdbcSuite, List<DerivesImportBatches> derivesBatches);

   /**
    * Importe les objets contenus dans le fichier.
    * @param file Fichier d'import.
    * @param importTemplate Template d'import.
    * @param utilisateur Utilisateur.
    * @return ImportHistorique
    */
   ImportHistorique importFileManager(ImportTemplate importTemplate, Utilisateur utilisateur, InputStream fis);

   /**
    * Extrait toutes les valeurs de la première colonne et les place
    * dans une liste de Strings.
    * @param fis
    * @param boolean fixNulls, récupère les nulls si false
    * @return
    */
   List<String> extractListOfStringFromExcelFile(InputStream fis, boolean fixNulls);

   /**
    * Surcharge de la methode importFileManaer pour passer le contenu 
    * du fichier à importer sous la forme d'une Sheet
    * @param importTemplate
    * @param utilisateur
    * @param wb HSSFSheet or XSSFSheet
    * @return ImportHistorique
    * @version 2.0.10.6
    */
   ImportHistorique importFileManager(ImportTemplate importTemplate, Utilisateur utilisateur, Sheet sheet);

   /**
    * Extrait les non conformites (séparés par des ';') et prepare la liste.
    * @param prlvt Prelevement.
    * @param colonne Colonne actuelle.
    * @param row Ligne actuelle.
    * @param properties Objet contenant les variables globales
    * de l'import.
    * @param peuple une liste de non conformites par ligne
    * @return les non conformites
    */
   List<NonConformite> setNonConformites(TKAnnotableObject obj, ImportColonne colonne, Row row, ImportProperties properties,
      Map<TKAnnotableObject, List<NonConformite>> ncfsList);

   Map<TKAnnotableObject, List<NonConformite>> getNcfsPrelevement();

   Map<TKAnnotableObject, List<NonConformite>> getNcfsEchanTrait();

   Map<TKAnnotableObject, List<NonConformite>> getNcfsEchanCess();

   Map<TKAnnotableObject, List<NonConformite>> getNcfsDeriveTrait();

   Map<TKAnnotableObject, List<NonConformite>> getNcfsDeriveCess();

   /**
    * Import spécifique de dérivés de dérivés (1ere génération) à 
    * partir d'une Sheet spéciale, qui doit contenir a minima
    * les colonnes clefs naturelles des dérivés parents, et les colonnes 
    * permettant la créatien des dérivés enfants.
    * @param importTemplate
    * @param utilisateur
    * @param wb HSSFSheet or XSSFSheet
    * @param String texte (internationalisé) utilisé pour observations 
    * de l'évènement de stockage automatique EPUISEMENT du parent transformé.
    * @return ImportHistorique
    * @version 2.0.11
    */
   ImportHistorique importSubDeriveFileManager(ImportTemplate importTemplate, Utilisateur utilisateur, Sheet sheet,
      String retourTransfoEpuisement);

   /**
    * Ajoute le derive contenu dans la ligne 
    * à un batches de dérivés existants, ou créé le batch 
    * si il n'existe pas. Un batch est défini par le parent de la transformation, 
    * la quantité utilisée, et la date de sortie du parent avant transformation 
    * (date utilisée pour crée l'évènement d'épuisement du parent, ou la complétion 
    * de l'évènement ENCOURS si le parent est un TKStockableObject).
    * @param row ligne fichier Import
    * @param derive ProdDerive à ajouter au batch
    * @param batches liste de batches actuellement disponibles
    * @param parent matériel à l'origine de la transformation
    * @param transfoQte quantité transformée
    * @param dateSortie date de l'évènement de stockahe
    * @param observations observations éventuelles à ajouter à l'évènement 
    * @param properties ImportProperties
    * @since 2.0.12
    * @version 2.0.12
    */
   void addToDeriveBatchesManager(Row row, ProdDerive derive, List<DerivesImportBatches> batches, TKAnnotableObject parent,
      Float transfoQte, Calendar dateSortie, String observations, ImportProperties properties);

   /**
    * Persiste les dérivés par lots, à partir des batches composés à la 
    * lecture du fichier d'importation.
    * @param batches liste de DeriveImportBatches
    * @param properties ImportPropertries
    * @param importations liste d'importation à laquelle ajouté les enregistrements
    * @param utilisateur Utilisateur en charge de la création
    * @param baseDir 
    * @param errors liste d'errors à compléter le cas échéant.
    * @since 2.0.12
    * @version 2.0.12
    */
   void saveDeriveBatchesManager(List<DerivesImportBatches> batches, ImportProperties properties, List<Importation> importations,
      Utilisateur utilisateur, String baseDir, List<ImportError> errors);

}
