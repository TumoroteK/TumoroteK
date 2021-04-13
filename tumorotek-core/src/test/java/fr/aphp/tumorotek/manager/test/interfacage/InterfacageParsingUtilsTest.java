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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.interfacage.EmetteurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.impl.interfacage.ConfigurationParsing;
import fr.aphp.tumorotek.manager.interfacage.BlocExterneManager;
import fr.aphp.tumorotek.manager.interfacage.DossierExterneManager;
import fr.aphp.tumorotek.manager.interfacage.InterfacageParsingUtils;
import fr.aphp.tumorotek.manager.interfacage.ValeurExterneManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;

/**
 *
 * Classe de test pour le manager InterfacageParsingUtils.
 * Classe créée le 20/10/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.2.2-diamic
 *
 */
public class InterfacageParsingUtilsTest extends AbstractManagerTest4
{

	@Autowired
	private InterfacageParsingUtils interfacageParsingUtils;
	@Autowired
	private EmetteurDao emetteurDao;
	@Autowired
	private DossierExterneManager dossierExterneManager;
	@Autowired
	private BlocExterneManager blocExterneManager;
	@Autowired
	private ValeurExterneManager valeurExterneManager;
	@Autowired
	private EntiteDao entiteDao;

	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public InterfacageParsingUtilsTest(){}

	@Test
	public void testInitConfigurationParsing(){
		final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";

		// On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		Document document = null;
		try{
			//On crée un nouveau document JDOM avec en 
			// argument le fichier XML
			document = sxb.build(new File(folder + "interfacage.xml"));

			// On initialise un nouvel élément racine avec 
			// l'élément racine du document.
			Element racine = document.getRootElement();

			ConfigurationParsing conf = interfacageParsingUtils.initConfigurationParsing(racine);
			assertNotNull(conf);
			assertTrue(conf.getSeparateurChamps().equals("|"));
			assertTrue(conf.getSeparateurComposants().equals("~"));
			assertTrue(conf.getSeparateurSousComposants().equals("^"));
			assertTrue(conf.getBlocLibreKey().equals("OBX"));

			// On crée une instance de SAXBuilder
			sxb = new SAXBuilder();
			document = null;
			//On crée un nouveau document JDOM avec en 
			// argument le fichier XML
			document = sxb.build(new File(folder + "bad_interfacage.xml"));

			// On initialise un nouvel élément racine avec 
			// l'élément racine du document.
			racine = document.getRootElement();
			conf = interfacageParsingUtils.initConfigurationParsing(racine);
			assertNotNull(conf);
			assertTrue(conf.getSeparateurChamps().equals("|"));
			assertTrue(conf.getSeparateurComposants().equals("XXX"));
			assertNull(conf.getSeparateurSousComposants());
			assertNull(conf.getBlocLibreKey());
		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}
	}

	/**
	 * @throws IOException
	 */
	@Test
	public void testParseFileToInjectInTk() throws IOException{
		String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";

		InputStream input = null;
		try{
			input = new FileInputStream(folder + "ANP59248.AST");
			byte[] ba = new byte[input.available()];
			input.read(ba);
			String str = new String(ba);

			// On crée une instance de SAXBuilder
			final SAXBuilder sxb = new SAXBuilder();
			Document document = null;
			//On crée un nouveau document JDOM avec en 
			// argument le fichier XML
			document = sxb.build(new File(folder + "interfacage.xml"));

			// On initialise un nouvel élément racine avec 
			// l'élément racine du document.
			Element racine = document.getRootElement();

			ConfigurationParsing conf = interfacageParsingUtils.initConfigurationParsing(racine);

			Hashtable<String, List<String>> record = 
					interfacageParsingUtils.parseFileToInjectInTk(conf, str).get(0);
			assertNotNull(record);
			assertTrue(record.size() == 39);
			assertTrue(record.containsKey("H"));
			assertTrue(record.containsKey("P"));
			assertTrue(record.containsKey("OBR"));

			assertTrue(record.get("H").size() == 14);
			assertTrue(record.get("H").contains("ANP59248"));
			assertTrue(record.get("H").contains("UF000~SIH"));
			assertTrue(record.get("H").contains("20070503160500"));

			assertTrue(record.get("OBR").size() == 28);
			assertTrue(record.get("OBR").contains("20070420000000"));
			assertTrue(record.get("OBR").contains("11,54"));
			assertTrue(record.get("OBR").get(27).equals(" "));

			assertTrue(record.containsKey("OBR_OBX1"));
			assertTrue(record.containsKey("OBR_OBX2"));
			assertTrue(record.containsKey("OBR_OBX3"));
			assertTrue(record.containsKey("L_OBX1"));

			input.close();

			// test SPM
			folder = "src/test/resources/data/DIAMIC/";
			input = new FileInputStream(folder.concat("diamic-multiORC.hl7"));
			ba = new byte[input.available()];
			input.read(ba);
			str = new String(ba);
			document = sxb.build(new File(folder.concat("diamic-mapping.xml")));
			racine = document.getRootElement();
			conf = interfacageParsingUtils.initConfigurationParsing(racine);

			List<Hashtable<String, List<String>>> contenu = 
					interfacageParsingUtils.parseFileToInjectInTk(conf, str);
			assertTrue(contenu.size() == 2);

			record = contenu.get(0);
			assertTrue(record.size() == 12);
			assertTrue(record.containsKey("MSH"));
			assertTrue(record.containsKey("PID"));
			assertTrue(record.containsKey("PV1"));
			assertTrue(record.containsKey("ORC"));
			assertTrue(record.containsKey("OBR"));
			assertTrue(record.containsKey("OBR_OBX1"));
			assertTrue(record.containsKey("OBR_OBX2"));
			assertTrue(record.containsKey("OBR_OBX3"));
			assertTrue(record.containsKey("OBR_OBX4"));
			assertTrue(record.containsKey("OBR_OBX5"));
			assertTrue(record.containsKey("OBR_OBX6"));
			assertTrue(record.containsKey("OBR_OBX7"));

			record = contenu.get(1);
			assertTrue(record.size() == 26);
			assertTrue(record.containsKey("MSH"));
			assertTrue(record.containsKey("PID"));
			assertTrue(record.containsKey("PV1"));
			assertTrue(record.containsKey("ORC"));
			assertTrue(record.containsKey("OBR"));
			assertTrue(record.containsKey("OBR_OBX1"));
			assertTrue(record.containsKey("OBR_OBX2"));
			assertTrue(record.containsKey("OBR_OBX3"));
			assertTrue(record.containsKey("OBR_OBX4"));
			assertTrue(record.containsKey("OBR_OBX5"));
			assertTrue(record.containsKey("OBR_OBX6"));
			assertTrue(record.containsKey("OBR_OBX7"));
			assertTrue(record.containsKey("SPM1"));
			assertTrue(record.containsKey("SPM1_OBX1"));
			assertTrue(record.containsKey("SPM1_OBX2"));
			assertTrue(record.containsKey("SPM1_OBX3"));
			assertTrue(record.containsKey("SPM1_OBX4"));
			assertTrue(record.containsKey("SPM1_OBX5"));
			assertTrue(record.containsKey("SPM1_OBX6"));
			assertTrue(record.containsKey("SPM2"));
			assertTrue(record.containsKey("SPM2_OBX1"));
			assertTrue(record.containsKey("SPM2_OBX2"));
			assertTrue(record.containsKey("SPM2_OBX3"));
			assertTrue(record.containsKey("SPM2_OBX4"));
			assertTrue(record.containsKey("SPM2_OBX5"));
			assertTrue(record.containsKey("SPM2_OBX6"));

			input.close();

		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}finally{
			if(input != null){
				try { input.close(); } catch (Exception e) {}
			}
		}
	}

