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
package fr.aphp.tumorotek.manager.impl.systeme;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.SharedEntityManagerCreator;

import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Implémentation du manager du bean de domaine Entite.
 * Classe créée le 30/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class EntiteManagerImpl implements EntiteManager
{

   private final Logger log = LoggerFactory.getLogger(EntiteManager.class);

   /** Bean Dao EntiteDao. */
   private EntiteDao entiteDao;

   /** Bean Dao EntityManagerFactory. */
   private EntityManagerFactory entityManagerFactory;

   /**
    * Setter du bean EntiteDao.
    * @param eDao est le bean Dao.
    */
   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   /**
    * Setter du bean EntityManagerFactory.
    * @param eDao est le bean Dao.
    */
   public void setEntityManagerFactory(final EntityManagerFactory eFactory){
      this.entityManagerFactory = eFactory;
   }

   /**
    * Recherche une entité dont l'identifiant est passé en paramètre.
    * @param entiteId Identifiant du type que l'on recherche.
    * @return Une Entite.
    */
   @Override
   public Entite findByIdManager(final Integer entiteId){
      return entiteDao.findById(entiteId);
   }

   /**
    * Recherche toutes les entités présentes dans la base.
    * @return Liste de Entite.
    */
   @Override
   public List<Entite> findAllObjectsManager(){
      log.debug("Recherche de toutes les Entites");
      return entiteDao.findAll();
   }

   @Override
   public List<Entite> findByNomManager(final String nom){
      log.debug("Recherche Entite par " + nom);
      if(nom != null){
         return entiteDao.findByNom(nom);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Entite> findAnnotablesManager(){
      log.debug("Recherche Entite annotables");
      return entiteDao.findAnnotables();
   }

   @Override
   public Object findObjectByEntiteAndIdManager(final Entite entite, final Integer objectId){

      if(objectId != null && entite != null){
         log.debug("Recherche l'objet correspondant au couple Entite : " + entite.toString() + " - ObjetId : " + objectId);
         final String nomTable = entite.getNom();

         String nomAttribut = "id";

         if(!entite.getNom().matches("ChampAnnotation|ChampDelegue|ChampEntite")){
            final String first = nomTable.substring(0, 1);
            final String end = nomTable.substring(1);
            nomAttribut = first.toLowerCase().concat(end);
            nomAttribut = nomAttribut.concat("Id");
         }

         final StringBuffer sb = new StringBuffer();
         sb.append("SELECT e FROM ");
         sb.append(nomTable);
         sb.append(" e WHERE e.");
         sb.append(nomAttribut);
         sb.append(" = ");
         sb.append(objectId);

         //EntityManager em = entityManagerFactory.createEntityManager();
         final EntityManager em = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
         final TypedQuery<Object> query = em.createQuery(sb.toString(), Object.class);

         final List<Object> results = query.getResultList();
         if(results.size() > 0){
            results.get(0).getClass();
            return results.get(0);
         }
      }
      return null;
   }

   @Override
   public List<Integer> findIdsByEntiteAndIdAfterBanqueFiltreManager(final Entite entite, final List<Integer> objectIds,
      final List<Banque> banks){
      List<Integer> ids = new ArrayList<>();

      if(objectIds != null && !objectIds.isEmpty()){
         final String nomTable = entite.getNom();
         final String first = nomTable.substring(0, 1);
         final String end = nomTable.substring(1);
         String nomAttribut = first.toLowerCase().concat(end);
         nomAttribut = nomAttribut.concat("Id");

         final StringBuffer sb = new StringBuffer();
         sb.append("SELECT e." + nomAttribut + " FROM ");
         sb.append(nomTable);
         sb.append(" e WHERE e.");
         sb.append(nomAttribut);
         sb.append(" in (:ids) ");

         if(banks != null && !banks.isEmpty() && (entite.getNom().equals("Prelevement") || entite.getNom().equals("Echantillon")
            || entite.getNom().equals("ProdDerive") || entite.getNom().equals("Cession"))){
            sb.append(" AND e.banque in (:banques)");
         }

         final EntityManager em = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
         final TypedQuery<Integer> query = em.createQuery(sb.toString(), Integer.class);
         query.setParameter("ids", objectIds);
         if(query.getParameters().size() == 2){
            query.setParameter("banques", banks);
         }
         ids = query.getResultList();

      }
      return ids;
   }

}