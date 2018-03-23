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

import fr.aphp.tumorotek.dao.cession.DestructionMotifDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.cession.DestructionMotifManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.cession.DestructionMotifValidator;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.cession.DestructionMotif;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine DestructionMotif.
 * Interface créée le 27/01/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class DestructionMotifManagerImpl implements DestructionMotifManager
{

   private final Log log = LogFactory.getLog(DestructionMotifManager.class);

   /** Bean Dao DestructionMotifDao. */
   private DestructionMotifDao destructionMotifDao;
   private DestructionMotifValidator destructionMotifValidator;
   private PlateformeDao plateformeDao;

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Setter du bean DestructionMotifDao.
    * @param dDao est le bean Dao.
    */
   public void setDestructionMotifDao(final DestructionMotifDao dDao){
      this.destructionMotifDao = dDao;
   }

   public void setDestructionMotifValidator(final DestructionMotifValidator dValidator){
      this.destructionMotifValidator = dValidator;
   }

   /**
    * Recherche un DestructionMotif dont l'identifiant est passé en paramètre.
    * @param destructionMotifId Identifiant du DestructionMotif que 
    * l'on recherche.
    * @return Un DestructionMotif.
    */
   @Override
   public DestructionMotif findByIdManager(final Integer destructionMotifId){
      return destructionMotifDao.findById(destructionMotifId);
   }

   //	/**
   //	 * Recherche tous les DestructionMotifs présents dans la base.
   //	 * @return Liste de DestructionMotifs.
   //	 */
   //	@Override
   //	public List<DestructionMotif> findAllObjectsManager() {
   //		return destructionMotifDao.findByOrder();
   //	}

   /**
    * Recherche tous les DestructionMotifs dont le motif commence
    * comme celui passé en paramètre.
    * @param motif DestructionMotif que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de DestructionMotifs.
    */
   @Override
   public List<DestructionMotif> findByMotifLikeManager(String motif, final boolean exactMatch){

      log.debug("Recherche DestructionMotif par " + motif + " exactMatch " + String.valueOf(exactMatch));
      if(motif != null){
         if(!exactMatch){
            motif = motif + "%";
         }
         return destructionMotifDao.findByMotif(motif);
      }else{
         return new ArrayList<>();
      }

   }

   @Override
   public boolean findDoublonManager(final Object o){
      if(o != null){
         final DestructionMotif motif = (DestructionMotif) o;
         if(motif.getDestructionMotifId() == null){
            return destructionMotifDao.findAll().contains(motif);
         }else{
            return destructionMotifDao.findByExcludedId(motif.getDestructionMotifId()).contains(motif);
         }
      }else{
         return false;
      }
   }

   @Override
   public boolean isUsedObjectManager(final Object obj){
      final DestructionMotif motif = destructionMotifDao.mergeObject((DestructionMotif) obj);
      return motif.getCessions().size() > 0;
   }

   @Override
   public void createObjectManager(final Object obj){
      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(((DestructionMotif) obj).getPlateforme() == null){
         throw new RequiredObjectIsNullException("DestructionMotif", "creation", "Plateforme");
      }else{
         ((DestructionMotif) obj).setPlateforme(plateformeDao.mergeObject(((DestructionMotif) obj).getPlateforme()));
      }
      BeanValidator.validateObject(obj, new Validator[] {destructionMotifValidator});
      if(!findDoublonManager(obj)){
         destructionMotifDao.createObject((DestructionMotif) obj);
         log.info("Enregistrement objet DestructionMotif " + obj.toString());
      }else{
         log.warn("Doublon lors creation objet DestructionMotif " + obj.toString());
         throw new DoublonFoundException("DestructionMotif", "creation");
      }
   }

   @Override
   public void updateObjectManager(final Object obj){
      BeanValidator.validateObject(obj, new Validator[] {destructionMotifValidator});
      if(!findDoublonManager(obj)){
         destructionMotifDao.updateObject((DestructionMotif) obj);
         log.info("Modification objet DestructionMotif " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet DestructionMotif " + obj.toString());
         throw new DoublonFoundException("DestructionMotif", "modification");
      }
   }

   @Override
   public void removeObjectManager(final Object obj){
      if(obj != null){
         //			if (!isUsedObjectManager(obj)) {
         destructionMotifDao.removeObject(((DestructionMotif) obj).getDestructionMotifId());
         log.info("Suppression objet DestructionMotif " + obj.toString());
         //			} else {
         //				log.warn("Suppression objet DestructionMotif " 
         //						+ obj.toString()
         //						+ " impossible car est reference (par Cession)");
         //				throw new ObjectUsedException();
         //			}
      }else{
         log.warn("Suppression d'un DestructionMotif null");
      }
   }

   @Override
   public List<? extends TKThesaurusObject> findByOrderManager(final Plateforme pf){
      return destructionMotifDao.findByOrder(pf);
   }
}
