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
package fr.aphp.tumorotek.dao.test.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.validation.ActionDao;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.validation.Action;
import fr.aphp.tumorotek.model.validation.ActionType;
import fr.aphp.tumorotek.test.AbstractInMemoryTests;

/**
 * @author Gille Chapelot
 *
 */
@Transactional
public class ActionDaoTest extends AbstractInMemoryTests
{

   @Autowired
   private ActionDao actionDao;

   @Autowired
   private EntiteDao entiteDao;

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.validation.ActionDao#findByLibelle(java.lang.String)}.
    */
   @Test
   public void testFindByLibelle(){

      final String libelleTest = "Action 1";

      final List<Action> fromDB = actionDao.findByLibelle(libelleTest);

      assertThat(fromDB.size(), not(is(0)));

      for(final Action actionFromDB : fromDB){
         assertThat(actionFromDB.getLibelle(), is(libelleTest));
      }

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.validation.ActionDao#findByEntiteAndType(fr.aphp.tumorotek.model.systeme.Entite, fr.aphp.tumorotek.model.validation.ActionType)}.
    */
   @Test
   public void testFindByEntiteAndType(){

      final Entite uneEntite = entiteDao.findById(1);

      final List<Action> fromDB = actionDao.findByEntiteAndType(uneEntite, ActionType.VALIDATION);

      //Contrainte d'unicité sur entite + type -> on ne peut avoir que 1 résultat
      assertThat(fromDB.size(), is(1));

      for(final Action actionFromDB : fromDB){
         assertThat(actionFromDB.getEntite(), is(uneEntite));
         assertThat(actionFromDB.getTypeAction(), is(ActionType.VALIDATION));
      }

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#createObject(java.lang.Object)}.
    */
   @Test
   public void testCreateObject(){

      final Entite entite = entiteDao.findById(4);

      final Action action = new Action();
      action.setLibelle("action1");
      action.setEntite(entite);
      action.setTypeAction(ActionType.VALIDATION);

      actionDao.createObject(action);

      assertThat(action.getId(), is(notNullValue()));

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#findAll()}.
    */
   @Test
   public void testFindAll(){
      
      final List<Action> allActions = actionDao.findAll();
      assertThat(allActions.size(), is(3));
      
   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#updateObject(java.lang.Object)}.
    */
   @Test
   public void testUpdateObject(){

      final String newLibelle = "nouveau libelle";

      final Action action = actionDao.findById(1);

      action.setLibelle(newLibelle);

      actionDao.updateObject(action);

      final Action fromDB = actionDao.findById(1);

      assertThat(fromDB.getLibelle(), is(newLibelle));

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#removeObject(java.io.Serializable)}.
    */
   @Test
   public void testRemoveObject(){

      final Action action = actionDao.findById(1);
      actionDao.removeObject(action.getId());

      final Action fromDB = actionDao.findById(1);
      
      assertThat(fromDB, is(nullValue()));
      
   }

}
