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
package fr.aphp.tumorotek.manager.test.code;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.code.CodeDossierManager;
import fr.aphp.tumorotek.manager.code.CodeSelectManager;
import fr.aphp.tumorotek.manager.code.CodeUtilisateurManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.code.CodeDossierValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager CodeDossierManager.
 * Classe créée le 06/06/10.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class CodeDossierManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CodeDossierManager codeDossierManager;
   @Autowired
   private CodeUtilisateurManager codeUtilisateurManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private OperationManager operationManager;
   @Autowired
   private CodeDossierValidator codeDossierValidator;
   @Autowired
   private TableCodageDao tableCodageDao;
   @Autowired
   private CodeSelectManager codeSelectManager;
   @Autowired
   private OperationTypeDao operationTypeDao;

   public CodeDossierManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<CodeDossier> dos = codeDossierManager.findAllCodeDossiersManager();
      assertTrue(dos.size() == 4);
   }

   @Test
   public void testFindByNomLikeManager(){
      final Banque b = banqueDao.findById(1).orElse(null);
      //teste une recherche exactMatch
      List<CodeDossier> dos = codeDossierManager.findByNomLikeManager("Dossier2", true, b);
      assertTrue(dos.size() == 1);
      //teste une recherche non exactMatch
      dos = codeDossierManager.findByNomLikeManager("User", false, b);
      assertTrue(dos.size() == 2);
      //teste une recherche infructueuse
      dos = codeDossierManager.findByNomLikeManager("LUX", true, b);
      assertTrue(dos.size() == 0);
      //null recherche
      dos = codeDossierManager.findByNomLikeManager(null, false, b);
      assertTrue(dos.size() == 0);
   }

   @Test
   public void testFindByCodeDossierParentManager(){
      final CodeDossier d1 = codeDossierManager.findAllCodeDossiersManager().get(0);
      List<CodeDossier> dos = codeDossierManager.findByCodeDossierParentManager(d1);
      assertTrue(dos.size() == 1);
      assertTrue(dos.get(0).getNom().equals("Dossier2"));
      final CodeDossier d2 = codeDossierManager.findAllCodeDossiersManager().get(1);
      dos = codeDossierManager.findByCodeDossierParentManager(d2);
      assertTrue(dos.size() == 0);
      //null recherche
      dos = codeDossierManager.findByCodeDossierParentManager(null);
      assertTrue(dos.size() == 0);
   }

   @Test
   public void testFindByRootDossierUtilisateurManager(){
      Banque b = banqueDao.findById(1).orElse(null);
      List<CodeDossier> dos = codeDossierManager.findByRootCodeDossierUtilisateurManager(b);
      assertTrue(dos.size() == 2);
      b = banqueDao.findById(2).orElse(null);
      dos = codeDossierManager.findByRootCodeDossierUtilisateurManager(b);
      assertTrue(dos.size() == 0);
   }

   @Test
   public void testFindByRootSelectUtilisateurManager(){
      final Banque b = banqueDao.findById(1).orElse(null);
      Utilisateur u = utilisateurDao.findById(1).orElse(null);
      List<CodeDossier> dos = codeDossierManager.findByRootCodeDossierSelectManager(u, b);
      assertTrue(dos.size() == 1);
      assertTrue(dos.get(0).getCodeDossierId() == 3);
      u = utilisateurDao.findById(2).orElse(null);
      dos = codeDossierManager.findByRootCodeDossierSelectManager(u, b);
      assertTrue(dos.size() == 0);
   }

   @Test
   public void testFindByUtilisateurAndBanqueManager(){
      Utilisateur u = utilisateurDao.findById(1).orElse(null);
      Banque b = banqueDao.findById(1).orElse(null);
      List<CodeDossier> dos = codeDossierManager.findByUtilisateurAndBanqueManager(u, b);
      assertTrue(dos.size() == 2);
      u = utilisateurDao.findById(2).orElse(null);
      dos = codeDossierManager.findByUtilisateurAndBanqueManager(u, b);
      assertTrue(dos.size() == 1);
      assertTrue(dos.get(0).getNom().equals("DossierUser2"));
      b = banqueDao.findById(2).orElse(null);
      dos = codeDossierManager.findByUtilisateurAndBanqueManager(u, b);
      assertTrue(dos.size() == 0);
      //null recherche
      dos = codeDossierManager.findByUtilisateurAndBanqueManager(null, null);
      assertTrue(dos.size() == 0);
   }

   @Test
   public void testFindBySelectUtilisateurAndBanqueManager(){
      Utilisateur u = utilisateurDao.findById(1).orElse(null);
      final Banque b = banqueDao.findById(1).orElse(null);
      List<CodeDossier> dos = codeDossierManager.findBySelectUtilisateurAndBanqueManager(u, b);
      assertTrue(dos.size() == 1);
      assertTrue(dos.get(0).getNom().equals("DossierFavoris"));
      u = utilisateurDao.findById(2).orElse(null);
      dos = codeDossierManager.findBySelectUtilisateurAndBanqueManager(u, b);
      assertTrue(dos.size() == 0);
      dos = codeDossierManager.findBySelectUtilisateurAndBanqueManager(null, null);
      assertTrue(dos.size() == 0);
   }

   @Test
   public void testFindDoublonManager(){
      final Banque b = banqueDao.findById(1).orElse(null);
      //Cree le doublon
      final CodeDossier d1 = codeDossierManager.findByNomLikeManager("Dossier2", true, b).get(0);

      final CodeDossier d2 = new CodeDossier();
      d2.setNom(d1.getNom());
      d2.setUtilisateur(d1.getUtilisateur());
      d2.setBanque(d1.getBanque());
      assertTrue(codeDossierManager.findDoublonManager(d2));

      d1.setNom("DossierUser1");
      assertTrue(codeDossierManager.findDoublonManager(d1));
   }

   @Test
   public void testCRUD(){
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   public void createObjectManagerTest(){

      final CodeDossier dos = new CodeDossier();
      /*Champs obligatoires*/
      dos.setNom("newDossier");
      final Banque b = banqueDao.findById(1).orElse(null);
      final Utilisateur u = utilisateurDao.findById(2).orElse(null);
      dos.setCodeSelect(false);
      final CodeDossier parent = codeDossierManager.findAllCodeDossiersManager().get(3);

      //required Entite
      try{
         codeDossierManager.createOrUpdateManager(dos, null, null, u, "creation");
      }catch(final RequiredObjectIsNullException re){
         assertTrue(true);
      }
      try{
         codeDossierManager.createOrUpdateManager(dos, null, b, null, "creation");
      }catch(final RequiredObjectIsNullException re){
         assertTrue(true);
      }
      //operation invalide
      try{
         codeDossierManager.createOrUpdateManager(dos, dos, b, u, null);
      }catch(final NullPointerException ne){
         assertTrue(ne.getMessage().equals("operation cannot be " + "set to null for createorUpdateMethod"));
      }
      try{
         codeDossierManager.createOrUpdateManager(dos, null, b, u, "");
      }catch(final IllegalArgumentException ie){
         assertTrue(ie.getMessage().equals("Operation must match " + "'creation/modification' values"));
      }
      testFindAllObjectsManager();
      codeDossierManager.createOrUpdateManager(dos, parent, b, u, "creation");
      assertTrue((codeDossierManager.findByNomLikeManager("newDossier", true, b)).size() == 1);
      assertTrue(codeDossierManager.findByCodeDossierParentManager(parent).size() == 1);

      assertTrue(operationManager.findByObjectManager(dos).size() == 1);
      assertTrue(operationManager.findByObjectManager(dos).get(0).getOperationType().getNom().equals("Creation"));

      //Insertion d'un doublon engendrant une exception
      final CodeDossier d2 = new CodeDossier();
      d2.setNom(dos.getNom());
      d2.setBanque(b);
      d2.setUtilisateur(u);
      assertTrue(dos.equals(d2));
      Boolean catched = false;
      try{
         codeDossierManager.createOrUpdateManager(d2, null, null, null, "creation");
      }catch(final Exception ex){
         if(ex.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((codeDossierManager.findByNomLikeManager("newDossier", true, b)).size() == 1);
      assertTrue(operationManager.findByObjectManager(dos).size() == 1);

      // creation code appartenant au dossier
      final CodeUtilisateur herited = new CodeUtilisateur();
      herited.setCode("file");
      codeUtilisateurManager.createOrUpdateManager(herited, dos, b, u, null, null, "creation");
      assertTrue(codeUtilisateurManager.findByCodeDossierManager(dos).size() == 1);

      // creation sous-dossier
      final CodeDossier sousdos = new CodeDossier();
      sousdos.setNom("sousdos");
      sousdos.setCodeSelect(false);
      codeDossierManager.createOrUpdateManager(sousdos, dos, b, u, "creation");
      assertTrue(codeDossierManager.findByCodeDossierParentManager(dos).size() == 1);
   }

   private void updateObjectManagerTest(){
      final Banque b = banqueDao.findById(1).orElse(null);
      final CodeDossier d = codeDossierManager.findByNomLikeManager("newDossier", true, b).get(0);
      //c.setCode("AnotherCode%£µ%£&&");
      d.setNom("###~¯°×");
      d.setDescription("nouvelle description");
      Boolean catched = false;
      //Modification d'un champ entrainant validationException
      try{
         codeDossierManager.createOrUpdateManager(d, null, null, null, "modification");
      }catch(final ValidationException e){
         catched = true;
      }
      assertTrue(catched);

      d.setNom("AnotherDossier");
      codeDossierManager.createOrUpdateManager(d, null, null, null, "modification");
      assertTrue((codeDossierManager.findByNomLikeManager("AnotherDossier", true, b)).size() == 1);

      assertTrue(operationManager.findByObjectManager(d).size() == 2);
      assertTrue(
         operationManager.findByObjetIdEntiteAndOpeTypeManager(d, operationTypeDao.findByNom("Modification").get(0)).size() == 1);
      //Modification en un doublon engendrant une exception
      catched = false;
      try{
         d.setNom("DossierUser2");
         codeDossierManager.createOrUpdateManager(d, null, null, null, "modification");
      }catch(final DoublonFoundException e){
         catched = true;
      }
      assertTrue(catched);
      assertTrue((codeDossierManager.findByNomLikeManager("AnotherDossier", true, b)).size() == 1);
   }

   private void removeObjectManagerTest(){
      final Banque b = banqueDao.findById(1).orElse(null);
      final CodeDossier d = codeDossierManager.findByNomLikeManager("AnotherDossier", true, b).get(0);
      codeDossierManager.removeObjectManager(d);
      assertTrue((codeDossierManager.findByNomLikeManager("AnotherDossier", true, b)).size() == 0);
      assertTrue(operationManager.findByObjectManager(d).size() == 0);
      //verifie que l'etat des tables modifies est revenu identique
      assertTrue(codeUtilisateurManager.findAllObjectsManager().size() == 6);
      assertTrue(operationManager.findAllObjectsManager().size() == 19);
      testFindAllObjectsManager();
      // null remove
      codeDossierManager.removeObjectManager(null);
   }

   @Test
   public void testNomValidation(){
      final CodeDossier d = new CodeDossier();

      // nom
      List<Errors> errs = new ArrayList<>();
      try{
         BeanValidator.validateObject(d, new Validator[] {codeDossierValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("dossier.nom.empty"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      d.setNom("");
      try{
         BeanValidator.validateObject(d, new Validator[] {codeDossierValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("dossier.nom.empty"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      d.setNom("$$###'¤¤");
      try{
         BeanValidator.validateObject(d, new Validator[] {codeDossierValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("dossier.nom.illegal"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      d.setNom(createOverLength(25));
      try{
         BeanValidator.validateObject(d, new Validator[] {codeDossierValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("dossier.nom.tooLong"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
   }

   @Test
   public void testcreateAndRemoveSelectDossierManagerTest(){

      final CodeDossier dos = new CodeDossier();
      /*Champs obligatoires*/
      dos.setNom("newDossierSelect");
      final Banque b = banqueDao.findById(1).orElse(null);
      final Utilisateur u = utilisateurDao.findById(2).orElse(null);
      dos.setCodeSelect(true);

      codeDossierManager.createOrUpdateManager(dos, null, b, u, "creation");
      assertTrue((codeDossierManager.findByNomLikeManager("newDossierSelect", true, b)).size() == 1);

      // creation codes appartenant au dossier
      final CodeSelect cs1 = new CodeSelect();
      cs1.setCodeId(28);
      cs1.setTableCodage(tableCodageDao.findById(3)).orElse(null);
      codeSelectManager.createOrUpdateManager(cs1, dos, b, u, "creation");
      assertTrue(codeSelectManager.findByCodeDossierManager(dos).size() == 1);

      final CodeSelect cs2 = new CodeSelect();
      cs2.setCodeId(32);
      cs2.setTableCodage(tableCodageDao.findById(3)).orElse(null);
      codeSelectManager.createOrUpdateManager(cs2, dos, b, u, "creation");
      assertTrue(codeSelectManager.findByCodeDossierManager(dos).size() == 2);

      // remove 
      codeDossierManager.removeObjectManager(dos);
      assertTrue((codeDossierManager.findByNomLikeManager("newDossierSelect", true, b)).size() == 0);
      assertTrue(operationManager.findByObjectManager(dos).size() == 0);
      //verifie que l'etat des tables modifies est revenu identique
      assertTrue(codeSelectManager.findAllObjectsManager().size() == 5);
      assertTrue(operationManager.findAllObjectsManager().size() == 19);
      testFindAllObjectsManager();
   }

   @Test
   public void testFindByRootDossierBanqueManager(){
      Banque b = banqueDao.findById(1).orElse(null);
      List<CodeDossier> doss = codeDossierManager.findByRootDossierBanqueManager(b, true);
      assertTrue(doss.size() == 1);
      assertTrue(doss.get(0).getCodeDossierId() == 3);
      b = banqueDao.findById(2).orElse(null);
      doss = codeDossierManager.findByRootDossierBanqueManager(b, false);
      assertTrue(doss.size() == 0);
   }
}
