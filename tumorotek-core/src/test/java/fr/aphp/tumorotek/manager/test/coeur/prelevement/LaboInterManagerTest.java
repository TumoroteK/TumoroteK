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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.LaboInterManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.LaboInterValidator;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 *
 * Classe de test pour le manager LaboInterManager.
 * Classe créée le 05/10/09.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class LaboInterManagerTest extends AbstractManagerTest4
{

   @Autowired
   private LaboInterManager laboInterManager;

   @Autowired
   private ServiceDao serviceDao;

   @Autowired
   private CollaborateurDao collaborateurDao;

   @Autowired
   private TransporteurDao transporteurDao;

   @Autowired
   private PrelevementDao prelevementDao;

   @Autowired
   private LaboInterValidator laboInterValidator;

   public LaboInterManagerTest(){}

   /**
    * Test la méthode findAllObjectsManager.
    */
   @Test
   public void testFindAllObjectsManager(){
      final List<LaboInter> laboInters = laboInterManager.findAllObjectsManager();
      assertTrue(laboInters.size() == 3);
   }

   /**
    * Test la methode findByServiceManager.
    */
   @Test
   public void testFindServiceManager(){
      Service s1 = serviceDao.findById(2);
      //teste une recherche fructueuse
      List<LaboInter> laboInters = laboInterManager.findByServiceManager(s1);
      assertTrue(laboInters.size() == 1);
      //teste une recherche infructueuse
      s1 = serviceDao.findById(4);
      laboInters = laboInterManager.findByServiceManager(s1);
      assertTrue(laboInters.size() == 0);
      //null recherche
      laboInters = laboInterManager.findByServiceManager(null);
      assertTrue(laboInters.size() == 0);
   }

   /**
    * Test la methode findByCollaborateurManager.
    */
   @Test
   public void testFindCollaborateurManager(){
      Collaborateur c1 = collaborateurDao.findById(2);
      //teste une recherche fructueuse
      List<LaboInter> laboInters = laboInterManager.findByCollaborateurManager(c1);
      assertTrue(laboInters.size() == 3);
      //teste une recherche infructueuse
      c1 = collaborateurDao.findById(1);
      laboInters = laboInterManager.findByCollaborateurManager(c1);
      assertTrue(laboInters.size() == 0);
      //null recherche
      laboInters = laboInterManager.findByCollaborateurManager(null);
      assertTrue(laboInters.size() == 0);
   }

   /**
    * Test la methode findByTransporteurManager.
    */
   @Test
   public void testFindTransporteurManager(){
      Transporteur t1 = transporteurDao.findById(1);
      //teste une recherche fructueuse
      List<LaboInter> laboInters = laboInterManager.findByTransporteurManager(t1);
      assertTrue(laboInters.size() == 2);
      //teste une recherche infructueuse
      t1 = transporteurDao.findById(2);
      laboInters = laboInterManager.findByTransporteurManager(t1);
      assertTrue(laboInters.size() == 0);
      //null recherche
      laboInters = laboInterManager.findByTransporteurManager(null);
      assertTrue(laboInters.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByPrelevementWithOrder().
    */
   @Test
   public void testFindByPrelevementWithOrder(){
      Prelevement p = prelevementDao.findById(1);
      List<LaboInter> labos = laboInterManager.findByPrelevementWithOrder(p);
      assertTrue(labos.size() == 3);
      assertTrue(labos.get(0).getLaboInterId() == 1);

      p = prelevementDao.findById(2);
      labos = laboInterManager.findByPrelevementWithOrder(p);
      assertTrue(labos.size() == 0);
   }

   @Test
   public void testFindDoublon(){
      final Service s1 = serviceDao.findById(2);
      //Cree le doublon
      final LaboInter l1 = (laboInterManager.findByServiceManager(s1)).get(0);
      final LaboInter l2 = new LaboInter();
      l2.setPrelevement(l1.getPrelevement());
      l2.setOrdre(l1.getOrdre());
      assertTrue(l2.equals(l1));
      assertTrue(laboInterManager.findDoublonManager(l2));

      //teste pas doublon si modification autre que clef naturelle
      l1.setConservTemp(new Float(-123.5));
      assertFalse(laboInterManager.findDoublonManager(l1));
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
      final LaboInter l1 = new LaboInter();
      final Transporteur t1 = transporteurDao.findById(2);
      final Collaborateur c1 = collaborateurDao.findById(1);
      final Prelevement p1 = prelevementDao.findById(1);
      l1.setOrdre(4);
      l1.setTransportTemp(new Float(-12.08));
      l1.setConservTemp(new Float(-10.10));
      l1.setSterile(false);
      laboInterManager.createObjectManager(l1, p1, null, c1, t1, true);
      assertTrue((laboInterManager.findByTransporteurManager(t1)).size() == 1);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      LaboInter l2 = new LaboInter();
      l2.setOrdre(3);
      l2.setSterile(false);
      try{
         laboInterManager.createObjectManager(l2, p1, null, null, null, true);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((laboInterManager.findByTransporteurManager(t1)).size() == 1);

      //Validation tests
      final Prelevement[] prelValues = new Prelevement[] {null, p1};
      final Integer[] ordreValues = new Integer[] {0, -10, null, 2};
      final Float[] conservTempValues = new Float[] {new Float(-1001.44), new Float(1003.87), null, new Float(0.08)};
      final Float[] transportTempValues = new Float[] {new Float(-1001.44), new Float(1003.87), null, new Float(0.08)};
      l2 = new LaboInter();
      l2.setSterile(false);
      for(int i = 0; i < prelValues.length; i++){
         for(int j = 0; j < ordreValues.length; j++){
            for(int k = 0; k < conservTempValues.length; k++){
               for(int g = 0; g < transportTempValues.length; g++){
                  try{
                     l2.setOrdre(ordreValues[j]);
                     l2.setConservTemp(conservTempValues[k]);
                     l2.setTransportTemp(transportTempValues[g]);
                     if(!(i == 1 && j == 3 && k > 1 && g > 1)){ //car creation valide
                        laboInterManager.createObjectManager(l2, prelValues[i], null, null, null, true);
                     }
                  }catch(final Exception e){
                     if(i == 0){
                        assertTrue(e.getClass().getSimpleName().equals("RequiredObjectIsNullException"));
                     }else{
                        assertTrue(e.getClass().getSimpleName().equals("ValidationException"));
                     }
                     //verifie qu'aucune ligne n'a ete ajoutee
                     assertTrue(laboInterManager.findAllObjectsManager().size() == 4);
                  }
               }
            }
         }
      }
   }

   private void updateObjectManagerTest(){
      //Modification d'un enregistrement
      final Service s1 = serviceDao.findById(3);
      final Transporteur t1 = transporteurDao.findById(2);
      final LaboInter l1 = (laboInterManager.findByTransporteurManager(t1)).get(0);
      final Prelevement p1 = prelevementDao.findById(3);
      l1.setOrdre(5);
      laboInterManager.updateObjectManager(l1, p1, s1, null, null, true);
      assertTrue((laboInterManager.findByServiceManager(s1)).size() == 1);
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         l1.setOrdre(1);
         final Prelevement p2 = prelevementDao.findById(1);
         laboInterManager.updateObjectManager(l1, p2, s1, null, null, true);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((laboInterManager.findByServiceManager(s1)).size() == 1);

      //Validation tests
      final Prelevement[] prelValues = new Prelevement[] {null, p1};
      final Integer[] ordreValues = new Integer[] {0, -10, null, 2};
      final Float[] conservTempValues = new Float[] {new Float(-1001.44), new Float(1003.87), null, new Float(0.08)};
      final Float[] transportTempValues = new Float[] {new Float(-1001.44), new Float(1003.87), null, new Float(0.08)};
      for(int i = 0; i < prelValues.length; i++){
         for(int j = 0; j < ordreValues.length; j++){
            for(int k = 0; k < conservTempValues.length; k++){
               for(int g = 0; g < transportTempValues.length; g++){
                  try{
                     l1.setOrdre(ordreValues[j]);
                     l1.setConservTemp(conservTempValues[k]);
                     l1.setTransportTemp(transportTempValues[g]);
                     if(i != 1 && j != 3 && k != 3 && g != 3){ //car creation valide
                        laboInterManager.updateObjectManager(l1, prelValues[i], null, null, null, true);
                     }
                  }catch(final Exception e){
                     if(i == 0){
                        assertTrue(e.getClass().getSimpleName().equals("RequiredObjectIsNullException"));
                     }else{
                        assertTrue(e.getClass().getSimpleName().equals("ValidationException"));
                     }
                     //verifie que l'enregistrement n'a pas ete modifie
                     assertTrue((laboInterManager.findByTransporteurManager(t1)).get(0).getOrdre() == 5);
                     assertTrue(
                        (laboInterManager.findByTransporteurManager(t1)).get(0).getTransportTemp().equals(new Float(-12.08)));
                     assertTrue(
                        (laboInterManager.findByTransporteurManager(t1)).get(0).getConservTemp().equals(new Float(-10.10)));
                  }
               }
            }
         }
      }
   }

   private void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final Transporteur t1 = transporteurDao.findById(2);
      final LaboInter l1 = (laboInterManager.findByTransporteurManager(t1)).get(0);
      laboInterManager.removeObjectManager(l1);
      assertTrue((laboInterManager.findByTransporteurManager(t1)).size() == 0);
      //null remove
      laboInterManager.removeObjectManager(null);
      testFindAllObjectsManager();
   }

   /**
    * Teste la validation de la date d'arrivee au labo
    * intermediaire.
    * @throws ParseException 
    */
   @Test
   public void testDateArriveeCoherence() throws ParseException{
      Prelevement p = prelevementDao.findById(3).clone();
      p.setPrelevementId(null);
      p.setDatePrelevement(null);
      final Set<LaboInter> set = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      set.add(l1);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      set.add(l2);
      final LaboInter l3 = new LaboInter();
      l3.setOrdre(3);
      set.add(l3);
      final LaboInter l4 = new LaboInter();
      l4.setOrdre(4);
      set.add(l4);
      final LaboInter l5 = new LaboInter();
      l5.setOrdre(5);
      set.add(l5);

      l3.setPrelevement(p);
      p.setLaboInters(set);

      final Calendar val1 = Calendar.getInstance();
      val1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1973"));
      final Calendar ref = Calendar.getInstance();
      ref.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1974 10:00:12"));

      final Calendar refNoHours = Calendar.getInstance();
      refNoHours.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1974"));

      final Calendar val2 = Calendar.getInstance();
      val2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1975"));

      final Calendar arrDate = Calendar.getInstance();
      arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1971"));
      l3.setDateArrivee(arrDate);
      // limites inf
      Errors errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("date.validation.infDateNaissance", errs.getFieldError().getCode());

      l3.setDateArrivee(val1);
      p.setDatePrelevement(ref);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("date.validation.infDatePrelevement", errs.getFieldError().getCode());

      l3.setDateArrivee(refNoHours);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertTrue(errs.getErrorCount() == 0);

      l3.setDateArrivee(val1);
      p.setDateDepart(ref);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("date.validation.infDateDepartPrelevement", errs.getFieldError().getCode());
      // teste recursivite n-2
      l1.setDateArrivee(ref);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("laboInter.dateArrivee.infPreviousDateArrivee", errs.getFieldError().getCode());

      l1.setDateDepart(ref);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("laboInter.dateArrivee.infPreviousDateDepart", errs.getFieldError().getCode());

      errs = null;

      l2.setDateArrivee(ref);
      l1.setDateArrivee(null);
      l1.setDateDepart(null);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("laboInter.dateArrivee.infPreviousDateArrivee", errs.getFieldError().getCode());

      l2.setDateDepart(ref);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("laboInter.dateArrivee.infPreviousDateDepart", errs.getFieldError().getCode());
      // limites sup
      arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2209"));
      l3.setDateArrivee(arrDate);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("date.validation.supDateActuelle", errs.getFieldError().getCode());

      l3.setDateArrivee(val2);
      p.setDateArrivee(ref);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("date.validation.supDateArriveePrelevement", errs.getFieldError().getCode());

      l3.setDateArrivee(refNoHours);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertTrue(errs.getErrorCount() == 0);

      // teste recursivite n+2
      l3.setDateArrivee(val2);
      l5.setDateDepart(ref);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("laboInter.dateArrivee.supNextDateDepart", errs.getFieldError().getCode());

      l5.setDateArrivee(ref);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("laboInter.dateArrivee.supNextDateArrivee", errs.getFieldError().getCode());

      l4.setDateDepart(ref);
      l5.setDateArrivee(null);
      l5.setDateDepart(null);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("laboInter.dateArrivee.supNextDateDepart", errs.getFieldError().getCode());

      l4.setDateArrivee(ref);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("laboInter.dateArrivee.supNextDateArrivee", errs.getFieldError().getCode());

      l3.setDateDepart(ref);
      errs = laboInterValidator.checkDateArriveeCoherence(l3);
      assertEquals("laboInter.dateArrivee.supDateDepart", errs.getFieldError().getCode());

      // teste laboInter date sup dateStockage d'un echantillon
      p = prelevementDao.findById(1);
      p.setDateArrivee(null);
      final List<LaboInter> labs = laboInterManager.findByPrelevementWithOrder(p);
      final LaboInter l = labs.get(2);
      l.setPrelevement(p);
      p.setLaboInters(new HashSet<>(labs));
      val1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("17/03/2008"));
      l.setDateArrivee(val1);
      l.setDateDepart(null);

      errs = laboInterValidator.checkDateArriveeCoherence(l);
      assertEquals("date.validation.supDateStockEchanEnfant", errs.getFieldError().getCode());
   }

   /**
    * Teste la validation de la date de depart du labo
    * intermediaire.
    * @throws ParseException  
    */
   @Test
   public void testDateDepartCoherence() throws ParseException{
      Prelevement p = prelevementDao.findById(3).clone();
      p.setPrelevementId(null);
      p.setDatePrelevement(null);
      final Set<LaboInter> set = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      set.add(l1);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      set.add(l2);
      final LaboInter l3 = new LaboInter();
      l3.setOrdre(3);
      set.add(l3);
      final LaboInter l4 = new LaboInter();
      l4.setOrdre(4);
      set.add(l4);
      final LaboInter l5 = new LaboInter();
      l5.setOrdre(5);
      set.add(l5);

      l3.setPrelevement(p);
      p.setLaboInters(set);

      final Calendar val1 = Calendar.getInstance();
      val1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1973"));
      final Calendar ref = Calendar.getInstance();
      ref.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1974"));
      final Calendar val2 = Calendar.getInstance();
      val2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1975"));

      final Calendar depDate = Calendar.getInstance();
      depDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1971"));
      l3.setDateDepart(depDate);
      // limites inf
      Errors errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("date.validation.infDateNaissance", errs.getFieldError().getCode());

      l3.setDateDepart(val1);
      p.setDatePrelevement(ref);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("date.validation.infDatePrelevement", errs.getFieldError().getCode());

      p.setDateDepart(ref);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("date.validation.infDateDepartPrelevement", errs.getFieldError().getCode());
      // teste recursivite n-2
      l1.setDateArrivee(ref);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("laboInter.dateDepart.infPreviousDateArrivee", errs.getFieldError().getCode());

      l1.setDateDepart(ref);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("laboInter.dateDepart.infPreviousDateDepart", errs.getFieldError().getCode());

      l2.setDateArrivee(ref);
      l1.setDateArrivee(null);
      l1.setDateDepart(null);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("laboInter.dateDepart.infPreviousDateArrivee", errs.getFieldError().getCode());

      l2.setDateDepart(ref);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("laboInter.dateDepart.infPreviousDateDepart", errs.getFieldError().getCode());

      l3.setDateArrivee(ref);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("laboInter.dateDepart.infDateArrivee", errs.getFieldError().getCode());

      // limites sup
      depDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2209"));
      l3.setDateDepart(depDate);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("date.validation.supDateActuelle", errs.getFieldError().getCode());

      l3.setDateDepart(val2);
      p.setDateArrivee(ref);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("date.validation.supDateArriveePrelevement", errs.getFieldError().getCode());

      // teste recursivite n+2
      l5.setDateDepart(ref);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("laboInter.dateDepart.supNextDateDepart", errs.getFieldError().getCode());

      l5.setDateArrivee(ref);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("laboInter.dateDepart.supNextDateArrivee", errs.getFieldError().getCode());

      l4.setDateDepart(ref);
      l5.setDateArrivee(null);
      l5.setDateDepart(null);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("laboInter.dateDepart.supNextDateDepart", errs.getFieldError().getCode());

      l4.setDateArrivee(ref);
      errs = laboInterValidator.checkDateDepartCoherence(l3);
      assertEquals("laboInter.dateDepart.supNextDateArrivee", errs.getFieldError().getCode());

      // teste laboInter date sup dateStockage d'un echantillon
      p = prelevementDao.findById(1);
      p.setDateArrivee(null);
      final List<LaboInter> labs = laboInterManager.findByPrelevementWithOrder(p);
      final LaboInter l = labs.get(2);
      l.setPrelevement(p);
      p.setLaboInters(new HashSet<>(labs));
      val1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("17/03/2008"));
      l.setDateDepart(val1);

      errs = laboInterValidator.checkDateDepartCoherence(l);
      assertEquals("date.validation.supDateStockEchanEnfant", errs.getFieldError().getCode());
   }

   @Test
   public void testSteriliteCoherence(){

      final Prelevement p = prelevementDao.findById(3);
      final Set<LaboInter> set = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      set.add(l1);
      l1.setPrelevement(p);
      l1.setSterile(null);
      p.setLaboInters(set);

      // null sterilite
      Errors errs = laboInterValidator.checkSteriliteAntecedence(l1);
      assertTrue(errs.getAllErrors().size() == 0);

      l1.setSterile(true);
      errs = laboInterValidator.checkSteriliteAntecedence(l1);
      assertEquals("sterile.validation.prelevementNotSterile", errs.getFieldError().getCode());

      // false sterilite
      l1.setSterile(false);
      errs = laboInterValidator.checkSteriliteAntecedence(l1);
      assertTrue(errs.getAllErrors().size() == 0);

      // test un labo sans precedent
      l1.setSterile(true);
      p.setSterile(false);
      errs = laboInterValidator.checkSteriliteAntecedence(l1);
      assertEquals("sterile.validation.prelevementNotSterile", errs.getFieldError().getCode());
      p.setSterile(true);
      errs = laboInterValidator.checkSteriliteAntecedence(l1);
      assertTrue(errs.getAllErrors().size() == 0);

      // test un labo precedent
      l1.setSterile(false);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(3);
      set.add(l2);
      l2.setPrelevement(p);
      p.setLaboInters(set);
      l2.setSterile(true);
      errs = laboInterValidator.checkSteriliteAntecedence(l2);
      assertEquals("sterile.validation.previousNotSterile", errs.getFieldError().getCode());
      l1.setSterile(null);
      errs = laboInterValidator.checkSteriliteAntecedence(l2);
      assertEquals("sterile.validation.previousNotSterile", errs.getFieldError().getCode());

      l1.setSterile(true);
      errs = laboInterValidator.checkSteriliteAntecedence(l2);
      assertTrue(errs.getAllErrors().size() == 0);
   }
}
