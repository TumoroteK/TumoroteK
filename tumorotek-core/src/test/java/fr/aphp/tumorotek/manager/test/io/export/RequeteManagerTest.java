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

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.export.GroupementManager;
import fr.aphp.tumorotek.manager.io.export.RequeteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Groupement;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class RequeteManagerTest extends AbstractManagerTest4
{

   @Autowired
   private RequeteManager manager;

   @Autowired
   private GroupementManager groupementManager;

   @Autowired
   private BanqueDao banqueDao;

   @Autowired
   private UtilisateurDao utilisateurDao;

   @Test
   public void testFindByBanueManager(){
      final Banque b1 = banqueDao.findById(1);
      List<Requete> liste = manager.findByBanqueManager(b1);
      assertTrue(liste.size() == 3);

      final Banque b2 = banqueDao.findById(2);
      liste = manager.findByBanqueManager(b2);
      assertTrue(liste.size() == 1);

      final Banque b3 = banqueDao.findById(3);
      liste = manager.findByBanqueManager(b3);
      assertTrue(liste.size() == 0);

      liste = manager.findByBanqueManager(null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindByBanqueInLIstManager(){
      final List<Banque> bks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      bks.add(b1);
      List<Requete> liste = manager.findByBanqueInLIstManager(bks);
      assertTrue(liste.size() == 3);

      final Banque b2 = banqueDao.findById(2);
      bks.add(b2);
      liste = manager.findByBanqueInLIstManager(bks);
      assertTrue(liste.size() == 4);

      liste = manager.findByBanqueInLIstManager(new ArrayList<Banque>());
      assertTrue(liste.size() == 0);

      liste = manager.findByBanqueInLIstManager(null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testRenameRequete(){
      // On teste un renommage avec des attributs non valides
      // On boucle sur les 3 possibilités
      for(int i = 0; i < Math.pow(2, 2) - 1; i++){
         Requete requete = null;
         if(i >= 2){
            requete = manager.findByIdManager(3);
         }
         final int toTest = i % 2;
         String intitule = null;
         if(toTest > 0){
            intitule = "Intitulé";
         }
         try{
            manager.renameRequeteManager(requete, intitule);
         }catch(final RequiredObjectIsNullException e){
            assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
         }catch(final ValidationException e){
            assertEquals("ValidationException", e.getClass().getSimpleName());
         }
      }
      Requete requete = manager.findByIdManager(1);
      try{
         manager.renameRequeteManager(requete, requete.getIntitule());
      }catch(final DoublonFoundException e){
         assertEquals("DoublonFoundException", e.getClass().getSimpleName());
      }

      //On récupère une requete
      requete = manager.findByIdManager(2);
      final int idRequete = requete.getRequeteId();
      final String oldIntitule = requete.getIntitule();
      final String intitule = "MODIFICATION";
      manager.renameRequeteManager(requete, intitule);

      //on récupère l'objet modifié
      final Requete requete2 = manager.findByIdManager(idRequete);
      //On verifie la modification de l'intitule
      assertTrue(requete2.getIntitule().equals(intitule));

      //On remet l'intitule original
      manager.renameRequeteManager(requete2, oldIntitule);
   }

   /**
    * Teste la méthode findByIdManager.
    */
   @Test
   public void testFindById(){
      // On teste une recherche avec des attributs non valides
      try{
         manager.findByIdManager(null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      final Requete requete = manager.findByIdManager(1);
      assertNotNull(requete);
      assertTrue(requete.getGroupementRacine().getGroupementId().equals(1));

      final Requete requeteNull = manager.findByIdManager(50);
      assertNull(requeteNull);
   }

   /**
    * Teste la méthode findAllObjects.
    */
   @Test
   public void testFindAllObjects(){
      final List<Requete> list = manager.findAllObjectsManager();
      //On vérifie chaque élément par un findById
      final Iterator<Requete> it = list.iterator();
      while(it.hasNext()){
         final Requete temp = it.next();
         if(temp != null){
            assertTrue(temp.equals(manager.findByIdManager(temp.getRequeteId())));
         }
      }
   }


   @Test
   public void testIsUsedObjectManager(){
      final Requete r3 = manager.findByIdManager(3);
      assertTrue(manager.isUsedObjectManager(r3));

      final Requete r2 = manager.findByIdManager(2);
      assertTrue(manager.isUsedObjectManager(r2));

      final Requete r4 = manager.findByIdManager(4);
      assertFalse(manager.isUsedObjectManager(r4));

      assertFalse(manager.isUsedObjectManager(null));

      assertFalse(manager.isUsedObjectManager(new Requete()));
   }

}
