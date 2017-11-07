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

import fr.aphp.tumorotek.interfacage.sender.SenderFactory;
import fr.aphp.tumorotek.interfacage.sender.StorageRobotSender;
import fr.aphp.tumorotek.interfacage.storageRobot.StorageEmplacement;
import fr.aphp.tumorotek.manager.qualite.OperationTypeManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.interfacage.Logiciel;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Emplacement;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-camel-config.xml",
   "classpath:applicationContextManagerBase.xml",
   "classpath:applicationContextDaoBase.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class SenderTest {

   @Autowired
   private CamelContext camelContext;

   @Autowired
   private StorageRobotSender storageRobotSender;

   @Autowired
   private SenderFactory senderFactory;

   @Autowired
   private OperationTypeManager operationTypeManager;

   @Autowired
   private EmplacementManager emplacementManager;

   private MockEndpoint mockDirect;
   private MockEndpoint mockEp;
   private MockEndpoint mockDead;
   private MockEndpoint mockLogger;

   private Recepteur robot;

   @Before
   public void setUp() {
      robot = new Recepteur();
      robot.setRecepteurId(2);
      robot.setIdentification("STORAGE-ROBOT-ONE");
      Logiciel irelec = new Logiciel();
      irelec.setNom("IRELEC");
      robot.setLogiciel(irelec);

      mockDirect = (MockEndpoint) camelContext.getEndpoint("mock:direct:storage-robot");
      mockEp = (MockEndpoint) camelContext.getEndpoint("mock:file:/home/mathieu/Desktop/IRELEC");
      mockDead = (MockEndpoint) camelContext.getEndpoint("mock:storage-robot.deadLetter");
      mockLogger = (MockEndpoint) camelContext.getEndpoint("mock:bean:exceptionLogProcessor");
   }

   // TODO : Test à corriger pour être lancé avec maven test. Fonctionne avec JUnit en revanche - JDI
   //@Test
   @DirtiesContext
   public void testStorageRobotRoute() throws Exception {
      Map<TKStockableObject, Emplacement> tkEmpls = initTKEmpls();

      senderFactory.sendEmplacements(robot, tkEmpls, null);
      senderFactory.sendEmplacements(robot, tkEmpls, operationTypeManager.findByNomLikeManager("Destockage", true).get(0));
      senderFactory.sendEmplacements(null, tkEmpls, null);
      senderFactory.sendEmplacements(null, tkEmpls, null);

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
   public void testStorageEmplacementSort() {

      StorageEmplacement st1 =
         new StorageEmplacement("E1", "B", "B", "B", "2"); // 2
      StorageEmplacement st2 =
         new StorageEmplacement("E2", "B", "B", "C", "2"); // 4
      StorageEmplacement st3 =
         new StorageEmplacement("E3", "B", "B", "C", "3"); // 5
      StorageEmplacement st4 =
         new StorageEmplacement("E4", "B", "C", "C", "3"); // 6
      StorageEmplacement st5 =
         new StorageEmplacement("E5", "C", "A", "C", "3"); // 7
      StorageEmplacement st6 =
         new StorageEmplacement("E6", "A", "D", "C", "1"); // 1
      StorageEmplacement st7 =
         new StorageEmplacement("Aa", "B", "B", "C", "2"); // 3
      // nulls unclassables
      StorageEmplacement st8 =
         new StorageEmplacement(null, "B", "B", "C", "2");
      StorageEmplacement st9 =
         new StorageEmplacement("E8", null, "B", "C", "2");
      StorageEmplacement st10 =
         new StorageEmplacement("Aa", "B", null, "C", "2");
      StorageEmplacement st11 =
         new StorageEmplacement("Aa", "B", "B", null, "2");
      StorageEmplacement st12 =
         new StorageEmplacement("Aa", "B", "B", "C", null);
      // StorageEmplacement st13 = null;

      List<StorageEmplacement> sts = new ArrayList<StorageEmplacement>();
      sts.add(st1);
      sts.add(st2);
      sts.add(st3);
      sts.add(st4);
      sts.add(st5);
      sts.add(st6);
      sts.add(st7);
      sts.add(st8);
      sts.add(st9);
      sts.add(st10);
      sts.add(st11);
      sts.add(st12);

      Collections.sort(sts);
      assertTrue(sts.get(0).equals(st6));
      assertTrue(sts.get(1).equals(st1));
      assertTrue(sts.get(2).equals(st7));
      assertTrue(sts.get(3).equals(st2));
      assertTrue(sts.get(4).equals(st3));
      assertTrue(sts.get(5).equals(st4));
      assertTrue(sts.get(6).equals(st5));
   }

   @Test
   public void testMakeCSV() {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      Map<TKStockableObject, Emplacement> tkEmpls = initTKEmpls();

      OperationType stockage = operationTypeManager.findByNomLikeManager("Stockage", true).get(0);
      OperationType destockage = operationTypeManager.findByNomLikeManager("Destockage", true).get(0);

      try {
         storageRobotSender.makeCSVfromMap(robot, baos, tkEmpls, stockage, "|", 5);

         String[] out = baos.toString().split("\\\n");

         assertTrue(out.length == 5);
         assertTrue("1|ECHAN1|CC1|1|1|10".equals(out[0]));
         assertTrue("2|ECHAN2|CC1|1|1|11".equals(out[1]));
         assertTrue("3|ECHAN3|CC1|2|1|1".equals(out[2]));
         assertTrue("4|||||".equals(out[3]));
         assertTrue("5|||||".equals(out[4]));

         baos.close();

         baos = new ByteArrayOutputStream();
         storageRobotSender.makeCSVfromMap(robot, baos, tkEmpls, destockage, ";", -1);

         out = baos.toString().split("\\\n");
         assertTrue(out.length == 3);
         assertTrue("1;;;;;;ECHAN1;CC1;1;1;10".equals(out[0]));
         assertTrue("2;;;;;;ECHAN2;CC1;1;1;11".equals(out[1]));
         assertTrue("3;;;;;;ECHAN3;CC1;2;1;1".equals(out[2]));

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

   private Map<TKStockableObject, Emplacement> initTKEmpls() {
      Map<TKStockableObject, Emplacement> tkEmpls =
         new HashMap<TKStockableObject, Emplacement>();
      Echantillon e1 = new Echantillon();
      e1.setCode("ECHAN1");
      Emplacement emp4 = emplacementManager.findByIdManager(4);
      tkEmpls.put(e1, emp4);
      Echantillon e3 = new Echantillon();
      e3.setCode("ECHAN3");
      Emplacement emp6 = emplacementManager.findByIdManager(6);
      tkEmpls.put(e3, emp6);
      Echantillon e4 = new Echantillon();
      e4.setCode("ECHAN4");
      tkEmpls.put(e4, null);
      Echantillon e2 = new Echantillon();
      e2.setCode("ECHAN2");
      Emplacement emp5 = emplacementManager.findByIdManager(5);
      tkEmpls.put(e2, emp5);

      return tkEmpls;
   }
}