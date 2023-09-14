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
package fr.aphp.tumorotek.action.recherche.historique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.recherche.RechercheCompValues;

/**
 *
 * Classe caractérisant l'historique de l'exécution d'une recherche. Les informations collectée sont
 * la date d'exécution, le nombre de résultat ainsi que les champs de formulaire employés et les valeurs
 * saisies.
 *
 * @author Julien HUSSON
 * @version 2.0.10.1
 *
 */
public class SearchHistory implements Serializable
{

   private static final long serialVersionUID = 1L;

   private static final String GREATER_THAN = " > ";

   private static final String SPACE = " ";

   private List<RechercheCompValues> listSearchHistoryComponent;

   private List<RechercheCompValues> listAnnotationsComponent;

   private String type;

   private int resultCount = 0;

   private String searchTime;

   private String info;

   List<String> openedGroupsIds = new ArrayList<>();

   public SearchHistory(){
      listSearchHistoryComponent = new ArrayList<>();
      listAnnotationsComponent = new ArrayList<>();
   }

   public String getType(){
      return type;
   }

   public void setType(final String type){
      this.type = type;
   }

   public int getResultCount(){
      return resultCount;
   }

   public void setResultCount(final int resultCount){
      this.resultCount = resultCount;
   }

   public String getSearchTime(){
      return searchTime;
   }

   public void setSearchTime(final String searchTime){
      this.searchTime = searchTime;
   }

   public void setInfo(final String time, final int count){
      setSearchTime(time);
      setResultCount(count);

      this.info = searchTime + GREATER_THAN + resultCount + SPACE + Labels.getLabel("recherche.avancee.historique.resultat");
   }

   public String getInfo(){
      return info;
   }

   public void setInfo(final String info){
      this.info = info;
   }

   public List<RechercheCompValues> getListSearchHistoryComponent(){
      return listSearchHistoryComponent;
   }

   public void setListSearchHistoryComponent(final List<RechercheCompValues> lshc){
      this.listSearchHistoryComponent = lshc;
   }

   public List<RechercheCompValues> getListAnnotationsComponent(){
      return listAnnotationsComponent;
   }

   public void setListAnnotationsComponent(final List<RechercheCompValues> lac){
      this.listAnnotationsComponent = lac;
   }

   public List<String> getOpenedGroupsIds(){
      return openedGroupsIds;
   }

   public void setOpenedGroupsIds(final List<String> ogi){
      this.openedGroupsIds = ogi;
   }
}
