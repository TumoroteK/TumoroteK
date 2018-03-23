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
package fr.aphp.tumorotek.manager.test.coeur.echantillon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.ModePrepaManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le manager ModePrepaManager.
 * Classe créée le 24/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ModePrepaManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ModePrepaManager modePrepaManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public ModePrepaManagerTest(){}

   @Test
   public void testFindById(){
      final ModePrepa mode = modePrepaManager.findByIdManager(1);
      assertNotNull(mode);
      assertTrue(mode.getNom().equals("PREPA1"));

      final ModePrepa modeNull = modePrepaManager.findByIdManager(5);
      assertNull(modeNull);
   }

   /**
    * Test la méthode findByModePrepaLikeManager.
    */
   @Test
   public void testFindByModePrepaLikeExactManager(){
      List<ModePrepa> list = modePrepaManager.findByModePrepaLikeManager("PREPA1", true);
      assertTrue(list.size() == 1);

      list = modePrepaManager.findByModePrepaLikeManager("PREPA", true);
      assertTrue(list.size() == 0);

      list = modePrepaManager.findByModePrepaLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByModePrepaLikeManager.
    */
   @Test
   public void testFindByModePrepaLikeManager(){
      List<ModePrepa> list = modePrepaManager.findByModePrepaLikeManager("PREPA1", false);
      assertTrue(list.size() == 1);

      list = modePrepaManager.findByModePrepaLikeManager("PREPA", false);
      assertTrue(list.size() == 4);

      list = modePrepaManager.findByModePrepaLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode isUsedObject.
    */
   @Test
   public void testIsUsed(){
      final ModePrepa mode1 = modePrepaManager.findByIdManager(1);
      assertNotNull(mode1);
      assertTrue(modePrepaManager.isUsedObjectManager(mode1));

      final ModePrepa mode2 = modePrepaManager.findByIdManager(4);
      assertNotNull(mode2);
      assertFalse(modePrepaManager.isUsedObjectManager(mode2));
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      ModePrepa mode = new ModePrepa();
      mode.setNom("PREPA");
      mode.setPlateforme(plateformeDao.findById(1));
      assertFalse(modePrepaManager.findDoublonManager(mode));

      mode.setNom("PREPA1");
      mode.setNomEn("PREPA_EN1");
      assertTrue(modePrepaManager.findDoublonManager(mode));

      mode = modePrepaManager.findByIdManager(1);
      assertFalse(modePrepaManager.findDoublonManager(mode));
      mode.setNom("PREPA2");
      mode.setNomEn("PREPA_EN2");
      assertTrue(modePrepaManager.findDoublonManager(mode));
   }

   /**
    * Test d'un CRUD.
    */
   @Test
   public void testCrud(){
      // Insertion
      final ModePrepa mode1 = new ModePrepa();
      mode1.setNom("PREPA1");
      mode1.setNomEn("PREPA_EN1");
      Boolean catched = false;
      try{
         modePrepaManager.createObjectManager(mode1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      catched = false;
      mode1.setPlateforme(plateformeDao.findById(1));
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancé
      try{
         modePrepaManager.createObjectManager(mode1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      // On teste une insertion avec un attribut nom non valide
      final String[] nomValues = new String[] {"", "  ", null, "€€¢¢¢®®®€", createOverLength(200), "GOOD"};
      final String[] nomEnValues = new String[] {"", " ", "€€¢¢¢®®®€", createOverLength(25), "GOOD"};
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < nomEnValues.length; j++){
            catched = false;
            try{
               mode1.setNom(nomValues[i]);
               mode1.setNomEn(nomEnValues[j]);
               if(i != 5 || j != 4){ //car creation valide
                  modePrepaManager.createObjectManager(mode1);
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
      assertTrue(modePrepaManager.findByOrderManager(mode1.getPlateforme()).size() == 3);
      // on teste une insertion valide
      mode1.setNom("PREPA");
      mode1.setNomEn(null);
      modePrepaManager.createObjectManager(mode1);
      assertTrue(mode1.getModePrepaId() == 5);

      // Test de la mise à jour
      final ModePrepa mode2 = modePrepaManager.findByIdManager(5);
      final String modeUpdated1 = "NEW";
      final String modeUpdated2 = "PREPA1";
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      mode2.setNom(modeUpdated2);
      mode2.setNomEn("PREPA_EN1");
      Boolean catchedUpdate = false;
      try{
         modePrepaManager.updateObjectManager(mode2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      // On teste une modif avec l'attribut nom non valide
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < nomEnValues.length; j++){
            catched = false;
            try{
               mode2.setNom(nomValues[i]);
               mode2.setNomEn(nomEnValues[j]);
               if(i != 5 || j != 4){ //car creation valide
                  modePrepaManager.updateObjectManager(mode2);
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
      // On teste une mise à jour valide
      mode2.setNom(modeUpdated1);
      modePrepaManager.updateObjectManager(mode2);
      final ModePrepa mode3 = modePrepaManager.findByIdManager(5);
      assertTrue(mode3.getNom().equals(modeUpdated1));

      // Test de la suppression
      final ModePrepa mode4 = modePrepaManager.findByIdManager(5);
      modePrepaManager.removeObjectManager(mode4);
      assertNull(modePrepaManager.findByIdManager(5));
      // On test la suppression d'un objet utilisé
      //		ModePrepa mode5 = modePrepaManager.findByIdManager(1);
      //		Boolean catchedDelete = false;
      //		try {
      //			modePrepaManager.removeObjectManager(mode5);
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
      List<? extends TKThesaurusObject> list = modePrepaManager.findByOrderManager(pf);
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("PREPA1"));
      pf = plateformeDao.findById(2);
      list = modePrepaManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = modePrepaManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }

}
