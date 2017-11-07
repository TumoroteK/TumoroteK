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
import fr.aphp.tumorotek.dao.contexte.SpecialiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Specialite;

/**
 * 
 * Classe de test pour le DAO SpecialiteDao et le bean du domaine Specialite.
 * 
 * Date: 09/09/2009
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class SpecialiteDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private SpecialiteDao specialiteDao;
	
	/** valeur du nom pour la maj. */
	private String updatedNom = "Spe mise a jour";

	/** Constructeur par défaut. **/
	public SpecialiteDaoTest() {
		
	}
	
	/**
	 * Setter du bean Dao.
	 * @param sDao est le bean Dao.
	 */
	public void setSpecialiteDao(SpecialiteDao sDao) {
		this.specialiteDao = sDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAllSpecialites() {
		List<Specialite> specialites = specialiteDao.findAll();
		assertTrue(specialites.size() == 5);
	}
	
	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	public void testFindByOrder() {
		List<Specialite> list = specialiteDao.findByOrder();
		assertTrue(list.size() == 5);
		assertTrue(list.get(1).getNom().equals("BIOCHIMIE"));
	}
	
	/**
	 * Test l'appel de la méthode findByNom().
	 */
	public void testFindByNom() {
		List<Specialite> specialites = specialiteDao.findByNom("DERMATOLOGIE");
		assertTrue(specialites.size() == 1);
		specialites = specialiteDao.findByNom("SPE5");
		assertTrue(specialites.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByCollaborateurId().
	 */
	public void testFindByCollaborateurId() {
		List<Specialite> specialites = specialiteDao.findByCollaborateurId(6);
		assertTrue(specialites.size() == 1);
		assertTrue(specialites.get(0).getSpecialiteId() == 1);
		specialites = specialiteDao.findByCollaborateurId(10);
		assertTrue(specialites.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	public void testFindByExcludedId() {
		List<Specialite> liste = specialiteDao.findByExcludedId(1);
		assertTrue(liste.size() == 4);
		Specialite spe = liste.get(0);
		assertNotNull(spe);
		assertTrue(spe.getSpecialiteId() == 2);
		
		liste = specialiteDao.findByExcludedId(15);
		assertTrue(liste.size() == 5);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'une spécialité.
	 * @throws Exception lance une exception sur le traitement des données.
	 */
	@Rollback(false)
	public void testCrudSpecialite() throws Exception {
		
		Specialite s = new Specialite();
		s.setNom("NEW SPE");
		// Test de l'insertion
		specialiteDao.createObject(s);
		assertEquals(new Integer(6), s.getSpecialiteId());
		
		// Test de la mise à jour
		Specialite s2 = specialiteDao.findById(6);
		assertNotNull(s2);
		assertTrue(s2.getNom().equals("NEW SPE"));
		s2.setNom(updatedNom);
		specialiteDao.updateObject(s2);
		assertTrue(specialiteDao.findById(
				new Integer(6)).getNom().equals(updatedNom));
		
		// Test de la délétion
		specialiteDao.removeObject(new Integer(6));
		assertNull(specialiteDao.findById(new Integer(6)));
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String nom = "Nom1";
		String nom2 = "Nom2";
		Specialite s1 = new Specialite();
		s1.setNom(nom);
		Specialite s2 = new Specialite();
		s2.setNom(nom);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(s1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(s1.equals(s1));
		// 2 objets sont égaux entre eux
		assertTrue(s1.equals(s2));
		assertTrue(s2.equals(s1));
		
		// Vérification de la différenciation de 2 objets
		s2.setNom(nom2);
		assertFalse(s1.equals(s2));
		assertFalse(s2.equals(s1));
		
		s2.setNom(null);
		assertFalse(s1.equals(s2));
		assertFalse(s2.equals(s1));
		s1.setNom(null);
		assertTrue(s1.equals(s2));
		s2.setNom(nom);
		assertFalse(s1.equals(s2));
		
		Categorie c = new Categorie();
		assertFalse(s1.equals(c));
		
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String nom = "Nom1";
		Specialite s1 = new Specialite();
		s1.setSpecialiteId(1);
		s1.setNom(nom);
		Specialite s2 = new Specialite();
		s1.setSpecialiteId(2);
		s2.setNom(nom);
		Specialite s3 = new Specialite();
		s3.setNom(null);
		assertTrue(s3.hashCode() > 0);
		
		int hash = s1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(s1.hashCode() == s2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == s1.hashCode());
		assertTrue(hash == s1.hashCode());
		assertTrue(hash == s1.hashCode());
		assertTrue(hash == s1.hashCode());
		
	}

}