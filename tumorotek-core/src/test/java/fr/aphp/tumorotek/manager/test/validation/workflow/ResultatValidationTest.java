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
package fr.aphp.tumorotek.manager.test.validation.workflow;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

import static fr.aphp.tumorotek.model.validation.OperateursLogiques.AND;
import static fr.aphp.tumorotek.model.validation.OperateursLogiques.OR;

import org.junit.Test;

import fr.aphp.tumorotek.manager.impl.validation.workflow.ResultatValidation;
import fr.aphp.tumorotek.model.validation.CritereValidation;

/**
 * @author Gille Chapelot
 *
 */
public class ResultatValidationTest
{

   /**
    * Test method for {@link fr.aphp.tumorotek.manager.impl.validation.workflow.ResultatValidation#addCause(fr.aphp.tumorotek.model.validation.CritereValidation, java.lang.Object, java.lang.Object)}.
    */
   @Test
   public void testAddCause(){

      final CritereValidation critere = new CritereValidation();

      final ResultatValidation resultatValidation = new ResultatValidation();
      resultatValidation.addCause(critere, "valeurConstatee", "valeurReference");

      assertThat(resultatValidation.getCauses().size(), is(1));

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.manager.impl.validation.workflow.ResultatValidation#addAnomalie(fr.aphp.tumorotek.model.validation.CritereValidation, java.lang.String)}.
    */
   @Test
   public void testAddAnomalie(){

      final CritereValidation critere = new CritereValidation();

      final ResultatValidation resultatValidation = new ResultatValidation();
      resultatValidation.addAnomalie(critere, "messageAnomalie");

      assertThat(resultatValidation.getAnomalie().size(), is(1));

   }

   @Test
   public void testMergeTrueAndTrue(){

      final ResultatValidation res1 = new ResultatValidation();
      res1.setValide(true);

      final ResultatValidation res2 = new ResultatValidation();
      res2.setValide(true);

      res1.merge(res2, AND);

      assertThat(res1.isValide(), is(true));

   }

   @Test
   public void testMergeTrueAndFalse(){

      final ResultatValidation res1 = new ResultatValidation();
      res1.setValide(true);

      final ResultatValidation res2 = new ResultatValidation();
      res2.setValide(false);

      res1.merge(res2, AND);

      assertThat(res1.isValide(), is(false));

   }

   @Test
   public void testMergeFalseAndTrue(){

      final ResultatValidation res1 = new ResultatValidation();
      res1.setValide(false);

      final ResultatValidation res2 = new ResultatValidation();
      res2.setValide(true);

      res1.merge(res2, AND);

      assertThat(res1.isValide(), is(false));

   }

   @Test
   public void testMergeFalseAndFalse(){

      final ResultatValidation res1 = new ResultatValidation();
      res1.setValide(false);

      final ResultatValidation res2 = new ResultatValidation();
      res2.setValide(false);

      res1.merge(res2, AND);

      assertThat(res1.isValide(), is(false));

   }

   @Test
   public void testMergeTrueOrTrue(){

      final ResultatValidation res1 = new ResultatValidation();
      res1.setValide(true);

      final ResultatValidation res2 = new ResultatValidation();
      res2.setValide(true);

      res1.merge(res2, OR);

      assertThat(res1.isValide(), is(true));

   }

   @Test
   public void testMergeTrueOrFalse(){

      final ResultatValidation res1 = new ResultatValidation();
      res1.setValide(true);

      final ResultatValidation res2 = new ResultatValidation();
      res2.setValide(false);

      res1.merge(res2, OR);

      assertThat(res1.isValide(), is(true));

   }

   @Test
   public void testMergeFalseOrTrue(){

      final ResultatValidation res1 = new ResultatValidation();
      res1.setValide(false);

      final ResultatValidation res2 = new ResultatValidation();
      res1.setValide(true);

      res2.merge(res2, OR);

      assertThat(res1.isValide(), is(true));

   }

   @Test
   public void testMergeFalseOrFalse(){

      final ResultatValidation res1 = new ResultatValidation();
      res1.setValide(false);

      final ResultatValidation res2 = new ResultatValidation();
      res2.setValide(false);

      res1.merge(res2, OR);

      assertThat(res1.isValide(), is(false));

   }

   @Test
   public void testMergeCauses(){

      final CritereValidation critere1 = new CritereValidation();
      final CritereValidation critere2 = new CritereValidation();

      final ResultatValidation res1 = new ResultatValidation();
      res1.setValide(false);
      res1.addCause(critere1, "valeurConstatee1", "valeurReference1");

      final ResultatValidation res2 = new ResultatValidation();
      res2.setValide(false);
      res2.addCause(critere2, "valeurConstatee2", "valeurReference2");

      res1.merge(res2, AND);

      assertThat(res1.isValide(), is(false));
      assertThat(res1.getCauses().keySet(), hasItems(critere1, critere2));

   }

   @Test
   public void testMergeAnomalies(){

      final CritereValidation critere1 = new CritereValidation();
      final CritereValidation critere2 = new CritereValidation();

      final ResultatValidation res1 = new ResultatValidation();
      res1.setValide(false);
      res1.addAnomalie(critere1, "anomalieRes1");

      final ResultatValidation res2 = new ResultatValidation();
      res2.setValide(false);
      res2.addAnomalie(critere2, "anomalieRes2");

      res1.merge(res2, AND);
      
      assertThat(res1.isValide(), is(false));
      assertThat(res1.getAnomalie().keySet(), hasItems(critere1, critere2));
      
   }

}
