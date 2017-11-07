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
package fr.aphp.tumorotek.webapp.general;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;

import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.au.out.AuDownload;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.sys.WebAppCtrl;
import org.zkoss.zk.ui.util.DeferredValue;

public class FileDownloadTumo {
	/** Open a download dialog to save the specified content at the client.
	 */
	public static void save(Media media, Desktop desktop) {
		save(media, null, desktop);
	}
	/** Open a download dialog to save the specified content at the client
	 * with the suggested file name.
	 *
	 * @param media the media to download
	 * @param flnm the suggested file name, e.g., myfile.pdf.
	 * If null, {@link Media#getName} is assumed.
	 */
	public static void save(Media media, String flnm, Desktop desktop) {
		((WebAppCtrl)desktop.getWebApp())
			.getUiEngine().addResponse(null,
				new AuDownload(new DownloadURL(media, flnm))); //Bug 2114380
	}
	/** Open a download dialog to save the specified content at the client
	 * with the suggested file name.
	 *
	 * @param content the content
	 * @param contentType the content type (aka., mine type),
	 * e.g., application/pdf
	 * @param flnm the suggested file name, e.g., myfile.pdf.
	 * If null, no suggested name is provided.
	 */
	public static void save(byte[] content, String contentType, 
			String flnm, Desktop desktop) {
		save(new AMedia(flnm, null, contentType, content), flnm, desktop);
	}
	/** Open a download dialog to save the specified content at the client
	 * with the suggested file name.
	 *
	 * @param content the content
	 * @param contentType the content type (aka., mine type),
	 * e.g., application/pdf
	 * @param flnm the suggested file name, e.g., myfile.pdf.
	 * If null, no suggested name is provided.
	 */
	public static void save(String content, String contentType, 
			String flnm, Desktop desktop) {
		save(new AMedia(flnm, null, contentType, content), flnm, desktop);
	}
	/** Open a download dialog to save the specified content at the client
	 * with the suggested file name.<br/>
	 * Note: You don't need to close the content (a InputStream), it will be closed automatically after download. 
	 * @param content the content
	 * @param contentType the content type (aka., mine type),
	 * e.g., application/pdf
	 * @param flnm the suggested file name, e.g., myfile.pdf.
	 * If null, no suggested name is provided.
	 */
	public static void save(InputStream content, String contentType, 
			String flnm, Desktop desktop) {
		save(new AMedia(flnm, null, contentType, content), flnm, desktop);
	}
	/** Open a download dialog to save the specified content at the client
	 * with the suggested file name.<br/>
	 * Note: You don't need to close the content (a Reader), it will be closed automatically after download.
	 * @param content the content
	 * @param contentType the content type (aka., mine type),
	 * e.g., application/pdf
	 * @param flnm the suggested file name, e.g., myfile.pdf.
	 * If null, no suggested name is provided.
	 */
	public static void save(Reader content, String contentType, 
			String flnm, Desktop desktop) {
		save(new AMedia(flnm, null, contentType, content), flnm, desktop);
	}
	/** Open a download dialog to save the specified file at the client.
	 *
	 * @param file the file to download to the client
	 * @param contentType the content type, e.g., application/pdf.
	 * Unlike other save methods, it is optional. If null, the file name's
	 * extension is used to determine the content type.
	 * @exception FileNotFoundException if the file is not found.
	 * @since 3.0.8
	 */
	public static void save(File file, String contentType, Desktop desktop)
	throws FileNotFoundException {
		save(new AMedia(file, contentType, null), file.getName(), desktop);
	}
	/** Open a download dialog to save the resource of the specified URL
	 * at the client.
	 * The path must be retrieveable by use of {@link org.zkoss.zk.ui.WebApp#getResource}.
	 *
	 * @param url the URL to get the resource
	 * @param contentType the content type, e.g., application/pdf.
	 * Unlike other save methods, it is optional. If null, the path's
	 * extension is used to determine the content type.
	 * @exception FileNotFoundException if the resource is not found.
	 * @since 3.0.8
	 */
	public static void save(URL url, String contentType, Desktop desktop)
	throws FileNotFoundException {
		String name = url.toExternalForm();
		int j = name.lastIndexOf('/');
		if (j >= 0 && j < name.length() - 1)
			name = name.substring(j + 1);
		save(new AMedia(url, contentType, null), name, desktop);
	}
	/** Open a download dialog to save the resource of the specified path
	 * at the client.
	 *
	 * @param path the path of the resource.
	 * It must be retrieveable by use of {@link org.zkoss.zk.ui.WebApp#getResource}.
	 * @param contentType the content type, e.g., application/pdf.
	 * Unlike other save methods, it is optional. If null, the path's
	 * extension is used to determine the content type.
	 * @exception FileNotFoundException if the resource is not found.
	 * @since 3.0.8
	 */
	public static void save(String path, String contentType, Desktop desktop)
	throws FileNotFoundException {
		final URL url = Executions.getCurrent().getDesktop().getWebApp()
			.getResource(path);
		if (url == null)
			throw new FileNotFoundException(path);
		save(url, contentType, desktop);
	}

	private static class DownloadURL implements DeferredValue {
		private final Media _media;
		private final String _path;
		private DownloadURL(Media media, String flnm) {
			_media = media;

			if (flnm == null) flnm = media.getName();

			final StringBuffer sb = new StringBuffer(32);
			if (flnm != null && flnm.length() != 0) {
				sb.append('/');
				sb.append(URLEncoder.encode(flnm));
				if (flnm.lastIndexOf('.') < 0) {
					final String format = media.getFormat();
					if (format != null)
						sb.append('.').append(format);
				}
			}
			_path = sb.toString();
		}
		public String getValue() {
			return Executions.getCurrent().getDesktop()
				.getDownloadMediaURI(_media, _path);
		}
	}
}
