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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.manager.exception.AbstractImportCellScopeException;
import fr.aphp.tumorotek.manager.exception.BadFileFormatException;
import fr.aphp.tumorotek.manager.exception.HeaderException;
import fr.aphp.tumorotek.manager.exception.ImportCleFonctionnelleIncoherenteException;
import fr.aphp.tumorotek.manager.exception.ImportCleFonctionnelleInexploitableException;
import fr.aphp.tumorotek.manager.exception.ImportCleFonctionnelleManquanteException;
import fr.aphp.tumorotek.manager.exception.ImportDataControleInexploitableException;
import fr.aphp.tumorotek.manager.exception.ImportDataRowException;
import fr.aphp.tumorotek.manager.exception.ImportDoublonColonneException;
import fr.aphp.tumorotek.manager.exception.ImportDoublonInFileException;
import fr.aphp.tumorotek.manager.exception.ImportEcrasementDonneeWarningForFileException;
import fr.aphp.tumorotek.manager.exception.ImportKeyNotFoundException;
import fr.aphp.tumorotek.manager.exception.ImportKeyPatientNotFoundException;
import fr.aphp.tumorotek.manager.exception.ImportPrerequisitesException;
import fr.aphp.tumorotek.manager.exception.ImportTransactionKOException;
import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.entitestrategy.ImportChampAnnotationEntiteStrategyFactory;
import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.traitement.ImportChampAnnotationTraitementFactory;
import fr.aphp.tumorotek.manager.io.imports.ImportBatchProcessor;
import fr.aphp.tumorotek.manager.io.imports.ImportCellError;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.io.imports.ImportRowError;
import fr.aphp.tumorotek.manager.io.imports.ImportTemplateManager;
import fr.aphp.tumorotek.manager.io.imports.modification.champannotation.ImportChampAnnotationEntiteStrategy;
import fr.aphp.tumorotek.manager.io.imports.modification.champannotation.ImportChampAnnotationTraitement;
import fr.aphp.tumorotek.model.CodeIdPair;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.io.imports.EImportationType;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.qualite.EOperationTypeId;
import fr.aphp.tumorotek.model.systeme.EEntiteId;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.io.ExcelIllegalContentTypeException;
import fr.aphp.tumorotek.utils.io.ExcelUtility;

/**
 * gère le traitement des imports pour modifier les champs d'annotation d'objets existants
 * Celui-ci utilise des requêtes de type "batch" pour optimiser le traitement
 * 
 * @since 2.3.1.0 (TK-258)
 * @author chuet
 *
 */
public class ImportChampAnnotationBatchProcessor implements ImportBatchProcessor
{
   private static final String SQL_INSERT_IMPORTATION = "INSERT into IMPORTATION (OBJET_ID, ENTITE_ID, IMPORT_HISTORIQUE_ID, TYPE_CODE) "
      + "values (?, ?, ?, ?)"; 
   
   private static final String SQL_INSERT_OPERATION = "INSERT into OPERATION (UTILISATEUR_ID, DATE_, OBJET_ID, OPERATION_TYPE_ID, ENTITE_ID) "
      + "values (?, ?, ?, ?, ?)";
   
   private final Logger log = LoggerFactory.getLogger(ImportChampAnnotationBatchProcessor.class);
   
   private EntityManagerFactory entityManagerFactory;
   
   private ImportTemplateManager importTemplateManager;
   private ImportHistoriqueManager importHistoriqueManager;

   private ImportChampAnnotationEntiteStrategyFactory importChampAnnotationEntiteStrategyFactory;

