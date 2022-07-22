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
package fr.aphp.tumorotek.manager.impl.coeur.cession;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.cession.ProtocoleTypeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.cession.ProtocoleTypeManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.cession.ProtocoleTypeValidator;
import fr.aphp.tumorotek.model.cession.ProtocoleType;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine ProtocoleType.
 * Interface créée le 27/01/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ProtocoleTypeManagerImpl implements ProtocoleTypeManager
{

   private final Log log = LogFactory.getLog(ProtocoleTypeManager.class);

   /** Bean Dao CategorieDao. */
   private ProtocoleTypeDao protocoleTypeDao;

   private ProtocoleTypeValidator protocoleTypeValidator;

   private PlateformeDao plateformeDao;

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Setter du bean ProtocoleTypeDao.
    * @param pDao est le bean Dao.
    */
   public void setProtocoleTypeDao(final ProtocoleTypeDao pDao){
      this.protocoleTypeDao = pDao;
   }

   public void setProtocoleTypeValidator(final ProtocoleTypeValidator pValidator){
      this.protocoleTypeValidator = pValidator;
   }

   /**
    * Recherche un ProtocoleType dont l'identifiant est passé en paramètre.
    * @param protocoleTypeId Identifiant du ProtocoleType que l'on recherche.
    * @return Un ProtocoleType.
    */
   @Override
   public ProtocoleType findByIdManager(final Integer protocoleTypeId){
      return protocoleTypeDao.findById(protocoleTypeId);
   }

   /**
    * Recherche tous les ProtocoleType dont le type commence
    * comme celui passé en paramètre.
    * @param type ProtocoleType que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de ProtocoleTypes.
    */
   @Override
   public List<ProtocoleType> findByTypeLikeManager(String type, final boolean exactMatch){

      log.debug("Recherche ProtocoleType par " + type + " exactMatch " + String.valueOf(exactMatch));
      if(type != null){
         if(!exactMatch){
            type = type + "%";
         }
         return protocoleTypeDao.findByType(type);
      }
      return new ArrayList<>();
   }

   @Override
   public boolean findDoublonManager(final ProtocoleType o){
      if(o != null){
         final ProtocoleType type = o;
         if(type.getId() == null){
            return protocoleTypeDao.findAll().contains(type);
         }
         return protocoleTypeDao.findByExcludedId(type.getId()).contains(type);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final ProtocoleType obj){
      final ProtocoleType type = protocoleTypeDao.mergeObject(obj);
      return type.getContrats().size() > 0;
   }

   @Override
   public void createObjectManager(final ProtocoleType obj){
      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(obj.getPlateforme() == null){
         throw new RequiredObjectIsNullException("ProtocoleType", "creation", "Plateforme");
      }
      obj.setPlateforme(plateformeDao.mergeObject(obj.getPlateforme()));
      BeanValidator.validateObject(obj, new Validator[] {protocoleTypeValidator});
      if(!findDoublonManager(obj)){
         protocoleTypeDao.createObject(obj);
         log.info("Enregistrement objet ProtocoleType " + obj.toString());
      }else{
         log.warn("Doublon lors creation objet ProtocoleType " + obj.toString());
         throw new DoublonFoundException("ProtocoleType", "creation");
      }
   }

   @Override
   public void updateObjectManager(final ProtocoleType obj){
      BeanValidator.validateObject(obj, new Validator[] {protocoleTypeValidator});
      if(!findDoublonManager(obj)){
         protocoleTypeDao.updateObject(obj);
         log.info("Modification objet ProtocoleType " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet ProtocoleType " + obj.toString());
         throw new DoublonFoundException("ProtocoleType", "modification");
      }
   }

   @Override
   public void removeObjectManager(final ProtocoleType obj){
      if(obj != null){
         protocoleTypeDao.removeObject(obj.getId());
         log.info("Suppression objet ProtocoleType " + obj.toString());
      }else{
         log.warn("Suppression d'un ProtocoleType null");
      }
   }

   @Override
   public List<ProtocoleType> findByOrderManager(final Plateforme pf){
      return protocoleTypeDao.findByPfOrder(pf);
   }

   @Override
   public List<ProtocoleType> findByOrderManager(){
      return protocoleTypeDao.findByOrder();
   }
}
