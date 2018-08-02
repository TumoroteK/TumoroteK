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
package fr.aphp.tumorotek.interfacage.ack.test;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.aphp.tumorotek.interfacage.sender.SenderFactory;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.interfacage.Logiciel;
import fr.aphp.tumorotek.model.interfacage.Recepteur;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-camel-config.xml", "classpath:applicationContextManagerBase.xml",
   "classpath:applicationContextDaoBase-test-mysql.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class AckTest
{

   @Autowired
   private CamelContext camelContext;

   @Autowired
   private SenderFactory senderFactory;

   @Autowired
   private PrelevementManager prelevementManager;

   private MockEndpoint mockDirect;
   private MockEndpoint mockDmeEp;
   private MockEndpoint mockDead;
   private MockEndpoint mockLogger;

   private MockEndpoint mockDirect2;
   private MockEndpoint mockSglEp;

   @org.junit.Before
   public void setUp(){

      mockDirect = (MockEndpoint) camelContext.getEndpoint("mock:direct:ack-dme");
      mockDmeEp = (MockEndpoint) camelContext.getEndpoint("mock:file:/home/mathieu/Desktop/DME");

      mockDirect2 = (MockEndpoint) camelContext.getEndpoint("mock:direct:ack-sgl");
      mockSglEp = (MockEndpoint) camelContext.getEndpoint("mock:file:/home/mathieu/Desktop/URD");

      mockDead = (MockEndpoint) camelContext.getEndpoint("mock:ack-dme.deadLetter");
      mockLogger = (MockEndpoint) camelContext.getEndpoint("mock:bean:exceptionLogProcessor");
   }

   // TODO : Test à corriger pour être lancé avec maven test - JDI
   //@org.junit.Test
   @DirtiesContext
   public void testDmeRoute() throws Exception{

      final Recepteur dmeR = new Recepteur();
      dmeR.setRecepteurId(2);
      dmeR.setIdentification("DME");
      final Logiciel hm = new Logiciel();
      hm.setNom("HOPITAL MANAGER");
      dmeR.setLogiciel(hm);

      senderFactory.sendMessage(dmeR, prelevementManager.findByIdManager(1), null, null);
      senderFactory.sendMessage(dmeR, prelevementManager.findByIdManager(2), null, null);
      senderFactory.sendMessage(dmeR, null, null, null);
      senderFactory.sendMessage(null, prelevementManager.findByIdManager(3), null, null);

      dmeR.setRecepteurId(null);
      senderFactory.sendMessage(dmeR, prelevementManager.findByIdManager(3), null, null);

      mockDirect.expectedMessageCount(4);
      mockDmeEp.expectedMessageCount(4);
      mockLogger.expectedMessageCount(0);
      mockDead.expectedMessageCount(0);

      Thread.sleep(100);

      mockDirect.assertIsSatisfied();
      mockDmeEp.assertIsSatisfied();
      mockLogger.assertIsSatisfied();
      mockLogger.assertIsSatisfied();

      assertTrue(true);

   }

   @org.junit.Test
   @DirtiesContext
   public void testSglRoute() throws Exception{

      final Recepteur dmeR = new Recepteur();
      dmeR.setIdentification("URD-ACK");
      final Logiciel hm = new Logiciel();
      hm.setNom("DIAMIC");
      dmeR.setLogiciel(hm);

      senderFactory.sendMessage(dmeR, prelevementManager.findByIdManager(1), "H12345", "http://test@camerl.org");
      senderFactory.sendMessage(dmeR, prelevementManager.findByIdManager(2), "H12345", "http://test@camerl.org");

      senderFactory.sendMessage(dmeR, null, "H12345", "http://test@camerl.org");

      senderFactory.sendMessage(null, prelevementManager.findByIdManager(3), "H12345", "http://test@camerl.org");

      dmeR.setLogiciel(null);
      senderFactory.sendMessage(dmeR, prelevementManager.findByIdManager(3), "H12345", "http://test@camerl.org");

      mockDirect2.expectedMessageCount(4);
      mockSglEp.expectedMessageCount(4);
      mockLogger.expectedMessageCount(0);
      mockDead.expectedMessageCount(9);

      Thread.sleep(500);

      mockDirect2.assertIsSatisfied();
      mockSglEp.assertIsSatisfied();
      mockLogger.assertIsSatisfied();
      mockLogger.assertIsSatisfied();

      assertTrue(true);

   }

   // TODO : Test à corriger pour être lancé avec maven test - JDI
   //@org.junit.Test
   @DirtiesContext
   public void testDmeRouteForListObjs() throws Exception{

      final Recepteur dmeR = new Recepteur();
      dmeR.setRecepteurId(2);
      dmeR.setIdentification("DME");
      final Logiciel hm = new Logiciel();
      hm.setNom("HOPITAL MANAGER");
      dmeR.setLogiciel(hm);

      final List<TKAnnotableObject> objs = new ArrayList<>();
      senderFactory.sendMessages(dmeR, objs, 2); // 0 messages

      objs.addAll(prelevementManager.findAllObjectsManager());

      senderFactory.sendMessages(dmeR, objs, 2); // 6 messages
      senderFactory.sendMessages(dmeR, null, 100); // 0 messages
      senderFactory.sendMessages(dmeR, objs, null); // 2 messages
      senderFactory.sendMessages(dmeR, objs, 0); // 2 messages
      senderFactory.sendMessages(null, objs, 5); // 0 messages

      dmeR.setRecepteurId(null);
      senderFactory.sendMessages(dmeR, objs, 5); // 0 messages

      mockDirect.expectedMessageCount(10);
      mockDmeEp.expectedMessageCount(10);
      mockLogger.expectedMessageCount(0);
      mockDead.expectedMessageCount(0);

      Thread.sleep(100);

      mockDirect.assertIsSatisfied();
      mockDmeEp.assertIsSatisfied();
      mockLogger.assertIsSatisfied();
      mockLogger.assertIsSatisfied();

      assertTrue(true);

   }

   // TODO : Test à corriger pour être lancé avec maven test - JDI
   //@org.junit.Test
   @DirtiesContext
   public void testProduceAllForRennes() throws Exception{

      // ce test vise à produire artificiellement les messages HM DME
      // pour tous les prélèvements de la base

      final Recepteur dmeR = new Recepteur();
      dmeR.setRecepteurId(2);
      dmeR.setIdentification("DME");
      final Logiciel hm = new Logiciel();
      hm.setNom("HOPITAL MANAGER");
      dmeR.setLogiciel(hm);

      final List<TKAnnotableObject> prels = new ArrayList<>();
      prels.addAll(prelevementManager.findAllObjectsManager());

      senderFactory.sendMessages(dmeR, prels, 100);

      mockDirect.expectedMessageCount(284);
      mockDmeEp.expectedMessageCount(284);
      mockLogger.expectedMessageCount(0);
      mockDead.expectedMessageCount(0);

      Thread.sleep(100);

      mockDirect.assertIsSatisfied();
      mockDmeEp.assertIsSatisfied();
      mockLogger.assertIsSatisfied();
      mockLogger.assertIsSatisfied();

      assertTrue(true);

   }

   // TODO : Test à corriger pour être lancé avec maven test - JDI
   //@org.junit.Test
   @DirtiesContext
   public void createUDMAckBatchFromCSV() throws Exception{

      final Recepteur dmeR = new Recepteur();
      dmeR.setIdentification("URD-ACK");
      final Logiciel hm = new Logiciel();
      hm.setNom("DIAMIC");
      dmeR.setLogiciel(hm);

      String line = "";
      final String cvsSplitBy = "\t";
      final File csvFile = new File(
         "/home/mathieu/Documents/tumorotek/documents/migrationv2_diags/migrationsHCL/tk2diamic_rattrapage01a062017.csv");
      final String base_url = "http://di57su:8080/tumo2/ext/prelevement?id=&prelId&";

      BufferedReader br = null;
      final Prelevement p = new Prelevement();
      String url;
      int i = 0;
      try{
         br = new BufferedReader(new FileReader(csvFile));

         while((line = br.readLine()) != null){

            // use comma as separator
            final String[] values = line.split(cvsSplitBy);

            p.setPrelevementId(Integer.parseInt(values[1]));

            url = base_url.replace("&prelId&", values[1]);

            senderFactory.sendMessage(dmeR, p, values[0], url);
            i++;
         }

      }catch(final IOException e){
         e.printStackTrace();
      }finally{
         if(br != null){
            br.close();
         }
      }

      mockDirect2.expectedMessageCount(i * 2);
      mockSglEp.expectedMessageCount(i * 2);
      mockLogger.expectedMessageCount(0);
      mockDead.expectedMessageCount(0);

      Thread.sleep(500);

      mockDirect2.assertIsSatisfied();
      mockSglEp.assertIsSatisfied();
      mockLogger.assertIsSatisfied();
      mockLogger.assertIsSatisfied();

      assertTrue(true);

   }
}
