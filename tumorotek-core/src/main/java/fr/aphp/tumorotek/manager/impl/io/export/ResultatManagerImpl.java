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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.io.export.AffichageDao;
import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.io.export.ResultatDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.SearchedObjectIdNotExistException;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.export.ResultatManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.io.export.ResultatValidator;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Resultat;

/**
 *
 * Implémentation du manager du bean de domaine Résultat.
 * Classe créée le 02/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class ResultatManagerImpl implements ResultatManager
{

   private final Log log = LogFactory.getLog(ResultatManager.class);

   /** Bean Dao ResultatDao. */
   private ResultatDao resultatDao = null;
   /** Bean Dao AffichageDao. */
   private AffichageDao affichageDao;
   /** Bean Dao ChampDao. */
   private ChampDao champDao;
   /** Bean Manager ChampManager. */
   private ChampManager champManager;
   /** Bean Validator. */
   private ResultatValidator resultatValidator;

   public ResultatManagerImpl(){
      super();
   }

   /**
    * Setter du bean ResultatDao.
    * @param resDao est le bean Dao.
    */
   public void setResultatDao(final ResultatDao resDao){
      this.resultatDao = resDao;
   }

   /**
    * Setter du bean AffichageDao.
    * @param aDao est le bean Dao.
    */
   public void setAffichageDao(final AffichageDao aDao){
      this.affichageDao = aDao;
   }

   /**
    * Setter du bean ChampDao.
    * @param cDao est le bean Dao.
    */
   public void setChampDao(final ChampDao cDao){
      this.champDao = cDao;
   }

   /**
    * Setter du bean ChampManager.
    * @param cManager est le bean Manager.
    */
   public void setChampManager(final ChampManager cManager){
      this.champManager = cManager;
   }

   /**
    * Setter du bean ResultatValidator.
    * @param resultatValidator est le bean Validator.
    */
   public void setResultatValidator(final ResultatValidator validator){
      this.resultatValidator = validator;
   }

   /**
    * Recherche un Résultat dont l'identifiant est passé en paramètre.
    * @param idResultat Identifiant du Résultat que l'on recherche.
    * @return un Résultat.
    */
   @Override
   public Resultat findByIdManager(final Integer idResultat){
      //On vérifie que l'identifiant n'est pas nul
      if(idResultat == null){
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet Resultat");
         throw new RequiredObjectIsNullException("Resultat", "recherche par identifiant", "identifiant");
      }
      return resultatDao.findById(idResultat);
   }

   /**
    * Recherche tous les Résultats présents dans la BDD.
    * @return Liste de Résultats.
    */
   @Override
   public List<Resultat> findAllObjectsManager(){
      return resultatDao.findAll();
   }

   /**
    * Copie un Résultat en BDD.
    * @param resultat Résultat à copier.
    * @return le Résultat copié.
    */
   @Override
   public Resultat copyResultatManager(final Resultat resultat, final Affichage affichage){
      //On vérifie que le résultat n'est pas nul
      if(resultat == null){
         log.warn("Objet obligatoire Resultat manquant lors " + "de la copie d'un objet Resultat");
         throw new RequiredObjectIsNullException("Resultat", "copie", "Resultat");
      }
      //On vérifie que l'affichage n'est pas nul
      if(affichage == null){
         log.warn("Objet obligatoire Affichage manquant lors " + "de la copie d'un objet Resultat");
         throw new RequiredObjectIsNullException("Resultat", "copie", "Affichage");
      }
      //On copie le champ
      Champ champ = null;
      if(resultat.getChamp() != null){
         champ = champManager.copyChampManager(resultat.getChamp());
      }
      final Resultat temp = new Resultat(resultat.getNomColonne(), champ, resultat.getTri(), resultat.getOrdreTri(),
         resultat.getPosition(), resultat.getFormat(), affichage);
      BeanValidator.validateObject(resultat, new Validator[] {resultatValidator});
      createObjectManager(temp, temp.getAffichage(), temp.getChamp());
      return temp;
   }

   /**
    * Créé un Résultat en BDD.
    * @param resultat Résultat à créer.
    * @param affichage Affichage du Résultat.
    * @param champ Champ du Résultat.
    */
   @Override
   public void createObjectManager(final Resultat resultat, Affichage affichage, Champ champ){
      //On vérifie que le résultat n'est pas nul
      if(resultat == null){
         log.warn("Objet obligatoire Resultat manquant lors " + "de la création d'un objet Resultat");
         throw new RequiredObjectIsNullException("Resultat", "création", "Resultat");
      }
      //On vérifie que l'affichage n'est pas nul
      if(affichage == null){
         log.warn("Objet obligatoire Affichage manquant lors " + "de la création d'un objet Resultat");
         throw new RequiredObjectIsNullException("Resultat", "création", "Affichage");
      }
      //On vérifie que le champ n'est pas nul
      if(champ == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la création d'un objet Resultat");
         throw new RequiredObjectIsNullException("Resultat", "création", "Champ");
      }
      if(champ.getChampId() != null){
         champ = champDao.mergeObject(champ);
      }else{
         champManager.createObjectManager(champ, champ.getChampParent());
      }
      resultat.setChamp(champ);
      if(affichage.getAffichageId() != null){
         affichage = affichageDao.mergeObject(affichage);
      }
      resultat.setAffichage(affichage);
      BeanValidator.validateObject(resultat, new Validator[] {resultatValidator});
      resultatDao.createObject(resultat);
   }

   /**
    * Met à jour un Résultat en BDD.
    * @param resultat Résultat à mettre à jour.
    * @param affichage Affichage du Résultat.
    * @param champ Champ du Résultat.
    */
   @Override
   public void updateObjectManager(final Resultat resultat, Affichage affichage, Champ champ){
      //On vérifie que le résultat n'est pas nul
      if(resultat == null){
         log.warn("Objet obligatoire Resultat manquant lors " + "de la modification d'un objet Resultat");
         throw new RequiredObjectIsNullException("Resultat", "modification", "Resultat");
      }
      //On vérifie que l'affichage n'est pas nul
      if(affichage == null){
         log.warn("Objet obligatoire Affichage manquant lors " + "de la modification d'un objet Resultat");
         throw new RequiredObjectIsNullException("Resultat", "modification", "Affichage");
      }
      //On vérifie que le champ n'est pas nul
      if(champ == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la modification d'un objet Resultat");
         throw new RequiredObjectIsNullException("Resultat", "modification", "Champ");
      }
      final Champ oldChamp = resultat.getChamp();
      if(champ.getChampId() != null){
         champ = champDao.mergeObject(champ);
      }else{
         champManager.createObjectManager(champ, champ.getChampParent());
      }
      resultat.setChamp(champ);
      if(affichage.getAffichageId() != null){
         affichage = affichageDao.mergeObject(affichage);
      }else{
         affichageDao.createObject(affichage);
      }
      resultat.setAffichage(affichage);
      BeanValidator.validateObject(resultat, new Validator[] {resultatValidator});
      resultatDao.updateObject(resultat);

      // On supprime l'ancien champ
      if(oldChamp != null && oldChamp.getChampId() != null && !oldChamp.equals(resultat.getChamp())){
         champManager.removeObjectManager(oldChamp);
      }
   }

   /**
    * Supprime un Résultat.
    * @param groupement Résultat à supprimer.
    */
   @Override
   public void removeObjectManager(final Resultat resultat){
      //On vérifie que le resultat n'est pas nul
      if(resultat == null){
         throw new RequiredObjectIsNullException("Resultat", "suppression", "Resultat");
      }
      //On vérifie que le resultat est en BDD
      if(findByIdManager(resultat.getResultatId()) == null){
         throw new SearchedObjectIdNotExistException("Resultat", resultat.getResultatId());
      }
      final Champ oldChamp = resultat.getChamp();

      resultatDao.removeObject(resultat.getResultatId());

      //On supprime le champ
      if(oldChamp != null){
         champManager.removeObjectManager(oldChamp);
      }
   }

   /**
    * Recherche la liste des Résultats de l'Affichage passé en paramètre.
    * @param affichage Affichage dont on souhaite obtenir les Résultats.
    * @return la liste des Résultats d'un Affichage
    */
   @Override
   public List<Resultat> findByAffichageManager(final Affichage affichage){
      //On vérifie que l'affichage n'est pas nul
      if(affichage == null){
         log.warn("Objet obligatoire Affichage manquant lors de la " + "recherche par l'Affichage d'un objet Resultat");
         throw new RequiredObjectIsNullException("Resultat", "recherche par Affichage", "Affichage");
      }
      return resultatDao.findByAffichage(affichage);
   }
}
