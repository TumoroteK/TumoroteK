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
package fr.aphp.tumorotek.test.manager.systeme;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.coeur.ObjetStatutManager;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager ObjetStatutManager.
 * Classe créée le 01/10/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ObjetStatutManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ObjetStatutManager objetStatutManager;

   public ObjetStatutManagerTest(){}

   @Test
   public void testFindById(){
      final ObjetStatut objet = objetStatutManager.findByIdManager(1);
      assertNotNull(objet);
      assertTrue(objet.getStatut().equals("STOCKE"));

      final ObjetStatut objetNull = objetStatutManager.findByIdManager(7);
      assertNull(objetNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<ObjetStatut> list = objetStatutManager.findAllObjectsManager();
      assertTrue(list.size() == 6);
   }

   /**
    * Test la méthode findByStatutLikeManager.
    */
   @Test
   public void testFindByStatutLikeExactManager(){
      List<ObjetStatut> list = objetStatutManager.findByStatutLikeManager("EPUISE", true);
      assertTrue(list.size() == 1);

      list = objetStatutManager.findByStatutLikeManager("EPUI", true);
      assertTrue(list.size() == 0);

      list = objetStatutManager.findByStatutLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByStatutLikeManager.
    */
   @Test
   public void testFindByStatutLikeManager(){
      List<ObjetStatut> list = objetStatutManager.findByStatutLikeManager("EPUISE", false);
      assertTrue(list.size() == 1);

      list = objetStatutManager.findByStatutLikeManager("EPUI", false);
      assertTrue(list.size() == 1);

      list = objetStatutManager.findByStatutLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode isUsedObject.
    */
   @Test
   public void testIsUsed(){
      final ObjetStatut objet1 = objetStatutManager.findByIdManager(1);
      assertNotNull(objet1);
      assertTrue(objetStatutManager.isUsedObjectManager(objet1));

      final ObjetStatut objet2 = objetStatutManager.findByIdManager(5);
      assertNotNull(objet2);
      assertFalse(objetStatutManager.isUsedObjectManager(objet2));
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      final ObjetStatut objet1 = new ObjetStatut();
      objet1.setStatut("STOCKE");
      assertTrue(objetStatutManager.findDoublonManager(objet1));

      final ObjetStatut objet2 = new ObjetStatut();
      objet2.setStatut("TEST");
      assertFalse(objetStatutManager.findDoublonManager(objet2));
   }

   /**
    * Test d'un CRUD.
    */
   @Test
   public void testCrud(){
      // Test de l'insertion
      final ObjetStatut objet1 = new ObjetStatut();
      objet1.setStatut("RESERVE");
      Boolean catchedInsert = false;
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancé
      try{
         objetStatutManager.saveManager(objet1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      // On teste une insertion avec un attribut statut non valide
      final String[] emptyValues = new String[] {"", "  ", null, "$$*te¤¤15|", createOverLength(20)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            objet1.setStatut(emptyValues[i]);
            objetStatutManager.saveManager(objet1);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      assertTrue(objetStatutManager.findAllObjectsManager().size() == 6);
      // On test une insertion valide
      objet1.setStatut("OK");
      objetStatutManager.saveManager(objet1);
      assertTrue(objet1.getObjetStatutId() == 7);

      // Test de la mise à jour
      final ObjetStatut objet2 = objetStatutManager.findByIdManager(7);
      final String statutUpdated1 = "NEW";
      final String statutUpdated2 = "DETRUIT";
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      objet2.setStatut(statutUpdated2);
      Boolean catchedUpdate = false;
      try{
         objetStatutManager.saveManager(objet2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      // On teste une modif avec l'attribut statut non valide
      for(int i = 0; i < emptyValues.length; i++){
         catchedUpdate = false;
         try{
            objet2.setStatut(emptyValues[i]);
            objetStatutManager.saveManager(objet2);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedUpdate = true;
            }
         }
         assertTrue(catchedUpdate);
      }
      // On teste une mise à jour valide
      objet2.setStatut(statutUpdated1);
      objetStatutManager.saveManager(objet2);
      final ObjetStatut objet3 = objetStatutManager.findByIdManager(7);
      assertTrue(objet3.getStatut().equals(statutUpdated1));

      // Test de la suppression
      final ObjetStatut objet4 = objetStatutManager.findByIdManager(7);
      objetStatutManager.removeObjectManager(objet4);
      assertNull(objetStatutManager.findByIdManager(7));
      // On test la suppression d'un objet utilisé
      final ObjetStatut objet5 = objetStatutManager.findByIdManager(1);
      Boolean catchedDelete = false;
      try{
         objetStatutManager.removeObjectManager(objet5);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catchedDelete = true;
         }
      }
      assertTrue(catchedDelete);
   }

}
