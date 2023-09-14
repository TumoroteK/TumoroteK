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
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonTypeManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdTypeManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.systeme.CouleurEntiteTypeManager;
import fr.aphp.tumorotek.manager.systeme.CouleurManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;

/**
 *
 * Classe de test pour le manager CouleurEntiteTypeManager.
 * Classe créée le 30/04/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class CouleurEntiteTypeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CouleurEntiteTypeManager couleurEntiteTypeManager;

   @Autowired
   private CouleurManager couleurManager;

   @Autowired
   private BanqueManager banqueManager;

   @Autowired
   private EchantillonTypeManager echantillonTypeManager;

   @Autowired
   private ProdTypeManager prodTypeManager;

   public CouleurEntiteTypeManagerTest(){}

   @Test
   public void testFindById(){
      final CouleurEntiteType c1 = couleurEntiteTypeManager.findByIdManager(1);
      assertNotNull(c1);
      assertTrue(c1.getCouleur().getCouleur().equals("ROUGE"));

      final CouleurEntiteType cNull = couleurEntiteTypeManager.findByIdManager(10);
      assertNull(cNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<CouleurEntiteType> list = couleurEntiteTypeManager.findAllObjectsManager();
      assertTrue(list.size() == 3);
   }

   /**
    * Test la méthode findAllObjectsByBanqueManager.
    */
   @Test
   public void testFindAllObjectsByBanqueManager(){
      final Banque b1 = banqueManager.findByIdManager(1);
      List<CouleurEntiteType> list = couleurEntiteTypeManager.findAllObjectsByBanqueManager(b1);
      assertTrue(list.size() == 2);

      final Banque b3 = banqueManager.findByIdManager(3);
      list = couleurEntiteTypeManager.findAllObjectsByBanqueManager(b3);
      assertTrue(list.size() == 0);

      list = couleurEntiteTypeManager.findAllObjectsByBanqueManager(new Banque());
      assertTrue(list.size() == 0);

      list = couleurEntiteTypeManager.findAllObjectsByBanqueManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findAllCouleursForEchanTypeByBanqueManager.
    */
   @Test
   public void testFindAllCouleursForEchanTypeByBanqueManager(){
      final Banque b1 = banqueManager.findByIdManager(1);
      List<CouleurEntiteType> list = couleurEntiteTypeManager.findAllCouleursForEchanTypeByBanqueManager(b1);
      assertTrue(list.size() == 1);
      assertNotNull(list.get(0).getEchantillonType());
      assertNull(list.get(0).getProdType());

      final Banque b3 = banqueManager.findByIdManager(3);
      list = couleurEntiteTypeManager.findAllCouleursForEchanTypeByBanqueManager(b3);
      assertTrue(list.size() == 0);

      list = couleurEntiteTypeManager.findAllCouleursForEchanTypeByBanqueManager(new Banque());
      assertTrue(list.size() == 0);

      list = couleurEntiteTypeManager.findAllCouleursForEchanTypeByBanqueManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findAllCouleursForProdTypeByBanqueManager.
    */
   @Test
   public void testFindAllCouleursForProdTypeByBanqueManager(){
      final Banque b1 = banqueManager.findByIdManager(1);
      List<CouleurEntiteType> list = couleurEntiteTypeManager.findAllCouleursForProdTypeByBanqueManager(b1);
      assertTrue(list.size() == 1);
      assertNull(list.get(0).getEchantillonType());
      assertNotNull(list.get(0).getProdType());

      final Banque b3 = banqueManager.findByIdManager(3);
      list = couleurEntiteTypeManager.findAllCouleursForProdTypeByBanqueManager(b3);
      assertTrue(list.size() == 0);

      list = couleurEntiteTypeManager.findAllCouleursForProdTypeByBanqueManager(new Banque());
      assertTrue(list.size() == 0);

      list = couleurEntiteTypeManager.findAllCouleursForProdTypeByBanqueManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test de la méthode findDoublonManager().
    */
   @Test
   public void testFindDoublonManager(){
      final Banque b1 = banqueManager.findByIdManager(1);
      final EchantillonType eType = echantillonTypeManager.findByIdManager(1);
      final EchantillonType eType2 = echantillonTypeManager.findByIdManager(2);

      final CouleurEntiteType c1 = new CouleurEntiteType();
      assertFalse(couleurEntiteTypeManager.findDoublonManager(c1));
      c1.setBanque(b1);
      assertFalse(couleurEntiteTypeManager.findDoublonManager(c1));
      c1.setEchantillonType(eType);
      c1.setProdType(null);
      assertTrue(couleurEntiteTypeManager.findDoublonManager(c1));
      c1.setEchantillonType(eType2);
      assertFalse(couleurEntiteTypeManager.findDoublonManager(c1));

      final CouleurEntiteType c2 = couleurEntiteTypeManager.findByIdManager(2);
      assertFalse(couleurEntiteTypeManager.findDoublonManager(c2));
      c2.setEchantillonType(eType);
      c2.setProdType(null);
      assertTrue(couleurEntiteTypeManager.findDoublonManager(c2));

      assertFalse(couleurEntiteTypeManager.findDoublonManager(new CouleurEntiteType()));

      assertFalse(couleurEntiteTypeManager.findDoublonManager(null));
   }

   /**
    * Test le CRUD d'un ProtocoleExt.
    * @throws ParseException 
    */
   @Test
   public void testCrud() throws ParseException{
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   private void createObjectManagerTest() throws ParseException{

      final Banque b1 = banqueManager.findByIdManager(1);
      final Couleur couleur = couleurManager.findByIdManager(1);
      final EchantillonType eTypeDoublon = echantillonTypeManager.findByIdManager(1);
      final EchantillonType eType = echantillonTypeManager.findByIdManager(2);
      final ProdType pType = prodTypeManager.findByIdManager(1);

      final CouleurEntiteType cet1 = new CouleurEntiteType();

      Boolean catched = false;
      // on test l'insertion avec la banque nulle
      try{
         couleurEntiteTypeManager.createObjectManager(cet1, couleur, null, eType, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 3);

      // on test l'insertion avec la couleur nulle
      try{
         couleurEntiteTypeManager.createObjectManager(cet1, null, b1, eType, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 3);

      // on teste l'insertion avec aucun type
      try{
         couleurEntiteTypeManager.createObjectManager(cet1, couleur, b1, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidMultipleAssociationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 3);

      // on teste l'insertion avec les 2 types
      try{
         couleurEntiteTypeManager.createObjectManager(cet1, couleur, b1, eType, pType);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidMultipleAssociationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 3);

      // on test l'insertion d'un doublon
      try{
         couleurEntiteTypeManager.createObjectManager(cet1, couleur, b1, eTypeDoublon, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 3);

      // On test une insertion valide avec le type d'échantillon
      couleurEntiteTypeManager.createObjectManager(cet1, couleur, b1, eType, null);
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 4);
      final int id = cet1.getCouleurEntiteTypeId();

      // Vérification
      final CouleurEntiteType cetTest1 = couleurEntiteTypeManager.findByIdManager(id);
      assertNotNull(cetTest1);
      assertNotNull(cetTest1.getCouleur());
      assertNotNull(cetTest1.getBanque());
      assertNotNull(cetTest1.getEchantillonType());
      assertNull(cetTest1.getProdType());

      // On test une insertion valide avec le type de dérivés
      final CouleurEntiteType cet2 = new CouleurEntiteType();
      couleurEntiteTypeManager.createObjectManager(cet2, couleur, b1, null, pType);
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 5);
      final int id2 = cet2.getCouleurEntiteTypeId();

      // Vérification
      final CouleurEntiteType cetTest2 = couleurEntiteTypeManager.findByIdManager(id2);
      assertNotNull(cetTest2);
      assertNotNull(cetTest2.getCouleur());
      assertNotNull(cetTest2.getBanque());
      assertNull(cetTest2.getEchantillonType());
      assertNotNull(cetTest2.getProdType());

      // Suppression
      couleurEntiteTypeManager.removeObjectManager(cetTest1);
      couleurEntiteTypeManager.removeObjectManager(cetTest2);
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 3);
   }

   private void updateObjectManagerTest() throws ParseException{

      final Banque b1 = banqueManager.findByIdManager(1);
      final Couleur couleur = couleurManager.findByIdManager(1);
      final EchantillonType eTypeDoublon = echantillonTypeManager.findByIdManager(1);
      final EchantillonType eType = echantillonTypeManager.findByIdManager(2);
      final EchantillonType eType2 = echantillonTypeManager.findByIdManager(3);
      final ProdType pType = prodTypeManager.findByIdManager(1);

      final CouleurEntiteType cet = new CouleurEntiteType();
      couleurEntiteTypeManager.createObjectManager(cet, couleur, b1, eType, null);
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 4);
      final int id = cet.getCouleurEntiteTypeId();

      final CouleurEntiteType cetUp1 = couleurEntiteTypeManager.findByIdManager(id);
      Boolean catched = false;
      // on test l'update avec la banque nulle
      try{
         couleurEntiteTypeManager.updateObjectManager(cetUp1, couleur, null, eType, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 4);

      // on test l'update avec la couleur nulle
      try{
         couleurEntiteTypeManager.updateObjectManager(cetUp1, null, b1, eType, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 4);

      // on teste l'update avec aucun type
      try{
         couleurEntiteTypeManager.updateObjectManager(cetUp1, couleur, b1, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidMultipleAssociationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 4);

      // on teste l'update avec les 2 types
      try{
         couleurEntiteTypeManager.updateObjectManager(cetUp1, couleur, b1, eType, pType);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidMultipleAssociationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 4);

      // on test l'update d'un doublon
      try{
         couleurEntiteTypeManager.updateObjectManager(cetUp1, couleur, b1, eTypeDoublon, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 4);

      // On test un update valide avec le type d'échantillon
      couleurEntiteTypeManager.updateObjectManager(cetUp1, couleur, b1, eType2, null);
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 4);

      // Vérification
      final CouleurEntiteType cetTest1 = couleurEntiteTypeManager.findByIdManager(id);
      assertNotNull(cetTest1);
      assertNotNull(cetTest1.getCouleur());
      assertNotNull(cetTest1.getBanque());
      assertNotNull(cetTest1.getEchantillonType());
      assertNull(cetTest1.getProdType());

      // On test un update valide avec le type de dérivés
      final CouleurEntiteType cetUp2 = couleurEntiteTypeManager.findByIdManager(id);
      couleurEntiteTypeManager.updateObjectManager(cetUp2, couleur, b1, null, pType);
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 4);

      // Vérification
      final CouleurEntiteType cetTest2 = couleurEntiteTypeManager.findByIdManager(id);
      assertNotNull(cetTest2);
      assertNotNull(cetTest2.getCouleur());
      assertNotNull(cetTest2.getBanque());
      assertNull(cetTest2.getEchantillonType());
      assertNotNull(cetTest2.getProdType());

      // Suppression
      couleurEntiteTypeManager.removeObjectManager(cetTest1);
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 3);
   }

   private void removeObjectManagerTest(){
      // test de la suppression d'un objet null
      couleurEntiteTypeManager.removeObjectManager(null);
      assertTrue(couleurEntiteTypeManager.findAllObjectsManager().size() == 3);
   }

}
