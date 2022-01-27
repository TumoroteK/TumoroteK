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
package fr.aphp.tumorotek.test.manager.coeur.echantillon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.manager.code.CodeAssigneManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.PrelEchansManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.dto.EchantillonDTOManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.systeme.FichierManager;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager PrelEchansManager.
 * Classe créée le 06/02/2017.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.1.1
 *
 */
public class PrelEchanManagerTest extends AbstractManagerTest4
{

//   @Autowired
   private PrelEchansManager prelEchansManager;
//   @Autowired
   private PrelevementManager prelevementManager;
//   @Autowired
   private EchantillonManager echantillonManager;
//   @Autowired
   private EchantillonDTOManager echantillonDTOManager;
//   @Autowired
   private BanqueDao banqueDao;
//   @Autowired
   private UtilisateurDao utilisateurDao;
//   @Autowired
   private EchantillonTypeDao echantillonTypeDao;
//   @Autowired
   private NatureDao natureDao;
//   @Autowired
   private ConsentTypeDao consentTypeDao;
//   @Autowired
   private EmplacementManager emplacementManager;
//   @Autowired
   private MaladieManager maladieManager;
//   @Autowired
   private PatientManager patientManager;
//   @Autowired
   private CodeAssigneManager codeAssigneManager;
//   @Autowired
   private NonConformiteDao nonConformiteDao;
 //  @Autowired
   private ObjetNonConformeManager objetNonConformeManager;
//   @Autowired
   private TerminaleDao terminaleDao;
   // @Autowired
   // private EntiteDao entiteDao;
//   @Autowired
   private FichierManager fichierManager;

   private Patient pat;
   private Maladie mal;
   private Prelevement prel;
   private Echantillon e1, e2, e3, e4, e5;
   private final List<EchantillonDTO> dtos = new ArrayList<>();
   private final List<CodeAssigne> cOrgs1 = new ArrayList<>();
   private final List<CodeAssigne> cOrgs2 = new ArrayList<>();
   private final List<CodeAssigne> cLes1 = new ArrayList<>();
   private final List<CodeAssigne> cLes2 = new ArrayList<>();

   private int nbPatsBeforeTests;
   private int nbMalsBeforeTests;
   private int nbPrelsBeforeTests;
   private int nbEchansBeforeTests;
   private int nbEmplBeforeTests;
   private int nbEmplOccupeBeforeTests;
   private int nbFichiersBeforeTests;

   private final String bDir = "/tmp/pt_1/coll_1";

   public PrelEchanManagerTest(){}

   private void initCodesAssigne(){
      final CodeAssigne o1 = new CodeAssigne();
      o1.setCode("C1");
      o1.setLibelle("L1");
      o1.setOrdre(1);
      o1.setExport(true);
      o1.setIsOrgane(true);
      o1.setIsMorpho(false);
      cOrgs1.add(o1);
      final CodeAssigne o2 = new CodeAssigne();
      o2.setIsOrgane(true);
      o2.setIsMorpho(false);
      o2.setOrdre(2);
      o2.setExport(false);
      o2.setCode("C2");
      o2.setLibelle("L2");
      cOrgs1.add(o2);
      final CodeAssigne o3 = new CodeAssigne();
      o3.setIsOrgane(true);
      o3.setIsMorpho(false);
      o3.setOrdre(1);
      o3.setExport(true);
      o3.setCode("C3");
      o3.setLibelle("L3");
      cOrgs2.add(o3);

      final CodeAssigne l1 = new CodeAssigne();
      l1.setIsOrgane(false);
      l1.setIsMorpho(true);
      l1.setOrdre(1);
      l1.setExport(true);
      l1.setCode("1111");
      l1.setLibelle("LES1");
      cLes1.add(l1);
      final CodeAssigne l2 = new CodeAssigne();
      l2.setIsOrgane(false);
      l2.setIsMorpho(true);
      l2.setOrdre(1);
      l2.setExport(true);
      l2.setCode("2222");
      l2.setLibelle("LES2");
      cLes2.add(l2);
      final CodeAssigne l3 = new CodeAssigne();
      l3.setIsOrgane(false);
      l3.setIsMorpho(true);
      l3.setOrdre(2);
      l3.setExport(false);
      l3.setCode("3333");
      l3.setLibelle("LES3");
      cLes2.add(l3);
   }

