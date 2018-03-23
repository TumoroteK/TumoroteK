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
import fr.aphp.tumorotek.manager.io.export.CritereManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Combinaison;
import fr.aphp.tumorotek.model.io.export.Critere;

public class CritereManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CritereManager manager;
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

      final Critere critere = manager.findByIdManager(1);
      assertNotNull(critere);
      assertTrue(critere.getCritereId().equals(1));

      final Critere critereNull = manager.findByIdManager(50);
      assertNull(critereNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAllObjects(){
      final List<Critere> list = manager.findAllObjectsManager();
      //On vérifie chaque élément par un findById
      final Iterator<Critere> it = list.iterator();
      while(it.hasNext()){
         final Critere temp = it.next();
         if(temp != null){
            assertTrue(temp.equals(manager.findByIdManager(temp.getCritereId())));
         }
      }
   }

   @Test
   public void testCopyCritere(){
      // On teste une copie avec des attributs non valides
      try{
         manager.copyCritereManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      try{
         manager.copyCritereManager(new Critere());
      }catch(final ValidationException e){
         assertEquals("ValidationException", e.getClass().getSimpleName());
      }
      Critere critere = new Critere();
      critere.setChamp(new Champ());
      critere.setCombinaison(new Combinaison());
      try{
         manager.copyCritereManager(new Critere());
      }catch(final ValidationException e){
         assertEquals("ValidationException", e.getClass().getSimpleName());
      }

      critere = manager.findByIdManager(1);
      final Critere copie = manager.copyCritereManager(critere);
      assertTrue(copie.getCritereId() > 0);
      //On verifie que le critere et la copie sont identiques
      assertTrue(critere.equals(copie));
      //On verifie que la copie est bien en BDD
      assertTrue(copie.equals(manager.findByIdManager(copie.getCritereId())));

      //On supprime les éléments créés
      manager.removeObjectManager(copie);
   }

   @Test
   public void testCrud(){
      /** On teste une création et une modification avec des attributs 
       * non valides. */
      //Création et modification pour Critere null Champ null Combinaison null
      try{
         manager.createObjectManager(null, null, null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      try{
         manager.updateObjectManager(null, null, null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      //Création et modification pour Critere null Champ OK Combinaison OK
      try{
         manager.createObjectManager(null, new Champ(), new Combinaison());
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      try{
         manager.updateObjectManager(null, new Champ(), new Combinaison());
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      //Création et modification pour Critere OK Champ null Combinaison null
      try{
         manager.createObjectManager(new Critere(), null, null);
      }catch(final ValidationException e){
         assertEquals("ValidationException", e.getClass().getSimpleName());
      }
      try{
         manager.updateObjectManager(new Critere(), null, null);
      }catch(final ValidationException e){
         assertEquals("ValidationException", e.getClass().getSimpleName());
      }

      /** On teste une suppression invalide. */
      try{
         manager.removeObjectManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      try{
         manager.removeObjectManager(new Critere());
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      /** Save. */
      final Champ champ = new Champ(champEntiteDao.findById(20));
      final Critere critere = new Critere(champ, "=", "07B0%");
      manager.createObjectManager(critere, champ, null);
      assertTrue(critere.getCritereId() > 0);
      //On recupere le critere depuis la BDD
      Critere critere2 = manager.findByIdManager(critere.getCritereId());
      //On verifie l'egalite des deux
      assertTrue(critere.equals(critere2));

      /** Update. */
      final String oldValeur = critere.getValeur();
      final String valeur = "val2";
      critere.setValeur(valeur);

      manager.updateObjectManager(critere, champ, null);

      assertFalse(critere.getValeur().equals(oldValeur));
      assertTrue(critere.getValeur().equals(valeur));

      /** Delete. */
      //On récupère un critere
      final int id = critere.getCritereId();
      //On le supprime
      manager.removeObjectManager(critere);
      //On essaye de le récupérer via l'identifiant
      critere2 = manager.findByIdManager(id);
      //On vérifie qu'il n'existe pas dans la liste du manager
      assertNull(critere2);
   }
}
