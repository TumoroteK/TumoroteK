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
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.io.imports.ImportHistoriqueDao;
import fr.aphp.tumorotek.dao.io.imports.ImportTemplateDao;
import fr.aphp.tumorotek.dao.io.imports.ImportationDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class ImportHistoriqueManagerImpl implements ImportHistoriqueManager
{

   private final Log log = LogFactory.getLog(ImportHistoriqueManager.class);

   private ImportHistoriqueDao importHistoriqueDao;
   private ImportTemplateDao importTemplateDao;
   private UtilisateurDao utilisateurDao;
   private ImportationDao importationDao;
   private EntiteDao entiteDao;

   public void setImportHistoriqueDao(final ImportHistoriqueDao iDao){
      this.importHistoriqueDao = iDao;
   }

   public void setImportTemplateDao(final ImportTemplateDao iDao){
      this.importTemplateDao = iDao;
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
   public List<ImportHistorique> findAllObjectsManager(){
      log.debug("Recherche de tous les ImportHistoriques.");
      return importHistoriqueDao.findAll();
   }

   @Override
   public List<ImportHistorique> findByTemplateWithOrderManager(final ImportTemplate importTemplate){
      if(importTemplate != null){
         return importHistoriqueDao.findByTemplateWithOrder(importTemplate);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Importation> findImportationsByHistoriqueManager(final ImportHistorique importHistorique){
      if(importHistorique != null){
         return importationDao.findByHistorique(importHistorique);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Importation> findImportationsByHistoriqueAndEntiteManager(final ImportHistorique importHistorique,
      final Entite entite){
      if(importHistorique != null && entite != null){
         return importationDao.findByHistoriqueAndEntite(importHistorique, entite);
      }else{
         return new ArrayList<>();
      }
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
         return importationDao.findByEntiteAndObjetId(e, id);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Importation> findImportationsByEntiteAndObjectIdManager(final Entite entite, final Integer objetId){
      if(entite != null && objetId != null){
         return importationDao.findByEntiteAndObjetId(entite, objetId);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public void createObjectManager(final ImportHistorique importHistorique, final ImportTemplate importTemplate,
      final Utilisateur utilisateur, final List<Importation> importations){
      // importTemplate required
      if(importTemplate != null){
         importHistorique.setImportTemplate(importTemplateDao.mergeObject(importTemplate));
      }else{
         log.warn("Objet obligatoire ImportTemplate manquant" + " lors de la création d'un ImportHistorique");
         throw new RequiredObjectIsNullException("ImportHistorique", "creation", "importTemplate");
      }

      // utilisateur required
      if(utilisateur != null){
         importHistorique.setUtilisateur(utilisateurDao.mergeObject(utilisateur));
      }else{
         log.warn("Objet obligatoire Utilisateur manquant" + " lors de la création d'un ImportHistorique");
         throw new RequiredObjectIsNullException("Utilisateur", "creation", "importTemplate");
      }

      if(importations != null){
         importHistorique.setImportations(new HashSet<Importation>());
         for(int i = 0; i < importations.size(); i++){
            final Importation imp = importations.get(i);
            imp.setImportHistorique(importHistorique);
            imp.setEntite(entiteDao.mergeObject(imp.getEntite()));
            importHistorique.getImportations().add(imp);
         }
      }

      importHistoriqueDao.createObject(importHistorique);
      log.info("Enregistrement objet ImportHistorique " + importHistorique.toString());
   }

   @Override
   public void removeObjectManager(final ImportHistorique importHistorique){
      if(importHistorique != null){
         importHistoriqueDao.removeObject(importHistorique.getImportHistoriqueId());
         log.info("Suppression de l'objet ImportHistorique : " + importHistorique.toString());
      }else{
         log.warn("Suppression d'un ImportHistorique null");
      }
   }

   @Override
   public void removeImportationManager(final Importation importation){
      if(importation != null){
         importationDao.removeObject(importation.getImportationId());
         log.info("Suppression de l'objet Importation : " + importation.toString());
      }else{
         log.warn("Suppression d'un Importation null");
      }
   }

   @Override
   public List<Prelevement> findPrelevementByImportHistoriqueManager(final ImportHistorique ih){
      return importHistoriqueDao.findPrelevementByImportHistorique(ih);
   }
}
