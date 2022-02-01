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
package fr.aphp.tumorotek.test.manager.contexte;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.CatalogueDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonTypeDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ContexteDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilUtilisateurDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.code.CodeDossierManager;
import fr.aphp.tumorotek.manager.code.CodeSelectManager;
import fr.aphp.tumorotek.manager.code.CodeUtilisateurManager;
import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.cession.CessionManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ExistingAnnotationValuesException;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.export.AffichageManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteTypeManager;
import fr.aphp.tumorotek.manager.systeme.CouleurEntiteTypeManager;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.BanqueValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Contexte;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Classe de test pour le manager BanqueManager.
 * Classe créée le 01/10/09.
 *
 * @author Pierre Ventadour.
 * @version 2.1
 *
 */
public class BanqueManagerTest extends AbstractManagerTest4
{

   @Autowired
   private BanqueManager banqueManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private PlateformeDao plateformeDao;
   @Autowired
   private ContexteDao contexteDao;
   @Autowired
   private TableCodageDao tableCodageDao;
   @Autowired
   private BanqueValidator banqueValidator;
   @Autowired
   private ServiceDao serviceDao;
   @Autowired
   private CollaborateurDao collaborateurDao;
   @Autowired
   private CouleurDao couleurDao;
   @Autowired
   private ConteneurDao conteneurDao;
   @Autowired
   private TableAnnotationDao tableAnnotationDao;
   @Autowired
   private EnceinteManager enceinteManager;
   @Autowired
   private EnceinteTypeManager enceinteTypeManager;
   @Autowired
   private ConteneurManager conteneurManager;
   @Autowired
   private TableAnnotationManager tableAnnotationManager;
   @Autowired
   private EchantillonTypeDao echantillonTypeDao;
   @Autowired
   private CouleurEntiteTypeManager couleurEntiteTypeManager;
   @Autowired
   private CodeSelectManager codeSelectManager;
   @Autowired
   private CodeUtilisateurManager codeUtilisateurManager;
   @Autowired
   private PrelevementManager prelevementManager;
   @Autowired
   private EchantillonManager echantillonManager;
   @Autowired
   private ProdDeriveManager prodDeriveManager;
   @Autowired
   private CessionManager cessionManager;
   @Autowired
   private CodeDossierManager codeDossierManager;
   @Autowired
   private AffichageManager affichageManager;
   @Autowired
   private CatalogueDao catalogueDao;
   @Autowired
   private ObjetStatutDao objetStatutDao;
   @Autowired
   private ProdTypeDao prodTypeDao;
   @Autowired
   private ProfilDao profilDao;
   @Autowired
   private ProfilUtilisateurDao profilUtilisateurDao;
   @Autowired
   private UtilisateurManager utilisateurManager;
   
   public BanqueManagerTest(){}

   @Test
   public void testFindById(){
      final Banque banque = banqueManager.findByIdManager(1);
      assertNotNull(banque);
      assertTrue(banque.getNom().equals("BANQUE1"));

      final Banque banqueNull = banqueManager.findByIdManager(5);
      assertNull(banqueNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Banque> list = banqueManager.findAllObjectsManager();
      assertTrue(list.size() == 4);
   }

   /**
    * Test la méthode getPrelevementsManager.
    */
   @Test
   public void testGetPrelevementsManager(){
      final Banque banque1 = banqueManager.findByIdManager(1);
      assertNotNull(banque1);
      Set<Prelevement> list = banqueManager.getPrelevementsManager(banque1);
      assertTrue(list.size() == 3);

      final Banque banque2 = banqueManager.findByIdManager(2);
      assertNotNull(banque2);
      list = banqueManager.getPrelevementsManager(banque2);
      assertTrue(list.size() == 1);

      assertTrue(banqueManager.getPrelevementsManager(null).size() == 0);
   }

   /**
    * Test la méthode getEchantillonsManager.
    */
   @Test
   public void testGetEchantillonsManager(){
      final Banque banque1 = banqueManager.findByIdManager(1);
      assertNotNull(banque1);
      Set<Echantillon> list = banqueManager.getEchantillonsManager(banque1);
      assertTrue(list.size() == 3);

      final Banque banque2 = banqueManager.findByIdManager(2);
      assertNotNull(banque2);
      list = banqueManager.getEchantillonsManager(banque2);
      assertTrue(list.size() == 1);

      assertTrue(banqueManager.getEchantillonsManager(null).size() == 0);
   }

   /**
    * Test la méthode getProdDerivesManager.
    */
   @Test
   public void testGetProdDerivesManager(){
      final Banque banque1 = banqueManager.findByIdManager(1);
      assertNotNull(banque1);
      Set<ProdDerive> list = banqueManager.getProdDerivesManager(banque1);
      assertTrue(list.size() == 3);

      final Banque banque2 = banqueManager.findByIdManager(2);
      assertNotNull(banque2);
      list = banqueManager.getProdDerivesManager(banque2);
      assertTrue(list.size() == 1);

      assertTrue(banqueManager.getProdDerivesManager(null).size() == 0);
   }

   /**
    * Test la méthode getServicesStockageManager.
    */
   @Test
   public void testGetServicesStockageManager(){
      final Banque banque1 = banqueManager.findByIdManager(1);
      assertNotNull(banque1);
      Set<Service> list = banqueManager.getServicesStockageManager(banque1);
      assertTrue(list.size() == 1);

      final Banque banque2 = banqueManager.findByIdManager(2);
      assertNotNull(banque2);
      list = banqueManager.getServicesStockageManager(banque2);
      assertTrue(list.size() == 2);

      assertTrue(banqueManager.getServicesStockageManager(null).size() == 0);
   }

   /**
    * @version 2.1
    */
   @Test
   public void testFindByPlateformeAndArchiveManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<Banque> banques = banqueManager.findByPlateformeAndArchiveManager(pf, false);
      assertTrue(banques.size() == 3);
      assertTrue(banques.get(0).getBanqueId() == 1);
      assertTrue(banques.get(1).getBanqueId() == 2);
      assertTrue(banques.get(2).getBanqueId() == 3);
      banques = banqueManager.findByPlateformeAndArchiveManager(pf, true);
      assertTrue(banques.size() == 0);
      banques = banqueManager.findByPlateformeAndArchiveManager(pf, null);
      assertTrue(banques.size() == 3);
      pf = plateformeDao.findById(2);
      banques = banqueManager.findByPlateformeAndArchiveManager(pf, false);
      assertTrue(banques.size() == 0);
      banques = banqueManager.findByPlateformeAndArchiveManager(pf, true);
      assertTrue(banques.size() == 1);
      assertTrue(banques.get(0).getBanqueId() == 4);
      banques = banqueManager.findByPlateformeAndArchiveManager(pf, null);
      assertTrue(banques.size() == 1);
      assertTrue(banques.get(0).getBanqueId() == 4);
      banques = banqueManager.findByPlateformeAndArchiveManager(null, null);
      assertTrue(banques.size() == 0);
      pf = plateformeDao.findById(3);
      banques = banqueManager.findByPlateformeAndArchiveManager(pf, false);
      assertTrue(banques.size() == 0);
   }

   @Test
   public void testFindContexteCataloguesManager(){
      List<Catalogue> cats = banqueManager.findContexteCataloguesManager(1);
      assertTrue(cats.size() == 4);
      assertTrue(cats.get(0).getNom().equals("INCa"));
      cats = banqueManager.findContexteCataloguesManager(2);
      assertTrue(cats.size() == 4);
      assertTrue(cats.get(0).getNom().equals("INCa"));
      cats = banqueManager.findContexteCataloguesManager(4);
      assertTrue(cats.size() == 1);
      assertTrue(cats.get(0).getNom().equals("INCa"));
   }

