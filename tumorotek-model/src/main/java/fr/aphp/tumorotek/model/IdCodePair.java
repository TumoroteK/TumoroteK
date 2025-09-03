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
package fr.aphp.tumorotek.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * classe à utiliser pour récupérer de la base de données un couple id / code
 * l'id étant la clé de l'objet (recherche dans une liste avec l'id)
 * Pour les cas où la clé doit être le code,  utiliser la classe {@link CodeIdPair}
 * Utile notamment pour effectuer des contrôles d'existence lors de traitements de masse
 * 
 * @since 2.3.1.0 (TK-538) 
 * @author chuet
 *
 */
public class IdCodePair implements Serializable
{
   
   private static final long serialVersionUID = 1L;
   
   private Integer id;
   private String code;

   //constructeur utile pour utiliser la méthode contains quand on n'a que l'id
   public IdCodePair(Integer id) {
      this.id = id;
   }
   
   public Integer getId(){
      return id;
   }
   public void setId(Integer id){
      this.id = id;
   }
   public String getCode(){
      return code;
   }
   public void setCode(String code){
      this.code = code;
   }
   
   @Override
   public int hashCode(){
      return Objects.hash(id);
   }

   @Override
   public boolean equals(Object obj){
      if(this == obj)
         return true;
      if(obj == null)
         return false;
      if(getClass() != obj.getClass())
         return false;
      IdCodePair other = (IdCodePair) obj;
      return Objects.equals(id, other.id);
   }
   
   
   
   
}
