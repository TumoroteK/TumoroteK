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
package fr.aphp.tumorotek.dao.test.echantillon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.cession.CessionTypeDao;
import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchanQualiteDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonTypeDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.ModePrepaDao;
import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.cession.CessionType;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le DAO EchantillonDao et le bean du
 * domaine Echantillon.
 * Classe de test créée le 22/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3.0-gatsbi
 *
 */
public class EchantillonDaoTest extends AbstractDaoTest
{

   private EchantillonDao echantillonDao;
   private PrelevementDao prelevementDao;
   private BanqueDao banqueDao;
   private ObjetStatutDao objetStatutDao;
   private EchantillonTypeDao echantillonTypeDao;
   private EchanQualiteDao echanQualiteDao;
   private ModePrepaDao modePrepaDao;
   private EntiteDao entiteDao;
   private TerminaleDao terminaleDao;
   private EtablissementDao etablissementDao;
   private CessionTypeDao cessionTypeDao;
   private MaladieDao maladieDao;
   private PlateformeDao plateformeDao;

   public EchantillonDaoTest(){}

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setPrelevementDao(final PrelevementDao pDao){
      this.prelevementDao = pDao;
   }

   public void setObjetStatutDao(final ObjetStatutDao oDao){
      this.objetStatutDao = oDao;
   }

   public void setEchantillonTypeDao(final EchantillonTypeDao eDao){
      this.echantillonTypeDao = eDao;
   }

   public void setEchanQualiteDao(final EchanQualiteDao eDao){
      this.echanQualiteDao = eDao;
   }

