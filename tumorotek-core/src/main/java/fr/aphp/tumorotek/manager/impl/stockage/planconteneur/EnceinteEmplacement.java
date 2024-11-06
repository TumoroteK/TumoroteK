package fr.aphp.tumorotek.manager.impl.stockage.planconteneur;

import fr.aphp.tumorotek.model.stockage.Enceinte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnceinteEmplacement {
    private final Enceinte enceinte;
    private final List<EnceinteEmplacement> children;
    private boolean isLastEnceinte;

    public EnceinteEmplacement(Enceinte enceinte) {
        this.enceinte = enceinte;
        this.children = new ArrayList<>();
        this.isLastEnceinte = false;
    }

    public void addChild(EnceinteEmplacement child) {
        children.add(child);
    }

    public void setIsLastEnceinte(boolean isLast) {
        this.isLastEnceinte = isLast;
    }

    public boolean isLastEnceinte() {
        return this.isLastEnceinte;
    }

    /**
     * Gets the number of leaf nodes (terminal enceintes) under this node.
     * For table display, this represents how many columns this enceinte should span.
     * @return number of terminal enceintes
     */
    public int getColumnSpan() {
        // If this is a leaf node with no children, use nbPlaces
        if (children.isEmpty() || isLastEnceinte) {
            return 1;
        }

     
        // For parent nodes, sum up children's spans
        int span = 0;
        for (EnceinteEmplacement child : children) {
            if (child != null && child.getEnceinte() != null) {
                span += child.getColumnSpan();
            }
        }

        return Math.max(span, enceinte.getNbPlaces());
    }

    public Enceinte getEnceinte() {
        return enceinte;
    }

    public List<EnceinteEmplacement> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String toString() {
        return enceinte != null ? enceinte.getNom() : "(vide)";
    }
}
