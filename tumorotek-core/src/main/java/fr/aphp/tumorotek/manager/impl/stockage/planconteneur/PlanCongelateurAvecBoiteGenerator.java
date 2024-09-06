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
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.utils.TKStringUtils;

import java.util.*;

/**
 * Classe abstraite fournissant une implémentation de base pour la génération de plans de congélateurs.
 * Elle devrait se concentrer uniquement sur la génération du contenu  * (c'est-à-dire, `DocumentWithDataAsArray`).
 * Les spécificités de format devraient être gérées par les implémentations  * concrètes et les producteurs.
 *
 */

public abstract class PlanCongelateurAvecBoiteGenerator implements PlanCongelateurGenerator {

    public OutputStreamData generate(List<Conteneur> list, Locale locale) {
        List<DocumentWithDataAsArray> listPlanConteneur = new ArrayList<>();
        for (Conteneur conteneur : list) {
            listPlanConteneur.add(buildPlanConteneur(conteneur, locale));
        }
        return produceOutput(listPlanConteneur);
    }

    protected abstract OutputStreamData produceOutput(List<DocumentWithDataAsArray> listPlanConteneur);

    protected DocumentWithDataAsArray buildPlanConteneur(Conteneur conteneur, Locale locale) {
        return new DocumentWithDataAsArray(
                buildFileName(),
                buildEntetePlan(conteneur, locale),
                buildDetailPlan(conteneur, locale),
                buildPiedPagePlan(conteneur)
        );
    }

    private void sortBoitesByPosition(List<Terminale> boites) {
        if (boites == null) return;

        Collections.sort(boites, Comparator.comparingInt(Terminale::getPosition));
    }


    public DocumentContext buildEntetePlan(Conteneur conteneur, Locale locale) {
        List<LabelValue> listLabelValue = new ArrayList<>();

        String currentDate = TKStringUtils.getCurrentDate(null);
        listLabelValue.add(new LabelValue(currentDate, "", true, false));

        // 2. Label for "Champ.Retour.Conteneur" and its value
        String containerNameLabel = "Nom de congélateur";
        String nomConteneur = conteneur.getNom();
        listLabelValue.add(new LabelValue(containerNameLabel, nomConteneur, false, true));

        // 3. Label for "conteneur.description" and its value
        String descriptionLabel = "Description";
        String conteneurDescription = conteneur.getDescription();
        listLabelValue.add(new LabelValue(descriptionLabel, conteneurDescription, false, false));

        // 4. Label for "stockage.excel.plan.etablish.service" and its value
        // todo: add etablish
        String serviceLabel = "Etablissement / service";
        String serviceValue = conteneur.getService().getNom();
        listLabelValue.add(new LabelValue(serviceLabel, serviceValue, false, false));


        return new DocumentContext(listLabelValue);
    }


    public DocumentFooter buildPiedPagePlan(Conteneur conteneur) {
        String contenurName = conteneur.getNom();
        return new DocumentFooter(contenurName, null, null);
    }

    protected abstract DocumentData buildDetailPlan(Conteneur conteneur, Locale locale);

    protected abstract String buildFileName();

    protected abstract DocumentProducer getDocumentProducer();

}
