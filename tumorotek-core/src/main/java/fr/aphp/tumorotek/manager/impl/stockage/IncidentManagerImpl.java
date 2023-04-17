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
package fr.aphp.tumorotek.manager.impl.stockage;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteDao;
import fr.aphp.tumorotek.dao.stockage.IncidentDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.stockage.IncidentManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.stockage.IncidentValidator;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 *
 * Implémentation du manager du bean de domaine Incident.
 * Modifiée 11/10/2013 pour gérer les références d'un incident vers une
 * enceinte ou une terminale.
 * Interface créée le 17/03/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class IncidentManagerImpl implements IncidentManager
{

   private final Logger log = LoggerFactory.getLogger(IncidentManager.class);

   private IncidentDao incidentDao;

   private ConteneurDao conteneurDao;

   private EnceinteDao enceinteDao;

   private TerminaleDao terminaleDao;

   private IncidentValidator incidentValidator;

   /**
    * Setter du bean IncidentDao.
    * @param iDao est le bean Dao.
    */
   public void setIncidentDao(final IncidentDao iDao){
      this.incidentDao = iDao;
   }

   public void setConteneurDao(final ConteneurDao cDao){
      this.conteneurDao = cDao;
   }

   public ConteneurDao getConteneurDao(){
      return conteneurDao;
   }

   public void setEnceinteDao(final EnceinteDao ec){
      this.enceinteDao = ec;

   }

   public void setTerminaleDao(final TerminaleDao terminaleDao){
      this.terminaleDao = terminaleDao;
   }

   public void setIncidentValidator(final IncidentValidator iValidator){
      this.incidentValidator = iValidator;
   }

   @Override
   public Incident findByIdManager(final Integer incidentId){
      return incidentDao.findById(incidentId);
   }

   @Override
   public List<Incident> findAllObjectsManager(){
      return incidentDao.findAll();
   }

   @Override
   public List<Incident> findAllObjectsByConteneurManager(final Conteneur conteneur){
      log.debug("Recherche de tous les Incidents d'un conteneur");
      if(conteneur != null){
         return incidentDao.findByConteneurOrderByDate(conteneur);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Incident> findByEnceinteManager(final Enceinte enceinte){
      return incidentDao.findByEnceinte(enceinte);
   }

   @Override
   public List<Incident> findByTerminaleManager(final Terminale terminale){
      return incidentDao.findByTerminale(terminale);
   }

   @Override
   public Boolean findDoublonManager(final Incident incident){
      if(incident != null){
         if(incident.getConteneur() != null){
            if(incident.getIncidentId() == null){
               return incidentDao.findByConteneurOrderByDate(incident.getConteneur()).contains(incident);
            }else{
               return incidentDao.findByExcludedIdAndConteneur(incident.getIncidentId(), incident.getConteneur())
                  .contains(incident);
            }
         }else if(incident.getEnceinte() != null){
            if(incident.getIncidentId() == null){
               return incidentDao.findByEnceinte(incident.getEnceinte()).contains(incident);
            }else{
               return incidentDao.findByExcludedIdAndEnceinte(incident.getIncidentId(), incident.getEnceinte())
                  .contains(incident);
            }
         }else if(incident.getTerminale() != null){
            if(incident.getIncidentId() == null){
               return incidentDao.findByTerminale(incident.getTerminale()).contains(incident);
            }else{
               return incidentDao.findByExcludedIdAndTerminale(incident.getIncidentId(), incident.getTerminale())
                  .contains(incident);
            }
         }
      }
      return false;
   }

   /**
    * Recherche les doublons dans une liste d'Incidents.
    * @param incidents Liste d'incidents.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonInListManager(final List<Incident> incidents){
      boolean doublon = false;

      for(int i = 0; i < incidents.size(); i++){
         final Incident inc = incidents.get(i);

         for(int j = i + 1; j < incidents.size(); j++){
            if(inc.equals(incidents.get(j))){
               doublon = true;
            }
         }
      }
      return doublon;
   }

   @Override
   public void createObjectManager(final Incident incident, final Conteneur conteneur, final Enceinte enceinte,
      final Terminale terminale){

      //contenant required
      if(conteneur != null){
         incident.setConteneur(conteneurDao.mergeObject(conteneur));
      }else if(enceinte != null){
         incident.setEnceinte(enceinteDao.mergeObject(enceinte));
      }else if(terminale != null){
         incident.setTerminale(terminaleDao.mergeObject(terminale));
      }else{
         log.warn("Objet obligatoire Conteneur/Enceinte/Terminale manquant" + " lors de la création d'un Incident");
         throw new RequiredObjectIsNullException("Incident", "creation", "Conteneur/Enceinte/Terminale");
      }

      // Test s'il y a des doublons
      if(findDoublonManager(incident)){
         log.warn("Doublon lors de la creation de l'objet Incident : " + incident.toString());
         throw new DoublonFoundException("Incident", "creation");
      }else{

         // validation du Contrat
         BeanValidator.validateObject(incident, new Validator[] {incidentValidator});

         incidentDao.createObject(incident);

         log.info("Enregistrement de l'objet Incident : " + incident.toString());
      }
   }

   @Override
   public void updateObjectManager(final Incident incident, final Conteneur conteneur, final Enceinte enceinte,
      final Terminale terminale){

      //contenant required
      if(conteneur != null){
         incident.setConteneur(conteneurDao.mergeObject(conteneur));
         incident.setEnceinte(null);
         incident.setTerminale(null);
      }else if(enceinte != null){
         incident.setEnceinte(enceinteDao.mergeObject(enceinte));
         incident.setConteneur(null);
         incident.setTerminale(null);
      }else if(terminale != null){
         incident.setConteneur(null);
         incident.setEnceinte(null);
         incident.setTerminale(terminaleDao.mergeObject(terminale));
      }else{
         log.warn("Objet obligatoire Conteneur/Enceinte/Terminale manquant" + " lors de la modification d'un Incident");
         throw new RequiredObjectIsNullException("Incident", "modification", "Conteneur/Enceinte/Terminale");
      }

      // Test s'il y a des doublons
      if(findDoublonManager(incident)){
         log.warn("Doublon lors de la modification de l'objet Incident : " + incident.toString());
         throw new DoublonFoundException("Incident", "modification");
      }else{

         // validation du Contrat
         BeanValidator.validateObject(incident, new Validator[] {incidentValidator});

         incidentDao.updateObject(incident);

         log.info("Modification de l'objet Incident : " + incident.toString());
      }
   }

   @Override
   public void removeObjectManager(final Incident incident){
      if(incident != null){
         incidentDao.removeObject(incident.getIncidentId());
         log.info("Suppression de l'objet Incident : " + incident.toString());
      }else{
         log.warn("Suppression d'un Incident null");
      }
   }
}