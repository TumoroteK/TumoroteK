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
package fr.aphp.tumorotek.manager.impl.io.export;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.io.export.GroupementDao;
import fr.aphp.tumorotek.dao.io.export.RechercheDao;
import fr.aphp.tumorotek.dao.io.export.RequeteDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.SearchedObjectIdNotExistException;
import fr.aphp.tumorotek.manager.io.export.GroupementManager;
import fr.aphp.tumorotek.manager.io.export.RequeteManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.io.export.RequeteValidator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Groupement;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager du bean de domaine Requête.
 * Classe créée le 02/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class RequeteManagerImpl implements RequeteManager
{

   private final Logger log = LoggerFactory.getLogger(RequeteManager.class);

   /** Liste des requêtes du manager. */
   private List<Requete> requetes = new ArrayList<>();

   /** Bean Dao RequeteDao. */
   private RequeteDao requeteDao = null;

   /** Bean Manager GroupementManager. */
   private GroupementManager groupementManager = null;

   /** Bean Dao GroupementDao. */
   private GroupementDao groupementDao = null;

   /** Bean Validator. */
   private RequeteValidator requeteValidator = null;

   private RechercheDao rechercheDao;

   public RequeteManagerImpl(){
      super();
   }

   /**
    * Setter du bean RequeteDao.
    * @param reqDao est le bean Dao.
    */
   public void setRequeteDao(final RequeteDao reqDao){
      this.requeteDao = reqDao;
   }

   /**
    * Setter du bean GroupementManager.
    * @param grManager est le bean Manager
    */
   public void setGroupementManager(final GroupementManager grManager){
      this.groupementManager = grManager;
   }

   /**
    * Setter du bean GroupementDao.
    * @param grDao est le bean Dao.
    */
   public void setGroupementDao(final GroupementDao grDao){
      this.groupementDao = grDao;
   }

   /**
    * Setter du bean RequeteValidator.
    * @param requeteValidator est le bean Validator.
    */
   public void setRequeteValidator(final RequeteValidator validator){
      this.requeteValidator = validator;
   }

   public void setRechercheDao(final RechercheDao rDao){
      this.rechercheDao = rDao;
   }

   /**
    * Recherche une Requête dont l'identifiant est passé en paramètre.
    * @param id Identifiant de la Requête que l'on recherche.
    * @return une Requête.
    */
   @Override
   public Requete findByIdManager(final Integer id){
      //On vérifie que l'identifiant n'est pas nul
      if(id == null){
         log.warn("Objet obligatoire identifiant manquant lors de la recherche par l'identifiant d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "recherche par identifiant", "identifiant");
      }
      return requeteDao.findById(id);
   }

   /**
    * Recherche toutes les Requêtes présentes dans la BDD.
    * @return Liste de Requêtes.
    */
   @Override
   public List<Requete> findAllObjectsManager(){
      return requeteDao.findAll();
   }

   /**
    * Renomme une Requête (change son intitulé).
    * @param requete Requête à renommer.
    * @param intitule nouvel intitulé de la Requête.
    */
   @Override
   public void renameRequeteManager(final Requete requete, final String intitule){
      //On verifie que la requete n'est pas nulle
      if(requete == null){
         log.warn("Objet obligatoire Requete manquant lors du renommage d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "modification", "Requete");
      }
      if(findByIdManager(requete.getRequeteId()) == null){
         throw new SearchedObjectIdNotExistException("Requete", requete.getRequeteId());
      }else{
         //on modifie l'intitule
         requete.setIntitule(intitule);
         //On met a jour la requete
         if(findDoublonManager(requete)){
            log.warn("Doublon lors de la modification de l'objet " + "Requete : " + requete.toString());
            throw new DoublonFoundException("Requete", "modification");
         }else{
            BeanValidator.validateObject(requete, new Validator[] {requeteValidator});
            requeteDao.updateObject(requete);
         }
      }
   }

   /**
    * Copie une Requête en BDD.
    * @param requete Requête à copier.
    * @param copieur Utilisateur qui copie la Requête.
    * @return la Requête copiée.
    */
   @Override
   public Requete copyRequeteManager(final Requete requete, final Utilisateur copieur, final Banque banque){
      //On verifie que la requete n'est pas nulle
      if(requete == null){
         log.warn("Objet obligatoire Requete manquant lors de la copie d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "copie", "Requete");
      }
      //On verifie que l'utilisateur n'est pas nul
      if(copieur == null){
         log.warn("Objet obligatoire Utilisateur manquant lors de la copie d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "modification", "Utilisateur");
      }
      //On vérifie que la banque n'est pas nul
      if(banque == null){
         log.warn("Objet obligatoire Banque manquant lors de la création d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "création", "Banque");
      }
      //On copie le groupement racine
      final Groupement groupement = groupementManager.copyGroupementManager(requete.getGroupementRacine());
      //copie de la requete
      final Requete r = new Requete(requete.getIntitule(), copieur, groupement);
      r.setBanque(banque);
      BeanValidator.validateObject(r, new Validator[] {requeteValidator});
      //enregistrement de la requete en BDD
      requeteDao.createObject(r);
      //ajout de la requete copiee dans la liste
      requetes.add(r);

      return r;
   }

   /**
    * Créé une nouvelle Requête en BDD.
    * @param requete Requête à créer.
    * @param createur Utilisateur qui créé la Requête.
    */
   @Override
   public void createObjectManager(final Requete requete, Groupement groupement, final Utilisateur createur, final Banque banque){
      //On vérifie que la requête n'est pas nulle
      if(requete == null){
         log.warn("Objet obligatoire Requete manquant lors de la création d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "création", "Requete");
      }
      //On vérifie que le groupement n'est pas nul
      if(groupement == null){
         log.warn("Objet obligatoire Groupement manquant lors de la création d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "création", "Groupement");
      }
      //On vérifie que le createur n'est pas nul
      if(createur == null){
         log.warn("Objet obligatoire Utilisateur manquant lors de la création d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "création", "Utilisateur");
      }
      //On vérifie que la banque n'est pas nul
      if(banque == null){
         log.warn("Objet obligatoire Banque manquant lors de la création d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "création", "Banque");
      }
      requete.setBanque(banque);
      if(groupement.getGroupementId() != null){
         groupement = groupementDao.mergeObject(groupement);
      }else{
         groupementManager.createObjectManager(groupement, groupement.getCritere1(), groupement.getCritere2(),
            groupement.getOperateur(), groupement.getParent());
      }
      requete.setGroupementRacine(groupement);
      // On met l'utilisateur dans la requete
      requete.setCreateur(createur);
      BeanValidator.validateObject(requete, new Validator[] {requeteValidator});
      // On enregistre la requete
      requeteDao.createObject(requete);
      // ajout de la requete dans la liste
      requetes.add(requete);
   }

   /**
    * Met à jour une Requête en BDD.
    * @param requete Requête à mettre à jour.
    * @param createur Utilisateur qui met à jour la Requête.
    */
   @Override
   public void updateObjectManager(final Requete requete, Groupement groupement, final Utilisateur createur){
      //On vérifie que la requête n'est pas nulle
      if(requete == null){
         log.warn("Objet obligatoire Requete manquant lors de la modification d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "modification", "Requete");
      }
      //On vérifie que le groupement n'est pas nul
      if(groupement == null){
         log.warn("Objet obligatoire Groupement manquant lors de la modification d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "modification", "Groupement");
      }
      //On vérifie que le createur n'est pas nul
      if(createur == null){
         log.warn("Objet obligatoire Utilisateur manquant lors de la modification d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "modification", "Utilisateur");
      }
      final Groupement oldGroupement = requete.getGroupementRacine();
      if(groupement.getGroupementId() != null){
         groupement = groupementDao.mergeObject(groupement);
      }else{
         groupementManager.createObjectManager(groupement, groupement.getCritere1(), groupement.getCritere2(),
            groupement.getOperateur(), groupement.getParent());
      }
      requete.setGroupementRacine(groupement);
      // On met l'utilisateur dans la requete
      requete.setCreateur(createur);
      // On vérifie que le bean est valide
      BeanValidator.validateObject(requete, new Validator[] {requeteValidator});
      // On met à jour la requete
      requeteDao.updateObject(requete);
      // mise a jour de la liste des requetes
      requetes = findAllObjectsManager();

      //On supprime l'ancien groupement racine.
      if(oldGroupement != null){
         groupementManager.removeObjectManager(oldGroupement);
      }
   }

   /**
    * Supprime une Requête en BDD.
    * @param requete Requête à supprimer
    */
   @Override
   public void removeObjectManager(final Requete requete){
      //On vérifie que la requête n'est pas nulle
      if(requete == null){
         throw new RequiredObjectIsNullException("Requete", "suppression", "Requete");
      }
      //On vérifie que la requête est en BDD
      if(findByIdManager(requete.getRequeteId()) == null){
         throw new SearchedObjectIdNotExistException("Requete", requete.getRequeteId());
      }else{
         if(isUsedObjectManager(requete)){
            log.warn("Objet utilisé lors de la suppression de l'objet " + "Requete : " + requete.toString());
            throw new ObjectUsedException("Requete", "suppression");
         }else{
            //suppression de la requete dans la liste
            final Iterator<Requete> it = requetes.iterator();
            while(it.hasNext()){
               final Requete temp = it.next();
               if(temp.getRequeteId().equals(requete.getRequeteId())){
                  requetes.remove(temp);
                  break;
               }
            }

            // On supprime le groupement racine
            groupementManager.removeObjectManager(requete.getGroupementRacine());

            // On supprime la requête
            requeteDao.removeObject(requete.getRequeteId());
         }
      }
   }

   /**
    * Recherche les Requêtes dont l'utilisateur créateur est passé en
    * paramètre.
    * @param util Utilisateur qui à créé les Requêtes recherchées.
    * @return la liste de toutes les Requêtes de l'Utilisateur.
    */
   @Override
   public List<Requete> findByUtilisateurManager(final Utilisateur util){
      //On vérifie que l'utilisateur n'est pas nul
      if(util == null){
         log.warn("Objet obligatoire Utilisateur manquant lors de la recherche par l'Utilisateur d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "recherche par Utilisateur", "Utilisateur");
      }
      return requeteDao.findByUtilisateur(util);
   }

   /**
    * Recherche les Requêtes dont l'intitulé est passé en paramètre.
    * @param intitule Intitulé des Requêtes recherchées.
    * @return la liste de toutes les Requêtes de l'intitulé.
    */
   @Override
   public List<Requete> findByIntituleManager(final String intitule){
      //On vérifie que l'utilisateur n'est pas nul
      if(intitule == null){
         log.warn("Objet obligatoire intitule manquant lors de la recherche par l'intitulé d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "recherche par Intitulé", "Intitulé");
      }
      return requeteDao.findByIntitule(intitule);
   }

   @Override
   public List<Requete> findByIntituleAndUtilisateurManager(final String intitule, final Utilisateur util){
      if(intitule != null && util != null){
         return requeteDao.findByIntituleUtilisateur(intitule, util);
      }else{
         return new ArrayList<>();
      }
   }

   /**
    * Recherche les doublons d'une Requete passée en paramètre.
    * @param requete une Requete pour laquelle on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonManager(final Requete requete){
      //On vérifie que l'affichage n'est pas nul
      if(requete == null){
         log.warn("Objet obligatoire Requete manquant lors de la recherche de doublon d'un objet Requete");
         throw new RequiredObjectIsNullException("Requete", "recherche de doublon", "Requete");
      }
      if(requete.getRequeteId() == null){
         return requeteDao.findAll().contains(requete);
      }else{
         return requeteDao.findByExcludedId(requete.getRequeteId()).contains(requete);
      }

   }

   /**
    * Méthode qui permet de vérifier que 2 Requêtes sont des copies.
    * @param r Requête première Requête à vérifier.
    * @param copie deuxième Requête à vérifier.
    * @return true si les 2 Requêtes sont des copies, false sinon.
    */
   @Override
   public Boolean isCopyManager(final Requete r, final Requete copie){
      if(copie == null){
         return false;
      }else if(r.getIntitule() == null){
         if(copie.getIntitule() == null){
            if(r.getGroupementRacine() == null){
               return (copie.getGroupementRacine() == null);
            }else{
               return groupementManager.isCopyManager(r.getGroupementRacine(), copie.getGroupementRacine());
            }
         }else{
            return false;
         }
      }else{
         if(r.getIntitule().equals(copie.getIntitule())){
            if(r.getGroupementRacine() == null){
               return (copie.getGroupementRacine() == null);
            }else{
               return groupementManager.isCopyManager(r.getGroupementRacine(), copie.getGroupementRacine());
            }
         }else{
            return false;
         }
      }
   }

   @Override
   public Boolean isUsedObjectManager(final Requete requete){
      if(requete != null && requete.getRequeteId() != null){
         return (rechercheDao.findByRequete(requete).size() > 0);
      }else{
         return false;
      }
   }

   @Override
   public List<Requete> findByBanqueManager(final Banque banque){
      if(banque != null){
         return requeteDao.findByBanque(banque);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Requete> findByBanqueInLIstManager(final List<Banque> banques){
      if(banques != null && banques.size() > 0){
         return requeteDao.findByBanqueInList(banques);
      }else{
         return new ArrayList<>();
      }
   }

}