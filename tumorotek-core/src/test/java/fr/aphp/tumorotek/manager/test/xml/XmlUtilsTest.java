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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.impl.xml.BlocPrincipal;
import fr.aphp.tumorotek.manager.impl.xml.BoiteContenu;
import fr.aphp.tumorotek.manager.impl.xml.BoiteImpression;
import fr.aphp.tumorotek.manager.impl.xml.CoupleAccordValeur;
import fr.aphp.tumorotek.manager.impl.xml.CoupleSimpleValeur;
import fr.aphp.tumorotek.manager.impl.xml.CoupleValeur;
import fr.aphp.tumorotek.manager.impl.xml.EnteteListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneAccord;
import fr.aphp.tumorotek.manager.impl.xml.LigneDeuxColonnesParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.LigneListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.LigneSimpleParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.ListeElement;
import fr.aphp.tumorotek.manager.impl.xml.ListeSignature;
import fr.aphp.tumorotek.manager.impl.xml.Paragraphe;
import fr.aphp.tumorotek.manager.impl.xml.Signatures;
import fr.aphp.tumorotek.manager.impl.xml.SousParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.ValeursSignatures;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.xml.XmlUtils;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 *
 * Classe de test pour le manager XmlUtils.
 * Classe créée le 09/07/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class XmlUtilsTest extends AbstractManagerTest4
{

   @Autowired
   private XmlUtils xmlUtils;

   @Autowired
   private PrelevementManager prelevementManager;

   @Autowired
   private EchantillonManager echantillonManager;

   @Autowired
   private TerminaleManager terminaleManager;
   //  @Autowired
   // private EmetteurDao emetteurDao;

   public XmlUtilsTest(){

   }

   @Test
   public void testCreateJDomDocument(){

      final Document doc = xmlUtils.createJDomDocument();
      assertNotNull(doc);
      assertNotNull(doc.getRootElement());
      assertTrue(doc.getRootElement().getName().equals("Impression"));

   }

   /**
    * Test la méthode addCoupleValeur.
    */

   @Test
   public void testAddCoupleValeur(){

      final Element ligne = new Element("LigneParagraphe");
      final CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
      xmlUtils.addCoupleValeur(ligne, cv1);
      final CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
      xmlUtils.addCoupleValeur(ligne, cv2);

      final List<?> children = ligne.getChildren("CoupleValeurs");
      assertTrue(children.size() == 2);
      final Element firstCouple = Element.class.cast(children.get(0));
      assertTrue(firstCouple.getChildText("NomValeur").equals("Nom"));
      assertTrue(firstCouple.getChildText("Valeur").equals("Valeur"));

      final Element secondCouple = Element.class.cast(children.get(1));
      assertTrue(secondCouple.getChildText("NomValeur").equals("Nom2"));
      assertTrue(secondCouple.getChildText("Valeur").equals("Valeur2"));

      xmlUtils.addCoupleValeur(null, cv2);
      xmlUtils.addCoupleValeur(ligne, null);
      assertTrue(ligne.getChildren("CoupleValeurs").size() == 2);
   }

   /**
    * Test la méthode addCoupleValeur.
    */

   @Test
   public void testAddCoupleSimpleValeur(){

      final Element ligne = new Element("LigneParagraphe");
      final CoupleSimpleValeur cv1 = new CoupleSimpleValeur("Nom", "Valeur");
      xmlUtils.addCoupleSimpleValeur(ligne, cv1);
      final CoupleSimpleValeur cv2 = new CoupleSimpleValeur("Nom2", "Valeur2");
      xmlUtils.addCoupleSimpleValeur(ligne, cv2);

      final List<?> children = ligne.getChildren("CoupleSimpleValeurs");
      assertTrue(children.size() == 2);
      final Element firstCouple = Element.class.cast(children.get(0));
      assertTrue(firstCouple.getChildText("NomValeur").equals("Nom"));
      assertTrue(firstCouple.getChildText("Valeur").equals("Valeur"));

      final Element secondCouple = Element.class.cast(children.get(1));
      assertTrue(secondCouple.getChildText("NomValeur").equals("Nom2"));
      assertTrue(secondCouple.getChildText("Valeur").equals("Valeur2"));

      xmlUtils.addCoupleSimpleValeur(null, cv2);
      xmlUtils.addCoupleSimpleValeur(ligne, null);
      assertTrue(ligne.getChildren("CoupleSimpleValeurs").size() == 2);
   }

   /**
    * Test la méthode addLigne.
    */

   @Test
   public void testAddLigne(){

      final Element sousP = new Element("SousParagraphe");
      final CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
      final CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
      final CoupleValeur cv3 = new CoupleValeur("Nom3", "Valeur3");
      final LigneParagraphe ligne = new LigneParagraphe("li1", new CoupleValeur[] {cv1, cv2, cv3});
      xmlUtils.addLigne(sousP, ligne);

      assertTrue(sousP.getChildren("Ligne").size() == 1);
      final Element ligneElt = (Element) sousP.getChildren("Ligne").get(0);
      final List<?> children = ligneElt.getChildren("LigneParagraphe");
      assertTrue(children.size() == 1);
      final Element ligneP = Element.class.cast(children.get(0));
      assertTrue(ligneP.getChildren("CoupleValeurs").size() == 3);
      final List<?> childrenL = ligneP.getChildren("CoupleValeurs");
      final Element firstCouple = Element.class.cast(childrenL.get(0));
      assertTrue(firstCouple.getChildText("NomValeur").equals("Nom"));
      assertTrue(firstCouple.getChildText("Valeur").equals("Valeur"));

      final Element secondCouple = Element.class.cast(childrenL.get(1));
      assertTrue(secondCouple.getChildText("NomValeur").equals("Nom2"));
      assertTrue(secondCouple.getChildText("Valeur").equals("Valeur2"));

      xmlUtils.addLigne(sousP, null);
      xmlUtils.addLigne(null, ligne);
      assertTrue(sousP.getChildren("Ligne").size() == 1);
   }

   /**
    * Test la méthode addLigneDeuxColonnesParagraphe.
    */

   @Test
   public void testAddLigneDeuxColonnesParagraphe(){

      final Element sousP = new Element("SousParagraphe");
      final CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
      final LigneDeuxColonnesParagraphe ligne = new LigneDeuxColonnesParagraphe(cv1);
      xmlUtils.addLigneDeuxColonnesParagraphe(sousP, ligne);

      assertTrue(sousP.getChildren("Ligne").size() == 1);
      final Element ligneElt = (Element) sousP.getChildren("Ligne").get(0);
      final List<?> children = ligneElt.getChildren("LigneDeuxColonnesParagraphe");
      assertTrue(children.size() == 1);
      final Element ligneP = Element.class.cast(children.get(0));
      assertTrue(ligneP.getChildren("CoupleValeurs").size() == 1);
      final List<?> childrenL = ligneP.getChildren("CoupleValeurs");
      final Element firstCouple = Element.class.cast(childrenL.get(0));
      assertTrue(firstCouple.getChildText("NomValeur").equals("Nom"));
      assertTrue(firstCouple.getChildText("Valeur").equals("Valeur"));

      xmlUtils.addLigneDeuxColonnesParagraphe(sousP, null);
      xmlUtils.addLigneDeuxColonnesParagraphe(null, ligne);
      assertTrue(sousP.getChildren("Ligne").size() == 1);
   }

   /**
    * Test la méthode addLigneSimple.
    */

   @Test
   public void testAddLigneSimple(){

      final Element sousP = new Element("SousParagraphe");
      final CoupleSimpleValeur cv1 = new CoupleSimpleValeur("Nom", "Valeur");

      final LigneSimpleParagraphe ligne = new LigneSimpleParagraphe(cv1);
      xmlUtils.addLigneSimple(sousP, ligne);

      assertTrue(sousP.getChildren("Ligne").size() == 1);
      final Element ligneElt = (Element) sousP.getChildren("Ligne").get(0);
      final List<?> children = ligneElt.getChildren("LigneSimpleParagraphe");
      assertTrue(children.size() == 1);
      final Element ligneP = Element.class.cast(children.get(0));
      assertTrue(ligneP.getChildren("CoupleSimpleValeurs").size() == 1);

      xmlUtils.addLigneSimple(sousP, null);
      xmlUtils.addLigneSimple(null, ligne);
      assertTrue(sousP.getChildren("Ligne").size() == 1);
   }

   /**
    * Test la méthode addLigneListe.
    */

   @Test
   public void testAddLigneListe(){

      final Element sousP = new Element("SousParagraphe");
      final LigneListe ligne = new LigneListe(new String[] {"Cell1", "Cell2", "Cell3"});
      xmlUtils.addLigneListe(sousP, ligne);

      final List<?> children = sousP.getChildren("LigneListe");
      assertTrue(children.size() == 1);
      final Element ligneP = Element.class.cast(children.get(0));
      assertTrue(ligneP.getChildren("Cellule").size() == 3);
      final List<?> childrenL = ligneP.getChildren("Cellule");
      final Element firstCellule = Element.class.cast(childrenL.get(0));
      assertTrue(firstCellule.getText().equals("Cell1"));

      final Element secCellule = Element.class.cast(childrenL.get(1));
      assertTrue(secCellule.getText().equals("Cell2"));

      xmlUtils.addLigneListe(sousP, null);
      xmlUtils.addLigneListe(null, ligne);
      assertTrue(sousP.getChildren("LigneListe").size() == 1);
   }

   /**
    * Test la méthode addColonnesListe.
    */

   @Test
   public void testAddColonnesListe(){

      final Element sousP = new Element("SousParagraphe");
      xmlUtils.addColonnesListe(sousP, 5);

      final List<?> children = sousP.getChildren("Colonnes");
      assertTrue(children.size() == 1);
      final Element entetel = Element.class.cast(children.get(0));
      assertTrue(entetel.getChildren("Colonne").size() == 5);

      xmlUtils.addColonnesListe(null, 5);
      assertTrue(sousP.getChildren("Colonnes").size() == 1);
   }

   /**
    * Test la méthode addEnteteListe.
    */

   @Test
   public void testAddEnteteListe(){

      final Element sousP = new Element("SousParagraphe");
      final EnteteListe entete = new EnteteListe(new String[] {"Col1", "Col2", "Col3"});
      xmlUtils.addEnteteListe(sousP, entete);

      final List<?> children = sousP.getChildren("EnteteListe");
      assertTrue(children.size() == 1);
      final Element entetel = Element.class.cast(children.get(0));
      assertTrue(entetel.getChildren("NomColonne").size() == 3);
      final List<?> childrenL = entetel.getChildren("NomColonne");
      final Element firstCol = Element.class.cast(childrenL.get(0));
      assertTrue(firstCol.getText().equals("Col1"));

      final Element secCol = Element.class.cast(childrenL.get(1));
      assertTrue(secCol.getText().equals("Col2"));

      xmlUtils.addEnteteListe(sousP, null);
      xmlUtils.addEnteteListe(null, entete);
      assertTrue(sousP.getChildren("EnteteListe").size() == 1);
   }

   /**
    * Test la méthode addSousParagraphe.
    */

   @Test
   public void testAddSousParagraphe(){

      final Element para = new Element("Paragraphe");
      final CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
      final CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
      final CoupleValeur cv3 = new CoupleValeur("Nom3", "Valeur3");
      final LigneParagraphe ligne1 = new LigneParagraphe("li1", new CoupleValeur[] {cv1, cv2});
      final LigneParagraphe ligne2 = new LigneParagraphe("li2", new CoupleValeur[] {cv3});
      final String inconnu = "Inconnu";
      final ListeElement liste = new ListeElement();
      final SousParagraphe sous = new SousParagraphe("Titre", new LigneParagraphe[] {ligne1, ligne2}, inconnu, liste);

      xmlUtils.addSousParagraphe(para, sous);

      List<?> children = para.getChildren("SousParagraphe");
      assertTrue(children.size() == 1);
      Element sousP = Element.class.cast(children.get(0));
      assertNotNull(sousP);
      assertTrue(sousP.getChildText("TitreSousParagraphe").equals("Titre"));
      assertTrue(sousP.getChildren("Ligne").size() == 2);
      assertTrue(sousP.getChildren("Inconnu").size() == 1);
      assertTrue(sousP.getChildren("Liste").size() == 1);

      final SousParagraphe sous2 = new SousParagraphe();
      xmlUtils.addSousParagraphe(para, sous2);
      children = para.getChildren("SousParagraphe");
      assertTrue(children.size() == 2);
      sousP = Element.class.cast(children.get(1));
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

   @Test
   public void testAddParagraphe(){

      final CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
      final CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
      final CoupleValeur cv3 = new CoupleValeur("Nom3", "Valeur3");
      final CoupleValeur cv4 = new CoupleValeur("Nom4", "Valeur4");
      final LigneParagraphe ligne1 = new LigneParagraphe("li1", new CoupleValeur[] {cv1, cv2});
      final LigneParagraphe ligne2 = new LigneParagraphe("li2", new CoupleValeur[] {cv3});
      final SousParagraphe sous = new SousParagraphe("Titre", new LigneParagraphe[] {ligne1, ligne2}, null, null);
      final SousParagraphe sous2 = new SousParagraphe();
      final LigneParagraphe ligne4 = new LigneParagraphe("li4", new CoupleValeur[] {cv4});
      final String inconnu = "Inconnu";
      final ListeElement liste = new ListeElement();

      final Paragraphe para =
         new Paragraphe("Titre", new LigneParagraphe[] {ligne4}, new SousParagraphe[] {sous, sous2}, inconnu, liste);
      final Element page = new Element("Page");

      xmlUtils.addParagraphe(page, para);

      List<?> children = page.getChildren("Paragraphe");
      assertTrue(children.size() == 1);
      Element p = Element.class.cast(children.get(0));
      assertNotNull(p);
      assertTrue(p.getChildText("TitreParagraphe").equals("Titre"));
      assertTrue(p.getChildren("Ligne").size() == 1);
      assertTrue(p.getChildren("SousParagraphe").size() == 2);
      assertTrue(p.getChildren("Inconnu").size() == 1);
      assertTrue(p.getChildren("Liste").size() == 1);

      final Paragraphe para2 = new Paragraphe();
      xmlUtils.addParagraphe(page, para2);
      children = page.getChildren("Paragraphe");
      assertTrue(children.size() == 2);
      p = Element.class.cast(children.get(1));
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

   @Test
   public void testAddListe(){

      final LigneListe ligne1 = new LigneListe(new String[] {"Cell1", "Cell2", "Cell3"});
      final LigneListe ligne2 = new LigneListe(new String[] {"Bell1", "Bell2", "Bell3"});
      final LigneListe ligne3 = new LigneListe(new String[] {"Tell1", "Tell2", "Tell3"});
      final LigneListe ligne4 = new LigneListe(new String[] {"Aell1", "Aell2", "Aell3"});

      final EnteteListe entete = new EnteteListe(new String[] {"Col1", "Col2", "Col3"});

      final ListeElement liste = new ListeElement("Liste", entete, new LigneListe[] {ligne1, ligne2, ligne3, ligne4});
      final Element page = new Element("Page");
      xmlUtils.addListe(page, liste);

      List<?> children = page.getChildren("Liste");
      assertTrue(children.size() == 1);
      Element p = Element.class.cast(children.get(0));
      assertNotNull(p);
      assertTrue(p.getChildText("TitreParagraphe").equals("Liste"));
      assertTrue(p.getChildren("Colonnes").size() == 1);
      assertTrue(p.getChildren("EnteteListe").size() == 1);
      assertTrue(p.getChildren("LigneListe").size() == 4);

      final ListeElement liste2 = new ListeElement();
      xmlUtils.addListe(page, liste2);
      children = page.getChildren("Liste");
      assertTrue(children.size() == 2);
      p = Element.class.cast(children.get(1));
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
   @Test
   public void testAddPage(){
      final Document doc = xmlUtils.createJDomDocument();
      final Element root = doc.getRootElement();
      final String titre = "Titre";

      final Element page = xmlUtils.addPage(root, titre);
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
   @Test
   public void testAddTitreForDocument(){
      final Document doc = xmlUtils.createJDomDocument();
      final Element root = doc.getRootElement();
      xmlUtils.addTitreForDocument(root, null);
      assertTrue(root.getChildText("Titre").equals(""));

      final Document doc2 = xmlUtils.createJDomDocument();
      final Element root2 = doc2.getRootElement();
      xmlUtils.addTitreForDocument(root2, "TITRE");
      assertTrue(root2.getChildText("Titre").equals("TITRE"));
   }

   /**
    * Test de la méthode createXMLFile().
    */
   @Test
   public void testCreateXMLFile(){
      final Document doc = xmlUtils.createJDomDocument();
      final Element root = doc.getRootElement();
      final Element page = xmlUtils.addPage(root, "TITRE");

      final CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
      cv1.setObligatoire(true);
      final CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
      cv2.setAnonyme(true);
      final CoupleValeur cv3 = new CoupleValeur("Nom3", "Valeur3");
      final CoupleValeur cv4 = new CoupleValeur("Nom4", "Valeur4");
      final LigneParagraphe ligne1 = new LigneParagraphe("li1", new CoupleValeur[] {cv1, cv2});
      final LigneParagraphe ligne2 = new LigneParagraphe("li2", new CoupleValeur[] {cv3});
      final SousParagraphe sous = new SousParagraphe("Titre", new LigneParagraphe[] {ligne1, ligne2}, null, null);
      final SousParagraphe sous2 = new SousParagraphe();
      final LigneParagraphe ligne4 = new LigneParagraphe("li4", new CoupleValeur[] {cv4});

      final Paragraphe para =
         new Paragraphe("Titre", new LigneParagraphe[] {ligne4}, new SousParagraphe[] {sous, sous2}, null, null);
      xmlUtils.addParagraphe(page, para);

      final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
      final String file = "test.xml";
      xmlUtils.createXMLFile(doc, folder, file);

      final String path = folder + file;
      final File f = new File(path);
      assertTrue(f.exists());
      assertTrue(f.length() > 0);

      //f.delete();
   }

   /**
    * Test de la méthode createXMLFile().
    */
   @Test
   public void testTransformAsPdf(){
      final Document doc = xmlUtils.createJDomDocument();
      final Element root = doc.getRootElement();
      final Element page = xmlUtils.addPage(root, "TITRE");

      final CoupleValeur cv1 = new CoupleValeur("Nom", "Valeur");
      final CoupleValeur cv2 = new CoupleValeur("Nom2", "Valeur2");
      final CoupleValeur cv3 = new CoupleValeur("Nom3", "Valeur3");
      final CoupleValeur cv4 = new CoupleValeur("Nom4", "Valeur4");
      final LigneParagraphe ligne1 = new LigneParagraphe("li1", new CoupleValeur[] {cv1, cv2});
      final LigneParagraphe ligne2 = new LigneParagraphe("li2", new CoupleValeur[] {cv3});
      final SousParagraphe sous = new SousParagraphe("Titre", new LigneParagraphe[] {ligne1, ligne2}, null, null);
      final LigneParagraphe ligne4 = new LigneParagraphe("li4", new CoupleValeur[] {cv4});

      final Paragraphe para = new Paragraphe("Titre", new LigneParagraphe[] {ligne4}, new SousParagraphe[] {sous}, null, null);
      xmlUtils.addParagraphe(page, para);

      final org.w3c.dom.Document transformed = xmlUtils.transformAsPdf(doc);

      assertNotNull(transformed);
   }

   public Document creerDocument(){
      final Prelevement p = prelevementManager.findByIdManager(1);

      final Document doc = xmlUtils.createJDomDocument();
      final Element root = doc.getRootElement();
      final Element page = xmlUtils.addPage(root, null);
      xmlUtils.addBasDePage(root, "Fiche Prélèvement - 15/07/2010");
      xmlUtils.addHautDePage(root, "Fiche d'impression Tumorotek", true,
         this.getClass().getClassLoader().getResource("data/empImpressionUSE.gif").getPath());

      // Partie principale
      final CoupleValeur cp1 = new CoupleValeur("Code de prélèvement", p.getCode());
      cp1.setObligatoire(true);
      final CoupleValeur cp2 = new CoupleValeur("N° de Laboratorie", "Numéro interne du prélèvement du" + " laboratoire");
      cp2.setAnonyme(false);
      final CoupleSimpleValeur cp3 = new CoupleSimpleValeur("Nature", "AAAAAAAAA AAAAAAAA AAAAAAAA AAAAAAAA AAAAAAAAAA");
      final CoupleSimpleValeur cp3Bis =
         new CoupleSimpleValeur("Nature pour le prélèvement actuel", "AAAAAAAAA AAAAAAAA AAAAAAAA AAAAAAAA AAAAAAAAAA");
      final LigneParagraphe li1 = new LigneParagraphe("li1", new CoupleValeur[] {cp1, cp2});
      final LigneSimpleParagraphe li1Bis = new LigneSimpleParagraphe(cp3);
      final LigneSimpleParagraphe li1Bis2 = new LigneSimpleParagraphe(cp3Bis);
      final LigneDeuxColonnesParagraphe lic = new LigneDeuxColonnesParagraphe(cp1);
      final CoupleValeur cp1Bis = new CoupleValeur("Value de test trop longue pour une colonne", "Et HOP !!!!!");
      final LigneDeuxColonnesParagraphe lic2 = new LigneDeuxColonnesParagraphe(cp1Bis);
      final Paragraphe par1 = new Paragraphe(null, new Object[] {li1Bis, li1, li1Bis2, lic, lic2}, null, null, null);
      xmlUtils.addParagraphe(page, par1);
      xmlUtils.addParagraphe(page, par1);
      xmlUtils.addParagraphe(page, par1);
      xmlUtils.addParagraphe(page, par1);

      // Partie Patient
      final CoupleValeur cp4 = new CoupleValeur("N° Patient", p.getMaladie().getPatient().getNip());
      cp4.setObligatoire(true);
      final CoupleValeur cp5 = new CoupleValeur("N° de dossier", p.getPatientNda());
      final CoupleValeur cp6 = new CoupleValeur("Nom usuel", p.getMaladie().getPatient().getNom());
      cp6.setObligatoire(true);
      final CoupleValeur cp7 = new CoupleValeur("Prénom", p.getMaladie().getPatient().getPrenom());
      final CoupleValeur cp8 = new CoupleValeur("Date de naissance", "08/10/1983");
      final CoupleValeur cp9 = new CoupleValeur("Sexe", p.getMaladie().getPatient().getSexe());
      final LigneParagraphe li2 = new LigneParagraphe("li2", new CoupleValeur[] {cp4, cp5});
      final LigneParagraphe li3 = new LigneParagraphe("li3", new CoupleValeur[] {cp6, cp7});
      final LigneParagraphe li4 = new LigneParagraphe("li4", new CoupleValeur[] {cp8, cp9});

      final CoupleValeur cp10 = new CoupleValeur("Libellé", p.getMaladie().getLibelle());
      final CoupleValeur cp11 = new CoupleValeur("Code diagnostic", p.getMaladie().getCode());
      final LigneParagraphe li5 = new LigneParagraphe("li5", new CoupleValeur[] {cp10, cp11});
      final SousParagraphe sousPar1 = new SousParagraphe("Informations maladie", new LigneParagraphe[] {li5}, null, null);
      final Paragraphe par2 = new Paragraphe("Informations patient", new LigneParagraphe[] {li2, li3, li4},
         new SousParagraphe[] {sousPar1}, null, null);
      xmlUtils.addParagraphe(page, par2);

      // Informations prélèvement
      final CoupleValeur cp12 = new CoupleValeur("Date et heure de prélèvement", p.getDatePrelevement().getTime().toString());
      final CoupleValeur cp13 = new CoupleValeur("Type de prélèvement", p.getPrelevementType().getNom());
      final CoupleValeur cp14 = new CoupleValeur("Stérile", p.getSterile().toString());
      final CoupleValeur cp15 = new CoupleValeur("Date de congélation", "07/07/2010");
      final CoupleValeur cp16 = new CoupleValeur("Etablissement", p.getServicePreleveur().getEtablissement().getNom());
      final CoupleValeur cp17 = new CoupleValeur("Service", p.getServicePreleveur().getNom());
      final CoupleValeur cp18 = new CoupleValeur("Préleveur", p.getPreleveur().getNomAndPrenom());
      final LigneParagraphe li6 = new LigneParagraphe("", new CoupleValeur[] {cp12, cp13});
      final LigneParagraphe li7 = new LigneParagraphe("", new CoupleValeur[] {cp14, cp15});
      final LigneParagraphe li8 = new LigneParagraphe("", new CoupleValeur[] {cp16});
      final LigneParagraphe li9 = new LigneParagraphe("", new CoupleValeur[] {cp17});
      final LigneParagraphe li10 = new LigneParagraphe("", new CoupleValeur[] {cp18});
      // conditionnement
      final CoupleValeur cp19 = new CoupleValeur("Type de conditionnement", p.getConditType().getNom());
      final CoupleValeur cp20 = new CoupleValeur("Nombre", p.getConditNbr().toString());
      final CoupleSimpleValeur cp21 = new CoupleSimpleValeur("Milieu", p.getConditMilieu().getNom());
      final LigneParagraphe li11 = new LigneParagraphe("", new CoupleValeur[] {cp19, cp20});
      final LigneSimpleParagraphe li12 = new LigneSimpleParagraphe(cp21);
      final SousParagraphe sousPar2 = new SousParagraphe("Conditionnement", new Object[] {li11, li12}, null, null);
      // statut juridique
      final CoupleValeur cp22 = new CoupleValeur("Statut juridique", p.getConsentType().getNom());
      final CoupleValeur cp23 = new CoupleValeur("Date", "07/10/1995");
      final LigneParagraphe li13 = new LigneParagraphe("", new CoupleValeur[] {cp22, cp23});
      final SousParagraphe sousPar3 = new SousParagraphe("Statut juridique", new LigneParagraphe[] {li13}, null, null);

      final Paragraphe par3 = new Paragraphe("Informations prélèvement", new LigneParagraphe[] {li6, li7, li8, li9, li10},
         new SousParagraphe[] {sousPar2, sousPar3}, null, null);
      xmlUtils.addParagraphe(page, par3);
      xmlUtils.addParagraphe(page, par3);
      xmlUtils.addParagraphe(page, par3);
      xmlUtils.addParagraphe(page, par3);
      xmlUtils.addParagraphe(page, par3);
      xmlUtils.addParagraphe(page, par3);

      // Site intermédiaire
      final SousParagraphe sousPar4 = new SousParagraphe("Départ du site préleveur", null, "Site de départ inconnu", null);
      final EnteteListe enteteS = new EnteteListe(new String[] {"Date arrivée", "Date départ", "Laboratoire", "Température"});
      final LigneListe ligneS1 = new LigneListe(new String[] {"08/04/2010", "09/04/2010", "HEMATO", "-10.5"});
      final LigneListe ligneS2 = new LigneListe(new String[] {"13/05/2010", "15/05/2010", "CELLULO", "-8.5"});
      final ListeElement listeSite = new ListeElement(null, enteteS, new LigneListe[] {ligneS1, ligneS2});
      final SousParagraphe sousPar5 = new SousParagraphe("Sites intermédiaires", null, null, listeSite);
      final Paragraphe par4 = new Paragraphe("Transfert du site préleveur " + "vers le site de stockage", null,
         new SousParagraphe[] {sousPar4, sousPar5}, null, null);
      xmlUtils.addParagraphe(page, par4);

      // Liste d'échantillons
      final EnteteListe entete = new EnteteListe(new String[] {"Code", "Date stockage", "Type", "Statut"});
      final Echantillon e1 = echantillonManager.findByIdManager(1);
      final LigneListe ligneL1 = new LigneListe(
         new String[] {e1.getCode(), "09/07/2010", e1.getEchantillonType().getNom(), e1.getObjetStatut().getStatut()});
      final Echantillon e2 = echantillonManager.findByIdManager(2);
      final LigneListe ligneL2 = new LigneListe(new String[] {e2.getCode(), e2.getDateStock().getTime().toString(),
         e2.getEchantillonType().getNom(), e2.getObjetStatut().getStatut()});
      final ListeElement listeEchan = new ListeElement("Echantillons (2)", entete,
         new LigneListe[] {ligneL1, ligneL2, ligneL2, ligneL2, ligneL1, ligneL2, ligneL2, ligneL2, ligneL1, ligneL2, ligneL2,
            ligneL2, ligneL1, ligneL2, ligneL2, ligneL2, ligneL1, ligneL2, ligneL2, ligneL2, ligneL1, ligneL2, ligneL2, ligneL2,
            ligneL1, ligneL2, ligneL2, ligneL2, ligneL1, ligneL2, ligneL2, ligneL2});
      xmlUtils.addListe(page, listeEchan);
      xmlUtils.addListe(page, listeEchan);

      return doc;
   }

   public Document creerDocumentBoite(){
      final Document doc = xmlUtils.createJDomDocumentBoites();
      final Element root = doc.getRootElement();
      final Element page = xmlUtils.addPageBoite(root, "Déstockage des échantillons");

      final Terminale boite = terminaleManager.findByIdManager(1);
      final List<String> instructions = new ArrayList<>();
      instructions.add("Ouvrir le conteneur n° C1");
      instructions.add("Ouvrir le compartiment n° A.");
      final List<String> elements = new ArrayList<>();
      elements.add("1: Echantillon n° : TESTPP.1");
      final List<Integer> positions = new ArrayList<>();
      positions.add(2);
      final BoiteImpression boiteI = new BoiteImpression(boite, elements, instructions, "Pris", "Selectionnes", "Vide",
         "Boite : B1", positions, "Instructions a faire", "Récupération des échantillons", "Visualisation");
      boiteI.setTitreIntermediaire("Echantillons");
      boiteI.setSeparateur(true);
      xmlUtils.addBoite(page, boiteI, "");

      return doc;
   }

   public Document creerDocumentContenu(){
      final Document doc = xmlUtils.createJDomDocumentContenuBoite();
      final Element root = doc.getRootElement();
      final Element page = xmlUtils.addPageContenuBoite(root, "Impression d'une boîte");

      final Terminale boite = terminaleManager.findByIdManager(1);
      final List<String> parents = new ArrayList<>();
      parents.add("Conteneur : C1");
      parents.add("Compartiment : A");
      parents.add("Boîte : A1");
      final List<String> nombres = new ArrayList<>();
      nombres.add("Nombre d'échantillons : 95");
      nombres.add("Nombre de patients : 25");
      final BoiteContenu boiteC = new BoiteContenu(boite, nombres, parents, "Vide");

      xmlUtils.addBoiteContenu(page, boiteC, "", new Hashtable<String, String>(), "VERT", new Hashtable<String, String>(),
         "VERT");

      return doc;
   }

   @Test
   public void testCreerPdf(){
      // création du document contenant les infos
      final Document doc = creerDocument();

      byte[] results = null;
      try{
         results = xmlUtils.creerPdf(doc);
         assertTrue(results.length > 0);

         final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
         final String file = "test.pdf";
         final String path = folder + file;

         final File f = new File(path);
         FileOutputStream fo = new FileOutputStream(f);

         for(int i = 0; i < results.length; i++){
            fo.write(results[i]);
         }
         fo.close();
      }catch(final IOException e){
         e.printStackTrace();
      }catch(final Exception e){
         e.printStackTrace();
      }

   }

   @Test
   public void testCreerHtml(){
      final Document doc = creerDocument();

      byte[] results = null;
      try{
         results = xmlUtils.creerHtml(doc);

         assertTrue(results.length > 0);

         final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
         final String file = "testBoite.html";
         final String path = folder + file;
         final File f = new File(path);
         java.io.FileWriter fw;
         fw = new java.io.FileWriter(f);

         for(int i = 0; i < results.length; i++){
            fw.write(results[i]);
         }
         fw.close();
      }catch(final IOException e){
         e.printStackTrace();
      }catch(final Exception e){
         e.printStackTrace();
      }
   }

   /***********************************************************/
   /************ Modelisation des boites **********************/
   /***********************************************************/

   /**
    * Test la méthode createJDomDocumentBoites.
    */
   @Test
   public void testCreateJDomDocumentBoites(){

      final Document doc = xmlUtils.createJDomDocumentBoites();
      assertNotNull(doc);
      assertNotNull(doc.getRootElement());
      assertTrue(doc.getRootElement().getName().equals("ImpressionBoite"));

   }

   /**
    * Test de la méthode addPageBoite().
    */
   @Test
   public void testAddPageBoite(){
      final Document doc = xmlUtils.createJDomDocumentBoites();
      final Element root = doc.getRootElement();
      final String titre = "Titre";

      final Element page = xmlUtils.addPageBoite(root, titre);
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
   @Test
   public void testAddTitreIntermediaire(){
      final Document doc = xmlUtils.createJDomDocumentBoites();
      final Element root = doc.getRootElement();
      xmlUtils.addTitreIntermediaire(root, null);
      assertNull(root.getChildText("TitreIntermediaire"));

      final Document doc2 = xmlUtils.createJDomDocumentBoites();
      final Element root2 = doc2.getRootElement();
      xmlUtils.addTitreIntermediaire(root2, "TITRE");
      assertTrue(root2.getChildText("TitreIntermediaire").equals("TITRE"));
   }

   /**
    * Test de la méthode addSeparateur().
    */
   @Test
   public void testAddSeparateur(){
      final Document doc = xmlUtils.createJDomDocumentBoites();
      final Element root = doc.getRootElement();
      xmlUtils.addSeparateur(null);
      assertTrue(root.getChildren("Separateur").size() == 0);

      final Document doc2 = xmlUtils.createJDomDocumentBoites();
      final Element root2 = doc2.getRootElement();
      xmlUtils.addSeparateur(root2);
      assertTrue(root2.getChildren("Separateur").size() == 1);
   }

   /**
    * Test la méthode addInstructions.
    */

   @Test
   public void testAddInstructions(){

      final Element page = new Element("PageBoite");
      final List<String> instructionsListe = new ArrayList<>();
      instructionsListe.add("Test");
      instructionsListe.add("test1");
      instructionsListe.add("test3");
      xmlUtils.addInstructions(page, instructionsListe);

      final List<?> children = page.getChildren("Instructions");
      assertTrue(children.size() == 1);
      final Element instructions = Element.class.cast(children.get(0));
      assertTrue(instructions.getChildren("Instruction").size() == 3);
      final List<?> childrenI = instructions.getChildren("Instruction");
      final Element first = Element.class.cast(childrenI.get(0));
      assertTrue(first.getText().equals("Test"));

      final Element second = Element.class.cast(childrenI.get(1));
      assertTrue(second.getText().equals("test1"));

      xmlUtils.addInstructions(page, null);
      xmlUtils.addInstructions(null, instructionsListe);
      assertTrue(page.getChildren("Instructions").size() == 1);
   }

   /**
    * Test la méthode addListeElements.
    */

   @Test
   public void testAddListeElements(){

      final Element page = new Element("PageBoite");
      final List<String> elementsListe = new ArrayList<>();
      elementsListe.add("Elt1");
      elementsListe.add("Elt2");
      elementsListe.add("Elt3");
      xmlUtils.addListeElements(page, elementsListe, "Titre");

      final List<?> children = page.getChildren("ListeElements");
      assertTrue(children.size() == 1);
      final Element listeElements = Element.class.cast(children.get(0));
      assertTrue(listeElements.getChildText("TitreListe").equals("Titre"));

      assertTrue(listeElements.getChildren("Element").size() == 3);
      final List<?> childrenElt = listeElements.getChildren("Element");
      final Element first = Element.class.cast(childrenElt.get(0));
      assertTrue(first.getText().equals("Elt1"));

      final Element second = Element.class.cast(childrenElt.get(1));
      assertTrue(second.getText().equals("Elt2"));

      xmlUtils.addListeElements(page, null, null);
      xmlUtils.addListeElements(null, elementsListe, null);
      assertTrue(page.getChildren("ListeElements").size() == 1);
   }

   /**
    * Test la méthode createModelisation.
    */

   @Test
   public void testCreateModelisation(){

      final Element page = new Element("PageBoite");
      final Terminale t1 = terminaleManager.findByIdManager(1);
      final List<Integer> pos = new ArrayList<>();
      pos.add(2);
      xmlUtils.createModelisation(page, t1, "Boite : BT1", pos, "Vide", "Pris", "Selectionne", "");

      final List<?> children = page.getChildren("Modelisation");
      assertTrue(children.size() == 1);
      final Element modele = Element.class.cast(children.get(0));
      assertTrue(modele.getChildText("TitreBoite").equals("Boite : BT1"));

      assertTrue(modele.getChildren("LigneBoite").size() == 10);
      final List<?> childrenElt = modele.getChildren("LigneBoite");

      final Element firstLigne = Element.class.cast(childrenElt.get(0));
      final List<?> emps = firstLigne.getChildren("Emplacement");
      assertTrue(emps.size() == 10);
      assertTrue(Element.class.cast(emps.get(0)).getChildren("EmpOccupe").size() == 1);
      assertTrue(Element.class.cast(emps.get(1)).getChildren("EmpSelectionne").size() == 1);
      assertTrue(Element.class.cast(emps.get(5)).getChildren("EmpLibre").size() == 1);

      assertTrue(modele.getChildren("LegendeVide").size() == 1);
      assertTrue(modele.getChildren("LegendePris").size() == 1);
      assertTrue(modele.getChildren("LegendeSelectionne").size() == 1);

   }

   /**
    * Test la méthode addBoite.
    */

   @Test
   public void testAddBoite(){

      final Element page = new Element("PageBoite");
      final Terminale t1 = terminaleManager.findByIdManager(1);
      final List<String> instructionsL = new ArrayList<>();
      instructionsL.add("In1");
      instructionsL.add("In2");
      final List<String> elementsL = new ArrayList<>();
      elementsL.add("Elt1");
      elementsL.add("Elt1");
      elementsL.add("Elt3");
      final List<Integer> positions = new ArrayList<>();
      positions.add(2);
      final BoiteImpression boiteI = new BoiteImpression(t1, elementsL, instructionsL, "Pris", "Selectionne", "Vide",
         "Boite : B1", positions, "Instructions", "Récupération des échantillons", "Visualisation");
      xmlUtils.addBoite(page, boiteI, "");

      final List<?> children = page.getChildren("Boite");
      assertTrue(children.size() == 1);
      final Element boite = Element.class.cast(children.get(0));
      assertTrue(boite.getChildren("Modelisation").size() == 1);

      final List<?> instructions = boite.getChildren("Instructions");
      assertTrue(Element.class.cast(instructions.get(0)).getChildren("Instruction").size() == 2);

      final List<?> elements = boite.getChildren("ListeElements");
      assertTrue(Element.class.cast(elements.get(0)).getChildren("Element").size() == 3);

      assertTrue(boite.getChildren("TitreModelisation").size() == 1);
      assertTrue(boite.getChildren("TitreInstruction").size() == 1);

   }

   /**
    * Test de la méthode createXMLFile().
    */
   @Test
   public void testCreateXMLFileBoite(){
      final Document doc = creerDocumentBoite();

      //FIXME Fichier crée dans le dossier src ? Commité inutilement ?
      final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
      final String file = "testBoite.xml";
      xmlUtils.createXMLFile(doc, folder, file);

      final String path = folder + file;
      final File f = new File(path);
      assertTrue(f.exists());
      assertTrue(f.length() > 0);

      f.delete();
   }

   @Test
   public void testCreerBoiteHtml(){
      final Document doc = creerDocumentBoite();

      byte[] results = null;
      try{
         results = xmlUtils.creerBoiteHtml(doc);
         assertTrue(results.length > 0);

         final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
         final String file = "testBoite.html";
         final String path = folder + file;
         final File f = new File(path);
         java.io.FileWriter fw;
         fw = new java.io.FileWriter(f);

         for(int i = 0; i < results.length; i++){
            fw.write(results[i]);
         }
         fw.close();
      }catch(final IOException e){
         e.printStackTrace();
      }catch(final Exception e){
         e.printStackTrace();
      }

   }

   @Test
   public void testEquals(){
      final Terminale boite = terminaleManager.findByIdManager(1);
      final List<String> instructions = new ArrayList<>();
      instructions.add("Ouvrir le conteneur n° C1");
      instructions.add("Ouvrir le compartiment n° A.");
      final List<String> elements = new ArrayList<>();
      elements.add("1: Echantillon n° : TESTPP.1");
      final List<Integer> positions = new ArrayList<>();
      positions.add(2);
      final BoiteImpression boiteI = new BoiteImpression(boite, elements, instructions, "Pris", "Selectionnes", "Vide",
         "Boite : B1", positions, "Instructions a faire", "Récupération des échantillons", "Visualisation");

      final BoiteImpression boiteI2 = new BoiteImpression(boite, elements, instructions, "Pris", "Selectionnes", "Vide",
         "Boite : B1", positions, "Instructions a faire", "Récupération des échantillons", "Visualisation");

      assertTrue(boiteI.equals(boiteI2));
      boiteI2.setNom("***");
      assertTrue(boiteI.equals(boiteI2));
      final Terminale boite3 = terminaleManager.findByIdManager(3);
      boiteI2.setBoite(boite3);
      assertFalse(boiteI.equals(boiteI2));
      assertFalse(boiteI.equals(null));
   }

   @Test
   public void testHashCode(){
      final Terminale boite = terminaleManager.findByIdManager(1);
      final List<String> instructions = new ArrayList<>();
      instructions.add("Ouvrir le conteneur n° C1");
      instructions.add("Ouvrir le compartiment n° A.");
      final List<String> elements = new ArrayList<>();
      elements.add("1: Echantillon n° : TESTPP.1");
      final List<Integer> positions = new ArrayList<>();
      positions.add(2);
      final BoiteImpression boiteI = new BoiteImpression(boite, elements, instructions, "Pris", "Selectionnes", "Vide",
         "Boite : B1", positions, "Instructions a faire", "Récupération des échantillons", "Visualisation");

      final BoiteImpression boiteI2 = new BoiteImpression(boite, elements, instructions, "Pris", "Selectionnes", "Vide",
         "Boite : B1", positions, "Instructions a faire", "Récupération des échantillons", "Visualisation");

      assertTrue(boiteI.hashCode() == boiteI2.hashCode());
      final int val = boiteI.hashCode();
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
   @Test
   public void testCreateJDomDocumentContenuBoite(){

      final Document doc = xmlUtils.createJDomDocumentContenuBoite();
      assertNotNull(doc);
      assertNotNull(doc.getRootElement());
      assertTrue(doc.getRootElement().getName().equals("ImpressionContenu"));

   }

   /**
    * Test de la méthode addPageContenuBoite().
    */
   @Test
   public void testAddPageContenuBoite(){
      final Document doc = xmlUtils.createJDomDocumentContenuBoite();
      final Element root = doc.getRootElement();
      final String titre = "Titre";

      final Element page = xmlUtils.addPageContenuBoite(root, titre);
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

   @Test
   public void testAddListeParents(){

      final Element page = new Element("PageContenuBoite");
      final List<String> parentsListe = new ArrayList<>();
      parentsListe.add("Test");
      parentsListe.add("test1");
      parentsListe.add("test3");
      xmlUtils.addListeParents(page, parentsListe);

      final List<?> children = page.getChildren("ListeParents");
      assertTrue(children.size() == 1);
      final Element parents = Element.class.cast(children.get(0));
      assertTrue(parents.getChildren("Parent").size() == 3);
      final List<?> childrenI = parents.getChildren("Parent");
      final Element first = Element.class.cast(childrenI.get(0));
      assertTrue(first.getText().equals("Test"));

      final Element second = Element.class.cast(childrenI.get(1));
      assertTrue(second.getText().equals("test1"));

      xmlUtils.addListeParents(page, null);
      xmlUtils.addListeParents(null, parentsListe);
      assertTrue(page.getChildren("ListeParents").size() == 1);
   }

   /**
    * Test la méthode addListeNombres.
    */

   @Test
   public void testAddListeNombres(){

      final Element page = new Element("PageBoite");
      final List<String> nombresListe = new ArrayList<>();
      nombresListe.add("Elt1");
      nombresListe.add("Elt2");
      nombresListe.add("Elt3");
      xmlUtils.addListeNombres(page, nombresListe);

      final List<?> children = page.getChildren("ListeNombres");
      assertTrue(children.size() == 1);
      final Element listeElements = Element.class.cast(children.get(0));

      assertTrue(listeElements.getChildren("Nombre").size() == 3);
      final List<?> childrenElt = listeElements.getChildren("Nombre");
      final Element first = Element.class.cast(childrenElt.get(0));
      assertTrue(first.getText().equals("Elt1"));

      final Element second = Element.class.cast(childrenElt.get(1));
      assertTrue(second.getText().equals("Elt2"));

      xmlUtils.addListeNombres(page, null);
      xmlUtils.addListeNombres(null, nombresListe);
      assertTrue(page.getChildren("ListeNombres").size() == 1);
   }

   /**
    * Test la méthode createContenu.
    */

   @Test
   public void testCreateContenu(){

      final Element page = new Element("PageContenuBoite");
      final Terminale t1 = terminaleManager.findByIdManager(1);
      xmlUtils.createContenu(page, t1, "Vide", "", new Hashtable<String, String>(), "VERT", new Hashtable<String, String>(),
         "VERT");

      final List<?> children = page.getChildren("Modelisation");
      assertTrue(children.size() == 1);
      final Element modele = Element.class.cast(children.get(0));

      assertTrue(modele.getChildren("LigneBoite").size() == 10);
      final List<?> childrenElt = modele.getChildren("LigneBoite");

      final Element firstLigne = Element.class.cast(childrenElt.get(0));
      final List<?> emps = firstLigne.getChildren("Emplacement");
      assertTrue(emps.size() == 10);
      assertTrue(Element.class.cast(emps.get(0)).getChildren("EmpOccupe").size() == 1);
      assertTrue(Element.class.cast(emps.get(1)).getChildren("EmpOccupe").size() == 1);
      assertTrue(Element.class.cast(emps.get(5)).getChildren("EmpLibre").size() == 1);

   }

   /**
    * Test la méthode addBoiteContenu.
    */

   @Test
   public void testAddBoiteContenu(){

      final Element page = new Element("PageBoite");
      final Terminale t1 = terminaleManager.findByIdManager(1);
      final List<String> listeParents = new ArrayList<>();
      listeParents.add("Conteneur : C1");
      listeParents.add("Compartiment : A");
      final List<String> listeNombres = new ArrayList<>();
      listeNombres.add("23 Echantillons");
      listeNombres.add("20 Dérivés");
      listeNombres.add("15 patients");
      final BoiteContenu boiteC = new BoiteContenu(t1, listeNombres, listeParents, "Vide");
      xmlUtils.addBoiteContenu(page, boiteC, "", new Hashtable<String, String>(), "VERT", new Hashtable<String, String>(),
         "VERT");

      final List<?> children = page.getChildren("Contenu");
      assertTrue(children.size() == 1);
      final Element boite = Element.class.cast(children.get(0));
      assertTrue(boite.getChildren("Modelisation").size() == 1);

      final List<?> instructions = boite.getChildren("ListeNombres");
      assertTrue(Element.class.cast(instructions.get(0)).getChildren("Nombre").size() == 3);

      final List<?> elements = boite.getChildren("ListeParents");
      assertTrue(Element.class.cast(elements.get(0)).getChildren("Parent").size() == 2);

   }

   /**
    * Test de la méthode createXMLFile().
    */
   @Test
   public void testCreateXMLFileContenu(){
      final Document doc = creerDocumentContenu();

      //FIXME Fichier crée dans le dossier src ? Commité inutilement ?
      final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
      final String file = "testContenu.xml";
      xmlUtils.createXMLFile(doc, folder, file);

      final String path = folder + file;
      final File f = new File(path);
      assertTrue(f.exists());
      assertTrue(f.length() > 0);

      f.delete();
   }

   @Test
   public void testCreerContenuHtml(){
      final Document doc = creerDocumentContenu();

      byte[] results = null;
      try{
         results = xmlUtils.creerContenuHtml(doc);

         assertTrue(results.length > 0);

         final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
         final String file = "testContenu.html";
         final String path = folder + file;
         final File f = new File(path);
         FileWriter fw = new FileWriter(f);

         for(int i = 0; i < results.length; i++){
            fw.write(results[i]);
         }
         fw.close();
      }catch(final IOException e){
         e.printStackTrace();
      }catch(final Exception e){
         e.printStackTrace();
      }
   }

   /************************************************************/
   /******************* ACCORD DE TRANSFERT ********************/
   /************************************************************/

   /**
    * Test la méthode createJDomAccordTranfert.
    */
   @Test
   public void testCreateJDomAccordTranfert(){

      final Document doc = xmlUtils.createJDomAccordTranfert();
      assertNotNull(doc);
      assertNotNull(doc.getRootElement());
      assertTrue(doc.getRootElement().getName().equals("AccordTransfert"));

   }

   /**
    * Test la méthode addCoupleAccordValeur.
    */

   @Test
   public void testAddCoupleAccordValeur(){

      final Element ligne = new Element("LigneParagraphe");
      final CoupleAccordValeur cv1 = new CoupleAccordValeur("Nom", new String[] {"value1", "value2"});
      xmlUtils.addCoupleAccordValeur(ligne, cv1);
      final CoupleAccordValeur cv2 = new CoupleAccordValeur("NomB", new String[] {"value3"});
      xmlUtils.addCoupleAccordValeur(ligne, cv2);

      final List<?> children = ligne.getChildren("CoupleAccordValeur");
      assertTrue(children.size() == 2);
      final Element firstCouple = Element.class.cast(children.get(0));
      assertTrue(firstCouple.getChildText("NomValeur").equals("Nom"));
      final List<?> valeurs = firstCouple.getChildren("Valeurs");
      assertTrue(valeurs.size() == 1);
      final List<?> valeur = Element.class.cast(valeurs.get(0)).getChildren("Valeur");
      assertTrue(valeur.size() == 2);
      assertTrue(Element.class.cast(valeur.get(0)).getText().equals("value1"));

      xmlUtils.addCoupleAccordValeur(null, cv2);
      xmlUtils.addCoupleAccordValeur(ligne, null);
      assertTrue(ligne.getChildren("CoupleAccordValeur").size() == 2);
   }

   /**
    * Test la méthode addLigneAccord.
    */

   @Test
   public void testAddLigneAccord(){

      final Element bloc = new Element("BlocPrincipal");
      final CoupleAccordValeur cv1 = new CoupleAccordValeur("Nom1", new String[] {"value1", "value2"});
      final CoupleAccordValeur cv2 = new CoupleAccordValeur("Nom2", new String[] {"value3"});
      final CoupleAccordValeur cv3 = new CoupleAccordValeur("Nom3", new String[] {"value4"});
      final LigneAccord ligne = new LigneAccord(new CoupleAccordValeur[] {cv1, cv2, cv3});
      xmlUtils.addLigneAccord(bloc, ligne);

      final List<?> children = bloc.getChildren("LigneAccord");
      assertTrue(children.size() == 1);
      final Element ligneP = Element.class.cast(children.get(0));
      assertTrue(ligneP.getChildren("CoupleAccordValeur").size() == 3);
      final List<?> childrenL = ligneP.getChildren("CoupleAccordValeur");
      final Element firstCouple = Element.class.cast(childrenL.get(0));
      assertTrue(firstCouple.getChildText("NomValeur").equals("Nom1"));

      final Element secondCouple = Element.class.cast(childrenL.get(1));
      assertTrue(secondCouple.getChildText("NomValeur").equals("Nom2"));

      xmlUtils.addLigneAccord(bloc, null);
      xmlUtils.addLigneAccord(null, ligne);
      assertTrue(bloc.getChildren("LigneAccord").size() == 1);
   }

   /**
    * Test la méthode addBlocPrincipal.
    */

   @Test
   public void testAddBlocPrincipal(){

      final CoupleAccordValeur cv1 = new CoupleAccordValeur("Nom1", new String[] {"value1", "value2"});
      final CoupleAccordValeur cv2 = new CoupleAccordValeur("Nom2", new String[] {"value3"});
      final CoupleAccordValeur cv3 = new CoupleAccordValeur("Nom3", new String[] {"value4"});
      final CoupleAccordValeur cv4 = new CoupleAccordValeur("Nom4", new String[] {"value5", "value6"});
      final LigneAccord ligne1 = new LigneAccord(new CoupleAccordValeur[] {cv1, cv2});
      final LigneAccord ligne2 = new LigneAccord(new CoupleAccordValeur[] {cv3});
      final LigneAccord ligne3 = new LigneAccord(new CoupleAccordValeur[] {cv4});

      final BlocPrincipal bloc = new BlocPrincipal("Titre1", new LigneAccord[] {ligne1, ligne2, ligne3});

      final Element page = new Element("Page");
      xmlUtils.addBlocPrincipal(page, bloc);

      List<?> children = page.getChildren("BlocPrincipal");
      assertTrue(children.size() == 1);
      Element p = Element.class.cast(children.get(0));
      assertNotNull(p);
      assertTrue(p.getChildText("NomBloc").equals("Titre1"));
      assertTrue(p.getChildren("LigneAccord").size() == 3);

      final BlocPrincipal bloc2 = new BlocPrincipal();
      xmlUtils.addBlocPrincipal(page, bloc2);
      children = page.getChildren("BlocPrincipal");
      assertTrue(children.size() == 2);
      p = Element.class.cast(children.get(1));
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

   @Test
   public void testAddTableau(){

      final CoupleAccordValeur cv1 = new CoupleAccordValeur("Nom1", new String[] {"value1", "value2"});
      final CoupleAccordValeur cv2 = new CoupleAccordValeur("Nom2", new String[] {"value3"});
      final CoupleAccordValeur cv3 = new CoupleAccordValeur("Nom3", new String[] {"value4"});
      final CoupleAccordValeur cv4 = new CoupleAccordValeur("Nom4", new String[] {"value5", "value6"});
      final LigneAccord ligne1 = new LigneAccord(new CoupleAccordValeur[] {cv1, cv2});
      final LigneAccord ligne2 = new LigneAccord(new CoupleAccordValeur[] {cv3});
      final LigneAccord ligne3 = new LigneAccord(new CoupleAccordValeur[] {cv4});

      final BlocPrincipal bloc = new BlocPrincipal("Titre1", new LigneAccord[] {ligne1, ligne2, ligne3});

      final Element page = new Element("Page");
      xmlUtils.addTableau(page, new BlocPrincipal[] {bloc, bloc, bloc});

      List<?> children = page.getChildren("Tableau");
      assertTrue(children.size() == 1);
      Element p = Element.class.cast(children.get(0));
      assertNotNull(p);
      assertTrue(p.getChildren("BlocPrincipal").size() == 3);

      xmlUtils.addTableau(page, new BlocPrincipal[] {});
      children = page.getChildren("Tableau");
      assertTrue(children.size() == 2);
      p = Element.class.cast(children.get(1));
      assertNotNull(p);
      assertTrue(p.getChildren("BlocPrincipal").size() == 0);

      xmlUtils.addTableau(page, null);
      xmlUtils.addTableau(null, new BlocPrincipal[] {});
      assertTrue(page.getChildren("Tableau").size() == 2);
   }

   /**
    * Test la méthode addValeursSignatures.
    */

   @Test
   public void testAddValeursSignatures(){

      final Element ligne = new Element("LigneParagraphe");
      final ValeursSignatures vs1 = new ValeursSignatures("Nom", "Valeur");
      xmlUtils.addValeursSignatures(ligne, vs1);
      final ValeursSignatures vs2 = new ValeursSignatures("Nom2", "Valeur2");
      xmlUtils.addValeursSignatures(ligne, vs2);

      final List<?> children = ligne.getChildren("ValeursSignatures");
      assertTrue(children.size() == 2);
      final Element firstCouple = Element.class.cast(children.get(0));
      assertTrue(firstCouple.getChildText("Valeur1").equals("Nom"));
      assertTrue(firstCouple.getChildText("Valeur2").equals("Valeur"));

      final Element secondCouple = Element.class.cast(children.get(1));
      assertTrue(secondCouple.getChildText("Valeur1").equals("Nom2"));
      assertTrue(secondCouple.getChildText("Valeur2").equals("Valeur2"));

      xmlUtils.addValeursSignatures(null, vs2);
      xmlUtils.addValeursSignatures(ligne, null);
      assertTrue(ligne.getChildren("ValeursSignatures").size() == 2);
   }

   /**
    * Test la méthode addLigne.
    */

   @Test
   public void testAddListeSignature(){

      final Element sousP = new Element("SousParagraphe");
      final ValeursSignatures vs1 = new ValeursSignatures("Nom", "Valeur");
      final ValeursSignatures vs2 = new ValeursSignatures("Nom2", "Valeur2");
      final ListeSignature ligne = new ListeSignature(new ValeursSignatures[] {vs1, vs2});
      xmlUtils.addListeSignature(sousP, ligne);

      final List<?> children = sousP.getChildren("ListeSignature");
      assertTrue(children.size() == 1);
      final Element ligneP = Element.class.cast(children.get(0));
      assertTrue(ligneP.getChildren("ValeursSignatures").size() == 2);
      final List<?> childrenL = ligneP.getChildren("ValeursSignatures");
      final Element firstCouple = Element.class.cast(childrenL.get(0));
      assertTrue(firstCouple.getChildText("Valeur1").equals("Nom"));
      assertTrue(firstCouple.getChildText("Valeur2").equals("Valeur"));

      final Element secondCouple = Element.class.cast(childrenL.get(1));
      assertTrue(secondCouple.getChildText("Valeur1").equals("Nom2"));
      assertTrue(secondCouple.getChildText("Valeur2").equals("Valeur2"));

      xmlUtils.addListeSignature(sousP, null);
      xmlUtils.addListeSignature(null, ligne);
      assertTrue(sousP.getChildren("ListeSignature").size() == 1);
   }

   /**
    * Test la méthode addSignatures.
    */

   @Test
   public void testAddSignatures(){

      final ValeursSignatures vs1 = new ValeursSignatures("Nom", "Valeur");
      final ValeursSignatures vs2 = new ValeursSignatures("Nom2", "Valeur2");
      final ListeSignature ligne = new ListeSignature(new ValeursSignatures[] {vs1, vs2});
      final ValeursSignatures vs3 = new ValeursSignatures("Nom3", "Valeur3");
      final ValeursSignatures vs4 = new ValeursSignatures("Nom4", "Valeur4");
      final ListeSignature ligne2 = new ListeSignature(new ValeursSignatures[] {vs3, vs4});

      final Signatures sign = new Signatures("Titre1", "Titre2", new ListeSignature[] {ligne, ligne2});
      final Element page = new Element("Page");

      xmlUtils.addSignatures(page, sign);

      List<?> children = page.getChildren("Signatures");
      assertTrue(children.size() == 1);
      Element p = Element.class.cast(children.get(0));
      assertNotNull(p);
      assertTrue(p.getChildren("Entete").size() == 1);
      final Element e = (Element) p.getChildren("Entete").get(0);
      assertTrue(e.getChildText("Titre1").equals("Titre1"));
      assertTrue(e.getChildText("Titre2").equals("Titre2"));
      assertTrue(p.getChildren("ListeSignature").size() == 2);

      final Signatures sign2 = new Signatures();
      xmlUtils.addSignatures(page, sign2);
      children = page.getChildren("Signatures");
      assertTrue(children.size() == 2);
      p = Element.class.cast(children.get(1));
      assertNotNull(p);
      assertTrue(p.getChild("Entete") == null);
      assertTrue(p.getChildren("ListeSignature").size() == 0);

      xmlUtils.addSignatures(page, null);
      xmlUtils.addSignatures(null, sign2);
      assertTrue(page.getChildren("Signatures").size() == 2);
   }

   public Document creerDocumentAccord(){
      final Document doc = xmlUtils.createJDomAccordTranfert();
      final Element root = doc.getRootElement();
      final Element page = xmlUtils.addPage(root, "Accord de transfert");
      xmlUtils.addBasDePage(root, "bas");
      xmlUtils.addHautDePage(root, "haut", false, null);

      // Tableau
      final CoupleAccordValeur cv1 =
         new CoupleAccordValeur("Nom organisation", new String[] {"Hopital Bichat", "12 rue de billancourt - 75019 PARIS"});
      final CoupleAccordValeur cv2 = new CoupleAccordValeur("Responsable", new String[] {"Dr GREGORY"});
      final CoupleAccordValeur cv3 = new CoupleAccordValeur("Téléphone", new String[] {"0143568975", "mail@mail.fr"});
      final LigneAccord li1 = new LigneAccord(new CoupleAccordValeur[] {cv1});
      final LigneAccord li2 = new LigneAccord(new CoupleAccordValeur[] {cv2});
      final LigneAccord li3 = new LigneAccord(new CoupleAccordValeur[] {cv3});
      final BlocPrincipal bloc1 = new BlocPrincipal("Destinataire", new LigneAccord[] {li1, li2, li3});

      final CoupleAccordValeur cv4 = new CoupleAccordValeur("Nom organisation",
         new String[] {"Hopital Saint Louis", "1 rue Claude Vellefeaux - 75010 PARIS"});
      final CoupleAccordValeur cv5 = new CoupleAccordValeur("Responsable", new String[] {"Dr VENTADOUR"});
      final CoupleAccordValeur cv6 = new CoupleAccordValeur("Téléphone", new String[] {"0143487965", "mail@mail.fr"});
      final LigneAccord li4 = new LigneAccord(new CoupleAccordValeur[] {cv4});
      final LigneAccord li5 = new LigneAccord(new CoupleAccordValeur[] {cv5});
      final LigneAccord li6 = new LigneAccord(new CoupleAccordValeur[] {cv6});
      final BlocPrincipal bloc2 = new BlocPrincipal("Expéditeur", new LigneAccord[] {li4, li5, li6});

      final CoupleAccordValeur cv7 = new CoupleAccordValeur("Nature", new String[] {"CELLULES"});
      final CoupleAccordValeur cv8 = new CoupleAccordValeur("Quantité", new String[] {"10ml"});
      final CoupleAccordValeur cv9 = new CoupleAccordValeur("Date", new String[] {"15/11/2010"});
      final CoupleAccordValeur cv10 = new CoupleAccordValeur("Contenant", new String[] {"TUBE"});
      final CoupleAccordValeur cv11 = new CoupleAccordValeur("Transport", new String[] {"Réfrigéré"});
      final CoupleAccordValeur cv12 = new CoupleAccordValeur("Traçabilité", new String[] {"Suivi constant"});
      final CoupleAccordValeur cv13 = new CoupleAccordValeur("Informations", new String[] {"Non communiquées"});
      final LigneAccord li7 = new LigneAccord(new CoupleAccordValeur[] {cv7});
      final LigneAccord li8 = new LigneAccord(new CoupleAccordValeur[] {cv8});
      final LigneAccord li9 = new LigneAccord(new CoupleAccordValeur[] {cv9});
      final LigneAccord li10 = new LigneAccord(new CoupleAccordValeur[] {cv10});
      final LigneAccord li11 = new LigneAccord(new CoupleAccordValeur[] {cv11});
      final LigneAccord li12 = new LigneAccord(new CoupleAccordValeur[] {cv12});
      final LigneAccord li13 = new LigneAccord(new CoupleAccordValeur[] {cv13});
      final BlocPrincipal bloc3 =
         new BlocPrincipal("Matériel biologique", new LigneAccord[] {li7, li8, li9, li10, li11, li12, li13});
      xmlUtils.addTableau(page, new BlocPrincipal[] {bloc1, bloc2, bloc3});

      final ValeursSignatures vs1 = new ValeursSignatures("Nom du responsable", "Nom du responsable");
      final ValeursSignatures vs3 = new ValeursSignatures("Signature pour accord", "Signature pour accord");
      final ValeursSignatures vs5 = new ValeursSignatures("Date", "Date");
      final ListeSignature ligne = new ListeSignature(new ValeursSignatures[] {vs1});
      final ListeSignature ligne3 = new ListeSignature(new ValeursSignatures[] {vs3});
      final ListeSignature ligne5 = new ListeSignature(new ValeursSignatures[] {vs5});
      final Signatures sign = new Signatures("EXPEDITEUR", "DESTINATAIRE", new ListeSignature[] {ligne, ligne3, ligne5});
      xmlUtils.addSignatures(page, sign);

      final Element page2 = xmlUtils.addPage(root, "Accord de transfert");
      xmlUtils.addTableau(page2, new BlocPrincipal[] {bloc1, bloc2, bloc3});
      xmlUtils.addSignatures(page2, sign);

      return doc;
   }

   /**
    * Test de la méthode createXMLFile().
    */
   @Test
   public void testCreateXMLAccord(){
      final Document doc = creerDocumentAccord();

      final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
      final String file = "testAccord.xml";
      xmlUtils.createXMLFile(doc, folder, file);

      final String path = folder + file;
      final File f = new File(path);
      assertTrue(f.exists());
      assertTrue(f.length() > 0);

      //f.delete();
   }

   @Test
   public void testCreerAccordPdf(){
      // création du document contenant les infos
      final Document doc = creerDocumentAccord();

      byte[] results = null;
      try{
         results = xmlUtils.creerAccordTransfertPdf(doc);

         assertTrue(results.length > 0);

         final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/xml/";
         final String file = "testAccord.pdf";
         final String path = folder + file;

         final File f = new File(path);
         FileOutputStream fo;
         fo = new FileOutputStream(f);

         for(int i = 0; i < results.length; i++){
            fo.write(results[i]);
         }
         fo.close();
      }catch(final IOException e){
         e.printStackTrace();
      }catch(final Exception e){
         e.printStackTrace();
      }

   }

}
