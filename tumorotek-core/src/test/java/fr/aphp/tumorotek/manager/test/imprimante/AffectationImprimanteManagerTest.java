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
package fr.aphp.tumorotek.manager.test.imprimante;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.imprimante.ImprimanteDao;
import fr.aphp.tumorotek.dao.imprimante.ModeleDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.imprimante.AffectationImprimanteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimantePK;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager AffectationImprimanteManager.
 * Classe créée le 22/03/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class AffectationImprimanteManagerTest extends AbstractManagerTest4
{

   @Autowired
   private AffectationImprimanteManager affectationImprimanteManager;
   @Autowired
   private ImprimanteDao imprimanteDao;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private ModeleDao modeleDao;

   public AffectationImprimanteManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<AffectationImprimante> liste = affectationImprimanteManager.findAllObjectsManager();
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByIdManager().
    */
   @Test
   public void testFindByIdManager(){
      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);
      final Utilisateur u4 = utilisateurDao.findById(4).orElse(null);
      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Imprimante i1 = imprimanteDao.findById(1).orElse(null);
      AffectationImprimantePK pk = new AffectationImprimantePK(u1, b1, i1);

      AffectationImprimante ai = affectationImprimanteManager.findByIdManager(pk);
      assertNotNull(ai);

      pk = new AffectationImprimantePK(u4, b1, i1);
      ai = affectationImprimanteManager.findByIdManager(pk);
      assertNull(ai);

      ai = affectationImprimanteManager.findByIdManager(null);
      assertNull(ai);
   }

   /**
    * Test l'appel de la méthode findByExcludedPKManager().
    */
   @Test
   public void testFindByExcludedPKManager(){
      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);
      final Utilisateur u4 = utilisateurDao.findById(4).orElse(null);
      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Imprimante i1 = imprimanteDao.findById(1).orElse(null);
      AffectationImprimantePK pk = new AffectationImprimantePK(u1, b1, i1);

      List<AffectationImprimante> liste = affectationImprimanteManager.findByExcludedPKManager(pk);
      assertTrue(liste.size() == 3);

      pk = new AffectationImprimantePK(u4, b1, i1);
      liste = affectationImprimanteManager.findByExcludedPKManager(pk);
      assertTrue(liste.size() == 4);

      liste = affectationImprimanteManager.findByExcludedPKManager(null);
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByUtilisateurBanqueManager().
    */
   @Test
   public void testFindByUtilisateurBanqueManager(){
      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Banque b3 = banqueDao.findById(3).orElse(null);
      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);
      final Utilisateur u4 = utilisateurDao.findById(4).orElse(null);

      List<AffectationImprimante> liste = affectationImprimanteManager.findByBanqueUtilisateurManager(b1, u1);
      assertTrue(liste.size() == 1);

      liste = affectationImprimanteManager.findByBanqueUtilisateurManager(b1, u4);
      assertTrue(liste.size() == 0);

      liste = affectationImprimanteManager.findByBanqueUtilisateurManager(b3, u1);
      assertTrue(liste.size() == 0);

      liste = affectationImprimanteManager.findByBanqueUtilisateurManager(b1, null);
      assertTrue(liste.size() == 0);

      liste = affectationImprimanteManager.findByBanqueUtilisateurManager(null, u1);
      assertTrue(liste.size() == 0);

      liste = affectationImprimanteManager.findByBanqueUtilisateurManager(null, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test la méthode findDoublonManager.
    */
   @Test
   public void testFindDoublonManager(){

      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);
      final Utilisateur u4 = utilisateurDao.findById(4).orElse(null);
      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Imprimante i1 = imprimanteDao.findById(1).orElse(null);

      assertTrue(affectationImprimanteManager.findDoublonManager(u1, b1, i1));
      assertFalse(affectationImprimanteManager.findDoublonManager(u4, b1, i1));

      assertFalse(affectationImprimanteManager.findDoublonManager(null, b1, i1));
      assertFalse(affectationImprimanteManager.findDoublonManager(u1, null, i1));
      assertFalse(affectationImprimanteManager.findDoublonManager(u1, b1, null));
   }

   /**
    * Test la méthode validateObjectManager.
    */
   @Test
   public void testValidateObjectManager(){
      final Utilisateur u3 = utilisateurDao.findById(3).orElse(null);
      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Imprimante i1 = imprimanteDao.findById(1).orElse(null);

      Boolean catched = false;
      // on test l'insertion avec l'utilisateur null
      try{
         affectationImprimanteManager.validateObjectManager(null, b1, i1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec la banque null
      try{
         affectationImprimanteManager.validateObjectManager(u3, null, i1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec l'irmpirmante null
      try{
         affectationImprimanteManager.validateObjectManager(u3, b1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
   }

   @Test
   public void testCrud(){
      final Utilisateur u3 = utilisateurDao.findById(3).orElse(null);
      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Imprimante i1 = imprimanteDao.findById(1).orElse(null);
      final Modele m1 = modeleDao.findById(1).orElse(null);
      final AffectationImprimante ai = new AffectationImprimante();

      Boolean catched = false;
      // on test l'insertion avec l'utilisateur null
      try{
         affectationImprimanteManager.createObjectManager(ai, null, b1, i1, m1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec la banque null
      try{
         affectationImprimanteManager.createObjectManager(ai, u3, null, i1, m1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec l'irmpirmante null
      try{
         affectationImprimanteManager.createObjectManager(ai, u3, b1, null, m1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // test d'une insertion valide avec modele
      affectationImprimanteManager.createObjectManager(ai, u3, b1, i1, m1);
      AffectationImprimantePK pk = ai.getPk();
      assertTrue(affectationImprimanteManager.findAllObjectsManager().size() == 5);

      final AffectationImprimante aiTest = affectationImprimanteManager.findByIdManager(pk);
      assertNotNull(aiTest);
      assertTrue(aiTest.getImprimante().equals(i1));
      assertTrue(aiTest.getUtilisateur().equals(u3));
      assertTrue(aiTest.getBanque().equals(b1));
      assertTrue(aiTest.getModele().equals(m1));

      // test d'une insertion valide sans modele
      final AffectationImprimante ai2 = new AffectationImprimante();
      final Imprimante i2 = imprimanteDao.findById(2).orElse(null);
      affectationImprimanteManager.createObjectManager(ai2, u3, b1, i2, null);
      pk = ai2.getPk();
      assertTrue(affectationImprimanteManager.findAllObjectsManager().size() == 6);
      final AffectationImprimante aiTest2 = affectationImprimanteManager.findByIdManager(pk);
      assertNotNull(aiTest2);
      assertTrue(aiTest2.getImprimante().equals(i2));
      assertTrue(aiTest2.getUtilisateur().equals(u3));
      assertTrue(aiTest2.getBanque().equals(b1));
      assertNull(aiTest2.getModele());

      // on test un update
      final AffectationImprimante aiUp1 = new AffectationImprimante();
      affectationImprimanteManager.createObjectManager(aiUp1, u3, b1, i2, m1);
      pk = aiUp1.getPk();
      assertTrue(affectationImprimanteManager.findAllObjectsManager().size() == 6);
      final AffectationImprimante aiTest1 = affectationImprimanteManager.findByIdManager(pk);
      assertNotNull(aiTest1);
      assertNotNull(aiTest1.getBanque());
      assertNotNull(aiTest1.getUtilisateur());
      assertNotNull(aiTest1.getImprimante());
      assertNotNull(aiTest1.getModele());

      // suppression du profilUtilisateur ajouté
      affectationImprimanteManager.removeObjectManager(aiTest);
      affectationImprimanteManager.removeObjectManager(aiTest2);
      assertTrue(affectationImprimanteManager.findAllObjectsManager().size() == 4);

      // suppression d'un profilUtilisateur null
      affectationImprimanteManager.removeObjectManager(null);
      assertTrue(affectationImprimanteManager.findAllObjectsManager().size() == 4);

      // suppression d'un profilUtilisateur inexistant
      affectationImprimanteManager.removeObjectManager(aiTest);
      assertTrue(affectationImprimanteManager.findAllObjectsManager().size() == 4);
   }

}
