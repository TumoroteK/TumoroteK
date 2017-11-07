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
package fr.aphp.tumorotek.dao.test.qualite;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.qualite.ConformiteTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * 
 * Classe de test pour le DAO ConformiteTypeDao et le 
 * bean du domaine ConformiteType.
 * Date:  08/11/2011
 * 
 * @author Pierre Ventadour.
 * @version 2.0.10
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ConformiteTypeDaoTest extends AbstractDaoTest {
	
	private ConformiteTypeDao conformiteTypeDao;
	private EntiteDao entiteDao;
	
	public ConformiteTypeDaoTest() {
		
	}

	public void setConformiteTypeDao(ConformiteTypeDao cDao) {
		this.conformiteTypeDao = cDao;
	}
	
	public void setEntiteDao(EntiteDao e) {
		this.entiteDao = e;
	}

	private int tot = 5;
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<ConformiteType> liste = conformiteTypeDao.findAll();
		assertTrue(liste.size() == tot);
	}
	
	/**
	 * Test l'appel de la méthode findByType().
	 */
	public void testFindByEntiteAndType() {
		List<ConformiteType> liste = conformiteTypeDao
				.findByEntiteAndType("Arrivee", entiteDao.findById(2));
		assertTrue(liste.size() == 1);
		
		liste = conformiteTypeDao.findByEntiteAndType("Arrivee", null);
		assertTrue(liste.size() == 0);
		
		liste = conformiteTypeDao.findByEntiteAndType("Arrivee", entiteDao.findById(3));
		assertTrue(liste.size() == 0);
		
		liste = conformiteTypeDao.findByEntiteAndType("Traitement", entiteDao.findById(3));
		assertTrue(liste.size() == 1);
		
		liste = conformiteTypeDao.findByEntiteAndType("Traitement", entiteDao.findById(8));
		assertTrue(liste.size() == 1);
		
		liste = conformiteTypeDao.findByEntiteAndType("Test", entiteDao.findById(2));
		assertTrue(liste.size() == 0);
		
		liste = conformiteTypeDao.findByEntiteAndType(null, entiteDao.findById(2));
		assertTrue(liste.size() == 0);
	}
	
	@Rollback(false)
	public void testCrud() throws Exception {
		
		ConformiteType type = new ConformiteType();
		type.setConformiteType("Test");
		type.setEntite(entiteDao.findById(8));
		
		// Test de l'insertion
		conformiteTypeDao.createObject(type);
		Integer id = new Integer(tot + 1);
		assertEquals(id, type.getConformiteTypeId());
		
		ConformiteType c2 = conformiteTypeDao.findById(id);
		// Vérification des données entrées dans la base
		assertNotNull(c2);
		assertTrue(c2.getConformiteType().equals("Test"));
		assertTrue(c2.getEntite().getNom().equals("ProdDerive"));
		
		// Test de la mise à jour
		c2.setConformiteType("UP");
		conformiteTypeDao.updateObject(c2);
		assertTrue(conformiteTypeDao.findById(id).getConformiteType().equals("UP"));
		
		// Test de la délétion
		conformiteTypeDao.removeObject(id);
		assertNull(conformiteTypeDao.findById(id));
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String nom = "NOM";
		String nom2 = "NOM2";
		Entite e1 = entiteDao.findById(3);
		Entite e2 = entiteDao.findById(8);
		ConformiteType c1 = new ConformiteType();
		c1.setConformiteType(nom);
		c1.setEntite(e1);
		ConformiteType c2 = new ConformiteType();
		c2.setConformiteType(nom);
		c2.setEntite(e1);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(c1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(c1.equals(c1));
		// 2 objets sont égaux entre eux
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
		
		// Vérification de la différenciation de 2 objets
		c2.setConformiteType(nom2);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		
		c2.setConformiteType(null);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		
		c1.setConformiteType(null);
		assertTrue(c1.equals(c2));
		c2.setConformiteType(nom);
		assertFalse(c1.equals(c2));
		
		c1.setConformiteType(nom);
		assertTrue(c2.equals(c1));
		
		c2.setEntite(e2);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		
		c2.setEntite(null);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		
		c1.setEntite(null);
		assertTrue(c1.equals(c2));
		c2.setEntite(e1);
		assertFalse(c1.equals(c2));
		
		Categorie c = new Categorie();
		assertFalse(c1.equals(c));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String nom = "NOM";
		Entite e1 = entiteDao.findById(2);
		ConformiteType c1 = new ConformiteType();
		c1.setConformiteType(nom);
		c1.setEntite(e1);
		ConformiteType c2 = new ConformiteType();
		c2.setConformiteType(nom);
		c2.setEntite(e1);
		ConformiteType c3 = new ConformiteType();
		c3.setConformiteType(null);
		assertTrue(c3.hashCode() > 0);
		
		int hash = c1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(c1.hashCode() == c2.hashCode());
		assertFalse(c1.hashCode() == c3.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		ConformiteType c1 = conformiteTypeDao.findById(1);
		assertTrue(c1.toString().
				equals("{" + c1.getConformiteType() + "}"));
		
		ConformiteType c2 = new ConformiteType();
		assertTrue(c2.toString().equals("{Empty ConformiteType}"));
	}

}
