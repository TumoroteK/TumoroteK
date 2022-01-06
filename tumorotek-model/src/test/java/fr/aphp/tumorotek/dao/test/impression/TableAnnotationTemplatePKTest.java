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



import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplatePK;
import fr.aphp.tumorotek.model.impression.Template;

/**
 *
 * Classe de test pour le bean du domaine TableAnnotationTemplatePK.
 *
 * @author Pierre Ventadour.
 * @version 22/07/2010
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class TableAnnotationTemplatePKTest extends AbstractDaoTest
{


   @Autowired
 TemplateDao templateDao;

   @Autowired
 TableAnnotationDao tableAnnotationDao;

   public TableAnnotationTemplatePKTest(){

   }

   @Test
public void setTemplateDao(final TemplateDao tDao){
      this.templateDao = tDao;
   }

   @Test
public void setTableAnnotationDao(final TableAnnotationDao tDao){
      this.tableAnnotationDao = tDao;
   }

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   @Test
public void testEquals() throws ParseException{
      final TableAnnotationTemplatePK pk1 = new TableAnnotationTemplatePK();
      final TableAnnotationTemplatePK pk2 = new TableAnnotationTemplatePK();

      // L'objet 1 n'est pas égal à null
      assertFalse(pk1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(pk1.equals(pk1));

      /*null*/
      assertTrue(pk1.equals(pk2));
      assertTrue(pk2.equals(pk1));

      populateClefsToTestEqualsAndHashCode();

      final Categorie c3 = new Categorie();
      assertFalse(pk1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    * @throws ParseException 
    */
   @Test
public void testHashCode() throws ParseException{
      final TableAnnotationTemplatePK pk1 = new TableAnnotationTemplatePK();
      final TableAnnotationTemplatePK pk2 = new TableAnnotationTemplatePK();

      /*null*/
      assertTrue(pk1.hashCode() == pk2.hashCode());

      populateClefsToTestEqualsAndHashCode();

      // un même objet garde le même hashcode dans le temps
      final int hash = pk1.hashCode();
      assertTrue(hash == pk1.hashCode());
      assertTrue(hash == pk1.hashCode());
      assertTrue(hash == pk1.hashCode());
      assertTrue(hash == pk1.hashCode());
   }

   @Autowired
 void populateClefsToTestEqualsAndHashCode() throws ParseException{

      final TableAnnotation ta1 = tableAnnotationDao.findById(1);
      final TableAnnotation ta2 = tableAnnotationDao.findById(2);
      final TableAnnotation ta3 = tableAnnotationDao.findById(1);
      final TableAnnotation[] tables = new TableAnnotation[] {null, ta1, ta2, ta3};
      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);
      final Template t3 = templateDao.findById(1);
      final Template[] templates = new Template[] {null, t1, t2, t3};

      final TableAnnotationTemplatePK pk1 = new TableAnnotationTemplatePK();
      final TableAnnotationTemplatePK pk2 = new TableAnnotationTemplatePK();

      for(int i = 0; i < tables.length; i++){
         for(int j = 0; j < templates.length; j++){
            for(int k = 0; k < tables.length; k++){
               for(int l = 0; l < templates.length; l++){

                  pk1.setTableAnnotation(tables[i]);
                  pk1.setTemplate(templates[j]);

                  pk2.setTableAnnotation(tables[k]);
                  pk2.setTemplate(templates[l]);

                  if(((i == k) || (i + k == 4)) && ((j == l) || (j + l == 4))){
                     assertTrue(pk1.equals(pk2));
                     assertTrue(pk1.hashCode() == pk2.hashCode());
                  }else{
                     assertFalse(pk1.equals(pk2));
                  }
               }
            }
         }
      }
   }

   /**
    * Test la méthode toString.
    */
   @Test
public void testToString(){
      final TableAnnotation ta1 = tableAnnotationDao.findById(1);
      final Template t1 = templateDao.findById(1);
      final TableAnnotationTemplatePK pk1 = new TableAnnotationTemplatePK();
      pk1.setTableAnnotation(ta1);
      pk1.setTemplate(t1);

      assertTrue(pk1.toString().equals(
         "{" + pk1.getTemplate().toString() + " (Template), " + pk1.getTableAnnotation().toString() + " (TableAnnotation)}"));

      pk1.setTableAnnotation(null);
      assertTrue(pk1.toString().equals("{Empty TableAnnotationTemplatePK}"));
      pk1.setTableAnnotation(ta1);

      pk1.setTemplate(null);
      assertTrue(pk1.toString().equals("{Empty TableAnnotationTemplatePK}"));
      pk1.setTemplate(t1);

      pk1.setTableAnnotation(null);
      pk1.setTemplate(null);
      assertTrue(pk1.toString().equals("{Empty TableAnnotationTemplatePK}"));

      final TableAnnotationTemplatePK pk2 = new TableAnnotationTemplatePK();
      assertTrue(pk2.toString().equals("{Empty TableAnnotationTemplatePK}"));
   }

}
