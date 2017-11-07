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
package fr.aphp.tumorotek.manager.test.xml;

import fr.aphp.tumorotek.dao.interfacage.EmetteurDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.impl.xml.*;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.manager.xml.XmlUtils;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.stockage.Terminale;
import org.jdom.Document;
import org.jdom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * 
 * Classe de test pour le manager XmlUtils.
 * Classe créée le 09/07/2010.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class XmlUtilsTest extends AbstractManagerTest {
	
	/** Bean. */
	private XmlUtils xmlUtils;
	/** Bean. */
	private PrelevementManager prelevementManager;
	/** Bean. */
	private EchantillonManager echantillonManager;
	private TerminaleManager terminaleManager;
	private EmetteurDao emetteurDao;
	
	public XmlUtilsTest() {
		
	}

	public void setXmlUtils(XmlUtils xUtils) {
		this.xmlUtils = xUtils;
	}
	
	public void setPrelevementManager(PrelevementManager pManager) {
		this.prelevementManager = pManager;
	}

	public void setEchantillonManager(EchantillonManager eManager) {
		this.echantillonManager = eManager;
	}

	public void setTerminaleManager(TerminaleManager tManager) {
		this.terminaleManager = tManager;
	}

	public void setEmetteurDao(EmetteurDao eDao) {
		this.emetteurDao = eDao;
	}

	/**
	 * Test la méthode createJDomDocument.
	 */
	public void testCreateJDomDocument() {
		
		Document doc = xmlUtils.createJDomDocument();
		assertNotNull(doc);
		assertNotNull(doc.getRootElement());
		assertTrue(doc.getRootElement().getName().equals("Impression"));
		
	}
	
	/**
	 * Test la méthode addCoupleValeur.
	 */
	@SuppressWarnings("unchecked")
	public void testAddCoupleValeur() {
		
		Element ligne = new Element("LigneParagraphe");
		CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
		xmlUtils.addCoupleValeur(ligne, cv1);
		CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
		xmlUtils.addCoupleValeur(ligne, cv2);
		
		List<Element> children = ligne.getChildren("CoupleValeurs");
		assertTrue(children.size() == 2);
		Element firstCouple = children.get(0);
		assertTrue(firstCouple.getChildText("NomValeur").equals("Nom"));
		assertTrue(firstCouple.getChildText("Valeur").equals("Valeur"));
		
		Element secondCouple = children.get(1);
		assertTrue(secondCouple.getChildText("NomValeur").equals("Nom2"));
		assertTrue(secondCouple.getChildText("Valeur").equals("Valeur2"));
		
		xmlUtils.addCoupleValeur(null, cv2);
		xmlUtils.addCoupleValeur(ligne, null);
		assertTrue(ligne.getChildren("CoupleValeurs").size() == 2);
	}
	
	/**
	 * Test la méthode addCoupleValeur.
	 */
	@SuppressWarnings("unchecked")
	public void testAddCoupleSimpleValeur() {
		
		Element ligne = new Element("LigneParagraphe");
		CoupleSimpleValeur cv1 = new CoupleSimpleValeur("Nom", "Valeur");
		xmlUtils.addCoupleSimpleValeur(ligne, cv1);
		CoupleSimpleValeur cv2 = new CoupleSimpleValeur("Nom2", "Valeur2");
		xmlUtils.addCoupleSimpleValeur(ligne, cv2);
		
		List<Element> children = ligne.getChildren("CoupleSimpleValeurs");
		assertTrue(children.size() == 2);
		Element firstCouple = children.get(0);
		assertTrue(firstCouple.getChildText("NomValeur").equals("Nom"));
		assertTrue(firstCouple.getChildText("Valeur").equals("Valeur"));
		
		Element secondCouple = children.get(1);
		assertTrue(secondCouple.getChildText("NomValeur").equals("Nom2"));
		assertTrue(secondCouple.getChildText("Valeur").equals("Valeur2"));
		
		xmlUtils.addCoupleSimpleValeur(null, cv2);
		xmlUtils.addCoupleSimpleValeur(ligne, null);
		assertTrue(ligne.getChildren("CoupleSimpleValeurs").size() == 2);
	}
	
	/**
	 * Test la méthode addLigne.
	 */
	@SuppressWarnings("unchecked")
	public void testAddLigne() {
		
		Element sousP = new Element("SousParagraphe");
		CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
		CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
		CoupleValeur cv3 = new CoupleValeur("Nom3", "Valeur3");
		LigneParagraphe ligne = new LigneParagraphe("li1",
				new CoupleValeur[]{cv1, cv2, cv3});
		xmlUtils.addLigne(sousP, ligne);
		
		assertTrue(sousP.getChildren("Ligne").size() == 1);
		Element ligneElt = (Element) sousP.getChildren("Ligne").get(0);
		List<Element> children = ligneElt.getChildren("LigneParagraphe");
		assertTrue(children.size() == 1);
		Element ligneP = children.get(0);
		assertTrue(ligneP.getChildren("CoupleValeurs").size() == 3);
		List<Element> childrenL = ligneP.getChildren("CoupleValeurs");
		Element firstCouple = childrenL.get(0);
		assertTrue(firstCouple.getChildText("NomValeur").equals("Nom"));
		assertTrue(firstCouple.getChildText("Valeur").equals("Valeur"));
		
		Element secondCouple = childrenL.get(1);
		assertTrue(secondCouple.getChildText("NomValeur").equals("Nom2"));
		assertTrue(secondCouple.getChildText("Valeur").equals("Valeur2"));
		
		xmlUtils.addLigne(sousP, null);
		xmlUtils.addLigne(null, ligne);
		assertTrue(sousP.getChildren("Ligne").size() == 1);
	}
	
	/**
	 * Test la méthode addLigneDeuxColonnesParagraphe.
	 */
	@SuppressWarnings("unchecked")
	public void testAddLigneDeuxColonnesParagraphe() {
		
		Element sousP = new Element("SousParagraphe");
		CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
		LigneDeuxColonnesParagraphe ligne = new LigneDeuxColonnesParagraphe(
				cv1);
		xmlUtils.addLigneDeuxColonnesParagraphe(sousP, ligne);
		
		assertTrue(sousP.getChildren("Ligne").size() == 1);
		Element ligneElt = (Element) sousP.getChildren("Ligne").get(0);
		List<Element> children = ligneElt.getChildren(
				"LigneDeuxColonnesParagraphe");
		assertTrue(children.size() == 1);
		Element ligneP = children.get(0);
		assertTrue(ligneP.getChildren("CoupleValeurs").size() == 1);
		List<Element> childrenL = ligneP.getChildren("CoupleValeurs");
		Element firstCouple = childrenL.get(0);
		assertTrue(firstCouple.getChildText("NomValeur").equals("Nom"));
		assertTrue(firstCouple.getChildText("Valeur").equals("Valeur"));
		
		xmlUtils.addLigneDeuxColonnesParagraphe(sousP, null);
		xmlUtils.addLigneDeuxColonnesParagraphe(null, ligne);
		assertTrue(sousP.getChildren("Ligne").size() == 1);
	}
	
	/**
	 * Test la méthode addLigneSimple.
	 */
	@SuppressWarnings("unchecked")
	public void testAddLigneSimple() {
		
		Element sousP = new Element("SousParagraphe");
		CoupleSimpleValeur cv1 = new CoupleSimpleValeur("Nom", "Valeur");
		
		LigneSimpleParagraphe ligne = new LigneSimpleParagraphe(cv1);
		xmlUtils.addLigneSimple(sousP, ligne);
		
		assertTrue(sousP.getChildren("Ligne").size() == 1);
		Element ligneElt = (Element) sousP.getChildren("Ligne").get(0);
		List<Element> children = ligneElt.getChildren("LigneSimpleParagraphe");
		assertTrue(children.size() == 1);
		Element ligneP = children.get(0);
		assertTrue(ligneP.getChildren("CoupleSimpleValeurs").size() == 1);
		
		xmlUtils.addLigneSimple(sousP, null);
		xmlUtils.addLigneSimple(null, ligne);
		assertTrue(sousP.getChildren("Ligne").size() == 1);
	}
	
	/**
	 * Test la méthode addLigneListe.
	 */
	@SuppressWarnings("unchecked")
	public void testAddLigneListe() {
		
		Element sousP = new Element("SousParagraphe");
		LigneListe ligne = new LigneListe(
				new String[]{"Cell1", "Cell2", "Cell3"});
		xmlUtils.addLigneListe(sousP, ligne);
		
		List<Element> children = sousP.getChildren("LigneListe");
		assertTrue(children.size() == 1);
		Element ligneP = children.get(0);
		assertTrue(ligneP.getChildren("Cellule").size() == 3);
		List<Element> childrenL = ligneP.getChildren("Cellule");
		Element firstCellule = childrenL.get(0);
		assertTrue(firstCellule.getText().equals("Cell1"));
		
		Element secCellule = childrenL.get(1);
		assertTrue(secCellule.getText().equals("Cell2"));
		
		xmlUtils.addLigneListe(sousP, null);
		xmlUtils.addLigneListe(null, ligne);
		assertTrue(sousP.getChildren("LigneListe").size() == 1);
	}
	
	/**
	 * Test la méthode addColonnesListe.
	 */
	@SuppressWarnings("unchecked")
	public void testAddColonnesListe() {
		
		Element sousP = new Element("SousParagraphe");
		xmlUtils.addColonnesListe(sousP, 5);
		
		List<Element> children = sousP.getChildren("Colonnes");
		assertTrue(children.size() == 1);
		Element entetel = children.get(0);
		assertTrue(entetel.getChildren("Colonne").size() == 5);
		
		xmlUtils.addColonnesListe(null, 5);
		assertTrue(sousP.getChildren("Colonnes").size() == 1);
	}
	
	/**
	 * Test la méthode addEnteteListe.
	 */
	@SuppressWarnings("unchecked")
	public void testAddEnteteListe() {
		
		Element sousP = new Element("SousParagraphe");
		EnteteListe entete = new EnteteListe(
				new String[]{"Col1", "Col2", "Col3"});
		xmlUtils.addEnteteListe(sousP, entete);
		
		List<Element> children = sousP.getChildren("EnteteListe");
		assertTrue(children.size() == 1);
		Element entetel = children.get(0);
		assertTrue(entetel.getChildren("NomColonne").size() == 3);
		List<Element> childrenL = entetel.getChildren("NomColonne");
		Element firstCol = childrenL.get(0);
		assertTrue(firstCol.getText().equals("Col1"));
		
		Element secCol = childrenL.get(1);
		assertTrue(secCol.getText().equals("Col2"));
		
		xmlUtils.addEnteteListe(sousP, null);
		xmlUtils.addEnteteListe(null, entete);
		assertTrue(sousP.getChildren("EnteteListe").size() == 1);
	}
	
	/**
	 * Test la méthode addSousParagraphe.
	 */
	@SuppressWarnings("unchecked")
	public void testAddSousParagraphe() {
		
		Element para = new Element("Paragraphe");
		CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
		CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
		CoupleValeur cv3 = new CoupleValeur("Nom3", "Valeur3");
		LigneParagraphe ligne1 = new LigneParagraphe("li1",
				new CoupleValeur[]{cv1, cv2});
		LigneParagraphe ligne2 = new LigneParagraphe("li2",
				new CoupleValeur[]{cv3});
		String inconnu = "Inconnu";
		ListeElement liste = new ListeElement();
		SousParagraphe sous = new SousParagraphe("Titre", 
				new LigneParagraphe[]{ligne1, ligne2},
				inconnu, liste);
		
		xmlUtils.addSousParagraphe(para, sous);
		
		List<Element> children = para.getChildren("SousParagraphe");
		assertTrue(children.size() == 1);
		Element sousP = children.get(0);
		assertNotNull(sousP);
		assertTrue(sousP.getChildText("TitreSousParagraphe").equals("Titre"));
		assertTrue(sousP.getChildren("Ligne").size() == 2);
		assertTrue(sousP.getChildren("Inconnu").size() == 1);
		assertTrue(sousP.getChildren("Liste").size() == 1);
		
		SousParagraphe sous2 = new SousParagraphe();
		xmlUtils.addSousParagraphe(para, sous2);
		children = para.getChildren("SousParagraphe");
		assertTrue(children.size() == 2);
		sousP = children.get(1);
		assertNotNull(sousP);
		assertTrue(sousP.getChild("TitreSousParagraphe") == null);
		assertTrue(sousP.getChildren("Ligne").size() == 0);
		
		xmlUtils.addSousParagraphe(para, null);
		xmlUtils.addSousParagraphe(null, sous);
		assertTrue(para.getChildren("SousParagraphe").size() == 2);
	}
	
	/**
	 * Test la méthode addParagraphe.
	 */
	@SuppressWarnings("unchecked")
	public void testAddParagraphe() {
		
		CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
		CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
		CoupleValeur cv3 = new CoupleValeur("Nom3", "Valeur3");
		CoupleValeur cv4 = new CoupleValeur("Nom4", "Valeur4");
		LigneParagraphe ligne1 = new LigneParagraphe("li1",
				new CoupleValeur[]{cv1, cv2});
		LigneParagraphe ligne2 = new LigneParagraphe("li2",
				new CoupleValeur[]{cv3});
		SousParagraphe sous = new SousParagraphe("Titre", 
				new LigneParagraphe[]{ligne1, ligne2},
				null, null);
		SousParagraphe sous2 = new SousParagraphe();
		LigneParagraphe ligne4 = new LigneParagraphe("li4",
				new CoupleValeur[]{cv4});
		String inconnu = "Inconnu";
		ListeElement liste = new ListeElement();
		
		Paragraphe para = new Paragraphe("Titre", 
				new LigneParagraphe[]{ligne4}, 
				new SousParagraphe[]{sous, sous2},
				inconnu, liste);
		Element page = new Element("Page");
		
		xmlUtils.addParagraphe(page, para);
		
		List<Element> children = page.getChildren("Paragraphe");
		assertTrue(children.size() == 1);
		Element p = children.get(0);
		assertNotNull(p);
		assertTrue(p.getChildText("TitreParagraphe").equals("Titre"));
		assertTrue(p.getChildren("Ligne").size() == 1);
		assertTrue(p.getChildren("SousParagraphe").size() == 2);
		assertTrue(p.getChildren("Inconnu").size() == 1);
		assertTrue(p.getChildren("Liste").size() == 1);
		
		Paragraphe para2 = new Paragraphe();
		xmlUtils.addParagraphe(page, para2);
		children = page.getChildren("Paragraphe");
		assertTrue(children.size() == 2);
		p = children.get(1);
		assertNotNull(p);
		assertTrue(p.getChild("TitreParagraphe") == null);
		assertTrue(p.getChildren("Ligne").size() == 0);
		assertTrue(p.getChildren("SousParagraphe").size() == 0);
		
		xmlUtils.addParagraphe(page, null);
		xmlUtils.addParagraphe(null, para);
		assertTrue(page.getChildren("Paragraphe").size() == 2);
	}
	
	/**
	 * Test la méthode addListe.
	 */
	@SuppressWarnings("unchecked")
	public void testAddListe() {
		
		LigneListe ligne1 = new LigneListe(
				new String[]{"Cell1", "Cell2", "Cell3"});
		LigneListe ligne2 = new LigneListe(
				new String[]{"Bell1", "Bell2", "Bell3"});
		LigneListe ligne3 = new LigneListe(
				new String[]{"Tell1", "Tell2", "Tell3"});
		LigneListe ligne4 = new LigneListe(
				new String[]{"Aell1", "Aell2", "Aell3"});
		
		EnteteListe entete = new EnteteListe(
				new String[]{"Col1", "Col2", "Col3"});
		
		ListeElement liste = new ListeElement("Liste", entete, 
				new LigneListe[]{ligne1, ligne2, ligne3, ligne4});
		Element page = new Element("Page");
		xmlUtils.addListe(page, liste);
		
		List<Element> children = page.getChildren("Liste");
		assertTrue(children.size() == 1);
		Element p = children.get(0);
		assertNotNull(p);
		assertTrue(p.getChildText("TitreParagraphe").equals("Liste"));
		assertTrue(p.getChildren("Colonnes").size() == 1);
		assertTrue(p.getChildren("EnteteListe").size() == 1);
		assertTrue(p.getChildren("LigneListe").size() == 4);
		
		ListeElement liste2 = new ListeElement();
		xmlUtils.addListe(page, liste2);
		children = page.getChildren("Liste");
		assertTrue(children.size() == 2);
		p = children.get(1);
		assertNotNull(p);
		assertTrue(p.getChild("TitreParagraphe") == null);
		assertTrue(p.getChildren("EnteteListe").size() == 0);
		assertTrue(p.getChildren("LigneListe").size() == 0);
		
		xmlUtils.addListe(page, null);
		xmlUtils.addListe(null, liste2);
		assertTrue(page.getChildren("Liste").size() == 2);
	}

	/**
	 * Test de la méthode addPage().
	 */
	public void testAddPage() {
		Document doc = xmlUtils.createJDomDocument();
		Element root = doc.getRootElement();
		String titre = "Titre";
		
		Element page = xmlUtils.addPage(root, titre);
		assertNotNull(page);
		xmlUtils.addPage(root, null);
		xmlUtils.addPage(root, null);
		xmlUtils.addPage(root, null);
		xmlUtils.addPage(null, null);
		
		assertTrue(root.getChildren("Page").size() == 4);
	}
	
	/**
	 * Test de la méthode addTitreForDocument().
	 */
	public void testAddTitreForDocument() {
		Document doc = xmlUtils.createJDomDocument();
		Element root = doc.getRootElement();
		xmlUtils.addTitreForDocument(root, null);
		assertTrue(root.getChildText("Titre").equals(""));
		
		Document doc2 = xmlUtils.createJDomDocument();
		Element root2 = doc2.getRootElement();
		xmlUtils.addTitreForDocument(root2, "TITRE");
		assertTrue(root2.getChildText("Titre").equals("TITRE"));
	}
	
	/**
	 * Test de la méthode createXMLFile().
	 */
	public void testCreateXMLFile() {
		Document doc = xmlUtils.createJDomDocument();
		Element root = doc.getRootElement();
		Element page = xmlUtils.addPage(root, "TITRE");
		
		CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
		cv1.setObligatoire(true);
		CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
		cv2.setAnonyme(true);
		CoupleValeur cv3 = new CoupleValeur("Nom3", "Valeur3");
		CoupleValeur cv4 = new CoupleValeur("Nom4", "Valeur4");
		LigneParagraphe ligne1 = new LigneParagraphe("li1",
				new CoupleValeur[]{cv1, cv2});
		LigneParagraphe ligne2 = new LigneParagraphe("li2",
				new CoupleValeur[]{cv3});
		SousParagraphe sous = new SousParagraphe("Titre", 
				new LigneParagraphe[]{ligne1, ligne2},
				null, null);
		SousParagraphe sous2 = new SousParagraphe();
		LigneParagraphe ligne4 = new LigneParagraphe("li4",
				new CoupleValeur[]{cv4});
		
		Paragraphe para = new Paragraphe("Titre",  
				new LigneParagraphe[]{ligne4}, 
				new SousParagraphe[]{sous, sous2},
				null, null);		
		xmlUtils.addParagraphe(page, para);
		
		String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
		String file = "test.xml";
		xmlUtils.createXMLFile(doc, folder, file);
		
		String path = folder + file;
		File f = new File(path);
		assertTrue(f.exists());
		assertTrue(f.length() > 0);
		
		//f.delete();
	}
	
	/**
	 * Test de la méthode createXMLFile().
	 */
	public void testTransformAsPdf() {
		Document doc = xmlUtils.createJDomDocument();
		Element root = doc.getRootElement();
		Element page = xmlUtils.addPage(root, "TITRE");
		
		CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
		CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
		CoupleValeur cv3 = new CoupleValeur("Nom3", "Valeur3");
		CoupleValeur cv4 = new CoupleValeur("Nom4", "Valeur4");
		LigneParagraphe ligne1 = new LigneParagraphe("li1",
				new CoupleValeur[]{cv1, cv2});
		LigneParagraphe ligne2 = new LigneParagraphe("li2",
				new CoupleValeur[]{cv3});
		SousParagraphe sous = new SousParagraphe("Titre", 
				new LigneParagraphe[]{ligne1, ligne2},
				null, null);
		LigneParagraphe ligne4 = new LigneParagraphe("li4",
				new CoupleValeur[]{cv4});
		
		Paragraphe para = new Paragraphe("Titre",  
				new LigneParagraphe[]{ligne4}, 
				new SousParagraphe[]{sous},
				null, null);		
		xmlUtils.addParagraphe(page, para);
		
		org.w3c.dom.Document transformed = xmlUtils.transformAsPdf(doc);
		
		assertNotNull(transformed);
	}
	
	public Document creerDocument() {
		Prelevement p = prelevementManager.findByIdManager(1);
		
		Document doc = xmlUtils.createJDomDocument();
		Element root = doc.getRootElement();
		Element page = xmlUtils.addPage(root, null);
		xmlUtils.addBasDePage(root, "Fiche Prélèvement - 15/07/2010");
		xmlUtils.addHautDePage(root, "Fiche d'impression Tumorotek",
				true, this.getClass().getClassLoader().getResource("data/empImpressionUSE.gif").getPath());
		
		// Partie principale
		CoupleValeur cp1 = new CoupleValeur("Code de prélèvement", p.getCode());
		cp1.setObligatoire(true);
		CoupleValeur cp2 = new CoupleValeur(
				"N° de Laboratorie", "Numéro interne du prélèvement du" 
				+ " laboratoire");
		cp2.setAnonyme(false);
		CoupleSimpleValeur cp3 = new CoupleSimpleValeur(
				"Nature", "AAAAAAAAA AAAAAAAA AAAAAAAA AAAAAAAA AAAAAAAAAA");
		CoupleSimpleValeur cp3Bis = new CoupleSimpleValeur(
				"Nature pour le prélèvement actuel", 
				"AAAAAAAAA AAAAAAAA AAAAAAAA AAAAAAAA AAAAAAAAAA");
		LigneParagraphe li1 = new LigneParagraphe("li1",
				new CoupleValeur[]{cp1, cp2});
		LigneSimpleParagraphe li1Bis = new LigneSimpleParagraphe(cp3);
		LigneSimpleParagraphe li1Bis2 = new LigneSimpleParagraphe(cp3Bis);
		LigneDeuxColonnesParagraphe lic = new LigneDeuxColonnesParagraphe(cp1);
		CoupleValeur cp1Bis = new CoupleValeur(
				"Value de test trop longue pour une colonne", "Et HOP !!!!!");
		LigneDeuxColonnesParagraphe lic2 = 
			new LigneDeuxColonnesParagraphe(cp1Bis);
		Paragraphe par1 = new Paragraphe(null,  
				new Object[]{li1Bis, li1, li1Bis2, lic, lic2}, 
				null,
				null, null);
		xmlUtils.addParagraphe(page, par1);
		xmlUtils.addParagraphe(page, par1);
		xmlUtils.addParagraphe(page, par1);
		xmlUtils.addParagraphe(page, par1);
		
		// Partie Patient
		CoupleValeur cp4 = new CoupleValeur(
				"N° Patient", p.getMaladie().getPatient().getNip());
		cp4.setObligatoire(true);
		CoupleValeur cp5 = new CoupleValeur(
				"N° de dossier", p.getPatientNda());
		CoupleValeur cp6 = new CoupleValeur(
				"Nom usuel", p.getMaladie().getPatient().getNom());
		cp6.setObligatoire(true);
		CoupleValeur cp7 = new CoupleValeur(
				"Prénom", p.getMaladie().getPatient().getPrenom());
		CoupleValeur cp8 = new CoupleValeur(
				"Date de naissance", "08/10/1983");
		CoupleValeur cp9 = new CoupleValeur(
				"Sexe", p.getMaladie().getPatient().getSexe());
		LigneParagraphe li2 = new LigneParagraphe("li2",
				new CoupleValeur[]{cp4, cp5});
		LigneParagraphe li3 = new LigneParagraphe("li3",
				new CoupleValeur[]{cp6, cp7});
		LigneParagraphe li4 = new LigneParagraphe("li4",
				new CoupleValeur[]{cp8, cp9});
		
		CoupleValeur cp10 = new CoupleValeur(
				"Libellé", p.getMaladie().getLibelle());
		CoupleValeur cp11 = new CoupleValeur(
				"Code diagnostic", p.getMaladie().getCode());
		LigneParagraphe li5 = new LigneParagraphe("li5",
				new CoupleValeur[]{cp10, cp11});
		SousParagraphe sousPar1 = new SousParagraphe("Informations maladie", 
				new LigneParagraphe[]{li5},
				null, null);
		Paragraphe par2 = new Paragraphe("Informations patient",  
				new LigneParagraphe[]{li2, li3, li4}, 
				new SousParagraphe[]{sousPar1},
				null, null);
		xmlUtils.addParagraphe(page, par2);
		
		// Informations prélèvement
		CoupleValeur cp12 = new CoupleValeur(
				"Date et heure de prélèvement", 
				p.getDatePrelevement().getTime().toString());
		CoupleValeur cp13 = new CoupleValeur(
				"Type de prélèvement", p.getPrelevementType().getType());
		CoupleValeur cp14 = new CoupleValeur(
				"Stérile", p.getSterile().toString());
		CoupleValeur cp15 = new CoupleValeur(
				"Date de congélation", "07/07/2010");
		CoupleValeur cp16 = new CoupleValeur(
				"Etablissement", p.getServicePreleveur()
					.getEtablissement().getNom());
		CoupleValeur cp17 = new CoupleValeur(
				"Service", p.getServicePreleveur().getNom());
		CoupleValeur cp18 = new CoupleValeur(
				"Préleveur", p.getPreleveur().getNomAndPrenom());
		LigneParagraphe li6 = new LigneParagraphe("",
				new CoupleValeur[]{cp12, cp13});
		LigneParagraphe li7 = new LigneParagraphe("",
				new CoupleValeur[]{cp14, cp15});
		LigneParagraphe li8 = new LigneParagraphe("",
				new CoupleValeur[]{cp16});
		LigneParagraphe li9 = new LigneParagraphe("",
				new CoupleValeur[]{cp17});
		LigneParagraphe li10 = new LigneParagraphe("",
				new CoupleValeur[]{cp18});
		// conditionnement
		CoupleValeur cp19 = new CoupleValeur(
				"Type de conditionnement", 
				p.getConditType().getType());
		CoupleValeur cp20 = new CoupleValeur(
				"Nombre", p.getConditNbr().toString());
		CoupleSimpleValeur cp21 = new CoupleSimpleValeur(
				"Milieu", p.getConditMilieu().getMilieu());
		LigneParagraphe li11 = new LigneParagraphe("",
				new CoupleValeur[]{cp19, cp20});
		LigneSimpleParagraphe li12 = new LigneSimpleParagraphe(cp21);
		SousParagraphe sousPar2 = new SousParagraphe("Conditionnement", 
				new Object[]{li11, li12},
				null, null);
		// statut juridique
		CoupleValeur cp22 = new CoupleValeur(
				"Statut juridique", p.getConsentType().getType());
		CoupleValeur cp23 = new CoupleValeur(
				"Date", "07/10/1995");
		LigneParagraphe li13 = new LigneParagraphe("",
				new CoupleValeur[]{cp22, cp23});
		SousParagraphe sousPar3 = new SousParagraphe("Statut juridique", 
				new LigneParagraphe[]{li13},
				null, null);
		
		Paragraphe par3 = new Paragraphe("Informations prélèvement",  
				new LigneParagraphe[]{li6, li7, li8, li9, li10}, 
				new SousParagraphe[]{sousPar2, sousPar3},
				null, null);
		xmlUtils.addParagraphe(page, par3);
		xmlUtils.addParagraphe(page, par3);
		xmlUtils.addParagraphe(page, par3);
		xmlUtils.addParagraphe(page, par3);
		xmlUtils.addParagraphe(page, par3);
		xmlUtils.addParagraphe(page, par3);
		
		// Site intermédiaire
		SousParagraphe sousPar4 = new SousParagraphe(
				"Départ du site préleveur", 
				null,
				"Site de départ inconnu", null);
		EnteteListe enteteS = new EnteteListe(new String[]{
				"Date arrivée", "Date départ", "Laboratoire", "Température"
		});
		LigneListe ligneS1 = new LigneListe(new String[]{
				"08/04/2010",
				"09/04/2010",
				"HEMATO",
				"-10.5"
		});
		LigneListe ligneS2 = new LigneListe(new String[]{
				"13/05/2010",
				"15/05/2010",
				"CELLULO",
				"-8.5"
		});
		ListeElement listeSite = new ListeElement(null,
				enteteS, new LigneListe[]{ligneS1, ligneS2});
		SousParagraphe sousPar5 = new SousParagraphe("Sites intermédiaires", 
				null,
				null, listeSite);
		Paragraphe par4 = new Paragraphe("Transfert du site préleveur "
				+ "vers le site de stockage",  
				null, 
				new SousParagraphe[]{sousPar4, sousPar5},
				null, null);
		xmlUtils.addParagraphe(page, par4);
		
		// Liste d'échantillons
		EnteteListe entete = new EnteteListe(new String[]{
				"Code", "Date stockage", "Type", "Statut"
		});
		Echantillon e1 = echantillonManager.findByIdManager(1);
		LigneListe ligneL1 = new LigneListe(new String[]{
				e1.getCode(),
				"09/07/2010",
				e1.getEchantillonType().getType(),
				e1.getObjetStatut().getStatut()
		});
		Echantillon e2 = echantillonManager.findByIdManager(2);
		LigneListe ligneL2 = new LigneListe(new String[]{
				e2.getCode(),
				e2.getDateStock().getTime().toString(),
				e2.getEchantillonType().getType(),
				e2.getObjetStatut().getStatut()
		});
		ListeElement listeEchan = new ListeElement("Echantillons (2)",
				entete, new LigneListe[]{ligneL1, ligneL2, ligneL2, ligneL2,
				ligneL1, ligneL2, ligneL2, ligneL2, ligneL1, ligneL2, 
				ligneL2, ligneL2, ligneL1, ligneL2, ligneL2, ligneL2,
				ligneL1, ligneL2, ligneL2, ligneL2,
				ligneL1, ligneL2, ligneL2, ligneL2, ligneL1, ligneL2, 
				ligneL2, ligneL2, ligneL1, ligneL2, ligneL2, ligneL2});
		xmlUtils.addListe(page, listeEchan);
		xmlUtils.addListe(page, listeEchan);
		
		return doc;
	}
	
	public Document creerDocumentBoite() {
		Document doc = xmlUtils.createJDomDocumentBoites();
		Element root = doc.getRootElement();
		Element page = xmlUtils.addPageBoite(root, 
				"Déstockage des échantillons");
				
		Terminale boite = terminaleManager.findByIdManager(1);
		List<String> instructions = new ArrayList<String>();
		instructions.add("Ouvrir le conteneur n° C1");
		instructions.add("Ouvrir le compartiment n° A.");
		List<String> elements = new ArrayList<String>();
		elements.add("1: Echantillon n° : TESTPP.1");
		List<Integer> positions = new ArrayList<Integer>();
		positions.add(2);
		BoiteImpression boiteI = new BoiteImpression(boite,
				elements, instructions, "Pris", "Selectionnes", 
				"Vide", "Boite : B1", positions, "Instructions a faire", 
				"Récupération des échantillons", "Visualisation");
		boiteI.setTitreIntermediaire("Echantillons");
		boiteI.setSeparateur(true);
		xmlUtils.addBoite(page, boiteI, "");
		
		return doc;
	}
	
	public Document creerDocumentContenu() {
		Document doc = xmlUtils.createJDomDocumentContenuBoite();
		Element root = doc.getRootElement();
		Element page = xmlUtils.addPageContenuBoite(root, 
				"Impression d'une boîte");
				
		Terminale boite = terminaleManager.findByIdManager(1);
		List<String> parents = new ArrayList<String>();
		parents.add("Conteneur : C1");
		parents.add("Compartiment : A");
		parents.add("Boîte : A1");
		List<String> nombres = new ArrayList<String>();
		nombres.add("Nombre d'échantillons : 95");
		nombres.add("Nombre de patients : 25");
		BoiteContenu boiteC = new BoiteContenu(
				boite, nombres, parents, "Vide");

		xmlUtils.addBoiteContenu(page, boiteC, "",
				new Hashtable<String, String>(),
				"VERT", new Hashtable<String, String>(),
				"VERT");
		
		return doc;
	}
	
	public void testCreerPdf() {
		// création du document contenant les infos
		Document doc = creerDocument();
		
		byte[] results = null;
		try {
			results = xmlUtils.creerPdf(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertTrue(results.length > 0);
		
		String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
		String file = "test.pdf";		
		String path = folder + file;
		
		File f = new File(path);
		FileOutputStream fo;
		try {
			fo = new FileOutputStream(f);
		
			for (int i = 0; i < results.length; i++) {
				fo.write(results[i]);
			}
			fo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void testCreerHtml() {
		Document doc = creerDocument();
		
		byte[] results = null;
		try {
			results = xmlUtils.creerHtml(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertTrue(results.length > 0);
		
		String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
		String file = "testBoite.html";		
		String path = folder + file;
		File f = new File(path);
		java.io.FileWriter fw;
		try {
			fw = new java.io.FileWriter(f);
		
			for (int i = 0; i < results.length; i++) {
				fw.write(results[i]);
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/***********************************************************/
	/************ Modelisation des boites **********************/
	/***********************************************************/
	
	/**
	 * Test la méthode createJDomDocumentBoites.
	 */
	public void testCreateJDomDocumentBoites() {
		
		Document doc = xmlUtils.createJDomDocumentBoites();
		assertNotNull(doc);
		assertNotNull(doc.getRootElement());
		assertTrue(doc.getRootElement().getName().equals("ImpressionBoite"));
		
	}
	
	/**
	 * Test de la méthode addPageBoite().
	 */
	public void testAddPageBoite() {
		Document doc = xmlUtils.createJDomDocumentBoites();
		Element root = doc.getRootElement();
		String titre = "Titre";
		
		Element page = xmlUtils.addPageBoite(root, titre);
		assertNotNull(page);
		xmlUtils.addPageBoite(root, null);
		xmlUtils.addPageBoite(root, null);
		xmlUtils.addPageBoite(root, null);
		xmlUtils.addPageBoite(null, null);
		
		assertTrue(root.getChildren("PageBoite").size() == 4);
	}
	
	/**
	 * Test de la méthode addTitreIntermediaire().
	 */
	public void testAddTitreIntermediaire() {
		Document doc = xmlUtils.createJDomDocumentBoites();
		Element root = doc.getRootElement();
		xmlUtils.addTitreIntermediaire(root, null);
		assertNull(root.getChildText("TitreIntermediaire"));
		
		Document doc2 = xmlUtils.createJDomDocumentBoites();
		Element root2 = doc2.getRootElement();
		xmlUtils.addTitreIntermediaire(root2, "TITRE");
		assertTrue(root2.getChildText("TitreIntermediaire").equals("TITRE"));
	}
	
	/**
	 * Test de la méthode addSeparateur().
	 */
	public void testAddSeparateur() {
		Document doc = xmlUtils.createJDomDocumentBoites();
		Element root = doc.getRootElement();
		xmlUtils.addSeparateur(null);
		assertTrue(root.getChildren("Separateur").size() == 0);
		
		Document doc2 = xmlUtils.createJDomDocumentBoites();
		Element root2 = doc2.getRootElement();
		xmlUtils.addSeparateur(root2);
		assertTrue(root2.getChildren("Separateur").size() == 1);
	}
	
	/**
	 * Test la méthode addInstructions.
	 */
	@SuppressWarnings("unchecked")
	public void testAddInstructions() {
		
		Element page = new Element("PageBoite");
		List<String> instructionsListe = new ArrayList<String>();
		instructionsListe.add("Test");
		instructionsListe.add("test1");
		instructionsListe.add("test3");
		xmlUtils.addInstructions(page, instructionsListe);
		
		List<Element> children = page.getChildren("Instructions");
		assertTrue(children.size() == 1);
		Element instructions = children.get(0);
		assertTrue(instructions.getChildren("Instruction").size() == 3);
		List<Element> childrenI = instructions.getChildren("Instruction");
		Element first = childrenI.get(0);
		assertTrue(first.getText().equals("Test"));
		
		Element second = childrenI.get(1);
		assertTrue(second.getText().equals("test1"));
		
		xmlUtils.addInstructions(page, null);
		xmlUtils.addInstructions(null, instructionsListe);
		assertTrue(page.getChildren("Instructions").size() == 1);
	}
	
	/**
	 * Test la méthode addListeElements.
	 */
	@SuppressWarnings("unchecked")
	public void testAddListeElements() {
		
		Element page = new Element("PageBoite");
		List<String> elementsListe = new ArrayList<String>();
		elementsListe.add("Elt1");
		elementsListe.add("Elt2");
		elementsListe.add("Elt3");
		xmlUtils.addListeElements(page, elementsListe, "Titre");
		
		List<Element> children = page.getChildren("ListeElements");
		assertTrue(children.size() == 1);
		Element listeElements = children.get(0);
		assertTrue(listeElements.getChildText("TitreListe").equals("Titre"));
		
		assertTrue(listeElements.getChildren("Element").size() == 3);
		List<Element> childrenElt = listeElements.getChildren("Element");
		Element first = childrenElt.get(0);
		assertTrue(first.getText().equals("Elt1"));
		
		Element second = childrenElt.get(1);
		assertTrue(second.getText().equals("Elt2"));
		
		xmlUtils.addListeElements(page, null, null);
		xmlUtils.addListeElements(null, elementsListe, null);
		assertTrue(page.getChildren("ListeElements").size() == 1);
	}
	
	/**
	 * Test la méthode createModelisation.
	 */
	@SuppressWarnings("unchecked")
	public void testCreateModelisation() {
		
		Element page = new Element("PageBoite");
		Terminale t1 = terminaleManager.findByIdManager(1);
		List<Integer> pos = new ArrayList<Integer>();
		pos.add(2);
		xmlUtils.createModelisation(page, t1, "Boite : BT1",
				pos, "Vide", "Pris", "Selectionne", "");
		
		List<Element> children = page.getChildren("Modelisation");
		assertTrue(children.size() == 1);
		Element modele = children.get(0);
		assertTrue(modele.getChildText("TitreBoite").equals("Boite : BT1"));
		
		assertTrue(modele.getChildren("LigneBoite").size() == 10);
		List<Element> childrenElt = modele.getChildren("LigneBoite");
		
		Element firstLigne = childrenElt.get(0);
		List<Element> emps = firstLigne.getChildren("Emplacement");
		assertTrue(emps.size() == 10);
		assertTrue(emps.get(0).getChildren("EmpOccupe").size() == 1);
		assertTrue(emps.get(1).getChildren("EmpSelectionne").size() == 1);
		assertTrue(emps.get(5).getChildren("EmpLibre").size() == 1);
		
		assertTrue(modele.getChildren("LegendeVide").size() == 1);
		assertTrue(modele.getChildren("LegendePris").size() == 1);
		assertTrue(modele.getChildren("LegendeSelectionne").size() == 1);
		
	}
	
	/**
	 * Test la méthode addBoite.
	 */
	@SuppressWarnings("unchecked")
	public void testAddBoite() {
		
		Element page = new Element("PageBoite");
		Terminale t1 = terminaleManager.findByIdManager(1);
		List<String> instructionsL = new ArrayList<String>();
		instructionsL.add("In1");
		instructionsL.add("In2");
		List<String> elementsL = new ArrayList<String>();
		elementsL.add("Elt1");
		elementsL.add("Elt1");
		elementsL.add("Elt3");
		List<Integer> positions = new ArrayList<Integer>();
		positions.add(2);
		BoiteImpression boiteI = new BoiteImpression(t1,
				elementsL, instructionsL, "Pris", "Selectionne", 
				"Vide", "Boite : B1", positions, "Instructions", 
				"Récupération des échantillons", "Visualisation");
		xmlUtils.addBoite(page, boiteI, "");
		
		List<Element> children = page.getChildren("Boite");
		assertTrue(children.size() == 1);
		Element boite = children.get(0);
		assertTrue(boite.getChildren("Modelisation").size() == 1);
		
		List<Element> instructions = boite.getChildren("Instructions");
		assertTrue(instructions.get(0).getChildren("Instruction").size() == 2);
		
		List<Element> elements = boite.getChildren("ListeElements");
		assertTrue(elements.get(0).getChildren("Element").size() == 3);
		
		assertTrue(boite.getChildren("TitreModelisation").size() == 1);
		assertTrue(boite.getChildren("TitreInstruction").size() == 1);
		
	}
	
	/**
	 * Test de la méthode createXMLFile().
	 */
	public void testCreateXMLFileBoite() {
		Document doc = creerDocumentBoite();
		
		String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
		String file = "testBoite.xml";
		xmlUtils.createXMLFile(doc, folder, file);
		
		String path = folder + file;
		File f = new File(path);
		assertTrue(f.exists());
		assertTrue(f.length() > 0);
		
		f.delete();
	}
	
	public void testCreerBoiteHtml() {
		Document doc = creerDocumentBoite();
		
		byte[] results = null;
		try {
			results = xmlUtils.creerBoiteHtml(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertTrue(results.length > 0);
		
		String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
		String file = "testBoite.html";		
		String path = folder + file;
		File f = new File(path);
		java.io.FileWriter fw;
		try {
			fw = new java.io.FileWriter(f);
		
			for (int i = 0; i < results.length; i++) {
				fw.write(results[i]);
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testEquals() {
		Terminale boite = terminaleManager.findByIdManager(1);
		List<String> instructions = new ArrayList<String>();
		instructions.add("Ouvrir le conteneur n° C1");
		instructions.add("Ouvrir le compartiment n° A.");
		List<String> elements = new ArrayList<String>();
		elements.add("1: Echantillon n° : TESTPP.1");
		List<Integer> positions = new ArrayList<Integer>();
		positions.add(2);
		BoiteImpression boiteI = new BoiteImpression(boite,
				elements, instructions, "Pris", "Selectionnes", 
				"Vide", "Boite : B1", positions, "Instructions a faire", 
				"Récupération des échantillons", "Visualisation");
		
		BoiteImpression boiteI2 = new BoiteImpression(boite,
				elements, instructions, "Pris", "Selectionnes", 
				"Vide", "Boite : B1", positions, "Instructions a faire", 
				"Récupération des échantillons", "Visualisation");
		
		assertTrue(boiteI.equals(boiteI2));
		boiteI2.setNom("***");
		assertTrue(boiteI.equals(boiteI2));
		Terminale boite3 = terminaleManager.findByIdManager(3);
		boiteI2.setBoite(boite3);
		assertFalse(boiteI.equals(boiteI2));
		assertFalse(boiteI.equals(null));
	}
	
	public void testHashCode() {
		Terminale boite = terminaleManager.findByIdManager(1);
		List<String> instructions = new ArrayList<String>();
		instructions.add("Ouvrir le conteneur n° C1");
		instructions.add("Ouvrir le compartiment n° A.");
		List<String> elements = new ArrayList<String>();
		elements.add("1: Echantillon n° : TESTPP.1");
		List<Integer> positions = new ArrayList<Integer>();
		positions.add(2);
		BoiteImpression boiteI = new BoiteImpression(boite,
				elements, instructions, "Pris", "Selectionnes", 
				"Vide", "Boite : B1", positions, "Instructions a faire", 
				"Récupération des échantillons", "Visualisation");
		
		BoiteImpression boiteI2 = new BoiteImpression(boite,
				elements, instructions, "Pris", "Selectionnes", 
				"Vide", "Boite : B1", positions, "Instructions a faire", 
				"Récupération des échantillons", "Visualisation");
		
		assertTrue(boiteI.hashCode() == boiteI2.hashCode());
		int val = boiteI.hashCode();
		boiteI.hashCode();
		boiteI.hashCode();
		boiteI.hashCode();
		assertTrue(val == boiteI.hashCode());
	}
	
	/***********************************************************/
	/***************** Contenu des boites **********************/
	/***********************************************************/
	
	/**
	 * Test la méthode createJDomDocumentContenuBoite.
	 */
	public void testCreateJDomDocumentContenuBoite() {
		
		Document doc = xmlUtils.createJDomDocumentContenuBoite();
		assertNotNull(doc);
		assertNotNull(doc.getRootElement());
		assertTrue(doc.getRootElement().getName().equals("ImpressionContenu"));
		
	}
	
	/**
	 * Test de la méthode addPageContenuBoite().
	 */
	public void testAddPageContenuBoite() {
		Document doc = xmlUtils.createJDomDocumentContenuBoite();
		Element root = doc.getRootElement();
		String titre = "Titre";
		
		Element page = xmlUtils.addPageContenuBoite(root, titre);
		assertNotNull(page);
		xmlUtils.addPageContenuBoite(root, null);
		xmlUtils.addPageContenuBoite(root, null);
		xmlUtils.addPageContenuBoite(root, null);
		xmlUtils.addPageContenuBoite(null, null);
		
		assertTrue(root.getChildren("PageContenuBoite").size() == 4);
	}
	
	/**
	 * Test la méthode addListeParents.
	 */
	@SuppressWarnings("unchecked")
	public void testAddListeParents() {
		
		Element page = new Element("PageContenuBoite");
		List<String> parentsListe = new ArrayList<String>();
		parentsListe.add("Test");
		parentsListe.add("test1");
		parentsListe.add("test3");
		xmlUtils.addListeParents(page, parentsListe);
		
		List<Element> children = page.getChildren("ListeParents");
		assertTrue(children.size() == 1);
		Element parents = children.get(0);
		assertTrue(parents.getChildren("Parent").size() == 3);
		List<Element> childrenI = parents.getChildren("Parent");
		Element first = childrenI.get(0);
		assertTrue(first.getText().equals("Test"));
		
		Element second = childrenI.get(1);
		assertTrue(second.getText().equals("test1"));
		
		xmlUtils.addListeParents(page, null);
		xmlUtils.addListeParents(null, parentsListe);
		assertTrue(page.getChildren("ListeParents").size() == 1);
	}
	
	/**
	 * Test la méthode addListeNombres.
	 */
	@SuppressWarnings("unchecked")
	public void testAddListeNombres() {
		
		Element page = new Element("PageBoite");
		List<String> nombresListe = new ArrayList<String>();
		nombresListe.add("Elt1");
		nombresListe.add("Elt2");
		nombresListe.add("Elt3");
		xmlUtils.addListeNombres(page, nombresListe);
		
		List<Element> children = page.getChildren("ListeNombres");
		assertTrue(children.size() == 1);
		Element listeElements = children.get(0);
		
		assertTrue(listeElements.getChildren("Nombre").size() == 3);
		List<Element> childrenElt = listeElements.getChildren("Nombre");
		Element first = childrenElt.get(0);
		assertTrue(first.getText().equals("Elt1"));
		
		Element second = childrenElt.get(1);
		assertTrue(second.getText().equals("Elt2"));
		
		xmlUtils.addListeNombres(page, null);
		xmlUtils.addListeNombres(null, nombresListe);
		assertTrue(page.getChildren("ListeNombres").size() == 1);
	}
	
	/**
	 * Test la méthode createContenu.
	 */
	@SuppressWarnings("unchecked")
	public void testCreateContenu() {
		
		Element page = new Element("PageContenuBoite");
		Terminale t1 = terminaleManager.findByIdManager(1);
		xmlUtils.createContenu(page, t1, "Vide", "",
				new Hashtable<String, String>(),
				"VERT", new Hashtable<String, String>(),
				"VERT");
		
		List<Element> children = page.getChildren("Modelisation");
		assertTrue(children.size() == 1);
		Element modele = children.get(0);
		
		assertTrue(modele.getChildren("LigneBoite").size() == 10);
		List<Element> childrenElt = modele.getChildren("LigneBoite");
		
		Element firstLigne = childrenElt.get(0);
		List<Element> emps = firstLigne.getChildren("Emplacement");
		assertTrue(emps.size() == 10);
		assertTrue(emps.get(0).getChildren("EmpOccupe").size() == 1);
		assertTrue(emps.get(1).getChildren("EmpOccupe").size() == 1);
		assertTrue(emps.get(5).getChildren("EmpLibre").size() == 1);
		
	}
	
	/**
	 * Test la méthode addBoiteContenu.
	 */
	@SuppressWarnings("unchecked")
	public void testAddBoiteContenu() {
		
		Element page = new Element("PageBoite");
		Terminale t1 = terminaleManager.findByIdManager(1);
		List<String> listeParents = new ArrayList<String>();
		listeParents.add("Conteneur : C1");
		listeParents.add("Compartiment : A");
		List<String> listeNombres = new ArrayList<String>();
		listeNombres.add("23 Echantillons");
		listeNombres.add("20 Dérivés");
		listeNombres.add("15 patients");
		BoiteContenu boiteC = new BoiteContenu(
				t1, listeNombres, listeParents, "Vide");
		xmlUtils.addBoiteContenu(page, boiteC, "",
				new Hashtable<String, String>(),
				"VERT", new Hashtable<String, String>(),
				"VERT");
		
		List<Element> children = page.getChildren("Contenu");
		assertTrue(children.size() == 1);
		Element boite = children.get(0);
		assertTrue(boite.getChildren("Modelisation").size() == 1);
		
		List<Element> instructions = boite.getChildren("ListeNombres");
		assertTrue(instructions.get(0).getChildren("Nombre").size() == 3);
		
		List<Element> elements = boite.getChildren("ListeParents");
		assertTrue(elements.get(0).getChildren("Parent").size() == 2);
		
	}
	
	/**
	 * Test de la méthode createXMLFile().
	 */
	public void testCreateXMLFileContenu() {
		Document doc = creerDocumentContenu();
		
		String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
		String file = "testContenu.xml";
		xmlUtils.createXMLFile(doc, folder, file);
		
		String path = folder + file;
		File f = new File(path);
		assertTrue(f.exists());
		assertTrue(f.length() > 0);
		
		f.delete();
	}
	
	public void testCreerContenuHtml() {
		Document doc = creerDocumentContenu();
		
		byte[] results = null;
		try {
			results = xmlUtils.creerContenuHtml(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertTrue(results.length > 0);
		
		String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
		String file = "testContenu.html";		
		String path = folder + file;
		File f = new File(path);
		java.io.FileWriter fw;
		try {
			fw = new java.io.FileWriter(f);
		
			for (int i = 0; i < results.length; i++) {
				fw.write(results[i]);
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/************************************************************/
	/******************* ACCORD DE TRANSFERT ********************/
	/************************************************************/
	
	/**
	 * Test la méthode createJDomAccordTranfert.
	 */
	public void testCreateJDomAccordTranfert() {
		
		Document doc = xmlUtils.createJDomAccordTranfert();
		assertNotNull(doc);
		assertNotNull(doc.getRootElement());
		assertTrue(doc.getRootElement().getName()
					.equals("AccordTransfert"));
		
	}
	
	/**
	 * Test la méthode addCoupleAccordValeur.
	 */
	@SuppressWarnings("unchecked")
	public void testAddCoupleAccordValeur() {
		
		Element ligne = new Element("LigneParagraphe");
		CoupleAccordValeur cv1 = new CoupleAccordValeur(
				"Nom", new String[]{"value1", "value2"});
		xmlUtils.addCoupleAccordValeur(ligne, cv1);
		CoupleAccordValeur cv2 = new CoupleAccordValeur(
				"NomB", new String[]{"value3"});
		xmlUtils.addCoupleAccordValeur(ligne, cv2);
		
		List<Element> children = ligne.getChildren("CoupleAccordValeur");
		assertTrue(children.size() == 2);
		Element firstCouple = children.get(0);
		assertTrue(firstCouple.getChildText("NomValeur").equals("Nom"));
		List<Element> valeurs = firstCouple.getChildren("Valeurs");
		assertTrue(valeurs.size() == 1);
		List<Element> valeur = valeurs.get(0).getChildren("Valeur");
		assertTrue(valeur.size() == 2);
		assertTrue(valeur.get(0).getText().equals("value1"));
		
		xmlUtils.addCoupleAccordValeur(null, cv2);
		xmlUtils.addCoupleAccordValeur(ligne, null);
		assertTrue(ligne.getChildren("CoupleAccordValeur").size() == 2);
	}
	
	/**
	 * Test la méthode addLigneAccord.
	 */
	@SuppressWarnings("unchecked")
	public void testAddLigneAccord() {
		
		Element bloc = new Element("BlocPrincipal");
		CoupleAccordValeur cv1 = new CoupleAccordValeur(
				"Nom1", new String[]{"value1", "value2"});
		CoupleAccordValeur cv2 = new CoupleAccordValeur(
				"Nom2", new String[]{"value3"});
		CoupleAccordValeur cv3 = new CoupleAccordValeur(
				"Nom3", new String[]{"value4"});
		LigneAccord ligne = new LigneAccord(
				new CoupleAccordValeur[]{cv1, cv2, cv3});
		xmlUtils.addLigneAccord(bloc, ligne);
		
		List<Element> children = bloc.getChildren("LigneAccord");
		assertTrue(children.size() == 1);
		Element ligneP = children.get(0);
		assertTrue(ligneP.getChildren("CoupleAccordValeur")
				.size() == 3);
		List<Element> childrenL = ligneP.getChildren("CoupleAccordValeur");
		Element firstCouple = childrenL.get(0);
		assertTrue(firstCouple.getChildText("NomValeur").equals("Nom1"));
		
		Element secondCouple = childrenL.get(1);
		assertTrue(secondCouple.getChildText("NomValeur").equals("Nom2"));
		
		xmlUtils.addLigneAccord(bloc, null);
		xmlUtils.addLigneAccord(null, ligne);
		assertTrue(bloc.getChildren("LigneAccord").size() == 1);
	}
	
	/**
	 * Test la méthode addBlocPrincipal.
	 */
	@SuppressWarnings("unchecked")
	public void testAddBlocPrincipal() {
		
		CoupleAccordValeur cv1 = new CoupleAccordValeur(
				"Nom1", new String[]{"value1", "value2"});
		CoupleAccordValeur cv2 = new CoupleAccordValeur(
				"Nom2", new String[]{"value3"});
		CoupleAccordValeur cv3 = new CoupleAccordValeur(
				"Nom3", new String[]{"value4"});
		CoupleAccordValeur cv4 = new CoupleAccordValeur(
				"Nom4", new String[]{"value5", "value6"});
		LigneAccord ligne1 = new LigneAccord(
				new CoupleAccordValeur[]{cv1, cv2});
		LigneAccord ligne2 = new LigneAccord(
				new CoupleAccordValeur[]{cv3});
		LigneAccord ligne3 = new LigneAccord(
				new CoupleAccordValeur[]{cv4});
		
		BlocPrincipal bloc = new BlocPrincipal("Titre1", 
				new LigneAccord[]{ligne1, ligne2, ligne3});
		
		Element page = new Element("Page");
		xmlUtils.addBlocPrincipal(page, bloc);
		
		List<Element> children = page.getChildren("BlocPrincipal");
		assertTrue(children.size() == 1);
		Element p = children.get(0);
		assertNotNull(p);
		assertTrue(p.getChildText("NomBloc").equals("Titre1"));
		assertTrue(p.getChildren("LigneAccord").size() == 3);
		
		BlocPrincipal bloc2 = new BlocPrincipal();
		xmlUtils.addBlocPrincipal(page, bloc2);
		children = page.getChildren("BlocPrincipal");
		assertTrue(children.size() == 2);
		p = children.get(1);
		assertNotNull(p);
		assertTrue(p.getChild("NomBloc") == null);
		assertTrue(p.getChildren("LigneAccord").size() == 0);
		
		xmlUtils.addBlocPrincipal(page, null);
		xmlUtils.addBlocPrincipal(null, bloc);
		assertTrue(page.getChildren("BlocPrincipal").size() == 2);
	}
	
	/**
	 * Test la méthode addTableau.
	 */
	@SuppressWarnings("unchecked")
	public void testAddTableau() {
		
		CoupleAccordValeur cv1 = new CoupleAccordValeur(
				"Nom1", new String[]{"value1", "value2"});
		CoupleAccordValeur cv2 = new CoupleAccordValeur(
				"Nom2", new String[]{"value3"});
		CoupleAccordValeur cv3 = new CoupleAccordValeur(
				"Nom3", new String[]{"value4"});
		CoupleAccordValeur cv4 = new CoupleAccordValeur(
				"Nom4", new String[]{"value5", "value6"});
		LigneAccord ligne1 = new LigneAccord(
				new CoupleAccordValeur[]{cv1, cv2});
		LigneAccord ligne2 = new LigneAccord(
				new CoupleAccordValeur[]{cv3});
		LigneAccord ligne3 = new LigneAccord(
				new CoupleAccordValeur[]{cv4});
		
		BlocPrincipal bloc = new BlocPrincipal("Titre1", 
				new LigneAccord[]{ligne1, ligne2, ligne3});
		
		Element page = new Element("Page");
		xmlUtils.addTableau(page, new BlocPrincipal[]{bloc, bloc, bloc});
		
		List<Element> children = page.getChildren("Tableau");
		assertTrue(children.size() == 1);
		Element p = children.get(0);
		assertNotNull(p);
		assertTrue(p.getChildren("BlocPrincipal").size() == 3);
		
		xmlUtils.addTableau(page, new BlocPrincipal[]{});
		children = page.getChildren("Tableau");
		assertTrue(children.size() == 2);
		p = children.get(1);
		assertNotNull(p);
		assertTrue(p.getChildren("BlocPrincipal").size() == 0);
		
		xmlUtils.addTableau(page, null);
		xmlUtils.addTableau(null, new BlocPrincipal[]{});
		assertTrue(page.getChildren("Tableau").size() == 2);
	}
	
	/**
	 * Test la méthode addValeursSignatures.
	 */
	@SuppressWarnings("unchecked")
	public void testAddValeursSignatures() {
		
		Element ligne = new Element("LigneParagraphe");
		ValeursSignatures vs1 = new ValeursSignatures("Nom", "Valeur");
		xmlUtils.addValeursSignatures(ligne, vs1);
		ValeursSignatures vs2 = new ValeursSignatures("Nom2", "Valeur2");
		xmlUtils.addValeursSignatures(ligne, vs2);
		
		List<Element> children = ligne.getChildren("ValeursSignatures");
		assertTrue(children.size() == 2);
		Element firstCouple = children.get(0);
		assertTrue(firstCouple.getChildText("Valeur1").equals("Nom"));
		assertTrue(firstCouple.getChildText("Valeur2").equals("Valeur"));
		
		Element secondCouple = children.get(1);
		assertTrue(secondCouple.getChildText("Valeur1").equals("Nom2"));
		assertTrue(secondCouple.getChildText("Valeur2").equals("Valeur2"));
		
		xmlUtils.addValeursSignatures(null, vs2);
		xmlUtils.addValeursSignatures(ligne, null);
		assertTrue(ligne.getChildren("ValeursSignatures").size() == 2);
	}
	
	/**
	 * Test la méthode addLigne.
	 */
	@SuppressWarnings("unchecked")
	public void testAddListeSignature() {
		
		Element sousP = new Element("SousParagraphe");
		ValeursSignatures vs1 = new ValeursSignatures("Nom", "Valeur");
		ValeursSignatures vs2 = new ValeursSignatures("Nom2", "Valeur2");
		ListeSignature ligne = new ListeSignature(
				new ValeursSignatures[]{vs1, vs2});
		xmlUtils.addListeSignature(sousP, ligne);
		
		List<Element> children = sousP.getChildren("ListeSignature");
		assertTrue(children.size() == 1);
		Element ligneP = children.get(0);
		assertTrue(ligneP.getChildren("ValeursSignatures").size() == 2);
		List<Element> childrenL = ligneP.getChildren("ValeursSignatures");
		Element firstCouple = childrenL.get(0);
		assertTrue(firstCouple.getChildText("Valeur1").equals("Nom"));
		assertTrue(firstCouple.getChildText("Valeur2").equals("Valeur"));
		
		Element secondCouple = childrenL.get(1);
		assertTrue(secondCouple.getChildText("Valeur1").equals("Nom2"));
		assertTrue(secondCouple.getChildText("Valeur2").equals("Valeur2"));
		
		xmlUtils.addListeSignature(sousP, null);
		xmlUtils.addListeSignature(null, ligne);
		assertTrue(sousP.getChildren("ListeSignature").size() == 1);
	}
	
	/**
	 * Test la méthode addSignatures.
	 */
	@SuppressWarnings("unchecked")
	public void testAddSignatures() {
		
		ValeursSignatures vs1 = new ValeursSignatures("Nom", "Valeur");
		ValeursSignatures vs2 = new ValeursSignatures("Nom2", "Valeur2");
		ListeSignature ligne = new ListeSignature(
				new ValeursSignatures[]{vs1, vs2});
		ValeursSignatures vs3 = new ValeursSignatures("Nom3", "Valeur3");
		ValeursSignatures vs4 = new ValeursSignatures("Nom4", "Valeur4");
		ListeSignature ligne2 = new ListeSignature(
				new ValeursSignatures[]{vs3, vs4});
		
		Signatures sign = new Signatures("Titre1", "Titre2",
				new ListeSignature[]{ligne, ligne2});
		Element page = new Element("Page");
		
		xmlUtils.addSignatures(page, sign);
		
		List<Element> children = page.getChildren("Signatures");
		assertTrue(children.size() == 1);
		Element p = children.get(0);
		assertNotNull(p);
		assertTrue(p.getChildren("Entete").size() == 1);
		Element e = (Element) p.getChildren("Entete").get(0);
		assertTrue(e.getChildText("Titre1").equals("Titre1"));
		assertTrue(e.getChildText("Titre2").equals("Titre2"));
		assertTrue(p.getChildren("ListeSignature").size() == 2);
		
		Signatures sign2 = new Signatures();
		xmlUtils.addSignatures(page, sign2);
		children = page.getChildren("Signatures");
		assertTrue(children.size() == 2);
		p = children.get(1);
		assertNotNull(p);
		assertTrue(p.getChild("Entete") == null);
		assertTrue(p.getChildren("ListeSignature").size() == 0);
		
		xmlUtils.addSignatures(page, null);
		xmlUtils.addSignatures(null, sign2);
		assertTrue(page.getChildren("Signatures").size() == 2);
	}
	
	public Document creerDocumentAccord() {
		Document doc = xmlUtils.createJDomAccordTranfert();
		Element root = doc.getRootElement();
		Element page = xmlUtils.addPage(root, "Accord de transfert");
		xmlUtils.addBasDePage(root, "bas");
		xmlUtils.addHautDePage(root, "haut", false, null);
		
		// Tableau
		CoupleAccordValeur cv1 = new CoupleAccordValeur(
				"Nom organisation", new String[]{"Hopital Bichat", 
						"12 rue de billancourt - 75019 PARIS"});
		CoupleAccordValeur cv2 = new CoupleAccordValeur(
				"Responsable", new String[]{"Dr GREGORY"});
		CoupleAccordValeur cv3 = new CoupleAccordValeur(
				"Téléphone", new String[]{"0143568975", "mail@mail.fr"});
		LigneAccord li1 = new LigneAccord(
				new CoupleAccordValeur[]{cv1});
		LigneAccord li2 = new LigneAccord(
				new CoupleAccordValeur[]{cv2});
		LigneAccord li3 = new LigneAccord(
				new CoupleAccordValeur[]{cv3});
		BlocPrincipal bloc1 = new BlocPrincipal("Destinataire", 
				new LigneAccord[]{li1, li2, li3});
		
		CoupleAccordValeur cv4 = new CoupleAccordValeur(
				"Nom organisation", new String[]{"Hopital Saint Louis", 
						"1 rue Claude Vellefeaux - 75010 PARIS"});
		CoupleAccordValeur cv5 = new CoupleAccordValeur(
				"Responsable", new String[]{"Dr VENTADOUR"});
		CoupleAccordValeur cv6 = new CoupleAccordValeur(
				"Téléphone", new String[]{"0143487965", "mail@mail.fr"});
		LigneAccord li4 = new LigneAccord(
				new CoupleAccordValeur[]{cv4});
		LigneAccord li5 = new LigneAccord(
				new CoupleAccordValeur[]{cv5});
		LigneAccord li6 = new LigneAccord(
				new CoupleAccordValeur[]{cv6});
		BlocPrincipal bloc2 = new BlocPrincipal("Expéditeur", 
				new LigneAccord[]{li4, li5, li6});
		
		CoupleAccordValeur cv7 = new CoupleAccordValeur(
				"Nature", new String[]{"CELLULES"});
		CoupleAccordValeur cv8 = new CoupleAccordValeur(
				"Quantité", new String[]{"10ml"});
		CoupleAccordValeur cv9 = new CoupleAccordValeur(
				"Date", new String[]{"15/11/2010"});
		CoupleAccordValeur cv10 = new CoupleAccordValeur(
				"Contenant", new String[]{"TUBE"});
		CoupleAccordValeur cv11 = new CoupleAccordValeur(
				"Transport", new String[]{"Réfrigéré"});
		CoupleAccordValeur cv12 = new CoupleAccordValeur(
				"Traçabilité", new String[]{"Suivi constant"});
		CoupleAccordValeur cv13 = new CoupleAccordValeur(
				"Informations", new String[]{"Non communiquées"});
		LigneAccord li7 = new LigneAccord(
				new CoupleAccordValeur[]{cv7});
		LigneAccord li8 = new LigneAccord(
				new CoupleAccordValeur[]{cv8});
		LigneAccord li9 = new LigneAccord(
				new CoupleAccordValeur[]{cv9});
		LigneAccord li10 = new LigneAccord(
				new CoupleAccordValeur[]{cv10});
		LigneAccord li11 = new LigneAccord(
				new CoupleAccordValeur[]{cv11});
		LigneAccord li12 = new LigneAccord(
				new CoupleAccordValeur[]{cv12});
		LigneAccord li13 = new LigneAccord(
				new CoupleAccordValeur[]{cv13});
		BlocPrincipal bloc3 = new BlocPrincipal("Matériel biologique", 
				new LigneAccord[]{li7, li8, li9, li10, li11,
					li12, li13});
		xmlUtils.addTableau(page, new BlocPrincipal[]{
				bloc1, bloc2, bloc3});
		
		ValeursSignatures vs1 = new ValeursSignatures("Nom du responsable", 
				"Nom du responsable");
		ValeursSignatures vs3 = new ValeursSignatures("Signature pour accord", 
				"Signature pour accord");
		ValeursSignatures vs5 = new ValeursSignatures("Date", "Date");
		ListeSignature ligne = new ListeSignature(
				new ValeursSignatures[]{vs1});
		ListeSignature ligne3 = new ListeSignature(
				new ValeursSignatures[]{vs3});
		ListeSignature ligne5 = new ListeSignature(
				new ValeursSignatures[]{vs5});
		Signatures sign = new Signatures("EXPEDITEUR", "DESTINATAIRE",
				new ListeSignature[]{ligne, ligne3, ligne5});
		xmlUtils.addSignatures(page, sign);
		
		Element page2 = xmlUtils.addPage(root, "Accord de transfert");
		xmlUtils.addTableau(page2, new BlocPrincipal[]{
				bloc1, bloc2, bloc3});
		xmlUtils.addSignatures(page2, sign);
		
		return doc;
	}
	
	/**
	 * Test de la méthode createXMLFile().
	 */
	public void testCreateXMLAccord() {
		Document doc = creerDocumentAccord();
				
		String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
		String file = "testAccord.xml";
		xmlUtils.createXMLFile(doc, folder, file);
		
		String path = folder + file;
		File f = new File(path);
		assertTrue(f.exists());
		assertTrue(f.length() > 0);
		
		//f.delete();
	}
	
	public void testCreerAccordPdf() {
		// création du document contenant les infos
		Document doc = creerDocumentAccord();
		
		byte[] results = null;
		try {
			results = xmlUtils.creerAccordTransfertPdf(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertTrue(results.length > 0);
		
		String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
		String file = "testAccord.pdf";		
		String path = folder + file;
		
		File f = new File(path);
		FileOutputStream fo;
		try {
			fo = new FileOutputStream(f);
		
			for (int i = 0; i < results.length; i++) {
				fo.write(results[i]);
			}
			fo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
