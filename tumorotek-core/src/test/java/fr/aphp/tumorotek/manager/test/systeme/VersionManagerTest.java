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
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.systeme.VersionManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.systeme.Version;

/**
 *
 * Classe de test pour le manager VersionManager.
 * Classe créée le 26/05/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class VersionManagerTest extends AbstractManagerTest4
{

   @Autowired
   private VersionManager versionManager;

   public VersionManagerTest(){}

   /**
    * Test la méthode findById.
    */
   @Test
   public void testFindById(){
      final Version v1 = versionManager.findByIdManager(1);
      assertNotNull(v1);
      assertTrue(v1.getVersion().equals("2.0 beta 1"));

      final Version vNull = versionManager.findByIdManager(25);
      assertNull(vNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Version> list = versionManager.findAllObjectsManager();
      assertTrue(list.size() == 3);
   }

   /**
    * Test la méthode findByDateChronologiqueManager.
    */
   @Test
   public void testFindByDateChronologiqueManager(){
      final List<Version> list = versionManager.findByDateChronologiqueManager();
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getVersion().equals("2.0 beta 1"));
   }

   /**
    * Test la méthode findByCurrentVersionManager.
    */
   @Test
   public void testFindByCurrentVersionManager(){
      final Version v1 = versionManager.findByCurrentVersionManager();
      assertNotNull(v1);
      assertTrue(v1.getVersion().equals("2.0"));
   }

}