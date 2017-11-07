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
package fr.aphp.tumorotek.manager.impl.io.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import fr.aphp.tumorotek.manager.io.utils.CorrespondanceIdManager;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.utils.Utils;

public class CorrespondanceIdManagerImpl implements CorrespondanceIdManager {

	private EntityManagerFactory entityManagerFactory;
	
	public CorrespondanceIdManagerImpl() {
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}
	
	@Override
	public List<Integer> findTargetIdsFromIdsManager(List<Integer> ids, 
			Entite from, Entite target, List<Banque> banks, Boolean desc) {
		EntityManager em = entityManagerFactory.createEntityManager();
		
		if (from.getNom().equals("Patient")) {
			if (target.getNom().equals("Prelevement")) {
				return findPrelevementsFromPatientsIds(ids, em, banks);
			} else if (target.getNom().equals("Echantillon")) {
				return findEchantillonsFromPrelevementsIds(
					findPrelevementsFromPatientsIds(ids, em, banks), em, banks);
			} else if (target.getNom().equals("ProdDerive")) {
				// path 1 : echantillon
				List<Integer> res =  findDerivesFromEchantillonsIds(
						findEchantillonsFromPrelevementsIds(
							findPrelevementsFromPatientsIds(ids, em, banks), 
								em, banks),
						em, banks);
				// path 2 : prelevements
				res.addAll(findDerivesFromPrelevementsIds(
						findPrelevementsFromPatientsIds(ids, em, banks), em, banks));
				
				// path 3 derives desc
				List<Integer> totDerives = new ArrayList<Integer>();
				totDerives.addAll(res);
				return findDerivesDescFromDerivesIds(res, em, banks, totDerives);
			} else if (target.getNom().equals("Cession")) {
				return findCessionsFromPrelevementsIds(findPrelevementsFromPatientsIds(ids, em, banks), em, banks);
			}
		}
		
		if (from.getNom().equals("Prelevement")) {
			if (target.getNom().equals("Patient")) {
				return findPatientsFromPrelevementsIds(ids, em);
			} else if (target.getNom().equals("Echantillon")) {
				return findEchantillonsFromPrelevementsIds(ids, em, banks);
			} else if (target.getNom().equals("ProdDerive")) {
				return findAllDerivesFromPrelevementsIds(ids, em, banks);
			} else if (target.getNom().equals("Cession")) {
				return findCessionsFromPrelevementsIds(ids, em, banks);
			} 
		}
		
		if (from.getNom().equals("Echantillon")) {
			if (target.getNom().equals("Prelevement")) {
				return findPrelevementsFromEchantillonsIds(ids, em, banks);
			} else if (target.getNom().equals("Patient")) {
				return findPatientsFromPrelevementsIds(
					findPrelevementsFromEchantillonsIds(ids, em, banks), em);
			} else if (target.getNom().equals("ProdDerive")) {
				List<Integer> res = findDerivesFromEchantillonsIds(ids, em, banks);
				// path derives desc
				List<Integer> totDerives = new ArrayList<Integer>();
				totDerives.addAll(res);
				return findDerivesDescFromDerivesIds(res, em, banks, totDerives);
			} else if (target.getNom().equals("Cession")) {
				return findCessionsFromEchantillonsIds(ids, em, banks);
			} 
		}
		
		if (from.getNom().equals("ProdDerive")) {
			List<Integer> totDerives = new ArrayList<Integer>();
			if (target.getNom().equals("Echantillon")) {
				totDerives.addAll(ids);
				return findEchantillonsFromDerivesIds(
						findDerivesAscFromDerivesIds(ids, em, banks, totDerives), em, banks);
			} else if (target.getNom().equals("Prelevement")) {
				totDerives.addAll(ids);
				// path1 echantillons
				List<Integer> res = findPrelevementsFromEchantillonsIds(
						findEchantillonsFromDerivesIds(
								findDerivesAscFromDerivesIds(ids, em, banks, totDerives), em, banks), em, banks);
				res.addAll(findPrelevementsFromDerivesIds(
						findDerivesAscFromDerivesIds(ids, em, banks, totDerives), em, banks));
				return res;
			} else if (target.getNom().equals("ProdDerive")) {
				if (desc == null || desc) { // DESC par défaut
					return findDerivesDescFromDerivesIds(ids, em, banks, totDerives);
				} else {
					return findDerivesAscFromDerivesIds(ids, em, banks, totDerives);
				}
			} else if (target.getNom().equals("Patient")) {
				totDerives.addAll(ids);
				// Obtention prelevements
				List<Integer> res = findPrelevementsFromEchantillonsIds(
						findEchantillonsFromDerivesIds(
								findDerivesAscFromDerivesIds(ids, em, banks, totDerives), em, banks), em, banks);
				res.addAll(findPrelevementsFromDerivesIds(
						findDerivesAscFromDerivesIds(ids, em, banks, totDerives), em, banks));
				
				return findPatientsFromPrelevementsIds(res, em);
			} else if (target.getNom().equals("Cession")) {
				return findCessionsFromDerivesIds(ids, em, banks);
			} 
		}
		
		if (from.getNom().equals("Cession")) {
			if (target.getNom().equals("Echantillon")) {
				return findEchantillonsFromCessionsIds(ids, em, banks);
			} else  if (target.getNom().equals("ProdDerive")) {
				return findDerivesFromCessionsIds(ids, em, banks);
			} else {
				
				List<Integer> prels = findPrelevementsFromEchantillonsIds(
						findEchantillonsFromCessionsIds(ids, em, banks), em, banks);
				prels.addAll(findPrelevementsFromDerivesIds(findDerivesFromCessionsIds(ids, em, banks), em, banks));
				if (target.getNom().equals("Prelevement")) {
					return prels;
				} else if (target.getNom().equals("Patient")) {
					return findPatientsFromPrelevementsIds(prels, em);
				}
			}
		}

		return new ArrayList<Integer>();
	}
	
