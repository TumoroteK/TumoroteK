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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.stockage.EmplacementDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleNumerotationDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleTypeDao;
import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.InvalidPositionException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.UsedPositionException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.impl.coeur.cession.OldEmplTrace;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.stockage.CheckPositionManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.stockage.IncidentManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.stockage.IncidentValidator;
import fr.aphp.tumorotek.manager.validation.stockage.TerminaleValidator;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.stockage.TerminaleNumerotation;
import fr.aphp.tumorotek.model.stockage.TerminaleType;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Terminale.
 * Interface créée le 19/03/10.
 *
 * @author Pierre Ventadour
 * @version 2.2-2-diamic
 *
 */
public class TerminaleManagerImpl implements TerminaleManager
{

   private final Logger log = LoggerFactory.getLogger(TerminaleManager.class);

   private TerminaleDao terminaleDao;

   private EnceinteDao enceinteDao;

   private TerminaleTypeDao terminaleTypeDao;

   private BanqueDao banqueDao;

   private EntiteDao entiteDao;

   private TerminaleNumerotationDao terminaleNumerotationDao;

   private EmplacementDao emplacementDao;

   private EchantillonDao echantillonDao;

   private ProdDeriveDao prodDeriveDao;

   private CheckPositionManager checkPositionManager;

   private TerminaleValidator terminaleValidator;

   private OperationManager operationManager;

   private OperationTypeDao operationTypeDao;

   private CouleurDao couleurDao;

   private IncidentManager incidentManager;

   private IncidentValidator incidentValidator;

   private EmplacementManager emplacementManager;

   // @since 2.1
   private DataSource dataSource;

   public void setTerminaleDao(final TerminaleDao tDao){
      this.terminaleDao = tDao;
   }

   public void setEnceinteDao(final EnceinteDao eDao){
      this.enceinteDao = eDao;
   }

