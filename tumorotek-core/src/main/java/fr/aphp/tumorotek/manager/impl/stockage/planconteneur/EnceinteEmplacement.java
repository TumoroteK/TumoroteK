package fr.aphp.tumorotek.manager.impl.stockage.planconteneur;

import fr.aphp.tumorotek.model.stockage.Enceinte;

public class EnceinteEmplacement {

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

