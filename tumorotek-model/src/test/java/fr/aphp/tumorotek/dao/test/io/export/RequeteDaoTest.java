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
package fr.aphp.tumorotek.dao.test.io.export;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import fr.aphp.tumorotek.dao.test.Config;

import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.io.export.CombinaisonDao;
import fr.aphp.tumorotek.dao.io.export.CritereDao;
import fr.aphp.tumorotek.dao.io.export.GroupementDao;
import fr.aphp.tumorotek.dao.io.export.RequeteDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Combinaison;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Groupement;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le DAO RequeteDao et le bean du domaine Requete. Classe
 * de test créée le 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class RequeteDaoTest extends AbstractDaoTest {

	@Autowired
	RequeteDao requeteDao;

	@Autowired
	GroupementDao groupementDao;

	@Autowired
	UtilisateurDao utilisateurDao;

	@Autowired
	CritereDao critereDao;

	@Autowired
	ChampDao champDao;

	@Autowired
	CombinaisonDao combinaisonDao;

	@Autowired
	EntiteDao entiteDao;

	@Autowired
	ChampEntiteDao champEntiteDao;

	@Autowired
	DataTypeDao dataTypeDao;

	@Autowired
	BanqueDao banqueDao;

	@Test
	public void testFindByBanque() {
		final Banque b1 = banqueDao.findById(1).get();
		List<Requete> liste = requeteDao.findByBanque(b1);
		assertTrue(liste.size() == 3);

		final Banque b2 = banqueDao.findById(2).get();
		liste = requeteDao.findByBanque(b2);
		assertTrue(liste.size() == 1);

		final Banque b3 = banqueDao.findById(3).get();
		liste = requeteDao.findByBanque(b3);
		assertTrue(liste.size() == 0);

		liste = requeteDao.findByBanque(null);
		assertTrue(liste.size() == 0);
	}

	@Test
	public void testFindByBanqueInList() {
		final List<Banque> bks = new ArrayList<>();
		final Banque b1 = banqueDao.findById(1).get();
		bks.add(b1);
		List<Requete> liste = requeteDao.findByBanqueInList(bks);
		assertTrue(liste.size() == 3);

		final Banque b2 = banqueDao.findById(2).get();
		bks.add(b2);
		liste = requeteDao.findByBanqueInList(bks);
		assertTrue(liste.size() == 4);

		liste = requeteDao.findByBanqueInList(null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findRequetesByUtilisateurId().
	 */
	@Test
	public void testFindRequetesByUtilisateurId() throws Exception {
		final List<Utilisateur> utilisateurs = IterableUtils.toList(utilisateurDao.findAll());
		final Iterator<Utilisateur> itUtilisateur = utilisateurs.iterator();
		while (itUtilisateur.hasNext()) {
			final Utilisateur utilisateur = itUtilisateur.next();
			final List<Requete> requetes = requeteDao.findByUtilisateur(utilisateur);
			final Iterator<Requete> it = requetes.iterator();
			while (it.hasNext()) {
				assertTrue(it.next().getCreateur().equals(utilisateur));
			}
		}
	}

	/**
	 * Test l'appel de la méthode findByIntituleUtilisateur().
	 */
	@Test
	public void testFindByIntituleUtilisateur() throws Exception {
		final Utilisateur u1 = utilisateurDao.findById(1).get();
		final Utilisateur u2 = utilisateurDao.findById(2).get();
		List<Requete> liste = requeteDao.findByIntituleUtilisateur("Echantillon%", u1);
		assertTrue(liste.size() == 1);

		liste = requeteDao.findByIntituleUtilisateur("yug%", u1);
		assertTrue(liste.size() == 0);

		liste = requeteDao.findByIntituleUtilisateur("Essen%", u2);
		assertTrue(liste.size() == 0);

		liste = requeteDao.findByIntituleUtilisateur(null, u1);
		assertTrue(liste.size() == 0);

		liste = requeteDao.findByIntituleUtilisateur("Essen%", null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'une requete.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testCrudRequete() throws Exception {
		final Utilisateur createur = this.utilisateurDao.findById(3).get();
		DataType dataType = dataTypeDao.findById(3).get();
		ChampEntite chEntite = new ChampEntite(entiteDao.findById(5).get(), "champEntite1", dataType, true, true, null,
				false, null);
		this.champEntiteDao.save(chEntite);
		final int idChEn1 = chEntite.getId();
		final Champ ch1 = new Champ(chEntite);
		this.champDao.save(ch1);
		final int idCh1 = ch1.getChampId();
		dataType = dataTypeDao.findById(2).get();
		chEntite = new ChampEntite(entiteDao.findById(5).get(), "champEntite1", dataType, true, true, null, false,
				null);
		this.champEntiteDao.save(chEntite);
		final int idChEn2 = chEntite.getId();
		final Champ ch2 = new Champ(chEntite);
		this.champDao.save(ch2);
		final int idCh2 = ch2.getChampId();

		final Combinaison cb1 = new Combinaison(ch1, "+", ch2);
		this.combinaisonDao.save(cb1);
		final int idCb1 = cb1.getCombinaisonId();

		final Critere cr1 = new Critere(ch1, "<", "3");
		this.critereDao.save(cr1);
		final int idCr1 = cr1.getCritereId();
		final Critere cr2 = new Critere(cb1, "=", "val2");
		this.critereDao.save(cr2);
		final int idCr2 = cr2.getCritereId();
		final Critere cr3 = new Critere(ch1, "=", "test");
		this.critereDao.save(cr3);
		final int idCr3 = cr3.getCritereId();

		final Groupement g1 = new Groupement(null, cr3, "and", null);
		this.groupementDao.save(g1);
		final int idG1 = g1.getGroupementId();
		final Groupement g2 = new Groupement(cr1, cr2, "or", g1);
		this.groupementDao.save(g2);
		final int idG2 = g2.getGroupementId();
		final Groupement g3 = new Groupement(cr1, cr2, "and", null);
		this.groupementDao.save(g3);
		final int idG3 = g3.getGroupementId();

		final Groupement groupementRacine = this.groupementDao.findById(idG1).get();
		final String intitule = "intitule";
		final Banque b1 = banqueDao.findById(1).get();

		final Requete r = new Requete();
		r.setCreateur(createur);
		r.setGroupementRacine(groupementRacine);
		r.setIntitule(intitule);
		r.setBanque(b1);

		Integer idObject = new Integer(-1);
		// Test de l'insertion
		this.requeteDao.save(r);
		final List<Requete> requetes = IterableUtils.toList(this.requeteDao.findAll());
		final Iterator<Requete> itRequete = requetes.iterator();
		boolean found = false;
		while (itRequete.hasNext()) {
			final Requete temp = itRequete.next();
			if (temp.equals(r)) {
				found = true;
				idObject = temp.getRequeteId();
				break;
			}
		}
		assertTrue(found);

		// Test de la mise à jour
		final Requete r2 = this.requeteDao.findById(idObject).get();
		assertNotNull(r2);
		assertNotNull(createur);
		assertTrue(r2.getCreateur().equals(createur));
		assertNotNull(groupementRacine);
		assertTrue(r2.getGroupementRacine().equals(groupementRacine));
		assertNotNull(intitule);
		assertTrue(r2.getIntitule().equals(intitule));
		assertNotNull(r2.getBanque());
		assertTrue(r2.getBanque().equals(b1));

		final Utilisateur updatedCreateur = this.utilisateurDao.findById(2).get();
		final Groupement updatedGroupementRacine = this.groupementDao.findById(idG2).get();
		final String updatedIntitule = "intitule2";
		final Banque updatedBanque = banqueDao.findById(2).get();

		r2.setCreateur(updatedCreateur);
		r2.setGroupementRacine(updatedGroupementRacine);
		r2.setIntitule(updatedIntitule);
		r2.setBanque(updatedBanque);

		this.requeteDao.save(r2);
		assertNotNull(updatedCreateur);
		assertTrue(this.requeteDao.findById(idObject).get().getCreateur().equals(updatedCreateur));
		assertNotNull(updatedGroupementRacine);
		assertTrue(this.requeteDao.findById(idObject).get().getGroupementRacine().equals(updatedGroupementRacine));
		assertNotNull(updatedIntitule);
		assertTrue(this.requeteDao.findById(idObject).get().getIntitule().equals(updatedIntitule));
		assertNotNull(this.requeteDao.findById(idObject).get().getBanque());
		assertTrue(this.requeteDao.findById(idObject).get().getBanque().equals(updatedBanque));
		// Test de la délétion
		this.requeteDao.deleteById(idObject);
		assertFalse(this.requeteDao.findById(idObject).isPresent());

		// On supprime les éléments créés
		this.groupementDao.deleteById(idG3);
		this.groupementDao.deleteById(idG2);
		this.groupementDao.deleteById(idG1);
		this.critereDao.deleteById(idCr3);
		this.critereDao.deleteById(idCr2);
		this.critereDao.deleteById(idCr1);
		if (this.combinaisonDao.existsById(idCb1)) {
			this.combinaisonDao.deleteById(idCb1);
		}
		if (this.champDao.existsById(idCh2)) {
			this.champDao.deleteById(idCh2);
		}
		if (this.champDao.existsById(idCh1)) {
			this.champDao.deleteById(idCh1);
		}
		this.champEntiteDao.deleteById(idChEn2);
		this.champEntiteDao.deleteById(idChEn1);
	}

	/**
	 * test toString().
	 */
	@Test
	public void testToString() {
		final Requete r1 = requeteDao.findById(1).get();
		assertTrue(r1.toString().equals("{" + r1.getIntitule() + "}"));

		final Requete r2 = new Requete();
		assertTrue(r2.toString().equals("{Empty Requete}"));
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		// On boucle sur les 4 possibilités
		for (int i = 0; i < Math.pow(2, 2); i++) {
			final Requete requete1 = new Requete();
			final Requete requete2 = new Requete();
			Utilisateur createur = null;
			if (i >= 2) {
				createur = utilisateurDao.findById(2).get();
			}
			requete1.setCreateur(createur);
			requete2.setCreateur(createur);
			String intitule = null;
			final int toTest = i % 2;
			if (toTest > 0) {
				intitule = "Intitule";
			}
			requete1.setIntitule(intitule);
			requete2.setIntitule(intitule);
			// On compare les 2 requêtes
			assertTrue(requete1.equals(requete2));
		}
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		// On boucle sur les 4 possibilités
		for (int i = 0; i < Math.pow(2, 2); i++) {
			final Requete requete = new Requete();
			int hash = 7;
			Utilisateur createur = null;
			int hashCreateur = 0;
			if (i >= 2) {
				createur = utilisateurDao.findById(3).get();
				hashCreateur = createur.hashCode();
			}
			final int toTest = i % 2;
			String intitule = null;
			int hashIntitule = 0;
			if (toTest > 0) {
				intitule = "Intitule";
				hashIntitule = intitule.hashCode();
			}
			hash = 31 * hash + hashCreateur;
			hash = 31 * hash + hashIntitule;
			requete.setCreateur(createur);
			requete.setIntitule(intitule);
			// On vérifie que le hashCode est bon
			assertTrue(requete.hashCode() == hash);
			assertTrue(requete.hashCode() == hash);
			assertTrue(requete.hashCode() == hash);
		}
	}
}
