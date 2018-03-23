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

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.code.CodeDossierDao;
import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.code.CodeSelectManager;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager CodeUtilisateurManager.
 * Classe créée le 21/05/10.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class CodeSelectManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CodeSelectManager codeSelectManager;
   @Autowired
   private CodeDossierDao codeDossierDao;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private OperationManager operationManager;
   @Autowired
   private TableCodageDao tableCodageDao;
   @Autowired
   private OperationTypeDao operationTypeDao;

   public CodeSelectManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<CodeSelect> codes = codeSelectManager.findAllObjectsManager();
      assertTrue(codes.size() == 5);
   }

   @Test
   public void testFindByCodeDossierManager(){
      CodeDossier dos = codeDossierDao.findById(3);
      List<CodeSelect> codes = codeSelectManager.findByCodeDossierManager(dos);
      assertTrue(codes.size() == 2);
      dos = codeDossierDao.findById(2);
      codes = codeSelectManager.findByCodeDossierManager(dos);
      assertTrue(codes.size() == 0);
      //null recherche
      codes = codeSelectManager.findByCodeDossierManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindCodesFromSelectByDossierManager(){
      CodeDossier dos = codeDossierDao.findById(3);
      List<CodeCommon> codes = codeSelectManager.findCodesFromSelectByDossierManager(dos);
      assertTrue(codes.size() == 2);
      dos = codeDossierDao.findById(2);
      codes = codeSelectManager.findCodesFromSelectByDossierManager(dos);
      assertTrue(codes.size() == 0);
      //null recherche
      codes = codeSelectManager.findCodesFromSelectByDossierManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindByRootDossierManager(){
      final Banque b = banqueDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(1);
      final List<CodeCommon> codes = codeSelectManager.findByRootDossierManager(u, b);
      assertTrue(codes.size() == 1);
      assertTrue(codes.get(0).getCodeId() == 2);
   }

   @Test
   public void testFindByUtilisateurAndBanqueManager(){
      Utilisateur u = utilisateurDao.findById(1);
      Banque b = banqueDao.findById(1);
      List<CodeSelect> codes = codeSelectManager.findByUtilisateurAndBanqueManager(u, b);
      assertTrue(codes.size() == 3);
      u = utilisateurDao.findById(2);
      codes = codeSelectManager.findByUtilisateurAndBanqueManager(u, b);
      assertTrue(codes.size() == 0);
      b = banqueDao.findById(2);
      codes = codeSelectManager.findByUtilisateurAndBanqueManager(u, b);
      assertTrue(codes.size() == 1);
      //null recherche
      codes = codeSelectManager.findByUtilisateurAndBanqueManager(null, null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindCodesFromSelectByUtilisateurAndBanqueManager(){
      Utilisateur u = utilisateurDao.findById(1);
      Banque b = banqueDao.findById(1);
      List<CodeCommon> codes = codeSelectManager.findCodesFromSelectByUtilisateurAndBanqueManager(u, b);
      assertTrue(codes.size() == 3);
      u = utilisateurDao.findById(2);
      codes = codeSelectManager.findCodesFromSelectByUtilisateurAndBanqueManager(u, b);
      assertTrue(codes.size() == 0);
      b = banqueDao.findById(2);
      codes = codeSelectManager.findCodesFromSelectByUtilisateurAndBanqueManager(u, b);
      assertTrue(codes.size() == 1);
      //null recherche
      codes = codeSelectManager.findCodesFromSelectByUtilisateurAndBanqueManager(null, null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindDoublonManager(){
      final Banque b = banqueDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(1);
      //Cree le doublon
      final CodeSelect c1 = codeSelectManager.findByUtilisateurAndBanqueManager(u, b).get(0);

      final CodeSelect c2 = new CodeSelect();
      c2.setCodeId(c1.getCodeId());
      c2.setTableCodage(c1.getTableCodage());
      c2.setUtilisateur(c1.getUtilisateur());
      c2.setBanque(c1.getBanque());
      assertTrue(codeSelectManager.findDoublonManager(c2));

      c1.setCodeId(2);
      c1.setTableCodage(tableCodageDao.findById(2));
      assertTrue(codeSelectManager.findDoublonManager(c1));
   }

   @Test
   public void testCRUD(){
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   private void createObjectManagerTest(){

      final CodeSelect code = new CodeSelect();
      /*Champs obligatoires*/
      final Banque b = banqueDao.findById(3);
      final Utilisateur u = utilisateurDao.findById(3);
      final TableCodage cimo = tableCodageDao.findById(3);
      code.setCodeId(28);
      code.setTableCodage(cimo);
      final CodeDossier dos = codeDossierDao.findById(3);

      //required Banque
      try{
         codeSelectManager.createOrUpdateManager(code, dos, null, u, "creation");
      }catch(final RequiredObjectIsNullException re){
         assertTrue(true);
      }
      //required utilisateur
      try{
         codeSelectManager.createOrUpdateManager(code, dos, b, null, "creation");
      }catch(final RequiredObjectIsNullException re){
         assertTrue(true);
      }
      //operation invalide
      try{
         codeSelectManager.createOrUpdateManager(code, null, b, u, null);
      }catch(final NullPointerException ne){
         assertTrue(ne.getMessage().equals("operation cannot be " + "set to null for createorUpdateMethod"));
      }
      try{
         codeSelectManager.createOrUpdateManager(code, null, b, u, "");
      }catch(final IllegalArgumentException ie){
         assertTrue(ie.getMessage().equals("Operation must match " + "'creation/modification' values"));
      }
      testFindAllObjectsManager();
      codeSelectManager.createOrUpdateManager(code, dos, b, u, "creation");
      assertTrue(codeSelectManager.findByUtilisateurAndBanqueManager(u, b).size() == 1);

      assertTrue(codeSelectManager.findByCodeDossierManager(codeDossierDao.findById(3)).size() == 3);

      assertTrue(operationManager.findByObjectManager(code).size() == 1);
      assertTrue(operationManager.findByObjectManager(code).get(0).getOperationType().getNom().equals("Creation"));

      //Insertion d'un doublon engendrant une exception
      final CodeSelect c2 = new CodeSelect();
      c2.setCodeId(code.getCodeId());
      c2.setTableCodage(cimo);
      c2.setBanque(b);
      c2.setUtilisateur(u);
      assertTrue(code.equals(c2));
      Boolean catched = false;
      try{
         codeSelectManager.createOrUpdateManager(c2, null, null, null, "creation");
      }catch(final Exception ex){
         if(ex.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(codeSelectManager.findByUtilisateurAndBanqueManager(u, b).size() == 1);
      assertTrue(operationManager.findByObjectManager(code).size() == 1);
   }

   private void updateObjectManagerTest(){
      // move le code dans le dossier root
      final Banque b = banqueDao.findById(3);
      final Utilisateur u = utilisateurDao.findById(3);
      final CodeDossier dos = codeDossierDao.findById(3);
      final CodeSelect c = codeSelectManager.findByCodeDossierManager(dos).get(2);
      c.setCodeDossier(null);
      codeSelectManager.createOrUpdateManager(c, null, null, null, "modification");

      assertTrue(codeSelectManager.findByRootDossierManager(u, b).size() == 1);

      assertTrue(operationManager.findByObjectManager(c).size() == 2);
      assertTrue(
         operationManager.findByObjetIdEntiteAndOpeTypeManager(c, operationTypeDao.findByNom("Modification").get(0)).size() == 1);
   }

   private void removeObjectManagerTest(){
      final Banque b = banqueDao.findById(3);
      final Utilisateur u = utilisateurDao.findById(3);
      final CodeSelect c = codeSelectManager.findByUtilisateurAndBanqueManager(u, b).get(0);
      codeSelectManager.removeObjectManager(c);
      assertTrue(codeSelectManager.findByUtilisateurAndBanqueManager(u, b).size() == 0);
      assertTrue(operationManager.findByObjectManager(c).size() == 0);
      //verifie que l'etat des tables modifies est revenu identique
      assertTrue(operationManager.findAllObjectsManager().size() == 19);
      testFindAllObjectsManager();
      // null remove
      codeSelectManager.removeObjectManager(null);
   }

   @Test
   public void testFindByCodeOrLibelleLikeManager(){
      final Banque b = banqueDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(1);
      //teste une recherche exactMatch
      List<CodeCommon> codes = codeSelectManager.findByCodeOrLibelleLikeManager("A", true, u, b);
      assertTrue(codes.size() == 1);
      //teste une recherche non exactMatch
      codes = codeSelectManager.findByCodeOrLibelleLikeManager("A", false, u, b);
      assertTrue(codes.size() == 2);
      //teste une recherche non exactMatch
      codes = codeSelectManager.findByCodeOrLibelleLikeManager("ales", false, u, b);
      assertTrue(codes.size() == 1);
      //teste une recherche infructueuse
      codes = codeSelectManager.findByCodeOrLibelleLikeManager("LUX", true, u, b);
      assertTrue(codes.size() == 0);
      //null recherche
      codes = codeSelectManager.findByCodeOrLibelleLikeManager(null, false, u, b);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindByRootDossierAndBanqueManager(){
      Banque b = banqueDao.findById(1);
      List<CodeCommon> codes = codeSelectManager.findByRootDossierAndBanqueManager(b);
      assertTrue(codes.size() == 1);
      b = banqueDao.findById(2);
      codes = codeSelectManager.findByRootDossierAndBanqueManager(b);
      assertTrue(codes.size() == 1);
      codes = codeSelectManager.findByRootDossierAndBanqueManager(null);
      assertTrue(codes.size() == 0);
   }
}
