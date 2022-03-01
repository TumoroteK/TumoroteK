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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.aphp.tumorotek.manager.systeme.CouleurManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.test.Config;
import fr.aphp.tumorotek.model.systeme.Couleur;

/**
 *
 * Classe de test pour le manager CouleurManager.
 * Classe créée le 30/04/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class CouleurManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CouleurManager couleurManager;

   public CouleurManagerTest(){}

   @Test
   public void testFindById(){
      final Couleur c1 = couleurManager.findByIdManager(1);
      assertNotNull(c1);
      assertTrue(c1.getCouleur().equals("VERT"));

      final Couleur cNull = couleurManager.findByIdManager(25);
      assertNull(cNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Couleur> list = couleurManager.findAllObjectsManager();
      assertTrue(list.size() == 15);
   }

   /**
    * Test la méthode findByOrdreVisotubeManager.
    */
   @Test
   public void testFindByOrdreVisotubeManager(){
      final List<Couleur> list = couleurManager.findByOrdreVisotubeManager();
      assertTrue(list.size() == 12);
   }

   /**
    * Test la méthode findByCouleurLikeManager.
    */
   @Test
   public void testFindByCouleurLikeExactManager(){
      List<Couleur> list = couleurManager.findByCouleurLikeManager("VERT", true);
      assertTrue(list.size() == 1);

      list = couleurManager.findByCouleurLikeManager("VE", true);
      assertTrue(list.size() == 0);

      list = couleurManager.findByCouleurLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByCouleurLikeManager.
    */
   @Test
   public void testFindByCouleurLikeManager(){
      List<Couleur> list = couleurManager.findByCouleurLikeManager("VERT", false);
      assertTrue(list.size() == 1);

      list = couleurManager.findByCouleurLikeManager("VE", false);
      assertTrue(list.size() == 1);

      list = couleurManager.findByCouleurLikeManager(null, false);
      assertTrue(list.size() == 0);
   }
}
