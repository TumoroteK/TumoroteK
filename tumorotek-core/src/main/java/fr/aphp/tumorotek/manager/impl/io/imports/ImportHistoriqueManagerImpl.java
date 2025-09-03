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
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.dao.io.imports.ImportHistoriqueDao;
import fr.aphp.tumorotek.dao.io.imports.ImportationDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.io.imports.EImportationType;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.systeme.EEntiteId;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class ImportHistoriqueManagerImpl implements ImportHistoriqueManager
{

   private final Logger log = LoggerFactory.getLogger(ImportHistoriqueManager.class);

   private EntityManagerFactory entityManagerFactory;
   
   private ImportHistoriqueDao importHistoriqueDao;

   private UtilisateurDao utilisateurDao;

   private ImportationDao importationDao;

   private EntiteDao entiteDao;


   public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory){
      this.entityManagerFactory = entityManagerFactory;
   }
   
   public void setImportHistoriqueDao(final ImportHistoriqueDao iDao){
      this.importHistoriqueDao = iDao;
   }

   public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   public void setImportationDao(final ImportationDao iDao){
      this.importationDao = iDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   @Override
   public ImportHistorique findByIdManager(final Integer importHistoriqueId){
      return importHistoriqueDao.findById(importHistoriqueId);
   }

   @Override
   public List<ImportHistorique> findAllObjectsManager(){// A CREUSER !!
      log.debug("Recherche de tous les ImportHistoriques.");
      return importHistoriqueDao.findAll();
   }

   @Override
   public List<ImportHistorique> findByTemplateIdAndImportBanqueIdWithOrderManager(Integer templateId, Integer importBanqueId) {
         return importHistoriqueDao.findByTemplateIdAndImportBanqueIdWithOrder(templateId, importBanqueId);
   }
   
   @Override
   public List<Importation> findImportationsByHistoriqueManager(final ImportHistorique importHistorique){
      if(importHistorique != null){
         return importationDao.findByHistoriqueId(importHistorique.getImportHistoriqueId());
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Importation> findImportationsByHistoriqueAndEntiteManager(final ImportHistorique importHistorique,
      final Entite entite){
      if(importHistorique != null && entite != null){
         return importationDao.findByHistoriqueIdAndEntiteId(importHistorique.getImportHistoriqueId(), entite.getEntiteId());
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Importation> findImportationsByHistoriqueAndEEntiteIdManager(final ImportHistorique importHistorique,
      final EEntiteId eEntiteId){
      return findImportationsByHistoriqueAndEntiteManager(importHistorique, entiteDao.findById(eEntiteId.getId()));

   }   
   
   @Override
   public List<Importation> findImportationsByObjectManager(final Object object){

      Entite e = null;
      Integer id = 0;
      if(object != null){
         if(object.getClass().getSimpleName().equals("Patient")){
            e = entiteDao.findByNom("Patient").get(0);
            id = ((Patient) object).getPatientId();
         }else if(object.getClass().getSimpleName().equals("Prelevement")){
            e = entiteDao.findByNom("Prelevement").get(0);
            id = ((Prelevement) object).getPrelevementId();
         }else if(object.getClass().getSimpleName().equals("Echantillon")){
            e = entiteDao.findByNom("Echantillon").get(0);
            id = ((Echantillon) object).getEchantillonId();
         }else if(object.getClass().getSimpleName().equals("ProdDerive")){
            e = entiteDao.findByNom("ProdDerive").get(0);
            id = ((ProdDerive) object).getProdDeriveId();
         }
      }

      if(e != null && id > 0){
         return importationDao.findByEntiteIdAndObjetId(e.getEntiteId(), id);
      }else{
         return new ArrayList<>();
      }
   }

 
   @Override
   public List<Importation> findImportationsForCreationByEntiteIdAndObjectIdManager(final Integer entiteId, final Integer objetId){
      if(entiteId != null && objetId != null){
         return importationDao.findByEntiteIdObjetIdAndTypeCode(entiteId, objetId, EImportationType.CREATION.getCode());
      }else{
         return new ArrayList<>();
      }
   }
   
   
   @Override
   public void createObjectManager(final ImportHistorique importHistorique, final Utilisateur utilisateur, final List<Importation> importations){
      // utilisateur required
      if(utilisateur != null){
         importHistorique.setUtilisateur(utilisateurDao.mergeObject(utilisateur));
      }else{
         log.warn("Objet obligatoire Utilisateur manquant  lors de la création d'un ImportHistorique");
         throw new RequiredObjectIsNullException("Utilisateur", "creation", "importTemplate");
      }

      importHistoriqueDao.createObject(importHistorique);

      if(importations != null){
         for(int i = 0; i < importations.size(); i++){
            final Importation imp = importations.get(i);
            imp.setImportHistoriqueId(importHistorique.getImportHistoriqueId());
            importationDao.createObject(imp);
         }
      }
      
      log.info("Enregistrement objet ImportHistorique {} et les objets importations liés ",  importHistorique);
   }
   
   
   @Override
   public void removeObjectManager(final ImportHistorique importHistorique){
      if(importHistorique != null){
         importHistoriqueDao.removeObject(importHistorique.getImportHistoriqueId());
         //TK-538 : lien importHistorique / Importation supprimé pour optimiser.
         //Le lien était en delete cascade donc gestion ici du delete cascade supprimé
         //mais sans chercher à optimiser puisque la fonctionnalité n'est pas possible depuis l'applicaiton
         //la méthode est appelée uniquement par les tests :-(
         List<Importation> importations = importationDao.findByHistoriqueId(importHistorique.getImportHistoriqueId());
         for(Importation importation : importations) {
            importationDao.removeObject(importation.getImportationId());
         }
         log.info("Suppression de l'objet ImportHistorique : {}",  importHistorique);
      }else{
         log.warn("Suppression d'un ImportHistorique null");
      }
   }

   @Override
   public void removeImportationManager(final Importation importation){
      if(importation != null){
         importationDao.removeObject(importation.getImportationId());
         log.info("Suppression de l'objet Importation : {}",  importation);
      }else{
         log.warn("Suppression d'un Importation null");
      }
   }

   @Override
   public List<Prelevement> findPrelevementByImportHistoriqueManager(final ImportHistorique ih){
      if(ih != null) {
         return importHistoriqueDao.findPrelevementByImportHistoriqueId(ih.getImportHistoriqueId());
      }
      return new ArrayList<Prelevement>();
   }
   
   @Override
   public Date findMaxDateImportationForImportTemplateId(Integer importTemplateId, Integer utilisateurBanqueId) {
      final EntityManager em = entityManagerFactory.createEntityManager();
      
      Session session = em.unwrap(Session.class);
      SQLQuery query = session.createSQLQuery("SELECT max(DATE_) FROM IMPORT_HISTORIQUE WHERE IMPORT_TEMPLATE_ID = :importTemplateId and IMPORT_BANQUE_ID = :utilisateurBanqueId");
      query.setParameter("importTemplateId", importTemplateId);
      query.setParameter("utilisateurBanqueId", utilisateurBanqueId);
       
      return (Date)query.uniqueResult();
   }

   
   @Override
   public List<String> findNomBanqueUtilisantUnTemplatePartage(Integer importTemplateId, Integer templateBanqueId) {
      final EntityManager em = entityManagerFactory.createEntityManager();
      
      Session session = em.unwrap(Session.class);
      SQLQuery query = session.createSQLQuery("SELECT distinct b.nom FROM IMPORT_HISTORIQUE h inner join BANQUE b on b.BANQUE_ID = h.IMPORT_BANQUE_ID WHERE IMPORT_TEMPLATE_ID = :importTemplateId and IMPORT_BANQUE_ID != :templateBanqueId");
      query.setParameter("importTemplateId", importTemplateId);
      query.setParameter("templateBanqueId", templateBanqueId);
       
      return query.list();
   }

   
}