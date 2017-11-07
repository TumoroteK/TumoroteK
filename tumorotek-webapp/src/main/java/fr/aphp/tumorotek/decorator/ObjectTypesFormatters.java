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
package fr.aphp.tumorotek.decorator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Vlayout;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.administration.annotations.AnnoItemRowRenderer;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.cession.CessionStatut;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Protocole;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Fichier;

/**
 * Classe utilitaire regroupant les methodes statiques permettant le formatage 
 * des types JAVA pour l'affichage.
 * Date: 26/03/2010
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public final class ObjectTypesFormatters {
	
	// largeur d'un character en pixels
	// public static int CHARTOPIX = 10;
	
	private static DecimalFormat dF = new DecimalFormat("###.###");
		
	private ObjectTypesFormatters() { }
	
	/**
	 * Provide parameters to label string.
	 * @param key
	 * @param parameters
	 * @return
	 */
	public static String getLabel(String key, String[] parameters) {
		String label = org.zkoss.util.resource.Labels.getLabel(key);
		for (int i = 0; i < parameters.length; i++) {
			String parameter = parameters[i];
			label = label.replaceAll("\\{" + (i + 1) + "\\}", parameter);
		}
		return label;
	}
	
	/**
	 * Formatte le contenu booleen pour un affichage user friendly.
	 * @param booleen
	 * @return contenu booleen formaté.
	 */
	public static String booleanLitteralFormatter(Boolean b) {
		if (b != null) {
			if (b.booleanValue()) {
				return Labels.getLabel("general.checkbox.true");
			} else {
				return Labels.getLabel("general.checkbox.false");
			}
		}
		return "-";
	}
	
	/**
	 * Formatte le double. Enlève le trailing .0 si le double est
	 * un entier.
	 * @param d double
	 * @return double formaté
	 */
	public static String doubleLitteralFormatter2(Double d) {
		if (d != null) {
			String s = Double.toString(d);
			s = s.replaceAll((String) "\\.0$", "");
			return s;
		} 
		return null;
	}
	
	public static String doubleLitteralFormatter(Double d) {
		return dF.format(d);
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
	 * Formatte un collaborateur pour affichage sous la forme
	 * BARTHELEMY M. .
	 * @param Collaborateur
	 * @return collaborateur sous la forme affichage
	 */
	public static String collaborateurFormatter(Collaborateur c) {
		String res = c.getNom();
		if (c.getPrenom() != null) {
			res = res + " " + c.getPrenom().substring(0, 1) + ".";
		}
		
		return res;
	}
	
	/**
	 * Formate l'affichage d'une liste d'item(s) pour le mode static.
	 * Ajoute une * si l'item est un item par défaut.
	 * @param its liste d'Item à afficher
	 * @param liste defauts accompagnant la liste Items
	 * @return liste formatée pour affichage
	 */
	public static String renderItems(List<Item> items, 
										Set<AnnotationDefaut> defauts) {
		StringBuilder strbld = new StringBuilder();
		Iterator<Item> itor = items.iterator();
		Item item;
		String valeur;
		while (itor.hasNext()) {
			item = itor.next();
			strbld.append("- ");
			strbld.append(item.getLabel());
			if (defauts != null
					&& AnnoItemRowRenderer.isItemDefaut(item, defauts)) {
				strbld.append("*");
			}
			valeur = item.getValeur();		
			if (valeur != null && !valeur.equals(item.getLabel())) {
				strbld.append(" [");
				strbld.append(valeur);
				strbld.append("]");
			}
			if (itor.hasNext()) { // ajoute pas ligne si dernier
				strbld.append("\n");
			}
		}
		return strbld.toString();
	}
	
	/**
	 * Formatte la date pour contourner le formatage automatique Zk.
	 * Utilise le format en fonction de la locale du navigateur.
	 * Applique le format 'simple' jj/mm/aaaa sans objet DATE ou avec
	 * hh:mm:ss si Calendar.
	 * N'affiche rien si 00:00:00
	 * @param Objet Date ou Calendar
	 * @return date formatée.
	 */
	public static String dateRenderer2(Object date) {
		if (date != null) {
			SimpleDateFormat df;
			
			// par defaut
			df = new SimpleDateFormat(Labels
						.getLabel("validation.date.format.simple"));
			
			if (date instanceof Calendar) {
				if (((Calendar) date).get(Calendar.HOUR_OF_DAY) > 0 
					|| ((Calendar) date).get(Calendar.MINUTE) > 0 
					|| ((Calendar) date).get(Calendar.SECOND) > 0) {
					df = new SimpleDateFormat(Labels
						.getLabel("validation.date.format"));
				}
				return df.format(((Calendar) date).getTime());
			}
			return df.format(date);
		} else {
			return null;
		}
	}
	
	/**
	 * Renvoie la classe css pour l'affichage d'un collaborateur (en fct
	 * de son archivage).
	 * @param collab Collaborateur que l'on veut formater.
	 * @return La classe css.
	 */
	public static String sClassCollaborateur(Collaborateur collab) {
		if (collab != null) {
			if (collab.getArchive() != null && collab.getArchive()) {
				return "formArchiveValue";
			} else {
				return "formValue";
			}
		} else {
			return "formValue";
		}
	}
	
	/**
	 * Renvoie la classe css pour l'affichage d'un service (en fct
	 * de son archivage).
	 * @param service Service que l'on veut formater.
	 * @return La classe css.
	 */
	public static String sClassService(Service serv) {
		if (serv != null) {
			if (serv.getArchive()) {
				return "formArchiveValue";
			} else {
				return "formValue";
			}
		} else {
			return "formValue";
		}
	}
	
	/**
	 * Renvoie la classe css pour l'affichage d'un établissemnet (en fct
	 * de son archivage).
	 * @param etab Etablissement que l'on veut formater.
	 * @return La classe css.
	 */
	public static String sClassEtablissement(Etablissement etab) {
		if (etab != null) {
			if (etab.getArchive()) {
				return "formArchiveValue";
			} else {
				return "formValue";
			}
		} else {
			return "formValue";
		}
	}
	
	/**
	 * Formate les messages d'erreurs renvoyés par le Validator pour un champ
	 * spécifié pour les afficher dans l'interface.
	 * @param errs
	 * @param field
	 * @return les messages d'erreurs formatés.
	 */
	public static String handleErrors(Errors errs, String field) {
		
		FieldError err;
		String errMessage = "";
		
		Iterator<FieldError> fieldErrorIt =
							errs.getFieldErrors(field).iterator();
		String label = "";
		while (fieldErrorIt.hasNext()) {
			err = fieldErrorIt.next();
			if (Labels.getLabel(err.getCode()) != null) {
				label = Labels.getLabel(err.getCode());
			} else {
				label = err.getCode();
			}
			if (errMessage.equals("")) {
				errMessage = label;
			} else {
				errMessage = errMessage  + ";" + label;
			}
		}
		return errMessage;
	}
	
	/**
	 * Dessine dans un label le code ou le libelle (en fonction 
	 * de l'association entre la banque et la table de codification) du 
	 * premier code de la liste. 
	 * Utilisation d'un tooltip pour afficher la totalité des codes 
	 * suivant cette même règle.
	 * S'adapate au grid (Row) ou a listbox (Listitem)
	 * @param Row row
	 * @param Listiem li
	 * @param Component Parent
	 * @param ecrit le [code echantillon]
	 */
	public static void drawCodesExpLabel(List<CodeAssigne> codes, 
													Row row, Listitem li,
													boolean addCodeEchan) {
		
		if (codes != null && !codes.isEmpty()) {

			//setCodeExportFirst(codes);
		
			List<String> strs = ManagerLocator
				.getCodeAssigneManager().formatCodesAsStringsManager(codes);
			
			Label c1Label = new Label(strs.get(0));
			// dessine le label avec un lien vers popup 
			if (strs.size() > 1) {
				Hlayout labelAndLinkBox = new Hlayout();
				labelAndLinkBox.setSpacing("5px");
				Label moreLabel = new Label("...");
				moreLabel.setClass("formLink");
				Popup malPopUp = new Popup();
				if (row != null) {
					malPopUp.setParent(row.getParent().getParent().getParent());
				} else {
					malPopUp.setParent(li.getParent().getParent().getParent());
				}
				
				Label lab;
				Vbox popupVbox = new Vbox();
				String labStr;
				for (int i = 0; i < strs.size(); i++) {
					labStr = strs.get(i);
					if (addCodeEchan) {
						labStr = labStr + " [" 
						+ codes.get(i).getEchantillon().getCode() 
						+ "]";
					}
					lab = new Label(labStr);
					lab.setSclass("formValue");
					popupVbox.appendChild(lab);
					
					// surligne le code exporté
					if (codes.get(i).getExport()) {
						((Label) popupVbox.getFirstChild())
							.setStyle("font-style: italic; font-weight: bold");
					}
				}
				
				malPopUp.appendChild(popupVbox);
				moreLabel.setTooltip(malPopUp);
				labelAndLinkBox.appendChild(c1Label);
				labelAndLinkBox.appendChild(moreLabel);
				if (row != null) {
					labelAndLinkBox.setParent(row);
				} else {
					Listcell cell = new Listcell();
					labelAndLinkBox.setParent(cell);
					cell.setParent(li);
				}
			} else {
				if (row != null) {
					c1Label.setParent(row);
				} else {
					Listcell cell = new Listcell();
					c1Label.setParent(cell);
					cell.setParent(li);
				}
			}
		} else {
			if (row != null) {
				new Label().setParent(row);
			} else {
				Listcell cell = new Listcell();
				cell.setParent(li);
			}
		}
	}
	
//	/**
//	 * Place le code exporté en premier dans la liste.
//	 * @param codes
//	 */
//	private static void setCodeExportFirst(List<CodeAssigne> codes) {
//		for (int i = 0; i < codes.size(); i++) {
//			if (codes.get(i).getExport()) {
//				CodeAssigne c = codes.get(i);
//				codes.remove(c);
//				codes.add(0, c);
//				break; // un seul code exporté
//			}
//		}
//	}
	
	/**
	 * Méthode utilitaire renvoyant la date d'un Calendar sans les heures et 
	 * minutes qui sont passées à 0.
	 * (Méthode utilisée dans la création d'un prélèvement lors du 
	 * transfert de la date de Prelevement sans les heures.)
	 * @param cal
	 * @return date sans heures ni minutes Calendar
	 */
	public static Calendar getDateWithoutHoursAndMins(Calendar cal) {
		Calendar c = null;
		if (cal != null) {
			// c = cal.clone();
			c = Calendar.getInstance();
			c.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			c.set(Calendar.MONTH, cal.get(Calendar.MONTH));
			c.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.HOUR, 0);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
		} 
		return c;	
	}
	
	public static String getLabelForChampEntite(ChampEntite c) {
		StringBuffer iProperty = new StringBuffer();
		iProperty.append("Champ.");
		iProperty.append(c.getEntite().getNom());
		iProperty.append(".");
		
		String champOk = "";
		// si le nom du champ finit par "Id", on le retire
		if (c.getNom().endsWith("Id")) {
			champOk = c.getNom().substring(
					0, c.getNom().length() - 2);
		} else {
			champOk = c.getNom();
		}
		iProperty.append(champOk);
		
		// on ajoute la valeur du champ
		return Labels.getLabel(iProperty.toString());
	}

	/**
	 * Formatte l'affichage de la temperature.
	 * @param temp Float
	 */
	public static String formatTemperature(Float temp) {
		
		if (temp != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(temp);
			sb.append("°C");
			return sb.toString();
		}
		return null;
		
	}
	
	/**
	 * Méthode qui encrypte le mot de passe en paramètre en
	 * utilisant l'algorithme MD5.
	 * @param pwd Mot de passe à encrypté.
	 * @return Mot de passe encrypté.
	 */
	public static String getEncodedPassword(String pwd) {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
	    return encoder.encodePassword(pwd, null);
	}
	
	/**
	 * Render les valeurs d'une annotation.
	 * @param valeur
	 * @return
	 */
	public static String annotationValeurRenderer(AnnotationValeur valeur) {
		String result = "";
		
		Object obj = ManagerLocator.getAnnotationValeurManager()
			.getValueForAnnotationValeur(valeur);
		
		if (obj != null) {
			if (obj.getClass().getSimpleName().equals("String")) {
				result = (String) obj;
			} else if (obj.getClass().getSimpleName().equals("Date")) {
				result = ObjectTypesFormatters.dateRenderer2((Date) obj);
			} else if (obj.getClass().getSimpleName().contains("Calendar")) {
				result = ObjectTypesFormatters.dateRenderer2((Calendar) obj);
			} else if (obj.getClass().getSimpleName().equals("Boolean")) {
				result = ObjectTypesFormatters
					.booleanLitteralFormatter((Boolean) obj);
			} else if (obj.getClass().getSimpleName().equals("Item")) {
				result = ((Item) obj).getLabel();
			} else if (obj.getClass().getSimpleName().equals("Fichier")) {
				result = ((Fichier) obj).getNom();
			}
		}
		return result;
	}
	
	/**
	 * Retourne la durée de validité du MDP, défini dans le fichier
	 * tumorotek.properties.
	 */
	public static Integer getNbMoisMdp() {
		Integer nbMoisMdp = null;
		
		// init du nb de mois de validité du MDP
		// on récupère le bundle de paramétrage de l'application
		ResourceBundle res = null;
    	if (ManagerLocator.getResourceBundleTumo()
    			.doesResourceBundleExists("tumorotek.properties")) {
    		res = ManagerLocator.getResourceBundleTumo()
    			.getResourceBundle("tumorotek.properties");
    	}
		
		// on récupère la propriété définissant le nb de mois
    	if (res != null && res.containsKey("NB_MOIS_VALIDITE_MDP")) {
    		String nb = res.getString("NB_MOIS_VALIDITE_MDP");
    		
    		if (nb != null) {
    			try {
    				nbMoisMdp = Integer.parseInt(nb);
    				if (nbMoisMdp < 1) {
    					nbMoisMdp = null;
    				}
    			} catch (NumberFormatException e) {
					nbMoisMdp = null;
				}
    		} else {
    			nbMoisMdp = null;
    		}
    	} else {
    		nbMoisMdp = null;
    	}
    	
    	return nbMoisMdp;
	}
	
	/**
	 * Retourne un délai formatté en heure/min.
	 * @param minuteValue Délai en minutes.
	 * @return Délai en heure/min.
	 */
	public static String getHeureMinuteLabel(Integer minuteValue) {
		Integer heure = minuteValue / 60;
		Integer heureDelai = 0;
		Integer minDelai = 0;
		String resultat = "";
		if (heure > 0) {
			heureDelai = heure.intValue();
			minDelai = minuteValue - (heureDelai * 60);
			StringBuffer sb = new StringBuffer();
			sb.append(heureDelai.toString());
			sb.append("h ");
			sb.append(minDelai.toString());
			sb.append("min");
			resultat = sb.toString();
		} else {
			heureDelai = 0;
			minDelai = minuteValue;
			StringBuffer sb = new StringBuffer();
			sb.append(heureDelai.toString());
			sb.append("h ");
			sb.append(minDelai.toString());
			sb.append("min");
			resultat = sb.toString();
		}
		return resultat;
	}
	
	/**
	 * Retourne l'adresse logique d'un objet.
	 * @param object Objet dont on cherche l'adresse logique.
	 * @return L'adresse logique de l'objet.
	 */
	public static String getEmplacementAdrl(TKAnnotableObject object) {
		String adrl = null;
		
		if (object != null) {
			if (object instanceof Echantillon) {
				adrl =  ManagerLocator.getEchantillonManager()
					.getEmplacementAdrlManager((Echantillon) object);
			} else if (object instanceof ProdDerive) {
				adrl = ManagerLocator.getProdDeriveManager()
					.getEmplacementAdrlManager((ProdDerive) object);
			}
		}
		
		return adrl;
	}

	/**
	 * Dessine dans un label les references des protocoles pour 
	 * un objet Prelevement dans un contexte Serotheque. 
	 * Utilisation d'un tooltip pour afficher la totalité des protocoles  
	 * suivant cette même règle.
	 * S'adapate au grid (Row) ou a listbox (Listitem)
	 * @param Row row
	 * @param Listiem li
	 */	
	public static void drawProtocolesLabel(Set<Protocole> protocoles, 
													Row row, Listitem li) {
		
		if (protocoles != null && !protocoles.isEmpty()) {
			
			Iterator<Protocole> protosIt = protocoles.iterator();
			
			Label c1Label = new Label(protosIt.next().getNom());
			// dessine le label avec un lien vers popup 
			if (protosIt.hasNext()) {
				Hlayout labelAndLinkBox = new Hlayout();
				labelAndLinkBox.setSpacing("5px");
				Label moreLabel = new Label("...");
				moreLabel.setClass("formLink");
				Popup malPopUp = new Popup();
				if (row != null) {
					malPopUp.setParent(row.getParent().getParent().getParent());
				} else {
					malPopUp.setParent(li.getParent().getParent().getParent());
				}
				
				Label lab;
				Vlayout popupVbox = new Vlayout();
				String labStr;
				while (protosIt.hasNext()) {
					labStr = protosIt.next().getNom();
					lab = new Label(labStr);
					lab.setSclass("formValue");
					popupVbox.appendChild(lab);
				}
							
				malPopUp.appendChild(popupVbox);
				moreLabel.setTooltip(malPopUp);
				labelAndLinkBox.appendChild(c1Label);
				labelAndLinkBox.appendChild(moreLabel);
				if (row != null) {
					labelAndLinkBox.setParent(row);
				} else {
					Listcell cell = new Listcell();
					labelAndLinkBox.setParent(cell);
					cell.setParent(li);
				}
			} else {
				if (row != null) {
					c1Label.setParent(row);
				} else {
					Listcell cell = new Listcell();
					c1Label.setParent(cell);
					cell.setParent(li);
				}
			}
		} else {
			if (row != null) {
				new Label().setParent(row);
			} else {
				Listcell cell = new Listcell();
				cell.setParent(li);
			}
		}
	}
	
	/**
	 * Formate un string passé en paramètre en une liste de string.
	 * @param value
	 * @return
	 */
	public static List<String> formateStringToList(String value) {
		List<String> res = new ArrayList<String>();
		if (value != null && value.contains(",")) {
			value = value.replaceAll(" ", "");
			String[] split = value.split(",");
			for (int i = 0; i < split.length; i++) {
				res.add(split[i]);
			}
		}
		return res;
	}
	
	public static String formatObject(Object o) {		
		if (o instanceof String) {
			return (String) o;
		} else if (o instanceof Integer) {
			return String.valueOf((Integer) o);
		} else if (o instanceof Float) {
			return doubleLitteralFormatter(new Double((Float) o));
		} else if (o instanceof Boolean) {
			return booleanLitteralFormatter((Boolean) o); 
		} else if (o instanceof Date) {
			return dateRenderer2(o);
		} else if (o instanceof Calendar) {
			return dateRenderer2(o);
		}
		return null;
	}
	
	public static String ILNObjectStatut(ObjetStatut statut) {
		if (statut != null) {
			return Labels.getLabel("Statut." 
						+ statut.getStatut().replaceAll("\\s","_"));
		}
		return null;
	}
	
	public static String ILNObjectStatut(CessionStatut statut) {
		if (statut != null) {
			return Labels.getLabel("Statut." 
						+ statut.getStatut().replaceAll("\\s","_"));
		}
		return null;
	}
}
