package it.uniroma3.agiw.provaES;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlParser {
	
	public static void parseFile(String inputHtmlFile) throws IOException{
		
		File file = new File(inputHtmlFile);		
		Document doc = Jsoup.parse(file, "UTF-8", "");	
		Element eMETA = doc.select("META").first();
		System.out.println(eMETA.toString());
	}

	public static void main(String[] args) throws IOException{
		
		parseFile("../ProgettoHtmlIndexer/src/resourceshtml/test.html");
		
	}
}
