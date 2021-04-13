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
package fr.aphp.tumorotek.manager.test.coeur.prelevement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConditMilieuDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConditTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.LaboInterDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.RisqueDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.dao.interfacage.EmetteurDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.dao.qualite.ObjetNonConformeDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonTypeManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementValidator;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.EnceinteType;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager PrelevementManager. Classe créée le 14/10/09.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.1
 *
 */
public class PrelevementManagerTest extends AbstractManagerTest4
{

   @Autowired
   private PrelevementManager prelevementManager;
   @Autowired
   private MaladieManager maladieManager;
   @Autowired
   private PatientManager patientManager;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private NatureDao natureDao;
   @Autowired
   private MaladieDao maladieDao;
   @Autowired
   private ConsentTypeDao consentTypeDao;
   @Autowired
   private CollaborateurDao collaborateurDao;
   @Autowired
   private ServiceDao serviceDao;
   @Autowired
   private PrelevementTypeDao prelevementTypeDao;
   @Autowired
   private ConditTypeDao conditTypeDao;
   @Autowired
   private ConditMilieuDao conditMilieuDao;
   @Autowired
   private UniteDao uniteDao;
   @Autowired
   private TransporteurDao transporteurDao;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private EchantillonDao echantillonDao;
   @Autowired
   private LaboInterDao laboInterDao;
   @Autowired
   private PrelevementValidator prelevementValidator;
   @Autowired
   private ChampAnnotationDao champAnnotationDao;
   @Autowired
   private AnnotationValeurManager annotationValeurManager;
   @Autowired
   private EchantillonTypeManager echantillonTypeManager;
   @Autowired
   private PatientDao patientDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private TransformationManager transformationManager;
   @Autowired
   private ProdDeriveManager prodDeriveManager;
   @Autowired
   private ProdTypeDao prodTypeDao;
   @Autowired
   private CessionDao cessionDao;
   @Autowired
   private CederObjetManager cederObjetManager;
   @Autowired
   private RisqueDao risqueDao;
   @Autowired
   private ConteneurManager conteneurManager;
   @Autowired
   private EnceinteManager enceinteManager;
   @Autowired
   private TerminaleManager terminaleManager;
   @Autowired
   private EmplacementManager emplacementManager;
   @Autowired
   private BanqueManager banqueManager;
   @Autowired
   private EchantillonManager echantillonManager;
   @Autowired
   private EnceinteTypeDao enceinteTypeDao;
   @Autowired
   private EmetteurDao emetteurDao;
   @Autowired
   private ObjetStatutDao objetStatutDao;
   @Autowired
   private NonConformiteDao nonConformiteDao;
   @Autowired
   private ObjetNonConformeDao objetNonConformeDao;
   @Autowired
   private PlateformeDao plateformeDao;

