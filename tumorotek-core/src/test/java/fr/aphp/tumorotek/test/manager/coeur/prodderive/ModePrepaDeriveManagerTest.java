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
import fr.aphp.tumorotek.manager.coeur.prodderive.ModePrepaDeriveManager;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager ModePrepaDeriveManager.
 * Classe créée le 05/01/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ModePrepaDeriveManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ModePrepaDeriveManager modePrepaDeriveManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public ModePrepaDeriveManagerTest(){

   }

   @Test
   public void testFindById(){
      final ModePrepaDerive mode = modePrepaDeriveManager.findByIdManager(1);
      assertNotNull(mode);
      assertTrue(mode.getNom().equals("PREPA1_DERIVE"));

      final ModePrepaDerive modeNull = modePrepaDeriveManager.findByIdManager(5);
      assertNull(modeNull);
   }

   @Test
   public void testFindByModePrepaLikeExactManager(){
      List<ModePrepaDerive> list = modePrepaDeriveManager.findByModePrepaDeriveLikeManager("PREPA1_DERIVE", true);
      assertTrue(list.size() == 1);

      list = modePrepaDeriveManager.findByModePrepaDeriveLikeManager("PREPA", true);
      assertTrue(list.size() == 0);

      list = modePrepaDeriveManager.findByModePrepaDeriveLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindByModePrepaLikeManager(){
      List<ModePrepaDerive> list = modePrepaDeriveManager.findByModePrepaDeriveLikeManager("PREPA1_DERIVE", false);
      assertTrue(list.size() == 1);

      list = modePrepaDeriveManager.findByModePrepaDeriveLikeManager("PREPA", false);
      assertTrue(list.size() == 4);

      list = modePrepaDeriveManager.findByModePrepaDeriveLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testIsUsed(){
      final ModePrepaDerive mode1 = modePrepaDeriveManager.findByIdManager(1);
      assertNotNull(mode1);
      assertTrue(modePrepaDeriveManager.isUsedObjectManager(mode1));

      final ModePrepaDerive mode2 = modePrepaDeriveManager.findByIdManager(4);
      assertNotNull(mode2);
      assertFalse(modePrepaDeriveManager.isUsedObjectManager(mode2));
   }

   @Test
   public void testFindDoublon(){
      ModePrepaDerive mode = new ModePrepaDerive();
      mode.setNom("PREPA");
      mode.setPlateforme(plateformeDao.findById(1));
      assertFalse(modePrepaDeriveManager.findDoublonManager(mode));

      mode.setNom("PREPA1_DERIVE");
      mode.setNomEn("PREPA_EN1_DERIVE");
      assertTrue(modePrepaDeriveManager.findDoublonManager(mode));

      mode = modePrepaDeriveManager.findByIdManager(1);
      assertFalse(modePrepaDeriveManager.findDoublonManager(mode));
      mode.setNom("PREPA2_DERIVE");
      mode.setNomEn("PREPA_EN2_DERIVE");
      assertTrue(modePrepaDeriveManager.findDoublonManager(mode));
   }

   @Test
   public void testCrud(){
      // Insertion
      final ModePrepaDerive mode1 = new ModePrepaDerive();
      mode1.setNom("PREPA1_DERIVE");
      mode1.setNomEn("PREPA_EN1_DERIVE");
      Boolean catched = false;
      try{
         modePrepaDeriveManager.saveManager(mode1);
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
         modePrepaDeriveManager.saveManager(mode1);
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
                  modePrepaDeriveManager.saveManager(mode1);
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
      assertTrue(modePrepaDeriveManager.findByOrderManager(mode1.getPlateforme()).size() == 3);
      // on teste une insertion valide
      mode1.setNom("PREPA");
      mode1.setNomEn(null);
      modePrepaDeriveManager.saveManager(mode1);
      assertTrue(mode1.getModePrepaDeriveId() == 5);

      // Test de la mise à jour
      final ModePrepaDerive mode2 = modePrepaDeriveManager.findByIdManager(5);
      final String modeUpdated1 = "NEW";
      final String modeUpdated2 = "PREPA1_DERIVE";
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      mode2.setNom(modeUpdated2);
      mode2.setNomEn("PREPA_EN1_DERIVE");
      Boolean catchedUpdate = false;
      try{
         modePrepaDeriveManager.saveManager(mode2);
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
                  modePrepaDeriveManager.saveManager(mode2);
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
      modePrepaDeriveManager.saveManager(mode2);
      final ModePrepaDerive mode3 = modePrepaDeriveManager.findByIdManager(5);
      assertTrue(mode3.getNom().equals(modeUpdated1));

      // Test de la suppression
      final ModePrepaDerive mode4 = modePrepaDeriveManager.findByIdManager(5);
      modePrepaDeriveManager.removeObjectManager(mode4);
      assertNull(modePrepaDeriveManager.findByIdManager(5));
      // On test la suppression d'un objet utilisé
      //		ModePrepaDerive mode5 = modePrepaDeriveManager.findByIdManager(1);
      //		Boolean catchedDelete = false;
      //		try {
      //			modePrepaDeriveManager.removeObjectManager(mode5);
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
      List<? extends TKThesaurusObject> list = modePrepaDeriveManager.findByOrderManager(pf);
      assertTrue(list.size() == 3);
      assertTrue(list.get(1).getNom().equals("PREPA2_DERIVE"));
      pf = plateformeDao.findById(2);
      list = modePrepaDeriveManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = modePrepaDeriveManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }

}
