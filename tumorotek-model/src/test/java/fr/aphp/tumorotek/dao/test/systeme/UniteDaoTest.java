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
package fr.aphp.tumorotek.dao.test.systeme;

import java.util.List;

import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 * 
 * Classe de test pour le DAO UniteDao et le bean du domaine Unite.
 * 
 * @author Pierre Ventadour.
 * @version 10/09/2009
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class UniteDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private UniteDao uniteDao;
	/** valeur du nom pour la maj. */
	private String updatedUnite = "Unite mise a jour";

	/** Constructeur. */
	public UniteDaoTest() {
		
	}
	
	/**
	 * Setter du bean UniteDao.
	 * @param uDao est le bean Dao.
	 */
	public void setUniteDao(UniteDao uDao) {
		this.uniteDao = uDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAllUnites() {
		List<Unite> unites = uniteDao.findAll();
		assertTrue(unites.size() == 14);
	}
	
	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	public void testFindByOrder() {
		List<Unite> list = uniteDao.findByOrder();
		assertTrue(list.size() == 14);
		//assertTrue(list.get(0).getUnite().equals("10^6 CELL"));
	}
	
	/**
	 * Test l'appel de la méthode findByUnite().
	 */
	public void testFindByUnite() {
		List<Unite> unites = uniteDao.findByUnite("µg");
		assertTrue(unites.size() == 1);
		unites = uniteDao.findByUnite("L");
		assertTrue(unites.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByTypeWithOrder().
	 */
	public void testFindByTypeWithOrder() {
		List<Unite> unites = uniteDao.findByTypeWithOrder("masse");
		assertTrue(unites.size() == 4);
		assertTrue(unites.get(1).getUnite().equals("mg"));
		
		unites = uniteDao.findByTypeWithOrder("poids");
		assertTrue(unites.size() == 0);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'une Unite.
	 * @throws Exception Lance une exception en cas d'erreur.
	 */
	public void testCrudUnite() throws Exception {
		
		Unite u = new Unite();
		
		u.setUnite("ML");
		// Test de l'insertion
		uniteDao.createObject(u);
		assertEquals(new Integer(15), u.getUniteId());
		
		// Test de la mise à jour
		Unite u2 = uniteDao.findById(new Integer(15));
		assertNotNull(u2);
		assertTrue(u2.getUnite().equals("ML"));
		u2.setUnite(updatedUnite);
		uniteDao.updateObject(u2);
		assertTrue(uniteDao.findById(
				new Integer(15)).getUnite().equals(updatedUnite));
		
		// Test de la délétion
		uniteDao.removeObject(new Integer(15));
		assertNull(uniteDao.findById(new Integer(15)));
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String unite = "ML";
		String unite2 = "CL";
		String type = "type";
		String type2 = "type2";
		Unite u1 = new Unite();
		u1.setUnite(unite);
		u1.setType(type);
		Unite u2 = new Unite();
		u2.setUnite(unite);
		u2.setType(type);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(u1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(u1.equals(u1));
		// 2 objets sont égaux entre eux
		assertTrue(u1.equals(u2));
		assertTrue(u2.equals(u1));
		
		u1.setUnite(null);
		u1.setType(null);
		u2.setUnite(null);
		u2.setType(null);
		assertTrue(u1.equals(u2));
		u2.setType(type);
		assertFalse(u1.equals(u2));
		u1.setType(type);
		assertTrue(u1.equals(u2));
		u2.setType(type2);
		assertFalse(u1.equals(u2));
		u2.setType(null);
		assertFalse(u1.equals(u2));
		u2.setUnite(unite);
		assertFalse(u1.equals(u2));
		
		u1.setUnite(unite);
		u1.setType(null);
		u2.setType(null);
		u2.setUnite(unite);
		assertTrue(u1.equals(u2));
		u2.setUnite(unite2);
		assertFalse(u1.equals(u2));
		u2.setType(type);
		assertFalse(u1.equals(u2));
		
		// Vérification de la différenciation de 2 objets
		u2.setUnite(unite2);
		assertFalse(u1.equals(u2));
		assertFalse(u2.equals(u1));
		u2.setUnite(unite);
		u2.setType(type2);
		assertFalse(u1.equals(u2));
		assertFalse(u2.equals(u1));
		
		Categorie c = new Categorie();
		assertFalse(u1.equals(c));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String unite = "ML";
		String type = "type";
		Unite u1 = new Unite(1, unite, type);
		Unite u2 = new Unite(2, unite, type);
		Unite u3 = new Unite(3, null, null);
		assertTrue(u3.hashCode() > 0);
		
		int hash = u1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(u1.hashCode() == u2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == u1.hashCode());
		assertTrue(hash == u1.hashCode());
		assertTrue(hash == u1.hashCode());
		assertTrue(hash == u1.hashCode());
		
	}

	/**
	 * test toString().
	 */
	public void testToString() {
		Unite u1 = uniteDao.findById(1);
		assertTrue(u1.toString().
				equals("{" + u1.getUnite() + ", " + u1.getType() + "}"));
		
		Unite u2 = new Unite();
		assertTrue(u2.toString().equals("{Empty Unite}"));
	}
}
