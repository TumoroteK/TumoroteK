package fr.aphp.tumorotek.utils.xml;

import java.io.File;
import java.io.InputStream;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLTransform
{

   public static void transform(final InputStream inXML, final InputStream inXSL, final File f)
      throws TransformerConfigurationException, TransformerException{
      final TransformerFactory factory = TransformerFactory.newInstance();
      final StreamSource xslStream = new StreamSource(inXSL);
      final Transformer transformer = factory.newTransformer(xslStream);
      transformer.setErrorListener(new MyErrorListener());
      final StreamSource in = new StreamSource(inXML);
      final StreamResult out = new StreamResult(f);
      transformer.transform(in, out);
      System.out.println("The generated XML file is:" + f);
   }
}

class MyErrorListener implements ErrorListener
{
   @Override
   public void warning(final TransformerException e) throws TransformerException{
      show("Warning", e);
      throw (e);
   }

   @Override
   public void error(final TransformerException e) throws TransformerException{
      show("Error", e);
      throw (e);
   }

   @Override
   public void fatalError(final TransformerException e) throws TransformerException{
      show("Fatal Error", e);
      throw (e);
   }

   private void show(final String type, final TransformerException e){
      System.out.println(type + ": " + e.getMessage());
      if(e.getLocationAsString() != null){
         System.out.println(e.getLocationAsString());
      }
   }
}
