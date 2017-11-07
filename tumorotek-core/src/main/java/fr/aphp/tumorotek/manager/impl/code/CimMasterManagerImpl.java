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
package fr.aphp.tumorotek.manager.impl.code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.code.CimMasterDao;
import fr.aphp.tumorotek.manager.code.CimMasterManager;
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.code.CodeCommon;

/**
 * 
 * Implémentation du manager du bean de domaine CimMaster.
 * Date: 20/05/2010.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class CimMasterManagerImpl implements CimMasterManager {
	
	private Log log = LogFactory.getLog(CimMasterManager.class);
	
	private CimMasterDao cimMasterDao;
	private EntityManagerFactory entityManagerFactory;

	public void setCimMasterDao(CimMasterDao cDao) {
		this.cimMasterDao = cDao;
	}

	public void setEntityManagerFactory(EntityManagerFactory emFactory) {
		this.entityManagerFactory = emFactory;
	}

	@Override
	public List< ? extends CodeCommon> findAllObjectsManager() {
		return cimMasterDao.findAll();
	}

	@Override
	public List< ? extends CodeCommon> findByCodeLikeManager(String code,
			boolean exactMatch) {
		if (!exactMatch) {
			code = "%" + code + "%";
		}
		log.debug("Recherche Cim par code: " 
				+ code + " exactMatch " + String.valueOf(exactMatch));
		return cimMasterDao.findByCodeLike(code);
	}

	@Override
	public List< ? extends CodeCommon> findByLibelleLikeManager(String libelle,
			boolean exactMatch) {
		if (!exactMatch) {
			libelle = "%" + libelle + "%";
		}
		log.debug("Recherche Cim par libelle: " 
				+ libelle + " exactMatch " + String.valueOf(exactMatch));
		// List<CimMaster> cims = new ArrayList<CimMaster>();
		// List<CimLibelle> libs = cimLibelleDao.findByLibelleLike(libelle);
		// for (int i = 0; i < libs.size(); i++) {
		//	cims.add(libs.get(i).getCimMaster());
		//}		
		return cimMasterDao.findByLibelleLike(libelle);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CimMaster> findByCimParentManager(CimMaster parent) {
		List<CimMaster> cims = new ArrayList<CimMaster>();
		if (parent != null) {
			if (parent.getLevel() < 7) { // ->| id7 ds codif
				try {
					String idColname = "id" + parent.getLevel();
					Integer levelId;
				
					levelId = (Integer) PropertyUtils.getSimpleProperty(
														parent, idColname);
					StringBuffer sb = new StringBuffer();
					sb.append("SELECT c FROM CimMaster c WHERE c.");
					sb.append(idColname);
					sb.append(" = ");
					sb.append(levelId);
					sb.append(" AND c.level = ");
					sb.append(parent.getLevel() + 1);
									
					EntityManager em = 
									entityManagerFactory.createEntityManager();
					Query query = em.createQuery(sb.toString());
					cims = (List<CimMaster>) query.getResultList();
				} catch (Exception e) {
					log.error("level cimMaster introuvable par PropertyUtils");
				}
			} 
		} else { // renvoie les codes de niveau 1
			cims = cimMasterDao.findByLevel(1);
		}
		
		return cims;
	}

	@Override
	public Set<Adicap> getAdicapsManager(CimMaster cim) {
		Set<Adicap> adicaps = new HashSet<Adicap>();
		CimMaster cimM = cimMasterDao.mergeObject(cim);
		adicaps = cimM.getAdicaps();
		adicaps.size(); // operation empechant LazyInitialisationException
		return adicaps;
	}
	
	@Override
	public List<CimMaster> findChildrenCodesManager(CimMaster code) {
		List<CimMaster> codes = new ArrayList<CimMaster>();
		if (code != null) {
			codes.add(code);
			findRecursiveChildren(code, codes);
		}
		return codes;
	}
	
	private void findRecursiveChildren(CimMaster parent, 
											List<CimMaster> codes) {
		List<CimMaster> children  = findByCimParentManager(parent);
		codes.addAll(children);
		for (int i = 0; i < children.size(); i++) {
			findRecursiveChildren(children.get(i), codes);
		}
	}

	@Override
	public CimMaster findByIdManager(Integer codeId) {
		return cimMasterDao.findById(codeId);
	}
}
