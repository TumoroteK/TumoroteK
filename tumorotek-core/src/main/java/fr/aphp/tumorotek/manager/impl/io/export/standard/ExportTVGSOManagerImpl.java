package fr.aphp.tumorotek.manager.impl.io.export.standard;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.manager.io.export.standard.TvgsoComplementExport;
import fr.aphp.tumorotek.manager.io.export.standard.TvgsoEchantillonExport;
import fr.aphp.tumorotek.manager.io.export.standard.TvgsoMaladieExport;
import fr.aphp.tumorotek.manager.io.export.standard.TvgsoPatientExport;
import fr.aphp.tumorotek.manager.io.export.standard.TvgsoPrelevementExport;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

public class ExportTVGSOManagerImpl extends ExportCatalogueManagerImpl
{

   private final Log log = LogFactory.getLog(ExportTVGSOManagerImpl.class);

   private TvgsoPatientExport tvgsoPatientExport;
   private TvgsoMaladieExport tvgsoMaladieExport;
   private TvgsoPrelevementExport tvgsoPrelevementExport;
   private TvgsoEchantillonExport tvgsoEchantillonExport;
   private TvgsoComplementExport tvgsoComplementExport;

   public void setTvgsoPatientExport(final TvgsoPatientExport tExport){
      this.tvgsoPatientExport = tExport;
   }

   public void setTvgsoMaladieExport(final TvgsoMaladieExport iExport){
      this.tvgsoMaladieExport = iExport;
   }

   public void setTvgsoPrelevementExport(final TvgsoPrelevementExport tExport){
      this.tvgsoPrelevementExport = tExport;
   }

   public void setTvgsoEchantillonExport(final TvgsoEchantillonExport iExport){
      this.tvgsoEchantillonExport = iExport;
   }

   public void setTvgsoComplementExport(final TvgsoComplementExport iExport){
      this.tvgsoComplementExport = iExport;
   }

