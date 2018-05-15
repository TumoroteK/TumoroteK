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

import fr.aphp.tumorotek.dao.cession.CessionExamenDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.cession.CessionExamenManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.cession.CessionExamenValidator;
import fr.aphp.tumorotek.model.cession.CessionExamen;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine CessionExamen.
 * Interface créée le 26/01/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class CessionExamenManagerImpl implements CessionExamenManager
{

   private final Log log = LogFactory.getLog(CessionExamenManager.class);

   /** Bean Dao CessionExamenDao. */
   private CessionExamenDao cessionExamenDao;
   private CessionExamenValidator cessionExamenValidator;
   private PlateformeDao plateformeDao;

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Setter du bean CessionExamenDao.
    * @param cDao est le bean Dao.
    */
   public void setCessionExamenDao(final CessionExamenDao cDao){
      this.cessionExamenDao = cDao;
   }

   public void setCessionExamenValidator(final CessionExamenValidator cValidator){
      this.cessionExamenValidator = cValidator;
   }

   /**
    * Recherche un CessionExamen dont l'identifiant est passé en paramètre.
    * @param cessionExamenId Identifiant du CessionExamen que l'on recherche.
    * @return Un CessionExamen.
    */
   @Override
   public CessionExamen findByIdManager(final Integer cessionExamenId){
      return cessionExamenDao.findById(cessionExamenId);
   }

   /**
    * Recherche tous les CessionExamen dont l'examen commence
    * comme celui passé en paramètre.
    * @param exmaen Examen du CessionExamen que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de CessionExamens.
    */
   @Override
   public List<CessionExamen> findByExamenLikeManager(String examen, final boolean exactMatch){

      log.debug("Recherche CessionExamen par " + examen + " exactMatch " + String.valueOf(exactMatch));
      if(examen != null){
         if(!exactMatch){
            examen = examen + "%";
         }
         return cessionExamenDao.findByExamen(examen);
      }
      return new ArrayList<>();
   }

   @Override
   public boolean findDoublonManager(final CessionExamen o){
      if(o != null){
         final CessionExamen examen = o;
         if(examen.getCessionExamenId() == null){
            return cessionExamenDao.findAll().contains(examen);
         }
         return cessionExamenDao.findByExcludedId(examen.getCessionExamenId()).contains(examen);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final CessionExamen obj){
      final CessionExamen examen = cessionExamenDao.mergeObject(obj);
      return examen.getCessions().size() > 0;
   }

   @Override
   public void createObjectManager(final CessionExamen obj){
      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(obj.getPlateforme() == null){
         throw new RequiredObjectIsNullException("CessionExamen", "creation", "Plateforme");
      }
      obj.setPlateforme(plateformeDao.mergeObject(obj.getPlateforme()));
      BeanValidator.validateObject(obj, new Validator[] {cessionExamenValidator});
      if(!findDoublonManager(obj)){
         cessionExamenDao.createObject(obj);
         log.debug("Enregistrement objet CessionExamen " + obj.toString());
      }else{
         log.warn("Doublon lors creation objet CessionExamen " + obj.toString());
         throw new DoublonFoundException("CessionExamen", "creation");
      }
   }

   @Override
   public void updateObjectManager(final CessionExamen obj){
      BeanValidator.validateObject(obj, new Validator[] {cessionExamenValidator});
      if(!findDoublonManager(obj)){
         cessionExamenDao.updateObject(obj);
         log.debug("Modification objet CessionExamen " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet CessionExamen " + obj.toString());
         throw new DoublonFoundException("CessionExamen", "modification");
      }
   }

   @Override
   public void removeObjectManager(final CessionExamen obj){
      if(obj != null){
         cessionExamenDao.removeObject(obj.getCessionExamenId());
         log.debug("Suppression objet CessionExamen " + obj.toString());
      }else{
         log.warn("Suppression d'une CessionExamen null");
      }
   }

   @Override
   public List<CessionExamen> findByOrderManager(final Plateforme pf){
      return cessionExamenDao.findByOrder(pf);
   }
}
