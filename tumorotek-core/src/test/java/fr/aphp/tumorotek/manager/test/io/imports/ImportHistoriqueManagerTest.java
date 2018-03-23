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
package fr.aphp.tumorotek.manager.test.io.imports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.imports.ImportTemplateDao;
import fr.aphp.tumorotek.dao.io.imports.ImportationDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager ImportHistoriqueManager.
 * Classe créée le 09/02/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0.10.3
 *
 */
public class ImportHistoriqueManagerTest extends AbstractManagerTest
{

   private ImportHistoriqueManager importHistoriqueManager;
   private ImportTemplateDao importTemplateDao;
   private UtilisateurDao utilisateurDao;
   private ImportationDao importationDao;
   private EntiteDao entiteDao;
   private ObjetStatutDao objetStatutDao;

   private PatientManager patientManager;
   private PrelevementManager prelevementManager;
   private EchantillonManager echantillonManager;
   private ProdDeriveManager prodDeriveManager;
   private BanqueDao banqueDao;
   private NatureDao natureDao;
   private ConsentTypeDao consentTypeDao;
   private EchantillonTypeDao echantillonTypeDao;
   private ProdTypeDao prodTypeDao;

   public ImportHistoriqueManagerTest(){

   }

   public void setImportHistoriqueManager(final ImportHistoriqueManager iManager){
      this.importHistoriqueManager = iManager;
   }

   public void setImportTemplateDao(final ImportTemplateDao iDao){
      this.importTemplateDao = iDao;
   }

