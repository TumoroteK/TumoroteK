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
package fr.aphp.tumorotek.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import fr.aphp.tumorotek.dao.FinderExecutor;
import fr.aphp.tumorotek.dao.GenericDaoJpa;

/**
 *
 * Implémentation du Generic DAO.
 * @param <T> est la classe de l'objet.
 * @param <PK> est sa clé primaire.
 *
 * @author Pierre Ventadour
 * @version 09/09/2009
 *
 */
public class GenericDaoJpaImpl<T, PK extends Serializable> extends JpaDaoSupport implements GenericDaoJpa<T, PK>, FinderExecutor
{

   private static Logger log = LoggerFactory.getLogger(GenericDaoJpa.class);

   //@PersistenceContext
   //private EntityManager em;
   //private EntityManagerFactory entityManagerFactory;

   /** Classe de l'objet. */
   private final Class<T> type;

   /**
    * Constructor.
    * @param t est la classe de l'objet.
    */
   public GenericDaoJpaImpl(final Class<T> t){
      this.type = t;
      //em = entityManagerFactory.createEntityManager();
   }

   /**
    * Renvoie la classe de l'objet.
    * @return la classe de l'objet.
    */
   public Class<T> getType(){
      return type;
   }

   //	public void setType(Class<T> type) {
   //		this.type = type;
   //	}

   //	public EntityManager getEm() {
   //		return em;
   //	}
   //
   //	public void setEm(EntityManager em) {
   //		this.em = em;
   //	}

   /**
    * Persist une instance d'objet dans la base de données.
    * @param o est  une instance de l'objet à créer.
    */
   @Override
   public void createObject(final T o){
      //return (PK) getSession().save(o);
      //em.persist(o);
      //em.persist(o);
      if(o != null){
         getJpaTemplate().persist(o);
      }else{
         log.debug("Tentative de creation d'un objet null");
      }
   }

   /**
    *   Retrouve un objet qui était persistant dans la base de données en
    *   utilisant sa clé primaire.
    *   @param id est la clé primaire de l'objet.
    *   @return l'objet.
    */
   @Override
   public T findById(final PK id){
      //return (T) getSession().get(type, id);
      //return (T) em.find(type, id);
      if(id != null){
         return getJpaTemplate().find(type, id);
      }

      log.debug("FindById avec PK null");
      return null;
   }

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param o est l'objet à mettre à jour dans la base
    * de données.
    */
   @Override
   public void updateObject(final T o){
      //getSession().update(o);
      //em.merge(o);
      if(o != null){
         getJpaTemplate().merge(o);
      }else{
         log.debug("Tentative de modification d'un objet null");
      }
   }

   @Override
   public T mergeObject(final T o){
      if(o != null){
         return getJpaTemplate().merge(o);
      }

      log.debug("Tentative de merge d'un objet null");
      return null;
   }

   /**
    * Supprime un objet de la base de données.
    * @param id est la clé primaire de l'objet à supprimer.
    */
   @Override
   public void removeObject(final PK id){
      //getSession().delete(o);
      /*T o = em.find(type, id);
      em.remove(o);*/
      if(id != null){
         final T o = getJpaTemplate().find(type, id);
         if(o != null){
            getJpaTemplate().remove(o);
         }else{
            log.debug("Tentative de suppression d'un objet null");
         }
      }else{
         log.debug("Tentative de suppression d'un objet dont Id null");
      }
   }

   /**
    * Renvoie tous les objets d'une certaine table présents dans la base
    * de données.
    * @return tous les objets d'une table.
    */
   @Override
   public List<T> findAll(){

      final List<T> list = getJpaTemplate().find("select p from " + type.getSimpleName() + " p");
      /**
       * Essai avec JpaCallBack mais getJpaTemplate().
       * find(query) est tout aussi efficace!
       */
      /*List<T> list = (List<T>) getJpaTemplate().execute(new JpaCallback() {
      	public Object doInJpa(final EntityManager em)
      		throws javax.persistence.PersistenceException {
      		StringBuilder queryStr = new StringBuilder();
      		queryStr.append("SELECT em FROM ");
      		queryStr.append(type.getSimpleName());
      		queryStr.append(" em ");
      		//if (ordClause.length() > 0) {
      		//	queryStr.append(" em ORDER BY ");
      		//}
      
      		logger.debug(queryStr.toString());
      

      		final Query query = em.createQuery(queryStr.toString());
      		return query.getResultList();
      	}
      });*/
      return list;
   }

   /**
    * Exécute une méthode de type findBy qui correspond à une requête SELECT
    * avec paramètres sur la base de données.
    * @param method est la requête à exécuter.
    * @param queryArgs sont les arguments de a requête.
    * @return une liste d'objets résultants de la requête.
    */

   @Override
   public List<T> executeFinder(final Method method, final Object[] queryArgs){
      final String queryName = queryNameFromMethod(method);

      final List<T> list = (List<T>) getJpaTemplate().execute(new JpaCallback<Object>()
      {
         @Override
         public Object doInJpa(final EntityManager em){
            final Query query = em.createNamedQuery(queryName);
            for(int i = 0; i < queryArgs.length; ++i){
               query.setParameter(i + 1, queryArgs[i]);
            }
            return query.getResultList();
         }
      });
      return list;
   }

   
   public Long executeCounter(final Method method, final Object[] queryArgs){
      final String queryName = queryNameFromMethod(method);

      final Long nb = (Long) getJpaTemplate().execute(new JpaCallback<Object>()
      {
         @Override
         public Long doInJpa(final EntityManager em){
            final Query query = em.createNamedQuery(queryName);
            for(int i = 0; i < queryArgs.length; ++i){
               query.setParameter(i + 1, queryArgs[i]);
            }
            return (Long) query.getSingleResult();
         }
      });
      return nb;
   }
   
   
   
   /**
    * Renvoie le nom de la NamedQuery pour la méthode passée en paramètre.
    * @param finderMethod est la méthode qui contient la requête.
    * @return le nom de la NamedQuery.
    */
   public String queryNameFromMethod(final Method finderMethod){
      return type.getSimpleName() + "." + finderMethod.getName();
   }

}
