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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.interfacage.DossierExterneDao;
import fr.aphp.tumorotek.dao.interfacage.EmetteurDao;
import fr.aphp.tumorotek.manager.interfacage.BlocExterneManager;
import fr.aphp.tumorotek.manager.interfacage.DossierExterneManager;
import fr.aphp.tumorotek.manager.interfacage.ValeurExterneManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;

/**
 *
 * Classe de test pour le manager DossierExterne.
 * Classe créée le 07/10/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.2.3-genno
 *
 */
public class DossierExterneManagerTest extends AbstractManagerTest4
{

   @Autowired
   private DossierExterneManager dossierExterneManager;

   @Autowired
   private DossierExterneDao dossierExterneDao;

   @Autowired
   private BlocExterneManager blocExterneManager;

   @Autowired
   private EmetteurDao emetteurDao;

   @Autowired
   private ValeurExterneManager valeurExterneManager;
   //	
   //	@SuppressWarnings("deprecation")
   //	public DossierExterneManagerTest() {
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
      final DossierExterne m = dossierExterneManager.findByIdManager(1);
      assertNotNull(m);

      final DossierExterne mNull = dossierExterneManager.findByIdManager(100);
      assertNull(mNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<DossierExterne> list = dossierExterneManager.findAllObjectsManager();
      assertTrue(list.size() == 6);
   }

