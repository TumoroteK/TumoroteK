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
package fr.aphp.tumorotek.manager.impl.io.imports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.imports.ImportTemplateDao;
import fr.aphp.tumorotek.dao.qualite.OperationDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.io.imports.ImportColonneManager;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.io.imports.ImportTemplateManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.io.imports.ImportTemplateValidator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.io.imports.EImportTemplateStatutPartage;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.qualite.EOperationTypeId;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.systeme.EEntiteId;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

public class ImportTemplateManagerImpl implements ImportTemplateManager
{

   private final Logger log = LoggerFactory.getLogger(ImportTemplateManager.class);

   /** Bean Dao. */
   private ImportTemplateDao importTemplateDao;

   /** Bean Dao. */
   private BanqueDao banqueDao;

   /** Bean Dao. */
   private EntiteDao entiteDao;

   private ImportTemplateValidator importTemplateValidator;

   private ImportColonneManager importColonneManager;
   
   private OperationManager operationManager;
   
   private OperationTypeDao operationTypeDao;
   
   private ImportHistoriqueManager importHistoriqueManager;
      

   public void setImportTemplateDao(final ImportTemplateDao iDao){
      this.importTemplateDao = iDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setImportTemplateValidator(final ImportTemplateValidator iValidator){
      this.importTemplateValidator = iValidator;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setImportColonneManager(final ImportColonneManager iManager){
      this.importColonneManager = iManager;
   }

   public void setOperationManager(OperationManager operationManager){
      this.operationManager = operationManager;
   }

   public void setOperationTypeDao(OperationTypeDao operationTypeDao){
      this.operationTypeDao = operationTypeDao;
   }

   public void setImportHistoriqueManager(ImportHistoriqueManager importHistoriqueManager){
      this.importHistoriqueManager = importHistoriqueManager;
   }
   
   @Override
   public ImportTemplate findByIdManager(final Integer importTemplateId){
      return importTemplateDao.findById(importTemplateId);
   }

   @Override
   public List<ImportTemplate> findAllObjectsManager(){
      log.debug("Recherche de tous les ImportTemplates.");
      return importTemplateDao.findAll();
   }

   @Override
   public List<ImportTemplate> findByBanqueManager(final Banque banque){
      log.debug("Recherche de tous les ImportTemplates d'une banque.");
      if(banque != null){
         return importTemplateDao.findByBanqueWithOrder(banque);
      }else{
         return new ArrayList<>();
      }
   }
   
   //TK-537
   @Override
   public List<ImportTemplate> findTemplateByStatutPartageAndBanqueWithOrder(EImportTemplateStatutPartage eImportTemplateStatutPartage,
          Banque banque) {
      log.debug("Recherche de tous les ImportTemplates de la banque {} qui sont partagés.", banque.getNom());
      if(banque != null){
         return importTemplateDao.findTemplateByStatutPartageAndBanqueWithOrder(eImportTemplateStatutPartage.getImportTemplateStatutPartageCode(), banque);
      }else{
         return new ArrayList<>();
      }      
   }
   
   @Override
   public List<ImportTemplate> findImportTemplateCreatedOrUsedByBanqueWithOrder(Banque banque) {
      if(banque != null){
         log.debug("Recherche de tous les ImportTemplates créés par la banque {} ou partagés et utilisés pour cette banque.", banque.getNom());
         List<ImportTemplate> resultat = new ArrayList<ImportTemplate>();
         List<ImportTemplate> banqueTemplates = findByBanqueManager(banque);///!\ dans dao : order qu'on peut supprimer
         resultat.addAll(banqueTemplates);
         List<ImportTemplate> otherUsedTemplates = importTemplateDao.findTemplatePartageUtiliseByBanque(banque.getBanqueId());
         resultat.addAll(otherUsedTemplates);
         Collections.sort(resultat);
         return resultat;
      }else{
         return new ArrayList<>();
      }      
   }
   
   @Override
   public List<ImportTemplate> findTemplateNotArchiveByStatutPartageAndPlateformeWithOrder(EImportTemplateStatutPartage eImportTemplateStatutPartage,
          Plateforme plateforme) {
      if(plateforme != null){
         log.debug("Recherche de tous les ImportTemplates partagés sur la plateforme {}.", plateforme.getNom());
         return importTemplateDao.findTemplateByStatutPartageAndArchiveAndPlateformeWithOrder(eImportTemplateStatutPartage.getImportTemplateStatutPartageCode(), false, plateforme);
      }else{
         return new ArrayList<>();
      }      
   }
   //
   
   @Override
   public Set<Entite> getEntiteManager(ImportTemplate importTemplate){
      if(importTemplate != null && importTemplate.getImportTemplateId() != null){
         importTemplate = importTemplateDao.mergeObject(importTemplate);
         final Set<Entite> entites = importTemplate.getEntites();
         entites.size();
         return entites;
      }else{
         return new HashSet<>();
      }
   }

   @Override
   public Boolean findDoublonManager(final ImportTemplate importTemplate){
      if(importTemplate != null){
         if(importTemplate.getImportTemplateId() == null){
            return importTemplateDao.findAll().contains(importTemplate);
         }else{
            return importTemplateDao.findByExcludedId(importTemplate.getImportTemplateId()).contains(importTemplate);
         }
      }else{
         return false;
      }
   }

   @Override
   public void createObjectManager(final ImportTemplate importTemplate, final Banque banque, final List<Entite> entites,
      final List<ImportColonne> colonnesToCreate, Utilisateur loggedUser){
      // banque required
      if(banque != null){
         importTemplate.setBanque(banqueDao.mergeObject(banque));
      }else{
         log.warn("Objet obligatoire Banque manquant  lors de la création d'un ImportTemplate");
         throw new RequiredObjectIsNullException("ImportTemplate", "creation", "Banque");
      }

      // on vérifie la validité des colonnes
      if(colonnesToCreate != null){
         if(!importColonneManager.findDoublonInListManager(colonnesToCreate)){
            for(int i = 0; i < colonnesToCreate.size(); i++){
               importColonneManager.validateObjectManager(colonnesToCreate.get(i), importTemplate,
                  colonnesToCreate.get(i).getChamp(), "creation");
            }
         }else{
            log.warn("Doublon lors creation objet ImportColonne {}",  importTemplate);
            throw new DoublonFoundException("ImportColonne", "creation");
         }
      }

      //Doublon
      if(!findDoublonManager(importTemplate)){

         // validation due l'utilisateur
         BeanValidator.validateObject(importTemplate, new Validator[] {importTemplateValidator});

         importTemplateDao.createObject(importTemplate);

         updateAssociations(importTemplate, entites, colonnesToCreate, null);

         log.info("Enregistrement objet ImportTemplate {}",  importTemplate);
         
         // Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, loggedUser, operationTypeDao.findByNom("Creation").get(0),
            importTemplate);

         log.info("Enregistrement de l'opération Création par {} ", loggedUser);

      }else{
         log.warn("Doublon lors creation objet ImportTemplate {}",  importTemplate);
         throw new DoublonFoundException("ImportTemplate", "creation");
      }
   }

   @Override
   public void updateObjectManager(final ImportTemplate importTemplate, final Banque banque, final List<Entite> entites,
      final List<ImportColonne> colonnesToCreate, final List<ImportColonne> colonnesToremove, Utilisateur loggedUser){
      // banque required
      if(banque != null){
         importTemplate.setBanque(banqueDao.mergeObject(banque));
      }else{
         log.warn("Objet obligatoire Banque manquant  lors de la modification d'un ImportTemplate");
         throw new RequiredObjectIsNullException("ImportTemplate", "modification", "Banque");
      }

      // on vérifie la validité des colonnes
      if(colonnesToCreate != null){
         if(!importColonneManager.findDoublonInListManager(colonnesToCreate)){
            for(int i = 0; i < colonnesToCreate.size(); i++){
               importColonneManager.validateObjectManager(colonnesToCreate.get(i), importTemplate,
                  colonnesToCreate.get(i).getChamp(), "creation");
            }
         }else{
            log.warn("Doublon lors creation objet ImportColonne {}",  importTemplate);
            throw new DoublonFoundException("ImportColonne", "modification");
         }
      }

      //Doublon
      if(!findDoublonManager(importTemplate)){

         // validation due l'utilisateur
         BeanValidator.validateObject(importTemplate, new Validator[] {importTemplateValidator});

         importTemplateDao.updateObject(importTemplate);

         updateAssociations(importTemplate, entites, colonnesToCreate, colonnesToremove);

         log.info("Enregistrement objet ImportTemplate {}",  importTemplate);
         
         // Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, loggedUser, operationTypeDao.findByNom("Modification").get(0),
            importTemplate);

         log.info("Enregistrement de l'opération Modification par {} ", loggedUser);

      }else{
         log.warn("Doublon lors modification objet ImportTemplate {}",  importTemplate);
         throw new DoublonFoundException("ImportTemplate", "modification");
      }
   }

