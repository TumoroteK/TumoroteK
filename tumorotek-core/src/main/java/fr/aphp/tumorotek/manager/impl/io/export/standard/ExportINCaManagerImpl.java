package fr.aphp.tumorotek.manager.impl.io.export.standard;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.manager.io.export.standard.IncaComplementExport;
import fr.aphp.tumorotek.manager.io.export.standard.IncaEchantillonExport;
import fr.aphp.tumorotek.manager.io.export.standard.IncaMaladieExport;
import fr.aphp.tumorotek.manager.io.export.standard.IncaPatientExport;
import fr.aphp.tumorotek.manager.io.export.standard.IncaPrelevementExport;
import fr.aphp.tumorotek.manager.io.export.standard.IncaSpecifiqueExport;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

public class ExportINCaManagerImpl extends ExportCatalogueManagerImpl
{

   private final Log log = LogFactory.getLog(ExportINCaManagerImpl.class);

   private IncaPatientExport incaPatientExport;
   private IncaMaladieExport incaMaladieExport;
   private IncaPrelevementExport incaPrelevementExport;
   private IncaEchantillonExport incaEchantillonExport;
   private IncaComplementExport incaComplementExport;
   private IncaSpecifiqueExport incaSpecifiqueExport;

   public void setIncaPatientExport(final IncaPatientExport iExport){
      this.incaPatientExport = iExport;
   }

   public void setIncaMaladieExport(final IncaMaladieExport iExport){
      this.incaMaladieExport = iExport;
   }

   public void setIncaPrelevementExport(final IncaPrelevementExport iExport){
      this.incaPrelevementExport = iExport;
   }

   public void setIncaEchantillonExport(final IncaEchantillonExport iExport){
      this.incaEchantillonExport = iExport;
   }

   public void setIncaComplementExport(final IncaComplementExport iExport){
      this.incaComplementExport = iExport;
   }

   public void setIncaSpecifiqueExport(final IncaSpecifiqueExport iExport){
      this.incaSpecifiqueExport = iExport;
   }

