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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.validation.CritereValidationDao;
import fr.aphp.tumorotek.model.validation.CritereValidation;
import fr.aphp.tumorotek.model.validation.OperateursComparaison;
import fr.aphp.tumorotek.test.AbstractInMemoryTests;

/**
 * @author Gille Chapelot
 *
 */
@Transactional
public class CritereValidationDaoTest extends AbstractInMemoryTests
{

   @Autowired
   private CritereValidationDao critereValidationDao;

   @Autowired
   private ChampDao champDao;

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#createObject(java.lang.Object)}.
    */
   @Test
   public void testCreateObject(){

      final CritereValidation critere = new CritereValidation();
      critere.setChamp(champDao.findById(1));
      critere.setOperateur(OperateursComparaison.DIFFERENT);

      critereValidationDao.createObject(critere);

      assertThat(critere.getId(), is(notNullValue()));

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#findById(java.io.Serializable)}.
    */
   @Test
   public void testFindById(){

      final CritereValidation critere = critereValidationDao.findById(1);
      assertThat(critere, is(notNullValue()));

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#findAll()}.
    */
   @Test
   public void testFindAll(){

      final List<CritereValidation> listCriteres = critereValidationDao.findAll();

      assertThat(listCriteres.size(), is(2));

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#updateObject(java.lang.Object)}.
    */
   @Test
   public void testUpdateObject(){

      final String valeurRef = "valeurRef";

      final CritereValidation critere = critereValidationDao.findById(1);

      critere.setValeurRef(valeurRef);

      final CritereValidation fromDB = critereValidationDao.findById(1);

      assertThat(fromDB.getValeurRef(), is(valeurRef));
      
   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#removeObject(java.io.Serializable)}.
    */
   @Test
   public void testRemoveObject(){

      critereValidationDao.removeObject(1);

      final CritereValidation fromDB = critereValidationDao.findById(1);
      
      assertThat(fromDB, is(nullValue()));

   }

}
