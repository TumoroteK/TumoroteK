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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.export.CritereManager;
import fr.aphp.tumorotek.manager.io.export.GroupementManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Groupement;

public class GroupementManagerTest extends AbstractManagerTest4
{

   @Autowired
   private GroupementManager manager;

   @Autowired
   private CritereManager critereManager;

   @Autowired
   private ChampEntiteDao champEntiteDao;

   @Test
   public void testFindById(){
      // On teste une recherche avec des attributs non valides
      try{
         manager.findByIdManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      final Groupement groupement = manager.findByIdManager(1);
      assertNotNull(groupement);
      assertTrue(groupement.getGroupementId().equals(1));

      final Groupement groupementNull = manager.findByIdManager(50);
      assertNull(groupementNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAllObjects(){
      final List<Groupement> list = manager.findAllObjectsManager();
      //On vérifie chaque élément par un findById
      final Iterator<Groupement> it = list.iterator();
      while(it.hasNext()){
         final Groupement temp = it.next();
         if(temp != null){
            assertEquals(manager.findByIdManager(temp.getGroupementId()), temp);
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

      final List<Groupement> list = manager.findAllObjectsManager();
      final Iterator<Groupement> it = list.iterator();
      while(it.hasNext()){
         final Groupement temp = it.next();
         if(temp != null){
            final List<Groupement> enfants = manager.findEnfantsManager(temp);
            final Iterator<Groupement> it2 = enfants.iterator();
            while(it2.hasNext()){
               final Groupement tempEnfant = it2.next();
               if(tempEnfant != null){
                  assertEquals(temp, tempEnfant.getParent());
               }
            }
         }
      }
   }

   /**
    * Test la méthode findEnfantsManager.
    */
   @Test
   public void testFindCriteres(){
      // On teste une recherche d'enfants avec des attributs non valides
      try{
         manager.findCriteresManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      final List<Groupement> list = manager.findAllObjectsManager();
      final Iterator<Groupement> it = list.iterator();
      while(it.hasNext()){
         final Groupement temp = it.next();
         if(temp != null){
            final List<Critere> criteres = findCriteres(temp);
            final Iterator<Critere> itCrit = manager.findCriteresManager(temp).iterator();
            while(itCrit.hasNext()){
               final Critere crit = itCrit.next();
               if(crit != null){
                  assertTrue(criteres.contains(crit));
               }
            }

         }
      }
   }

   /**
    * Test la méthode copyGroupementManager.
    */
   @Test
   public void testCopyGroupement(){
      // On teste une copie avec des attributs non valides
      try{
         manager.copyGroupementManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      /*Groupement groupement = new Groupement();
      try {
      	manager.copyGroupementManager(groupement);
      } catch (ValidationException e) {
      	assertEquals("ValidationException",
      			e.getClass().getSimpleName());
      }*/

      //On recupere un groupement
      final Groupement groupement = manager.findByIdManager(2);
      final Groupement copie = manager.copyGroupementManager(groupement);
      //On verifie que le groupement et la copie sont identiques
      assertTrue(manager.isCopyManager(groupement, copie));

      //On verifie que la copie est bien en base de donnees
      final Groupement copie2 = manager.findByIdManager(copie.getGroupementId());
      assertNotNull(copie2);

      //On supprime les éléments créés
      manager.removeObjectManager(copie);
   }

   /*
   @Test
   public void testToString() {
   	// On teste un affichage avec des attributs non valides
   	try {
   		manager.toStringManager(null);
   	} catch (RequiredObjectIsNullException e) {
   		assertEquals("RequiredObjectIsNullException",
   				e.getClass().getSimpleName());
   	}
   	
   	Groupement groupement = manager.findByIdManager(2);
   	String string = manager.toStringManager(groupement);
   	assertNotNull(string);
   	assertFalse(string.equals("") || string.equals(" "));
   }*/

   @Test
   public void testCrud(){
      /** On teste une création et une suppression avec des attributs non
       * valides. */
      try{
         manager.createObjectManager(null, new Critere(), new Critere(), "=", new Groupement());
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      try{
         manager.updateObjectManager(null, new Critere(), new Critere(), "!=", new Groupement());
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      /** On teste une suppression invalide. */
      try{
         manager.removeObjectManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      try{
         manager.removeObjectManager(new Groupement());
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      /** Save. */
      Champ champ = new Champ(champEntiteDao.findById(10));
      Critere critere1 = new Critere(champ, "=", "valeurDefaut");
      Critere critere2 = null;
      String operateur = "and";
      Groupement parent = manager.findByIdManager(3);
      //On créé un groupement
      final Groupement groupement = new Groupement();
      manager.createObjectManager(groupement, critere1, critere2, operateur, parent);
      //On vérifie que la sauvegarde a été correctement effectuée
      Groupement groupement2 = manager.findByIdManager(groupement.getGroupementId());
      assertTrue(groupement.equals(groupement2));

      /** Update. */
      final Critere oldCritere1 = groupement.getCritere1();
      champ = new Champ(champEntiteDao.findById(20));
      critere1 = new Critere(champ, "!=", "vDef");
      groupement.setCritere1(critere1);
      final Critere oldCritere2 = groupement.getCritere2();
      champ = new Champ(champEntiteDao.findById(30));
      critere2 = new Critere(champ, "=", "0.0");
      groupement.setCritere2(critere2);
      final String oldOperateur = groupement.getOperateur();
      operateur = "or";
      groupement.setOperateur(operateur);
      final Groupement oldParent = groupement.getParent();
      parent = manager.findByIdManager(2);
      groupement.setParent(parent);

      manager.updateObjectManager(groupement, critere1, critere2, operateur, parent);

      if(groupement.getOperateur() != null){
         assertFalse(groupement.getOperateur().equals(oldOperateur));
         assertTrue(groupement.getOperateur().equals(operateur));
      }else{
         assertFalse(oldOperateur != null);
      }
      if(groupement.getCritere1() != null){
         assertFalse(groupement.getCritere1().equals(oldCritere1));
         assertTrue(groupement.getCritere1().equals(critere1));
      }else{
         assertFalse(oldCritere1 != null);
      }
      if(groupement.getCritere2() != null){
         assertFalse(groupement.getCritere2().equals(oldCritere2));
         assertTrue(groupement.getCritere2().equals(critere2));
      }else{
         assertFalse(oldCritere2 != null);
      }
      if(groupement.getParent() != null){
         assertFalse(groupement.getParent().equals(oldParent));
         assertEquals(parent, groupement.getParent());
      }else{
         assertFalse(oldParent != null);
         assertTrue(parent == null);
      }

      /** Remove. */
      final int id = groupement.getGroupementId();
      //On le supprime
      manager.removeObjectManager(groupement);
      //On essaye de le récupérer via l'identifiant
      groupement2 = manager.findByIdManager(id);
      //On verifie qu'il n'existe pas dans la liste du manager
      assertNull(groupement2);

      //On supprime les éléments créés
      critereManager.removeObjectManager(oldCritere1);
   }

   private List<Critere> findCriteres(final Groupement groupement){
      final List<Critere> criteres = new ArrayList<>();
      // On vérifie que le groupement n'est pas nul
      if(groupement != null){
         if(groupement.getCritere1() != null){
            criteres.add(groupement.getCritere1());
         }
         if(groupement.getCritere2() != null){
            criteres.add(groupement.getCritere2());
         }
         final Iterator<Groupement> it = manager.findEnfantsManager(groupement).iterator();
         while(it.hasNext()){
            final Groupement temp = it.next();
            criteres.addAll(findCriteres(temp));
            if(temp.getCritere1() != null){
               criteres.add(temp.getCritere1());
            }
            if(temp.getCritere2() != null){
               criteres.add(temp.getCritere2());
            }
         }
      }
      return criteres;
   }

}
