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
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.io.export.ResultatDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.export.AffichageManager;
import fr.aphp.tumorotek.manager.io.export.ResultatManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
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
