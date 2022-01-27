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
package fr.aphp.tumorotek.test.manager.coeur.prodderive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdTypeManager;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager ProdTypeManager.
 * Classe créée le 29/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ProdTypeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ProdTypeManager prodTypeManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public ProdTypeManagerTest(){}

   @Test
   public void testFindById(){
      final ProdType type = prodTypeManager.findByIdManager(1);
      assertNotNull(type);
      assertTrue(type.getType().equals("ADN"));

      final ProdType typeNull = prodTypeManager.findByIdManager(4);
      assertNull(typeNull);
   }

   @Test
   public void testFindByTypeLikeExactManager(){
      List<ProdType> list = prodTypeManager.findByTypeLikeManager("PROTEINE", true);
      assertTrue(list.size() == 1);

      list = prodTypeManager.findByTypeLikeManager("PROT", true);
      assertTrue(list.size() == 0);

      list = prodTypeManager.findByTypeLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindByTypeLikeManager(){
      List<ProdType> list = prodTypeManager.findByTypeLikeManager("ADN", false);
      assertTrue(list.size() == 1);

      list = prodTypeManager.findByTypeLikeManager("A", false);
      assertTrue(list.size() == 2);

      list = prodTypeManager.findByTypeLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testIsUsed(){
      final ProdType type = prodTypeManager.findByIdManager(1);
      assertNotNull(type);
      assertTrue(prodTypeManager.isUsedObjectManager(type));

      final ProdType type2 = prodTypeManager.findByIdManager(3);
      assertNotNull(type2);
      assertTrue(prodTypeManager.isUsedObjectManager(type2));
   }

   @Test
   public void testFindDoublon(){
      ProdType type1 = new ProdType();
      type1.setType("PROT");
      type1.setPlateforme(plateformeDao.findById(1));
      assertFalse(prodTypeManager.findDoublonManager(type1));

      final ProdType type2 = new ProdType();
      type2.setType("PROTEINE");
      type2.setPlateforme(plateformeDao.findById(1));
      assertTrue(prodTypeManager.findDoublonManager(type2));

      type1 = prodTypeManager.findByIdManager(1);
      assertFalse(prodTypeManager.findDoublonManager(type1));
      type1.setType("PROTEINE");
      assertTrue(prodTypeManager.findDoublonManager(type1));
   }

   @Test
   public void testCrud(){
      // Test de l'insertion
      final ProdType type1 = new ProdType();
      type1.setType("ARN");
      Boolean catchedInsert = false;
      try{
         prodTypeManager.saveManager(type1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catchedInsert = true;
         }
      }
      catchedInsert = false;
      type1.setPlateforme(plateformeDao.findById(1));
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancé
      try{
         prodTypeManager.saveManager(type1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      // On teste une insertion avec un attribut type non valide
      final String[] emptyValues = new String[] {"", "  ", null, "-/=##¤e$$hjo", createOverLength(200)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            type1.setType(emptyValues[i]);
            prodTypeManager.saveManager(type1);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      assertTrue(prodTypeManager.findByOrderManager(type1.getPlateforme()).size() == 3);
      // On test une insertion valide
      type1.setType("BON");
      prodTypeManager.saveManager(type1);
      assertTrue(type1.getProdTypeId() == 4);

      // Test de la mise à jour
      final ProdType type2 = prodTypeManager.findByIdManager(4);
      final String typeUpdated1 = "NEW";
      final String typeUpdated2 = "ARN";
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      type2.setType(typeUpdated2);
      Boolean catchedUpdate = false;
      try{
         prodTypeManager.saveManager(type2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      // On teste une modif avec l'attribut path non valide
      for(int i = 0; i < emptyValues.length; i++){
         catchedUpdate = false;
         try{
            type2.setType(emptyValues[i]);
            prodTypeManager.saveManager(type2);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedUpdate = true;
            }
         }
         assertTrue(catchedUpdate);
      }
      // On teste une mise à jour valide
      type2.setType(typeUpdated1);
      prodTypeManager.saveManager(type2);
      final ProdType type3 = prodTypeManager.findByIdManager(4);
      assertTrue(type3.getType().equals(typeUpdated1));

      // Test de la suppression
      final ProdType type4 = prodTypeManager.findByIdManager(4);
      prodTypeManager.deleteByIdManager(type4);
      assertNull(prodTypeManager.findByIdManager(4));
      // On test la suppression d'un objet utilisé
      final ProdType type5 = prodTypeManager.findByIdManager(1);
      Boolean catchedDelete = false;
      try{
         prodTypeManager.deleteByIdManager(type5);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catchedDelete = true;
         }
      }
      assertTrue(catchedDelete);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = prodTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 3);
      assertTrue(list.get(1).getNom().equals("ARN"));
      pf = plateformeDao.findById(2);
      list = prodTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 0);
      list = prodTypeManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }

}
