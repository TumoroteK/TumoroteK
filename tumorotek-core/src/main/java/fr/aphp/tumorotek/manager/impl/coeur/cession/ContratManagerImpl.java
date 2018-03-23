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

import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.cession.ContratDao;
import fr.aphp.tumorotek.dao.cession.ProtocoleTypeDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.manager.coeur.cession.ContratManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.cession.ContratValidator;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.cession.ProtocoleType;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

public class ContratManagerImpl implements ContratManager
{

   private final Log log = LogFactory.getLog(ContratManager.class);

   /** Bean Dao. */
   private ContratDao contratDao;
   /** Bean Dao CessionDao. */
   private CessionDao cessionDao;
   /** Bean Dao. */
   private CollaborateurDao collaborateurDao;
   /** Bean Dao. */
   private ServiceDao serviceDao;
   /** Bean Dao. */
   private EtablissementDao etablissementDao;
   /** Bean Dao. */
   private ProtocoleTypeDao protocoleTypeDao;
   /** Bean Dao. */
   private PlateformeDao plateformeDao;
   /** Bean Validator. */
   private ContratValidator contratValidator;
   private OperationManager operationManager;
   private OperationTypeDao operationTypeDao;

   /**
    * Setter du bean ContratDao.
    * @param mDao est le bean Dao.
    */
   public void setContratDao(final ContratDao cDao){
      this.contratDao = cDao;
   }

   /**
    * Setter du bean CessionDao.
    * @param cDao est le bean Dao.
    */
   public void setCessionDao(final CessionDao cDao){
      this.cessionDao = cDao;
   }

   /**
    * Setter du bean ContratDao.
    * @param cDao est le bean Dao.
    */
   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   /**
    * Setter du bean ContratDao.
    * @param sDao est le bean Dao.
    */
   public void setServiceDao(final ServiceDao sDao){
      this.serviceDao = sDao;
   }

   public void setEtablissementDao(final EtablissementDao eDao){
      this.etablissementDao = eDao;
   }

