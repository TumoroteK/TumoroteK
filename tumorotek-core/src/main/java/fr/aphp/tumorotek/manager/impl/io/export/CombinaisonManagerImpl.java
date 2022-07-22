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

import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.io.export.CombinaisonDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.SearchedObjectIdNotExistException;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.export.CombinaisonManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.io.export.CombinaisonValidator;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Combinaison;

/**
 *
 * Implémentation du manager du bean de domaine Combinaison.
 * Classe créée le 02/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class CombinaisonManagerImpl implements CombinaisonManager
{

   private final Log log = LogFactory.getLog(CombinaisonManager.class);

   /** Bean Dao CombinaisonDao. */
   private CombinaisonDao combinaisonDao = null;

   /** Bean Dao ChampDao. */
   private ChampDao champDao = null;

   /** Bean Dao ChampManager. */
   private ChampManager champManager = null;

   /** Bean Validator. */
   private CombinaisonValidator combinaisonValidator = null;

   public CombinaisonManagerImpl(){
      super();
   }

   /**
    * Setter du bean CombinaisonDao.
    * @param combDao est le bean Dao.
    */
   public void setCombinaisonDao(final CombinaisonDao combDao){
      this.combinaisonDao = combDao;
   }

   /**
    * Setter du bean ChampDao.
    * @param chDao est le bean Dao.
    */
   public void setChampDao(final ChampDao chDao){
      this.champDao = chDao;
   }

   /**
    * Setter du bean ChampManager.
    * @param chManager est le bean Manager.
    */
   public void setChampManager(final ChampManager chManager){
      this.champManager = chManager;
   }

   /**
    * Setter du bean CombinaisonValidator.
    * @param combinaisonValidator est le bean Validator.
    */
   public void setCombinaisonValidator(final CombinaisonValidator validator){
      this.combinaisonValidator = validator;
   }

   /**
    * Copie une combinaison en BDD.
    * @param combinaison Combinaison à copier.
    * @return la Combinaison copiée
    * @throws RequiredObjectIsNullException la Combinaison passée en paramètre
    * doit être valide.
    */
   @Override
   public Combinaison copyCombinaisonManager(final Combinaison combinaison){
      Combinaison temp = null;
      //On verifie que la combinaison n'est pas nulle
      if(combinaison == null){
         log.warn("Objet obligatoire Combinaison manquant lors " + "de la copie d'un objet Combinaison");
         throw new RequiredObjectIsNullException("Combinaison", "copie", "Combinaison");
      }
      //On copie le champ1
      Champ champ1 = null;
      if(combinaison.getChamp1() != null){
         champ1 = champManager.copyChampManager(combinaison.getChamp1());
      }
      Champ champ2 = null;
      //On copie le champ2
      if(combinaison.getChamp2() != null){
         champ2 = champManager.copyChampManager(combinaison.getChamp2());
      }
      temp = new Combinaison(champ1, combinaison.getOperateur(), champ2);
      createObjectManager(temp, temp.getChamp1(), temp.getChamp2());
      return temp;
   }

   /**
    * Créé une nouvelle Combinaison en BDD.
    * @param combinaison Combinaison à créer.
    * @param champ1 Premier champ de la Combinaison.
    * @param champ2 Deuxième champ de la Combinaison.
    * @throws RequiredObjectIsNullException aucun des paramètres ne doit pas
    * être null.
    */
   @Override
   public void createObjectManager(final Combinaison combinaison, Champ champ1, Champ champ2){
      //On vérifie que la combinaison n'est pas nulle
      if(combinaison == null){
         log.warn("Objet obligatoire Combinaison manquant lors " + "de la création d'un objet Combinaison");
         throw new RequiredObjectIsNullException("Combinaison", "création", "Combinaison");
      }
      //On vérifie que le champ1 n'est pas nul
      if(champ1 == null){
         log.warn("Objet obligatoire Champ1 manquant lors " + "de la création d'un objet Combinaison");
         throw new RequiredObjectIsNullException("Combinaison", "création", "Champ1");
      }
      //On vérifie que le champ2 n'est pas nul
      if(champ2 == null){
         log.warn("Objet obligatoire Champ2 manquant lors " + "de la création d'un objet Combinaison");
         throw new RequiredObjectIsNullException("Combinaison", "création", "Champ2");
      }
      if(champ1.getChampId() != null){
         champ1 = champDao.mergeObject(champ1);
      }else{
         champManager.createObjectManager(champ1, champ1.getChampParent());
      }
      combinaison.setChamp1(champ1);
      if(champ2.getChampId() != null){
         champ2 = champDao.mergeObject(champ2);
      }else{
         champManager.createObjectManager(champ2, champ2.getChampParent());
      }
      combinaison.setChamp2(champ2);
      BeanValidator.validateObject(combinaison, new Validator[] {combinaisonValidator});
      combinaisonDao.createObject(combinaison);
   }

   /**
    * Met à jour une Combinaison en BDD.
    * @param combinaison Combinaison à mettre à jour.
    * @param champ1 Premier champ de la Combinaison.
    * @param champ2 Deuxième champ de la Combinaison.
    * @throws RequiredObjectIsNullException aucun des paramètres ne doit pas
    * être null.
    */
   @Override
   public void updateObjectManager(final Combinaison combinaison, Champ champ1, Champ champ2){
      //On vérifie que la combinaison n'est pas nulle
      if(combinaison == null){
         log.warn("Objet obligatoire Combinaison manquant lors " + "de la modification d'un objet Combinaison");
         throw new RequiredObjectIsNullException("Combinaison", "modification", "Combinaison");
      }
      //On vérifie que le champ1 n'est pas nul
      if(champ1 == null){
         log.warn("Objet obligatoire Champ1 manquant lors " + "de la modification d'un objet Combinaison");
         throw new RequiredObjectIsNullException("Combinaison", "modification", "Champ1");
      }
      //On vérifie que le champ2 n'est pas nul
      if(champ2 == null){
         log.warn("Objet obligatoire Champ2 manquant lors " + "de la modification d'un objet Combinaison");
         throw new RequiredObjectIsNullException("Combinaison", "modification", "Champ2");
      }
      /*if (combinaison.getChamp1() != null && champ1 != null
      		&& !combinaison.getChamp1().getChampId().equals(
      				champ1.getChampId())) {
      	champManager.removeObjectManager(combinaison.getChamp1());
      }*/
      if(champ1.getChampId() != null){
         champ1 = champDao.mergeObject(champ1);
      }else{
         champManager.createObjectManager(champ1, champ1.getChampParent());
      }
      final Champ oldChamp1 = combinaison.getChamp1();
      combinaison.setChamp1(champ1);
      /*if (combinaison.getChamp2() != null && champ2 != null
      		&& !combinaison.getChamp2().getChampId().equals(
      				champ2.getChampId())) {
      	champManager.removeObjectManager(combinaison.getChamp2());
      }*/
      if(champ2.getChampId() != null){
         champ2 = champDao.mergeObject(champ2);
      }else{
         champManager.createObjectManager(champ2, champ2.getChampParent());
      }
      final Champ oldChamp2 = combinaison.getChamp2();
      combinaison.setChamp2(champ2);
      BeanValidator.validateObject(combinaison, new Validator[] {combinaisonValidator});
      combinaisonDao.updateObject(combinaison);

      if(oldChamp1 != null && oldChamp1.getChampId() != null && !oldChamp1.equals(combinaison.getChamp1())){
         champManager.removeObjectManager(oldChamp1);
      }
      if(oldChamp2 != null && oldChamp2.getChampId() != null && !oldChamp2.equals(combinaison.getChamp2())){
         champManager.removeObjectManager(oldChamp2);
      }

   }

   /**
    * Supprime une Combinaison en BDD.
    * @param combinaison Combinaison à supprimer.
    * @throws RequiredObjectIsNullException la Combinaison passée en paramètre
    * ne doit pas être null.
    */
   @Override
   public void removeObjectManager(final Combinaison combinaison){
      //On vérifie que la combinaison n'est pas nulle
      if(combinaison == null){
         log.warn("Objet obligatoire Combinaison manquant lors " + "de la suppression d'un objet Combinaison");
         throw new RequiredObjectIsNullException("Combinaison", "suppression", "Combinaison");
      }
      //On vérifie que la combinaison est en BDD
      if(findByIdManager(combinaison.getCombinaisonId()) == null){
         throw new SearchedObjectIdNotExistException("Combinaison", combinaison.getCombinaisonId());
      }
      combinaisonDao.removeObject(combinaison.getCombinaisonId());
   }

   /**
    * Recherche une Combinaison dont l'identifiant est passé en paramètre.
    * @param idCombinaison Identifiant de la Combinaison que l'on recherche.
    * @return une Combinaison.
    * @throws RequiredObjectIsNullException l'identifiant passé en paramètre ne
    *  doit pas être null.
    */
   @Override
   public Combinaison findByIdManager(final Integer idCombinaison){
      //On vérifie que l'identifiant n'est pas nul
      if(idCombinaison == null){
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet Combinaison");
         throw new RequiredObjectIsNullException("Combinaison", "recherche par identifiant", "identifiant");
      }
      return combinaisonDao.findById(idCombinaison);
   }

   /**
    * Recherche toutes les Combinaisons présentes dans la BDD.
    * @return Liste d'Affichages.
    */
   @Override
   public List<Combinaison> findAllObjectsManager(){
      return combinaisonDao.findAll();
   }
}
