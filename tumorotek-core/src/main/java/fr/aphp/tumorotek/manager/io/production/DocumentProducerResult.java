/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 *
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français
 * et respectant les principes de diffusion des logiciels libres. Vous
 * pouvez utiliser, modifier et/ou redistribuer ce programme sous les
 * conditions de la licence CeCILL telle que diffusée par le CEA, le
 * CNRS et l'INRIA sur le site "http://www.cecill.info".
 * En contrepartie de l'accessibilité au code source et des droits de
 * copie, de modification et de redistribution accordés par cette
 * licence, il n'est offert aux utilisateurs qu'une garantie limitée.
 * Pour les mêmes raisons, seule une responsabilité restreinte pèse sur
 * l'auteur du programme, le titulaire des droits patrimoniaux et les
 * concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les
 * risques associés au chargement,  à l'utilisation,  à la modification
 * et/ou au  développement et à la reproduction du logiciel par
 * l'utilisateur étant donné sa spécificité de logiciel libre, qui peut
 * le rendre complexe à manipuler et qui le réserve donc à des
 * développeurs et des professionnels  avertis possédant  des
 * connaissances  informatiques approfondies.  Les utilisateurs sont
 * donc invités à charger  et  tester  l'adéquation  du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs
 * systèmes et ou de leurs données et, plus généralement, à l'utiliser
 * et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.manager.io.production;

import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * La classe DocumentProducerResult représente le résultat produit par la génération, à un format donné,
 * d'un fichier (contenant potentiellement plusieurs {@DocumentToGenerate})
 * Elle contient des informations sur le format du document, le type de contenu associé, ainsi que
 * le flux de sortie correspondant au document généré.
 *
 * <p>Cette classe est utilisée pour encapsuler les informations liées à la production d'un document afin de
 * les transmettre à l'appelant qui transformera cet objet en un {@link OutoutStreanData} attendu par la couche front 
 *
 * <p>Les attributs principaux sont :
 * <ul>
 *   <li>format : le format du document (par exemple, pdf, docx, xlsx) utilisé comme extension dans le nom du fichier associé au document généré.
 *   <li>contentType : le type MIME du contenu (par exemple, application/pdf, application/msword).
 *   <li>outputStream : le flux de sortie contenant les données du document généré.</li>
 * </ul>
 * <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */
public class DocumentProducerResult {
    //format du document (par exemple, pdf, docx, xlsx). 
    //Cette valeur sera utilisée comme extension dans le nom du fichier correspondant au document généré.
    //Utiliser une constante de ConfigManager.
    private String format;

    //le type MIME du contenu (par exemple, application/pdf, application/msword)
    //Utiliser une constante de ConfigManager.
    private String contentType;

    //le flux de sortie contenant les données du document généré.
    private ByteArrayOutputStream outputStream;
    
    public DocumentProducerResult(String format, String contentType, ByteArrayOutputStream outputStream) {
       this.format = format;
       this.contentType = contentType;
       this.outputStream = outputStream;
    }

    public String getFormat() {
        return format;
    }

    public String getContentType() {
        return contentType;
    }

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }
}