	@Test
	public void testParseFileToExtractBlocsLibres() throws IOException{
		final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";
		InputStream input = null;
		try{
			input = new FileInputStream(folder + "ANP59248.AST");
			final byte[] ba = new byte[input.available()];
			input.read(ba);
			final String str = new String(ba);

			// On crée une instance de SAXBuilder
			final SAXBuilder sxb = new SAXBuilder();
			Document document = null;
			//On crée un nouveau document JDOM avec en 
			// argument le fichier XML
			document = sxb.build(new File(folder + "interfacage.xml"));

			// On initialise un nouvel élément racine avec 
			// l'élément racine du document.
			final Element racine = document.getRootElement();

			final ConfigurationParsing conf = interfacageParsingUtils.initConfigurationParsing(racine);

			final List<String> blocsLibres = interfacageParsingUtils.parseFileToExtractBlocsLibres(conf, str);
			assertNotNull(blocsLibres);
			assertTrue(blocsLibres.size() == 34);
			assertTrue(blocsLibres.get(0).equals("1|TX|ANTERIORITE| |GE00516022 GE00592767 "));
			assertTrue(blocsLibres.get(3).equals("1|ST|~ADICAP| |GHLOJ7F4| | | | | |F"));
			assertTrue(blocsLibres.get(33).equals("29|NM|Ttt_preop| |non| | | | | |F"));
		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}finally{
			if(input != null){
				input.close();
			}
		}
	}

	@Test

