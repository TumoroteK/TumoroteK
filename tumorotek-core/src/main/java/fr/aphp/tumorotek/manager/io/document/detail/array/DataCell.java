package fr.aphp.tumorotek.manager.io.document.detail.array;


public class DataCell {
    private CellContent cellContent;
    private String borderLeftColor;
    private int colspan = 1;

    private boolean withBorder;
    private AlignmentType alignmentType = AlignmentType.LEFT;

    public DataCell(CellContent cellContent) {
        this.cellContent = cellContent;
    }

    public DataCell(CellContent cellContent, String borderLeftColor) {
        this.cellContent = cellContent;
        this.borderLeftColor = borderLeftColor;
    }

    public DataCell(CellContent cellContent, String borderLeftColor, int colspan, AlignmentType alignmentType) {
        this.cellContent = cellContent;
        this.borderLeftColor = borderLeftColor;
        this.colspan = colspan;
        this.alignmentType = alignmentType;
    }

    public DataCell(CellContent cellContent, String borderLeftColor, int colspan, boolean withBorder, AlignmentType alignmentType) {
        this.cellContent = cellContent;
        this.borderLeftColor = borderLeftColor;
        this.colspan = colspan;
        this.withBorder = withBorder;
        this.alignmentType = alignmentType;
    }

    public CellContent getCellContent() {
        return cellContent;
    }

    public void setCellContent(CellContent cellContent) {
        this.cellContent = cellContent;
    }

    public String getBorderLeftColor() {
        return borderLeftColor;
    }

    public void setBorderLeftColor(String borderLeftColor) {
        this.borderLeftColor = borderLeftColor;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
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
}
