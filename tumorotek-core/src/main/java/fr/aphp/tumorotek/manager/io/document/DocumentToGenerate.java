package fr.aphp.tumorotek.manager.io.document;


public class DocumentToGenerate {

    private String documentName;
    private DocumentContext context;
    private DocumentData data;
    private DocumentFooter footer;


    public DocumentToGenerate(String documentName, DocumentContext context, DocumentData data, DocumentFooter footer) {
        this.documentName = documentName;
        this.context = context;
        this.data = data;
        this.footer = footer;
    }

    public DocumentContext getContext() {
        return context;
    }

    public DocumentData getData() {
        return data;
    }

    public DocumentFooter getFooter() {
        return footer;
    }
}