   //TK-537
   @Override
   public void updateStatutPartage(Integer importTemplateId, EImportTemplateStatutPartage newValue, Utilisateur loggedUser) throws IllegalArgumentException {
      ImportTemplate importTemplate = importTemplateDao.findById(importTemplateId);
      if(importTemplate != null && newValue != null) {
         if(importTemplate.getStatutPartage() != newValue) {
            if(EImportTemplateStatutPartage.checkCoherenceUpdate(importTemplate.getStatutPartage(), newValue)) {
               importTemplate.setStatutPartageCode(newValue.getImportTemplateStatutPartageCode());
               importTemplateDao.updateObject(importTemplate);
               
               String operationTypeNom = "Partage";
               if(newValue != EImportTemplateStatutPartage.PARTAGE_ENCOURS) {
                  operationTypeNom = "SuppressionPartage";
               }
               // Enregistrement de l'operation associee
               final Operation creationOp = new Operation();
               creationOp.setDate(Utils.getCurrentSystemCalendar());
               operationManager.createObjectManager(creationOp, loggedUser, operationTypeDao.findByNom(operationTypeNom).get(0),
                  importTemplate);
            }
            else {
               throw new IllegalArgumentException("Impossible de modifier le partage à cause de valeurs incohérentes :  en cours =  " + EImportTemplateStatutPartage.findByCode(importTemplate.getStatutPartage().getImportTemplateStatutPartageCode()) + " / nouveau = " + newValue);
            }
         }
         else {
            log.warn("Aucune mise à jour faite : le modèle {} avait déjà le champ 'statut_partage' à {}", importTemplateId, newValue.getImportTemplateStatutPartageCode());
         }
      }
      else {
         log.warn("Aucune mise à jour faite car des valeurs sont nulles : importTemplateId = {}, newValue = {}", importTemplate, newValue);
      }      
   }
   
