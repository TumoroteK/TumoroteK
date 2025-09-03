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
package fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.entitestrategy;

import fr.aphp.tumorotek.manager.io.imports.modification.champannotation.ImportChampAnnotationEntiteStrategy;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.systeme.EEntiteId;

public class ImportChampAnnotationEntiteStrategyFactory
{
   private ImportChampAnnotationPatientStrategy importChampAnnotationPatientStrategy;
   private ImportChampAnnotationPatientGatsbiStrategy importChampAnnotationPatientGatsbiStrategy;
   private ImportChampAnnotationPrelevementStrategy importChampAnnotationPrelevementStrategy;
   private ImportChampAnnotationEchantillonStrategy importChampAnnotationEchantillonStrategy;
   private ImportChampAnnotationProdDeriveStrategy importChampAnnotationProdDeriveStrategy;

   
   public ImportChampAnnotationEntiteStrategy retrieveEntiteStrategy(EEntiteId eEntiteId, EContexte eContexte) {
      switch(eEntiteId){
         case PATIENT:
            if(eContexte == EContexte.GATSBI) {
               return importChampAnnotationPatientGatsbiStrategy;
            }
            return importChampAnnotationPatientStrategy;
         
         case PRELEVEMENT:
            return importChampAnnotationPrelevementStrategy;
   
         case ECHANTILLON:  
            return importChampAnnotationEchantillonStrategy;
            
         case PROD_DERIVE:   
            return importChampAnnotationProdDeriveStrategy;
            
         default:
            //c'est impossible de passer par ici vu que l'entité est tiré du modèle d'import. On renvoie donc juste une IllegalArgumentException
            throw new IllegalArgumentException(new StringBuilder("traitement d'import pour modifier des annotations impossible pour ce type d'entité : ").append(String.valueOf(eEntiteId.getId())).toString());
      }  
   }

   public void setImportChampAnnotationPatientStrategy(ImportChampAnnotationPatientStrategy importChampAnnotationPatientStrategy){
      this.importChampAnnotationPatientStrategy = importChampAnnotationPatientStrategy;
   }
   public void setImportChampAnnotationPatientGatsbiStrategy(
      ImportChampAnnotationPatientGatsbiStrategy importChampAnnotationPatientGatsbiStrategy){
      this.importChampAnnotationPatientGatsbiStrategy = importChampAnnotationPatientGatsbiStrategy;
   }
   public void setImportChampAnnotationPrelevementStrategy(ImportChampAnnotationPrelevementStrategy importChampAnnotationPrelevementStrategy){
      this.importChampAnnotationPrelevementStrategy = importChampAnnotationPrelevementStrategy;
   }
   public void
      setImportChampAnnotationEchantillonStrategy(ImportChampAnnotationEchantillonStrategy importChampAnnotationEchantillonStrategy){
      this.importChampAnnotationEchantillonStrategy = importChampAnnotationEchantillonStrategy;
   }
   public void
      setImportChampAnnotationProdDeriveStrategy(ImportChampAnnotationProdDeriveStrategy importChampAnnotationProdDeriveStrategy){
      this.importChampAnnotationProdDeriveStrategy = importChampAnnotationProdDeriveStrategy;
   }
}
