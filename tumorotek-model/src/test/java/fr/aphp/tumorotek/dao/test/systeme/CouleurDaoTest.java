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

import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Couleur;

/**
 *
 * Classe de test pour le DAO CouleurDao et le bean du domaine Couleur.
 * Classe de test créée le 29/04/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class CouleurDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private CouleurDao couleurDao;

   /** Valeur du nom pour la maj. */
   private final String updatedCouleur = "ROSE";

   /**
    * Constructeur.
    */
   public CouleurDaoTest(){}

   public void setCouleurDao(final CouleurDao cDao){
      this.couleurDao = cDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testFindAll(){
      final List<Couleur> liste = couleurDao.findAll();
      assertTrue(liste.size() == 15);
   }

   /**
    * Test l'appel de la méthode findByCouleur().
    */
   public void testFindByCouleur(){
      List<Couleur> liste = couleurDao.findByCouleur("VERT");
      assertTrue(liste.size() == 1);
      liste = couleurDao.findByCouleur("BLABLA");
      assertTrue(liste.size() == 0);
      liste = couleurDao.findByCouleur("VE%");
      assertTrue(liste.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByVisotube().
    */
   public void testFindByVisotube(){
      final List<Couleur> liste = couleurDao.findByVisotube();
      assertTrue(liste.size() == 12);
      assertTrue(liste.get(0).getCouleur().equals("TRANSPARENT"));
      assertTrue(liste.get(4).getCouleur().equals("VERT"));
      assertTrue(liste.get(11).getCouleur().equals("PISTACHE"));
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'une couleur.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudCouleur() throws Exception{
      final Couleur c = new Couleur();

      c.setCouleur("NOIR");
      c.setHexa("#000000");
      c.setOrdreVisotube(21);
      // Test de l'insertion
      couleurDao.createObject(c);
      assertEquals(new Integer(16), c.getCouleurId());

      // Test de la mise à jour
      final Couleur c2 = couleurDao.findById(new Integer(16));
      assertNotNull(c2);
      assertTrue(c2.getCouleur().equals("NOIR"));
      assertTrue(c2.getHexa().equals("#000000"));
      assertTrue(c2.getHexaCssStyle().equals("color: #000000"));
      assertTrue(c2.getCouleurMinCase().equals("noir"));
      assertTrue(c2.getOrdreVisotube() == 21);
      c2.setCouleur(updatedCouleur);
      couleurDao.updateObject(c2);
      assertTrue(couleurDao.findById(new Integer(16)).getCouleur().equals(updatedCouleur));

      // Test de la délétion
      couleurDao.removeObject(new Integer(16));
      assertNull(couleurDao.findById(new Integer(16)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String couleur = "NOIR";
      final String couleur2 = "ROSE";
      final Couleur c1 = new Couleur();
      c1.setCouleur(couleur);
      final Couleur c2 = new Couleur();
      c2.setCouleur(couleur);

      // L'objet 1 n'est pas égal à null
      assertFalse(c1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(c1.equals(c1));
      // 2 objets sont égaux entre eux
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      // Vérification de la différenciation de 2 objets
      c2.setCouleur(couleur2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));

      c2.setCouleur(null);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));

      c1.setCouleur(null);
      assertTrue(c1.equals(c2));
      c2.setCouleur(couleur);
      assertFalse(c1.equals(c2));

      final Banque b = new Banque();
      assertFalse(c1.equals(b));

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String couleur = "NOIR";
      final Couleur c1 = new Couleur();
      c1.setCouleur(couleur);
      final Couleur c2 = new Couleur();
      c2.setCouleur(couleur);
      final Couleur c3 = new Couleur();
      c3.setCouleur(null);
      assertTrue(c3.hashCode() > 0);

      final int hash = c1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(c1.hashCode() == c2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());

   }

}
