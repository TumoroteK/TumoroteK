/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2er011)
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.interfacage.BlocExterneDao;
import fr.aphp.tumorotek.dao.interfacage.EmetteurDao;
import fr.aphp.tumorotek.dao.interfacage.ValeurExterneDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.manager.interfacage.DossierExterneManager;
import fr.aphp.tumorotek.manager.interfacage.InjectionManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

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
	private DossierExterneManager dossierExterneManager;
	@Autowired
	private ValeurExterneDao valeurExterneDao;
	@Autowired
	private EmetteurDao emetteurDao;
	@Autowired
	private UtilisateurDao utilisateurDao;
	@Autowired
	private PrelevementManager prelevementManager;
	@Autowired
	private PatientManager patientManager;
	@Autowired
	private PlateformeDao plateformeDao;
	@Autowired
	private TransformationManager transformationManager;
	@Autowired
	private ProdDeriveManager prodDeriveManager;
	@Autowired
	private OperationTypeDao operationTypeDao;
	

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
		assertTrue(((Nature) obj).getNom().equals("TISSU"));
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
		assertTrue(prlvt.getNature().getNom().equals("TISSU"));

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
		assertTrue(prlvt.getNature().getNom().equals("SANG"));
		assertTrue(format.format(prlvt.getDatePrelevement().getTime()).equals("2011-08-16"));
		assertTrue(annoValeurs.size() == 0);
	}

	/**
	 * Test de la méthode injectDossierManager().
	 */
	@Test
	public void testInjectDossierManager(){
		final DossierExterne dossier = dossierExterneManager.findByIdManager(1);
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
		resultat = injectionManager.injectDossierManager(dossierExterneManager.findByIdManager(4), b1);
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
		assertTrue(resultat.getPrelevement().getNature().getNom().equals("SANG"));
		assertTrue(resultat.getCodesOrgane().size() == 3);

		// test 2.2.2-diamic evolution
		// @since 2.1 query view -> no ids
		ValeurExterne codeE = new ValeurExterne();
		codeE.setValeur("CODE1");
		codeE.setChampEntiteId(54);
		ValeurExterne typeE = new ValeurExterne();
		typeE.setValeur("CELLULES");
		typeE.setChampEntiteId(58);
		ValeurExterne dateStock = new ValeurExterne();
		dateStock.setValeur("201602021400");
		dateStock.setChampEntiteId(56);
		ValeurExterne empl = new ValeurExterne();
		empl.setValeur("C1.R1.T1.BT1.C-5");
		empl.setChampEntiteId(57);
		ValeurExterne codeE2 = new ValeurExterne();
		codeE2.setValeur("CODE2");
		codeE2.setChampEntiteId(54);
		ValeurExterne typeE2 = new ValeurExterne();
		typeE2.setValeur("CULOT SEC");
		typeE2.setChampEntiteId(58);
		ValeurExterne dateStock2 = new ValeurExterne();
		dateStock2.setValeur("201602021400");
		dateStock2.setChampEntiteId(56);

		final DossierExterne dExtDiamic = new DossierExterne();
		dExtDiamic.getBlocExternes().add(bEchan);

		final BlocExterne bEchanDiamic = new BlocExterne();
		bEchanDiamic.setEntiteId(3);
		bEchanDiamic.setOrdre(4);
		dExtDiamic.getBlocExternes().add(bEchanDiamic);
		bEchanDiamic.getValeurs().add(codeE);
		bEchanDiamic.getValeurs().add(typeE);
		bEchanDiamic.getValeurs().add(dateStock);
		bEchanDiamic.getValeurs().add(empl);

		final BlocExterne bEchanDiamic2 = new BlocExterne();
		bEchanDiamic2.setEntiteId(3);
		bEchanDiamic2.setOrdre(5);
		dExtDiamic.getBlocExternes().add(bEchanDiamic2);
		bEchanDiamic2.getValeurs().add(codeE2);
		bEchanDiamic2.getValeurs().add(typeE2);
		bEchanDiamic2.getValeurs().add(dateStock2);

		resultat = injectionManager.injectDossierManager(dExtDiamic, b1);
		assertNotNull(resultat);
		assertTrue(resultat.getCodesOrgane().size() == 3);
		assertTrue(resultat.getEchanAdrls().size() == 2);
		Echantillon e1 = new Echantillon();
		e1.setBanque(b1); e1.setCode("CODE1");
		List<Echantillon> keys = new ArrayList<Echantillon>(resultat.getEchanAdrls().keySet());
		assertTrue(keys.get(keys.indexOf(e1)).getEchantillonType().getId() == 1);
		assertTrue(keys.get(keys.indexOf(e1)).getDateStock() != null);
		assertTrue(resultat.getEchanAdrls().get(e1).equals("C1.R1.T1.BT1.C-5"));
		Echantillon e2 = new Echantillon();
		e2.setBanque(b1); e2.setCode("CODE2");
		keys = new ArrayList<Echantillon>(resultat.getEchanAdrls().keySet());
		assertTrue(keys.get(keys.indexOf(e2)).getEchantillonType().getId() == 3);
		assertTrue(keys.get(keys.indexOf(e2)).getDateStock() != null);
		assertTrue(resultat.getEchanAdrls().get(e2) == null);
	}

	@Test
	public void testDaveDossierAndChildrenManager() throws ParseException {

		final Banque b1 = banqueDao.findById(1);
		final Utilisateur u1 = utilisateurDao.findById(1);
		final Emetteur e1 = emetteurDao.findById(1);

		final Integer nbD = dossierExterneManager.findAllObjectsManager().size();
		final Integer nbB = blocExterneDao.findAll().size();
		final Integer nbV = valeurExterneDao.findAll().size();

		// ATTENTION PAS TRANSMIS PAR GENNO 
		// maladie LIBELLE
		// prelevement CONSENT

		// maladie
		// final ValeurExterne libMal = new ValeurExterne();
		// libMal.setValeur("NEWMAL");
		// libMal.setChampEntiteId(17)

		// creation des dossiers 
		// - NEW_PREL_PARENT_001 JEAN PAT 1
		// -- NEW_DERIVE_001 ADN CONC
		// -- NEW_DERIVE_002 ARN
		DossierExterne parent = new DossierExterne();
		parent.setIdentificationDossier("NEW_PREL_PARENT_001");
		parent.setDateOperation(Calendar.getInstance());
		parent.setOperation("D");

		List<BlocExterne> blocsParent = new ArrayList<BlocExterne>();
		Hashtable<BlocExterne, List<ValeurExterne>> valeursParentMap = new Hashtable<BlocExterne, List<ValeurExterne>>();

		// patient
		final ValeurExterne nomVal = new ValeurExterne();
		nomVal.setValeur("NEW_PAT1_001");
		nomVal.setChampEntiteId(3);
		final ValeurExterne prenomVal = new ValeurExterne();
		prenomVal.setValeur("JEAN");
		prenomVal.setChampEntiteId(5);
		final ValeurExterne sexeVal = new ValeurExterne();
		sexeVal.setValeur("M");
		sexeVal.setChampEntiteId(6);
		final ValeurExterne dateNVal = new ValeurExterne();
		dateNVal.setValeur("19540604");
		dateNVal.setChampEntiteId(7);
		List<ValeurExterne> valeursPatientParent = new ArrayList<ValeurExterne>();
		valeursPatientParent.add(nomVal); valeursPatientParent.add(prenomVal);
		valeursPatientParent.add(sexeVal); valeursPatientParent.add(dateNVal);

		final BlocExterne bPat = new BlocExterne();
		bPat.setEntiteId(1);
		bPat.setOrdre(1);
		blocsParent.add(bPat);	   
		valeursParentMap.put(bPat, valeursPatientParent);

		final ValeurExterne codePrel = new ValeurExterne();
		codePrel.setValeur("NEW_PREL_PARENT_001");
		codePrel.setChampEntiteId(23);   
		final ValeurExterne natPrel = new ValeurExterne();
		natPrel.setValeur("TISSU");
		natPrel.setChampEntiteId(24);
		final ValeurExterne consentPrel = new ValeurExterne();
		consentPrel.setValeur("EN ATTENTE");
		consentPrel.setChampEntiteId(26);
		List<ValeurExterne> valeursPrelParent = new ArrayList<ValeurExterne>();
		valeursPrelParent.add(codePrel); valeursPrelParent.add(natPrel);
		valeursPrelParent.add(consentPrel);

		final BlocExterne bPrel = new BlocExterne();
		bPrel.setEntiteId(2);
		bPrel.setOrdre(2);
		blocsParent.add(bPrel);	   
		valeursParentMap.put(bPrel, valeursPrelParent);

		dossierExterneManager.createObjectManager(parent, e1, blocsParent, valeursParentMap, 2000);

		// dossier dérivé 1
		DossierExterne der1 = new DossierExterne();
		der1.setIdentificationDossier("NEW_DERIVE_001");
		der1.setDateOperation(Calendar.getInstance());
		der1.setOperation("D");
		der1.setEntiteId(8);

		List<BlocExterne> blocsDer1 = new ArrayList<BlocExterne>();
		Hashtable<BlocExterne, List<ValeurExterne>> valeursDer1Map = new Hashtable<BlocExterne, List<ValeurExterne>>();

		final ValeurExterne codePrelDer1 = new ValeurExterne();
		codePrelDer1.setValeur("NEW_PREL_PARENT_001");
		codePrelDer1.setChampEntiteId(23);
		List<ValeurExterne> valeursDer1Prel = new ArrayList<ValeurExterne>();
		valeursDer1Prel.add(codePrelDer1);

		final BlocExterne bPrelDer1 = new BlocExterne();
		bPrelDer1.setEntiteId(2);
		bPrelDer1.setOrdre(1);
		blocsDer1.add(bPrelDer1);	   
		valeursDer1Map.put(bPrelDer1, valeursDer1Prel);

		final ValeurExterne codeDer1 = new ValeurExterne();
		codeDer1.setValeur("NEW_DERIVE_001");
		codeDer1.setChampEntiteId(79);
		final ValeurExterne typeDer1 = new ValeurExterne();
		typeDer1.setValeur("ADN");
		typeDer1.setChampEntiteId(78);
		final ValeurExterne concDer1 = new ValeurExterne();
		concDer1.setValeur("112");
		concDer1.setChampEntiteId(85);
		final ValeurExterne concUDer1 = new ValeurExterne();
		concUDer1.setValeur("ng/µl");
		concUDer1.setChampEntiteId(89);
		List<ValeurExterne> valeursDer1 = new ArrayList<ValeurExterne>();
		valeursDer1.add(codeDer1); valeursDer1.add(typeDer1);
		valeursDer1.add(concDer1); valeursDer1.add(concUDer1);

		final BlocExterne bDer1 = new BlocExterne();
		bDer1.setEntiteId(8);
		bDer1.setOrdre(2);
		blocsDer1.add(bDer1);	   
		valeursDer1Map.put(bDer1, valeursDer1);

		dossierExterneManager.createObjectManager(der1, e1, blocsDer1, valeursDer1Map, 2000);	

		// dossier dérivé 2
		DossierExterne der2 = new DossierExterne();
		der2.setIdentificationDossier("NEW_DERIVE_002");
		der2.setDateOperation(Calendar.getInstance());
		der2.setEmetteur(emetteurDao.findById(1));
		der2.setOperation("D");
		der2.setEntiteId(8);

		List<BlocExterne> blocsDer2 = new ArrayList<BlocExterne>();
		Hashtable<BlocExterne, List<ValeurExterne>> valeursDer2Map = new Hashtable<BlocExterne, List<ValeurExterne>>();

		final ValeurExterne codePrelDer2 = new ValeurExterne();
		codePrelDer2.setValeur("NEW_PREL_PARENT_001");
		codePrelDer2.setChampEntiteId(23);
		List<ValeurExterne> valeursDer2Prel = new ArrayList<ValeurExterne>();
		valeursDer2Prel.add(codePrelDer2);

		final BlocExterne bPrelDer2 = new BlocExterne();
		bPrelDer2.setEntiteId(2);
		bPrelDer2.setOrdre(1);
		blocsDer2.add(bPrelDer2);	   
		valeursDer2Map.put(bPrelDer2, valeursDer2Prel);

		final ValeurExterne codeDer2 = new ValeurExterne();
		codeDer2.setValeur("NEW_DERIVE_002");
		codeDer2.setChampEntiteId(79);
		final ValeurExterne typeDer2 = new ValeurExterne();
		typeDer2.setValeur("ARN");
		typeDer2.setChampEntiteId(78);
		List<ValeurExterne> valeursDer2 = new ArrayList<ValeurExterne>();
		valeursDer2.add(codeDer2); valeursDer2.add(typeDer2);

		final BlocExterne bDer2 = new BlocExterne();
		bDer2.setEntiteId(8);
		bDer2.setOrdre(2);
		blocsDer2.add(bDer2);	   
		valeursDer2Map.put(bDer2, valeursDer2);

		dossierExterneManager.createObjectManager(der2, e1, blocsDer2, valeursDer2Map, 2000);	

		// tout a bien été enregistré
		assertTrue(dossierExterneManager.findAllObjectsManager().size() == nbD + 3);
		assertTrue(blocExterneDao.findAll().size() == nbB + 6);
		assertTrue(valeurExterneDao.findAll().size() == nbV + 15);

		injectionManager.saveDossierAndChildrenManager(parent, b1, u1, "/path/to/baseDir");

		// assertion creation données
		Prelevement newPrel = prelevementManager
				.findByCodeLikeManager("NEW_PREL_PARENT_001", true).get(0);
		assertTrue(newPrel.getNature().getId().equals(1));
		assertTrue(newPrel.getConsentType().getId().equals(1));
		Maladie newMal = newPrel.getMaladie();
		assertTrue(newMal.getLibelle().equals("Glioblastome")); // maladie defaut pour banque
		Patient newPat = newMal.getPatient();
		assertTrue(newPat.getNom().equals("NEW_PAT1_001"));
		assertTrue(newPat.getPrenom().equals("JEAN"));
		assertTrue(newPat.getSexe().equals("M"));
		assertTrue(newPat.getDateNaissance().equals(format.parseObject("1954-06-04"))); // 19540604
		assertTrue(newPat.getPatientEtat().equals("V"));
		
		ProdDerive derive1 = prodDeriveManager.findByCodeLikeManager("NEW_DERIVE_001", true).get(0);
		assertTrue(derive1.getProdType().getId().equals(1));
		assertTrue(derive1.getTransformation().getEntite().getEntiteId().equals(2));
		assertTrue(derive1.getTransformation().getObjetId().equals(newPrel.getPrelevementId()));
		assertTrue(derive1.getConc().equals(new Float(112)));
		assertTrue(derive1.getConcUnite().getId().equals(13));

		ProdDerive derive2 = prodDeriveManager.findByCodeLikeManager("NEW_DERIVE_002", true).get(0);
		assertTrue(derive2.getProdType().getId().equals(2));
		assertTrue(derive2.getTransformation().getEntite().getEntiteId().equals(2));
		assertTrue(derive2.getTransformation().getObjetId().equals(newPrel.getPrelevementId()));
		assertTrue(derive2.getConc() == null);
		assertTrue(derive2.getConcUnite() == null);
		
		// 1 seule transfo
		assertTrue(derive1.getTransformation().getTransformationId()
				.equals(derive1.getTransformation().getTransformationId()));

		// les dossiers ont été supprimés si tout s'est bien passé
		assertTrue(dossierExterneManager.findAllObjectsManager().size() == nbD);
		assertTrue(blocExterneDao.findAll().size() == nbB);
		assertTrue(valeurExterneDao.findAll().size() == nbV);

		// clean up
		prelevementManager.removeObjectCascadeManager(newPrel, null, u1, null);
		patientManager.removeObjectManager(newPat, null, u1, null);
		
		assertTrue(prelevementManager.findByCodeLikeManager("NEW_PREL_PARENT_001", true).isEmpty());

		// nulls
		injectionManager.saveDossierAndChildrenManager(null, b1, u1, "/path/to/dir");
		injectionManager.saveDossierAndChildrenManager(parent, null, u1, "/path/to/dir");
		
		assertTrue(prelevementManager.findByCodeLikeManager("NEW_PREL_PARENT_001", true).isEmpty());
		
		final List<TKFantomableObject> fs = new ArrayList<>();
		fs.add(newPat);
		fs.add(newMal);
		fs.add(newPrel);
		fs.add(derive1);
		fs.add(derive2);
		cleanUpFantomes(fs);
	}
	
	@Test
	public void testFindExistingPrelevementByEmetteurManager() {
		Emetteur emet = emetteurDao.findById(3);
		Plateforme pf1 = plateformeDao.findById(1);
		List<DossierExterne> dos = injectionManager.findExistingPrelevementByEmetteurManager(emet, pf1);
		assertTrue(dos.size() == 1);
		assertTrue(dos.get(0).getIdentificationDossier().equals("PRLVT1"));
		
		// creation d'un dossier corespondant à un dérivé
		// qui ne doit pas ressortir même si le code correspond à un prélèvement
		DossierExterne newDos2 = new DossierExterne();
		newDos2.setDateOperation(Calendar.getInstance());
		newDos2.setOperation("D");
		newDos2.setIdentificationDossier("PRLVT3");
		newDos2.setEntiteId(8);
		dossierExterneManager.createObjectManager(newDos2, emet, null, null, 25);
		
		dos = injectionManager.findExistingPrelevementByEmetteurManager(emet, pf1);
		assertTrue(dos.size() == 1);
		assertTrue(dos.get(0).getIdentificationDossier().equals("PRLVT1"));
		
		// nulls
		dos = injectionManager.findExistingPrelevementByEmetteurManager(null, pf1);
		assertTrue(dos.isEmpty());
		dos = injectionManager.findExistingPrelevementByEmetteurManager(emet, null);
		assertTrue(dos.isEmpty());
		
		// clean up
		dossierExterneManager.removeObjectManager(newDos2);
	}
	
	@Test
	public void testFindExistingParentByEmetteurAndEntiteManager() {
		
		Emetteur emet = emetteurDao.findById(3);
		Plateforme pf1 = plateformeDao.findById(1);
		List<DossierExterne> dos = injectionManager.findExistingParentByEmetteurAndEntiteManager(emet, 23, pf1);
		assertTrue(dos.isEmpty());

		// creation d'un dossier dérivé dont le parent existe
		// en base
		DossierExterne der2 = new DossierExterne();
		der2.setIdentificationDossier("NEW_DERIVE_002");
		der2.setDateOperation(Calendar.getInstance());
		der2.setEmetteur(emetteurDao.findById(1));
		der2.setOperation("D");
		der2.setEntiteId(8);

		List<BlocExterne> blocsDer2 = new ArrayList<BlocExterne>();
		Hashtable<BlocExterne, List<ValeurExterne>> valeursDer2Map = new Hashtable<BlocExterne, List<ValeurExterne>>();

		final ValeurExterne codePrelDer2 = new ValeurExterne();
		codePrelDer2.setValeur("PRLVT1");
		codePrelDer2.setChampEntiteId(23);
		List<ValeurExterne> valeursDer2Prel = new ArrayList<ValeurExterne>();
		valeursDer2Prel.add(codePrelDer2);

		final BlocExterne bPrelDer2 = new BlocExterne();
		bPrelDer2.setEntiteId(2);
		bPrelDer2.setOrdre(1);
		blocsDer2.add(bPrelDer2);	   
		valeursDer2Map.put(bPrelDer2, valeursDer2Prel);

		final ValeurExterne codeDer2 = new ValeurExterne();
		codeDer2.setValeur("NEW_DERIVE_002");
		codeDer2.setChampEntiteId(79);
		final ValeurExterne typeDer2 = new ValeurExterne();
		typeDer2.setValeur("ARN");
		typeDer2.setChampEntiteId(78);
		List<ValeurExterne> valeursDer2 = new ArrayList<ValeurExterne>();
		valeursDer2.add(codeDer2); valeursDer2.add(typeDer2);

		final BlocExterne bDer2 = new BlocExterne();
		bDer2.setEntiteId(8);
		bDer2.setOrdre(2);
		blocsDer2.add(bDer2);	   
		valeursDer2Map.put(bDer2, valeursDer2);
		
		dossierExterneManager.createObjectManager(der2, emet, blocsDer2, valeursDer2Map, 25);

		dos = injectionManager.findExistingParentByEmetteurAndEntiteManager(emet, 23, pf1);
		assertTrue(dos.size() == 1);
		assertTrue(dos.get(0).getIdentificationDossier().equals("NEW_DERIVE_002"));
		
		// nulls
		dos = injectionManager.findExistingParentByEmetteurAndEntiteManager(null, 23, pf1);
		assertTrue(dos.isEmpty());
		dos = injectionManager.findExistingParentByEmetteurAndEntiteManager(emet, null, pf1);
		assertTrue(dos.isEmpty());
		dos = injectionManager.findExistingParentByEmetteurAndEntiteManager(emet, 23, null);
		assertTrue(dos.isEmpty());
		
		// clean up
		dossierExterneManager.removeObjectManager(der2);
	}
	
	@Test
	public void testFindExistingChildByEmetteurAndEntiteManager() {
		
		Emetteur emet = emetteurDao.findById(3);
		Plateforme pf1 = plateformeDao.findById(1);
		List<DossierExterne> dos = injectionManager.findExistingChildByEmetteurAndEntiteManager(emet, pf1);
		assertTrue(dos.isEmpty());
		
		// creation d'un dossier dérivé à sync
		DossierExterne der2 = new DossierExterne();
		der2.setIdentificationDossier("PTRA.1.2");
		der2.setDateOperation(Calendar.getInstance());
		der2.setEmetteur(emetteurDao.findById(1));
		der2.setOperation("D");
		der2.setEntiteId(8);
		dossierExterneManager.createObjectManager(der2, emet, null, null, 25);
		
		dos = injectionManager.findExistingChildByEmetteurAndEntiteManager(emet, pf1);
		assertTrue(dos.size() == 1);
		assertTrue(dos.get(0).getIdentificationDossier().equals("PTRA.1.2"));
		
		// nulls
		dos = injectionManager.findExistingChildByEmetteurAndEntiteManager(null, pf1);
		assertTrue(dos.isEmpty());
		dos = injectionManager.findExistingChildByEmetteurAndEntiteManager(emet, null);
		assertTrue(dos.isEmpty());
		
		// clean up
		dossierExterneManager.removeObjectManager(der2);
	}
	
	@Test
	public void testSynchronizeDeriveChildrenManager() {
		
		final Plateforme pf1 = plateformeDao.findById(1);
		final Utilisateur u1 = utilisateurDao.findById(1);
		final Emetteur e2 = emetteurDao.findById(2);
		
		final Integer nbD = dossierExterneManager.findAllObjectsManager().size();
		final Integer nbDerive = prodDeriveManager.findAllObjectsManager().size();
		final Integer nbTransfo = transformationManager.findAllObjectsManager().size();
		
		// rien a sync
		// nulls
		assertFalse(injectionManager.synchronizeDeriveChildrenManager(e2, pf1, u1, "/to/baseDir"));

		// creation des dossiers 
		// - PREL_PARENT_EXISTANT PRLVT1
		// -- NEW_DERIVE_001 PARENT PRLVT1
		// -- NEW_DERIVE_002 ARN PARENT PRLVT2
		// -- PTRA1.2 -> existe déja, non sync !
		// -- NEW_DERIVE_004 ADN PARENT PRLVT2
		// -> 3 derives ajoutés sur 2 prels donc 2 transfos
		DossierExterne parent = new DossierExterne();
		parent.setIdentificationDossier("PRLVT1");
		parent.setDateOperation(Calendar.getInstance());
		parent.setOperation("D");

		List<BlocExterne> blocsParent = new ArrayList<BlocExterne>();
		Hashtable<BlocExterne, List<ValeurExterne>> valeursParentMap = new Hashtable<BlocExterne, List<ValeurExterne>>();

		final ValeurExterne codePrel = new ValeurExterne();
		codePrel.setValeur("PRLVT1");
		codePrel.setChampEntiteId(23);   
		final ValeurExterne natPrel = new ValeurExterne();
		natPrel.setValeur("TISSU");
		natPrel.setChampEntiteId(24);
		final ValeurExterne consentPrel = new ValeurExterne();
		consentPrel.setValeur("EN ATTENTE");
		consentPrel.setChampEntiteId(26);
		List<ValeurExterne> valeursPrelParent = new ArrayList<ValeurExterne>();
		valeursPrelParent.add(codePrel); valeursPrelParent.add(natPrel);
		valeursPrelParent.add(consentPrel);

		final BlocExterne bPrel = new BlocExterne();
		bPrel.setEntiteId(2);
		bPrel.setOrdre(2);
		blocsParent.add(bPrel);	   
		valeursParentMap.put(bPrel, valeursPrelParent);

		dossierExterneManager.createObjectManager(parent, e2, blocsParent, valeursParentMap, 2000);

		// dossier dérivé 1
		DossierExterne der1 = new DossierExterne();
		der1.setIdentificationDossier("NEW_DERIVE_001");
		der1.setDateOperation(Calendar.getInstance());
		der1.setOperation("D");
		der1.setEntiteId(8);

		List<BlocExterne> blocsDer1 = new ArrayList<BlocExterne>();
		Hashtable<BlocExterne, List<ValeurExterne>> valeursDer1Map = new Hashtable<BlocExterne, List<ValeurExterne>>();

		final ValeurExterne codePrelDer1 = new ValeurExterne();
		codePrelDer1.setValeur("PRLVT1");
		codePrelDer1.setChampEntiteId(23);
		List<ValeurExterne> valeursDer1Prel = new ArrayList<ValeurExterne>();
		valeursDer1Prel.add(codePrelDer1);

		final BlocExterne bPrelDer1 = new BlocExterne();
		bPrelDer1.setEntiteId(2);
		bPrelDer1.setOrdre(1);
		blocsDer1.add(bPrelDer1);	   
		valeursDer1Map.put(bPrelDer1, valeursDer1Prel);

		final ValeurExterne codeDer1 = new ValeurExterne();
		codeDer1.setValeur("NEW_DERIVE_001");
		codeDer1.setChampEntiteId(79);
		final ValeurExterne typeDer1 = new ValeurExterne();
		typeDer1.setValeur("ADN");
		typeDer1.setChampEntiteId(78);
		List<ValeurExterne> valeursDer1 = new ArrayList<ValeurExterne>();
		valeursDer1.add(codeDer1); valeursDer1.add(typeDer1);

		final BlocExterne bDer1 = new BlocExterne();
		bDer1.setEntiteId(8);
		bDer1.setOrdre(2);
		blocsDer1.add(bDer1);	   
		valeursDer1Map.put(bDer1, valeursDer1);

		dossierExterneManager.createObjectManager(der1, e2, blocsDer1, valeursDer1Map, 2000);	

		// dossier dérivé 2
		DossierExterne der2 = new DossierExterne();
		der2.setIdentificationDossier("NEW_DERIVE_002");
		der2.setDateOperation(Calendar.getInstance());
		der2.setOperation("D");
		der2.setEntiteId(8);

		List<BlocExterne> blocsDer2 = new ArrayList<BlocExterne>();
		Hashtable<BlocExterne, List<ValeurExterne>> valeursDer2Map = new Hashtable<BlocExterne, List<ValeurExterne>>();

		final ValeurExterne codePrelDer2 = new ValeurExterne();
		codePrelDer2.setValeur("PRLVT2");
		codePrelDer2.setChampEntiteId(23);
		List<ValeurExterne> valeursDer2Prel = new ArrayList<ValeurExterne>();
		valeursDer2Prel.add(codePrelDer2);

		final BlocExterne bPrelDer2 = new BlocExterne();
		bPrelDer2.setEntiteId(2);
		bPrelDer2.setOrdre(1);
		blocsDer2.add(bPrelDer2);	   
		valeursDer2Map.put(bPrelDer2, valeursDer2Prel);

		final ValeurExterne codeDer2 = new ValeurExterne();
		codeDer2.setValeur("NEW_DERIVE_002");
		codeDer2.setChampEntiteId(79);
		final ValeurExterne typeDer2 = new ValeurExterne();
		typeDer2.setValeur("ADN");
		typeDer2.setChampEntiteId(78);
		List<ValeurExterne> valeursDer2 = new ArrayList<ValeurExterne>();
		valeursDer2.add(codeDer2); valeursDer2.add(typeDer2);

		final BlocExterne bDer2 = new BlocExterne();
		bDer2.setEntiteId(8);
		bDer2.setOrdre(2);
		blocsDer2.add(bDer2);	   
		valeursDer2Map.put(bDer2, valeursDer2);

		dossierExterneManager.createObjectManager(der2, e2, blocsDer2, valeursDer2Map, 2000);
		
		// dossier dérivé 3 existant
		DossierExterne der3 = new DossierExterne();
		der3.setIdentificationDossier("PTRA.1.2");
		der3.setDateOperation(Calendar.getInstance());
		der3.setOperation("D");
		der3.setEntiteId(8);

		List<BlocExterne> blocsDer3 = new ArrayList<BlocExterne>();
		Hashtable<BlocExterne, List<ValeurExterne>> valeursDer3Map = new Hashtable<BlocExterne, List<ValeurExterne>>();

		final ValeurExterne codePrelDer3 = new ValeurExterne();
		codePrelDer3.setValeur("PRLVT1");
		codePrelDer3.setChampEntiteId(23);
		List<ValeurExterne> valeursDer3Prel = new ArrayList<ValeurExterne>();
		valeursDer3Prel.add(codePrelDer3);

		final BlocExterne bPrelDer3 = new BlocExterne();
		bPrelDer3.setEntiteId(2);
		bPrelDer3.setOrdre(1);
		blocsDer3.add(bPrelDer3);	   
		valeursDer3Map.put(bPrelDer3, valeursDer3Prel);

		final ValeurExterne codeDer3 = new ValeurExterne();
		codeDer3.setValeur("PTRA.1.2");
		codeDer3.setChampEntiteId(79);
		final ValeurExterne typeDer3 = new ValeurExterne();
		typeDer3.setValeur("ADN");
		typeDer3.setChampEntiteId(78);
		List<ValeurExterne> valeursDer3 = new ArrayList<ValeurExterne>();
		valeursDer3.add(codeDer3); valeursDer3.add(typeDer3);

		final BlocExterne bDer3 = new BlocExterne();
		bDer3.setEntiteId(8);
		bDer3.setOrdre(2);
		blocsDer3.add(bDer3);	   
		valeursDer3Map.put(bDer3, valeursDer3);

		dossierExterneManager.createObjectManager(der3, e2, blocsDer3, valeursDer3Map, 2000);
		
		// dossier dérivé 4 
		DossierExterne der4 = new DossierExterne();
		der4.setIdentificationDossier("NEW_DERIVE_004");
		der4.setDateOperation(Calendar.getInstance());
		der4.setOperation("D");
		der4.setEntiteId(8);

		List<BlocExterne> blocsDer4 = new ArrayList<BlocExterne>();
		Hashtable<BlocExterne, List<ValeurExterne>> valeursDer4Map = new Hashtable<BlocExterne, List<ValeurExterne>>();

		final ValeurExterne codePrelDer4 = new ValeurExterne();
		codePrelDer4.setValeur("PRLVT2");
		codePrelDer4.setChampEntiteId(23);
		List<ValeurExterne> valeursDer4Prel = new ArrayList<ValeurExterne>();
		valeursDer4Prel.add(codePrelDer4);

		final BlocExterne bPrelDer4 = new BlocExterne();
		bPrelDer4.setEntiteId(2);
		bPrelDer4.setOrdre(1);
		blocsDer4.add(bPrelDer4);	   
		valeursDer4Map.put(bPrelDer4, valeursDer4Prel);

		final ValeurExterne codeDer4 = new ValeurExterne();
		codeDer4.setValeur("NEW_DERIVE_004");
		codeDer4.setChampEntiteId(79);
		final ValeurExterne typeDer4 = new ValeurExterne();
		typeDer4.setValeur("ARN");
		typeDer4.setChampEntiteId(78);
		List<ValeurExterne> valeursDer4 = new ArrayList<ValeurExterne>();
		valeursDer4.add(codeDer4); valeursDer4.add(typeDer4);

		final BlocExterne bDer4 = new BlocExterne();
		bDer4.setEntiteId(8);
		bDer4.setOrdre(2);
		blocsDer4.add(bDer4);	   
		valeursDer4Map.put(bDer4, valeursDer4);

		dossierExterneManager.createObjectManager(der4, e2, blocsDer4, valeursDer4Map, 2000);
	
		// tout a bien été enregistré
		assertTrue(dossierExterneManager.findAllObjectsManager().size() == nbD + 5);
		assertTrue(prodDeriveManager.findAllObjectsManager().size() == nbDerive);
		assertTrue(transformationManager.findAllObjectsManager().size() == nbTransfo);                                                                                              
		
		// sync = 2 dérivés ajoutés
		assertTrue(injectionManager.synchronizeDeriveChildrenManager(e2, pf1, u1, "/to/baseDir"));
		
		assertTrue(prodDeriveManager.findAllObjectsManager().size() == nbDerive + 3);
		assertTrue(transformationManager.findAllObjectsManager().size() == nbTransfo + 2);                                                                                         
		
		ProdDerive derive1 = prodDeriveManager.findByCodeLikeManager("NEW_DERIVE_001", true).get(0);
		assertTrue(derive1.getProdType().getId().equals(1));
		assertTrue(derive1.getTransformation().getEntite().getEntiteId().equals(2));
		assertTrue(derive1.getTransformation().getObjetId().equals(1));
		assertTrue(derive1.getConc() == null);
		assertTrue(derive1.getConcUnite() == null);

		ProdDerive derive2 = prodDeriveManager.findByCodeLikeManager("NEW_DERIVE_002", true).get(0);
		assertTrue(derive2.getProdType().getId().equals(1));
		assertTrue(derive2.getTransformation().getEntite().getEntiteId().equals(2));
		assertTrue(derive2.getTransformation().getObjetId().equals(2));
		assertTrue(derive2.getConc() == null);
		assertTrue(derive2.getConcUnite() == null);
		
		ProdDerive derive4 = prodDeriveManager
						.findByCodeLikeManager("NEW_DERIVE_004", true).get(0);
		assertTrue(derive4.getProdType().getId().equals(2));
		assertTrue(derive4.getTransformation().getEntite().getEntiteId().equals(2));
		assertTrue(derive4.getTransformation().getObjetId().equals(2));
		assertTrue(derive4.getConc() == null);
		assertTrue(derive4.getConcUnite() == null);
		
		// 1 seule transfo
		assertTrue(derive2.getTransformation().getTransformationId()
				.equals(derive4.getTransformation().getTransformationId()));
		
		// 2 operation synchro
		assertTrue(getOperationManager()
			.findByObjetIdEntiteAndOpeTypeManager(prelevementManager.findByIdManager(1), 
				operationTypeDao.findById(19)).size() == 1); // sync
		assertTrue(getOperationManager()
			.findByObjetIdEntiteAndOpeTypeManager(prelevementManager.findByIdManager(2), 
				operationTypeDao.findById(19)).size() == 1); // sync
		
		// nulls
		assertFalse(injectionManager.synchronizeDeriveChildrenManager(null, pf1, u1, "/to/baseDir"));
		assertFalse(injectionManager.synchronizeDeriveChildrenManager(e2, null, u1, "/to/baseDir"));
		assertFalse(injectionManager.synchronizeDeriveChildrenManager(e2, pf1, null, "/to/baseDir"));
		assertFalse(injectionManager.synchronizeDeriveChildrenManager(e2, pf1, u1, null));
		
		// clean up
		dossierExterneManager.removeObjectManager(parent);
		dossierExterneManager.removeObjectManager(der3);
		prodDeriveManager.removeObjectManager(derive1, null, u1, null);
		prodDeriveManager.removeObjectManager(derive2, null, u1, null); 
		prodDeriveManager.removeObjectManager(derive4, null, u1, null); 
		transformationManager.removeObjectManager(derive2.getTransformation(), null, u1);    
		transformationManager.removeObjectManager(derive1.getTransformation(), null, u1);
		getOperationManager().removeObjectManager(getOperationManager()
			.findByObjetIdEntiteAndOpeTypeManager(prelevementManager.findByIdManager(1), 
					operationTypeDao.findById(19)).get(0)); // sync
		getOperationManager().removeObjectManager(getOperationManager()
			.findByObjetIdEntiteAndOpeTypeManager(prelevementManager.findByIdManager(2), 
					operationTypeDao.findById(19)).get(0)); // sync
		
		// les dossiers ont été supprimés si tout s'est bien passé
		// les deux dérivés ont été sync donc suppr
		assertTrue(dossierExterneManager.findAllObjectsManager().size() == nbD);
		assertTrue(transformationManager.findAllObjectsManager().size() == nbTransfo);                                                                                         
		
		final List<TKFantomableObject> fs = new ArrayList<>();
		fs.add(derive1);
		fs.add(derive2);
		fs.add(derive4);
		cleanUpFantomes(fs);
	}
}