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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.interfacage.DossierExterneDao;
import fr.aphp.tumorotek.dao.interfacage.EmetteurDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;

/**
 * 
 * Classe de test.
 * 
 * @author Pierre Ventadour.
 * @version 2.1
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class DossierExterneDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private DossierExterneDao dossierExterneDao;
	/** Bean Dao. */
	private EmetteurDao emetteurDao;
	
	public DossierExterneDaoTest() {
		
	}
	
	@Override
	protected String[] getConfigLocations()	{
		return new String[]{ "applicationContextDao-interfacages.xml" };
	}

	public void setDossierExterneDao(DossierExterneDao dDao) {
		this.dossierExterneDao = dDao;
	}

	public void setEmetteurDao(EmetteurDao eDao) {
		this.emetteurDao = eDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<DossierExterne> liste = dossierExterneDao.findAll();
		assertTrue(liste.size() == 6);
	}
	
	/**
	 * Test l'appel de la méthode findByEmetteur().
	 */
	public void testFindByEmetteur() {
		Emetteur e1 = emetteurDao.findById(1);
		List<DossierExterne> liste = dossierExterneDao.findByEmetteur(e1);
		assertTrue(liste.size() == 0);
		
		Emetteur e2 = emetteurDao.findById(2);
		liste = dossierExterneDao.findByEmetteur(e2);
		assertTrue(liste.size() == 4);
		assertTrue(liste.get(0).getIdentificationDossier().equals("758910000"));
		
		Emetteur e3 = emetteurDao.findById(3);
		liste = dossierExterneDao.findByEmetteur(e3);
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(0).getIdentificationDossier().equals("PRLVT1"));
		
		liste = dossierExterneDao.findByEmetteur(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByEmetteurAndIdentification().
	 */
	public void testFindByEmetteurAndIdentification() {
		Emetteur e2 = emetteurDao.findById(2);
		List<DossierExterne> liste = dossierExterneDao
			.findByEmetteurAndIdentification(e2, "758910000");
		assertTrue(liste.size() == 1);
		
		liste = dossierExterneDao
			.findByEmetteurAndIdentification(e2, "kljsvoi");
		assertTrue(liste.size() == 0);
		
		Emetteur e1 = emetteurDao.findById(1);
		liste = dossierExterneDao.findByEmetteurAndIdentification(
				e1, "758910000");
		assertTrue(liste.size() == 0);
		
		liste = dossierExterneDao.findByEmetteurAndIdentification(
				null, "758910000");
		assertTrue(liste.size() == 0);
		
		liste = dossierExterneDao.findByEmetteurAndIdentification(e2, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByIdentification().
	 */
	public void testFindByIdentification() {
		List<DossierExterne> liste = dossierExterneDao
			.findByIdentification("758910000");
		assertTrue(liste.size() == 1);
		
		liste = dossierExterneDao
			.findByIdentification("kljsvoi");
		assertTrue(liste.size() == 0);
		
		liste = dossierExterneDao.findByIdentification("X459GCT");
		assertTrue(liste.size() == 2);
		
		liste = dossierExterneDao.findByIdentification(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByEmetteurInListAndIdentification().
	 */
	public void testFindByEmetteurInListAndIdentification() {
		List<Emetteur> emetteurs = new ArrayList<Emetteur>();
		Emetteur e2 = emetteurDao.findById(2);
		emetteurs.add(e2);
		List<DossierExterne> liste = dossierExterneDao
			.findByEmetteurInListAndIdentification(
					emetteurs, "X459GCT");
		assertTrue(liste.size() == 1);
		
		Emetteur e3 = emetteurDao.findById(3);
		emetteurs.add(e3);
		liste = dossierExterneDao
			.findByEmetteurInListAndIdentification(
					emetteurs, "X459GCT");
		assertTrue(liste.size() == 2);
		
		liste = dossierExterneDao
			.findByEmetteurInListAndIdentification(emetteurs, "kljsvoi");
		assertTrue(liste.size() == 0);
		
		Emetteur e1 = emetteurDao.findById(1);
		emetteurs = new ArrayList<Emetteur>();
		emetteurs.add(e1);
		liste = dossierExterneDao.findByEmetteurInListAndIdentification(
				emetteurs, "758910000");
		assertTrue(liste.size() == 0);
		
		liste = dossierExterneDao.findByEmetteurInListAndIdentification(
				null, "758910000");
		assertTrue(liste.size() == 0);
		
		liste = dossierExterneDao.findByEmetteurInListAndIdentification(
				emetteurs, null);
		assertTrue(liste.size() == 0);
	}
	
	public void testFindByEmetteurInListSelectIdentification() {
		List<Emetteur> emetteurs = new ArrayList<Emetteur>();
		emetteurs.add(emetteurDao.findById(2));
		emetteurs.add(emetteurDao.findById(3));
		
		List<String> liste = dossierExterneDao
			.findByEmetteurInListSelectIdentification(
					emetteurs);
		assertTrue(liste.size() == 6);
		assertTrue(liste.get(0).equals("758910000"));
		
		emetteurs = new ArrayList<Emetteur>();
		emetteurs.add(emetteurDao.findById(1));
		emetteurs.add(emetteurDao.findById(3));
		liste = dossierExterneDao
			.findByEmetteurInListSelectIdentification(
				emetteurs);
		assertTrue(liste.size() == 2);
		
		liste = dossierExterneDao
			.findByEmetteurInListSelectIdentification(
				null);
		assertTrue(liste.size() == 0);
	}
	
	@Rollback(false)
	public void testCrud() throws Exception {
		
		Emetteur e = emetteurDao.findById(1);
		DossierExterne d1 = new DossierExterne();
		d1.setEmetteur(e);
		d1.setIdentificationDossier("XXXXX");
		
		// Test de l'insertion
		dossierExterneDao.createObject(d1);
		assertEquals(new Integer(7), d1.getDossierExterneId());
		
		DossierExterne d2 = dossierExterneDao.findById(new Integer(7));
		// Vérification des données entrées dans la base
		assertNotNull(d2);
		assertNotNull(d2.getEmetteur());
		assertTrue(d2.getIdentificationDossier().equals("XXXXX"));
		assertNull(d2.getDateOperation());
		assertNull(d2.getOperation());
		
		// Test de la mise à jour
		d2.setIdentificationDossier("YYYY");
		Calendar date = Calendar.getInstance();
		date.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
											.parse("17/09/2006 12:12:23"));
		d2.setDateOperation(date);
		d2.setOperation("OP");
		dossierExterneDao.updateObject(d2);
		assertTrue(dossierExterneDao.findById(
				new Integer(7)).getIdentificationDossier().equals("YYYY"));
		assertTrue(dossierExterneDao.findById(
				new Integer(7)).getDateOperation().equals(date));
		assertTrue(dossierExterneDao.findById(
				new Integer(7)).getOperation().equals("OP"));
		
		// Test de la délétion
		dossierExterneDao.removeObject(new Integer(7));
		assertNull(dossierExterneDao.findById(new Integer(7)));
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String nom = "id1";
		String nom2 = "id2";
		Emetteur e1 = emetteurDao.findById(1);
		Emetteur e2 = emetteurDao.findById(2);
		DossierExterne d1 = new DossierExterne();
		DossierExterne d2 = new DossierExterne();
		d1.setIdentificationDossier(nom);
		d1.setEmetteur(e1);
		d2.setIdentificationDossier(nom);
		d2.setEmetteur(e1);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(d1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(d1.equals(d1));
		// 2 objets sont égaux entre eux
		assertTrue(d1.equals(d2));
		assertTrue(d2.equals(d1));
		
		d1.setEmetteur(null);
		d1.setIdentificationDossier(null);
		d2.setEmetteur(null);
		d2.setIdentificationDossier(null);
		assertTrue(d1.equals(d2));
		d2.setIdentificationDossier(nom);
		assertFalse(d1.equals(d2));
		d1.setIdentificationDossier(nom);
		assertTrue(d1.equals(d2));
		d2.setIdentificationDossier(nom2);
		assertFalse(d1.equals(d2));
		d2.setIdentificationDossier(null);
		assertFalse(d1.equals(d2));
		d2.setEmetteur(e1);
		assertFalse(d1.equals(d2));
		
		d1.setEmetteur(e1);
		d1.setIdentificationDossier(null);
		d2.setIdentificationDossier(null);
		d2.setEmetteur(e1);
		assertTrue(d1.equals(d2));
		d2.setEmetteur(e2);
		assertFalse(d1.equals(d2));
		d2.setIdentificationDossier(nom);
		assertFalse(d1.equals(d2));
		
		// Vérification de la différenciation de 2 objets
		d1.setIdentificationDossier(nom);
		assertFalse(d1.equals(d2));
		d2.setIdentificationDossier(nom2);
		d2.setEmetteur(e1);
		assertFalse(d1.equals(d2));
		

		Categorie c3 = new Categorie();
		assertFalse(d1.equals(c3));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String nom = "id1";
		String nom2 = "id2";
		Emetteur e1 = emetteurDao.findById(1);
		Emetteur e2 = emetteurDao.findById(2);
		DossierExterne d1 = new DossierExterne();
		DossierExterne d2 = new DossierExterne();
		//null
		assertTrue(d1.hashCode() == d2.hashCode());
		
		//Nom
		d2.setIdentificationDossier(nom);
		assertFalse(d1.hashCode() == d2.hashCode());
		d1.setIdentificationDossier(nom2);
		assertFalse(d1.hashCode() == d2.hashCode());
		d1.setIdentificationDossier(nom);
		assertTrue(d1.hashCode() == d2.hashCode());
		
		//ProtocoleType
		d2.setEmetteur(e1);
		assertFalse(d1.hashCode() == d2.hashCode());
		d1.setEmetteur(e2);
		assertFalse(d1.hashCode() == d2.hashCode());
		d1.setEmetteur(e1);
		assertTrue(d1.hashCode() == d2.hashCode());
		
		// un même objet garde le même hashcode dans le temps
		int hash = d1.hashCode();
		assertTrue(hash == d1.hashCode());
		assertTrue(hash == d1.hashCode());
		assertTrue(hash == d1.hashCode());
		assertTrue(hash == d1.hashCode());
		
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		DossierExterne d1 = dossierExterneDao.findById(1);
		assertTrue(d1.toString().
				equals("{" + d1.getIdentificationDossier() + ", " 
				+ d1.getEmetteur().getIdentification() + "(Emetteur)}"));
		
		DossierExterne d2 = new DossierExterne();
		assertTrue(d2.toString().equals("{Empty DossierExterne}"));
	}
	
	/**
	 * @since 2.1
	 */
	public void testFindCountAll() {
		assertTrue(dossierExterneDao.findCountAll().size() == 1);
		assertTrue(dossierExterneDao.findCountAll().get(0) == 6);
	}
	
	/**
	 * @since 2.1
	 */
	public void testFindFirst() {
		List<DossierExterne> first = dossierExterneDao.findFirst();
		assertTrue(first.size() == 1);
		assertTrue(first.get(0).getDossierExterneId() == 6);
	}

}
