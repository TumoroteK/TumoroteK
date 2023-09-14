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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.stockage.TerminaleTypeDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.stockage.TerminaleTypeManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.stockage.TerminaleTypeValidator;
import fr.aphp.tumorotek.model.stockage.TerminaleType;

/**
 *
 * Implémentation du manager du bean de domaine TerminaleType.
 * Interface créée le 17/03/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class TerminaleTypeManagerImpl implements TerminaleTypeManager
{

   private final Log log = LogFactory.getLog(TerminaleTypeManager.class);

   /** Bean Dao CessionStatutDao. */
   private TerminaleTypeDao terminaleTypeDao;

   private TerminaleTypeValidator terminaleTypeValidator;

   /**
    * Setter du bean TerminaleTypeDao.
    * @param tDao est le bean Dao.
    */
   public void setTerminaleTypeDao(final TerminaleTypeDao tDao){
      this.terminaleTypeDao = tDao;
   }

   public void setTerminaleTypeValidator(final TerminaleTypeValidator tValidator){
      this.terminaleTypeValidator = tValidator;
   }

   @Override
   public TerminaleType findByIdManager(final Integer terminaleTypeId){
      return terminaleTypeDao.findById(terminaleTypeId);
   }

   @Override
   public List<TerminaleType> findAllObjectsManager(){
      return terminaleTypeDao.findByOrder();
   }

   @Override
   public boolean findDoublonManager(final TerminaleType o){
      if(o != null){
         final TerminaleType type = o;
         if(type.getTerminaleTypeId() == null){
            return terminaleTypeDao.findAll().contains(type);
         }
         return terminaleTypeDao.findByExcludedId(type.getTerminaleTypeId()).contains(type);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final TerminaleType obj){
      final TerminaleType type = terminaleTypeDao.mergeObject(obj);
      return type.getTerminales().size() > 0;
   }

   @Override
   public void createObjectManager(final TerminaleType obj){
      BeanValidator.validateObject(obj, new Validator[] {terminaleTypeValidator});
      if(!findDoublonManager(obj)){
         terminaleTypeDao.createObject(obj);
         log.info("Enregistrement objet TerminaleType " + obj.toString());
      }else{
         log.warn("Doublon lors creation objet TerminaleType " + obj.toString());
         throw new DoublonFoundException("TerminaleType", "creation");
      }
   }

   @Override
   public void updateObjectManager(final TerminaleType obj){
      BeanValidator.validateObject(obj, new Validator[] {terminaleTypeValidator});
      if(!findDoublonManager(obj)){
         terminaleTypeDao.updateObject(obj);
         log.info("Modification objet TerminaleType " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet TerminaleType " + obj.toString());
         throw new DoublonFoundException("TerminaleType", "modification");
      }
   }

   @Override
   public void removeObjectManager(final TerminaleType obj){
      if(obj != null){
         if(!isUsedObjectManager(obj)){
            terminaleTypeDao.removeObject(obj.getTerminaleTypeId());
            log.info("Suppression objet TerminaleType " + obj.toString());
         }else{
            log.warn("Suppression objet TerminaleType " + obj.toString() + " impossible car est reference (par Terminale)");
            throw new ObjectUsedException("terminaleType.deletion.isReferenced", false);
         }
      }else{
         log.warn("Suppression d'une TerminaleType null");
      }
   }

   @Override
   public List<TerminaleType> findByTypeManager(final String type){
      if(type != null){
         return terminaleTypeDao.findByType(type);
      }
      return new ArrayList<>();
   }
}
