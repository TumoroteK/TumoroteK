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
package fr.aphp.tumorotek.manager.test.contexte;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.context.CoordonneeManager;
import fr.aphp.tumorotek.manager.context.TransporteurManager;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager TransporteurManager.
 * Classe créée le 01/10/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class TransporteurManagerTest extends AbstractManagerTest4
{

   @Autowired
   private TransporteurManager transporteurManager;
   @Autowired
   private CoordonneeManager coordonneeManager;
   @Autowired
   private UtilisateurDao utilisateurDao;

   public TransporteurManagerTest(){}

   @Test
   public void testFindById(){
      final Transporteur transporteur = transporteurManager.findByIdManager(1);
      assertNotNull(transporteur);
      assertTrue(transporteur.getNom().equals("HOPITAL ST LOUIS - ANAPATH"));

      final Transporteur transporteurNull = transporteurManager.findByIdManager(5);
      assertNull(transporteurNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Transporteur> list = transporteurManager.findAllObjectsManager();
      assertTrue(list.size() == 3);
   }

   /**
    * Test la méthode findDoublonManager.
    */
   @Test
   public void testFindDoublonManager(){
      Transporteur t1 = new Transporteur();
      assertFalse(transporteurManager.findDoublonManager(t1));
      t1.setNom("HOPITAL ST LOUIS - ANAPATH");
      t1.setContactNom("ME STUMPERT");
      t1.setContactPrenom("RAYMONDE");
      t1.setContactTel("0142499127");
      t1.setContactFax("0142499198");
      t1.setContactMail("mail@mail.fr");
      t1.setArchive(false);
      assertTrue(transporteurManager.findDoublonManager(t1));

      t1 = transporteurManager.findByIdManager(2);
      assertFalse(transporteurManager.findDoublonManager(t1));
      t1.setNom("HOPITAL ST LOUIS - ANAPATH");
      t1.setContactNom("ME STUMPERT");
      t1.setContactPrenom("RAYMONDE");
      t1.setContactTel("0142499127");
      t1.setContactFax("0142499198");
      t1.setContactMail("mail@mail.fr");
      t1.setArchive(false);
      assertTrue(transporteurManager.findDoublonManager(t1));

      assertFalse(transporteurManager.findDoublonManager(null));
   }

   /**
    * Test l'appel de la méthode findByArchive().
    */
   @Test
   public void testFindAllActiveManager(){
      final List<Transporteur> transporteurs = transporteurManager.findAllActiveManager();
      assertTrue(transporteurs.size() == 2);
   }

   /**
    * Test d'un CRUD.
    */
   @Test
   public void testCrud(){
      createObjectManagerTest();
      updateObjectManagerTest();
   }

   private void createObjectManagerTest(){
      // Insertion
      final Transporteur t1 = new Transporteur();
      t1.setNom("HOPITAL ST LOUIS - ANAPATH");
      t1.setContactNom("ME STUMPERT");
      t1.setContactPrenom("RAYMONDE");
      t1.setContactTel("0142499127");
      t1.setContactFax("0142499198");
      t1.setContactMail("mail@mail.fr");
      t1.setArchive(false);
      Boolean catched = false;
      final Coordonnee coordExcp = new Coordonnee();
      final Utilisateur u = utilisateurDao.findById(1).orElse(null);
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         transporteurManager.createObjectManager(t1, coordExcp, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 3);

      // Test de la validation lors de la création
      validationInsert(t1);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 3);

      // Test de la validation de la coordonnée
      t1.setNom("CLERMONT");
      t1.setContactNom("PIERRE");
      final Coordonnee newCoord1 = new Coordonnee();
      newCoord1.setAdresse(createOverLength(250));
      catched = false;
      try{
         transporteurManager.createObjectManager(t1, newCoord1, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 3);

      // on teste une insertion valide avec les associations 
      // non obigatoires nulles
      transporteurManager.createObjectManager(t1, null, u);
      final int idT1 = t1.getTransporteurId();
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 4);
      assertTrue(getOperationManager().findByObjectManager(t1).size() == 1);

      // on teste une insertion valide avec une nouvelle coordonnée
      final Transporteur t2 = new Transporteur();
      t2.setNom("FEDEX");
      t2.setContactNom("MR MARTIN");
      newCoord1.setAdresse("TMP");
      transporteurManager.createObjectManager(t2, newCoord1, u);
      final int id = newCoord1.getCoordonneeId();
      final int idT2 = t2.getTransporteurId();
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 6);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 5);
      assertTrue(getOperationManager().findByObjectManager(t2).size() == 1);

      // On vérifie que toutes associations ont étées enregistrées
      final Transporteur testInsert = transporteurManager.findByIdManager(idT2);
      assertNotNull(testInsert.getCoordonnee());
      assertTrue(testInsert.getCoordonnee().getCoordonneeId() == id);
      // On test les attributs
      assertTrue(testInsert.getNom().length() > 0);
      assertTrue(testInsert.getContactNom().length() > 0);

      // Suppression
      final Transporteur t3 = transporteurManager.findByIdManager(idT1);
      transporteurManager.removeObjectManager(t3, null, u);
      final Transporteur t4 = transporteurManager.findByIdManager(idT2);
      transporteurManager.removeObjectManager(t4, null, u);
      assertNull(coordonneeManager.findByIdManager(id));
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 3);
      assertTrue(getOperationManager().findByObjectManager(t3).size() == 0);
      assertTrue(getOperationManager().findByObjectManager(t4).size() == 0);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(t3);
      fs.add(t4);
      cleanUpFantomes(fs);
   }

   private void updateObjectManagerTest(){

      final Transporteur t = new Transporteur();
      t.setNom("TEST");
      t.setContactNom("NOM");
      final Utilisateur u = utilisateurDao.findById(1).orElse(null);
      transporteurManager.createObjectManager(t, null, u);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 4);
      assertTrue(getOperationManager().findByObjectManager(t).size() == 1);
      final Integer idT = t.getTransporteurId();

      // Insertion
      final Transporteur tUp1 = transporteurManager.findByIdManager(idT);
      tUp1.setNom("HOPITAL ST LOUIS - ANAPATH");
      tUp1.setContactNom("ME STUMPERT");
      tUp1.setContactPrenom("RAYMONDE");
      tUp1.setContactTel("0142499127");
      tUp1.setContactFax("0142499198");
      tUp1.setContactMail("mail@mail.fr");
      tUp1.setArchive(false);
      Boolean catched = false;
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         transporteurManager.updateObjectManager(tUp1, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 4);

      // Test de la validation lors de la création
      validationUpdate(tUp1);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 4);

      // Test de la validation de la coordonnée
      tUp1.setNom("CLERMONT");
      tUp1.setContactNom("PIERRE");
      final Coordonnee newCoord1 = new Coordonnee();
      newCoord1.setAdresse(createOverLength(250));
      catched = false;
      try{
         transporteurManager.updateObjectManager(tUp1, newCoord1, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 4);

      // on teste une modif valide avec les associations 
      // non obigatoires nulles
      tUp1.setNom("RAAAAAAAAA");
      transporteurManager.updateObjectManager(tUp1, null, u);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 4);
      assertTrue(getOperationManager().findByObjectManager(tUp1).size() == 2);

      // on teste une insertion valide avec une nouvelle coordonnée
      final Transporteur tUp2 = transporteurManager.findByIdManager(idT);
      assertTrue(tUp2.getNom().equals("RAAAAAAAAA"));
      tUp2.setNom("FEDEX");
      tUp2.setContactNom("MR MARTIN");
      newCoord1.setAdresse("TMP");
      transporteurManager.updateObjectManager(tUp2, newCoord1, u);
      final int id = newCoord1.getCoordonneeId();
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 6);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 4);
      assertTrue(getOperationManager().findByObjectManager(tUp2).size() == 3);

      // On vérifie que toutes associations ont étées enregistrées
      final Transporteur testInsert = transporteurManager.findByIdManager(idT);
      assertNotNull(testInsert.getCoordonnee());
      assertTrue(testInsert.getCoordonnee().getCoordonneeId() == id);
      // On test les attributs
      assertTrue(testInsert.getNom().equals("FEDEX"));
      assertTrue(testInsert.getContactNom().equals("MR MARTIN"));

      // Maj de la coordonnée
      testInsert.getCoordonnee().setAdresse("LAS-BAS");
      transporteurManager.updateObjectManager(testInsert, testInsert.getCoordonnee(), u);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 6);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 4);
      final Coordonnee cTest = coordonneeManager.findByIdManager(id);
      assertTrue(cTest.getAdresse().equals("LAS-BAS"));
      assertTrue(getOperationManager().findByObjectManager(testInsert).size() == 4);

      // Suppression
      final Transporteur t3 = transporteurManager.findByIdManager(idT);
      transporteurManager.removeObjectManager(t3, null, u);
      assertNull(coordonneeManager.findByIdManager(id));
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      assertTrue(transporteurManager.findAllObjectsManager().size() == 3);
      assertTrue(getOperationManager().findByObjectManager(t3).size() == 0);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(t3);
      cleanUpFantomes(fs);
   }

   /**
    * Test la validation d'un transporteur lors de sa création.
    * @param coordonnee Coordonnee à tester.
    */
   private void validationInsert(final Transporteur transporteur){
      final Utilisateur u = utilisateurDao.findById(1).orElse(null);
      boolean catchedInsert = false;
      final String[] nomValues = new String[] {null, "", "  ", "l#$$¤jd%", createOverLength(50), "GOOD"};
      final String[] contactNomValues = new String[] {null, "", " ", "%*¤¤$$€kjs", createOverLength(50), "50"};
      final String[] contactPrenomValues = new String[] {"", " ", "%€$*k¤", createOverLength(50), "50"};
      final String[] contactTelValues = new String[] {"", " ", "%*k$$j¤s", createOverLength(15), "GOOD"};
      final String[] contactFaxValues = new String[] {"", " ", "%*€$k¤", createOverLength(15), "GOOD"};
      final String[] contactMailValues = new String[] {"", " ", "%*€$k¤", createOverLength(100), "GOOD"};
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < contactNomValues.length; j++){
            for(int k = 0; k < contactPrenomValues.length; k++){
               catchedInsert = false;
               try{
                  transporteur.setNom(nomValues[i]);
                  transporteur.setContactNom(contactNomValues[j]);
                  transporteur.setContactPrenom(contactPrenomValues[k]);
                  if(i != 5 || j != 5 || k != 4){ //car creation valide
                     transporteurManager.createObjectManager(transporteur, null, u);
                  }
               }catch(final Exception e){
                  if(e.getClass().getSimpleName().equals("ValidationException")){
                     catchedInsert = true;
                  }
               }
               if(i != 5 || j != 5 || k != 4){
                  assertTrue(catchedInsert);
               }
            }
         }
      }

      for(int m = 0; m < contactTelValues.length; m++){
         for(int n = 0; n < contactFaxValues.length; n++){
            for(int o = 0; o < contactMailValues.length; o++){
               catchedInsert = false;
               try{
                  transporteur.setNom("GOOD");
                  transporteur.setContactNom("GOOD");
                  transporteur.setContactPrenom(null);
                  transporteur.setContactTel(contactTelValues[m]);
                  transporteur.setContactFax(contactFaxValues[n]);
                  transporteur.setContactMail(contactMailValues[o]);
                  if(m != 4 || n != 4 || o != 4){
                     //car creation valide
                     transporteurManager.createObjectManager(transporteur, null, u);
                  }
               }catch(final Exception e){
                  if(e.getClass().getSimpleName().equals("ValidationException")){
                     catchedInsert = true;
                  }
               }
               if(m != 4 || n != 4 || o != 4){
                  assertTrue(catchedInsert);
               }
            }
         }
      }
   }

   /**
    * Test la validation d'un transporteur lors de sa modif.
    * @param coordonnee Coordonnee à tester.
    */
   private void validationUpdate(final Transporteur transporteur){
      final Utilisateur u = utilisateurDao.findById(1).orElse(null);
      boolean catchedUpdate = false;
      final String[] nomValues = new String[] {null, "", "  ", "l#$$jd¤%", createOverLength(50), "GOOD"};
      final String[] contactNomValues = new String[] {null, "", " ", "%*$$€kjs", createOverLength(50), "50"};
      final String[] contactPrenomValues = new String[] {"", " ", "%€$*k", createOverLength(50), "50"};
      final String[] contactTelValues = new String[] {"", " ", "%*k$$¤js", createOverLength(15), "GOOD"};
      final String[] contactFaxValues = new String[] {"", " ", "%*€$k", createOverLength(15), "GOOD"};
      final String[] contactMailValues = new String[] {"", " ", "%*€$k", createOverLength(100), "GOOD"};
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < contactNomValues.length; j++){
            for(int k = 0; k < contactPrenomValues.length; k++){
               catchedUpdate = false;
               try{
                  transporteur.setNom(nomValues[i]);
                  transporteur.setContactNom(contactNomValues[j]);
                  transporteur.setContactPrenom(contactPrenomValues[k]);
                  if(i != 5 || j != 5 || k != 4){ //car creation valide
                     transporteurManager.updateObjectManager(transporteur, null, u);
                  }
               }catch(final Exception e){
                  if(e.getClass().getSimpleName().equals("ValidationException")){
                     catchedUpdate = true;
                  }
               }
               if(i != 5 || j != 5 || k != 4){
                  assertTrue(catchedUpdate);
               }
            }
         }
      }

      for(int m = 0; m < contactTelValues.length; m++){
         for(int n = 0; n < contactFaxValues.length; n++){
            for(int o = 0; o < contactMailValues.length; o++){
               catchedUpdate = false;
               try{
                  transporteur.setNom("GOOD");
                  transporteur.setContactNom("GOOD");
                  transporteur.setContactPrenom(null);
                  transporteur.setContactTel(contactTelValues[m]);
                  transporteur.setContactFax(contactFaxValues[n]);
                  transporteur.setContactMail(contactMailValues[o]);
                  if(m != 4 || n != 4 || o != 4){
                     //car creation valide
                     transporteurManager.updateObjectManager(transporteur, null, u);
                  }
               }catch(final Exception e){
                  if(e.getClass().getSimpleName().equals("ValidationException")){
                     catchedUpdate = true;
                  }
               }
               if(m != 4 || n != 4 || o != 4){
                  assertTrue(catchedUpdate);
               }
            }
         }
      }
   }

   @Test
   public void testIsObjectReferencedManager(){
      Transporteur t = transporteurManager.findByIdManager(1);
      final Utilisateur u = utilisateurDao.findById(1).orElse(null);
      boolean catched = false;
      try{
         transporteurManager.removeObjectManager(t, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("transporteur.deletion.isReferenced"));
      }
      assertTrue(catched);

      catched = false;
      t = transporteurManager.findByIdManager(2);
      try{
         transporteurManager.removeObjectManager(t, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("transporteur.deletion.isReferenced"));
      }
      assertTrue(catched);

      catched = false;
      t = transporteurManager.findByIdManager(3);
      try{
         transporteurManager.removeObjectManager(t, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("transporteur.deletion.isReferenced"));
      }
      assertTrue(catched);
   }
}
