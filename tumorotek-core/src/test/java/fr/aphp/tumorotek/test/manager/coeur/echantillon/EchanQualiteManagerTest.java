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

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchanQualiteManager;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager EchanQualiteManager.
 * Classe créée le 24/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class EchanQualiteManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EchanQualiteManager echanQualiteManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public EchanQualiteManagerTest(){}

   @Test
   public void testFindById(){
      final EchanQualite qualite = echanQualiteManager.findByIdManager(1);
      assertNotNull(qualite);
      assertTrue(qualite.getEchanQualite().equals("MELANGE MO"));

      final EchanQualite qualiteNull = echanQualiteManager.findByIdManager(4);
      assertNull(qualiteNull);
   }

   /**
    * Test la méthode findByQualiteLikeManager.
    */
   @Test
   public void testFindByQualiteLikeExactManager(){
      List<EchanQualite> list = echanQualiteManager.findByQualiteLikeManager("MELANGE MO", true);
      assertTrue(list.size() == 1);

      list = echanQualiteManager.findByQualiteLikeManager("MELANGE", true);
      assertTrue(list.size() == 0);

      list = echanQualiteManager.findByQualiteLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByQualiteLikeManager.
    */
   @Test
   public void testFindByQualiteLikeManager(){
      List<EchanQualite> list = echanQualiteManager.findByQualiteLikeManager("MELANGE MO+SG", false);
      assertTrue(list.size() == 1);

      list = echanQualiteManager.findByQualiteLikeManager("MELANGE", false);
      assertTrue(list.size() == 3);

      list = echanQualiteManager.findByQualiteLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode isUsedObject.
    */
   @Test
   public void testIsUsed(){
      final EchanQualite qualite1 = echanQualiteManager.findByIdManager(1);
      assertNotNull(qualite1);
      assertTrue(echanQualiteManager.isUsedObjectManager(qualite1));

      final EchanQualite qualite2 = echanQualiteManager.findByIdManager(3);
      assertNotNull(qualite2);
      assertFalse(echanQualiteManager.isUsedObjectManager(qualite2));
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      EchanQualite qualite = new EchanQualite();
      qualite.setEchanQualite("MELANGE");
      qualite.setPlateforme(plateformeDao.findById(1));
      assertFalse(echanQualiteManager.findDoublonManager(qualite));

      qualite.setEchanQualite("MELANGE MO");
      assertTrue(echanQualiteManager.findDoublonManager(qualite));

      qualite = echanQualiteManager.findByIdManager(1);
      assertFalse(echanQualiteManager.findDoublonManager(qualite));

      qualite.setEchanQualite("MELANGE SG");
      assertTrue(echanQualiteManager.findDoublonManager(qualite));
   }

   @Test
   public void testCrud(){
      // Test de l'insertion
      final EchanQualite qualite1 = new EchanQualite();
      qualite1.setEchanQualite("MELANGE MO");
      Boolean catchedInsert = false;
      try{
         echanQualiteManager.saveManager(qualite1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      qualite1.setPlateforme(plateformeDao.findById(1));
      catchedInsert = false;
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancé
      try{
         echanQualiteManager.saveManager(qualite1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      // On teste une insertion avec un attribut qualité non valide
      final String[] emptyValues = new String[] {"", "  ", null, "€€¢¢¢®®®€", createOverLength(200)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            qualite1.setEchanQualite(emptyValues[i]);
            echanQualiteManager.saveManager(qualite1);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      assertTrue(echanQualiteManager.findByOrderManager(qualite1.getPlateforme()).size() == 2);
      // On test une insertion valide
      qualite1.setEchanQualite("MELANGE");
      echanQualiteManager.saveManager(qualite1);
      assertTrue(qualite1.getEchanQualiteId() == 4);

      // Test de la mise à jour
      final EchanQualite qualite2 = echanQualiteManager.findByIdManager(4);
      final String qualiteUpdated1 = "NEW";
      final String qualiteUpdated2 = "MELANGE MO";
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      qualite2.setEchanQualite(qualiteUpdated2);
      Boolean catchedUpdate = false;
      try{
         echanQualiteManager.saveManager(qualite2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      // On teste une modif avec l'attribut qualité non valide
      for(int i = 0; i < emptyValues.length; i++){
         catchedUpdate = false;
         try{
            qualite2.setEchanQualite(emptyValues[i]);
            echanQualiteManager.saveManager(qualite2);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedUpdate = true;
            }
         }
         assertTrue(catchedUpdate);
      }
      // On teste une mise à jour valide
      qualite2.setEchanQualite(qualiteUpdated1);
      echanQualiteManager.saveManager(qualite2);
      final EchanQualite qualite3 = echanQualiteManager.findByIdManager(4);
      assertTrue(qualite3.getEchanQualite().equals(qualiteUpdated1));

      // Test de la suppression
      final EchanQualite qualite4 = echanQualiteManager.findByIdManager(4);
      echanQualiteManager.deleteByIdManager(qualite4);
      assertNull(echanQualiteManager.findByIdManager(4));
      // On test la suppression d'un objet utilisé
      //		EchanQualite qualite5 = echanQualiteManager.findByIdManager(1);
      //		Boolean catchedDelete = false;
      //		try {
      //			echanQualiteManager.deleteByIdManager(qualite5);
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
      List<? extends TKThesaurusObject> list = echanQualiteManager.findByOrderManager(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getNom().equals("MELANGE MO"));
      pf = plateformeDao.findById(2);
      list = echanQualiteManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = echanQualiteManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }
}
