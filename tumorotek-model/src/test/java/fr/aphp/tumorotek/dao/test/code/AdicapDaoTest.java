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

import fr.aphp.tumorotek.dao.code.AdicapDao;
import fr.aphp.tumorotek.dao.code.AdicapGroupeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.AdicapGroupe;
import fr.aphp.tumorotek.model.contexte.Categorie;

public class AdicapDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private AdicapDao adicapDao;
   private AdicapGroupeDao adicapGroupeDao;

   /**
    * Constructeur.
    */
   public AdicapDaoTest(){}

   @Override
   protected String[] getConfigLocations(){
      return new String[] {"applicationContextDao-codes-test-mysql.xml"};
   }

   /**
    * Setter du bean Dao.
    * @param aDao est le bean Dao.
    */
   public void setAdicapDao(final AdicapDao aDao){
      this.adicapDao = aDao;
   }

   public void setAdicapGroupeDao(final AdicapGroupeDao agDao){
      this.adicapGroupeDao = agDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllAdicaps(){
      final List<Adicap> adicaps = adicapDao.findAll();
      //assertTrue(!adicaps.isEmpty());
      assertEquals(8906, adicaps.size()); // JDI : 8906 dans ma table, est-ce grave?
   }

   public void testFindByCode(){
      List<Adicap> adicaps = adicapDao.findByCodeLike("%AF%");
      assertTrue(adicaps.size() == 11);
      adicaps = adicapDao.findByCodeLike("BN");
      assertTrue(adicaps.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByLibelle().
    */
   public void testFindByLibelle(){
      List<Adicap> adicaps = adicapDao.findByLibelleLike("%ADENOSE%");
      assertTrue(adicaps.size() == 6);
      adicaps = adicapDao.findByLibelleLike("PEARL");
      assertTrue(adicaps.size() == 0);
   }

   public void testFindByAdicapGroupe(){
      final AdicapGroupe d1 = adicapGroupeDao.findById(1);
      List<Adicap> adicaps = adicapDao.findByAdicapGroupeNullParent(d1);
      assertTrue(adicaps.size() == 19);
      final AdicapGroupe d3 = adicapGroupeDao.findById(3);
      adicaps = adicapDao.findByAdicapGroupeNullParent(d3);
      assertTrue(adicaps.size() == 18);
      // teste le  parent = null
      final AdicapGroupe g43 = adicapGroupeDao.findById(43);
      adicaps = adicapDao.findByAdicapGroupeNullParent(g43);
      assertTrue(adicaps.size() == 7); // 10 dans le groupe
      final AdicapGroupe nullGroupe = adicapGroupeDao.findById(400);
      adicaps = adicapDao.findByAdicapGroupeNullParent(nullGroupe);
      assertTrue(adicaps.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByMorpho().
    */
   public void testFindByMorpho(){
      List<Adicap> adicaps = adicapDao.findByMorpho(true);
      //assertTrue(!adicaps.isEmpty());
      assertEquals(1448, adicaps.size()); // JDI : 1448 dans ma table, est-ce grave?
      adicaps = adicapDao.findByMorpho(false);
      assertTrue(adicaps.size() == 0);
   }

   public void testFindByTopoParent(){
      final Adicap parent = adicapDao.findById(42);
      List<Adicap> adicaps = adicapDao.findByAdicapParentAndCodeOrLibelle(parent, "%");
      assertTrue(adicaps.size() == 8);
      assertTrue(adicapDao.findByAdicapParentAndCodeOrLibelle(adicaps.get(7), "%").size() == 6);
      assertTrue(adicapDao.findByAdicapParentAndCodeOrLibelle(adicaps.get(7), "").isEmpty());
      adicaps = adicapDao.findByAdicapParentAndCodeOrLibelle(parent, "PHARYNX");
      assertTrue(adicaps.size() == 1);
      adicaps = adicapDao.findByAdicapParentAndCodeOrLibelle(parent, "%PHARYNX%");
      assertTrue(adicaps.size() == 4);
      final Adicap parent2 = adicapDao.findById(2);
      adicaps = adicapDao.findByAdicapParentAndCodeOrLibelle(parent2, "%");
      assertTrue(adicaps.size() == 0);
      assertTrue(adicapDao.findByAdicapParentAndCodeOrLibelle(null, "%").isEmpty());
      assertTrue(adicapDao.findByAdicapParentAndCodeOrLibelle(parent, null).isEmpty());
   }

   public void testFindByAdicapGroupeAndCodeOrLibelle(){
      List<Adicap> adicaps = adicapDao.findByAdicapGroupeAndCodeOrLibelle(adicapGroupeDao.findById(2), "%");
      assertTrue(adicaps.size() == 22);
      adicaps = adicapDao.findByAdicapGroupeAndCodeOrLibelle(adicapGroupeDao.findById(2), "%CYTO%");
      assertTrue(adicaps.size() == 4);
      adicaps = adicapDao.findByAdicapGroupeAndCodeOrLibelle(adicapGroupeDao.findById(2), "K");
      assertTrue(adicaps.size() == 1);
      assertTrue(adicapDao.findByAdicapGroupeAndCodeOrLibelle(adicapGroupeDao.findById(2), "").isEmpty());
      assertTrue(adicapDao.findByAdicapGroupeAndCodeOrLibelle(null, "").isEmpty());
      assertTrue(adicapDao.findByAdicapGroupeAndCodeOrLibelle(adicapGroupeDao.findById(2), null).isEmpty());

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final Integer id1 = 1;
      final Integer id2 = 2;
      final Adicap a1 = new Adicap();
      final Adicap a2 = new Adicap();

      // L'objet 1 n'est pas égal à null
      assertFalse(a1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(a1.equals(a1));

      /*null --> Ids ne pouvant etre nuls car table systemes*/
      assertFalse(a1.equals(a2));
      assertFalse(a2.equals(a1));

      /*Id*/
      a2.setAdicapId(id1);
      assertFalse(a1.equals(a2));
      assertFalse(a2.equals(a1));
      a1.setAdicapId(id2);
      assertFalse(a1.equals(a2));
      assertFalse(a2.equals(a1));
      a1.setAdicapId(id1);
      assertTrue(a1.equals(a2));
      assertTrue(a2.equals(a1));

      final Categorie c = new Categorie();
      assertFalse(a1.equals(c));

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){

      final Integer id1 = 1;
      final Adicap a1 = new Adicap();
      a1.setAdicapId(id1);
      final Adicap a2 = new Adicap();
      a2.setAdicapId(id1);
      final Adicap a3 = new Adicap();
      a3.setAdicapId(null);
      assertTrue(a3.hashCode() > 0);

      final int hash = a1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(a1.hashCode() == a2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == a1.hashCode());
      assertTrue(hash == a1.hashCode());
      assertTrue(hash == a1.hashCode());
      assertTrue(hash == a1.hashCode());

   }

   public void testToString(){
      final Adicap a = new Adicap();
      a.setCode("Disease");
      assertTrue(a.toString().equals("{Adicap: Disease}"));
   }

   public void testFindByDicoAndCodeOrLibelle(){
      AdicapGroupe g = adicapGroupeDao.findById(6);
      List<Adicap> adicaps = adicapDao.findByDicoAndCodeOrLibelle(g, "BD0%");
      assertTrue(adicaps.size() == 37);
      g = adicapGroupeDao.findById(3);
      adicaps = adicapDao.findByDicoAndCodeOrLibelle(g, "BD0%");
      assertTrue(adicaps.size() == 0);
      adicaps = adicapDao.findByDicoAndCodeOrLibelle(g, "LANGUE");
      assertTrue(adicaps.size() == 1);
      adicaps = adicapDao.findByDicoAndCodeOrLibelle(g, null);
      assertTrue(adicaps.size() == 0);
      adicaps = adicapDao.findByDicoAndCodeOrLibelle(null, "BD0%");
      assertTrue(adicaps.size() == 0);
   }
}
