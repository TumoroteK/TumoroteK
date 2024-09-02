package fr.aphp.tumorotek.manager.io.production;

import java.io.ByteArrayOutputStream;

public class OutputStreamData {
    String documentName;

    String format;

    String ContentType;

    ByteArrayOutputStream outputStream;

    public OutputStreamData(String documentName, String format, String contentType, ByteArrayOutputStream outputStream) {
        this.documentName = documentName;
        this.format = format;
        ContentType = contentType;
        this.outputStream = outputStream;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
