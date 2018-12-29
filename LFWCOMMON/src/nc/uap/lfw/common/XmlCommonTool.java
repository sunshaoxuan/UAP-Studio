package nc.uap.lfw.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.uap.lfw.plugin.common.CommonPlugin;

import org.w3c.dom.Document;

public class XmlCommonTool {

	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	private static DocumentBuilder builder = null;
	
	private static TransformerFactory transFactory = TransformerFactory.newInstance();
	
	public static Document parseXML(File xmlFile){
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlFile);
		} catch (Exception e) {
			CommonPlugin.getPlugin().logError(e.getMessage());
		}		
		return doc;
		
	}
	public static Document createDocument(){
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
		} catch (Exception e) {
			CommonPlugin.getPlugin().logError(e.getMessage());
		}		
		return doc;
	}
	public static void documentToXml(Document doc,File xmlFile){
		Transformer trans;
		try {
			trans = transFactory.newTransformer();
			trans.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
			trans.setOutputProperty(OutputKeys.INDENT,"yes");
			trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "7");
			DOMSource source = new DOMSource(doc);
			
			Writer wr = null;
	    	if(!xmlFile.exists())
	    		xmlFile.createNewFile();
	    	wr = new OutputStreamWriter(new FileOutputStream(xmlFile), "UTF-8");
	    	StreamResult result = new StreamResult(wr);
	    	trans.transform(source, result);
	    	wr.close();
		} catch (Exception e) {
			CommonPlugin.getPlugin().logError(e.getMessage());
		}
		
	}
}
