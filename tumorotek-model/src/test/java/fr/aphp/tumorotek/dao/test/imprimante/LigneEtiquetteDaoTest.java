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
package fr.aphp.tumorotek.dao.test.imprimante;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import fr.aphp.tumorotek.dao.test.Config;



import fr.aphp.tumorotek.dao.imprimante.LigneEtiquetteDao;
import fr.aphp.tumorotek.dao.imprimante.ModeleDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;

/**
 *
 * Classe de test pour le DAO ModeleDao et le bean du domaine LigneEtiquette.
 *
 * @author Pierre Ventadour.
 * @version 08/06/2011
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class LigneEtiquetteDaoTest extends AbstractDaoTest
{


   @Autowired
 LigneEtiquetteDao ligneEtiquetteDao;

   @Autowired
 ModeleDao modeleDao;

   public LigneEtiquetteDaoTest(){

   }

   @Test
public void setLigneEtiquetteDao(final LigneEtiquetteDao lDao){
      this.ligneEtiquetteDao = lDao;
   }

   @Test
public void setModeleDao(final ModeleDao mDao){
      this.modeleDao = mDao;
   }

   /**
    * Test l'appel de la méthode findById().
    */
   @Test
public void testFindById(){
      final LigneEtiquette le = ligneEtiquetteDao.findById(1);
      assertNotNull(le);

      final LigneEtiquette leNull = ligneEtiquetteDao.findById(100);
      assertNull(leNull);
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAll(){
      final List<LigneEtiquette> liste = IterableUtils.toList(ligneEtiquetteDao.findAll());
      assertTrue(liste.size() == 7);
   }

   /**
    * Test l'appel de la méthode findByModele().
    */
   @Test
public void testFindByModele(){
      final Modele m2 = modeleDao.findById(2);
      List<LigneEtiquette> liste = ligneEtiquetteDao.findByModele(m2);
      assertTrue(liste.size() == 7);

      final Modele m1 = modeleDao.findById(1);
      liste = ligneEtiquetteDao.findByModele(m1);
      assertTrue(liste.size() == 0);

      liste = ligneEtiquetteDao.findByModele(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'une ligne.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   @Test
public void testCrud() throws Exception{

      final Modele m1 = modeleDao.findById(1);
      final LigneEtiquette le = new LigneEtiquette();
      le.setModele(m1);
      le.setOrdre(1);
      le.setIsBarcode(true);
      le.setEntete("ENTETE");
      le.setContenu("CONTENU");
      le.setFont("FONT");
      le.setStyle("BOLD");
      le.setSize(10);

      // Test de l'insertion
      ligneEtiquetteDao.save(le);
      assertEquals(new Integer(8), le.getLigneEtiquetteId());

      final LigneEtiquette le2 = ligneEtiquetteDao.findById(new Integer(8));
      // Vérification des données entrées dans la base
      assertNotNull(le2);
      assertNotNull(le2.getModele());
      assertTrue(le2.getOrdre() == 1);
      assertTrue(le2.getIsBarcode());
      assertTrue(le2.getEntete().equals("ENTETE"));
      assertTrue(le2.getContenu().equals("CONTENU"));
      assertTrue(le2.getFont().equals("FONT"));
      assertTrue(le2.getStyle().equals("BOLD"));
      assertTrue(le2.getSize() == 10);

      // Test de la mise à jour
      le2.setOrdre(2);
      le2.setIsBarcode(false);
      ligneEtiquetteDao.save(le2);
      assertTrue(ligneEtiquetteDao.findById(new Integer(8)).getOrdre() == 2);
      assertFalse(ligneEtiquetteDao.findById(new Integer(8)).getIsBarcode());

      // Test de la délétion
      ligneEtiquetteDao.deleteById(new Integer(8));
      assertNull(ligneEtiquetteDao.findById(new Integer(8)));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final Integer o1 = 1;
      final Integer o2 = 2;
      final Modele m1 = modeleDao.findById(1);
      final Modele m2 = modeleDao.findById(2);
      final LigneEtiquette l1 = new LigneEtiquette();
      final LigneEtiquette l2 = new LigneEtiquette();
      l1.setOrdre(o1);
      l1.setModele(m1);
      l2.setOrdre(o1);
      l2.setModele(m1);

      // L'objet 1 n'est pas égal à null
      assertFalse(l1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(l1.equals(l1));
      // 2 objets sont égaux entre eux
      assertTrue(l1.equals(l2));
      assertTrue(l2.equals(l1));

      l1.setModele(null);
      l1.setOrdre(null);
      l2.setModele(null);
      l2.setOrdre(null);
      assertTrue(l1.equals(l2));
      l2.setOrdre(o1);
      assertFalse(l1.equals(l2));
      l1.setOrdre(o1);
      assertTrue(l1.equals(l2));
      l2.setOrdre(o2);
      assertFalse(l1.equals(l2));
      l2.setOrdre(null);
      assertFalse(l1.equals(l2));
      l2.setModele(m1);
      assertFalse(l1.equals(l2));

      l1.setModele(m1);
      l1.setOrdre(null);
      l2.setOrdre(null);
      l2.setModele(m1);
      assertTrue(l1.equals(l2));
      l2.setModele(m2);
      assertFalse(l1.equals(l2));
      l2.setOrdre(o1);
      assertFalse(l1.equals(l2));

      // Vérification de la différenciation de 2 objets
      l1.setOrdre(o1);
      assertFalse(l1.equals(l2));
      l2.setOrdre(o2);
      l2.setModele(m1);
      assertFalse(l1.equals(l2));

      final Categorie c3 = new Categorie();
      assertFalse(l1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final Integer o1 = 1;
      final Integer o2 = 2;
      final Modele m1 = modeleDao.findById(1);
      final Modele m2 = modeleDao.findById(2);
      final LigneEtiquette l1 = new LigneEtiquette();
      final LigneEtiquette l2 = new LigneEtiquette();
      //null
      assertTrue(l1.hashCode() == l2.hashCode());

      //Ordre
      l2.setOrdre(o1);
      assertFalse(l1.hashCode() == l2.hashCode());
      l1.setOrdre(o2);
      assertFalse(l1.hashCode() == l2.hashCode());
      l1.setOrdre(o1);
      assertTrue(l1.hashCode() == l2.hashCode());

      //Modele
      l2.setModele(m1);
      assertFalse(l1.hashCode() == l2.hashCode());
      l1.setModele(m2);
      assertFalse(l1.hashCode() == l2.hashCode());
      l1.setModele(m1);
      assertTrue(l1.hashCode() == l2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = l1.hashCode();
      assertTrue(hash == l1.hashCode());
      assertTrue(hash == l1.hashCode());
      assertTrue(hash == l1.hashCode());
      assertTrue(hash == l1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   @Test
public void testToString(){
      final LigneEtiquette l1 = ligneEtiquetteDao.findById(1);
      assertTrue(l1.toString().equals("{" + l1.getOrdre() + ", " + l1.getModele().getNom() + "(Modele)}"));

      final LigneEtiquette l2 = new LigneEtiquette();
      assertTrue(l2.toString().equals("{Empty LigneEtiquette}"));
   }

   /**
    * Test la méthode clone.
    */
   @Test
public void testClone(){
      final LigneEtiquette l1 = ligneEtiquetteDao.findById(1);
      LigneEtiquette l2 = new LigneEtiquette();
      l2 = l1.clone();

      assertTrue(l1.equals(l2));

      if(l1.getLigneEtiquetteId() != null){
         assertTrue(l1.getLigneEtiquetteId() == l2.getLigneEtiquetteId());
      }else{
         assertNull(l2.getLigneEtiquetteId());
      }

      if(l1.getOrdre() != null){
         assertTrue(l1.getOrdre() == l2.getOrdre());
      }else{
         assertNull(l2.getOrdre());
      }

      if(l1.getSize() != null){
         assertTrue(l1.getSize() == l2.getSize());
      }else{
         assertNull(l2.getSize());
      }

      if(l1.getModele() != null){
         assertTrue(l1.getModele().equals(l2.getModele()));
      }else{
         assertNull(l2.getModele());
      }

      if(l1.getIsBarcode() != null){
         assertTrue(l1.getIsBarcode() == l2.getIsBarcode());
      }else{
         assertNull(l2.getIsBarcode());
      }

      if(l1.getEntete() != null){
         assertTrue(l1.getEntete().equals(l2.getEntete()));
      }else{
         assertNull(l2.getEntete());
      }

      if(l1.getContenu() != null){
         assertTrue(l1.getContenu().equals(l2.getContenu()));
      }else{
         assertNull(l2.getContenu());
      }

      if(l1.getFont() != null){
         assertTrue(l1.getFont().equals(l2.getFont()));
      }else{
         assertNull(l2.getFont());
      }

      if(l1.getStyle() != null){
         assertTrue(l1.getStyle().equals(l2.getStyle()));
      }else{
         assertNull(l2.getStyle());
      }

      if(l1.getChampLigneEtiquettes() != null){
         assertTrue(l1.getChampLigneEtiquettes().equals(l2.getChampLigneEtiquettes()));
      }else{
         assertNull(l2.getChampLigneEtiquettes());
      }
   }

}
