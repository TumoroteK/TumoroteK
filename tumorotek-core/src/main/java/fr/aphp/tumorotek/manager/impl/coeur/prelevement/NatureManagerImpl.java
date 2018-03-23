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
package fr.aphp.tumorotek.manager.impl.coeur.prelevement;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.NatureManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.prelevement.NatureValidator;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine Nature (de prelevement).
 * Classe créée le 01/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class NatureManagerImpl implements NatureManager
{

   private final Log log = LogFactory.getLog(NatureManager.class);

   /* Beans injectes par Spring*/
   private NatureDao natureDao;
   private NatureValidator natureValidator;
   private PlateformeDao plateformeDao;

   public NatureManagerImpl(){}

   /* Properties setters */
   public void setNatureDao(final NatureDao nDao){
      this.natureDao = nDao;
   }

   public void setNatureValidator(final NatureValidator natVal){
      this.natureValidator = natVal;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
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

      BeanValidator.validateObject(obj, new Validator[] {natureValidator});
      if(!findDoublonManager(obj)){
         natureDao.createObject((Nature) obj);
         log.info("Enregistrement objet Nature " + obj.toString());
      }else{
         log.warn("Doublon lors creation objet Nature " + obj.toString());
         throw new DoublonFoundException("Nature", "creation");
      }
   }

   @Override
   public void updateObjectManager(final Object obj){
      BeanValidator.validateObject(obj, new Validator[] {natureValidator});
      if(!findDoublonManager(obj)){
         natureDao.updateObject((Nature) obj);
         log.info("Modification objet Nature " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet Nature " + obj.toString());
         throw new DoublonFoundException("Nature", "modification");
      }
   }

   //	@Override
   //	public List<Nature> findAllObjectsManager() {
   //		log.info("Recherche totalite des Nature");
   //		return natureDao.findByOrder();
   //	}

   @Override
   public List<Nature> findByNatureLikeManager(String nature, final boolean exactMatch){
      if(!exactMatch){
         nature = nature + "%";
      }
      log.debug("Recherche Nature par nature: " + nature + " exactMatch " + String.valueOf(exactMatch));
      return natureDao.findByNature(nature);
   }

   @Override
   public void removeObjectManager(final Object obj){
      if(obj != null){
         if(!isUsedObjectManager(obj)){
            natureDao.removeObject(((Nature) obj).getNatureId());
            log.info("Suppression objet Nature " + obj.toString());
         }else{
            log.warn("Suppression objet Nature " + obj.toString() + " impossible car est reference (par Prelevement)");
            throw new ObjectUsedException();
         }
      }else{
         log.warn("Suppression d'une Nature null");
      }
   }

   @Override
   public boolean isUsedObjectManager(final Object o){
      final Nature nature = natureDao.mergeObject((Nature) o);
      return nature.getPrelevements().size() > 0;
   }

   @Override
   public boolean findDoublonManager(final Object o){
      if(o != null){
         final Nature nature = (Nature) o;
         if(nature.getNatureId() == null){
            return natureDao.findAll().contains(nature);
         }else{
            return natureDao.findByExcludedId(nature.getNatureId()).contains(nature);
         }
      }else{
         return false;
      }
   }

   @Override
   public List<? extends TKThesaurusObject> findByOrderManager(final Plateforme pf){
      return natureDao.findByOrder(pf);
   }

   @Override
   public TKThesaurusObject findByIdManager(final Integer id){
      return natureDao.findById(id);
   }
}
