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
package fr.aphp.tumorotek.interfacage.gilda.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.aphp.tumorotek.dao.qualite.FantomeDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.interfacage.jaxb.gilda.GildaMessage;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.interfacage.PatientSipManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-camel-config.xml", "classpath:applicationContextManagerBase.xml",
   "classpath:applicationContextDaoBase-test-mysql.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class Test
{

   public Test(){
      super();
   }

   @Autowired
   private CamelContext camelContext;

   @Autowired
   private PatientSipManager patientSipManager;
   @Autowired
   private PatientManager patientManager;
   @Autowired
   private OperationManager operationManager;
   @Autowired
   private FantomeDao fantomeDao;
   @Autowired
   private OperationTypeDao operationTypeDao;

   private MockEndpoint mockBeans;
   private MockEndpoint mockHandle;
   private MockEndpoint mockDead;
   private MockEndpoint mockLogger;

   @org.junit.Before
   public void setUp(){
      //    	System.out.println("SET UP");
      //		// creation patient system qui sera synchronizé avec SIP
      //    	Patient sys = new Patient();
      //    	sys.setNip("121212");
      //    	sys.setNom("Ramirez");
      //    	sys.setPrenom("Louisa");
      //    	sys.setSexe("F");
      //    	sys.setPatientEtat("V");
      //    	
      //    	Patient a1 = new Patient();
      //    	a1.setNip("060606");
      //    	a1.setNom("ACTIF_SYS_1");
      //    	a1.setPrenom("John");
      //    	a1.setSexe("M");
      //    	a1.setPatientEtat("V");
      //    	
      //    	Patient a2 = new Patient();
      //    	a2.setNip("070707");
      //    	a2.setNom("ACTIF_SYS_2");
      //    	a2.setPrenom("Johnita");
      //    	a2.setSexe("F");
      //    	a2.setPatientEtat("V");
      //    	
      //    	Patient a3 = new Patient();
      //    	a3.setNip("080808");
      //    	a3.setNom("ACTIF_SYS_3");
      //    	a3.setPrenom("Joss");
      //    	a3.setSexe("F");
      //    	a3.setPatientEtat("V");
      //    	
      //    	try {
      //			sys.setDateNaissance(new SimpleDateFormat("dd/mm/yyyy")
      //													.parse("05/04/1995"));
      //			a1.setDateNaissance(new SimpleDateFormat("dd/mm/yyyy")
      //			.parse("11/10/1991"));
      //			a2.setDateNaissance(new SimpleDateFormat("dd/mm/yyyy")
      //			.parse("11/10/1991"));
      //			a3.setDateNaissance(new SimpleDateFormat("dd/mm/yyyy")
      //			.parse("02/08/1987"));
      //		} catch (ParseException e) {
      //			e.printStackTrace();
      //		}
      //		patientManager
      //			.createOrUpdateObjectManager(sys, null, null, null, 
      //							null, null, null, "creation", null, false);
      //		patientManager
      //			.createOrUpdateObjectManager(a1, null, null, null, 
      //							null, null, null, "creation", null, false);
      //		patientManager
      //		.createOrUpdateObjectManager(a2, null, null, null, 
      //							null, null, null, "creation", null, false);
      //		patientManager
      //		.createOrUpdateObjectManager(a3, null, null, null, 
      //							null, null, null, "creation", null, false);
      //		 // assertTrue(patientManager.findAllObjectsManager().size() == 9);
      //		
      //		PatientSip pSip = new PatientSip();
      //		pSip.setNip("010101");
      //		pSip.setNom("REBILLARD");
      //		pSip.setSexe("F");
      //		pSip.setPrenom("SAMANTHA");
      //		PatientSip pSip2 = new PatientSip();
      //		pSip2.setNip("020202");
      //		pSip2.setNom("REBILLON");
      //		pSip2.setSexe("F");
      //		pSip2.setPrenom("PAMELA");
      //		PatientSip pSip3 = new PatientSip();
      //		pSip3.setNip("090909");
      //		pSip3.setNom("IN_TK_SYS");
      //		pSip3.setSexe("F");
      //		pSip3.setPrenom("Jessy");
      //		try {
      //			pSip.setDateNaissance(new SimpleDateFormat("dd/mm/yyyy")
      //													.parse("06/06/1965"));
      //			pSip2.setDateNaissance(new SimpleDateFormat("dd/mm/yyyy")
      //			.parse("06/06/1965"));
      //			pSip3.setDateNaissance(new SimpleDateFormat("dd/mm/yyyy")
      //			.parse("06/09/1969"));
      //		} catch (ParseException e) {
      //			e.printStackTrace();
      //		}
      //		patientSipManager.createOrUpdatePatientInTempTableManager(pSip, 4);
      //		patientSipManager.createOrUpdatePatientInTempTableManager(pSip2, 5);
      //		patientSipManager.createOrUpdatePatientInTempTableManager(pSip3, 6);
      // assertTrue(patientSipManager.findAllObjectsManager().size() == 6);

      mockHandle = (MockEndpoint) camelContext.getEndpoint("mock:direct:gilda-handle");
      mockBeans = (MockEndpoint) camelContext.getEndpoint("mock:bean:patientHandler");
      mockDead = (MockEndpoint) camelContext.getEndpoint("mock:deads");
      mockLogger = (MockEndpoint) camelContext.getEndpoint("mock:bean:exceptionLogProcessor");

      mockBeans.setAssertPeriod(20000);
      //mockHandle.setAssertPeriod(30000);
      //mockHandle.setResultWaitTime(10000);
      mockLogger.setAssertPeriod(10000);
      mockDead.setAssertPeriod(10000);
   }

   @org.junit.Test
   @DirtiesContext
   public void testAlors() throws Exception{

      final GildaMessage mes1 = new GildaMessage(); //newPatByModif.dat
      mes1.getEntete().setNumEvt(new Long(201119235655L));
      final GildaMessage mes2 = new GildaMessage(); //newPat.dat
      mes2.getEntete().setNumEvt(new Long(201119236618L));
      final GildaMessage mes3 = new GildaMessage(); //jaxb_error.dat
      mes3.getEntete().setNumEvt(new Long(201119999L));
      final GildaMessage mes4 = new GildaMessage(); // systemPat.dat
      mes4.getEntete().setNumEvt(new Long(1666));
      final GildaMessage mes5 = new GildaMessage(); // systemPatException.dat
      mes5.getEntete().setNumEvt(new Long(1213));
      final GildaMessage mes6 = new GildaMessage(); //newPatByModif2.dat
      mes6.getEntete().setNumEvt(new Long(201119235658L));
      final GildaMessage mes7 = new GildaMessage(); //fusion1.dat
      mes7.getEntete().setNumEvt(new Long(1231234));
      final GildaMessage mes8 = new GildaMessage(); //fusion2.dat
      mes8.getEntete().setNumEvt(new Long(12312345));
      final GildaMessage mes9 = new GildaMessage(); //fusion3.dat
      mes9.getEntete().setNumEvt(new Long(2468));
      final GildaMessage mes10 = new GildaMessage(); //fusion4.dat
      mes10.getEntete().setNumEvt(new Long(101));
      mockBeans.expectedBodiesReceivedInAnyOrder(mes1, mes2, mes4, mes5, mes6, mes7, mes8, mes9, mes10);
      mockHandle.expectedBodiesReceivedInAnyOrder(mes1, mes2, mes4, mes5, mes6, mes7, mes8, mes9, mes10);
      mockLogger.expectedMessageCount(2);
      mockLogger.expectedBodiesReceivedInAnyOrder(mes5, mes3);
      mockDead.expectedMessageCount(2);
      mockDead.expectedBodiesReceivedInAnyOrder(mes5, mes3);

      // teste le contenu convertBody
      final List<Exchange> list = mockHandle.getReceivedExchanges();
      for(final Exchange exchange : list){
         final Message in = exchange.getIn();
         final GildaMessage.Entete entete = ((GildaMessage) in.getBody()).getEntete();
         final GildaMessage.Id id = ((GildaMessage) in.getBody()).getId();
         if(entete.getCodeEvt().equals("CID")){
            assertTrue(entete.getDateMes().equals("14032011134907"));
            assertTrue(entete.getTypeMes().equals(01));

            assertTrue(id.getNip().equals("2911013055"));
            assertTrue(id.getNom().equals("OZTOPRAK"));
            assertNull(id.getNomPatro());
            assertTrue(id.getPrenom().equals("THADIGEH"));
            assertTrue(id.getDateNaissance().equals(new SimpleDateFormat("dd/MM/yyyy").parse("05/04/1958")));
            assertTrue(id.getSexe().equals("F"));
            assertTrue(id.getEtatPatient().equals("N"));
         }
      }

      Thread.sleep(10000);

      //mockLogger.assertIsSatisfied();
      //mockDead.assertIsSatisfied();
      //mockHandle.assertIsSatisfied();
      //mockBeans.assertIsSatisfied();
   }

   @After
   public void tearDown(){
      System.out.println("TEAR DOWN");

      //asserts
      // newPat.dat CID
      //    	assertFalse(patientSipManager
      //    			.findByNipLikeManager("2911013055", true).isEmpty());
      //    	
      //    	// newPatByModif MID
      //    	assertTrue(patientSipManager
      //    			.findByNipLikeManager("2911011013", true).size() == 1);
      //    	
      //    	// newPatByModif2 MID
      //    	assertTrue(patientSipManager
      //    			.findByNipLikeManager("2911011013", true).get(0).getNom()
      //    			.matches("(CORNELL)|(CHEN KEN)"));
      //    	
      //    	// systemPat
      //    	assertTrue(patientManager
      //    			.findByNipLikeManager("121212", true).get(0).getNom()
      //    			.equals("GAGA"));
      //    	assertTrue(operationManager.findByObjectManager(patientManager
      //    			.findByNipLikeManager("121212", true).get(0)).size() == 2);
      //    	
      //    	// fusion1.dat fusion dans le sip
      //    	assertTrue(patientSipManager
      //    			.findByNipLikeManager("010101", true).isEmpty());
      //    	assertFalse(patientSipManager
      //    			.findByNipLikeManager("020202", true).isEmpty());
      //    	assertTrue(patientSipManager
      //    			.findByNipLikeManager("020202", true).get(0)
      //    			.getPrenom().equals("PAMELA"));
      //    	
      //    	// fusion2.dat creation à partir de la fusion
      //    	assertTrue(patientSipManager
      //    			.findByNipLikeManager("030303", true).isEmpty());
      //    	assertFalse(patientSipManager
      //    			.findByNipLikeManager("040404", true).isEmpty());
      //    	
      //    	// fusion3.dat fusion dans TK
      //    	assertTrue(patientManager
      //    			.findByNipLikeManager("060606", true).isEmpty());
      //    	assertTrue(patientManager
      //    			.findByNipLikeManager("070707", true).size() == 1);
      //    	Patient p070707 = patientManager
      //    					.findByNipLikeManager("070707", true).get(0);
      //    	assertTrue(p070707.getNom().equals("ACTIF_SYS_2"));
      //    	assertTrue(operationManager.findByObjectManager(p070707).size() == 2);
      //    	assertFalse(operationManager
      //    			.findByObjetIdEntiteAndOpeTypeManager(p070707, 
      //    				operationTypeDao.findByNom("Fusion").get(0)).isEmpty());
      //    	
      //    	// fusion4.dat fusion sip avec patient TK
      //    	assertTrue(patientSipManager
      //    			.findByNipLikeManager("090909", true).isEmpty());
      //    	assertTrue(patientManager
      //    			.findByNipLikeManager("080808", true).isEmpty());
      //    	assertTrue(patientManager
      //    			.findByNipLikeManager("090909", true).size() == 1);
      //    	Patient p090909 = patientManager
      //    					.findByNipLikeManager("090909", true).get(0);
      //    	assertTrue(p090909.getNom().equals("IN_TK_SYS"));
      //    	assertTrue(p090909.getPrenom().equals("JESSY"));
      //    	try {
      //    		assertTrue(p090909.getDateNaissance()
      //    			.equals(new SimpleDateFormat("dd/MM/yyyy").parse("06/07/1965")));
      //    	} catch (Exception e) {
      //    		e.printStackTrace();
      //    	}
      //    	assertTrue(operationManager.findByObjectManager(p090909).size() == 2);
      //    	assertFalse(operationManager
      //    			.findByObjetIdEntiteAndOpeTypeManager(p090909, 
      //    				operationTypeDao.findByNom("Synchronisation").get(0)).isEmpty());
      //    	
      //    	// clean up  	
      //    	Patient p = patientManager.findByNipLikeManager("121212", true).get(0);
      //    	patientManager.removeObjectManager(p, null, null);    	
      //    	patientManager.removeObjectManager(p070707, null, null);
      //    	patientManager.removeObjectManager(p090909, null, null);
      //
      //		operationManager.removeObjectManager(operationManager
      //					.findByObjectManager(fantomeDao
      //						.findByNom(p.getPhantomData()).get(0)).get(0));
      ////		operationManager.removeObjectManager(operationManager
      ////				.findByObjectManager(fantomeDao
      ////					.findByNom("ACTIF_SYS_2 Johnita").get(0)).get(0));
      //		operationManager.removeObjectManager(operationManager
      //				.findByObjectManager(fantomeDao
      //					.findByNom(p070707.getPhantomData()).get(0)).get(0));
      //		operationManager.removeObjectManager(operationManager
      //				.findByObjectManager(fantomeDao
      //					.findByNom("ACTIF_SYS_1 John").get(0)).get(0));
      //		operationManager.removeObjectManager(operationManager
      //				.findByObjectManager(fantomeDao
      //					.findByNom(p090909.getPhantomData()).get(0)).get(0));
      //		assertTrue(fantomeDao.findAll().size() == 5);	
      //		assertTrue(operationManager.findAllObjectsManager().size() == 19);
      //		assertTrue(patientManager.findAllObjectsManager().size() == 5);
      //		
      //		 // clean up
      //		List<PatientSip> sips = patientSipManager.findAllObjectsManager();
      //		//assertTrue(sips.size() == 8);
      //		for (int k = 0; k < sips.size(); k++) {
      //			if (sips.get(k).getPatientSipId() > 3) {
      //				patientSipManager.removeObjectManager(sips.get(k));
      //			}
      //		}
      //		
      //		assertTrue(patientSipManager.findAllObjectsManager().size() == 3);	
   }

}
