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

import fr.aphp.tumorotek.dto.OutputStreamData;
import fr.aphp.tumorotek.manager.io.document.*;
import fr.aphp.tumorotek.manager.io.production.DocumentProducer;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.utils.TKStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Classe abstraite fournissant une implémentation de base pour la génération de plans de congélateurs.
 * Elle devrait se concentrer uniquement sur la génération du contenu (c'est-à-dire, `DocumentWithDataAsTable`).
 * Les spécificités de format devraient être gérées par les implémentations  concrètes et les producteurs.
 */

public abstract class AbstractPlanCongelateurGenerator implements PlanCongelateurGenerator {

    protected static final String DATE_FORMAT = "yyyyMMddHHmm";


    @Override
    public OutputStreamData generate(List<Conteneur> listConteneurs, Locale locale) {
        OutputStreamData outputStreamData = new OutputStreamData();
        List<DocumentWithDataAsTable> listPlanConteneur = new ArrayList<>();

        for (Conteneur conteneur : listConteneurs) {
            listPlanConteneur.add(buildPlanConteneur(conteneur, locale));
        }
        getDocumentProducer().produce(listPlanConteneur, outputStreamData);
        String filename = String.format("%s.%s", getFileNamePrefix(listConteneurs), outputStreamData.getFormat());
        outputStreamData.setFileName(filename);
        return outputStreamData;
    }

    /**
     * Construit le préfixe du nom de fichier sans l'extension. La liste est nécessaire pour obtenir les informations.
     *
     * @param listConteneurs indique s'il y a plusieurs conteneurs ou un
     * @return le préfixe du nom de fichier
     */
    protected abstract String getFileNamePrefix(List<Conteneur> listConteneurs);


    /**
     * Construit un plan de conteneur sous forme de {@link DocumentWithDataAsTable}, représentant une feuille Excel avec des données sous forme de tableau.
     *
     * @param conteneur le conteneur à traiter
     * @param locale    la locale à utiliser pour la génération
     * @return un document contenant les données du conteneur sous forme de tableau
     */
    protected DocumentWithDataAsTable buildPlanConteneur(Conteneur conteneur, Locale locale) {
        return new DocumentWithDataAsTable(
                conteneur.getNom(),
                buildEntetePlan(conteneur, locale),
                buildDetailPlan(conteneur, locale),
                buildPiedPagePlan(conteneur)
        );
    }


    /**
     * Construit l'en-tête du plan, fournissant des informations sur le conteneur.
     *
     * @param conteneur le conteneur pour lequel construire l'en-tête
     * @param locale    la locale à utiliser pour la génération
     * @return le contexte du document contenant les labels et valeurs
     */
    public DocumentContext buildEntetePlan(Conteneur conteneur, Locale locale) {
        List<LabelValue> listLabelValue = new ArrayList<>();

        String currentDate = TKStringUtils.getCurrentDate(null);
        listLabelValue.add(new LabelValue(currentDate, "", true, false));

        String containerNameLabel = "Nom de congélateur";
        String nomConteneur = conteneur.getNom();
        listLabelValue.add(new LabelValue(containerNameLabel, nomConteneur, false, true));

        String descriptionLabel = "Description";
        String conteneurDescription = conteneur.getDescription();
        listLabelValue.add(new LabelValue(descriptionLabel, conteneurDescription, false, false));

        String serviceLabel = "Etablissement / service";
        String service = conteneur.getService().getNom();
        String etabli = conteneur.getService().getEtablissement().getNom();
        String serviceEtabliValue = etabli + " / " + service;
        listLabelValue.add(new LabelValue(serviceLabel, serviceEtabliValue, false, false));

        return new DocumentContext(listLabelValue);
    }


    /**
     * Construit le pied de page du plan. Dans l'implémentation Excel, le pied de page est affiché uniquement lors de l'impression.
     *
     * @param conteneur le conteneur pour lequel construire le pied de page
     * @return le pied de page du document
     */
    public DocumentFooter buildPiedPagePlan(Conteneur conteneur) {
        String contenurName = conteneur.getNom();
        return new DocumentFooter(contenurName, null, null);
    }

    /**
     * Construit les détails du plan principal où les données principales sont écrites.
     *
     * @param conteneur le conteneur pour lequel construire les détails du plan
     * @param locale    la locale à utiliser pour la génération
     * @return les données du document
     */
    protected abstract DocumentData buildDetailPlan(Conteneur conteneur, Locale locale);


    /**
     * Obtient le producteur de documents utilisé pour produire le fichier.
     * Ce producteur est spécifique au type de fichier : par exemple, Excel aura sa propre implémentation,
     * PDF la sienne, etc.
     *
     * @return le producteur
     */
    protected abstract DocumentProducer getDocumentProducer();

}
