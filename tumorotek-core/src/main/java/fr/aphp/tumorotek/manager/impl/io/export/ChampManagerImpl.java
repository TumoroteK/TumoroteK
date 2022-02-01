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
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.SearchedObjectIdNotExistException;
import fr.aphp.tumorotek.manager.io.ChampDelegueManager;
import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.export.GroupementManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKDelegetableObject;
import fr.aphp.tumorotek.model.io.export.Champ;

/**
 *
 * Implémentation du manager du bean de domaine Champ. Classe créée le
 * 05/05/10.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class ChampManagerImpl implements ChampManager
{

   private final Log log = LogFactory.getLog(GroupementManager.class);

   /** Bean Dao ChampDao. */
   private ChampDao champDao = null;
   
   private ChampEntiteManager champEntiteManager;
   private ChampDelegueManager champDelegueManager;
   private AnnotationValeurManager annotationValeurManager;
   
   public ChampManagerImpl(){
      super();
   }

   /**
    * Setter du bean ChampDao. 
    * @param cDao
    *            est le bean Dao.
    */
   public void setChampDao(final ChampDao cDao){
      this.champDao = cDao;
   }

   /**
    * Créé un Champ en BDD.
    * 
    * @param champ
    *            Champ à créer.
    * @param parent
    *            Champ parent du champ à créer.
    */
   @Override
   public void createObjectManager(final Champ champ, Champ parent){
      // On vérifie que le groupement n'est pas nul
      if(champ == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la création d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "création", "Champ");
      }
      // On enregsitre d'abord son parent
      if(null != parent){
         createObjectManager(parent);
      }

      champ.setChampParent(parent);
      champDao.save(champ);
   }

   /**
    * Créé un Champ en BDD.
    * 
    * @param champ Champ à créer.
    */
   @Override
   public void createObjectManager(final Champ champ){
      // On vérifie que le groupement n'est pas nul
      if(champ == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la création d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "création", "Champ");
      }
      // On enregsitre d'abord son parent
      if(null != champ.getChampParent()){
         createObjectManager(champ.getChampParent());
      }

      champDao.save(champ);
   }


   /**
    * Met à jour un Champ en BDD.
    * 
    * @param champ
    *            Champ à créer.
    * @param parent
    *            Champ parent du champ à mettre à jour.
    */
   @Override
   public void updateObjectManager(final Champ champ, Champ parent){
      //On vérifie que le groupement n'est pas nul
      if(champ == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la modification d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "modification", "Champ");
      }
      //On met à jour le parent d'abord
      Champ oldChampParent = champDao.findById(champ.getChampId()).orElse(new Champ()).getChampParent();
      updateParent(parent, oldChampParent);

      champ.setChampParent(parent);
      champDao.save(champ);
   }

   /**
    * Met à jour un Champ en BDD.
    * 
    * @param champ Champ à mettre à jour.
    */
   @Override
   public void updateObjectManager(final Champ champ){
      //On vérifie que le groupement n'est pas nul
      if(champ == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la modification d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "modification", "Champ");
      }
      //On met à jour le parent d'abord
      Champ oldChampParent = champDao.findById(champ.getChampId()).orElse(new Champ()).getChampParent();
      updateParent(champ.getChampParent(), oldChampParent);

      champDao.save(champ);
   }

   /**
    * Met à jour un champ parent associé au champ
    * @param newChampParent l'ancien champ
    * @param oldChampParent le nouveau champ
    */
   private void updateParent(Champ newChampParent, Champ oldChampParent){
      if(null == newChampParent && null != oldChampParent){ // Suppression du champ
         removeObjectManager(oldChampParent);
      }else if(null != newChampParent && null == oldChampParent){ // Nouveau champ
         createObjectManager(newChampParent, newChampParent.getChampParent());
      }else if(null != newChampParent && null != oldChampParent){ // Mise à jour Champ
         newChampParent.setChampId(oldChampParent.getChampId());
         updateObjectManager(newChampParent, newChampParent.getChampParent());
      }
   }

   /**
    * Supprime un Champ et son parent d'abord.
    * 
    * @param groupement
    *            Champ à supprimer.
    */
   @Override
   public void removeObjectManager(final Champ champ){
      // On vérifie que le champ n'est pas nul
      if(champ == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la suppression d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "suppression", "Champ");
      }
      // On vérifie que le champ est en BDD
      if(findByIdManager(champ.getChampId()) == null){
         throw new SearchedObjectIdNotExistException("Champ", champ.getChampId());
      }
      // On supprime d'abord son parent
      if(null != champ.getChampParent()){
         removeObjectManager(champ.getChampParent());
      }

      // On supprime le champ
      champDao.deleteById(champ.getChampId());
   }

   /**
    * Copie un Champ en BDD.
    * 
    * @param champ
    *            Champ à copier.
    */
   @Override
   public Champ copyChampManager(final Champ champ){
      // On vérifie que le groupement n'est pas nul
      if(champ == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la copie d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "copie", "Champ");
      }
      // On enregistre d'abord son parent
      final Champ copy = champ.copy();
      Champ parent = copy.getChampParent();
      if(parent != null){
         if(parent.getChampId() != null){
            parent = champDao.save(parent);
         }else{
            parent = copyChampManager(parent);
         }
      }
      copy.setChampParent(parent);
      champDao.save(copy);

      return copy;
   }

   /**
    * Chercher les Champs enfants du Champ passé en paramètre.
    * 
    * @param groupement
    *            Champ dont on souhaite obtenir la liste d'enfants.
    * @return liste des enfants (Champs) d'un Champ.
    */
   @Override
   public ArrayList<Champ> findEnfantsManager(final Champ champ){
      // On vérifie que le groupement n'est pas nul
      if(champ == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la recherche d'enfants d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "recherche d'enfants", "Champ");
      }
      return champDao.findEnfants(champ);
   }

   /**
    * Recherche un Champ dont l'identifiant est passé en paramètre.
    * 
    * @param champId
    *            Identifiant du Champ que l'on recherche.
    * @return un Champ.
    */
   @Override
   public Champ findByIdManager(final Integer id){
      // On vérifie que l'identifiant n'est pas nul
      if(id == null){
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "recherche par identifiant", "identifiant");
      }
      return champDao.findById(id).orElse(null);
   }

   /**
    * Recherche tous les Champs présents dans la BDD.
    * 
    * @return Liste de Champs.
    */
   @Override
   public List<Champ> findAllObjectsManager(){
      return IterableUtils.toList(champDao.findAll());
   }

   @Override
   public <T> Object getValueForObjectManager(final Champ champ, final T obj, final boolean prettyFormat){

      Object res = null;
      
      if(champ != null && obj != null){
         // si le champ est bien un champ interne à TK
         if(null != champ.getChampEntite()){
            res = champEntiteManager.getValueForObjectManager(champ.getChampEntite(), obj, prettyFormat);
         }
         else if(null != champ.getChampAnnotation() && obj instanceof TKAnnotableObject) {
            res = annotationValeurManager.findByChampAndObjetManager(champ.getChampAnnotation(), (TKAnnotableObject)obj).get(0);
         }
         else if( null != champ.getChampDelegue() && obj instanceof TKDelegetableObject) {
            res = champDelegueManager.getValueForEntite(champ.getChampDelegue(), (TKDelegetableObject<T>) obj);
         }
      }
      
      return res;
      
   }

   public void setChampEntiteManager(ChampEntiteManager champEntiteManager){
      this.champEntiteManager = champEntiteManager;
   }

   public void setChampDelegueManager(ChampDelegueManager champDelegueManager){
      this.champDelegueManager = champDelegueManager;
   }

   public void setAnnotationValeurManager(AnnotationValeurManager annotationValeurManager){
      this.annotationValeurManager = annotationValeurManager;
   }

}
