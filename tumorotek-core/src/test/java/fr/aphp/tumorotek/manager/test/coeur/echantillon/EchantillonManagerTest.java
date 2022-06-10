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
package fr.aphp.tumorotek.manager.test.coeur.echantillon;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.qualite.ConformiteTypeDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.dao.qualite.ObjetNonConformeDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteTypeDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.code.CodeAssigneManager;
import fr.aphp.tumorotek.manager.coeur.ObjetStatutManager;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.*;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.context.CollaborateurManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.io.ExtractValueFromChampManager;
import fr.aphp.tumorotek.manager.qualite.OperationTypeManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.manager.systeme.FichierManager;
import fr.aphp.tumorotek.manager.systeme.UniteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.*;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

import static org.junit.Assert.*;

/**
 *
 * Classe de test pour le manager EchantillonManager.
 * Classe créée le 25/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.1
 *
 */
public class EchantillonManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EchantillonManager echantillonManager;
   @Autowired
   private EchantillonTypeManager echantillonTypeManager;
   @Autowired
   private BanqueManager banqueManager;
   @Autowired
   private PrelevementDao prelevementDao;
   @Autowired
   private CollaborateurManager collaborateurManager;
   @Autowired
   private ObjetStatutManager objetStatutManager;
   @Autowired
   private UniteManager uniteManager;
   @Autowired
   private EchanQualiteManager echanQualiteManager;
   @Autowired
   private ModePrepaManager modePrepaManager;
   @Autowired
   private FichierManager fichierManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private EchantillonValidator echantillonValidator;
   @Autowired
   private CodeAssigneManager codeAssigneManager;
   @Autowired
   private TableCodageDao tableCodageDao;
   @Autowired
   private ChampAnnotationDao champAnnotationDao;
   @Autowired
   private AnnotationValeurManager annotationValeurManager;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private OperationTypeManager operationTypeManager;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private TransformationManager transformationManager;
   @Autowired
   private ProdTypeDao prodTypeDao;
   @Autowired
   private ProdDeriveManager prodDeriveManager;
   @Autowired
   private CederObjetManager cederObjetManager;
   @Autowired
   private UniteDao uniteDao;
   @Autowired
   private CessionDao cessionDao;
   @Autowired
   private TerminaleDao terminaleDao;
   @Autowired
   private ConteneurManager conteneurManager;
   @Autowired
   private EnceinteTypeDao enceinteTypeDao;
   @Autowired
   private EmplacementManager emplacementManager;
   @Autowired
   private EnceinteManager enceinteManager;
   @Autowired
   private TerminaleManager terminaleManager;
   @Autowired
   private ItemDao itemDao;
   @Autowired
   private ChampEntiteDao champEntiteDao;
   @Autowired
   private ExtractValueFromChampManager extractValueFromChampManager;
   @Autowired
   private RetourManager retourManager;
   @Autowired
   private NonConformiteDao nonConformiteDao;
   @Autowired
   private ObjetNonConformeDao objetNonConformeDao;
   @Autowired
   private ConformiteTypeDao conformiteTypeDao;
   @Autowired
   @Qualifier("dataSource")
   private DataSource dataSource;
   @Autowired
   private PlateformeDao plateformeDao;

   public EchantillonManagerTest(){}

   @Rule
   public TemporaryFolder testFolder = new TemporaryFolder();

   @Test
   public void testFindById(){
      final Echantillon echan = echantillonManager.findByIdManager(1);
      assertNotNull(echan);
      assertTrue(echan.getCode().equals("PTRA.1"));

      final Echantillon echanNull = echantillonManager.findByIdManager(5);
      assertNull(echanNull);
   }

   @Test
   public void testFindAll(){
      final List<Echantillon> echans = echantillonManager.findAllObjectsManager();
      assertEquals(4, echans.size());
   }

   @Test
   public void testFindByBanquesManager(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      List<Echantillon> res = echantillonManager.findByBanquesManager(banks);
      assertTrue(res.size() == 4);
      banks.clear();
      res = echantillonManager.findByBanquesManager(banks);
      assertTrue(res.size() == 0);
      res = echantillonManager.findByBanquesManager(null);
      assertTrue(res.size() == 0);
   }

   @Test
   public void testFindAllObjectsIdsByBanquesManager(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      List<Integer> res = echantillonManager.findAllObjectsIdsByBanquesManager(banks);
      assertTrue(res.size() == 4);
      banks.clear();
      res = echantillonManager.findAllObjectsIdsByBanquesManager(banks);
      assertTrue(res.size() == 0);
      res = echantillonManager.findAllObjectsIdsByBanquesManager(null);
      assertTrue(res.size() == 0);
   }

   @Test
   public void testFindByIdsInListManager(){
      List<Integer> ids = new ArrayList<>();
      ids.add(1);
      ids.add(2);
      ids.add(3);
      ids.add(10);
      List<Echantillon> res = echantillonManager.findByIdsInListManager(ids);
      assertTrue(res.size() == 3);

      ids = new ArrayList<>();
      res = echantillonManager.findByIdsInListManager(ids);
      assertTrue(res.size() == 0);

      res = echantillonManager.findByIdsInListManager(null);
      assertTrue(res.size() == 0);
   }

   @Test
   public void testFindByEchantillonTypeManager(){
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      List<Echantillon> echans = echantillonManager.findByEchantillonTypeManager(type);
      assertTrue(echans.size() == 3);

      final EchantillonType type2 = echantillonTypeManager.findByIdManager(2);
      echans = echantillonManager.findByEchantillonTypeManager(type2);
      assertTrue(echans.size() == 0);
   }

   @Test
   public void testFindByDate() throws Exception{
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("13/05/2008");
      List<Echantillon> echans = echantillonManager.findByDateStockAfterDateManager(search);
      assertTrue(echans.size() == 2);

      search = new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2009");
      echans = echantillonManager.findByDateStockAfterDateManager(search);
      assertTrue(echans.size() == 1);

      echans = echantillonManager.findByDateStockAfterDateManager(null);
      assertTrue(echans.size() == 0);
   }

   @Test
   public void testFindByDateStockAfterDateWithBanqueManager() throws Exception{
      final Banque b1 = banqueManager.findByIdManager(1);
      final Banque b2 = banqueManager.findByIdManager(2);
      final List<Banque> banks = new ArrayList<>();

      banks.add(b1);
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2009");
      List<Echantillon> echans = echantillonManager.findByDateStockAfterDateWithBanqueManager(search, banks);
      assertTrue(echans.size() == 1);

      banks.add(b2);
      echans = echantillonManager.findByDateStockAfterDateWithBanqueManager(search, banks);
      assertTrue(echans.size() == 1);

      echans = echantillonManager.findByDateStockAfterDateWithBanqueManager(search, null);

      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByDateStockAfterDateWithBanqueManager(null, banks);
      assertTrue(echans.size() == 0);

      banks.clear();
      banks.add(b1);
      search = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2010");
      echans = echantillonManager.findByDateStockAfterDateWithBanqueManager(search, banks);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByDateStockAfterDateWithBanqueManager(null, null);
      assertTrue(echans.size() == 0);
   }

   @Test
   public void testFindLastCreationManager(){
      final Banque b1 = banqueManager.findByIdManager(1);
      final Banque b2 = banqueManager.findByIdManager(2);
      final Banque b3 = banqueManager.findByIdManager(3);

      final List<Banque> banks = new ArrayList<>();

      List<Echantillon> list = echantillonManager.findLastCreationManager(banks, 5);
      assertTrue(list.size() == 0);

      banks.add(b1);
      list = echantillonManager.findLastCreationManager(banks, 5);
      assertTrue(list.size() == 3);

      banks.add(b2);

      list = echantillonManager.findLastCreationManager(banks, 6);
      assertTrue(list.size() == 4);

      banks.add(b3);

      list = echantillonManager.findLastCreationManager(banks, 1);
      assertTrue(list.size() == 1);

      list = echantillonManager.findLastCreationManager(banks, 0);
      assertTrue(list.size() == 0);

      list = echantillonManager.findLastCreationManager(banks, 10);
      assertTrue(list.size() == 4);

      list = echantillonManager.findLastCreationManager(null, 10);
      assertTrue(list.size() == 0);

   }

   @Test
   public void testFindRestantsByPrelevementManager(){
      Prelevement p = prelevementDao.findById(1);
      List<Echantillon> echans = echantillonManager.findRestantsByPrelevementManager(p);
      assertTrue(echans.size() == 1);

      echans = echantillonManager.findRestantsByPrelevementManager(null);
      assertTrue(echans.size() == 0);

      p = prelevementDao.findById(2);
      echans = echantillonManager.findRestantsByPrelevementManager(p);
      assertTrue(echans.size() == 0);
   }

   @Test
   public void testFindByPrelevementAndStatutManager(){
      Prelevement p = prelevementDao.findById(1);
      final ObjetStatut s = objetStatutManager.findByIdManager(1);
      List<Echantillon> echans = echantillonManager.findByPrelevementAndStatutManager(p, s);
      assertTrue(echans.size() == 2);

      p = prelevementDao.findById(2);
      echans = echantillonManager.findByPrelevementAndStatutManager(p, s);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByPrelevementAndStatutManager(null, s);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByPrelevementAndStatutManager(p, null);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByPrelevementAndStatutManager(null, null);
      assertTrue(echans.size() == 0);
   }

   @Test
   public void testFindByPatientNomReturnIdsManager(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      List<Integer> echans = echantillonManager.findByPatientNomReturnIdsManager("DELPHINO", banks, true);
      assertTrue(echans.size() == 3);

      echans = echantillonManager.findByPatientNomReturnIdsManager("ELPH", banks, false);
      assertTrue(echans.size() == 3);

      echans = echantillonManager.findByPatientNomReturnIdsManager("DELP", banks, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByPatientNomReturnIdsManager("DELP%", banks, true);
      assertTrue(echans.size() == 3);

      banks.add(b2);
      echans = echantillonManager.findByPatientNomReturnIdsManager("MAYER", banks, false);
      assertTrue(echans.size() == 1);

      echans = echantillonManager.findByPatientNomReturnIdsManager("SOLIS", banks, false);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByPatientNomReturnIdsManager(null, banks, false);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByPatientNomReturnIdsManager("MAYER", null, true);
      assertTrue(echans.size() == 0);
   }

   @Test
   public void testFindByCodeLikeExactManager(){
      List<Echantillon> echans = echantillonManager.findByCodeLikeManager("PTRA.1", true);
      assertTrue(echans.size() == 1);

      echans = echantillonManager.findByCodeLikeManager("PTRa", true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeManager("XXX", true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeManager("", true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeManager(null, true);
      assertTrue(echans.size() == 0);
   }

   @Test
   public void testFindByCodeLikeManager(){
      List<Echantillon> echans = echantillonManager.findByCodeLikeManager("PTRA", false);
      assertTrue(echans.size() == 2);

      echans = echantillonManager.findByCodeLikeManager("XXX", false);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeManager("", false);
      assertTrue(echans.size() == 4);

      echans = echantillonManager.findByCodeLikeManager(null, false);
      assertTrue(echans.size() == 0);
   }

   @Test
   public void testFindByCodeLikeWithBanqueManager(){
      final Banque b1 = banqueManager.findByIdManager(1);
      final Banque b2 = banqueManager.findByIdManager(2);

      List<Echantillon> echans = echantillonManager.findByCodeLikeWithBanqueManager("PTRA", b1, false);
      assertTrue(echans.size() == 2);

      echans = echantillonManager.findByCodeLikeWithBanqueManager("PTRA", b2, false);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeWithBanqueManager("PTRA", null, false);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeWithBanqueManager("XXX", b1, false);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeWithBanqueManager("", b1, false);
      assertTrue(echans.size() == 3);

      echans = echantillonManager.findByCodeLikeWithBanqueManager(null, b1, false);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeWithBanqueManager(null, null, false);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeWithBanqueManager("PTRA.1", b1, true);
      assertTrue(echans.size() == 1);

      echans = echantillonManager.findByCodeLikeWithBanqueManager("PTRA.1", b2, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeWithBanqueManager("PTRA.1", null, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeWithBanqueManager("PTRa", b1, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeWithBanqueManager("XXX", b1, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeWithBanqueManager("", b1, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeWithBanqueManager(null, b1, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeWithBanqueManager(null, null, true);
      assertTrue(echans.size() == 0);
   }

   @Test
   public void testFindByCodeLikeBothSideWithBanqueReturnIdsManager(){
      final Banque b1 = banqueManager.findByIdManager(1);
      final Banque b2 = banqueManager.findByIdManager(2);

      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);

      List<Integer> echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("TRA", banks, false);
      assertTrue(echans.size() == 2);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("PTRA", banks, false);
      assertTrue(echans.size() == 2);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("TRA.1", banks, false);
      assertTrue(echans.size() == 1);

      banks.add(b2);
      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("TRA", banks, false);
      assertTrue(echans.size() == 2);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("TRA", null, false);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("XXX", banks, false);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("", banks, false);
      assertTrue(echans.size() == 4);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager(null, banks, false);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager(null, null, false);
      assertTrue(echans.size() == 0);

      banks.clear();
      banks.add(b1);
      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("PTRA.1", banks, true);
      assertTrue(echans.size() == 1);

      banks.clear();
      banks.add(b2);
      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("PTRA.1", banks, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("PTRA.1", null, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("TRA", banks, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("XXX", banks, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager("", banks, true);
      assertTrue(echans.size() == 0);

      banks.add(b1);
      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager(null, banks, true);
      assertTrue(echans.size() == 0);

      echans = echantillonManager.findByCodeLikeBothSideWithBanqueReturnIdsManager(null, null, true);
      assertTrue(echans.size() == 0);
   }

   @Test
   public void testFindAllCodesForBanqueManager(){
      final Banque b = banqueManager.findByIdManager(1);
      List<String> codes = echantillonManager.findAllCodesForBanqueManager(b);
      assertTrue(codes.size() == 3);
      assertTrue(codes.get(1).equals("PTRA.2"));

      final Banque b2 = banqueManager.findByIdManager(2);
      codes = echantillonManager.findAllCodesForBanqueManager(b2);
      assertTrue(codes.size() == 1);

      codes = echantillonManager.findAllCodesForBanqueManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindAllCodesForBanqueAndStockesManager(){
      final Banque b = banqueManager.findByIdManager(1);
      List<String> codes = echantillonManager.findAllCodesForBanqueAndStockesManager(b);
      assertTrue(codes.size() == 2);
      assertTrue(codes.get(0).equals("PTRA.1"));

      final Banque b2 = banqueManager.findByIdManager(2);
      codes = echantillonManager.findAllCodesForBanqueAndStockesManager(b2);
      assertTrue(codes.size() == 0);

      codes = echantillonManager.findAllCodesForBanqueAndStockesManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindAllCodesForMultiBanquesAndStockesManager(){
      final List<Banque> banques = new ArrayList<>();
      final Banque b1 = banqueManager.findByIdManager(1);
      final Banque b2 = banqueManager.findByIdManager(2);
      banques.add(b1);
      banques.add(b2);
      List<String> codes = echantillonManager.findAllCodesForMultiBanquesAndStockesManager(banques);
      assertTrue(codes.size() == 2);
      assertTrue(codes.get(0).equals("PTRA.1"));

      banques.remove(b1);
      codes = echantillonManager.findAllCodesForMultiBanquesAndStockesManager(banques);
      assertTrue(codes.size() == 0);

      codes = echantillonManager.findAllCodesForMultiBanquesAndStockesManager(null);
      assertTrue(codes.size() == 0);
   }

   /**
    * Test la méthode findAllCodesForBanqueAndQuantiteManager.
    */
   @Test
   public void testFindAllCodesForBanqueAndQuantiteManager(){
      final Banque b = banqueManager.findByIdManager(1);
      List<String> codes = echantillonManager.findAllCodesForBanqueAndQuantiteManager(b);
      assertTrue(codes.size() == 2);
      assertTrue(codes.get(0).equals("PTRA.1"));
      assertTrue(codes.get(1).equals("PTRA.2"));

      final Banque b2 = banqueManager.findByIdManager(2);
      codes = echantillonManager.findAllCodesForBanqueAndQuantiteManager(b2);
      assertTrue(codes.size() == 0);

      codes = echantillonManager.findAllCodesForBanqueAndQuantiteManager(null);
      assertTrue(codes.size() == 0);
   }

   /**
    * Test la méthode getPrelevementManager.
    */
   @Test
   public void testGetPrelevementManager(){
      final Echantillon echan1 = echantillonManager.findByIdManager(1);
      final Prelevement prlvt1 = echantillonManager.getPrelevementManager(echan1);
      assertTrue(prlvt1.getCode().equals("PRLVT1"));
      assertTrue(prlvt1.getClass().getSimpleName().equals("Prelevement"));

      final Echantillon echan2 = echantillonManager.findByIdManager(3);
      final Prelevement prlvt2 = echantillonManager.getPrelevementManager(echan2);
      assertTrue(prlvt2.getCode().equals("PRLVT2"));

      final Prelevement prlvtNull = echantillonManager.getPrelevementManager(null);
      assertNull(prlvtNull);
   }

   /**
    * Test la méthode getProdDerivesManager.
    */
   @Test
   public void testGetProdDerivesManager(){
      final Echantillon echan1 = echantillonManager.findByIdManager(1);
      List<ProdDerive> derives = echantillonManager.getProdDerivesManager(echan1);
      assertTrue(derives.size() == 1);

      final Echantillon echan2 = echantillonManager.findByIdManager(2);
      derives = echantillonManager.getProdDerivesManager(echan2);
      assertTrue(derives.size() == 0);

      derives = echantillonManager.getProdDerivesManager(null);
      assertTrue(derives.size() == 0);
   }

   /**
    * Test la méthode getEmplacementManager.
    */
   @Test
   public void testGetEmplacementManager(){
      final Echantillon echan1 = echantillonManager.findByIdManager(1);
      final Emplacement empl1 = echantillonManager.getEmplacementManager(echan1);
      assertNull(empl1);

      final Echantillon echan2 = echantillonManager.findByIdManager(2);
      final Emplacement empl2 = echantillonManager.getEmplacementManager(echan2);
      assertNotNull(empl2);
      assertTrue(empl2.getPosition().equals(3));
      assertFalse(empl2.getVide());

      final Emplacement emplNull = echantillonManager.getEmplacementManager(null);
      assertNull(emplNull);
   }

   /**
    * Test la méthode getEmplacementAdrlManager.
    */
   @Test
   public void testGetEmplacementAdrlManager(){
      final Echantillon echan1 = echantillonManager.findByIdManager(1);
      final String adrl1 = echantillonManager.getEmplacementAdrlManager(echan1);
      assertNotNull(adrl1);
      assertTrue(adrl1.equals(""));

      final Echantillon echan2 = echantillonManager.findByIdManager(2);
      final String adrl2 = echantillonManager.getEmplacementAdrlManager(echan2);
      assertNotNull(adrl2);
      assertTrue(adrl2.equals("CC1.R1.T1.BT1.A-C"));

      final String adrlNull = echantillonManager.getEmplacementAdrlManager(null);
      assertNotNull(adrlNull);
      assertTrue(adrlNull.equals(""));
   }

   /**
    * Test la méthode isUsedObjectManager.
    */
   @Test
   public void testIsUsedObjectManager(){
      final Echantillon echan1 = echantillonManager.findByIdManager(1);
      assertNotNull(echan1);
      assertTrue(echantillonManager.isUsedObjectManager(echan1));

      final Echantillon echan2 = echantillonManager.findByIdManager(3);
      assertNotNull(echan2);
      assertFalse(echantillonManager.isUsedObjectManager(echan2));
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      final Banque banque1 = banqueManager.findByIdManager(1);
      final Banque banque2 = banqueManager.findByIdManager(2);
      final Echantillon echan = new Echantillon();
      echan.setCode("PTRA.1");
      echan.setBanque(banque2);
      assertFalse(echan.equals(echantillonManager.findByIdManager(1)));
      assertTrue(echantillonManager.findDoublonManager(echan));

      echan.setBanque(banque1);
      assertTrue(echan.equals(echantillonManager.findByIdManager(1)));
      assertTrue(echantillonManager.findDoublonManager(echan));

      // pf
      echan.setBanque(banqueDao.findById(4));
      assertFalse(echan.equals(echantillonManager.findByIdManager(1)));
      assertFalse(echantillonManager.findDoublonManager(echan));

      // null
      echan.setBanque(null);
      assertFalse(echan.equals(echantillonManager.findByIdManager(1)));
      assertFalse(echantillonManager.findDoublonManager(echan));

      echan.setCode("PTRA.3");
      echan.setBanque(banque1);
      assertFalse(echantillonManager.findDoublonManager(echan));

      final Echantillon echan2 = echantillonManager.findByIdManager(1);
      assertFalse(echantillonManager.findDoublonManager(echan2));

      assertFalse(echantillonManager.findDoublonManager(echan2));

      echan2.setCode("PTRA.2");
      assertTrue(echantillonManager.findDoublonManager(echan2));
   }

   @Test
   public void testFindAfterDateCreationReturnIdsManager() throws ParseException{
      final Banque b1 = banqueManager.findByIdManager(1);
      final Banque b2 = banqueManager.findByIdManager(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      //recherche fructueuse
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009");
      final Calendar cal = Calendar.getInstance();
      cal.setTime(search);
      List<Integer> liste = echantillonManager.findAfterDateCreationReturnIdsManager(cal, banks);
      assertTrue(liste.size() == 2);
      //recherche infructueuse
      search = new SimpleDateFormat("dd/MM/yyyy").parse("08/11/2009");
      cal.setTime(search);
      liste = echantillonManager.findAfterDateCreationReturnIdsManager(cal, banks);
      assertTrue(liste.size() == 0);
      //null recherche
      liste = echantillonManager.findAfterDateCreationReturnIdsManager(null, banks);
      assertTrue(liste.size() == 0);

      // recherche avec une banque null
      search = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009");
      cal.setTime(search);
      liste = echantillonManager.findAfterDateCreationReturnIdsManager(cal, null);
      assertTrue(liste.size() == 0);
      //null recherche
      liste = echantillonManager.findAfterDateCreationReturnIdsManager(null, null);
      assertTrue(liste.size() == 0);

      banks.add(b2);
      liste = echantillonManager.findAfterDateCreationReturnIdsManager(cal, banks);
      assertTrue(liste.size() == 3);
   }

   /**
    * Test la methode findAfterDateModificationManager.
    * @throws ParseException 
    */
   @Test
   public void testFindAfterDateModificationManager() throws ParseException{
      final Banque b1 = banqueManager.findByIdManager(1);
      //recherche fructueuse
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
      final Calendar cal = Calendar.getInstance();
      cal.setTime(search);
      List<Echantillon> liste = echantillonManager.findAfterDateModificationManager(cal, b1);
      assertTrue(liste.size() == 1);
      //recherche infructueuse
      search = new SimpleDateFormat("dd/MM/yyyy").parse("31/11/2009");
      cal.setTime(search);
      liste = echantillonManager.findAfterDateModificationManager(cal, b1);
      assertTrue(liste.size() == 0);
      //null recherche
      liste = echantillonManager.findAfterDateModificationManager(null, b1);
      assertTrue(liste.size() == 0);

      // recherche avec une banque null
      search = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
      cal.setTime(search);
      liste = echantillonManager.findAfterDateModificationManager(cal, null);
      assertTrue(liste.size() == 0);
      //null recherche
      liste = echantillonManager.findAfterDateModificationManager(null, null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testItemINCa50To53Manager(){
      final Echantillon e1 = echantillonManager.findByIdManager(1);
      assertTrue(echantillonManager.itemINCa50To53Manager(e1, "CELLULES"));

      assertTrue(echantillonManager.itemINCa50To53Manager(e1, "SANG"));

      assertTrue(echantillonManager.itemINCa50To53Manager(e1, "ADN"));

      assertFalse(echantillonManager.itemINCa50To53Manager(e1, "TISSUS"));

      assertFalse(echantillonManager.itemINCa50To53Manager(e1, null));

      assertFalse(echantillonManager.itemINCa50To53Manager(null, "SANG"));
   }

   @Test
   public void testFindByCodeInListManager(){
      final List<String> criteres = new ArrayList<>();
      criteres.add("PTRA.1");
      criteres.add("PTRA.2");
      criteres.add("JEG.1");
      criteres.add("BOMB");
      criteres.add("CALM");
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));

      final List<String> notfounds = new ArrayList<>();

      List<Integer> liste = echantillonManager.findByCodeInListManager(criteres, bks, notfounds);
      assertTrue(liste.size() == 3);
      assertTrue(notfounds.size() == 2);
      assertTrue(notfounds.contains("BOMB"));
      assertTrue(notfounds.contains("CALM"));

      criteres.clear();
      notfounds.clear();
      criteres.add("PTRA.1");
      liste = echantillonManager.findByCodeInListManager(criteres, bks, notfounds);
      assertTrue(liste.size() == 1);
      assertTrue(notfounds.isEmpty());

      liste = echantillonManager.findByCodeInListManager(null, bks, notfounds);
      assertTrue(liste.size() == 0);
      liste = echantillonManager.findByCodeInListManager(criteres, null, notfounds);
      assertTrue(liste.size() == 0);
      liste = echantillonManager.findByCodeInListManager(new ArrayList<String>(), bks, notfounds);
      assertTrue(liste.size() == 0);
      liste = echantillonManager.findByCodeInListManager(criteres, new ArrayList<Banque>(), notfounds);
      assertTrue(liste.size() == 0);
      liste = echantillonManager.findByCodeInListManager(criteres, bks, null);
      assertTrue(liste.size() == 0);
      assertTrue(notfounds.isEmpty());
   }

   @Test
   public void testFindByPatientNomOrNipInListManager(){
      List<String> criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("MAYER");
      criteres.add("SOLIS");
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));

      List<Integer> liste = echantillonManager.findByPatientNomOrNipInListManager(criteres, bks);
      assertTrue(liste.size() == 4);

      criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("12");
      liste = echantillonManager.findByPatientNomOrNipInListManager(criteres, bks);
      assertTrue(liste.size() == 4);

      criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("876");
      liste = echantillonManager.findByPatientNomOrNipInListManager(criteres, bks);
      assertTrue(liste.size() == 3);

      liste = echantillonManager.findByPatientNomOrNipInListManager(null, bks);
      assertTrue(liste.size() == 0);
      liste = echantillonManager.findByPatientNomOrNipInListManager(criteres, null);
      assertTrue(liste.size() == 0);
      liste = echantillonManager.findByPatientNomOrNipInListManager(new ArrayList<String>(), bks);
      assertTrue(liste.size() == 0);
      liste = echantillonManager.findByPatientNomOrNipInListManager(criteres, new ArrayList<Banque>());
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testCrud(){

      // Récupération des objets associés à un produit dérivé
      final Banque banque = banqueManager.findByIdManager(1);
      final Prelevement prelevement = prelevementDao.findById(1);
      final Collaborateur collab = collaborateurManager.findByIdManager(1);
      ObjetStatut statut = objetStatutManager.findByIdManager(1);
      //Emplacement emplacement = emplacementManager.findByIdManager(1);
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final Unite quantite = uniteManager.findByIdManager(1);
      final EchanQualite qualite = echanQualiteManager.findByIdManager(1);
      final ModePrepa prepa = modePrepaManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      // Insertion
      final Echantillon echan1 = new Echantillon();
      echan1.setCode("Code");
      echan1.setType(type);

      final Emplacement emp = new Emplacement();
      emp.setTerminale(terminaleDao.findById(6));
      emp.setPosition(3);
      emp.setObjetId(echan1.getEchantillonId());
      emp.setEntite(entiteDao.findById(3));
      emp.setVide(false);

      Boolean catched = false;
      // on test l'insertion avec la banque nulle
      try{
         echantillonManager.createObjectManager(echan1, null, null, null, null, null, type, null, null, null, null, null,
            null, null, true, null, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec le type nul
      try{
         echantillonManager.createObjectManager(echan1, banque, null, null, null, null, null, null, null, null, null, null,
            null, null, true, null, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec le statut null
      try{
         echantillonManager.createObjectManager(echan1, banque, null, null, null, null, type, null, null, null, null, null,
            null, null, true, null, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // Emplacement occupé par un dérivé
      echan1.setCode("PTRA.3");
      final Emplacement empl = emplacementManager.findByIdManager(2);
      catched = false;
      try{
         echantillonManager.createObjectManager(echan1, banque, null, null, statut, empl, type, null, null, null, null,
            null, null, null, true, null, false);
      }catch(final TKException ex){
         catched = true;
         assertTrue(ex.getMessage().equals("PTRA.3 : error.emplacement.notEmpty"));
         assertTrue(ex.getIdentificationObjetException().equals("PTRA.3"));
      }
      assertTrue(catched);
      
      echan1.setCode("PTRA.1");
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         echantillonManager.createObjectManager(echan1, banque, null, null, statut, null, type, null, null, null, null,
            null, null, null, true, null, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);

      // Test de la validation lors de la création
      validationInsert(echan1);
      assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
      // on teste une insertion valide avec les associations 
      // non obigatoires nulles et en sautant la validation
      echan1.setCode("PTRA.3");
      echan1.setDelaiCgl((float) -1.0);
      echan1.setQuantite(null);
      echan1.setQuantiteInit(null);
      echantillonManager.createObjectManager(echan1, banque, null, null, statut, null, type, null, null, null, null, null,
         null, utilisateur, false, null, false);
      assertTrue(echantillonManager.findByCodeLikeManager("PTRA.3", true).size() == 1);
      assertNull(echan1.getSterile());
      assertTrue(getOperationManager().findByObjectManager(echan1).size() == 1);
      Echantillon testInsert = echantillonManager.findByCodeLikeManager("PTRA.3", true).get(0);
      assertTrue(testInsert.getDelaiCgl() < 0);
      assertNull(testInsert.getQuantite());
      assertNull(testInsert.getQuantiteInit());

      // on teste une insertion avec les associations 
      // non obigatoires remplies 
      final Echantillon echan2 = new Echantillon();
      final float num1 = (float) 15.1111;
      final float num2 = (float) 15.2555;
      echan2.setCode("PTRA.4");
      echan2.setQuantite(num1);
      echan2.setQuantiteInit(num2);
      echan2.setDelaiCgl((float) 1.0);
      echan2.setSterile(false);
      echan2.setConformeTraitement(false);
      echan2.setConformeCession(true);

      echantillonManager.createObjectManager(echan2, banque, prelevement, collab, statut, emp, type, null, quantite, qualite,
         prepa, null, null, utilisateur, true, null, false);
      assertTrue(echantillonManager.findByCodeLikeManager("PTRA.4", true).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(echan2).size() == 1);

      // On vérifie que toutes associations ont étées enregistrées
      testInsert = echantillonManager.findByCodeLikeManager("PTRA.4", true).get(0);
      assertNotNull(testInsert.getBanque());
      assertNotNull(testInsert.getPrelevement());
      assertNotNull(testInsert.getCollaborateur());
      assertNotNull(testInsert.getEmplacement());
      assertNotNull(testInsert.getEchantillonType());
      assertNotNull(testInsert.getQuantiteUnite());
      assertNotNull(testInsert.getEchanQualite());
      assertNotNull(testInsert.getModePrepa());
      // On test les attributs
      assertTrue(testInsert.getQuantite() == (float) 15.111);
      assertTrue(testInsert.getQuantiteInit() == (float) 15.255);
      assertTrue(testInsert.getDelaiCgl() > 0);
      assertFalse(testInsert.getConformeTraitement());
      assertTrue(testInsert.getConformeCession());

      // Suppression
      final Echantillon echan3 = echantillonManager.findByCodeLikeManager("PTRA.3", true).get(0);
      echantillonManager.removeObjectManager(echan3, null, utilisateur, null);
      final Echantillon echan4 = echantillonManager.findByCodeLikeManager("PTRA.4", true).get(0);
      echantillonManager.removeObjectManager(echan4, null, utilisateur, null);
      assertTrue(getOperationManager().findByObjectManager(echan1).size() == 0);
      assertTrue(getOperationManager().findByObjectManager(echan2).size() == 0);

      // Update
      final String codeUpdated1 = "PTRA.5";
      final String codeUpdated2 = "PTRA.2";
      final String codeUpdated3 = "PTRA.6";
      final Echantillon echan5 = new Echantillon();
      echan5.setCode("PTRA.3");
      echan5.setSterile(null);
      statut = objetStatutManager.findByIdManager(4);
      echantillonManager.createObjectManager(echan5, banque, null, null, statut, null, type, null, null, null, null, null,
         null, utilisateur, true, null, false);
      assertTrue(echantillonManager.findByCodeLikeManager("PTRA.3", true).size() == 1);

      Boolean catchedUpdate = false;
      // on test l'update avec une banque nulle
      final Echantillon echan6 = echantillonManager.findByCodeLikeManager("PTRA.3", true).get(0);
      echan6.setCode(codeUpdated1);
      echan6.setBanque(null);     
      try{
         echantillonManager.updateObjectManager(echan6, null, null, null, null, null, type, null, null, null, null, null,
            null, null, null, null, null, true, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;

      // on test l'update avec un type null
      final Echantillon echan8 = echantillonManager.findByCodeLikeManager("PTRA.3", true).get(0);
      echan8.setCode(codeUpdated1);
      echan8.setEchantillonType(null);     
      try{
         echantillonManager.updateObjectManager(echan8, banque, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, true, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;
      
      final Echantillon echan10 = echantillonManager.findByCodeLikeManager("PTRA.3", true).get(0);
      // MAJ emplacemet Emplacement occupé par un dérivé
      catched = false;
      try{
         echantillonManager.updateObjectManager(echan10, banque, null, null, null, empl, type, null, null, null, null, null,
            null, null, null, null, utilisateur, false, null, null);
      }catch(final TKException ex){
         catched = true;
         assertTrue(ex.getMessage().equals("PTRA.3 : error.emplacement.notEmpty"));
         assertTrue(ex.getIdentificationObjetException().equals("PTRA.3"));
      }
      assertTrue(catched);

      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      final Echantillon echan9 = echantillonManager.findByCodeLikeManager("PTRA.3", true).get(0);
      echan9.setCode(codeUpdated2);
      try{
         echantillonManager.updateObjectManager(echan9, banque, null, null, null, null, type, null, null, null, null, null,
            null, null, null, null, null, true, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;



      // On test la validation lors d'un update
      validationUpdate(echan10);
      // On teste une mise à jour valide avec les assos a null
      // et en sautant la validation
      echan10.setDelaiCgl((float) -1.0);
      echan10.setQuantite(null);
      echan10.setQuantiteInit(null);
      echantillonManager.updateObjectManager(echan10, banque, null, null, null, null, type, null, null, null, null, null,
         null, null, null, null, utilisateur, false, null, null);
      //Echantillon echan1 = echantillonManager.findByIdManager(7);
      assertTrue(getOperationManager().findByObjectManager(echan10).size() == 2);
      Echantillon testInsert2 = echantillonManager.findByCodeLikeManager("test", true).get(0);
      assertTrue(testInsert2.getDelaiCgl() < 0);
      assertNull(testInsert2.getQuantite());
      assertNull(testInsert2.getQuantiteInit());

      // On teste une mise à jour valide avec les assos
      final Echantillon echan12 = echantillonManager.findByCodeLikeManager("test", true).get(0);
      echan12.setCode(codeUpdated3);
      echan12.setQuantite(num1);
      echan12.setQuantiteInit(num2);
      echan12.setDelaiCgl((float) 1.0);
      echan12.setConformeTraitement(true);
      echan12.setConformeCession(false);
      final List<OperationType> opTypes = new ArrayList<>();
      opTypes.add(operationTypeManager.findByIdManager(1));
      opTypes.add(operationTypeManager.findByIdManager(2));
      echantillonManager.updateObjectManager(echan12, banque, prelevement, collab, objetStatutManager.findByIdManager(1), emp,
         type, null, null, quantite, qualite, prepa, null, null, null, null, utilisateur, true, opTypes, null);
      final Echantillon echan13 = echantillonManager.findByCodeLikeManager("PTRA.6", true).get(0);
      assertTrue(echan13.getCode().equals(codeUpdated3));
      assertTrue(getOperationManager().findByObjectManager(echan12).size() == 5);

      // On vérifie que toutes associations ont étées enregistrées
      testInsert2 = echantillonManager.findByCodeLikeManager("PTRA.6", true).get(0);
      assertNotNull(testInsert2.getBanque());
      assertNotNull(testInsert2.getPrelevement());
      assertNotNull(testInsert2.getObjetStatut());
      assertNotNull(testInsert2.getCollaborateur());
      assertNotNull(testInsert2.getEmplacement());
      assertNotNull(testInsert2.getEchantillonType());
      assertNotNull(testInsert2.getQuantiteUnite());
      assertNotNull(testInsert2.getEchanQualite());
      assertNotNull(testInsert2.getModePrepa());
      assertNull(testInsert2.getCrAnapath());
      // On test les attributs
      assertTrue(testInsert2.getQuantite() == (float) 15.111);
      assertTrue(testInsert2.getQuantiteInit() == (float) 15.255);
      assertTrue(testInsert2.getDelaiCgl() > 0);
      assertTrue(testInsert2.getConformeTraitement());
      assertFalse(testInsert2.getConformeCession());

      final Echantillon echan14 = echantillonManager.findByCodeLikeManager("PTRA.6", true).get(0);
      // Emplacement empl14 = echan14.getEmplacement();
      echantillonManager.removeObjectManager(echan14, null, utilisateur, null);
      assertNull(echantillonManager.findByIdManager(7));
      assertTrue(getOperationManager().findByObjectManager(echan14).size() == 0);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(echan3);
      fs.add(echan4);
      fs.add(echan14);
      cleanUpFantomes(fs);
   }

   /**
    * Test la validation d'un échantillon lors de sa création.
    * @param echantillon Echantillon à tester.
    */
   private void validationInsert(final Echantillon echantillon){
      final Banque banque = banqueManager.findByIdManager(1);
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);
      final ObjetStatut st = objetStatutManager.findByIdManager(4);
      echantillon.setObjetStatut(st);

      boolean catchedInsert = false;
      // On teste une insertion avec un attribut code non valide
      final String[] emptyValues = new String[] {"", "  ", null, "%$*gd", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            echantillon.setCode(emptyValues[i]);
            echantillonManager.createObjectManager(echantillon, banque, null, null, st, null, type, null, null, null, null,
               null, null, utilisateur, true, null, false);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      echantillon.setCode("test");

      // On teste une insertion avec un attribut quantiteInit non valide
      final float negative = (float) -1.0;
      echantillon.setQuantiteInit(negative);
      catchedInsert = false;
      try{
         echantillonManager.createObjectManager(echantillon, banque, null, null, st, null, type, null, null, null, null,
            null, null, utilisateur, true, null, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      echantillon.setQuantiteInit(null);

      // On teste une insertion avec un attribut quantite non valide
      echantillon.setQuantite(negative);
      catchedInsert = false;
      try{
         echantillonManager.createObjectManager(echantillon, banque, null, null, st, null, type, null, null, null, null,
            null, null, utilisateur, true, null, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      echantillon.setQuantite(null);

      // On teste une insertion avec un attribut quantite > quantiteInit
      final float q1 = (float) 2.0;
      final float q2 = (float) 1.0;
      echantillon.setQuantite(q1);
      echantillon.setQuantiteInit(q2);
      catchedInsert = false;
      try{
         echantillonManager.createObjectManager(echantillon, banque, null, null, null, null, type, null, null, null, null,
            null, null, utilisateur, true, null, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      echantillon.setQuantite(null);
      echantillon.setQuantiteInit(null);

      // On teste une insertion avec un attribut delaCgl non valide
      echantillon.setDelaiCgl(negative);
      catchedInsert = false;
      try{
         echantillonManager.createObjectManager(echantillon, banque, null, null, null, null, type, null, null, null, null,
            null, null, utilisateur, true, null, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      echantillon.setDelaiCgl(null);

      // On teste une insertion avec un attribut lateralite non valide
      echantillon.setLateralite("O3");
      catchedInsert = false;
      try{
         echantillonManager.createObjectManager(echantillon, banque, null, null, null, null, type, null, null, null, null,
            null, null, utilisateur, true, null, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      echantillon.setLateralite("I");
   }

   /**
    * Test la validation d'un échantillon lors de sa modification.
    * @param echantillon Echantillon à valider.
    */
   private void validationUpdate(final Echantillon echantillon){
      final Banque banque = banqueManager.findByIdManager(1);
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      boolean catchedInsert = false;
      // On teste une modif avec un attribut code non valide
      final String[] emptyValues = new String[] {"", "  ", null, "%$*gd", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            echantillon.setCode(emptyValues[i]);
            echantillonManager.updateObjectManager(echantillon, banque, null, null, null, null, type, null, null, null,
               null, null, null, null, null, null, utilisateur, true, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      echantillon.setCode("test");

      // On teste une modif avec un attribut quantiteInit non valide
      final float negative = (float) -1.0;
      echantillon.setQuantiteInit(negative);
      catchedInsert = false;
      try{
         echantillonManager.updateObjectManager(echantillon, banque, null, null, null, null, type, null, null, null, null,
            null, null, null, null, null, utilisateur, true, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      echantillon.setQuantiteInit(null);

      // On teste une modif avec un attribut quantite non valide
      echantillon.setQuantite(negative);
      catchedInsert = false;
      try{
         echantillonManager.updateObjectManager(echantillon, banque, null, null, null, null, type, null, null, null, null,
            null, null, null, null, null, utilisateur, true, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      echantillon.setQuantite(null);

   }

   /**
    * Teste la validation de la date de stockage.
    * @throws ParseException 
    */
   @Test
   public void testDateStockageCoherence() throws ParseException{
      final Prelevement p = prelevementDao.findById(3).clone();
      p.setPrelevementId(null);
      Echantillon e = new Echantillon();
      e.setSterile(false);
      e.setCode("test");
      e.setPrelevement(p);
      p.setDatePrelevement(null);
      p.setDateDepart(null);
      p.setDateArrivee(null);
      final Set<LaboInter> set = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      set.add(l1);
      final LaboInter l3 = new LaboInter();
      l3.setOrdre(3);
      set.add(l3);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      set.add(l2);
      p.setLaboInters(set);

      final Calendar val1 = Calendar.getInstance();
      val1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1970"));
      final Calendar ref = Calendar.getInstance();
      ref.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1974"));
      final Calendar val2 = Calendar.getInstance();
      val2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1975"));

      final Calendar dateStock = Calendar.getInstance();
      dateStock.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1970 12:00:13"));

      e.setDateStock(dateStock);
      //e.setDateStock(val1);
      // limites inf
      p.setDatePrelevement(null);
      Errors errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertEquals("date.validation.infDateNaissance", errs.getFieldError().getCode());

      p.setDatePrelevement(ref);
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertEquals("date.validation.infDatePrelevement", errs.getFieldError().getCode());

      //		p.setDateCongelation(ref);
      //		errs = echantillonValidator.checkDateStockageCoherence(e);
      //		assertEquals("date.validation.infDateCongelationPrelevement",
      //												errs.getFieldError().getCode());
      p.setDatePrelevement(dateStock);
      p.setDateDepart(ref);
      errs = echantillonValidator.checkDateStockageCoherence(e, false);
      assertEquals("date.validation.infDateDepartPrelevement", errs.getFieldError().getCode());
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertTrue(errs.getAllErrors().size() == 0);

      // teste labo Inters
      l1.setDateArrivee(ref);
      errs = echantillonValidator.checkDateStockageCoherence(e, false);
      assertEquals("date.validation.infLastDateLaboInter", errs.getFieldError().getCode());
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertTrue(errs.getAllErrors().size() == 0);

      l1.setDateDepart(ref);
      errs = echantillonValidator.checkDateStockageCoherence(e, false);
      assertEquals("date.validation.infLastDateLaboInter", errs.getFieldError().getCode());
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertTrue(errs.getAllErrors().size() == 0);

      l2.setDateArrivee(ref);
      errs = echantillonValidator.checkDateStockageCoherence(e, false);
      assertEquals("date.validation.infLastDateLaboInter", errs.getFieldError().getCode());
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertTrue(errs.getAllErrors().size() == 0);

      l2.setDateDepart(ref);
      errs = echantillonValidator.checkDateStockageCoherence(e, false);
      assertEquals("date.validation.infLastDateLaboInter", errs.getFieldError().getCode());
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertTrue(errs.getAllErrors().size() == 0);

      p.setDateArrivee(ref);
      errs = echantillonValidator.checkDateStockageCoherence(e, false);
      assertEquals("date.validation.infDateArriveePrelevement", errs.getFieldError().getCode());
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites sup
      e.setPrelevement(null);
      dateStock.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2209 12:57:01"));
      e.setDateStock(dateStock);
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertEquals("date.validation.supDateActuelle", errs.getFieldError().getCode());

      // derives enfant
      e = echantillonManager.findByIdManager(1);
      e.setPrelevement(null);
      dateStock.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/03/2009 12:57:01"));
      e.setDateStock(dateStock);

      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertEquals("date.validation.supDateTransfoEnfant", errs.getFieldError().getCode());

      // null validation
      e.setDateStock(null);
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertTrue(errs.getAllErrors().size() == 0);
   }

   @Test
   public void testSteriliteCoherence(){
      //Echantillon e = echantillonManager.findByIdManager(1);
      Echantillon e = new Echantillon();
      e.setSterile(null);

      // null sterilite
      Errors errs = echantillonValidator.checkSteriliteAntecedence(e);
      assertTrue(errs.getAllErrors().size() == 0);

      // false sterilite
      e.setSterile(false);
      errs = echantillonValidator.checkSteriliteAntecedence(e);
      assertTrue(errs.getAllErrors().size() == 0);

      e.setSterile(true);

      // test sans prelevement
      e.setPrelevement(null);
      errs = echantillonValidator.checkSteriliteAntecedence(e);
      assertTrue(errs.getAllErrors().size() == 0);

      // test avec un prelevement non sterile
      final Prelevement p = new Prelevement();
      e.setPrelevement(p);
      p.setSterile(null);
      errs = echantillonValidator.checkSteriliteAntecedence(e);
      assertEquals("sterile.validation.prelevementNotSterile", errs.getFieldError().getCode());
      p.setSterile(false);
      errs = echantillonValidator.checkSteriliteAntecedence(e);
      assertEquals("sterile.validation.prelevementNotSterile", errs.getFieldError().getCode());

      p.setSterile(true);
      errs = echantillonValidator.checkSteriliteAntecedence(e);
      assertTrue(errs.getAllErrors().size() == 0);

      // test avec un labo precedent non sterile
      final Set<LaboInter> set = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      set.add(l1);
      l1.setSterile(true);
      final LaboInter l3 = new LaboInter();
      l3.setOrdre(3);
      set.add(l3);
      l3.setSterile(false);
      p.setLaboInters(set);
      errs = echantillonValidator.checkSteriliteAntecedence(e);
      assertEquals("sterile.validation.previousNotSterile", errs.getFieldError().getCode());

      l3.setSterile(false);
      errs = echantillonValidator.checkSteriliteAntecedence(e);
      assertEquals("sterile.validation.previousNotSterile", errs.getFieldError().getCode());

      l3.setSterile(true);
      errs = echantillonValidator.checkSteriliteAntecedence(e);
      assertTrue(errs.getAllErrors().size() == 0);

      // teste avec un retour non sterile
      e = echantillonManager.findByIdManager(1);
      e.setPrelevement(null);
      e.setSterile(true);
      errs = echantillonValidator.checkSteriliteAntecedence(e);
      assertEquals("sterile.validation.retourNotSterile", errs.getFieldError().getCode());

      // teste avec un retour sterile
      e = echantillonManager.findByIdManager(4);
      e.setPrelevement(null);
      e.setSterile(true);
      errs = echantillonValidator.checkSteriliteAntecedence(e);
      assertTrue(errs.getAllErrors().size() == 0);

   }

   @Test
   public void testCreateAndUpdateCodesAssignes(){
      // créé un échantillon
      final Banque banque = banqueManager.findByIdManager(1);
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      List<CodeAssigne> codes = new ArrayList<>();
      final CodeAssigne cOrg1 = new CodeAssigne();
      cOrg1.setCode("BL");
      cOrg1.setLibelle("langue");
      cOrg1.setIsOrgane(true);
      cOrg1.setCodeRefId(55);
      cOrg1.setTableCodage(tableCodageDao.findById(1));
      cOrg1.setOrdre(1);
      cOrg1.setExport(true);
      CodeAssigne cOrg2 = new CodeAssigne();
      cOrg2.setCode("codePerso");
      cOrg2.setLibelle("languedeboeuf");
      cOrg2.setIsOrgane(true);
      cOrg2.setOrdre(2);
      cOrg2.setExport(true);
      codes.add(cOrg1);
      codes.add(cOrg2);

      // Insertion
      final Echantillon echan1 = new Echantillon();
      echan1.setObjetStatut(objetStatutManager.findByIdManager(4));
      echan1.setCode("CodeWithAssigne");
      // erreur validation sur CodeOrganeExport null alors que orgs.size > 0
      boolean catchedInsert = false;
      try{
         echantillonManager.createObjectManager(echan1, banque, null, null, null, null, type, codes, null, null, null, null,
            null, utilisateur, true, null, false);
      }catch(final RuntimeException e){
         // e.printStackTrace();
         assertTrue(e.getMessage().equals("echantillon" + ".codesAssigne.organe.exportNbIllegal"));
         catchedInsert = true;
      }
      assertTrue(catchedInsert);
      //		//echan1.setEchantillonId(null);
      //		catchedInsert = false;
      //		try {
      //			echantillonManager.createObjectManager(echan1, banque, 
      //					null, null, null, null, type, null, les, null, 
      //					null, null, null, null, null, null, null, null, 
      //												utilisateur, true, null);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"RequiredObjectIsNullException")) {
      //				catchedInsert = true;
      //			}
      //		}
      //		assertTrue(catchedInsert);
      // erreur validation
      cOrg2.setCode("#¤¤*ù&&");
      cOrg2.setExport(false);
      catchedInsert = false;
      try{
         echantillonManager.createObjectManager(echan1, banque, null, null, null, null, type, codes, null, null, null, null,
            null, utilisateur, true, null, false);
      }catch(final ValidationException e){
         catchedInsert = true;
      }
      assertTrue(catchedInsert);
      //echan1.setEchantillonId(null);
      cOrg2.setCode("codePerso");
      echantillonManager.createObjectManager(echan1, banque, null, null, null, null, type, codes, null, null, null, null,
         null, utilisateur, true, null, false);
      assertTrue(getOperationManager().findByObjectManager(echan1).size() == 1);
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(echan1).size() == 2);
      codeAssigneManager.findCodesOrganeByEchantillonManager(echan1).get(0).getCode().equals("BL");
      codeAssigneManager.findCodesOrganeByEchantillonManager(echan1).get(1).getCode().equals("codePerso");
      final Echantillon testInsert = echantillonManager.findByCodeLikeManager("CodeWithAssigne", true).get(0);

      codes.clear();
      final CodeAssigne cLes1 = new CodeAssigne();
      cLes1.setCode("K14.5");
      cLes1.setLibelle("langue plicaturée");
      cLes1.setIsOrgane(false);
      cLes1.setIsMorpho(true);
      cLes1.setCodeRefId(4630);
      cLes1.setTableCodage(tableCodageDao.findById(2));
      cLes1.setOrdre(1);
      cLes1.setExport(true);
      codes.add(cLes1);

      final List<CodeAssigne> codesToD = new ArrayList<>();
      codesToD.add(codeAssigneManager.findCodesOrganeByEchantillonManager(echan1).get(0));

      echantillonManager.updateObjectManager(testInsert, banque, null, null, null, null, type, codes, codesToD, null, null,
         null, null, null, null, null, utilisateur, true, null, null);

      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(echan1).size() == 1);
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(echan1).size() == 1);

      //		assertTrue(testInsert
      //						.getCodeLesExport().getCode().equals(cLes1.getCode()));
      //		assertTrue(testInsert
      //					.getCodeOrganeExport().getCode().equals(cOrg2.getCode()));

      // update codes apres insert echantillon
      final Echantillon echan2 = new Echantillon();
      echan2.setCode("CodeWithAssigne2");
      echantillonManager.createObjectManager(echan2, banque, null, null, objetStatutManager.findByIdManager(4), null, type,
         null, null, null, null, null, null, utilisateur, true, null, false);

      final List<CodeAssigne> codes2 = new ArrayList<>();
      final CodeAssigne cOrg3 = new CodeAssigne();
      cOrg3.setCode("BL2");
      cOrg3.setLibelle("langue");
      cOrg3.setIsOrgane(true);
      cOrg3.setCodeRefId(55);
      cOrg3.setTableCodage(tableCodageDao.findById(1));
      cOrg3.setOrdre(1);
      codes2.add(cOrg3);
      final CodeAssigne cLes3 = new CodeAssigne();
      cLes3.setCode("K14.5");
      cLes3.setLibelle("langue plicaturée");
      cLes3.setIsOrgane(false);
      cLes3.setIsMorpho(true);
      cLes3.setCodeRefId(4630);
      cLes3.setOrdre(1);
      cLes3.setTableCodage(tableCodageDao.findById(2));
      codes2.add(cLes3);
      try{
         echantillonManager.updateObjectManager(echan2, banque, null, null, null, null, type, codes2, null, null, null,
            null, null, null, null, null, utilisateur, true, null, null);
      }catch(final RuntimeException e){
         assertTrue(e.getMessage().equals("echantillon" + ".codesAssigne.organe.exportNbIllegal"));
         catchedInsert = true;
      }
      assertTrue(catchedInsert);
      catchedInsert = false;
      cOrg3.setExport(true);
      try{
         echantillonManager.updateObjectManager(echan2, banque, null, null, null, null, type, codes2, null, null, null,
            null, null, null, null, null, utilisateur, true, null, null);
      }catch(final RuntimeException e){
         assertTrue(e.getMessage().equals("echantillon" + ".codesAssigne.morpho.exportNbIllegal"));
         catchedInsert = true;
      }
      assertTrue(catchedInsert);
      cLes3.setExport(true);
      echantillonManager.updateObjectManager(echan2, banque, null, null, null, null, type, codes2, null, null, null, null,
         null, null, null, null, utilisateur, true, null, null);
      assertTrue(getOperationManager().findByObjectManager(echan2).size() == 2);
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(echan2).size() == 1);
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(echan2).size() == 1);
      codeAssigneManager.findCodesOrganeByEchantillonManager(echan2).get(0).getCode().equals("BL2");
      //		Echantillon testInsert2 = echantillonManager
      //					.findByCodeLikeManager("CodeWithAssigne2", true).get(0);
      //		assertTrue(testInsert2.getCodeLesExport()
      //									.getCode().equals(cLes3.getCode()));
      //		assertTrue(testInsert2.getCodeOrganeExport()
      //										.getCode().equals(cOrg3.getCode()));

      // modification des champs assignes
      codes = new ArrayList<>();
      cOrg1.setExport(false);
      cOrg2 = codeAssigneManager.findByCodeLikeManager("codePerso", true).get(0);
      cOrg2.setCode("newCode");
      cOrg2.setOrdre(3);
      final CodeAssigne cOrg4 = new CodeAssigne();
      cOrg4.setCode("encoreUnCode");
      cOrg4.setLibelle("languedechat");
      cOrg4.setIsOrgane(true);
      cOrg4.setOrdre(2);
      cOrg4.setExport(true);
      codes.add(cOrg1);
      codes.add(cOrg2);
      codes.add(cOrg4);
      codes.addAll(codeAssigneManager.findCodesMorphoByEchantillonManager(testInsert));
      final CodeAssigne cLes2 = new CodeAssigne();
      cLes2.setCode("12.12");
      cLes2.setLibelle("langue noire");
      cLes2.setIsOrgane(false);
      cLes2.setIsMorpho(true);
      cLes2.setOrdre(2);
      codes.add(cLes2);
      echantillonManager.updateObjectManager(testInsert, banque, null, null, null, null, type, codes, null, null, null,
         null, null, null, null, null, utilisateur, false, null, null);

      assertTrue(getOperationManager().findByObjectManager(testInsert).size() == 3);
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(testInsert).size() == 2);
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(testInsert).get(0).getCode().equals("K14.5"));
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(testInsert).get(1).getCode().equals("12.12"));
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(testInsert).size() == 3);
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(testInsert).get(0).getCode().equals("BL"));
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(testInsert).get(1).getCode().equals("encoreUnCode"));
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(testInsert).get(2).getCode().equals("newCode"));

      final Echantillon testUpdate = echantillonManager.findByCodeLikeManager("CodeWithAssigne", true).get(0);

      //		assertTrue(testUpdate.getCodeOrganeExport()
      //										.getCode().equals(cOrg2.getCode()));
      //		assertTrue(testUpdate.getCodeLesExport()
      //										.getCode().equals(cLes1.getCode()));

      // modif que les exports
      //		cOrg1 = codeAssigneManager
      //						.findCodesOrganeByEchantillonManager(testInsert).get(0);
      //		cLes2 = codeAssigneManager
      //				.findCodesMorphoByEchantillonManager(testInsert)
      //					.get(codeAssigneManager
      //						.findCodesMorphoByEchantillonManager(testInsert)
      //																.size() - 1);
      //		
      //		echantillonManager.updateObjectManager(testInsert, banque, 
      //				null, null, null, null, type, null, null, null, null,
      //				null, null, null, null, cOrg1, cLes2, null, null, 
      //													utilisateur, false, 
      //													null, null);
      //		testUpdate = echantillonManager
      //				.findByCodeLikeManager("CodeWithAssigne", true).get(0);		
      //		
      //		assertTrue(testUpdate.getCodeOrganeExport()
      //										.getCode().equals("BL"));
      //		assertTrue(testUpdate.getCodeLesExport()
      //										.getCode().equals("12.12"));
      //		
      // delete tous les codes d'un echantillon
      final List<CodeAssigne> les = new ArrayList<>();
      les.addAll(codeAssigneManager.findCodesMorphoByEchantillonManager(testUpdate));
      for(int j = 0; j < les.size(); j++){
         codeAssigneManager.removeObjectManager(les.get(j));
      }
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(testUpdate).isEmpty());

      // clean up
      echantillonManager.removeObjectManager(testUpdate, null, utilisateur, null);
      echantillonManager.removeObjectManager(echan2, null, utilisateur, null);
      testFindAll();
      assertTrue(codeAssigneManager.findAllObjectsManager().size() == 5);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(echan2);
      fs.add(testInsert);
      cleanUpFantomes(fs);
   }

   //	/**
   //	 * teste le cas ou le code exporté est deleté et donc l'export
   //	 * est passé à un autre code.
   //	 */
   //	@Test
   //  public void testDeleteCodeExport() {
   //		// créé un échantillon
   //		Banque banque = banqueManager.findByIdManager(1);
   //		EchantillonType type = echantillonTypeManager.findByIdManager(1);
   //		Utilisateur utilisateur = utilisateurDao.findById(1);
   //		
   //		List<CodeAssigne> orgs = new ArrayList<CodeAssigne>();
   //		CodeAssigne cOrg1 = new CodeAssigne();
   //		cOrg1.setCode("BL");
   //		cOrg1.setLibelle("langue");
   //		cOrg1.setIsOrgane(true);
   //		cOrg1.setCodeRefId(55);
   //		cOrg1.setTableCodage(tableCodageDao.findById(1));
   //		cOrg1.setOrdre(1);
   //		CodeAssigne cOrg2 = new CodeAssigne();
   //		cOrg2.setCode("codePerso");
   //		cOrg2.setLibelle("languedeboeuf");
   //		cOrg2.setIsOrgane(true);
   //		cOrg2.setOrdre(2);
   //		orgs.add(cOrg1); orgs.add(cOrg2);
   //		
   //		List<CodeAssigne> morphs = new ArrayList<CodeAssigne>();
   //		CodeAssigne cM1 = new CodeAssigne();
   //		cM1.setCode("Morpho1");
   //		cM1.setLibelle("langue noire");
   //		cM1.setIsOrgane(false);
   //		cM1.setIsMorpho(true);
   //		cM1.setOrdre(1);
   //		CodeAssigne cM2 = new CodeAssigne();
   //		cM2.setCode("Paul");
   //		cM2.setLibelle("langue de poulpe");
   //		cM2.setIsOrgane(false);
   //		cM2.setIsMorpho(true);
   //		cM2.setOrdre(2);
   //		morphs.add(cM1); morphs.add(cM2);
   //		
   //		
   //		// Insertion
   //		Echantillon echan1 = new Echantillon();
   //		echan1.setCode("CodeWithAssigne");
   //		echantillonManager.createObjectManager(echan1, banque, 
   //				null, null, null, null, type, orgs, morphs, null, 
   //				null, null, null, null, null, cOrg1, cM2, null, 
   //											utilisateur, true, null);
   //		assertTrue(codeAssigneManager
   //				.findCodesOrganeByEchantillonManager(echan1).size() == 2);
   //		assertTrue(codeAssigneManager
   //				.findCodesMorphoByEchantillonManager(echan1).size() == 2);
   //		Echantillon testInsert = echantillonManager
   //						.findByCodeLikeManager("CodeWithAssigne", true).get(0);
   //		
   //		//testInsert = echantillonManager
   //		//.findByCodeLikeManager("CodeWithAssigne", true).get(0);
   //		
   //		// passage export au code restant
   //		echantillonManager.updateObjectManager(testInsert, banque, 
   //				null, null, null, null, type, null, null, null, null,
   //				null, null, null, null, null, cM1, null, null, 
   //													utilisateur, true, 
   //													null, null);
   //		
   //		// suppression code exporte
   //		cOrg1 = codeAssigneManager
   //					.findCodesOrganeByEchantillonManager(testInsert).get(0);
   //		codeAssigneManager
   //			.removeObjectManager(codeAssigneManager
   //				.findCodesOrganeByEchantillonManager(testInsert).get(0));
   //		codeAssigneManager
   //			.removeObjectManager(codeAssigneManager
   //				.findCodesOrganeByEchantillonManager(testInsert).get(0));
   //		cM2 = codeAssigneManager
   //					.findCodesMorphoByEchantillonManager(testInsert).get(1);
   //		codeAssigneManager.removeObjectManager(cM2);
   //		
   //		assertTrue(codeAssigneManager
   //				.findCodesOrganeByEchantillonManager(testInsert).size() == 0);
   //		assertTrue(testInsert.getCodeOrganeExport() == null);
   //	
   //		// clean up
   //		echantillonManager.removeObjectManager(testInsert, "suppr", 
   //															utilisateur);
   //		testFindAll();
   //		assertTrue(codeAssigneManager.findAllObjectsManager().size() == 5);
   //		
   //		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
   //		fs.add(testInsert);
   //		cleanUpFantomes(fs);
   //	}

   @Test
   public void testCreateUpdateEchantillonWithAnnots(){

      // créé un échantillon
      final Banque b1 = banqueManager.findByIdManager(1);
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      // on insert 1 nouveau echan pour les tests
      final Echantillon echan = new Echantillon();
      echan.setCode("EchanAnnotated");

      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final List<AnnotationValeur> listDelete = new ArrayList<>();

      final ChampAnnotation c = champAnnotationDao.findById(7);

      final AnnotationValeur num = new AnnotationValeur();
      num.setChampAnnotation(c);
      num.setAlphanum("123456");
      num.setBanque(b1);
      listAnnots.add(num);

      echan.setObjetStatut(objetStatutManager.findByIdManager(4));

      // teste erreur sur annots -> rollback
      num.setAlphanum("&é**$$$");
      boolean catched = false;
      try{
         echantillonManager.createObjectManager(echan, b1, null, null, null, null, type, null, null, null, null, listAnnots,
            null, utilisateur, false, null, false);
      }catch(final ValidationException ve){
         catched = true;
      }
      assertTrue(catched);

      // verification de l'etat de la base
      testFindAll();

      num.setAlphanum("123456");

      echantillonManager.createObjectManager(echan, b1, null, null, null, null, type, null, null, null, null, listAnnots,
         null, utilisateur, false, "/tmp/", false);

      assertTrue(echantillonManager.findByCodeLikeManager("EchanAnnotated", true).size() == 1);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, echan).get(0).getAlphanum().equals("123456"));
      assertTrue(getOperationManager().findByObjectManager(echan).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(echan).get(1).getOperationType().getNom().equals("Annotation"));

      // on teste une deletion puis insertion nouvelle valeur
      listDelete.add(annotationValeurManager.findByChampAndObjetManager(c, echan).get(0));
      listAnnots.clear();
      final AnnotationValeur num2 = new AnnotationValeur();
      num2.setChampAnnotation(c);
      num2.setAlphanum("789");
      num2.setBanque(b1);
      listAnnots.add(num2);
      final AnnotationValeur bool1 = new AnnotationValeur();
      final ChampAnnotation c2 = champAnnotationDao.findById(4);
      bool1.setChampAnnotation(c2);
      bool1.setBool(true);
      bool1.setBanque(b1);
      listAnnots.add(bool1);

      echan.setTumoral(true);

      echantillonManager.updateObjectManager(echan, b1, null, null, null, null, type, null, null, null, null, null,
         listAnnots, listDelete, null, null, utilisateur, false, null, "/tmp/");

      assertTrue(echantillonManager.findByCodeLikeManager("EchanAnnotated", true).size() == 1);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, echan).size() == 1);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, echan).get(0).getAlphanum().equals("789"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c2, echan).get(0).getBool());
      assertTrue(getOperationManager().findByObjectManager(echan).size() == 4);
      assertTrue(getOperationManager().findByObjectManager(echan).get(3).getOperationType().getNom().equals("Annotation"));

      // suppression annots
      listDelete.add(annotationValeurManager.findByChampAndObjetManager(c, echan).get(0));
      //		listDelete.add(annotationValeurManager
      //				.findByChampAndObjetManager(c2, echan).get(0));
      listAnnots.clear();

      echantillonManager.updateObjectManager(echan, b1, null, null, null, null, type, null, null, null, null, null,
         listAnnots, listDelete, null, null, utilisateur, false, null, "/tmp/");

      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, echan).size() == 0);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c2, echan).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(echan).size() == 6);
      assertTrue(getOperationManager().findByObjectManager(echan).get(5).getOperationType().getNom().equals("Annotation"));

      // Nettoyage
      echantillonManager.removeObjectManager(echan, null, utilisateur, null);

      assertTrue(getOperationManager().findByObjectManager(echan).size() == 0);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);
      testFindAll();

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(echan);
      cleanUpFantomes(fs);
   }

   @Test
   public void testCreateUpdateEchantillonCrAnapath(){
      // créé un échantillon
      final Banque b1 = banqueManager.findByIdManager(1);
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);
      final Echantillon echan = new Echantillon();
      echan.setObjetStatut(objetStatutManager.findByIdManager(4));

      final Integer totFichiers = fichierManager.findAllObjectsManager().size();

      // creation architecture fichiers
      new File("/tmp/" + "pt_" + b1.getPlateforme().getPlateformeId() + "/" + "coll_" + b1.getBanqueId() + "/cr_anapath/")
         .mkdirs();

      final Fichier cr = new Fichier();
      cr.setNom("anapath1");
      String tmp = "Echantillon diagnostic Veronica virus";
      byte[] byteArray = tmp.getBytes();
      ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      // exception
      echan.setCode("*ù$&àé");
      try{
         echantillonManager.createObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null, cr,
            bais, filesCreated, null, utilisateur, true, "/tmp/", false);
      }catch(final Exception e){
         testFindAll();
         assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers);
         assertEquals(0, new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length);
      }
      assertTrue(filesCreated.isEmpty());

      // exception cause basedir incorrecte
      echan.setCode("EchanAnapath");
      try{
         echantillonManager.createObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null, cr,
            bais, filesCreated, null, utilisateur, false, "**/mm", false);
      }catch(final Exception e){
         // e.printStackTrace();
         assertTrue(e.getMessage().equals("error.filesystem.access"));
         testFindAll();
         assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers);
         assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 0);
      }
      assertTrue(filesCreated.isEmpty());

      // creation valide
      echantillonManager.createObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null, cr,
         bais, null, null, utilisateur, false, "/tmp/", false);

      assertTrue(echantillonManager.findByCodeLikeManager("EchanAnapath", true).size() == 1);
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers + 1);
      assertTrue(new File(cr.getPath()).length() == 37);
      assertTrue(filesCreated.size() == 0); // car list filesToCreate passée à null

      filesCreated.clear();

      // modification du nom fichier
      final Fichier cr2 = cr.clone();
      cr2.setNom("cr2");
      echantillonManager.updateObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null, null,
         cr2, null, filesCreated, filesToDelete, null, null, utilisateur, false, null, "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers + 1);
      assertTrue(fichierManager.findByIdManager(cr2.getFichierId()).getNom().equals("cr2"));
      assertTrue(new File(cr2.getPath()).length() == 37);

      // update exception
      final Fichier cr2ex = cr2.clone();
      cr2ex.setNom("*ù$ù##");
      echan.setLateralite("B");
      try{
         echantillonManager.updateObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null,
            null, cr2ex, null, filesCreated, filesToDelete, null, null, utilisateur, false, null, "/tmp/");
      }catch(final Exception e){
         assertNull(echantillonManager.findByCodeLikeManager("EchanAnapath", true).get(0).getLateralite());
         assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers + 1);
         assertTrue(echantillonManager.findByCodeLikeManager("EchanAnapath", true).get(0).getCrAnapath().getNom().equals("cr2"));
         assertTrue(new File(cr2.getPath()).length() == 37);
      }
      echan.setLateralite(null);

      // modification du contenu
      final Fichier cr4 = cr2.clone();
      tmp = "Echantillon diagnostic T virus";
      byteArray = tmp.getBytes();
      bais = new ByteArrayInputStream(byteArray);

      echantillonManager.updateObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null, null,
         cr4, bais, filesCreated, filesToDelete, null, null, utilisateur, false, null, "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers + 1);
      assertTrue(new File(echan.getCrAnapath().getPath()).length() == 30);
      assertTrue(filesCreated.size() == 1);
      assertTrue(filesToDelete.size() == 1);

      filesCreated.clear();
      filesToDelete.clear();

      // nullifying
      echantillonManager.updateObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null, null,
         null, null, filesCreated, filesToDelete, null, null, utilisateur, false, null, "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers);
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 0);
      assertTrue(filesToDelete.size() == 1);
      assertTrue(filesCreated.size() == 0);

      // update creation nouveau fichier exception test rollback
      final Fichier cr3 = new Fichier();
      cr3.setNom("fin");
      tmp = "Les tenebres n'ont pas de fin";
      byteArray = tmp.getBytes();
      bais = new ByteArrayInputStream(byteArray);

      echan.setCode("*ù$ù%%##&é-");
      try{
         echantillonManager.updateObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null,
            null, cr3, bais, filesCreated, filesToDelete, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers);
         assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 0);
      }
      assertTrue(filesToDelete.size() == 1);
      assertTrue(filesCreated.size() == 0);

      // update nouveau fichier ok
      echan.setCode("ok");
      echantillonManager.updateObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null, null,
         cr3, bais, filesCreated, filesToDelete, null, null, utilisateur, true, null, "/tmp/");
      assertTrue(echantillonManager.findByCodeLikeManager("ok", true).size() == 1);
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers + 1);
      assertTrue(new File(cr3.getPath()).length() == 29);
      assertTrue(filesToDelete.size() == 1);
      assertTrue(filesCreated.size() == 1);

      filesToDelete.clear();
      filesCreated.clear();

      // zero modif
      echantillonManager.updateObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null, null,
         cr3, null, filesCreated, filesToDelete, null, null, utilisateur, false, null, "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers + 1);
      assertTrue(new File(cr3.getPath()).length() == 29);
      assertTrue(filesToDelete.size() == 0);
      assertTrue(filesCreated.size() == 0);

      // update stream rollback
      cr3.setNom("rollback");
      tmp = "rollback";
      byteArray = tmp.getBytes();
      bais = new ByteArrayInputStream(byteArray);

      echan.setCode("*ù$ù%%##&é-");
      try{
         echantillonManager.updateObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null,
            null, cr3, bais, filesCreated, filesToDelete, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers + 1);
         assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);
         assertTrue(cr3.getPath().equals("/tmp/pt_1/coll_1/cr_anapath/fin_" + cr3.getFichierId()));
         assertTrue(new File(cr3.getPath()).length() == 29);
      }

      final Integer c3Id = cr3.getFichierId();

      // update valide
      echan.setCode("ok");
      echantillonManager.updateObjectWithCrAnapathManager(echan, b1, null, null, null, null, type, null, null, null, null, null,
         cr3, bais, filesCreated, filesToDelete, null, null, utilisateur, true, null, "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers + 1);
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);
      assertTrue(echan.getCrAnapath().getFichierId() > c3Id);
      assertTrue(echan.getCrAnapath().getPath().equals("/tmp/pt_1/coll_1/cr_anapath/fin_" + echan.getCrAnapath().getFichierId()));
      assertTrue(new File(echan.getCrAnapath().getPath()).length() == 8);

      // Nettoyage
      echantillonManager.removeObjectManager(echan, null, utilisateur, null);

      assertTrue(getOperationManager().findByObjectManager(echan).size() == 0);
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichiers);
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 0);
      testFindAll();

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(echan);
      cleanUpFantomes(fs);
   }

   /**
    * Teste les modifications suppressions sur un CR anapath qui est référencé
    * par plusieurs échantillons.
    */
   @Test
   public void testCreateUpdateCrAnapathForEchantillons(){

      // créé 6 échantillons
      final Banque b1 = banqueManager.findByIdManager(1);
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);
      final ObjetStatut statut = objetStatutManager.findByIdManager(4);

      final Integer totFichier = fichierManager.findAllObjectsManager().size();

      final Echantillon e1 = new Echantillon();
      e1.setCode("E1");
      final Echantillon e2 = new Echantillon();
      e2.setCode("E2");
      final Echantillon e3 = new Echantillon();
      e3.setCode("E3");
      final Echantillon e4 = new Echantillon();
      e4.setCode("E4");
      final Echantillon e5 = new Echantillon();
      e5.setCode("E5");

      // creation architecture fichiers
      new File("/tmp/" + "pt_" + b1.getPlateforme().getPlateformeId() + "/" + "coll_" + b1.getBanqueId() + "/cr_anapath/")
         .mkdirs();
      // test
      final String path = echantillonManager.writeCrAnapathFilePath("/tmp/", b1, null);
      assertNotNull(path);

      final Fichier cr = new Fichier();
      cr.setNom("crPour5");
      String tmp = "Come on baby light my fire";
      byte[] byteArray = tmp.getBytes();
      ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      echantillonManager.createObjectWithCrAnapathManager(e1, b1, null, null, statut, null, type, null, null, null, null, cr,
         bais, filesCreated, null, utilisateur, false, "/tmp/", false);

      final Fichier cr2 = cr.cloneNoId();
      echantillonManager.createObjectWithCrAnapathManager(e2, b1, null, null, statut, null, type, null, null, null, null, cr2,
         null, filesCreated, null, utilisateur, false, "/tmp/", false);
      Fichier cr3 = cr.cloneNoId();
      echantillonManager.createObjectWithCrAnapathManager(e3, b1, null, null, statut, null, type, null, null, null, null, cr3,
         null, filesCreated, null, utilisateur, false, "/tmp/", false);
      final Fichier cr4 = cr.cloneNoId();
      echantillonManager.createObjectWithCrAnapathManager(e4, b1, null, null, statut, null, type, null, null, null, null, cr4,
         null, filesCreated, null, utilisateur, false, "/tmp/", false);
      final Fichier cr5 = cr.cloneNoId();
      echantillonManager.createObjectWithCrAnapathManager(e5, b1, null, null, statut, null, type, null, null, null, null, cr5,
         null, filesCreated, null, utilisateur, false, "/tmp/", false);

      assertTrue(echantillonManager.findAllObjectsManager().size() == 9);
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 5);
      assertTrue(fichierManager.isPathSharedManager(cr));
      assertTrue(new File(cr.getPath()).length() == 26);
      assertEquals(1, new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length);
      assertTrue(filesCreated.size() == 1);
      assertTrue(filesToDelete.size() == 0);

      filesCreated.clear();

      // modification du nom fichier -> exception pour e1
      e1.setCode("&*ù*ù$");
      cr.setNom("cr2");
      try{
         echantillonManager.updateObjectWithCrAnapathManager(e1, b1, null, null, null, null, type, null, null, null, null, null,
            cr, null, filesCreated, filesToDelete, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         assertEquals(totFichier + 5, fichierManager.findAllObjectsManager().size());
         assertTrue(
            echantillonManager.findByCodeLikeManager("E1", true).get(0).getCrAnapath().getFichierId().equals(cr.getFichierId()));
         assertFalse(echantillonManager.findByCodeLikeManager("E1", true).get(0).getCrAnapath().getNom().equals("cr2"));
         assertTrue(new File(cr2.getPath()).length() == 26);
         assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);
      }
      assertTrue(filesCreated.size() == 0);
      assertTrue(filesToDelete.size() == 0);

      // modification du nom fichier pour e1
      e1.setCode("ok");
      echantillonManager.updateObjectWithCrAnapathManager(e1, b1, null, null, null, null, type, null, null, null, null, null, cr,
         null, filesCreated, filesToDelete, null, null, utilisateur, false, null, "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 5);
      Fichier f = echantillonManager.findByCodeLikeManager("ok", true).get(0).getCrAnapath();
      assertTrue(f.getNom().equals("cr2"));
      assertTrue(f.getPath().equals(cr.getPath()));
      assertTrue(new File(f.getPath()).length() == 26);
      assertTrue(e2.getCrAnapath().getNom().equals("crPour5"));
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);
      assertTrue(filesCreated.size() == 0);
      assertTrue(filesToDelete.size() == 0);

      // modification du contenu pour e3
      cr3.setNom("cr3");
      // cr3.setPath(null);
      tmp = "Love me two times";
      byteArray = tmp.getBytes();
      bais = new ByteArrayInputStream(byteArray);

      // modification du contenu du fichier -> exception pour e1
      e3.setCode("&*ù*ù$");
      try{
         echantillonManager.updateObjectWithCrAnapathManager(e3, b1, null, null, null, null, type, null, null, null, null, null,
            cr3, bais, filesCreated, filesToDelete, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 5);
         assertTrue(
            echantillonManager.findByCodeLikeManager("E3", true).get(0).getCrAnapath().getFichierId().equals(cr3.getFichierId()));
         assertTrue(echantillonManager.findByCodeLikeManager("E3", true).get(0).getCrAnapath().getNom().equals(cr2.getNom()));
         assertTrue(
            new File(echantillonManager.findByCodeLikeManager("E3", true).get(0).getCrAnapath().getPath()).length() == 26);
         assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);
      }
      assertTrue(filesCreated.size() == 0);
      assertTrue(filesToDelete.size() == 0);

      // ok 
      cr3 = e3.getCrAnapath();
      e3.setCode("ok2");
      echantillonManager.updateObjectWithCrAnapathManager(e3, b1, null, null, null, null, type, null, null, null, null, null, cr3,
         bais, filesCreated, filesToDelete, null, null, utilisateur, false, null, "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 5);
      f = echantillonManager.findByCodeLikeManager("ok2", true).get(0).getCrAnapath();
      assertTrue(new File(f.getPath()).length() == 17);
      assertTrue(new File(e4.getCrAnapath().getPath()).length() == 26);
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 2);
      assertTrue(filesCreated.size() == 1);
      assertTrue(filesToDelete.size() == 0); // car le fichier est encore reference 

      filesCreated.clear();
      filesToDelete.clear();

      // zero modif
      echantillonManager.updateObjectWithCrAnapathManager(e3, b1, null, null, null, null, type, null, null, null, null, null,
         e3.getCrAnapath(), null, filesCreated, filesToDelete, null, null, utilisateur, false, null, "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 5);
      f = echantillonManager.findByCodeLikeManager("ok2", true).get(0).getCrAnapath();
      assertTrue(new File(f.getPath()).length() == 17);
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 2);
      assertTrue(filesCreated.size() == 0);
      assertTrue(filesToDelete.size() == 0);

      // nullifying pour e4
      echantillonManager.updateObjectWithCrAnapathManager(e4, b1, null, null, null, null, type, null, null, null, null, null,
         null, null, filesCreated, filesToDelete, null, null, utilisateur, false, null, "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 4);
      assertNull(echantillonManager.findByCodeLikeManager("e4", true).get(0).getCrAnapath());
      assertTrue(new File(e5.getCrAnapath().getPath()).length() == 26);
      assertTrue(new File(e3.getCrAnapath().getPath()).length() == 17);
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 2);
      assertTrue(filesCreated.size() == 0);
      assertTrue(filesToDelete.size() == 0); // fichier encore reference donc non suppr.

      // suppression e5
      echantillonManager.removeObjectManager(e5, null, utilisateur, filesToDelete);
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 3);
      assertTrue(new File(e2.getCrAnapath().getPath()).length() == 26);
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 2);
      assertTrue(filesCreated.size() == 0);
      assertTrue(filesToDelete.size() == 0);

      // nullifying e3 doit entrainer suppression cr car plus partagé
      echantillonManager.updateObjectWithCrAnapathManager(e3, b1, null, null, null, null, type, null, null, null, null, null,
         null, null, filesCreated, filesToDelete, null, null, utilisateur, false, null, "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 2);
      assertNull(echantillonManager.findByCodeLikeManager("ok2", true).get(0).getCrAnapath());
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);
      assertTrue(filesCreated.size() == 0);
      assertTrue(filesToDelete.size() == 1);

      filesToDelete.clear();

      // Nettoyage
      echantillonManager.removeObjectManager(e1, null, utilisateur, filesToDelete);
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);
      // suppression du dernier fichier faisant reference
      echantillonManager.removeObjectManager(e2, null, utilisateur, null);
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 0);
      assertTrue(filesCreated.size() == 0);
      assertTrue(filesToDelete.size() == 0);
      echantillonManager.removeObjectManager(e3, null, utilisateur, filesToDelete);
      echantillonManager.removeObjectManager(e4, null, utilisateur, filesToDelete);

      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier);
      testFindAll();
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 0);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(e1);
      fs.add(e2);
      fs.add(e3);
      fs.add(e4);
      fs.add(e5);
      cleanUpFantomes(fs);

   }

   @Test
   public void testValidationCoherenceDate() throws ParseException{
      final Prelevement p = new Prelevement();
      final Echantillon e = new Echantillon();
      e.setPrelevement(p);

      final Calendar val1 = Calendar.getInstance();
      val1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1973"));
      final Calendar val2 = Calendar.getInstance();
      val2.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/1973 10:12:12"));

      p.setDatePrelevement(val1);
      e.setDateStock(val2);
      Errors errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertTrue(errs.getErrorCount() == 0);

      p.setDatePrelevement(val2);
      e.setDateStock(val1);
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertTrue(errs.getErrorCount() == 0);

      val2.setTime(new SimpleDateFormat("dd/MM/yyyy hh:MM:ss").parse("01/01/1974 10:12:12"));

      p.setDatePrelevement(val1);
      e.setDateStock(val2);
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertTrue(errs.getErrorCount() == 0);

      p.setDatePrelevement(val2);
      e.setDateStock(val1);
      errs = echantillonValidator.checkDateStockageCoherence(e, true);
      assertEquals("date.validation.infDatePrelevement", errs.getFieldError().getCode());
   }

   @Test
   public void testRemoveUsedAndCessedObject(){
      boolean catched = false;
      final Utilisateur u = utilisateurDao.findById(1);
      final Echantillon e4 = echantillonManager.findByIdManager(4);
      try{
         echantillonManager.removeObjectManager(e4, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("echantillon.deletion.isUsedCascade"));
         assertTrue(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);
      catched = false;

      final Echantillon e3 = echantillonManager.findByIdManager(3);
      try{
         echantillonManager.removeObjectManager(e3, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("echantillon.deletion.isUsedNonCascade"));
         assertFalse(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);
      catched = false;

      final Echantillon e1 = echantillonManager.findByIdManager(1);
      try{
         echantillonManager.removeObjectManager(e1, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("echantillon.deletion.isUsedNonCascade"));
         assertFalse(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);
   }

   @Test
   public void testCascadeEchantillonChildren(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Echantillon e = new Echantillon();
      /*Champs obligatoires*/
      final Banque b = banqueDao.findById(2);
      e.setCode("EchanWithDerives");
      final EchantillonType eType = echantillonTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);
      e.setObjetStatut(objetStatutManager.findByIdManager(4));

      echantillonManager.createObjectManager(e, b, null, null, null, null, eType, null, null, null, null, null, null,
         utilisateur, false, null, false);

      final Retour r = new Retour();
      /*Champs obligatoires*/
      r.setTempMoyenne(new Float(22.0));
      r.setDateSortie(Calendar.getInstance());
      r.setDateRetour(Calendar.getInstance());

      //insertion valide
      retourManager.createOrUpdateObjectManager(r, e, null, null, null, null, null, u, "creation");

      assertTrue(echantillonManager.findByCodeLikeManager("EchanWithDerives", true).size() == 1);
      assertTrue(retourManager.getRetoursForObjectManager(e).size() == 1);
      assertTrue(retourManager.findAllObjectsManager().size() == 9);

      final Transformation transfo1 = new Transformation();
      final Entite entite = entiteDao.findById(3);
      transfo1.setObjetId(e.getEchantillonId());
      transformationManager.createObjectManager(transfo1, entite, null);

      final List<ProdDerive> derives = new ArrayList<>();
      final ProdType type = prodTypeDao.findById(1);

      final ProdDerive derive1 = new ProdDerive();
      derive1.setCode("D1");
      derive1.setProdType(type);
      derives.add(derive1);
      final ProdDerive derive2 = new ProdDerive();
      derive2.setCode("D2");
      derive2.setProdType(type);
      derives.add(derive2);
      final ProdDerive derive3 = new ProdDerive();
      derive3.setCode("D3");
      derive3.setProdType(type);
      derives.add(derive3);

      prodDeriveManager.createDeriveListWithAnnotsManager(derives, b, transfo1, utilisateur, null, null, null, null);

      assertTrue(transformationManager.findAllDeriveFromParentManager(e).size() == 3);

      // delete cascade -> broken à cause d'une cession
      final Cession c = cessionDao.findById(1);
      final CederObjet ced2 = new CederObjet();
      ced2.setObjetId(derive3.getProdDeriveId());
      ced2.setQuantite((float) 10.0);
      final Unite qteUnite = uniteDao.findById(5);
      cederObjetManager.createObjectManager(ced2, c, entiteDao.findByNom("ProdDerive").get(0), qteUnite);

      boolean catched = false;
      try{
         echantillonManager.removeObjectCascadeManager(e, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("derive.cascade.isCessed"));
         assertFalse(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);

      assertTrue(transformationManager.findAllDeriveFromParentManager(e).size() == 3);

      // suppr ceder objet pour pouvoir cascader
      cederObjetManager.removeObjectManager(ced2);
      echantillonManager.removeObjectCascadeManager(e, "cascade!!!", u, null);

      testFindAll();
      assertTrue(transformationManager.findAllObjectsManager().size() == 5);
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
      assertTrue(retourManager.findAllObjectsManager().size() == 8);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(e);
      fs.addAll(derives);
      cleanUpFantomes(fs);
   }

   @Test
   public void testFindByPrelevementManager(){
      Prelevement p = prelevementDao.findById(1);
      List<Echantillon> echans = echantillonManager.findByPrelevementManager(p);
      assertTrue(echans.size() == 2);
      p = prelevementDao.findById(4);
      echans = echantillonManager.findByPrelevementManager(p);
      assertTrue(echans.size() == 0);
      echans = echantillonManager.findByPrelevementManager(null);
      assertTrue(echans.size() == 0);

   }

   /**
    * @version 2.1
    */
   @Test
   public void testSwitchBanque(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = banqueDao.findById(3);
      final Banque b4 = banqueDao.findById(4);
      // ne doit pas planter
      echantillonManager.switchBanqueCascadeManager(null, b3, false, u, null, null);
      Echantillon e = new Echantillon();
      e.setObjetStatut(objetStatutManager.findByIdManager(4));
      // ne doit pas planter getBanque() == null
      echantillonManager.switchBanqueCascadeManager(e, null, false, u, null, null);
      e.setCode("JEG.2");
      echantillonManager.createObjectManager(e, b3, null, null, null, null, echantillonTypeManager.findByIdManager(1), null, null,
         null, null, null, null, u, false, null, false);
      assertTrue(banqueManager.getEchantillonsManager(b3).size() == 1);
      e = echantillonManager.findByIdManager(e.getEchantillonId());

      // banque depart = banque arrivee
      echantillonManager.switchBanqueCascadeManager(e, b3, false, u, null, null);
      assertTrue(getOperationManager().findByObjectManager(e).size() == 1);

      // switch 3 -> 2		
      // doublon
      // @since 2.1 doublon code + PF 
      // switchBanque doit retourner doublon exception si déplacement entre deux pfs
      final Echantillon ebis = new Echantillon();
      ebis.setCode("JEG.2");
      ebis.setObjetStatut(objetStatutManager.findByIdManager(4));
      echantillonManager.createObjectManager(ebis, b4, null, null, null, null, echantillonTypeManager.findByIdManager(4), null,
         null, null, null, null, null, u, false, null, false);
      assertTrue(banqueManager.getEchantillonsManager(b4).size() == 1);

      boolean catched = false;
      try{
         echantillonManager.switchBanqueCascadeManager(ebis, b2, false, u, null, null);
      }catch(final DoublonFoundException dbe){
         catched = true;
      }
      assertTrue(catched);
      assertTrue(banqueManager.getEchantillonsManager(b3).size() == 1);
      assertTrue(banqueManager.getEchantillonsManager(b2).size() == 1);
      assertTrue(banqueManager.getEchantillonsManager(b4).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(e).size() == 1);

      e.setCode("switchEchan");
      echantillonManager.updateObjectManager(e, e.getBanque(), e.getPrelevement(), null, e.getObjetStatut(), null,
         e.getEchantillonType(), null, null, null, null, null, null, null, null, null, u, false, null, null);
      echantillonManager.switchBanqueCascadeManager(e, b2, false, u, null, null);
      // switched ok
      assertTrue(banqueManager.getEchantillonsManager(b3).isEmpty());
      assertTrue(banqueManager.getEchantillonsManager(b2).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(e).size() == 3);

      // prodDerive cascade 2 -> 3
      final Transformation transfo1 = new Transformation();
      transfo1.setObjetId(e.getEchantillonId());
      transformationManager.createObjectManager(transfo1, entiteDao.findById(3), null);
      ProdDerive derive = new ProdDerive();
      derive.setCode("deriveSwitch");
      prodDeriveManager.createObjectManager(derive, b2, prodTypeDao.findById(1), objetStatutManager.findByIdManager(4), null,
         null, null, null, null, null, null, transfo1, null, null, u, false, null, false);
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 2);
      echantillonManager.switchBanqueCascadeManager(e, b3, true, u, null, null);
      // switched ok
      assertTrue(banqueManager.getEchantillonsManager(b3).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);
      derive = prodDeriveManager.findByIdManager(derive.getProdDeriveId());

      // cession de l'echantillon -> erreur
      final Cession cession = cessionDao.findById(1);
      final Entite entite = entiteDao.findById(3);
      // Unite qteUnite = uniteDao.findById(5);
      // float qte = (float) 10.0;	
      final CederObjet ced1 = new CederObjet();
      ced1.setObjetId(e.getEchantillonId());
      cederObjetManager.createObjectManager(ced1, cession, entite, null);
      catched = false;
      try{
         echantillonManager.switchBanqueCascadeManager(e, b4, true, u, null, null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("echantillon.switchBanque.isCessed"));
      }
      assertTrue(catched);
      assertTrue(banqueManager.getEchantillonsManager(b3).size() == 1);
      assertTrue(banqueManager.getEchantillonsManager(b2).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(e).size() == 4);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);

      cederObjetManager.removeObjectManager(ced1);
      final CederObjet ced2 = new CederObjet();
      ced2.setObjetId(derive.getProdDeriveId());
      cederObjetManager.createObjectManager(ced2, cession, entiteDao.findById(8), null);
      catched = false;
      try{
         echantillonManager.switchBanqueCascadeManager(e, b4, true, u, null, null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("derive.switchBanque.isCessed"));
      }
      assertTrue(banqueManager.getEchantillonsManager(b3).size() == 1);
      assertTrue(banqueManager.getEchantillonsManager(b2).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(e).size() == 4);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);

      cederObjetManager.removeObjectManager(ced2);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
      catched = false;

      // emplacement erreur
      final Conteneur cbase = conteneurManager.findByIdManager(1);
      final Terminale t = terminaleDao.findById(1);
      final List<Banque> banques = new ArrayList<>();
      banques.add(b3);
      banques.add(b1);
      final EnceinteType eType1 = enceinteTypeDao.findById(6);
      final EnceinteType eType2 = enceinteTypeDao.findById(2);
      final EnceinteType eType3 = enceinteTypeDao.findById(1);
      final List<Enceinte> enceintes = new ArrayList<>();
      final Conteneur c = new Conteneur();
      c.setCode("CNew");
      c.setNom("Congélateur New");
      c.setTemp((float) -50);
      c.setNbrEnc(1);
      c.setNbrNiv(4);
      c.setConteneurType(cbase.getConteneurType());
      c.setArchive(false);
      c.setService(cbase.getService());
      final Enceinte e1 = new Enceinte();
      e1.setEnceinteType(eType1);
      e1.setNom("R");
      e1.setNbPlaces(1);
      enceintes.add(e1);
      final Enceinte e2 = new Enceinte();
      e2.setEnceinteType(eType2);
      e2.setNom("T");
      e2.setNbPlaces(1);
      enceintes.add(e2);
      final Enceinte e3 = new Enceinte();
      e3.setEnceinteType(eType3);
      e3.setNom("C");
      e3.setNbPlaces(1);
      enceintes.add(e3);
      final Terminale term = new Terminale();
      term.setTerminaleType(t.getTerminaleType());
      term.setNom("BT");
      term.setTerminaleNumerotation(t.getTerminaleNumerotation());
      final List<Integer> goodPositions = new ArrayList<>();
      goodPositions.add(10);
      goodPositions.add(null);
      goodPositions.add(25);
      goodPositions.add(null);
      conteneurManager.createAllArborescenceManager(c, enceintes, term, goodPositions, banques, null, null, false, u,
         b1.getPlateforme());
      Emplacement emp = new Emplacement();
      emp.setPosition(55);
      emp.setVide(false);
      emp.setObjetId(derive.getProdDeriveId());
      final Terminale t1 =
         enceinteManager
            .getTerminalesManager(
               enceinteManager
                  .getEnceintesManager(enceinteManager
                     .getEnceintesManager(conteneurManager.getEnceintesManager(c).iterator().next()).iterator().next())
                  .iterator().next())
            .iterator().next();
      emplacementManager.createObjectManager(emp, t1, entiteDao.findById(8));
      prodDeriveManager.updateObjectManager(derive, derive.getBanque(), derive.getProdType(), null, null, emp, null, null, null,
         null, null, derive.getTransformation(), null, null, null, null, u, false, null, null);

      try{
         echantillonManager.switchBanqueCascadeManager(e, b2, true, u, null, null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("derive.switchBanque.badBanqueStockage"));
      }
      assertTrue(catched);
      assertTrue(banqueManager.getEchantillonsManager(b3).size() == 1);
      assertTrue(banqueManager.getEchantillonsManager(b2).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(e).size() == 4);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);

      emp.setObjetId(e.getEchantillonId());
      emplacementManager.updateObjectManager(emp, emp.getTerminale(), entiteDao.findById(3));
      emp = emplacementManager.findByIdManager(emp.getEmplacementId());
      emp.getTerminale().getEnceinte();
      prodDeriveManager.updateObjectManager(derive, derive.getBanque(), derive.getProdType(), null, null, null, null, null, null,
         null, null, transfo1, null, null, null, null, u, false, null, null);
      echantillonManager.updateObjectManager(e, e.getBanque(), null, null, null, emp, e.getEchantillonType(), null, null, null,
         null, null, null, null, null, null, u, false, null, null);
      // e = echantillonManager.findByIdManager(e.getEchantillonId());

      try{
         echantillonManager.switchBanqueCascadeManager(e, b2, true, u, null,null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("echantillon.switchBanque.badBanqueStockage"));
      }
      assertTrue(catched);
      assertTrue(banqueManager.getEchantillonsManager(b3).size() == 1);
      assertTrue(banqueManager.getEchantillonsManager(b2).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(e).size() == 5);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);

      // annotations
      echantillonManager.switchBanqueCascadeManager(e, b1, true, u, null, null);
      // switched ok
      assertTrue(banqueManager.getEchantillonsManager(b3).isEmpty());
      assertTrue(banqueManager.getEchantillonsManager(b1).size() == 4);
      assertTrue(getOperationManager().findByObjectManager(e).size() == 6);

      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final AnnotationValeur num = new AnnotationValeur();
      num.setChampAnnotation(champAnnotationDao.findById(7));
      num.setAlphanum("123456");
      num.setBanque(b1);
      listAnnots.add(num);
      final AnnotationValeur link = new AnnotationValeur();
      link.setChampAnnotation(champAnnotationDao.findById(16));
      link.setAlphanum("http://yahoo.fr");
      link.setBanque(b1);
      listAnnots.add(link);
      final AnnotationValeur thes = new AnnotationValeur();
      thes.setChampAnnotation(champAnnotationDao.findById(40));
      thes.setItem(itemDao.findById(83));
      thes.setBanque(b1);
      listAnnots.add(thes);
      echantillonManager.updateObjectManager(e, e.getBanque(), null, null, null, emp, e.getEchantillonType(), null, null, null,
         null, null, listAnnots, null, null, null, u, false, null, "/tmp/");
      assertTrue(annotationValeurManager.findByObjectManager(e).size() == 3);

      // remise des ceder-objets même pfs -> pas erreur
      cederObjetManager.createObjectManager(ced2, cession, entiteDao.findById(8), null);
      cederObjetManager.createObjectManager(ced1, cession, entite, null);

      echantillonManager.switchBanqueCascadeManager(e, b3, true, u, null, null);
      assertTrue(annotationValeurManager.findByObjectManager(e).size() == 1);
      assertTrue(annotationValeurManager.findByObjectManager(e).get(0).getChampAnnotation().getId() == 7);

      // clean up
      final List<Enceinte> encs = enceinteManager.findAllEnceinteByConteneurManager(c);
      final List<Terminale> terminales = new ArrayList<>();
      final Iterator<Enceinte> encIt = encs.iterator();
      while(encIt.hasNext()){
         terminales.addAll(terminaleManager.findByEnceinteWithOrderManager(encIt.next()));
      }
      cederObjetManager.removeObjectManager(ced1);
      cederObjetManager.removeObjectManager(ced2);

      //prodDeriveManager.removeObjectManager(derive, null, u);
      echantillonManager.removeObjectCascadeManager(e, null, u, null);
      echantillonManager.removeObjectCascadeManager(ebis, null, u, null);
      emp.setEntite(null);
      emp.setObjetId(null);
      emp.setVide(true);
      emplacementManager.removeObjectManager(emp);
      conteneurManager.removeObjectManager(conteneurManager.findByIdManager(c.getConteneurId()), null, u);
      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(e);
      fs.add(ebis);
      fs.add(derive);
      fs.add(c);
      fs.addAll(encs);
      fs.addAll(terminales);
      cleanUpFantomes(fs);
   }

   @Test
   public void testUpdateMultipleObjectsManager() throws ParseException, IOException{
      final Banque b1 = banqueManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);
      final Prelevement p1 = prelevementDao.findById(1);

      // on insert 3 nouveaux Echantillons pour les tests
      final Echantillon echan1 = new Echantillon();
      final Echantillon echan2 = new Echantillon();
      final Echantillon echan3 = new Echantillon();
      echan1.setCode("e1");
      echan2.setCode("e2");
      echan3.setCode("e3");
      echan1.setSterile(false);
      echan2.setSterile(false);
      echan3.setSterile(false);
      final EchantillonType eT = echantillonTypeManager.findByIdManager(1);
      final Collaborateur c = collaborateurManager.findByIdManager(1);
      final Calendar stockDate = Calendar.getInstance();
      stockDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/2010 14:57:01"));
      echan1.setDateStock(stockDate);
      echan2.setDateStock(stockDate);
      final ObjetStatut statut = objetStatutManager.findByIdManager(2);
      final EchanQualite eQual = echanQualiteManager.findByIdManager(1);
      final ModePrepa prepa = modePrepaManager.findByIdManager(1);
      final Utilisateur u = utilisateurDao.findById(1);

      final int totFichier = fichierManager.findAllObjectsManager().size();
      final int totEchans = echantillonManager.findAllObjectsManager().size();

      // creation architecture fichiers
      File targetFolder = new File("target");
      File file = new File("target", "/tmp/" + "pt_" + b1.getPlateforme().getPlateformeId() + "/" + "coll_" + b1.getBanqueId() + "/cr_anapath/");
      if(file.exists()){
         FileUtils.deleteDirectory(file);
      }
      String pathFile = targetFolder.getAbsolutePath().replace("\\", "/");
      file.mkdirs();

      final Fichier cr1 = new Fichier();
      cr1.setNom("cr1");
      final String tmp = "CR1";
      final ByteArrayInputStream bais1 = new ByteArrayInputStream(tmp.getBytes());
      final Fichier cr2 = new Fichier();
      cr2.setNom("cr2");
      final String tmp2 = "CR24";
      final ByteArrayInputStream bais2 = new ByteArrayInputStream(tmp2.getBytes());

      final List<CodeAssigne> codes = new ArrayList<>();
      final CodeAssigne cOrg1 = new CodeAssigne();
      cOrg1.setCode("BL");
      cOrg1.setLibelle("langue");
      cOrg1.setIsOrgane(true);
      cOrg1.setCodeRefId(55);
      cOrg1.setTableCodage(tableCodageDao.findById(1));
      cOrg1.setOrdre(1);
      cOrg1.setExport(true);
      final CodeAssigne cOrg2 = new CodeAssigne();
      cOrg2.setCode("codePerso");
      cOrg2.setLibelle("languedeboeuf");
      cOrg2.setIsOrgane(true);
      cOrg2.setOrdre(2);
      codes.add(cOrg1);
      codes.add(cOrg2);

      final CodeAssigne cLes1 = new CodeAssigne();
      cLes1.setCode("K14.5");
      cLes1.setLibelle("langue plicaturée");
      cLes1.setIsOrgane(false);
      cLes1.setIsMorpho(true);
      cLes1.setCodeRefId(4630);
      cLes1.setTableCodage(tableCodageDao.findById(2));
      cLes1.setOrdre(1);
      cLes1.setExport(true);
      codes.add(cLes1);

      final List<Echantillon> echans = new ArrayList<>();
      echans.add(echan1);
      echans.add(echan2);
      echans.add(echan3);
      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final List<AnnotationValeur> listDelete = new ArrayList<>();

      final AnnotationValeur num1 = new AnnotationValeur();
      num1.setChampAnnotation(champAnnotationDao.findById(7));
      num1.setAlphanum("1234");
      listAnnots.add(num1);
      final AnnotationValeur num2 = new AnnotationValeur();
      num2.setChampAnnotation(champAnnotationDao.findById(7));
      num2.setAlphanum("5678");
      listAnnots.add(num2);

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      echantillonManager.createObjectWithCrAnapathManager(echan1, b1, p1, c, statut, null, eT, codes, null, eQual, prepa, cr1,
         bais1, filesCreated, null, utilisateur, false, pathFile + "/tmp/", false);
      echan2.setDelaiCgl(new Float(15));
      cOrg1.setExport(false);
      cOrg2.setExport(true);
      echantillonManager.createObjectWithCrAnapathManager(echan2, b1, p1, c, statut, null, eT, codes, null, eQual, prepa, cr2,
         bais2, filesCreated, null, utilisateur, false, pathFile + "/tmp/", false);
      echantillonManager.createObjectWithCrAnapathManager(echan3, b1, p1, c, statut, null, eT, null, null, eQual, prepa, null,
         null, filesCreated, null, utilisateur, false, pathFile + "/tmp/", false);

      assertTrue(filesCreated.size() == 2);
      assertTrue(new File("target", "/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 2);
      filesCreated.clear();

      for(int i = 0; i < listAnnots.size(); i++){
         annotationValeurManager.createOrUpdateObjectManager(listAnnots.get(i), listAnnots.get(i).getChampAnnotation(),
            echans.get(i), b1, null, utilisateur, "creation", pathFile + "/tmp/", filesCreated, filesToDelete);
      }

      final int id1 = echan1.getEchantillonId();
      final int id2 = echan2.getEchantillonId();
      final int id3 = echan3.getEchantillonId();

      assertTrue(echantillonManager.findAllObjectsManager().size() == totEchans + 3);
      assertTrue(codeAssigneManager.findAllObjectsManager().size() == 11);
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 2);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 14);
      final Echantillon eUp1 = echantillonManager.findByCodeLikeManager("e1", true).get(0);
      final Echantillon eUp2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      final Echantillon eUp3 = echantillonManager.findByCodeLikeManager("e3", true).get(0);
      assertTrue(eUp1.getCrAnapath().getNom().equals("cr1"));
      assertTrue(new File(eUp1.getCrAnapath().getPath()).length() == 3);
      assertTrue(eUp2.getCrAnapath().getNom().equals("cr2"));
      assertTrue(new File(eUp2.getCrAnapath().getPath()).length() == 4);
      assertTrue(new File("target", "/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 2);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(id1, entiteDao.findById(3)).isEmpty());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id2, entiteDao.findById(3)).isEmpty());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id3, entiteDao.findById(3)).isEmpty());

      // on teste une maj valide
      final String lat = "D";
      eUp1.setLateralite(lat);
      eUp2.setLateralite(lat);
      eUp3.setLateralite(lat);
      List<Echantillon> list = new ArrayList<>();
      list.add(eUp1);
      list.add(eUp2);
      list.add(eUp3);
      listAnnots.clear();
      listAnnots.addAll(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), echan1));
      listAnnots.addAll(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), echan2));
      final AnnotationValeur num3 = new AnnotationValeur();
      num3.setChampAnnotation(champAnnotationDao.findById(7));
      num3.setAlphanum("91011");
      num3.setObjetId(id3);
      num3.setBanque(b1);
      listAnnots.add(num3);
      listAnnots.get(0).setAlphanum("1111");
      listAnnots.get(1).setAlphanum("2222");

      codes.clear();
      final CodeAssigne cOrg3 = new CodeAssigne();
      cOrg3.setCode("GR");
      cOrg3.setLibelle("gras");
      cOrg3.setIsOrgane(true);
      cOrg3.setTableCodage(tableCodageDao.findById(1));
      cOrg3.setCodeRefId(16);
      cOrg3.setOrdre(1);
      cOrg3.setExport(true);
      codes.add(cOrg3);

      final CodeAssigne cLes3 = new CodeAssigne();
      cLes3.setCode("B12");
      cLes3.setLibelle("sel poivre");
      cLes3.setIsOrgane(false);
      cLes3.setCodeRefId(23);
      cLes3.setIsMorpho(true);
      cLes3.setTableCodage(tableCodageDao.findById(2));
      cLes3.setOrdre(1);
      cLes3.setExport(true);
      codes.add(cLes3);

      //		eUp1.setCodeOrganeExport(cOrg3);
      //		eUp1.setCodeLesExport(cLes3);
      //		eUp2.setCodeOrganeExport(cOrg3);
      //		eUp2.setCodeLesExport(cLes3);
      //		eUp3.setCodeOrganeExport(cOrg3);
      //		eUp3.setCodeLesExport(cLes3);

      final List<CodeAssigne> toDeletes = new ArrayList<>();
      toDeletes.addAll(codeAssigneManager.findCodesOrganeByEchantillonManager(eUp1));
      toDeletes.addAll(codeAssigneManager.findCodesMorphoByEchantillonManager(eUp1));
      toDeletes.addAll(codeAssigneManager.findCodesOrganeByEchantillonManager(eUp2));
      toDeletes.addAll(codeAssigneManager.findCodesMorphoByEchantillonManager(eUp2));

      final List<NonConformite> ncfsTrait = new ArrayList<>();
      ncfsTrait.add(nonConformiteDao.findById(4));

      final List<NonConformite> ncfsCess = new ArrayList<>();
      ncfsCess.add(nonConformiteDao.findById(6));

      //		Fichier cr3 = new Fichier(); cr3.setNom("cr3"); String tmp3 = "CR345";
      //		ByteArrayInputStream bais3 = new ByteArrayInputStream(tmp3.getBytes());
      //		-> ok. Ecrase par nouveau fichier

      echantillonManager.updateMultipleObjectsManager(list, null, codes, toDeletes, eUp1.getCrAnapath(), null, null, listAnnots,
         listDelete, ncfsTrait, ncfsCess, utilisateur, pathFile + "/tmp/");
      assertTrue(codeAssigneManager.findAllObjectsManager().size() == 11);
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 3);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 15);

      Echantillon eTest1 = echantillonManager.findByCodeLikeManager("e1", true).get(0);
      Echantillon eTest2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      Echantillon eTest3 = echantillonManager.findByCodeLikeManager("e3", true).get(0);
      assertTrue(eTest1.getLateralite().equals(lat));
      assertTrue(eTest2.getLateralite().equals(lat));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), eTest1).get(0).getAlphanum()
         .equals("1111"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), eTest2).get(0).getAlphanum()
         .equals("2222"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), eTest3).get(0).getAlphanum()
         .equals("91011"));

      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(eTest1).size() == 1);
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(eTest1).size() == 1);
      //assertTrue(eTest1.getCodeOrganeExport().getCode().equals("GR"));
      //assertTrue(eTest1.getCodeLesExport().getCode().equals("B12"));
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(eTest2).size() == 1);
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(eTest2).size() == 1);
      //assertTrue(eTest2.getCodeOrganeExport().getCode().equals("GR"));
      //assertTrue(eTest2.getCodeLesExport().getCode().equals("B12"));
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(eTest3).size() == 1);
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(eTest3).size() == 1);
      //assertTrue(eTest3.getCodeOrganeExport().getCode().equals("GR"));
      //assertTrue(eTest3.getCodeLesExport().getCode().equals("B12"));
      assertFalse(eTest1.getCrAnapath().equals(eTest2.getCrAnapath()));
      assertFalse(eTest2.getCrAnapath().equals(eTest3.getCrAnapath()));
      assertFalse(eTest1.getCrAnapath().equals(eTest3.getCrAnapath()));
      assertTrue(eTest1.getCrAnapath().getPath().equals(eTest2.getCrAnapath().getPath()));
      assertTrue(eTest2.getCrAnapath().getPath().equals(eTest3.getCrAnapath().getPath()));
      assertTrue(eTest1.getCrAnapath().getPath().equals(eTest3.getCrAnapath().getPath()));
      assertTrue(eTest1.getCrAnapath().getNom().equals("cr1"));
      assertTrue(eTest2.getCrAnapath().getNom().equals("cr1"));
      assertTrue(eTest3.getCrAnapath().getNom().equals("cr1"));
      assertTrue(new File(eTest1.getCrAnapath().getPath()).length() == 3);
      assertTrue(new File("target", "/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);

      assertTrue(getOperationManager().findByObjectManager(eTest1).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(eTest1).get(1).getOperationType().getNom().equals("ModifMultiple"));

      assertTrue(objetNonConformeDao.findByObjetAndEntite(id1, entiteDao.findById(3)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id2, entiteDao.findById(3)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id3, entiteDao.findById(3)).size() == 2);

      // on teste une maj non valide sur le 1er élément
      eTest1.setQuantite(new Float(-12.5));
      eTest1.setDelaiCgl(new Float(10.0));
      eTest2.setDelaiCgl(new Float(20.0));
      eTest3.setDelaiCgl(new Float(30.0));
      list = new ArrayList<>();
      list.add(eTest1);
      list.add(eTest2);
      list.add(eTest3);
      listAnnots.get(0).setAlphanum("0000");
      listAnnots.get(2).setAlphanum("9999");

      ncfsTrait.clear();
      ncfsCess.clear();

      boolean catched = false;
      try{
         echantillonManager.updateMultipleObjectsManager(list, list, null, null, null, null, null, listAnnots, listDelete,
            ncfsTrait, ncfsTrait, utilisateur, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;

            assertTrue(((ValidationException) e).getEntiteObjetException().equals("Echantillon"));
            assertTrue(((ValidationException) e).getIdentificationObjetException().equals(list.get(0).getCode()));
         }
      }
      assertTrue(catched);
      eTest1 = echantillonManager.findByCodeLikeManager("e1", true).get(0);
      eTest2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      eTest3 = echantillonManager.findByCodeLikeManager("e3", true).get(0);
      assertNull(eTest1.getDelaiCgl());
      assertTrue(eTest2.getDelaiCgl().equals(new Float(15.0)));
      assertNull(eTest3.getDelaiCgl());
      assertFalse(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), eTest1).get(0).getAlphanum()
         .equals("0000"));

      // on teste une maj non valide sur le 3eme élément
      eTest1.setQuantite(new Float(-12.5));
      eTest1.setDelaiCgl(new Float(10.0));
      eTest2.setDelaiCgl(new Float(20.0));
      eTest3.setDelaiCgl(new Float(30.0));
      list = new ArrayList<>();
      list.add(eTest1);
      list.add(eTest2);
      list.add(eTest3);
      listAnnots.get(0).setAlphanum("0000");
      listAnnots.get(2).setAlphanum("9999");

      toDeletes.clear();
      toDeletes.add(codeAssigneManager.findCodesOrganeByEchantillonManager(eTest3).get(0));
      //eTest3.setCodeOrganeExport(null);
      codes.clear();

      catched = false;
      try{
         echantillonManager.updateMultipleObjectsManager(list, list, codes, toDeletes, null, null, null, listAnnots, listDelete,
            ncfsTrait, ncfsCess, utilisateur, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      eTest1 = echantillonManager.findByCodeLikeManager("e1", true).get(0);
      eTest2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      eTest3 = echantillonManager.findByCodeLikeManager("e3", true).get(0);
      list.clear();
      list.add(eTest1);
      list.add(eTest2);
      list.add(eTest3);
      assertNull(eTest1.getDelaiCgl());
      assertTrue(eTest2.getDelaiCgl().equals(new Float(15.0)));
      assertNull(eTest3.getDelaiCgl());
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(eTest3).size() == 1);
      // assertNotNull(eTest3.getCodeOrganeExport());
      assertFalse(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), eTest1).get(0).getAlphanum()
         .equals("0000"));

      assertTrue(objetNonConformeDao.findByObjetAndEntite(id1, entiteDao.findById(3)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id2, entiteDao.findById(3)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id3, entiteDao.findById(3)).size() == 2);

      // on teste avec creation d'annotation
      listDelete.add(listAnnots.get(0));
      listDelete.add(listAnnots.get(1));
      listAnnots.clear();
      eTest1.setCode("NEW");
      final AnnotationValeur bool4 = new AnnotationValeur();
      bool4.setBool(true);
      bool4.setChampAnnotation(champAnnotationDao.findById(4));
      bool4.setObjetId(id1);
      bool4.setBanque(b1);
      listAnnots.add(bool4);
      final AnnotationValeur alpha5 = new AnnotationValeur();
      alpha5.setAlphanum("&&é**¤¤");
      alpha5.setChampAnnotation(champAnnotationDao.findById(7));
      alpha5.setObjetId(id2);
      alpha5.setBanque(b1);
      listAnnots.add(alpha5);
      // validationException
      try{
         echantillonManager.updateMultipleObjectsManager(list, list, null, null, null, null, null, listAnnots, listDelete, null,
            null, utilisateur, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      eTest1 = echantillonManager.findByCodeLikeManager("e1", true).get(0);
      eTest2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      assertFalse(eTest1.getCode().equals("NEW"));
      assertFalse(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(4), eTest1).size() > 0);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), eTest2).get(0).getAlphanum()
         .equals("2222"));

      // modifications valides
      //alpha1.setAnnotationValeurId(null);
      alpha5.setAlphanum("8888");
      echantillonManager.updateMultipleObjectsManager(list, null, null, null, null, null, false, listAnnots, listDelete, null,
         null, utilisateur, pathFile + "/tmp/");
      eTest1 = echantillonManager.findByCodeLikeManager("NEW", true).get(0);
      eTest2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      eTest3 = echantillonManager.findByCodeLikeManager("e3", true).get(0);
      assertTrue(eTest1.getCode().equals("NEW"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(4), eTest1).get(0).getBool());
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), eTest2).get(0).getAlphanum()
         .equals("8888"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), eTest3).get(0).getAlphanum()
         .equals("91011"));
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 3);
      assertTrue(eTest3.getCrAnapath().getNom().equals("cr1"));
      assertTrue(new File(eTest3.getCrAnapath().getPath()).length() == 3);

      // update cr anapath partagé
      Fichier cr4 = new Fichier();
      cr4.setNom("cr4");
      final String tmp4 = "CR3456789";
      final ByteArrayInputStream bais4 = new ByteArrayInputStream(tmp4.getBytes());
      echantillonManager.updateMultipleObjectsManager(list, null, null, null, cr4, bais4, false, null, null, null, null,
         utilisateur, pathFile + "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 3);
      eTest1 = echantillonManager.findByCodeLikeManager("NEW", true).get(0);
      eTest2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      eTest3 = echantillonManager.findByCodeLikeManager("e3", true).get(0);
      assertFalse(eTest1.getCrAnapath().equals(eTest2.getCrAnapath()));
      assertFalse(eTest2.getCrAnapath().equals(eTest3.getCrAnapath()));
      assertFalse(eTest1.getCrAnapath().equals(eTest3.getCrAnapath()));
      assertTrue(eTest1.getCrAnapath().getPath().equals(eTest2.getCrAnapath().getPath()));
      assertTrue(eTest2.getCrAnapath().getPath().equals(eTest3.getCrAnapath().getPath()));
      assertTrue(eTest1.getCrAnapath().getPath().equals(eTest3.getCrAnapath().getPath()));
      assertTrue(eTest1.getCrAnapath().getNom().equals("cr4"));
      assertTrue(eTest2.getCrAnapath().getNom().equals("cr4"));
      assertTrue(eTest3.getCrAnapath().getNom().equals("cr4"));
      assertTrue(new File(eTest1.getCrAnapath().getPath()).length() == 9);
      assertTrue(new File("target", "/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);

      final String oldPath = eTest1.getCrAnapath().getPath();

      assertTrue(objetNonConformeDao.findByObjetAndEntite(id1, entiteDao.findById(3)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id2, entiteDao.findById(3)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id3, entiteDao.findById(3)).size() == 2);

      // update nom cr anapath partagé
      cr4 = eTest2.getCrAnapath();
      cr4.setNom("cr5");
      echantillonManager.updateMultipleObjectsManager(list, list, null, null, cr4, null, null, null, null, null, null,
         utilisateur, pathFile + "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 3);
      eTest1 = echantillonManager.findByCodeLikeManager("NEW", true).get(0);
      eTest2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      eTest3 = echantillonManager.findByCodeLikeManager("e3", true).get(0);
      assertFalse(eTest1.getCrAnapath().equals(eTest2.getCrAnapath()));
      assertFalse(eTest2.getCrAnapath().equals(eTest3.getCrAnapath()));
      assertFalse(eTest1.getCrAnapath().equals(eTest3.getCrAnapath()));
      assertTrue(eTest1.getCrAnapath().getPath().equals(eTest2.getCrAnapath().getPath()));
      assertTrue(eTest2.getCrAnapath().getPath().equals(eTest3.getCrAnapath().getPath()));
      assertTrue(eTest1.getCrAnapath().getPath().equals(eTest3.getCrAnapath().getPath()));
      assertTrue(eTest1.getCrAnapath().getPath().equals(oldPath));
      assertTrue(eTest1.getCrAnapath().getNom().equals("cr5"));
      assertTrue(eTest2.getCrAnapath().getNom().equals("cr5"));
      assertTrue(eTest3.getCrAnapath().getNom().equals("cr5"));
      assertTrue(new File(eTest1.getCrAnapath().getPath()).length() == 9);
      assertTrue(new File("target", "/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);

      // delete cr anapath pour 2 echans
      list.remove(eTest3);
      echantillonManager.updateMultipleObjectsManager(list, list, null, null, null, null, true, null, null, null, null,
         utilisateur, pathFile + "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 1);
      eTest1 = echantillonManager.findByCodeLikeManager("NEW", true).get(0);
      eTest2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      eTest3 = echantillonManager.findByCodeLikeManager("e3", true).get(0);
      assertNull(eTest1.getCrAnapath());
      assertNull(eTest2.getCrAnapath());
      assertNotNull(eTest3.getCrAnapath());
      assertTrue(eTest3.getCrAnapath().getPath().equals(oldPath));
      assertTrue(eTest3.getCrAnapath().getNom().equals("cr5"));
      assertTrue(new File(eTest3.getCrAnapath().getPath()).length() == 9);
      assertTrue(new File("target", "/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);

      // restaure un fichier pour les 3
      final Fichier cr6 = new Fichier();
      cr6.setNom("cr6");
      final String tmp6 = "666";
      final ByteArrayInputStream bais6 = new ByteArrayInputStream(tmp6.getBytes());
      eTest1 = echantillonManager.findByCodeLikeManager("NEW", true).get(0);
      eTest2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      eTest3 = echantillonManager.findByCodeLikeManager("e3", true).get(0);
      list.clear();
      list.add(eTest1);
      list.add(eTest2);
      list.add(eTest3);
      echantillonManager.updateMultipleObjectsManager(list, null, null, null, cr6, bais6, false, null, null, null, null,
         utilisateur, pathFile + "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier + 3);
      eTest1 = echantillonManager.findByCodeLikeManager("NEW", true).get(0);
      eTest2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      eTest3 = echantillonManager.findByCodeLikeManager("e3", true).get(0);
      assertFalse(eTest1.getCrAnapath().equals(eTest2.getCrAnapath()));
      assertFalse(eTest2.getCrAnapath().equals(eTest3.getCrAnapath()));
      assertFalse(eTest1.getCrAnapath().equals(eTest3.getCrAnapath()));
      assertTrue(eTest1.getCrAnapath().getPath().equals(eTest2.getCrAnapath().getPath()));
      assertTrue(eTest2.getCrAnapath().getPath().equals(eTest3.getCrAnapath().getPath()));
      assertTrue(eTest1.getCrAnapath().getPath().equals(eTest3.getCrAnapath().getPath()));
      assertFalse(eTest1.getCrAnapath().getPath().equals(oldPath));
      assertTrue(eTest1.getCrAnapath().getNom().equals("cr6"));
      assertTrue(eTest2.getCrAnapath().getNom().equals("cr6"));
      assertTrue(eTest3.getCrAnapath().getNom().equals("cr6"));
      assertTrue(new File(eTest1.getCrAnapath().getPath()).length() == 3);
      assertTrue(new File("target", "/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 1);

      // suppr cr anapath pour tous
      echantillonManager.updateMultipleObjectsManager(list, null, null, null, null, null, true, null, null, null, null,
         utilisateur, pathFile + "/tmp/");
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier);
      eTest1 = echantillonManager.findByCodeLikeManager("NEW", true).get(0);
      eTest2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);
      eTest3 = echantillonManager.findByCodeLikeManager("e3", true).get(0);
      assertNull(eTest1.getCrAnapath());
      assertNull(eTest2.getCrAnapath());
      assertNull(eTest3.getCrAnapath());
      assertTrue(new File("target", "/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 0);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(id1, entiteDao.findById(3)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id2, entiteDao.findById(3)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id3, entiteDao.findById(3)).size() == 2);

      // Suppression
      for(int i = 0; i < list.size(); i++){
         //cleanup cascade vers echantillons
         echantillonManager.removeObjectCascadeManager(list.get(i), null, u, null);
      }
      // verification de l'etat de la base
      testFindAll();
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);
      assertTrue(codeAssigneManager.findAllObjectsManager().size() == 5);
      assertTrue(fichierManager.findAllObjectsManager().size() == totFichier);
      assertTrue(objetNonConformeDao.findAll().size() == 6);
      assertTrue(new File("target", "/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 0);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(list);
      cleanUpFantomes(fs);
   }

   @Test
   public void testExtractValueForChampManager(){
      final Echantillon e1 = echantillonManager.findByIdManager(1);
      final Champ c54 = new Champ(champEntiteDao.findById(54));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(e1, c54).equals("PTRA.1"));

      final Champ c3 = new Champ(champEntiteDao.findById(3));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(e1, c3).equals("DELPHINO"));

      final Champ c23 = new Champ(champEntiteDao.findById(23));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(e1, c23).equals("PRLVT1"));

      final Champ c18 = new Champ(champEntiteDao.findById(18));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(e1, c18).equals("C45.3"));

      // test sur le risque
      final Champ c249 = new Champ(champEntiteDao.findById(249));
      final String[] expected = new String[]{"HIV", "LEUCEMIE"};
      String value = extractValueFromChampManager.extractValueForChampManager(e1, c249);
      List<String> actual = Stream.of(value.split(",")).map( token -> token.trim() ).collect(Collectors.toList());
      assertTrue( actual.size() == 2 && actual.containsAll(Arrays.asList(expected)) );

      // test sur l'emplacement
      final Echantillon e2 = echantillonManager.findByIdManager(2);
      final Champ c57 = new Champ(champEntiteDao.findById(57));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(e2, c57).equals("CC1.R1.T1.BT1.A-C"));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(e1, c57).equals(""));

      // test sur les codes organes
      final Champ c229 = new Champ(champEntiteDao.findById(229));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(e1, c229).equals("BL, face dorsale de la langue"));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(e2, c229).equals(""));

      // test sur les codes morphos
      final Champ c230 = new Champ(champEntiteDao.findById(230));
      assertTrue(
         extractValueFromChampManager.extractValueForChampManager(e1, c230).equals("langue plicaturée, BL0211-2, D5-22050"));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(e2, c230).equals(""));
      final Echantillon nully = null;
      assertNull(extractValueFromChampManager.extractValueForChampManager(nully, c18));
      assertNull(extractValueFromChampManager.extractValueForChampManager(e1, null));
      assertNull(extractValueFromChampManager.extractValueForChampManager(nully, null));
   }

   @Test
   public void testUpdateCodeEchantillonsManager(){
      final Banque banque = banqueManager.findByIdManager(1);
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      // Création de 3 échantillons
      final Echantillon echan1 = new Echantillon();
      echan1.setObjetStatut(objetStatutManager.findByIdManager(4));
      echan1.setCode("XTRA.1");
      echantillonManager.createObjectManager(echan1, banque, null, null, null, null, type, null, null, null, null, null,
         null, utilisateur, false, null, false);
      final Echantillon echan2 = new Echantillon();
      echan2.setObjetStatut(objetStatutManager.findByIdManager(4));
      echan2.setCode("XTRA.2");
      echantillonManager.createObjectManager(echan2, banque, null, null, null, null, type, null, null, null, null, null,
         null, utilisateur, false, null, false);
      final Echantillon echan3 = new Echantillon();
      echan3.setCode("XTRA.3");
      echan3.setObjetStatut(objetStatutManager.findByIdManager(4));
      echantillonManager.createObjectManager(echan3, banque, null, null, null, null, type, null, null, null, null, null,
         null, utilisateur, false, null, false);
      assertTrue(echantillonManager.findAllObjectsManager().size() == 7);
      assertTrue(echantillonManager.findByCodeLikeManager("XTRA", false).size() == 3);

      final List<Echantillon> liste = new ArrayList<>();
      liste.add(echan1);
      liste.add(echan2);
      liste.add(echan3);

      // maj non valide : 1 élt null
      echantillonManager.updateCodeEchantillonsManager(liste, null, "XXX", utilisateur);
      assertTrue(echantillonManager.findByCodeLikeManager("XTRA", false).size() == 3);
      echantillonManager.updateCodeEchantillonsManager(null, "XTRA", "XXX", utilisateur);
      assertTrue(echantillonManager.findByCodeLikeManager("XTRA", false).size() == 3);
      echantillonManager.updateCodeEchantillonsManager(liste, "XTRA", null, utilisateur);
      assertTrue(echantillonManager.findByCodeLikeManager("XTRA", false).size() == 3);
      echantillonManager.updateCodeEchantillonsManager(liste, "XTRA", "XXX", null);
      assertTrue(echantillonManager.findByCodeLikeManager("XTRA", false).size() == 3);

      // maj non valide : ancien prefixe = new prefixe
      List<Echantillon> res = echantillonManager.updateCodeEchantillonsManager(liste, "XTRA", "XTRA", utilisateur);
      assertTrue(echantillonManager.findByCodeLikeManager("XTRA", false).size() == 3);
      assertTrue(res.size() == 3);
      for(int i = 0; i < res.size(); i++){
         assertTrue(res.get(i).getCode().startsWith("XTRA"));
      }

      // maj non valide : ancien prefixe incorrect
      res = echantillonManager.updateCodeEchantillonsManager(liste, "XXX", "XPOR", utilisateur);
      assertTrue(echantillonManager.findByCodeLikeManager("XTRA", false).size() == 3);
      assertTrue(echantillonManager.findByCodeLikeManager("XPOR", false).size() == 0);
      assertTrue(res.size() == 3);
      for(int i = 0; i < res.size(); i++){
         assertTrue(res.get(i).getCode().startsWith("XTRA"));
      }

      // maj valide
      res = echantillonManager.updateCodeEchantillonsManager(liste, "XTRA", "XPOR", utilisateur);
      assertTrue(echantillonManager.findByCodeLikeManager("XTRA", false).size() == 0);
      assertTrue(echantillonManager.findByCodeLikeManager("XPOR", false).size() == 3);
      assertTrue(res.size() == 3);
      for(int i = 0; i < res.size(); i++){
         assertTrue(res.get(i).getCode().startsWith("XPOR"));
      }

      // Suppression
      final List<TKFantomableObject> fs = new ArrayList<>();
      for(int i = 0; i < res.size(); i++){
         echantillonManager.removeObjectManager(res.get(i), null, utilisateur, null);
         fs.add(res.get(i));
      }
      cleanUpFantomes(fs);
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
   }

   @Test
   public void testSaveEchantillonEmplacementManager(){
      final Banque banque = banqueManager.findByIdManager(1);
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);
      final ObjetStatut statut = objetStatutManager.findByStatutLikeManager("STOCKE", true).get(0);
      Emplacement empl = emplacementManager.findByIdManager(4);
      final OperationType op = operationTypeManager.findByNomLikeManager("Stockage", true).get(0);
      final List<OperationType> ops = new ArrayList<>();
      ops.add(op);

      // Création de 3 échantillons
      final Echantillon echan1 = new Echantillon();
      echan1.setCode("XTRA.1");
      echantillonManager.createObjectManager(echan1, banque, null, null, objetStatutManager.findByIdManager(4), null, type, null,
         null, null, null, null, null, utilisateur, false, "/tmp/", false);
      assertTrue(echantillonManager.findAllObjectsManager().size() == 5);
      assertTrue(echantillonManager.findByCodeLikeManager("XTRA", false).size() == 1);
      final Integer id = echan1.getEchantillonId();

      // maj non valide : 1 élt null
      echantillonManager.saveEchantillonEmplacementManager(null, statut, empl, utilisateur, ops);
      Echantillon eTest1 = echantillonManager.findByIdManager(id);
      assertNull(echantillonManager.getEmplacementManager(eTest1));
      echantillonManager.saveEchantillonEmplacementManager(echan1, statut, empl, null, ops);
      assertNull(echantillonManager.getEmplacementManager(eTest1));

      // maj non valide : Emplacement occupé par un échantillon
      empl = emplacementManager.findByIdManager(3);
      boolean catched = false;
      try{
         echantillonManager.saveEchantillonEmplacementManager(echan1, statut, empl, utilisateur, ops);
      }catch(final TKException ex){
         catched = true;
         assertTrue(ex.getMessage().equals("XTRA.1 : error.emplacement.notEmpty"));
         assertTrue(ex.getIdentificationObjetException().equals("XTRA.1"));
      }
      assertTrue(catched);
      eTest1 = echantillonManager.findByIdManager(id);
      assertNull(echantillonManager.getEmplacementManager(eTest1));

      // maj non valide : Emplacement occupé par un dérivé
      empl = emplacementManager.findByIdManager(2);
      catched = false;
      try{
         echantillonManager.saveEchantillonEmplacementManager(eTest1, statut, empl, utilisateur, ops);
      }catch(final TKException ex){
         catched = true;
         assertTrue(ex.getMessage().equals("XTRA.1 : error.emplacement.notEmpty"));
         assertTrue(ex.getIdentificationObjetException().equals("XTRA.1"));
      }
      assertTrue(catched);
      eTest1 = echantillonManager.findByIdManager(id);
      assertNull(echantillonManager.getEmplacementManager(eTest1));

      // maj valide
      empl = emplacementManager.findByIdManager(4);
      echantillonManager.saveEchantillonEmplacementManager(echan1, statut, empl, utilisateur, ops);
      final Echantillon eTest2 = echantillonManager.findByIdManager(id);
      final Emplacement empl2 = echantillonManager.getEmplacementManager(eTest2);
      assertNotNull(empl2);
      assertTrue(empl2.getEmplacementId() == 4);
      assertTrue(eTest2.getObjetStatut().getStatut().equals("STOCKE"));

      // maj à null
      catched = false;
      try{
         eTest2.setObjetStatut(null);
         echantillonManager.saveEchantillonEmplacementManager(eTest2, null, null, utilisateur, ops);
      }catch(final RequiredObjectIsNullException re){
         catched = true;
      }
      assertTrue(catched);

      final Echantillon eTest3 = echantillonManager.findByIdManager(id);
      echantillonManager.saveEchantillonEmplacementManager(eTest3, objetStatutManager.findByIdManager(4), null, utilisateur, ops);

      assertNull(echantillonManager.getEmplacementManager(eTest3));
      assertTrue(eTest3.getObjetStatut().getStatut().equals("NON STOCKE"));

      // Suppression
      final List<TKFantomableObject> fs = new ArrayList<>();
      echantillonManager.removeObjectManager(eTest3, null, utilisateur, null);
      fs.add(eTest3);
      cleanUpFantomes(fs);
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
   }

   @Test
   public void testFindCountByPrelevement(){
      final Prelevement p1 = prelevementDao.findById(1);
      Long res = echantillonManager.findCountByPrelevementManager(p1);
      assertTrue(res == 2);

      final Prelevement p2 = prelevementDao.findById(2);
      res = echantillonManager.findCountByPrelevementManager(p2);
      assertTrue(res == 1);

      final Prelevement p3 = prelevementDao.findById(3);
      res = echantillonManager.findCountByPrelevementManager(p3);
      assertTrue(res == 1);

      final Prelevement p4 = prelevementDao.findById(4);
      res = echantillonManager.findCountByPrelevementManager(p4);
      assertTrue(res == 0);
   }

   @Test
   public void testFindCountRestantsByPrelevement(){
      final Prelevement p1 = prelevementDao.findById(1);
      Long res = echantillonManager.findCountRestantsByPrelevementManager(p1);
      assertTrue(res == 1);

      final Prelevement p2 = prelevementDao.findById(2);
      res = echantillonManager.findCountRestantsByPrelevementManager(p2);
      assertTrue(res == 0);

      final Prelevement p3 = prelevementDao.findById(3);
      res = echantillonManager.findCountRestantsByPrelevementManager(p3);
      assertTrue(res == 0);

      final Prelevement p4 = prelevementDao.findById(4);
      res = echantillonManager.findCountRestantsByPrelevementManager(p4);
      assertTrue(res == 0);
   }

   @Test
   public void testFindCountByPrelevementAndStockeReserve(){
      final Prelevement p1 = prelevementDao.findById(1);
      Long res = echantillonManager.findCountByPrelevementAndStockeReserveManager(p1);
      assertTrue(res == 2);

      final Prelevement p2 = prelevementDao.findById(2);
      res = echantillonManager.findCountByPrelevementAndStockeReserveManager(p2);
      assertTrue(res == 0);

      final Prelevement p3 = prelevementDao.findById(3);
      res = echantillonManager.findCountByPrelevementAndStockeReserveManager(p3);
      assertTrue(res == 1);

      final Prelevement p4 = prelevementDao.findById(4);
      res = echantillonManager.findCountByPrelevementAndStockeReserveManager(p4);
      assertTrue(res == 0);
   }

   @Test
   public void testStatutEmplacementValidation(){
      final Echantillon e = new Echantillon();
      e.setCode("test");
      final Emplacement emp = new Emplacement();
      final ObjetStatut o = new ObjetStatut();

      e.setObjetStatut(o);

      boolean catched = false;
      o.setStatut("RESERVE");
      try{
         BeanValidator.validateObject(e, new Validator[] {echantillonValidator});
      }catch(final ValidationException ve){
         assertEquals("echantillon.statut.incoherent", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);

      catched = false;
      o.setStatut("STOCKE");
      try{
         BeanValidator.validateObject(e, new Validator[] {echantillonValidator});
      }catch(final ValidationException ve){
         assertEquals("echantillon.statut.incoherent", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);

      e.setEmplacement(emp);

      BeanValidator.validateObject(e, new Validator[] {echantillonValidator});

      catched = false;
      o.setStatut("NON STOCKE");
      try{
         BeanValidator.validateObject(e, new Validator[] {echantillonValidator});
      }catch(final ValidationException ve){
         assertEquals("echantillon.statut.incoherent", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);

      catched = false;
      o.setStatut("DETRUIT");
      try{
         BeanValidator.validateObject(e, new Validator[] {echantillonValidator});
      }catch(final ValidationException ve){
         assertEquals("echantillon.statut.incoherent", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);

      catched = false;
      o.setStatut("EPUISE");
      try{
         BeanValidator.validateObject(e, new Validator[] {echantillonValidator});
      }catch(final ValidationException ve){
         assertEquals("echantillon.statut.incoherent", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);
   }

   @Test
   public void testCreateUpdateWithNonConformites(){
      Echantillon e1 = new Echantillon();
      e1.setCode("e1");
      e1.setSterile(true);
      final Utilisateur u = utilisateurDao.findById(1);

      final Banque b1 = banqueDao.findById(1);
      // creation architecture fichiers 
      // car sinon errs
      new File("/tmp/" + "pt_" + b1.getPlateforme().getPlateformeId() + "/" + "coll_" + b1.getBanqueId() + "/cr_anapath/")
         .mkdirs();

      final List<NonConformite> ncfsTrait = new ArrayList<>();
      final List<NonConformite> ncfsCess = new ArrayList<>();

      Echantillon e0 = new Echantillon();
      e0.setCode("e0");
      e0.setSterile(true);

      echantillonManager.createObjectWithNonConformitesManager(e0, b1, null, null, objetStatutManager.findByIdManager(4), null,
         echantillonTypeManager.findByIdManager(1), null, null, null, null, null, null, null, u, false, "/tmp/", false,
         ncfsTrait, ncfsCess);

      e0 = echantillonManager.findByCodeLikeManager("e0", true).get(0);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(e0.getEchantillonId(), entiteDao.findById(3)).size() == 0);
      assertNull(e0.getConformeCession());
      assertNull(e0.getConformeTraitement());

      ncfsTrait.add(nonConformiteDao.findById(4));
      ncfsCess.add(nonConformiteDao.findById(6));
      ncfsCess.add(nonConformiteDao.findById(7));

      echantillonManager.createObjectWithNonConformitesManager(e1, banqueDao.findById(1), null, null,
         objetStatutManager.findByIdManager(4), null, echantillonTypeManager.findByIdManager(1), null, null, null, null,
         null, null, null, u, false, "/tmp/", false, ncfsTrait, ncfsCess);

      e1 = echantillonManager.findByCodeLikeManager("e1", true).get(0);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(e1.getEchantillonId(), entiteDao.findById(3)).size() == 3);
      assertFalse(e1.getConformeCession());
      assertFalse(e1.getConformeTraitement());

      Echantillon e2 = new Echantillon();
      e2.setCode("e2");
      e2.setSterile(true);

      echantillonManager.createObjectWithNonConformitesManager(e2, banqueDao.findById(1), null, null,
         objetStatutManager.findByIdManager(4), null, echantillonTypeManager.findByIdManager(1), null, null, null, null,
         null, null, null, u, false, "/tmp/", false, null, null);

      e2 = echantillonManager.findByCodeLikeManager("e2", true).get(0);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(e2.getEchantillonId(), entiteDao.findById(3)).size() == 0);
      assertNull(e2.getConformeCession());
      assertNull(e2.getConformeTraitement());

      echantillonManager.updateObjectWithNonConformitesManager(e1, banqueDao.findById(1), null, null,
         objetStatutManager.findByIdManager(4), null, echantillonTypeManager.findByIdManager(1), null, null, null, null,
         null, null, null, null, null, u, false, null, "/tmp/", ncfsTrait, ncfsCess);

      ncfsTrait.remove(0);
      ncfsCess.clear();
      e1.setConformeTraitement(true);
      e1.setConformeCession(true);

      echantillonManager.updateObjectWithNonConformitesManager(e1, banqueDao.findById(1), null, null,
         objetStatutManager.findByIdManager(4), null, echantillonTypeManager.findByIdManager(1), null, null, null, null,
         null, null, null, null, null, u, false, null, "/tmp/", ncfsTrait, ncfsCess);

      e1 = echantillonManager.findByCodeLikeManager("e1", true).get(0);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(e1.getEchantillonId(), entiteDao.findById(3)).size() == 0);
      assertTrue(e1.getConformeTraitement());
      assertTrue(e1.getConformeCession());

      ncfsTrait.add(nonConformiteDao.findById(5));
      ncfsCess.add(nonConformiteDao.findById(6));

      echantillonManager.updateObjectWithNonConformitesManager(e1, banqueDao.findById(1), null, null,
         objetStatutManager.findByIdManager(4), null, e1.getEchantillonType(), null, null, null, null, null, null, null,
         null, null, u, false, null, "/tmp/", ncfsTrait, ncfsCess);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(e1.getEchantillonId(), entiteDao.findById(3)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(e1.getEchantillonId(), entiteDao.findById(3)).get(0).getNonConformite()
         .getNom().equals("Echantillon non stérile")
         || objetNonConformeDao.findByObjetAndEntite(e1.getEchantillonId(), entiteDao.findById(3)).get(0).getNonConformite()
            .getNom().equals("Non cédable"));
      assertTrue(objetNonConformeDao
         .findByObjetEntiteAndType(e1.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(3)).size() == 1);
      assertTrue(objetNonConformeDao
         .findByObjetEntiteAndType(e1.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(2)).size() == 1);
      assertFalse(e1.getConformeTraitement());
      assertFalse(e1.getConformeCession());

      echantillonManager.removeObjectManager(e1, null, u, null);
      echantillonManager.removeObjectManager(e2, null, u, null);
      echantillonManager.removeObjectManager(e0, null, u, null);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(e1);
      fs.add(e2);
      fs.add(e0);
      cleanUpFantomes(fs);
      assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
      assertTrue(objetNonConformeDao.findAll().size() == 6);

   }

   @Test
   public void testPrepareJDBC() throws ParseException, SQLException{

      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      final EchantillonJdbcSuite jdbcSuite = new EchantillonJdbcSuite();

      try{
         conn = DataSourceUtils.getConnection(dataSource);
         stmt = conn.createStatement();
         rs = stmt.executeQuery("select max(echantillon_id) from ECHANTILLON");
         rs.first();
         jdbcSuite.setMaxEchantillonId(rs.getInt(1));
         final Integer maxEchId = jdbcSuite.getMaxEchantillonId();
         rs.close();
         rs = stmt.executeQuery("select max(annotation_valeur_id) from ANNOTATION_VALEUR");
         rs.first();
         jdbcSuite.setMaxAnnotationValeurId(rs.getInt(1));
         final Integer maxAnnoId = jdbcSuite.getMaxAnnotationValeurId();
         rs.close();
         rs = stmt.executeQuery("select max(code_assigne_id) from CODE_ASSIGNE");
         rs.first();
         jdbcSuite.setMaxCodeAssigneId(rs.getInt(1));
         final Integer maxCdId = jdbcSuite.getMaxCodeAssigneId();
         rs.close();
         rs = stmt.executeQuery("select max(objet_non_conforme_id) from OBJET_NON_CONFORME");
         rs.first();
         jdbcSuite.setMaxObjetNonConformeId(rs.getInt(1));
         final Integer maxOncId = jdbcSuite.getMaxObjetNonConformeId();
         rs.close();

         final String sql = "insert into ECHANTILLON (ECHANTILLON_ID, " + "BANQUE_ID, ECHANTILLON_TYPE_ID, OBJET_STATUT_ID, "
            + "PRELEVEMENT_ID, COLLABORATEUR_ID, QUANTITE_UNITE_ID, " + "ECHAN_QUALITE_ID, MODE_PREPA_ID, EMPLACEMENT_ID, "
            + "CODE, DATE_STOCK, QUANTITE, QUANTITE_INIT, " + "LATERALITE, DELAI_CGL, " + "TUMORAL, STERILE, "
            + "CONFORME_TRAITEMENT, CONFORME_CESSION, " + "ETAT_INCOMPLET, ARCHIVE) "
            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
         jdbcSuite.setPstmt(conn.prepareStatement(sql));

         final String sql2 = "insert into OPERATION (UTILISATEUR_ID, " + "OBJET_ID, ENTITE_ID, OPERATION_TYPE_ID, " + "DATE_, V1)"
            + "values (?,?,?,?,?,?)";
         jdbcSuite.setPstmtOp(conn.prepareStatement(sql2));

         final String sql3 = "insert into CODE_ASSIGNE (CODE_ASSIGNE_ID, ECHANTILLON_ID, "
            + "CODE, LIBELLE, CODE_REF_ID, TABLE_CODAGE_ID, IS_ORGANE, " + "IS_MORPHO, ORDRE, EXPORT) "
            + "values (?,?,?,?,?,?,?,?,?,?)";
         jdbcSuite.setPstmtCd(conn.prepareStatement(sql3));

         final String sql4 =
            "insert into ANNOTATION_VALEUR (ANNOTATION_VALEUR_ID, " + "CHAMP_ANNOTATION_ID, OBJET_ID, BANQUE_ID, "
               + "ALPHANUM, BOOL, ANNO_DATE, " + "TEXTE, ITEM_ID) " + "values (?,?,?,?,?,?,?,?,?)";
         jdbcSuite.setPstmtAnno(conn.prepareStatement(sql4));

         final String sql5 = "insert into OBJET_NON_CONFORME (OBJET_NON_CONFORME_ID, "
            + "OBJET_ID, ENTITE_ID, NON_CONFORMITE_ID) " + "values (?,?,?,?)";
         jdbcSuite.setPstmtNc(conn.prepareStatement(sql5));

         // Récupération des objets associés à un produit dérivé
         final Banque banque = banqueManager.findByIdManager(1);
         final Prelevement prelevement = prelevementDao.findById(1);
         final Collaborateur collab = collaborateurManager.findByIdManager(1);
         final ObjetStatut nonstocke = objetStatutManager.findByIdManager(4);
         //Emplacement emplacement = emplacementManager.findByIdManager(1);
         final EchantillonType type = echantillonTypeManager.findByIdManager(1);
         final Unite quantite = uniteManager.findByIdManager(1);
         final EchanQualite qualite = echanQualiteManager.findByIdManager(1);
         final ModePrepa prepa = modePrepaManager.findByIdManager(1);
         final Utilisateur utilisateur = utilisateurDao.findById(1);

         // Insertion
         final Echantillon echan1 = new Echantillon();
         echan1.setCode("Code");

         Boolean catched = false;
         // on test l'insertion avec la banque nulle
         try{
            echantillonManager.prepareObjectJDBCManager(jdbcSuite, echan1, null, null, null, null, null, type, null, null, null,
               null, null, null, null, null, true, false, new ArrayList<Integer>());
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
               catched = true;
            }
         }
         assertTrue(catched);
         catched = false;

         // assertTrue(stmt.isClosed());
         // assertTrue(pstmt.isClosed());
         // assertTrue(pstmtOp.isClosed());

         // on test l'insertion avec le type nul
         try{
            echantillonManager.prepareObjectJDBCManager(jdbcSuite, echan1, banque, null, null, null, null, null, null, null, null,
               null, null, null, null, null, true, false, new ArrayList<Integer>());
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
               catched = true;
            }
         }
         assertTrue(catched);
         catched = false;

         // on test l'insertion avec le statut null
         try{
            echantillonManager.prepareObjectJDBCManager(jdbcSuite, echan1, banque, null, null, null, null, type, null, null, null,
               null, null, null, null, null, true, false, new ArrayList<Integer>());
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
               catched = true;
            }
         }
         assertTrue(catched);
         catched = false;

         // on test l'insertion avec utilistateur null
         try{
            echantillonManager.prepareObjectJDBCManager(jdbcSuite, echan1, banque, null, null, nonstocke, null, type, null, null,
               null, null, null, null, null, null, true, false, new ArrayList<Integer>());
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
               catched = true;
            }
         }
         assertTrue(catched);
         catched = false;

         echan1.setCode("PTRA.1");
         // On teste l'insertion d'un doublon pour vérifier que l'exception
         // est lancée
         try{
            echantillonManager.prepareObjectJDBCManager(jdbcSuite, echan1, banque, null, null, nonstocke, null, type, null, null,
               null, null, null, null, null, utilisateur, true, false, new ArrayList<Integer>());
         }catch(final Exception e){
            // e.printStackTrace();
            if(e.getClass().getSimpleName().equals("DoublonFoundException")){
               catched = true;
            }
         }
         assertTrue(catched);

         // Emplacement occupé par un dérivé
         echan1.setCode("PTRA.3");
         final ObjetStatut stocke = objetStatutManager.findByIdManager(1);
         final Emplacement empl = emplacementManager.findByIdManager(2);
         catched = false;
         try{
            echantillonManager.prepareObjectJDBCManager(jdbcSuite, echan1, banque, null, null, stocke, empl, type, null, null,
               null, null, null, null, null, utilisateur, true, false, new ArrayList<Integer>());
         }catch(final TKException ex){
            catched = true;
            assertTrue(ex.getMessage().equals("PTRA.3 : error.emplacement.notEmpty"));
            assertTrue(ex.getIdentificationObjetException().equals("PTRA.3"));
         }
         assertTrue(catched);

         // Test de la validation lors de la création
         validationInsert(echan1);
         // on teste une insertion valide avec les associations 
         // non obigatoires nulles et en sautant la validation
         echan1.setCode("PTRA.3");
         echan1.setLateralite(null);

         // id non changé
         assertTrue(jdbcSuite.getMaxEchantillonId().equals(maxEchId));
         assertTrue(jdbcSuite.getMaxAnnotationValeurId().equals(maxAnnoId));
         assertTrue(jdbcSuite.getMaxCodeAssigneId().equals(maxCdId));
         assertTrue(jdbcSuite.getMaxObjetNonConformeId().equals(maxOncId));

         // teste rollback à partir d'une erreur de codes assignes
         final List<CodeAssigne> codes = new ArrayList<>();

         // c3 erreurs pour tester rollback
         final CodeAssigne c3 = new CodeAssigne();
         c3.setCode("c3");
         c3.setIsOrgane(true);
         c3.setIsMorpho(true);
         c3.setOrdre(1);
         c3.setExport(true);
         codes.add(c3);

         catched = false;
         try{
            echantillonManager.prepareObjectJDBCManager(jdbcSuite, echan1, banque, null, null, stocke, null, type, null, null,
               null, codes, null, null, null, utilisateur, true, false, new ArrayList<Integer>());
         }catch(final ValidationException ex){
            catched = true;
         }
         assertTrue(catched);
         assertTrue(echantillonManager.findAllObjectsManager().size() == 4);

         // id non changé
         assertTrue(jdbcSuite.getMaxEchantillonId().equals(maxEchId));
         assertTrue(jdbcSuite.getMaxAnnotationValeurId().equals(maxAnnoId));
         assertTrue(jdbcSuite.getMaxCodeAssigneId().equals(maxCdId));
         assertTrue(jdbcSuite.getMaxObjetNonConformeId().equals(maxOncId));

         // clean before insert
         jdbcSuite.getPstmt().clearBatch();
         jdbcSuite.getPstmtCd().clearBatch();
         jdbcSuite.getPstmtAnno().clearBatch();
         jdbcSuite.getPstmtNc().clearBatch();
         jdbcSuite.getPstmtOp().clearBatch();

         echantillonManager.prepareObjectJDBCManager(jdbcSuite, echan1, banque, null, null, nonstocke, null, type, null, null,
            null, null, new ArrayList<AnnotationValeur>(), null, null, utilisateur, true, false, new ArrayList<Integer>());

         // ids changés
         assertTrue(jdbcSuite.getMaxEchantillonId().equals(maxEchId + 1));
         assertTrue(jdbcSuite.getMaxAnnotationValeurId().equals(maxAnnoId));
         assertTrue(jdbcSuite.getMaxCodeAssigneId().equals(maxCdId));
         assertTrue(jdbcSuite.getMaxObjetNonConformeId().equals(maxOncId));

         // on teste une insertion avec les associations 
         // non obigatoires remplies 
         final Echantillon echan2 = new Echantillon();
         final float num1 = (float) 15.1111;
         final float num2 = (float) 15.2555;
         echan2.setCode("PTRA.4");
         echan2.setQuantite(num1);
         echan2.setQuantiteInit(num2);
         echan2.setDelaiCgl((float) 1.0);
         echan2.setSterile(false);
         // echan2.setConformeTraitement(false);
         echan2.setConformeCession(true);
         echan2.setLateralite("G");
         echan2.setTumoral(false);
         final Calendar cal = Calendar.getInstance();
         cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2012"));
         echan2.setDateStock(cal);

         final Emplacement emp = new Emplacement();
         emp.setTerminale(terminaleDao.findById(6));
         emp.setPosition(3);
         emp.setObjetId(echan2.getEchantillonId());
         emp.setEntite(entiteDao.findById(3));
         // emp.setVide(false);

         final List<AnnotationValeur> avs = new ArrayList<>();
         final AnnotationValeur av1 = new AnnotationValeur();
         av1.setAlphanum("1.36");
         av1.setChampAnnotation(champAnnotationDao.findById(7));
         avs.add(av1);
         final AnnotationValeur av2 = new AnnotationValeur();
         av2.setBool(false);
         av2.setChampAnnotation(champAnnotationDao.findById(4));
         avs.add(av2);

         final List<NonConformite> ncfsTrait = new ArrayList<>();
         final List<NonConformite> ncfsCess = new ArrayList<>();
         ncfsTrait.add(nonConformiteDao.findById(4));
         ncfsCess.add(nonConformiteDao.findById(6));
         ncfsCess.add(nonConformiteDao.findById(7));

         // c1 toutes les valeurs assignees
         final CodeAssigne c1 = new CodeAssigne();
         c1.setCode("c1");
         c1.setLibelle("lib1");
         c1.setIsOrgane(true);
         c1.setIsMorpho(false);
         c1.setOrdre(1);
         c1.setExport(true);
         c1.setTableCodage(tableCodageDao.findById(1));
         c1.setCodeRefId(2);
         codes.add(c1);

         // c2 seules les valeurs obligatoires
         final CodeAssigne c2 = new CodeAssigne();
         c2.setCode("c2");
         c2.setIsOrgane(true);
         c2.setOrdre(2);
         c2.setExport(false);
         codes.add(c2);

         // c3 
         c3.setIsOrgane(false);

         echantillonManager.prepareObjectJDBCManager(jdbcSuite, echan2, banque, prelevement, collab, stocke, emp, type, quantite,
            qualite, prepa, codes, avs, ncfsTrait, ncfsCess, utilisateur, true, false, new ArrayList<Integer>());

         // ids changés
         assertTrue(jdbcSuite.getMaxEchantillonId().equals(maxEchId + 2));
         assertTrue(jdbcSuite.getMaxAnnotationValeurId().equals(maxAnnoId + 2));
         assertTrue(jdbcSuite.getMaxCodeAssigneId().equals(maxCdId + 3));
         assertTrue(jdbcSuite.getMaxObjetNonConformeId().equals(maxOncId + 3));

         jdbcSuite.getPstmt().executeBatch();
         jdbcSuite.getPstmtOp().executeBatch();
         jdbcSuite.getPstmtCd().executeBatch();
         jdbcSuite.getPstmtAnno().executeBatch();
         jdbcSuite.getPstmtNc().executeBatch();

         // asserts
         // verif premier echantillon
         assertTrue(echantillonManager.findByCodeLikeManager("PTRA.3", true).size() == 1);
         Echantillon testInsert = echantillonManager.findByCodeLikeManager("PTRA.3", true).get(0);
         assertTrue(testInsert.getBanque().equals(banque));
         assertTrue(testInsert.getObjetStatut().equals(nonstocke));
         assertTrue(testInsert.getEchantillonType().equals(type));
         assertNull(testInsert.getDelaiCgl());
         assertNull(testInsert.getQuantite());
         assertNull(testInsert.getQuantiteInit());
         assertNull(testInsert.getTumoral());
         assertNull(testInsert.getSterile());
         assertNull(testInsert.getConformeTraitement());
         assertNull(testInsert.getConformeCession());
         assertNull(testInsert.getLateralite());

         assertTrue(annotationValeurManager.findByObjectManager(testInsert).isEmpty());
         assertTrue(objetNonConformeDao.findByObjetAndEntite(testInsert.getEchantillonId(), entiteDao.findById(3)).isEmpty());
         assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(testInsert).isEmpty());
         assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(testInsert).isEmpty());

         final Operation op = getOperationManager().findByObjectManager(testInsert).get(0);
         assertTrue(op.getUtilisateur().equals(utilisateur));
         assertTrue(op.getOperationType().getNom().equals("Creation"));
         assertFalse(op.getV1());

         assertTrue(echantillonManager.findByCodeLikeManager("PTRA.4", true).size() == 1);

         // On vérifie que toutes associations ont étées enregistrées
         testInsert = echantillonManager.findByCodeLikeManager("PTRA.4", true).get(0);
         assertTrue(echantillonManager.getPrelevementManager(testInsert).equals(prelevement));
         assertTrue(testInsert.getCollaborateur().equals(collab));
         assertTrue(echantillonManager.getEmplacementManager(testInsert).equals(emp));
         assertTrue(!echantillonManager.getEmplacementManager(testInsert).getVide());
         assertTrue(testInsert.getQuantiteUnite().equals(quantite));
         assertTrue(testInsert.getEchanQualite().equals(qualite));
         assertTrue(testInsert.getModePrepa().equals(prepa));
         assertTrue(testInsert.getQuantite() == (float) 15.111);
         assertTrue(testInsert.getQuantiteInit() == (float) 15.255);
         assertTrue(testInsert.getDelaiCgl() > 0);
         assertFalse(testInsert.getSterile());
         assertTrue(testInsert.getLateralite().equals("G"));
         assertFalse(testInsert.getTumoral());
         assertTrue(testInsert.getDateStock().equals(cal));
         assertFalse(testInsert.getConformeTraitement());
         assertFalse(testInsert.getConformeCession());

         assertTrue(annotationValeurManager.findByObjectManager(testInsert).size() == 2);
         assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), testInsert).get(0)
            .getAlphanum().equals("1.36"));
         assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(4), testInsert).get(0)
            .getBool().equals(false));

         assertTrue(getOperationManager().findByObjectManager(testInsert).size() == 2);
         assertTrue(getOperationManager().findByObjectManager(testInsert).get(0).getOperationType().getNom().equals("Creation"));
         assertTrue(
            getOperationManager().findByObjectManager(testInsert).get(1).getOperationType().getNom().equals("Annotation"));

         assertTrue(objetNonConformeDao.findByObjetAndEntite(testInsert.getEchantillonId(), entiteDao.findById(3)).size() == 3);

         assertTrue(objetNonConformeDao
            .findByObjetEntiteAndType(testInsert.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(3))
            .size() == 2);
         assertTrue(objetNonConformeDao
            .findByObjetEntiteAndType(testInsert.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(3)).get(0)
            .getNonConformite().equals(nonConformiteDao.findById(6))
            || objetNonConformeDao
               .findByObjetEntiteAndType(testInsert.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(3))
               .get(1).getNonConformite().equals(nonConformiteDao.findById(6)));
         assertTrue(objetNonConformeDao
            .findByObjetEntiteAndType(testInsert.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(3)).get(0)
            .getNonConformite().equals(nonConformiteDao.findById(7))
            || objetNonConformeDao
               .findByObjetEntiteAndType(testInsert.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(3))
               .get(1).getNonConformite().equals(nonConformiteDao.findById(7)));

         assertTrue(objetNonConformeDao
            .findByObjetEntiteAndType(testInsert.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(2))
            .size() == 1);
         assertTrue(objetNonConformeDao
            .findByObjetEntiteAndType(testInsert.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(2)).get(0)
            .getNonConformite().equals(nonConformiteDao.findById(4)));

         assertFalse(testInsert.getConformeTraitement());
         assertFalse(testInsert.getConformeCession());

         // codes
         assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(testInsert).size() == 2);
         assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(testInsert).size() == 1);

         // Suppression
         final Echantillon echan3 = echantillonManager.findByCodeLikeManager("PTRA.3", true).get(0);
         echantillonManager.removeObjectManager(echan3, null, utilisateur, null);
         final Echantillon echan4 = echantillonManager.findByCodeLikeManager("PTRA.4", true).get(0);
         echantillonManager.removeObjectManager(echan4, null, utilisateur, null);
         assertTrue(getOperationManager().findByObjectManager(echan1).size() == 0);
         assertTrue(getOperationManager().findByObjectManager(echan2).size() == 0);

         final List<TKFantomableObject> fs = new ArrayList<>();
         fs.add(echan3);
         fs.add(echan4);
         cleanUpFantomes(fs);
      }catch(final CannotGetJdbcConnectionException e){
         e.printStackTrace();
      }finally{
         if(null != stmt)
         stmt.close();
         if(null != rs)
         rs.close();
         if(null != conn)
         conn.close();
         jdbcSuite.closePs();
      }
   }

   @Test
   public void testRemoveListFromIds() throws ParseException{
      final Banque b2 = banqueDao.findById(2);
      final Utilisateur u2 = utilisateurDao.findById(2);
      final EchantillonType et1 = echantillonTypeManager.findByIdManager(1);
      final List<Integer> ids = new ArrayList<>();

      // null list
      echantillonManager.removeListFromIdsManager(null, "", u2);
      // empty list
      echantillonManager.removeListFromIdsManager(ids, null, u2);
      // non existing ids
      ids.add(22);
      ids.add(33);
      echantillonManager.removeListFromIdsManager(ids, "test", u2);

      testFindAll();

      new File("/tmp/" + "pt_" + b2.getPlateforme().getPlateformeId() + "/" + "coll_" + b2.getBanqueId() + "/cr_anapath/")
         .mkdirs();

      final Fichier cr = new Fichier();
      cr.setNom("anapath1");
      final String tmp = "Echantillon diagnostic Veronica virus";
      final byte[] byteArray = tmp.getBytes();
      final ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);

      final List<File> filesCreated = new ArrayList<>();

      final Echantillon ech1 = new Echantillon();
      ech1.setCode("ECH1");
      echantillonManager.createObjectWithCrAnapathManager(ech1, b2, null, null, objetStatutManager.findByIdManager(1), null, et1,
         null, null, null, null, cr, bais, filesCreated, null, u2, false, "/tmp/", false);
      assertTrue(new File(ech1.getCrAnapath().getPath()).exists());
      final Echantillon ech2 = new Echantillon();
      ech2.setCode("ECH2");
      echantillonManager.createObjectManager(ech2, b2, null, null, objetStatutManager.findByIdManager(1), null, et1, null, null,
         null, null, null, filesCreated, u2, false, null, false);
      final Echantillon ech3 = new Echantillon();
      ech3.setCode("ECH3");
      echantillonManager.createObjectManager(ech3, b2, null, null, objetStatutManager.findByIdManager(1), null, et1, null, null,
         null, null, null, filesCreated, u2, false, null, false);

      assertTrue(echantillonManager.findByCodeLikeWithBanqueManager("ECH_", b2, false).size() == 3);

      ids.add(ech1.getEchantillonId());
      ids.add(ech2.getEchantillonId());
      ids.add(ech3.getEchantillonId());
      final Integer cessedEchanId = new Integer(1);
      ids.add(cessedEchanId); // cet echantillon est cede
      // rollback used object Exception
      boolean catched = false;
      try{
         echantillonManager.removeListFromIdsManager(ids, "test suppr ids", u2);
      }catch(final ObjectUsedException oe){
         catched = true;
         assertTrue(oe.getMessage().equals("echantillon.cascade.isCessed"));
      }
      assertTrue(catched);
      assertTrue(echantillonManager.findByCodeLikeWithBanqueManager("ECH_", b2, false).size() == 3);

      // fichier non supprime
      assertTrue(new File(ech1.getCrAnapath().getPath()).exists());

      ids.remove(cessedEchanId);
      echantillonManager.removeListFromIdsManager(ids, "test suppr ids", u2);
      assertTrue(echantillonManager.findByCodeLikeWithBanqueManager("ECH_", b2, false).isEmpty());

      assertFalse(new File(ech1.getCrAnapath().getPath()).exists());

      assertTrue(getFantomeDao().findByNom("ECH3").get(0).getCommentaires().equals("test suppr ids"));

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(ech1);
      fs.add(ech2);
      fs.add(ech3);
      cleanUpFantomes(fs);

      testFindAll();
   }

   /**
    * @since 2.1
    */
   @Test
   public void testFindByCodeInPlateformeBanqueManager(){
      final Plateforme p1 = plateformeDao.findById(1);
      List<Echantillon> echans = echantillonManager.findByCodeInPlateformeManager("PTRA.1", p1);
      assertTrue(echans.size() == 1);
      echans = echantillonManager.findByCodeInPlateformeManager("%", p1);
      assertTrue(echans.size() == 4);
      echans = echantillonManager.findByCodeInPlateformeManager(null, p1);
      assertTrue(echans.size() == 0);
      echans = echantillonManager.findByCodeInPlateformeManager("%", null);
      assertTrue(echans.size() == 0);
   }
}
