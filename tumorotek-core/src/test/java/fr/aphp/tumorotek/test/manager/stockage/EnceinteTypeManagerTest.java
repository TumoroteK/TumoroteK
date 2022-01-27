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
package fr.aphp.tumorotek.test.manager.stockage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.stockage.EnceinteTypeManager;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.EnceinteType;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager EnceinteTypeManager.
 * Classe créée le 17/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class EnceinteTypeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EnceinteTypeManager enceinteTypeManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public EnceinteTypeManagerTest(){}

   /**
    * Test la méthode findById.
    */
   @Test
   public void testFindById(){
      final EnceinteType et1 = enceinteTypeManager.findByIdManager(1);
      assertNotNull(et1);
      assertTrue(et1.getNom().equals("CASIER"));

      final EnceinteType csNull = enceinteTypeManager.findByIdManager(100);
      assertNull(csNull);
   }

   //	/**
   //	 * Test la méthode findAllObjects.
   //	 */
   //	@Test
   //	public void testFindAll() {
   //		List<EnceinteType> list = enceinteTypeManager.findAllObjectsManager();
   //		assertTrue(list.size() == 8);
   //		assertTrue(list.get(0).getNom().equals("BOITE"));
   //	}

   /**
    * Test la méthode findAllObjectsExceptBoiteManager.
    */
   @Test
   public void testFindAllObjectsExceptBoiteManager(){
      final Plateforme pf1 = plateformeDao.findById(1);
      final List<EnceinteType> list = enceinteTypeManager.findAllObjectsExceptBoiteManager(pf1);
      assertTrue(list.size() == 7);
      assertTrue(list.get(0).getNom().equals("CANISTER"));
   }

   @Test
   public void testFindByTypeManager(){
      List<EnceinteType> list = enceinteTypeManager.findByTypeManager("CASIER", plateformeDao.findById(1));
      assertTrue(list.size() == 1);

      list = enceinteTypeManager.findByTypeManager("CASIER2", plateformeDao.findById(1));
      assertTrue(list.size() == 0);

      list = enceinteTypeManager.findByTypeManager("CASIER", plateformeDao.findById(2));
      assertTrue(list.size() == 0);

      list = enceinteTypeManager.findByTypeManager("CASIER", null);
      assertTrue(list.size() == 0);

      list = enceinteTypeManager.findByTypeManager(null, plateformeDao.findById(1));
      assertTrue(list.size() == 0);

      list = enceinteTypeManager.findByTypeManager(null, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final EnceinteType e1 = enceinteTypeManager.findByIdManager(1);
      assertFalse(enceinteTypeManager.findDoublonManager(e1));
      e1.setNom("TIROIR");
      e1.setPrefixe("TIR");
      assertTrue(enceinteTypeManager.findDoublonManager(e1));
      final EnceinteType e2 = new EnceinteType();
      e2.setNom(e1.getNom());
      e2.setPrefixe(e1.getPrefixe());
      e2.setPlateforme(plateformeDao.findById(1));
      assertTrue(e2.equals(e1));
      assertTrue(enceinteTypeManager.findDoublonManager(e2));
      e2.setNom("ROOO");
      e2.setPrefixe("ROOO");
      assertFalse(enceinteTypeManager.findDoublonManager(e2));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObjectManager(){
      //Enregistrement est reference
      final EnceinteType e2 = enceinteTypeManager.findByIdManager(2);
      assertTrue(enceinteTypeManager.isUsedObjectManager(e2));
      //Enregistrement n'est pas reference
      final EnceinteType e1 = enceinteTypeManager.findByIdManager(1);
      assertFalse(enceinteTypeManager.isUsedObjectManager(e1));
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
      final EnceinteType e1 = new EnceinteType();
      e1.setNom("NEW");
      e1.setPrefixe("NEW");
      e1.setPlateforme(plateformeDao.findById(1));
      enceinteTypeManager.saveManager(e1);
      assertTrue(enceinteTypeManager.findByOrderManager(e1.getPlateforme()).size() == 9);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final EnceinteType e1Bis = new EnceinteType();
      e1Bis.setNom("NEW");
      e1Bis.setPrefixe("NEW");
      try{
         enceinteTypeManager.saveManager(e1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      e1Bis.setPlateforme(e1.getPlateforme());
      assertTrue(catched);
      catched = false;
      try{
         enceinteTypeManager.saveManager(e1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(enceinteTypeManager.findByOrderManager(e1.getPlateforme()).size() == 9);

      //validation test Type
      final String[] nomValues = new String[] {"", "  ", null, "HOO$$%", createOverLength(200), "goo d98-Type"};
      final String[] prefValues = new String[] {"", "  ", null, "HOO$$%", createOverLength(5), "goo"};
      final EnceinteType e2 = new EnceinteType();
      e2.setPlateforme(e1.getPlateforme());
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < prefValues.length; j++){
            try{
               e2.setNom(nomValues[i]);
               e2.setPrefixe(prefValues[j]);
               if(i != 5 && j != 5){ //car creation valide
                  enceinteTypeManager.saveManager(e2);
               }
            }catch(final ValidationException e){
               //verifie qu'aucune ligne n'a ete ajoutee
               assertTrue(enceinteTypeManager.findByOrderManager(e1.getPlateforme()).size() == 9);
            }
         }
      }
   }

   private void saveManagerTest(){
      //Modification d'un enregistrement
      final EnceinteType e1 = enceinteTypeManager.findByIdManager(10);
      e1.setNom("NEW BIS");
      enceinteTypeManager.saveManager(e1);
      final EnceinteType e1Bis = enceinteTypeManager.findByIdManager(10);
      assertTrue(e1Bis.getNom().equals("NEW BIS"));
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         e1Bis.setNom("CASIER");
         e1Bis.setPrefixe("CAS");
         enceinteTypeManager.saveManager(e1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);

      //validation test Type
      final String[] nomValues = new String[] {"", "  ", null, "HOO$$%", createOverLength(200), "goo d98-Type"};
      final String[] prefValues = new String[] {"", "  ", null, "HOO$$%", createOverLength(5), "goo"};
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < prefValues.length; j++){
            try{
               e1Bis.setNom(nomValues[i]);
               e1Bis.setPrefixe(prefValues[j]);
               if(i != 5 && j != 5){ //car creation valide
                  catched = false;
                  enceinteTypeManager.saveManager(e1Bis);
               }
            }catch(final ValidationException e){
               //verifie qu'aucune ligne n'a ete ajoutee
               catched = true;
            }
            assertTrue(catched);
         }
      }
   }

   private void deleteByIdManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final EnceinteType e1 = enceinteTypeManager.findByIdManager(10);
      enceinteTypeManager.deleteByIdManager(e1);
      assertTrue(enceinteTypeManager.findByOrderManager(e1.getPlateforme()).size() == 8);
      //Suppression engrendrant une exception
      Boolean catched = false;
      try{
         final EnceinteType e2 = enceinteTypeManager.findByIdManager(2);
         enceinteTypeManager.deleteByIdManager(e2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(enceinteTypeManager.findByOrderManager(e1.getPlateforme()).size() == 8);
      //null remove
      enceinteTypeManager.deleteByIdManager(null);
      assertTrue(enceinteTypeManager.findByOrderManager(e1.getPlateforme()).size() == 8);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = enceinteTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 8);
      assertTrue(list.get(4).getNom().equals("PANIER"));
      pf = plateformeDao.findById(2);
      list = enceinteTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = enceinteTypeManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }

}
