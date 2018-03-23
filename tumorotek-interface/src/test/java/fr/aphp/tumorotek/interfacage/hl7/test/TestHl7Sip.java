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
package fr.aphp.tumorotek.interfacage.hl7.test;

import static org.junit.Assert.assertTrue;

import org.apache.camel.CamelContext;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

// TODO : Test à corriger pour être lancé avec maven test - JDI
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-camel-config.xml", "classpath:applicationContextManagerBase.xml",
   "classpath:applicationContextDaoBase-test-mysql.xml"})
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class TestHl7Sip
{

   public TestHl7Sip(){
      super();
   }

   @Autowired
   private CamelContext camelContext;
   private MockEndpoint mockBeans;
   private MockEndpoint mockHandle;
   private MockEndpoint mockHandle2;
   private MockEndpoint mockDead;
   private MockEndpoint mockLogger;

   @Before
   public void setUp(){

      new SimpleNamingContextBuilder();

      mockHandle = (MockEndpoint) camelContext.getEndpoint("mock:direct:gilda-handle");
      mockHandle2 = (MockEndpoint) camelContext.getEndpoint("mock:direct:sip-handle");
      mockBeans = (MockEndpoint) camelContext.getEndpoint("mock:bean:patientHandler");
      mockDead = (MockEndpoint) camelContext.getEndpoint("mock:deads");
      mockLogger = (MockEndpoint) camelContext.getEndpoint("mock:bean:exceptionLogProcessor");

      //mockHandle2.setResultWaitTime(30000);
      //mockBeans.setAssertPeriod(30000);
      //mockHandle2.setAssertPeriod(30000);
      //mockHandle2.setResultWaitTime(30000);
      //mockLogger.setAssertPeriod(10000);
      //mockDead.setAssertPeriod(10000);
   }

   // TODO : Test à corriger pour être lancé avec maven test - JDI
   //@Test
   @DirtiesContext
   public void testAlors() throws Exception{

      mockHandle.expectedMessageCount(0);
      mockHandle2.expectedMessageCount(93);
      mockBeans.expectedMessageCount(78);
      mockLogger.expectedMessageCount(0);

      Thread.sleep(15000);

      mockHandle.assertIsSatisfied();
      mockHandle2.assertIsSatisfied();
      mockBeans.assertIsSatisfied();
      mockLogger.assertIsSatisfied();

      assertTrue(true);
   }
}
