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
package fr.aphp.tumorotek.decorator;

import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.model.impression.CleImpression;

/**
 * Classe décorator pour les CleImpression
 * Reprends les méthode de CleImpression pour permettre l'affichage de labels internationalisés et de valeurs formattées
 * Classe créée le 09/04/2018
 * 
 * @author Answald Bournique
 * @version 2.2.0
 * @since 2.2.0
 *
 */
public class CleImpressionDecorator
{

   /**
    * La CleImpression à décorer
    */
   private CleImpression cleImpression;
   
   /**
    * La valeur du champ pour une sélection
    */
   private String value;

   public CleImpressionDecorator(CleImpression cleImpression){
      this.cleImpression = cleImpression;
   }
   
   /**
    * Décore une liste de CleImpression
    * @return liste de CleImpressionDecorator
    */
   public static List<CleImpressionDecorator> decorateList(List<CleImpression> cleImpressionList){
      List<CleImpressionDecorator> cleImpressionDecoList = new ArrayList<>();
      for(CleImpression cle : cleImpressionList){
         cleImpressionDecoList.add(new CleImpressionDecorator(cle));
      }
      return cleImpressionDecoList;
   }
   
   /**
    * Décore une liste de CleImpression
    * @return liste de CleImpressionDecorator
    */
   public static List<CleImpression> getCleImpressionList(List<CleImpressionDecorator> cleImpressionDecoList){
      List<CleImpression> cleImpressionList = new ArrayList<>();
      for(CleImpressionDecorator cle : cleImpressionDecoList){
         cleImpressionList.add(cle.getCleImpression());
      }
      return cleImpressionList;
   }

   /**
    * Le nom de la clé
    * @return Le nom de la clé
    */
   public String getNom(){
      return this.cleImpression.getNom();
   }

   /**
    * Le Label de clé complet et internationalisé
    * @return le label de clé coimplet et internationalisé
    */
   public String getLabel(){
      return getNom() + " = " + getEntiteLabel() + "." + getChampLabel();
   }
   /**
    * L'entité internationalisée
    * @return L'entité internationalisée
    */
   public String getEntiteLabel(){
      String label = null;
      if(null != this.cleImpression && null != this.cleImpression.getChamp() && null != this.cleImpression.getChamp().entite()){
         label = new EntiteDecorator(this.cleImpression.getChamp().entite()).getLabel();
      }
      return label;
   }

   /**
    * Le champ internationalisé
    * @return Le champ internationalisé
    */
   public String getChampLabel(){
      String label = null;
      if(null != this.cleImpression && null != this.cleImpression.getChamp()){
         label = new ChampDecorator(this.cleImpression.getChamp()).getLabel();
      }
      return label;
   }
   
   /**
    * La CleImpression à décorer
    * @return La CleImpression à décorer
    */
   public CleImpression getCleImpression(){
      return cleImpression;
   }

   /**
    * La CleImpression à décorer
    * @param cleImpression La CleImpression à décorer
    */
   public void setCleImpression(CleImpression cleImpression){
      this.cleImpression = cleImpression;
   }
   
   /**
    * valeur de la clé pour la sélection
    * @return valeur de la clé pour la sélection
    */
   public String getValue(){
      return this.value;
   }

   /**
    * La valeur de la clé pour la sélection
    * @param value valeur de la clé pour la sélection
    */
   public void setValue(String value){
      this.value = value;
   }
}
