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

import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.export.AffichageDao;
import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.io.export.ResultatDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ResultatDaoTest extends AbstractDaoTest {

	@Autowired
	ResultatDao resultatDao;

	@Autowired
	ChampDao champDao;

	@Autowired
	AffichageDao affichageDao;

	@Autowired
	EntiteDao entiteDao;

	@Autowired
	ChampEntiteDao champEntiteDao;

	@Autowired
	UtilisateurDao utilisateurDao;

	@Autowired
	DataTypeDao dataTypeDao;

	@Autowired
	BanqueDao banqueDao;

	/**
	 * Test l'appel de la méthode findByAffichage().
	 */
	@Test
	@Transactional
	public void testFindByAffichage() throws Exception {
		final List<Affichage> affichages = IterableUtils.toList(this.affichageDao.findAll());
		final Iterator<Affichage> itAff = affichages.iterator();
		while (itAff.hasNext()) {
			final Affichage affichage = itAff.next();
			final List<Resultat> resultats = this.resultatDao.findByAffichage(affichage);
			assertTrue(affichage.getResultats().size() == resultats.size());
			final Iterator<Resultat> it = resultats.iterator();
			while (it.hasNext()) {
				final Resultat resultat = it.next();
				boolean found = false;
				final Iterator<Resultat> it2 = affichage.getResultats().iterator();
				while (it2.hasNext()) {
					if (resultat.equals(it2.next())) {
						found = true;
						break;
					}
				}
				assertTrue(found);
			}
		}
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un resultat.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testCrudResultat() throws Exception {
		final String format = "format";
		final String nomColonne = "nomColonne";
		final Integer position = new Integer(3);
		final boolean tri = false;
		final Integer ordreTri = new Integer(3);

		DataType dataType = dataTypeDao.findById(3).get();

		ChampEntite chEntite = new ChampEntite(entiteDao.findById(2).get(), "champEntite1", dataType, false, true,
				"000-0", false, null);
		champEntiteDao.save(chEntite);
		final int idChEn1 = chEntite.getId();
		Champ ch = new Champ(chEntite);
		champDao.save(ch);
		final int idCh1 = ch.getChampId();
		dataType = dataTypeDao.findById(2).get();
		chEntite = new ChampEntite(entiteDao.findById(1).get(), "champEntite2", dataType, false, false, null, false,
				null);
		champEntiteDao.save(chEntite);
		final int idChEn2 = chEntite.getId();
		ch = new Champ(chEntite);
		champDao.save(ch);
		final int idCh2 = ch.getChampId();

		final Champ champ = champDao.findById(idCh1).get();

		final Utilisateur ut = new Utilisateur();
		ut.setLogin("login");
		ut.setPassword("pass");
		utilisateurDao.save(ut);
		final int idU = ut.getUtilisateurId();

		final Utilisateur utilisateur = utilisateurDao.findById(idU).get();

		Affichage a = new Affichage("affichage", utilisateur, 25);
		a.setBanque(banqueDao.findById(1).get());
		this.affichageDao.save(a);
		final int idA1 = a.getAffichageId();
		a = new Affichage("affichage2", utilisateur, 35);
		a.setBanque(banqueDao.findById(1).get());
		this.affichageDao.save(a);
		final int idA2 = a.getAffichageId();

		final Affichage affichage = this.affichageDao.findById(idA1).get();

		final Resultat r = new Resultat();
		r.setChamp(champ);
		r.setFormat(format);
		r.setNomColonne(nomColonne);
		r.setPosition(position);
		r.setTri(tri);
		r.setOrdreTri(ordreTri);
		r.setAffichage(affichage);

		// Test de l'insertion
		Integer idObject = new Integer(-1);
		resultatDao.save(r);
		final List<Resultat> resultats = IterableUtils.toList(resultatDao.findAll());
		final Iterator<Resultat> itResultat = resultats.iterator();
		boolean found = false;
		while (itResultat.hasNext()) {
			final Resultat temp = itResultat.next();
			if (temp.equals(r)) {
				found = true;
				idObject = temp.getResultatId();
				break;
			}
		}
		assertTrue(found);

		// Test de la mise à jour
		final Resultat r2 = resultatDao.findById(idObject).get();
		assertNotNull(r2);
		assertTrue(r2.getChamp().equals(champ));
		if (r2.getFormat() != null) {
			assertTrue(r2.getFormat().equals(format));
		} else {
			assertNull(format);
		}
		assertNotNull(r2.getNomColonne());
		assertTrue(r2.getNomColonne().equals(nomColonne));
		assertNotNull(r2.getPosition());
		assertTrue(r2.getPosition().equals(position));
		assertNotNull(r2.getTri());
		assertTrue(r2.getTri().equals(tri));
		assertNotNull(r2.getOrdreTri());
		assertTrue(r2.getOrdreTri().equals(ordreTri));
		assertNotNull(r2.getAffichage());
		assertTrue(r2.getAffichage().equals(affichage));

		final Champ updatedChamp = champDao.findById(idCh2).get();
		final String updatedFormat = "format2";
		final String updatedNomColonne = "nomColonne2";
		final Integer updatedPosition = new Integer(2);
		final boolean updatedTri = true;
		final Integer updatedOrdreTri = new Integer(2);
		final Affichage updatedAffichage = this.affichageDao.findById(idA2).get();

		r2.setChamp(updatedChamp);
		r2.setFormat(updatedFormat);
		r2.setNomColonne(updatedNomColonne);
		r2.setPosition(updatedPosition);
		r2.setTri(updatedTri);
		r2.setOrdreTri(updatedOrdreTri);
		r2.setAffichage(updatedAffichage);

		resultatDao.save(r2);
		assertTrue(resultatDao.findById(idObject).get().getChamp().equals(updatedChamp));
		if (resultatDao.findById(idObject).get().getFormat() != null) {
			assertTrue(resultatDao.findById(idObject).get().getFormat().equals(updatedFormat));
		} else {
			assertNull(updatedFormat);
		}
		assertNotNull(resultatDao.findById(idObject).get().getNomColonne());
		assertTrue(resultatDao.findById(idObject).get().getNomColonne().equals(updatedNomColonne));
		assertNotNull(resultatDao.findById(idObject).get().getPosition());
		assertTrue(resultatDao.findById(idObject).get().getPosition().equals(updatedPosition));
		assertNotNull(resultatDao.findById(idObject).get().getTri());
		assertTrue(resultatDao.findById(idObject).get().getTri().equals(updatedTri));
		assertNotNull(resultatDao.findById(idObject).get().getOrdreTri());
		assertTrue(resultatDao.findById(idObject).get().getOrdreTri().equals(updatedOrdreTri));
		assertNotNull(resultatDao.findById(idObject).get().getAffichage());
		assertTrue(resultatDao.findById(idObject).get().getAffichage().equals(updatedAffichage));
		// Test de la délétion
		resultatDao.deleteById(idObject);
		assertFalse(resultatDao.findById(idObject).isPresent());

		// On supprime les éléments créés
		this.affichageDao.deleteById(idA1);
		this.affichageDao.deleteById(idA2);
		this.champDao.deleteById(idCh1);
		this.champDao.deleteById(idCh2);
		this.champEntiteDao.deleteById(idChEn1);
		this.champEntiteDao.deleteById(idChEn2);
		this.utilisateurDao.deleteById(idU);
	}

	/**
	 * test toString().
	 */
	@Test
	public void testToString() {
		final Resultat r1 = resultatDao.findById(1).get();
		assertTrue(r1.toString().equals("{" + r1.getResultatId() + "}"));

		final Resultat r2 = new Resultat();
		assertTrue(r2.toString().equals("{Empty Resultat}"));
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		// On boucle sur les 4 possibilités
		for (int i = 0; i < Math.pow(2, 2); i++) {
			final Resultat resultat1 = new Resultat();
			final Resultat resultat2 = new Resultat();
			String nomColonne = null;
			if (i >= 2) {
				nomColonne = "Nom de la colonne";
			}
			resultat1.setNomColonne(nomColonne);
			resultat2.setNomColonne(nomColonne);
			final int toTest = i % 2;
			Affichage affichage = null;
			if (toTest > 0) {
				affichage = affichageDao.findById(3).get();
			}
			resultat1.setAffichage(affichage);
			resultat2.setAffichage(affichage);
			// On compare les 2 résultats
			assertTrue(resultat1.equals(resultat2));
		}
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		// On boucle sur les 8 possibilités
		for (int i = 0; i < Math.pow(2, 2); i++) {
			final Resultat resultat = new Resultat();
			int hash = 7;
			String nomColonne = null;
			int hashNomColonne = 0;
			if (i >= 2) {
				nomColonne = "nomCol";
				hashNomColonne = nomColonne.hashCode();
			}
			final int toTest = i % 2;
			Affichage affichage = null;
			int hashAffichage = 0;
			if (toTest > 0) {
				affichage = affichageDao.findById(3).get();
				hashAffichage = affichage.hashCode();
			}
			hash = 31 * hash + hashNomColonne;
			hash = 31 * hash + hashAffichage;
			resultat.setNomColonne(nomColonne);
			resultat.setAffichage(affichage);
			// On vérifie que le hashCode est bon
			assertTrue(resultat.hashCode() == hash);
			assertTrue(resultat.hashCode() == hash);
			assertTrue(resultat.hashCode() == hash);
		}
	}
}
