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
package fr.aphp.tumorotek.manager.impl.qualite;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.qualite.FantomeDao;
import fr.aphp.tumorotek.dao.qualite.OperationDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.qualite.OperationValidator;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.qualite.Fantome;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Operation.
 * Classe créée le 14/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class OperationManagerImpl implements OperationManager
{

   private final Logger log = LoggerFactory.getLogger(OperationManager.class);

   /* Beans injectes par Spring*/
   private OperationDao operationDao;

   private UtilisateurDao utilisateurDao;

   private EntiteDao entiteDao;

   private OperationTypeDao operationTypeDao;

   private OperationValidator operationValidator;

   private FantomeDao fantomeDao;

   private EntityManagerFactory entityManagerFactory;

   private DataSource dataSource;

   public OperationManagerImpl(){}

   /* Properties setters */
   public void setOperationDao(final OperationDao oDao){
      this.operationDao = oDao;
   }

   public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setOperationTypeDao(final OperationTypeDao otDao){
      this.operationTypeDao = otDao;
   }

   public void setOperationValidator(final OperationValidator oValidator){
      this.operationValidator = oValidator;
   }

   public void setFantomeDao(final FantomeDao fDao){
      this.fantomeDao = fDao;
   }

   public void setEntityManagerFactory(final EntityManagerFactory eFactory){
      this.entityManagerFactory = eFactory;
   }

   public void setDataSource(final DataSource ds){
      this.dataSource = ds;
   }

   @Override
   public void createObjectManager(final Operation operation, final Utilisateur utilisateur, final OperationType operationType,
      final Object obj){
      operation.setUtilisateur(utilisateurDao.mergeObject(utilisateur));
      //OperationType required
      if(operationType == null){
         log.warn("Objet obligatoire OperationType manquant  lors de la creation d'une Operation");
         throw new RequiredObjectIsNullException("Operation", "creation", "OperationType");
      }
      operation.setOperationType(operationTypeDao.mergeObject(operationType));
      //Obj required
      if(obj == null){
         log.warn("Objet obligatoire Object manquant  lors de la creation d'une Operation");
         throw new RequiredObjectIsNullException("Operation", "creation", "Object");
      }
      //recupere la reference vers entite a partir de la classe de obj
      String entiteNom = obj.getClass().getSimpleName();
      // CodeAssigne regroupe 3 entites
      if(entiteNom.equals("CodeAssigne")){
         if(((CodeAssigne) obj).getIsOrgane()){
            entiteNom = "CodeOrgane";
         }else if(((CodeAssigne) obj).getIsMorpho()){
            entiteNom = "CodeMorpho";
         }else{
            entiteNom = "CodeDiagnostic";
         }
      }
      operation.setEntite(entiteDao.findByNom(entiteNom).get(0));

      //recupere l'ObjetId a partir de obj
      operation.setObjetId(getObjetIdFromObject(obj));
      //Validation
      BeanValidator.validateObject(obj, new Validator[] {operationValidator});

      operationDao.createObject(operation);
      log.debug("Enregistrement objet Operation {}",  obj);

   }

   @Override
   public List<Operation> findAllObjectsManager(){
      log.debug("Recherche totalite des Operation");
      return operationDao.findAll();
   }

   @Override
   public List<Operation> findByUtilisateurManager(final Utilisateur utilisateur){
      if(utilisateur != null){
         log.debug("Recherche les operations pour l'utilisateur : {}",  utilisateur);
      }
      return operationDao.findByUtilisateur(utilisateur);
   }

   @Override
   public boolean findDoublonManager(final Operation operation){
      return operationDao.findAll().contains(operation);
   }

   @Override
   public List<Operation> findByObjectManager(final Object obj){
      Entite entite = null;
      Integer objetId = null;
      if(obj != null){
         //recupere la reference vers entite a partir de la classe de obj
         String entiteNom = obj.getClass().getSimpleName();
         // CodeAssigne regroupe 3 entites
         if(entiteNom.equals("CodeAssigne")){
            if(((CodeAssigne) obj).getIsOrgane()){
               entiteNom = "CodeOrgane";
            }else if(((CodeAssigne) obj).getIsMorpho()){
               entiteNom = "CodeMorpho";
            }else{
               entiteNom = "CodeDiagnostic";
            }
         }
         log.debug("Recherche des Operation pour l'objet {}",  obj);
         entite = entiteDao.findByNom(entiteNom).get(0);
         objetId = getObjetIdFromObject(obj);
      }
      return operationDao.findByObjetIdAndEntite(objetId, entite);
   }

   @Override
   public List<Operation> findByObjectForHistoriqueManager(final Object obj){
      Entite entite = null;
      Integer objetId = null;
      if(obj != null){
         //recupere la reference vers entite a partir de la classe de obj
         String entiteNom = obj.getClass().getSimpleName();
         // CodeAssigne regroupe 3 entites
         if(entiteNom.equals("CodeAssigne")){
            if(((CodeAssigne) obj).getIsOrgane()){
               entiteNom = "CodeOrgane";
            }else if(((CodeAssigne) obj).getIsMorpho()){
               entiteNom = "CodeMorpho";
            }else{
               entiteNom = "CodeDiagnostic";
            }
         }
         log.debug("Recherche des Operation pour l'objet {}",  obj);
         entite = entiteDao.findByNom(entiteNom).get(0);
         objetId = getObjetIdFromObject(obj);
      }
      return operationDao.findByObjetIdAndEntiteForHistorique(objetId, entite);
   }

   @Override
   public List<Operation> findByObjetIdEntiteAndOpeTypeManager(final Object obj, final OperationType oType){
      Entite entite = null;
      Integer objetId = null;
      if(obj != null){
         //recupere la reference vers entite a partir de la classe de obj
         String entiteNom = obj.getClass().getSimpleName();
         // CodeAssigne regroupe 3 entites
         if(entiteNom.equals("CodeAssigne")){
            if(((CodeAssigne) obj).getIsOrgane()){
               entiteNom = "CodeOrgane";
            }else if(((CodeAssigne) obj).getIsMorpho()){
               entiteNom = "CodeMorpho";
            }else{
               entiteNom = "CodeDiagnostic";
            }
         }
         entite = entiteDao.findByNom(entiteNom).get(0);
         objetId = getObjetIdFromObject(obj);
      }
      return operationDao.findByObjetIdEntiteAndOperationType(objetId, entite, oType);
   }

   @Override
   public void removeObjectManager(final Operation operation){
      if(operation != null){
         // suppression fantome en cascade
         if(operation.getOperationType().getNom().equals("Suppression")){
            fantomeDao.removeObject(operation.getObjetId());
         }

         operationDao.removeObject(operation.getOperationId());
         log.info("Suppression objet Operation {}",  operation);
      }else{
         log.warn("Suppression d'une Operation null");
      }
   }

   /**
    * Recherche la date de création de l'objet passé en paramètres.
    * @param obj Un Object pour lequel on cherche la date de
    * creation.
    * @return Date de création de l'objet.
    */
   @Override
   public Calendar findDateCreationManager(final Object obj){

      final Operation operation = findOperationCreationManager(obj);

      if(operation != null){
         return operation.getDate();
      }

      return null;
   }

   @Override
   public Operation findOperationCreationManager(final Object obj){
      Entite entite = null;
      Integer objetId = null;
      if(obj != null){
         final OperationType creation = operationTypeDao.findByNom("Creation").get(0);

         log.debug("Recherche l'opération de création pour l'objet {}",  obj);
         entite = entiteDao.findByNom(obj.getClass().getSimpleName()).get(0);
         objetId = getObjetIdFromObject(obj);

         final List<Operation> operations = operationDao.findByObjetIdEntiteAndOperationType(objetId, entite, creation);

         if(operations != null && operations.size() > 0){
            return operations.get(0);
         }
      }
      return null;
   }

   /**
    * Recupere a partir d'un bean du domaine la reference vers l'entite
    * et son id afin de l'assigner a l'Operation passee en parametre.
    * @param operation Operation
    * @param obj Bean du domaine
    */
   private Integer getObjetIdFromObject(final Object obj){
      //recupere l'objetId pour l'assigner a l'operation
      try{
         final Class<?> targetClass = obj.getClass();
         final Method targetmethod = targetClass.getMethod("get" + targetClass.getSimpleName() + "Id", new Class[] {});
         return (Integer) targetmethod.invoke(obj, new Object[] {});
      }catch(final IllegalArgumentException e){
         log.error("Creation Operation a echoue car impossible de recupere ObjetId: {}",  e.getMessage());
      }catch(final IllegalAccessException e){
         log.error("Creation Operation a echoue car impossible de recupere ObjetId: {}",  e.getMessage());
      }catch(final InvocationTargetException e){
         log.error("Creation Operation a echoue car impossible de recupere ObjetId: {}",  e.getMessage());
      }catch(final SecurityException e){
         log.error("Creation Operation a echoue car impossible de recupere ObjetId: {}",  e.getMessage());
      }catch(final NoSuchMethodException e){
         log.error("Creation Operation a echoue car impossible de recupere ObjetId: {}",  e.getMessage());
      }
      return null;
   }

   @Override
   public List<Operation> findByDateOperationManager(final Calendar date){
      if(date != null){
         return operationDao.findByDate(date);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Operation> findAfterDateOperationManager(final Calendar date){
      if(date != null){
         return operationDao.findByAfterDate(date);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Operation> findBeforeDateOperationManager(final Calendar date){
      if(date != null){
         return operationDao.findByBeforeDate(date);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Operation> findBetweenDatesOperationManager(final Calendar date1, final Calendar date2){
      if(date1 != null && date2 != null){
         return operationDao.findByBetweenDates(date1, date2);
      }
      return new ArrayList<>();
   }

   @Override
   public void createPhantomManager(final TKFantomableObject obj, final String comments, final Utilisateur user){
      final Fantome f = new Fantome();
      f.setNom(obj.getPhantomData());
      f.setCommentaires(comments);
      f.setEntite(entiteDao.findByNom(obj.entiteNom()).get(0));
      fantomeDao.createObject(f);

      final Operation suppr = new Operation();
      suppr.setDate(Utils.getCurrentSystemCalendar());

      createObjectManager(suppr, user, operationTypeDao.findByNom("Suppression").get(0), f);
   }

   @Override
   public void removeAssociateOperationsManager(final Object obj, final String comments, final Utilisateur user){

      //		List<Operation> ops = findByObjectManager(obj);
      //		for (int i = 0; i < ops.size(); i++) {
      //			removeObjectManager(ops.get(i));
      //		}

      if(obj instanceof TKFantomableObject){
         createPhantomManager((TKFantomableObject) obj, comments, user);
      }
   }

   @Override
   public List<Operation> findByMultiCriteresManager(final String operateurDate1, final Calendar date1,
      final String operateurDate2, final Calendar date2, final OperationType operationType, final List<Utilisateur> users,
      final boolean showLogin){
      boolean critere = false;
      final List<Operation> operations = new ArrayList<>();

      if(date1 != null || operationType != null || (users != null && !users.isEmpty())){
         final StringBuffer sql = new StringBuffer();
         sql.append("SELECT DISTINCT o FROM Operation o WHERE ");

         if(operateurDate1 != null && date1 != null){
            critere = true;
            sql.append("o.date ");
            sql.append(operateurDate1);
            sql.append(":date1 ");
         }
         if(operateurDate2 != null && date2 != null){
            if(critere){
               sql.append("AND ");
            }
            critere = true;
            sql.append("o.date ");
            sql.append(operateurDate2);
            sql.append(":date2 ");
         }

         if(operationType != null){
            if(critere){
               sql.append("AND ");
            }
            critere = true;
            sql.append("o.operationType = ");
            sql.append(":type ");
         }

         if(users != null && !users.isEmpty()){
            if(critere){
               sql.append("AND ");
            }
            critere = true;
            sql.append("o.utilisateur in ");
            sql.append("(:users) ");
         }

         if(!showLogin){
            sql.append("AND o.operationType.nom != 'Login' ");
            sql.append("AND o.operationType.nom != 'Logout' ");
         }

         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Operation> query = em.createQuery(sql.toString(), Operation.class);
         log.debug("Recherche des opérations par multi-critères.");
         log.debug(sql.toString());
         if(sql.toString().contains("date1")){
            query.setParameter("date1", date1);
         }
         if(sql.toString().contains("date2")){
            query.setParameter("date2", date2);
         }
         if(sql.toString().contains("type")){
            query.setParameter("type", operationType);
         }
         if(sql.toString().contains("users")){
            query.setParameter("users", users);
         }

         operations.addAll(query.getResultList());
      }

      return operations;
   }

   @Override
   public Operation findLastByUtilisateurAndTypeManager(final OperationType operationType, final Utilisateur user, final int pos){
      Operation ope = null;

      if(operationType != null && user != null){
         final StringBuffer sql = new StringBuffer();
         sql.append("SELECT DISTINCT o FROM Operation o ");
         sql.append("WHERE o.utilisateur = :user ");
         sql.append("AND o.operationType = :type ");
         sql.append("ORDER BY o.date DESC");

         final EntityManager em = entityManagerFactory.createEntityManager();
         final Query query = em.createQuery(sql.toString());
         log.debug("Recherche de la dernière operation.");
         log.debug(sql.toString());
         query.setParameter("type", operationType);
         query.setParameter("user", user);

         // last one
         query.setMaxResults(pos);

         final List<?> res = query.getResultList();
         if(!res.isEmpty() && (pos - 1) >= 0 && (pos - 1) < res.size()){
            ope = (Operation) res.get(pos - 1);
         }

      }

      return ope;
   }

   @Override
   public void batchSaveManager(final List<Integer> objsId, final Utilisateur u, final OperationType oT, final Calendar cal,
      final Entite e){
      if(u != null && oT != null && e != null && cal != null && objsId != null && !objsId.isEmpty()){
         PreparedStatement pst = null;
         //         Connection conn = null;
         try{
            // conn = DataSourceUtils.getConnection(dataSource);
            // Calendar curr = Utils.getCurrentSystemCalendar();
            pst = DataSourceUtils.getConnection(dataSource).prepareStatement("insert into OPERATION (objet_id, "
               + "entite_id, utilisateur_id, date_, operation_type_id, v1) " + "values (?, ?, ?, ?, ?, 0)");

            for(final Integer i : objsId){
               pst.setInt(3, u.getUtilisateurId());
               pst.setInt(2, e.getEntiteId());
               pst.setInt(1, i);
               pst.setInt(5, oT.getOperationTypeId());
               pst.setTimestamp(4, new Timestamp(cal.getTime().getTime()));
               pst.addBatch();
            }
            pst.executeBatch();
            // DataSourceUtils.getConnection(dataSource).commit();
            //entityManagerFactory.getCache().evict(getClass());

         }catch(final CannotGetJdbcConnectionException e1){
            throw new RuntimeException(e1);
         }catch(final SQLException e1){
            throw new RuntimeException(e1);
         }finally{
            if(pst != null){
               try{
                  pst.close();
               }catch(final SQLException qe){}finally{
                  pst = null;
               }
            }
            //            if(conn != null){
            //               try{
            //                  conn.close();
            //               }catch(final SQLException qe){}finally{
            //                  conn = null;
            //               }
            //            }
         }
      }else{
         throw new NullPointerException();
      }
   }
}