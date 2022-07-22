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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.xml.BoiteImpression;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 * Utility class fournissant les methodes static utiles dans
 * l'onglet cession.
 * Date: 26/07/2013.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10
 */
public class CessionUtils
{

   /**
    * Méthode qui permet d'afficher la page d'impression de la cession.
    */
   public static Document createFileHtmlToPrint(final Cession cession, final List<CederObjet> echantillonsCedes,
      final List<CederObjet> derivesCedes){

      Document doc = null;

      //		List<CederObjetDecorator> echantillonsCedesDecores =
      //				cedeObjFactory.decorateListe(echantillonsCedes);
      //		List<CederObjetDecorator> derivesCedesDecores =
      //				cedeObjFactory.decorateListe(derivesCedes);

      // s'il y a des éléments a céder
      if(echantillonsCedes.size() > 0 || derivesCedes.size() > 0){

         // création du document, de la page et de son titre
         doc = ManagerLocator.getXmlUtils().createJDomDocumentBoites();
         final Element root = doc.getRootElement();
         final StringBuffer title = new StringBuffer();
         title.append(Labels.getLabel("impression.boite.title.destockage"));
         title.append(" : ");
         title.append(cession.getNumero());
         final Element page = ManagerLocator.getXmlUtils().addPageBoite(root, title.toString());

         // s'il y a des échantillons a céder
         if(echantillonsCedes.size() > 0){
            final List<BoiteImpression> listeBoites = new ArrayList<>();

            // pour chaque échantillon cédé
            for(int i = 0; i < echantillonsCedes.size(); i++){

               // on récupère l'échantillon, son emplacement et la
               // boite
               final Echantillon echan =
                  ManagerLocator.getEchantillonManager().findByIdManager(echantillonsCedes.get(i).getObjetId());
               final Emplacement emp = ManagerLocator.getEchantillonManager().getEmplacementManager(echan);
               if(emp != null){
                  final Terminale term = emp.getTerminale();

                  final BoiteImpression newBi = new BoiteImpression();
                  newBi.setBoite(term);

                  // titre intermédiaire
                  if(i == 0){
                     newBi.setTitreIntermediaire(Labels.getLabel("impression.boite.echanillons"));
                  }

                  // si c'est la 1ère fois que l'on rencontre cette boite
                  if(!listeBoites.contains(newBi)){
                     // on remplit tous titres et légendes
                     newBi.setTitreModelisation(Labels.getLabel("impression.boite.title.visualisation"));
                     newBi.setTitreInstructions(Labels.getLabel("impression.boite.title.instructions"));
                     newBi.setNom(ObjectTypesFormatters.getLabel("impression.boite.nom", new String[] {term.getNom()}));
                     newBi.setTitreListe(Labels.getLabel("impression.boite.elements" + ".title.destockage.echantillons"));
                     newBi.setLegendeVide(Labels.getLabel("impression.boite.legende.vide"));
                     newBi.setLegendePris(Labels.getLabel("impression.boite.legende.pris"));
                     newBi.setLegendeSelectionne(
                        Labels.getLabel("impression.boite.legende.selectionne" + ".destockage.echantillons"));

                     // création des intructions pour récupérer la boite
                     // Récup des parents de la boite
                     final List<Object> parents = ManagerLocator.getTerminaleManager().getListOfParentsManager(term);
                     final List<String> instructions = new ArrayList<>();
                     // pour chaque parent
                     for(int j = 0; j < parents.size(); j++){
                        if(parents.get(j).getClass().getSimpleName().equals("Conteneur")){
                           final Conteneur c = (Conteneur) parents.get(j);
                           instructions.add(ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.conteneur",
                              new String[] {c.getCode()}));
                        }else if(parents.get(j).getClass().getSimpleName().equals("Enceinte")){
                           final Enceinte e = (Enceinte) parents.get(j);
                           instructions.add(ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.enceinte",
                              new String[] {e.getNom()}));
                        }
                     }
                     // ajout des instructions à la boite
                     instructions.add(
                        ObjectTypesFormatters.getLabel("impression.boite.instruction.terminale", new String[] {term.getNom()}));
                     instructions.add(Labels.getLabel("impression.boite.instruction" + ".destockage.echantillons"));
                     newBi.setInstructions(instructions);

                     // ajout de l'échantillon à la liste des éléments
                     // a extraire
                     final List<String> elements = new ArrayList<>();
                     elements.add(ObjectTypesFormatters.getLabel("impression.boite.numero.echantillon",
                        new String[] {"1", echan.getCode()}));
                     newBi.setElements(elements);

                     // ajout de la position de l'échntillon
                     final List<Integer> positions = new ArrayList<>();
                     positions.add(emp.getPosition());
                     newBi.setPositions(positions);

                     // ajout de la boite à la liste
                     listeBoites.add(newBi);

                  }else{
                     // sinon on récupère la boite dans la liste
                     final BoiteImpression bi = listeBoites.get(listeBoites.indexOf(newBi));

                     // ajout de l'échantillon et de sa position aux
                     // éléments à extraire
                     final int pos = bi.getPositions().size() + 1;
                     bi.getPositions().add(emp.getPosition());
                     bi.getElements().add(ObjectTypesFormatters.getLabel("impression.boite.numero.echantillon",
                        new String[] {Integer.toString(pos), echan.getCode()}));
                  }
               }
            }

            final StringBuffer adrImages = new StringBuffer();
            adrImages.append(((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getContextPath());
            adrImages.append("/images/icones/emplacements/");
            for(int i = 0; i < listeBoites.size(); i++){
               ManagerLocator.getXmlUtils().addBoite(page, listeBoites.get(i), adrImages.toString());
            }
         }

         // s'il y a des dérivés a céder
         if(derivesCedes.size() > 0){
            final List<BoiteImpression> listeBoites = new ArrayList<>();

            // pour chaque dérivé cédé
            for(int i = 0; i < derivesCedes.size(); i++){

               // on récupère le dérivé, son emplacement et la
               // boite
               final ProdDerive derive = ManagerLocator.getProdDeriveManager().findByIdManager(derivesCedes.get(i).getObjetId());
               final Emplacement emp = ManagerLocator.getProdDeriveManager().getEmplacementManager(derive);
               final Terminale term = emp.getTerminale();

               final BoiteImpression newBi = new BoiteImpression();
               newBi.setBoite(term);

               // titre intermédiaire
               if(i == 0){
                  newBi.setTitreIntermediaire(Labels.getLabel("impression.boite.prodDerive"));
               }

               // si c'est la 1ère fois que l'on rencontre cette boite
               if(!listeBoites.contains(newBi)){
                  // on remplit tous titres et légendes
                  newBi.setTitreModelisation(Labels.getLabel("impression.boite.title.visualisation"));
                  newBi.setTitreInstructions(Labels.getLabel("impression.boite.title.instructions"));
                  newBi.setNom(ObjectTypesFormatters.getLabel("impression.boite.nom", new String[] {term.getNom()}));
                  newBi.setTitreListe(Labels.getLabel("impression.boite.elements" + ".title.destockage.prodDerives"));
                  newBi.setLegendeVide(Labels.getLabel("impression.boite.legende.vide"));
                  newBi.setLegendePris(Labels.getLabel("impression.boite.legende.pris"));
                  newBi
                     .setLegendeSelectionne(Labels.getLabel("impression.boite.legende.selectionne" + ".destockage.prodDerives"));

                  // création des intructions pour récupérer la boite
                  // Récup des parents de la boite
                  final List<Object> parents = ManagerLocator.getTerminaleManager().getListOfParentsManager(term);
                  final List<String> instructions = new ArrayList<>();
                  // pour chaque parent
                  for(int j = 0; j < parents.size(); j++){
                     if(parents.get(j).getClass().getSimpleName().equals("Conteneur")){
                        final Conteneur c = (Conteneur) parents.get(j);
                        instructions.add(ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.conteneur",
                           new String[] {c.getCode()}));
                     }else if(parents.get(j).getClass().getSimpleName().equals("Enceinte")){
                        final Enceinte e = (Enceinte) parents.get(j);
                        instructions.add(ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.enceinte",
                           new String[] {e.getNom()}));
                     }
                  }
                  // ajout des instructions à la boite
                  instructions
                     .add(ObjectTypesFormatters.getLabel("impression.boite.instruction.terminale", new String[] {term.getNom()}));
                  instructions.add(Labels.getLabel("impression.boite.instruction" + ".destockage.prodDerives"));
                  newBi.setInstructions(instructions);

                  // ajout du dérivé à la liste des éléments
                  // a extraire
                  final List<String> elements = new ArrayList<>();
                  elements.add(
                     ObjectTypesFormatters.getLabel("impression.boite.numero.prodDerive", new String[] {"1", derive.getCode()}));
                  newBi.setElements(elements);

                  // ajout de la position du dérivé
                  final List<Integer> positions = new ArrayList<>();
                  positions.add(emp.getPosition());
                  newBi.setPositions(positions);

                  // ajout de la boite à la liste
                  listeBoites.add(newBi);

               }else{
                  // sinon on récupère la boite dans la liste
                  final BoiteImpression bi = listeBoites.get(listeBoites.indexOf(newBi));

                  // ajout du dérivé et de sa position aux
                  // éléments à extraire
                  final int pos = bi.getPositions().size() + 1;
                  bi.getPositions().add(emp.getPosition());
                  bi.getElements().add(ObjectTypesFormatters.getLabel("impression.boite.numero.prodDerive",
                     new String[] {Integer.toString(pos), derive.getCode()}));
               }
            }

            final StringBuffer adrImages = new StringBuffer();
            adrImages.append(((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getContextPath());
            adrImages.append("/images/icones/emplacements/");
            for(int i = 0; i < listeBoites.size(); i++){
               ManagerLocator.getXmlUtils().addBoite(page, listeBoites.get(i), adrImages.toString());
            }
         }
      }

      return doc;
   }

}
