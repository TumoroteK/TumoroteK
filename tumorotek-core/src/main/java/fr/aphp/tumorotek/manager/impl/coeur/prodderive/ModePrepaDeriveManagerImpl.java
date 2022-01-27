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

import fr.aphp.tumorotek.dao.coeur.prodderive.ModePrepaDeriveDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prodderive.ModePrepaDeriveManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.prodderive.ModePrepaDeriveValidator;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine ModePrepaDerive.
 * Classe créée le 05/01/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ModePrepaDeriveManagerImpl implements ModePrepaDeriveManager
{

   private final Log log = LogFactory.getLog(ModePrepaDeriveManager.class);

   /** Bean Dao ModePrepaDeriveDao. */
   private ModePrepaDeriveDao modePrepaDeriveDao;
   /** Bean Dao EchantillonDao. */
   private ProdDeriveDao prodDeriveDao;
   /** Bean Validator. */
   private ModePrepaDeriveValidator modePrepaDeriveValidator;
   private PlateformeDao plateformeDao;

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   public void setModePrepaDeriveDao(final ModePrepaDeriveDao mDao){
      this.modePrepaDeriveDao = mDao;
   }

   public void setProdDeriveDao(final ProdDeriveDao pDao){
      this.prodDeriveDao = pDao;
   }

   public void setModePrepaDeriveValidator(final ModePrepaDeriveValidator mValidator){
      this.modePrepaDeriveValidator = mValidator;
   }

   @Override
   public ModePrepaDerive findByIdManager(final Integer modePrepaDeriveId){
      return modePrepaDeriveDao.findById(modePrepaDeriveId);
   }

   @Override
   public List<ModePrepaDerive> findByModePrepaDeriveLikeManager(String mode, final boolean exactMatch){
      log.debug("Recherche ModePrepaDerive par " + mode + " exactMatch " + String.valueOf(exactMatch));
      if(mode != null){
         if(!exactMatch){
            mode = mode + "%";
         }
         return modePrepaDeriveDao.findByNom(mode);
      }
      return new ArrayList<>();
   }

   @Override
   public boolean findDoublonManager(final ModePrepaDerive obj){
      final ModePrepaDerive mode = obj;
      if(mode != null){
         if(mode.getId() == null){
            return IterableUtils.toList(modePrepaDeriveDao.findAll()).contains(mode);
         }
         return modePrepaDeriveDao.findByExcludedId(mode.getId()).contains(mode);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final ModePrepaDerive obj){
      final ModePrepaDerive mode = obj;
      final List<ProdDerive> derives = prodDeriveDao.findByModePrepaDerive(mode);

      return (derives.size() > 0);
   }

   @Override
   public void saveManager(final ModePrepaDerive obj){
      final ModePrepaDerive mode = obj;
      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(mode.getPlateforme() == null){
         throw new RequiredObjectIsNullException("ModePrepaDerive", "creation", "Plateforme");
      }
      mode.setPlateforme(plateformeDao.save(mode.getPlateforme()));

      if(findDoublonManager(mode)){
         log.warn("Doublon lors de la creation de l'objet " + "ModePrepaDerive : " + mode.toString());
         throw new DoublonFoundException("ModePrepaDerive", "creation");
      }
      BeanValidator.validateObject(mode, new Validator[] {modePrepaDeriveValidator});
      modePrepaDeriveDao.save(mode);
      log.info("Enregistrement de l'objet ModePrepaDerive : " + mode.toString());
   }

   @Override
   public void saveManager(final ModePrepaDerive obj){
      final ModePrepaDerive mode = obj;
      if(findDoublonManager(mode)){
         log.warn("Doublon lors de la modification de l'objet " + "ModePrepaDerive : " + mode.toString());
         throw new DoublonFoundException("ModePrepaDerive", "modification");
      }
         BeanValidator.validateObject(mode, new Validator[] {modePrepaDeriveValidator});
         modePrepaDeriveDao.save(mode);
         log.info("Modification de l'objet ModePrepaDerive : " + mode.toString());
   }

   @Override
   public void deleteByIdManager(final ModePrepaDerive obj){
      final ModePrepaDerive mode = obj;
      modePrepaDeriveDao.deleteById(mode.getId());
      log.info("Suppression de l'objet ModePrepaDerive : " + mode.toString());
   }

   @Override
   public List<ModePrepaDerive> findByOrderManager(final Plateforme pf){
      return modePrepaDeriveDao.findByPfOrder(pf);
   }

   @Override
   public List<ModePrepaDerive> findByOrderManager(){
      return modePrepaDeriveDao.findByOrder();
   }
}
