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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.DroitObjetDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.utilisateur.DroitObjetManager;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.DroitObjet;
import fr.aphp.tumorotek.model.utilisateur.DroitObjetPK;
import fr.aphp.tumorotek.model.utilisateur.Profil;

/**
 *
 * Implémentation du manager du bean de domaine DroitObjet.
 * Interface créée le 19/05/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class DroitObjetManagerImpl implements DroitObjetManager
{

   private final Log log = LogFactory.getLog(DroitObjetManager.class);

   /** Bean Dao. */
   private DroitObjetDao droitObjetDao;
   /** Bean Dao. */
   private ProfilDao profilDao;
   /** Bean Dao. */
   private EntiteDao entiteDao;
   /** Bean Dao. */
   private OperationTypeDao operationTypeDao;

   public void setDroitObjetDao(final DroitObjetDao dDao){
      this.droitObjetDao = dDao;
   }

   public void setProfilDao(final ProfilDao pDao){
      this.profilDao = pDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   @Override
   public DroitObjet findByIdManager(final DroitObjetPK droitObjetPK){
      return droitObjetDao.findById(droitObjetPK);
   }

   @Override
   public List<DroitObjet> findAllObjectsManager(){
      log.debug("Recherche de tous les DroitObjets");
      return droitObjetDao.findAll();
   }

   @Override
   public List<DroitObjet> findByExcludedPKManager(final DroitObjetPK pk){
      if(pk != null){
         return droitObjetDao.findByExcludedPK(pk);
      }else{
         return droitObjetDao.findAll();
      }
   }

   @Override
   public List<DroitObjet> findByProfilManager(final Profil profil){
      if(profil != null){
         return droitObjetDao.findByProfil(profil);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<DroitObjet> findByProfilEntiteManager(final Profil profil, final Entite entite){
      if(profil != null && entite != null){
         return droitObjetDao.findByProfilEntite(profil, entite);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<OperationType> getOperationsByProfilEntiteManager(final Profil profil, final String nomEntite){
      // on récupère l'objet Entite
      Entite entite = null;
      if(nomEntite != null){
         entite = entiteDao.findByNom(nomEntite).get(0);
      }
      // on extrait les DroitObjets
      final List<DroitObjet> droits = findByProfilEntiteManager(profil, entite);
      final List<OperationType> results = new ArrayList<>();
      // pour chaque DroitObjet, on extrait l'OperationType
      for(int i = 0; i < droits.size(); i++){
         results.add(droits.get(i).getOperationType());
      }

      return results;
   }

   @Override
   public List<DroitObjet> findByProfilOperationManager(final Profil profil, final OperationType type){
      if(profil != null && type != null){
         return droitObjetDao.findByProfilOperation(profil, type);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public Boolean hasProfilOperationOnEntitesManager(final Profil profil, final OperationType type, final List<Entite> entites){

      if(entites != null && !entites.isEmpty()){
         for(final Entite entite : entites){
            if(!getOperationsByProfilEntiteManager(profil, entite.getNom()).contains(type)){
               return false;
            }
         }
      }else{
         return false;
      }

      return true;
   }

   @Override
   public Boolean findDoublonManager(final Profil profil, final Entite entite, final OperationType type){

      final DroitObjetPK pk = new DroitObjetPK(profil, entite, type);

      return (droitObjetDao.findById(pk) != null);
   }

   @Override
   public void validateObjectManager(final Profil profil, final Entite entite, final OperationType type){
      //profil required
      if(profil == null){
         log.warn("Objet obligatoire Profil manquant" + " lors de la validation d'un DroitObjet");
         throw new RequiredObjectIsNullException("DroitObjet", "creation", "Profil");
      }

      //Entite required
      if(entite == null){
         log.warn("Objet obligatoire Entite manquant" + " lors de la validation d'un DroitObjet");
         throw new RequiredObjectIsNullException("DroitObjet", "creation", "Entite");
      }

      //OperationType required
      if(type == null){
         log.warn("Objet obligatoire OperationType manquant" + " lors de la validation d'un DroitObjet");
         throw new RequiredObjectIsNullException("DroitObjet", "creation", "OperationType");
      }

      //Doublon
      if(profil.getProfilId() != null){
         if(findDoublonManager(profil, entite, type)){

            log.warn("Doublon lors validation objet DroitObjet");
            throw new DoublonFoundException("DroitObjet", "creation");
         }
      }
   }

   @Override
   public void createObjectManager(final DroitObjet droitObjet, final Profil profil, final Entite entite,
      final OperationType type){

      // validation de l'objet à créer
      validateObjectManager(profil, entite, type);

      droitObjet.setProfil(profilDao.mergeObject(profil));
      droitObjet.setEntite(entiteDao.mergeObject(entite));
      droitObjet.setOperationType(operationTypeDao.mergeObject(type));

      // création
      droitObjetDao.createObject(droitObjet);

      log.info("Enregistrement objet DroitObjet " + droitObjet.toString());

   }

   @Override
   public void removeObjectManager(final DroitObjet droitObjet){
      if(droitObjet != null){
         droitObjetDao.removeObject(droitObjet.getPk());
         log.info("Suppression de l'objet DroitObjet : " + droitObjet.toString());
      }else{
         log.warn("Suppression d'un DroitObjet null");
      }
   }

}
