package fr.aphp.tumorotek.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

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

public class XMLUtils
{
   private static String ROOT_NODE_NAME = "results";

   private static String ROW_NODE_NAME = "row";

   static Connection con;

   public static InputStream resultSetToXML(final ResultSet rs)
      throws ParserConfigurationException, TransformerConfigurationException, TransformerException{

      Document doc = null;
      try{
         final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         final DocumentBuilder builder = factory.newDocumentBuilder();
         doc = builder.newDocument();
         final Element results = doc.createElement(ROOT_NODE_NAME);
         doc.appendChild(results);

         final ResultSetMetaData rsmd = rs.getMetaData();
         final int colCount = rsmd.getColumnCount();

         while(rs.next()){
            final Element row = doc.createElement(ROW_NODE_NAME);
            results.appendChild(row);
            for(int i = 1; i <= colCount; i++){
               final String columnName = rsmd.getColumnName(i);
               String value = rs.getString(i);
               if(value == null){
                  value = "";
               }
               final Element node = doc.createElement(columnName);
               node.appendChild(doc.createTextNode(value));
               row.appendChild(node);
            }
         }

      }catch(final Exception e){
         e.printStackTrace();
      }finally{
         try{
            if(con != null){
               con.close();
            }
            if(rs != null){
               rs.close();
            }
         }catch(final Exception e){}
      }
      return getDocumentAsXml(doc);
   }

   public static InputStream getDocumentAsXml(final Document doc) throws TransformerConfigurationException, TransformerException{
      final DOMSource domSource = new DOMSource(doc);
      final TransformerFactory tf = TransformerFactory.newInstance();
      final Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      final StringWriter sw = new StringWriter();
      final StreamResult sr = new StreamResult(sw);
      transformer.transform(domSource, sr);

      return new ByteArrayInputStream(sw.toString().getBytes());
   }
}
