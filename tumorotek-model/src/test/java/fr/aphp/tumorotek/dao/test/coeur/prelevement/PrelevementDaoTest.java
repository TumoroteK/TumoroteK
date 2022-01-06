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
package fr.aphp.tumorotek.dao.test.coeur.prelevement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConditMilieuDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConditTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.RisqueDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 *
 * Classe de test pour le DAO PrelevementDao et le bean du
 * domaine Prelevement.
 * Classe de test créée le 29/09/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.1
 *
 */
public class PrelevementDaoTest extends AbstractDaoTest
{

   @Autowired
 PrelevementDao prelevementDao;
   @Autowired
 BanqueDao banqueDao;
   @Autowired
 NatureDao natureDao;
   @Autowired
 MaladieDao maladieDao;
   @Autowired
 ConsentTypeDao consentTypeDao;
   @Autowired
 CollaborateurDao collaborateurDao;
   @Autowired
 ServiceDao serviceDao;
   @Autowired
 PrelevementTypeDao prelevementTypeDao;
   @Autowired
 ConditTypeDao conditTypeDao;
   @Autowired
 ConditMilieuDao conditMilieuDao;
   @Autowired
 UniteDao uniteDao;
   @Autowired
 TransporteurDao transporteurDao;
   @Autowired
 EntiteDao entiteDao;
   @Autowired
 PatientDao patientDao;
   @Autowired
 RisqueDao risqueDao;
   @Autowired
 PlateformeDao plateformeDao;
   @Autowired
 EtablissementDao etablissementDao;

   public PrelevementDaoTest(){}

   @Test
public void setPrelevementDao(final PrelevementDao pDao){
      this.prelevementDao = pDao;
   }

   @Test
public void setNatureDao(final NatureDao nDao){
      this.natureDao = nDao;
   }

   @Test
public void setMaladieDao(final MaladieDao mDao){
      this.maladieDao = mDao;
   }

   @Test
public void setConsentTypeDao(final ConsentTypeDao ctDao){
      this.consentTypeDao = ctDao;
   }

   @Test
public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   @Test
public void setServiceDao(final ServiceDao sDao){
      this.serviceDao = sDao;
   }

   @Test
public void setPrelevementTypeDao(final PrelevementTypeDao ptDao){
      this.prelevementTypeDao = ptDao;
   }

   @Test
public void setConditTypeDao(final ConditTypeDao ctDao){
      this.conditTypeDao = ctDao;
   }

   @Test
public void setConditMilieuDao(final ConditMilieuDao cmDao){
      this.conditMilieuDao = cmDao;
   }

   @Test
public void setUniteDao(final UniteDao uDao){
      this.uniteDao = uDao;
   }

   @Test
public void setTransporteurDao(final TransporteurDao tDao){
      this.transporteurDao = tDao;
   }

   @Test
public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   @Test
public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   @Test
public void setPatientDao(final PatientDao pDao){
      this.patientDao = pDao;
   }

   @Test
public void setPlateformeDao(final PlateformeDao pfDao){
      this.plateformeDao = pfDao;
   }

   @Test
public void setRisqueDao(final RisqueDao rDao){
      this.risqueDao = rDao;
   }

