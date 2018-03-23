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

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteTypeDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.stockage.EnceinteTypeManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.stockage.EnceinteTypeValidator;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.EnceinteType;

/**
 *
 * Implémentation du manager du bean de domaine EnceinteType.
 * Interface créée le 17/03/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class EnceinteTypeManagerImpl implements EnceinteTypeManager
{

   /** Bean Dao EnceinteTypeDao. */
   private EnceinteTypeDao enceinteTypeDao;
   private EnceinteTypeValidator enceinteTypeValidator;
   private final Log log = LogFactory.getLog(EnceinteTypeManager.class);

   private PlateformeDao plateformeDao;

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Setter du bean EnceinteTypeDao.
    * @param eDao est le bean Dao.
    */
   public void setEnceinteTypeDao(final EnceinteTypeDao eDao){
      this.enceinteTypeDao = eDao;
   }

   public void setEnceinteTypeValidator(final EnceinteTypeValidator eValidator){
      this.enceinteTypeValidator = eValidator;
   }

   @Override
   public EnceinteType findByIdManager(final Integer enceinteTypeId){
      return enceinteTypeDao.findById(enceinteTypeId);
   }

   //	@Override
   //	public List<EnceinteType> findAllObjectsManager() {
   //		return enceinteTypeDao.findByOrder();
   //	}

   @Override
   public List<EnceinteType> findAllObjectsExceptBoiteManager(final Plateforme pf){
      return enceinteTypeDao.findByOrderExceptBoite(pf);
   }

   @Override
   public boolean findDoublonManager(final Object o){
      if(o != null){
         final EnceinteType type = (EnceinteType) o;
         if(type.getEnceinteTypeId() == null){
            return enceinteTypeDao.findAll().contains(type);
         }else{
            return enceinteTypeDao.findByExcludedId(type.getEnceinteTypeId()).contains(type);
         }
      }else{
         return false;
      }
   }

   @Override
   public boolean isUsedObjectManager(final Object obj){
      final EnceinteType type = enceinteTypeDao.mergeObject((EnceinteType) obj);
      return type.getEnceintes().size() > 0;
   }

   @Override
   public void createObjectManager(final Object obj){
      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(((TKThesaurusObject) obj).getPlateforme() == null){
         throw new RequiredObjectIsNullException("Nature", "creation", "Plateforme");
      }else{
         ((TKThesaurusObject) obj).setPlateforme(plateformeDao.mergeObject(((TKThesaurusObject) obj).getPlateforme()));
      }
      BeanValidator.validateObject(obj, new Validator[] {enceinteTypeValidator});
      if(!findDoublonManager(obj)){
         enceinteTypeDao.createObject((EnceinteType) obj);
         log.info("Enregistrement objet EnceinteType " + obj.toString());
      }else{
         log.warn("Doublon lors creation objet EnceinteType " + obj.toString());
         throw new DoublonFoundException("EnceinteType", "creation");
      }
   }

   @Override
   public void updateObjectManager(final Object obj){
      BeanValidator.validateObject(obj, new Validator[] {enceinteTypeValidator});
      if(!findDoublonManager(obj)){
         enceinteTypeDao.updateObject((EnceinteType) obj);
         log.info("Modification objet EnceinteType " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet EnceinteType " + obj.toString());
         throw new DoublonFoundException("EnceinteType", "modification");
      }
   }

   @Override
   public void removeObjectManager(final Object obj){
      if(obj != null){
         if(!isUsedObjectManager(obj)){
            enceinteTypeDao.removeObject(((EnceinteType) obj).getEnceinteTypeId());
            log.info("Suppression objet EnceinteType " + obj.toString());
         }else{
            log.warn("Suppression objet EnceinteType " + obj.toString() + " impossible car est reference " + "(par Enceinte)");
            throw new ObjectUsedException();
         }
      }else{
         log.warn("Suppression d'une EnceinteType null");
      }
   }

   @Override
   public List<? extends TKThesaurusObject> findByOrderManager(final Plateforme pf){
      return enceinteTypeDao.findByOrder(pf);
   }

   @Override
   public List<EnceinteType> findByTypeManager(final String type, final Plateforme pf){
      if(type != null && pf != null){
         return enceinteTypeDao.findByTypeAndPf(type, pf);
      }else{
         return new ArrayList<>();
      }
   }
}
