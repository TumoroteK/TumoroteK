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
package fr.aphp.tumorotek.dao.test.echantillon;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonTypeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le DAO EchantillonTypeDao et le bean
 * du domaine EchantillonType.
 *
 * @author Pierre Ventadour.
 * @version 10/09/2009
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class EchantillonTypeDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private EchantillonTypeDao echantillonTypeDao;
   private PlateformeDao plateformeDao;

   private final String updatedType = "Type mis a jour";

   /** Constructeur. */
   public EchantillonTypeDaoTest(){

   }

   /**
    * Setter du bean EchantillonTypeDao.
    * @param eDao est le bean Dao.
    */
   public void setEchantillonTypeDao(final EchantillonTypeDao eDao){
      this.echantillonTypeDao = eDao;
   }

   public void setPlateformeDao(final PlateformeDao pfDao){
      this.plateformeDao = pfDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllObjetTypes(){
      final List<EchantillonType> types = echantillonTypeDao.findAll();
      assertTrue(types.size() == 4);
   }

   public void testFindByOrder(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = echantillonTypeDao.findByOrder(pf);
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("ADN"));
      pf = plateformeDao.findById(2);
      list = echantillonTypeDao.findByOrder(pf);
      assertTrue(list.size() == 1);
      list = echantillonTypeDao.findByOrder(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByType().
    */
   public void testFindByType(){
      List<EchantillonType> types = echantillonTypeDao.findByType("ADN");
      assertTrue(types.size() == 1);
      types = echantillonTypeDao.findByType("ARN");
      assertTrue(types.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByIncaCat().
    */
   public void testFindByIncaCat(){
      List<EchantillonType> types = echantillonTypeDao.findByIncaCat("CAT1");
      assertTrue(types.size() == 1);
      types = echantillonTypeDao.findByIncaCat("CAT");
      assertTrue(types.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEchantillonId().
    */
   public void testFindByEchantillonId(){
      List<EchantillonType> types = echantillonTypeDao.findByEchantillonId(1);
      assertTrue(types.size() == 1);
      assertTrue(types.get(0).getEchantillonTypeId() == 1);
      types = echantillonTypeDao.findByEchantillonId(3);
      assertTrue(types.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<EchantillonType> liste = echantillonTypeDao.findByExcludedId(1);
      assertTrue(liste.size() == 3);
      final EchantillonType type = liste.get(0);
      assertNotNull(type);
      assertTrue(type.getEchantillonTypeId() == 2);

      liste = echantillonTypeDao.findByExcludedId(15);
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un type 
    * d'échantillon.
    * @throws Exception Lance une exception en cas d'eereur.
    */
   @Rollback(false)
   public void testCrudEchanType() throws Exception{

      final EchantillonType e = new EchantillonType();

      e.setType("TYPE");
      e.setIncaCat("CAT10");
      e.setPlateforme(plateformeDao.findById(1));
      // Test de l'insertion
      echantillonTypeDao.createObject(e);
      assertEquals(new Integer(5), e.getEchantillonTypeId());

      // Test de la mise à jour
      final EchantillonType e2 = echantillonTypeDao.findById(new Integer(5));
      assertNotNull(e2);
      assertTrue(e2.getType().equals("TYPE"));
      assertTrue(e2.getIncaCat().equals("CAT10"));
      e2.setType(updatedType);
      echantillonTypeDao.updateObject(e2);
      assertTrue(echantillonTypeDao.findById(new Integer(5)).getType().equals(updatedType));

      // Test de la délétion
      echantillonTypeDao.removeObject(new Integer(5));
      assertNull(echantillonTypeDao.findById(new Integer(5)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String type = "Type1";
      final String type2 = "Type2";
      final String inca = "INCA1";
      final String inca2 = "INCA2";
      final EchantillonType e1 = new EchantillonType();
      e1.setType(type);
      e1.setIncaCat(inca);
      final EchantillonType e2 = new EchantillonType();
      e2.setType(type);
      e2.setIncaCat(inca);

      // L'objet 1 n'est pas égal à null
      assertFalse(e1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(e1.equals(e1));
      // 2 objets sont égaux entre eux
      assertTrue(e1.equals(e2));
      assertTrue(e2.equals(e1));

      e1.setType(null);
      e1.setIncaCat(null);
      e2.setType(null);
      e2.setIncaCat(null);
      assertTrue(e1.equals(e2));
      e2.setIncaCat(inca);
      assertFalse(e1.equals(e2));
      e1.setIncaCat(inca);
      assertTrue(e1.equals(e2));
      e2.setIncaCat(inca2);
      assertFalse(e1.equals(e2));
      e2.setIncaCat(null);
      assertFalse(e1.equals(e2));
      e2.setType(type);
      assertFalse(e1.equals(e2));

      e1.setType(type);
      e1.setIncaCat(null);
      e2.setIncaCat(null);
      e2.setType(type);
      assertTrue(e1.equals(e2));
      e2.setType(type2);
      assertFalse(e1.equals(e2));
      e2.setIncaCat(inca);
      assertFalse(e1.equals(e2));

      // Vérification de la différenciation de 2 objets
      e2.setType(type2);
      assertFalse(e1.equals(e2));
      assertFalse(e2.equals(e1));
      e2.setType(type);
      e2.setIncaCat(inca2);
      assertFalse(e1.equals(e2));
      assertFalse(e2.equals(e1));

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      e1.setType(e2.getType());
      e1.setIncaCat(e2.getIncaCat());
      e1.setPlateforme(pf1);
      e2.setPlateforme(pf1);
      assertTrue(e1.equals(e2));
      e2.setPlateforme(pf2);
      assertFalse(e1.equals(e2));

      final Categorie c = new Categorie();
      assertFalse(e1.equals(c));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String type = "Type1";
      final String inca = "INCA1";
      final EchantillonType e1 = new EchantillonType(1, type, inca);
      final EchantillonType e2 = new EchantillonType(2, type, inca);
      final EchantillonType e3 = new EchantillonType(3, null, null);
      assertTrue(e3.hashCode() > 0);

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      e1.setPlateforme(pf1);
      e2.setPlateforme(pf1);
      e3.setPlateforme(pf2);

      final int hash = e1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(e1.hashCode() == e2.hashCode());
      assertFalse(e1.hashCode() == e3.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());
   }

   /**
    * Test toString().
    */
   public void testToString(){
      final EchantillonType type1 = echantillonTypeDao.findById(1);
      assertTrue(type1.toString().equals("{" + type1.getType() + "}"));

      final EchantillonType type2 = new EchantillonType();
      assertTrue(type2.toString().equals("{Empty EchantillonType}"));
   }

}
