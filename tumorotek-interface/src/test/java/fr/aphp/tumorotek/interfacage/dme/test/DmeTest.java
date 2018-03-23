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
package fr.aphp.tumorotek.interfacage.dme.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.interfacage.jaxb.hm.Dme;
import fr.aphp.tumorotek.interfacage.jaxb.hm.Formulaire;
import fr.aphp.tumorotek.interfacage.jaxb.hm.Rubrique;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;

public class DmeTest
{

   @Autowired
   private PrelevementManager prelevementManager;

   @Test
   public void testDmeMarshaller() throws JAXBException{

      final Dme dme = new Dme();

      dme.getFormulaires().add(addFormulaire());

      final JAXBContext jaxbContext = JAXBContext.newInstance(Dme.class);

      final Marshaller jaxbMarshallerT = jaxbContext.createMarshaller();

      jaxbMarshallerT.setEventHandler(new ValidationEventHandler()
      {
         @Override
         public boolean handleEvent(final ValidationEvent event){
            throw new RuntimeException(event.getMessage(), event.getLinkedException());
         }
      });

      // output pretty printed
      jaxbMarshallerT.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      // jaxbMarshallerT.marshal(request, file);
      jaxbMarshallerT.marshal(dme, System.out);
   }

   private Formulaire addFormulaire(){
      final Formulaire fm = new Formulaire();

      fm.setIdentifiant("id_12345");
      // fm.getPatient().setIpp("PAT6677799");
      // fm.setCodForm(79);

      final Rubrique rb1 = new Rubrique();
      rb1.setCodRub("LST_TYPPRELVT1");
      rb1.getValeurs().add("LIQ");
      fm.getRubriques().add(rb1);
      final Rubrique rb2 = new Rubrique();
      rb2.setCodRub("DAT_PRLVT_TUMO2");
      rb2.getValeurs().add(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
      fm.getRubriques().add(rb2);
      final Rubrique rb3 = new Rubrique();
      rb3.setCodRub("TXT_NAT_PRLVT3");
      rb3.getValeurs().add("SANG");
      fm.getRubriques().add(rb3);
      final Rubrique rb4 = new Rubrique();
      rb4.setCodRub("NUM_NB_ECHANT4");
      rb4.getValeurs().add("23");
      fm.getRubriques().add(rb4);
      return fm;
   }

   @Test
   public void testDmeFromPrelevementMarshaller() throws JAXBException, ParseException{

      final Dme dme = new Dme();

      final Prelevement prel1 = new Prelevement();
      prel1.setPrelevementId(123456);
      final Nature nat = new Nature();
      nat.setNature("TISSU TUMORAL");
      prel1.setNature(nat);
      final Calendar dateP = Calendar.getInstance();
      dateP.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("21/09/2014 12:56"));
      prel1.setDatePrelevement(dateP);
      prel1.setPatientNda("33");

      final Patient p = new Patient();
      p.setNip("9852576");
      final Maladie m = new Maladie();
      m.setPatient(p);
      prel1.setMaladie(m);

      final Prelevement prel2 = new Prelevement();
      final Patient p2 = new Patient();
      final Maladie m2 = new Maladie();
      m2.setPatient(p2);
      prel2.setMaladie(m2);

      dme.getFormulaires().add(new Formulaire(prel1, true, 12));
      dme.getFormulaires().add(new Formulaire(new Prelevement(), false, null));
      dme.getFormulaires().add(new Formulaire(prel2, false, null));

      final JAXBContext jaxbContext = JAXBContext.newInstance(Dme.class);

      final Marshaller jaxbMarshallerT = jaxbContext.createMarshaller();

      jaxbMarshallerT.setEventHandler(new ValidationEventHandler()
      {
         @Override
         public boolean handleEvent(final ValidationEvent event){
            throw new RuntimeException(event.getMessage(), event.getLinkedException());
         }
      });

      // output pretty printed
      jaxbMarshallerT.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      // jaxbMarshallerT.marshal(request, file);
      jaxbMarshallerT.marshal(dme, System.out);
   }
}
