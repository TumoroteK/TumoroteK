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
package fr.aphp.tumorotek.manager.test.report;

import fr.aphp.tumorotek.dao.cession.CessionTypeDao;
import fr.aphp.tumorotek.dao.code.CimMasterDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.manager.report.IncaReportManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.cession.CessionType;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 
 * Classe de test pour le manager CategorieManager.
 * Classe créée le 06/01/10.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class IncaReportManagerTest extends AbstractManagerTest4 {
	
	@Autowired
	private IncaReportManager incaReportManager;
	@Autowired
	private EtablissementDao etablissementDao;
	@Autowired
	private BanqueDao banqueDao;
	@Autowired
	private CessionTypeDao cessionTypeDao;
	@Autowired
	private NatureDao natureDao;
	@Autowired
	private EchantillonTypeDao echantillonTypeDao;
	@Autowired
	private ConsentTypeDao consentTypeDao;
	@Autowired
	private CimMasterDao cimMasterDao;

	@Test
	public void testCountSamplesManager() throws ParseException {
		Calendar d1 = Calendar.getInstance();
		d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/05/2009"));
		Calendar d2 = Calendar.getInstance();
		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("04/11/2009")); 
		List<Banque> banks = new ArrayList<Banque>();
		Banque b1 = banqueDao.findById(1);
		banks.add(b1);
		
		Integer month = 31;
		
		List<Long> res = incaReportManager
			.countSamplesManager(d1, d2, month, banks, false);
		
		assertTrue(res.size() == 6);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertEquals(Long.valueOf(1), res.get(4));
		assertTrue(res.get(5) == 1);
		//assertTrue(res.get(6) == 0);
		
		res = incaReportManager
			.countSamplesManager(d1, d2, month, banks, true);
	
		assertTrue(res.size() == 6);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 1);
		assertTrue(res.get(5) == 2);
		//assertTrue(res.get(6) == 1);
		
		// le nombre d'intervalle tombe juste
