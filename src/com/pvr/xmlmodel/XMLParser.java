package com.pvr.xmlmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class XMLParser {
	public XMLParser() {
	}

	public String getXmlFromFile(Context context,String filename) 
	{
		   StringBuffer buff = new StringBuffer();
		   //File root = Environment.getExternalStorageDirectory();
		   //File xml = new File(root, filename);
		   
		   File xml = new File(filename);
		   BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(xml));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			Toast.makeText(context, " " + filename + " not found.", Toast.LENGTH_LONG).show();
			Log.d("tag11",e.toString());
		}
		   String line = null;
		   try {
			while ((line = reader.readLine()) != null)
			   {
			      buff.append(line).append("\n");
			   }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("tag12",e.toString());
		}
		   try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("tag13",e.toString());
		}
		   return buff.toString();
		}
	
	public String getXMLFromRow(Context context, int xml_resource) {
		String xml = "";
		InputStream stream = context.getResources().openRawResource(
				xml_resource);
		try {
			byte[] buffer = new byte[stream.available()];
			stream.read(buffer);
			stream.close();
			xml = new String(buffer, "UTF-8"); // you just need to specify the
												// charsetName
			return xml;
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("GET_XML_FROM_ROW",e.toString());
			return "";
		}
	}

	/**
	 * Getting XML DOM element
	 * 
	 * @param XML
	 *            string
	 * @throws TransformerException 
	 * */
	public Document getDomElement(Context context,String xml, int dtdResourceId) throws Exception {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dbf.setValidating(true);
			
			
			//if(dbf.isCoalescing() == false)
			//{
			//	dbf.setCoalescing(true);
			//}
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);
			
			DOMSource source = new DOMSource(doc);
			
			TransformerFactory mTransformerFactory = TransformerFactory.newInstance();
			Transformer mTransformer = mTransformerFactory.newTransformer();
			
			String dtd = this.getXMLFromRow(context, dtdResourceId);
			
			mTransformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd);
			
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			
			mTransformer.transform(source, result);
			
			db.parse(new InputSource(new StringReader(writer.toString())));
			
		} catch (ParserConfigurationException e) {
			Log.d("ERROR", e.toString());
			return null;
		} catch (SAXException e) {
			Log.d("ERROR", e.toString());
			return null;
		} catch (IOException e) {
			Log.d("ERROR", e.toString());
			return null;
		} catch (Exception e) {
			Log.d("ERROR", e.toString());
			throw e;
		}
		return doc;
	}

	/**
	 * Getting node value
	 * 
	 * @param elem
	 *            element
	 */
	public final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	/**
	 * Getting node value
	 * 
	 * @param Element
	 *            node
	 * @param key
	 *            string
	 * */
	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}
}
