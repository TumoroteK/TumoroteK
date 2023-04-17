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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.manager.coeur.cession.LargeCessionManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.validation.coeur.cession.LargeCessionHelper;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager gérant la création d'une large cession
 * par appel procédure JDBC.
 * Classe créée le 15/11/2016.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class LargeCessionManagerImpl implements LargeCessionManager
{

   private final Logger log = LoggerFactory.getLogger(LargeCessionManager.class);

   private EchantillonManager echantillonManager;

   private ProdDeriveManager prodDeriveManager;

   private EntityManagerFactory entityManagerFactory;

   public void setEchantillonManager(final EchantillonManager _e){
      this.echantillonManager = _e;
   }

   public void setProdDeriveManager(final ProdDeriveManager _p){
      this.prodDeriveManager = _p;
   }

   public void setEntityManagerFactory(final EntityManagerFactory _e){
      this.entityManagerFactory = _e;
   }

   @Override
   public LargeCessionHelper addObjectsAndValidateCession(final Cession cession, final List<String> codes, final Integer entiteId,
      final List<Banque> bks, final Utilisateur utilisateur){

      final LargeCessionHelper errs = validateCessionByCodesUploadManager(cession, codes, entiteId, bks);

      if(errs != null && !errs.containsErrs()){
         if(cession != null && cession.getCessionId() != null && utilisateur != null && utilisateur.getUtilisateurId() != null){

            Connection conn = null;
            PreparedStatement insertIds = null;
            CallableStatement addObjectsAndValidateCall = null;

            // open new connection because of temporary temp table
            try{
               // Connection to database
               final java.util.Properties props = new java.util.Properties();
               props.put("user", Utils.getUsernameDB());
               props.put("password", Utils.getPasswordDB());

               conn = DriverManager.getConnection(Utils.getDatabaseURL(), props);
               conn.setAutoCommit(false);
               insertIds = conn.prepareStatement("insert into TEMP_TRSFT (id) values (?) ");
               addObjectsAndValidateCall = conn.prepareCall("call largeCession(?,?,?)");

               // prepare batch insert
               for(final Integer id : errs.getIdsFound()){
                  insertIds.clearParameters();
                  insertIds.setInt(1, id);
                  insertIds.addBatch();
               }

               // insert ids
               insertIds.executeBatch();

               // procedure!
               addObjectsAndValidateCall.setInt(1, cession.getCessionId());
               addObjectsAndValidateCall.setInt(2, entiteId);
               addObjectsAndValidateCall.setInt(3, utilisateur.getUtilisateurId());
               addObjectsAndValidateCall.execute();

               conn.commit();

            }catch(final Exception e){
               e.printStackTrace();
               log.error(e.getMessage());
            }finally{
               if(conn != null){
                  try{
                     conn.close();
                  }catch(final Exception e){
                     conn = null;
                  }
               }
               if(insertIds != null){
                  try{
                     insertIds.close();
                  }catch(final Exception e){
                     insertIds = null;
                  }
               }
               if(addObjectsAndValidateCall != null){
                  try{
                     addObjectsAndValidateCall.close();
                  }catch(final Exception e){
                     addObjectsAndValidateCall = null;
                  }
               }
            }
         }
      }else{
         return errs;
      }
      return null;
   }

   public LargeCessionHelper validateCessionByCodesUploadManager(final Cession cession, final List<String> codes,
      final Integer entiteId, final List<Banque> bks){

      LargeCessionHelper errs = null;
      if(codes != null && entiteId != null && bks != null){

         errs = new LargeCessionHelper(entiteId);

         if(entiteId == 3){ // echantillon
            // ids founds + codes not found
            errs.getIdsFound().addAll(echantillonManager.findByCodeInListManager(codes, bks, errs.getCodesNotFound()));
         }else if(entiteId == 8){ // derive
            // ids founds + codes not found
            errs.getIdsFound().addAll(prodDeriveManager.findByCodeInListManager(codes, bks, errs.getCodesNotFound()));
         }

         // statuts autre que STOCKE
         errs.getObjsNonStockes().addAll(findIncompatibleStatutsManager(errs.getIdsFound(), entiteId));

         // date stockage > date Cession
         errs.getDateStockIncompatible()
            .addAll(findDateStockIncompatiblesManager(errs.getIdsFound(), entiteId, cession.getDepartDate().getTime()));

         // evt stockage > date Cession
         errs.getDateStockIncompatible()
            .addAll(findEvtStockIncompatiblesManager(errs.getIdsFound(), entiteId, cession.getDepartDate().getTime()));

      }
      return errs;
   }

   @Override
   public List<String> findIncompatibleStatutsManager(final List<Integer> ids, final Integer entiteId){

      final List<String> codes = new ArrayList<>();
      if(ids != null && !ids.isEmpty() && entiteId != null){

         final EntityManager em = entityManagerFactory.createEntityManager();

         TypedQuery<String> query = null;
         if(entiteId == 3){ // echantillon
            query = em.createQuery("SELECT e.code " + "FROM Echantillon e " + "WHERE e.echantillonId in (:ids) "
               + "AND e.objetStatut.statut not like 'STOCKE'" + "ORDER BY e.code", String.class);
         }else if(entiteId == 8){ // derive
            query = em.createQuery("SELECT p.code " + "FROM ProdDerive p " + "WHERE p.prodDeriveId in (:ids) "
               + "AND p.objetStatut.statut not like 'STOCKE'" + "ORDER BY p.code", String.class);
         }

         if(query != null){
            query.setParameter("ids", ids);
            query.setFirstResult(0);
            codes.addAll(query.getResultList());
         }

      }
      return codes;
   }

   @Override
   public List<String> findDateStockIncompatiblesManager(final List<Integer> ids, final Integer entiteId, final Date dateCession){

      final List<String> codes = new ArrayList<>();
      if(ids != null && !ids.isEmpty() && entiteId != null && dateCession != null){

         final EntityManager em = entityManagerFactory.createEntityManager();

         final Calendar cal = Calendar.getInstance();
         cal.setTime(dateCession);

         TypedQuery<String> query = null;
         if(entiteId == 3){ // echantillon
            query = em.createQuery("SELECT e.code " + "FROM Echantillon e " + "WHERE e.echantillonId in (:ids) "
               + "AND e.dateStock > :dateCession " + "ORDER BY e.code", String.class);
         }else if(entiteId == 8){ // derive
            query = em.createQuery("SELECT p.code " + "FROM ProdDerive p " + "WHERE p.prodDeriveId in (:ids) "
               + "AND p.dateStock > :dateCession " + "ORDER BY p.code", String.class);
         }

         if(query != null){
            query.setParameter("ids", ids);
            query.setParameter("dateCession", cal);
            query.setFirstResult(0);
            codes.addAll(query.getResultList());
         }

      }
      return codes;
   }

   @Override
   public List<String> findEvtStockIncompatiblesManager(final List<Integer> ids, final Integer entiteId, final Date dateCession){

      final List<String> codes = new ArrayList<>();
      if(ids != null && !ids.isEmpty() && entiteId != null && dateCession != null){

         final EntityManager em = entityManagerFactory.createEntityManager();

         final Calendar cal = Calendar.getInstance();
         cal.setTime(dateCession);

         TypedQuery<String> query = null;
         if(entiteId == 3){ // echantillon
            query = em.createQuery("SELECT distinct e.code " + "FROM Echantillon e, Retour r "
               + "WHERE  e.echantillonId = r.objetId " + "AND e.echantillonId in (:ids) " + "AND r.entite.entiteId = 3 "
               + "AND r.dateRetour > :dateCession " + "ORDER BY e.code", String.class);
         }else if(entiteId == 8){ // derive
            query = em.createQuery("SELECT distinct p.code " + "FROM ProdDerive p, Retour r "
               + "WHERE p.prodDeriveId = r.objetId " + "AND p.prodDeriveId in (:ids) " + "AND r.entite.entiteId = 8 "
               + "AND r.dateRetour > :dateCession " + "ORDER BY p.code", String.class);
         }

         if(query != null){
            query.setParameter("ids", ids);
            query.setParameter("dateCession", cal);
            query.setFirstResult(0);
            codes.addAll(query.getResultList());
         }

      }
      return codes;
   }
}