//		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/11/2009"));
//		res = incaReportManager
//					.countSamplesManager(d1, d2, month, banks, false);
//		assertTrue(res.size() == 6);
//		assertTrue(res.get(0) == 0);
//		assertTrue(res.get(1) == 0);
//		assertTrue(res.get(2) == 1);
//		assertTrue(res.get(3) == 0);
//		assertTrue(res.get(4) == 1);
//		assertTrue(res.get(5) == 1);
		
		banks.clear();
		res = incaReportManager
				.countSamplesManager(d1, d2, month, banks, false);
		assertNull(res);
		
		res = incaReportManager
			.countSamplesManager(d1, d2, month, null, false);
		assertNull(res);
	}
	
	@Test
	public void testCountEchanSamplesExtManager() throws ParseException {
		Calendar d1 = Calendar.getInstance();
		d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/05/2009"));
		Calendar d2 = Calendar.getInstance();
		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("29/11/2009")); 
		List<Banque> banks = new ArrayList<Banque>();
		Banque b1 = banqueDao.findById(1);
		banks.add(b1);
		Banque b2 = banqueDao.findById(2);
		banks.add(b2);
		
		List<Etablissement> etabs = new ArrayList<Etablissement>();
		Etablissement e4 = etablissementDao.findById(2);
		etabs.add(e4);
		
		Integer month = 31;
		
		List<Long> res = incaReportManager
			.countSamplesExtManager(d1, d2, month, banks, etabs);
		
		assertTrue(res.size() == 6);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertEquals(Long.valueOf(1), res.get(4));
		assertTrue(res.get(5) == 2);
		
		Etablissement e1 = etablissementDao.findById(1);
		etabs.add(e1);
				
		// le nombre d'intervalle tombe juste
		//d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2008"));
		res = incaReportManager
			.countSamplesExtManager(d1, d2, month, banks, etabs);
		
		assertTrue(res.size() == 6);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 0);
		assertTrue(res.get(5) == 0);
				
		res = incaReportManager
			.countSamplesExtManager(d1, d2, month, null, etabs);
		assertNull(res);
		
		res = incaReportManager
			.countSamplesExtManager(d1, d2, month, banks, null);
		assertNull(res);
		
		banks.clear();
		res = incaReportManager
			.countSamplesExtManager(d1, d2, month, banks, etabs);
		assertNull(res);
		
		banks.add(b1);
		etabs.clear();
		res = incaReportManager
			.countSamplesExtManager(d1, d2, month, banks, etabs);
		assertNull(res);
	}
	
	@Test
	public void testCountEchansByCessTypesManager() throws ParseException {
		Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
		Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse("17/11/2009"); 
		List<Banque> banks = new ArrayList<Banque>();
		Banque b1 = banqueDao.findById(1);
		banks.add(b1);
		
		CessionType cSan = cessionTypeDao.findById(1);
		List<Long> res = incaReportManager
						.countEchansByCessTypesManager(cSan, d1, d2, 5, banks);
		assertTrue(res.size() == 4);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 1);
		assertTrue(res.get(3) == 0);
		
			
		CessionType cRech = cessionTypeDao.findById(2);
		res = incaReportManager
						.countEchansByCessTypesManager(cRech, d1, d2, 5, banks);
		assertTrue(res.size() == 4);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
			
		d1 = new SimpleDateFormat("dd/MM/yyyy").parse("19/10/2009");
		res = incaReportManager
					.countEchansByCessTypesManager(cRech, d1, d2, 10, banks);
		assertTrue(res.size() == 3);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 3);
		assertTrue(res.get(2) == 0);
			
		CessionType cDest = cessionTypeDao.findById(3);
		res = incaReportManager
				.countEchansByCessTypesManager(cDest, d1, d2, 10, banks);
		assertTrue(res.size() == 3);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		
		res = incaReportManager
			.countEchansByCessTypesManager(cDest, d1, d2, 10, null);
		assertNull(res);
		
		banks.clear();
		res = incaReportManager
			.countEchansByCessTypesManager(cDest, d1, d2, 10, banks);
		assertNull(res);
	}

	@Test
	public void testCountsPrelsAndAssociatesByCimManager() 
													throws ParseException {
		Calendar d1 = Calendar.getInstance();
		d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1999"));
		Calendar d2 = Calendar.getInstance();
		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009")); 
		List<Banque> banks = new ArrayList<Banque>();
		Banque b2 = banqueDao.findById(2);
		Banque b1 = banqueDao.findById(1);
		
		//List<TableCodage> tables = new ArrayList<TableCodage>();
		List<ConsentType> cTypes = new ArrayList<ConsentType>();
		List<Nature> sainTypes = new ArrayList<Nature>();
		List<EchantillonType> sainEchanTypes = new ArrayList<EchantillonType>();
		List<Nature> sangTypes = new ArrayList<Nature>();
		List<EchantillonType> sangEchanTypes = new ArrayList<EchantillonType>();
		
		CimMaster c = cimMasterDao.findByCodeLike("C02.9").get(0);
		
		List<Long> res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(c, null, d1, d2, null,
				null, null, null, null, null);
		assertNull(res);
		
		res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(c, null, d1, d2, banks,
				null, null, null, null, null);
		assertNull(res);
		
		banks.add(b2);
		res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(c, null, d1, d2, banks, 
											null, null, null, null, null);
		assertTrue(res.get(0) == 0);
		 banks.add(b1);	
		res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(c, null, d1, d2, banks, 
											null, null, null, null, null);
		assertTrue(res.get(0) == 1);
		assertTrue(res.get(1) == 1);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 0);
		
		// test transcodage avec ADICAP
		res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(null, "BL", d1, d2, 
								banks, null, null, null, null, null);	
		assertTrue(res.get(0) == 1);

		res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(null, "LANGUE", 
													d1, d2, banks, 
													sainTypes, sainEchanTypes, 
													sangTypes, sangEchanTypes, 
													cTypes);
		assertTrue(res.get(0) == 1);
		
		// test Sain associés
		sainTypes.add(natureDao.findById(2));
		banks.add(banqueDao.findById(3));
		res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(c, null,  
											d1, d2, banks,
											sainTypes, sainEchanTypes, 
											sangTypes, sangEchanTypes, 
											cTypes);
		assertTrue(res.get(0) == 1);
		assertTrue(res.get(1) == 1);
		assertTrue(res.get(2) == 100);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 0);
		
		sainTypes.clear();
		sainEchanTypes.add(echantillonTypeDao.findById(2));
		res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(c, null, 
										d1, d2, banks, 
										sainTypes, sainEchanTypes, 
										null, null, null);
		assertTrue(res.get(0) == 1);
		assertTrue(res.get(1) == 1);
		assertTrue(res.get(2) == 0);
		
		sainEchanTypes.add(echantillonTypeDao.findById(1));
		res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(c, null,
										d1, d2, banks,
										sainTypes, sainEchanTypes, 
										null, null, null);
		assertTrue(res.get(0) == 1);
		assertTrue(res.get(1) == 1);
		assertTrue(res.get(2) == 100);
		assertTrue(res.get(3) == 0);
		
		sangTypes.add(natureDao.findById(2));
		res = incaReportManager
		.countsPrelsAndAssociatesByCimManager(c, null,
									d1, d2, banks, 
									sainTypes, sainEchanTypes, 
									sangTypes, sangEchanTypes, 
									cTypes);
		assertTrue(res.get(0) == 1);
		assertTrue(res.get(1) == 1);
		assertTrue(res.get(2) == 100);
		assertTrue(res.get(3) == 100);
		
		sangTypes.clear();
		sangEchanTypes.add(echantillonTypeDao.findById(1));
		res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(c, null,
									d1, d2, banks,
									sainTypes, sainEchanTypes, 
									sangTypes, sangEchanTypes, 
									cTypes);
		assertTrue(res.get(0) == 1);
		assertTrue(res.get(1) == 1);
		assertTrue(res.get(2) == 100);
		assertTrue(res.get(3) == 100);
		
		// consentTypes
		cTypes.add(consentTypeDao.findById(1));
		res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(c, null,
				d1, d2, banks, sainTypes, sainEchanTypes, 
								sangTypes, sangEchanTypes, cTypes);
		assertTrue(res.get(4) == 0);
		
		cTypes.add(consentTypeDao.findById(3));
		res = incaReportManager
			.countsPrelsAndAssociatesByCimManager(c, null,
				d1, d2, banks, sainTypes, sainEchanTypes, 
								sangTypes, sangEchanTypes, cTypes);
		assertTrue(res.get(4) == 100);
	}
	
	@Test
	public void testCountPrelevedByDatesManager() throws ParseException {
		Calendar d1 = Calendar.getInstance();
		d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/06/2009"));
		Calendar d2 = Calendar.getInstance();
		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("18/12/2009")); 
		List<Banque> banks = new ArrayList<Banque>();
		Banque b1 = banqueDao.findById(1);
		banks.add(b1);
		Banque b2 = banqueDao.findById(2);
		banks.add(b2);
		
		Integer month = 31;
		
		List<Long> res = incaReportManager
			.countPrelevedByDatesManager(d1, d2, month, banks, false, false);
		
		assertTrue(res.size() == 7);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 2);
		assertTrue(res.get(5) == 0);
		assertTrue(res.get(6) == 0);
		
		res = incaReportManager
			.countPrelevedByDatesManager(d1, d2, month, banks, false, true);
	
		assertTrue(res.size() == 7);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 0);
		assertTrue(res.get(5) == 0);
		assertTrue(res.get(6) == 0);
		
		res = incaReportManager
			.countPrelevedByDatesManager(d1, d2, month, banks, true, false);
	
		assertTrue(res.size() == 7);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 2);
		assertTrue(res.get(5) == 2);
		assertTrue(res.get(6) == 2);
		
		// intervalle tombe juste
		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/12/2009")); 
		res = incaReportManager
			.countPrelevedByDatesManager(d1, d2, month, banks, false, false);
		assertTrue(res.size() == 6);
		
		d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/06/1983"));
		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("18/12/1983")); 
		
		res = incaReportManager
		.countPrelevedByDatesManager(d1, d2, month, banks, true, true);

		assertTrue(res.size() == 7);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 1);
		assertTrue(res.get(3) == 3);
		assertTrue(res.get(4) == 3);
		assertTrue(res.get(5) == 3);
		assertTrue(res.get(6) == 3);
		
		banks.clear();
		res = incaReportManager
			.countPrelevedByDatesManager(d1, d2, month, banks, false, false);
		assertNull(res);
		
		res = incaReportManager
			.countPrelevedByDatesManager(d1, d2, month, null, false, false);
		assertNull(res);
	}
	
	@Test
	public void testCountPrelevedByDatesExtManager() throws ParseException {
		Calendar d1 = Calendar.getInstance();
		d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/06/2009"));
		Calendar d2 = Calendar.getInstance();
		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("18/12/2009"));
		List<Banque> banks = new ArrayList<Banque>();
		Banque b1 = banqueDao.findById(1);
		banks.add(b1);
		Banque b2 = banqueDao.findById(2);
		banks.add(b2);
		
		List<Etablissement> etabs = new ArrayList<Etablissement>();
		Etablissement e4 = etablissementDao.findById(2);
		etabs.add(e4);
		
		Integer month = 31;
		
		List<Long> res = incaReportManager
			.countPrelevedByDatesExtManager(d1, d2, month, banks, etabs, false);
		
		assertTrue(res.size() == 7);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 2);
		assertTrue(res.get(5) == 0);
		assertTrue(res.get(6) == 0);
		
		Etablissement e1 = etablissementDao.findById(1);
		etabs.add(e1);			
		// le nombre d'intervalle tombe juste
		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/12/2009"));
		res = incaReportManager
			.countPrelevedByDatesExtManager(d1, d2, month, banks, etabs, false);
		
		assertTrue(res.size() == 6);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 0);
		assertTrue(res.get(5) == 0);
		
		d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/06/1983"));
		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("18/12/1983"));
		etabs.remove(e1);
		res = incaReportManager
		.countPrelevedByDatesExtManager(d1, d2, month, banks, etabs, true);
	
		assertTrue(res.size() == 7);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 1);
		assertTrue(res.get(3) == 2);
		assertTrue(res.get(4) == 0);
		assertTrue(res.get(5) == 0);
		assertTrue(res.get(6) == 0);
		
		etabs.add(e1);
		res = incaReportManager
		.countPrelevedByDatesExtManager(d1, d2, month, banks, etabs, true);
	
		assertTrue(res.size() == 7);
		assertTrue(res.get(0) == 0);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 0);
		assertTrue(res.get(5) == 0);
		assertTrue(res.get(6) == 0);
				
		res = incaReportManager
			.countPrelevedByDatesExtManager(d1, d2, month, null, etabs, false);
		assertNull(res);
		
		res = incaReportManager
			.countPrelevedByDatesExtManager(d1, d2, month, banks, null, true);
		assertNull(res);
		
		banks.clear();
		res = incaReportManager
			.countPrelevedByDatesExtManager(d1, d2, month, banks, etabs, false);
		assertNull(res);
		
		banks.add(b1);
		etabs.clear();
		res = incaReportManager
			.countPrelevedByDatesExtManager(d1, d2, month, banks, etabs, true);
		assertNull(res);
	}
	
	@Test
	public void testCountEclConsentByDatesManager() throws ParseException {
		Calendar d1 = Calendar.getInstance();
		d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2000"));
		Calendar d2 = Calendar.getInstance();
		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009"));  
		List<Banque> banks = new ArrayList<Banque>();
		Banque b1 = banqueDao.findById(1);
		banks.add(b1);
		Banque b2 = banqueDao.findById(2);
		banks.add(b2);
		
		List<ConsentType> cTypes = new ArrayList<ConsentType>();
		ConsentType rech = consentTypeDao.findById(2);
		cTypes.add(rech);
		cTypes.add(consentTypeDao.findById(1));	
		
		Integer year = 365;
		
		List<Long> res = incaReportManager
			.countEclConsentByDatesManager(cTypes, d1, d2, year, banks);
		
		assertTrue(res.size() == 10);
		assertTrue(res.get(0) == 1);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 0);
		assertTrue(res.get(5) == 0);
		assertTrue(res.get(6) == 0);
		assertTrue(res.get(7) == 0);
		assertTrue(res.get(8) == 0);
		assertTrue(res.get(9) == 0);
		
		
		// intervalle tombe juste
		d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("28/10/2009"));
		
		cTypes.add(consentTypeDao.findById(3));
		res = incaReportManager
				.countEclConsentByDatesManager(cTypes, d1, d2, year, banks);
	
		assertTrue(res.size() == 9);
		assertTrue(res.get(0) == 2);
		assertTrue(res.get(1) == 0);
		assertTrue(res.get(2) == 0);
		assertTrue(res.get(3) == 0);
		assertTrue(res.get(4) == 0);
		assertTrue(res.get(5) == 0);
		assertTrue(res.get(6) == 0);
		assertTrue(res.get(7) == 0);
		assertTrue(res.get(8) == 1);
		
		banks.clear();
		res = incaReportManager
			.countEclConsentByDatesManager(cTypes, d1, d2, year, banks);
		assertNull(res);
		
		res = incaReportManager
			.countEclConsentByDatesManager(cTypes, d1, d2, year, null);
		assertNull(res);
		
		banks.add(b1);
		cTypes.clear();
		res = incaReportManager
			.countEclConsentByDatesManager(cTypes, d1, d2, year, banks);
		assertNull(res);
		
		res = incaReportManager
			.countEclConsentByDatesManager(null, d1, d2, year, banks);
		assertNull(res);
	}
	
}