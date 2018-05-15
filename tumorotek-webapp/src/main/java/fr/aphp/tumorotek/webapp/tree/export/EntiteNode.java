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
import java.util.Arrays;
import java.util.List;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.tree.TumoTreeNode;

/**
 * Classe représentant un noeud entité d'un arbre contenant les
 * champs entités ou annotations.
 * Classe créée le 07/04/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0.
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
    * Recherche tous les champsEntites et champsAnnotations d'une entite node
    */
   @Override
   public void readChildren(){
      children = new ArrayList<>();
      List<ChampEntite> champsEntite = new ArrayList<>();
      // On récupère les champs pouvant etre affichés
      if(null == dataTypeList){
         champsEntite = ManagerLocator.getChampEntiteManager().findByEntiteAndImportManager(entite, true);
      }else if(!dataTypeList.isEmpty()){
         if(excludeIds){
         champsEntite =
            ManagerLocator.getChampEntiteManager().findByEntiteAndImportManagerAndDatatype(entite, true, dataTypeList, excludeIds);
         }else{
            ManagerLocator.getChampEntiteManager().findByEntiteAndImportManagerAndDatatype(entite, true, dataTypeList);
         }
      }

      addVirtualChampEntite(champsEntite);
      //Collections.sort(champsEntite);

      for(int i = 0; i < champsEntite.size(); i++){
         final Champ chp = new Champ();
         // pour chaque champ, on vérifie qu'il n'est pas
         // déjà sélectionné
         chp.setChampEntite(champsEntite.get(i));
         if(!oldSelectedChamps.contains(chp)){
            final ChampNode node = new ChampNode(chp);
            // recherche anonymisée.
            children.add(node);
         }
      }

      // Récupération des annotations disponibles
      final List<TableAnnotation> tas = ManagerLocator.getTableAnnotationManager().findByEntiteAndBanqueManager(entite, banque);
      final List<ChampAnnotation> champsAnnotations = new ArrayList<>();
      for(int i = 0; i < tas.size(); i++){
         if(null == dataTypeList){
            champsAnnotations.addAll(ManagerLocator.getChampAnnotationManager().findByTableManager(tas.get(i)));
         }else if(!dataTypeList.isEmpty()){
            champsAnnotations
            .addAll(ManagerLocator.getChampAnnotationManager().findByTableAndDataTypeManager(tas.get(i), dataTypeList));
            
            DataType calcule = ManagerLocator.getDataTypeManager().findByTypeManager("calcule");
            List<ChampAnnotation> champAnnoCalculeList = ManagerLocator.getChampAnnotationManager().findByTableAndDataTypeManager(tas.get(i), Arrays.asList(calcule));
            for(ChampAnnotation champAnnoCalcule : champAnnoCalculeList){
               if(dataTypeList.contains(champAnnoCalcule.getChampCalcule().getDataType())){
                  champsAnnotations.add(champAnnoCalcule);
               }
            }
         }
      }
      for(int i = 0; i < champsAnnotations.size(); i++){
         final Champ chp = new Champ();
         chp.setChampAnnotation(champsAnnotations.get(i));
         // pour chaque annotation, on vérifie qu'il n'est pas
         // déjà sélectionné
         if(!oldSelectedChamps.contains(chp)){
            final ChampNode node = new ChampNode(chp);
            children.add(node);
         }
      }
   }

   /**
    * ??
    * Ajoute des champs d'entite virtuels en fonction de 
    * l'entite.
    * @param champsEntite
    */
   private void addVirtualChampEntite(final List<ChampEntite> champsEntite){
      if(entite.getNom().equals("Prelevement")){
         // Etablissement preleveur
         ChampEntite ce = ManagerLocator.getChampEntiteManager().findByIdManager(193); //TODO Ids en dur ?!
         if((null == excludeIds || !excludeIds) && (null == dataTypeList || (null != dataTypeList && dataTypeList.contains(ce.getDataType())))){
            champsEntite.add(ManagerLocator.getChampEntiteManager().findByIdManager(193)); //TODO Ids en dur ?!
         }
      }else if(entite.getNom().equals("Echantillon")){
         //TempStock
         ChampEntite ce = ManagerLocator.getChampEntiteManager().findByIdManager(265); //TODO Ids en dur ?!
         if(null == dataTypeList || (null != dataTypeList && dataTypeList.contains(ce.getDataType()))){
            champsEntite.add(ManagerLocator.getChampEntiteManager().findByIdManager(265)); //TODO Ids en dur ?!
         }
      }else if(entite.getNom().equals("ProdDerive")){
         //TempStock
         ChampEntite ce = ManagerLocator.getChampEntiteManager().findByIdManager(266); //TODO Ids en dur ?! 
         if(null == dataTypeList || (null != dataTypeList && dataTypeList.contains(ce.getDataType()))){
            champsEntite.add(ManagerLocator.getChampEntiteManager().findByIdManager(266)); //TODO Ids en dur ?! 
         }
      }
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
   public void setExcludeIds(Boolean excludeIds){
      this.excludeIds = excludeIds;
   }
   
   

}