	public void testExtractMappingValuesForThesaurus() throws Exception{
		final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";

		// On crée une instance de SAXBuilder
		final SAXBuilder sxb = new SAXBuilder();
		Document document = null;
		try{
			//On crée un nouveau document JDOM avec en 
			// argument le fichier XML
			document = sxb.build(new File(folder + "interfacage.xml"));
			final Element racine = document.getRootElement();
			// on récupère tous les blocs
			final List<?> blocs = racine.getChildren("Bloc");
			final Element currBloc = Element.class.cast(blocs.get(1));
			final List<?> mappings = currBloc.getChildren("Mapping");
			Element currMapping = Element.class.cast(mappings.get(3));
			Element modifier = currMapping.getChild("Source").getChild("Modifier");
			Hashtable<String, String> res = interfacageParsingUtils.extractMappingValuesForThesaurs(modifier);
			assertTrue(res.size() == 3);

			assertTrue(res.containsKey("Prélèvement Sanguin"));
			assertTrue(res.get("TISSU TUMORAL").equals("TISSU"));

			currMapping = Element.class.cast(mappings.get(1));
			modifier = currMapping.getChild("Source").getChild("Modifier");
			res = interfacageParsingUtils.extractMappingValuesForThesaurs(modifier);
			assertTrue(res.size() == 0);

			res = interfacageParsingUtils.extractMappingValuesForThesaurs(currMapping);
			assertTrue(res.size() == 0);

			res = interfacageParsingUtils.extractMappingValuesForThesaurs(null);
			assertTrue(res.size() == 0);
		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testInitNewDossierExterne() throws IOException{
		final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";

		// On crée une instance de SAXBuilder
		final SAXBuilder sxb = new SAXBuilder();
		Document document = null;
		try{
			//On crée un nouveau document JDOM avec en 
			// argument le fichier XML
			document = sxb.build(new File(folder + "interfacage.xml"));
			final Element racine = document.getRootElement();
			final ConfigurationParsing conf = interfacageParsingUtils.initConfigurationParsing(racine);

			InputStream input = null;
			try{
				input = new FileInputStream(folder + "ANP59248.AST");
				final byte[] ba = new byte[input.available()];
				input.read(ba);
				final String str = new String(ba);
				final Hashtable<String, List<String>> contenu = 
						interfacageParsingUtils.parseFileToInjectInTk(conf, str).get(0);

				final DossierExterne dossier = interfacageParsingUtils.initNewDossierExterne(conf, contenu, racine);
				assertNotNull(dossier);
				assertTrue(dossier.getIdentificationDossier().equals("ANP59248"));
				assertTrue(dossier.getOperation().equals("ORU"));
				assertTrue(format.format(dossier.getDateOperation().getTime()).equals("2007-05-03"));
			}catch(final Exception e){
				e.printStackTrace();
				assertTrue(false);
			}finally{
				if(input != null){
					input.close();
				}
			}
		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testExtractEmetteurFromFileToInjectInTk() throws IOException{
		final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";

		InputStream input = null;
		try{
			input = new FileInputStream(folder + "ANP59248.AST");
			final byte[] ba = new byte[input.available()];
			input.read(ba);
			final String str = new String(ba);

			Emetteur eTest = interfacageParsingUtils.extractEmetteurFromFileToInjectInTk(folder + "configuration_interfacage.xml",
					str, "Boite Apix");
			assertNotNull(eTest);
			assertTrue(eTest.getEmetteurId().equals(2));

			eTest =
					interfacageParsingUtils.extractEmetteurFromFileToInjectInTk(folder + "configuration_interfacage.xml", str, "Boite");
			assertNull(eTest);

			eTest = interfacageParsingUtils.extractEmetteurFromFileToInjectInTk(folder + "configuration_interfacage.xml", str,
					"Boite Erreur");
			assertNull(eTest);
		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}finally{
			if(input != null){
				input.close();
			}
		}
	}

	@Test
	public void testExtractXMLFIleFromFileToInjectInTk() throws IOException{
		final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";

		InputStream input = null;
		try{
			input = new FileInputStream(folder + "ANP59248.AST");
			final byte[] ba = new byte[input.available()];
			input.read(ba);
			final String str = new String(ba);

			// Emetteur e2 = emetteurDao.findById(2);
			String xml = interfacageParsingUtils.extractInboxParamFromFile(folder + "configuration_interfacage.xml", str,
					"Boite Apix", "XML");
			//e2);
			assertNotNull(xml);
			assertTrue(xml.equals("interfacage_anapath.xml"));

			// Emetteur e1 = emetteurDao.findById(1);
			xml = interfacageParsingUtils.extractInboxParamFromFile(folder + "configuration_interfacage.xml", str, "Boite", "XML");
			//e1);
			assertNull(xml);

			xml = interfacageParsingUtils.extractInboxParamFromFile(folder + "configuration_interfacage.xml", str,
					"Boite Erreur", "XML");
			//e2);
			assertNotNull(xml);
			assertTrue(xml.equals("interfacage_hemato.xml"));

			// balise non reconnue
			xml = interfacageParsingUtils.extractInboxParamFromFile(folder + "configuration_interfacage.xml", str,
					"Boite Apix", "nooope");
			//e2);
			assertNull(xml);

		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}finally{
			if(input != null){
				input.close();
			}
		}
	}

	/**
	 * @throws IOException
	 */
	@Test
	public void testGetValueFromBlocAndEmplacement() throws IOException{
		// init
		final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";

		InputStream input = null;
		try{
			input = new FileInputStream(folder + "ANP59248.AST");
			final byte[] ba = new byte[input.available()];
			input.read(ba);
			final String str = new String(ba);
			// On crée une instance de SAXBuilder
			final SAXBuilder sxb = new SAXBuilder();
			Document document = null;
			//On crée un nouveau document JDOM avec en 
			// argument le fichier XML
			document = sxb.build(new File(folder + "interfacage.xml"));

			// On initialise un nouvel élément racine avec 
			// l'élément racine du document.
			final Element racine = document.getRootElement();
			final ConfigurationParsing conf = interfacageParsingUtils.initConfigurationParsing(racine);
			final Hashtable<String, List<String>> contenu = 
					interfacageParsingUtils.parseFileToInjectInTk(conf, str).get(0);

			// test niveau 1
			String value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "H", "2");
			assertTrue(value.equals("ANP59248"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "1");
			assertTrue(value.equals("1"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "H", "13");
			assertTrue(value.equals("20070503160500"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "H", "5");
			assertNull(value);

			// test niveau 2
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "5.1");
			assertTrue(value.equals("TESTN3"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "5.2");
			assertTrue(value.equals("Testp3"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "OBR", "10.1");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "OBR", "10.2");
			assertTrue(value.equals("MT"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "OBR", "10.4");
			assertTrue(value.equals("Docteur"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "9.2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "7.1");
			assertTrue(value.equals("19410904"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "7.2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "3.1");
			assertNull(value);

			// test niveau 3
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "H", "14.2.1");
			assertTrue(value.equals("TEST1"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "H", "14.2.2");
			assertTrue(value.equals("TEST2"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "10.1.1");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "10.1.3");
			assertTrue(value.equals("SANG"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "10.1.5");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "5.2.1");
			assertTrue(value.equals("Testp3"));
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "5.2.2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "9.2.1");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "P", "3.1.1");
			assertNull(value);

			// mauvais paramétrage
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(null, conf, "H", "2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, null, "H", "2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, null, "2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "H", null);
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "H455", "2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "H", "15");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, conf, "OBR", "3.5");
			assertNull(value);

		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}finally{
			if(input != null){
				input.close();
			}
		}
	}

	@Test
	public void testGetValueFromEmplacement() throws IOException{
		// init
		final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";

		InputStream input = null;
		try{
			input = new FileInputStream(folder + "ANP59248.AST");

			final byte[] ba = new byte[input.available()];
			input.read(ba);
			final String str = new String(ba);
			// On crée une instance de SAXBuilder
			final SAXBuilder sxb = new SAXBuilder();
			Document document = null;
			//On crée un nouveau document JDOM avec en 
			// argument le fichier XML
			document = sxb.build(new File(folder + "interfacage.xml"));

			// On initialise un nouvel élément racine avec 
			// l'élément racine du document.
			final Element racine = document.getRootElement();
			final ConfigurationParsing conf = interfacageParsingUtils.initConfigurationParsing(racine);
			final Hashtable<String, List<String>> contenu = 
					interfacageParsingUtils.parseFileToInjectInTk(conf, str).get(0);

			// test niveau 1
			String value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("H"), conf, "2");
			assertTrue(value.equals("ANP59248"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "1");
			assertTrue(value.equals("1"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("H"), conf, "13");
			assertTrue(value.equals("20070503160500"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("H"), conf, "5");
			assertNull(value);

			// test niveau 2
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "2.1");
			assertTrue(value.equals("1493053993"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "5.1");
			assertTrue(value.equals("TESTN3"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "5.2");
			assertTrue(value.equals("Testp3"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("OBR"), conf, "10.1");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("OBR"), conf, "10.2");
			assertTrue(value.equals("MT"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("OBR"), conf, "10.4");
			assertTrue(value.equals("Docteur"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "9.2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "7.1");
			assertTrue(value.equals("19410904"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "7.2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "3.1");
			assertNull(value);

			// test niveau 3
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "2.5.2");
			assertTrue(value.equals("93285"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("H"), conf, "14.2.1");
			assertTrue(value.equals("TEST1"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("H"), conf, "14.2.2");
			assertTrue(value.equals("TEST2"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "10.1.1");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "10.1.3");
			assertTrue(value.equals("SANG"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "10.1.5");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "5.2.1");
			assertTrue(value.equals("Testp3"));
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "5.2.2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "9.2.1");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("P"), conf, "3.1.1");
			assertNull(value);

			// mauvais paramétrage
			value = interfacageParsingUtils.getValueFromEmplacement(null, conf, "2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("H"), null, "2");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("H"), conf, null);
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("H"), conf, "15");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("OBR"), conf, "3.5");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromEmplacement(new ArrayList<String>(), conf, "2");
			assertNull(value);
		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}finally{
			if(input != null){
				input.close();
			}
		}
	}

	@Test
	public void testGetValueFromBlocLibre() throws IOException{
		// init
		final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";

		InputStream input = null;
		try{
			input = new FileInputStream(folder + "ANP59248.AST");
			final byte[] ba = new byte[input.available()];
			input.read(ba);
			final String str = new String(ba);
			// On crée une instance de SAXBuilder
			final SAXBuilder sxb = new SAXBuilder();
			Document document = null;
			//On crée un nouveau document JDOM avec en 
			// argument le fichier XML
			document = sxb.build(new File(folder + "interfacage.xml"));

			// On initialise un nouvel élément racine avec 
			// l'élément racine du document.
			final Element racine = document.getRootElement();
			final ConfigurationParsing conf = interfacageParsingUtils.initConfigurationParsing(racine);
			final List<String> blocs = interfacageParsingUtils.parseFileToExtractBlocsLibres(conf, str);

			// OK
			String value = interfacageParsingUtils.getValueFromBlocLibre(blocs, conf, "ANTERIORITE", "3", "5");
			assertTrue(value.equals("GE00516022 GE00592767 "));
			value = interfacageParsingUtils.getValueFromBlocLibre(blocs, conf, "ADICAP", "3.2", "5");
			assertTrue(value.equals("GHLOJ7F4"));
			value = interfacageParsingUtils.getValueFromBlocLibre(blocs, conf, "Tumor Budding (bourgeonnement)", "3", "5.1");
			assertTrue(value.equals("non"));

			// NON
			value = interfacageParsingUtils.getValueFromBlocLibre(null, conf, "ANTERIORITE", "3", "5");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocLibre(blocs, null, "ADICAP", "3.2", "5");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocLibre(blocs, conf, null, "3", "5.1");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocLibre(blocs, conf, "ANTERIORITE", null, "5");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocLibre(blocs, conf, "ADICAP", "3.2", null);
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocLibre(new ArrayList<String>(), conf, "Tumor Budding (bourgeonnement)",
					"3", "5.1");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocLibre(blocs, conf, "Tumor Budding (bourgeonnement)", "1", "5.1");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocLibre(blocs, conf, "Tumor Budding (bourgeonnement)", "20", "5.1");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocLibre(blocs, conf, "Tumor Budding (bourgeonnement)", "3", "25");
			assertNull(value);
			value = interfacageParsingUtils.getValueFromBlocLibre(blocs, conf, "Tumor Buddi", "3", "5.1");
			assertNull(value);
		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}finally{
			if(input != null){
				input.close();
			}
		}
	}

	@Test
	public void testFormateValueUsingFunction(){
		Element fcElt = new Element("Fonction");
		fcElt.setAttribute("nom", "stringToLowerCase");
		assertTrue(interfacageParsingUtils.formateValueUsingFunction(fcElt, "TEST25", null, null, null, null, null).equals("test25"));
		assertTrue(interfacageParsingUtils.formateValueUsingFunction(fcElt, "", null, null, null, null, null).equals(""));
		fcElt.setAttribute("nom", "stringToUpperCase");
		assertTrue(interfacageParsingUtils.formateValueUsingFunction(fcElt, "test25", null, null, null, null, null).equals("TEST25"));
		assertTrue(interfacageParsingUtils.formateValueUsingFunction(fcElt, "", null, null, null, null, null).equals(""));
		fcElt.setAttribute("nom", "replaceCommaByDot");
		assertTrue(interfacageParsingUtils.formateValueUsingFunction(fcElt, "15,25", null, null, null, null, null).equals("15.25"));
		assertTrue(interfacageParsingUtils.formateValueUsingFunction(fcElt, "", null, null, null, null, null).equals(""));
		fcElt.setAttribute("nom", "autre");
		assertTrue(interfacageParsingUtils.formateValueUsingFunction(fcElt, "test25", null, null, null, null, null).equals("test25"));
		fcElt.setAttribute("nom", "null");
		assertTrue(interfacageParsingUtils.formateValueUsingFunction(null, "test25", null, null, null, null, null).equals("test25"));
		fcElt.setAttribute("nom", "stringToUpperCase");
		assertNull(interfacageParsingUtils.formateValueUsingFunction(fcElt, null, null, null, null, null, null));

		// test emplacement
		StringBuilder bld = new StringBuilder();
		ConfigurationParsing conf = new ConfigurationParsing();
		conf.setSeparateurComposants("^");
		fcElt.setAttribute("nom", "emplacementAddr");
		fcElt.setAttribute("niveau", "conteneur");
		Hashtable<String, String> contMap = new Hashtable<String, String>();
		contMap.put("AP9 -80°C", "C1");
		assertNull(interfacageParsingUtils.formateValueUsingFunction(fcElt, "C2", null, null, null, bld, contMap));
		assertTrue(bld.toString().equals("C2"));
		bld = new StringBuilder();
		assertNull(interfacageParsingUtils.formateValueUsingFunction(fcElt, "AP9 -80°C", null, null, null, bld, contMap));
		assertTrue(bld.toString().equals("C1"));    
		fcElt.setAttribute("niveau", "enceinte");
		Hashtable<String, String> encMap = new Hashtable<String, String>();
		encMap.put("Rayon2", "R2");
		encMap.put("Tiroir3", "T3");
		assertNull(interfacageParsingUtils.formateValueUsingFunction(fcElt, "Cas^1^Rayon^2^Tiroir^3", null, conf, null, bld, encMap));
		assertTrue(bld.toString().equals("C1.Cas1.R2.T3"));
		fcElt.setAttribute("niveau", "boite");
		assertNull(interfacageParsingUtils.formateValueUsingFunction(fcElt, "2FIBROCHIR", null, conf, null, bld, null));
		assertTrue(bld.toString().equals("C1.Cas1.R2.T3.2FIBROCHIR"));
		fcElt.setAttribute("niveau", "emplacement");
		assertTrue(interfacageParsingUtils.formateValueUsingFunction(fcElt, "A^06^0", null, conf, null, bld, null).equals("C1.Cas1.R2.T3.2FIBROCHIR.A-6"));
		assertTrue(bld.toString().equals("C1.Cas1.R2.T3.2FIBROCHIR.A-6"));

		// terminale alias found
		bld = new StringBuilder("C1.Cas1.R2.T3");
		fcElt.setAttribute("niveau", "boite");
		fcElt.setAttribute("alias", "true");
		assertNull(interfacageParsingUtils.formateValueUsingFunction(fcElt, "ALIAS BT5", null, conf, null, bld, null));
		assertTrue(bld.toString().equals("C1.Cas1.R2.T3.BT5"));
		fcElt.setAttribute("niveau", "emplacement");
		assertTrue(interfacageParsingUtils.formateValueUsingFunction(fcElt, "A^06^0", null, conf, null, bld, null).equals("C1.Cas1.R2.T3.BT5.A-6"));
		assertTrue(bld.toString().equals("C1.Cas1.R2.T3.BT5.A-6"));	  

	}

	/**
	 * @version 2.2.3-genno
	 * @throws Exception
	 */
	@Test
	public void testParseInterfacageXmlFile() throws Exception{
		final Emetteur e1 = emetteurDao.findById(1);
		final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";
		final Integer nbD = dossierExterneManager.findAllObjectsManager().size();
		final Integer nbB = blocExterneManager.findAllObjectsManager().size();
		final Integer nbV = valeurExterneManager.findAllObjectsManager().size();

		InputStream input = null;
		try{
			input = new FileInputStream(folder + "ANP59248.AST");
			//InputStream input = new FileInputStream(folder + "apix-message.hl7");
			final byte[] ba = new byte[input.available()];
			input.read(ba);
			final String str = new String(ba);

			DossierExterne dossier =
					interfacageParsingUtils
					.parseInterfacageXmlFile(folder + "interfacage.xml", str, e1, false, 10, null).get(0);
			//		DossierExterne dossier = interfacageParsingUtils
			//			.parseInterfacageXmlFile(
			//			folder + "apix-mapping.xml", str, e1);
			assertTrue(dossierExterneManager.findAllObjectsManager().size() == nbD + 1);
			assertTrue(blocExterneManager.findAllObjectsManager().size() == nbB + 4);
			assertTrue(valeurExterneManager.findAllObjectsManager().size() == nbV + 15);
			assertTrue(dossier.getIdentificationDossier().equals("ANP59248"));
			assertTrue(dossier.getOperation().equals("ORU"));
			assertTrue(format.format(dossier.getDateOperation().getTime()).equals("2007-05-03"));
			assertTrue(dossier.getEntiteId() == null);

			// teste la presence du contenu byte[]
			byte[] contenu = null;
			for(final ValeurExterne val : valeurExterneManager.findAllObjectsManager()){
				if(val.getContenu() != null){
					if(contenu != null){
						// 1 seule valeur a un contenu...
						assertTrue(false);
					}
					contenu = val.getContenu();
					assertTrue(contenu.length > 0);
				}
			}

			// suppression
			dossierExterneManager.removeObjectManager(dossier);
			assertTrue(dossierExterneManager.findAllObjectsManager().size() == nbD);
			assertTrue(blocExterneManager.findAllObjectsManager().size() == nbB);
			assertTrue(valeurExterneManager.findAllObjectsManager().size() == nbV);
			
			// @since 2.2.3-genno
			// Const
			dossier = interfacageParsingUtils
				.parseInterfacageXmlFile(folder + "interfacage_const.xml", str, e1, false, 10, null).get(0);
			//		DossierExterne dossier = interfacageParsingUtils
			//			.parseInterfacageXmlFile(
			//			folder + "apix-mapping.xml", str, e1);
			assertTrue(dossierExterneManager.findAllObjectsManager().size() == nbD + 1);
			assertTrue(blocExterneManager.findAllObjectsManager().size() == nbB + 1);
			assertTrue(valeurExterneManager.findAllObjectsManager().size() == nbV + 3);
			assertTrue(dossier.getIdentificationDossier().equals("ANP59248"));

			// teste le bon enregistrement de la constante
			BlocExterne bPrel = blocExterneManager
					.findByDossierExterneManager(dossier).iterator().next();
			List<ValeurExterne> vals = valeurExterneManager
					.findByBlocExterneManager(bPrel);
			assertTrue(vals.size() == 3);
			ValeurExterne valToAssert = new ValeurExterne();
			valToAssert.setChampEntiteId(27); valToAssert.setBlocExterne(bPrel);
			assertTrue(vals.get(vals.indexOf(valToAssert)).getValeur().equals("12"));

			// suppression
			dossierExterneManager.removeObjectManager(dossier);
			assertTrue(dossierExterneManager.findAllObjectsManager().size() == nbD);
			assertTrue(blocExterneManager.findAllObjectsManager().size() == nbB);
			assertTrue(valeurExterneManager.findAllObjectsManager().size() == nbV);
			
			
		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}finally{
			if(input != null){
				input.close();
			}
		}
	}

	@Test
	public void testAPIX2010NipBug() throws IOException{
		// init
		final String folder = "src/test/java/fr/aphp/tumorotek/manager/test/interfacage/";

		InputStream input = null;
		try{
			input = new FileInputStream(folder + "apix_2010_nip.hl7");

			final byte[] ba = new byte[input.available()];
			input.read(ba);
			final String str = new String(ba);
			// On crée une instance de SAXBuilder
			final SAXBuilder sxb = new SAXBuilder();
			Document document = null;
			//On crée un nouveau document JDOM avec en 
			// argument le fichier XML
			document = sxb.build(new File(folder + "apix-mapping.xml"));

			// On initialise un nouvel élément racine avec 
			// l'élément racine du document.
			final Element racine = document.getRootElement();
			final ConfigurationParsing conf = interfacageParsingUtils.initConfigurationParsing(racine);
			final Hashtable<String, List<String>> contenu = 
					interfacageParsingUtils.parseFileToInjectInTk(conf, str).get(0);

			// test niveau 2
			final String value = interfacageParsingUtils.getValueFromEmplacement(contenu.get("PID"), conf, "3.1");
			assertTrue(value.equals("8003773399"));
		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}finally{
			if(input != null){
				input.close();
			}
		}
	}


	/**
	 * @since 2.2.2-diamic
	 * @throws IOException
	 */
	@Test   
	public void testDIAMICEvolutionMultiORC() throws IOException{
		final Emetteur e1 = emetteurDao.findById(1);
		final String folder = "src/test/resources/data/DIAMIC/";
		final Integer nbD = dossierExterneManager.findAllObjectsManager().size();
		final Integer nbB = blocExterneManager.findAllObjectsManager().size();
		final Integer nbV = valeurExterneManager.findAllObjectsManager().size();

		InputStream input = null;

		final List<DossierExterne> dossiers = new ArrayList<DossierExterne>();
		try{
			input = new FileInputStream(folder.concat("diamic-multiORC.hl7"));
			final byte[] ba = new byte[input.available()];
			input.read(ba);
			final String str = new String(ba);

			dossiers.addAll(
					interfacageParsingUtils
					.parseInterfacageXmlFile(folder.concat("diamic-mapping.xml"), str, e1, false, 10, null));

			assertTrue(dossierExterneManager.findAllObjectsManager().size() == nbD + 2);
			assertTrue(blocExterneManager.findAllObjectsManager().size() == nbB + 8);
			assertTrue(valeurExterneManager.findAllObjectsManager().size() == nbV + 36);
			assertTrue(dossiers.size() == 2);
			assertTrue(dossiers.get(0).getIdentificationDossier().equals("S1600013-1"));
			assertTrue(dossiers.get(0).getOperation().equals("CM"));
			assertTrue(dossiers.get(0).getEntiteId() == null);
			assertTrue(format.format(dossiers.get(0).getDateOperation().getTime()).equals("2016-02-08"));
			assertTrue(dossiers.get(1).getIdentificationDossier().equals("S1600013b-2"));
			assertTrue(dossiers.get(1).getOperation().equals("IP"));
			assertTrue(format.format(dossiers.get(1).getDateOperation().getTime()).equals("2016-02-08"));
			assertTrue(dossiers.get(1).getEntiteId() == null);

			// teste la bonne répartition des valeurs
			ValeurExterne nip = new ValeurExterne(); nip.setChampEntiteId(2);
			ValeurExterne nom = new ValeurExterne(); nom.setChampEntiteId(3);
			ValeurExterne prenom = new ValeurExterne(); prenom.setChampEntiteId(5);
			ValeurExterne sexe = new ValeurExterne(); sexe.setChampEntiteId(6);
			ValeurExterne dateNais = new ValeurExterne(); dateNais.setChampEntiteId(7);
			ValeurExterne codePrel = new ValeurExterne(); codePrel.setChampEntiteId(23);
			ValeurExterne numLabo = new ValeurExterne(); numLabo.setChampEntiteId(45);
			ValeurExterne datePrel = new ValeurExterne(); datePrel.setChampEntiteId(30);
			ValeurExterne dateArr = new ValeurExterne(); dateArr.setChampEntiteId(38);
			ValeurExterne servicePrel = new ValeurExterne(); servicePrel.setChampEntiteId(29);
			ValeurExterne cr = new ValeurExterne(); cr.setChampEntiteId(255);
			ValeurExterne codeLes = new ValeurExterne(); codeLes.setChampEntiteId(230);
			ValeurExterne codeOrg = new ValeurExterne(); codeOrg.setChampEntiteId(229);
			ValeurExterne codeEchan = new ValeurExterne(); codeEchan.setChampEntiteId(54);
			ValeurExterne eType = new ValeurExterne(); eType.setChampEntiteId(58);
			ValeurExterne dateStock = new ValeurExterne(); dateStock.setChampEntiteId(56);
			ValeurExterne collab = new ValeurExterne(); collab.setChampEntiteId(53);
			ValeurExterne empl = new ValeurExterne(); empl.setChampEntiteId(57);

			// dossier 1
			assertTrue(blocExterneManager.findByDossierExterneManager(dossiers.get(0)).size() == 3);
			BlocExterne patBloc = blocExterneManager
					.findByDossierExterneAndEntiteManager(dossiers.get(0), entiteDao.findById(1)).get(0);
			List<ValeurExterne> vals = valeurExterneManager.findByBlocExterneManager(patBloc);
			assertTrue(vals.size() == 5);
			nip.setBlocExterne(patBloc);
			assertTrue(vals.get(vals.indexOf(nip)).getValeur().equals("0123793941"));
			nom.setBlocExterne(patBloc);
			assertTrue(vals.get(vals.indexOf(nom)).getValeur().equals("TESTUD"));
			prenom.setBlocExterne(patBloc);
			assertTrue(vals.get(vals.indexOf(prenom)).getValeur().equals("CORINNE"));
			sexe.setBlocExterne(patBloc);
			assertTrue(vals.get(vals.indexOf(sexe)).getValeur().equals("M"));
			dateNais.setBlocExterne(patBloc);
			assertTrue(vals.get(vals.indexOf(dateNais)).getValeur().equals("19150823"));
			BlocExterne prelBloc = blocExterneManager
					.findByDossierExterneAndEntiteManager(dossiers.get(0), entiteDao.findById(2)).get(0);
			vals = valeurExterneManager.findByBlocExterneManager(prelBloc);
			assertTrue(vals.size() == 5);
			codePrel.setBlocExterne(prelBloc);
			assertTrue(vals.get(vals.indexOf(codePrel)).getValeur().equals("S1600013-1"));
			numLabo.setBlocExterne(prelBloc);
			assertTrue(vals.get(vals.indexOf(numLabo)).getValeur().equals("16020080148"));
			datePrel.setBlocExterne(prelBloc);
			assertTrue(vals.get(vals.indexOf(datePrel)).getValeur().equals("201602011022"));
			dateArr.setBlocExterne(prelBloc);
			assertTrue(vals.get(vals.indexOf(dateArr)).getValeur().equals("201602031456"));
			servicePrel.setBlocExterne(prelBloc);
			assertTrue(vals.get(vals.indexOf(servicePrel)).getValeur().equals("1589"));
			BlocExterne echanBloc = blocExterneManager
					.findByDossierExterneAndEntiteManager(dossiers.get(0), entiteDao.findById(3)).get(0);
			vals = valeurExterneManager.findByBlocExterneManager(echanBloc);
			assertTrue(vals.size() == 3);
			cr.setBlocExterne(echanBloc);
			assertTrue(vals.get(vals.indexOf(cr)).getValeur().equals("000000058_S1600013.PDF"));
			codeLes.setBlocExterne(echanBloc);
			assertTrue(vals.get(vals.indexOf(codeLes)).getValeur().equals("0000"));
			codeOrg.setBlocExterne(echanBloc);
			assertTrue(vals.get(vals.indexOf(codeOrg)).getValeur().equals("SA"));

			// dossier 2
			assertTrue(blocExterneManager.findByDossierExterneManager(dossiers.get(1)).size() == 5);
			patBloc = blocExterneManager
					.findByDossierExterneAndEntiteManager(dossiers.get(1), entiteDao.findById(1)).get(0);
			vals = valeurExterneManager.findByBlocExterneManager(patBloc);
			assertTrue(vals.size() == 5);
			nip.setBlocExterne(patBloc);
			assertTrue(vals.get(vals.indexOf(nip)).getValeur().equals("0123793941"));
			nom.setBlocExterne(patBloc);
			assertTrue(vals.get(vals.indexOf(nom)).getValeur().equals("TESTUD"));
			prenom.setBlocExterne(patBloc);
			assertTrue(vals.get(vals.indexOf(prenom)).getValeur().equals("CORINNE"));
			sexe.setBlocExterne(patBloc);
			assertTrue(vals.get(vals.indexOf(sexe)).getValeur().equals("M"));
			dateNais.setBlocExterne(patBloc);
			assertTrue(vals.get(vals.indexOf(dateNais)).getValeur().equals("19150823"));
			prelBloc = blocExterneManager
					.findByDossierExterneAndEntiteManager(dossiers.get(1), entiteDao.findById(2)).get(0);
			vals = valeurExterneManager.findByBlocExterneManager(prelBloc);
			assertTrue(vals.size() == 5);
			codePrel.setBlocExterne(prelBloc);
			assertTrue(vals.get(vals.indexOf(codePrel)).getValeur().equals("S1600013b-2"));
			numLabo.setBlocExterne(prelBloc);
			assertTrue(vals.get(vals.indexOf(numLabo)).getValeur().equals("16020080148"));
			datePrel.setBlocExterne(prelBloc);
			assertTrue(vals.get(vals.indexOf(datePrel)).getValeur().equals("201706081212"));
			dateArr.setBlocExterne(prelBloc);
			assertTrue(vals.get(vals.indexOf(dateArr)).getValeur().equals("201708091213"));
			servicePrel.setBlocExterne(prelBloc);
			assertTrue(vals.get(vals.indexOf(servicePrel)).getValeur().equals("1589"));
			echanBloc = blocExterneManager
					.findByDossierExterneAndEntiteManager(dossiers.get(1), entiteDao.findById(3)).get(0);
			vals = valeurExterneManager.findByBlocExterneManager(echanBloc);
			assertTrue(vals.size() == 3);
			cr.setBlocExterne(echanBloc);
			assertTrue(vals.get(vals.indexOf(cr)).getValeur().equals("000000059_S1600018.PDF"));
			codeLes.setBlocExterne(echanBloc);
			assertTrue(vals.get(vals.indexOf(codeLes)).getValeur().equals("K7G7"));
			codeOrg.setBlocExterne(echanBloc);
			assertTrue(vals.get(vals.indexOf(codeOrg)).getValeur().equals("OT"));
			BlocExterne spmBloc = blocExterneManager
					.findByDossierExterneAndEntiteManager(dossiers.get(1), entiteDao.findById(3)).get(1);
			vals = valeurExterneManager.findByBlocExterneManager(spmBloc);
			assertTrue(vals.size() == 5);
			codeEchan.setBlocExterne(spmBloc);
			assertTrue(vals.get(vals.indexOf(codeEchan)).getValeur().equals("S1600018/B-1"));
			eType.setBlocExterne(spmBloc);
			assertTrue(vals.get(vals.indexOf(eType)).getValeur().equals("T"));
			dateStock.setBlocExterne(spmBloc);
			assertTrue(vals.get(vals.indexOf(dateStock)).getValeur().equals("201602021400"));
			collab.setBlocExterne(spmBloc);
			assertTrue(vals.get(vals.indexOf(collab)).getValeur().equals("1212"));
			empl.setBlocExterne(spmBloc);
			assertTrue(vals.get(vals.indexOf(empl)).getValeur().equals("C1.R1.Tiroir1.FIBRO CHIR.B-9"));
			spmBloc = blocExterneManager
					.findByDossierExterneAndEntiteManager(dossiers.get(1), entiteDao.findById(3)).get(2);
			vals = valeurExterneManager.findByBlocExterneManager(spmBloc);
			assertTrue(vals.size() == 5);
			codeEchan.setBlocExterne(spmBloc);
			assertTrue(vals.get(vals.indexOf(codeEchan)).getValeur().equals("S1600018/B-2"));
			eType.setBlocExterne(spmBloc);
			assertTrue(vals.get(vals.indexOf(eType)).getValeur().equals("C"));
			dateStock.setBlocExterne(spmBloc);
			assertTrue(vals.get(vals.indexOf(dateStock)).getValeur().equals("201602021415"));
			collab.setBlocExterne(spmBloc);
			assertTrue(vals.get(vals.indexOf(collab)).getValeur().equals("1213"));
			empl.setBlocExterne(spmBloc);
			assertTrue(vals.get(vals.indexOf(empl)).getValeur().equals("C2.Cas1.R2.T3.2FIBROCHIR.A-6"));
		}catch(final Exception e){
			e.printStackTrace();
			assertTrue(false);
		}finally{
			if(input != null){
				input.close();
			}
			// clean up
			if (!dossiers.isEmpty()) {
				dossierExterneManager.removeObjectManager(dossiers.get(0));
				dossierExterneManager.removeObjectManager(dossiers.get(1));
			}
			assertTrue(dossierExterneManager.findAllObjectsManager().size() == nbD);
			assertTrue(blocExterneManager.findAllObjectsManager().size() == nbB);
			assertTrue(valeurExterneManager.findAllObjectsManager().size() == nbV);
		}
	}
}
