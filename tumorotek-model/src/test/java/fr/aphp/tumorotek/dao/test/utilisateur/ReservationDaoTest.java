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
package fr.aphp.tumorotek.dao.test.utilisateur;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.ReservationDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.utilisateur.Reservation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Classe de test pour le DAO ReservationDao et le bean du domaine Reservation.
 * 
 * @author Pierre Ventadour.
 * @version 10/09/2009
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ReservationDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private ReservationDao reservationDao;
	/** Bean Dao. */
	private UtilisateurDao utilisateurDao;
	/** valeur du login pour la maj. */
	private String updatedDate = "28/07/2009";

	/** Constructeur. */
	public ReservationDaoTest() {
		
	}
	
	/**
	 * Setter du bean ReservationDao.
	 * @param rDao est le bean Dao.
	 */
	public void setReservationDao(ReservationDao rDao) {
		this.reservationDao = rDao;
	}

	/**
	 * Setter du bean UtilisateurDao.
	 * @param uDao est le bean Dao.
	 */
	public void setUtilisateurDao(UtilisateurDao uDao) {
		this.utilisateurDao = uDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAllReservations() {
		List<Reservation> reservations = reservationDao.findAll();
		assertTrue(reservations.size() == 4);
	}
	
	/**
	 * Test l'appel de la méthode findByDebut().
	 * @throws Exception Lance une exception en cas d'erreur.
	 */
	public void testFindByDebut() throws Exception {
		Date search = new SimpleDateFormat("dd/MM/yyyy").parse("01/06/2009");
		List<Reservation> reservations = reservationDao.findByDebut(search);
		assertTrue(reservations.size() == 1);
		search = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990");
		reservations = reservationDao.findByDebut(search);
		assertTrue(reservations.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByFin().
	 * @throws Exception Lance une exception en cas d'erreur.
	 */
	public void testFindByFin() throws Exception {
		Date search = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
		List<Reservation> reservations = reservationDao.findByFin(search);
		assertTrue(reservations.size() == 1);
		search = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990");
		reservations = reservationDao.findByFin(search);
		assertTrue(reservations.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByDebutAfterDate().
	 * @throws Exception Lance une exception en cas d'erreur.
	 */
	public void testFindByDebutAfterDate() throws Exception {
		Date search = new SimpleDateFormat("dd/MM/yyyy").parse("28/05/2009");
		List<Reservation> reservations = reservationDao.
				findByDebutAfterDate(search);
		assertTrue(reservations.size() == 2);
		search = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020");
		reservations = reservationDao.findByDebutAfterDate(search);
		assertTrue(reservations.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByFinBeforeDate().
	 * @throws Exception Lance une exception en cas d'erreur.
	 */
	public void testFindByFinBeforeDate() throws Exception {
		Date search = new SimpleDateFormat("dd/MM/yyyy").parse("15/11/2009");
		List<Reservation> reservations = reservationDao.
				findByFinBeforeDate(search);
		assertTrue(reservations.size() == 3);
		search = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990");
		reservations = reservationDao.findByFinBeforeDate(search);
		assertTrue(reservations.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByDateBetweenDebutFin().
	 * @throws Exception Lance une exception en cas d'erreur.
	 */
	public void testFindByDateBetweenDebutFin() throws Exception {
		Date search = new SimpleDateFormat("dd/MM/yyyy").parse("28/07/2009");
		List<Reservation> reservations = reservationDao.
				findByDateBetweenDebutFin(search);
		assertTrue(reservations.size() == 2);
		search = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990");
		reservations = reservationDao.findByDateBetweenDebutFin(search);
		assertTrue(reservations.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByUtilisateurId().
	 */
	public void testFindByUtilisateurId() {
		Utilisateur u = utilisateurDao.findById(1);
		List<Reservation> reservations = reservationDao.findByUtilisateur(u);
		assertTrue(reservations.size() == 2);
		Utilisateur u2 = utilisateurDao.findById(3);
		reservations = reservationDao.findByUtilisateur(u2);
		assertTrue(reservations.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByIdWithFetch().
	 */
	public void testFindByIdWithFetch() {
		List<Reservation> reservations = reservationDao.findByIdWithFetch(1);
		Reservation reservation = reservations.get(0);
		assertNotNull(reservation);
		assertTrue(reservation.getUtilisateur().getUtilisateurId() == 1);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'une reservation.
	 * @throws Exception Lance une exception en cas d'erreur.
	 */
	public void testCrudReservation() throws Exception {
		
		Reservation r = new Reservation();
		Utilisateur u = utilisateurDao.findById(2);
		Date deb = new SimpleDateFormat("dd/MM/yyyy").parse("15/02/2009");
		Date fin = new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2009");
		Date up = new SimpleDateFormat("dd/MM/yyyy").parse(updatedDate);
		// Test de l'insertion
		r.setDebut(deb);
		r.setFin(fin);
		r.setUtilisateur(u);
		reservationDao.createObject(r);
		assertEquals(new Integer(5), r.getReservationId());
		
		// Test de la mise à jour
		Reservation r2 = reservationDao.findById(new Integer(5));
		assertNotNull(r2);
		assertNotNull(r2.getUtilisateur());
		assertTrue(r2.getDebut().getTime() == deb.getTime());
		assertTrue(r2.getFin().getTime() == fin.getTime());
		r2.setDebut(up);
		reservationDao.updateObject(r2);
		assertTrue(reservationDao.findById(
				new Integer(5)).getDebut().getTime() == up.getTime());
		
		// Test de la délétion
		reservationDao.removeObject(new Integer(5));
		assertNull(reservationDao.findById(new Integer(5)));
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 * @throws Exception Lance une exception en cas d'erreur.
	 */
	public void testEquals() throws Exception {
		Date deb1 = new SimpleDateFormat("dd/MM/yyyy").parse("15/02/2009");
		Date fin1 = new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2009");
		Date deb2 = new SimpleDateFormat("dd/MM/yyyy").parse("15/02/2008");
		Date fin2 = new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2008");
		Reservation r1 = new Reservation();
		r1.setDebut(deb1);
		r1.setFin(fin1);
		Reservation r2 = new Reservation();
		r2.setDebut(deb1);
		r2.setFin(fin1);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(r1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(r1.equals(r1));
		// 2 objets sont égaux entre eux
		assertTrue(r1.equals(r2));
		assertTrue(r2.equals(r1));
		
		r1.setDebut(null);
		r1.setFin(null);
		r2.setDebut(null);
		r2.setFin(null);
		assertTrue(r1.equals(r2));
		r2.setFin(fin1);
		assertFalse(r1.equals(r2));
		r1.setFin(fin1);
		assertTrue(r1.equals(r2));
		r2.setFin(fin2);
		assertFalse(r1.equals(r2));
		r2.setFin(null);
		assertFalse(r1.equals(r2));
		r2.setDebut(deb1);
		assertFalse(r1.equals(r2));
		
		r1.setDebut(deb1);
		r1.setFin(null);
		r2.setFin(null);
		r2.setDebut(deb1);
		assertTrue(r1.equals(r2));
		r2.setDebut(deb2);
		assertFalse(r1.equals(r2));
		r2.setFin(fin1);
		assertFalse(r1.equals(r2));
		
		// Vérification de la différenciation de 2 objets
		r2.setDebut(deb2);
		assertFalse(r1.equals(r2));
		assertFalse(r2.equals(r1));
		r2.setDebut(deb1);
		r2.setFin(fin2);
		assertFalse(r1.equals(r2));
		assertFalse(r2.equals(r1));
		
		Categorie c = new Categorie();
		assertFalse(r1.equals(c));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 * @throws Exception Lance une exception en cas d'erreur.
	 */
	public void testHashCode() throws Exception {
		Date deb1 = new SimpleDateFormat("dd/MM/yyyy").parse("15/02/2009");
		Date fin1 = new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2009");
		Reservation r1 = new Reservation(1, deb1, fin1);
		Reservation r2 = new Reservation(2, deb1, fin1);
		Reservation r3 = new Reservation(3, null, null);
		assertTrue(r3.hashCode() > 0);
		
		int hash = r1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(r1.hashCode() == r2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == r1.hashCode());
		assertTrue(hash == r1.hashCode());
		assertTrue(hash == r1.hashCode());
		assertTrue(hash == r1.hashCode());
		
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		Reservation r1 = reservationDao.findById(1);
		assertTrue(r1.toString().
				equals("{" + r1.getDebut() + " - " + r1.getFin() + "}"));
		
		Reservation r2 = new Reservation();
		assertTrue(r2.toString().equals("{Empty Reservation}"));
	}
	
	/**
	 * Test la méthode clone.
	 */
	public void testClone() {
		Reservation r1 = reservationDao.findById(1);
		Reservation r2 = new Reservation();
		r2 = r1.clone();
		
		assertTrue(r1.equals(r2));
		
		if (r1.getReservationId() != null) {
			assertTrue(r1.getReservationId() == r2.getReservationId());
		} else {
			assertNull(r2.getReservationId());
		}
		
		if (r1.getDebut() != null) {
			assertTrue(r1.getDebut().equals(r2.getDebut()));
		} else {
			assertNull(r2.getDebut());
		}
		
		if (r1.getFin() != null) {
			assertTrue(r1.getFin()
					.equals(r2.getFin()));
		} else {
			assertNull(r2.getFin());
		}
		
		if (r1.getUtilisateur() != null) {
			assertTrue(r1.getUtilisateur()
					.equals(r2.getUtilisateur()));
		} else {
			assertNull(r2.getUtilisateur());
		}
		
		if (r1.getEchantillons() != null) {
			assertTrue(r1.getEchantillons()
					.equals(r2.getEchantillons()));
		} else {
			assertNull(r2.getEchantillons());
		}
		
		if (r1.getProdDerives() != null) {
			assertTrue(r1.getProdDerives()
					.equals(r2.getProdDerives()));
		} else {
			assertNull(r2.getProdDerives());
		}
		
	}

}
