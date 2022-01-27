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
package fr.aphp.tumorotek.test.manager.coeur.cession;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.coeur.cession.LargeCessionManager;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager CessionManager.
 * Classe créée le 01/02/10.
 *
 * @author Pierre Ventadour.
 * @version 2.1
 *
 */
public class LargeCessionManagerTest extends AbstractManagerTest4
{

   @Autowired
   private LargeCessionManager largeCessionManager;

   public LargeCessionManagerTest(){}

   @Test
   public void testFindIncompatibleStatutsManager(){
      final List<Integer> ids = new ArrayList<>();
      ids.add(1);
      // nulls
      assertTrue(largeCessionManager.findIncompatibleStatutsManager(null, null).isEmpty());
      assertTrue(largeCessionManager.findIncompatibleStatutsManager(null, 3).isEmpty());
      assertTrue(largeCessionManager.findIncompatibleStatutsManager(ids, null).isEmpty());
      // empty
      ids.clear();
      assertTrue(largeCessionManager.findIncompatibleStatutsManager(ids, 3).isEmpty());

      ids.add(1);
      ids.add(2);
      ids.add(3);
      ids.add(4);
      List<String> codes = largeCessionManager.findIncompatibleStatutsManager(ids, 3);
      assertTrue(codes.size() == 2);
      assertTrue(codes.contains("EHT.1"));
      assertTrue(codes.contains("JEG.1"));

      codes = largeCessionManager.findIncompatibleStatutsManager(ids, 8);
      assertTrue(codes.size() == 1);
      assertTrue(codes.contains("EHT.1.1"));

      // bad entite
      assertTrue(largeCessionManager.findIncompatibleStatutsManager(ids, 7).isEmpty());
   }

   @Test
   public void testFindDateStockIncompatiblesManager() throws ParseException{
      final List<Integer> ids = new ArrayList<>();
      final Date dateC = new SimpleDateFormat("ddMMyyyy").parse("05072009");
      ids.add(1);
      // nulls
      assertTrue(largeCessionManager.findDateStockIncompatiblesManager(null, null, null).isEmpty());
      assertTrue(largeCessionManager.findDateStockIncompatiblesManager(null, 3, dateC).isEmpty());
      assertTrue(largeCessionManager.findDateStockIncompatiblesManager(ids, null, dateC).isEmpty());
      assertTrue(largeCessionManager.findDateStockIncompatiblesManager(ids, 3, null).isEmpty());
      // empty
      ids.clear();
      assertTrue(largeCessionManager.findDateStockIncompatiblesManager(ids, 3, dateC).isEmpty());

      ids.add(1);
      ids.add(2);
      ids.add(3);
      ids.add(4);
      List<String> codes = largeCessionManager.findDateStockIncompatiblesManager(ids, 3, dateC);
      assertTrue(codes.size() == 1);
      assertTrue(codes.contains("EHT.1"));

      codes = largeCessionManager.findDateStockIncompatiblesManager(ids, 8, dateC);
      assertTrue(codes.size() == 2);
      assertTrue(codes.contains("EHT.1.1"));
      assertTrue(codes.contains("JEG.1.1"));

      // bad entite
      assertTrue(largeCessionManager.findDateStockIncompatiblesManager(ids, 7, dateC).isEmpty());
   }

   @Test
   public void testFindEvtStockIncompatiblesManager() throws ParseException{
      final List<Integer> ids = new ArrayList<>();
      Date dateC = new SimpleDateFormat("ddMMyyyy").parse("05012010");
      ids.add(1);
      // nulls
      assertTrue(largeCessionManager.findEvtStockIncompatiblesManager(null, null, null).isEmpty());
      assertTrue(largeCessionManager.findEvtStockIncompatiblesManager(null, 3, dateC).isEmpty());
      assertTrue(largeCessionManager.findEvtStockIncompatiblesManager(ids, null, dateC).isEmpty());
      assertTrue(largeCessionManager.findEvtStockIncompatiblesManager(ids, 3, null).isEmpty());
      // empty
      ids.clear();
      assertTrue(largeCessionManager.findEvtStockIncompatiblesManager(ids, 3, dateC).isEmpty());

      ids.add(1);
      ids.add(2);
      ids.add(3);
      ids.add(4);
      List<String> codes = largeCessionManager.findEvtStockIncompatiblesManager(ids, 3, dateC);
      assertTrue(codes.size() == 1);
      assertTrue(codes.contains("PTRA.1"));

      codes = largeCessionManager.findEvtStockIncompatiblesManager(ids, 8, dateC);
      assertTrue(codes.size() == 0);

      dateC = new SimpleDateFormat("ddMMyyyy").parse("05012009");
      codes = largeCessionManager.findEvtStockIncompatiblesManager(ids, 8, dateC);
      assertTrue(codes.size() == 1);
      assertTrue(codes.contains("EHT.1.1"));

      // bad entite
      assertTrue(largeCessionManager.findIncompatibleStatutsManager(ids, 7).isEmpty());
   }
}
