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

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ProtocoleDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Protocole;

/**
 *
 * Classe de test pour le DAO ProtocoleDao et le
 * bean du domaine Protocole.
 * Classe de test créée le 22/01/12.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.6
 *
 */
public class ProtocoleDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private ProtocoleDao protocoleDao;
   private PlateformeDao plateformeDao;

   /**
    * Constructeur.
    */
   public ProtocoleDaoTest(){}

   /**
    * Setter du bean Dao.
    * @param ctDao est le bean Dao.
    */
   public void setProtocoleDao(final ProtocoleDao pDao){
      this.protocoleDao = pDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Test l'appel de la méthode toString().
    */
   public void testToString(){
      Protocole p1 = protocoleDao.findById(1);
      assertTrue(p1.toString().equals("{" + p1.getNom() + "}"));
      p1 = new Protocole();
      assertTrue(p1.toString().equals("{Empty Protocole}"));
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllProtocole(){
      final List<Protocole> types = protocoleDao.findAll();
      assertTrue(types.size() == 3);
   }

   public void testFindByOrder(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = protocoleDao.findByOrder(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getNom().equals("OFSEP"));
      pf = plateformeDao.findById(2);
      list = protocoleDao.findByOrder(pf);
      assertTrue(list.size() == 1);
      list = protocoleDao.findByOrder(null);
      assertTrue(list.size() == 0);
   }

   public void testFindByNom(){
      List<Protocole> types = protocoleDao.findByNom("TYSABRI");
      assertTrue(types.size() == 1);
      types = protocoleDao.findByNom("EDMUS");
      assertTrue(types.size() == 1);
      types = protocoleDao.findByNom("OF%");
      assertTrue(types.size() == 1);
      types = protocoleDao.findByNom(null);
      assertTrue(types.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un protcole.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudProtocole() throws Exception{
      final Protocole p = new Protocole();
      p.setNom("MELBASE");
      p.setPlateforme(plateformeDao.findById(1));
      // Test de l'insertion
      protocoleDao.createObject(p);
      assertEquals(new Integer(4), p.getProtocoleId());

      // Test de la mise à jour
      final Protocole p2 = protocoleDao.findByNom("MELBASE").get(0);
      assertNotNull(p2);
      assertTrue(p2.getNom().equals("MELBASE"));
      p2.setNom("XEOGR");
      protocoleDao.updateObject(p2);
      assertTrue(protocoleDao.findById(new Integer(4)).getNom().equals("XEOGR"));

      // Test de la délétion
      protocoleDao.removeObject(new Integer(4));
      assertNull(protocoleDao.findById(new Integer(4)));
   }

   /**
    * Test des méthodes surchargées "equals" et hashcode.
    */
   public void testEqualsAndHashCode(){
      final Protocole p1 = new Protocole();
      final Protocole p2 = new Protocole();
      assertFalse(p1.equals(null));
      assertNotNull(p2);
      assertTrue(p1.equals(p1));
      assertTrue(p1.equals(p2));
      assertTrue(p1.hashCode() == p2.hashCode());

      final String s1 = "nom1";
      final String s2 = "nom2";
      final String s3 = new String("nom2");

      p1.setNom(s1);
      assertFalse(p1.equals(p2));
      assertFalse(p2.equals(p1));
      assertTrue(p1.hashCode() != p2.hashCode());
      p2.setNom(s2);
      assertFalse(p1.equals(p2));
      assertFalse(p2.equals(p1));
      assertTrue(p1.hashCode() != p2.hashCode());
      p1.setNom(s2);
      assertTrue(p1.equals(p2));
      assertTrue(p2.equals(p1));
      assertTrue(p1.hashCode() == p2.hashCode());
      p1.setNom(s3);
      assertTrue(p1.equals(p2));
      assertTrue(p2.equals(p1));
      assertTrue(p1.hashCode() == p2.hashCode());

      final Plateforme f1 = plateformeDao.findById(1);
      final Plateforme f2 = plateformeDao.findById(2);
      final Plateforme f3 = new Plateforme();
      f3.setNom(f2.getNom());
      assertTrue(f3.equals(f2));

      p1.setPlateforme(f1);
      assertFalse(p1.equals(p2));
      assertFalse(p2.equals(p1));
      assertTrue(p1.hashCode() != p2.hashCode());
      p2.setPlateforme(f2);
      assertFalse(p1.equals(p2));
      assertFalse(p2.equals(p1));
      assertTrue(p1.hashCode() != p2.hashCode());
      p1.setPlateforme(f2);
      assertTrue(p1.equals(p2));
      assertTrue(p2.equals(p1));
      assertTrue(p1.hashCode() == p2.hashCode());
      p1.setPlateforme(f3);
      assertTrue(p1.equals(p2));
      assertTrue(p2.equals(p1));
      assertTrue(p1.hashCode() == p2.hashCode());

      // dummy
      final Categorie c = new Categorie();
      assertFalse(p1.equals(c));
   }

}
