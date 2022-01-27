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
package fr.aphp.tumorotek.test.manager.coeur.prelevement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.NatureManager;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager NatureManager.
 * Classe créée le 02/10/09.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class NatureManagerTest extends AbstractManagerTest4
{

   @Autowired
   private NatureManager natureManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public NatureManagerTest(){}

   @Test
   public void testFindByNatureLikeManager(){
      //teste une recherche exactMatch
      List<Nature> natures = natureManager.findByNatureLikeManager("SANG", true);
      assertTrue(natures.size() == 1);
      //teste une recherche non exactMatch
      natures = natureManager.findByNatureLikeManager("S", false);
      assertTrue(natures.size() == 1);
      assertTrue(natures.get(0).getNature().equals("SANG"));
      //teste une recherche infructueuse
      natures = natureManager.findByNatureLikeManager("URINES", false);
      assertTrue(natures.size() == 0);
      //null recherche
      natures = natureManager.findByNatureLikeManager(null, false);
      assertTrue(natures.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final Nature n1 = (natureManager.findByNatureLikeManager("TISSU", true)).get(0);
      assertFalse(natureManager.findDoublonManager(n1));
      n1.setNature("SANG");
      assertTrue(natureManager.findDoublonManager(n1));
      final Nature n2 = new Nature();
      n2.setNature(n1.getNature());
      n2.setPlateforme(n1.getPlateforme());
      assertTrue(n2.equals(n1));
      assertTrue(natureManager.findDoublonManager(n2));
      n2.setNature("ROOO");
      assertFalse(natureManager.findDoublonManager(n2));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObjectManager(){
      //Enregistrement est reference
      final Nature n1 = (natureManager.findByNatureLikeManager("SANG", true)).get(0);
      assertTrue(natureManager.isUsedObjectManager(n1));
      //Enregistrement n'est pas reference
      final Nature n2 = (natureManager.findByNatureLikeManager("LCR", true)).get(0);
      assertFalse(natureManager.isUsedObjectManager(n2));
   }

   /**
    * Teste les methodes CRUD. 
    */
   @Test
   public void testCRUD(){
      saveManagerTest();
      saveManagerTest();
      deleteByIdManagerTest();
   }

   private void saveManagerTest(){
      //Insertion nouvel enregistrement
      final Nature n1 = new Nature();
      n1.setNature("CERVEAU -");
      n1.setPlateforme(plateformeDao.findById(1));
      natureManager.saveManager(n1);
      assertTrue((natureManager.findByNatureLikeManager("CERVEAU -", true)).size() == 1);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final Nature nBis = new Nature();
      nBis.setNature("CERVEAU -");
      try{
         natureManager.saveManager(nBis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      nBis.setPlateforme(n1.getPlateforme());
      assertTrue(catched);
      catched = false;
      try{
         natureManager.saveManager(nBis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((natureManager.findByNatureLikeManager("CERVEAU -", true)).size() == 1);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "¤¨¨12.K", createOverLength(200), null};
      final Nature n2 = new Nature();
      n2.setPlateforme(n1.getPlateforme());
      for(int i = 0; i < emptyValues.length; i++){
         try{
            n2.setNature(emptyValues[i]);
            natureManager.saveManager(n2);
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(natureManager.findByOrderManager(n1.getPlateforme()).size() == 4);
         }
      }
   }

   /**
    * Teste la methode saveManager. 
    */
   private void saveManagerTest(){
      //Modification d'un enregistrement
      final Nature n1 = (natureManager.findByNatureLikeManager("CERVEAU -", true)).get(0);
      n1.setNature("UR. INES");
      natureManager.saveManager(n1);
      assertTrue((natureManager.findByNatureLikeManager("UR. INES", true)).size() == 1);
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         n1.setNature("SANG");
         natureManager.saveManager(n1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((natureManager.findByNatureLikeManager("SANG", true)).size() == 1);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "plk$_¤¤", createOverLength(200), null};
      for(int i = 0; i < emptyValues.length; i++){
         try{
            n1.setNature(emptyValues[i]);
            natureManager.saveManager(n1);
         }catch(final ValidationException e){
            //verifie que l'enregistrement n'a pas ete modifie
            assertTrue((natureManager.findByNatureLikeManager("SANG", true)).size() == 1);
         }
      }
   }

   private void deleteByIdManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final Nature n1 = (natureManager.findByNatureLikeManager("UR. INES", true)).get(0);
      natureManager.deleteByIdManager(n1);
      assertTrue((natureManager.findByNatureLikeManager("UR. INES", true)).size() == 0);
      //Suppression engrendrant une exception
      Boolean catched = false;
      try{
         final Nature n2 = (natureManager.findByNatureLikeManager("SANG", true)).get(0);
         natureManager.deleteByIdManager(n2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((natureManager.findByNatureLikeManager("SANG", true)).size() > 0);
      //null remove
      natureManager.deleteByIdManager(null);
      assertTrue(natureManager.findByOrderManager(n1.getPlateforme()).size() == 3);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = natureManager.findByOrderManager(pf);
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("LIQUIDE D'ASCITE"));
      pf = plateformeDao.findById(2);
      list = natureManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = natureManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }
}
