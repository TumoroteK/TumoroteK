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


import fr.aphp.tumorotek.manager.impl.io.production.DocumentWithDataAsTableExcelProducer;
import fr.aphp.tumorotek.manager.io.production.DocumentProducer;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;


/**
 * La classe  étend {@link AbstractPlanCongelateurSansBoiteGenerator}  et est responsable de la génération d'un plan
 * pour un congélateur sans boîte au format Excel.
 *
 * <p>Elle utilise un producteur de documents {@link DocumentWithDataAsTableExcelProducer} pour créer le fichier
 * Excel à partir des données fournies. La méthode {@code buildFileName} génère le nom de fichier pour le document
 * Excel basé sur une liste de conteneurs.</p>
 */
public class PlanCongelateurSansBoiteExcelGenerator extends AbstractPlanCongelateurSansBoiteGenerator {
   //3e implémentation (CHT)
   private EnceinteManager enceinteManager;
   //CHT : définir le documentProducer avec le type concret permet de mettre en évidence que c'est un producer excel
   //Par contre, ça nécessite de définir un getter nommé getDocumentWithDataAsTableExcelProducer pour que l'injection de dépendance marche
   //La méthode getDocumentProducer() nécessaire pour le polymorphisme fera donc juste un appel au getter getDocumentWithDataAsTableExcelProducer
   private DocumentWithDataAsTableExcelProducer documentWithDataAsTableExcelProducer;

   public PlanCongelateurSansBoiteExcelGenerator() {}

   public void setEnceinteManager(EnceinteManager enceinteManager){
      this.enceinteManager = enceinteManager;
    }
   
   public void setDocumentWithDataAsTableExcelProducer(DocumentWithDataAsTableExcelProducer documentWithDataAsTableExcelProducer){
       this.documentWithDataAsTableExcelProducer = documentWithDataAsTableExcelProducer;
   }

   @Override
   protected EnceinteManager getEnceinteManager() {
       if (this.enceinteManager == null) {
           throw new IllegalStateException("EnceinteManager has not been initialized.");
       }
       return this.enceinteManager;
   }


   protected DocumentWithDataAsTableExcelProducer getDocumentWithDataAsTableExcelProducer() {
       if (this.documentWithDataAsTableExcelProducer == null) {
           throw new IllegalStateException("documentWithDataAsTableExcelProducer has not been initialized.");
       }
       return this.documentWithDataAsTableExcelProducer;
   }

   @Override
   protected DocumentProducer getDocumentProducer() {
       return getDocumentWithDataAsTableExcelProducer();
   }

   
   
//   //1ere implémentation (Pini)
//   private EnceinteManager enceinteManager;
//
//   private DocumentWithDataAsTableExcelProducer documentWithDataAsTableExcelProducer;
//
//
//
//   public PlanCongelateurSansBoiteExcelGenerator(EnceinteManager enceinteManager, DocumentWithDataAsTableExcelProducer documentWithDataAsTableExcelProducer) {
//       this.enceinteManager = enceinteManager;
//       this.documentWithDataAsTableExcelProducer = documentWithDataAsTableExcelProducer;
//   }
//
//
//   @Override
//   protected EnceinteManager getEnceinteManager() {
//       if (this.enceinteManager == null) {
//           throw new IllegalStateException("EnceinteManager has not been initialized.");
//       }
//       return this.enceinteManager;
//   }
//
//
//   @Override
//   protected DocumentWithDataAsTableExcelProducer getDocumenProducer() {
//       if (this.documentWithDataAsTableExcelProducer == null) {
//           throw new IllegalStateException("documentWithDataAsTableExcelProducer has not been initialized.");
//       }
//       return this.documentWithDataAsTableExcelProducer;
//   }

   
   //2e implémentation (CHT)
   /*
   private EnceinteManager enceinteManager;
   //CHT : renommage
   private DocumentProducer documentProducer;



   public PlanCongelateurSansBoiteExcelGenerator() {}

   public void setEnceinteManager(EnceinteManager enceinteManager){
     this.enceinteManager = enceinteManager;
   }
   public void setDocumentProducer(DocumentProducer documentWithDataAsTableExcelProducer){
      this.documentProducer = documentWithDataAsTableExcelProducer;
   }
  
   //CHT : à supprimer car utilisé que pour les tests. Il faut passer par les setters qui manquent (indispensable pour l'injection de dépendance)
   public PlanCongelateurSansBoiteExcelGenerator(EnceinteManager enceinteManager, DocumentWithDataAsTableExcelProducer documentWithDataAsTableExcelProducer) {
       this.enceinteManager = enceinteManager;
       this.documentProducer = documentWithDataAsTableExcelProducer;
   }


   @Override
   protected EnceinteManager getEnceinteManager() {
       if (this.enceinteManager == null) {
           throw new IllegalStateException("EnceinteManager has not been initialized.");
       }
       return this.enceinteManager;
   }



   @Override
   protected DocumentProducer getDocumentProducer() {
       if (this.documentProducer == null) {
           throw new IllegalStateException("documentWithDataAsTableExcelProducer has not been initialized.");
       }
       return this.documentProducer;
   }
   */
}