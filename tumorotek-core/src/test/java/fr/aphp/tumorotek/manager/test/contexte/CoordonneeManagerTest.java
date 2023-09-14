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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.context.CollaborateurManager;
import fr.aphp.tumorotek.manager.context.CoordonneeManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;

/**
 *
 * Classe de test pour le manager CoordonneeManager.
 * Classe créée le 05/01/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class CoordonneeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CoordonneeManager coordonneeManager;

   @Autowired
   private CollaborateurManager collaborateurManager;

   public CoordonneeManagerTest(){}

   @Test
   public void testFindById(){
      final Coordonnee coord = coordonneeManager.findByIdManager(1);
      assertNotNull(coord);
      assertTrue(coord.getVille().equals("PARIS"));

      final Coordonnee coordNull = coordonneeManager.findByIdManager(10);
      assertNull(coordNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Coordonnee> list = coordonneeManager.findAllObjectsManager();
      assertTrue(list.size() == 5);
   }

   /**
    * Test la méthode getCollaborateursManager.
    */
   @Test
   public void testGetCollaborateursManager(){
      final Coordonnee coord1 = coordonneeManager.findByIdManager(1);
      assertNotNull(coord1);
      Set<Collaborateur> list = coordonneeManager.getCollaborateursManager(coord1);
      assertTrue(list.size() == 3);

      final Coordonnee coord2 = coordonneeManager.findByIdManager(4);
      assertNotNull(coord2);
      list = coordonneeManager.getCollaborateursManager(coord2);
      assertTrue(list.size() == 1);

      final Coordonnee coordNull = null;
      list = coordonneeManager.getCollaborateursManager(coordNull);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      final Collaborateur collab = collaborateurManager.findByIdManager(1);
      final Coordonnee coord1 = coordonneeManager.findByIdManager(1);
      final Coordonnee coord2 = coordonneeManager.findByIdManager(2);
      final Coordonnee newCoord = new Coordonnee();

      // test du doublon pour une nouvelle coordonnée
      newCoord.setAdresse(coord1.getAdresse());
      newCoord.setCp(coord1.getCp());
      newCoord.setVille(coord1.getVille());
      newCoord.setPays(coord1.getPays());
      newCoord.setTel(coord1.getTel());
      newCoord.setFax(coord1.getFax());
      newCoord.setMail(coord1.getMail());
      assertTrue(coordonneeManager.findDoublonForCollaborateurManager(newCoord, collab));

      newCoord.setAdresse(null);
      assertFalse(coordonneeManager.findDoublonForCollaborateurManager(newCoord, collab));

      // test du doublon pour une coordonnée existante
      coord2.setAdresse(coord1.getAdresse());
      coord2.setCp(coord1.getCp());
      coord2.setVille(coord1.getVille());
      coord2.setPays(coord1.getPays());
      coord2.setTel(coord1.getTel());
      coord2.setFax(coord1.getFax());
      coord2.setMail(coord1.getMail());
      assertTrue(coordonneeManager.findDoublonForCollaborateurManager(coord2, collab));

      // test sur les nulls
      assertFalse(coordonneeManager.findDoublonForCollaborateurManager(null, collab));
      assertFalse(coordonneeManager.findDoublonForCollaborateurManager(newCoord, null));
      assertFalse(coordonneeManager.findDoublonForCollaborateurManager(null, null));
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublonInList(){
      List<Coordonnee> list = new ArrayList<>();
      list.add(coordonneeManager.findByIdManager(1));
      list.add(coordonneeManager.findByIdManager(2));
      list.add(coordonneeManager.findByIdManager(3));
      assertFalse(coordonneeManager.findDoublonInListManager(list));

      list = new ArrayList<>();
      list.add(coordonneeManager.findByIdManager(1));
      list.add(coordonneeManager.findByIdManager(1));
      list.add(coordonneeManager.findByIdManager(3));
      assertTrue(coordonneeManager.findDoublonInListManager(list));

      list = new ArrayList<>();
      list.add(coordonneeManager.findByIdManager(1));
      list.add(coordonneeManager.findByIdManager(2));
      final Coordonnee newCoordonnee = new Coordonnee();
      newCoordonnee.setAdresse("TEST");
      list.add(newCoordonnee);
      assertFalse(coordonneeManager.findDoublonInListManager(list));

      list.add(newCoordonnee);
      assertTrue(coordonneeManager.findDoublonInListManager(list));
   }

   /**
    * Test la méthode isUsedByOtherObjectManager.
    */
   @Test
   public void testIsUsedByOtherObjectManager(){

      final Collaborateur collab = collaborateurManager.findByIdManager(6);
      final Coordonnee coord1 = coordonneeManager.findByIdManager(1);
      final Coordonnee coord4 = coordonneeManager.findByIdManager(4);
      final Coordonnee coord5 = coordonneeManager.findByIdManager(5);

      assertTrue(coordonneeManager.isUsedByOtherObjectManager(coord1, collab));
      assertTrue(coordonneeManager.isUsedByOtherObjectManager(coord4, collab));
      assertFalse(coordonneeManager.isUsedByOtherObjectManager(coord5, collab));

   }

   /**
    * Test d'un CRUD.
    */
   @Test
   public void testCrud(){
      // Récupération des objets associés à une Coordonnee
      final Collaborateur collab1 = collaborateurManager.findByIdManager(1);
      final Collaborateur collab2 = collaborateurManager.findByIdManager(2);
      final Collaborateur collab3 = collaborateurManager.findByIdManager(3);
      List<Collaborateur> collabs = new ArrayList<>();
      Set<Collaborateur> collabsTest = new HashSet<>();

      // Insertion
      final Coordonnee coord1 = new Coordonnee();
      // Test de la validation lors de la création
      validationInsert(coord1);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);

      // on teste une insertion valide avec les associations 
      // non obigatoires nulles
      coord1.setAdresse("TEST");
      coord1.setCp("TEST");
      coord1.setVille("TEST");
      coord1.setPays("TEST");
      coord1.setTel("TEST");
      coord1.setFax("TEST");
      coord1.setMail("TEST");
      coordonneeManager.createObjectManager(coord1, null);
      final int id1 = coord1.getCoordonneeId();
      assertTrue(coord1.getCoordonneeId() == id1);

      // on teste une insertion valide avec les associations 
      // non obigatoires remplies
      final Coordonnee coord2 = new Coordonnee();
      coord2.setAdresse("TEST2");
      coord2.setCp("TEST2");
      coord2.setVille("TEST2");
      coord2.setPays("TEST2");
      coord2.setTel("TEST2");
      coord2.setFax("TEST2");
      coord2.setMail("TEST2");
      collabs.add(collab1);
      collabs.add(collab2);
      coordonneeManager.createObjectManager(coord2, collabs);
      final int id2 = coord2.getCoordonneeId();
      assertTrue(coord2.getCoordonneeId() == id2);

      // On vérifie que toutes associations ont étées enregistrées
      final Coordonnee testInsert = coordonneeManager.findByIdManager(id2);
      // On test les attributs
      assertTrue(testInsert.getAdresse().length() > 0);
      assertTrue(testInsert.getCp().length() > 0);
      assertTrue(testInsert.getVille().length() > 0);
      assertTrue(testInsert.getPays().length() > 0);
      assertTrue(testInsert.getTel().length() > 0);
      assertTrue(testInsert.getFax().length() > 0);
      assertTrue(testInsert.getMail().length() > 0);
      assertTrue(coordonneeManager.getCollaborateursManager(testInsert).size() == 2);

      // Suppression
      final Coordonnee coord3 = coordonneeManager.findByIdManager(id1);
      coordonneeManager.removeObjectManager(coord3);
      final Coordonnee coord4 = coordonneeManager.findByIdManager(id2);
      coordonneeManager.removeObjectManager(coord4);

      // Update
      final Coordonnee coord5 = new Coordonnee();
      coord5.setAdresse("TEST");
      coordonneeManager.createObjectManager(coord5, null);
      final int id = coord5.getCoordonneeId();
      assertTrue(coord5.getCoordonneeId() == id);

      // On test la validation lors d'un update
      final Coordonnee coord7 = coordonneeManager.findByIdManager(id);
      validationUpdate(coord7);

      // On teste une mise à jour sans validation avec les 
      // assos a null
      coord7.setAdresse("         ");
      coord7.setCp("TEST3");
      coord7.setVille("TEST3");
      coord7.setPays("TEST3");
      coord7.setTel("TEST3");
      coord7.setFax("TEST3");
      coord7.setMail("TEST3");
      coordonneeManager.updateObjectManager(coord7, null, false);
      final Coordonnee coord8 = coordonneeManager.findByIdManager(id);
      assertTrue(coord8.getAdresse().equals("         "));

      // On teste une mise à jour valide avec les assos et validation
      final Coordonnee coord9 = coordonneeManager.findByIdManager(id);
      coord9.setAdresse("TEST4");
      coord9.setCp("TEST4");
      coord9.setVille("TEST4");
      coord9.setPays("TEST4");
      coord9.setTel("TEST4");
      coord9.setFax("TEST4");
      coord9.setMail("TEST4");
      collabs = new ArrayList<>();
      collabs.add(collab1);
      collabs.add(collab2);
      coordonneeManager.updateObjectManager(coord9, collabs, true);

      // On vérifie que toutes associations ont étées enregistrées
      final Coordonnee testInsert2 = coordonneeManager.findByIdManager(id);
      // On test les attributs
      assertTrue(testInsert2.getAdresse().length() > 0);
      assertTrue(testInsert2.getAdresse().equals("TEST4"));
      assertTrue(testInsert2.getCp().length() > 0);
      assertTrue(testInsert2.getVille().length() > 0);
      assertTrue(testInsert2.getPays().length() > 0);
      assertTrue(testInsert2.getTel().length() > 0);
      assertTrue(testInsert2.getFax().length() > 0);
      assertTrue(testInsert2.getMail().length() > 0);
      assertTrue(coordonneeManager.getCollaborateursManager(testInsert2).size() == 2);

      // On test la modif d'une liste de collabs
      final Coordonnee coord10 = coordonneeManager.findByIdManager(id);
      collabs = new ArrayList<>();
      collabs.add(collab3);
      coordonneeManager.updateObjectManager(coord10, collabs, true);
      // On vérifie que les associations de services ont étée modifiées
      final Coordonnee testInsert3 = coordonneeManager.findByIdManager(id);
      collabsTest = coordonneeManager.getCollaborateursManager(testInsert3);
      assertTrue(collabsTest.size() == 1);
      final Iterator<Collaborateur> it = collabsTest.iterator();
      while(it.hasNext()){
         final Collaborateur tmp = it.next();
         assertTrue(tmp.getCollaborateurId() == 3);
      }

      final Coordonnee coord11 = coordonneeManager.findByIdManager(id);
      coordonneeManager.removeObjectManager(coord11);
      assertNull(coordonneeManager.findByIdManager(id));
   }

   /**
    * Test la validation d'une coordonnee lors de sa création.
    * @param coordonnee Coordonnee à tester.
    */
   private void validationInsert(final Coordonnee coordonnee){

      boolean catchedInsert = false;
      final String[] adresseValues = new String[] {"", "  ", createOverLength(250), "GOOD"};
      final String[] cpValues = new String[] {"", " ", "%*$$€kj¤¤s", createOverLength(10), "GOOD"};
      final String[] villeValues = new String[] {"", " ", "%€$*¤¤k", createOverLength(20), "GOOD"};
      final String[] paysValues = new String[] {"", "  ", "l#$$jd¤¤%", createOverLength(20), "GOOD"};
      final String[] telValues = new String[] {"", " ", "%*k$$js¤¤", createOverLength(15), "GOOD"};
      final String[] faxValues = new String[] {"", " ", "%*€$k¤¤", createOverLength(15), "GOOD"};
      final String[] mailValues = new String[] {"", " ", "%*€$k¤¤", createOverLength(100), "GOOD"};
      for(int i = 0; i < adresseValues.length; i++){
         for(int j = 0; j < cpValues.length; j++){
            for(int k = 0; k < villeValues.length; k++){
               catchedInsert = false;
               try{
                  coordonnee.setAdresse(adresseValues[i]);
                  coordonnee.setCp(cpValues[j]);
                  coordonnee.setVille(villeValues[k]);
                  if(i != 3 || j != 4 || k != 4){ //car creation valide
                     coordonneeManager.createObjectManager(coordonnee, null);
                  }
               }catch(final Exception e){
                  if(e.getClass().getSimpleName().equals("ValidationException")){
                     catchedInsert = true;
                  }
               }
               if(i != 3 || j != 4 || k != 4){
                  assertTrue(catchedInsert);
               }
            }
         }
      }
      for(int l = 0; l < paysValues.length; l++){
         for(int m = 0; m < telValues.length; m++){
            for(int n = 0; n < faxValues.length; n++){
               for(int o = 0; o < mailValues.length; o++){
                  catchedInsert = false;
                  try{
                     coordonnee.setAdresse(null);
                     coordonnee.setCp(null);
                     coordonnee.setVille(null);
                     coordonnee.setPays(paysValues[l]);
                     coordonnee.setTel(telValues[m]);
                     coordonnee.setFax(faxValues[n]);
                     coordonnee.setMail(mailValues[o]);
                     if(l != 4 || m != 4 || n != 4 || o != 4){ //car creation valide
                        coordonneeManager.createObjectManager(coordonnee, null);
                     }
                  }catch(final Exception e){
                     if(e.getClass().getSimpleName().equals("ValidationException")){
                        catchedInsert = true;
                     }
                  }
                  if(l != 4 || m != 4 || n != 4 || o != 4){
                     assertTrue(catchedInsert);
                  }
               }
            }
         }
      }
   }

   /**
    * Test la validation d'une coordonnee lors de sa modif.
    * @param coordonnee Coordonnee à tester.
    */
   private void validationUpdate(final Coordonnee coordonnee){

      boolean catchedInsert = false;
      final String[] adresseValues = new String[] {"", "  ", createOverLength(250), "GOOD"};
      final String[] cpValues = new String[] {"", " ", "%*k$$j¤s", createOverLength(10), "GOOD"};
      final String[] villeValues = new String[] {"", " ", "%$€*¤k", createOverLength(20), "GOOD"};
      final String[] paysValues = new String[] {"", "  ", "l#$€j¤¤d%", createOverLength(20), "GOOD"};
      final String[] telValues = new String[] {"", " ", "%*ke$$j¤s", createOverLength(15), "GOOD"};
      final String[] faxValues = new String[] {"", " ", "%€€k¤", createOverLength(15), "GOOD"};
      final String[] mailValues = new String[] {"", " ", "%*$€¤k", createOverLength(100), "GOOD"};
      for(int i = 0; i < adresseValues.length; i++){
         for(int j = 0; j < cpValues.length; j++){
            for(int k = 0; k < villeValues.length; k++){
               catchedInsert = false;
               try{
                  coordonnee.setAdresse(adresseValues[i]);
                  coordonnee.setCp(cpValues[j]);
                  coordonnee.setVille(villeValues[k]);
                  if(i != 3 || j != 4 || k != 4){ //car creation valide
                     coordonneeManager.updateObjectManager(coordonnee, null, true);
                  }
               }catch(final Exception e){
                  if(e.getClass().getSimpleName().equals("ValidationException")){
                     catchedInsert = true;
                  }
               }
               if(i != 3 || j != 4 || k != 4){
                  assertTrue(catchedInsert);
               }
            }
         }
      }
      for(int l = 0; l < paysValues.length; l++){
         for(int m = 0; m < telValues.length; m++){
            for(int n = 0; n < faxValues.length; n++){
               for(int o = 0; o < mailValues.length; o++){
                  catchedInsert = false;
                  try{
                     coordonnee.setAdresse(null);
                     coordonnee.setCp(null);
                     coordonnee.setVille(null);
                     coordonnee.setPays(paysValues[l]);
                     coordonnee.setTel(telValues[m]);
                     coordonnee.setFax(faxValues[n]);
                     coordonnee.setMail(mailValues[o]);
                     if(l != 4 || m != 4 || n != 4 || o != 4){ //car creation valide
                        coordonneeManager.updateObjectManager(coordonnee, null, true);
                     }
                  }catch(final Exception e){
                     if(e.getClass().getSimpleName().equals("ValidationException")){
                        catchedInsert = true;
                     }
                  }
                  if(l != 4 || m != 4 || n != 4 || o != 4){
                     assertTrue(catchedInsert);
                  }
               }
            }
         }
      }
   }

}
