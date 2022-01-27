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
package fr.aphp.tumorotek.manager.impl.interfacage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.interfacage.BlocExterneDao;
import fr.aphp.tumorotek.dao.interfacage.ValeurExterneDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.exception.EntiteObjectIdNotExistException;
import fr.aphp.tumorotek.manager.exception.InvalidMultipleAssociationException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.interfacage.ValeurExterneManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.interfacage.ValeurExterneValidator;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 *
 * Implémentation du manager du bean de domaine ValeurExterne.
 * Classe créée le 05/10/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ValeurExterneManagerImpl implements ValeurExterneManager
{

   private final Log log = LogFactory.getLog(ValeurExterneManager.class);

   /** Bean Dao. */
   private ValeurExterneDao valeurExterneDao;
   /** Bean Dao. */
   private BlocExterneDao blocExterneDao;
   /** Bean Dao. */
   private ChampEntiteDao champEntiteDao;
   /** Bean Dao. */
   private ChampAnnotationDao champAnnotationDao;
   /** Bean validator. */
   private ValeurExterneValidator valeurExterneValidator;

   public void setValeurExterneDao(final ValeurExterneDao vDao){
      this.valeurExterneDao = vDao;
   }

   public void setBlocExterneDao(final BlocExterneDao bDao){
      this.blocExterneDao = bDao;
   }

   public void setChampEntiteDao(final ChampEntiteDao cDao){
      this.champEntiteDao = cDao;
   }

   public void setChampAnnotationDao(final ChampAnnotationDao cDao){
      this.champAnnotationDao = cDao;
   }

   public void setValeurExterneValidator(final ValeurExterneValidator vValidator){
      this.valeurExterneValidator = vValidator;
   }

   @Override
   public ValeurExterne findByIdManager(final Integer valeurExterneId){
      return valeurExterneDao.findById(valeurExterneId);
   }

   @Override
   public List<ValeurExterne> findAllObjectsManager(){
      log.debug("Recherche de toutes les ValeurExternes");
      return IterableUtils.toList(valeurExterneDao.findAll());
   }

   @Override
   public List<ValeurExterne> findByBlocExterneManager(final BlocExterne blocExterne){
      log.debug("Recherche de toutes les ValeurExternes d'un bloc");
      if(blocExterne != null){
         return valeurExterneDao.findByBlocExterne(blocExterne);
      }else{
         return new ArrayList<>();
      }
   }
   
   @Override
   public List<ValeurExterne> findByDossierChampEntiteIdAndBlocEntiteIdManager(DossierExterne dos, 
		   Integer chpId, Integer eId) {
	   return valeurExterneDao.findByDossierChampEntiteIdAndBlocEntiteId(dos, chpId, eId);
   }


   @Override
   public ChampEntite getChampEntiteManager(final ValeurExterne valeurExterne){
      if(valeurExterne != null && valeurExterne.getChampEntiteId() != null){
         return champEntiteDao.findById(valeurExterne.getChampEntiteId());
      }else{
         return null;
      }
   }

   @Override
   public ChampAnnotation getChampAnnotationManager(final ValeurExterne valeurExterne){
      if(valeurExterne != null && valeurExterne.getChampAnnotationId() != null){
         return champAnnotationDao.findById(valeurExterne.getChampAnnotationId());
      }else{
         return null;
      }
   }

   @Override
   public boolean findDoublonManager(final ValeurExterne valeurExterne){
      if(valeurExterne != null){
         return valeurExterneDao.findByBlocExterne(valeurExterne.getBlocExterne()).contains(valeurExterne);
      }else{
         return false;
      }
   }

   @Override
   public void validateValeurExterneManager(final ValeurExterne valeurExterne, final BlocExterne blocExterne){

      // blocExterne required
      if(blocExterne == null){
         log.warn("Objet obligatoire BlocExterne manquant" + " lors de la création d'une ValeurExterne");
         throw new RequiredObjectIsNullException("ValeurExterne", "creation", "BlocExterne");
      }

      // il faut au moins un des champs
      if(valeurExterne.getChampEntiteId() == null && valeurExterne.getChampAnnotationId() == null){
         throw new InvalidMultipleAssociationException("ValeurExterne", "creation", true);
      }

      // il ne faut pas 2 champs
      if(valeurExterne.getChampEntiteId() != null && valeurExterne.getChampAnnotationId() != null){
         throw new InvalidMultipleAssociationException("ValeurExterne", "creation", false);
      }

      // il faut que le champ entité existe
      if(valeurExterne.getChampEntiteId() != null){
         if(champEntiteDao.findById(valeurExterne.getChampEntiteId()) == null){
            throw new EntiteObjectIdNotExistException("ValeurExterne", "ChampEntite", valeurExterne.getChampEntiteId());
         }
      }

      // il faut que le champ annotation existe
      if(valeurExterne.getChampAnnotationId() != null){
         if(champAnnotationDao.findById(valeurExterne.getChampAnnotationId()) == null){
            throw new EntiteObjectIdNotExistException("ValeurExterne", "ChampAnnotation", valeurExterne.getChampAnnotationId());
         }
      }

      // validation de la valeur
      BeanValidator.validateObject(valeurExterne, new Validator[] {valeurExterneValidator});
   }

   @Override
   public void saveManager(final ValeurExterne valeurExterne, final BlocExterne blocExterne){
      // Validation de la valeur
      validateValeurExterneManager(valeurExterne, blocExterne);

      valeurExterne.setBlocExterne(blocExterneDao.save(blocExterne));

      if(findDoublonManager(valeurExterne)){
         final ValeurExterne oldValeur = valeurExterneDao.findByBlocExterne(blocExterne)
            .get(valeurExterneDao.findByBlocExterne(blocExterne).indexOf(valeurExterne));

         // dans de codes organes ou lésionnels, on va fusoinner
         // les valeurs pour obtenir une liste de codes
         if(valeurExterne.getChampEntiteId() != null){
            final ChampEntite ce = champEntiteDao.findById(valeurExterne.getChampEntiteId());
            if(ce.getNom().equals("CodeOrganes") || ce.getNom().equals("CodeMorphos")){
               final StringBuffer sb = new StringBuffer(oldValeur.getValeur());
               if(valeurExterne.getValeur() != null && !valeurExterne.getValeur().equals("")){
                  sb.append(";");
                  sb.append(valeurExterne.getValeur());
               }
               valeurExterne.setValeur(sb.toString());
            }
         }

         deleteByIdManager(oldValeur);
      }
      valeurExterneDao.save(valeurExterne);

      log.info("Enregistrement de l'objet ValeurExterne : " + valeurExterne.toString());
   }

   @Override
   public void deleteByIdManager(final ValeurExterne valeurExterne){
      if(valeurExterne != null && valeurExterne.getValeurExterneId() != null){
         valeurExterneDao.deleteById(valeurExterne.getValeurExterneId());
         log.info("Suppression de l'objet ValeurExterne : " + valeurExterne.toString());
      }else{
         log.warn("Suppression d'une ValeurExterne null");
      }
   }
}
