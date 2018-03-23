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
package fr.aphp.tumorotek.manager.impl.coeur.prodderive;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdTypeManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.prodderive.ProdTypeValidator;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine ProdType.
 * Classe créée le 29/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ProdTypeManagerImpl implements ProdTypeManager
{

   private final Log log = LogFactory.getLog(ProdTypeManager.class);

   /** Bean Dao ProdQualiteDao. */
   private ProdTypeDao prodTypeDao;
   /** Bean Dao ProdDeriveDao. */
   private ProdDeriveDao prodDeriveDao;
   /** Bean Validator. */
   private ProdTypeValidator prodTypeValidator;
   private PlateformeDao plateformeDao;

   /**
    * Setter du bean ProdTypeDao.
    * @param pDao est le bean Dao.
    */
   public void setProdTypeDao(final ProdTypeDao pDao){
      this.prodTypeDao = pDao;
   }

   /**
    * Setter du bean ProdDeriveDao.
    * @param pDao est le bean Dao.
    */
   public void setProdDeriveDao(final ProdDeriveDao pDao){
      this.prodDeriveDao = pDao;
   }

   public void setProdTypeValidator(final ProdTypeValidator validator){
      this.prodTypeValidator = validator;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Recherche un type de produit dérivé dont l'identifiant est 
    * passé en paramètre.
    * @param prodTypeId Identifiant du type que l'on recherche.
    * @return Un ProdType.
    */
   @Override
   public ProdType findByIdManager(final Integer prodTypeId){
      return prodTypeDao.findById(prodTypeId);
   }

   //	/**
   //	 * Recherche tous les types de produits dérivés présents dans la base.
   //	 * @return Liste de ProdType.
   //	 */
   //	public List<ProdType> findAllObjectsManager() {
   //		log.info("Recherche de tous les ProdTypes");
   //		return prodTypeDao.findByOrder();
   //	}

   /**
    * Recherche tous les types de produits dérivés dont la valeur commence
    * comme celle passée en paramètre.
    * @param type Type que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de ProdType.
    */
   @Override
   public List<ProdType> findByTypeLikeManager(String type, final boolean exactMatch){
      log.debug("Recherche ProdType par " + type + " exactMatch " + String.valueOf(exactMatch));
      if(type != null){
         if(!exactMatch){
            type = type + "%";
         }
         return prodTypeDao.findByType(type);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public boolean findDoublonManager(final Object obj){
      final ProdType type = (ProdType) obj;
      if(type != null){
         if(type.getProdTypeId() == null){
            return prodTypeDao.findAll().contains(type);
         }else{
            return prodTypeDao.findByExcludedId(type.getProdTypeId()).contains(type);
         }
      }else{
         return false;
      }
   }

   @Override
   public boolean isUsedObjectManager(final Object obj){
      final ProdType type = (ProdType) obj;

      final List<ProdDerive> list = prodDeriveDao.findByProdType(type);

      return (list.size() > 0);
   }

   @Override
   public void createObjectManager(final Object obj){
      final ProdType type = (ProdType) obj;
      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(type.getPlateforme() == null){
         throw new RequiredObjectIsNullException("ProdType", "creation", "Plateforme");
      }else{
         type.setPlateforme(plateformeDao.mergeObject(type.getPlateforme()));
      }

      if(findDoublonManager(type)){
         log.warn("Doublon lors de la creation de l'objet ProdType : " + type.toString());
         throw new DoublonFoundException("ProdType", "creation");
      }else{
         BeanValidator.validateObject(type, new Validator[] {prodTypeValidator});
         prodTypeDao.createObject(type);
         log.info("Enregistrement de l'objet ProdType : " + type.toString());
      }
   }

   @Override
   public void updateObjectManager(final Object obj){
      final ProdType type = (ProdType) obj;
      if(findDoublonManager(type)){
         log.warn("Doublon lors de la modification de l'objet ProdType : " + type.toString());
         throw new DoublonFoundException("ProdType", "modification");
      }else{
         BeanValidator.validateObject(type, new Validator[] {prodTypeValidator});
         prodTypeDao.updateObject(type);
         log.info("Modification de l'objet ProdType : " + type.toString());
      }
   }

   @Override
   public void removeObjectManager(final Object obj){
      final ProdType type = (ProdType) obj;
      if(isUsedObjectManager(type)){
         log.warn("Objet utilisé lors de la suppression de l'objet " + "ProdType : " + type.toString());
         throw new ObjectUsedException("ProdType", "suppression");
      }else{
         prodTypeDao.removeObject(type.getProdTypeId());
         log.info("Suppression de l'objet ProdType : " + type.toString());
      }
   }

   @Override
   public List<? extends TKThesaurusObject> findByOrderManager(final Plateforme pf){
      return prodTypeDao.findByOrder(pf);
   }
}
