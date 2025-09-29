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
package fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation;

import java.io.Serializable;
import java.util.Objects;

/**
 * cette classe contient toutes les informations nécessaires pour gérer une cellule d'un fichier d'import pour modifier des annotations
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportChampAnnotationInfo implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   //champs valorisés à l'initialisation de l'objet :
   private Integer champAnnotationId;
   //id de l'objet "matériel biologique"
   private Integer objetId;
   private Object newValeur;
   
   //champs valorisés après traitement en cas de mise à jour d'un champ d'annotation
   private Object oldValeur;
   private Integer annotationValeurId;
   
   private Integer numRow;

   public ImportChampAnnotationInfo(Integer champAnnotationId, Integer objetId, Object newValeur, Integer numRow) {
      this.champAnnotationId = champAnnotationId;
      this.objetId = objetId;
      this.newValeur = newValeur;
      this.numRow = numRow;
   }

   public Integer getChampAnnotationId(){
      return champAnnotationId;
   }

   public Integer getObjetId(){
      return objetId;
   }

   public Object getNewValeur(){
      return newValeur;
   }
      
   public Object getOldValeur(){
      return oldValeur;
   }

   public void setOldValeur(Object oldValeur){
      this.oldValeur = oldValeur;
   }

   public Integer getAnnotationValeurId(){
      return annotationValeurId;
   }

   public void setAnnotationValeurId(Integer annotationValeurId){
      this.annotationValeurId = annotationValeurId;
   }
   
   public boolean isUpdate(){
      return annotationValeurId != null;
   }

   public Integer getNumRow(){
      return numRow;
   }

   public void setNumRow(Integer numRow){
      this.numRow = numRow;
   }
   
   @Override
   public int hashCode(){
      return Objects.hash(champAnnotationId, objetId);
   }

   @Override
   public boolean equals(Object obj){
      if(this == obj)
         return true;
      if(obj == null)
         return false;
      if(getClass() != obj.getClass())
         return false;
      ImportChampAnnotationInfo other = (ImportChampAnnotationInfo) obj;
      return Objects.equals(champAnnotationId, other.champAnnotationId) && Objects.equals(objetId, other.objetId);
   }
   
}
