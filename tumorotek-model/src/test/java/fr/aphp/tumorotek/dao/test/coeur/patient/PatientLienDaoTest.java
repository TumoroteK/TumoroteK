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
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.patient.LienFamilialDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientLienDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.patient.LienFamilial;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientLien;
import fr.aphp.tumorotek.model.coeur.patient.PatientLienPK;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 *
 * Classe de test pour le DAO PatientLienDao et le
 * bean du domaine PatientLien.
 * Classe de test créée le 28/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class PatientLienDaoTest extends AbstractDaoTest
{

   /** Beans Dao. */
   private PatientLienDao patientLienDao;
   private PatientDao patientDao;
   private LienFamilialDao lienDao;

   /**
    * Constructeur.
    */
   public PatientLienDaoTest(){}

   /**
    * Setter du bean Dao.
    * @param mDao est le bean Dao.
    */
   public void setPatientLienDao(final PatientLienDao mDao){
      this.patientLienDao = mDao;
   }

   public void setPatientDao(final PatientDao pDao){
      this.patientDao = pDao;
   }

   public void setLienDao(final LienFamilialDao lDao){
      this.lienDao = lDao;
   }

   /**
    * Test l'appel de la méthode toString().
    */
   public void testToString(){
      final PatientLienPK pk = new PatientLienPK(patientDao.findById(2), patientDao.findById(5));
      PatientLien pl1 = patientLienDao.findById(pk);
      assertTrue(
         pl1.toString().equals("{" + pl1.getPatient1() + " - " + pl1.getLienFamilial() + " - " + pl1.getPatient2() + "}"));
      pl1 = new PatientLien();
      assertTrue(pl1.toString().equals("{Empty PatientLien}"));
      pl1.setPatient1(patientDao.findById(1));
      assertTrue(pl1.toString().equals("{Empty PatientLien}"));
      pl1.setLienFamilial(lienDao.findById(1));
      assertTrue(pl1.toString().equals("{Empty PatientLien}"));
      pl1.setPatient1(null);
      pl1.setPatient2(patientDao.findById(1));
      assertTrue(pl1.toString().equals("{Empty PatientLien}"));
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllPatientLiens(){
      final List<PatientLien> patientLiens = patientLienDao.findAll();
      assertTrue(patientLiens.size() == 2);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un patientLien.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    *
    **/
   @Rollback(false)
   public void testCrudLienFamilial(){
      final PatientLien pl = new PatientLien();
      final Patient p1 = patientDao.findById(1);
      final Patient p2 = patientDao.findById(3);
      final LienFamilial l1 = lienDao.findById(2);
      pl.setPatient1(p2);
      pl.setPatient2(p1);
      pl.setLienFamilial(l1);
      // Test de l'insertion
      patientLienDao.createObject(pl);
      assertTrue(patientLienDao.findAll().size() == 3);
      // Test de la mise à jour
      final PatientLienPK pk = new PatientLienPK();
      pk.setPatient1(p2);
      pk.setPatient2(p1);
      final PatientLien pl2 = patientLienDao.findById(pk);
      assertNotNull(pl2);
      assertTrue(pl2.getPatient1().equals(p2));
      assertTrue(pl2.getPatient2().equals(p1));
      assertTrue(pl2.getLienFamilial().equals(l1));
      //update
      final LienFamilial l2 = lienDao.findById(3);
      //pl2.setPatient1(p3);
      //pl2.setPatient2(p2);
      pl2.setLienFamilial(l2);
      patientLienDao.updateObject(pl2);
      assertTrue(patientLienDao.findById(pk).equals(pl2));
      assertTrue(patientLienDao.findById(pk).getLienFamilial().equals(l2));
      // Test de la délétion
      patientLienDao.removeObject(pk);
      assertNull(patientLienDao.findById(pk));
   }

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   public void testEquals() throws ParseException{

      final PatientLien pl1 = new PatientLien();
      final PatientLien pl2 = new PatientLien();

      // L'objet 1 n'est pas égal à null
      assertFalse(pl1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(pl1.equals(pl1));
      // 2 objets sont égaux entre eux
      assertTrue(pl1.equals(pl2));
      assertTrue(pl2.equals(pl1));

      populateClefsToTestEqualsAndHashCode(pl1, pl2);

      //dummy test
      final Banque b = new Banque();
      assertFalse(pl1.equals(b));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    * @throws ParseException 
    */
   public void testHashCode() throws ParseException{
      final PatientLien pl1 = new PatientLien();
      final PatientLien pl2 = new PatientLien();
      final PatientLien r3 = new PatientLien();

      assertTrue(pl1.hashCode() == pl2.hashCode());
      assertTrue(pl2.hashCode() == r3.hashCode());
      assertTrue(r3.hashCode() > 0);

      //teste dans methode precedente
      //populateClefsToTestEqualsAndHashCode(pl1, pl2);

      final int hash = pl1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(pl1.hashCode() == pl2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == pl1.hashCode());
      assertTrue(hash == pl1.hashCode());
      assertTrue(hash == pl1.hashCode());
      assertTrue(hash == pl1.hashCode());
   }

   private void populateClefsToTestEqualsAndHashCode(final PatientLien pl1, final PatientLien pl2) throws ParseException{

      final Patient p1 = patientDao.findById(1);
      final Patient p2 = patientDao.findById(2);
      final Patient p3 = patientDao.findById(1);
      final Patient[] patients = new Patient[] {null, p1, p2, p3};

      PatientLienPK pk1 = new PatientLienPK();
      final PatientLienPK pk2 = new PatientLienPK();

      for(int i = 0; i < patients.length; i++){
         for(int j = 0; j < patients.length; j++){
            for(int k = 0; k < patients.length; k++){
               for(int l = 0; l < patients.length; l++){
                  pk1.setPatient1(patients[i]);
                  pk1.setPatient2(patients[j]);
                  pk2.setPatient1(patients[k]);
                  pk2.setPatient2(patients[l]);
                  pl1.setPk(pk1);
                  pl2.setPk(pk2);
                  if((((i == k) || (i + k == 4)) && ((j == l) || (j + l == 4)))
                     || ((i == l) || (i + l == 4)) && ((j == k) || (j + k == 4))){
                     assertTrue(pl1.equals(pl2));
                     assertTrue(pl1.hashCode() == pl2.hashCode());
                  }else{
                     assertFalse(pl1.equals(pl2));
                  }
               }
            }
         }
      }

      //pk testing
      assertTrue(pk1.equals(pk1));
      pl2.setPk(pk1);
      assertTrue(pl1.equals(pl2));
      assertTrue(pl1.hashCode() == pl2.hashCode());
      assertFalse(pk1.equals(null));
      pk1 = null;
      pl1.setPk(pk1);
      assertFalse(pl1.equals(pl2));
      assertFalse(pl1.hashCode() == pl2.hashCode());
      final Banque b = new Banque();
      assertFalse(pk2.equals(b));
   }

}
