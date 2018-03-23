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
package fr.aphp.tumorotek.dao.test.systeme;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.NumerotationDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Numerotation;

public class NumerotationDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private NumerotationDao numerotationDao;
   private BanqueDao banqueDao;
   private EntiteDao entiteDao;

   public NumerotationDaoTest(){

   }

   public void setNumerotationDao(final NumerotationDao nDao){
      this.numerotationDao = nDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAlls(){
      final List<Numerotation> liste = numerotationDao.findAll();
      assertTrue(liste.size() == 3);
   }

   public void testFindByBanques(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = banqueDao.findById(3);
      List<Banque> banques = new ArrayList<>();
      banques.add(b1);
      banques.add(b2);

      List<Numerotation> liste = numerotationDao.findByBanques(banques);
      assertTrue(liste.size() == 3);
      assertTrue(liste.get(0).getCodeFormula().equals("PRLVT[]"));

      banques = new ArrayList<>();
      banques.add(b3);
      liste = numerotationDao.findByBanques(banques);
      assertTrue(liste.size() == 0);

      liste = numerotationDao.findByBanques(null);
      assertTrue(liste.size() == 0);
   }

   public void testFindByBanqueAndEntite(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);

      List<Numerotation> liste = numerotationDao.findByBanqueAndEntite(b1, e2);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).getCodeFormula().equals("PRLVT[]"));

      liste = numerotationDao.findByBanqueAndEntite(b2, e2);
      assertTrue(liste.size() == 0);

      liste = numerotationDao.findByBanqueAndEntite(b1, e1);
      assertTrue(liste.size() == 0);

      liste = numerotationDao.findByBanqueAndEntite(null, e1);
      assertTrue(liste.size() == 0);

      liste = numerotationDao.findByBanqueAndEntite(b1, null);
      assertTrue(liste.size() == 0);

      liste = numerotationDao.findByBanqueAndEntite(null, null);
      assertTrue(liste.size() == 0);
   }

   public void testFindByBanqueSelectEntite(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = banqueDao.findById(3);

      List<Entite> liste = numerotationDao.findByBanqueSelectEntite(b1);
      assertTrue(liste.size() == 2);

      liste = numerotationDao.findByBanqueSelectEntite(b2);
      assertTrue(liste.size() == 1);

      liste = numerotationDao.findByBanqueSelectEntite(b3);
      assertTrue(liste.size() == 0);

      liste = numerotationDao.findByBanqueSelectEntite(null);
      assertTrue(liste.size() == 0);
   }

   public void testFindByExcludedId(){
      List<Numerotation> liste = numerotationDao.findByExcludedId(2);
      assertTrue(liste.size() == 2);

      liste = numerotationDao.findByExcludedId(25);
      assertTrue(liste.size() == 3);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'une Numerotation.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   public void testCrud() throws Exception{

      final Numerotation num = new Numerotation();
      final Banque b = banqueDao.findById(1);
      final Entite e = entiteDao.findById(1);

      final String code1 = "XXX[]";
      final String code2 = "XXX[]XXX";

      num.setBanque(b);
      num.setEntite(e);
      num.setCodeFormula(code1);
      num.setCurrentIncrement(5);
      num.setStartIncrement(1);
      num.setNbChiffres(5);
      num.setZeroFill(true);

      // Test de l'insertion
      numerotationDao.createObject(num);
      assertEquals(new Integer(4), num.getNumerotationId());

      // Test de la mise à jour
      final Numerotation num2 = numerotationDao.findById(new Integer(4));
      assertNotNull(num2);
      assertNotNull(num2.getBanque());
      assertNotNull(num2.getEntite());
      assertTrue(num2.getCodeFormula().equals(code1));
      assertTrue(num2.getCurrentIncrement() == 5);
      assertTrue(num2.getStartIncrement() == 1);
      assertTrue(num2.getNbChiffres() == 5);
      assertTrue(num2.getZeroFill());
      num2.setCodeFormula(code2);
      numerotationDao.updateObject(num2);
      assertTrue(numerotationDao.findById(new Integer(4)).getCodeFormula().equals(code2));

      // Test de la délétion
      numerotationDao.removeObject(new Integer(4));
      assertNull(numerotationDao.findById(new Integer(4)));
   }

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   public void testEquals() throws ParseException{
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final Numerotation n1 = new Numerotation();
      final Numerotation n2 = new Numerotation();

      // L'objet 1 n'est pas égal à null
      assertFalse(n1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(n1.equals(n1));

      /*null*/
      assertTrue(n1.equals(n2));
      assertTrue(n2.equals(n1));

      /*Banque*/
      n2.setBanque(b1);
      assertFalse(n1.equals(n2));
      assertFalse(n2.equals(n1));
      n1.setBanque(b2);
      assertFalse(n1.equals(n2));
      assertFalse(n2.equals(n1));
      n1.setBanque(b1);
      assertTrue(n1.equals(n2));
      assertTrue(n2.equals(n1));

      /*Prenom*/
      n2.setEntite(e1);
      assertFalse(n1.equals(n2));
      assertFalse(n2.equals(n1));
      n1.setEntite(e2);
      assertFalse(n1.equals(n2));
      assertFalse(n2.equals(n1));
      n1.setEntite(e1);
      assertTrue(n1.equals(n2));

      final Categorie c3 = new Categorie();
      assertFalse(n1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    * @throws ParseException 
    */
   public void testHashCode() throws ParseException{
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final Numerotation n1 = new Numerotation();
      final Numerotation n2 = new Numerotation();

      /*null*/
      assertTrue(n1.hashCode() == n2.hashCode());

      /*Nom*/
      n2.setBanque(b1);
      assertFalse(n1.hashCode() == n2.hashCode());
      n1.setBanque(b2);
      assertFalse(n1.hashCode() == n2.hashCode());
      n1.setBanque(b1);
      assertTrue(n1.hashCode() == n2.hashCode());

      /*Prenom*/
      n2.setEntite(e1);
      assertFalse(n1.hashCode() == n2.hashCode());
      n1.setEntite(e2);
      assertFalse(n1.hashCode() == n2.hashCode());
      n1.setEntite(e1);
      assertTrue(n1.hashCode() == n2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = n1.hashCode();
      assertTrue(hash == n1.hashCode());
      assertTrue(hash == n1.hashCode());
      assertTrue(hash == n1.hashCode());
      assertTrue(hash == n1.hashCode());
   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final Numerotation n1 = numerotationDao.findById(1);
      assertTrue(n1.toString().equals("{" + n1.getCodeFormula() + "}"));

      final Numerotation n2 = new Numerotation();
      assertTrue(n2.toString().equals("{Empty Numerotation}"));
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){
      final Numerotation num1 = numerotationDao.findById(1);
      Numerotation num2 = new Numerotation();
      num2 = num1.clone();

      assertTrue(num1.equals(num2));

      if(num1.getNumerotationId() != null){
         assertTrue(num1.getNumerotationId() == num2.getNumerotationId());
      }else{
         assertNull(num2.getNumerotationId());
      }

      if(num1.getBanque() != null){
         assertTrue(num1.getBanque().equals(num2.getBanque()));
      }else{
         assertNull(num2.getBanque());
      }

      if(num1.getEntite() != null){
         assertTrue(num1.getEntite().equals(num2.getEntite()));
      }else{
         assertNull(num2.getEntite());
      }

      if(num1.getCodeFormula() != null){
         assertTrue(num1.getCodeFormula().equals(num2.getCodeFormula()));
      }else{
         assertNull(num2.getCodeFormula());
      }

      if(num1.getCurrentIncrement() != null){
         assertTrue(num1.getCurrentIncrement() == num2.getCurrentIncrement());
      }else{
         assertNull(num2.getCurrentIncrement());
      }

      if(num1.getStartIncrement() != null){
         assertTrue(num1.getStartIncrement() == num2.getStartIncrement());
      }else{
         assertNull(num2.getStartIncrement());
      }

      if(num1.getNbChiffres() != null){
         assertTrue(num1.getNbChiffres() == num2.getNbChiffres());
      }else{
         assertNull(num2.getNbChiffres());
      }

      if(num1.getZeroFill() != null){
         assertTrue(num1.getZeroFill().equals(num2.getZeroFill()));
      }else{
         assertNull(num2.getZeroFill());
      }

   }

}