   public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   public void setImportationDao(final ImportationDao iDao){
      this.importationDao = iDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setPatientManager(final PatientManager pManager){
      this.patientManager = pManager;
   }

   public void setPrelevementManager(final PrelevementManager pManager){
      this.prelevementManager = pManager;
   }

   public void setEchantillonManager(final EchantillonManager eManager){
      this.echantillonManager = eManager;
   }

   public void setProdDeriveManager(final ProdDeriveManager pManager){
      this.prodDeriveManager = pManager;
   }

   public void setNatureDao(final NatureDao nDao){
      this.natureDao = nDao;
   }

   public void setConsentTypeDao(final ConsentTypeDao ctDao){
      this.consentTypeDao = ctDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEchantillonTypeDao(final EchantillonTypeDao eTypeDao){
      this.echantillonTypeDao = eTypeDao;
   }

   public void setProdTypeDao(final ProdTypeDao pDao){
      this.prodTypeDao = pDao;
   }

   public void setObjetStatutDao(final ObjetStatutDao oDao){
      this.objetStatutDao = oDao;
   }

   /**
    * Test la méthode findById.
    */
   public void testFindById(){
      final ImportHistorique temp = importHistoriqueManager.findByIdManager(1);
      assertNotNull(temp);

      final ImportHistorique tempNull = importHistoriqueManager.findByIdManager(10);
      assertNull(tempNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   public void testFindAll(){
      final List<ImportHistorique> list = importHistoriqueManager.findAllObjectsManager();
      assertTrue(list.size() == 3);
   }

   /**
    * Test la méthode findByTemplateWithOrderManager.
    */
   public void testFindByTemplateWithOrderManager(){
      ImportTemplate it = importTemplateDao.findById(1);
      List<ImportHistorique> list = importHistoriqueManager.findByTemplateWithOrderManager(it);
      assertTrue(list.size() == 2);

      it = importTemplateDao.findById(2);
      list = importHistoriqueManager.findByTemplateWithOrderManager(it);
      assertTrue(list.size() == 1);

      it = importTemplateDao.findById(3);
      list = importHistoriqueManager.findByTemplateWithOrderManager(it);
      assertTrue(list.size() == 0);

      list = importHistoriqueManager.findByTemplateWithOrderManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findImportationsByHistoriqueManager.
    */
   public void testFindImportationsByHistoriqueManager(){
      ImportHistorique ih = importHistoriqueManager.findByIdManager(1);
      List<Importation> list = importHistoriqueManager.findImportationsByHistoriqueManager(ih);
      assertTrue(list.size() == 2);

      ih = importHistoriqueManager.findByIdManager(2);
      list = importHistoriqueManager.findImportationsByHistoriqueManager(ih);
      assertTrue(list.size() == 0);

      list = importHistoriqueManager.findImportationsByHistoriqueManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findImportationsByHistoriqueAndEntiteManager.
    */
   public void testFindImportationsByHistoriqueAndEntiteManager(){
      final ImportHistorique ih = importHistoriqueManager.findByIdManager(1);
      final ImportHistorique ih2 = importHistoriqueManager.findByIdManager(2);
      final Entite e1 = entiteDao.findById(1);
      final Entite e3 = entiteDao.findById(3);
      List<Importation> list = importHistoriqueManager.findImportationsByHistoriqueAndEntiteManager(ih, e1);
      assertTrue(list.size() == 1);

      list = importHistoriqueManager.findImportationsByHistoriqueAndEntiteManager(ih, e3);
      assertTrue(list.size() == 0);

      list = importHistoriqueManager.findImportationsByHistoriqueAndEntiteManager(ih2, e1);
      assertTrue(list.size() == 0);

      list = importHistoriqueManager.findImportationsByHistoriqueAndEntiteManager(null, e1);
      assertTrue(list.size() == 0);

      list = importHistoriqueManager.findImportationsByHistoriqueAndEntiteManager(ih, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findImportationsByObjectManager.
    */
   public void testFindImportationsByObjectManager(){
      final Patient p1 = patientManager.findByNomLikeManager("MAYER", true).get(0);
      final Prelevement prlvt = prelevementManager.findByIdManager(4);
      final Echantillon echan = echantillonManager.findByIdManager(1);
      final ProdDerive derive = prodDeriveManager.findByIdManager(1);
      final Banque b = banqueDao.findById(1);

      List<Importation> list = importHistoriqueManager.findImportationsByObjectManager(p1);
      assertTrue(list.size() == 1);

      list = importHistoriqueManager.findImportationsByObjectManager(prlvt);
      assertTrue(list.size() == 1);

      list = importHistoriqueManager.findImportationsByObjectManager(echan);
      assertTrue(list.size() == 0);

      list = importHistoriqueManager.findImportationsByObjectManager(derive);
      assertTrue(list.size() == 0);

      list = importHistoriqueManager.findImportationsByObjectManager(b);
      assertTrue(list.size() == 0);

      list = importHistoriqueManager.findImportationsByObjectManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findImportationsByEntiteAndObjectIdManager.
    */
   public void testFindImportationsByEntiteAndObjectIdManager(){
      final Entite e1 = entiteDao.findById(1);
      final Entite e3 = entiteDao.findById(3);

      List<Importation> list = importHistoriqueManager.findImportationsByEntiteAndObjectIdManager(e1, 1);
      assertTrue(list.size() == 1);

      list = importHistoriqueManager.findImportationsByEntiteAndObjectIdManager(e1, 15);
      assertTrue(list.size() == 0);

      list = importHistoriqueManager.findImportationsByEntiteAndObjectIdManager(e3, 1);
      assertTrue(list.size() == 0);

      list = importHistoriqueManager.findImportationsByEntiteAndObjectIdManager(e1, null);
      assertTrue(list.size() == 0);

      list = importHistoriqueManager.findImportationsByEntiteAndObjectIdManager(null, 15);
      assertTrue(list.size() == 0);
   }

   /**
    * Teste la methode createObjectManager. 
    * @throws ParseException 
    */
   public void testCreateObjectManager() throws ParseException{

      final ImportTemplate it1 = importTemplateDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(1);
      final Calendar cal = Calendar.getInstance();
      final Entite e3 = entiteDao.findById(3);

      final ImportHistorique ih1 = new ImportHistorique();
      ih1.setImportTemplate(it1);
      ih1.setUtilisateur(u);
      ih1.setDate(cal);

      Boolean catched = false;
      // on test l'insertion avec le template null
      try{
         importHistoriqueManager.createObjectManager(ih1, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 3);

      // on test l'insertion avec l'utilisateur null
      try{
         importHistoriqueManager.createObjectManager(ih1, it1, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 3);

      // insertion valide avec les assos à null
      importHistoriqueManager.createObjectManager(ih1, it1, u, null);
      assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 4);
      assertTrue(importationDao.findAll().size() == 2);
      final Integer idH1 = ih1.getImportHistoriqueId();

      // Vérification
      final ImportHistorique ihTest = importHistoriqueManager.findByIdManager(idH1);
      assertNotNull(ihTest);
      assertNotNull(ihTest.getDate());
      assertNotNull(ihTest.getImportTemplate());
      assertNotNull(ihTest.getUtilisateur());
      assertTrue(importHistoriqueManager.findImportationsByHistoriqueManager(ihTest).size() == 0);

      // insertion valide avec les assos
      final ImportHistorique ih2 = new ImportHistorique();
      ih2.setImportTemplate(it1);
      ih2.setUtilisateur(u);
      final Calendar cal2 = Calendar.getInstance();
      cal2.add(Calendar.MONTH, 2);
      ih2.setDate(cal2);

      final Importation i1 = new Importation();
      i1.setObjetId(1);
      i1.setEntite(e3);
      i1.setImportHistorique(ih2);
      final Importation i2 = new Importation();
      i2.setObjetId(2);
      i2.setEntite(e3);
      i2.setImportHistorique(ih2);
      final List<Importation> importations = new ArrayList<>();
      importations.add(i1);
      importations.add(i2);

      importHistoriqueManager.createObjectManager(ih2, it1, u, importations);
      assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 5);
      assertTrue(importationDao.findAll().size() == 4);
      final Integer idH2 = ih2.getImportHistoriqueId();

      // Vérification
      final ImportHistorique ihTest2 = importHistoriqueManager.findByIdManager(idH2);
      assertNotNull(ihTest2);
      assertNotNull(ihTest2.getDate());
      assertNotNull(ihTest2.getImportTemplate());
      assertNotNull(ihTest2.getUtilisateur());
      assertTrue(importHistoriqueManager.findImportationsByHistoriqueManager(ihTest2).size() == 2);

      importHistoriqueManager
         .removeImportationManager(importHistoriqueManager.findImportationsByHistoriqueManager(ihTest2).get(0));
      assertTrue(importationDao.findAll().size() == 3);
      importHistoriqueManager.removeObjectManager(ihTest);
      importHistoriqueManager.removeObjectManager(ihTest2);
      assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 3);
      assertTrue(importationDao.findAll().size() == 2);

      importHistoriqueManager.removeImportationManager(null);
      importHistoriqueManager.removeObjectManager(null);
      assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 3);
      assertTrue(importationDao.findAll().size() == 2);
   }

   /**
    * Test de la suppression en cascade des importations lors de la
    * suppression d'un objet importé.
    * @throws ParseException 
    */
   public void testRemoveImportationsByObjects() throws ParseException{
      final ImportTemplate it1 = importTemplateDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(1);
      final Calendar cal = Calendar.getInstance();
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final Entite e3 = entiteDao.findById(3);
      final Entite e8 = entiteDao.findById(8);
      final List<Importation> importations = new ArrayList<>();

      final Banque banque = banqueDao.findById(1);
      final Nature nature = natureDao.findById(1);
      final ConsentType consentType = consentTypeDao.findById(1);
      final EchantillonType eType = echantillonTypeDao.findById(1);
      final ProdType pType = prodTypeDao.findById(1);

      final ImportHistorique ih1 = new ImportHistorique();
      ih1.setImportTemplate(it1);
      ih1.setUtilisateur(u);
      ih1.setDate(cal);

      // Patient
      final Patient pat = new Patient();
      pat.setNom("MOI");
      pat.setPatientEtat("V");
      pat.setPrenom("MOI");
      pat.setSexe("M");
      final Date date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/1958");
      cal.setTime(date);
      pat.setDateNaissance(cal.getTime());
      patientManager.createOrUpdateObjectManager(pat, null, null, null, null, null, null, null, u, "creation", null, false);
      assertTrue(patientManager.findAllObjectsManager().size() == 6);
      final Importation i1 = new Importation();
      i1.setImportHistorique(ih1);
      i1.setEntite(e1);
      i1.setObjetId(pat.getPatientId());
      importations.add(i1);

      // Prelevement
      final Prelevement prlvt = new Prelevement();
      prlvt.setCode("TMP");
      prelevementManager.createObjectManager(prlvt, banque, nature, null, consentType, null, null, null, null, null, null, null,
         null, null, null, null, u, false, null, false);
      assertTrue(prelevementManager.findAllObjectsManager().size() == 6);
      final Importation i2 = new Importation();
      i2.setImportHistorique(ih1);
      i2.setEntite(e2);
      i2.setObjetId(prlvt.getPrelevementId());
      importations.add(i2);

      // Echantillon
      final Echantillon echan = new Echantillon();
      echan.setCode("RRRRRR");
      echantillonManager.createObjectManager(echan, banque, null, null, objetStatutDao.findById(4), null, eType, null, null, null,
         null, null, null, null, u, false, null, false);
      assertTrue(echantillonManager.findAllObjectsManager().size() == 5);
      final Importation i3 = new Importation();
      i3.setImportHistorique(ih1);
      i3.setEntite(e3);
      i3.setObjetId(echan.getEchantillonId());
      importations.add(i3);

      // ProdDerive
      final ProdDerive derive = new ProdDerive();
      derive.setCode("jhosbj");
      prodDeriveManager.createObjectManager(derive, banque, pType, objetStatutDao.findById(4), null, null, null, null, null, null,
         null, null, null, null, null, u, false, null, false);
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == 5);
      final Importation i4 = new Importation();
      i4.setImportHistorique(ih1);
      i4.setEntite(e8);
      i4.setObjetId(derive.getProdDeriveId());
      importations.add(i4);

      // Historique
      importHistoriqueManager.createObjectManager(ih1, it1, u, importations);
      assertTrue(importationDao.findAll().size() == 6);

      // Suppression
      patientManager.removeObjectManager(pat, null, u, null);
      prelevementManager.removeObjectManager(prlvt, null, u, null);
      echantillonManager.removeObjectManager(echan, null, u, null);
      prodDeriveManager.removeObjectManager(derive, null, u, null);

      assertTrue(patientManager.findAllObjectsManager().size() == 5);
      assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
      assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
      assertTrue(importationDao.findAll().size() == 2);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(pat);
      fs.add(prlvt);
      fs.add(echan);
      fs.add(derive);
      cleanUpFantomes(fs);

      importHistoriqueManager.removeObjectManager(ih1);
      assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 3);
   }

   public void testFindPrelevementByImportHistoriqueManager(){
      final ImportHistorique ih1 = importHistoriqueManager.findByIdManager(1);
      final List<Prelevement> prels = new ArrayList<>();
      prels.addAll(importHistoriqueManager.findPrelevementByImportHistoriqueManager(ih1));
      assertTrue(prels.size() == 1);
      assertTrue(prels.get(0).getPrelevementId() == 4);

      prels.clear();

      prels.addAll(importHistoriqueManager.findPrelevementByImportHistoriqueManager(importHistoriqueManager.findByIdManager(2)));
      assertTrue(prels.isEmpty());

      prels.addAll(importHistoriqueManager.findPrelevementByImportHistoriqueManager(null));
      assertTrue(prels.isEmpty());

   }
}
