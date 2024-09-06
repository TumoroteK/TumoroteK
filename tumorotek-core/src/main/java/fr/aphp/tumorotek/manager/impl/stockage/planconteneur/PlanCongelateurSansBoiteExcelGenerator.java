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
import fr.aphp.tumorotek.manager.impl.io.production.DocumentWithDataAsArrayExcelProducer;
import fr.aphp.tumorotek.manager.io.document.DocumentWithDataAsArray;
import fr.aphp.tumorotek.manager.io.production.DocumentProducer;

import java.util.List;


/**
 * La classe  étend {@link PlanCongelateurSansBoiteGenerator}  et est responsable de la génération d'un plan
 * pour un congélateur sans boîte au format Excel.
 *
 * <p>Elle utilise un producteur de documents {@link DocumentWithDataAsArrayExcelProducer} pour créer le fichier
 * Excel à partir des données fournies. La méthode {@code buildFileName} génère le nom de fichier pour le document
 * Excel basé sur une liste de conteneurs.</p>
 *
 */
public class PlanCongelateurSansBoiteExcelGenerator extends PlanCongelateurSansBoiteGenerator {


    private DocumentWithDataAsArrayExcelProducer documentWithDataAsArrayExcelProducer;


    @Override
    protected OutputStreamData produceOutput(List<DocumentWithDataAsArray> listPlanConteneur) {
        OutputStreamData outputStreamData = new OutputStreamData();
        documentWithDataAsArrayExcelProducer.produce(listPlanConteneur, outputStreamData);
        return outputStreamData;
    }

    @Override
    protected String buildFileName() {
        return null;
    }

    @Override
    protected DocumentProducer getDocumentProducer() {
        return documentWithDataAsArrayExcelProducer;
    }
}