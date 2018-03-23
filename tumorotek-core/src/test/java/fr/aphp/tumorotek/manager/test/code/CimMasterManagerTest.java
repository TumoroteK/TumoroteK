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
package fr.aphp.tumorotek.manager.test.code;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.code.CimMasterManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.code.CimMaster;

/**
 *
 * Classe de test pour le manager CimMasterManager.
 * Classe créée le 19/05/10.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class CimMasterManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CimMasterManager cimMasterManager;

   
   @Test
   public void testFindAllObjectsManager(){
      final List<CimMaster> cims = (List<CimMaster>) cimMasterManager.findAllObjectsManager();
      assertTrue(cims.size() == 19178);
   }

   
   @Test
   public void testFindByCodeLikeManager(){
      //teste une recherche exactMatch
      List<CimMaster> cims = (List<CimMaster>) cimMasterManager.findByCodeLikeManager("B00", true);
      assertTrue(cims.size() == 1);
      //teste une recherche non exactMatch
      cims = (List<CimMaster>) cimMasterManager.findByCodeLikeManager("B00", false);
      assertTrue(cims.size() == 11);
      //teste une recherche infructueuse
      cims = (List<CimMaster>) cimMasterManager.findByCodeLikeManager("LUX", true);
      assertTrue(cims.size() == 0);
      //null recherche
      cims = (List<CimMaster>) cimMasterManager.findByCodeLikeManager(null, false);
      assertTrue(cims.size() == 0);
   }

   
   @Test
   public void testFindByLibelleLikeManager(){
      //teste une recherche exactMatch
      List<CimMaster> cims = (List<CimMaster>) cimMasterManager.findByLibelleLikeManager("choléra", true);
      assertTrue(cims.size() == 1);
      assertTrue(cims.get(0).getCode().equals("A00"));
      //teste une recherche non exactMatch
      cims = (List<CimMaster>) cimMasterManager.findByLibelleLikeManager("choléra", false);
      assertTrue(cims.size() == 6);
      //teste une recherche infructueuse
      cims = (List<CimMaster>) cimMasterManager.findByLibelleLikeManager("LUXTEST", false);
      assertTrue(cims.size() == 0);
      //null recherche
      cims = (List<CimMaster>) cimMasterManager.findByLibelleLikeManager(null, false);
      assertTrue(cims.size() == 0);
   }

   
   @Test
   public void testFindByCimParentManager(){
      CimMaster parent = (CimMaster) cimMasterManager.findByCodeLikeManager("A00", true).get(0);
      List<CimMaster> cims = cimMasterManager.findByCimParentManager(parent);
      assertTrue(cims.size() == 3);
      final List<CimMaster> cims2 = (List<CimMaster>) cimMasterManager.findByCodeLikeManager("A00.", false);
      assertTrue(cims.equals(cims2));
      parent = (CimMaster) cimMasterManager.findByCodeLikeManager("(A00-A09)", true).get(0);
      cims = cimMasterManager.findByCimParentManager(parent);
      assertTrue(cims.size() == 10);
      cims = cimMasterManager.findByCimParentManager(null);
      assertTrue(cims.size() == 21);
   }

   @Test
   public void testGetAdicapsManager(){
      CimMaster a = (CimMaster) cimMasterManager.findByCodeLikeManager("C00.9", true).get(0);
      assertTrue(cimMasterManager.getAdicapsManager(a).size() == 4);
      a = (CimMaster) cimMasterManager.findByCodeLikeManager("A00.1", false).get(0);
      assertTrue(cimMasterManager.getAdicapsManager(a).size() == 0);
   }

   @Test
   public void testFindChildrenCodesManager(){
      CimMaster a = (CimMaster) cimMasterManager.findByCodeLikeManager("(A00-A09)", true).get(0);
      List<CimMaster> codes = cimMasterManager.findChildrenCodesManager(a);
      assertTrue(codes.size() == 69);

      a = (CimMaster) cimMasterManager.findByCodeLikeManager("M00.0", true).get(0);
      codes = cimMasterManager.findChildrenCodesManager(a);
      assertTrue(codes.size() == 11);

      a = (CimMaster) cimMasterManager.findByCodeLikeManager("M00.07", true).get(0);
      codes = cimMasterManager.findChildrenCodesManager(a);
      assertTrue(codes.size() == 1 && codes.get(0).equals(a));

      codes = cimMasterManager.findChildrenCodesManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindByIdManager(){
      assertTrue(cimMasterManager.findByIdManager(34).getCode().equals("A04.7"));
      assertNull(cimMasterManager.findByIdManager(null));
      assertNull(cimMasterManager.findByIdManager(1258741258));
   }
}
