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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.patient.PatientMedecinDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.CoordonneeDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.SpecialiteDao;
import fr.aphp.tumorotek.dao.contexte.TitreDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.manager.context.CollaborateurManager;
import fr.aphp.tumorotek.manager.context.CoordonneeManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.CollaborateurValidator;
import fr.aphp.tumorotek.manager.validation.contexte.CoordonneeValidator;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecin;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecinPK;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.contexte.Titre;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Collaborateur.
 * Interface créée le 01/10/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class CollaborateurManagerImpl implements CollaborateurManager
{

   private final Log log = LogFactory.getLog(CollaborateurManager.class);

   private CollaborateurDao collaborateurDao;
   private EtablissementDao etablissementDao;
   private SpecialiteDao specialiteDao;
   private TitreDao titreDao;
   private ServiceDao serviceDao;
   private CoordonneeDao coordonneeDao;
   private CoordonneeManager coordonneeManager;
   private OperationManager operationManager;
   private OperationTypeDao operationTypeDao;
   private PatientMedecinDao patientMedecinDao;
   private CollaborateurValidator collaborateurValidator;
   private CoordonneeValidator coordonneeValidator;

   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   public void setEtablissementDao(final EtablissementDao eDao){
      this.etablissementDao = eDao;
   }

   public void setSpecialiteDao(final SpecialiteDao sDao){
      this.specialiteDao = sDao;
   }

   public void setTitreDao(final TitreDao tDao){
      this.titreDao = tDao;
   }

   public void setServiceDao(final ServiceDao sDao){
      this.serviceDao = sDao;
   }

   public void setCoordonneeDao(final CoordonneeDao cDao){
      this.coordonneeDao = cDao;
   }

   public void setCoordonneeManager(final CoordonneeManager cManager){
      this.coordonneeManager = cManager;
   }

   public void setCoordonneeValidator(final CoordonneeValidator cValidator){
      this.coordonneeValidator = cValidator;
   }

   public void setCollaborateurValidator(final CollaborateurValidator validator){
      this.collaborateurValidator = validator;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setPatientMedecinDao(final PatientMedecinDao pDao){
      this.patientMedecinDao = pDao;
   }

   /**
    * Recherche un Collaborateur dont l'identifiant est passé en paramètre.
    * @param collaborateurId Identifiant du Collaborateur que l'on recherche.
    * @return Un Collaborateur.
    */
   @Override
   public Collaborateur findByIdManager(final Integer collaborateurId){
      return collaborateurDao.findById(collaborateurId);
   }

   /**
    * Recherche tous les collaborateur présents dans la base.
    * @return Liste de Collaborateur.
    */
   @Override
   public List<Collaborateur> findAllObjectsManager(){
      log.debug("Recherche tous les collaborateurs");
      return collaborateurDao.findAll();
   }

   /**
    * Recherche tous les collaborateur présents dans la base.
    * @return Liste ordonnée de Collaborateur.
    */
   @Override
   public List<Collaborateur> findAllObjectsWithOrderManager(){
      return collaborateurDao.findByOrder();
   }

   /**
    * Recherche tous les collaborateurs non archivés présents dans la base.
    * @return Liste ordonnée de Collaborateur.
    */
   @Override
   public List<Collaborateur> findAllActiveObjectsWithOrderManager(){
      return collaborateurDao.findByArchive(false);
   }

   /**
    * Recherche tous les collaborateurs présents dans la base qui
    * n'ont pas de service.
    * @return Liste de Collaborateurs.
    */
   @Override
   public List<Collaborateur> findAllObjectsWithoutService(){
      return collaborateurDao.findByCollaborateurWithoutService();
   }

   /**
    * Recherche tous les collaborateurs présents dans la base qui
    * n'ont pas de service et qui ne sont pas archivés.
    * @return Liste de Collaborateurs.
    */
   @Override
   public List<Collaborateur> findAllActiveObjectsWithoutService(){
      return collaborateurDao.findByCollaborateurWithoutServiceAndArchive(false);
   }

   @Override
   public Long findCountByEtablissementManager(final Etablissement etal){
      if(etal != null){
         return collaborateurDao.findCountByEtablissement(etal).get(0);
      }
      return new Long(0);
   }

   /**
    * Recherche les services liés au collaborateur passé en paramètre.
    * @param collaborateur Collaborateur pour lequel on recherche des
    * services.
    * @return Liste de Services.
    */
   @Override
   public Set<Service> getServicesManager(Collaborateur collaborateur){
      if(collaborateur != null){
         collaborateur = collaborateurDao.mergeObject(collaborateur);
         final Set<Service> services = collaborateur.getServices();
         services.size();
         return services;
      }
      return new HashSet<>();
   }

   @Override
   public List<Collaborateur> findByServicesAndArchiveManager(final Service service, final boolean archive){
      if(service != null && service.getServiceId() != null){
         return collaborateurDao.findByServiceIdArchiveWithOrder(service.getServiceId(), archive);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche les coordonnées liées au collaborateur passé en paramètre.
    * @param collaborateur Collaborateur pour lequel on recherche des
    * services.
    * @return Liste de Coordonnee.
    */
   @Override
   public Set<Coordonnee> getCoordonneesManager(Collaborateur collaborateur){
      if(collaborateur != null){
         collaborateur = collaborateurDao.mergeObject(collaborateur);
         final Set<Coordonnee> coordonnees = collaborateur.getCoordonnees();
         if(coordonnees != null){
            coordonnees.size();
            return coordonnees;
         }
      }
      return new HashSet<>();
   }

   /**
    * Recherche une liste de Collaborateurs dont le nom commence comme
    * celui passé en paramètre.
    * @param nom Nom pour lequel on recherche des Collaborateurs.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de Collaborateurs.
    */
   @Override
   public List<Collaborateur> findByNomLikeManager(String nom, final boolean exactMatch){
      log.debug("Recherche Collaborateur par nom : " + nom + " exactMatch " + String.valueOf(exactMatch));
      if(nom != null){
         if(!exactMatch){
            nom = nom + "%";
         }
         return collaborateurDao.findByNom(nom);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Collaborateur> findByNomLikeBothSideManager(String nom){
      log.debug("Recherche Collaborateur par nom : " + nom);
      if(nom != null){
         nom = "%" + nom + "%";
         return collaborateurDao.findByNom(nom);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche une liste de Collaborateurs dont le prénom commence comme
    * celui passé en paramètre.
    * @param prenom Prénom pour lequel on recherche des Collaborateurs.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de Collaborateurs.
    */
   @Override
   public List<Collaborateur> findByPrenomLikeManager(String prenom, final boolean exactMatch){
      log.debug("Recherche Collaborateur par prenom : " + prenom + " exactMatch " + String.valueOf(exactMatch));
      if(prenom != null){
         if(!exactMatch){
            prenom = prenom + "%";
         }
         return collaborateurDao.findByPrenom(prenom);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche une liste de Collaborateurs dont la spécialité est
    * passée en paramètre.
    * @param specialite Specialite pour laquelle on recherche des collabs.
    * @return Liste de Collaborateurs.
    */
   @Override
   public List<Collaborateur> findBySpecialiteManager(final Specialite specialite){

      if(specialite != null){
         return collaborateurDao.findBySpecialite(specialite);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche les doublons du Collaborateur passé en paramètre.
    * @param collaborateur Collaborateur pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonManager(final Collaborateur collaborateur){
      if(collaborateur.getCollaborateurId() == null){
         return collaborateurDao.findAll().contains(collaborateur);
      }
      return collaborateurDao.findByExcludedId(collaborateur.getCollaborateurId()).contains(collaborateur);
   }

   @Override
   public Boolean isArchivableManager(final Collaborateur collaborateur, final Service service){
      boolean archivable = true;

      if(collaborateur != null){
         final List<Service> services = serviceDao.findByCollaborateurIdAndArchive(collaborateur.getCollaborateurId(), false);
         services.remove(service);
         if(services.size() > 0){
            archivable = false;
         }else{
            archivable = true;
         }
      }

      return archivable;
   }

   /**
    * Persist une instance de Collaborateur dans la base de données.
    * @param collaborateur Nouvelle instance de l'objet à créer.
    * @param titre Titre associé au collaborateur.
    * @param etablissement Etablissement associé.
    * @param specialite Specialite du collaborateur.
    */
   @Override
   public void createObjectManager(final Collaborateur collaborateur, final Titre titre, final Etablissement etablissement,
      final Specialite specialite, final List<Service> services, final List<Coordonnee> coordonnees,
      final Utilisateur utilisateur){

      collaborateur.setEtablissement(etablissementDao.mergeObject(etablissement));
      collaborateur.setSpecialite(specialiteDao.mergeObject(specialite));
      collaborateur.setTitre(titreDao.mergeObject(titre));

      // on vérifie qu'il n'y a pas de doublons pour le collab
      if(findDoublonManager(collaborateur)){
         log.warn("Doublon lors de la creation de l'objet Collaborateur : " + collaborateur.toString());
         throw new DoublonFoundException("Collaborateur", "creation");
      }
      // on vérifie la validité du collab
      BeanValidator.validateObject(collaborateur, new Validator[] {collaborateurValidator});

      // si le collab a des coordonnées
      if(coordonnees != null){
         collaborateur.setCoordonnees(new HashSet<Coordonnee>());

         // on vérifie qu'il n'y a pas de doublons dans la liste
         if(coordonneeManager.findDoublonInListManager(coordonnees)){
            log.warn("Doublon dans la liste des coordonnées");
            throw new DoublonFoundException("Coordonnee", "creation");
         }
         // pour chaque coordonnée
         for(int i = 0; i < coordonnees.size(); i++){
            final Coordonnee coordonnee = coordonnees.get(i);
            // validation de la coordonnée
            BeanValidator.validateObject(coordonnee, new Validator[] {coordonneeValidator});

            // on récupère la liste des collabs de la coord
            List<Collaborateur> collabs;
            if(coordonnee.getCoordonneeId() != null && coordonnee.getCollaborateurs() != null){
               collabs = new ArrayList<>();
               final Iterator<Collaborateur> it = coordonneeManager.getCollaborateursManager(coordonnee).iterator();
               while(it.hasNext()){
                  collabs.add(it.next());
               }
            }else{
               collabs = null;
            }

            // si nouvelle coord => creation
            // sinon => update
            if(coordonnee.getCoordonneeId() == null){
               coordonneeManager.createObjectManager(coordonnee, collabs);
            }else{
               coordonneeManager.updateObjectManager(coordonnee, collabs, true);
            }

         }
      }

      collaborateurDao.createObject(collaborateur);

      updateServicesAndCoordonnees(collaborateur, services, coordonnees);
      log.info("Enregistrement de l'objet Collaborateur : " + collaborateur.toString());

      //Enregistrement de l'operation associee
      final Operation creationOp = new Operation();
      creationOp.setDate(Utils.getCurrentSystemCalendar());
      operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Creation").get(0), collaborateur);
   }

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param collaborateur Objet à mettre à jour dans la base.
    * @param titre Titre associé au collaborateur.
    * @param etablissement Etablissement associé.
    * @param specialite Specialite du collaborateur.
    */
   @Override
   public void updateObjectManager(final Collaborateur collaborateur, final Titre titre, final Etablissement etablissement,
      final Specialite specialite, final List<Service> services, final List<Coordonnee> coordonnees,
      final Utilisateur utilisateur, final boolean doValidation){

      collaborateur.setEtablissement(etablissementDao.mergeObject(etablissement));
      collaborateur.setSpecialite(specialiteDao.mergeObject(specialite));
      collaborateur.setTitre(titreDao.mergeObject(titre));

      // on vérifie qu'il n'y a pas de doublons pour le collab
      if(findDoublonManager(collaborateur)){
         log.warn("Doublon lors de la modification de l'objet " + "Collaborateur : " + collaborateur.toString());
         throw new DoublonFoundException("Collaborateur", "modification");
      }
      // on vérifie la validité du collab
      if(doValidation){
         BeanValidator.validateObject(collaborateur, new Validator[] {collaborateurValidator});
      }

      // si le collab a des coordonnées
      if(coordonnees != null){

         // on vérifie qu'il n'y a pas de doublons dans la liste
         if(coordonneeManager.findDoublonInListManager(coordonnees)){
            log.warn("Doublon dans la liste des coordonnées");
            throw new DoublonFoundException("Coordonnee", "modification");
         }
         // pour chaque coordonnée
         for(int i = 0; i < coordonnees.size(); i++){
            final Coordonnee coordonnee = coordonnees.get(i);
            // validation de la coordonnée
            if(doValidation){
               BeanValidator.validateObject(coordonnee, new Validator[] {coordonneeValidator});
            }

            // on récupère la liste des collabs de la coord
            List<Collaborateur> collabs;

            if(coordonnee.getCoordonneeId() != null && coordonnee.getCollaborateurs() != null){

               collabs = new ArrayList<>();
               final Iterator<Collaborateur> it = coordonneeManager.getCollaborateursManager(coordonnee).iterator();
               while(it.hasNext()){
                  collabs.add(it.next());
               }
            }else{
               collabs = null;
            }

            // si nouvelle coord => creation
            // sinon => update
            if(coordonnee.getCoordonneeId() == null){
               coordonneeManager.createObjectManager(coordonnee, collabs);
            }else{
               coordonneeManager.updateObjectManager(coordonnee, collabs, doValidation);
            }
         }
      }

      collaborateurDao.updateObject(collaborateur);

      updateServicesAndCoordonnees(collaborateur, services, coordonnees);

      log.info("Enregistrement de l'objet Collaborateur : " + collaborateur.toString());

      //Enregistrement de l'operation associee
      final Operation creationOp = new Operation();
      creationOp.setDate(Utils.getCurrentSystemCalendar());
      operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Modification").get(0),
         collaborateur);
   }

   @Override
   public boolean isReferencedObjectManager(final Collaborateur collabo){
      final Collaborateur clb = collaborateurDao.mergeObject(collabo);
      return !clb.getPlateformes().isEmpty() || !clb.getBanques().isEmpty() || !clb.getUtilisateurs().isEmpty()
         || !clb.getPrelevementsOperateur().isEmpty() || !clb.getPrelevementsPreleveur().isEmpty()
         || !clb.getEchantillons().isEmpty() || !clb.getProdDerives().isEmpty() || !clb.getCessionDemandeurs().isEmpty()
         || !clb.getCessionDestinataires().isEmpty() || !clb.getCessionExecutants().isEmpty() || !clb.getContrats().isEmpty()
         || !clb.getLaboInters().isEmpty() || !clb.getPatientMedecins().isEmpty() || !clb.getMaladies().isEmpty()
         || !clb.getRetours().isEmpty();
   }

   @Override
   public void removeObjectManager(Collaborateur collaborateur, final String comments, final Utilisateur user){
      if(collaborateur != null){
         if(!isReferencedObjectManager(collaborateur)){
            collaborateur = collaborateurDao.mergeObject(collaborateur);
            final Iterator<Service> it = collaborateur.getServices().iterator();
            while(it.hasNext()){
               final Service tmp = serviceDao.mergeObject(it.next());
               tmp.getCollaborateurs().remove(collaborateur);
            }

            final Iterator<Coordonnee> it2 = collaborateur.getCoordonnees().iterator();
            while(it2.hasNext()){
               final Coordonnee tmp = coordonneeDao.mergeObject(it2.next());
               if(tmp != null){
                  tmp.getCollaborateurs().remove(collaborateur);
                  if(tmp.getCollaborateurs().isEmpty()){
                     coordonneeDao.removeObject(tmp.getCoordonneeId());
                  }
               }
            }
            collaborateur.getCoordonnees().clear();

            //Supprime operations associes
            CreateOrUpdateUtilities.removeAssociateOperations(collaborateur, operationManager, comments, user);

            collaborateurDao.removeObject(collaborateur.getCollaborateurId());
            log.info("Suppression de l'objet Collaborateur : " + collaborateur.toString());
         }else{
            log.warn("Objet référencé lors de la suppression " + "de l'objet Collaborateur : " + collaborateur.toString());
            throw new ObjectReferencedException("collaborateur" + ".deletion.isReferencedCascade", false);
         }
      }
   }

   /**
    * Cette méthode met à jour les associations entre un collaborateur et
    * une liste de service.
    * @param collaborateur Collaborateur pour lequel on veut mettre à jour
    * les associations.
    * @param services Liste des services que l'on veut associer au 
    * collaborateur.
    */
   public void updateServices(final Collaborateur collaborateur, final List<Service> services){

      final Collaborateur coll = collaborateurDao.mergeObject(collaborateur);

      final Iterator<Service> it = coll.getServices().iterator();
      final List<Service> servicesToRemove = new ArrayList<>();
      // on parcourt les services qui sont actuellement associés
      // au collaborateur
      while(it.hasNext()){
         final Service tmp = it.next();
         // si un service n'est pas dans la nouvelle liste, on
         // le conserve afin de le retirer par la suite
         if(!services.contains(tmp)){
            servicesToRemove.add(tmp);
         }
      }

      // on parcourt la liste la liste des services à retirer de
      // l'association
      for(int i = 0; i < servicesToRemove.size(); i++){
         final Service serv = serviceDao.mergeObject(servicesToRemove.get(i));
         // on retire le service de chaque coté de l'association
         coll.getServices().remove(serv);
         serv.getCollaborateurs().remove(coll);

         log.debug(
            "Suppression de l'association entre le collaborateur : " + coll.toString() + " et le service : " + serv.toString());
      }

      // on parcourt la nouvelle liste de services
      for(int i = 0; i < services.size(); i++){
         // si un service n'était pas associé au collaborateur
         if(!coll.getServices().contains(services.get(i))){
            // on ajoute le service des deux cotés de l'association
            coll.getServices().add(serviceDao.mergeObject(services.get(i)));
            serviceDao.mergeObject(services.get(i)).getCollaborateurs().add(coll);

            log.debug("Ajout de l'association entre le collaborateur : " + coll.toString() + " et le service : "
               + services.get(i).toString());
         }
      }
   }

   /**
    * Cette méthode met à jour les associations entre un collaborateur,
    * une liste de service et une liste de coordonnées.
    * @param collaborateur Collaborateur pour lequel on veut mettre à jour
    * les associations.
    * @param services Liste des services que l'on veut associer au 
    * collaborateur.
    * @param coordonnees Liste des coordonnees que l'on veut associer au 
    * collaborateur.
    */
   public void updateServicesAndCoordonnees(final Collaborateur collaborateur, final List<Service> services,
      final List<Coordonnee> coordonnees){
      final Collaborateur coll = collaborateurDao.mergeObject(collaborateur);

      if(coordonnees != null){
         final Iterator<Coordonnee> it = coll.getCoordonnees().iterator();
         final List<Coordonnee> coordonneesToRemove = new ArrayList<>();
         // on parcourt les Coordonnees qui sont actuellement associés
         // au collaborateur
         while(it.hasNext()){
            final Coordonnee tmp = it.next();
            // si une Coordonnee n'est pas dans la nouvelle liste, on
            // la conserve afin de la retirer par la suite
            if(!coordonnees.contains(tmp)){
               coordonneesToRemove.add(tmp);
            }
         }

         // on parcourt la liste des Coordonnees à retirer de
         // l'association
         for(int i = 0; i < coordonneesToRemove.size(); i++){
            final Coordonnee coord = coordonneeDao.mergeObject(coordonneesToRemove.get(i));
            // on retire la Coordonnee de chaque coté de l'association
            coll.getCoordonnees().remove(coord);

            log.debug("Suppression de l'association entre le " + "collaborateur : " + coll.toString() + " et la coordonnée : "
               + coord.toString());
         }

         // on parcourt la nouvelle liste de Coordonnees
         for(int i = 0; i < coordonnees.size(); i++){
            // si une Coordonnee n'était pas associée au collaborateur
            if(!coll.getCoordonnees().contains(coordonnees.get(i))){
               // on ajoute la Coordonnee des deux cotés de l'association
               final Coordonnee coord = coordonneeDao.mergeObject(coordonnees.get(i));
               coll.getCoordonnees().add(coord);

               log.debug("Ajout de l'association entre le " + "collaborateur : " + coll.toString() + " et la coordonnee : "
                  + coordonnees.get(i).toString());
            }
         }
      }

      if(services != null){
         final Iterator<Service> it = coll.getServices().iterator();
         final List<Service> servicesToRemove = new ArrayList<>();
         // on parcourt les services qui sont actuellement associés
         // au collaborateur
         while(it.hasNext()){
            final Service tmp = it.next();
            // si un service n'est pas dans la nouvelle liste, on
            // le conserve afin de le retirer par la suite
            if(!services.contains(tmp)){
               servicesToRemove.add(tmp);
            }
         }

         // on parcourt la liste la liste des services à retirer de
         // l'association
         for(int i = 0; i < servicesToRemove.size(); i++){
            final Service serv = serviceDao.mergeObject(servicesToRemove.get(i));
            // on retire le service de chaque coté de l'association
            coll.getServices().remove(serv);
            serv.getCollaborateurs().remove(coll);

            log.debug("Suppression de l'association entre le " + "collaborateur : " + coll.toString() + " et le service : "
               + serv.toString());
         }

         // on parcourt la nouvelle liste de services
         for(int i = 0; i < services.size(); i++){
            // si un service n'était pas associé au collaborateur
            if(!coll.getServices().contains(services.get(i))){
               // on ajoute le service des deux cotés de l'association
               coll.getServices().add(serviceDao.mergeObject(services.get(i)));
               serviceDao.mergeObject(services.get(i)).getCollaborateurs().add(coll);

               log.debug("Ajout de l'association entre le " + "collaborateur : " + coll.toString() + " et le service : "
                  + services.get(i).toString());
            }
         }
      }
   }

   @Override
   public void removeObjectCascadeManager(Collaborateur collab, final Service service, final String comments,
      final Utilisateur user){

      if(collab != null){
         log.info("Suppression en cascade Collaborateur " + collab.toString());
         collab = collaborateurDao.mergeObject(collab);
         if(service != null){
            collab.getServices().remove(service);
            final Service tmp = serviceDao.mergeObject(service);
            tmp.getCollaborateurs().remove(collab);
         }

         if(collab.getEtablissement() != null){
            collab.getEtablissement().getCollaborateurs().remove(collab);
            collab.setEtablissement(null);
         }

         // supprime le collab en cascade si aucun service ne 
         // le référence
         if(collab.getServices().isEmpty()){
            removeObjectManager(collab, comments, user);
         }else{
            // passe en reference l'etablissement du premier 
            // service restant
            collab.setEtablissement(new ArrayList<>(collab.getServices()).get(0).getEtablissement());
            collaborateurDao.mergeObject(collab);
         }
      }

   }

   @Override
   public List<Collaborateur> findByEtablissementNoServiceManager(final Etablissement etab){
      return collaborateurDao.findByEtablissementNoService(etab);
   }

   @Override
   public List<Collaborateur> findByVilleLikeManager(final String ville){
      log.debug("Recherche Collaborateur par ville : " + ville);
      if(ville != null){
         //ville = "%" + ville + "%";
         return collaborateurDao.findByVille(ville);
      }
      return new ArrayList<>();
   }

   @Override
   public Long findCountByServicedIdManager(final Service serv){
      if(serv != null){
         return collaborateurDao.findCountByServiceId(serv.getServiceId()).get(0);
      }
      return new Long(0);
   }

   @Override
   public void fusionCollaborateurManager(final int idActif, final int idPassif, final String comments, final Utilisateur user){

      final Collaborateur cActif = collaborateurDao.findById(idActif);
      final Collaborateur cPassif = collaborateurDao.findById(idPassif);

      if(cActif != null && cPassif != null && idActif != idPassif){

         // relations many-to-many
         final Set<Collaborateur> setCollaborateurA = new HashSet<>();
         setCollaborateurA.add(cActif);

         // BANQUE-COLLABORATEUR
         final Set<Banque> banquesP = new HashSet<>(cPassif.getBanques());

         for(final Banque bq : banquesP){
            bq.setCollaborateur(cActif);
            cActif.getBanques().add(bq);
         }
         cPassif.getBanques().clear();

         // BANQUE-CONTACT		
         final Set<Banque> cBanquesP = new HashSet<>(cPassif.getContactBanques());

         for(final Banque bq : cBanquesP){
            bq.setContact(cActif);
            cActif.getContactBanques().add(bq);
         }
         cPassif.getContactBanques().clear();

         // CONTRAT
         final Set<Contrat> contratsP = cPassif.getContrats();

         for(final Contrat cont : contratsP){
            cont.setCollaborateur(cActif);
            cActif.getContrats().add(cont);
         }
         cPassif.getContrats().clear();

         // COORDONNEES ET SERVICES
         final Set<Coordonnee> coorP = cPassif.getCoordonnees();
         final Set<Coordonnee> coorA = cActif.getCoordonnees();
         final Set<Service> servicesP = cPassif.getServices();
         final Set<Service> servicesA = cActif.getServices();

         coorA.addAll(coorP);

         final Set<Service> servs = new HashSet<>();
         servs.addAll(servicesA);
         servs.addAll(servicesP);

         updateServicesAndCoordonnees(cActif, new ArrayList<>(servs), new ArrayList<>(coorA));
         updateServicesAndCoordonnees(cPassif, new ArrayList<Service>(), new ArrayList<Coordonnee>());

         //PRELEVEMENTS
         final Set<Prelevement> prelevementsPreleveurP = new HashSet<>(cPassif.getPrelevementsPreleveur());

         for(final Prelevement prel : prelevementsPreleveurP){
            prel.setPreleveur(cActif);
            cActif.getPrelevementsPreleveur().add(prel);
         }
         cPassif.getPrelevementsPreleveur().clear();

         final Set<Prelevement> prelevementsOperateurP = new HashSet<>(cPassif.getPrelevementsOperateur());

         for(final Prelevement prel : prelevementsOperateurP){
            prel.setOperateur(cActif);
            cActif.getPrelevementsOperateur().add(prel);
         }
         cPassif.getPrelevementsOperateur().clear();

         // LABO INTER
         final Set<LaboInter> laboIntersP = new HashSet<>(cPassif.getLaboInters());
         // Set<LaboInter> laboIntersA = cActif.getLaboInters();

         for(final LaboInter lab : laboIntersP){
            lab.setCollaborateur(cActif);
            cActif.getLaboInters().add(lab);
         }
         cPassif.getLaboInters().clear();

         // ECHANTILLON
         final Set<Echantillon> echantillonsP = new HashSet<>(cPassif.getEchantillons());

         for(final Echantillon ech : echantillonsP){
            ech.setCollaborateur(cActif);
            cActif.getEchantillons().add(ech);
         }
         cPassif.getEchantillons().clear();

         // PROD DERIVE
         final Set<ProdDerive> produitDeriveP = new HashSet<>(cPassif.getProdDerives());

         for(final ProdDerive derive : produitDeriveP){
            derive.setCollaborateur(cActif);
            cActif.getProdDerives().add(derive);
         }
         cPassif.getProdDerives().clear();

         // RETOUR
         final Set<Retour> retoursP = new HashSet<>(cPassif.getRetours());

         for(final Retour retour : retoursP){
            retour.setCollaborateur(cActif);
            cActif.getRetours().add(retour);
         }
         cPassif.getRetours().clear();

         // CESSION
         final Set<Cession> cessionsDestinataireP = new HashSet<>(cPassif.getCessionDestinataires());

         for(final Cession cess : cessionsDestinataireP){
            cess.setDestinataire(cActif);
            cActif.getCessionDestinataires().add(cess);
         }
         cPassif.getCessionDestinataires().clear();

         final Set<Cession> cessionsDemandeursP = new HashSet<>(cPassif.getCessionDemandeurs());

         for(final Cession cess : cessionsDemandeursP){
            cess.setDemandeur(cActif);
            cActif.getCessionDemandeurs().add(cess);
         }
         cPassif.getCessionDemandeurs().clear();

         final Set<Cession> cessionsExecutantP = new HashSet<>(cPassif.getCessionExecutants());

         for(final Cession cess : cessionsExecutantP){
            cess.setExecutant(cActif);
            cActif.getCessionExecutants().add(cess);
         }
         cPassif.getCessionExecutants().clear();

         // PLATEFORME
         final Set<Plateforme> plateformesP = new HashSet<>(cPassif.getPlateformes());
         for(final Plateforme pf : plateformesP){
            pf.setCollaborateur(cActif);
            cActif.getPlateformes().add(pf);
         }
         cPassif.getPlateformes().clear();

         // UTILISATEUR
         final Set<Utilisateur> utilisateurP = new HashSet<>(cPassif.getUtilisateurs());

         for(final Utilisateur usr : utilisateurP){
            usr.setCollaborateur(cActif);
            cActif.getUtilisateurs().add(usr);
         }
         cPassif.getUtilisateurs().clear();

         // MEDECINS REFERENTS - DAO MERGE				
         final Set<PatientMedecin> patientMedsP = new HashSet<>(cPassif.getPatientMedecins());

         //			List<PatientMedecin> patientMedsPList = new ArrayList<PatientMedecin>();
         //			for (PatientMedecin pM : patientMedsP) {
         //				patientMedsPList.add(pM);
         //			}
         //
         //			int i = patientMedsA.size();

         for(final PatientMedecin patientMedecin : patientMedsP){
            final PatientMedecin pm = new PatientMedecin();
            final PatientMedecinPK pk = new PatientMedecinPK(cActif, patientMedecin.getPatient());
            pm.setPk(pk);
            pm.setOrdre(patientMedecin.getOrdre());

            if(!cActif.getPatientMedecins().contains(pm)){
               patientMedecinDao.mergeObject(pm);
            }
            patientMedecinDao.removeObject(patientMedecin.getPk());
         }
         cPassif.getPatientMedecins().clear();

         //MALADIE_MEDECIN
         final Set<Maladie> maladieP = new HashSet<>(cPassif.getMaladies());

         for(final Maladie mal : maladieP){
            if(!cActif.getMaladies().contains(mal)){
               cActif.getMaladies().add(mal);
               //			mal.setCollaborateurs(setCollaborateurA);
               mal.getCollaborateurs().remove(cPassif);
               mal.getCollaborateurs().add(cActif);
            }else{
               mal.getCollaborateurs().remove(cPassif);
            }

         }
         cPassif.getMaladies().clear();

         // Operation FUSION attribuée à l'utilisateur actif
         final Operation fusionOp = new Operation();
         fusionOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(fusionOp, user, operationTypeDao.findByNom("Fusion").get(0), cActif);

         removeObjectManager(cPassif, "fusion id: " + idActif + " ." + comments, user);
      }
   }
}
