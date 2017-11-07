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
import fr.aphp.tumorotek.dao.contexte.CoordonneeDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.test.PopulateBeanForTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 * 
 * Classe de test pour le DAO TransporteurDao et le bean du 
 * domaine Transporteur.
 * 
 * Date: 09/09/2009
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class TransporteurDaoTest extends AbstractDaoTest {
	
	/** Bean Dao TransporteurDao. */
	private TransporteurDao transporteurDao;
	/** Bean Dao CoordonneeDao. */
	private CoordonneeDao coordonneeDao;
	/** valeur du nom pour la maj. */
	private String updatedNom = "Transporteur mis a jour";

	/** Constructeur. */
	public TransporteurDaoTest() {
		
	}

	/**
	 * Setter du bean Dao TransporteurDao.
	 * @param tDao est le bean Dao.
	 */
	public void setTransporteurDao(TransporteurDao tDao) {
		this.transporteurDao = tDao;
	}

	/**
	 * Setter du bean Dao CoordonneeDao.
	 * @param cDao est le bean Dao.
	 */
	public void setCoordonneeDao(CoordonneeDao cDao) {
		this.coordonneeDao = cDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAllTransorteurs() {
		List<Transporteur> transporteurs = transporteurDao.findAll();
		assertTrue(transporteurs.size() == 3);
	}
	
	public void testFindByExcludedId() {
		List<Transporteur> transporteurs = transporteurDao.findByExcludedId(1);
		assertTrue(transporteurs.size() == 2);
		
		transporteurs = transporteurDao.findByExcludedId(10);
		assertTrue(transporteurs.size() == 3);
	}
	
	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	public void testFindByOrder() {
		List<Transporteur> list = transporteurDao.findByOrder();
		assertTrue(list.size() == 3);
		assertTrue(list.get(0).getNom().equals("HOPITAL ST LOUIS - ANAPATH"));
	}
	
	/**
	 * Test l'appel de la méthode findByNom().
	 */
	public void testFindByNom() {
		List<Transporteur> transporteurs = transporteurDao.
			findByNom("HOPITAL ST LOUIS - ANAPATH");
		assertTrue(transporteurs.size() == 1);
		transporteurs = transporteurDao.findByNom("BICHAT");
		assertTrue(transporteurs.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByContactNom().
	 */
	public void testfindByContactNom() {
		List<Transporteur> transporteurs = transporteurDao.
			findByContactNom("ME PISSANERO");
		assertTrue(transporteurs.size() == 1);
		transporteurs = transporteurDao.findByContactNom("TEST");
		assertTrue(transporteurs.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByArchive().
	 */
	public void testFindByArchive() {
		List<Transporteur> transporteurs = transporteurDao.findByArchive(false);
		assertTrue(transporteurs.size() == 2);
	}
	
	/**
	 * Test l'appel de la méthode findByCoordonnee().
	 */
	public void testFindByCoordonnee() {
		Coordonnee c = coordonneeDao.findById(1);
		List<Transporteur> transporteurs = transporteurDao.
			findByCoordonnee(c);
		assertTrue(transporteurs.size() == 1);
		c = coordonneeDao.findById(5);
		transporteurs = transporteurDao.findByCoordonnee(c);
		assertTrue(transporteurs.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByIdWithFetch().
	 */
	public void testFindByIdWithFetch() {
		List<Transporteur> transporteurs = transporteurDao.findByIdWithFetch(1);
		assertTrue(transporteurs.size() == 1);
		Transporteur transporteur = transporteurs.get(0);
		assertNotNull(transporteur);
		assertTrue(transporteur.getCoordonnee().getCoordonneeId() == 1);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'un transporteur.
	 * @throws Exception lance une exception en cas d'erreurs sur les données.
	 */
	@Rollback(false)
	public void testTransporteur() throws Exception {
		
		Transporteur t = new Transporteur();
		//Coordonnee c = coordonneeDao.findById(7);
		//Coordonnee c = new Coordonnee();
		
		// on remplit le nouveau transporteur avec les données du fichier
		// "transporteur.properties"
		try {
			PopulateBeanForTest.populateBean(t, "transporteur");
			//PopulateBeanForTest.populateBean(c, "coordonnee");
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}
		//t.setCoordonnee(c);
		// Test de l'insertion
		transporteurDao.createObject(t);
		
		// Test de la mise à jour
		Transporteur t2 = transporteurDao.findById(t.getTransporteurId());
		assertNotNull(t2);
		assertTrue(t2.getNom().equals("Nouveau transporteur"));
		assertTrue(t2.getContactNom().equals("MR Test"));
		assertTrue(t2.getContactPrenom().equals("Test"));
		assertTrue(t2.getContactTel().equals("0145628975"));
		assertTrue(t2.getContactFax().equals("0145628985"));
		assertTrue(t2.getContactMail().equals("toto@yahoo.fr"));
		//assertNotNull(t2.getCoordonnee());
		t2.setNom(updatedNom);
		transporteurDao.updateObject(t2);
		assertTrue(transporteurDao.findById(t2.getTransporteurId())
										.getNom().equals(updatedNom));
		
		// Test de la délétion
		transporteurDao.removeObject(t2.getTransporteurId());
		assertNull(transporteurDao.findById(t2.getTransporteurId()));
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String nom = "Nom";
		String nom2 = "Nom2";
		String cnom = "Contact Nom";
		String cnom2 = "Contact Nom2";
		Transporteur t1 = new Transporteur();
		t1.setNom(nom);
		t1.setContactNom(cnom);
		Transporteur t2 = new Transporteur();
		t2.setNom(nom);
		t2.setContactNom(cnom);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(t1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(t1.equals(t1));
		// 2 objets sont égaux entre eux
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));
		
		// Vérification de la différenciation de 2 objets
		t2.setNom(nom2);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		t2.setNom(nom);
		t2.setContactNom(cnom2);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		
		t2.setContactNom(null);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		t2.setNom(null);
		t2.setContactNom(cnom);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		
		Categorie c3 = new Categorie();
		assertFalse(t1.equals(c3));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String nom = "Nom";
		String cnom = "Contact Nom";
		String cPrenom = "Prenom";
		String tel = "546541313";
		String fax = "4453435131";
		String mail = "mail@mail.fr";
		Transporteur t1 = new Transporteur();
		t1.setTransporteurId(1);
		t1.setNom(nom);
		t1.setContactNom(cnom);
		t1.setContactPrenom(cPrenom);
		t1.setContactTel(tel);
		t1.setContactFax(fax);
		t1.setContactMail(mail);
		t1.setArchive(true);		
		Transporteur t2 = new Transporteur();
		t2.setNom(nom);
		t2.setContactNom(cnom);
		t2.setContactPrenom(cPrenom);
		t2.setContactTel(tel);
		t2.setContactFax(fax);
		t2.setContactMail(mail);
		t2.setArchive(true);
		Transporteur t3 = new Transporteur();
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

	/**
	 * Test la méthode clone.
	 */
	public void testClone() {
		Transporteur t1 = transporteurDao.findById(1);
		Transporteur t2 = t1.clone();
		assertTrue(t1.equals(t2));
		
		if (t1.getTransporteurId() != null) {
			assertTrue(t1.getTransporteurId() == t2.getTransporteurId());
		} else {
			assertNull(t2.getTransporteurId());
		}
		
		if (t1.getNom() != null) {
			assertTrue(t1.getNom().equals(t2.getNom()));
		} else {
			assertNull(t2.getNom());
		}
		
		if (t1.getContactNom() != null) {
			assertTrue(t1.getContactNom().equals(t2.getContactNom()));
		} else {
			assertNull(t2.getContactNom());
		}
		
		if (t1.getContactPrenom() != null) {
			assertTrue(t1.getContactPrenom().equals(t2.getContactPrenom()));
		} else {
			assertNull(t2.getContactPrenom());
		}
		
		if (t1.getContactTel() != null) {
			assertTrue(t1.getContactTel().equals(t2.getContactTel()));
		} else {
			assertNull(t2.getContactTel());
		}
		
		if (t1.getContactFax() != null) {
			assertTrue(t1.getContactFax().equals(t2.getContactFax()));
		} else {
			assertNull(t2.getContactFax());
		}
		
		if (t1.getContactMail() != null) {
			assertTrue(t1.getContactMail().equals(t2.getContactMail()));
		} else {
			assertNull(t2.getContactMail());
		}
		
		if (t1.getCoordonnee() != null) {
			assertTrue(t1.getCoordonnee().equals(t2.getCoordonnee()));
		} else {
			assertNull(t2.getCoordonnee());
		}
		
		assertTrue(t1.getArchive() == t2.getArchive());
	}
	
}