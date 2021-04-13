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
package fr.aphp.tumorotek.dao.test.contexte;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.BanqueTableCodageDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodagePK;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 *
 * Classe de test pour le DAO BanqueTableCodageDao et le
 * bean du domaine BanqueTableCodage.
 * Classe de test créée le 22/08/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class BanqueTableCodageDaoTest extends AbstractDaoTest
{

   /** Beans Dao. */
   private BanqueTableCodageDao banqueTableCodageDao;
   private BanqueDao banqueDao;
   private TableCodageDao tableCodageDao;

   /**
    * Constructeur.
    */
   public BanqueTableCodageDaoTest(){}

   public void setBanqueTableCodageDao(final BanqueTableCodageDao b){
      this.banqueTableCodageDao = b;
   }

   public void setBanqueDao(final BanqueDao b){
      this.banqueDao = b;
   }

   public void setTableCodageDao(final TableCodageDao t){
      this.tableCodageDao = t;
   }

   public void testFindByBanque(){
      final Banque b1 = banqueDao.findById(1);
      List<BanqueTableCodage> btcs = banqueTableCodageDao.findByBanque(b1);
      assertTrue(btcs.size() == 2);
      assertTrue(btcs.get(0).getTableCodage().getNom().equals("ADICAP"));
      assertFalse(btcs.get(0).getLibelleExport());
      assertTrue(btcs.get(1).getLibelleExport());
      final Banque b2 = banqueDao.findById(2);
      btcs = banqueTableCodageDao.findByBanque(b2);
      assertTrue(btcs.size() == 1);
      assertTrue(btcs.get(0).getLibelleExport());
      final Banque b3 = banqueDao.findById(3);
      btcs = banqueTableCodageDao.findByBanque(b3);
      assertTrue(btcs.size() == 0);
   }

   /**
    * Test l'appel de la méthode toString().
    */
   public void testToString(){
      final BanqueTableCodagePK pk = new BanqueTableCodagePK(banqueDao.findById(1), tableCodageDao.findById(1));
      BanqueTableCodage btc1 = banqueTableCodageDao.findById(pk);
      assertTrue(btc1.toString().equals("{" + btc1.getBanque().getNom() + " - " + btc1.getTableCodage().getNom() + "}"));
      btc1 = new BanqueTableCodage();
      assertTrue(btc1.toString().equals("{Empty BanqueTableCodage}"));
      btc1.setBanque(banqueDao.findById(1));
      assertTrue(btc1.toString().equals("{Empty BanqueTableCodage}"));
      btc1.setBanque(null);
      btc1.setTableCodage(tableCodageDao.findById(1)); 
      assertTrue(btc1.toString().equals("ADICAP 5.03"));
   }

   public void testReadAllBanqueTableCodage(){
      final List<BanqueTableCodage> btcs = banqueTableCodageDao.findAll();
      assertTrue(btcs.size() == 3);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'une association BanqueTableCodage.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    *
    **/
   @Rollback(false)
   public void testCrud(){
      final BanqueTableCodage btc = new BanqueTableCodage();
      final Banque b2 = banqueDao.findById(2);
      final TableCodage t3 = tableCodageDao.findById(3);
      btc.setBanque(b2);
      btc.setTableCodage(t3);
      btc.setLibelleExport(true);
      // Test de l'insertion
      banqueTableCodageDao.createObject(btc);
      assertTrue(banqueTableCodageDao.findAll().size() == 4);
      // Test de la mise à jour
      final BanqueTableCodagePK pk = new BanqueTableCodagePK();
      pk.setBanque(b2);
      pk.setTableCodage(t3);
      final BanqueTableCodage btc2 = banqueTableCodageDao.findById(pk);
      assertNotNull(btc2);
      assertTrue(btc2.getBanque().equals(b2));
      assertTrue(btc2.getTableCodage().equals(t3));
      assertTrue(btc2.getLibelleExport());
      //update
      btc2.setLibelleExport(false);
      banqueTableCodageDao.updateObject(btc2);
      assertTrue(banqueTableCodageDao.findById(pk).equals(btc2));
      assertFalse(banqueTableCodageDao.findById(pk).getLibelleExport());
      // Test de la délétion
      banqueTableCodageDao.removeObject(pk);
      assertNull(banqueTableCodageDao.findById(pk));
   }

   public void testEqualsAndHashCodeTranscodeUtilisateur(){
      final BanqueTableCodage btc1 = new BanqueTableCodage();
      final BanqueTableCodage btc2 = new BanqueTableCodage();
      assertFalse(btc1.equals(null));
      assertNotNull(btc2);
      assertTrue(btc1.equals(btc1));
      assertTrue(btc1.equals(btc2));
      assertTrue(btc1.hashCode() == btc2.hashCode());

      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = new Banque();
      b3.setNom(b2.getNom());
      b3.setPlateforme(b2.getPlateforme());
      assertFalse(b1.equals(b2));
      assertFalse(b1.hashCode() == b2.hashCode());
      assertTrue(b2.equals(b3));
      btc1.setBanque(b1);
      assertFalse(btc1.equals(btc2));
      assertFalse(btc2.equals(btc1));
      assertTrue(btc1.hashCode() != btc2.hashCode());
      btc2.setBanque(b2);
      assertFalse(btc1.equals(btc2));
      assertFalse(btc2.equals(btc1));
      assertTrue(btc1.hashCode() != btc2.hashCode());
      btc1.setBanque(b3);
      assertTrue(btc1.equals(btc2));
      assertTrue(btc2.equals(btc1));
      assertTrue(btc1.hashCode() == btc2.hashCode());
      btc1.setBanque(b2);
      assertTrue(btc1.equals(btc2));
      assertTrue(btc2.equals(btc1));
      assertTrue(btc1.hashCode() == btc2.hashCode());

      final TableCodage t1 = tableCodageDao.findById(1);
      final TableCodage t2 = tableCodageDao.findById(2);
      final TableCodage t3 = new TableCodage();
      t3.setNom(t2.getNom());
      t3.setVersion(t2.getVersion());
      assertFalse(t1.equals(t2));
      assertFalse(t1.hashCode() == t2.hashCode());
      assertTrue(t2.equals(t3));
      btc1.setTableCodage(t1);
      assertFalse(btc1.equals(btc2));
      assertFalse(btc2.equals(btc1));
      assertTrue(btc1.hashCode() != btc2.hashCode());
      btc2.setTableCodage(t2);
      assertFalse(btc1.equals(btc2));
      assertFalse(btc2.equals(btc1));
      assertTrue(btc1.hashCode() != btc2.hashCode());
      btc1.setTableCodage(t3);
      assertTrue(btc1.equals(btc2));
      assertTrue(btc2.equals(btc1));
      assertTrue(btc1.hashCode() == btc2.hashCode());
      btc1.setTableCodage(t2);
      assertTrue(btc1.equals(btc2));
      assertTrue(btc2.equals(btc1));
      assertTrue(btc1.hashCode() == btc2.hashCode());

      // dummy
      final Categorie c = new Categorie();
      assertFalse(btc1.equals(c));
   }
}
