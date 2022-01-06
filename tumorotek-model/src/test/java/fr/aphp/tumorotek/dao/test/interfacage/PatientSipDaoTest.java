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
package fr.aphp.tumorotek.dao.test.interfacage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.interfacage.PatientSipDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.interfacage.PatientSip;
import fr.aphp.tumorotek.model.interfacage.PatientSipSejour;

/**
 *
 * Classe de test pour le DAO PatientSipDao et le
 * bean du domaine PatientSip.
 * Classe de test créée le 15/04/11.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class PatientSipDaoTest extends AbstractDaoTest
{

   @Override
   protected String[] getConfigLocations(){
      return new String[] {"applicationContextDao-interfacages-test-mysql.xml"};
   }


   @Autowired
 PatientSipDao patientSipDao;

   /**
    * Constructeur.
    */
   public PatientSipDaoTest(){}

   @Test
public void setPatientSipDao(final PatientSipDao pDao){
      this.patientSipDao = pDao;
   }

   @Test
public void testToString(){
      PatientSip p1 = patientSipDao.findById(1);
      assertTrue(p1.toString().equals("{" + p1.getNip() + " " + p1.getNom() + "}"));
      p1 = new PatientSip();
      assertTrue(p1.toString().equals("{Empty PatientSip}"));
   }

   @Test
public void testReadAllPatientSips(){
      final List<PatientSip> patients = IterableUtils.toList(patientSipDao.findAll());
      assertTrue(patients.size() == 3);
   }

   @Test
public void testFindByNip(){
      List<PatientSip> patients = patientSipDao.findByNip("666");
      assertTrue(patients.size() == 1);
      patients = patientSipDao.findByNip("K12");
      assertTrue(patients.size() == 0);
      patients = patientSipDao.findByNip("%");
      assertTrue(patients.size() == 3);
      patients = patientSipDao.findByNip(null);
      assertTrue(patients.size() == 0);
   }

   @Test
