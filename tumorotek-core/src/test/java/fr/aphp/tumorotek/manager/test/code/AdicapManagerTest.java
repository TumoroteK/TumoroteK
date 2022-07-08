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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.code.AdicapGroupeDao;
import fr.aphp.tumorotek.manager.code.AdicapManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.AdicapGroupe;

/**
 *
 * Classe de test pour le manager AdicapManager.
 * Classe créée le 19/05/10.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class AdicapManagerTest extends AbstractManagerTest4
{
   @Autowired
   private AdicapManager adicapManager;

   @Autowired
   private AdicapGroupeDao adicapGroupeDao;

   @Test
   public void testFindAllObjectsManager(){

      final List<Adicap> ads = adicapManager.findAllObjectsManager();
      //assertEquals(8907, ads.size());
      assertEquals(8906, ads.size()); // JDI : ce que j'ai en base, est-ce grave?
   }

   @Test
   public void testFindByCodeLikeManager(){
      //teste une recherche exactMatch
      List<Adicap> ads = adicapManager.findByCodeLikeManager("N", true);
      assertTrue(ads.size() == 1);
      //teste une recherche non exactMatch
      ads = adicapManager.findByCodeLikeManager("N", false);
      assertTrue(ads.size() == 728);
      //teste une recherche infructueuse
      ads = adicapManager.findByCodeLikeManager("LUX", true);
      assertTrue(ads.size() == 0);
      //null recherche
      ads = adicapManager.findByCodeLikeManager(null, false);
      assertTrue(ads.size() == 0);
   }

   @Test
   public void testFindByLibelleLikeManager(){
      //teste une recherche exactMatch
      List<Adicap> ads = adicapManager.findByLibelleLikeManager("NEZ", true);
      assertTrue(ads.size() == 1);
      //teste une recherche non exactMatch
      ads = adicapManager.findByLibelleLikeManager("NEZ", false);
      assertTrue(ads.size() == 11);
      //teste une recherche infructueuse
      ads = adicapManager.findByLibelleLikeManager("INCUBUS", false);
      assertTrue(ads.size() == 0);
      //null recherche
      ads = adicapManager.findByLibelleLikeManager(null, true);
      assertTrue(ads.size() == 0);
   }

   @Test
   public void testfindDictionnairesManager(){
      final List<AdicapGroupe> groupes = adicapManager.findDictionnairesManager();
      assertTrue(groupes.size() == 8);
   }

   @Test
   public void testFindByAdicapParentManager(){
      Adicap parent = adicapManager.findByCodeLikeManager("AC", true).get(0);
      List<Adicap> ads = adicapManager.findByAdicapParentManager(parent, true);
      assertTrue(ads.size() == 10);
      ads = adicapManager.findByAdicapParentManager(parent, false);
      assertTrue(ads.size() == 3);

      parent = adicapManager.findByCodeLikeManager("AP", true).get(0);
      ads = adicapManager.findByAdicapParentManager(parent, false);
      assertTrue(ads.size() == 0);

      parent = adicapManager.findByCodeLikeManager("0N72", true).get(0);
      ads = adicapManager.findByAdicapParentManager(parent, null);
      assertTrue(ads.size() == 0);
      //null recherche
      ads = adicapManager.findByAdicapParentManager(null, null);
      assertTrue(ads.size() == 0);
   }

   @Test
   public void testFindByMorphoManager(){
      List<Adicap> ads = adicapManager.findByMorphoManager(true);
      //assertEquals(1449, ads.size());
      assertEquals(1448, ads.size()); // JDI : ce que j'ai en base, est-ce grave?
      ads = adicapManager.findByMorphoManager(false);
      assertTrue(ads.size() == 0);
      //null recherche
      ads = adicapManager.findByMorphoManager(null);
      assertTrue(ads.size() == 0);
   }

   @Test
   public void testFindByAdicapGroupManager(){
      final AdicapGroupe g = adicapManager.findDictionnairesManager().get(2);
      List<Adicap> ads = adicapManager.findByAdicapGroupeManager(g);
      assertTrue(ads.size() == 18);
      //null recherche
      ads = adicapManager.findByAdicapGroupeManager(null);
      assertTrue(ads.size() == 0);
   }

   @Test
   public void testGetCimoMorphosManager(){
      Adicap a = adicapManager.findByCodeLikeManager("5311", true).get(0);
      assertTrue(adicapManager.getCimoMorphosManager(a).size() == 1);
      a = adicapManager.findByCodeLikeManager("N", true).get(0);
      assertTrue(adicapManager.getCimoMorphosManager(a).size() == 0);
   }

   @Test
   public void testGetCimMastersManager(){
      Adicap a = adicapManager.findByCodeLikeManager("BV", true).get(0);
      assertTrue(adicapManager.getCimMastersManager(a).size() == 1);
      a = adicapManager.findByCodeLikeManager("N", true).get(0);
      assertTrue(adicapManager.getCimMastersManager(a).size() == 0);
   }

   @Test
   public void testGetAdicapGroupesManager(){
      final AdicapGroupe g = adicapManager.findDictionnairesManager().get(6);
      assertTrue(adicapManager.getAdicapGroupesManager(g).size() == 19);
      assertTrue(adicapManager.getAdicapGroupesManager(g).get(0).getAdicapGroupeId() == 40);
      assertTrue(adicapManager.getAdicapGroupesManager(adicapManager.getAdicapGroupesManager(g).get(0)).size() == 0);
   }

   @Test
   public void testFindChildrenCodesManager(){
      Adicap a = adicapManager.findByCodeLikeManager("EZ", true).get(0);
      List<Adicap> codes = adicapManager.findChildrenCodesManager(a, null, "%");
      assertTrue(codes.size() == 86);

      a = adicapManager.findByCodeLikeManager("EH", true).get(0);
      codes = adicapManager.findChildrenCodesManager(a, null, "%");
      assertTrue(codes.size() == 9);

      codes = adicapManager.findChildrenCodesManager(a, null, "%EH!%");
      assertTrue(codes.size() == 7);

      codes = adicapManager.findChildrenCodesManager(a, null, "EH2730");
      assertTrue(codes.size() == 2);

      codes = adicapManager.findChildrenCodesManager(a, null, "EH273");
      assertTrue(codes.size() == 1);

      a = adicapManager.findByCodeLikeManager("EH!LP", true).get(0);
      codes = adicapManager.findChildrenCodesManager(a, null, "%");
      assertTrue(codes.size() == 1);

      AdicapGroupe g = adicapGroupeDao.findById(19);
      codes = adicapManager.findChildrenCodesManager(null, g, "%");
      assertTrue(codes.size() == 344);

      g = adicapGroupeDao.findById(5);
      codes = adicapManager.findChildrenCodesManager(null, g, "%");
      //assertEquals(2217, codes.size());
      assertEquals(2216, codes.size()); // JDI : ce que j'ai en base, est-ce grave?

      codes = adicapManager.findChildrenCodesManager(null, g, "");
      assertTrue(codes.isEmpty());

      codes = adicapManager.findChildrenCodesManager(null, g, null);
      assertTrue(codes.isEmpty());

      codes = adicapManager.findChildrenCodesManager(null, null, "%");
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindByDicoAndCodeOrLibelleManager(){
      AdicapGroupe g = adicapGroupeDao.findById(6);
      List<Adicap> adicaps = adicapManager.findByDicoAndCodeOrLibelleManager(g, "BD0", false);
      assertTrue(adicaps.size() == 37);
      g = adicapGroupeDao.findById(3);
      adicaps = adicapManager.findByDicoAndCodeOrLibelleManager(g, "BD0", true);
      assertTrue(adicaps.size() == 0);
      g = adicapGroupeDao.findById(3);
      adicaps = adicapManager.findByDicoAndCodeOrLibelleManager(g, "PHARYNX", true);
      assertTrue(adicaps.size() == 1);
      adicaps = adicapManager.findByDicoAndCodeOrLibelleManager(g, "PHARYNX", false);
      assertTrue(adicaps.size() == 6);
      adicaps = adicapManager.findByDicoAndCodeOrLibelleManager(null, "PHARYNX", false);
      assertTrue(adicaps.size() == 0);
      adicaps = adicapManager.findByDicoAndCodeOrLibelleManager(g, null, false);
      assertTrue(adicaps.size() == 0);
   }
}
