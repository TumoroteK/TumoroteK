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
import java.util.Objects;

public class ParametrageValue implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer champEntiteId;

   private String thesaurusTableNom;

   private String defaultValue;

   public ParametrageValue(final Integer _c, final String _v, final String _t){
      super();
      this.champEntiteId = _c;
      this.thesaurusTableNom = _t;
      this.defaultValue = _v;
   }

   public Integer getChampEntiteId(){
      return champEntiteId;
   }

   public void setChampEntiteId(final Integer _c){
      this.champEntiteId = _c;
   }

   public String getDefaultValue(){
      return defaultValue;
   }

   public void setDefaultValue(final String _v){
      this.defaultValue = _v;
   }

   public String getThesaurusTableNom(){
      return thesaurusTableNom;
   }

   public void setThesaurusTableNom(final String _t){
      this.thesaurusTableNom = _t;
   }

   @Override
   public boolean equals(final Object obj){
      if(obj == this){
         return true;
      }
      if(obj == null || obj.getClass() != this.getClass()){
         return false;
      }

      final ParametrageValue param = (ParametrageValue) obj;

      return Objects.equals(champEntiteId, param.getChampEntiteId());
   }

   @Override
   public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result + ((champEntiteId == null) ? 0 : champEntiteId.hashCode());
      return result;
   }
}