   @Override
   public Map<String, String> objetExport(final Echantillon echan, final Connection con){

      setDateFormatterINCA(new SimpleDateFormat("dd/MM/yyyy"));

      setEchantillon(echan);

      final HashMap<String, String> map = new LinkedHashMap<>();

      initExportObjectsManager();

      String flag = "";

      try{
         /***********************************************************/
         /************* INFORMATIONS PATIENT ************************/
         /***********************************************************/
         /*Item 2 FINESS OBLIGATOIRE */
         flag = "FINESS";
         map.put("FINESS", tvgsoPatientExport.getFiness(con, getPrelevement(), getPatient()));

         /*Item 2 Code patient OBLIGATOIRE */
         flag = "Identifiant patient";
         map.put("Identifiant patient", tvgsoPatientExport.getPatientCode(getPatient()));

         /*Item 3 Date de naissance du patient OBLIGATOIRE */
         flag = "Date naissance";
         map.put("Date naissance", tvgsoPatientExport.getDateNaissance(getPatient(), getDateFormatterINCA()));

         /*Item 4 Sexe du patient OBLIGATOIRE */
         flag = "Sexe";
         map.put("Sexe", tvgsoPatientExport.getSexe(getPatient()));

         /*Item 5 Etat du patient */
         flag = "Etat du patient";
         map.put("Etat du patient", tvgsoPatientExport.getPatientEtat(con, getPatient(), getBanque()));

         /*Item 6 Date de l'etat du patient */
         flag = "Date de l'Etat";
         map.put("Date de l'Etat", tvgsoPatientExport.getDateEtat(con, getPatient(), getDateFormatterINCA(), getBanque()));

         /*Item 76 Cause de deces du patient */
         // flag = "Cause deces"; 
         //map.put("Cause deces", tvgsoPatientExport
         //		.getCauseDeces(con, getPatient(), getBanque()));

         /***********************************************************/
         /************* INFORMATIONS MALADIE ************************/
         /***********************************************************/
         /*Item 7 Diagnostic principal de la tumeur initiale OBLIGATOIRE*/
         flag = "Diagnostic principal";
         map.put("Diagnostic principal", tvgsoMaladieExport.getDiagnosticPrincipal(con, getPrelevement()));

         /*Item 8 Date du diagnostic */
         flag = "Date du diagnostic";
         map.put("Date du diagnostic", tvgsoMaladieExport.getDateDiag(con, getPrelevement(), true, getDateFormatterINCA()));

         /*Item 9 Version cTNM */
         flag = "Version TNM";
         map.put("Version TNM", tvgsoMaladieExport.getVersionCTNM(con, getPrelevement()));

         /*Item 10 Taille de la tumeur */
         flag = "T du cTNM";
         map.put("T du cTNM", tvgsoMaladieExport.getTailleTumeur(con, getPrelevement()));

         /*Item 11 Envahissement ganglionnaire */
         flag = "N du cTNM";
         map.put("N du cTNM", tvgsoMaladieExport.getEnvahGangR(con, getPrelevement()));

         /*Item 12 Extension metastatique */
         flag = "M du cTNM";
         map.put("M du cTNM", tvgsoMaladieExport.getExtMetastatique(con, getPrelevement()));

         /********************************************************/
         /************ INFORMATIONS PRELEVELEMENT ****************/
         /********************************************************/

         /*Item 13 Centre se stockage OBLIGATOIRE*/
         flag = "Centre stockage";
         map.put("Centre stockage", tvgsoPrelevementExport.getCentreStockage(con, getEchantillon()));

         /*Item 14 Responsable tumorotheque OBLIGATOIRE*/
         flag = "Contact";
         map.put("Contact", tvgsoComplementExport.getContactShort(getBanque()));

         /*colonne 15 Code prelevement OBLIGATOIRE item14 Inca*/
         flag = "Code prelevement";
         map.put("Code prelevement", tvgsoPrelevementExport.getCodePrelevement(getPrelevement()));

         /*colonne 16 numero sejour VIDE*/
         flag = "Numero sejour";
         map.put("Numero sejour", "");

         /*colonne 17 Date prelevement item15 INCa*/
         flag = "Date prelevement";
         map.put("Date prelevement", tvgsoPrelevementExport.getDatePrelevement(getPrelevement(), getDateFormatterINCA()));

         /*colonne 18 Mode de prelevement OBLIGATOIRE item16 INCa*/
         flag = "Mode prelevement";
         map.put("Mode prelevement", tvgsoPrelevementExport.getModePrelevement(getPrelevement()));

         /*colonne 19 Classification utilisee OBLIGATOIRE item17 INCa*/
         flag = "Classification";
         map.put("Classification", tvgsoPrelevementExport.getClassif(getBanque()));

         /* colonne 20 Code organe Item18 et item20 INCa*/
         flag = "Code organe";
         map.put("Code organe", tvgsoPrelevementExport.getCodeOrgane(con, getEchantillon()));

         /* colonne 21 Type lesionnel Item19 et item21 INCa*/
         flag = "Type lesionnel";
         map.put("Type lesionnel", tvgsoPrelevementExport.getTypeLesionnel(con, getEchantillon()));

         /*colonne22 Type d'evenement OBLIGATOIRE item22 INCa*/
         flag = "TYPE_EVENT";
         map.put("TYPE_EVENT", tvgsoPrelevementExport.getTypeEvent(con, getPrelevement()));

         /*colonne 23 code organe tumeur primitive item16B VIDE */
         flag = "code organe tumeur primitive";
         map.put("code organe tumeur primitive", "");

         /*colonne 24 Version du pTNM item23*/
         flag = "Version pTNM";
         map.put("Version pTNM", tvgsoPrelevementExport.getVersionPTNM(con, getPrelevement()));

         /*colonne 25 pT item 24 INCa*/
         flag = "T du pTNM";
         map.put("T du pTNM", tvgsoPrelevementExport.getTailleTumeurPT(con, getPrelevement()));

         /*colonne 26 Presence/absence metastases */
         /* ganglionnaires pN item25 INCa*/
         flag = "N du pTNM";
         map.put("N du pTNM", tvgsoPrelevementExport.getEnvGangPN(con, getPrelevement()));

         /*Item26 Presence/absence metastases e distance pM*/
         // flag = "pM"; 
         // map.put("pM", tvgsoPrelevementExport
         //				.getExtMetastaticPM(con, getPrelevement()));

         /********************************************************/
         /************ INFORMATIONS ECHANTILLON ******************/
         /********************************************************/
         boolean isTumoral = false;
         /*colonne 27 echantillon tumoral ou non tumoral */
         /* OBLIGATOIRE Item27 et Item39*/
         String item27 = "";
         String item39 = "";
         flag = "Echantillon tumoral/non tumoral";
         if(tvgsoEchantillonExport.getIsTumoral(getEchantillon()).equals("O")){
            item27 = "O";
            item39 = "N";
            isTumoral = true;
         }else{
            item27 = "N";
            item39 = "O";
            isTumoral = false;
         }

         /*colonne 28 Item28 (et Item40) Mode de conservation */
         String item28 = "", item40 = "";

         if(isTumoral){
            flag = "Mode conservation tumoral";
            item28 = tvgsoEchantillonExport.getModeConservation(getEchantillon());
            item40 = "";
         }else{
            flag = "Mode conservation non tumoral";
            item40 = tvgsoEchantillonExport.getModeConservation(getEchantillon());
            item28 = "";
         }

         /*colonne 29 et 41 type echantillon*/
         String item29 = "", item41 = "";
         if(isTumoral){
            flag = "Type echantillon tumoral";
            item29 = getEchantillon().getEchantillonType().getType();
         }else{
            flag = "Type echantillon non tumoral";
            item41 = getEchantillon().getEchantillonType().getType();
         }

         /* colonne 30 Item30 (et item42) Mode de preparation*/
         String item30 = "", item42 = "";
         if(isTumoral){
            flag = "Mode preparation tumoral";
            item30 = tvgsoEchantillonExport.getModePreparation(con, getEchantillon());
         }else{
            flag = "Mode preparation non tumoral";
            item42 = tvgsoEchantillonExport.getModePreparation(con, getEchantillon());
         }

         /*colonne 31 Item31 (et item43) Delai de congelation*/
         String item31 = "", item43 = "";
         if(isTumoral){
            flag = "Delai congelation tumoral";
            item31 = tvgsoEchantillonExport.getDelaiCongelation(getEchantillon());
         }else{
            flag = "Delai congelation non tumoral";
            item43 = tvgsoEchantillonExport.getDelaiCongelation(getEchantillon());
         }

         /*colonne 32 Item32 et (item44) Controles sur tissu*/
         String item32 = "", item44 = "";
         if(isTumoral){
            flag = "Controles sur tissu tumoral";
            item32 = tvgsoEchantillonExport.getControles(con, getEchantillon());
         }else{
            flag = "Controles sur tissu non tumoral";
            item44 = tvgsoEchantillonExport.getControles(con, getEchantillon());
         }

         /* colonnes 33 et 34 Item33, item34 (et item45, item46) */
         /* Quantite de l'echantillon et unite*/
         String item33 = "", item45 = "", item34 = "", item46 = "";
         if(getEchantillon().getQuantite() != null && getEchantillon().getQuantiteUnite() != null){
            if(isTumoral){
               flag = "Quantite echantillon tumoral";
               item33 = getEchantillon().getQuantite().toString();
               flag = "Quantite unite echantillon tumoral";
               item34 = getEchantillon().getQuantiteUnite().getUnite();
            }else{
               flag = "Quantite echantillon non tumoral";
               item45 = getEchantillon().getQuantite().toString();
               flag = "Quantite unite echantillon non tumoral";
               item46 = getEchantillon().getQuantiteUnite().getUnite();
            }
         }

         /*colonne 35 Item35 Pourcentage cellules tumorales*/
         String item35 = "";
         if(isTumoral){
            flag = "Pourcentage cellules tumorales";
            item35 = tvgsoEchantillonExport.getPourcentageCellulesTumorales(con, echan);
         }

         /* colonnes 36, 37, 38: Item36, item37, item38*/
         /* (et item47, item48, item49) */
         /* respectivement ADN, ARN, proteine derives*/
         String item36 = "N", item37 = "N", item38 = "N", item47 = "N", item48 = "N", item49 = "N";
         if(isTumoral){
            flag = "ADN derive tumoral";
            item36 = tvgsoEchantillonExport.getProdTypeAssocie(con, getEchantillon(), "([Aa][Dd][nN])|([Dd][nN][Aa])");
            flag = "ARN derive tumoral";
            item37 = tvgsoEchantillonExport.getProdTypeAssocie(con, getEchantillon(), "([Aa][Rr][nN])|([Rr][nN][Aa])");
            flag = "Proteines derives tumoral";
            item38 = tvgsoEchantillonExport.getProdTypeAssocie(con, getEchantillon(), ".*[Pp][Rr][Oo][Tt][Ee][Ii][Nn].*");
         }else{
            flag = "ADN derive non tumoral";
            item47 = tvgsoEchantillonExport.getProdTypeAssocie(con, getEchantillon(), "([Aa][Dd][nN])|([Dd][nN][Aa])");
            flag = "ARN derive non tumoral";
            item48 = tvgsoEchantillonExport.getProdTypeAssocie(con, getEchantillon(), "([Aa][Rr][nN])|([Rr][nN][Aa])");
            flag = "Proteines derives non tumoral";
            item49 = tvgsoEchantillonExport.getProdTypeAssocie(con, getEchantillon(), ".*[Pp][Rr][Oo][Tt][Ee][Ii][Nn].*");
         }

         map.put("Echantillon tumoral", item27);
         map.put("Mode conservation tumoral", item28);
         map.put("Type echantillon tumoral", item29);
         map.put("Mode preparation tumoral", item30);
         map.put("Delai congelation tumoral", item31);
         map.put("Controles sur tissu tumoral", item32);
         map.put("Quantite echantillon tumoral", item33);
         map.put("Quantite unite echantillon tumoral", item34);
         map.put("Pourcentage cellules tumorales", item35);
         map.put("ADN derive tumoral", item36);
         map.put("ARN derive tumoral", item37);
         map.put("Proteines derives tumoral", item38);

         map.put("Echantillon non tumoral", item39);
         map.put("Identifiant echantillon", "");
         map.put("Mode conservation non tumoral", item40);
         map.put("Type echantillon non tumoral", item41);
         map.put("Mode preparation non tumoral", item42);
         map.put("Delai congelation non tumoral", item43);
         map.put("Controles sur tissu non tumoral", item44);
         map.put("Quantite echantillon non tumoral", item45);
         map.put("Quantite unite echantillon non tumoral", item46);
         map.put("ADN derive non tumoral", item47);
         map.put("ARN derive non tumoral", item48);
         map.put("Proteines derives non tumoral", item49);

         /***************************************************************/
         /****** INFORMATIONS RESSOURCES BIOLOGIQUES ASSOCIEES **********/
         /***************************************************************/

         //colonnes 51, 52, 53, 54 Item50, Item51, Item52, 
         //Item53 respectivement serum, plasma, liquides, ADN*/		
         flag = "Serum";
         map.put("Serum", tvgsoEchantillonExport.getRessourceBiolAssociee(getEchantillon(), "%SERUM%"));
         flag = "Plasma";
         map.put("Plasma", tvgsoEchantillonExport.getRessourceBiolAssociee(getEchantillon(), "%PLASMA%"));
         flag = "Liquides";
         map.put("Liquides", tvgsoEchantillonExport.getRessourceBiolAssociee(getEchantillon(), "%LIQUIDES%"));
         //ADN Constitutionnel
         flag = "ADN";
         map.put("ADN", tvgsoEchantillonExport.getADNconstitutionnel(con, getEchantillon()));

         /***********************************************************/
         /******** RENSEIGNEMENT COMPLEMENTAIRES ********************/
         /***********************************************************/

         /*Item54 Compte rendu anapath interrogeable*/
         flag = "CR anapath interrogeable";
         map.put("CR anapath interrogeable", tvgsoComplementExport.getCRAnapathInterro(con, getPrelevement()));

         /*Item55 Donnees cliniques disponibles dans une base*/
         flag = "Donnees cliniques disponibles";
         map.put("Donnees cliniques disponibles", tvgsoComplementExport.getDonneesClinBase(con, getPrelevement()));

         /*Item56, item57 Inclusion dans un protocole therapeutique */
         /* et nom du protocole*/
         flag = "Inclusion protocole therapeutique";
         map.put("Inclusion protocole therapeutique", tvgsoComplementExport.getInclusionTherap(con, getPrelevement()));
         flag = "Nom protocole";
         map.put("Nom protocole", tvgsoComplementExport.getNomProtocoleTherap(con, getPrelevement()));

         /*Item58 item59 Caryotype et anomalies eventuelles*/
         flag = "Caryotype";
         map.put("Caryotype", tvgsoComplementExport.getCaryotype(con, getPatient(), getBanque()));
         flag = "Anomalie eventuelle";
         map.put("Anomalie eventuelle", tvgsoComplementExport.getAnomalieCaryo(con, getPatient(), getBanque()));

         /*Item60 item61 Anomalie genomique et description*/
         flag = "Anomalie genomique";
         map.put("Anomalie genomique", tvgsoComplementExport.getAnomalieGenomique(con, getPatient(), getBanque()));
         flag = "Description anomalie genomique";
         map.put("Description anomalie genomique",
            tvgsoComplementExport.getAnomalieGenomiqueDescr(con, getPatient(), getBanque()));

         /*Item62 Controle qualite*/
         flag = "Controle qualite";
         map.put("Controle qualite", tvgsoComplementExport.getControleQualite(getEchantillon()));

         /*Item63 item64 Inclusion dans un protocole de recherche 
         /* et nom du programme*/
         flag = "Inclusion programme recherche";
         map.put("Inclusion programme recherche", tvgsoComplementExport.getInclusionProtocoleRech(con, getEchantillon()));
         flag = "Nom programme";
         map.put("Nom programme", tvgsoComplementExport.getNomProtocoleRech(con, getEchantillon()));

         /* colonne 66 champs spécifique du cancer item 65 INCa*/
         flag = "Champ specifique";
         map.put("Champ specifique", tvgsoComplementExport.getChampSpecCancer(con, getPatient(), getBanque()));

         /*44a information du patient* -> VIDE */
         flag = "information du patient";
         map.put("information du patient", "");

         /*44b consentement*/
         flag = "Consentement";
         map.put("Consentement",
            tvgsoComplementExport.getConsentement(getPrelevement(), ".*AUTORISATION.*", "(.*ATTENTE.*)|(.*REFUS.*)"));
      }catch(final ItemException ie){
         map.put("Erreur",
            "Erreur echantillon: " + getEchantillon().getCode() + ". Item :" + flag + ". Message: " + ie.getMessage());
         log.info("Erreur echantillon: " + getEchantillon().getCode() + ". Item :" + flag + ". Message: " + ie.getMessage());

      }catch(final Exception e){
         map.put("Erreur", "Erreur non attendue echantillon: " + getEchantillon().getCode() + ". Item :" + flag + ". Message: "
            + e.getMessage());
         log.error("Erreur echantillon: " + getEchantillon().getCode() + ". Item :" + flag + ". Message: " + e.getMessage());
      }

      return map;
   }

