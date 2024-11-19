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
   //l'enceinte présente à l'emplacement peut être null dans le cas d'un emplacement vide
   private Enceinte enceinte;

   private EnceinteEmplacement emplacementParent;

   //valeur qui sera incrémentée lors de la lecture de l'arborescence du conteneur
   //cette valeur correspondra au colspan stocké dans les cellules d'entête du tableau final
   private int nbEnceinteDernierNiveau = 0;


   public EnceinteEmplacement(Enceinte enceinte, EnceinteEmplacement emplacementParent) {
      this.enceinte = enceinte;
      this.emplacementParent = emplacementParent;
   }

   public void increaseNbEnceinteDernierNiveau() {
      nbEnceinteDernierNiveau++;
   }


   public Enceinte getEnceinte(){
      return enceinte;
   }

   public EnceinteEmplacement getEmplacementParent(){
      return emplacementParent;
   }

   public int getNbEnceinteDernierNiveau(){
      return nbEnceinteDernierNiveau;
   }

   @Override
   public String toString() {
      Enceinte enceinte = getEnceinte();
      if(enceinte == null) {
         return "null";
      }
      else {
         return enceinte.getNom();
      }
   }
}