public void testFindByNom(){
      List<PatientSip> patients = patientSipDao.findByNom("Lucifer");
      assertTrue(patients.size() == 1);
      patients = patientSipDao.findByNom("SIMPSON");
      assertTrue(patients.size() == 0);
      patients = patientSipDao.findByNom("%");
      assertTrue(patients.size() == 3);
      patients = patientSipDao.findByNom(null);
      assertTrue(patients.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un patient temporaire venant du SIP.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   @Test
public void testCrudPatientSip() throws Exception{
      final PatientSip p = new PatientSip();
      p.setNip("113");
      p.setNom("Winch");
      p.setNomNaissance("Jacky");
      p.setPrenom("Largo");
      p.setSexe("M");
      Date date = new SimpleDateFormat("dd/MM/yyyy").parse("13/08/1945");
      p.setDateNaissance(date);
      p.setVilleNaissance("Fère-en-tardenois");
      p.setPaysNaissance("FRANCE");
      p.setPatientEtat("D");
      final Date etat = new SimpleDateFormat("dd/MM/yyyy").parse("28/10/2009");
      p.setDateEtat(etat);
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/05/2010"));
      p.setDateCreation(cal);

      // Numéros de sejour
      final Set<PatientSipSejour> sejs = new HashSet<>();
      final PatientSipSejour s1 = new PatientSipSejour();
      s1.setNumero("112233445566");
      s1.setDateSejour(etat);
      s1.setPatientSip(p);
      sejs.add(s1);
      final PatientSipSejour s2 = new PatientSipSejour();
      s2.setNumero("445566889900");
      s2.setDateSejour(cal.getTime());
      s2.setPatientSip(p);
      sejs.add(s2);
      p.setSejours(sejs);

      // Test de l'insertion
      patientSipDao.save(p);
      assertNotNull(p.getPatientSipId());
      assertEquals(4, IterableUtils.toList(patientSipDao.findAll()).size());

      // Test de la mise à jour
      final PatientSip p2 = patientSipDao.findById(p.getPatientSipId());
      assertNotNull(p2);
      assertTrue(p2.getNip().equals("113"));
      assertTrue(p2.getNom().equals("Winch"));
      assertTrue(p2.getNomNaissance().equals("Jacky"));
      assertTrue(p2.getPrenom().equals("Largo"));
      assertTrue(p2.getDateNaissance().equals(date));
      assertTrue(p2.getVilleNaissance().equals("Fère-en-tardenois"));
      assertTrue(p2.getPaysNaissance().equals("FRANCE"));
      assertTrue(p2.getPatientEtat().equals("D"));
      assertTrue(p2.getDateEtat().equals(etat));
      assertNull(p2.getDateDeces());
      assertTrue(p2.getDateCreation().equals(cal));
      assertNull(p2.getDateModification());
      assertTrue(p2.getSejours().size() == 2);
      //update
      p.setNom("Rey mysterio");
      p.setNomNaissance(null);
      p.setPrenom("Junior");
      date = new SimpleDateFormat("dd/MM/yyyy").parse("13/08/2005");
      p.setDateNaissance(date);
      p.setVilleNaissance("Mexico city");
      p.setPaysNaissance("MEXICO");
      p.setPatientEtat("V");
      p.setDateEtat(null);
      final Calendar cal2 = Calendar.getInstance();
      cal2.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/05/2010 15:15:15"));
      final PatientSipSejour s3 = new PatientSipSejour();
      s3.setNumero("88889999");
      s3.setDateSejour(cal.getTime());
      s3.setPatientSip(p2);
      p2.getSejours().add(s3);

      p.setDateModification(cal2);
      patientSipDao.save(p2);

      assertTrue(IterableUtils.toList(patientSipDao.findAll()).size() == 4);
      final int id = p.getPatientSipId();
      assertTrue(patientSipDao.findById(id).getNip().equals("113"));
      assertTrue(patientSipDao.findById(id).getNom().equals("Rey mysterio"));
      assertNull(patientSipDao.findById(id).getNomNaissance());
      assertTrue(patientSipDao.findById(id).getPrenom().equals("Junior"));
      assertTrue(patientSipDao.findById(id).getDateNaissance().equals(date));
      assertTrue(patientSipDao.findById(id).getVilleNaissance().equals("Mexico city"));
      assertTrue(patientSipDao.findById(id).getPaysNaissance().equals("MEXICO"));
      assertTrue(patientSipDao.findById(id).getPatientEtat().equals("V"));
      assertNull(patientSipDao.findById(id).getDateEtat());
      assertTrue(patientSipDao.findById(id).getDateCreation().equals(cal));
      assertTrue(patientSipDao.findById(id).getDateModification().equals(cal2));
      assertTrue(patientSipDao.findById(id).getSejours().size() == 3);
      assertTrue(patientSipDao.findByNumeroSejour("88889999").size() == 1);

      // Test de la délétion
      patientSipDao.deleteById(id);
      assertNull(patientSipDao.findById(id));
      assertTrue(IterableUtils.toList(patientSipDao.findAll()).size() == 3);
      assertTrue(patientSipDao.findByNumeroSejour("88889999").isEmpty());
   }

   @Test
public void testFindByNumeroSejour(){
      List<PatientSip> sips = patientSipDao.findByNumeroSejour("13562717");
      assertTrue(sips.isEmpty());
      sips = patientSipDao.findByNumeroSejour(null);
      assertTrue(sips.isEmpty());
      sips = patientSipDao.findByNumeroSejour("770000391");
      assertTrue(sips.size() == 1);
      assertTrue(sips.get(0).getNom().equals("Sun"));
      sips = patientSipDao.findByNumeroSejour("LUCY_6662");
      assertTrue(sips.size() == 1);
      assertTrue(sips.get(0).getNom().equals("Lucifer"));
      sips = patientSipDao.findByNumeroSejour("LUCY_6663");
      assertTrue(sips.size() == 1);
      assertTrue(sips.get(0).getNom().equals("Lucifer"));

   }

   /**
    * Test des méthodes surchargées "equals" et hashcode pour
    * la table transcodeUtilisateur.
    */
   @Test
public void testEqualsAndHashCode(){
      final PatientSip p1 = new PatientSip();
      final PatientSip p2 = new PatientSip();
      assertFalse(p1.equals(null));
      assertNotNull(p2);
      assertTrue(p1.equals(p2));
      assertTrue(p1.equals(p2));
      assertTrue(p1.hashCode() == p2.hashCode());

      final String i1 = "azs1";
      final String i2 = "azs2";
      final String i3 = new String("azs2");

      p1.setNip(i1);
      assertFalse(p1.equals(p2));
      assertFalse(p2.equals(p1));
      assertTrue(p1.hashCode() != p2.hashCode());
      p2.setNip(i2);
      assertFalse(p1.equals(p2));
      assertFalse(p2.equals(p1));
      assertTrue(p1.hashCode() != p2.hashCode());
      p1.setNip(i3);
      assertTrue(p1.equals(p2));
      assertTrue(p2.equals(p1));
      assertTrue(p1.hashCode() == p2.hashCode());
      p1.setNip(i2);
      assertTrue(p1.equals(p2));
      assertTrue(p2.equals(p1));
      assertTrue(p1.hashCode() == p2.hashCode());

      // dummy
      final Categorie c = new Categorie();
      assertFalse(p1.equals(c));
   }

   @Test
public void testClone(){
      final PatientSip p = patientSipDao.findById(1);
      final PatientSip p2 = p.clone();
      assertTrue(p.equals(p2));
      assertTrue(p.getNip().equals(p2.getNip()));
      assertTrue(p.getNom().equals(p2.getNom()));
      assertNull(p2.getNomNaissance());
      assertTrue(p.getPrenom().equals(p2.getPrenom()));
      assertTrue(p.getSexe().equals(p2.getSexe()));
      assertTrue(p.getDateNaissance().equals(p2.getDateNaissance()));
      assertTrue(p.getVilleNaissance().equals(p2.getVilleNaissance()));
      assertTrue(p.getPaysNaissance().equals(p2.getPaysNaissance()));
      assertNull(p2.getDateDeces());
      assertNull(p2.getDateEtat());
      assertTrue(p.getPatientEtat().equals(p2.getPatientEtat()));
      assertTrue(p.getDateCreation().equals(p2.getDateCreation()));
      assertNull(p2.getDateModification());
   }

   @Test
public void testFindCountAll(){
      assertTrue(patientSipDao.findCountAll().size() == 1);
      assertTrue(patientSipDao.findCountAll().get(0) == 3);
   }

   @Test
public void testFindFirst(){
      final List<PatientSip> first = patientSipDao.findFirst();
      assertTrue(first.size() == 1);
      assertTrue(first.get(0).getPatientSipId() == 1);
   }

   @Test
public void testToPatient() throws ParseException{
      final PatientSip p = new PatientSip();
      p.setNip("123");
      p.setNom("TEST");
      p.setPrenom("JOHN");
      p.setNomNaissance("TEST2");
      p.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("12/12/1912"));
      p.setSexe("F");
      p.setVilleNaissance("PARIS");
      p.setPaysNaissance("FRANCE");
      p.setPatientEtat("D");
      p.setDateEtat(new SimpleDateFormat("dd/MM/yyyy").parse("12/01/2012"));
      p.setDateDeces(new SimpleDateFormat("dd/MM/yyyy").parse("12/01/2012"));
      final Patient pat = p.toPatient();
      assertTrue(pat.getNip().equals(p.getNip()));
      assertTrue(pat.getNom().equals(p.getNom()));
      assertTrue(pat.getNomNaissance().equals(p.getNomNaissance()));
      assertTrue(pat.getPrenom().equals(p.getPrenom()));
      assertTrue(pat.getSexe().equals(p.getSexe()));
      assertTrue(pat.getDateNaissance().equals(p.getDateNaissance()));
      assertTrue(pat.getVilleNaissance().equals(p.getVilleNaissance()));
      assertTrue(pat.getPaysNaissance().equals(p.getPaysNaissance()));
      assertTrue(pat.getDateDeces().equals(p.getDateDeces()));
      assertTrue(pat.getDateEtat().equals(p.getDateEtat()));
      assertTrue(pat.getPatientEtat().equals(p.getPatientEtat()));
   }
}
