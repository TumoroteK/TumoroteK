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
package fr.aphp.tumorotek.manager.impl.coeur.echantillon;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchanQualiteDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
// import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDelegateDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonTypeDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.ModePrepaDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.TransformationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.stockage.EmplacementDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.manager.code.CodeAssigneManager;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonValidator;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.impl.systeme.MvFichier;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.systeme.FichierManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.echantillon.gatsbi.EchantillonGatsbiValidator;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Echantillon.
 * Classe créée le 25/09/09.
 *
 * @author Pierre Ventadour
 * @author Mathieu Barthélémy
 * @version 2.3.0-gatsbi
 *
 */
public class EchantillonManagerImpl implements EchantillonManager
{

   private final Log log = LogFactory.getLog(EchantillonManager.class);

   private EchantillonDao echantillonDao;
   // private EchantillonDelegateDao delegateDao;
   private TransformationManager transformationManager;
   private TransformationDao transformationDao;
   private BanqueDao banqueDao;
   private PrelevementDao prelevementDao;
   private CollaborateurDao collaborateurDao;
   private ObjetStatutDao objetStatutDao;
   private EmplacementDao emplacementDao;
   private EchantillonTypeDao echantillonTypeDao;
   private UniteDao uniteDao;
   private EchanQualiteDao echanQualiteDao;
   private ModePrepaDao modePrepaDao;
   private EchantillonValidator echantillonValidator;
   private OperationTypeDao operationTypeDao;
   private OperationManager operationManager;
   private EntityManagerFactory entityManagerFactory;
   private EntiteDao entiteDao;
   private EmplacementManager emplacementManager;
   private CodeAssigneManager codeAssigneManager;
   private AnnotationValeurManager annotationValeurManager;
   private FichierManager fichierManager;
   private ProdDeriveManager prodDeriveManager;
   private CederObjetManager cederObjetManager;
   private ImportHistoriqueManager importHistoriqueManager;
   private ConteneurManager conteneurManager;
   private RetourManager retourManager;
   private ObjetNonConformeManager objetNonConformeManager;

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setTransformationManager(final TransformationManager tManager){
      this.transformationManager = tManager;
   }

