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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteTypeDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.EnceinteSizeException;
import fr.aphp.tumorotek.manager.exception.IncoherenceException;
import fr.aphp.tumorotek.manager.exception.InvalidParentException;
import fr.aphp.tumorotek.manager.exception.InvalidPositionException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.UsedPositionException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.stockage.CheckPositionManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.stockage.IncidentManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.stockage.EnceinteValidator;
import fr.aphp.tumorotek.manager.validation.stockage.IncidentValidator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.EnceinteType;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.2.1
 */
public class EnceinteManagerImpl implements EnceinteManager
{

   private final Log log = LogFactory.getLog(EnceinteManager.class);

   private TerminaleDao terminaleDao;
   private EnceinteDao enceinteDao;
   private EnceinteTypeDao enceinteTypeDao;
   private ConteneurDao conteneurDao;
   private EntiteDao entiteDao;
   private BanqueDao banqueDao;
   private TerminaleManager terminaleManager;
   private EnceinteValidator enceinteValidator;
   private JpaTransactionManager txManager;
   private OperationManager operationManager;
   private OperationTypeDao operationTypeDao;
   private CheckPositionManager checkPositionManager;
   private CouleurDao couleurDao;
   private IncidentManager incidentManager;
   private IncidentValidator incidentValidator;

   public void setTerminaleDao(final TerminaleDao tDao){
      this.terminaleDao = tDao;
   }

   public void setEnceinteDao(final EnceinteDao eDao){
      this.enceinteDao = eDao;
   }

   public void setEnceinteTypeDao(final EnceinteTypeDao eDao){
      this.enceinteTypeDao = eDao;
   }

