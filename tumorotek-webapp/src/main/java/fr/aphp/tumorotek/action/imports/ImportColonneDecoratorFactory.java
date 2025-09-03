/**
 * Copyright ou © ou Copr. SESAN
 * projet-tk@sesan.fr
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

package fr.aphp.tumorotek.action.imports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;

/**
 * classe qui instance un ImportColonneDecorator selon les besoins
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportColonneDecoratorFactory
{
   
   private ImportColonneDecoratorFactory() {}
   
   /**
    * crée l'importColonneDecorator d'un ImportTemplate pour un champEntite
    * @param importTemplate modèle d'import concerné par l'importColonneDecorator à créer
    * @param champEntite à rattacher à l'importColonneDecorator à créer
    * @param eContexte le contexte de la collection du modèle d'import concerné pour gérer les particularités liées à Gatsbi
    * @param isPartOfCleFonctionnelle permet de définir les actions possibles sur cet importColonneDecorator.
    * @return
    */
   public static ImportColonneDecorator createForImportTemplateFromChampEntite(ImportTemplate importTemplate, ChampEntite champEntite, 
               EContexte eContexte, boolean isPartOfCleFonctionnelle) {
      if(importTemplate != null && champEntite != null) {
         final ImportColonne importColonne = new ImportColonne();
         importColonne.setImportTemplate(importTemplate);
         importColonne.setChamp(new Champ(champEntite));
         ImportColonneDecorator importColonneDecorator = new ImportColonneDecorator(importColonne, eContexte);
         importColonneDecorator.defineAsElementOfCleFonctionnelle();
         return importColonneDecorator;
      }
      
      return null;
      
   }
   
   public static ImportColonneDecorator decorateColonneObligatoire(ImportColonne importColonne, EContexte eContexte){
      ImportColonneDecorator importColonneDecorator = new ImportColonneDecorator(importColonne, eContexte);
      importColonneDecorator.setCanDelete(false);
      
      return importColonneDecorator;
   }
   

   public static ImportColonneDecorator decorateColonneObligatoireEtNonDeplacable(ImportColonne importColonne, EContexte eContexte){
      ImportColonneDecorator importColonneDecorator = decorateColonneObligatoire(importColonne, eContexte);
      importColonneDecorator.setCanMove(false);
      
      return importColonneDecorator;
   }


   public static ImportColonneDecorator decorateColonneObligatoireNonDeplacableAuLibelleNonEditable(ImportColonne importColonne, EContexte eContexte){
      ImportColonneDecorator importColonneDecorator = decorateColonneObligatoireEtNonDeplacable(importColonne, eContexte);
      importColonneDecorator.setDisableEditLabel(true);
      
      return importColonneDecorator;      
   }

   /**
    * Extrait les ImportColonne d'une liste de Decorator.
    *
    * @param ImportColonne
    * @return ImportColonne décorés.
    */
   public static List<ImportColonne> undecorateListe(final List<ImportColonneDecorator> cols){
      final List<ImportColonne> liste = new ArrayList<>();
      final Iterator<ImportColonneDecorator> it = cols.iterator();

      while(it.hasNext()){
         liste.add(it.next().getColonne());
      }
      return liste;
   }
}
