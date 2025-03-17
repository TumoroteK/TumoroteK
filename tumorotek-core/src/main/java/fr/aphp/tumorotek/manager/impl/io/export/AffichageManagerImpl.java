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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.model.contexte.Plateforme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.io.export.AffichageDao;
import fr.aphp.tumorotek.dao.io.export.RechercheDao;
import fr.aphp.tumorotek.dao.io.export.ResultatDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.SearchedObjectIdNotExistException;
import fr.aphp.tumorotek.manager.io.export.AffichageManager;
import fr.aphp.tumorotek.manager.io.export.ResultatManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.io.export.AffichageValidator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager du bean de domaine Affichage.
 * Classe créée le 02/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class AffichageManagerImpl implements AffichageManager
{

   private final Logger log = LoggerFactory.getLogger(AffichageManager.class);

   /** Liste des affichages du manager. */
   private final List<Affichage> affichages = new ArrayList<>();

   /** Bean Dao AffichageDao. */
   private AffichageDao affichageDao = null;

   /** Bean Dao ResultatDao. */
   private ResultatDao resultatDao = null;

   /** Bean Manager ResultatManager. */
   private ResultatManager resultatManager = null;

   /** Bean Validator. */
   private AffichageValidator affichageValidator;

   private RechercheDao rechercheDao;

   public AffichageManagerImpl(){
      super();
   }

   public void setAffichageValidator(final AffichageValidator validator){
      this.affichageValidator = validator;
   }

   /**
    * Setter du bean AffichageDao.
    * @param affDao est le bean Dao.
    */
   public void setAffichageDao(final AffichageDao affDao){
      this.affichageDao = affDao;
   }

   /**
    * Setter du bean ResultatDao.
    * @param rDao est le bean Dao.
    */
   public void setResultatDao(final ResultatDao rDao){
      this.resultatDao = rDao;
   }

   /**
    * Setter du bean ResultatManager.
    * @param resManager est le bean Manager
    */
   public void setResultatManager(final ResultatManager resManager){
      this.resultatManager = resManager;
   }

   public void setRechercheDao(final RechercheDao rDao){
      this.rechercheDao = rDao;
   }

   /**
    * Recherche un Affichage dont l'identifiant est passé en paramètre.
    * @param affichageId Identifiant de l'Affichage que l'on recherche.
    * @return un Affichage.
    */
   @Override
   public Affichage findByIdManager(final Integer id){
      //On vérifie que l'identifiant n'est pas nul
      if(id == null){
         log.warn("Objet obligatoire identifiant manquant lors de la recherche par l'identifiant d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "recherche par identifiant", "identifiant");
      }
      return affichageDao.findById(id);
   }

   /**
    * Recherche tous les Affichages présents dans la BDD.
    * @return Liste d'Affichages.
    */
   @Override
   public List<Affichage> findAllObjectsManager(){
      log.debug("Recherche de tous les Affichages");
      return affichageDao.findAll();
   }

   //A brancher sur le front (TK-641)
   //NB : la modification en base de l'intitulé est faite
   //par l'appel de l'update global sur l'objet donc
   //il vaut mieux le récupérer avant pour ne pas écraser
   //des modifications faites entre temps par un autre utilisateur
   //Sinon, il faut écrire une requête hql pour mettre à jour uniquement
   //ce champ. Ceci serai la méthode la plus optimisée.
   /**
    * Renomme un Affichage (change son intitulé).
    * @param affichage Affichage à renommer.
    * @param intitule nouvel intitulé de l'Affichage.
    */
   @Override
   public void renameAffichageManager(final Affichage affichage, final String intitule){
      //On verifie que l'affichage n'est pas nul
      if(affichage == null){
         log.warn("Objet obligatoire Affichage manquant lors du renommage d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "modification", "Affichage");
      }
      Affichage refreshedAffichage = findByIdManager(affichage.getAffichageId());
      if(refreshedAffichage == null){
         throw new SearchedObjectIdNotExistException("Affichage", affichage.getAffichageId());
      }
      //on modifie l'intitule de l'affichage
      refreshedAffichage.setIntitule(intitule);
      //On met a jour l'affichage
      if(isDoublonIntituleInPlateformeManager(refreshedAffichage, refreshedAffichage.getBanque().getPlateforme())){
         log.warn("Doublon lors de la modification de l'objet Affichage : {}",  affichage);
         throw new DoublonFoundException("Affichage", "modification");
      }
      BeanValidator.validateObject(refreshedAffichage, new Validator[] {affichageValidator});
      affichageDao.updateObject(refreshedAffichage);
      log.info("Modification de l'objet Affichage : {}",  refreshedAffichage);
   }


   /**
    * Créé un nouvel Affichage en BDD.
    * @param affichage Affichage à créer.
    * @param createur Utilisateur qui créé l'Affichage.
    */
   @Override
   public void createObjectManager(final Affichage affichage, final List<Resultat> resultats, final Utilisateur createur,
      final Banque banque){
      //On vérifie que l'affichage n'est pas nul
      if(affichage == null){
         log.warn("Objet obligatoire Affichage manquant lors de la création d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "création", "Affichage");
      }
      //On vérifie que le créateur n'est pas nul
      if(createur == null){
         log.warn("Objet obligatoire Utilisateur manquant lors de la création d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "création", "Utilisateur");
      }
      // On met l'utilisateur dans l'affichage
      affichage.setCreateur(createur);
      //On vérifie que la banque n'est pas nul
      if(banque == null){
         log.warn("Objet obligatoire Banque manquant lors de la création d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "création", "Banque");
      }
      // On vérifie si un affichage avec le même intitulé existe déjà
      if(isDoublonIntituleInPlateformeManager(affichage, banque.getPlateforme())){
         log.warn("Doublon lors de la creation de l'objet Affichage : {}",  affichage);
         throw new DoublonFoundException("Affichage", "creation");
      }
      // On met l'utilisateur dans l'affichage
      affichage.setBanque(banque);
      
      // On enregistre l'affichage
      BeanValidator.validateObject(affichage, new Validator[] {affichageValidator});
      affichageDao.createObject(affichage);

      if(resultats != null){
         updateResultatsManager(affichage, resultats, null);
      }

      // ajout de la requete dans la liste
      affichages.add(affichage);
      log.info("Enregistrement de l'objet Affichage : {}",  affichage);
   }

   /**
    * Met à jour un Affichage en BDD.
    * @param affichage Affichage à mettre à jour.
    */
   @Override
   public void updateObjectManager(final Affichage affichage, final List<Resultat> resultats,
      final List<Resultat> resultatsToRemove){
      //On vérifie que l'affichage n'est pas nul
      if(affichage == null){
         log.warn("Objet obligatoire Affichage manquant lors de la modification d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "modification", "Affichage");
      }
      if(findByIdManager(affichage.getAffichageId()) == null){
         throw new SearchedObjectIdNotExistException("Affichage", affichage.getAffichageId());
      }
      //On met à jour l'affichage
      if(isDoublonIntituleInPlateformeManager(affichage, affichage.getBanque().getPlateforme())){
         log.warn("Doublon lors de la modification de l'objet Affichage : {}",  affichage);
         throw new DoublonFoundException("Affichage", "modification");
      }
      BeanValidator.validateObject(affichage, new Validator[] {affichageValidator});
      affichageDao.updateObject(affichage);

      if(resultats != null){
         updateResultatsManager(affichage, resultats, resultatsToRemove);
      }

      log.info("Modification de l'objet Affichage : {}",  affichage);

   }

   @Override
   public Boolean isUsedObjectManager(final Affichage affichage){
      if(affichage != null && affichage.getAffichageId() != null){
         return (rechercheDao.findByAffichage(affichage).size() > 0);
      }
      return false;
   }


   /**
    * Supprimme un Affichage en BDD.
    * @param affichage Affichage à supprimer.
    */
   @Override
   public void removeObjectManager(final Affichage affichage){
      //On vérifie que l'affichage n'est pas nul
      if(affichage == null){
         throw new RequiredObjectIsNullException("Affichage", "suppression", "Affichage");
      }
      //On vérifie que l'affichage est en BDD
      if(findByIdManager(affichage.getAffichageId()) == null){
         throw new SearchedObjectIdNotExistException("Affichage", affichage.getAffichageId());
      }
      if(isUsedObjectManager(affichage)){
         log.warn("Objet utilisé lors de la suppression de l'objet Affichage : {}",  affichage);
         throw new ObjectUsedException("Affichage", "suppression");
      }
      //suppression de l'affichage dans la liste
      final Iterator<Affichage> it = affichages.iterator();
      while(it.hasNext()){
         final Affichage temp = it.next();
         if(temp.getAffichageId().equals(affichage.getAffichageId())){
            affichages.remove(temp);
            break;
         }
      }
      //suppression de l'affichage en BDD
      //updateObjectManager(affichage, new ArrayList<Resultat>());
      final List<Resultat> res = resultatManager.findByAffichageManager(affichage);
      for(int i = 0; i < res.size(); i++){
         resultatManager.removeObjectManager(res.get(i));
      }
      affichageDao.removeObject(affichage.getAffichageId());
   }


   public void updateResultatsManager(final Affichage affichage, final List<Resultat> resultats,
      final List<Resultat> resultatsToRemove){

      // suppression des résultats
      if(resultatsToRemove != null){
         for(int i = 0; i < resultatsToRemove.size(); i++){
            resultatManager.removeObjectManager(resultatsToRemove.get(i));
         }
      }

      // Maj des résultats
      if(resultats != null){
         for(int i = 0; i < resultats.size(); i++){
            resultats.get(i).setAffichage(affichage);
            if(resultats.get(i).getResultatId() == null){
               resultatManager.createObjectManager(resultats.get(i), affichage, resultats.get(i).getChamp());
            }else{
               resultatManager.updateObjectManager(resultats.get(i), affichage, resultats.get(i).getChamp());
            }
         }
      }
   }

   @Override
   public List<Affichage> findByBanqueManager(final Banque banque){
      if(banque != null){
         return affichageDao.findByBanque(banque);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Affichage> findByBanqueInLIstManager(final List<Banque> banques){
      if(banques != null && banques.size() > 0){
         return affichageDao.findByBanqueInList(banques);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Affichage> findByIntituleInPlateformeManager(String intitule, Plateforme plateforme) {
      if (intitule == null || plateforme == null) {
         return Collections.emptyList();
      }
      return affichageDao.findByIntituleInPlateforme(intitule, plateforme);
   }

   //Depuis le ticket TK-524, cette méthode remplace la méthode findDoublonManager :
   //le contrôle de "doublon" est désormais fait sur l'intitulé uniquement pour une plateforme donnée. 
   //Le nom de la méthode a été modifié pour mieux refléter ce qu'elle fait.
   @Override
   public boolean isDoublonIntituleInPlateformeManager(Affichage affichage, Plateforme plateforme) {
      List<Affichage> intitulesExistants = findByIntituleInPlateformeManager(affichage.getIntitule(), plateforme);
      
      // Si la liste n'est pas vide, cela signifie que l'intitulé existe déjà
      if(!intitulesExistants.isEmpty()) {
         // Si l'affichage n'a pas d'ID, cela signifie que c'est un nouvel ajout
         if(affichage.getAffichageId() == null)  {
            return true;
         }
         // Sinon, on modifie l'objet : vérifier si l'intitulé appartient à un **autre** Affichage
         for(final Affichage affichageCourant : intitulesExistants) {
            if(!affichage.getAffichageId().equals(affichageCourant.getAffichageId())) return true;
         }
      }
      return false;
   }


}
