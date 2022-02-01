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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.CategorieDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.CoordonneeDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.manager.context.CollaborateurManager;
import fr.aphp.tumorotek.manager.context.CoordonneeManager;
import fr.aphp.tumorotek.manager.context.EtablissementManager;
import fr.aphp.tumorotek.manager.context.ServiceManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.CoordonneeValidator;
import fr.aphp.tumorotek.manager.validation.contexte.EtablissementValidator;
import fr.aphp.tumorotek.model.coeur.cession.Contrat;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Etablissement. Interface créée
 * le 01/10/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class EtablissementManagerImpl implements EtablissementManager
{

   private final Log log = LogFactory.getLog(EtablissementManager.class);

   private EtablissementDao etablissementDao;
   private ServiceDao serviceDao;
   private CollaborateurDao collaborateurDao;
   /** Bean Dao CoordonneeDao. */
   private CoordonneeDao coordonneeDao;
   /** Bean Dao CategorieDao. */
   private CategorieDao categorieDao;
   /** Bean Manager CoordonneeManager. */
   private CoordonneeManager coordonneeManager;
   /** Bean Manager CoordonneeManager. */
   private ServiceManager serviceManager;
   private OperationManager operationManager;
   private OperationTypeDao operationTypeDao;
   private CollaborateurManager collaborateurManager;

   /** Bean validator. */
   private EtablissementValidator etablissementValidator;
   /** Bean Validator CoordonneeValidator. */
   private CoordonneeValidator coordonneeValidator;

   /**
    * Setter du bean EtablissementDao.
    * 
    * @param eDao
    *            est le bean Dao.
    */
   public void setEtablissementDao(final EtablissementDao eDao){
      this.etablissementDao = eDao;
   }

   public void setServiceDao(final ServiceDao eDao){
      this.serviceDao = eDao;
   }

   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   public void setCoordonneeDao(final CoordonneeDao cDao){
      this.coordonneeDao = cDao;
   }

   public void setCategorieDao(final CategorieDao cDao){
      this.categorieDao = cDao;
   }

   public void setEtablissementValidator(final EtablissementValidator eValidator){
      this.etablissementValidator = eValidator;
   }

   public void setCoordonneeManager(final CoordonneeManager cManager){
      this.coordonneeManager = cManager;
   }

   public void setCoordonneeValidator(final CoordonneeValidator cValidator){
      this.coordonneeValidator = cValidator;
   }

   public void setServiceManager(final ServiceManager sManager){
      this.serviceManager = sManager;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setCollaborateurManager(final CollaborateurManager cManager){
      this.collaborateurManager = cManager;
   }

   /**
    * Recherche un Etablissement dont l'identifiant est passé en paramètre.
    * 
    * @param etablissementId
    *            Identifiant du Etablissement que l'on recherche.
    * @return Un Etablissement.
    */
   @Override
   public Etablissement findByIdManager(final Integer etablissementId){
      return etablissementDao.findById(etablissementId).orElse(null);
   }

   /**
    * Recherche tous les Etablissements présents dans la base.
    * 
    * @return Liste d'Etablissement.
    */
   @Override
   public List<Etablissement> findAllObjectsManager(){
      return IterableUtils.toList(etablissementDao.findAll());
   }

   /**
    * Recherche tous les Etablissements présents dans la base.
    * 
    * @return Liste ordonnée d'Etablissement.
    */
   @Override
   public List<Etablissement> findAllObjectsWithOrderManager(){
      return etablissementDao.findByOrder();
   }

   /**
    * Recherche tous les Etablissements présents dans la base.
    * 
    * @return Liste ordonnée d'Etablissement.
    */
   @Override
   public List<Etablissement> findAllActiveObjectsWithOrderManager(){
      return etablissementDao.findByArchiveWithOrder(false);
   }

   /**
    * Recherche une liste d'Etablissement dont le nom commence comme celui
    * passé en paramètre.
    * 
    * @param nom
    *            Nom pour lequel on recherche des Etablissements.
    * @param exactMatch
    *            True si l'on souhaite seulement récuéprer les matchs exactes.
    * @return Liste d'Etablissements.
    */
   @Override
   public List<Etablissement> findByNomLikeManager(String nom, final boolean exactMatch){

      log.debug("Recherche Etablissement par nom : " + nom + " exactMatch " + String.valueOf(exactMatch));
      if(nom != null){
         if(!exactMatch){
            nom = nom + "%";
         }
         return etablissementDao.findByNom(nom);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Etablissement> findByNomLikeBothSideManager(String nom){

      log.debug("Recherche Etablissement par nom : " + nom);
      if(nom != null){
         nom = "%" + nom + "%";
         return etablissementDao.findByNom(nom);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Etablissement> findByVilleLikeManager(String ville){

      log.debug("Recherche Etablissement par ville : " + ville);
      if(ville != null){
         ville = "%" + ville + "%";
         return etablissementDao.findByVille(ville);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche une liste d'Etablissement dont le finess commence comme celui
    * passé en paramètre.
    * 
    * @param finess
    *            Finess pour lequel on recherche des Etablissements.
    * @param exactMatch
    *            True si l'on souhaite seulement récuéprer les matchs exactes.
    * @return Liste d'Etablissements.
    */
   @Override
   public List<Etablissement> findByFinessLikeManager(String finess, final boolean exactMatch){

      log.debug("Recherche Etablissement par finess : " + finess + " exactMatch " + String.valueOf(exactMatch));
      if(finess != null){
         if(!exactMatch){
            finess = finess + "%";
         }
         return etablissementDao.findByFiness(finess);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche les services liés à l'établissement passé en paramètre.
    * 
    * @param etablissement
    *            Etablissement pour lequel on recherche des services.
    * @return Liste de Services.
    */
   @Override
   public Set<Service> getServicesManager(Etablissement etablissement){
      etablissement = etablissementDao.save(etablissement);
      final Set<Service> services = etablissement.getServices();
      services.size();

      return services;
   }

   /**
    * Recherche les services liés à l'établissement passé en paramètre.
    * 
    * @param etablissement
    *            Etablissement pour lequel on recherche des services.
    * @return Liste ordonnéev de Services.
    */
   @Override
   public List<Service> getServicesWithOrderManager(final Etablissement etablissement){
      if(etablissement != null){
         return serviceDao.findByEtablissementWithOrder(etablissement);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche les services liés à l'établissement passé en paramètre.
    * 
    * @param etablissement
    *            Etablissement pour lequel on recherche des services.
    * @return Liste ordonnéev de Services.
    */
   @Override
   public List<Service> getActiveServicesWithOrderManager(final Etablissement etablissement){
      if(etablissement != null){
         return serviceDao.findByEtablissementArchiveWithOrder(etablissement, false);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche les collaborateurs liés à l'établissement passé en paramètre.
    * 
    * @param etablissement
    *            Etablissement pour lequel on recherche des collaborateurs.
    * @return Liste de Collaborateurs.
    */
   @Override
   public Set<Collaborateur> getCollaborateursManager(Etablissement etablissement){
      etablissement = etablissementDao.save(etablissement);
      final Set<Collaborateur> collabs = etablissement.getCollaborateurs();
      collabs.size();

      return collabs;
   }

   /**
    * Recherche les collaborateurs liés à l'établissement passé en paramètre.
    * 
    * @param etablissement
    *            Etablissement pour lequel on recherche des collaborateurs.
    * @return Liste ordonnée de Collaborateurs.
    */
   @Override
   public List<Collaborateur> getCollaborateursWithOrderManager(final Etablissement etablissement){
      if(etablissement != null){
         return collaborateurDao.findByEtablissementWithOrder(etablissement);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche les collaborateurs non archivés liés à l'établissement passé en
    * paramètre.
    * 
    * @param etablissement
    *            Etablissement pour lequel on recherche des collaborateurs.
    * @return Liste ordonnée de Collaborateurs.
    */
   @Override
   public List<Collaborateur> getActiveCollaborateursWithOrderManager(final Etablissement etablissement){
      if(etablissement != null){
         return collaborateurDao.findByEtablissementArchiveWithOrder(etablissement, false);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche les doublons de l'Etablissement passé en paramètre.
    * 
    * @param etablissement
    *            Etablissement pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonManager(final Etablissement etablissement){
      if(etablissement.getEtablissementId() == null){
         return IterableUtils.toList(etablissementDao.findAll()).contains(etablissement);
      }
      return etablissementDao.findByExcludedId(etablissement.getEtablissementId()).contains(etablissement);
   }

   /**
    * Persist une instance de Etablissement dans la base de données.
    * 
    * @param etablissement
    *            Nouvelle instance de l'objet à créer.
    * @param coordonnee
    *            Coordonnee associée à l'Etablissement.
    * @param categorie
    *            Categorie associée.
    */
   @Override
   public void createObjectManager(final Etablissement etablissement, final Coordonnee coordonnee, final Categorie categorie,
      final Utilisateur utilisateur){

      if(findDoublonManager(etablissement)){
         log.warn("Doublon lors de la creation de l'objet Etablissement : " + etablissement.toString());
         throw new DoublonFoundException("Etablissement", "creation");
      }

      BeanValidator.validateObject(etablissement, new Validator[] {etablissementValidator});

      if(coordonnee != null){
         BeanValidator.validateObject(coordonnee, new Validator[] {coordonneeValidator});
         if(coordonnee.getCoordonneeId() == null){
            coordonneeManager.createObjectManager(coordonnee, null);
         }else{
            coordonneeManager.updateObjectManager(coordonnee, null, true);
         }
      }
      // else {
      //	etablissement.setCoordonnee(null);
      //}

      etablissement.setCoordonnee(coordonneeDao.save(coordonnee));
      etablissement.setCategorie(categorieDao.save(categorie));

      etablissementDao.save(etablissement);
      log.info("Enregistrement de l'objet Etablissement : " + etablissement.toString());

      // Enregistrement de l'operation associee
      final Operation creationOp = new Operation();
      creationOp.setDate(Utils.getCurrentSystemCalendar());
      operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Creation").get(0), etablissement);

   }

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * 
    * @param etablissement
    *            Objet à mettre à jour dans la base.
    * @param coordonnee
    *            Coordonnee associée à l'Etablissement.
    * @param categorie
    *            Categorie associée.
    */
   @Override
   public void updateObjectManager(final Etablissement etablissement, final Coordonnee coordonnee, final Categorie categorie,
      final Utilisateur utilisateur, final boolean cascadeArchive){

      if(findDoublonManager(etablissement)){
         log.warn("Doublon lors de la modif de l'objet Etablissement : " + etablissement.toString());
         throw new DoublonFoundException("Etablissement", "modification");
      }

      BeanValidator.validateObject(etablissement, new Validator[] {etablissementValidator});

      if(coordonnee != null){
         BeanValidator.validateObject(coordonnee, new Validator[] {coordonneeValidator});
         if(coordonnee.getCoordonneeId() == null){
            coordonneeManager.createObjectManager(coordonnee, null);
         }else{
            coordonneeManager.updateObjectManager(coordonnee, null, true);
         }
      }

      etablissement.setCoordonnee(coordonneeDao.save(coordonnee));
      etablissement.setCategorie(categorieDao.save(categorie));

      etablissementDao.save(etablissement);
      log.info("Modification de l'objet Etablissement : " + etablissement.toString());

      if(cascadeArchive){
         archiveServices(etablissement, utilisateur);
         archiveCollaborateurs(etablissement, collaborateurManager.findByEtablissementNoServiceManager(etablissement),
            utilisateur);
      }

      // Enregistrement de l'operation associee
      final Operation creationOp = new Operation();
      creationOp.setDate(Utils.getCurrentSystemCalendar());
      operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Modification").get(0),
         etablissement);

   }

   @Override
   public boolean isUsedObjectManager(final Etablissement et){
      final Etablissement etab = etablissementDao.save(et);
      // References vers echantillons, derives?

      return etab.getServices().size() > 0 || collaborateurManager.findByEtablissementNoServiceManager(etab).size() > 0;
   }

   @Override
   public boolean isReferencedObjectManager(final Etablissement et){
      final Etablissement etab = etablissementDao.save(et);

      final Iterator<Service> servsIt = etab.getServices().iterator();
      while(servsIt.hasNext()){
         if(serviceManager.isReferencedObjectManager(servsIt.next())){
            return true;
         }
      }

      // Supprime les collaborateurs
      final List<Collaborateur> collabs = new ArrayList<>();
      collabs.addAll(collaborateurManager.findByEtablissementNoServiceManager(etab));
      final Iterator<Collaborateur> collabsIt = collabs.iterator();

      while(collabsIt.hasNext()){
         if(collaborateurManager.isReferencedObjectManager(collabsIt.next())){
            return true;
         }
      }

      return etab.getContrats().size() > 0;
   }

   /**
    * Supprime un Etablissement de la base de données.
    * 
    * @param etablissement
    *            Etablissement à supprimer de la base de données.
    */
   @Override
   public void removeObjectManager(final Etablissement etablissement, final String comments, final Utilisateur user){

      if(etablissement != null){
         if(!isUsedObjectManager(etablissement) && !isReferencedObjectManager(etablissement)){

            // Supprime operations associees
            CreateOrUpdateUtilities.removeAssociateOperations(etablissement, operationManager, comments, user);

            etablissementDao.deleteById(etablissement.getEtablissementId());
            log.info("Suppression de l'objet Etablissement : " + etablissement.toString());

         }else{
            if(!isReferencedObjectManager(etablissement)){
               log.warn("Objet utilisé lors de la suppression de l'objet " + "Etablissement : " + etablissement.toString());
               throw new ObjectUsedException("etablissement.deletion." + "isUsedCascade", true);
            }
            log.warn("Objet référencé lors de la suppression " + "de l'objet Etablissement : " + etablissement.toString());
            throw new ObjectReferencedException("etablissement" + ".deletion.isReferencedCascade", true);
         }
      }
   }

   @Override
   public void removeObjectCascadeManager(Etablissement etablissement, final String comments, final Utilisateur user){

      if(etablissement != null){
         log.info("Suppression en cascade depuis objet Etablissement " + etablissement.toString());

         etablissement = etablissementDao.save(etablissement);

         // Supprime les services
         final List<Service> servs = new ArrayList<>();
         servs.addAll(getServicesManager(etablissement));
         final Iterator<Service> servsIt = servs.iterator();

         while(servsIt.hasNext()){
            serviceManager.removeObjectCascadeManager(servsIt.next(), comments, user);
         }

         // Supprime les collaborateurs
         final List<Collaborateur> collabs = new ArrayList<>();
         collabs.addAll(collaborateurManager.findByEtablissementNoServiceManager(etablissement));
         final Iterator<Collaborateur> collabsIt = collabs.iterator();

         while(collabsIt.hasNext()){
            collaborateurManager.removeObjectCascadeManager(collabsIt.next(), null, comments, user);
         }
         collabs.clear();

         etablissement.setServices(new HashSet<Service>());
         etablissement.setCollaborateurs(new HashSet<>(collabs));

         removeObjectManager(etablissement, comments, user);
      }
   }

   public void archiveServices(final Etablissement etablissement, final Utilisateur utilisateur){
      if(etablissement != null){
         // on transfert le set de services dans une liste pour pouvoir
         // les parcourir
         final Set<Service> servs = getServicesManager(etablissement);
         final Iterator<Service> it = servs.iterator();
         final List<Service> services = new ArrayList<>();
         while(it.hasNext()){
            services.add(it.next());
         }

         // pour chaque service
         for(int i = 0; i < services.size(); i++){
            final Service serv = services.get(i);
            // si l'étab est archivé mais pas le service
            if(etablissement.getArchive()){
               if(!serv.getArchive()){
                  // recup des collabs du service
                  final List<Collaborateur> collabs = serviceManager.getCollaborateursWithOrderManager(serv);
                  // MAJ du collab
                  serv.setArchive(true);
                  serviceManager.updateObjectManager(serv, serv.getCoordonnee(), etablissement, collabs, utilisateur, true,
                     false);
               }
            }else{
               // si l'étab n'est pas archivé et que le service l'est
               if(serv.getArchive()){
                  // recup des collabs du service
                  final List<Collaborateur> collabs = serviceManager.getCollaborateursWithOrderManager(serv);

                  // MAJ du collab
                  serv.setArchive(false);
                  serviceManager.updateObjectManager(serv, serv.getCoordonnee(), etablissement, collabs, utilisateur, true,
                     false);
               }
            }
         }
      }
   }

   public void archiveCollaborateurs(final Etablissement etab, final List<Collaborateur> collaborateurs,
      final Utilisateur utilisateur){
      if(etab != null){
         // pour chaque collab
         for(int i = 0; i < collaborateurs.size(); i++){
            final Collaborateur collab = collaborateurs.get(i);
            // si le service est archivé et que le collab peut l'être
            if(etab.getArchive()){
               if(collaborateurManager.isArchivableManager(collab, null)){
                  // recup des coords du collab
                  final Iterator<Coordonnee> itC = collaborateurManager.getCoordonneesManager(collab).iterator();
                  final List<Coordonnee> coords = new ArrayList<>();
                  while(itC.hasNext()){
                     coords.add(itC.next());
                  }
                  // MAJ du collab
                  collab.setArchive(true);
                  collaborateurManager.updateObjectManager(collab, collab.getTitre(), collab.getEtablissement(),
                     collab.getSpecialite(), null, coords, utilisateur, false);
               }
            }else{
               // si le service n'est pas archivé et que le collab l'est
               if(collab.getArchive()){
                  // recup des coords du collab
                  final Iterator<Coordonnee> itC = collaborateurManager.getCoordonneesManager(collab).iterator();
                  final List<Coordonnee> coords = new ArrayList<>();
                  while(itC.hasNext()){
                     coords.add(itC.next());
                  }
                  // MAJ du collab
                  collab.setArchive(false);
                  collaborateurManager.updateObjectManager(collab, collab.getTitre(), collab.getEtablissement(),
                     collab.getSpecialite(), null, coords, utilisateur, false);
               }
            }
         }
      }
   }

   @Override
   public void fusionEtablissementManager(final int idActif, final int idPassif, final String comments, final Utilisateur user){

      final Etablissement etabliPassif = etablissementDao.findById(idPassif).orElse(null);
      final Etablissement etabliActif = etablissementDao.findById(idActif).orElse(null);

      if(etabliActif != null && etabliPassif != null && etabliPassif != etabliActif){

         final Set<Collaborateur> collabP = new HashSet<>(etabliPassif.getCollaborateurs());

         for(final Collaborateur col : collabP){
            col.setEtablissement(etabliActif);
            etabliActif.getCollaborateurs().add(col);
         }
         etabliPassif.getCollaborateurs().clear();

         final Set<Contrat> contratP = new HashSet<>(etabliPassif.getContrats());
         for(final Contrat cont : contratP){
            cont.setEtablissement(etabliActif);
            etabliActif.getContrats().add(cont);
         }
         etabliPassif.getContrats().clear();

         final Set<Service> serviceP = new HashSet<>(etabliPassif.getServices());
         final List<Service> toFusion = new ArrayList<>();
         for(final Service srv : serviceP){
            srv.setEtablissement(etabliActif);
            // ajoute le service si pas doublon
            if(!etabliActif.getServices().contains(srv)){
               etabliActif.getServices().add(srv);
            }else{
               toFusion.add(srv);
            }
         }
         etabliPassif.getServices().clear();

         if(!toFusion.isEmpty()){
            final List<Service> actifServices = new ArrayList<>(etabliActif.getServices());
            for(final Service srv : toFusion){
               serviceManager.fusionServiceManager(actifServices.get(actifServices.indexOf(srv)).getServiceId(),
                  srv.getServiceId(), "fusion services", user);
            }
         }

         // Operation FUSION attribuée à l'utilisateur actif
         final Operation fusionOp = new Operation();
         fusionOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(fusionOp, user, operationTypeDao.findByNom("Fusion").get(0), etabliActif);

         //etape finale, suppression de l'etablissement passif
         removeObjectManager(etabliPassif, "fusion id: " + idActif + " ." + comments, user);

      }
   }
}
