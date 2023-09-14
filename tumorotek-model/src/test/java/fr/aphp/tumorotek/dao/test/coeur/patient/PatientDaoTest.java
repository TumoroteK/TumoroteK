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
package fr.aphp.tumorotek.dao.test.coeur.patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.gatsbi.PatientIdentifiant;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Etablissement;

/**
 *
 * Classe de test pour le DAO PatientDao et le
 * bean du domaine Patient.
 * Classe de test créée le 28/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class PatientDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private PatientDao patientDao;
   private EntiteDao entiteDao;
   private BanqueDao banqueDao;
   private EtablissementDao etablissementDao;

   /**
    * Constructeur.
    */
   public PatientDaoTest(){}

   public void setPatientDao(final PatientDao pDao){
      this.patientDao = pDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEtablissementDao(final EtablissementDao eDao){
      this.etablissementDao = eDao;
   }

   public void testToString(){
      Patient p1 = patientDao.findById(1);
      assertTrue(p1.toString().equals("{" + p1.getNom() + " " + p1.getPrenom() + "}"));
      assertTrue(p1.listableObjectId().equals(new Integer(1)));
      assertTrue(p1.entiteNom().equals(entiteDao.findByNom("Patient").get(0).getNom()));
      p1 = patientDao.findById(5);
      assertTrue(p1.toString().equals("{" + p1.getNom() + " prenom inconnu}"));
      p1 = new Patient();
      assertTrue(p1.toString().equals("{Empty Patient}"));
   }

   public void testReadAllPatients(){
      final List<Patient> patients = patientDao.findAll();
      assertTrue(patients.size() == 5);
   }

   public void testFindByAllIds(){
      final List<Integer> ids = patientDao.findByAllIds();
      assertTrue(ids.size() == 5);
   }

   public void testFindByAllIdsWithBanques(){
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));

      List<Integer> ids = patientDao.findByAllIdsWithBanques(bks);
      assertTrue(ids.size() == 1);

      bks.add(banqueDao.findById(2));
      ids = patientDao.findByAllIdsWithBanques(bks);
      assertTrue(ids.size() == 2);

      ids = patientDao.findByAllIdsWithBanques(null);
      assertTrue(ids.size() == 0);
   }

   public void testFindByExcludedId(){
      List<Patient> patients = patientDao.findByExcludedId(1, "MAYER");
      assertTrue(patients.size() == 0);
      patients = patientDao.findByExcludedId(8, "MAYER");
      assertTrue(patients.size() == 1);
      patients = patientDao.findByExcludedId(8, "MAYER2");
      assertTrue(patients.size() == 0);
      patients = patientDao.findByExcludedId(null, null);
      assertTrue(patients.size() == 0);
   }

   public void testFindByIdInList(){
      final List<Integer> ids = new ArrayList<>();
      ids.add(1);
      ids.add(2);
      ids.add(3);
      ids.add(10);
      List<Patient> patients = patientDao.findByIdInList(ids);
      assertTrue(patients.size() == 3);

      patients = patientDao.findByIdInList(null);
      assertTrue(patients.size() == 0);
   }

   public void testFindByNip(){
      List<Patient> patients = patientDao.findByNip("876%");
      assertTrue(patients.size() == 2);
      patients = patientDao.findByNip("K12");
      assertTrue(patients.size() == 0);
      patients = patientDao.findByNip("876");
      assertTrue(patients.size() == 1);
      patients = patientDao.findByNip(null);
      assertTrue(patients.size() == 0);
   }

   public void testFindByNipWithExcludedId(){
      List<Patient> patients = patientDao.findByNipWithExcludedId("876%", 1);
      assertTrue(patients.size() == 2);
      patients = patientDao.findByNipWithExcludedId("876%", 3);
      assertTrue(patients.size() == 1);
      patients = patientDao.findByNipWithExcludedId("K12", 1);
      assertTrue(patients.size() == 0);
      patients = patientDao.findByNipWithExcludedId("876", 1);
      assertTrue(patients.size() == 1);
      patients = patientDao.findByNipWithExcludedId("876", 3);
      assertTrue(patients.size() == 0);
      patients = patientDao.findByNipWithExcludedId(null, 1);
      assertTrue(patients.size() == 0);
   }

   public void testFindByNom(){
      List<Patient> patients = patientDao.findByNom("MA%");
      assertTrue(patients.size() == 2);
      patients = patientDao.findByNom("SIMPSON");
      assertTrue(patients.size() == 0);
      patients = patientDao.findByNom("MAYER");
      assertTrue(patients.size() == 1);
      patients = patientDao.findByNom(null);
      assertTrue(patients.size() == 0);
   }

   public void testFindByNipReturnIds(){
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      List<Integer> patients = patientDao.findByNipReturnIds("876%", bks);
      assertTrue(patients.size() == 1);

      patients = patientDao.findByNipReturnIds("K12", bks);
      assertTrue(patients.size() == 0);

      patients = patientDao.findByNipReturnIds("876", bks);
      assertTrue(patients.size() == 1);

      bks.add(banqueDao.findById(2));
      patients = patientDao.findByNipReturnIds("%", bks);
      assertTrue(patients.size() == 2);

      patients = patientDao.findByNipReturnIds(null, bks);
      assertTrue(patients.size() == 0);

      patients = patientDao.findByNipReturnIds("%", null);
      assertTrue(patients.size() == 0);
   }

   public void testFindByNomReturnIds(){
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      List<Integer> patients = patientDao.findByNomReturnIds("MA%", bks);
      assertTrue(patients.size() == 0);

      patients = patientDao.findByNomReturnIds("DEL%", bks);
      assertTrue(patients.size() == 1);

      patients = patientDao.findByNomReturnIds("DELPHINO", bks);
      assertTrue(patients.size() == 1);

      patients = patientDao.findByNomReturnIds("SIMPSON", bks);
      assertTrue(patients.size() == 0);

      bks.add(banqueDao.findById(2));
      patients = patientDao.findByNomReturnIds("MAYER", bks);
      assertTrue(patients.size() == 1);

      patients = patientDao.findByNomReturnIds("%", bks);
      assertTrue(patients.size() == 2);

      patients = patientDao.findByNomReturnIds(null, bks);
      assertTrue(patients.size() == 0);

      patients = patientDao.findByNomReturnIds("DEL%", null);
      assertTrue(patients.size() == 0);
   }

   public void testFindByDateNaissance() throws ParseException{
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("14/12/1971");
      List<Patient> patients = patientDao.findByDateNaissance(search);
      assertTrue(patients.size() == 1);
      search = new SimpleDateFormat("dd/MM/yyyy").parse("15/12/1971");
      patients = patientDao.findByDateNaissance(search);
      assertTrue(patients.size() == 0);
      patients = patientDao.findByDateNaissance(null);
      assertTrue(patients.size() == 0);
   }

   public void testFindByIncomplet(){
      final List<Patient> patients = patientDao.findByEtatIncomplet();
      assertTrue(patients.size() == 1);
   }

   /**
    * Test l'appel de la méthode findAllNips().
    */
   public void testFindAllNips(){
      final List<String> nips = patientDao.findAllNips();
      assertTrue(nips.size() == 4);
      assertTrue(nips.get(0).equals("0987"));
      assertTrue(nips.get(3).equals("87666"));
   }

   /**
    * Test l'appel de la méthode findAllNoms().
    */
   public void testFindAllNoms(){
      final List<String> noms = patientDao.findAllNoms();
      assertTrue(noms.size() == 5);
      assertTrue(noms.get(0).equals("DELPHINO"));
      assertTrue(noms.get(4).equals("SOLIS"));
   }

   public void testFindByNomInList(){
      List<String> criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("MAYER");
      criteres.add("SOLIS");
      final List<Banque> banks = new ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));

      List<Integer> liste = patientDao.findByNomInList(criteres, banks);
      assertTrue(liste.size() == 2);

      criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      liste = patientDao.findByNomInList(criteres, banks);
      assertTrue(liste.size() == 1);
   }

   public void testFindByNipInList(){
      List<String> criteres = new ArrayList<>();
      criteres.add("12");
      criteres.add("0987");
      criteres.add("876");
      final List<Banque> banks = new ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));

      List<Integer> liste = patientDao.findByNipInList(criteres, banks);
      assertTrue(liste.size() == 2);

      criteres = new ArrayList<>();
      criteres.add("12");
      liste = patientDao.findByNipInList(criteres, banks);
      assertTrue(liste.size() == 1);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un patient.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudPatient() throws Exception{
      final Patient p = new Patient();
      p.setNip("13z");
      p.setNom("SIMPSON");
      p.setNomNaissance("BOUVIER");
      p.setPrenom("Margery");
      final Date date = new SimpleDateFormat("dd/MM/yyyy").parse("24/03/1991");
      p.setDateNaissance(date);
      p.setVilleNaissance("Springfield");
      p.setPaysNaissance("USA");
      p.setPatientEtat("V");
      final Date etat = new SimpleDateFormat("dd/MM/yyyy").parse("28/10/2009");
      p.setDateEtat(etat);
      p.setDateDeces(null);
      p.setEtatIncomplet(false);
      p.setArchive(false);     
      // @since 2.3.0-gatsbi
//      p.setIdentifiant("gatsbi123");
      
      // Test de l'insertion
      patientDao.createObject(p);
      assertEquals(new Integer(6), p.getPatientId());

      // Test de la mise à jour
      final Patient p2 = patientDao.findById(new Integer(6));
      assertNotNull(p2);
      assertTrue(p2.getNip().equals("13z"));
      assertTrue(p2.getNom().equals("SIMPSON"));
      assertTrue(p2.getNomNaissance().equals("BOUVIER"));
      assertTrue(p2.getPrenom().equals("Margery"));
      assertTrue(p2.getDateNaissance().equals(date));
      assertTrue(p2.getVilleNaissance().equals("Springfield"));
      assertTrue(p2.getPaysNaissance().equals("USA"));
      assertTrue(p2.getPatientEtat().equals("V"));
      assertTrue(p2.getDateEtat().equals(etat));
      assertNull(p2.getDateDeces());
      assertFalse(p2.getEtatIncomplet());
      assertFalse(p2.getArchive());
//      assertTrue(p2.getIdentifiant().equals("gatsbi123"));
      
      //update
      p.setNip(null);
      p.setNom("Rey mysterio");
      p.setNomNaissance(null);
      p.setPrenom("Junior");
      p.setDateNaissance(null);
      p.setVilleNaissance(null);
      p.setPaysNaissance(null);
      p.setPatientEtat("D");
      p.setDateEtat(null);
      final Date deces = new SimpleDateFormat("dd/MM/yyyy").parse("13/10/2006");
      p.setDateDeces(deces);
      p.setEtatIncomplet(null);
      p.setArchive(null);
      // @since 2.3.0-gatsbi
//      p.setIdentifiant("gatsbi124");
      patientDao.updateObject(p2);
      assertNull(patientDao.findById(6).getNip());
      assertTrue(patientDao.findById(6).getNom().equals("Rey mysterio"));
      assertNull(patientDao.findById(6).getNomNaissance());
      assertTrue(patientDao.findById(6).getPrenom().equals("Junior"));
      assertNull(patientDao.findById(6).getDateNaissance());
      assertNull(patientDao.findById(6).getVilleNaissance());
      assertNull(patientDao.findById(6).getPaysNaissance());
      assertTrue(patientDao.findById(6).getPatientEtat().equals("D"));
      assertNull(patientDao.findById(6).getDateEtat());
      assertTrue(patientDao.findById(6).getDateDeces().equals(deces));
      assertNull(patientDao.findById(6).getEtatIncomplet());
      assertNull(patientDao.findById(6).getArchive());
//      assertTrue(patientDao.findById(6).getIdentifiant().equals("gatsbi124"));


      // Test de la délétion
      patientDao.removeObject(new Integer(6));
      assertNull(patientDao.findById(new Integer(6)));
   }

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   public void testEquals() throws ParseException{

      final Patient p1 = new Patient();
      final Patient p2 = new Patient();

      // L'objet 1 n'est pas égal à null
      assertFalse(p1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(p1.equals(p1));
      // 2 objets sont égaux entre eux
      assertTrue(p1.equals(p2));
      assertTrue(p2.equals(p1));

      populateClefsToTestEqualsAndHashCode(p1, p2);

      //dummy test
      final Banque b = new Banque();
      assertFalse(p1.equals(b));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    * @throws ParseException 
    */
   public void testHashCode() throws ParseException{

      final Patient p1 = new Patient();
      p1.setPatientId(1);
      final Patient p2 = new Patient();
      p2.setPatientId(2);
      final Patient r3 = new Patient();
      r3.setPatientId(3);

      assertTrue(p1.hashCode() == p2.hashCode());
      assertTrue(p2.hashCode() == r3.hashCode());
      assertTrue(r3.hashCode() > 0);

      //teste dans methode precedente
      //populateClefsToTestEqualsAndHashCode(p1, p2);

      final int hash = p1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(p1.hashCode() == p2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == p1.hashCode());
      assertTrue(hash == p1.hashCode());
      assertTrue(hash == p1.hashCode());
      assertTrue(hash == p1.hashCode());
   }

   private void populateClefsToTestEqualsAndHashCode(final Patient p1, final Patient p2) throws ParseException{
      final String[] noms = new String[] {null, "nom1", "nom2", "nom1"};
      final String[] prenoms = new String[] {null, "prenom1", "prenom2", "prenom1"};
      final Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse("13/10/2006");
      final Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2001");
      final Date date3 = new SimpleDateFormat("dd/MM/yyyy").parse("13/10/2006");
      final Date[] dates = new Date[] {null, date1, date2, date3};
      final String[] villes = new String[] {null, "ville1", "ville2", "ville1"};
 //     final String[] identifiants = new String[] {null, "g1", "g2", "g1"};

      for(int i = 0; i < noms.length; i++){
         for(int j = 0; j < prenoms.length; j++){
            for(int k = 0; k < dates.length; k++){
               for(int l = 0; l < villes.length; l++){
               //   for(int d = 0; d < identifiants.length; d++){
                     for(int m = 0; m < noms.length; m++){
                        for(int n = 0; n < prenoms.length; n++){
                           for(int o = 0; o < dates.length; o++){
                              for(int q = 0; q < villes.length; q++){
               //                  for(int e = 0; e < identifiants.length; e++){

                                    p1.setNom(noms[i]);
                                    p1.setPrenom(prenoms[j]);
                                    p1.setDateNaissance(dates[k]);
                                    p1.setVilleNaissance(villes[l]);
                 //                   p1.setIdentifiant(identifiants[d]);
                                    p2.setNom(noms[m]);
                                    p2.setPrenom(prenoms[n]);
                                    p2.setDateNaissance(dates[o]);
                                    p2.setVilleNaissance(villes[q]);
                 //                   p2.setIdentifiant(identifiants[e]);
                                    
                                    if(((i == m) || (i + m == 4)) && ((j == n) || (j + n == 4)) && ((k == o) || (k + o == 4))
                                       && ((l == q) || (l + q == 4) || l == 0 || q == 0)){
                                     //   && ((e == d) || (e + d == 4)) 
                                       assertTrue(p1.equals(p2));
                                       assertTrue(p1.hashCode() == p2.hashCode());
                                    }else{
                                       assertFalse(p1.equals(p2));
                                    }
                              //   }
                              }
                           }
                        }
                  //   }
                  }
               }
            }
         }
      }
   }

   public void testClone(){
      final Patient p = patientDao.findById(1);
      final Patient p2 = p.clone();
      assertTrue(p.equals(p2));
      if(p.getPatientId() != null){
         assertTrue(p.getPatientId().equals(p2.getPatientId()));
      }else{
         assertNull(p2.getPatientId());
      }
      if(p.getNip() != null){
         assertTrue(p.getNip().equals(p2.getNip()));
      }else{
         assertNull(p2.getNip());
      }
      if(p.getNom() != null){
         assertTrue(p.getNom().equals(p2.getNom()));
      }else{
         assertNull(p2.getNom());
      }
      if(p.getNomNaissance() != null){
         assertTrue(p.getNomNaissance().equals(p2.getNomNaissance()));
      }else{
         assertNull(p2.getNomNaissance());
      }
      if(p.getPrenom() != null){
         assertTrue(p.getPrenom().equals(p2.getPrenom()));
      }else{
         assertNull(p2.getPrenom());
      }
      if(p.getSexe() != null){
         assertTrue(p.getSexe().equals(p2.getSexe()));
      }else{
         assertNull(p2.getSexe());
      }
      if(p.getDateNaissance() != null){
         assertTrue(p.getDateNaissance().equals(p2.getDateNaissance()));
      }else{
         assertNull(p2.getDateNaissance());
      }
      if(p.getVilleNaissance() != null){
         assertTrue(p.getVilleNaissance().equals(p2.getVilleNaissance()));
      }else{
         assertNull(p2.getVilleNaissance());
      }
      if(p.getPaysNaissance() != null){
         assertTrue(p.getPaysNaissance().equals(p2.getPaysNaissance()));
      }else{
         assertNull(p2.getPaysNaissance());
      }
      if(p.getDateDeces() != null){
         assertTrue(p.getDateDeces().equals(p2.getDateDeces()));
      }else{
         assertNull(p2.getDateDeces());
      }
      if(p.getDateEtat() != null){
         assertTrue(p.getDateEtat().equals(p2.getDateEtat()));
      }else{
         assertNull(p2.getDateEtat());
      }
      if(p.getPatientEtat() != null){
         assertTrue(p.getPatientEtat().equals(p2.getPatientEtat()));
      }else{
         assertNull(p2.getPatientEtat());
      }
      if(p.getEtatIncomplet() != null){
         assertTrue(p.getEtatIncomplet().equals(p2.getEtatIncomplet()));
      }else{
         assertNull(p2.getEtatIncomplet());
      }
      if(p.getArchive() != null){
         assertTrue(p.getArchive().equals(p2.getArchive()));
      }else{
         assertNull(p2.getArchive());
      }
   }

   public void testFindCountMaladies(){
      Patient p = patientDao.findById(1);
      assertTrue(patientDao.findCountMaladies(p).get(0).equals(new Long(2)));
      p = patientDao.findById(2);
      assertTrue(patientDao.findCountMaladies(p).get(0).equals(new Long(0)));
   }

   /**
    * Teste en parallele findCountPrelevements 
    * et findCountPrelevementsByBanque.
    */
   public void testFindCountPrelevementsByBanqueOrNot(){
      Patient p = patientDao.findById(3);
      final Banque b = banqueDao.findById(1);
      assertTrue(patientDao.findCountPrelevements(p).get(0).equals(new Long(3)));
      assertTrue(patientDao.findCountPrelevementsByBanque(p, b).get(0).equals(new Long(2)));
      p = patientDao.findById(1);
      assertTrue(patientDao.findCountPrelevements(p).get(0).equals(new Long(1)));
      assertTrue(patientDao.findCountPrelevementsByBanque(p, b).get(0).equals(new Long(0)));
      p = patientDao.findById(2);
      assertTrue(patientDao.findCountPrelevements(p).get(0).equals(new Long(0)));
      assertTrue(patientDao.findCountPrelevementsByBanque(p, b).get(0).equals(new Long(0)));
   }

   public void testFindCountPrelevedByDatesSaisie() throws ParseException{
      final Calendar d1 = Calendar.getInstance();
      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/06/2009"));
      final Calendar d2 = Calendar.getInstance();
      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009"));
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banks.add(b1);

      List<Long> res = patientDao.findCountPrelevedByDatesSaisie(d1, d2, banks);
      assertTrue(res.get(0) == 0);

      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      res = patientDao.findCountPrelevedByDatesSaisie(d1, d2, banks);
      assertTrue(res.get(0) == 1);

      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/0001"));
      res = patientDao.findCountPrelevedByDatesSaisie(d1, d2, banks);
      assertTrue(res.get(0) == 1);

      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/11/2009"));

      res = patientDao.findCountPrelevedByDatesSaisie(d1, d2, banks);
      assertTrue(res.get(0) == 2);

      final Banque b3 = banqueDao.findById(3);
      banks.add(b3);
      res = patientDao.findCountPrelevedByDatesSaisie(d1, d2, banks);
      assertTrue(res.get(0) == 2);
   }

   public void testFindCountPrelevedByDatesPrel() throws ParseException{
      final Calendar d1 = Calendar.getInstance();
      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/06/1983"));
      final Calendar d2 = Calendar.getInstance();
      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/1983"));
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banks.add(b1);

      List<Long> res = patientDao.findCountPrelevedByDatesPrel(d1, d2, banks);
      assertTrue(res.get(0) == 1);

      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      res = patientDao.findCountPrelevedByDatesPrel(d1, d2, banks);
      assertTrue(res.get(0) == 2);

      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/0001"));
      res = patientDao.findCountPrelevedByDatesPrel(d1, d2, banks);
      assertTrue(res.get(0) == 2);

      final Banque b3 = banqueDao.findById(3);
      banks.add(b3);
      res = patientDao.findCountPrelevedByDatesPrel(d1, d2, banks);
      assertTrue(res.get(0) == 2);

      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/11/1982"));

      res = patientDao.findCountPrelevedByDatesPrel(d1, d2, banks);
      assertTrue(res.get(0) == 0);
   }

   public void testFindCountPrelevedByDatesSaisieExt() throws ParseException{
      final Calendar d1 = Calendar.getInstance();
      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009"));
      final Calendar d2 = Calendar.getInstance();
      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("03/11/2009"));
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      banks.add(b1);
      banks.add(b2);
      final List<Etablissement> etabs = new ArrayList<>();
      final Etablissement e4 = etablissementDao.findById(2);
      etabs.add(e4);
      List<Long> res = patientDao.findCountPrelevedByDatesSaisieExt(d1, d2, banks, etabs);
      assertTrue(res.get(0) == 2);

      final Etablissement e1 = etablissementDao.findById(1);
      etabs.add(e1);
      res = patientDao.findCountPrelevedByDatesSaisieExt(d1, d2, banks, etabs);
      assertTrue(res.get(0) == 0);

      res = patientDao.findCountPrelevedByDatesSaisieExt(null, d2, banks, etabs);
      assertTrue(res.get(0) == 0);
      res = patientDao.findCountPrelevedByDatesSaisieExt(d1, null, banks, etabs);
      assertTrue(res.get(0) == 0);
      res = patientDao.findCountPrelevedByDatesSaisieExt(d1, d2, banks, null);
      assertTrue(res.get(0) == 0);
   }

   public void testFindCountPrelevedByDatesPrelExt() throws ParseException{
      final Calendar d1 = Calendar.getInstance();
      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/06/1983"));
      final Calendar d2 = Calendar.getInstance();
      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/09/1983"));
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      banks.add(b1);
      banks.add(b2);
      final List<Etablissement> etabs = new ArrayList<>();
      final Etablissement e4 = etablissementDao.findById(2);
      etabs.add(e4);
      List<Long> res = patientDao.findCountPrelevedByDatesPrelExt(d1, d2, banks, etabs);
      assertTrue(res.get(0) == 1);

      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("19/09/1983"));
      res = patientDao.findCountPrelevedByDatesPrelExt(d1, d2, banks, etabs);
      assertTrue(res.get(0) == 2);

      final Etablissement e1 = etablissementDao.findById(1);
      etabs.add(e1);
      res = patientDao.findCountPrelevedByDatesPrelExt(d1, d2, banks, etabs);
      assertTrue(res.get(0) == 0);

      res = patientDao.findCountPrelevedByDatesPrelExt(null, d2, banks, etabs);
      assertTrue(res.get(0) == 0);
      res = patientDao.findCountPrelevedByDatesPrelExt(d1, null, banks, etabs);
      assertTrue(res.get(0) == 0);
      res = patientDao.findCountPrelevedByDatesPrelExt(d1, d2, banks, null);
      assertTrue(res.get(0) == 0);
   }
   
   public void testFindByIdentifiantReturnIds(){
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      List<Integer> patients = patientDao.findByIdentifiantReturnIds("SLS-1234", bks);
      assertTrue(patients.size() == 1);

      patients = patientDao.findByIdentifiantReturnIds("K12", bks);
      assertTrue(patients.size() == 0);

      patients = patientDao.findByIdentifiantReturnIds("SLS-123%", bks);
      assertTrue(patients.size() == 1);

      bks.add(banqueDao.findById(2));
      patients = patientDao.findByIdentifiantReturnIds("%", bks);
      assertTrue(patients.size() == 2);

      patients = patientDao.findByIdentifiantReturnIds(null, bks);
      assertTrue(patients.size() == 0);

      patients = patientDao.findByIdentifiantReturnIds("%", null);
      assertTrue(patients.size() == 0);
   }
   
   public void testFindByIdentifiantInList(){
      List<String> identifiants = new ArrayList<>();
      identifiants.add("SLS-1234");
      identifiants.add("MEL-889");
      identifiants.add("SLS-1255");
      final List<Banque> banks = new ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));

      List<Integer> liste = patientDao.findByIdentifiantInList(identifiants, banks);
      assertTrue(liste.size() == 2);

      identifiants.clear();
      identifiants.add("MEL-889");
      liste = patientDao.findByIdentifiantInList(identifiants, banks);
      assertTrue(liste.size() == 1);
   }
   
   public void testFindByIdentifiant(){
      String identifiant = "SLS-1234";
      final List<Banque> banks = new ArrayList<>();
      banks.add(banqueDao.findById(1));

      List<Patient> patients = patientDao.findByIdentifiant(identifiant, banks);
      assertTrue(patients.size() == 1);
      assertTrue(patients.get(0).getPatientId() == 1);
      
      identifiant = "SLS-12%";
      patients = patientDao.findByIdentifiant(identifiant, banks);
      assertTrue(patients.size() == 2);


      banks.add(banqueDao.findById(2));
      identifiant = "%";
      patients = patientDao.findByIdentifiant(identifiant, banks);
      assertTrue(patients.size() == 2);
      
      identifiant = "UNK";
      patients = patientDao.findByIdentifiant(identifiant, banks);
      assertTrue(patients.isEmpty());
   }
   
   public void testFindIdentifiantsByPatientAndBanques(){
      final List<Banque> banks = new ArrayList<>();
      banks.add(banqueDao.findById(1));

      final List<PatientIdentifiant> identifiants = patientDao
         .findIdentifiantsByPatientAndBanques(patientDao.findById(1), banks);
      assertTrue(identifiants.size() == 1);
      assertTrue(identifiants.get(0).getIdentifiant().equals("SLS-1234"));
    
      banks.add(banqueDao.findById(2));
      identifiants.clear();
      identifiants.addAll(patientDao
         .findIdentifiantsByPatientAndBanques(patientDao.findById(1), banks));
      assertTrue(identifiants.size() == 2);
      assertTrue(identifiants.get(0).getIdentifiant().equals("SLS-1234"));
      assertTrue(identifiants.get(1).getIdentifiant().equals("MEL-889"));
      
      identifiants.clear();
      identifiants.addAll(patientDao
         .findIdentifiantsByPatientAndBanques(patientDao.findById(2), banks));
      assertTrue(identifiants.size() == 1);
      assertTrue(identifiants.get(0).getIdentifiant().equals("SLS-1255"));

      // nulls
      identifiants.clear();
      identifiants.addAll(patientDao
         .findIdentifiantsByPatientAndBanques(null, banks));
      assertTrue(identifiants.isEmpty());
   }
}
