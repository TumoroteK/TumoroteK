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
package fr.aphp.tumorotek.manager.test.coeur.prodderive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ModePrepaDeriveDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.qualite.ConformiteTypeDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.dao.qualite.ObjetNonConformeDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteTypeDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.ObjetStatutManager;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveValidator;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdQualiteManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdTypeManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.context.CollaborateurManager;
import fr.aphp.tumorotek.manager.exception.DeriveBatchSaveException;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectStatutException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.io.ExtractValueFromChampManager;
import fr.aphp.tumorotek.manager.qualite.OperationTypeManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.systeme.UniteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.utilisateur.ReservationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.EnceinteType;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Reservation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager ProdDeriveManager.
 * Classe créée le 02/10/09.
 *
 * @author Pierre Ventadour.
 * @version 2.1
 *
 */
public class ProdDeriveManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ProdDeriveManager prodDeriveManager;
   @Autowired
   private BanqueManager banqueManager;
   @Autowired
   private ProdTypeManager prodTypeManager;
   @Autowired
   private ObjetStatutManager objetStatutManager;
   @Autowired
   private CollaborateurManager collaborateurManager;
   @Autowired
   private UniteManager uniteManager;
   @Autowired
   private ProdQualiteManager prodQualiteManager;
   @Autowired
   private TransformationManager transformationManager;
   @Autowired
   private ReservationManager reservationManager;
   @Autowired
   private EchantillonManager echantillonManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private EntiteManager entiteManager;
   @Autowired
   private ProdDeriveValidator prodDeriveValidator;
   @Autowired
   private ChampAnnotationDao champAnnotationDao;
   @Autowired
   private AnnotationValeurManager annotationValeurManager;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private PrelevementDao prelevementDao;
   @Autowired
   private OperationTypeManager operationTypeManager;
   @Autowired
   private CederObjetManager cederObjetManager;
   @Autowired
   private CessionDao cessionDao;
   @Autowired
   private ModePrepaDeriveDao modePrepaDeriveDao;
   @Autowired
   private TerminaleDao terminaleDao;
   @Autowired
   private ConteneurManager conteneurManager;
   @Autowired
   private EnceinteManager enceinteManager;
   @Autowired
   private TerminaleManager terminaleManager;
   @Autowired
   private EnceinteTypeDao enceinteTypeDao;
   @Autowired
   private EmplacementManager emplacementManager;
   @Autowired
   private ChampEntiteDao champEntiteDao;
   @Autowired
   private ExtractValueFromChampManager extractValueFromChampManager;
   @Autowired
   private OperationTypeDao operationTypeDao;
   @Autowired
   private RetourManager retourManager;
   @Autowired
   private NonConformiteDao nonConformiteDao;
   @Autowired
   private ObjetNonConformeDao objetNonConformeDao;
   @Autowired
   private ConformiteTypeDao conformiteTypeDao;
   @Autowired
   private PrelevementManager prelevementManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public ProdDeriveManagerTest(){}

   @Test
   public void testFindById(){
      final ProdDerive derive = prodDeriveManager.findByIdManager(1);
      assertNotNull(derive);
      assertTrue(derive.getCode().equals("PTRA.1.1"));

      final ProdDerive deriveNull = prodDeriveManager.findByIdManager(10);
      assertNull(deriveNull);
   }

   @Test
   public void testFindAll(){
      final List<ProdDerive> list = prodDeriveManager.findAllObjectsManager();
      assertTrue(list.size() == 4);
   }

   @Test
   public void testFindByIdsInListManager(){
      List<Integer> ids = new ArrayList<>();
      ids.add(1);
      ids.add(2);
      ids.add(3);
      ids.add(10);
      List<ProdDerive> liste = prodDeriveManager.findByIdsInListManager(ids);
      assertTrue(liste.size() == 3);

      ids = new ArrayList<>();
      liste = prodDeriveManager.findByIdsInListManager(ids);
      assertTrue(liste.size() == 0);

      liste = prodDeriveManager.findByIdsInListManager(null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindByBanquesManager(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      List<ProdDerive> res = prodDeriveManager.findByBanquesManager(banks);
      assertTrue(res.size() == 4);
      banks.clear();
      res = prodDeriveManager.findByBanquesManager(banks);
      assertTrue(res.size() == 0);
      res = prodDeriveManager.findByBanquesManager(null);
      assertTrue(res.size() == 0);
   }

   @Test
   public void testFindAllObjectsIdsByBanquesManager(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      List<Integer> res = prodDeriveManager.findAllObjectsIdsByBanquesManager(banks);
      assertTrue(res.size() == 4);
      banks.clear();
      res = prodDeriveManager.findAllObjectsIdsByBanquesManager(banks);
      assertTrue(res.size() == 0);
      res = prodDeriveManager.findAllObjectsIdsByBanquesManager(null);
      assertTrue(res.size() == 0);
   }

   /**
    * Test la méthode findByDateStockAfterDateManager.
    * @throws Exception Lance une exception en cas d'erreur sur une date.
    */
   @Test
   public void testFindByDateStockAfterDateManager() throws Exception{
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2009");
      List<ProdDerive> list = prodDeriveManager.findByDateStockAfterDateManager(search);
      assertTrue(list.size() == 4);

      search = new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2009");
      list = prodDeriveManager.findByDateStockAfterDateManager(search);
      assertTrue(list.size() == 2);

      list = prodDeriveManager.findByDateStockAfterDateManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByDateTransfoAfterDateManager.
    * @throws Exception Lance une exception en cas d'erreur sur une date.
    */
   @Test
   public void testFindByDateTransfoAfterDateManager() throws Exception{
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2009");
      List<ProdDerive> list = prodDeriveManager.findByDateTransfoAfterDateManager(search);
      assertTrue(list.size() == 4);

      search = new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2009");
      list = prodDeriveManager.findByDateTransfoAfterDateManager(search);
      assertTrue(list.size() == 2);

      list = prodDeriveManager.findByDateTransfoAfterDateManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByDateTransformationAfterDateWithBanqueManager.
    * @throws Exception Lance une exception en cas d'erreur sur une date.
    */
   @Test
   public void testFindByDateTransformationAfterDateWithBanqueManager() throws Exception{
      final Banque b1 = banqueManager.findByIdManager(1);
      final Banque b2 = banqueManager.findByIdManager(2);
      final List<Banque> banks = new ArrayList<>();

      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2009");
      List<ProdDerive> list = prodDeriveManager.findByDateTransformationAfterDateWithBanqueManager(search, banks);
      assertTrue(list.size() == 0);

      banks.add(b1);
      search = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2009");
      list = prodDeriveManager.findByDateTransformationAfterDateWithBanqueManager(search, banks);
      assertTrue(list.size() == 3);

      banks.add(b2);
      list = prodDeriveManager.findByDateTransformationAfterDateWithBanqueManager(search, banks);
      assertTrue(list.size() == 4);

      list = prodDeriveManager.findByDateTransformationAfterDateWithBanqueManager(search, null);
      assertTrue(list.size() == 0);

      search = new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2009");
      list = prodDeriveManager.findByDateTransformationAfterDateWithBanqueManager(search, banks);
      assertTrue(list.size() == 2);

      list = prodDeriveManager.findByDateTransformationAfterDateWithBanqueManager(null, banks);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByDateTransformationAfterDateWithBanqueManager(null, null);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindLastCreationManager(){
      final Banque b1 = banqueManager.findByIdManager(1);
      final Banque b2 = banqueManager.findByIdManager(2);
      final Banque b3 = banqueManager.findByIdManager(3);
      final List<Banque> banks = new ArrayList<>();

      List<ProdDerive> list = prodDeriveManager.findLastCreationManager(banks, 5);
      assertTrue(list.size() == 0);

      banks.add(b1);
      list = prodDeriveManager.findLastCreationManager(banks, 5);
      assertTrue(list.size() == 3);

      list = prodDeriveManager.findLastCreationManager(banks, 1);
      assertTrue(list.size() == 1);

      banks.add(b2);

      list = prodDeriveManager.findLastCreationManager(banks, 5);
      assertTrue(list.size() == 4);

      list = prodDeriveManager.findLastCreationManager(banks, 0);
      assertTrue(list.size() == 0);

      banks.add(b3);
      list = prodDeriveManager.findLastCreationManager(banks, 10);
      assertTrue(list.size() == 4);

      list = prodDeriveManager.findLastCreationManager(null, 10);
      assertTrue(list.size() == 0);

   }

   @Test
   public void testFindByCodeLikeExactManager(){
      List<ProdDerive> list = prodDeriveManager.findByCodeLikeManager("PTRA.1.1", true);
      assertTrue(list.size() == 1);

      list = prodDeriveManager.findByCodeLikeManager("PTRA", true);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindByCodeLikeManager(){
      List<ProdDerive> list = prodDeriveManager.findByCodeLikeManager("PTRA.1.1", false);
      assertTrue(list.size() == 1);

      list = prodDeriveManager.findByCodeLikeManager("PTRA", false);
      assertTrue(list.size() == 2);

      list = prodDeriveManager.findByCodeLikeManager("PTRA%", false);
      assertTrue(list.size() == 2);

      list = prodDeriveManager.findByCodeLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindAllCodesForBanqueManager(){
      final Banque b = banqueManager.findByIdManager(1);
      List<String> codes = prodDeriveManager.findAllCodesForBanqueManager(b);
      assertTrue(codes.size() == 3);
      assertTrue(codes.get(1).equals("PTRA.1.2"));

      final Banque b2 = banqueManager.findByIdManager(2);
      codes = prodDeriveManager.findAllCodesForBanqueManager(b2);
      assertTrue(codes.size() == 1);

      codes = prodDeriveManager.findAllCodesForBanqueManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindAllCodesForBanqueAndQuantiteManager(){
      final Banque b = banqueManager.findByIdManager(1);
      List<String> codes = prodDeriveManager.findAllCodesForBanqueAndQuantiteManager(b);
      assertTrue(codes.size() == 2);
      assertTrue(codes.get(1).equals("PTRA.1.2"));

      final Banque b2 = banqueManager.findByIdManager(2);
      codes = prodDeriveManager.findAllCodesForBanqueAndQuantiteManager(b2);
      assertTrue(codes.size() == 0);

      codes = prodDeriveManager.findAllCodesForBanqueAndQuantiteManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindAllCodesForBanqueAndStockesManager(){
      final Banque b = banqueManager.findByIdManager(1);
      List<String> codes = prodDeriveManager.findAllCodesForBanqueAndStockesManager(b);
      assertTrue(codes.size() == 2);
      assertTrue(codes.get(0).equals("PTRA.1.1"));

      final Banque b2 = banqueManager.findByIdManager(2);
      codes = prodDeriveManager.findAllCodesForBanqueAndStockesManager(b2);
      assertTrue(codes.size() == 1);

      final Banque b3 = banqueManager.findByIdManager(3);
      codes = prodDeriveManager.findAllCodesForBanqueAndStockesManager(b3);
      assertTrue(codes.size() == 0);

      codes = prodDeriveManager.findAllCodesForBanqueAndStockesManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindAllCodesForMultiBanquesAndStockesManager(){
      List<Banque> banques = new ArrayList<>();
      final Banque b1 = banqueManager.findByIdManager(1);
      final Banque b2 = banqueManager.findByIdManager(2);
      banques.add(b1);
      banques.add(b2);
      List<String> codes = prodDeriveManager.findAllCodesForMultiBanquesAndStockesManager(banques);
      assertTrue(codes.size() == 3);

      banques = new ArrayList<>();
      final Banque b3 = banqueManager.findByIdManager(3);
      banques.add(b3);
      codes = prodDeriveManager.findAllCodesForMultiBanquesAndStockesManager(banques);
      assertTrue(codes.size() == 0);

      codes = prodDeriveManager.findAllCodesForMultiBanquesAndStockesManager(null);
      assertTrue(codes.size() == 0);
   }

   @Test
   public void testFindByCodeOrLaboWithBanqueManager(){
      final Banque b1 = banqueManager.findByIdManager(1);
      final Banque b2 = banqueManager.findByIdManager(2);

      List<ProdDerive> list = prodDeriveManager.findByCodeOrLaboWithBanqueManager("PTRA.1.1", b1, false);
      assertTrue(list.size() == 1);

      list = prodDeriveManager.findByCodeOrLaboWithBanqueManager(

         "PTRA.1.1", b1, true);
      assertTrue(list.size() == 1);

      list = prodDeriveManager.findByCodeOrLaboWithBanqueManager("PTRA.1.1", b2, false);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboWithBanqueManager("PTRA.1.1", b2, true);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboWithBanqueManager("PTRA.1.1", null, false);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboWithBanqueManager("PTRA.1.1", null, true);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboWithBanqueManager("PTRA", b1, false);
      assertTrue(list.size() == 2);

      list = prodDeriveManager.findByCodeOrLaboWithBanqueManager("PTRA", b2, false);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboWithBanqueManager("PTRA", null, false);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboWithBanqueManager("PTRA", b1, true);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboWithBanqueManager(null, b1, false);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboWithBanqueManager(null, null, false);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindByCodeOrLaboBothSideWithBanqueReturnIdsManager(){
      final Banque b1 = banqueManager.findByIdManager(1);
      final Banque b2 = banqueManager.findByIdManager(2);
      final List<Banque> banks = new ArrayList<>();

      List<Integer> list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager("PTRA.1.1", banks, false);
      assertTrue(list.size() == 0);

      banks.add(b1);
      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager("PTRA.1.1", banks, false);
      assertTrue(list.size() == 1);

      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager("PTRA.1.1", banks, true);
      assertTrue(list.size() == 1);

      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager("", banks, false);
      assertTrue(list.size() == 3);

      banks.add(b2);
      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager("", banks, false);
      assertTrue(list.size() == 4);

      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager("EHT.1.1", banks, true);
      assertTrue(list.size() == 1);

      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager("PTRA.1.1", null, false);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager("PTRA.1.1", null, true);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager("TRA", banks, false);
      assertTrue(list.size() == 2);

      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager("PTRA", null, false);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager("PTRA", banks, true);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager(null, banks, false);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager(null, null, false);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindByPatientNomReturnIdsManager(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      List<Integer> list = prodDeriveManager.findByPatientNomReturnIdsManager("DELPHINO", banks, true);
      assertTrue(list.size() == 2);

      list = prodDeriveManager.findByPatientNomReturnIdsManager("ELPH", banks, false);
      assertTrue(list.size() == 2);

      list = prodDeriveManager.findByPatientNomReturnIdsManager("ELPH", banks, true);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByPatientNomReturnIdsManager("%ELPH%", banks, true);
      assertTrue(list.size() == 2);

      list = prodDeriveManager.findByPatientNomReturnIdsManager("MAYER", banks, false);
      assertTrue(list.size() == 0);

      banks.add(b2);
      list = prodDeriveManager.findByPatientNomReturnIdsManager("MAYER", banks, true);
      assertTrue(list.size() == 1);

      list = prodDeriveManager.findByPatientNomReturnIdsManager("JACKSON", banks, false);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByPatientNomReturnIdsManager(null, banks, false);
      assertTrue(list.size() == 0);

      list = prodDeriveManager.findByPatientNomReturnIdsManager("MAYER", null, true);
      assertTrue(list.size() == 0);

   }

   @Test
   public void testGetProdDerivesManager(){
      final ProdDerive derive1 = prodDeriveManager.findByIdManager(1);
      List<ProdDerive> derives = prodDeriveManager.getProdDerivesManager(derive1);
      assertTrue(derives.size() == 1);

      final ProdDerive derive2 = prodDeriveManager.findByIdManager(2);
      derives = prodDeriveManager.getProdDerivesManager(derive2);
      assertTrue(derives.size() == 0);
   }

   @Test
   public void testGetEmplacementManager(){
      final ProdDerive derive1 = prodDeriveManager.findByIdManager(1);
      final Emplacement empl1 = prodDeriveManager.getEmplacementManager(derive1);
      assertNotNull(empl1);
      assertTrue(empl1.getPosition().equals(1));
      assertFalse(empl1.getVide());

      final ProdDerive derive3 = prodDeriveManager.findByIdManager(3);
      final Emplacement empl3 = prodDeriveManager.getEmplacementManager(derive3);
      assertNull(empl3);

      final Emplacement emplNull = prodDeriveManager.getEmplacementManager(null);
      assertNull(emplNull);
   }

   @Test
   public void testGetEmplacementAdrlManager(){
      final ProdDerive derive1 = prodDeriveManager.findByIdManager(1);
      final String adrl1 = prodDeriveManager.getEmplacementAdrlManager(derive1);
      assertNotNull(adrl1);
      assertTrue(adrl1.equals("CC1.R1.T1.BT1.A-A"));

      final ProdDerive derive3 = prodDeriveManager.findByIdManager(3);
      final String adrl3 = prodDeriveManager.getEmplacementAdrlManager(derive3);
      assertNotNull(adrl3);
      assertTrue(adrl3.equals(""));

      final String adrlNull = prodDeriveManager.getEmplacementAdrlManager(null);
      assertNotNull(adrlNull);
      assertTrue(adrlNull.equals(""));
   }

   /**
    * @version 2.1
    */
   @Test
   public void testFindDoublon(){
      final Banque banque1 = banqueManager.findByIdManager(1);
      final Banque banque2 = banqueManager.findByIdManager(2);
      final ProdDerive derive = new ProdDerive();
      derive.setCode("PTRA.1.1");
      derive.setBanque(banque2);
      assertTrue(prodDeriveManager.findDoublonManager(derive));

      derive.setBanque(banque1);
      assertTrue(prodDeriveManager.findDoublonManager(derive));

      // pf
      derive.setBanque(banqueDao.findById(4));
      assertFalse(derive.equals(prodDeriveManager.findByIdManager(1)));
      assertFalse(prodDeriveManager.findDoublonManager(derive));

      // null
      derive.setBanque(null);
      assertFalse(derive.equals(prodDeriveManager.findByIdManager(1)));
      assertFalse(prodDeriveManager.findDoublonManager(derive));

      derive.setCode("PTRA.1.3");
      assertFalse(prodDeriveManager.findDoublonManager(derive));

      final ProdDerive derive2 = prodDeriveManager.findByIdManager(1);
      assertFalse(prodDeriveManager.findDoublonManager(derive2));

      derive2.setCodeLabo("CODE");
      assertFalse(prodDeriveManager.findDoublonManager(derive2));

      derive2.setCodeLabo(null);
      derive2.setCode("PTRA.1.2");
      assertTrue(prodDeriveManager.findDoublonManager(derive2));
   }

   @Test
   public void testFindAfterDateCreationManager() throws ParseException{

      final Banque banque1 = banqueManager.findByIdManager(1);
      final Banque banque2 = banqueManager.findByIdManager(2);
      final List<Banque> banks = new ArrayList<>();
      //recherche sans banque
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009");
      final Calendar cal = Calendar.getInstance();
      cal.setTime(search);
      List<Integer> liste = prodDeriveManager.findAfterDateCreationReturnIdsManager(cal, banks);
      assertTrue(liste.size() == 0);
      //recherche fructueuse	
      banks.add(banque1);
      search = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009");
      cal.setTime(search);
      liste = prodDeriveManager.findAfterDateCreationReturnIdsManager(cal, banks);
      assertTrue(liste.size() == 1);
      banks.add(banque2);
      search = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009");
      cal.setTime(search);
      liste = prodDeriveManager.findAfterDateCreationReturnIdsManager(cal, banks);
      assertTrue(liste.size() == 2);
      //recherche infructueuse
      search = new SimpleDateFormat("dd/MM/yyyy").parse("08/11/2009");
      cal.setTime(search);
      liste = prodDeriveManager.findAfterDateCreationReturnIdsManager(cal, banks);
      assertTrue(liste.size() == 0);
      //null recherche
      liste = prodDeriveManager.findAfterDateCreationReturnIdsManager(null, banks);
      assertTrue(liste.size() == 0);

      // recherche avec une banque null
      search = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009");
      cal.setTime(search);
      liste = prodDeriveManager.findAfterDateCreationReturnIdsManager(cal, null);
      assertTrue(liste.size() == 0);
      //null recherche
      liste = prodDeriveManager.findAfterDateCreationReturnIdsManager(null, null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindAfterDateModificationManager() throws ParseException{
      final Banque b1 = banqueManager.findByIdManager(1);
      //recherche fructueuse
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
      final Calendar cal = Calendar.getInstance();
      cal.setTime(search);
      List<ProdDerive> liste = prodDeriveManager.findAfterDateModificationManager(cal, b1);
      assertTrue(liste.size() == 1);
      //recherche infructueuse
      search = new SimpleDateFormat("dd/MM/yyyy").parse("31/11/2009");
      cal.setTime(search);
      liste = prodDeriveManager.findAfterDateModificationManager(cal, b1);
      assertTrue(liste.size() == 0);
      //null recherche
      liste = prodDeriveManager.findAfterDateModificationManager(null, b1);
      assertTrue(liste.size() == 0);

      // recherche avec une banque null
      search = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
      cal.setTime(search);
      liste = prodDeriveManager.findAfterDateModificationManager(cal, null);
      assertTrue(liste.size() == 0);
      //null recherche
      liste = prodDeriveManager.findAfterDateModificationManager(null, null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindByTransformationManager(){
      Transformation transfo = transformationManager.findByIdManager(1);
      List<ProdDerive> derives = prodDeriveManager.findByTransformationManager(transfo);
      assertTrue(derives.size() == 1);

      transfo = transformationManager.findByIdManager(5);
      derives = prodDeriveManager.findByTransformationManager(transfo);
      assertTrue(derives.size() == 0);
   }

   @Test
   public void testFindByParentAndTypeManager(){
      final Echantillon e1 = echantillonManager.findByIdManager(1);
      List<ProdDerive> res = prodDeriveManager.findByParentAndTypeManager(e1, "ADN");
      assertTrue(res.size() == 1);

      final Prelevement p1 = prelevementDao.findById(1);
      res = prodDeriveManager.findByParentAndTypeManager(p1, "ADN");
      assertTrue(res.size() == 1);

      final ProdDerive pd1 = prodDeriveManager.findByIdManager(1);
      res = prodDeriveManager.findByParentAndTypeManager(pd1, "ARN");
      assertTrue(res.size() == 1);

      res = prodDeriveManager.findByParentAndTypeManager(pd1, "ADN");
      assertTrue(res.size() == 0);

      res = prodDeriveManager.findByParentAndTypeManager(pd1, "");
      assertTrue(res.size() == 0);

      final Banque b1 = banqueDao.findById(1);
      res = prodDeriveManager.findByParentAndTypeManager(b1, "ARN");
      assertTrue(res.size() == 0);

      res = prodDeriveManager.findByParentAndTypeManager(pd1, null);
      assertTrue(res.size() == 0);

      res = prodDeriveManager.findByParentAndTypeManager(null, "ADN");
      assertTrue(res.size() == 0);

      res = prodDeriveManager.findByParentAndTypeManager(null, null);
      assertTrue(res.size() == 0);
   }

   @Test
   public void testFindByCodeInListManager(){
      final List<String> criteres = new ArrayList<>();
      criteres.add("PTRA.1.1");
      criteres.add("PTRA.1.2");
      criteres.add("JEG.1.1");
      criteres.add("BOMB");
      criteres.add("CALM");
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));

      final List<String> notfounds = new ArrayList<>();

      List<Integer> liste = prodDeriveManager.findByCodeInListManager(criteres, bks, notfounds);
      assertTrue(liste.size() == 3);
      assertTrue(notfounds.size() == 2);
      assertTrue(notfounds.contains("BOMB"));
      assertTrue(notfounds.contains("CALM"));

      criteres.clear();
      notfounds.clear();
      criteres.add("PTRA.1.1");
      liste = prodDeriveManager.findByCodeInListManager(criteres, bks, notfounds);
      assertTrue(liste.size() == 1);
      assertTrue(notfounds.isEmpty());

      liste = prodDeriveManager.findByCodeInListManager(null, bks, notfounds);
      assertTrue(liste.size() == 0);
      liste = prodDeriveManager.findByCodeInListManager(criteres, null, notfounds);
      assertTrue(liste.size() == 0);
      liste = prodDeriveManager.findByCodeInListManager(new ArrayList<String>(), bks, notfounds);
      assertTrue(liste.size() == 0);
      liste = prodDeriveManager.findByCodeInListManager(criteres, new ArrayList<Banque>(), notfounds);
      assertTrue(liste.size() == 0);
      liste = prodDeriveManager.findByCodeInListManager(criteres, bks, null);
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

      List<Integer> liste = prodDeriveManager.findByPatientNomOrNipInListManager(criteres, bks);
      assertTrue(liste.size() == 3);

      criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("12");
      liste = prodDeriveManager.findByPatientNomOrNipInListManager(criteres, bks);
      assertTrue(liste.size() == 3);

      criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("876");
      liste = prodDeriveManager.findByPatientNomOrNipInListManager(criteres, bks);
      assertTrue(liste.size() == 2);

      liste = prodDeriveManager.findByPatientNomOrNipInListManager(null, bks);
      assertTrue(liste.size() == 0);
      liste = prodDeriveManager.findByPatientNomOrNipInListManager(criteres, null);
      assertTrue(liste.size() == 0);
      liste = prodDeriveManager.findByPatientNomOrNipInListManager(new ArrayList<String>(), bks);
      assertTrue(liste.size() == 0);
      liste = prodDeriveManager.findByPatientNomOrNipInListManager(criteres, new ArrayList<Banque>());
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testCrud(){
      // Récupération des objets associés à un produit dérivé
      final Banque banque = banqueManager.findByIdManager(1);
      final ProdType type = prodTypeManager.findByIdManager(1);
      ObjetStatut statut = objetStatutManager.findByIdManager(4);
      final Collaborateur collab = collaborateurManager.findByIdManager(1);
      //Emplacement emplacement = emplacementManager.findByIdManager(1);
      final Unite quantite = uniteManager.findByIdManager(5);
      final Unite volume = uniteManager.findByIdManager(4);
      final Unite conc = uniteManager.findByIdManager(8);
      final ProdQualite qualite = prodQualiteManager.findByIdManager(1);
      final Reservation reserv = reservationManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);
      final ModePrepaDerive mode = modePrepaDeriveDao.findById(1);

      // Insertion
      final ProdDerive derive1 = new ProdDerive();
      derive1.setCode("Code1");
      derive1.setProdType(type);

      final Emplacement emp = new Emplacement();
      emp.setTerminale(terminaleDao.findById(6));
      emp.setPosition(3);
      emp.setObjetId(derive1.getProdDeriveId());
      emp.setEntite(entiteManager.findByIdManager(3));

      Boolean catched = false;
      // on test l'insertion avec la banque nulle
      try{
         prodDeriveManager.createObjectManager(derive1, null, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      // on test l'insertion avec le type nul
      try{
         prodDeriveManager.createObjectManager(derive1, banque, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec un statut nul
      try{
         prodDeriveManager.createObjectManager(derive1, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      derive1.setCode("PTRA.1.1");
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         prodDeriveManager.createObjectManager(derive1, banque, type, statut, null, null, null, null, null, null, null, null,
            null, null, null, null, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);

      // Emplacement occupé par un échantillon
      derive1.setCode("PTRA.1.3");
      Emplacement empl = emplacementManager.findByIdManager(3);
      catched = false;
      try{
         prodDeriveManager.createObjectManager(derive1, banque, type, statut, null, empl, null, null, null, null, null, null,
            null, null, null, null, true, "/tmp/", false);
      }catch(final TKException ex){
         catched = true;
         assertTrue(ex.getMessage().equals("PTRA.1.3 : error.emplacement.notEmpty"));
         assertTrue(ex.getIdentificationObjetException().equals("PTRA.1.3"));
      }
      assertTrue(catched);
      catched = false;

      // Emplacement occupé par un autre dérivé
      empl = emplacementManager.findByIdManager(2);
      catched = false;
      try{
         prodDeriveManager.createObjectManager(derive1, banque, type, statut, null, empl, null, null, null, null, null, null,
            null, null, null, null, true, "/tmp/", false);
      }catch(final TKException ex){
         catched = true;
         assertTrue(ex.getMessage().equals("PTRA.1.3 : error.emplacement.notEmpty"));
         assertTrue(ex.getIdentificationObjetException().equals("PTRA.1.3"));
      }
      assertTrue(catched);
      catched = false;

      // On teste l'insertion d'une mauvaise transformation pour 
      // vérifier que l'exception est lancée
      Transformation badT = new Transformation();
      badT.setEntite(entiteManager.findByNomManager("Echantillon").get(0));
      badT.setObjetId(10);
      try{
         prodDeriveManager.createObjectManager(derive1, banque, type, statut, null, null, null, null, null, null, null, badT,
            null, null, null, null, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EntiteObjectIdNotExistException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(transformationManager.findAllObjectsManager().size() == 5);

      // on teste la validation lors d'un insertion
      validationInsert(derive1);
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
      // on teste une insertion valide avec les associations 
      // non obigatoires nulles, en suatant la validation et avec une
      // nouvelle transformation
      derive1.setCode("PTRA.1.3");
      derive1.setQuantite((float) -1.0);
      final Transformation newT = new Transformation();
      newT.setEntite(entiteManager.findByNomManager("Echantillon").get(0));
      newT.setObjetId(2);
      newT.setQuantite((float) 15.6);
      newT.setQuantiteUnite(quantite);
      prodDeriveManager.createObjectManager(derive1, banque, type, statut, null, null, null, null, null, null, null, newT, null,
         null, null, utilisateur, false, "/tmp/", false);
      final int id1 = derive1.getProdDeriveId();
      assertTrue(derive1.getProdDeriveId() == id1);
      assertTrue(getOperationManager().findByObjectManager(derive1).size() == 1);
      ProdDerive testInsert = prodDeriveManager.findByIdManager(id1);
      assertTrue(testInsert.getQuantite() < 0);
      assertTrue(transformationManager.findAllObjectsManager().size() == 6);
      final int idNewT = newT.getTransformationId();

      // on teste une insertion valide avec les associations 
      // non obigatoires remplies dont une transfo existante
      final ProdDerive derive2 = new ProdDerive();
      statut = objetStatutManager.findByIdManager(1);
      derive2.setCode("PTRA.1.4");
      final float num = (float) 1.0;
      derive2.setCodeLabo("AYGgdyc");
      derive2.setQuantite(num);
      derive2.setQuantiteInit(num);
      derive2.setVolume(num);
      derive2.setVolumeInit(num);
      derive2.setConc(num);
      final Transformation upT = transformationManager.findByIdManager(idNewT);
      upT.setQuantite((float) 21.3);
      prodDeriveManager.createObjectManager(derive2, banque, type, statut, collab, emp, volume, conc, quantite, mode, qualite,
         upT, null, null, reserv, utilisateur, true, "/tmp/", false);
      final int id2 = derive2.getProdDeriveId();
      assertTrue(derive2.getProdDeriveId() == id2);
      assertTrue(getOperationManager().findByObjectManager(derive2).size() == 1);
      assertTrue(transformationManager.findAllObjectsManager().size() == 6);

      // On vérifie que toutes associations ont étées enregistrées
      testInsert = prodDeriveManager.findByIdManager(id2);
      assertNotNull(testInsert.getBanque());
      assertNotNull(testInsert.getProdType());
      assertNotNull(testInsert.getObjetStatut());
      assertNotNull(testInsert.getCollaborateur());
      assertNotNull(testInsert.getEmplacement());
      assertNotNull(testInsert.getVolumeUnite());
      assertNotNull(testInsert.getConcUnite());
      assertNotNull(testInsert.getQuantiteUnite());
      assertNotNull(testInsert.getProdQualite());
      assertNotNull(testInsert.getTransformation());
      assertNotNull(testInsert.getReservation());
      assertNotNull(testInsert.getModePrepaDerive());
      // On test les attributs
      assertTrue(testInsert.getCodeLabo().length() > 0);
      assertTrue(testInsert.getQuantite() > 0);
      assertTrue(testInsert.getQuantiteInit() > 0);
      assertTrue(testInsert.getVolume() > 0);
      assertTrue(testInsert.getVolumeInit() > 0);
      assertTrue(testInsert.getConc() > 0);
      assertTrue(testInsert.getTransformation().getQuantite().equals((float) 21.3));

      // Suppression
      final ProdDerive derive3 = prodDeriveManager.findByIdManager(id1);
      prodDeriveManager.removeObjectManager(derive3, null, utilisateur, null);
      final ProdDerive derive4 = prodDeriveManager.findByIdManager(id2);
      prodDeriveManager.removeObjectManager(derive4, null, utilisateur, null);
      prodDeriveManager.removeObjectCascadeManager(upT, null, null, null);
      assertTrue(getOperationManager().findByObjectManager(derive1).size() == 0);
      assertTrue(getOperationManager().findByObjectManager(derive2).size() == 0);
      assertTrue(transformationManager.findAllObjectsManager().size() == 5);

      // Update
      final ProdDerive derive5 = new ProdDerive();
      derive5.setCode("PTRA.1.3");
      derive5.setProdType(type);
      derive5.setBanque(banque);
      prodDeriveManager.createObjectManager(derive5, banque, type, objetStatutManager.findByIdManager(4), null, null, null, null,
         null, null, null, null, null, null, null, utilisateur, true, "/tmp/", false);
      final int id3 = derive5.getProdDeriveId();
      assertTrue(derive5.getProdDeriveId() == id3);
      Boolean catchedUpdate = false;

      // on test l'update avec une banque nulle
      final ProdDerive derive6 = prodDeriveManager.findByIdManager(id3);
      final String codeUpdated1 = "PTRA.1.5";
      final String codeUpdated2 = "PTRA.1.2";
      final String codeUpdated3 = "PTRA.1.6";
      derive6.setCode(codeUpdated1);
      try{
         prodDeriveManager.updateObjectManager(derive6, null, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;

      // on test l'update avec un type nul
      final ProdDerive derive7 = prodDeriveManager.findByIdManager(id3);
      derive6.setCode(codeUpdated1);
      try{
         prodDeriveManager.updateObjectManager(derive7, banque, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;

      // on test l'update avec un objetstatut nul
      derive7.setObjetStatut(null);
      try{
         prodDeriveManager.updateObjectManager(derive7, banque, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;

      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      final ProdDerive derive8 = prodDeriveManager.findByIdManager(id3);
      derive8.setCode(codeUpdated2);
      try{
         prodDeriveManager.updateObjectManager(derive8, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;

      final ProdDerive derive9 = prodDeriveManager.findByIdManager(id3);
      // MAJ emplacemet Emplacement occupé par un dérivé
      try{
         prodDeriveManager.updateObjectManager(derive9, banque, type, null, null, empl, null, null, null, null, null, null, null,
            null, null, null, null, null, true, null, "/tmp/");
      }catch(final TKException ex){
         catchedUpdate = true;
         assertTrue(ex.getMessage().equals("PTRA.1.3 : error.emplacement.notEmpty"));
         assertTrue(ex.getIdentificationObjetException().equals("PTRA.1.3"));
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;

      derive8.setCode(codeUpdated1);
      // On teste l'insertion d'une mauvaise transformation pour 
      // vérifier que l'exception est lancée
      badT = new Transformation();
      badT.setEntite(entiteManager.findByNomManager("Echantillon").get(0));
      badT.setObjetId(10);
      try{
         prodDeriveManager.updateObjectManager(derive8, banque, type, null, null, null, null, null, null, null, null, badT, null,
            null, null, null, null, null, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EntiteObjectIdNotExistException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(transformationManager.findAllObjectsManager().size() == 5);

      // on teste la validation lors d'un insertion
      validationUpdate(derive9);
      derive9.setProdDeriveId(id3); // car id perdu dans validation
      // On teste une mise à jour valide avec les assos a nul
      // en sautant la validation et avec une nouvelle transfo
      derive9.setCodeLabo("LAB");
      derive9.setQuantite((float) -1.0);
      final Transformation newT2 = new Transformation();
      newT2.setEntite(entiteManager.findByNomManager("Echantillon").get(0));
      newT2.setObjetId(1);
      newT2.setQuantite((float) 15.6);
      newT2.setQuantiteUnite(quantite);
      prodDeriveManager.updateObjectManager(derive9, banque, type, null, null, null, null, null, null, null, null, newT2, null,
         null, null, null, null, utilisateur, false, null, "/tmp/");
      final ProdDerive derive10 = prodDeriveManager.findByIdManager(id3);
      assertTrue(derive10.getCodeLabo().equals("LAB"));
      assertTrue(getOperationManager().findByObjectManager(derive9).size() == 2);
      ProdDerive testInsert2 = prodDeriveManager.findByIdManager(id3);
      assertTrue(testInsert2.getQuantite() < 0);
      assertTrue(transformationManager.findAllObjectsManager().size() == 6);
      final int idNewT2 = newT2.getTransformationId();

      // On teste une mise à jour valide avec les assos et une
      // transfo existante
      final ProdDerive derive11 = prodDeriveManager.findByIdManager(id3);
      derive11.setCode(codeUpdated3);
      derive11.setCodeLabo("AYGgdyc");
      derive11.setQuantite(num);
      derive11.setQuantiteInit(num);
      derive11.setVolume(num);
      derive11.setVolumeInit(num);
      derive11.setConc(num);
      final Transformation upT2 = transformationManager.findByIdManager(idNewT2);
      upT2.setQuantite((float) 21.3);
      final List<OperationType> opTypes = new ArrayList<>();
      opTypes.add(operationTypeManager.findByIdManager(1));
      opTypes.add(operationTypeManager.findByIdManager(2));
      prodDeriveManager.updateObjectManager(derive11, banque, type, statut, collab, emp, volume, conc, quantite, mode, qualite,
         upT2, null, null, null, null, reserv, utilisateur, true, opTypes, "/tmp/");
      final ProdDerive derive12 = prodDeriveManager.findByIdManager(id3);
      assertTrue(derive12.getCode().equals(codeUpdated3));
      assertTrue(getOperationManager().findByObjectManager(derive11).size() == 5);
      assertTrue(transformationManager.findAllObjectsManager().size() == 6);

      // On vérifie que toutes associations ont étées enregistrées
      testInsert2 = prodDeriveManager.findByIdManager(id3);
      assertNotNull(testInsert2.getBanque());
      assertNotNull(testInsert2.getProdType());
      assertNotNull(testInsert2.getObjetStatut());
      assertNotNull(testInsert2.getCollaborateur());
      assertNotNull(testInsert2.getEmplacement());
      assertNotNull(testInsert2.getVolumeUnite());
      assertNotNull(testInsert2.getConcUnite());
      assertNotNull(testInsert2.getQuantiteUnite());
      assertNotNull(testInsert2.getProdQualite());
      assertNotNull(testInsert2.getTransformation());
      assertNotNull(testInsert2.getReservation());
      assertNotNull(testInsert2.getModePrepaDerive());
      // On test les attributs
      assertTrue(testInsert2.getCodeLabo().length() > 0);
      assertTrue(testInsert2.getQuantite() > 0);
      assertTrue(testInsert2.getQuantiteInit() > 0);
      assertTrue(testInsert2.getVolume() > 0);
      assertTrue(testInsert2.getVolumeInit() > 0);
      assertTrue(testInsert2.getConc() > 0);
      assertTrue(testInsert2.getTransformation().getQuantite().equals((float) 21.3));

      final ProdDerive derive13 = prodDeriveManager.findByIdManager(id3);
      // Emplacement empl13 = derive13.getEmplacement();
      prodDeriveManager.removeObjectManager(derive13, null, utilisateur, null);
      transformationManager.removeObjectManager(upT2, null, null);
      assertNull(prodDeriveManager.findByIdManager(id3));
      assertTrue(getOperationManager().findByObjectManager(derive13).size() == 0);
      assertTrue(transformationManager.findAllObjectsManager().size() == 5);
      // emplacementManager.removeObjectManager(empl13);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(derive3);
      fs.add(derive4);
      fs.add(derive13);
      cleanUpFantomes(fs);
   }

   @Test
   public void testFindParent(){
      final String code = "PTRA.1.1";
      final List<ProdDerive> derives = prodDeriveManager.findByCodeLikeManager(code, true);
      assertTrue(derives.size() == 1);
      final ProdDerive derive = derives.get(0);
      assertTrue(derive.getCode().equals(code));
      final Echantillon echantillon =
         echantillonManager.findByIdManager(((Echantillon) prodDeriveManager.findParent(derive)).getEchantillonId());
      assertTrue(echantillonManager.getProdDerivesManager(echantillon).contains(derive));
   }

   @Test
   public void testGetPrelevementParent(){
      final ProdDerive pd1 = prodDeriveManager.findByIdManager(1);
      final Prelevement prel1 = prodDeriveManager.getPrelevementParent(pd1);
      assertNotNull(prel1);
      assertTrue(prel1.getCode().equals("PRLVT1"));

      final ProdDerive pd2 = prodDeriveManager.findByIdManager(2);
      final Prelevement prel2 = prodDeriveManager.getPrelevementParent(pd2);
      assertNotNull(prel2);
      assertTrue(prel2.getCode().equals("PRLVT1"));

      final ProdDerive pd3 = prodDeriveManager.findByIdManager(3);
      final Prelevement prel3 = prodDeriveManager.getPrelevementParent(pd3);
      assertNotNull(prel3);
      assertTrue(prel3.getCode().equals("PRLVT1"));

      final ProdDerive pd4 = prodDeriveManager.findByIdManager(4);
      final Prelevement prel4 = prodDeriveManager.getPrelevementParent(pd4);
      assertNotNull(prel4);
      assertTrue(prel4.getCode().equals("PRLVT3"));

   }

   @Test
   public void testGetPatientParent(){
      final ProdDerive pd1 = prodDeriveManager.findByIdManager(1);
      final Patient pat1 = prodDeriveManager.getPatientParentManager(pd1);
      assertNotNull(pat1);
      assertTrue(pat1.getNom().equals("DELPHINO"));

      final ProdDerive pd2 = prodDeriveManager.findByIdManager(2);
      final Patient pat2 = prodDeriveManager.getPatientParentManager(pd2);
      assertNotNull(pat2);
      assertTrue(pat2.getNom().equals("DELPHINO"));

      final ProdDerive pd3 = prodDeriveManager.findByIdManager(3);
      final Patient pat3 = prodDeriveManager.getPatientParentManager(pd3);
      assertNotNull(pat3);
      assertTrue(pat3.getNom().equals("DELPHINO"));

      final ProdDerive pd4 = prodDeriveManager.findByIdManager(4);
      final Patient pat4 = prodDeriveManager.getPatientParentManager(pd4);
      assertNotNull(pat4);
      assertTrue(pat4.getNom().equals("MAYER"));
   }

   private void validationInsert(final ProdDerive derive){
      // Récupération des objets associés à un produit dérivé
      final Banque banque = banqueManager.findByIdManager(1);
      final ProdType type = prodTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      boolean catchedInsert = false;
      // On teste une insertion avec un attribut code non valide
      final String[] codes = new String[] {"", "  ", null, "l#jd$$", createOverLength(50), "GOOD"};
      final String[] codesLabos = new String[] {"", " ", "%¢¢kjs", createOverLength(10), "GOOD"};
      for(int i = 0; i < codes.length; i++){
         for(int j = 0; j < codesLabos.length; j++){
            catchedInsert = false;
            try{
               derive.setCode(codes[i]);
               derive.setCodeLabo(codesLabos[j]);
               if(i != 5 || j != 4){ //car creation valide
                  prodDeriveManager.createObjectManager(derive, banque, type, null, null, null, null, null, null, null, null,
                     null, null, null, null, utilisateur, true, "/tmp/", false);
               }
            }catch(final Exception e){
               if(e.getClass().getSimpleName().equals("ValidationException")){
                  catchedInsert = true;
               }
            }
            if(i != 5 || j != 4){
               assertTrue(catchedInsert);
            }
         }
      }
      derive.setCodeLabo(null);

      // On teste une insertion avec un attribut quantiteInit non valide
      final float negative = (float) -1.0;
      derive.setQuantiteInit(negative);
      catchedInsert = false;
      try{
         prodDeriveManager.createObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, utilisateur, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      derive.setQuantiteInit(null);

      // On teste une insertion avec un attribut quantite non valide
      derive.setQuantite(negative);
      catchedInsert = false;
      try{
         prodDeriveManager.createObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, utilisateur, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      derive.setQuantite(null);

      // On teste une insertion avec un attribut quantite > quantiteInit
      final float q1 = (float) 2.0;
      final float q2 = (float) 1.0;
      derive.setQuantite(q1);
      derive.setQuantiteInit(q2);
      catchedInsert = false;
      try{
         prodDeriveManager.createObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, utilisateur, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      derive.setQuantite(null);
      derive.setQuantiteInit(null);

      // On teste une insertion avec un attribut volumeInit non valide
      derive.setVolumeInit(negative);
      catchedInsert = false;
      try{
         prodDeriveManager.createObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, utilisateur, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      derive.setVolumeInit(null);

      // On teste une insertion avec un attribut volume non valide
      derive.setVolume(negative);
      catchedInsert = false;
      try{
         prodDeriveManager.createObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, utilisateur, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      derive.setVolume(null);

      // On teste une insertion avec un attribut volume > volumeInit
      derive.setVolume(q1);
      derive.setVolumeInit(q2);
      catchedInsert = false;
      try{
         prodDeriveManager.createObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, utilisateur, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      derive.setVolume(null);
      derive.setVolumeInit(null);

      // On teste une insertion avec un attribut conc non valide
      derive.setConc(negative);
      catchedInsert = false;
      try{
         prodDeriveManager.createObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, utilisateur, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      derive.setConc(null);

      // On teste une insertion avec une date de stokage non valide
      final Calendar stock = Calendar.getInstance();
      final Calendar transfo = Calendar.getInstance();
      try{
         stock.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/06/2009"));
         transfo.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("05/06/2009"));
      }catch(final ParseException e1){
         e1.printStackTrace();
      }
      derive.setDateStock(stock);
      derive.setDateTransformation(transfo);
      catchedInsert = false;
      try{
         prodDeriveManager.createObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, utilisateur, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      derive.setDateStock(null);
      derive.setDateTransformation(null);

      // On teste une insertion avec un attribut quantiteInit ne 
      // respectant pas le volumeInit et la concentration
      final float qte1 = (float) 2.0;
      final float vol1 = (float) 1.0;
      final float conc1 = (float) 1.0;
      derive.setQuantiteInit(qte1);
      derive.setVolumeInit(vol1);
      derive.setConc(conc1);
      catchedInsert = false;
      try{
         prodDeriveManager.createObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, utilisateur, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      derive.setQuantiteInit(null);
      derive.setVolumeInit(null);
      derive.setConc(null);

      // On teste une insertion avec les unités non valides
      final Unite quantite = uniteManager.findByIdManager(1);
      final Unite volume = uniteManager.findByIdManager(4);
      final Unite conc = uniteManager.findByIdManager(2);
      catchedInsert = false;
      try{
         prodDeriveManager.createObjectManager(derive, banque, type, null, null, null, volume, conc, quantite, null, null, null,
            null, null, null, utilisateur, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
   }

   private void validationUpdate(final ProdDerive derive){
      // Récupération des objets associés à un produit dérivé
      final Banque banque = banqueManager.findByIdManager(1);
      final ProdType type = prodTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      boolean catchedUpdate = false;
      final String[] codes = new String[] {"", "  ", null, "l¢ËÊ¢jd%", createOverLength(50), "GOOD"};
      final String[] codesLabos = new String[] {"", " ", "%$$ÆÂ¢kjs", createOverLength(10), "GOOD"};
      for(int i = 0; i < codes.length; i++){
         for(int j = 0; j < codesLabos.length; j++){
            catchedUpdate = false;
            try{
               derive.setCode(codes[i]);
               derive.setCodeLabo(codesLabos[j]);
               if(i != 5 || j != 4){ //car creation valide
                  prodDeriveManager.updateObjectManager(derive, banque, type, null, null, null, null, null, null, null, null,
                     null, null, null, null, null, null, utilisateur, true, null, "/tmp/");
               }
            }catch(final Exception e){
               if(e.getClass().getSimpleName().equals("ValidationException")){
                  catchedUpdate = true;
               }
            }
            if(i != 5 || j != 4){
               assertTrue(catchedUpdate);
            }
         }
      }
      derive.setCodeLabo(null);

      // On teste une modif avec un attribut quantiteInit non valide
      final float negative = (float) -1.0;
      derive.setQuantiteInit(negative);
      catchedUpdate = false;
      try{
         prodDeriveManager.updateObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      derive.setQuantiteInit(null);

      // On teste une modif avec un attribut quantite non valide
      derive.setQuantite(negative);
      catchedUpdate = false;
      try{
         prodDeriveManager.updateObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      derive.setQuantite(null);

      // On teste une insertion avec un attribut quantite > quantiteInit
      final float q1 = (float) 2.0;
      final float q2 = (float) 1.0;
      derive.setQuantite(q1);
      derive.setQuantiteInit(q2);
      catchedUpdate = false;
      try{
         prodDeriveManager.updateObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      derive.setQuantite(null);
      derive.setQuantiteInit(null);

      // On teste une modif avec un attribut volumeInit non valide
      derive.setVolumeInit(negative);
      catchedUpdate = false;
      try{
         prodDeriveManager.updateObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      derive.setVolumeInit(null);

      // On teste une modif avec un attribut volume non valide
      derive.setVolume(negative);
      catchedUpdate = false;
      try{
         prodDeriveManager.updateObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      derive.setVolume(null);

      // On teste une insertion avec un attribut volume > volumeInit
      derive.setVolume(q1);
      derive.setVolumeInit(q2);
      catchedUpdate = false;
      try{
         prodDeriveManager.updateObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      derive.setVolume(null);
      derive.setVolumeInit(null);

      // On teste une modif avec un attribut conc non valide
      derive.setConc(negative);
      catchedUpdate = false;
      try{
         prodDeriveManager.updateObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      derive.setConc(null);

      // On teste une insertion avec une date de stokage non valide
      final Calendar stock = Calendar.getInstance();
      final Calendar transfo = Calendar.getInstance();
      try{
         stock.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/06/2009"));
         transfo.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("05/06/2009"));
      }catch(final ParseException e1){
         e1.printStackTrace();
      }
      derive.setDateStock(stock);
      derive.setDateTransformation(transfo);
      catchedUpdate = false;
      try{
         prodDeriveManager.updateObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      derive.setDateStock(null);
      derive.setDateTransformation(null);

      // On teste un update avec un attribut quantiteInit ne 
      // respectant pas le volumeInit et la concentration
      final float qte1 = (float) 2.0;
      final float vol1 = (float) 1.0;
      final float conc1 = (float) 1.0;
      derive.setQuantiteInit(qte1);
      derive.setVolumeInit(vol1);
      derive.setConc(conc1);
      catchedUpdate = false;
      try{
         prodDeriveManager.updateObjectManager(derive, banque, type, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      derive.setQuantiteInit(null);
      derive.setVolumeInit(null);
      derive.setConc(null);

      // On teste unupdate avec les unités non valides
      final Unite quantite = uniteManager.findByIdManager(1);
      final Unite volume = uniteManager.findByIdManager(4);
      final Unite conc = uniteManager.findByIdManager(2);
      catchedUpdate = false;
      try{
         prodDeriveManager.updateObjectManager(derive, banque, type, null, null, null, volume, conc, quantite, null, null, null,
            null, null, null, null, null, utilisateur, true, null, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
   }

   @Test
   public void testUpdateMultipleObjectsManager(){
      final Banque banque = banqueManager.findByIdManager(1);
      final ProdType type = prodTypeManager.findByIdManager(1);
      final ObjetStatut statut = objetStatutManager.findByIdManager(4);
      //		Collaborateur collab = collaborateurManager.findByIdManager(1);
      //		Emplacement emplacement = emplacementManager.findByIdManager(1);
      final Unite quantite = uniteManager.findByIdManager(5);
      //		Unite volume = uniteManager.findByIdManager(4);
      //		Unite conc = uniteManager.findByIdManager(8);
      //		ProdQualite qualite = prodQualiteManager.findByIdManager(1);
      //		Reservation reserv = reservationManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      final Transformation newT = new Transformation();
      newT.setEntite(entiteManager.findByNomManager("Echantillon").get(0));
      newT.setObjetId(2);
      newT.setQuantite((float) 15.6);
      newT.setQuantiteUnite(quantite);

      // on insert 3 nouveaux dérivés pour les tests
      final ProdDerive derive1 = new ProdDerive();
      derive1.setCode("PTRA.1.4");
      final float num = (float) 1.0;
      derive1.setCodeLabo("AYGgdyc");
      derive1.setQuantite(num);
      derive1.setQuantiteInit(num);
      derive1.setVolume(num);
      derive1.setVolumeInit(num);
      derive1.setConc(num);
      derive1.setProdType(type);
      derive1.setObjetStatut(statut);
      derive1.setConformeCession(true);
      derive1.setConformeTraitement(true);

      final ProdDerive derive2 = new ProdDerive();
      derive2.setCode("PTRA.1.5");
      derive2.setCodeLabo("AYGgdyc");
      derive2.setQuantite(num);
      derive2.setQuantiteInit(num);
      derive2.setVolume(num);
      derive2.setVolumeInit(num);
      derive2.setConc(num);
      derive2.setProdType(type);
      derive2.setObjetStatut(statut);

      final ProdDerive derive3 = new ProdDerive();
      derive3.setCode("PTRA.1.6");
      derive3.setCodeLabo("AYGgdyc");
      derive3.setQuantite(num);
      derive3.setQuantiteInit(num);
      derive3.setVolume(num);
      derive3.setVolumeInit(num);
      derive3.setConc(num);
      derive3.setProdType(type);
      derive3.setObjetStatut(statut);

      final List<ProdDerive> derives = new ArrayList<>();
      derives.add(derive1);
      derives.add(derive2);
      derives.add(derive3);
      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final List<AnnotationValeur> listDelete = new ArrayList<>();

      final AnnotationValeur bool1 = new AnnotationValeur();
      bool1.setChampAnnotation(champAnnotationDao.findById(18));
      bool1.setBool(true);
      bool1.setBanque(banque);
      listAnnots.add(bool1);
      prodDeriveManager.createDeriveListWithAnnotsManager(derives, banque, newT, utilisateur, listAnnots, "/tmp/",
         new ArrayList<NonConformite>(), new ArrayList<NonConformite>());

      final int idNewT = newT.getTransformationId();
      final int id1 = derive1.getProdDeriveId();
      final int id2 = derive2.getProdDeriveId();
      final int id3 = derive3.getProdDeriveId();

      assertTrue(prodDeriveManager.findAllObjectsManager().size() == 7);
      assertTrue(transformationManager.findAllObjectsManager().size() == 6);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 15);

      assertTrue(prodDeriveManager.findByIdManager(id1).getConformeTraitement());
      assertTrue(prodDeriveManager.findByIdManager(id1).getConformeCession());
      assertNull(prodDeriveManager.findByIdManager(id2).getConformeTraitement());
      assertNull(prodDeriveManager.findByIdManager(id2).getConformeCession());

      // on teste une maj valide
      final String codeLaboUp = "heya";
      final ProdDerive deriveUp1 = prodDeriveManager.findByIdManager(id1);
      final ProdDerive deriveUp2 = prodDeriveManager.findByIdManager(id2);
      final ProdDerive deriveUp3 = prodDeriveManager.findByIdManager(id3);
      deriveUp1.setCodeLabo(codeLaboUp);
      deriveUp2.setCodeLabo(codeLaboUp);
      deriveUp3.setCodeLabo(codeLaboUp);
      List<ProdDerive> list = new ArrayList<>();
      list.add(deriveUp1);
      list.add(deriveUp2);
      list.add(deriveUp3);
      listAnnots.clear();
      listAnnots.addAll(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(18), derive1));
      listAnnots.addAll(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(18), derive2));
      listAnnots.addAll(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(18), derive3));
      listAnnots.get(0).setBool(false);
      listAnnots.get(1).setBool(true);
      listAnnots.get(2).setBool(false);

      final List<NonConformite> ncfsTrait = new ArrayList<>();
      ncfsTrait.add(nonConformiteDao.findById(8));

      final List<NonConformite> ncfsCess = new ArrayList<>();
      ncfsCess.add(nonConformiteDao.findById(10));

      prodDeriveManager.updateMultipleObjectsManager(list, null, listAnnots, listDelete, ncfsTrait, ncfsCess, utilisateur,
         "/tmp/");

      final ProdDerive deriveTest1 = prodDeriveManager.findByIdManager(id1);
      final ProdDerive deriveTest2 = prodDeriveManager.findByIdManager(id2);
      final ProdDerive deriveTest3 = prodDeriveManager.findByIdManager(id3);
      assertTrue(deriveTest1.getCodeLabo().equals(codeLaboUp));
      assertTrue(deriveTest2.getCodeLabo().equals(codeLaboUp));
      assertTrue(deriveTest3.getCodeLabo().equals(codeLaboUp));

      assertFalse(
         annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(18), deriveTest1).get(0).getBool());
      assertTrue(
         annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(18), deriveTest2).get(0).getBool());
      assertFalse(
         annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(18), deriveTest3).get(0).getBool());

      assertTrue(objetNonConformeDao.findByObjetAndEntite(id1, entiteManager.findByIdManager(8)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id2, entiteManager.findByIdManager(8)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id3, entiteManager.findByIdManager(8)).size() == 2);

      // on teste une maj non valide sur le 1er élément
      deriveTest1.setVolume((float) 15.0);
      final String codeLaboUp2 = "wghougo";
      deriveTest1.setCodeLabo(codeLaboUp2);
      deriveTest2.setCodeLabo(codeLaboUp2);
      deriveTest3.setCodeLabo(codeLaboUp2);
      list = new ArrayList<>();
      list.add(deriveTest1);
      list.add(deriveTest2);
      list.add(deriveTest3);
      listAnnots.get(0).setBool(true);
      //listAnnots.get(1).setBool(true);
      listAnnots.get(2).setBool(true);

      boolean catched = false;
      try{
         prodDeriveManager.updateMultipleObjectsManager(list, null, listAnnots, listDelete, new ArrayList<NonConformite>(), null,
            utilisateur, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;

            assertTrue(((ValidationException) e).getEntiteObjetException().equals("ProdDerive"));
            assertTrue(((ValidationException) e).getIdentificationObjetException().equals(list.get(0).getCode()));
         }
      }
      assertTrue(catched);
      final ProdDerive deriveTest4 = prodDeriveManager.findByIdManager(id1);
      final ProdDerive deriveTest5 = prodDeriveManager.findByIdManager(id2);
      final ProdDerive deriveTest6 = prodDeriveManager.findByIdManager(id3);
      assertTrue(deriveTest4.getCodeLabo().equals(codeLaboUp));
      assertTrue(deriveTest5.getCodeLabo().equals(codeLaboUp));
      assertTrue(deriveTest6.getCodeLabo().equals(codeLaboUp));
      assertFalse(
         annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(18), deriveTest4).get(0).getBool());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id1, entiteManager.findByIdManager(8)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id2, entiteManager.findByIdManager(8)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id3, entiteManager.findByIdManager(8)).size() == 2);

      // on teste une maj non valide sur le 3eme élément
      deriveTest6.setVolume((float) 15.0);
      deriveTest4.setCodeLabo(codeLaboUp2);
      deriveTest5.setCodeLabo(codeLaboUp2);
      deriveTest6.setCodeLabo(codeLaboUp2);
      list = new ArrayList<>();
      list.add(deriveTest4);
      list.add(deriveTest5);
      list.add(deriveTest6);

      catched = false;
      try{
         prodDeriveManager.updateMultipleObjectsManager(list, list, listAnnots, listDelete, null, null, utilisateur, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      final ProdDerive deriveTest7 = prodDeriveManager.findByIdManager(id1);
      final ProdDerive deriveTest8 = prodDeriveManager.findByIdManager(id2);
      final ProdDerive deriveTest9 = prodDeriveManager.findByIdManager(id3);
      assertTrue(deriveTest7.getCodeLabo().equals(codeLaboUp));
      assertTrue(deriveTest8.getCodeLabo().equals(codeLaboUp));
      assertTrue(deriveTest9.getCodeLabo().equals(codeLaboUp));

      assertFalse(
         annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(18), deriveTest7).get(0).getBool());

      // on teste avec creation d'annotation
      deriveTest6.setVolume((float) 1.0);
      final AnnotationValeur alpha1 = new AnnotationValeur();
      alpha1.setAlphanum("val1");
      alpha1.setChampAnnotation(champAnnotationDao.findById(17));
      alpha1.setObjetId(id1);
      alpha1.setBanque(banque);
      listAnnots.add(alpha1);
      final AnnotationValeur alpha2 = new AnnotationValeur();
      alpha2.setAlphanum("&&é$$¤¤");
      alpha2.setChampAnnotation(champAnnotationDao.findById(17));
      alpha2.setObjetId(id2);
      alpha2.setBanque(banque);
      listAnnots.add(alpha2);
      listDelete.add(listAnnots.get(2));
      listAnnots.remove(2); // delete
      // validationException
      try{
         prodDeriveManager.updateMultipleObjectsManager(list, null, listAnnots, listDelete, null, null, utilisateur, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(prodDeriveManager.findByIdManager(id1).getCodeLabo().equals(codeLaboUp));
      assertTrue(prodDeriveManager.findByIdManager(id2).getCodeLabo().equals(codeLaboUp));
      assertTrue(prodDeriveManager.findByIdManager(id3).getCodeLabo().equals(codeLaboUp));

      assertFalse(annotationValeurManager
         .findByChampAndObjetManager(champAnnotationDao.findById(18), prodDeriveManager.findByIdManager(id3)).get(0).getBool());
      // modifications valides
      //alpha1.setAnnotationValeurId(null);
      alpha2.setAlphanum("yy");
      prodDeriveManager.updateMultipleObjectsManager(list, null, listAnnots, listDelete, null, new ArrayList<NonConformite>(),
         utilisateur, "/tmp/");
      assertTrue(prodDeriveManager.findByIdManager(id3).getCodeLabo().equals(codeLaboUp2));
      assertTrue(annotationValeurManager
         .findByChampAndObjetManager(champAnnotationDao.findById(18), prodDeriveManager.findByIdManager(id1)).get(0).getBool());
      assertTrue(annotationValeurManager
         .findByChampAndObjetManager(champAnnotationDao.findById(17), prodDeriveManager.findByIdManager(id2)).get(0).getAlphanum()
         .equals("yy"));
      assertTrue(annotationValeurManager
         .findByChampAndObjetManager(champAnnotationDao.findById(17), prodDeriveManager.findByIdManager(id3)).size() == 0);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id1, entiteManager.findByIdManager(8)).size() == 1);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id2, entiteManager.findByIdManager(8)).size() == 1);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id3, entiteManager.findByIdManager(8)).size() == 1);

      // Suppression en cascade
      final Transformation t = transformationManager.findByIdManager(idNewT);
      prodDeriveManager.removeObjectCascadeManager(t, null, utilisateur, null);
      assertTrue(getOperationManager().findByObjectManager(deriveTest7).size() == 0);
      assertTrue(getOperationManager().findByObjectManager(deriveTest8).size() == 0);
      assertTrue(transformationManager.findAllObjectsManager().size() == 5);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);
      assertTrue(objetNonConformeDao.findAll().size() == 6);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(list);
      cleanUpFantomes(fs);

   }

   @Test
   public void testDateStockCoherence() throws ParseException{
      ProdDerive derive = new ProdDerive();
      final Calendar transfo = Calendar.getInstance();
      transfo.setTime(new SimpleDateFormat("dd/MM/yy").parse("01/01/1973"));
      final Calendar stock = Calendar.getInstance();
      stock.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1972"));
      derive.setDateTransformation(transfo);
      derive.setDateStock(stock);

      // limites inf
      Errors errs = prodDeriveValidator.checkDateStockCoherence(derive);
      assertEquals("date.validation.infDateTransfo", errs.getFieldError().getCode());

      transfo.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1972"));

      derive.setDateTransformation(transfo);

      // parent prelevement
      Transformation t = transformationManager.findByIdManager(3);
      derive.setTransformation(t);

      errs = prodDeriveValidator.checkDateStockCoherence(derive);
      assertEquals("date.validation.infDatePrelevement", errs.getFieldError().getCode());
      // -> car skipToDatePrel true

      // parent echantillon
      t = transformationManager.findByIdManager(4);
      derive.setTransformation(t);

      errs = prodDeriveValidator.checkDateStockCoherence(derive);
      assertEquals("date.validation.infDateStockEchan", errs.getFieldError().getCode());

      // parent derive
      t = transformationManager.findByIdManager(2);
      derive.setTransformation(t);

      errs = prodDeriveValidator.checkDateStockCoherence(derive);
      assertEquals("date.validation.infDateStockDeriveParent", errs.getFieldError().getCode());

      // limite sup
      derive.setTransformation(null);
      derive.setDateTransformation(null);
      stock.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2072"));
      derive.setDateStock(stock);
      errs = prodDeriveValidator.checkDateStockCoherence(derive);
      assertEquals("date.validation.supDateActuelle", errs.getFieldError().getCode());

      // derives enfant
      derive = prodDeriveManager.findByIdManager(1);
      //derive.setDateTransformation(null);
      stock.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("08/07/2009"));
      derive.setDateStock(stock);

      errs = prodDeriveValidator.checkDateStockCoherence(derive);
      assertEquals("date.validation.supDateTransfoEnfant", errs.getFieldError().getCode());

      stock.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("09/07/2009"));
      derive.setDateStock(stock);

      errs = prodDeriveValidator.checkDateStockCoherence(derive);
      assertEquals("date.validation.supDateTransfoEnfant", errs.getFieldError().getCode());

      // null validation
      derive.setDateStock(null);
      errs = prodDeriveValidator.checkDateStockCoherence(derive);
      assertTrue(errs.getAllErrors().size() == 0);
   }

   @Test
   public void testDateTransfoCoherence() throws ParseException{
      ProdDerive derive = new ProdDerive();
      final Calendar transfo = Calendar.getInstance();
      transfo.setTime(new SimpleDateFormat("dd/MM/yy").parse("01/01/1973"));
      final Calendar stock = Calendar.getInstance();
      stock.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1972"));
      derive.setDateTransformation(transfo);

      // limites inf

      // parent prelevement
      Transformation t = transformationManager.findByIdManager(3);
      derive.setTransformation(t);

      Errors errs = prodDeriveValidator.checkDateTransfoCoherence(derive);
      assertEquals("date.validation.infDatePrelevement", errs.getFieldError().getCode());
      // -> car skipToDatePrel true

      // parent echantillon
      t = transformationManager.findByIdManager(4);
      derive.setTransformation(t);

      errs = prodDeriveValidator.checkDateTransfoCoherence(derive);
      assertEquals("date.validation.infDateStockEchan", errs.getFieldError().getCode());

      final Calendar test = Calendar.getInstance();
      test.setTime(new SimpleDateFormat("dd/MM/yy").parse("12/13/2008 10:00:00"));
      derive.setDateTransformation(test);
      errs = prodDeriveValidator.checkDateTransfoCoherence(derive);
      assertTrue(errs.getErrorCount() == 0);

      // parent derive
      derive.setDateTransformation(transfo);
      t = transformationManager.findByIdManager(2);
      derive.setTransformation(t);

      errs = prodDeriveValidator.checkDateTransfoCoherence(derive);
      assertEquals("date.validation.infDateStockDeriveParent", errs.getFieldError().getCode());

      //limites sup
      derive.setTransformation(null);
      derive.setDateStock(stock);

      errs = prodDeriveValidator.checkDateTransfoCoherence(derive);
      assertEquals("date.validation.supDateStock", errs.getFieldError().getCode());

      derive.setDateStock(null);
      transfo.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2072"));
      derive.setDateTransformation(transfo);
      errs = prodDeriveValidator.checkDateTransfoCoherence(derive);
      assertEquals("date.validation.supDateActuelle", errs.getFieldError().getCode());

      // derives enfant
      derive = prodDeriveManager.findByIdManager(1);
      derive.setDateStock(null);
      transfo.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("08/07/2009"));
      derive.setDateTransformation(transfo);

      errs = prodDeriveValidator.checkDateTransfoCoherence(derive);
      assertEquals("date.validation.supDateTransfoEnfant", errs.getFieldError().getCode());

      transfo.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("09/07/2009"));
      derive.setDateTransformation(transfo);

      errs = prodDeriveValidator.checkDateTransfoCoherence(derive);
      assertEquals("date.validation.supDateTransfoEnfant", errs.getFieldError().getCode());

      // null validation
      derive.setDateTransformation(null);
      errs = prodDeriveValidator.checkDateTransfoCoherence(derive);
      assertTrue(errs.getAllErrors().size() == 0);
   }

   @Test
   public void testCreateDeriveListWithAnnotsManager(){

      final List<ProdDerive> derives = new ArrayList<>();
      final Banque banque = banqueManager.findByIdManager(1);
      final ProdType type = prodTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);
      final ObjetStatut st = objetStatutManager.findByIdManager(4);

      final ProdDerive derive1 = new ProdDerive();
      derive1.setCode("Code1");
      derive1.setProdType(type);
      derive1.setObjetStatut(st);
      derives.add(derive1);
      final ProdDerive derive2 = new ProdDerive();
      derive2.setCode("Code2");
      derive2.setProdType(type);
      derive2.setObjetStatut(st);
      derives.add(derive2);
      final ProdDerive derive3 = new ProdDerive();
      derive3.setCode("Code$!");
      derive3.setProdType(type);
      derive3.setObjetStatut(st);
      derives.add(derive3);

      final List<AnnotationValeur> valeurs = new ArrayList<>();

      final AnnotationValeur bool2 = new AnnotationValeur();
      bool2.setChampAnnotation(champAnnotationDao.findById(18));
      bool2.setBool(true);
      bool2.setBanque(banque);
      valeurs.add(bool2);
      final AnnotationValeur alpha = new AnnotationValeur();
      alpha.setAlphanum("$$€€");
      alpha.setChampAnnotation(champAnnotationDao.findById(17));
      alpha.setBanque(banque);
      valeurs.add(alpha);

      final List<NonConformite> ncfsTrait = new ArrayList<>();
      ncfsTrait.add(nonConformiteDao.findById(8));

      final List<NonConformite> ncfsCess = new ArrayList<>();
      ncfsCess.add(nonConformiteDao.findById(10));
      ncfsCess.add(nonConformiteDao.findById(11));

      // teste la creation avec rollback sur derives
      try{
         prodDeriveManager.createDeriveListWithAnnotsManager(derives, banque, null, utilisateur, valeurs, "/tmp/", ncfsTrait,
            ncfsCess);
      }catch(final DeriveBatchSaveException ve){
         assertTrue(ve.getTargetExeption() instanceof ValidationException);
         assertTrue(true);
      }
      testFindAll();
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);

      // teste la creation avec rollback sur annotations
      derive3.setCode("code3");
      try{
         prodDeriveManager.createDeriveListWithAnnotsManager(derives, banque, null, utilisateur, valeurs, "/tmp/", ncfsTrait,
            ncfsCess);
      }catch(final DeriveBatchSaveException ve){
         assertTrue(ve.getTargetExeption() instanceof ValidationException);
         assertTrue(true);
      }
      testFindAll();
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);

      // creation réussie
      alpha.setAlphanum("ok");
      prodDeriveManager.createDeriveListWithAnnotsManager(derives, banque, null, utilisateur, valeurs, "/tmp/", ncfsTrait,
         ncfsCess);
      assertTrue(prodDeriveManager.findByCodeLikeManager("Code", false).size() == 3);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 18);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(17),
         prodDeriveManager.findByCodeLikeManager("Code", false).get(0)).get(0).getAlphanum().equals("ok"));

      assertFalse(prodDeriveManager.findByIdManager(derive1.getProdDeriveId()).getConformeCession());
      assertFalse(prodDeriveManager.findByIdManager(derive1.getProdDeriveId()).getConformeTraitement());
      assertFalse(prodDeriveManager.findByIdManager(derive2.getProdDeriveId()).getConformeCession());
      assertFalse(prodDeriveManager.findByIdManager(derive2.getProdDeriveId()).getConformeTraitement());
      assertFalse(prodDeriveManager.findByIdManager(derive3.getProdDeriveId()).getConformeCession());
      assertFalse(prodDeriveManager.findByIdManager(derive3.getProdDeriveId()).getConformeTraitement());

      assertTrue(
         objetNonConformeDao.findByObjetAndEntite(derive1.getProdDeriveId(), entiteManager.findByIdManager(8)).size() == 3);
      assertTrue(
         objetNonConformeDao.findByObjetAndEntite(derive2.getProdDeriveId(), entiteManager.findByIdManager(8)).size() == 3);
      assertTrue(
         objetNonConformeDao.findByObjetAndEntite(derive3.getProdDeriveId(), entiteManager.findByIdManager(8)).size() == 3);

      // clean up
      for(int i = 0; i < derives.size(); i++){
         prodDeriveManager.removeObjectManager(derives.get(i), null, utilisateur, null);
      }

      testFindAll();
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(derives);
      cleanUpFantomes(fs);
   }

   @Test
   public void testRemoveUsedAndCessedObject(){
      boolean catched = false;
      final Utilisateur u = utilisateurDao.findById(1);
      final ProdDerive p1 = prodDeriveManager.findByIdManager(1);
      try{
         prodDeriveManager.removeObjectManager(p1, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("derive.deletion.isUsedNonCascade"));
         assertFalse(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);
      catched = false;

      final ProdDerive p3 = prodDeriveManager.findByIdManager(3);
      try{
         prodDeriveManager.removeObjectManager(p3, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("derive.deletion.isUsedNonCascade"));
         assertFalse(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);
      catched = false;
   }

   @Test
   public void testCascadeDeriveChildren(){
      final Utilisateur u = utilisateurDao.findById(1);
      final ProdDerive p = new ProdDerive();
      /*Champs obligatoires*/
      final Banque b = banqueDao.findById(2);
      p.setCode("DerivesWithDerives");
      final ProdType pType = prodTypeManager.findByIdManager(1);
      final ObjetStatut st = objetStatutManager.findByIdManager(4);

      prodDeriveManager.createObjectManager(p, b, pType, st, null, null, null, null, null, null, null, null, null, null, null, u,
         false, "/tmp/", false);

      final Retour r = new Retour();
      /*Champs obligatoires*/
      r.setTempMoyenne(new Float(22.0));
      r.setDateSortie(Calendar.getInstance());
      r.setDateRetour(Calendar.getInstance());

      //insertion valide
      retourManager.createOrUpdateObjectManager(r, p, null, null, null, null, null, u, "creation");

      assertTrue(retourManager.getRetoursForObjectManager(p).size() == 1);
      assertTrue(retourManager.findAllObjectsManager().size() == 9);

      final Transformation transfo1 = new Transformation();
      final Entite entite = entiteManager.findByIdManager(8);
      transfo1.setObjetId(p.getProdDeriveId());
      transformationManager.createObjectManager(transfo1, entite, null);

      final List<ProdDerive> derives = new ArrayList<>();
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      final ProdDerive derive1 = new ProdDerive();
      derive1.setCode("D1");
      derive1.setProdType(pType);
      derive1.setObjetStatut(st);
      derives.add(derive1);
      final ProdDerive derive2 = new ProdDerive();
      derive2.setCode("D2");
      derive2.setProdType(pType);
      derive2.setObjetStatut(st);
      derives.add(derive2);
      final ProdDerive derive3 = new ProdDerive();
      derive3.setCode("D3");
      derive3.setProdType(pType);
      derive3.setObjetStatut(st);
      derives.add(derive3);

      prodDeriveManager.createDeriveListWithAnnotsManager(derives, b, transfo1, utilisateur, null, null, null, null);

      assertTrue(transformationManager.findAllDeriveFromParentManager(p).size() == 3);

      // delete derive impossible car used -> cascadable
      boolean catched = false;
      try{
         prodDeriveManager.removeObjectManager(p, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("derive.deletion.isUsedCascade"));
         assertTrue(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);

      // delete cascade -> broken à cause d'une cession
      final Cession c = cessionDao.findById(1);
      final CederObjet ced2 = new CederObjet();
      ced2.setObjetId(derive3.getProdDeriveId());
      ced2.setQuantite((float) 10.0);
      final Unite qteUnite = uniteManager.findByIdManager(5);
      cederObjetManager.createObjectManager(ced2, c, entiteManager.findByNomManager("ProdDerive").get(0), qteUnite);

      catched = false;
      try{
         prodDeriveManager.removeObjectCascadeManager(p, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("derive.cascade.isCessed"));
         assertFalse(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);

      // suppr ceder objet pour pouvoir cascader
      cederObjetManager.removeObjectManager(ced2);
      prodDeriveManager.removeObjectCascadeManager(p, "cascade!!!", u, null);

      testFindAll();
      assertTrue(transformationManager.findAllObjectsManager().size() == 5);
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
      assertTrue(retourManager.findAllObjectsManager().size() == 8);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p);
      fs.addAll(derives);
      cleanUpFantomes(fs);
   }

   @Test
   public void testFindByParentManager(){
      final Echantillon e1 = echantillonManager.findByIdManager(1);
      List<ProdDerive> derives = prodDeriveManager.findByParentManager(e1, false);
      assertTrue(derives.size() == 1);

      derives = prodDeriveManager.findByParentManager(e1, true);
      assertTrue(derives.size() == 2);

      final ProdDerive p1 = prodDeriveManager.findByIdManager(1);
      derives = prodDeriveManager.findByParentManager(p1, true);
      assertTrue(derives.size() == 1);

      final ProdDerive p2 = prodDeriveManager.findByIdManager(2);
      derives = prodDeriveManager.findByParentManager(p2, true);
      assertTrue(derives.size() == 0);

      derives = prodDeriveManager.findByParentManager(null, true);
      assertTrue(derives.size() == 0);
   }

   @Test
   public void testCreateUpdateDeriveWithAnnots(){

      // créé un échantillon
      final Banque b1 = banqueManager.findByIdManager(1);
      final ProdType type = prodTypeManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      // on insert 1 nouveau derive pour les tests
      final ProdDerive derive = new ProdDerive();
      derive.setCode("DeriveAnnotated");

      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final List<AnnotationValeur> listDelete = new ArrayList<>();

      final ChampAnnotation c = champAnnotationDao.findById(17);

      final AnnotationValeur a1 = new AnnotationValeur();
      a1.setChampAnnotation(c);
      a1.setAlphanum("alpha1");
      a1.setBanque(b1);
      listAnnots.add(a1);

      // teste erreur sur annots -> rollback
      a1.setAlphanum("&é**$$$¤¤");
      boolean catched = false;
      try{
         prodDeriveManager.createObjectManager(derive, b1, type, objetStatutManager.findByIdManager(4), null, null, null, null,
            null, null, null, null, listAnnots, null, null, utilisateur, true, "/tmp/", false);
      }catch(final ValidationException ve){
         catched = true;
      }
      assertTrue(catched);

      // verification de l'etat de la base
      testFindAll();

      a1.setAlphanum("alpha1");

      prodDeriveManager.createObjectManager(derive, b1, type, objetStatutManager.findByIdManager(4), null, null, null, null, null,
         null, null, null, listAnnots, null, null, utilisateur, true, "/tmp/", false);

      assertTrue(prodDeriveManager.findByCodeLikeManager("deriveAnnotated", true).size() == 1);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, derive).get(0).getAlphanum().equals("alpha1"));
      assertTrue(getOperationManager().findByObjectManager(derive).size() == 2);
      assertTrue(getOperationManager()
         .findByObjetIdEntiteAndOpeTypeManager(derive, operationTypeDao.findByNom("Annotation").get(0)).size() == 1);

      // on teste une deletion puis insertion nouvelle valeur
      listDelete.add(annotationValeurManager.findByChampAndObjetManager(c, derive).get(0));
      listAnnots.clear();
      final AnnotationValeur a2 = new AnnotationValeur();
      a2.setChampAnnotation(c);
      a2.setAlphanum("789");
      a2.setBanque(b1);
      listAnnots.add(a2);
      final AnnotationValeur bool1 = new AnnotationValeur();
      final ChampAnnotation c2 = champAnnotationDao.findById(18);
      bool1.setChampAnnotation(c2);
      bool1.setBool(true);
      bool1.setBanque(b1);
      listAnnots.add(bool1);

      prodDeriveManager.updateObjectManager(derive, b1, type, null, null, null, null, null, null, null, null, null, listAnnots,
         listDelete, null, null, null, utilisateur, true, null, "/tmp/");

      assertTrue(prodDeriveManager.findByCodeLikeManager("deriveAnnotated", true).size() == 1);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, derive).size() == 1);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, derive).get(0).getAlphanum().equals("789"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c2, derive).get(0).getBool());
      assertTrue(getOperationManager().findByObjectManager(derive).size() == 4);
      assertTrue(getOperationManager()
         .findByObjetIdEntiteAndOpeTypeManager(derive, operationTypeDao.findByNom("Annotation").get(0)).size() == 2);

      // suppression annots
      listDelete.add(annotationValeurManager.findByChampAndObjetManager(c, derive).get(0));
      //		listDelete.add(annotationValeurManager
      //				.findByChampAndObjetManager(c2, echan).get(0));
      listAnnots.clear();

      prodDeriveManager.updateObjectManager(derive, b1, type, null, null, null, null, null, null, null, null, null, listAnnots,
         listDelete, null, null, null, utilisateur, true, null, "/tmp/");

      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, derive).size() == 0);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c2, derive).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(derive).size() == 6);
      assertTrue(getOperationManager()
         .findByObjetIdEntiteAndOpeTypeManager(derive, operationTypeDao.findByNom("Annotation").get(0)).size() == 3);

      // Nettoyage
      prodDeriveManager.removeObjectManager(derive, null, utilisateur, null);

      assertTrue(getOperationManager().findByObjectManager(derive).size() == 0);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);
      testFindAll();

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(derive);
      cleanUpFantomes(fs);
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
      prodDeriveManager.switchBanqueCascadeManager(null, b3, false, u, null);
      ProdDerive p = new ProdDerive();
      p.setCode("JEG.1.2");
      // ne doit pas planter getBanque() == null
      prodDeriveManager.switchBanqueCascadeManager(p, null, false, u, null);
      prodDeriveManager.createObjectManager(p, b3, prodTypeManager.findByIdManager(1), objetStatutManager.findByIdManager(4),
         null, null, null, null, null, null, null, null, null, null, null, u, false, "/tmp/", false);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);
      p = prodDeriveManager.findByIdManager(p.getProdDeriveId());

      // banque depart = banque arrivee
      prodDeriveManager.switchBanqueCascadeManager(p, b3, false, u, null);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 1);

      // doublon
      // @since 2.1 doublon code + PF 
      // switchBanque doit retourner doublon exception si déplacement entre deux pfs
      final ProdDerive pbis = new ProdDerive();
      pbis.setCode("JEG.1.2");
      pbis.setObjetStatut(objetStatutManager.findByIdManager(4));
      prodDeriveManager.createObjectManager(pbis, b4, prodTypeManager.findByIdManager(1), objetStatutManager.findByIdManager(4),
         null, null, null, null, null, null, null, null, null, null, null, u, false, "/tmp/", false);
      assertTrue(banqueManager.getProdDerivesManager(b4).size() == 1);
      boolean catched = false;
      try{
         prodDeriveManager.switchBanqueCascadeManager(pbis, b2, false, u, null);
      }catch(final DoublonFoundException dbe){
         catched = true;
      }
      assertTrue(catched);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b4).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 1);

      p.setCode("switchDerive");
      prodDeriveManager.updateObjectManager(p, p.getBanque(), p.getProdType(), p.getObjetStatut(), null, null, null, null, null,
         null, null, null, null, null, null, null, null, u, false, null, null);
      prodDeriveManager.switchBanqueCascadeManager(p, b2, false, u, null);
      // switched ok
      assertTrue(banqueManager.getProdDerivesManager(b3).isEmpty());
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 3);

      // prodDerive cascade 2 -> 3
      final Transformation transfo1 = new Transformation();
      transfo1.setObjetId(p.getProdDeriveId());
      transformationManager.createObjectManager(transfo1, entiteManager.findByIdManager(8), null);
      ProdDerive derive = new ProdDerive();
      derive.setCode("deriveSwitch");
      prodDeriveManager.createObjectManager(derive, b2, prodTypeManager.findByIdManager(1), objetStatutManager.findByIdManager(4),
         null, null, null, null, null, null, null, transfo1, null, null, null, u, false, "/tmp/", false);
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 3);
      prodDeriveManager.switchBanqueCascadeManager(p, b3, true, u, null);
      // switched ok
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 2);
      derive = prodDeriveManager.findByIdManager(derive.getProdDeriveId());

      // cession du derive -> erreur
      final Cession cession = cessionDao.findById(1);
      final Entite entite = entiteManager.findByIdManager(8);
      final CederObjet ced1 = new CederObjet();
      ced1.setObjetId(p.getProdDeriveId());
      cederObjetManager.createObjectManager(ced1, cession, entite, null);
      catched = false;
      try{
         prodDeriveManager.switchBanqueCascadeManager(p, b4, true, u, null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("derive.switchBanque.isCessed"));
      }
      assertTrue(catched);
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 4);

      cederObjetManager.removeObjectManager(ced1);
      final CederObjet ced2 = new CederObjet();
      ced2.setObjetId(derive.getProdDeriveId());
      cederObjetManager.createObjectManager(ced2, cession, entite, null);
      catched = false;
      try{
         prodDeriveManager.switchBanqueCascadeManager(p, b4, true, u, null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("derive.switchBanque.isCessed"));
      }
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 4);

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
      emplacementManager.createObjectManager(emp, t1, entite);
      prodDeriveManager.updateObjectManager(derive, derive.getBanque(), derive.getProdType(), null, null, emp, null, null, null,
         null, null, derive.getTransformation(), null, null, null, null, null, u, false, null, "/tmp/");

      try{
         prodDeriveManager.switchBanqueCascadeManager(derive, b2, true, u, null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("derive.switchBanque.badBanqueStockage"));
      }
      assertTrue(catched);
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 4);

      emp.setObjetId(p.getProdDeriveId());
      emplacementManager.updateObjectManager(emp, emp.getTerminale(), entite);
      emp = emplacementManager.findByIdManager(emp.getEmplacementId());
      emp.getTerminale().getEnceinte();
      prodDeriveManager.updateObjectManager(derive, derive.getBanque(), derive.getProdType(), null, null, null, null, null, null,
         null, null, transfo1, null, null, null, null, null, u, false, null, "/tmp/");
      prodDeriveManager.updateObjectManager(p, p.getBanque(), p.getProdType(), null, null, emp, null, null, null, null, null,
         null, null, null, null, null, null, u, false, null, "/tmp/");
      // e = echantillonManager.findByIdManager(e.getEchantillonId());

      try{
         prodDeriveManager.switchBanqueCascadeManager(p, b2, true, u, null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("derive.switchBanque.badBanqueStockage"));
      }
      assertTrue(catched);
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 5);

      // remise des cessions qui ne doivent pas lancer d'erreur si 
      // banques même pf
      cederObjetManager.createObjectManager(ced1, cession, entite, null);
      cederObjetManager.createObjectManager(ced2, cession, entite, null);

      // annotations
      prodDeriveManager.switchBanqueCascadeManager(p, b1, true, u, null);
      // switched ok
      assertTrue(banqueManager.getProdDerivesManager(b3).isEmpty());
      assertTrue(banqueManager.getProdDerivesManager(b1).size() == 5);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 6);

      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final AnnotationValeur num = new AnnotationValeur();
      num.setChampAnnotation(champAnnotationDao.findById(17));
      num.setAlphanum("123456");
      num.setBanque(b1);
      listAnnots.add(num);
      final AnnotationValeur bool = new AnnotationValeur();
      bool.setChampAnnotation(champAnnotationDao.findById(18));
      bool.setBool(true);
      bool.setBanque(b1);
      listAnnots.add(bool);
      prodDeriveManager.updateObjectManager(p, p.getBanque(), p.getProdType(), null, null, emp, null, null, null, null, null,
         null, listAnnots, null, null, null, null, u, false, null, "/tmp/");
      assertTrue(annotationValeurManager.findByObjectManager(p).size() == 2);

      prodDeriveManager.switchBanqueCascadeManager(p, b3, true, u, null);
      // switched ok
      assertTrue(annotationValeurManager.findByObjectManager(p).size() == 0);

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
      prodDeriveManager.removeObjectCascadeManager(p, null, u, null);
      prodDeriveManager.removeObjectCascadeManager(pbis, null, u, null);
      emp.setEntite(null);
      emp.setObjetId(null);
      emp.setVide(true);
      emplacementManager.removeObjectManager(emp);
      conteneurManager.removeObjectManager(conteneurManager.findByIdManager(c.getConteneurId()), null, u);
      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p);
      fs.add(pbis);
      fs.add(derive);
      fs.add(c);
      fs.addAll(encs);
      fs.addAll(terminales);
      cleanUpFantomes(fs);
   }

   @Test
   public void testExtractValueForChampManager(){
      final ProdDerive p1 = prodDeriveManager.findByIdManager(1);
      final Champ c54 = new Champ(champEntiteDao.findById(54));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(p1, c54).equals("PTRA.1"));

      final Champ c3 = new Champ(champEntiteDao.findById(3));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(p1, c3).equals("DELPHINO"));

      final Champ c23 = new Champ(champEntiteDao.findById(23));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(p1, c23).equals("PRLVT1"));

      final Champ c18 = new Champ(champEntiteDao.findById(18));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(p1, c18).equals("C45.3"));

      final Champ c79 = new Champ(champEntiteDao.findById(79));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(p1, c79).equals("PTRA.1.1"));

      // test sur le risque
      final Champ c249 = new Champ(champEntiteDao.findById(249));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(p1, c249).equals("LEUCEMIE, HIV"));

      // test sur l'emplacement
      final Champ c87 = new Champ(champEntiteDao.findById(87));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(p1, c87).equals("CC1.R1.T1.BT1.A-A"));

      // test sur les codes organes de l'échantillon
      final Champ c229 = new Champ(champEntiteDao.findById(229));
      assertTrue(extractValueFromChampManager.extractValueForChampManager(p1, c229).equals("BL, face dorsale de la langue"));

      // test sur les codes morphos de l'échantillon
      final Champ c230 = new Champ(champEntiteDao.findById(230));
      assertTrue(
         extractValueFromChampManager.extractValueForChampManager(p1, c230).equals("langue plicaturée, BL0211-2, D5-22050"));

      final ProdDerive nully = null;
      assertNull(extractValueFromChampManager.extractValueForChampManager(nully, c18));
      assertNull(extractValueFromChampManager.extractValueForChampManager(p1, null));
      assertNull(extractValueFromChampManager.extractValueForChampManager(nully, null));
   }

   @Test
   public void testStatutEmplacementValidation(){
      final ProdDerive e = new ProdDerive();
      e.setCode("test");
      final Emplacement emp = new Emplacement();
      final ObjetStatut o = new ObjetStatut();

      e.setObjetStatut(o);

      boolean catched = false;
      o.setStatut("RESERVE");
      try{
         BeanValidator.validateObject(e, new Validator[] {prodDeriveValidator});
      }catch(final ValidationException ve){
         assertEquals("prodDerive.statut.incoherent", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);

      catched = false;
      o.setStatut("STOCKE");
      try{
         BeanValidator.validateObject(e, new Validator[] {prodDeriveValidator});
      }catch(final ValidationException ve){
         assertEquals("prodDerive.statut.incoherent", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);

      e.setEmplacement(emp);

      BeanValidator.validateObject(e, new Validator[] {prodDeriveValidator});

      catched = false;
      o.setStatut("NON STOCKE");
      try{
         BeanValidator.validateObject(e, new Validator[] {prodDeriveValidator});
      }catch(final ValidationException ve){
         assertEquals("prodDerive.statut.incoherent", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);

      catched = false;
      o.setStatut("DETRUIT");
      try{
         BeanValidator.validateObject(e, new Validator[] {prodDeriveValidator});
      }catch(final ValidationException ve){
         assertEquals("prodDerive.statut.incoherent", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);

      catched = false;
      o.setStatut("EPUISE");
      try{
         BeanValidator.validateObject(e, new Validator[] {prodDeriveValidator});
      }catch(final ValidationException ve){
         assertEquals("prodDerive.statut.incoherent", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);
   }

   @Test
   public void testCreateUpdateWithNonConformites(){
      ProdDerive p1 = new ProdDerive();
      p1.setCode("p1");
      final Utilisateur u = utilisateurDao.findById(1);

      final List<NonConformite> ncfsTrait = new ArrayList<>();
      final List<NonConformite> ncfsCess = new ArrayList<>();

      final ProdDerive p0 = new ProdDerive();
      p0.setCode("p0");

      prodDeriveManager.createObjectWithNonConformitesManager(p0, banqueDao.findById(1), prodTypeManager.findByIdManager(1),
         objetStatutManager.findByIdManager(4), null, null, null, null, null, null, null, null, null, null, u, false, null, false,
         ncfsTrait, ncfsCess);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(p0.getProdDeriveId(), entiteManager.findByIdManager(8)).size() == 0);

      assertNull(p0.getConformeTraitement());
      assertNull(p0.getConformeCession());

      ncfsTrait.add(nonConformiteDao.findById(8));
      ncfsTrait.add(nonConformiteDao.findById(9));
      ncfsCess.add(nonConformiteDao.findById(10));

      prodDeriveManager.createObjectWithNonConformitesManager(p1, banqueDao.findById(1), prodTypeManager.findByIdManager(1),
         objetStatutManager.findByIdManager(4), null, null, null, null, null, null, null, null, null, null, u, false, null, false,
         ncfsTrait, ncfsCess);

      p1 = prodDeriveManager.findByCodeLikeManager("p1", true).get(0);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(p1.getProdDeriveId(), entiteManager.findByIdManager(8)).size() == 3);
      assertFalse(p1.getConformeTraitement());
      assertFalse(p1.getConformeCession());

      final ProdDerive p2 = new ProdDerive();
      p2.setCode("p2");

      prodDeriveManager.createObjectWithNonConformitesManager(p2, banqueDao.findById(1), prodTypeManager.findByIdManager(1),
         objetStatutManager.findByIdManager(4), null, null, null, null, null, null, null, null, null, null, u, false, null, false,
         null, null);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(p2.getProdDeriveId(), entiteManager.findByIdManager(8)).size() == 0);

      assertNull(p2.getConformeTraitement());
      assertNull(p2.getConformeCession());

      prodDeriveManager.updateObjectWithNonConformitesManager(p1, banqueDao.findById(1), prodTypeManager.findByIdManager(1),
         objetStatutManager.findByIdManager(4), null, null, null, null, null, null, null, null, null, null, null, u, false, null,
         null, ncfsTrait, ncfsCess);

      p1 = prodDeriveManager.findByCodeLikeManager("p1", true).get(0);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(p1.getProdDeriveId(), entiteManager.findByIdManager(8)).size() == 3);

      ncfsCess.clear();
      ncfsTrait.clear();
      p1.setConformeCession(true);
      p1.setConformeTraitement(true);

      prodDeriveManager.updateObjectWithNonConformitesManager(p1, banqueDao.findById(1), prodTypeManager.findByIdManager(1),
         objetStatutManager.findByIdManager(4), null, null, null, null, null, null, null, null, null, null, null, u, false, null,
         null, ncfsTrait, ncfsCess);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(p1.getProdDeriveId(), entiteManager.findByIdManager(8)).size() == 0);

      assertTrue(p1.getConformeTraitement());
      assertTrue(p1.getConformeCession());

      ncfsTrait.add(nonConformiteDao.findById(8));
      ncfsCess.add(nonConformiteDao.findById(11));

      prodDeriveManager.updateObjectWithNonConformitesManager(p1, banqueDao.findById(1), prodTypeManager.findByIdManager(1),
         objetStatutManager.findByIdManager(4), null, null, null, null, null, null, null, null, null, null, null, u, false, null,
         null, ncfsTrait, ncfsCess);

      assertFalse(p1.getConformeTraitement());
      assertFalse(p1.getConformeCession());

      assertTrue(objetNonConformeDao.findByObjetAndEntite(p1.getProdDeriveId(), entiteManager.findByIdManager(8)).size() == 2);
      assertTrue(objetNonConformeDao
         .findByObjetEntiteAndType(p1.getProdDeriveId(), entiteManager.findByIdManager(8), conformiteTypeDao.findById(4))
         .size() == 1);
      assertTrue(objetNonConformeDao
         .findByObjetEntiteAndType(p1.getProdDeriveId(), entiteManager.findByIdManager(8), conformiteTypeDao.findById(5))
         .size() == 1);

      prodDeriveManager.removeObjectManager(p1, null, u, null);
      prodDeriveManager.removeObjectManager(p2, null, u, null);
      prodDeriveManager.removeObjectManager(p0, null, u, null);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p1);
      fs.add(p2);
      fs.add(p0);
      cleanUpFantomes(fs);
      assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
      assertTrue(objetNonConformeDao.findAll().size() == 6);
   }

   @Test
   public void testCreateDerivesEmplacement() throws ParseException{
      final Banque b2 = banqueDao.findById(2);
      final Utilisateur u2 = utilisateurDao.findById(2);

      final List<ProdDerive> derives = new ArrayList<>();
      final ProdDerive pd1 = new ProdDerive();
      pd1.setCode("PD1");
      pd1.setBanque(b2);
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      final ProdDerive pd2 = new ProdDerive();
      pd2.setCode("PD2");
      pd2.setBanque(b2);
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      final ProdDerive pd3 = new ProdDerive();
      pd3.setCode("PD3");
      pd3.setBanque(b2);
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      final Hashtable<ProdDerive, Emplacement> empls = new Hashtable<>();
      empls.put(pd1, emplacementManager.findByIdManager(7));
      // Emplacement new1 = new Emplacement();
      // new1.setTerminale(terminaleDao.findById(6));
      // new1.setPosition(3);
      // empls.put(pd2, new1);
      final Emplacement new2 = new Emplacement();
      new2.setTerminale(terminaleDao.findById(6));
      new2.setPosition(4);
      empls.put(pd3, new2);

      final Calendar dateS = Calendar.getInstance();
      dateS.setTime(new SimpleDateFormat("dd/MM/yy HH:mm").parse("12/12/2012 22:21"));

      // 1 Sans parent		
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, null, null, empls, null, null, null, null, null, false, null,
         null);

      final List<ProdDerive> created = prodDeriveManager.findByCodeOrLaboWithBanqueManager("PD_", b2, false);
      assertTrue(created.size() == 3);
      assertTrue(created.get(0).getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(created.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertTrue(created.get(2).getObjetStatut().getStatut().equals("STOCKE"));

      final List<Emplacement> emps = emplacementManager.findByTerminaleWithOrder(terminaleDao.findById(6));
      assertTrue(emps.size() == 3);
      assertTrue(emps.get(0).getVide() && emps.get(0).getEntite() == null && emps.get(0).getObjetId() == null);
      assertTrue(!emps.get(1).getVide() && emps.get(1).getEntite().getEntiteId() == 8
         && emps.get(1).getObjetId().equals(pd1.getProdDeriveId()));
      assertTrue(prodDeriveManager.getEmplacementManager(pd1).equals(emps.get(1)));
      // assertTrue(!emps.get(2).getVide() && emps.get(2).getEntite().getEntiteId() == 8 
      //		&& emps.get(2).getObjetId().equals(pd2.getProdDeriveId()));
      assertNull(prodDeriveManager.getEmplacementManager(pd2));
      assertTrue(!emps.get(2).getVide() && emps.get(2).getEntite().getEntiteId() == 8
         && emps.get(2).getObjetId().equals(pd3.getProdDeriveId()));
      assertTrue(prodDeriveManager.getEmplacementManager(pd3).equals(emps.get(2)));
      assertFalse(getOperationManager().findByObjetIdEntiteAndOpeTypeManager(pd1, operationTypeDao.findByNom("Stockage").get(0))
         .isEmpty());
      assertTrue(getOperationManager().findByObjetIdEntiteAndOpeTypeManager(pd2, operationTypeDao.findByNom("Stockage").get(0))
         .isEmpty());
      assertFalse(getOperationManager().findByObjetIdEntiteAndOpeTypeManager(pd3, operationTypeDao.findByNom("Stockage").get(0))
         .isEmpty());

      // clean up
      final Emplacement e7 = emps.get(1);
      e7.setVide(true);
      e7.setEntite(null);
      e7.setObjetId(null);
      emplacementManager.updateObjectManager(e7, e7.getTerminale(), null);

      final ObjetStatut nonstocke = objetStatutManager.findByStatutLikeManager("NON STOCKE", true).get(0);
      pd1.setEmplacement(null);
      pd1.setObjetStatut(nonstocke);
      // pd2.setEmplacement(null); pd2.setObjetStatut(nonstocke);
      pd3.setEmplacement(null);
      pd3.setObjetStatut(nonstocke);
      prodDeriveManager.updateMultipleObjectsManager(derives, derives, null, null, null, null, u2, "/tmp/");

      emplacementManager.removeObjectManager(emps.get(2));
      // emplacementManager.removeObjectManager(emps.get(3));

      final List<Integer> ids = new ArrayList<>();
      ids.add(pd1.getProdDeriveId());
      ids.add(pd2.getProdDeriveId());
      ids.add(pd3.getProdDeriveId());
      prodDeriveManager.removeListFromIdsManager(ids, null, u2);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(derives);
      cleanUpFantomes(fs);

      testFindAll();
   }

   @Test
   public void testCreateDerivesPrelevement() throws ParseException{
      final Banque b2 = banqueDao.findById(2);
      final Utilisateur u2 = utilisateurDao.findById(2);

      final List<ProdDerive> derives = new ArrayList<>();
      ProdDerive pd1 = new ProdDerive();
      pd1.setCode("PD1");
      pd1.setBanque(b2);
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      ProdDerive pd2 = new ProdDerive();
      pd2.setCode("PD2");
      pd2.setBanque(b2);
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      ProdDerive pd3 = new ProdDerive();
      pd3.setCode("PD3");
      pd3.setBanque(b2);
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      final Hashtable<ProdDerive, List<AnnotationValeur>> annos = new Hashtable<>();
      final List<AnnotationValeur> avs1 = new ArrayList<>();
      final AnnotationValeur avD1 = new AnnotationValeur();
      avD1.setChampAnnotation(champAnnotationDao.findById(17));
      avD1.setAlphanum("v1");
      avD1.setBanque(b2);
      avs1.add(avD1);
      final AnnotationValeur avD2 = new AnnotationValeur();
      avD2.setChampAnnotation(champAnnotationDao.findById(18));
      avD2.setBool(true);
      avD2.setBanque(b2);
      avs1.add(avD2);
      annos.put(pd1, avs1);
      final List<AnnotationValeur> avs2 = new ArrayList<>();
      final AnnotationValeur avD3 = new AnnotationValeur();
      avD3.setChampAnnotation(champAnnotationDao.findById(17));
      avD3.setAlphanum("v33");
      avD3.setBanque(b2);
      avs2.add(avD3);
      annos.put(pd2, avs2);

      final Hashtable<ProdDerive, List<NonConformite>> ncfTraits = new Hashtable<>();
      final List<NonConformite> ncT1 = new ArrayList<>();
      ncT1.add(nonConformiteDao.findById(8));
      ncfTraits.put(pd3, ncT1);
      final List<NonConformite> ncT2 = new ArrayList<>();
      ncT2.add(nonConformiteDao.findById(8));
      ncT2.add(nonConformiteDao.findById(9));
      ncfTraits.put(pd1, ncT2);

      final Hashtable<ProdDerive, List<NonConformite>> ncfCess = new Hashtable<>();
      final List<NonConformite> ncC1 = new ArrayList<>();
      ncC1.add(nonConformiteDao.findById(10));
      ncC1.add(nonConformiteDao.findById(11));
      ncfCess.put(pd3, ncC1);
      final List<NonConformite> ncC2 = new ArrayList<>();
      ncC2.add(nonConformiteDao.findById(10));
      ncfCess.put(pd2, ncC2);

      final Prelevement p1 = prelevementDao.findById(1);
      Prelevement prel = new Prelevement();
      prel.setCode("PRELTOTRANSFORM");
      prel.setQuantite(new Float(100));
      final Unite mL = uniteManager.findByUniteLikeManager("ml", false).get(0);
      prelevementManager.createObjectManager(prel, b2, p1.getNature(), null, p1.getConsentType(), null, null, null, null, null,
         null, null, mL, null, null, null, u2, false, "/tmp/", false);

      // Transformation Prelevement quantite non nulle		
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, prel, annos, null, new Float(69.0), mL, null, null, "/tmp/",
         false, ncfTraits, ncfCess);

      Transformation transfo = transformationManager
         .findByEntiteObjetManager(entiteManager.findByNomManager("Prelevement").get(0), prel.getPrelevementId()).get(0);
      assertTrue(transfo.getQuantite().equals(new Float(69.0)));
      assertTrue(transfo.getQuantiteUnite().equals(mL));
      assertTrue(transformationManager.findAllDeriveFromParentManager(prel).size() == 3);
      prel = prelevementDao.findById(prel.getPrelevementId());
      assertTrue(prel.getQuantite().equals(new Float(31.0)));
      assertTrue(prel.getQuantiteUnite().equals(mL));

      assertTrue(annotationValeurManager.findByObjectManager(pd1).size() == 2);
      assertTrue(annotationValeurManager.findByObjectManager(pd2).size() == 1);
      assertTrue(annotationValeurManager.findByObjectManager(pd2).get(0).getAlphanum().equals("v33"));
      assertTrue(annotationValeurManager.findByObjectManager(pd3).isEmpty());

      assertFalse(pd1.getConformeTraitement());
      assertTrue(objetNonConformeDao
         .findByObjetEntiteAndType(pd1.getProdDeriveId(), entiteManager.findByIdManager(8), conformiteTypeDao.findById(4))
         .size() == 2);
      assertNull(pd1.getConformeCession());
      assertTrue(objetNonConformeDao
         .findByObjetEntiteAndType(pd1.getProdDeriveId(), entiteManager.findByIdManager(8), conformiteTypeDao.findById(5))
         .isEmpty());
      assertNull(pd2.getConformeTraitement());
      assertTrue(objetNonConformeDao
         .findByObjetEntiteAndType(pd2.getProdDeriveId(), entiteManager.findByIdManager(8), conformiteTypeDao.findById(4))
         .isEmpty());
      assertFalse(pd2.getConformeCession());
      assertTrue(objetNonConformeDao
         .findByObjetEntiteAndType(pd2.getProdDeriveId(), entiteManager.findByIdManager(8), conformiteTypeDao.findById(5))
         .size() == 1);
      assertFalse(pd3.getConformeTraitement());
      assertTrue(objetNonConformeDao
         .findByObjetEntiteAndType(pd3.getProdDeriveId(), entiteManager.findByIdManager(8), conformiteTypeDao.findById(4))
         .size() == 1);
      assertFalse(pd3.getConformeCession());
      assertTrue(objetNonConformeDao
         .findByObjetEntiteAndType(pd3.getProdDeriveId(), entiteManager.findByIdManager(8), conformiteTypeDao.findById(5))
         .size() == 2);

      assertTrue(annotationValeurManager.findByObjectManager(pd2).size() == 1);
      assertTrue(annotationValeurManager.findByObjectManager(pd2).get(0).getAlphanum().equals("v33"));
      assertTrue(annotationValeurManager.findByObjectManager(pd3).isEmpty());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(derives);

      // Prelevement parent quantite nulle
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1b");
      pd1.setBanque(b2);
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2b");
      pd2.setBanque(b2);
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3b");
      pd3.setBanque(b2);
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      Prelevement prel2 = new Prelevement();
      prel2.setCode("PRELTOTRANSFORMQTENULLE");
      prelevementManager.createObjectManager(prel2, b2, p1.getNature(), null, p1.getConsentType(), null, null, null, null, null,
         null, null, null, null, null, null, u2, false, "/tmp/", false);

      // Transformation Prelevement quantite non nulle		
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, prel2, null, null, new Float(12.0), null, null, null, "/tmp/",
         false, null, null);

      transfo = transformationManager
         .findByEntiteObjetManager(entiteManager.findByNomManager("Prelevement").get(0), prel2.getPrelevementId()).get(0);
      assertTrue(transfo.getQuantite().equals(new Float(12.0)));
      assertNull(transfo.getQuantiteUnite());
      assertTrue(transformationManager.findAllDeriveFromParentManager(prel2).size() == 3);
      prel2 = prelevementDao.findById(prel2.getPrelevementId());
      assertNull(prel2.getQuantite());
      assertNull(prel2.getQuantiteUnite());

      fs.addAll(derives);

      // Prelevement parent quantite non nulle transfo nulle
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1c");
      pd1.setBanque(b2);
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2c");
      pd2.setBanque(b2);
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3c");
      pd3.setBanque(b2);
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      Prelevement prel3 = new Prelevement();
      prel3.setCode("TRANSFOQTENULLE");
      prel3.setQuantite(new Float(84));
      prelevementManager.createObjectManager(prel3, b2, p1.getNature(), null, p1.getConsentType(), null, null, null, null, null,
         null, null, mL, null, null, null, u2, false, "/tmp/", false);

      // Transformation Prelevement quantite non nulle quantite transfo nulle		
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, prel3, null, null, null, null, null, null, "/tmp/", false, null,
         null);

      transfo = transformationManager
         .findByEntiteObjetManager(entiteManager.findByNomManager("Prelevement").get(0), prel3.getPrelevementId()).get(0);
      assertNull(transfo.getQuantite());
      assertNull(transfo.getQuantiteUnite());
      assertTrue(transformationManager.findAllDeriveFromParentManager(prel3).size() == 3);
      prel3 = prelevementDao.findById(prel3.getPrelevementId());
      assertTrue(prel3.getQuantite().equals(new Float(84)));
      assertTrue(prel3.getQuantiteUnite().equals(mL));

      final List<Integer> ids = new ArrayList<>();
      ids.add(prel.getPrelevementId());
      ids.add(prel2.getPrelevementId());
      ids.add(prel3.getPrelevementId());
      prelevementManager.removeListFromIdsManager(ids, null, u2);

      fs.addAll(derives);
      fs.add(prel);
      fs.add(prel2);
      fs.add(prel3);
      cleanUpFantomes(fs);

      testFindAll();
   }

   @Test
   public void testCreateDerivesEchantillon() throws ParseException{
      final Banque b2 = banqueDao.findById(2);
      final Utilisateur u2 = utilisateurDao.findById(2);

      final List<ProdDerive> derives = new ArrayList<>();
      ProdDerive pd1 = new ProdDerive();
      pd1.setCode("PD1");
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      ProdDerive pd2 = new ProdDerive();
      pd2.setCode("PD2");
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      ProdDerive pd3 = new ProdDerive();
      pd3.setCode("PD3");
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      final Echantillon e1 = echantillonManager.findByIdManager(1);
      Echantillon ech = new Echantillon();
      ech.setCode("ECHTOTRANSFORM");
      ech.setQuantite(new Float(100));
      final Unite mL = uniteManager.findByUniteLikeManager("ml", false).get(0);
      Emplacement new2 = new Emplacement();
      new2.setTerminale(terminaleDao.findById(6));
      new2.setPosition(4);
      new2.setVide(false);
      echantillonManager.createObjectManager(ech, b2, null, null, e1.getObjetStatut(), new2, e1.getEchantillonType(), null, mL,
         null, null, null, null, null, u2, false, "/tmp/", false);
      new2 = echantillonManager.getEmplacementManager(ech);
      new2.setObjetId(ech.getEchantillonId());
      emplacementManager.updateObjectManager(new2, new2.getTerminale(), entiteManager.findByNomManager("Echantillon").get(0));

      ech = echantillonManager.findByIdManager(ech.getEchantillonId());
      assertTrue(ech.getObjetStatut().getStatut().equals("STOCKE"));

      // statut incoherent
      ObjectStatutException e = null;
      try{
         // Transformation incomplete Echantillon stocke quantite non nulle	
         prodDeriveManager.createProdDerivesManager(derives, b2, u2, echantillonManager.findByIdManager(3), // EHT.1 EPUISE
            null, null, new Float(25.0), mL, null, null, null, false, null, null);
      }catch(final DeriveBatchSaveException oe){
         e = (ObjectStatutException) oe.getTargetExeption();
      }
      assertTrue(e != null);
      assertTrue(e.getEntite().equals("Echantillon"));
      assertTrue(e.getOperation().equals("transformation"));

      // Transformation incomplete Echantillon stocke quantite non nulle	
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, ech, null, null, new Float(25.0), mL, null, null, null, false,
         null, null);

      final Transformation transfo = transformationManager
         .findByEntiteObjetManager(entiteManager.findByNomManager("Echantillon").get(0), ech.getEchantillonId()).get(0);
      assertTrue(transfo.getQuantite().equals(new Float(25.0)));
      assertTrue(transfo.getQuantiteUnite().equals(mL));
      assertTrue(transformationManager.findAllDeriveFromParentManager(ech).size() == 3);
      assertTrue(ech.getQuantite().equals(new Float(75.0)));
      assertTrue(ech.getQuantiteUnite().equals(mL));
      assertTrue(ech.getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(echantillonManager.getEmplacementManager(ech) != null);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(derives);

      // Transfo quantite nulle
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1b");
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2b");
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3b");
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // Transformation incomplete Echantillon stocke quantite non nulle	
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, ech, null, null, null, null, null, null, null, false, null,
         null);

      final Transformation transfo2 = pd1.getTransformation();
      assertNull(transfo2.getQuantite());
      assertNull(transfo2.getQuantiteUnite());
      assertTrue(transformationManager.findAllDeriveFromParentManager(ech).size() == 6);
      assertTrue(prodDeriveManager.findByTransformationManager(transfo2).size() == 3);
      assertTrue(ech.getQuantite().equals(new Float(75.0)));
      assertTrue(ech.getQuantiteUnite().equals(mL));
      assertTrue(ech.getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(echantillonManager.getEmplacementManager(ech) != null);

      fs.addAll(derives);
      // Epuisement
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1c");
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2c");
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3c");
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // Transformation epuisement
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, ech, null, null, new Float(75.0), mL, null, "OBS", null, false,
         null, null);

      final Transformation transfo3 = pd1.getTransformation();
      assertTrue(transfo3.getQuantite().equals(new Float(75.0)));
      assertTrue(transfo3.getQuantiteUnite().equals(mL));
      assertTrue(transformationManager.findAllDeriveFromParentManager(ech).size() == 9);
      assertTrue(prodDeriveManager.findByTransformationManager(transfo3).size() == 3);
      assertTrue(ech.getQuantite().equals(new Float(0.0)));
      assertTrue(ech.getQuantiteUnite().equals(mL));
      assertTrue(ech.getObjetStatut().getStatut().equals("EPUISE"));
      assertTrue(echantillonManager.getEmplacementManager(ech) == null);
      assertTrue(getOperationManager().findByObjetIdEntiteAndOpeTypeManager(ech, operationTypeDao.findByNom("Destockage").get(0))
         .size() == 1);
      // epuisement -> creation Retour
      assertTrue(retourManager.getRetoursForObjectManager(ech).size() == 1);
      Retour epuisement = retourManager.getRetoursForObjectManager(ech).get(0);
      assertTrue(epuisement.getTempMoyenne().equals(new Float(20.0)));
      assertTrue(epuisement.getObservations().equals("OBS"));
      assertTrue(epuisement.getTransformation().equals(transfo3));

      fs.addAll(derives);
      // echantillon NON STOCKE quantite nulle!
      Echantillon ech2 = new Echantillon();
      ech2.setCode("ECHTOTRANSFORM2");
      echantillonManager.createObjectManager(ech2, b2, null, null,
         objetStatutManager.findByStatutLikeManager("NON STOCKE", true).get(0), null, e1.getEchantillonType(), null, null, null,
         null, null, null, null, u2, false, "/tmp/", false);

      ech2 = echantillonManager.findByIdManager(ech2.getEchantillonId());
      assertTrue(ech2.getObjetStatut().getStatut().equals("NON STOCKE"));

      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1d");
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2d");
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3d");
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // Transformation incomplete Echantillon stocke quantite non nulle	
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, ech2, null, null, new Float(25.0), mL, null, null, null, false,
         null, null);

      final Transformation transfo4 = transformationManager
         .findByEntiteObjetManager(entiteManager.findByNomManager("Echantillon").get(0), ech2.getEchantillonId()).get(0);
      assertTrue(transfo4.getQuantite().equals(new Float(25.0)));
      assertTrue(transfo4.getQuantiteUnite().equals(mL));
      assertTrue(transformationManager.findAllDeriveFromParentManager(ech2).size() == 3);
      assertNull(ech2.getQuantite());
      assertNull(ech2.getQuantiteUnite());
      assertTrue(ech2.getObjetStatut().getStatut().equals("NON STOCKE"));
      assertTrue(echantillonManager.getEmplacementManager(ech2) == null);

      fs.addAll(derives);
      // Epuisement non stocke
      ech2.setQuantite(new Float(88.0));
      echantillonManager.updateObjectManager(ech2, b2, null, null, ech2.getObjetStatut(), null, ech2.getEchantillonType(), null,
         null, null, null, null, null, null, null, null, null, u2, false, null, "/tmp/");
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1e");
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2e");
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3e");
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // Transformation epuisement
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, ech2, null, null, new Float(88.0), mL, null, "OBSNS", null,
         false, null, null);

      final Transformation transfo5 = pd1.getTransformation();
      assertTrue(transfo5.getQuantite().equals(new Float(88.0)));
      assertTrue(transfo5.getQuantiteUnite().equals(mL));
      assertTrue(transformationManager.findAllDeriveFromParentManager(ech2).size() == 6);
      assertTrue(prodDeriveManager.findByTransformationManager(transfo5).size() == 3);
      assertTrue(ech2.getQuantite().equals(new Float(0.0)));
      assertNull(ech2.getQuantiteUnite());
      assertTrue(ech2.getObjetStatut().getStatut().equals("EPUISE"));
      assertTrue(echantillonManager.getEmplacementManager(ech2) == null);
      assertTrue(getOperationManager().findByObjetIdEntiteAndOpeTypeManager(ech2, operationTypeDao.findByNom("Destockage").get(0))
         .isEmpty());
      // epuisement -> creation Retour
      assertTrue(retourManager.getRetoursForObjectManager(ech2).size() == 1);
      epuisement = retourManager.getRetoursForObjectManager(ech2).get(0);
      assertTrue(epuisement.getTempMoyenne().equals(new Float(20.0)));
      assertTrue(epuisement.getObservations().equals("OBSNS"));
      assertTrue(epuisement.getTransformation().equals(transfo5));
      assertNotNull(epuisement.getDateSortie());
      assertNull(epuisement.getDateRetour());

      echantillonManager.removeObjectCascadeManager(ech, null, u2, null);
      echantillonManager.removeObjectCascadeManager(ech2, null, u2, null);
      emplacementManager.removeObjectManager(new2);

      //		List<Integer> ids = new ArrayList<Integer>();
      //		ids.add(ech.getPrelevementId());
      //		ids.add(prel2.getPrelevementId());
      //		ids.add(prel3.getPrelevementId());
      //		prelevementManager.removeListFromIdsManager(ids, null, u2);

      fs.addAll(derives);
      fs.add(ech);
      fs.add(ech2);
      cleanUpFantomes(fs);

      testFindAll();
   }

   @Test
   public void testCreateDerivesDerives() throws ParseException{
      final Banque b2 = banqueDao.findById(2);
      final Utilisateur u2 = utilisateurDao.findById(2);

      final Unite mL = uniteManager.findByUniteLikeManager("ml", false).get(0);
      final Unite mG = uniteManager.findByUniteLikeManager("mg", false).get(0);

      final List<ProdDerive> derivesToCreate = new ArrayList<>();
      final ProdDerive der1 = new ProdDerive();
      der1.setCode("DERTOTRANSFORM1");
      der1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      der1.setQuantite(new Float(100));
      der1.setQuantiteInit(new Float(100));
      der1.setQuantiteUnite(mG);
      der1.setVolume(new Float(50));
      der1.setVolumeInit(new Float(50));
      der1.setVolumeUnite(mL);
      der1.setBanque(b2);
      derivesToCreate.add(der1);

      final ProdDerive der2 = new ProdDerive();
      der2.setCode("DERTOTRANSFORM2");
      der2.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      der2.setQuantite(new Float(100));
      der2.setQuantiteInit(new Float(100));
      der2.setQuantiteUnite(mG);
      der2.setBanque(b2);
      derivesToCreate.add(der2);

      final ProdDerive der3 = new ProdDerive();
      der3.setCode("DERTOTRANSFORM3");
      der3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      der3.setVolume(new Float(50));
      der3.setVolumeInit(new Float(50));
      der3.setVolumeUnite(mL);
      der3.setBanque(b2);
      derivesToCreate.add(der3);

      final List<ProdDerive> derives = new ArrayList<>();
      ProdDerive pd1 = new ProdDerive();
      pd1.setCode("PD1");
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      ProdDerive pd2 = new ProdDerive();
      pd2.setCode("PD2");
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      ProdDerive pd3 = new ProdDerive();
      pd3.setCode("PD3");
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      final Hashtable<ProdDerive, Emplacement> empls = new Hashtable<>();
      // Emplacement new1 = new Emplacement();
      // new1.setTerminale(terminaleDao.findById(6));
      // new1.setPosition(3);
      // empls.put(pd2, new1);
      final Emplacement new2 = new Emplacement();
      new2.setTerminale(terminaleDao.findById(6));
      new2.setPosition(4);
      empls.put(der1, new2);

      // 1 Sans parent		
      prodDeriveManager.createProdDerivesManager(derivesToCreate, b2, u2, null, null, empls, null, null, null, null, null, false,
         null, null);

      // Transformation incomplete Derive stocke quantite / volume	
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, der1, null, null, new Float(50.0), mG, null, null, null, false,
         null, null);

      final Transformation transfo = transformationManager
         .findByEntiteObjetManager(entiteManager.findByNomManager("ProdDerive").get(0), der1.getProdDeriveId()).get(0);
      assertTrue(transfo.getQuantite().equals(new Float(50.0)));
      assertTrue(transfo.getQuantiteUnite().equals(mG));
      assertTrue(transformationManager.findAllDeriveFromParentManager(der1).size() == 3);
      assertTrue(der1.getQuantite().equals(new Float(50.0)));
      assertTrue(der1.getQuantiteUnite().equals(mG));
      assertTrue(der1.getVolume().equals(new Float(25.0)));
      assertTrue(der1.getVolumeUnite().equals(mL));
      assertTrue(der1.getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(prodDeriveManager.getEmplacementManager(der1) != null);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(derives);

      //  der2 pas maj volume
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1b");
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2b");
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3b");
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // Transformation incomplete Echantillon stocke quantite non nulle	
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, der2, null, null, new Float(50.0), mG, null, null, null, false,
         null, null);

      final Transformation transfo2 = transformationManager
         .findByEntiteObjetManager(entiteManager.findByNomManager("ProdDerive").get(0), der2.getProdDeriveId()).get(0);
      assertTrue(transfo2.getQuantite().equals(new Float(50.0)));
      assertTrue(transfo2.getQuantiteUnite().equals(mG));
      assertTrue(transformationManager.findAllDeriveFromParentManager(der2).size() == 3);
      assertTrue(der2.getQuantite().equals(new Float(50.0)));
      assertTrue(der2.getQuantiteUnite().equals(mG));
      assertNull(der2.getVolume());
      assertNull(der2.getVolumeUnite());
      assertTrue(der2.getObjetStatut().getStatut().equals("NON STOCKE"));
      assertTrue(prodDeriveManager.getEmplacementManager(der2) == null);

      fs.addAll(derives);
      //  der3 pas quantite = EPUISEMENT impossible
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1c");
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2c");
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3c");
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // Transformation incomplete Echantillon stocke quantite non nulle	
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, der3, null, null, new Float(200.0), mG, null, null, null, false,
         null, null);

      final Transformation transfo3 = transformationManager
         .findByEntiteObjetManager(entiteManager.findByNomManager("ProdDerive").get(0), der3.getProdDeriveId()).get(0);
      assertTrue(transfo3.getQuantite().equals(new Float(200.0)));
      assertTrue(transfo3.getQuantiteUnite().equals(mG));
      assertTrue(transformationManager.findAllDeriveFromParentManager(der3).size() == 3);
      assertNull(der3.getQuantite());
      assertNull(der3.getQuantiteUnite());
      assertTrue(der3.getVolume().equals(new Float(50.0)));
      assertTrue(der3.getVolumeUnite().equals(mL));
      assertTrue(der3.getObjetStatut().getStatut().equals("NON STOCKE"));
      assertTrue(prodDeriveManager.getEmplacementManager(der3) == null);

      fs.addAll(derives);
      // Epuisement der1
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1d");
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2d");
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3d");
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // Transformation epuisement
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, der1, null, null, new Float(50.0), mG, null, "OBS", null, false,
         null, null);

      final Transformation transfo4 = pd1.getTransformation();
      assertTrue(transfo4.getQuantite().equals(new Float(50.0)));
      assertTrue(transfo4.getQuantiteUnite().equals(mG));
      assertTrue(transformationManager.findAllDeriveFromParentManager(der1).size() == 6);
      assertTrue(prodDeriveManager.findByTransformationManager(transfo4).size() == 3);
      assertTrue(der1.getQuantite().equals(new Float(0.0)));
      assertTrue(der1.getQuantiteUnite().equals(mG));
      assertTrue(der1.getVolume().equals(new Float(0.0)));
      assertTrue(der1.getVolumeUnite().equals(mL));
      assertTrue(der1.getObjetStatut().getStatut().equals("EPUISE"));
      assertTrue(prodDeriveManager.getEmplacementManager(der1) == null);
      assertTrue(getOperationManager().findByObjetIdEntiteAndOpeTypeManager(der1, operationTypeDao.findByNom("Destockage").get(0))
         .size() == 1);
      // epuisement -> creation Retour
      assertTrue(retourManager.getRetoursForObjectManager(der1).size() == 1);
      Retour epuisement = retourManager.getRetoursForObjectManager(der1).get(0);
      assertTrue(epuisement.getTempMoyenne().equals(new Float(20.0)));
      assertTrue(epuisement.getObservations().equals("OBS"));
      assertTrue(epuisement.getTransformation().equals(transfo4));

      fs.addAll(derives);
      // Epuisement der2
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1e");
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2e");
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3e");
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // Transformation epuisement
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, der2, null, null, new Float(50.0), mG, null, "OBSNS", null,
         false, null, null);

      final Transformation transfo5 = pd1.getTransformation();
      assertTrue(transfo5.getQuantite().equals(new Float(50.0)));
      assertTrue(transfo5.getQuantiteUnite().equals(mG));
      assertTrue(transformationManager.findAllDeriveFromParentManager(der2).size() == 6);
      assertTrue(prodDeriveManager.findByTransformationManager(transfo5).size() == 3);
      assertTrue(der2.getQuantite().equals(new Float(0.0)));
      assertTrue(der2.getQuantiteUnite().equals(mG));
      assertNull(der2.getVolume());
      assertNull(der2.getVolumeUnite());
      assertTrue(der2.getObjetStatut().getStatut().equals("EPUISE"));
      assertTrue(prodDeriveManager.getEmplacementManager(der2) == null);
      assertTrue(getOperationManager().findByObjetIdEntiteAndOpeTypeManager(der2, operationTypeDao.findByNom("Destockage").get(0))
         .isEmpty());
      // epuisement -> creation Retour
      assertTrue(retourManager.getRetoursForObjectManager(der2).size() == 1);
      epuisement = retourManager.getRetoursForObjectManager(der2).get(0);
      assertTrue(epuisement.getTempMoyenne().equals(new Float(20.0)));
      assertTrue(epuisement.getObservations().equals("OBSNS"));
      assertTrue(epuisement.getTransformation().equals(transfo5));
      assertNotNull(epuisement.getDateSortie());
      assertNull(epuisement.getDateRetour());

      final List<Integer> ids = new ArrayList<>();
      ids.add(der1.getProdDeriveId());
      ids.add(der2.getProdDeriveId());
      ids.add(der3.getProdDeriveId());
      prodDeriveManager.removeListFromIdsManager(ids, null, u2);

      emplacementManager.removeObjectManager(new2);

      fs.addAll(derives);
      fs.add(der1);
      fs.add(der2);
      fs.add(der3);
      cleanUpFantomes(fs);

      testFindAll();
   }

   @Test
   public void testCreateDerivesFocusRetour() throws ParseException{
      final Banque b2 = banqueDao.findById(2);
      final Utilisateur u2 = utilisateurDao.findById(2);

      final List<ProdDerive> derives = new ArrayList<>();
      ProdDerive pd1 = new ProdDerive();
      pd1.setCode("PD1");
      pd1.setBanque(b2);
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      ProdDerive pd2 = new ProdDerive();
      pd2.setCode("PD2");
      pd2.setBanque(b2);
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      ProdDerive pd3 = new ProdDerive();
      pd3.setCode("PD3");
      pd3.setBanque(b2);
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      final Echantillon e1 = echantillonManager.findByIdManager(1);
      Echantillon ech = new Echantillon();
      ech.setCode("ECHTOTRANSFORM");
      ech.setQuantite(new Float(100));
      final Unite mL = uniteManager.findByUniteLikeManager("ml", false).get(0);
      Emplacement new2 = new Emplacement();
      new2.setTerminale(terminaleDao.findById(6));
      new2.setPosition(4);
      echantillonManager.createObjectManager(ech, b2, null, null, e1.getObjetStatut(), new2, e1.getEchantillonType(), null, mL,
         null, null, null, null, null, u2, false, "/tmp/", false);
      new2 = echantillonManager.getEmplacementManager(ech);
      new2.setObjetId(ech.getEchantillonId());
      emplacementManager.updateObjectManager(new2, new2.getTerminale(), entiteManager.findByNomManager("Echantillon").get(0));

      ech = echantillonManager.findByIdManager(ech.getEchantillonId());
      assertTrue(ech.getObjetStatut().getStatut().equals("STOCKE"));

      // Transformation incomplete Echantillon stocke quantite non nulle pas retour		
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, ech, null, null, new Float(25.0), mL, null, null, null, false,
         null, null);

      final Transformation transfo = transformationManager
         .findByEntiteObjetManager(entiteManager.findByNomManager("Echantillon").get(0), ech.getEchantillonId()).get(0);
      assertTrue(transfo.getQuantite().equals(new Float(25.0)));
      assertTrue(transfo.getQuantiteUnite().equals(mL));
      assertTrue(transformationManager.findAllDeriveFromParentManager(ech).size() == 3);
      assertTrue(ech.getQuantite().equals(new Float(75.0)));
      assertTrue(ech.getQuantiteUnite().equals(mL));
      assertTrue(ech.getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(echantillonManager.getEmplacementManager(ech) != null);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(derives);

      // Transformation incomplete Echantillon stocke quantite non nulle avec retour	
      // complete par une date
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1b");
      pd1.setBanque(b2);
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2b");
      pd2.setBanque(b2);
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3b");
      pd3.setBanque(b2);
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // retour incomplete
      Calendar dateSortie = Calendar.getInstance();
      dateSortie.setTimeInMillis(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/12/2012 10:55").getTime());
      Retour incomp = new Retour();
      incomp.setDateSortie(dateSortie);
      incomp.setTempMoyenne(new Float(21.0));
      incomp.setObservations("OBS1");
      retourManager.createOrUpdateObjectManager(incomp, ech, echantillonManager.getEmplacementManager(ech), null, null, null,
         null, u2, "creation");
      final List<Integer> echIds = new ArrayList<>();
      echIds.add(ech.getEchantillonId());
      assertTrue(retourManager.findByObjectDateRetourEmptyManager(echIds, entiteManager.findByNomManager("Echantillon").get(0))
         .get(0).getDateSortie().equals(dateSortie));

      final Calendar dateRetour = Calendar.getInstance();
      dateRetour.setTimeInMillis(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/12/2012 10:58").getTime());

      // Transformation Echantillon stocke quantite non nulle 
      // avec retour empty à compléter		
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, ech, null, null, new Float(30.0), mL, dateRetour, "OBSMODIF",
         null, false, null, null);

      final Transformation transfo2 = pd1.getTransformation();
      assertTrue(transfo2.getQuantite().equals(new Float(30.0)));
      assertTrue(transfo2.getQuantiteUnite().equals(mL));
      assertTrue(transformationManager.findAllDeriveFromParentManager(ech).size() == 6);
      assertTrue(prodDeriveManager.findByTransformationManager(transfo2).size() == 3);
      assertTrue(ech.getQuantite().equals(new Float(45.0)));
      assertTrue(ech.getQuantiteUnite().equals(mL));
      assertTrue(ech.getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(echantillonManager.getEmplacementManager(ech) != null);
      assertTrue(retourManager.getRetoursForObjectManager(ech).size() == 1);
      incomp = retourManager.getRetoursForObjectManager(ech).get(0);
      assertTrue(incomp.getTempMoyenne().equals(new Float(21.0)));
      assertTrue(incomp.getObservations().equals("OBS1")); // pas modif des observations du retour
      assertTrue(incomp.getTransformation().equals(transfo2));
      assertTrue(incomp.getDateSortie().equals(dateSortie));
      assertTrue(incomp.getDateRetour().equals(dateRetour));

      fs.addAll(derives);
      // Transformation epuisement Echantillon stocke quantite non nulle avec retour		
      // complete par date automatique
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1c");
      pd1.setBanque(b2);
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2c");
      pd2.setBanque(b2);
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3c");
      pd3.setBanque(b2);
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // retour incomplete
      dateSortie = Calendar.getInstance();
      dateSortie.setTimeInMillis(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("13/12/2012 13:55").getTime());
      incomp = new Retour();
      incomp.setTempMoyenne(new Float(22.0));
      incomp.setDateSortie(dateSortie);
      incomp.setSterile(true);
      retourManager.createOrUpdateObjectManager(incomp, ech, echantillonManager.getEmplacementManager(ech), null, null, null,
         null, u2, "creation");
      assertTrue(retourManager.findByObjectDateRetourEmptyManager(echIds, entiteManager.findByNomManager("Echantillon").get(0))
         .get(0).getDateSortie().equals(dateSortie));

      final Calendar current = Calendar.getInstance();

      // Transformation Echantillon stocke quantite non nulle 
      // avec retour empty complété automatique		
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, ech, null, null, null, mL, null, null, null, false, null, null);

      final Transformation transfo3 = pd1.getTransformation();
      assertNull(transfo3.getQuantite());
      assertTrue(transfo3.getQuantiteUnite().equals(mL));
      assertTrue(transformationManager.findAllDeriveFromParentManager(ech).size() == 9);
      assertTrue(prodDeriveManager.findByTransformationManager(transfo3).size() == 3);
      assertTrue(ech.getQuantite().equals(new Float(45.0)));
      assertTrue(ech.getQuantiteUnite().equals(mL));
      assertTrue(ech.getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(echantillonManager.getEmplacementManager(ech) != null);
      assertTrue(retourManager.getRetoursForObjectManager(ech).size() == 2);
      incomp = retourManager.getRetoursForObjectManager(ech).get(1);
      assertTrue(incomp.getTempMoyenne().equals(new Float(22.0)));
      assertNull(incomp.getObservations()); // pas modif des observations du retour
      assertTrue(incomp.getTransformation().equals(transfo3));
      assertTrue(incomp.getDateSortie().equals(dateSortie));
      assertTrue(incomp.getSterile());
      assertTrue(incomp.getDateRetour().get(Calendar.YEAR) == current.get(Calendar.YEAR));
      assertTrue(incomp.getDateRetour().get(Calendar.MONTH) == current.get(Calendar.MONTH));
      assertTrue(incomp.getDateRetour().get(Calendar.DAY_OF_MONTH) == current.get(Calendar.DAY_OF_MONTH));
      assertTrue(incomp.getDateRetour().get(Calendar.HOUR) == current.get(Calendar.HOUR));
      assertTrue(incomp.getDateRetour().get(Calendar.MINUTE) >= current.get(Calendar.MINUTE));

      fs.addAll(derives);
      // Transformation epuisement Echantillon stocke quantite non nulle avec retour		
      // pas completion du retour
      derives.clear();
      pd1 = new ProdDerive();
      pd1.setCode("PD1d");
      pd1.setBanque(b2);
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      pd2 = new ProdDerive();
      pd2.setCode("PD2d");
      pd2.setBanque(b2);
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      pd3 = new ProdDerive();
      pd3.setCode("PD3d");
      pd3.setBanque(b2);
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // retour incomplete
      dateSortie.setTimeInMillis(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/12/2012 13:55").getTime());
      incomp = new Retour();
      incomp.setTempMoyenne(new Float(23.0));
      incomp.setDateSortie(dateSortie);
      incomp.setObservations("EPUIS");
      retourManager.createOrUpdateObjectManager(incomp, ech, echantillonManager.getEmplacementManager(ech), null, null, null,
         null, u2, "creation");

      // Transformation Echantillon stocke quantite non nulle 
      // avec retour non complété		
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, ech, null, null, new Float(45.0), mL, null, null, null, false,
         null, null);

      final Transformation transfo4 = pd1.getTransformation();
      assertTrue(transfo4.getQuantite().equals(new Float(45.0)));
      assertTrue(transfo4.getQuantiteUnite().equals(mL));
      assertTrue(transformationManager.findAllDeriveFromParentManager(ech).size() == 12);
      assertTrue(prodDeriveManager.findByTransformationManager(transfo4).size() == 3);
      assertTrue(ech.getQuantite().equals(new Float(0.0)));
      assertTrue(ech.getQuantiteUnite().equals(mL));
      assertTrue(ech.getObjetStatut().getStatut().equals("EPUISE"));
      assertTrue(echantillonManager.getEmplacementManager(ech) == null);
      assertTrue(retourManager.getRetoursForObjectManager(ech).size() == 3);
      incomp = retourManager.getRetoursForObjectManager(ech).get(1);
      assertTrue(incomp.getTempMoyenne().equals(new Float(23.0)));
      assertTrue(incomp.getTransformation().equals(transfo4));
      assertTrue(incomp.getDateSortie().equals(dateSortie));
      assertNull(incomp.getSterile());
      assertTrue(incomp.getObservations().equals("EPUIS"));
      assertNull(incomp.getDateRetour());

      echantillonManager.removeObjectCascadeManager(ech, null, u2, null);

      emplacementManager.removeObjectManager(new2);

      fs.addAll(derives);
      fs.add(ech);
      cleanUpFantomes(fs);

      testFindAll();
   }

   @Test
   public void testRemoveListFromIds() throws ParseException{
      final Banque b2 = banqueDao.findById(2);
      final Utilisateur u2 = utilisateurDao.findById(2);

      final List<Integer> ids = new ArrayList<>();

      // null list
      prodDeriveManager.removeListFromIdsManager(null, "", u2);
      // empty list
      prodDeriveManager.removeListFromIdsManager(ids, null, u2);
      // non existing ids
      ids.add(22);
      ids.add(33);
      prodDeriveManager.removeListFromIdsManager(ids, "test", u2);

      testFindAll();

      final List<ProdDerive> derives = new ArrayList<>();
      final ProdDerive pd1 = new ProdDerive();
      pd1.setCode("PD1");
      pd1.setBanque(b2);
      pd1.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd1);
      final ProdDerive pd2 = new ProdDerive();
      pd2.setCode("PD2");
      pd2.setBanque(b2);
      pd2.setProdType(prodTypeManager.findByTypeLikeManager("ARN", true).get(0));
      derives.add(pd2);
      final ProdDerive pd3 = new ProdDerive();
      pd3.setCode("PD3");
      pd3.setBanque(b2);
      pd3.setProdType(prodTypeManager.findByTypeLikeManager("ADN", true).get(0));
      derives.add(pd3);

      // 1 Sans parent		
      prodDeriveManager.createProdDerivesManager(derives, b2, u2, null, null, null, null, null, null, null, null, false, null,
         null);

      assertTrue(prodDeriveManager.findByCodeOrLaboWithBanqueManager("PD_", b2, false).size() == 3);

      ids.add(pd1.getProdDeriveId());
      ids.add(pd2.getProdDeriveId());
      ids.add(pd3.getProdDeriveId());
      final Integer cessedderiveId = new Integer(3);
      ids.add(cessedderiveId); // ce derive est cédé

      // rollback used object Exception
      boolean catched = false;
      try{
         prodDeriveManager.removeListFromIdsManager(ids, "test suppr ids", u2);
      }catch(final ObjectUsedException oe){
         catched = true;
         assertTrue(oe.getMessage().equals("derive.cascade.isCessed"));
      }
      assertTrue(catched);
      assertTrue(prodDeriveManager.findByCodeOrLaboWithBanqueManager("PD_", b2, false).size() == 3);

      ids.remove(cessedderiveId);
      prodDeriveManager.removeListFromIdsManager(ids, "test suppr ids", u2);
      assertTrue(prodDeriveManager.findByCodeOrLaboWithBanqueManager("PD_", b2, false).isEmpty());

      assertTrue(getFantomeDao().findByNom("PD3").get(0).getCommentaires().equals("test suppr ids"));

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(derives);
      cleanUpFantomes(fs);

      testFindAll();
   }

   /**
    * @since 2.1
    */
   @Test
   public void testFindByCodeInPlateformeBanqueManager(){
      final Plateforme p1 = plateformeDao.findById(1);
      List<ProdDerive> derives = prodDeriveManager.findByCodeInPlateformeManager("PTRA.1.1", p1);
      assertTrue(derives.size() == 1);
      derives = prodDeriveManager.findByCodeInPlateformeManager("%", p1);
      assertTrue(derives.size() == 4);
      derives = prodDeriveManager.findByCodeInPlateformeManager(null, p1);
      assertTrue(derives.size() == 0);
      derives = prodDeriveManager.findByCodeInPlateformeManager("%", null);
      assertTrue(derives.size() == 0);
   }
}
