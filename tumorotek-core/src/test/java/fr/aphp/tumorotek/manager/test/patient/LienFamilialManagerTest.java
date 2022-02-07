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
package fr.aphp.tumorotek.manager.test.patient;

import static org.junit.Assert.*;


import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.coeur.patient.LienFamilialManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.coeur.patient.LienFamilial;

/**
 *
 * Classe de test pour le manager LienFamilialManager.
 * Classe créée le 29/10/09.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class LienFamilialManagerTest extends AbstractManagerTest4
{

   @Autowired
   private LienFamilialManager lienFamilialManager;

   public LienFamilialManagerTest(){}

   public void setLienFamilialManager(final LienFamilialManager lfManager){
      this.lienFamilialManager = lfManager;
   }

   /**
    * Test la méthode findAllObjectsManager.
    */
   @Test
   public void testFindAllObjectsManager(){
      final List<LienFamilial> lienFamilials = lienFamilialManager.findAllObjectsManager();
      assertTrue(lienFamilials.size() == 6);
   }

   /**
    * Test la methode findByNomLikeManager.
    */
   @Test
   public void testfindByNomLikeManager(){
      //teste une recherche exactMatch
      List<LienFamilial> lienFamilials = lienFamilialManager.findByNomLikeManager("Pere-Fille", true);
      assertTrue(lienFamilials.size() == 1);
      //teste une recherche non exactMatch
      lienFamilials = lienFamilialManager.findByNomLikeManager("Tan", false);
      assertTrue(lienFamilials.size() == 1);
      //teste une recherche infructueuse
      lienFamilials = lienFamilialManager.findByNomLikeManager("Mer", false);
      assertTrue(lienFamilials.size() == 0);
      //null recherche
      lienFamilials = lienFamilialManager.findByNomLikeManager(null, false);
      assertTrue(lienFamilials.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final LienFamilial lf1 = (lienFamilialManager.findByNomLikeManager("Tante-Neveu", true)).get(0);
      final LienFamilial lf2 = new LienFamilial();
      lf2.setNom(lf1.getNom());
      assertTrue(lf2.equals(lf1));
      assertTrue(lienFamilialManager.findDoublonManager(lf2));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObject(){
      //Enregistrement est reference
      final LienFamilial lf1 = (lienFamilialManager.findByNomLikeManager("Pere-Fille", true)).get(0);
      assertTrue(lienFamilialManager.isUsedObjectManager(lf1));
      //Enregistrement n'est pas reference
      final LienFamilial lf2 = (lienFamilialManager.findByNomLikeManager("Frere-Soeur", false)).get(0);
      assertFalse(lienFamilialManager.isUsedObjectManager(lf2));
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

   @Test
   public void createObjectManagerTest(){
      //Insertion nouvel enregistrement
      final LienFamilial lf1 = new LienFamilial();
      lf1.setNom("GrandMere-Petitfils");
      lf1.setAscendant(false);
      lienFamilialManager.createObjectManager(lf1);
      assertTrue((lienFamilialManager.findByNomLikeManager("GrandMere-Petitfils", true)).size() == 1);
      assertTrue((lienFamilialManager.findByNomLikeManager("Petitfils-GrandMere", true)).size() == 1);
      assertTrue((lienFamilialManager.findByNomLikeManager("Petitfils-GrandMere", true)).get(0).getReciproque().equals(lf1));
      assertTrue((lienFamilialManager.findByNomLikeManager("Petitfils-GrandMere", true)).get(0).getAscendant());
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      LienFamilial lf2 = new LienFamilial();
      lf2.setNom(lf1.getNom());
      assertTrue(lf2.equals(lf1));
      try{
         lienFamilialManager.createObjectManager(lf2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((lienFamilialManager.findByNomLikeManager("%GrandMere", false)).size() == 2);

      //validation test Type
      final String[] nomValues =
         new String[] {"", "  ", null, "HOO", "RR--TTT", createOverLength(25) + "-" + createOverLength(25), "good-TypeDS"};
      lf2 = new LienFamilial();
      for(int i = 0; i < nomValues.length; i++){
         try{
            lf2.setNom(nomValues[i]);
            if(i != 6){ //car creation valide
               lienFamilialManager.createObjectManager(lf2);
            }
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(lienFamilialManager.findAllObjectsManager().size() == 8);
         }
      }
   }

   @Test
   public void updateObjectManagerTest(){
      //Modification d'un enregistrement
      final LienFamilial lf1 = (lienFamilialManager.findByNomLikeManager("GrandMere-Petitfils", true)).get(0);
      lf1.setNom("Kilo-Jaffar");
      lf1.setAscendant(true);
      lienFamilialManager.updateObjectManager(lf1);
      assertTrue((lienFamilialManager.findByNomLikeManager("Kilo-Jaffar", true)).get(0).getAscendant());
      assertTrue(
         (lienFamilialManager.findByNomLikeManager("Kilo-Jaffar", true)).get(0).getReciproque().getNom().equals("Jaffar-Kilo"));
      assertFalse((lienFamilialManager.findByNomLikeManager("Kilo-Jaffar", true)).get(0).getReciproque().getAscendant());
      lf1.setAscendant(null);
      lienFamilialManager.updateObjectManager(lf1);
      assertNull((lienFamilialManager.findByNomLikeManager("Kilo-Jaffar", true)).get(0).getAscendant());
      assertNull((lienFamilialManager.findByNomLikeManager("Kilo-Jaffar", true)).get(0).getReciproque().getAscendant());
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         lf1.setNom("Fille-Pere");
         lienFamilialManager.updateObjectManager(lf1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((lienFamilialManager.findByNomLikeManager("Fille-Pere", false)).size() == 1);
   }

   @Test
   public void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final LienFamilial lf1 = (lienFamilialManager.findByNomLikeManager("Kilo-Jaffar", true)).get(0);
      lienFamilialManager.removeObjectManager(lf1);
      assertTrue((lienFamilialManager.findByNomLikeManager("%Jaffar", false)).size() == 0);
      //Suppression engrendrant une exception
      Boolean catched = false;
      try{
         final LienFamilial lf2 = (lienFamilialManager.findByNomLikeManager("Fille-Pere", true)).get(0);
         lienFamilialManager.removeObjectManager(lf2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((lienFamilialManager.findByNomLikeManager("Fille-Pere", true)).size() > 0);
      //null remove
      lienFamilialManager.removeObjectManager(null);
      testFindAllObjectsManager();
   }
}
