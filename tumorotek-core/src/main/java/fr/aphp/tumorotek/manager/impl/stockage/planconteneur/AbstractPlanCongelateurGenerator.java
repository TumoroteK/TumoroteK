/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.manager.impl.stockage.planconteneur;

import fr.aphp.tumorotek.dto.DocumentProducerResult;
import fr.aphp.tumorotek.dto.OutputStreamData;
import fr.aphp.tumorotek.manager.io.document.DocumentContext;
import fr.aphp.tumorotek.manager.io.document.DocumentData;
import fr.aphp.tumorotek.manager.io.document.DocumentFooter;
import fr.aphp.tumorotek.manager.io.document.DocumentWithDataAsTable;
import fr.aphp.tumorotek.manager.io.document.LabelValue;
import fr.aphp.tumorotek.manager.io.production.DocumentProducer;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.utils.TKStringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe abstraite fournissant une implémentation de base pour la génération de plans de congélateurs.
 * Elle devrait se concentrer uniquement sur la génération du contenu (c'est-à-dire, `DocumentWithDataAsTable`).
 * Les spécificités de format devraient être gérées par les implémentations concrètes et les producteurs.
 *
 * <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */

public abstract class AbstractPlanCongelateurGenerator implements PlanCongelateurGenerator
{

   // Format de date utilisé pour les noms de fichiers
   protected static final String DATE_FORMAT = "yyyyMMddHHmm";

   // Constante représentant le contenu d'une cellule correspondant à un emplacement sans enceinte
   protected static final String EMPTY_POSITION = "(Vide)";

   // Préfixe utilisé pour nommer les fichiers générés
   protected static final String PREFIX_FILE_NAME = "plan_conteneur";


   @Override
   public OutputStreamData generate(List<Conteneur> listConteneurs) throws IOException{
      // Crée une liste pour stocker les plans de chaque conteneur
      List<DocumentWithDataAsTable> listPlanConteneur = new ArrayList<>();

      for(Conteneur conteneur : listConteneurs){
         listPlanConteneur.add(buildPlanConteneur(conteneur));
      }
      // Produit le document final avec le format spécifique (Excel, PDF, etc.)
      DocumentProducerResult producerResult = getDocumentProducer().produce(listPlanConteneur);
      // Génère un nom de fichier unique basé sur la date courante
      String currentDate = TKStringUtils.getCurrentDate(DATE_FORMAT);

      String fileName =
         new StringBuilder(PREFIX_FILE_NAME).append("_").append(currentDate).append(".").append(producerResult.getFormat())
            .toString();

      return new OutputStreamData(fileName, producerResult);
   }

   /**
    * Construit un plan de conteneur sous forme de {@link DocumentWithDataAsTable}, représentant une feuille Excel avec des données sous forme de tableau.
    *
    * @param conteneur le conteneur à traiter
    * @return un document contenant les données du conteneur sous forme de tableau
    */
   protected DocumentWithDataAsTable buildPlanConteneur(Conteneur conteneur){
      // Crée un document structuré avec en-tête, contenu et pied de page
      DocumentWithDataAsTable documentWithDataAsTable = new DocumentWithDataAsTable(
                           conteneur.getNom(),       // Titre du document
                           buildEntetePlan(conteneur),  // Information g├®n├®rale sur le conteneur
                           buildDetailPlan(conteneur), // Contenu principal (impl├®ment├® par les classes filles)
                           buildPiedPagePlan(conteneur) // Informations de bas de page
      );
      documentWithDataAsTable.setColumnWidth(22);
      return documentWithDataAsTable;

   }

   /**
    * Construit l'en-tête du plan, fournissant des informations sur le conteneur.
    *
    * @param conteneur le conteneur pour lequel construire l'en-tête
    * @return le contexte du document contenant les labels et valeurs
    */
   public DocumentContext buildEntetePlan(Conteneur conteneur){
      // Liste pour stocker les paires label/valeur de l'en-tête
      List<LabelValue> listLabelValue = new ArrayList<>();

      // Ajoute la date courante en première ligne -> Le {date}
      String date = TKStringUtils.getCurrentDate(null);
      String labelText = new StringBuilder("Le ")
              .append(date)
              .toString();
      listLabelValue.add(new LabelValue(labelText, "", true, false));


      // Ajoute les informations du conteneur
      listLabelValue.add(new LabelValue("Conteneur", conteneur.getNom(), false, true));
      listLabelValue.add(new LabelValue("Description", conteneur.getDescription(), false, false));

      // Crée la valeur de service/établissement en une seule ligne
      String serviceEtabliValue = conteneur.getService().getEtablissement().getNom() + " / " +
         conteneur.getService().getNom();
      listLabelValue.add(new LabelValue("Etablissement / service", serviceEtabliValue, false, false));

      return new DocumentContext(listLabelValue);
   }

   /**
    * Construit le pied de page du plan. Dans l'implémentation Excel, le pied de page est affiché uniquement lors de l'impression.
    *
    * @param conteneur le conteneur pour lequel construire le pied de page
    * @return le pied de page du document
    */
   public DocumentFooter buildPiedPagePlan(Conteneur conteneur){
      String contenurName = conteneur.getNom();
      return new DocumentFooter(contenurName, null, null);
   }

   /**
    * Construit les détails du plan principal où les données principales sont écrites.
    *
    * @param conteneur le conteneur pour lequel construire les détails du plan
    * @return les données du document
    */
   protected abstract DocumentData buildDetailPlan(Conteneur conteneur);

   /**
    * Obtient le producteur de documents utilisé pour produire le fichier.
    * Ce producteur est spécifique au type de fichier : par exemple, Excel aura sa propre implémentation,
    * PDF la sienne, etc.
    *
    * @return le producteur
    */
   protected abstract DocumentProducer getDocumentProducer();


   protected  abstract EnceinteManager getEnceinteManager();
   /**
    * Crée une Map de position associant des positions d'enceintes à leurs objets respectifs.
    * Cela facilite la gestion et l'accès aux enceintes en fonction de leurs positions.
    * @param enceintes La liste d'enceintes à mapper.
    * @return Une map associant les positions aux enceintes.
    */

   protected Map<Integer, Enceinte> createMapEnceintesByPosition(List<Enceinte> enceintes){
      // Créer une nouvelle Map vide pour stocker les associations entre positions et enceintes
      Map<Integer, Enceinte> positionMap = new HashMap<>();

      // Vérifier que le paramètre liste n’est pas nul
      if(enceintes != null){
         for(Enceinte enceinte : enceintes){
            // Insérer chaque enceinte dans map selon sa position
            positionMap.put(enceinte.getPosition(), enceinte);
         }
      }

      return positionMap;
   }

/**
    * formate l'alias en ajoutant des parenthèses autour de l'alias fourni, si celui-ci existe.
    *
    * @param alias L'alias à formater. Peut être nul.
    * @return L'alias formaté avec des parenthèses, ou une chaîne vide si l'alias est nul.
    */
   protected String formatAlias(String alias){
      if(alias != null){
         return new StringBuilder("(").append(alias).append(")").toString();
      }else{
         return "";
      }
   }
}
