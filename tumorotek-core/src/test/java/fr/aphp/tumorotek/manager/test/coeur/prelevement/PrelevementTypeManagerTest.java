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
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementTypeManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le manager PrelevementTypeManager.
 * Classe créée le 05/10/09.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class PrelevementTypeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private PrelevementTypeManager prelevementTypeManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public PrelevementTypeManagerTest(){}

   @Test
   public void testFindByTypeLikeManager(){
      //teste une recherche exactMatch
      List<PrelevementType> prelevementTypes = prelevementTypeManager.findByTypeLikeManager("BIOPSIE", true);
      assertTrue(prelevementTypes.size() == 1);
      //teste une recherche non exactMatch
      prelevementTypes = prelevementTypeManager.findByTypeLikeManager("PONC", false);
      assertTrue(prelevementTypes.size() == 1);
      //teste une recherche infructueuse
      prelevementTypes = prelevementTypeManager.findByTypeLikeManager("PRISE", false);
      assertTrue(prelevementTypes.size() == 0);
      //null recherche
      prelevementTypes = prelevementTypeManager.findByTypeLikeManager(null, false);
      assertTrue(prelevementTypes.size() == 0);
   }

   /**
    * Test la methode findByIncaCatManager.
    */
   @Test
   public void testFindByIncaCatManager(){
      //teste une recherche fructueuse
      List<PrelevementType> prelevementTypes = prelevementTypeManager.findByIncaCatManager("P");
      assertTrue(prelevementTypes.size() == 2);
      //teste une recherche infructueuse
      prelevementTypes = prelevementTypeManager.findByIncaCatManager("C");
      assertTrue(prelevementTypes.size() == 0);
      //null recherche
      prelevementTypes = prelevementTypeManager.findByIncaCatManager(null);
      assertTrue(prelevementTypes.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final PrelevementType pt1 = (prelevementTypeManager.findByTypeLikeManager("BIOPSIE", true)).get(0);
      assertFalse(prelevementTypeManager.findDoublonManager(pt1));
      final PrelevementType pt2 = new PrelevementType();
      pt2.setNom(pt1.getNom());
      pt2.setIncaCat(pt1.getIncaCat());
      pt2.setPlateforme(pt1.getPlateforme());
      assertTrue(pt2.equals(pt1));
      assertTrue(prelevementTypeManager.findDoublonManager(pt2));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObject(){
      //Enregistrement est reference
      final PrelevementType pt1 = (prelevementTypeManager.findByTypeLikeManager("BIOPSIE", true)).get(0);
      assertTrue(prelevementTypeManager.isUsedObjectManager(pt1));
      //Enregistrement n'est pas reference
      final PrelevementType ct2 = (prelevementTypeManager.findByTypeLikeManager("NECROPSIE", true)).get(0);
      assertFalse(prelevementTypeManager.isUsedObjectManager(ct2));
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
      final PrelevementType pt1 = new PrelevementType();
      pt1.setNom("PRISE DE SANG.");
      pt1.setIncaCat("S");
      pt1.setPlateforme(plateformeDao.findById(1));
      prelevementTypeManager.createObjectManager(pt1);
      assertTrue((prelevementTypeManager.findByTypeLikeManager("PRISE DE SANG.", true)).size() == 1);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final PrelevementType pt1Bis = new PrelevementType();
      pt1Bis.setNom("PRISE DE SANG.");
      pt1Bis.setIncaCat("S");
      try{
         prelevementTypeManager.createObjectManager(pt1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      pt1Bis.setPlateforme(pt1.getPlateforme());
      assertTrue(catched);
      try{
         prelevementTypeManager.createObjectManager(pt1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((prelevementTypeManager.findByTypeLikeManager("PRISE%", false)).size() == 1);
      assertTrue((prelevementTypeManager.findByIncaCatManager("S")).size() == 1);

      //validation test Type
      final String[] typeValues = new String[] {"", "  ", null, "*$$¢**", createOverLength(200), "goodType"};
      final String[] incaCatValues = new String[] {"", " ", null, "EEE", "goodCat"};
      final PrelevementType pt2 = new PrelevementType();
      pt2.setPlateforme(pt1.getPlateforme());
      for(int i = 0; i < typeValues.length; i++){
         for(int j = 0; j < incaCatValues.length; j++){
            try{
               pt2.setNom(typeValues[i]);
               pt2.setIncaCat(incaCatValues[j]);
               if(i != 5 && j != 3){ //car creation valide
                  prelevementTypeManager.createObjectManager(pt2);
               }
            }catch(final ValidationException e){
               //verifie qu'aucune ligne n'a ete ajoutee
               assertTrue(prelevementTypeManager.findByOrderManager(pt1.getPlateforme()).size() == 4);
            }
         }
      }
   }

   private void updateObjectManagerTest(){
      //Modification d'un enregistrement
      final PrelevementType pt1 = (prelevementTypeManager.findByTypeLikeManager("PRISE DE SANG.", true)).get(0);
      pt1.setNom("Èré+");
      pt1.setIncaCat("F");
      prelevementTypeManager.updateObjectManager(pt1);
      assertTrue((prelevementTypeManager.findByTypeLikeManager("Èré+", true)).get(0).getIncaCat().equals("F"));
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         pt1.setNom("BIOPSIE");
         pt1.setIncaCat("B");
         prelevementTypeManager.updateObjectManager(pt1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((prelevementTypeManager.findByTypeLikeManager("BIOPSIE", true)).size() == 1);

      //Validation test
      final String[] typeValues = new String[] {"", "  ", null, "$$@¤¤", createOverLength(200), "goodType"};
      final String[] incaCatValues = new String[] {"", " ", null, "EEE", "goodCat"};
      for(int i = 0; i < typeValues.length; i++){
         for(int j = 0; j < incaCatValues.length; j++){
            try{
               pt1.setNom(typeValues[i]);
               pt1.setIncaCat(incaCatValues[j]);
               if(i != 5 && j != 4){ //car modification valide
                  prelevementTypeManager.updateObjectManager(pt1);
               }
            }catch(final ValidationException e){
               //verifie que l'enregistrement n'a pas ete modifie
               assertTrue((prelevementTypeManager.findByTypeLikeManager("Èré+", true)).get(0).getIncaCat().equals("F"));
            }
         }
      }
   }

   private void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final PrelevementType pt1 = (prelevementTypeManager.findByTypeLikeManager("Èré+", true)).get(0);
      prelevementTypeManager.removeObjectManager(pt1);
      assertTrue((prelevementTypeManager.findByTypeLikeManager("Èré+", true)).size() == 0);
      //Suppression engrendrant une exception
      //		Boolean catched = false;
      //		try {
      //			PrelevementType pt2 = (prelevementTypeManager
      //					.findByTypeLikeManager("BIOPSIE", true)).get(0);
      //			prelevementTypeManager.removeObjectManager(pt2);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catched = true;
      //			}
      //		}
      //		assertTrue(catched);
      assertTrue((prelevementTypeManager.findByTypeLikeManager("BIOPSIE", true)).size() > 0);
      //null remove
      prelevementTypeManager.removeObjectManager(null);
      assertTrue(prelevementTypeManager.findByOrderManager(pt1.getPlateforme()).size() == 3);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = prelevementTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("BIOPSIE"));
      pf = plateformeDao.findById(2);
      list = prelevementTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = prelevementTypeManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }
}
