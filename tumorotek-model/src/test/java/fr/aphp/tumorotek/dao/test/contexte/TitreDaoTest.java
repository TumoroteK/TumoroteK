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
package fr.aphp.tumorotek.dao.test.contexte;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import fr.aphp.tumorotek.dao.contexte.TitreDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Titre;

/**
 * 
 * Classe de test pour le DAO TitreDao et le bean du domaine Titre.
 * 
 * Date: 09/09/2009
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class TitreDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private TitreDao titreDao;
	/** valeur du nom pour la maj. */
	private String updatedNom = "Tit2";

	/** Constructeur par défaut. */
	public TitreDaoTest() {
		
	}
	
	/**
	 * Setter du bean Dao.
	 * @param tDao est le bean Dao.
	 */
	public void setTitreDao(TitreDao tDao) {
		this.titreDao = tDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAllTitres() {
		List<Titre> titres = titreDao.findAll();
		assertTrue(titres.size() == 5);
	}
	
	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	public void testFindByOrder() {
		List<Titre> list = titreDao.findByOrder();
		assertTrue(list.size() == 5);
		assertTrue(list.get(0).getTitre().equals("DR"));
	}
	
	/**
	 * Test l'appel de la méthode findByNom().
	 */
	public void testFindByTitre() {
		List<Titre> titres = titreDao.findByTitre("PR");
		assertTrue(titres.size() == 1);
		assertTrue(titres.get(0).getTitreId() == 1);
		titres = titreDao.findByTitre("TT5");
		assertTrue(titres.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByCollaborateurId().
	 */
	public void testFindByCollaborateurId() {
		List<Titre> titres = titreDao.findByCollaborateurId(4);
		assertTrue(titres.size() == 1);
		assertTrue(titres.get(0).getTitreId() == 1);
		titres = titreDao.findByCollaborateurId(10);
		assertTrue(titres.size() == 0);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'un titre.
	 * @throws Exception lance une exception.
	 */
	@Rollback(false)
	public void testCrudTitre() throws Exception {
		
		Titre t = new Titre();
		t.setTitre("TIT");
		// Test de l'insertion
		titreDao.createObject(t);
		assertEquals(new Integer(6), t.getTitreId());
		
		// Test de la mise à jour
		Titre t2 = titreDao.findById(6);
		assertNotNull(t2);
		assertTrue(t2.getTitre().equals("TIT"));
		t2.setTitre(updatedNom);
		titreDao.updateObject(t2);
		assertTrue(titreDao.findById(
				new Integer(6)).getTitre().equals(updatedNom));
		
		// Test de la délétion
		titreDao.removeObject(new Integer(6));
		assertNull(titreDao.findById(new Integer(6)));
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String titre = "Titre";
		String titre2 = "Titre2";
		Titre t1 = new Titre();
		t1.setTitre(titre);
		Titre t2 = new Titre();
		t2.setTitre(titre);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(t1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(t1.equals(t1));
		// 2 objets sont égaux entre eux
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));
		
		// Vérification de la différenciation de 2 objets
		t2.setTitre(titre2);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		
		t2.setTitre(null);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		t1.setTitre(null);
		assertTrue(t1.equals(t2));
		t2.setTitre(titre);
		assertFalse(t1.equals(t2));
		
		Categorie c = new Categorie();
		assertFalse(t1.equals(c));
		
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String titre = "Titre";
		Titre t1 = new Titre();
		t1.setTitreId(1);
		t1.setTitre(titre);
		Titre t2 = new Titre();
		t1.setTitreId(2);
		t2.setTitre(titre);
		Titre t3 = new Titre();
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