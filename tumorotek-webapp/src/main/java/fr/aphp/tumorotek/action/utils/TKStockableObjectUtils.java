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

import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.qualite.NonConformite;

/**
 * Utility class fournissant les methodes récupérant et formattant les valeurs
 * de objets stockables Echantillon et Dérivé pour un
 * affichage particulier dans l'interface.
 * Date: 16/11/2012.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.9
 */
public final class TKStockableObjectUtils
{

   private TKStockableObjectUtils(){}

   /**
    * Dessine la liste d'icône en tête d'une liste d'objets Echantillon
    * ou dérivés
    * @param TKStockable objet
    * @return Hlayout contenant les icônes
    * @since 2.0.9
    */
   public static Hlayout drawListIcones(final TKStockableObject obj, final List<NonConformite> ncfsTrait,
      final List<NonConformite> ncfsCess){

      final Hlayout icones = new Hlayout();

      // impact qualité
      if(obj != null && !ManagerLocator.getRetourManager().findByObjectAndImpactManager(obj, true).isEmpty()){
         final Div imp = new Div();
         imp.setWidth("18px");
         imp.setHeight("18px");
         imp.setSclass("impact");
         imp.setParent(icones);
         imp.setTooltiptext(Labels.getLabel("Champ.Retour.Impact"));
      }

      // non conformité apres traitement
      if(obj != null && obj.getConformeTraitement() != null){
         final Div nonConf = new Div();
         nonConf.setWidth("18px");
         nonConf.setHeight("18px");
         if(obj.getConformeTraitement()){
            nonConf.setSclass("conformeTraitement");
            nonConf.setTooltiptext(Labels.getLabel("tooltip.conforme.traitement"));
         }else{
            String noconfsTrait = ": ";
            Iterator<NonConformite> ncIt;
            if(((TKdataObject) obj).listableObjectId() != null){
               ncIt = ManagerLocator.getNonConformiteManager().getFromObjetNonConformes(
                  ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(obj, "Traitement")).iterator();
            }else{
               ncIt = ncfsTrait.iterator();
            }
            while(ncIt.hasNext()){
               noconfsTrait = noconfsTrait + ncIt.next().getNom();
               if(ncIt.hasNext()){
                  noconfsTrait = noconfsTrait + ", ";
               }
            }
            nonConf.setSclass("nonConformeTraitement");
            nonConf.setTooltiptext(ObjectTypesFormatters.getLabel("tooltip.nonconforme.traitement", new String[] {noconfsTrait}));
         }
         nonConf.setParent(icones);
      }

      // non conformité pour la cession
      if(obj != null && obj.getConformeCession() != null){
         final Div nonConf = new Div();
         nonConf.setWidth("18px");
         nonConf.setHeight("18px");
         if(obj.getConformeCession()){
            nonConf.setSclass("conformeCession");
            nonConf.setTooltiptext(Labels.getLabel("tooltip.conforme.cession"));
         }else{
            String noconfsCess = ": ";
            Iterator<NonConformite> ncIt;
            if(((TKdataObject) obj).listableObjectId() != null){
               ncIt = ManagerLocator.getNonConformiteManager()
                  .getFromObjetNonConformes(ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(obj, "Cession"))
                  .iterator();
            }else{
               ncIt = ncfsCess.iterator();
            }

            while(ncIt.hasNext()){
               noconfsCess = noconfsCess + ncIt.next().getNom();
               if(ncIt.hasNext()){
                  noconfsCess = noconfsCess + ", ";
               }
            }
            nonConf.setSclass("nonConformeCession");
            nonConf.setTooltiptext(ObjectTypesFormatters.getLabel("tooltip.nonconforme.cession", new String[] {noconfsCess}));
         }
         nonConf.setParent(icones);
      }
      return icones;
   }
}
