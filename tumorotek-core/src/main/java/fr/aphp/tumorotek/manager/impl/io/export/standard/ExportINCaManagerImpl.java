package fr.aphp.tumorotek.manager.impl.io.export.standard;

import java.util.ArrayList;
import java.util.List;


public class ExportINCaManagerImpl extends ExportCatalogueManagerImpl
{

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