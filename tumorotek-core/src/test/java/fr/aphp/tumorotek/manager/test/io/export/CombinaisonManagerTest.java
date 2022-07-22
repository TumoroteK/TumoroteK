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

import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.export.CombinaisonManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Combinaison;

public class CombinaisonManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CombinaisonManager manager;

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

      final Combinaison combinaison = manager.findByIdManager(1);
      assertNotNull(combinaison);
      assertTrue(combinaison.getCombinaisonId().equals(1));

      final Combinaison combinaisonNull = manager.findByIdManager(5);
      assertNull(combinaisonNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAllObjects(){
      final List<Combinaison> list = manager.findAllObjectsManager();
      //On vérifie chaque élément par un findById
      final Iterator<Combinaison> it = list.iterator();
      while(it.hasNext()){
         final Combinaison temp = it.next();
         if(temp != null){
            assertTrue(temp.equals(manager.findByIdManager(temp.getCombinaisonId())));
         }
      }
   }

   @Test
   public void testCopyCombinaison(){
      // On teste une copie avec des attributs non valides
      try{
         manager.copyCombinaisonManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      //On boucle sur les 7 possibilités
      for(int i = 0; i < Math.pow(2, 3) - 1; i++){
         final Combinaison combinaison = new Combinaison();
         Champ champ1 = null;
         if(i >= 4){
            champ1 = new Champ(champEntiteDao.findById(2));
         }
         int toTest = i % 4;
         Champ champ2 = null;
         if(toTest >= 2){
            champ2 = new Champ(champEntiteDao.findById(3));
         }
         toTest = toTest % 2;
         String operateur = null;
         if(toTest > 0){
            operateur = "+";
         }
         combinaison.setChamp1(champ1);
         combinaison.setChamp2(champ2);
         combinaison.setOperateur(operateur);
         try{
            manager.copyCombinaisonManager(combinaison);
         }catch(final RequiredObjectIsNullException e){
            assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
         }catch(final ValidationException e){
            assertEquals("ValidationException", e.getClass().getSimpleName());
         }
      }

      final Combinaison combinaison = manager.findByIdManager(1);
      final Combinaison copie = manager.copyCombinaisonManager(combinaison);
      assertTrue(copie.getCombinaisonId() > 0);
      //On verifie que la combinaison et la copie sont identiques
      assertTrue(combinaison.equals(copie));
      //On verifie que la copie est bien en BDD
      assertTrue(copie.equals(manager.findByIdManager(copie.getCombinaisonId())));

      //On supprime les éléments créés
      manager.removeObjectManager(copie);
   }

   @Test
   public void testCrud(){
      /** On teste une création et une suppression avec des attributs 
       * non valides. */

      //On boucle sur les 7 possibilités
      for(int i = 0; i < Math.pow(2, 3) - 1; i++){
         final Combinaison combinaison = new Combinaison();
         Champ champ1 = null;
         if(i >= 4){
            champ1 = new Champ(champEntiteDao.findById(2));
         }
         int toTest = i % 4;
         Champ champ2 = null;
         if(toTest >= 2){
            champ2 = new Champ(champEntiteDao.findById(3));
         }
         toTest = toTest % 2;
         String operateur = null;
         if(toTest > 0){
            operateur = "+";
         }
         combinaison.setOperateur(operateur);
         try{
            manager.createObjectManager(combinaison, champ1, champ2);
         }catch(final RequiredObjectIsNullException e){
            assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
         }catch(final ValidationException e){
            assertEquals("ValidationException", e.getClass().getSimpleName());
         }
         try{
            manager.updateObjectManager(combinaison, champ1, champ2);
         }catch(final RequiredObjectIsNullException e){
            assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
         }catch(final ValidationException e){
            assertEquals("ValidationException", e.getClass().getSimpleName());
         }
      }
      /** On teste une suppression invalide. */
      try{
         manager.removeObjectManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      try{
         manager.removeObjectManager(new Combinaison());
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      /** Save. */
      Champ champ1 = new Champ(champEntiteDao.findById(2));
      Champ champ2 = new Champ(champEntiteDao.findById(3));
      final Combinaison combinaison = new Combinaison();
      combinaison.setOperateur("-");
      manager.createObjectManager(combinaison, champ1, champ2);
      assertTrue(combinaison.getCombinaisonId() > 0);
      //On récupère la combinaison depuis la BDD
      Combinaison combinaison2 = manager.findByIdManager(combinaison.getCombinaisonId());
      //On vérifie l'égalité des deux
      assertTrue(combinaison.equals(combinaison2));

      /** Update. */
      final String oldOperateur = combinaison.getOperateur();
      final String operateur = "+";
      combinaison.setOperateur(operateur);
      final Champ oldChamp1 = combinaison.getChamp1();
      final Champ oldChamp2 = combinaison.getChamp2();
      champ1 = new Champ(champEntiteDao.findById(45));
      champ2 = new Champ(champEntiteDao.findById(13));

      manager.updateObjectManager(combinaison, champ1, champ2);

      assertFalse(combinaison.getOperateur().equals(oldOperateur));
      assertTrue(combinaison.getOperateur().equals(operateur));
      assertFalse(combinaison.getChamp1().equals(oldChamp1));
      assertTrue(combinaison.getChamp1().equals(champ1));
      assertFalse(combinaison.getChamp2().equals(oldChamp2));
      assertTrue(combinaison.getChamp2().equals(champ2));

      /** Delete. */
      //On récupère une combinaison
      final int id = combinaison.getCombinaisonId();
      //On le supprime
      manager.removeObjectManager(combinaison);
      //On essaye de la récupérer via l'identifiant
      combinaison2 = manager.findByIdManager(id);
      //On vérifie qu'elle n'existe pas dans la liste du manager
      assertNull(combinaison2);

   }
}
