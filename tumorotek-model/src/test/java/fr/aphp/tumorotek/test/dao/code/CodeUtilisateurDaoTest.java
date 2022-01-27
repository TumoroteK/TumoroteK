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
package fr.aphp.tumorotek.test.dao.code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import fr.aphp.tumorotek.dao.code.CodeDossierDao;
import fr.aphp.tumorotek.dao.code.CodeSelectDao;
import fr.aphp.tumorotek.dao.code.CodeUtilisateurDao;
import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.code.TranscodeUtilisateur;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO CodeUtilisateurDao et le bean du domaine
 * CodeUtilisateur. Classe de test créée le 24/09/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class CodeUtilisateurDaoTest extends AbstractDaoTest {

	@Autowired
	CodeUtilisateurDao codeUtilisateurDao;

	@Autowired
	UtilisateurDao utilisateurDao;

	@Autowired
	BanqueDao banqueDao;

	@Autowired
	CodeDossierDao codeDossierDao;

	@Autowired
	TableCodageDao tableCodageDao;

	@Autowired
	CodeSelectDao codeSelectDao;

	@Test
	public void testReadAllCodes() {
		final List<CodeUtilisateur> codes = IterableUtils.toList(codeUtilisateurDao.findAll());
		assertTrue(codes.size() == 6);
	}

	@Test
	public void testFindByCodeLike() {
		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();
		final List<Banque> banks = new ArrayList<>();
		banks.add(b1);
		List<CodeUtilisateur> codes = codeUtilisateurDao.findByCodeLike("code%", banks);
		assertTrue(codes.size() == 5);
		codes = codeUtilisateurDao.findByCodeLike("code4", banks);
		assertTrue(codes.size() == 0);
		banks.add(b2);
		codes = codeUtilisateurDao.findByCodeLike("code%", banks);
		assertTrue(codes.size() == 6);
	}

	@Test
	public void testFindByLibelleLike() {
		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();
		final List<Banque> banks = new ArrayList<>();
		banks.add(b1);
		List<CodeUtilisateur> codes = codeUtilisateurDao.findByLibelleLike("libelle1%", banks);
		assertTrue(codes.size() == 4);
		codes = codeUtilisateurDao.findByLibelleLike("%ding%", banks);
		assertTrue(codes.size() == 0);
		banks.add(b2);
		codes = codeUtilisateurDao.findByLibelleLike("libelle%", banks);
		assertTrue(codes.size() == 5);
	}

	@Test
	public void testFindByUtilisateurAndBanque() {
		Utilisateur u = utilisateurDao.findById(1).get();
		Banque b = banqueDao.findById(1).get();
		List<CodeUtilisateur> codes = codeUtilisateurDao.findByUtilisateurAndBanque(u, b);
		assertTrue(codes.size() == 5);
		u = utilisateurDao.findById(2).get();
		codes = codeUtilisateurDao.findByUtilisateurAndBanque(u, b);
		assertTrue(codes.size() == 0);
		b = banqueDao.findById(2).get();
		codes = codeUtilisateurDao.findByUtilisateurAndBanque(u, b);
		assertTrue(codes.size() == 1);
	}

	@Test
	public void testFindByCodeDossier() {
		CodeDossier dos = codeDossierDao.findById(2).get();
		List<CodeUtilisateur> codes = codeUtilisateurDao.findByCodeDossier(dos);
		assertTrue(codes.size() == 2);
		dos = codeDossierDao.findById(3).get();
		codes = codeUtilisateurDao.findByCodeDossier(dos);
		assertTrue(codes.size() == 0);
	}

	@Test
	public void testFindByRootDossier() {
		final Banque b = banqueDao.findById(1).get();
		final List<CodeUtilisateur> codes = codeUtilisateurDao.findByRootDossier(b);
		assertTrue(codes.size() == 1);
	}

	@Test
	public void testFindByCodeParent() {
		CodeUtilisateur c = codeUtilisateurDao.findById(4).get();
		List<CodeUtilisateur> codes = codeUtilisateurDao.findByCodeParent(c);
		assertTrue(codes.size() == 2);
		c = codeUtilisateurDao.findById(1).get();
		codes = codeUtilisateurDao.findByCodeParent(c);
		assertTrue(codes.size() == 0);
	}

	@Test
	public void findByExcludedId() {
		final CodeUtilisateur c = codeUtilisateurDao.findById(1).get();
		List<CodeUtilisateur> codes = codeUtilisateurDao.findByExcludedId(c.getCodeUtilisateurId());
		assertTrue(codes.size() == 5);
		codes = codeUtilisateurDao.findByExcludedId(8);
		assertTrue(codes.size() == 6);
	}

	@Test
	@Transactional
	public void testTranscode() {
		final CodeUtilisateur c1 = codeUtilisateurDao.findById(1).get();
		assertTrue(c1.getTranscodes().size() == 3);
	}

	@Test
	public void testFindByTranscodage() {
		final List<Banque> banks = new ArrayList<>();
		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();
		banks.add(b2);

		final TableCodage adicap = tableCodageDao.findByNom("ADICAP").get(0);
		List<CodeUtilisateur> codes = codeUtilisateurDao.findByTranscodage(adicap, 1, banks);
		assertTrue(codes.isEmpty());
		banks.add(b1);
		codes = codeUtilisateurDao.findByTranscodage(adicap, 1, banks);
		assertTrue(codes.size() == 1);
		assertTrue(codes.get(0).getCode().equals("code1"));

		final TableCodage cim = tableCodageDao.findById(2).get();
		codes = codeUtilisateurDao.findByTranscodage(cim, 1, banks);
		assertTrue(codes.size() == 1);
		assertTrue(codes.get(0).getCode().equals("code1"));

		codes = codeUtilisateurDao.findByTranscodage(cim, 12, banks);
		assertTrue(codes.size() == 1);
		assertTrue(codes.get(0).getCode().equals("code2"));

		codes = codeUtilisateurDao.findByTranscodage(adicap, 12, banks);
		assertTrue(codes.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un code select.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testCrud() throws Exception {
		final CodeUtilisateur c = new CodeUtilisateur();

		final Utilisateur u = utilisateurDao.findById(2).get();
		final Banque b = banqueDao.findById(1).get();
		final CodeDossier dos = codeDossierDao.findById(1).get();
		c.setUtilisateur(u);
		c.setBanque(b);
		c.setCode("crud");
		c.setCodeDossier(dos);

		// Transcodes
		final Set<TranscodeUtilisateur> codes = new HashSet<>();
		final TranscodeUtilisateur tr1 = new TranscodeUtilisateur();
		final TableCodage t1 = tableCodageDao.findById(4).get();
		tr1.setTableCodage(t1);
		tr1.setCodeId(1);
		tr1.setCodeUtilisateur(c);
		final TranscodeUtilisateur tr2 = new TranscodeUtilisateur();
		tr2.setTableCodage(t1);
		tr2.setCodeId(2);
		tr2.setCodeUtilisateur(c);
		codes.add(tr1);
		codes.add(tr2);
		c.setTranscodes(codes);

		// Test de l'insertion
		codeUtilisateurDao.save(c);
		assertEquals(new Integer(7), c.getCodeUtilisateurId());

		assertTrue(codeUtilisateurDao.findById(new Integer(7)).get().getTranscodes().size() == 2);

		// Test de la mise à jour
		final CodeUtilisateur c2 = codeUtilisateurDao.findById(new Integer(7)).get();
		assertNotNull(c2);
		assertTrue(c2.getCode() == "crud");
		assertNotNull(c2.getBanque());
		assertNotNull(c2.getUtilisateur());
		assertNotNull(c2.getCodeDossier());
		c2.setCode("update");

		// codes.clear();
		// TranscodeUtilisateur tr3 = new TranscodeUtilisateur();
		// tr3.setTableCodage(t1);
		// tr3.setCodeId(3);
		// tr3.setCodeUtilisateur(c2);
		// codes.add(tr3);
		// c2.setTranscodes(codes);

		codeUtilisateurDao.save(c2);
		assertTrue(codeUtilisateurDao.findById(new Integer(7)).get().getCode() == "update");
		// assertTrue(codeUtilisateurDao.
		// findById(new Integer(7)).getTranscodes().size() == 1).get();

		// Test de la délétion
		codeUtilisateurDao.deleteById(new Integer(7));
		assertFalse(codeUtilisateurDao.findById(new Integer(7)).isPresent());
		testReadAllCodes();
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String code1 = "code1";
		final String code2 = "code2";
		final Utilisateur u1 = utilisateurDao.findById(1).get();
		final Utilisateur u2 = utilisateurDao.findById(2).get();
		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();
		final CodeUtilisateur c1 = new CodeUtilisateur();
		final CodeUtilisateur tr2 = new CodeUtilisateur();

		// L'objet 1 n'est pas égal à null
		assertFalse(c1.equals(null));
		// L'objet 1 est égal à lui même
		assertTrue(c1.equals(c1));

		/* null */
		assertTrue(c1.equals(tr2));
		assertTrue(tr2.equals(c1));

		/* Code */
		tr2.setCode(code1);
		assertFalse(c1.equals(tr2));
		assertFalse(tr2.equals(c1));
		c1.setCode(code2);
		assertFalse(c1.equals(tr2));
		assertFalse(tr2.equals(c1));
		c1.setCode(code1);
		assertTrue(c1.equals(tr2));
		assertTrue(tr2.equals(c1));

		/* Utilisateur (code etant egaux) */
		tr2.setUtilisateur(u1);
		assertFalse(c1.equals(tr2));
		assertFalse(tr2.equals(c1));
		c1.setUtilisateur(u2);
		assertFalse(c1.equals(tr2));
		assertFalse(tr2.equals(c1));
		c1.setUtilisateur(u1);
		assertTrue(c1.equals(tr2));
		assertTrue(tr2.equals(c1));

		/* Banque (les premieres props etant egales) */
		tr2.setBanque(b1);
		assertFalse(c1.equals(tr2));
		assertFalse(tr2.equals(c1));
		c1.setBanque(b2);
		assertFalse(c1.equals(tr2));
		assertFalse(tr2.equals(c1));
		c1.setBanque(b1);
		assertTrue(c1.equals(tr2));
		assertTrue(tr2.equals(c1));

		final Categorie c3 = new Categorie();
		assertFalse(c1.equals(c3));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {

		final Utilisateur u = utilisateurDao.findById(2).get();
		final Banque b = banqueDao.findById(1).get();
		final CodeUtilisateur c1 = new CodeUtilisateur();
		c1.setUtilisateur(u);
		c1.setBanque(b);
		c1.setCode("hash");
		final CodeUtilisateur tr2 = new CodeUtilisateur();
		tr2.setUtilisateur(u);
		tr2.setBanque(b);
		tr2.setCode("hash");
		final CodeUtilisateur c3 = new CodeUtilisateur();
		c3.setUtilisateur(null);
		c3.setBanque(null);
		c3.setCode(null);
		assertTrue(c3.hashCode() > 0);

		final int hash = c1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(c1.hashCode() == tr2.hashCode());
		c1.setCode("code");
		assertFalse(c1.hashCode() == tr2.hashCode());
		c1.setCode("hash");
		assertTrue(hash == c1.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
	}

	@Test
	public void testToString() {
		CodeUtilisateur c1 = codeUtilisateurDao.findById(1).get();
		assertTrue(c1.toString().equals("{CodeUtilisateur: " + c1.getCode() + "}"));

		c1 = new CodeUtilisateur();
		assertTrue(c1.toString().equals("{Empty CodeUtilisateur}"));
	}

	@Test
	@Transactional
	public void testClone() {
		final CodeUtilisateur c1 = codeUtilisateurDao.findById(1).get();
		c1.setCodeParent(codeUtilisateurDao.findById(2).get()); // pour eviter null
		c1.setCodeSelect(codeSelectDao.findById(3).get());
		final CodeUtilisateur clone = c1.clone();
		assertTrue(c1.equals(clone));
		assertTrue(c1.hashCode() == clone.hashCode());
		assertEquals(c1.getCodeUtilisateurId(), clone.getCodeUtilisateurId());
		assertEquals(c1.getCode(), clone.getCode());
		assertEquals(c1.getLibelle(), clone.getLibelle());
		assertEquals(c1.getCodesUtilisateur(), clone.getCodesUtilisateur());
		assertEquals(c1.getUtilisateur(), clone.getUtilisateur());
		assertEquals(c1.getBanque(), clone.getBanque());
		assertEquals(c1.getCodeDossier(), clone.getCodeDossier());
		assertEquals(c1.getCodeParent(), clone.getCodeParent());
		assertEquals(c1.getTranscodes(), clone.getTranscodes());
		assertEquals(c1.getCodeSelect(), clone.getCodeSelect());
	}

	/**
	 * Test des méthodes surchargées "equals" et hashcode pour la table
	 * transcodeUtilisateur.
	 */
	@Test
	public void testEqualsAndHashCodeTranscodeUtilisateur() {
		final TranscodeUtilisateur tr1 = new TranscodeUtilisateur();
		final TranscodeUtilisateur tr2 = new TranscodeUtilisateur();
		assertFalse(tr1.equals(null));
		assertNotNull(tr2);
		assertTrue(tr1.equals(tr1));
		assertTrue(tr1.equals(tr2));
		assertTrue(tr1.hashCode() == tr2.hashCode());

		final Integer i1 = 1;
		final Integer i2 = 2;
		final Integer i3 = new Integer(2);

		tr1.setCodeId(i1);
		assertFalse(tr1.equals(tr2));
		assertFalse(tr2.equals(tr1));
		assertTrue(tr1.hashCode() != tr2.hashCode());
		tr2.setCodeId(i2);
		assertFalse(tr1.equals(tr2));
		assertFalse(tr2.equals(tr1));
		assertTrue(tr1.hashCode() != tr2.hashCode());
		tr1.setCodeId(i3);
		assertTrue(tr1.equals(tr2));
		assertTrue(tr2.equals(tr1));
		assertTrue(tr1.hashCode() == tr2.hashCode());
		tr1.setCodeId(i2);
		assertTrue(tr1.equals(tr2));
		assertTrue(tr2.equals(tr1));
		assertTrue(tr1.hashCode() == tr2.hashCode());

		final TableCodage t1 = tableCodageDao.findById(1).get();
		final TableCodage t2 = tableCodageDao.findById(2).get();
		final TableCodage t3 = new TableCodage();
		t3.setNom(t2.getNom());
		t3.setVersion(t2.getVersion());
		assertFalse(t1.equals(t2));
		assertFalse(t1.hashCode() == t2.hashCode());
		assertTrue(t2.equals(t3));
		tr1.setTableCodage(t1);
		assertFalse(tr1.equals(tr2));
		assertFalse(tr2.equals(tr1));
		assertTrue(tr1.hashCode() != tr2.hashCode());
		tr2.setTableCodage(t2);
		assertFalse(tr1.equals(tr2));
		assertFalse(tr2.equals(tr1));
		assertTrue(tr1.hashCode() != tr2.hashCode());
		tr1.setTableCodage(t3);
		assertTrue(tr1.equals(tr2));
		assertTrue(tr2.equals(tr1));
		assertTrue(tr1.hashCode() == tr2.hashCode());
		tr1.setTableCodage(t2);
		assertTrue(tr1.equals(tr2));
		assertTrue(tr2.equals(tr1));
		assertTrue(tr1.hashCode() == tr2.hashCode());

		final CodeUtilisateur c1 = codeUtilisateurDao.findById(1).get();
		final CodeUtilisateur c2 = codeUtilisateurDao.findById(2).get();
		final CodeUtilisateur c3 = new CodeUtilisateur();
		c3.setCode(c2.getCode());
		c3.setLibelle(c2.getLibelle());
		c3.setUtilisateur(c2.getUtilisateur());
		c3.setBanque(c2.getBanque());
		assertFalse(c1.equals(c2));
		assertFalse(c1.hashCode() == c2.hashCode());
		assertTrue(c2.equals(c3));
		tr1.setCodeUtilisateur(c1);
		assertFalse(tr1.equals(tr2));
		assertFalse(tr2.equals(tr1));
		assertTrue(tr1.hashCode() != tr2.hashCode());
		tr2.setCodeUtilisateur(c2);
		assertFalse(tr1.equals(tr2));
		assertFalse(tr2.equals(tr1));
		assertTrue(tr1.hashCode() != tr2.hashCode());
		tr1.setCodeUtilisateur(c3);
		assertTrue(tr1.equals(tr2));
		assertTrue(tr2.equals(tr1));
		assertTrue(tr1.hashCode() == tr2.hashCode());
		tr1.setCodeUtilisateur(c2);
		assertTrue(tr1.equals(tr2));
		assertTrue(tr2.equals(tr1));
		assertTrue(tr1.hashCode() == tr2.hashCode());

		// dummy
		final Categorie c = new Categorie();
		assertFalse(tr1.equals(c));
	}
}
