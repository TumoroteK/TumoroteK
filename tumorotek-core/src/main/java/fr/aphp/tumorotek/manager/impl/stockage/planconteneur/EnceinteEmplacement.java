package fr.aphp.tumorotek.manager.impl.stockage.planconteneur;

import fr.aphp.tumorotek.model.stockage.Enceinte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente un nœud dans l'arborescence des enceintes de stockage.
 * Cette classe permet de modéliser la hiérarchie des emplacements de stockage
 * et de calculer leur disposition dans une interface tabulaire.
 */
public class EnceinteEmplacement
{
   /** L'enceinte associée à ce nœud */
   private final Enceinte enceinte;

   /** Liste des enceintes enfants dans la hiérarchie */
   private final List<EnceinteEmplacement> children;

   /** Indique si cette enceinte est une feuille dans l'arborescence */
   private boolean isLeafNode;

   /** Cache pour optimiser le calcul des spans des colonnes */
   private static final Map<EnceinteEmplacement, Integer> spanCache = new HashMap<>();

   public EnceinteEmplacement(Enceinte enceinte){
      this.enceinte = enceinte;
      this.children = new ArrayList<>();
      this.isLeafNode = false;
   }

   /**
    * Ajoute une enceinte enfant à ce nœud.
    * @param child L'enceinte enfant à ajouter
    */
   public void addChild(EnceinteEmplacement child){
      children.add(child);
   }

   public boolean isLeafNode(){
      return isLeafNode;
   }

   public void setLeafNode(boolean leafNode){
      isLeafNode = leafNode;
   }

   /**
    * Cette méthode calcule le nombre de colonnes que cet emplacement peut occuper.
    *
    * La méthode utilise un cache pour éviter de recalculer la portée des colonnes si elle a déjà été calculée.
    * Elle vérifie d'abord si la portée est déjà présente dans le cache. Si c'est le cas, elle renvoie la valeur mise en cache,
    * ce qui améliore l'efficacité du programme en évitant des calculs redondants.
    *
    * Si l'emplacement n'a pas d'enceinte ou s'il s'agit de la dernière enceinte, il occupe une seule colonne.
    * Sinon, si l'emplacement n'a pas d'enfants, la portée est égale au nombre de places disponibles dans l'enceinte.
    * Si l'emplacement a des enfants, il additionne les portées des colonnes de chacun de ses enfants et prend également en compte
    * le nombre de places disponibles dans son enceinte pour déterminer la portée maximale.
    *
    * @return int Le nombre total de colonnes que cet emplacement peut occuper.
    */
   public int getColumnSpan() {
      // Vérifie d'abord si la portée a déjà été calculée et stockée dans le cache
      if (spanCache.containsKey(this)) {
         return spanCache.get(this); // Retourne directement la valeur mise en cache pour éviter un recalcul
      }

      int span; // Déclaration d'une variable pour stocker la portée des colonnes

      // Vérifie si l'emplacement n'a pas d'enceinte ou s'il s'agit du dernier emplacement
      if (this.enceinte == null || isLeafNode) {
         span = 1; // L'emplacement n'occupe qu'une seule colonne
      } else if (children.isEmpty()) { // Vérifie si cet emplacement n'a pas d'enfants
         span = enceinte.getNbPlaces(); // La portée est égale au nombre de places disponibles dans l'enceinte
      } else {
         span = 0; // Initialise la portée à zéro

         // Parcourt chaque enfant et calcule sa portée en fonction de sa présence ou absence
         for (EnceinteEmplacement child : children) {
            if (child == null) {
               span += 1; // Compte comme une colonne supplémentaire si l'enfant est nul
            } else {
               span += child.getColumnSpan(); // Ajoute la portée de chaque enfant non nul à celle-ci
            }
         }

         // Prend le maximum entre la somme des portées des enfants et le nombre de places dans l'enceinte
         span = Math.max(span, enceinte.getNbPlaces());
      }

      spanCache.put(this, span); // Stocke le résultat calculé dans le cache pour une utilisation future

      return span; // Retourne finalement la portée calculée
   }

   /**
    * Retourne l'enceinte associée à ce nœud.
    * @return l'enceinte ou null si c'est un emplacement vide
    */
   public Enceinte getEnceinte(){
      return enceinte;
   }

   /**
    * Retourne une vue non modifiable de la liste des enfants.
    * @return liste des enceintes enfants
    */
   public List<EnceinteEmplacement> getChildren(){
      return Collections.unmodifiableList(children);
   }

   @Override
   public String toString(){
      return enceinte != null ? enceinte.getNom() : "(vide)";
   }
}
