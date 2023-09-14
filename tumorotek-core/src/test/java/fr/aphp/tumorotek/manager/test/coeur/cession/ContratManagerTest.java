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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.cession.ContratManager;
import fr.aphp.tumorotek.manager.coeur.cession.ProtocoleTypeManager;
import fr.aphp.tumorotek.manager.context.CollaborateurManager;
import fr.aphp.tumorotek.manager.context.ServiceManager;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.coeur.cession.ContratValidator;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.cession.ProtocoleType;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager ProtocoleExtManager.
 * Classe créée le 27/01/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ContratManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ContratManager contratManager;

   @Autowired
   private CollaborateurManager collaborateurManager;

   @Autowired
   private ServiceManager serviceManager;

   @Autowired
   private ProtocoleTypeManager protocoleTypeManager;

   @Autowired
   private UtilisateurDao utilisateurDao;

   @Autowired
   private EtablissementDao etablissementDao;

   @Autowired
   private PlateformeDao plateformeDao;

   public ContratManagerTest(){}

   @Test
   public void testFindById(){
      final Contrat mta = contratManager.findByIdManager(1);
      assertNotNull(mta);
      assertTrue(mta.getNumero().equals("CONTRAT 78551269"));

      final Contrat mtaNull = contratManager.findByIdManager(10);
      assertNull(mtaNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Contrat> list = contratManager.findAllObjectsManager();
      assertTrue(list.size() == 4);
   }

   /**
    * Test la méthode findAllObjectsByPlateformeManager.
    */
   @Test
   public void testFindAllObjectsByPlateformeManager(){
      final Plateforme pf1 = plateformeDao.findById(1);
      List<Contrat> list = contratManager.findAllObjectsByPlateformeManager(pf1);
      assertTrue(list.size() == 3);

      final Plateforme pf2 = plateformeDao.findById(2);
      list = contratManager.findAllObjectsByPlateformeManager(pf2);
      assertTrue(list.size() == 1);

      list = contratManager.findAllObjectsByPlateformeManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode getCessionsManager.
    */
   @Test
   public void testGetCessionsManager(){
      Contrat contrat = contratManager.findByIdManager(1);
      List<Cession> cessions = contratManager.getCessionsManager(contrat);
      assertTrue(cessions.size() == 1);

      contrat = contratManager.findByIdManager(2);
      cessions = contratManager.getCessionsManager(contrat);
      assertTrue(cessions.size() == 0);

      cessions = contratManager.getCessionsManager(null);
      assertTrue(cessions.size() == 0);
   }

   /**
    * Test la méthode findByNumeroLikeManager.
    */
   @Test
   public void testFindByNumeroLikeExactManager(){
      List<Contrat> list = contratManager.findByNumeroLikeManager("CONTRAT 78551269", true);
      assertTrue(list.size() == 1);

      list = contratManager.findByNumeroLikeManager("CONTRAT", true);
      assertTrue(list.size() == 0);

      list = contratManager.findByNumeroLikeManager("", true);
      assertTrue(list.size() == 0);

      list = contratManager.findByNumeroLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByNumeroLikeManager.
    */
   @Test
   public void testFindByNumeroLikeManager(){
      List<Contrat> list = contratManager.findByNumeroLikeManager("CONTRAT 78551269", false);
      assertTrue(list.size() == 1);

      list = contratManager.findByNumeroLikeManager("CONTRAT", false);
      assertTrue(list.size() == 3);

      list = contratManager.findByNumeroLikeManager("", false);
      assertTrue(list.size() == 4);

      list = contratManager.findByNumeroLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){

      final String numero1 = "CONTRAT 78551269";
      final String numero2 = "MTA";
      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);

      final Contrat mta1 = new Contrat();
      mta1.setNumero(numero1);
      mta1.setPlateforme(pf1);
      assertTrue(contratManager.findDoublonManager(mta1));

      mta1.setPlateforme(pf2);
      assertFalse(contratManager.findDoublonManager(mta1));
      mta1.setPlateforme(pf1);

      mta1.setNumero(numero2);
      assertFalse(contratManager.findDoublonManager(mta1));

      final Contrat mta2 = contratManager.findByIdManager(2);
      assertFalse(contratManager.findDoublonManager(mta2));

      mta2.setNumero(numero1);
      assertTrue(contratManager.findDoublonManager(mta2));

      assertFalse(contratManager.findDoublonManager(null));

   }

   /**
    * Test la méthode isUsedObjectManager.
    */
   @Test
   public void testIsUsedObjectManager(){
      Contrat mta = contratManager.findByIdManager(1);
      assertTrue(contratManager.isUsedObjectManager(mta));

      mta = contratManager.findByIdManager(2);
      assertFalse(contratManager.isUsedObjectManager(mta));

      assertFalse(contratManager.isUsedObjectManager(null));
   }

   /**
    * Test le CRUD d'un ProtocoleExt.
    * @throws ParseException 
    */
   @Test
   public void testCrud() throws ParseException{
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   /**
    * Teste la methode createObjectManager. 
    * @throws ParseException 
    */
   private void createObjectManagerTest() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Plateforme pf1 = plateformeDao.findById(1);
      final Collaborateur collab = collaborateurManager.findByIdManager(1);
      final Service serv = serviceManager.findByIdManager(1);
      final Etablissement etab = etablissementDao.findById(1);
      final ProtocoleType type = protocoleTypeManager.findByIdManager(1);

      final Contrat contrat1 = new Contrat();
      contrat1.setNumero("NUM1");

      Boolean catched = false;
      // on test l'insertion avec la pf nulle
      try{
         contratManager.createObjectManager(contrat1, null, null, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(contratManager.findAllObjectsManager().size() == 4);

      // on test l'insertion d'un doublon
      contrat1.setNumero("CONTRAT 78551269");
      try{
         contratManager.createObjectManager(contrat1, null, null, null, null, pf1, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(contratManager.findAllObjectsManager().size() == 4);

      // Test de la validation lors de la création
      try{
         validationInsert(contrat1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(contratManager.findAllObjectsManager().size() == 4);

      // on teste une insertion valide avec les associations 
      // non obigatoires nulles
      contrat1.setNumero("NUM");
      contratManager.createObjectManager(contrat1, null, null, null, null, pf1, u);
      assertTrue(contratManager.findAllObjectsManager().size() == 5);
      assertTrue(getOperationManager().findByObjectManager(contrat1).size() == 1);
      final int id = contrat1.getContratId();

      // On test une insertion valide
      final Contrat contrat2 = new Contrat();
      final Date demande = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
      final Date validation = new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2009");
      final Date demandeRedac = new SimpleDateFormat("dd/MM/yyyy").parse("05/10/2009");
      final Date envoi = new SimpleDateFormat("dd/MM/yyyy").parse("10/10/2009");
      final Date signature = new SimpleDateFormat("dd/MM/yyyy").parse("15/10/2009");
      final Float montant = new Float(142);
      contrat2.setNumero("TEST");
      contrat2.setDateDemandeCession(demande);
      contrat2.setDateValidation(validation);
      contrat2.setDateDemandeRedaction(demandeRedac);
      contrat2.setDateEnvoiContrat(envoi);
      contrat2.setDateSignature(signature);
      contrat2.setTitreProjet("Titre");
      contrat2.setDescription("DESC");
      contrat2.setMontant(montant);
      contratManager.createObjectManager(contrat2, collab, serv, etab, type, pf1, u);
      assertTrue(contratManager.findAllObjectsManager().size() == 6);
      assertTrue(getOperationManager().findByObjectManager(contrat2).size() == 1);
      final int id2 = contrat2.getContratId();

      // Vérification
      final Contrat contratTest = contratManager.findByIdManager(id2);
      assertNotNull(contratTest);
      assertNotNull(contratTest.getCollaborateur());
      assertNotNull(contratTest.getService());
      assertNotNull(contratTest.getProtocoleType());
      assertNotNull(contratTest.getEtablissement());
      assertNotNull(contratTest.getPlateforme());
      assertTrue(contratTest.getNumero().equals("TEST"));
      assertTrue(contratTest.getDateDemandeCession().equals(demande));
      assertTrue(contratTest.getDateValidation().equals(validation));
      assertTrue(contratTest.getDateDemandeRedaction().equals(demandeRedac));
      assertTrue(contratTest.getDateEnvoiContrat().equals(envoi));
      assertTrue(contratTest.getDateSignature().equals(signature));
      assertTrue(contratTest.getTitreProjet().equals("Titre"));
      assertTrue(contratTest.getDescription().equals("DESC"));
      assertTrue(contratTest.getMontant().equals(montant));

      // Suppression
      final Contrat cToRemove = contratManager.findByIdManager(id);
      contratManager.removeObjectManager(cToRemove, null, u);
      contratManager.removeObjectManager(contratTest, null, u);
      assertTrue(contratManager.findAllObjectsManager().size() == 4);
      assertTrue(getOperationManager().findByObjectManager(cToRemove).size() == 0);
      assertTrue(getOperationManager().findByObjectManager(contratTest).size() == 0);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(cToRemove);
      fs.add(contratTest);
      cleanUpFantomes(fs);
   }

   /**
    * Teste la methode createObjectManager. 
    * @throws ParseException 
    */
   private void updateObjectManagerTest() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Plateforme pf1 = plateformeDao.findById(1);
      final Collaborateur collab = collaborateurManager.findByIdManager(1);
      final Service serv = serviceManager.findByIdManager(1);
      final Etablissement etab = etablissementDao.findById(1);
      final ProtocoleType type = protocoleTypeManager.findByIdManager(1);

      final Contrat contrat = new Contrat();
      contrat.setNumero("NUM1");
      contratManager.createObjectManager(contrat, null, null, null, null, pf1, u);
      assertTrue(contratManager.findAllObjectsManager().size() == 5);
      assertTrue(getOperationManager().findByObjectManager(contrat).size() == 1);
      final int id = contrat.getContratId();

      final Contrat contrat2 = contratManager.findByIdManager(id);
      Boolean catched = false;
      // on test l'update avec la pf nulle
      try{
         contratManager.updateObjectManager(contrat2, null, null, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(contratManager.findAllObjectsManager().size() == 5);

      // on test l'insertion d'un doublon
      contrat2.setNumero("CONTRAT 78551269");
      try{
         contratManager.updateObjectManager(contrat2, null, null, null, null, pf1, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(contratManager.findAllObjectsManager().size() == 5);

      // Test de la validation lors de la création
      try{
         validationUpdate(contrat2);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(contratManager.findAllObjectsManager().size() == 5);

      // on teste une modif valide avec les associations 
      // non obigatoires nulles
      contrat2.setNumero("NUMUP");
      contratManager.updateObjectManager(contrat2, null, null, null, null, pf1, u);
      assertTrue(contratManager.findAllObjectsManager().size() == 5);
      assertTrue(getOperationManager().findByObjectManager(contrat2).size() == 2);
      final Contrat contrat3 = contratManager.findByIdManager(id);
      assertTrue(contrat3.getNumero().equals("NUMUP"));

      // On test une modif valide
      final Date demande = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
      final Date validation = new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2009");
      final Date demandeRedac = new SimpleDateFormat("dd/MM/yyyy").parse("05/10/2009");
      final Date envoi = new SimpleDateFormat("dd/MM/yyyy").parse("10/10/2009");
      final Date signature = new SimpleDateFormat("dd/MM/yyyy").parse("15/10/2009");
      final Float montant = new Float(124);
      contrat3.setNumero("TEST");
      contrat3.setDateDemandeCession(demande);
      contrat3.setDateValidation(validation);
      contrat3.setDateDemandeRedaction(demandeRedac);
      contrat3.setDateEnvoiContrat(envoi);
      contrat3.setDateSignature(signature);
      contrat3.setTitreProjet("Titre");
      contrat3.setDescription("DESC");
      contrat3.setMontant(montant);
      contratManager.updateObjectManager(contrat3, collab, serv, etab, type, pf1, u);
      assertTrue(contratManager.findAllObjectsManager().size() == 5);
      assertTrue(getOperationManager().findByObjectManager(contrat3).size() == 3);

      // Vérification
      final Contrat contratTest = contratManager.findByIdManager(id);
      assertNotNull(contratTest);
      assertNotNull(contratTest.getCollaborateur());
      assertNotNull(contratTest.getService());
      assertNotNull(contratTest.getProtocoleType());
      assertNotNull(contratTest.getEtablissement());
      assertNotNull(contratTest.getPlateforme());
      assertTrue(contratTest.getNumero().equals("TEST"));
      assertTrue(contratTest.getDateDemandeCession().equals(demande));
      assertTrue(contratTest.getDateValidation().equals(validation));
      assertTrue(contratTest.getDateDemandeRedaction().equals(demandeRedac));
      assertTrue(contratTest.getDateEnvoiContrat().equals(envoi));
      assertTrue(contratTest.getDateSignature().equals(signature));
      assertTrue(contratTest.getTitreProjet().equals("Titre"));
      assertTrue(contratTest.getDescription().equals("DESC"));
      assertTrue(contratTest.getMontant().equals(montant));

      // Suppression
      contratManager.removeObjectManager(contratTest, null, u);
      assertTrue(contratManager.findAllObjectsManager().size() == 4);
      assertTrue(getOperationManager().findByObjectManager(contratTest).size() == 0);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(contratTest);
      cleanUpFantomes(fs);
   }

   /**
    * Teste la methode removeObjectManager. 
    */
   private void removeObjectManagerTest(){
      final Utilisateur u = utilisateurDao.findById(1);
      // test de la suppression d'un objet null
      contratManager.removeObjectManager(null, null, null);
      assertTrue(contratManager.findAllObjectsManager().size() == 4);

      // test de la suppression d'un objet utilisé
      final Contrat contrat = contratManager.findByIdManager(1);
      boolean catched = false;
      try{
         contratManager.removeObjectManager(contrat, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
            assertTrue(((ObjectUsedException) e).getKey().equals("contrat.deletion.isReferenced"));
         }
      }
      assertTrue(catched);
      assertTrue(contratManager.findAllObjectsManager().size() == 4);
   }

   /**
    * Test la validation d'un protocoleExt lors de sa création.
    * @param contrat Contrat à tester.
    * @throws ParseException 
    */
   private void validationInsert(final Contrat contrat) throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Plateforme pf = plateformeDao.findById(1);
      boolean catchedInsert = false;
      // On teste une insertion avec un attribut numéro non valide
      final String[] emptyValues = new String[] {"", "  ", null, "%$*gd¤¤", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            contrat.setNumero(emptyValues[i]);
            contratManager.createObjectManager(contrat, null, null, null, null, pf, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      contrat.setNumero("test");

      // On teste une insertion avec un attribut titre non valide
      final String[] titreValues = new String[] {"", "  ", "%$*gd¤¤", createOverLength(100)};
      for(int i = 0; i < titreValues.length; i++){
         catchedInsert = false;
         try{
            contrat.setTitreProjet(titreValues[i]);
            contratManager.createObjectManager(contrat, null, null, null, null, pf, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      contrat.setTitreProjet("titre");

      // On teste une insertion avec un attribut description non valide
      final String[] descValues = new String[] {"", "  "};
      for(int i = 0; i < descValues.length; i++){
         catchedInsert = false;
         try{
            contrat.setDescription(descValues[i]);
            contratManager.createObjectManager(contrat, null, null, null, null, pf, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      contrat.setDescription("desc");

   }

   /**
    * Test la validation d'un protocoleExt lors de son update.
    * @param mta Contrat à tester.
    * @throws ParseException 
    */
   private void validationUpdate(final Contrat contrat) throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Plateforme pf = plateformeDao.findById(1);
      boolean catched = false;
      // On teste un update avec un attribut numéro non valide
      final String[] emptyValues = new String[] {"", "  ", null, "¤¤%$*gd", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            contrat.setNumero(emptyValues[i]);
            contratManager.updateObjectManager(contrat, null, null, null, null, pf, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      contrat.setNumero("test");

      // On teste un update avec un attribut titre non valide
      final String[] titreValues = new String[] {"", "  ", "%$*gd¤¤", createOverLength(100)};
      for(int i = 0; i < titreValues.length; i++){
         catched = false;
         try{
            contrat.setTitreProjet(titreValues[i]);
            contratManager.updateObjectManager(contrat, null, null, null, null, pf, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      contrat.setTitreProjet("titre");

      // On teste un update avec un attribut description non valide
      final String[] descValues = new String[] {"", "  "};
      for(int i = 0; i < descValues.length; i++){
         catched = false;
         try{
            contrat.setDescription(descValues[i]);
            contratManager.updateObjectManager(contrat, null, null, null, null, pf, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      contrat.setDescription("desc");

   }

   @Test
   public void testDemandeDateCoherence() throws ParseException{
      final Contrat c = new Contrat();

      // null validation
      c.setDateDemandeCession(null);
      Errors errs = ContratValidator.checkDemandeDateCoherence(c);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites sup
      c.setDateValidation(new SimpleDateFormat("dd/MM/yyyy").parse("13/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertEquals("date.validation.supDateValidation", errs.getFieldError().getCode());

      c.setDateValidation(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertTrue(errs.getAllErrors().size() == 0);

      c.setDateValidation(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateValidation(null);

      // date de demande de rédaction
      c.setDateDemandeRedaction(new SimpleDateFormat("dd/MM/yyyy").parse("13/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertEquals("date.validation.supDateDemandeRedaction", errs.getFieldError().getCode());

      c.setDateDemandeRedaction(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertTrue(errs.getAllErrors().size() == 0);

      c.setDateDemandeRedaction(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateDemandeRedaction(null);

      // date d'envoi du contrat
      c.setDateEnvoiContrat(new SimpleDateFormat("dd/MM/yyyy").parse("13/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertEquals("date.validation.supDateEnvoiContrat", errs.getFieldError().getCode());

      c.setDateEnvoiContrat(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertTrue(errs.getAllErrors().size() == 0);

      c.setDateEnvoiContrat(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateEnvoiContrat(null);

      // date de signature
      c.setDateSignature(new SimpleDateFormat("dd/MM/yyyy").parse("13/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertEquals("date.validation.supDateSignature", errs.getFieldError().getCode());

      c.setDateSignature(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertTrue(errs.getAllErrors().size() == 0);

      c.setDateSignature(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDemandeDateCoherence(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateSignature(null);
   }

   @Test
   public void testValidationDateCoherence() throws ParseException{
      final Contrat c = new Contrat();

      // null validation
      c.setDateValidation(null);
      Errors errs = ContratValidator.checkDateValidation(c);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      c.setDateValidation(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateValidation(c);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDateValidation(c);
      assertEquals("date.validation.infDemandeCession", errs.getFieldError().getCode());

      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateValidation(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("13/07/2010"));
   }

   @Test
   public void testDateDemandeRedactionCoherence() throws ParseException{
      final Contrat c = new Contrat();

      // null redaction
      c.setDateDemandeRedaction(null);
      Errors errs = ContratValidator.checkDateDemandeRedaction(c);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      c.setDateDemandeRedaction(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateDemandeRedaction(c);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      // date demande de cession
      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDateDemandeRedaction(c);
      assertEquals("date.validation.infDemandeCession", errs.getFieldError().getCode());

      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateDemandeRedaction(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("13/07/2010"));

      // limtes sup
      // date d'envoi du contrat
      c.setDateEnvoiContrat(new SimpleDateFormat("dd/MM/yyyy").parse("13/07/2010"));
      errs = ContratValidator.checkDateDemandeRedaction(c);
      assertEquals("date.validation.supDateEnvoiContrat", errs.getFieldError().getCode());

      c.setDateEnvoiContrat(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDateDemandeRedaction(c);
      assertTrue(errs.getAllErrors().size() == 0);

      c.setDateEnvoiContrat(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateDemandeRedaction(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateEnvoiContrat(null);

      // date de signature
      c.setDateSignature(new SimpleDateFormat("dd/MM/yyyy").parse("13/07/2010"));
      errs = ContratValidator.checkDateDemandeRedaction(c);
      assertEquals("date.validation.supDateSignature", errs.getFieldError().getCode());

      c.setDateSignature(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDateDemandeRedaction(c);
      assertTrue(errs.getAllErrors().size() == 0);

      c.setDateSignature(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateDemandeRedaction(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateSignature(null);
   }

   @Test
   public void testDateEnvoiContratCoherence() throws ParseException{
      final Contrat c = new Contrat();

      // null redaction
      c.setDateEnvoiContrat(null);
      Errors errs = ContratValidator.checkDateEnvoiContrat(c);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      c.setDateEnvoiContrat(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateEnvoiContrat(c);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      // date redaction
      c.setDateDemandeRedaction(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDateEnvoiContrat(c);
      assertEquals("date.validation.infDateDemandeRedaction", errs.getFieldError().getCode());

      c.setDateDemandeRedaction(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateEnvoiContrat(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateDemandeRedaction(null);
      // date demande de cession
      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDateEnvoiContrat(c);
      assertEquals("date.validation.infDemandeCession", errs.getFieldError().getCode());

      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateEnvoiContrat(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("13/07/2010"));

      // limtes sup
      // date de signature
      c.setDateSignature(new SimpleDateFormat("dd/MM/yyyy").parse("13/07/2010"));
      errs = ContratValidator.checkDateEnvoiContrat(c);
      assertEquals("date.validation.supDateSignature", errs.getFieldError().getCode());

      c.setDateSignature(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDateEnvoiContrat(c);
      assertTrue(errs.getAllErrors().size() == 0);

      c.setDateSignature(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateEnvoiContrat(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateSignature(null);
   }

   @Test
   public void testDateSignatureCoherence() throws ParseException{
      final Contrat c = new Contrat();

      // null signature
      c.setDateSignature(null);
      Errors errs = ContratValidator.checkDateSignature(c);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      c.setDateSignature(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateSignature(c);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      // date envoi
      c.setDateEnvoiContrat(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDateSignature(c);
      assertEquals("date.validation.infDateEnvoiContrat", errs.getFieldError().getCode());

      c.setDateEnvoiContrat(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateSignature(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateEnvoiContrat(null);
      // date redaction
      c.setDateDemandeRedaction(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDateSignature(c);
      assertEquals("date.validation.infDateDemandeRedaction", errs.getFieldError().getCode());

      c.setDateDemandeRedaction(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateSignature(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateDemandeRedaction(null);
      // date demande de cession
      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2010"));
      errs = ContratValidator.checkDateSignature(c);
      assertEquals("date.validation.infDemandeCession", errs.getFieldError().getCode());

      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("14/07/2010"));
      errs = ContratValidator.checkDateSignature(c);
      assertTrue(errs.getAllErrors().size() == 0);
      c.setDateDemandeCession(new SimpleDateFormat("dd/MM/yyyy").parse("13/07/2010"));
   }

}
