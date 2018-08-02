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
package fr.aphp.tumorotek.action.utils;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.io.export.AbstractTKChamp;
import fr.aphp.tumorotek.model.io.export.Champ;

/**
 * Classe regroupant des méthodes utilitaires relatives à la classe {@link fr.aphp.tumorotek.model.io.export.Champ Champ}
 * @author Gille Chapelot
 *
 */
public abstract class ChampUtils
{

   /**
    * Constructeur privé
    */
   private ChampUtils(){}

   /**
    * Retourne le libellé internationalisé de l'entité à laquelle est liée le {@link fr.aphp.tumorotek.model.io.export.Champ Champ} passé en paramètre
    * @param champ
    * @return
    */
   public static String getEntiteLieeLibelle(final Champ champ){
      return Labels.getLabel("Entite." + champ.entite().getNom());
   }

   /**
    * Retourne le champ ancêtre du {@link fr.aphp.tumorotek.model.io.export.Champ Champ} passé en paramètre
    * @param champ
    * @return
    */
   public static Champ getChampRacine(final Champ champ){

      Champ champRacine = champ;

      while(champRacine.getChampParent() != null){
         champRacine = champRacine.getChampParent();
      }

      return champRacine;

   }

   /**
    * Retourne le datatype du {@link fr.aphp.tumorotek.model.io.export.Champ Champ} passé en paramètre
    * @param champ
    * @return
    */
   public static DataType getChampDataType(final Champ champ){

      DataType dataType = null;

      if(champ.getChampAnnotation() != null){
         dataType = champ.getChampAnnotation().getDataType();
         if("calcule".equals(dataType.getType())) {
            dataType = champ.getChampAnnotation().getChampCalcule().getDataType();
         }
      }else if(champ.getChampDelegue() != null) {
         dataType = champ.getChampDelegue().getDataType();
      }else if(champ.getChampEntite().getQueryChamp() == null){
         dataType = champ.getChampEntite().getDataType();
      }else{
         dataType = champ.getChampEntite().getQueryChamp().getDataType();
      }

      return dataType;

   }

   /**
    * Retourne le nom du {@link fr.aphp.tumorotek.model.io.export.Champ Champ} passé en paramètre
    * @param champ
    * @return
    */
   public static String getChampNom(final Champ champ){

      String nom = null;

      if(champ.getChampAnnotation() != null){
         nom = champ.getChampAnnotation().getNom();
      }else if(champ.getChampDelegue() != null) {
         nom = champ.getChampDelegue().getNom();
      }else if(champ.getChampEntite().getQueryChamp() == null){
         nom = champ.getChampEntite().getNom();
      }else{
         nom = champ.getChampEntite().getQueryChamp().getNom();
      }

      return nom;

   }
   
   public static String getNomEntiteAncetre(Champ champ) {
      
      String nomEntiteAncetre = null;
      Champ parent = champ.getChampParent();
      AbstractTKChamp ceParent = null;
      
      while(parent != null && (parent.getChampEntite() != null || parent.getChampDelegue() != null)){
         
         if(null != parent.getChampEntite()){
            ceParent = parent.getChampEntite();
            nomEntiteAncetre = parent.getChampEntite().getEntite().getNom();
         }else if(null != parent.getChampDelegue()){
            ceParent = parent.getChampDelegue();
            nomEntiteAncetre = parent.getChampDelegue().getEntite().getNom();
         }
         
         parent = parent.getChampParent();
      }
      
      return nomEntiteAncetre.replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
      
   }
   
}
