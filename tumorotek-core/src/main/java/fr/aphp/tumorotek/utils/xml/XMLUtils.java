package fr.aphp.tumorotek.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLUtils {
	private static String ROOT_NODE_NAME = "results";
	private static String ROW_NODE_NAME = "row";

	static Connection con;

	public static InputStream resultSetToXML(ResultSet rs)
			throws ParserConfigurationException, TransformerConfigurationException, TransformerException {

		Statement stmt = null;
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			Element results = doc.createElement(ROOT_NODE_NAME);
			doc.appendChild(results);

			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();

			while (rs.next()) {
				Element row = doc.createElement(ROW_NODE_NAME);
				results.appendChild(row);
				for (int i = 1; i <= colCount; i++) {
					String columnName = rsmd.getColumnName(i);
					String value = rs.getString(i);
					if(value == null) {
						value = "";
					}
					Element node = doc.createElement(columnName);
					node.appendChild(doc.createTextNode(value));
					row.appendChild(node);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return getDocumentAsXml(doc);
	}

		public static InputStream getDocumentAsXml(Document doc)
				throws TransformerConfigurationException, TransformerException {
			DOMSource domSource = new DOMSource(doc);
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    StringWriter sw = new StringWriter();
		    StreamResult sr = new StreamResult(sw);
		    transformer.transform(domSource, sr);

			return new ByteArrayInputStream(sw.toString().getBytes());
		}
}
