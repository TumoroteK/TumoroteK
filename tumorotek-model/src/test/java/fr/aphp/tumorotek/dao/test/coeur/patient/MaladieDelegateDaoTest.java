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

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.contexte.ContexteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.serotk.MaladieDelegate;
import fr.aphp.tumorotek.model.coeur.patient.serotk.MaladieSero;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 *
 * Classe de test pour la delegation des maladie.
 * Classe de test créée le 19/02/2012.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.6
 *
 */
public class MaladieDelegateDaoTest extends AbstractDaoTest
{

   private MaladieDao maladieDao;
   private ContexteDao contexteDao;
   private PatientDao patientDao;

   public void setContexteDao(final ContexteDao ceDao){
      this.contexteDao = ceDao;
   }

   public void setMaladieDao(final MaladieDao maladieDao){
      this.maladieDao = maladieDao;
   }

   public void setPatientDao(final PatientDao pDao){
      this.patientDao = pDao;
   }

   /**
    * Constructeur.
    */
   public MaladieDelegateDaoTest(){}

   /**
    * Test l'appel de la méthode toString().
    */
   public void testToString(){
      MaladieDelegate p1 = maladieDao.findById(4).getDelegate();
      assertTrue(p1.toString().equals("{Addiction medocs}.CONT1"));
      p1 = new MaladieDelegate();
      assertTrue(p1.toString().equals("{Empty delegate}"));
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'une maladie avec 
    * son delegate.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudMaladieAndDelegate() throws Exception{

      Maladie m = new Maladie();

      /*Champs obligatoires*/
      m.setCode("C12.13");
      m.setPatient(patientDao.findById(1));
      m.setLibelle("PROSTALGIE FUGACE");
      m.setSystemeDefaut(false);

      final MaladieSero ms1 = new MaladieSero();
      ms1.setContexte(contexteDao.findById(1));
      ms1.setDiagnostic("S");
      ms1.setMaladie(m);
      m.setDelegate(ms1);

      // Test de l'insertion
      maladieDao.createObject(m);
      assertTrue(maladieDao.findAll().size() == 7);

      m = maladieDao.findByCode("C12.13").get(0);
      assertNotNull(m.getDelegate());
      MaladieSero ms2 = (MaladieSero) m.getDelegate();
      assertTrue(ms2.getDiagnostic().equals("S"));

      ms2.setDiagnostic("C");

      maladieDao.updateObject(m);
      assertTrue(maladieDao.findAll().size() == 7);

      // Test de la mise à jour
      m = maladieDao.findByCode("C12.13").get(0);
      assertNotNull(m.getDelegate());
      ms2 = (MaladieSero) m.getDelegate();
      assertTrue(ms2.getDiagnostic().equals("C"));

      // Test de la délétion
      m.setDelegate(null);
      m.setCode("updated");
      maladieDao.updateObject(m);
      m = maladieDao.findByCode("updated").get(0);
      assertTrue(m.getDelegate() == null);

      maladieDao.removeObject(m.getMaladieId());
      assertTrue(maladieDao.findAll().size() == 6);
   }

   public void testIsEmpty(){
      final MaladieSero mSero = new MaladieSero();
      assertTrue(mSero.isEmpty());
      mSero.setDiagnostic("C");
      assertFalse(mSero.isEmpty());
      mSero.setDiagnostic("");
      assertTrue(mSero.isEmpty());
      mSero.setDiagnostic(null);
      assertTrue(mSero.isEmpty());
   }

   /**
    * Test des méthodes surchargées "equals" et hashcode pour
    * la table transcodeUtilisateur.
    */
   public void testEqualsAndHashCode(){
      final MaladieDelegate m1 = new MaladieDelegate();
      final MaladieDelegate m2 = new MaladieDelegate();
      assertFalse(m1.equals(null));
      assertNotNull(m2);
      assertTrue(m1.equals(m2));
      assertTrue(m1.equals(m2));
      assertTrue(m1.hashCode() == m2.hashCode());

      m1.setMaladie(maladieDao.findById(1));
      assertFalse(m1.equals(m2));
      assertFalse(m2.equals(m1));
      assertTrue(m1.hashCode() != m2.hashCode());
      m2.setMaladie(maladieDao.findById(2));
      assertFalse(m1.equals(m2));
      assertFalse(m2.equals(m1));
      assertTrue(m1.hashCode() != m2.hashCode());
      m1.setMaladie(maladieDao.findById(2));
      assertTrue(m1.equals(m2));
      assertTrue(m2.equals(m1));
      assertTrue(m1.hashCode() == m2.hashCode());

      // dummy
      final Categorie c = new Categorie();
      assertFalse(m1.equals(c));
   }
}
