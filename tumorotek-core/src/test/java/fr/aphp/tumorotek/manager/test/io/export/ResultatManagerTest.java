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

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.io.export.AffichageDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.export.ResultatManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.utils.io.ConstanteIO;

public class ResultatManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ResultatManager manager;
   @Autowired
   private ChampEntiteDao champEntiteDao;
   @Autowired
   private AffichageDao affichageDao;

   @Test
   public void testFindById(){
      // On teste une recherche avec des attributs non valides
      try{
         manager.findByIdManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      final Resultat resultat = manager.findByIdManager(3);
      assertNotNull(resultat);
      assertTrue(resultat.getResultatId().equals(3));

      final Resultat resultatNull = manager.findByIdManager(50);
      assertNull(resultatNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAllObjects(){
      final List<Resultat> list = manager.findAllObjectsManager();
      //On vérifie chaque élément par un findById
      final Iterator<Resultat> it = list.iterator();
      while(it.hasNext()){
         final Resultat temp = it.next();
         if(temp != null){
            assertTrue(temp.equals(manager.findByIdManager(temp.getResultatId())));
         }
      }
   }

   @Test
   public void testCopyResultat(){
      // On teste une copie avec des attributs non valides
      // On boucle sur les 3 possibilités
      for(int i = 0; i < Math.pow(2, 2) - 1; i++){
         Resultat resultat = null;
         if(i >= 2){
            resultat = manager.findByIdManager(3);
         }
         final int toTest = i % 2;
         Affichage affichage = null;
         if(toTest > 0){
            affichage = affichageDao.findById(2).orElse(null);
         }
         try{
            manager.copyResultatManager(resultat, affichage);
         }catch(final RequiredObjectIsNullException e){
            assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
         }
      }

      final Affichage affichage = affichageDao.findById(3).orElse(null);
      final Resultat resultat = manager.findByIdManager(4);
      final Resultat copie = manager.copyResultatManager(resultat, affichage);
      assertTrue(copie.getResultatId() > 0);
      //On verifie que le resultat et la copie sont identiques
      assertTrue(resultat.isCopy(copie));
      //On verifie que la copie est bien en BDD
      assertTrue(copie.equals(manager.findByIdManager(copie.getResultatId())));

      //On supprime les éléments créés
      manager.removeObjectManager(copie);
   }

   @Test
   public void testCrud(){
      /** On teste une création et une suppression avec des attributs 
       * non valides. */

      //On boucle sur les 7 possibilités
      for(int i = 0; i < Math.pow(2, 3) - 1; i++){
         Resultat resultat = null;
         if(i >= 4){
            resultat = manager.findByIdManager(2);
         }
         int toTest = i % 4;
         Affichage affichage = null;
         if(toTest >= 2){
            affichage = affichageDao.findById(3).orElse(null);
         }
         toTest = toTest % 2;
         Champ champ = null;
         if(toTest > 0){
            champ = new Champ(champEntiteDao.findById(29)).orElse(null);
         }
         try{
            manager.createObjectManager(resultat, affichage, champ);
         }catch(final RequiredObjectIsNullException e){
            assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
         }
         try{
            manager.updateObjectManager(resultat, affichage, champ);
         }catch(final RequiredObjectIsNullException e){
            assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
         }
      }
      /** On teste une suppression invalide. */
      try{
         manager.removeObjectManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      try{
         manager.removeObjectManager(new Resultat());
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      /** Save. */
      final Affichage affichage = affichageDao.findById(3).orElse(null);
      final Champ champ = new Champ(champEntiteDao.findById(56)).orElse(null);
      Resultat resultat = new Resultat("columnName", champ, ConstanteIO.TRI_ASC, 2, 1, "", affichage);
      manager.createObjectManager(resultat, affichage, champ);
      assertTrue(resultat.getResultatId() > 0);
      //On recupere le résultat depuis la BDD
      Resultat resultat2 = manager.findByIdManager(resultat.getResultatId());
      //On verifie l'égalité des deux
      assertTrue(resultat.equals(resultat2));
      /** Update. */
      resultat2 = manager.findByIdManager(resultat.getResultatId());

      resultat.setNomColonne("MODIFICATION");
      final Champ champ2 = new Champ(champEntiteDao.findById(58)).orElse(null);

      manager.updateObjectManager(resultat, resultat.getAffichage(), champ2);

      assertFalse(resultat.equals(resultat2));
      assertFalse(resultat.getNomColonne().equals(resultat2.getNomColonne()));
      assertTrue(manager.findByIdManager(resultat.getResultatId()).getChamp().getChampEntite().getId().equals(58));
      /** Remove. */
      final int id = resultat.getResultatId();
      manager.removeObjectManager(resultat);
      resultat = manager.findByIdManager(id);
      assertNull(resultat);
   }

   @Test
   public void testFindByAffichage(){
      try{
         manager.findByAffichageManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      final List<Affichage> affichages = IterableUtils.toList(affichageDao.findAll());
      //On vérifie chaque élément par un findById
      final Iterator<Affichage> it = affichages.iterator();
      while(it.hasNext()){
         final Affichage temp = it.next();
         if(temp != null){
            final List<Resultat> list = manager.findByAffichageManager(temp);
            //On vérifie chaque élément par un findById
            final Iterator<Resultat> it2 = list.iterator();
            while(it2.hasNext()){
               final Resultat res = it2.next();
               if(res != null){
                  assertEquals(temp, res.getAffichage());
               }
            }
         }
      }
   }
}
