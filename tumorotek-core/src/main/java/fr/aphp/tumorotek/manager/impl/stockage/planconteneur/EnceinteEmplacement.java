package fr.aphp.tumorotek.manager.impl.stockage.planconteneur;

import fr.aphp.tumorotek.model.stockage.Enceinte;

/**
 * Classe représentant l'emplacement d'une enceinte dans une structure hiérarchique de stockage.
 * Cette classe permet de gérer la position et les relations parent-enfant entre les enceintes,
 * ainsi que le calcul du nombre d'enceintes au dernier niveau pour l'affichage en tableau.
 *
 * <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */
public class EnceinteEmplacement {

    /** L'enceinte associée à cet emplacement. Peut être null si l'emplacement est vide */
    private Enceinte enceinte;

    /** Référence vers l'emplacement parent dans la hiérarchie */
    private EnceinteEmplacement emplacementParent;

    /** 
     * Compteur du nombre d'enceintes au dernier niveau.
     * Cette valeur est utilisée pour déterminer le colspan des cellules d'en-tête
     * lors de la génération du tableau de visualisation.
     */
    private int nbEnceinteDernierNiveau = 0;

    /**
     * Constructeur par défaut.
     */
    public EnceinteEmplacement() {
    }

    /**
     * Constructeur avec paramètres.
     * @param enceinte L'enceinte à placer dans cet emplacement
     * @param emplacementParent L'emplacement parent dans la hiérarchie
     */
    public EnceinteEmplacement(Enceinte enceinte, EnceinteEmplacement emplacementParent) {
        this.enceinte = enceinte;
        this.emplacementParent = emplacementParent;
    }

    /**
     * Incrémente le compteur d'enceintes au dernier niveau.
     * Cette méthode est appelée lors de la lecture de l'arborescence du conteneur.
     */
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

    public void setEnceinte(Enceinte enceinte){
        this.enceinte = enceinte;
    }

    public void setEmplacementParent(EnceinteEmplacement emplacementParent){
        this.emplacementParent = emplacementParent;
    }

    public void setNbEnceinteDernierNiveau(int nbEnceinteDernierNiveau){
        this.nbEnceinteDernierNiveau = nbEnceinteDernierNiveau;
    }

    /**
     * Retourne une représentation textuelle de l'emplacement.
     * @return Le nom de l'enceinte si elle existe, "null" sinon
     */
    @Override
    public String toString(){
        return (this.enceinte != null) ? this.enceinte.getNom() : "null";
    }
}

