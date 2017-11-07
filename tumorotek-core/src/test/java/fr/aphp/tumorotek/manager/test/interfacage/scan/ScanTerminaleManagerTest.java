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
package fr.aphp.tumorotek.manager.test.interfacage.scan;

import fr.aphp.tumorotek.dao.interfacage.scan.ScanDeviceDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.manager.exception.interfacage.scan.ScannedTerminaleNotUniqueException;
import fr.aphp.tumorotek.manager.exception.interfacage.scan.ScannedTerminaleOverSizeException;
import fr.aphp.tumorotek.manager.interfacage.scan.ScanTerminaleManager;
import fr.aphp.tumorotek.manager.interfacage.scan.TKScanTerminaleDTO;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTube;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 
 * Classe de test pour le manager ScanTerminaleManager.
 * Classe créée le 04/05/2016.
 * 
 * @author Mathieu BARTHELEMY.
 * @version 2.1.0
 *
 */
public class ScanTerminaleManagerTest extends AbstractManagerTest4 {
	
	@Autowired
	private ScanTerminaleManager scanTerminaleManager;
	
	@Autowired
	private ScanDeviceDao scanDeviceDao;
	
	@Autowired
	private EnceinteManager enceinteManager;
	
	@Autowired
	private ConteneurDao conteneurDao;
	
	@Test
	public void testFindTKObjectCodesManager() {
		
		ScanTerminale sT = new ScanTerminale();
		sT.setName("BT1");
		sT.setHeight(10);
		sT.setWidth(10);
		sT.setDateScan(Calendar.getInstance());
		sT.setScanDevice(scanDeviceDao.findById(1));
		
		ScanTube t1 = new ScanTube();
		t1.setCode("PTRA.1.1");
		t1.setCell("A01");
		t1.setRow("A");
		t1.setCol("1");
		sT.addTube(t1);
		
		
		ScanTube t2 = new ScanTube();
		t2.setCode("PTRA.1.2");
		t2.setCell("A01");
		t2.setRow("A");
		t2.setCol("2");
		sT.addTube(t2);

		ScanTube t3 = new ScanTube();
		t3.setCode("PTRA.2");
		t3.setCell("A03");
		t3.setRow("A");
		t3.setCol("3");
		sT.addTube(t3);
		
		ScanTube emptyT = new ScanTube();
		emptyT.setCell("A04");
		emptyT.setRow("A");
		emptyT.setCol("4");
		sT.addTube(emptyT);
		
		scanTerminaleManager.createObjectManager(sT, scanDeviceDao.findById(1));
		
		List<String> codes = scanTerminaleManager.findTKObjectCodesManager(sT);
		assertTrue(codes.size() == 3);
		assertTrue(codes.contains("PTRA.1.1"));
		assertTrue(codes.contains("PTRA.1.2"));
		assertTrue(codes.contains("PTRA.2"));
		
		scanTerminaleManager.removeObjectManager(sT);
		
		assertTrue(scanTerminaleManager.findAllManager().isEmpty());
	}
	
