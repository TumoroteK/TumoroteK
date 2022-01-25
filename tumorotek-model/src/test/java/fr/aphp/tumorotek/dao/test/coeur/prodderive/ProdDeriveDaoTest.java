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
package fr.aphp.tumorotek.dao.test.coeur.prodderive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ModePrepaDeriveDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdQualiteDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.TransformationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le DAO ProdDeriveDao et le bean du domaine ProdDerive.
 * Classe de test créée le 29/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.1.1
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ProdDeriveDaoTest extends AbstractDaoTest {

	@Autowired
	ProdDeriveDao prodDeriveDao;
	
	@Autowired
	ProdQualiteDao prodQualiteDao;
	
	@Autowired
	ProdTypeDao prodTypeDao;
	
	@Autowired
	TransformationDao transformationDao;
	
	@Autowired
	ObjetStatutDao objetStatutDao;
	
	@Autowired
	BanqueDao banqueDao;
	
	@Autowired
	EntiteDao entiteDao;
	
	@Autowired
	TerminaleDao terminaleDao;
	
	@Autowired
	ModePrepaDeriveDao modePrepaDeriveDao;
	
	@Autowired
	PlateformeDao plateformeDao;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllCategories() {
		final List<ProdDerive> derives = IterableUtils.toList(prodDeriveDao.findAll());
		assertTrue(derives.size() == 4);
	}

	@Test
	public void testFindByBanques() {
		final List<Banque> banks = new java.util.ArrayList<>();
		banks.add(banqueDao.findById(1).get());
		banks.add(banqueDao.findById(2).get());
		final List<ProdDerive> res = prodDeriveDao.findByBanques(banks);
		assertTrue(res.size() == 4);
	}

	@Test
	public void testFindByBanquesAllIds() {
		final List<Banque> banks = new java.util.ArrayList<>();
		banks.add(banqueDao.findById(1).get());
		banks.add(banqueDao.findById(2).get());
		final List<Integer> res = prodDeriveDao.findByBanquesAllIds(banks);
		assertTrue(res.size() == 4);
	}

	/**
	 * Test l'appel de la méthode findByCode().
	 */
	@Test
	public void testFindByCode() {
		List<ProdDerive> derives = prodDeriveDao.findByCode("EHT.1.1");
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByCode("PTRA");
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByCode("PTRA%");
		assertTrue(derives.size() == 2);

		derives = prodDeriveDao.findByCode(null);
		assertTrue(derives.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByCodeOrLaboWithBanque().
	 */
	@Test
	public void testFindByCodeOrLaboWithBanque() {
		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();

		List<ProdDerive> derives = prodDeriveDao.findByCodeOrLaboWithBanque("EHT.1.1", b1);
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByCodeOrLaboWithBanque("PTRA", b1);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByCodeOrLaboWithBanque("PTRA%", b1);
		assertTrue(derives.size() == 2);

		derives = prodDeriveDao.findByCodeOrLaboWithBanque("PTRA%", b2);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByCodeOrLaboWithBanque("PTRA%", null);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByCodeOrLaboWithBanque(null, null);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByCodeOrLaboWithBanque("LABO%", b1);
		assertTrue(derives.size() == 3);

		derives = prodDeriveDao.findByCodeOrLaboWithBanque("LABO%", null);
		assertTrue(derives.size() == 0);
	}

	/**
	 * @since 2.1
	 */
	@Test
	public void testFindByCodeInPlateforme() {
		final Plateforme p1 = plateformeDao.findById(1).get();
		List<ProdDerive> ders = prodDeriveDao.findByCodeInPlateforme("PTRA.1.1", p1);
		assertTrue(ders.size() == 1);
		ders = prodDeriveDao.findByCodeInPlateforme("JEG.1.1", p1);
		assertTrue(ders.size() == 1);
		ders = prodDeriveDao.findByCodeInPlateforme("PTRA.1.1", null);
		assertTrue(ders.size() == 0);
		ders = prodDeriveDao.findByCodeInPlateforme("PTRA", p1);
		assertTrue(ders.size() == 0);
		ders = prodDeriveDao.findByCodeInPlateforme("PTRA", null);
		assertTrue(ders.size() == 0);
		ders = prodDeriveDao.findByCodeInPlateforme("%.1.1", p1);
		assertTrue(ders.size() == 3);
		ders = prodDeriveDao.findByCodeInPlateforme("PTRA.1._", p1);
		assertTrue(ders.size() == 2);
		ders = prodDeriveDao.findByCodeInPlateforme("%", p1);
		assertTrue(ders.size() == 4);
		ders = prodDeriveDao.findByCodeInPlateforme("%", null);
		assertTrue(ders.size() == 0);
		ders = prodDeriveDao.findByCodeInPlateforme(null, p1);
		assertTrue(ders.size() == 0);

		final Plateforme p2 = plateformeDao.findById(2).get();
		ders = prodDeriveDao.findByCodeInPlateforme("PTRA.1.1", p2);
		assertTrue(ders.size() == 0);
		ders = prodDeriveDao.findByCodeInPlateforme("PTRA", p2);
		assertTrue(ders.size() == 0);
		ders = prodDeriveDao.findByCodeInPlateforme("%", p2);
		assertTrue(ders.size() == 0);
		ders = prodDeriveDao.findByCodeInPlateforme(null, p2);
		assertTrue(ders.size() == 0);

		// car ne s'applique pas sur numero labo!
		ders = prodDeriveDao.findByCodeInPlateforme("LABO_PTRA", p1);
		assertTrue(ders.size() == 0);
		ders = prodDeriveDao.findByCodeInPlateforme("XXX", p1);
		assertTrue(ders.size() == 0);
		ders = prodDeriveDao.findByCodeInPlateforme("LABO%", p1);
		assertTrue(ders.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByCodeOrLaboWithBanqueReturnIds().
	 */
	@Test
	public void testFindByCodeOrLaboWithBanqueReturnIds() {
		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();

		List<Integer> derives = prodDeriveDao.findByCodeOrLaboWithBanqueReturnIds("EHT.1.1", b1);
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByCodeOrLaboWithBanqueReturnIds("PTRA", b1);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByCodeOrLaboWithBanqueReturnIds("PTRA%", b1);
		assertTrue(derives.size() == 2);

		derives = prodDeriveDao.findByCodeOrLaboWithBanqueReturnIds("PTRA%", b2);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByCodeOrLaboWithBanqueReturnIds("PTRA%", null);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByCodeOrLaboWithBanqueReturnIds(null, null);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByCodeOrLaboWithBanqueReturnIds("LABO%", b1);
		assertTrue(derives.size() == 3);

		derives = prodDeriveDao.findByCodeOrLaboWithBanqueReturnIds("LABO%", null);
		assertTrue(derives.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByCodeLabo().
	 */
	@Test
	public void testFindByCodeLabo() {
		List<ProdDerive> derives = prodDeriveDao.findByCodeLabo("LABO_EHT");
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByCodeLabo("LABO");
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByCodeLabo("LABO%");
		assertTrue(derives.size() == 3);
	}

	/**
	 * Test l'appel de la méthode findByDateStockAfterDate(). throws Exception Lance
	 * une exception en cas d'erreur.
	 */
	@Test
	public void testFindByDateStockAfterDate() throws Exception {
		final Calendar search = Calendar.getInstance();
		search.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2009"));
		List<ProdDerive> derives = prodDeriveDao.findByDateStockAfterDate(search);
		assertTrue(derives.size() == 4);

		search.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2009"));
		derives = prodDeriveDao.findByDateStockAfterDate(search);
		assertTrue(derives.size() == 2);
	}

	/**
	 * Test l'appel de la méthode findByDateTransformationAfterDate(). throws
	 * Exception Lance une exception en cas d'erreur.
	 */
	@Test
	public void testFindByDateTransformationAfterDate() throws Exception {
		final Calendar search = Calendar.getInstance();
		search.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2009"));
		List<ProdDerive> derives = prodDeriveDao.findByDateTransformationAfterDate(search);
		assertTrue(derives.size() == 4);

		search.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2009"));
		derives = prodDeriveDao.findByDateTransformationAfterDate(search);
		assertTrue(derives.size() == 2);

		derives = prodDeriveDao.findByDateTransformationAfterDate(null);
		assertTrue(derives.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByDateTransformationAfterDateWithBanque().
	 * throws Exception Lance une exception en cas d'erreur.
	 */
	@Test
	public void testFindByDateTransformationAfterDateWithBanque() throws Exception {
		final Banque b1 = banqueDao.findById(1).get();

		final Calendar search = Calendar.getInstance();
		search.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2009"));
		List<ProdDerive> derives = prodDeriveDao.findByDateTransformationAfterDateWithBanque(search, b1);
		assertTrue(derives.size() == 3);

		search.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2009"));
		derives = prodDeriveDao.findByDateTransformationAfterDateWithBanque(search, b1);
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByDateTransformationAfterDateWithBanque(search, null);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByDateTransformationAfterDateWithBanque(null, b1);
		assertTrue(derives.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByExcludedIdCodes().
	 */
	@Test
	public void testFindByExcludedIdCodes() {
		final Banque b = banqueDao.findById(1).get();
		List<String> codes = prodDeriveDao.findByExcludedIdCodes(1, b);
		assertTrue(codes.size() == 2);
		codes = prodDeriveDao.findByExcludedIdCodes(10, b);
		assertTrue(codes.size() == 3);
		codes = prodDeriveDao.findByExcludedIdCodes(null, null);
		assertTrue(codes.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByIdInList().
	 */
	@Test
	public void testFindByIdInList() {
		final List<Integer> ids = new ArrayList<>();
		ids.add(1);
		ids.add(2);
		ids.add(3);
		ids.add(10);
		List<ProdDerive> liste = prodDeriveDao.findByIdInList(ids);
		assertTrue(liste.size() == 3);

		liste = prodDeriveDao.findByIdInList(null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByObjetStatut().
	 */
	@Test
	public void testFindByObjetStatut() {
		ObjetStatut obj = objetStatutDao.findById(1).get();
		List<ProdDerive> derives = prodDeriveDao.findByObjetStatut(obj);
		assertTrue(derives.size() == 3);

		obj = objetStatutDao.findById(4).get();
		derives = prodDeriveDao.findByObjetStatut(obj);
		assertTrue(derives.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByProdType().
	 */
	@Test
	public void testFindByProdType() {
		ProdType type = prodTypeDao.findById(1).get();
		List<ProdDerive> derives = prodDeriveDao.findByProdType(type);
		assertTrue(derives.size() == 2);

		type = prodTypeDao.findById(3).get();
		derives = prodDeriveDao.findByProdType(type);
		assertTrue(derives.size() == 1);
	}

	/**
	 * Test l'appel de la méthode findByModePrepaDerive().
	 */
	@Test
	public void testFindByModePrepaDerive() {
		ModePrepaDerive m1 = modePrepaDeriveDao.findById(1).get();
		List<ProdDerive> derives = prodDeriveDao.findByModePrepaDerive(m1);
		assertTrue(derives.size() == 2);

		m1 = modePrepaDeriveDao.findById(4).get();
		derives = prodDeriveDao.findByModePrepaDerive(m1);
		assertTrue(derives.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByProdQualite().
	 */
	@Test
	public void testFindByProdQualite() {
		ProdQualite qualite = prodQualiteDao.findById(1).get();
		List<ProdDerive> derives = prodDeriveDao.findByProdQualite(qualite);
		assertTrue(derives.size() == 3);

		qualite = prodQualiteDao.findById(3).get();
		derives = prodDeriveDao.findByProdQualite(qualite);
		assertTrue(derives.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByTransformation().
	 */
	@Test
	public void testFindByTransformation() {
		Transformation transfo = transformationDao.findById(1).get();
		List<ProdDerive> derives = prodDeriveDao.findByTransformation(transfo);
		assertTrue(derives.size() == 1);

		transfo = transformationDao.findById(5).get();
		derives = prodDeriveDao.findByTransformation(transfo);
		assertTrue(derives.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByBanqueSelectCode().
	 */
	@Test
	public void testFindByBanqueSelectCode() {
		final Banque b1 = banqueDao.findById(1).get();
		List<String> codes = prodDeriveDao.findByBanqueSelectCode(b1);
		assertTrue(codes.size() == 3);
		assertTrue(codes.get(2).equals("EHT.1.1"));

		final Banque b2 = banqueDao.findById(2).get();
		codes = prodDeriveDao.findByBanqueSelectCode(b2);
		assertTrue(codes.size() == 1);
	}

	/**
	 * Test l'appel de la méthode findByBanqueAndQuantiteSelectCode().
	 */
	@Test
	public void testFindByBanqueAndQuantiteSelectCode() {
		final Banque b1 = banqueDao.findById(1).get();
		List<String> codes = prodDeriveDao.findByBanqueAndQuantiteSelectCode(b1);
		assertTrue(codes.size() == 2);
		assertTrue(codes.get(1).equals("PTRA.1.2"));

		final Banque b2 = banqueDao.findById(2).get();
		codes = prodDeriveDao.findByBanqueAndQuantiteSelectCode(b2);
		assertTrue(codes.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByBanqueStatutSelectCode().
	 */
	@Test
	public void testFindByBanqueStatutSelectCode() {
		final Banque b1 = banqueDao.findById(1).get();
		final ObjetStatut o1 = objetStatutDao.findById(1).get();
		List<String> codes = prodDeriveDao.findByBanqueStatutSelectCode(b1, o1);
		assertTrue(codes.size() == 2);
		assertTrue(codes.get(0).equals("PTRA.1.1"));

		final ObjetStatut o2 = objetStatutDao.findById(2).get();
		codes = prodDeriveDao.findByBanqueStatutSelectCode(b1, o2);
		assertTrue(codes.size() == 1);

		final ObjetStatut o3 = objetStatutDao.findById(3).get();
		codes = prodDeriveDao.findByBanqueStatutSelectCode(b1, o3);
		assertTrue(codes.size() == 0);

		final Banque b3 = banqueDao.findById(3).get();
		codes = prodDeriveDao.findByBanqueStatutSelectCode(b3, o1);
		assertTrue(codes.size() == 0);

		codes = prodDeriveDao.findByBanqueStatutSelectCode(b1, null);
		assertTrue(codes.size() == 0);

		codes = prodDeriveDao.findByBanqueStatutSelectCode(null, o1);
		assertTrue(codes.size() == 0);

		codes = prodDeriveDao.findByBanqueStatutSelectCode(null, null);
		assertTrue(codes.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByBanqueInListStatutSelectCode().
	 */
	@Test
	public void testFindByBanqueInListStatutSelectCode() {
		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();
		final List<Banque> banques = new ArrayList<>();
		banques.add(b1);
		banques.add(b2);
		final ObjetStatut o1 = objetStatutDao.findById(1).get();
		List<String> codes = prodDeriveDao.findByBanqueInListStatutSelectCode(banques, o1);
		assertTrue(codes.size() == 3);

		final ObjetStatut o2 = objetStatutDao.findById(2).get();
		codes = prodDeriveDao.findByBanqueInListStatutSelectCode(banques, o2);
		assertTrue(codes.size() == 1);

		final ObjetStatut o3 = objetStatutDao.findById(3).get();
		codes = prodDeriveDao.findByBanqueInListStatutSelectCode(banques, o3);
		assertTrue(codes.size() == 0);

		codes = prodDeriveDao.findByBanqueInListStatutSelectCode(banques, null);
		assertTrue(codes.size() == 0);

		codes = prodDeriveDao.findByBanqueInListStatutSelectCode(null, o1);
		assertTrue(codes.size() == 0);

		codes = prodDeriveDao.findByBanqueInListStatutSelectCode(null, null);
		assertTrue(codes.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByTerminale().
	 */
	@Test
	public void testFindByTerminale() {
		Entite entite = entiteDao.findByNom("ProdDerive").get(0);
		final Terminale term1 = terminaleDao.findById(1).get();
		List<ProdDerive> liste = prodDeriveDao.findByTerminale(entite, term1);
		assertTrue(liste.size() == 2);

		entite = entiteDao.findByNom("Prelevement").get(0);
		liste = prodDeriveDao.findByTerminale(entite, term1);
		assertTrue(liste.size() == 0);

		entite = entiteDao.findByNom("ProdDerive").get(0);
		final Terminale term2 = terminaleDao.findById(2).get();
		liste = prodDeriveDao.findByTerminale(entite, term2);
		assertTrue(liste.size() == 0);

		liste = prodDeriveDao.findByTerminale(entite, null);
		assertTrue(liste.size() == 0);

		liste = prodDeriveDao.findByTerminale(null, term1);
		assertTrue(liste.size() == 0);

		liste = prodDeriveDao.findByTerminale(null, null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByTerminaleDirect().
	 */
	@Test
	public void testFindByTerminaleDirect() {
		final Terminale term1 = terminaleDao.findById(1).get();
		List<ProdDerive> liste = prodDeriveDao.findByTerminaleDirect(term1);
		assertTrue(liste.size() == 2);

		final Terminale term2 = terminaleDao.findById(2).get();
		liste = prodDeriveDao.findByTerminaleDirect(term2);
		assertTrue(liste.size() == 0);

		liste = prodDeriveDao.findByTerminaleDirect(null);
		assertTrue(liste.size() == 0);

	}

	/**
	 * Test l'appel de la méthode findByParentAndType().
	 */
	@Test
	public void testFindByParentAndType() {
		Entite entite = entiteDao.findByNom("Echantillon").get(0);
		List<ProdDerive> derives = prodDeriveDao.findByParentAndType(1, entite, "ADN");
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByParentAndType(2, entite, "ADN");
		assertTrue(derives.size() == 0);

		entite = entiteDao.findByNom("Prelevement").get(0);
		derives = prodDeriveDao.findByParentAndType(1, entite, "ADN");
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByParentAndType(1, entite, "ARN");
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByParentAndType(null, entite, "ARN");
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByParentAndType(1, null, "ARN");
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByParentAndType(1, entite, null);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByParentAndType(null, null, null);
		assertTrue(derives.size() == 0);
	}

	@Test
	public void testFindByParent() {
		Entite entite = entiteDao.findByNom("Echantillon").get(0);
		List<ProdDerive> derives = prodDeriveDao.findByParent(1, entite);
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByParent(2, entite);
		assertTrue(derives.size() == 0);

		entite = entiteDao.findByNom("Prelevement").get(0);
		derives = prodDeriveDao.findByParent(1, entite);
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByParent(null, entite);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByParent(1, null);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByParent(null, null);
		assertTrue(derives.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByEchantillonPatientNomReturnIds().
	 */
	@Test
	public void testFindByEchantillonPatientNomReturnIds() {
		final Banque b1 = banqueDao.findById(1).get();
		List<Integer> derives = prodDeriveDao.findByEchantillonPatientNomReturnIds("DELPHINO", b1);
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByEchantillonPatientNomReturnIds("876", b1);
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByEchantillonPatientNomReturnIds("MAYER", b1);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByEchantillonPatientNomReturnIds("12%", b1);
		assertTrue(derives.size() == 0);

		final Banque b2 = banqueDao.findById(2).get();
		derives = prodDeriveDao.findByEchantillonPatientNomReturnIds("MAYER", b2);
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByEchantillonPatientNomReturnIds("12%", b2);
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByEchantillonPatientNomReturnIds("JACKSON", b1);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByEchantillonPatientNomReturnIds(null, b1);
		assertTrue(derives.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByPrelevementPatientNomreturnIds().
	 */
	@Test
	public void testFindByPrelevementPatientNomreturnIds() {
		final Banque b1 = banqueDao.findById(1).get();
		List<Integer> derives = prodDeriveDao.findByPrelevementPatientNomreturnIds("DELPHINO", b1);
		assertTrue(derives.size() == 1);

		derives = prodDeriveDao.findByPrelevementPatientNomreturnIds("MAYER", b1);
		assertTrue(derives.size() == 0);

		derives = prodDeriveDao.findByPrelevementPatientNomreturnIds(null, b1);
		assertTrue(derives.size() == 0);
	}

	@Test
	public void testFindByCodeInListWithBanque() {
		List<String> criteres = new ArrayList<>();
		criteres.add("PTRA.1.1");
		criteres.add("PTRA.1.2");
		criteres.add("JEG.1.1");
		final List<Banque> bks = new ArrayList<>();
		bks.add(banqueDao.findById(1).get());
		bks.add(banqueDao.findById(2).get());

		List<Object[]> liste = prodDeriveDao.findByCodeInListWithBanque(criteres, bks);
		assertTrue(liste.size() == 3);
		for (final Object[] obj : liste) {
			if ((Integer) obj[0] == 1) {
				assertTrue(obj[1].equals("PTRA.1.1"));
			} else if ((Integer) obj[0] == 2) {
				assertTrue(obj[1].equals("PTRA.1.2"));
			} else if ((Integer) obj[0] == 4) {
				assertTrue(obj[1].equals("JEG.1.1"));
			}
		}

		criteres = new ArrayList<>();
		criteres.add("PTRA.1.1");
		liste = prodDeriveDao.findByCodeInListWithBanque(criteres, bks);
		assertTrue(liste.size() == 1);
		assertTrue((Integer) liste.get(0)[0] == 1);
		assertTrue(liste.get(0)[1].equals("PTRA.1.1"));
	}

	@Test
	public void testFindByEchantillonPatientNomInListReturnIds() {
		List<String> criteres = new ArrayList<>();
		criteres.add("DELPHINO");
		criteres.add("MAYER");
		criteres.add("SOLIS");
		final List<Banque> bks = new ArrayList<>();
		bks.add(banqueDao.findById(1).get());
		bks.add(banqueDao.findById(2).get());

		List<Integer> liste = prodDeriveDao.findByEchantillonPatientNomInListReturnIds(criteres, bks);
		assertTrue(liste.size() == 2);

		criteres = new ArrayList<>();
		criteres.add("DELPHINO");
		criteres.add("12");
		liste = prodDeriveDao.findByEchantillonPatientNomInListReturnIds(criteres, bks);
		assertTrue(liste.size() == 2);

		criteres = new ArrayList<>();
		criteres.add("DELPHINO");
		criteres.add("876");
		liste = prodDeriveDao.findByEchantillonPatientNomInListReturnIds(criteres, bks);
		assertTrue(liste.size() == 1);
	}

	@Test
	public void testFindByPrelevementPatientNomInListreturnIds() {
		List<String> criteres = new ArrayList<>();
		criteres.add("DELPHINO");
		criteres.add("MAYER");
		criteres.add("SOLIS");
		final List<Banque> bks = new ArrayList<>();
		bks.add(banqueDao.findById(1).get());
		bks.add(banqueDao.findById(2).get());

		List<Integer> liste = prodDeriveDao.findByPrelevementPatientNomInListreturnIds(criteres, bks);
		assertTrue(liste.size() == 1);

		criteres = new ArrayList<>();
		criteres.add("DELPHINO");
		criteres.add("12");
		liste = prodDeriveDao.findByPrelevementPatientNomInListreturnIds(criteres, bks);
		assertTrue(liste.size() == 1);

		criteres = new ArrayList<>();
		criteres.add("DELPHINO");
		criteres.add("876");
		liste = prodDeriveDao.findByPrelevementPatientNomInListreturnIds(criteres, bks);
		assertTrue(liste.size() == 1);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un produit dérivé.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testCrud() throws Exception {
		final ProdDerive p = new ProdDerive();
		final String codeUpdated = "CodeUp";
		final ObjetStatut statut = objetStatutDao.findById(1).get();

		// Emplacement e = emplacementDao.findById(1).get();
		final Banque b = banqueDao.findById(1).get();
		// assertNotNull(e);
		final ProdType pt = prodTypeDao.findById(1).get();
		assertNotNull(pt);
		p.setCode("code");
		// p.setEmplacement(e);
		p.setProdType(pt);
		p.setDateStock(null);
		p.setDateTransformation(null);
		p.setBanque(b);
		p.setObjetStatut(statut);
		p.setArchive(false);
		p.setVolumeInit((float) 10.1111);
		p.setVolume((float) 9.9999);
		p.setConc((float) 0.1256);
		p.setQuantiteInit((float) 7.8945);
		p.setQuantite((float) 6);
		p.setConformeCession(true);
		p.setConformeTraitement(true);
		// Test de l'insertion
		prodDeriveDao.save(p);
		assertEquals(new Integer(5), p.getProdDeriveId());

		// Test de la mise à jour
		final ProdDerive p2 = prodDeriveDao.findById(new Integer(5)).get();
		assertNotNull(p2);
		assertNotNull(p2.getProdType());
		assertNull(p2.getDateStock());
		assertTrue(p2.getVolumeInit() == (float) 10.111);
		assertTrue(p2.getVolume() == 10);
		assertTrue(p2.getConc() == (float) 0.126);
		assertTrue(p2.getQuantiteInit() == (float) 7.894);
		assertTrue(p2.getQuantite() == 6);
		assertTrue(p2.getConformeCession());
		assertTrue(p2.getConformeTraitement());
		p2.setCode(codeUpdated);
		p.setVolumeInit(null);
		p.setVolume(null);
		p.setConc(null);
		p.setQuantiteInit(null);
		p.setQuantite(null);
		prodDeriveDao.save(p2);
		assertTrue(prodDeriveDao.findById(new Integer(5)).get().getCode().equals(codeUpdated));
		assertNull(prodDeriveDao.findById(new Integer(5)).get().getVolume());
		assertNull(prodDeriveDao.findById(new Integer(5)).get().getVolumeInit());
		assertNull(prodDeriveDao.findById(new Integer(5)).get().getConc());
		assertNull(prodDeriveDao.findById(new Integer(5)).get().getQuantite());
		assertNull(prodDeriveDao.findById(new Integer(5)).get().getQuantiteInit());

		// Test de la délétion
		prodDeriveDao.deleteById(new Integer(5));
		assertFalse(prodDeriveDao.findById(new Integer(5)).isPresent());
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String code1 = "code1";
		final String code2 = "code2";
		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();
		final ProdDerive p1 = new ProdDerive();
		p1.setCode(code1);
		p1.setBanque(b1);
		final ProdDerive p2 = new ProdDerive();
		p2.setCode(code1);
		p2.setBanque(b1);

		// L'objet 1 n'est pas égal à null
		assertFalse(p1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(p1.equals(p1));
		// 2 objets sont égaux entre eux
		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));

		p1.setCode(null);
		p1.setBanque(null);
		p2.setCode(null);
		p2.setBanque(null);
		assertTrue(p1.equals(p2));
		p2.setBanque(b1);
		assertFalse(p1.equals(p2));
		p1.setBanque(b1);
		assertTrue(p1.equals(p2));
		p2.setBanque(b2);
		assertFalse(p1.equals(p2));
		p2.setBanque(null);
		assertFalse(p1.equals(p2));
		p2.setCode(code1);
		assertFalse(p1.equals(p2));

		p1.setCode(code1);
		p1.setBanque(null);
		p2.setBanque(null);
		p2.setCode(code1);
		assertTrue(p1.equals(p2));
		p2.setCode(code2);
		assertFalse(p1.equals(p2));
		p2.setBanque(b1);
		assertFalse(p1.equals(p2));

		// Vérification de la différenciation de 2 objets
		p1.setBanque(b1);
		assertFalse(p1.equals(p2));
		p2.setBanque(b2);
		p2.setCode(code1);
		assertFalse(p1.equals(p2));

		final Categorie c = new Categorie();
		assertFalse(p1.equals(c));

	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String code1 = "code1";
		final Banque b1 = banqueDao.findById(1).get();
		final ProdDerive p1 = new ProdDerive();
		p1.setCode(code1);
		p1.setBanque(b1);
		final ProdDerive p2 = new ProdDerive();
		p2.setCode(code1);
		p2.setBanque(b1);
		final ProdDerive p3 = new ProdDerive();
		p3.setCode(null);
		p3.setBanque(null);
		assertTrue(p3.hashCode() > 0);

		final int hash = p1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(p1.hashCode() == p2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());

	}

	/**
	 * Test toString().
	 */
	@Test
	public void testToString() {
		final ProdDerive p1 = prodDeriveDao.findById(1).get();
		assertTrue(p1.toString().equals("{" + p1.getCode() + "}"));
		assertTrue(p1.listableObjectId().equals(new Integer(1)));
		assertTrue(p1.entiteNom().equals(entiteDao.findByNom("ProdDerive").get(0).getNom()));
		final ProdDerive p2 = new ProdDerive();
		assertTrue(p2.toString().equals("{Empty ProdDerive}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	@Transactional
	public void testClone() {
		final ProdDerive p1 = prodDeriveDao.findById(1).get();
		final ProdDerive p2 = p1.clone();
		assertTrue(p1.equals(p2));

		if (p1.getProdDeriveId() != null) {
			assertTrue(p1.getProdDeriveId().equals(p2.getProdDeriveId()));
		} else {
			assertNull(p2.getProdDeriveId());
		}
		if (p1.getBanque() != null) {
			assertTrue(p1.getBanque().equals(p2.getBanque()));
		} else {
			assertNull(p2.getBanque());
		}
		if (p1.getProdType() != null) {
			assertTrue(p1.getProdType().equals(p2.getProdType()));
		} else {
			assertNull(p2.getProdType());
		}
		if (p1.getCode() != null) {
			assertTrue(p1.getCode().equals(p2.getCode()));
		} else {
			assertNull(p2.getCode());
		}
		if (p1.getCodeLabo() != null) {
			assertTrue(p1.getCodeLabo().equals(p2.getCodeLabo()));
		} else {
			assertNull(p2.getCodeLabo());
		}
		if (p1.getObjetStatut() != null) {
			assertTrue(p1.getObjetStatut().equals(p2.getObjetStatut()));
		} else {
			assertNull(p2.getObjetStatut());
		}
		if (p1.getCollaborateur() != null) {
			assertTrue(p1.getCollaborateur().equals(p2.getCollaborateur()));
		} else {
			assertNull(p2.getCollaborateur());
		}
		if (p1.getVolumeInit() != null) {
			assertTrue(p1.getVolumeInit().equals(p2.getVolumeInit()));
		} else {
			assertNull(p2.getVolumeInit());
		}
		if (p1.getVolume() != null) {
			assertTrue(p1.getVolume().equals(p2.getVolume()));
		} else {
			assertNull(p2.getVolume());
		}
		if (p1.getConc() != null) {
			assertTrue(p1.getConc().equals(p2.getConc()));
		} else {
			assertNull(p2.getConc());
		}
		if (p1.getDateStock() != null) {
			assertTrue(p1.getDateStock().equals(p2.getDateStock()));
		} else {
			assertNull(p2.getDateStock());
		}
		if (p1.getEmplacement() != null) {
			assertTrue(p1.getEmplacement().getAdrl().equals(p2.getEmplacement().getAdrl()));
		} else {
			assertNull(p2.getEmplacement());
		}
		if (p1.getVolumeUnite() != null) {
			assertTrue(p1.getVolumeUnite().equals(p2.getVolumeUnite()));
		} else {
			assertNull(p2.getVolumeUnite());
		}
		if (p1.getConcUnite() != null) {
			assertTrue(p1.getConcUnite().equals(p2.getConcUnite()));
		} else {
			assertNull(p2.getConcUnite());
		}
		if (p1.getQuantiteInit() != null) {
			assertTrue(p1.getQuantiteInit().equals(p2.getQuantiteInit()));
		} else {
			assertNull(p2.getQuantiteInit());
		}
		if (p1.getQuantite() != null) {
			assertTrue(p1.getQuantite().equals(p2.getQuantite()));
		} else {
			assertNull(p2.getQuantite());
		}
		if (p1.getQuantiteUnite() != null) {
			assertTrue(p1.getQuantiteUnite().equals(p2.getQuantiteUnite()));
		} else {
			assertNull(p2.getQuantiteUnite());
		}
		if (p1.getProdQualite() != null) {
			assertTrue(p1.getProdQualite().equals(p2.getProdQualite()));
		} else {
			assertNull(p2.getProdQualite());
		}
		if (p1.getTransformation() != null) {
			assertTrue(p1.getTransformation().equals(p2.getTransformation()));
		} else {
			assertNull(p2.getTransformation());
		}
		if (p1.getDateTransformation() != null) {
			assertTrue(p1.getDateTransformation().equals(p2.getDateTransformation()));
		} else {
			assertNull(p2.getDateTransformation());
		}
		if (p1.getModePrepaDerive() != null) {
			assertTrue(p1.getModePrepaDerive().equals(p2.getModePrepaDerive()));
		} else {
			assertNull(p2.getModePrepaDerive());
		}
		if (p1.getEtatIncomplet() != null) {
			assertTrue(p1.getEtatIncomplet().equals(p2.getEtatIncomplet()));
		} else {
			assertNull(p2.getEtatIncomplet());
		}
		if (p1.getArchive() != null) {
			assertTrue(p1.getArchive().equals(p2.getArchive()));
		} else {
			assertNull(p2.getArchive());
		}
		if (p1.getConformeTraitement() != null) {
			assertTrue(p1.getConformeTraitement().equals(p2.getConformeTraitement()));
		} else {
			assertNull(p2.getConformeTraitement());
		}
		if (p1.getConformeCession() != null) {
			assertTrue(p1.getConformeCession().equals(p2.getConformeCession()));
		} else {
			assertNull(p2.getConformeCession());
		}

	}

	@Test
	public void testSortByCode() {
		final ProdDerive p1 = new ProdDerive();
		p1.setCode("TEST.1");
		final ProdDerive p2 = new ProdDerive();
		p2.setCode("TEST.1.11");
		final ProdDerive p3 = new ProdDerive();
		p3.setCode("TEST.1.9");
		final ProdDerive p4 = new ProdDerive();
		p4.setCode("TEST.12");
		final ProdDerive p5 = new ProdDerive();
		p5.setCode("TEST.3");
		final ProdDerive p6 = new ProdDerive();
		p6.setCode("TEST.1.1");
		final ProdDerive p7 = new ProdDerive();
		p7.setCode("TEST.1.1.1");
		final ProdDerive p8 = new ProdDerive();
		p8.setCode("ZAAA.1.1");
		final ProdDerive p9 = new ProdDerive();
		p9.setCode("ZAAA..");
		final ProdDerive p10 = new ProdDerive();
		p10.setCode("AAA.1.1");
		final List<ProdDerive> prods = new ArrayList<>();
		prods.add(p5);
		prods.add(p2);
		prods.add(p3);
		prods.add(p9);
		prods.add(p10);
		prods.add(p8);
		prods.add(p6);
		prods.add(p7);
		prods.add(p1);
		prods.add(p4);
		Collections.sort(prods, new ProdDerive.CodeComparator(false));
		assertTrue(prods.get(0).equals(p10));
		assertTrue(prods.get(1).equals(p1));
		assertTrue(prods.get(2).equals(p6));
		assertTrue(prods.get(3).equals(p7));
		assertTrue(prods.get(4).equals(p3));
		assertTrue(prods.get(5).equals(p2));
		assertTrue(prods.get(6).equals(p5));
		assertTrue(prods.get(7).equals(p4));
		assertTrue(prods.get(8).equals(p9));
		assertTrue(prods.get(9).equals(p8));
	}

	@Test
	public void testFindByEmplacementDao() {
		List<ProdDerive> derives = prodDeriveDao.findByEmplacement(terminaleDao.findById(1).get(), 1);
		assertTrue(derives.size() == 1);
		assertTrue(derives.contains(prodDeriveDao.findById(1).get()));
		derives = prodDeriveDao.findByEmplacement(terminaleDao.findById(1).get(), 3);
		assertTrue(derives.isEmpty());
		derives = prodDeriveDao.findByEmplacement(null, 1);
		assertTrue(derives.isEmpty());
		derives = prodDeriveDao.findByEmplacement(terminaleDao.findById(1).get(), null);
		assertTrue(derives.isEmpty());
	}

	@Test
	public void testFindCountByParent() {
		// nulls
		Long cc = prodDeriveDao.findCountByParent(null, null).get(0);
		assertTrue(cc.longValue() == 0);
		cc = prodDeriveDao.findCountByParent(1, entiteDao.findById(3).get()).get(0);
		assertTrue(cc.longValue() == 1);
		cc = prodDeriveDao.findCountByParent(2, entiteDao.findById(3).get()).get(0);
		assertTrue(cc.longValue() == 0);
		cc = prodDeriveDao.findCountByParent(4, entiteDao.findById(3).get()).get(0);
		assertTrue(cc.longValue() == 1);
		cc = prodDeriveDao.findCountByParent(1, entiteDao.findById(8).get()).get(0);
		assertTrue(cc.longValue() == 1);

	}
}