   @Override
   public Map<String, String> objetExport(final Echantillon echan, final Connection con){

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
         map.put("FINESS", incaPatientExport.getFiness(con, getPrelevement(), getPatient()));

         /*Item 2 Code patient OBLIGATOIRE */
         flag = "Identifiant patient";
         map.put("Identifiant patient", incaPatientExport.getPatientId(getPatient()));

         /*Item 3 Date de naissance du patient OBLIGATOIRE */
         flag = "Date naissance";
         map.put("Date naissance", incaPatientExport.getDateNaissance(getPatient(), getDateFormatterINCA()));

         /*Item 4 Sexe du patient OBLIGATOIRE */
         flag = "Sexe";
         map.put("Sexe", incaPatientExport.getSexe(getPatient()));

         /*Item 5 Etat du patient */
         flag = "Etat du patient";
         map.put("Etat du patient", incaPatientExport.getPatientEtat(getPatient()));

         /*Item 6 Date de l'etat du patient */
         flag = "Date de l'Etat";
         map.put("Date de l'Etat", incaPatientExport.getDateEtat(getPatient(), getDateFormatterINCA()));

         /***********************************************************/
         /************* INFORMATIONS MALADIE ************************/
         /***********************************************************/
         /*Item 7 Diagnostic principal de la tumeur initiale OBLIGATOIRE*/
         flag = "Diagnostic principal";
         map.put("Diagnostic principal", incaMaladieExport.getDiagnosticPrincipal(con, getPrelevement(), true));

         /*Item 8 Date du diagnostic */
         flag = "Date du diagnostic";
         map.put("Date du diagnostic", incaMaladieExport.getDateDiag(con, getPrelevement(), true, getDateFormatterINCA()));

         /*Item 9 Version cTNM */
         flag = "Version TNM";
         map.put("Version TNM", incaMaladieExport.getVersionCTNM(con, getPrelevement()));

         /*Item 10 Taille de la tumeur */
         flag = "T du cTNM";
         map.put("T du cTNM", incaMaladieExport.getTailleTumeur(con, getPrelevement()));

         /*Item 11 Envahissement ganglionnaire */
         flag = "N du cTNM";
         map.put("N du cTNM", incaMaladieExport.getEnvahGangR(con, getPrelevement()));

         /*Item 12 Extension metastatique */
         flag = "M du cTNM";
         map.put("M du cTNM", incaMaladieExport.getExtMetastatique(con, getPrelevement()));

         /********************************************************/
         /************ INFORMATIONS PRELEVELEMENT ****************/
         /********************************************************/

         /*Item 13 Centre se stockage OBLIGATOIRE*/
         flag = "Centre stockage";
         map.put("Centre stockage", incaPrelevementExport.getCentreStockage(getEchantillon()));

         /*Item 14 ID Prelevement OBLIGATOIRE*/
         flag = "ID prelevement";
         map.put("ID prelevement", incaPrelevementExport.getCodePrelevement(getPrelevement()));

         /*colonne 15 Date prelevement OBLIGATOIRE */
         flag = "Date prelevement";
         map.put("Date prelevement", incaPrelevementExport.getDatePrelevement(getPrelevement(), getDateFormatterINCA()));

         /*colonne 16 Mode de prelevement OBLIGATOIRE */
         flag = "Mode prelevement";
         map.put("Mode prelevement", incaPrelevementExport.getModePrelevement(getPrelevement()));

         /*colonne 17 Classification utilisee OBLIGATOIRE*/
         flag = "Classification";
         map.put("Classification", incaPrelevementExport.getClassif(getBanque()));

         /* colonne 18 Code organe CIM*/
         flag = "Code organe CIM";
         if(map.get("Classification").contains("C")){
            map.put("Code organe CIM", incaPrelevementExport.getCodeOrgane(con, getEchantillon(), "C"));
         }else{
            map.put("Code organe CIM", "");
         }

         /* colonne 19 Type lésionnel CIM*/
         flag = "Type lesionnel CIM";
         if(map.get("Classification").contains("C")){
            map.put("Type lesionnel CIM", incaPrelevementExport.getTypeLesionnel(con, getEchantillon(), "C"));
         }else{
            map.put("Type lesionnel CIM", "");
         }

         /* colonne 20 Code organe ADICAP */
         flag = "Code organe ADICAP";
         if(map.get("Classification").contains("A")){
            map.put("Code organe ADICAP", incaPrelevementExport.getCodeOrgane(con, getEchantillon(), "A"));
         }else{
            map.put("Code organe ADICAP", "");
         }

         /* colonne 21 Type lesionnel ADICAP*/
         flag = "Type lesionnel ADICAP";
         if(map.get("Classification").contains("A")){
            map.put("Type lesionnel ADICAP", incaPrelevementExport.getCodeOrgane(con, getEchantillon(), "A"));
         }else{
            map.put("Type lesionnel ADICAP", "");
         }

         /*colonne22 Type d'evenement OBLIGATOIRE item22 INCa*/
         flag = "TYPE_EVENT";
         map.put("TYPE_EVENT", incaPrelevementExport.getTypeEvent(con, getPrelevement()));

         /*colonne 23 Version du pTNM */
         flag = "Version pTNM";
         map.put("Version pTNM", incaPrelevementExport.getVersionPTNM(con, getPrelevement()));

         /*colonne 24 pT */
         flag = "T du pTNM";
         map.put("T du pTNM", incaPrelevementExport.getTailleTumeurPT(con, getPrelevement()));

         /*colonne 25 Presence/absence metastases pN */
         flag = "N du pTNM";
         map.put("N du pTNM", incaPrelevementExport.getEnvGangPN(con, getPrelevement()));

         /*colonne 26 Presence/absence metastases distance pM*/
         flag = "pM";
         map.put("pM", incaPrelevementExport.getExtMetastaticPM(con, getPrelevement()));

         /********************************************************/
         /************ INFORMATIONS ECHANTILLON ******************/
         /********************************************************/
         boolean isTumoral = false;
         /*colonne 27 echantillon tumoral ou non tumoral */
         /* OBLIGATOIRE Item27 et Item39*/
         String item27 = "", item39 = "";
         flag = "Echantillon tumoral/non tumoral";
         if(incaEchantillonExport.getIsTumoral(getEchantillon()).equals("O")){
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
            item28 = incaEchantillonExport.getModeConservation(getEchantillon());
            item40 = "";
         }else{
            flag = "Mode conservation non tumoral";
            item40 = incaEchantillonExport.getModeConservation(getEchantillon());
            item28 = "";
         }

         /*colonne 29 et 41 type echantillon*/
         String item29 = "", item41 = "";
         if(isTumoral){
            flag = "Type echantillon tumoral";
            item29 = incaEchantillonExport.getEchantillonType(getEchantillon());
         }else{
            flag = "Type echantillon non tumoral";
            item41 = incaEchantillonExport.getEchantillonType(getEchantillon());
         }

         /* colonne 30 Item30 (et item42) Mode de preparation*/
         String item30 = "", item42 = "";
         if(isTumoral){
            flag = "Mode preparation tumoral";
            item30 = incaEchantillonExport.getModePreparation(getEchantillon());
         }else{
            flag = "Mode preparation non tumoral";
            item42 = incaEchantillonExport.getModePreparation(getEchantillon());
         }

         /*colonne 31 Item31 (et item43) Delai de congelation*/
         String item31 = "", item43 = "";
         if(isTumoral){
            flag = "Delai congelation tumoral";
            item31 = incaEchantillonExport.getDelaiCongelation(getEchantillon());
         }else{
            flag = "Delai congelation non tumoral";
            item43 = incaEchantillonExport.getDelaiCongelation(getEchantillon());
         }

         /*colonne 32 Item32 et (item44) Controles sur tissu*/
         String item32 = "", item44 = "";
         if(isTumoral){
            flag = "Controles sur tissu tumoral";
            item32 = incaEchantillonExport.getControles(con, getEchantillon());
         }else{
            flag = "Controles sur tissu non tumoral";
            item44 = incaEchantillonExport.getControles(con, getEchantillon());
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
         }else{
            throw new ItemException(2, "Quantite et/ou unité manquant");
         }

         /*colonne 35 Item35 Pourcentage cellules tumorales*/
         String item35 = "";
         if(isTumoral){
            flag = "Pourcentage cellules tumorales";
            item35 = incaEchantillonExport.getPourcentageCellulesTumorales(con, echan);
         }

         /* colonnes 36, 37, 38: Item36, item37, item38*/
         /* (et item47, item48, item49) */
         /* respectivement ADN, ARN, proteine derives*/
         String item36 = "N", item37 = "N", item38 = "N", item47 = "N", item48 = "N", item49 = "N";
         if(isTumoral){
            flag = "ADN derive tumoral";
            item36 = incaEchantillonExport.getProdTypeAssocie(con, getEchantillon(), "([Aa][Dd][nN])|([Dd][nN][Aa])");
            flag = "ARN derive tumoral";
            item37 = incaEchantillonExport.getProdTypeAssocie(con, getEchantillon(), "([Aa][Rr][nN])|([Rr][nN][Aa])");
            flag = "Proteines derives tumoral";
            item38 = incaEchantillonExport.getProdTypeAssocie(con, getEchantillon(), ".*[Pp][Rr][Oo][Tt][Ee][Ii][Nn].*");
         }else{
            flag = "ADN derive non tumoral";
            item47 = incaEchantillonExport.getProdTypeAssocie(con, getEchantillon(), "([Aa][Dd][nN])|([Dd][nN][Aa])");
            flag = "ARN derive non tumoral";
            item48 = incaEchantillonExport.getProdTypeAssocie(con, getEchantillon(), "([Aa][Rr][nN])|([Rr][nN][Aa])");
            flag = "Proteines derives non tumoral";
            item49 = incaEchantillonExport.getProdTypeAssocie(con, getEchantillon(), ".*[Pp][Rr][Oo][Tt][Ee][Ii][Nn].*");
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

         //Item50, Item51, Item52, 
         //Item53 respectivement serum, plasma, liquides, ADN*/		
         flag = "Serum";
         map.put("Serum", incaEchantillonExport.getRessourceBiolAssociee(getEchantillon(), "%SERUM%"));
         flag = "Plasma";
         map.put("Plasma", incaEchantillonExport.getRessourceBiolAssociee(getEchantillon(), "%PLASMA%"));
         flag = "Liquides";
         map.put("Liquides", incaEchantillonExport.getRessourceBiolAssociee(getEchantillon(), "%LIQUIDES%"));
         //ADN Constitutionnel
         flag = "ADN";
         map.put("ADN", incaEchantillonExport.getADNconstitutionnel(con, getEchantillon(), "%GENETIQUE%"));

         /***********************************************************/
         /******** RENSEIGNEMENT COMPLEMENTAIRES ********************/
         /***********************************************************/

         /*Item54 Compte rendu anapath interrogeable*/
         flag = "CR anapath interrogeable";
         map.put("CR anapath interrogeable", incaComplementExport.getCRAnapathInterro(con, getPrelevement()));

         /*Item55 Donnees cliniques disponibles dans une base*/
         flag = "Donnees cliniques disponibles";
         map.put("Donnees cliniques disponibles", incaComplementExport.getDonneesClinBase(con, getPatient(), getBanque()));

         /*Item56, item57 Inclusion dans un protocole therapeutique */
         /* et nom du protocole*/
         flag = "Inclusion protocole therapeutique";
         map.put("Inclusion protocole therapeutique", incaComplementExport.getInclusionTherap(con, getPatient(), getBanque()));
         flag = "Nom protocole";
         map.put("Nom protocole", incaComplementExport.getNomProtocoleTherap(con, getPatient(), getBanque()));

         /*Item58 item59 Caryotype et anomalies eventuelles*/
         flag = "Caryotype";
         map.put("Caryotype", incaComplementExport.getCaryotype(con, getPatient(), getBanque()));
         flag = "Anomalie eventuelle";
         map.put("Anomalie eventuelle", incaComplementExport.getAnomalieCaryo(con, getPatient(), getBanque()));

         /*Item60 item61 Anomalie genomique et description*/
         flag = "Anomalie genomique";
         map.put("Anomalie genomique", incaComplementExport.getAnomalieGenomique(con, getPatient(), getBanque()));
         flag = "Description anomalie genomique";
         map.put("Description anomalie genomique",
            incaComplementExport.getAnomalieGenomiqueDescr(con, getPatient(), getBanque()));

         /*Item62 Controle qualite*/
         flag = "Controle qualite";
         map.put("Controle qualite", incaComplementExport.getControleQualite(getEchantillon()));

         /*Item63 item64 Inclusion dans un protocole de recherche 
         /* et nom du programme*/
         flag = "Inclusion programme recherche";
         map.put("Inclusion programme recherche", incaComplementExport.getInclusionProtocoleRech(con, getPrelevement()));
         flag = "Nom programme";
         map.put("Nom programme", incaComplementExport.getNomProtocoleRech(con, getPrelevement()));

         /* Item 65 champs spécifique du cancer*/
         flag = "Champ specifique";
         map.put("Champ specifique", incaComplementExport.getChampSpecCancer(con, getPrelevement()));

         /* Item 66 Contact nom prenom */
         /* Item 67 mail contact */
         /* Item 68 tel contact */
         flag = "Contact";
         final List<String> contact = incaComplementExport.getContact(getBanque());
         map.put("Contact", contact.get(0) + " " + contact.get(1));
         if(!contact.get(2).equals("") || !contact.get(3).equals("")){
            map.put("Mail", contact.get(2));
            map.put("Tel", contact.get(3));
         }else{
            throw new ItemException(2, "Mail et tel du contact manquants");
         }

         /* Item 69 Questionnaire antecedents tabac */
         flag = "questionnaire antecedents tabac";
         map.put("Questionnaire antecedents tabac", incaSpecifiqueExport.getQuestAntTabac(con, getPatient(), getBanque()));

         /* Item 70 Questionnaire familial */
         flag = "questionnaire familial";
         map.put("Questionnaire familial", incaSpecifiqueExport.getQuestFamilial(con, getPatient(), getBanque()));

         /* Item 71 Questionnaire professionnel */
         flag = "questionnaire professionnel";
         map.put("Questionnaire professionnel", incaSpecifiqueExport.getQuestPro(con, getPatient(), getBanque()));

         /* Item 72 Echantillon radio-naif */
         flag = "Echantillon radio-naif";
         map.put("Echantillon radio-naif", incaSpecifiqueExport.getRadioNaif(con, getEchantillon()));

         /* Item 73 Echantillon chimio-naif */
         flag = "Echantillon chimio-naif";
         map.put("Echantillon chimio-naif", incaSpecifiqueExport.getChimioNaif(con, getEchantillon()));

         /* Item 74 Statut tabac */
         flag = "Statut tabac";
         map.put("Statut tabac", incaSpecifiqueExport.getStatutTabac(con, getPatient(), getBanque()));

         /* Item 75 NPA */
         flag = "NPA";
         map.put("NPA", incaSpecifiqueExport.getNPA(con, getPatient(), getBanque()));

         /*Item 76 Cause de deces du patient */
         flag = "Cause deces";
         map.put("Cause deces", incaPatientExport.getCauseDeces(con, getPatient(), getBanque()));

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
      headers.add("Diagnostic principal");
      headers.add("Date du diagnostic");
      headers.add("Version cTNM");
      headers.add("cT");
      headers.add("cN");
      headers.add("cM");
      headers.add("Centre stockage");
      headers.add("ID prelevement");
      headers.add("Date prelevement");
      headers.add("Type prelevement");
      headers.add("Classification");
      headers.add("Code organe CIM");
      headers.add("Type lesionnel CIM");
      headers.add("Code organe ADICAP");
      headers.add("Type lesionnel ADICAP");
      headers.add("Type evenement");
      headers.add("Version pTNM");
      headers.add("pT");
      headers.add("pN");
      headers.add("pM");
      headers.add("Echantillon tumoral");
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
      headers.add("Echantillon non tumoral");
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
      headers.add("ADN constitutionnel");
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
      headers.add("Contact nom");
      headers.add("Contact email");
      headers.add("Contact tel");
      headers.add("Questionnaire antécédents tabac");
      headers.add("Questionnaire familial");
      headers.add("Questionnaire professionnel");
      headers.add("Echantillon radio-naif");
      headers.add("Echantillon chimio-naif");
      headers.add("Statut tabac");
      headers.add("NPA");
      headers.add("Cause deces");

      return headers;
   }

}
