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
package fr.aphp.tumorotek.test.dao.coeur.prelevement;

import java.util.HashSet;
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

import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.ProtocoleDao;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.AbstractPrelevementDelegate;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Protocole;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour la delegation des prélèvements. Classe de test créée le
 * 01/02/2012.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class PrelevementDelegateDaoTest extends AbstractDaoTest 
{

	@Autowired
	NatureDao natureDao;

	@Autowired
	PrelevementDao prelevementDao;

	@Autowired
	BanqueDao banqueDao;

	@Autowired
	ConsentTypeDao consentTypeDao;

	@Autowired
	ProtocoleDao protocoleDao;

	@Test
	public void testToString() {
		AbstractPrelevementDelegate p2 = prelevementDao.findById(2).get().getPrelevementSero();
		assertTrue(p2.toString().equals("{PRLVT2}." + PrelevementSero.class.getSimpleName()));
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un prelevement avec son
	 * delegate.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testCrudPrelAndDelegate() throws Exception {

		Prelevement p = new Prelevement();

		/* Champs obligatoires */
		final Banque b = banqueDao.findById(2).get();
		p.setBanque(b);
		p.setCode("prelDel1");
		final Nature n = natureDao.findById(1).get();
		p.setNature(n);
		final ConsentType ct = consentTypeDao.findById(2).get();
		p.setConsentType(ct);

		final PrelevementSero ps1 = new PrelevementSero();
		ps1.setLibelle("SERO1");
		List<Protocole> protos = IterableUtils.toList(protocoleDao.findAll());
		ps1.setProtocoles(new HashSet<>(protos));
		ps1.setDelegator(p);
		p.setDelegate(ps1);

		// Test de l'insertion
		prelevementDao.save(p);
		assertTrue(IterableUtils.toList(prelevementDao.findAll()).size() == 6);

		p = prelevementDao.findByCode("prelDel1").get(0);
		assertNotNull(p.getPrelevementSero());
		PrelevementSero ps2 = p.getPrelevementSero();
		assertTrue(ps2.getLibelle().equals("SERO1"));
		assertTrue(ps2.getProtocoles().size() == 3);

		protos = protocoleDao.findByNom("OFSEP");
		ps2.setProtocoles(new HashSet<>(protos));

		prelevementDao.save(p);
		assertTrue(IterableUtils.toList(prelevementDao.findAll()).size() == 6);

		// Test de la mise à jour
		p = prelevementDao.findByCode("prelDel1").get(0);
		assertNotNull(p.getPrelevementSero());
		ps2 = p.getPrelevementSero();
		assertTrue(ps2.getLibelle().equals("SERO1"));
		assertTrue(ps2.getProtocoles().size() == 1);
		assertTrue(ps2.getProtocoles().iterator().next().getNom().equals("OFSEP"));

		// Test de la délétion
		p.setCode("updated");
		p.getDelegate().removeAssociations();
		prelevementDao.save(p);
		p = prelevementDao.findByCode("updated").get(0);
		assertTrue(p.getPrelevementSero().getProtocoles().isEmpty());

		p.setDelegate(null);
		prelevementDao.save(p);
		p = prelevementDao.findByCode("updated").get(0);
		assertTrue(p.getPrelevementSero() == null);

		prelevementDao.deleteById(p.getPrelevementId());
		assertTrue(IterableUtils.toList(prelevementDao.findAll()).size() == 5);
	}

	@Test
	public void testIsEmpty() {
		final PrelevementSero pSero = new PrelevementSero();
		assertTrue(pSero.isEmpty());
		pSero.setLibelle("libelle");
		assertFalse(pSero.isEmpty());
		pSero.setLibelle("");
		assertTrue(pSero.isEmpty());
		pSero.setLibelle(null);
		assertTrue(pSero.isEmpty());
		pSero.getProtocoles().add(new Protocole());
		assertFalse(pSero.isEmpty());
		pSero.setProtocoles(null);
		assertTrue(pSero.isEmpty());
	}

	/**
	 * Test des méthodes surchargées "equals" et hashcode pour la table
	 * transcodeUtilisateur.
	 */
	@Test
	@Transactional
	public void testEqualsAndHashCode() {
		final AbstractPrelevementDelegate p1 = new PrelevementSero();
		final AbstractPrelevementDelegate p2 = new PrelevementSero();
		assertFalse(p1.equals(null));
		assertNotNull(p2);
		assertTrue(p1.equals(p2)); // FIXME false
		assertTrue(p2.equals(p1));
		assertTrue(p1.hashCode() == p2.hashCode());

		p1.setDelegator(prelevementDao.findById(1).get());
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
		assertTrue(p1.hashCode() != p2.hashCode());
		p2.setDelegator(prelevementDao.findById(2).get());
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
		assertTrue(p1.hashCode() != p2.hashCode());
		p1.setDelegator(prelevementDao.findById(2).get());
		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));
		assertTrue(p1.hashCode() == p2.hashCode());

		// dummy
		final Categorie c = new Categorie();
		assertFalse(p1.equals(c));
	}
}
