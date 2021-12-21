package fr.aphp.tumorotek.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.repository.CrudRepository;

import fr.aphp.tumorotek.dao.FinderExecutor;

public abstract class AbstractJpaDAO<T extends Serializable, PK extends Serializable> implements CrudRepository {

	   private Class< T > type;

	   @PersistenceContext(unitName="simple-jpa")
	   EntityManager entityManager;

	   public final void setClazz(Class< T > clazzToSet){
	      this.type = clazzToSet;
	   }

	   public T findById(final PK id){
	      return entityManager.find(type, id);
	   }
	   
	   public List<T> findAll(){
	      // return entityManager.createQuery( "from " + type.getName() )
	      // .getResultList();
		return entityManager.createQuery("select p from " + type.getSimpleName() + " p", type).getResultList();

	   }

	   public void createObject(T entity){
	      entityManager.persist(entity);
	   }

	   public T updateObject(final T entity){
	      return entityManager.merge(entity);
	   }

	   public void removeObject(final PK id){
		   entityManager.remove(entityManager.find(type, id));
	   }
	   
	   
		@SuppressWarnings("unchecked")
		@Override
		public List<T> executeFinder(final Method method, final Object[] queryArgs){
			final String queryName = queryNameFromMethod(method);
							
			final Query query = entityManager.createNamedQuery(queryName, type);
			for(int i = 0; i < queryArgs.length; ++i){
				query.setParameter(i + 1, queryArgs[i]);
			}
			return query.getResultList();


//			final List<T> list = (List<T>) getJpaTemplate().execute(new JpaCallback<Object>()
//			{
//				@Override
//				public Object doInJpa(final EntityManager em){
//					final Query query = em.createNamedQuery(queryName);
//					for(int i = 0; i < queryArgs.length; ++i){
//						query.setParameter(i + 1, queryArgs[i]);
//					}
//					return query.getResultList();
//				}
//			});
//			return list;
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