	/***************** Patient - Prelevement *****************************/
	
	private List<Integer> findPrelevementsFromPatientsIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks) {
		
		String hql = "select distinct z.prelevementId from Prelevement z "
			+ " where z.maladie.patient.patientId in (:ids)";
		
		return execTypedQuery(ids, hql, em, banks);
	}
	
	private List<Integer> findPatientsFromPrelevementsIds(List<Integer> ids, 
			EntityManager em) {
		
		String hql = "select distinct z.patientId from Patient z"
			+ " join z.maladies m "
			+ " join m.prelevements s where s.prelevementId in (:ids)";
		
		return execTypedQuery(ids, hql, em, null);
	}
	
	/***************** Prelevement - Echantillon *****************************/
	
	private List<Integer> findEchantillonsFromPrelevementsIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks) {
		
		String hql = "select distinct z.echantillonId from Echantillon z "
			+ " where z.prelevement.prelevementId in (:ids)";
		
		return execTypedQuery(ids, hql, em, banks);
	}
	
	private List<Integer> findPrelevementsFromEchantillonsIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks) {
		
		String hql = "select distinct z.prelevementId from Prelevement z"
			+ " join z.echantillons e where e.echantillonId in (:ids)";
		
		return execTypedQuery(ids, hql, em, banks);
	}
	
	/***************** Echantillon - Derive *****************************/
	
	private List<Integer> findDerivesFromEchantillonsIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks) {
		
		String hql = "select distinct z.prodDeriveId from ProdDerive z "
			+ "where z.transformation.entite.entiteId = 3 "
			+ "and z.transformation.objetId in (:ids)";
		
		return execTypedQuery(ids, hql, em, banks);
	}
	
	private List<Integer> findEchantillonsFromDerivesIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks) {
		
		String hql = 
		"select distinct z.echantillonId from Echantillon z, Transformation f "
			+ "join f.prodDerives p  "
			+ "where f.objetId = z.echantillonId "
			+ "and f.entite.entiteId = 3 and p.prodDeriveId in (:ids)";
		
		return execTypedQuery(ids, hql, em, banks);
	}
	
	/***************** Prelevement - Derive *****************************/
	
	private List<Integer> findDerivesFromPrelevementsIds(List<Integer> ids, 
									EntityManager em, List<Banque> banks) {
		
		String hql = "select distinct z.prodDeriveId from ProdDerive z "
			+ "where z.transformation.entite.entiteId = 2 "
			+ "and z.transformation.objetId in (:ids)";
		
		return execTypedQuery(ids, hql, em, banks);
	}
	
	private List<Integer> findPrelevementsFromDerivesIds(List<Integer> ids, 
									EntityManager em, List<Banque> banks) {
		
		String hql = 
		"select distinct z.prelevementId from Prelevement z, Transformation f "
			+ "join f.prodDerives p  "
			+ "where f.objetId = z.prelevementId "
			+ "and f.entite.entiteId = 2 and p.prodDeriveId in (:ids)";
		
		return execTypedQuery(ids, hql, em, banks);
	}
	
	private List<Integer> findAllDerivesFromPrelevementsIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks) {
		// path 1 echantillons
		List<Integer> res = findDerivesFromEchantillonsIds(
			findEchantillonsFromPrelevementsIds(ids, em, banks), em, banks);
		// path 2 prélèvements 
		res.addAll(findDerivesFromPrelevementsIds(ids, em, banks));
		// path 3 derives desc
		List<Integer> totDerives = new ArrayList<Integer>();
		totDerives.addAll(res);
		return findDerivesDescFromDerivesIds(res, em, banks, totDerives);
	}
	
	/***************** Derive - Derive *****************************/
	
	private List<Integer> findDerivesAscFromDerivesIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks, List<Integer> tots) {

		String hql = 
				"select distinct z.prodDeriveId from ProdDerive z, Transformation f "
						+ "join f.prodDerives p  "
						+ "where f.objetId = z.prodDeriveId "
						+ "and f.entite.entiteId = 8 and p.prodDeriveId in (:ids)";
		
		List<Integer> res = execTypedQuery(ids, hql, em, banks);
		if (!res.isEmpty()) {
			tots.addAll(res);
			return findDerivesAscFromDerivesIds(res, em, banks,tots);
		} else {
			return tots;
		}
	}

	private List<Integer> findDerivesDescFromDerivesIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks, List<Integer> tots) {

		String hql = "select distinct z.prodDeriveId from ProdDerive z "
				+ "where z.transformation.entite.entiteId = 8 "
				+ "and z.transformation.objetId in (:ids)";

		List<Integer> res = execTypedQuery(ids, hql, em, banks);
		if (!res.isEmpty()) {
			tots.addAll(res);
			return findDerivesDescFromDerivesIds(res, em, banks,tots);
		} else {
			return tots;
		}	
	}
	
	/***************** Cession - Echantillon *******************************/
	
	private List<Integer> findEchantillonsFromCessionsIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks) {

		String hql = 
				"select distinct z.echantillonId from Echantillon z, CederObjet r "
					+ "where r.pk.objetId = z.echantillonId "
					+ "and r.pk.entite.entiteId = 3 and "
					+ "r.pk.cession.cessionId in (:ids)";
		
		return execTypedQuery(ids, hql, em, banks);
	}
	
	private List<Integer> findCessionsFromEchantillonsIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks) {

		String hql = 
				"select distinct z.cessionId from Cession z "
					+ "join z.cederObjets c  "
					+ "where c.pk.entite.entiteId = 3 and c.pk.objetId in (:ids)";
		
		return execTypedQuery(ids, hql, em, banks);
	}
	
	
	/***************** Cession - Echantillon *******************************/

	
	private List<Integer> findDerivesFromCessionsIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks) {

		String hql = 
				"select distinct z.prodDeriveId from ProdDerive z, CederObjet r "
					+ "where r.pk.objetId = z.prodDeriveId "
					+ "and r.pk.entite.entiteId = 8 "
					+ "and r.pk.cession.cessionId in (:ids)";
		
		return execTypedQuery(ids, hql, em, banks);
	}
	
	private List<Integer> findCessionsFromDerivesIds(List<Integer> ids, 
			EntityManager em, List<Banque> banks) {

		String hql = 
				"select distinct z.cessionId from Cession z "
					+ "join z.cederObjets c  "
					+ "where c.pk.entite.entiteId = 8 and c.pk.objetId in (:ids)";
		
		return execTypedQuery(ids, hql, em, banks);
	}
	
	private List<Integer> findCessionsFromPrelevementsIds(List<Integer> ids, 
										EntityManager em, List<Banque> banks) {
		// attention doublon cession si dérivé et échantillon ds même cession
		Set<Integer> res = new HashSet<Integer>();
		res.addAll(findCessionsFromEchantillonsIds(findEchantillonsFromPrelevementsIds(ids, em, banks), em, banks));
		res.addAll(findCessionsFromDerivesIds(findAllDerivesFromPrelevementsIds(ids, em, banks), em, banks));
		return new ArrayList<Integer>(res);
	}


	private List<Integer> execTypedQuery(List<Integer> ids, String sql, 
										EntityManager em, List<Banque> banks) {
		
		HashSet<Integer> res = new HashSet<Integer>();
		
		if (banks != null) {
			sql = sql + " and z.banque in (:banks)";
		}
		
		if (ids != null && !ids.isEmpty()) {
			
			TypedQuery<Integer> query = em.createQuery(sql, Integer.class);
		
			// 2.0.10.5
			// Oracle split list to prevent ORA-01795 not more than 1000 elements
			// are allowed for 'in' subquery
			List<List<Integer>> chunks = Utils.split(ids, 1000);
		
			for (List<Integer> chks : chunks) {
				query.setParameter("ids", chks);
				if (sql.contains("(:banks)")) {
					query.setParameter("banks", banks);
				}
				
				res.addAll(query.getResultList());
			}
		} 
		return new ArrayList<Integer>(res);
	}
	

}
