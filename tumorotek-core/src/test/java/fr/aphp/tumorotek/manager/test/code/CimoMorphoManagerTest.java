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

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.code.CimoMorphoManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.code.CimoMorpho;

/**
 *
 * Classe de test pour le manager CimoMorphoManager.
 * Classe créée le 21/05/10.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class CimoMorphoManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CimoMorphoManager cimoMorphoManager;

   @Test
   public void testFindAllObjectsManager(){
      final List<CimoMorpho> cims = cimoMorphoManager.findAllObjectsManager();
      assertTrue(cims.size() == 1161);
   }

   @Test
   public void testFindByCodeLikeManager(){
      //teste une recherche exactMatch
      List<CimoMorpho> cimos = cimoMorphoManager.findByCodeLikeManager("D0-20150", true);
      assertTrue(cimos.size() == 1);
      //teste une recherche non exactMatch
      cimos = cimoMorphoManager.findByCodeLikeManager("D0", false);
      assertTrue(cimos.size() == 6);
      //teste une recherche CIMREF
      cimos = cimoMorphoManager.findByCodeLikeManager("C5", false);
      assertTrue(cimos.size() == 87);
      // teste une recherche CIMREF -> verifie que le resultat ne 
      // contient pas de doublons
      cimos = cimoMorphoManager.findByCodeLikeManager("50", false);
      assertTrue(cimos.size() == 110);
      //teste une recherche infructueuse
      cimos = cimoMorphoManager.findByCodeLikeManager("LUX", true);
      assertTrue(cimos.size() == 0);
      //null recherche
      cimos = cimoMorphoManager.findByCodeLikeManager(null, false);
      assertTrue(cimos.size() == 0);
   }

   @Test
   public void testFindByLibelleLikeManager(){
      //teste une recherche exactMatch
      List<CimoMorpho> cimos = cimoMorphoManager.findByLibelleLikeManager("FIBROMATOSE", true);
      assertTrue(cimos.size() == 1);
      //teste une recherche non exactMatch
      cimos = cimoMorphoManager.findByLibelleLikeManager("FIBROMATOSE", false);
      assertTrue(cimos.size() == 7);
      //teste une recherche infructueuse
      cimos = cimoMorphoManager.findByLibelleLikeManager("CULLUM", false);
      assertTrue(cimos.size() == 0);
      //null recherche
      cimos = cimoMorphoManager.findByLibelleLikeManager(null, true);
      assertTrue(cimos.size() == 0);
   }

   @Test
   public void testGetAdicapsManager(){
      CimoMorpho a = cimoMorphoManager.findByCodeLikeManager("M-8005/0", true).get(0);
      assertTrue(cimoMorphoManager.getAdicapsManager(a).size() == 2);
      a = cimoMorphoManager.findByLibelleLikeManager("Keasbey", false).get(0);
      assertTrue(cimoMorphoManager.getAdicapsManager(a).size() == 0);
   }
}
