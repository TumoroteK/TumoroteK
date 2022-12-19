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
package fr.aphp.tumorotek.manager.test.coeur.patient;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.DiagnosticDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.serotk.MaladieSero;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager MaladieManager.
 * Classe créée le 30/10/09.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class MaladieManagerTest extends AbstractManagerTest4
{

   @Autowired
   private MaladieManager maladieManager;

   @Autowired
   private PatientManager patientManager;

   @Autowired
   private PatientDao patientDao;

   @Autowired
   private CollaborateurDao collaborateurDao;

   @Autowired
   private PrelevementDao prelevementDao;

   @Autowired
   private UtilisateurDao utilisateurDao;

   @Autowired
   private DiagnosticDao diagnosticDao;

   public MaladieManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<Maladie> mals = maladieManager.findAllObjectsManager();
      assertEquals(6, mals.size());
   }

   @Test
   public void testFindByLibelleLikeManager(){
      //teste une recherche exactMatch
      List<Maladie> mals = maladieManager.findByLibelleLikeManager("Fracture", true);
      assertTrue(mals.size() == 1);
      //teste une recherche non exactMatch
      mals = maladieManager.findByLibelleLikeManager("Addiction", false);
      assertTrue(mals.size() == 2);
      //teste une recherche infructueuse
      mals = maladieManager.findByLibelleLikeManager("Grippe", true);
      assertTrue(mals.size() == 0);
      //null recherche
      mals = maladieManager.findByLibelleLikeManager(null, false);
      assertTrue(mals.size() == 0);
   }

   @Test
   public void testFindByLibelleAndPatientManager(){
      Patient pat3 = patientDao.findById(3);
      List<Maladie> maladies = maladieManager.findByLibelleAndPatientManager("Addiction%", pat3);
      assertTrue(maladies.size() == 2);
      maladies = maladieManager.findByLibelleAndPatientManager("Grippe A", pat3);
      assertTrue(maladies.size() == 0);
      maladies = maladieManager.findByLibelleAndPatientManager("Addiction%", patientDao.findById(2));
      assertTrue(maladies.size() == 0);
      maladies = maladieManager.findByLibelleAndPatientManager(null, patientDao.findById(3));
      assertTrue(maladies.size() == 0);
      maladies = maladieManager.findByLibelleAndPatientManager("Addiction%", null);
      assertTrue(maladies.size() == 0);
   }

   @Test
   public void testFindByCodeLikeManager(){
      //teste une recherche exactMatch
      List<Maladie> mals = maladieManager.findByCodeLikeManager("12.56", true);
      assertTrue(mals.size() == 1);
      //teste une recherche non exactMatch
      mals = maladieManager.findByCodeLikeManager("C", false);
      assertTrue(mals.size() == 2);
      //teste une recherche infructueuse
      mals = maladieManager.findByCodeLikeManager("13.3", true);
      assertTrue(mals.size() == 0);
      //null recherche
      mals = maladieManager.findByCodeLikeManager(null, false);
      assertTrue(mals.size() == 0);
   }

   /**
    * @version 2.2.3-genno
    * @throws ParseException
    */
   @Test
   public void testFindDoublon() throws ParseException{
      //Cree le doublon
      // null maladie id
      final Maladie m1 = maladieManager.findByCodeLikeManager("12.56", true).get(0);
      final Maladie m2 = new Maladie();
      m2.setLibelle(m1.getLibelle());
      m2.setPatient(m1.getPatient());
      m2.setDateDiagnostic(new SimpleDateFormat("dd/MM/yyyy").parse("03/06/2009"));
      assertTrue(m2.equals(m1));
      assertTrue(maladieManager.findDoublonManager(m2, m1.getPatient()));

      // different libelle
      m2.setLibelle("NEWLIB");
      assertFalse(m2.equals(m1));
      assertFalse(maladieManager.findDoublonManager(m2, m1.getPatient()));

      // même maladie Id
      m2.setMaladieId(m1.getMaladieId());
      m2.setLibelle(m1.getLibelle());
      assertFalse(maladieManager.findDoublonManager(m2, m1.getPatient()));

      // diff maladie Id
      m2.setMaladieId(m1.getMaladieId() + 1);
      m2.setLibelle(m1.getLibelle());
      assertTrue(maladieManager.findDoublonManager(m2, m1.getPatient()));

      // patient non enregistré
      assertFalse(maladieManager.findDoublonManager(m2, new Patient()));

      // null patient
      // repose uniquement le libellé + maladieId
      m2.setMaladieId(null);
      assertTrue(maladieManager.findDoublonManager(m2, null));

      m2.setMaladieId(m1.getMaladieId());
      assertFalse(maladieManager.findDoublonManager(m2, null));

      m2.setMaladieId(m1.getMaladieId() + 1);
      assertTrue(maladieManager.findDoublonManager(m2, null));

   }

   @Test
   public void testIsUsedObject(){
      //Enregistrement est reference
      Maladie m1 = maladieManager.findByLibelleLikeManager("Addiction medocs", true).get(0);
      assertTrue(maladieManager.isUsedObjectManager(m1));
      //Enregistrement n'est pas reference
      m1 = maladieManager.findByLibelleLikeManager("Fracture", true).get(0);
      assertFalse(maladieManager.isUsedObjectManager(m1));
   }

   @Test
   public void testGetPrelevementsManager(){
      Maladie m = maladieManager.findByLibelleLikeManager("Addiction coco", true).get(0);
      assertTrue((maladieManager.getPrelevementsManager(m)).size() == 1);
      assertTrue((maladieManager.getPrelevementsManager(m)).iterator().next().equals(prelevementDao.findById(2)));
      m = maladieManager.findByLibelleLikeManager("Fracture", true).get(0);
      assertTrue((maladieManager.getPrelevementsManager(m)).size() == 0);
   }

   @Test
   public void testCRUD() throws ParseException{
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   public void createObjectManagerTest() throws ParseException{
      //Insertion nouvel enregistrement
      final Maladie m = new Maladie();
      /*Champs obligatoires*/
      final Patient p = patientDao.findById(2);
      m.setLibelle("La maladie du siecle --2009");
      m.setCode("12.12-1");
      final Date dateDiag = new SimpleDateFormat("dd/MM/yyyy").parse("17/09/2006");
      m.setDateDiagnostic(dateDiag);
      final Date dateDebut = new SimpleDateFormat("dd/MM/yyyy").parse("06/09/2000");
      m.setDateDebut(dateDebut);
      final List<Collaborateur> medecins = new ArrayList<>();
      medecins.add(collaborateurDao.findById(1));
      medecins.add(collaborateurDao.findById(2));
      final Utilisateur u = utilisateurDao.findById(1);
      //insertion
      maladieManager.createOrUpdateObjectManager(m, p, medecins, u, "creation");
      assertTrue((maladieManager.findByCodeLikeManager("12.12-1", true)).size() == 1);
      assertTrue((maladieManager.getCollaborateursManager(m)).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(m).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(m).get(0).getOperationType().getNom().equals("Creation"));
      //Insertion d'un doublon engendrant une exception
      Maladie m2 = new Maladie();
      m2.setLibelle(m.getLibelle());
      m2.setDateDiagnostic(dateDiag);
      Boolean catched = false;
      try{
         maladieManager.createOrUpdateObjectManager(m2, p, null, u, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(maladieManager.findByCodeLikeManager("12.12-1", false).size() == 1);

      //validation test Type
      final Patient[] patients = new Patient[] {null, p};
      final String[] libelles = new String[] {"", "  ", null, "12¤¤.4", createOverLength(300), "12 La poyloçite"};
      final String[] codes = new String[] {"", "  ", "12$$¤.4", createOverLength(50), null, "14GH.0"};
      m2 = new Maladie();
      for(int i = 0; i < patients.length; i++){
         for(int j = 0; j < libelles.length; j++){
            for(int k = 0; k < codes.length; k++){
               m2.setLibelle(libelles[j]);
               m2.setCode(codes[k]);
               try{
                  if(!(i == 1 && j == 5 && k > 3)){ //car creation valide
                     maladieManager.createOrUpdateObjectManager(m2, patients[i], null, u, "creation");
                  }
               }catch(final Exception e){
                  if(i == 0){
                     assertTrue(e.getClass().getSimpleName().equals("RequiredObjectIsNullException"));
                  }else{
                     assertTrue(e.getClass().getSimpleName().equals("ValidationException"));
                  }
                  //verifie qu'aucune ligne n'a ete ajoutee
                  assertEquals(7, maladieManager.findAllObjectsManager().size());
               }
            }
         }
      }

      //concordance des dates
      //date naissance solis 1974-09-03 18:00:00
      final Date avant = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/1973");
      final Date apres = new SimpleDateFormat("dd/MM/yyyy").parse("02/11/2000");
      final Date naissance = p.getDateNaissance();
      final Date deces = p.getDateDeces();
      final Date futur = new SimpleDateFormat("dd/MM/yyyy").parse("31/11/2119");
      final Date[] dates = new Date[] {futur, avant, null, apres, naissance, deces};
      //Date[] debuts = new Date[]{futur, avant, apres, null}; 
      m2.setLibelle(null);
      for(int x = 0; x < dates.length; x++){
         for(int y = 0; y < dates.length; y++){
            m2.setDateDiagnostic(dates[x]);
            m2.setDateDebut(dates[y]);
            try{
               maladieManager.createOrUpdateObjectManager(m2, null, null, null, "creation");
            }catch(final Exception e){
               assertTrue(e.getClass().getSimpleName().equals("ValidationException"));
            }
         }
      }

      m2.getPatient().setDateDeces(null);
      m2.getPatient().setDateNaissance(null);
      m2.setLibelle(null);
      try{
         maladieManager.createOrUpdateObjectManager(m2, null, null, null, "creation");
      }catch(final Exception e){
         assertTrue(e.getClass().getSimpleName().equals("ValidationException"));
      }

      m2.setLibelle("new");
      m2.setDateDebut(null);
      m2.setDateDiagnostic(null);
      // teste operation mal renseigne
      try{
         maladieManager.createOrUpdateObjectManager(m2, patients[1], null, u, null);
      }catch(final NullPointerException ne){
         assertTrue(true);
      }
      try{
         maladieManager.createOrUpdateObjectManager(m2, patients[1], null, u, "dummy");
      }catch(final IllegalArgumentException ie){
         assertTrue(true);
      }
      assertTrue(maladieManager.findAllObjectsManager().size() == 7);
   }

   public void updateObjectManagerTest() throws ParseException{
      //Modification d'un enregistrement
      final Maladie m = maladieManager.findByCodeLikeManager("12.12-1", false).get(0);
      /*Champs obligatoires*/
      m.setLibelle("Maladie grave");
      m.setCode(null);
      m.setDateDebut(null);
      m.setDateDiagnostic(null);
      final List<Collaborateur> medecins = new ArrayList<>();
      medecins.add(collaborateurDao.findById(1));
      medecins.add(collaborateurDao.findById(3));
      final Utilisateur u = utilisateurDao.findById(2);
      maladieManager.createOrUpdateObjectManager(m, null, medecins, u, "modification");
      assertNull((maladieManager.findByLibelleLikeManager("Maladie grave", true)).get(0).getCode());
      assertTrue(maladieManager.getCollaborateursManager(m).size() == 2);
      assertTrue(maladieManager.getCollaborateursManager(m).contains(collaborateurDao.findById(3)));
      assertTrue(getOperationManager().findByObjectManager(m).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(m).get(1).getOperationType().getNom().equals("Modification"));
      // supprime toutes les associations vers collaborateur
      maladieManager.createOrUpdateObjectManager(m, null, null, u, "modification");
      assertTrue(maladieManager.getCollaborateursManager(m).size() == 2);
      maladieManager.createOrUpdateObjectManager(m, null, new ArrayList<Collaborateur>(), u, "modification");
      assertTrue(maladieManager.getCollaborateursManager(m).size() == 0);
      //Modification en un doublon engendrant une exception
      final Patient p = patientDao.findById(1);
      Boolean catched = false;
      try{
         m.setLibelle("Fracture");
         m.setDateDiagnostic(new SimpleDateFormat("dd/MM/yyyy").parse("03/12/2006"));
         maladieManager.createOrUpdateObjectManager(m, p, null, u, "modification");
      }catch(final DoublonFoundException e){
         catched = true;
      }
      assertTrue(catched);
      assertTrue(maladieManager.findByLibelleLikeManager("Fracture", true).size() == 1);

      //test validation inutile car teste dans le create
   }

   /**
    * Teste la methode removeObjectManager. 
    */
   public void removeObjectManagerTest(){
      final Utilisateur u = utilisateurDao.findById(1);
      //Suppression de l'enregistrement precedemment insere
      final Maladie m1 = maladieManager.findByLibelleLikeManager("Maladie grave", true).get(0);
      maladieManager.removeObjectManager(m1, "Soignée par Gérard", u);
      assertTrue(maladieManager.findByCodeLikeManager("Maladie grave", true).size() == 0);
      assertTrue(getOperationManager().findByObjectManager(m1).size() == 0);
      //Suppression engrendrant une exception
      Boolean catched = false;
      try{
         final Maladie m2 = maladieManager.findByLibelleLikeManager("Addiction medocs", true).get(0);
         maladieManager.removeObjectManager(m2, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(maladieManager.findByLibelleLikeManager("Addiction medocs", true).size() > 0);
      //null remove
      maladieManager.removeObjectManager(null, null, null);

      // fantome verification et suppression
      assertTrue(getOperationManager().findAllObjectsManager().size() == 20);

      //verifie que l'etat des tables modifies est revenu identique
      testFindAllObjectsManager();
      final List<TKFantomableObject> fs = new ArrayList<>();
      //fs.add(p);
      fs.add(m1);
      cleanUpFantomes(fs);
   }

   @Test
   public void testCreateMaladieAndPatient() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      Patient p = new Patient();
      p.setNom("Snoop");
      p.setPrenom("Dogg");
      p.setPatientEtat("V");
      p.setArchive(false);
      p.setSexe("M");
      p.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("24/03/1978"));
      Maladie m = new Maladie();
      m.setLibelle("Deshydratation");
      maladieManager.createOrUpdateObjectManager(m, p, null, u, "creation");
      assertTrue((maladieManager.findByLibelleLikeManager("Deshydratation", true)).size() == 1);
      assertTrue((patientDao.findByNom("Snoop")).size() == 1);
      // clean up
      maladieManager.removeObjectManager(m, null, u);
      patientManager.removeObjectManager(patientDao.findByNom("Snoop").get(0), null, u, null);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p);
      fs.add(m);
      cleanUpFantomes(fs);

      // erreur de validation sur le patient
      p = new Patient();
      p.setNom("Snoop#%!$¤¤$");
      p.setPatientEtat("V");
      m = new Maladie();
      m.setLibelle("Bowse");
      try{
         maladieManager.createOrUpdateObjectManager(m, p, null, u, "creation");
      }catch(final Exception e){
         assertEquals("ValidationException", e.getClass().getSimpleName());
      }

      testFindAllObjectsManager();
      assertTrue(patientDao.findAll().size() == 5);
   }

   @Test
   public void testFindAllByPatientManager(){
      final Patient p = patientDao.findById(1);
      final List<Maladie> mals = maladieManager.findAllByPatientManager(p);
      assertTrue(mals.size() == 2);
      assertTrue(mals.get(0).equals(maladieManager.findByLibelleLikeManager("Non precise", true).get(0)));
      assertTrue(mals.get(1).equals(maladieManager.findByLibelleLikeManager("Fracture", true).get(0)));
   }

   @Test
   public void testFindByPatientNoSystemManager(){
      final Patient p = patientDao.findById(1);
      final List<Maladie> mals = maladieManager.findByPatientNoSystemManager(p);
      assertTrue(mals.size() == 2);
      assertTrue(mals.contains(maladieManager.findByLibelleLikeManager("Non precise", true).get(0)));
      assertTrue(mals.contains(maladieManager.findByLibelleLikeManager("Fracture", true).get(0)));
   }

   @Test
   public void testMaladieDelegate(){
      final Utilisateur u = utilisateurDao.findById(1);

      Maladie m = new Maladie();
      /*Champs obligatoires*/
      final Patient p = patientDao.findById(2);
      m.setLibelle("maldelegate");
      m.setCode("xde");
      MaladieSero sero = new MaladieSero();
      sero.setDiagnostic(diagnosticDao.findById(1));
      // sero.setContexte(contexteDao.findById(1));
      sero.setDelegator(m);
      m.setDelegate(sero);

      maladieManager.createOrUpdateObjectManager(m, p, null, u, "creation");
      m = maladieManager.findByCodeLikeManager("xde", true).get(0);
      assertTrue(m.getDelegate() != null);

      m.setDelegate(null);
      maladieManager.createOrUpdateObjectManager(m, null, null, u, "modification");
      m = maladieManager.findByCodeLikeManager("xde", true).get(0);
      assertTrue(m.getDelegate() == null);

      sero = new MaladieSero();
      sero.setDiagnostic(diagnosticDao.findById(1));
      // sero.setContexte(contexteDao.findById(1));
      sero.setDelegator(m);
      m.setDelegate(sero);

      maladieManager.createOrUpdateObjectManager(m, null, null, u, "modification");
      m = maladieManager.findByCodeLikeManager("xde", true).get(0);
      assertTrue(m.getDelegate() != null);
      assertTrue(((MaladieSero) m.getDelegate()).getDiagnostic().equals(diagnosticDao.findById(1)));

      maladieManager.removeObjectManager(m, null, u);

      //verifie que l'etat des tables modifies est revenu identique
      testFindAllObjectsManager();
      final List<TKFantomableObject> fs = new ArrayList<>();
      //fs.add(p);
      fs.add(m);
      cleanUpFantomes(fs);
   }
}
