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
package fr.aphp.tumorotek.model.stats;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Objet de transfert des données agrégés permettant de dessiner les
 * graphes du tableau de bord.
 * Classe créée le 19/12/13.
 *
 * @author Marc DESCHAMPS
 * @version 2.0.10
 *
 */
public class GraphesModele
{

   private Boolean stacked = false;

   public GraphesModele(){}

   public GraphesModele(final ResultSet rset){
      mapFromResultSet(rset);
   }

   private Map<String, List<Integer>> countsMap = new LinkedHashMap<>();

   public Map<String, List<Integer>> getCountsMap(){
      return countsMap;
   }

   public void setCountMap(final Map<String, List<Integer>> e){
      this.countsMap = e;
   }

   /**
    * Transforme le resulset en Map clef-valeurs.
    * Clef = catégorie
    * Valeur = compte 
    * En cas d'erreur, vide le map
    * @param rs ResultSet
    * @param map
    */
   private void mapFromResultSet(final ResultSet rs){
      if(rs != null){
         try{
            final ResultSetMetaData rsmd = rs.getMetaData();
            final int columnsNumber = rsmd.getColumnCount();
            stacked = columnsNumber > 2;
            List<Integer> counts;
            if(columnsNumber > 1){
               while(rs.next()){
                  counts = new ArrayList<>();
                  if(rs.getString(1) != null){
                     for(int i = 2; i <= columnsNumber; i++){
                        counts.add(rs.getInt(i));
                     }
                     getCountsMap().put(rs.getString(1), counts);

                  }
                  //permet de ne pas prendre en compte les données à 0
                  //						if (rs.getString(2) == null) {
                  //							//permet de ne pas provoquer d'erreur à cause des null potentiels(nom non renseignés)
                  //							getCountsMap().put("n/a", rs.getInt(1));
                  //						} else {
                  //							getCountsMap().put(rs.getString(1), rs.getInt(é));
                  //						}
                  //					}
               }
            }
         }catch(final SQLException sqle){
            getCountsMap().clear();
         }
      }
   }

   public Boolean getStacked(){
      return stacked;
   }

   public void setStacked(final Boolean stacked){
      this.stacked = stacked;
   }

}
