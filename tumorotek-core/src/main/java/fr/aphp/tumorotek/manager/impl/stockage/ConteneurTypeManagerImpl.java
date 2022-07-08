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
package fr.aphp.tumorotek.manager.impl.stockage;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurTypeDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.stockage.ConteneurTypeManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.stockage.ConteneurTypeValidator;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.ConteneurType;

/**
 *
 * Implémentation du manager du bean de domaine ConteneurType.
 * Interface créée le 17/03/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ConteneurTypeManagerImpl implements ConteneurTypeManager
{

   private final Log log = LogFactory.getLog(ConteneurTypeManager.class);

   /** Bean Dao ConteneurTypeDao. */
   private ConteneurTypeDao conteneurTypeDao;

   private ConteneurTypeValidator conteneurTypeValidator;

   private PlateformeDao plateformeDao;

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Setter du bean ConteneurTypeDao.
    * @param cDao est le bean cDao.
    */
   public void setConteneurTypeDao(final ConteneurTypeDao cDao){
      this.conteneurTypeDao = cDao;
   }

   public void setConteneurTypeValidator(final ConteneurTypeValidator cValidator){
      this.conteneurTypeValidator = cValidator;
   }

   @Override
   public ConteneurType findByIdManager(final Integer conteneurTypeId){
      return conteneurTypeDao.findById(conteneurTypeId);
   }

   @Override
   public boolean findDoublonManager(final ConteneurType o){
      if(o != null){
         final ConteneurType type = o;
         if(type.getId() == null){
            return conteneurTypeDao.findAll().contains(type);
         }
         return conteneurTypeDao.findByExcludedId(type.getId()).contains(type);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final ConteneurType obj){
      final ConteneurType type = conteneurTypeDao.mergeObject(obj);
      return type.getConteneurs().size() > 0;
   }

   @Override
   public void createObjectManager(final ConteneurType obj){
      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(obj.getPlateforme() == null){
         throw new RequiredObjectIsNullException("Nature", "creation", "Plateforme");
      }
      obj.setPlateforme(plateformeDao.mergeObject(obj.getPlateforme()));
      BeanValidator.validateObject(obj, new Validator[] {conteneurTypeValidator});
      if(!findDoublonManager(obj)){
         conteneurTypeDao.createObject(obj);
         log.info("Enregistrement objet ConteneurType " + obj.toString());
      }else{
         log.warn("Doublon lors creation objet ConteneurType " + obj.toString());
         throw new DoublonFoundException("ConteneurType", "creation");
      }
   }

   @Override
   public void updateObjectManager(final ConteneurType obj){
      BeanValidator.validateObject(obj, new Validator[] {conteneurTypeValidator});
      if(!findDoublonManager(obj)){
         conteneurTypeDao.updateObject(obj);
         log.info("Modification objet ConteneurType " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet ConteneurType " + obj.toString());
         throw new DoublonFoundException("ConteneurType", "modification");
      }
   }

   @Override
   public void removeObjectManager(final ConteneurType obj){
      if(obj != null){
         conteneurTypeDao.removeObject(obj.getId());
         log.info("Suppression objet ConteneurType " + obj.toString());
      }else{
         log.warn("Suppression d'un ConteneurType null");
      }
   }

   @Override
   public List<ConteneurType> findByOrderManager(final Plateforme pf){
      return conteneurTypeDao.findByPfOrder(pf);
   }

   @Override
   public List<ConteneurType> findByOrderManager(){
      return conteneurTypeDao.findByOrder();
   }
}
