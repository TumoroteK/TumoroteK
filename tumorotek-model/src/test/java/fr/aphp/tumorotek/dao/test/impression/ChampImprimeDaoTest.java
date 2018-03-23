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
package fr.aphp.tumorotek.dao.test.impression;

import java.text.ParseException;
import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.impression.BlocImpressionDao;
import fr.aphp.tumorotek.dao.impression.ChampImprimeDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.impression.ChampImprimePK;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 *
 * Classe de test pour le DAO ChampImprimeDao et le bean
 * du domaine ChampImprime.
 *
 * @author Pierre Ventadour.
 * @version 23/07/2010
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ChampImprimeDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private ChampImprimeDao champImprimeDao;
   /** Bean Dao. */
   private ChampEntiteDao champEntiteDao;
   /** Bean Dao. */
   private TemplateDao templateDao;
   /** Bean Dao. */
   private BlocImpressionDao blocImpressionDao;

   public ChampImprimeDaoTest(){

   }

   public void setChampImprimeDao(final ChampImprimeDao cDao){
      this.champImprimeDao = cDao;
   }

   public void setChampEntiteDao(final ChampEntiteDao cDao){
      this.champEntiteDao = cDao;
   }

   public void setTemplateDao(final TemplateDao tDao){
      this.templateDao = tDao;
   }

   public void setBlocImpressionDao(final BlocImpressionDao bDao){
      this.blocImpressionDao = bDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAll(){
      final List<ChampImprime> liste = champImprimeDao.findAll();
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findById().
    */
   public void testFindById(){
      final ChampEntite c1 = champEntiteDao.findById(54);
      final Template t1 = templateDao.findById(1);
      final BlocImpression b1 = blocImpressionDao.findById(5);
      ChampImprimePK pk = new ChampImprimePK(t1, c1, b1);

      ChampImprime ci = champImprimeDao.findById(pk);
      assertNotNull(ci);

      final Template t2 = templateDao.findById(2);
      pk = new ChampImprimePK(t2, c1, b1);
      ci = champImprimeDao.findById(pk);
      assertNull(ci);
   }

   /**
    * Test l'appel de la méthode findByExcludedPK().
    */
   public void testFindByExcludedPK(){
      final ChampEntite c1 = champEntiteDao.findById(54);
      final Template t1 = templateDao.findById(1);
      final BlocImpression b1 = blocImpressionDao.findById(5);
      ChampImprimePK pk = new ChampImprimePK(t1, c1, b1);

      List<ChampImprime> liste = champImprimeDao.findByExcludedPK(pk);
      assertTrue(liste.size() == 3);

      final Template t2 = templateDao.findById(2);
      pk = new ChampImprimePK(t2, c1, b1);
      liste = champImprimeDao.findByExcludedPK(pk);
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByTemplate().
    */
   public void testFindByTemplate(){
      final Template t1 = templateDao.findById(1);
      List<ChampImprime> liste = champImprimeDao.findByTemplate(t1);
      assertTrue(liste.size() == 4);

      final Template t2 = templateDao.findById(2);
      liste = champImprimeDao.findByTemplate(t2);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un ChampImprime.
    **/
   @Rollback(false)
   public void testCrud(){

      final ChampImprime ci1 = new ChampImprime();
      final ChampEntite c1 = champEntiteDao.findById(53);
      final Template t1 = templateDao.findById(1);
      final BlocImpression b1 = blocImpressionDao.findById(5);
      final Integer ordre = 5;
      final Integer ordreUp = 6;

      ci1.setChampEntite(c1);
      ci1.setTemplate(t1);
      ci1.setBlocImpression(b1);
      ci1.setOrdre(ordre);

      // Test de l'insertion
      champImprimeDao.createObject(ci1);
      assertTrue(champImprimeDao.findAll().size() == 5);

      // Test de la mise à jour
      final ChampImprimePK pk = new ChampImprimePK();
      pk.setChampEntite(c1);
      pk.setTemplate(t1);
      pk.setBlocImpression(b1);
      final ChampImprime ci2 = champImprimeDao.findById(pk);
      assertNotNull(ci2);
      assertTrue(ci2.getTemplate().equals(t1));
      assertTrue(ci2.getChampEntite().equals(c1));
      assertTrue(ci2.getOrdre() == ordre);

      //update
      ci2.setOrdre(ordreUp);
      champImprimeDao.updateObject(ci2);
      assertTrue(champImprimeDao.findById(pk).equals(ci2));
      assertTrue(champImprimeDao.findById(pk).getOrdre() == ordreUp);
      assertTrue(champImprimeDao.findAll().size() == 5);

      // Test de la délétion
      champImprimeDao.removeObject(pk);
      assertNull(champImprimeDao.findById(pk));
      assertTrue(champImprimeDao.findAll().size() == 4);
   }

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   public void testEquals() throws ParseException{

      final ChampImprime ci1 = new ChampImprime();
      final ChampImprime ci2 = new ChampImprime();

      // L'objet 1 n'est pas égal à null
      assertFalse(ci1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(ci1.equals(ci1));
      // 2 objets sont égaux entre eux
      assertTrue(ci1.equals(ci2));
      assertTrue(ci2.equals(ci1));

      populateClefsToTestEqualsAndHashCode(ci1, ci2);

      //dummy test
      final Banque b = new Banque();
      assertFalse(ci1.equals(b));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    * @throws ParseException 
    */
   public void testHashCode() throws ParseException{
      final ChampImprime ci1 = new ChampImprime();
      final ChampImprime ci2 = new ChampImprime();
      final ChampImprime ci3 = new ChampImprime();

      assertTrue(ci1.hashCode() == ci2.hashCode());
      assertTrue(ci2.hashCode() == ci3.hashCode());
      assertTrue(ci3.hashCode() > 0);

      //teste dans methode precedente
      populateClefsToTestEqualsAndHashCode(ci1, ci2);

      final int hash = ci1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(ci1.hashCode() == ci2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == ci1.hashCode());
      assertTrue(hash == ci1.hashCode());
      assertTrue(hash == ci1.hashCode());
      assertTrue(hash == ci1.hashCode());
   }

   private void populateClefsToTestEqualsAndHashCode(final ChampImprime ci1, final ChampImprime ci2) throws ParseException{

      final ChampEntite c1 = champEntiteDao.findById(1);
      final ChampEntite c2 = champEntiteDao.findById(2);
      final Template t1 = templateDao.findById(1);
      final BlocImpression b1 = blocImpressionDao.findById(5);
      final ChampImprimePK pk1 = new ChampImprimePK(t1, c1, b1);
      final ChampImprimePK pk2 = new ChampImprimePK(t1, c2, b1);
      final ChampImprimePK pk3 = new ChampImprimePK(t1, c1, b1);
      final ChampImprimePK[] pks = new ChampImprimePK[] {null, pk1, pk2, pk3};

      for(int i = 0; i < pks.length; i++){
         for(int k = 0; k < pks.length; k++){

            ci1.setPk(pks[i]);
            ci2.setPk(pks[k]);

            if(((i == k) || (i + k == 4))){
               assertTrue(ci1.equals(ci2));
               assertTrue(ci1.hashCode() == ci2.hashCode());
            }else{
               assertFalse(ci1.equals(ci2));
            }
         }
      }
   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final ChampEntite c1 = champEntiteDao.findById(54);
      final Template t1 = templateDao.findById(1);
      final BlocImpression b1 = blocImpressionDao.findById(5);
      final ChampImprimePK pk1 = new ChampImprimePK(t1, c1, b1);
      final ChampImprime ci1 = champImprimeDao.findById(pk1);

      assertTrue(ci1.toString().equals("{" + ci1.getPk().toString() + "}"));

      final ChampImprime ci2 = new ChampImprime();
      ci2.setPk(null);
      assertTrue(ci2.toString().equals("{Empty ChampImprime}"));
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){
      final ChampEntite c1 = champEntiteDao.findById(54);
      final Template t1 = templateDao.findById(1);
      final BlocImpression b1 = blocImpressionDao.findById(5);
      final ChampImprimePK pk1 = new ChampImprimePK(t1, c1, b1);
      final ChampImprime ci1 = champImprimeDao.findById(pk1);

      ChampImprime ci2 = new ChampImprime();
      ci2 = ci1.clone();

      assertTrue(ci1.equals(ci2));

      if(ci1.getPk() != null){
         assertTrue(ci1.getPk().equals(ci2.getPk()));
      }else{
         assertNull(ci2.getPk());
      }

      if(ci1.getOrdre() != null){
         assertTrue(ci1.getOrdre() == ci2.getOrdre());
      }else{
         assertNull(ci2.getOrdre());
      }
   }

}
