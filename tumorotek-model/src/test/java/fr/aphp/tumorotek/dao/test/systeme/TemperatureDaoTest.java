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

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.systeme.TemperatureDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Temperature;

/**
 * 
 * Classe de test pour le DAO TemperatureDao et le bean du domaine Temperature.
 * Classe de test créée le 07/07/2010.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class TemperatureDaoTest extends AbstractDaoTest {

	/** Bean Dao. */
	private TemperatureDao temperatureDao;
	
	public TemperatureDaoTest() {
		
	}

	public void setTemperatureDao(TemperatureDao tDao) {
		this.temperatureDao = tDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testFindAll() {
		List<Temperature> liste = temperatureDao.findAll();
		assertTrue(liste.size() == 5);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	public void testFindByExcludedId() {
		List<Temperature> liste = temperatureDao.findByExcludedId(1);
		assertTrue(liste.size() == 4);
		
		liste = temperatureDao.findByExcludedId(10);
		assertTrue(liste.size() == 5);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'une température.
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Rollback(false)
	public void testCrudCouleur() throws Exception {
		Temperature t = new Temperature();
		
		Float temp = new Float(50);
		Float tempUp = new Float(70);
		t.setTemperature(temp);
		// Test de l'insertion
		temperatureDao.createObject(t);
		assertEquals(new Integer(6), t.getTemperatureId());
		
		// Test de la mise à jour
		Temperature t2 = temperatureDao.findById(new Integer(6));
		assertNotNull(t2);
		assertTrue(t2.getTemperature().equals(temp));
		t2.setTemperature(tempUp);
		temperatureDao.updateObject(t2);
		assertTrue(temperatureDao.findById(new Integer(6))
				.getTemperature().equals(tempUp));
		
		// Test de la délétion
		temperatureDao.removeObject(new Integer(6));
		assertNull(temperatureDao.findById(new Integer(6)));
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		Float temp1 = new Float(50);
		Float temp2 = new Float(70);
		Temperature t1 = new Temperature();
		t1.setTemperature(temp1);
		Temperature t2 = new Temperature();
		t2.setTemperature(temp1);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(t1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(t1.equals(t1));
		// 2 objets sont égaux entre eux
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));
		
		// Vérification de la différenciation de 2 objets
		t2.setTemperature(temp2);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		
		t2.setTemperature(null);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		
		t1.setTemperature(null);
		assertTrue(t1.equals(t2));
		t2.setTemperature(temp1);
		assertFalse(t1.equals(t2));
		
		Banque b = new Banque();
		assertFalse(t1.equals(b));
		
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		Float temp1 = new Float(50);
		Temperature t1 = new Temperature();
		t1.setTemperature(temp1);
		Temperature t2 = new Temperature();
		t2.setTemperature(temp1);
		Temperature t3 = new Temperature();
		t3.setTemperature(null);
		assertTrue(t3.hashCode() > 0);
		
		int hash = t1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(t1.hashCode() == t2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());
		
	}
	
}