   private void assertsPrelEchansOK(final Banque b){
      // prel
      assertTrue(prelevementManager.findAllObjectsManager().size() == nbPrelsBeforeTests + 1);
      assertTrue(prelevementManager.findByCodeLikeManager("QuickPrel", true).get(0).getBanque().equals(b));
      assertTrue(prelevementManager.getEchantillonsManager(prel).size() == 5);

      // echans
      assertTrue(echantillonManager.findAllObjectsManager().size() == nbEchansBeforeTests + 5);
      assertTrue(echantillonManager.findByCodeLikeWithBanqueManager("QuickE_", b, true).size() == 5);
   }

   @Test
   public void testCreateQuickPrelAndEchansExistingPatAndMaladie(){

      // prel + maladie + nature + consent_type
      // 5 echans + codes organe et lesionnels

      final Banque b1 = banqueDao.findById(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      prel = new Prelevement();
      prel.setCode("QuickPrel");
      prel.setNature(natureDao.findById(1));
      prel.setConsentType(consentTypeDao.findById(1));

      initCodesAssigne();

      dtos.get(0).getCodesOrgsToCreateOrEdit().addAll(cOrgs1);
      dtos.get(0).getCodesLesToCreateOrEdit().addAll(cLes1);

      dtos.get(1).getCodesOrgsToCreateOrEdit().addAll(cOrgs1);
      dtos.get(1).getCodesLesToCreateOrEdit().addAll(cLes1);

      dtos.get(2).getCodesOrgsToCreateOrEdit().addAll(cOrgs2);
      dtos.get(2).getCodesLesToCreateOrEdit().addAll(cLes2);

      dtos.get(3).getCodesOrgsToCreateOrEdit().addAll(cOrgs2);
      dtos.get(3).getCodesLesToCreateOrEdit().addAll(cLes2);

      dtos.get(4).getCodesOrgsToCreateOrEdit().addAll(cOrgs1);
      dtos.get(4).getCodesLesToCreateOrEdit().addAll(cLes2);

      prel = prelEchansManager.createQuickPrelAndEchansManager(patientManager.findByIdManager(5),
         maladieManager.findByCodeLikeManager("C34.5", true).get(0), prel, dtos, null, b1, utilisateur, null, null);

      assertTrue(patientManager.findAllObjectsManager().size() == nbPatsBeforeTests);
      assertTrue(maladieManager.findAllObjectsManager().size() == nbMalsBeforeTests);

      // maladie take precedence on patient if both have ids
      assertTrue(prelevementManager.findByMaladieLibelleLikeManager("Cancer prostate", true).contains(prel));

      assertsPrelEchansOK(b1);

      final List<Echantillon> echans = new ArrayList<>(prelevementManager.getEchantillonsManager(prel));

      // codes assigne
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(echans.get(0)).size() == 2);
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(echans.get(0)).get(0).getCode().equals("C1"));
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(echans.get(0)).size() == 1);
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(echans.get(0)).get(0).getCode().equals("1111"));
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(echans.get(3)).size() == 1);
      assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(echans.get(3)).get(0).getCode().equals("C3"));
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(echans.get(3)).size() == 2);
      assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(echans.get(3)).get(0).getCode().equals("2222"));
   }

   @Test
   public void testCreateQuickPrelAndEchansExistingPatNewMaladie(){

      // prel + maladie + nature + consent_type
      // 5 echans + nonconfs
      final Banque b1 = banqueDao.findById(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      mal = new Maladie();
      mal.setCode("M1");
      mal.setLibelle("LIB1");

      prel = new Prelevement();
      prel.setCode("QuickPrel");
      prel.setNature(natureDao.findById(1));
      prel.setConsentType(consentTypeDao.findById(1));

      // cas possibles non conformites
      dtos.get(0).getEchantillon().setConformeTraitement(false);
      dtos.get(0).getNonConformiteTraitements().add(nonConformiteDao.findById(4));
      dtos.get(0).getEchantillon().setConformeCession(false);
      dtos.get(0).getNonConformiteCessions().add(nonConformiteDao.findById(7));
      dtos.get(0).getCodesLesToCreateOrEdit().addAll(cLes1);

      dtos.get(1).getEchantillon().setConformeTraitement(true);
      dtos.get(1).getEchantillon().setConformeCession(true);

      dtos.get(2).getEchantillon().setConformeTraitement(false);
      dtos.get(2).getEchantillon().setConformeCession(false);

      // true --> noconfs non pris en compte
      dtos.get(3).getEchantillon().setConformeTraitement(true);
      dtos.get(3).getNonConformiteTraitements().add(nonConformiteDao.findById(4));
      dtos.get(3).getEchantillon().setConformeCession(true);
      dtos.get(3).getNonConformiteCessions().add(nonConformiteDao.findById(7));
      dtos.get(3).getCodesLesToCreateOrEdit().addAll(cLes1);

      // dtos.get(4) -> nulls

      prel = prelEchansManager.createQuickPrelAndEchansManager(patientManager.findByIdManager(5), mal, prel, dtos, null, b1,
         utilisateur, null, null);

      // pour tearDown cleanup
      mal = prel.getMaladie();

      assertTrue(patientManager.findAllObjectsManager().size() == nbPatsBeforeTests);
      assertTrue(maladieManager.findAllObjectsManager().size() == nbMalsBeforeTests + 1);

      assertTrue(prelevementManager.findByMaladieLibelleLikeManager("LIB1", true).contains(prel));

      assertsPrelEchansOK(b1);

      final List<Echantillon> echans = new ArrayList<>(prelevementManager.getEchantillonsManager(prel));

      // noconfs
      assertTrue(objetNonConformeManager.findByObjetManager(echans.get(0)).size() == 2);
      assertTrue(
         objetNonConformeManager.findByObjetAndTypeManager(echans.get(0), nonConformiteDao.findById(4).getConformiteType()).get(0)
            .getNonConformite().getId() == 4);
      assertTrue(
         objetNonConformeManager.findByObjetAndTypeManager(echans.get(0), nonConformiteDao.findById(7).getConformiteType()).get(0)
            .getNonConformite().getId() == 7);
      assertTrue(objetNonConformeManager.findByObjetManager(echans.get(1)).isEmpty());
      assertTrue(objetNonConformeManager.findByObjetManager(echans.get(2)).isEmpty());
      assertTrue(objetNonConformeManager.findByObjetManager(echans.get(3)).isEmpty());
      assertTrue(objetNonConformeManager.findByObjetManager(echans.get(4)).isEmpty());
   }

   @Test
   public void testCreateQuickPrelAndEchansNewPatNewMaladie() throws ParseException{

      // prel + maladie + nature + consent_type
      // 5 echans + stockage
      final Banque b1 = banqueDao.findById(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      pat = new Patient();
      pat.setNom("GRIMES");
      pat.setPrenom("RICK");
      pat.setDateNaissance(new SimpleDateFormat("yyyyMMdd").parse("19740325"));
      pat.setSexe("M");
      pat.setPatientEtat("V");

      mal = new Maladie();
      mal.setCode("M1");
      mal.setLibelle("LIB1");

      prel = new Prelevement();
      prel.setCode("QuickPrel");
      prel.setNature(natureDao.findById(1));
      prel.setConsentType(consentTypeDao.findById(1));

      // stockage
      final Map<Echantillon, Emplacement> empls = new HashMap<>();
      empls.put(e2, emplacementManager.findByIdManager(5)); // existing empl
      final Emplacement newE = new Emplacement(); // new Empl T6 pos 3
      newE.setTerminale(terminaleDao.findById(6));
      newE.setPosition(3);
      empls.put(e3, newE);
      empls.put(e5, emplacementManager.findByIdManager(7)); // existing empl

      prel = prelEchansManager.createQuickPrelAndEchansManager(pat, mal, prel, dtos, empls, b1, utilisateur, null, null);

      // pour tearDown cleanup
      mal = prel.getMaladie();
      pat = mal.getPatient();

      assertTrue(patientManager.findAllObjectsManager().size() == nbPatsBeforeTests + 1);
      assertTrue(maladieManager.findAllObjectsManager().size() == nbMalsBeforeTests + 1);

      assertTrue(patientManager.findByNomLikeManager("GRIMES", true).get(0).getPrenom().equals("RICK"));
      assertTrue(maladieManager.findByPatientManager(pat).get(0).equals(mal));
      assertTrue(prelevementManager.findByMaladieLibelleLikeManager("LIB1", true).contains(prel));

      assertsPrelEchansOK(b1);

      final List<Echantillon> echans = new ArrayList<>(prelevementManager.getEchantillonsManager(prel));

      // stockage asserts
      assertTrue(echans.get(0).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertTrue(echans.get(1).getObjetStatut().getStatut().equals("STOCKE"));
      assertFalse(emplacementManager.findByIdManager(5).getVide());
      assertTrue(emplacementManager.findByIdManager(5).getObjetId().equals(echans.get(1).getEchantillonId()));
      assertTrue(echans.get(2).getObjetStatut().getStatut().equals("STOCKE"));
      final Emplacement newEmp = echantillonManager.getEmplacementManager(echans.get(2));
      assertFalse(newEmp.getVide());
      assertTrue(echans.get(3).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertTrue(echans.get(4).getObjetStatut().getStatut().equals("STOCKE"));

      // clean up emplacements
      final Emplacement emp5 = emplacementManager.findByIdManager(5);
      emp5.setVide(true);
      emp5.setObjetId(null);
      emp5.setEntite(null);
      emplacementManager.saveManager(emp5, emp5.getTerminale(), null);
      echantillonManager.saveManager(echans.get(1), b1, prel, null, echans.get(4).getObjetStatut(), null,
         echans.get(4).getEchantillonType(), null, null, null, null, null, null, null, null, null, utilisateur, false, null,
         null);
      final Emplacement emp7 = emplacementManager.findByIdManager(7);
      emp7.setVide(true);
      emp7.setObjetId(null);
      emp7.setEntite(null);
      emplacementManager.saveManager(emp7, emp7.getTerminale(), null);
      echantillonManager.saveManager(echans.get(4), b1, prel, null, echans.get(4).getObjetStatut(), null,
         echans.get(4).getEchantillonType(), null, null, null, null, null, null, null, null, null, utilisateur, false, null,
         null);
   }

   @Test
   public void testCreateQuickPrelAndEchansErrorOnEchan(){

      // prel + maladie + nature + consent_type
      // 5 echans + codes organe et lesionnels

      final Banque b1 = banqueDao.findById(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      prel = new Prelevement();
      prel.setCode("QuickPrel");
      prel.setNature(natureDao.findById(1));
      prel.setConsentType(consentTypeDao.findById(1));

      // error
      dtos.get(3).getEchantillon().setCode("**$$ùù**$");

      boolean catched = false;
      try{
         prelEchansManager.createQuickPrelAndEchansManager(null, maladieManager.findByCodeLikeManager("C34.5", true).get(0), prel,
            dtos, null, b1, utilisateur, null, null);
      }catch(final RuntimeException ve){
         catched = true;
         prel.setPrelevementId(null);
      }
      assertTrue(catched);
      assertNothingHappened();

      // correctif
      // permet relancer enregistrement après erreur
      dtos.get(3).getEchantillon().setCode("QuickE4");

      prel = prelEchansManager.createQuickPrelAndEchansManager(null, maladieManager.findByCodeLikeManager("C34.5", true).get(0),
         prel, dtos, null, b1, utilisateur, null, null);

      assertsPrelEchansOK(b1);
   }

   @Test
   public void testCreateQuickPrelAndEchansNewPatNewMaladieErrorOnEchan() throws ParseException{

      // prel + maladie + nature + consent_type
      // 5 echans + stockage + fichiers CR + anno chp 13 et 14
      final Banque b1 = banqueDao.findById(1);
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      final Fichier cr = new Fichier();
      cr.setNom("anapath1");
      cr.setMimeType("text/plain");
      final String tmp = "Echantillon diagnostic Veronica virus";
      final byte[] byteArray = tmp.getBytes();
      final ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);

      final Fichier cr2 = new Fichier();
      cr2.setNom("anapath2");
      cr2.setMimeType("text/plain");
      final String tmp2 = "Echantillon diagnostic no virus";
      final byte[] byteArray2 = tmp2.getBytes();
      final ByteArrayInputStream bais2 = new ByteArrayInputStream(byteArray2);

      pat = new Patient();
      pat.setNom("GRIMES");
      pat.setPrenom("RICK");
      pat.setDateNaissance(new SimpleDateFormat("yyyyMMdd").parse("19740325"));
      pat.setSexe("M");
      pat.setPatientEtat("V");

      mal = new Maladie();
      mal.setCode("M1");
      mal.setLibelle("LIB1");

      prel = new Prelevement();
      prel.setCode("QuickPrel");
      prel.setNature(natureDao.findById(1));
      prel.setConsentType(consentTypeDao.findById(1));

      // stockage
      final Map<Echantillon, Emplacement> empls = new HashMap<>();
      empls.put(e2, emplacementManager.findByIdManager(5)); // existing empl
      final Emplacement newE = new Emplacement(); // new Empl T6 pos 3
      newE.setTerminale(terminaleDao.findById(6));
      newE.setPosition(3);
      empls.put(e3, newE);
      empls.put(e5, emplacementManager.findByIdManager(7)); // existing empl

      // cr anapath pour 2 lots d'echs
      e2.setCrAnapath(cr.cloneNoId());
      e2.setAnapathStream(bais); // first
      e4.setCrAnapath(cr2.cloneNoId());
      e4.setAnapathStream(bais2);
      e5.setCrAnapath(cr2.cloneNoId());

      // error
      dtos.get(4).getEchantillon().setCode("**$$ùù**$");

      boolean catched = false;
      try{
         prelEchansManager.createQuickPrelAndEchansManager(null, maladieManager.findByCodeLikeManager("C34.5", true).get(0), prel,
            dtos, null, b1, utilisateur, null, null);
      }catch(final RuntimeException ve){
         catched = true;
         prel.setPrelevementId(null);
      }
      assertTrue(catched);
      assertNothingHappened();

      // correctif
      // permet relancer enregistrement après erreur
      dtos.get(4).getEchantillon().setCode("QuickE5");

      prel = prelEchansManager.createQuickPrelAndEchansManager(pat, mal, prel, dtos, empls, b1, utilisateur, null, "/tmp/");

      // pour tearDown cleanup
      mal = prel.getMaladie();
      pat = mal.getPatient();

      assertTrue(patientManager.findAllObjectsManager().size() == nbPatsBeforeTests + 1);
      assertTrue(maladieManager.findAllObjectsManager().size() == nbMalsBeforeTests + 1);

      assertsPrelEchansOK(b1);

      final List<Echantillon> echans = new ArrayList<>(prelevementManager.getEchantillonsManager(prel));

      // stockage asserts
      assertTrue(echans.get(0).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertTrue(echans.get(1).getObjetStatut().getStatut().equals("STOCKE"));
      assertFalse(emplacementManager.findByIdManager(5).getVide());
      assertTrue(emplacementManager.findByIdManager(5).getObjetId().equals(echans.get(1).getEchantillonId()));
      assertTrue(echans.get(2).getObjetStatut().getStatut().equals("STOCKE"));
      final Emplacement newEmp = echantillonManager.getEmplacementManager(echans.get(2));
      assertFalse(newEmp.getVide());
      assertTrue(echans.get(3).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertTrue(echans.get(4).getObjetStatut().getStatut().equals("STOCKE"));

      // file asserts
      assertTrue(fichierManager.findAllObjectsManager().size() == nbFichiersBeforeTests + 3);
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 2);
      assertTrue(echans.get(0).getCrAnapath() == null);
      assertTrue(echans.get(1).getCrAnapath() != null);
      assertTrue(new File(echans.get(1).getCrAnapath().getPath()).length() == 37);
      assertTrue(echans.get(2).getCrAnapath() == null);
      assertTrue(echans.get(3).getCrAnapath().getPath().equals(echans.get(4).getCrAnapath().getPath()));
      assertTrue(new File(echans.get(3).getCrAnapath().getPath()).length() == 31);

      // clean up emplacements
      final Emplacement emp5 = emplacementManager.findByIdManager(5);
      emp5.setVide(true);
      emp5.setObjetId(null);
      emp5.setEntite(null);
      emplacementManager.saveManager(emp5, emp5.getTerminale(), null);
      echantillonManager.saveManager(echans.get(1), b1, prel, null, echans.get(4).getObjetStatut(), null,
         echans.get(4).getEchantillonType(), null, null, null, null, null, null, null, null, null, utilisateur, false, null,
         null);
      final Emplacement emp7 = emplacementManager.findByIdManager(7);
      emp7.setVide(true);
      emp7.setObjetId(null);
      emp7.setEntite(null);
      emplacementManager.saveManager(emp7, emp7.getTerminale(), null);
      echantillonManager.saveManager(echans.get(4), b1, prel, null, echans.get(4).getObjetStatut(), null,
         echans.get(4).getEchantillonType(), null, null, null, null, null, null, null, null, null, utilisateur, false, null,
         null);
   }

   @Before
   public void setup(){
      pat = null;
      mal = null;
      prel = null;
      dtos.clear();
      cOrgs1.clear();
      cOrgs2.clear();
      cLes1.clear();
      cLes2.clear();

      assertNothingHappened();

      final EchantillonType type1 = echantillonTypeDao.findById(1);
      final EchantillonType type2 = echantillonTypeDao.findById(2);

      e1 = new Echantillon();
      e1.setCode("QuickE1");
      e1.setEchantillonType(type1);
      final EchantillonDTO dto1 = echantillonDTOManager.initEchantillonDecoratorManager(e1);

      e2 = new Echantillon();
      e2.setCode("QuickE2");
      e2.setEchantillonType(type1);
      final EchantillonDTO dto2 = echantillonDTOManager.initEchantillonDecoratorManager(e2);

      e3 = new Echantillon();
      e3.setCode("QuickE3");
      e3.setEchantillonType(type2);
      final EchantillonDTO dto3 = echantillonDTOManager.initEchantillonDecoratorManager(e3);

      e4 = new Echantillon();
      e4.setCode("QuickE4");
      e4.setEchantillonType(type2);
      final EchantillonDTO dto4 = echantillonDTOManager.initEchantillonDecoratorManager(e4);

      e5 = new Echantillon();
      e5.setCode("QuickE5");
      e5.setEchantillonType(type2);
      final EchantillonDTO dto5 = echantillonDTOManager.initEchantillonDecoratorManager(e5);

      dtos.add(dto1);
      dtos.add(dto2);
      dtos.add(dto3);
      dtos.add(dto4);
      dtos.add(dto5);

      // tmp dir
      // creation architecture fichiers
      new File(bDir + "/cr_anapath/").mkdirs();

      // creation architecture fichiers
      new File(bDir + "annos/chp_13").mkdirs();
      new File(bDir + "annos/chp_14").mkdirs();
   }

   private void assertNothingHappened(){
      nbPatsBeforeTests = patientManager.findAllObjectsManager().size();
      nbMalsBeforeTests = maladieManager.findAllObjectsManager().size();
      nbPrelsBeforeTests = prelevementManager.findAllObjectsManager().size();
      nbEchansBeforeTests = echantillonManager.findAllObjectsManager().size();
      assertTrue(echantillonManager.findByCodeLikeManager("QuickE_", true).isEmpty());
      nbEmplBeforeTests = emplacementManager.findAllObjectsManager().size();
      nbEmplOccupeBeforeTests = emplacementManager.findByTerminaleAndVideManager(terminaleDao.findById(1), false).size()
         + emplacementManager.findByTerminaleAndVideManager(terminaleDao.findById(2), false).size();
      nbFichiersBeforeTests = fichierManager.findAllObjectsManager().size();
   }

   @After
   public void tearDown(){

      final List<Echantillon> echans = echantillonDTOManager.extractListeManager(dtos);

      final List<TKFantomableObject> fs = new ArrayList<>();

      // cleanUp// Nettoyage
      if(prel.getPrelevementId() != null){
         prelevementManager.deleteByIdCascadeManager(prel, null, utilisateurDao.findById(1), null);
         fs.add(prel);
         fs.addAll(echans);
      }

      if(mal != null && mal.getMaladieId() != null){
         maladieManager.deleteByIdManager(mal, null, utilisateurDao.findById(1));
         fs.add(mal);
      }

      if(pat != null && pat.getPatientId() != null){
         patientManager.deleteByIdManager(pat, null, utilisateurDao.findById(1), null);
         fs.add(pat);
      }

      cleanUpFantomes(fs);

      assertTrue(nbPrelsBeforeTests == prelevementManager.findAllObjectsManager().size());
      assertTrue(nbEchansBeforeTests == echantillonManager.findAllObjectsManager().size());
      assertTrue(nbPatsBeforeTests == patientManager.findAllObjectsManager().size());
      assertTrue(nbMalsBeforeTests == maladieManager.findAllObjectsManager().size());
      assertTrue(nbEmplBeforeTests == emplacementManager.findAllObjectsManager().size());
      assertTrue(
         nbEmplOccupeBeforeTests == emplacementManager.findByTerminaleAndVideManager(terminaleDao.findById(1), false).size()
            + emplacementManager.findByTerminaleAndVideManager(terminaleDao.findById(2), false).size());
      assertTrue(nbFichiersBeforeTests == fichierManager.findAllObjectsManager().size());

      // file system
      assertTrue(new File("/tmp/pt_1/coll_1/cr_anapath").listFiles().length == 0);
      // assertTrue(new File("/tmp/pt_1/coll_1/annos/chp_13").listFiles().length == 0);
      // assertTrue(new File("/tmp/pt_1/coll_1/annos/chp_14").listFiles().length == 0);

      // delete
      new File(bDir).delete();
   }

}
