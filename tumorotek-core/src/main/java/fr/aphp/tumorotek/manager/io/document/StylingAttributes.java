package fr.aphp.tumorotek.manager.io.document;

import fr.aphp.tumorotek.manager.io.document.detail.table.AlignmentType;

public class StylingAttributes {
    private String borderLeftColor;
    private boolean withBorder;
    private AlignmentType alignmentType;

    private int colspan;

    private boolean firstTextInItalic;
    private boolean secondTextInItalic;
    private boolean secondTextInBold;
    private boolean firstTextInBold;

    private boolean complementOnAnotherLine;

    public StylingAttributes() {

    }

    public String getBorderLeftColor() {
        return borderLeftColor;
    }

    public void setBorderLeftColor(String borderLeftColor) {
        this.borderLeftColor = borderLeftColor;
    }

    public boolean isWithBorder() {
        return withBorder;
    }

    public void setWithBorder(boolean withBorder) {
        this.withBorder = withBorder;
    }

    public AlignmentType getAlignmentType() {
        return alignmentType;
    }

    public void setAlignmentType(AlignmentType alignmentType) {
        this.alignmentType = alignmentType;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public boolean isFirstTextInItalic() {
        return firstTextInItalic;
    }

    public void setFirstTextInItalic(boolean firstTextInItalic) {
        this.firstTextInItalic = firstTextInItalic;
    }

    public boolean isComplementOnAnotherLine() {
        return complementOnAnotherLine;
    }

    public void setComplementOnAnotherLine(boolean complementOnAnotherLine) {
        this.complementOnAnotherLine = complementOnAnotherLine;
    }

    public boolean isSecondTextInItalic() {
        return secondTextInItalic;
    }

    public void setSecondTextInItalic(boolean secondTextInItalic) {
        this.secondTextInItalic = secondTextInItalic;
    }

    public boolean isSecondTextInBold() {
        return secondTextInBold;
    }

    public void setSecondTextInBold(boolean secondTextInBold) {
        this.secondTextInBold = secondTextInBold;
    }

    public boolean isFirstTextInBold() {
        return firstTextInBold;
    }

    public void setFirstTextInBold(boolean firstTextInBold) {
        this.firstTextInBold = firstTextInBold;
    }
}
