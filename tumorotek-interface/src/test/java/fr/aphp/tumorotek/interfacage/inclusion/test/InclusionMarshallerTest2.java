
package fr.aphp.tumorotek.interfacage.inclusion.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.aphp.tumorotek.interfacage.PatientHandler;
import fr.aphp.tumorotek.interfacage.jaxb.inclusion.CreateRequest;
import fr.aphp.tumorotek.interfacage.jaxb.inclusion.GenderType;
import fr.aphp.tumorotek.interfacage.jaxb.inclusion.InclusionServiceClient;
import fr.aphp.tumorotek.interfacage.jaxb.inclusion.SiteRefType;
import fr.aphp.tumorotek.interfacage.jaxb.inclusion.StudyRefType;
import fr.aphp.tumorotek.interfacage.jaxb.inclusion.StudySubjectType;
import fr.aphp.tumorotek.interfacage.jaxb.inclusion.SubjectType;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.model.coeur.patient.Patient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-ws.xml"})
// @TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class InclusionMarshallerTest2
{

   @Autowired
   PatientHandler patientHandler;

   @Autowired
   PatientManager patientManager;

   @Autowired
   InclusionServiceClient inclusionServiceClient;

   @Test
   @Rollback(true)
   public void testInclusionHandler() throws JAXBException, ParseException{

      final File file = new File("/home/mathieu/Documents/tumorotek/interfacages/" + "inclusion/subjects.xml");
      final JAXBContext jaxbContext = JAXBContext.newInstance(CreateRequest.class);

      final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

      jaxbUnmarshaller.setEventHandler(new ValidationEventHandler()
      {
         @Override
         public boolean handleEvent(final ValidationEvent event){
            throw new RuntimeException(event.getMessage(), event.getLinkedException());
         }
      });

      final CreateRequest message = (CreateRequest) jaxbUnmarshaller.unmarshal(file);

      assertTrue(message.getStudySubject().size() == 2);

      if(patientHandler != null){

         patientHandler.handleInclusionMelbase(message.getStudySubject().get(0));

         assertFalse(patientManager.findByNomLikeManager("10-10", true).isEmpty());
         final Patient p = patientManager.findByNomLikeManager("10-10", true).get(0);

         assertNull(p.getNip());
         assertTrue(p.getPrenom().equals("M;B R"));
         assertTrue(p.getSexe().equals("M;B R"));
         assertTrue(p.getDateNaissance().equals(new SimpleDateFormat("dd-Mm-yyyy").parse("01-01-1956")));
         assertTrue(patientManager.getTotMaladiesCountManager(p) == 3);
      }

   }

   @Test
   @Rollback(true)
   public void testInclusionMarshaller() throws JAXBException, ParseException, DatatypeConfigurationException{

      final CreateRequest request = new CreateRequest();

      request.getStudySubject().add(setSubject());

      final File file = new File("/home/mathieu/Bureau/file.xml");

      final JAXBContext jaxbContext = JAXBContext.newInstance(CreateRequest.class);

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

      jaxbMarshallerT.marshal(request, file);
      jaxbMarshallerT.marshal(request, System.out);
   }

   @Test
   @Rollback(true)
   public void testAddSubject() throws ParseException, DatatypeConfigurationException{
      assertTrue(inclusionServiceClient.addSubject(setSubject()));

   }

   @Test
   @Rollback(true)
   public void testAddSubjectTK() throws ParseException, DatatypeConfigurationException{
      assertTrue(inclusionServiceClient.addSubjectTK(setSubject()));

   }

   private StudySubjectType setSubject() throws ParseException, DatatypeConfigurationException{
      final StudySubjectType st = new StudySubjectType();

      st.setLabel("WS4");
      st.setSecondaryLabel("SPRING3");

      final GregorianCalendar g = new GregorianCalendar();
      g.setTime(new SimpleDateFormat("dd-MM-yyyy").parse("12-12-2001"));
      st.setEnrollmentDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(g));

      final StudyRefType srt = new StudyRefType();
      srt.setIdentifier("MELBASE");
      final SiteRefType sr = new SiteRefType();
      sr.setIdentifier("26");
      srt.setSiteRef(sr);
      st.setStudyRef(srt);

      final SubjectType sub = new SubjectType();
      sub.setUniqueIdentifier("1000");
      sub.setYearOfBirth(new BigInteger("1987"));
      sub.setGender(GenderType.F);
      st.setSubject(sub);

      return st;
   }
}
