package fr.aphp.tumorotek.utils.xml;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLTransform {

	public static void transform(InputStream inXML, InputStream inXSL, File f)
			throws TransformerConfigurationException, TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		StreamSource xslStream = new StreamSource(inXSL);
		Transformer transformer = factory.newTransformer(xslStream);
		transformer.setErrorListener(new MyErrorListener());
		StreamSource in = new StreamSource(inXML);
		StreamResult out = new StreamResult(f);
		transformer.transform(in, out);
		System.out.println("The generated XML file is:" + f);
	}
}

class MyErrorListener implements ErrorListener {
	public void warning(TransformerException e) throws TransformerException {
		show("Warning", e);
		throw (e);
	}

	public void error(TransformerException e) throws TransformerException {
		show("Error", e);
		throw (e);
	}

	public void fatalError(TransformerException e) throws TransformerException {
		show("Fatal Error", e);
		throw (e);
	}

	private void show(String type, TransformerException e) {
		System.out.println(type + ": " + e.getMessage());
		if (e.getLocationAsString() != null)
			System.out.println(e.getLocationAsString());
	}
}