   public void setModePrepaDao(final ModePrepaDao mDao){
      this.modePrepaDao = mDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setCessionTypeDao(final CessionTypeDao cDao){
      this.cessionTypeDao = cDao;
   }

   public void setTerminaleDao(final TerminaleDao tDao){
      this.terminaleDao = tDao;
   }

   public void setEtablissementDao(final EtablissementDao eDao){
      this.etablissementDao = eDao;
   }

   public void setMaladieDao(final MaladieDao mDao){
      this.maladieDao = mDao;
   }

   public void setPlateformeDao(final PlateformeDao p){
      this.plateformeDao = p;
   }

   public void testReadAllCategories(){
      final List<Echantillon> echans = echantillonDao.findAll();
      assertTrue(echans.size() == 4);
   }

   public void testFindByBanques(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      final List<Echantillon> res = echantillonDao.findByBanques(banks);
      assertTrue(res.size() == 4);
   }

   public void testFindByBanquesAllIds(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      final List<Integer> res = echantillonDao.findByBanquesAllIds(banks);
      assertTrue(res.size() == 4);
   }

   public void testFindByIds(){
      final List<Integer> ids = new java.util.ArrayList<>();
      ids.add(1);
      ids.add(2);
      ids.add(3);
      ids.add(10);
      List<Echantillon> res = echantillonDao.findByIds(ids);
      assertTrue(res.size() == 3);

      res = echantillonDao.findByIds(null);
      assertTrue(res.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCode().
    */
   public void testFindByCode(){
      List<Echantillon> echans = echantillonDao.findByCode("EHT.1");
      assertTrue(echans.size() == 1);
      echans = echantillonDao.findByCode("PTRA");
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByCode("PTRA%");
      assertTrue(echans.size() == 2);
      echans = echantillonDao.findByCode(null);
      assertTrue(echans.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCodeWithBanque().
    */
   public void testFindByCodeWithBanque(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      List<Echantillon> echans = echantillonDao.findByCodeWithBanque("EHT.1", b1);
      assertTrue(echans.size() == 1);
      echans = echantillonDao.findByCodeWithBanque("PTRA", b1);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByCodeWithBanque("PTRA%", b1);
      assertTrue(echans.size() == 2);
      echans = echantillonDao.findByCodeWithBanque("PTRA%", b2);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByCodeWithBanque("PTRA%", null);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByCodeWithBanque(null, b1);
      assertTrue(echans.size() == 0);
   }



   /**
    * @since 2.1
    */
   public void testFindByCodeInPlateforme() {
      final Plateforme p1 = plateformeDao.findById(1);

      List<Echantillon> echans = echantillonDao.findByCodeInPlateforme("PTRA.1", p1);
      assertTrue(echans.size() == 1);
      echans = echantillonDao.findByCodeInPlateforme("JEG.1", p1);
      assertTrue(echans.size() == 1);
      echans = echantillonDao.findByCodeInPlateforme("PTRA.1", null);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByCodeInPlateforme("PTRA", p1);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByCodeInPlateforme("PTRA", null);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByCodeInPlateforme("%.1", p1);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByCodeInPlateforme("PTRA._", p1);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByCodeInPlateforme("%", p1);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByCodeInPlateforme("%", null);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByCodeInPlateforme(null, p1);
      assertTrue(echans.size() == 0);

      final Plateforme p2 = plateformeDao.findById(2);

      echans = echantillonDao.findByCodeInPlateforme("PTRA.1", p2);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByCodeInPlateforme("PTRA", p2);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByCodeInPlateforme("%", p2);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByCodeInPlateforme(null, p2);
      assertTrue(echans.size() == 0);
   }
   /**
    * Test l'appel de la méthode findByCodeWithBanqueReturnIds().
    */
   public void testFindByCodeWithBanqueReturnIds(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      List<Integer> echans = echantillonDao.findByCodeWithBanqueReturnIds("EHT.1", b1);
      assertTrue(echans.size() == 1);
      echans = echantillonDao.findByCodeWithBanqueReturnIds("PTRA", b1);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByCodeWithBanqueReturnIds("PTRA%", b1);
      assertTrue(echans.size() == 2);
      echans = echantillonDao.findByCodeWithBanqueReturnIds("PTRA%", b2);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByCodeWithBanqueReturnIds("PTRA%", null);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByCodeWithBanqueReturnIds(null, b1);
      assertTrue(echans.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByDateStockAfterDate().
    * throws Exception Lance une exception en cas d'erreur.
    */
   public void testFindByDateStockAfterDate() throws Exception{
      final Calendar search = Calendar.getInstance();
      search.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2009"));
      List<Echantillon> echans = echantillonDao.findByDateStockAfterDate(search);
      assertTrue(echans.size() == 1);
      search.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("26/07/2009"));
      echans = echantillonDao.findByDateStockAfterDate(search);
      assertTrue(echans.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByDateStockAfterDateWithBanque().
    * throws Exception Lance une exception en cas d'erreur.
    */
   public void testFindByDateStockAfterDateWithBanque() throws Exception{
      final Banque b1 = banqueDao.findById(1);
      final Calendar search = Calendar.getInstance();
      search.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2008"));
      List<Echantillon> echans = echantillonDao.findByDateStockAfterDateWithBanque(search, b1);
      assertTrue(echans.size() == 2);
      search.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/07/2009"));
      echans = echantillonDao.findByDateStockAfterDateWithBanque(search, b1);
      assertTrue(echans.size() == 1);

      search.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2008"));
      echans = echantillonDao.findByDateStockAfterDateWithBanque(null, b1);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByDateStockAfterDateWithBanque(null, b1);
      assertTrue(echans.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedIdCodes().
    */
   public void testFindByExcludedIdCodes(){
      final Banque b = banqueDao.findById(1);
      List<String> codes = echantillonDao.findByExcludedIdCodes(1, b);
      assertTrue(codes.size() == 2);
      codes = echantillonDao.findByExcludedIdCodes(10, b);
      assertTrue(codes.size() == 3);
      codes = echantillonDao.findByExcludedIdCodes(null, null);
      assertTrue(codes.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByObjetStatut().
    */
   public void testFindByObjetStatut(){
      ObjetStatut obj = objetStatutDao.findById(1);
      List<Echantillon> echans = echantillonDao.findByObjetStatut(obj);
      assertTrue(echans.size() == 2);
      obj = objetStatutDao.findById(4);
      echans = echantillonDao.findByObjetStatut(obj);
      assertTrue(echans.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEchanQualite().
    */
   public void testFindByEchanQualite(){
      EchanQualite qualite = echanQualiteDao.findById(1);
      List<Echantillon> echans = echantillonDao.findByEchanQualite(qualite);
      assertTrue(echans.size() == 3);
      qualite = echanQualiteDao.findById(2);
      echans = echantillonDao.findByEchanQualite(qualite);
      assertTrue(echans.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByEchantillonType().
    */
   public void testFindByEchantillonType(){
      EchantillonType type = echantillonTypeDao.findById(1);
      List<Echantillon> echans = echantillonDao.findByEchantillonType(type);
      assertTrue(echans.size() == 3);
      type = echantillonTypeDao.findById(3);
      echans = echantillonDao.findByEchantillonType(type);
      assertTrue(echans.size() == 1);
   }

   //	/**
   //	 * Test l'appel de la méthode findByCrAnapath().
   //	 */
   //	public void testFindByCrAnapath() {
   //		Fichier anapath = fichierDao.findById(1);
   //		List<Echantillon> echans = echantillonDao.findByCrAnapath(anapath);
   //		assertTrue(echans.size() == 1);
   //		Fichier anapath2 = fichierDao.findById(3);
   //		echans = echantillonDao.findByCrAnapath(anapath2);
   //		assertTrue(echans.size() == 0);
   //	}

   /**
    * Test l'appel de la méthode findByModePrepa().
    */
   public void testFindByModePrepa(){
      final ModePrepa mode = modePrepaDao.findById(1);
      List<Echantillon> echans = echantillonDao.findByModePrepa(mode);
      assertTrue(echans.size() == 2);
      final ModePrepa mode2 = modePrepaDao.findById(4);
      echans = echantillonDao.findByModePrepa(mode2);
      assertTrue(echans.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByPrelevement().
    */
   public void testFindByPrelevement(){
      Prelevement prlvt = prelevementDao.findById(1);
      List<Echantillon> echans = echantillonDao.findByPrelevement(prlvt);
      assertTrue(echans.size() == 2);
      prlvt = prelevementDao.findById(2);
      echans = echantillonDao.findByPrelevement(prlvt);
      assertTrue(echans.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByPatientNomReturnIds().
    */
   public void testFindByPatientNomReturnIds(){
      final Banque b1 = banqueDao.findById(1);
      List<Integer> echans = echantillonDao.findByPatientNomReturnIds("DELPHINO", b1);
      assertTrue(echans.size() == 3);

      echans = echantillonDao.findByPatientNomReturnIds("DELP%", b1);
      assertTrue(echans.size() == 3);

      echans = echantillonDao.findByPatientNomReturnIds("876", b1);
      assertTrue(echans.size() == 3);

      final Banque b2 = banqueDao.findById(2);
      echans = echantillonDao.findByPatientNomReturnIds("DELP%", b2);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByPatientNomReturnIds("876", b2);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByPatientNomReturnIds("SOLIS", b1);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByPatientNomReturnIds(null, b1);
      assertTrue(echans.size() == 0);
   }

   /**
    * Test l'appel de la méthode findRestantsByPrelevement().
    */
   public void testFindRestantsByPrelevement(){
      Prelevement prlvt = prelevementDao.findById(1);
      List<Echantillon> echans = echantillonDao.findRestantsByPrelevement(prlvt);
      assertTrue(echans.size() == 1);
      prlvt = prelevementDao.findById(2);
      echans = echantillonDao.findRestantsByPrelevement(prlvt);
      assertTrue(echans.size() == 0);
      prlvt = prelevementDao.findById(3);
      echans = echantillonDao.findRestantsByPrelevement(prlvt);
      assertTrue(echans.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByPrelevementAndStatut().
    */
   public void testFindByPrelevementAndStatut(){
      Prelevement prlvt = prelevementDao.findById(1);
      final ObjetStatut statut = objetStatutDao.findById(1);
      List<Echantillon> echans = echantillonDao.findByPrelevementAndStatut(prlvt, statut);
      assertTrue(echans.size() == 2);
      prlvt = prelevementDao.findById(2);
      echans = echantillonDao.findByPrelevementAndStatut(prlvt, statut);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByPrelevementAndStatut(null, statut);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByPrelevementAndStatut(prlvt, null);
      assertTrue(echans.size() == 0);
      echans = echantillonDao.findByPrelevementAndStatut(null, null);
      assertTrue(echans.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByBanqueSelectCode().
    */
   public void testFindByBanqueSelectCode(){
      final Banque b1 = banqueDao.findById(1);
      List<String> codes = echantillonDao.findByBanqueSelectCode(b1);
      assertTrue(codes.size() == 3);
      assertTrue(codes.get(2).equals("EHT.1"));

      final Banque b2 = banqueDao.findById(2);
      codes = echantillonDao.findByBanqueSelectCode(b2);
      assertTrue(codes.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByBanqueAndQuantiteSelectCode().
    */
   public void testFindByBanqueAndQuantiteSelectCode(){
      final Banque b1 = banqueDao.findById(1);
      List<String> codes = echantillonDao.findByBanqueAndQuantiteSelectCode(b1);
      assertTrue(codes.size() == 2);
      assertTrue(codes.get(1).equals("PTRA.2"));

      final Banque b2 = banqueDao.findById(2);
      codes = echantillonDao.findByBanqueAndQuantiteSelectCode(b2);
      assertTrue(codes.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByBanqueStatutSelectCode().
    */
   public void testFindByBanqueStatutSelectCode(){
      final Banque b1 = banqueDao.findById(1);
      final ObjetStatut o1 = objetStatutDao.findById(1);
      List<String> codes = echantillonDao.findByBanqueStatutSelectCode(b1, o1);
      assertTrue(codes.size() == 2);
      assertTrue(codes.get(0).equals("PTRA.1"));

      final ObjetStatut o2 = objetStatutDao.findById(2);
      codes = echantillonDao.findByBanqueStatutSelectCode(b1, o2);
      assertTrue(codes.size() == 1);

      final ObjetStatut o3 = objetStatutDao.findById(3);
      codes = echantillonDao.findByBanqueStatutSelectCode(b1, o3);
      assertTrue(codes.size() == 0);

      final Banque b2 = banqueDao.findById(2);
      codes = echantillonDao.findByBanqueStatutSelectCode(b2, o1);
      assertTrue(codes.size() == 0);

      codes = echantillonDao.findByBanqueStatutSelectCode(b1, null);
      assertTrue(codes.size() == 0);

      codes = echantillonDao.findByBanqueStatutSelectCode(null, o1);
      assertTrue(codes.size() == 0);

      codes = echantillonDao.findByBanqueStatutSelectCode(null, null);
      assertTrue(codes.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByBanqueInListStatutSelectCode().
    */
   public void findByBanqueInListStatutSelectCode(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final ObjetStatut o1 = objetStatutDao.findById(1);
      List<Banque> banques = new ArrayList<>();
      banques.add(b1);
      banques.add(b2);
      List<String> codes = echantillonDao.findByBanqueInListStatutSelectCode(banques, o1);
      assertTrue(codes.size() == 2);
      assertTrue(codes.get(0).equals("PTRA.1"));

      final ObjetStatut o2 = objetStatutDao.findById(2);
      codes = echantillonDao.findByBanqueInListStatutSelectCode(banques, o2);
      assertTrue(codes.size() == 1);

      final ObjetStatut o3 = objetStatutDao.findById(3);
      codes = echantillonDao.findByBanqueInListStatutSelectCode(banques, o3);
      assertTrue(codes.size() == 1);

      banques = new ArrayList<>();
      banques.add(b2);
      codes = echantillonDao.findByBanqueInListStatutSelectCode(banques, o1);
      assertTrue(codes.size() == 0);

      codes = echantillonDao.findByBanqueInListStatutSelectCode(banques, null);
      assertTrue(codes.size() == 0);

      codes = echantillonDao.findByBanqueInListStatutSelectCode(null, o1);
      assertTrue(codes.size() == 0);

      codes = echantillonDao.findByBanqueInListStatutSelectCode(null, null);
      assertTrue(codes.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByTerminale().
    */
   public void testFindByTerminale(){
      Entite entite = entiteDao.findByNom("Echantillon").get(0);
      final Terminale term1 = terminaleDao.findById(1);
      List<Echantillon> echans = echantillonDao.findByTerminale(entite, term1);
      assertTrue(echans.size() == 1);

      entite = entiteDao.findByNom("Prelevement").get(0);
      echans = echantillonDao.findByTerminale(entite, term1);
      assertTrue(echans.size() == 0);

      entite = entiteDao.findByNom("Echantillon").get(0);
      final Terminale term2 = terminaleDao.findById(2);
      echans = echantillonDao.findByTerminale(entite, term2);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByTerminale(entite, null);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByTerminale(null, term1);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByTerminale(null, null);
      assertTrue(echans.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByTerminaleDirect().
    */
   public void testFindByTerminaleDirect(){
      final Terminale term1 = terminaleDao.findById(1);
      List<Echantillon> echans = echantillonDao.findByTerminaleDirect(term1);
      assertTrue(echans.size() == 1);

      final Terminale term2 = terminaleDao.findById(2);
      echans = echantillonDao.findByTerminaleDirect(term2);
      assertTrue(echans.size() == 0);

      echans = echantillonDao.findByTerminaleDirect(null);
      assertTrue(echans.size() == 0);
   }

   public void testFindByPatientNomOrNipInList(){
      List<String> criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("MAYER");
      criteres.add("SOLIS");
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));

      List<Integer> liste = echantillonDao.findByPatientNomOrNipInList(criteres, bks);
      assertTrue(liste.size() == 4);

      criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("12");
      liste = echantillonDao.findByPatientNomOrNipInList(criteres, bks);
      assertTrue(liste.size() == 4);

      criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("876");
      liste = echantillonDao.findByPatientNomOrNipInList(criteres, bks);
      assertTrue(liste.size() == 3);
   }

   public void testFindByCodeOrNumLaboInListWithBanque(){
      List<String> criteres = new ArrayList<>();
      criteres.add("PTRA.1");
      criteres.add("PTRA.2");
      criteres.add("JEG.1");
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));

      List<Object[]> liste = echantillonDao.findByCodeInListWithBanque(criteres, bks);
      assertTrue(liste.size() == 3);

      for(final Object[] obj : liste){
         if((Integer) obj[0] == 1){
            assertTrue(obj[1].equals("PTRA.1"));
         }else if((Integer) obj[0] == 2){
            assertTrue(obj[1].equals("PTRA.2"));
         }else if((Integer) obj[0] == 4){
            assertTrue(obj[1].equals("JEG.1"));
         }
      }

      criteres = new ArrayList<>();
      criteres.add("PTRA.1");
      liste = echantillonDao.findByCodeInListWithBanque(criteres, bks);
      assertTrue(liste.size() == 1);
      assertTrue((Integer) liste.get(0)[0] == 1);
      assertTrue(liste.get(0)[1].equals("PTRA.1"));
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un échantillon.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrud() throws Exception{
      final Echantillon e = new Echantillon();
      final String codeUpdated = "CodeUp";
      final ObjetStatut statut = objetStatutDao.findById(1);

      final Prelevement p = prelevementDao.findById(1);
      final Banque b = banqueDao.findById(1);
      final EchantillonType et = echantillonTypeDao.findById(2);
      e.setCode("code");
      e.setEchantillonType(et);
      e.setPrelevement(p);
      e.setBanque(b);
      e.setObjetStatut(statut);
      e.setDateStock(null);
      e.setArchive(false);
      e.setLateralite("D");
      e.setSterile(true);
      e.setConformeTraitement(true);
      e.setConformeCession(true);
      e.setQuantite((float) 15.4587);
      e.setQuantiteInit((float) 18.1122);
      // Test de l'insertion
      echantillonDao.createObject(e);

      assertNotNull(e.getEchantillonId());
      final Integer echanId = e.getEchantillonId();

      // Test de la mise à jour
      final Echantillon e2 = echantillonDao.findById(echanId);
      assertTrue(e2.getQuantite() == (float) 15.459);
      assertTrue(e2.getQuantiteInit() == (float) 18.112);
      assertNotNull(e2);
      assertNotNull(e2.getPrelevement());
      assertNull(e2.getDateStock());
      e2.setCode(codeUpdated);
      assertTrue(e2.getSterile());
      assertTrue(e2.getSterile());
      e2.setSterile(false);
      assertTrue(e2.getConformeTraitement());
      e2.setConformeTraitement(false);
      assertTrue(e2.getConformeCession());
      e2.setConformeCession(false);
      e2.setQuantite(null);
      e2.setQuantiteInit(null);
      echantillonDao.updateObject(e2);
      assertTrue(echantillonDao.findById(echanId).getCode().equals(codeUpdated));
      assertFalse(echantillonDao.findById(echanId).getSterile());
      assertFalse(echantillonDao.findById(echanId).getConformeTraitement());
      assertFalse(echantillonDao.findById(echanId).getConformeCession());
      assertNull(echantillonDao.findById(echanId).getQuantite());
      assertNull(echantillonDao.findById(echanId).getQuantiteInit());

      // Test de la délétion
      echantillonDao.removeObject(echanId);
      assertNull(echantillonDao.findById(echanId));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String code1 = "code1";
      final String code2 = "code2";
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Echantillon e1 = new Echantillon();
      e1.setBanque(b1);
      e1.setCode(code1);
      final Echantillon e2 = new Echantillon();
      e2.setBanque(b1);
      e2.setCode(code1);

      // L'objet 1 n'est pas égal à null
      assertFalse(e1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(e1.equals(e1));
      // 2 objets sont égaux entre eux
      assertTrue(e1.equals(e2));
      assertTrue(e2.equals(e1));

      e1.setBanque(null);
      e1.setCode(null);
      e2.setBanque(null);
      e2.setCode(null);
      assertTrue(e1.equals(e2));
      e2.setCode(code1);
      assertFalse(e1.equals(e2));
      e1.setCode(code1);
      assertTrue(e1.equals(e2));
      e2.setCode(code2);
      assertFalse(e1.equals(e2));
      e2.setCode(null);
      assertFalse(e1.equals(e2));
      e2.setBanque(b1);
      assertFalse(e1.equals(e2));

      e1.setBanque(b1);
      e1.setCode(null);
      e2.setCode(null);
      e2.setBanque(b1);
      assertTrue(e1.equals(e2));
      e2.setBanque(b2);
      assertFalse(e1.equals(e2));
      e2.setCode(code1);
      assertFalse(e1.equals(e2));

      // Vérification de la différenciation de 2 objets
      e1.setCode(code1);
      assertFalse(e1.equals(e2));
      e2.setCode(code2);
      e2.setBanque(b1);
      assertFalse(e1.equals(e2));

      final Categorie c = new Categorie();
      assertFalse(e1.equals(c));

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String code1 = "code1";
      final Banque b1 = banqueDao.findById(1);
      final Echantillon e1 = new Echantillon();
      e1.setBanque(b1);
      e1.setCode(code1);
      final Echantillon e2 = new Echantillon();
      e2.setBanque(b1);
      e2.setCode(code1);
      final Echantillon e3 = new Echantillon();
      e3.setBanque(null);
      e3.setCode(null);
      assertTrue(e3.hashCode() > 0);

      final int hash = e1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(e1.hashCode() == e2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());

   }

   /**
    * Test le toString().
    */
   public void testToString(){
      final Echantillon echan1 = echantillonDao.findById(1);
      assertTrue(echan1.toString().equals("{" + echan1.getCode() + "}"));
      assertTrue(echan1.listableObjectId().equals(new Integer(1)));
      assertTrue(echan1.entiteNom().equals(entiteDao.findByNom("Echantillon").get(0).getNom()));
      final Echantillon echan2 = new Echantillon();
      assertTrue(echan2.toString().equals("{Empty Echantillon}"));
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){

      final Echantillon e1 = echantillonDao.findById(1);
      final ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {});
      e1.setAnapathStream(bais);
      final Echantillon e2 = e1.clone();
      assertTrue(e1.equals(e2));

      if(e1.getEchantillonId() != null){
         assertTrue(e1.getEchantillonId().equals(e2.getEchantillonId()));
      }else{
         assertNull(e2.getEchantillonId());
      }
      if(e1.getBanque() != null){
         assertTrue(e1.getBanque().equals(e2.getBanque()));
      }else{
         assertNull(e2.getBanque());
      }
      if(e1.getPrelevement() != null){
         final Prelevement p1 = e1.getPrelevement();
         final Prelevement p2 = e2.getPrelevement();
         assertTrue(p1.getCode().equals(p2.getCode()) && p1.getBanque().equals(p2.getBanque()));
      }else{
         assertNull(e2.getPrelevement());
      }
      if(e1.getCollaborateur() != null){
         assertTrue(e1.getCollaborateur().equals(e2.getCollaborateur()));
      }else{
         assertNull(e2.getCollaborateur());
      }
      if(e1.getCode() != null){
         assertTrue(e1.getCode().equals(e2.getCode()));
      }else{
         assertNull(e2.getCode());
      }
      if(e1.getObjetStatut() != null){
         assertTrue(e1.getObjetStatut().equals(e2.getObjetStatut()));
      }else{
         assertNull(e2.getObjetStatut());
      }
      if(e1.getDateStock() != null){
         assertTrue(e1.getDateStock().equals(e2.getDateStock()));
      }else{
         assertNull(e2.getDateStock());
      }
      if(e1.getEmplacement() != null){
         assertTrue(e1.getEmplacement().equals(e2.getEmplacement()));
      }else{
         assertNull(e2.getEmplacement());
      }
      if(e1.getEchantillonType() != null){
         assertTrue(e1.getEchantillonType().equals(e2.getEchantillonType()));
      }else{
         assertNull(e2.getEchantillonType());
      }
      if(e1.getQuantite() != null){
         assertTrue(e1.getQuantite().equals(e2.getQuantite()));
      }else{
         assertNull(e2.getQuantite());
      }
      if(e1.getQuantiteInit() != null){
         assertTrue(e1.getQuantiteInit().equals(e2.getQuantiteInit()));
      }else{
         assertNull(e2.getQuantiteInit());
      }
      if(e1.getQuantiteUnite() != null){
         assertTrue(e1.getQuantiteUnite().equals(e2.getQuantiteUnite()));
      }else{
         assertNull(e2.getQuantiteUnite());
      }
      /*if (e1.getVolume() != null) {
      	assertTrue(e1.getVolume().equals(e2.getVolume()));
      } else {
      	assertNull(e2.getVolume());
      }
      if (e1.getVolumeInit() != null) {
      	assertTrue(e1.getVolumeInit().equals(e2.getVolumeInit()));
      } else {
      	assertNull(e2.getVolumeInit());
      }
      if (e1.getVolumeUnite() != null) {
      	assertTrue(e1.getVolumeUnite().equals(e2.getVolumeUnite()));
      } else {
      	assertNull(e2.getVolumeUnite());
      }*/
      if(e1.getDelaiCgl() != null){
         assertTrue(e1.getDelaiCgl().equals(e2.getDelaiCgl()));
      }else{
         assertNull(e2.getDelaiCgl());
      }
      if(e1.getEchanQualite() != null){
         assertTrue(e1.getEchanQualite().equals(e2.getEchanQualite()));
      }else{
         assertNull(e2.getEchanQualite());
      }
      if(e1.getTumoral() != null){
         assertTrue(e1.getTumoral().equals(e2.getTumoral()));
      }else{
         assertNull(e2.getTumoral());
      }
      if(e1.getModePrepa() != null){
         assertTrue(e1.getModePrepa().equals(e2.getModePrepa()));
      }else{
         assertNull(e2.getModePrepa());
      }
      if(e1.getCrAnapath() != null){
         assertTrue(e1.getCrAnapath().equals(e2.getCrAnapath()));
      }else{
         assertNull(e2.getCrAnapath());
      }
      if(e1.getSterile() != null){
         assertTrue(e1.getSterile().equals(e2.getSterile()));
      }else{
         assertNull(e2.getSterile());
      }
      assertTrue(e1.getSterile().equals(e2.getSterile()));
      if(e1.getConformeTraitement() != null){
         assertTrue(e1.getConformeTraitement().equals(e2.getConformeTraitement()));
      }else{
         assertNull(e2.getConformeTraitement());
      }
      if(e1.getConformeCession() != null){
         assertTrue(e1.getConformeCession().equals(e2.getConformeCession()));
      }else{
         assertNull(e2.getConformeCession());
      }
      if(e1.getEtatIncomplet() != null){
         assertTrue(e1.getEtatIncomplet().equals(e2.getEtatIncomplet()));
      }else{
         assertNull(e2.getEtatIncomplet());
      }
      if(e1.getArchive() != null){
         assertTrue(e1.getArchive().equals(e2.getArchive()));
      }else{
         assertNull(e2.getArchive());
      }
      // assertTrue(e1.getCodeOrganes().equals(e2.getCodeOrganes()));
      // assertTrue(e1.getCodeMorphos().equals(e2.getCodeMorphos()));
      assertTrue(e1.getLateralite().equals(e2.getLateralite()));
      // assertTrue(e1.getCodeOrganeExport()
      //					.equals(e2.getCodeOrganeExport()));
      // assertTrue(e1.getCodeLesExport().equals(e2.getCodeLesExport()));
      assertTrue(e1.getCodesAssignes().equals(e2.getCodesAssignes()));
      assertTrue(e1.getAnapathStream().equals(e2.getAnapathStream()));

      try{
         bais.close();
      }catch(final IOException e){
         e.printStackTrace();
      }
   }

   public void testNewEchantillon(){
      final Echantillon echan1 = echantillonDao.findById(1);
      assertFalse(echan1.newEchantillon());

      final Echantillon newEchan = new Echantillon();
      assertTrue(newEchan.newEchantillon());
   }

   public void testCountCongelesByDates() throws ParseException{
      final Calendar d1 = Calendar.getInstance();
      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2008"));
      final Calendar d2 = Calendar.getInstance();
      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("04/11/2009"));
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banks.add(b1);

      List<Long> res = echantillonDao.findCountSamplesByDates(d1, d2, banks);
      assertTrue(res.get(0) == 2);

      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      res = echantillonDao.findCountSamplesByDates(d1, d2, banks);
      assertTrue(res.get(0) == 3);

      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/0001"));
      res = echantillonDao.findCountSamplesByDates(d1, d2, banks);
      assertTrue(res.get(0) == 3);

      res = echantillonDao.findCountSamplesByDates(null, d2, banks);
      assertTrue(res.get(0) == 0);
      res = echantillonDao.findCountSamplesByDates(d1, null, banks);
      assertTrue(res.get(0) == 0);
      res = echantillonDao.findCountSamplesByDates(d1, d2, null);
      assertTrue(res.get(0) == 0);

      d1.setTime(new SimpleDateFormat("dd/MM/yyyy hh:MM:ss").parse("31/10/2010 00:10:00"));
      d2.setTime(new SimpleDateFormat("dd/MM/yyyy hh:MM:ss").parse("31/10/2011 00:10:00"));
      res = echantillonDao.findCountSamplesByDates(d1, d2, banks);
      assertTrue(res.get(0) == 0);

   }

   public void testFindByMaladieAndType() throws ParseException{
      final Maladie m4 = maladieDao.findById(4);
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("06/09/1983 10:00:00"));
      List<Echantillon> liste = echantillonDao.findByMaladieAndType(m4, "CELLULES", cal);
      assertTrue(liste.size() == 2);

      liste = echantillonDao.findByMaladieAndType(m4, "CULOT SEC", cal);
      assertTrue(liste.size() == 0);

      final Maladie m3 = maladieDao.findById(3);
      cal.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("16/09/1983 10:00:00"));
      liste = echantillonDao.findByMaladieAndType(m3, "CULOT SEC", cal);
      assertTrue(liste.size() == 1);

      liste = echantillonDao.findByMaladieAndType(m3, "", cal);
      assertTrue(liste.size() == 0);

      liste = echantillonDao.findByMaladieAndType(null, "CELLULES", cal);
      assertTrue(liste.size() == 0);

      liste = echantillonDao.findByMaladieAndType(m3, null, cal);
      assertTrue(liste.size() == 0);

      liste = echantillonDao.findByMaladieAndType(null, null, cal);
      assertTrue(liste.size() == 0);
   }

   public void testCountCongelesByDatesExt() throws ParseException{
      final Calendar d1 = Calendar.getInstance();
      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2008"));
      final Calendar d2 = Calendar.getInstance();
      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("04/11/2009"));
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      banks.add(b1);
      banks.add(b2);
      final List<Etablissement> etabs = new ArrayList<>();
      final Etablissement e2 = etablissementDao.findById(2);
      etabs.add(e2);
      List<Long> res = echantillonDao.findCountSamplesByDatesExt(d1, d2, banks, etabs);
      assertTrue(res.get(0) == 3);

      final Etablissement e1 = etablissementDao.findById(1);
      etabs.add(e1);
      res = echantillonDao.findCountSamplesByDatesExt(d1, d2, banks, etabs);
      assertTrue(res.get(0) == 0);

      final Etablissement e3 = etablissementDao.findById(3);
      etabs.add(e3);
      etabs.remove(e1);
      res = echantillonDao.findCountSamplesByDatesExt(d1, d2, banks, etabs);
      assertTrue(res.get(0) == 3);

      res = echantillonDao.findCountSamplesByDatesExt(d1, d2, banks, null);
      assertTrue(res.get(0) == 0);
   }

   public void testFindCountEchansByCessTypes() throws ParseException{
      Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("12/11/2009");
      final Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse("14/11/2009");
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      final CessionType cSan = cessionTypeDao.findById(1);
      List<Long> res = echantillonDao.findCountEchansByCessTypes(cSan, d1, d2, banks);
      assertTrue(res.get(0) == 0);

      banks.add(b1);
      res = echantillonDao.findCountEchansByCessTypes(cSan, d1, d2, banks);
      assertTrue(res.get(0) == 1);

      final CessionType cRech = cessionTypeDao.findById(2);
      res = echantillonDao.findCountEchansByCessTypes(cRech, d1, d2, banks);
      assertTrue(res.get(0) == 0);

      d1 = new SimpleDateFormat("dd/MM/yyyy").parse("12/10/2009");
      res = echantillonDao.findCountEchansByCessTypes(cRech, d1, d2, banks);
      assertTrue(res.get(0) == 3);

      final CessionType cDest = cessionTypeDao.findById(3);
      res = echantillonDao.findCountEchansByCessTypes(cDest, d1, d2, banks);
      assertTrue(res.get(0) == 0);
   }

   public void testFindAssociateEchansOfType(){
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banks.add(b1);

      final List<EchantillonType> types = new ArrayList<>();
      types.add(echantillonTypeDao.findById(3));

      final Maladie m4 = maladieDao.findById(4);
      final Prelevement p1 = prelevementDao.findById(1);

      List<Echantillon> res = echantillonDao.findAssociateEchansOfType(m4, types, banks, null);
      assertTrue(res.size() == 0);

      types.add(echantillonTypeDao.findById(1));
      res = echantillonDao.findAssociateEchansOfType(m4, types, banks, null);
      assertTrue(res.size() == 2);

      types.add(echantillonTypeDao.findById(1));
      res = echantillonDao.findAssociateEchansOfType(m4, types, banks, p1);
      assertTrue(res.size() == 2);

      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      res = echantillonDao.findAssociateEchansOfType(m4, types, banks, null);
      assertTrue(res.size() == 2);

      final Maladie m1 = maladieDao.findById(1);
      res = echantillonDao.findAssociateEchansOfType(m1, types, banks, null);
      assertTrue(res.size() == 1);

      final Maladie m2 = maladieDao.findById(2);
      res = echantillonDao.findAssociateEchansOfType(m2, types, banks, null);
      assertTrue(res.size() == 0);

      res = echantillonDao.findAssociateEchansOfType(m2, types, banks, p1);
      assertTrue(res.size() == 2);

      res = echantillonDao.findAssociateEchansOfType(null, types, banks, p1);
      assertTrue(res.size() == 2);
   }

   public void testFindCountByPrelevement(){
      final Prelevement p1 = prelevementDao.findById(1);
      List<Long> res = echantillonDao.findCountByPrelevement(p1);
      assertTrue(res.get(0) == 2);

      final Prelevement p2 = prelevementDao.findById(2);
      res = echantillonDao.findCountByPrelevement(p2);
      assertTrue(res.get(0) == 1);

      final Prelevement p3 = prelevementDao.findById(3);
      res = echantillonDao.findCountByPrelevement(p3);
      assertTrue(res.get(0) == 1);

      final Prelevement p4 = prelevementDao.findById(4);
      res = echantillonDao.findCountByPrelevement(p4);
      assertTrue(res.get(0) == 0);
   }

   public void testFindCountRestantsByPrelevement(){
      final Prelevement p1 = prelevementDao.findById(1);
      List<Long> res = echantillonDao.findCountRestantsByPrelevement(p1);
      assertTrue(res.get(0) == 1);

      final Prelevement p2 = prelevementDao.findById(2);
      res = echantillonDao.findCountRestantsByPrelevement(p2);
      assertTrue(res.get(0) == 0);

      final Prelevement p3 = prelevementDao.findById(3);
      res = echantillonDao.findCountRestantsByPrelevement(p3);
      assertTrue(res.get(0) == 0);

      final Prelevement p4 = prelevementDao.findById(4);
      res = echantillonDao.findCountRestantsByPrelevement(p4);
      assertTrue(res.get(0) == 0);
   }

   public void testFindCountByPrelevementAndStockeReserve(){
      final Prelevement p1 = prelevementDao.findById(1);
      List<Long> res = echantillonDao.findCountByPrelevementAndStockeReserve(p1);
      assertTrue(res.get(0) == 2);

      final Prelevement p2 = prelevementDao.findById(2);
      res = echantillonDao.findCountByPrelevementAndStockeReserve(p2);
      assertTrue(res.get(0) == 0);

      final Prelevement p3 = prelevementDao.findById(3);
      res = echantillonDao.findCountByPrelevementAndStockeReserve(p3);
      assertTrue(res.get(0) == 1);

      final Prelevement p4 = prelevementDao.findById(4);
      res = echantillonDao.findCountByPrelevementAndStockeReserve(p4);
      assertTrue(res.get(0) == 0);
   }

   public void testSortByCode(){
      final Echantillon e1 = new Echantillon();
      e1.setCode("TEST.1");
      final Echantillon e2 = new Echantillon();
      e2.setCode("TEST.11");
      final Echantillon e3 = new Echantillon();
      e3.setCode("TEST.2");
      final Echantillon e4 = new Echantillon();
      e4.setCode("TATAT");
      final Echantillon e5 = new Echantillon();
      e5.setCode("ZAAA.ZZ");
      final Echantillon e6 = new Echantillon();
      e6.setCode("ZAAA.23A");
      final Echantillon e7 = new Echantillon();
      e7.setCode("ZAAA.2");
      final Echantillon e8 = new Echantillon();
      e8.setCode("AAA.2");
      final Echantillon e9 = new Echantillon();
      e9.setCode("ZAAA.");
      final Echantillon e10 = new Echantillon();
      e10.setCode("ZAAA..");
      final List<Echantillon> echans = new ArrayList<>();
      echans.add(e5);
      echans.add(e2);
      echans.add(e3);
      echans.add(e9);
      echans.add(e10);
      echans.add(e8);
      echans.add(e6);
      echans.add(e7);
      echans.add(e1);
      echans.add(e4);
      Collections.sort(echans, new Echantillon.CodeComparator(true));
      assertTrue(echans.get(0).equals(e8));
      assertTrue(echans.get(1).equals(e4));
      assertTrue(echans.get(2).equals(e1));
      assertTrue(echans.get(3).equals(e3));
      assertTrue(echans.get(4).equals(e2));
      assertTrue(echans.get(5).equals(e9));
      assertTrue(echans.get(6).equals(e10));
      assertTrue(echans.get(7).equals(e7));
      assertTrue(echans.get(8).equals(e6));
      assertTrue(echans.get(9).equals(e5));
   }

   public void testFindByEmplacementDao(){
      List<Echantillon> echans = echantillonDao.findByEmplacement(terminaleDao.findById(1), 3);
      assertTrue(echans.size() == 1);
      assertTrue(echans.contains(echantillonDao.findById(2)));
      echans = echantillonDao.findByEmplacement(terminaleDao.findById(1), 1);
      assertTrue(echans.isEmpty());
      echans = echantillonDao.findByEmplacement(null, 1);
      assertTrue(echans.isEmpty());
      echans = echantillonDao.findByEmplacement(terminaleDao.findById(1), null);
      assertTrue(echans.isEmpty());
   }
   
   public void testFindByPatientIdentifiantOrNomOrNipInList(){
      List<String> criteres = new ArrayList<>();
      criteres.add("876");
      criteres.add("SOLIS");
      criteres.add("SLS-1234");
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));

      // les échantillons patients 876 (DELPHINO) et SOLIS
      // ne seront pas pris en compte car ils n'ont pas d'identifiant (donc pas Gatsbi !)
      List<Integer> liste = echantillonDao.findByPatientIdentifiantOrNomOrNipInList(criteres, bks);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).equals(4));

      // NOM
      criteres.clear();
      criteres.add("MAYER");
      liste = echantillonDao.findByPatientIdentifiantOrNomOrNipInList(criteres, bks);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).equals(4));
      
      // NIP
      criteres.clear();
      criteres.add("12");
      liste = echantillonDao.findByPatientIdentifiantOrNomOrNipInList(criteres, bks);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).equals(4));
   }
   
   public void testFindByPatientIdentifiantOrNomOrNipReturnIds(){
      
      // IDENTIFIANT
      String search = "SLS-1234";
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));

      List<Integer> liste = echantillonDao.findByPatientIdentifiantOrNomOrNipReturnIds(search, bks);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).equals(4));

      // NOM
      search = "MAYER";
      liste = echantillonDao.findByPatientIdentifiantOrNomOrNipReturnIds(search, bks);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).equals(4));
      
      // NIP
      search = "12";
      liste = echantillonDao.findByPatientIdentifiantOrNomOrNipReturnIds(search, bks);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).equals(4));
      
      // FAIL
      search = "NOO";
      liste = echantillonDao.findByPatientIdentifiantOrNomOrNipReturnIds(search, bks);
      assertTrue(liste.isEmpty());
   }
}
