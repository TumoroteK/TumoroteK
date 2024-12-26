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
package fr.aphp.tumorotek.action.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.stockage.ListeStockages;
import fr.aphp.tumorotek.dto.OutputStreamData;
import fr.aphp.tumorotek.manager.impl.stockage.planconteneur.AbstractPlanCongelateurGenerator;
import fr.aphp.tumorotek.model.stockage.Conteneur;

public class StockageUtils
{

   private static final Logger log = LoggerFactory.getLogger(StockageUtils.class);
   
   /**
    * Génère un fichier Excel pour les plans de conteneurs, incluant ou non des boîtes.
    *
    * @param conteneurs Liste des conteneurs à traiter.
    * @param avecBoites Indique si le fichier Excel doit inclure des informations sur les boîtes.
    */
   public static void createExcelForPlanConteneur(List<Conteneur> conteneurs, boolean withBoites){
      try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

          // Récupération du générateur spécifique selon le besoin (avec ou sans boîtes)
          AbstractPlanCongelateurGenerator generator = 
             withBoites ? ManagerLocator.getPlanCongelateurAvecBoiteExcelGenerator()
                : ManagerLocator.getPlanCongelateurSansBoiteExcelGenerator();
          
          // Génération du fichier Excel directement dans le flux de sortie
          OutputStreamData result = generator.generate(conteneurs);
   
          // Envoi du fichier généré à l'utilisateur avec le type MIME approprié pour Excel
          Filedownload.save(result.getOutputStream().toByteArray(), result.getContentType(), result.getFileName());

       } catch (Exception e) {
          StringBuilder messageErrorForLog = new StringBuilder("Erreur lors de la génération du plan ").append(withBoites ? "avec":"sans").append(" boites des conteneurs : ")
             .append(conteneurs == null ? "null" : conteneurs.stream().map(c -> c.getNom()).collect(Collectors.joining(",")));
          log.error(messageErrorForLog.toString(), e);
          String labelErrorKey = null;
          if(conteneurs == null) {
             labelErrorKey = "error.generation.aucun.planConteneur";
          }
          else {
             labelErrorKey = (conteneurs.size() == 1 ? "error.generation.du.planConteneur" : "error.generation.des.planConteneur");
          }
          Messagebox.show(Labels.getLabel(labelErrorKey), "Error", Messagebox.OK, Messagebox.ERROR);
       }   

   }

}
