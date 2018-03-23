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
package fr.aphp.tumorotek.manager.test.interfacage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.interfacage.LogicielDao;
import fr.aphp.tumorotek.manager.interfacage.EmetteurManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.Logiciel;

/**
 *
 * Classe de test pour le manager Emetteur.
 * Classe créée le 07/10/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class EmetteurManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EmetteurManager emetteurManager;
   @Autowired
   private LogicielDao logicielDao;

   //	@SuppressWarnings("deprecation")
   //	public EmetteurManagerTest() {
   //		// pour eviter l'autowiring vers fantomeDao
   //		this.setDependencyCheck(false);
   //	}
   //	
   //	@Override
   //	protected String[] getConfigLocations() {
   //        return new String[]{ "applicationContextManagerBase.xml",
   //       		 "applicationContextDaoBase.xml" };
   //    }

   @Test
   public void testFindById(){
      final Emetteur m = emetteurManager.findByIdManager(1);
      assertNotNull(m);

      final Emetteur mNull = emetteurManager.findByIdManager(100);
      assertNull(mNull);
   }

   @Test
   public void testFindAll(){
      final List<Emetteur> list = emetteurManager.findAllObjectsManager();
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getIdentification().equals("Apix Anapath"));
   }

   @Test
   public void testFindByLogicielManager(){
      final Logiciel l1 = logicielDao.findById(1);
      final Logiciel l2 = logicielDao.findById(2);

      List<Emetteur> list = emetteurManager.findByLogicielManager(l1);
      assertTrue(list.size() == 2);

      list = emetteurManager.findByLogicielManager(l2);
      assertTrue(list.size() == 0);

      list = emetteurManager.findByLogicielManager(null);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindByIdentificationAndServiceManager(){
      List<Emetteur> liste = emetteurManager.findByIdentificationAndServiceManager("Apix Anapath", "ANAP");
      assertTrue(liste.size() == 1);

      liste = emetteurManager.findByIdentificationAndServiceManager("Apix", "ANAP");
      assertTrue(liste.size() == 0);

      liste = emetteurManager.findByIdentificationAndServiceManager("Apix Anapath", "AN");
      assertTrue(liste.size() == 0);

      liste = emetteurManager.findByIdentificationAndServiceManager(null, "ANAP");
      assertTrue(liste.size() == 0);

      liste = emetteurManager.findByIdentificationAndServiceManager("Apix Anapath", null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindByIdinListManager(){
      final List<Integer> ids = new ArrayList<>();
      ids.add(1);
      List<Emetteur> liste = emetteurManager.findByIdinListManager(ids);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).getIdentification().equals("Glims Hémato"));

      ids.add(2);
      liste = emetteurManager.findByIdinListManager(ids);
      assertTrue(liste.size() == 2);
      assertTrue(liste.get(0).getIdentification().equals("Apix Anapath"));

      liste = emetteurManager.findByIdinListManager(new ArrayList<Integer>());
      assertTrue(liste.size() == 0);

      liste = emetteurManager.findByIdinListManager(null);
      assertTrue(liste.size() == 0);
   }

}
