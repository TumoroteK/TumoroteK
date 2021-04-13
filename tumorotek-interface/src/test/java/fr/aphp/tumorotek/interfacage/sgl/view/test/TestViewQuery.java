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
package fr.aphp.tumorotek.interfacage.sgl.view.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.aphp.tumorotek.interfacage.sgl.view.ViewHandlerFactory;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.Logiciel;

import static org.junit.Assert.assertTrue;

// TODO : Test à corriger pour être lancé avec maven test - JDI
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-camel-config.xml", "classpath:applicationContextManagerBase.xml",
   "classpath:applicationContextDaoBase-test-mysql.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class TestViewQuery
{

   public TestViewQuery(){
      super();
   }

   @Autowired
   private ViewHandlerFactory viewHandlerFactory;

   // TODO : Test à corriger pour être lancé avec maven test - JDI
   //@org.junit.Test
   @DirtiesContext
   public void testViewRoute() throws Exception{
      final Logiciel log = new Logiciel();
      log.setNom("DAVINCI");
      final Emetteur eT = new Emetteur();
      eT.setIdentification("DVC TEST");
      eT.setLogiciel(log);

      //		Class.forName("com.mysql.cj.jdbc.Driver");

      //		boolean catched = false;
      //		try {
      //			viewHandlerFactory.sendQuery( eT, "DOS1");
      //		} catch (RuntimeException re) {
      //			assertTrue(re.getMessage().equals("view.jdbc.properties.not.found"));
      //			catched = true;
      //		}
      //		assertTrue(catched);

      // DOS1 tous champs presents 
      DossierExterne dExt = viewHandlerFactory.sendQuery(eT, "DOS1", null);

      int allBlockHere = 0;
      assertTrue(dExt != null);
      assertTrue(dExt.getIdentificationDossier().equals("DOS1"));
      assertTrue(dExt.getBlocExternes().size() == 4);
      for(final BlocExterne bExt : dExt.getBlocExternes()){
         if(bExt.getEntiteId() == 1){ // patient
            assertTrue(bExt.getOrdre() == 1);
            assertTrue(bExt.getValeurs().size() == 6);
            assertTrue(bExt.getValeurs().get(0).getValeur().equals("1234567")); // NIP
            assertTrue(bExt.getValeurs().get(1).getValeur().equals("ROLAND")); // NOM
            assertTrue(bExt.getValeurs().get(2).getValeur().equals("DIAZ")); // NOM_NAIS
            assertTrue(bExt.getValeurs().get(3).getValeur().equals("CAMERON")); // PRENOM
            assertTrue(bExt.getValeurs().get(4).getValeur().equals("19560514")); // DATE_NAIS
            assertTrue(bExt.getValeurs().get(5).getValeur().equals("M")); // SEXE
            allBlockHere++;
         }
         if(bExt.getEntiteId() == 7){ // maladie
            assertTrue(bExt.getOrdre() == 2);
            assertTrue(bExt.getValeurs().size() == 1);
            assertTrue(bExt.getValeurs().get(0).getValeur().equals("BL;BZ")); // CIM
            allBlockHere++;
         }
         if(bExt.getEntiteId() == 2){ // prelevement
            assertTrue(bExt.getOrdre() == 3);
            assertTrue(bExt.getValeurs().size() == 2);
            assertTrue(bExt.getValeurs().get(0).getValeur().equals("NDA1234")); // NDA
            assertTrue(bExt.getValeurs().get(1).getValeur().equals("20160725141201")); // DATE_PREL
            allBlockHere++;
         }
         if(bExt.getEntiteId() == 3){ // echantillon
            assertTrue(bExt.getOrdre() == 4);
            assertTrue(bExt.getValeurs().size() == 1);
            // assertTrue(new String(bExt.getValeurs().get(0).getContenu(), StandardCharsets.UTF_8).equals(
            //		 "gagk aejhkj aekhak akjhjkhkjae kahkjhk aekhaekjh aekhaekjh akhkhae khkkjhkh")); // CR
            // assertTrue(bExt.getValeurs().get(1).getValeur().equals("BL;BZ")); // CODES ORG
            assertTrue(bExt.getValeurs().get(0).getValeur().equals("N7X0;NAX0;NBX0")); // CODES LE
            allBlockHere++;
         }
      }
      assertTrue(allBlockHere == 4);

      // DOS 2 pas heure prel
      dExt = viewHandlerFactory.sendQuery(eT, "DOS2", null);

      allBlockHere = 0;
      assertTrue(dExt != null);
      assertTrue(dExt.getIdentificationDossier().equals("DOS2"));
      assertTrue(dExt.getBlocExternes().size() == 4);
      for(final BlocExterne bExt : dExt.getBlocExternes()){
         if(bExt.getOrdre() == 1){ // patient
            assertTrue(bExt.getEntiteId() == 1);
            assertTrue(bExt.getValeurs().size() == 5);
            assertTrue(bExt.getValeurs().get(0).getValeur().equals("8899888")); // NIP
            assertTrue(bExt.getValeurs().get(1).getValeur().equals("PIERRE")); // NOM
            // assertTrue(bExt.getValeurs().get(2).getValeur().equals("DIAZ")); // NOM_NAIS
            assertTrue(bExt.getValeurs().get(2).getValeur().equals("JEAN-PIERRE")); // PRENOM
            assertTrue(bExt.getValeurs().get(3).getValeur().equals("19670112")); // DATE_NAIS
            assertTrue(bExt.getValeurs().get(4).getValeur().equals("M")); // SEXE
            allBlockHere++;
         }
         if(bExt.getOrdre() == 2){ // maladie
            assertTrue(bExt.getEntiteId() == 7);
            assertTrue(bExt.getValeurs().size() == 1);
            assertTrue(bExt.getValeurs().get(0).getValeur().equals("BL")); // CIM
            allBlockHere++;
         }
         if(bExt.getOrdre() == 3){ // prelevement
            assertTrue(bExt.getEntiteId() == 2);
            assertTrue(bExt.getValeurs().size() == 2);
            assertTrue(bExt.getValeurs().get(0).getValeur().equals("nda")); // NDA
            assertTrue(bExt.getValeurs().get(1).getValeur().equals("20160725")); // DATE_PREL SANS HEURES
            allBlockHere++;
         }
         if(bExt.getOrdre() == 4){ // echantillon
            assertTrue(bExt.getEntiteId() == 3);
            assertTrue(bExt.getValeurs().size() == 1);
            // assertTrue(new String(bExt.getValeurs().get(0).getContenu(), StandardCharsets.UTF_8).equals(
            //		 "gagk aejhkj aekhak akjhjkhkjae kahkjhk aekhaekjh aekhaekjh akhkhae khkkjhkh")); // CR
            // assertTrue(bExt.getValeurs().get(1).getValeur().equals("BL;BZ")); // CODES ORG
            assertTrue(bExt.getValeurs().get(0).getValeur().equals("N7X0")); // CODES LES
            allBlockHere++;
         }
      }
      assertTrue(allBlockHere == 4);

      // DOS 3  nulls everywhere!
      dExt = viewHandlerFactory.sendQuery(eT, "DOS3", null);

      assertTrue(dExt != null);
      assertTrue(dExt.getIdentificationDossier().equals("DOS3"));
      assertTrue(dExt.getBlocExternes().size() == 0);

      // DOS 4  blanks everywhere!
      dExt = viewHandlerFactory.sendQuery(eT, "DOS4", null);

      assertTrue(dExt != null);
      assertTrue(dExt.getIdentificationDossier().equals("DOS4"));
      assertTrue(dExt.getBlocExternes().size() == 0);
   }

}
