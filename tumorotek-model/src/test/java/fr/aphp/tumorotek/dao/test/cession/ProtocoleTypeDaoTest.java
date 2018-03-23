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
package fr.aphp.tumorotek.dao.test.cession;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.cession.ProtocoleTypeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.cession.ProtocoleType;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le DAO ProtocoleTypeDao et le bean
 * du domaine ProtocoleType.
 *
 * @author Pierre Ventadour.
 * @version 25/01/2010
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ProtocoleTypeDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private ProtocoleTypeDao protocoleTypeDao;
   private PlateformeDao plateformeDao;

   private final String updatedType = "Mis a jour";

   /** Constructeur. */
   public ProtocoleTypeDaoTest(){

   }

   public void setProtocoleTypeDao(final ProtocoleTypeDao pDao){
      this.protocoleTypeDao = pDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllProtocoleTypes(){
      final List<ProtocoleType> liste = protocoleTypeDao.findAll();
      assertTrue(liste.size() == 2);
   }

   public void testFindByOrder(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = protocoleTypeDao.findByOrder(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getNom().equals("RECHERCHE"));
      pf = plateformeDao.findById(2);
      list = protocoleTypeDao.findByOrder(pf);
      assertTrue(list.size() == 0);
      list = protocoleTypeDao.findByOrder(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByType().
    */
   public void testFindByType(){
      List<ProtocoleType> liste = protocoleTypeDao.findByType("RECHERCHE");
      assertTrue(liste.size() == 1);

      liste = protocoleTypeDao.findByType("REC");
      assertTrue(liste.size() == 0);

      liste = protocoleTypeDao.findByType("REC%");
      assertTrue(liste.size() == 1);

      liste = protocoleTypeDao.findByType(null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<ProtocoleType> liste = protocoleTypeDao.findByExcludedId(1);
      assertTrue(liste.size() == 1);
      final ProtocoleType type = liste.get(0);
      assertNotNull(type);
      assertTrue(type.getProtocoleTypeId() == 2);

      liste = protocoleTypeDao.findByExcludedId(15);
      assertTrue(liste.size() == 2);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un ProtocoleType.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   public void testCrudProtocoleType() throws Exception{

      final ProtocoleType pt = new ProtocoleType();
      pt.setPlateforme(plateformeDao.findById(1));
      pt.setType("TEST");
      // Test de l'insertion
      protocoleTypeDao.createObject(pt);
      assertEquals(new Integer(3), pt.getProtocoleTypeId());

      // Test de la mise à jour
      final ProtocoleType pt2 = protocoleTypeDao.findById(new Integer(3));
      assertNotNull(pt2);
      assertTrue(pt2.getType().equals("TEST"));
      pt2.setType(updatedType);
      protocoleTypeDao.updateObject(pt2);
      assertTrue(protocoleTypeDao.findById(new Integer(3)).getType().equals(updatedType));

      // Test de la délétion
      protocoleTypeDao.removeObject(new Integer(3));
      assertNull(protocoleTypeDao.findById(new Integer(3)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String type = "TYPE";
      final String type2 = "TYPE2";
      final ProtocoleType pt1 = new ProtocoleType();
      pt1.setType(type);
      final ProtocoleType pt2 = new ProtocoleType();
      pt2.setType(type);

      // L'objet 1 n'est pas égal à null
      assertFalse(pt1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(pt1.equals(pt1));
      // 2 objets sont égaux entre eux
      assertTrue(pt1.equals(pt2));
      assertTrue(pt2.equals(pt1));

      // Vérification de la différenciation de 2 objets
      pt2.setType(type2);
      assertFalse(pt1.equals(pt2));
      assertFalse(pt2.equals(pt1));

      pt2.setType(null);
      assertFalse(pt1.equals(pt2));
      assertFalse(pt2.equals(pt1));

      pt1.setType(null);
      assertTrue(pt1.equals(pt2));
      pt2.setType(type);
      assertFalse(pt1.equals(pt2));

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      pt1.setType(pt2.getType());
      pt1.setPlateforme(pf1);
      pt2.setPlateforme(pf1);
      assertTrue(pt1.equals(pt2));
      pt2.setPlateforme(pf2);
      assertFalse(pt1.equals(pt2));

      final Categorie c = new Categorie();
      assertFalse(pt1.equals(c));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String type = "TYPE";
      final ProtocoleType pt1 = new ProtocoleType();
      pt1.setType(type);
      final ProtocoleType pt2 = new ProtocoleType();
      pt2.setType(type);
      final ProtocoleType pt3 = new ProtocoleType();
      pt3.setType(null);
      assertTrue(pt3.hashCode() > 0);

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      pt1.setPlateforme(pf1);
      pt2.setPlateforme(pf1);
      pt3.setPlateforme(pf2);

      final int hash = pt1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(pt1.hashCode() == pt2.hashCode());
      assertFalse(pt1.hashCode() == pt3.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == pt1.hashCode());
      assertTrue(hash == pt1.hashCode());
      assertTrue(hash == pt1.hashCode());
      assertTrue(hash == pt1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final ProtocoleType pt1 = protocoleTypeDao.findById(1);
      assertTrue(pt1.toString().equals("{" + pt1.getType() + "}"));

      final ProtocoleType pt2 = new ProtocoleType();
      assertTrue(pt2.toString().equals("{Empty ProtocoleType}"));
   }

}
