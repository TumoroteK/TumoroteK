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

import java.text.ParseException;
import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleNumerotationDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleTypeDao;
import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.stockage.TerminaleNumerotation;
import fr.aphp.tumorotek.model.stockage.TerminaleType;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le DAO TerminaleDao et le bean
 * du domaine Terminale.
 *
 * @author Pierre Ventadour.
 * @version 18/03/2010
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class TerminaleDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private TerminaleDao terminaleDao;
   /** Bean Dao. */
   private TerminaleTypeDao terminaleTypeDao;
   /** Bean Dao. */
   private TerminaleNumerotationDao terminaleNumerotationDao;
   /** Bean Dao. */
   private BanqueDao banqueDao;
   /** Bean Dao. */
   private EnceinteDao enceinteDao;
   /** Bean Dao. */
   private EntiteDao entiteDao;
   /** Bean Dao. */
   private CouleurDao couleurDao;

   public TerminaleDaoTest(){

   }

   public void setTerminaleDao(final TerminaleDao tDao){
      this.terminaleDao = tDao;
   }

   public void setTerminaleTypeDao(final TerminaleTypeDao tDao){
      this.terminaleTypeDao = tDao;
   }

   public void setTerminaleNumerotationDao(final TerminaleNumerotationDao tDao){
      this.terminaleNumerotationDao = tDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEnceinteDao(final EnceinteDao eDao){
      this.enceinteDao = eDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setCouleurDao(final CouleurDao cDao){
      this.couleurDao = cDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAll(){
      final List<Terminale> liste = terminaleDao.findAll();
      assertTrue(liste.size() == 6);
   }

   /**
    * Test l'appel de la méthode findByEnceinteWithOrder().
    */
   public void testFindByEnceinteWithOrder(){
      final Enceinte e1 = enceinteDao.findById(3);
      List<Terminale> liste = terminaleDao.findByEnceinteWithOrder(e1);
      assertTrue(liste.size() == 3);
      assertTrue(liste.get(0).getNom().equals("BT1"));

      final Enceinte e2 = enceinteDao.findById(1);
      liste = terminaleDao.findByEnceinteWithOrder(e2);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteWithOrder(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEnceinteAndPosition().
    */
   public void testFindByEnceinteAndPosition(){
      final Enceinte e1 = enceinteDao.findById(3);
      List<Terminale> liste = terminaleDao.findByEnceinteAndPosition(e1, 1);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).getNom().equals("BT1"));

      liste = terminaleDao.findByEnceinteAndPosition(e1, 10);
      assertTrue(liste.size() == 0);

      final Enceinte e2 = enceinteDao.findById(1);
      liste = terminaleDao.findByEnceinteAndPosition(e2, 1);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteAndPosition(null, 1);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteAndPosition(e1, null);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteAndPosition(null, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEnceinteAndNom().
    */
   public void testFindByEnceinteAndNom(){
      final Enceinte e1 = enceinteDao.findById(3);
      List<Terminale> liste = terminaleDao.findByEnceinteAndNom(e1, "BT1");
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).getNom().equals("BT1"));

      liste = terminaleDao.findByEnceinteAndNom(e1, "hcq");
      assertTrue(liste.size() == 0);

      final Enceinte e2 = enceinteDao.findById(1);
      liste = terminaleDao.findByEnceinteAndNom(e2, "BT1");
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteAndNom(null, "BT1");
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteAndNom(e1, null);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteAndNom(null, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEnceinteAndPositionExcludedId().
    */
   public void testFindByEnceinteAndPositionExcludedId(){
      final Enceinte e1 = enceinteDao.findById(3);
      List<Terminale> liste = terminaleDao.findByEnceinteAndPositionExcludedId(e1, 1, 10);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).getNom().equals("BT1"));

      liste = terminaleDao.findByEnceinteAndPositionExcludedId(e1, 1, 1);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteAndPositionExcludedId(e1, 10, 1);
      assertTrue(liste.size() == 0);

      final Enceinte e2 = enceinteDao.findById(1);
      liste = terminaleDao.findByEnceinteAndPositionExcludedId(e2, 1, 1);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteAndPositionExcludedId(null, 1, 1);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteAndPositionExcludedId(e1, null, 1);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteAndPositionExcludedId(null, null, 1);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByEnceinteAndPositionExcludedId(null, null, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findNumberTerminalesForEnceinte().
    */
   public void testFindNumberTerminalesForEnceinte(){
      final Enceinte e1 = enceinteDao.findById(3);
      List<Long> liste = terminaleDao.findNumberTerminalesForEnceinte(e1);
      assertTrue(liste.get(0) == 3);

      final Enceinte e2 = enceinteDao.findById(1);
      liste = terminaleDao.findNumberTerminalesForEnceinte(e2);
      assertTrue(liste.get(0) == 0);

      liste = terminaleDao.findNumberTerminalesForEnceinte(null);
      assertTrue(liste.get(0) == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedIdEnceinte().
    */
   public void testFindByExcludedIdEnceinte(){
      final Enceinte e1 = enceinteDao.findById(1);
      List<Terminale> liste = terminaleDao.findByExcludedIdEnceinte(1, e1);
      assertTrue(liste.size() == 0);

      final Enceinte e3 = enceinteDao.findById(3);
      liste = terminaleDao.findByExcludedIdEnceinte(3, e3);
      assertTrue(liste.size() == 2);

      liste = terminaleDao.findByExcludedIdEnceinte(13, e3);
      assertTrue(liste.size() == 3);

      liste = terminaleDao.findByExcludedIdEnceinte(3, null);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByExcludedIdEnceinte(null, e3);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByExcludedIdEnceinte(null, null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'appel de la méthode findByTwoExcludedIdsWithEnceinte().
    */
   public void testFindByTwoExcludedIdsWithEnceinte(){
      final Enceinte e3 = enceinteDao.findById(3);
      List<Terminale> liste = terminaleDao.findByTwoExcludedIdsWithEnceinte(4, 5, e3);
      assertTrue(liste.size() == 3);

      liste = terminaleDao.findByTwoExcludedIdsWithEnceinte(1, 2, e3);
      assertTrue(liste.size() == 1);

      liste = terminaleDao.findByTwoExcludedIdsWithEnceinte(1, 5, e3);
      assertTrue(liste.size() == 2);

      liste = terminaleDao.findByTwoExcludedIdsWithEnceinte(1, 2, null);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByTwoExcludedIdsWithEnceinte(1, null, e3);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByTwoExcludedIdsWithEnceinte(null, 2, e3);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByTwoExcludedIdsWithEnceinte(null, 2, null);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByTwoExcludedIdsWithEnceinte(1, null, null);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByTwoExcludedIdsWithEnceinte(null, null, e3);
      assertTrue(liste.size() == 0);

      liste = terminaleDao.findByTwoExcludedIdsWithEnceinte(null, null, null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'une Terminale.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   public void testCrud() throws Exception{

      final Terminale t = new Terminale();
      final Enceinte pere = enceinteDao.findById(1);
      final TerminaleType type = terminaleTypeDao.findById(1);
      final Entite ent = entiteDao.findById(3);
      final Banque b1 = banqueDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
      final String nomUp = "UP";
      final Couleur c1 = couleurDao.findById(1);

      t.setEnceinte(pere);
      t.setTerminaleType(type);
      t.setNom("NOM");
      t.setPosition(1);
      t.setAlias("ALIAS");
      t.setBanque(b1);
      t.setEntite(ent);
      t.setArchive(false);
      t.setTerminaleNumerotation(num);
      t.setCouleur(c1);

      // Test de l'insertion
      terminaleDao.createObject(t);
      assertEquals(new Integer(7), t.getTerminaleId());

      final Terminale t2 = terminaleDao.findById(new Integer(7));
      // Vérification des données entrées dans la base
      assertNotNull(t2);
      assertNotNull(t2.getEnceinte());
      assertNotNull(t2.getTerminaleNumerotation());
      assertNotNull(t2.getTerminaleType());
      assertNotNull(t2.getBanque());
      assertNotNull(t2.getEntite());
      assertNotNull(t2.getCouleur());
      assertTrue(t2.getNom().equals("NOM"));
      assertTrue(t2.getAlias().equals("ALIAS"));
      assertTrue(t2.getPosition() == 1);

      // Test de la mise à jour
      t2.setNom(nomUp);
      terminaleDao.updateObject(t2);
      assertTrue(terminaleDao.findById(new Integer(7)).getNom().equals(nomUp));

      // Test de la délétion
      terminaleDao.removeObject(new Integer(7));
      assertNull(terminaleDao.findById(new Integer(7)));

   }

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   public void testEquals() throws ParseException{
      final String nom = "Term1";
      final String nom2 = "Term2";
      final Enceinte p1 = enceinteDao.findById(1);
      final Enceinte p2 = enceinteDao.findById(2);
      final Terminale t1 = new Terminale();
      final Terminale t2 = new Terminale();

      // L'objet 1 n'est pas égal à null
      assertFalse(t1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(t1.equals(t1));

      /*null*/
      assertTrue(t1.equals(t2));
      assertTrue(t2.equals(t1));

      /*Nom*/
      t2.setNom(nom);
      assertFalse(t1.equals(t2));
      assertFalse(t2.equals(t1));
      t1.setNom(nom2);
      assertFalse(t1.equals(t2));
      assertFalse(t2.equals(t1));
      t1.setNom(nom);
      assertTrue(t1.equals(t2));
      assertTrue(t2.equals(t1));

      /*Enceinte*/
      t2.setEnceinte(p1);
      assertFalse(t1.equals(t2));
      assertFalse(t2.equals(t1));
      t1.setEnceinte(p2);
      assertFalse(t1.equals(t2));
      assertFalse(t2.equals(t1));
      t1.setEnceinte(p1);
      assertTrue(t1.equals(t2));

      final Categorie c3 = new Categorie();
      assertFalse(t1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    * @throws ParseException 
    */
   public void testHashCode() throws ParseException{
      final String nom = "Term1";
      final String nom2 = "Term2";
      final Enceinte p1 = enceinteDao.findById(1);
      final Enceinte p2 = enceinteDao.findById(2);
      final Terminale t1 = new Terminale();
      final Terminale t2 = new Terminale();

      /*null*/
      assertTrue(t1.hashCode() == t2.hashCode());

      /*Nom*/
      t2.setNom(nom);
      assertFalse(t1.hashCode() == t2.hashCode());
      t1.setNom(nom2);
      assertFalse(t1.hashCode() == t2.hashCode());
      t1.setNom(nom);
      assertTrue(t1.hashCode() == t2.hashCode());

      /*Date*/
      t2.setEnceinte(p1);
      assertFalse(t1.hashCode() == t2.hashCode());
      t1.setEnceinte(p2);
      assertFalse(t1.hashCode() == t2.hashCode());
      t1.setEnceinte(p1);
      assertTrue(t1.hashCode() == t2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = t1.hashCode();
      assertTrue(hash == t1.hashCode());
      assertTrue(hash == t1.hashCode());
      assertTrue(hash == t1.hashCode());
      assertTrue(hash == t1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final Terminale t1 = terminaleDao.findById(1);
      assertTrue(t1.toString().equals("{" + t1.getNom() + "}"));

      final Terminale t2 = new Terminale();
      assertTrue(t2.toString().equals("{Empty Terminale}"));
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){
      final Terminale t1 = terminaleDao.findById(1);
      Terminale t2 = new Terminale();
      t2 = t1.clone();

      assertTrue(t1.equals(t2));

      if(t1.getTerminaleId() != null){
         assertTrue(t1.getTerminaleId() == t2.getTerminaleId());
      }else{
         assertNull(t2.getTerminaleId());
      }

      if(t1.getNom() != null){
         assertTrue(t1.getNom().equals(t2.getNom()));
      }else{
         assertNull(t2.getNom());
      }

      if(t1.getEnceinte() != null){
         assertTrue(t1.getEnceinte().equals(t2.getEnceinte()));
      }else{
         assertNull(t2.getEnceinte());
      }

      if(t1.getTerminaleType() != null){
         assertTrue(t1.getTerminaleType().equals(t2.getTerminaleType()));
      }else{
         assertNull(t2.getTerminaleType());
      }

      if(t1.getPosition() != null){
         assertTrue(t1.getPosition() == t2.getPosition());
      }else{
         assertNull(t2.getPosition());
      }

      if(t1.getAlias() != null){
         assertTrue(t1.getAlias().equals(t2.getAlias()));
      }else{
         assertNull(t2.getAlias());
      }

      if(t1.getEntite() != null){
         assertTrue(t1.getEntite().equals(t2.getEntite()));
      }else{
         assertNull(t2.getEntite());
      }

      if(t1.getArchive() != null){
         assertTrue(t1.getArchive().equals(t2.getArchive()));
      }else{
         assertNull(t2.getArchive());
      }

      if(t1.getBanque() != null){
         assertTrue(t1.getBanque().equals(t2.getBanque()));
      }else{
         assertNull(t2.getBanque());
      }

      if(t1.getTerminaleNumerotation() != null){
         assertTrue(t1.getTerminaleNumerotation().equals(t2.getTerminaleNumerotation()));
      }else{
         assertNull(t2.getTerminaleNumerotation());
      }

      if(t1.getCouleur() != null){
         assertTrue(t1.getCouleur().equals(t2.getCouleur()));
      }else{
         assertNull(t2.getCouleur());
      }

      if(t1.getEmplacements() != null){
         assertTrue(t1.getEmplacements().equals(t2.getEmplacements()));
      }else{
         assertNull(t2.getEmplacements());
      }

   }

}
