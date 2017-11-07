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
package fr.aphp.tumorotek.manager.interfacage;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Element;

import fr.aphp.tumorotek.manager.impl.interfacage.ConfigurationParsing;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;

/**
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.1
 */
public interface InterfacageParsingUtils {
	
	/**
	 * Extrait la conf du parsing du fichier XML et crée une instance
	 * de l'objet ConfigurationParsing.
	 * @param xmlFile Fixher XML contenant la conf.
	 * @return Un ConfigurationParsing.
	 */
	ConfigurationParsing initConfigurationParsing(Element parent);
	
	/**
	 * Parse le fichier à injecter et renvoie chaque catégorie de
	 * celui-ci dans une HashTable.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	Hashtable<String, List<String>> parseFileToInjectInTk(
			ConfigurationParsing conf, String file) throws IOException;
	
	/**
	 * Parse le fichier à injecter et renvoie une liste contenant tous
	 * les blocs libres.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	List<String> parseFileToExtractBlocsLibres(
			ConfigurationParsing conf, String file) throws IOException;
	
	/**
	 * Renvoie l'emetteur pour le fichier reçu à injecter dans TK.
	 * @param fileXml Ficher XML de configuration.
	 * @param file Contenu du fichier à injecter dans TK.
	 * @param boiteFtp Boite ftp ayant reçu le message.
	 * @return Emetteur du message, retorune null s'il ne
	 * trouve pas l'emetteur en BDD.
	 * @throws IOException 
	 */
	Emetteur extractEmetteurFromFileToInjectInTk(
			String fileXml, String file, String boiteFtp) throws IOException;
	
	/**
	 * Renvoie le fichier XML à utiliser pour le fichier reçu 
	 * à injecter dans TK.
	 * @param fileXml Ficher XML de configuration.
	 * @param file Contenu du fichier à injecter dans TK.
	 * @param boiteFtp Boite ftp ayant reçu le message.
	// * @param emetteur Emetteur pour lequel on recherche le XML.
	 * @return Fichier XML à utiliser pour injecter le message
	 * dans TK.
	 * @throws IOException 
	 */
	String extractXMLFIleFromFileToInjectInTk(
			String fileXml, String file, String boiteFtp) throws IOException;
	
	/**
	 * Extrait les mappings entre les valeurs de thésaurus dans le fichier
	 * et celles dans TK. Ces mappings seront placés dans une Hahstable.
	 * @param element Element parent du mapping.
	 * @return Hahstable.
	 */
	Hashtable<String, String> extractMappingValuesForThesaurs(Element element);
	
	/**
	 * Initialise un nouveau dossier : set du numéro, de la date et de
	 * l'opération.
	 * @param config
	 * @param contenu
	 * @param racine
	 * @return
	 */
	DossierExterne initNewDossierExterne(ConfigurationParsing config,
			Hashtable<String, List<String>> contenu,
			Element racine);
	
	/**
	 * Retourne la valeur recherchée dans une liste de valeurs sous
	 * la forme d'une hashtable et de son emplacement.
	 * @param contenu
	 * @param conf
	 * @param emplacement
	 * @return
	 */
	String getValueFromEmplacement(List<String> values,
			ConfigurationParsing conf, 
			String emplacement);
	
	/**
	 * Retourne la valeur recherchée en fct du contenu du fichier sous
	 * la forme d'une hashtable et de son emplacement.
	 * @param contenu
	 * @param conf
	 * @param emplacement
	 * @return
	 */
	String getValueFromBlocAndEmplacement(
			Hashtable<String, List<String>> contenu,
			ConfigurationParsing conf, 
			String bloc,
			String emplacement);
	
	/**
	 * Retourne la valeur recherchée pour un bloc libre.
	 * @param contenu
	 * @param conf
	 * @param emplacement
	 * @return
	 */
	String getValueFromBlocLibre(List<String> blocsLibres,
			ConfigurationParsing conf, 
			String bloc,
			String keyBloc,
			String emplacement);
	
	/**
	 * Formate une valeur extraite en utilisant la fonction dont
	 * le nom est passé en paramètres.
	 * @param fonction
	 * @param value
	 * @return
	 */
	String formateValueUsingFunction(String fonction, String value);
	
	/**
	 * Parse le fichier XML de conf et extrait les valeurs
	 * du fichier à injecter dans TK.
	 * @param file
	 * @param emetteur
	 * @param true si delete file associée au message
	 * @param int max pour First In First Out
	 * @return
	 * @throws Exception
	 * @version 2.1
	 */
	DossierExterne parseInterfacageXmlFile(String xmlFile, String message,
										Emetteur emetteur, 
										boolean delFile, int max) throws Exception;
	
	/**
	 * Parse un TS HL7 en une date JAVA.
	 * @param dateStr TS
	 * @return date.
	 */
	public Date parseHl7Date(String dateStr);
}