   public void setConteneurDao(final ConteneurDao cDao){
      this.conteneurDao = cDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setTerminaleManager(final TerminaleManager tManager){
      this.terminaleManager = tManager;
   }

   public void setEnceinteValidator(final EnceinteValidator eValidator){
      this.enceinteValidator = eValidator;
   }

   public void setTxManager(final JpaTransactionManager tManager){
      this.txManager = tManager;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setCheckPositionManager(final CheckPositionManager cManager){
      this.checkPositionManager = cManager;
   }

   public void setCouleurDao(final CouleurDao cDao){
      this.couleurDao = cDao;
   }

   public void setIncidentManager(final IncidentManager iM){
      this.incidentManager = iM;
   }

   public void setIncidentValidator(final IncidentValidator iV){
      this.incidentValidator = iV;
   }

   @Override
   public Enceinte findByIdManager(final Integer enceinteId){
      return enceinteDao.findById(enceinteId);
   }

   @Override
   public List<Enceinte> findAllObjectsManager(){
      return enceinteDao.findAll();
   }

   @Override
   public List<Enceinte> findByConteneurWithOrderManager(final Conteneur conteneur){
      log.debug("Recherche de toutes les enceintes d'un conteneur");
      if(conteneur != null){
         return enceinteDao.findByConteneurWithOrder(conteneur);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Enceinte> findByEnceintePereWithOrderManager(final Enceinte enceintePere){
      log.debug("Recherche de toutes les enceintes d'une enceinte père");
      if(enceintePere != null){
         return enceinteDao.findByEnceintePereWithOrder(enceintePere);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Enceinte> findByConteneurAndNomManager(final Conteneur conteneur, final String nom){
      if(conteneur != null && nom != null){
         return enceinteDao.findByConteneurAndNom(conteneur, nom);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Enceinte> findByEnceintePereAndNomManager(final Enceinte enceintePere, final String nom){
      if(enceintePere != null && nom != null){
         return enceinteDao.findByEnceintePereAndNom(enceintePere, nom);
      }
      return new ArrayList<>();
   }

   @Override
   public List<String> usedNomsExceptOneManager(final Enceinte enceinte){
      if(enceinte != null){
         List<String> liste = new ArrayList<>();
         if(enceinte.getConteneur() != null){
            liste = enceinteDao.findByConteneurSelectNom(enceinte.getConteneur());
            if(liste.contains(enceinte.getNom())){
               liste.remove(enceinte.getNom());
            }
         }else if(enceinte.getEnceintePere() != null){
            liste = enceinteDao.findByEnceintePereSelectNom(enceinte.getEnceintePere());
            if(liste.contains(enceinte.getNom())){
               liste.remove(enceinte.getNom());
            }
         }
         return liste;
      }
      return new ArrayList<>();
   }

   @Override
   public Set<Banque> getBanquesManager(Enceinte enceinte){
      if(enceinte != null && enceinte.getEnceinteId() != null){
         log.debug("Recherche de toutes les banques d'une enceinte");
         enceinte = enceinteDao.mergeObject(enceinte);
         final Set<Banque> banques = enceinte.getBanques();
         banques.size();

         return banques;
      }
      return new HashSet<>();
   }

   @Override
   public Set<Enceinte> getEnceintesManager(Enceinte enceintePere){
      if(enceintePere != null && enceintePere.getEnceinteId() != null){
         log.debug("Recherche de toutes les enceintes d'une enceinte");
         enceintePere = enceinteDao.mergeObject(enceintePere);
         final Set<Enceinte> enceintes = enceintePere.getEnceintes();
         enceintes.size();

         return enceintes;
      }
      return new HashSet<>();
   }

   @Override
   public Set<Terminale> getTerminalesManager(Enceinte enceinte){
      if(enceinte != null && enceinte.getEnceinteId() != null){
         log.debug("Recherche de toutes les terminales d'une enceinte");
         enceinte = enceinteDao.mergeObject(enceinte);
         final Set<Terminale> terms = enceinte.getTerminales();
         terms.size();

         return terms;
      }
      return new HashSet<>();
   }

   @Override
   public Boolean checkEnceinteInEnceintePereLimitesManager(final Enceinte enceinte){
      Boolean valide = true;
      // on vérifie que l'enceinte n'est pas nulle
      if(enceinte != null){
         final Enceinte enceintePere = enceinte.getEnceintePere();

         if(enceintePere != null && enceinte.getPosition() != null){
            // sa position doit être contenue dans les limites de
            // l'enceinte
            final Integer nbPlaces = enceintePere.getNbPlaces();
            if(enceinte.getPosition() > nbPlaces){
               valide = false;
            }
         }else{
            valide = false;
         }

      }else{
         valide = false;
      }

      return valide;
   }

   @Override
   public Boolean checkEnceinteInConteneurLimitesManager(final Enceinte enceinte){
      Boolean valide = true;
      // on vérifie que l'enceinte n'est pas nulle
      if(enceinte != null){
         final Conteneur conteneur = enceinte.getConteneur();

         if(conteneur != null && enceinte.getPosition() != null){
            // sa position doit être contenue dans les limites du
            // conteneur
            final Integer nbPlaces = conteneur.getNbrEnc();
            if(enceinte.getPosition() > nbPlaces){
               valide = false;
            }
         }else{
            valide = false;
         }

      }else{
         valide = false;
      }

      return valide;
   }

   @Override
   public Integer getLevelEnceinte(final Enceinte enceinte){
      int level = 0;

      if(enceinte != null){
         ++level;
         Enceinte enc = enceinte;
         while(enc.getEnceintePere() != null){
            enc = enc.getEnceintePere();
            ++level;
         }
      }

      return level;
   }

   /**
    * Teste si l'enceinte passée en paramètre est la dernière de
    * l'arborescence (ses filles sont des terminales).
    * @param enceinte Enceinte à tester.
    * @return True si c'est la dernière enceinte.
    */
   @Override
   public Boolean checkLastEnceinte(final Enceinte enceinte){
      int level = 1;

      if(enceinte != null){
         Enceinte enc = enceinte;
         while(enc.getEnceintePere() != null){
            enc = enc.getEnceintePere();
            ++level;
         }

         if(enc.getConteneur() != null){
            return ((enc.getConteneur().getNbrNiv() - level) == 1);
         }
         return false;
      }
      return false;
   }

   /**
    * Compte le nombre d'emplacements libres dans l'enceinte.
    * @param enceinte Enceinte.
    * @return Le nombre d'emplacements libre.
    */
   @Override
   public Long getNumberEmplacementsLibres(final Enceinte enceinte){
      Long nbLibres = (long) 0;
      if(enceinte != null && enceinte.getEnceinteId() != null){

         if(checkLastEnceinte(enceinte)){
            final List<Terminale> terms = terminaleDao.findByEnceinteWithOrder(enceinte);

            for(int i = 0; i < terms.size(); i++){
               nbLibres = nbLibres + terminaleManager.getNumberEmplacementsLibresManager(terms.get(i));
            }
         }else{
            final List<Enceinte> encs = findByEnceintePereWithOrderManager(enceinte);
            for(int i = 0; i < encs.size(); i++){
               nbLibres = nbLibres + getNumberEmplacementsLibres(encs.get(i));
            }
         }
      }

      return nbLibres;
   }

   /**
    * Crée l'Adrl de l'enceinte passée en paramètre.
    * @param enceinte Enceinte pour laquelle on souhaite
    * créer l'Adrl.
    * @return Adrl de l'enceinte.
    */
   @Override
   public String getAdrlManager(final Enceinte enceinte){
      if(enceinte != null && enceinte.getEnceinteId() != null){

         final StringBuffer adrl = new StringBuffer();
         Enceinte enc = enceinte;
         while(enc.getEnceintePere() != null){
            enc = enc.getEnceintePere();
            adrl.insert(0, enc.getNom());
            adrl.insert(0, ".");
         }

         if(enc.getConteneur() != null){
            adrl.insert(0, enc.getConteneur().getCode());
         }
         return adrl.toString();

      }
      return "";
   }

   @Override
   public Conteneur getConteneurManager(final Enceinte enceinte){
      if(enceinte != null){
         Enceinte enc = enceinte;
         Conteneur cont = null;
         while(enc.getEnceintePere() != null){
            enc = enc.getEnceintePere();
         }

         if(enc.getConteneur() != null){
            cont = enc.getConteneur();
         }

         return cont;

      }
      return null;
   }

   @Override
   public Boolean findDoublonManager(final Enceinte enceinte){
      if(enceinte != null){
         // si l'enceinte est issue d'un conteneur
         if(enceinte.getConteneur() != null){
            final Conteneur conteneur = enceinte.getConteneur();
            if(enceinte.getEnceinteId() != null){
               return enceinteDao.findByExcludedIdWithConteneur(enceinte.getEnceinteId(), conteneur).contains(enceinte);
            }
            return enceinteDao.findByConteneurWithOrder(conteneur).contains(enceinte);
         }else if(enceinte.getEnceintePere() != null){
            // si l'enceinte est issue d'une enceinte
            final Enceinte enceintePere = enceinte.getEnceintePere();
            if(enceinte.getEnceinteId() != null){
               return enceinteDao.findByExcludedIdWithEnceinte(enceinte.getEnceinteId(), enceintePere).contains(enceinte);
            }
            return enceinteDao.findByEnceintePereWithOrder(enceintePere).contains(enceinte);
         }else{
            return false;
         }
      }
      return false;
   }

   /**
    * Recherche les doublons de l'Enceinte passée en paramètre en
    * excluant une enceinte de cette recherche.
    * @param enceinte Enceinte pour laquelle on cherche des doublons.
    * @param enceinteToExclude Enceinte à exclure.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonWithoutTwoEnceintesManager(final Enceinte enceinte, final Enceinte enceinteDestination){
      if(enceinte != null && enceinteDestination != null && enceinte.getEnceinteId() != null
         && enceinteDestination.getEnceinteId() != null){
         // si l'enceinte est issue d'un conteneur
         if(enceinteDestination.getConteneur() != null){
            final Conteneur conteneur = enceinte.getConteneur();

            return enceinteDao
               .findByTwoExcludedIdsWithConteneur(enceinteDestination.getEnceinteId(), enceinte.getEnceinteId(), conteneur)
               .contains(enceinte);
         }else if(enceinteDestination.getEnceintePere() != null){
            // si l'enceinte est issue d'une enceinte
            final Enceinte enceintePere = enceinte.getEnceintePere();
            return enceinteDao
               .findByTwoExcludedIdsWithEnceinte(enceinteDestination.getEnceinteId(), enceinte.getEnceinteId(), enceintePere)
               .contains(enceinte);
         }else{
            return false;
         }
      }
      return false;
   }

   @Override
   public Boolean isUsedObjectManager(final Enceinte enceinte){
      boolean isUsed = false;
      if(enceinte != null && enceinte.getEnceinteId() != null){
         final List<Terminale> terms = terminaleDao.findByEnceinteWithOrder(enceinte);

         if(terms.size() > 0){
            for(int i = 0; i < terms.size(); i++){
               if(terminaleManager.isUsedObjectManager(terms.get(i))){
                  isUsed = true;
               }
            }
         }else{
            final List<Enceinte> encs = findByEnceintePereWithOrderManager(enceinte);
            for(int i = 0; i < encs.size(); i++){
               if(isUsedObjectManager(encs.get(i))){
                  isUsed = true;
               }
            }
         }
      }
      return isUsed;
   }

   @Override
   public void createObjectManager(final Enceinte enceinte, final EnceinteType enceinteType, final Conteneur conteneur,
      final Enceinte enceintePere, final Entite entite, final List<Banque> banques, final Couleur couleur,
      final Utilisateur utilisateur){

      // EnceinteType required
      if(enceinteType != null){
         enceinte.setEnceinteType(enceinteTypeDao.mergeObject(enceinteType));
      }else{
         log.warn("Objet obligatoire EnceinteType manquant" + " lors de la création d'une Enceinte");
         throw new RequiredObjectIsNullException("Enceinte", "creation", "EnceinteType");
      }

      // il faut que soit le conteneur soit une enceintePere
      // soit défini
      if(enceintePere != null && conteneur != null){
         log.warn("Deux parents sont définis" + " lors de la création d'une Enceinte");
         throw new InvalidParentException("Enceinte", "creation", false);
      }else if(enceintePere == null && conteneur == null){
         log.warn("Aucun parent n'est défini" + " lors de la création d'une Enceinte");
         throw new InvalidParentException("Enceinte", "creation", true);
      }

      // on associe tous les objets
      enceinte.setConteneur(conteneurDao.mergeObject(conteneur));
      enceinte.setEnceintePere(enceinteDao.mergeObject(enceintePere));
      enceinte.setEntite(entiteDao.mergeObject(entite));
      enceinte.setCouleur(couleurDao.mergeObject(couleur));

      // Test de la position : dans les limites du parent
      if(enceinte.getConteneur() != null){
         if(!checkEnceinteInConteneurLimitesManager(enceinte)){
            log.warn("La position n'est pas dans la limite des places du " + "conteneur parent");
            throw new InvalidPositionException("Enceinte", "creation", enceinte.getPosition());
         }
      }else if(enceinte.getEnceintePere() != null){
         if(!checkEnceinteInEnceintePereLimitesManager(enceinte)){
            log.warn("La position n'est pas dans la limite des places de " + "l'enceinte parente");
            throw new InvalidPositionException("Enceinte", "creation", enceinte.getPosition());
         }
      }

      // Test de la position : vide
      if(enceinte.getConteneur() != null){
         if(!checkPositionManager.checkPositionLibreInConteneurManager(enceinte.getConteneur(), enceinte.getPosition(), null)){
            log.warn("La position est déjà occupée par un " + "autre objet dans " + "le conteneur parent");
            throw new UsedPositionException("Enceinte", "creation", enceinte.getPosition());
         }
      }else if(enceinte.getEnceintePere() != null){
         if(!checkPositionManager.checkPositionLibreInEnceinteManager(enceinte.getEnceintePere(), enceinte.getPosition(), null,
            null)){
            log.warn("La position est déjà occupée par un " + "autre objet dans " + "l'enceinte parent");
            throw new UsedPositionException("Enceinte", "creation", enceinte.getPosition());
         }
      }

      // Test s'il y a des doublons
      if(findDoublonManager(enceinte)){
         log.warn("Doublon lors de la creation de l'objet Enceinte : " + enceinte.toString());
         throw new DoublonFoundException("Enceinte", "creation");
      }

      // validation du Contrat
      BeanValidator.validateObject(enceinte, new Validator[] {enceinteValidator});

      enceinte.setArchive(false);
      enceinteDao.createObject(enceinte);

      if(banques != null){
         // verifie la coherence avec conteneur
         for(int i = 0; i < banques.size(); i++){
            if(!getConteneurParent(enceinte).getBanques().contains(banques.get(i))){
               enceinte.setEnceinteId(null);
               throw new IncoherenceException("Enceinte", "enceinteBanque");
            }
         }
         updateBanques(enceinte, banques);
      }

      log.info("Enregistrement de l'objet Enceinte : " + enceinte.toString());

      //Enregistrement de l'operation associee
      final Operation creationOp = new Operation();
      creationOp.setDate(Utils.getCurrentSystemCalendar());
      operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Creation").get(0), enceinte);
   }

   @Override
   public List<Enceinte> createMultiObjetcsForConteneurManager(final Conteneur conteneur, final Enceinte enceinte,
      final Integer number, Integer firstPosition, final Utilisateur utilisateur){
      final List<Enceinte> enceintes = new ArrayList<>();

      if(enceinte != null){
         //Conteneur required
         if(conteneur != null){
            if(number != null){
               if(number > conteneur.getNbrEnc()){
                  log.warn("La position n'est pas dans la limite " + "des places du " + "conteneur");
                  throw new InvalidPositionException("Enceinte", "creation", number);
               }

               for(int i = 0; i < number; i++){
                  final Enceinte enc = new Enceinte();
                  final StringBuffer nom = new StringBuffer(enceinte.getNom());
                  if(firstPosition != null){
                     nom.append(firstPosition);
                     ++firstPosition;
                  }else{
                     nom.append(i + 1);
                  }
                  enc.setNom(nom.toString());
                  enc.setPosition(i + 1);
                  if(enceinte.getAlias() != null){
                     final StringBuffer alias = new StringBuffer(enceinte.getAlias());
                     alias.append(i + 1);
                     enc.setAlias(alias.toString());
                  }
                  enc.setNbPlaces(enceinte.getNbPlaces());
                  enc.setArchive(false);

                  createObjectManager(enc, enceinte.getEnceinteType(), conteneur, null, enceinte.getEntite(), null, null,
                     utilisateur);

                  enceintes.add(enc);
               }
            }

         }else{
            log.warn("Objet obligatoire Conteneur manquant" + " lors de la creation d'une Enceinte");
            throw new RequiredObjectIsNullException("Enceinte", "creation", "Conteneur");
         }
      }

      return enceintes;
   }

   @Override
   public List<Enceinte> createMultiObjetcsForEnceinteManager(final Enceinte enceintePere, final Enceinte enceinte,
      final Integer number, Integer firstPosition, final Utilisateur utilisateur){
      final List<Enceinte> enceintes = new ArrayList<>();

      if(enceinte != null){
         //enceintePere required
         if(enceintePere != null){
            if(number != null){
               if(number > enceintePere.getNbPlaces()){
                  log.warn("La position n'est pas dans la limite " + "des places de " + "l'enceinte parent");
                  throw new InvalidPositionException("Enceinte", "creation", number);
               }

               for(int i = 0; i < number; i++){
                  final Enceinte enc = new Enceinte();
                  final StringBuffer nom = new StringBuffer(enceinte.getNom());
                  if(firstPosition != null){
                     nom.append(firstPosition);
                     ++firstPosition;
                  }else{
                     nom.append(i + 1);
                  }
                  enc.setNom(nom.toString());
                  enc.setPosition(i + 1);
                  if(enceinte.getAlias() != null){
                     final StringBuffer alias = new StringBuffer(enceinte.getAlias());
                     alias.append(i + 1);
                     enc.setAlias(alias.toString());
                  }
                  enc.setNbPlaces(enceinte.getNbPlaces());
                  enc.setArchive(false);

                  createObjectManager(enc, enceinte.getEnceinteType(), null, enceintePere, enceinte.getEntite(), null, null,
                     utilisateur);

                  enceintes.add(enc);
               }
            }

         }else{
            log.warn("Objet obligatoire EnceintePere manquant" + " lors de la creation d'une Enceinte");
            throw new RequiredObjectIsNullException("Enceinte", "creation", "EnceintePere");
         }
      }

      return enceintes;
   }

   @Override
   public void updateObjectManager(final Enceinte enceinte, final EnceinteType enceinteType, final Conteneur conteneur,
      final Enceinte enceintePere, final Entite entite, final List<Banque> banques, final Couleur couleur,
      final List<Incident> incidents, final Utilisateur utilisateur, final List<OperationType> operations){

      // EnceinteType required
      if(enceinteType != null){
         enceinte.setEnceinteType(enceinteTypeDao.mergeObject(enceinteType));
      }else{
         log.warn("Objet obligatoire EnceinteType manquant" + " lors de la modification d'une Enceinte");
         throw new RequiredObjectIsNullException("Enceinte", "modification", "EnceinteType");
      }

      // il faut que soit le conteneur soit une enceintePere
      // soit défini
      if(enceintePere != null && conteneur != null){
         log.warn("Deux parents sont définis" + " lors de la modification d'une Enceinte");
         throw new InvalidParentException("Enceinte", "modification", false);
      }else if(enceintePere == null && conteneur == null){
         log.warn("Aucun parent n'est défini" + " lors de la modification d'une Enceinte");
         throw new InvalidParentException("Enceinte", "modification", true);
      }

      // on associe tous les objets
      enceinte.setConteneur(conteneurDao.mergeObject(conteneur));
      enceinte.setEnceintePere(enceinteDao.mergeObject(enceintePere));
      enceinte.setEntite(entiteDao.mergeObject(entite));
      enceinte.setCouleur(couleurDao.mergeObject(couleur));

      // Test de la position : dans les limites du parent
      if(enceinte.getConteneur() != null){
         if(!checkEnceinteInConteneurLimitesManager(enceinte)){
            log.warn("La position n'est pas dans la limite des places du " + "conteneur parent");
            throw new InvalidPositionException("Enceinte", "modification", enceinte.getPosition());
         }
      }else if(enceinte.getEnceintePere() != null){
         if(!checkEnceinteInEnceintePereLimitesManager(enceinte)){
            log.warn("La position n'est pas dans la limite des places de " + "l'enceinte parente");
            throw new InvalidPositionException("Enceinte", "modification", enceinte.getPosition());
         }
      }

      // Test de la position : vide
      if(enceinte.getConteneur() != null){
         if(!checkPositionManager.checkPositionLibreInConteneurManager(enceinte.getConteneur(), enceinte.getPosition(),
            enceinte.getEnceinteId())){
            log.warn("La position est déjà occupée par un " + "autre objet dans " + "le conteneur parent");
            throw new UsedPositionException("Enceinte", "modification", enceinte.getPosition());
         }
      }else if(enceinte.getEnceintePere() != null){
         if(!checkPositionManager.checkPositionLibreInEnceinteManager(enceinte.getEnceintePere(), enceinte.getPosition(), null,
            enceinte.getEnceinteId())){
            log.warn("La position est déjà occupée par un " + "autre objet dans " + "l'enceinte parent");
            throw new UsedPositionException("Enceinte", "modification", enceinte.getPosition());
         }
      }

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
               incidentManager.createObjectManager(incident, null, enceinte, null);
            }else{
               incidentManager.updateObjectManager(incident, null, enceinte, null);
            }
         }
      }

      // Test s'il y a des doublons
      if(findDoublonManager(enceinte)){
         log.warn("Doublon lors de la modification de l'objet Enceinte : " + enceinte.toString());
         throw new DoublonFoundException("Enceinte", "modification");
      }

      // validation du Contrat
      BeanValidator.validateObject(enceinte, new Validator[] {enceinteValidator});

      enceinteDao.updateObject(enceinte);

      if(banques != null){
         // verifie la coherence avec conteneur
         for(int i = 0; i < banques.size(); i++){
            if(!getConteneurParent(enceinte).getBanques().contains(banques.get(i))){
               throw new IncoherenceException("Enceinte", "enceinteBanque");
            }
         }
         updateBanques(enceinte, banques);
      }

      log.info("Modification de l'objet Enceinte : " + enceinte.toString());

      //Enregistrement de l'operation associee
      final Operation creationOp = new Operation();
      creationOp.setDate(Utils.getCurrentSystemCalendar());
      operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Modification").get(0), enceinte);

      if(operations != null){
         for(int i = 0; i < operations.size(); i++){
            //Enregistrement de l'operation associee
            final Operation dateOp = new Operation();
            dateOp.setDate(Utils.getCurrentSystemCalendar());
            operationManager.createObjectManager(dateOp, utilisateur, operations.get(i), enceinte);
         }
      }
   }

   @Override
   public void removeObjectManager(final Enceinte enceinte, final String comments, final Utilisateur user){
      if(enceinte != null){
         if(isUsedObjectManager(enceinte)){
            log.warn("Objet utilisé lors de la suppression de l'objet " + "Enceinte : " + enceinte.toString());
            throw new ObjectUsedException("enceinte.deletion.isUsed", false);
         }
         // suppression des enfants
         final List<Terminale> terms = terminaleDao.findByEnceinteWithOrder(enceinte);
         if(terms.size() > 0){
            for(int i = 0; i < terms.size(); i++){
               terminaleManager.removeObjectManager(terms.get(i), comments, user);
            }
         }else{
            final List<Enceinte> encs = findByEnceintePereWithOrderManager(enceinte);
            for(int i = 0; i < encs.size(); i++){
               removeObjectManager(encs.get(i), comments, user);
            }
         }

         //Supprime operations associes
         CreateOrUpdateUtilities.removeAssociateOperations(enceinte, operationManager, comments, user);

         enceinteDao.removeObject(enceinte.getEnceinteId());
         log.info("Suppression de l'objet Enceinte : " + enceinte.toString());
      }else{
         log.warn("Suppression d'une Enceinte null");
      }
   }

   /**
    * Cette méthode met à jour les associations entre une enceinte et
    * une liste de banques.
    * @param enceinte Enceinte pour laquelle on veut mettre à jour
    * les associations.
    * @param banques Liste des banques que l'on veut associer à 
    * l'enceinte.
    */
   public void updateBanques(final Enceinte enceinte, final List<Banque> banques){

      final Enceinte enc = enceinteDao.mergeObject(enceinte);

      final Iterator<Banque> it = enc.getBanques().iterator();
      final List<Banque> banquesToRemove = new ArrayList<>();
      // on parcourt les banques qui sont actuellement associés
      // a l'enceinte
      while(it.hasNext()){
         final Banque tmp = it.next();
         // si une banque n'est pas dans la nouvelle liste, on
         // la conserve afin de la retirer par la suite
         if(!banques.contains(tmp)){
            banquesToRemove.add(tmp);
         }
      }

      // on parcourt la liste des banques à retirer de
      // l'association
      for(int i = 0; i < banquesToRemove.size(); i++){
         final Banque bank = banqueDao.mergeObject(banquesToRemove.get(i));
         // on retire la banque de chaque coté de l'association
         enc.getBanques().remove(bank);

         log.debug("Suppression de l'association entre l'enceinte : " + enc.toString() + " et la banque : " + bank.toString());
      }

      // on parcourt la nouvelle liste de banques
      for(int i = 0; i < banques.size(); i++){
         // si une banque n'était pas associé au contrat
         if(!enc.getBanques().contains(banques.get(i))){
            // on ajoute la banque des deux cotés de l'association
            enc.getBanques().add(banques.get(i));

            log.debug(
               "Ajout de l'association entre l'enceinte : " + enc.toString() + " et la banque : " + banques.get(i).toString());
         }
      }
   }

   @Override
   public void echangerDeuxEnceintesManager(final Enceinte enceinte1, final Enceinte enceinte2, final Utilisateur utilisateur){
      if(enceinte1 != null && enceinte1.getEnceinteId() != null && enceinte2 != null && enceinte2.getEnceinteId() != null){

         // Test s'il y a des doublons
         if(findDoublonWithoutTwoEnceintesManager(enceinte1, enceinte2)){
            log.warn("Doublon lors du déplacement de l'objet Enceinte : " + enceinte1.toString());
            throw new DoublonFoundException("Enceinte", "deplacement");
         }else if(findDoublonWithoutTwoEnceintesManager(enceinte2, enceinte1)){
            log.warn("Doublon lors du déplacement de l'objet Enceinte : " + enceinte2.toString());
            throw new DoublonFoundException("Enceinte", "deplacement");
         }else{

            // validation des enceintes
            BeanValidator.validateObject(enceinte1, new Validator[] {enceinteValidator});
            BeanValidator.validateObject(enceinte2, new Validator[] {enceinteValidator});

            enceinteDao.updateObject(enceinte1);
            enceinteDao.updateObject(enceinte2);

            log.info("Modification de l'objet Enceinte : " + enceinte1.toString());
            log.info("Modification de l'objet Enceinte : " + enceinte2.toString());

            //Enregistrement des operations associees
            final Operation op1 = new Operation();
            op1.setDate(Utils.getCurrentSystemCalendar());
            operationManager.createObjectManager(op1, utilisateur, operationTypeDao.findByNom("Deplacement").get(0), enceinte1);
            final Operation op2 = new Operation();
            op2.setDate(Utils.getCurrentSystemCalendar());
            operationManager.createObjectManager(op2, utilisateur, operationTypeDao.findByNom("Deplacement").get(0), enceinte2);
         }
      }
   }

   @Override
   public Long getNbEmplacementsLibresByPS(final Enceinte enceinte){
      Long result = (long) 0;
      Long total = (long) 0;
      Long totalPris = (long) 0;
      if(enceinte != null && enceinte.getEnceinteId() != null){
         java.sql.Connection con = null;
         PreparedStatement pstmt = null;
         PreparedStatement pstmt2 = null;
         PreparedStatement pstmt3 = null;
         try{
            // connection à la base
            con = txManager.getDataSource().getConnection();
            con.setAutoCommit(false);

            // requete qui récupère les ids de toutes les enceintes filles
            // d'une enceinte
            String sql = "select ENCEINTE_ID FROM ENCEINTE " + "WHERE ENCEINTE_PERE_ID = ?";
            pstmt = con.prepareStatement(sql);

            // requete qui récupère le nb de places de ttes les terminales
            // d'une enceinte
            sql = "SELECT nb_places from TERMINALE_TYPE tt " + "join TERMINALE t on t.terminale_type_id "
               + "= tt.terminale_type_id " + "where enceinte_id = ?";
            pstmt2 = con.prepareStatement(sql);

            // requete qui récupère le nb d'emplacements occupés de ttes 
            // les terminales d'une enceinte
            sql = "SELECT count(emplacement_id) from EMPLACEMENT e " + "join TERMINALE t on t.terminale_id = e.terminale_id "
               + "where t.enceinte_id = ? and e.vide=0";
            pstmt3 = con.prepareStatement(sql);

            // on récupère toutes les enceintes filles d'une enceinte
            final List<Integer> enceintesId = new ArrayList<>();
            enceintesId.add(enceinte.getEnceinteId());
            executeStatementForEnceintes(enceinte.getEnceinteId(), pstmt, enceintesId);

            // récupère le nb de places de ttes les terminales
            // d'une enceinte
            final List<Long> totaux = new ArrayList<>();
            for(int i = 0; i < enceintesId.size(); i++){
               executeStatementForTerminales(enceintesId.get(i), pstmt2, totaux);
            }
            // on calcul le nb total d'emplacements
            for(int i = 0; i < totaux.size(); i++){
               total = total + totaux.get(i);
            }

            // récupère le nb d'emplacements occupés de ttes 
            // les terminales d'une enceinte
            final List<Long> pris = new ArrayList<>();
            for(int i = 0; i < enceintesId.size(); i++){
               executeStatementForTerminales(enceintesId.get(i), pstmt3, pris);
            }
            // on calcul le nb total d'emplacements occupés
            for(int i = 0; i < pris.size(); i++){
               totalPris = totalPris + pris.get(i);
            }

            result = total - totalPris;
         }catch(final SQLException e){
            log.error(e);
         }finally{
            if(con != null){
               try{
                  con.close();
               }catch(final Exception e){
                  con = null;
               }
            }
            if(pstmt != null){
               try{
                  pstmt.close();
               }catch(final Exception e){
                  pstmt = null;
               }
            }
            if(pstmt2 != null){
               try{
                  pstmt2.close();
               }catch(final Exception e){
                  pstmt2 = null;
               }
            }
            if(pstmt3 != null){
               try{
                  pstmt3.close();
               }catch(final Exception e){
                  pstmt3 = null;
               }
            }
         }
      }
      return result;
   }

   @Override
   public Long getNbEmplacementsOccupesByPS(final Enceinte enceinte){
      Long totalPris = (long) 0;
      if(enceinte != null && enceinte.getEnceinteId() != null){
         java.sql.Connection con = null;
         PreparedStatement pstmt = null;
         PreparedStatement pstmt2 = null;
         PreparedStatement pstmt3 = null;
         try{
            // connection à la base
            con = txManager.getDataSource().getConnection();
            con.setAutoCommit(false);

            // requete qui récupère les ids de toutes les enceintes filles
            // d'une enceinte
            String sql = "select ENCEINTE_ID FROM ENCEINTE " + "WHERE ENCEINTE_PERE_ID = ?";
            pstmt = con.prepareStatement(sql);

            // requete qui récupère le nb d'emplacements occupés de ttes 
            // les terminales d'une enceinte
            sql = "SELECT count(emplacement_id) from EMPLACEMENT e " + "join TERMINALE t on t.terminale_id = e.terminale_id "
               + "where t.enceinte_id = ? and e.vide=0";
            pstmt3 = con.prepareStatement(sql);

            // on récupère toutes les enceintes filles d'une enceinte
            final List<Integer> enceintesId = new ArrayList<>();
            enceintesId.add(enceinte.getEnceinteId());
            executeStatementForEnceintes(enceinte.getEnceinteId(), pstmt, enceintesId);

            // récupère le nb d'emplacements occupés de ttes 
            // les terminales d'une enceinte
            final List<Long> pris = new ArrayList<>();
            for(int i = 0; i < enceintesId.size(); i++){
               executeStatementForTerminales(enceintesId.get(i), pstmt3, pris);
            }
            // on calcul le nb total d'emplacements occupés
            for(int i = 0; i < pris.size(); i++){
               totalPris = totalPris + pris.get(i);
            }
         }catch(final SQLException e){
            log.error(e);
         }finally{
            if(con != null){
               try{
                  con.close();
               }catch(final Exception e){
                  con = null;
               }
            }
            if(pstmt != null){
               try{
                  pstmt.close();
               }catch(final Exception e){
                  pstmt = null;
               }
            }
            if(pstmt2 != null){
               try{
                  pstmt2.close();
               }catch(final Exception e){
                  pstmt2 = null;
               }
            }
            if(pstmt3 != null){
               try{
                  pstmt3.close();
               }catch(final Exception e){
                  pstmt3 = null;
               }
            }
         }
      }
      return totalPris;
   }

   @Override
   public List<Integer> getObjetIdsByEntiteByPS(final Enceinte enceinte, final Entite entite){
      final List<Integer> ids = new ArrayList<>();
      if(enceinte != null && enceinte.getEnceinteId() != null && entite != null && entite.getEntiteId() != null){
         java.sql.Connection con = null;
         PreparedStatement pstmt = null;
         PreparedStatement pstmt2 = null;
         PreparedStatement pstmt3 = null;
         try{
            // connection à la base
            con = txManager.getDataSource().getConnection();
            con.setAutoCommit(false);

            // requete qui récupère les ids de toutes les enceintes filles
            // d'une enceinte
            String sql = "select ENCEINTE_ID FROM ENCEINTE " + "WHERE ENCEINTE_PERE_ID = ?";
            pstmt = con.prepareStatement(sql);

            // requete qui récupère les ids
            sql = "SELECT objet_id from EMPLACEMENT e " + "join TERMINALE t on t.terminale_id = e.terminale_id "
               + "where t.enceinte_id = ? and e.vide=0 " + "and e.entite_id = ? and e.objet_id is not null";
            pstmt3 = con.prepareStatement(sql);

            // on récupère toutes les enceintes filles d'une enceinte
            final List<Integer> enceintesId = new ArrayList<>();
            enceintesId.add(enceinte.getEnceinteId());
            executeStatementForEnceintes(enceinte.getEnceinteId(), pstmt, enceintesId);

            // récupère le nb d'emplacements occupés de ttes 
            // les terminales d'une enceinte
            for(int i = 0; i < enceintesId.size(); i++){
               executeStatementForIds(enceintesId.get(i), entite.getEntiteId(), pstmt3, ids);
            }
         }catch(final SQLException e){
            log.error(e);
         }finally{
            if(con != null){
               try{
                  con.close();
               }catch(final Exception e){
                  con = null;
               }
            }
            if(pstmt != null){
               try{
                  pstmt.close();
               }catch(final Exception e){
                  pstmt = null;
               }
            }
            if(pstmt2 != null){
               try{
                  pstmt2.close();
               }catch(final Exception e){
                  pstmt2 = null;
               }
            }
            if(pstmt3 != null){
               try{
                  pstmt3.close();
               }catch(final Exception e){
                  pstmt3 = null;
               }
            }
         }
      }
      return ids;
   }

   /**
    * Exécute un preparde statement récupérant les enceintes filles 
    * d'une enceinte.
    * @param id Identifiant de l'enceinte mère.
    * @param ps PreparedStatement.
    * @param list Liste des enceintes filles.
    */
   public void executeStatementForEnceintes(final Integer id, final PreparedStatement ps, final List<Integer> list){
      final List<Integer> tmp = new ArrayList<>();
      ResultSet results = null;
      try{
         ps.setInt(1, id);
         results = ps.executeQuery();

         while(results.next()){
            list.add(results.getInt(1));
            tmp.add(results.getInt(1));

         }
      }catch(final SQLException e){
         log.error(e);
      }finally{
         if(results != null){
            try{
               results.close();
            }catch(final Exception e){
               results = null;
            }
         }
      }

      for(int i = 0; i < tmp.size(); i++){
         executeStatementForEnceintes(tmp.get(i), ps, list);
      }
   }

   /**
    * Exécute un preparde statement récupérant le nombre total d'emplacement ou
    * le nb d'emplacements occupés d'une enceinte.
    * @param id Identifiant de l'enceinte mère.
    * @param ps PreparedStatement.
    * @param list Liste des nombres.
    */
   public void executeStatementForTerminales(final Integer id, final PreparedStatement ps, final List<Long> list){
      ResultSet results = null;
      try{
         ps.setInt(1, id);
         results = ps.executeQuery();

         while(results.next()){
            list.add(results.getLong(1));
         }
      }catch(final SQLException e){
         log.error(e);
      }finally{
         if(results != null){
            try{
               results.close();
            }catch(final Exception e){
               results = null;
            }
         }
      }
   }

   /**
    * Exécute un preparde statement récupérant les ids.
    * @param idEnceinte Identifiant de l'enceinte mère.
    * @param idEntite Identifiant de l'entité.
    * @param ps PreparedStatement.
    * @param list Liste des nombres.
    */
   public void executeStatementForIds(final Integer idEnceinte, final Integer idEntite, final PreparedStatement ps,
      final List<Integer> list){
      ResultSet results = null;
      try{
         ps.setInt(1, idEnceinte);
         ps.setInt(2, idEntite);
         results = ps.executeQuery();

         while(results.next()){
            list.add(results.getInt(1));
         }
      }catch(final SQLException e){
         log.error(e);
      }finally{
         if(results != null){
            try{
               results.close();
            }catch(final Exception e){
               results = null;
            }
         }
      }
   }

   @Override
   public void createAllArborescenceManager(final Enceinte enceinte, final List<Enceinte> enceintes, final Terminale terminale,
      final List<Integer> firstPositions, final List<Banque> banques, final Utilisateur utilisateur) throws Exception{
      findAllObjectsManager();
      if(enceinte != null && enceintes != null && terminale != null && firstPositions != null
         && firstPositions.size() == enceintes.size() + 1){
         // création de la premiere enceinte
         createObjectManager(enceinte, enceinte.getEnceinteType(), enceinte.getConteneur(), enceinte.getEnceintePere(),
            enceinte.getEntite(), banques, enceinte.getCouleur(), utilisateur);
         // création des enceintes
         List<Enceinte> enceintesCrees = new ArrayList<>();
         enceintesCrees.add(enceinte);
         for(int i = 0; i < enceintes.size(); i++){
            final Enceinte current = enceintes.get(i);
            final Integer currentPosition = firstPositions.get(i);

            // creation des enceintes
            final List<Enceinte> enceintesCreesTmp = new ArrayList<>();
            for(int j = 0; j < enceintesCrees.size(); j++){
               try{
                  enceintesCreesTmp.addAll(createMultiObjetcsForEnceinteManager(enceintesCrees.get(j), current,
                     enceintesCrees.get(j).getNbPlaces(), currentPosition, utilisateur));
               }catch(final Exception e){
                  // en cas d'exception, on met l'id du
                  // conteneur à null
                  enceinte.setEnceinteId(null);
                  throw e;
               }
            }
            enceintesCrees = enceintesCreesTmp;

         }

         final Integer currentPosition = firstPositions.get(firstPositions.size() - 1);
         // création des terminales
         for(int i = 0; i < enceintesCrees.size(); i++){
            try{
               terminaleManager.createMultiObjetcsManager(enceintesCrees.get(i), terminale, enceintesCrees.get(i).getNbPlaces(),
                  currentPosition, utilisateur);
            }catch(final Exception e){
               // en cas d'exception, on met l'id du
               // conteneur à null
               enceinte.setEnceinteId(null);
               throw e;
            }
         }
      }
   }

   @Override
   public void updatewithCreateAllArborescenceManager(final Enceinte enceinte, final List<Enceinte> enceintes,
      final Terminale terminale, final List<Integer> firstPositions, final List<Banque> banques, final Utilisateur utilisateur)
      throws Exception{
      findAllObjectsManager();
      if(enceinte != null && enceintes != null && terminale != null && firstPositions != null
         && firstPositions.size() == enceintes.size() + 1){
         // création de la premiere enceinte
         updateObjectManager(enceinte, enceinte.getEnceinteType(), enceinte.getConteneur(), enceinte.getEnceintePere(),
            enceinte.getEntite(), banques, enceinte.getCouleur(), null, utilisateur, null);
         // création des enceintes
         List<Enceinte> enceintesCrees = new ArrayList<>();
         enceintesCrees.add(enceinte);
         for(int i = 0; i < enceintes.size(); i++){
            final Enceinte current = enceintes.get(i);
            final Integer currentPosition = firstPositions.get(i);

            // creation des enceintes
            final List<Enceinte> enceintesCreesTmp = new ArrayList<>();
            for(int j = 0; j < enceintesCrees.size(); j++){
               try{
                  enceintesCreesTmp.addAll(createMultiObjetcsForEnceinteManager(enceintesCrees.get(j), current,
                     enceintesCrees.get(j).getNbPlaces(), currentPosition, utilisateur));
               }catch(final Exception e){
                  throw e;
               }
            }
            enceintesCrees = enceintesCreesTmp;

         }

         final Integer currentPosition = firstPositions.get(firstPositions.size() - 1);
         // création des terminales
         for(int i = 0; i < enceintesCrees.size(); i++){
            try{
               terminaleManager.createMultiObjetcsManager(enceintesCrees.get(i), terminale, enceintesCrees.get(i).getNbPlaces(),
                  currentPosition, utilisateur);
            }catch(final Exception e){
               throw e;
            }
         }
      }
   }

   @Override
   public Conteneur getConteneurParent(final Enceinte enceinte){
      return getConteneurParentRecursive(enceinte);
   }

   /**
    * Implémente la recursivité permettant de 
    * trouver le conteneur contenant l'enceinte.
    * @param enceinte
    * @return Conteneur parent.
    */
   private Conteneur getConteneurParentRecursive(final Enceinte enceinte){
      if(enceinte.getConteneur() != null){
         return enceinte.getConteneur();
      }else if(enceinte.getEnceintePere() != null){
         return getConteneurParentRecursive(enceinte.getEnceintePere());
      }
      return null;
   }

   @Override
   public List<Enceinte> findAllEnceinteByConteneurManager(final Conteneur c){
      final List<Enceinte> encs = new ArrayList<>();
      encs.addAll(findByConteneurWithOrderManager(c));
      final List<Enceinte> coll = new ArrayList<>();
      coll.addAll(encs);
      findEnceinteRecursiveManager(encs, coll);
      return coll;
   }

   @Override
   public void findEnceinteRecursiveManager(final List<Enceinte> encs, final List<Enceinte> coll){
      for(int i = 0; i < encs.size(); i++){
         coll.addAll(findByEnceintePereWithOrderManager(encs.get(i)));

         findEnceinteRecursiveManager(findByEnceintePereWithOrderManager(encs.get(i)), coll);
      }
   }

   @Override
   public List<Terminale> getAllTerminalesInArborescenceManager(final Enceinte enceinte){
      final List<Terminale> terminales = new ArrayList<>();
      if(enceinte != null && enceinte.getEnceinteId() != null){

         if(checkLastEnceinte(enceinte)){
            terminales.addAll(terminaleDao.findByEnceinteWithOrder(enceinte));
         }else{
            final List<Enceinte> encs = findByEnceintePereWithOrderManager(enceinte);
            for(int i = 0; i < encs.size(); i++){
               terminales.addAll(getAllTerminalesInArborescenceManager(encs.get(i)));
            }
         }
      }
      return terminales;
   }

   @Override
   public Enceinte updateTailleEnceinteManager(Enceinte enceinte, final Integer nbPlaces, final Utilisateur utilisateur){

      if(enceinte != null && nbPlaces != null && utilisateur != null && nbPlaces != 0){

         final int occup = getTerminalesManager(enceinte).size() + getEnceintesManager(enceinte).size();

         if(occup > enceinte.getNbPlaces() + nbPlaces){
            throw new EnceinteSizeException(enceinte, occup);
         }
         final Integer newNbPlaces = enceinte.getNbPlaces() + nbPlaces;
         enceinte.setNbPlaces(newNbPlaces);
         enceinte = enceinteDao.mergeObject(enceinte);

         // reordonne les enceintes contenues si la dernière position de 
         // dépasse la taille totale de l'enceinte
         int i = 1;
         final List<Enceinte> contenues = new ArrayList<>(getEnceintesManager(enceinte));
         if(!contenues.isEmpty() && contenues.get(contenues.size() - 1).getPosition() > newNbPlaces){
            i = 1;
            for(final Enceinte enc : contenues){
               enc.setPosition(i);
               enceinteDao.mergeObject(enceinte);
               i++;
            }
         }

         // reordonne les terminales contenues si la dernière position de 
         // dépasse la taille totale de l'enceinte
         final List<Terminale> terminales = new ArrayList<>(getTerminalesManager(enceinte));
         if(!terminales.isEmpty() && terminales.get(terminales.size() - 1).getPosition() > newNbPlaces){
            i = 1;
            for(final Terminale term : terminales){
               term.setPosition(i);
               terminaleDao.mergeObject(term);
               i++;
            }
         }

         final Operation updateOp = new Operation();
         updateOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(updateOp, utilisateur, operationTypeDao.findByNom("Modification").get(0), enceinte);
      }

      return enceinte;
   }
   
   @Override
   public List<Banque> getDistinctBanquesFromTkObjectsManager(Enceinte enc) {
	   List<Banque> banks = new ArrayList<Banque>();
	   
	   if (enc != null) {
		   List<Terminale> terms = getAllTerminalesInArborescenceManager(enc);
		   banks.addAll(terms.stream().map(t -> terminaleManager.getDistinctBanquesFromTkObjectsManager(t))
				   .flatMap(List::stream)
				   .distinct().collect(Collectors.toList()));
	   }
	   
	   return banks;
   }
}