   public void setTerminaleTypeDao(final TerminaleTypeDao tDao){
      this.terminaleTypeDao = tDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setTerminaleNumerotationDao(final TerminaleNumerotationDao tDao){
      this.terminaleNumerotationDao = tDao;
   }

   public void setEmplacementDao(final EmplacementDao eDao){
      this.emplacementDao = eDao;
   }

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setProdDeriveDao(final ProdDeriveDao pDao){
      this.prodDeriveDao = pDao;
   }

   public void setTerminaleValidator(final TerminaleValidator tValidator){
      this.terminaleValidator = tValidator;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setCouleurDao(final CouleurDao cDao){
      this.couleurDao = cDao;
   }

   public void setCheckPositionManager(final CheckPositionManager cManager){
      this.checkPositionManager = cManager;
   }

   public void setIncidentManager(final IncidentManager iM){
      this.incidentManager = iM;
   }

   public void setIncidentValidator(final IncidentValidator iV){
      this.incidentValidator = iV;
   }

   public void setDataSource(final DataSource dataSource){
      this.dataSource = dataSource;
   }

   public void setEmplacementManager(final EmplacementManager _e){
      this.emplacementManager = _e;
   }

   @Override
   public Terminale findByIdManager(final Integer terminaleId){
      return terminaleDao.findById(terminaleId);
   }

   @Override
   public List<Terminale> findAllObjectsManager(){
      return terminaleDao.findAll();
   }

   @Override
   public List<Terminale> findByEnceinteWithOrderManager(final Enceinte enceinte){
      log.debug("Recherche de toutes les terminales d'une enceinte");
      if(enceinte != null){
         return terminaleDao.findByEnceinteWithOrder(enceinte);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Terminale> findByEnceinteAndNomManager(final Enceinte enceinte, final String nom){
      if(enceinte != null && nom != null){
         return terminaleDao.findByEnceinteAndNom(enceinte, nom);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public Set<Emplacement> getEmplacementsManager(Terminale terminale){
      if(terminale != null && terminale.getTerminaleId() != null){
         terminale = terminaleDao.mergeObject(terminale);
         final Set<Emplacement> empls = terminale.getEmplacements();
         empls.size();

         return empls;
      }else{
         return new HashSet<>();
      }
   }

   @Override
   public Boolean checkTerminaleInEnceinteLimitesManager(final Terminale terminale){
      boolean valide = true;
      // on vérifie que la terminale n'est pas nulle
      if(terminale != null){
         final Enceinte enceinte = terminale.getEnceinte();

         if(enceinte != null && terminale.getPosition() != null){
            // sa position doit être contenue dans les limites de
            // l'enceinte
            final Integer nbPlaces = enceinte.getNbPlaces();
            if(terminale.getPosition() > nbPlaces){
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

   /**
    * Compte le nombre d'emplacements libres dans la terminales.
    * @param terminale Terminale.
    * @return Le nombre d'emplacements libre.
    */
   @Override
   public Long getNumberEmplacementsLibresManager(final Terminale terminale){

      if(terminale != null && terminale.getTerminaleId() != null){
         final List<Long> nbPris = emplacementDao.findByCountTerminaleAndVide(terminale, false);

         final long libres = terminale.getTerminaleType().getNbPlaces() - nbPris.get(0);

         return libres;

      }else{
         return (long) 0;
      }

   }

   @Override
   public Long getNumberEmplacementsOccupesManager(final Terminale terminale){

      if(terminale != null && terminale.getTerminaleId() != null){
         final List<Long> nbPris = emplacementDao.findByCountTerminaleAndVide(terminale, false);

         return nbPris.get(0);

      }else{
         return (long) 0;
      }

   }

   @Override
   public List<String> getCodesLignesManager(final Terminale terminale){
      List<String> results = new ArrayList<>();

      // on vérifie que les params sont valides
      if(terminale != null && terminale.getTerminaleId() != null){
         Integer nbLignes = 0;
         // si un schéma est défini
         if(terminale.getTerminaleType().getScheme() != null){
            // on récupère le nb de lignes
            nbLignes = terminale.getTerminaleType().getScheme().split(";").length;
         }else{
            nbLignes = terminale.getTerminaleType().getHauteur();
         }

         if(nbLignes > 0){
            // en fct de la numérotation, on génère la liste des
            // résultats
            if(terminale.getTerminaleNumerotation().getLigne().equals("CAR")){
               results = Utils.createListChars(nbLignes, null, new ArrayList<String>());
            }else{
               for(int i = 0; i < nbLignes; i++){
                  results.add(String.valueOf(i + 1));
               }
            }
         }
      }

      return results;
   }

   @Override
   public List<String> getCodesColonnesManager(final Terminale terminale, final Integer numLigne){
      List<String> results = new ArrayList<>();

      // on vérifie que les params sont valides
      if(terminale != null && terminale.getTerminaleId() != null && numLigne != null && numLigne > 0){

         Integer nbColonnes = 0;
         // si un schéma est défini
         if(terminale.getTerminaleType().getScheme() != null){
            // on récupère les lignes
            final String[] lignes = terminale.getTerminaleType().getScheme().split(";");
            // si le num de ligne est valide
            if(numLigne <= lignes.length){
               // on récupère le nb de colonnes
               nbColonnes = Integer.parseInt(lignes[numLigne - 1]);
            }

         }else{
            // si le num de ligne est valide
            if(numLigne <= terminale.getTerminaleType().getHauteur()){
               // on récupère le nb de colonnes
               nbColonnes = terminale.getTerminaleType().getLongueur();
            }
         }

         // en fct de la numérotation, on génère la liste des
         // résultats
         if(terminale.getTerminaleNumerotation().getColonne().equals("CAR")){
            results = Utils.createListChars(nbColonnes, null, new ArrayList<String>());
         }else{
            for(int i = 0; i < nbColonnes; i++){
               results.add(String.valueOf(i + 1));
            }
         }
      }

      return results;
   }

   @Override
   public List<Echantillon> getEchantillonsManager(final Terminale terminale){
      if(terminale != null && terminale.getTerminaleId() != null){
         return echantillonDao.findByTerminaleDirect(terminale);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<ProdDerive> getProdDerivesManager(final Terminale terminale){
      if(terminale != null && terminale.getTerminaleId() != null){
         return prodDeriveDao.findByTerminaleDirect(terminale);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public Boolean findDoublonManager(final Terminale terminale){
      if(terminale != null){
         final Enceinte enceinte = terminale.getEnceinte();
         if(terminale.getTerminaleId() == null){
            return terminaleDao.findByEnceinteWithOrder(enceinte).contains(terminale);
         }else{
            return terminaleDao.findByExcludedIdEnceinte(terminale.getTerminaleId(), enceinte).contains(terminale);
         }
      }else{
         return false;
      }
   }

   @Override
   public Boolean findDoublonWithoutTwoTerminalesManager(final Terminale terminale, final Terminale terminaleDestination){

      if(terminale != null && terminaleDestination != null && terminale.getTerminaleId() != null
         && terminaleDestination.getTerminaleId() != null){

         if(terminaleDestination.getEnceinte() != null){
            return terminaleDao.findByTwoExcludedIdsWithEnceinte(terminale.getTerminaleId(),
               terminaleDestination.getTerminaleId(), terminale.getEnceinte()).contains(terminale);
         }else{
            return false;
         }

      }else{
         return false;
      }

   }

   @Override
   public Boolean isUsedObjectManager(final Terminale terminale){
      if(terminale != null && terminale.getTerminaleId() != null){
         final List<Emplacement> empls = emplacementDao.findByTerminaleAndVide(terminale, false);

         return (empls.size() > 0);
      }else{
         return false;
      }
   }

   @Override
   public List<Object> getListOfParentsManager(final Terminale terminale){
      final List<Object> results = new ArrayList<>();

      if(terminale != null){
         Conteneur cont = null;
         Enceinte enc = terminale.getEnceinte();
         results.add(enc);
         while(enc.getEnceintePere() != null){
            enc = enc.getEnceintePere();
            results.add(0, enc);
         }

         if(enc.getConteneur() != null){
            cont = enc.getConteneur();
            results.add(0, cont);
         }
      }

      return results;
   }

   @Override
   public void createObjectManager(final Terminale terminale, final Enceinte enceinte, final TerminaleType terminaleType,
      final Banque banque, final Entite entite, final TerminaleNumerotation terminaleNumerotation, final Couleur couleur,
      final Utilisateur utilisateur){

      // Enceinte required
      if(enceinte != null){
         terminale.setEnceinte(enceinteDao.mergeObject(enceinte));
      }else{
         log.warn("Objet obligatoire Enceinte manquant  lors de la création d'une Terminale");
         throw new RequiredObjectIsNullException("Terminale", "creation", "Enceinte");
      }

      // TerminaleType required
      if(terminaleType != null){
         terminale.setTerminaleType(terminaleTypeDao.mergeObject(terminaleType));
      }else{
         log.warn("Objet obligatoire TerminaleType manquant  lors de la création d'une Terminale");
         throw new RequiredObjectIsNullException("Terminale", "creation", "TerminaleType");
      }

      // TerminaleNumerotation required
      if(terminaleNumerotation != null){
         terminale.setTerminaleNumerotation(terminaleNumerotationDao.mergeObject(terminaleNumerotation));
      }else{
         log.warn("Objet obligatoire TerminaleNumerotation manquant  lors de la création d'une Terminale");
         throw new RequiredObjectIsNullException("Terminale", "creation", "TerminaleNumerotation");
      }

      terminale.setBanque(banqueDao.mergeObject(banque));
      terminale.setEntite(entiteDao.mergeObject(entite));
      terminale.setCouleur(couleurDao.mergeObject(couleur));

      // Test de la position
      if(!checkTerminaleInEnceinteLimitesManager(terminale)){
         log.warn("La position n'est pas dans la limite des places de l'enceinte parent");
         throw new InvalidPositionException("Terminale", "creation", terminale.getPosition());
      }

      // Test de la position
      if(!checkPositionManager.checkPositionLibreInEnceinteManager(terminale.getEnceinte(), terminale.getPosition(), null, null)){
         log.warn("La position est déjà occupée par un autre objet dans l'enceinte parent");
         throw new UsedPositionException("Terminale", "creation", terminale.getPosition());
      }

      // Test s'il y a des doublons
      if(findDoublonManager(terminale)){
         log.warn("Doublon lors de la creation de l'objet Terminale : {}",  terminale);
         throw new DoublonFoundException("Terminale", "creation");
      }else{

         // validation du Contrat
         BeanValidator.validateObject(terminale, new Validator[] {terminaleValidator});

         terminale.setArchive(false);
         terminaleDao.createObject(terminale);

         log.info("Enregistrement de l'objet Terminale : {}",  terminale);

         //Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Creation").get(0), terminale);
      }
   }

   @Override
   public void updateObjectManager(final Terminale terminale, final Enceinte enceinte, final TerminaleType terminaleType,
      final Banque banque, final Entite entite, final TerminaleNumerotation terminaleNumerotation, final Couleur couleur,
      final List<Incident> incidents, final Utilisateur utilisateur, final List<OperationType> operations){

      // Enceinte required
      if(enceinte != null){
         terminale.setEnceinte(enceinteDao.mergeObject(enceinte));
      }else{
         log.warn("Objet obligatoire Enceinte manquant  lors de la modification d'une Terminale");
         throw new RequiredObjectIsNullException("Terminale", "modification", "Enceinte");
      }

      // TerminaleType required
      if(terminaleType != null){
         terminale.setTerminaleType(terminaleTypeDao.mergeObject(terminaleType));
      }else{
         log.warn("Objet obligatoire TerminaleType manquant  lors de la modification d'une Terminale");
         throw new RequiredObjectIsNullException("Terminale", "modification", "TerminaleType");
      }

      // TerminaleNumerotation required
      if(terminaleNumerotation != null){
         terminale.setTerminaleNumerotation(terminaleNumerotationDao.mergeObject(terminaleNumerotation));
      }else{
         log.warn("Objet obligatoire TerminaleNumerotation manquant  lors de la modification d'une Terminale");
         throw new RequiredObjectIsNullException("Terminale", "modification", "TerminaleNumerotation");
      }

      terminale.setBanque(banqueDao.mergeObject(banque));
      terminale.setEntite(entiteDao.mergeObject(entite));
      terminale.setCouleur(couleurDao.mergeObject(couleur));

      // Test de la position
      if(!checkTerminaleInEnceinteLimitesManager(terminale)){
         log.warn("La position n'est pas dans la limite des places de l'enceinte parent");
         throw new InvalidPositionException("Terminale", "modification", terminale.getPosition());
      }

      // Test de la position
      if(!checkPositionManager.checkPositionLibreInEnceinteManager(terminale.getEnceinte(), terminale.getPosition(),
         terminale.getTerminaleId(), null)){
         log.warn("La position est déjà occupée par un autre objet dans l'enceinte parent");
         throw new UsedPositionException("Terminale", "modification", terminale.getPosition());
      }

      if(incidents != null){
         // maj des incidents
         // on vérifie qu'il n'y a pas de doublons dans la liste
         if(incidentManager.findDoublonInListManager(incidents)){
            log.warn("Doublon dans la liste des incidents");
            throw new DoublonFoundException("Incident", "modification");
         }else{
            // pour chaque coordonnée
            for(int i = 0; i < incidents.size(); i++){
               final Incident incident = incidents.get(i);
               // validation de la coordonnée
               BeanValidator.validateObject(incident, new Validator[] {incidentValidator});

               // si nouvel incident => creation
               // sinon => update
               if(incident.getIncidentId() == null){
                  incidentManager.createObjectManager(incident, null, null, terminale);
               }else{
                  incidentManager.updateObjectManager(incident, null, null, terminale);
               }
            }
         }
      }

      // Test s'il y a des doublons
      if(findDoublonManager(terminale)){
         log.warn("Doublon lors de la modification de l'objet Terminale : {}",  terminale);
         throw new DoublonFoundException("Terminale", "modification");
      }else{

         // validation de la terminale
         BeanValidator.validateObject(terminale, new Validator[] {terminaleValidator});

         terminaleDao.updateObject(terminale);

         log.info("Modification de l'objet Terminale : {}",  terminale);

         //Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Modification").get(0),
            terminale);

         if(operations != null){
            for(int i = 0; i < operations.size(); i++){
               //Enregistrement de l'operation associee
               final Operation dateOp = new Operation();
               dateOp.setDate(Utils.getCurrentSystemCalendar());
               operationManager.createObjectManager(dateOp, utilisateur, operations.get(i), terminale);
            }
         }
      }
   }

   @Override
   public void removeObjectManager(final Terminale terminale, final String comments, final Utilisateur user){
      if(terminale != null){
         if(isUsedObjectManager(terminale)){
            log.warn("Objet utilisé lors de la suppression de l'objet Terminale : {}",  terminale);
            throw new ObjectUsedException("Terminale", "suppression");
         }else{

            //Supprime operations associes
            CreateOrUpdateUtilities.removeAssociateOperations(terminale, operationManager, comments, user);

            terminaleDao.removeObject(terminale.getTerminaleId());
            log.info("Suppression de l'objet Terminale : {}",  terminale);
         }
      }else{
         log.warn("Suppression d'une Terminale null");
      }
   }

   @Override
   public List<Terminale> createMultiObjetcsManager(final Enceinte enceinte, final Terminale terminale, final Integer number,
      Integer firstPosition, final Utilisateur utilisateur){

      final List<Terminale> terminales = new ArrayList<>();

      if(terminale != null){
         //Enceinte required
         if(enceinte != null){
            if(number != null){
               if(number > enceinte.getNbPlaces()){
                  log.warn("La position n'est pas dans la limite des places de l'enceinte");
                  throw new InvalidPositionException("Terminale", "creation", number);
               }

               for(int i = 0; i < number; i++){
                  final Terminale term = new Terminale();
                  final StringBuffer nom = new StringBuffer(terminale.getNom());
                  if(firstPosition != null){
                     nom.append(firstPosition);
                     ++firstPosition;
                  }else{
                     nom.append(i + 1);
                  }
                  term.setNom(nom.toString());
                  term.setPosition(i + 1);
                  if(terminale.getAlias() != null){
                     final StringBuffer alias = new StringBuffer(terminale.getAlias());
                     alias.append(i + 1);
                     term.setAlias(alias.toString());
                  }
                  term.setArchive(false);

                  createObjectManager(term, enceinte, terminale.getTerminaleType(), terminale.getBanque(), terminale.getEntite(),
                     terminale.getTerminaleNumerotation(), null, utilisateur);

                  terminales.add(term);
               }
            }

         }else{
            log.warn("Objet obligatoire Enceinte manquant  lors de la creation d'une Terminale");
            throw new RequiredObjectIsNullException("Terminale", "creation", "Enceinte");
         }
      }

      return terminales;
   }

   @Override
   public void echangerDeuxTerminalesManager(final Terminale terminale1, final Terminale terminale2,
      final Utilisateur utilisateur){
      if(terminale1 != null && terminale1.getTerminaleId() != null && terminale2 != null && terminale2.getTerminaleId() != null){

         // Test s'il y a des doublons
         if(findDoublonWithoutTwoTerminalesManager(terminale1, terminale2)){
            log.warn("Doublon lors du déplacement de l'objet Terminale : {}",  terminale1);
            throw new DoublonFoundException("Terminale", "deplacement");
         }else if(findDoublonWithoutTwoTerminalesManager(terminale2, terminale1)){
            log.warn("Doublon lors du déplacement de l'objet Terminale : {}",  terminale2);
            throw new DoublonFoundException("Terminale", "deplacement");
         }else{

            // validation des enceintes
            BeanValidator.validateObject(terminale1, new Validator[] {terminaleValidator});
            BeanValidator.validateObject(terminale2, new Validator[] {terminaleValidator});

            terminaleDao.updateObject(terminale1);
            terminaleDao.updateObject(terminale2);

            log.info("Modification de l'objet Terminale : {}",  terminale1);
            log.info("Modification de l'objet Terminale : {}",  terminale2);

            //Enregistrement des operations associees
            final Operation op1 = new Operation();
            op1.setDate(Utils.getCurrentSystemCalendar());
            operationManager.createObjectManager(op1, utilisateur, operationTypeDao.findByNom("Deplacement").get(0), terminale1);
            final Operation op2 = new Operation();
            op2.setDate(Utils.getCurrentSystemCalendar());
            operationManager.createObjectManager(op2, utilisateur, operationTypeDao.findByNom("Deplacement").get(0), terminale2);
         }
      }
   }

   @Override
   public void updateNumerotationForMultiTerminales(final List<Terminale> terminales, final TerminaleNumerotation numrotation,
      final Utilisateur utilisateur){
      if(terminales != null && terminales.size() > 0 && numrotation != null && utilisateur != null){
         // update de chaque terminale
         for(int i = 0; i < terminales.size(); i++){
            updateObjectManager(terminales.get(i), terminales.get(i).getEnceinte(), terminales.get(i).getTerminaleType(),
               terminales.get(i).getBanque(), terminales.get(i).getEntite(), numrotation, null, null, utilisateur, null);
         }
      }
   }

   @Override
   public List<Terminale> createMultiVisotubesManager(final Enceinte enceinte, final Terminale terminale, final Integer number,
      Integer firstPosition, final boolean nameFromColor, final Integer size, final Utilisateur utilisateur){

      final List<Terminale> terminales = new ArrayList<>();

      if(terminale != null && size != null){
         //Enceinte required
         if(enceinte != null){
            if(number != null){
               if(number > enceinte.getNbPlaces()){
                  log.warn("La position n'est pas dans la limite des places de l'enceinte");
                  throw new InvalidPositionException("Terminale", "creation", number);
               }

               for(int i = 0; i < number; i++){
                  final Terminale term = new Terminale();

                  // gestion de la couleur
                  final List<Couleur> couleurs = couleurDao.findByVisotube();
                  Couleur couleur = null;
                  if(i < couleurs.size()){
                     couleur = couleurs.get(i);
                  }else{
                     couleur = couleurs.get(0);
                  }

                  // gestion du nom
                  final StringBuffer nom = new StringBuffer();
                  if(nameFromColor){
                     if(couleur != null){
                        nom.append(couleur.getCouleur());
                     }else{
                        nom.append(terminale.getNom());
                     }
                  }else{
                     nom.append(terminale.getNom());
                  }

                  if(firstPosition != null){
                     nom.append(firstPosition);
                     ++firstPosition;
                  }else{
                     nom.append(i + 1);
                  }
                  term.setNom(nom.toString());
                  term.setPosition(i + 1);
                  if(terminale.getAlias() != null){
                     final StringBuffer alias = new StringBuffer(terminale.getAlias());
                     alias.append(i + 1);
                     term.setAlias(alias.toString());
                  }
                  term.setArchive(false);

                  // gestion du type de terminale
                  TerminaleType type = null;
                  // la 1ère boite sera un visotube rond
                  if(i == 0){
                     final List<TerminaleType> tmp = terminaleTypeDao.findByType("VISOTUBE_" + size.toString() + "_ROND");
                     if(tmp.size() > 0){
                        type = tmp.get(0);
                     }
                  }else{
                     // sinon la boite sera un visotube triangle
                     final List<TerminaleType> tmp = terminaleTypeDao.findByType("VISOTUBE_" + size.toString() + "_TRI");
                     if(tmp.size() > 0){
                        type = tmp.get(0);
                     }
                  }

                  createObjectManager(term, enceinte, type, terminale.getBanque(), terminale.getEntite(),
                     terminale.getTerminaleNumerotation(), couleur, utilisateur);

                  terminales.add(term);
               }
            }

         }else{
            log.warn("Objet obligatoire Enceinte manquant  lors de la creation d'une Terminale");
            throw new RequiredObjectIsNullException("Terminale", "creation", "Enceinte");
         }
      }

      return terminales;

   }

   @Override
   public Map<TKStockableObject, Emplacement> getTkObjectsAndEmplacementsManager(final Terminale term){
      final Map<TKStockableObject, Emplacement> res = new HashMap<>();

      final List<Emplacement> emps = emplacementDao.findByTerminaleAndVide(term, false);
      final List<Echantillon> echans = getEchantillonsManager(term);
      final List<ProdDerive> derives = getProdDerivesManager(term);

      // check coherence
      if(emps.size() != (echans.size() + derives.size())){
         throw new RuntimeException("stockage.terminale.contenu.incoherent");
      }

      for(final Emplacement emp : emps){
         if(emp.getEntite().getNom().equals("Echantillon")){
            echanLoop: for(final Echantillon e : echans){
               if(e.getEchantillonId().equals(emp.getObjetId())){
                  res.put(e, emp);
                  echans.remove(e);
                  break echanLoop;
               }
            }
         }else{
            derivesLoop: for(final ProdDerive p : derives){
               if(p.getProdDeriveId().equals(emp.getObjetId())){
                  res.put(p, emp);
                  derives.remove(p);
                  break derivesLoop;
               }
            }
         }
      }

      return res;
   }

   @Override
   public List<OldEmplTrace> getTkObjectsEmplacementTracesManager(final Terminale term){
      final List<OldEmplTrace> traces = new ArrayList<>();

      final List<Emplacement> emps = emplacementDao.findByTerminaleAndVide(term, false);
      final List<Echantillon> echans = getEchantillonsManager(term);
      final List<ProdDerive> derives = getProdDerivesManager(term);

      // check coherence
      if(emps.size() != (echans.size() + derives.size())){
         throw new RuntimeException("stockage.terminale.contenu.incoherent");
      }

      for(final Emplacement emp : emps){
         if(emp.getEntite().getNom().equals("Echantillon")){
            echanLoop: for(final Echantillon e : echans){
               if(e.getEchantillonId().equals(emp.getObjetId())){
                  traces.add(new OldEmplTrace(e, emplacementManager.getAdrlManager(emp, false),
                     emplacementManager.getConteneurManager(emp), emp));
                  echans.remove(e);
                  break echanLoop;
               }
            }
         }else{
            derivesLoop: for(final ProdDerive p : derives){
               if(p.getProdDeriveId().equals(emp.getObjetId())){
                  traces.add(new OldEmplTrace(p, emplacementManager.getAdrlManager(emp, false),
                     emplacementManager.getConteneurManager(emp), emp));
                  derives.remove(p);
                  break derivesLoop;
               }
            }
         }
      }

      return traces;
   }

   @Override
   public List<Banque> getDistinctBanquesFromTkObjectsManager(final Terminale term){
      final List<Banque> banks = new ArrayList<>();

      if(term != null){
         final List<TKStockableObject> tkObjs = new ArrayList<>();
         tkObjs.addAll(getEchantillonsManager(term));
         tkObjs.addAll(getProdDerivesManager(term));

         banks.addAll(tkObjs.stream().map(o -> o.getBanque()).distinct().collect(Collectors.toList()));
      }
      return banks;
   }

   @Override
   public List<Terminale> findByAliasManager(final String _a){
      return terminaleDao.findByAlias(_a);
   }

   @Override
   public List<Integer> findTerminaleIdsFromNomManager(final String nom, final Enceinte enc, final List<Conteneur> conts){

      final List<Integer> ids = new ArrayList<>();

      if(nom != null && conts != null){
         Connection conn = null;
         CallableStatement stmt = null;
         ResultSet rset = null;
         try{
            conn = DataSourceUtils.getConnection(dataSource);

            // DBMS
            if(Utils.isOracleDBMS()){
               stmt = conn.prepareCall("call get_term_by_nom_and_parent(?,?,?,?)");
               stmt.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
            }else{
               stmt = conn.prepareCall("call get_term_by_nom_and_parent(?,?,?)");
            }

            for(final Conteneur cont : conts){
               stmt.clearParameters();
               stmt.setString(1, nom);
               if(enc != null){
                  stmt.setInt(2, enc.getEnceinteId());
               }else{
                  stmt.setNull(2, Types.INTEGER);
               }
               stmt.setInt(3, cont.getConteneurId());

               // DBMS
               if(Utils.isOracleDBMS()){
                  stmt.executeQuery();
                  rset = (ResultSet) stmt.getObject(4);
               }else{
                  if(stmt.execute()){
                     rset = stmt.getResultSet();
                  }
               }

               while(rset.next()){
                  ids.add(rset.getInt(1));
               }
            }
         }catch(final Exception e){
            log.error(e.getMessage());
         }finally{
            if(rset != null){
               try{
                  rset.close();
               }catch(final Exception ex){
                  rset = null;
               }
            }
            if(stmt != null){
               try{
                  stmt.close();
               }catch(final Exception ex){
                  stmt = null;
               }
            }
            if(conn != null){
               try{
                  conn.close();
               }catch(final Exception ex){
                  conn = null;
               }
            }
         }
      }
      return ids;
   }
}