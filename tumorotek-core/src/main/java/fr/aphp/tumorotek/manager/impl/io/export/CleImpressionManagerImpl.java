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

import fr.aphp.tumorotek.dao.impression.CleImpressionDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.SearchedObjectIdNotExistException;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.export.CleImpressionManager;
import fr.aphp.tumorotek.manager.io.export.GroupementManager;
import fr.aphp.tumorotek.model.impression.CleImpression;
import fr.aphp.tumorotek.model.io.export.Champ;

/**
 *
 * Implémentation du manager du bean de domaine CleImpression.
 * 
  * Classe créée le 16/01/2018.
 *
 * @author Answald Bournique
 * @version 2.2
 *
 */
public class CleImpressionManagerImpl implements CleImpressionManager
{

   private final Log log = LogFactory.getLog(GroupementManager.class);

   /** Bean Dao CleDeao. */
   private CleImpressionDao cleImpressionDao;

   /** Bean Manager Champ */
   private ChampManager champManager;

   public CleImpressionManagerImpl(){
      super();
   }

   /**
    * Setter du bean CleImpressionDao.
    * @param cDao est le bean Dao.
    */
   public void setCleImpressionDao(final CleImpressionDao cDao){
      this.cleImpressionDao = cDao;
   }

   /**
    * Setter du bean ChampManager.
    * @param champManager est le bean champManager.
    */
   public void setChampManager(final ChampManager champManager){
      this.champManager = champManager;
   }

   @Override
   public void createObjectManager(final CleImpression cleImpression){
      // On vérifie que la clé n'est pas nulle
      if(cleImpression == null){
         log.warn("Objet obligatoire Cle manquant lors " + "de la création d'un objet Cle");
         throw new RequiredObjectIsNullException("Cle", "création", "Cle");
      }
      // On enregsitre d'abord les Champs
      if(null != cleImpression.getChamp()){
         champManager.createObjectManager(cleImpression.getChamp(), cleImpression.getChamp().getChampParent());
      }
      cleImpressionDao.createObject(cleImpression);
   }

   @Override
   public void updateObjectManager(final CleImpression cleImpression){
      //On vérifie que la clé n'est pas nulle
      if(cleImpression == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la modification d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "modification", "Champ");
      }
      //On met à jour le champs
      final Champ oldChamp = cleImpressionDao.findById(cleImpression.getCleId()).getChamp();

      if(null == cleImpression.getChamp() && null != oldChamp){ // Suppression du champ
         champManager.removeObjectManager(oldChamp);
      }else if(null != cleImpression.getChamp() && null == cleImpression.getChamp().getChampId()){ // Nouveau champ
         champManager.createObjectManager(cleImpression.getChamp(), cleImpression.getChamp().getChampParent());
         if(null != oldChamp){
            champManager.removeObjectManager(oldChamp);
         }
      }else if(null != cleImpression.getChamp() && null != oldChamp){ // Mise à jour Champ
         champManager.updateObjectManager(cleImpression.getChamp(), cleImpression.getChamp().getChampParent());
      }

      cleImpressionDao.updateObject(cleImpression);
   }

   @Override
   public void removeObjectManager(final CleImpression cleImpression){
      
      // On vérifie que la clé n'est pas nulle
      if(cleImpression == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la suppression d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "suppression", "Champ");
      }
      // On vérifie que la clé est en BDD
      if(findByIdManager(cleImpression.getCleId()) == null){
         throw new SearchedObjectIdNotExistException("Champ", cleImpression.getCleId());
      }
      // On supprime d'abord les champs
      if(null != cleImpression.getChamp()){
         champManager.removeObjectManager(cleImpression.getChamp());
      }
      // On supprime la clé
      cleImpressionDao.removeObject(cleImpression.getCleId());
   }

   @Override
   public CleImpression findByIdManager(final Integer cleId){
      // On vérifie que l'identifiant n'est pas nul
      if(cleId == null){
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "recherche par identifiant", "identifiant");
      }
      return cleImpressionDao.findById(cleId);
   }

   @Override
   public CleImpression findByNameManager(final String name){
      // On vérifie que l'identifiant n'est pas nul
      if(name.isEmpty()){
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "recherche par identifiant", "identifiant");
      }
      
      List<CleImpression> cleImprList = cleImpressionDao.findByName(name);
      if(!cleImprList.isEmpty()){
         //Boucle pour le traitement du retour SQL qui n'est pas sensible à l'accent. On vérifie donc l'exactitude des noms de clés ici.
         for(CleImpression cle : cleImprList){
            if(cle.getNom().equals(name)){
               return cle;
            }
         }
      }
      
      return null;
   }

   @Override
   public List<CleImpression> findAllObjectsManager(){
      return cleImpressionDao.findAll();
   }

//   @Override
//   public List<CleImpression> findByTemplateManager(final Template template){
//      // On vérifie que l'identifiant n'est pas nul
//      if(null == template){
//         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet Template");
//         throw new RequiredObjectIsNullException("Template", "recherche par identifiant", "identifiant");
//      }
//      return cleImpressionDao.findByTemplate(template);
//   }
}
