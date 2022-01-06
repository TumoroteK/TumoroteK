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
package fr.aphp.tumorotek.dao.test.io.export;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.io.export.CritereDao;
import fr.aphp.tumorotek.dao.io.export.GroupementDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Groupement;

/**
 *
 * Classe de test pour le DAO GroupementDao et le
 * bean du domaine Groupement.
 * Classe de test créée le 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class GroupementDaoTest extends AbstractDaoTest
{


   @Autowired
 GroupementDao groupementDao;
   @Autowired
 CritereDao critereDao;
   @Autowired
 ChampDao champDao;

   /** Constructeur. */
   public GroupementDaoTest(){}

   /**
    * Setter du bean GroupementDao.
    * @param gDao est le bean Dao.
    */
   @Test
public void setGroupementDao(final GroupementDao gDao){
      this.groupementDao = gDao;
   }

   /**
    * Setter du bean CritereDao.
    * @param cDao est le bean Dao.
    */
   @Test
public void setCritereDao(final CritereDao cDao){
      this.critereDao = cDao;
   }

   /**
    * Setter du bean ChampDao.
    * @param cDao est le bean Dao.
    */
   @Test
public void setChampDao(final ChampDao cDao){
      this.champDao = cDao;
   }

   @Test
public void testFindEnfants() throws Exception{
      final List<Groupement> parents = this.IterableUtils.toList(groupementDao.findAll());
      final Iterator<Groupement> itParents = parents.iterator();
      while(itParents.hasNext()){
         final Groupement parent = itParents.next();
         final ArrayList<Groupement> enfants = this.groupementDao.findEnfants(parent);
         final Iterator<Groupement> it = enfants.iterator();
         while(it.hasNext()){
            assertTrue(parent.equals(it.next().getParent()));
         }
      }
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
   * d'un groupement.
   * @throws Exception lance une exception en cas de problème lors du CRUD.
   */
   @Rollback(false)
   @Test
public void testCrudGroupement() throws Exception{
      /** Create. */
      Critere c = new Critere(this.champDao.findById(3), "!=", "toto");
      this.critereDao.save(c);
      final int id1 = c.getCritereId();
      c = new Critere(this.champDao.findById(4), "=", "tti");
      this.critereDao.save(c);
      final int id2 = c.getCritereId();

      final String condition = "and";
      final Critere critere1 = this.critereDao.findById(id1);
      final Critere critere2 = this.critereDao.findById(id2);
      final Groupement parent = this.groupementDao.findById(1);

      final Groupement g = new Groupement();
      g.setOperateur(condition);
      g.setCritere1(critere1);
      g.setCritere2(critere2);
      g.setParent(parent);

      Integer idObject = new Integer(-1);
      // Test de l'insertion
      this.groupementDao.save(g);
      final List<Groupement> groupements = this.IterableUtils.toList(groupementDao.findAll());
      final Iterator<Groupement> itGroupement = groupements.iterator();
      boolean found = false;
      while(itGroupement.hasNext()){
         final Groupement temp = itGroupement.next();
         if(temp.equals(g)){
            found = true;
            idObject = temp.getGroupementId();
            break;
         }
      }
      assertTrue(found);

      /** Update. */
      final Groupement g2 = this.groupementDao.findById(idObject);
      assertNotNull(g2);
      assertNotNull(g2.getOperateur());
      assertTrue(g2.getOperateur().equals(condition));
      if(g2.getCritere1() != null){
         assertTrue(g2.getCritere1().equals(critere1));
      }else{
         assertNull(critere1);
      }
      if(g2.getCritere2() != null){
         assertTrue(g2.getCritere2().equals(critere2));
      }else{
         assertNull(critere2);
      }
      if(g2.getParent() != null){
         assertTrue(g2.getParent().equals(parent));
      }else{
         assertNull(parent);
      }

      final String updatedCondition = "or";
      final Critere updatedCritere1 = this.critereDao.findById(id2);
      final Critere updatedCritere2 = this.critereDao.findById(id1);
      final Groupement updatedParent = this.groupementDao.findById(2);

      g2.setOperateur(updatedCondition);
      g2.setCritere1(updatedCritere1);
      g2.setCritere2(updatedCritere2);
      g2.setParent(updatedParent);

      this.groupementDao.save(g2);
      assertNotNull(this.groupementDao.findById(idObject).getOperateur());
      assertTrue(this.groupementDao.findById(idObject).getOperateur().equals(updatedCondition));
      if(this.groupementDao.findById(idObject).getCritere1() != null){
         assertTrue(this.groupementDao.findById(idObject).getCritere1().equals(updatedCritere1));
      }else{
         assertNull(updatedCritere1);
      }
      if(this.groupementDao.findById(idObject).getCritere2() != null){
         assertTrue(this.groupementDao.findById(idObject).getCritere2().equals(updatedCritere2));
      }else{
         assertNull(updatedCritere2);
      }
      if(this.groupementDao.findById(idObject).getParent() != null){
         assertTrue(this.groupementDao.findById(idObject).getParent().equals(updatedParent));
      }else{
         assertNull(updatedParent);
      }

      /** Delete. */
      this.groupementDao.deleteById(idObject);
      assertNull(this.groupementDao.findById(idObject));

      //On supprime les éléments créés
      this.critereDao.deleteById(id1);
      this.critereDao.deleteById(id2);
   }

   /**
    * test toString().
    */
   @Test
public void testToString(){
      final Groupement g1 = groupementDao.findById(1);
      assertTrue(g1.toString().equals("{" + g1.getGroupementId() + "}"));

      final Groupement g2 = new Groupement();
      assertTrue(g2.toString().equals("{Empty Groupement}"));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      //On boucle sur les 16 possibilités
      for(int i = 0; i < Math.pow(2, 4); i++){
         final Groupement groupement1 = new Groupement();
         final Groupement groupement2 = new Groupement();
         String operateur = null;
         if(i >= 8){
            operateur = "and";
         }
         groupement1.setOperateur(operateur);
         groupement2.setOperateur(operateur);
         int toTest = i % 8;
         Critere critere1 = null;
         if(i / 4 >= 0){
            critere1 = critereDao.findById(4);
         }
         groupement1.setCritere1(critere1);
         groupement2.setCritere1(critere1);
         toTest = i % 4;
         Critere critere2 = null;
         if(toTest >= 2){
            critere2 = critereDao.findById(2);
         }
         groupement1.setCritere2(critere2);
         groupement2.setCritere2(critere2);
         toTest = toTest % 2;
         Groupement parent = null;
         if(toTest > 0){
            parent = groupementDao.findById(1);
         }
         groupement1.setParent(parent);
         groupement2.setParent(parent);
         //On compare les 2 groupements
         assertTrue(groupement1.equals(groupement2));
      }
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      //On boucle sur les 16 possibilités
      for(int i = 0; i < Math.pow(2, 4); i++){
         final Groupement groupement = new Groupement();
         int hash = 7;
         String operateur = null;
         int hashOperateur = 0;
         if(i >= 8){
            operateur = "and";
            hashOperateur = operateur.hashCode();
         }
         int toTest = i % 8;
         Critere critere1 = null;
         int hashCritere1 = 0;
         if(toTest >= 4){
            critere1 = critereDao.findById(3);
            hashCritere1 = critere1.hashCode();
         }
         toTest = toTest % 4;
         Critere critere2 = null;
         int hashCritere2 = 0;
         if(toTest >= 2){
            critere2 = critereDao.findById(1);
            hashCritere2 = critere2.hashCode();
         }
         toTest = i % 2;
         Groupement parent = null;
         int hashParent = 0;
         if(toTest > 0){
            parent = groupementDao.findById(3);
            hashParent = parent.hashCode();
         }
         hash = 31 * hash + hashOperateur;
         hash = 31 * hash + hashCritere1;
         hash = 31 * hash + hashCritere2;
         hash = 31 * hash + hashParent;
         groupement.setOperateur(operateur);
         groupement.setCritere1(critere1);
         groupement.setCritere2(critere2);
         groupement.setParent(parent);
         //On vérifie que le hashCode est bon
         assertTrue(groupement.hashCode() == hash);
         assertTrue(groupement.hashCode() == hash);
         assertTrue(groupement.hashCode() == hash);
      }
   }
}
