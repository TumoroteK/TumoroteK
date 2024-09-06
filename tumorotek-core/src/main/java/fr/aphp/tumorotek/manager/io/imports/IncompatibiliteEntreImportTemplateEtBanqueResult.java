/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
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
package fr.aphp.tumorotek.manager.io.imports;

import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.io.export.ChampDelegue;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.utils.ItemForErrorResult;
/**
 * TK-537 : classe qui permet de retourner le resultat de la validation de la compatibilité entre un modèle d'import et une banque
 * {@link fr.aphp.tumorotek.manager.io.imports.CompatibiliteEntreImportTemplateEtBanqueValidator}
 * @author chuet
 *
 */
public class IncompatibiliteEntreImportTemplateEtBanqueResult
{
   //NB : pour le cas des champs d'annotation, l'info à remonter à l'utilisateur est du niveau table d'annotation et non champ d'annotation
   // => on retourne un ItemForErrorResult<TableAnnotation>
   //Toutefois, on peut avoir besoin des importColonne liées aux annotations dans le cas de la création ensuite d'un nouveau modèle à partir de celui-ci
   // => on retourne en plus les importColonne associés
   //Par contre, pour les champs délégués et les champs standard, c'est bien l'info au niveau champ et donc au niveau de la colonne qui sera retournée
   private ItemForErrorResult<TableAnnotation> tableAnnotationItem = null;
   private List<ImportColonne> listImportColonneDesChampsAnnotationKO = null;

   //Champs délégués présents dans le modèle mais incompatibles avec la banque (ex : modèle séro, champ protocoles pour une banque anapat')
   private ItemForErrorResult<ImportColonne> champDelegueItem = null;
   //Champs présents dans le modèle mais incompatibles avec la banque 
   // cas 1 : modèle anapat' avec code lésionnel pour une banque séro 
   // cas 2 : modèle anapat' ou séro pour une banque Gatsbi
   // cas 3 : modèle Gatsbi pour une banque Gatsbi avec d'autres contextes
   private ItemForErrorResult<ImportColonne> champItem = null;
   
   //contient les champs qui sont bien présents dans le modèle mais qui ne sont pas obligatoire alors qu'il est obligatoire
   //pour la banque

   //Résultat du contrôle sur la notion champ obligatoire :
   //cet item est non null si des champs obligatoires de la banque sont absents du modèle ou présents mais non obligatoires
   //Pour ne pas compliquer l'affichage du message, il sera générique et on affichera uniquement le nom des entités concernées
   private ItemForErrorResult<Entite> obligatoireItem = null;
   //Lors de la création d'un nouveau modèle à partir de celui-ci, il faudra ajouter les champs obligatoire dans la banque mais
   //non présents dans le modèle. Ce champ permet de les récupérer
   private List<ChampEntite> listChampObligatoirePourBanqueMaisNonPresentDansModele = null;

   public IncompatibiliteEntreImportTemplateEtBanqueResult(
         ItemForErrorResult<TableAnnotation> tableAnnotationItem, 
         List<ImportColonne> listImportColonneDesChampsAnnotationKO,
         ItemForErrorResult<ImportColonne> champDelegueItem, 
         ItemForErrorResult<ImportColonne> champGatbsiItem,
         ItemForErrorResult<Entite> obligatoireItem,
         List<ChampEntite> listChampObligatoirePourBanqueMaisNonPresentDansModele) {
      this.tableAnnotationItem = tableAnnotationItem;
      this.listImportColonneDesChampsAnnotationKO = listImportColonneDesChampsAnnotationKO;
      this.champDelegueItem = champDelegueItem;
      this.champItem = champGatbsiItem;
      this.obligatoireItem = obligatoireItem;
      this.listChampObligatoirePourBanqueMaisNonPresentDansModele = listChampObligatoirePourBanqueMaisNonPresentDansModele;
   }
   
   public ItemForErrorResult<TableAnnotation> getTableAnnotationItem(){
      return tableAnnotationItem;
   }
   
   public List<ImportColonne> getListImportColonneDesChampsAnnotationKO(){
      return listImportColonneDesChampsAnnotationKO;
   }
   
   public ItemForErrorResult<ImportColonne> getChampDelegueItem(){
      return champDelegueItem;
   }

   public ItemForErrorResult<ImportColonne> getChampItem(){
      return champItem;
   }
  
   public ItemForErrorResult<Entite> getObligatoireItem(){
      return obligatoireItem;
   }

   public List<ChampEntite> getListChampObligatoirePourBanqueMaisNonPresentDansModele(){
      return listChampObligatoirePourBanqueMaisNonPresentDansModele;
   }
   
   //Concaténation de toutes les colonnes qui constituent une incompatibilité entre le modèle et la banque
   //Les éléments sont récupérés des ItemForErrorResult ou des listes dédiées selon les types de champs et d'incompatibilité
   //Ils seront exclus lors de l'initialisation du nouveau modèle à créer à partir de celui-ci
   public List<ImportColonne> retrieveAllImportColonnesKO() {
      List<ImportColonne> allImportColonnesKO = new ArrayList<ImportColonne>();
      if(getListImportColonneDesChampsAnnotationKO() != null) {
         allImportColonnesKO.addAll(getListImportColonneDesChampsAnnotationKO());
      }
      if(getChampDelegueItem() != null) {
         allImportColonnesKO.addAll(getChampDelegueItem().getListDataInError());
      }
      if(getChampItem() != null) {
         allImportColonnesKO.addAll(getChampItem().getListDataInError());
      }
      
      return allImportColonnesKO;
   }
}
