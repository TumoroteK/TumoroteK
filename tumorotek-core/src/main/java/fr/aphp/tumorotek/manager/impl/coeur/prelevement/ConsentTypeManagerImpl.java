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

import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.ConsentTypeManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.prelevement.ConsentTypeValidator;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine ConsentType.
 * Classe créée le 05/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class ConsentTypeManagerImpl implements ConsentTypeManager
{

   private final Log log = LogFactory.getLog(ConsentTypeManager.class);

   /* Beans injectes par Spring*/
   private ConsentTypeDao consentTypeDao;
   private ConsentTypeValidator consentTypeValidator;
   private PlateformeDao plateformeDao;

   public ConsentTypeManagerImpl(){}

   /* Properties setters */
   public void setConsentTypeDao(final ConsentTypeDao cDao){
      this.consentTypeDao = cDao;
   }

   public void setConsentTypeValidator(final ConsentTypeValidator ctValidator){
      this.consentTypeValidator = ctValidator;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   @Override
   public void createObjectManager(final ConsentType obj){

      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(obj.getPlateforme() == null){
         throw new RequiredObjectIsNullException("ConsentType", "creation", "Plateforme");
      }
      obj.setPlateforme(plateformeDao.mergeObject(obj.getPlateforme()));

      BeanValidator.validateObject(obj, new Validator[] {consentTypeValidator});
      if(!findDoublonManager(obj)){
         consentTypeDao.createObject(obj);
         log.info("Enregistrement objet ConsentType " + obj.toString());
      }else{
         log.warn("Doublon lors creation objet ConsentType " + obj.toString());
         throw new DoublonFoundException("ConsentType", "creation");
      }
   }

   @Override
   public void updateObjectManager(final ConsentType obj){
      BeanValidator.validateObject(obj, new Validator[] {consentTypeValidator});
      if(!findDoublonManager(obj)){
         consentTypeDao.updateObject(obj);
         log.info("Modification objet ConsentType " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet ConsentType " + obj.toString());
         throw new DoublonFoundException("ConsentType", "modification");
      }
   }

   @Override
   public List<ConsentType> findByTypeLikeManager(String type, final boolean exactMatch){
      if(!exactMatch){
         type = type + "%";
      }
      log.debug("Recherche ConsentType par type: " + type + " exactMatch " + String.valueOf(exactMatch));
      return consentTypeDao.findByType(type);
   }

   @Override
   public void removeObjectManager(final ConsentType obj){
      if(obj != null){
         if(!isUsedObjectManager(obj)){
            consentTypeDao.removeObject(obj.getId());
            log.info("Suppression objet ConsentType " + obj.toString());
         }else{
            log.warn("Suppression objet ConsentType " + obj.toString() + " impossible car est reference (par Prelevement)");
            throw new ObjectUsedException();
         }
      }else{
         log.warn("Suppression d'un ConsentType null");
      }
   }

   @Override
   public boolean findDoublonManager(final ConsentType o){
      if(o != null){
         final ConsentType type = o;
         if(type.getId() == null){
            return consentTypeDao.findAll().contains(type);
         }
         return consentTypeDao.findByExcludedId(type.getId()).contains(type);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final ConsentType o){
      final ConsentType consentType = consentTypeDao.mergeObject(o);
      return consentType.getPrelevements().size() > 0;
   }

   @Override
   public List<ConsentType> findByOrderManager(final Plateforme pf){
      return consentTypeDao.findByPfOrder(pf);
   }

   @Override
   public ConsentType findByIdManager(final Integer id){
      return consentTypeDao.findById(id);
   }

   @Override
   public List<ConsentType> findByOrderManager(){
      return consentTypeDao.findByOrder();
   }

}