   @Override
   public List<String> getHeaders(){
      final List<String> headers = new ArrayList<>();
      headers.add("FINESS");
      headers.add("Identifiant patient");
      headers.add("Date naissance");
      headers.add("Sexe");
      headers.add("Etat du patient");
      headers.add("Date de l'Etat");
      //headers.add("Cause deces"); 
      headers.add("Diagnostic principal");
      headers.add("Date du diagnostic");
      headers.add("Version TNM");
      headers.add("T du cTNM");
      headers.add("N du cTNM");
      headers.add("M du cTNM");
      headers.add("Centre stockage");
      headers.add("Contact");
      headers.add("Code prelevement");
      headers.add("Numero sejour");
      headers.add("Date prelevement");
      headers.add("Mode prelevement");
      headers.add("Classification");
      headers.add("Code organe");
      headers.add("Type lesionnel");
      headers.add("Type evenement");
      headers.add("code organe tumeur primitive");
      headers.add("Version pTNM");
      headers.add("T du pTNM");
      headers.add("N du pTNM");
      //headers.add("pM"); 
      headers.add("Echantillon tumoral/non tumoral");
      headers.add("Mode conservation tumoral");
      headers.add("Type echantillon tumoral");
      headers.add("Mode preparation tumoral");
      headers.add("Delai congelation tumoral");
      headers.add("Controles sur tissu tumoral");
      headers.add("Quantite echantillon tumoral");
      headers.add("Quantite unite echantillon tumoral");
      headers.add("Pourcentage cellules tumorales");
      headers.add("ADN derive tumoral");
      headers.add("ARN derive tumoral");
      headers.add("Proteines derives tumoral");
      headers.add("Echantillon tumoral/non tumoral");
      headers.add("Identifiant echantillon");
      headers.add("Mode conservation non tumoral");
      headers.add("Type echantillon non tumoral");
      headers.add("Mode preparation non tumoral");
      headers.add("Delai congelation non tumoral");
      headers.add("Controles sur tissu non tumoral");
      headers.add("Quantite echantillon non tumoral");
      headers.add("Quantite unite echantillon non tumoral");
      headers.add("ADN derive non tumoral");
      headers.add("ARN derive non tumoral");
      headers.add("Proteines derives non tumoral");
      headers.add("Serum");
      headers.add("Plasma");
      headers.add("Liquides");
      headers.add("ADN");
      headers.add("CR anapath interrogeable");
      headers.add("Donnees cliniques disponibles");
      headers.add("Inclusion protocole therapeutique");
      headers.add("Nom protocole");
      headers.add("Caryotype");
      headers.add("Anomalie eventuelle");
      headers.add("Anomalie genomique");
      headers.add("Description anomalie genomique");
      headers.add("Controle qualite");
      headers.add("Inclusion programme recherche");
      headers.add("Nom programme");
      headers.add("Champ specifique");
      headers.add("Information du patient");
      headers.add("Consentement");

      return headers;
   }

   @Override
   public void printDataIntoRow(final Object obj, final int index, final int nbRow, final List<String> values){
      try{
         getExportUtils().addDataToRowCSV((BufferedWriter) obj, index, values, "|", "|\n");
      }catch(final IOException e){
         log.error(e);
      }
   }
}
