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
package fr.aphp.tumorotek.manager.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.aphp.tumorotek.dao.qualite.FantomeDao;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.model.TKFantomableObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
   locations = {"classpath:applicationContextDaoBase-test-mysql.xml", "classpath:applicationContextManagerBase.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public abstract class AbstractManagerTest4
{

   // nv var
   static{
      System.setProperty("catalina.base", "src/test/resources/tomcat");
   }

   @Autowired
   private FantomeDao fantomeDao;

   @Autowired
   private OperationManager operationManager;

   public OperationManager getOperationManager(){
      return operationManager;
   }

   public FantomeDao getFantomeDao(){
      return fantomeDao;
   }

   public String createOverLength(final int length){
      final StringBuilder sb = new StringBuilder();

      for(int i = 0; i < length + 2; i++){
         sb.append('a');
      }

      return sb.toString();
   }

   /**
    * Generateur de valeurs Integer invalides, négatifs ou extremes 
    * pour la validation.
    * @param over valeur a dépasser
    * @return liste d'Integer invalides
    */
   protected List<Integer> createNegativeAndOverIntegers(final Integer over){
      final List<Integer> list = new ArrayList<>();
      list.add(-1); // negative
      if(over != null){ // cree des 'over values'
         list.add(-over.intValue() - 1);
         list.add(over.intValue() + 1);
      }
      return list;
   }

   /**
    * Generateur de valeurs Float invalides, négatifs ou extremes 
    * pour la validation.
    * @param over valeur a dépasser
    * @return liste Float invalides
    */
   protected List<Float> createNegativeAndOverFloats(final Float over){
      final List<Float> list = new ArrayList<>();
      list.add(new Float(-10.0)); // negative
      if(over != null){ // cree des 'over values'
         list.add(new Float(-over.floatValue() - 1.5));
         list.add(new Float(over.floatValue() + 1.5));
      }
      return list;
   }

   /**
    * Generateur de valeurs String invalides, vides, espaces 
    * ou trop longue pour la validation.
    * @param over valeur a dépasser pour la longueur
    * @return liste String invalides
    */
   protected List<String> createInvalideAndOverStrings(final int over){
      final List<String> list = new ArrayList<>();
      list.add("");
      list.add("  ");
      list.add("aaz{");
      list.add("pojk$");
      if(over > 0){ // cree des 'over values'
         list.add(createOverLength(over));
      }
      return list;
   }

   public void cleanUpFantomes(final List<TKFantomableObject> objs){

      if(objs != null){
         for(int i = 0; i < objs.size(); i++){
            operationManager.removeObjectManager(
               operationManager.findByObjectManager(fantomeDao.findByNom(objs.get(i).getPhantomData()).get(0)).get(0));
         }
      }
      assertEquals(5, fantomeDao.findAll().size());
      assertEquals(19, operationManager.findAllObjectsManager().size());
   }

   //    /**
   //     * Spring will automatically inject the SessionFactory 
   //     * instance on startup.
   //     * Only necessary for Hibernate-backed DAO testing
   //     */
   //     public void setEntityManagerFactory(EntityManagerFactory factory) {
   // 		this.entityManagerFactory = factory;
   // 	}
}
