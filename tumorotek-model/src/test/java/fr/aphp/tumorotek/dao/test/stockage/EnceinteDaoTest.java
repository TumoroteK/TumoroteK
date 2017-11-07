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
package fr.aphp.tumorotek.dao.test.stockage;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteTypeDao;
import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.EnceinteType;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * 
 * Classe de test pour le DAO EnceinteDao et le bean 
 * du domaine Enceinte.
 * 
 * @author Pierre Ventadour.
 * @version 18/03/2010
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class EnceinteDaoTest extends AbstractDaoTest {

	/** Bean Dao. */
	private BanqueDao banqueDao;
	/** Bean Dao. */
	private ConteneurDao conteneurDao;
	/** Bean Dao. */
	private EnceinteDao enceinteDao;
	/** Bean Dao. */
	private EnceinteTypeDao enceinteTypeDao;
	/** Bean Dao. */
	private EntiteDao entiteDao;
	/** Bean Dao. */
	private CouleurDao couleurDao;
	
	public EnceinteDaoTest() {
		
	}

	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}

	public void setConteneurDao(ConteneurDao cDao) {
		this.conteneurDao = cDao;
	}

	public void setEnceinteDao(EnceinteDao eDao) {
		this.enceinteDao = eDao;
	}

	public void setEnceinteTypeDao(EnceinteTypeDao eDao) {
		this.enceinteTypeDao = eDao;
	}

	public void setEntiteDao(EntiteDao eDao) {
		this.entiteDao = eDao;
	}
	
	public void setCouleurDao(CouleurDao cDao) {
		this.couleurDao = cDao;
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<Enceinte> liste = enceinteDao.findAll();
		assertTrue(liste.size() == 7);
	}
	
	/**
	 * Test l'appel de la méthode findByConteneurAndNom().
	 */
	public void testFindByConteneurAndNom() {
		Conteneur c1 = conteneurDao.findById(1);
		List<Enceinte> liste = enceinteDao.findByConteneurAndNom(
				c1, "R1");
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getNom().equals("R1"));
		
		liste = enceinteDao.findByConteneurAndNom(c1, "jvhq");
		assertTrue(liste.size() == 0);
		
		Conteneur c2 = conteneurDao.findById(2);
		liste = enceinteDao.findByConteneurAndNom(c2, "R1");
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByConteneurAndNom(null, "R1");
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByEnceintePereAndNom().
	 */
	public void testFindByEnceintePereAndNom() {
		Enceinte e1 = enceinteDao.findById(1);
		List<Enceinte> liste = enceinteDao.findByEnceintePereAndNom(
				e1, "T1");
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getNom().equals("T1"));
		
		liste = enceinteDao.findByEnceintePereAndNom(e1, "udsc");
		assertTrue(liste.size() == 0);
		
		Enceinte e2 = enceinteDao.findById(3);
		liste = enceinteDao.findByEnceintePereAndNom(e2, "T1");
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByEnceintePereAndNom(null, "T1");
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByConteneurWithOrder().
	 */
	public void testFindByConteneurWithOrder() {
		Conteneur c1 = conteneurDao.findById(1);
		List<Enceinte> liste = enceinteDao.findByConteneurWithOrder(c1);
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(0).getNom().equals("R1"));
		
		Conteneur c2 = conteneurDao.findById(2);
		liste = enceinteDao.findByConteneurWithOrder(c2);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByConteneurWithOrder(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByEnceintePereWithOrder().
	 */
	public void testFindByEnceintePereWithOrder() {
		Enceinte e1 = enceinteDao.findById(1);
		List<Enceinte> liste = enceinteDao.findByEnceintePereWithOrder(e1);
		assertTrue(liste.size() == 3);
		assertTrue(liste.get(0).getNom().equals("T1"));
		
		Enceinte e2 = enceinteDao.findById(3);
		liste = enceinteDao.findByEnceintePereWithOrder(e2);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByEnceintePereWithOrder(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByConteneurSelectNom().
	 */
	public void testFindByConteneurSelectNom() {
		Conteneur c1 = conteneurDao.findById(1);
		List<String> liste = enceinteDao.findByConteneurSelectNom(c1);
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(0).equals("R1"));
		
		Conteneur c2 = conteneurDao.findById(2);
		liste = enceinteDao.findByConteneurSelectNom(c2);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByConteneurSelectNom(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByEnceintePereSelectNom().
	 */
	public void testFindByEnceintePereSelectNom() {
		Enceinte e1 = enceinteDao.findById(1);
		List<String> liste = enceinteDao.findByEnceintePereSelectNom(e1);
		assertTrue(liste.size() == 3);
		assertTrue(liste.get(0).equals("T1"));
		
		Enceinte e2 = enceinteDao.findById(3);
		liste = enceinteDao.findByEnceintePereSelectNom(e2);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByEnceintePereSelectNom(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findNumberEnceinteFilles().
	 */
	public void testFindNumberEnceinteFilles() {
		Enceinte e1 = enceinteDao.findById(1);
		List<Long> liste = enceinteDao.findNumberEnceinteFilles(e1);
		assertTrue(liste.get(0) == 3);
		
		Enceinte e2 = enceinteDao.findById(3);
		liste = enceinteDao.findNumberEnceinteFilles(e2);
		assertTrue(liste.get(0) == 0);
		
		liste = enceinteDao.findNumberEnceinteFilles(null);
		assertTrue(liste.get(0) == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByEnceintePereAndPosition().
	 */
	public void testFindByEnceintePereAndPosition() {
		Enceinte e1 = enceinteDao.findById(1);
		List<Enceinte> liste = enceinteDao.findByEnceintePereAndPosition(e1, 1);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getNom().equals("T1"));
		
		liste = enceinteDao.findByEnceintePereAndPosition(e1, 10);
		assertTrue(liste.size() == 0);
		
		Enceinte e2 = enceinteDao.findById(3);
		liste = enceinteDao.findByEnceintePereAndPosition(e2, 1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByEnceintePereAndPosition(null, 1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByEnceintePereAndPosition(e1, null);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByEnceintePereAndPosition(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByEnceintePereAndPositionExcludedId().
	 */
	public void testFindByEnceintePereAndPositionExcludedId() {
		Enceinte e1 = enceinteDao.findById(1);
		List<Enceinte> liste = enceinteDao
			.findByEnceintePereAndPositionExcludedId(e1, 1, 10);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getNom().equals("T1"));
		
		liste = enceinteDao
			.findByEnceintePereAndPositionExcludedId(e1, 1, 3);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByEnceintePereAndPositionExcludedId(e1, 10, 3);
		assertTrue(liste.size() == 0);
		
		Enceinte e2 = enceinteDao.findById(3);
		liste = enceinteDao.findByEnceintePereAndPositionExcludedId(e2, 1, 3);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByEnceintePereAndPositionExcludedId(null, 1, 3);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByEnceintePereAndPositionExcludedId(
				e1, null, 3);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByEnceintePereAndPositionExcludedId(
				null, null, 3);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByEnceintePereAndPositionExcludedId(
				null, null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByConteneurAndPosition().
	 */
	public void testFindByConteneurAndPosition() {
		Conteneur c1 = conteneurDao.findById(1);
		List<Enceinte> liste = enceinteDao.findByConteneurAndPosition(c1, 1);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getNom().equals("R1"));
		
		liste = enceinteDao.findByConteneurAndPosition(c1, 10);
		assertTrue(liste.size() == 0);
		
		Conteneur c2 = conteneurDao.findById(3);
		liste = enceinteDao.findByConteneurAndPosition(c2, 1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByConteneurAndPosition(null, 1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByConteneurAndPosition(c1, null);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByConteneurAndPosition(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByConteneurAndPositionExcludedId().
	 */
	public void testFindByConteneurAndPositionExcludedId() {
		Conteneur c1 = conteneurDao.findById(1);
		List<Enceinte> liste = enceinteDao
			.findByConteneurAndPositionExcludedId(c1, 1, 10);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getNom().equals("R1"));
		
		liste = enceinteDao
			.findByConteneurAndPositionExcludedId(c1, 1, 1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByConteneurAndPositionExcludedId(c1, 10, 3);
		assertTrue(liste.size() == 0);
		
		Conteneur c2 = conteneurDao.findById(3);
		liste = enceinteDao.findByConteneurAndPositionExcludedId(c2, 1, 3);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByConteneurAndPositionExcludedId(null, 1, 3);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByConteneurAndPositionExcludedId(
				c1, null, 3);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByConteneurAndPositionExcludedId(
				null, null, 3);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByConteneurAndPositionExcludedId(
				null, null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedIdWithPere().
	 */
	public void testFindByExcludedIdWithPere() {
		Conteneur c1 = conteneurDao.findById(1);
		List<Enceinte> liste = enceinteDao.findByExcludedIdWithConteneur(
				1, c1);
		assertTrue(liste.size() == 1);
		
		liste = enceinteDao.findByExcludedIdWithConteneur(10, c1);
		assertTrue(liste.size() == 2);
		
		liste = enceinteDao.findByExcludedIdWithConteneur(1, null);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByExcludedIdWithConteneur(null, c1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByExcludedIdWithConteneur(null, null);
		assertTrue(liste.size() == 0);
		
	}
	
	/**
	 * Test l'appel de la méthode findByTwoExcludedIdsWithConteneur().
	 */
	public void testFindByTwoExcludedIdsWithConteneur() {
		Conteneur c1 = conteneurDao.findById(1);
		List<Enceinte> liste = enceinteDao.findByTwoExcludedIdsWithConteneur(
				1, 2, c1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithConteneur(1, 3, c1);
		assertTrue(liste.size() == 1);
		
		liste = enceinteDao.findByTwoExcludedIdsWithConteneur(10, 11, c1);
		assertTrue(liste.size() == 2);
		
		liste = enceinteDao.findByTwoExcludedIdsWithConteneur(1, 2, null);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithConteneur(null, 1, c1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithConteneur(1, null, c1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithConteneur(null, null, c1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithConteneur(1, null, null);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithConteneur(null, 1, null);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithConteneur(null, null, null);
		assertTrue(liste.size() == 0);
		
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedIdWithEnceinte().
	 */
	public void testFindByExcludedIdWithEnceinte() {
		Enceinte e1 = enceinteDao.findById(1);
		List<Enceinte> liste = enceinteDao.findByExcludedIdWithEnceinte(
				1, e1);
		assertTrue(liste.size() == 3);
		
		liste = enceinteDao.findByExcludedIdWithEnceinte(3, e1);
		assertTrue(liste.size() == 2);
		
		liste = enceinteDao.findByExcludedIdWithEnceinte(3, null);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByExcludedIdWithEnceinte(null, e1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByExcludedIdWithEnceinte(null, null);
		assertTrue(liste.size() == 0);
		
	}
	
	/**
	 * Test l'appel de la méthode findByTwoExcludedIdsWithEnceinte().
	 */
	public void testFindByTwoExcludedIdsWithEnceinte() {
		Enceinte e1 = enceinteDao.findById(1);
		List<Enceinte> liste = enceinteDao.findByTwoExcludedIdsWithEnceinte(
				1, 2, e1);
		assertTrue(liste.size() == 3);
		
		liste = enceinteDao.findByTwoExcludedIdsWithEnceinte(3, 4, e1);
		assertTrue(liste.size() == 1);
		
		liste = enceinteDao.findByTwoExcludedIdsWithEnceinte(3, 7, e1);
		assertTrue(liste.size() == 2);
		
		liste = enceinteDao.findByTwoExcludedIdsWithEnceinte(3, 4, null);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithEnceinte(3, null, e1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithEnceinte(null, 3, e1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithEnceinte(null, 4, null);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithEnceinte(3, null, null);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithEnceinte(null, null, e1);
		assertTrue(liste.size() == 0);
		
		liste = enceinteDao.findByTwoExcludedIdsWithEnceinte(null, null, null);
		assertTrue(liste.size() == 0);
		
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'une Enceinte.
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	public void testCrud() throws Exception {
		
		Enceinte e = new Enceinte();
		Enceinte pere = enceinteDao.findById(1);
		Conteneur c = conteneurDao.findById(1);
		EnceinteType type = enceinteTypeDao.findById(1);
		Entite ent = entiteDao.findById(3);
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		Set<Banque> banques = new HashSet<Banque>();
		banques.add(b1);
		String nomUp = "UP";
		Couleur c1 = couleurDao.findById(1);
		
		e.setEnceinteType(type);
		e.setConteneur(c);
		e.setEnceintePere(pere);
		e.setNom("NOM");
		e.setPosition(1);
		e.setAlias("ALIAS");
		e.setNbPlaces(10);
		e.setEntite(ent);
		e.setBanques(banques);
		e.setArchive(false);
		e.setCouleur(c1);
		
		// Test de l'insertion
		enceinteDao.createObject(e);
		assertEquals(new Integer(8), e.getEnceinteId());
		
		Enceinte e2 = enceinteDao.findById(new Integer(8));
		// Vérification des données entrées dans la base
		assertNotNull(e2);
		assertNotNull(e2.getEnceintePere());
		assertNotNull(e2.getConteneur());
		assertNotNull(e2.getEnceinteType());
		assertNotNull(e2.getEntite());
		assertNotNull(e2.getCouleur());
		assertTrue(e2.getNom().equals("NOM"));
		assertTrue(e2.getAlias().equals("ALIAS"));
		assertTrue(e2.getPosition() == 1);
		assertTrue(e2.getNbPlaces() == 10);
		assertTrue(e2.getBanques().size() == 1);
		
		// Test de la mise à jour
		e2.setNom(nomUp);
		banques.add(b2);
		enceinteDao.updateObject(e2);
		assertTrue(enceinteDao.findById(
				new Integer(8)).getNom().equals(nomUp));
		assertTrue(enceinteDao.findById(
				new Integer(8)).getBanques().size() == 2);
		
		// Test de la délétion
		enceinteDao.removeObject(new Integer(8));
		assertNull(enceinteDao.findById(new Integer(8)));
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 * @throws ParseException 
	 */
	public void testEquals() throws ParseException {
		String nom = "Enc1";
		String nom2 = "Enc2";		
		Conteneur c1 = conteneurDao.findById(1);
		Conteneur c2 = conteneurDao.findById(2);
		Enceinte p1 = enceinteDao.findById(1);
		Enceinte p2 = enceinteDao.findById(2);
		Enceinte e1 = new Enceinte();
		Enceinte e2 = new Enceinte();
		
		// L'objet 1 n'est pas égal à null
		assertFalse(e1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(e1.equals(e1));
		
		/*null*/
		assertTrue(e1.equals(e2));
		assertTrue(e2.equals(e1));
		
		/*Nom*/
		e2.setNom(nom);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
		e1.setNom(nom2);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
		e1.setNom(nom);
		assertTrue(e1.equals(e2));
		assertTrue(e2.equals(e1));
		
		/*Enceinte*/
		e2.setEnceintePere(p1);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
		e1.setEnceintePere(p2);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
		e1.setEnceintePere(p1);
		assertTrue(e1.equals(e2));
		
		/*Conteneur*/
		e2.setConteneur(c1);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
		e1.setConteneur(c2);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
		e1.setConteneur(c1);
		assertTrue(e1.equals(e2));
		assertTrue(e2.equals(e1));

		Categorie c3 = new Categorie();
		assertFalse(e1.equals(c3));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 * @throws ParseException 
	 */
	public void testHashCode() throws ParseException {
		String nom = "Enc1";
		String nom2 = "Enc2";		
		Conteneur c1 = conteneurDao.findById(1);
		Conteneur c2 = conteneurDao.findById(2);
		Enceinte p1 = enceinteDao.findById(1);
		Enceinte p2 = enceinteDao.findById(2);
		Enceinte e1 = new Enceinte();
		Enceinte e2 = new Enceinte();
		
		/*null*/
		assertTrue(e1.hashCode() == e2.hashCode());
		
		/*Nom*/
		e2.setNom(nom);
		assertFalse(e1.hashCode() == e2.hashCode());
		e1.setNom(nom2);
		assertFalse(e1.hashCode() == e2.hashCode());
		e1.setNom(nom);
		assertTrue(e1.hashCode() == e2.hashCode());
		
		/*Date*/
		e2.setEnceintePere(p1);
		assertFalse(e1.hashCode() == e2.hashCode());
		e1.setEnceintePere(p2);
		assertFalse(e1.hashCode() == e2.hashCode());
		e1.setEnceintePere(p1);
		assertTrue(e1.hashCode() == e2.hashCode());
		
		/*Conteneur*/
		e2.setConteneur(c1);
		assertFalse(e1.hashCode() == e2.hashCode());
		e1.setConteneur(c2);
		assertFalse(e1.hashCode() == e2.hashCode());
		e1.setConteneur(c1);
		assertTrue(e1.hashCode() == e2.hashCode());
		
		// un même objet garde le même hashcode dans le temps
		int hash = e1.hashCode();
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());
		
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		Enceinte e1 = enceinteDao.findById(1);
		assertTrue(e1.toString().
				equals("{" + e1.getNom() + "}"));
		
		Enceinte e2 = new Enceinte();
		assertTrue(e2.toString().equals("{Empty Enceinte}"));
	}
	
	/**
	 * Test la méthode clone.
	 */
	public void testClone() {
		Enceinte enceinte1 = enceinteDao.findById(1);
		Enceinte enceinte2 = new Enceinte();
		enceinte2 = enceinte1.clone();
		
		assertTrue(enceinte1.equals(enceinte2));
		
		if (enceinte1.getEnceinteId() != null) {
			assertTrue(enceinte1.getEnceinteId() == enceinte2.getEnceinteId());
		} else {
			assertNull(enceinte2.getEnceinteId());
		}
		
		if (enceinte1.getNom() != null) {
			assertTrue(enceinte1.getNom().equals(enceinte2.getNom()));
		} else {
			assertNull(enceinte2.getNom());
		}
		
		if (enceinte1.getEnceinteType() != null) {
			assertTrue(enceinte1.getEnceinteType()
					.equals(enceinte2.getEnceinteType()));
		} else {
			assertNull(enceinte2.getEnceinteType());
		}
		
		if (enceinte1.getConteneur() != null) {
			assertTrue(enceinte1.getConteneur()
					.equals(enceinte2.getConteneur()));
		} else {
			assertNull(enceinte2.getConteneur());
		}
		
		if (enceinte1.getEnceintePere() != null) {
			assertTrue(enceinte1.getEnceintePere()
					.equals(enceinte2.getEnceintePere()));
		} else {
			assertNull(enceinte2.getEnceintePere());
		}
		
		if (enceinte1.getPosition() != null) {
			assertTrue(enceinte1.getPosition()
					== enceinte2.getPosition());
		} else {
			assertNull(enceinte2.getPosition());
		}
		
		if (enceinte1.getAlias() != null) {
			assertTrue(enceinte1.getAlias()
					.equals(enceinte2.getAlias()));
		} else {
			assertNull(enceinte2.getAlias());
		}
		
		if (enceinte1.getNbPlaces() != null) {
			assertTrue(enceinte1.getNbPlaces()
					== enceinte2.getNbPlaces());
		} else {
			assertNull(enceinte2.getNbPlaces());
		}
		
		if (enceinte1.getEntite() != null) {
			assertTrue(enceinte1.getEntite()
					.equals(enceinte2.getEntite()));
		} else {
			assertNull(enceinte2.getEntite());
		}
		
		if (enceinte1.getCouleur() != null) {
			assertTrue(enceinte1.getCouleur()
					.equals(enceinte2.getCouleur()));
		} else {
			assertNull(enceinte2.getCouleur());
		}
		
		if (enceinte1.getArchive() != null) {
			assertTrue(enceinte1.getArchive()
					.equals(enceinte2.getArchive()));
		} else {
			assertNull(enceinte2.getArchive());
		}
		
		if (enceinte1.getBanques() != null) {
			assertTrue(enceinte1.getBanques()
					.equals(enceinte2.getBanques()));
		} else {
			assertNull(enceinte2.getBanques());
		}
		
		if (enceinte1.getEnceintes() != null) {
			assertTrue(enceinte1.getEnceintes()
					.equals(enceinte2.getEnceintes()));
		} else {
			assertNull(enceinte2.getEnceintes());
		}
		
		if (enceinte1.getTerminales() != null) {
			assertTrue(enceinte1.getTerminales()
					.equals(enceinte2.getTerminales()));
		} else {
			assertNull(enceinte2.getTerminales());
		}
		
	}
	
}
