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
package fr.aphp.tumorotek.manager.test.interfacage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.interfacage.PatientSipDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.interfacage.PatientSipManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.interfacage.PatientSip;
import fr.aphp.tumorotek.model.interfacage.PatientSipSejour;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Classe de test pour le manager PatientSipManager.
 * Classe créée le 02/05/11.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class PatientSipManagerTest extends AbstractManagerTest4
{

   @Autowired
   private PatientSipManager patientSipManager;
   @Autowired
   private PatientSipDao patientSipDao;
   @Autowired
   private PatientManager patientManager;
   @Autowired
   private UtilisateurDao utilisateurDao;

   //	@SuppressWarnings("deprecation")
   //	@Test
   //	public PatientSipManagerTest() {		
   //		// pour eviter l'autowiring vers fantomeDao
   //		this.setDependencyCheck(false);
   //	}
   //	
   //	@Override
   //	protected String[] getConfigLocations() {
   //        return new String[]{ "applicationContextManagerBase.xml",
   //       		 "applicationContextDaoBase.xml" };
   //    }
   //	

   @Test
   public void testFindAllObjectsManager(){
      final List<PatientSip> pats = patientSipManager.findAllObjectsManager();
      assertTrue(pats.size() == 3);
   }

   /**
    * Test la methode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeManager(){
      //teste une recherche exactMatch
      List<PatientSip> pats = patientSipManager.findByNomLikeManager("Sun", true);
      assertTrue(pats.size() == 1);
      //teste une recherche non exactMatch
      pats = patientSipManager.findByNomLikeManager("", false);
      assertTrue(pats.size() == 3);
      //teste une recherche infructueuse
      pats = patientSipManager.findByNomLikeManager("LUX", true);
      assertTrue(pats.size() == 0);
      //null recherche
      pats = patientSipManager.findByNomLikeManager(null, false);
      assertTrue(pats.size() == 0);
   }

   /**
    * Test la methode findByNipLikeManager.
    */
   @Test
   public void testFindByNipLikeManager(){
      //teste une recherche exactMatch
      List<PatientSip> pats = patientSipManager.findByNipLikeManager("713705", true);
      assertTrue(pats.size() == 1);
      //teste une recherche non exactMatch
      pats = patientSipManager.findByNipLikeManager("", false);
      assertTrue(pats.size() == 3);
      //teste une recherche infructueuse
      pats = patientSipManager.findByNipLikeManager("133", true);
      assertTrue(pats.size() == 0);
      //null recherche
      pats = patientSipManager.findByNipLikeManager(null, false);
      assertTrue(pats.size() == 0);
   }

   @Test
   public void testFindByNumeroSejourManager(){
      List<PatientSip> pats = patientSipManager.findByNumeroSejourManager("770000391", true);
      assertTrue(pats.size() == 1);
      pats = patientSipManager.findByNumeroSejourManager("", false);
      assertTrue(pats.size() == 2);
      pats = patientSipManager.findByNumeroSejourManager("LUCY", false);
      assertTrue(pats.size() == 1);
      pats = patientSipManager.findByNumeroSejourManager("LUCY_669", true);
      assertTrue(pats.size() == 0);
      pats = patientSipManager.findByNumeroSejourManager(null, false);
      assertTrue(pats.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      final PatientSip p2 = new PatientSip();
      p2.setNip("12345");
      assertTrue(patientSipManager.findDoublonManager(p2));
      p2.setNip("123456");
      assertFalse(patientSipManager.findDoublonManager(p2));
   }

   @Test
   public void testCreateObjectManagerTest() throws ParseException{
      //Insertion nouvel enregistrement
      PatientSip p = new PatientSip();
      /*Champs obligatoires*/
      p.setNip("68");
      p.setNom("KIEDIS");
      p.setPrenom("Anthony");
      p.setSexe("M");
      p.setPatientEtat("V");
      final Date dateNaissance = new SimpleDateFormat("dd/MM/yyyy").parse("17/03/1971");
      p.setDateNaissance(dateNaissance);

      //insertion
      patientSipManager.createOrUpdatePatientInTempTableManager(p, 10, false);
      assertTrue((patientSipManager.findByNomLikeManager("KIEDIS", true)).size() == 1);
      assertNotNull(p.getDateCreation());
      final Calendar today = Utils.getCurrentSystemCalendar();
      assertTrue(today.get(Calendar.DAY_OF_MONTH) == p.getDateCreation().get(Calendar.DAY_OF_MONTH));
      assertTrue(today.get(Calendar.MONTH) == p.getDateCreation().get(Calendar.MONTH));
      assertTrue(today.get(Calendar.YEAR) == p.getDateCreation().get(Calendar.YEAR));
      assertNull(p.getDateModification());

      //update
      final PatientSip p2 = p.clone();
      p2.setPatientSipId(null);
      p2.setPrenom("YOuch");
      // update
      patientSipManager.createOrUpdatePatientInTempTableManager(p2, 10, false);
      assertTrue(patientSipManager.findByNipLikeManager("68", false).size() == 1);
      p = patientSipManager.findByNipLikeManager("68", false).get(0);
      assertNotNull(p.getDateCreation());
      assertNotNull(p.getDateModification());
      assertTrue(p.getPrenom().equals("YOuch"));

      // clean up
      patientSipManager.removeObjectManager(p);
      patientSipManager.removeObjectManager(null);

      testFindAllObjectsManager();
   }

   @Test
   public void testUpdatePatientInTempTableManager() throws ParseException{
      //Insertion nouvel enregistrement
      final PatientSip p = new PatientSip();
      /*Champs obligatoires*/
      p.setNip("68");
      p.setNom("KIEDIS");
      p.setPrenom("Anthony");
      p.setSexe("M");
      p.setPatientEtat("V");
      final Date dateNaissance = new SimpleDateFormat("dd/MM/yyyy").parse("17/03/1971");
      p.setDateNaissance(dateNaissance);

      //insertion
      patientSipManager.createOrUpdatePatientInTempTableManager(p, 10, false);
      assertTrue((patientSipManager.findByNomLikeManager("KIEDIS", true)).size() == 1);

      //Update
      PatientSip p2 = patientSipManager.findByNomLikeManager("KIEDIS", true).get(0);

      // pas update car identique
      patientSipManager.updatePatientInTempTableManager(p2, false);
      assertNull(p2.getDateModification());

      p2.setNom("Cantrell");
      p2.setPrenom("Jerry");

      patientSipManager.updatePatientInTempTableManager(p2, false);

      p2 = patientSipManager.findByNomLikeManager("Cantrell", true).get(0);

      assertNotNull(p2.getDateCreation());
      assertNotNull(p2.getDateModification());
      final Calendar today = Utils.getCurrentSystemCalendar();
      final Calendar modif = p2.getDateModification();
      assertTrue(today.get(Calendar.DAY_OF_MONTH) == modif.get(Calendar.DAY_OF_MONTH));
      assertTrue(today.get(Calendar.MONTH) == modif.get(Calendar.MONTH));
      assertTrue(today.get(Calendar.YEAR) == modif.get(Calendar.YEAR));

      // erreur enregistrement champ non null
      boolean catched = false;
      p2 = patientSipManager.findByNomLikeManager("Cantrell", true).get(0);
      p2.setNom(null);
      try{
         patientSipManager.updatePatientInTempTableManager(p2, false);
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);
      assertTrue(patientSipManager.findByNipLikeManager("68", false).size() == 1);
      assertTrue(patientSipManager.findByNipLikeManager("68", false).get(0).getDateModification().equals(modif));

      // clean up
      patientSipManager.removeObjectManager(p);
      patientSipManager.removeObjectManager(null);

      testFindAllObjectsManager();
   }

   @Test
   public void testIsSynchronizedPatientManager() throws ParseException, NoSuchFieldException{
      PatientSip p = new PatientSip();
      p.setNip("987Sting");
      p.setNom("Furtado");
      p.setPrenom("Nelly");
      p.setSexe("F");
      p.setPatientEtat("V");
      p.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("17/03/1971"));
      p.setDateEtat(new SimpleDateFormat("dd/MM/yyyy").parse("25/02/2015"));
      final PatientSip patSip = p.clone();

      //insertion
      patientSipManager.createOrUpdatePatientInTempTableManager(p, 10, false);
      assertTrue((patientSipManager.findByNomLikeManager("Furtado", true)).size() == 1);
      p = patientSipManager.findByNomLikeManager("Furtado", true).get(0);

      // identiques et nulls des deux cotés
      assertTrue(patientSipManager.isSynchronizedPatientManager(patSip).isEmpty());

      // modification chps obligatoires
      PatientSip patSip2 = patSip.clone();
      patSip2.setNom("BEN LADEN");
      patSip2.setPrenom("Oussama");
      patSip2.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("01/02/1935"));
      patSip2.setSexe("M");
      patSip2.setPatientEtat("D");
      List<Field> fields = patientSipManager.isSynchronizedPatientManager(patSip2);
      assertTrue(fields.size() == 5);
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("nom")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("prenom")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("dateNaissance")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("sexe")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("patientEtat")));

      // modification des champs non obigatoires 
      // nulls coté temp
      patSip2 = patSip.clone();
      patSip2.setNomNaissance("MALIK");
      patSip2.setVilleNaissance("Islamabad");
      patSip2.setPaysNaissance("PAKISTAN");
      patSip2.setDateEtat(new SimpleDateFormat("dd/MM/yyyy").parse("01/05/2011"));
      patSip2.setDateDeces(new SimpleDateFormat("dd/MM/yyyy").parse("01/05/2011"));

      fields = patientSipManager.isSynchronizedPatientManager(patSip2);
      assertTrue(fields.size() == 5);
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("nomNaissance")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("villeNaissance")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("paysNaissance")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("dateDeces")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("dateEtat")));

      // valeurs identiques pour les champs nulls 
      p.setNomNaissance("MALIK");
      p.setVilleNaissance("Islamabad");
      p.setPaysNaissance("PAKISTAN");
      p.setDateEtat(new SimpleDateFormat("dd/MM/yyyy").parse("01/05/2011"));
      p.setDateDeces(new SimpleDateFormat("dd/MM/yyyy").parse("01/05/2011"));
      patientSipManager.updatePatientInTempTableManager(p, false);

      assertTrue(patientSipManager.isSynchronizedPatientManager(patSip2).isEmpty());

      // nulls coté sip -> seuls nom naissance change
      final PatientSip patSip3 = patSip.clone();
      patSip3.setDateEtat(null);
      fields = patientSipManager.isSynchronizedPatientManager(patSip3);
      assertTrue(fields.size() == 1);
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("nomNaissance")));

      // modifications champs non obligatoires
      patSip3.setNomNaissance("MALIK2");
      patSip3.setVilleNaissance("Islamabad2");
      patSip3.setPaysNaissance("PAKISTAN2");
      patSip3.setDateEtat(new SimpleDateFormat("dd/MM/yyyy").parse("01/06/2011"));
      patSip3.setDateDeces(new SimpleDateFormat("dd/MM/yyyy").parse("01/06/2011"));
      fields = patientSipManager.isSynchronizedPatientManager(patSip3);
      assertTrue(fields.size() == 5);
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("nomNaissance")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("villeNaissance")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("paysNaissance")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("dateDeces")));
      assertTrue(fields.contains(PatientSip.class.getDeclaredField("dateEtat")));

      assertTrue(patientSipManager.isSynchronizedPatientManager(null).isEmpty());

      // clean up
      patientSipManager.removeObjectManager(p);
      testFindAllObjectsManager();
   }

   @Test
   public void testDoSynchronizePatientManager(){
      // creation d'un nouveau PatientSip
      final PatientSip p1 = new PatientSip();
      p1.setNip("68");
      assertNull(patientSipManager.doSynchronizePatientManager(p1));
      // update d'un patient temp
      p1.setNip("666");
      assertNull(patientSipManager.doSynchronizePatientManager(p1));
      // synchronisation
      p1.setNip("876");
      assertTrue(
         patientSipManager.doSynchronizePatientManager(p1).equals(patientManager.findByNomLikeManager("DELPHINO", true).get(0)));
      // null
      assertNull(patientSipManager.doSynchronizePatientManager(null));
   }

   @Test
   public void testUpdatePatientSystem() throws ParseException{
      // creation patient système
      Patient p = new Patient();
      p.setNip("69");
      p.setNom("Gainsbourg");
      p.setPrenom("Serge");
      p.setSexe("M");
      p.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("05/11/1945"));
      p.setVilleNaissance("Paris");
      p.setPaysNaissance("FRANCE");
      p.setPatientEtat("D");
      p.setDateEtat(new SimpleDateFormat("dd/MM/yyyy").parse("15/09/1991"));
      p.setDateDeces(new SimpleDateFormat("dd/MM/yyyy").parse("15/09/1991"));
      final Utilisateur u = utilisateurDao.findById(1);

      patientManager.createOrUpdateObjectManager(p, null, null, null, null, null, null, null, u, "creation", null, false);
      assertFalse(patientManager.findByNipLikeManager("69", true).isEmpty());
      assertFalse(patientManager.findByNomLikeManager("Gainsbourg", true).isEmpty());
      assertTrue(getOperationManager().findByObjectManager(p).size() == 1);

      p = patientManager.findByNipLikeManager("69", true).get(0);

      // Patient sip
      final PatientSip pSip = new PatientSip();
      pSip.setNip("69");
      pSip.setNom("Gainsbarre");
      pSip.setPrenom("Serge");
      pSip.setSexe("M");
      pSip.setNomNaissance("lulu");
      pSip.setPatientEtat("D");
      pSip.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("05/11/1945"));

      patientSipManager.updatePatientSystem(pSip, p);

      assertFalse(patientManager.findByNomLikeManager("Gainsbarre", true).isEmpty());
      assertTrue(patientManager.findByNomLikeManager("Gainsbourg", true).isEmpty());

      p = patientManager.findByNipLikeManager("69", true).get(0);
      assertTrue(p.getNomNaissance().equals("lulu"));
      assertTrue(p.getPatientEtat().equals("D"));
      assertTrue(getOperationManager().findByObjectManager(p).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(p).get(1).getOperationType().getNom().equals("Synchronisation"));
      assertNull(getOperationManager().findByObjectManager(p).get(1).getUtilisateur());

      // Synchronisation engendrant une erreur de validation
      pSip.setPatientEtat("KKK");
      boolean catched = false;
      try{
         patientSipManager.updatePatientSystem(pSip, p);
      }catch(final ValidationException ve){
         catched = true;
      }
      assertTrue(catched);
      p = patientManager.findByNipLikeManager("69", true).get(0);
      assertTrue(p.getPatientEtat().equals("D"));
      assertTrue(getOperationManager().findByObjectManager(p).size() == 2);

      // cleanUp
      patientManager.removeObjectManager(p, null, u, null);
      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p);
      cleanUpFantomes(fs);
      assertTrue(patientManager.findAllObjectsManager().size() == 5);
      testFindAllObjectsManager();
   }

   @Test
   public void testFirstInFirstOut() throws ParseException, NoSuchFieldException{
      // PatientSip first = patientSipDao.findFirst().get(0);
      // creation Object qui sera supprime
      final PatientSip p1 = new PatientSip();
      p1.setNip("11111");
      p1.setNom("FirstOut");
      p1.setPrenom("Out");
      p1.setSexe("F");
      p1.setPatientEtat("V");
      p1.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("17/03/1971"));
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("17/03/2001"));
      p1.setDateCreation(cal);
      patientSipManager.createOrUpdatePatientInTempTableManager(p1, 4, false);

      assertTrue(patientSipDao.findFirst().get(0).equals(p1));

      final PatientSip p2 = new PatientSip();
      p2.setNip("24536");
      p2.setNom("Last");
      p2.setPrenom("Last");
      p2.setSexe("F");
      p2.setPatientEtat("V");
      p2.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("17/03/1971"));

      //insertion engendrant FIRST OUT
      patientSipManager.createOrUpdatePatientInTempTableManager(p2, 4, false);
      assertTrue((patientSipManager.findByNomLikeManager("Last", true)).size() == 1);
      assertTrue((patientSipManager.findByNomLikeManager(p1.getNom(), true)).isEmpty());
      assertTrue(patientSipManager.findAllObjectsManager().size() == 4);

      // clean up
      patientSipManager.removeObjectManager(patientSipManager.findByNomLikeManager("Last", true).get(0));

      testFindAllObjectsManager();

      // pour retrouver l'ordre
      // testFirstInFirstOut();
      // testFirstInFirstOut();
   }

   @Test
   public void testparseHl7MessagePID() throws IOException, ParseException{
      String pid =
         "PID|1|" + "|1330000303^^^IF_MCKN||TESTEXPLOBALUN^ARNAUD^^^^^D~NOMNAISS^ARNAUD^^^^^L|" + "MAMAN_NAME|19550115|M||"
            + "||100||||S||770000390||||||||||100^100||N\n" + "PV1|1|P|1005|R|770000391||||||||||||||770000391^^^CR"
            + "|||||||||||||||||||||||||" + "20110915080000||||||128498|A\n";

      PatientSip pSip = patientSipManager.parseHl7MessagePID(pid);

      assertTrue(pSip.getNip().equals("1330000303"));
      assertTrue(pSip.getNom().equals("TESTEXPLOBALUN"));
      assertTrue(pSip.getNomNaissance().equals("NOMNAISS"));
      assertTrue(pSip.getPrenom().equals("ARNAUD"));
      assertTrue(pSip.getDateNaissance().equals(new SimpleDateFormat("dd/MM/yyyy").parse("15/01/1955")));
      assertTrue(pSip.getSexe().equals("M"));
      assertNull(pSip.getPaysNaissance());
      assertNull(pSip.getVilleNaissance());
      assertNull(pSip.getDateEtat());
      assertNull(pSip.getDateDeces());
      assertTrue(pSip.getPatientEtat().equals("V"));
      assertTrue(pSip.getSejours().size() == 1);

      final PatientSipSejour s = pSip.getSejours().iterator().next();
      assertTrue(s.getNumero().equals("770000391"));
      assertTrue(s.getDateSejour().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("15/09/2011 08:00:00")));

      pid = "PID|1|" + "|1330000304^^^IF_MCKN||DEADUNK^^^^^^Z|" + "|19120115|X||"
         + "||100||||S||770000390||||||||||100^100|20100919|Y\n";

      pSip = patientSipManager.parseHl7MessagePID(pid);

      assertTrue(pSip.getNip().equals("1330000304"));
      assertTrue(pSip.getNom().equals("DEADUNK"));
      assertNull(pSip.getNomNaissance());
      assertNull(pSip.getPrenom());
      assertTrue(pSip.getDateNaissance().equals(new SimpleDateFormat("dd/MM/yyyy").parse("15/01/1912")));
      assertTrue(pSip.getSexe().equals("Ind"));
      assertNull(pSip.getPaysNaissance());
      assertNull(pSip.getVilleNaissance());
      assertNull(pSip.getDateEtat());
      assertTrue(pSip.getDateDeces().equals(new SimpleDateFormat("dd/MM/yyyy").parse("19/09/2010")));
      assertTrue(pSip.getPatientEtat().equals("D"));
      assertTrue(pSip.getSejours().isEmpty());
   }

   @Test
   public void testparseHl7MessageMRG() throws IOException, ParseException{
      String pid = "MRG|1330000316^^^IF_MCKN|||1330000316^^^IF_MCKN\n";

      PatientSip pSip = patientSipManager.parseHl7MessageMRG(pid);

      assertTrue(pSip.getNip().equals("1330000316"));
      assertNull(pSip.getNom());
      assertNull(pSip.getNomNaissance());
      assertNull(pSip.getPrenom());
      assertNull(pSip.getDateNaissance());
      assertNull(pSip.getSexe());
      assertNull(pSip.getPaysNaissance());
      assertNull(pSip.getVilleNaissance());
      assertNull(pSip.getDateEtat());
      assertNull(pSip.getDateDeces());
      assertTrue(pSip.getPatientEtat().equals("V"));

      pid = "MRG|13300003199^^^IF_MCKN|||1330000316^^^IF_MCKN\n";

      pSip = patientSipManager.parseHl7MessageMRG(pid);

      assertTrue(pSip.getNip().equals("13300003199"));
      assertNull(pSip.getNom());
      assertNull(pSip.getNomNaissance());
      assertNull(pSip.getPrenom());
      assertNull(pSip.getDateNaissance());
      assertNull(pSip.getSexe());
      assertNull(pSip.getPaysNaissance());
      assertNull(pSip.getVilleNaissance());
      assertNull(pSip.getDateEtat());
      assertNull(pSip.getDateDeces());
      assertTrue(pSip.getPatientEtat().equals("V"));
   }
}
