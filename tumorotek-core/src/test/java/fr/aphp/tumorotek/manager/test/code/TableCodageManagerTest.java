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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.manager.code.AdicapManager;
import fr.aphp.tumorotek.manager.code.CimMasterManager;
import fr.aphp.tumorotek.manager.code.CimoMorphoManager;
import fr.aphp.tumorotek.manager.code.CodeUtilisateurManager;
import fr.aphp.tumorotek.manager.code.CommonUtilsManager;
import fr.aphp.tumorotek.manager.code.TableCodageManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.code.CimoMorpho;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 *
 * Classe de test pour le manager TableCodageManager.
 * Classe créée le 19/05/10.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class TableCodageManagerTest extends AbstractManagerTest4
{

   @Autowired
   private TableCodageManager tableCodageManager;

   @Autowired
   private BanqueDao banqueDao;

   @Autowired
   private AdicapManager adicapManager;

   @Autowired
   private CimMasterManager cimMasterManager;

   @Autowired
   private CimoMorphoManager cimoMorphoManager;

   @Autowired
   private CodeUtilisateurManager codeUtilisateurManager;

   @Autowired
   private CommonUtilsManager commonUtilsManager;

   public TableCodageManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<TableCodage> tables = tableCodageManager.findAllObjectsManager();
      assertTrue(tables.size() == 5);
   }

   @Test
   public void testFindByNomManager(){
      List<TableCodage> tables = tableCodageManager.findByNomManager("ADICAP");
      assertTrue(tables.size() == 1);
      tables = tableCodageManager.findByNomManager("JAMIROQUAI");
      assertTrue(tables.size() == 0);
      tables = tableCodageManager.findByNomManager(null);
      assertTrue(tables.size() == 0);
   }

   @Test
   public void testfindCodeByTableCodageAndIdManager(){
      TableCodage t1 = tableCodageManager.findByNomManager("ADICAP").get(0);
      final CodeCommon a1 = commonUtilsManager.findCodeByTableCodageAndIdManager(4, t1);
      assertNotNull(a1);
      assertTrue(a1.getClass().getSimpleName().equals("Adicap"));
      assertTrue((a1).getCode().equals("E"));

      t1 = tableCodageManager.findByNomManager("CIM_MASTER").get(0);
      final CodeCommon c1 = commonUtilsManager.findCodeByTableCodageAndIdManager(7, t1);
      assertNotNull(c1);
      assertTrue(c1.getClass().getSimpleName().equals("CimMaster"));
      assertTrue((c1).getCode().equals("A01"));

      t1 = tableCodageManager.findByNomManager("CIMO_MORPHO").get(0);
      final CodeCommon c2 = commonUtilsManager.findCodeByTableCodageAndIdManager(29, t1);
      assertNotNull(c2);
      assertTrue(c2.getClass().getSimpleName().equals("CimoMorpho"));
      assertTrue(((CimoMorpho) c2).getLibelle().equals("cheilite"));

      t1 = tableCodageManager.findByNomManager("UTILISATEUR").get(0);
      CodeCommon c3 = commonUtilsManager.findCodeByTableCodageAndIdManager(14, t1);
      assertNull(c3);

      c3 = commonUtilsManager.findCodeByTableCodageAndIdManager(14, null);
      assertNull(c3);
   }

   @Test
   public void testGetTableCodageFromCodeCommon(){
      TableCodage t1 = tableCodageManager.findByNomManager("ADICAP").get(0);
      CodeCommon c = commonUtilsManager.findCodeByTableCodageAndIdManager(4, t1);

      t1 = tableCodageManager.findByNomManager("CIM_MASTER").get(0);
      c = commonUtilsManager.findCodeByTableCodageAndIdManager(7, t1);
      assertTrue(commonUtilsManager.getTableCodageFromCodeCommonManager(c).equals(t1));

      t1 = tableCodageManager.findByNomManager("CIMO_MORPHO").get(0);
      c = commonUtilsManager.findCodeByTableCodageAndIdManager(29, t1);
      assertTrue(commonUtilsManager.getTableCodageFromCodeCommonManager(c).equals(t1));

      t1 = tableCodageManager.findByNomManager("UTILISATEUR").get(0);
      c = commonUtilsManager.findCodeByTableCodageAndIdManager(1, t1);
      assertTrue(commonUtilsManager.getTableCodageFromCodeCommonManager(c).equals(t1));

      c = commonUtilsManager.findCodeByTableCodageAndIdManager(14, t1);
      assertNull(commonUtilsManager.getTableCodageFromCodeCommonManager(c));

   }

   @Test
   public void testTranscodeManager(){
      final List<TableCodage> tables = new ArrayList<>();
      final TableCodage adicap = tableCodageManager.findByNomManager("ADICAP").get(0);
      final TableCodage cim = tableCodageManager.findByNomManager("CIM_MASTER").get(0);
      final TableCodage cimo = tableCodageManager.findByNomManager("CIMO_MORPHO").get(0);
      final TableCodage user = tableCodageManager.findByNomManager("UTILISATEUR").get(0);

      tables.add(cimo);

      final List<Banque> banks = new ArrayList<>();
      banks.add(banqueDao.findById(2));

      List<CodeCommon> transcodes;

      Adicap a = adicapManager.findByCodeLikeManager("OR!TE", true).get(0);
      transcodes = tableCodageManager.transcodeManager(a, tables, banks);
      assertTrue(transcodes.size() == 0);

      tables.add(cim);
      transcodes = tableCodageManager.transcodeManager(a, tables, banks);
      assertTrue(transcodes.size() == 1);
      assertTrue(transcodes.get(0).getCode().equals("C30.1"));

      a = adicapManager.findByCodeLikeManager("E0G8", true).get(0);
      transcodes = tableCodageManager.transcodeManager(a, tables, banks);
      assertTrue(transcodes.size() == 1);
      assertTrue(transcodes.get(0).getCode().equals("M-75530"));

      tables.add(user);
      a = adicapManager.findByCodeLikeManager("F", true).get(0);
      transcodes = tableCodageManager.transcodeManager(a, tables, banks);
      assertTrue(transcodes.size() == 0);
      banks.add(banqueDao.findById(1));
      transcodes = tableCodageManager.transcodeManager(a, tables, banks);
      assertTrue(transcodes.size() == 1);
      assertTrue(transcodes.get(0).getCode().equals("code2"));

      tables.add(adicap);
      CimMaster cm = cimMasterManager.findByCodeLikeManager("C02.9", true).get(0);
      transcodes = tableCodageManager.transcodeManager(cm, tables, banks);
      assertTrue(transcodes.size() == 4);

      cm = cimMasterManager.findByCodeLikeManager("A01.4", true).get(0);
      transcodes = tableCodageManager.transcodeManager(cm, tables, banks);
      assertTrue(transcodes.size() == 1);
      assertTrue(transcodes.get(0).getCode().equals("code2"));

      // null bank
      cm = cimMasterManager.findByCodeLikeManager("A01.4", true).get(0);
      transcodes = tableCodageManager.transcodeManager(cm, tables, null);
      assertTrue(transcodes.size() == 0);

      CimoMorpho co = cimoMorphoManager.findByCodeLikeManager("M-8031/3", true).get(0);
      transcodes = tableCodageManager.transcodeManager(co, tables, null);
      assertTrue(transcodes.size() == 2);

      co = cimoMorphoManager.findByCodeLikeManager("D0-20150", true).get(0);
      transcodes = tableCodageManager.transcodeManager(co, tables, banks);
      assertTrue(transcodes.size() == 2);
      assertTrue(transcodes.get(0).getCode().equals("H0V2"));
      assertTrue(transcodes.get(1).getCode().equals("code1"));

      final CodeUtilisateur cu = codeUtilisateurManager.findByCodeLikeManager("code1", true, banks).get(0);
      transcodes = tableCodageManager.transcodeManager(cu, tables, banks);
      assertTrue(transcodes.size() == 3);

      transcodes = tableCodageManager.transcodeManager(cu, null, banks);
      assertTrue(transcodes.isEmpty());

      transcodes = tableCodageManager.transcodeManager(null, tables, banks);
      assertTrue(transcodes.isEmpty());

      tables.clear();
      transcodes = tableCodageManager.transcodeManager(cu, tables, banks);
      assertTrue(transcodes.isEmpty());

      cm = cimMasterManager.findByCodeLikeManager("C02.9", true).get(0);
      transcodes = tableCodageManager.transcodeManager(cm, tables, banks);
      assertTrue(transcodes.size() == 0);
   }

   @Test
   public void testFindCodesAndTranscodesFromStringManager(){
      final TableCodage adicap = tableCodageManager.findByNomManager("ADICAP").get(0);
      final TableCodage cim = tableCodageManager.findByNomManager("CIM_MASTER").get(0);
      final TableCodage cimo = tableCodageManager.findByNomManager("CIMO_MORPHO").get(0);
      final TableCodage user = tableCodageManager.findByNomManager("UTILISATEUR").get(0);
      final List<TableCodage> tables = new ArrayList<>();
      tables.add(cim);
      tables.add(cimo);
      tables.add(user);

      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b2);

      List<CodeCommon> codes;

      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("C02.9", tables, banks, false);
      assertTrue(codes.size() == 1);

      tables.add(adicap);
      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("C02.9", tables, banks, false);
      assertTrue(codes.size() == 5);
      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("langue, sans précision", tables, banks, false);
      assertTrue(codes.size() == 8);
      //		codes = tableCodageManager
      //			.findCodesAndTranscodesFromStringManager("C0", tables, banks);
      //		assertTrue(codes.size() == 0);

      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("OR!TE", tables, banks, false);
      assertTrue(codes.size() == 2);
      assertTrue(codes.get(1).getCode().equals("C30.1"));
      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("TROMPE D'EUSTACHE", tables, banks, false);
      assertTrue(codes.size() == 10);
      //assertTrue(codes.get(1).getCode().equals("C30.1"));
      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("TROMPE", tables, banks, false);
      assertTrue(codes.size() == 31);

      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("TROMPE", tables, banks, true);
      assertTrue(codes.size() == 2);

      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("H0V2", tables, banks, false);
      assertTrue(codes.size() == 2);
      assertTrue(codes.get(0).getCode().equals("H0V2"));
      assertTrue(codes.get(1).getCode().equals("D0-20150"));

      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("M-8031/3", tables, banks, false);
      assertTrue(codes.size() == 3);
      codes =
         tableCodageManager.findCodesAndTranscodesFromStringManager("Carcinome à " + "cellules géantes", tables, banks, false);
      assertTrue(codes.size() == 7);
      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("Carcinome à", tables, banks, false);
      assertTrue(codes.size() == 177);

      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("Carcinome à", tables, banks, true);
      assertTrue(codes.size() == 0);

      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("D0-20150", tables, banks, false);
      assertTrue(codes.size() == 2);
      assertTrue(codes.get(0).getCode().equals("D0-20150"));
      assertTrue(codes.get(1).getCode().equals("H0V2"));

      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("code1", tables, banks, false);
      assertTrue(codes.size() == 0);

      banks.add(b1);
      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("code1", tables, banks, false);
      assertTrue(codes.size() == 6);

      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("D0-20150", tables, banks, false);
      assertTrue(codes.size() == 3);
      assertTrue(codes.get(2).getCode().equals("code1"));

      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("libelle1", tables, banks, false);
      assertTrue(codes.size() == 7);
      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("libelle", tables, banks, false);
      assertTrue(codes.size() == 8);

      codes = tableCodageManager.findCodesAndTranscodesFromStringManager("libelle1", null, banks, true);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testGetListCodesFromCodeCommon(){
      final CodeCommon c1 = new Adicap();
      c1.setCode("c1");
      c1.setLibelle("lib1");
      final CodeCommon c2 = new CimMaster();
      c2.setCode("c2");
      c2.setLibelle(null);
      final CodeCommon c3 = new CimoMorpho();
      c3.setCode("c3");
      c3.setLibelle("lib3");
      final CodeCommon c4 = new CodeUtilisateur();
      c4.setCode(null);
      c4.setLibelle("lib4");
      final List<CodeCommon> codes = new ArrayList<>();
      codes.add(c1);
      codes.add(c2);
      codes.add(c3);
      codes.add(c4);

      List<String> codesStr = tableCodageManager.getListCodesFromCodeCommon(codes);
      assertTrue(codesStr.size() == 3);
      assertTrue(codesStr.contains("c1"));
      assertTrue(codesStr.contains("c2"));
      assertTrue(codesStr.contains("c3"));

      codes.clear();
      codesStr = tableCodageManager.getListCodesFromCodeCommon(codes);
      assertTrue(codesStr.size() == 0);

      codesStr = tableCodageManager.getListCodesFromCodeCommon(null);
      assertTrue(codesStr.size() == 0);
   }

   @Test
   public void testGetListLibellesFromCodeCommon(){
      final CodeCommon c1 = new Adicap();
      c1.setCode("c1");
      c1.setLibelle("lib1");
      final CodeCommon c2 = new CimMaster();
      c2.setCode("c2");
      c2.setLibelle(null);
      final CodeCommon c3 = new CimoMorpho();
      c3.setCode("c3");
      c3.setLibelle("lib3");
      final CodeCommon c4 = new CodeUtilisateur();
      c4.setCode(null);
      c4.setLibelle("lib4");
      final List<CodeCommon> codes = new ArrayList<>();
      codes.add(c1);
      codes.add(c2);
      codes.add(c3);
      codes.add(c4);

      List<String> libStr = tableCodageManager.getListLibellesFromCodeCommon(codes);
      assertTrue(libStr.size() == 3);
      assertTrue(libStr.contains("lib1"));
      assertTrue(libStr.contains("lib3"));
      assertTrue(libStr.contains("lib4"));

      codes.clear();
      libStr = tableCodageManager.getListLibellesFromCodeCommon(codes);
      assertTrue(libStr.size() == 0);

      libStr = tableCodageManager.getListLibellesFromCodeCommon(null);
      assertTrue(libStr.size() == 0);
   }
}
