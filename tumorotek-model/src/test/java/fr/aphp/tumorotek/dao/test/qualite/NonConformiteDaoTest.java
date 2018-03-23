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
package fr.aphp.tumorotek.dao.test.qualite;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.qualite.ConformiteTypeDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.qualite.NonConformite;

/**
 *
 * Classe de test pour le DAO NonConformiteDao et le bean
 * du domaine NonConformite.
 * Date: 08/11/2011
 *
 * @author Pierre Ventadour.
 * @version 2.0.10
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class NonConformiteDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private NonConformiteDao nonConformiteDao;
   /** Bean Dao. */
   private ConformiteTypeDao conformiteTypeDao;
   /** Bean Dao. */
   private PlateformeDao plateformeDao;

   private final int tot = 11;

   public NonConformiteDaoTest(){

   }

   public void setNonConformiteDao(final NonConformiteDao nDao){
      this.nonConformiteDao = nDao;
   }

   public void setConformiteTypeDao(final ConformiteTypeDao cDao){
      this.conformiteTypeDao = cDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAll(){
      final List<NonConformite> liste = nonConformiteDao.findAll();
      assertTrue(liste.size() == tot);
   }

   /**
    * Test l'appel de la méthode findByTypeAndPf().
    */
   public void testFindByTypeAndPf(){
      final ConformiteType t1 = conformiteTypeDao.findById(1);
      final Plateforme pf1 = plateformeDao.findById(1);
      List<NonConformite> liste = nonConformiteDao.findByTypeAndPf(t1, pf1);
      assertTrue(liste.size() == 2);
      assertTrue(liste.get(0).getNom().equals("Erreur dossier"));

      final Plateforme pf2 = plateformeDao.findById(2);
      liste = nonConformiteDao.findByTypeAndPf(t1, pf2);
      assertTrue(liste.size() == 1);

      final ConformiteType t2 = conformiteTypeDao.findById(2);
      liste = nonConformiteDao.findByTypeAndPf(t2, pf2);
      assertTrue(liste.size() == 0);

      liste = nonConformiteDao.findByTypeAndPf(null, pf2);
      assertTrue(liste.size() == 0);

      liste = nonConformiteDao.findByTypeAndPf(t1, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByTypePfAndNom().
    */
   public void testFindByTypePfAndNom(){
      final ConformiteType t1 = conformiteTypeDao.findById(1);
      final Plateforme pf1 = plateformeDao.findById(1);
      List<NonConformite> liste = nonConformiteDao.findByTypePfAndNom(t1, pf1, "Erreur dossier");
      assertTrue(liste.size() == 1);

      final Plateforme pf2 = plateformeDao.findById(2);
      liste = nonConformiteDao.findByTypePfAndNom(t1, pf2, "Inconnu");
      assertTrue(liste.size() == 1);

      liste = nonConformiteDao.findByTypePfAndNom(t1, pf2, "Test");
      assertTrue(liste.size() == 0);

      final ConformiteType t2 = conformiteTypeDao.findById(2);
      liste = nonConformiteDao.findByTypePfAndNom(t2, pf1, "Erreur dossier");
      assertTrue(liste.size() == 0);

      liste = nonConformiteDao.findByTypePfAndNom(null, pf2, "Erreur dossier");
      assertTrue(liste.size() == 0);

      liste = nonConformiteDao.findByTypePfAndNom(t1, null, "Erreur dossier");
      assertTrue(liste.size() == 0);

      liste = nonConformiteDao.findByTypePfAndNom(t1, pf1, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test de la méthode findByExcludedId.
    */
   public void testFindByExcludedId(){
      List<NonConformite> liste = nonConformiteDao.findByExcludedId(1);
      assertTrue(liste.size() == tot - 1);

      liste = nonConformiteDao.findByExcludedId(100);
      assertTrue(liste.size() == tot);
   }

   @Rollback(false)
   public void testCrud() throws Exception{

      final NonConformite n1 = new NonConformite();
      final ConformiteType t1 = conformiteTypeDao.findById(1);
      final Plateforme p1 = plateformeDao.findById(1);
      n1.setNom("TEST");
      n1.setConformiteType(t1);
      n1.setPlateforme(p1);

      // Test de l'insertion
      nonConformiteDao.createObject(n1);
      final Integer id = new Integer(tot + 1);
      assertEquals(id, n1.getNonConformiteId());

      final NonConformite n2 = nonConformiteDao.findById(id);
      // Vérification des données entrées dans la base
      assertNotNull(n2);
      assertNotNull(n2.getConformiteType());
      assertNotNull(n2.getPlateforme());
      assertTrue(n2.getNom().equals("TEST"));

      // Test de la mise à jour
      n2.setNom("UP");
      nonConformiteDao.updateObject(n2);
      assertTrue(nonConformiteDao.findById(id).getNom().equals("UP"));

      // Test de la délétion
      nonConformiteDao.removeObject(id);
      assertNull(nonConformiteDao.findById(id));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String nom = "Nom1";
      final String nom2 = "Nom2";
      final ConformiteType t1 = conformiteTypeDao.findById(1);
      final ConformiteType t2 = conformiteTypeDao.findById(2);
      final Plateforme p1 = plateformeDao.findById(1);
      final Plateforme p2 = plateformeDao.findById(2);
      final NonConformite n1 = new NonConformite();
      final NonConformite n2 = new NonConformite();

      // L'objet 1 n'est pas égal à null
      assertFalse(n1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(n1.equals(n1));

      /*null*/
      assertTrue(n1.equals(n2));
      assertTrue(n2.equals(n1));

      /*Nom*/
      n2.setNom(nom);
      assertFalse(n1.equals(n2));
      assertFalse(n2.equals(n1));
      n1.setNom(nom2);
      assertFalse(n1.equals(n2));
      assertFalse(n2.equals(n1));
      n1.setNom(nom);
      assertTrue(n1.equals(n2));
      assertTrue(n2.equals(n1));

      /*Type*/
      n2.setConformiteType(t1);
      assertFalse(n1.equals(n2));
      assertFalse(n2.equals(n1));
      n1.setConformiteType(t2);
      assertFalse(n1.equals(n2));
      assertFalse(n2.equals(n1));
      n1.setConformiteType(t1);
      assertTrue(n1.equals(n2));

      /*PF*/
      n2.setPlateforme(p1);
      assertFalse(n1.equals(n2));
      assertFalse(n2.equals(n1));
      n1.setPlateforme(p2);
      assertFalse(n1.equals(n2));
      assertFalse(n2.equals(n1));
      n1.setPlateforme(p1);
      assertTrue(n1.equals(n2));
      assertTrue(n2.equals(n1));

      final Categorie c3 = new Categorie();
      assertFalse(n1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String nom = "Nom1";
      final String nom2 = "Nom2";
      final ConformiteType t1 = conformiteTypeDao.findById(1);
      final ConformiteType t2 = conformiteTypeDao.findById(2);
      final Plateforme p1 = plateformeDao.findById(1);
      final Plateforme p2 = plateformeDao.findById(2);
      final NonConformite n1 = new NonConformite();
      final NonConformite n2 = new NonConformite();

      /*null*/
      assertTrue(n1.hashCode() == n2.hashCode());

      /*Nom*/
      n2.setNom(nom);
      assertFalse(n1.hashCode() == n2.hashCode());
      n1.setNom(nom2);
      assertFalse(n1.hashCode() == n2.hashCode());
      n1.setNom(nom);
      assertTrue(n1.hashCode() == n2.hashCode());

      /*Type*/
      n2.setConformiteType(t1);
      assertFalse(n1.hashCode() == n2.hashCode());
      n1.setConformiteType(t2);
      assertFalse(n1.hashCode() == n2.hashCode());
      n1.setConformiteType(t1);
      assertTrue(n1.hashCode() == n2.hashCode());

      /*Plateforme*/
      n2.setPlateforme(p1);
      assertFalse(n1.hashCode() == n2.hashCode());
      n1.setPlateforme(p2);
      assertFalse(n1.hashCode() == n2.hashCode());
      n1.setPlateforme(p1);
      assertTrue(n1.hashCode() == n2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = n1.hashCode();
      assertTrue(hash == n1.hashCode());
      assertTrue(hash == n1.hashCode());
      assertTrue(hash == n1.hashCode());
      assertTrue(hash == n1.hashCode());
   }

   /**
    * test toString().
    */
   public void testToString(){
      final NonConformite n1 = nonConformiteDao.findById(1);
      assertTrue(n1.toString().equals("{" + n1.getNom() + ", " + n1.getConformiteType().getConformiteType() + "(ConformiteType), "
         + n1.getPlateforme().getNom() + "(Plateforme)}"));

      final NonConformite n2 = new NonConformite();
      assertTrue(n2.toString().equals("{Empty NonConformite}"));
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){
      final NonConformite n1 = nonConformiteDao.findById(1);
      final NonConformite n2 = n1.clone();
      assertTrue(n1.equals(n2));

      if(n1.getNonConformiteId() != null){
         assertTrue(n1.getNonConformiteId() == n2.getNonConformiteId());
      }else{
         assertNull(n2.getNonConformiteId());
      }

      if(n1.getConformiteType() != null){
         assertTrue(n1.getConformiteType().equals(n2.getConformiteType()));
      }else{
         assertNull(n2.getConformiteType());
      }

      if(n1.getPlateforme() != null){
         assertTrue(n1.getPlateforme().equals(n2.getPlateforme()));
      }else{
         assertNull(n2.getPlateforme());
      }

      if(n1.getNom() != null){
         assertTrue(n1.getNom().equals(n2.getNom()));
      }else{
         assertNull(n2.getNom());
      }
   }

}
