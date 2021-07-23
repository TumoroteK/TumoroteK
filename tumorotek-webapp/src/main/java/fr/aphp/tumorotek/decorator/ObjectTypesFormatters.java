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
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.text.NumberFormatter;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Vlayout;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.administration.annotations.AnnoItemRowRenderer;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.cession.CessionStatut;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampCalcule;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Protocole;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.utils.Duree;
import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.param.TumorotekProperties;

/**
 * Classe utilitaire regroupant les methodes statiques permettant le formatage
 * des types JAVA pour l'affichage.
 * Date: 26/03/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public final class ObjectTypesFormatters
{

   // largeur d'un character en pixels
   // public static int CHARTOPIX = 10;

   private static DecimalFormat dF = new DecimalFormat("###.###");

   private ObjectTypesFormatters(){}

   /**
    * Provide parameters to label string.
    * @param key
    * @param parameters
    * @return
    */
   public static String getLabel(final String key, final String[] parameters){
      String label = org.zkoss.util.resource.Labels.getLabel(key);
      for(int i = 0; i < parameters.length; i++){
         final String parameter = parameters[i];
         label = label.replaceAll("\\{" + (i + 1) + "\\}", parameter);
      }
      return label;
   }

   /**
    * Formatte le contenu booleen pour un affichage user friendly.
    * @param b
    * @return contenu booleen formaté.
    */
   public static String booleanLitteralFormatter(final Boolean b){
      if(b != null){
         if(b.booleanValue()){
            return Labels.getLabel("general.checkbox.true");
         }
         return Labels.getLabel("general.checkbox.false");
      }
      return "-";
   }

   /**
    * Formatte une valeur numérique. Enlève le trailing .0 si le numérique est
    * un entier.
    * @param o double
    * @return double formaté
    */
   public static String numericFormatter(final Object o){
      if(o != null && o instanceof Number){
         String s = o.toString();
         s = s.replaceAll("\\.0+$", "");
         return s;
      }
      return null;
   }

   public static String doubleLitteralFormatter(final Double d){
      return dF.format(d);
   }
   
   public static String doubleLitteralFormatter(final Double d, DecimalFormatSymbols dfs){
      DecimalFormat dEn = new DecimalFormat("###.###", dfs);
      return dEn.format(d);
   }
   
   /**
    * @since 2.3.0-gatsbi
    * @param number
    * @return
    * @throws ParseException
    */
	public static String formatAnyNumber(Number number) throws ParseException {
		if (number != null) {
			if (number instanceof Integer) {
				return ((Integer) number).toString();
			} else if (number instanceof Double) {
				return doubleLitteralFormatter((Double) number);
			}
		}
		return new NumberFormatter().valueToString(number);
	}

   /**
    * Arrondi d'un double avec n éléments après la virgule.
    * @param a La valeur à convertir.
    * @param n Le nombre de décimales à conserver.
    * @return La valeur arrondi à n décimales.
    */
   public static Float floor(final Float a, final int n){
      if(a != null){
         final double p = Math.pow(10.0, n);
         return (float) (Math.floor((a * p) + 0.5) / p);
      }
      return null;
   }

   /**
    * Formatte un collaborateur pour affichage sous la forme
    * BARTHELEMY M. .
    * @param Collaborateur
    * @return collaborateur sous la forme affichage
    */
   public static String collaborateurFormatter(final Collaborateur c){
      String res = c.getNom();
      if(c.getPrenom() != null){
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
   public static String renderItems(final List<Item> items, final Set<AnnotationDefaut> defauts){
      final StringBuilder strbld = new StringBuilder();
      final Iterator<Item> itor = items.iterator();
      Item item;
      String valeur;
      while(itor.hasNext()){
         item = itor.next();
         strbld.append("- ");
         strbld.append(item.getLabel());
         if(defauts != null && AnnoItemRowRenderer.isItemDefaut(item, defauts)){
            strbld.append("*");
         }
         valeur = item.getValeur();
         if(valeur != null && !valeur.equals(item.getLabel())){
            strbld.append(" [");
            strbld.append(valeur);
            strbld.append("]");
         }
         if(itor.hasNext()){ // ajoute pas ligne si dernier
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
   public static String dateRenderer2(final Object date){
      if(date != null){
         SimpleDateFormat df;

         // par defaut
         df = new SimpleDateFormat(Labels.getLabel("validation.date.format.simple"));

         if(date instanceof Calendar){
            if(((Calendar) date).get(Calendar.HOUR_OF_DAY) > 0 || ((Calendar) date).get(Calendar.MINUTE) > 0
               || ((Calendar) date).get(Calendar.SECOND) > 0){
               df = new SimpleDateFormat(Labels.getLabel("validation.date.format"));
            }
            return df.format(((Calendar) date).getTime());
         }
         return df.format(date);
      }
      return null;
   }

   /**
    * Renvoie la classe css pour l'affichage d'un collaborateur (en fct
    * de son archivage).
    * @param collab Collaborateur que l'on veut formater.
    * @return La classe css.
    */
   public static String sClassCollaborateur(final Collaborateur collab){
      if(collab != null){
         if(collab.getArchive() != null && collab.getArchive()){
            return "formArchiveValue";
         }
         return "formValue";
      }
      return "formValue";
   }

   /**
    * Renvoie la classe css pour l'affichage d'un service (en fct
    * de son archivage).
    * @param service Service que l'on veut formater.
    * @return La classe css.
    */
   public static String sClassService(final Service serv){
      if(serv != null){
         if(serv.getArchive()){
            return "formArchiveValue";
         }
         return "formValue";
      }
      return "formValue";
   }

   /**
    * Renvoie la classe css pour l'affichage d'un établissemnet (en fct
    * de son archivage).
    * @param etab Etablissement que l'on veut formater.
    * @return La classe css.
    */
   public static String sClassEtablissement(final Etablissement etab){
      if(etab != null){
         if(etab.getArchive()){
            return "formArchiveValue";
         }
         return "formValue";
      }
      return "formValue";
   }

   /**
    * Formate les messages d'erreurs renvoyés par le Validator pour un champ
    * spécifié pour les afficher dans l'interface.
    * @param errs
    * @param field
    * @return les messages d'erreurs formatés.
    */
   public static String handleErrors(final Errors errs, final String field){

      FieldError err;
      String errMessage = "";

      final Iterator<FieldError> fieldErrorIt = errs.getFieldErrors(field).iterator();
      String label = "";
      while(fieldErrorIt.hasNext()){
         err = fieldErrorIt.next();
         if(Labels.getLabel(err.getCode()) != null){
            label = Labels.getLabel(err.getCode());
         }else{
            label = err.getCode();
         }
         if(errMessage.equals("")){
            errMessage = label;
         }else{
            errMessage = errMessage + ";" + label;
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
   public static void drawCodesExpLabel(final List<CodeAssigne> codes, final Row row, final Listitem li,
      final boolean addCodeEchan, final Integer pos){

      if(codes != null && !codes.isEmpty()){

         //setCodeExportFirst(codes);

         final List<String> strs = ManagerLocator.getCodeAssigneManager().formatCodesAsStringsManager(codes);

         final Label c1Label = new Label(strs.get(0));
         // dessine le label avec un lien vers popup
         if(strs.size() > 1){
            final Hlayout labelAndLinkBox = new Hlayout();
            labelAndLinkBox.setSpacing("5px");
            final Label moreLabel = new Label("...");
            moreLabel.setClass("formLink");
            final Popup malPopUp = new Popup();
            if(row != null){
               malPopUp.setParent(row.getParent().getParent().getParent());
            }else{
               malPopUp.setParent(li.getParent().getParent().getParent());
            }

            Label lab;
            final Vbox popupVbox = new Vbox();
            String labStr;
            for(int i = 0; i < strs.size(); i++){
               labStr = strs.get(i);
               if(addCodeEchan){
                  labStr = labStr + " [" + codes.get(i).getEchantillon().getCode() + "]";
               }
               lab = new Label(labStr);
               lab.setSclass("formValue");
               popupVbox.appendChild(lab);

               // surligne le code exporté
               if(codes.get(i).getExport()){
                  ((Label) popupVbox.getFirstChild()).setStyle("font-style: italic; font-weight: bold");
               }
            }

            malPopUp.appendChild(popupVbox);
            moreLabel.setTooltip(malPopUp);
            labelAndLinkBox.appendChild(c1Label);
            labelAndLinkBox.appendChild(moreLabel);
            if(row != null){
               int idx = pos == null ? row.getChildren().size() : pos;
               row.getChildren().add(idx, labelAndLinkBox);
//               labelAndLinkBox.setParent(row);
            }else{
               final Listcell cell = new Listcell();
               labelAndLinkBox.setParent(cell);
               cell.setParent(li);
            }
         }else{
            if(row != null){
               int idx = pos == null ? row.getChildren().size() : pos;
               row.getChildren().add(idx, c1Label);
//               c1Label.setParent(row);
            }else{
               final Listcell cell = new Listcell();
               c1Label.setParent(cell);
               cell.setParent(li);
            }
         }
      }else{
         if(row != null){
            int idx = pos == null ? row.getChildren().size() : pos;
            row.getChildren().add(idx, new Label());
//            new Label().setParent(row);
         }else{
            final Listcell cell = new Listcell();
            cell.setParent(li);
         }
      }
   }

   public static void drawCodesExpLabel(final List<CodeAssigne> codes, final Row row, final Listitem li,
      final boolean addCodeEchan){
      drawCodesExpLabel(codes, row, li, addCodeEchan, null);
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
   public static Calendar getDateWithoutHoursAndMins(final Calendar cal){
      Calendar c = null;
      if(cal != null){
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

   public static String getLabelForChampEntite(final ChampEntite c){
      final StringBuffer iProperty = new StringBuffer();
      iProperty.append("Champ.");
      iProperty.append(c.getEntite().getNom());
      iProperty.append(".");

      String champOk = "";
      // si le nom du champ finit par "Id", on le retire
      if(c.getNom().endsWith("Id")){
         champOk = c.getNom().substring(0, c.getNom().length() - 2);
      }else{
         champOk = c.getNom();
      }
      iProperty.append(champOk);

      // on ajoute la valeur du champ
      return Labels.getLabel(iProperty.toString());
   }

   /**
    * Retourne le libellé internationalisé correspondant à un champ
    * @param champ
    * @return
    */
   public static String getLabelForChamp(final Champ champ){

      String label = "";

      if(champ.getChampAnnotation() != null){
         label = champ.getChampAnnotation().getNom();
      }else{

         String nomEntite = null;
         String nomChamp = null;

         String propertyKey = "Champ.";
         
         if(champ.getChampEntite() != null){
            nomEntite = champ.getChampEntite().getEntite().getNom();
            nomChamp = champ.getChampEntite().getNom().replace("Id", "");
            propertyKey += nomEntite + "." + nomChamp;
         }else if(champ.getChampDelegue() != null){
            EContexte contexte = champ.getChampDelegue().getContexte();
            nomEntite = champ.getChampDelegue().getEntite().getNom();
            nomChamp = champ.getChampDelegue().getNom().replace("Id", "");
            propertyKey += nomEntite + "." + contexte.getNom() + "." + StringUtils.capitalize(nomChamp);
         }

         label = Labels.getLabel(propertyKey);

      }

      return label;

   }

   /**
    * Formatte l'affichage de la temperature.
    * @param temp Float
    */
   public static String formatTemperature(final Float temp){

      if(temp != null){
         final StringBuilder sb = new StringBuilder();
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
   public static String getEncodedPassword(final String pwd){
      final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
      return encoder.encodePassword(pwd, null);
   }

   /**
    * Render les valeurs d'une annotation.
    * @param valeur
    * @return
    */
   public static String annotationValeurRenderer(final AnnotationValeur valeur){
      String result = "";

      final Object obj = ManagerLocator.getAnnotationValeurManager().getValueForAnnotationValeur(valeur);

      if(obj != null){
         if(String.class.isInstance(obj)){
            result = String.class.cast(obj);
         }else if(Date.class.isInstance(obj)){
            result = ObjectTypesFormatters.dateRenderer2(obj);
         }else if(Calendar.class.isInstance(obj)){
            result = ObjectTypesFormatters.dateRenderer2(obj);
         }else if(Boolean.class.isInstance(obj)){
            result = ObjectTypesFormatters.booleanLitteralFormatter((Boolean) obj);
         }else if(Item.class.isInstance(obj)){
            result = Item.class.cast(obj).getLabel();
         }else if(Fichier.class.isInstance(obj)){
            result = Fichier.class.cast(obj).getNom();
         }
      }
      return result;
   }

   /**
    * Retourne la durée de validité du MDP, défini dans le fichier
    * tumorotek.properties.
    */
   public static Integer getNbMoisMdp(){
      Integer nbMoisMdp = null;

      // init du nb de mois de validité du MDP
      // on récupère le bundle de paramétrage de l'application
      ResourceBundle res = null;
      if(ManagerLocator.getResourceBundleTumo().doesResourceBundleExists(TumorotekProperties.TUMO_PROPERTIES_FILENAME)){
         res = ManagerLocator.getResourceBundleTumo().getResourceBundle(TumorotekProperties.TUMO_PROPERTIES_FILENAME);
      }

      // on récupère la propriété définissant le nb de mois
      if(res != null && res.containsKey(TkParam.DUREE_VALIDITE_MDP.getKey())){
         final String nb = res.getString(TkParam.DUREE_VALIDITE_MDP.getKey());

         if(nb != null){
            try{
               nbMoisMdp = Integer.parseInt(nb);
               if(nbMoisMdp < 1){
                  nbMoisMdp = null;
               }
            }catch(final NumberFormatException e){
               nbMoisMdp = null;
            }
         }
      }

      return nbMoisMdp;
   }

   /**
    * Retourne un délai formatté en heure/min.
    * @param minuteValue Délai en minutes.
    * @return Délai en heure/min.
    */
   public static String getHeureMinuteLabel(final Integer minuteValue){
      final Integer heure = minuteValue / 60;
      Integer heureDelai = 0;
      Integer minDelai = 0;
      String resultat = "";
      if(heure > 0){
         heureDelai = heure.intValue();
         minDelai = minuteValue - (heureDelai * 60);
         final StringBuffer sb = new StringBuffer();
         sb.append(heureDelai.toString());
         sb.append("h ");
         sb.append(minDelai.toString());
         sb.append("min");
         resultat = sb.toString();
      }else{
         heureDelai = 0;
         minDelai = minuteValue;
         final StringBuffer sb = new StringBuffer();
         sb.append(heureDelai.toString());
         sb.append("h ");
         sb.append(minDelai.toString());
         sb.append("min");
         resultat = sb.toString();
      }
      return resultat;
   }

   /**
    * Retourne une durée (temps en secondes) formatté en Années Mois Jours Heures Minutes.
    * @param duree secondes.
    * @return Délai en Années Mois Jours Heures Minutes.
    */
   public static String formatDuree(final Duree duree){
      final Duree dureeDecompte = new Duree(duree.getTemps(Duree.MILLISECONDE), Duree.MILLISECONDE);
      final Long annees = dureeDecompte.getTemps(Duree.ANNEE);
      dureeDecompte.addTemps(-annees, Duree.ANNEE);
      final Long mois = dureeDecompte.getTemps(Duree.MOIS);
      dureeDecompte.addTemps(-mois, Duree.MOIS);
      final Long jours = dureeDecompte.getTemps(Duree.JOUR);
      dureeDecompte.addTemps(-jours, Duree.JOUR);
      final Long heures = dureeDecompte.getTemps(Duree.HEURE);
      dureeDecompte.addTemps(-heures, Duree.HEURE);
      final Long minutes = dureeDecompte.getTemps(Duree.MINUTE);

      final StringBuffer sb = new StringBuffer();
      // Flag pour savoir d'où commence le formattage et ne pas avoir de "trous"

      if(annees > 0){
         if(annees == 1){
            sb.append(annees + " " + Labels.getLabel("date.annee") + " ");
         }else{
            sb.append(annees + " " + Labels.getLabel("date.annees") + " ");
         }
      }

      if(mois > 0){
         if(mois == 1){
            sb.append(mois + " " + Labels.getLabel("date.mois") + " ");
         }else{
            sb.append(mois + " " + Labels.getLabel("date.months") + " ");
         }
      }

      if(jours > 0){
         if(jours == 1){
            sb.append(jours + " " + Labels.getLabel("date.jour") + " ");
         }else{
            sb.append(jours + " " + Labels.getLabel("date.jours") + " ");
         }
      }

      if(heures > 0){
         if(heures == 1){
            sb.append(heures + " " + Labels.getLabel("date.heure") + " ");
         }else{
            sb.append(heures + " " + Labels.getLabel("date.heures") + " ");
         }
      }

      if(minutes > 0){
         if(minutes == 1){
            sb.append(minutes + " " + Labels.getLabel("date.minute") + " ");
         }else{
            sb.append(minutes + " " + Labels.getLabel("date.minutes") + " ");
         }
      }

      return sb.toString();
   }

   /**
    * Formatte un champCalculé pour l'affichage (label)
    * @param champCalcule
    * @return label du champCalculé
    */
   public static String renderChampCalcule(final ChampCalcule champCalcule){
      final StringBuffer sb = new StringBuffer();
      if(null != champCalcule && null != champCalcule.getChamp1()){
         sb.append("[" + formatChampLabel(champCalcule.getChamp1(), true, "-") + "]");
         sb.append(" " + champCalcule.getOperateur() + " ");

         if(null != champCalcule.getChamp2()){
            sb.append("[" + formatChampLabel(champCalcule.getChamp2(), true, "-") + "]");
         }else if("date".equals(champCalcule.getDataType().getType()) || "datetime".equals(champCalcule.getDataType().getType())
            || "duree".equals(champCalcule.getDataType().getType())){
            final Duree duree = new Duree(new Long(champCalcule.getValeur()), Duree.SECONDE);
            sb.append(formatDuree(duree));
         }else{
            sb.append(champCalcule.getValeur());
         }
      }

      return sb.toString();
   }

   /**
    * Retourne un label formaté avec internationalisation
    * @param entite entité
    * @return label formaté
    */
   public static String formatEntiteLabel(final Entite entite){
      final String entiteNom = entite.getNom();
      final String entiteLabel = Labels.getLabel("Entite." + entiteNom);

      if(null == entiteLabel || "".equals(entiteLabel)){
         return entiteNom;
      }

      return entiteLabel;
   }

   /**
    * Retourne un label formaté avec internationalisation
    * @param champ champ à formatter
    * @param withEntityName précéder le label du champ avec son entité ?
    * @param separator
    * @return Label formatté
    */
   public static String formatChampLabel(final Champ champ, final Boolean withEntityName, final String separator){
      String labelChamp = formatChampLabel(champ);
      if(withEntityName){
         final String labelEntite = formatEntiteLabel(champ.entite());
         if(null != separator){
            labelChamp = labelEntite + separator + labelChamp;
         }else{
            labelChamp = labelEntite + labelChamp;
         }
      }
      return labelChamp;
   }

   /**
    * Retourne un label formaté avec internationalisation
    * @param champ champ à formatter
    * @return Label formatté
    */
   public static String formatChampLabel(final Champ champ){
      String labelChamp = null;
      String champNom = champ.nom();
      if(null != champ.getChampEntite() && champNom.endsWith("Id")){
         // si le nom du champ finit par "Id", on le retire
         champNom = champ.nom().substring(0, champNom.length() - 2);
      }
      final String entiteNom = champ.entite().getNom();
      
      if(champ.getChampEntite() != null) {
         labelChamp = Labels.getLabel("Champ." + entiteNom + "." + champNom);
      } else if(champ.getChampDelegue() != null){
         EContexte contexte = champ.getChampDelegue().getContexte();
         labelChamp = Labels.getLabel("Champ." + entiteNom + "." + contexte.getNom() + "." + StringUtils.capitalize(champNom));
      }

      if(null == labelChamp || "".equals(labelChamp)){
         labelChamp = champ.nom();
      }
      return labelChamp;
   }

   /**
    * Retourne l'adresse logique d'un objet.
    * @param object Objet dont on cherche l'adresse logique.
    * @return L'adresse logique de l'objet.
    */
   public static String getEmplacementAdrl(final TKAnnotableObject object){
      String adrl = null;

      if(object != null){
         if(object instanceof Echantillon){
            adrl = ManagerLocator.getEchantillonManager().getEmplacementAdrlManager((Echantillon) object);
         }else if(object instanceof ProdDerive){
            adrl = ManagerLocator.getProdDeriveManager().getEmplacementAdrlManager((ProdDerive) object);
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
   public static void drawProtocolesLabel(final Set<Protocole> protocoles, final Row row, final Listitem li){

      if(protocoles != null && !protocoles.isEmpty()){

         final Iterator<Protocole> protosIt = protocoles.iterator();

         final Label c1Label = new Label(protosIt.next().getNom());
         // dessine le label avec un lien vers popup
         if(protosIt.hasNext()){
            final Hlayout labelAndLinkBox = new Hlayout();
            labelAndLinkBox.setSpacing("5px");
            final Label moreLabel = new Label("...");
            moreLabel.setClass("formLink");
            final Popup malPopUp = new Popup();
            if(row != null){
               malPopUp.setParent(row.getParent().getParent().getParent());
            }else{
               malPopUp.setParent(li.getParent().getParent().getParent());
            }

            Label lab;
            final Vlayout popupVbox = new Vlayout();
            String labStr;
            while(protosIt.hasNext()){
               labStr = protosIt.next().getNom();
               lab = new Label(labStr);
               lab.setSclass("formValue");
               popupVbox.appendChild(lab);
            }

            malPopUp.appendChild(popupVbox);
            moreLabel.setTooltip(malPopUp);
            labelAndLinkBox.appendChild(c1Label);
            labelAndLinkBox.appendChild(moreLabel);
            if(row != null){
               labelAndLinkBox.setParent(row);
            }else{
               final Listcell cell = new Listcell();
               labelAndLinkBox.setParent(cell);
               cell.setParent(li);
            }
         }else{
            if(row != null){
               c1Label.setParent(row);
            }else{
               final Listcell cell = new Listcell();
               c1Label.setParent(cell);
               cell.setParent(li);
            }
         }
      }else{
         if(row != null){
            new Label().setParent(row);
         }else{
            final Listcell cell = new Listcell();
            cell.setParent(li);
         }
      }
   }

   /**
    * Formate un string passé en paramètre en une liste de string.
    * @param value
    * @return
    */
   public static List<String> formateStringToList(String value){
      final List<String> res = new ArrayList<>();
      if(value != null && value.contains(",")){
         value = value.replaceAll(" ", "");
         final String[] split = value.split(",");
         for(int i = 0; i < split.length; i++){
            res.add(split[i]);
         }
      }
      return res;
   }

   public static String formatObject(final Object o){
      if(o instanceof String){
         return (String) o;
      }else if(o instanceof Integer || o instanceof Long){
         return String.valueOf(o);
      }else if(o instanceof Float){
         return doubleLitteralFormatter(new Double((Float) o));
      }else if(o instanceof Double){
         return doubleLitteralFormatter((Double) o);
      }else if(o instanceof Boolean){
         return booleanLitteralFormatter((Boolean) o);
      }else if(o instanceof Date){
         return dateRenderer2(o);
      }else if(o instanceof Calendar){
         return dateRenderer2(o);
      }
      return null;
   }

   public static String ILNObjectStatut(final ObjetStatut statut){
      if(statut != null){
         return Labels.getLabel("Statut." + statut.getStatut().replaceAll("\\s", "_"));
      }
      return null;
   }

   public static String ILNObjectStatut(final CessionStatut statut){
      if(statut != null){
         return Labels.getLabel("Statut." + statut.getStatut().replaceAll("\\s", "_"));
      }
      return null;
   }
   
   /**
	 * Dessine dans un label le complément diagnostic propre à un p
	 * prélèvement dans une collection sérothèque. N'affiche que les 
	 * caractères avant un espace.
	 * Utilisation d'un tooltip pour afficher la totalité du texte
	 * S'adapate au grid (Row) ou a listbox (Listitem)
	 * @param Row row
	 * @param Listiem li
	 * @param Component Parent
	 * @param ecrit le [code echantillon]
	 * @since 2.2.3-rc1
	 */
	public static void drawComplementDiagnosticLabel(final String compDiag, final Row row, final Listitem li){

		if(!StringUtils.isEmpty(compDiag)){

			String[] strs = compDiag.trim().split(" ");
			final Label c1Label = new Label(strs[0]);
			// dessine le label avec un lien vers popup
			if(strs.length > 1) {
				final Hlayout labelAndLinkBox = new Hlayout();
				labelAndLinkBox.setSpacing("5px");
				final Label moreLabel = new Label("...");
				moreLabel.setClass("formLink");
				final Popup diagPopUp = new Popup();
				if(row != null){
					diagPopUp.setParent(row.getParent().getParent().getParent());
				}else{
					diagPopUp.setParent(li.getParent().getParent().getParent());
	            }

				// contenu de la popup = tout le texte
				Label lab = new Label(compDiag.trim());
				lab.setSclass("formValue");

				diagPopUp.appendChild(lab);
				moreLabel.setTooltip(diagPopUp);
				labelAndLinkBox.appendChild(c1Label);
				labelAndLinkBox.appendChild(moreLabel);

				if (row != null) {
					labelAndLinkBox.setParent(row);
				} else { 
					final Listcell cell = new Listcell();
					labelAndLinkBox.setParent(cell);
		            cell.setParent(li);
				}
			} else { // 1 seul mot à afficher
				if (row != null) {
					c1Label.setParent(row);
				} else {
					final Listcell cell = new Listcell();
					c1Label.setParent(cell);
		            cell.setParent(li);
				}
			}
		} else { // empty
			if (row != null) {
				new Label().setParent(row);
			} else {
				final Listcell cell = new Listcell();
	            cell.setParent(li);
			}
		}
	}
}
