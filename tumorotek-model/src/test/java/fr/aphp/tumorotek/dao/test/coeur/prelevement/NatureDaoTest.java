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
package fr.aphp.tumorotek.dao.test.coeur.prelevement;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 * 
 * Classe de test pour le DAO NatureDao et le 
 * bean du domaine Nature (de prelevement).
 * Classe de test créée le 01/10/09.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class NatureDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private NatureDao natureDao;
	private PlateformeDao plateformeDao;
	
	/** Valeur du nature pour la maj. */
	private String updatedNature = "Nature mis a jour";
	
	/**
	 * Constructeur.
	 */
	public NatureDaoTest() {
	}
	
	/**
	 * Setter du bean Dao.
	 * @param nDao est le bean Dao.
	 */
	public void setNatureDao(NatureDao nDao) {
		this.natureDao = nDao;
	}
	
	public void setPlateformeDao(PlateformeDao pDao) {
		this.plateformeDao = pDao;
	}

	/**
	 * Test l'appel de la méthode toString().
	 */
	public void testToString() {
		Nature n1 = natureDao.findById(1);
		assertTrue(n1.toString().equals("{" + n1.getNature() + "}"));
		n1 = new Nature();
		assertTrue(n1.toString().equals("{Empty Nature}"));
	}	
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAllNatures() {
		List<Nature> natures = natureDao.findAll();
		assertTrue(natures.size() == 4);
	}
	
	public void testFindByOrder() {
		Plateforme pf = plateformeDao.findById(1);
		List< ? extends TKThesaurusObject> list = 
								natureDao.findByOrder(pf);
		assertTrue(list.size() == 3);
		assertTrue(list.get(2).getNom().equals("TISSU"));
		pf = plateformeDao.findById(2);
		list = natureDao.findByOrder(pf);
		assertTrue(list.size() == 1);
		list = natureDao.findByOrder(null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByNature().
	 */
	public void testFindByNature() {
		List<Nature> natures = natureDao.findByNature("LIQUIDE D'ASCITE");
		assertTrue(natures.size() == 1);
		natures = natureDao.findByNature("URINES");
		assertTrue(natures.size() == 0);
		natures = natureDao.findByNature("LIQU%");
		assertTrue(natures.size() == 1);
		natures = natureDao.findByNature(null);
		assertTrue(natures.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	public void testFindByExcludedId() {
		List<Nature> liste = natureDao.findByExcludedId(1);
		assertTrue(liste.size() == 3);
		Nature nature = liste.get(0);
		assertNotNull(nature);
		assertTrue(nature.getNatureId() == 2);
		
		liste = natureDao.findByExcludedId(15);
		assertTrue(liste.size() == 4);
	}
		
	/**
	 * Test l'insertion, la mise à jour et la suppression 
	 * d'une nature de prelevement.
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Rollback(false)
	public void testCrudNature() throws Exception {
		Nature n = new Nature();		
		n.setNature("URINES");
		n.setPlateforme(plateformeDao.findById(1));
		// Test de l'insertion
		natureDao.createObject(n);
		assertEquals(new Integer(5), n.getNatureId());
		
		// Test de la mise à jour
		Nature n2 = natureDao.findById(new Integer(5));
		assertNotNull(n2);
		assertTrue(n2.getNature().equals("URINES"));
		n2.setNature(updatedNature);
		natureDao.updateObject(n2);
		assertTrue(natureDao.findById(new Integer(5)).getNature()
				.equals(updatedNature));
		
		// Test de la délétion
		natureDao.removeObject(new Integer(5));
		assertNull(natureDao.findById(new Integer(5)));
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String nature = "Nature";
		String nature2 = "Nature2";
		Plateforme pf1 = plateformeDao.findById(1);
		Plateforme pf2 = plateformeDao.findById(2);
		Nature n1 = new Nature();
		n1.setNature(nature);
		Nature n2 = new Nature();
		n2.setNature(nature);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(n1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(n1.equals(n1));
		// 2 objets sont égaux entre eux
		assertTrue(n1.equals(n2));
		assertTrue(n2.equals(n1));
		
		// Vérification de la différenciation de 2 objets
		n2.setNature(nature2);
		assertFalse(n1.equals(n2));
		assertFalse(n2.equals(n1));
		
		//passe la clef naturelle nature a nulle pour un des objets
		n2.setNature(null);
		assertFalse(n1.equals(n2));
		assertFalse(n2.equals(n1));
		
		//passe la clef naturelle nature a nulle pour l'autre objet
		n1.setNature(null);
		assertTrue(n1.equals(n2));
		n2.setNature(nature);
		assertFalse(n1.equals(n2));
		
		//plateforme
		n1.setNature(n2.getNature());
		n1.setPlateforme(pf1);
		n2.setPlateforme(pf1);
		assertTrue(n1.equals(n2));
		n2.setPlateforme(pf2);
		assertFalse(n1.equals(n2));
		
		//dummy test
		Banque b = new Banque();
		assertFalse(n1.equals(b));
		
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String nature = "Nature";
		Nature n1 = new Nature();
		n1.setNatureId(1);
		n1.setNature(nature);
		Nature n2 = new Nature();
		n2.setNatureId(2);
		n2.setNature(nature);
		Nature n3 = new Nature();
		n3.setNatureId(3);
		n3.setNature(null);
		assertTrue(n3.hashCode() > 0);
		
		Plateforme pf1 = plateformeDao.findById(1);
		Plateforme pf2 = plateformeDao.findById(2);
		n1.setPlateforme(pf1);
		n2.setPlateforme(pf1);
		n3.setPlateforme(pf2);
		
		int hash = n1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(n1.hashCode() == n2.hashCode());
		assertFalse(n1.hashCode() == n3.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == n1.hashCode());
		assertTrue(hash == n1.hashCode());
		assertTrue(hash == n1.hashCode());
		assertTrue(hash == n1.hashCode());
		
	}
	
	/**
	 * Test la méthode clone.
	 */
	public void testClone() {
		Nature n1 = natureDao.findById(1);
		Nature n2 = n1.clone();
		
		assertTrue(n1.equals(n2));
		
		if (n1.getNatureId() != null) {
			assertTrue(n1.getNatureId() == n2.getNatureId());
		} else {
			assertNull(n2.getNatureId());
		}
		
		if (n1.getNature() != null) {
			assertTrue(n1.getNature().equals(n2.getNature()));
		} else {
			assertNull(n2.getNature());
		}
		assertTrue(n1.getPlateforme().equals(n2.getPlateforme()));
	}

}
