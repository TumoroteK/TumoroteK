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
package fr.aphp.tumorotek.test.manager.code;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.code.CodeAssigneManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.code.CodeCommonValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager CodeAssigneManager.
 * Classe créée le 24/06/10.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0.10.6
 *
 */
public class CodeAssigneManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CodeAssigneManager codeAssigneManager;
   
   @Autowired
   private OperationManager operationManager;
   
   @Autowired
   private CodeCommonValidator codeCommonValidator;
   
   @Autowired
   private EchantillonDao echantillonDao;
   
   @Autowired
   private TableCodageDao tableCodageDao;
   
   @Autowired
   private UtilisateurDao utilisateurDao;
   
   @Autowired
   private PrelevementDao prelevementDao;
   
   @Autowired
   private PatientDao patientDao;
   
   @Autowired
   private OperationTypeDao operationTypeDao;
   
   @Autowired
   @Qualifier("dataSource")
   private DataSource dataSource;

   public CodeAssigneManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<CodeAssigne> codes = codeAssigneManager.findAllObjectsManager();
      assertEquals(5, codes.size());
   }

   @Test
   public void testFindByCodeLikeManager(){
      //teste une recherche exactMatch
      List<CodeAssigne> codes = codeAssigneManager.findByCodeLikeManager("BL", true);
      assertTrue(codes.size() == 1);
      //teste une recherche non exactMatch
      codes = codeAssigneManager.findByCodeLikeManager("BL", false);
      assertTrue(codes.size() == 2);
      //teste une recherche infructueuse
      codes = codeAssigneManager.findByCodeLikeManager("LUX", true);
      assertTrue(codes.size() == 0);
      //null recherche
      codes = codeAssigneManager.findByCodeLikeManager(null, false);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindByLibelleLikeManager(){
      //teste une recherche exactMatch
      List<CodeAssigne> codes = codeAssigneManager.findByLibelleLikeManager("langue2", true);
      assertTrue(codes.size() == 1);
      //teste une recherche non exactMatch
      codes = codeAssigneManager.findByLibelleLikeManager("langue", false);
      assertTrue(codes.size() == 4);
      //teste une recherche infructueuse
      codes = codeAssigneManager.findByLibelleLikeManager("LUX", true);
      assertTrue(codes.size() == 0);
      //null recherche
      codes = codeAssigneManager.findByLibelleLikeManager(null, false);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindCodesOrganeByEchantillon(){
      Echantillon e = echantillonDao.findById(1).get();
      List<CodeAssigne> codes = codeAssigneManager.findCodesOrganeByEchantillonManager(e);
      assertTrue(codes.size() == 2);
      e = echantillonDao.findById(2).get();
      codes = codeAssigneManager.findCodesOrganeByEchantillonManager(e);
      assertTrue(codes.size() == 0);
      codes = codeAssigneManager.findCodesOrganeByEchantillonManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindCodesMorphoByEchantillon(){
      Echantillon e = echantillonDao.findById(1).get();
      List<CodeAssigne> codes = codeAssigneManager.findCodesMorphoByEchantillonManager(e);
      assertTrue(codes.size() == 3);
      e = echantillonDao.findById(2).get();
      codes = codeAssigneManager.findCodesMorphoByEchantillonManager(e);
      assertTrue(codes.size() == 0);
      codes = codeAssigneManager.findCodesMorphoByEchantillonManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindDoublonManager(){
      //Cree le doublon
      //		CodeAssigne c1 = codeAssigneManager
      //									.findByCodeLikeManager("BL", true).get(0);
      //		
      //		CodeAssigne c2 = new CodeAssigne();
      //		c2.setCode(c1.getCode());
      //		c2.setLibelle(c1.getLibelle());
      //		c2.setEchantillon(c1.getEchantillon());
      //		c2.setCodeRefId(c1.getCodeRefId());
      //		c2.setTableCodage(c1.getTableCodage());
      //		c2.setIsOrgane(c1.getIsOrgane());
      //		c2.setIsMorpho(c1.getIsMorpho());
      //		assertTrue(codeAssigneManager.findDoublonManager(c2));
      //		
      //		c1.setCode("K14.5");
      //		c1.setLibelle("langue plicaturée");
      //		c1.setCodeRefId(4630);
      //		c1.setTableCodage(tableCodageDao.findById(2)).get();
      //		c1.setIsMorpho(true);
      //		c1.setIsOrgane(false);
      //		assertTrue(codeAssigneManager.findDoublonManager(c1));

      final CodeAssigne c3 = new CodeAssigne();
      c3.setCode("NH");
      c3.setLibelle(null);
      c3.setEchantillon(null);
      c3.setIsOrgane(true);
      c3.setIsMorpho(false);
      assertFalse(codeAssigneManager.findDoublonManager(c3));
   }

   @Test
   public void testCRUD(){
	   createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   public void createObjectManagerTest(){

      final CodeAssigne code = new CodeAssigne();
      /*Champs obligatoires*/
      code.setCode("newCode");
      final Echantillon e = echantillonDao.findById(2).get();
      code.setLibelle("newLib");
      code.setIsOrgane(false);
      code.setIsMorpho(true);
      code.setOrdre(1);
      final TableCodage t = tableCodageDao.findById(1).get();
      final Utilisateur u = utilisateurDao.findById(1).get();

      //required Echantillon
      try{
         codeAssigneManager.createOrUpdateManager(code, null, null, u, "creation");
      }catch(final RequiredObjectIsNullException re){
         assertTrue(true);
      }
      //operation invalide
      try{
         codeAssigneManager.createOrUpdateManager(code, e, null, u, null);
      }catch(final NullPointerException ne){
         assertTrue(ne.getMessage().equals("operation cannot be " + "set to null for createorUpdateMethod"));
      }
      try{
         codeAssigneManager.createOrUpdateManager(code, e, null, u, "");
      }catch(final IllegalArgumentException ie){
         assertTrue(ie.getMessage().equals("Operation must match " + "'creation/modification' values"));
      }
      // table et codeRef
      //		try {
      //			codeAssigneManager.createOrUpdateManager(code, e, t, u, "creation");
      //		} catch (ValidationException re) {
      //			assertTrue(re.getErrors().get(0).getFieldError()
      //							.getCode().equals("code.codeRefIDOrTableEmpty"));
      //		}
      //		code.setCodeRefId(1);
      //		code.setTableCodage(null);
      //		try {
      //			codeAssigneManager
      //						.createOrUpdateManager(code, e, null, u, "creation");
      //		} catch (ValidationException re) {
      //			assertTrue(re.getErrors().get(0).getFieldError()
      //							.getCode().equals("code.codeRefIDOrTableEmpty"));
      //		}

      testFindAllObjectsManager();
      codeAssigneManager.createOrUpdateManager(code, e, t, u, "creation");
      assertTrue((codeAssigneManager.findByCodeLikeManager("newCode", true)).size() == 1);
      assertTrue((codeAssigneManager.findByLibelleLikeManager("newLib", false)).size() == 1);
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(e).size() == 1);
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(e).size() == 0);

      assertTrue(operationManager.findByObjectManager(code).size() == 1);
      assertTrue(operationManager.findByObjectManager(code).get(0).getOperationType().getNom().equals("Creation"));

      //Insertion d'un doublon engendrant une exception
      final CodeAssigne c2 = new CodeAssigne();
      c2.setCode(code.getCode());
      c2.setLibelle(code.getLibelle());
      c2.setIsOrgane(code.getIsOrgane());
      c2.setIsMorpho(code.getIsMorpho());
      c2.setCodeRefId(code.getCodeRefId());
      Boolean catched = false;
      try{
         codeAssigneManager.createOrUpdateManager(c2, e, t, u, "creation");
      }catch(final Exception ex){
         if(ex.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }else{
            ex.printStackTrace();
         }
      }
      assertTrue(catched);
      assertTrue((codeAssigneManager.findByCodeLikeManager("newCode", true)).size() == 1);
      assertTrue(operationManager.findByObjectManager(code).size() == 1);
   }

   public void updateObjectManagerTest(){
      final CodeAssigne c = codeAssigneManager.findByCodeLikeManager("newCode", true).get(0);
      c.setCode("##$$#~^÷××");
      c.setLibelle(null);
      c.setCodeRefId(null);
      c.setTableCodage(null);
      final Utilisateur u = utilisateurDao.findById(1).get();

      //Modification d'un code entrainant validationException
      boolean catched = false;
      try{
         codeAssigneManager.createOrUpdateManager(c, null, null, u, "modification");
      }catch(final ValidationException e){
         catched = true;
      }
      assertTrue(catched);

      c.setCode("AnotherCode");
      codeAssigneManager.createOrUpdateManager(c, null, null, u, "modification");
      assertTrue((codeAssigneManager.findByCodeLikeManager("AnotherCode", true)).size() == 1);
      final Echantillon e = echantillonDao.findById(2).get();
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(e).size() == 1);
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(e).size() == 0);

      assertTrue(operationManager.findByObjectManager(c).size() == 2);
      assertTrue(
         operationManager.findByObjetIdEntiteAndOpeTypeManager(c, operationTypeDao.findByNom("Modification").get(0)).size() == 1);

      // teste si modifications enregistres
      c.setExport(true);
      codeAssigneManager.createOrUpdateManager(c, null, null, u, "modification");
      assertTrue(operationManager.findByObjectManager(c).size() == 2);
      c.setLibelle("newLib");
      codeAssigneManager.createOrUpdateManager(c, null, null, u, "modification");
      assertTrue(operationManager.findByObjectManager(c).size() == 3);
      c.setLibelle("newLib2");
      codeAssigneManager.createOrUpdateManager(c, null, null, u, "modification");
      assertTrue(operationManager.findByObjectManager(c).size() == 4);
      c.setExport(true);
      codeAssigneManager.createOrUpdateManager(c, null, null, u, "modification");
      assertTrue(operationManager.findByObjectManager(c).size() == 4);
      c.setLibelle(null);
      codeAssigneManager.createOrUpdateManager(c, null, null, u, "modification");
      assertTrue(operationManager.findByObjectManager(c).size() == 5);

      //Modification en un doublon engendrant une exception
      catched = false;
      try{
         c.setCode("BL");
         c.setLibelle("LANGUE2");
         final Echantillon e1 = echantillonDao.findById(1).get();
         final TableCodage t1 = tableCodageDao.findById(1).get();
         c.setCodeRefId(55);
         c.setIsMorpho(false);
         c.setIsOrgane(true);
         codeAssigneManager.createOrUpdateManager(c, e1, t1, u, "modification");
      }catch(final DoublonFoundException de){
         catched = true;
      }
      assertTrue(catched);
      assertTrue((codeAssigneManager.findByCodeLikeManager("AnotherCode", true)).size() == 1);
   }

   public void removeObjectManagerTest(){
      final CodeAssigne c = codeAssigneManager.findByCodeLikeManager("AnotherCode", true).get(0);
      codeAssigneManager.removeObjectManager(c);
      assertTrue(codeAssigneManager.findByCodeLikeManager("AnotherCode", true).size() == 0);
      assertTrue(operationManager.findByObjectManager(c).size() == 0);
      //verifie que l'etat des tables modifies est revenu identique
      assertTrue(operationManager.findAllObjectsManager().size() == 19);
      testFindAllObjectsManager();
      // null remove
      codeAssigneManager.removeObjectManager(null);
   }

   @Test
   public void testCodeValidation(){
      final CodeAssigne c = new CodeAssigne();

      // code
      List<Errors> errs = new ArrayList<>();
      try{
         BeanValidator.validateObject(c, new Validator[] {codeCommonValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("code.code.empty"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      c.setCode("$$###''");
      try{
         BeanValidator.validateObject(c, new Validator[] {codeCommonValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("code.code.illegal"));
      }
      c.setCode("ok");
      c.setLibelle("Tumeurs contigües, sans précisions");
      // morpho-organe
      c.setIsMorpho(true);
      c.setIsOrgane(true);
      try{
         BeanValidator.validateObject(c, new Validator[] {codeCommonValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("code.bothIsOrganeAndIsMorpho"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      c.setIsMorpho(false);
      BeanValidator.validateObject(c, new Validator[] {codeCommonValidator});
   }

   @Test
   public void testFindCodeLesExportedByPrelevementManager(){
      final Prelevement p1 = prelevementDao.findById(1).get();
      List<CodeAssigne> codes = codeAssigneManager.findCodesLesExportedByPrelevementManager(p1);
      assertTrue(codes.size() == 1);
      assertTrue(codes.get(0).getCode().equals("D5-22050"));
      final Prelevement p2 = prelevementDao.findById(2).get();
      codes = codeAssigneManager.findCodesLesExportedByPrelevementManager(p2);
      assertTrue(codes.size() == 0);
      codes = codeAssigneManager.findCodesLesExportedByPrelevementManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindFirstCodesLesByPrelevementManager(){
      final Prelevement p1 = prelevementDao.findById(1).get();
      List<CodeAssigne> codes = codeAssigneManager.findFirstCodesLesByPrelevementManager(p1);
      assertTrue(codes.size() == 1);
      assertTrue(codes.get(0).getCode().equals("K14.5"));
      final Prelevement p2 = prelevementDao.findById(2).get();
      codes = codeAssigneManager.findFirstCodesLesByPrelevementManager(p2);
      assertTrue(codes.size() == 0);
      codes = codeAssigneManager.findFirstCodesLesByPrelevementManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindCodeOrgExportedByPrelevement(){
      final Prelevement p1 = prelevementDao.findById(1).get();
      List<CodeAssigne> codes = codeAssigneManager.findCodesOrgExportedByPrelevementManager(p1);
      assertTrue(codes.size() == 1);
      assertTrue(codes.get(0).getCode().equals("BL"));
      final Prelevement p2 = prelevementDao.findById(2).get();
      codes = codeAssigneManager.findCodesOrgExportedByPrelevementManager(p2);
      assertTrue(codes.size() == 0);
      codes = codeAssigneManager.findCodesOrgExportedByPrelevementManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindFirstCodesOrgByPrelevementManager(){
      final Prelevement p1 = prelevementDao.findById(1).get();
      List<CodeAssigne> codes = codeAssigneManager.findFirstCodesOrgByPrelevementManager(p1);
      assertTrue(codes.size() == 1);
      assertTrue(codes.get(0).getCode().equals("BL"));
      final Prelevement p2 = prelevementDao.findById(2).get();
      codes = codeAssigneManager.findFirstCodesOrgByPrelevementManager(p2);
      assertTrue(codes.size() == 0);
      codes = codeAssigneManager.findFirstCodesOrgByPrelevementManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindCodeOrgExportedByPatient(){
      final Patient p3 = patientDao.findById(3).get();
      List<CodeAssigne> codes = codeAssigneManager.findCodesOrgExportedByPatientManager(p3);
      assertTrue(codes.size() == 1);
      assertTrue(codes.get(0).getCode().equals("BL"));
      final Patient p1 = patientDao.findById(1).get();
      codes = codeAssigneManager.findCodesOrgExportedByPatientManager(p1);
      assertTrue(codes.size() == 0);
      codes = codeAssigneManager.findCodesOrgExportedByPatientManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindFirstCodesOrgByPatientManager(){
      final Patient p3 = patientDao.findById(3).get();
      List<CodeAssigne> codes = codeAssigneManager.findFirstCodesOrgByPatientManager(p3);
      assertTrue(codes.size() == 1);
      assertTrue(codes.get(0).getCode().equals("BL"));
      final Patient p1 = patientDao.findById(1).get();
      codes = codeAssigneManager.findFirstCodesOrgByPatientManager(p1);
      assertTrue(codes.size() == 0);
      codes = codeAssigneManager.findFirstCodesOrgByPatientManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFormatCodesAsStringsManager(){
      // creation artificielle de codes assignes
      final Echantillon e = echantillonDao.findById(2).get();
      // e -> banque 1 -> cim en libelle, le reste en codes
      final CodeAssigne a1 = new CodeAssigne();
      a1.setTableCodage(tableCodageDao.findById(1).get());
      a1.setEchantillon(e);
      a1.setCode("a1");
      a1.setLibelle("la1");
      final CodeAssigne c2 = new CodeAssigne();
      c2.setTableCodage(tableCodageDao.findById(2).get());
      c2.setEchantillon(e);
      c2.setCode("c2");
      c2.setLibelle(null);
      final CodeAssigne c2b = new CodeAssigne();
      c2b.setTableCodage(tableCodageDao.findById(2).get());
      c2b.setEchantillon(e);
      c2b.setCode("c2b");
      c2b.setLibelle("lib2");
      final CodeAssigne c3 = new CodeAssigne();
      c3.setTableCodage(tableCodageDao.findById(3).get());
      c3.setEchantillon(e);
      c3.setCode("c3");
      c3.setLibelle("lc3");
      final CodeAssigne u4 = new CodeAssigne();
      u4.setTableCodage(tableCodageDao.findById(4).get());
      u4.setEchantillon(e);
      u4.setCode("u4");
      u4.setLibelle("lib4");
      final CodeAssigne a2 = new CodeAssigne();
      a2.setTableCodage(tableCodageDao.findById(1).get());
      a2.setEchantillon(e);
      a2.setCode("a2");
      a2.setLibelle("la2");

      final List<CodeAssigne> codes = new ArrayList<>();
      codes.add(a1);
      codes.add(c2);
      codes.add(c2b);
      codes.add(c3);
      codes.add(u4);
      codes.add(a2);

      List<String> strs = codeAssigneManager.formatCodesAsStringsManager(codes);
      assertTrue(strs.size() == 6);
      assertTrue(strs.get(0).equals("a1"));
      assertTrue(strs.get(1).equals("c2"));
      assertTrue(strs.get(2).equals("lib2"));
      assertTrue(strs.get(3).equals("c3"));
      assertTrue(strs.get(4).equals("u4"));
      assertTrue(strs.get(5).equals("a2"));

      // chgt de banque
      final Echantillon e4 = echantillonDao.findById(4).get();
      a1.setEchantillon(e4);
      c2.setEchantillon(e4);
      c2b.setEchantillon(e4);
      c3.setEchantillon(e4);
      u4.setEchantillon(e4);
      a2.setEchantillon(e4);

      strs = codeAssigneManager.formatCodesAsStringsManager(codes);
      assertTrue(strs.size() == 6);
      assertTrue(strs.get(0).equals("a1"));
      assertTrue(strs.get(1).equals("c2"));
      assertTrue(strs.get(2).equals("c2b"));
      assertTrue(strs.get(3).equals("c3"));
      assertTrue(strs.get(4).equals("lib4"));
      assertTrue(strs.get(5).equals("a2"));

      codes.clear();
      strs = codeAssigneManager.formatCodesAsStringsManager(codes);
      assertTrue(strs.size() == 0);

      strs = codeAssigneManager.formatCodesAsStringsManager(null);
      assertTrue(strs.size() == 0);
   }

   @Test
   public void testPrepareListJDBC() throws SQLException{

      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      final EchantillonJdbcSuite suite = new EchantillonJdbcSuite();
      try{
         conn = DataSourceUtils.getConnection(dataSource);
         stmt = conn.createStatement();
         rs = stmt.executeQuery("select max(code_assigne_id)" + " from CODE_ASSIGNE");
         rs.first();
         final Integer maxCdId = rs.getInt(1);
         suite.setMaxCodeAssigneId(maxCdId);

         final String sql = "insert into CODE_ASSIGNE (CODE_ASSIGNE_ID, ECHANTILLON_ID, "
            + "CODE, LIBELLE, CODE_REF_ID, TABLE_CODAGE_ID, IS_ORGANE, " + "IS_MORPHO, ORDRE, EXPORT) "
            + "values (?,?,?,?,?,?,?,?,?,?)";
         suite.setPstmtCd(conn.prepareStatement(sql));

         final String sql2 = "insert into OPERATION (UTILISATEUR_ID, " + "OBJET_ID, ENTITE_ID, OPERATION_TYPE_ID, " + "DATE_, V1)"
            + "values (?,?,?,?,?,?)";
         suite.setPstmtOp(conn.prepareStatement(sql2));

         final Echantillon e2 = echantillonDao.findById(2).get();
         final Utilisateur u1 = utilisateurDao.findById(1).get();
         final List<CodeAssigne> codes = new ArrayList<>();

         // c1 toutes les valeurs assignees
         final CodeAssigne c1 = new CodeAssigne();
         c1.setCode("c1");
         c1.setLibelle("lib1");
         c1.setIsOrgane(true);
         c1.setIsMorpho(false);
         c1.setOrdre(1);
         c1.setExport(true);
         c1.setTableCodage(tableCodageDao.findById(1).get());
         c1.setCodeRefId(2);
         codes.add(c1);

         // c2 seules les valeurs obligatoires
         final CodeAssigne c2 = new CodeAssigne();
         c2.setCode("c2");
         c2.setIsOrgane(true);
         c2.setOrdre(2);
         c2.setExport(false);
         codes.add(c2);

         // c3 erreurs pour tester rollback
         final CodeAssigne c3 = new CodeAssigne();
         c3.setCode("c3");
         c3.setIsOrgane(true);
         c3.setIsMorpho(true);
         c3.setOrdre(1);
         c3.setExport(true);
         codes.add(c3);

         //required Echantillon
         boolean catched = false;
         try{
            codeAssigneManager.prepareListJDBCManager(suite, null, codes, u1);
         }catch(final RequiredObjectIsNullException re){
            catched = true;
         }
         assertTrue(catched);
         catched = false;
         try{
            codeAssigneManager.prepareListJDBCManager(suite, new Echantillon(), codes, u1);
         }catch(final RequiredObjectIsNullException re){
            catched = true;
         }
         assertTrue(catched);

         //validation isOrgane & isMorpho true
         catched = false;
         try{
            codeAssigneManager.prepareListJDBCManager(suite, e2, codes, u1);
         }catch(final ValidationException ve){
            catched = true;
         }
         assertTrue(catched);

         //doublon
         c3.setIsOrgane(false);
         //		c3.setCode("K14.5");
         //		c3.setLibelle("langue plicaturée");
         //		c3.setCodeRefId(4630);
         //		c3.setTableCodage(tableCodageDao.findById(2)).get();
         //		catched = false;
         //		try {		
         //			codeAssigneManager.createListJDBCManager(echantillonDao.findById(1).get(), 
         //											codes, u1);
         //		} catch (DoublonFoundException de) {
         //			catched = true;
         //		}
         //		assertTrue(catched);

         testFindAllObjectsManager();

         // id n'a pas changé
         assertTrue(suite.getMaxCodeAssigneId().equals(maxCdId));

         suite.getPstmtCd().clearBatch();
         suite.getPstmtOp().clearBatch();

         //		c3.setCode("c3");

         codeAssigneManager.prepareListJDBCManager(suite, e2, codes, u1);
         suite.getPstmtCd().executeBatch();
         suite.getPstmtOp().executeBatch();

         assertTrue(suite.getMaxCodeAssigneId().equals(maxCdId + 3));

         // asserts
         assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(e2).size() == 2);
         assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(e2).size() == 1);

         final CodeAssigne c1b = codeAssigneManager.findByCodeLikeManager("c1", true).get(0);
         assertTrue(c1b.getLibelle().equals("lib1"));
         assertTrue(c1b.getIsOrgane());
         assertFalse(c1b.getIsMorpho());
         assertTrue(c1b.getOrdre() == 1);
         assertTrue(c1b.getExport());
         assertTrue(c1b.getTableCodage().equals(tableCodageDao.findById(1).get()));
         assertTrue(c1b.getCodeRefId() == 2);
         assertTrue(c1b.getEchantillon().equals(e2));

         assertTrue(operationManager.findByObjectManager(c1b).size() == 1);
         assertTrue(operationManager.findByObjectManager(c1b).get(0).getOperationType().getNom().equals("Creation"));
         assertTrue(operationManager.findByObjectManager(c1b).get(0).getUtilisateur().equals(u1));

         final CodeAssigne c2b = codeAssigneManager.findByCodeLikeManager("c2", true).get(0);
         assertNull(c2b.getLibelle());
         assertTrue(c2b.getIsOrgane());
         assertNull(c2b.getIsMorpho());
         assertTrue(c2b.getOrdre() == 2);
         assertFalse(c2b.getExport());
         assertNull(c2b.getTableCodage());
         assertNull(c2b.getCodeRefId());
         assertTrue(c2b.getEchantillon().equals(e2));

         assertTrue(operationManager.findByObjectManager(c2b).size() == 1);
         assertTrue(operationManager.findByObjectManager(c2b).get(0).getOperationType().getNom().equals("Creation"));
         assertTrue(operationManager.findByObjectManager(c2b).get(0).getUtilisateur().equals(u1));

         final CodeAssigne c3b = codeAssigneManager.findByCodeLikeManager("c3", true).get(0);
         assertNull(c3b.getLibelle());
         assertFalse(c3b.getIsOrgane());
         assertTrue(c3b.getIsMorpho());
         assertTrue(c3b.getOrdre() == 1);
         assertTrue(c3b.getExport());
         assertNull(c3b.getTableCodage());
         assertNull(c3b.getCodeRefId());
         assertTrue(c3b.getEchantillon().equals(e2));
         //		assertTrue(c3b.getLibelle().equals("langue plicaturée"));
         //		assertFalse(c3b.getIsOrgane());
         //		assertTrue(c3b.getIsMorpho());
         //		assertTrue(c3b.getOrdre() == 1);
         //		assertTrue(c3b.getExport());
         //		assertTrue(c3b.getTableCodage().equals(tableCodageDao.findById(2))).get();
         //		assertTrue(c3b.getCodeRefId() == 4630);
         //		assertTrue(c3b.getEchantillon().equals(e2));

         assertTrue(operationManager.findByObjectManager(c3b).size() == 1);
         assertTrue(operationManager.findByObjectManager(c3b).get(0).getOperationType().getNom().equals("Creation"));
         assertTrue(operationManager.findByObjectManager(c3b).get(0).getUtilisateur().equals(u1));

         // clean up 
         codeAssigneManager.removeObjectManager(c1b);
         codeAssigneManager.removeObjectManager(c2b);
         codeAssigneManager.removeObjectManager(c3b);

         testFindAllObjectsManager();

         cleanUpFantomes(null);
      }catch(final Exception e){
         e.printStackTrace();
      }finally{
         if(null != stmt)
            stmt.close();
         if(null != rs)
            rs.close();
         if(null != conn)
            conn.close();
         suite.closePs();
      }
   }
}
