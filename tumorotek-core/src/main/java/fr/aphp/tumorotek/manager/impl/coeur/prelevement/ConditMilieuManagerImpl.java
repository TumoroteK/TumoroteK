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
package fr.aphp.tumorotek.manager.impl.coeur.prelevement;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.prelevement.ConditMilieuDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.ConditMilieuManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.prelevement.ConditMilieuValidator;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine ConditMilieu.
 * Classe créée le 05/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class ConditMilieuManagerImpl implements ConditMilieuManager
{

   private final Log log = LogFactory.getLog(ConditMilieuManager.class);

   /* Beans injectes par Spring*/
   private ConditMilieuDao conditMilieuDao;
   private ConditMilieuValidator conditMilieuValidator;
   private PlateformeDao plateformeDao;

   public ConditMilieuManagerImpl(){}

   /* Properties setters */
   public void setConditMilieuDao(final ConditMilieuDao cmDao){
      this.conditMilieuDao = cmDao;
   }

   public void setConditMilieuValidator(final ConditMilieuValidator cmValidator){
      this.conditMilieuValidator = cmValidator;
   }

   public void setPlateformeDao(final PlateformeDao pfDao){
      this.plateformeDao = pfDao;
   }

   @Override
   public void createObjectManager(final Object obj){

      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(((TKThesaurusObject) obj).getPlateforme() == null){
         throw new RequiredObjectIsNullException("ConditMilieu", "creation", "Plateforme");
      }else{
         ((TKThesaurusObject) obj).setPlateforme(plateformeDao.mergeObject(((TKThesaurusObject) obj).getPlateforme()));
      }

      BeanValidator.validateObject(obj, new Validator[] {conditMilieuValidator});
      if(!findDoublonManager(obj)){
         conditMilieuDao.createObject((ConditMilieu) obj);
         log.info("Enregistrement objet ConditMilieu " + obj.toString());
      }else{
         log.warn("Doublon lors creation objet ConditMilieu " + obj.toString());
         throw new DoublonFoundException("ConditMilieu", "creation");
      }
   }

   @Override
   public void updateObjectManager(final Object obj){
      BeanValidator.validateObject(obj, new Validator[] {conditMilieuValidator});
      if(!findDoublonManager(obj)){
         conditMilieuDao.updateObject((ConditMilieu) obj);
         log.info("Modification objet ConditMilieu " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet ConditMilieu " + obj.toString());
         throw new DoublonFoundException("ConditMilieu", "modification");
      }
   }

   //	@Override
   //	public List<ConditMilieu> findAllObjectsManager() {
   //		log.info("Recherche totalite des ConditMilieu");
   //		return conditMilieuDao.findByOrder();
   //	}

   @Override
   public List<ConditMilieu> findByMilieuLikeManager(String milieu, final boolean exactMatch){
      if(!exactMatch){
         milieu = milieu + "%";
      }
      log.debug("Recherche ConditMilieu par milieu: " + milieu + " exactMatch " + String.valueOf(exactMatch));
      return conditMilieuDao.findByMilieu(milieu);
   }

   @Override
   public void removeObjectManager(final Object obj){
      if(obj != null){
         //			if (!isUsedObjectManager(obj)) {
         conditMilieuDao.removeObject(((ConditMilieu) obj).getConditMilieuId());
         log.info("Suppression objet ConditMilieu " + obj.toString());
         //			} else {
         //				log.warn("Suppression objet ConditMilieu " + obj.toString()
         //						+ " impossible car est reference (par Prelevement)");
         //				throw new ObjectUsedException();
         //			}
      }else{
         log.warn("Suppression d'un ConditMilieu null");
      }
   }

   @Override
   public boolean findDoublonManager(final Object o){
      if(o != null){
         final ConditMilieu milieu = (ConditMilieu) o;
         if(milieu.getConditMilieuId() == null){
            return conditMilieuDao.findAll().contains(milieu);
         }else{
            return conditMilieuDao.findByExcludedId(milieu.getConditMilieuId()).contains(milieu);
         }
      }else{
         return false;
      }
   }

   @Override
   public boolean isUsedObjectManager(final Object o){
      final ConditMilieu conditMilieu = conditMilieuDao.mergeObject((ConditMilieu) o);
      return conditMilieu.getPrelevements().size() > 0;
   }

   @Override
   public List<? extends TKThesaurusObject> findByOrderManager(final Plateforme pf){
      return conditMilieuDao.findByOrder(pf);
   }

   @Override
   public TKThesaurusObject findByIdManager(final Integer id){
      return conditMilieuDao.findById(id);
   }

}
