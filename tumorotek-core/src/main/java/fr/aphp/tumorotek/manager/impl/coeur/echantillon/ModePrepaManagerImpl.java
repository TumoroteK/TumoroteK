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

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.ModePrepaDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.ModePrepaManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.echantillon.ModePrepaValidator;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine ModePrepa.
 * Classe créée le 24/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ModePrepaManagerImpl implements ModePrepaManager
{

   private final Log log = LogFactory.getLog(ModePrepaManager.class);

   /** Bean Dao ModePrepaDao. */
   private ModePrepaDao modePrepaDao;
   /** Bean Dao EchantillonDao. */
   private EchantillonDao echantillonDao;
   /** Bean Validator. */
   private ModePrepaValidator modePrepaValidator;
   private PlateformeDao plateformeDao;

   /**
    * Setter du bean ModePrepaDao.
    * @param mDao est le bean Dao.
    */
   public void setModePrepaDao(final ModePrepaDao mDao){
      this.modePrepaDao = mDao;
   }

   /**
    * Setter du bean EchantillonDao.
    * @param eDao est le bean Dao.
    */
   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setModePrepaValidator(final ModePrepaValidator validator){
      this.modePrepaValidator = validator;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Recherche un mode de préparation dont l'identifiant est 
    * passé en paramètre.
    * @param modePrepaId Identifiant du mode de préparation que l'on recherche.
    * @return Un ModePrepa.
    */
   @Override
   public ModePrepa findByIdManager(final Integer modePrepaId){
      return modePrepaDao.findById(modePrepaId).orElse(null);
   }

   //	/**
   //	 * Recherche tous les modes de préparation présents dans la base.
   //	 * @return Liste de EchantillonType.
   //	 */
   //	public List<ModePrepa> findAllObjectsManager() {
   //		log.info("Recherche de tous les ModePrepas");
   //		return modePrepaDao.findByOrder();
   //	}

   /**
    * Recherche tous les modes de préparation dont la valeur commence
    * comme celle passée en paramètre.
    * @param mode Mode de préparation que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de ModePrepa.
    */
   @Override
   public List<ModePrepa> findByModePrepaLikeManager(String mode, final boolean exactMatch){
      log.debug("Recherche ModePrepa par " + mode + " exactMatch " + String.valueOf(exactMatch));
      if(mode != null){
         if(!exactMatch){
            mode = mode + "%";
         }
         return modePrepaDao.findByNom(mode);
      }
      return new ArrayList<>();
   }

   @Override
   public boolean findDoublonManager(final ModePrepa obj){
      final ModePrepa mode = obj;
      if(mode != null){
         if(mode.getId() == null){
            return IterableUtils.toList(modePrepaDao.findAll()).contains(mode);
         }
         return modePrepaDao.findByExcludedId(mode.getId()).contains(mode);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final ModePrepa obj){
      final ModePrepa mode = obj;
      final List<Echantillon> echans = echantillonDao.findByModePrepa(mode);

      return (echans.size() > 0);
   }

   @Override
   public void createObjectManager(final ModePrepa obj){

      final ModePrepa mode = obj;

      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(mode.getPlateforme() == null){
         throw new RequiredObjectIsNullException("ModePrepa", "creation", "Plateforme");
      }
         mode.setPlateforme(plateformeDao.save(mode.getPlateforme()));

      if(findDoublonManager(mode)){
         log.warn("Doublon lors de la creation de l'objet ModePrepa : " + mode.toString());
         throw new DoublonFoundException("ModePrepa", "creation");
      }
         BeanValidator.validateObject(mode, new Validator[] {modePrepaValidator});
         modePrepaDao.save(mode);
         log.info("Enregistrement de l'objet ModePrepa : " + mode.toString());
   }

   @Override
   public void updateObjectManager(final ModePrepa obj){

      final ModePrepa mode = obj;

      if(findDoublonManager(mode)){
         log.warn("Doublon lors de la modification de l'objet ModePrepa : " + mode.toString());
         throw new DoublonFoundException("ModePrepa", "modification");
      }
         BeanValidator.validateObject(mode, new Validator[] {modePrepaValidator});
         modePrepaDao.save(mode);
         log.info("Modification de l'objet ModePrepa : " + mode.toString());
   }

   @Override
   public void removeObjectManager(final ModePrepa obj){

      final ModePrepa mode = obj;
      modePrepaDao.deleteById(mode.getId());
   }

   @Override
   public List<ModePrepa> findByOrderManager(final Plateforme pf){
      return modePrepaDao.findByPfOrder(pf);
   }

   @Override
   public List<ModePrepa> findByOrderManager(){
      return modePrepaDao.findByOrder();
   }
}
