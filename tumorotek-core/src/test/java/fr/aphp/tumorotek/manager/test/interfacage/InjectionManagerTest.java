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
package fr.aphp.tumorotek.manager.test.interfacage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.interfacage.BlocExterneDao;
import fr.aphp.tumorotek.dao.interfacage.DossierExterneDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.manager.interfacage.InjectionManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.stockage.Emplacement;

public class InjectionManagerTest extends AbstractManagerTest4
{

   @Autowired
   private InjectionManager injectionManager;
   @Autowired
   private ChampEntiteDao champEntiteDao;
   @Autowired
   private NatureDao natureDao;
   @Autowired
   private EmplacementManager emplacementManager;
   @Autowired
   private ChampAnnotationManager champAnnotationManager;
   @Autowired
   private ItemDao itemDao;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private BlocExterneDao blocExterneDao;
   @Autowired
   private DossierExterneDao dossierExterneDao;

   private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

   public InjectionManagerTest(){}

   //	@Override
   //	protected String[] getConfigLocations() {
   //        return new String[]{ "applicationContextManagerBase.xml",
   //       		 "applicationContextDaoBase.xml" };
   //    }

   @Test
   public void testSetPropertyValueForObject(){
      final Prelevement prlvt = new Prelevement();

      // test sur un string
      final ImportColonne col = new ImportColonne();
      col.setNom("TEST");
      ChampEntite ce = champEntiteDao.findById(23);
      injectionManager.setPropertyValueForObject("CODE", ce, prlvt);
      assertTrue(prlvt.getCode().equals("CODE"));

      // test sur un integer
      ce = champEntiteDao.findById(34);
      injectionManager.setPropertyValueForObject("5", ce, prlvt);
      assertTrue(prlvt.getConditNbr() == 5);
      injectionManager.setPropertyValueForObject("tcg", ce, prlvt);
      assertNull(prlvt.getConditNbr());

      // test sur un float
      ce = champEntiteDao.findById(40);
      injectionManager.setPropertyValueForObject("5.5", ce, prlvt);
      assertTrue(prlvt.getQuantite() == 5.5);
      injectionManager.setPropertyValueForObject("tcg", ce, prlvt);
      assertNull(prlvt.getQuantite());

      // test sur un boolean
      ce = champEntiteDao.findById(47);
      injectionManager.setPropertyValueForObject("1", ce, prlvt);
      assertTrue(prlvt.getSterile());
      injectionManager.setPropertyValueForObject("tcg", ce, prlvt);
      assertNull(prlvt.getSterile());

      // test sur une date
      ce = champEntiteDao.findById(27);
      injectionManager.setPropertyValueForObject("20051110123517", ce, prlvt);
      assertTrue(format.format(prlvt.getConsentDate()).equals("2005-11-10"));
      injectionManager.setPropertyValueForObject("tcg", ce, prlvt);
      assertNull(prlvt.getConsentDate());

      // test avec une date sans heures
      ce = champEntiteDao.findById(27);
      injectionManager.setPropertyValueForObject("20051110", ce, prlvt);
      assertTrue(format.format(prlvt.getConsentDate()).equals("2005-11-10"));
      injectionManager.setPropertyValueForObject("tcg", ce, prlvt);
      assertNull(prlvt.getConsentDate());

      // test avec une date avec heures mais sans secondes
      ce = champEntiteDao.findById(27);
      injectionManager.setPropertyValueForObject("200511101230", ce, prlvt);
      assertTrue(format.format(prlvt.getConsentDate()).equals("2005-11-10"));
      injectionManager.setPropertyValueForObject("tcg", ce, prlvt);
      assertNull(prlvt.getConsentDate());

      // test sur un calendar
      ce = champEntiteDao.findById(30);
      injectionManager.setPropertyValueForObject("20051110123517", ce, prlvt);
      assertTrue(format.format(prlvt.getDatePrelevement().getTime()).equals("2005-11-10"));
      injectionManager.setPropertyValueForObject("tcg", ce, prlvt);
      assertNull(prlvt.getDatePrelevement());

      // test sur un calendar sans heures
      ce = champEntiteDao.findById(30);
      injectionManager.setPropertyValueForObject("20051110", ce, prlvt);
      assertTrue(format.format(prlvt.getDatePrelevement().getTime()).equals("2005-11-10"));
      injectionManager.setPropertyValueForObject("tcg", ce, prlvt);
      assertNull(prlvt.getDatePrelevement());

      // test sur un calendar avec heures mais sans secondes
      ce = champEntiteDao.findById(30);
      injectionManager.setPropertyValueForObject("200511101543", ce, prlvt);
      assertTrue(format.format(prlvt.getDatePrelevement().getTime()).equals("2005-11-10"));
      injectionManager.setPropertyValueForObject("tcg", ce, prlvt);
      assertNull(prlvt.getDatePrelevement());

      // test avec un thesaurus
      final Nature n = natureDao.findById(1);
      ce = champEntiteDao.findById(24);
      injectionManager.setPropertyValueForObject(n, ce, prlvt);
      assertTrue(prlvt.getNature().equals(n));

      // test avec un emplacement
      final Emplacement empl = emplacementManager.findByIdManager(1);
      final Echantillon e = new Echantillon();
      ce = champEntiteDao.findById(57);
      injectionManager.setPropertyValueForObject(empl, ce, e);
      assertNotNull(e.getEmplacement());

      // test avec un null
      ce = champEntiteDao.findById(23);
      injectionManager.setPropertyValueForObject(null, ce, prlvt);
      assertNull(prlvt.getCode());
   }

