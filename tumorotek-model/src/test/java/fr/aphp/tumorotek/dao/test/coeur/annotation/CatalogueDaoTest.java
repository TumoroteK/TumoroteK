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
package fr.aphp.tumorotek.dao.test.coeur.annotation;

import java.util.List;

import fr.aphp.tumorotek.dao.annotation.CatalogueDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 *
 * Classe de test pour le DAO CatalogueDao et le
 * bean du domaine Catalogue.
 * Classe de test créée le 18/03/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class CatalogueDaoTest extends AbstractDaoTest
{

   @Autowired
 CatalogueDao catalogueDao;
   @Autowired
 BanqueDao banqueDao;

   /**
    * Constructeur.
    */
   public CatalogueDaoTest(){}

   @Test
public void setCatalogueDao(final CatalogueDao cDao){
      this.catalogueDao = cDao;
   }

   @Test
public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   @Test
public void testFindNoms(){
      final List<String> noms = catalogueDao.findNoms();
      assertTrue(noms.size() == 4);
      assertTrue(noms.get(1).equals("INCa"));
      assertTrue(noms.get(2).equals("INCa-Tabac"));
      assertTrue(noms.get(3).equals("TVGSO"));
   }

   @Test
public void testToString(){
      Catalogue c1 = catalogueDao.findById(1);
      assertTrue(c1.toString().equals("{" + c1.getNom() + "}"));

      c1 = new Catalogue();
      assertTrue(c1.toString().equals("{Empty Catalogue}"));
   }

   @Test
public void testFindByAssignedBanque(){
      final Banque b1 = banqueDao.findById(1);
      List<Catalogue> catas = catalogueDao.findByAssignedBanque(b1);
      assertTrue(catas.size() == 2);
      assertTrue(catas.get(0).getNom().equals("INCa"));
      final Banque b2 = banqueDao.findById(2);
      catas = catalogueDao.findByAssignedBanque(b2);
      assertTrue(catas.size() == 1);
      assertTrue(catas.get(0).getNom().equals("INCa"));
      final Banque b4 = banqueDao.findById(4);
      catas = catalogueDao.findByAssignedBanque(b4);
      assertTrue(catas.size() == 0);
   }

   /**
    * Test des méthodes surchargées "equals" et hashcode.
    */
   @Test
public void testEqualsAndHashCode(){
      final Catalogue c1 = new Catalogue();
      final Catalogue c2 = new Catalogue();
      assertFalse(c1.equals(null));
      assertNotNull(c2);
      assertTrue(c1.equals(c1));
      assertTrue(c1.equals(c2));
      assertTrue(c1.hashCode() == c2.hashCode());

      final String s1 = "cata1";
      final String s2 = "cata2";
      final String s3 = new String("cata2");

      c1.setNom(s1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setNom(s2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setNom(s2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setNom(s3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      // dummy
      final Categorie c = new Categorie();
      assertFalse(c1.equals(c));
   }
}
