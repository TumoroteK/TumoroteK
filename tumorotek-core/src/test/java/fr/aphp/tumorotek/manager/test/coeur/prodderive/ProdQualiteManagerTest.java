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
package fr.aphp.tumorotek.manager.test.coeur.prodderive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdQualiteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le manager ProdQualiteManager.
 * Classe créée le 29/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ProdQualiteManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ProdQualiteManager prodQualiteManager;

   @Autowired
   private PlateformeDao plateformeDao;

   public ProdQualiteManagerTest(){}

   @Test
   public void testFindById(){
      final ProdQualite qualite = prodQualiteManager.findByIdManager(1);
      assertNotNull(qualite);
      assertTrue(qualite.getProdQualite().equals("REPRESENTATIF"));

      final ProdQualite qualiteNull = prodQualiteManager.findByIdManager(4);
      assertNull(qualiteNull);
   }

   @Test
   public void testFindByQualiteLikeExactManager(){
      List<ProdQualite> list = prodQualiteManager.findByQualiteLikeManager("NECROSE", true);
      assertTrue(list.size() == 1);

      list = prodQualiteManager.findByQualiteLikeManager("NECR", true);
      assertTrue(list.size() == 0);

      list = prodQualiteManager.findByQualiteLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindByQualiteLikeManager(){
      List<ProdQualite> list = prodQualiteManager.findByQualiteLikeManager("NECROSE", false);
      assertTrue(list.size() == 1);

      list = prodQualiteManager.findByQualiteLikeManager("NECR", false);
      assertTrue(list.size() == 1);

      list = prodQualiteManager.findByQualiteLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testIsUsed(){
      final ProdQualite qualite = prodQualiteManager.findByIdManager(1);
      assertNotNull(qualite);
      assertTrue(prodQualiteManager.isUsedObjectManager(qualite));

      final ProdQualite qualite2 = prodQualiteManager.findByIdManager(3);
      assertNotNull(qualite2);
      assertFalse(prodQualiteManager.isUsedObjectManager(qualite2));
   }

   @Test
   public void testFindDoublon(){
      ProdQualite p1 = new ProdQualite();
      p1.setProdQualite("TUM");
      p1.setPlateforme(plateformeDao.findById(2));
      assertFalse(prodQualiteManager.findDoublonManager(p1));

      final ProdQualite p2 = new ProdQualite();
      p2.setProdQualite("TUMEUR");
      p2.setPlateforme(plateformeDao.findById(2));
      assertTrue(prodQualiteManager.findDoublonManager(p2));

      p1 = prodQualiteManager.findByIdManager(1);
      assertFalse(prodQualiteManager.findDoublonManager(p1));
      p1.setProdQualite("NECROSE");
      assertTrue(prodQualiteManager.findDoublonManager(p1));
   }

   @Test
   public void testCrud(){
      // Test de l'insertion
      final ProdQualite qualite1 = new ProdQualite();
      qualite1.setProdQualite("TUMEUR");
      Boolean catchedInsert = false;
      try{
         prodQualiteManager.createObjectManager(qualite1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catchedInsert = true;
         }
      }
      catchedInsert = false;
      qualite1.setPlateforme(plateformeDao.findById(2));
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancé
      try{
         prodQualiteManager.createObjectManager(qualite1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      // On teste une insertion avec un attribut qualite non valide
      final String[] emptyValues = new String[] {"", "  ", null, "%$uhec¤¤", createOverLength(200)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            qualite1.setProdQualite(emptyValues[i]);
            prodQualiteManager.createObjectManager(qualite1);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      assertTrue(prodQualiteManager.findByOrderManager(qualite1.getPlateforme()).size() == 1);
      // On test une insertion valide
      qualite1.setProdQualite("BONNE");
      prodQualiteManager.createObjectManager(qualite1);
      assertTrue(qualite1.getProdQualiteId() == 4);

      // Test de la mise à jour
      final ProdQualite qualite2 = prodQualiteManager.findByIdManager(4);
      final String qualiteUpdated1 = "NEW";
      final String qualiteUpdated2 = "TUMEUR";
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      qualite2.setProdQualite(qualiteUpdated2);
      Boolean catchedUpdate = false;
      try{
         prodQualiteManager.updateObjectManager(qualite2);
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
            qualite2.setProdQualite(emptyValues[i]);
            prodQualiteManager.updateObjectManager(qualite2);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedUpdate = true;
            }
         }
         assertTrue(catchedUpdate);
      }
      // On teste une mise à jour valide
      qualite2.setProdQualite(qualiteUpdated1);
      prodQualiteManager.updateObjectManager(qualite2);
      final ProdQualite qualite3 = prodQualiteManager.findByIdManager(4);
      assertTrue(qualite3.getProdQualite().equals(qualiteUpdated1));

      // Test de la suppression
      final ProdQualite qualite4 = prodQualiteManager.findByIdManager(4);
      prodQualiteManager.removeObjectManager(qualite4);
      assertNull(prodQualiteManager.findByIdManager(4));
      // On test la suppression d'un objet utilisé
      //		ProdQualite qualite5 = prodQualiteManager.findByIdManager(1);
      //		Boolean catchedDelete = false;
      //		try {
      //			prodQualiteManager.removeObjectManager(qualite5);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catchedDelete = true;
      //			}
      //		}
      //		assertTrue(catchedDelete);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = prodQualiteManager.findByOrderManager(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(1).getNom().equals("REPRESENTATIF"));
      pf = plateformeDao.findById(2);
      list = prodQualiteManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = prodQualiteManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }

}