   /**
    * Test de la méthode setPropertyValueForAnnotationValeur().
    */
   @Test
   public void testSetPropertyValueForAnnotationValeur(){
      final Banque banque = banqueDao.findById(1);
      final ImportColonne col = new ImportColonne();
      col.setNom("TEST");
      AnnotationValeur av = new AnnotationValeur();
      // test sur un string
      ChampAnnotation ca = champAnnotationManager.findByNomManager("Alphanum1").get(0);
      av = injectionManager.setPropertyValueForAnnotationValeur("CODE", ca, banque);
      assertTrue(av.getAlphanum().equals("CODE"));
      assertTrue(av.getChampAnnotation().equals(ca));
      assertTrue(av.getBanque().equals(banque));

      // test sur un integer
      av = new AnnotationValeur();
      ca = champAnnotationManager.findByNomManager("Num1").get(0);
      av = injectionManager.setPropertyValueForAnnotationValeur("50.5", ca, banque);
      assertTrue(av.getAlphanum().equals("50.5"));
      assertTrue(av.getChampAnnotation().equals(ca));
      assertTrue(av.getBanque().equals(banque));

      // test sur un boolean
      av = new AnnotationValeur();
      ca = champAnnotationManager.findByNomManager("Bool1").get(0);
      av = injectionManager.setPropertyValueForAnnotationValeur("1", ca, banque);
      assertTrue(av.getBool());
      av = injectionManager.setPropertyValueForAnnotationValeur("sfd", ca, banque);
      assertNull(av);

      // test sur un calendar
      av = new AnnotationValeur();
      ca = champAnnotationManager.findByNomManager("Date1").get(0);
      av = injectionManager.setPropertyValueForAnnotationValeur("20051110123517", ca, banque);
      assertTrue(format.format(av.getDate().getTime()).equals("2005-11-10"));
      av = injectionManager.setPropertyValueForAnnotationValeur("qsc", ca, banque);
      assertNull(av);

      // test sur un calendar sans heures
      av = new AnnotationValeur();
      ca = champAnnotationManager.findByNomManager("Date1").get(0);
      av = injectionManager.setPropertyValueForAnnotationValeur("20051110", ca, banque);
      assertTrue(format.format(av.getDate().getTime()).equals("2005-11-10"));
      av = injectionManager.setPropertyValueForAnnotationValeur("qsc", ca, banque);
      assertNull(av);

      // test avec un text
      av = new AnnotationValeur();
      ca = champAnnotationManager.findByNomManager("Texte1").get(0);
      av = injectionManager.setPropertyValueForAnnotationValeur("hhdhdhd", ca, banque);
      assertTrue(av.getTexte().equals("hhdhdhd"));

      // test avec un hyperlien
      av = new AnnotationValeur();
      ca = champAnnotationManager.findByNomManager("Link1").get(0);
      av = injectionManager.setPropertyValueForAnnotationValeur("www.google.fr", ca, banque);
      assertTrue(av.getAlphanum().equals("www.google.fr"));

      // test avec un thesaurus
      final Item it = itemDao.findById(12);
      av = new AnnotationValeur();
      ca = champAnnotationManager.findByNomManager("009 : version cTNM").get(0);
      av = injectionManager.setPropertyValueForAnnotationValeur(it, ca, banque);
      assertTrue(av.getItem().equals(it));
   }

