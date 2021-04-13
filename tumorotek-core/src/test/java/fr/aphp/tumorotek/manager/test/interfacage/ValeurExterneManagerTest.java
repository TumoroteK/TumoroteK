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
package fr.aphp.tumorotek.manager.test.interfacage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.interfacage.BlocExterneDao;
import fr.aphp.tumorotek.dao.interfacage.DossierExterneDao;
import fr.aphp.tumorotek.manager.interfacage.ValeurExterneManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 *
 * Classe de test pour le manager ValeurExterne.
 * Classe créée le 05/10/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ValeurExterneManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ValeurExterneManager valeurExterneManager;
   @Autowired
   private BlocExterneDao blocExterneDao;
   @Autowired
   private DossierExterneDao dossierExterneDao;

   //	@SuppressWarnings("deprecation")
   //	public ValeurExterneManagerTest() {
   //		// pour eviter l'autowiring vers fantomeDao
   //		this.setDependencyCheck(false);
   //	}
   //	
   //	@Override
   //	protected String[] getConfigLocations() {
   //        return new String[]{ "applicationContextManagerBase.xml",
   //       		 "applicationContextDaoBase.xml" };
   //    }

   @Test
   public void testFindById(){
      final ValeurExterne m = valeurExterneManager.findByIdManager(1);
      assertNotNull(m);

      final ValeurExterne mNull = valeurExterneManager.findByIdManager(100);
      assertNull(mNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<ValeurExterne> list = valeurExterneManager.findAllObjectsManager();
      assertTrue(list.size() >= 7);
   }

   /**
    * Test la méthode findByBlocExterneManager.
    */
   @Test
   public void testFindByBlocExterneManager(){
      final BlocExterne b1 = blocExterneDao.findById(1);
      final BlocExterne b3 = blocExterneDao.findById(3);

      List<ValeurExterne> list = valeurExterneManager.findByBlocExterneManager(b1);
      assertTrue(list.size() == 4);

      list = valeurExterneManager.findByBlocExterneManager(b3);
      assertTrue(list.size() == 0);

      list = valeurExterneManager.findByBlocExterneManager(null);
      assertTrue(list.size() == 0);
   }
   
   @Test
   public void testFindByDossierChampEntiteIdAndBlocEntiteIdManager() {
		  DossierExterne dos = dossierExterneDao.findById(4);
	      List<ValeurExterne> liste = valeurExterneManager
	    		  .findByDossierChampEntiteIdAndBlocEntiteIdManager(dos, 44, 2);
	      assertTrue(liste.size() == 1);
	      assertTrue(liste.get(0).getValeur().equals("NDA127896 BI"));

	      liste = valeurExterneManager
	    		  .findByDossierChampEntiteIdAndBlocEntiteIdManager(dos, 230, 3);
	      assertTrue(liste.size() == 1);
	      assertTrue(liste.get(0).getValeur().equals("GHLOJ7F4;GHOTJMF4"));

	      liste = valeurExterneManager
	    		  .findByDossierChampEntiteIdAndBlocEntiteIdManager(dos, 89, 3);
	      assertTrue(liste.isEmpty());
	      
	      liste = valeurExterneManager
	    		  .findByDossierChampEntiteIdAndBlocEntiteIdManager(null, 89, 3);
	      assertTrue(liste.isEmpty());
	      
	      liste = valeurExterneManager
	    		  .findByDossierChampEntiteIdAndBlocEntiteIdManager(dos, null, 3);
	      assertTrue(liste.isEmpty());
	      
	      liste = valeurExterneManager
	    		  .findByDossierChampEntiteIdAndBlocEntiteIdManager(dos, 89, null);
	      assertTrue(liste.isEmpty());
	   }

   /**
    * Test de la méthode getChampEntiteManager.
    */
   @Test
   public void testGetChampEntiteManager(){
      final ValeurExterne v1 = valeurExterneManager.findByIdManager(1);
      ChampEntite ce = valeurExterneManager.getChampEntiteManager(v1);
      assertNotNull(ce);
      assertTrue(ce.getNom().equals("Nom"));

      final ValeurExterne v6 = valeurExterneManager.findByIdManager(6);
      ce = valeurExterneManager.getChampEntiteManager(v6);
      assertNotNull(ce);
      assertTrue(ce.getNom().equals("Code"));

      final ValeurExterne v4 = valeurExterneManager.findByIdManager(4);
      ce = valeurExterneManager.getChampEntiteManager(v4);
      assertNull(ce);

      ce = valeurExterneManager.getChampEntiteManager(null);
      assertNull(ce);
   }

   /**
    * Test de la méthode getChampAnnotationManager.
    */
   @Test
   public void testGetChampAnnotationManager(){
      final ValeurExterne v4 = valeurExterneManager.findByIdManager(4);
      ChampAnnotation ca = valeurExterneManager.getChampAnnotationManager(v4);
      assertNotNull(ca);
      assertTrue(ca.getNom().equals("Alphanum1"));

      final ValeurExterne v1 = valeurExterneManager.findByIdManager(1);
      ca = valeurExterneManager.getChampAnnotationManager(v1);
      assertNull(ca);

      ca = valeurExterneManager.getChampAnnotationManager(null);
      assertNull(ca);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      final Integer ce = 3;
      final Integer ca = null;
      final BlocExterne be1 = blocExterneDao.findById(1);
      final BlocExterne be3 = blocExterneDao.findById(3);

      final ValeurExterne ve = new ValeurExterne();
      ve.setBlocExterne(be1);
      ve.setChampEntiteId(ce);
      ve.setChampAnnotationId(ca);
      assertTrue(valeurExterneManager.findDoublonManager(ve));

      ve.setBlocExterne(be3);
      assertFalse(valeurExterneManager.findDoublonManager(ve));
      ve.setBlocExterne(be1);

      ve.setChampEntiteId(10);
      assertFalse(valeurExterneManager.findDoublonManager(ve));
      ve.setChampEntiteId(3);

      ve.setChampAnnotationId(10);
      assertFalse(valeurExterneManager.findDoublonManager(ve));
      ve.setChampAnnotationId(null);
   }

   @Test
   public void testValidateValeurExterneManager(){
      final BlocExterne be = blocExterneDao.findById(1);
      final ValeurExterne ve = new ValeurExterne();
      ve.setChampEntiteId(10);

      boolean catched = false;
      // on test l'insertion avec un bloc null
      try{
         valeurExterneManager.validateValeurExterneManager(ve, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      ve.setChampEntiteId(null);
      // on test l'insertion avec les 2 champs null
      try{
         valeurExterneManager.validateValeurExterneManager(ve, be);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidMultipleAssociationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      ve.setChampEntiteId(10);
      ve.setChampAnnotationId(5);
      // on test l'insertion avec les 2 champs
      try{
         valeurExterneManager.validateValeurExterneManager(ve, be);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidMultipleAssociationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test si le champentite n'existe pas
      ve.setChampAnnotationId(null);
      ve.setChampEntiteId(500);
      try{
         valeurExterneManager.validateValeurExterneManager(ve, be);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EntiteObjectIdNotExistException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test si le champannotation n'existe pas
      ve.setChampAnnotationId(500);
      ve.setChampEntiteId(null);
      try{
         valeurExterneManager.validateValeurExterneManager(ve, be);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EntiteObjectIdNotExistException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      ve.setChampEntiteId(10);
      ve.setChampAnnotationId(null);
      // on teste la validation de la valeur
      final String[] emptyValues = new String[] {"", "  ", "%¬ ↓»üß*d", createOverLength(250)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            ve.setValeur(emptyValues[i]);
            valeurExterneManager.validateValeurExterneManager(ve, be);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
   }

   @Test
   public void testCrud(){
      final BlocExterne be = blocExterneDao.findById(3);
      final ValeurExterne ve = new ValeurExterne();

      final Integer nb = valeurExterneManager.findAllObjectsManager().size();
      ve.setChampAnnotationId(3);
      boolean catched = false;
      // on test l'insertion avec un bloc null
      try{
         valeurExterneManager.createObjectManager(ve, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(valeurExterneManager.findAllObjectsManager().size() == nb);

      // on test une insertion valide avec une annotation et sans
      // valeur
      ve.setChampEntiteId(null);
      ve.setChampAnnotationId(5);
      valeurExterneManager.createObjectManager(ve, be);
      assertTrue(valeurExterneManager.findAllObjectsManager().size() == nb + 1);
      final Integer id1 = ve.getValeurExterneId();

      final ValeurExterne vTest = valeurExterneManager.findByIdManager(id1);
      assertNotNull(vTest);
      assertTrue(vTest.getBlocExterne().equals(be));
      assertTrue(vTest.getChampAnnotationId().equals(5));
      assertNull(vTest.getChampEntiteId());
      assertNull(vTest.getValeur());

      // on test une insertion valide avec un champ entité et une
      // valeur
      final ValeurExterne ve2 = new ValeurExterne();
      ve2.setValeur("VALEUR");
      ve2.setChampEntiteId(3);
      ve2.setChampAnnotationId(null);
      valeurExterneManager.createObjectManager(ve2, be);
      assertTrue(valeurExterneManager.findAllObjectsManager().size() == nb + 2);
      final Integer id2 = ve2.getValeurExterneId();

      final ValeurExterne vTest2 = valeurExterneManager.findByIdManager(id2);
      assertNotNull(vTest2);
      assertTrue(vTest2.getBlocExterne().equals(be));
      assertTrue(vTest2.getChampEntiteId().equals(3));
      assertNull(vTest2.getChampAnnotationId());
      assertTrue(vTest2.getValeur().equals("VALEUR"));

      // on test une insertion valide en doublon
      final ValeurExterne ve3 = new ValeurExterne();
      ve3.setValeur("VALEUR BIS");
      ve3.setChampEntiteId(3);
      valeurExterneManager.createObjectManager(ve3, be);
      assertTrue(valeurExterneManager.findAllObjectsManager().size() == nb + 2);
      final Integer id3 = ve3.getValeurExterneId();

      final ValeurExterne vTest3 = valeurExterneManager.findByIdManager(id3);
      assertNotNull(vTest3);
      assertTrue(vTest3.getBlocExterne().equals(be));
      assertTrue(vTest3.getChampEntiteId().equals(3));
      assertNull(vTest3.getChampAnnotationId());
      assertTrue(vTest3.getValeur().equals("VALEUR BIS"));

      // Suppression
      valeurExterneManager.removeObjectManager(vTest);
      valeurExterneManager.removeObjectManager(vTest3);
      assertTrue(valeurExterneManager.findAllObjectsManager().size() == nb);

      // Suppressions invalides
      valeurExterneManager.removeObjectManager(null);
      valeurExterneManager.removeObjectManager(new ValeurExterne());
   }

}
