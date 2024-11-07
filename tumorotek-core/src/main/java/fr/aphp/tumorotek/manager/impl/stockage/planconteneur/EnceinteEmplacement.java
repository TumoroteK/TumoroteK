package fr.aphp.tumorotek.manager.impl.stockage.planconteneur;

import fr.aphp.tumorotek.model.stockage.Enceinte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnceinteEmplacement {
    private final Enceinte enceinte;
    private final List<EnceinteEmplacement> children;
    private boolean isLastEnceinte;
    private static final Map<EnceinteEmplacement, Integer> spanCache = new HashMap<>();

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
        // Check cache first
        if (spanCache.containsKey(this)) {
            return spanCache.get(this);
        }

        int span;
        if (this.enceinte == null) {
            span = 1;
        } else if (children.isEmpty() || isLastEnceinte) {
            span = enceinte.getNbPlaces();
        } else {
            span = 0;
            for (EnceinteEmplacement child : children) {
                if (child == null) {
                    span += 1;
                } else {
                    span += child.getColumnSpan();
                }
            }
            span = Math.max(span, enceinte.getNbPlaces());
        }

        // Cache the result
        spanCache.put(this, span);
        System.out.println("Final span for " + this + ": " + span);

        return span;
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
