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

import fr.aphp.tumorotek.dao.validation.NiveauValidationDao;
import fr.aphp.tumorotek.dao.validation.ValidationDao;
import fr.aphp.tumorotek.model.validation.NiveauValidation;
import fr.aphp.tumorotek.model.validation.Validation;
import fr.aphp.tumorotek.test.AbstractInMemoryTests;

/**
 * @author 7009917
 *
 */
@Transactional
public class ValidationDaoTest extends AbstractInMemoryTests
{

   @Autowired
   private NiveauValidationDao niveauValidationDao;
   
   @Autowired
   private ValidationDao validationDao;

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#createObject(java.lang.Object)}.
    */
   @Test
   public void testCreateObject(){

      final Validation validation = new Validation();
      final NiveauValidation niveau = niveauValidationDao.findById(2);
      
      validation.setNiveauValidation(niveau);
      
      validationDao.createObject(validation);

      assertThat(validation.getId(), is(notNullValue()));
      
   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#findById(java.io.Serializable)}.
    */
   @Test
   public void testFindById(){

      final Validation validation = validationDao.findById(1);

      assertThat(validation, is(notNullValue()));

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#findAll()}.
    */
   @Test
   public void testFindAll(){

      final List<Validation> listeValidations = validationDao.findAll();

      assertThat(listeValidations.size(), is(2));

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#updateObject(java.lang.Object)}.
    */
   @Test
   public void testUpdateObject(){

      final String libelle = "nouveau libelle";

      final Validation validation = validationDao.findById(1);
      validation.setLibelle(libelle);

      validationDao.updateObject(validation);

      final Validation fromDB = validationDao.findById(1);
      
      assertThat(fromDB.getLibelle(), is(libelle));

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.dao.GenericDaoJpa#removeObject(java.io.Serializable)}.
    */
   @Test
   public void testRemoveObject(){

      validationDao.removeObject(1);

      Validation validation = validationDao.findById(1);
      
      assertThat(validation, is(nullValue()));
      

   }

}
