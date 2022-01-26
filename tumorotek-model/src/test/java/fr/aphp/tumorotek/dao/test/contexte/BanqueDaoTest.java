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

import javax.transaction.Transactional;

import org.springframework.test.annotation.Rollback;
import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import fr.aphp.tumorotek.dao.test.Config;

import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ContexteDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Contexte;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le DAO BanqueDao et le bean du domaine Banque.
 *
 * @author Pierre Ventadour.
 * @version 2.2.1
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class BanqueDaoTest extends AbstractDaoTest {

	@Autowired
	BanqueDao banqueDao;

	@Autowired
	CollaborateurDao collaborateurDao;

	@Autowired
	ServiceDao serviceDao;

	@Autowired
	PlateformeDao plateformeDao;

	@Autowired
	ContexteDao contexteDao;

	@Autowired
	CouleurDao couleurDao;

	@Autowired
	UtilisateurDao utilisateurDao;

	@Autowired
	EntiteDao entiteDao;

	@Autowired
	TableAnnotationDao tableAnnotationDao;

	@Autowired
	ConteneurDao conteneurDao;

	private final String updatedNom = "Banque mise a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllBanques() {
		final List<Banque> banques = IterableUtils.toList(banqueDao.findAll());
		assertTrue(banques.size() == 4);
	}

	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	@Test
	public void testFindByOrder() {
		final List<Banque> list = banqueDao.findByOrder();
		assertTrue(list.size() == 4);
		assertTrue(list.get(0).getNom().equals("BANQUE1"));
	}

	/**
	 * Test l'appel de la méthode findByNom().
	 */
	@Test
	public void testFindByNom() {
		List<Banque> banques = banqueDao.findByNom("BANQUE1");
		assertTrue(banques.size() == 1);
		banques = banqueDao.findByNom("BICHAT");
		assertTrue(banques.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByIdentification().
	 */
	@Test
	public void testFindByIdentification() {
		List<Banque> banques = banqueDao.findByIdentification("B1");
		assertTrue(banques.size() == 1);
		banques = banqueDao.findByIdentification("548969125");
		assertTrue(banques.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByAutoriseCrossPatient().
	 */
	@Test
	public void testFindByAutoriseCrossPatient() {
		final List<Banque> banques = banqueDao.findByAutoriseCrossPatient(true);
		assertTrue(banques.size() == 2);
	}

	/**
	 * Test l'appel de la méthode findByArchive().
	 */
	@Test
	public void testFindByArchive() {
		final List<Banque> banques = banqueDao.findByArchive(false);
		assertTrue(banques.size() == 3);
	}

	/**
	 * Test l'appel de la méthode findByCollaborateur().
	 */
	@Test
	public void testFindByCollaborateur() {
		Collaborateur c = collaborateurDao.findById(1).get();
		List<Banque> banques = banqueDao.findByCollaborateur(c);
		assertTrue(banques.size() == 1);
		c = collaborateurDao.findById(3).get();
		banques = banqueDao.findByCollaborateur(c);
		assertTrue(banques.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByProprietaire().
	 */
	@Test
	public void testFindByProprietaire() {
		Service s = serviceDao.findById(1).get();
		List<Banque> banques = banqueDao.findByProprietaire(s);
		assertTrue(banques.size() == 2);
		s = serviceDao.findById(4).get();
		banques = banqueDao.findByProprietaire(s);
		assertTrue(banques.size() == 1);
		assertFalse(serviceDao.findById(5).isPresent());
		banques = banqueDao.findByProprietaire(null);
		assertTrue(banques.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByPlateformeAndArchive().
	 * 
	 * @version 2.1
	 */
	@Test
	public void testFindByPlateforme() {
		Plateforme pf = plateformeDao.findById(1).get();
		List<Banque> banques = banqueDao.findByPlateformeAndArchive(pf, false);
		assertTrue(banques.size() == 3);
		assertTrue(banques.get(0).getBanqueId() == 1);
		assertTrue(banques.get(1).getBanqueId() == 2);
		assertTrue(banques.get(1).getBanqueId() == 2);

		banques = banqueDao.findByPlateformeAndArchive(pf, true);
		assertTrue(banques.size() == 0);
		pf = plateformeDao.findById(2).get();
		banques = banqueDao.findByPlateformeAndArchive(pf, false);
		assertTrue(banques.size() == 0);
		banques = banqueDao.findByPlateformeAndArchive(pf, true);
		assertTrue(banques.size() == 1);
		banques = banqueDao.findByPlateformeAndArchive(null, false);
		assertTrue(banques.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByIdWithFetch().
	 */
	@Test
	public void testFindByIdWithFetch() {
		final List<Banque> banques = banqueDao.findByIdWithFetch(3);
		final Banque banque = banques.get(0);
		assertNotNull(banque);
		assertTrue(banque.getCollaborateur().getCollaborateurId() == 4);
		assertTrue(banque.getProprietaire().getServiceId() == 2);
		assertTrue(banque.getPlateforme().getPlateformeId() == 1);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'une banque.
	 * 
	 * @throws Exception lance une exception en cas d'erreur sur les données.
	 */
	@Rollback(false)
	@Test
	public void testCrudBanque() throws Exception {
		final Banque b = new Banque();
		final Collaborateur c = collaborateurDao.findById(1).get();
		final Service s2 = serviceDao.findById(2).get();
		final Contexte co = contexteDao.findById(1).get();
		final Plateforme p = plateformeDao.findById(1).get();
		final Couleur coul1 = couleurDao.findById(1).get();
		final Couleur coul2 = couleurDao.findById(2).get();

		b.setNom("Banque5");
		b.setIdentification("B5");
		b.setDescription("banque n5");
		b.setAutoriseCrossPatient(true);
		b.setArchive(false);

		b.setDefautMaladie("glioblastome");
		b.setDefautMaladieCode("C71.0");
		b.setCollaborateur(c);
		b.setContact(c);
		b.setContexte(co);
		b.setProprietaire(s2);
		b.setPlateforme(p);
		b.setEchantillonCouleur(coul1);
		b.setProdDeriveCouleur(coul2);
		// Test de l'insertion
		banqueDao.save(b);
		assertEquals(new Integer(5), b.getBanqueId());

		// Test de la mise à jour
		final Banque b2 = banqueDao.findById(new Integer(5)).get();
		// Vérification des données entrées dans la base
		assertNotNull(b2);
		assertTrue(b2.getNom().equals("Banque5"));
		assertTrue(b2.getIdentification().equals("B5"));
		assertTrue(b2.getDescription().equals("banque n5"));
		assertTrue(b2.getAutoriseCrossPatient());
		assertFalse(b2.getArchive());
		assertTrue(b2.getDefMaladies());
		assertNotNull(b2.getDefautMaladie());
		assertNotNull(b2.getDefautMaladieCode());
		assertNotNull(b2.getCollaborateur());
		assertNotNull(b2.getContact());
		assertNotNull(b2.getProprietaire());
		assertTrue(b2.getProprietaire().getServiceId() == 2);
		assertNotNull(b2.getPlateforme());
		assertNotNull(b2.getContexte());
		assertNotNull(b2.getEchantillonCouleur());
		assertNotNull(b2.getProdDeriveCouleur());
		b2.setNom(updatedNom);
		b2.setArchive(true);
		banqueDao.save(b2);
		assertTrue(banqueDao.findById(b2.getBanqueId()).get().getNom().equals(updatedNom));
		assertTrue(banqueDao.findById(b2.getBanqueId()).get().getArchive());

		// Test de la délétion
		banqueDao.deleteById(new Integer(5));
		assertFalse(banqueDao.findById(new Integer(5)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String nom = "Banque1";
		final String nom2 = "Banque2";
		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		final Banque b1 = new Banque();
		final Banque b2 = new Banque();

		// L'objet 1 n'est pas égal à null
		assertFalse(b1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(b1.equals(b1));

		/* null */
		assertTrue(b1.equals(b2));
		assertTrue(b2.equals(b1));

		/* Nom */
		b2.setNom(nom);
		assertFalse(b1.equals(b2));
		assertFalse(b2.equals(b1));
		b1.setNom(nom2);
		assertFalse(b1.equals(b2));
		assertFalse(b2.equals(b1));
		b1.setNom(nom);
		assertTrue(b1.equals(b2));
		assertTrue(b2.equals(b1));

		/* Plateforme (nom etant egaux) */
		b2.setPlateforme(pf1);
		assertFalse(b1.equals(b2));
		assertFalse(b2.equals(b1));
		b1.setPlateforme(pf2);
		assertFalse(b1.equals(b2));
		assertFalse(b2.equals(b1));
		b1.setPlateforme(pf1);
		assertTrue(b1.equals(b2));
		assertTrue(b2.equals(b1));

		final Categorie c = new Categorie();
		assertFalse(b1.equals(c));

	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String nom = "Banque1";
		final String nom2 = "Banque2";
		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		final Banque b1 = new Banque();
		final Banque b2 = new Banque();

		assertTrue(b1.hashCode() > 0);

		/* null */
		assertTrue(b1.hashCode() == b2.hashCode());

		/* Nom */
		b2.setNom(nom);
		assertFalse(b1.hashCode() == b2.hashCode());
		b1.setNom(nom2);
		assertFalse(b1.hashCode() == b2.hashCode());
		b1.setNom(nom);
		assertTrue(b1.hashCode() == b2.hashCode());

		/* Plateforme (nom etant egaux) */
		b2.setPlateforme(pf1);
		assertFalse(b1.hashCode() == b2.hashCode());
		b1.setPlateforme(pf2);
		assertFalse(b1.hashCode() == b2.hashCode());
		b1.setPlateforme(pf1);
		assertTrue(b1.hashCode() == b2.hashCode());

		// un même objet garde le même hashcode dans le temps
		final int hash = b1.hashCode();
		assertTrue(hash == b1.hashCode());
		assertTrue(hash == b1.hashCode());
		assertTrue(hash == b1.hashCode());
		assertTrue(hash == b1.hashCode());

	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testClone() {
		Banque c1 = banqueDao.findById(3).get();
		Banque c2 = c1.clone();
		assertTrue(c1.equals(c2));

		if (c1.getBanqueId() != null) {
			assertTrue(c1.getBanqueId() == c2.getBanqueId());
		} else {
			assertNull(c2.getBanqueId());
		}

		if (c1.getCollaborateur() != null) {
			assertTrue(c1.getCollaborateur().equals(c2.getCollaborateur()));
		} else {
			assertNull(c2.getCollaborateur());
		}

		if (c1.getContact() != null) {
			assertTrue(c1.getContact().equals(c2.getContact()));
		} else {
			assertNull(c2.getContact());
		}

		if (c1.getNom() != null) {
			assertTrue(c1.getNom().equals(c2.getNom()));
		} else {
			assertNull(c2.getNom());
		}

		if (c1.getIdentification() != null) {
			assertTrue(c1.getIdentification().equals(c2.getIdentification()));
		} else {
			assertNull(c2.getIdentification());
		}

		if (c1.getDescription() != null) {
			assertTrue(c1.getDescription().equals(c2.getDescription()));
		}

		if (c1.getProprietaire() != null) {
			assertTrue(c1.getProprietaire().equals(c2.getProprietaire()));
		} else {
			assertNull(c2.getProprietaire());
		}

		assertTrue(c1.getAutoriseCrossPatient() == c2.getAutoriseCrossPatient());
		assertTrue(c1.getArchive() == c2.getArchive());
		assertTrue(c1.getDefMaladies() == c2.getDefMaladies());

		if (c1.getContexte() != null) {
			assertTrue(c1.getContexte().equals(c2.getContexte()));
		}

		if (c1.getPlateforme() != null) {
			assertTrue(c1.getPlateforme().equals(c2.getPlateforme()));
		} else {
			assertNull(c2.getPlateforme());
		}

		assertTrue(c1.getEchantillonCouleur().equals(c2.getEchantillonCouleur()));
		assertTrue(c1.getProdDeriveCouleur().equals(c2.getProdDeriveCouleur()));

		if (c1.getAffectationImprimantes() != null) {
			assertTrue(c1.getAffectationImprimantes().equals(c2.getAffectationImprimantes()));
		} else {
			assertNull(c2.getAffectationImprimantes());
		}

		if (c1.getAnnotationDefauts() != null) {
			assertTrue(c1.getAnnotationDefauts().equals(c2.getAnnotationDefauts()));
		} else {
			assertNull(c2.getAnnotationDefauts());
		}

		if (c1.getNumerotations() != null) {
			assertTrue(c1.getNumerotations().equals(c2.getNumerotations()));
		} else {
			assertNull(c2.getNumerotations());
		}

		if (c1.getTemplates() != null) {
			assertTrue(c1.getTemplates().equals(c2.getTemplates()));
		} else {
			assertNull(c2.getTemplates());
		}
		if (c1.getCatalogues() != null) {
			assertTrue(c1.getCatalogues().equals(c2.getCatalogues()));
		} else {
			assertNull(c2.getCatalogues());
		}

		c1 = banqueDao.findById(1).get();
		c2 = c1.clone();
		assertTrue(c2.getDefautMaladie().equals(c1.getDefautMaladie()));
		assertTrue(c2.getDefautMaladieCode().equals(c1.getDefautMaladieCode()));
	}

	@Test
	public void testFindContexteCatalogues() {
		final List<Catalogue> cats = banqueDao.findContexteCatalogues(1);
		assertTrue(cats.size() == 4);
		assertTrue(cats.get(0).getNom().equals("INCa"));
	}

	@Test
	public void testFindByEntiteConsultByUtilisateur() {
		Utilisateur utilisateur = utilisateurDao.findById(2).get();
		final Plateforme pf = plateformeDao.findById(1).get();
		final Entite prel = entiteDao.findById(2).get();
		List<Banque> banks = banqueDao.findByEntiteConsultByUtilisateur(utilisateur, prel, pf);
		assertTrue(banks.size() == 2);
		assertTrue(banks.contains(banqueDao.findById(1).get()));
		assertTrue(banks.contains(banqueDao.findById(2).get()));

		utilisateur = utilisateurDao.findById(1).get();
		banks = banqueDao.findByEntiteConsultByUtilisateur(utilisateur, prel, pf);
		assertTrue(banks.size() == 0);

		// autorise cross patient
		banks = banqueDao.findByEntiteConsultByUtilisateur(utilisateurDao.findById(4).get(), prel, pf);
		assertTrue(banks.size() == 0);
	}

	@Test
	public void testFindByEntiteModifByUtilisateur() {
		Utilisateur utilisateur = utilisateurDao.findById(2).get();
		final Plateforme pf = plateformeDao.findById(1).get();
		final Entite prel = entiteDao.findById(2).get();
		List<Banque> banks = banqueDao.findByEntiteModifByUtilisateur(utilisateur, prel, pf);
		assertTrue(banks.size() == 2);
		assertTrue(banks.contains(banqueDao.findById(1).get()));
		assertTrue(banks.contains(banqueDao.findById(2).get()));

		utilisateur = utilisateurDao.findById(3).get();
		banks = banqueDao.findByEntiteModifByUtilisateur(utilisateur, prel, pf);
		assertTrue(banks.size() == 0);

		// autorise cross patient
		banks = banqueDao.findByEntiteModifByUtilisateur(utilisateurDao.findById(4).get(), prel, pf);
		assertTrue(banks.size() == 0);
	}

	@Test
	public void testFindByUtilisateurIsAdmin() {
		Utilisateur utilisateur = utilisateurDao.findById(1).get();
		Plateforme pf = plateformeDao.findById(1).get();
		List<Banque> banks = banqueDao.findByUtilisateurIsAdmin(utilisateur, pf);
		assertTrue(banks.size() == 2);
		assertTrue(banks.contains(banqueDao.findById(1).get()));
		assertTrue(banks.contains(banqueDao.findById(2).get()));
		banks = banqueDao.findByUtilisateurIsAdmin(utilisateurDao.findById(2).get(), pf);
		assertTrue(banks.size() == 0);

		pf = plateformeDao.findById(2).get();
		banks = banqueDao.findByUtilisateurIsAdmin(utilisateur, pf);
		assertTrue(banks.size() == 0);

		// super et admin PF
		utilisateur = utilisateurDao.findById(5).get();
		banks = banqueDao.findByUtilisateurIsAdmin(utilisateur, pf);
		assertTrue(banks.size() == 0);

		pf = plateformeDao.findById(1).get();
		banks = banqueDao.findByUtilisateurIsAdmin(utilisateur, pf);
		assertTrue(banks.size() == 3);
	}

	@Test
	public void testFindByUtilisateurAndPF() {
		Utilisateur utilisateur = utilisateurDao.findById(1).get();
		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		List<Banque> banks = banqueDao.findByUtilisateurAndPF(utilisateur, pf1);
		assertTrue(banks.size() == 2);
		assertTrue(banks.contains(banqueDao.findById(1).get()));
		assertTrue(banks.contains(banqueDao.findById(2).get()));
		banks = banqueDao.findByUtilisateurAndPF(utilisateur, pf2);
		assertTrue(banks.size() == 0);

		utilisateur = utilisateurDao.findById(2).get();
		banks = banqueDao.findByUtilisateurAndPF(utilisateur, pf1);
		assertTrue(banks.size() == 2);
		banks = banqueDao.findByUtilisateurAndPF(utilisateur, pf2);
		assertTrue(banks.size() == 0);

		utilisateur = utilisateurDao.findById(5).get();
		banks = banqueDao.findByUtilisateurAndPF(utilisateur, pf1);
		assertTrue(banks.size() == 3);
		banks = banqueDao.findByUtilisateurAndPF(utilisateur, pf2);
		assertTrue(banks.size() == 0);
	}

	@Test
	public void testFindByExcludedId() {
		List<Banque> banques = banqueDao.findByExcludedId(1);
		assertTrue(banques.size() == 3);

		banques = banqueDao.findByExcludedId(10);
		assertTrue(banques.size() == 4);
	}

	@Test
	public void testFindByProfilUtilisateur() {
		final Utilisateur u1 = utilisateurDao.findById(1).get();
		List<Banque> banks = banqueDao.findByProfilUtilisateur(u1);
		assertTrue(banks.size() == 3);
		assertTrue(banks.get(0).getNom().equals("BANQUE1"));
		assertTrue(banks.get(1).getNom().equals("BANQUE2"));
		assertTrue(banks.get(2).getNom().equals("BANQUE4"));
		banks.clear();
		final Utilisateur u3 = utilisateurDao.findById(3).get();
		banks = banqueDao.findByProfilUtilisateur(u3);
		assertTrue(banks.size() == 2);
		assertTrue(banks.get(0).getNom().equals("BANQUE1"));
		assertTrue(banks.get(1).getNom().equals("BANQUE2"));
		final Utilisateur u4 = utilisateurDao.findById(4).get();
		banks = banqueDao.findByProfilUtilisateur(u4);
		assertTrue(banks.size() == 0);
	}

	@Test
	public void testFindByTableAnnotation() {
		TableAnnotation t = tableAnnotationDao.findById(1).get();
		List<Banque> banks = banqueDao.findByTableAnnotation(t);
		assertTrue(banks.size() == 1);
		assertTrue(banks.get(0).equals(banqueDao.findById(1).get()));
		t = tableAnnotationDao.findById(3).get();
		banks = banqueDao.findByTableAnnotation(t);
		assertTrue(banks.size() == 3);
		banks = banqueDao.findByTableAnnotation(null);
		assertTrue(banks.size() == 0);
	}

	@Test
	public void testFindByConteneur() {

		final Conteneur c1 = conteneurDao.findById(1).get();
		List<Banque> banks = banqueDao.findByConteneur(c1);
		assertEquals(4, banks.size());

		final Conteneur c2 = conteneurDao.findById(2).get();
		banks = banqueDao.findByConteneur(c2);
		assertEquals(2, banks.size());

		banks = banqueDao.findByConteneur(null);
		assertEquals(0, banks.size());
	}
}
