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
package fr.aphp.tumorotek.manager.test.coeur.prelevement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.RisqueManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le manager RisqueManager.
 * Classe créée le 13/10/09.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class RisqueManagerTest extends AbstractManagerTest4
{

   @Autowired
   private RisqueManager risqueManager;
   @Autowired
   private PlateformeDao plateformeDao;
   @Autowired
   private PatientDao patientDao;

   public RisqueManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<Risque> risques = risqueManager.findAllObjectsManager();
      assertTrue(risques.size() == 3);
   }

   /**
    * Test la methode findByNomLikeManager.
    */
   @Test
   public void testfindByNomLikeManager(){
      //teste une recherche exactMatch
      List<Risque> risques = risqueManager.findByNomLikeManager("HIV", true);
      assertTrue(risques.size() == 1);
      //teste une recherche non exactMatch
      risques = risqueManager.findByNomLikeManager("LEU", false);
      assertTrue(risques.size() == 1);
      //teste une recherche infructueuse
      risques = risqueManager.findByNomLikeManager("HEPAT", false);
      assertTrue(risques.size() == 0);
      //null recherche
      risques = risqueManager.findByNomLikeManager(null, false);
      assertTrue(risques.size() == 0);
   }

   /**
    * Test la methode findByInfectieuxManager.
    */
   @Test
   public void testfindByInfectieuxManager(){
      //teste une recherche fructueuse
      List<Risque> risques = risqueManager.findByInfectieuxManager(true);
      assertTrue(risques.size() == 2);
      //null recherche
      risques = risqueManager.findByInfectieuxManager(null);
      assertTrue(risques.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final Risque r1 = (risqueManager.findByNomLikeManager("HIV", true)).get(0);
      assertFalse(risqueManager.findDoublonManager(r1));
      final Risque r2 = new Risque();
      r2.setNom(r1.getNom());
      r2.setInfectieux(r1.getInfectieux());
      r2.setPlateforme(r1.getPlateforme());
      assertTrue(r2.equals(r1));
      assertTrue(risqueManager.findDoublonManager(r2));

      r1.setNom("GRIPPE A");
      assertTrue(risqueManager.findDoublonManager(r1));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObject(){
      //Enregistrement est reference
      final Risque r1 = (risqueManager.findByNomLikeManager("HIV", true)).get(0);
      assertTrue(risqueManager.isUsedObjectManager(r1));
      //Enregistrement n'est pas reference
      final Risque r2 = (risqueManager.findByNomLikeManager("GRIPPE", false)).get(0);
      assertFalse(risqueManager.isUsedObjectManager(r2));
   }

   /**
    * Teste les methodes CRUD. 
    */
   @Test
   public void testCRUD(){
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   private void createObjectManagerTest(){
      //Insertion nouvel enregistrement
      final Risque r1 = new Risque();
      r1.setNom("HEPATITE B-");
      r1.setInfectieux(false);
      r1.setPlateforme(plateformeDao.findById(1)).orElse(null);
      risqueManager.createObjectManager(r1);
      assertTrue((risqueManager.findByNomLikeManager("HEPATITE B-", true)).size() == 1);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final Risque r1Bis = new Risque();
      r1Bis.setNom("HEPATITE B-");
      r1Bis.setInfectieux(false);
      try{
         risqueManager.createObjectManager(r1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      r1Bis.setPlateforme(r1.getPlateforme());
      catched = false;
      try{
         risqueManager.createObjectManager(r1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((risqueManager.findByNomLikeManager("HEPAT%", false)).size() == 1);
      assertTrue((risqueManager.findByInfectieuxManager(false)).size() == 2);

      //validation test Type
      final String[] nomValues = new String[] {"", "  ", null, "HOO%", createOverLength(200), "goo d98-Type"};
      final Boolean[] infValues = new Boolean[] {null, false};
      final Risque r2 = new Risque();
      r2.setPlateforme(plateformeDao.findById(2)).orElse(null);
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < infValues.length; j++){
            try{
               r2.setNom(nomValues[i]);
               r2.setInfectieux(infValues[j]);
               if(i != 5 && j != 1){ //car creation valide
                  risqueManager.createObjectManager(r2);
               }
            }catch(final ValidationException e){
               //verifie qu'aucune ligne n'a ete ajoutee
               assertTrue(risqueManager.findAllObjectsManager().size() == 4);
            }
         }
      }
   }

   private void updateObjectManagerTest(){
      //Modification d'un enregistrement
      final Risque r1 = (risqueManager.findByNomLikeManager("HEPATITE B-", true)).get(0);
      r1.setNom("L'uôpuç+");
      r1.setInfectieux(true);
      risqueManager.updateObjectManager(r1);
      assertTrue((risqueManager.findByNomLikeManager("L'uôpuç+", true)).get(0).getInfectieux().equals(true));
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         r1.setNom("HIV");
         risqueManager.updateObjectManager(r1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((risqueManager.findByNomLikeManager("HIV", false)).size() == 1);

      //Validation test
      final String[] nomValues = new String[] {"", "  ", null, "|mpl1", createOverLength(200), "goo d98-Type"};
      final Boolean[] infValues = new Boolean[] {null, false};
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < infValues.length; j++){
            try{
               r1.setNom(nomValues[i]);
               r1.setInfectieux(infValues[j]);
               if(i != 4 && j != 1){ //car creation valide
                  risqueManager.updateObjectManager(r1);
               }
            }catch(final ValidationException e){
               //verifie que l'enregistrement n'a pas ete modifie
               assertTrue((risqueManager.findByNomLikeManager("L'uôpuç+", true)).get(0).getInfectieux().equals(true));
            }
         }
      }
   }

   private void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final Risque r1 = (risqueManager.findByNomLikeManager("L'uôpuç+", true)).get(0);
      risqueManager.removeObjectManager(r1);
      assertTrue((risqueManager.findByNomLikeManager("L'uôpuç+", true)).size() == 0);
      //Suppression engrendrant une exception
      //		Boolean catched = false;
      //		try {
      //			Risque r2 = (risqueManager
      //					.findByNomLikeManager("HIV", true)).get(0);
      //			risqueManager.removeObjectManager(r2);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catched = true;
      //			}
      //		}
      //		assertTrue(catched);
      assertTrue((risqueManager.findByNomLikeManager("HIV", true)).size() > 0);
      //null remove
      risqueManager.removeObjectManager(null);
      testFindAllObjectsManager();
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1).orElse(null);
      List<? extends TKThesaurusObject> list = risqueManager.findByOrderManager(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getNom().equals("GRIPPE A"));
      pf = plateformeDao.findById(2).orElse(null);
      list = risqueManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = risqueManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindByPatientAndPlateformeManager(){
      Patient pat = patientDao.findById(3).orElse(null);
      Plateforme pf = plateformeDao.findById(1).orElse(null);
      final List<Risque> risks = risqueManager.findByPatientAndPlateformeManager(pat, pf);
      assertTrue(risks.size() == 2);
      assertTrue(risks.contains(risqueManager.findByNomLikeManager("HIV", true).get(0)));
      assertTrue(risks.contains(risqueManager.findByNomLikeManager("LEUCEMIE", true).get(0)));

      pat = patientDao.findById(1).orElse(null);
      assertNull(risqueManager.findByPatientAndPlateformeManager(pat, pf));

      pat = patientDao.findById(3).orElse(null);
      pf = plateformeDao.findById(2).orElse(null);
      assertNull(risqueManager.findByPatientAndPlateformeManager(pat, pf));

      assertNull(risqueManager.findByPatientAndPlateformeManager(null, pf));
      assertNull(risqueManager.findByPatientAndPlateformeManager(pat, null));
      assertNull(risqueManager.findByPatientAndPlateformeManager(null, null));
   }
}