   @Test
   public void testFindByEntiteConsultByUtilisateurManager(){
      Utilisateur utilisateur = utilisateurDao.findById(3);
      final Plateforme pf = plateformeDao.findById(1);
      final Entite prel = entiteDao.findById(2);
      List<Banque> banks = banqueManager.findByEntiteConsultByUtilisateurManager(utilisateur, prel, pf);
      assertTrue(banks.size() == 2);
      assertTrue(banks.contains(banqueManager.findByIdManager(1)));
      assertTrue(banks.contains(banqueManager.findByIdManager(2)));

      banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateurDao.findById(2), prel, pf);
      assertTrue(banks.size() == 2);

      // superadmin since 2.0.13
      utilisateur = utilisateurDao.findById(5);
      banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateur, prel, pf);
      assertTrue(banks.size() == 3);

      utilisateur = utilisateurDao.findById(1);
      banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateur, prel, pf);
      assertTrue(banks.size() == 2);

      final Entite der = entiteDao.findById(8);
      banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateur, der, pf);
      assertTrue(banks.size() == 2);

      utilisateur = utilisateurDao.findById(2);
      banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateur, der, pf);
      assertTrue(banks.size() == 0);

      banks = banqueManager.findByEntiteConsultByUtilisateurManager(utilisateurDao.findById(4), prel, pf);
      assertTrue(banks.size() == 0);
   }

   @Test
   public void testFindByEntiteModifByUtilisateurManager(){
      Utilisateur utilisateur = utilisateurDao.findById(3);
      Plateforme pf = plateformeDao.findById(1);
      final Entite prel = entiteDao.findById(2);
      List<Banque> banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateur, prel, pf);
      assertTrue(banks.size() == 1);
      assertTrue(banks.contains(banqueManager.findByIdManager(1)));

      banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateurDao.findById(2), prel, pf);
      assertTrue(banks.size() == 2);

      utilisateur = utilisateurDao.findById(1);
      banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateur, prel, pf);
      assertTrue(banks.size() == 2);

      final Entite der = entiteDao.findById(8);
      banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateur, der, pf);
      assertTrue(banks.size() == 2);

      utilisateur = utilisateurDao.findById(2);
      banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateur, der, pf);
      assertTrue(banks.size() == 0);

      pf = plateformeDao.findById(2);
      banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateurDao.findById(2), prel, pf);
      assertTrue(banks.size() == 0);

      plateformeDao.findById(1);
      banks = banqueManager.findByEntiteModifByUtilisateurManager(utilisateurDao.findById(4), prel, pf);
      assertTrue(banks.size() == 0);
   }

   @Test
   public void testFindByAutoriseCrossPatientManager(){
      List<Banque> banks = banqueManager.findByAutoriseCrossPatientManager(true);
      assertTrue(banks.size() == 2);
      assertTrue(banks.contains(banqueManager.findByIdManager(1)));
      assertTrue(banks.contains(banqueManager.findByIdManager(4)));

      banks = banqueManager.findByAutoriseCrossPatientManager(false);
      assertTrue(banks.size() == 2);
   }

   @Test
   public void testFindByUtilisateurIsAdminManager(){
      Utilisateur utilisateur = utilisateurDao.findById(1);
      Plateforme pf = plateformeDao.findById(1);
      List<Banque> banks = banqueManager.findByUtilisateurIsAdminManager(utilisateur, pf);
      assertTrue(banks.size() == 2);
      assertTrue(banks.contains(banqueManager.findByIdManager(1)));
      assertTrue(banks.contains(banqueManager.findByIdManager(2)));
      banks = banqueManager.findByUtilisateurIsAdminManager(utilisateurDao.findById(3), pf);
      assertTrue(banks.size() == 1);

      // super user
      utilisateur = utilisateurDao.findById(5);
      banks = banqueManager.findByUtilisateurIsAdminManager(utilisateur, pf);
      assertTrue(banks.size() == 3);

      pf = plateformeDao.findById(2);
      banks = banqueManager.findByUtilisateurIsAdminManager(utilisateur, pf);
      assertTrue(banks.size() == 0); // since 2.1 archive = true!

   }

   @Test
   public void testFindByUtilisateurAndPFManager(){
      Utilisateur utilisateur = utilisateurDao.findById(1);
      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      List<Banque> banks = banqueManager.findByUtilisateurAndPFManager(utilisateur, pf1);
      assertTrue(banks.size() == 2);
      assertTrue(banks.contains(banqueManager.findByIdManager(1)));
      assertTrue(banks.contains(banqueManager.findByIdManager(2)));
      banks = banqueManager.findByUtilisateurAndPFManager(utilisateur, pf2);
      assertTrue(banks.size() == 0);

      utilisateur = utilisateurDao.findById(2);
      banks = banqueManager.findByUtilisateurAndPFManager(utilisateur, pf1);
      assertTrue(banks.size() == 2);
      banks = banqueManager.findByUtilisateurAndPFManager(utilisateur, pf2);
      assertTrue(banks.size() == 0);

      utilisateur = utilisateurDao.findById(5);
      banks = banqueManager.findByUtilisateurAndPFManager(utilisateur, pf1);
      assertTrue(banks.size() == 3);
      banks = banqueManager.findByUtilisateurAndPFManager(utilisateur, pf2);
      assertTrue(banks.size() == 0);

      utilisateur = utilisateurDao.findById(2);
      banks = banqueManager.findByUtilisateurAndPFManager(null, pf1);
      assertTrue(banks.size() == 0);
      banks = banqueManager.findByUtilisateurAndPFManager(utilisateur, null);
      assertTrue(banks.size() == 0);
   }

   @Test
   public void testFindDoublonManager(){
      final Plateforme pf = plateformeDao.findById(1);
      final Banque banque = new Banque();
      banque.setNom("BANQUE1");
      banque.setPlateforme(pf);
      assertTrue(banqueManager.findDoublonManager(banque));

      banque.setNom("BANQUE1b");
      assertFalse(banqueManager.findDoublonManager(banque));

      final Banque banque2 = banqueManager.findByIdManager(2);
      assertFalse(banqueManager.findDoublonManager(banque2));

      banque2.setNom("BANQUE1");
      assertTrue(banqueManager.findDoublonManager(banque2));
   }

   @Test
   public void testGetConteneursManager(){
      final Banque b2 = banqueManager.findByIdManager(2);
      Set<Conteneur> conts = banqueManager.getConteneursManager(b2);
      assertTrue(conts.size() == 4);
      assertTrue(conts.contains(conteneurDao.findById(1)));
      assertTrue(conts.contains(conteneurDao.findById(2)));
      assertTrue(conts.contains(conteneurDao.findById(3)));
      assertTrue(conts.contains(conteneurDao.findById(4)));
      final Banque b3 = banqueManager.findByIdManager(3);
      conts = banqueManager.getConteneursManager(b3);
      assertTrue(conts.size() == 1);
      assertTrue(conts.contains(conteneurDao.findById(1)));
      conts = banqueManager.getConteneursManager(null);
      assertTrue(conts.size() == 0);
   }

   @Test
   public void testGetImprimantesManager(){
      /*Banque b1 = banqueManager.findByIdManager(1);
      Set<Imprimante> liste = banqueManager.getImprimantesManager(b1);
      assertTrue(liste.size() == 2);
      Banque b3 = banqueManager.findByIdManager(3);
      liste = banqueManager.getImprimantesManager(b3);
      assertTrue(liste.size() == 0);
      liste = banqueManager.getImprimantesManager(null);
      assertTrue(liste.size() == 0);*/
   }

   @Test
   public void testValidation(){
      final Banque b = new Banque();
      // nom
      List<Errors> errs = new ArrayList<>();
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.nom.empty"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      b.setNom("");
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.nom.empty"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      b.setNom("$$###'¤¤'");
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.nom.illegal"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      b.setNom(createOverLength(100));
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.nom.tooLong"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      // identification
      b.setNom("ok");
      b.setIdentification("   ");
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.identification.empty"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      b.setIdentification("%$$*gd¤¤");
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.identification.illegal"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      b.setIdentification(createOverLength(50));
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.identification.tooLong"));
      }
      // description
      b.setIdentification(null);
      b.setDescription("   ");
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.description.empty"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      b.setDescription(createOverLength(250));
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.description.tooLong"));
      }
      assertTrue(errs.isEmpty());
      // maladie deafut
      b.setDescription(null);
      BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      b.setDefMaladies(null);
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.defMaladies.empty"));
      }
      // libelle defaut
      b.setDefMaladies(true);
      b.setDefautMaladie("  ");
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.defautMaladie.empty"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      b.setDefautMaladie("$$1&¤¤&&");
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.defautMaladie.illegal"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      b.setDefautMaladie(createOverLength(250));
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.defautMaladie.tooLong"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      b.setDefautMaladie("ok");
      b.setDefMaladies(false);
      try{
         BeanValidator.validateObject(b, new Validator[] {banqueValidator});
      }catch(final ValidationException ve){
         errs = ve.getErrors();
         assertTrue(errs.get(0).getFieldError().getCode().equals("banque.defautMaladie.noDefMaladie"));
      }
      assertFalse(errs.isEmpty());
      errs.clear();
      b.setDefautMaladie(null);
      BeanValidator.validateObject(b, new Validator[] {banqueValidator});
   }

   @Test
   public void testCRUD() throws ParseException{
      Banque b = saveManagerTest();
      b = saveManagerTest(b);
      removeObjectManagerTest(b);
   }

   private Banque saveManagerTest(){
      final Banque b = new Banque();
      final Utilisateur u = utilisateurDao.findById(1);

      final Plateforme pf = plateformeDao.findById(1);
      final Contexte ct = contexteDao.findById(1);
      final Set<Catalogue> cats = new HashSet<>();
      cats.add(catalogueDao.findById(1));
      cats.add(catalogueDao.findById(2));

      //required plateforme
      try{
         banqueManager.createOrsaveManager(b, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, u, null, "creation", null);
      }catch(final RequiredObjectIsNullException re){
         assertTrue(re.getMessage().equals("Banque: Plateforme est " + "null lors de l'opération de creation"));
      }

      //required contexte
      try{
         banqueManager.createOrsaveManager(b, pf, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, u, null, "creation", null);
      }catch(final RequiredObjectIsNullException re){
         assertTrue(re.getMessage().equals("Banque: Contexte est " + "null lors de l'opération de creation"));
      }

      //validation
      try{
         banqueManager.createOrsaveManager(b, pf, ct, null, null, null, null, null, null, null, null, null, null, null,
            null, null, u, null, "creation", null);
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("banque.nom.empty"));
      }
      b.setNom("BANQUE1");
      assertTrue(b.equals(banqueManager.findByIdManager(1)));
      Boolean catched = false;
      //Insertion d'un doublon engendrant une exception
      try{
         banqueManager.createOrsaveManager(b, pf, ct, null, null, null, null, null, null, null, null, null, null, null,
            null, null, u, null, "creation", null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      b.setNom("Credit Agricole");
      b.setIdentification("CA");
      b.setDescription("La banque des paysans");
      final Service s = serviceDao.findById(1);
      final Collaborateur c = collaborateurDao.findById(2);
      final Collaborateur co = collaborateurDao.findById(3);
      final Couleur c1 = couleurDao.findById(3);
      final Couleur c2 = couleurDao.findById(4);
      b.setCatalogues(cats);

      //operation invalide
      try{
         banqueManager.createOrsaveManager(b, pf, ct, null, null, null, null, null, null, null, null, null, null, null,
            null, null, u, null, null, null);
      }catch(final NullPointerException ne){
         assertTrue(ne.getMessage().equals("operation cannot be " + "set to null for createorUpdateMethod"));
      }
      try{
         banqueManager.createOrsaveManager(b, pf, ct, null, null, null, null, null, null, null, null, null, null, null,
            null, null, u, null, "jiggs", null);
      }catch(final IllegalArgumentException ie){
         assertTrue(ie.getMessage().equals("Operation must match " + "'creation/modification' values"));
      }
      assertTrue(banqueManager.findByPlateformeAndArchiveManager(plateformeDao.findById(1), null).size() == 3);

      //insertion
      banqueManager.createOrsaveManager(b, pf, ct, s, c, co, null, null, null, null, null, null, null, null, c1, c2, u,
         null, "creation", "/tmp/");
      assertTrue(banqueManager.findAllObjectsManager().size() == 5);
      assertTrue(banqueManager.findByIdManager(b.getBanqueId()).getPlateforme().equals(pf));
      assertTrue(banqueManager.findByIdManager(b.getBanqueId()).getContexte().equals(ct));
      assertTrue(banqueManager.findByIdManager(b.getBanqueId()).getNom().equals("Credit Agricole"));
      assertTrue(banqueManager.findByIdManager(b.getBanqueId()).getIdentification().equals("CA"));
      assertTrue(banqueManager.findByIdManager(b.getBanqueId()).getDescription().equals("La banque des paysans"));
      assertNull(banqueManager.findByIdManager(b.getBanqueId()).getAutoriseCrossPatient());
      assertTrue(banqueManager.findByIdManager(b.getBanqueId()).getDefMaladies());
      assertTrue(banqueManager.findByIdManager(b.getBanqueId()).getProprietaire().equals(s));
      assertTrue(banqueManager.findByIdManager(b.getBanqueId()).getCollaborateur().equals(c));
      assertTrue(banqueManager.findByIdManager(b.getBanqueId()).getContact().equals(co));
      assertTrue(banqueManager.findByIdManager(b.getBanqueId()).getEchantillonCouleur().equals(c1));
      assertTrue(banqueManager.findByIdManager(b.getBanqueId()).getProdDeriveCouleur().equals(c2));
      assertTrue(getOperationManager().findByObjectManager(b).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(b).get(0).getOperationType().getNom().equals("Creation"));
      assertTrue(catalogueDao.findByAssignedBanque(b).size() == 2);
      return b;
   }

   private Banque saveManagerTest(final Banque bank){
      // recupere la derniere banque inseree
      final Banque b2 = banqueManager.findByIdManager(bank.getBanqueId());
      final Utilisateur u = utilisateurDao.findById(2);

      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         b2.setNom("BANQUE2");
         banqueManager.createOrsaveManager(b2, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, u, null, "modification", null);
      }catch(final DoublonFoundException e){
         catched = true;
      }
      assertTrue(catched);
      b2.setNom("BNP Parisbas");
      b2.setAutoriseCrossPatient(true);
      b2.setDefautMaladie("*ù$*ù$$##¤#");
      catched = false;
      try{
         banqueManager.createOrsaveManager(b2, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, u, null, "modification", null);
      }catch(final ValidationException e){
         catched = true;
      }
      assertTrue(catched);
      b2.setDefautMaladie("Voleurs");
      b2.setDefautMaladieCode("$$$$");
      catched = false;
      try{
         banqueManager.createOrsaveManager(b2, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, u, null, "modification", null);
      }catch(final ValidationException e){
         catched = true;
      }
      assertTrue(catched);

      b2.setDefautMaladieCode("05045");
      b2.getCatalogues().remove(catalogueDao.findById(1));

      banqueManager.createOrsaveManager(b2, null, null, null, null, null, null, null, null, null, null, null, null, null,
         null, null, u, null, "modification", null);

      assertTrue(banqueManager.findAllObjectsManager().size() == 5);
      assertTrue(banqueManager.findByIdManager(b2.getBanqueId()).getNom().equals("BNP Parisbas"));
      assertNotNull(banqueManager.findByIdManager(b2.getBanqueId()).getContexte());
      assertTrue(banqueManager.findByIdManager(b2.getBanqueId()).getIdentification().equals("CA"));
      assertTrue(banqueManager.findByIdManager(b2.getBanqueId()).getDescription().equals("La banque des paysans"));
      assertTrue(banqueManager.findByIdManager(b2.getBanqueId()).getAutoriseCrossPatient());
      assertTrue(banqueManager.findByIdManager(b2.getBanqueId()).getDefMaladies());
      assertTrue(banqueManager.findByIdManager(b2.getBanqueId()).getDefautMaladie().equals("Voleurs"));
      assertTrue(banqueManager.findByIdManager(b2.getBanqueId()).getDefautMaladieCode().equals("05045"));
      assertNull(banqueManager.findByIdManager(b2.getBanqueId()).getProprietaire());
      assertNull(banqueManager.findByIdManager(b2.getBanqueId()).getCollaborateur());
      assertNull(banqueManager.findByIdManager(b2.getBanqueId()).getEchantillonCouleur());
      assertNull(banqueManager.findByIdManager(b2.getBanqueId()).getProdDeriveCouleur());
      assertTrue(getOperationManager().findByObjectManager(b2).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(b2).get(1).getOperationType().getNom().equals("Modification"));
      assertTrue(catalogueDao.findByAssignedBanque(b2).size() == 1);
      return b2;
   }

   private void removeObjectManagerTest(final Banque bank){
      final Utilisateur u = utilisateurDao.findById(1);
      final Banque b = banqueManager.findByIdManager(bank.getBanqueId());
      banqueManager.removeObjectManager(b, null, u, "/tmp/", false);
      assertTrue(banqueManager.findAllObjectsManager().size() == 4);
      assertTrue(getOperationManager().findByObjectManager(b).size() == 0);

      banqueManager.removeObjectManager(null, null, null, null, true);
      //verifie que l'etat des tables modifies est revenu identique
      testFindAll();

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(b);
      cleanUpFantomes(fs);
   }

   @Test
   public void testCreateUpdateConteneurs(){
      Banque b = new Banque();
      final Utilisateur u = utilisateurDao.findById(2);
      b.setNom("Banque a conteneurs");
      final Plateforme pf = plateformeDao.findById(1);
      final Contexte ct = contexteDao.findById(1);

      final List<Conteneur> conts = new ArrayList<>();
      final Conteneur c1 = conteneurDao.findById(1);
      final Conteneur c2 = conteneurDao.findById(2);
      final Conteneur c3 = conteneurDao.findById(3);
      conts.add(c1);
      conts.add(c2);
      conts.add(c3);

      final List<BanqueTableCodage> tabs = new ArrayList<>();
      final List<TableAnnotation> tabsPat = new ArrayList<>();
      final List<TableAnnotation> tabsPrel = new ArrayList<>();
      final List<TableAnnotation> tabsEchan = new ArrayList<>();
      final List<TableAnnotation> tabsCess = new ArrayList<>();
      final List<CouleurEntiteType> couls = new ArrayList<>();

      banqueManager.createOrsaveManager(b, pf, ct, null, null, null, conts, tabs, tabsPat, tabsPrel, tabsEchan, null,
         tabsCess, couls, null, null, u, null, "creation", "/tmp/");
      b = banqueManager.findByIdManager(b.getBanqueId());
      assertTrue(banqueManager.findAllObjectsManager().size() == 5);
      assertTrue(banqueManager.getConteneursManager(b).size() == 3);
      assertTrue(banqueManager.getConteneursManager(b).contains(c1));
      assertTrue(banqueManager.getConteneursManager(b).contains(c2));
      assertTrue(banqueManager.getConteneursManager(b).contains(c3));

      // ajout sur deux enceintes (une du conteneur c1, et une nouvelle
      // insére dans c3) d'une reservation 
      // pour la banque qui sera par la suite otée de l'association.		
      final Enceinte e6 = enceinteManager.findByIdManager(6);
      final Set<Banque> banske6 = enceinteManager.getBanquesManager(e6);
      banske6.add(b);
      enceinteManager.saveManager(e6, e6.getEnceinteType(), e6.getConteneur(), e6.getEnceintePere(), e6.getEntite(),
         new ArrayList<>(banske6), null, null, u, null);
      final Enceinte ec3 = new Enceinte();
      ec3.setNom("R3");
      ec3.setPosition(1);
      ec3.setAlias("ALIAS");
      ec3.setNbPlaces(6);
      final List<Banque> banskec3 = new ArrayList<>();
      banskec3.add(b);
      enceinteManager.saveManager(ec3, enceinteTypeManager.findByIdManager(1), c3, null, null, banskec3, null, u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(enceinteManager.getBanquesManager(e6).size() == 1);
      assertTrue(conteneurManager.getEnceintesManager(c3).size() == 1);
      assertTrue(conteneurManager.getEnceintesManager(c3).contains(ec3));
      assertTrue(enceinteManager.getBanquesManager(ec3).size() == 1);

      // ajout d'un et remove des deux c1 et c2
      conts.remove(c1);
      conts.remove(c2);
      final Conteneur c4 = conteneurDao.findById(4);
      conts.add(c4);

      banqueManager.createOrsaveManager(b, null, null, null, null, null, conts, tabs, tabsPat, tabsPrel, tabsEchan, null,
         null, couls, null, null, u, null, "modification", null);
      assertTrue(banqueManager.findAllObjectsManager().size() == 5);
      // recupere la derniere banque
      final Banque b2 = banqueManager.findByIdManager(b.getBanqueId());

      assertTrue(banqueManager.getConteneursManager(b2).size() == 2);
      assertTrue(banqueManager.getConteneursManager(b2).contains(c3));
      assertTrue(banqueManager.getConteneursManager(b2).contains(c4));
      // suppression des reservations sur les enceintes de c1
      assertTrue(enceinteManager.getBanquesManager(e6).size() == 0);
      assertTrue(enceinteManager.getBanquesManager(ec3).size() == 1);

      // suppression et verification de la cascade
      banqueManager.removeObjectManager(b, null, u, "/tmp/", true);
      testFindAll();
      final Enceinte e = enceinteManager.findByIdManager(ec3.getEnceinteId());
      assertTrue(enceinteManager.getBanquesManager(e).size() == 0);
      final List<Operation> ops = getOperationManager().findByObjectManager(e6);
      for(int i = 0; i < ops.size(); i++){
         getOperationManager().removeObjectManager(ops.get(i));
      }

      // clean up
      enceinteManager.removeObjectManager(e, null, u);
      assertTrue(conteneurManager.getEnceintesManager(c3).size() == 0);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(e);
      fs.add(b);
      cleanUpFantomes(fs);
   }

   @Test
   public void testCreateUpdateTablesCodage(){
      Banque b = new Banque();
      final Utilisateur u = utilisateurDao.findById(2);
      b.setNom("Banque a tables codage");
      final Plateforme pf = plateformeDao.findById(1);
      final Contexte ct = contexteDao.findById(1);

      final List<BanqueTableCodage> tabs = new ArrayList<>();
      final TableCodage t1 = tableCodageDao.findById(1);
      final BanqueTableCodage btc1 = new BanqueTableCodage();
      btc1.setTableCodage(t1);
      btc1.setLibelleExport(true);
      final TableCodage t2 = tableCodageDao.findById(2);
      final BanqueTableCodage btc2 = new BanqueTableCodage();
      btc2.setTableCodage(t2);
      btc2.setLibelleExport(true);
      final TableCodage t3 = tableCodageDao.findById(4);
      final BanqueTableCodage btc3 = new BanqueTableCodage();
      btc3.setTableCodage(t3);
      btc3.setLibelleExport(false);

      tabs.add(btc1);
      tabs.add(btc2);
      tabs.add(btc3);

      banqueManager.createOrsaveManager(b, pf, ct, null, null, null, null, tabs, null, null, null, null, null, null, null,
         null, u, null, "creation", "/tmp/");
      assertTrue(banqueManager.findAllObjectsManager().size() == 5);
      b = banqueManager.findByIdManager(b.getBanqueId());
      assertTrue(banqueManager.getBanqueTableCodageByBanqueManager(b).size() == 3);
      assertTrue(banqueManager.getBanqueTableCodageByBanqueManager(b).contains(btc1));
      assertTrue(banqueManager.getBanqueTableCodageByBanqueManager(b).contains(btc2));
      assertTrue(banqueManager.getBanqueTableCodageByBanqueManager(b).contains(btc3));
      //assertTrue(tableCodageManager.getBanquesManager(t3).size()  == 2);

      // ajout d'une table et remove des deux t1 et t2
      tabs.remove(btc1);
      tabs.remove(btc2);
      final TableCodage t4 = tableCodageDao.findById(5);
      final BanqueTableCodage btc4 = new BanqueTableCodage();
      btc4.setTableCodage(t4);
      btc4.setLibelleExport(false);
      tabs.add(btc4);

      banqueManager.createOrsaveManager(b, null, null, null, null, null, null, tabs, null, null, null, null, null, null,
         null, null, u, null, "modification", null);
      assertTrue(banqueManager.findAllObjectsManager().size() == 5);
      // recupere la derniere banque
      final Banque b2 = banqueManager.findByIdManager(b.getBanqueId());

      assertTrue(banqueManager.getBanqueTableCodageByBanqueManager(b2).size() == 2);
      assertTrue(banqueManager.getBanqueTableCodageByBanqueManager(b2).contains(btc3));
      assertTrue(banqueManager.getBanqueTableCodageByBanqueManager(b2).contains(btc4));
      //assertTrue(tableCodageManager.getBanquesManager(t3).size()  == 2);

      // suppression et verification de la cascade
      banqueManager.removeObjectManager(b, null, u, "/tmp/", true);
      testFindAll();
      //assertTrue(tableCodageManager.getBanquesManager(t3).size()  == 1);
      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(b);
      cleanUpFantomes(fs);
   }

   @Test
   public void testCreateUpdateTablesAnnotation(){
      Banque b = new Banque();
      final Utilisateur u = utilisateurDao.findById(2);
      b.setNom("Banque a tables annots");
      final Plateforme pf = plateformeDao.findById(1);
      final Contexte ct = contexteDao.findById(1);

      final List<TableAnnotation> tabsPat = new ArrayList<>();
      final TableAnnotation t1 = tableAnnotationDao.findById(1);
      tabsPat.add(t1);
      final List<TableAnnotation> tabsPrel = new ArrayList<>();
      final TableAnnotation t2 = tableAnnotationDao.findById(2);
      tabsPrel.add(t2);
      final List<TableAnnotation> tabsEchan = new ArrayList<>();
      final TableAnnotation t3 = tableAnnotationDao.findById(3);
      final TableAnnotation t4 = tableAnnotationDao.findById(4);
      final TableAnnotation t5 = tableAnnotationDao.findById(5);
      tabsEchan.add(t5);
      tabsEchan.add(t3);
      final List<TableAnnotation> tabsDerive = new ArrayList<>();
      final TableAnnotation t6 = tableAnnotationDao.findById(6);
      tabsDerive.add(t6);

      banqueManager.createOrsaveManager(b, pf, ct, null, null, null, null, null, tabsPat, tabsPrel, tabsEchan, tabsDerive,
         null, null, null, null, u, null, "creation", "/tmp/");

      assertTrue(banqueManager.findAllObjectsManager().size() == 5);
      b = banqueManager.findByIdManager(b.getBanqueId());
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Patient").get(0), b).size() == 1);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Patient").get(0), b).contains(t1));
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Prelevement").get(0), b).size() == 1);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Prelevement").get(0), b).contains(t2));
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Echantillon").get(0), b).size() == 2);
      assertTrue(
         tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Echantillon").get(0), b).get(0).equals(t5));
      assertTrue(
         tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Echantillon").get(0), b).get(1).equals(t3));
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("ProdDerive").get(0), b).size() == 1);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("ProdDerive").get(0), b).contains(t6));

      // ajout et remove
      tabsPat.remove(t1);
      tabsEchan.remove(t5);
      tabsEchan.add(t4);

      banqueManager.createOrsaveManager(b, pf, null, null, null, null, null, null, tabsPat, tabsPrel, tabsEchan,
         tabsDerive, null, null, null, null, u, null, "modification", null);

      assertTrue(banqueManager.findAllObjectsManager().size() == 5);
      // recupere la derniere banque
      final Banque b2 = banqueManager.findByIdManager(b.getBanqueId());

      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Patient").get(0), b2).size() == 0);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Prelevement").get(0), b2).size() == 1);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Prelevement").get(0), b2).contains(t2));
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Echantillon").get(0), b).size() == 2);
      assertTrue(
         tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Echantillon").get(0), b2).get(0).equals(t3));
      assertTrue(
         tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Echantillon").get(0), b).get(1).equals(t4));
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("ProdDerive").get(0), b).size() == 1);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("ProdDerive").get(0), b).contains(t6));

      // suppression et verification de la cascade
      banqueManager.removeObjectManager(b, null, u, "/tmp/", true);
      testFindAll();
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Patient").get(0), b2).size() == 0);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Prelevement").get(0), b2).size() == 0);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Echantillon").get(0), b).size() == 0);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("ProdDerive").get(0), b).size() == 0);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(b);
      cleanUpFantomes(fs);
   }

   @Test
   public void testCreateUpdateCouleurEntiteTypes(){
      Banque b = new Banque();
      final Utilisateur u = utilisateurDao.findById(2);
      b.setNom("Banque a coulTypes");
      final Plateforme pf = plateformeDao.findById(1);
      final Contexte ct = contexteDao.findById(1);

      List<CouleurEntiteType> coulTypes = new ArrayList<>();
      final EchantillonType e1 = echantillonTypeDao.findById(1);
      final EchantillonType e2 = echantillonTypeDao.findById(2);
      final EchantillonType e3 = echantillonTypeDao.findById(3);
      final ProdType p1 = prodTypeDao.findById(1);
      final Couleur coul5 = couleurDao.findById(5);
      final CouleurEntiteType cE1 = new CouleurEntiteType();
      cE1.setCouleur(couleurDao.findById(1));
      cE1.setEchantillonType(e1);
      final CouleurEntiteType cE2 = new CouleurEntiteType();
      cE2.setCouleur(couleurDao.findById(2));
      cE2.setEchantillonType(e2);
      final CouleurEntiteType cE3 = new CouleurEntiteType();
      cE3.setCouleur(couleurDao.findById(3));
      cE3.setEchantillonType(e3);
      final CouleurEntiteType cD1 = new CouleurEntiteType();
      cD1.setCouleur(coul5);
      cD1.setProdType(p1);
      coulTypes.add(cE1);
      coulTypes.add(cE2);
      coulTypes.add(cE3);
      coulTypes.add(cD1);

      banqueManager.createOrsaveManager(b, pf, ct, null, null, null, null, null, null, null, null, null, null, coulTypes,
         null, null, u, null, "creation", "/tmp/");

      assertTrue(banqueManager.findAllObjectsManager().size() == 5);
      b = banqueManager.findByIdManager(b.getBanqueId());
      assertTrue(couleurEntiteTypeManager.findAllObjectsByBanqueManager(b).size() == 4);
      assertTrue(couleurEntiteTypeManager.findAllCouleursForEchanTypeByBanqueManager(b).size() == 3);
      assertTrue(couleurEntiteTypeManager.findAllCouleursForEchanTypeByBanqueManager(b).contains(cE1));
      assertTrue(couleurEntiteTypeManager.findAllCouleursForEchanTypeByBanqueManager(b).contains(cE2));
      assertTrue(couleurEntiteTypeManager.findAllCouleursForEchanTypeByBanqueManager(b).contains(cE3));
      assertTrue(couleurEntiteTypeManager.findAllCouleursForProdTypeByBanqueManager(b).size() == 1);
      assertTrue(couleurEntiteTypeManager.findAllCouleursForProdTypeByBanqueManager(b).contains(cD1));
      assertTrue(couleurEntiteTypeManager.findAllCouleursForProdTypeByBanqueManager(b).get(0).getCouleur().equals(coul5));

      // ajout et remove et modification
      coulTypes = couleurEntiteTypeManager.findAllObjectsByBanqueManager(b);
      coulTypes.remove(cE1);
      coulTypes.remove(cE2);
      final EchantillonType e4 = echantillonTypeDao.findById(4);
      final CouleurEntiteType cE4 = new CouleurEntiteType();
      final Couleur coul4 = couleurDao.findById(4);
      cE4.setCouleur(coul4);
      cE4.setEchantillonType(e4);
      coulTypes.add(cE4);
      // modification
      coulTypes.get(coulTypes.indexOf(cD1)).setCouleur(coul4);

      banqueManager.createOrsaveManager(b, pf, null, null, null, null, null, null, null, null, null, null, null,
         coulTypes, null, null, u, null, "modification", null);

      assertTrue(banqueManager.findAllObjectsManager().size() == 5);
      // recupere la derniere banque
      final Banque b2 = banqueManager.findByIdManager(b.getBanqueId());
      assertTrue(couleurEntiteTypeManager.findAllObjectsByBanqueManager(b2).size() == 3);
      assertTrue(couleurEntiteTypeManager.findAllCouleursForEchanTypeByBanqueManager(b2).size() == 2);
      assertTrue(couleurEntiteTypeManager.findAllCouleursForEchanTypeByBanqueManager(b2).contains(cE3));
      assertTrue(couleurEntiteTypeManager.findAllCouleursForEchanTypeByBanqueManager(b2).contains(cE4));
      assertTrue(couleurEntiteTypeManager.findAllCouleursForProdTypeByBanqueManager(b2).size() == 1);
      assertTrue(couleurEntiteTypeManager.findAllCouleursForProdTypeByBanqueManager(b2).contains(cD1));
      assertTrue(couleurEntiteTypeManager.findAllCouleursForProdTypeByBanqueManager(b2).get(0).getCouleur().equals(coul4));

      // suppression et verification de la cascade
      banqueManager.removeObjectManager(b2, null, u, "/tmp/", true);
      testFindAll();
      assertTrue(couleurEntiteTypeManager.findAllObjectsByBanqueManager(b2).size() == 0);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(b);
      cleanUpFantomes(fs);
   }

   @Test
   public void testCreateOrUpdateBanqueWithProfilUtilisateur() throws IOException{

      //Suppression du FS
      if(new File("/tmp/pt_1").exists()) {
         FileUtils.deleteDirectory(new File("/tmp/pt_1"));
      }
      
      final Plateforme pf = plateformeDao.findById(1);
      final Contexte contexte = contexteDao.findById(1);
      
      //Récupération des profils utilisé pour le test
      final Profil profilConsultation = profilDao.findById(1);
      final Profil profilGestionPatient = profilDao.findById(2);
      final Profil profilAdminBanque = profilDao.findById(4);
      
      //Préparation de la banque
      final Utilisateur admin = utilisateurDao.findById(5);
      final Banque banque = new Banque();
      banque.setNom("BANQUE TEST UTLISATEURPROFIL");
      banque.setPlateforme(pf);
      banque.setDefMaladies(false);
      banque.setContexte(contexte);
      
      //Préparation des utlisateurs
      Utilisateur user1 = new Utilisateur();
      user1.setLogin("USER_TEST_UTLISATEURPROFIL_1");
      user1.setPassword("passwd1");
      user1.setSuperAdmin(false);
      
      Utilisateur user2 = new Utilisateur();
      user2.setLogin("USER_TEST_UTLISATEURPROFIL_2");
      user2.setPassword("passwd2");
      user2.setSuperAdmin(false);
      
      utilisateurManager.saveManager(user1, null, null, null, admin, pf);
      utilisateurManager.saveManager(user2, null, null, null, admin, pf);
      
      //Préparation des ProfilUtilisateur à ajouter à la création de la banque
      final ProfilUtilisateur profilUtilisateur1 = new ProfilUtilisateur();
      profilUtilisateur1.setUtilisateur(user1);
      profilUtilisateur1.setProfil(profilConsultation);
      profilUtilisateur1.setBanque(banque);
      user1.getProfilUtilisateurs().add(profilUtilisateur1);
      
      final ProfilUtilisateur profilUtilisateur2 = new ProfilUtilisateur();
      profilUtilisateur2.setUtilisateur(user2);
      profilUtilisateur2.setProfil(profilAdminBanque);
      profilUtilisateur2.setBanque(banque);
      user2.getProfilUtilisateurs().add(profilUtilisateur2);
      
      final Set<Utilisateur> banqueUsers = new HashSet<>(Arrays.asList(user1, user2));
      
      banqueManager.createOrsaveManager(banque, pf, contexte, null, null, null, null,
         null, null, null, null, null, null, null, null, null, admin, banqueUsers, "creation", "/tmp");
      
      List<ProfilUtilisateur> listPuForBanque = profilUtilisateurDao.findByBanque(banque, false);
      
      assertTrue(listPuForBanque.containsAll(Arrays.asList(profilUtilisateur1, profilUtilisateur2)));
      
      //Préparation du ProfilUtilisateur à ajouter
      final ProfilUtilisateur profilUtilisateur3 = new ProfilUtilisateur();
      profilUtilisateur3.setUtilisateur(user1);
      profilUtilisateur3.setProfil(profilGestionPatient);
      profilUtilisateur3.setBanque(banque);
      user1.getProfilUtilisateurs().add(profilUtilisateur3);

      //Update de la banque (ajout de ProfilUtilisateur)
      banqueManager.createOrsaveManager(banque, pf, contexte, null, null, null, null,
         null, null, null, null, null, null, null, null, null, admin, banqueUsers, "modification", "/tmp");

      listPuForBanque = profilUtilisateurDao.findByBanque(banque, false);
      
      assertTrue(listPuForBanque.containsAll(Arrays.asList(profilUtilisateur1, profilUtilisateur2, profilUtilisateur3)));
      
      //Suppression du profil de consultation pour le user1
      user1.getProfilUtilisateurs().remove(profilUtilisateur1);
      
      //Update de la banque (suppression de ProfilUtilisateur)
      banqueManager.createOrsaveManager(banque, pf, contexte, null, null, null, null,
         null, null, null, null, null, null, null, null, null, admin, banqueUsers, "modification", "/tmp");

      listPuForBanque = profilUtilisateurDao.findByBanque(banque, false);
      
      assertFalse(listPuForBanque.contains(profilUtilisateur1));
      assertTrue(listPuForBanque.containsAll(Arrays.asList(profilUtilisateur2, profilUtilisateur3)));

      //Suppression de la banque et des utilisateurs créées
      utilisateurManager.removeObjectManager(user1);
      utilisateurManager.removeObjectManager(user2);
      banqueManager.removeObjectManager(banque, null, admin, "/tmp/", true);
      cleanUpFantomes(Arrays.asList(banque));
      
   }
   
   @Test
   public void testFindByProfilUtilisateurManager(){
      final Utilisateur u2 = utilisateurDao.findById(2);
      List<Banque> banks = banqueManager.findByProfilUtilisateurManager(u2);
      assertTrue(banks.size() == 3);
      assertTrue(banks.get(0).getNom().equals("BANQUE1"));
      assertTrue(banks.get(1).getNom().equals("BANQUE2"));
      banks.clear();
      final Utilisateur u4 = utilisateurDao.findById(4);
      banks = banqueManager.findByProfilUtilisateurManager(u4);
      assertTrue(banks.size() == 0);
      banks = banqueManager.findByProfilUtilisateurManager(null);
      assertTrue(banks.size() == 0);
   }

   @Test
   public void testGetBanqueTableCodageByBanqueManager(){
      final Banque b1 = banqueManager.findByIdManager(1);
      List<BanqueTableCodage> btcs = banqueManager.getBanqueTableCodageByBanqueManager(b1);
      assertTrue(btcs.size() == 2);
      assertTrue(btcs.get(0).getTableCodage().getNom().equals("ADICAP"));
      assertFalse(btcs.get(0).getLibelleExport());
      assertTrue(btcs.get(1).getLibelleExport());
      final Banque b2 = banqueManager.findByIdManager(2);
      btcs = banqueManager.getBanqueTableCodageByBanqueManager(b2);
      assertTrue(btcs.size() == 1);
      assertTrue(btcs.get(0).getLibelleExport());
      final Banque b3 = banqueManager.findByIdManager(3);
      btcs = banqueManager.getBanqueTableCodageByBanqueManager(b3);
      assertTrue(btcs.size() == 0);
      btcs = banqueManager.getBanqueTableCodageByBanqueManager(null);
      assertTrue(btcs.size() == 0);
   }

   @Test
   public void testCascadeDeletion(){
      Banque b = new Banque();
      final Utilisateur u = utilisateurDao.findById(2);
      b.setNom("Banque a cascader");
      final Plateforme pf = plateformeDao.findById(1);
      final Contexte ct = contexteDao.findById(1);

      final Banque b1 = banqueManager.findByIdManager(1);

      banqueManager.createOrsaveManager(b, pf, ct, b1.getProprietaire(), b1.getCollaborateur(), b1.getContact(),
         new ArrayList<>(banqueManager.getConteneursManager(b1)),
         new ArrayList<>(banqueManager.getBanqueTableCodageByBanqueManager(b1)), null, null, null, null, null, null, null,
         couleurDao.findById(2), u, null, "creation", "/tmp/");

      assertTrue(banqueManager.findAllObjectsManager().size() == 5);
      b = banqueManager.findByIdManager(b.getBanqueId());

      // cree code dosser select
      final CodeDossier codeDos = new CodeDossier();
      codeDos.setNom("dos");
      codeDos.setCodeSelect(true);
      codeDossierManager.createOrUpdateManager(codeDos, null, b, u, "creation");
      // cree code Select
      final CodeSelect codeSel = new CodeSelect();
      codeSel.setCodeId(1);
      codeSel.setTableCodage(tableCodageDao.findById(1));
      codeSelectManager.createOrUpdateManager(codeSel, codeDos, b, u, "creation");
      // cree code dosser utilisateur
      final CodeDossier codeDos2 = new CodeDossier();
      codeDos2.setNom("dos2");
      codeDos2.setCodeSelect(false);
      codeDossierManager.createOrUpdateManager(codeDos2, null, b, u, "creation");
      // cree codew Utilisateur
      final CodeUtilisateur codeU = new CodeUtilisateur();
      codeU.setCode("codeCas");
      codeU.setLibelle("libCasc");
      codeUtilisateurManager.createOrUpdateManager(codeU, codeDos2, b, u, null, null, "creation");
      final CodeUtilisateur codeU2 = new CodeUtilisateur();
      codeU2.setCode("codeCas2");
      codeU2.setLibelle("libCasc2");
      codeUtilisateurManager.createOrUpdateManager(codeU2, null, b, u, codeU, null, "creation");
      // cree code dosser utilisateur
      final CodeDossier codeDos3 = new CodeDossier();
      codeDos3.setNom("dos3");
      codeDos3.setCodeSelect(false);
      codeDossierManager.createOrUpdateManager(codeDos3, codeDos2, b, u, "creation");

      assertTrue(codeSelectManager.findAllObjectsManager().size() == 6);
      assertTrue(codeUtilisateurManager.findAllObjectsManager().size() == 8);
      assertTrue(codeDossierManager.findAllCodeDossiersManager().size() == 7);

      // cree  objets cascables JPA
      final Affichage a = new Affichage();
      a.setIntitule("Aff");
      a.setNbLignes(2);
      affichageManager.saveManager(a, null, u, b);

      final CouleurEntiteType cType = new CouleurEntiteType();
      couleurEntiteTypeManager.saveManager(cType, couleurDao.findById(2), b, echantillonTypeDao.findById(3), null);

      assertFalse(banqueManager.isReferencedObjectManager(b));

      // reference rendant cascade impossible
      final Prelevement p = new Prelevement();
      p.setCode("prel");
      final Prelevement p1 = prelevementManager.findByIdManager(1);
      prelevementManager.saveManager(p, b, p1.getNature(), null, p1.getConsentType(), null, null, null, null, null, null,
         null, null, null, null, null, u, false, "/tmp/", false);

      boolean catched = false;
      try{
         banqueManager.removeObjectManager(b, null, u, "/tmp/", false);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("banque.deletion.isReferenced"));
         assertFalse(ore.isCascadable());
      }
      assertTrue(catched);

      prelevementManager.removeObjectManager(p, null, u, null);
      assertFalse(banqueManager.isReferencedObjectManager(b));

      // reference rendant cascade impossible
      final Echantillon e = new Echantillon();
      e.setCode("echan");
      final Echantillon e1 = echantillonManager.findByIdManager(1);
      echantillonManager.saveManager(e, b, null, null, e1.getObjetStatut(), null, e1.getEchantillonType(), null, null,
         null, null, null, null, u, false, "/tmp/", false);

      catched = false;
      try{
         banqueManager.removeObjectManager(b, null, u, null, false);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("banque.deletion.isReferenced"));
         assertFalse(ore.isCascadable());
      }
      assertTrue(catched);

      echantillonManager.removeObjectManager(e, null, u, null);
      assertFalse(banqueManager.isReferencedObjectManager(b));

      // reference rendant cascade impossible
      final ProdDerive d = new ProdDerive();
      d.setCode("derive");
      final ProdDerive d1 = prodDeriveManager.findByIdManager(1);
      prodDeriveManager.saveManager(d, b, d1.getProdType(), objetStatutDao.findById(4), null, null, null, null, null,
         null, null, null, null, null, u, true, "/tmp/", false);

      catched = false;
      try{
         banqueManager.removeObjectManager(b, null, u, "/tmp/", false);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("banque.deletion.isReferenced"));
         assertFalse(ore.isCascadable());
      }
      assertTrue(catched);

      prodDeriveManager.removeObjectManager(d, null, u, null);
      assertFalse(banqueManager.isReferencedObjectManager(b));

      // reference rendant cascade impossible
      final Cession c = new Cession();
      c.setNumero("34589");
      final Cession c1 = cessionManager.findByIdManager(1);
      cessionManager.saveManager(c, b, c1.getCessionType(), null, null, null, null, null, c1.getCessionStatut(), null,
         null, null, null, null, u, null, "/tmp/");

      catched = false;
      try{
         banqueManager.removeObjectManager(b, null, u, "/tmp/", false);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("banque.deletion.isReferenced"));
         assertFalse(ore.isCascadable());
      }
      assertTrue(catched);

      cessionManager.removeObjectManager(c, null, u, null);

      // suppression et verification de la cascade
      banqueManager.removeObjectManager(b, null, u, "/tmp/", false);
      testFindAll();
      assertTrue(codeSelectManager.findAllObjectsManager().size() == 5);
      assertTrue(codeUtilisateurManager.findAllObjectsManager().size() == 6);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(b);
      fs.add(p);
      fs.add(e);
      fs.add(d);
      fs.add(c);
      cleanUpFantomes(fs);
   }

   @Test
   public void testBanqueFileSystem() throws IOException{
      final Banque b = new Banque();
      b.setNom("BanqueTest");
      final Plateforme pf = plateformeDao.findById(1);
      final Contexte ct = contexteDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(1);

      boolean catched = false;
      try{
         banqueManager.createOrsaveManager(b, pf, ct, null, null, null, null, null, null, null, null, null, null, null,
            null, null, u, null, "creation", null);
      }catch(final RuntimeException re){
         catched = true;
      }
      assertTrue(catched);

      banqueManager.createOrsaveManager(b, pf, ct, null, null, null, null, null, null, null, null, null, null, null, null,
         null, u, null, "creation", "/tmp");

      assertTrue(new File(Utils.writeAnnoFilePath("/tmp/", b, null, null)).exists());
      assertTrue(new File(Utils.writeAnnoFilePath("/tmp/", b, null, null) + "/anno").exists());
      assertTrue(new File(Utils.writeAnnoFilePath("/tmp/", b, null, null) + "/cr_anapath").exists());

      // insertion de fichiers et de dossiers
      new File(Utils.writeAnnoFilePath("/tmp/", b, null, null) + "/anno/chp1").mkdir();
      new File(Utils.writeAnnoFilePath("/tmp/", b, null, null) + "/anno/chp1/test1").createNewFile();

      new File(Utils.writeAnnoFilePath("/tmp/", b, null, null) + "/cr_anapath/test2").createNewFile();

      banqueManager.removeObjectManager(b, null, u, "/tmp/", false);
      assertFalse(new File(Utils.writeAnnoFilePath("/tmp/", b, null, null)).exists());
      assertFalse(new File(Utils.writeAnnoFilePath("/tmp/", b, null, null) + "/anno").exists());
      assertFalse(new File(Utils.writeAnnoFilePath("/tmp/", b, null, null) + "/cr_anapath").exists());

      // clean up
      try{
         FileUtils.deleteDirectory(new File("/tmp/pt_1"));
         Thread.sleep(500);
         assertFalse(new File("/tmp/pt_1").exists());
      }catch(final IOException e){
         assertFalse(true);
      }catch(final InterruptedException e){
         assertFalse(true);
         e.printStackTrace();
      }
      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(b);
      cleanUpFantomes(fs);
   }

   @Test
   public void testFindBanqueForSwitchManager(){
      Prelevement p = prelevementManager.findByIdManager(1);
      Utilisateur u = utilisateurDao.findById(5);
      List<Banque> banks = banqueManager.findBanqueForSwitchManager(p, u);
      assertTrue(banks.size() == 2);
      assertTrue(banks.contains(banqueManager.findByIdManager(2)));
      assertTrue(banks.contains(banqueManager.findByIdManager(3)));
      u = utilisateurDao.findById(1);
      banks = banqueManager.findBanqueForSwitchManager(p, u);
      assertTrue(banks.size() == 1);
      assertTrue(banks.contains(banqueManager.findByIdManager(2)));
      p = prelevementManager.findByIdManager(3);
      banks = banqueManager.findBanqueForSwitchManager(p, u);
      assertTrue(banks.size() == 2);
      assertTrue(banks.contains(banqueManager.findByIdManager(2)));
      assertTrue(banks.contains(banqueManager.findByIdManager(1)));
      banks = banqueManager.findBanqueForSwitchManager(p, null);
      assertTrue(banks.size() == 0);
      u = utilisateurDao.findById(2);
      banks = banqueManager.findBanqueForSwitchManager(p, u);
      assertTrue(banks.size() == 0);
      banks = banqueManager.findBanqueForSwitchManager(null, u);
      assertTrue(banks.size() == 0);
   }

   @Test
   public void testExistingAnnotationValuesException(){
      final Banque b2 = banqueManager.findByIdManager(2);
      final TableAnnotation t4 = tableAnnotationDao.findById(4);
      final TableAnnotation t3 = tableAnnotationDao.findById(3);
      final List<TableAnnotation> tabEchans = new ArrayList<>();
      tabEchans.add(t3);
      tabEchans.add(t4);

      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findById(3), b2).size() == 1);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findById(3), b2).contains(t3));

      banqueManager.createOrsaveManager(b2, b2.getPlateforme(), b2.getContexte(), b2.getProprietaire(),
         b2.getCollaborateur(), b2.getContact(), null, null, null, null, tabEchans, null, null, null, b2.getEchantillonCouleur(),
         b2.getProdDeriveCouleur(), utilisateurDao.findById(1), null, "modification", "/tmp");

      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findById(3), b2).size() == 2);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findById(3), b2).contains(t3));
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findById(3), b2).contains(t4));

      // suppr exception
      tabEchans.clear();
      boolean catched = false;
      try{
         banqueManager.createOrsaveManager(b2, b2.getPlateforme(), b2.getContexte(), b2.getProprietaire(),
            b2.getCollaborateur(), b2.getContact(), null, null, null, null, tabEchans, null, null, null,
            b2.getEchantillonCouleur(), b2.getProdDeriveCouleur(), utilisateurDao.findById(1), null, "modification", "/tmp");
      }catch(final ExistingAnnotationValuesException ex){
         assertTrue(ex.getBanque().equals(b2));
         assertTrue(ex.getTable().equals(t3));
         catched = true;
      }
      assertTrue(catched);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findById(3), b2).size() == 2);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findById(3), b2).contains(t3));
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findById(3), b2).contains(t4));

      tabEchans.add(t3);
      banqueManager.createOrsaveManager(b2, b2.getPlateforme(), b2.getContexte(), b2.getProprietaire(),
         b2.getCollaborateur(), b2.getContact(), null, null, null, null, tabEchans, null, null, null, b2.getEchantillonCouleur(),
         b2.getProdDeriveCouleur(), utilisateurDao.findById(1), null, "modification", "/tmp");

      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findById(3), b2).size() == 1);
      assertTrue(tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findById(3), b2).contains(t3));

      // clean up operations
      final List<Operation> ops = getOperationManager().findByObjectManager(b2);
      for(final Operation operation : ops){
         getOperationManager().removeObjectManager(operation);
      }

      cleanUpFantomes(null);
   }
   
   @Test
   public void testFindByConteneurManager(){

      final Conteneur c1 = conteneurDao.findById(1);
      List<Banque> banks = banqueManager.findByConteneurManager(c1);
      assertTrue(banks.size() == 4);

      final Conteneur c2 = conteneurDao.findById(2);
      banks = banqueManager.findByConteneurManager(c2);
      assertTrue(banks.size() == 2);
      
      banks = banqueManager.findByConteneurManager(new Conteneur());
      assertTrue(banks.isEmpty());

      banks = banqueManager.findByConteneurManager(null);
      assertTrue(banks.isEmpty());
   }
}
