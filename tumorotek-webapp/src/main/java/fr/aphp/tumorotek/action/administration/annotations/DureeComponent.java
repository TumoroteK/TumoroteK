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
package fr.aphp.tumorotek.action.administration.annotations;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Longbox;

import fr.aphp.tumorotek.model.utils.Duree;

/**
 * MacroComponent dessinant les composant editables permettant à
 * l'utilisateur de saisir une durée.
 * Classe crée le 20/03/2018
 * 
 * @author Answald Bournique
 * @version 2.2.0
 * @since 2.2.0
 *
 */
public class DureeComponent extends Div
{

   /**
    *
    */
   private static final long serialVersionUID = 6063687343765504014L;

   /**
    * Durée lié au composant
    */
   private Duree duree = new Duree(0L, Duree.SECONDE);
   /**
    * Désactivation du composant (grisage des différents champs)
    */
   private Boolean disabled = false;

   private final Label anneesLabel = new Label(Labels.getLabel("date.annees")+" ");
   private final Longbox anneesBox = new Longbox();

   Label moisLabel = new Label(Labels.getLabel("date.months")+" ");
   Longbox moisBox = new Longbox();

   Label joursLabel = new Label(Labels.getLabel("date.jours")+" ");
   final Longbox joursBox = new Longbox();

   Label heuresLabel = new Label(Labels.getLabel("date.heures")+" ");
   final Longbox heuresBox = new Longbox();

   Label minutesLabel = new Label(Labels.getLabel("date.minutes")+" ");
   final Longbox minutesBox = new Longbox();

   /**
    * Initialise le composant Duree à zero (non rempli)
    */
   public DureeComponent(){
      super();
      buildComponent();
   }
   
   /**
    * Initialise le composant avec une durée liée
    * @param duree
    */
   public DureeComponent(Duree duree){
      this();
      setDuree(duree);
   }

   /**
    * Récupère la durée liée au composant
    * @return
    */
   public Duree getDuree(){
      fillupDuree();
      return duree;
   }

   /**
    * Lie la durée au composant
    * @param duree
    */
   public void setDuree(Duree duree){
      this.duree = duree;
      fillupComponent();
   }
   
   /**
    * Retourne true si le composent est désactivé
    * @return Retourne true si le composent est désactivé
    */
   public Boolean isDisabled(){
      return this.disabled;
   }
   
   /**
    * Activer ou désactiver le composent
    * @param disabled true/false
    */
   public void setDisabled(Boolean disabled){
      anneesBox.setDisabled(disabled);
      moisBox.setDisabled(disabled);
      joursBox.setDisabled(disabled);
      heuresBox.setDisabled(disabled);
      minutesBox.setDisabled(disabled);
   }

   /**
    * Cree la structure du composant
    */
   private void buildComponent(){
      anneesBox.setWidth("15px");
      anneesBox.setValue(0L);

      moisBox.setWidth("15px");
      moisBox.setValue(0L);

      joursBox.setWidth("15px");
      joursBox.setValue(0L);

      heuresBox.setWidth("15px");
      heuresBox.setValue(0L);

      minutesBox.setWidth("15px");
      minutesBox.setValue(0L);

      this.appendChild(anneesBox);
      this.appendChild(anneesLabel);
      this.appendChild(moisBox);
      this.appendChild(moisLabel);
      this.appendChild(joursBox);
      this.appendChild(joursLabel);
      this.appendChild(heuresBox);
      this.appendChild(heuresLabel);
      this.appendChild(minutesBox);
      this.appendChild(minutesLabel);
   }

   /**
    * Rempli le composant en fonction de la durée
    */
   private void fillupComponent(){
      if(this.duree != null){
         Duree dureeDecompte = new Duree(duree.getTemps(Duree.MILLISECONDE), Duree.MILLISECONDE);
         Long annees = dureeDecompte.getTemps(Duree.ANNEE);
         dureeDecompte.addTemps(-annees, Duree.ANNEE);
         Long mois = dureeDecompte.getTemps(Duree.MOIS);
         dureeDecompte.addTemps(-mois, Duree.MOIS);
         Long jours = dureeDecompte.getTemps(Duree.JOUR);
         dureeDecompte.addTemps(-jours, Duree.JOUR);
         Long heures = dureeDecompte.getTemps(Duree.HEURE);
         dureeDecompte.addTemps(-heures, Duree.HEURE);
         Long minutes = dureeDecompte.getTemps(Duree.MINUTE);

         anneesBox.setValue(annees);
         moisBox.setValue(mois);
         joursBox.setValue(jours);
         heuresBox.setValue(heures);
         minutesBox.setValue(minutes);
      }else{
         anneesBox.setValue(0L);
         moisBox.setValue(0L);
         joursBox.setValue(0L);
         heuresBox.setValue(0L);
         minutesBox.setValue(0L);
      }
   }
   
   /**
    * Génère la durée en fonction des valeurs du composant
    */
   private void fillupDuree(){
      Duree duree = new Duree(0L, Duree.SECONDE);
      duree.addTemps(minutesBox.getValue(), Duree.MINUTE);
      duree.addTemps(heuresBox.getValue(), Duree.HEURE);
      duree.addTemps(joursBox.getValue(), Duree.JOUR);
      duree.addTemps(moisBox.getValue(), Duree.MOIS);
      duree.addTemps(anneesBox.getValue(), Duree.ANNEE);
      this.duree = duree;
   }
   
}