   /**
    * Setter du bean ContratDao.
    * @param pDao est le bean Dao.
    */
   public void setProtocoleTypeDao(final ProtocoleTypeDao pDao){
      this.protocoleTypeDao = pDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Setter du bean ContratValidator.
    * @param mValidator est le bean validator.
    */
   public void setContratValidator(final ContratValidator cValidator){
      this.contratValidator = cValidator;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   /**
    * Recherche un Contrat dont l'identifiant est passé en paramètre.
    * @param mtaId Identifiant du Contrat que l'on recherche.
    * @return Un Contrat.
    */
   @Override
   public Contrat findByIdManager(final Integer mtaId){
      return contratDao.findById(mtaId);
   }

   /**
    * Recherche tous les Mtas présents dans la base.
    * @return Liste de Mtas.
    */
   @Override
   public List<Contrat> findAllObjectsManager(){
      log.debug("Recherche de tous les Mtas");
      return contratDao.findAll();
   }

   /**
    * Recherche tous les Contrats (ordonnés par numero) d'une banque.
    * @param banque Banque pour laquelle on recherche.
    * @return Liste de Contrats.
    */
   @Override
   public List<Contrat> findAllObjectsByPlateformeManager(final Plateforme plateforme){
      log.debug("Recherche de tous les Contrats d'une plateforme");
      if(plateforme != null){
         return contratDao.findByPlateforme(plateforme);
      }else{
         return new ArrayList<>();
      }
   }

   /**
    * Recherche les Cessions liées au Contrat passé en paramètre.
    * @param contrat Contrat pour lequel on recherche des cessions.
    * @return Liste de Cessions.
    */
   @Override
   public List<Cession> getCessionsManager(final Contrat contrat){
      if(contrat != null){
         return cessionDao.findByContrat(contrat);
      }else{
         return new ArrayList<>();
      }
   }

   /**
    * Recherche une liste de Contrat dont le numero commence comme
    * celui passé en paramètre.
    * @param numero Numero pour lequel on recherche des Contrats.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de Contrats.
    */
   @Override
   public List<Contrat> findByNumeroLikeManager(String numero, final boolean exactMatch){

      log.debug("Recherche Contrat par numero : " + numero + " exactMatch " + String.valueOf(exactMatch));
      if(numero != null){
         if(!exactMatch){
            numero = numero + "%";
         }
         return contratDao.findByNumero(numero);
      }else{
         return new ArrayList<>();
      }

   }

   /**
    * Recherche les doublons du Contrat passé en paramètre.
    * @param contrat Contrat pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonManager(final Contrat contrat){
      if(contrat != null){
         if(contrat.getContratId() == null){
            return contratDao.findAll().contains(contrat);
         }else{
            return contratDao.findByExcludedId(contrat.getContratId()).contains(contrat);
         }
      }else{
         return false;
      }
   }

   /**
    * Teste si le Contrat passé en paramètre est utilisé par d'autres
    * objets.
    * @param contrat Contrat que l'on test.
    * @return True si l'objet est utilisé.
    */
   @Override
   public Boolean isUsedObjectManager(final Contrat contrat){
      if(contrat != null){
         return (getCessionsManager(contrat).size() > 0);
      }else{
         return false;
      }
   }

   /**
    * Persist une instance de Contrat dans la base de données.
    * @param contrat Nouvelle instance de l'objet à créer.
    * @param collaborateur Collaborateur du contrat.
    * @param service Service du Contrat.
    * @param protocoleType ProtocoleType du contrat.
    */
   @Override
   public void createObjectManager(final Contrat contrat, final Collaborateur collaborateur, final Service service,
      final Etablissement etablissement, final ProtocoleType protocoleType, final Plateforme plateforme,
      final Utilisateur utilisateur){

      //plateforme required
      if(plateforme != null){
         contrat.setPlateforme(plateformeDao.mergeObject(plateforme));
      }else{
         log.warn("Objet obligatoire Plateforme manquant" + " lors de la création d'un Contrat");
         throw new RequiredObjectIsNullException("Contrat", "Creation", "Plateforme");
      }

      contrat.setCollaborateur(collaborateurDao.mergeObject(collaborateur));
      contrat.setService(serviceDao.mergeObject(service));
      contrat.setEtablissement(etablissementDao.mergeObject(etablissement));
      contrat.setProtocoleType(protocoleTypeDao.mergeObject(protocoleType));

      // Test s'il y a des doublons
      if(findDoublonManager(contrat)){
         log.warn("Doublon lors de la creation de l'objet Contrat : " + contrat.toString());
         throw new DoublonFoundException("Contrat", "creation");
      }else{

         // validation du Contrat
         BeanValidator.validateObject(contrat, new Validator[] {contratValidator});

         contratDao.createObject(contrat);

         log.info("Enregistrement de l'objet Contrat : " + contrat.toString());

         //Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Creation").get(0), contrat);
      }
   }

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param contrat Objet à persister.
    * @param collaborateur Collaborateur du contrat.
    * @param service Service du Contrat.
    * @param protocoleType ProtocoleType du contrat.
    */
   @Override
   public void updateObjectManager(final Contrat contrat, final Collaborateur collaborateur, final Service service,
      final Etablissement etablissement, final ProtocoleType protocoleType, final Plateforme plateforme,
      final Utilisateur utilisateur){

      //plateforme required
      if(plateforme != null){
         contrat.setPlateforme(plateformeDao.mergeObject(plateforme));
      }else{
         log.warn("Objet obligatoire Plateforme manquant" + " lors de la modification d'un Contrat");
         throw new RequiredObjectIsNullException("Contrat", "modification", "Plateforme");
      }

      contrat.setCollaborateur(collaborateurDao.mergeObject(collaborateur));
      contrat.setService(serviceDao.mergeObject(service));
      contrat.setEtablissement(etablissementDao.mergeObject(etablissement));
      contrat.setProtocoleType(protocoleTypeDao.mergeObject(protocoleType));

      // Test s'il y a des doublons
      if(findDoublonManager(contrat)){
         log.warn("Doublon lors de la modification de l'objet Contrat : " + contrat.toString());
         throw new DoublonFoundException("Contrat", "modification");
      }else{

         // validation du Contrat
         BeanValidator.validateObject(contrat, new Validator[] {contratValidator});

         contratDao.updateObject(contrat);

         log.info("Modification de l'objet Contrat : " + contrat.toString());

         //Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Modification").get(0),
            contrat);
      }
   }

   @Override
   public void removeObjectManager(final Contrat contrat, final String comments, final Utilisateur u){
      if(contrat != null){
         if(isUsedObjectManager(contrat)){
            log.warn("Objet utilisé lors de la suppression de l'objet " + "Contrat : " + contrat.toString());
            throw new ObjectUsedException("contrat.deletion.isReferenced", false);
         }else{

            //Supprime operations associes
            CreateOrUpdateUtilities.removeAssociateOperations(contrat, operationManager, comments, u);

            contratDao.removeObject(contrat.getContratId());
            log.info("Suppression de l'objet Contrat : " + contrat.toString());
         }
      }else{
         log.warn("Suppression d'un Contrat null");
      }
   }
}
