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
package fr.aphp.tumorotek.manager.test.io.utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.io.utils.CorrespondanceIdManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;

public class CorrespondanceIdManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EntiteDao entiteDao;

   @Autowired
   private BanqueDao banqueDao;

   @Autowired
   private CorrespondanceIdManager correspondanceIdManager;

   @Test
   public void testPatientPrelevements(){
      final Entite patient = entiteDao.findById(1);
      final Entite prelevement = entiteDao.findById(2);

      // from Patient
      final List<Banque> banks = banqueDao.findAll();
      final List<Integer> pIds = new ArrayList<>();
      List<Integer> res;

      // empty ids
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, patient, prelevement, banks, null);
      assertTrue(res.isEmpty());

      res = correspondanceIdManager.findTargetIdsFromIdsManager(null, patient, prelevement, banks, null);
      assertTrue(res.isEmpty());

      pIds.add(1);
      pIds.add(3);
      pIds.add(2);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, patient, prelevement, banks, null);

      assertTrue(res.size() == 4);

      banks.remove(banqueDao.findById(2));
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, patient, prelevement, banks, null);

      assertTrue(res.size() == 3);

      // from Prelevement
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, prelevement, patient, null, null);
      assertTrue(res.size() == 2);
   }

   @Test
   public void testPrelevementsEchantillons(){
      final Entite echantillon = entiteDao.findById(3);
      final Entite prelevement = entiteDao.findById(2);

      // from Patient
      List<Banque> banks = banqueDao.findAll();
      final List<Integer> pIds = new ArrayList<>();
      List<Integer> res;

      pIds.add(1);
      pIds.add(3);
      pIds.add(2);
      pIds.add(4);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, prelevement, echantillon, banks, null);

      assertTrue(res.size() == 4);

      banks.remove(banqueDao.findById(1));
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, prelevement, echantillon, banks, null);

      assertTrue(res.size() == 1);

      // from Echantillon
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, echantillon, prelevement, banks, null);
      assertTrue(res.size() == 1);

      banks = banqueDao.findAll();
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, echantillon, prelevement, banks, null);
      assertTrue(res.size() == 3);
   }

   @Test
   public void testPatientsEchantillons(){
      final Entite echantillon = entiteDao.findById(3);
      final Entite patient = entiteDao.findById(1);

      // from Patient
      final List<Banque> banks = banqueDao.findAll();
      final List<Integer> pIds = new ArrayList<>();
      List<Integer> res;

      pIds.add(1);
      pIds.add(2);
      pIds.add(3);
      pIds.add(4);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, patient, echantillon, banks, null);

      assertTrue(res.size() == 4);

      pIds.remove(0);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, patient, echantillon, banks, null);

      assertTrue(res.size() == 3);

      // from Echantillon
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, echantillon, patient, banks, null);
      assertTrue(res.size() == 2);

      pIds.add(1);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, echantillon, patient, banks, null);
      assertTrue(res.size() == 2);
   }

   @Test
   public void testEchantillonsDerives(){
      final Entite echantillon = entiteDao.findById(3);
      final Entite derive = entiteDao.findById(8);

      final List<Banque> banks = banqueDao.findAll();
      final List<Integer> pIds = new ArrayList<>();
      List<Integer> res;

      pIds.add(1);
      pIds.add(2);
      pIds.add(3);
      pIds.add(4);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, echantillon, derive, banks, null);

      assertTrue(res.size() == 3);

      pIds.remove(0);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, echantillon, derive, banks, null);

      assertTrue(res.size() == 1);

      // from Echantillon
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, echantillon, banks, null);
      assertTrue(res.size() == 2);

      pIds.add(1);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, echantillon, banks, null);
      assertTrue(res.size() == 2);
   }

   @Test
   public void testPrelevementDerives(){
      final Entite prelevement = entiteDao.findById(2);
      final Entite derive = entiteDao.findById(8);

      final List<Banque> banks = banqueDao.findAll();
      final List<Integer> pIds = new ArrayList<>();
      List<Integer> res;

      pIds.add(1);
      pIds.add(2);
      pIds.add(3);
      pIds.add(4);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, prelevement, derive, banks, null);

      assertTrue(res.size() == 4);

      pIds.remove(0);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, prelevement, derive, banks, null);

      assertTrue(res.size() == 1);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, prelevement, banks, null);
      assertTrue(res.size() == 3);

      pIds.add(1);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, prelevement, banks, null);
      assertTrue(res.size() == 3);
   }

   @Test
   public void testDerivesDerives(){
      final Entite derive = entiteDao.findById(8);

      final List<Banque> banks = banqueDao.findAll();
      final List<Integer> pIds = new ArrayList<>();
      List<Integer> res;

      pIds.add(1);
      pIds.add(2);
      pIds.add(3);
      pIds.add(4);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, derive, banks, true);

      assertTrue(res.size() == 1);

      pIds.remove(0);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, derive, banks, true);

      assertTrue(res.size() == 0);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, derive, banks, false);
      assertTrue(res.size() == 1);

      pIds.remove(1);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, derive, banks, false);
      assertTrue(res.size() == 0);
   }

   @Test
   public void testPatientDerives(){
      final Entite patient = entiteDao.findById(1);
      final Entite derive = entiteDao.findById(8);

      final List<Banque> banks = banqueDao.findAll();
      final List<Integer> pIds = new ArrayList<>();
      List<Integer> res;

      pIds.add(1);
      pIds.add(3);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, patient, derive, banks, null);

      assertTrue(res.size() == 4);

      pIds.remove(1);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, patient, derive, banks, null);

      assertTrue(res.size() == 1);

      pIds.clear();
      pIds.add(1);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, patient, null, null);
      assertTrue(res.get(0) == 3);

      pIds.clear();
      pIds.add(3);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, patient, null, null);
      assertTrue(res.get(0) == 3);

      pIds.add(1);
      pIds.add(2);
      pIds.add(4);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, patient, banks, null);
      assertTrue(res.size() == 2);
   }

   @Test
   public void testCessionEchantillons(){
      final Entite cession = entiteDao.findById(5);
      final Entite echantillon = entiteDao.findById(3);

      final List<Banque> banks = banqueDao.findAll();
      final List<Integer> pIds = new ArrayList<>();
      List<Integer> res;

      pIds.add(1);
      pIds.add(2);
      pIds.add(3);
      pIds.add(4);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, cession, echantillon, banks, null);

      assertTrue(res.size() == 3);

      pIds.remove(1);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, cession, echantillon, banks, null);
      assertTrue(res.size() == 1);

      pIds.clear();
      pIds.add(1);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, echantillon, cession, banks, null);
      assertTrue(res.size() == 2);

      pIds.add(2);
      pIds.add(3);
      pIds.add(4);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, echantillon, cession, banks, null);
      assertTrue(res.size() == 2);
   }

   @Test
   public void testCessionDerives(){
      final Entite cession = entiteDao.findById(5);
      final Entite derive = entiteDao.findById(8);

      final List<Banque> banks = banqueDao.findAll();
      final List<Integer> pIds = new ArrayList<>();
      List<Integer> res;

      pIds.add(1);
      pIds.add(2);
      pIds.add(3);
      pIds.add(4);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, cession, derive, banks, null);

      assertTrue(res.size() == 2);

      pIds.remove(3);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, cession, derive, banks, null);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 3);

      pIds.clear();
      pIds.add(1);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, cession, banks, null);
      assertTrue(res.size() == 1);

      pIds.add(2);
      pIds.add(3);
      pIds.add(4);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, cession, banks, null);
      assertTrue(res.size() == 2);

      banks.remove(banqueDao.findById(1));
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, derive, cession, banks, null);
      assertTrue(res.isEmpty());
   }

   @Test
   public void testCessionPrelevements(){
      final Entite cession = entiteDao.findById(5);
      final Entite prelevement = entiteDao.findById(2);

      final List<Banque> banks = banqueDao.findAll();
      final List<Integer> pIds = new ArrayList<>();
      List<Integer> res;

      pIds.add(1);
      pIds.add(2);
      pIds.add(3);
      pIds.add(4);

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, cession, prelevement, banks, null);

      assertTrue(res.size() == 2);

      pIds.clear();
      pIds.add(1);
      pIds.add(3);
      pIds.add(4);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, cession, prelevement, banks, null);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      pIds.clear();
      pIds.add(2);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, prelevement, cession, banks, null);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 2);

      pIds.add(1);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, prelevement, cession, banks, null);
      assertTrue(res.size() == 3);
   }

   @Test
   public void testCessionPatients(){
      final Entite patient = entiteDao.findById(1);
      final Entite cession = entiteDao.findById(5);

      // from Cession
      final List<Banque> banks = banqueDao.findAll();
      final List<Integer> pIds = new ArrayList<>();
      List<Integer> res;

      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, cession, patient, banks, null);
      assertTrue(res.isEmpty());

      res = correspondanceIdManager.findTargetIdsFromIdsManager(null, cession, patient, banks, null);
      assertTrue(res.isEmpty());

      pIds.add(1);
      pIds.add(2);
      pIds.add(3);
      pIds.add(4);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, cession, patient, banks, null);
      assertTrue(res.size() == 1);

      pIds.clear();
      pIds.add(1);
      pIds.add(3);
      pIds.add(4);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, cession, patient, banks, null);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 3);

      pIds.clear();
      pIds.add(1);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, patient, cession, banks, null);
      assertTrue(res.isEmpty());

      pIds.add(3);
      res = correspondanceIdManager.findTargetIdsFromIdsManager(pIds, patient, cession, banks, null);
      assertTrue(res.size() == 3);
   }

}
