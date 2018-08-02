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
package fr.aphp.tumorotek.dao.test.stockage;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.stockage.TerminaleNumerotationDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.stockage.TerminaleNumerotation;

public class TerminaleNumerotationDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private TerminaleNumerotationDao terminaleNumerotationDao;
   /** valeur du nom pour la maj. */
   private final String updated = "UP";

   /** Constructeur. */
   public TerminaleNumerotationDaoTest(){

   }

   public void setTerminaleNumerotationDao(final TerminaleNumerotationDao tDao){
      this.terminaleNumerotationDao = tDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAlls(){
      final List<TerminaleNumerotation> liste = terminaleNumerotationDao.findAll();
      assertTrue(liste.size() == 5);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un CessionExamen.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   public void testCrud() throws Exception{

      final TerminaleNumerotation tn = new TerminaleNumerotation();

      tn.setLigne("CAR");
      tn.setColonne("CAR");
      // Test de l'insertion
      terminaleNumerotationDao.createObject(tn);
      assertEquals(new Integer(6), tn.getTerminaleNumerotationId());

      // Test de la mise à jour
      final TerminaleNumerotation tn2 = terminaleNumerotationDao.findById(new Integer(6));
      assertNotNull(tn2);
      assertTrue(tn2.getLigne().equals("CAR"));
      tn2.setLigne(updated);
      terminaleNumerotationDao.updateObject(tn2);
      assertTrue(terminaleNumerotationDao.findById(new Integer(6)).getLigne().equals(updated));

      // Test de la délétion
      terminaleNumerotationDao.removeObject(new Integer(6));
      assertNull(terminaleNumerotationDao.findById(new Integer(6)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String ligne = "L1";
      final String ligne2 = "L2";
      final String col1 = "C1";
      final String col2 = "C2";
      final TerminaleNumerotation tn1 = new TerminaleNumerotation();
      TerminaleNumerotation tn2 = new TerminaleNumerotation();

      // L'objet 1 n'est pas égal à null
      assertFalse(tn1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(tn1.equals(tn1));

      /*null*/
      assertTrue(tn1.equals(tn2));
      assertTrue(tn2.equals(tn1));

      /*Nom*/
      tn2.setLigne(ligne);
      assertFalse(tn1.equals(tn2));
      assertFalse(tn2.equals(tn1));
      tn1.setLigne(ligne2);
      assertFalse(tn1.equals(tn2));
      assertFalse(tn2.equals(tn1));
      tn1.setLigne(ligne);
      assertTrue(tn1.equals(tn2));
      assertTrue(tn2.equals(tn1));

      /*Etablissement*/
      tn2.setColonne(col1);
      assertFalse(tn1.equals(tn2));
      assertFalse(tn2.equals(tn1));
      tn1.setColonne(col2);
      assertFalse(tn1.equals(tn2));
      assertFalse(tn2.equals(tn1));
      tn1.setColonne(col1);
      assertTrue(tn1.equals(tn2));
      assertTrue(tn2.equals(tn1));

      final Categorie c3 = new Categorie();
      assertFalse(tn1.equals(c3));

      //teste doublons
      tn2 = terminaleNumerotationDao.findById(2);
      tn1.setLigne(tn2.getLigne());
      tn1.setColonne(tn2.getColonne());
      assertTrue(terminaleNumerotationDao.findAll().contains(tn1));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String ligne = "L1";
      final String ligne2 = "L2";
      final String col1 = "C1";
      final String col2 = "C2";
      final TerminaleNumerotation tn1 = new TerminaleNumerotation();
      final TerminaleNumerotation tn2 = new TerminaleNumerotation();

      /*null*/
      assertTrue(tn1.hashCode() == tn2.hashCode());

      /*Nom*/
      tn2.setLigne(ligne);
      assertFalse(tn1.hashCode() == tn2.hashCode());
      tn1.setLigne(ligne2);
      assertFalse(tn1.hashCode() == tn2.hashCode());
      tn1.setLigne(ligne);
      assertTrue(tn1.hashCode() == tn2.hashCode());

      /*Specialite*/
      tn2.setColonne(col1);
      assertFalse(tn1.hashCode() == tn2.hashCode());
      tn1.setColonne(col2);
      assertFalse(tn1.hashCode() == tn2.hashCode());
      tn1.setColonne(col1);
      assertTrue(tn1.hashCode() == tn2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = tn1.hashCode();
      assertTrue(hash == tn1.hashCode());
      assertTrue(hash == tn1.hashCode());
      assertTrue(hash == tn1.hashCode());
      assertTrue(hash == tn1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final TerminaleNumerotation ct1 = terminaleNumerotationDao.findById(1);
      assertTrue(ct1.toString().equals("{" + ct1.getLigne() + " " + ct1.getColonne() + "}"));

      final TerminaleNumerotation ct2 = new TerminaleNumerotation();
      assertTrue(ct2.toString().equals("{Empty TerminaleNumerotation}"));
   }

}
