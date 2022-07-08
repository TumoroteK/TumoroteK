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
package fr.aphp.tumorotek.webapp.tree.export;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.utils.ChampUtils;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.io.ChampDelegueManager;
import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.AbstractTKChamp;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.tree.TumoTreeNode;

/**
 * Classe représentant un noeud entité d'un arbre contenant les
 * champs entités ou annotations.
 * Classe créée le 07/04/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.3.0-gatsbi
 *
 */
public class EntiteNode extends TumoTreeNode
{

   private Entite entite;

   private List<Champ> oldSelectedChamps = new ArrayList<>();

   /**
    * Liste des dataTypes à afficher
    */
   private List<DataType> dataTypeList;

   /**
    * Exclure les champs numérique de type Id
    */
   private Boolean excludeIds;

   private Banque banque;

   public EntiteNode(final Entite e){
      this.entite = e;
   }

   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite e){
      this.entite = e;
   }

   /**
    * Recherche tous les champs (entité, annotation et délégué) d'une entite node
    */
   @Override
   public void readChildren(){

      children = new ArrayList<>();

      final Predicate<AbstractTKChamp> dataTypeAutorise =
         tkChamp -> dataTypeList == null || dataTypeList.contains(tkChamp.getDataType());
      final Predicate<Champ> notSelected = champ -> !oldSelectedChamps.contains(champ);

      final Stream<ChampEntite> virtualChampsStream = getVirtualChampEntite().stream();

      // @since gatsbi
      // final Stream<ChampEntite> champsEntiteStream =
      //   ManagerLocator.getManager(ChampEntiteManager.class).findByEntiteAndImportManager(entite, true).stream();
      final Stream<ChampEntite> champsEntiteStream =
         GatsbiController.findByEntiteImportAndIsNullableManager(entite, true, null).stream();

      //Ajout des noeuds "champ entité"
      Stream.concat(virtualChampsStream, champsEntiteStream).filter(dataTypeAutorise)
         .filter(champEntite -> excludeIds == null || !excludeIds || !champEntite.getNom().endsWith("Id")).map(Champ::new)
         .filter(notSelected).map(ChampNode::new).forEach(children::add);

      //Ajout des noeuds "champ annotation"
      ManagerLocator.getManager(TableAnnotationManager.class).findByEntiteAndBanqueManager(entite, banque).stream()
         .map(table -> ManagerLocator.getManager(ChampAnnotationManager.class).findByTableManager(table))
         .flatMap(listChampsFromTable -> listChampsFromTable.stream()).filter(dataTypeAutorise).map(Champ::new)
         .filter(notSelected).map(ChampNode::new).forEach(children::add);

      //Ajout des noeuds "champ délégué"
      ManagerLocator.getManager(ChampDelegueManager.class).findByEntiteAndContexte(entite, SessionUtils.getCurrentContexte())
         .stream().filter(dataTypeAutorise).map(Champ::new).filter(notSelected).map(ChampNode::new).forEach(children::add);

      //Tri de la liste
      children.sort(Comparator.comparing(node -> ChampUtils.getChampNom(((ChampNode) node).getChamp()).toLowerCase()));

   }

   /**
    * Retourne la liste des champs d'entite virtuels en fonction de
    * l'entite.
    * @param champsEntite
    */
   private List<ChampEntite> getVirtualChampEntite(){

      final List<ChampEntite> virtualChampEntiteList = new ArrayList<>();
      ChampEntite ce = null;

      //On utilise directement les id car il peut potentiellement exister plusieurs ChampEntite de même nom
      //pour une même entité. TODO Ajouter une contrainte d'unicité sur nom + entité pour les champs entité ?
      switch(entite.getNom()){
         case "Prelevement":
            ce = ManagerLocator.getManager(ChampEntiteManager.class).findByIdManager(193);
            if((null == excludeIds || !excludeIds)
               && (null == dataTypeList || (null != dataTypeList && dataTypeList.contains(ce.getDataType())))
               // since 2.3.0-gatsbi etablissement depend de service preleveur
               && (SessionUtils.getCurrentGatsbiContexteForEntiteId(entite.getEntiteId()) == null
                  || SessionUtils.getCurrentGatsbiContexteForEntiteId(entite.getEntiteId()).isChampIdVisible(29))){
               virtualChampEntiteList.add(ce);
            }
            break;
         case "Echantillon":
            ce = ManagerLocator.getChampEntiteManager().findByIdManager(265);
            if(null == dataTypeList || (null != dataTypeList && dataTypeList.contains(ce.getDataType()))){
               virtualChampEntiteList.add(ce);
            }
            break;
         case "ProdDerive":
            ce = ManagerLocator.getChampEntiteManager().findByIdManager(266);
            if(null == dataTypeList || (null != dataTypeList && dataTypeList.contains(ce.getDataType()))){
               virtualChampEntiteList.add(ce);
            }
            break;
      }

      return virtualChampEntiteList;

   }

   /**
    * Ce n'est pas une feuille.
    */
   @Override
   public boolean isLeaf(){

      return false;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final EntiteNode node = (EntiteNode) obj;
      return this.getEntite().equals(node.getEntite());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashEntite = 0;

      if(this.entite != null){
         hashEntite = this.entite.hashCode();
      }

      hash = 7 * hash + hashEntite;

      return hash;
   }

   public List<Champ> getOldSelectedChamps(){
      return oldSelectedChamps;
   }

   public void setOldSelectedChamps(final List<Champ> oldSelected){
      this.oldSelectedChamps = oldSelected;
   }

   /**
    * Liste des dataTypes à afficher
    * @return Liste des dataTypes à afficher
    */
   public List<DataType> getDataTypeList(){
      return dataTypeList;
   }

   /**
    * Liste des dataTypes à afficher
    * @param dataTypeList Liste des dataTypes à afficher
    */
   public void setDataTypeList(final List<DataType> dataTypeList){
      this.dataTypeList = dataTypeList;
   }

   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }

   /**
    * Exclure les champs numérique de type Id
    * @return Exclure les champs numérique de type Id
    */
   public Boolean getExcludeIds(){
      return excludeIds;
   }

   /**
    * Exclure les champs numérique de type Id
    * @param excludeIds Exclure les champs numérique de type Id
    */
   public void setExcludeIds(final Boolean excludeIds){
      this.excludeIds = excludeIds;
   }

}
