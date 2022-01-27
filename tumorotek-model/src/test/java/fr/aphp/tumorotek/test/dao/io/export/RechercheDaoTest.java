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
package fr.aphp.tumorotek.test.dao.io.export;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.export.AffichageDao;
import fr.aphp.tumorotek.dao.io.export.RechercheDao;
import fr.aphp.tumorotek.dao.io.export.RequeteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO RechercheDao et le bean du domaine Recherche.
 * Classe de test créée le 13/04/10.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class RechercheDaoTest extends AbstractDaoTest {

	@Autowired
	RechercheDao rechercheDao;

	@Autowired
	UtilisateurDao utilisateurDao;

	@Autowired
	RequeteDao requeteDao;

	@Autowired
	AffichageDao affichageDao;

	@Autowired
	BanqueDao banqueDao;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@Test
	public void testFindByBanqueId() {
		List<Recherche> liste = rechercheDao.findByBanqueId(1);
		assertTrue(liste.size() == 3);

		liste = rechercheDao.findByBanqueId(3);
		assertTrue(liste.size() == 1);

		liste = rechercheDao.findByBanqueId(null);
		assertTrue(liste.size() == 0);
	}

	@Test
	public void testFindByBanqueInList() {
		final List<Integer> bks = new ArrayList<>();
		bks.add(1);
		List<Recherche> liste = rechercheDao.findByBanqueIdinList(bks);
		assertTrue(liste.size() == 3);

		bks.add(2);
		liste = rechercheDao.findByBanqueIdinList(bks);
		assertTrue(liste.size() == 4);

		liste = rechercheDao.findByBanqueIdinList(null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findRecherchesByUtilisateur().
	 */
	@Test
	public void testFindRecherchesByUtilisateur() throws Exception {
		final List<Utilisateur> utilisateurs = IterableUtils.toList(utilisateurDao.findAll());
		final Iterator<Utilisateur> itUtil = utilisateurs.iterator();
		while (itUtil.hasNext()) {
			final Utilisateur utilisateur = itUtil.next();
			final List<Recherche> recherches = this.rechercheDao.findByUtilisateur(utilisateur);
			final Iterator<Recherche> it = recherches.iterator();
			while (it.hasNext()) {
				assertTrue(it.next().getCreateur().equals(utilisateur));
			}
		}
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		final List<Recherche> liste = IterableUtils.toList(rechercheDao.findAll());
		final Iterator<Recherche> it = liste.iterator();
		while (it.hasNext()) {
			final Recherche temp = it.next();
			final List<Recherche> recherches = rechercheDao.findByExcludedId(temp.getRechercheId());
			assertTrue(recherches.size() == liste.size() - 1);
			assertFalse(recherches.contains(temp));
		}

	}

	@Test
	public void testFindByRequete() {
		final Requete r1 = requeteDao.findById(1).get();
		List<Recherche> liste = rechercheDao.findByRequete(r1);
		assertTrue(liste.size() == 0);

		final Requete r3 = requeteDao.findById(3).get();
		liste = rechercheDao.findByRequete(r3);
		assertTrue(liste.size() == 2);

		liste = rechercheDao.findByRequete(null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByIntituleUtilisateur().
	 */
	@Test
	public void testFindByIntituleUtilisateur() throws Exception {
		final Utilisateur u1 = utilisateurDao.findById(1).get();
		final Utilisateur u2 = utilisateurDao.findById(2).get();
		List<Recherche> liste = rechercheDao.findByIntituleUtilisateur("Aff%", u1);
		assertTrue(liste.size() == 1);

		liste = rechercheDao.findByIntituleUtilisateur("yug%", u1);
		assertTrue(liste.size() == 0);

		liste = rechercheDao.findByIntituleUtilisateur("Essen%", u2);
		assertTrue(liste.size() == 0);

		liste = rechercheDao.findByIntituleUtilisateur(null, u1);
		assertTrue(liste.size() == 0);

		liste = rechercheDao.findByIntituleUtilisateur("Essen%", null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un recherche.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testCrudRecherche() throws Exception {
		final int idCreateur = 3;
		final int idRequete = 3;
		final int idAffichage = 2;

		final Utilisateur createur = this.utilisateurDao.findById(idCreateur).get();
		final Requete requete = this.requeteDao.findById(idRequete).get();
		final Affichage affichage = this.affichageDao.findById(idAffichage).get();
		final String intitule = "intitule";

		final ArrayList<Banque> banques = new ArrayList<Banque>();
		banques.add(this.banqueDao.findById(2).get());
		banques.add(this.banqueDao.findById(3).get());
		banques.add(this.banqueDao.findById(1).get());

		final Recherche r = new Recherche();
		r.setIntitule(intitule);
		r.setCreateur(createur);
		r.setAffichage(affichage);
		r.setRequete(requete);
		r.setBanques(banques);

		// Test de l'insertion
		Integer idObject = new Integer(-1);
		this.rechercheDao.save(r);
		final List<Recherche> recherches = IterableUtils.toList(rechercheDao.findAll());
		final Iterator<Recherche> itRecherche = recherches.iterator();
		boolean found = false;
		while (itRecherche.hasNext()) {
			final Recherche temp = itRecherche.next();
			if (temp.equals(r)) {
				found = true;
				idObject = temp.getRechercheId();
				break;
			}
		}
		assertTrue(found);

		final Recherche r2 = this.rechercheDao.findById(r.getRechercheId()).get();
		assertNotNull(r2);
		assertNotNull(this.rechercheDao.findById(idObject).get().getIntitule());
		assertTrue(r2.getIntitule().equals("intitule"));
		assertNotNull(this.rechercheDao.findById(idObject).get().getCreateur());
		assertTrue(r2.getCreateur().equals(createur));
		assertNotNull(this.rechercheDao.findById(idObject).get().getAffichage());
		assertTrue(r2.getAffichage().equals(affichage));
		assertNotNull(this.rechercheDao.findById(idObject).get().getRequete());
		assertTrue(r2.getRequete().equals(requete));
		assertNotNull(this.rechercheDao.findById(idObject).get().getBanques());
		final Iterator<Banque> it = rechercheDao.findById(idObject).get().getBanques().iterator();
		while (it.hasNext()) {
			assertTrue(r2.getBanques().contains(it.next()));
		}

		// Test de la mise à jour
		final int idCreateur2 = 2;
		final int idRequete2 = 2;
		final int idAffichage2 = 1;

		final Utilisateur updatedCreateur = this.utilisateurDao.findById(idCreateur2).get();
		final String updatedIntitule = "intitule2";
		final Requete updatedRequete = this.requeteDao.findById(idRequete2).get();
		final Affichage updatedAffichage = this.affichageDao.findById(idAffichage2).get();

		final ArrayList<Banque> updatedBanques = new ArrayList<>();
		updatedBanques.add(this.banqueDao.findById(2).get());
		updatedBanques.add(this.banqueDao.findById(1).get());

		r2.setIntitule(updatedIntitule);
		r2.setCreateur(updatedCreateur);
		r2.setAffichage(updatedAffichage);
		r2.setBanques(updatedBanques);
		r2.setRequete(updatedRequete);

		this.rechercheDao.save(r2);
		assertNotNull(this.rechercheDao.findById(idObject).get().getIntitule());
		assertTrue(this.rechercheDao.findById(idObject).get().getIntitule().equals(updatedIntitule));
		assertNotNull(this.rechercheDao.findById(idObject).get().getCreateur());
		assertTrue(this.rechercheDao.findById(idObject).get().getCreateur().equals(updatedCreateur));
		assertNotNull(this.rechercheDao.findById(idObject).get().getAffichage());
		assertTrue(this.rechercheDao.findById(idObject).get().getAffichage().equals(updatedAffichage));
		assertNotNull(this.rechercheDao.findById(idObject).get().getRequete());
		assertTrue(this.rechercheDao.findById(idObject).get().getRequete().equals(updatedRequete));
		assertNotNull(this.rechercheDao.findById(idObject).get().getBanques());
		assertTrue(this.rechercheDao.findById(idObject).get().getBanques().equals(updatedBanques));
		// Test de la délétion
		this.rechercheDao.deleteById(idObject);
		assertFalse(this.rechercheDao.findById(idObject).isPresent());

	}

	/**
	 * test toString().
	 */
	@Test
	public void testToString() {
		final Recherche r1 = rechercheDao.findById(1).get();
		assertTrue(r1.toString().equals(r1.getIntitule()));

		final Recherche r2 = new Recherche();
		assertTrue(r2.toString().equals("{Empty Recherche}"));
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		// On boucle sur les 32 possibilités
		for (int i = 0; i < Math.pow(2, 4); i++) {
			final Recherche recherche1 = new Recherche();
			final Recherche recherche2 = new Recherche();
			int toTest = i % 8;
			Affichage affichage = null;
			if (toTest >= 8) {
				affichage = affichageDao.findById(2).get();
			}
			recherche1.setAffichage(affichage);
			recherche2.setAffichage(affichage);
			toTest = i % 4;
			Requete requete = null;
			if (toTest >= 4) {
				requete = requeteDao.findById(2).get();
			}
			recherche1.setRequete(requete);
			recherche2.setRequete(requete);
			toTest = i % 2;
			Utilisateur createur = null;
			if (toTest >= 2) {
				createur = utilisateurDao.findById(2).get();
			}
			recherche1.setCreateur(createur);
			recherche2.setCreateur(createur);
			String intitule = null;

			if (toTest > 0) {
				intitule = "Intitule";
			}
			recherche1.setIntitule(intitule);
			recherche2.setIntitule(intitule);
			// On compare les 2 recherches
			assertTrue(recherche1.equals(recherche2));
		}
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		// On boucle sur les 32 possibilités
		for (int i = 0; i < Math.pow(2, 4); i++) {
			final Recherche recherche = new Recherche();
			int hash = 7;
			int toTest = i % 8;
			Affichage affichage = null;
			int hashAffichage = 0;
			if (toTest >= 8) {
				affichage = affichageDao.findById(2).get();
				hashAffichage = affichage.hashCode();
			}
			toTest = i % 4;
			Requete requete = null;
			int hashRequete = 0;
			if (toTest >= 4) {
				requete = requeteDao.findById(2).get();
				hashRequete = requete.hashCode();
			}
			Utilisateur createur = null;
			int hashCreateur = 0;
			if (i >= 2) {
				createur = utilisateurDao.findById(3).get();
				hashCreateur = createur.hashCode();
			}
			toTest = i % 2;
			String intitule = null;
			int hashIntitule = 0;
			if (toTest > 0) {
				intitule = "Intitule";
				hashIntitule = intitule.hashCode();
			}
			hash = 31 * hash + hashIntitule;
			hash = 31 * hash + hashCreateur;
			hash = 31 * hash + hashAffichage;
			hash = 31 * hash + hashRequete;

			recherche.setAffichage(affichage);
			recherche.setRequete(requete);
			recherche.setCreateur(createur);
			recherche.setIntitule(intitule);
			// On vérifie que le hashCode est bon
			assertTrue(recherche.hashCode() == hash);
			assertTrue(recherche.hashCode() == hash);
			assertTrue(recherche.hashCode() == hash);
		}
	}
}
