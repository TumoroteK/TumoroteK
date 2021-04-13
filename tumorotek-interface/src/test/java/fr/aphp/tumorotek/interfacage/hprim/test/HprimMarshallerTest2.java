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
package fr.aphp.tumorotek.interfacage.hprim.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import fr.aphp.tumorotek.interfacage.jaxb.hprim.HprimMessage;

public class HprimMarshallerTest2
{

   @Test
   public void testUnmarshalVenue() throws JAXBException, ParseException{

      final File file = new File("src/test/resources/HPRIM_GEMA/venuePatient.xml");
      final JAXBContext jaxbContext = JAXBContext.newInstance(HprimMessage.class);

      final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      final HprimMessage message = (HprimMessage) jaxbUnmarshaller.unmarshal(file);

      assertTrue(message.getEntete().getNumEvt().equals(23783L));
      assertTrue(message.getEntete().getDateMes().equals(("2011-09-09T13:31:58")));
      assertTrue(message.toPatientSip().getNip().equals("9018961"));
      assertTrue(message.toPatientSip().getNom().equals("NOMTEST"));
      assertTrue(message.toPatientSip().getNomNaissance().equals("PATROTEST"));
      assertTrue(message.toPatientSip().getSexe().equals("M"));
      assertTrue(message.toPatientSip().getPrenom().equals("ALAIN"));
      assertTrue(message.toPatientSip().getDateNaissance().equals(new SimpleDateFormat("dd/MM/yyyy").parse(("02/05/1975"))));
      assertTrue(message.toPatientSip().getSejours().size() == 1);
      assertTrue(message.toPatientSip().getSejours().iterator().next().getNumero().equals("531604772"));
      assertTrue(message.toPatientSip().getDateEtat() == null);
      assertTrue(message.toPatientSip().getPatientEtat() == "D");
      assertTrue(message.toPatientSip().getDateDeces().equals(new SimpleDateFormat("dd/MM/yyyy").parse(("04/09/2019"))));
      
      assertTrue(message.getAction().equals("création"));
      assertTrue(message.toPatientPassif() == null);
   }

   @Test
   public void testUnmarshalFusion() throws JAXBException, ParseException{

      final File file = new File("src/test/resources/HPRIM_GEMA/fusionPatient.xml");
      final JAXBContext jaxbContext = JAXBContext.newInstance(HprimMessage.class);

      final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      final HprimMessage message = (HprimMessage) jaxbUnmarshaller.unmarshal(file);

      assertTrue(message.getEntete().getNumEvt().equals(121212L));
      assertTrue(message.getEntete().getDateMes().equals(("2011-09-09T09:29:29")));
      assertTrue(message.toPatientSip().getNip().equals("0080"));
      assertTrue(message.toPatientSip().getNom().equals("Doe"));
      assertTrue(message.toPatientSip().getNomNaissance() == null);
      assertTrue(message.toPatientSip().getSexe().equals("M"));
      assertTrue(message.toPatientSip().getPrenom().equals("John"));
      assertTrue(message.toPatientSip().getDateNaissance().equals(new SimpleDateFormat("dd/MM/yyyy").parse(("25/09/1985"))));
      assertTrue(message.toPatientSip().getDateEtat() == null);
      assertTrue(message.getAction().equals("fusion"));
      assertTrue(message.toPatientPassif().getNip().equals("0001"));
      assertTrue(message.toPatientPassif().getNom().equals("Doe2"));
      assertTrue(message.toPatientPassif().getNomNaissance() == null);
      assertTrue(message.toPatientPassif().getSexe().equals("F"));
      assertTrue(message.toPatientPassif().getPrenom().equals("Johnasse"));
      assertTrue(message.toPatientPassif().getDateNaissance().equals(new SimpleDateFormat("dd/MM/yyyy").parse(("11/03/1960"))));
      assertTrue(message.toPatientPassif().getDateEtat() == null);

   }
}