   public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory){
      this.entityManagerFactory = entityManagerFactory;
   }
   
   public void setImportTemplateManager(ImportTemplateManager importTemplateManager){
      this.importTemplateManager = importTemplateManager;
   }
   public void setImportHistoriqueManager(ImportHistoriqueManager importHistoriqueManager){
      this.importHistoriqueManager = importHistoriqueManager;
   }
   
   public void setImportChampAnnotationEntiteStrategyFactory(
      ImportChampAnnotationEntiteStrategyFactory importChampAnnotationEntiteStrategyFactory){
      this.importChampAnnotationEntiteStrategyFactory = importChampAnnotationEntiteStrategyFactory;
   }

   
   /**
    * le principe du traitement est de gérer toutes les créations et mises à jour d'annotation par batchs :
    * - 1 batch d'insert par champ de la table ANNOTATION_VALEUR à renseigner (quelque soit le champ d'annotation)
    * - 1 batch de mise à jour par champ de la table ANNOTATION_VALEUR à renseigner (quelque soit le champ d'annotation)
    * le traitement se fait par lots de lignes de données. La taille de ceux dépendra du nombre de champs d'annotation à traiter 
    * 
    * Le traitement s'appuie sur le principe de polymorphisme. Les spécificités liées à chaque champ de la table ANNOTATION_VALEUR
    * seront définies dans des classes implémentant {@link ImportChampAnnotationTraitement}
    * 
    * Le traitement se décompose en plusieurs étapes :
    *    - contrôles sur les prerequis
    *    - préparation des objets ImportChampAnnotationTraitement liés aux champs d'annotation concernés par l'import
    *    - lecture du fichier et alimentation des objets ImportChampAnnotationTraitement
    *    - si aucune erreur n'est détectée, appel des requêtes massives associées à chaque ImportChampAnnotationTraitement
    */
   @Override
   public ImportHistorique process(ImportTemplate importTemplate, Utilisateur utilisateur, Banque banque, EContexte eContexte, Sheet sheet, boolean modeSousControle) throws ImportPrerequisitesException, ImportDoublonInFileException, ImportKeyNotFoundException, ImportDataRowException, ImportEcrasementDonneeWarningForFileException, 
            ImportCleFonctionnelleInexploitableException, ImportCleFonctionnelleManquanteException, ImportCleFonctionnelleIncoherenteException, ImportDataControleInexploitableException, ImportTransactionKOException {
      log.debug("Début du traitement de mise à jour des annotation (méthode process) pour l'importTemplate {} de la banque {}", importTemplate, banque);
      ImportHistorique historique = null;
      int nbLotsTraites = 0;
      
      Integer rowNum = 0;
      Iterator<Row> rit;
      Row row = null;

      EntityManager entityManager = null;
      
      try{
         // on se positionne sur la première ligne
         rit = sheet.rowIterator();
         row = rit.next();
         rowNum++;
         //----------------------------------------------
         // Validation des prérequis : renvoie d'exception en cas de validation KO
         //----------------------------------------------
         log.debug("Début de la validation des prérequis");

         InfoColonnes infoColonnes = validateFileFormat(row, importTemplate);//renvoie une exception fille de ImportPrerequisitesException si contrôle KO

         final Set<Entite> entites = importTemplateManager.getEntiteManager(importTemplate);
         EEntiteId entiteIdOfImport = EEntiteId.findById(entites.iterator().next().getEntiteId());
         
         List<CodeIdPair> listCodeExistant = checkClesFonctionnelles(infoColonnes, rit, rowNum, entiteIdOfImport, banque, eContexte);
         //----------------------------------------------
         // fin de la validation des prérequis  --------------
         //----------------------------------------------
         
         //le nombre de ligne du fichier est le nombre de codes à traiter plus l'entête
         int nbLignesDansFichier = listCodeExistant.size() + 1;
         
         //----------------------------------------------
         //Préparation des objets de traitement :
         //----------------------------------------------
         //ImportChampAnnotationTraitement est une interface qui permet de regrouper le traitement de toutes les colonnes du fichier ayant le même type (alphanum, date, ...) car
         //les valeurs de ces colonnes sont stockées dans le même champ de ANNOTATION_VALEUR
         //Une implémentation est donc définie par type ce qui permet de regrouper les spécificités liées au type du champ d'annotation. Et une instance 
         //de ImportChampAnnotationTraitement sera créée pour chaque champ d'annotation à gérer.
         //Pour faciliter le traitement, ces instances seront stockées dans une liste "listImportTraitement" mais également dans une map les regroupant "par type" : "mapImportChampAnnotationTraitementByType"
         //les clés de la map seront les valeurs de l'enum associée aux colonnes contenant les valeurs dans annotationValeur
         //et les valeurs les listes des instances des filles de ImportChampAnnotationTraitement.
         //Cela permettra de fusionner les ImportChampAnnotationTraitement d'une même clé pour faire un traitement de masse par "type" de valeur
         List<ImportChampAnnotationTraitement> listImportTraitement = new ArrayList<ImportChampAnnotationTraitement>();
         //map des ImportChampAnnotationTraitement par type : le type (clé) est le nom de la classe
          EnumMap<EAnnotationValeurColonneForData, List<ImportChampAnnotationTraitement>> mapImportChampAnnotationTraitementByType = new EnumMap<EAnnotationValeurColonneForData, List<ImportChampAnnotationTraitement>>(EAnnotationValeurColonneForData.class);
         //préparation des objets ImportChampAnnotationTraitement dédiés à chaque champ d'annotation à traiter
         for(ImportColonne importColonne : infoColonnes.getListImportColonne()) {
            ChampAnnotation champAnnotation = importColonne.getChamp().getChampAnnotation();
            //on ne prend pas en compte la première colonne qui est le code de l'entité
            if(champAnnotation != null) {
               ImportChampAnnotationTraitement importChampAnnotationTraitement = ImportChampAnnotationTraitementFactory.createImportChampAnnotationTraitementFor(
                  champAnnotation.getDataType().getType(), 
                  champAnnotation.getId(),
                  infoColonnes.getListNomColonne().indexOf(importColonne.getNom()),
                  importColonne, entityManagerFactory);
               listImportTraitement.add(importChampAnnotationTraitement);
               mapImportChampAnnotationTraitementByType.computeIfAbsent(
                  importChampAnnotationTraitement.getAnnotationValeurInfoColonne().getAnnotationValeurColonneForData(), 
                  key -> new ArrayList<ImportChampAnnotationTraitement>())
               .add(importChampAnnotationTraitement);
            }
         }
         //----------------------------------------------
         //Fin de la préparation des objets de traitement
         //----------------------------------------------
         
         EcrasementChampAnnotationSupervisor ecrasementChampAnnotationSupervisor = new EcrasementChampAnnotationSupervisor(modeSousControle);
         List<ImportRowError> listImportRowErrorForWarningEcrasement = new ArrayList<ImportRowError>();
         
         //----------------------------------------------
         //Lecture du fichier et traitement par lot. Le nombre de lignes par lot dépend du nombre de types de colonnes à traiter
         //----------------------------------------------                  
         FormulaEvaluator formulaEvaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
         //listRowError correspond à une liste contenant toutes les erreurs rencontrées, regroupées par ligne (pour le lot courant). 
         List<ImportRowError> listRowError = new ArrayList<ImportRowError>();
         //liste des objets id traités (pour le lot courant) : nécessaire pour créer les "importations"
         
         entityManager = entityManagerFactory.createEntityManager();
         Session session = entityManager.unwrap(Session.class);
         
         //Définition de la taille de la transaction :
         //Par défaut, lot de 50 lignes pour 5 colonnes. Si plus de colonnes, diminution de la taille du lot
         final int DEFAULT_NB_COLONNE = 5;
         final int DEFAULT_TAILLE_LOT = 100; 
         final int nbLignesPourUneTransaction = Math.round(DEFAULT_TAILLE_LOT/(Math.max(DEFAULT_NB_COLONNE, infoColonnes.getListImportColonne().size())/DEFAULT_NB_COLONNE));
         
         //on reparcourt le fichier pour récupérer les données en sautant la 1ere ligne qui est l'entête :
         int j = 0;
         for(Row row2  : sheet) {
            j++;
            if(j == 1) {
               continue;
            }
           
            //Alimentation des objets ImportChampAnnotationTraitement contenus dans listImportTraitement et dans mapImportChampAnnotationTraitementByType :
            //en lisant pour chaque ligne, la colonne associée à chaque objet
            //puis traitement lorque la taille d'un lot est atteint
            String code = null;
            try {
               code = ExcelUtility.readAlphanumContent(row2.getCell(0));
            }
            catch (ExcelIllegalContentTypeException e) {
               //on ne peut jamais passer dans ce catch car on est sur la 2e lecture des codes.
               //la 1ere a été faite pour les contrôles des prerequis (checkClesFonctionnelles). Donc si on est arrivé à cet endroit du code
               //c'est qu'il n'y a pas de problème sur les codes.
               //On fait donc juste un continue pour sécuriser mais on pourrait laisser vide.
               continue;
            }
            //si le code récupéré est null, on considère qu'on est arrivé à la fin des lignes à traiter
            if(code == null) {
               break;
            }
            //TODO : Si problème de perfs, trier listCodeExistant AVANT LA BOUCLE et utiliser Collections.binarySearch()
            
            Integer objetId = listCodeExistant.get(listCodeExistant.indexOf(new CodeIdPair(code))).getId();
            //importRowError permet de stocker toutes les erreurs rencontrées lors du traitement d'une ligne du fichier
            ImportRowError importRowError = new ImportRowError(j);
            //traitement de chaque colonne de la ligne :
            for (ImportChampAnnotationTraitement importTraitement : listImportTraitement) {
               try {
                  importTraitement.readCellForAnnotationValeur(row2.getCell(importTraitement.getIndexColonneInFile()), formulaEvaluator, objetId, j);
               }
               catch (AbstractImportCellScopeException wrongImportValueExceptionForThesaurus) {
                  importRowError.addImportCellError(new ImportCellError(wrongImportValueExceptionForThesaurus, importTraitement.getIndexColonneInFile()));
               }
            }

            if(!importRowError.isEmpty()) {
               //on l'ajoute à la liste des erreurs par ligne
               listRowError.add(importRowError);
            }
            else {
               //on force à null pour ne pas garder une référence inutile et faciliter le passage du garbage collector
               importRowError = null;
            }
            
            /////si pas d'erreur, on fait le traitement d'import des données par lot : 
            
            //gestion par lot : 
            //j commence à 0 mais est incrémenté au début de la boucle. De plus la première ligne est l'entête donc si on veut traiter nbLignesPourUneTransaction pour une transaction
            //il faut faire les traitements pour % == 1 ou j == nb de lignes dans le fichier
            if(j % nbLignesPourUneTransaction == 1 || j == nbLignesDansFichier) {
               
               if(listRowError.isEmpty()) {
                  //si on est sur le 1er lot : insertion dans la table IMPORT_HISTORIQUE pour récupération de l'id utilisé dans IMPORTATION
                  if(nbLotsTraites == 0) {
                     historique = new ImportHistorique();
                     historique.setImportTemplateId(importTemplate.getImportTemplateId());
                     historique.setImportBanqueId(banque.getBanqueId());
                     historique.setUtilisateur(utilisateur);
                     historique.setDate(Calendar.getInstance());
                     //les importations seront gérées dans un second temps
                     importHistoriqueManager.createObjectManager(historique, utilisateur, null);
                  }
                  
                  //récupération des valeurs existantes :
                  retrieveExistingValues(session, listImportTraitement);
                  List<ImportRowError> listImportRowErrorForWarningEcrasementForThisLot = ecrasementChampAnnotationSupervisor.checkByLot(listImportTraitement, nbLotsTraites*nbLignesPourUneTransaction);
                  //Si il y a des modifications et qu'on est dans l'appel "avec contrôle update", on ne fera pas les mises à jour : 
                  listImportRowErrorForWarningEcrasement.addAll(listImportRowErrorForWarningEcrasementForThisLot);
                  
                  
                  //insert et update par batch
                  //NB : on ne récupère pas l'utilisateur depuis l'objet historique pour éviter de faire une requête en base pour le récupérer alors qu'on l'a déjà :
                  try {
                     //si il n'y a pas de warning lié à l'écrasement d'une valeur existante, on fait le traitement de mise à jour
                     //sinon on continue à parcourir le fichier pour remonter les éventuels autres cas d'écrasement. A la fin de la lecture du fichier, une exception sera envoyée pour remonter l'information à l'utilisateur
                     if(listImportRowErrorForWarningEcrasement.size() == 0) {
                        doInsertsAndUpdatesInOneTransaction(session, mapImportChampAnnotationTraitementByType, banque, entiteIdOfImport, historique, utilisateur.getUtilisateurId());
                     }
                  }
                  catch (HibernateException hibernateException) {
                     throw new ImportTransactionKOException(new StringBuilder("Erreur lors des requêtes d'import pour le modèle d'import ").append(importTemplate.getNom()).toString(), 
                           hibernateException.getCause(), nbLotsTraites*nbLignesPourUneTransaction);
                  }
                  finally {
                     //nettoyage pour préparer le traitement du prochain lot :
                     //clear des importChampAnnotationTraitement pour préparer le traitement du prochain lot :
                     for (ImportChampAnnotationTraitement importTraitement : listImportTraitement) {
                        importTraitement.clear();
                     }
                  }
                  
                  //si on est à la fin du fichier : si il y a des warnings concernant l'écrasement de données, on sort en lançant une exception, sinon on continue à lire le fichier pour détecter les
                  //autres éventuelles mises à jour :
                  if(j == nbLignesDansFichier) {
                     if(listImportRowErrorForWarningEcrasement.size() > 0) {
                        throw new ImportEcrasementDonneeWarningForFileException(listImportRowErrorForWarningEcrasement, ecrasementChampAnnotationSupervisor.getNbRowTraiteAvant1erWarning());
                     }
                  }
                  
                  nbLotsTraites++;
               }
               else {
                  //si on est à la fin du fichier, on renvoie l'exception avec toutes les erreurs, sinon on continue à lire les lignes pour détecter les erreurs et les remonter
                  //A noter que par sécurité concernant la volumétrie, on sort du traitement si 100 erreurs ont été détectées. Ca permet d'éviter de traiter
                  //un très gros fichier inutilement.
                  final int NB_ERRORS_MAX = 100;
                  int nbErrors = listRowError.size();
                  if(j == nbLignesDansFichier || nbErrors == NB_ERRORS_MAX) {
                     int nbRowsImportees = nbLotsTraites*nbLignesPourUneTransaction;
                     //si il y a des erreurs sur un lot, on sort du traitement. On indiquera à l'utilisateur les lignes éventuellement traitées
                     throw new ImportDataRowException(listRowError, nbRowsImportees);
                  }
                  else {
                     continue;
                  }
               }
            }            
         }
      }
      catch(Exception e) {
         //si une exception est intervenue après la création de importHistorique mais pendant le traitement du 1er lot,
         //suppression d'importHistorique pour garder le fonctionnement habituel de ne tracer l'import que si il a réussi
         if(historique != null && historique.getImportHistoriqueId() != null && nbLotsTraites == 0) {
            importHistoriqueManager.removeObjectManager(historique);
            historique = null;
         }
         throw e;
      }      
      finally{
         //NB : session est la vue hibernate de l'entityManager donc fermer l'entityManager ferme la session...
         if(entityManager != null) {
            entityManager.close();
         }
      }

      return historique;

   }
   
 // ------------------------- DEBUT des contrôles des prerequis ------------------------------
   
   private InfoColonnes validateFileFormat(Row headerRow, ImportTemplate importTemplate) throws ImportPrerequisitesException {
      //récupère les noms de colonnes et vérifie qu'il n'y a pas de doublon : renvoie une exception si KO
      List<String> listNomColonne = retrieveColonnesAndCheckColonneEnDoublon(headerRow);

      //correspondance des colonnes trouvées avec les colonnes attendues : renvoie une exception si colonnes manquantes
      List<ImportColonne> listTemplateImportColonne = checkColonnesManquantes(importTemplate, listNomColonne);
      
      String nomChampForControle = checkColonneForControleCleFonctionnelle(listTemplateImportColonne);

      return new InfoColonnes(listTemplateImportColonne, listNomColonne, nomChampForControle);
   }

   private List<String> retrieveColonnesAndCheckColonneEnDoublon(Row headerRow) throws HeaderException, ImportDoublonColonneException {
      log.debug("Lecture des colonnes");
      List<String> listNomColonne = new ArrayList<String>();
      final int nbColonnes = headerRow.getLastCellNum();
      List<String> listColonneEnDoublon = new ArrayList<String>();
      String nomColonne = null;
      // pour chaque cellule
      for(int i = 0; i < nbColonnes; i++){
         // on extrait le nom de la colonne
         //final String nomColonne = getCellContent(row.getCell(i), false, null);
         try {
            nomColonne = headerRow.getCell(i).getStringCellValue();
         }
         catch (IllegalStateException illegalStateException) {//TODO TK-538 : à revoir pour ne pas sortir dès la 1ere erreur (faire comme pour ImportDoublonColonneException mais nécessite de revoir HeaderException définie pour l'import en création).
            throw new HeaderException(i+1);
         }
         if(listNomColonne.contains(nomColonne)) {
            listColonneEnDoublon.add(nomColonne);
         }
         else {
            listNomColonne.add(nomColonne);
         }
      }
      
      if(listColonneEnDoublon.size() > 0) {
         throw new ImportDoublonColonneException(listColonneEnDoublon);
      }
      log.debug("colonnes récupérées : {}", listNomColonne.stream().collect(Collectors.joining(", ")));
      
      return listNomColonne;
   }
   
   private List<ImportColonne> checkColonnesManquantes(ImportTemplate importTemplate, List<String> listNomColonne)
      throws BadFileFormatException{
      List<ImportColonne> listColonneManquante = new ArrayList<ImportColonne>();
      EntityManager entityManager = null;
      List<ImportColonne> listTemplateImportColonne = null;
      try {
         entityManager = entityManagerFactory.createEntityManager();
         TypedQuery<ImportColonne> query = entityManager.createNamedQuery("ImportColonne.findByTemplateWithOrder", ImportColonne.class);
         listTemplateImportColonne = query.setParameter(1, importTemplate).getResultList();
      }
      finally {
         entityManager.close();
      }
      for(ImportColonne importColonne : listTemplateImportColonne) {
         if(!listNomColonne.stream().anyMatch(importColonne.getNom()::equalsIgnoreCase)) {
            listColonneManquante.add(importColonne);
         }
      }
      
      if(listColonneManquante.size() > 0){
         throw new BadFileFormatException(listColonneManquante);
      }
      return listTemplateImportColonne;
   }
   
   //si une colonne de contrôle est définie, elle est en 2e position. Cette colonne n'est dans ce cas pas un champ d'annotation
   private String checkColonneForControleCleFonctionnelle(List<ImportColonne> listImportColonneSorted) {
      if(listImportColonneSorted != null && listImportColonneSorted.get(1).getChamp().getChampEntite() != null) {
         return listImportColonneSorted.get(1).getChamp().getChampEntite().getNom();
      }
      return null;
   }

   
   //1ere lecture des lignes de données du fichier (1ere colonne uniquement à partir de la 2e ligne) pour vérifier les codes :
   //    - les codes doivent être renseignés
   //    - les codes doivent être des alphanumeriques
   //    - il ne faut pas de doublon dans le fichier
   //    - il faut que le code existe en base
   //    - et il faut que si une colonne de contrôle est définie, que le code soit bien associé à la valeur de cette colonne de contrôle 
   private List<CodeIdPair> checkClesFonctionnelles(InfoColonnes infoColonnes, Iterator<Row> iteratorDataRow, Integer firstRowNum, EEntiteId entiteIdOfImport, Banque banque, EContexte eContexte) throws ImportCleFonctionnelleInexploitableException, ImportCleFonctionnelleManquanteException, ImportDoublonInFileException, ImportKeyNotFoundException, ImportCleFonctionnelleIncoherenteException, ImportDataControleInexploitableException {
      log.debug("Contrôle des clés fonctionnelles");
      DataForControles dataForControle = checkDoublon(infoColonnes, iteratorDataRow, firstRowNum, entiteIdOfImport, eContexte);
      //check si les codes du fichier existent bien en base de données. Si ils existent tous les renvoie associés à leur Id sinon lance une exception :
      List<CodeIdPair> listCodeExistant = checkCleFonctionnelleInexistante(infoColonnes, entiteIdOfImport, banque, eContexte, dataForControle);
      //si des données de contrôles existent, vérifie la cohérence entre les données du fichier et les données en base. Si incohérence rencontrée, renvoie une exception
      checkCoherenceCodeDataForControle(infoColonnes, listCodeExistant, dataForControle, firstRowNum);
      
      //retourne la liste des codes à traiter si tous sont valides
      return listCodeExistant;

   }

   //Contrôle si plusieurs lignes avec la même clé fonctionnelle existent.
   //si oui lance une ImportDoublonInFileException contenant les codes correspondants et les numéros des lignes associées
   //si non retourne DataForControles contenant la liste des codes contenus dans le fichier ainsi que les éventuelles valeurs renseignées pour les contrôles des codes
   //private DataForControles checkDoublon(String nomKeyColonne, Iterator<Row> iteratorDataRow, Integer firstRowNum, EEntiteId entiteIdOfImport, EContexte eContexte)
   private DataForControles checkDoublon(InfoColonnes infoColonnes, Iterator<Row> iteratorDataRow, Integer firstRowNum, EEntiteId entiteIdOfImport, EContexte eContexte)
      throws ImportCleFonctionnelleInexploitableException, ImportCleFonctionnelleManquanteException, ImportDataControleInexploitableException, ImportDoublonInFileException{
      log.debug("Contrôle si plusieurs lignes sont définies avec la même clé fonctionnelle");
      String nomKeyColonne = infoColonnes.getListNomColonne().get(0);
      String nomChampForControle = infoColonnes.getNomChampForControle();
      int nbColonne = infoColonnes.getListNomColonne().size();
      //Pour le traitement de contrôle des doublons, utilisation de 2 maps alimentées au fur et à mesure des contrôles:
      // - la 1ere (mapCodeRowNum) garde le 1er numéro de ligne de tous les codes lus : la clé de la map est la clé fonctionnelle (le code), la valeur le numéro de la ligne de la 1ere occurence du code
      // - la 2e (mapDoublon) stocke uniquement les codes en doublons : la clé est le code en doublon et les valeurs les numéros des lignes contenant ce code (le numéro commence à 1)
      Map<String, Integer> mapCodeRowNum = new HashMap<String, Integer>();
      Map<String, List<Integer>> mapDoublon = new HashMap<String, List<Integer>>();
      
      //liste des numeros de ligne des codes inexploitables : si lors de la lecture des codes, une erreur est rencontrée, 
      //le numéro de ligne correspondant sera ajouté à cette liste pour renvoyer l'information
      List<Integer> listRowNumForCleFonctionnelleInexploitable = new ArrayList<Integer>();
      List<Integer> listRowNumForCleFonctionnelleManquante = new ArrayList<Integer>();
      List<Integer> listRowNumForDataForControleInexploitable = new ArrayList<Integer>();
      
      List<String> listValueForControle = null;
      if(nomChampForControle != null) {
         listValueForControle = new ArrayList<String>();
      }
      
      Row row = null;
      Integer rowNum = firstRowNum;
      while(iteratorDataRow.hasNext()){
         row = iteratorDataRow.next();
         rowNum++;
         //code de l'objet à mettre à jour : récupéré de la 1ere colonne
         String code = null;
         //si code non récupéré on regarde si toute la ligne est vide : si oui on considère qu'il n'y a plus de ligne à traiter : parfois l'iterator ramène des lignes non significative
         //A noter que le mode debug montre que si une ligne est vide dans le fichier, POI ne la met pas dans l'iterator des lignes...
         //la boucle lisant toutes les cellules de la ligne est tout de même laissé par sécurité
         if(ExcelUtility.isEmpty(row.getCell(0))) {
            boolean emptyRow = true;
            int indexCell = 1;
            while (emptyRow && indexCell < nbColonne) {
               emptyRow = ExcelUtility.isEmpty(row.getCell(indexCell));
               indexCell++;
            }
            if(emptyRow) {
               break;
            }
            else {
               listRowNumForCleFonctionnelleManquante.add(rowNum);
               continue;
            }
         }
         try {
            code = ExcelUtility.readAlphanumContent(row.getCell(0));
         }
         catch (ExcelIllegalContentTypeException e) {
            listRowNumForCleFonctionnelleInexploitable.add(rowNum);
            continue;
         }
         
         Integer rowNumEventuelDoublon = mapCodeRowNum.get(code);
         if(rowNumEventuelDoublon == null) {
            mapCodeRowNum.put(code, rowNum);//on stocke juste le lien entre le code et rowNum.
         }
         else {//cas d'un doublon sur la clé fonctionnelle.
            //on ajoute le rowNum courant et on le rowNumEventuelDoublon si c'est la première fois que le test de doublon est positif pour ce code
            List<Integer> listRowNumForCode = mapDoublon.get(code);
            if(listRowNumForCode == null) {
               listRowNumForCode = new ArrayList<Integer>();
               mapDoublon.put(code, listRowNumForCode);
               //ajout de la 1ere occurrence :
               listRowNumForCode.add(rowNumEventuelDoublon);
            }
            //ajout de l'occurrence courante :
            listRowNumForCode.add(firstRowNum);
         }
         
         //lors de cette passe, on en profite pour lire la colonne si il y en a une :
         if(nomChampForControle != null) {
            try {
               listValueForControle.add(ExcelUtility.readAlphanumContent(row.getCell(1)));
            }
            catch (ExcelIllegalContentTypeException e) {
               listRowNumForDataForControleInexploitable.add(rowNum);
               continue;
            }
         }
      }
      
      if(!listRowNumForCleFonctionnelleInexploitable.isEmpty()) {
         throw new ImportCleFonctionnelleInexploitableException(nomKeyColonne, listRowNumForCleFonctionnelleInexploitable);
      }
      if(!listRowNumForCleFonctionnelleManquante.isEmpty()) {
         throw new ImportCleFonctionnelleManquanteException(nomKeyColonne, listRowNumForCleFonctionnelleManquante);
      }
      if(!listRowNumForDataForControleInexploitable.isEmpty()) {
         throw new ImportDataControleInexploitableException(infoColonnes.getListNomColonne().get(1), listRowNumForDataForControleInexploitable);
      }
      
      if(!mapDoublon.isEmpty()) {
         throw new ImportDoublonInFileException(nomKeyColonne, mapDoublon);
      }
      
      List<String> listCode = new ArrayList<String>(mapCodeRowNum.keySet());
      
      //plus besoin des maps de test des doublons
      mapCodeRowNum = null;
      mapDoublon = null;
      
      return new DataForControles(listCode, listValueForControle);
   }
   
   //contrôle que tous les codes de listCodeForControleExistence existent bien en base de données :
   //si c'est le cas, renvoie une liste de CodeIdPair : objet associant le code à l'id de l'objet en base
   //sinon lance une ImportKeyNotFoundException contenant la liste des codes qui n'existent pas en base de données
   private List<CodeIdPair> checkCleFonctionnelleInexistante(InfoColonnes infoColonnes, EEntiteId entiteIdOfImport, Banque banque, EContexte eContexte,
      DataForControles dataForControles) throws ImportKeyNotFoundException{

      log.debug("Contrôle si les objets associés aux clés fonctionnelles contenues dans le fichier existent bien en base de données");
      String nomKeyColonne = infoColonnes.getListNomColonne().get(0);
      //vérification que l'objet existe
      //lotissement de la récupération des valeurs pour éviter une clause in de la requête select trop grosse, en cas de fichier volumineux
      List<CodeIdPair> listCodeExistant = new ArrayList<CodeIdPair>();
      
      final int NB_ROWS_MAX = 200;
      List<String> listCodeForControleExistence = dataForControles.getListCode();
      int nbCode = listCodeForControleExistence.size();
      List<String>  listCodeForSelect = new ArrayList<String>();
      int indexMinCodeATraiter = 0;
      int indexMaxCodeATraiter =  indexMinCodeATraiter+NB_ROWS_MAX;
      ImportChampAnnotationEntiteStrategy entiteStrategy = importChampAnnotationEntiteStrategyFactory.retrieveEntiteStrategy(entiteIdOfImport, eContexte);
      while(indexMinCodeATraiter < nbCode) {
         listCodeForSelect.clear();
         listCodeForSelect.addAll(listCodeForControleExistence.subList(indexMinCodeATraiter, Math.min(indexMaxCodeATraiter, nbCode)));
         
         listCodeExistant.addAll(entiteStrategy.findIdAndDataForControleByCodesAndBanque(listCodeForSelect, banque, infoColonnes.getNomChampForControle()));

         indexMinCodeATraiter = indexMaxCodeATraiter;
         indexMaxCodeATraiter = indexMinCodeATraiter+NB_ROWS_MAX;
      }
      
      final List<CodeIdPair> listCodeExistantFinal = listCodeExistant;
      //récupération des éventuels codes non présents en base :
      //initialisation de listCodeInexistant avec tous les codes et filtre ensuite pour ne garder que les cas non présents en base
      List<String> listCodeInexistant = new ArrayList<String>(listCodeForControleExistence);
      listCodeInexistant.removeIf(code -> listCodeExistantFinal.contains(new CodeIdPair(code)));

      if(!listCodeInexistant.isEmpty()) {
         if(entiteIdOfImport == EEntiteId.PATIENT) {
            throw new ImportKeyPatientNotFoundException(nomKeyColonne, listCodeInexistant);
         }
         throw new ImportKeyNotFoundException(nomKeyColonne, listCodeInexistant);
      }

      return listCodeExistant;
   }

   private void checkCoherenceCodeDataForControle(InfoColonnes infoColonnes, List<CodeIdPair> listCodeExistant, DataForControles dataForControle, Integer firstRowNum) throws ImportCleFonctionnelleIncoherenteException {
      //si des données de contrôles existent :
      if(dataForControle != null && dataForControle.getListValueForControle() != null) {
         String nomKeyColonne = infoColonnes.getListNomColonne().get(0);
         String nomChampForControle = infoColonnes.getListNomColonne().get(1);
         int nbCode = listCodeExistant.size();
         List<String> listValueForControle = dataForControle.getListValueForControle();
         //parcours des 2 listes listValeurForControle et comparaison des valeurs : 
         //Pour les données provenant de la base, les valeurs à comparer sont rattachés à chaque objet contenu dans listCodeExistant (de type CodeIdPair)
         //Par contre, pour les valeurs lues, les valeurs sont directement dans dataForControle.getListValueForControle()
         //les 2 listes sont dans le même ordre du fait du traitement
         ImportCleFonctionnelleIncoherenteException cleFonctionnelleIncoherenteException = null;
         for(int i=0; i<nbCode; i++) {
            //si l'utilisateur n'a pas renseignée de valeur pour le champ de contrôle, on considère qu'il ne veut pas faire de contrôle...
            if(listValueForControle.get(i) != null && !listValueForControle.get(i).isEmpty() &&  !listValueForControle.get(i).equals(listCodeExistant.get(i).getValeurForControle())) {
               if(cleFonctionnelleIncoherenteException == null) {
                  cleFonctionnelleIncoherenteException = new ImportCleFonctionnelleIncoherenteException(nomKeyColonne, nomChampForControle);
               }
               cleFonctionnelleIncoherenteException.addErrors(firstRowNum+1+i, listCodeExistant.get(i).getCode(), 
                  listCodeExistant.get(i).getValeurForControle(), listValueForControle.get(i));
            }
         }
         
         if(cleFonctionnelleIncoherenteException != null) {
            throw cleFonctionnelleIncoherenteException;
         }
      }
   }
   
