package fr.aphp.tumorotek.manager.io.document.detail.array;

public class CellContent {
    private String text;
    private String complement;
    private boolean complementInItalic;

    private boolean complementOnAnotherLine;



    public CellContent(String text) {
        this.text = text;
    }

    public CellContent(String text, String complement) {
        this.text = text;
        this.complement = complement;
    }

    public CellContent(String text, String complement, boolean complementInItalic) {
        this.text = text;
        this.complement = complement;
        this.complementInItalic = complementInItalic;
    }


}
