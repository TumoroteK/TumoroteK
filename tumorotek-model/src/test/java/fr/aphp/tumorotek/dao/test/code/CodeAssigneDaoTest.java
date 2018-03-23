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
package fr.aphp.tumorotek.dao.test.code;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.code.CodeAssigneDao;
import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 *
 * Classe de test pour le DAO CodeAssigneDao et le bean du
 * domaine CodeAssigne.
 * Classe de test créée le 21/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class CodeAssigneDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private CodeAssigneDao codeAssigneDao;
   /** Bean Dao. */
   private EchantillonDao echantillonDao;
   /** Bean Dao. */
   private TableCodageDao tableCodageDao;
   private PrelevementDao prelevementDao;
   private PatientDao patientDao;

   /**
    * Constructeur.
    */
   public CodeAssigneDaoTest(){}

   /**
    * Setter du bean Dao.
    * @param cDao est le bean Dao.
    */
   public void setCodeAssigneDao(final CodeAssigneDao cDao){
      this.codeAssigneDao = cDao;
   }

   /**
    * Setter du bean Dao.
    * @param eDao est le bean Dao.
    */
   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   /**
    * Setter du bean Dao.
    * @param tDao est le bean Dao.
    */
   public void setTableCodageDao(final TableCodageDao tDao){
      this.tableCodageDao = tDao;
   }

   public void setPrelevementDao(final PrelevementDao pDao){
      this.prelevementDao = pDao;
   }

   public void setPatientDao(final PatientDao pDao){
      this.patientDao = pDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllCategories(){
      final List<CodeAssigne> codediags = codeAssigneDao.findAll();
      assertTrue(codediags.size() == 5);
   }

   public void testFindByCodeLike(){
      List<CodeAssigne> codes = codeAssigneDao.findByCodeLike("BL%");
      assertTrue(codes.size() == 2);
      codes = codeAssigneDao.findByCodeLike("D5-22050");
      assertTrue(codes.size() == 1);
   }

   public void testFindByCodeAndEchantillon(){
      final Echantillon e1 = echantillonDao.findById(1);
      List<CodeAssigne> codes = codeAssigneDao.findByCodeAndEchantillon("BL%", e1);
      assertTrue(codes.size() == 2);
      codes = codeAssigneDao.findByCodeAndEchantillon("D5-22050", e1);
      assertTrue(codes.size() == 1);
      codes = codeAssigneDao.findByCodeAndEchantillon("%", e1);
      assertTrue(codes.size() == 5);
      final Echantillon e2 = echantillonDao.findById(2);
      codes = codeAssigneDao.findByCodeAndEchantillon("BL%", e2);
      assertTrue(codes.size() == 0);
      codes = codeAssigneDao.findByCodeAndEchantillon("BL%", null);
      assertTrue(codes.size() == 0);
      codes = codeAssigneDao.findByCodeAndEchantillon(null, e1);
      assertTrue(codes.size() == 0);
   }

   public void testFindByLibelleLike(){
      List<CodeAssigne> codes = codeAssigneDao.findByLibelleLike("Langue%");
      assertTrue(codes.size() == 3);
      codes = codeAssigneDao.findByLibelleLike("Langue2");
      assertTrue(codes.size() == 1);
   }

   public void testFindCodesMorphoByEchantillon(){
      Echantillon e = echantillonDao.findById(1);
      List<CodeAssigne> codeOrganes = codeAssigneDao.findCodesMorphoByEchantillon(e);
      assertTrue(codeOrganes.size() == 3);
      assertTrue(codeOrganes.get(0).getCode().equals("K14.5"));
      assertTrue(codeOrganes.get(1).getCode().equals("BL0211-2"));
      assertTrue(codeOrganes.get(2).getCode().equals("D5-22050"));
      e = echantillonDao.findById(3);
      codeOrganes = codeAssigneDao.findCodesMorphoByEchantillon(e);
      assertTrue(codeOrganes.size() == 0);
   }

   public void testFindCodesOrganeByEchantillon(){
      Echantillon e = echantillonDao.findById(1);
      List<CodeAssigne> codeOrganes = codeAssigneDao.findCodesOrganeByEchantillon(e);
      assertTrue(codeOrganes.size() == 2);
      e = echantillonDao.findById(3);
      assertTrue(codeOrganes.get(0).getCode().equals("BL"));
      assertTrue(codeOrganes.get(1).getCode().equals("C02.0.1234"));
      codeOrganes = codeAssigneDao.findCodesOrganeByEchantillon(e);
      assertTrue(codeOrganes.size() == 0);
   }

   public void findByExcludedId(){
      final CodeAssigne c = codeAssigneDao.findById(1);
      List<CodeAssigne> codes = codeAssigneDao.findByExcludedId(c.getCodeAssigneId(), c.getCode(), c.getEchantillon());
      assertTrue(codes.size() == 0);
      codes = codeAssigneDao.findByExcludedId(8, "BL", echantillonDao.findById(1));
      assertTrue(codes.size() == 1);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un CodeDiagnostic.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrud() throws Exception{
      final CodeAssigne c = new CodeAssigne();
      final String codeUpdated = "UPDATED";

      final Echantillon e = echantillonDao.findById(1);
      final TableCodage tc = tableCodageDao.findById(4);
      c.setEchantillon(e);
      c.setTableCodage(tc);
      c.setCodeRefId(1);
      c.setCode("TEST");
      c.setIsOrgane(false);
      c.setIsMorpho(true);
      // Test de l'insertion
      codeAssigneDao.createObject(c);
      assertNotNull(c.getCodeAssigneId());

      final Integer cId = c.getCodeAssigneId();

      // Test de la mise à jour
      final CodeAssigne c2 = codeAssigneDao.findById(cId);
      assertNotNull(c2);
      assertTrue(c2.getCode().equals("TEST"));
      assertNotNull(c2.getEchantillon());
      assertNotNull(c2.getTableCodage());
      assertTrue(c2.getOrdre() == 1);
      assertFalse(c2.getExport());
      c2.setCode(codeUpdated);
      codeAssigneDao.updateObject(c2);
      assertTrue(codeAssigneDao.findById(cId).getCode().equals(codeUpdated));

      // Test de la délétion
      codeAssigneDao.removeObject(cId);
      assertNull(codeAssigneDao.findById(cId));

   }

   public void testEqualsAndHashCode(){
      final CodeAssigne c1 = new CodeAssigne();
      final CodeAssigne c2 = new CodeAssigne();
      assertFalse(c1.equals(null));
      assertNotNull(c2);
      assertTrue(c1.equals(c1));
      assertTrue(c1.equals(c2));
      assertTrue(c1.hashCode() == c2.hashCode());

      final String s1 = "code1";
      final String s2 = "code2";
      final String s3 = new String("code2");

      c1.setCode(s1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setCode(s2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setCode(s2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setCode(s3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      final String l1 = "libelle1";
      final String l2 = "libelle2";
      final String l3 = new String("libelle2");

      c1.setLibelle(l1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setLibelle(l2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setLibelle(l2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setLibelle(l3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      final Echantillon e1 = echantillonDao.findById(1);
      final Echantillon e2 = echantillonDao.findById(2);
      final Echantillon e3 = new Echantillon();
      e3.setBanque(e2.getBanque());
      e3.setCode(e2.getCode());
      assertFalse(e1.equals(e2));
      assertFalse(e1.hashCode() == e2.hashCode());
      assertTrue(e2.equals(e3));
      c1.setEchantillon(e1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setEchantillon(e2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setEchantillon(e3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setEchantillon(e2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      final Integer i1 = 1;
      final Integer i2 = 2;
      final Integer i3 = new Integer(2);

      c1.setCodeRefId(i1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setCodeRefId(i2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setCodeRefId(i2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setCodeRefId(i3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      final TableCodage t1 = tableCodageDao.findById(1);
      final TableCodage t2 = tableCodageDao.findById(2);
      final TableCodage t3 = new TableCodage();
      t3.setNom(t2.getNom());
      t3.setVersion(t2.getVersion());
      assertFalse(t1.equals(t2));
      assertFalse(t1.hashCode() == t2.hashCode());
      assertTrue(t2.equals(t3));
      c1.setTableCodage(t1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setTableCodage(t2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setTableCodage(t3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setTableCodage(t2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      final Boolean b1 = true;
      final Boolean b2 = false;
      final Boolean b3 = new Boolean(false);
      c1.setIsOrgane(b1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setIsOrgane(b2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setIsOrgane(b3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setIsOrgane(b2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      c1.setIsMorpho(b1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setIsMorpho(b2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setIsMorpho(b3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setIsMorpho(b2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      // dummy
      final Categorie c = new Categorie();
      assertFalse(c1.equals(c));
   }

   public void testToString(){
      final CodeAssigne c = new CodeAssigne();
      assertTrue(c.toString().equals("{Empty CodeAssigne}"));
      c.setCode("CodeAss");
      c.setIsOrgane(true);
      assertTrue(c.toString().equals("{CodeOrgane: CodeAss}"));
      c.setIsOrgane(false);
      c.setIsMorpho(true);
      assertTrue(c.toString().equals("{CodeMorpho: CodeAss}"));
      c.setIsMorpho(false);
      assertTrue(c.toString().equals("{CodeDiag: CodeAss}"));
   }

   public void testClone(){
      final CodeAssigne c = codeAssigneDao.findById(2);
      //c.setEchanExpLes(echantillonDao.findById(3));
      //c.setEchanExpOrg(echantillonDao.findById(3));
      final CodeAssigne clone = c.clone();
      assertTrue(clone.equals(c));
      assertTrue(clone.hashCode() == c.hashCode());
      assertTrue(clone.getCodeAssigneId().equals(c.getCodeAssigneId()));
      assertTrue(clone.getEchantillon().equals(c.getEchantillon()));
      assertTrue(clone.getCode().equals(c.getCode()));
      assertTrue(clone.getLibelle().equals(c.getLibelle()));
      assertTrue(clone.getIsMorpho().equals(c.getIsMorpho()));
      assertTrue(clone.getIsOrgane().equals(c.getIsOrgane()));
      assertTrue(clone.getCodeRefId().equals(c.getCodeRefId()));
      assertTrue(clone.getTableCodage().equals(c.getTableCodage()));
      //assertTrue(clone.getEchanExpLes().equals(c.getEchanExpLes()));
      //assertTrue(clone.getEchanExpOrg()
      //								.equals(c.getEchanExpOrg()));
      assertTrue(clone.getOrdre().equals(c.getOrdre()));
      assertTrue(clone.getExport().equals(c.getExport()));
   }

   public void testFindCodeLesExportedByPrelevement(){
      final Prelevement p1 = prelevementDao.findById(1);
      List<CodeAssigne> codes = codeAssigneDao.findCodesLesExportedByPrelevement(p1);
      assertTrue(codes.size() == 1);
      assertTrue(codes.get(0).getCode().equals("D5-22050"));
      final Prelevement p2 = prelevementDao.findById(2);
      codes = codeAssigneDao.findCodesLesExportedByPrelevement(p2);
      assertTrue(codes.size() == 0);
   }

   public void testFindCodeOrgExportedByPrelevement(){
      final Prelevement p1 = prelevementDao.findById(1);
      List<CodeAssigne> codes = codeAssigneDao.findCodesOrgExportedByPrelevement(p1);
      assertTrue(codes.size() == 1);
      assertTrue(codes.get(0).getCode().equals("BL"));
      final Prelevement p2 = prelevementDao.findById(2);
      codes = codeAssigneDao.findCodesOrgExportedByPrelevement(p2);
      assertTrue(codes.size() == 0);
   }

   public void testFindCodeOrgExportedByPatient(){
      final Patient p3 = patientDao.findById(3);
      List<CodeAssigne> codes = codeAssigneDao.findCodesOrgExportedByPatient(p3);
      assertTrue(codes.size() == 1);
      assertTrue(codes.get(0).getCode().equals("BL"));
      final Patient p1 = patientDao.findById(1);
      codes = codeAssigneDao.findCodesOrgExportedByPatient(p1);
      assertTrue(codes.size() == 0);
   }
}
