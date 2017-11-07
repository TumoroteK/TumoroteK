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
package fr.aphp.tumorotek.dao.test.interfacage;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.interfacage.BlocExterneDao;
import fr.aphp.tumorotek.dao.interfacage.ValeurExterneDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;

/**
 * 
 * Classe de test pour le DAO ValeurExterneDao 
 * et le bean du domaine ValeurExterne.
 * 
 * @author Pierre Ventadour.
 * @version 05/10/2011
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ValeurExterneDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private ValeurExterneDao valeurExterneDao;
	/** Bean Dao. */
	private BlocExterneDao blocExterneDao;
	
	public ValeurExterneDaoTest() {
		
	}
	
	@Override
	protected String[] getConfigLocations()	{
		return new String[]{ "applicationContextDao-interfacages.xml" };
	}

	public void setValeurExterneDao(ValeurExterneDao vDao) {
		this.valeurExterneDao = vDao;
	}

	public void setBlocExterneDao(BlocExterneDao bDao) {
		this.blocExterneDao = bDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<ValeurExterne> liste = valeurExterneDao.findAll();
		assertTrue(liste.size() >= 10);
	}
	
	/**
	 * Test l'appel de la méthode findByBlocExterne().
	 */
	public void testFindByBlocExterne() {
		BlocExterne b1 = blocExterneDao.findById(1);
		List<ValeurExterne> liste = valeurExterneDao.findByBlocExterne(b1);
		assertTrue(liste.size() == 4);
		
		BlocExterne b3 = blocExterneDao.findById(3);
		liste = valeurExterneDao.findByBlocExterne(b3);
		assertTrue(liste.size() == 0);
		
		liste = valeurExterneDao.findByBlocExterne(null);
		assertTrue(liste.size() == 0);
	}
	
	@Rollback(false)
	public void testCrud() throws Exception {
		
		BlocExterne b1 = blocExterneDao.findById(1);
		ValeurExterne v1 = new ValeurExterne();
		v1.setBlocExterne(b1);
		
		// Test de l'insertion
		Integer id = valeurExterneDao.findAll().size() + 1;
		valeurExterneDao.createObject(v1);
		assertEquals(new Integer(id), v1.getValeurExterneId());
		
		ValeurExterne v2 = valeurExterneDao.findById(new Integer(id));
		// Vérification des données entrées dans la base
		assertNotNull(v2);
		assertNotNull(v2.getBlocExterne());
		assertNull(v2.getValeur());
		assertNull(v2.getChampAnnotationId());
		assertNull(v2.getChampEntiteId());
		
		// Test de la mise à jour
		v2.setValeur("VALEUR");
		v2.setChampAnnotationId(1);
		v2.setChampEntiteId(2);
		v2.setContenu("CONTENT...".getBytes());
		valeurExterneDao.updateObject(v2);
		assertTrue(valeurExterneDao.findById(
				new Integer(id)).getValeur().equals("VALEUR"));
		assertTrue(valeurExterneDao.findById(
				new Integer(id)).getChampAnnotationId() == 1);
		assertTrue(valeurExterneDao.findById(
				new Integer(id)).getChampEntiteId() == 2);
		assertEquals(new String(valeurExterneDao.findById(
				new Integer(id)).getContenu()), "CONTENT...");
		
		// Test de la délétion
		valeurExterneDao.removeObject(new Integer(id));
		assertNull(valeurExterneDao.findById(new Integer(id)));
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		Integer c1 = 1;
		Integer c2 = 2;
		Integer a1 = 1;
		Integer a2 = 2;
		BlocExterne b1 = blocExterneDao.findById(1);
		BlocExterne b2 = blocExterneDao.findById(2);
		ValeurExterne v1 = new ValeurExterne();
		ValeurExterne v2 = new ValeurExterne();
		
		// L'objet 1 n'est pas égal à null
		assertFalse(v1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(v1.equals(v1));
		
		/*null*/
		assertTrue(v1.equals(v2));
		assertTrue(v2.equals(v1));
		
		v2.setChampAnnotationId(a1);
		assertFalse(v1.equals(v2));
		assertFalse(v2.equals(v1));
		v1.setChampAnnotationId(a2);
		assertFalse(v1.equals(v2));
		assertFalse(v2.equals(v1));
		v1.setChampAnnotationId(a1);
		assertTrue(v1.equals(v2));
		assertTrue(v2.equals(v1));
		
		v2.setChampEntiteId(c1);
		assertFalse(v1.equals(v2));
		assertFalse(v2.equals(v1));
		v1.setChampEntiteId(c2);
		assertFalse(v1.equals(v2));
		assertFalse(v2.equals(v1));
		v1.setChampEntiteId(c1);
		assertTrue(v1.equals(v2));
		
		v2.setBlocExterne(b1);
		assertFalse(v1.equals(v2));
		assertFalse(v2.equals(v1));
		v1.setBlocExterne(b2);
		assertFalse(v1.equals(v2));
		assertFalse(v2.equals(v1));
		v1.setBlocExterne(b1);
		assertTrue(v1.equals(v2));
		assertTrue(v2.equals(v1));
		
		Categorie c3 = new Categorie();
		assertFalse(v1.equals(c3));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		Integer c1 = 1;
		Integer c2 = 2;
		Integer a1 = 1;
		Integer a2 = 2;
		BlocExterne b1 = blocExterneDao.findById(1);
		BlocExterne b2 = blocExterneDao.findById(2);
		ValeurExterne v1 = new ValeurExterne();
		ValeurExterne v2 = new ValeurExterne();
		
		/*null*/
		assertTrue(v1.hashCode() == v2.hashCode());
		
		v2.setChampAnnotationId(a1);
		assertFalse(v1.hashCode() == v2.hashCode());
		v1.setChampAnnotationId(a2);
		assertFalse(v1.hashCode() == v2.hashCode());
		v1.setChampAnnotationId(a1);
		assertTrue(v1.hashCode() == v2.hashCode());
		
		v2.setChampEntiteId(c1);
		assertFalse(v1.hashCode() == v2.hashCode());
		v1.setChampEntiteId(c2);
		assertFalse(v1.hashCode() == v2.hashCode());
		v1.setChampEntiteId(c1);
		assertTrue(v1.hashCode() == v2.hashCode());
		
		v2.setBlocExterne(b1);
		assertFalse(v1.hashCode() == v2.hashCode());
		v1.setBlocExterne(b2);
		assertFalse(v1.hashCode() == v2.hashCode());
		v1.setBlocExterne(b1);
		assertTrue(v1.hashCode() == v2.hashCode());
				
		// un même objet garde le même hashcode dans le temps
		int hash = v1.hashCode();
		assertTrue(hash == v1.hashCode());
		assertTrue(hash == v1.hashCode());
		assertTrue(hash == v1.hashCode());
		assertTrue(hash == v1.hashCode());
	}

	/**
	 * test toString().
	 */
	public void testToString() {
		ValeurExterne v1 = valeurExterneDao.findById(1);
		assertTrue(v1.toString().equals(
				"{" + v1.getChampEntiteId() + "(ChampEntite), " 
				+ v1.getChampAnnotationId() + "(ChampAnnotation), " 
				+ v1.getBlocExterne().toString() 
				+ "(BlocExterne)}"));
		
		ValeurExterne v2 = new ValeurExterne();
		assertTrue(v2.toString().equals("{Empty ValeurExterne}"));
	}

}
