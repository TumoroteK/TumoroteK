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
package fr.aphp.tumorotek.manager.test.qualite;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.qualite.ConformiteTypeManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.qualite.ConformiteType;

/**
 *
 * Classe de test pour le manager ConformiteType.
 * Classe créée le 08/11/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ConformiteTypeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ConformiteTypeManager conformiteTypeManager;

   @Autowired
   private EntiteDao entiteDao;

   public ConformiteTypeManagerTest(){

   }

   private final int tot = 5;

   @Test
   public void testFindById(){
      final ConformiteType c = conformiteTypeManager.findByIdManager(1);
      assertNotNull(c);

      final ConformiteType cNull = conformiteTypeManager.findByIdManager(100);
      assertNull(cNull);
   }

   @Test
   public void testFindAll(){
      final List<ConformiteType> list = conformiteTypeManager.findAllObjectsManager();
      assertTrue(list.size() == tot);
   }

   @Test
   public void testFindByEntiteAndTypeManager(){
      List<ConformiteType> list = conformiteTypeManager.findByEntiteAndTypeManager("Arrivee", entiteDao.findById(2));
      assertTrue(list.size() == 1);

      list = conformiteTypeManager.findByEntiteAndTypeManager("Test", entiteDao.findById(3));
      assertTrue(list.size() == 0);

      list = conformiteTypeManager.findByEntiteAndTypeManager(null, null);
      assertTrue(list.size() == 0);
   }

}
