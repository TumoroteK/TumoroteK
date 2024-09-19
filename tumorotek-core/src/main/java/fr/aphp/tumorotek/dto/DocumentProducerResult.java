package fr.aphp.tumorotek.dto;

import java.io.ByteArrayOutputStream;

/**
 * La classe DocumentProducerResult représente le résultat produit par un générateur de documents.
 * Elle contient des informations sur le format du document, le type de contenu associé, ainsi que
 * le flux de sortie où le document est généré.
 *
 * <p>Cette classe est utilisée pour encapsuler ces informations afin de les retourner après la
 * génération d'un document, facilitant ainsi le traitement ultérieur des résultats.</p>
 *
 * <p>Les attributs principaux sont :
 * <ul>
 *   <li>format : le format du document (par exemple, pdf, docx).</li>
 *   <li>contentType : le type MIME du contenu (par exemple, application/pdf, application/msword).</li>
 *   <li>outputStream : le flux de sortie contenant les données du document généré.</li>
 * </ul>
 */
public class DocumentProducerResult {

    String format;
    String contentType;
    ByteArrayOutputStream outputStream;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
