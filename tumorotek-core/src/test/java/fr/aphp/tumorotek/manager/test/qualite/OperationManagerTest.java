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
package fr.aphp.tumorotek.manager.test.qualite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager OperationManager.
 * Classe créée le 14/10/09.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class OperationManagerTest extends AbstractManagerTest
{

   /* Managers injectes par Spring*/
   private OperationManager operationManager;
   private OperationTypeDao operationTypeDao;
   private UtilisateurDao utilisateurDao;
   private PrelevementDao prelevementDao;
   private EchantillonDao echantillonDao;
   private EntiteDao entiteDao;

   public OperationManagerTest(){}

   @Override
   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao otDao){
      this.operationTypeDao = otDao;
   }

   public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   public void setPrelevementDao(final PrelevementDao pDao){
      this.prelevementDao = pDao;
   }

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   /**
    * Test la méthode findAllObjectsManager.
    */
   public void testFindAllObjectsManager(){
      final List<Operation> ops = operationManager.findAllObjectsManager();
      assertEquals(19, ops.size());
   }

   /**
    * Test la methode findByUtilisateurManager.
    */
   public void testFindByUtilisateurManager(){
      //teste une recherche fructueuse
      Utilisateur u = utilisateurDao.findById(1);
      List<Operation> ops = operationManager.findByUtilisateurManager(u);
      assertEquals(17, ops.size());
      //teste une recherche infructueuse
      u = utilisateurDao.findById(3);
      ops = operationManager.findByUtilisateurManager(u);
      assertTrue(ops.size() == 0);
      //null recherche
      ops = operationManager.findByUtilisateurManager(null);
      assertTrue(ops.size() == 0);
   }

   /**
    * Test la methode findByObjectManager.
    */
   public void testFindByObjectManager(){
      //teste une recherche fructueuse
      final Prelevement p = prelevementDao.findById(2);
      List<Operation> ops = operationManager.findByObjectManager(p);
      assertTrue(ops.size() == 2);
      //teste une recherche infructueuse
      final Echantillon e = echantillonDao.findById(3);
      ops = operationManager.findByObjectManager(e);
      assertEquals(0, ops.size());
      //null recherche
      ops = operationManager.findByObjectManager(null);
      assertTrue(ops.size() == 0);
   }

   /**
    * Test la methode findByObjectForHistoriqueManager.
    */
   public void testFindByObjectForHistoriqueManager(){
      //teste une recherche fructueuse
      final Prelevement p = prelevementDao.findById(2);
      List<Operation> ops = operationManager.findByObjectForHistoriqueManager(p);
      assertTrue(ops.size() == 2);
      //teste une recherche infructueuse
      final Echantillon e = echantillonDao.findById(3);
      ops = operationManager.findByObjectForHistoriqueManager(e);
      assertEquals(0, ops.size());
      //null recherche
      ops = operationManager.findByObjectForHistoriqueManager(null);
      assertTrue(ops.size() == 0);
   }

   /**
    * Test la methode findDateCreationManager.
    * @throws ParseException 
    */
   public void testFindDateCreationManager() throws ParseException{

      //teste une recherche fructueuse pour un prlvt
      Date date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2008");
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      final Prelevement p = prelevementDao.findById(1);
      final Calendar datePrlvt = operationManager.findDateCreationManager(p);
      assertNotNull(datePrlvt);
      assertTrue(datePrlvt.compareTo(cal) == 0);

      //teste une recherche fructueuse pour un échantillon
      date = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
      cal.setTime(date);
      Echantillon e = echantillonDao.findById(1);
      Calendar dateEchan = operationManager.findDateCreationManager(e);
      assertNotNull(dateEchan);
      assertTrue(dateEchan.compareTo(cal) == 0);

      //teste une recherche infructueuse
      e = echantillonDao.findById(3);
      dateEchan = operationManager.findDateCreationManager(e);
      assertNull(dateEchan);

      //null recherche
      cal = operationManager.findDateCreationManager(null);
      assertNull(cal);
   }

   /**
    * Test la methode findDateCreationManager.
    * @throws ParseException 
    */
   public void testFindOperationCreationManager() throws ParseException{

      //teste une recherche fructueuse pour un prlvt
      Date date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2008");
      final Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      final Prelevement p = prelevementDao.findById(1);
      final Utilisateur u1 = utilisateurDao.findById(1);
      Operation op1 = operationManager.findOperationCreationManager(p);
      assertNotNull(op1);
      assertTrue(op1.getDate().compareTo(cal) == 0);
      assertTrue(op1.getUtilisateur().equals(u1));

      //teste une recherche fructueuse pour un échantillon
      date = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
      cal.setTime(date);
      Echantillon e = echantillonDao.findById(1);
      op1 = operationManager.findOperationCreationManager(e);
      assertNotNull(op1);
      assertTrue(op1.getDate().compareTo(cal) == 0);
      assertTrue(op1.getUtilisateur().equals(u1));

      //teste une recherche infructueuse
      e = echantillonDao.findById(3);
      op1 = operationManager.findOperationCreationManager(e);
      assertNull(op1);

      //null recherche
      op1 = operationManager.findOperationCreationManager(null);
      assertNull(op1);
   }

   /**
    * Teste la méthode findDoublon.
    */
   public void testFindDoublon(){
      //Cree le doublon
      final Utilisateur u = utilisateurDao.findById(2);
      final Operation o1 = operationManager.findByUtilisateurManager(u).get(0);
      final Operation o2 = new Operation();
      o2.setUtilisateur(o1.getUtilisateur());
      o2.setObjetId(o1.getObjetId());
      o2.setEntite(o1.getEntite());
      o2.setOperationType(o1.getOperationType());
      o2.setDate(o1.getDate());
      assertTrue(o2.equals(o1));
      assertTrue(operationManager.findDoublonManager(o2));
   }

   /**
    * Teste les methodes CRUD. 
    * @throws ParseException 
    */
   public void testCRUD() throws ParseException{
      createObjectManagerTest();
      removeObjectManagerTest();
   }

   /**
    * Teste la methode createObjectManager. 
    * @throws ParseException 
    */
   public void createObjectManagerTest() throws ParseException{
      //Insertion nouvel enregistrement
      final Utilisateur u = utilisateurDao.findById(3);
      final Echantillon e = echantillonDao.findById(3);
      final OperationType oType = operationTypeDao.findByNom("Creation").get(0);
      final Operation o1 = new Operation();
      final Date date = new SimpleDateFormat("dd/MM/yyyy").parse("14/10/2009");
      final Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      o1.setDate(cal);
      operationManager.createObjectManager(o1, u, oType, e);
      assertTrue(operationManager.findByObjectManager(e).size() == 1);

      //validation test Type
      final Calendar[] dateValues = new Calendar[] {null, cal};
      final Utilisateur[] utilisateurValues = new Utilisateur[] {null, u};
      final OperationType[] oTypeValues = new OperationType[] {null, oType};
      final Prelevement pNullId = new Prelevement();
      final Object[] objectValues = new Object[] {null, pNullId, e};
      final Operation o2 = new Operation();
      for(int i = 0; i < dateValues.length; i++){
         for(int j = 0; j < utilisateurValues.length; j++){
            for(int k = 0; k < oTypeValues.length; k++){
               for(int l = 0; l < objectValues.length; l++){
                  try{
                     o2.setDate(dateValues[i]);
                     if(i != 1
                        // && j != 1
                        && k != 1 && l != 2){ //car creation valide
                        operationManager.createObjectManager(o2, utilisateurValues[j], oTypeValues[k], objectValues[l]);
                     }
                  }catch(final Exception ex){
                     if(j == 0 || k == 0 || l == 0){
                        assertTrue(ex.getClass().getSimpleName().equals("RequiredObjectIsNullException"));
                     }else{
                        assertTrue(ex.getClass().getSimpleName().equals("ValidationException"));
                     }
                     //verifie qu'aucune ligne n'a ete ajoutee
                     assertEquals(20, operationManager.findAllObjectsManager().size());
                  }
               }
            }

         }
      }
   }

   /**
    * Teste la methode removeObjectManager. 
    */
   public void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final Utilisateur u = utilisateurDao.findById(3);
      final Operation o1 = operationManager.findByUtilisateurManager(u).get(0);
      operationManager.removeObjectManager(o1);
      assertTrue(operationManager.findByUtilisateurManager(u).size() == 0);
      //null remove
      operationManager.removeObjectManager(null);
      testFindAllObjectsManager();
   }

   /**
    * Test de la méthode findByDateOperationManager().
    * @throws ParseException
    */
   public void testFindByDateOperationManager() throws ParseException{
      Date date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2008");
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      List<Operation> ops = operationManager.findByDateOperationManager(cal);
      assertTrue(ops.size() == 1);

      date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2010");
      cal = Calendar.getInstance();
      cal.setTime(date);
      ops = operationManager.findByDateOperationManager(cal);
      assertTrue(ops.size() == 0);

      ops = operationManager.findByDateOperationManager(null);
      assertTrue(ops.size() == 0);
   }

   /**
    * Test de la méthode findAfterDateOperationManager().
    * @throws ParseException
    */
   public void testFindAfterDateOperationManager() throws ParseException{
      Date date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2008");
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      List<Operation> ops = operationManager.findAfterDateOperationManager(cal);
      assertEquals(16, ops.size());

      date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2010");
      cal = Calendar.getInstance();
      cal.setTime(date);
      ops = operationManager.findAfterDateOperationManager(cal);
      assertTrue(ops.size() == 0);

      ops = operationManager.findAfterDateOperationManager(null);
      assertTrue(ops.size() == 0);
   }

   /**
    * Test de la méthode findBeforeDateOperationManager().
    * @throws ParseException
    */
   public void testFindBeforeDateOperationManager() throws ParseException{
      Date date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2008");
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      List<Operation> ops = operationManager.findBeforeDateOperationManager(cal);
      assertTrue(ops.size() == 4);

      date = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1980");
      cal = Calendar.getInstance();
      cal.setTime(date);
      ops = operationManager.findBeforeDateOperationManager(cal);
      assertTrue(ops.size() == 0);

      ops = operationManager.findBeforeDateOperationManager(null);
      assertTrue(ops.size() == 0);
   }

   /**
    * Test de la méthode findBetweenDatesOperationManager().
    * @throws ParseException
    */
   public void testFindBetweenDatesOperationManager() throws ParseException{
      Date date = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009");
      final Calendar cal1 = Calendar.getInstance();
      cal1.setTime(date);
      date = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
      final Calendar cal2 = Calendar.getInstance();
      cal2.setTime(date);
      List<Operation> ops = operationManager.findBetweenDatesOperationManager(cal1, cal2);
      assertTrue(ops.size() == 5);

      date = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1980");
      cal1.setTime(date);
      date = new SimpleDateFormat("dd/MM/yyyy").parse("01/12/1980");
      cal2.setTime(date);
      ops = operationManager.findBetweenDatesOperationManager(cal1, cal2);
      assertTrue(ops.size() == 0);

      ops = operationManager.findBetweenDatesOperationManager(null, null);
      assertTrue(ops.size() == 0);
   }

   public void testFindByMultiCriteresManager() throws ParseException{
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Utilisateur u2 = utilisateurDao.findById(2);
      final List<Utilisateur> us = new ArrayList<>();
      final OperationType t3 = operationTypeDao.findById(3);
      final OperationType t5 = operationTypeDao.findById(5);

      Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("01/03/2001 10:10");
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      List<Operation> ops = operationManager.findByMultiCriteresManager("=", cal, null, null, null, null, false);
      assertTrue(ops.size() == 2);

      ops = operationManager.findByMultiCriteresManager("=", cal, null, null, t3, null, true);
      assertTrue(ops.size() == 1);

      us.add(u1);
      ops = operationManager.findByMultiCriteresManager("=", cal, null, null, null, us, false);
      assertTrue(ops.size() == 2);

      ops = operationManager.findByMultiCriteresManager("=", cal, null, null, t3, us, true);
      assertTrue(ops.size() == 1);

      us.clear();
      us.add(u2);
      ops = operationManager.findByMultiCriteresManager("=", cal, null, null, null, us, false);
      assertTrue(ops.size() == 0);

      date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2008");
      cal = Calendar.getInstance();
      cal.setTime(date);
      ops = operationManager.findByMultiCriteresManager(">=", cal, null, null, null, null, false);
      assertEquals(16, ops.size());
      ops = operationManager.findByMultiCriteresManager(">=", cal, null, null, t3, null, false);
      assertTrue(ops.size() == 12);

      us.add(u1);
      ops = operationManager.findByMultiCriteresManager(">=", cal, null, null, null, us, false);
      assertTrue(ops.size() == 16);

      ops = operationManager.findByMultiCriteresManager(">=", cal, null, null, t3, us, false);
      assertTrue(ops.size() == 12);

      date = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009");
      final Calendar cal1 = Calendar.getInstance();
      cal1.setTime(date);
      date = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
      final Calendar cal2 = Calendar.getInstance();
      cal2.setTime(date);
      ops = operationManager.findByMultiCriteresManager(">=", cal1, "<=", cal2, null, null, false);
      assertTrue(ops.size() == 5);

      ops = operationManager.findByMultiCriteresManager(">=", cal1, "<=", cal2, t3, null, false);
      assertTrue(ops.size() == 4);

      ops = operationManager.findByMultiCriteresManager(">=", cal1, "<=", cal2, null, us, false);
      assertTrue(ops.size() == 5);

      us.remove(u1);
      ops = operationManager.findByMultiCriteresManager(">=", cal1, "<=", cal2, t3, us, false);
      assertTrue(ops.size() == 1);

      ops = operationManager.findByMultiCriteresManager(null, null, null, null, t5, null, false);
      assertTrue(ops.size() == 5);

      ops = operationManager.findByMultiCriteresManager(null, null, null, null, null, us, true);
      assertTrue(ops.size() == 2);

      ops = operationManager.findByMultiCriteresManager(null, null, null, null, null, null, false);
      assertTrue(ops.size() == 0);
   }

   public void testFindLastByUtilisateurAndType() throws ParseException{
      Operation last;
      Utilisateur u = utilisateurDao.findById(2);
      OperationType oT = operationTypeDao.findById(3);
      last = operationManager.findLastByUtilisateurAndTypeManager(oT, u, 1);
      assertNotNull(last);
      assertEquals(Integer.valueOf(1), last.getObjetId());

      last = operationManager.findLastByUtilisateurAndTypeManager(oT, u, 2);
      assertNotNull(last);
      assertTrue(last.getObjetId() == 2);

      u = utilisateurDao.findById(1);
      oT = operationTypeDao.findById(5);
      last = operationManager.findLastByUtilisateurAndTypeManager(oT, u, 1);
      assertNotNull(last);
      assertTrue(last.getOperationId() == 17);
      assertTrue(last.getDate().getTime().equals(new SimpleDateFormat("dd/MM/yyyy").parse("10/12/2009")));

      last = operationManager.findLastByUtilisateurAndTypeManager(oT, u, 2);
      assertNotNull(last);
      assertTrue(last.getOperationId() == 9);
      assertTrue(last.getDate().getTime().equals(new SimpleDateFormat("dd/MM/yyyy").parse("05/11/2009")));

      last = operationManager.findLastByUtilisateurAndTypeManager(oT, u, 3);
      assertNotNull(last);
      assertTrue(last.getOperationId() == 13);
      assertTrue(last.getDate().getTime().equals(new SimpleDateFormat("dd/MM/yyyy").parse("02/11/2009")));

      u = utilisateurDao.findById(3);
      oT = operationTypeDao.findById(5);
      last = operationManager.findLastByUtilisateurAndTypeManager(oT, u, 1);
      assertNull(last);

      u = utilisateurDao.findById(1);
      oT = operationTypeDao.findById(17);
      last = operationManager.findLastByUtilisateurAndTypeManager(oT, u, 1);
      assertNull(last);

      u = utilisateurDao.findById(1);
      oT = operationTypeDao.findById(null);
      last = operationManager.findLastByUtilisateurAndTypeManager(oT, u, 1);
      assertNull(last);

      u = utilisateurDao.findById(null);
      oT = operationTypeDao.findById(3);
      last = operationManager.findLastByUtilisateurAndTypeManager(oT, u, 1);
      assertNull(last);

      u = utilisateurDao.findById(1);
      oT = operationTypeDao.findById(3);
      last = operationManager.findLastByUtilisateurAndTypeManager(oT, u, 0);
      assertNull(last);
   }

   public void testBatchSaveManager(){
      final List<Integer> ids = new ArrayList<>();
      ids.add(1);
      ids.add(2);
      ids.add(4);
      final Utilisateur u = utilisateurDao.findById(1);
      final Entite e = entiteDao.findById(3);
      final OperationType oT = operationTypeDao.findById(2);
      final Calendar cal = Calendar.getInstance();

      final int count = operationManager.findAllObjectsManager().size();

      operationManager.batchSaveManager(ids, u, oT, cal, e);

      assertTrue(operationManager.findAllObjectsManager().size() == count + 3);
      final List<Operation> ops = operationManager.findByDateOperationManager(cal);
      assertTrue(ops.size() == 3);
      for(final Operation op : ops){
         assertTrue(ids.contains(op.getObjetId()));
         assertTrue(op.getEntite().equals(e));
         assertTrue(op.getUtilisateur().equals(u));
         assertTrue(op.getOperationType().equals(oT));
         operationManager.removeObjectManager(op);
      }

      // teste JPA auto_increment refresh
      //		Operation ope = new Operation();
      //		ope.setDate(cal);
      //		ope.setEntite(entiteDao.findById(1));
      //		ope.setObjetId(2);
      //		operationManager.createObjectManager(ope, u, oT, prelevementDao.findById(1));

      boolean catched = false;
      try{
         operationManager.batchSaveManager(null, u, oT, cal, e);
      }catch(final NullPointerException ne){
         catched = true;
      }
      assertTrue(catched);
      catched = false;
      try{
         operationManager.batchSaveManager(ids, null, oT, cal, e);
      }catch(final NullPointerException ne){
         catched = true;
      }
      assertTrue(catched);
      catched = false;
      try{
         operationManager.batchSaveManager(ids, u, null, cal, e);
      }catch(final NullPointerException ne){
         catched = true;
      }
      assertTrue(catched);
      catched = false;
      try{
         operationManager.batchSaveManager(ids, u, oT, null, e);
      }catch(final NullPointerException ne){
         catched = true;
      }
      assertTrue(catched);
      catched = false;
      try{
         operationManager.batchSaveManager(ids, u, oT, cal, null);
      }catch(final NullPointerException ne){
         catched = true;
      }
      assertTrue(catched);
      catched = false;
      ids.clear();
      try{
         operationManager.batchSaveManager(ids, u, oT, cal, e);
      }catch(final NullPointerException ne){
         catched = true;
      }
      assertTrue(catched);

      // clean up
      testFindAllObjectsManager();
   }
}
