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

import fr.aphp.tumorotek.dao.code.CodeDossierDao;
import fr.aphp.tumorotek.dao.code.CodeSelectDao;
import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le DAO CodeSelectDao et le bean du domaine CodeSelect.
 * Classe de test créée le 18/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class CodeSelectDaoTest extends AbstractDaoTest
{

   @Autowired
 CodeSelectDao codeSelectDao;
   @Autowired
 UtilisateurDao utilisateurDao;
   @Autowired
 BanqueDao banqueDao;
   @Autowired
 TableCodageDao tableCodesDao;
   @Autowired
 CodeDossierDao codeDossierDao;

   public CodeSelectDaoTest(){}

   @Test
public void setCodeSelectDao(final CodeSelectDao cDao){
      this.codeSelectDao = cDao;
   }

   @Test
public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   @Test
public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   @Test
public void setTableCodageDao(final TableCodageDao tCDao){
      this.tableCodesDao = tCDao;
   }

   @Test
public void setCodeDossierDao(final CodeDossierDao cDao){
      this.codeDossierDao = cDao;
   }

   @Test
public void testReadAllCodes(){
      final List<CodeSelect> codeSelects = IterableUtils.toList(codeSelectDao.findAll());
      assertTrue(codeSelects.size() == 5);
   }

   @Test
public void testFindByUtilisateurAndBanque(){
      Utilisateur u = utilisateurDao.findById(1);
      Banque b = banqueDao.findById(1);
      List<CodeSelect> codeSelects = codeSelectDao.findByUtilisateurAndBanque(u, b);
      assertTrue(codeSelects.size() == 3);
      u = utilisateurDao.findById(2);
      codeSelects = codeSelectDao.findByUtilisateurAndBanque(u, b);
      assertTrue(codeSelects.size() == 0);
      b = banqueDao.findById(2);
      codeSelects = codeSelectDao.findByUtilisateurAndBanque(u, b);
      assertTrue(codeSelects.size() == 1);
   }

   @Test
public void testFindByBanque(){
      Banque b = banqueDao.findById(1);
      List<CodeSelect> codeSelects = codeSelectDao.findByBanque(b);
      assertTrue(codeSelects.size() == 3);
      b = banqueDao.findById(2);
      codeSelects = codeSelectDao.findByBanque(b);
      assertTrue(codeSelects.size() == 1);
      b = banqueDao.findById(4);
      codeSelects = codeSelectDao.findByBanque(b);
      assertTrue(codeSelects.size() == 0);
   }

   @Test
public void testFindByCodeDossier(){
      CodeDossier dos = codeDossierDao.findById(3);
      List<CodeSelect> codes = codeSelectDao.findByCodeDossier(dos);
      assertTrue(codes.size() == 2);
      dos = codeDossierDao.findById(1);
      codes = codeSelectDao.findByCodeDossier(dos);
      assertTrue(codes.size() == 0);
   }

   @Test
public void testFindByRootDossier(){
      final Banque b = banqueDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(1);
      final List<CodeSelect> codes = codeSelectDao.findByRootDossier(u, b);
      assertTrue(codes.size() == 1);
   }

   @Test
public void findByExcludedId(){
      final CodeSelect c = codeSelectDao.findById(1);
      List<CodeSelect> codes = codeSelectDao.findByExcludedId(c.getCodeSelectId());
      assertTrue(codes.size() == 4);
      codes = codeSelectDao.findByExcludedId(8);
      assertTrue(codes.size() == 5);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un code select.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   @Test
public void testCrud() throws Exception{
      final CodeSelect c = new CodeSelect();

      final Utilisateur u = utilisateurDao.findById(2);
      final Banque b = banqueDao.findById(1);
      final TableCodage tc = tableCodesDao.findById(3);
      final CodeDossier dos = codeDossierDao.findById(3);
      c.setUtilisateur(u);
      c.setBanque(b);
      c.setTableCodage(tc);
      c.setCodeId(5);
      c.setCodeDossier(dos);
      // Test de l'insertion
      codeSelectDao.save(c);
      assertEquals(new Integer(6), c.getCodeSelectId());

      // Test de la mise à jour
      final CodeSelect c2 = codeSelectDao.findById(new Integer(6));
      assertNotNull(c2);
      assertTrue(c2.getCodeId() == 5);
      assertNotNull(c2.getBanque());
      assertNotNull(c2.getUtilisateur());
      assertNotNull(c2.getTableCodage());
      c2.setCodeId(1);
      codeSelectDao.save(c2);
      assertTrue(codeSelectDao.findById(new Integer(6)).getCodeId() == 1);

      // Test de la délétion
      codeSelectDao.deleteById(new Integer(6));
      assertFalse(codeSelectDao.findById(new Integer(6)).isPresent());

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final Integer id1 = 1;
      final Integer id2 = 2;
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Utilisateur u2 = utilisateurDao.findById(2);
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final TableCodage t1 = tableCodesDao.findById(1);
      final TableCodage t2 = tableCodesDao.findById(2);
      final CodeSelect c1 = new CodeSelect();
      final CodeSelect c2 = new CodeSelect();

      // L'objet 1 n'est pas égal à null
      assertFalse(c1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(c1.equals(c1));

      /*null*/
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      /*Code id*/
      c2.setCodeId(id1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setCodeId(id2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setCodeId(id1);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      /*Utilisateur (code ids etant egaux)*/
      c2.setUtilisateur(u1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setUtilisateur(u2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setUtilisateur(u1);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      /*Banque (les premieres props etant egales)*/
      c2.setBanque(b1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setBanque(b2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setBanque(b1);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      /*Table (les premieres props etant egales)*/
      c2.setTableCodage(t1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setTableCodage(t2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setTableCodage(t1);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      final Categorie c3 = new Categorie();
      assertFalse(c1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){

      final Utilisateur u = utilisateurDao.findById(2);
      final Banque b = banqueDao.findById(1);
      final TableCodage tc = tableCodesDao.findById(3);
      final CodeSelect c1 = new CodeSelect();
      c1.setUtilisateur(u);
      c1.setBanque(b);
      c1.setTableCodage(tc);
      c1.setCodeId(5);
      final CodeSelect c2 = new CodeSelect();
      c2.setUtilisateur(u);
      c2.setBanque(b);
      c2.setTableCodage(tc);
      c2.setCodeId(5);
      final CodeSelect c3 = new CodeSelect();
      c3.setUtilisateur(null);
      c3.setBanque(null);
      c3.setTableCodage(null);
      c3.setCodeId(null);
      assertTrue(c3.hashCode() > 0);

      final int hash = c1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(c1.hashCode() == c2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());

   }

   @Test
public void testToString(){
      final CodeSelect a = new CodeSelect();
      final TableCodage t = tableCodesDao.findById(1);
      a.setTableCodage(t);
      a.setCodeId(33);
      assertTrue(a.toString().equals("{CodeSelect: ADICAP.33}"));
   }

   @Test
public void testFindByRootDossierAndBanque(){
      Banque b = banqueDao.findById(1);
      List<CodeSelect> codes = codeSelectDao.findByRootDossierAndBanque(b);
      assertTrue(codes.size() == 1);
      b = banqueDao.findById(2);
      codes = codeSelectDao.findByRootDossierAndBanque(b);
      assertTrue(codes.size() == 1);
      codes = codeSelectDao.findByRootDossierAndBanque(null);
      assertTrue(codes.size() == 0);
   }

}
