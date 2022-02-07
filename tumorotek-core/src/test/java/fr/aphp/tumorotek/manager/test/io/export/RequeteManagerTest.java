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
      final Banque b1 = banqueDao.findById(1).orElse(null);
      List<Requete> liste = manager.findByBanqueManager(b1);
      assertTrue(liste.size() == 3);

      final Banque b2 = banqueDao.findById(2).orElse(null);
      liste = manager.findByBanqueManager(b2);
      assertTrue(liste.size() == 1);

      final Banque b3 = banqueDao.findById(3).orElse(null);
      liste = manager.findByBanqueManager(b3);
      assertTrue(liste.size() == 0);

      liste = manager.findByBanqueManager(null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindByBanqueInLIstManager(){
      final List<Banque> bks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1).orElse(null);
      bks.add(b1);
      List<Requete> liste = manager.findByBanqueInLIstManager(bks);
      assertTrue(liste.size() == 3);

      final Banque b2 = banqueDao.findById(2).orElse(null);
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

   @Test
   public void testCopyRequete(){
      // On teste une copie avec des attributs non valides
      // On boucle sur les 3 possibilités
      final Banque b = banqueDao.findById(1).orElse(null);
      for(int i = 0; i < Math.pow(2, 2) - 1; i++){
         Requete requete = null;
         if(i >= 2){
            requete = manager.findByIdManager(3);
         }
         final int toTest = i % 2;
         Utilisateur copieur = null;
         if(toTest > 0){
            copieur = utilisateurDao.findById(2).orElse(null);
         }
         try{
            manager.copyRequeteManager(requete, copieur, b);
         }catch(final RequiredObjectIsNullException e){
            assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
         }
      }

      Requete requete = manager.findByIdManager(3);
      final Utilisateur copieur = utilisateurDao.findById(2).orElse(null);
      try{
         manager.copyRequeteManager(requete, copieur, null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      final Utilisateur util = utilisateurDao.findById(1).orElse(null);
      //On récupère la premiere requete
      requete = manager.findByIdManager(4);
      final Requete copie = manager.copyRequeteManager(requete, util, b);

      //On verifie que la requete et la copie sont identiques
      assertTrue(manager.isCopyManager(requete, copie));

      //On verifie que la copie est bien en base de donnees
      final Requete copie2 = manager.findByIdManager(copie.getRequeteId());
      assertNotNull(copie2);

      //On supprime les éléments créés
      manager.removeObjectManager(copie2);
   }

   @Test
   public void testCrud(){
      /** On teste une création et une suppression avec des attributs 
       * non valides. */
      final Banque b = banqueDao.findById(1).orElse(null);
      //On boucle sur les 7 possibilités
      for(int i = 0; i < Math.pow(2, 3) - 1; i++){
         Requete requete = null;
         if(i >= 4){
            requete = manager.findByIdManager(2);
         }
         int toTest = i % 4;
         Groupement groupement = null;
         if(toTest >= 2){
            groupement = groupementManager.findByIdManager(3);
         }
         toTest = toTest % 2;
         Utilisateur createur = null;
         if(toTest > 0){
            createur = utilisateurDao.findById(2).orElse(null);
         }
         try{
            manager.createObjectManager(requete, groupement, createur, b);
         }catch(final RequiredObjectIsNullException e){
            assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
         }
         try{
            manager.updateObjectManager(requete, groupement, createur);
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
         manager.removeObjectManager(new Requete());
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      /** Save. */
      //On cree une requete
      Utilisateur util = utilisateurDao.findById(1).orElse(null);
      Groupement groupement = groupementManager.findByIdManager(1);
      final Groupement gCopie = groupementManager.copyGroupementManager(groupement);
      final Requete requete = new Requete();
      try{
         manager.createObjectManager(requete, groupement, util, null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      requete.setIntitule("requete one");
      manager.createObjectManager(requete, gCopie, util, b);
      //On verifie que la sauvegarde a ete correctement effectuee
      Requete requete2 = manager.findByIdManager(requete.getRequeteId());
      assertNotNull(requete2);
      assertTrue(requete.equals(requete2));

      /** Update. */
      util = utilisateurDao.findById(3).orElse(null);
      groupement = groupementManager.findByIdManager(2);
      final Groupement gCopie2 = groupementManager.copyGroupementManager(groupement);
      requete2.setIntitule("requete two");

      manager.updateObjectManager(requete2, gCopie2, util);

      assertFalse(requete.equals(requete2));
      assertFalse(requete.getCreateur().equals(requete2.getCreateur()));
      assertFalse(requete.getIntitule().equals(requete2.getIntitule()));
      assertFalse(requete.getGroupementRacine().equals(requete2.getGroupementRacine()));

      /** Remove. */
      //On récupère une requete
      final int id = requete.getRequeteId();
      //On la supprime
      manager.removeObjectManager(requete2);
      //On essaye de la récupèrer via l'identifiant
      requete2 = manager.findByIdManager(id);
      //On verifie qu'elle n'existe pas dans la liste du manager
      assertNull(requete2);

      // test de la suppresion d'un objet utilisé
      final Requete r3 = manager.findByIdManager(3);
      boolean catched = false;
      try{
         manager.removeObjectManager(r3);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(manager.findAllObjectsManager().size() == 4);
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

   /**
    * Teste la méthode findByUtilisateur.
    */
   @Test
   public void testFindByUtilisateur(){
      final Iterator<Utilisateur> itUtil = IterableUtils.toList(utilisateurDao.findAll()).iterator();
      while(itUtil.hasNext()){
         final Utilisateur utilisateur = itUtil.next();
         final Iterator<Requete> it = manager.findByUtilisateurManager(utilisateur).iterator();
         while(it.hasNext()){
            final Requete temp = it.next();
            if(temp != null){
               assertEquals(temp.getCreateur(), utilisateur);
            }
         }
      }
   }

   @Test
   public void testFindByIntituleAndUtilisateurManager(){
      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);
      final Utilisateur u2 = utilisateurDao.findById(2).orElse(null);
      List<Requete> liste = manager.findByIntituleAndUtilisateurManager("Echantillon%", u1);
      assertTrue(liste.size() == 1);

      liste = manager.findByIntituleAndUtilisateurManager("yug%", u1);
      assertTrue(liste.size() == 0);

      liste = manager.findByIntituleAndUtilisateurManager("Echantillon%", u2);
      assertTrue(liste.size() == 0);

      liste = manager.findByIntituleAndUtilisateurManager(null, u1);
      assertTrue(liste.size() == 0);

      liste = manager.findByIntituleAndUtilisateurManager("Echantillon%", null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindDoublons(){
      final Requete requete = new Requete();
      final Utilisateur createur = utilisateurDao.findById(1).orElse(null);
      requete.setCreateur(createur);
      requete.setIntitule("Echantillon en attente de consentement");

      assertTrue(manager.findDoublonManager(requete));

      requete.setIntitule("Echantillons");
      assertFalse(manager.findDoublonManager(requete));
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
