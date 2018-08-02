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
package fr.aphp.tumorotek.manager.test.io.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.io.export.Champ;

public class ChampManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ChampManager manager;
   @Autowired
   private ChampEntiteDao champEntiteDao;
   @Autowired
   private PatientDao patientDao;
   @Autowired
   private PrelevementDao prelevementDao;

   @Test
   public void testFindById(){
      // On teste une recherche avec des attributs non valides
      try{
         manager.findByIdManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      final Champ champ = manager.findByIdManager(1);
      assertNotNull(champ);
      assertTrue(champ.getChampId().equals(1));

      final Champ champNull = manager.findByIdManager(500);
      assertNull(champNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAllObjects(){
      final List<Champ> list = manager.findAllObjectsManager();
      //On vérifie chaque élément par un findById
      final Iterator<Champ> it = list.iterator();
      while(it.hasNext()){
         final Champ temp = it.next();
         if(temp != null){
            assertTrue(temp.equals(manager.findByIdManager(temp.getChampId())));
         }
      }
   }

   /**
    * Test la méthode findEnfantsManager.
    */
   @Test
   public void testFindEnfants(){
      // On teste une recherche d'enfants avec des attributs non valides
      try{
         manager.findEnfantsManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      final List<Champ> list = manager.findAllObjectsManager();
      final Iterator<Champ> it = list.iterator();
      while(it.hasNext()){
         final Champ temp = it.next();
         if(temp != null){
            final List<Champ> enfants = manager.findEnfantsManager(temp);
            final Iterator<Champ> it2 = enfants.iterator();
            while(it2.hasNext()){
               final Champ tempEnfant = it2.next();
               if(tempEnfant != null){
                  assertEquals(temp, tempEnfant.getChampParent());
               }
            }
         }
      }
   }

   @Test
   public void testCopyChamp(){
      final Champ champ = manager.findByIdManager(4);
      final Champ copie = manager.copyChampManager(champ);
      assertTrue(copie.getChampId() > 0);
      //On verifie que le champ et la copie sont identiques
      assertNotNull(copie);
      //On verifie que la copie est bien en BDD
      assertTrue(copie.equals(manager.findByIdManager(copie.getChampId())));

      //On supprime les éléments créés
      manager.removeObjectManager(copie);
   }

   @Test
   public void testCrud(){

      /** On teste une suppression invalide. */
      try{
         manager.removeObjectManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      try{
         manager.removeObjectManager(new Champ());
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      /** Save. */
      final Champ champ = new Champ(champEntiteDao.findById(200));
      manager.createObjectManager(champ, champ.getChampParent());
      //On vérifie que la sauvegarde a été correctement effectuée
      final Champ champ2 = manager.findByIdManager(champ.getChampId());
      assertTrue(champ.equals(champ2));

      /** Update. */
      champ.setChampEntite(champEntiteDao.findById(201));
      final Champ parent = new Champ(champEntiteDao.findById(202));

      manager.updateObjectManager(champ, parent);

      assertFalse(champ.getChampEntite().getId().equals(10));

      /** Remove. */
      final int id = champ.getChampId();
      //On le supprime
      manager.removeObjectManager(champ);
      //On essaye de le récupérer via l'identifiant
      final Champ champDeleted = manager.findByIdManager(id);
      //On verifie qu'il n'existe pas dans la liste du manager
      assertNull(champDeleted);
   }

   @Test
   public void testGetValueForObjectManager() throws ParseException{
      final Patient p1 = patientDao.findById(1);
      final Champ c1 = new Champ(champEntiteDao.findById(3));
      assertTrue(manager.getValueForObjectManager(c1, p1, true).equals("MAYER"));

      final Prelevement prlvt1 = prelevementDao.findById(1);
      final Champ c24 = new Champ(champEntiteDao.findById(24));
      assertTrue(manager.getValueForObjectManager(c24, prlvt1, true).equals("SANG"));
      assertTrue(manager.getValueForObjectManager(c24, prlvt1, false).equals("SANG"));
      final Champ c28 = new Champ(champEntiteDao.findById(28));
      assertTrue(manager.getValueForObjectManager(c28, prlvt1, true).equals("VIAL"));
      final Champ c27 = new Champ(champEntiteDao.findById(27));
      assertTrue(manager.getValueForObjectManager(c27, prlvt1, true).equals("06/09/1983"));
      assertTrue(
         manager.getValueForObjectManager(c27, prlvt1, false).equals(new SimpleDateFormat("dd/MM/yyyy").parse("06/09/1983")));
      final Champ c30 = new Champ(champEntiteDao.findById(30));
      assertTrue(manager.getValueForObjectManager(c30, prlvt1, true).equals("06/09/1983 10:00"));
      final Champ c34 = new Champ(champEntiteDao.findById(34));
      assertTrue(manager.getValueForObjectManager(c34, prlvt1, true).equals("10"));
      final Champ c37 = new Champ(champEntiteDao.findById(37));
      assertTrue(manager.getValueForObjectManager(c37, prlvt1, true).equals("-5.0"));
      final Champ c47 = new Champ(champEntiteDao.findById(47));
      assertTrue(manager.getValueForObjectManager(c47, prlvt1, true).equals("Oui"));

      // Mauvais paramétrage
      final Champ cWrong = new Champ(champEntiteDao.findById(24));
      assertNull(manager.getValueForObjectManager(cWrong, p1, true));
      assertNull(manager.getValueForObjectManager(new Champ(), p1, true));
      assertNull(manager.getValueForObjectManager(c1, null, true));
      assertNull(manager.getValueForObjectManager(null, p1, true));
      assertNull(manager.getValueForObjectManager(null, null, true));
   }

}
