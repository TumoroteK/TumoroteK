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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.CoordonneeDao;
import fr.aphp.tumorotek.manager.context.CoordonneeManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.CoordonneeValidator;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;

/**
 *
 * Implémentation du manager du bean de domaine Coordonnee.
 * Interface créée le 05/01/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class CoordonneeManagerImpl implements CoordonneeManager
{

   private final Log log = LogFactory.getLog(CoordonneeManager.class);

   /** Bean Dao CollaborateurDao. */
   private CoordonneeDao coordonneeDao;
   /** Bean Dao CollaborateurDao. */
   private CollaborateurDao collaborateurDao;

   /** Bean validator. */
   private CoordonneeValidator coordonneeValidator;

   /**
    * Setter du bean CoordonneeDao.
    * @param cDao est le bean Dao.
    */
   public void setCoordonneeDao(final CoordonneeDao cDao){
      this.coordonneeDao = cDao;
   }

   /**
    * Setter du bean CollaborateurDao.
    * @param cDao est le bean Dao.
    */
   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   /**
    * Setter du bean CoordonneeValidator.
    * @param cValidator est le bean validator.
    */
   public void setCoordonneeValidator(final CoordonneeValidator cValidator){
      this.coordonneeValidator = cValidator;
   }

   /**
    * Recherche une Coordonnee dont l'identifiant est passé en paramètre.
    * @param coordonneeId Identifiant de la Coordonnee que l'on recherche.
    * @return Une Coordonnee.
    */
   @Override
   public Coordonnee findByIdManager(final Integer coordonneeId){
      return coordonneeDao.findById(coordonneeId);
   }

   /**
    * Recherche toutes les Coordonnees présentes dans la base.
    * @return Liste de Coordonnees.
    */
   @Override
   public List<Coordonnee> findAllObjectsManager(){
      log.debug("Recherche toutes les coordonnees");
      return coordonneeDao.findAll();
   }

   /**
    * Recherche les Collaborateurs liés à la Coordonnee passeé
    * en paramètre.
    * @param coordonnee Coordonnee pour laquelle on recherche des
    * collaborateurs.
    * @return Liste de Collaborateurs.
    */
   @Override
   public Set<Collaborateur> getCollaborateursManager(Coordonnee coordonnee){
      if(coordonnee != null){
         coordonnee = coordonneeDao.mergeObject(coordonnee);
         final Set<Collaborateur> collabs = coordonnee.getCollaborateurs();
         collabs.size();
         return collabs;
      }else{
         return new HashSet<>();
      }
   }

   /**
    * Recherche les doublons de la coordonnée pour le collaborateur passé
    *  en paramètre.
    * @param collaborateur Collaborateur pour lequel on cherche des doublons.
    * @param coordonnee Coordonnee pour laquelle on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonForCollaborateurManager(final Coordonnee coordonnee, final Collaborateur collaborateur){

      if(collaborateur != null && coordonnee != null){
         if(coordonnee.getCoordonneeId() == null){
            return coordonneeDao.findByCollaborateurId(collaborateur.getCollaborateurId()).contains(coordonnee);
         }else{
            return coordonneeDao
               .findByCollaborateurIdAndExcludedId(collaborateur.getCollaborateurId(), coordonnee.getCoordonneeId())
               .contains(coordonnee);
         }
      }else{
         return false;
      }

   }

   /**
    * Recherche les doublons dans une liste de coordonnées.
    * @param coordonnees Liste de coordonnées.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonInListManager(final List<Coordonnee> coordonnees){
      boolean doublon = false;

      for(int i = 0; i < coordonnees.size(); i++){
         final Coordonnee coord = coordonnees.get(i);

         for(int j = i + 1; j < coordonnees.size(); j++){
            if(coord.equals(coordonnees.get(j))){
               doublon = true;
            }
         }
      }
      return doublon;
   }

   /**
    * Test si la coordonnée est utilisée par d'autres objets que le
    * collaborateur passé en paramètre.
    * @param coordonnee Coordonnée à tester.
    * @param collaborateur Collaborateur pour lequel on teste la coord.
    * @return True si la coord estutilisée par d'autres objets.
    */
   @Override
   public Boolean isUsedByOtherObjectManager(final Coordonnee coordonnee, final Collaborateur collaborateur){
      boolean isUsed = false;

      final Set<Collaborateur> collabs = getCollaborateursManager(coordonnee);
      // si la coordonnée est liée à un établissement, un service
      // ou plusieurs collaborateurs
      if(coordonnee.getEtablissement() != null || coordonnee.getService() != null || collabs.size() > 1){
         isUsed = true;
         // sinon, si elle est liée a un autre collab
      }else if(collabs.size() > 0 && !collabs.contains(collaborateur)){
         isUsed = true;
      }

      return isUsed;
   }

   /**
    * Persist une instance de Coordonnee dans la base de données.
    * @param coordonnee Nouvelle instance de l'objet à créer.
    * @param collaborateurs Liste de collaborateurs associés à la
    * coordonnée.
    */
   @Override
   public void createObjectManager(final Coordonnee coordonnee, final List<Collaborateur> collaborateurs){

      BeanValidator.validateObject(coordonnee, new Validator[] {coordonneeValidator});

      if(collaborateurs != null){
         coordonnee.setCollaborateurs(new HashSet<Collaborateur>());
         for(int i = 0; i < collaborateurs.size(); i++){
            coordonnee.getCollaborateurs().add(collaborateurDao.mergeObject(collaborateurs.get(i)));
         }
      }
      //		} else {
      //			coordonnee.setCollaborateurs(null);
      //		}

      coordonneeDao.createObject(coordonnee);

      log.info("Enregistrement de l'objet Coordonnee : " + coordonnee.toString());

   }

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param coordonnee Objet à mettre à jour dans la base.
    * @param collaborateurs Liste de collaborateurs associés à la
    * coordonnée.
    */
   @Override
   public void updateObjectManager(Coordonnee coordonnee, final List<Collaborateur> collaborateurs, final boolean doValidation){

      coordonnee = coordonneeDao.mergeObject(coordonnee);
      if(doValidation){
         BeanValidator.validateObject(coordonnee, new Validator[] {coordonneeValidator});
      }

      if(collaborateurs != null){
         coordonnee.setCollaborateurs(new HashSet<Collaborateur>());
         for(int i = 0; i < collaborateurs.size(); i++){
            coordonnee.getCollaborateurs().add(collaborateurDao.mergeObject(collaborateurs.get(i)));
         }
      }
      // else {
      //	coordonnee.setCollaborateurs(null);
      //}

      coordonneeDao.updateObject(coordonnee);

      log.info("Enregistrement de l'objet Coordonnee : " + coordonnee.toString());

   }

   /**
    * Supprime une Coordonnee de la base de données.
    * @param coordonnee Coordonnee à supprimer de la base de données.
    */
   @Override
   public void removeObjectManager(Coordonnee coordonnee){

      coordonnee = coordonneeDao.mergeObject(coordonnee);
      if(coordonnee.getCollaborateurs() != null){
         final Iterator<Collaborateur> it = coordonnee.getCollaborateurs().iterator();
         while(it.hasNext()){
            final Collaborateur tmp = collaborateurDao.mergeObject(it.next());
            tmp.getCoordonnees().remove(coordonnee);
         }
      }

      coordonneeDao.removeObject(coordonnee.getCoordonneeId());
      log.info("Suppression de l'objet Coordonnee : " + coordonnee.toString());

   }

}
