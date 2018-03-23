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
package fr.aphp.tumorotek.manager.test.coeur.prodderive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonTypeManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.systeme.UniteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager TransformationManager.
 * Classe créée le 30/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class TransformationManagerTest extends AbstractManagerTest4
{

   @Autowired
   private TransformationManager transformationManager;
   @Autowired
   private EntiteManager entiteManager;
   @Autowired
   private EchantillonManager echantillonManager;
   @Autowired
   private EchantillonTypeManager echantillonTypeManager;
   @Autowired
   private UniteManager uniteManager;
   @Autowired
   private ProdDeriveManager prodDeriveManager;
   @Autowired
   private UtilisateurDao utilisateurDao;

   /** Constructeur par défaut. */
   public TransformationManagerTest(){}

   @Test
   public void testFindById(){
      final Transformation transfo = transformationManager.findByIdManager(1);
      assertNotNull(transfo);
      assertTrue(transfo.getObjetId() == 1);

      final Transformation transfoNull = transformationManager.findByIdManager(10);
      assertNull(transfoNull);
   }

   @Test
   public void testFindAll(){
      final List<Transformation> transfos = transformationManager.findAllObjectsManager();
      assertTrue(transfos.size() == 5);
   }

   @Test
   public void testFindByEntiteObjetManager(){
      final Entite entite1 = entiteManager.findByIdManager(3);
      List<Transformation> transfos = transformationManager.findByEntiteObjetManager(entite1, 1);
      assertTrue(transfos.size() == 2);

      final Entite entite2 = entiteManager.findByIdManager(5);
      transfos = transformationManager.findByEntiteObjetManager(entite2, 5);
      assertTrue(transfos.size() == 0);
   }

   @Test
   public void testFindByParentManager(){
      final Echantillon echantillon = echantillonManager.findByIdManager(1);
      List<Transformation> transfos = transformationManager.findByParentManager(echantillon);
      assertNotNull(transfos);
      assertTrue(transfos.size() == 2);

      final ProdDerive derive = prodDeriveManager.findByIdManager(1);
      transfos = transformationManager.findByParentManager(derive);
      assertNotNull(transfos);
      assertTrue(transfos.size() == 1);

      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      transfos = transformationManager.findByParentManager(type);
      assertNotNull(transfos);
      assertTrue(transfos.size() == 0);

      transfos = transformationManager.findByParentManager(new Echantillon());
      assertNotNull(transfos);
      assertTrue(transfos.size() == 0);
   }

   @Test
   public void testFindDoublon(){
      final Entite entite = entiteManager.findByIdManager(3);
      final Transformation transfo = new Transformation();
      transfo.setEntite(entite);
      transfo.setObjetId(5);
      assertFalse(transformationManager.findDoublonManager(transfo));

      transfo.setObjetId(1);
      assertTrue(transformationManager.findDoublonManager(transfo));

      Entite entite2 = entiteManager.findByIdManager(7);
      transfo.setEntite(entite2);
      assertFalse(transformationManager.findDoublonManager(transfo));

      final Transformation transfo2 = transformationManager.findByIdManager(2);
      transfo2.setEntite(entite);
      transfo2.setObjetId(5);
      assertFalse(transformationManager.findDoublonManager(transfo2));

      transfo2.setObjetId(1);
      assertTrue(transformationManager.findDoublonManager(transfo2));

      entite2 = entiteManager.findByIdManager(8);
      transfo2.setEntite(entite2);
      assertFalse(transformationManager.findDoublonManager(transfo2));
   }

   @Test
   public void testCrud(){
      // Insertion
      final Transformation transfo1 = new Transformation();
      final Entite entite = entiteManager.findByIdManager(3);
      final Unite quantite = uniteManager.findByIdManager(1);
      transfo1.setObjetId(6);
      transfo1.setEntite(entite);
      transfo1.setQuantite(null);
      Boolean catched = false;
      // on test l'insertion avec une entité nulle
      try{
         transformationManager.createObjectManager(transfo1, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      // on test l'insertion d'un couple Entite/ObjetId qui n'existe pas
      try{
         transformationManager.createObjectManager(transfo1, entite, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EntiteObjectIdNotExistException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      transfo1.setObjetId(1);
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      /*try {
      	transformationManager.createObjectManager(
      			transfo1, entite, null, null);
      } catch (Exception e) {
      	if (e.getClass().getSimpleName().equals(
      			"DoublonFoundException")) {
      		catched = true;
      	}
      }
      assertTrue(catched);*/
      // On teste une insertion avec un attribut quantite non valide
      transfo1.setObjetId(2);
      float number = (float) -1.0;
      transfo1.setQuantite(number);
      catched = false;
      try{
         transformationManager.createObjectManager(transfo1, entite, quantite);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);

      // on teste une insertion valide
      transfo1.setObjetId(2);
      transfo1.setQuantite(null);
      transformationManager.createObjectManager(transfo1, entite, quantite);
      assertTrue(transformationManager.findAllObjectsManager().size() == 6);
      final int id = transfo1.getTransformationId();

      // Update
      final Transformation transfo2 = transformationManager.findByIdManager(id);
      Boolean catchedUpdate = false;
      // on test l'update avec une entité nulle
      transfo2.setEntite(entite);
      try{
         transformationManager.updateObjectManager(transfo2, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;
      // on test l'update d'un couple Entite/ObjetId qui n'existe pas
      transfo2.setObjetId(7);
      try{
         transformationManager.updateObjectManager(transfo2, entite, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EntiteObjectIdNotExistException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;
      // On teste une modif avec un attribut quantite non valide
      transfo2.setObjetId(4);
      number = (float) -1.0;
      transfo2.setQuantite(number);
      try{
         transformationManager.updateObjectManager(transfo2, entite, quantite);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);

      // On teste une mise à jour valide
      transfo2.setObjetId(4);
      transfo2.setQuantite(null);
      transformationManager.updateObjectManager(transfo2, entite, quantite);
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      /*Transformation transfo3 = transformationManager.findByIdManager(3);
      transfo3.setObjetId(1);
      try {
      	transformationManager.updateObjectManager(
      			transfo3, entite, null, null);
      } catch (Exception e) {
      	if (e.getClass().getSimpleName().equals(
      			"DoublonFoundException")) {
      		catchedUpdate = true;
      	}
      }
      assertTrue(catchedUpdate);*/
      final Transformation transfo4 = transformationManager.findByIdManager(id);
      assertTrue(transfo4.getObjetId() == 4);

      // Suppression
      final Utilisateur u = utilisateurDao.findById(1);
      final Transformation transfo5 = transformationManager.findByIdManager(id);
      transformationManager.removeObjectManager(transfo5, null, u);
      assertNull(transformationManager.findByIdManager(id));
      final Transformation transfo6 = transformationManager.findByIdManager(1);
      try{
         prodDeriveManager.removeObjectCascadeManager(transfo6, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("derive.cascade.isCessed"));
         assertFalse(oe.isCascadable());
      }
      assertNotNull(transformationManager.findByIdManager(1));
   }

   @Test
   public void testfindAllDeriveFromParentManager(){
      Echantillon e = echantillonManager.findByIdManager(4);
      List<ProdDerive> derives = transformationManager.findAllDeriveFromParentManager(e);
      assertTrue(derives.size() == 1);
      e = echantillonManager.findByIdManager(1);
      derives = transformationManager.findAllDeriveFromParentManager(e);
      assertTrue(derives.size() == 2);
      assertTrue(derives.get(1).getCode().equals("EHT.1.1"));
   }
}
