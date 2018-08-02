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
package fr.aphp.tumorotek.action.stats.im.model;

import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * Classe représentant une ligne pour l'affichage de la grid
 * de résultats du calcul des indicateurs
 * Date: 12/08/2015
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1.0
 *
 */

public class StatResultsRow
{

   private Integer subDivId;
   private String subDivNom;
   private Banque banque;
   // private Boolean isSelected = false;
   // private String calling;
   private final List<Number> values = new ArrayList<>();
   private final List<Number> valuesPourcentage = new ArrayList<>();

   private String value;

   private Boolean firstSubdivForBanque = false;

   private Integer rowspan;

   public StatResultsRow(){}

   //	public StatResultsRow(Collection collection, TKThesaurusObject o) {
   //		this.collection = collection;
   //		setThObj(o);
   //	}
   //	
   //	public StatResultsRow(Collection collection, TKThesaurusObject o, Number v) {
   //		this.collection = collection;
   //		setThObj(o);
   //		value = v;
   //	}

   public StatResultsRow(final Banque b, final Integer si, final String sn, final Boolean f){
      setBanque(b);
      setSubDivId(si);
      setSubDivNom(sn);
      firstSubdivForBanque = f;
   }

   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      banque = b;
   }

   @Override
   public String toString(){
      String out;
      if(this.banque != null){
         out = "{" + banque.getNom();
         if(this.subDivNom != null){
            out = out + "-" + this.subDivNom;
         }
         return out + "}";
      }
      return "{Empty StatResultsRow}";
   }

   @Override
   public int hashCode(){
      int hashCode = 17;
      hashCode = 31 * hashCode + (subDivId == null ? 0 : subDivId.hashCode());
      hashCode = 31 * hashCode + (banque == null ? 0 : banque.hashCode());
      return hashCode;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || (!(obj instanceof StatResultsRow))){
         return false;
      }

      final StatResultsRow st = (StatResultsRow) obj;

      return (this.subDivId == st.getSubDivId() || (subDivId != null && subDivId.equals(st.getSubDivId())))
         && (this.banque == st.getBanque() || (banque != null && banque.equals(st.getBanque())));
   }

   //	public boolean getIsFirstSubdivisionOfTheCollection() {
   //		return (this.equals(collection.getSubdivisions().get(0)));
   //	}

   public Boolean getFirstSubdivForBanque(){
      return firstSubdivForBanque;
   }

   public List<Number> getValues(){
      return values;
   }

   public List<Number> getValuesPourcentage(){
      return valuesPourcentage;
   }

   public Integer getSubDivId(){
      return subDivId;
   }

   public void setSubDivId(final Integer i){
      this.subDivId = i;
   }

   public String getSubDivNom(){
      return subDivNom != null ? subDivNom : "";
   }

   public void setSubDivNom(final String subDivNom){
      this.subDivNom = subDivNom;
   }

   //
   //	public String getCalling() {
   //		return calling;
   //	}

   public String getValue(){
      return value;
   }

   //	public void setCalling(String calling) {
   //		this.calling = calling;
   //	}

   public void setValue(final String value){
      this.value = value;
   }

   public Integer getRowspan(){
      return rowspan;
   }

   public void setRowspan(final Integer rowspan){
      this.rowspan = rowspan;
   }

}
