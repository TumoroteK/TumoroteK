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
package fr.aphp.tumorotek.test.manager.io.export;

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
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.io.export.ResultatDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.export.AffichageManager;
import fr.aphp.tumorotek.manager.io.export.ResultatManager;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;
import fr.aphp.tumorotek.utils.io.ConstanteIO;

public class AffichageManagerTest extends AbstractManagerTest4
{

   @Autowired
   private AffichageManager manager;
   @Autowired
   private ResultatManager resultatManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private ResultatDao resultatDao;
   @Autowired
   private ChampEntiteDao champEntiteDao;
   @Autowired
   private BanqueDao banqueDao;

   @Test
   public void testFindById(){
      final Affichage affichage = manager.findByIdManager(1);
      assertNotNull(affichage);
      assertTrue(affichage.getNbLignes().equals(20));

      final Affichage affichageNull = manager.findByIdManager(5);
      assertNull(affichageNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAllObjects(){
      final List<Affichage> list = manager.findAllObjectsManager();
      //On vérifie chaque élément par un findById
      final Iterator<Affichage> it = list.iterator();
      while(it.hasNext()){
         final Affichage temp = it.next();
         if(temp != null){
            assertTrue(temp.equals(manager.findByIdManager(temp.getAffichageId())));
         }
      }
   }

   @Test
   public void testFindByBanueManager(){
      final Banque b1 = banqueDao.findById(1);
      List<Affichage> liste = manager.findByBanqueManager(b1);
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
      List<Affichage> liste = manager.findByBanqueInLIstManager(bks);
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
   public void testCopyAffichage(){
      //On récupère un affichage
      Affichage affichage = manager.findByIdManager(4);
      assertNotNull(affichage);
      final Utilisateur util = utilisateurDao.findById(3);
      final Banque b1 = banqueDao.findById(1);

      // On teste une copie avec des attributs non valides
      try{
         manager.copyAffichageManager(affichage, null, b1);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      try{
         manager.copyAffichageManager(null, util, b1);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }
      try{
         manager.copyAffichageManager(affichage, util, null);
      }catch(final RequiredObjectIsNullException e){
         assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
      }

      // On récupère le premier affichage
      affichage = manager.findByIdManager(1);
      final Affichage copie = manager.copyAffichageManager(affichage, util, b1);

      //MAJ des résultats
      affichage.setResultats(resultatDao.findByAffichage(affichage));
      copie.setResultats(resultatDao.findByAffichage(copie));

      //On vérifie que l'affichage et la copie sont identiques
      assertTrue(manager.isCopyManager(affichage, copie));

      //On vérifie que la copie est bien en base de données
      final Affichage copie2 = manager.findByIdManager(copie.getAffichageId());
      assertNotNull(copie2);

      //On supprime les éléments créés
      manager.removeObjectManager(copie);
   }

   @Test
   public void testRenameAffichage(){
      //On récupère un affichage
      Affichage affichage = manager.findByIdManager(4);
      assertNotNull(affichage);

      // On teste une insertion avec des attributs non valides
      final String[] intituleValues = new String[] {"", "  ", null, "$$hfu_|(", createOverLength(100)};
      for(int i = 0; i < intituleValues.length; i++){
         try{
            manager.renameAffichageManager(affichage, intituleValues[i]);
         }catch(final ValidationException e){
            assertEquals("ValidationException", e.getClass().getSimpleName());
         }
         try{
            manager.renameAffichageManager(null, intituleValues[i]);
         }catch(final RequiredObjectIsNullException e){
            assertEquals("RequiredObjectIsNullException", e.getClass().getSimpleName());
         }
      }

      //On récupère un affichage
      affichage = manager.findByIdManager(1);
      assertNotNull(affichage);
      //On teste un affichage valide
      final String oldIntitule = affichage.getIntitule();
      final String intitule = affichage.getIntitule() + " MODIFICATION";
      manager.renameAffichageManager(affichage, intitule);

      //On récupère l'objet modifié
      final Affichage affichage2 = manager.findByIdManager(affichage.getAffichageId());
      //On vérifie la modification de l'intitulé
      assertTrue(affichage2.getIntitule().equals(intitule));

      //On réécrit l'intitule original
      manager.renameAffichageManager(affichage, oldIntitule);
   }

   @Test
   public void testAddResultat(){
      manager.findAllObjectsManager();
      //On récupère un affichage
      final Affichage affichage = manager.findByIdManager(3);
      //On récupère un champ
      final Champ champ = new Champ(champEntiteDao.findById(20));
      //On crée un résultat
      final Resultat resultat = new Resultat("code derive", champ, ConstanteIO.TRI_ASC, 3, 1, "Chaine", affichage);

      //MAJ des résultats
      affichage.setResultats(resultatDao.findByAffichage(affichage));

      //On l'ajoute dans la liste de l'affichage
      manager.addResultatManager(affichage, resultat);

      //On vérifie que le résultat a un identifiant
      assertTrue(resultat.getResultatId() > 0);
      //On vérifie que le résultat est bien dans la liste de l'affichage
      final Affichage affichage2 = manager.findByIdManager(affichage.getAffichageId());
      boolean test = false;
      affichage2.setResultats(resultatDao.findByAffichage(affichage2));
      final Iterator<Resultat> it = affichage2.getResultats().iterator();
      while(it.hasNext()){
         final Resultat temp = it.next();
         if(temp.getResultatId().equals(resultat.getResultatId())){
            test = true;
            break;
         }
      }
      assertTrue(test);

      //On supprime les éléments créés
      manager.removeResultatManager(affichage2, resultat);
   }

   @Test
   public void testFindByUtilisateur(){
      final Utilisateur util = utilisateurDao.findById(1);
      final List<Affichage> affichages = manager.findByUtilisateurManager(util);
      final Iterator<Affichage> it = affichages.iterator();
      while(it.hasNext()){
         final Affichage temp = it.next();
         assertTrue(temp.getCreateur().equals(util));
      }
   }

   @Test
   public void testFindByIntituleAndUtilisateurManager(){
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Utilisateur u2 = utilisateurDao.findById(2);
      List<Affichage> liste = manager.findByIntituleAndUtilisateurManager("Essen%", u1);
      assertTrue(liste.size() == 1);

      liste = manager.findByIntituleAndUtilisateurManager("yug%", u1);
      assertTrue(liste.size() == 0);

      liste = manager.findByIntituleAndUtilisateurManager("Essen%", u2);
      assertTrue(liste.size() == 0);

      liste = manager.findByIntituleAndUtilisateurManager(null, u1);
      assertTrue(liste.size() == 0);

      liste = manager.findByIntituleAndUtilisateurManager("Essen%", null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testMoveResultat(){
      final Affichage affichage = manager.findByIdManager(2);

      //MAJ des resultats
      affichage.setResultats(resultatDao.findByAffichage(affichage));

      final Resultat resultat = affichage.getResultats().get(0);
      final int idResultat = resultat.getResultatId();
      final int positionInitiale = resultat.getPosition();

      manager.moveResultatManager(affichage, resultat, 1);

      //On vérifie que la position en BDD a été modifiée
      final Resultat resultat2 = resultatDao.findById(idResultat);
      assertFalse(resultat2.getPosition().equals(positionInitiale));

      //On vérifie que la position dans la liste a été modifiée
      assertFalse(affichage.getResultats().get(0).getPosition().equals(positionInitiale));

      //On replace les éléments comme ils étaient
      manager.moveResultatManager(affichage, resultat2, positionInitiale);

   }

   @Test
   public void testCrud(){
      /** Save. */
      final Utilisateur util = utilisateurDao.findById(1);

      final Champ champ1 = new Champ(champEntiteDao.findById(3));
      final Resultat resultat1 = new Resultat("col1", champ1, true, 1, 2, "format", null);
      final Champ champ2 = new Champ(champEntiteDao.findById(4));
      final Resultat resultat2 = new Resultat("col2", champ2, false, 2, 1, null, null);
      final Banque b1 = banqueDao.findById(1);

      final List<Resultat> res = new ArrayList<>();
      res.add(resultat1);
      res.add(resultat2);

      //On créé un affichage
      final Affichage affichage = new Affichage("test enregistrement affichage", util, 20);
      manager.saveManager(affichage, res, util, b1);
      //On vérifie que la sauvegarde a été correctement effectuée
      Affichage affichage2 = manager.findByIdManager(affichage.getAffichageId());
      assertNotNull(affichage2);

      //MAJ des résultats
      affichage.setResultats(resultatDao.findByAffichage(affichage));
      affichage2.setResultats(resultatDao.findByAffichage(affichage2));

      assertTrue(affichage.equals(affichage2));

      /** Update. */
      final String oldIntitule = affichage.getIntitule();
      final String intitule = "test modification affichage";
      affichage.setIntitule(intitule);

      final Champ champ3 = new Champ(champEntiteDao.findById(1));
      final Resultat resultat3 = new Resultat("col3", champ3, true, 3, 3, null, null);

      //MAJ des résultats
      final List<Resultat> resultats = resultatDao.findByAffichage(affichage);
      resultats.add(resultat3);

      manager.saveManager(affichage, resultats, null);

      assertFalse(affichage.getIntitule().equals(oldIntitule));
      assertTrue(affichage.getIntitule().equals(intitule));

      //On vérifie que le résultat 3 est dans la liste du manager
      boolean found = false;
      Iterator<Resultat> it = resultatDao.findByAffichage(affichage).iterator();
      while(it.hasNext()){
         final Resultat temp = it.next();
         if(temp.getResultatId().equals(resultat3.getResultatId())){
            found = true;
            break;
         }
      }
      assertTrue(found);

      //On verifie que le resultat 3 est
      // dans la liste des resultats de l'affichage
      affichage2 = manager.findByIdManager(affichage.getAffichageId());
      Resultat resultat = resultatManager.findByIdManager(resultat3.getResultatId());
      assertNotNull(resultat);

      //On teste la suppression de résultats
      final int idResultat = resultat.getResultatId();
      manager.removeResultatManager(affichage, resultat);

      //On vérifie que le résultat n'est plus en BDD
      resultat = resultatDao.findById(idResultat);

      assertNull(resultat);
      //On vérifie que le résultat n'est pas non plus dans la liste du manager
      found = false;
      it = affichage.getResultats().iterator();
      while(it.hasNext()){
         final Resultat temp = it.next();
         if(temp.getResultatId().equals(idResultat)){
            found = true;
            break;
         }
      }
      assertFalse(found);

      /** Delete. */
      //On récupère un affichage
      final int id = affichage.getAffichageId();
      //On le supprime
      manager.removeObjectManager(affichage);
      //On essaye de le récupérer via l'identifiant
      affichage2 = manager.findByIdManager(id);
      //On vérifie qu'il n'existe pas dans la liste du manager
      assertNull(affichage2);

      // test de la suppresion d'un objet utilisé
      final Affichage a1 = manager.findByIdManager(1);
      boolean catched = false;
      try{
         manager.removeObjectManager(a1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(manager.findAllObjectsManager().size() == 4);
   }

   @Test
   public void testFindDoublons(){
      final Affichage affichage = new Affichage();
      final Utilisateur createur = utilisateurDao.findById(1);
      affichage.setCreateur(createur);
      affichage.setIntitule("Essentiel Patient");

      assertTrue(manager.findDoublonManager(affichage));

      affichage.setIntitule("Résumé Patient");
      assertFalse(manager.findDoublonManager(affichage));
   }

   @Test
   public void testIsUsedObjectManager(){
      final Affichage a1 = manager.findByIdManager(1);
      assertTrue(manager.isUsedObjectManager(a1));

      final Affichage a2 = manager.findByIdManager(2);
      assertTrue(manager.isUsedObjectManager(a2));

      final Affichage a4 = manager.findByIdManager(4);
      assertFalse(manager.isUsedObjectManager(a4));

      assertFalse(manager.isUsedObjectManager(null));

      assertFalse(manager.isUsedObjectManager(new Affichage()));
   }

}
