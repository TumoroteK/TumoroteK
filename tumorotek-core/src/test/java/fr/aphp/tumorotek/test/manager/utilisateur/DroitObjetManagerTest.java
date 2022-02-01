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
package fr.aphp.tumorotek.test.manager.utilisateur;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.manager.utilisateur.DroitObjetManager;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.DroitObjet;
import fr.aphp.tumorotek.model.utilisateur.DroitObjetPK;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager DroitObjetManager.
 * Classe créée le 19/05/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class DroitObjetManagerTest extends AbstractManagerTest4
{

   @Autowired
   private DroitObjetManager droitObjetManager;
   @Autowired
   private ProfilDao profilDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private OperationTypeDao operationTypeDao;

   public DroitObjetManagerTest(){}

   @Test
   public void testFindById(){
      final Profil p1 = profilDao.findById(1);
      final Profil p4 = profilDao.findById(4);
      final Entite e1 = entiteDao.findById(3);
      final OperationType o1 = operationTypeDao.findById(1);
      final DroitObjetPK pk1 = new DroitObjetPK(p1, e1, o1);

      final DroitObjet do1 = droitObjetManager.findByIdManager(pk1);
      assertNotNull(do1);

      final DroitObjetPK pk2 = new DroitObjetPK(p4, e1, o1);
      final DroitObjet do2 = droitObjetManager.findByIdManager(pk2);
      assertNull(do2);

      final DroitObjet do3 = droitObjetManager.findByIdManager(null);
      assertNull(do3);
   }

   @Test
   public void testFindAll(){
      final List<DroitObjet> list = droitObjetManager.findAllObjectsManager();
      assertTrue(list.size() == 9);
   }

   @Test
   public void testFindByExcludedPKManager(){
      final Profil p1 = profilDao.findById(1);
      final Profil p4 = profilDao.findById(4);
      final Entite e1 = entiteDao.findById(3);
      final OperationType o1 = operationTypeDao.findById(1);
      final DroitObjetPK pk1 = new DroitObjetPK(p1, e1, o1);

      List<DroitObjet> list = droitObjetManager.findByExcludedPKManager(pk1);
      assertTrue(list.size() == 8);

      final DroitObjetPK pk2 = new DroitObjetPK(p4, e1, o1);
      list = droitObjetManager.findByExcludedPKManager(pk2);
      assertTrue(list.size() == 9);

      list = droitObjetManager.findByExcludedPKManager(null);
      assertTrue(list.size() == 9);
   }

   @Test
   public void testFindByProfilManager(){
      final Profil p1 = profilDao.findById(1);
      final Profil p3 = profilDao.findById(3);

      List<DroitObjet> liste = droitObjetManager.findByProfilManager(p1);
      assertTrue(liste.size() == 3);

      liste = droitObjetManager.findByProfilManager(p3);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindByProfilEntiteManager(){
      final Profil p2 = profilDao.findById(2);
      final Profil p3 = profilDao.findById(3);
      final Entite e2 = entiteDao.findById(2);
      final Entite e3 = entiteDao.findById(3);

      List<DroitObjet> liste = droitObjetManager.findByProfilEntiteManager(p2, e2);
      assertTrue(liste.size() == 3);

      liste = droitObjetManager.findByProfilEntiteManager(p2, e3);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.findByProfilEntiteManager(p3, e2);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.findByProfilEntiteManager(p2, null);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.findByProfilEntiteManager(null, e2);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.findByProfilEntiteManager(null, null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testGetOperationsByProfilEntiteManager(){
      final Profil p2 = profilDao.findById(2);
      final Profil p3 = profilDao.findById(3);
      final String e2 = "Prelevement";
      final String e3 = "Echantillon";

      List<OperationType> liste = droitObjetManager.getOperationsByProfilEntiteManager(p2, e2);
      assertTrue(liste.size() == 3);

      liste = droitObjetManager.getOperationsByProfilEntiteManager(p2, e3);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.getOperationsByProfilEntiteManager(p3, e2);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.getOperationsByProfilEntiteManager(p2, null);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.getOperationsByProfilEntiteManager(null, e2);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.getOperationsByProfilEntiteManager(null, null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindByProfilOperationManager(){
      final Profil p2 = profilDao.findById(2);
      final Profil p3 = profilDao.findById(3);
      final OperationType o1 = operationTypeDao.findById(1);
      final OperationType o2 = operationTypeDao.findById(2);

      List<DroitObjet> liste = droitObjetManager.findByProfilOperationManager(p2, o1);
      assertTrue(liste.size() == 2);

      liste = droitObjetManager.findByProfilOperationManager(p2, o2);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.findByProfilOperationManager(p3, o1);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.findByProfilOperationManager(p2, null);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.findByProfilOperationManager(null, o2);
      assertTrue(liste.size() == 0);

      liste = droitObjetManager.findByProfilOperationManager(null, null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindDoublonManager(){
      final Profil p1 = profilDao.findById(1);
      final Profil p4 = profilDao.findById(4);
      final Entite e1 = entiteDao.findById(3);
      final OperationType o1 = operationTypeDao.findById(1);

      assertTrue(droitObjetManager.findDoublonManager(p1, e1, o1));
      assertFalse(droitObjetManager.findDoublonManager(p4, e1, o1));

      assertFalse(droitObjetManager.findDoublonManager(null, e1, o1));
      assertFalse(droitObjetManager.findDoublonManager(p4, null, o1));
      assertFalse(droitObjetManager.findDoublonManager(p4, e1, null));

   }

   @Test
   public void testValidateObjectManager(){
      final Profil p1 = profilDao.findById(1);
      final Entite e1 = entiteDao.findById(3);
      final OperationType o1 = operationTypeDao.findById(1);

      Boolean catched = false;
      // on test l'insertion avec l'opération nulle
      try{
         droitObjetManager.validateObjectManager(p1, e1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec le profil null
      try{
         droitObjetManager.validateObjectManager(null, e1, o1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec l'entité nulle
      try{
         droitObjetManager.validateObjectManager(p1, null, o1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         droitObjetManager.validateObjectManager(p1, e1, o1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

   }

   @Test
   public void testCrud(){
      final Profil p1 = profilDao.findById(1);
      final Profil p4 = profilDao.findById(4);
      final Entite e1 = entiteDao.findById(3);
      final OperationType o1 = operationTypeDao.findById(1);

      final DroitObjet do1 = new DroitObjet();

      Boolean catched = false;
      // on test l'insertion avec l'opération nulle
      try{
         droitObjetManager.saveManager(do1, p1, e1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);
      catched = false;

      // on test l'insertion avec le profil null
      try{
         droitObjetManager.saveManager(do1, null, e1, o1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);
      catched = false;

      // on test l'insertion avec l'entité nulle
      try{
         droitObjetManager.saveManager(do1, p1, null, o1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);
      catched = false;

      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         droitObjetManager.saveManager(do1, p1, e1, o1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);
      catched = false;

      // insertion valide
      droitObjetManager.saveManager(do1, p4, e1, o1);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 10);
      final DroitObjetPK pk = do1.getPk();

      final DroitObjet doTest = droitObjetManager.findByIdManager(pk);
      assertNotNull(doTest);
      assertTrue(doTest.getEntite().equals(e1));
      assertTrue(doTest.getProfil().equals(p4));
      assertTrue(doTest.getOperationType().equals(o1));

      // suppression du DroitObjet créé
      droitObjetManager.removeObjectManager(doTest);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);

      // suppression d'un droitobjet null
      droitObjetManager.removeObjectManager(null);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);

      // Suppression d'un droitobjet qui n'est pas dans la base
      droitObjetManager.removeObjectManager(doTest);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);
   }

   @Test
   public void testHasProfilOperationOnEntitesManager(){
      final OperationType creation = operationTypeDao.findById(3);
      final Profil p2 = profilDao.findById(2);
      final List<Entite> entites = new ArrayList<>();
      assertFalse(droitObjetManager.hasProfilOperationOnEntitesManager(p2, creation, entites));

      entites.add(entiteDao.findById(1));
      assertTrue(droitObjetManager.hasProfilOperationOnEntitesManager(p2, creation, entites));
      entites.add(entiteDao.findById(2));
      assertTrue(droitObjetManager.hasProfilOperationOnEntitesManager(p2, creation, entites));
      entites.add(entiteDao.findById(3));
      assertFalse(droitObjetManager.hasProfilOperationOnEntitesManager(p2, creation, entites));

      final Profil p3 = profilDao.findById(3);
      final OperationType consultation = operationTypeDao.findById(1);
      assertFalse(droitObjetManager.hasProfilOperationOnEntitesManager(p3, creation, entites));
      assertFalse(droitObjetManager.hasProfilOperationOnEntitesManager(p3, consultation, entites));

      final Profil p1 = profilDao.findById(1);
      assertFalse(droitObjetManager.hasProfilOperationOnEntitesManager(p1, creation, entites));
      assertTrue(droitObjetManager.hasProfilOperationOnEntitesManager(p1, consultation, entites));

      assertFalse(droitObjetManager.hasProfilOperationOnEntitesManager(p2, creation, null));
      assertFalse(droitObjetManager.hasProfilOperationOnEntitesManager(p2, null, entites));
      assertFalse(droitObjetManager.hasProfilOperationOnEntitesManager(null, creation, entites));

   }

}
