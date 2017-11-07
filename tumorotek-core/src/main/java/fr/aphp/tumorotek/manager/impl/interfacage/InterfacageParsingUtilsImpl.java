package fr.aphp.tumorotek.manager.impl.interfacage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import fr.aphp.tumorotek.manager.interfacage.DossierExterneManager;
import fr.aphp.tumorotek.manager.interfacage.EmetteurManager;
import fr.aphp.tumorotek.manager.interfacage.InterfacageParsingUtils;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.utils.Utils;

public class InterfacageParsingUtilsImpl implements InterfacageParsingUtils {

	private Log log = LogFactory.getLog(InterfacageParsingUtils.class);
	
	private DossierExterneManager dossierExterneManager;
	private EmetteurManager emetteurManager;

	public void setDossierExterneManager(DossierExterneManager dManager) {
		this.dossierExterneManager = dManager;
	}

	public void setEmetteurManager(EmetteurManager eManager) {
		this.emetteurManager = eManager;
	}

	@Override
	public ConfigurationParsing initConfigurationParsing(Element parent) {
		ConfigurationParsing conf = new ConfigurationParsing();
		
		// on récupère l'elt Configuration
		Element confElt = parent.getChild("Configuration");
		if (confElt != null) {
			// on récupère tous les séparateurs
			Element sepElt = confElt.getChild("SeparateurChamps");
			if (sepElt != null) {
				conf.setSeparateurChamps(sepElt.getText());
			} else {
				log.info("initConfigurationParsing : " 
					+ "Balise 'SeparateurChamps' absente du fichier.");
			}
			sepElt = confElt.getChild("SeparateurComposants");
			if (sepElt != null) {
				conf.setSeparateurComposants(sepElt.getText());
			} else {
				log.info("initConfigurationParsing : " 
					+ "Balise 'SeparateurComposants' absente du fichier.");
			}
			sepElt = confElt.getChild("SeparateurSousComposants");
			if (sepElt != null) {
				conf.setSeparateurSousComposants(sepElt.getText());
			} else {
				log.info("initConfigurationParsing : " 
						+ "Balise 'SeparateurSousComposants' " 
						+ "absente du fichier.");
			}
			sepElt = confElt.getChild("BlocLibreKey");
			if (sepElt != null) {
				conf.setBlocLibreKey(sepElt.getText());
			} else {
				log.info("initConfigurationParsing : " 
					+ "Balise 'BlocLibreKey' absente du fichier.");
			}
		} else {
			log.warn("initConfigurationParsing : " 
					+ "Balise 'Configuration' absente du fichier.");
		}
		
		return conf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Emetteur extractEmetteurFromFileToInjectInTk(String fileXml,
			String file, String boiteFtp) throws IOException {
		
		Emetteur emetteurObj = null;
		
		if (file != null && fileXml != null && boiteFtp != null) {
			// On crée une instance de SAXBuilder
			SAXBuilder sxb = new SAXBuilder();
			Document document = null;
			try {
				//On crée un nouveau document JDOM avec en 
				// argument le fichier XML
				document = sxb.build(new File(fileXml));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// On initialise un nouvel élément racine avec 
			// l'élément racine du document.
			Element racine = document.getRootElement();
			// on récupère toutes les boites ftp que l'on va parcourir
			List<Element> boites = racine.getChildren("BoiteFtp");
			for (int i = 0; i < boites.size(); i++) {
				// on va traiter la boite correspondant à celle passée
				// en paramètre
				if (boites.get(i).getAttributeValue("nom").equals(boiteFtp)) {
					// init de la config pour parser le message
					ConfigurationParsing config = initConfigurationParsing(
							boites.get(i));
					
					// EXTRACTION DE L'EMETTEUR
					// extraction du bloc
					String bloc = boites.get(i).getChild("Emetteur")
						.getChildText("Bloc");
					// extraction de l'emplacement
					String emplacement = boites.get(i).getChild("Emetteur")
						.getChildText("Emplacement");
					
					String emetteur = null;
					if (bloc != null && emplacement != null) {
						Hashtable<String, List<String>> contenu = 
							parseFileToInjectInTk(config, file);
						emetteur = getValueFromBlocAndEmplacement(
								contenu, config, bloc, emplacement);
					}
					
					// EXTRACTION DU SERVICE
					// extraction du bloc
					bloc = boites.get(i).getChild("Service")
						.getChildText("Bloc");
					// extraction de l'emplacement
					emplacement = boites.get(i).getChild("Service")
						.getChildText("Emplacement");
					
					String service = null;
					if (bloc != null && emplacement != null) {
						Hashtable<String, List<String>> contenu = 
							parseFileToInjectInTk(config, file);
						service = getValueFromBlocAndEmplacement(
								contenu, config, bloc, emplacement);
					}
					
					// recherche de l'emetteur en fct de son nom
					// et de son service
					List<Emetteur> emts = emetteurManager
						.findByIdentificationAndServiceManager(
								emetteur, service);
					
					if (emts.size() == 1) {
						emetteurObj = emts.get(0);
					}
				}
			}
		} else {
			if (file == null) {
				log.warn("extractEmetteurFromFileToInjectInTk : " 
						+ "Le fichier à injecter dans TK est NULL");
			}
			if (fileXml == null) {
				log.warn("extractEmetteurFromFileToInjectInTk : " 
						+ "Le ficher XML de configuration est NULL");
			}
			if (file == null) {
				log.warn("extractEmetteurFromFileToInjectInTk : " 
						+ "La boite ftp ayant reçu le message est NULL");
			}
		}
		return emetteurObj;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String extractXMLFIleFromFileToInjectInTk(String fileXml,
			String file, String boiteFtp) 
	throws IOException {
		String xml = null;
		
		if (file != null && fileXml != null && boiteFtp != null) {
//				&& emetteur != null) {
			// On crée une instance de SAXBuilder
			SAXBuilder sxb = new SAXBuilder();
			Document document = null;
			try {
				//On crée un nouveau document JDOM avec en 
				// argument le fichier XML
				document = sxb.build(new File(fileXml));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// On initialise un nouvel élément racine avec 
			// l'élément racine du document.
			Element racine = document.getRootElement();
			// on récupère toutes les boites ftp que l'on va parcourir
			List<Element> boites = racine.getChildren("BoiteFtp");
			for (int i = 0; i < boites.size(); i++) {
				// on va traiter la boite correspondant à celle passée
				// en paramètre
				if (boites.get(i).getAttributeValue("nom").equals(boiteFtp)) {
					List<Element> mappings = boites.get(i)
						.getChildren("MappingXml");
					// pour chaque mapping
					for (int j = 0; j < mappings.size(); j++) {
						// on recherche l'emetteur correspondant
						// à celui passé en params
	//					String id = mappings.get(j).getChildText("EmetteurId");
	//					if (emetteur.getEmetteurId()
	//							.equals(Integer.parseInt(id))) {
							xml = mappings.get(j).getChildText("XML");
	//					}
					}
				}
			}
		} else {
			if (file == null) {
				log.warn("extractXMLFIleFromFileToInjectInTk : " 
						+ "Le fichier à injecter dans TK est NULL");
			}
			if (fileXml == null) {
				log.warn("extractXMLFIleFromFileToInjectInTk : " 
						+ "Le ficher XML de configuration est NULL");
			}
			if (file == null) {
				log.warn("extractXMLFIleFromFileToInjectInTk : " 
						+ "La boite ftp ayant reçu le message est NULL");
			}
		}
		return xml;
	}
	
	@Override
	public Hashtable<String, List<String>> parseFileToInjectInTk(
			ConfigurationParsing conf, String file) throws IOException {

		InputStream is = new java.io.ByteArrayInputStream(file.getBytes());

		BufferedReader buff = new BufferedReader(new InputStreamReader(is));
		
		Hashtable<String, List<String>> contenu = 
			new Hashtable<String, List<String>>();

		try {
			String line;
			String currentKey = "";
			// Lecture du fichier ligne par ligne. Cette boucle se termine
			// quand la méthode retourne la valeur null.
			while ((line = buff.readLine()) != null) {
				if (conf.getSeparateurChamps() != null) {
					// on divise la ligne en fct du séparateur
					List<String> values = new ArrayList<String>();
					
					// dans le cas où un élément ne serait pas
					// présent, les séparateurs seront à coté (||)
					// pour pouvoir conserver cet élément vide dans
					// la liste finale, on injecte un espace
					StringBuffer sb1 = new StringBuffer();
					sb1.append(conf.getSeparateurChamps());
					sb1.append(conf.getSeparateurChamps());
					StringBuffer sb2 = new StringBuffer();
					sb2.append(conf.getSeparateurChamps());
					sb2.append(" ");
					sb2.append(conf.getSeparateurChamps());
					while (line.contains(sb1.toString())) {
						line = line.replace(sb1.toString(), 
								sb2.toString());
					}
					
					// si la ligne se termine par un séparateur, on rajoute
					// un espace pour que cette élément soit pris en compte
					if (line.endsWith(conf.getSeparateurChamps())
							|| line.endsWith(conf.getSeparateurComposants())
							|| line.endsWith(
									conf.getSeparateurSousComposants())) {
						StringBuffer tmp = new StringBuffer(line);
						tmp.append(" ");
						line = tmp.toString();
					}
					
					StringTokenizer st = new StringTokenizer(
							line, conf.getSeparateurChamps());
					int cpt = 0;
					String key = "";
					while (st.hasMoreTokens()) {
						if (cpt == 0) {
							// le 1er élt de la ligne est sa clé
							key = st.nextToken();
							if (!key.equals("OBX")) {
								currentKey = key;
							} else {
								// si la clé est un OBX, on va créer
								// une clé basée sur l'élt parent de
								// cet OBX (pusiqu'il peut y en avoir 
								// plusieurs
								StringBuffer sb = new StringBuffer();
								sb.append(currentKey);
								sb.append("_");
								sb.append(key);
								key = sb.toString();
							}
						} else {
							values.add(st.nextToken());
						}
						++cpt;
					}
					
					// si c'est un OBX, on ajoute l'id de celui-ci
					if (key.contains("OBX")) {
						if (values.get(0) != null) {
							key = key.concat(values.get(0));
						}
					}
					contenu.put(key, values);
				}
			}
		} finally {
			// dans tous les cas, on ferme nos flux
			buff.close();
		}
		
		return contenu;
	}
	
	@Override
	public List<String> parseFileToExtractBlocsLibres(
			ConfigurationParsing conf, String file) throws IOException {
		InputStream is = new java.io.ByteArrayInputStream(file.getBytes());

		BufferedReader buff = new BufferedReader(new InputStreamReader(is));
		
		List<String> blocsLibres = new ArrayList<String>();

		try {
			String line;
			// Lecture du fichier ligne par ligne. Cette boucle se termine
			// quand la méthode retourne la valeur null.
			while ((line = buff.readLine()) != null) {
				if (conf.getSeparateurChamps() != null) {
					// dans le cas où un élément ne serait pas
					// présent, les séparateurs seront à coté (||)
					// pour pouvoir conserver cet élément vide dans
					// la liste finale, on injecte un espace
					StringBuffer sb1 = new StringBuffer();
					sb1.append(conf.getSeparateurChamps());
					sb1.append(conf.getSeparateurChamps());
					StringBuffer sb2 = new StringBuffer();
					sb2.append(conf.getSeparateurChamps());
					sb2.append(" ");
					sb2.append(conf.getSeparateurChamps());
					while (line.contains(sb1.toString())) {
						line = line.replace(sb1.toString(), 
								sb2.toString());
					}
					
					// si la ligne se termine par un séparateur, on rajoute
					// un espace pour que cette élément soit pris en compte
					if (line.endsWith(conf.getSeparateurChamps())
							|| line.endsWith(conf.getSeparateurComposants())
							|| line.endsWith(
									conf.getSeparateurSousComposants())) {
						StringBuffer tmp = new StringBuffer(line);
						tmp.append(" ");
						line = tmp.toString();
					}
					
					StringTokenizer st = new StringTokenizer(
							line, conf.getSeparateurChamps());
					int cpt = 0;
					String key = "";
					while (st.hasMoreTokens()) {
						// on ne s'occupe que du 1er élément de la ligne
						if (cpt == 0) {
							// le 1er élt de la ligne est sa clé
							key = st.nextToken();
							// si la clé correspond à un bloc libre
							if (key.equals(conf.getBlocLibreKey())) {
								// on enlève cette clé et le premier séparateur
								StringBuffer sb = new StringBuffer();
								sb.append(conf.getBlocLibreKey());
								sb.append(conf.getSeparateurChamps());
								// on ajoute le contenu de la ligne au résultat
								String value = line.replace(sb.toString(), "");
								blocsLibres.add(value);
							}
						} else {
							st.nextToken();
						}
						++cpt;
					}
				}
			}
		} finally {
			// dans tous les cas, on ferme nos flux
			buff.close();
		}
		
		return blocsLibres;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Hashtable<String, String> extractMappingValuesForThesaurs(
			Element element) {
		Hashtable<String, String> mappings = new Hashtable<String, String>();
		
		if (element != null && element.getName().equals("Modifier")
				&& element.getAttribute("nom")
				.getValue().equals("Thesaurus")) {
			
			// on extrait les mappings
			List<Element> mappingThes = element.getChildren("MappingThes");
			for (int i = 0; i < mappingThes.size(); i++) {
				String source = null;
				String tk = null;
				// on extrait chaque mapping pour le mettre dans la
				// hashtable
				if (mappingThes.get(i).getChild("SourceThes") != null
						&& mappingThes.get(i).getChild("SourceThes")
							.getText() != null
						&& !mappingThes.get(i).getChild("SourceThes")
							.getText().equals("")) {
					source = mappingThes.get(i)
						.getChild("SourceThes").getText();
				}
				if (mappingThes.get(i).getChild("TkThes") != null
						&& mappingThes.get(i).getChild("TkThes")
							.getText() != null
						&& !mappingThes.get(i).getChild("TkThes")
							.getText().equals("")) {
					tk = mappingThes.get(i)
						.getChild("TkThes").getText();
				}
				
				if (source != null && tk != null) {
					mappings.put(source, tk);
				}
			}
			
		}
		
		return mappings;
	}
	
	@Override
	public DossierExterne initNewDossierExterne(ConfigurationParsing config,
			Hashtable<String, List<String>> contenu, Element racine) {
		DossierExterne dossier = new DossierExterne();
		
		if (config != null && contenu != null && racine != null) {
			// on récupère l'elt Configuration
			Element confElt = racine.getChild("Configuration");
			// extraction du numéro de dossier
			if (confElt.getChild("NumeroDossier") != null) {
				String bloc = confElt.getChild("NumeroDossier")
					.getChildText("BlocValue");
				String emp = confElt.getChild("NumeroDossier")
					.getChildText("Key");
				dossier.setIdentificationDossier(getValueFromBlocAndEmplacement(
						contenu, config, bloc, emp));
			} else {
				log.info("initNewDossierExterne : " 
					+ "La balise 'NumeroDossier' est absente du fichier");
			}
			
			// extraction du numéro de la date
			if (confElt.getChild("DateDossier") != null) {
				String bloc = confElt.getChild("DateDossier")
					.getChildText("BlocValue");
				String emp = confElt.getChild("DateDossier")
					.getChildText("Key");
				String dateStr = getValueFromBlocAndEmplacement(
						contenu, config, bloc, emp);
				
				Date date = parseHl7Date(dateStr);
				
				Calendar cal = Calendar.getInstance();
				if (date != null) {
					cal.setTime(date);
				} else {
					cal = null;
				}
				dossier.setDateOperation(cal);
			} else {
				log.info("initNewDossierExterne : " 
					+ "La balise 'DateDossier' est absente du fichier");
			}
			
			// extraction du numéro de l'opération
			if (confElt.getChild("OperationDossier") != null) {
				String bloc = confElt.getChild("OperationDossier")
					.getChildText("BlocValue");
				String emp = confElt.getChild("OperationDossier")
					.getChildText("Key");
				dossier.setOperation(getValueFromBlocAndEmplacement(
						contenu, config, bloc, emp));
			} else {
				log.info("initNewDossierExterne : " 
					+ "La balise 'OperationDossier' est absente du fichier");
			}
		} else {
			if (config == null) {
				log.warn("initNewDossierExterne : " 
						+ "La configuration pour parser le message " 
						+ "est NULL");
			}
			if (contenu == null) {
				log.warn("initNewDossierExterne : " 
						+ "Le fichier à injecter dans TK " 
						+ "est NULL");
			}
			if (racine == null) {
				log.warn("initNewDossierExterne : " 
						+ "La racine du fichier de mapping " 
						+ "est NULL");
			}
		}
		
		return dossier;
	}
	
	@Override
	public String getValueFromEmplacement(
			List<String> values, ConfigurationParsing conf,
			String emplacement) {
		
		String value = null;
		
		if (values != null && conf != null
				&& emplacement != null) {
			// on divise l'emplacement
			List<String> empls = new ArrayList<String>();
			if (emplacement.contains(".")) {
				StringTokenizer st = new StringTokenizer(
						emplacement, ".");
				while (st.hasMoreTokens()) {
					empls.add(st.nextToken());
				}
			} else {
				empls.add(emplacement);
			}
			
			String niv1 = null;
			// on récupère la valeur de la liste plassée à l'emplacement
			// demandé (si elle existe
			if (values.size() >= Integer.parseInt(empls.get(0))) {
				niv1 = values.get(Integer.parseInt(empls.get(0)) - 1);
				if (niv1.equals(" ") || niv1.equals("")) {
					niv1 = null;
				}
			}
			
			// si un deuxième niveau de subdivision est demandé
			if (empls.size() > 1 && niv1 != null) {
				// dans le cas où un élément ne serait pas
				// présent, les séparateurs seront à coté (~~)
				// pour pouvoir conserver cet élément vide dans
				// la liste finale, on injecte un espace
				StringBuffer sb1 = new StringBuffer();
				sb1.append(conf.getSeparateurComposants());
				sb1.append(conf.getSeparateurComposants());
				StringBuffer sb2 = new StringBuffer();
				sb2.append(conf.getSeparateurComposants());
				sb2.append(" ");
				sb2.append(conf.getSeparateurComposants());
				while (niv1.contains(sb1.toString())) {
					niv1 = niv1.replace(sb1.toString(), 
							sb2.toString());
				}
				
				// si la ligne commence par un séparateur, on rajoute
				// un espace pour que cette élément soit pris en compte
				if (niv1.startsWith(conf.getSeparateurComposants())
						|| niv1.startsWith(
								conf.getSeparateurSousComposants())) {
					StringBuffer tmp = new StringBuffer(" ");
					tmp.append(niv1);
					niv1 = tmp.toString();
				}
				
				// si la ligne se termine par un séparateur, on rajoute
				// un espace pour que cette élément soit pris en compte
				if (niv1.endsWith(conf.getSeparateurComposants())
						|| niv1.endsWith(
								conf.getSeparateurSousComposants())) {
					StringBuffer tmp = new StringBuffer(niv1);
					tmp.append(" ");
					niv1 = tmp.toString();
				}
				
				// on sépare la valeur en fct du séparateur 
				// de composants
				List<String> niv2s = new ArrayList<String>();
				if (niv1.contains(conf.getSeparateurComposants())) {
					StringTokenizer st = new StringTokenizer(
							niv1, conf.getSeparateurComposants());
					while (st.hasMoreTokens()) {
						niv2s.add(st.nextToken());
					}
					
					// on extrait la valeur plassée à l'emplacement
					// demandé
					String niv2 = null;
					if (niv2s.size() >= Integer.parseInt(empls.get(1))) {
						niv2 = niv2s.get(Integer.parseInt(
								empls.get(1)) - 1);
						if (niv2.equals(" ") || niv2.equals("")) {
							niv2 = null;
						}
					}
					
					// si un troisième niveau de subdivision est demandé
					if (empls.size() > 2 && niv2 != null) {
						// dans le cas où un élément ne serait pas
						// présent, les séparateurs seront à coté (~~)
						// pour pouvoir conserver cet élément vide dans
						// la liste finale, on injecte un espace
						StringBuffer sb11 = new StringBuffer();
						sb11.append(conf.getSeparateurSousComposants());
						sb11.append(conf.getSeparateurSousComposants());
						StringBuffer sb22 = new StringBuffer();
						sb22.append(conf.getSeparateurSousComposants());
						sb22.append(" ");
						sb22.append(conf.getSeparateurSousComposants());
						while (niv2.contains(sb11.toString())) {
							niv2 = niv2.replace(sb11.toString(), 
									sb22.toString());
						}
						
						// si la ligne commence par un séparateur, 
						// on rajoute  un espace pour que cette élément 
						// soit pris en compte
						if (niv2.startsWith(
								conf.getSeparateurSousComposants())) {
							StringBuffer tmp = new StringBuffer(" ");
							tmp.append(niv2);
							niv2 = tmp.toString();
						}
						
						// si la ligne se termine par un séparateur, 
						// on rajoute un espace pour que cette élément 
						// soit pris en compte
						if (niv2.endsWith(
								conf.getSeparateurSousComposants())) {
							StringBuffer tmp = new StringBuffer(niv2);
							tmp.append(" ");
							niv2 = tmp.toString();
						}
						
						// on sépare la valeur en fct du séparateur 
						// de composants
						List<String> niv3s = new ArrayList<String>();
						if (niv2.contains(
								conf.getSeparateurSousComposants())) {
							StringTokenizer st3 = new StringTokenizer(
									niv2, 
									conf.getSeparateurSousComposants());
							while (st3.hasMoreTokens()) {
								niv3s.add(st3.nextToken());
							}
							
							// on extrait la valeur plassée à l'emplacement
							// demandé
							String niv3 = null;
							if (niv3s.size() 
									>= Integer.parseInt(empls.get(2))) {
								niv3 = niv3s.get(Integer.parseInt(
										empls.get(2)) - 1);
								if (niv3.equals(" ") || niv3.equals("")) {
									niv3 = null;
								}
							}
							
							value = niv3;
						} else {
							if (empls.get(2).equals("1")) {
								value = niv2;
							} else {
								return null;
							}
						}
					} else {
						value = niv2;
					}
				} else {
					if (empls.get(1).equals("1")) {
						value = niv1;
					} else {
						return null;
					}
				}
			} else {
				value = niv1;
			}
		}
		
		// check only spaces
		// @since 2.1
		if (value != null && value.trim().equals("")) {
			value = null;
		}
		
		return value;
	}
	
	@Override
	public String getValueFromBlocAndEmplacement(
			Hashtable<String, List<String>> contenu, ConfigurationParsing conf,
			String bloc, String emplacement) {
		String value = null;
		
		if (contenu != null && conf != null && bloc != null
				&& emplacement != null) {
			if (contenu.containsKey(bloc)) {
				value = getValueFromEmplacement(
						contenu.get(bloc), conf, emplacement);
			} else {
				log.info("getValueFromBlocAndEmplacement : " 
						+ "Le bloc '" 
						+ bloc
						+ "' n'est pas présent dans le fichier");
			}
		} else {
			if (conf == null) {
				log.warn("getValueFromBlocAndEmplacement : " 
						+ "La configuration pour parser le message " 
						+ "est NULL");
			}
			if (bloc == null) {
				log.warn("getValueFromBlocAndEmplacement : " 
						+ "Le bloc dans lequel extraire la valeur " 
						+ "est NULL");
			}
			if (contenu == null) {
				log.warn("getValueFromBlocAndEmplacement : " 
						+ "Le contenu du fichier est NULL");
			}
			if (emplacement == null) {
				log.warn("getValueFromBlocAndEmplacement : " 
						+ "L'emplacement de la valeur à extraire " 
						+ "est NULL");
			}
		}
		
		return value;
	}
	
	@Override
	public String getValueFromBlocLibre(List<String> blocsLibres,
			ConfigurationParsing conf, String bloc, String keyBloc,
			String emplacement) {
		String value = null;
		
		if (blocsLibres != null && conf != null && bloc != null
				&& emplacement != null && keyBloc != null) {
			
			int found = -1;
			int cpt = 0;
			// on parcourt tous les blocslibres
			while (cpt < blocsLibres.size() && found < 0) {
				// on recherche le bon bloc libre
				if (blocsLibres.get(cpt).contains(bloc)) {
					List<String> values = new ArrayList<String>();
					// on découpe le bloc libre
					StringTokenizer st = new StringTokenizer(
							blocsLibres.get(cpt), 
							conf.getSeparateurChamps());
					while (st.hasMoreTokens()) {
						values.add(st.nextToken());
					}
					
					// on va vérifier que c'est le bon bloc libre
					String nomBloc = getValueFromEmplacement(
							values, conf, keyBloc);
					if (bloc.equals(nomBloc)) {
						found = cpt;
					}
				}
				cpt++;
			}
			
			// si on a trouvé le bon bloc libre
			if (found >= 0) {
				List<String> values = new ArrayList<String>();
				// on découpe le bloc libre
				StringTokenizer st = new StringTokenizer(
						blocsLibres.get(found), 
						conf.getSeparateurChamps());
				while (st.hasMoreTokens()) {
					values.add(st.nextToken());
				}
				
				value = getValueFromEmplacement(values, conf, emplacement);
			} else {
				log.info("getValueFromBlocLibre : " 
						+ "Le bloc libre '" 
						+ bloc
						+ "' n'est pas présent dans le fichier");
			}
		} else {
			if (conf == null) {
				log.warn("getValueFromBlocLibre : " 
						+ "La configuration pour parser le message " 
						+ "est NULL");
			}
			if (bloc == null) {
				log.warn("getValueFromBlocLibre : " 
						+ "Le bloc dans lequel extraire la valeur " 
						+ "est NULL");
			}
			if (blocsLibres == null) {
				log.warn("getValueFromBlocLibre : " 
						+ "La liste des blocs libres est NULL");
			}
			if (emplacement == null) {
				log.warn("getValueFromBlocLibre : " 
						+ "L'emplacement de la valeur à extraire " 
						+ "est NULL");
			}
		}
		return value;
	}

	@Override
	public String formateValueUsingFunction(String fonction, String value) {
		if (fonction != null && value != null) {
			// en fonction du nom de la fonction, on va appeler
			// la bonne méthode pour formater la valeur
			if ("stringToLowerCase".equals(fonction)) {
				value = Utils.stringToLowerCase(value);
			} else if ("stringToUpperCase".equals(fonction)) {
				value = Utils.stringToUpperCase(value);
			} else if ("replaceCommaByDot".equals(fonction)) {
				value = Utils.replaceCommaByDot(value);
			} else {
				log.info("formateValueUsingFunction : " 
						+ "La fonction de formatage '" 
						+ fonction
						+ "' n'existe pas");
			}
		}
		
		return value;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DossierExterne parseInterfacageXmlFile(String xmlFile,
			String message,
			Emetteur emetteur, boolean delFile, int max) throws Exception {
		// On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		Document document = null;
		try {
			//On crée un nouveau document JDOM avec en 
			// argument le fichier XML
			document = sxb.build(new File(xmlFile));
		} catch (Exception e) {
			log.error(e);
		}
		
		// liste des fichiers associés à supprimer
		// après traitement du message
		List<File> relatedFiles = new ArrayList<File>();
		
		// On initialise un nouvel élément racine avec 
		// l'élément racine du document.
		Element racine = document.getRootElement();
		
		// on init la configuration du parsing
		ConfigurationParsing config = initConfigurationParsing(racine);
		
		// on parse le message pour le transformer en hashtable
		Hashtable<String, List<String>> contenu = parseFileToInjectInTk(
				config, message);
		
		// on parse le message pour récupérer tous les blocs libres
		List<String> contenuLibre = parseFileToExtractBlocsLibres(
				config, message);

		// inits
		List<BlocExterne> blocExternes = 
			new ArrayList<BlocExterne>();
		Hashtable<BlocExterne, List<ValeurExterne>> valeurExternes = 
			new Hashtable<BlocExterne, List<ValeurExterne>>();
		Hashtable<Integer, BlocExterne> entitesBlocs = 
			new Hashtable<Integer, BlocExterne>();
		
		// init du dossier externe
		DossierExterne dossierExterne = initNewDossierExterne(
				config, contenu, racine);
		dossierExterne.setEmetteur(emetteur);

		int ordreBloc = 1;
		// on récupère tous les blocs
		List<Element> blocs = racine.getChildren("Bloc");
		for (int i = 0; i < blocs.size(); i++) {
			List<Element> mappings = blocs.get(i).getChildren("Mapping");
			// pour chaque Mapping
			for (int j = 0; j < mappings.size(); j++) {
				Element tkElt = mappings.get(j).getChild("Tk");
				Element entiteElt = tkElt.getChild("Entite");
				Integer entite = entiteElt.getAttribute("idChamp")
				.getIntValue();

				BlocExterne blocCurrent = null;
				if (entitesBlocs.containsKey(entite)) {
					blocCurrent = entitesBlocs.get(entite);
				} else {
					// création d'un nouveau bloc
					blocCurrent = new BlocExterne();
					blocCurrent.setDossierExterne(dossierExterne);
					blocCurrent.setEntiteId(entite);
					blocCurrent.setOrdre(ordreBloc);
					++ordreBloc;
					entitesBlocs.put(entite, blocCurrent);
					blocExternes.add(blocCurrent);
					valeurExternes.put(blocCurrent,
							new ArrayList<ValeurExterne>());
				}

				// création de la valeur externe
				ValeurExterne valeur = new ValeurExterne();
				valeur.setBlocExterne(blocCurrent);
				if (tkElt.getChild("ChampEntite") != null) {
					valeur.setChampEntiteId(tkElt.getChild("ChampEntite")
							.getAttribute("idChamp").getIntValue());
				} else {
					valeur.setChampAnnotationId(tkElt.getChild("Annotation")
							.getAttribute("idChamp").getIntValue());
				}

				Element sourceElt = mappings.get(j).getChild("Source");
				// extraction de la valeur dans le fichier
				String value = getValueFromBlocAndEmplacement(
						contenu, config, 
						blocs.get(i).getAttributeValue("nom"), 
						sourceElt.getChildText("Key"));
				byte[] file = null;
				
				// on regarde si des fonctions de formatage sont
				// demandées
				if (sourceElt.getChild("Fonctions") != null) {
					List<Element> fonctions = sourceElt.getChild("Fonctions")
						.getChildren("Fonction");
					for (int k = 0; k < fonctions.size(); k++) {
						if (fonctions.get(k).getAttribute("nom") != null) {
							value = formateValueUsingFunction(
									fonctions.get(k).getAttributeValue("nom"), 
									value);
						}
					}
				}
				
				// on regarde si un mapping existe entre une valeur du
				// fichier et une valeur de TK
				if (sourceElt.getChild("Modifier") != null) {
					if (sourceElt.getChild("Modifier")
							.getAttribute("nom").getValue()
							.equals("Thesaurus")) {
						// si c'est la cas, on extrait ces mappings
						Hashtable<String, String> mappingValues = 
							extractMappingValuesForThesaurs(
									sourceElt.getChild("Modifier"));
						
						if (value != null 
								&& mappingValues.containsKey(value)) {
							value = mappingValues.get(value);
						}
					} else if (sourceElt.getChild("Modifier")
							.getAttribute("nom").getValue()
							.equals("Fichier")) {
						File f = new File(sourceElt
								.getChild("Modifier")
								.getChild("Path").getValue() 
								+ value);
						if (f.exists()) {
							file = FileUtils.readFileToByteArray(f);
							relatedFiles.add(f);
						} else {
							log.error("hl7.file.path.empty: " 
									+ sourceElt
									.getChild("Modifier")
									.getChild("Path").getValue() 
									+ value);
							value = null;						
						}
						
					}
				}
				
				// on n'ajoute la valeur que si elle n'est pas Null
				if (value != null) {
					valeur.setValeur(value);
					valeur.setContenu(file);
					// insertion de la valeur dans la liste d'éléments
					List<ValeurExterne> vals = valeurExternes.get(blocCurrent);
					vals.add(valeur);
					valeurExternes.remove(blocCurrent);
					valeurExternes.put(blocCurrent, vals);
				} else {
					log.info("La valeur du bloc '" 
						+ blocs.get(i).getAttributeValue("nom")
						+ "' à l'emplacement '" 
						+ sourceElt.getChildText("Key")
						+ "' est NULL");
				}
			}
		}
		
		// on récupère tous les blocs libres
		List<Element> blocsLibres = racine.getChildren("BlocLibre");
		for (int i = 0; i < blocsLibres.size(); i++) {
			List<Element> mappings = blocsLibres.get(i).getChildren("Mapping");
			Element sourceBloc = blocsLibres.get(i).getChild("Source");
			Element keyBloc = sourceBloc.getChild("Key");
			// pour chaque Mapping
			for (int j = 0; j < mappings.size(); j++) {
				Element tkElt = mappings.get(j).getChild("Tk");
				Element entiteElt = tkElt.getChild("Entite");
				Integer entite = entiteElt.getAttribute("idChamp")
					.getIntValue();
				

				BlocExterne blocCurrent = null;
				if (entitesBlocs.containsKey(entite)) {
					blocCurrent = entitesBlocs.get(entite);
				} else {
					// création d'un nouveau bloc
					blocCurrent = new BlocExterne();
					blocCurrent.setDossierExterne(dossierExterne);
					blocCurrent.setEntiteId(entite);
					blocCurrent.setOrdre(ordreBloc);
					++ordreBloc;
					entitesBlocs.put(entite, blocCurrent);
					blocExternes.add(blocCurrent);
					valeurExternes.put(blocCurrent,
							new ArrayList<ValeurExterne>());
				}

				// création de la valeur externe
				ValeurExterne valeur = new ValeurExterne();
				valeur.setBlocExterne(blocCurrent);
				if (tkElt.getChild("ChampEntite") != null) {
					valeur.setChampEntiteId(tkElt.getChild("ChampEntite")
							.getAttribute("idChamp").getIntValue());
				} else {
					valeur.setChampAnnotationId(tkElt.getChild("Annotation")
							.getAttribute("idChamp").getIntValue());
				}

				Element sourceElt = mappings.get(j).getChild("Source");
				// extraction de la valeur dans le fichier
				String value = getValueFromBlocLibre(
						contenuLibre, 
						config, 
						blocsLibres.get(i).getAttributeValue("nom"), 
						keyBloc.getText(), 
						sourceElt.getChildText("Key"));
				
				
				byte[] file = null;

				// on regarde si des fonctions de formatage sont
				// demandées
				if (sourceElt.getChild("Fonctions") != null) {
					List<Element> fonctions = sourceElt.getChild("Fonctions")
						.getChildren("Fonction");
					for (int k = 0; k < fonctions.size(); k++) {
						if (fonctions.get(k).getAttribute("nom") != null) {
							value = formateValueUsingFunction(
									fonctions.get(k).getAttributeValue("nom"), 
									value);
						}
					}
				}
				
				// on regarde si un mapping existe entre une valeur du
				// fichier et une valeur de TK
				if (sourceElt.getChild("Modifier") != null && value != null) {
					if (sourceElt.getChild("Modifier")
								.getAttribute("nom").getValue()
								.equals("Thesaurus")) {
						// si c'est la cas, on extrait ces mappings
						Hashtable<String, String> mappingValues = 
							extractMappingValuesForThesaurs(
									sourceElt.getChild("Modifier"));
						
						if (value != null 
								&& mappingValues.containsKey(value)) {
							value = mappingValues.get(value);
						}
					} else if (sourceElt.getChild("Modifier")
							.getAttribute("nom").getValue()
							.equals("Fichier") ) {
						File f = new File(sourceElt
								.getChild("Modifier")
								.getChild("Path").getValue() 
								+ value);
						if (f.exists()) {
							file = FileUtils.readFileToByteArray(f);
							relatedFiles.add(f);
						} else {
							log.error("hl7.file.path.empty: " 
									+ sourceElt
									.getChild("Modifier")
									.getChild("Path").getValue() 
									+ value);
							value = null;						
						}
					}
				}
				
				// on n'ajoute la valeur que si elle n'est pas Null
				if (value != null) {
					valeur.setValeur(value);
					valeur.setContenu(file);
					// insertion de la valeur dans la liste d'éléments
					List<ValeurExterne> vals = valeurExternes
						.get(blocCurrent);
					vals.add(valeur);
					valeurExternes.remove(blocCurrent);
					valeurExternes.put(blocCurrent, vals);
				} else {
					log.info("La valeur du bloc libre '" 
						+ blocsLibres.get(i).getAttributeValue("nom")
						+ "' à l'emplacement '" 
						+ sourceElt.getChildText("Key")
						+ "' est NULL");
				}
			}
		}

		// enregistrement du dossier externe
		dossierExterneManager.createObjectManager(dossierExterne, emetteur, 
				blocExternes, valeurExternes, max);
		
		// supprimes les fichiers associés si tout s'est bien passé
		if (delFile) {
			for (File file : relatedFiles) {
				log.info("deletion du fichier: " + file.getName());
				file.delete();			
			}
		}

		return dossierExterne;
	}
	
	@Override
	public Date parseHl7Date(String dateStr) {
		// on transforme le string en date
		Date date = null;
		if (dateStr != null) {
			if (dateStr.equals("\"\"")) {
				return null;
			}
			SimpleDateFormat sdf = 
				new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				date = sdf.parse(dateStr);
			} catch (ParseException e) {
				sdf = 
					new SimpleDateFormat("yyyyMMddHHmm");
				try {
					date = sdf.parse(dateStr);
				} catch (ParseException e1) {
					sdf = 
						new SimpleDateFormat("yyyyMMdd");
					try {
						date = sdf.parse(dateStr);
					} catch (ParseException e2) {
						log.error("hl7 TS date parse exception");
						throw new RuntimeException(e2);
					}
				}
			}
		}
		return date;
	}

}
