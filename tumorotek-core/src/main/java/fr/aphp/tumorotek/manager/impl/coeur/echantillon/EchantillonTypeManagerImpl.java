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
package fr.aphp.tumorotek.manager.impl.coeur.echantillon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonTypeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonTypeManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.echantillon.EchantillonTypeValidator;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine EchantillonType.
 * Classe créée le 23/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class EchantillonTypeManagerImpl implements EchantillonTypeManager
{

   private final Log log = LogFactory.getLog(EchantillonTypeManager.class);

   /** Bean Dao EchantillonTypeDao. */
   private EchantillonTypeDao echantillonTypeDao;
   /** Bean Dao EchantillonDao. */
   private EchantillonDao echantillonDao;
   /** Bean Validator. */
   private EchantillonTypeValidator echantillonTypeValidator;
   private PlateformeDao plateformeDao;

   /**
    * Setter du bean EchantillonTypeDao.
    * @param eDao est le bean Dao.
    */
   public void setEchantillonTypeDao(final EchantillonTypeDao eDao){
      this.echantillonTypeDao = eDao;
   }

   /**
    * Setter du bean EchantillonDao.
    * @param eDao est le bean Dao.
    */
   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setEchantillonTypeValidator(final EchantillonTypeValidator validator){
      this.echantillonTypeValidator = validator;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Recherche un type d'échantillon dont l'identifiant est 
    * passé en paramètre.
    * @param echantillonTypeId Identifiant du type que l'on recherche.
    * @return Un EchantillonType.
    */
   @Override
   public EchantillonType findByIdManager(final Integer echantillonTypeId){
      return echantillonTypeDao.findById(echantillonTypeId);
   }

   //	/**
   //	 * Recherche tous les types d'échantillons présents dans la base.
   //	 * @return Liste de EchantillonType.
   //	 */
   //	public List<EchantillonType> findAllObjectsManager() {
   //		log.info("Recherche de tous les EchantillonTypes");
   //		return echantillonTypeDao.findByOrder();
   //	}

   /**
    * Recherche le type de l'échantillon passé en paramètre.
    * @param echantillon Echantillon pour lequel on recherche un type.
    * @return Un EchantillonType.
    */
   @Override
   public EchantillonType findByEchantillonManager(final Echantillon echantillon){
      final List<EchantillonType> types = echantillonTypeDao.findByEchantillonId(echantillon.getEchantillonId());

      if(types.size() == 0){
         return null;
      }
         return types.get(0);
   }

   /**
    * Recherche les échantillons liés au type passé en paramètre.
    * @param type Type des échantillons que l'on recherche.
    * @return Liste d'échantillons.
    */
   @Override
   public Set<Echantillon> getEchantillonsManager(EchantillonType type){
      type = echantillonTypeDao.mergeObject(type);
      final Set<Echantillon> echans = type.getEchantillons();
      echans.size();
      return echans;
   }

   /**
    * Recherche tous les types d'échantillons dont la valeur commence
    * comme celle passée en paramètre.
    * @param type Type que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de EchantillonType.
    */
   @Override
   public List<EchantillonType> findByTypeLikeManager(String type, final boolean exactMatch){
      log.debug("Recherche EchantillonType par " + type + " exactMatch " + String.valueOf(exactMatch));
      if(type != null){
         if(!exactMatch){
            type = type + "%";
         }
         return echantillonTypeDao.findByType(type);
      }
      return new ArrayList<>();
   }

   @Override
   public boolean findDoublonManager(final EchantillonType obj){
      final EchantillonType type = obj;
      if(type != null){
         if(type.getId() == null){
            return echantillonTypeDao.findAll().contains(type);
         }
         return echantillonTypeDao.findByExcludedId(type.getId()).contains(type);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final EchantillonType obj){
      final List<Echantillon> echans = echantillonDao.findByEchantillonType(obj);

      return (echans.size() > 0);
   }

   @Override
   public void createObjectManager(final EchantillonType obj){

      final EchantillonType type = obj;

      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(type.getPlateforme() == null){
         throw new RequiredObjectIsNullException("EchantillonType", "creation", "Plateforme");
      }
      type.setPlateforme(plateformeDao.mergeObject(type.getPlateforme()));

      if(findDoublonManager(type)){
         log.warn("Doublon lors de la creation de l'objet " + "EchantillonType : " + type.toString());
         throw new DoublonFoundException("EchantillonType", "creation");
      }
      BeanValidator.validateObject(type, new Validator[] {echantillonTypeValidator});
      echantillonTypeDao.createObject(type);
      log.info("Enregistrement de l'objet EchantillonType : " + type.toString());
   }

   @Override
   public void updateObjectManager(final EchantillonType obj){

      final EchantillonType type = obj;

      if(findDoublonManager(type)){
         log.warn("Doublon lors de la modification de l'objet " + "EchantillonType : " + type.toString());
         throw new DoublonFoundException("EchantillonType", "modification");
      }
      BeanValidator.validateObject(type, new Validator[] {echantillonTypeValidator});
      echantillonTypeDao.updateObject(type);
      log.info("Modification de l'objet EchantillonType : " + type.toString());
   }

   @Override
   public void removeObjectManager(final EchantillonType obj){
      final EchantillonType type = obj;
      if(isUsedObjectManager(type)){
         log.warn("Objet utilisé lors de la suppression de l'objet " + "EchantillonType : " + type.toString());
         throw new ObjectUsedException("EchantillonType", "suppression");
      }
      echantillonTypeDao.removeObject(type.getId());
      log.info("Suppression de l'objet EchantillonType : " + type.toString());
   }

   @Override
   public List<EchantillonType> findByOrderManager(final Plateforme pf){
      return echantillonTypeDao.findByPfOrder(pf);
   }

   @Override
   public List<EchantillonType> findByOrderManager(){
      return echantillonTypeDao.findByOrder();
   }
}
