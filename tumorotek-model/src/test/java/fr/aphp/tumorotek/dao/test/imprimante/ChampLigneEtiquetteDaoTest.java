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



import fr.aphp.tumorotek.dao.imprimante.ChampLigneEtiquetteDao;
import fr.aphp.tumorotek.dao.imprimante.LigneEtiquetteDao;
import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le DAO ModeleDao et le bean du
 * domaine ChampLigneEtiquette.
 *
 * @author Pierre Ventadour.
 * @version 08/06/2011
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class ChampLigneEtiquetteDaoTest extends AbstractDaoTest
{


   @Autowired
 ChampLigneEtiquetteDao champLigneEtiquetteDao;

   @Autowired
 EntiteDao entiteDao;

   @Autowired
 LigneEtiquetteDao ligneEtiquetteDao;

   @Autowired
 ChampDao champDao;

   public ChampLigneEtiquetteDaoTest(){

   }

   @Test
public void setChampLigneEtiquetteDao(final ChampLigneEtiquetteDao cDao){
      this.champLigneEtiquetteDao = cDao;
   }

   @Test
public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   @Test
public void setLigneEtiquetteDao(final LigneEtiquetteDao lDao){
      this.ligneEtiquetteDao = lDao;
   }

   @Test
public void setChampDao(final ChampDao cDao){
      this.champDao = cDao;
   }

   /**
    * Test l'appel de la méthode findById().
    */
   @Test
public void testFindById(){
      final ChampLigneEtiquette cle = champLigneEtiquetteDao.findById(1);
      assertNotNull(cle);

      final ChampLigneEtiquette cleNull = champLigneEtiquetteDao.findById(100);
      assertNull(cleNull);
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAll(){
      final List<ChampLigneEtiquette> liste = IterableUtils.toList(champLigneEtiquetteDao.findAll());
      assertTrue(liste.size() == 10);
   }

   /**
    * Test l'appel de la méthode findByLigneEtiquette().
    */
   @Test
public void testFindByLigneEtiquette(){
      final LigneEtiquette le1 = ligneEtiquetteDao.findById(1);
      List<ChampLigneEtiquette> liste = champLigneEtiquetteDao.findByLigneEtiquette(le1);
      assertTrue(liste.size() == 2);

      final LigneEtiquette le7 = ligneEtiquetteDao.findById(7);
      liste = champLigneEtiquetteDao.findByLigneEtiquette(le7);
      assertTrue(liste.size() == 0);

      liste = champLigneEtiquetteDao.findByLigneEtiquette(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByLigneEtiquetteAndEntite().
    */
   @Test
public void testFindByLigneEtiquetteAndEntite(){
      final LigneEtiquette le1 = ligneEtiquetteDao.findById(1);
      final LigneEtiquette le7 = ligneEtiquetteDao.findById(7);
      final Entite e3 = entiteDao.findById(3);
      final Entite e4 = entiteDao.findById(4);
      final Entite e8 = entiteDao.findById(8);
      List<ChampLigneEtiquette> liste = champLigneEtiquetteDao.findByLigneEtiquetteAndEntite(le1, e3);
      assertTrue(liste.size() == 1);

      liste = champLigneEtiquetteDao.findByLigneEtiquetteAndEntite(le1, e8);
      assertTrue(liste.size() == 1);

      liste = champLigneEtiquetteDao.findByLigneEtiquetteAndEntite(le1, e4);
      assertTrue(liste.size() == 0);

      liste = champLigneEtiquetteDao.findByLigneEtiquetteAndEntite(le7, e3);
      assertTrue(liste.size() == 0);

      liste = champLigneEtiquetteDao.findByLigneEtiquetteAndEntite(null, e3);
      assertTrue(liste.size() == 0);

      liste = champLigneEtiquetteDao.findByLigneEtiquetteAndEntite(le1, null);
      assertTrue(liste.size() == 0);

      liste = champLigneEtiquetteDao.findByLigneEtiquetteAndEntite(null, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'une ligne.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   @Test
public void testCrud() throws Exception{

      final LigneEtiquette le = ligneEtiquetteDao.findById(6);
      final Champ c = champDao.findById(142);
      final Entite e8 = entiteDao.findById(8);
      final ChampLigneEtiquette cle = new ChampLigneEtiquette();
      cle.setLigneEtiquette(le);
      cle.setChamp(c);
      cle.setEntite(e8);
      cle.setOrdre(1);
      cle.setExpReg("EXP");

      // Test de l'insertion
      champLigneEtiquetteDao.save(cle);
      assertEquals(new Integer(11), cle.getChampLigneEtiquetteId());

      final ChampLigneEtiquette cle2 = champLigneEtiquetteDao.findById(new Integer(11));
      // Vérification des données entrées dans la base
      assertNotNull(cle2);
      assertNotNull(cle2.getLigneEtiquette());
      assertNotNull(cle2.getChamp());
      assertNotNull(cle2.getEntite());
      assertTrue(cle2.getOrdre() == 1);
      assertTrue(cle2.getExpReg().equals("EXP"));

      // Test de la mise à jour
      cle2.setOrdre(2);
      cle2.setExpReg("REG");
      champLigneEtiquetteDao.save(cle2);
      assertTrue(champLigneEtiquetteDao.findById(new Integer(11)).getOrdre() == 2);
      assertTrue(champLigneEtiquetteDao.findById(new Integer(11)).getExpReg().equals("REG"));

      // Test de la délétion
      champLigneEtiquetteDao.deleteById(new Integer(11));
      assertNull(champLigneEtiquetteDao.findById(new Integer(11)));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final Integer o1 = 1;
      final Integer o2 = 2;
      final LigneEtiquette le1 = ligneEtiquetteDao.findById(1);
      final LigneEtiquette le2 = ligneEtiquetteDao.findById(2);
      final Champ c1 = champDao.findById(141);
      final Champ c2 = champDao.findById(142);
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final ChampLigneEtiquette cle1 = new ChampLigneEtiquette();
      final ChampLigneEtiquette cle2 = new ChampLigneEtiquette();

      // L'objet 1 n'est pas égal à null
      assertFalse(cle1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(cle1.equals(cle1));

      /*null*/
      assertTrue(cle1.equals(cle2));
      assertTrue(cle2.equals(cle1));

      /*ordre*/
      cle2.setOrdre(o1);
      assertFalse(cle1.equals(cle2));
      assertFalse(cle2.equals(cle1));
      cle1.setOrdre(o2);
      assertFalse(cle1.equals(cle2));
      assertFalse(cle2.equals(cle1));
      cle1.setOrdre(o1);
      assertTrue(cle1.equals(cle2));
      assertTrue(cle2.equals(cle1));

      /*ligne*/
      cle2.setLigneEtiquette(le1);
      assertFalse(cle1.equals(cle2));
      assertFalse(cle2.equals(cle1));
      cle1.setLigneEtiquette(le2);
      assertFalse(cle1.equals(cle2));
      assertFalse(cle2.equals(cle1));
      cle1.setLigneEtiquette(le1);
      assertTrue(cle1.equals(cle2));

      /*champ*/
      cle2.setChamp(c1);
      assertFalse(cle1.equals(cle2));
      assertFalse(cle2.equals(cle1));
      cle1.setChamp(c2);
      assertFalse(cle1.equals(cle2));
      assertFalse(cle2.equals(cle1));
      cle1.setChamp(c1);
      assertTrue(cle1.equals(cle2));
      assertTrue(cle2.equals(cle1));

      /*Entite*/
      cle2.setEntite(e1);
      assertFalse(cle1.equals(cle2));
      assertFalse(cle2.equals(cle1));
      cle1.setEntite(e2);
      assertFalse(cle1.equals(cle2));
      assertFalse(cle2.equals(cle1));
      cle1.setEntite(e1);
      assertTrue(cle1.equals(cle2));
      assertTrue(cle2.equals(cle1));

      final Categorie c3 = new Categorie();
      assertFalse(cle1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final Integer o1 = 1;
      final Integer o2 = 2;
      final LigneEtiquette le1 = ligneEtiquetteDao.findById(1);
      final LigneEtiquette le2 = ligneEtiquetteDao.findById(2);
      final Champ c1 = champDao.findById(141);
      final Champ c2 = champDao.findById(142);
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final ChampLigneEtiquette cle1 = new ChampLigneEtiquette();
      final ChampLigneEtiquette cle2 = new ChampLigneEtiquette();

      /*null*/
      assertTrue(cle1.hashCode() == cle2.hashCode());

      /*Ordre*/
      cle2.setOrdre(o1);
      assertFalse(cle1.hashCode() == cle2.hashCode());
      cle1.setOrdre(o2);
      assertFalse(cle1.hashCode() == cle2.hashCode());
      cle1.setOrdre(o1);
      assertTrue(cle1.hashCode() == cle2.hashCode());

      /*Ligne*/
      cle2.setLigneEtiquette(le1);
      assertFalse(cle1.hashCode() == cle2.hashCode());
      cle1.setLigneEtiquette(le2);
      assertFalse(cle1.hashCode() == cle2.hashCode());
      cle1.setLigneEtiquette(le1);
      assertTrue(cle1.hashCode() == cle2.hashCode());

      /*setChamp*/
      cle2.setChamp(c1);
      assertFalse(cle1.hashCode() == cle2.hashCode());
      cle1.setChamp(c2);
      assertFalse(cle1.hashCode() == cle2.hashCode());
      cle1.setChamp(c1);
      assertTrue(cle1.hashCode() == cle2.hashCode());

      /*Entite*/
      cle2.setEntite(e1);
      assertFalse(cle1.hashCode() == cle2.hashCode());
      cle1.setEntite(e2);
      assertFalse(cle1.hashCode() == cle2.hashCode());
      cle1.setEntite(e1);
      assertTrue(cle1.hashCode() == cle2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = cle1.hashCode();
      assertTrue(hash == cle1.hashCode());
      assertTrue(hash == cle1.hashCode());
      assertTrue(hash == cle1.hashCode());
      assertTrue(hash == cle1.hashCode());
   }

   /**
    * test toString().
    */
   @Test
public void testToString(){
      final ChampLigneEtiquette cle1 = champLigneEtiquetteDao.findById(1);
      assertTrue(cle1.toString().equals("{" + cle1.getOrdre() + ", " + cle1.getLigneEtiquette().getOrdre() + "(LigneEtiquette) "
         + cle1.getEntite().getNom() + "(Entite)}"));

      final ChampLigneEtiquette cle2 = new ChampLigneEtiquette();
      assertTrue(cle2.toString().equals("{Empty ChampLigneEtiquette}"));
   }

   /**
    * Test la méthode clone.
    */
   @Test
public void testClone(){
      final ChampLigneEtiquette cle1 = champLigneEtiquetteDao.findById(1);
      ChampLigneEtiquette cle2 = new ChampLigneEtiquette();
      cle2 = cle1.clone();

      assertTrue(cle1.equals(cle2));

      if(cle1.getChampLigneEtiquetteId() != null){
         assertTrue(cle1.getChampLigneEtiquetteId() == cle2.getChampLigneEtiquetteId());
      }else{
         assertNull(cle2.getChampLigneEtiquetteId());
      }

      if(cle1.getOrdre() != null){
         assertTrue(cle1.getOrdre() == cle2.getOrdre());
      }else{
         assertNull(cle2.getOrdre());
      }

      if(cle1.getLigneEtiquette() != null){
         assertTrue(cle1.getLigneEtiquette().equals(cle2.getLigneEtiquette()));
      }else{
         assertNull(cle2.getLigneEtiquette());
      }

      if(cle1.getChamp() != null){
         assertTrue(cle1.getChamp().equals(cle2.getChamp()));
      }else{
         assertNull(cle2.getChamp());
      }

      if(cle1.getEntite() != null){
         assertTrue(cle1.getEntite().equals(cle2.getEntite()));
      }else{
         assertNull(cle2.getEntite());
      }

      if(cle1.getExpReg() != null){
         assertTrue(cle1.getExpReg().equals(cle2.getExpReg()));
      }else{
         assertNull(cle2.getExpReg());
      }
   }

}
