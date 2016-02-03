package com.example.spellingbee;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.AsyncTask;

public class URLReader {


	
	private String word;
	private String URLr;
	private String URLd;
	private String audioPath;
	
	
	public URLReader(String w)
	{
		word = w;
		URLr = "http://apifree.forvo.com/key/ae346b70452f2d1aa39f87b1b2837951/format/xml/action/word-pronunciations/word/" + word +
		"/language/en/order/rate-desc";
		URLd = "http://services.aonaware.com/DictService/DictService.asmx/Define?word=" + w;
		
		
	}

	public String getWord()
	{
	  return word;
	}
	
	public String getDefinition(){
		String str = "No definition found";
		
		try{
		URL d_url = new URL(URLd);
		URLConnection urlConnection2 = d_url.openConnection();
		InputStream inputStream2 = d_url.openStream();	
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document inputXML2 = documentBuilder.parse(inputStream2);
		
		NodeList nList = inputXML2.getElementsByTagName("Definition");
		
		int spot = 0;
		
		for(int c = 0; c<nList.getLength(); c++){
		Element eElement2 = (Element) nList.item(c);
		NodeList dict = eElement2.getElementsByTagName("Id");
		
		System.out.println("\nCurrnet Element :" + dict.item(0).getTextContent());
		if(dict.item(0).getTextContent().equals("wn")){
		  spot = c;	
		}
		
		}
		Element t = (Element) nList.item(spot);
		NodeList def = t.getElementsByTagName("WordDefinition");
		
		System.out.println(def.item(0).getTextContent());	
		str = def.item(0).getTextContent();
		}
		catch(Exception e){
			return str;
		}
		return str;
	}
	
	public String parseURL(){
		String str = "";

		try {

			URL l_url = new URL(URLr);
			URLConnection urlConnection = l_url.openConnection();
			InputStream inputStream = l_url.openStream();	
			
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document inputXML = documentBuilder.parse(inputStream);
			
			System.out.println("Root element :" + inputXML.getDocumentElement().getNodeName());
			 
			NodeList nList = inputXML.getElementsByTagName("item");
			
			Node nNode = nList.item(0);
			 
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
	            str = eElement.getElementsByTagName("pathmp3").item(0).getTextContent();
			}
			
	       }
		catch(Exception e){
			return str;
		}
		
		
		
		return str;
     }
	

    public static String findWord(String w){
		String str = "";

		String URL2 = "http://apifree.forvo.com/key/ae346b70452f2d1aa39f87b1b2837951/format/xml/action/word-pronunciations/word/" + w + 
		"/language/en/order/rate-desc";		
		
		System.out.println("YAY!");
		
		try {

			URL l_url = new URL(URL2);
			URLConnection urlConnection = l_url.openConnection();
			InputStream inputStream = l_url.openStream();	
			
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document inputXML = documentBuilder.parse(inputStream);
			
			System.out.println("Root element :" + inputXML.getDocumentElement().getNodeName());
			 
			NodeList nList = inputXML.getElementsByTagName("item");
			
			Node nNode = nList.item(0);
			 
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
	            str = eElement.getElementsByTagName("pathmp3").item(0).getTextContent();
			}

	       }
		catch(Exception e){
			e.printStackTrace(System.out);

			System.out.println("hoy!");
			str = "";
			
		}
		finally{
		
		System.out.println("noy!");
		
		return str;}    	
    }
	


    
    
	public void setWord(String w){
		word = w;
		URLr = "http://apifree.forvo.com/key/ae346b70452f2d1aa39f87b1b2837951/format/xml/action/word-pronunciations/word/" + word + 
		"/language/en/order/rate-desc";	
		URLd = "http://services.aonaware.com/DictService/DictService.asmx/Define?word=" + w;
				
	}
	
}
	

