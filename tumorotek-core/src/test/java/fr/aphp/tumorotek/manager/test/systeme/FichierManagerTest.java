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
package fr.aphp.tumorotek.manager.test.systeme;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.annotation.AnnotationValeurDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.manager.systeme.FichierManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.systeme.Fichier;

/**
 *
 * Classe de test pour le manager CrAnapathManager.
 * Classe créée le 24/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class FichierManagerTest extends AbstractManagerTest4
{

   @Autowired
   private FichierManager fichierManager;
   @Autowired
   private AnnotationValeurDao annotationValeurDao;
   @Autowired
   private EchantillonDao echantillonDao;

   public FichierManagerTest(){}

   @Test
   public void testFindById(){
      final Fichier path = fichierManager.findByIdManager(1);
      assertNotNull(path);
      assertTrue(path.getPath().equals("PATH1"));
      final Fichier pathNull = fichierManager.findByIdManager(5);
      assertNull(pathNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Fichier> list = fichierManager.findAllObjectsManager();
      assertTrue(list.size() == 4);
   }

   /**
    * Test la méthode findByCrAnapathLikeManager.
    */
   @Test
   public void testFindByCrAnapathLikeExactManager(){
      List<Fichier> list = fichierManager.findByPathLikeManager("PATH1", true);
      assertTrue(list.size() == 2);

      list = fichierManager.findByPathLikeManager("PATH", true);
      assertTrue(list.size() == 0);

      list = fichierManager.findByPathLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode isUsedObject.
    */
   @Test
   public void testIsUsed(){
      final Fichier path1 = fichierManager.findByIdManager(1);
      assertNotNull(path1);
      assertTrue(fichierManager.isUsedObjectManager(path1));

      final Fichier path2 = fichierManager.findByIdManager(3);
      assertFalse(fichierManager.isUsedObjectManager(path2));
   }

   @Test
   public void testIsPathShared(){
      final Fichier path1 = fichierManager.findByIdManager(1);
      assertNotNull(path1);
      assertTrue(fichierManager.isPathSharedManager(path1));

      final Fichier path2 = fichierManager.findByIdManager(2);
      assertFalse(fichierManager.isPathSharedManager(path2));

      assertFalse(fichierManager.isPathSharedManager(null));
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      final Fichier file = new Fichier();
      assertFalse(fichierManager.findDoublonManager(file));
      file.setFichierId(1);
      assertFalse(fichierManager.findDoublonManager(file));

      file.setPath("PATH110");
      assertFalse(fichierManager.findDoublonManager(file));
      file.setFichierId(null);
      assertFalse(fichierManager.findDoublonManager(file));

      file.setPath("PATH1");
      file.setFichierId(null);
      assertFalse(fichierManager.findDoublonManager(file));

      file.setAnnotationValeur(null);
      file.setEchantillon(echantillonDao.findById(2));
      assertTrue(fichierManager.findDoublonManager(file));

      file.setEchantillon(echantillonDao.findById(1));
      file.setPath("PATH1");
      file.setFichierId(1);
      assertFalse(fichierManager.findDoublonManager(file));

      file.setEchantillon(echantillonDao.findById(2));
      file.setFichierId(2);
      file.setPath("PATH1");
      assertTrue(fichierManager.findDoublonManager(file));

      file.setPath("PATH1");
      file.setFichierId(10);
      assertTrue(fichierManager.findDoublonManager(file));

      file.setPath("PATH10");
      assertFalse(fichierManager.findDoublonManager(file));
   }

   /**
    * Test d'un CRUD.
    */
   @Test
   public void testCrud(){
      final List<File> filesCreated = new ArrayList<>();
      assertFalse(new File("/tmp/path_TK").exists());
      // Test de l'insertion
      final Fichier path1 = new Fichier();
      path1.setPath("PATH1");
      path1.setNom("xavi");
      path1.setMimeType("application/octet-stream");
      String tmp = "abcdefghijklmnopqrstuvwxyz";
      byte[] byteArray = tmp.getBytes();
      ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
      Boolean catchedInsert = false;
      //		// On teste l'insertion d'un doublon
      //		try {
      //			fichierManager.createObjectManager(path1, bais);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"DoublonFoundException")) {
      //				catchedInsert = true;
      //			}
      //		}
      //		path1.setFichierId(null);
      //		assertTrue(catchedInsert);
      // On teste une insertion avec un attribut path non valide
      final String[] emptyValues = new String[] {"", "  ", null, "*/uh", createOverLength(250)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            path1.setPath(emptyValues[i]);
            fichierManager.createObjectManager(path1, bais, filesCreated);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }

      assertTrue(fichierManager.findAllObjectsManager().size() == 4);
      assertFalse(new File("PATH1").exists());
      assertTrue(filesCreated.isEmpty());

      // On teste la creation avec un stream vide
      path1.setPath("/tmp/path_TK");

      catchedInsert = false;
      try{
         fichierManager.createObjectManager(path1, null, filesCreated);
      }catch(final RuntimeException re){
         assertTrue(re.getMessage().equals("fichier.path.illegal"));
         catchedInsert = true;
      }
      assertTrue(catchedInsert);

      assertTrue(fichierManager.findAllObjectsManager().size() == 4);
      assertFalse(new File("/tmp/path_TK").exists());
      assertTrue(filesCreated.isEmpty());

      //		try {
      //			fichierManager.createObjectManager(path1, null);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"RuntimeException")) {
      //				catchedInsert = true;
      //			}
      //		}
      //		assertTrue(catchedInsert);
      assertFalse(new File("/tmp/path_TK").exists());
      // On test une insertion valide		
      fichierManager.createObjectManager(path1, bais, filesCreated);
      assertTrue(path1.getPath().equals("/tmp/path_TK_" + path1.getFichierId()));
      assertTrue(new File(path1.getPath()).exists());
      assertTrue(filesCreated.size() == 1);
      assertTrue(new File(path1.getPath()).length() == 26);

      Integer fId = path1.getFichierId();
      // Test de la mise à jour
      Fichier path2 = fichierManager.findByIdManager(fId);
      // On teste l'update d'un doublon
      path2.setPath("PATH1");
      Boolean catchedUpdate = false;
      //		try {
      //			fichierManager.updateObjectManager(path2, null);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"DoublonFoundException")) {
      //				catchedUpdate = true;
      //			}
      //		}
      //		assertTrue(catchedUpdate);

      final List<File> filesToDelete = new ArrayList<>();

      path2.setPath("/tmp/path_TK");
      // On teste une modif avec l'attribut nom non valide
      for(int i = 0; i < emptyValues.length; i++){
         catchedUpdate = false;
         try{
            path2.setNom(emptyValues[i]);
            fichierManager.updateObjectManager(path2, null, filesCreated, filesToDelete);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedUpdate = true;
            }
         }
         assertTrue(catchedUpdate);
      }
      assertTrue(filesCreated.size() == 1);
      assertTrue(filesToDelete.size() == 0);

      // On teste une modif avec l'attribut path non valide
      for(int i = 0; i < emptyValues.length; i++){
         catchedUpdate = false;
         try{
            path2.setPath(emptyValues[i]);
            fichierManager.updateObjectManager(path2, null, filesCreated, filesToDelete);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedUpdate = true;
            }
         }
         assertTrue(catchedUpdate);
      }
      assertTrue(filesCreated.size() == 1);
      assertTrue(filesToDelete.size() == 0);

      // On teste une mise à jour valide du nom
      path2.setNom("alonzo");
      path2.setPath("/tmp/path_TK_" + fId);
      fichierManager.updateObjectManager(path2, null, filesCreated, filesToDelete);
      assertTrue(fichierManager.findByIdManager(fId).getNom().equals("alonzo"));
      assertTrue(path2.getPath().equals("/tmp/path_TK_" + fId));
      assertTrue(new File(path2.getPath()).exists());
      assertTrue(new File(path2.getPath()).length() == 26);
      assertTrue(filesCreated.size() == 1);
      assertTrue(filesToDelete.size() == 0);

      // On teste une mise à jour valide du stream
      // implique suppression ancien fichier
      // creation nouveau fichier + nouvel reference Fichier
      tmp = "viva espana";
      byteArray = tmp.getBytes();
      bais = new ByteArrayInputStream(byteArray);
      fichierManager.updateObjectManager(path2, bais, filesCreated, filesToDelete);
      assertNull(fichierManager.findByIdManager(fId));
      path2 = fichierManager.findByPathLikeManager("/tmp/path_TK_", false).get(0);
      assertFalse(path2.getPath().equals("/tmp/path_TK_" + fId));
      assertTrue(fichierManager.findByIdManager(fId) == null);
      assertTrue(path2.getFichierId() > fId);
      assertTrue(path2.getPath().equals("/tmp/path_TK_" + path2.getFichierId()));
      assertTrue(new File(path2.getPath()).exists());
      assertTrue(new File(path2.getPath()).length() == 11);
      assertTrue(filesCreated.size() == 2);
      assertTrue(filesToDelete.size() == 1);
      // l'ancien fichier est envoyé à la suppression
      assertTrue(new File("/tmp/path_TK_" + fId).exists());
      filesToDelete.get(0).delete();
      assertFalse(new File("/tmp/path_TK_" + fId).exists());
      filesToDelete.clear();

      // Test de la suppression reference en base
      fId = path2.getFichierId();
      final Fichier path4 = fichierManager.findByIdManager(path2.getFichierId());
      fichierManager.removeObjectManager(path4, filesToDelete);
      assertNull(fichierManager.findByIdManager(fId));
      assertTrue(filesCreated.size() == 2);
      assertTrue(filesToDelete.size() == 1);
      assertTrue(new File(path4.getPath()).exists());
      filesToDelete.get(0).delete();
      assertFalse(new File("/tmp/path_TK_" + fId).exists());

      // new File("/tmp/pathTK_" + path4.getFichierId()).delete();

      filesCreated.clear();
      filesToDelete.clear();
      // teste la suppression reference bas + fichier path5
      // et reference fichier path partage path6
      final Fichier path5 = new Fichier();
      path5.setNom("TOBEREMOVED1");
      path5.setPath("/tmp/path_TK");
      path5.setEchantillon(echantillonDao.findById(1));
      path5.setMimeType("application/octet-stream");
      tmp = "domenech for ever";
      byteArray = tmp.getBytes();
      bais = new ByteArrayInputStream(byteArray);
      fichierManager.createObjectManager(path5, bais, filesCreated);

      assertTrue(new File(path5.getPath()).exists());
      assertTrue(new File(path5.getPath()).length() == 17);
      final Fichier path6 = new Fichier();
      path6.setNom("TOBEREMOVED2");
      path6.setPath(path5.getPath());
      path6.setEchantillon(echantillonDao.findById(2));
      path6.setMimeType("application/octet-stream");
      fichierManager.createObjectManager(path6, null, filesCreated);
      assertTrue(fichierManager.findByPathLikeManager(path5.getPath(), true).size() == 2);
      assertTrue(new File(path6.getPath()).exists());
      assertTrue(new File(path6.getPath()).length() == 17);
      assertTrue(filesCreated.size() == 1);
      assertTrue(filesToDelete.size() == 0);

      // suprr la ref seulement
      fichierManager.removeObjectManager(path6, filesToDelete);
      assertTrue(new File(path6.getPath()).exists());
      assertTrue(filesToDelete.size() == 0);
      // suppr ref + fichier
      fichierManager.removeObjectManager(path5, filesToDelete);
      assertTrue(filesToDelete.size() == 1);
      assertTrue(new File(path5.getPath()).exists());
      assertTrue(new File(path6.getPath()).exists());
      filesToDelete.get(0).delete();
      assertFalse(new File(path5.getPath()).exists());
      assertFalse(new File(path6.getPath()).exists());

      testFindAll();

      fichierManager.removeObjectManager(null, filesToDelete);

      testFindAll();
   }
}
