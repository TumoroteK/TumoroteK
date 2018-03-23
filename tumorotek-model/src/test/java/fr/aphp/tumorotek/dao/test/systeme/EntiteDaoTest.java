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

import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le DAO EntiteDao et le bean
 * du domaine Entite.
 * Classe créée le 29/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class EntiteDaoTest extends AbstractDaoTest
{

   private EntiteDao entiteDao;

   public EntiteDaoTest(){

   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAll(){
      final List<Entite> entites = entiteDao.findAll();
      assertTrue(entites.size() == 64);
   }

   /**
    * Test l'appel de la méthode findByNom().
    */
   public void testFindByNom(){
      List<Entite> entites = entiteDao.findByNom("Patient");
      assertTrue(entites.size() == 1);
      entites = entiteDao.findByNom("Test");
      assertTrue(entites.size() == 0);
   }

   public void testFindAnnotables(){
      final List<Entite> entites = entiteDao.findAnnotables();
      assertTrue(entites.size() == 5);
      assertTrue(entites.get(3).getNom().equals("Cession"));
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'une Entite.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   public void testCrud() throws Exception{

      final Entite e = new Entite();
      final String updatedEntite = "MAJ";

      e.setNom("NEW");
      e.setMasc(true);
      e.setAnnotable(false);
      // Test de l'insertion
      entiteDao.createObject(e);

      // Test de la mise à jour
      final Entite e2 = entiteDao.findById(e.getEntiteId());
      assertNotNull(e2);
      assertTrue(e2.getNom().equals("NEW"));
      assertTrue(e2.getMasc());
      assertFalse(e2.getAnnotable());
      e2.setNom(updatedEntite);
      entiteDao.updateObject(e2);
      assertTrue(entiteDao.findById(e2.getEntiteId()).getNom().equals(updatedEntite));

      // Test de la délétion
      entiteDao.removeObject(e2.getEntiteId());
      assertNull(entiteDao.findById(e2.getEntiteId()));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String nom = "Entite";
      final String nom2 = "Entite2";
      final Entite e1 = new Entite();
      e1.setNom(nom);
      final Entite e2 = new Entite();
      e2.setNom(nom);

      // L'objet 1 n'est pas égal à null
      assertFalse(e1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(e1.equals(e1));
      // 2 objets sont égaux entre eux
      assertTrue(e1.equals(e2));
      assertTrue(e2.equals(e1));

      // Vérification de la différenciation de 2 objets
      e2.setNom(nom2);
      assertFalse(e1.equals(e2));
      assertFalse(e2.equals(e1));

      e2.setNom(null);
      assertFalse(e1.equals(e2));
      assertFalse(e2.equals(e1));

      e1.setNom(null);
      assertTrue(e1.equals(e2));
      e2.setNom(nom);
      assertFalse(e1.equals(e2));

      final Categorie c = new Categorie();
      assertFalse(e1.equals(c));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String nom = "Entite";
      final Entite e1 = new Entite();
      e1.setNom(nom);
      final Entite e2 = new Entite();
      e2.setNom(nom);
      final Entite e3 = new Entite();
      e3.setNom(null);
      assertTrue(e3.hashCode() > 0);

      final int hash = e1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(e1.hashCode() == e2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());

   }

   /**
    * test toString().
    */
   public void testToString(){
      final Entite e1 = entiteDao.findById(1);
      assertTrue(e1.toString().equals("{" + e1.getNom() + "}"));

      final Entite e2 = new Entite();
      assertTrue(e2.toString().equals("{Empty Entite}"));
   }
}
