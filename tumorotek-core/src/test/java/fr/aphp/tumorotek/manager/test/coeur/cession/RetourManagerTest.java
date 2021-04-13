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
package fr.aphp.tumorotek.manager.test.coeur.cession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.TransformationDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.stockage.EmplacementDao;
import fr.aphp.tumorotek.dao.stockage.IncidentDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.exception.ObjectStatutException;
import fr.aphp.tumorotek.manager.impl.coeur.cession.OldEmplTrace;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.cession.retour.RetourValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager RetourManager.
 * Classe créée le 25/01/10.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.2.3-genno
 *
 */
public class RetourManagerTest extends AbstractManagerTest4
{

   @Autowired
   private RetourManager retourManager;
   @Autowired
   private EchantillonDao echantillonDao;
   @Autowired
   private ProdDeriveDao prodDeriveDao;
   @Autowired
   private CollaborateurDao collaborateurDao;
   @Autowired
   private CessionDao cessionDao;
   @Autowired
   private IncidentDao incidentDao;
   @Autowired
   private RetourValidator retourValidator;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private TransformationDao transformationDao;
   @Autowired
   private EmplacementDao emplacementDao;
   @Autowired
   private ConteneurDao conteneurDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private ObjetStatutDao objetStatutDao;

   public RetourManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<Retour> retours = retourManager.findAllObjectsManager();
      assertTrue(retours.size() == 8);
   }

   /**
    * Teste la méthode findDoublon.
    * @throws ParseException 
    */
   @Test
   public void testFindDoublon() throws ParseException{
      //Cree le doublon
      final Retour r1 = retourManager.findAllObjectsManager().get(0);
      final Retour r2 = new Retour();
      r2.setObjetId(r1.getObjetId());
      r2.setEntite(r1.getEntite());
      r2.setDateSortie(r1.getDateSortie());
      r2.setDateRetour(r1.getDateRetour());
      assertTrue(r2.equals(r1));
      assertTrue(retourManager.findDoublonManager(r2));
   }

   @Test
   public void testGetRetoursForObjectManager(){
      final Echantillon e = echantillonDao.findById(1);
      List<Retour> retours = retourManager.getRetoursForObjectManager(e);
      assertTrue(retours.size() == 5);
      ProdDerive pd = prodDeriveDao.findById(3);
      retours = retourManager.getRetoursForObjectManager(pd);
      assertTrue(retours.size() == 2);
      pd = prodDeriveDao.findById(1);
      retours = retourManager.getRetoursForObjectManager(pd);
      assertTrue(retours.size() == 0);
      retours = retourManager.getRetoursForObjectManager(null);
      assertTrue(retours.size() == 0);
   }

   @Test
   public void testCRUD() throws ParseException{
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   private void createObjectManagerTest() throws ParseException{
      //Insertion nouvel enregistrement
      final Retour r = new Retour();
      /*Champs obligatoires*/
      final Echantillon e1 = echantillonDao.findById(1);
      final Echantillon e2 = echantillonDao.findById(2);
      final Utilisateur u = utilisateurDao.findById(1);

      r.setTempMoyenne(new Float(22.0));

      final Collaborateur c = collaborateurDao.findById(2);
      final Incident i1 = incidentDao.findById(1);

      final Retour r1 = retourManager.getRetoursForObjectManager(e1).get(0);
      r.setDateRetour(r1.getDateRetour());
      r.setDateSortie(r1.getDateSortie());
      //insertion doublon
      Boolean catched = false;
      try{
         retourManager.createOrUpdateObjectManager(r, e1, null, c, null, null, i1, u, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      testFindAllObjectsManager();
      assertTrue(retourManager.getRetoursForObjectManager(e2).size() == 0);

      //insertion valide incomplete
      r.setDateRetour(null);
      r.setOldEmplacementAdrl("CC1.R2.T2.BT1.56");
      retourManager.createOrUpdateObjectManager(r, e2, null, c, null, null, i1, u, "creation");
      assertTrue(retourManager.findAllObjectsManager().size() == 9);
      assertTrue(retourManager.getRetoursForObjectManager(e2).size() == 1);
      // emplacement n'a pas changé
      assertTrue(retourManager.getRetoursForObjectManager(e2).get(0).getOldEmplacementAdrl().equals("CC1.R1.T1.BT1.A-C"));
      assertTrue(retourManager.getRetoursForObjectManager(e2).get(0).getConteneur().equals(conteneurDao.findById(1)));
      assertTrue(getOperationManager().findByObjectManager(r).size() == 1);
      assertNull(retourManager.getRetoursForObjectManager(e2).get(0).getDateRetour());
      assertTrue(echantillonDao.findById(2).getObjetStatut().getStatut().equals("ENCOURS"));
      assertTrue(retourManager.getRetoursForObjectManager(e2).get(0).getObjetStatut().getStatut().equals("STOCKE"));

      final Calendar dateSortie = Calendar.getInstance();
      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2010 12:10:25"));
      r.setDateSortie(dateSortie);
      final Calendar dateRetour = Calendar.getInstance();
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("02/01/2010 16:47:10"));
      r.setDateRetour(dateRetour);
      catched = false;
      try{
         retourManager.createOrUpdateObjectManager(r, e2, null, c, null, null, i1, u, null);
      }catch(final NullPointerException e){
         catched = true;
      }
      assertTrue(catched);

      catched = false;
      try{
         retourManager.createOrUpdateObjectManager(r, e2, null, c, null, null, i1, u, "test");
      }catch(final IllegalArgumentException e){
         catched = true;
      }
      assertTrue(catched);
      assertTrue(retourManager.findAllObjectsManager().size() == 9);
      assertTrue(retourManager.getRetoursForObjectManager(e2).size() == 1);
   }

   private void updateObjectManagerTest() throws ParseException{
      //Modification d'un enregistrement
      final Echantillon e2 = echantillonDao.findById(2);
      final Echantillon e1 = echantillonDao.findById(1);
      final Retour r = retourManager.getRetoursForObjectManager(e2).get(0);

      final Utilisateur u = utilisateurDao.findById(1);

      assertTrue(retourManager.getRetoursForObjectManager(e1).size() == 5);

      final Calendar dateSortie = Calendar.getInstance();
      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2010 12:10:25"));
      r.setDateSortie(dateSortie);
      final Calendar dateRetour = Calendar.getInstance();
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("02/01/2010 16:47:10"));
      r.setDateRetour(dateRetour);

      r.setTempMoyenne(new Float(28.0));

      //modification valide
      retourManager.createOrUpdateObjectManager(r, e2, emplacementDao.findById(2), r.getCollaborateur(), r.getCession(),
         r.getTransformation(), r.getIncident(), u, "modification");
      assertTrue(retourManager.findAllObjectsManager().size() == 9);
      assertTrue(retourManager.getRetoursForObjectManager(e2).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(r).size() == 2);
      assertTrue(retourManager.getRetoursForObjectManager(e2).get(0).getTempMoyenne().equals(new Float(28.0)));
      // emplacement a changé
      assertTrue(retourManager.getRetoursForObjectManager(e2).get(0).getOldEmplacementAdrl().equals("CC1.R1.T1.BT1.A-B"));
      assertTrue(retourManager.getRetoursForObjectManager(e2).get(0).getConteneur().equals(conteneurDao.findById(1)));

      // statut ré-affecté
      assertNotNull(retourManager.getRetoursForObjectManager(e2).get(0).getDateRetour());
      assertTrue(echantillonDao.findById(2).getObjetStatut().getStatut().equals("STOCKE"));
      assertNull(retourManager.getRetoursForObjectManager(e2).get(0).getObjetStatut());
   }

   private void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final Echantillon e2 = echantillonDao.findById(2);
      final Retour r = retourManager.getRetoursForObjectManager(e2).get(0);

      retourManager.removeObjectManager(r);
      //null remove
      retourManager.removeObjectManager(null);

      //verifie que l'etat des tables modifies est revenu identique
      testFindAllObjectsManager();

      cleanUpFantomes(new ArrayList<TKFantomableObject>());
   }

   @Test
   public void testValidation() throws ParseException{
      List<Errors> errs = new ArrayList<>();
      final Retour r = new Retour();

      try{
         BeanValidator.validateObject(r, new Validator[] {retourValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("validation.syntax.empty"));
      }

      final Calendar dateSortie = Calendar.getInstance();
      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2010 12:10:25"));
      r.setDateSortie(dateSortie);

      try{
         BeanValidator.validateObject(r, new Validator[] {retourValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("validation.syntax.empty"));
      }

      Calendar dateRetour = Calendar.getInstance();
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2010 12:10:24"));
      r.setDateRetour(dateRetour);

      try{
         BeanValidator.validateObject(r, new Validator[] {retourValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("validation.syntax.empty"));
      }

      r.setTempMoyenne(new Float(12.3));

      try{
         BeanValidator.validateObject(r, new Validator[] {retourValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("date.validation.supDateRetour"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();

      dateRetour = Calendar.getInstance();
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2010 12:10:26"));
      r.setDateRetour(dateRetour);

      r.setObservations("  ");
      try{
         BeanValidator.validateObject(r, new Validator[] {retourValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("retour.observations.empty"));
      }
      r.setObservations(createOverLength(250));
      try{
         BeanValidator.validateObject(r, new Validator[] {retourValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("retour.observations.tooLong"));
      }
   }

   @Test
   @SuppressWarnings("deprecation")
   public void testCreateRetourListManager() throws ParseException{
      final Retour base = new Retour();

      /*Champs obligatoires*/
      final Calendar dateSortie = Calendar.getInstance();
      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2013 12:10:25"));
      base.setDateSortie(dateSortie);
      final Calendar dateRetour = Calendar.getInstance();
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2013 14:22:45"));
      base.setDateRetour(dateRetour);
      base.setTempMoyenne(new Float(26.0));
      base.setSterile(true);
      base.setObservations("knoxk on wood ouhuo!");

      final Collaborateur c = collaborateurDao.findById(2);
      final Cession c1 = cessionDao.findById(1);

      final Utilisateur u = utilisateurDao.findById(1);

      // objects
      final List<TKStockableObject> objs = new ArrayList<>();
      objs.add(echantillonDao.findById(1));
      objs.add(echantillonDao.findById(2));
      objs.add(prodDeriveDao.findById(2));
      objs.add(prodDeriveDao.findById(1));

      // old emplacements
      final List<OldEmplTrace> oldEmps = new ArrayList<OldEmplTrace>();
      oldEmps.add(new OldEmplTrace(echantillonDao.findById(2), "OLD.C1.R1.T1.23", 
    		  conteneurDao.findById(1), emplacementDao.findById(2)));
      oldEmps.add(new OldEmplTrace(prodDeriveDao.findById(1), null, null, emplacementDao.findById(4)));

      retourManager.createRetourListManager(objs, oldEmps, base, c, c1, null, null, u);

      assertTrue(retourManager.findAllObjectsManager().size() == 12);
      assertTrue(retourManager.getRetoursForObjectManager(echantillonDao.findById(1)).size() == 6);
      assertTrue(retourManager.getRetoursForObjectManager(prodDeriveDao.findById(2)).size() == 1);
      assertTrue(retourManager.getRetoursForObjectManager(prodDeriveDao.findById(1)).size() == 1);
      assertTrue(retourManager.getRetoursForObjectManager(echantillonDao.findById(2)).size() == 1);
      assertFalse(echantillonDao.findById(2).getObjetStatut().getStatut().equals("ENCOURS"));

      Retour r = retourManager.getRetoursForObjectManager(echantillonDao.findById(2)).get(0);
      assertTrue(r.getCollaborateur().equals(c));
      assertTrue(r.getCession().equals(c1));
      assertNull(r.getIncident());
      assertNull(r.getTransformation());
      assertTrue(r.getTempMoyenne().equals(new Float(26.0)));
      assertTrue(r.getObservations().equals("knoxk on wood ouhuo!"));
      assertTrue(r.getSterile());
      assertTrue(r.getDateRetour().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2013 14:22:45")));
      assertTrue(r.getDateSortie().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2013 12:10:25")));
      assertTrue(r.getOldEmplacementAdrl().equals("CC1.R1.T1.BT1.A-B"));
      assertTrue(r.getConteneur().equals(conteneurDao.findById(1)));

      r = retourManager.getRetoursForObjectManager(prodDeriveDao.findById(2)).get(0);
      assertTrue(r.getOldEmplacementAdrl().equals("CC1.R1.T1.BT1.A-B"));
      assertTrue(r.getConteneur().equals(conteneurDao.findById(1)));

      r = retourManager.getRetoursForObjectManager(prodDeriveDao.findById(1)).get(0);
      assertTrue(r.getOldEmplacementAdrl().equals("CC1.R1.T1.BT1.A-J"));
      assertTrue(r.getConteneur().equals(conteneurDao.findById(1)));

      List<Retour> rets = retourManager.findAllObjectsManager();
      for(int i = 0; i < rets.size(); i++){
         if(rets.get(i).getRetourId() > 8){
            retourManager.removeObjectManager(rets.get(i));
         }
      }
      testFindAllObjectsManager();

      // teste null old emplacements -> utilise les emplacements associés 
      // aux objets
      retourManager.createRetourListManager(objs, null, base, c, c1, null, null, u);

      r = retourManager.getRetoursForObjectManager(echantillonDao.findById(2)).get(0);
      assertTrue(r.getOldEmplacementAdrl().equals("CC1.R1.T1.BT1.A-C"));
      assertTrue(r.getConteneur().equals(conteneurDao.findById(1)));

      r = retourManager.getRetoursForObjectManager(prodDeriveDao.findById(2)).get(0);
      assertTrue(r.getOldEmplacementAdrl().equals("CC1.R1.T1.BT1.A-B"));
      assertTrue(r.getConteneur().equals(conteneurDao.findById(1)));

      // r = retourManager
      // .getRetoursForObjectManager(echantillonDao.findById(1)).get(0);
      // assertNull(r.getOldEmplacementAdrl());

      rets = retourManager.findAllObjectsManager();
      for(int i = 0; i < rets.size(); i++){
         if(rets.get(i).getRetourId() > 8){
            retourManager.removeObjectManager(rets.get(i));
         }
      }
      testFindAllObjectsManager();

      retourManager.createRetourListManager(null, null, base, c, c1, null, null, u);

      testFindAllObjectsManager();
   }

   @Test
   @SuppressWarnings("deprecation")
   public void testCreateRetourHugeListManager() throws ParseException{
      final Retour base = new Retour();

      /*Champs obligatoires*/
      final Calendar dateSortie = Calendar.getInstance();
      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2011 12:10:00"));
      base.setDateSortie(dateSortie);
      final Calendar dateRetour = Calendar.getInstance();
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2011 16:15:00"));
      base.setDateRetour(dateRetour);

      base.setTempMoyenne(new Float(26.0));
      base.setSterile(true);
      base.setImpact(true);
      base.setObservations("knoxk on wood ouhuo!");

      final Collaborateur c = collaborateurDao.findById(2);
      final Cession c1 = cessionDao.findById(1);
      final Transformation transformation = transformationDao.findById(1);
      final Incident incident = incidentDao.findById(1);

      final Utilisateur u = utilisateurDao.findById(1);

      // objects
      final List<TKStockableObject> objs = new ArrayList<>();
      objs.add(echantillonDao.findById(1));
      objs.add(echantillonDao.findById(2));
      objs.add(prodDeriveDao.findById(2));
      objs.add(prodDeriveDao.findById(1));

      // old emplacements
      final List<OldEmplTrace> oldEmps = new ArrayList<OldEmplTrace>();
      oldEmps.add(new OldEmplTrace(echantillonDao.findById(2), "CC1.R2.T6.BT1.A-1", 
    		  conteneurDao.findById(1), emplacementDao.findById(3)));
      oldEmps.add(new OldEmplTrace(prodDeriveDao.findById(1), "CC1.R1.T1.BT1.A-A", 
    		  conteneurDao.findById(1), emplacementDao.findById(1)));
      // oldEmps.put(echantillonDao.findById(2), emplacementDao.findById(6));
      // oldEmps.put(prodDeriveDao.findById(1), emplacementDao.findById(1));

      retourManager.createRetourHugeListManager(objs, oldEmps, base, c, c1, transformation, incident, u);

      assertTrue(retourManager.findAllObjectsManager().size() == 12);
      assertTrue(retourManager.getRetoursForObjectManager(echantillonDao.findById(1)).size() == 6);
      assertTrue(retourManager.getRetoursForObjectManager(prodDeriveDao.findById(2)).size() == 1);
      assertTrue(retourManager.getRetoursForObjectManager(prodDeriveDao.findById(1)).size() == 1);
      assertTrue(retourManager.getRetoursForObjectManager(echantillonDao.findById(2)).size() == 1);
      Retour test = retourManager.getRetoursForObjectManager(echantillonDao.findById(2)).get(0);
      assertNotNull(test);
      assertTrue(test.getObjetId() == 2);
      assertTrue(test.getEntite().getEntiteId() == 3);
      assertTrue(
         new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(test.getDateSortie().getTime()).equals("01/01/2011 12:10:00"));
      assertTrue(
         new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(test.getDateRetour().getTime()).equals("01/01/2011 16:15:00"));
      assertTrue(test.getTempMoyenne().equals(new Float(26.0)));
      assertTrue(test.getSterile());
      assertTrue(test.getImpact());
      assertTrue(test.getCollaborateur().equals(c));
      assertTrue(test.getObservations().equals("knoxk on wood ouhuo!"));
      assertTrue(test.getOldEmplacementAdrl().equals("CC1.R2.T6.BT1.A-1"));
      assertTrue(test.getCession().equals(c1));
      assertTrue(test.getTransformation().equals(transformation));
      assertTrue(test.getConteneur().equals(conteneurDao.findById(1)));
      assertTrue(test.getIncident().equals(incident));

      assertTrue(retourManager.getRetoursForObjectManager(prodDeriveDao.findById(1)).get(0).getOldEmplacementAdrl()
         .equals("CC1.R1.T1.BT1.A-A"));
      assertTrue(retourManager.getRetoursForObjectManager(prodDeriveDao.findById(1)).get(0).getConteneur()
         .equals(conteneurDao.findById(1)));

      // test retour JPAException
      final Retour r2 = new Retour();
      r2.setTempMoyenne(new Float(33.0));
      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2011 22:10:00"));
      r2.setDateSortie(dateSortie);

      retourManager.createOrUpdateObjectManager(r2, echantillonDao.findById(1), null, null, null, null, null, u, "creation");
      assertTrue(retourManager.findAllObjectsManager().size() == 13);
      assertTrue(retourManager.getRetoursForObjectManager(echantillonDao.findById(1)).size() == 7);
      assertTrue(echantillonDao.findById(1).getObjetStatut().getStatut().equals("ENCOURS"));
      assertTrue(echantillonDao.findById(1).getSterile());

      //refresh
      objs.clear();
      objs.add(echantillonDao.findById(1));
      objs.add(echantillonDao.findById(2));
      objs.add(prodDeriveDao.findById(2));
      objs.add(prodDeriveDao.findById(1));

      // validation error un echantillon (id=1) ENCOURS
      boolean catched = true;
      try{
         base.setDateRetour(null);
         base.setSterile(null);
         base.setImpact(null);
         base.setObservations(null);
         base.setOldEmplacementAdrl(null);
         retourManager.createRetourHugeListManager(objs, null, base, null, null, null, null, u);
      }catch(final RuntimeException ve){
         catched = true;
      }
      assertTrue(catched);
      assertTrue(retourManager.findAllObjectsManager().size() == 13);
      assertTrue(echantillonDao.findByObjetStatut(objetStatutDao.findByStatut("ENCOURS").get(0)).size() == 1);

      final Retour last = retourManager.getRetoursForObjectManager(echantillonDao.findById(1)).get(6);
      assertTrue(last.getDateRetour() == null);
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2011 23:10:00"));
      last.setDateRetour(dateRetour);
      retourManager.createOrUpdateObjectManager(last, echantillonDao.findById(1), null, null, null, null, null, u,
         "modification");
      assertTrue(echantillonDao.findByObjetStatut(objetStatutDao.findByStatut("ENCOURS").get(0)).size() == 0);

      //refresh
      objs.clear();
      objs.add(echantillonDao.findById(1));
      objs.add(echantillonDao.findById(2));
      objs.add(prodDeriveDao.findById(2));
      objs.add(prodDeriveDao.findById(1));

      // validation error en fin de liste sur prod derive (id=3) cause cohérence date
      catched = true;
      try{
         dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2011 22:54:00"));
         base.setDateSortie(dateSortie);
         base.setDateRetour(null);
         base.setSterile(null);
         base.setImpact(null);
         base.setObservations(null);
         base.setOldEmplacementAdrl(null);
         retourManager.createRetourHugeListManager(objs, null, base, null, null, null, null, u);
      }catch(final RuntimeException re){
         assertTrue(re.getMessage().contains("date.validation.retourExistant.incoherent"));
         catched = true;
      }
      assertTrue(catched);
      assertTrue(retourManager.findAllObjectsManager().size() == 13);
      assertTrue(echantillonDao.findByObjetStatut(objetStatutDao.findByStatut("ENCOURS").get(0)).size() == 0);
      assertTrue(prodDeriveDao.findByObjetStatut(objetStatutDao.findByStatut("ENCOURS").get(0)).size() == 0);

      List<Retour> rets = retourManager.findAllObjectsManager();
      for(int i = 0; i < rets.size(); i++){
         if(rets.get(i).getRetourId() > 8){
            retourManager.removeObjectManager(rets.get(i));
         }
      }
      testFindAllObjectsManager();

      retourManager.createRetourListManager(null, null, base, c, c1, null, null, u);

      testFindAllObjectsManager();

      // même test avec les associations à null
      base.setDateRetour(null);
      base.setSterile(null);
      base.setImpact(null);
      base.setObservations(null);
      base.setOldEmplacementAdrl(null);
      retourManager.createRetourHugeListManager(objs, null, base, null, null, null, null, u);

      assertTrue(retourManager.findAllObjectsManager().size() == 12);
      test = retourManager.getRetoursForObjectManager(echantillonDao.findById(2)).get(0);
      assertNotNull(test);
      assertTrue(test.getObjetId() == 2);
      assertTrue(test.getEntite().getEntiteId() == 3);
      assertTrue(
         new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(test.getDateSortie().getTime()).equals("01/01/2011 22:54:00"));
      assertNull(test.getDateRetour());
      assertTrue(test.getTempMoyenne().equals(new Float(26.0)));
      assertNull(test.getSterile());
      assertNull(test.getImpact());
      assertNull(test.getCollaborateur());
      assertNull(test.getObservations());
      assertNull(test.getCession());
      assertNull(test.getTransformation());
      assertNull(test.getIncident());

      // teste update objs
      assertTrue(echantillonDao.findById(objs.get(0).listableObjectId()).getObjetStatut().getStatut().equals("ENCOURS"));

      // date Retour nulle implique statut ENCOURS
      assertTrue(echantillonDao.findByObjetStatut(objetStatutDao.findByStatut("ENCOURS").get(0)).size() == 2);
      assertTrue(echantillonDao.findByObjetStatut(objetStatutDao.findByStatut("STOCKE").get(0)).isEmpty());
      assertTrue(prodDeriveDao.findByObjetStatut(objetStatutDao.findByStatut("ENCOURS").get(0)).size() == 2);

      // adrl inchangée
      assertTrue(test.getOldEmplacementAdrl().equals("CC1.R1.T1.BT1.A-C"));
      assertTrue(test.getConteneur().equals(conteneurDao.findById(1)));

      // null emplacement
      assertNull(retourManager.getRetoursForObjectManager(prodDeriveDao.findById(3)).get(0).getOldEmplacementAdrl());
      assertNull(retourManager.getRetoursForObjectManager(prodDeriveDao.findById(3)).get(0).getConteneur());

      rets = retourManager.findAllObjectsManager();
      for(int i = 0; i < rets.size(); i++){
         if(rets.get(i).getRetourId() > 8){
            retourManager.removeObjectManager(rets.get(i));
         }
      }

      assertTrue(echantillonDao.findByObjetStatut(objetStatutDao.findByStatut("ENCOURS").get(0)).isEmpty());
      assertTrue(echantillonDao.findByObjetStatut(objetStatutDao.findByStatut("STOCKE").get(0)).size() == 2);
      assertTrue(prodDeriveDao.findByObjetStatut(objetStatutDao.findByStatut("ENCOURS").get(0)).isEmpty());

      testFindAllObjectsManager();

      retourManager.createRetourListManager(null, null, base, c, c1, null, null, u);

      testFindAllObjectsManager();
   }

   @Test
   public void testDeleteIncompleteRetour(){
      Retour r = new Retour();
      Retour rp = new Retour();
      Retour r2 = new Retour();
      /*Champs obligatoires*/
      final Echantillon e1 = echantillonDao.findById(1);
      final Echantillon e2 = echantillonDao.findById(2);
      final Echantillon e3 = echantillonDao.findById(3);
      final ProdDerive p1 = prodDeriveDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(1);

      r.setTempMoyenne(new Float(22.0));
      r2.setTempMoyenne(new Float(02.0));
      rp.setTempMoyenne(new Float(12.00));

      final Collaborateur c = collaborateurDao.findById(2);
      final Incident i1 = incidentDao.findById(1);

      final Retour r1 = retourManager.getRetoursForObjectManager(e1).get(0);
      r.setDateSortie(r1.getDateSortie());
      rp.setDateSortie(r1.getDateSortie());
      r2.setDateSortie(r1.getDateSortie());

      //insertions valides incompletes
      Boolean catched = false;
      try{ // EPUISE
         retourManager.createOrUpdateObjectManager(r, e3, null, c, null, null, i1, u, "creation");
      }catch(final ObjectStatutException oe){
         catched = true;
      }
      assertTrue(catched);
      retourManager.createOrUpdateObjectManager(r2, e2, null, c, null, null, i1, u, "creation");
      retourManager.createOrUpdateObjectManager(rp, p1, null, c, null, null, i1, u, "creation");

      assertTrue(retourManager.findAllObjectsManager().size() == 10);
      assertTrue(retourManager.getRetoursForObjectManager(e3).size() == 0);
      assertTrue(retourManager.getRetoursForObjectManager(e2).size() == 1);
      assertTrue(retourManager.getRetoursForObjectManager(p1).size() == 1);
      // emplacement n'a pas changé
      assertTrue(retourManager.getRetoursForObjectManager(p1).get(0).getOldEmplacementAdrl().equals("CC1.R1.T1.BT1.A-A"));
      assertTrue(retourManager.getRetoursForObjectManager(e2).get(0).getOldEmplacementAdrl().equals("CC1.R1.T1.BT1.A-C"));
      assertTrue(retourManager.getRetoursForObjectManager(e2).get(0).getConteneur().equals(conteneurDao.findById(1)));
      assertTrue(retourManager.getRetoursForObjectManager(p1).get(0).getConteneur().equals(conteneurDao.findById(1)));
      assertNull(retourManager.getRetoursForObjectManager(e2).get(0).getDateRetour());
      assertNull(retourManager.getRetoursForObjectManager(p1).get(0).getDateRetour());
      assertTrue(echantillonDao.findById(3).getObjetStatut().getStatut().equals("EPUISE"));
      assertTrue(echantillonDao.findById(2).getObjetStatut().getStatut().equals("ENCOURS"));
      assertTrue(prodDeriveDao.findById(1).getObjetStatut().getStatut().equals("ENCOURS"));
      assertTrue(retourManager.getRetoursForObjectManager(e2).get(0).getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(retourManager.getRetoursForObjectManager(p1).get(0).getObjetStatut().getStatut().equals("STOCKE"));

      // STATUT EN COURS -> ObjectStatutException
      r = new Retour();
      r.setTempMoyenne(new Float(12.0));
      r.setDateSortie(r1.getDateSortie());
      try{
         retourManager.createOrUpdateObjectManager(r, p1, null, c, null, null, i1, u, "creation");
      }catch(final ObjectStatutException e){
         catched = true;
      }
      assertTrue(catched);

      rp = retourManager.getRetoursForObjectManager(p1).get(0);
      r2 = retourManager.getRetoursForObjectManager(e2).get(0);
      retourManager.removeObjectManager(r2);
      retourManager.removeObjectManager(rp);

      // re attribution des statuts
      assertTrue(prodDeriveDao.findById(1).getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(echantillonDao.findById(2).getObjetStatut().getStatut().equals("STOCKE"));

      //verifie que l'etat des tables modifies est revenu identique
      testFindAllObjectsManager();

      cleanUpFantomes(new ArrayList<TKFantomableObject>());
   }

   @Test
   public void testFindByObjDatesManager() throws ParseException{
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("06/01/2010 12:12"));
      List<Retour> rs = retourValidator.findByObjDatesManager(cal, echantillonDao.findById(1), 0);
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).getRetourId().equals(4));

      rs = retourValidator.findByObjDatesManager(cal, echantillonDao.findById(4), null);
      assertTrue(rs.isEmpty());

      cal.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("01/01/2010 12:12"));
      rs = retourValidator.findByObjDatesManager(cal, echantillonDao.findById(4), -1);
      assertTrue(rs.isEmpty());

      cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2010"));
      rs = retourValidator.findByObjDatesManager(cal, echantillonDao.findById(4), 0);
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).getRetourId().equals(6));

      rs = retourValidator.findByObjDatesManager(cal, prodDeriveDao.findById(3), null);
      assertTrue(rs.size() == 2);
      assertTrue(rs.get(0).getRetourId() > 6);

      rs = retourValidator.findByObjDatesManager(cal, null, 2);
      assertTrue(rs.isEmpty());

      rs = retourValidator.findByObjDatesManager(null, prodDeriveDao.findById(3), 7);
      assertTrue(rs.isEmpty());
   }

   @Test
   public void testFindByObjInsideDates() throws ParseException{
      final Calendar cal1 = Calendar.getInstance();
      cal1.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("06/01/2010 12:12"));
      final Calendar cal2 = Calendar.getInstance();
      cal2.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("10/01/2010 12:12"));
      List<Retour> rs = retourValidator.findByObjInsideDatesManager(cal1, cal2, echantillonDao.findById(1), 0);
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).getRetourId().equals(5));

      cal1.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("31/12/2009 12:12"));
      rs = retourValidator.findByObjInsideDatesManager(cal1, cal2, echantillonDao.findById(1), null);
      assertTrue(rs.size() == 5);

      rs = retourValidator.findByObjInsideDatesManager(cal1, cal2, echantillonDao.findById(4), -1);
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).getRetourId().equals(6));

      rs = retourValidator.findByObjInsideDatesManager(cal1, cal2, echantillonDao.findById(2), null);
      assertTrue(rs.isEmpty());

      cal1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2010"));

      rs = retourValidator.findByObjInsideDatesManager(cal1, cal2, echantillonDao.findById(4), 0);
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).getRetourId().equals(6));

      rs = retourValidator.findByObjInsideDatesManager(cal1, cal2, prodDeriveDao.findById(3), -1);
      assertTrue(rs.size() == 2);
      assertTrue(rs.get(0).getRetourId() > 6);

      rs = retourValidator.findByObjInsideDatesManager(cal1, cal2, null, 2);
      assertTrue(rs.isEmpty());

      rs = retourValidator.findByObjInsideDatesManager(cal1, null, prodDeriveDao.findById(3), 7);
      assertTrue(rs.isEmpty());

      rs = retourValidator.findByObjInsideDatesManager(null, cal2, prodDeriveDao.findById(3), 3);
      assertTrue(rs.isEmpty());
   }

   @Test
   public void testDateSortieCoherence() throws ParseException{
      Errors errs;
      final Retour r = new Retour();
      r.setEntite(entiteDao.findById(3));
      r.setObjetId(2);

      errs = retourValidator.checkDateSortieCoherence(r);
      assertTrue(errs.getFieldError().getCode().equals("retour.dateSortie.empty"));

      final Calendar dateSortie = Calendar.getInstance();
      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2007 12:10:25"));
      r.setDateSortie(dateSortie);

      errs = retourValidator.checkDateSortieCoherence(r);
      assertTrue(errs.getFieldError().getCode().equals("date.validation.infDateStockage"));

      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("16/03/2008 00:00:00"));
      r.setDateSortie(dateSortie);

      final Calendar dateRetour = Calendar.getInstance();
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("15/03/2008 23:59:59"));
      r.setDateRetour(dateRetour);
      errs = retourValidator.checkDateSortieCoherence(r);
      assertTrue(errs.getFieldError().getCode().equals("date.validation.supDateRetour"));

      r.setObjetId(1);
      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("31/12/2009 00:00:00"));
      r.setDateSortie(dateSortie);
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("02/01/2010 23:59:59"));
      r.setDateRetour(dateRetour);
      errs = retourValidator.checkDateSortieCoherence(r);
      assertTrue(errs.getFieldError().getCode().equals("date.validation.inclueRetourExistant"));

      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("08/01/2010 10:00:00"));
      r.setDateSortie(dateSortie);
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("10/01/2010 23:59:59"));
      r.setDateRetour(dateRetour);
      errs = retourValidator.checkDateSortieCoherence(r);
      assertTrue(errs.getFieldError().getCode().equals("date.validation.incluDansRetourExistant"));

      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("10/01/2010 10:00:00"));
      r.setDateSortie(dateSortie);
      errs = retourValidator.checkDateSortieCoherence(r);
      assertFalse(errs.hasErrors());

      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("10/01/2010 10:00:00"));
      r.setDateRetour(dateRetour);
      errs = retourValidator.checkDateSortieCoherence(r);
      assertFalse(errs.hasErrors());

   }

   @Test
   public void testDateRetourCoherence() throws ParseException{
      Errors errs;
      final Retour r = new Retour();
      r.setEntite(entiteDao.findById(3));
      r.setObjetId(2);

      errs = retourValidator.checkDateRetourCoherence(r);
      assertFalse(errs.hasErrors());

      final Calendar dateRetour = Calendar.getInstance();
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2007 12:10:25"));
      r.setDateRetour(dateRetour);

      errs = retourValidator.checkDateRetourCoherence(r);
      assertTrue(errs.getFieldError().getCode().equals("retour.dateSortie.empty"));

      final Calendar dateSortie = Calendar.getInstance();
      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("16/03/2008 00:00:00"));
      r.setDateSortie(dateSortie);
      errs = retourValidator.checkDateRetourCoherence(r);
      assertTrue(errs.getFieldError().getCode().equals("date.validation.infDateSortie"));

      r.setObjetId(1);
      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("31/12/2009 00:00:00"));
      r.setDateSortie(dateSortie);
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("02/01/2010 23:59:59"));
      r.setDateRetour(dateRetour);
      errs = retourValidator.checkDateRetourCoherence(r);
      assertTrue(errs.getFieldError().getCode().equals("date.validation.inclueRetourExistant"));

      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("31/12/2009 10:00:00"));
      r.setDateSortie(dateSortie);
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2010 16:59:59"));
      r.setDateRetour(dateRetour);
      errs = retourValidator.checkDateRetourCoherence(r);
      assertTrue(errs.getFieldError().getCode().equals("date.validation.incluDansRetourExistant"));

      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("31/12/2009 10:00:00"));
      r.setDateRetour(dateRetour);
      errs = retourValidator.checkDateRetourCoherence(r);
      assertFalse(errs.hasErrors());
   }

   @Test
   public void testFindByObjectDateRetourEmptyManager(){
      final List<Integer> objsIds = new ArrayList<>();

      List<Retour> incomps = retourManager.findByObjectDateRetourEmptyManager(objsIds, entiteDao.findById(3));
      assertTrue(incomps.isEmpty());

      incomps = retourManager.findByObjectDateRetourEmptyManager(null, entiteDao.findById(3));
      assertTrue(incomps.isEmpty());

      objsIds.add(2);
      incomps = retourManager.findByObjectDateRetourEmptyManager(objsIds, entiteDao.findById(3));
      assertTrue(incomps.isEmpty());

      final Retour r1 = new Retour();
      final Echantillon e2 = echantillonDao.findById(2);
      final Utilisateur u = utilisateurDao.findById(1);

      r1.setTempMoyenne(new Float(22.0));

      final Collaborateur c = collaborateurDao.findById(2);
      final Incident i1 = incidentDao.findById(1);

      final Calendar dateS = Calendar.getInstance();

      r1.setDateSortie(dateS);

      retourManager.createOrUpdateObjectManager(r1, e2, null, c, null, null, i1, u, "creation");

      incomps = retourManager.findByObjectDateRetourEmptyManager(objsIds, entiteDao.findById(3));
      assertTrue(incomps.size() == 1);
      assertTrue(incomps.get(0).getIncident().equals(i1));
      assertTrue(echantillonDao.findById(2).getObjetStatut().getStatut().equals("ENCOURS"));

      final Retour r2 = new Retour();
      final Echantillon e1 = echantillonDao.findById(1);
      r2.setTempMoyenne(new Float(22.0));
      r2.setDateSortie(dateS);

      retourManager.createOrUpdateObjectManager(r2, e1, null, c, null, null, null, u, "creation");

      objsIds.add(1);
      incomps = retourManager.findByObjectDateRetourEmptyManager(objsIds, entiteDao.findById(3));
      assertTrue(incomps.size() == 2);

      retourManager.removeObjectManager(r1);
      retourManager.removeObjectManager(r2);

      //verifie que l'etat des tables modifies est revenu identique
      testFindAllObjectsManager();

      cleanUpFantomes(new ArrayList<TKFantomableObject>());

      assertTrue(echantillonDao.findById(2).getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(echantillonDao.findById(1).getObjetStatut().getStatut().equals("STOCKE"));

   }

   @Test
   @SuppressWarnings("deprecation")
   public void testUpdateMultipleObjectManager(){
      final Retour base = new Retour();

      /*Champs obligatoires*/
      final Echantillon e1 = echantillonDao.findById(1);
      final Echantillon e2 = echantillonDao.findById(2);
      final ProdDerive p1 = prodDeriveDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(1);

      final List<TKStockableObject> objs = new ArrayList<>();
      objs.add(e1);
      objs.add(e2);
      objs.add(p1);

      base.setTempMoyenne(new Float(22.0));

      final Collaborateur c = collaborateurDao.findById(2);
      final Incident i1 = incidentDao.findById(1);
      final Cession c1 = cessionDao.findById(1);
      final Transformation t1 = transformationDao.findById(1);

      base.setDateSortie(Calendar.getInstance());

      retourManager.createRetourListManager(objs, null, base, c, c1, t1, i1, u);

      assertTrue(prodDeriveDao.findById(1).getObjetStatut().getStatut().equals("ENCOURS"));
      assertTrue(echantillonDao.findById(2).getObjetStatut().getStatut().equals("ENCOURS"));
      assertTrue(prodDeriveDao.findById(1).getObjetStatut().getStatut().equals("ENCOURS"));

      assertTrue(retourManager.getRetoursForObjectManager(echantillonDao.findById(1)).size() == 6);

      final List<Integer> echanIds = new ArrayList<>();
      echanIds.add(1);
      echanIds.add(2);
      final List<Integer> pdIds = new ArrayList<>();
      pdIds.add(1);

      List<Retour> retours = retourManager.findByObjectDateRetourEmptyManager(echanIds, entiteDao.findById(3));
      assertTrue(retours.size() == 2);
      assertNotNull(retours.get(0).getIncident());
      assertNotNull(retours.get(0).getCession());
      assertNotNull(retours.get(0).getTransformation());
      assertNotNull(retours.get(1).getIncident());
      assertNotNull(retours.get(1).getCession());
      assertNotNull(retours.get(1).getTransformation());
      assertTrue(retourManager.getRetoursForObjectManager(echantillonDao.findById(2)).size() == 1);

      retours = retourManager.findByObjectDateRetourEmptyManager(pdIds, entiteDao.findById(8));
      assertTrue(retourManager.getRetoursForObjectManager(prodDeriveDao.findById(1)).size() == 1);
      assertTrue(retours.size() == 1);
      assertNotNull(retours.get(0).getIncident());
      assertNotNull(retours.get(0).getCession());
      assertNotNull(retours.get(0).getTransformation());

      final Calendar dateR = Calendar.getInstance();
      echanIds.remove(new Integer(2));
      final Retour r1 = retourManager.findByObjectDateRetourEmptyManager(echanIds, entiteDao.findById(3)).get(0);
      r1.setDateRetour(dateR);
      Retour r2 = retourManager.getRetoursForObjectManager(echantillonDao.findById(2)).get(0);
      r2.setDateRetour(dateR);
      r2.setTempMoyenne(new Float(0.0));
      final Retour r3 = retourManager.getRetoursForObjectManager(prodDeriveDao.findById(1)).get(0);
      r3.setDateRetour(dateR);

      retours.clear();
      retours.add(r1);
      retours.add(r2);
      retours.add(r3);

      retourManager.updateMultipleObjectManager(retours, c, null, null, null, u);

      // re attribution des statuts
      assertTrue(prodDeriveDao.findById(1).getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(echantillonDao.findById(2).getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(prodDeriveDao.findById(1).getObjetStatut().getStatut().equals("STOCKE"));

      r2 = retourManager.getRetoursForObjectManager(e2).get(0);
      assertTrue(r2.getTempMoyenne().equals(new Float(0.0)));
      assertNotNull(r2.getIncident());
      assertNotNull(r2.getCession());
      assertNotNull(r2.getTransformation());

      // clean up
      retourManager.removeObjectManager(r1);
      retourManager.removeObjectManager(r2);
      retourManager.removeObjectManager(r3);

      //verifie que l'etat des tables modifies est revenu identique
      testFindAllObjectsManager();

      cleanUpFantomes(new ArrayList<TKFantomableObject>());

      // null test
      retourManager.updateMultipleObjectManager(null, null, null, null, i1, null);
   }

   @Test
   public void testFindByObjectAndImpactManager(){
      List<Retour> impacts = retourManager.findByObjectAndImpactManager(echantillonDao.findById(1), true);
      assertTrue(impacts.size() == 3);
      impacts = retourManager.findByObjectAndImpactManager(echantillonDao.findById(1), false);
      assertTrue(impacts.size() == 1);
      impacts = retourManager.findByObjectAndImpactManager(echantillonDao.findById(1), null);
      assertTrue(impacts.size() == 0);
      impacts = retourManager.findByObjectAndImpactManager(prodDeriveDao.findById(3), false);
      assertTrue(impacts.size() == 2);
      impacts = retourManager.findByObjectAndImpactManager(null, false);
      assertTrue(impacts.size() == 0);
   }
}
