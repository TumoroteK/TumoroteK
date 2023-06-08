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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurPlateformeDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurTypeDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.stockage.IncidentManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.stockage.ConteneurValidator;
import fr.aphp.tumorotek.manager.validation.stockage.IncidentValidator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateforme;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateformePK;
import fr.aphp.tumorotek.model.stockage.ConteneurType;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 * @version 2.2.1-IRELEC
 * @author Mathieu BARTHELEMY
 *
 */
public class ConteneurManagerImpl implements ConteneurManager
{

   private final Logger log = LoggerFactory.getLogger(ConteneurManager.class);

   private ConteneurDao conteneurDao;

   private ConteneurTypeDao conteneurTypeDao;

   private ServiceDao serviceDao;

   private BanqueDao banqueDao;

   private EnceinteManager enceinteManager;

   private TerminaleManager terminaleManager;

   private IncidentManager incidentManager;

   private ConteneurValidator conteneurValidator;

   private IncidentValidator incidentValidator;

   private OperationManager operationManager;

   private OperationTypeDao operationTypeDao;

   private ConteneurPlateformeDao conteneurPlateformeDao;

   public void setConteneurDao(final ConteneurDao cDao){
      this.conteneurDao = cDao;
   }

   public void setConteneurTypeDao(final ConteneurTypeDao cDao){
      this.conteneurTypeDao = cDao;
   }