	@Test
	public void testFindEmplacementsToFillManager() {
		
		List<Conteneur> conts = conteneurDao.findAll();
		
		// null scanTerminale
		TKScanTerminaleDTO scanDTO = scanTerminaleManager
				.compareScanAndTerminaleManager(null, null, conts);
		assertTrue(scanDTO.getEmplacementsToFill().isEmpty());
		assertTrue(scanDTO.getEmplacementsToFree().isEmpty());
		assertTrue(scanDTO.getEmplacementsMismatch().isEmpty());
		assertTrue(scanDTO.getTerminale() == null);
		assertTrue(scanDTO.getScanTerminale() == null);
		
		// ScannedTerminaleNotUniqueException
		boolean catched = false;
		ScanTerminale sT = new ScanTerminale();
		sT.setName("BT1");
		sT.setWidth(10);
		sT.setHeight(10);
		
		// null conteneurs
		scanDTO = scanTerminaleManager
				.compareScanAndTerminaleManager(sT, null, null);
		assertTrue(scanDTO.getEmplacementsToFill().isEmpty());
		assertTrue(scanDTO.getEmplacementsToFree().isEmpty());
		assertTrue(scanDTO.getEmplacementsMismatch().isEmpty());
		assertTrue(scanDTO.getTerminale() == null);
		assertFalse(scanDTO.getScanTerminale() == null);
		
		// si tous les contes -> non unique
		try {
			scanDTO = scanTerminaleManager.compareScanAndTerminaleManager(sT, null, conts);
		} catch (ScannedTerminaleNotUniqueException sce) {
			catched = true;
			assertTrue(sce.getCode().equals("BT1"));
			List<String> adrls = sce.getEnceinteAdrls();
			assertEquals(4, adrls.size());
			assertTrue(adrls.contains("CC1.R1.T1.BT1"));
			assertTrue(adrls.contains("CC1.R1.T4.BT1"));
			assertTrue(adrls.contains("CC1.R2.T1.BT1"));
			assertTrue(adrls.contains("CC1.R2.T6.BT1"));
		}
		assertTrue(catched);
		assertTrue(scanDTO.getEmplacementsToFill().isEmpty());
		assertTrue(scanDTO.getEmplacementsToFree().isEmpty());
		assertTrue(scanDTO.getEmplacementsMismatch().isEmpty());
		assertTrue(scanDTO.getTerminale() == null);
		catched = false;
		
		// scanTube1 -> emplacement existant occupé ok
		// CC1.R1.T1.BT1.1 occupé par le dérivé PTRA.1.1
		ScanTube t1 = new ScanTube();
		t1.setScanTerminale(sT);
		t1.setCode("PTRA.1.1");
		t1.setRow("A");
		t1.setCol("1");
		t1.setCell("A01");
		sT.getScanTubes().add(t1);
		// restrict search to T1 to avoid ScannedTerminaleNotUniqueException
		scanDTO = scanTerminaleManager.compareScanAndTerminaleManager(sT, 
									enceinteManager.findByIdManager(3), conts);
		assertTrue(scanDTO.getEmplacementsToFill().isEmpty());
		assertTrue(scanDTO.getEmplacementsToFree().isEmpty());
		assertTrue(scanDTO.getEmplacementsMismatch().isEmpty());
		assertTrue(scanDTO.getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getScanTerminale().equals(sT));
		
		// scanTube2 -> emplacement existant occupé mais obj different ScannedTubeCodeMisMatchException 
		// CC1.R1.T1.BT1.2 occupé par le dérivé PTRA.1.2
		ScanTube t2 = new ScanTube();
		t2.setScanTerminale(sT);
		t2.setCode("DERIVE1");
		t2.setRow("A");
		t2.setCol("2");
		t2.setCell("A02");
		sT.getScanTubes().add(t2);
		// restrict search to T1 to avoid ScannedTerminaleNotUniqueException
		scanDTO = scanTerminaleManager
			.compareScanAndTerminaleManager(sT, enceinteManager.findByIdManager(3), conts);
		assertTrue(scanDTO.getEmplacementsToFill().isEmpty());
		assertTrue(scanDTO.getEmplacementsToFree().isEmpty());
		assertTrue(scanDTO.getEmplacementsMismatch().size() == 1);
		assertTrue(scanDTO.getEmplacementsMismatch().get(t2).getCode().equals("PTRA.1.2"));
		assertTrue(scanDTO.getEmplacementsMismatch().get(t2).listableObjectId() == 2);
		
		t2.setCode("PTRA.1.2");
		// scanTube -> emplacement existant mais vide ok
		// CC1.R1.T1.BT1.11 vide emplacement_id = 5
		ScanTube t3 = new ScanTube();
		t3.setScanTerminale(sT);
		t3.setCode("NEWTUBE1");
		t3.setRow("B");
		t3.setCol("1");
		t3.setCell("B01");
		sT.getScanTubes().add(t3);
		// restrict search to T1 to avoid ScannedTerminaleNotUniqueException
		scanDTO = scanTerminaleManager.compareScanAndTerminaleManager(sT, 
									enceinteManager.findByIdManager(3), conts);
		assertTrue(scanDTO.getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().size() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getEmplacementId() == 5);
		assertTrue(scanDTO.getEmplacementsToFree().isEmpty());
		assertTrue(scanDTO.getEmplacementsMismatch().isEmpty());
	
		// scantube -> emplacement à créer mais position overSize -> ScannedTerminaleOverSizeException
		// CC1.R1.T1.BT1.123
		ScanTube t4 = new ScanTube();
		t4.setScanTerminale(sT);
		t4.setCode("OVERPLACED");
		t4.setRow("M");
		t4.setCol("3");
		t4.setCell("M03");
		sT.getScanTubes().add(t4);
		assertTrue(t4.getPosition() == 123);
		try {
			// restrict search to T1 to avoid ScannedTerminaleNotUniqueException
			scanDTO = scanTerminaleManager
					.compareScanAndTerminaleManager(sT, enceinteManager.findByIdManager(3), conts);
		} catch (ScannedTerminaleOverSizeException stoe) {
			catched = true;
			assertTrue(stoe.getScanTube().equals(t4));
			assertTrue(stoe.getTerminale().getTerminaleId() == 1);
			assertTrue(stoe.getTerminale().getTerminaleType().getNbPlaces() == 100);
		}
		assertTrue(catched);
		catched = false;
		
		// scantube -> emplacement à créer
		// CC1.R1.T1.BT1.66
		t4.setRow("G");
		t4.setCol("6");
		t4.setCell("G06");
		assertTrue(t4.getPosition() == 66);
		// restrict search to T1 to avoid ScannedTerminaleNotUniqueException
		scanDTO = scanTerminaleManager.compareScanAndTerminaleManager(sT, 
									enceinteManager.findByIdManager(3), conts);
		assertTrue(scanDTO.getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().size() == 2);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getEmplacementId() == 5);
		assertTrue(scanDTO.getEmplacementsToFree().isEmpty());
		assertTrue(scanDTO.getEmplacementsMismatch().isEmpty());
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getEmplacementId() == 5);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().get(t4).getEmplacementId() == null);
		assertTrue(scanDTO.getEmplacementsToFill().get(t4).getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().get(t4).getPosition() == 66);
	}
	
	@Test
	public void testFindEmplacementsToFreeManager() {

		List<Conteneur> conts = conteneurDao.findAll();

		// null scanTerminale
		TKScanTerminaleDTO scanDTO = 
				scanTerminaleManager.compareScanAndTerminaleManager(null, null, conts);
		assertTrue(scanDTO.getEmplacementsToFill().isEmpty());
		assertTrue(scanDTO.getEmplacementsToFree().isEmpty());
		assertTrue(scanDTO.getEmplacementsMismatch().isEmpty());
		
		boolean catched = false;
		ScanTerminale sT = new ScanTerminale();
		sT.setName("BT1");
		sT.setWidth(10);
		sT.setHeight(10);
		// scanTube -> emplacement existant occupé à vider
		// CC1.R1.T1.BT1.1 à vider
		ScanTube t1 = new ScanTube();
		t1.setScanTerminale(sT);
		t1.setRow("A");
		t1.setCol("1");
		t1.setCell("A01");
		sT.getScanTubes().add(t1);
		// restrict search to T1 to avoid ScannedTerminaleNotUniqueException
		scanDTO = scanTerminaleManager.compareScanAndTerminaleManager(sT, 
									enceinteManager.findByIdManager(3), conts);
		assertTrue(scanDTO.getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().isEmpty());
		assertTrue(scanDTO.getEmplacementsMismatch().isEmpty());
		assertTrue(scanDTO.getEmplacementsToFree().size() == 1);
		assertTrue(scanDTO.getEmplacementsToFree().get(t1).getCode().equals("PTRA.1.1"));
		assertTrue(scanDTO.getEmplacementsToFree().get(t1).listableObjectId() == 1);
		
		// scanTube2 -> emplacement existant vide = rien a faire
		// CC1.R1.T1.BT1.11 vide emplacement_id = 5
		ScanTube t2 = new ScanTube();
		t2.setScanTerminale(sT);
		t2.setRow("B");
		t2.setCol("1");
		t2.setCell("B01");
		sT.getScanTubes().add(t2);
		// restrict search to T1 to avoid ScannedTerminaleNotUniqueException
		scanDTO = scanTerminaleManager.compareScanAndTerminaleManager(sT, 
									enceinteManager.findByIdManager(3), conts);
		assertTrue(scanDTO.getEmplacementsToFill().isEmpty());
		assertTrue(scanDTO.getEmplacementsMismatch().isEmpty());
		assertTrue(scanDTO.getEmplacementsToFree().size() == 1);
		assertTrue(scanDTO.getEmplacementsToFree().get(t1).getCode().equals("PTRA.1.1"));
		assertTrue(scanDTO.getEmplacementsToFree().get(t1).listableObjectId() == 1);

		// scantube -> emplacement à vider mais position overSize -> ScannedTerminaleOverSizeException
		// CC1.R1.T1.BT1.123
		ScanTube t4 = new ScanTube();
		t4.setScanTerminale(sT);
		t4.setRow("M");
		t4.setCol("3");
		t4.setCell("M03");
		sT.getScanTubes().add(t4);
		assertTrue(t4.getPosition() == 123);
		try {
			// restrict search to T1 to avoid ScannedTerminaleNotUniqueException
			scanDTO = scanTerminaleManager
				.compareScanAndTerminaleManager(sT, enceinteManager.findByIdManager(3), conts);
		} catch (ScannedTerminaleOverSizeException stoe) {
			catched = true;
			assertTrue(stoe.getScanTube().equals(t4));
			assertTrue(stoe.getTerminale().getTerminaleId() == 1);
			assertTrue(stoe.getTerminale().getTerminaleType().getNbPlaces() == 100);
		}
		assertTrue(catched);
		catched = false;
		
		sT.getScanTubes().remove(t4);
		// scanTube3 -> emplacement existant à remplir
		// CC1.R1.T1.BT1.11 vide emplacement_id = 5
		ScanTube t3 = new ScanTube();
		t3.setScanTerminale(sT);
		t3.setCode("NEWTUBE1");
		t3.setRow("B");
		t3.setCol("1");
		t3.setCell("B01");
		sT.getScanTubes().add(t3);
		// restrict search to T1 to avoid ScannedTerminaleNotUniqueExceptionm
		scanDTO = scanTerminaleManager.compareScanAndTerminaleManager(sT, 
									enceinteManager.findByIdManager(3), conts);
		assertTrue(scanDTO.getEmplacementsToFill().size() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getEmplacementId() == 5);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getPosition() == 11);
		assertTrue(scanDTO.getEmplacementsMismatch().isEmpty());
		assertTrue(scanDTO.getEmplacementsToFree().size() == 1);
		assertTrue(scanDTO.getEmplacementsToFree().get(t1).getCode().equals("PTRA.1.1"));
		assertTrue(scanDTO.getEmplacementsToFree().get(t1).listableObjectId() == 1);
		
		// scanTube5 -> emplacement vide à remplir
		// CC1.R1.T1.BT1.66 vide 
		ScanTube t5 = new ScanTube();
		t5.setScanTerminale(sT);
		t5.setCode("NEWTUBE2");
		t5.setRow("G");
		t5.setCol("6");
		t5.setCell("G06");
		sT.getScanTubes().add(t5);
		assertTrue(t5.getPosition() == 66);
		// restrict search to T1 to avoid ScannedTerminaleNotUniqueException
		scanDTO = scanTerminaleManager.compareScanAndTerminaleManager(sT, 
									enceinteManager.findByIdManager(3), conts);
		assertTrue(scanDTO.getEmplacementsToFill().size() == 2);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getEmplacementId() == 5);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getPosition() == 11);
		assertTrue(scanDTO.getEmplacementsToFill().get(t5).getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().get(t5).getEmplacementId() == null);
		assertTrue(scanDTO.getEmplacementsToFill().get(t5).getPosition() == 66);
		assertTrue(scanDTO.getEmplacementsMismatch().isEmpty());
		assertTrue(scanDTO.getEmplacementsToFree().size() == 1);
		assertTrue(scanDTO.getEmplacementsToFree().get(t1).getCode().equals("PTRA.1.1"));
		assertTrue(scanDTO.getEmplacementsToFree().get(t1).listableObjectId() == 1);	
		
		// mismatch
		ScanTube t6 = new ScanTube();
		t6.setScanTerminale(sT);
		t6.setCode("DERIVE1");
		t6.setRow("A");
		t6.setCol("2");
		t6.setCell("A02");
		sT.getScanTubes().add(t6);
		// restrict search to T1 to avoid ScannedTerminaleNotUniqueException
		scanDTO = scanTerminaleManager.compareScanAndTerminaleManager(sT, 
									enceinteManager.findByIdManager(3), conts);
		assertTrue(scanDTO.getEmplacementsToFill().size() == 2);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getEmplacementId() == 5);
		assertTrue(scanDTO.getEmplacementsToFill().get(t3).getPosition() == 11);
		assertTrue(scanDTO.getEmplacementsToFill().get(t5).getTerminale().getTerminaleId() == 1);
		assertTrue(scanDTO.getEmplacementsToFill().get(t5).getEmplacementId() == null);
		assertTrue(scanDTO.getEmplacementsToFill().get(t5).getPosition() == 66);;	
		assertTrue(scanDTO.getEmplacementsToFree().size() == 1);
		assertTrue(scanDTO.getEmplacementsToFree().get(t1).getCode().equals("PTRA.1.1"));
		assertTrue(scanDTO.getEmplacementsToFree().get(t1).listableObjectId() == 1);	
		assertTrue(scanDTO.getEmplacementsMismatch().size() == 1);
		assertTrue(scanDTO.getEmplacementsMismatch().get(t6).getCode().equals("PTRA.1.2"));
		assertTrue(scanDTO.getEmplacementsMismatch().get(t6).listableObjectId() == 2);
		
	}
}