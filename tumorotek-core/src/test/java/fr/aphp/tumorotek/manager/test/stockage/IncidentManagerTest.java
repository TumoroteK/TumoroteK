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
package fr.aphp.tumorotek.manager.test.stockage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.manager.stockage.IncidentManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 *
 * Classe de test pour le manager IncidentManager.
 * Classe créée le 17/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class IncidentManagerTest extends AbstractManagerTest4
{

   @Autowired
   private IncidentManager incidentManager;
   @Autowired
   private ConteneurDao conteneurDao;
   @Autowired
   private EnceinteDao enceinteDao;
   @Autowired
   private TerminaleDao terminaleDao;

   public IncidentManagerTest(){}

   @Test
   public void testFindById(){
      final Incident cs1 = incidentManager.findByIdManager(1);
      assertNotNull(cs1);
      assertTrue(cs1.getNom().equals("COUPURE ELECTRIQUE"));

      final Incident csNull = incidentManager.findByIdManager(10);
      assertNull(csNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Incident> list = incidentManager.findAllObjectsManager();
      assertTrue(list.size() == 5);
   }

   /**
    * Test la méthode findAllObjectsByConteneurManager.
    */
   @Test
   public void testFindAllObjectsByConteneurManager(){
      final Conteneur c1 = conteneurDao.findById(1);
      List<Incident> list = incidentManager.findAllObjectsByConteneurManager(c1);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getNom().equals("CHUTE MATERIEL"));

      final Conteneur c2 = conteneurDao.findById(2);
      list = incidentManager.findAllObjectsByConteneurManager(c2);
      assertTrue(list.size() == 1);

      list = incidentManager.findAllObjectsByConteneurManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findAllObjectsByConteneurManager.
    */
   @Test
   public void testFindByEnceinte(){
      final Enceinte e1 = enceinteDao.findById(1);
      List<Incident> list = incidentManager.findByEnceinteManager(e1);
      assertTrue(list.size() == 1);
      assertTrue(list.get(0).getNom().equals("TIROIR EN VRAC"));

      final Enceinte e2 = enceinteDao.findById(2);
      list = incidentManager.findByEnceinteManager(e2);
      assertTrue(list.isEmpty());

      list = incidentManager.findByEnceinteManager(null);
      assertTrue(list.isEmpty());
   }

   /**
    * Test la méthode findAllObjectsByConteneurManager.
    */
   @Test
   public void testFindByTerminale(){
      final Terminale t1 = terminaleDao.findById(1);
      List<Incident> list = incidentManager.findByTerminaleManager(t1);
      assertTrue(list.isEmpty());

      final Terminale t3 = terminaleDao.findById(3);
      list = incidentManager.findByTerminaleManager(t3);
      assertTrue(list.size() == 1);
      assertTrue(list.get(0).getNom().equals("BOITE PAR TERRE"));

      list = incidentManager.findByTerminaleManager(null);
      assertTrue(list.isEmpty());
   }

   /**
    * Test la méthode findDoublonManager.
    * @throws ParseException 
    */
   @Test
   public void testFindDoublonManager() throws ParseException{
      final String nom1 = "CHUTE MATERIEL";
      final String nom2 = "INCIDENT";
      final Date d1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("10/05/2009 10:15:00");
      final Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
      final Conteneur c1 = conteneurDao.findById(1);

      final Incident i1 = new Incident();
      i1.setNom(nom1);
      assertFalse(incidentManager.findDoublonManager(i1));
      i1.setDate(d1);
      assertFalse(incidentManager.findDoublonManager(i1));
      i1.setConteneur(c1);
      assertTrue(incidentManager.findDoublonManager(i1));

      i1.setNom(nom2);
      assertFalse(incidentManager.findDoublonManager(i1));
      i1.setNom(nom1);
      i1.setDate(d2);
      assertFalse(incidentManager.findDoublonManager(i1));
      i1.setDate(d1);
      i1.setConteneur(null);
      assertFalse(incidentManager.findDoublonManager(i1));

      final Incident i2 = incidentManager.findByIdManager(1);
      assertFalse(incidentManager.findDoublonManager(i2));

      i2.setNom(nom1);
      assertFalse(incidentManager.findDoublonManager(i2));
      i2.setDate(d1);
      assertTrue(incidentManager.findDoublonManager(i2));

      i1.setNom("TIROIR EN VRAC");
      i1.setDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("12/08/2009 15:00:00"));
      assertFalse(incidentManager.findDoublonManager(i1));
      i1.setEnceinte(enceinteDao.findById(1));
      i1.setConteneur(null);
      assertTrue(incidentManager.findDoublonManager(i1));

      i2.setNom("TIROIR EN VRAC");
      i2.setDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("12/08/2009 15:00:00"));
      i2.setEnceinte(enceinteDao.findById(1));
      i2.setConteneur(null);
      assertTrue(incidentManager.findDoublonManager(i2));

      i1.setNom("BOITE PAR TERRE");
      i1.setDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("12/10/2013 06:06:06"));
      assertFalse(incidentManager.findDoublonManager(i1));
      i1.setEnceinte(null);
      i1.setTerminale(terminaleDao.findById(3));
      assertTrue(incidentManager.findDoublonManager(i1));

      i2.setNom("BOITE PAR TERRE");
      i2.setDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("12/10/2013 06:06:06"));
      i2.setEnceinte(null);
      i2.setTerminale(terminaleDao.findById(3));
      assertTrue(incidentManager.findDoublonManager(i2));

      assertFalse(incidentManager.findDoublonManager(null));
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublonInList(){
      List<Incident> list = new ArrayList<>();
      list.add(incidentManager.findByIdManager(1));
      list.add(incidentManager.findByIdManager(2));
      list.add(incidentManager.findByIdManager(3));
      assertFalse(incidentManager.findDoublonInListManager(list));

      list = new ArrayList<>();
      list.add(incidentManager.findByIdManager(1));
      list.add(incidentManager.findByIdManager(1));
      list.add(incidentManager.findByIdManager(3));
      assertTrue(incidentManager.findDoublonInListManager(list));

      list = new ArrayList<>();
      list.add(incidentManager.findByIdManager(1));
      list.add(incidentManager.findByIdManager(2));
      final Incident newIncident = new Incident();
      newIncident.setNom("TEST");
      list.add(newIncident);
      assertFalse(incidentManager.findDoublonInListManager(list));

      list.add(newIncident);
      assertTrue(incidentManager.findDoublonInListManager(list));
   }

   /**
    * Test le CRUD d'un ProtocoleExt.
    * @throws ParseException 
    */
   @Test
   public void testCrud() throws ParseException{
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   private void createObjectManagerTest() throws ParseException{

      final Conteneur c = conteneurDao.findById(1);

      final Incident i1 = new Incident();
      i1.setNom("TMP");

      Boolean catched = false;
      // on test l'insertion avec la banque nulle
      try{
         incidentManager.createObjectManager(i1, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(incidentManager.findAllObjectsManager().size() == 5);

      // on test l'insertion d'un doublon
      i1.setNom("CHUTE MATERIEL");
      final Date d1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("10/05/2009 10:15:00");
      i1.setDate(d1);
      try{
         incidentManager.createObjectManager(i1, c, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(incidentManager.findAllObjectsManager().size() == 5);

      // Test de la validation lors de la création
      try{
         validationInsert(i1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(incidentManager.findAllObjectsManager().size() == 5);

      // On test une insertion valide pour un conteneur
      final Date date = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
      i1.setNom("INCIDENT");
      i1.setDate(date);
      i1.setDescription("DESC");
      incidentManager.createObjectManager(i1, c, null, null);
      assertTrue(incidentManager.findAllObjectsManager().size() == 6);
      final int id = i1.getIncidentId();

      // Vérification
      final Incident iTest = incidentManager.findByIdManager(id);
      assertNotNull(iTest);
      assertNotNull(iTest.getConteneur());
      assertNull(iTest.getEnceinte());
      assertNull(iTest.getTerminale());
      assertTrue(iTest.getNom().equals("INCIDENT"));
      assertTrue(iTest.getDescription().equals("DESC"));
      assertTrue(iTest.getDate().equals(date));

      // Suppression
      incidentManager.removeObjectManager(iTest);
      assertTrue(incidentManager.findAllObjectsManager().size() == 5);
   }

   private void updateObjectManagerTest() throws ParseException{

      final Enceinte e1 = enceinteDao.findById(1);

      final Incident i = new Incident();
      i.setNom("TMP");
      final Date date = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
      i.setDate(date);
      incidentManager.createObjectManager(i, null, e1, null);
      assertTrue(incidentManager.findAllObjectsManager().size() == 6);
      final int id = i.getIncidentId();

      final Incident iUp = incidentManager.findByIdManager(id);
      assertTrue(incidentManager.findByEnceinteManager(e1).size() == 2);
      assertNull(iUp.getConteneur());
      assertNull(iUp.getTerminale());
      Boolean catched = false;
      // on test l'update avec la banque nulle
      try{
         incidentManager.updateObjectManager(iUp, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(incidentManager.findAllObjectsManager().size() == 6);

      // on test l'update d'un doublon
      iUp.setNom("TIROIR EN VRAC");
      final Date d1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("12/08/2009 15:00:00");
      iUp.setDate(d1);
      try{
         incidentManager.updateObjectManager(iUp, null, e1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(incidentManager.findAllObjectsManager().size() == 6);

      // Test de la validation lors de l'update
      try{
         validationUpdate(iUp);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(incidentManager.findAllObjectsManager().size() == 6);

      // On test un update valide
      final Date dateUp = new SimpleDateFormat("dd/MM/yyyy").parse("10/09/2009");
      iUp.setNom("INCIDENT BOITE");
      iUp.setDate(dateUp);
      iUp.setDescription("DESC");
      incidentManager.updateObjectManager(iUp, null, null, terminaleDao.findById(1));
      assertTrue(incidentManager.findAllObjectsManager().size() == 6);

      // Vérification
      final Incident iUpTest = incidentManager.findByIdManager(id);
      assertNotNull(iUpTest);
      assertNotNull(iUpTest.getTerminale());
      assertNull(iUpTest.getEnceinte());
      assertTrue(iUpTest.getNom().equals("INCIDENT BOITE"));
      assertTrue(iUpTest.getDescription().equals("DESC"));
      assertTrue(iUpTest.getDate().equals(dateUp));

      // Suppression
      incidentManager.removeObjectManager(iUpTest);
      assertTrue(incidentManager.findAllObjectsManager().size() == 5);
   }

   private void removeObjectManagerTest(){
      // test de la suppression d'un objet null
      incidentManager.removeObjectManager(null);
      assertTrue(incidentManager.findAllObjectsManager().size() == 5);
   }

   /**
    * Test la validation d'un Incident lors de sa création.
    * @param incident Incident à tester.
    * @throws ParseException 
    */
   private void validationInsert(final Incident incident) throws ParseException{

      final Conteneur c = conteneurDao.findById(1);
      boolean catchedInsert = false;
      // On teste une insertion avec un attribut nom non valide
      final String[] emptyValues = new String[] {"", "  ", null, "%$$*g¤¤d", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            final Date d = new SimpleDateFormat("dd/MM/yyyy").parse("10/05/200");
            incident.setNom(emptyValues[i]);
            incident.setDate(d);
            incidentManager.createObjectManager(incident, c, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      incident.setNom("test");

      // On teste une insertion avec un attribut date non valide
      Date d = new SimpleDateFormat("dd/MM/yyyy").parse("10/05/2040");
      final Date[] dateValues = new Date[] {null, d};
      for(int i = 0; i < dateValues.length; i++){
         catchedInsert = false;
         try{
            incident.setDate(dateValues[i]);
            incidentManager.createObjectManager(incident, c, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      d = new SimpleDateFormat("dd/MM/yyyy").parse("10/05/2000");
      incident.setDate(d);
   }

   /**
    * Test la validation d'un Incident lors de sa modif.
    * @param incident Incident à tester.
    * @throws ParseException 
    */
   private void validationUpdate(final Incident incident) throws ParseException{

      final Conteneur c = conteneurDao.findById(1);
      boolean catched = false;
      // On teste un update avec un attribut nom non valide
      final String[] emptyValues = new String[] {"", "  ", null, "%$$*gd¤¤", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            final Date d = new SimpleDateFormat("dd/MM/yyyy").parse("10/05/200");
            incident.setNom(emptyValues[i]);
            incident.setDate(d);
            incidentManager.updateObjectManager(incident, c, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      incident.setNom("test");

      // On teste un update avec un attribut date non valide
      Date d = new SimpleDateFormat("dd/MM/yyyy").parse("10/05/2040");
      final Date[] dateValues = new Date[] {null, d};
      for(int i = 0; i < dateValues.length; i++){
         catched = false;
         try{
            incident.setDate(dateValues[i]);
            incidentManager.updateObjectManager(incident, c, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      d = new SimpleDateFormat("dd/MM/yyyy").parse("10/05/200");
      incident.setDate(d);
   }

}
