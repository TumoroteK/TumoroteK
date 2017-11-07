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
package fr.aphp.tumorotek.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Fichier;


/**
 * Utils class
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public final class Utils {
	
	//private Log log = LogFactory.getLog(Utils.class);
	
	private Utils() {
	}
	
	/**
	 * Uilitaire formatant la date systeme courante.
	 * @return Date courante
	 */
	public static Date getCurrentSystemDate() {	
		return Calendar.getInstance().getTime();
	}
	
	/**
	 * Uilitaire formatant la date systeme courante.
	 * @return Calendar courante
	 */
	public static Calendar getCurrentSystemCalendar() {	
		return Calendar.getInstance();
	}
	
	public static String getCurrentSystemTime() {
		 DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		 
		 return  dateFormat.format(getCurrentSystemDate());
	}
	
	/**
	 * Méthode générant un code lettre ("AA", "BC"...) en fonction d'un nombre
	 * fourni en paramètre.
	 * @return Liste de caractères dont le dernier correspond au code à générer
	 * pour le nombre fourni.
	 */
	public static List<String> createListChars(
			int max, Integer prefix, List<String> out) {
		String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
				"V", "W", "X", "Y", "Z"};
		if (prefix == null) {
			int i = 0;
			prefix = 0;
			while (i < max && i < 26) {
				out.add(alphabet[i]);
				i++;
			}
		} else {
			int i = 0;
			while (i < max && i < 26) {
				out.add(alphabet[prefix] + alphabet[i]);
				i++;
			}
			prefix++;
		} 
		if (max < 27) {
			return out;
		} else {
			return createListChars(max - 26, prefix, out);
		}
	}
	
	/**
	 * Ecris le filesystem path pour une banque et/ou le champ annotation.
	 * @param bank Banque
	 * @param chp ChampAnnotation
	 * @param obj TKAnnotableObject
	 * @return path
	 */
	public static String writeAnnoFilePath(String basedir, 
					Banque bank, ChampAnnotation chp, Fichier file) {
		if (basedir == null || !new File(basedir).exists()) {
			throw new RuntimeException("error.filesystem.access");
		}
		String path = basedir + "pt_" + bank.getPlateforme().getPlateformeId()
		+ "/" + "coll_" + bank.getBanqueId();
		
		if (chp != null) {
			path = path + "/anno/chp_" + chp.getChampAnnotationId() + "/"; 
		}
//		
//		if (obj != null) {
//			path = path + "/" + obj.entiteNom() + "_";
//			if (obj.listableObjectId() != null) {
//				path = path + obj.listableObjectId();
//			} else {
//				path = path + 0;
//			}
//					
//		}
		if (file != null && file.getNom() != null) {
			path = path + file.getNom(); 
		}

		return path;
	}
	
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
	
	/**
	 * Arrondi d'un double avec n éléments après la virgule.
	 * @param a La valeur à convertir.
	 * @param n Le nombre de décimales à conserver.
	 * @return La valeur arrondi à n décimales.
	 */
	public static Float floor(Float a, int n) {
		if (a != null) {
			double p = Math.pow(10.0, n);
			return (float) (Math.floor((a * p) + 0.5) / p);
		} else {
			return null;
		}
	}
	
	/**
	 * Cette méthode formate un string en fct d'une expression régulière
	 * de la forme : >., <., ou [1,3]. 
	 * @param value String à formater.
	 * @param expReg Expression régulière.
	 * @return String formaté.
	 */
	public static String formateString(String value, String expReg) {
		String resultat = "";
		// on vérifie que le paramétrage est ok
		if (value != null && !value.equals("")) {
			// on vérifie que le paramétrage est ok
			if (expReg != null && !expReg.equals("")) {
				// on garde ce qui se trouve avant un certain caractère
				if (expReg.contains("<")) {
					String car = expReg.substring(1);
					if (value.contains(car)) {
						resultat = value.substring(0, value.indexOf(car));
					} else {
						resultat = "";
					}
				} else if (expReg.contains(">")) {
					// on garde ce qui se trouve après un certain caractère
					String car = expReg.substring(1);
					int size = car.length();
					if (value.contains(car)) {
						resultat = value.substring(
								value.indexOf(car) + size);
					} else {
						resultat = "";
					}
				} else if (expReg.contains("[")) {
					// on conserve entre 2 indices de caractères
					int deb = Integer.parseInt(expReg.substring(
							1, expReg.indexOf(",")));
					int fin = Integer.parseInt(expReg.substring(
							expReg.indexOf(",") + 1, 
							expReg.indexOf("]")));
					if (deb > 0 && fin > 0 && deb < fin) {
						--deb;
						if (value.length() >= deb) {
							if (value.length() >= fin) {
								resultat = value.substring(deb, fin);
							} else {
								resultat = value.substring(deb);
							}
						} else {
							resultat = "";
						}
					} else {
						resultat = "";
					}
				}
			} else {
				resultat = value;
			}
		}
		
		return resultat;
	}
	
	public static Hashtable<Integer, List<Integer>> 
		extractAssosPlateformesEmetteursRecepteurs(String value) {
		Hashtable<Integer, List<Integer>> associations = 
			new Hashtable<Integer, List<Integer>>();
		
		if (value != null && !value.equals("")) {
			value = value.replace(" ", "");
			String[] pfs = null;
			if (value.contains(":")) {
				pfs = value.split(";");
			} else {
				pfs = new String[1];
				pfs[0] = value;
			}
			
			for (int i = 0; i < pfs.length; i++) {
				if (pfs[i].contains(":")) {
					String pf = pfs[i].substring(0, pfs[i].indexOf(":"));
					String tmp = pfs[i].substring(pfs[i].indexOf(":") + 1);
					String[] ems = null;
					if (tmp.contains(",")) {
						ems = tmp.split(",");
					} else {
						ems = new String[1];
						ems[0] = tmp;
					}
					
					List<Integer> emsIds = new ArrayList<Integer>();
					for (int j = 0; j < ems.length; j++) {
						emsIds.add(Integer.valueOf(ems[j]));
					}
					
					if (emsIds.size() > 0) {
						associations.put(Integer.valueOf(pf), emsIds);
					}
				}
			}
		}
		
		return associations;
	}
	
	/**
	 * Formate un string en upper case.
	 * @param value
	 * @return
	 */
	public static String stringToUpperCase(String value) {
		if (value != null) {
			return value.toUpperCase();
		} else {
			return null;
		}
	}
	
	/**
	 * Formate un string en lower case.
	 * @param value
	 * @return
	 */
	public static String stringToLowerCase(String value) {
		if (value != null) {
			return value.toLowerCase();
		} else {
			return null;
		}
	}
	
	/**
	 * Remplace toutes les virgules d'un string par des points.
	 * @param value
	 * @return
	 */
	public static String replaceCommaByDot(String value) {
		if (value != null) {
			while (value.contains(",")) {
				value = value.replace(',', '.');
			}
			return value;
		} else {
			return null;
		}
	}
	
	/**
	 * @see http://stackoverflow.com/questions/1910236/how-can-i-split-an-arraylist-into-several-lists
	 * @param list
	 * @param targetSize
	 * @return
	 */
	public static <T extends Object> List<List<T>> split(List<T> list, int targetSize) {
	    List<List<T>> lists = new ArrayList<List<T>>();
	    for (int i = 0; i < list.size(); i += targetSize) {
	        lists.add(list.subList(i, Math.min(i + targetSize, list.size())));
	    }
	    return lists;
	}
	
	/**
	 * Obtient la ReadableProperty exploitable par introspection PropertyUtils 
	 * pour un objet à partir de l'objet ChampEntite.
	 * @param chpE
	 * @return readable property
	 */
	public static String getReadablePropertyFromChampEntite(ChampEntite chpE) {
		String readProp = null;
		if (chpE != null) {
			readProp = chpE.getNom().replaceFirst(".", 
					(chpE.getNom().charAt(0) + "").toLowerCase());
			if (readProp.endsWith("Id")) {
				readProp = readProp.substring(0, readProp.length() - 2);
			}
		}
		return readProp;
	}
	
	public static String getDatabaseURL() throws NamingException {
		// get a handle on the JNDI root context
		Context ctx = new InitialContext();

		// and access the environment variable for this web component
		return (String) ctx.lookup("java:comp/env/jdbc/url");
	}

	/**
	 * Accède via JNDI au path spécifié dans le ficher de conf du server.
	 * 
	 * @return database url path
	 * @throws NamingException 
	 */
	public static String getDriverClass() throws NamingException {

		// get a handle on the JNDI root context
		Context ctx = new InitialContext();

		// and access the environment variable for this web component
		return (String) ctx
				.lookup("java:comp/env/jdbc/driverClass");
	}

	
	public static Boolean isOracleDBMS() throws NamingException {
		return getDatabaseURL().contains("oracle");
	}
	
	/**
	 * 
	 * @return usernameDB
	 * @throws NamingException 
	 */
	public static String getUsernameDB() throws NamingException {
	
		// get a handle on the JNDI root context
		Context ctx = new InitialContext();

		// and access the environment variable for this web component
		return (String) ctx.lookup("java:comp/env/jdbc/user");
	}
	
	/**
	 * 
	 * @return passwordDB
	 * @throws NamingException 
	 */
	public static String getPasswordDB() throws NamingException {
		Context ctx = new InitialContext();

		return (String) ctx.lookup("java:comp/env/jdbc/password");
	}
}