   /**
    * Test la méthode findByEmetteurManager.
    */
   @Test
   public void testFindByEmetteurManager(){
      final Emetteur e1 = emetteurDao.findById(1);
      final Emetteur e2 = emetteurDao.findById(2);

      List<DossierExterne> list = dossierExterneManager.findByEmetteurManager(e2);
      assertTrue(list.size() == 4);

      list = dossierExterneManager.findByEmetteurManager(e1);
      assertTrue(list.size() == 0);

      list = dossierExterneManager.findByEmetteurManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByEmetteurAndIdentificationManager.
    */
   @Test
   public void testFindByEmetteurAndIdentificationManager(){
      final Emetteur e1 = emetteurDao.findById(1);
      final Emetteur e2 = emetteurDao.findById(2);
      final String num1 = "X459GCT";
      final String num2 = "TTTTTTT";

      List<DossierExterne> list = dossierExterneManager.findByEmetteurAndIdentificationManager(e2, num1);
      assertTrue(list.size() == 1);

      list = dossierExterneManager.findByEmetteurAndIdentificationManager(e2, num2);
      assertTrue(list.size() == 0);

      list = dossierExterneManager.findByEmetteurAndIdentificationManager(e1, num1);
      assertTrue(list.size() == 0);

      list = dossierExterneManager.findByEmetteurAndIdentificationManager(e1, num2);
      assertTrue(list.size() == 0);

      list = dossierExterneManager.findByEmetteurAndIdentificationManager(null, num1);
      assertTrue(list.size() == 0);

      list = dossierExterneManager.findByEmetteurAndIdentificationManager(e2, null);
      assertTrue(list.size() == 0);

      list = dossierExterneManager.findByEmetteurAndIdentificationManager(null, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test de la méthode findByEmetteurInListAndIdentificationManager.
    */
   @Test
   public void testFindByEmetteurInListAndIdentificationManager(){
      List<Emetteur> emetteurs = new ArrayList<>();
      final Emetteur e2 = emetteurDao.findById(2);
      emetteurs.add(e2);
      List<DossierExterne> liste = dossierExterneManager.findByEmetteurInListAndIdentificationManager(emetteurs, "X459GCT");
      assertTrue(liste.size() == 1);

      final Emetteur e3 = emetteurDao.findById(3);
      emetteurs.add(e3);
      liste = dossierExterneManager.findByEmetteurInListAndIdentificationManager(emetteurs, "X459GCT");
      assertTrue(liste.size() == 2);

      liste = dossierExterneManager.findByEmetteurInListAndIdentificationManager(emetteurs, "kljsvoi");
      assertTrue(liste.size() == 0);

      final Emetteur e1 = emetteurDao.findById(1);
      emetteurs = new ArrayList<>();
      emetteurs.add(e1);
      liste = dossierExterneManager.findByEmetteurInListAndIdentificationManager(emetteurs, "758910000");
      assertTrue(liste.size() == 0);

      liste = dossierExterneManager.findByEmetteurInListAndIdentificationManager(null, "758910000");
      assertTrue(liste.size() == 0);

      liste = dossierExterneManager.findByEmetteurInListAndIdentificationManager(new ArrayList<Emetteur>(), "758910000");
      assertTrue(liste.size() == 0);

      liste = dossierExterneManager.findByEmetteurInListAndIdentificationManager(emetteurs, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByIdentificationManager().
    */
   @Test
   public void testFindByIdentificationManager(){
      List<DossierExterne> liste = dossierExterneManager.findByIdentificationManager("758910000");
      assertTrue(liste.size() == 1);

      liste = dossierExterneManager.findByIdentificationManager("kljsvoi");
      assertTrue(liste.size() == 0);

      liste = dossierExterneManager.findByIdentificationManager("X459GCT");
      assertTrue(liste.size() == 2);

      liste = dossierExterneManager.findByIdentificationManager(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      final Emetteur e1 = emetteurDao.findById(1);
      final Emetteur e2 = emetteurDao.findById(2);
      final String num1 = "X459GCT";
      final String num2 = "TTTTTTT";

      final DossierExterne de = new DossierExterne();
      de.setEmetteur(e2);
      de.setIdentificationDossier(num1);
      assertTrue(dossierExterneManager.findDoublonManager(de));

      de.setEmetteur(e1);
      assertFalse(dossierExterneManager.findDoublonManager(de));
      de.setEmetteur(e2);

      de.setIdentificationDossier(num2);
      assertFalse(dossierExterneManager.findDoublonManager(de));
   }

   @Test
   public void testValidateValeurExterneManager(){
      final DossierExterne de = new DossierExterne();
      final Emetteur e1 = emetteurDao.findById(1);

      de.setIdentificationDossier("ID");
      boolean catched = false;
      // on test l'insertion avec un emetteur null
      try{
         dossierExterneManager.validateDossierExterneManager(de, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on teste la validation de la valeur
      final String[] emptyValues = new String[] {"", "  ", "%¬ ↓»üß*d", createOverLength(100)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            de.setIdentificationDossier(emptyValues[i]);
            dossierExterneManager.validateDossierExterneManager(de, e1, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      de.setIdentificationDossier("ID");

      // validation des blocs
      List<BlocExterne> blocs = new ArrayList<>();
      final BlocExterne b1 = new BlocExterne();
      b1.setEntiteId(1);
      b1.setOrdre(1);
      final BlocExterne b2 = new BlocExterne();
      b2.setEntiteId(2);
      b2.setOrdre(2);
      final BlocExterne b3 = new BlocExterne();
      b3.setEntiteId(null);
      b3.setOrdre(3);
      blocs.add(b1);
      blocs.add(b2);
      blocs.add(b3);
      try{
         dossierExterneManager.validateDossierExterneManager(de, e1, blocs, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // validation des valeurs
      blocs = new ArrayList<>();
      b3.setEntiteId(3);
      blocs.add(b1);
      blocs.add(b2);
      blocs.add(b3);
      final ValeurExterne ve1 = new ValeurExterne();
      ve1.setValeur("VAL");
      ve1.setChampEntiteId(3);
      final ValeurExterne ve2 = new ValeurExterne();
      ve2.setValeur("VAL2");
      ve2.setChampEntiteId(5);
      final List<ValeurExterne> vals1 = new ArrayList<>();
      vals1.add(ve1);
      vals1.add(ve2);
      final ValeurExterne ve3 = new ValeurExterne();
      ve3.setValeur("VAL3");
      final List<ValeurExterne> vals2 = new ArrayList<>();
      vals2.add(ve3);
      final Hashtable<BlocExterne, List<ValeurExterne>> valeurs = new Hashtable<>();
      valeurs.put(b1, vals1);
      valeurs.put(b3, vals2);
      try{
         dossierExterneManager.validateDossierExterneManager(de, e1, blocs, valeurs);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidMultipleAssociationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
   }

   @Test
   public void testCrud() throws ParseException{
      final Emetteur e1 = emetteurDao.findById(1);

      final Integer nb = dossierExterneManager.findAllObjectsManager().size();
      final Integer nbB = blocExterneManager.findAllObjectsManager().size();
      final Integer nbV = valeurExterneManager.findAllObjectsManager().size();
      // insertion sans blocs
      final DossierExterne de1 = new DossierExterne();
      de1.setIdentificationDossier("ID");
      de1.setOperation("TEST");
      final Calendar date = Calendar.getInstance();
      date.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/2006 12:12:23"));
      de1.setDateOperation(date);
      dossierExterneManager.createObjectManager(de1, e1, null, null, 20);
      assertTrue(dossierExterneManager.findAllObjectsManager().size() == nb + 1);
      final Integer id1 = de1.getDossierExterneId();
      final DossierExterne dTest1 = dossierExterneManager.findByIdManager(id1);
      assertNotNull(dTest1);
      assertTrue(dTest1.getEmetteur().equals(e1));
      assertTrue(dTest1.getIdentificationDossier().equals("ID"));
      assertTrue(dTest1.getDateOperation().equals(date));
      assertTrue(dTest1.getOperation().equals("TEST"));

      // insertion avec blocs mais sans valeurs
      final DossierExterne de2 = new DossierExterne();
      de2.setIdentificationDossier("ID2");
      de2.setOperation("TEST2");
      List<BlocExterne> blocs = new ArrayList<>();
      final BlocExterne b1 = new BlocExterne();
      b1.setEntiteId(1);
      b1.setOrdre(1);
      final BlocExterne b2 = new BlocExterne();
      b2.setEntiteId(2);
      b2.setOrdre(2);
      blocs.add(b1);
      blocs.add(b2);
      dossierExterneManager.createObjectManager(de2, e1, blocs, null, 20);
      assertTrue(dossierExterneManager.findAllObjectsManager().size() == nb + 2);
      assertTrue(blocExterneManager.findAllObjectsManager().size() == nbB + 2);
      final Integer id2 = de2.getDossierExterneId();
      final DossierExterne dTest2 = dossierExterneManager.findByIdManager(id2);
      assertNotNull(dTest2);
      assertTrue(blocExterneManager.findByDossierExterneManager(dTest2).size() == 2);
      assertTrue(blocExterneManager.findByDossierExterneManager(dTest2).contains(b1));
      assertTrue(blocExterneManager.findByDossierExterneManager(dTest2).contains(b2));

      // insertion avec blocs et valeurs
      final DossierExterne de3 = new DossierExterne();
      de3.setIdentificationDossier("ID3");
      de3.setOperation("TEST3");
      blocs = new ArrayList<>();
      final BlocExterne b3 = new BlocExterne();
      b3.setEntiteId(1);
      b3.setOrdre(1);
      final BlocExterne b4 = new BlocExterne();
      b4.setEntiteId(2);
      b4.setOrdre(2);
      blocs.add(b3);
      blocs.add(b4);
      final ValeurExterne ve1 = new ValeurExterne();
      ve1.setValeur("VAL");
      ve1.setChampEntiteId(3);
      final ValeurExterne ve2 = new ValeurExterne();
      ve2.setValeur("VAL2");
      ve2.setChampEntiteId(5);
      final List<ValeurExterne> vals1 = new ArrayList<>();
      vals1.add(ve1);
      vals1.add(ve2);
      Hashtable<BlocExterne, List<ValeurExterne>> valeurs = new Hashtable<>();
      valeurs.put(b3, vals1);
      dossierExterneManager.createObjectManager(de3, e1, blocs, valeurs, 20);
      assertTrue(dossierExterneManager.findAllObjectsManager().size() == nb + 3);
      assertTrue(blocExterneManager.findAllObjectsManager().size() == nbB + 4);
      assertTrue(valeurExterneManager.findAllObjectsManager().size() == nbV + 2);
      final Integer id3 = de3.getDossierExterneId();
      final DossierExterne dTest3 = dossierExterneManager.findByIdManager(id3);
      assertNotNull(dTest3);
      assertTrue(blocExterneManager.findByDossierExterneManager(dTest3).size() == 2);
      final BlocExterne bTest = blocExterneManager.findByDossierExterneManager(dTest3).get(0);
      assertTrue(valeurExterneManager.findByBlocExterneManager(bTest).size() == 2);
      assertTrue(valeurExterneManager.findByBlocExterneManager(bTest).contains(ve1));
      assertTrue(valeurExterneManager.findByBlocExterneManager(bTest).contains(ve2));

      // Insertion d'un doublon
      final DossierExterne de4 = new DossierExterne();
      de4.setIdentificationDossier("ID3");
      de4.setOperation("TEST3");
      blocs = new ArrayList<>();
      final BlocExterne b5 = new BlocExterne();
      b5.setEntiteId(3);
      b5.setOrdre(1);
      blocs.add(b5);
      final ValeurExterne ve3 = new ValeurExterne();
      ve3.setValeur("TTT");
      ve3.setChampEntiteId(8);
      final List<ValeurExterne> vals3 = new ArrayList<>();
      vals3.add(ve3);
      valeurs = new Hashtable<>();
      valeurs.put(b5, vals3);
      dossierExterneManager.createObjectManager(de4, e1, blocs, valeurs, 20);
      assertTrue(dossierExterneManager.findAllObjectsManager().size() == nb + 3);
      assertTrue(blocExterneManager.findAllObjectsManager().size() == nbB + 3);
      assertTrue(valeurExterneManager.findAllObjectsManager().size() == nbV + 1);
      final Integer id4 = de4.getDossierExterneId();
      final DossierExterne dTest4 = dossierExterneManager.findByIdManager(id4);
      assertNotNull(dTest4);
      assertTrue(blocExterneManager.findByDossierExterneManager(dTest4).size() == 1);
      final BlocExterne bTest2 = blocExterneManager.findByDossierExterneManager(dTest4).get(0);
      assertTrue(valeurExterneManager.findByBlocExterneManager(bTest2).size() == 1);
      assertTrue(valeurExterneManager.findByBlocExterneManager(bTest2).contains(ve3));

      // suppressions
      dossierExterneManager.removeObjectManager(dTest1);
      dossierExterneManager.removeObjectManager(dTest2);
      dossierExterneManager.removeObjectManager(dTest4);
      dossierExterneManager.removeObjectManager(null);
      dossierExterneManager.removeObjectManager(new DossierExterne());
      assertTrue(dossierExterneManager.findAllObjectsManager().size() == nb);
      assertTrue(blocExterneManager.findAllObjectsManager().size() == nbB);
      assertTrue(valeurExterneManager.findAllObjectsManager().size() == nbV);
   }

   @Test
   public void testFirstInFirstOut() throws ParseException, NoSuchFieldException{
      final int totD = dossierExterneManager.findAllObjectsManager().size();
      final int totB = blocExterneManager.findAllObjectsManager().size();
      final int totV = valeurExterneManager.findAllObjectsManager().size();

      // creation Object qui sera supprime
      // insertion avec blocs et valeurs
      final DossierExterne de3 = new DossierExterne();
      de3.setIdentificationDossier("ID3");
      de3.setOperation("TEST3");
      final Calendar date = Calendar.getInstance();
      date.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/2006 12:12:23"));
      de3.setDateOperation(date);
      final List<BlocExterne> blocs = new ArrayList<>();
      final BlocExterne b3 = new BlocExterne();
      b3.setEntiteId(1);
      b3.setOrdre(1);
      final BlocExterne b4 = new BlocExterne();
      b4.setEntiteId(2);
      b4.setOrdre(2);
      blocs.add(b3);
      blocs.add(b4);
      final ValeurExterne ve1 = new ValeurExterne();
      ve1.setValeur("VAL");
      ve1.setChampEntiteId(3);
      final ValeurExterne ve2 = new ValeurExterne();
      ve2.setValeur("VAL2");
      ve2.setChampEntiteId(5);
      final List<ValeurExterne> vals1 = new ArrayList<>();
      vals1.add(ve1);
      vals1.add(ve2);
      final Hashtable<BlocExterne, List<ValeurExterne>> valeurs = new Hashtable<>();
      valeurs.put(b3, vals1);
      dossierExterneManager.createObjectManager(de3, emetteurDao.findById(1), blocs, valeurs, 7);
      assertTrue(dossierExterneManager.findAllObjectsManager().size() == totD + 1); // 7
      assertTrue(blocExterneManager.findAllObjectsManager().size() == totB + 2);
      assertTrue(valeurExterneManager.findAllObjectsManager().size() == totV + 2);
      final Integer id3 = de3.getDossierExterneId();
      final DossierExterne d3 = dossierExterneManager.findByIdManager(id3);

      assertTrue(dossierExterneDao.findFirst().get(0).equals(d3));

      //insertion engendrant FIRST OUT
      final DossierExterne de1 = new DossierExterne();
      de1.setIdentificationDossier("ID");
      de1.setOperation("TEST");
      date.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/2017 12:12:23"));
      de1.setDateOperation(date);
      dossierExterneManager.createObjectManager(de1, emetteurDao.findById(1), null, null, 7);

      assertTrue(dossierExterneManager.findAllObjectsManager().size() == 7);
      assertTrue(dossierExterneDao.findFirst().get(0).getDossierExterneId() == 6);

      assertTrue(dossierExterneManager.findByIdManager(id3) == null);
      assertTrue(dossierExterneManager.findByIdentificationManager("ID3").isEmpty());

      // clean up
      dossierExterneManager.removeObjectManager(dossierExterneManager.findByIdentificationManager("ID").get(0));

      testFindAll();
   }

   /**
    * @since 2.2.3-genno
    */
   @Test
   public void testFindChildrenByEmetteurValeurManager(){
      Emetteur emet = emetteurDao.findById(2);
      List<DossierExterne> dos = dossierExterneManager.findChildrenByEmetteurValeurManager(emet, 23, "%75891000%");
      assertTrue(dos.isEmpty());

      // creation d'un dossier enfant 758910000 pour le test
      DossierExterne der4 = new DossierExterne();
      der4.setIdentificationDossier("NEW_DERIVE_004");
      der4.setDateOperation(Calendar.getInstance());
      der4.setOperation("D");
      der4.setEntiteId(8);

      List<BlocExterne> blocsDer4 = new ArrayList<BlocExterne>();
      Hashtable<BlocExterne, List<ValeurExterne>> valeursDer4Map = new Hashtable<BlocExterne, List<ValeurExterne>>();

      final ValeurExterne codePrelDer4 = new ValeurExterne();
      codePrelDer4.setValeur("758910000");
      codePrelDer4.setChampEntiteId(23);
      List<ValeurExterne> valeursDer4Prel = new ArrayList<ValeurExterne>();
      valeursDer4Prel.add(codePrelDer4);

      final BlocExterne bPrelDer4 = new BlocExterne();
      bPrelDer4.setEntiteId(2);
      bPrelDer4.setOrdre(1);
      blocsDer4.add(bPrelDer4);
      valeursDer4Map.put(bPrelDer4, valeursDer4Prel);

      final ValeurExterne codeDer4 = new ValeurExterne();
      codeDer4.setValeur("NEW_DERIVE_004");
      codeDer4.setChampEntiteId(79);
      final ValeurExterne typeDer4 = new ValeurExterne();
      typeDer4.setValeur("ARN");
      typeDer4.setChampEntiteId(78);
      List<ValeurExterne> valeursDer4 = new ArrayList<ValeurExterne>();
      valeursDer4.add(codeDer4);
      valeursDer4.add(typeDer4);

      final BlocExterne bDer4 = new BlocExterne();
      bDer4.setEntiteId(8);
      bDer4.setOrdre(2);
      blocsDer4.add(bDer4);
      valeursDer4Map.put(bDer4, valeursDer4);

      dossierExterneManager.createObjectManager(der4, emetteurDao.findById(2), blocsDer4, valeursDer4Map, 2000);

      dos = dossierExterneManager.findChildrenByEmetteurValeurManager(emet, 23, "%75891000%");
      assertTrue(dos.size() == 1);
      assertTrue(dos.get(0).getIdentificationDossier().equals("NEW_DERIVE_004"));

      dos = dossierExterneManager.findChildrenByEmetteurValeurManager(emet, 23, "%Add%");
      assertTrue(dos.isEmpty());

      dos = dossierExterneManager.findChildrenByEmetteurValeurManager(emet, null, "%test%");
      assertTrue(dos.isEmpty());

      dos = dossierExterneManager.findChildrenByEmetteurValeurManager(emet, 23, null);
      assertTrue(dos.isEmpty());

      dos = dossierExterneManager.findChildrenByEmetteurValeurManager(null, 23, "%test%");
      assertTrue(dos.isEmpty());

      // clean up
      dossierExterneManager.removeObjectManager(der4);
   }

   /**
    * @since 2.2.3-genno
    */
   @Test
   public void testFindByEmetteurAndEntiteNullManager(){
      Emetteur emet = emetteurDao.findById(2);
      List<DossierExterne> dos = dossierExterneManager.findByEmetteurAndEntiteNullManager(emet);
      assertTrue(dos.size() == 4);

      dos = dossierExterneManager.findByEmetteurAndEntiteNullManager(emetteurDao.findById(1));
      assertTrue(dos.isEmpty());

      dos = dossierExterneManager.findByEmetteurAndEntiteNullManager(null);
      assertTrue(dos.isEmpty());
   }
}
