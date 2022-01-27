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
package fr.aphp.tumorotek.manager.impl.imprimante;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.imprimante.ImprimanteApiDao;
import fr.aphp.tumorotek.dao.imprimante.ImprimanteDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.imprimante.ImprimanteManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.imprimante.ImprimanteValidator;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.ImprimanteApi;

/**
 *
 * Implémentation du manager du bean de domaine Imprimante.
 * Classe créée le 21/03/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ImprimanteManagerImpl implements ImprimanteManager
{

   private final Log log = LogFactory.getLog(ImprimanteManager.class);

   /** Bean Dao. */
   private ImprimanteDao imprimanteDao;
   /** Bean Dao. */
   private ImprimanteApiDao imprimanteApiDao;
   /** Bean Dao. */
   private PlateformeDao plateformeDao;
   /** Bean Validator. */
   private ImprimanteValidator imprimanteValidator;

   public void setImprimanteDao(final ImprimanteDao iDao){
      this.imprimanteDao = iDao;
   }

   public void setImprimanteApiDao(final ImprimanteApiDao iDao){
      this.imprimanteApiDao = iDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   public void setImprimanteValidator(final ImprimanteValidator iValidator){
      this.imprimanteValidator = iValidator;
   }

   @Override
   public Imprimante findByIdManager(final Integer imprimanteId){
      return imprimanteDao.findById(imprimanteId);
   }

   @Override
   public List<Imprimante> findAllObjectsManager(){
      log.debug("Recherche de toutes les Imprimantes");
      return IterableUtils.toList(imprimanteDao.findAll());
   }

   @Override
   public List<Imprimante> findByPlateformeManager(final Plateforme plateforme){
      log.debug("Recherche de toutes les Imprimantes d'une Plateforme");
      if(plateforme != null){
         return imprimanteDao.findByPlateforme(plateforme);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Imprimante> findByNomAndPlateformeManager(final String nom, final Plateforme plateforme){
      if(plateforme != null && nom != null){
         return imprimanteDao.findByNomAndPlateforme(nom, plateforme);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<String> findByPlateformeSelectNomManager(final Plateforme plateforme){
      log.debug("Recherche de toutes les Imprimantes d'une Plateforme");
      if(plateforme != null){
         return imprimanteDao.findByPlateformeSelectNom(plateforme);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public Boolean findDoublonManager(final Imprimante imprimante){
      if(imprimante != null){
         if(imprimante.getImprimanteId() == null){
            return IterableUtils.toList(imprimanteDao.findAll()).contains(imprimante);
         }else{
            return imprimanteDao.findByExcludedId(imprimante.getImprimanteId()).contains(imprimante);
         }
      }else{
         return false;
      }
   }

   @Override
   public Boolean isUsedObjectManager(Imprimante imprimante){
      if(imprimante != null && imprimante.getImprimanteId() != null){
         imprimante = imprimanteDao.save(imprimante);
         return (imprimante.getAffectationImprimantes().size() > 0);
      }else{
         return false;
      }
   }

   @Override
   public void saveManager(final Imprimante imprimante, final Plateforme plateforme, final ImprimanteApi imprimanteApi){
      // plateforme required
      if(plateforme != null){
         imprimante.setPlateforme(plateformeDao.save(plateforme));
      }else{
         log.warn("Objet obligatoire Plateforme manquant" + " lors de la création d'une Imprimante");
         throw new RequiredObjectIsNullException("Imprimante", "creation", "Plateforme");
      }

      // imprimanteApi required
      if(imprimanteApi != null){
         imprimante.setImprimanteApi(imprimanteApiDao.save(imprimanteApi));
      }else{
         log.warn("Objet obligatoire ImprimanteApi manquant" + " lors de la création d'une Imprimante");
         throw new RequiredObjectIsNullException("Imprimante", "creation", "ImprimanteApi");
      }

      // Test s'il y a des doublons
      if(findDoublonManager(imprimante)){
         log.warn("Doublon lors de la creation de l'objet Imprimante : " + imprimante.toString());
         throw new DoublonFoundException("Imprimante", "creation");
      }else{

         // validation du Contrat
         BeanValidator.validateObject(imprimante, new Validator[] {imprimanteValidator});

         imprimanteDao.save(imprimante);

         log.info("Enregistrement de l'objet Imprimante : " + imprimante.toString());
      }
   }

   @Override
   public void saveManager(final Imprimante imprimante, final Plateforme plateforme, final ImprimanteApi imprimanteApi){
      // plateforme required
      if(plateforme != null){
         imprimante.setPlateforme(plateformeDao.save(plateforme));
      }else{
         log.warn("Objet obligatoire Plateforme manquant" + " lors de la modification d'une Imprimante");
         throw new RequiredObjectIsNullException("Imprimante", "modification", "Plateforme");
      }

      // imprimanteApi required
      if(imprimanteApi != null){
         imprimante.setImprimanteApi(imprimanteApiDao.save(imprimanteApi));
      }else{
         log.warn("Objet obligatoire ImprimanteApi manquant" + " lors de la modification d'une Imprimante");
         throw new RequiredObjectIsNullException("Imprimante", "modification", "ImprimanteApi");
      }

      // Test s'il y a des doublons
      if(findDoublonManager(imprimante)){
         log.warn("Doublon lors de la modification de l'objet Imprimante : " + imprimante.toString());
         throw new DoublonFoundException("Imprimante", "modification");
      }else{

         // validation du Contrat
         BeanValidator.validateObject(imprimante, new Validator[] {imprimanteValidator});

         imprimanteDao.save(imprimante);

         log.info("Enregistrement de l'objet Imprimante : " + imprimante.toString());
      }
   }

   @Override
   public void deleteByIdManager(final Imprimante imprimante){
      if(imprimante != null){
         imprimanteDao.deleteById(imprimante.getImprimanteId());
         log.info("Suppression de l'objet Imprimante : " + imprimante.toString());
      }else{
         log.warn("Suppression d'un Imprimante null");
      }
   }
}
