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
package fr.aphp.tumorotek.action.imports.strategy;

import java.util.List;

import fr.aphp.tumorotek.action.imports.ImportColonneDecorator;
import fr.aphp.tumorotek.decorator.EntiteDecoratorForOneToManyComponent;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.EImportTemplateType;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Pattern strategy utilisé pour gérer les différents cas de modèle d'import :
 *    - création de plusieurs objets en une fois
 *    - création d'un dérivé lié à un parent
 *    - mise à jour des annotations d'un objet 
 *    ...
 * cette interface permet de définir les règles de gestion différentes selon les cas
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public interface ImportTemplateStrategy
{
   /**
    * retourne l' eImportTemplateType associé à la stratégie
    * @return
    */
   EImportTemplateType getEImportTemplateType();
   
   /**
    * retourne la clé pour le libellé internationalisé à afficher en titre
    * @return
    */
   String defineKeyI18nForTitle();
  
   /**
    * retourne la liste des entités à proposer dans le modèle
    * @return
    */
   List<EntiteDecoratorForOneToManyComponent> defineAddableEntites();
   
   /**
    * retourne true si il faut afficher le bloc "parent du dérivé" dans le zul qui est commun à tous les modèles. 
    * cas spécifique au modèle de création d'un produit dérivé avec un parent
    * @return
    */
   boolean isBlocDeriveParentVisible();
   
   /**
    * permet de définir un traitement spécifique lors de l'initialisation d'un nouveau modèle
    * utilisé notamment pour le modèle "produit dérivé avec parent" pour notamment forcer l'utilisation de l'entité "Produit dérivé", supprimer les 3 colonnes liées au parent qui ne sont pas stockées en base de données
    * sera généralement laissé vide. Les paramètres correspondent aux besoins actuels pour le traitement du produit dérivé...
    * @param entitesAssociees
    * @param importTemplate
    * @param importColonnesDecorator liste de toutes colonnes présentes dans l'affichage du modèle lors de sa définition par l'utilisateur.
    * @param banque
    */
   void doActionsSpecifiquesForInitNewObject(List<EntiteDecoratorForOneToManyComponent> entitesAssociees, ImportTemplate importTemplate, List<ImportColonneDecorator> importColonnesDecorator, Banque banque);
   
   /**
    * permet de définir un traitement spécifique avant de créer le nouveau modèle en base de données
    * utilisé notamment pour le modèle "produit dérivé avec parent" pour supprimer les 3 colonnes liées au parent qui ne sont pas stockés en base de données
    * @param importColonnesDecorator liste de toutes colonnes présentes dans l'affichage du modèle lors de sa définition par l'utilisateur.
    */
   void doActionsSpecifiquesBeforeCreatingOrUpdatingObject(List<ImportColonneDecorator> importColonnesDecorator);   
   
   /**
    * si retourne true, il ne faut pas passer en mode édition le composant entitesAssocieesImport lors du passage en mode édition de l'objet "parent"
    * exemple : pour le modèle pour les dérivés avec un parent : l'utilisateur ne peut pas choisir l'entité associée
    * @return
    */
   boolean forbidEditableModeForEntitesAssociees();
   
   /**
    * récupère les colonnes définies dans un modèle d'import existant et crée les objets ImportColonneDecorator associé 
    * (en valorisant précisement ses attributs). 
    * @param importTemplate
    * @return
    */
   List<ImportColonneDecorator> defineColonnesDecoratorForImportTemplate(ImportTemplate importTemplate);
   
   /**
    * récupère tous les champs pouvant être importés pour l'entité et la banque passées en paramètre dans le cadre du type d'import courant
    * cette méthode ne fait pas la distinction "nullable" ou pas. Ce sera à l'appelant de filtrer si besoin ceux qu'il ne veut pas dans le contexte d'appel
    * @param entite
    * @param banque
    * @return
    */
   List<Champ> retrieveAllForEntite(Entite entite, Banque banque);
   
   
   /**
    * retourne tous les champs obligatoires pour une entité donnée
    * @param entite
    * @param banque, utilisée dans le cas d'un contexte Gatsbi pour récupérer le contexte qui porte l'information "champ obligatoire ou non"
    * @return
    */
   List<ChampEntite> retrieveAllChampObligatoire(Entite entite, Banque banque);
   
   /**
    * crée une liste de ImportColonneDecorator à partir de la liste de ImportColonne passée en paramètre
    * @param listImportColonne
    * @param banque, utile dans le cas d'un contexte Gatsbi pour récupérer les champs obligatoires et donc non supprimables
    * @return
    */
   List<ImportColonneDecorator> decorateImportColonnes(List<ImportColonne> listImportColonne, Banque banque);
   
   /**
    * définit le nombre max d'entités sélectionnables. Utile notamment quand une seule entité peut être sélectionnée
    * @return
    */
   public Integer defineNbMaxEntiteSelectionnable();
   
   /**
    * retourne le nombre de lignes du modèle qui sont fixes. Est utilisé pour limiter le déplacement des colonnes vers le haut lors de la définition de l'ordre
    * @return 
    */
   public Integer defineNbLigneEnteteFixe();
   
   /**
    * retourne le nom de la méthode à appeler dans FicheImportTemplate pour réaliser l'import : par défaut onLaterImport
    * @return
    */
   public String defineNomMethodePourExecuterImport();
   
   /**
    * return l'EContexte attaché à la strategy
    * @return
    */
   public EContexte getEContexte();
}
