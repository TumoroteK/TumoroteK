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
package fr.aphp.tumorotek.manager.test.contexte;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.context.ProtocoleManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Protocole;

/**
 *
 * Classe de test pour le manager ProtocoleManager.
 * Classe créée le 12/02/12.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0.6
 *
 */
public class ProtocoleManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ProtocoleManager protocoleManager;

   @Autowired
   private PlateformeDao plateformeDao;

   public ProtocoleManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<Protocole> protocoles = protocoleManager.findAllObjectsManager();
      assertTrue(protocoles.size() == 3);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final Protocole p1 = protocoleManager.findAllObjectsManager().get(0);
      assertFalse(protocoleManager.findDoublonManager(p1));
      final Protocole p2 = new Protocole();
      p2.setNom(p1.getNom());
      p2.setPlateforme(p1.getPlateforme());
      assertTrue(p2.equals(p1));
      assertTrue(protocoleManager.findDoublonManager(p2));

      p1.setNom("OFSEP");
      assertTrue(protocoleManager.findDoublonManager(p1));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObject(){
      //Enregistrement est reference
      final Plateforme pf1 = plateformeDao.findById(1);
      final Protocole tysa = protocoleManager.findByOrderManager(pf1).get(1);
      assertTrue(protocoleManager.isUsedObjectManager(tysa));
      //Enregistrement n'est pas reference
      final Plateforme pf2 = plateformeDao.findById(2);
      final Protocole edmus = protocoleManager.findByOrderManager(pf2).get(0);
      assertTrue(protocoleManager.isUsedObjectManager(edmus));
   }

   /**
    * Teste les methodes CRUD. 
    */
   @Test
   public void testCRUD(){
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   /**
    * Teste la methode createObjectManager. 
    */
   private void createObjectManagerTest(){
      //Insertion nouvel enregistrement
      final Protocole p1 = new Protocole();
      p1.setNom("MELBASE");
      p1.setPlateforme(plateformeDao.findById(1));
      protocoleManager.createObjectManager(p1);
      assertTrue((protocoleManager.findAllObjectsManager()).size() == 4);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final Protocole p1Bis = new Protocole();
      p1Bis.setNom("MELBASE");
      try{
         protocoleManager.createObjectManager(p1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      p1Bis.setPlateforme(p1.getPlateforme());
      catched = false;
      try{
         protocoleManager.createObjectManager(p1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((protocoleManager.findAllObjectsManager()).size() == 4);

      //validation test Type
      final String[] nomValues = new String[] {"", "  ", null, "HOOÉÀ≠ÉÇ™È–", createOverLength(200), "goo d98-Type"};
      final Protocole p2 = new Protocole();
      p2.setPlateforme(plateformeDao.findById(2));
      for(int i = 0; i < nomValues.length; i++){
         try{
            p2.setNom(nomValues[i]);
            if(i != 5){ //car creation valide
               protocoleManager.createObjectManager(p2);
            }
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(protocoleManager.findAllObjectsManager().size() == 4);
         }
      }
   }

   /**
    * Teste la methode updateObjectManager. 
    */
   private void updateObjectManagerTest(){
      //Modification d'un enregistrement
      final Plateforme pf1 = plateformeDao.findById(1);
      final Protocole p1 = protocoleManager.findByOrderManager(pf1).get(0);
      p1.setNom("Melbase+234");
      protocoleManager.updateObjectManager(p1);
      assertTrue(protocoleManager.findByOrderManager(pf1).get(0).getNom().equals("Melbase+234"));
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         p1.setNom("OFSEP");
         protocoleManager.updateObjectManager(p1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(protocoleManager.findAllObjectsManager().size() == 4);

      //Validation test
      final String[] nomValues = new String[] {"", "  ", null, "|mpl1‑ÀÇÈ≠ÀÀÈÇ™Ç", createOverLength(200), "goo d98-Type"};
      for(int i = 0; i < nomValues.length; i++){
         try{
            p1.setNom(nomValues[i]);
            if(i != 5){ //car creation valide
               protocoleManager.updateObjectManager(p1);
            }
         }catch(final ValidationException e){
            //verifie que l'enregistrement n'a pas ete modifie
            assertTrue(protocoleManager.findByOrderManager(pf1).get(0).getNom().equals("Melbase+234"));
         }
      }
   }

   /**
    * Teste la methode removeObjectManager. 
    */
   private void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final Plateforme pf1 = plateformeDao.findById(1);
      final Protocole p1 = protocoleManager.findByOrderManager(pf1).get(0);
      protocoleManager.removeObjectManager(p1);
      assertTrue(protocoleManager.findAllObjectsManager().size() == 3);
      //Suppression engrendrant une exception
      //		Boolean catched = false;
      //		try {
      //			Protocole r2 = (Protocole) protocoleManager
      //		.findByOrderManager(pf1).get(0);
      //			protocoleManager.removeObjectManager(r2);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catched = true;
      //			}
      //		}
      //		assertTrue(catched);
      //		assertTrue((protocoleManager
      //			.findByOrderManager(pf1)).size() == 2);
      //null remove
      protocoleManager.removeObjectManager(null);
      testFindAllObjectsManager();
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = protocoleManager.findByOrderManager(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getNom().equals("OFSEP"));
      pf = plateformeDao.findById(2);
      list = protocoleManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = protocoleManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }
}
