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
package fr.aphp.tumorotek.manager.impl.stockage.planconteneur;

import fr.aphp.tumorotek.manager.io.production.DocumentProducer;
import fr.aphp.tumorotek.manager.io.production.OutputStreamData;
import fr.aphp.tumorotek.manager.io.document.DocumentContext;
import fr.aphp.tumorotek.manager.io.document.DocumentData;
import fr.aphp.tumorotek.manager.io.document.DocumentFooter;
import fr.aphp.tumorotek.manager.io.document.DocumentWithDataAsArray;
import fr.aphp.tumorotek.model.stockage.Conteneur;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Classe abstraite fournissant une implémentation de base pour la génération de plans de congélateurs.
 *
 * Cette classe implémente l'interface {@link PlanCongelateurGenerator} et fournit une
 * implémentation de la méthode  generate. Les classes concrètes
 * qui étendent cette classe abstraite devront fournir des implémentations spécifiques
 * pour les méthodes abstraites définies ici.
 *
 * <p>
 * La méthode {@code generate} génère des données de sortie en se basant sur une liste de
 * conteneurs, un paramètre de locale, et un nom de fichier. Elle utilise des méthodes protégées
 * pour construire les différentes sections du plan.
 * </p>
 *
 */

public abstract class AbstractPlanCongelateurGenerator implements PlanCongelateurGenerator {

    public OutputStreamData generate(List<Conteneur> list, Locale locale) {
        List<DocumentWithDataAsArray> listPlanConteneur = new ArrayList();
        for (Conteneur conteneur : list) {
            listPlanConteneur.add(buildPlanConteneur(conteneur, locale));
        }

        return getDocumentProducer().produce(listPlanConteneur);

    }

    protected DocumentWithDataAsArray buildPlanConteneur(Conteneur c, Locale locale) {
        return new DocumentWithDataAsArray(buildNomPlan(), buildEntetePlan(c, locale), buildDetailPlan(c, locale),
                buildPiedPagePlan(c));
    }

    protected DocumentContext buildEntetePlan(Conteneur c, Locale locale) {
        //pini to implement
        return null;
    }


    protected DocumentFooter buildPiedPagePlan(Conteneur c) {
        //pini to implement
        return null;
    }

    protected abstract DocumentData buildDetailPlan(Conteneur c, Locale locale);

    protected abstract String buildNomPlan();

    protected abstract DocumentProducer getDocumentProducer();

}