   public void setTransformationDao(final TransformationDao tDao){
      this.transformationDao = tDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setPrelevementDao(final PrelevementDao pDao){
      this.prelevementDao = pDao;
   }

   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   public void setObjetStatutDao(final ObjetStatutDao oDao){
      this.objetStatutDao = oDao;
   }

   public void setEmplacementDao(final EmplacementDao eDao){
      this.emplacementDao = eDao;
   }

   public void setEchantillonTypeDao(final EchantillonTypeDao eDao){
      this.echantillonTypeDao = eDao;
   }

   public void setUniteDao(final UniteDao uDao){
      this.uniteDao = uDao;
   }

   public void setEchanQualiteDao(final EchanQualiteDao eDao){
      this.echanQualiteDao = eDao;
   }

   public void setModePrepaDao(final ModePrepaDao mDao){
      this.modePrepaDao = mDao;
   }

   public void setEchantillonValidator(final EchantillonValidator validator){
      this.echantillonValidator = validator;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setEntityManagerFactory(final EntityManagerFactory eFactory){
      this.entityManagerFactory = eFactory;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setEmplacementManager(final EmplacementManager eManager){
      this.emplacementManager = eManager;
   }

   public void setCodeAssigneManager(final CodeAssigneManager cManager){
      this.codeAssigneManager = cManager;
   }

   public void setAnnotationValeurManager(final AnnotationValeurManager aVManager){
      this.annotationValeurManager = aVManager;
   }

   public void setFichierManager(final FichierManager fManager){
      this.fichierManager = fManager;
   }

   public void setProdDeriveManager(final ProdDeriveManager pManager){
      this.prodDeriveManager = pManager;
   }

   public void setCederObjetManager(final CederObjetManager coManager){
      this.cederObjetManager = coManager;
   }

   public void setImportHistoriqueManager(final ImportHistoriqueManager iManager){
      this.importHistoriqueManager = iManager;
   }

   public void setConteneurManager(final ConteneurManager cManager){
      this.conteneurManager = cManager;
   }

   public void setRetourManager(final RetourManager rManager){
      this.retourManager = rManager;
   }

   public void setObjetNonConformeManager(final ObjetNonConformeManager oM){
      this.objetNonConformeManager = oM;
   }

//   public void setDelegateDao(EchantillonDelegateDao delegateDao){
//      this.delegateDao = delegateDao;
//   }

   @Override
   public Echantillon findByIdManager(final Integer echantillonId){
      final Echantillon e = echantillonDao.findById(echantillonId);
      return e;
   }

   @Override
   public List<Echantillon> findAllObjectsManager(){
      log.debug("Recherche de tous les Echantillons");
      return echantillonDao.findAll();
   }

   @Override
   public List<Echantillon> findByIdsInListManager(final List<Integer> ids){
      if(ids != null && ids.size() > 0){
         return echantillonDao.findByIds(ids);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Echantillon> findByBanquesManager(final List<Banque> banks){
      List<Echantillon> echans = new ArrayList<>();
      if(banks != null && !banks.isEmpty()){
         echans = echantillonDao.findByBanques(banks);
      }
      return echans;
   }

   @Override
   public List<Integer> findAllObjectsIdsByBanquesManager(final List<Banque> banques){
      if(banques != null && banques.size() > 0){
         return echantillonDao.findByBanquesAllIds(banques);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche une liste d'échantillons dont le type est passé en paramètre.
    * @param echantillonType EchantillonType pour lequel on recherche des
    * échantillons.
    * @return Liste d'Echantillons.
    */
   @Override
   public List<Echantillon> findByEchantillonTypeManager(final EchantillonType type){
      return echantillonDao.findByEchantillonType(type);
   }

   @Override
   public List<Echantillon> findByDateStockAfterDateManager(final Date date){
      Calendar cal = null;
      if(date != null){
         log.debug(
            "Recherche des Echantillons stockés après la date " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(date));
         cal = Calendar.getInstance();
         cal.setTime(date);
      }
      return echantillonDao.findByDateStockAfterDate(cal);
   }

   @Override
   public List<Echantillon> findByDateStockAfterDateWithBanqueManager(final Date date, final List<Banque> banques){
      final List<Echantillon> echans = new ArrayList<>();
      Calendar cal = null;
      if(date != null && banques != null){
         log.debug(
            "Recherche des Echantillons stockés après la date " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(date));
         cal = Calendar.getInstance();
         cal.setTime(date);

         final Iterator<Banque> it = banques.iterator();
         while(it.hasNext()){
            echans.addAll(echantillonDao.findByDateStockAfterDateWithBanque(cal, it.next()));
         }
      }
      return echans;
   }

   @Override
   public List<Echantillon> findRestantsByPrelevementManager(final Prelevement pl){
      return echantillonDao.findRestantsByPrelevement(pl);
   }

   @Override
   public List<Echantillon> findByPrelevementAndStatutManager(final Prelevement p, final ObjetStatut statut){
      if(p != null && statut != null){
         return echantillonDao.findByPrelevementAndStatut(p, statut);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche une liste d'échantillons dont le code commence comme
    * celui passé en paramètre.
    * @param code Code pour lequel on recherche des échantillons.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste d'Echantillons.
    */
   @Override
   public List<Echantillon> findByCodeLikeManager(String code, final boolean exactMatch){
      log.debug("Recherche Echantillon par " + code + " exactMatch " + String.valueOf(exactMatch));
      if(!exactMatch){
         code = code + "%";
      }
      return echantillonDao.findByCode(code);
   }

   /**
    * Recherche une liste d'échantillons dont le code commence comme
    * celui passé en paramètre.
    * @param code Code pour lequel on recherche des échantillons.
    * @param banque Banque à laquelle appartient l'échantillon.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste d'Echantillons.
    */
   @Override
   public List<Echantillon> findByCodeLikeWithBanqueManager(String code, final Banque banque, final boolean exactMatch){
      log.debug("Recherche Echantillon par " + code + " exactMatch " + String.valueOf(exactMatch));
      if(banque != null){
         if(!exactMatch){
            code = code + "%";
         }
         return echantillonDao.findByCodeWithBanque(code, banque);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findByCodeLikeBothSideWithBanqueReturnIdsManager(String code, final List<Banque> banques,
      final boolean exactMatch){

      log.debug("Recherche Echantillon par " + code + " exactMatch " + String.valueOf(exactMatch));
      final List<Integer> echans = new ArrayList<>();
      if(banques != null){
         if(!exactMatch){
            code = "%" + code + "%";
         }
         final Iterator<Banque> it = banques.iterator();
         while(it.hasNext()){
            echans.addAll(echantillonDao.findByCodeWithBanqueReturnIds(code, it.next()));
         }
      }
      return echans;
   }

   @Override
   public List<Integer> findByPatientNomReturnIdsManager(String nom, final List<Banque> banks, final boolean exactMatch){
      final List<Integer> res = new ArrayList<>();
      if(nom != null && banks != null){
         if(!exactMatch){
            nom = "%" + nom + "%";
         }
         for(int i = 0; i < banks.size(); i++){
            res.addAll(echantillonDao.findByPatientNomReturnIds(nom, banks.get(i)));
         }
      }
      return res;
   }

   @Override
   public Prelevement getPrelevementManager(final Echantillon echantillon){
      Prelevement parent = null;
      if(echantillon != null){
         final List<Prelevement> prels = prelevementDao.findByEchantillonId(echantillon.getEchantillonId());
         if(!prels.isEmpty()){
            parent = prels.get(0);
         }
      }
      return parent;
   }

   @Override
   public List<ProdDerive> getProdDerivesManager(final Echantillon echantillon){

      if(echantillon != null){
         final List<Transformation> list = transformationManager.findByParentManager(echantillon);
         final List<ProdDerive> derives = new ArrayList<>();

         for(int i = 0; i < list.size(); ++i){
            final Transformation transfo = transformationDao.mergeObject(list.get(i));
            final Set<ProdDerive> tmp = transfo.getProdDerives();
            final Iterator<ProdDerive> it = tmp.iterator();
            while(it.hasNext()){
               final ProdDerive current = it.next();
               if(!derives.contains(current)){
                  derives.add(current);
               }
            }
         }

         return derives;
      }
      return new ArrayList<>();
   }

   @Override
   public Emplacement getEmplacementManager(Echantillon echantillon){
      if(echantillon != null){
         echantillon = echantillonDao.mergeObject(echantillon);
         
         final Emplacement empl = echantillon.getEmplacement();

         if(empl != null){
            empl.getPosition();
         }
         return empl;
      }
      return null;
   }

   @Override
   public String getEmplacementAdrlManager(Echantillon echantillon){
      if(echantillon != null){
         echantillon = echantillonDao.mergeObject(echantillon);

         return emplacementManager.getAdrlManager(echantillon.getEmplacement(), false);

      }
      return "";
   }

   @Override
   public List<String> findAllCodesForBanqueManager(final Banque banque){
      if(banque != null){
         return echantillonDao.findByBanqueSelectCode(banque);
      }
      return new ArrayList<>();
   }

   @Override
   public List<String> findAllCodesForBanqueAndStockesManager(final Banque banque){
      final ObjetStatut stocke = objetStatutDao.findByStatut("STOCKE").get(0);

      if(stocke != null && banque != null){
         return echantillonDao.findByBanqueStatutSelectCode(banque, stocke);
      }
      return new ArrayList<>();
   }

   @Override
   public List<String> findAllCodesForMultiBanquesAndStockesManager(final List<Banque> banques){
      final ObjetStatut stocke = objetStatutDao.findByStatut("STOCKE").get(0);

      if(stocke != null && banques != null){
         return echantillonDao.findByBanqueInListStatutSelectCode(banques, stocke);
      }
      return new ArrayList<>();
   }

   @Override
   public List<String> findAllCodesForBanqueAndQuantiteManager(final Banque banque){
      if(banque != null){
         return echantillonDao.findByBanqueAndQuantiteSelectCode(banque);
      }
      return new ArrayList<>();
   }
   
   @Override
   public List<String> findAllCodesForDerivesByBanque(final Banque banque){
      if(banque != null){
         return echantillonDao.findAllCodesByBanqueAndQuantiteNotNullOrInCessionTraitement(banque);
      }
      return new ArrayList<>();
   }

   /**
    * Manager qui recherche les doublons de l'Echantillon passé en paramètre.
    * @param echantillon Un Echantillon pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonManager(final Echantillon echantillon){
      final List<Echantillon> dbls = echantillonDao.findByCodeInPlateforme(echantillon.getCode(),
         echantillon.getBanque() != null ? echantillon.getBanque().getPlateforme() : null);

      if(!dbls.isEmpty()){
         if(echantillon.getEchantillonId() == null){
            return true;
         }

         for(final Echantillon d : dbls){
            if(!d.getEchantillonId().equals(echantillon.getEchantillonId())){
               return true;
            }
         }
      }
      return false;

   }

   @Override
   public Boolean isUsedObjectManager(final Echantillon echantillon){
      final List<Transformation> list = transformationManager.findByParentManager(echantillon);

      return (list.size() > 0);
   }

   @Override
   public Boolean isCessedObjectManager(final Echantillon echantillon){
      return (cederObjetManager.findByObjetManager(echantillon).size() > 0);
   }

   @Override
   public List<Integer> findAfterDateCreationReturnIdsManager(final Calendar date, final List<Banque> banques){
      final List<Integer> liste = new ArrayList<>();
      if(date != null && banques != null){
         log.debug("Recherche des Echantillons enregistres apres la date "
            + new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date.getTime()));
         final Iterator<Banque> it = banques.iterator();
         while(it.hasNext()){
            liste.addAll(findByOperationTypeAndDateReturnIds(operationTypeDao.findByNom("Creation").get(0), date, it.next()));
         }
      }
      return liste;
   }

   /**
    * Recherche tous les échantillons dont la date de modification systeme est 
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @param banque Banque à laquelle appartient l'échantillon.
    * @return Liste d'Echantillon.
    */
   @Override
   public List<Echantillon> findAfterDateModificationManager(final Calendar date, final Banque banque){
      List<Echantillon> liste = new ArrayList<>();
      if(date != null && banque != null){
         log.debug("Recherche des Echantillons modifies apres la date "
            + new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date.getTime()));
         liste = findByOperationTypeAndDate(operationTypeDao.findByNom("Modification").get(0), date, banque);
      }
      return liste;
   }

   /**
    * Recupere la liste d'échantillons en fonction du type d'operation et 
    * d'une date a laquelle la date d'enregistrement de l'operation doit
    * etre superieure ou egale.
    * Dans un premier temps, recupere la liste des objetIds qui sont ensuite
    * utilises pour recuperer les échantillons.
    * @param oType OperationType
    * @param date
    * @param banque Banque à laquelle appartient l'échantillon.
    * @return List de Echantillon.
    */
   private List<Echantillon> findByOperationTypeAndDate(final OperationType oType, final Calendar date, final Banque banque){
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<Integer> opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
         + "Operation o WHERE o.entite = :entite AND " + "o.operationType = :oType AND date >= :date", Integer.class);
      opQuery.setParameter("entite", entiteDao.findByNom("Echantillon"));
      opQuery.setParameter("oType", oType);
      opQuery.setParameter("date", date);
      final List<Integer> ids = opQuery.getResultList();
      TypedQuery<Echantillon> prelQuery;
      if(ids.size() > 1 && banque != null){ //HQL IN () si liste taille > 1
         prelQuery = em.createQuery(
            "SELECT DISTINCT e FROM Echantillon e " + "WHERE e.echantillonId IN (:ids) " + "AND e.banque = :banque",
            Echantillon.class);
         prelQuery.setParameter("ids", ids);
         prelQuery.setParameter("banque", banque);
      }else if(ids.size() == 1 && banque != null){
         prelQuery =
            em.createQuery("SELECT DISTINCT e FROM Echantillon e " + "WHERE e.echantillonId = :id " + "AND e.banque = :banque",
               Echantillon.class);
         prelQuery.setParameter("id", ids.get(0));
         prelQuery.setParameter("banque", banque);
      }else{
         return new ArrayList<>();
      }

      return prelQuery.getResultList();
   }

   /**
    * Recupere la liste d'Ids d'échantillons en fonction du type 
    * d'operation et 
    * d'une date a laquelle la date d'enregistrement de l'operation doit
    * etre superieure ou egale.
    * Dans un premier temps, recupere la liste des objetIds qui sont ensuite
    * utilises pour recuperer les échantillons.
    * @param oType OperationType
    * @param date
    * @param banque Banque à laquelle appartient l'échantillon.
    * @return List de Echantillon.
    */

   private List<Integer> findByOperationTypeAndDateReturnIds(final OperationType oType, final Calendar date, final Banque banque){
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<Integer> opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
         + "Operation o WHERE o.entite = :entite AND " + "o.operationType = :oType AND date >= :date", Integer.class);
      opQuery.setParameter("entite", entiteDao.findByNom("Echantillon"));
      opQuery.setParameter("oType", oType);
      opQuery.setParameter("date", date);
      final List<Integer> ids = opQuery.getResultList();
      TypedQuery<Integer> prelQuery;
      if(ids.size() > 1 && banque != null){ //HQL IN () si liste taille > 1
         prelQuery = em.createQuery("SELECT DISTINCT e.echantillonId " + "FROM Echantillon e "
            + "WHERE e.echantillonId IN (:ids) " + "AND e.banque = :banque", Integer.class);
         prelQuery.setParameter("ids", ids);
         prelQuery.setParameter("banque", banque);
      }else if(ids.size() == 1 && banque != null){
         prelQuery = em.createQuery("SELECT DISTINCT e.echantillonId " + "FROM Echantillon e " + "WHERE e.echantillonId = :id "
            + "AND e.banque = :banque", Integer.class);
         prelQuery.setParameter("id", ids.get(0));
         prelQuery.setParameter("banque", banque);
      }else{
         return new ArrayList<>();
      }

      return prelQuery.getResultList();
   }

   @Override
   public List<Echantillon> findLastCreationManager(final List<Banque> banques, final int nbResults){

      final List<Echantillon> liste = new ArrayList<>();
      if(banques != null && banques.size() > 0 && nbResults > 0){
         log.debug("Recherche des " + nbResults + " derniers Echantillons " + "enregistres.");
         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Echantillon> query =
            em.createQuery("SELECT e " + "FROM Echantillon e " + "WHERE e.banque in (:banque) " + "ORDER BY e.echantillonId DESC",
               Echantillon.class);
         query.setParameter("banque", banques);
         query.setFirstResult(0);
         query.setMaxResults(nbResults);

         liste.addAll(query.getResultList());
      }
      return liste;

   }

   @Override
   public void createObjectManager(final Echantillon echantillon, final Banque banque, final Prelevement prelevement,
      final Collaborateur collaborateur, final ObjetStatut statut, final Emplacement emplacement, final EchantillonType type,
      final List<CodeAssigne> codes, final Unite quantite, final EchanQualite qualite, final ModePrepa preparation,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final List<File> filesCreated,
      final Utilisateur utilisateur, final boolean doValidation, final String baseDir, final boolean isImport){

      try{

         mergeNonRequiredObjects(echantillon, prelevement, collaborateur, emplacement, quantite, qualite, preparation);

         checkRequiredObjectsAndValidate(echantillon, banque, type, statut, "creation", utilisateur, codes, doValidation,
            isImport);

         if(!findDoublonManager(echantillon)){

            echantillonDao.createObject(echantillon);
            log.info("Enregistrement de l'objet Echantillon : " + echantillon.toString());

            // Enregistrement de l'operation associee
            final Operation creationOp = new Operation();
            creationOp.setDate(Utils.getCurrentSystemCalendar());
            operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Creation").get(0),
               echantillon);

            // ajout/update association vers codes assignes
            if(codes != null){
               try{
                  echantillon.setCodesAssignes(
                     new HashSet<>(createOrUpdateCodesAssignesManager(echantillon, codes, true, utilisateur, null)));
               }catch(final SQLException e){
                  // non reachable code
               }
            }

            // cree les annotations, null operation pour
            // laisser la possibilité création/modification au sein
            // de la liste
            if(listAnnoToCreateOrUpdate != null){
               annotationValeurManager.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, echantillon, utilisateur, null,
                  baseDir, filesCreated, null);
            }
         }else{ // doublon
            log.warn("Doublon lors de la creation de l'objet Echantillon : " + echantillon.toString());
            throw new DoublonFoundException("Echantillon", "creation", echantillon.getCode(), null);
         }
      }catch(final RuntimeException re){

         if(filesCreated != null){
            for(final File f : filesCreated){
               f.delete();
            }
         }else{
            log.warn("Rollback création fichier n'a pas pu être réalisée");
         }

         // en cas d'erreur lors enregistrement d'un code ou annotation
         // le rollback se fera mais echantillon aura un id assigne
         // qui déclenchera une TransientException si on essaie 
         // d'enregistrer a nouveau.
         if(!isImport){
            if(echantillon.getEchantillonId() != null){
               echantillon.setEchantillonId(null);
            }
         }
         throw (re);
      }
   }

   @Override
   public void createObjectWithCrAnapathManager(final Echantillon echantillon, final Banque banque, final Prelevement prelevement,
      final Collaborateur collaborateur, final ObjetStatut statut, final Emplacement emplacement, final EchantillonType type,
      final List<CodeAssigne> codes, final Unite quantite, final EchanQualite qualite, final ModePrepa preparation,
      Fichier anapath, final InputStream anapathStream, final List<File> filesCreated,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final Utilisateur utilisateur, final boolean doValidation,
      final String baseDir, final boolean isImport){

      try{
         createObjectManager(echantillon, banque, prelevement, collaborateur, statut, emplacement, type, codes, quantite, qualite,
            preparation, listAnnoToCreateOrUpdate, filesCreated, utilisateur, doValidation, baseDir, isImport);

         if(baseDir != null){
            fichierManager.createOrUpdateFileForObject(echantillon, anapath, anapathStream,
               writeCrAnapathFilePath(baseDir, echantillon.getBanque(), anapath), filesCreated, null);
            echantillonDao.updateObject(echantillon);
         }

      }catch(final RuntimeException re){

         if(!isImport){
            if(echantillon.getEchantillonId() != null){
               echantillon.setEchantillonId(null);
            }

            // supprime un cr enregistré en cas d'erreur
            if(anapath != null){
               if(filesCreated != null){
                  for(final File f : filesCreated){
                     f.delete();
                  }
               }else{
                  log.warn("Rollback création fichier n'a pas pu être réalisée");
               }
               if(anapathStream != null){
                  try{
                     anapathStream.reset();
                  }catch(final IOException e){
                     log.error(e);
                  }
               }
               anapath = anapath.clone();
               anapath.setFichierId(null);
               if(anapath.getPath() != null){
                  anapath.setPath(anapath.getPath().substring(0, anapath.getPath().lastIndexOf("_")));
               }
            }
         }
         throw re;
      }

   }

   @Override
   public Integer prepareObjectJDBCManager(final EchantillonJdbcSuite jdbcSuite, final Echantillon echantillon,
      final Banque banque, final Prelevement prelevement, final Collaborateur collaborateur, final ObjetStatut statut,
      final Emplacement emplacement, final EchantillonType type, final Unite quantite, final EchanQualite qualite,
      final ModePrepa preparation, final List<CodeAssigne> codes, final List<AnnotationValeur> listAnnoToCreateOrUpdate,
      final List<NonConformite> noconfsTrait, final List<NonConformite> noconfsCess, final Utilisateur utilisateur,
      final boolean doValidation, final boolean isImport, final List<Integer> requiredChampEntiteIds) throws SQLException{
      Integer echanId = null;
      if(jdbcSuite != null){
         echanId = jdbcSuite.getMaxEchantillonId();
         int banqueId;
         int typeId;
         int statutId;
         int utilId;
         Integer prelId = null;
         Integer collId = null;
         Integer empId = null;
         Integer qteId = null;
         Integer qualId = null;
         Integer prepaId = null;
         if(banque == null){
            throw new RequiredObjectIsNullException("Echantillon", "creation", "Banque");
         }
         banqueId = banque.getBanqueId();
         echantillon.setBanque(banqueDao.mergeObject(banque));
         // if (type == null) {
         // throw new RequiredObjectIsNullException("Echantillon", "creation",
         // "EchantillonType");
         // }
         checkEchantillonTypeSinceGatsbiAndReturnAllIds(echantillon, type, isImport, requiredChampEntiteIds);
         typeId = type != null ? type.getId() : null;

         if(statut == null){
            throw new RequiredObjectIsNullException("Echantillon", "creation", "ObjetStatut");
         }
         echantillon.setObjetStatut(objetStatutDao.mergeObject(statut));
         statutId = statut.getObjetStatutId();
         if(utilisateur == null){
            throw new RequiredObjectIsNullException("Echantillon", "creation", "Utilisateur");
         }
         utilId = utilisateur.getUtilisateurId();
         if(prelevement != null){
            prelId = prelevement.getPrelevementId();
         }
         if(collaborateur != null){
            collId = collaborateur.getCollaborateurId();
         }
         if(emplacement != null && checkEmplacementOccupied(emplacement, echantillon)){
            echantillon.setEmplacement(emplacementDao.mergeObject(emplacement));
         }
         if(quantite != null){
            qteId = quantite.getId();
         }
         if(qualite != null){
            qualId = qualite.getId();
         }
         if(preparation != null){
            prepaId = preparation.getId();
         }
         if(findDoublonManager(echantillon)){
            log.warn("Doublon lors de la creation de l'objet Echantillon : " + echantillon.toString());
            throw new DoublonFoundException("Echantillon", "creation");
         }

         if(noconfsTrait != null && !noconfsTrait.isEmpty()){
            echantillon.setConformeTraitement(false);
         }

         if(noconfsCess != null && !noconfsCess.isEmpty()){
            echantillon.setConformeCession(false);
         }

         try{
            // @since gatsbi
            doValidation(echantillon, doValidation, requiredChampEntiteIds, codes, isImport);

            // increment maxId
            jdbcSuite.incrementMaxEchantillonId();

            if(echantillon.getEmplacement() != null){
               echantillon.getEmplacement().setObjetId(jdbcSuite.getMaxEchantillonId());
               echantillon.getEmplacement().setEntite(entiteDao.findById(3));
               echantillon.getEmplacement().setVide(false);
               emplacementDao.mergeObject(echantillon.getEmplacement());
               empId = echantillon.getEmplacement().getEmplacementId();
            }

            jdbcSuite.getPstmt().setInt(1, jdbcSuite.getMaxEchantillonId());
            jdbcSuite.getPstmt().setInt(2, banqueId);
            jdbcSuite.getPstmt().setInt(3, typeId);
            jdbcSuite.getPstmt().setInt(4, statutId);
            if(prelId != null){
               jdbcSuite.getPstmt().setInt(5, prelId);
            }else{
               jdbcSuite.getPstmt().setNull(5, Types.INTEGER);
            }
            if(collId != null){
               jdbcSuite.getPstmt().setInt(6, collId);
            }else{
               jdbcSuite.getPstmt().setNull(6, Types.INTEGER);
            }
            if(qteId != null){
               jdbcSuite.getPstmt().setInt(7, qteId);
            }else{
               jdbcSuite.getPstmt().setNull(7, Types.INTEGER);
            }
            if(qualId != null){
               jdbcSuite.getPstmt().setInt(8, qualId);
            }else{
               jdbcSuite.getPstmt().setNull(8, Types.INTEGER);
            }
            if(prepaId != null){
               jdbcSuite.getPstmt().setInt(9, prepaId);
            }else{
               jdbcSuite.getPstmt().setNull(9, Types.INTEGER);
            }
            if(empId != null){
               jdbcSuite.getPstmt().setInt(10, empId);
            }else{
               jdbcSuite.getPstmt().setNull(10, Types.INTEGER);
            }
            jdbcSuite.getPstmt().setString(11, echantillon.getCode());
            if(echantillon.getDateStock() != null){
               jdbcSuite.getPstmt().setTimestamp(12, new java.sql.Timestamp(echantillon.getDateStock().getTimeInMillis()));
            }else{
               jdbcSuite.getPstmt().setNull(12, Types.DATE);
            }
            if(echantillon.getQuantite() != null){
               jdbcSuite.getPstmt().setBigDecimal(13, new BigDecimal(echantillon.getQuantite()));
            }else{
               jdbcSuite.getPstmt().setNull(13, Types.DECIMAL);
            }
            if(echantillon.getQuantiteInit() != null){
               jdbcSuite.getPstmt().setBigDecimal(14, new BigDecimal(echantillon.getQuantiteInit()));
            }else{
               jdbcSuite.getPstmt().setNull(14, Types.DECIMAL);
            }
            if(echantillon.getLateralite() != null){
               jdbcSuite.getPstmt().setString(15, echantillon.getLateralite());
            }else{
               jdbcSuite.getPstmt().setNull(15, Types.VARCHAR);
            }
            if(echantillon.getDelaiCgl() != null){
               jdbcSuite.getPstmt().setBigDecimal(16, new BigDecimal(echantillon.getDelaiCgl()));
            }else{
               jdbcSuite.getPstmt().setNull(16, Types.DECIMAL);
            }
            if(echantillon.getTumoral() != null){
               jdbcSuite.getPstmt().setBoolean(17, echantillon.getTumoral());
            }else{
               jdbcSuite.getPstmt().setNull(17, Types.BOOLEAN);
            }
            if(echantillon.getSterile() != null){
               jdbcSuite.getPstmt().setBoolean(18, echantillon.getSterile());
            }else{
               jdbcSuite.getPstmt().setNull(18, Types.BOOLEAN);
            }
            if(echantillon.getConformeTraitement() != null){
               jdbcSuite.getPstmt().setBoolean(19, echantillon.getConformeTraitement());
            }else{
               jdbcSuite.getPstmt().setNull(19, Types.BOOLEAN);
            }
            if(echantillon.getConformeCession() != null){
               jdbcSuite.getPstmt().setBoolean(20, echantillon.getConformeCession());
            }else{
               jdbcSuite.getPstmt().setNull(20, Types.BOOLEAN);
            }
            jdbcSuite.getPstmt().setBoolean(21, false);
            jdbcSuite.getPstmt().setBoolean(22, false);

            jdbcSuite.getPstmt().addBatch();
            jdbcSuite.getPstmtOp().setInt(1, utilId);
            jdbcSuite.getPstmtOp().setInt(2, jdbcSuite.getMaxEchantillonId());
            jdbcSuite.getPstmtOp().setInt(3, 3);
            jdbcSuite.getPstmtOp().setInt(4, 3);
            jdbcSuite.getPstmtOp().setTimestamp(5, new java.sql.Timestamp(Utils.getCurrentSystemCalendar().getTimeInMillis()));
            jdbcSuite.getPstmtOp().setBoolean(6, false);
            jdbcSuite.getPstmtOp().addBatch();

            echantillon.setEchantillonId(jdbcSuite.getMaxEchantillonId());

            // annotations
            if(listAnnoToCreateOrUpdate != null && !listAnnoToCreateOrUpdate.isEmpty()){
               annotationValeurManager.prepareAnnotationValeurListJDBCManager(jdbcSuite, listAnnoToCreateOrUpdate, echantillon,
                  utilisateur);

               jdbcSuite.getPstmtOp().clearParameters();
               jdbcSuite.getPstmtOp().setInt(1, utilId);
               jdbcSuite.getPstmtOp().setInt(2, jdbcSuite.getMaxEchantillonId());
               jdbcSuite.getPstmtOp().setInt(3, 3);
               jdbcSuite.getPstmtOp().setInt(4, 10);
               jdbcSuite.getPstmtOp().setTimestamp(5, new java.sql.Timestamp(Utils.getCurrentSystemCalendar().getTimeInMillis()));
               jdbcSuite.getPstmtOp().setBoolean(6, false);

               jdbcSuite.getPstmtOp().addBatch();
            }

            // no confs
            if(noconfsTrait != null){
               objetNonConformeManager.prepareListJDBCManager(jdbcSuite, echantillon, noconfsTrait);
               objetNonConformeManager.prepareListJDBCManager(jdbcSuite, echantillon, noconfsCess);
            }

            // ajout/update association vers codes assignes
            createOrUpdateCodesAssignesManager(echantillon, codes, true, utilisateur, jdbcSuite);

            // pour eviter erreurs JPA
            echantillon.setEchantillonId(null);

         }catch(final Exception e){
            jdbcSuite.setMaxEchantillonId(echanId);
            throw (e);
         }
         return jdbcSuite.getMaxEchantillonId();
      }
      return null;
   }

   @Override
   public void createObjectWithNonConformitesManager(final Echantillon echantillon, final Banque banque,
      final Prelevement prelevement, final Collaborateur collaborateur, final ObjetStatut statut, final Emplacement emplacement,
      final EchantillonType type, final List<CodeAssigne> codes, final Unite quantite, final EchanQualite qualite,
      final ModePrepa preparation, final Fichier anapath, final InputStream anapathStream,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final Utilisateur utilisateur, final boolean doValidation,
      final String baseDir, final boolean isImport, final List<NonConformite> noconfsTrait,
      final List<NonConformite> noconfsCess){

      final List<File> filesCreated = new ArrayList<>();
      try{
         if(noconfsTrait != null && !noconfsTrait.isEmpty()){
            echantillon.setConformeTraitement(false);
         }

         if(noconfsCess != null && !noconfsCess.isEmpty()){
            echantillon.setConformeCession(false);
         }

         createObjectWithCrAnapathManager(echantillon, banque, prelevement, collaborateur, statut, emplacement, type, codes,
            quantite, qualite, preparation, anapath, anapathStream, filesCreated, listAnnoToCreateOrUpdate, utilisateur,
            doValidation, baseDir, isImport);

         objetNonConformeManager.createUpdateOrRemoveListObjectManager(echantillon, noconfsTrait, "Traitement");
         objetNonConformeManager.createUpdateOrRemoveListObjectManager(echantillon, noconfsCess, "Cession");
      }catch(final RuntimeException re){
         for(final File f : filesCreated){
            f.delete();
         }
         throw (re);
      }
   }

   @Override
   public void updateObjectManager(final Echantillon echantillon, final Banque banque, final Prelevement prelevement,
      final Collaborateur collaborateur, final ObjetStatut statut, final Emplacement emplacement, final EchantillonType type,
      final List<CodeAssigne> codes, final List<CodeAssigne> codesToDelete, final Unite quantite, final EchanQualite qualite,
      final ModePrepa preparation, final List<AnnotationValeur> listAnnoToCreateOrUpdate,
      final List<AnnotationValeur> listAnnoToDelete, final List<File> filesCreated, final List<File> filesToDelete,
      final Utilisateur utilisateur, final boolean doValidation, final List<OperationType> operations, final String baseDir){

      try{

         mergeNonRequiredObjects(echantillon, prelevement, collaborateur, emplacement, quantite, qualite, preparation);

         checkRequiredObjectsAndValidate(echantillon, banque, type, statut, "modification", utilisateur, codes, doValidation,
            false);

         if(!findDoublonManager(echantillon)){

            echantillonDao.updateObject(echantillon);
            log.info("Modification de l'objet Echantillon : " + echantillon.toString());

            if(operations == null || !operations.contains(operationTypeDao.findByNom("ModifMultiple").get(0))){
               // Enregistrement de l'operation associee
               final Operation creationOp = new Operation();
               creationOp.setDate(Utils.getCurrentSystemCalendar());
               operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Modification").get(0),
                  echantillon);
            }

            if(operations != null){
               for(int i = 0; i < operations.size(); i++){
                  // Enregistrement de l'operation associee
                  final Operation dateOp = new Operation();
                  dateOp.setDate(Utils.getCurrentSystemCalendar());
                  operationManager.createObjectManager(dateOp, utilisateur, operations.get(i), echantillon);
               }
            }

            // délétion des champs à supprimer
            if(codesToDelete != null){
               for(int i = 0; i < codesToDelete.size(); i++){
                  codeAssigneManager.removeObjectManager(codesToDelete.get(i));
               }
            }

            // ajout/update association vers codes assignes
            if(codes != null){
               try{
                  echantillon.setCodesAssignes(
                     new HashSet<>(createOrUpdateCodesAssignesManager(echantillon, codes, false, utilisateur, null)));
               }catch(final SQLException e){
                  // never accessible
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
               annotationValeurManager.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, echantillon, utilisateur, null,
                  baseDir, filesCreated, filesToDelete);
            }

            // enregistre operation associee annotation
            // si il y a eu des deletes et pas d'updates
            if((listAnnoToCreateOrUpdate == null || listAnnoToCreateOrUpdate.isEmpty())
               && (listAnnoToDelete != null && !listAnnoToDelete.isEmpty())){
               CreateOrUpdateUtilities.createAssociateOperation(echantillon, operationManager,
                  operationTypeDao.findByNom("Annotation").get(0), utilisateur);
            }

            if(filesToDelete != null){
               for(final File f : filesToDelete){
                  f.delete();
               }
            }
         }else{ // doublon
            log.warn("Doublon lors de la modification de l'objet Echantillon : " + echantillon.toString());
            throw new DoublonFoundException("Echantillon", "modification", echantillon.getCode(), null);
         }
      }catch(final RuntimeException re){
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
   public void updateObjectWithCrAnapathManager(final Echantillon echantillon, final Banque banque, final Prelevement prelevement,
      final Collaborateur collaborateur, final ObjetStatut statut, final Emplacement emplacement, final EchantillonType type,
      final List<CodeAssigne> codes, final List<CodeAssigne> codesToDelete, final Unite quantite, final EchanQualite qualite,
      final ModePrepa preparation, Fichier anapath, final InputStream anapathStream, final List<File> filesCreated,
      final List<File> filesToDelete, final List<AnnotationValeur> listAnnoToCreateOrUpdate,
      final List<AnnotationValeur> listAnnoToDelete, final Utilisateur utilisateur, final boolean doValidation,
      final List<OperationType> operations, final String baseDir){

      try{
         updateObjectManager(echantillon, banque, prelevement, collaborateur, statut, emplacement, type, codes, codesToDelete,
            quantite, qualite, preparation, listAnnoToCreateOrUpdate, listAnnoToDelete, filesCreated, filesToDelete,
            utilisateur, doValidation, operations, baseDir);

         fichierManager.createOrUpdateFileForObject(echantillon, anapath, anapathStream,
            writeCrAnapathFilePath(baseDir, echantillon.getBanque(), anapath), filesCreated, filesToDelete);
         echantillonDao.updateObject(echantillon);

         if(filesToDelete != null){
            for(final File f : filesToDelete){
               f.delete();
            }
         }

      }catch(final RuntimeException re){
         if(filesCreated != null){
            for(final File f : filesCreated){
               f.delete();
            }
         }else{
            log.warn("Rollback création fichier n'a pas pu être réalisée");
         }
         // rollback la creation d'un fichier en cas d'erreur
         // supprime un cr enregistré en cas d'erreur
         if(anapath != null){
            if(anapathStream != null){

               try{
                  anapathStream.reset();
               }catch(final IOException e){
                  log.error(e);
               }

            }

            anapath = anapath.clone();
            anapath.setFichierId(null);
            if(anapath.getPath() != null){
               anapath.setPath(anapath.getPath().substring(0, anapath.getPath().lastIndexOf("_")));
            }
         }

         throw (re);
      }
   }

   @Override
   public void removeObjectManager(final Echantillon echantillon, final String comments, final Utilisateur user,
      final List<File> filesToDelete){
      if(echantillon != null){

         if(!isUsedObjectManager(echantillon) && !isCessedObjectManager(echantillon)){
            Iterator<CodeAssigne> it = codeAssigneManager.findCodesMorphoByEchantillonManager(echantillon).iterator();
            while(it.hasNext()){
               codeAssigneManager.removeObjectManager(it.next());
            }
            it = codeAssigneManager.findCodesOrganeByEchantillonManager(echantillon).iterator();
            while(it.hasNext()){
               codeAssigneManager.removeObjectManager(it.next());
            }

            final Iterator<Retour> retoursIt = retourManager.getRetoursForObjectManager(echantillon).iterator();
            while(retoursIt.hasNext()){
               retourManager.removeObjectManager(retoursIt.next());
            }

            // Supprime non conformites associees
            CreateOrUpdateUtilities.removeAssociateNonConformites(echantillon, objetNonConformeManager);

            echantillonDao.removeObject(echantillon.getEchantillonId());
            log.info("Suppression de l'objet Echantillon : " + echantillon.toString());

            //Supprime operations associees
            CreateOrUpdateUtilities.removeAssociateOperations(echantillon, operationManager, comments, user);

            //Supprime importations associes
            CreateOrUpdateUtilities.removeAssociateImportations(echantillon, importHistoriqueManager);

            //Supprime annotations associes
            annotationValeurManager.removeAnnotationValeurListManager(annotationValeurManager.findByObjectManager(echantillon),
               filesToDelete);

            // suppression du fichier associé base et systeme
            if(echantillon.getCrAnapath() != null){
               fichierManager.removeObjectManager(echantillon.getCrAnapath(), filesToDelete);
            }

         }else{
            if(!isCessedObjectManager(echantillon)){
               log.warn("Objet utilisé lors de la suppression de l'objet " + "Echantillon : " + echantillon.toString());
               throw new ObjectUsedException("echantillon.deletion." + "isUsedCascade", true);
            }
            log.warn("Objet cédé lors de la suppression de l'objet " + "Echantillon : " + echantillon.toString());
            throw new ObjectUsedException("echantillon.deletion." + "isUsedNonCascade", false);
         }
      }
   }

   @Override
   public void removeObjectCascadeManager(final Echantillon echantillon, final String comments, final Utilisateur user,
      final List<File> filesToDelete){
      if(!isCessedObjectManager(echantillon)){
         // suppression des dérivés en mode cascade
         final Iterator<Transformation> transfIt = transformationManager.findByParentManager(echantillon).iterator();
         while(transfIt.hasNext()){
            prodDeriveManager.removeObjectCascadeManager(transfIt.next(), comments, user, filesToDelete);
         }

         removeObjectManager(echantillon, comments, user, filesToDelete);
      }else{
         throw new ObjectUsedException("echantillon.cascade.isCessed", false);
      }
   }

   @Override
   public List<CodeAssigne> createOrUpdateCodesAssignesManager(final Echantillon echantillon, final List<CodeAssigne> codes,
      //CodeAssigne codeToExport, 
      final boolean isOrgane, final Utilisateur usr, final EchantillonJdbcSuite jdbcSuite) throws SQLException{

      final List<CodeAssigne> codesCreatedUpdated = new ArrayList<>();
      if(codes != null && !codes.isEmpty()){
         // verifie un seul code exporte
         int orgs = 0;
         int expsLes = 0;
         int les = 0;
         int expsOrg = 0;
         for(int i = 0; i < codes.size(); i++){
            if(codes.get(i).getIsOrgane()){
               orgs++;
               if(codes.get(i).getExport()){
                  expsOrg++;
               }
            }else{
               les++;
               if(codes.get(i).getExport()){
                  expsLes++;
               }
            }
         }
         if(orgs > 0 && expsOrg != 1){
            // recherche bug exportNbIllegal
            log.error(expsOrg);
            log.error(orgs);
            for(int i = 0; i < codes.size(); i++){
               log.error(codes.get(i).getCode() + "--" + codes.get(i).getIsOrgane() + "--" + codes.get(i).getExport());
            }
            throw new RuntimeException("echantillon" + ".codesAssigne.organe.exportNbIllegal");
         }else if(les > 0 && expsLes != 1){
            log.error(expsLes);
            log.error(les);
            for(int i = 0; i < codes.size(); i++){
               log.error(codes.get(i).getCode() + "--" + codes.get(i).getIsOrgane() + "--" + codes.get(i).getExport());
            }
            throw new RuntimeException("echantillon" + ".codesAssigne.morpho.exportNbIllegal");
         }else{
            if(jdbcSuite == null){
               String operation = "creation";
               CodeAssigne clone = null;
               // on parcourt la liste de champs
               for(int i = 0; i < codes.size(); i++){
                  clone = codes.get(i).clone();
                  operation = "creation";
                  clone.setEchantillon(echantillon);
                  if(clone.getCodeAssigneId() != null){
                     operation = "modification";
                  }
                  codeAssigneManager.createOrUpdateManager(clone, echantillon, null, usr, operation);

                  codesCreatedUpdated.add(clone);
               }
            }else{
               codeAssigneManager.prepareListJDBCManager(jdbcSuite, echantillon, codes, usr);
            }
         }
      }
      return codesCreatedUpdated;
   }

   @Override
   public String writeCrAnapathFilePath(String baseDir, final Banque bank, final Fichier file){
      if(!baseDir.endsWith("/")){
         baseDir = baseDir + "/";
      }
      String path =
         baseDir + "pt_" + bank.getPlateforme().getPlateformeId() + "/" + "coll_" + bank.getBanqueId() + "/cr_anapath/";

      if(!new File(path).exists()){
         throw new RuntimeException("error.filesystem.access");
      }

      if(file != null && file.getNom() != null){
         path = path + file.getNom();
      }

      return path;
   }

   @Override
   public boolean itemINCa50To53Manager(final Echantillon echantillon, final String value){
      boolean item = false;

      if(echantillon != null && value != null && !value.isEmpty()){
         final Prelevement prlvt = getPrelevementManager(echantillon);

         if(prlvt != null && prlvt.getMaladie() != null){
            if(prelevementDao.findByMaladieAndNature(prlvt.getMaladie(), value, prlvt.getDatePrelevement()).size() > 0){
               item = true;
            }else if(echantillonDao.findByMaladieAndType(prlvt.getMaladie(), value, prlvt.getDatePrelevement()).size() > 0){
               item = true;
            }else{
               final List<Prelevement> prlvts =
                  prelevementDao.findByMaladieAndBanque(prlvt.getMaladie(), echantillon.getBanque());

               for(int i = 0; i < prlvts.size(); i++){
                  if(prlvt.getDatePrelevement() == null || prlvt.getDatePrelevement().equals(prlvts.get(i).getDatePrelevement())){
                     if(prodDeriveManager.findByParentAndTypeManager(prlvts.get(i), value).size() > 0){
                        item = true;
                     }else{
                        final Set<Echantillon> echans = new HashSet<>();
                        echans.addAll(echantillonDao.findByPrelevement(prlvts.get(i)));
                        final Iterator<Echantillon> it = echans.iterator();
                        while(it.hasNext()){
                           if(prodDeriveManager.findByParentAndTypeManager(it.next(), value).size() > 0){
                              item = true;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return item;
   }

   @Override
   public List<Echantillon> findByPrelevementManager(final Prelevement prel){
      return echantillonDao.findByPrelevement(prel);
   }

   @Override
   public void switchBanqueCascadeManager(Echantillon echan, final Banque bank, final boolean doValidation, final Utilisateur u,
      final List<File> filesToDelete, final Set<MvFichier> filesToMove){
      if(bank != null && echan != null && !bank.equals(echan.getBanque())){

         if(doValidation){
            final List<CederObjet> ceds = cederObjetManager.findByObjetManager(echan);
            for(final CederObjet cederObjet : ceds){
               // cross plateforme switchBanqueException
               if(!cederObjet.getCession().getBanque().getPlateforme().equals(bank.getPlateforme())){
                  final TKException te = new TKException();
                  te.setMessage("echantillon.switchBanque.isCessed");
                  te.setIdentificationObjetException(echan.getCode());
                  te.setEntiteObjetException(echan.entiteNom());
                  throw te;
               }
            }
            final Set<Banque> banks =
               conteneurManager.getBanquesManager(conteneurManager.findFromEmplacementManager(echan.getEmplacement()));
            if(!banks.isEmpty() && !banks.contains(bank)){
               final TKException te = new TKException();
               te.setMessage("echantillon." + "switchBanque.badBanqueStockage");
               te.setIdentificationObjetException(echan.getCode());
               te.setEntiteObjetException(echan.entiteNom());
               throw te;
            }
         }

         final Iterator<ProdDerive> derivesIt = getProdDerivesManager(echan).iterator();
         while(derivesIt.hasNext()){
            prodDeriveManager.switchBanqueCascadeManager(derivesIt.next(), bank, doValidation, u, 
            		filesToDelete, filesToMove);
         }

         //Suppression du délégué si la banque de destination n'est pas dans le même contexte que la banque d'origine
         if(!bank.getContexte().equals(echan.getBanque().getContexte())){
            echan.setDelegate(null);
         }

         echan.setBanque(bank);

         if(findDoublonManager(echan)){
            throw new DoublonFoundException("Echantillon", "switchBanque", echan.getCode(), null);
         }
         echan = echantillonDao.mergeObject(echan);

         // @since 2.2.0
         // met à jour la référence vers le CR anapath
         fichierManager.switchBanqueManager(echan.getCrAnapath(), bank, filesToMove);

         // annotations
         annotationValeurManager.switchBanqueManager(echan, bank, filesToDelete, filesToMove);

         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, u, operationTypeDao.findByNom("ChangeCollection").get(0), echan);
      }
   }

   @Override
   public void updateMultipleObjectsManager(final List<Echantillon> echantillonsToUpdate,
      final List<Echantillon> baseEchantillons, final List<CodeAssigne> codes, final List<CodeAssigne> codesToDelete,
      final Fichier crAnapath, final InputStream anapathStream, final Boolean deleteAnapath,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final List<AnnotationValeur> listAnnoToDelete,
      final List<NonConformite> ncfsTrait, final List<NonConformite> ncfsCess, final Utilisateur utilisateur,
      final String baseDir){

      final List<OperationType> operations = new ArrayList<>();
      operations.add(operationTypeDao.findByNom("ModifMultiple").get(0));

      if(codesToDelete != null){
         for(int j = 0; j < codesToDelete.size(); j++){
            codeAssigneManager.removeObjectManager(codesToDelete.get(j));
         }
      }

      Fichier cloneFile = null;
      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      if(crAnapath != null && crAnapath.getPath() == null){
         crAnapath.setPath(writeCrAnapathFilePath(baseDir, echantillonsToUpdate.get(0).getBanque(), crAnapath));
      }

      boolean fileStored = false;

      for(int i = 0; i < echantillonsToUpdate.size(); i++){
         final Echantillon echan = echantillonsToUpdate.get(i);

         echan.setCodesAssignes(new HashSet<CodeAssigne>());

         try{
            if(crAnapath != null){
               if(echan.getCrAnapath() == null || !echan.getCrAnapath().equals(crAnapath)){
                  cloneFile = crAnapath.cloneNoId();
               }else{
                  cloneFile = null;
               }
            }

            updateObjectManager(echan, echan.getBanque(), echan.getPrelevement(), echan.getCollaborateur(),
               echan.getObjetStatut(), echan.getEmplacement(), echan.getEchantillonType(), codes, null, echan.getQuantiteUnite(),
               echan.getEchanQualite(), echan.getModePrepa(), null, null, filesCreated, filesToDelete,
               utilisateur, true, operations, baseDir);

            // enregistrement de la conformité
            objetNonConformeManager.createUpdateOrRemoveListObjectManager(echan, ncfsTrait, "Traitement");
            objetNonConformeManager.createUpdateOrRemoveListObjectManager(echan, ncfsCess, "Cession");

            if(crAnapath != null || deleteAnapath != null){
               if(cloneFile != null || (deleteAnapath != null && deleteAnapath)){
                  // nettoie fichier existant
                  fichierManager.removeObjectManager(echan.getCrAnapath(), filesToDelete);
                  echan.setCrAnapath(null);
               }

               if(crAnapath != null){
                  // update fichier + stream si fileStored = false
                  if(cloneFile != null){
                     fichierManager.createOrUpdateFileForObject(echan, cloneFile, fileStored ? null : anapathStream,
                        writeCrAnapathFilePath(baseDir, echan.getBanque(), cloneFile), filesCreated, filesToDelete);
                     crAnapath.setPath(echan.getCrAnapath().getPath());
                     crAnapath.setMimeType(echan.getCrAnapath().getMimeType());
                     fileStored = true;
                  }else{ // update nom fichier
                     echan.getCrAnapath().setNom(crAnapath.getNom());
                     fichierManager.createOrUpdateFileForObject(echan, echan.getCrAnapath(), null,
                        writeCrAnapathFilePath(baseDir, echan.getBanque(), echan.getCrAnapath()), filesCreated, filesToDelete);
                  }
                  echantillonDao.updateObject(echan);
               }
            }

         }catch(final RuntimeException e){
            if(e instanceof TKException){
               ((TKException) e).setEntiteObjetException("Echantillon");
               ((TKException) e).setIdentificationObjetException(echan.getCode());
            }

            for(final File f : filesCreated){
               f.delete();
            }

            throw e;
         }

      }

      try{

         // suppr les annotations pour tous les echantillons
         annotationValeurManager.removeAnnotationValeurListManager(listAnnoToDelete, filesToDelete);

         if(listAnnoToCreateOrUpdate != null){
            // traite en premier et retire les annotations 
            // création de fichiers pour 
            // enregistrement en batch 
            final List<AnnotationValeur> fileVals = new ArrayList<>();
            for(final AnnotationValeur val : listAnnoToCreateOrUpdate){
               if(val.getFichier() != null && val.getStream() != null){
                  annotationValeurManager.createFileBatchForTKObjectsManager(baseEchantillons, val.getFichier(), val.getStream(),
                     val.getChampAnnotation(), val.getBanque(), utilisateur, baseDir, filesCreated);
                  fileVals.add(val);
               }
            }
            listAnnoToCreateOrUpdate.removeAll(fileVals);

            // update les annotations, null operation pour
            // laisser la possibilité création/modification au sein 
            // de la liste
            annotationValeurManager.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, null, utilisateur, null, baseDir,
               filesCreated, filesToDelete);
         }

         for(final File f : filesToDelete){
            f.delete();
         }
      }catch(final RuntimeException e){
         for(final File f : filesCreated){
            f.delete();
         }

         throw e;
      }
   }

   @Override
   public List<Echantillon> updateCodeEchantillonsManager(final List<Echantillon> echantillons, final String oldPrefixe,
      final String newPrefixe, final Utilisateur utilisateur){
      final List<Echantillon> results = new ArrayList<>();

      if(echantillons != null && oldPrefixe != null && newPrefixe != null && utilisateur != null){

         for(int i = 0; i < echantillons.size(); i++){
            Echantillon echan = echantillons.get(i);

            // on vérifie que l'échantillon était bien
            // composé de l'ancien préfixe
            if(!oldPrefixe.equals(newPrefixe) && echan.getCode().contains(oldPrefixe)){
               echan.setCode(echan.getCode().replace(oldPrefixe, newPrefixe));

               // sauvegarde
               echan = echantillonDao.mergeObject(echan);
               final Operation creationOp = new Operation();
               creationOp.setDate(Utils.getCurrentSystemCalendar());
               operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Modification").get(0),
                  echan);
            }
            results.add(echan);
         }
      }
      return results;
   }

   @Override
   public void saveEchantillonEmplacementManager(final Echantillon echantillon, final ObjetStatut statut,
      final Emplacement emplacement, final Utilisateur utilisateur, final List<OperationType> operations){

      if(echantillon != null && utilisateur != null){
         // On vérifie que le statut n'est pas null. Si c'est le cas 
         // on envoie une exception
         if(emplacement != null){
            // verifie qu'un derive n'est pas deja présent dans l'emplacement
            // car contrainte d'unicité ne s'applique pas
            if(checkEmplacementOccupied(emplacement, echantillon)){
               echantillon.setEmplacement(emplacementDao.mergeObject(emplacement));
            }
         }else{
            echantillon.setEmplacement(null);
         }

         if(statut != null){
            echantillon.setObjetStatut(objetStatutDao.mergeObject(statut));
         }else if(echantillon.getObjetStatut() == null){
            log.warn("Objet obligatoire ObjetStatut manquant lors " + "de la creation " + "d'un objet Echantillon");
            throw new RequiredObjectIsNullException("Echantillon", "creation", "ObjetStatut");
         }

         echantillonDao.updateObject(echantillon);
         log.info("Modification de l'objet Echantillon : " + echantillon.toString());

         if(operations != null){
            for(int i = 0; i < operations.size(); i++){
               //Enregistrement de l'operation associee
               final Operation dateOp = new Operation();
               dateOp.setDate(Utils.getCurrentSystemCalendar());
               operationManager.createObjectManager(dateOp, utilisateur, operations.get(i), echantillon);
            }
         }
      }
   }

   @Override
   public Long findCountByOperateurManager(final Collaborateur colla){
      if(colla != null){
         return echantillonDao.findCountByOperateur(colla).get(0);
      }
      return 0l;
   }

   @Override
   public Long findCountByCollaborateur(final Collaborateur colla){
      if(colla != null){
         return echantillonDao.findCountByCollaborateur(colla).get(0);
      }
      return 0l;
   }

   @Override
   public Long findCountCreatedByCollaborateurManager(final Collaborateur colla){
      if(colla != null){
         return echantillonDao.findCountCreatedByCollaborateur(colla).get(0);
      }
      return 0l;
   }

   @Override
   public Long findCountByPrelevementManager(final Prelevement prlvt){
      if(prlvt != null){
         return echantillonDao.findCountByPrelevement(prlvt).get(0);
      }
      return 0l;
   }

   @Override
   public Long findCountRestantsByPrelevementManager(final Prelevement prlvt){
      if(prlvt != null){
         return echantillonDao.findCountRestantsByPrelevement(prlvt).get(0);
      }
      return 0l;
   }

   @Override
   public Long findCountByPrelevementAndStockeReserveManager(final Prelevement prlvt){
      if(prlvt != null){
         return echantillonDao.findCountByPrelevementAndStockeReserve(prlvt).get(0);
      }
      return 0l;
   }

   @Override
   public List<Integer> findByCodeInListManager(final List<String> criteres, final List<Banque> banques,
      final List<String> notfounds){
      if(criteres != null && banques != null && notfounds != null && criteres.size() > 0 && banques.size() > 0){
         final List<Object[]> res = echantillonDao.findByCodeInListWithBanque(criteres, banques);
         final List<Integer> idFounds = new ArrayList<>();
         final List<String> codeFounds = new ArrayList<>();
         for(final Object[] objArray : res){
            idFounds.add((Integer) objArray[0]);
            codeFounds.add((String) objArray[1]);
         }

         // since 2.1
         // notfounds
         notfounds.addAll(CollectionUtils.subtract(criteres, codeFounds));

         return idFounds;
      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findByPatientNomOrNipInListManager(final List<String> criteres, final List<Banque> banks){
      if(criteres != null && banks != null && criteres.size() > 0 && banks.size() > 0){
         return echantillonDao.findByPatientNomOrNipInList(criteres, banks);
      }
      return new ArrayList<>();
   }

   @Override
   public void updateObjectWithNonConformitesManager(final Echantillon echantillon, final Banque banque,
      final Prelevement prelevement, final Collaborateur collaborateur, final ObjetStatut statut, final Emplacement emplacement,
      final EchantillonType type, final List<CodeAssigne> codes, final List<CodeAssigne> codesToDelete, final Unite quantite,
      final EchanQualite qualite, final ModePrepa preparation, final Fichier anapath, final InputStream anapathStream,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate,
      final List<AnnotationValeur> listAnnoToDelete, final Utilisateur utilisateur, final boolean doValidation,
      final List<OperationType> operations, final String baseDir, final List<NonConformite> noconfsTraitement,
      final List<NonConformite> noconfsCession){

      if(noconfsTraitement != null && !noconfsTraitement.isEmpty()){
         echantillon.setConformeTraitement(false);
      }

      if(noconfsCession != null && !noconfsCession.isEmpty()){
         echantillon.setConformeCession(false);
      }

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      try{
         updateObjectWithCrAnapathManager(echantillon, banque, prelevement, collaborateur, statut, emplacement, type, codes,
            codesToDelete, quantite, qualite, preparation, anapath, anapathStream, filesCreated, filesToDelete,
            listAnnoToCreateOrUpdate, listAnnoToDelete, utilisateur, doValidation, operations, baseDir);

         objetNonConformeManager.createUpdateOrRemoveListObjectManager(echantillon, noconfsTraitement, "Traitement");
         objetNonConformeManager.createUpdateOrRemoveListObjectManager(echantillon, noconfsCession, "Cession");
         for(final File f : filesToDelete){
            f.delete();
         }
      }catch(final RuntimeException re){
         for(final File f : filesCreated){
            f.delete();
         }
         throw (re);
      }
   }

   /**
    * Vérifie que l'emplacement n'est pas occupé par un autre objet. Validation permettant 
    * d'éviter que la contrainte d'unicité sur emplacement_id soit contournée par la 
    * présence d'un dérivé ou d'un échantillon (si création nouveaux emplacements)
    * @param emplacement
    * @param echantillon
    * @return true si pas TKException lancée
    * @version 2.0.13.2
    */
   private boolean checkEmplacementOccupied(final Emplacement empl, final Echantillon echantillon){
      Emplacement emplacement = empl;
      if(empl.getEmplacementId() != null){
         emplacement = emplacementDao.mergeObject(empl);
      }
      boolean throwError = false;
      List<TKStockableObject> objs = emplacementManager.findObjByEmplacementManager(emplacement, "ProdDerive");
      // derives if not empty -> error
      throwError = !objs.isEmpty();

      if(!throwError){ // test emplacement occupé par echantillon différent
         objs = emplacementManager.findObjByEmplacementManager(emplacement, "Echantillon");
         throwError = !objs.isEmpty() && !objs.get(0).listableObjectId().equals(echantillon.getEchantillonId());
      }

      if(throwError){
         final TKException tke = new TKException();
         tke.setMessage("error.emplacement.notEmpty");
         tke.setIdentificationObjetException(echantillon.getCode());
         tke.setEntiteObjetException("Echantillon");
         tke.setTkObj(echantillon);
         throw tke;
      }
      return true;
   }

   @Override
   public void removeListFromIdsManager(final List<Integer> ids, final String comment, final Utilisateur u){
      if(ids != null){
         final List<File> filesToDelete = new ArrayList<>();
         Echantillon e;
         for(final Integer id : ids){
            e = findByIdManager(id);
            if(e != null){
               removeObjectCascadeManager(e, comment, u, filesToDelete);
            }
         }
         for(final File f : filesToDelete){
            f.delete();
         }
      }
   }

   @Override
   public List<Echantillon> findByCodeInPlateformeManager(final String code, final Plateforme pf){
      return echantillonDao.findByCodeInPlateforme(code, pf);
   }

   @Override
   public void updateEchantillon(final Echantillon echantillon){
      echantillonDao.updateObject(echantillon);
   }

   @Override
   public List<Echantillon> findByCodeInListWithPlateforme(final List<String> codes, final Plateforme pf){
      return echantillonDao.findByCodeInListWithPlateforme(codes, pf);
   }

   @Override
   public long calculDelaiStockage(final Echantillon echan, final Prelevement prel){

      long milli = -1;

      // on vérifie que la date de prlvt est exploitables
      if(prel != null && prel.getDatePrelevement() != null
         && (prel.getDatePrelevement().get(Calendar.HOUR_OF_DAY) != 0
         || prel.getDatePrelevement().get(Calendar.MINUTE) != 0
         || prel.getDatePrelevement() .get(Calendar.SECOND) != 0)) {

         // creation ou update dans procedure
         //			if (getLaboInters() != null) { 
         //				((Prelevement) getParentObject())
         //					.setLaboInters(new HashSet<LaboInter>(getLaboInters()));
         //			}
         //			
         //			Calendar dateRefPrel = ManagerLocator
         //				.getPrelevementManager()
         //					.getDateCongelationManager(getParentObject());
         //			if (dateRefPrel != null) {
         //				if (dateRefPrel.get(Calendar.HOUR_OF_DAY) != 0
         //					|| dateRefPrel.get(Calendar.MINUTE) != 0
         //					|| dateRefPrel.get(Calendar.SECOND) != 0) {
         //					milli = dateRefPrel.getTimeInMillis() 
         //							- getParentObject()
         //							.getDatePrelevement().getTimeInMillis();
         //				}			
         //			} else
    	  
         if(echan.getDateStock() != null) {
            if(echan.getDateStock().get(Calendar.HOUR_OF_DAY) != 0
               || echan.getDateStock().get(Calendar.MINUTE) != 0
               || echan.getDateStock().get(Calendar.SECOND) != 0) {
               milli =
                  echan.getDateStock().getTimeInMillis() - prel.getDatePrelevement().getTimeInMillis();
            }
         }        
      }
      
      return milli;

   }
   
   @Override
   public List<Integer> findByBanksAndImpact(List<Banque> banks, List<Boolean> impact){
      if(banks.size() > 0){
         return echantillonDao.findByBanksAndImpact(banks, impact);
      }
      return new ArrayList<>();
   }

   @Override
   public void checkRequiredObjectsAndValidate(final Echantillon echantillon, final Banque banque, final EchantillonType type,
      final ObjetStatut statut, final String operation, final Utilisateur utilisateur, final List<CodeAssigne> codes,
      final boolean doValidation, final boolean isImport){

      // Banque required
      if(banque != null){
         echantillon.setBanque(banque);
      }else if(echantillon.getBanque() == null){
         log.warn("Objet obligatoire Banque manquant" + " lors de la " + operation + " d'un Echantillon");
         throw new RequiredObjectIsNullException("Prelevement", operation, "Banque");
      }

      // Gatsbi required
      final List<Integer> requiredChampEntiteIds = new ArrayList<>();
      if(echantillon.getBanque().getEtude() != null){
         final Contexte echanContexte = echantillon.getBanque().getEtude().getContexteForEntite(3);
         if(echanContexte != null){
            requiredChampEntiteIds.addAll(echanContexte.getRequiredChampEntiteIds());
         }
      }

      // echantillon type
      // since 2.3.0-gatsbi peut être null
      checkEchantillonTypeSinceGatsbiAndReturnAllIds(echantillon, type, true, requiredChampEntiteIds);

      // On vérifie que le statut n'est pas null. Si c'est le cas
      // on envoie une exception
      if(statut != null){
         echantillon.setObjetStatut(objetStatutDao.mergeObject(statut));
      }else if(echantillon.getObjetStatut() == null){
         log.warn("Objet obligatoire ObjetStatut manquant lors " + "de la creation " + "d'un objet Echantillon");
         throw new RequiredObjectIsNullException("Echantillon", "creation", "ObjetStatut");
      }

      doValidation(echantillon, doValidation, requiredChampEntiteIds, codes, isImport);
   }

   private void doValidation(final Echantillon echantillon, final boolean skip, final List<Integer> requiredChampEntiteIds,
      final List<CodeAssigne> codes, final boolean isImport){
      // Validation
      if(skip){
         Validator[] validators;
         if(requiredChampEntiteIds.isEmpty()){ // pas de restriction gatsbi
            validators = new Validator[] {echantillonValidator};
         }else{ // gatsbi définit certain champs obligatoires
            final EchantillonGatsbiValidator gValidator =
               new EchantillonGatsbiValidator("echantillon", requiredChampEntiteIds, codes, isImport);
            validators = new Validator[] {gValidator, echantillonValidator};
         }

         BeanValidator.validateObject(echantillon, validators);
      }
   }

   /**
    * Merge et assigne tous les objects associes non obligatoires au prelevement
    * (sauf maladie car utilisé dans validation).
    * @since 2.3.0-gatsbi
    * @param echantillon
    * @param collaborateur
    * @param emplacement
    * @param quantiteUnite
    * @param qualite
    * @param preparation
    */
   private void mergeNonRequiredObjects(final Echantillon echantillon, final Prelevement prelevement,
      final Collaborateur collaborateur, final Emplacement emplacement, final Unite quantiteUnite, final EchanQualite qualite,
      final ModePrepa preparation){

      echantillon.setPrelevement(prelevementDao.mergeObject(prelevement));
      echantillon.setCollaborateur(collaborateurDao.mergeObject(collaborateur));

      // validation ne devrait pas être dans cette méthode...
      if(emplacement != null && checkEmplacementOccupied(emplacement, echantillon)){
         echantillon.setEmplacement(emplacementDao.mergeObject(emplacement));
      }else{
         echantillon.setEmplacement(null);
      }

      echantillon.setQuantiteUnite(uniteDao.mergeObject(quantiteUnite));
      echantillon.setEchanQualite(echanQualiteDao.mergeObject(qualite));
      echantillon.setModePrepa(modePrepaDao.mergeObject(preparation));
   }

   /**
    * Applique une vérification sur le champ EchantillonTyoe, car depuis GATSBI ce
    * thésaurus n'est plus obligatoire.
    * @param echantillon
    * @param type        échantillon, peut être null
    * @return liste champ entite ids définis comme obligatoire par gatsbi
    */
   private void checkEchantillonTypeSinceGatsbiAndReturnAllIds(final Echantillon echantillon, final EchantillonType type,
      final boolean setType, final List<Integer> requiredChampEntiteIds){

      // type may be null since Gatsbi
      if(type != null){
         if(setType){
            echantillon.setEchantillonType(echantillonTypeDao.mergeObject(type));
         }
      }else{ // valeur passée est nulle
         if(echantillon.getBanque().getEtude() == null || requiredChampEntiteIds.contains(58)){ // obligatoire!
            if(echantillon.getEchantillonType() == null){
               log.warn("Objet obligatoire EchantillonType manquant" + " lors de la " + "creation" + " d'un Echantillon");
               throw new RequiredObjectIsNullException("Echantillon", "creation", "EchantillonType");
            }
         }else{ // gastbi contexte non obligatoire
            echantillon.setEchantillonType(null);
         }
      }
   }

   @Override
   public List<Integer> findByPatientIdentifiantOrNomOrNipInListManager(List<String> idsNipsNoms, List<Banque> selectedBanques){
      if(idsNipsNoms != null && !idsNipsNoms.isEmpty() && selectedBanques != null && !selectedBanques.isEmpty()){
         return echantillonDao.findByPatientIdentifiantOrNomOrNipInList(idsNipsNoms, selectedBanques);
      }
      return new ArrayList<>();
   }
}