   public void setServiceDao(final ServiceDao sDao){
      this.serviceDao = sDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEnceinteManager(final EnceinteManager eManager){
      this.enceinteManager = eManager;
   }

   public void setTerminaleManager(final TerminaleManager tManager){
      this.terminaleManager = tManager;
   }

   public void setConteneurValidator(final ConteneurValidator cValidator){
      this.conteneurValidator = cValidator;
   }

   public void setConteneurPlateformeDao(final ConteneurPlateformeDao cd){
      this.conteneurPlateformeDao = cd;
   }

   public IncidentManager getIncidentManager(){
      return incidentManager;
   }

   public void setIncidentManager(final IncidentManager iManager){
      this.incidentManager = iManager;
   }

   public IncidentValidator getIncidentValidator(){
      return incidentValidator;
   }

   public void setIncidentValidator(final IncidentValidator iValidator){
      this.incidentValidator = iValidator;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   @Override
   public Conteneur findByIdManager(final Integer conteneurId){
      return conteneurDao.findById(conteneurId);
   }

   @Override
   public List<Conteneur> findAllObjectsManager(){
      return conteneurDao.findAll();
   }

   @Override
   public List<Conteneur> findByBanqueWithOrderManager(final Banque banque){
      log.debug("Recherche de tous les conteneurs d'une banque");
      if(banque != null && banque.getBanqueId() != null){
         return conteneurDao.findByBanqueIdWithOrder(banque.getBanqueId());
      }
      return new ArrayList<>();
   }

   @Override
   public List<Conteneur> findByBanqueAndCodeManager(final Banque banque, final String code){
      if(banque != null && banque.getBanqueId() != null && code != null){
         return conteneurDao.findByBanqueIdAndCode(banque.getBanqueId(), code);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Conteneur> findByBanquesWithOrderManager(final List<Banque> banques){
      log.debug("Recherche de tous les conteneurs d'une liste de banque");
      if(banques != null && banques.size() > 0){
         final List<Conteneur> coList = new ArrayList<>();
         for(int i = 0; i < banques.size(); i++){
            final Banque banque = banques.get(i);
            if(banque != null && banque.getBanqueId() != null){
               final List<Conteneur> tmp = conteneurDao.findByBanqueIdWithOrder(banque.getBanqueId());

               for(int j = 0; j < tmp.size(); j++){
                  if(!coList.contains(tmp.get(j))){
                     coList.add(tmp.get(j));
                  }
               }
            }
         }
         return coList;
      }
      return new ArrayList<>();
   }

   @Override
   public List<Conteneur> findByPlateformeOrigWithOrderManager(final Plateforme plateforme){
      if(plateforme != null && plateforme.getPlateformeId() != null){
         return conteneurDao.findByPlateformeOrigWithOrder(plateforme);
      }
      return new ArrayList<>();
   }

   @Override
   public Set<Banque> getBanquesManager(Conteneur conteneur){
      if(conteneur != null && conteneur.getConteneurId() != null){
         log.debug("Recherche de toutes les banques d'un conteneur");
         conteneur = conteneurDao.mergeObject(conteneur);
         final Set<Banque> banques = conteneur.getBanques();
         banques.size();

         return banques;
      }
      return new HashSet<>();
   }

   @Override
   public Set<Enceinte> getEnceintesManager(final Conteneur conteneur){
      if(conteneur != null){
         if(conteneur.getConteneurId() != null){
            return new HashSet<>(enceinteManager.findByConteneurWithOrderManager(conteneur));
         }
         return conteneur.getEnceintes();
      }
      return new HashSet<>();
   }

   @Override
   public List<Terminale> getAllTerminalesInArborescenceManager(final Conteneur conteneur){
      final List<Terminale> terminales = new ArrayList<>();
      if(conteneur != null && conteneur.getConteneurId() != null){
         final List<Enceinte> enceintes = enceinteManager.findByConteneurWithOrderManager(conteneur);

         for(int i = 0; i < enceintes.size(); i++){
            terminales.addAll(enceinteManager.getAllTerminalesInArborescenceManager(enceintes.get(i)));
         }
      }
      return terminales;
   }

   @Override
   public Set<Incident> getIncidentsManager(Conteneur conteneur){
      if(conteneur != null && conteneur.getConteneurId() != null){
         log.debug("Recherche de tous les incidents d'un conteneur");
         conteneur = conteneurDao.mergeObject(conteneur);
         final Set<Incident> incidents = conteneur.getIncidents();
         incidents.size();

         return incidents;
      }
      return new HashSet<>();
   }

   @Override
   public Set<ConteneurPlateforme> getConteneurPlateformesManager(Conteneur conteneur){
      if(conteneur != null && conteneur.getConteneurId() != null){
         conteneur = conteneurDao.mergeObject(conteneur);
         final Set<ConteneurPlateforme> plateformes = conteneur.getConteneurPlateformes();
         plateformes.size();

         return plateformes;
      }
      return new HashSet<>();
   }

   @Override
   public Boolean findDoublonManager(final Conteneur conteneur, final List<Banque> banques){
      if(conteneur != null && banques != null){
         final List<Conteneur> conteneurs = new ArrayList<>();
         if(conteneur.getConteneurId() == null){
            for(int i = 0; i < banques.size(); i++){
               conteneurs.addAll(conteneurDao.findByBanqueIdWithOrder(banques.get(i).getBanqueId()));
            }
         }else{
            for(int i = 0; i < banques.size(); i++){
               conteneurs
                  .addAll(conteneurDao.findByBanqueIdWithExcludedId(banques.get(i).getBanqueId(), conteneur.getConteneurId()));
            }
         }
         return conteneurs.contains(conteneur);
      }
      return false;
   }

   @Override
   public Boolean isUsedObjectManager(final Conteneur conteneur){
      boolean isUsed = false;
      if(conteneur != null && conteneur.getConteneurId() != null){
         final Iterator<Enceinte> it = getEnceintesManager(conteneur).iterator();

         while(it.hasNext()){
            final Enceinte tmp = it.next();
            if(enceinteManager.isUsedObjectManager(tmp)){
               isUsed = true;
            }
         }
      }
      return isUsed;
   }

   @Override
   public void createObjectManager(final Conteneur conteneur, final ConteneurType conteneurType, final Service service,
      final List<Banque> banques, final List<Plateforme> plateformes, final Utilisateur utilisateur, final Plateforme pfOrig){

      // Service required
      if(service != null){
         conteneur.setService(serviceDao.mergeObject(service));
      }else{
         log.warn("Objet obligatoire Service manquant  lors de la création d'un Conteneur");
         throw new RequiredObjectIsNullException("Conteneur", "creation", "Service");
      }

      // Plateforme Origine required
      if(pfOrig != null){
         conteneur.setPlateformeOrig(pfOrig);
      }else{
         log.warn("Objet obligatoire Plateforme manquant  lors de la création d'un Conteneur");
         throw new RequiredObjectIsNullException("Conteneur", "creation", "Plateforme");
      }

      conteneur.setConteneurType(conteneurTypeDao.mergeObject(conteneurType));

      // Test s'il y a des doublons
      if(findDoublonManager(conteneur, banques)){
         log.warn("Doublon lors de la creation de l'objet Conteneur : {}",  conteneur);
         throw new DoublonFoundException("Conteneur", "creation");
      }

      // validation du Contrat
      BeanValidator.validateObject(conteneur, new Validator[] {conteneurValidator});

      conteneur.setArchive(false);
      conteneurDao.createObject(conteneur);

      updateBanquesAndPlateformes(conteneur, banques, plateformes);

      log.info("Enregistrement de l'objet Conteneur : " + conteneur.toString());

      //Enregistrement de l'operation associee
      final Operation creationOp = new Operation();
      creationOp.setDate(Utils.getCurrentSystemCalendar());
      operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Creation").get(0), conteneur);
   }

   @Override
   public void updateObjectManager(final Conteneur conteneur, final ConteneurType conteneurType, final Service service,
      final List<Banque> banques, final List<Plateforme> plateformes, final List<Incident> incidents,
      final Utilisateur utilisateur){

      // Service required
      if(service != null){
         conteneur.setService(serviceDao.mergeObject(service));
      }else{
         log.warn("Objet obligatoire Service manquant  lors de la modification d'un Conteneur");
         throw new RequiredObjectIsNullException("Conteneur", "modification", "Service");
      }

      conteneur.setConteneurType(conteneurTypeDao.mergeObject(conteneurType));

      if(incidents != null){
         // maj des incidents
         // on vérifie qu'il n'y a pas de doublons dans la liste
         if(incidentManager.findDoublonInListManager(incidents)){
            log.warn("Doublon dans la liste des incidents");
            throw new DoublonFoundException("Incident", "modification");
         }
         // pour chaque coordonnée
         for(int i = 0; i < incidents.size(); i++){
            final Incident incident = incidents.get(i);
            // validation de la coordonnée
            BeanValidator.validateObject(incident, new Validator[] {incidentValidator});

            // si nouvel incident => creation
            // sinon => update
            if(incident.getIncidentId() == null){
               incidentManager.createObjectManager(incident, conteneur, null, null);
            }else{
               incidentManager.updateObjectManager(incident, conteneur, null, null);
            }
         }
      }

      // Test s'il y a des doublons
      if(findDoublonManager(conteneur, banques)){
         log.warn("Doublon lors de la modification de l'objet Conteneur : {}",  conteneur);
         throw new DoublonFoundException("Conteneur", "modification");
      }
      // validation du Contrat
      BeanValidator.validateObject(conteneur, new Validator[] {conteneurValidator});

      conteneurDao.updateObject(conteneur);

      updateBanquesAndPlateformes(conteneur, banques, plateformes);

      log.info("Modification de l'objet Conteneur : " + conteneur.toString());

      //Enregistrement de l'operation associee
      final Operation creationOp = new Operation();
      creationOp.setDate(Utils.getCurrentSystemCalendar());
      operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Modification").get(0), conteneur);
   }

   @Override
   public void removeObjectManager(final Conteneur conteneur, final String comments, final Utilisateur user){
      if(conteneur != null){
         if(isUsedObjectManager(conteneur)){
            log.warn("Objet utilisé lors de la suppression de l'objet " + "Conteneur : " + conteneur.toString());
            throw new ObjectUsedException("conteneur.deletion.isUsed", false);
         }
         // suppression manytomany
         final Iterator<Banque> it = getBanquesManager(conteneur).iterator();
         Banque bank;
         while(it.hasNext()){
            bank = banqueDao.mergeObject(it.next());
            bank.getConteneurs().remove(conteneur);
         }

         final Iterator<Enceinte> itE = getEnceintesManager(conteneur).iterator();
         while(itE.hasNext()){
            final Enceinte tmp = itE.next();
            enceinteManager.removeObjectManager(tmp, comments, user);
         }

         // suppression conteneur vide ssi évènements de stockage
         if(!hasRetoursManager(conteneur)){
            conteneurDao.removeObject(conteneur.getConteneurId());

            //Supprime operations associes
            CreateOrUpdateUtilities.removeAssociateOperations(conteneur, operationManager, comments, user);

            log.info("Suppression de l'objet Conteneur : " + conteneur.toString());
         }else{// archivage sinon
            conteneur.setArchive(true);
            conteneurDao.mergeObject(conteneur);
            log.info("Archivage automatique de l'objet Conteneur : " + conteneur.toString());
         }
      }else{
         log.warn("Suppression d'un Conteneur null");
      }
   }

   @Override
   public boolean hasRetoursManager(Conteneur conteneur){
      if(conteneur != null && conteneur.getConteneurId() != null){
         conteneur = conteneurDao.mergeObject(conteneur);
         return !conteneur.getRetours().isEmpty();
      }
      return false;
   }

   /**
    * Cette méthode met à jour les associations entre un conteneur,
    * une liste de banques et une liste de plateformes.
    * @param conteneur Conteneur pour lequel on veut mettre à jour
    * les associations.
    * @param banques Liste des banques que l'on veut associer au
    * conteneur.
    * @param plateformes Liste des plateformes auxquels l'accès au
    * conteneur pourra être partagé.
    */
   public void updateBanquesAndPlateformes(final Conteneur conteneur, final List<Banque> banques,
      final List<Plateforme> plateformes){
      final Conteneur cont = conteneurDao.mergeObject(conteneur);

      if(banques != null){
         final Iterator<Banque> it = cont.getBanques().iterator();
         final List<Banque> banquesToRemove = new ArrayList<>();
         // on parcourt les Banques qui sont actuellement associés
         // au conteneur
         while(it.hasNext()){
            final Banque tmp = it.next();
            // si une Banque n'est pas dans la nouvelle liste, on
            // la conserve afin de la retirer par la suite
            if(!banques.contains(tmp)){
               banquesToRemove.add(tmp);
            }
         }

         // on parcourt la liste des Banques à retirer de
         // l'association
         for(int i = 0; i < banquesToRemove.size(); i++){
            final Banque bank = banqueDao.mergeObject(banquesToRemove.get(i));
            // on retire la Banque de chaque coté de l'association
            cont.getBanques().remove(bank);
            bank.getConteneurs().remove(cont);

            log.debug("Suppression de l'association entre le conteneur : {} et la banque : {}", cont, bank);

         }

         // on parcourt la nouvelle liste de banques
         for(int i = 0; i < banques.size(); i++){
            // si une banque n'était pas associée au conteneur
            if(!cont.getBanques().contains(banques.get(i))){
               // on ajoute la Banque des deux cotés de l'association
               cont.getBanques().add(banqueDao.mergeObject(banques.get(i)));
               banqueDao.mergeObject(banques.get(i)).getConteneurs().add(cont);

               log.debug("Ajout de l'association entre le Conteneur : {} et la banque : {}", cont, banques.get(i));

            }
         }
      }

      if(plateformes != null){
         final Iterator<ConteneurPlateforme> it = cont.getConteneurPlateformes().iterator();
         final List<ConteneurPlateforme> pfsToRemove = new ArrayList<>();
         // on parcourt les Plateformes qui sont actuellement associés
         // au conteneur
         while(it.hasNext()){
            final ConteneurPlateforme tmp = it.next();
            // si une Plateforme n'est pas dans la nouvelle liste, on
            // la conserve afin de la retirer par la suite
            if(!plateformes.contains(tmp.getPlateforme()) && !tmp.getPartage()){
               pfsToRemove.add(tmp);
            }
         }

         // on parcourt la liste des Plateformes à retirer de
         // l'association
         ConteneurPlateforme cpf;
         for(int i = 0; i < pfsToRemove.size(); i++){
            cpf = conteneurPlateformeDao.mergeObject(pfsToRemove.get(i));
            // on retire la pf de chaque coté de l'association
            cont.getConteneurPlateformes().remove(cpf);
            conteneurPlateformeDao.removeObject(cpf.getPk());

            log.debug("Suppression de l'association entre le conteneur : {} et la plateforme : {}", cont, cpf.getPlateforme());

         }

         // on parcourt la nouvelle liste de plateformes
         for(int i = 0; i < plateformes.size(); i++){
            final ConteneurPlateforme cp = new ConteneurPlateforme();
            final ConteneurPlateformePK pk = new ConteneurPlateformePK(cont, plateformes.get(i));
            cp.setPk(pk);
            // si une plateforme n'était pas associé au conteneur
            if(!cont.getConteneurPlateformes().contains(cp)){
               cont.getConteneurPlateformes().add(conteneurPlateformeDao.mergeObject(cp));

               log.debug("Ajout de l'association entre le conteneur : {} et la plateforme : {}", cont, plateformes.get(i));

            }
         }
      }
   }

   @Override
   public void createAllArborescenceManager(final Conteneur conteneur, final List<Enceinte> enceintes, final Terminale terminale,
      final List<Integer> firstPositions, final List<Banque> banques, final List<Plateforme> plateformes,
      final Integer sizePaillettes, final boolean nameFromColor, final Utilisateur utilisateur, final Plateforme pfOrig){
      findAllObjectsManager();
      if(conteneur != null && enceintes != null && terminale != null && firstPositions != null
         && firstPositions.size() == enceintes.size() + 1){
         // création du conteneur
         createObjectManager(conteneur, conteneur.getConteneurType(), conteneur.getService(), banques, plateformes, utilisateur,
            pfOrig);

         // création des enceintes
         List<Enceinte> enceintesCrees = new ArrayList<>();
         for(int i = 0; i < enceintes.size(); i++){
            final Enceinte current = enceintes.get(i);
            final Integer currentPosition = firstPositions.get(i);

            // si 1ere enceinte, elle est le 1er niveau du conteneur
            if(i == 0){
               try{
                  // création des enceintes de 1er niveau
                  // les enceintes créées sont placées dans
                  // une liste
                  enceintesCrees = enceinteManager.createMultiObjetcsForConteneurManager(conteneur, current,
                     conteneur.getNbrEnc(), currentPosition, utilisateur);
               }catch(final RuntimeException e){
                  // en cas d'exception, on met l'id du
                  // conteneur à null
                  conteneur.setConteneurId(null);
                  throw e;
               }
            }else{
               // creation des enceintes de niveau > 1
               final List<Enceinte> enceintesCreesTmp = new ArrayList<>();
               for(int j = 0; j < enceintesCrees.size(); j++){
                  try{
                     enceintesCreesTmp.addAll(enceinteManager.createMultiObjetcsForEnceinteManager(enceintesCrees.get(j), current,
                        enceintesCrees.get(j).getNbPlaces(), currentPosition, utilisateur));
                  }catch(final RuntimeException e){
                     // en cas d'exception, on met l'id du
                     // conteneur à null
                     conteneur.setConteneurId(null);
                     throw e;
                  }
               }
               enceintesCrees = enceintesCreesTmp;
            }

         }

         final Integer currentPosition = firstPositions.get(firstPositions.size() - 1);
         // création des terminales
         for(int i = 0; i < enceintesCrees.size(); i++){
            try{
               if(sizePaillettes == null){
                  terminaleManager.createMultiObjetcsManager(enceintesCrees.get(i), terminale,
                     enceintesCrees.get(i).getNbPlaces(), currentPosition, utilisateur);
               }else{
                  terminaleManager.createMultiVisotubesManager(enceintesCrees.get(i), terminale,
                     enceintesCrees.get(i).getNbPlaces(), currentPosition, nameFromColor, sizePaillettes, utilisateur);
               }
            }catch(final RuntimeException e){
               // en cas d'exception, on met l'id du
               // conteneur à null
               conteneur.setConteneurId(null);
               throw e;
            }
         }
      }
   }

   @Override
   public List<Enceinte> getContainingEnceinteManager(final Conteneur conteneur){
      if(conteneur != null){
         return getChildrenEnceinteRecursive(getEnceintesManager(conteneur), new ArrayList<Enceinte>());
      }
      return new ArrayList<>();
   }

   /**
    * Parcoure les enceintes de manière récursives afin de
    * toutes les récupérer.
    * @param encs liste d'enceintes à partir desquelles on cherche
    * @param res liste d'enceintes resultats
    * @return liste d'enceintes resultats
    */
   private List<Enceinte> getChildrenEnceinteRecursive(final Set<Enceinte> encs, final List<Enceinte> res){
      res.addAll(encs);

      final Iterator<Enceinte> it = encs.iterator();
      while(it.hasNext()){
         getChildrenEnceinteRecursive(it.next().getEnceintes(), res);
      }

      return res;
   }

   @Override
   public void removeBanqueFromContAndEncManager(final Conteneur conteneur, final Banque banque){
      final Banque bank = banqueDao.mergeObject(banque);
      bank.getConteneurs().remove(conteneur);
      getBanquesManager(conteneur).remove(bank);

      final Iterator<Enceinte> it = getContainingEnceinteManager(conteneur).iterator();
      while(it.hasNext()){
         it.next().getBanques().remove(bank);
      }
   }

   @Override
   public Conteneur findFromEmplacementManager(final Emplacement empl){
      Conteneur cont = null;
      if(empl != null){
         Enceinte enc = empl.getTerminale().getEnceinte();
         while(cont == null){
            cont = enc.getConteneur();
            enc = enc.getEnceintePere();
         }
      }
      return cont;
   }

   @Override
   public List<Conteneur> findByPartageManager(final Plateforme pf, final Boolean partage){
      return conteneurDao.findByPartage(pf, partage);
   }

   @Override
   public Float findTempForEmplacementManager(final Emplacement emplacement){
      if(emplacement != null){
         final List<Float> temps = conteneurDao.findTempForEmplacementId(emplacement.getEmplacementId());
         if(!temps.isEmpty()){
            return temps.get(0);
         }
      }
      return null;
   }

   @Override
   public void updateObjectWithConteneurPlateformesManager(final Conteneur conteneur, final ConteneurType conteneurType,
      final Service service, final List<Banque> banques, final List<ConteneurPlateforme> conteneurPlateformes,
      final List<Incident> incidents, final Utilisateur utilisateur){

      List<Plateforme> plateformes = null;
      if(conteneurPlateformes != null){
         plateformes = new ArrayList<>();
         for(final ConteneurPlateforme cp : conteneurPlateformes){
            plateformes.add(cp.getPlateforme());
         }
      }

      updateObjectManager(conteneur, conteneurType, service, banques, plateformes, incidents, utilisateur);

      // met à jour les informations spécifiques au conteneurPlateforme (ex: restrictStock)
      if(conteneurPlateformes != null){
         for(final ConteneurPlateforme cp : conteneurPlateformes){
            conteneurPlateformeDao.mergeObject(cp);
         }
      }
   }

   @Override
   public ConteneurPlateforme getOneConteneurPlateformeManager(final Conteneur conteneur, final Plateforme pf){
      for(final ConteneurPlateforme cpf : getConteneurPlateformesManager(conteneur)){
         if(cpf.getPlateforme().equals(pf)){
            return cpf;
         }
      }
      return null;
   }
}
