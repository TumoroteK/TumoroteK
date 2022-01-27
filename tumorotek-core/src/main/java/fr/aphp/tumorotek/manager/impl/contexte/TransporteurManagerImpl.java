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
package fr.aphp.tumorotek.manager.impl.contexte;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.CoordonneeDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.manager.context.CoordonneeManager;
import fr.aphp.tumorotek.manager.context.TransporteurManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.CoordonneeValidator;
import fr.aphp.tumorotek.manager.validation.contexte.TransporteurValidator;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Transporteur.
 * Interface créée le 01/10/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class TransporteurManagerImpl implements TransporteurManager
{

   private final Log log = LogFactory.getLog(TransporteurManager.class);

   /** Bean Dao TransporteurDao. */
   private TransporteurDao transporteurDao;
   /** Bean Dao. */
   private CoordonneeDao coordonneeDao;
   /** Bean Manager. */
   private CoordonneeManager coordonneeManager;
   /** Bean validator. */
   private TransporteurValidator transporteurValidator;
   /** Bean validator. */
   private CoordonneeValidator coordonneeValidator;
   private OperationManager operationManager;
   private OperationTypeDao operationTypeDao;

   /**
    * Setter du bean TransporteurDao.
    * @param tDao est le bean Dao.
    */
   public void setTransporteurDao(final TransporteurDao tDao){
      this.transporteurDao = tDao;
   }

   public void setTransporteurValidator(final TransporteurValidator tValidator){
      this.transporteurValidator = tValidator;
   }

   public void setCoordonneeValidator(final CoordonneeValidator cValidator){
      this.coordonneeValidator = cValidator;
   }

   public void setCoordonneeDao(final CoordonneeDao cDao){
      this.coordonneeDao = cDao;
   }

   public void setCoordonneeManager(final CoordonneeManager cManager){
      this.coordonneeManager = cManager;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   /**
    * Recherche un Transporteur dont l'identifiant est passé en paramètre.
    * @param transporteurId Identifiant du Transporteur que l'on recherche.
    * @return Un Transporteur.
    */
   @Override
   public Transporteur findByIdManager(final Integer transporteurId){
      return transporteurDao.findById(transporteurId);
   }

   /**
    * Recherche tous les Transporteur présents dans la base.
    * @return Liste de Transporteur.
    */
   @Override
   public List<Transporteur> findAllObjectsManager(){
      return transporteurDao.findByOrder();
   }

   @Override
   public List<Transporteur> findAllActiveManager(){
      return transporteurDao.findByArchive(false);
   }

   @Override
   public Boolean findDoublonManager(final Transporteur transporteur){
      if(transporteur != null){
         if(transporteur.getTransporteurId() == null){
            return IterableUtils.toList(transporteurDao.findAll()).contains(transporteur);
         }else{
            return transporteurDao.findByExcludedId(transporteur.getTransporteurId()).contains(transporteur);
         }
      }else{
         return false;
      }
   }

   @Override
   public void saveManager(final Transporteur transporteur, final Coordonnee coordonnee, final Utilisateur utilisateur){
      if(findDoublonManager(transporteur)){
         log.warn("Doublon lors de la creation de l'objet Transporteur : " + transporteur.toString());
         throw new DoublonFoundException("Transporteur", "creation");
      }else{

         BeanValidator.validateObject(transporteur, new Validator[] {transporteurValidator});

         if(coordonnee != null){
            BeanValidator.validateObject(coordonnee, new Validator[] {coordonneeValidator});
            if(coordonnee.getCoordonneeId() == null){
               coordonneeManager.saveManager(coordonnee, null);
            }else{
               coordonneeManager.saveManager(coordonnee, null, true);
            }
         }else{
            transporteur.setCoordonnee(null);
         }

         transporteur.setCoordonnee(coordonneeDao.save(coordonnee));

         transporteurDao.save(transporteur);
         log.info("Enregistrement de l'objet Transporteur : " + transporteur.toString());

         // Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.saveManager(creationOp, utilisateur, operationTypeDao.findByNom("Creation").get(0),
            transporteur);
      }
   }

   @Override
   public void saveManager(final Transporteur transporteur, final Coordonnee coordonnee, final Utilisateur utilisateur){
      if(findDoublonManager(transporteur)){
         log.warn("Doublon lors de la modif de l'objet Transporteur : " + transporteur.toString());
         throw new DoublonFoundException("Transporteur", "modification");
      }else{

         BeanValidator.validateObject(transporteur, new Validator[] {transporteurValidator});

         if(coordonnee != null){
            BeanValidator.validateObject(coordonnee, new Validator[] {coordonneeValidator});
            if(coordonnee.getCoordonneeId() == null){
               coordonneeManager.saveManager(coordonnee, null);
            }else{
               coordonneeManager.saveManager(coordonnee, null, true);
            }
         }else{
            transporteur.setCoordonnee(null);
         }

         transporteur.setCoordonnee(coordonneeDao.save(coordonnee));

         transporteurDao.save(transporteur);
         log.info("Enregistrement de l'objet Transporteur : " + transporteur.toString());

         //Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.saveManager(creationOp, utilisateur, operationTypeDao.findByNom("Modification").get(0),
            transporteur);
      }
   }

   @Override
   public void deleteByIdManager(final Transporteur transporteur, final String comments, final Utilisateur user){
      if(transporteur != null){
         if(!isReferencedObjectManager(transporteur)){
            //Supprime operations associes
            CreateOrUpdateUtilities.removeAssociateOperations(transporteur, operationManager, comments, user);

            transporteurDao.deleteById(transporteur.getTransporteurId());
            log.info("Suppression de l'objet Transporteur : " + transporteur.toString());
         }else{
            log.warn("Objet référencé lors de la suppression " + "de l'objet Transporteur : " + transporteur.toString());
            throw new ObjectReferencedException("transporteur" + ".deletion.isReferenced", false);
         }
      }
   }

   @Override
   public boolean isReferencedObjectManager(final Transporteur tr){
      final Transporteur transp = transporteurDao.save(tr);
      return !transp.getPrelevements().isEmpty() || !transp.getLaboInters().isEmpty() || !transp.getCessions().isEmpty();
   }
}
