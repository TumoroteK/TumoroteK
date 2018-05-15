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
package fr.aphp.tumorotek.manager.impl.utilisateur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.utilisateur.DroitObjetManager;
import fr.aphp.tumorotek.manager.utilisateur.ProfilManager;
import fr.aphp.tumorotek.manager.utilisateur.ProfilUtilisateurManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.utilisateur.ProfilValidator;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.utilisateur.DroitObjet;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Profil.
 * Classe créée le 20/05/2010.
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class ProfilManagerImpl implements ProfilManager
{

   private final Log log = LogFactory.getLog(ProfilManager.class);

   private ProfilDao profilDao;
   private DroitObjetManager droitObjetManager;
   private ProfilUtilisateurManager profilUtilisateurManager;
   private ProfilValidator profilValidator;
   private OperationTypeDao operationTypeDao;
   private OperationManager operationManager;

   public void setProfilDao(final ProfilDao pDao){
      this.profilDao = pDao;
   }

   public void setDroitObjetManager(final DroitObjetManager dManager){
      this.droitObjetManager = dManager;
   }

   public void setProfilUtilisateurManager(final ProfilUtilisateurManager pManager){
      this.profilUtilisateurManager = pManager;
   }

   public void setProfilValidator(final ProfilValidator pValidator){
      this.profilValidator = pValidator;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   @Override
   public Profil findByIdManager(final Integer profilId){
      return profilDao.findById(profilId);
   }

   @Override
   public List<Profil> findAllObjectsManager(){
      log.debug("Recherche de toutes les Profils");
      return profilDao.findByOrder();
   }

   @Override
   public Boolean findDoublonManager(final Profil profil){
      if(profil != null){
         if(profil.getProfilId() == null){
            return profilDao.findByNom(profil.getNom()).contains(profil);
         }
         return profilDao.findByExcludedId(profil.getProfilId()).contains(profil);
      }
      return false;
   }

   @Override
   public Boolean isUsedObjectManager(Profil profil){
      if(profil != null && profil.getProfilId() != null){
         profil = profilDao.mergeObject(profil);
         return (profilUtilisateurManager.findByProfilManager(profil, null).size() > 0);
      }
      return false;
   }

   @Override
   public void createObjectManager(final Profil profil, final List<DroitObjet> droitObjets, final Utilisateur admin,
      final Plateforme pf){

      if(pf != null){
         profil.setPlateforme(pf);
      }else{
         log.warn("Objet obligatoire Plateforme manquant" + " lors de la creation du profil");
         throw new RequiredObjectIsNullException("Profil", "creation", "Plateforme");
      }

      //Doublon
      if(!findDoublonManager(profil)){

         // validation du Profil
         BeanValidator.validateObject(profil, new Validator[] {profilValidator});

         // validation des droitsobjets
         if(droitObjets != null){
            for(int i = 0; i < droitObjets.size(); i++){
               final DroitObjet obj = droitObjets.get(i);

               droitObjetManager.validateObjectManager(profil, obj.getEntite(), obj.getOperationType());
            }
         }

         profilDao.createObject(profil);
         log.info("Enregistrement objet Profil " + profil.toString());

         //Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, admin, operationTypeDao.findByNom("Creation").get(0), profil);

         // enregistrements des droitsobjets
         if(droitObjets != null){
            for(int i = 0; i < droitObjets.size(); i++){
               final DroitObjet obj = droitObjets.get(i);
               droitObjetManager.createObjectManager(obj, profil, obj.getEntite(), obj.getOperationType());
            }
         }

      }else{
         log.warn("Doublon lors creation objet Profil " + profil.toString());
         throw new DoublonFoundException("Profil", "creation");
      }
   }

   @Override
   public void updateObjectManager(final Profil profil, final List<DroitObjet> droitObjets, final Utilisateur admin){
      final List<DroitObjet> droitsToCreate = new ArrayList<>();
      final List<DroitObjet> droitsToRemove = new ArrayList<>();
      List<DroitObjet> oldDroits = new ArrayList<>();
      //Doublon
      if(!findDoublonManager(profil)){
         // validation du Contrat
         BeanValidator.validateObject(profil, new Validator[] {profilValidator});

         // validation des droitsobjets
         if(droitObjets != null){
            for(int i = 0; i < droitObjets.size(); i++){
               final DroitObjet obj = droitObjets.get(i);

               if(droitObjetManager.findByIdManager(obj.getPk()) == null){
                  droitObjetManager.validateObjectManager(profil, obj.getEntite(), obj.getOperationType());
                  droitsToCreate.add(obj);
               }
            }
         }

         profilDao.updateObject(profil);
         log.info("Enregistrement objet Profil " + profil.toString());

         //Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, admin, operationTypeDao.findByNom("Modification").get(0), profil);

         if(droitObjets != null){
            oldDroits = droitObjetManager.findByProfilManager(profil);
            for(int i = 0; i < oldDroits.size(); i++){
               if(!droitObjets.contains(oldDroits.get(i))){
                  droitsToRemove.add(oldDroits.get(i));
               }
            }

            for(int i = 0; i < droitsToRemove.size(); i++){
               droitObjetManager.removeObjectManager(droitsToRemove.get(i));
            }

            // enregistrements des droitsobjets
            for(int i = 0; i < droitsToCreate.size(); i++){
               final DroitObjet obj = droitsToCreate.get(i);
               droitObjetManager.createObjectManager(obj, profil, obj.getEntite(), obj.getOperationType());
            }
         }

      }else{
         log.warn("Doublon lors creation objet Profil " + profil.toString());
         throw new DoublonFoundException("Profil", "creation");
      }
   }

   @Override
   public void removeObjectManager(final Profil profil){
      if(profil != null){
         if(isUsedObjectManager(profil)){
            log.warn("Objet utilisé lors de la suppression de l'objet " + "Profil : " + profil.toString());
            throw new ObjectUsedException("deletion.profil.isUsed", false);
         }
         // suppression des DroitsObjets
         final List<DroitObjet> objets = droitObjetManager.findByProfilManager(profil);
         for(int i = 0; i < objets.size(); i++){
            droitObjetManager.removeObjectManager(objets.get(i));
         }

         final List<ProfilUtilisateur> profils = profilUtilisateurManager.findByProfilManager(profil, null);
         for(int i = 0; i < profils.size(); i++){
            profilUtilisateurManager.removeObjectManager(profils.get(i));
         }

         profilDao.removeObject(profil.getProfilId());
         log.info("Suppression de l'objet Profil : " + profil.toString());

         //Supprime operations associes
         final List<Operation> ops = operationManager.findByObjectManager(profil);
         for(int i = 0; i < ops.size(); i++){
            operationManager.removeObjectManager(ops.get(i));
         }
      }else{
         log.warn("Suppression d'une Profil null");
      }
   }

   @Override
   public List<Profil> findByPlateformeAndArchiveManager(final Plateforme pf, final Boolean archive){
      if(archive != null){
         return profilDao.findByPlateformeAndArchive(pf, archive);
      }
      final List<Profil> prfs = profilDao.findByPlateformeAndArchive(pf, false);
      prfs.addAll(profilDao.findByPlateformeAndArchive(pf, true));
      Collections.sort(prfs);
      return prfs;
   }

}