   public PrelevementManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<Prelevement> prels = prelevementManager.findAllObjectsManager();
      assertTrue(prels.size() == 5);
   }

   /**
    * Test la methode findByIdManager.
    */
   @Test
   public void testFindByIdManager(){
      Prelevement prelevement = prelevementManager.findByIdManager(1);
      assertNotNull(prelevement);
      assertTrue(prelevement.getPrelevementId() == 1);
      prelevement = prelevementManager.findByIdManager(2);
      assertNotNull(prelevement);
      assertTrue(prelevement.getPrelevementId() == 2);
      prelevement = prelevementManager.findByIdManager(3);
      assertNotNull(prelevement);
      assertTrue(prelevement.getPrelevementId() == 3);
      prelevement = prelevementManager.findByIdManager(4);
      assertNotNull(prelevement);
      assertTrue(prelevement.getPrelevementId() == 4);
      prelevement = prelevementManager.findByIdManager(9);
      assertNull(prelevement);
   }

   /**
    * Test la methode findByIdsInListManager.
    */
   @Test
   public void testFindByIdsInListManager(){
      List<Integer> ids = new ArrayList<>();
      ids.add(1);
      ids.add(2);
      ids.add(3);
      ids.add(10);
      List<Prelevement> liste = prelevementManager.findByIdsInListManager(ids);
      assertTrue(liste.size() == 3);

      ids = new ArrayList<>();
      liste = prelevementManager.findByIdsInListManager(ids);
      assertTrue(liste.size() == 0);

      liste = prelevementManager.findByIdsInListManager(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test la methode findByCodeLikeManager.
    */
   @Test
   public void testFindByCodeLikeManager(){
      // teste une recherche exactMatch
      List<Prelevement> prels = prelevementManager.findByCodeLikeManager("PRLVT1", true);
      assertTrue(prels.size() == 1);
      // teste une recherche non exactMatch
      prels = prelevementManager.findByCodeLikeManager("PRLVT", false);
      assertTrue(prels.size() == 4);
      // teste une recherche infructueuse
      prels = prelevementManager.findByCodeLikeManager("PRLVT", true);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findByCodeLikeManager(null, false);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la methode findByCodeOrNumLaboLikeWithBanqueManager.
    */
   @Test
   public void testFindByCodeOrNumLaboLikeWithBanqueManager(){
      final Banque b1 = banqueDao.findById(1);
      // teste une recherche exactMatch
      List<Prelevement> prels = prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager("PRLVT1", b1, true);
      assertTrue(prels.size() == 1);
      // teste une recherche non exactMatch
      prels = prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager("PRLVT", b1, false);
      assertTrue(prels.size() == 2);
      // teste une recherche infructueuse
      prels = prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager("PRLVT", b1, true);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager(null, b1, false);
      assertTrue(prels.size() == 0);

      // teste une recherche non exactMatch et une banque null
      prels = prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager("PRLVT", null, false);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager(null, null, false);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la methode findByCodeOrNumLaboLikeBothSideWithBanqueManager.
    */
   @Test
   public void testFindByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager(){

      final Banque b1 = banqueDao.findById(1);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      // teste une recherche exactMatch
      List<Integer> prels = prelevementManager.findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager("PRLVT1", banks, true);
      assertTrue(prels.size() == 1);
      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      prels = prelevementManager.findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager("PRLVT", banks, false);
      assertTrue(prels.size() == 3);
      // teste une recherche non exactMatch
      prels = prelevementManager.findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager("LVT", banks, false);
      assertTrue(prels.size() == 3);
      // teste une recherche infructueuse
      prels = prelevementManager.findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager("PRLVT", banks, true);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager(null, banks, false);
      assertTrue(prels.size() == 0);

      // teste une recherche non exactMatch et une banque null
      prels = prelevementManager.findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager("PRLVT", null, false);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager(null, null, false);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la methode findByMaladieLibelleLikeManager.
    */
   @Test
   public void testFindByMaladieLibelleLikeManager(){
      // teste une recherche exactMatch
      List<Prelevement> prels = prelevementManager.findByMaladieLibelleLikeManager("Non precise", true);
      assertTrue(prels.size() == 1);
      // teste une recherche non exactMatch
      prels = prelevementManager.findByMaladieLibelleLikeManager("Addiction", false);
      assertTrue(prels.size() == 3);
      // teste une recherche infructueuse
      prels = prelevementManager.findByMaladieLibelleLikeManager("Fracture", false);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findByMaladieLibelleLikeManager(null, false);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la methode findByTypeManager.
    */
   @Test
   public void testFindByTypeManager(){
      // teste une recherche fructueuse
      PrelevementType pType = prelevementTypeDao.findById(1);
      List<Prelevement> prels = prelevementManager.findByTypeManager(pType);
      assertTrue(prels.size() == 3);
      // teste une recherche non fructueuse
      pType = prelevementTypeDao.findById(2);
      prels = prelevementManager.findByTypeManager(pType);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findByTypeManager(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la methode findByConsentTypeManager.
    */
   @Test
   public void testFindByConsentTypeManager(){
      // teste une recherche fructueuse
      ConsentType cType = consentTypeDao.findById(3);
      List<Prelevement> prels = prelevementManager.findByConsentTypeManager(cType);
      assertTrue(prels.size() == 3);
      // teste une recherche non fructueuse
      cType = consentTypeDao.findById(2);
      prels = prelevementManager.findByConsentTypeManager(cType);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findByConsentTypeManager(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la methode findByNatureManager.
    */
   @Test
   public void testFindByNatureManager(){
      // teste une recherche fructueuse
      Nature nature = natureDao.findById(1);
      List<Prelevement> prels = prelevementManager.findByNatureManager(nature);
      assertTrue(prels.size() == 2);
      // teste une recherche non fructueuse
      nature = natureDao.findById(4);
      prels = prelevementManager.findByNatureManager(nature);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findByNatureManager(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la methode findAfterDatePrelevementManager.
    * 
    * @throws ParseException
    */
   @Test
   public void testFindAfterDatePrelevementManager() throws ParseException{
      // recherche fructueuse
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("07/09/1983");
      List<Prelevement> prels = prelevementManager.findAfterDatePrelevementManager(search);
      assertTrue(prels.size() == 2);
      // recherche infructueuse
      search = new SimpleDateFormat("dd/MM/yyyy").parse("15/09/2009");
      prels = prelevementManager.findAfterDatePrelevementManager(search);
      assertTrue(prels.size() == 0);
      // null rescherche
      prels = prelevementManager.findAfterDatePrelevementManager(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la methode findAfterDatePrelevementWithBanqueManager.
    * 
    * @throws ParseException
    */
   @Test
   public void testFindAfterDatePrelevementWithBanqueManager() throws ParseException{
      final Banque b1 = banqueDao.findById(1);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      // recherche fructueuse
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("07/09/1983");
      List<Prelevement> prels = prelevementManager.findAfterDatePrelevementWithBanqueManager(search, banks);
      assertTrue(prels.size() == 1);
      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      prels = prelevementManager.findAfterDatePrelevementWithBanqueManager(search, banks);
      assertTrue(prels.size() == 2);
      // recherche infructueuse
      search = new SimpleDateFormat("dd/MM/yyyy").parse("15/09/2009");
      prels = prelevementManager.findAfterDatePrelevementWithBanqueManager(search, banks);
      assertTrue(prels.size() == 0);
      // null rescherche
      prels = prelevementManager.findAfterDatePrelevementWithBanqueManager(null, banks);
      assertTrue(prels.size() == 0);

      // recherche fructueuse mais banque null
      search = new SimpleDateFormat("dd/MM/yyyy").parse("07/09/1983");
      prels = prelevementManager.findAfterDatePrelevementWithBanqueManager(search, null);
      assertTrue(prels.size() == 0);
      // null rescherche
      prels = prelevementManager.findAfterDatePrelevementWithBanqueManager(null, null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la methode findAfterDateConsentementManager.
    * 
    * @throws ParseException
    */
   @Test
   public void testFindAfterDateConsentementManager() throws ParseException{
      // recherche fructueuse
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("06/09/1983");
      List<Prelevement> prels = prelevementManager.findAfterDateConsentementManager(search);
      assertTrue(prels.size() == 3);
      // recherche infructueuse
      search = new SimpleDateFormat("dd/MM/yyyy").parse("15/09/2009");
      prels = prelevementManager.findAfterDateConsentementManager(search);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findAfterDateConsentementManager(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la methode findAfterDateCreationReturnIdsManager.
    * 
    * @throws ParseException
    */
   @Test
   public void testFindAfterDateCreationReturnIdsManager() throws ParseException{
      final Banque b1 = banqueDao.findById(1);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      // recherche fructueuse
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("01/03/2001");
      final Calendar cal = Calendar.getInstance();
      cal.setTime(search);
      List<Integer> prels = prelevementManager.findAfterDateCreationReturnIdsManager(cal, banks);
      assertTrue(prels.size() == 1);
      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      prels = prelevementManager.findAfterDateCreationReturnIdsManager(cal, banks);
      assertTrue(prels.size() == 2);
      // recherche infructueuse
      search = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2009");
      cal.setTime(search);
      prels = prelevementManager.findAfterDateCreationReturnIdsManager(cal, banks);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findAfterDateCreationReturnIdsManager(null, banks);
      assertTrue(prels.size() == 0);

      // recherche avec une banque null
      search = new SimpleDateFormat("dd/MM/yyyy").parse("01/03/2001");
      cal.setTime(search);
      prels = prelevementManager.findAfterDateCreationReturnIdsManager(cal, null);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findAfterDateCreationReturnIdsManager(null, null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la methode findAfterDateModificationManager.
    * 
    * @throws ParseException
    */
   @Test
   public void testFindAfterDateModificationManager() throws ParseException{
      final Banque b1 = banqueDao.findById(1);
      // recherche fructueuse
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2001");
      final Calendar cal = Calendar.getInstance();
      cal.setTime(search);
      List<Prelevement> prels = prelevementManager.findAfterDateModificationManager(cal, b1);
      assertTrue(prels.size() == 1);
      // recherche infructueuse
      search = new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2001");
      cal.setTime(search);
      prels = prelevementManager.findAfterDateModificationManager(cal, b1);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findAfterDateModificationManager(null, b1);
      assertTrue(prels.size() == 0);

      // recherche avec une banque null
      search = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2001");
      cal.setTime(search);
      prels = prelevementManager.findAfterDateModificationManager(cal, null);
      assertTrue(prels.size() == 0);
      // null recherche
      prels = prelevementManager.findAfterDateModificationManager(null, null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test la méthode findLastCreationManager.
    */
   @Test
   public void testFindLastCreationManager(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = banqueDao.findById(3);
      final List<Banque> banks = new ArrayList<>();

      List<Prelevement> list = prelevementManager.findLastCreationManager(banks, 5);
      assertTrue(list.size() == 0);
      banks.add(b1);

      list = prelevementManager.findLastCreationManager(banks, 5);

      assertTrue(list.size() == 3);
      assertNull(getOperationManager().findDateCreationManager(list.get(0)));
      final Calendar date1 = getOperationManager().findDateCreationManager(list.get(1));
      final Calendar date2 = getOperationManager().findDateCreationManager(list.get(2));
      assertTrue(date1.before(date2));

      list = prelevementManager.findLastCreationManager(banks, 1);
      assertTrue(list.size() == 1);

      banks.add(b2);
      list = prelevementManager.findLastCreationManager(banks, 5);
      assertTrue(list.size() == 4);

      list = prelevementManager.findLastCreationManager(banks, 0);
      assertTrue(list.size() == 0);

      banks.add(b3);
      list = prelevementManager.findLastCreationManager(banks, 10);
      assertTrue(list.size() == 5);

      list = prelevementManager.findLastCreationManager(null, 10);
      assertTrue(list.size() == 0);

   }

   /**
    * Test la méthode findAllCodesForBanqueManager.
    */
   @Test
   public void testFindAllCodesForBanqueManager(){
      final Banque b = banqueDao.findById(1);
      List<String> codes = prelevementManager.findAllCodesForBanqueManager(b);
      assertTrue(codes.size() == 3);
      assertTrue(codes.get(1).equals("PRLVT2"));

      final Banque b2 = banqueDao.findById(2);
      codes = prelevementManager.findAllCodesForBanqueManager(b2);
      assertTrue(codes.size() == 1);

      codes = prelevementManager.findAllCodesForBanqueManager(null);
      assertTrue(codes.size() == 0);
   }

   /**
    * Test la méthode findAllNdasForBanqueManager.
    */
   @Test
   public void testFindAllNdasForBanqueManager(){
      final Banque b = banqueDao.findById(1);
      List<String> ndas = prelevementManager.findAllNdasForBanqueManager(b);
      assertTrue(ndas.size() == 1);
      assertTrue(ndas.get(0).equals("NDA234"));

      final Banque b2 = banqueDao.findById(2);
      ndas = prelevementManager.findAllNdasForBanqueManager(b2);
      assertTrue(ndas.size() == 1);

      ndas = prelevementManager.findAllNdasForBanqueManager(null);
      assertTrue(ndas.size() == 0);
   }

   /**
    * Test la méthode getEchantillonsManager.
    */
   @Test
   public void testGetEchantillonsManager(){
      final Prelevement prlvt1 = prelevementManager.findByCodeLikeManager("PRLVT2", true).get(0);
      assertNotNull(prlvt1);
      Set<Echantillon> echans = prelevementManager.getEchantillonsManager(prlvt1);
      assertTrue(echans.size() == 1);
      assertTrue((prelevementManager.getEchantillonsManager(prlvt1)).iterator().next().equals(echantillonDao.findById(3)));

      echans = prelevementManager.getEchantillonsManager(prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0));
      assertFalse(echans.isEmpty());

      final Prelevement prlvt2 = prelevementManager.findByCodeLikeManager("C1234", true).get(0);
      assertNotNull(prlvt2);
      echans = prelevementManager.getEchantillonsManager(prlvt2);
      assertTrue(echans.size() == 0);

      echans = prelevementManager.getEchantillonsManager(null);
      assertTrue(echans.size() == 0);
   }

   /**
    * Test la méthode getLaboIntersManager.
    */
   @Test
   public void testGetLaboIntersManager(){
      final Prelevement prlvt1 = prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0);
      assertNotNull(prlvt1);
      Set<LaboInter> labos = prelevementManager.getLaboIntersManager(prlvt1);
      assertTrue(labos.size() == 3);

      final Prelevement prlvt2 = prelevementManager.findByCodeLikeManager("C1234", true).get(0);
      assertNotNull(prlvt2);
      labos = prelevementManager.getLaboIntersManager(prlvt2);
      assertTrue(labos.size() == 0);

      labos = prelevementManager.getLaboIntersManager(null);
      assertTrue(labos.size() == 0);
   }

   /**
    * Test la méthode getLaboIntersWithOrderManager.
    */
   @Test
   public void testGetLaboIntersWithOrderManager(){
      final Prelevement prlvt1 = prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0);
      assertNotNull(prlvt1);
      List<LaboInter> labos = prelevementManager.getLaboIntersWithOrderManager(prlvt1);
      assertTrue(labos.get(0).getLaboInterId() == 1);
      assertTrue(labos.size() == 3);

      final Prelevement prlvt2 = prelevementManager.findByCodeLikeManager("C1234", true).get(0);
      assertNotNull(prlvt2);
      labos = prelevementManager.getLaboIntersWithOrderManager(prlvt2);
      assertTrue(labos.size() == 0);

      labos = prelevementManager.getLaboIntersWithOrderManager(null);
      assertTrue(labos.size() == 0);
   }

   /**
    * Test la méthode getMaladieManager.
    */
   @Test
   public void testGetMaladieManager(){
      final Prelevement prlvt1 = prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0);
      assertNotNull(prlvt1);
      final Maladie m1 = prelevementManager.getMaladieManager(prlvt1);
      assertTrue(m1.getCode().equals("C45.3"));
      assertTrue(m1.getClass().getSimpleName().equals("Maladie"));

      final Maladie mNull = prelevementManager.getMaladieManager(null);
      assertNull(mNull);
   }

   /**
    * Test la méthode getProdDerivesManager.
    */
   @Test
   public void testGetProdDerivesManager(){
      final Prelevement prlvt1 = prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0);
      assertNotNull(prlvt1);
      List<ProdDerive> derives = prelevementManager.getProdDerivesManager(prlvt1);
      assertTrue(derives.size() == 1);

      final Prelevement prlvt2 = prelevementManager.findByCodeLikeManager("C1234", true).get(0);
      assertNotNull(prlvt2);
      derives = prelevementManager.getProdDerivesManager(prlvt2);
      assertTrue(derives.size() == 0);

      derives = prelevementManager.getProdDerivesManager(null);
      assertTrue(derives.size() == 0);
   }

   @Test
   public void testGetRisquesManager(){
      final Prelevement prlvt1 = prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0);
      assertNotNull(prlvt1);
      Set<Risque> risques = prelevementManager.getRisquesManager(prlvt1);
      assertTrue(risques.size() == 2);

      final Prelevement prlvt2 = prelevementManager.findByCodeLikeManager("C1234", true).get(0);
      assertNotNull(prlvt2);
      risques = prelevementManager.getRisquesManager(prlvt2);
      assertTrue(risques.size() == 0);

      risques = prelevementManager.getRisquesManager(null);
      assertTrue(risques.size() == 0);
   }

   @Test
   public void testFindByDossierExternesManager(){
      List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));
      List<Emetteur> ems = new ArrayList<>();
      ems.add(emetteurDao.findById(2));
      ems.add(emetteurDao.findById(3));

      // param OK
      List<Prelevement> liste = prelevementManager.findByDossierExternesManager(bks, ems);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).getCode().equals("PRLVT1"));

      // param KO
      bks = new ArrayList<>();
      bks.add(banqueDao.findById(2));
      liste = prelevementManager.findByDossierExternesManager(bks, ems);
      assertTrue(liste.size() == 0);

      bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      ems = new ArrayList<>();
      ems.add(emetteurDao.findById(2));
      liste = prelevementManager.findByDossierExternesManager(bks, ems);
      assertTrue(liste.size() == 0);

      ems = new ArrayList<>();
      ems.add(emetteurDao.findById(1));
      liste = prelevementManager.findByDossierExternesManager(bks, ems);
      assertTrue(liste.size() == 0);

      liste = prelevementManager.findByDossierExternesManager(new ArrayList<Banque>(), ems);
      assertTrue(liste.size() == 0);
      liste = prelevementManager.findByDossierExternesManager(bks, new ArrayList<Emetteur>());
      assertTrue(liste.size() == 0);
      liste = prelevementManager.findByDossierExternesManager(null, ems);
      assertTrue(liste.size() == 0);
      liste = prelevementManager.findByDossierExternesManager(bks, null);
      assertTrue(liste.size() == 0);
      liste = prelevementManager.findByDossierExternesManager(null, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * @version 2.1
    */
   @Test
   public void testFindDoublon(){
      // Cree le doublon
      final Prelevement p1 = prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0);
      final Prelevement p2 = new Prelevement();
      p2.setCode(p1.getCode());
      p2.setBanque(p1.getBanque());
      assertTrue(p2.equals(p1));
      assertTrue(prelevementManager.findDoublonManager(p2));

      // test pf
      final Prelevement p3 = new Prelevement();
      p3.setCode(p1.getCode());
      p3.setBanque(banqueDao.findById(2));
      assertFalse(p3.equals(p1));
      assertTrue(prelevementManager.findDoublonManager(p3));

      p3.setBanque(banqueDao.findById(4));
      assertFalse(prelevementManager.findDoublonManager(p3));

      // null
      final Prelevement p4 = new Prelevement();
      p4.setCode(p1.getCode());
      assertFalse(p4.equals(p1));
      assertFalse(prelevementManager.findDoublonManager(p4));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObject(){
      // Enregistrement est reference
      Prelevement p1 = prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0);
      assertTrue(prelevementManager.isUsedObjectManager(p1));
      // Enregistrement n'est pas reference
      p1 = prelevementManager.findByCodeLikeManager("C1234", true).get(0);
      assertFalse(prelevementManager.isUsedObjectManager(p1));
   }

   @Test
   public void testFindByCodeOrNumLaboInListManager(){
      List<String> criteres = new ArrayList<>();
      criteres.add("PRLVT1");
      criteres.add("PRLVT2");
      criteres.add("PRLVTCROSS");
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));

      List<Integer> liste = prelevementManager.findByCodeOrNumLaboInListManager(criteres, bks);
      assertTrue(liste.size() == 2);

      bks.add(banqueDao.findById(3));
      liste = prelevementManager.findByCodeOrNumLaboInListManager(criteres, bks);
      assertTrue(liste.size() == 3);

      criteres = new ArrayList<>();
      criteres.add("PRLVT1");
      criteres.add("121212");
      liste = prelevementManager.findByCodeOrNumLaboInListManager(criteres, bks);
      assertTrue(liste.size() == 2);

      criteres = new ArrayList<>();
      criteres.add("PRLVT2");
      criteres.add("121212");
      liste = prelevementManager.findByCodeOrNumLaboInListManager(criteres, bks);
      assertTrue(liste.size() == 1);

      liste = prelevementManager.findByCodeOrNumLaboInListManager(null, bks);
      assertTrue(liste.size() == 0);
      liste = prelevementManager.findByCodeOrNumLaboInListManager(criteres, null);
      assertTrue(liste.size() == 0);
      liste = prelevementManager.findByCodeOrNumLaboInListManager(new ArrayList<String>(), bks);
      assertTrue(liste.size() == 0);
      liste = prelevementManager.findByCodeOrNumLaboInListManager(criteres, new ArrayList<Banque>());
      assertTrue(liste.size() == 0);
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

      List<Integer> liste = prelevementManager.findByPatientNomOrNipInListManager(criteres, bks);
      assertTrue(liste.size() == 3);

      bks.add(banqueDao.findById(3));
      liste = prelevementManager.findByPatientNomOrNipInListManager(criteres, bks);
      assertTrue(liste.size() == 4);

      criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("12");
      liste = prelevementManager.findByPatientNomOrNipInListManager(criteres, bks);
      assertTrue(liste.size() == 4);

      criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("876");
      liste = prelevementManager.findByPatientNomOrNipInListManager(criteres, bks);
      assertTrue(liste.size() == 3);

      liste = prelevementManager.findByPatientNomOrNipInListManager(null, bks);
      assertTrue(liste.size() == 0);
      liste = prelevementManager.findByPatientNomOrNipInListManager(criteres, null);
      assertTrue(liste.size() == 0);
      liste = prelevementManager.findByPatientNomOrNipInListManager(new ArrayList<String>(), bks);
      assertTrue(liste.size() == 0);
      liste = prelevementManager.findByPatientNomOrNipInListManager(criteres, new ArrayList<Banque>());
      assertTrue(liste.size() == 0);
   }

   /**
    * Teste les methodes CRUD.
    * 
    * @throws ParseException
    */
   @Test
   public void testCRUD() throws ParseException{
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   private void createObjectManagerTest() throws ParseException{
      // Insertion nouvel enregistrement
      final Prelevement p = new Prelevement();
      /* Champs obligatoires */
      final Banque b = banqueDao.findById(2);
      p.setCode("pre_1234-12.0");
      final Nature n = natureDao.findById(1);
      final ConsentType ct = consentTypeDao.findById(2);
      //
      final Maladie m = maladieDao.findById(1);
      // Date ctDate = new SimpleDateFormat("dd/MM/yyyy").parse("17/09/2006");
      p.setConsentDate(null);
      final Collaborateur preleveur = collaborateurDao.findById(1);
      final Service s = serviceDao.findById(1);
      final Calendar preDate = Calendar.getInstance();
      preDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/2006 14:57:01"));
      p.setDatePrelevement(preDate);
      final PrelevementType ptype = prelevementTypeDao.findById(1);
      final ConditType cType = conditTypeDao.findById(1);
      final ConditMilieu milieu = conditMilieuDao.findById(1);
      p.setConditNbr(1);
      final Calendar depDate = Calendar.getInstance();
      depDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("20/09/2006 12:57:01"));
      p.setDateDepart(depDate);
      final Transporteur t = transporteurDao.findById(1);
      p.setTransportTemp(new Float(-5.9));
      // Calendar arrDate = Calendar.getInstance();
      // arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
      // .parse("22/09/2006 12:57:01"));
      // p.setDateArrivee(arrDate);
      final Collaborateur op = collaborateurDao.findById(2);
      p.setQuantite(new Float(12.5));
      final Unite qU = (uniteDao.findByUnite("mg")).get(0);
      p.setPatientNda("NDA1");
      p.setNumeroLabo("1234");
      p.setSterile(true);
      p.setConformeArrivee(true);
      p.setEtatIncomplet(false);
      p.setArchive(false);
      final List<LaboInter> labos = new ArrayList<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      labos.add(l1);
      l1.setCollaborateur(collaborateurDao.findById(1));
      l1.setSterile(true);
      final Calendar dateLab = Calendar.getInstance();
      dateLab.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("21/09/2006 12:12:12"));
      l1.setDateArrivee(dateLab);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      labos.add(l2);
      l2.setService(serviceDao.findById(1));
      l2.setTransporteur(transporteurDao.findById(1));
      l2.setSterile(true);
      l2.setDateDepart(dateLab);
      final Utilisateur u = utilisateurDao.findById(1);

      final Set<Risque> risks = new HashSet<>();
      final Risque r2 = risqueDao.findById(2);
      risks.add(r2);
      p.setRisques(risks);
      p.setQuantite((float) 15.7899);

      // insertion
      prelevementManager.createObjectManager(p, b, n, m, ct, preleveur, s, ptype, cType, milieu, t, op, qU, labos, null, null, u,
         true, "/tmp/", false);
      assertTrue((prelevementManager.findByCodeLikeManager("pre_1234-12.0", true)).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 1);
      assertTrue(laboInterDao.findByPrelevementWithOnlyOrder(p).size() == 2);
      assertTrue(laboInterDao.findByPrelevementWithOrder(p).get(0).getSterile());
      assertTrue(laboInterDao.findByPrelevementWithOrder(p).get(1).getSterile());
      assertTrue(prelevementManager.getRisquesManager(prelevementManager.findByCodeLikeManager("pre_1234-12.0", true).get(0))
         .size() == 1);
      assertTrue(prelevementManager.getRisquesManager(prelevementManager.findByCodeLikeManager("pre_1234-12.0", true).get(0))
         .contains(r2));
      assertTrue((prelevementManager.findByCodeLikeManager("pre_1234-12.0", true)).get(0).getQuantite() == (float) 15.79);
      assertTrue((prelevementManager.findByCodeLikeManager("pre_1234-12.0", true)).get(0).getConformeArrivee());

      // Insertion d'un doublon engendrant une exception
      final Prelevement p2 = new Prelevement();
      p2.setCode(p.getCode());
      Boolean catched = false;
      try{
         prelevementManager.createObjectManager(p2, b, n, m, ct, null, null, null, null, null, null, null, null, null, null, null,
            null, true, "/tmp/", false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(prelevementManager.findByCodeLikeManager("pre_1234-12.0", false).size() == 1);
   }

   private void updateObjectManagerTest() throws ParseException{
      // Modification d'un enregistrement
      final Prelevement p = prelevementManager.findByCodeLikeManager("pre", false).get(0);
      /* Champs obligatoires */
      final List<LaboInter> labos = new ArrayList<>(prelevementManager.getLaboIntersManager(p));
      p.setCode("Dupl_-RER");
      p.setConditNbr(12);
      assertNull(p.getCongArrivee());
      p.setCongArrivee(false);
      p.setConformeArrivee(false);
      final LaboInter l1 = labos.get(0);
      l1.setOrdre(3);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(4);
      labos.add(l2);
      final Calendar dateLab = Calendar.getInstance();
      dateLab.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("21/09/2006 12:12:12"));
      l2.setDateArrivee(dateLab);
      l2.setPrelevement(p);
      l2.setCongelation(true);
      final Utilisateur u = utilisateurDao.findById(1);
      final Risque r1 = risqueDao.findById(1);
      final Set<Risque> risks = prelevementManager.getRisquesManager(p);
      risks.add(r1);
      p.setRisques(risks);

      // Modif
      prelevementManager.updateObjectManager(p, null, null, null, null, null, null, null, null, null, null, null, null, labos,
         null, null, null, null, u, 3, true, "/tmp/", false);
      assertTrue(prelevementManager.findByCodeLikeManager("Dupl_-RER", true).get(0).getNumeroLabo().equals("1234"));
      assertTrue(getOperationManager().findByObjectManager(p).size() == 2);
      assertTrue(laboInterDao.findByPrelevementWithOrder(p).size() == 3);
      assertTrue(laboInterDao.findByPrelevementWithOrder(p).get(1).equals(l1));
      assertTrue(laboInterDao.findByPrelevementWithOrder(p).get(0).getSterile());
      assertFalse(laboInterDao.findByPrelevementWithOrder(p).get(1).getSterile());
      assertFalse(laboInterDao.findByPrelevementWithOrder(p).get(2).getSterile());
      assertTrue(
         prelevementManager.getRisquesManager(prelevementManager.findByCodeLikeManager("Dupl_-RER", true).get(0)).size() == 2);
      assertTrue(
         prelevementManager.getRisquesManager(prelevementManager.findByCodeLikeManager("Dupl_-RER", true).get(0)).contains(r1));
      assertFalse(prelevementManager.findByCodeLikeManager("Dupl_-RER", true).get(0).getConformeArrivee());
      // Modification en un doublon engendrant une exception
      final Banque b = banqueDao.findById(1);
      Boolean catched = false;
      try{
         p.setCode("PRLVT1");
         p.setConditNbr(10);
         prelevementManager.updateObjectManager(p, b, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, u, null, true, "/tmp/", false);
      }catch(final DoublonFoundException e){
         catched = true;
      }
      assertTrue(catched);
      assertTrue(prelevementManager.findByCodeLikeManager("PRLVT1", true).size() == 1);
   }

   private void removeObjectManagerTest(){
      final Utilisateur u = utilisateurDao.findById(1);
      // Suppression de l'enregistrement precedemment insere
      final Prelevement p1 = prelevementManager.findByCodeLikeManager("Dupl_-RER", true).get(0);
      prelevementManager.removeObjectManager(p1, "supprP1", u, null);
      assertTrue(prelevementManager.findByCodeLikeManager("Èré+", true).size() == 0);
      assertTrue(laboInterDao.findAll().size() == 3);
      // Suppression engrendrant une exception
      Boolean catched = false;
      try{
         final Prelevement p2 = prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0);
         prelevementManager.removeObjectManager(p2, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            assertTrue(((ObjectUsedException) e).getKey().equals("prelevement.deletion.isUsedCascade"));
            assertTrue(((ObjectUsedException) e).isCascadable());
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(prelevementManager.findByCodeLikeManager("PRLVT1", true).size() > 0);
      // null remove
      prelevementManager.removeObjectManager(null, null, null, null);
      testFindAllObjectsManager();

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p1);
      cleanUpFantomes(fs);
   }

   
   @Test
   public void testValidation(){
      final Prelevement p = new Prelevement();
      final Banque b = banqueDao.findById(2);
      final Nature n = natureDao.findById(1);
      final ConsentType ct = consentTypeDao.findById(2);
      final Unite qU = (uniteDao.findByUnite("mg")).get(0);
      final Collaborateur op = collaborateurDao.findById(2);
      final Utilisateur u = utilisateurDao.findById(1);
      final Collaborateur preleveur = collaborateurDao.findById(1);
      final Service s = serviceDao.findById(1);
      final PrelevementType ptype = prelevementTypeDao.findById(1);
      final ConditType cType = conditTypeDao.findById(1);
      final ConditMilieu milieu = conditMilieuDao.findById(1);
      final Maladie m = maladieDao.findById(1);
      final Transporteur t = transporteurDao.findById(1);
      // validation test Type
      final Banque[] banques = new Banque[] {null, b};
      final Nature[] natures = new Nature[] {null, n};
      final ConsentType[] cTypes = new ConsentType[] {null, ct};
      try{
         prelevementManager.createObjectManager(p, banques[0], natures[1], m, cTypes[1], preleveur, s, ptype, cType, milieu, t,
            op, qU, null, null, null, u, true, "/tmp/", false);
      }catch(final RequiredObjectIsNullException e){
         assertTrue(e.getMessage().contains("Banque"));
      }
      try{
         prelevementManager.createObjectManager(p, banques[1], natures[0], m, cTypes[1], preleveur, s, ptype, cType, milieu, t,
            op, qU, null, null, null, u, true, "/tmp/", false);
      }catch(final RequiredObjectIsNullException e){
         assertTrue(e.getMessage().contains("Nature"));
      }
      try{
         prelevementManager.createObjectManager(p, banques[1], natures[1], m, cTypes[0], preleveur, s, ptype, cType, milieu, t,
            op, qU, null, null, null, u, true, "/tmp/", false);
      }catch(final RequiredObjectIsNullException e){
         assertTrue(e.getMessage().contains("ConsentType"));
      }
      final String[] codes = new String[] {"", "  ", null, "12}.4", createOverLength(50)};
      for(int i = 0; i < codes.length; i++){
         try{
            p.setCode(codes[i]);
            prelevementManager.createObjectManager(p, banques[1], natures[1], m, cTypes[1], preleveur, s, ptype, cType, milieu, t,
               op, qU, null, null, null, u, true, "/tmp/", false);
         }catch(final ValidationException e){
            assertTrue(e.getErrors().get(0).getFieldErrors().iterator().next().getField().equals("code"));
         }
      }
      // assigne code valide
      p.setCode("14GH.0");

      // validation sur les champs nullables
      final ArrayList<Integer> conditNbrs = new ArrayList<>();
      conditNbrs.add(-1);
      final List<Float> tValues = createNegativeAndOverFloats(new Float(1000.0));
      final List<Float> floats = createNegativeAndOverFloats(null);
      final List<String> ndas = createInvalideAndOverStrings(20);
      final List<String> nums = createInvalideAndOverStrings(50);

      final List<?>[] listes = new List[] {conditNbrs, tValues, floats, ndas, nums};
      // parcoure les listes de valeurs
      for(int i = 0; i < listes.length; i++){
         p.setConditNbr(null);
         p.setTransportTemp(null);
         p.setQuantite(null);
         p.setPatientNda(null);
         p.setNumeroLabo(null);
         // parcoure les valeurs
         for(int j = 0; j < listes[i].size(); j++){
            switch(i){
               case 0:
                  p.setConditNbr((Integer) listes[i].get(j));
                  break;
               case 1:
                  p.setTransportTemp((Float) listes[i].get(j));
                  break;
               case 2:
                  p.setQuantite((Float) listes[i].get(j));
                  break;
               case 3:
                  p.setPatientNda((String) listes[i].get(j));
                  break;
               case 4:
                  p.setNumeroLabo((String) listes[i].get(j));
                  break;
               default:
                  break;
            }
         }
         try{
            prelevementManager.createObjectManager(p, banques[1], natures[1], m, cTypes[1], preleveur, s, ptype, cType, milieu, t,
               op, qU, null, null, null, u, true, "/tmp/", false);
         }catch(final ValidationException e){
            switch(i){
               case 0:
                  assertTrue(e.getErrors().get(0).getFieldErrors().iterator().next().getField().equals("conditNbr"));
                  break;
               case 1:
                  assertTrue(e.getErrors().get(0).getFieldErrors().iterator().next().getField().equals("transportTemp"));
                  break;
               case 2:
                  assertTrue(e.getErrors().get(0).getFieldErrors().iterator().next().getField().equals("quantite"));
                  break;
               case 3:
                  assertTrue(e.getErrors().get(0).getFieldErrors().iterator().next().getField().equals("patientNda"));
                  break;
               case 4:
                  assertTrue(e.getErrors().get(0).getFieldErrors().iterator().next().getField().equals("numeroLabo"));
                  break;
               default:
                  break;
            }
         }
      }
      p.setNumeroLabo(null);
      Boolean catched = false;
      try{
         p.setCongDepart(true);
         p.setCongArrivee(true);
         BeanValidator.validateObject(p, new Validator[] {prelevementValidator});
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("prelevement.congelation.bothDepartArrivee"));
         catched = true;
      }
      assertTrue(catched);

      testFindAllObjectsManager();
   }

   /**
    * teste la creation conjointe d'un prelevement d'une maladie.
    * 
    * @throws ParseException
    */
   @Test
   public void testCreateMaladieAndPrelevement() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Prelevement p = new Prelevement();
      /* Champs obligatoires */
      final Banque b = banqueDao.findById(1);
      p.setCode("AvecMaladie");
      final Nature n = natureDao.findById(1);
      final ConsentType ct = consentTypeDao.findById(2);
      // Patient existant, nouvelle maladie
      final Patient pat = patientManager.findByNomLikeManager("MAYER", true).get(0);
      final Maladie m = new Maladie();
      m.setPatient(pat);
      // avec erreur de validation sur la maladie
      m.setLibelle("");
      try{
         prelevementManager.createObjectManager(p, b, n, m, ct, null, null, null, null, null, null, null, null, null, null, null,
            u, true, "/tmp/", false);
      }catch(final Exception e){
         assertEquals("ValidationException", e.getClass().getSimpleName());
      }

      // avec erreur de doublon sur la maladie
      m.setLibelle("Fracture");
      m.setDateDiagnostic(new SimpleDateFormat("dd/MM/yyyy").parse("03/12/2006"));
      try{
         prelevementManager.createObjectManager(p, b, n, m, ct, null, null, null, null, null, null, null, null, null, null, null,
            u, true, "/tmp/", false);
      }catch(final Exception e){
         assertEquals("DoublonFoundException", e.getClass().getSimpleName());
      }

      // avec erreur sur le prelevement
      m.setLibelle("Obésité");
      p.setCode("PRLVT1");
      try{
         prelevementManager.createObjectManager(p, b, n, m, ct, null, null, null, null, null, null, null, null, null, null, null,
            u, true, "/tmp/", false);
      }catch(final Exception e){
         assertEquals("DoublonFoundException", e.getClass().getSimpleName());
      }

      testFindAllObjectsManager();
      assertTrue(maladieManager.findAllObjectsManager().size() == 6);

      p.setCode("AvecMaladie");
      prelevementManager.createObjectManager(p, b, n, m, ct, null, null, null, null, null, null, null, null, null, null, null, u,
         true, "/tmp/", false);
      assertTrue((maladieManager.findByLibelleLikeManager("Obésité", true)).size() == 1);
      assertTrue(maladieManager.getMaladiesManager(pat).size() == 3);

      // clean up
      prelevementManager.removeObjectManager(
         prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager("AvecMaladie", b, true).get(0), "suppr", u, null);
      maladieManager.removeObjectManager(maladieManager.findByLibelleLikeManager("Obésité", true).get(0), null, u);

      testFindAllObjectsManager();
      assertTrue(maladieManager.findAllObjectsManager().size() == 6);
      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p);
      fs.add(m);
      cleanUpFantomes(fs);
   }

   /**
    * teste la creation conjointe d'un prelevement d'une maladie et d'un
    * patient.
    * 
    * @throws ParseException
    */
   @Test
   public void testCreatePatientAnddMaladieAndPrelevement() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Prelevement p = new Prelevement();
      final Maladie m = new Maladie();
      m.setLibelle("SmallPox");
      /* Champs obligatoires */
      final Banque b = banqueDao.findById(2);
      p.setCode("AvecMaladie");
      final Nature n = natureDao.findById(1);
      final ConsentType ct = consentTypeDao.findById(2);
      // avec erreur de validation sur le patient
      final Patient pat = new Patient();
      pat.setNom("");
      pat.setSexe("M");
      pat.setPatientEtat("D");
      m.setPatient(pat);
      try{
         prelevementManager.createObjectManager(p, b, n, m, ct, null, null, null, null, null, null, null, null, null, null, null,
            u, true, "/tmp/", false);
      }catch(final Exception e){
         assertEquals("ValidationException", e.getClass().getSimpleName());
      }

      // avec erreur de doublon sur le patient
      pat.setNom("JACKSON");
      pat.setPrenom("MICHAEL");
      pat.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("24/12/1958"));
      pat.setVilleNaissance("Los Angeles");
      try{
         prelevementManager.createObjectManager(p, b, n, m, ct, null, null, null, null, null, null, null, null, null, null, null,
            u, true, "/tmp/", false);
      }catch(final Exception e){
         e.printStackTrace();
         assertEquals("DoublonFoundException", e.getClass().getSimpleName());
      }

      // avec erreur sur le prelevement
      pat.setNom("Albarn");
      p.setCode("*$*&é&&##");
      try{
         prelevementManager.createObjectManager(p, b, n, m, ct, null, null, null, null, null, null, null, null, null, null, null,
            u, true, "/tmp/", false);
      }catch(final Exception e){
         assertEquals("ValidationException", e.getClass().getSimpleName());
      }

      testFindAllObjectsManager();
      assertTrue(maladieManager.findAllObjectsManager().size() == 6);

      p.setCode("AvecMaladie");
      prelevementManager.createObjectManager(p, b, n, m, ct, null, null, null, null, null, null, null, null, null, null, null, u,
         true, "/tmp/", false);
      assertTrue((maladieManager.findByLibelleLikeManager("SmallPox", true)).size() == 1);
      assertTrue((patientManager.findByNomLikeManager("Albarn", true)).size() == 1);
      assertTrue(maladieManager.getMaladiesManager(pat).size() == 1);
      // clean up
      prelevementManager.removeObjectManager(
         prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager("AvecMaladie", b, true).get(0), null, u, null);
      maladieManager.removeObjectManager(maladieManager.findByLibelleLikeManager("SmallPox", true).get(0), null, u);
      patientManager.removeObjectManager(patientManager.findByNomLikeManager("Albarn", true).get(0), null, u, null);

      testFindAllObjectsManager();
      assertTrue(maladieManager.findAllObjectsManager().size() == 6);
      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p);
      fs.add(m);
      fs.add(pat);
      cleanUpFantomes(fs);
   }

   /**
    * Teste la validation de la date de prelevement.
    * 
    * @throws ParseException
    */
   @Test
   public void testDatePrelevementCoherence() throws ParseException{
      Prelevement p = new Prelevement();
      final Maladie m = maladieDao.findById(1); // date nais 14-12-1971

      // null validation
      p.setDatePrelevement(null);
      Errors errs = prelevementValidator.checkDatePrelevementCoherence(p);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      final Calendar preDate = Calendar.getInstance();
      preDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1970 12:57:01"));
      p.setDatePrelevement(preDate);
      errs = prelevementValidator.checkDatePrelevementCoherence(p);
      assertTrue(p.getDatePrelevement().equals(preDate));
      assertTrue(errs.getAllErrors().size() == 0);

      p.setMaladie(m);
      errs = prelevementValidator.checkDatePrelevementCoherence(p);
      assertEquals("date.validation.infDateNaissance", errs.getFieldError().getCode());

      // limites sup
      // date depart
      preDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/02/2011 12:57:01"));
      final Calendar refDate = Calendar.getInstance();
      refDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("31/01/2011 12:57:01"));
      p.setDatePrelevement(preDate);
      p.setDateDepart(refDate);
      errs = prelevementValidator.checkDatePrelevementCoherence(p);
      assertEquals("date.validation.supDateDepartPrelevement", errs.getFieldError().getCode());

      // date Arrivee
      p.setDateDepart(null);
      p.setDateArrivee(refDate);
      errs = prelevementValidator.checkDatePrelevementCoherence(p);
      assertEquals("date.validation.supDateArriveePrelevement", errs.getFieldError().getCode());

      // labos
      final Set<LaboInter> labs = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      labs.add(l1);
      l1.setOrdre(1);
      final LaboInter l2 = new LaboInter();
      labs.add(l2);
      l2.setOrdre(2);
      l2.setDateArrivee(refDate);
      final LaboInter l3 = new LaboInter();
      labs.add(l3);
      l3.setOrdre(3);
      l3.setDateDepart(refDate);
      p.setLaboInters(labs);
      // date arrivee labo
      errs = prelevementValidator.checkDatePrelevementCoherence(p);
      assertEquals("date.validation.supDateArriveeUnLaboInter", errs.getFieldError().getCode());
      l2.setOrdre(4);
      // date depart labo
      errs = prelevementValidator.checkDatePrelevementCoherence(p);
      assertEquals("date.validation.supDateDepartUnLaboInter", errs.getFieldError().getCode());

      preDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2069 12:57:01"));
      p.setDatePrelevement(preDate);
      p.getLaboInters().clear();
      p.setDateArrivee(null);

      errs = prelevementValidator.checkDatePrelevementCoherence(p);
      assertEquals("date.validation.supDateActuelle", errs.getFieldError().getCode());

      p = prelevementManager.findByCodeLikeManager("PRLVT3", true).get(0);
      p.setLaboInters(prelevementManager.getLaboIntersManager(p));
      preDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2009 12:57:01"));
      p.setDatePrelevement(preDate);
      errs = prelevementValidator.checkDatePrelevementCoherence(p);
      assertEquals("date.validation.supDateStockEchanEnfant", errs.getFieldError().getCode());

      // p = prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0);
      // prelevementManager.getLaboIntersManager(p);
      // preDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
      // .parse("01/01/202010 19:57:01"));
      // p.setDatePrelevement(preDate);
      // errs = prelevementValidator.checkDatePrelevementCoherence(p);
      // assertEquals("date.validation.supDateStockEchanEnfant",
      // errs.getFieldError().getCode());
   }

   /**
    * Teste la validation de la date de consentement.
    * 
    * @throws ParseException
    */
   @Test
   public void testDateConsentCoherence() throws ParseException{
      final Prelevement p = new Prelevement();
      final Maladie m = maladieDao.findById(1); // date nais 14-12-1971

      // null validation
      p.setConsentDate(null);
      Errors errs = prelevementValidator.checkDateConsentCoherence(p);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      p.setConsentDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1970"));
      errs = prelevementValidator.checkDateConsentCoherence(p);
      assertTrue(errs.getAllErrors().size() == 0);

      p.setMaladie(m);
      errs = prelevementValidator.checkDateConsentCoherence(p);
      assertEquals("date.validation.infDateNaissance", errs.getFieldError().getCode());

      // limites sup
      p.setConsentDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2069"));
      errs = prelevementValidator.checkDateConsentCoherence(p);
      assertEquals("date.validation.supDateActuelle", errs.getFieldError().getCode());
   }

   /**
    * Teste la validation de la date de depart.
    * 
    * @throws ParseException
    */
   @Test
   public void testDateDepartCoherence() throws ParseException{
      Prelevement p = new Prelevement();
      final Maladie m = maladieDao.findById(1); // date nais 14-12-1971

      // null validation
      p.setDateDepart(null);
      Errors errs = prelevementValidator.checkDateDepartCoherence(p);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      final Calendar depDate = Calendar.getInstance();
      depDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1970 12:57:01"));
      p.setDateDepart(depDate);
      errs = prelevementValidator.checkDateDepartCoherence(p);
      assertTrue(errs.getAllErrors().size() == 0);

      p.setMaladie(m);
      errs = prelevementValidator.checkDateDepartCoherence(p);
      assertEquals("date.validation.infDateNaissance", errs.getFieldError().getCode());

      depDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1972 12:57:01"));
      p.setDatePrelevement(depDate);
      errs = prelevementValidator.checkDateDepartCoherence(p);
      assertEquals("date.validation.infDatePrelevement", errs.getFieldError().getCode());

      // limites sup
      p.setDatePrelevement(null);
      p.setMaladie(null);
      final Calendar arrDate = Calendar.getInstance();
      arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1969 12:57:01"));
      p.setDateArrivee(arrDate);
      errs = prelevementValidator.checkDateDepartCoherence(p);
      assertEquals("date.validation.supDateArriveePrelevement", errs.getFieldError().getCode());

      final Calendar refDate = Calendar.getInstance();
      refDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1968 12:57:01"));
      // labos
      final Set<LaboInter> labs = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      labs.add(l1);
      l1.setOrdre(1);
      final LaboInter l2 = new LaboInter();
      labs.add(l2);
      l2.setOrdre(2);
      l2.setDateArrivee(refDate);
      final LaboInter l3 = new LaboInter();
      labs.add(l3);
      l3.setOrdre(3);
      l3.setDateDepart(refDate);
      p.setLaboInters(labs);
      // date arrivee labo
      errs = prelevementValidator.checkDateDepartCoherence(p);
      assertEquals("date.validation.supDateArriveeUnLaboInter", errs.getFieldError().getCode());
      l2.setOrdre(4);
      // date depart labo
      errs = prelevementValidator.checkDateDepartCoherence(p);
      assertEquals("date.validation.supDateDepartUnLaboInter", errs.getFieldError().getCode());

      p.setDateArrivee(null);
      p.getLaboInters().clear();
      depDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2069 12:57:01"));
      p.setDateDepart(depDate);
      errs = prelevementValidator.checkDateDepartCoherence(p);
      assertEquals("date.validation.supDateActuelle", errs.getFieldError().getCode());

      p = prelevementManager.findByCodeLikeManager("PRLVT3", true).get(0);
      p.setDateArrivee(null);
      p.setLaboInters(prelevementManager.getLaboIntersManager(p));
      depDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2009 12:57:01"));
      p.setDateDepart(depDate);
      errs = prelevementValidator.checkDateDepartCoherence(p);
      // assertEquals("date.validation.supDateStockEchanEnfant",
      // errs.getFieldError().getCode());
      assertTrue(errs.getAllErrors().size() == 0);

      // p = prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0);
      // p.setDateArrivee(null);
      // depDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
      // .parse("01/01/2010 12:57:01"));
      // p.setDateDepart(depDate);
      // errs = prelevementValidator.checkDateDepartCoherence(p);
      // assertEquals("date.validation.supDateStockEchanEnfant",
      // errs.getFieldError().getCode());
   }

   /**
    * Teste la validation de la date d'arrivee.
    * 
    * @throws ParseException
    */
   @Test
   public void testDateArriveeCoherence() throws ParseException{
      Prelevement p = new Prelevement();
      final Maladie m = maladieDao.findById(1); // date nais 14-12-1971

      // null validation
      p.setDateArrivee(null);
      Errors errs = prelevementValidator.checkDateArriveeCoherence(p);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      final Calendar arrDate = Calendar.getInstance();
      arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1970 12:57:01"));
      p.setDateArrivee(arrDate);
      errs = prelevementValidator.checkDateArriveeCoherence(p);
      assertTrue(errs.getAllErrors().size() == 0);

      p.setMaladie(m);
      errs = prelevementValidator.checkDateArriveeCoherence(p);
      assertEquals("date.validation.infDateNaissance", errs.getFieldError().getCode());

      final Calendar preDate = Calendar.getInstance();
      preDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1972 12:57:01"));
      p.setDatePrelevement(preDate);
      errs = prelevementValidator.checkDateArriveeCoherence(p);
      assertEquals("date.validation.infDatePrelevement", errs.getFieldError().getCode());

      p.setDatePrelevement(null);
      p.setMaladie(null);
      final Calendar depDate = Calendar.getInstance();
      arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1972 12:57:01"));
      p.setDateDepart(depDate);
      errs = prelevementValidator.checkDateArriveeCoherence(p);
      assertEquals("date.validation.infDateDepartPrelevement", errs.getFieldError().getCode());

      // limites sup
      p.setDateDepart(null);
      arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2069 12:57:01"));
      p.setDateArrivee(arrDate);
      errs = prelevementValidator.checkDateArriveeCoherence(p);
      assertEquals("date.validation.supDateActuelle", errs.getFieldError().getCode());

      p = prelevementManager.findByCodeLikeManager("PRLVT3", true).get(0);
      p.setLaboInters(prelevementManager.getLaboIntersManager(p));
      arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2009 12:57:01"));
      p.setDateArrivee(arrDate);
      errs = prelevementValidator.checkDateArriveeCoherence(p);
      // assertEquals("date.validation.supDateStockEchanEnfant",
      // errs.getFieldError().getCode());
      assertTrue(errs.getAllErrors().size() == 0);

      p = prelevementManager.findByCodeLikeManager("PRLVT1", true).get(0);
      p.setLaboInters(prelevementManager.getLaboIntersManager(p));
      arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2010 12:57:01"));
      p.setDateArrivee(arrDate);
      errs = prelevementValidator.checkDateArriveeCoherence(p);
      // assertEquals("date.validation.supDateStockEchanEnfant",
      // errs.getFieldError().getCode());
      assertTrue(errs.getAllErrors().size() == 0);

   }

   @Test
   public void testFindAllPrelevementsManager(){
      Patient p = patientManager.findByNomLikeManager("MAYER", true).get(0);
      assertTrue(prelevementManager.findAllPrelevementsManager(p).size() == 1);
      p = patientManager.findByNomLikeManager("DELPHINO", true).get(0);
      assertTrue(prelevementManager.findAllPrelevementsManager(p).size() == 3);
      p = patientManager.findByNomLikeManager("JACKSON", true).get(0);
      assertTrue(prelevementManager.findAllPrelevementsManager(p).size() == 0);
   }

   /**
    * Teste en parallele findByMaladieAndBanqueManager et
    * findByMaladieAndOtherBanquesManager.
    */
   @Test
   public void testFindByMaladieAndBanqueManagerOrOthers(){
      final Banque b1 = banqueDao.findById(1);
      Maladie m1 = maladieDao.findById(4);
      List<Prelevement> prels = prelevementManager.findByMaladieAndBanqueManager(m1, b1);
      assertTrue(prels.size() == 1);
      assertTrue(prels.get(0).getCode().equals("PRLVT1"));
      prels = prelevementManager.findByMaladieAndOtherBanquesManager(m1, b1);
      assertTrue(prels.size() == 1);
      assertTrue(prels.get(0).getCode().equals("PRLVTCROSS"));
      m1 = maladieDao.findById(3);
      prels = prelevementManager.findByMaladieAndBanqueManager(m1, b1);
      assertTrue(prels.size() == 1);
      prels = prelevementManager.findByMaladieAndOtherBanquesManager(m1, b1);
      assertTrue(prels.size() == 0);
   }

   @Test
   public void testCascadeNonSterileManager(){

      final Banque b = banqueDao.findById(1);

      // creation architecture fichiers
      new File("/tmp/" + "pt_" + b.getPlateforme().getPlateformeId() + "/" + "coll_" + b.getBanqueId() + "/cr_anapath/").mkdirs();

      final Prelevement p = prelevementManager.findByCodeLikeManager("C1234", true).get(0);
      p.setLaboInters(null);
      final Set<LaboInter> set = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      set.add(l1);
      l1.setSterile(true);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      set.add(l2);
      l2.setSterile(true);
      final LaboInter l3 = new LaboInter();
      l3.setOrdre(3);
      set.add(l3);
      l3.setSterile(true);
      final LaboInter l4 = new LaboInter();
      l4.setOrdre(4);
      set.add(l4);
      l4.setSterile(true);
      final LaboInter l5 = new LaboInter();
      l5.setOrdre(5);
      set.add(l5);
      l5.setSterile(true);

      // cascade labos et echantillons null
      List<LaboInter> labos = prelevementManager.cascadeNonSterileManager(p, null, null, false);
      assertNull(labos);
      labos = prelevementManager.cascadeNonSterileManager(p, null, 0, true);
      assertNull(labos);
      labos = prelevementManager.cascadeNonSterileManager(p, new ArrayList<>(set), null, true);
      assertTrue(labos.size() == 5);

      // totalite labos
      labos = prelevementManager.cascadeNonSterileManager(p, new ArrayList<>(set), 0, false);
      assertFalse(labos.get(0).getSterile());
      assertFalse(labos.get(4).getSterile());

      l1.setSterile(true);
      l2.setSterile(true);
      l3.setSterile(true);
      l4.setSterile(true);
      l5.setSterile(true);

      // une partie des labos
      labos = prelevementManager.cascadeNonSterileManager(p, new ArrayList<>(set), 3, false);
      for(int i = 0; i < labos.size(); i++){
         if(labos.get(i).getOrdre() >= 3){
            assertFalse(labos.get(i).getSterile());
         }else{
            assertTrue(labos.get(i).getSterile());
         }
      }

      // echantillons

      final Prelevement p2 = new Prelevement();
      /* Champs obligatoires */
      p2.setCode("prelWithEchansSterile");
      p2.setNature(natureDao.findById(1));
      p2.setConsentType(consentTypeDao.findById(2));
      p2.setSterile(true);

      final List<Echantillon> echans = new ArrayList<>();
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final ObjetStatut statut = objetStatutDao.findById(4); // NON STOCKE
      final Echantillon e1 = new Echantillon();
      e1.setCode("Echan1");
      e1.setEchantillonType(type);
      e1.setSterile(true);
      e1.setObjetStatut(statut);
      echans.add(e1);
      final Echantillon e2 = new Echantillon();
      e2.setCode("Echan2");
      e2.setEchantillonType(type);
      e2.setSterile(null);
      e2.setObjetStatut(statut);
      echans.add(e2);

      final Utilisateur u = utilisateurDao.findById(1);

      prelevementManager.createPrelAndEchansManager(p2, null, echans, null, b, u, true, "/tmp/");

      final Prelevement prel = prelevementManager.findByCodeLikeManager("prelWithEchansSterile", true).get(0);
      assertTrue(prelevementManager.getEchantillonsManager(prel).size() == 2);
      assertTrue(prelevementManager.getLaboIntersManager(prel).size() == 0);
      assertTrue(echantillonDao.findByCode("Echan1").get(0).getSterile());
      assertNull(echantillonDao.findByCode("Echan2").get(0).getSterile());

      labos = prelevementManager.cascadeNonSterileManager(p2, null, null, true);
      assertNull(labos);
      assertFalse(echantillonDao.findByCode("Echan1").get(0).getSterile());
      assertFalse(echantillonDao.findByCode("Echan2").get(0).getSterile());

      // force le commit pour revenir a etat initial table echantillon
      // EntityManager em = entityManagerFactory.createEntityManager();
      // em.getTransaction().begin();
      // Echantillon e1 = em.find(Echantillon.class, 1);
      // e1.setSterile(true);
      // em.merge(e1);
      // Echantillon e2 = em.find(Echantillon.class, 2);
      // e2.setSterile(null);
      // em.merge(e2);
      // em.flush();
      // em.getTransaction().commit();
      // em.close();

      // cleanup cascade vers Echantillon
      prelevementManager.removeObjectCascadeManager(prel, "cascade", u, null);

      // verification de l'etat de la base
      testFindAllObjectsManager();
      assertTrue(laboInterDao.findAll().size() == 3);
      assertTrue(echantillonDao.findAll().size() == 4);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(prel);
      fs.add(e1);
      fs.add(e2);
      cleanUpFantomes(fs);

   }

   @Test
   public void testFindByBanquesManager(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      List<Prelevement> res = prelevementManager.findByBanquesManager(banks);
      assertTrue(res.size() == 4);
      banks.clear();
      res = prelevementManager.findByBanquesManager(banks);
      assertTrue(res.size() == 0);
      res = prelevementManager.findByBanquesManager(null);
      assertTrue(res.size() == 0);
   }

   /**
    * Test la méthode findAllObjectsIdsByBanquesManager.
    */
   @Test
   public void testFindAllObjectsIdsByBanquesManager(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      List<Integer> res = prelevementManager.findAllObjectsIdsByBanquesManager(banks);
      assertTrue(res.size() == 4);
      banks.clear();
      res = prelevementManager.findAllObjectsIdsByBanquesManager(banks);
      assertTrue(res.size() == 0);
      res = prelevementManager.findAllObjectsIdsByBanquesManager(null);
      assertTrue(res.size() == 0);
   }

   @Test
   public void testFindByPatientManager(){
      Patient p = patientManager.findByNomLikeBothSideManager("DELPHINO", true).get(0);
      List<Prelevement> prels = prelevementManager.findByPatientManager(p);
      assertTrue(prels.size() == 3);
      p = patientManager.findByNomLikeBothSideManager("JACKSON", true).get(0);
      prels = prelevementManager.findByPatientManager(p);
      assertTrue(prels.size() == 0);
      prels = prelevementManager.findByPatientManager(null);
      assertTrue(prels.size() == 0);
   }

   @Test
   public void testFindByPatientNomManager(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b3 = banqueDao.findById(3);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      List<Integer> prels = prelevementManager.findByPatientNomReturnIdsManager("DELPHINO", banks, true);
      assertTrue(prels.size() == 2);

      prels = prelevementManager.findByPatientNomReturnIdsManager("ELPH", banks, false);
      assertTrue(prels.size() == 2);

      prels = prelevementManager.findByPatientNomReturnIdsManager("ELPH", banks, true);
      assertTrue(prels.size() == 0);

      prels = prelevementManager.findByPatientNomReturnIdsManager("%ELPH%", banks, true);
      assertTrue(prels.size() == 2);

      banks.add(b3);
      prels = prelevementManager.findByPatientNomReturnIdsManager("DELPHINO", banks, true);
      assertTrue(prels.size() == 3);

      prels = prelevementManager.findByPatientNomReturnIdsManager("JACKSON", banks, false);
      assertTrue(prels.size() == 0);

      prels = prelevementManager.findByPatientNomReturnIdsManager(null, banks, false);
      assertTrue(prels.size() == 0);

      prels = prelevementManager.findByPatientNomReturnIdsManager("DELPHINO", null, false);
      assertTrue(prels.size() == 0);
   }

   @Test
   public void testCreateUpdatePrelevementWithAnnots(){

      final Banque b1 = banqueDao.findById(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);
      final ChampAnnotation c = champAnnotationDao.findById(2);

      // on insert 1 nouveau prelevement pour les tests
      final Prelevement prel = new Prelevement();
      prel.setCode("PrelAnnotated");
      final Nature n = natureDao.findById(1);
      final ConsentType ct = consentTypeDao.findById(2);

      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final List<AnnotationValeur> listDelete = new ArrayList<>();

      final AnnotationValeur alpha = new AnnotationValeur();
      alpha.setChampAnnotation(c);
      alpha.setAlphanum("val1");
      alpha.setBanque(b1);
      listAnnots.add(alpha);

      final List<LaboInter> labos = new ArrayList<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      labos.add(l1);
      l1.setCollaborateur(collaborateurDao.findById(1));

      // teste erreur sur annots -> rollback
      alpha.setAlphanum("&é**$$$¤¤");
      boolean catched = false;
      try{
         prelevementManager.createObjectManager(prel, b1, n, null, ct, null, null, null, null, null, null, null, null, labos,
            listAnnots, null, utilisateur, true, "/tmp/", false);
      }catch(final ValidationException ve){
         catched = true;
      }
      assertTrue(catched);

      // verification de l'etat de la base
      testFindAllObjectsManager();

      alpha.setAlphanum("val1");
      prelevementManager.createObjectManager(prel, b1, n, null, ct, null, null, null, null, null, null, null, null, labos,
         listAnnots, null, utilisateur, true, "/tmp/", false);

      assertTrue(prelevementManager.findByCodeLikeManager("PrelAnnotated", true).size() == 1);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, prel).get(0).getAlphanum().equals("val1"));
      assertTrue(getOperationManager().findByObjectManager(prel).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(prel).get(1).getOperationType().getNom().equals("Annotation"));

      // on teste une deletion puis insertion nouvelle valeur
      listDelete.add(annotationValeurManager.findByChampAndObjetManager(c, prel).get(0));
      listAnnots.clear();
      final AnnotationValeur alpha2 = new AnnotationValeur();
      alpha2.setChampAnnotation(c);
      alpha2.setAlphanum("val2");
      alpha2.setBanque(b1);
      listAnnots.add(alpha2);
      final AnnotationValeur bool1 = new AnnotationValeur();
      final ChampAnnotation c2 = champAnnotationDao.findById(3);
      bool1.setChampAnnotation(c2);
      bool1.setBool(true);
      bool1.setBanque(b1);
      listAnnots.add(bool1);

      prel.setNumeroLabo("One");

      prelevementManager.updateObjectManager(prel, null, null, null, null, null, null, null, null, null, null, null, null, null,
         listAnnots, listDelete, null, null, utilisateur, null, true, "/tmp/", false);

      assertTrue(getOperationManager().findByObjectManager(prel).size() == 4);
      assertTrue(getOperationManager().findByObjectManager(prel).get(3).getOperationType().getNom().equals("Annotation"));

      assertTrue(getOperationManager().findByObjectManager(prel).size() == 4);
      assertTrue(getOperationManager().findByObjectManager(prel).get(3).getOperationType().getNom().equals("Annotation"));

      assertTrue(prelevementManager.findByCodeLikeManager("PrelAnnotated", true).size() == 1);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, prel).size() == 1);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, prel).get(0).getAlphanum().equals("val2"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c2, prel).get(0).getBool());

      // suppression annots
      listDelete.add(annotationValeurManager.findByChampAndObjetManager(c, prel).get(0));
      // listDelete.add(annotationValeurManager
      // .findByChampAndObjetManager(c2, prel).get(0));
      listAnnots.clear();

      prelevementManager.updateObjectManager(prel, null, null, null, null, null, null, null, null, null, null, null, null, null,
         listAnnots, listDelete, null, null, utilisateur, null, true, "/tmp/", false);

      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, prel).size() == 0);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c2, prel).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(prel).size() == 6);
      assertTrue(getOperationManager().findByObjectManager(prel).get(5).getOperationType().getNom().equals("Annotation"));

      // Nettoyage
      prelevementManager.removeObjectManager(prel, null, utilisateur, null);

      assertTrue(getOperationManager().findByObjectManager(prel).size() == 0);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);
      testFindAllObjectsManager();

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(prel);
      cleanUpFantomes(fs);
   }

   @Test
   public void testFindByNdaLikeManager(){
      List<Prelevement> prels = prelevementManager.findByNdaLikeManager("NDA%");
      assertTrue(prels.size() == 3);
      prels = prelevementManager.findByNdaLikeManager("NDA65");
      assertTrue(prels.size() == 1);
      prels = prelevementManager.findByNdaLikeManager("NDA1200");
      assertTrue(prels.size() == 0);
      prels = prelevementManager.findByNdaLikeManager(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * teste avec erreur sur echantillon pour forcer rollback.
    * 
    * @throws ParseException
    */
   @Test
   public void testCreatePrelevementAndEchantillons() throws ParseException{
      // Insertion nouvel enregistrement
      final Prelevement p = new Prelevement();
      /* Champs obligatoires */
      final Banque b = banqueDao.findById(1);
      p.setCode("prelWithEchans");
      p.setNature(natureDao.findById(1));
      p.setConsentType(consentTypeDao.findById(2));
      p.setMaladie(maladieDao.findById(1));
      p.setConsentDate(new SimpleDateFormat("dd/MM/yyyy").parse("17/09/2006"));
      p.setPreleveur(collaborateurDao.findById(1));
      p.setServicePreleveur(serviceDao.findById(1));
      final Calendar preDate = Calendar.getInstance();
      preDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/2006 14:57:01"));
      p.setDatePrelevement(preDate);
      p.setPrelevementType(prelevementTypeDao.findById(1));
      p.setConditType(conditTypeDao.findById(1));
      p.setConditMilieu(conditMilieuDao.findById(1));
      p.setConditNbr(1);
      final Calendar depDate = Calendar.getInstance();
      depDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("20/09/2006 12:57:01"));
      p.setDateDepart(depDate);
      p.setTransporteur(transporteurDao.findById(1));
      p.setTransportTemp(new Float(-5.9));
      final Calendar arrDate = Calendar.getInstance();
      arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("22/09/2006 12:57:01"));
      p.setDateArrivee(arrDate);
      p.setPreleveur(collaborateurDao.findById(2));
      p.setQuantite(new Float(12.5));
      p.setQuantiteUnite((uniteDao.findByUnite("mg")).get(0));
      p.setPatientNda("NDA1");
      p.setNumeroLabo("1234");
      p.setSterile(true);
      p.setEtatIncomplet(false);
      p.setArchive(false);

      final Set<LaboInter> labos = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      labos.add(l1);
      l1.setCollaborateur(collaborateurDao.findById(1));
      l1.setSterile(true);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      labos.add(l2);
      l2.setService(serviceDao.findById(1));
      l2.setTransporteur(transporteurDao.findById(1));
      l2.setSterile(true);
      p.setLaboInters(labos);

      final ChampAnnotation c = champAnnotationDao.findById(2);
      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final AnnotationValeur alpha = new AnnotationValeur();
      alpha.setChampAnnotation(c);
      alpha.setAlphanum("val1");
      alpha.setBanque(b);
      listAnnots.add(alpha);

      final List<Echantillon> echans = new ArrayList<>();
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final ObjetStatut statut = objetStatutDao.findById(4); // NON STOCKE
      final Echantillon e1 = new Echantillon();
      e1.setCode("Echan1");
      e1.setEchantillonType(type);
      echans.add(e1);
      e1.setObjetStatut(statut);
      final Echantillon e2 = new Echantillon();
      e2.setCode("Echan2");
      e2.setEchantillonType(type);
      echans.add(e2);
      e2.setObjetStatut(statut);
      final Echantillon e3 = new Echantillon();
      e3.setCode("PTRA.1");
      e3.setEchantillonType(type);
      echans.add(e3);
      e3.setObjetStatut(statut);
      // -> engendrera doublon exception

      final List<AnnotationValeur> listAnnots2 = new ArrayList<>();
      final ChampAnnotation c2 = champAnnotationDao.findById(7);

      final AnnotationValeur num = new AnnotationValeur();
      num.setChampAnnotation(c2);
      num.setAlphanum("123456");
      num.setBanque(b);
      listAnnots2.add(num);

      final Utilisateur u = utilisateurDao.findById(1);

      boolean catched = false;
      try{
         prelevementManager.createPrelAndEchansManager(p, listAnnots, echans, listAnnots2, b, u, true, "/tmp/");
      }catch(final DoublonFoundException dbe){
         catched = true;
      }
      assertTrue(catched);

      // verification de l'etat de la base
      testFindAllObjectsManager();
      assertTrue(laboInterDao.findAll().size() == 3);
      assertTrue(echantillonDao.findAll().size() == 4);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);

      // correction et enregistrement valide
      e3.setCode("Echan3");
      prelevementManager.createPrelAndEchansManager(p, listAnnots, echans, listAnnots2, b, u, true, "/tmp/");

      final Prelevement prel = prelevementManager.findByCodeLikeManager("prelWithEchans", true).get(0);
      assertTrue(prelevementManager.getEchantillonsManager(prel).size() == 3);
      assertTrue(prelevementManager.getLaboIntersManager(prel).size() == 2);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 16);

      // cleanup cascade vers echantillons
      prelevementManager.removeObjectCascadeManager(prel, null, u, null);

      // verification de l'etat de la base
      testFindAllObjectsManager();
      assertTrue(laboInterDao.findAll().size() == 3);
      assertTrue(echantillonDao.findAll().size() == 4);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(prel);
      fs.add(e1);
      fs.add(e2);
      fs.add(e3);
      cleanUpFantomes(fs);
   }

   /**
    * teste avec erreur sur echantillon pour forcer rollback. La maladie est
    * crée conjointement donc rollbakc doit entainer revert de maladieId;
    * 
    * @throws ParseException
    */
   @Test
   public void testCreatePrelevementAndEchantillonsWithMaladie() throws ParseException{
      // Insertion nouvel enregistrement
      final Prelevement p = new Prelevement();
      /* Champs obligatoires */
      final Banque b = banqueDao.findById(1);
      p.setCode("prelWithEchans");
      p.setNature(natureDao.findById(1));
      p.setConsentType(consentTypeDao.findById(2));
      p.setSterile(true);

      final Patient pat = patientDao.findById(1);
      final Maladie m = new Maladie();
      m.setLibelle("mala");
      m.setPatient(pat);
      p.setMaladie(m);

      final Set<LaboInter> labos = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      labos.add(l1);
      l1.setCollaborateur(collaborateurDao.findById(1));
      l1.setSterile(true);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      labos.add(l2);
      l2.setService(serviceDao.findById(1));
      l2.setTransporteur(transporteurDao.findById(1));
      l2.setSterile(true);
      p.setLaboInters(labos);

      final ChampAnnotation c = champAnnotationDao.findById(2);
      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final AnnotationValeur alpha = new AnnotationValeur();
      alpha.setChampAnnotation(c);
      alpha.setAlphanum("val1");
      alpha.setBanque(b);
      listAnnots.add(alpha);

      final List<Echantillon> echans = new ArrayList<>();
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final ObjetStatut statut = objetStatutDao.findById(4); // NON STOCKE
      final Echantillon e1 = new Echantillon();
      e1.setCode("Echan1");
      e1.setEchantillonType(type);
      echans.add(e1);
      e1.setObjetStatut(statut);
      final Echantillon e2 = new Echantillon();
      e2.setCode("Echan2");
      e2.setEchantillonType(type);
      echans.add(e2);
      e2.setObjetStatut(statut);
      final Echantillon e3 = new Echantillon();
      e3.setCode("PTRA.1");
      e3.setEchantillonType(type);
      echans.add(e3);
      e3.setObjetStatut(statut);
      // -> engendrera doublon exception

      final List<AnnotationValeur> listAnnots2 = new ArrayList<>();
      final ChampAnnotation c2 = champAnnotationDao.findById(7);

      final AnnotationValeur num = new AnnotationValeur();
      num.setChampAnnotation(c2);
      num.setAlphanum("123456");
      num.setBanque(b);
      listAnnots2.add(num);

      final Utilisateur u = utilisateurDao.findById(1);

      boolean catched = false;
      try{
         prelevementManager.createPrelAndEchansManager(p, listAnnots, echans, listAnnots2, b, u, true, "/tmp/");
      }catch(final DoublonFoundException dbe){
         catched = true;
      }
      assertTrue(catched);

      // verification de l'etat de la base
      testFindAllObjectsManager();
      assertTrue(laboInterDao.findAll().size() == 3);
      assertTrue(echantillonDao.findAll().size() == 4);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);

      // correction et enregistrement valide
      e3.setCode("Echan3");
      prelevementManager.createPrelAndEchansManager(p, listAnnots, echans, listAnnots2, b, u, true, "/tmp/");

      final Prelevement prel = prelevementManager.findByCodeLikeManager("prelWithEchans", true).get(0);
      assertTrue(prelevementManager.getEchantillonsManager(prel).size() == 3);
      assertTrue(prelevementManager.getLaboIntersManager(prel).size() == 2);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 16);

      // cleanup cascade sur echantillons
      prelevementManager.removeObjectCascadeManager(prel, null, u, null);
      maladieManager.removeObjectManager(m, null, u);

      // verification de l'etat de la base
      testFindAllObjectsManager();
      assertTrue(laboInterDao.findAll().size() == 3);
      assertTrue(echantillonDao.findAll().size() == 4);
      assertTrue(maladieDao.findAll().size() == 6);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(prel);
      fs.add(e1);
      fs.add(e2);
      fs.add(e3);
      fs.add(m);
      cleanUpFantomes(fs);
   }

   /**
    * teste avec erreur sur echantillon pour forcer rollback. La maladie et le
    * patient sont créés conjointement donc rollback doit entrainer revert de
    * maladieId et patient;
    * 
    * @throws ParseException
    */
   @Test
   public void testCreatePrelAndEchanSWithMaladieAndPatient() throws ParseException{
      // Insertion nouvel enregistrement
      final Prelevement p = new Prelevement();
      /* Champs obligatoires */
      final Banque b = banqueDao.findById(1);
      p.setCode("prelWithEchans");
      p.setNature(natureDao.findById(1));
      p.setConsentType(consentTypeDao.findById(2));
      p.setSterile(true);

      final Patient pat = new Patient();
      pat.setNom("Hose");
      pat.setPatientEtat("D");
      pat.setPrenom("Jeff");
      pat.setSexe("M");
      pat.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("22/09/1965"));
      final Maladie m = new Maladie();
      m.setLibelle("mala");
      m.setPatient(pat);
      p.setMaladie(m);

      final Set<LaboInter> labos = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      labos.add(l1);
      l1.setCollaborateur(collaborateurDao.findById(1));
      l1.setSterile(true);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      labos.add(l2);
      l2.setService(serviceDao.findById(1));
      l2.setTransporteur(transporteurDao.findById(1));
      l2.setSterile(true);
      p.setLaboInters(labos);

      final ChampAnnotation c = champAnnotationDao.findById(2);
      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final AnnotationValeur alpha = new AnnotationValeur();
      alpha.setChampAnnotation(c);
      alpha.setAlphanum("val1");
      alpha.setBanque(b);
      listAnnots.add(alpha);

      final List<Echantillon> echans = new ArrayList<>();
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final ObjetStatut statut = objetStatutDao.findById(4); // NON STOCKE
      final Echantillon e1 = new Echantillon();
      e1.setCode("Echan1");
      e1.setEchantillonType(type);
      echans.add(e1);
      e1.setObjetStatut(statut);
      final Echantillon e2 = new Echantillon();
      e2.setCode("Echan2");
      e2.setEchantillonType(type);
      echans.add(e2);
      e2.setObjetStatut(statut);
      final Echantillon e3 = new Echantillon();
      e3.setCode("PTRA.1");
      e3.setEchantillonType(type);
      echans.add(e3);
      e3.setObjetStatut(statut);
      // -> engendrera doublon exception

      final List<AnnotationValeur> listAnnots2 = new ArrayList<>();
      final ChampAnnotation c2 = champAnnotationDao.findById(7);

      final AnnotationValeur num = new AnnotationValeur();
      num.setChampAnnotation(c2);
      num.setAlphanum("123456");
      num.setBanque(b);
      listAnnots2.add(num);

      final Utilisateur u = utilisateurDao.findById(1);

      // creation architecture fichiers
      new File("/tmp/" + "pt_" + b.getPlateforme().getPlateformeId() + "/" + "coll_" + b.getBanqueId() + "/cr_anapath/").mkdirs();

      boolean catched = false;
      try{
         prelevementManager.createPrelAndEchansManager(p, listAnnots, echans, listAnnots2, b, u, true, "/tmp/");
      }catch(final DoublonFoundException dbe){
         catched = true;
      }
      assertTrue(catched);

      // verification de l'etat de la base
      testFindAllObjectsManager();
      assertTrue(laboInterDao.findAll().size() == 3);
      assertTrue(echantillonDao.findAll().size() == 4);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);

      // correction et enregistrement valide
      e3.setCode("Echan3");
      prelevementManager.createPrelAndEchansManager(p, listAnnots, echans, listAnnots2, b, u, true, "/tmp/");

      final Prelevement prel = prelevementManager.findByCodeLikeManager("prelWithEchans", true).get(0);
      assertTrue(prelevementManager.getEchantillonsManager(prel).size() == 3);
      assertTrue(laboInterDao.findAll().size() == 5);
      assertTrue(prelevementManager.getLaboIntersManager(prel).size() == 2);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 16);

      // cleanup cascade vers echantillons
      prelevementManager.removeObjectCascadeManager(prel, null, u, null);
      maladieManager.removeObjectManager(m, null, u);
      patientManager.removeObjectManager(pat, null, u, null);

      // verification de l'etat de la base
      testFindAllObjectsManager();
      assertTrue(laboInterDao.findAll().size() == 3);
      assertTrue(echantillonDao.findAll().size() == 4);
      assertTrue(maladieDao.findAll().size() == 6);
      assertTrue(patientDao.findAll().size() == 5);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);
      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(e1);
      fs.add(e2);
      fs.add(e3);
      fs.add(p);
      fs.add(m);
      fs.add(pat);
      cleanUpFantomes(fs);
   }

   @Test
   public void testRemoveUsedAndCessedObject(){
      boolean catched = false;
      final Utilisateur u = utilisateurDao.findById(1);

      // Cascade depuis prelevement -> echantillon cede
      final Prelevement p1 = prelevementManager.findByIdManager(1);
      try{
         prelevementManager.removeObjectCascadeManager(p1, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("echantillon.cascade.isCessed"));
         assertFalse(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);
      catched = false;

   }

   @Test
   public void testCascadePrelevementChildren(){

      final Banque b = banqueDao.findById(2);
      // creation architecture fichiers
      new File("/tmp/" + "pt_" + b.getPlateforme().getPlateformeId() + "/" + "coll_" + b.getBanqueId() + "/cr_anapath/").mkdirs();

      final Utilisateur u = utilisateurDao.findById(1);
      final Prelevement p = new Prelevement();
      /* Champs obligatoires */
      p.setCode("PrelWithDerives");
      p.setNature(natureDao.findById(1));
      p.setConsentType(consentTypeDao.findById(2));

      final List<Echantillon> echans = new ArrayList<>();
      final EchantillonType etype = echantillonTypeManager.findByIdManager(1);
      final ObjetStatut statut = objetStatutDao.findById(4); // NON STOCKE
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      final Echantillon e1 = new Echantillon();
      e1.setCode("E1");
      e1.setEchantillonType(etype);
      echans.add(e1);
      e1.setObjetStatut(statut);
      final Echantillon e2 = new Echantillon();
      e2.setCode("E2");
      e2.setEchantillonType(etype);
      echans.add(e2);
      e2.setObjetStatut(statut);
      final Echantillon e3 = new Echantillon();
      e3.setCode("E3");
      e3.setEchantillonType(etype);
      echans.add(e3);
      e3.setObjetStatut(statut);

      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      final Set<LaboInter> labs = new HashSet<>();
      labs.add(l1);
      labs.add(l2);
      p.setLaboInters(labs);

      prelevementManager.createPrelAndEchansManager(p, null, echans, null, b, utilisateur, false, "/tmp/");

      assertTrue(echantillonDao.findByPrelevement(p).size() == 3);

      // delete cascade -> broken à cause d'une cession
      final Cession c = cessionDao.findById(1);
      final CederObjet ced3 = new CederObjet();
      ced3.setObjetId(e3.getEchantillonId());
      ced3.setQuantite((float) 10.0);
      final Unite qteUnite = uniteDao.findById(5);
      cederObjetManager.createObjectManager(ced3, c, entiteDao.findByNom("Echantillon").get(0), qteUnite);

      boolean catched = false;
      try{
         prelevementManager.removeObjectCascadeManager(p, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("echantillon.cascade.isCessed"));
         assertFalse(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);

      // suppr ceder objet pour pouvoir cascader
      cederObjetManager.removeObjectManager(ced3);

      final Transformation transfo1 = new Transformation();
      final Entite entite = entiteDao.findById(2);
      transfo1.setObjetId(p.getPrelevementId());
      transformationManager.createObjectManager(transfo1, entite, null);

      final List<ProdDerive> derives = new ArrayList<>();
      final ProdType type = prodTypeDao.findById(1);

      final ProdDerive derive1 = new ProdDerive();
      derive1.setCode("D1");
      derive1.setProdType(type);
      derives.add(derive1);
      derive1.setObjetStatut(statut);
      final ProdDerive derive2 = new ProdDerive();
      derive2.setCode("D2");
      derive2.setProdType(type);
      derives.add(derive2);
      derive2.setObjetStatut(statut);
      final ProdDerive derive3 = new ProdDerive();
      derive3.setCode("D3");
      derive3.setProdType(type);
      derives.add(derive3);
      derive3.setObjetStatut(statut);

      prodDeriveManager.createDeriveListWithAnnotsManager(derives, b, transfo1, utilisateur, null, null, null, null);

      assertTrue(transformationManager.findAllDeriveFromParentManager(p).size() == 3);
      assertTrue(echantillonDao.findByPrelevement(p).size() == 3);

      assertNull(prodDeriveManager.findByIdManager(derive1.getProdDeriveId()).getConformeTraitement());
      assertNull(prodDeriveManager.findByIdManager(derive2.getProdDeriveId()).getConformeCession());

      // delete cascade -> broken à cause d'une cession
      final CederObjet ced2 = new CederObjet();
      ced2.setObjetId(derive3.getProdDeriveId());
      ced2.setQuantite((float) 10.0);
      cederObjetManager.createObjectManager(ced2, c, entiteDao.findByNom("ProdDerive").get(0), qteUnite);

      catched = false;
      try{
         prelevementManager.removeObjectCascadeManager(p, null, u, null);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("derive.cascade.isCessed"));
         assertFalse(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);

      assertTrue(echantillonDao.findByPrelevement(p).size() == 3);

      // suppr ceder objet pour pouvoir cascader
      cederObjetManager.removeObjectManager(ced2);
      prelevementManager.removeObjectCascadeManager(p, "cascade!!!", u, null);

      testFindAllObjectsManager();
      assertTrue(transformationManager.findAllObjectsManager().size() == 5);
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
      assertTrue(echantillonDao.findAll().size() == 4);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p);
      fs.addAll(echans);
      fs.addAll(derives);
      cleanUpFantomes(fs);
   }

   @Test
   public void testGetPrelevementChildren(){
      List<TKAnnotableObject> children;
      Prelevement p = prelevementManager.findByIdManager(1);
      children = prelevementManager.getPrelevementChildrenManager(p);
      assertTrue(children.size() == 5);
      p = prelevementManager.findByIdManager(3);
      children = prelevementManager.getPrelevementChildrenManager(p);
      assertTrue(children.size() == 2);
      assertTrue(children.contains(echantillonDao.findById(4)));
      assertTrue(children.contains(prodDeriveManager.findByIdManager(4)));
      p = prelevementManager.findByIdManager(4);
      children = prelevementManager.getPrelevementChildrenManager(p);
      assertTrue(children.size() == 0);
      children = prelevementManager.getPrelevementChildrenManager(null);
      assertTrue(children.size() == 0);
   }

   @Test
   public void testHasCessedObjectManager(){
      Prelevement p = prelevementManager.findByIdManager(1);
      assertTrue(prelevementManager.hasCessedObjectManager(p));
      p = prelevementManager.findByIdManager(3);
      assertFalse(prelevementManager.hasCessedObjectManager(p));
      p = prelevementManager.findByIdManager(2);
      assertTrue(prelevementManager.hasCessedObjectManager(p));
      assertFalse(prelevementManager.hasCessedObjectManager(null));
   }

   @Test
   public void testSimpleSwitchBanque(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = banqueDao.findById(3);
      final Banque b4 = banqueDao.findById(4);
      // ne doit pas planter
      prelevementManager.switchBanqueCascadeManager(null, b3, false, u, null, null);
      Prelevement p = new Prelevement();
      p.setCode("switchPrelev1");
      // ne doit pas planter getBanque() == null
      prelevementManager.switchBanqueCascadeManager(p, null, false, u, null, null);
      prelevementManager.createObjectManager(p, b3, natureDao.findById(1), null, consentTypeDao.findById(1), null, null, null,
         null, null, null, null, null, null, null, null, u, false, "/tmp/", false);
      assertTrue(banqueManager.getPrelevementsManager(b3).size() == 2);
      p = prelevementManager.findByIdManager(p.getPrelevementId());

      // banque depart = banque arrivee
      prelevementManager.switchBanqueCascadeManager(p, b3, false, u, null, null);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 1);

      // switch 3 -> 2
      prelevementManager.switchBanqueCascadeManager(p, b2, false, u, null, null);
      assertTrue(banqueManager.getPrelevementsManager(b3).size() == 1);
      assertTrue(banqueManager.getPrelevementsManager(b2).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 2);

      // prodDerive et echan cascade 2 -> 3
      Echantillon e = new Echantillon();
      e.setCode("echanSwitch");
      echantillonManager.createObjectManager(e, b2, p, null, objetStatutDao.findById(4), null,
         echantillonTypeManager.findByIdManager(1), null, null, null, null, null, null, u, false, "/tmp/", false);
      final Transformation transfo1 = new Transformation();
      transfo1.setObjetId(p.getPrelevementId());
      transformationManager.createObjectManager(transfo1, entiteDao.findById(2), null);
      ProdDerive derive = new ProdDerive();
      derive.setCode("deriveSwitch");
      prodDeriveManager.createObjectManager(derive, b2, prodTypeDao.findById(1), objetStatutDao.findById(4), null, null, null,
         null, null, null, null, transfo1, null, null, u, false, "/tmp/", false);
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 2);
      assertTrue(banqueManager.getEchantillonsManager(b2).size() == 2);
      prelevementManager.switchBanqueCascadeManager(p, b3, true, u, null, null);
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 1);
      assertTrue(banqueManager.getEchantillonsManager(b2).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);
      assertTrue(banqueManager.getPrelevementsManager(b3).size() == 2);
      e = echantillonManager.findByIdManager(e.getEchantillonId());
      derive = prodDeriveManager.findByIdManager(derive.getProdDeriveId());

      // cession du derive -> erreur
      final Cession cession = cessionDao.findById(1);
      final CederObjet ced1 = new CederObjet();
      ced1.setObjetId(derive.getProdDeriveId());
      cederObjetManager.createObjectManager(ced1, cession, entiteDao.findById(8), null);
      boolean catched = false;
      try{
         prelevementManager.switchBanqueCascadeManager(p, b4, true, u, null, null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("derive.switchBanque.isCessed"));
      }
      assertTrue(catched);
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 1);
      assertTrue(banqueManager.getEchantillonsManager(b2).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);
      assertTrue(banqueManager.getPrelevementsManager(b3).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 3);

      // cession de echantillon -> erreur
      cederObjetManager.removeObjectManager(ced1);
      final CederObjet ced2 = new CederObjet();
      ced2.setObjetId(e.getEchantillonId());
      cederObjetManager.createObjectManager(ced2, cession, entiteDao.findById(3), null);
      catched = false;
      try{
         prelevementManager.switchBanqueCascadeManager(p, b4, true, u, null, null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("echantillon.switchBanque.isCessed"));
      }
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 1);
      assertTrue(banqueManager.getEchantillonsManager(b2).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);
      assertTrue(banqueManager.getPrelevementsManager(b3).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 3);

      cederObjetManager.removeObjectManager(ced2);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
      catched = false;

      // emplacement erreur
      final Conteneur cbase = conteneurManager.findByIdManager(1);
      final Terminale t = terminaleManager.findByIdManager(1);
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
         plateformeDao.findById(1));
      final Emplacement emp = new Emplacement();
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
         null, null, derive.getTransformation(), null, null, null, null, u, false, null, "/tmp/");

      try{
         prelevementManager.switchBanqueCascadeManager(p, b2, true, u, null, null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("derive.switchBanque.badBanqueStockage"));
      }
      assertTrue(catched);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 3);

      final Emplacement emp2 = new Emplacement();
      emp2.setPosition(56);
      emplacementManager.createObjectManager(emp2, t1, null);

      // emp.setObjetId(e.getEchantillonId());
      // emplacementManager.updateObjectManager(emp, emp.getTerminale(),
      //		entiteDao.findById(3));
      // emp = emplacementManager.findByIdManager(emp.getEmplacementId());
      // emp.getTerminale().getEnceinte();
      echantillonManager.updateObjectManager(e, e.getBanque(), e.getPrelevement(), null, null, emp2, e.getEchantillonType(), null,
         null, null, null, null, null, null, null, null, u, false, null, "/tmp/");
      prodDeriveManager.updateObjectManager(derive, derive.getBanque(), derive.getProdType(), null, null, null, null, null, null,
         null, null, transfo1, null, null, null, null, u, false, null, "/tmp/");

      try{
         prelevementManager.switchBanqueCascadeManager(p, b2, true, u, null, null);
      }catch(final RuntimeException re){
         catched = true;
         assertTrue(re.getMessage().equals("echantillon.switchBanque.badBanqueStockage"));
      }
      assertTrue(catched);
      assertTrue(banqueManager.getProdDerivesManager(b2).size() == 1);
      assertTrue(banqueManager.getEchantillonsManager(b2).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);
      assertTrue(banqueManager.getProdDerivesManager(b3).size() == 1);
      assertTrue(banqueManager.getPrelevementsManager(b3).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 3);

      // annotations
      prelevementManager.switchBanqueCascadeManager(p, b1, true, u, null, null);
      assertTrue(banqueManager.getPrelevementsManager(b1).size() == 4);
      assertTrue(banqueManager.getPrelevementsManager(b2).size() == 1);
      assertTrue(banqueManager.getPrelevementsManager(b3).size() == 1);
      assertTrue(banqueManager.getEchantillonsManager(b1).size() == 4);
      assertTrue(banqueManager.getProdDerivesManager(b1).size() == 4);
      assertTrue(getOperationManager().findByObjectManager(p).size() == 4);

      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final AnnotationValeur num = new AnnotationValeur();
      num.setChampAnnotation(champAnnotationDao.findById(36));
      num.setTexte("youhou");
      num.setBanque(b1);
      listAnnots.add(num);
      final AnnotationValeur bool = new AnnotationValeur();
      bool.setChampAnnotation(champAnnotationDao.findById(3));
      bool.setBool(true);
      bool.setBanque(b1);
      listAnnots.add(bool);
      prelevementManager.updateObjectManager(p, p.getBanque(), p.getNature(), null, p.getConsentType(), null, null, null, null,
         null, null, null, null, null, listAnnots, null, null, null, u, null, false, "/tmp/", false);
      assertTrue(annotationValeurManager.findByObjectManager(p).size() == 2);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 14);

      prelevementManager.switchBanqueCascadeManager(p, b3, true, u, null, null);
      assertTrue(annotationValeurManager.findByObjectManager(p).size() == 0);

      prelevementManager.switchBanqueCascadeManager(p, b1, true, u, null, null);
      assertTrue(annotationValeurManager.findByObjectManager(p).size() == 0);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);

      // annotations patient
      // Patient pat = new Patient();
      // pat.setNom("YOUCH");
      // pat.setPatientEtat("V");
      // Maladie m = new Maladie();
      // m.setPatient(pat);
      // m.setLibelle("la maladie à youch");
      // List<Maladie> mals = new ArrayList<Maladie>(); mals.add(m);
      // listAnnots.clear();
      // AnnotationValeur alpha1 = new AnnotationValeur();
      // alpha1.setChampAnnotation(champAnnotationDao.findById(1));
      // alpha1.setAlphanum("et oui"); alpha1.setBanque(b1);
      // listAnnots.add(alpha1);
      // AnnotationValeur text = new AnnotationValeur();
      // text.setChampAnnotation(champAnnotationDao.findById(23));
      // text.setTexte("jamais je ne changerai"); text.setBanque(b1);
      // listAnnots.add(text);
      // patientManager.createOrUpdateObjectManager(pat, mals, null, null,
      // listAnnots, null, u, "creation", null);
      // assertTrue(annotationValeurManager
      // .findByObjectManager(pat).size() == 2);
      // assertTrue(annotationValeurManager
      // .findAllObjectsManager().size() == 14);
      // Prelevement p2 = new Prelevement();
      // p2.setCode("prelPatswicth");
      // prelevementManager.createObjectManager(p2, b1, natureDao.findById(1),
      // maladieManager.findByPatientManager(pat).get(0),
      // consentTypeDao.findById(1), null, null, null, null, null, null,
      // null, null, null, null, u, false, null);
      // p2 = prelevementManager.findByIdManager(p2.getPrelevementId());
      //
      // prelevementManager.switchBanqueCascadeManager(p2, b3, true, u);
      // assertTrue(annotationValeurManager
      // .findByObjectManager(pat).size() == 0);
      //
      // clean up
      final List<Enceinte> encs = enceinteManager.findAllEnceinteByConteneurManager(c);
      final List<Terminale> terminales = new ArrayList<>();
      final Iterator<Enceinte> encIt = encs.iterator();
      while(encIt.hasNext()){
         terminales.addAll(terminaleManager.findByEnceinteWithOrderManager(encIt.next()));
      }
      // prelevementManager.removeObjectCascadeManager(p2, null, u);
      // patientManager.removeObjectManager(pat, null, u);
      prelevementManager.removeObjectCascadeManager(p, null, u, null);
      emp.setEntite(null);
      emp.setObjetId(null);
      emp.setVide(true);
      emplacementManager.removeObjectManager(emp);
      conteneurManager.removeObjectManager(conteneurManager.findByIdManager(c.getConteneurId()), null, u);
      final List<TKFantomableObject> fs = new ArrayList<>();
      // fs.add(pat);
      // fs.add(m);
      // fs.add(p2);
      fs.add(p);
      fs.add(e);
      fs.add(derive);
      fs.add(c);
      fs.addAll(encs);
      fs.addAll(terminales);
      cleanUpFantomes(fs);
   }

   @Test
   public void testMultipleSwitchBanque(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Banque b1 = banqueDao.findById(1);
      final Nature n1 = natureDao.findById(1);
      final ConsentType c1 = consentTypeDao.findById(1);
      final Banque b3 = banqueDao.findById(3);

      // ne doit pas planter prel == null
      prelevementManager.switchBanqueMultiplePrelevementManager(null, b3, true, u);
      final Prelevement p1 = new Prelevement();
      p1.setCode("p1");
      prelevementManager.createObjectManager(p1, b1, n1, null, c1, null, null, null, null, null, null, null, null, null, null,
         null, u, false, "/tmp/", false);

      final Prelevement p2 = new Prelevement();
      p2.setCode("p2");
      prelevementManager.createObjectManager(p2, b1, n1, null, c1, null, null, null, null, null, null, null, null, null, null,
         null, u, false, "/tmp/", false);

      // null
      prelevementManager.switchBanqueMultiplePrelevementManager(null, b3, true, u);

      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      assertTrue(prelevementManager.findByBanquesManager(banks).size() == 5);

      banks.clear();
      banks.add(b3);
      assertTrue(prelevementManager.findByBanquesManager(banks).size() == 1);

      //valide
      final Prelevement[] ap = {p1, p2};
      prelevementManager.switchBanqueMultiplePrelevementManager(ap, b3, true, u);
      // b3
      assertTrue(prelevementManager.findByBanquesManager(banks).size() == 3);

      banks.clear();
      banks.add(b1);
      assertTrue(prelevementManager.findByBanquesManager(banks).size() == 3);

      // @since 2.1 doublon code + PF 
      // switchBanque doit retourner doublon exception si déplacement entre deux pfs
      final Banque b4pf2 = banqueDao.findById(4);
      final Prelevement p1bis = new Prelevement();
      p1bis.setCode("p1");
      prelevementManager.createObjectManager(p1bis, b4pf2, n1, null, c1, null, null, null, null, null, null, null, null, null,
         null, null, u, false, "/tmp/", false);

      final Prelevement[] ap2 = {p1bis};
      boolean catched = false;
      try{
         prelevementManager.switchBanqueMultiplePrelevementManager(ap2, b3, true, u);
      }catch(final DoublonFoundException db){
         catched = true;
      }
      assertTrue(catched);

      banks.add(b4pf2);
      assertTrue(prelevementManager.findByBanquesManager(banks).size() == 4);

      banks.clear();
      banks.add(b3);
      assertTrue(prelevementManager.findByBanquesManager(banks).size() == 3);

      // clean up
      prelevementManager.removeObjectCascadeManager(p1, null, u, null);
      prelevementManager.removeObjectCascadeManager(p2, null, u, null);
      prelevementManager.removeObjectCascadeManager(p1bis, null, u, null);
      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p1);
      fs.add(p2);
      fs.add(p1bis);
      cleanUpFantomes(fs);

      testFindAllObjectsManager();
   }

   @Test
   public void testGetDateCongelationManager() throws ParseException{
      Prelevement p = prelevementManager.findByIdManager(1);
      Date ref = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("21/10/2000 19:43:12");
      Calendar c = prelevementManager.getDateCongelationManager(p);
      assertTrue(c.getTime().equals(ref));
      p = prelevementManager.findByIdManager(5);
      ref = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("06/09/1983 18:30:00");
      c = prelevementManager.getDateCongelationManager(p);
      assertTrue(c.getTime().equals(ref));
      p = prelevementManager.findByIdManager(3);
      assertNull(prelevementManager.getDateCongelationManager(p));
      p = prelevementManager.findByIdManager(4);
      assertNull(prelevementManager.getDateCongelationManager(p));
      assertNull(prelevementManager.getDateCongelationManager(null));

      p = new Prelevement();
      final Set<LaboInter> labos = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      labos.add(l1);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      l2.setCongelation(true);
      l2.setDateDepart(c);
      labos.add(l2);
      p.setLaboInters(labos);
      assertTrue(prelevementManager.getDateCongelationManager(p).getTime().equals(c.getTime()));
   }

   @Test
   public void testUpdateMultipleObjectsManager() throws ParseException{
      final Banque b1 = banqueManager.findByIdManager(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      // on insert 3 nouveaux prélèvements pour les tests
      final Prelevement prel1 = new Prelevement();
      final Prelevement prel2 = new Prelevement();
      final Prelevement prel3 = new Prelevement();
      prel1.setCode("p1");
      prel2.setCode("p2");
      prel3.setCode("p3");
      final Nature n = natureDao.findById(1);
      final ConsentType ct = consentTypeDao.findById(2);
      final Collaborateur preleveur = collaborateurDao.findById(1);
      final Service s = serviceDao.findById(1);
      final Calendar preDate = Calendar.getInstance();
      preDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/2006 14:57:01"));
      prel1.setDatePrelevement(preDate);
      prel2.setDatePrelevement(preDate);
      final PrelevementType ptype = prelevementTypeDao.findById(1);
      final ConditType cType = conditTypeDao.findById(1);
      final ConditMilieu milieu = conditMilieuDao.findById(1);
      final Calendar depDate = Calendar.getInstance();
      depDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("20/09/2006 12:57:01"));
      prel3.setDateDepart(depDate);
      final Transporteur t = transporteurDao.findById(1);
      prel2.setTransportTemp(new Float(-5.9));
      prel1.setSterile(true);
      prel2.setSterile(true);
      final Set<LaboInter> labos = new HashSet<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      labos.add(l1);
      l1.setCollaborateur(collaborateurDao.findById(1));
      l1.setSterile(true);
      final Calendar dateLab = Calendar.getInstance();
      dateLab.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("21/09/2006 12:12:12"));
      l1.setDateArrivee(dateLab);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      labos.add(l2);
      l2.setService(serviceDao.findById(1));
      l2.setTransporteur(transporteurDao.findById(1));
      l2.setSterile(true);
      l2.setDateDepart(dateLab);
      final Utilisateur u = utilisateurDao.findById(1);
      final List<Echantillon> echans = new ArrayList<>();
      final EchantillonType type = echantillonTypeManager.findByIdManager(1);
      final ObjetStatut statut = objetStatutDao.findById(4); // NON STOCKE
      final Echantillon e1 = new Echantillon();
      e1.setSterile(true);
      e1.setObjetStatut(statut);
      e1.setCode("Echan1");
      e1.setEchantillonType(type);
      echans.add(e1);
      final Echantillon e2 = new Echantillon();
      e2.setSterile(true);
      e2.setObjetStatut(statut);
      e2.setCode("Echan2");
      e2.setEchantillonType(type);
      echans.add(e2);

      final Set<Risque> risks = new HashSet<>();
      final Risque r2 = risqueDao.findById(2);
      risks.add(r2);
      prel2.setRisques(risks);

      final List<NonConformite> ncfs = new ArrayList<>();
      ncfs.add(nonConformiteDao.findById(1));
      ncfs.add(nonConformiteDao.findById(2));

      final List<Prelevement> prels = new ArrayList<>();
      prels.add(prel1);
      prels.add(prel2);
      prels.add(prel3);
      final List<AnnotationValeur> listAnnots = new ArrayList<>();
      final List<AnnotationValeur> listDelete = new ArrayList<>();

      final AnnotationValeur alpha1 = new AnnotationValeur();
      alpha1.setChampAnnotation(champAnnotationDao.findById(2));
      alpha1.setAlphanum("bank1");
      listAnnots.add(alpha1);
      final AnnotationValeur alpha2 = new AnnotationValeur();
      alpha2.setChampAnnotation(champAnnotationDao.findById(2));
      alpha2.setAlphanum("bank2");
      listAnnots.add(alpha2);
      final AnnotationValeur alpha3 = new AnnotationValeur();
      alpha3.setChampAnnotation(champAnnotationDao.findById(2));
      alpha3.setAlphanum("bank3");
      listAnnots.add(alpha3);

      for(int i = 0; i < prels.size(); i++){
         if(!prels.get(i).getCode().equals("p1")){
            prelevementManager.createObjectManager(prels.get(i), b1, n, null, ct, preleveur, s, ptype, cType, milieu, t,
               preleveur, null, null, null, null, u, false, "/tmp/", false);
         }else{ // prel1 + labos + echans
            prels.get(i).setBanque(b1);
            prels.get(i).setLaboInters(labos);
            prels.get(i).setNature(n);
            prels.get(i).setConsentType(ct);
            prelevementManager.createPrelAndEchansManager(prels.get(i), null, echans, null, b1, u, false, null);
         }
         annotationValeurManager.createOrUpdateObjectManager(listAnnots.get(i), null, prels.get(i), b1, null, utilisateur,
            "creation", null, null, null);
      }

      final int id1 = prel1.getPrelevementId();
      final int id2 = prel2.getPrelevementId();

      assertTrue(prelevementManager.findAllObjectsManager().size() == 8);
      assertTrue(prelevementManager.getLaboIntersManager(prel1).size() == 2);
      assertTrue(prelevementManager.getLaboIntersManager(prel1).iterator().next().getSterile());
      assertTrue(echantillonManager.findAllObjectsManager().size() == 6);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 15);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(id1, entiteDao.findById(2)).isEmpty());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id2, entiteDao.findById(2)).isEmpty());

      // on teste une maj valide
      final String codeLabo = "ROOTS";
      final Prelevement prelUp1 = prelevementManager.findByCodeLikeManager("p1", true).get(0);
      final Prelevement prelUp2 = prelevementManager.findByCodeLikeManager("p2", true).get(0);
      final Prelevement prelUp3 = prelevementManager.findByCodeLikeManager("p3", true).get(0);
      prelUp1.setLaboInters(prelevementManager.getLaboIntersManager(prelUp1));
      prelUp2.setLaboInters(new HashSet<LaboInter>());
      prelUp3.setLaboInters(new HashSet<LaboInter>());
      prelUp1.setNumeroLabo(codeLabo);
      prelUp2.setNumeroLabo(codeLabo);
      prelUp3.setNumeroLabo(codeLabo);
      List<Prelevement> list = new ArrayList<>();
      list.add(prelUp1);
      list.add(prelUp2);
      list.add(prelUp3);
      listAnnots.clear();
      listAnnots.addAll(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(2), prel1));
      listAnnots.addAll(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(2), prel2));
      listAnnots.addAll(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(2), prel3));
      listAnnots.get(0).setAlphanum("i am");
      listAnnots.get(1).setAlphanum("so in love");
      listAnnots.get(2).setAlphanum("with you");

      prelevementManager.updateMultipleObjectsManager(list, null, listAnnots, listDelete, ncfs, true, utilisateur, "/tmp/");

      Prelevement prelTest1 = prelevementManager.findByCodeLikeManager("p1", true).get(0);
      Prelevement prelTest2 = prelevementManager.findByCodeLikeManager("p2", true).get(0);
      Prelevement prelTest3 = prelevementManager.findByCodeLikeManager("p3", true).get(0);
      prelTest1.setLaboInters(prelevementManager.getLaboIntersManager(prelTest1));
      prelTest2.setLaboInters(new HashSet<LaboInter>());
      prelTest3.setLaboInters(new HashSet<LaboInter>());
      assertTrue(prelTest1.getNumeroLabo().equals(codeLabo));
      assertTrue(prelTest2.getNumeroLabo().equals(codeLabo));
      assertTrue(prelTest3.getNumeroLabo().equals(codeLabo));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(2), prelTest1).get(0)
         .getAlphanum().equals("i am"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(2), prelTest2).get(0)
         .getAlphanum().equals("so in love"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(2), prelTest3).get(0)
         .getAlphanum().equals("with you"));
      assertTrue(prelTest1.getSterile());
      assertTrue(prelTest2.getSterile());
      assertFalse(prelevementManager.getLaboIntersManager(prel1).iterator().next().getSterile());

      assertTrue(objetNonConformeDao.findByObjetAndEntite(id1, entiteDao.findById(2)).size() == 2);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id2, entiteDao.findById(2)).size() == 2);

      // on teste une maj non valide sur le 1er élément
      prelTest1.setConsentDate(new SimpleDateFormat("dd/MM/yyyy").parse("17/09/2078"));
      final Float temp = new Float(-12.5);
      prelTest1.setTransportTemp(temp);
      prelTest2.setTransportTemp(temp);
      prelTest3.setTransportTemp(temp);
      list = new ArrayList<>();
      list.add(prelTest1);
      list.add(prelTest2);
      list.add(prelTest3);
      listAnnots.get(0).setAlphanum("TEST");
      // listAnnots.get(1).setBool(true);
      listAnnots.get(2).setAlphanum("TEST3");

      boolean catched = false;
      try{
         prelevementManager.updateMultipleObjectsManager(list, list, listAnnots, listDelete, null, false, utilisateur, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
            assertTrue(((ValidationException) e).getEntiteObjetException().equals("Prelevement"));
            assertTrue(((ValidationException) e).getIdentificationObjetException().equals(list.get(0).getCode()));
         }
      }
      assertTrue(catched);
      prelTest1 = prelevementManager.findByCodeLikeManager("p1", true).get(0);
      prelTest2 = prelevementManager.findByCodeLikeManager("p2", true).get(0);
      prelTest3 = prelevementManager.findByCodeLikeManager("p3", true).get(0);
      prelTest1.setLaboInters(prelevementManager.getLaboIntersManager(prelTest1));
      prelTest2.setLaboInters(new HashSet<LaboInter>());
      prelTest3.setLaboInters(new HashSet<LaboInter>());
      assertNull(prelTest1.getTransportTemp());
      assertTrue(prelTest2.getTransportTemp().equals(new Float(-5.9)));
      assertNull(prelTest3.getTransportTemp());
      assertFalse(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(2), prelTest1).get(0)
         .getAlphanum().equals("TEST"));

      // on teste une maj non valide sur le 3eme élément
      prelTest1.setConsentDate(new SimpleDateFormat("dd/MM/yyyy").parse("17/09/2078"));
      prelTest1.setTransportTemp(temp);
      prelTest2.setTransportTemp(temp);
      prelTest3.setTransportTemp(temp);
      list = new ArrayList<>();
      list.add(prelTest1);
      list.add(prelTest2);
      list.add(prelTest3);
      listAnnots.get(0).setAlphanum("TEST");
      // listAnnots.get(1).setBool(true);
      listAnnots.get(2).setAlphanum("TEST3");

      catched = false;
      try{
         prelevementManager.updateMultipleObjectsManager(list, list, listAnnots, listDelete, null, false, utilisateur, "/tmp/");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      prelTest1 = prelevementManager.findByCodeLikeManager("p1", true).get(0);
      prelTest2 = prelevementManager.findByCodeLikeManager("p2", true).get(0);
      prelTest3 = prelevementManager.findByCodeLikeManager("p3", true).get(0);
      prelTest1.setLaboInters(prelevementManager.getLaboIntersManager(prelTest1));
      prelTest2.setLaboInters(new HashSet<LaboInter>());
      prelTest3.setLaboInters(new HashSet<LaboInter>());
      list.clear();
      list.add(prelTest1);
      list.add(prelTest2);
      list.add(prelTest3);
      assertNull(prelTest1.getTransportTemp());
      assertTrue(prelTest2.getTransportTemp().equals(new Float(-5.9)));
      assertNull(prelTest3.getTransportTemp());
      assertFalse(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(2), prelTest1).get(0)
         .getAlphanum().equals("TEST"));

      // on teste avec creation d'annotation
      listDelete.add(listAnnots.get(0));
      listDelete.add(listAnnots.get(1));
      listAnnots.clear();
      prelTest1.setNumeroLabo("NEW");
      final AnnotationValeur bool4 = new AnnotationValeur();
      bool4.setBool(true);
      bool4.setChampAnnotation(champAnnotationDao.findById(3));
      bool4.setObjetId(id1);
      bool4.setBanque(b1);
      listAnnots.add(bool4);
      final AnnotationValeur alpha5 = new AnnotationValeur();
      alpha5.setAlphanum("&&é**¤¤");
      alpha5.setChampAnnotation(champAnnotationDao.findById(2));
      alpha5.setObjetId(id2);
      alpha5.setBanque(b1);
      listAnnots.add(alpha5);
      // validationException
      try{
         prelevementManager.updateMultipleObjectsManager(list, list, listAnnots, listDelete, null, false, utilisateur, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      prelTest1 = prelevementManager.findByCodeLikeManager("p1", true).get(0);
      prelTest2 = prelevementManager.findByCodeLikeManager("p2", true).get(0);
      prelTest1.setLaboInters(prelevementManager.getLaboIntersManager(prelTest1));
      prelTest2.setLaboInters(new HashSet<LaboInter>());
      assertFalse(prelTest1.getNumeroLabo().equals("NEW"));
      assertFalse(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(3), prelTest1).size() > 0);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(2), prelTest2).get(0)
         .getAlphanum().equals("so in love"));

      // modifications valides
      // alpha1.setAnnotationValeurId(null);
      ncfs.clear();
      ncfs.add(nonConformiteDao.findById(3));
      alpha5.setAlphanum("yy");
      prelevementManager.updateMultipleObjectsManager(list, list, listAnnots, listDelete, ncfs, false, utilisateur, "/tmp/");
      prelTest1 = prelevementManager.findByCodeLikeManager("p1", true).get(0);
      prelTest2 = prelevementManager.findByCodeLikeManager("p2", true).get(0);
      prelTest3 = prelevementManager.findByCodeLikeManager("p3", true).get(0);
      prelTest1.setLaboInters(prelevementManager.getLaboIntersManager(prelTest1));
      prelTest2.setLaboInters(new HashSet<LaboInter>());
      prelTest3.setLaboInters(new HashSet<LaboInter>());
      assertTrue(prelTest1.getNumeroLabo().equals("NEW"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(3), prelTest1).get(0).getBool());
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(2), prelTest2).get(0)
         .getAlphanum().equals("yy"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(2), prelTest3).get(0)
         .getAlphanum().equals("with you"));

      assertTrue(objetNonConformeDao.findByObjetAndEntite(id1, entiteDao.findById(2)).size() == 1);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(id2, entiteDao.findById(2)).size() == 1);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(prelTest3.getPrelevementId(), entiteDao.findById(2)).size() == 1);
      assertTrue(objetNonConformeDao.findByObjetAndEntite(prelTest3.getPrelevementId(), entiteDao.findById(2)).get(0)
         .getNonConformite().getNom().equals("Inconnu"));

      // Suppression
      for(int i = 0; i < list.size(); i++){
         // cleanup cascade vers echantillons
         prelevementManager.removeObjectCascadeManager(list.get(i), null, u, null);
      }
      // verification de l'etat de la base
      testFindAllObjectsManager();
      assertTrue(laboInterDao.findAll().size() == 3);
      assertTrue(echantillonDao.findAll().size() == 4);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 12);
      assertTrue(objetNonConformeDao.findAll().size() == 6);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(list);
      fs.addAll(echans);
      cleanUpFantomes(fs);
   }

   /**
    * Test de la méthode switchMaladieManager().
    */
   @Test
   public void testSwitchMaladieManager() throws ParseException{
      // Insertion nouvel enregistrement
      final Prelevement p = new Prelevement();
      /* Champs obligatoires */
      final Banque b = banqueDao.findById(2);
      p.setCode("pre_1234-12.0");
      final Nature n = natureDao.findById(1);
      final ConsentType ct = consentTypeDao.findById(2);
      //
      final Maladie m = maladieDao.findById(1);
      final Date ctDate = new SimpleDateFormat("dd/MM/yyyy").parse("17/09/2006");
      p.setConsentDate(ctDate);
      final Collaborateur preleveur = collaborateurDao.findById(1);
      final Service s = serviceDao.findById(1);
      final Calendar preDate = Calendar.getInstance();
      preDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/2006 14:57:01"));
      p.setDatePrelevement(preDate);
      final PrelevementType ptype = prelevementTypeDao.findById(1);
      final ConditType cType = conditTypeDao.findById(1);
      final ConditMilieu milieu = conditMilieuDao.findById(1);
      p.setConditNbr(1);
      final Calendar depDate = Calendar.getInstance();
      depDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("20/09/2006 12:57:01"));
      p.setDateDepart(depDate);
      final Transporteur t = transporteurDao.findById(1);
      p.setTransportTemp(new Float(-5.9));
      final Calendar arrDate = Calendar.getInstance();
      arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("22/09/2006 12:57:02"));
      p.setDateArrivee(arrDate);
      final Collaborateur op = collaborateurDao.findById(2);
      p.setQuantite(new Float(12.5));
      final Unite qU = (uniteDao.findByUnite("mg")).get(0);
      p.setPatientNda("NDA1");
      p.setNumeroLabo("1234");
      p.setSterile(true);
      p.setConformeArrivee(true);
      p.setEtatIncomplet(false);
      p.setArchive(false);
      final List<LaboInter> labos = new ArrayList<>();
      final LaboInter l1 = new LaboInter();
      l1.setOrdre(1);
      labos.add(l1);
      l1.setCollaborateur(collaborateurDao.findById(1));
      l1.setSterile(true);
      final Calendar dateLab = Calendar.getInstance();
      dateLab.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("21/09/2006 12:12:12"));
      l1.setDateArrivee(dateLab);
      final LaboInter l2 = new LaboInter();
      l2.setOrdre(2);
      labos.add(l2);
      l2.setService(serviceDao.findById(1));
      l2.setTransporteur(transporteurDao.findById(1));
      l2.setSterile(true);
      l2.setDateDepart(dateLab);
      final Utilisateur u = utilisateurDao.findById(1);

      final Set<Risque> risks = new HashSet<>();
      final Risque r2 = risqueDao.findById(2);
      risks.add(r2);
      p.setRisques(risks);

      // insertion
      prelevementManager.createObjectManager(p, b, n, m, ct, preleveur, s, ptype, cType, milieu, t, op, qU, labos, null, null, u,
         true, "/tmp/", false);

      final Integer id = p.getPrelevementId();
      final Prelevement p2 = prelevementManager.findByIdManager(id);

      // mauvais paramétrage
      prelevementManager.switchMaladieManager(p2, null, u);
      assertTrue(getOperationManager().findByObjectManager(p2).size() == 1);
      prelevementManager.switchMaladieManager(null, maladieDao.findById(2), u);
      assertTrue(getOperationManager().findByObjectManager(p2).size() == 1);
      prelevementManager.switchMaladieManager(p2, maladieDao.findById(2), null);
      assertTrue(getOperationManager().findByObjectManager(p2).size() == 1);
      prelevementManager.switchMaladieManager(p2, maladieDao.findById(1), u);
      assertTrue(getOperationManager().findByObjectManager(p2).size() == 1);

      // changement de maladie
      prelevementManager.switchMaladieManager(p2, maladieDao.findById(2), u);
      assertTrue(getOperationManager().findByObjectManager(p2).size() == 2);

      // vérification que les autres champs n'ont pas changés
      final Prelevement p3 = prelevementManager.findByIdManager(id);
      assertTrue(p3.getPrelevementId().equals(id));
      assertTrue(p3.getBanque().equals(b));
      assertTrue(p3.getCode().equals("pre_1234-12.0"));
      assertTrue(p3.getNature().equals(n));
      assertTrue(p3.getMaladie().equals(maladieDao.findById(2)));
      assertTrue(p3.getConsentType().equals(ct));
      assertTrue(p3.getConsentDate().equals(ctDate));
      assertTrue(p3.getPreleveur().equals(preleveur));
      assertTrue(p3.getServicePreleveur().equals(s));
      assertTrue(p3.getDatePrelevement().equals(preDate));
      assertTrue(p3.getPrelevementType().equals(ptype));
      assertTrue(p3.getConditType().equals(cType));
      assertTrue(p3.getConditMilieu().equals(milieu));
      assertTrue(p3.getConditNbr().equals(1));
      assertTrue(p3.getDateDepart().equals(depDate));
      assertTrue(p3.getTransporteur().equals(t));
      assertTrue(p3.getTransportTemp().equals(new Float(-5.9)));
      assertTrue(p3.getDateArrivee().equals(arrDate));
      assertTrue(p3.getOperateur().equals(op));
      assertTrue(p3.getQuantite().equals(new Float(12.5)));
      assertTrue(p3.getQuantiteUnite().equals(qU));
      assertTrue(p3.getPatientNda().equals("NDA1"));
      assertTrue(p3.getNumeroLabo().equals("1234"));
      assertTrue(p3.getSterile());
      assertNull(p3.getCongDepart());
      assertNull(p3.getCongArrivee());
      assertTrue(p3.getConformeArrivee());
      assertFalse(p3.getEtatIncomplet());
      assertFalse(p3.getArchive());
      assertTrue(prelevementManager.getRisquesManager(p3).size() == 1);
      assertTrue(prelevementManager.getLaboIntersManager(p3).size() == 2);

      prelevementManager.removeObjectCascadeManager(p3, null, u, null);
      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p3);
      cleanUpFantomes(fs);
      assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
      assertTrue(laboInterDao.findAll().size() == 3);
   }

   @Test
   public void testCreateUpdateWithNonConformites(){
      Prelevement prel1 = new Prelevement();
      prel1.setCode("p1");
      final Nature n = natureDao.findById(1);
      final ConsentType ct = consentTypeDao.findById(2);
      prel1.setSterile(true);
      final Utilisateur u = utilisateurDao.findById(1);

      final List<NonConformite> ncfs = new ArrayList<>();
      ncfs.add(nonConformiteDao.findById(1));
      ncfs.add(nonConformiteDao.findById(2));

      prelevementManager.createObjectWithNonConformitesManager(prel1, banqueDao.findById(1), n, null, ct, null, null, null, null,
         null, null, null, null, null, null, u, false, null, false, ncfs);

      prel1 = prelevementManager.findByCodeLikeManager("p1", true).get(0);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(prel1.getPrelevementId(), entiteDao.findById(2)).size() == 2);
      assertFalse(prel1.getConformeArrivee());

      ncfs.clear();
      ncfs.add(nonConformiteDao.findById(1));
      ncfs.add(nonConformiteDao.findById(3));

      prelevementManager.updateObjectWithNonConformitesManager(prel1, banqueDao.findById(1), n, null, ct, null, null, null, null,
         null, null, null, null, null, null, null, u, 0, false, null, false, ncfs);

      assertTrue(objetNonConformeDao.findByObjetAndEntite(prel1.getPrelevementId(), entiteDao.findById(2)).size() == 2);
      assertTrue((objetNonConformeDao.findByObjetAndEntite(prel1.getPrelevementId(), entiteDao.findById(2))).get(0)
         .getNonConformite().getNom().equals("Inconnu")
         || (objetNonConformeDao.findByObjetAndEntite(prel1.getPrelevementId(), entiteDao.findById(2))).get(0).getNonConformite()
            .getNom().equals("Problème livraison"));
      assertTrue((objetNonConformeDao.findByObjetAndEntite(prel1.getPrelevementId(), entiteDao.findById(2))).get(1)
         .getNonConformite().getNom().equals("Inconnu")
         || (objetNonConformeDao.findByObjetAndEntite(prel1.getPrelevementId(), entiteDao.findById(2))).get(1).getNonConformite()
            .getNom().equals("Problème livraison"));
      assertFalse((objetNonConformeDao.findByObjetAndEntite(prel1.getPrelevementId(), entiteDao.findById(2))).get(0)
         .equals((objetNonConformeDao.findByObjetAndEntite(prel1.getPrelevementId(), entiteDao.findById(2))).get(1)));

      prelevementManager.removeObjectManager(prel1, null, u, null);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(prel1);
      cleanUpFantomes(fs);
      assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
      assertTrue(objetNonConformeDao.findAll().size() == 6);
   }

   @Test
   public void testRemoveListFromIds() throws ParseException{
      final Banque b2 = banqueDao.findById(2);
      final Utilisateur u2 = utilisateurDao.findById(2);
      final Nature n1 = natureDao.findById(1);
      final ConsentType ct1 = consentTypeDao.findById(1);

      final List<Integer> ids = new ArrayList<>();

      // null list
      prelevementManager.removeListFromIdsManager(null, null, u2);
      // empty list
      prelevementManager.removeListFromIdsManager(ids, "test", u2);
      // non existing ids
      ids.add(22);
      ids.add(33);
      prelevementManager.removeListFromIdsManager(ids, "", u2);

      testFindAllObjectsManager();

      final Prelevement p1 = new Prelevement();
      p1.setCode("PD1");
      prelevementManager.createObjectManager(p1, b2, n1, null, ct1, null, null, null, null, null, null, null, null, null, null,
         null, u2, false, "/tmp/", false);
      final Prelevement p2 = new Prelevement();
      p2.setCode("PD2");
      prelevementManager.createObjectManager(p2, b2, n1, null, ct1, null, null, null, null, null, null, null, null, null, null,
         null, u2, false, "/tmp/", false);
      final Prelevement p3 = new Prelevement();
      p3.setCode("PD3");
      prelevementManager.createObjectManager(p3, b2, n1, null, ct1, null, null, null, null, null, null, null, null, null, null,
         null, u2, false, "/tmp/", false);

      assertTrue(prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager("PD_", b2, false).size() == 3);

      ids.add(p1.getPrelevementId());
      ids.add(p2.getPrelevementId());
      ids.add(p3.getPrelevementId());
      final Integer prelCessedObjectId = new Integer(1);
      ids.add(prelCessedObjectId); // ce prelevement est parent d'un object cédé

      // rollback used object Exception
      boolean catched = false;
      try{
         prelevementManager.removeListFromIdsManager(ids, "suppr list ids", u2);
      }catch(final ObjectUsedException oe){
         catched = true;
         assertTrue(oe.getMessage().equals("echantillon.cascade.isCessed"));
      }
      assertTrue(catched);
      assertTrue(prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager("PD_", b2, false).size() == 3);

      ids.remove(prelCessedObjectId);
      prelevementManager.removeListFromIdsManager(ids, "suppr list ids", u2);
      assertTrue(prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager("PD_", b2, false).isEmpty());

      assertTrue(getFantomeDao().findByNom("PD2").get(0).getCommentaires().equals("suppr list ids"));

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(p1);
      fs.add(p2);
      fs.add(p3);
      cleanUpFantomes(fs);

      testFindAllObjectsManager();
   }

   @Test
   public void testGindByPatientAndAuthorisationsManager(){

      List<Prelevement> prels = prelevementManager.findByPatientAndAuthorisationsManager(null, null, null);
      assertTrue(prels.isEmpty());

      final Utilisateur superadmin = utilisateurDao.findById(5);
      final Plateforme pf1 = plateformeDao.findById(1);

      // superadmin /////////////////////

      // MAYER = 1 prel
      Patient pat = patientDao.findById(1);

      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, null, superadmin);
      assertTrue(prels.isEmpty());
      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, null);
      assertTrue(prels.isEmpty());

      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, superadmin);
      assertTrue(prels.size() == 1);
      assertTrue(prels.contains(prelevementManager.findByIdManager(3)));

      // SOLIS = aucun prelevement
      pat = patientDao.findById(2);
      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, superadmin);
      assertTrue(prels.isEmpty());

      // DELPHINO = 3 prels
      pat = patientDao.findById(3);
      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, superadmin);
      assertTrue(prels.size() == 3);
      assertTrue(prels.contains(prelevementManager.findByIdManager(1)));
      assertTrue(prels.contains(prelevementManager.findByIdManager(2)));
      assertTrue(prels.contains(prelevementManager.findByIdManager(5)));

      // pf 2 -> aucun prelevement
      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, plateformeDao.findById(2), superadmin);
      assertTrue(prels.isEmpty());

      // user1 admins collections pas accès à banque3!///////////////

      final Utilisateur user1 = utilisateurDao.findById(1);
      // MAYER = 1 prel
      pat = patientDao.findById(1);

      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, user1);
      assertTrue(prels.size() == 1);
      assertTrue(prels.contains(prelevementManager.findByIdManager(3)));

      // SOLIS = aucun prelevement
      pat = patientDao.findById(2);
      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, user1);
      assertTrue(prels.isEmpty());

      // DELPHINO = 2 prels (pas celui de banque 3)
      pat = patientDao.findById(3);
      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, user1);
      assertTrue(prels.size() == 2);
      assertTrue(prels.contains(prelevementManager.findByIdManager(1)));
      assertTrue(prels.contains(prelevementManager.findByIdManager(2)));

      // user3 consult banque 2, admin banque 1 //////////////

      final Utilisateur user3 = utilisateurDao.findById(3);
      // MAYER = 1 prel
      pat = patientDao.findById(1);

      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, user3);
      assertTrue(prels.size() == 1);
      assertTrue(prels.contains(prelevementManager.findByIdManager(3)));

      // SOLIS = aucun prelevement
      pat = patientDao.findById(2);
      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, user3);
      assertTrue(prels.isEmpty());

      // DELPHINO = 2 prels (pas celui de banque 3)
      pat = patientDao.findById(3);
      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, user3);
      assertTrue(prels.size() == 2);
      assertTrue(prels.contains(prelevementManager.findByIdManager(1)));
      assertTrue(prels.contains(prelevementManager.findByIdManager(2)));

      // user4  aucun accès, sauf banque autorise cross patient B1 ///////////

      final Utilisateur user4 = utilisateurDao.findById(4);
      // MAYER = 1 prel
      pat = patientDao.findById(1);

      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, user4);
      assertTrue(prels.isEmpty());

      // SOLIS = aucun prelevement
      pat = patientDao.findById(2);
      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, user4);
      assertTrue(prels.isEmpty());

      // DELPHINO = 2 prels car Banque1 = cross bank
      pat = patientDao.findById(3);
      prels = prelevementManager.findByPatientAndAuthorisationsManager(pat, pf1, user4);
      assertTrue(prels.size() == 2);
      assertTrue(prels.contains(prelevementManager.findByIdManager(1)));
      assertTrue(prels.contains(prelevementManager.findByIdManager(2)));

      // new Patient TransientException
      // Patient newPat = new Patient();
      // prels = prelevementManager
      //		.findByPatientAndAuthorisationsManager(newPat, pf1, user4);
      // assertTrue(prels.size() == 2);

   }

   /**
    * @since 2.1
    */
   @Test
   public void testFindByCodeInPlateformeBanqueManager(){
      final Plateforme p1 = plateformeDao.findById(1);
      List<Prelevement> prels = prelevementManager.findByCodeInPlateformeManager("PRLVT1", p1);
      assertTrue(prels.size() == 1);
      prels = prelevementManager.findByCodeInPlateformeManager("PRLVT%", p1);
      assertTrue(prels.size() == 4);
      prels = prelevementManager.findByCodeInPlateformeManager(null, p1);
      assertTrue(prels.size() == 0);
      prels = prelevementManager.findByCodeInPlateformeManager("PRLVT%", null);
      assertTrue(prels.size() == 0);
   }

}
