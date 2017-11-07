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
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 * 
 * Classe de test pour le DAO ObjetStatutDao et le bean du domaine ObjetStatut.
 * 
 * @author Pierre Ventadour.
 * @version 10/09/2009
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ObjetStatutDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private ObjetStatutDao objetStatutDao;
	/** valeur du nom pour la maj. */
	private String updatedNom = "Stat mis a jour";

	/** Constructeur. */
	public ObjetStatutDaoTest() {
		
	}
	
	/**
	 * Setter du bean ObjetStatutDao.
	 * @param oDao est le bean Dao.
	 */
	public void setObjetStatutDao(ObjetStatutDao oDao) {
		this.objetStatutDao = oDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAllObjetStatuts() {
		List<ObjetStatut> objets = objetStatutDao.findAll();
		assertTrue(objets.size() == 6);
	}
	
	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	public void testFindByOrder() {
		List<ObjetStatut> list = objetStatutDao.findByOrder();
		assertTrue(list.size() == 6);
		assertTrue(list.get(0).getStatut().equals("DETRUIT"));
	}
	
	/**
	 * Test l'appel de la méthode findByStatut().
	 */
	public void testFindByStatut() {
		List<ObjetStatut> objets = objetStatutDao.findByStatut("STOCKE");
		assertTrue(objets.size() == 1);
		objets = objetStatutDao.findByStatut("CEDE");
		assertTrue(objets.size() == 0);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'un ObjetStatut.
	 * @throws Exception Lance une exception.
	 */
	@Rollback(false)
	public void testCrudObjetStatut() throws Exception {
		
		ObjetStatut o = new ObjetStatut();
		
		o.setStatut("CEDE");
		// Test de l'insertion
		objetStatutDao.createObject(o);
		assertEquals(new Integer(7), o.getObjetStatutId());
		
		// Test de la mise à jour
		ObjetStatut o2 = objetStatutDao.findById(new Integer(7));
		assertNotNull(o2);
		assertTrue(o2.getStatut().equals("CEDE"));
		o2.setStatut(updatedNom);
		objetStatutDao.updateObject(o2);
		assertTrue(objetStatutDao.findById(
				new Integer(7)).getStatut().equals(updatedNom));
		
		// Test de la délétion
		objetStatutDao.removeObject(new Integer(7));
		assertNull(objetStatutDao.findById(new Integer(7)));
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String statut = "cede";
		String statut2 = "non cede";
		ObjetStatut o1 = new ObjetStatut();
		o1.setStatut(statut);
		ObjetStatut o2 = new ObjetStatut();
		o2.setStatut(statut);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(o1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(o1.equals(o1));
		// 2 objets sont égaux entre eux
		assertTrue(o1.equals(o2));
		assertTrue(o2.equals(o1));
		
		// Vérification de la différenciation de 2 objets
		o2.setStatut(statut2);
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
		
		o2.setStatut(null);
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
		o1.setStatut(null);
		assertTrue(o1.equals(o2));
		o2.setStatut(statut);
		assertFalse(o1.equals(o2));
		
		Categorie c = new Categorie();
		assertFalse(o1.equals(c));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String statut = "cede";
		ObjetStatut o1 = new ObjetStatut(1, statut);
		ObjetStatut o2 = new ObjetStatut(2, statut);
		ObjetStatut o3 = new ObjetStatut(3, null);
		assertTrue(o3.hashCode() > 0);
		
		int hash = o1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(o1.hashCode() == o2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());
		
	}
	
	public void testToString() {
		ObjetStatut obj = objetStatutDao.findById(1);
		assertTrue(obj.toString().equals("{" + obj.getStatut() + "}"));
		
		ObjetStatut obj2 = new ObjetStatut();
		assertTrue(obj2.toString().equals("{Empty ObjetStatut}"));
	}

}
