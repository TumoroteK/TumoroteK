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
package fr.aphp.tumorotek.model.contexte.gatsbi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChampEntite implements Serializable, Comparable<ChampEntite>
{

   private static final long serialVersionUID = 1L;

   private Integer champEntiteId;

   private Integer champEntiteOrdre;

   private String dateFormat;

   private String thesaurusTableNom;

   private Boolean obligatoire = false;

   private Boolean visible = true;

   private Boolean inTableau = false;

   private Integer ordreTableau;

   private List<ThesaurusValue> thesaurusValues = new ArrayList<>();

   public ChampEntite(){}

   public ChampEntite(final Integer champId, final Integer champOrdre, final String dateFormat, final String thesaurusTableNom,
      final Boolean obligatoire, final Boolean visible, final Boolean inTableau, final Integer ordreTableau,
      final List<ThesaurusValue> thesaurusValues){
      super();
      this.champEntiteId = champId;
      this.champEntiteOrdre = champOrdre;
      this.dateFormat = dateFormat;
      this.thesaurusTableNom = thesaurusTableNom;
      this.obligatoire = obligatoire;
      this.visible = visible;
      this.inTableau = inTableau;
      this.ordreTableau = ordreTableau;
      this.thesaurusValues = thesaurusValues;
   }

   public Integer getChampEntiteId(){
      return champEntiteId;
   }

   public void setChampEntiteId(final Integer _i){
      this.champEntiteId = _i;
   }

   public Integer getChampEntiteOrdre(){
      return champEntiteOrdre;
   }

   public void setChampEntiteOrdre(final Integer _o){
      this.champEntiteOrdre = _o;
   }

   public String getDateFormat(){
      return dateFormat;
   }

   public void setDateFormat(final String _d){
      this.dateFormat = _d;
   }

   public String getThesaurusTableNom(){
      return thesaurusTableNom;
   }

   public void setThesaurusTableNom(final String _f){
      this.thesaurusTableNom = _f;
   }

   public Boolean getObligatoire(){
      return obligatoire != null && obligatoire;
   }

   public void setObligatoire(final Boolean _o){
      this.obligatoire = _o;
   }

   public Boolean getVisible(){
      return visible != null && visible;
   }

   public void setVisible(final Boolean _v){
      this.visible = _v;
   }

   public List<ThesaurusValue> getThesaurusValues(){
      return thesaurusValues;
   }

   public void setThesaurusValues(final List<ThesaurusValue> _v){
      this.thesaurusValues = _v;
   }

   public Boolean getInTableau(){
      return inTableau != null && inTableau;
   }

   public void setInTableau(final Boolean _i){
      this.inTableau = _i;
   }

   public Integer getOrdreTableau(){
      return ordreTableau;
   }

   public void setOrdreTableau(final Integer _o){
      this.ordreTableau = _o;
   }

   @Override
   public boolean equals(final Object obj){
      if(obj == this){
         return true;
      }
      if(obj == null || obj.getClass() != this.getClass()){
         return false;
      }

      final ChampEntite chp = (ChampEntite) obj;

      return Objects.equals(champEntiteId, chp.getChampEntiteId());
   }

   @Override
   public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result + ((champEntiteId == null) ? 0 : champEntiteId.hashCode());
      return result;
   }

   @Override
   public int compareTo(final ChampEntite arg0){
      if(ordreTableau != null && arg0 != null && arg0.getOrdreTableau() != null){
         return ordreTableau.compareTo(arg0.getOrdreTableau());
      }else if(ordreTableau == null){
         return 1;
      }else if(arg0 == null || arg0.getOrdreTableau() == null){
         return -1; // nulls last
      }
      return 0;
   }
}