   //TK-537
   @Override
   public void updateArchive(Integer importTemplateId, Boolean newValue, Utilisateur loggedUser){
      ImportTemplate importTemplate = importTemplateDao.findById(importTemplateId);
      if(importTemplate != null && newValue != null) {
         if(importTemplate.isArchive() != newValue) {
            importTemplate.setArchive(newValue);
            importTemplateDao.updateObject(importTemplate);
            
            String operationTypeNom = "Archivage";
            if(!newValue) {
               operationTypeNom = "Restauration";
            }
            // Enregistrement de l'operation associee
            final Operation creationOp = new Operation();
            creationOp.setDate(Utils.getCurrentSystemCalendar());
            operationManager.createObjectManager(creationOp, loggedUser, operationTypeDao.findByNom(operationTypeNom).get(0),
               importTemplate);

            log.info("Enregistrement de l'opération Modification par {} ", loggedUser);
         }
         else {
            log.warn("Aucune mise à jour faite : le modèle {} avait déjà la champ 'archive' à {}", importTemplateId, newValue);
         }
      }
      else {
         log.warn("Aucune mise à jour faite car des valeurs sont nulles : importTemplateId = {}, newValue = {}", importTemplate, newValue);
      }
   }

   @Override
   public void removeObjectManager(ImportTemplate importTemplate){
      if(importTemplate != null){
         // suppression des colonnes
         final List<ImportColonne> colonnes = importColonneManager.findByImportTemplateManager(importTemplate);
         for(int i = 0; i < colonnes.size(); i++){
            importColonneManager.removeObjectManager(colonnes.get(i));
         }

         importTemplate = importTemplateDao.mergeObject(importTemplate);
         final Iterator<Entite> it = importTemplate.getEntites().iterator();
         while(it.hasNext()){
            final Entite tmp = entiteDao.mergeObject(it.next());
            tmp.getImportTemplates().remove(importTemplate);
         }

         importTemplateDao.removeObject(importTemplate.getImportTemplateId());
         log.info("Suppression de l'objet ImportTemplate : {}",  importTemplate);
      }else{
         log.warn("Suppression d'un ImportTemplate null");
      }
   }