   /**
    * Test de la méthode extractValueForOneThesaurus().
    */
   @Test
   public void testExtractValueForOneThesaurus(){
      final Banque banque = banqueDao.findById(1);

      // Thésaurus Nature
      final ChampEntite ce111 = champEntiteDao.findById(111);
      Object obj = injectionManager.extractValueForOneThesaurus(ce111, banque, "TISSU");
      assertNotNull(obj);
      assertTrue(((Nature) obj).getNature().equals("TISSU"));
      obj = injectionManager.extractValueForOneThesaurus(ce111, banque, "XXX");
      assertNull(obj);

      // Thésaurus Collaborateur
      final ChampEntite ce199 = champEntiteDao.findById(199);
      obj = injectionManager.extractValueForOneThesaurus(ce199, banque, 2);
      assertNotNull(obj);
      assertTrue(((Collaborateur) obj).getNom().equals("DUFAY"));
      obj = injectionManager.extractValueForOneThesaurus(ce199, banque, 50);
      assertNull(obj);

      // Mauvais paramétrage
      obj = injectionManager.extractValueForOneThesaurus(null, banque, "DUFAY");
      assertNull(obj);
      obj = injectionManager.extractValueForOneThesaurus(ce199, null, 1);
      assertNull(obj);
      obj = injectionManager.extractValueForOneThesaurus(ce199, banque, null);
      assertNull(obj);
   }

   /**
    * Test de la méthode extractValueForOneAnnotationThesaurus().
    */
   @Test
   public void testExtractValueForOneAnnotationThesaurus(){
      final Banque banque = banqueDao.findById(1);

      // Thésaurus
      final ChampAnnotation ce11 = champAnnotationManager.findByNomManager("Thes1").get(0);
      Object obj = injectionManager.extractValueForOneAnnotationThesaurus(ce11, banque, "item1-1");
      assertNotNull(obj);
      assertTrue(((Item) obj).getLabel().equals("item1-1"));
      obj = injectionManager.extractValueForOneAnnotationThesaurus(ce11, banque, "XXX");
      assertNull(obj);

      // Thésaurus
      final ChampAnnotation ce28 = champAnnotationManager.findByNomManager("010 : Taille de la tumeur : cT").get(0);
      obj = injectionManager.extractValueForOneAnnotationThesaurus(ce28, banque, "X");
      assertNotNull(obj);
      assertTrue(((Item) obj).getLabel().equals("X"));
      obj = injectionManager.extractValueForOneAnnotationThesaurus(ce28, banque, "GSHT");
      assertNull(obj);

      // Mauvais paramétrage
      obj = injectionManager.extractValueForOneAnnotationThesaurus(null, banque, "X");
      assertNull(obj);
      obj = injectionManager.extractValueForOneAnnotationThesaurus(ce28, null, "X");
      assertNull(obj);
      obj = injectionManager.extractValueForOneAnnotationThesaurus(ce28, banque, null);
      assertNull(obj);
   }

