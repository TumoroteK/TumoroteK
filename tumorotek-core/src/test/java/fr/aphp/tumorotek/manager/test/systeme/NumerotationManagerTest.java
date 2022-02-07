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
package fr.aphp.tumorotek.manager.test.systeme;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.systeme.NumerotationManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Numerotation;

/**
 *
 * Classe de test pour le manager NumerotationManager.
 * Classe créée le 18/01/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class NumerotationManagerTest extends AbstractManagerTest4
{

   @Autowired
   private NumerotationManager numerotationManager;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private EntiteDao entiteDao;

   public NumerotationManagerTest(){}

   @Test
   public void testFindById(){
      final Numerotation num1 = numerotationManager.findByIdManager(1);
      assertNotNull(num1);
      assertTrue(num1.getCodeFormula().equals("PRLVT[]"));

      final Numerotation numNull = numerotationManager.findByIdManager(10);
      assertNull(numNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Numerotation> list = numerotationManager.findAllObjectsManager();
      assertTrue(list.size() == 3);
   }

   @Test
   public void testFindByBanquesManager(){
      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Banque b2 = banqueDao.findById(2).orElse(null);
      final Banque b3 = banqueDao.findById(3).orElse(null);
      List<Banque> banques = new ArrayList<>();
      banques.add(b1);
      banques.add(b2);

      List<Numerotation> liste = numerotationManager.findByBanquesManager(banques);
      assertTrue(liste.size() == 3);
      assertTrue(liste.get(0).getCodeFormula().equals("PRLVT[]"));

      banques = new ArrayList<>();
      banques.add(b3);
      liste = numerotationManager.findByBanquesManager(banques);
      assertTrue(liste.size() == 0);

      banques = new ArrayList<>();
      liste = numerotationManager.findByBanquesManager(banques);
      assertTrue(liste.size() == 0);

      liste = numerotationManager.findByBanquesManager(null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindByBanqueAndEntiteManager(){
      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Banque b2 = banqueDao.findById(2).orElse(null);
      final Entite e1 = entiteDao.findById(1).orElse(null);
      final Entite e2 = entiteDao.findById(2).orElse(null);

      List<Numerotation> liste = numerotationManager.findByBanqueAndEntiteManager(b1, e2);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).getCodeFormula().equals("PRLVT[]"));

      liste = numerotationManager.findByBanqueAndEntiteManager(b2, e2);
      assertTrue(liste.size() == 0);

      liste = numerotationManager.findByBanqueAndEntiteManager(b1, e1);
      assertTrue(liste.size() == 0);

      liste = numerotationManager.findByBanqueAndEntiteManager(null, e1);
      assertTrue(liste.size() == 0);

      liste = numerotationManager.findByBanqueAndEntiteManager(b1, null);
      assertTrue(liste.size() == 0);

      liste = numerotationManager.findByBanqueAndEntiteManager(null, null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindByBanqueSelectEntiteManager(){
      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Banque b2 = banqueDao.findById(2).orElse(null);
      final Banque b3 = banqueDao.findById(3).orElse(null);

      List<Entite> liste = numerotationManager.findByBanqueSelectEntiteManager(b1);
      assertTrue(liste.size() == 2);

      liste = numerotationManager.findByBanqueSelectEntiteManager(b2);
      assertTrue(liste.size() == 1);

      liste = numerotationManager.findByBanqueSelectEntiteManager(b3);
      assertTrue(liste.size() == 0);

      liste = numerotationManager.findByBanqueSelectEntiteManager(null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindDoublonManager(){
      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Entite e1 = entiteDao.findById(1).orElse(null);
      final Entite e2 = entiteDao.findById(5).orElse(null);

      final Numerotation num1 = new Numerotation();
      num1.setBanque(b1);
      num1.setEntite(e1);
      assertFalse(numerotationManager.findDoublonManager(num1));

      num1.setEntite(e2);
      assertTrue(numerotationManager.findDoublonManager(num1));

      final Numerotation num2 = numerotationManager.findByIdManager(1);
      assertFalse(numerotationManager.findDoublonManager(num2));

      num2.setEntite(e2);
      assertTrue(numerotationManager.findDoublonManager(num2));

      assertFalse(numerotationManager.findDoublonManager(null));
   }

   @Test
   public void testGetGeneratedCodeManager(){
      Numerotation num = numerotationManager.findByIdManager(1);
      assertTrue(numerotationManager.getGeneratedCodeManager(num).equals("PRLVT00003"));

      num = numerotationManager.findByIdManager(2);
      assertTrue(numerotationManager.getGeneratedCodeManager(num).equals("CONT00152TK"));

      num = numerotationManager.findByIdManager(3);
      assertTrue(numerotationManager.getGeneratedCodeManager(num).equals("15BK2"));

      assertNull(numerotationManager.getGeneratedCodeManager(null));

      final Numerotation numTest = new Numerotation();
      assertNull(numerotationManager.getGeneratedCodeManager(numTest));

      numTest.setCodeFormula("TMP[]");
      assertTrue(numerotationManager.getGeneratedCodeManager(numTest).equals("TMP[]"));

      numTest.setCurrentIncrement(5);
      assertTrue(numerotationManager.getGeneratedCodeManager(numTest).equals("TMP5"));

      numTest.setZeroFill(true);
      assertTrue(numerotationManager.getGeneratedCodeManager(numTest).equals("TMP5"));

      numTest.setNbChiffres(3);
      assertTrue(numerotationManager.getGeneratedCodeManager(numTest).equals("TMP005"));

      numTest.setCodeFormula("TMP");
      assertTrue(numerotationManager.getGeneratedCodeManager(numTest).equals("TMP"));
   }

   /**
    * Test le CRUD d'un ProtocoleExt.
    * @throws ParseException 
    */
   @Test
   public void testCrud() throws ParseException{
      createObjectManagerTest();
      updateObjectManagerTest();
   }

   private void createObjectManagerTest() throws ParseException{

      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Entite e2 = entiteDao.findById(2).orElse(null);
      final Entite e3 = entiteDao.findById(3).orElse(null);

      final Numerotation n1 = new Numerotation();
      n1.setCodeFormula("PRLVT[]");
      n1.setCurrentIncrement(15);

      Boolean catched = false;
      // on test l'insertion avec la banque nulle
      try{
         numerotationManager.createObjectManager(n1, null, e3);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(numerotationManager.findAllObjectsManager().size() == 3);

      // on test l'insertion avec l'entité nulle
      try{
         numerotationManager.createObjectManager(n1, b1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(numerotationManager.findAllObjectsManager().size() == 3);

      // on test l'insertion d'un doublon
      try{
         numerotationManager.createObjectManager(n1, b1, e2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(numerotationManager.findAllObjectsManager().size() == 3);

      // On test une insertion valide
      n1.setStartIncrement(1);
      n1.setNbChiffres(5);
      n1.setZeroFill(true);
      numerotationManager.createObjectManager(n1, b1, e3);
      assertTrue(numerotationManager.findAllObjectsManager().size() == 4);
      final int id = n1.getNumerotationId();

      // Vérification
      final Numerotation nTest = numerotationManager.findByIdManager(id);
      assertNotNull(nTest);
      assertNotNull(nTest.getBanque());
      assertNotNull(nTest.getEntite());
      assertTrue(nTest.getCodeFormula().equals("PRLVT[]"));
      assertTrue(nTest.getCurrentIncrement() == 15);
      assertTrue(nTest.getStartIncrement() == 1);
      assertTrue(nTest.getNbChiffres() == 5);
      assertTrue(nTest.getZeroFill());

      // Suppression
      numerotationManager.removeObjectManager(nTest);
      assertTrue(numerotationManager.findAllObjectsManager().size() == 3);
   }

   private void updateObjectManagerTest() throws ParseException{

      final Banque b1 = banqueDao.findById(1).orElse(null);
      final Entite e2 = entiteDao.findById(2).orElse(null);
      final Entite e3 = entiteDao.findById(3).orElse(null);

      final Numerotation n = new Numerotation();
      n.setCodeFormula("PRLVT[]");
      n.setCurrentIncrement(15);

      numerotationManager.createObjectManager(n, b1, e3);
      assertTrue(numerotationManager.findAllObjectsManager().size() == 4);
      final int id = n.getNumerotationId();

      final Numerotation nUp = numerotationManager.findByIdManager(id);
      Boolean catched = false;
      // on test l'update avec la banque nulle
      try{
         numerotationManager.updateObjectManager(nUp, null, e3);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(numerotationManager.findAllObjectsManager().size() == 4);

      // on test l'update avec l'entité nulle
      try{
         numerotationManager.updateObjectManager(nUp, b1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(numerotationManager.findAllObjectsManager().size() == 4);

      // on test l'update d'un doublon
      try{
         numerotationManager.updateObjectManager(nUp, b1, e2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(numerotationManager.findAllObjectsManager().size() == 4);

      // On test un update valide
      nUp.setStartIncrement(1);
      nUp.setNbChiffres(5);
      nUp.setZeroFill(true);
      numerotationManager.updateObjectManager(nUp, b1, e3);
      assertTrue(numerotationManager.findAllObjectsManager().size() == 4);

      // Vérification
      final Numerotation nTest = numerotationManager.findByIdManager(id);
      assertNotNull(nTest);
      assertNotNull(nTest.getBanque());
      assertNotNull(nTest.getEntite());
      assertTrue(nTest.getCodeFormula().equals("PRLVT[]"));
      assertTrue(nTest.getCurrentIncrement() == 15);
      assertTrue(nTest.getStartIncrement() == 1);
      assertTrue(nTest.getNbChiffres() == 5);
      assertTrue(nTest.getZeroFill());

      // Suppression
      numerotationManager.removeObjectManager(nTest);
      assertTrue(numerotationManager.findAllObjectsManager().size() == 3);
   }
}
