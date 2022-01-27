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
package fr.aphp.tumorotek.test.dao.coeur.patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO MaladieDao et le
 * bean du domaine Maladie.
 * Classe de test créée le 28/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class MaladieDaoTest extends AbstractDaoTest
{

   @Autowired
 MaladieDao maladieDao;
   
   @Autowired
 PatientDao patientDao;
   
   /**
    * Test l'appel de la méthode toString().
    */
   @Test
public void testToString(){
      Maladie m1 = maladieDao.findById(1).get();
      assertTrue(m1.toString().equals("{" + m1.getLibelle() + "}"));
      //		assertTrue(m1.listableObjectId().equals(new Integer(1)));
      //		assertTrue(m1.entiteNom()
      //				.equals(entiteDao.findByNom("Maladie").get(0).getNom()));
      m1 = new Maladie();
      assertTrue(m1.toString().equals("{Empty Maladie}"));
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAllMaladies(){
      final List<Maladie> maladies = IterableUtils.toList(maladieDao.findAll());
      assertTrue(maladies.size() == 6);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   @Test
public void testFindByExcludedId(){
      List<Maladie> maladies = maladieDao.findByExcludedId(1, "Non precise");
      assertTrue(maladies.size() == 0);
      maladies = maladieDao.findByExcludedId(8, "Fracture");
      assertTrue(maladies.size() == 1);
      maladies = maladieDao.findByExcludedId(8, "Fracture2");
      assertTrue(maladies.size() == 0);
      maladies = maladieDao.findByExcludedId(null, null);
      assertTrue(maladies.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByLibelle().
    */
   @Test
public void testFindByLibelle(){
      List<Maladie> maladies = maladieDao.findByLibelle("Addiction%");
      assertTrue(maladies.size() == 2);
      maladies = maladieDao.findByLibelle("Grippe A");
      assertTrue(maladies.size() == 0);
      maladies = maladieDao.findByLibelle("Non precise");
      assertTrue(maladies.size() == 1);
      maladies = maladieDao.findByLibelle(null);
      assertTrue(maladies.size() == 0);
   }
   
   /**
    * Test l'appel de la méthode findByLibelle().
    * @since 2.2.3-genno
    */
   @Test
public void testFindByLibelleAndPatient() {
	  Patient pat3 = patientDao.findById(3).get();
      List<Maladie> maladies = maladieDao.findByLibelleAndPatient("Addiction%", pat3);
      assertTrue(maladies.size() == 2);
      maladies = maladieDao.findByLibelleAndPatient("Grippe A", pat3);
      assertTrue(maladies.size() == 0);
      maladies = maladieDao.findByLibelleAndPatient("Addiction%", patientDao.findById(2).get());
      assertTrue(maladies.size() == 0);
      maladies = maladieDao.findByLibelleAndPatient(null, patientDao.findById(3).get());
      assertTrue(maladies.size() == 0);
      maladies = maladieDao.findByLibelleAndPatient("Addiction%", null);
      assertTrue(maladies.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCode().
    */
   @Test
public void testFindByCode(){
      List<Maladie> maladies = maladieDao.findByCode("C%");
      assertTrue(maladies.size() == 2);
      maladies = maladieDao.findByCode("D12");
      assertTrue(maladies.size() == 0);
      maladies = maladieDao.findByCode("12.56");
      assertTrue(maladies.size() == 1);
      maladies = maladieDao.findByCode(null);
      assertTrue(maladies.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un maladie.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Test
   @Transactional
   @Rollback(false)
public void testCrudMaladie() throws Exception{
      final Maladie m = new Maladie();
      final Patient p1 = patientDao.findById(1).get();
      m.setPatient(p1);
      m.setLibelle("Grippe A");
      m.setCode("N12.5");
      final Date diag = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("24/03/2008 12:12:12");
      m.setDateDiagnostic(diag);
      final Date debut = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2004");
      m.setDateDebut(debut);
      // Test de l'insertion
      maladieDao.save(m);
      assertTrue(IterableUtils.toList(maladieDao.findAll()).size() == 7);

      // Test de la mise à jour
      final Integer mId = m.getMaladieId();
      final Maladie m2 = maladieDao.findById(mId).get();
      assertNotNull(m2);
      assertTrue(m2.getPatient().equals(p1));
      assertTrue(m2.getLibelle().equals("Grippe A"));
      assertTrue(m2.getCode().equals("N12.5"));
      assertTrue(m2.getDateDiagnostic().equals(diag));
      assertTrue(m2.getDateDebut().equals(debut));
      //update
      final Patient p2 = patientDao.findById(2).get();
      m.setPatient(p2);
      m.setLibelle("Lupus");
      m.setCode(null);
      m.setDateDiagnostic(null);
      m.setDateDebut(null);
      maladieDao.save(m2);
      assertTrue(maladieDao.findById(mId).get().getPatient().equals(p2));
      assertFalse(maladieDao.findById(mId).get().getCode() != null);
      assertTrue(maladieDao.findById(mId).get().getLibelle().equals("Lupus"));
      assertFalse(maladieDao.findById(mId).get().getDateDebut() != null);
      assertFalse(maladieDao.findById(mId).get().getDateDiagnostic() != null);

      // Test de la délétion
      maladieDao.deleteById(new Integer(mId));
      assertFalse(maladieDao.findById(new Integer(mId)).isPresent());
   }

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   @Test
public void testEquals() throws ParseException{

      final Maladie m1 = new Maladie();
      final Maladie m2 = new Maladie();

      // L'objet 1 n'est pas égal à null
      assertFalse(m1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(m1.equals(m1));
      // 2 objets sont égaux entre eux
      assertTrue(m1.equals(m2));
      assertTrue(m2.equals(m1));

      populateClefsToTestEqualsAndHashCode(m1, m2);

      //dummy test
      final Banque b = new Banque();
      assertFalse(m1.equals(b));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    * @throws ParseException 
    */
   @Test
public void testHashCode() throws ParseException{

      final Maladie m1 = new Maladie();
      m1.setMaladieId(1);
      final Maladie m2 = new Maladie();
      m2.setMaladieId(2);
      final Maladie r3 = new Maladie();
      r3.setMaladieId(3);

      assertTrue(m1.hashCode() == m2.hashCode());
      assertTrue(m2.hashCode() == r3.hashCode());
      assertTrue(r3.hashCode() > 0);

      //teste dans methode precedente
      //populateClefsToTestEqualsAndHashCode(m1, m2);

      final int hash = m1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(m1.hashCode() == m2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == m1.hashCode());
      assertTrue(hash == m1.hashCode());
      assertTrue(hash == m1.hashCode());
      assertTrue(hash == m1.hashCode());
   }

 public void populateClefsToTestEqualsAndHashCode(final Maladie m1, final Maladie m2) throws ParseException{
      final String[] libelles = new String[] {null, "libelle1", "libelle2", "libelle1"};

      final Patient p1 = patientDao.findById(1).get();
      final Patient p2 = patientDao.findById(2).get();
      final Patient p3 = patientDao.findById(1).get();
      final Patient[] patients = new Patient[] {null, p1, p2, p3};

      for(int i = 0; i < libelles.length; i++){
         for(int j = 0; j < patients.length; j++){
            for(int k = 0; k < libelles.length; k++){
               for(int l = 0; l < patients.length; l++){
                  m1.setLibelle(libelles[i]);
                  m1.setPatient(patients[j]);
                  m2.setLibelle(libelles[k]);
                  m2.setPatient(patients[l]);
                  if(((i == k) || (i + k == 4)) && ((j == l) || (j + l == 4))){
                     assertTrue(m1.equals(m2));
                     assertTrue(m1.hashCode() == m2.hashCode());
                  }else{
                     assertFalse(m1.equals(m2));
                  }
               }
            }
         }
      }
      assertTrue(m1.equals(m2));
      final Date diag1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("24/03/2008 12:12:12");
      m1.setDateDiagnostic(diag1);
      m2.setDateDiagnostic(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("24/03/2009 12:12:12"));
      assertFalse(m1.equals(m2));
      assertFalse(m1.hashCode() == m2.hashCode());

      m2.setDateDiagnostic(diag1);
      assertTrue(m1.equals(m2));
      assertTrue(m1.hashCode() == m2.hashCode());
   }

   @Test
public void testClone(){
      final Maladie m = maladieDao.findById(1).get();
      final Maladie m2 = m.clone();
      assertTrue(m.equals(m2));
      if(m.getMaladieId() != null){
         assertTrue(m.getMaladieId().equals(m2.getMaladieId()));
      }else{
         assertNull(m2.getMaladieId());
      }
      if(m.getLibelle() != null){
         assertTrue(m.getLibelle().equals(m2.getLibelle()));
      }else{
         assertNull(m2.getLibelle());
      }
      if(m.getCode() != null){
         assertTrue(m.getCode().equals(m2.getCode()));
      }else{
         assertNull(m2.getCode());
      }
      if(m.getDateDebut() != null){
         assertTrue(m.getDateDebut().equals(m2.getDateDebut()));
      }else{
         assertNull(m2.getDateDebut());

      }
      if(m.getDateDiagnostic() != null){
         assertTrue(m.getDateDiagnostic().equals(m2.getDateDiagnostic()));
      }else{
         assertNull(m2.getDateDiagnostic());
      }
      assertTrue(m.getSystemeDefaut().equals(m2.getSystemeDefaut()));
      assertTrue(m.getDelegate().equals(m2.getDelegate()));
   }

   @Test
public void testFindByPatient(){
      final Patient p = patientDao.findById(3).get();
      List<Maladie> mals = maladieDao.findByPatient(p);
      assertTrue(mals.size() == 3);
      assertTrue(mals.get(0).equals(maladieDao.findById(6).get()));
      assertTrue(mals.get(1).equals(maladieDao.findById(4).get()));
      assertTrue(mals.get(2).equals(maladieDao.findById(3).get()));
      mals = maladieDao.findByPatient(patientDao.findById(2).get());
      assertTrue(mals.size() == 0);
      mals = maladieDao.findByPatient(null);
      assertTrue(mals.size() == 0);
   }

   @Test
public void testFindByPatientNoSystem(){
      final Patient p = patientDao.findById(3).get();
      List<Maladie> mals = maladieDao.findByPatientNoSystem(p);
      assertTrue(mals.size() == 2);
      assertTrue(mals.contains(maladieDao.findById(3).get()));
      assertTrue(mals.contains(maladieDao.findById(4).get()));
      mals = maladieDao.findByPatientNoSystem(patientDao.findById(2).get());
      assertTrue(mals.size() == 0);
      mals = maladieDao.findByPatientNoSystem(null);
      assertTrue(mals.size() == 0);
   }

}
