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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

   private final Log log = LogFactory.getLog(AffichageManager.class);

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
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet Affichage");
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

   /**
    * Renomme un Affichage (change son intitulé).
    * @param affichage Affichage à renommer.
    * @param intitule nouvel intitulé de l'Affichage.
    */
   @Override
   public void renameAffichageManager(final Affichage affichage, final String intitule){
      //On verifie que l'affichage n'est pas nul
      if(affichage == null){
         log.warn("Objet obligatoire Affichage manquant lors " + "du renommage d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "modification", "Affichage");
      }
      if(findByIdManager(affichage.getAffichageId()) == null){
         throw new SearchedObjectIdNotExistException("Affichage", affichage.getAffichageId());
      }
      //on modifie l'intitule de l'affichage
      affichage.setIntitule(intitule);
      //On met a jour l'affichage
      if(findDoublonManager(affichage)){
         log.warn("Doublon lors de la modification de l'objet " + "Affichage : " + affichage.toString());
         throw new DoublonFoundException("Affichage", "modification");
      }
      BeanValidator.validateObject(affichage, new Validator[] {affichageValidator});
      affichageDao.updateObject(affichage);
      log.info("Modification de l'objet Affichage : " + affichage.toString());
   }

   /**
    * Copie un Affichage en BDD.
    * @param affichage Affichage à copier.
    * @param copieur Utilisateur qui copie l'Affichage.
    * @return l'Affichage copié.
    */
   @Override
   public Affichage copyAffichageManager(final Affichage affichage, final Utilisateur copieur, final Banque banque){
      //On vérifie que l'affichage n'est pas nul
      if(affichage == null){
         log.warn("Objet obligatoire Affichage manquant lors " + "de la copie d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "copie", "Affichage");
      }
      //On vérifie que le créateur n'est pas nul
      if(copieur == null){
         log.warn("Objet obligatoire Utilisateur manquant lors " + "de la copie d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "copie", "Utilisateur");
      }
      //On vérifie que la banque n'est pas nul
      if(banque == null){
         log.warn("Objet obligatoire Banque manquant lors " + "de la création d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "création", "Banque");
      }
      final Affichage a = new Affichage();
      a.setIntitule(affichage.getIntitule());
      a.setCreateur(copieur);
      a.setNbLignes(affichage.getNbLignes());
      a.setBanque(banque);

      // On vérifie que l'affichage est bien enregistré
      if(findDoublonManager(a)){
         log.warn("Doublon lors de la modification de l'objet " + "Affichage : " + a.toString());
         throw new DoublonFoundException("Affichage", "modification");
      }
      final Iterator<Resultat> itR = resultatDao.findByAffichage(affichage).iterator();
      final ArrayList<Resultat> resultats = new ArrayList<>();
      while(itR.hasNext()){
         resultats.add(resultatManager.copyResultatManager(itR.next(), a));
      }
      a.setResultats(resultats);

      createObjectManager(a, a.getResultats(), a.getCreateur(), a.getBanque());
      // ajout de la requete dans la liste
      affichages.add(a);
      log.info("Enregistrement de l'objet Affichage : " + a.toString());

      return a;
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
         log.warn("Objet obligatoire Affichage manquant lors " + "de la création d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "création", "Affichage");
      }
      //On vérifie que le créateur n'est pas nul
      if(createur == null){
         log.warn("Objet obligatoire Utilisateur manquant lors " + "de la création d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "création", "Utilisateur");
      }
      // On met l'utilisateur dans l'affichage
      affichage.setCreateur(createur);
      //On vérifie que la banque n'est pas nul
      if(banque == null){
         log.warn("Objet obligatoire Banque manquant lors " + "de la création d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "création", "Banque");
      }
      // On met l'utilisateur dans l'affichage
      affichage.setBanque(banque);

      // On enregistre l'affichage
      if(findDoublonManager(affichage)){
         log.warn("Doublon lors de la creation de l'objet Affichage : " + affichage.toString());
         throw new DoublonFoundException("Affichage", "creation");
      }
      BeanValidator.validateObject(affichage, new Validator[] {affichageValidator});
      affichageDao.createObject(affichage);

      if(resultats != null){
         updateResultatsManager(affichage, resultats, null);
      }

      // ajout de la requete dans la liste
      affichages.add(affichage);
      log.info("Enregistrement de l'objet Affichage : " + affichage.toString());
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
         log.warn("Objet obligatoire Affichage manquant lors " + "de la modification d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "modification", "Affichage");
      }
      if(findByIdManager(affichage.getAffichageId()) == null){
         throw new SearchedObjectIdNotExistException("Affichage", affichage.getAffichageId());
      }
      //On met à jour l'affichage
      if(findDoublonManager(affichage)){
         log.warn("Doublon lors de la modification de l'objet " + "Affichage : " + affichage.toString());
         throw new DoublonFoundException("Affichage", "modification");
      }
      BeanValidator.validateObject(affichage, new Validator[] {affichageValidator});
      affichageDao.updateObject(affichage);

      if(resultats != null){
         updateResultatsManager(affichage, resultats, resultatsToRemove);
      }

      log.info("Modification de l'objet Affichage : " + affichage.toString());

   }

   @Override
   public Boolean isUsedObjectManager(final Affichage affichage){
      if(affichage != null && affichage.getAffichageId() != null){
         return (rechercheDao.findByAffichage(affichage).size() > 0);
      }
      return false;
   }

   /*private void updateResultatsAffichage(Affichage affichage,
   		List<Resultat> resultats) {
   	//On récupère les résultats de l'affichage
   	List<Resultat> resultatsAff = resultatManager
   			.findByAffichageManager(affichage);
   	//On enregistre ceux qui ne sont pas présents
   	for (int i = 0; i < resultats.size(); i++) {
   		Resultat temp = resultats.get(i);
   		if (!resultatsAff.contains(temp)) {
   			if (temp.getResultatId() != null) {
   				resultatsAff.add(resultatDao.mergeObject(temp));
   			} else {
   				resultatManager.createObjectManager(temp, affichage, 
   						temp.getChamp());
   				resultatsAff.add(temp);
   			}
   		}
   	}
   	//On supprime ceux qui ont disparus
   	for (int i = 0; i < resultatsAff.size(); i++) {
   		Resultat temp = resultatsAff.get(i);
   		if (!resultats.contains(temp)) {
   			resultatsAff.remove(temp);
   			resultatManager.removeObjectManager(temp);
   		}
   	}
   	
   	affichage.setResultats(resultatsAff);
   }*/

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
         log.warn("Objet utilisé lors de la suppression de l'objet " + "Affichage : " + affichage.toString());
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

   /**
    * Associe un nouveau Résultat à un Affichage en BDD.
    * @param affichage Affichage dont on veut associer le Résultat.
    * @param resultat Résultat à créer puis à associer.
    */
   @Override
   public void addResultatManager(final Affichage affichage, final Resultat resultat){
      //On verifie que ni l'affichage ni le résultat ne sont nuls
      if(affichage == null){
         throw new RequiredObjectIsNullException("Affichage", "modification", "Affichage");
      }else if(resultat == null){
         throw new RequiredObjectIsNullException("Affichage", "modification", "Résultat");
      }else{
         // On enregistre le résultat
         resultatManager.createObjectManager(resultat, affichage, resultat.getChamp());
      }
   }

   /**
    * Dissocie un Résultat d'un Affichage puis le supprime en BDD.
    * @param affichage Affichage dont on veut dissocier le Résultat.
    * @param resultat Résultat à dissocier puis à supprimer.
    */
   @Override
   public void removeResultatManager(final Affichage affichage, final Resultat resultat){
      //On verifie que ni l'affichage ni le résultat ne sont nuls
      if(affichage == null){
         throw new RequiredObjectIsNullException("Affichage", "modification", "Affichage");
      }else if(resultat == null){
         throw new RequiredObjectIsNullException("Affichage", "modification", "Résultat");
      }else{
         //On déplace les résultats qui ont une position supérieure
         Iterator<Resultat> it = affichage.getResultats().iterator();
         while(it.hasNext()){
            final Resultat temp = it.next();
            boolean modified = false;
            // On descend les positions supérieures
            if(temp.getPosition() > resultat.getPosition()){
               temp.setPosition(temp.getPosition() - 1);
               modified = true;
            }
            // On descend les ordres de tri supérieurs
            if(temp.getOrdreTri() > resultat.getOrdreTri()){
               temp.setOrdreTri(temp.getOrdreTri() - 1);
               modified = true;
            }
            if(modified){
               resultatManager.updateObjectManager(temp, temp.getAffichage(), temp.getChamp());
            }
         }

         //On supprime le résultat de la liste de l'affichage
         it = affichage.getResultats().iterator();
         while(it.hasNext()){
            final Resultat temp = it.next();
            if(temp.getResultatId().equals(resultat.getResultatId())){
               affichage.getResultats().remove(temp);
               break;
            }
         }

         // On supprime le résultat
         resultatManager.removeObjectManager(resultat);
      }
   }

   /**
    * Recherche les Affichages dont l'utilisateur créateur est passé en
    * paramètre. 
    * @param util Utilisateur qui à créé les Affichages recherchés.
    * @return la liste de tous les Affichages de l'Utilisateur.
    */
   @Override
   public List<Affichage> findByUtilisateurManager(final Utilisateur util){
      //On vérifie que l'utilisateur n'est pas nul
      if(util == null){
         log.warn("Objet obligatoire Utilisateur manquant lors de la " + "recherche par l'Utilisateur d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "recherche par Utilisateur", "Utilisateur");
      }
      return affichageDao.findByUtilisateur(util);
   }

   /**
    * Recherche les Affichages dont l'intitulé est passé en paramètre. 
    * @param intitilé des Affichages recherchés.
    * @return la liste de tous les Affichages de l'intitulé.
    */
   @Override
   public List<Affichage> findByIntituleManager(final String intitule){
      //On vérifie que l'utilisateur n'est pas nul
      if(intitule == null){
         log.warn("Objet obligatoire intitule manquant lors de la " + "recherche par l'intitulé d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "recherche par Intitulé", "Intitulé");
      }
      return affichageDao.findByIntitule(intitule);
   }

   @Override
   public List<Affichage> findByIntituleAndUtilisateurManager(final String intitule, final Utilisateur util){
      if(intitule != null && util != null){
         return affichageDao.findByIntituleUtilisateur(intitule, util);
      }
      return new ArrayList<>();
   }

   /**
    * Déplace un Résultat pour un Affichage.
    * @param affichage Affichage dont les résultats vont changer de position.
    * @param resultat Résultat à déplacer.
    * @param nouvellePosition position à atteindre pour le Résultat.			
    */
   @Override
   public void moveResultatManager(final Affichage affichage, final Resultat resultat, final int nouvellePosition){
      if(affichage != null){
         affichage.setResultats(resultatManager.findByAffichageManager(affichage));
         //On récupère le résultat dans l'affichage
         Resultat res = null;
         for(int i = 0; i < affichage.getResultats().size(); i++){
            if(affichage.getResultats().get(i).equals(resultat)){
               res = affichage.getResultats().get(i);
               break;
            }
         }
         if(resultat != null){
            //On met à jour le résultat en BDD
            affichage.deplacerResultat(res, nouvellePosition);
            updateObjectManager(affichage, affichage.getResultats(), null);
         }
      }
   }

   /**
    * Recherche les doublons d'un Affichage passé en paramètre.
    * @param affichage un Affichage pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonManager(final Affichage affichage){
      //On vérifie que l'affichage n'est pas nul
      if(affichage == null){
         log.warn("Objet obligatoire Affichage manquant lors de la " + "recherche de doublon d'un objet Affichage");
         throw new RequiredObjectIsNullException("Affichage", "recherche de doublon", "Affichage");
      }
      if(affichage.getAffichageId() == null){
         return affichageDao.findAll().contains(affichage);
      }
      return affichageDao.findByExcludedId(affichage.getAffichageId()).contains(affichage);

   }

   /**
    * Méthode qui permet de vérifier que 2 Affichages sont des copies.
    * @param a Affichage premier Affichage à vérifier.
    * @param copie deuxième Affichage à vérifier.
    * @return true si les 2 Affichages sont des copies, false sinon.
    */
   @Override
   public Boolean isCopyManager(Affichage a, final Affichage copie){
      if(copie == null){
         return false;
      }else if(!a.getCreateur().equals(copie.getCreateur())){
         if(a.getIntitule().equals(copie.getIntitule()) && a.getNbLignes().equals(copie.getNbLignes())){
            a = findByIdManager(a.getAffichageId());
            a.getResultats().size();
            final Iterator<Resultat> itResultats = a.getResultats().iterator();
            while(itResultats.hasNext()){
               boolean found = false;
               final Resultat temp = itResultats.next();
               final Iterator<Resultat> itTemp = copie.getResultats().iterator();
               while(itTemp.hasNext()){
                  final Resultat temp2 = itTemp.next();
                  if(temp.isCopy(temp2)){
                     found = true;
                     break;
                  }
               }
               if(!found){
                  return false;
               }
            }
            return true;
         }
         return false;
      }else{
         return false;
      }
   }

   /**
    * Cette méthode met à jour les associations entre un affichage et
    * une liste de resultats.
    * @param affichage Affichage pour lequel on veut mettre à jour
    * les associations.
    * @param resultats Liste des Resultats que l'on veut associer à l'Affichage.
    */
   public void updateResultatsManager2(Affichage affichage, final List<Resultat> resultats){

      affichage = affichageDao.mergeObject(affichage);

      for(int i = 0; i < resultats.size(); i++){
         resultats.get(i).setAffichage(affichage);
      }

      final Iterator<Resultat> it = affichage.getResultats().iterator();
      final List<Resultat> resToRemove = new ArrayList<>();

      // on parcourt les resultats qui sont actuellement associés
      // à l'affichage
      while(it.hasNext()){
         final Resultat tmp = it.next();
         // si un resultat n'est pas dans la nouvelle liste, on
         // le conserve afin de le retirer par la suite
         if(!resultats.contains(tmp)){
            resToRemove.add(tmp);
         }
      }

      // on parcourt la liste des resultats à retirer de
      // l'association
      for(int i = 0; i < resToRemove.size(); i++){
         final Resultat resultat = resultatDao.mergeObject(resToRemove.get(i));
         // on retire le collab de chaque coté de l'association
         affichage.getResultats().remove(resultat);
         log.debug("Suppression de l'association entre le resultat : " + resultat.toString() + " et l'affichage : "
            + affichage.toString());
         resultatManager.removeObjectManager(resToRemove.get(i));
      }

      // on parcourt la nouvelle liste de resultats
      for(int i = 0; i < resultats.size(); i++){
         // si un resultat n'était pas associé à l'affichage
         boolean found = false;
         for(int j = 0; j < affichage.getResultats().size(); j++){
            if(affichage.getResultats().get(j).isCopy(resultats.get(i))){
               found = true;
               break;
            }
         }
         if(!found){
            // on ajoute le resultat des deux cotés de l'association
            if(resultats.get(i).getResultatId() == null){
               resultats.get(i).setAffichage(affichage);
               resultatManager.createObjectManager(resultats.get(i), affichage, resultats.get(i).getChamp());
               affichage.getResultats().add(resultatDao.mergeObject(resultats.get(i)));
            }else{
               resultats.get(i).setAffichage(affichage);
               resultatDao.updateObject(resultats.get(i));
            }
            log.debug("Ajout de l'association entre l'affichage : " + affichage.toString() + " et le resultat : "
               + resultats.get(i).toString());
         }else{
            //Si sa position ou son ordre de tri a changé, on l'update
            final Resultat r = resultats.get(i);
            final Resultat rdb = resultatManager.findByIdManager(r.getResultatId());
            if(r.getPosition() != rdb.getPosition() || r.getOrdreTri() != rdb.getOrdreTri()){
               resultatManager.updateObjectManager(r, r.getAffichage(), r.getChamp());
            }
         }
      }
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
}
