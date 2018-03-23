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

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonTypeDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.systeme.CouleurEntiteTypeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;

/**
 *
 * Classe de test pour le DAO CouleurEntiteTypeDao et le bean
 * du domaine CouleurEntiteType.
 *
 * @author Pierre Ventadour
 * @version 30/04/2010
 *
 */
public class CouleurEntiteTypeDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private CouleurEntiteTypeDao couleurEntiteTypeDao;
   /** Bean Dao. */
   private BanqueDao banqueDao;
   /** Bean Dao. */
   private CouleurDao couleurDao;
   /** Bean Dao. */
   private EchantillonTypeDao echantillonTypeDao;
   /** Bean Dao. */
   private ProdTypeDao prodTypeDao;

   public CouleurEntiteTypeDaoTest(){

   }

   public void setCouleurEntiteTypeDao(final CouleurEntiteTypeDao cDao){
      this.couleurEntiteTypeDao = cDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setCouleurDao(final CouleurDao cDao){
      this.couleurDao = cDao;
   }

   public void setEchantillonTypeDao(final EchantillonTypeDao eDao){
      this.echantillonTypeDao = eDao;
   }

   public void setProdTypeDao(final ProdTypeDao pDao){
      this.prodTypeDao = pDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAll(){
      final List<CouleurEntiteType> liste = couleurEntiteTypeDao.findAll();
      assertTrue(liste.size() == 3);
   }

   /**
    * Test l'appel de la méthode findByBanque().
    */
   public void testFindByBanque(){
      final Banque b1 = banqueDao.findById(1);
      List<CouleurEntiteType> liste = couleurEntiteTypeDao.findByBanque(b1);
      assertTrue(liste.size() == 2);

      final Banque b3 = banqueDao.findById(3);
      liste = couleurEntiteTypeDao.findByBanque(b3);
      assertTrue(liste.size() == 0);

      liste = couleurEntiteTypeDao.findByBanque(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByBanqueAllEchanType().
    */
   public void testFindByBanqueAllEchanType(){
      final Banque b1 = banqueDao.findById(1);
      List<CouleurEntiteType> liste = couleurEntiteTypeDao.findByBanqueAllEchanType(b1);
      assertTrue(liste.size() == 1);
      assertNotNull(liste.get(0).getEchantillonType());
      assertNull(liste.get(0).getProdType());

      final Banque b3 = banqueDao.findById(3);
      liste = couleurEntiteTypeDao.findByBanqueAllEchanType(b3);
      assertTrue(liste.size() == 0);

      liste = couleurEntiteTypeDao.findByBanqueAllEchanType(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByBanqueAllProdType().
    */
   public void testFindByBanqueAllProdType(){
      final Banque b1 = banqueDao.findById(1);
      List<CouleurEntiteType> liste = couleurEntiteTypeDao.findByBanqueAllProdType(b1);
      assertTrue(liste.size() == 1);
      assertNull(liste.get(0).getEchantillonType());
      assertNotNull(liste.get(0).getProdType());

      final Banque b3 = banqueDao.findById(3);
      liste = couleurEntiteTypeDao.findByBanqueAllProdType(b3);
      assertTrue(liste.size() == 0);

      liste = couleurEntiteTypeDao.findByBanqueAllProdType(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<CouleurEntiteType> liste = couleurEntiteTypeDao.findByExcludedId(1);
      assertTrue(liste.size() == 2);

      liste = couleurEntiteTypeDao.findByExcludedId(25);
      assertTrue(liste.size() == 3);

   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un collaborateur.
    * @throws Exception lance une exception en cas d'erreur sur les données. 
    */
   @Rollback(false)
   public void testCrudCollaborateur() throws Exception{

      final CouleurEntiteType c = new CouleurEntiteType();
      final Couleur couleur = couleurDao.findById(1);
      final Couleur couleurUp = couleurDao.findById(2);
      final Banque bank = banqueDao.findById(1);
      final EchantillonType eType = echantillonTypeDao.findById(1);
      final ProdType pType = prodTypeDao.findById(1);

      c.setBanque(bank);
      c.setCouleur(couleur);
      c.setEchantillonType(eType);
      c.setProdType(pType);
      // Test de l'insertion
      couleurEntiteTypeDao.createObject(c);
      assertEquals(new Integer(4), c.getCouleurEntiteTypeId());

      // Test de la mise à jour
      final CouleurEntiteType c2 = couleurEntiteTypeDao.findById(new Integer(4));
      // Vérification des données entrées dans la base
      assertNotNull(c2);
      assertNotNull(c2.getBanque());
      assertNotNull(c2.getCouleur());
      assertNotNull(c2.getEchantillonType());
      assertNotNull(c2.getProdType());

      c2.setCouleur(couleurUp);
      couleurEntiteTypeDao.updateObject(c2);
      assertTrue(couleurEntiteTypeDao.findById(new Integer(4)).getCouleur().equals(couleurUp));
      // Test de la délétion
      couleurEntiteTypeDao.removeObject(new Integer(4));
      assertNull(couleurEntiteTypeDao.findById(new Integer(4)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final Banque bank = banqueDao.findById(1);
      final Banque bank2 = banqueDao.findById(2);
      final EchantillonType eType = echantillonTypeDao.findById(1);
      final EchantillonType eType2 = echantillonTypeDao.findById(2);
      final ProdType pType = prodTypeDao.findById(1);
      final ProdType pType2 = prodTypeDao.findById(2);
      final CouleurEntiteType c1 = new CouleurEntiteType();
      final CouleurEntiteType c2 = new CouleurEntiteType();

      // L'objet 1 n'est pas égal à null
      assertFalse(c1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(c1.equals(c1));

      /*null*/
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      /*Banque*/
      c2.setBanque(bank);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setBanque(bank2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setBanque(bank);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      /*EchantillonType*/
      c2.setEchantillonType(eType);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setEchantillonType(eType2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setEchantillonType(eType);
      assertTrue(c1.equals(c2));

      /*ProdType*/
      c2.setProdType(pType);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setProdType(pType2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setProdType(pType);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      final Categorie c3 = new Categorie();
      assertFalse(c1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final Banque bank = banqueDao.findById(1);
      final Banque bank2 = banqueDao.findById(2);
      final EchantillonType eType = echantillonTypeDao.findById(1);
      final EchantillonType eType2 = echantillonTypeDao.findById(2);
      final ProdType pType = prodTypeDao.findById(1);
      final ProdType pType2 = prodTypeDao.findById(2);
      final CouleurEntiteType c1 = new CouleurEntiteType();
      final CouleurEntiteType c2 = new CouleurEntiteType();

      /*null*/
      assertTrue(c1.hashCode() == c2.hashCode());

      /*Banque*/
      c2.setBanque(bank);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setBanque(bank2);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setBanque(bank);
      assertTrue(c1.hashCode() == c2.hashCode());

      /*EchantillonType*/
      c2.setEchantillonType(eType);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setEchantillonType(eType2);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setEchantillonType(eType);
      assertTrue(c1.hashCode() == c2.hashCode());

      /*ProdType*/
      c2.setProdType(pType);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setProdType(pType2);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setProdType(pType);
      assertTrue(c1.hashCode() == c2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = c1.hashCode();
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
   }

   /**
    * test toString().
    */
   public void testToString(){
      final CouleurEntiteType c1 = couleurEntiteTypeDao.findById(1);
      assertTrue(c1.toString()
         .equals("{" + c1.getCouleur() + ", " + c1.getBanque() + ", " + c1.getEchantillonType() + ", " + c1.getProdType() + "}"));

      final CouleurEntiteType c2 = new CouleurEntiteType();
      assertTrue(c2.toString().equals("{Empty CouleurEntiteType}"));
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){
      final CouleurEntiteType c1 = couleurEntiteTypeDao.findById(1);
      final CouleurEntiteType c2 = c1.clone();
      assertTrue(c1.equals(c2));

      if(c1.getCouleurEntiteTypeId() != null){
         assertTrue(c1.getCouleurEntiteTypeId() == c2.getCouleurEntiteTypeId());
      }else{
         assertNull(c2.getCouleurEntiteTypeId());
      }

      if(c1.getCouleur() != null){
         assertTrue(c1.getCouleur().equals(c2.getCouleur()));
      }else{
         assertNull(c2.getCouleur());
      }

      if(c1.getBanque() != null){
         assertTrue(c1.getBanque().equals(c2.getBanque()));
      }else{
         assertNull(c2.getBanque());
      }

      if(c1.getEchantillonType() != null){
         assertTrue(c1.getEchantillonType().equals(c2.getEchantillonType()));
      }else{
         assertNull(c2.getEchantillonType());
      }

      if(c1.getProdType() != null){
         assertTrue(c1.getProdType().equals(c2.getProdType()));
      }else{
         assertNull(c2.getProdType());
      }
   }

}