   /**
    * Cette méthode met à jour les associations entre un ImportTemplate,
    * et une liste d'entites.
    * @param utilisateur Utilisateur pour lequel on veut mettre à jour
    * les associations.
    * @param profils Liste de tous les profils de l'utilisateur : ceux
    * déjà existant et ceux a creer.
    * @param profilsToCreate Liste des profils a creer.
    * @param plateformes Liste des plateformes que l'on veut associer a
    * l'utilisateur.
    */
   public void updateAssociations(final ImportTemplate template, final List<Entite> entites,
      final List<ImportColonne> colonnesToCreate, final List<ImportColonne> colonnesToRemove){
      final ImportTemplate temp = importTemplateDao.mergeObject(template);

      // gestion des colonnes
      if(colonnesToRemove != null){
         // suppression des colonnes
         for(int i = 0; i < colonnesToRemove.size(); i++){
            importColonneManager.removeObjectManager(colonnesToRemove.get(i));
         }
      }

      if(colonnesToCreate != null){
         // ajout ou modif des colonnes
         for(int i = 0; i < colonnesToCreate.size(); i++){
            if(colonnesToCreate.get(i).getImportColonneId() == null){
               importColonneManager.createObjectManager(colonnesToCreate.get(i), temp, colonnesToCreate.get(i).getChamp());
            }else{
               importColonneManager.updateObjectManager(colonnesToCreate.get(i), temp, colonnesToCreate.get(i).getChamp());
            }
         }
      }

      // gestion des entites
      if(entites != null){
         final Iterator<Entite> it = temp.getEntites().iterator();
         final List<Entite> entitesToRemove = new ArrayList<>();
         // on parcourt les entites qui sont actuellement associés
         while(it.hasNext()){
            final Entite tmp = it.next();
            // si une Banque n'est pas dans la nouvelle liste, on
            // la conserve afin de la retirer par la suite
            if(!entites.contains(tmp)){
               entitesToRemove.add(tmp);
            }
         }

         // on parcourt la liste des Entites à retirer de
         // l'association
         for(int i = 0; i < entitesToRemove.size(); i++){
            final Entite e = entiteDao.mergeObject(entitesToRemove.get(i));
            // on retire l'entité de chaque coté de l'association
            temp.getEntites().remove(e);
            e.getImportTemplates().remove(temp);

            log.debug("Suppression de l'association entre l'ImportTemplate : {} et l'entité : {}", temp, e);

         }

         // on parcourt la nouvelle liste d'entites
         for(int i = 0; i < entites.size(); i++){
            // si une entité n'était pas associée
            if(!temp.getEntites().contains(entites.get(i))){
               // on ajoute la Banque des deux cotés de l'association
               temp.getEntites().add(entiteDao.mergeObject(entites.get(i)));
               entiteDao.mergeObject(entites.get(i)).getImportTemplates().add(temp);

               log.debug("Ajout de l'association entre l'ImportTemplate : {} et l'entité : {}", temp, entites.get(i));

            }
         }
      }
   }
   
   //TK-537
   @Override
   public boolean hasBeenModifiedAfterLastExecution(ImportTemplate importTemplate, Banque utilisateurBanque) {
      if(importTemplate != null && utilisateurBanque != null) {
         Date dateLastModification = operationManager.findMaxDateForObjetIdEntiteIdAndOperationTypeId(importTemplate.getImportTemplateId(), 
            EEntiteId.IMPORT_TEMPLATE.getId(), EOperationTypeId.MODIFICATION.getId());
         Date dateLastExecution = importHistoriqueManager.findMaxDateImportationForImportTemplateId(importTemplate.getImportTemplateId(), utilisateurBanque.getBanqueId());
         if(dateLastModification != null &&  dateLastExecution != null) {
            return dateLastModification.compareTo(dateLastExecution) >= 0;
         }
      }
      
      return false;
   }
 }
