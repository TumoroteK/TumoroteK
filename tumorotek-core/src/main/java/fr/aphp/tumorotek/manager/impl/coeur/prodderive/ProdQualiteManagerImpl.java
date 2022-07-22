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
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdQualiteDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdQualiteManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.prodderive.ProdQualiteValidator;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine ProdQualite.
 * Classe créée le 29/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ProdQualiteManagerImpl implements ProdQualiteManager
{

   private final Log log = LogFactory.getLog(ProdQualiteManager.class);

   /** Bean Dao ProdQualiteDao. */
   private ProdQualiteDao prodQualiteDao;

   /** Bean Dao ProdDeriveDao. */
   private ProdDeriveDao prodDeriveDao;

   /** Bean Validator. */
   private ProdQualiteValidator prodQualiteValidator;

   private PlateformeDao plateformeDao;

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Setter du bean ProdQualiteDao.
    * @param pDao est le bean Dao.
    */
   public void setProdQualiteDao(final ProdQualiteDao pDao){
      this.prodQualiteDao = pDao;
   }

   /**
    * Setter du bean ProdDeriveDao.
    * @param pDao est le bean Dao.
    */
   public void setProdDeriveDao(final ProdDeriveDao pDao){
      this.prodDeriveDao = pDao;
   }

   public void setProdQualiteValidator(final ProdQualiteValidator validator){
      this.prodQualiteValidator = validator;
   }

   /**
    * Recherche une qualité de produit dérivé dont l'identifiant est
    * passé en paramètre.
    * @param prodQualiteId Identifiant de la qualité que l'on recherche.
    * @return Un ProdQualite.
    */
   @Override
   public ProdQualite findByIdManager(final Integer prodQualiteId){
      return prodQualiteDao.findById(prodQualiteId);
   }

   //	/**
   //	 * Recherche toutes les qualités de produit dérivé présentes dans la base.
   //	 * @return Liste de ProdQualite.
   //	 */
   //	public List<ProdQualite> findAllObjectsManager() {
   //		log.info("Recherche de tous les ProdQualites");
   //		return prodQualiteDao.findByOrder();
   //	}

   /**
    * Recherche toutes les qualités de produits dérivés dont la valeur commence
    * comme celle passée en paramètre.
    * @param qualite Qualité que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de ProdQualite.
    */
   @Override
   public List<ProdQualite> findByQualiteLikeManager(String qualite, final boolean exactMatch){
      log.debug("Recherche ProdQualite par " + qualite + " exactMatch " + String.valueOf(exactMatch));
      if(qualite != null){
         if(!exactMatch){
            qualite = qualite + "%";
         }
         return prodQualiteDao.findByProdQualite(qualite);
      }
      return new ArrayList<>();
   }

   @Override
   public boolean findDoublonManager(final ProdQualite obj){
      final ProdQualite qualite = obj;
      if(qualite != null){
         if(qualite.getId() == null){
            return prodQualiteDao.findAll().contains(qualite);
         }
         return prodQualiteDao.findByExcludedId(qualite.getId()).contains(qualite);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final ProdQualite obj){
      final ProdQualite qualite = obj;

      final List<ProdDerive> list = prodDeriveDao.findByProdQualite(qualite);

      return (list.size() > 0);
   }

   @Override
   public void createObjectManager(final ProdQualite obj){
      final ProdQualite qualite = obj;
      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(qualite.getPlateforme() == null){
         throw new RequiredObjectIsNullException("ProdQualite", "creation", "Plateforme");
      }
      qualite.setPlateforme(plateformeDao.mergeObject(qualite.getPlateforme()));

      if(findDoublonManager(qualite)){
         log.warn("Doublon lors de la creation de l'objet ProdQualite : " + qualite.toString());
         throw new DoublonFoundException("ProdQualite", "creation");
      }
      BeanValidator.validateObject(qualite, new Validator[] {prodQualiteValidator});
      prodQualiteDao.createObject(qualite);
      log.info("Enregistrement de l'objet ProdQualite : " + qualite.toString());
   }

   @Override
   public void updateObjectManager(final ProdQualite obj){
      final ProdQualite qualite = obj;
      if(findDoublonManager(qualite)){
         log.warn("Doublon lors de la modification de l'objet " + "ProdQualite : " + qualite.toString());
         throw new DoublonFoundException("ProdQualite", "modification");
      }
      BeanValidator.validateObject(qualite, new Validator[] {prodQualiteValidator});
      prodQualiteDao.updateObject(qualite);
      log.info("Modification de l'objet ProdQualite : " + qualite.toString());
   }

   @Override
   public void removeObjectManager(final ProdQualite obj){
      final ProdQualite qualite = obj;
      prodQualiteDao.removeObject(qualite.getId());
      log.info("Suppression de l'objet ProdQualite : " + qualite.toString());
   }

   @Override
   public List<ProdQualite> findByOrderManager(final Plateforme pf){
      return prodQualiteDao.findByPfOrder(pf);
   }

   @Override
   public List<ProdQualite> findByOrderManager(){
      return prodQualiteDao.findByOrder();
   }
}
