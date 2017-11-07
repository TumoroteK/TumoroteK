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
package fr.aphp.tumorotek.interfacage.scan;

import fr.aphp.tumorotek.dao.interfacage.scan.ScanDeviceDao;
import fr.aphp.tumorotek.manager.interfacage.scan.ScanTerminaleManager;
import fr.aphp.tumorotek.model.interfacage.scan.ScanDevice;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTube;
import org.apache.camel.CamelContext;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-camel-config.xml", 
						"classpath:applicationContextManagerBase.xml",
						"classpath:applicationContextDaoBase.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class ExcelVMateParserTest {
	
    @Autowired
    private CamelContext camelContext;
	
	@Autowired
	private ExcelVMateParser excelVMateParser;
	
	@Autowired
	private ScanTerminaleManager scanTerminaleManager;
	
	@Autowired
	private ScanDeviceDao scanDeviceDao;
	
	@Test
	public void testParse() throws JAXBException, ParseException, FileNotFoundException {
	 
		// HSSFWorkbook xls test
		File file = new File("src/test/java/fr/aphp/tumorotek/interfacage/scan/test5.xls");

		ScanTerminale sT = excelVMateParser.processExcelVMate(new FileInputStream(file));
		
		assertTrue(sT.getName().equals("test5"));
		assertTrue(sT.getDateScan().getTime()
				.equals(new SimpleDateFormat("YYYYMMDD HH:mm:ss").parse("20160401 11:59:28")));
		assertTrue(sT.getWidth() == 12);
		assertTrue(sT.getHeight() == 8);
		assertTrue(sT.getScanTubes().size() == 96);
		assertTrue(sT.getScanTubes().get(29).getPosition() == 30);
		assertTrue(new ArrayList<ScanTube>(sT.getScanTubes()).get(0).getCode() == null);
	}
	
	@Test
	public void testPersist() throws FileNotFoundException, ParseException {
		File file = new File("src/test/java/fr/aphp/tumorotek/interfacage/scan/test5.xls");
		
		int scans = scanTerminaleManager.findAllManager().size();
		
		excelVMateParser.handleVMateScan(new FileInputStream(file));
		
		assertTrue(scanTerminaleManager.findAllManager().size() == scans + 1);
		
		ScanTerminale sT = scanTerminaleManager.findAllManager().get(scans); // last scan
		assertTrue(sT.getName().equals("test5"));
		assertTrue(sT.getDateScan().getTime()
				.equals(new SimpleDateFormat("YYYYMMDD HH:mm:ss").parse("20160401 11:59:28")));
		assertTrue(sT.getWidth() == 12);
		assertTrue(sT.getHeight() == 8);
		assertTrue(sT.getScanTubes().size() == 96);
		assertTrue(new ArrayList<ScanTube>(sT.getScanTubes()).get(0).getCode() == null);
		
		// clean up
		scanTerminaleManager.removeObjectManager(sT);
		
		assertTrue(scanTerminaleManager.findAllManager().size() == scans);		
	}

	// TODO : Test à corriger pour être lancé avec maven test - JDI
	//@Test
	@DirtiesContext 
	public void testRoute() throws Exception {
		
		int tots = scanTerminaleManager.findAllManager().size();
		  
		MockEndpoint mockBeans = (MockEndpoint) camelContext
				.getEndpoint("mock:bean:excelVMateParser");
		MockEndpoint mockDeads = (MockEndpoint) camelContext
				.getEndpoint("mock:deads");
		
		mockBeans.expectedMessageCount(16);
		mockDeads.expectedMessageCount(1);
		
		Thread.sleep(15000);
		
		mockBeans.assertIsSatisfied();
		
		assertTrue(true);
		
		assertTrue(scanTerminaleManager.findAllManager().size() == tots + 15);
		ScanDevice sD = scanDeviceDao.findById(1);
		
		List<ScanTerminale> scans = scanTerminaleManager.findByDeviceManager(sD);
		assertTrue(scans.size() == 15);
		assertTrue(scans.get(0).getName().equals("test5"));
		assertTrue(scans.get(0).getWidth() == 12);
		assertTrue(scans.get(0).getHeight() == 8);
		assertTrue(scans.get(0).getScanTubes().size() == 96);
		assertTrue(scans.get(1).getName().equals("test4"));
		assertTrue(scans.get(1).getWidth() == 12);
		assertTrue(scans.get(1).getHeight() == 8);
		assertTrue(scans.get(1).getScanTubes().size() == 96);
		assertTrue(scans.get(2).getName().equals("tetest3"));
		assertTrue(scans.get(2).getWidth() == 6);
		assertTrue(scans.get(2).getHeight() == 4);
		assertTrue(scans.get(2).getScanTubes().size() == 24);
		assertTrue(scans.get(3).getName().equals("hayem"));
		assertTrue(scans.get(3).getWidth() == 12);
		assertTrue(scans.get(3).getHeight() == 8);
		assertTrue(scans.get(3).getScanTubes().size() == 96);
		assertTrue(scans.get(4).getName().equals("TS00004113"));
		assertTrue(scans.get(4).getWidth() == 12);
		assertTrue(scans.get(4).getHeight() == 8);
		assertTrue(scans.get(4).getScanTubes().size() == 96);
		
		// clean up
		for (ScanTerminale scanTerminale : scans) {
			scanTerminaleManager.removeObjectManager(scanTerminale);
		}
		  
		assertTrue(scanTerminaleManager.findAllManager().size() == tots);
	}
	
}
