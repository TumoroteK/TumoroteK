package fr.aphp.tumorotek.manager.impl.io.export.standard;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportTVGSOManagerImpl extends ExportCatalogueManagerImpl
{

   private final Logger log = LoggerFactory.getLogger(ExportTVGSOManagerImpl.class);

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
         log.error("An error occurred: {}", e.toString()); 
      }
   }
}