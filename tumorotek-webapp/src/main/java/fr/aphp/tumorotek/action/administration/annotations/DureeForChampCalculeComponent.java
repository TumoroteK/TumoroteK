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

package fr.aphp.tumorotek.action.administration.annotations;

import fr.aphp.tumorotek.model.utils.Duree;
import fr.aphp.tumorotek.model.utils.DureeForChampCalcule;


/**
 * Cette sous classe de DureeComponent permet de gérer une durée pouvant être ajoutée à un champ existant dans le cas d'un champ d'annotation
 * de type champ calculé.
 * Elle est rattachée à un {@link DureeForChampCalcule} qui permet de gérer une durée de 2 façons (en mois - mode standard ou en jours) selon 
 * la saisie de l'utilisateur
 * @author Corinne HUET
 * @version 2.3.1.1
 * @since TK-864
 */
public class DureeForChampCalculeComponent extends DureeComponent
{

   private static final long serialVersionUID = 8854381806018969508L;

   /**
    * Initialise le composant Duree à zero (non rempli)
    */
   public DureeForChampCalculeComponent(){
      super();
      duree = new DureeForChampCalcule(0L, Duree.SECONDE);
   }
   
   public DureeForChampCalcule getDureeForChampCalcule() {
      return (DureeForChampCalcule) getDuree();
   }
   
   /**
    * Remplit le composant en fonction de la durée
    */
   @Override
   protected void fillupComponent(){
      super.fillupComponent();
      //TK-864 : si la durée doit être forcée en jours, surcharge des valeurs par défaut :
      //années et mois forcés à 0 et nombre de jours adaptés
      Boolean forceEnJours = (duree == null ? null : ((DureeForChampCalcule)duree).getForceEnJours());
      if(forceEnJours != null && forceEnJours) {
         Long annees = anneesBox.getValue();
         Long mois = moisBox.getValue();
         Long jours = joursBox.getValue();
         jours = jours + mois*Duree.NB_JOURS_DANS_MOIS + annees*Duree.NB_JOURS_DANS_ANNEE;
         joursBox.setValue(jours);
         moisBox.setValue(0L);
         anneesBox.setValue(0L);
      }
   }
   
   /**
    * Génère la durée en fonction des valeurs du composant et alimente le champ forceEnJours à true
    * si l'utilisateur n'a pas saisie d'année ni de mois mais un nombre de jours supérieur ou égal à 30
    */
   @Override
   protected void fillupDuree(){
      super.fillupDuree();
      //TK-864 : si l'utilisateur a renseigné un nombre de jours supérieur ou égal à 30
      //sans renseigner d'années ni de mois, on garde l'information 
      //qu'il ne veut pas que ce soit transformé en mois
      if((anneesBox.getValue() == null || anneesBox.getValue() == 0) 
         && (moisBox.getValue() == null || moisBox.getValue() == 0) 
         && (joursBox.getValue() != null && joursBox.getValue() >= 30)) {
         ((DureeForChampCalcule)this.duree).setForceEnJours(true);
      }
   }
   
   @Override
   protected void reinit(){
      duree = new DureeForChampCalcule(0L, Duree.SECONDE);
   }
}
