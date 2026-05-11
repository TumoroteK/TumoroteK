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

package fr.aphp.tumorotek.model.utils;

/**
 * Cette sous classe de Duree permet de gérer une durée pouvant être ajoutée à un champ existant dans le cas d'un champ d'annotation
 * de type champ calculé.
 * Lors de la définition de cette durée, l'utilisateur peut saisir un nombre de jours supérieur à 30 qu'il ne souhaite pas que TK transforme 
 * en mois + jours comme c'est le cas pour un type durée classique (utilisable comme type d'un champ d'annotation) 
 * En effet, la transformation du nombre de jours en mois peut donner un résultat différent de l'attendu si la date de référence est sur un mois
 * comportant 31 ou 28.
 * Exemple date de péremption : date de prélèvement + 35 jours, avec une durée standard (transformée en 1 mois + 5j), la date de péremption est 
 * à +36 jours si la date de prélèvement est sur un mois de 31 jours et à 33 si la date de prélèvement est sur février (pour une année non bissextile)
 *
 * @author Corinne HUET
 * @version 2.3.1.1
 * @since TK-864
 */
public class DureeForChampCalcule extends Duree
{
   
   public DureeForChampCalcule(final Long temps, final Long unite){
      super(temps, unite);
   }
   
   //TK-864
   //En standard, si l'utilisateur renseigne 35 jours, cela est transformé en 1 mois et 5 jours
   //Ainsi lors du calcul, c'est 1 mois plus 5 jours qui est appliqué. Or si la date à laquelle on ajoute
   //cette valeurs est sur un mois de 31 jours, on ajoute 36 jours et non 35 comme demandé alors que 
   //pour le mois de février (d'une année non bissextile) on ajoute 33 jours. Les valeurs calculées sont
   //donc inexactes.
   //ce champ forcerEnJours permet d'indiquer si la valeur doit être considérée comme des jours.
   //vaudra true si l'utilisateur renseigne 0 dans année, 0 dans mois et 30 ou plus dans jours
   //Ainsi :
   //Annee:0 Mois:0 J:35 H:12 Mn:0 => forcerEnJours : true
   //Annee:0 Mois:0 J:30 H:12 Mn:0 => forcerEnJours : true
   //Annee:0 Mois:0 J:29 H:12 Mn:0 => forcerEnJours : false (fonctionnement initial)
   //Annee:0 Mois:1 J:5  H:12 Mn:0 => forcerEnJours : false (fonctionnement initial)
   //Annee:0 Mois:0 J:0  H:5  Mn:0 => forcerEnJours : false (fonctionnement initial)
   //Annee:1 Mois:1 J:5 H:12 Mn:0 => forcerEnJours : false (fonctionnement initial)
   private Boolean forceEnJours;
   
   /**
    * Initialise une durée selon l'unité et l'indication "à garder en jours"
    * @param temps temps
    * @param unite unité utiliser (Duree.TYPE_SOUHAITE)
      @param forceEnJours : indication "à garder en jours"
    */
   public DureeForChampCalcule(final Long temps, final Long unite, Boolean forceEnJours){
      super(temps, unite);
      this.forceEnJours = forceEnJours;
   }
   
   
   public Boolean getForceEnJours(){
      return forceEnJours;
   }

   public void setForceEnJours(Boolean forceEnJours){
      this.forceEnJours = forceEnJours;
   }
   
   /**
    * retourne les valeurs pour années et mois : 0 si forceEnJours, sinon renvoie le standard 
    * @return
    */
   @Override
   protected Long[] retrieveAnneesAndMois() {
      if(forceEnJours != null && forceEnJours) {
         return new Long[]{0L, 0L};
      }
      
      return super.retrieveAnneesAndMois();
      
   }
}
