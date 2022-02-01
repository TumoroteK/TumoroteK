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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.cession.CessionDao;
import fr.aphp.tumorotek.dao.coeur.cession.CessionExamenDao;
import fr.aphp.tumorotek.dao.coeur.cession.CessionStatutDao;
import fr.aphp.tumorotek.dao.coeur.cession.CessionTypeDao;
import fr.aphp.tumorotek.dao.coeur.cession.ContratDao;
import fr.aphp.tumorotek.dao.coeur.cession.DestructionMotifDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.cession.CessionManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.cession.CessionValidator;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.cession.CederObjet;
import fr.aphp.tumorotek.model.coeur.cession.Cession;
import fr.aphp.tumorotek.model.coeur.cession.CessionExamen;
import fr.aphp.tumorotek.model.coeur.cession.CessionStatut;
import fr.aphp.tumorotek.model.coeur.cession.CessionType;
import fr.aphp.tumorotek.model.coeur.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.cession.DestructionMotif;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Cession.
 * Classe créée le 01/02/10.
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class CessionManagerImpl implements CessionManager
{

   private final Log log = LogFactory.getLog(CessionManager.class);

   private CessionDao cessionDao;
   private BanqueDao banqueDao;
   private CessionTypeDao cessionTypeDao;
   private CessionExamenDao cessionExamenDao;
   private ContratDao contratDao;
   private CollaborateurDao collaborateurDao;
   private ServiceDao serviceDao;
   private CessionStatutDao cessionStatutDao;
   private TransporteurDao transporteurDao;
   private DestructionMotifDao destructionMotifDao;
   // private CessionDelegateDao cessionDelegateDao;
   private CederObjetManager cederObjetManager;
   private OperationTypeDao operationTypeDao;
   private OperationManager operationManager;
   private EntiteDao entiteDao;
   private EntityManagerFactory entityManagerFactory;
   private CessionValidator cessionValidator;
   private AnnotationValeurManager annotationValeurManager;

   public void setCessionDao(final CessionDao cDao){
      this.cessionDao = cDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setCessionTypeDao(final CessionTypeDao cDao){
      this.cessionTypeDao = cDao;
   }

   public void setCessionExamenDao(final CessionExamenDao cDao){
      this.cessionExamenDao = cDao;
   }

   public void setContratDao(final ContratDao cDao){
      this.contratDao = cDao;
   }

   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   public void setServiceDao(final ServiceDao sDao){
      this.serviceDao = sDao;
   }

   public void setCessionStatutDao(final CessionStatutDao cDao){
      this.cessionStatutDao = cDao;
   }

   public void setTransporteurDao(final TransporteurDao tDao){
      this.transporteurDao = tDao;
   }

   public void setDestructionMotifDao(final DestructionMotifDao dDao){
      this.destructionMotifDao = dDao;
   }

//   public void setCessionDelegateDao(CessionDelegateDao cessionDelegateDao){
//      this.cessionDelegateDao = cessionDelegateDao;
//   }

   public void setCederObjetManager(final CederObjetManager cManager){
      this.cederObjetManager = cManager;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setEntityManagerFactory(final EntityManagerFactory eFactory){
      this.entityManagerFactory = eFactory;
   }

   public void setCessionValidator(final CessionValidator cValidator){
      this.cessionValidator = cValidator;
   }

   public void setAnnotationValeurManager(final AnnotationValeurManager avManager){
      this.annotationValeurManager = avManager;
   }

   /**
    * Recherche une Cession dont l'identifiant est passé en paramètre.
    * @param cessionId Identifiant de la Cession que l'on recherche.
    * @return Une Cession.
    */
   @Override
   public Cession findByIdManager(final Integer cessionId){
      return cessionDao.findById(cessionId).orElse(null);
   }

   /**
    * Recherche toutes les Cessions présentes dans la base.
    * @return Liste de Cessions.
    */
   @Override
   public List<Cession> findAllObjectsManager(){
      log.debug("Recherche de toutes les Cessions");
      return IterableUtils.toList(cessionDao.findAll());
   }

   @Override
   public List<Cession> findAllObjectsByBanquesManager(final List<Banque> banques){
      final List<Cession> cessions = new ArrayList<>();
      if(banques != null){
         final Iterator<Banque> it = banques.iterator();
         while(it.hasNext()){
            cessions.addAll(cessionDao.findByBanqueWithOrder(it.next()));
         }
      }
      return cessions;
   }

   @Override
   public List<Integer> findAllObjectsIdsByBanquesManager(final List<Banque> banques){
      if(banques != null && banques.size() > 0){
         return cessionDao.findByBanquesAllIds(banques);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche une liste de Cessions dont le numéro est égal à
    * celui passé en paramètre.
    * @param numero Numéro pour lequel on recherche des Cessions.
    * @return Liste de Cessions.
    */
   @Override
   public List<Cession> findByNumeroLikeManager(String numero, final boolean exactMatch){
      log.debug("Recherche Cession par numero : " + numero);
      if(numero != null){
         if(!exactMatch){
            numero = numero + "%";
         }
         return cessionDao.findByNumero(numero);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findByNumeroWithBanqueReturnIdsManager(String numero, final List<Banque> banques,
      final boolean exactMatch){
      log.debug("Recherche Cession par numero : " + numero);
      if(numero != null){
         if(!exactMatch){
            numero = "%" + numero + "%";
         }
         return cessionDao.findByNumeroWithBanqueReturnIds(numero, banques);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findByStatutWithBanquesReturnIdsManager(final String statut, final List<Banque> banques){

      if(statut != null && banques != null){
         return cessionDao.findByCessionStatutAndBanqueReturnIds(statut, banques);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findByEtatIncompletWithBanquesReturnIdsManager(final boolean incomplet, final List<Banque> banques){

      return cessionDao.findByEtatIncompletAndBanquesReturnIds(incomplet, banques);
   }

   /**
    * Recherche la liste des numéros utilisés par les Cessions liées à la
    * banque passée en paramètre.
    * @param banque Banque pour laquelle on recherche les numéros.
    * @return Liste de numéros.
    */
   @Override
   public List<String> findAllCodesForBanqueManager(final Banque banque){
      log.debug("Recherche de tous les numéros des Cessions d'une banque");
      if(banque != null){
         return cessionDao.findByBanqueSelectNumero(banque);
      }
      return new ArrayList<>();
   }

   /**
    * Renvoie tous les CederObjets d'une cession.
    * @param cession Cession pour laquelle on veut les objets cédés.
    * @return Une liste de CederObjets.
    */
   @Override
   public Set<CederObjet> getCederObjetsManager(Cession cession){

      if(cession != null){
         cession = cessionDao.save(cession);
         final Set<CederObjet> ceders = cession.getCederObjets();
         ceders.size();

         return ceders;
      }
      return new HashSet<>();
   }

   @Override
   public Boolean findDoublonManager(final Cession cession){
      if(cession != null){
         // Banque banque = cession.getBanque();
         final List<Cession> dbls = cessionDao.findByNumeroInPlateforme(cession.getNumero(),
            cession.getBanque() != null ? cession.getBanque().getPlateforme() : null);
         if(!dbls.isEmpty()){
            if(cession.getCessionId() == null){
               return true;
            }
            for(final Cession d : dbls){
               if(!d.getCessionId().equals(cession.getCessionId())){
                  return true;
               }
            }
         }
      }
      return false;

      //			if (cession.getCessionId() == null) {
      //				return cessionDao.findByBanqueSelectNumero(banque)
      //					.contains(cession.getNumero());
      //			} else {
      //				return cessionDao.findByExcludedIdNumeros(
      //						cession.getCessionId(), banque)
      //						.contains(cession.getNumero());
      //			}
      //		} else {
      //			return false;
      //		}
   }

   /**
    * Test si une Cession est lié à des CederObjets ou à des Retours.
    * @param cession Cession que l'on souhaite tester.
    * @return Vrai si la Cession est utilisée.
    */
   @Override
   public Boolean isUsedObjectManager(Cession cession){
      if(cession != null){
         cession = cessionDao.save(cession);
         int size = cession.getCederObjets().size();
         size = size + cession.getRetours().size();
         return (size > 0);
      }
      return false;
   }

   @Override
   public List<Integer> findAfterDateCreationReturnIdsManager(final Calendar date, final List<Banque> banques){
      final List<Integer> liste = new ArrayList<>();
      if(date != null && banques != null){
         log.debug("Recherche des Cessions enregistres apres la date "
            + new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date.getTime()));
         final Iterator<Banque> it = banques.iterator();
         while(it.hasNext()){
            liste.addAll(findByOperationTypeAndDateReturnIds(operationTypeDao.findByNom("Creation").get(0), date, it.next()));
         }
      }
      return liste;
   }

   /**
    * Recherche toutes les Cessions dont la date de modification systeme est 
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @param banque Banque à laquelle appartient la Cession.
    * @return Liste de Cessions.
    */
   @Override
   public List<Cession> findAfterDateModificationManager(final Calendar date, final Banque banque){
      List<Cession> liste = new ArrayList<>();
      if(date != null && banque != null){
         log.debug("Recherche des Cessions modifies apres la date "
            + new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date.getTime()));
         liste = findByOperationTypeAndDate(operationTypeDao.findByNom("Modification").get(0), date, banque);
      }
      return liste;
   }

   /**
    * Recupere la liste des cessions en fonction du type d'operation et 
    * d'une date a laquelle la date d'enregistrement de l'operation doit
    * etre superieure ou egale.
    * Dans un premier temps, recupere la liste des objetIds qui sont ensuite
    * utilises pour recuperer les cessions.
    * @param oType OperationType
    * @param date
    * @param banque Banque à laquelle appartient la cessions.
    * @return List de Cessions.
    */

   private List<Cession> findByOperationTypeAndDate(final OperationType oType, final Calendar date, final Banque banque){
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<Integer> opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
         + "Operation o WHERE o.entite = :entite AND " + "o.operationType = :oType AND date >= :date", Integer.class);
      opQuery.setParameter("entite", entiteDao.findByNom("Cession"));
      opQuery.setParameter("oType", oType);
      opQuery.setParameter("date", date);
      final List<Integer> ids = opQuery.getResultList();
      TypedQuery<Cession> prelQuery;
      if(ids.size() > 1 && banque != null){ //HQL IN () si liste taille > 1
         prelQuery = em.createQuery(
            "SELECT DISTINCT c FROM Cession c " + "WHERE c.cessionId IN (:ids) " + "AND c.banque = :banque", Cession.class);
         prelQuery.setParameter("ids", ids);
         prelQuery.setParameter("banque", banque);
      }else if(ids.size() == 1 && banque != null){
         prelQuery = em.createQuery("SELECT DISTINCT c FROM Cession c " + "WHERE c.cessionId = :id " + "AND c.banque = :banque",
            Cession.class);
         prelQuery.setParameter("id", ids.get(0));
         prelQuery.setParameter("banque", banque);
      }else{
         return new ArrayList<>();
      }

      return prelQuery.getResultList();
   }

   /**
    * Recupere la liste des cessions en fonction du type d'operation et 
    * d'une date a laquelle la date d'enregistrement de l'operation doit
    * etre superieure ou egale.
    * Dans un premier temps, recupere la liste des objetIds qui sont ensuite
    * utilises pour recuperer les cessions.
    * @param oType OperationType
    * @param date
    * @param banque Banque à laquelle appartient la cessions.
    * @return List de Cessions.
    */
   private List<Integer> findByOperationTypeAndDateReturnIds(final OperationType oType, final Calendar date, final Banque banque){
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<Integer> opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
         + "Operation o WHERE o.entite = :entite AND " + "o.operationType = :oType AND date >= :date", Integer.class);
      opQuery.setParameter("entite", entiteDao.findByNom("Cession"));
      opQuery.setParameter("oType", oType);
      opQuery.setParameter("date", date);
      final List<Integer> ids = opQuery.getResultList();
      TypedQuery<Integer> prelQuery;
      if(ids.size() > 1 && banque != null){ //HQL IN () si liste taille > 1
         prelQuery = em.createQuery(
            "SELECT DISTINCT c.cessionId " + "FROM Cession c " + "WHERE c.cessionId IN (:ids) " + "AND c.banque = :banque",
            Integer.class);
         prelQuery.setParameter("ids", ids);
         prelQuery.setParameter("banque", banque);
      }else if(ids.size() == 1 && banque != null){
         prelQuery = em.createQuery(
            "SELECT DISTINCT c.cessionId " + "FROM Cession c " + "WHERE c.cessionId = :id " + "AND c.banque = :banque",
            Integer.class);
         prelQuery.setParameter("id", ids.get(0));
         prelQuery.setParameter("banque", banque);
      }else{
         return new ArrayList<>();
      }

      return prelQuery.getResultList();
   }

   /**
    * Recherche les dernières Cessions créées dans le systeme.
    * @param banque Banque à laquelle appartiennent les Cessions.
    * @param nbResults Nombre de résultats souhaités.
    * @return Liste de Cessions.
    */

   @Override
   public List<Cession> findLastCreationManager(final List<Banque> banques, final int nbResults){

      final List<Cession> liste = new ArrayList<>();
      if(banques != null && !banques.isEmpty() && nbResults > 0){
         log.debug("Recherche des " + nbResults + " dernières Cessions " + "enregistrees.");
         // liste = findByLastOperationType(operationTypeDao
         //		.findByNom("Creation").get(0), banques, nbResults);
         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Cession> query = em.createQuery(
            "SELECT c " + "FROM Cession c " + "WHERE c.banque in (:banque) " + "ORDER BY c.cessionId DESC", Cession.class);
         //			Query query = em.createQuery("SELECT e " 
         //					+ "FROM Echantillon e, Operation o " 
         //					+ "WHERE o.objetId = e.echantillonId " 
         //					+ "AND o.entite = :entite " 
         //					+ "AND o.operationType = :oType " 
         //					+ "AND e.banque in (:banque) " 
         //					+ "ORDER BY o.date DESC");
         //			query.setParameter("entite", entiteDao.findByNom("Echantillon"));
         //			query.setParameter("oType", operationTypeDao
         //												.findByNom("Creation").get(0));
         query.setParameter("banque", banques);
         query.setFirstResult(0);
         query.setMaxResults(nbResults);

         liste.addAll(query.getResultList());
      }
      return liste;

   }

   /**
    * Récupère une liste de cessions en fonction de leur banque et d'un
    * type d'opération. Cette liste est ordonnée par la date de l'opération.
    * Sa taille maximale est fixée par un paramètre.
    * @param oType Type de l'opération.
    * @param banque liste de Banques des cessions.
    * @param nbResults Nombre max de cessions souhaitées.
    * @return Liste de Echantillons.
    */
   //	
   //	private List<Cession> findByLastOperationType(OperationType oType, 
   //			List<Banque> banques, int nbResults) {
   //		
   //		List<Cession> cessions = new ArrayList<Cession>();
   //		
   //		if (banques.size() > 0) {
   //			EntityManager em = entityManagerFactory.createEntityManager();
   //			Query query = em.createQuery("SELECT c " 
   //					+ "FROM Cession c, Operation o " 
   //					+ "WHERE o.objetId = c.cessionId " 
   //					+ "AND o.entite = :entite " 
   //					+ "AND o.operationType = :oType " 
   //					+ "AND c.banque in (:banques) "
   //					+ "ORDER BY o.date DESC");
   //			query.setParameter("entite", entiteDao.findByNom("Cession"));
   //			query.setParameter("oType", oType);
   //			query.setParameter("banques", banques);
   //			query.setMaxResults(nbResults);
   //			
   //			cessions.addAll(query.getResultList());
   //		}
   //		
   //		return cessions;
   //		
   //	}

   /**
    * Verifie que les Objets devant etre obligatoirement associes 
    * sont non nulls et lance la validation via le Validator.
    * @param cession
    * @param banque
    * @param cessionType
    * @param cessionStatut
    * @param operation
    */
   private void checkRequiredObjectsAndValidate(final Cession cession, final Banque banque, final CessionType cessionType,
      final CessionStatut cessionStatut, final String operation){

      //Banque required
      if(banque != null){
         cession.setBanque(banqueDao.save(banque));
      }else{
         log.warn("Objet obligatoire Banque manquant" + " lors de la " + operation + " d'une Cession");
         throw new RequiredObjectIsNullException("Cession", operation, "Banque");
      }

      //CessionType required
      if(cessionType != null){
         cession.setCessionType(cessionTypeDao.save(cessionType));
      }else{
         log.warn("Objet obligatoire CessionType manquant" + " lors de la " + operation + " d'une Cession");
         throw new RequiredObjectIsNullException("Cession", operation, "CessionType");
      }

      //CessionStatut required
      if(cessionStatut != null){
         cession.setCessionStatut(cessionStatutDao.save(cessionStatut));
      }else{
         log.warn("Objet obligatoire CessionStatut manquant" + " lors de la " + operation + " d'une Cession");
         throw new RequiredObjectIsNullException("Cession", operation, "CessionStatut");
      }
   }

   /**
    * Merge et assigne tous les objects associes non obligatoires à 
    * à la cession (sauf partie Contrat, sujette à la validation).
    * @param cession
    * @param cessionExamen
    * @param destinataire
    * @param servDest
    * @param demandeur
    * @param executant
    * @param transporteur
    * @param destructionMotif
    * @param contrat
    */
   private void mergeNonRequiredObjects(final Cession cession, final CessionExamen cessionExamen,
      final Collaborateur destinataire, final Service servDest, final Collaborateur demandeur, final Collaborateur executant,
      final Transporteur transporteur, final DestructionMotif destructionMotif, final Contrat contrat){

      // cession.setDelegate( cessionDelegateDao.save( cession.getDelegate() ) );
      cession.setCessionExamen(cessionExamenDao.save(cessionExamen));
      cession.setDestinataire(collaborateurDao.save(destinataire));
      cession.setServiceDest(serviceDao.save(servDest));
      cession.setDemandeur(collaborateurDao.save(demandeur));
      cession.setExecutant(collaborateurDao.save(executant));
      cession.setTransporteur(transporteurDao.save(transporteur));
      cession.setDestructionMotif(destructionMotifDao.save(destructionMotif));
      cession.setContrat(contratDao.save(contrat));

   }

   @Override
   public void createObjectManager(final Cession cession, final Banque banque, final CessionType cessionType,
      final CessionExamen cessionExamen, final Contrat contrat, final Collaborateur destinataire, final Service servDest,
      final Collaborateur demandeur, final CessionStatut cessionStatut, final Collaborateur executant,
      final Transporteur transporteur, final DestructionMotif destructionMotif,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final List<File> filesCreated, final Utilisateur utilisateur,
      final List<CederObjet> cederObjets, final String baseDir){

      //Verifie required Objects associes et validation
      checkRequiredObjectsAndValidate(cession, banque, cessionType, cessionStatut, "creation");

      //Doublon
      if(findDoublonManager(cession)){
         throw new DoublonFoundException("Cession", "creation", cession.getNumero(), null);
      }
      try{
         // validation du Contrat
         BeanValidator.validateObject(cession, new Validator[] {cessionValidator});

         if(cederObjets != null){
            for(int i = 0; i < cederObjets.size(); i++){
               final CederObjet obj = cederObjets.get(i);

               cederObjetManager.validateObjectManager(obj, cession, obj.getEntite(), obj.getQuantiteUnite(), "creation");
            }
         }

         mergeNonRequiredObjects(cession, /*banque,*/ cessionExamen, destinataire, servDest, demandeur, executant, transporteur,
            destructionMotif, contrat);

         cessionDao.save(cession);
         log.info("Enregistrement objet Cession " + cession.toString());

         //Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Creation").get(0), cession);

         // enregistrements des cederobjets
         if(cederObjets != null){
            for(int i = 0; i < cederObjets.size(); i++){
               final CederObjet obj = cederObjets.get(i);
               cederObjetManager.createObjectManager(obj, cession, obj.getEntite(), obj.getQuantiteUnite());
            }
         }

         // cree les annotations, null operation pour
         // laisser la possibilité création/modification au sein 
         // de la liste
         if(listAnnoToCreateOrUpdate != null){
            annotationValeurManager.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, cession, utilisateur, null,
               baseDir, filesCreated, null);
         }
      }catch(final RuntimeException re){
         if(filesCreated != null){
            for(final File f : filesCreated){
               f.delete();
            }
         }else{
            log.warn("Rollback création fichier n'a pas pu être réalisée");
         }
         // en cas d'erreur lors enregistrement annotation
         // le rollback se fera mais echantillon aura un id assigne
         // qui déclenchera une TransientException si on essaie 
         // d'enregistrer a nouveau.
         if(cession.getCessionId() != null){
            cession.setCessionId(null);
         }
         throw (re);
      }
   }

   @Override
   public void updateObjectManager(final Cession cession, final Banque banque, final CessionType cessionType,
      final CessionExamen cessionExamen, final Contrat contrat, final Collaborateur destinataire, final Service servDest,
      final Collaborateur demandeur, final CessionStatut cessionStatut, final Collaborateur executant,
      final Transporteur transporteur, final DestructionMotif destructionMotif,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final List<AnnotationValeur> listAnnoToDelete,
      final List<File> filesCreated, final List<File> filesToDelete, final Utilisateur utilisateur,
      final List<CederObjet> cederObjets, final String baseDir){

      //Verifie required Objects associes et validation
      checkRequiredObjectsAndValidate(cession, banque, cessionType, cessionStatut, "modification");

      //Doublon
      if(findDoublonManager(cession)){
         throw new DoublonFoundException("Cession", "modification", cession.getNumero(), null);
      }

      try{
         // validation du Contrat
         BeanValidator.validateObject(cession, new Validator[] {cessionValidator});

         if(cederObjets != null){
            for(final CederObjet obj : cederObjets){
               cederObjetManager.validateObjectManager(obj, cession, obj.getEntite(), obj.getQuantiteUnite(), "modification");
            }
         }

         mergeNonRequiredObjects(cession, /*banque,*/ cessionExamen, destinataire, servDest, demandeur, executant, transporteur,
            destructionMotif, contrat);

         cessionDao.save(cession);
         log.info("Modification objet Cession " + cession.toString());

         //Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Modification").get(0),
            cession);

         // enregistrements des cederobjets
         if(cederObjets != null){
            for(final CederObjet obj : cederObjets){
               if(cederObjetManager.findByIdManager(obj.getPk()) == null){
                  cederObjetManager.createObjectManager(obj, cession, obj.getEntite(), obj.getQuantiteUnite());
               }else{
                  cederObjetManager.updateObjectManager(obj, cession, obj.getEntite(), obj.getQuantiteUnite());
               }
            }

            // suppression des anciens CederObjet qui n'existent
            // plus
            final Set<CederObjet> oldCedes = getCederObjetsManager(cession);
            for(CederObjet tmp : oldCedes){
               if(!cederObjets.contains(tmp)){
                  cederObjetManager.removeObjectManager(tmp);
               }
            }
         }else{
            // suppression des anciens CederObjets qui n'existent
            // plus
            final Set<CederObjet> oldCedes = getCederObjetsManager(cession);
            for(CederObjet tmp : oldCedes){
               cederObjetManager.removeObjectManager(tmp);
            }
         }
         // Annotations
         // suppr les annotations
         if(listAnnoToDelete != null){
            annotationValeurManager.removeAnnotationValeurListManager(listAnnoToDelete, filesToDelete);
         }

         // update les annotations, null operation pour
         // laisser la possibilité création/modification au sein 
         // de la liste
         if(listAnnoToCreateOrUpdate != null){
            annotationValeurManager.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, cession, utilisateur, null,
               baseDir, filesCreated, filesToDelete);
         }
         // enregistre operation associee annotation 
         // si il y a eu des deletes et pas d'updates
         if((listAnnoToCreateOrUpdate == null || listAnnoToCreateOrUpdate.isEmpty())
            && (listAnnoToDelete != null && !listAnnoToDelete.isEmpty())){
            CreateOrUpdateUtilities.createAssociateOperation(cession, operationManager,
               operationTypeDao.findByNom("Annotation").get(0), utilisateur);
         }

         if(filesToDelete != null){
            for(final File f : filesToDelete){
               f.delete();
            }
         }

      }catch(final RuntimeException re){
         // rollback au besoin...
         if(filesCreated != null){
            for(final File f : filesCreated){
               f.delete();
            }
         }else{
            log.warn("Rollback création fichier n'a pas pu être réalisée");
         }
         throw (re);
      }
   }

   @Override
   public void removeObjectManager(final Cession cession, final String comments, final Utilisateur usr,
      final List<File> filesToDelete){
      if(cession != null){
         // suppression des CederObjets
         final Set<CederObjet> cederObjets = getCederObjetsManager(cession);
         if(cederObjets != null){
            final Iterator<CederObjet> it = cederObjets.iterator();
            while(it.hasNext()){
               cederObjetManager.removeObjectManager(it.next());
            }
         }

         cessionDao.deleteById(cession.getCessionId());
         log.info("Suppression de l'objet Cession : " + cession.toString());

         //Supprime operations associes
         CreateOrUpdateUtilities.removeAssociateOperations(cession, operationManager, comments, usr);

         //Supprime annotations associes
         annotationValeurManager.removeAnnotationValeurListManager(annotationValeurManager.findByObjectManager(cession),
            filesToDelete);

      }else{
         log.warn("Suppression d'une Cession null");
      }
   }

   @Override
   public List<Cession> findByIdsInListManager(final List<Integer> ids){
      if(ids != null && ids.size() > 0){
         return cessionDao.findByIdInList(ids);
      }
      return new ArrayList<>();
   }

   @Override

   public Map<String, Number> getTypesAndCountsManager(final Cession cession){

      final Map<String, Number> out = new HashMap<>();

      if(cession != null && cession.getCessionId() != null){
         final EntityManager em = entityManagerFactory.createEntityManager();

         // Echantillons
         Query query = em.createQuery("SELECT new map(e.echantillonType.nom as tp, "
            + "count(e.echantillonId) as ct) FROM Echantillon e, CederObjet c " + "WHERE c.pk.objetId = e.echantillonId "
            + "AND c.pk.entite.entiteId = 3 AND c.pk.cession = :cession " + "GROUP BY e.echantillonType.nom");
         query.setParameter("cession", cession);

         List<?> res = query.getResultList();
         //         List<Map<String, Object>> res = query.getResultList();

         for(final Object resObj : res){
            Map<?, ?> map = Map.class.cast(resObj);
            out.put(String.class.cast(map.get("tp")), Number.class.cast(map.get("ct")));
         }

         // derives: additionne les comptes si type identiques avec échantillons
         query = em.createQuery("SELECT new map(p.prodType.nom as tp, "
            + "count(p.prodDeriveId) as ct) FROM ProdDerive p, CederObjet c " + "WHERE c.pk.objetId = p.prodDeriveId "
            + "AND c.pk.entite.entiteId = 8 AND c.pk.cession = :cession " + "GROUP BY p.prodType.nom");
         query.setParameter("cession", cession);

         res = query.getResultList();
         for(final Object resObj : res){
            Map<?, ?> map = Map.class.cast(resObj);
            out.put((String) map.get("tp"), !out.containsKey(map.get("tp")) ? (Number) map.get("ct")
               : new Integer(((Number) map.get("ct")).intValue() + out.get(map.get("tp")).intValue()));
         }

      }
      return out;
   }

   @Override
   public Long findCountByDemandeurManager(final Collaborateur colla){
      if(colla != null){
         return cessionDao.findCountByDemandeur(colla).get(0);
      }
      return new Long(0);
   }

   @Override
   public Long findCountByDestinataireManager(final Collaborateur colla){
      if(colla != null){
         return cessionDao.findCountByDestinataire(colla).get(0);
      }
      return new Long(0);
   }

   @Override
   public Long findCountByExecutantManager(final Collaborateur colla){
      if(colla != null){
         return cessionDao.findCountByExecutant(colla).get(0);
      }
      return new Long(0);
   }

   @Override
   public void removeListFromIdsManager(final List<Integer> ids, final String comment, final Utilisateur u){
      if(ids != null){
         final List<File> filesToDelete = new ArrayList<>();
         Cession c;
         for(final Integer id : ids){
            c = findByIdManager(id);
            if(c != null){
               removeObjectManager(c, comment, u, filesToDelete);
            }
         }
         //         if(filesToDelete != null){
         for(final File f : filesToDelete){
            f.delete();
         }
         //         }
      }
   }

   @Override
   public List<Cession> findByNumeroInPlateformeManager(final String numero, final Plateforme pf){
      return cessionDao.findByNumeroInPlateforme(numero, pf);
   }

   @Override
   public void applyScanCheckDateManager(final Cession cess, final Calendar scanDate){
      if(cess != null && cess.getCessionId() != null){
         cess.setLastScanCheckDate(scanDate);
         cessionDao.save(cess);
      }
   }
}
