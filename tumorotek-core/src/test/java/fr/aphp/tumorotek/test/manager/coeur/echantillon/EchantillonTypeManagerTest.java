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
package fr.aphp.tumorotek.test.manager.coeur.echantillon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonTypeManager;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager EchantillonTypeManager.
 * Classe créée le 23/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class EchantillonTypeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EchantillonTypeManager echantillonTypeManager;
   @Autowired
   private EchantillonManager echantillonManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public EchantillonTypeManagerTest(){}

   @Test
   public void testFindById(){
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      assertNotNull(type);
      assertTrue(type.getType().equals("CELLULES"));
      final EchantillonType typeNull = echantillonTypeManager.findByIdManager(5);
      assertNull(typeNull);
   }

   @Test
   public void testFindByEchantillon(){
      final Echantillon e = echantillonManager.findByIdManager(1);

      final EchantillonType type = echantillonTypeManager.findByEchantillonManager(e);
      assertNotNull(type);
      assertTrue(type.getEchantillonTypeId() == 1);
   }

   /**
    * Test la méthode getEchantillonsManager.
    */
   @Test
   public void testGetEchantillons(){
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final Set<Echantillon> echans = echantillonTypeManager.getEchantillonsManager(type);
      assertNotNull(echans);
      assertTrue(echans.size() == 3);

      final EchantillonType type2 = echantillonTypeManager.findByIdManager(2);
      final Set<Echantillon> echans2 = echantillonTypeManager.getEchantillonsManager(type2);
      assertNotNull(echans2);
      assertTrue(echans2.size() == 0);
   }

   /**
    * Test la méthode findByTypeLikeManager.
    */
   @Test
   public void testFindByTypeLikeExactManager(){
      List<EchantillonType> list = echantillonTypeManager.findByTypeLikeManager("ADN", true);
      assertTrue(list.size() == 1);

      list = echantillonTypeManager.findByTypeLikeManager("A", true);
      assertTrue(list.size() == 0);

      list = echantillonTypeManager.findByTypeLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTypeLikeManager.
    */
   @Test
   public void testFindByTypeLikeManager(){
      List<EchantillonType> list = echantillonTypeManager.findByTypeLikeManager("ADN", false);
      assertTrue(list.size() == 1);

      list = echantillonTypeManager.findByTypeLikeManager("A", false);
      assertTrue(list.size() == 1);

      list = echantillonTypeManager.findByTypeLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode isUsedObject.
    */
   @Test
   public void testIsUsed(){
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      assertNotNull(type);
      assertTrue(echantillonTypeManager.isUsedObjectManager(type));
      final EchantillonType type2 = echantillonTypeManager.findByIdManager(2);
      assertNotNull(type2);
      assertFalse(echantillonTypeManager.isUsedObjectManager(type2));
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      EchantillonType type = new EchantillonType();
      type.setType("ADN");
      type.setIncaCat("CAT1");
      type.setPlateforme(plateformeDao.findById(1));
      assertFalse(echantillonTypeManager.findDoublonManager(type));

      type.setIncaCat("CAT2");
      assertTrue(echantillonTypeManager.findDoublonManager(type));

      type.setType(null);
      assertFalse(echantillonTypeManager.findDoublonManager(type));

      type.setType("ADN");
      type.setIncaCat(null);
      assertFalse(echantillonTypeManager.findDoublonManager(type));

      type = echantillonTypeManager.findByIdManager(1);
      assertFalse(echantillonTypeManager.findDoublonManager(type));

      type.setType("ADN");
      type.setIncaCat("CAT2");
      assertTrue(echantillonTypeManager.findDoublonManager(type));
   }

   /**
    * Test d'un CRUD.
    */
   @Test
   public void testCrud(){
      // Insertion
      final EchantillonType type1 = new EchantillonType();
      type1.setType("ADN");
      type1.setIncaCat("CAT2");
      Boolean catched = false;
      try{
         echantillonTypeManager.saveManager(type1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      type1.setPlateforme(plateformeDao.findById(1));
      catched = false;
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancé
      try{
         echantillonTypeManager.saveManager(type1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      // On teste une insertion avec un attribut type non valide
      final String[] emptyValues = new String[] {"", "  ", null, "€€¢¢¢®®®€", createOverLength(200), "GOOD"};
      final String[] incaCatValues = new String[] {"", " ", "€€¢¢¢®®®€", createOverLength(10), "GOOD"};
      for(int i = 0; i < emptyValues.length; i++){
         for(int j = 0; j < incaCatValues.length; j++){
            catched = false;
            try{
               type1.setType(emptyValues[i]);
               type1.setIncaCat(incaCatValues[j]);
               if(i != 5 || j != 4){ //car creation valide
                  echantillonTypeManager.saveManager(type1);
               }
            }catch(final Exception e){
               if(e.getClass().getSimpleName().equals("ValidationException")){
                  catched = true;
               }
            }
            if(i != 5 || j != 4){
               assertTrue(catched);
            }
         }
      }
      assertTrue(echantillonTypeManager.findByOrderManager(type1.getPlateforme()).size() == 3);
      // on teste une insertion valide
      type1.setType("RNA");
      type1.setIncaCat(null);
      echantillonTypeManager.saveManager(type1);
      assertTrue(type1.getEchantillonTypeId() == 5);

      // Update
      final EchantillonType type2 = echantillonTypeManager.findByIdManager(5);
      final String typeUpdated = "ADN";
      final String catUpdated1 = "CAT2";
      final String catUpdated2 = "CAT";
      // On teste une mise à jour valide
      type2.setType(typeUpdated);
      type2.setIncaCat(catUpdated2);
      echantillonTypeManager.saveManager(type2);
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      final EchantillonType type3 = echantillonTypeManager.findByIdManager(5);
      type3.setIncaCat(catUpdated1);
      Boolean catchedUpdate = false;
      try{
         echantillonTypeManager.saveManager(type3);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      // On teste une modif avec l'attribut type non valide
      for(int i = 0; i < emptyValues.length; i++){
         for(int j = 0; j < incaCatValues.length; j++){
            catchedUpdate = false;
            try{
               type3.setType(emptyValues[i]);
               type3.setIncaCat(incaCatValues[j]);
               if(i != 5 || j != 4){ //car modif valide
                  echantillonTypeManager.saveManager(type3);
               }
            }catch(final Exception e){
               if(e.getClass().getSimpleName().equals("ValidationException")){
                  catchedUpdate = true;
               }
            }
            if(i != 5 || j != 4){
               assertTrue(catchedUpdate);
            }
         }
      }
      // on test une modif valide
      final EchantillonType type4 = echantillonTypeManager.findByIdManager(5);
      assertTrue(type4.getType().equals(typeUpdated));

      // Suppression
      final EchantillonType type5 = echantillonTypeManager.findByIdManager(5);
      echantillonTypeManager.removeObjectManager(type5);
      assertNull(echantillonTypeManager.findByIdManager(5));
      final EchantillonType type6 = echantillonTypeManager.findByIdManager(1);
      try{
         echantillonTypeManager.removeObjectManager(type6);
      }catch(final Exception e){
         assertTrue(e.getClass().getSimpleName().equals("ObjectUsedException"));
      }
      assertNotNull(echantillonTypeManager.findByIdManager(1));
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = echantillonTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("ADN"));
      pf = plateformeDao.findById(2);
      list = echantillonTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = echantillonTypeManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }
}