// ------------------------- FIN des contrôles des prerequis ------------------------------

   //valorise les ImportChampAnnotationInfo contenus dans chaque traitement lié à une colonne (un objet par objet TK à mettre à jour)
   //avec la valeur existante en base de données
   private void retrieveExistingValues(Session session,
      List<ImportChampAnnotationTraitement> listImportChampAnnotationTraitement){
      
      session.doWork(new Work()
      {
          @Override
          public void execute(final Connection connection) throws SQLException {
             
             PreparedStatement stmSelectValeurExistante = null;// revoir le close du pstm

             connection.setAutoCommit(true);

             for(ImportChampAnnotationTraitement importTraitement : listImportChampAnnotationTraitement) {   
               try { 
                   if(!importTraitement.isEmpty()) {
                      stmSelectValeurExistante = connection.prepareStatement(importTraitement.getSqlSelectRequete());
                      importTraitement.prepareSelectCall(stmSelectValeurExistante);
                      
                      importTraitement.manageSelectResultSet(stmSelectValeurExistante.executeQuery());
                   }
               }
               finally {
                  if(stmSelectValeurExistante != null && !stmSelectValeurExistante.isClosed()) {
                     stmSelectValeurExistante.close();
                  }
               }
             }
          }
      });
   }
   
   //traitement de la transaction SQL
   private void doInsertsAndUpdatesInOneTransaction(Session session,
      EnumMap<EAnnotationValeurColonneForData, List<ImportChampAnnotationTraitement>> mapImportChampAnnotationTraitementByType, Banque banque, EEntiteId entiteIdOfImport, ImportHistorique importHistorique, Integer utilisateurId){
      
      session.doWork(new Work()
      {
          @Override
          public void execute(final Connection connection) throws SQLException {
             
             connection.setAutoCommit(false);
             
             List<PreparedStatement> listPreparedStatements = new ArrayList<PreparedStatement>();
             PreparedStatement stmInsertImportation = null;
             PreparedStatement stmInsertOperation = null;
             
             //Pour chaque ImportChampAnnotationTraitement contenu de la map alimentée au fur et à mesure de la lecture du fichier,
             //exécution des requêtes d'insert et d'update
             Iterator<EAnnotationValeurColonneForData> itKeyMapByType = mapImportChampAnnotationTraitementByType.keySet().iterator();
             Set<Integer> setObjetIdTraite = new HashSet<Integer>();
             try {
                while(itKeyMapByType.hasNext()) {
                   List<ImportChampAnnotationTraitement> listImportChampAnnotationTraitementByType = mapImportChampAnnotationTraitementByType.get(itKeyMapByType.next());
                   
                   int nbByType = listImportChampAnnotationTraitementByType.size();
                   PreparedStatement stmInsert = null;
                   PreparedStatement stmUpdate = null;
                   for(int i=0; i<nbByType ; i++) {
                      ImportChampAnnotationTraitement importChampAnnotationTraitement = listImportChampAnnotationTraitementByType.get(i);
                      setObjetIdTraite.addAll(importChampAnnotationTraitement.retrieveObjetIdTraite());
                      if(i==0) {
                         stmInsert = connection.prepareStatement(importChampAnnotationTraitement.getSqlInsertRequete());
                         stmUpdate = connection.prepareStatement(importChampAnnotationTraitement.getSqlUpdateRequete());
                         
                         listPreparedStatements.add(stmInsert);
                         listPreparedStatements.add(stmUpdate);
                      }
                      
                      importChampAnnotationTraitement.prepareInsertAndUpdateCall(stmInsert, stmUpdate, banque.getBanqueId());
                   }

                   for(PreparedStatement preparedStatement : listPreparedStatements) {
                      preparedStatement.executeBatch();
                   }
                }
                
                //insert importation + operation
                stmInsertImportation = connection.prepareStatement(SQL_INSERT_IMPORTATION);  
                stmInsertOperation = connection.prepareStatement(SQL_INSERT_OPERATION);
                Timestamp dateHeureOperation = new Timestamp(importHistorique.getDate().getTimeInMillis());
                Iterator<Integer> itObjetIdTraite = setObjetIdTraite.iterator();
                while (itObjetIdTraite.hasNext()) {
                   Integer objetId = itObjetIdTraite.next();
                   stmInsertImportation.setInt(1, objetId);
                   stmInsertImportation.setInt(2, entiteIdOfImport.getId());
                   stmInsertImportation.setInt(3, importHistorique.getImportHistoriqueId());
                   stmInsertImportation.setString(4, EImportationType.MODIFICATION_ANNOTATION.getCode());
                   
                   stmInsertImportation.addBatch();
                   
                   stmInsertOperation.setInt(1, utilisateurId);
                   stmInsertOperation.setTimestamp(2, dateHeureOperation);
                   stmInsertOperation.setInt(3, objetId);
                   stmInsertOperation.setInt(4, EOperationTypeId.IMPORT_ANNOTATION.getId());
                   stmInsertOperation.setInt(5, entiteIdOfImport.getId());
                   
                   stmInsertOperation.addBatch();
                }
                stmInsertImportation.executeBatch();
                stmInsertOperation.executeBatch();
                
                connection.commit();
             }
             catch (Exception e) {
               connection.rollback();
               throw e;
             }
             finally {
                for(PreparedStatement preparedStatement : listPreparedStatements) {
                   if(!preparedStatement.isClosed()) {
                      preparedStatement.close();
                   }
                }
                if(stmInsertImportation != null && !stmInsertImportation.isClosed()) {
                   stmInsertImportation.close();
                }
                if(stmInsertOperation != null && !stmInsertOperation.isClosed()) {
                   stmInsertOperation.close();
                }
             }

          }
      });
   }
   
   
   private class InfoColonnes {
      private List<ImportColonne> listImportColonne;
      //liste des noms des colonnes du fichier dans l'ordre (des numéros de colonne)
      private List<String> listNomColonne;
      //une colonne "for controle" est définie dans le cas de l'import patient pour éviter des erreurs de saisie sur le NIP qui
      //est moins parlant que les codes des materiels biologiques. 
      //nomChampForControle correspond au nom du champ associé à cette colonne. Il reste à null dans la majorité des cas
      private String nomChampForControle;


      public InfoColonnes(List<ImportColonne> listImportColonne, List<String> listNomColonne, String nomChampForControle){
         this.listImportColonne = listImportColonne;
         this.listNomColonne = listNomColonne;
         this.nomChampForControle = nomChampForControle;
      }

      public List<ImportColonne> getListImportColonne(){
         return listImportColonne;
      }

      public List<String> getListNomColonne(){
         return listNomColonne;
      }
      
      public String getNomChampForControle(){
         return nomChampForControle;
      }
   }
   
   //objet alimenté par la 1ere lecture du fichier qui se restreint aux premières colonnes nécessaires pour retrouver l'objet à modifier (code) et vérifier qu'il n'y a
   //pas d'erreur sur ce code :
   private class DataForControles {
      private List<String> listCode;
      //liste des noms de colonne à utiliser pour les contrôles : dans le cas général, cette liste est vide. Pour le patient, cette liste contient les noms des patients
      //le traitement prévoit qu'à terme plusieurs colonnes pourraient être renseignées par l'utilisateur. Mais à noter que ces colonnes sont figés par le traitement
      //ce n'est pas l'utilsiateur qui les détermine (cf les classes héritant de ImportChampAnnotationEntiteStrategy)
      //private List<List<String>> listValueForControles;
      private List<String> listValueForControle;
      
      public DataForControles(List<String> listCode, List<String> listValueForControle){
         this.listCode = listCode;
         this.listValueForControle = listValueForControle;
      }

      public List<String> getListCode(){
         return listCode;
      }

      public List<String> getListValueForControle(){
         return listValueForControle;
      }
   }
   
}
