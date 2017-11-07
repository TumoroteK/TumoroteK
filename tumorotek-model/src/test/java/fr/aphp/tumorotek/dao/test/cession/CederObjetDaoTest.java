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
package fr.aphp.tumorotek.dao.test.cession;

import java.text.ParseException;
import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.cession.CederObjetDao;
import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.CederObjetPK;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 * 
 * Classe de test pour le DAO CessionExamenDao et le bean 
 * du domaine CessionExamen.
 * 
 * @author Pierre Ventadour.
 * @version 25/01/2010
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class CederObjetDaoTest extends AbstractDaoTest {

	private CederObjetDao cederObjetDao;
	private CessionDao cessionDao;
	private UniteDao uniteDao;
	private EntiteDao entiteDao;
	
	public CederObjetDaoTest() {
		
	}

	public void setCederObjetDao(CederObjetDao dao) {
		this.cederObjetDao = dao;
	}

	public void setCessionDao(CessionDao dao) {
		this.cessionDao = dao;
	}

	public void setUniteDao(UniteDao dao) {
		this.uniteDao = dao;
	}

	public void setEntiteDao(EntiteDao eDao) {
		this.entiteDao = eDao;
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<CederObjet> liste = cederObjetDao.findAll();
		assertTrue(liste.size() == 6);
	}
	
	/**
	 * Test l'appel de la méthode findById().
	 */
	public void testFindById() {
		Cession c1 = cessionDao.findById(1);
		Entite e1 = entiteDao.findById(3);
		CederObjetPK pk = new CederObjetPK(c1, e1, 1);
		
		CederObjet co = cederObjetDao.findById(pk);
		assertNotNull(co);
		
		pk = new CederObjetPK(c1, e1, 15);
		co = cederObjetDao.findById(pk);
		assertNull(co);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedPK().
	 */
	public void testFindByExcludedPK() {
		Cession c1 = cessionDao.findById(1);
		Entite e1 = entiteDao.findById(3);
		CederObjetPK pk = cederObjetDao.findById(new CederObjetPK(c1, e1, 1)).getPk();
		//CederObjetPK pk = new CederObjetPK(c1, e1, 1);
		
		List<CederObjet> liste = cederObjetDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 5);
		
		pk = new CederObjetPK(c1, e1, 10);
		liste = cederObjetDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 6);
	}
	
	/**
	 * Test l'appel de la méthode findByEntite().
	 */
	public void testFindByEntite() {
		Entite e1 = entiteDao.findById(3);
		Entite e2 = entiteDao.findById(2);
		
		List<CederObjet> liste = cederObjetDao.findByEntite(e1);
		assertTrue(liste.size() == 4);
		
		liste = cederObjetDao.findByEntite(e2);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByObjetId().
	 */
	public void testFindByObjetId() {
		List<CederObjet> liste = cederObjetDao.findByObjetId(1);
		assertTrue(liste.size() == 3);
		
		liste = cederObjetDao.findByObjetId(2);
		assertTrue(liste.size() == 1);
		
		liste = cederObjetDao.findByObjetId(3);
		assertTrue(liste.size() == 2);
		
		liste = cederObjetDao.findByObjetId(4);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByEntiteObjet().
	 */
	public void testFindByEntiteObjet() {
		Entite e1 = entiteDao.findById(3);
		Entite e2 = entiteDao.findById(2);
		
		List<CederObjet> liste = cederObjetDao.findByEntiteObjet(e1, 1);
		assertTrue(liste.size() == 2);
		
		liste = cederObjetDao.findByEntiteObjet(e1, 10);
		assertTrue(liste.size() == 0);
		
		liste = cederObjetDao.findByEntiteObjet(e2, 1);
		assertTrue(liste.size() == 0);
		
		liste = cederObjetDao.findByEntiteObjet(e1, null);
		assertTrue(liste.size() == 0);
		
		liste = cederObjetDao.findByEntiteObjet(null, 1);
		assertTrue(liste.size() == 0);
		
		liste = cederObjetDao.findByEntiteObjet(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByCessionEntite().
	 */
	public void testFindByCessionEntite() {
		Entite e1 = entiteDao.findById(3);
		Entite e2 = entiteDao.findById(2);
		Cession c1 = cessionDao.findById(1);
		Cession c2 = cessionDao.findById(3);
		
		List<CederObjet> liste = cederObjetDao.findByCessionEntite(c1, e1);
		assertTrue(liste.size() == 1);
		
		liste = cederObjetDao.findByCessionEntite(c1, e2);
		assertTrue(liste.size() == 0);
		
		liste = cederObjetDao.findByCessionEntite(c2, e1);
		assertTrue(liste.size() == 0);
		
		liste = cederObjetDao.findByCessionEntite(c1, null);
		assertTrue(liste.size() == 0);
		
		liste = cederObjetDao.findByCessionEntite(null, e1);
		assertTrue(liste.size() == 0);
		
		liste = cederObjetDao.findByCessionEntite(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression 
	 * d'un cederObjet.
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 *
	 **/
	@Rollback(false)
	public void testCrudCederObjet() {
		
		CederObjet co = new CederObjet();
		Cession c = cessionDao.findById(1);
		Entite e = entiteDao.findById(3);
		Integer objetId = 2;
		float upValue = (float) 10.63598;
		Unite u = uniteDao.findById(1);
		
		co.setCession(c);
		co.setEntite(e);
		co.setObjetId(objetId);
		co.setQuantite(null);
		//co.setVolume(value);
		co.setQuantiteUnite(u);
		//co.setVolumeUnite(u);
		
		// Test de l'insertion
		cederObjetDao.createObject(co);
		assertTrue(cederObjetDao.findAll().size() == 7);	
		
		// Test de la mise à jour
		CederObjetPK pk = new CederObjetPK();
		pk.setCession(c);
		pk.setEntite(e);
		pk.setObjetId(objetId);
		CederObjet co2 = cederObjetDao.findById(pk);
		assertNotNull(co2);
		assertTrue(co2.getCession().equals(c));
		assertTrue(co2.getEntite().equals(e));
		assertTrue(co2.getObjetId() == objetId);
		assertNull(co2.getQuantite());
		assertTrue(co2.getQuantiteUnite().equals(u));
		
		//update
		co2.setQuantite(upValue);
		cederObjetDao.updateObject(co2);
		assertTrue(cederObjetDao.findById(pk).equals(co2));
		assertTrue(cederObjetDao.findById(pk).getQuantite() 
				== (float) 10.636);
		
		// Test de la délétion
		cederObjetDao.removeObject(pk);
		assertNull(cederObjetDao.findById(pk));
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 * @throws ParseException 
	 */
	public void testEquals() throws ParseException {

		CederObjet co1 = new CederObjet();
		CederObjet co2 = new CederObjet();
		
		// L'objet 1 n'est pas égal à null
		assertFalse(co1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(co1.equals(co1));
		// 2 objets sont égaux entre eux
		assertTrue(co1.equals(co2));
		assertTrue(co2.equals(co1));
	
		populateClefsToTestEqualsAndHashCode(co1, co2);
			
		//dummy test
		Banque b = new Banque();
		assertFalse(co1.equals(b));		
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 * @throws ParseException 
	 */
	public void testHashCode() throws ParseException {
		CederObjet co1 = new CederObjet();
		CederObjet co2 = new CederObjet();
		CederObjet co3 = new CederObjet();
		
		assertTrue(co1.hashCode() == co2.hashCode());
		assertTrue(co2.hashCode() == co3.hashCode());
		assertTrue(co3.hashCode() > 0);
		
		//teste dans methode precedente
		populateClefsToTestEqualsAndHashCode(co1, co2);
		
		int hash = co1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(co1.hashCode() == co2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == co1.hashCode());
		assertTrue(hash == co1.hashCode());
		assertTrue(hash == co1.hashCode());
		assertTrue(hash == co1.hashCode());
	}
	
	private void populateClefsToTestEqualsAndHashCode(CederObjet co1, 
			CederObjet co2) throws ParseException {
		
		Cession c1 = cessionDao.findById(1);
		Entite e1 = entiteDao.findById(3);
		CederObjetPK pk1 = new CederObjetPK(c1, e1, 1);
		CederObjetPK pk2 = new CederObjetPK(c1, e1, 2);
		CederObjetPK pk3 = new CederObjetPK(c1, e1, 1);
		CederObjetPK[] pks = new CederObjetPK[]{null, pk1, pk2, pk3};
				
		for (int i = 0; i < pks.length; i++) {
			for (int k = 0; k < pks.length; k++) {
								
				co1.setPk(pks[i]);				
				co2.setPk(pks[k]);
				
				if (((i == k) || (i + k == 4))) {
						assertTrue(co1.equals(co2));
						assertTrue(co1.hashCode() 
								== co2.hashCode());
				} else {
					assertFalse(co1.equals(co2));
				}
			}
		}
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		Cession c1 = cessionDao.findById(1);
		Entite e1 = entiteDao.findById(3);
		CederObjetPK pk = new CederObjetPK(c1, e1, 1);
		CederObjet co1 = cederObjetDao.findById(pk);
		
		assertTrue(co1.toString().
				equals("{" + co1.getPk().toString() + "}"));
		
		CederObjet co2 = new CederObjet();
		co2.setPk(null);
		assertTrue(co2.toString().equals("{Empty CederObjet}"));
	}
	
	/**
	 * Test la méthode clone.
	 */
	public void testClone() {
		Cession c1 = cessionDao.findById(1);
		Entite e1 = entiteDao.findById(3);
		CederObjetPK pk = new CederObjetPK(c1, e1, 1);
		
		CederObjet co1 = cederObjetDao.findById(pk);	
		CederObjet co2 = new CederObjet();
		co2 = co1.clone();
		
		assertTrue(co1.equals(co2));
				
		if (co1.getPk() != null) {
			assertTrue(co1.getPk().equals(co2.getPk()));
		} else {
			assertNull(co2.getPk());
		}
		
		if (co1.getQuantite() != null) {
			assertTrue(co1.getQuantite().equals(co2.getQuantite()));
		} else {
			assertNull(co2.getQuantite());
		}
		
		if (co1.getQuantiteUnite() != null) {
			assertTrue(co1.getQuantiteUnite().equals(co2.getQuantiteUnite()));
		} else {
			assertNull(co2.getQuantiteUnite());
		}
		
		/*if (co1.getVolume() != null) {
			assertTrue(co1.getVolume().equals(co2.getVolume()));
		} else {
			assertNull(co2.getVolume());
		}
		
		if (co1.getVolumeUnite() != null) {
			assertTrue(co1.getVolumeUnite().equals(co2.getVolumeUnite()));
		} else {
			assertNull(co2.getVolumeUnite());
		}*/
		
	}
	
	public void testCountObjectCessed() {
		Entite e1 = entiteDao.findById(3);
		Entite e2 = entiteDao.findById(2);
		Entite e8 = entiteDao.findById(8);
		Cession c1 = cessionDao.findById(1);
		Cession c2 = cessionDao.findById(2);
			
 		Long count = cederObjetDao.findObjectsCessedCount(c1, e1).get(0);
		assertTrue(count == 1);
		
		count = cederObjetDao.findObjectsCessedCount(c1, e8).get(0);
		assertTrue(count == 1);
		
		count = cederObjetDao.findObjectsCessedCount(c1, e2).get(0);
		assertTrue(count == 0);
		
		count = cederObjetDao.findObjectsCessedCount(c2, e1).get(0);
		assertTrue(count == 3);
		
		count = cederObjetDao.findObjectsCessedCount(c2, e8).get(0);
		assertTrue(count == 0);
		
		count = cederObjetDao.findObjectsCessedCount(c1, null).get(0);
		assertTrue(count == 0);
		
		count = cederObjetDao.findObjectsCessedCount(null, e1).get(0);
		assertTrue(count == 0);
		
		count = cederObjetDao.findObjectsCessedCount(null, null).get(0);
		assertTrue(count == 0);
	}
	
	public void testFindCodesEchantillonByCession() {
		
		Cession c1 = cessionDao.findById(1);
		Cession c2 = cessionDao.findById(2);
		Cession c3 = cessionDao.findById(3);
		
 		List<String> codes = cederObjetDao.findCodesEchantillonByCession(c1);
		assertTrue(codes.size() == 1);
		assertTrue(codes.contains("PTRA.1"));
		
		codes = cederObjetDao.findCodesEchantillonByCession(c2);
		assertTrue(codes.size() == 3);
		assertTrue(codes.get(0).equals("EHT.1"));
		assertTrue(codes.get(1).equals("PTRA.1"));
		assertTrue(codes.get(2).equals("PTRA.2"));
		
		codes = cederObjetDao.findCodesEchantillonByCession(c3);
		assertTrue(codes.isEmpty());
		
		codes = cederObjetDao.findCodesEchantillonByCession(null);
		assertTrue(codes.isEmpty());
	}
	
	public void testFindCodesDeriveByCession() {
		
		Cession c1 = cessionDao.findById(1);
		Cession c2 = cessionDao.findById(2);
		Cession c3 = cessionDao.findById(3);
		
 		List<String> codes = cederObjetDao.findCodesDeriveByCession(c1);
		assertTrue(codes.size() == 1);
		assertTrue(codes.contains("EHT.1.1"));
		
		codes = cederObjetDao.findCodesDeriveByCession(c2);
		assertTrue(codes.isEmpty());
		
		codes = cederObjetDao.findCodesDeriveByCession(c3);
		assertTrue(codes.isEmpty());
		
		codes = cederObjetDao.findCodesDeriveByCession(null);
		assertTrue(codes.isEmpty());
	}
	
	public void testFindCountObjCession() {
		// nulls
		Long cc = cederObjetDao.findCountObjCession(null, null).get(0);
		assertTrue(cc.longValue() == 0);
		cc = cederObjetDao.findCountObjCession(1, entiteDao.findById(3)).get(0);
		assertTrue(cc.longValue() == 2);
		cc =  cederObjetDao.findCountObjCession(2, entiteDao.findById(3)).get(0);
		assertTrue(cc.longValue() == 1);
		cc =  cederObjetDao.findCountObjCession(4, entiteDao.findById(3)).get(0);
		assertTrue(cc.longValue() == 0);
		cc =  cederObjetDao.findCountObjCession(1, entiteDao.findById(8)).get(0);
		assertTrue(cc.longValue() == 1);
		
	}
}