   /**
    * Test de la méthode injectValeurExterneInObject().
    */
   @Test
   public void testInjectValeurExterneInObject(){
      final Banque banque = banqueDao.findById(1);
      final Prelevement prlvt = new Prelevement();

      // Injection d'un string valide
      ValeurExterne ve = new ValeurExterne();
      ve.setValeur("CODE");
      ve.setChampEntiteId(23);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertTrue(prlvt.getCode().equals("CODE"));

      // Injection d'une date valide
      ve = new ValeurExterne();
      ve.setValeur("20051110123517");
      ve.setChampEntiteId(30);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertTrue(format.format(prlvt.getDatePrelevement().getTime()).equals("2005-11-10"));
      ve = new ValeurExterne();
      ve.setValeur("20051110123517");
      ve.setChampEntiteId(27);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertTrue(format.format(prlvt.getConsentDate()).equals("2005-11-10"));

      // Injection d'un boolean valide
      ve = new ValeurExterne();
      ve.setValeur("1");
      ve.setChampEntiteId(47);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertTrue(prlvt.getSterile());

      // Injection d'un int valide
      ve = new ValeurExterne();
      ve.setValeur("10");
      ve.setChampEntiteId(34);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertTrue(prlvt.getConditNbr().equals(10));

      // Injection d'un float valide
      ve = new ValeurExterne();
      ve.setValeur("10.5");
      ve.setChampEntiteId(40);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertTrue(prlvt.getQuantite().equals((float) 10.5));

      // Injection d'un thésaurus valide
      ve = new ValeurExterne();
      ve.setValeur("TISSU");
      ve.setChampEntiteId(24);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertTrue(prlvt.getNature().getNature().equals("TISSU"));

      // Injection d'une valeur vide ou null
      ve = new ValeurExterne();
      ve.setValeur("");
      ve.setChampEntiteId(23);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertNull(prlvt.getCode());
      ve = new ValeurExterne();
      ve.setValeur(null);
      ve.setChampEntiteId(23);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertNull(prlvt.getCode());

      // Injection d'un integer non valide
      ve = new ValeurExterne();
      ve.setValeur("qscqsc");
      ve.setChampEntiteId(34);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertNull(prlvt.getConditNbr());

      // Injection d'une date non valide
      ve = new ValeurExterne();
      ve.setValeur("10 Novembre 2011 12h37:84");
      ve.setChampEntiteId(30);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertNull(prlvt.getDatePrelevement());

      // Injection d'un thésaurus non valide
      ve = new ValeurExterne();
      ve.setValeur("BLABLA");
      ve.setChampEntiteId(24);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      assertNull(prlvt.getNature());

      // Injection dans un mauvais champentité
      ve = new ValeurExterne();
      ve.setValeur("BLABLA");
      ve.setChampEntiteId(67);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
      ve = new ValeurExterne();
      ve.setValeur("BLABLA");
      ve.setChampEntiteId(5000);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, null);
   }

   /**
    * Test de la méthode injectValeurExterneInObject().
    */
   @Test
   public void testInjectValeurExterneInAnnotationObject(){
      final Banque banque = banqueDao.findById(1);
      final Prelevement prlvt = new Prelevement();
      final Patient pat = new Patient();
      final Echantillon echan = new Echantillon();
      List<AnnotationValeur> annos = new ArrayList<>();

      // Injection d'un string valide
      ValeurExterne ve = new ValeurExterne();
      ve.setValeur("CODE");
      ve.setChampAnnotationId(1);
      injectionManager.injectValeurExterneInObject(pat, banque, ve, annos);
      assertTrue(annos.size() == 1);
      assertTrue(annos.get(0).getAlphanum().equals("CODE"));
      assertTrue(annos.get(0).getChampAnnotation().getId() == 1);
      assertTrue(annos.get(0).getBanque().equals(banque));

      // Injection d'une date valide
      annos = new ArrayList<>();
      ve = new ValeurExterne();
      ve.setValeur("20051110123517");
      ve.setChampAnnotationId(5);
      injectionManager.injectValeurExterneInObject(echan, banque, ve, annos);
      assertTrue(annos.size() == 1);
      assertTrue(format.format(annos.get(0).getDate().getTime()).equals("2005-11-10"));
      assertTrue(annos.get(0).getChampAnnotation().getId() == 5);
      assertTrue(annos.get(0).getBanque().equals(banque));

      // Injection d'un boolean valide
      annos = new ArrayList<>();
      ve = new ValeurExterne();
      ve.setValeur("1");
      ve.setChampAnnotationId(3);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, annos);
      assertTrue(annos.size() == 1);
      assertTrue(annos.get(0).getBool());
      assertTrue(annos.get(0).getChampAnnotation().getId() == 3);
      assertTrue(annos.get(0).getBanque().equals(banque));

      // Injection d'un int valide
      annos = new ArrayList<>();
      ve = new ValeurExterne();
      ve.setValeur("10.5");
      ve.setChampAnnotationId(7);
      injectionManager.injectValeurExterneInObject(echan, banque, ve, annos);
      assertTrue(annos.size() == 1);
      assertTrue(annos.get(0).getAlphanum().equals("10.5"));
      assertTrue(annos.get(0).getChampAnnotation().getId() == 7);
      assertTrue(annos.get(0).getBanque().equals(banque));

      // Injection d'un item valide
      annos = new ArrayList<>();
      ve = new ValeurExterne();
      ve.setValeur("item1-1");
      ve.setChampAnnotationId(11);
      injectionManager.injectValeurExterneInObject(echan, banque, ve, annos);
      assertTrue(annos.size() == 1);
      assertTrue(annos.get(0).getItem().getLabel().equals("item1-1"));
      assertTrue(annos.get(0).getChampAnnotation().getId() == 11);
      assertTrue(annos.get(0).getBanque().equals(banque));

      // Injection d'une valeur vide ou null
      annos = new ArrayList<>();
      ve = new ValeurExterne();
      ve.setValeur("");
      ve.setChampAnnotationId(1);
      injectionManager.injectValeurExterneInObject(pat, banque, ve, annos);
      assertTrue(annos.size() == 0);
      annos = new ArrayList<>();
      ve = new ValeurExterne();
      ve.setValeur(null);
      ve.setChampAnnotationId(1);
      injectionManager.injectValeurExterneInObject(pat, banque, ve, annos);
      assertTrue(annos.size() == 0);

      // Injection d'une date non valide
      annos = new ArrayList<>();
      ve = new ValeurExterne();
      ve.setValeur("10 Novembre 2011 12h37:84");
      ve.setChampAnnotationId(5);
      injectionManager.injectValeurExterneInObject(echan, banque, ve, annos);
      assertTrue(annos.size() == 0);

      // Injection d'un thésaurus non valide
      annos = new ArrayList<>();
      ve = new ValeurExterne();
      ve.setValeur("BLABLA");
      ve.setChampAnnotationId(11);
      injectionManager.injectValeurExterneInObject(echan, banque, ve, annos);
      assertTrue(annos.size() == 0);

      // Injection dans une mauvaise annotation
      annos = new ArrayList<>();
      ve = new ValeurExterne();
      ve.setValeur("BLABLA");
      ve.setChampAnnotationId(5000);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, annos);
      assertTrue(annos.size() == 0);
      annos = new ArrayList<>();
      ve = new ValeurExterne();
      ve.setValeur("BLABLA");
      ve.setChampAnnotationId(1);
      injectionManager.injectValeurExterneInObject(prlvt, banque, ve, annos);
      assertTrue(annos.size() == 0);
   }

   /**
    * Test de la méthdoe injectBlocExterneInObject().
   */
   @Test
   public void testInjectBlocExterneInObject(){
      final Banque banque = banqueDao.findById(1);
      List<AnnotationValeur> annoValeurs = new ArrayList<>();

      final BlocExterne b1 = blocExterneDao.findById(1);
      final Patient patient = new Patient();
      injectionManager.injectBlocExterneInObject(patient, banque, b1, annoValeurs);
      assertTrue(patient.getNom().equals("HOUSE"));
      assertTrue(patient.getPrenom().equals("GREGORY"));
      assertTrue(patient.getSexe().equals("M"));
      assertTrue(annoValeurs.size() == 1);
      assertTrue(annoValeurs.get(0).getAlphanum().equals("ANNO"));

      annoValeurs = new ArrayList<>();
      final BlocExterne b2 = blocExterneDao.findById(2);
      final Prelevement prlvt = new Prelevement();
      injectionManager.injectBlocExterneInObject(prlvt, banque, b2, annoValeurs);
      assertTrue(prlvt.getCode().equals("PRLVT_TEST"));
      assertTrue(prlvt.getNature().getNature().equals("SANG"));
      assertTrue(format.format(prlvt.getDatePrelevement().getTime()).equals("2011-08-16"));
      assertTrue(annoValeurs.size() == 0);
   }

   /**
    * Test de la méthode injectDossierManager().
    */
   @Test
   public void testInjectDossierManager(){
      final DossierExterne dossier = dossierExterneDao.findById(1);
      final Banque b1 = banqueDao.findById(1);
      ResultatInjection resultat = injectionManager.injectDossierManager(dossier, b1);
      assertNotNull(resultat);

      resultat = injectionManager.injectDossierManager(null, b1);
      assertNull(resultat);

      resultat = injectionManager.injectDossierManager(dossier, null);
      assertNull(resultat);

      resultat = injectionManager.injectDossierManager(null, null);
      assertNull(resultat);

      // echantillon
      resultat = injectionManager.injectDossierManager(dossierExterneDao.findById(4), b1);
      assertNotNull(resultat);
      assertTrue(resultat.getEchantillon() != null);
      assertTrue(resultat.getAnnosEchantillon().size() == 4);
      assertTrue(resultat.getCodesOrgane().size() == 3);
      assertTrue(resultat.getCodesOrgane().get(0).getCode().equals("AC!AP"));
      assertTrue(resultat.getCodesOrgane().get(0).getLibelle() == null);
      assertTrue(resultat.getCodesOrgane().get(1).getCode().equals("CC!CI"));
      assertTrue(resultat.getCodesOrgane().get(1).getLibelle() == null);
      assertTrue(resultat.getCodesOrgane().get(2).getCode().equals("PP-PLACENTA"));
      assertTrue(resultat.getCodesOrgane().get(2).getLibelle() == null);
      assertTrue(resultat.getCodesMorpho().size() == 2);
      assertTrue(resultat.getCodesMorpho().get(0).getCode().equals("GHLOJ7F4"));
      assertTrue(resultat.getCodesMorpho().get(0).getLibelle() == null);
      assertTrue(resultat.getCodesMorpho().get(1).getCode().equals("GHOTJMF4"));
      assertTrue(resultat.getCodesMorpho().get(1).getLibelle() == null);
      assertTrue(resultat.getCrAnapath() == null);
      assertTrue(resultat.getStream() == null);

      // @since 2.1 query view -> no ids
      final ValeurExterne nomVal = new ValeurExterne();
      nomVal.setValeur("PAT1");
      nomVal.setChampEntiteId(3);

      final ValeurExterne natPrel = new ValeurExterne();
      natPrel.setValeur("SANG");
      natPrel.setChampEntiteId(24);

      final ValeurExterne cOrgs = new ValeurExterne();
      cOrgs.setValeur("YZ~XZ~BZ");
      cOrgs.setChampEntiteId(229);

      final DossierExterne dExt = new DossierExterne();
      final BlocExterne bPat = new BlocExterne();
      bPat.setEntiteId(1);
      bPat.setOrdre(1);
      dExt.getBlocExternes().add(bPat);
      bPat.getValeurs().add(nomVal);

      final BlocExterne bPrel = new BlocExterne();
      bPrel.setEntiteId(2);
      bPrel.setOrdre(2);
      dExt.getBlocExternes().add(bPrel);
      bPrel.getValeurs().add(natPrel);

      final BlocExterne bEchan = new BlocExterne();
      bEchan.setEntiteId(3);
      bEchan.setOrdre(3);
      dExt.getBlocExternes().add(bEchan);
      bEchan.getValeurs().add(cOrgs);

      resultat = injectionManager.injectDossierManager(dExt, b1);
      assertNotNull(resultat);
      assertTrue(resultat.getPrelevement().getMaladie().getPatient().getNom().equals("PAT1"));
      assertTrue(resultat.getPrelevement().getNature().getNature().equals("SANG"));
      assertTrue(resultat.getCodesOrgane().size() == 3);

      // tesi interface 
   }
}