   @Test
public void setEtablissementDao(EtablissementDao eDao) {
	this.etablissementDao = eDao;
}

@Test
public void testFindByNumberEchantillons(){
      final Long nb = new Long(2);
      List<Prelevement> liste = prelevementDao.findByNumberEchantillons(nb);
      assertTrue(liste.size() == 1);

      liste = prelevementDao.findByNumberEchantillons(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testFindAllPrelevements(){
      final List<Prelevement> prels = IterableUtils.toList(prelevementDao.findAll());
      assertTrue(prels.size() == 5);
   }

   /**
    * Test l'appel de la méthode findByExcludedIdCodes().
    */
   @Test
public void testFindByExcludedId(){
      final Banque b = banqueDao.findById(1);
      List<String> codes = prelevementDao.findByExcludedIdCodes(1, b);
      assertTrue(codes.size() == 2);

      codes = prelevementDao.findByExcludedIdCodes(8, b);
      assertTrue(codes.size() == 3);
      codes = prelevementDao.findByExcludedIdCodes(null, null);
      assertTrue(codes.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByIdInList().
    */
   @Test
public void testFindByIdInList(){
      List<Integer> ids = new ArrayList<>();
      ids.add(1);
      ids.add(2);
      ids.add(3);
      List<Prelevement> liste = prelevementDao.findByIdInList(ids);
      assertTrue(liste.size() == 3);
      assertTrue(liste.get(0).getPrelevementId() == 1);

      ids = new ArrayList<>();
      ids.add(1);
      ids.add(10);
      liste = prelevementDao.findByIdInList(ids);
      assertTrue(liste.size() == 1);

      liste = prelevementDao.findByIdInList(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCode().
    */
   @Test
public void testFindByCode(){
      List<Prelevement> prels = prelevementDao.findByCode("PRLVT1");
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByCode("PTRA");
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCode("PRLVT%");
      assertTrue(prels.size() == 4);
      prels = prelevementDao.findByCode(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCodeOrNumLaboWithBanque().
    */
   @Test
public void testFindByCodeOrNumLaboWithBanque(){
      final Banque b1 = banqueDao.findById(1);
      List<Prelevement> prels = prelevementDao.findByCodeOrNumLaboWithBanque("PRLVT1", b1);
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByCodeOrNumLaboWithBanque("PRLVT1", null);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanque("PTRA", b1);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanque("PTRA", null);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanque("PRLVT%", b1);
      assertTrue(prels.size() == 2);
      prels = prelevementDao.findByCodeOrNumLaboWithBanque("PRLVT%", null);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanque(null, b1);
      assertTrue(prels.size() == 0);

      final Banque b2 = banqueDao.findById(2);
      prels = prelevementDao.findByCodeOrNumLaboWithBanque("PRLVT3", b2);
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByCodeOrNumLaboWithBanque("PTRA", b2);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanque("PRLVT%", b2);
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByCodeOrNumLaboWithBanque(null, b2);
      assertTrue(prels.size() == 0);

      prels = prelevementDao.findByCodeOrNumLaboWithBanque("12234", b1);
      assertTrue(prels.size() == 1);
      assertTrue(prels.get(0).getNumeroLabo().equals("12234"));
      prels = prelevementDao.findByCodeOrNumLaboWithBanque("PTRA", b1);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanque("12%", b1);
      assertTrue(prels.size() == 2);
   }

   /**
    * @since 2.1
    */
   @Test
public void testFindByCodeInPlateforme(){
      final Plateforme p1 = plateformeDao.findById(1);
      List<Prelevement> prels = prelevementDao.findByCodeInPlateforme("PRLVT1", p1);
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByCodeInPlateforme("PRLVT1", null);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeInPlateforme("PTRA", p1);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeInPlateforme("PTRA", null);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeInPlateforme("PRLVT%", p1);
      assertTrue(prels.size() == 4);
      prels = prelevementDao.findByCodeInPlateforme("PRLVT_", p1);
      assertTrue(prels.size() == 3);
      prels = prelevementDao.findByCodeInPlateforme("%", p1);
      assertTrue(prels.size() == 5);
      prels = prelevementDao.findByCodeInPlateforme("PRLVT%", null);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeInPlateforme(null, p1);
      assertTrue(prels.size() == 0);

      final Plateforme p2 = plateformeDao.findById(2);
      prels = prelevementDao.findByCodeInPlateforme("PRLVT3", p2);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeInPlateforme("PTRA", p2);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeInPlateforme("PRLVT%", p2);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeInPlateforme(null, p2);
      assertTrue(prels.size() == 0);

      // car ne s'applique pas sur numero labo!
      prels = prelevementDao.findByCodeInPlateforme("12234", p1);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeInPlateforme("PTRA", p1);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeInPlateforme("12%", p1);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCodeOrNumLaboWithBanqueReturnIds().
    */
   @Test
public void testFindByCodeOrNumLaboWithBanqueReturnIds(){
      final Banque b1 = banqueDao.findById(1);
      List<Integer> prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("PRLVT1", b1);
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("PRLVT1", null);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("PTRA", b1);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("PTRA", null);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("PRLVT%", b1);
      assertTrue(prels.size() == 2);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("PRLVT%", null);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds(null, b1);
      assertTrue(prels.size() == 0);

      final Banque b2 = banqueDao.findById(2);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("PRLVT3", b2);
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("PTRA", b2);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("PRLVT%", b2);
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds(null, b2);
      assertTrue(prels.size() == 0);

      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("12234", b1);
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("PTRA", b1);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds("12%", b1);
      assertTrue(prels.size() == 2);
   }

   /**
    * Test l'appel de la méthode toString().
    */
   @Test
public void testToString(){
      Prelevement p1 = prelevementDao.findById(1);
      assertTrue(p1.toString().equals("{" + p1.getCode() + "}"));
      assertTrue(p1.listableObjectId().equals(new Integer(1)));
      assertTrue(p1.entiteNom().equals(entiteDao.findByNom("Prelevement").get(0).getNom()));
      p1 = new Prelevement();
      assertTrue(p1.toString().equals("{Empty Prelevement}"));
   }

   /**
    * Test l'appel de la méthode findByDatePrelevementAfterDate().
    * throws Exception Lance une exception en cas d'erreur.
    */
   @Test
public void testFindByDatePrelevementAfterDate() throws Exception{
      final Calendar sDate = Calendar.getInstance();
      sDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("07/09/1983 12:12:23"));
      List<Prelevement> prels = prelevementDao.findByDatePrelevementAfterDate(sDate);
      assertTrue(prels.size() == 2);
      sDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("15/09/2009 12:12:23"));
      prels = prelevementDao.findByDatePrelevementAfterDate(sDate);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByDatePrelevementAfterDate(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByDatePrelevementAfterDateWithBanque().
    * throws Exception Lance une exception en cas d'erreur.
    */
   @Test
public void testFindByDatePrelevementAfterDateWithBanque() throws Exception{
      final Banque b1 = banqueDao.findById(1);
      final Calendar sDate = Calendar.getInstance();
      sDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("07/09/1983 00:00:01"));
      List<Prelevement> prels = prelevementDao.findByDatePrelevementAfterDateWithBanque(sDate, b1);
      assertTrue(prels.size() == 1);
      sDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("15/09/2009 00:00:01"));
      prels = prelevementDao.findByDatePrelevementAfterDateWithBanque(sDate, b1);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByDatePrelevementAfterDateWithBanque(null, b1);
      assertTrue(prels.size() == 0);

      sDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("07/09/1983 00:00:01"));
      prels = prelevementDao.findByDatePrelevementAfterDateWithBanque(sDate, null);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByDatePrelevementAfterDateWithBanque(null, null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByConsentDateAfterDate().
    * throws Exception Lance une exception en cas d'erreur.
    */
   @Test
public void testFindByConsentDateAfterDate() throws Exception{
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("07/09/1983");
      List<Prelevement> prels = prelevementDao.findByConsentDateAfterDate(search);
      assertTrue(prels.size() == 1);
      search = new SimpleDateFormat("dd/MM/yyyy").parse("15/09/2009");
      prels = prelevementDao.findByConsentDateAfterDate(search);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByConsentDateAfterDate(null);
      assertTrue(prels.size() == 0);
   }

   //	/**
   //	 * Test l'appel de la méthode findByDateCongelationAfterDate().
   //	 * throws Exception Lance une exception en cas d'erreur.
   //	 */
   //	@Test
public void testFindByDateCongelationAfterDate() throws Exception {
   //		Date search = new SimpleDateFormat("dd/MM/yyyy").parse("17/09/1983");
   //		List<Prelevement> prels = prelevementDao.
   //		findByDateCongelationAfterDate(search);
   //		assertTrue(prels.size() == 1);
   //		search = new SimpleDateFormat("dd/MM/yyyy").parse("18/09/1983");
   //		prels = prelevementDao.findByDateCongelationAfterDate(search);
   //		assertTrue(prels.size() == 0);
   //		prels = prelevementDao.findByDateCongelationAfterDate(null);
   //		assertTrue(prels.size() == 0);
   //	}
   //	
   /**
    * Test l'appel de la méthode findByPrelevementType().
    */
   @Test
public void testFindByPrelevementType(){
      PrelevementType type = prelevementTypeDao.findById(1);
      List<Prelevement> prels = prelevementDao.findByPrelevementType(type);
      assertTrue(prels.size() == 3);
      type = prelevementTypeDao.findById(2);
      prels = prelevementDao.findByPrelevementType(type);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByPrelevementType(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByNature().
    */
   @Test
public void testFindByNature(){
      Nature nature = natureDao.findById(1);
      List<Prelevement> prels = prelevementDao.findByNature(nature);
      assertTrue(prels.size() == 2);
      nature = natureDao.findById(4);
      prels = prelevementDao.findByNature(nature);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByNature(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByConsenType().
    */
   @Test
public void testFindByConsentType(){
      ConsentType type = consentTypeDao.findById(3);
      List<Prelevement> prels = prelevementDao.findByConsentType(type);
      assertTrue(prels.size() == 3);
      type = consentTypeDao.findById(2);
      prels = prelevementDao.findByConsentType(type);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByConsentType(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByMaladieLibelle().
    */
   @Test
public void testFindByMaladieLibelleLike(){
      List<Prelevement> prels = prelevementDao.findByMaladieLibelleLike("Addiction coco");
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByMaladieLibelleLike("Addiction%");
      assertTrue(prels.size() == 3);
      prels = prelevementDao.findByMaladieLibelleLike("Cancer prostate");
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByMaladieLibelleLike(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test l'appel de la méthode findBynda().
    */
   @Test
public void testFindByNdaLike(){
      List<Prelevement> prels = prelevementDao.findByNdaLike("NDA65");
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByNdaLike("NDA%");
      assertTrue(prels.size() == 3);
      prels = prelevementDao.findByNdaLike("12");
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByNdaLike(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByBanqueSelectCode().
    */
   @Test
public void testFindByBanqueSelectCode(){
      final Banque b1 = banqueDao.findById(1);
      List<String> codes = prelevementDao.findByBanqueSelectCode(b1);
      assertTrue(codes.size() == 3);
      assertTrue(codes.get(1).equals("PRLVT2"));

      final Banque b2 = banqueDao.findById(2);
      codes = prelevementDao.findByBanqueSelectCode(b2);
      assertTrue(codes.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByBanqueSelectNda().
    */
   @Test
public void testFindByBanqueSelectNda(){
      final Banque b1 = banqueDao.findById(1);
      final List<String> ndas = prelevementDao.findByBanqueSelectNda(b1);
      assertTrue(ndas.size() == 1);
      assertTrue(ndas.get(0).equals("NDA234"));
   }

   @Test
public void testFindByMaladieAndBanque(){
      Banque b1 = banqueDao.findById(1);
      final Maladie m1 = maladieDao.findById(4);
      List<Prelevement> prels = prelevementDao.findByMaladieAndBanque(m1, b1);
      assertTrue(prels.size() == 1);
      assertTrue(prels.get(0).getCode().equals("PRLVT1"));
      b1 = banqueDao.findById(2);
      prels = prelevementDao.findByMaladieAndBanque(m1, b1);
      assertTrue(prels.size() == 0);
   }

   @Test
public void testFindByMaladieAndOtherBanques(){
      final Banque b1 = banqueDao.findById(1);
      Maladie m1 = maladieDao.findById(4);
      List<Prelevement> prels = prelevementDao.findByMaladieAndOtherBanques(m1, b1);
      assertTrue(prels.size() == 1);
      assertTrue(prels.get(0).getCode().equals("PRLVTCROSS"));
      m1 = maladieDao.findById(3);
      prels = prelevementDao.findByMaladieAndBanque(m1, b1);
      assertTrue(prels.size() == 1);
      prels = prelevementDao.findByMaladieAndOtherBanques(m1, b1);
      assertTrue(prels.size() == 0);
   }

   @Test
public void testFindByPatient(){
      Patient p = patientDao.findById(3);
      List<Prelevement> prels = prelevementDao.findByPatient(p);
      assertTrue(prels.size() == 3);
      p = patientDao.findById(2);
      prels = prelevementDao.findByPatient(p);
      assertTrue(prels.size() == 0);
   }

   @Test
public void testFindByPatientNomNip(){
      final Banque b1 = banqueDao.findById(1);
      List<Integer> prels = prelevementDao.findByPatientNomReturnIds("DELPHINO", b1);
      assertTrue(prels.size() == 2);

      prels = prelevementDao.findByPatientNomReturnIds("DELP%", b1);
      assertTrue(prels.size() == 2);

      prels = prelevementDao.findByPatientNomReturnIds("876", b1);
      assertTrue(prels.size() == 2);

      final Banque b2 = banqueDao.findById(2);
      prels = prelevementDao.findByPatientNomReturnIds("DELP%", b2);
      assertTrue(prels.size() == 0);

      prels = prelevementDao.findByPatientNomReturnIds("876", b2);
      assertTrue(prels.size() == 0);

      prels = prelevementDao.findByPatientNomReturnIds("SOLIS", b1);
      assertTrue(prels.size() == 0);

      prels = prelevementDao.findByPatientNomReturnIds(null, b1);
      assertTrue(prels.size() == 0);
   }

   @Test
public void testFindByCodesAndBanquesInList(){
      List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));
      List<String> codes = new ArrayList<>();
      codes.add("PRLVT1");
      codes.add("C1234");
      codes.add("PRLVT3");

      List<Prelevement> liste = prelevementDao.findByCodesAndBanquesInList(codes, bks);
      assertTrue(liste.size() == 3);
      assertTrue(liste.get(0).getCode().equals("PRLVT1"));

      bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      liste = prelevementDao.findByCodesAndBanquesInList(codes, bks);
      assertTrue(liste.size() == 2);

      codes = new ArrayList<>();
      codes.add("XXX");
      codes.add("CCC");
      liste = prelevementDao.findByCodesAndBanquesInList(codes, bks);
      assertTrue(liste.size() == 0);

      liste = prelevementDao.findByCodesAndBanquesInList(null, bks);
      assertTrue(liste.size() == 0);

      liste = prelevementDao.findByCodesAndBanquesInList(codes, null);
      assertTrue(liste.size() == 0);

      liste = prelevementDao.findByCodesAndBanquesInList(null, null);
      assertTrue(liste.size() == 0);

      // since 2.0.13.1 numero_labo
      codes.clear();
      codes.add("121212");
      codes.add("CCC");

      liste = prelevementDao.findByCodesAndBanquesInList(codes, bks);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).getCode().equals("PRLVT2"));

      codes.add("12234");
      liste = prelevementDao.findByCodesAndBanquesInList(codes, bks);
      assertTrue(liste.size() == 2);
   }

   @Test
public void testFindByPatientNomOrNipInList(){
      List<String> criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("MAYER");
      criteres.add("SOLIS");
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));

      List<Integer> liste = prelevementDao.findByPatientNomOrNipInList(criteres, bks);
      assertTrue(liste.size() == 3);

      bks.add(banqueDao.findById(3));
      liste = prelevementDao.findByPatientNomOrNipInList(criteres, bks);
      assertTrue(liste.size() == 4);

      criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("12");
      liste = prelevementDao.findByPatientNomOrNipInList(criteres, bks);
      assertTrue(liste.size() == 4);

      criteres = new ArrayList<>();
      criteres.add("DELPHINO");
      criteres.add("876");
      liste = prelevementDao.findByPatientNomOrNipInList(criteres, bks);
      assertTrue(liste.size() == 3);
   }

   @Test
public void testFindByCodeOrNumLaboInListWithBanque(){
      List<String> criteres = new ArrayList<>();
      criteres.add("PRLVT1");
      criteres.add("PRLVT2");
      criteres.add("PRLVTCROSS");
      final List<Banque> bks = new ArrayList<>();
      bks.add(banqueDao.findById(1));
      bks.add(banqueDao.findById(2));

      List<Integer> liste = prelevementDao.findByCodeOrNumLaboInListWithBanque(criteres, bks);
      assertTrue(liste.size() == 2);

      bks.add(banqueDao.findById(3));
      liste = prelevementDao.findByCodeOrNumLaboInListWithBanque(criteres, bks);
      assertTrue(liste.size() == 3);

      criteres = new ArrayList<>();
      criteres.add("PRLVT1");
      criteres.add("121212");
      liste = prelevementDao.findByCodeOrNumLaboInListWithBanque(criteres, bks);
      assertTrue(liste.size() == 2);

      criteres = new ArrayList<>();
      criteres.add("PRLVT2");
      criteres.add("121212");
      liste = prelevementDao.findByCodeOrNumLaboInListWithBanque(criteres, bks);
      assertTrue(liste.size() == 1);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un prelevement.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   @Test
public void testCrudPrelevement() throws Exception{
      final Prelevement p = new Prelevement();

      /*Champs obligatoires*/
      final Banque b = banqueDao.findById(2);
      p.setBanque(b);
      p.setCode("pre1234");
      final Nature n = natureDao.findById(1);
      p.setNature(n);
      final ConsentType ct = consentTypeDao.findById(2);
      p.setConsentType(ct);
      //
      final Maladie m = maladieDao.findById(1);
      p.setMaladie(m);
      //Date ctDate = new SimpleDateFormat("dd/MM/yyyy").parse("17/09/2006");
      p.setConsentDate(null);
      final Collaborateur preleveur = collaborateurDao.findById(1);
      p.setPreleveur(preleveur);
      final Service s = serviceDao.findById(1);
      p.setServicePreleveur(s);
      final Calendar preDate = Calendar.getInstance();
      preDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/2006 12:12:23"));
      p.setDatePrelevement(preDate);
      final PrelevementType ptype = prelevementTypeDao.findById(1);
      p.setPrelevementType(ptype);
      final ConditType cType = conditTypeDao.findById(1);
      p.setConditType(cType);
      final ConditMilieu milieu = conditMilieuDao.findById(1);
      p.setConditMilieu(milieu);
      p.setConditNbr(10);
      final Calendar depDate = Calendar.getInstance();
      depDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("20/09/2006 12:22:23"));
      p.setDateDepart(depDate);
      final Transporteur t = transporteurDao.findById(1);
      p.setTransporteur(t);
      p.setTransportTemp(new Float(-5.9));
      final Calendar arrDate = Calendar.getInstance();
      arrDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("20/09/2006 12:32:23"));
      p.setDateArrivee(arrDate);
      final Collaborateur op = collaborateurDao.findById(2);
      p.setOperateur(op);
      p.setQuantite(null);
      final Unite qU = (uniteDao.findByUnite("mg")).get(0);
      p.setQuantiteUnite(qU);
      //p.setVolume(new Float(20.0));
      //Unite vU = (uniteDao.findByUnite("ml")).get(0);
      //p.setVolumeUnite(vU);
      p.setPatientNda("NDA1");
      p.setNumeroLabo("1234");
      p.setSterile(true);
      p.setCongDepart(true);
      p.setCongArrivee(false);
      p.setConformeArrivee(false);
      p.setEtatIncomplet(false);
      p.setArchive(false);

      final Set<Risque> risks = new HashSet<>();
      final Risque r2 = risqueDao.findById(2);
      risks.add(r2);
      final Risque r3 = risqueDao.findById(3);
      risks.add(r3);
      p.setRisques(risks);

      // Test de l'insertion
      prelevementDao.save(p);
      final Integer prelId = p.getPrelevementId();
      assertNotNull(prelId);

      // Test de la mise à jour
      final Prelevement p2 = prelevementDao.findById(prelId);
      assertNotNull(p2);
      assertTrue(p2.getBanque().equals(b));
      final Banque b2 = banqueDao.findById(3);
      p2.setBanque(b2);
      assertTrue(p2.getCode() == "pre1234");
      p2.setCode("pre55");
      assertTrue(p2.getNature().equals(n));
      final Nature n2 = natureDao.findById(2);
      p2.setNature(n2);
      assertTrue(p2.getConsentType().equals(ct));
      final ConsentType ct2 = consentTypeDao.findById(1);
      p2.setConsentType(ct2);
      assertTrue(p2.getMaladie().equals(m));
      final Maladie m2 = maladieDao.findById(2);
      p2.setMaladie(m2);
      assertTrue(p2.getConsentDate() == null);
      final Date ctDate2 = new SimpleDateFormat("dd/MM/yyyy").parse("12/10/2005");
      p2.setConsentDate(ctDate2);
      assertTrue(p2.getPreleveur().equals(preleveur));
      final Collaborateur preleveur2 = collaborateurDao.findById(2);
      p2.setPreleveur(preleveur2);
      assertTrue(p2.getServicePreleveur().equals(s));
      final Service s2 = serviceDao.findById(3);
      p2.setServicePreleveur(s2);
      assertTrue(p2.getDatePrelevement().equals(preDate));
      //Date preDate2 = 
      //	new SimpleDateFormat("dd/MM/yyyy").parse("12/10/2005");
      p2.setDatePrelevement(null);
      assertTrue(p2.getPrelevementType().equals(ptype));
      final PrelevementType ptype2 = prelevementTypeDao.findById(2);
      p2.setPrelevementType(ptype2);
      assertTrue(p2.getConditType().equals(cType));
      final ConditType cType2 = conditTypeDao.findById(2);
      p2.setConditType(cType2);
      assertTrue(p2.getConditMilieu().equals(milieu));
      final ConditMilieu milieu2 = conditMilieuDao.findById(2);
      p2.setConditMilieu(milieu2);
      assertTrue(p2.getConditNbr() == 10);
      p2.setConditNbr(11);
      assertTrue(p2.getDateDepart().equals(depDate));
      final Calendar depDate2 = Calendar.getInstance();
      depDate2.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("14/10/2005 12:12:23"));
      p2.setDateDepart(depDate2);
      assertTrue(p2.getTransporteur().equals(t));
      final Transporteur t2 = transporteurDao.findById(2);
      p2.setTransporteur(t2);
      assertTrue(p2.getTransportTemp().equals(new Float(-5.9)));
      p2.setTransportTemp(new Float(-10.5));
      assertTrue(p2.getDateArrivee().equals(arrDate));
      final Calendar arrDate2 = Calendar.getInstance();
      arrDate2.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("15/10/2005 12:12:23"));
      p2.setDateArrivee(arrDate2);
      assertTrue(p2.getOperateur().equals(op));
      final Collaborateur op2 = collaborateurDao.findById(3);
      p2.setOperateur(op2);
      assertNull(p2.getQuantite());
      assertTrue(p2.getQuantiteUnite().equals(qU));
      final List<Unite> unites = uniteDao.findByUnite("µg");
      final Unite qU2 = unites.get(0);
      p2.setQuantiteUnite(qU2);
      assertTrue(p2.getPatientNda() == "NDA1");
      p2.setPatientNda("NDA2");
      assertTrue(p2.getNumeroLabo() == "1234");
      p2.setNumeroLabo("12345");
      assertTrue(p2.getSterile());
      p2.setSterile(false);
      assertTrue(p2.getCongDepart());
      p2.setCongDepart(false);
      assertFalse(p2.getCongArrivee());
      p2.setCongArrivee(true);
      assertFalse(p2.getConformeArrivee());
      p2.setConformeArrivee(true);
      assertFalse(p2.getEtatIncomplet());
      p2.setEtatIncomplet(true);
      assertFalse(p2.getArchive());
      p2.setArchive(true);
      assertTrue(p2.getRisques().size() == 2);
      assertTrue(p2.getRisques().contains(r2));
      assertTrue(p2.getRisques().contains(r3));
      p2.getRisques().clear();
      final Risque r1 = risqueDao.findById(1);
      p2.getRisques().add(r1);
      p2.setQuantite((float) 12.55544);

      //test l'update
      prelevementDao.save(p2);
      assertTrue(prelevementDao.findById(prelId).getBanque().equals(b2));
      assertTrue(prelevementDao.findById(prelId).getCode() == "pre55");
      assertTrue(prelevementDao.findById(prelId).getNature().equals(n2));
      assertTrue(prelevementDao.findById(prelId).getConsentType().equals(ct2));
      assertTrue(prelevementDao.findById(prelId).getMaladie().equals(m2));
      assertTrue(prelevementDao.findById(prelId).getConsentDate().equals(ctDate2));
      assertTrue(prelevementDao.findById(prelId).getPreleveur().equals(preleveur2));
      assertTrue(prelevementDao.findById(prelId).getServicePreleveur().equals(s2));
      assertTrue(prelevementDao.findById(prelId).getDatePrelevement() == null);
      assertTrue(prelevementDao.findById(prelId).getPrelevementType().equals(ptype2));
      assertTrue(prelevementDao.findById(prelId).getConditType().equals(cType2));
      assertTrue(prelevementDao.findById(prelId).getConditMilieu().equals(milieu2));
      assertTrue(prelevementDao.findById(prelId).getConditNbr() == 11);
      assertTrue(prelevementDao.findById(prelId).getDateDepart().equals(depDate2));
      assertTrue(prelevementDao.findById(prelId).getTransporteur().equals(t2));
      assertTrue(prelevementDao.findById(prelId).getTransportTemp().equals(new Float(-10.5)));
      assertTrue(prelevementDao.findById(prelId).getDateArrivee().equals(arrDate2));
      assertTrue(prelevementDao.findById(prelId).getOperateur().equals(op2));
      assertTrue(prelevementDao.findById(prelId).getQuantiteUnite().equals(qU2));
      assertTrue(prelevementDao.findById(prelId).getPatientNda() == "NDA2");
      assertTrue(prelevementDao.findById(prelId).getNumeroLabo() == "12345");
      assertFalse(prelevementDao.findById(prelId).getSterile());
      assertTrue(prelevementDao.findById(prelId).getCongArrivee());
      assertTrue(prelevementDao.findById(prelId).getConformeArrivee());
      assertFalse(prelevementDao.findById(prelId).getCongDepart());
      assertTrue(prelevementDao.findById(prelId).getEtatIncomplet());
      assertTrue(prelevementDao.findById(prelId).getArchive());
      assertTrue(p2.getRisques().size() == 1);
      assertTrue(p2.getRisques().contains(r1));
      assertTrue(p2.getQuantite() == (float) 12.555);

      // Test de la délétion
      prelevementDao.deleteById(prelId);
      assertNull(prelevementDao.findById(prelId));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final Prelevement p1 = new Prelevement();
      final Prelevement p2 = new Prelevement();

      // L'objet 1 n'est pas égal à null
      assertFalse(p1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(p1.equals(p1));
      // 2 objets sont égaux entre eux
      assertTrue(p1.equals(p2));
      assertTrue(p2.equals(p1));

      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = new Banque();
      b3.setNom(b1.getNom());
      b3.setPlateforme(b1.getPlateforme());
      assertTrue(b1.equals(b3));
      assertFalse(b1 == b3);
      final Banque[] banques = new Banque[] {null, b1, b2, b3};
      final String[] codes = new String[] {null, "code1", "code2", ""};

      for(int i = 0; i < banques.length; i++){
         p1.setBanque(banques[i]);
         for(int j = 0; j < codes.length; j++){
            p1.setCode(codes[j]);
            for(int k = 0; k < banques.length; k++){
               p2.setBanque(banques[k]);
               for(int l = 0; l < codes.length; l++){
                  p2.setCode(codes[l]);
                  if(((i == k) || (i + k == 4)) && (j == l)){
                     assertTrue(p1.equals(p2));
                  }else{
                     assertFalse(p1.equals(p2));
                  }
               }
            }
         }
      }

      //dummy test
      final Banque b = new Banque();
      assertFalse(p1.equals(b));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final Prelevement p1 = new Prelevement();
      p1.setPrelevementId(1);
      final Prelevement p2 = new Prelevement();
      p2.setPrelevementId(2);
      final Prelevement p3 = new Prelevement();
      p3.setPrelevementId(3);
      assertTrue(p3.hashCode() > 0);

      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque[] banques = new Banque[] {null, b1, b2};
      final String[] codes = new String[] {null, "code1", "code2", ""};

      for(int i = 0; i < banques.length; i++){
         p1.setBanque(banques[i]);
         for(int j = 0; j < codes.length; j++){
            p1.setCode(codes[j]);
            for(int k = 0; k < banques.length; k++){
               p2.setBanque(banques[k]);
               for(int l = 0; l < codes.length; l++){
                  p2.setCode(codes[l]);
                  if((i == k) && (j == l)){
                     assertTrue(p1.hashCode() == p2.hashCode());
                  }
               }
            }
         }
      }

      final int hash = p1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(p1.hashCode() == p2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == p1.hashCode());
      assertTrue(hash == p1.hashCode());
      assertTrue(hash == p1.hashCode());
      assertTrue(hash == p1.hashCode());

   }

   /**
    * Test la méthode clone.
    */
   @Test
public void testClone(){
      final Prelevement p1 = prelevementDao.findById(1);
      final Prelevement p2 = p1.clone();
      assertTrue(p1.equals(p2));

      if(p1.getPrelevementId() != null){
         assertTrue(p1.getPrelevementId().equals(p2.getPrelevementId()));
      }else{
         assertNull(p2.getPrelevementId());
      }
      if(p1.getBanque() != null){
         assertTrue(p1.getBanque().equals(p2.getBanque()));
      }else{
         assertNull(p2.getBanque());
      }
      if(p1.getCode() != null){
         assertTrue(p1.getCode().equals(p2.getCode()));
      }else{
         assertNull(p2.getCode());
      }
      if(p1.getNature() != null){
         assertTrue(p1.getNature().equals(p2.getNature()));
      }else{
         assertNull(p2.getNature());
      }
      if(p1.getMaladie() != null){
         final Maladie m1 = p1.getMaladie();
         final Maladie m2 = p2.getMaladie();
         assertTrue(m1.getLibelle().equals(m2.getLibelle()) && m1.getPatient().equals(m2.getPatient()));
      }else{
         assertNull(p2.getMaladie());
      }
      if(p1.getConsentType() != null){
         assertTrue(p1.getConsentType().equals(p2.getConsentType()));
      }else{
         assertNull(p2.getConsentType());
      }
      if(p1.getConsentDate() != null){
         assertTrue(p1.getConsentDate().equals(p2.getConsentDate()));
      }else{
         assertNull(p2.getConsentDate());
      }
      if(p1.getPreleveur() != null){
         assertTrue(p1.getPreleveur().equals(p2.getPreleveur()));
      }else{
         assertNull(p2.getPreleveur());
      }
      if(p1.getServicePreleveur() != null){
         assertTrue(p1.getServicePreleveur().equals(p2.getServicePreleveur()));
      }else{
         assertNull(p2.getServicePreleveur());
      }
      if(p1.getDatePrelevement() != null){
         assertTrue(p1.getDatePrelevement().equals(p2.getDatePrelevement()));
      }else{
         assertNull(p2.getDatePrelevement());
      }
      if(p1.getPrelevementType() != null){
         assertTrue(p1.getPrelevementType().equals(p2.getPrelevementType()));
      }else{
         assertNull(p2.getPrelevementType());
      }
      if(p1.getConditType() != null){
         assertTrue(p1.getConditType().equals(p2.getConditType()));
      }else{
         assertNull(p2.getConditType());
      }
      if(p1.getConditMilieu() != null){
         assertTrue(p1.getConditMilieu().equals(p2.getConditMilieu()));
      }else{
         assertNull(p2.getConditMilieu());
      }
      if(p1.getConditNbr() != null){
         assertTrue(p1.getConditNbr().equals(p2.getConditNbr()));
      }else{
         assertNull(p2.getConditNbr());
      }
      if(p1.getDateDepart() != null){
         assertTrue(p1.getDateDepart().equals(p2.getDateDepart()));
      }else{
         assertNull(p2.getDateDepart());
      }
      if(p1.getTransporteur() != null){
         assertTrue(p1.getTransporteur().equals(p2.getTransporteur()));
      }else{
         assertNull(p2.getTransporteur());
      }
      if(p1.getTransportTemp() != null){
         assertTrue(p1.getTransportTemp().equals(p2.getTransportTemp()));
      }else{
         assertNull(p2.getTransportTemp());
      }
      if(p1.getDateArrivee() != null){
         assertTrue(p1.getDateArrivee().equals(p2.getDateArrivee()));
      }else{
         assertNull(p2.getDateArrivee());
      }
      if(p1.getOperateur() != null){
         assertTrue(p1.getOperateur().equals(p2.getOperateur()));
      }else{
         assertNull(p2.getOperateur());
      }
      if(p1.getQuantite() != null){
         assertTrue(p1.getQuantite().equals(p2.getQuantite()));
      }else{
         assertNull(p2.getQuantite());
      }
      if(p1.getQuantiteUnite() != null){
         assertTrue(p1.getQuantiteUnite().equals(p2.getQuantiteUnite()));
      }else{
         assertNull(p2.getQuantiteUnite());
      }
      /*if (p1.getVolume() != null) {
      	assertTrue(p1.getVolume()
      			.equals(p2.getVolume()));
      } else {
      	assertNull(p2.getVolume());
      }
      if (p1.getVolumeUnite() != null) {
      	assertTrue(p1.getVolumeUnite()
      			.equals(p2.getVolumeUnite()));
      } else {
      	assertNull(p2.getVolumeUnite());
      }*/
      if(p1.getPatientNda() != null){
         assertTrue(p1.getPatientNda().equals(p2.getPatientNda()));
      }else{
         assertNull(p2.getPatientNda());
      }
      if(p1.getNumeroLabo() != null){
         assertTrue(p1.getNumeroLabo().equals(p2.getNumeroLabo()));
      }else{
         assertNull(p2.getNumeroLabo());
      }
      if(p1.getSterile() != null){
         assertTrue(p1.getSterile().equals(p2.getSterile()));
      }else{
         assertNull(p2.getSterile());
      }
      assertTrue(p2.getCongDepart().equals(p1.getCongDepart()));
      assertTrue(p2.getCongArrivee().equals(p1.getCongArrivee()));
      if(p1.getConformeArrivee() != null){
         assertTrue(p1.getConformeArrivee().equals(p2.getConformeArrivee()));
      }else{
         assertNull(p2.getConformeArrivee());
      }
      if(p1.getEtatIncomplet() != null){
         assertTrue(p1.getEtatIncomplet().equals(p2.getEtatIncomplet()));
      }else{
         assertNull(p2.getEtatIncomplet());
      }
      if(p1.getArchive() != null){
         assertTrue(p1.getArchive().equals(p2.getArchive()));
      }else{
         assertNull(p2.getArchive());
      }
      assertTrue(p2.getRisques().size() == 2);
      //assertTrue(p1.getDelegate().equals(p2.getDelegate()));

   }

   @Test
public void testFindByBanques(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      final List<Prelevement> res = prelevementDao.findByBanques(banks);
      assertTrue(res.size() == 4);
   }

   @Test
public void testFindByBanquesAllIds(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      final List<Integer> res = prelevementDao.findByBanquesAllIds(banks);
      assertTrue(res.size() == 4);
   }

   @Test
public void testFindCountEclConsentByDates() throws ParseException{
      final Calendar d1 = Calendar.getInstance();
      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1999"));
      final Calendar d2 = Calendar.getInstance();
      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2005"));
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banks.add(b1);
      final List<ConsentType> cTypes = new ArrayList<>();
      final ConsentType rech = consentTypeDao.findById(2);
      cTypes.add(rech);

      List<Long> res = prelevementDao.findCountEclConsentByDates(cTypes, d1, d2, banks);
      assertTrue(res.get(0) == 0);

      cTypes.add(consentTypeDao.findById(1));
      res = prelevementDao.findCountEclConsentByDates(cTypes, d1, d2, banks);
      assertTrue(res.get(0) == 0);

      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      res = prelevementDao.findCountEclConsentByDates(cTypes, d1, d2, banks);
      assertTrue(res.get(0) == 1);

      cTypes.add(consentTypeDao.findById(3));
      res = prelevementDao.findCountEclConsentByDates(cTypes, d1, d2, banks);
      assertTrue(res.get(0) == 2);

      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/0001"));
      res = prelevementDao.findCountEclConsentByDates(cTypes, d1, d2, banks);
      assertTrue(res.get(0) == 2);

      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/11/2009"));
      res = prelevementDao.findCountEclConsentByDates(cTypes, d1, d2, banks);
      assertTrue(res.get(0) == 3);

      final Banque b3 = banqueDao.findById(3);
      banks.add(b3);
      res = prelevementDao.findCountEclConsentByDates(cTypes, d1, d2, banks);
      assertTrue(res.get(0) == 3);
   }

   @Test
public void testFindCountByOrganeByDates() throws ParseException{
      final Calendar d1 = Calendar.getInstance();
      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1999"));
      final Calendar d2 = Calendar.getInstance();
      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2005"));
      final List<Banque> banks = new ArrayList<>();
      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);

      final List<String> codes = new ArrayList<>();
      codes.add("incubus");

      List<Prelevement> res = prelevementDao.findByOrganeByDates(codes, d1, d2, banks);
      assertTrue(res.size() == 0);

      final Banque b1 = banqueDao.findById(1);
      banks.add(b1);
      res = prelevementDao.findByOrganeByDates(codes, d1, d2, banks);
      assertTrue(res.size() == 0);

      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/11/2009"));
      res = prelevementDao.findByOrganeByDates(codes, d1, d2, banks);
      assertTrue(res.size() == 0);

      codes.add("BL");
      res = prelevementDao.findByOrganeByDates(codes, d1, d2, banks);
      assertTrue(res.size() == 1);

      codes.clear();
      codes.add("cheilite");
      res = prelevementDao.findByOrganeByDates(codes, d1, d2, banks);
      assertTrue(res.size() == 0);

      final Banque b3 = banqueDao.findById(3);
      banks.add(b3);
      codes.clear();
      codes.add("K14.5");
      res = prelevementDao.findByOrganeByDates(codes, d1, d2, banks);
      assertTrue(res.size() == 0);
   }

   @Test
public void testFindCountByOrganeByDatesConsent() throws ParseException{
      final Calendar d1 = Calendar.getInstance();
      d1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1999"));
      final Calendar d2 = Calendar.getInstance();
      d2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009"));
      final List<Banque> banks = new ArrayList<>();
      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      final Banque b1 = banqueDao.findById(1);
      banks.add(b1);

      final List<ConsentType> cTypes = new ArrayList<>();
      final ConsentType rech = consentTypeDao.findById(2);
      cTypes.add(rech);

      final List<String> codes = new ArrayList<>();
      codes.add("incubus");

      List<Prelevement> res = prelevementDao.findByOrganeByDatesConsent(codes, d1, d2, banks, cTypes);
      assertTrue(res.size() == 0);

      codes.add("BL");
      res = prelevementDao.findByOrganeByDatesConsent(codes, d1, d2, banks, cTypes);
      assertTrue(res.size() == 0);

      cTypes.add(consentTypeDao.findById(3));
      res = prelevementDao.findByOrganeByDatesConsent(codes, d1, d2, banks, cTypes);
      assertTrue(res.size() == 1);

      final Banque b3 = banqueDao.findById(3);
      banks.add(b3);
      codes.add("K14.5");
      res = prelevementDao.findByOrganeByDatesConsent(codes, d1, d2, banks, cTypes);
      assertTrue(res.size() == 1);
   }

   @Test
public void testFindAssociatePrelsOfType(){
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banks.add(b1);

      final List<Nature> nats = new ArrayList<>();
      nats.add(natureDao.findById(2));

      final Maladie m4 = maladieDao.findById(4);

      List<Prelevement> res = prelevementDao.findAssociatePrelsOfType(m4, nats, banks);
      assertTrue(res.size() == 1);

      nats.add(natureDao.findById(1));
      res = prelevementDao.findAssociatePrelsOfType(m4, nats, banks);
      assertTrue(res.size() == 1);

      final Banque b3 = banqueDao.findById(3);
      banks.add(b3);
      res = prelevementDao.findAssociatePrelsOfType(m4, nats, banks);
      assertTrue(res.size() == 2);

      final Maladie m3 = maladieDao.findById(3);
      res = prelevementDao.findAssociatePrelsOfType(m3, nats, banks);
      assertTrue(res.size() == 1);

      res = prelevementDao.findAssociatePrelsOfType(null, nats, banks);
      assertTrue(res.size() == 0);
   }

   @Test
public void testFindByMaladieAndNature() throws ParseException{
      final Maladie m4 = maladieDao.findById(4);
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("06/09/1983 10:00:00"));
      List<Prelevement> prels = prelevementDao.findByMaladieAndNature(m4, "SANG", cal);
      assertTrue(prels.size() == 2);

      prels = prelevementDao.findByMaladieAndNature(m4, "TISSU", cal);
      assertTrue(prels.size() == 0);

      final Maladie m3 = maladieDao.findById(3);
      cal.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("16/09/1983 10:00:00"));
      prels = prelevementDao.findByMaladieAndNature(m3, "TISSU", cal);
      assertTrue(prels.size() == 1);

      prels = prelevementDao.findByMaladieAndNature(m3, "", cal);
      assertTrue(prels.size() == 0);

      prels = prelevementDao.findByMaladieAndNature(null, "TISSU", cal);
      assertTrue(prels.size() == 0);

      prels = prelevementDao.findByMaladieAndNature(m3, null, cal);
      assertTrue(prels.size() == 0);

      prels = prelevementDao.findByMaladieAndNature(null, null, cal);
      assertTrue(prels.size() == 0);
   }

   @Test
public void testFindByEchantillonId(){
      List<Prelevement> prels = prelevementDao.findByEchantillonId(1);
      assertTrue(prels.size() == 1);
      assertTrue(prels.get(0).equals(prelevementDao.findById(1)));
      prels = prelevementDao.findByEchantillonId(4);
      assertTrue(prels.size() == 1);
      assertTrue(prels.get(0).equals(prelevementDao.findById(3)));
      prels = prelevementDao.findByEchantillonId(10);
      assertTrue(prels.size() == 0);
      prels = prelevementDao.findByEchantillonId(null);
      assertTrue(prels.size() == 0);
   }

   @Test
public void testFindByComDiag(){
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banks.add(b1);

      List<Prelevement> prels = prelevementDao.findByComDiag("TEST", banks);
      assertTrue(prels.isEmpty());

      prels = prelevementDao.findByComDiag("%", banks);
      assertTrue(prels.size() == 2);

      prels = prelevementDao.findByComDiag("CODE SEROTK A", banks);
      assertTrue(prels.size() == 1);
   }

   @Test
public void testFindByEtablissementNom(){
      final List<Banque> banks = banqueDao.findByNom("BANQUE1");
      final List<Prelevement> prels = prelevementDao.findByEtablissementNom("%SAINT%", banks);
      assertFalse(prels.isEmpty());
   }

   @Test
public void testFindByEtablissementVide(){
      final List<Banque> banks = banqueDao.findByNom("BANQUE1");
      final List<Prelevement> prels = prelevementDao.findByEtablissementVide(banks);
      assertTrue(prels.size() == 1);
      assertTrue(prels.contains(prelevementDao.findById(4)));
   }

   @Test
public void testFindByPatientAndBanques(){

      final List<Banque> banks = new ArrayList<>();
      banks.add(banqueDao.findById(1));

      List<Prelevement> prels = prelevementDao.findByPatientAndBanques(null, banks);
      assertTrue(prels.isEmpty());

      // MAYER = 1 prel
      Patient pat = patientDao.findById(1);
      prels = prelevementDao.findByPatientAndBanques(pat, banks);
      assertTrue(prels.isEmpty());

      banks.add(banqueDao.findById(2));
      prels = prelevementDao.findByPatientAndBanques(pat, banks);
      assertTrue(prels.size() == 1);
      assertTrue(prels.contains(prelevementDao.findById(3)));

      // SOLIS = aucun prelevement
      pat = patientDao.findById(2);
      prels = prelevementDao.findByPatientAndBanques(pat, banks);
      assertTrue(prels.isEmpty());

      // DELPHINO = 3 prels
      pat = patientDao.findById(3);

      prels = prelevementDao.findByPatientAndBanques(pat, banks);
      assertTrue(prels.size() == 2);
      assertTrue(prels.contains(prelevementDao.findById(1)));
      assertTrue(prels.contains(prelevementDao.findById(2)));

      banks.add(banqueDao.findById(3));
      prels = prelevementDao.findByPatientAndBanques(pat, banks);
      assertTrue(prels.size() == 3);
      assertTrue(prels.contains(prelevementDao.findById(1)));
      assertTrue(prels.contains(prelevementDao.findById(2)));
      assertTrue(prels.contains(prelevementDao.findById(5)));

      banks.remove(banqueDao.findById(1));
      prels = prelevementDao.findByPatientAndBanques(pat, banks);
      assertTrue(prels.size() == 1);
      assertTrue(prels.contains(prelevementDao.findById(5)));

   }
   
   /**
    * @since 2.2.1
    */
   @Test
public void testFindByEtablissementLaboInter(){
	   
	   final List<Banque> banks = new ArrayList<>();
	   banks.add(banqueDao.findById(1));
	   
	   Etablissement etab1 = etablissementDao.findById(1); 
	   List<Prelevement> prels = prelevementDao.findByEtablissementLaboInter(etab1, banks);
	   assertTrue(prels.size() == 1);
	   assertTrue(prels.get(0).getPrelevementId() == 1);
	   
	   prels = prelevementDao.findByEtablissementLaboInter(etablissementDao.findById(2), banks);
	   assertTrue(prels.isEmpty());
	   
	   prels = prelevementDao.findByEtablissementLaboInter(null, banks);
	   assertTrue(prels.isEmpty());
   }

   /**
    * @since 2.2.1
    */
   @Test
public void testFindByServiceLaboInter(){
	   
	   final List<Banque> banks = new ArrayList<>();
	   banks.add(banqueDao.findById(1));
	   
	   Service srv1 = serviceDao.findById(1); 
	   List<Prelevement> prels = prelevementDao.findByServiceLaboInter(srv1, banks);
	   assertTrue(prels.size() == 1);
	   assertTrue(prels.get(0).getPrelevementId() == 1);
	   
	   prels = prelevementDao.findByServiceLaboInter(serviceDao.findById(4), banks);
	   assertTrue(prels.isEmpty());
	   
	   prels = prelevementDao.findByServiceLaboInter(null, banks);
	   assertTrue(prels.isEmpty());
   }
   
   /**
    * @since 2.2.1
    */
   @Test
public void testFindByOperateurLaboInter(){
	   
	   final List<Banque> banks = new ArrayList<>();
	   banks.add(banqueDao.findById(1));
	   
	   Collaborateur ope2 = collaborateurDao.findById(2); 
	   List<Prelevement> prels = prelevementDao.findByCollaborateurLaboInter(ope2, banks);
	   assertTrue(prels.size() == 1);
	   assertTrue(prels.get(0).getPrelevementId() == 1);
	   
	   prels = prelevementDao.findByCollaborateurLaboInter(collaborateurDao.findById(1), banks);
	   assertTrue(prels.isEmpty());
	   
	   prels = prelevementDao.findByCollaborateurLaboInter(null, banks);
	   assertTrue(prels.isEmpty());
   }
}
