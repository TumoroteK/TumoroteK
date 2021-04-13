/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.interfacage.sender.test;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.camel.CamelContext;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.aphp.tumorotek.interfacage.storageRobot.StorageMovement;
import fr.aphp.tumorotek.interfacage.storageRobot.StorageMovementComparator;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.interfacage.sender.SenderFactory;
import fr.aphp.tumorotek.interfacage.sender.StorageRobotSender;
import fr.aphp.tumorotek.model.interfacage.Logiciel;
import fr.aphp.tumorotek.model.interfacage.Recepteur;

/**
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.2.1-IRELEC
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-camel-config.xml", "classpath:applicationContextManagerBase.xml",
"classpath:applicationContextDaoBase-test-mysql.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class SenderTest
{

	@Autowired
	private CamelContext camelContext;

	@Autowired
	private StorageRobotSender storageRobotSender;

	@Autowired
	private SenderFactory senderFactory;

	@Autowired
	private UtilisateurManager utilisateurManager;

	private MockEndpoint mockDirect;
	private MockEndpoint mockEp;
	private MockEndpoint mockDead;
	private MockEndpoint mockLogger;

	private Recepteur robot;

	@Before
	public void setUp(){
		robot = new Recepteur();
		robot.setRecepteurId(2);
		robot.setIdentification("STORAGE-ROBOT-ONE");
		final Logiciel irelec = new Logiciel();
		irelec.setNom("IRELEC");
		robot.setLogiciel(irelec);

		mockDirect = (MockEndpoint) camelContext.getEndpoint("mock:direct:storage-robot-master");
		mockEp = (MockEndpoint) camelContext.getEndpoint("mock:file:/home/mathieu/Desktop/IRELEC");
		mockDead = (MockEndpoint) camelContext.getEndpoint("mock:storage-robot.deadLetter");
		mockLogger = (MockEndpoint) camelContext.getEndpoint("mock:bean:exceptionLogProcessor");
	}

	// TODO : Test à corriger pour être lancé avec maven test. Fonctionne avec JUnit en revanche - JDI
	@Test
	@DirtiesContext
	public void testStorageRobotRoute() throws Exception{

		List<StorageMovement> movs = initMoves();

		Utilisateur u1 = utilisateurManager.findByIdManager(1);

		senderFactory.sendEmplacements(null, movs, u1);
		senderFactory.sendEmplacements(robot, movs, u1);
		senderFactory.sendEmplacements(robot, null, u1);
		senderFactory.sendEmplacements(robot, movs, null);
		boolean caught = false;
		try {
			senderFactory.sendEmplacements(robot, initBadMoves(), u1);
		} catch (RuntimeException re) {
			caught = true;
			assertTrue(re.getMessage().equals("storage.robot.emplacement.adrl.incompatible"));
		}
		assertTrue(caught);

		mockDirect.expectedMessageCount(2);
		mockEp.expectedMessageCount(2);
		mockLogger.expectedMessageCount(0);
		mockDead.expectedMessageCount(0);

		Thread.sleep(100);

		mockDirect.assertIsSatisfied();
		mockEp.assertIsSatisfied();
		mockLogger.assertIsSatisfied();
		mockLogger.assertIsSatisfied();

		assertTrue(true);
	}

	@Test
	public void testStorageMovementSort() {

		StorageMovement st1 = 
				new StorageMovement("E1", "2", "IRE.1.1.1"); // 2 - 1
		StorageMovement st2 = 
				new StorageMovement("E2", "4", "IRE.1.1.2"); // 4 - 2
		StorageMovement st3 =
				new StorageMovement("E3", "5", "IRE.1.2.1"); // 5 - 3
		StorageMovement st4 = 
				new StorageMovement("E4", "6", "IRE.1.3.1"); // 6 - 4
		StorageMovement st5 = 
				new StorageMovement("E5", "7", "IRE.2.1.1"); // 7 - 5
		StorageMovement st6 = 
				new StorageMovement("E6", "1", "IRE.3.1.1"); // 1 - 6
		StorageMovement st7 = 
				new StorageMovement("Aa", "3", "JRE.1.1.1"); // 3 - 7
		StorageMovement st8 = 
				new StorageMovement(null, null, null); // inclassable
		// nulls unclassables
		StorageMovement st9 = 
				new StorageMovement("E8", null, "JRE.1.2.1");  // ? - 8
		StorageMovement st10 = 
				new StorageMovement("E9", "8", null);  // 8 - ?

		List<StorageMovement> sts = new ArrayList<StorageMovement>();
		sts.add(st1); sts.add(st2); sts.add(st3);
		sts.add(st4); sts.add(st5); sts.add(st6); sts.add(st7);
		sts.add(st8); sts.add(st9); sts.add(st10);  

		// sort by adrl
		Collections.sort(sts, new StorageMovementComparator(true));
		assertTrue(sts.get(0).equals(st6));
		assertTrue(sts.get(1).equals(st1));
		assertTrue(sts.get(2).equals(st7));
		assertTrue(sts.get(3).equals(st2));
		assertTrue(sts.get(4).equals(st3));
		assertTrue(sts.get(5).equals(st4));
		assertTrue(sts.get(6).equals(st5));
		assertTrue(sts.get(7).equals(st10));

		// sort by destAdrl
		Collections.sort(sts, new StorageMovementComparator(false));
		assertTrue(sts.get(0).equals(st1));
		assertTrue(sts.get(1).equals(st2));
		assertTrue(sts.get(2).equals(st3));
		assertTrue(sts.get(3).equals(st4));
		assertTrue(sts.get(4).equals(st5));
		assertTrue(sts.get(5).equals(st6));
		assertTrue(sts.get(6).equals(st7));
		assertTrue(sts.get(7).equals(st9));
	}

	@Test
	public void testWriteOneRecetteLine() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Utilisateur u1 = utilisateurManager.findByIdManager(1);

		try {						
			storageRobotSender.writeOneRecetteLine(baos, "test.csv", u1, "|");

			String[] out = baos.toString().split("\\\n");

			assertTrue(out.length == 1);
			assertTrue("test.csv|USER1".equals(out[0]));

			baos.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testMakeCSV() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		List<StorageMovement> movs = initMoves();

		Collections.sort(movs, new StorageMovementComparator(false));
		
		try {						
			storageRobotSender.makeCSVfromMap(robot, baos, movs, "|");

			String[] out = baos.toString().split("\\\n");

			assertTrue(out.length == 3);
			assertTrue("1|ECHAN1|0|0|0|1|1|1|1|10|".equals(out[0]));
			assertTrue("2|ECHAN2|0|0|0|2|1|1|1|11|".equals(out[1]));
			assertTrue("3|ECHAN3|0|0|0|3|1|2|1|1|".equals(out[2]));
			// assertTrue("4|ECHAN4|CC1|1|1|6|||||".equals(out[3]));

			baos.close();

			baos = new ByteArrayOutputStream();
			storageRobotSender.makeCSVfromMap(robot, baos, movs, ";");

			out = baos.toString().split("\\\n");
			assertTrue(out.length == 3);
			assertTrue("1;ECHAN1;0;0;0;1;1;1;1;10;".equals(out[0]));
			assertTrue("2;ECHAN2;0;0;0;2;1;1;1;11;".equals(out[1]));
			assertTrue("3;ECHAN3;0;0;0;3;1;2;1;1;".equals(out[2]));
			// assertTrue("4;ECHAN4;CC1;1;1;6;;;;;".equals(out[3]));

			baos.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private List<StorageMovement> initMoves() {
		List<StorageMovement> movs = new ArrayList<StorageMovement>();
		movs.add(new StorageMovement("ECHAN1", "1", "1.1.1.10"));
		movs.add(new StorageMovement("ECHAN3", "3", "1.2.1.1"));
		// movs.add(new StorageMovement("ECHAN4", "1.1.1.6", null));
		movs.add(new StorageMovement("ECHAN2", "2", "1.1.1.11"));
		return movs;
	}

	private List<StorageMovement> initBadMoves() {
		List<StorageMovement> movs = new ArrayList<StorageMovement>();
		movs.add(new StorageMovement("ECHAN1", "0.0.0.1", "1.X.1.1.10"));
		movs.add(new StorageMovement("ECHAN3", "0.0.0.X.3", "1.2.1.1"));
		return movs;
	}
}