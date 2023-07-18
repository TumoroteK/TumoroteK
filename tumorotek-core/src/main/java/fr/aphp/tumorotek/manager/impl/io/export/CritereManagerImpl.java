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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.io.export.CombinaisonDao;
import fr.aphp.tumorotek.dao.io.export.CritereDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.SearchedObjectIdNotExistException;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.export.CombinaisonManager;
import fr.aphp.tumorotek.manager.io.export.CritereManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.io.export.CritereValidator;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Combinaison;
import fr.aphp.tumorotek.model.io.export.Critere;

/**
 *
 * Implémentation du manager du bean de domaine Critère.
 * Classe créée le 02/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class CritereManagerImpl implements CritereManager
{

   private final Logger log = LoggerFactory.getLogger(CritereManager.class);

   /** Bean Dao CritereDao. */
   private CritereDao critereDao = null;

   /** Bean Dao CritereDao. */
   private ChampDao champDao = null;

   /** Bean Dao CombinaisonDao. */
   private CombinaisonDao combinaisonDao = null;

   /** Bean Manager CombinaisonManager. */
   private CombinaisonManager combinaisonManager;

   /** Bean Dao ChampManager. */
   private ChampManager champManager = null;

   /** Bean Validator. */
   private CritereValidator critereValidator = null;

   public CritereManagerImpl(){
      super();
   }

   /**
    * Setter du bean CritereDao.
    * @param critDao est le bean Dao.
    */
   public void setCritereDao(final CritereDao critDao){
      this.critereDao = critDao;
   }

   /**
    * Setter du bean ChampDao.
    * @param champDao est le bean Dao.
    */
   public void setChampDao(final ChampDao cDao){
      this.champDao = cDao;
   }

   /**
    * Setter du bean CombinaisonDao.
    * @param combDao est le bean Dao.
    */
   public void setCombinaisonDao(final CombinaisonDao combDao){
      this.combinaisonDao = combDao;
   }

   /**
    * Setter du bean ChampManager.
    * @param cManager est le bean Manager.
    */
   public void setChampManager(final ChampManager cManager){
      this.champManager = cManager;
   }

   /**
    * Setter du bean CombinaisonManager.
    * @param combManager est le bean Manager.
    */
   public void setCombinaisonManager(final CombinaisonManager combManager){
      this.combinaisonManager = combManager;
   }

   /**
    * Setter du bean CritereValidator.
    * @param critereValidator est le bean Validator.
    */
   public void setCritereValidator(final CritereValidator validator){
      this.critereValidator = validator;
   }

   /**
    * Copie un Critère en BDD.
    * @param critere Critère à copier.
    * @return le Critère copié.
    */
   @Override
   public Critere copyCritereManager(final Critere critere){
      Critere temp = null;
      //On vérifie que le critere n'est pas nul
      if(critere == null){
         log.warn("Objet obligatoire Critere manquant lors de la copie d'un objet Critere");
         throw new RequiredObjectIsNullException("Critere", "copie", "Critere");
      }
      if(critere.getChamp() != null){
         Champ champ = null;
         //On copie le champ
         champ = champManager.copyChampManager(critere.getChamp());
         /*if (critere.getChamp().getChampAnnotation() != null) {
         	champ = new Champ(critere.getChamp()
         			.getChampAnnotation());
         	champManager.createObjectManager(champ, champ
         		.getChampParent());
         } else {
         	champ = new Champ(critere.getChamp()
         			.getChampEntite());
         	champManager.createObjectManager(champ, champ
         		.getChampParent());
         }*/
         temp = new Critere(champ, critere.getOperateur(), critere.getValeur());
      }else if(critere.getCombinaison() != null){
         final Combinaison combinaison = combinaisonManager.copyCombinaisonManager(critere.getCombinaison());
         temp = new Critere(combinaison, critere.getOperateur(), critere.getValeur());
      }
      BeanValidator.validateObject(critere, new Validator[] {critereValidator});
      critereDao.createObject(temp);
      return temp;
   }

   /**
    * Créé un Critère en BDD.
    * @param critere Critère à créer.
    * @param champ Champ du Critère.
    * @param combinaison Combinaison du Critère.
    */
   @Override
   public void createObjectManager(final Critere critere, Champ champ, Combinaison combinaison){
      //On vérifie que le critère n'est pas nul
      if(critere == null){
         log.warn("Objet obligatoire Critere manquant lors de la création d'un objet Critere");
         throw new RequiredObjectIsNullException("Critere", "création", "Critere");
      }
      if(champ != null){
         if(champ.getChampId() != null){
            champ = champDao.mergeObject(champ);
         }else{
            champManager.createObjectManager(champ, champ.getChampParent());
         }
      }
      critere.setChamp(champ);
      if(combinaison != null){
         if(combinaison.getCombinaisonId() != null){
            combinaison = combinaisonDao.mergeObject(combinaison);
         }else{
            combinaisonDao.createObject(combinaison);
         }
      }
      critere.setCombinaison(combinaison);
      BeanValidator.validateObject(critere, new Validator[] {critereValidator});
      critereDao.createObject(critere);
   }

   /**
    * Met à jour un Critère en BDD.
    * @param critere Critère à mettre à jour.
    * @param champ Champ du Critère.
    * @param combinaison Combinaison du Critère.
    */
   @Override
   public void updateObjectManager(final Critere critere, Champ champ, Combinaison combinaison){
      //On vérifie que le critère n'est pas nul
      if(critere == null){
         log.warn("Objet obligatoire Critere manquant lors de la modification d'un objet Critere");
         throw new RequiredObjectIsNullException("Critere", "modification", "Critere");
      }
      final Champ oldChamp = critere.getChamp();
      if(champ != null){
         if(champ.getChampId() != null){
            champ = champDao.mergeObject(champ);
         }else{
            champManager.createObjectManager(champ, champ.getChampParent());
         }
      }
      critere.setChamp(champ);
      if(combinaison != null){
         if(combinaison.getCombinaisonId() != null){
            combinaison = combinaisonDao.mergeObject(combinaison);
         }else{
            combinaisonDao.createObject(combinaison);
         }
      }
      critere.setCombinaison(combinaison);
      BeanValidator.validateObject(critere, new Validator[] {critereValidator});
      critereDao.updateObject(critere);

      // On supprime l'ancien champ
      if(oldChamp != null && oldChamp.getChampId() != null && !oldChamp.equals(critere.getChamp())){
         champManager.removeObjectManager(oldChamp);
      }
   }

   /**
    * Supprime un Critère en BDD.
    * @param critere Critère à supprimer.
    */
   @Override
   public void removeObjectManager(final Critere critere){
      //On vérifie que le critère n'est pas nul
      if(critere == null){
         log.warn("Objet obligatoire Critere manquant lors de la suppression d'un objet Critere");
         throw new RequiredObjectIsNullException("Critere", "suppression", "Critere");
      }
      //On vérifie que le critère est en BDD
      if(findByIdManager(critere.getCritereId()) == null){
         throw new SearchedObjectIdNotExistException("Critere", critere.getCritereId());
      }
      final Champ oldChamp = critere.getChamp();
      critereDao.removeObject(critere.getCritereId());

      //On supprime le champ
      if(oldChamp != null){
         champManager.removeObjectManager(oldChamp);
      }
   }

   /**
    * Recherche un Critère dont l'identifiant est passé en paramètre.
    * @param idCritere Identifiant du Critère que l'on recherche.
    * @return une Combinaison.
    */
   @Override
   public Critere findByIdManager(final Integer idCritere){
      //On vérifie que l'identifiant n'est pas nul
      if(idCritere == null){
         log.warn("Objet obligatoire identifiant manquant lors de la recherche par l'identifiant d'un objet Critere");
         throw new RequiredObjectIsNullException("Critere", "recherche par identifiant", "identifiant");
      }
      return critereDao.findById(idCritere);
   }

   /**
    * Recherche tous les Critères présents dans la BDD.
    * @return Liste de Critères.
    */
   @Override
   public List<Critere> findAllObjectsManager(){
      return critereDao.findAll();
   }
}