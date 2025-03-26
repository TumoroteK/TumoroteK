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
package fr.aphp.tumorotek.action.contexte;

import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;

/**
 * Classe de décoration étendant les fonctionnalités d'un conteneur avec des informations
 * contextuelles spécifiques à une plateforme. Cette classe permet de gérer et de représenter un conteneur
 * en ajoutant des métadonnées et des comportements dynamiques basés sur la plateforme "courante".
 *
 * Principales fonctionnalités :
 * - Gestion de la visibilité de l'icône de suppression
 * - Détection du partage de conteneurs entre différentes plateformes
 * - Génération de libellés contextuels
 *
 * @author mathieu BARTHELEMY
 * @version 2.0.10
 * @since 2.0.10
 * @see Conteneur
 * @see Plateforme
 */
public class ConteneurDecorator
{

   private Conteneur conteneur;

   /**
    * Plateforme nécessaire pour appliquer des règles de gestion particulières dans le cas de conteneur partagé
    *  (mis à disposition par une autre plateforme)
    *
    * IMPORTANT : La signification de cette variable varie selon le contexte de l'application :
    *
    * 1. Dans l'onglet administration => Plateforme:
    *    - Représente la plateforme sur laquelle l'utilisateur a cliqué/sélectionné
    *    - Peut ne pas être la plateforme actuellement active
    *
    * 2. Dans les autres sections de l'application :
    *    - Représente la plateforme actuellement active
    */

   private Plateforme plateforme;

   /**
    * Indique si l'en-tête de suppression doit être visible.
    * Déterminé en fonction de la relation entre la plateforme courante
    * et la plateforme d'origine du conteneur.
    */
   private Boolean deleteHeaderVisible;

   /**
    * Libellé formaté représentant le conteneur.
    * Inclut le nom du conteneur, son code,
    * et éventuellement le nom de sa plateforme d'origine.
    */
   private String libelle;


   //Indique si le conteneur est partagé entre différentes plateformes (mis à disposition par une autre plateforme)
   private boolean shared;

   public ConteneurDecorator(final Conteneur c, final Plateforme currentPlateforme){
      this.conteneur = c;
      this.plateforme = currentPlateforme;
      this.deleteHeaderVisible = currentPlateforme == null || !currentPlateforme.equals(c.getPlateformeOrig());
      this.shared = determineSharedStatus();
      this.libelle = generateLibelle();
   }

   /**
    * Détermine si le conteneur est partagé
    * @return vrai si la plateforme courante est différente de la plateforme d'origine du conteneur
    */
   private boolean determineSharedStatus() {
      return plateforme != null &&
         conteneur != null &&
         !plateforme.equals(conteneur.getPlateformeOrig());
   }



   /**
    * Met à jour les variables d'instance dépendantes de l'objet.
    */
   private void updateDependentVariables() {
      this.shared = determineSharedStatus();

      this.deleteHeaderVisible = plateforme != null ?
         !plateforme.equals(conteneur.getPlateformeOrig()) : true;

      this.libelle = generateLibelle();
   }

   public void setShared(boolean shared){
      this.shared = shared;
   }

   public boolean isShared() {
      return shared;
   }


   public Boolean getDeleteHeaderVisible(){
      return deleteHeaderVisible;
   }

   public void setDeleteHeaderVisible(final Boolean i){
      this.deleteHeaderVisible = i;
   }

   public String getLibelle() {
      return libelle;
   }

   public void setLibelle(String libelle) {
      this.libelle = libelle;
   }

   public Conteneur getConteneur(){
      return conteneur;
   }

   public void setConteneur(final Conteneur c){
      this.conteneur = c;
      if (plateforme != null) {
         updateDependentVariables();
      }
   }

   public Plateforme getPlateforme(){
      return plateforme;
   }

   public void setPlateforme(final Plateforme plateforme){
      this.plateforme = plateforme;
      if (conteneur != null) {
         updateDependentVariables();
      }
   }
   public static List<ConteneurDecorator> decorateListe(final List<Conteneur> conts, final Plateforme current){
      List<ConteneurDecorator> decos = null;

      if(conts != null){
         decos = new ArrayList<>();
         for(final Conteneur c : conts){
            decos.add(new ConteneurDecorator(c, current));
         }
      }

      return decos;
   }

   @Override
   public ConteneurDecorator clone(){
      return new ConteneurDecorator(getConteneur(), getPlateforme());
   }

   /**
    * Récupère les conteneurs à passer dans la méthode d'update afin de
    * modifier les relations Conteneur_Plateforme.
    * Exclue de la liste tous les conteneurs dont la plateforme de création
    * correspond à la plateforme en cours
    * @param decos
    * @return List<Conteneur>
    */
   public static List<Conteneur> extractConteneursFromDecos(final List<ConteneurDecorator> decos){
      List<Conteneur> conts = null;
      if(decos != null){
         conts = new ArrayList<>();
         for(final ConteneurDecorator deco : decos){
            if(deco.getDeleteHeaderVisible()){
               conts.add(deco.getConteneur());
            }
         }
      }
      return conts;
   }

   /**
    * Crée et renvoie une chaîne de caractères formatée pour l'affichage du conteneur.
    * Le format suit le modèle : "[Nom du Conteneur] ([Code du Conteneur])"
    * Si la plateforme d'origine du conteneur est différente de la plateforme courante,
    * le nom de la plateforme d'origine est ajouté : "[Nom du Conteneur] ([Code du Conteneur]) [Plateforme d'Origine]"
    *
    * @return Une représentation formatée du conteneur avec son code et éventuellement les informations de plateforme
    */
   private String generateLibelle() {
      StringBuilder sb = new StringBuilder();
      sb.append(conteneur.getNom())
              .append(" (")
              .append(conteneur.getCode())
              .append(")");
      if (shared) {
         sb.append(" [")
                 .append(conteneur.getPlateformeOrig().getNom())
                 .append("]");
      }
      return sb.toString();
   }


   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ConteneurDecorator test = (ConteneurDecorator) obj;
      return ((this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme)))
         && (this.conteneur == test.conteneur || (this.conteneur != null && this.conteneur.equals(test.conteneur))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashConteneur = 0;
      int hashPf = 0;

      if(this.conteneur != null){
         hashConteneur = this.conteneur.hashCode();
      }
      if(this.plateforme != null){
         hashPf = this.plateforme.hashCode();
      }

      hash = 31 * hash + hashConteneur;
      hash = 31 * hash + hashPf;

      return hash;
   }
}
