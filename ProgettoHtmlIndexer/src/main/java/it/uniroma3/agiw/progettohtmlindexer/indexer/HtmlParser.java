package it.uniroma3.agiw.progettohtmlindexer.indexer;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
	
	public static void parseFile(String inputHtmlFile) throws IOException{
		
//		File file = new File(inputHtmlFile);		
//		Document doc = Jsoup.parse(file, "UTF-8", "");	
//		Element eMETA = doc.select("META").first();
//		System.out.println(eMETA.toString());
		
		/*1- Prendere un oggetto da S3, da questo estrapolare l'url dal campo metadata e richiamare
		 * il metodo di JSOUP*/
		Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
		Elements body = doc.getElementsByTag("body");
		/*2- Creare un oggetto che rappresenta la pagina, le cui variabili sono il nome e cognome(preso
		 * dal nome del file di S3), url, titolo e contenuto della pagina*/
		System.out.println(body.text());
		/*3- Per ogni oggetto che abbiamo creato, facciamo partire l'indicizzazione*/

	}

	public static void main(String[] args) throws IOException{
		
		parseFile("../ProgettoHtmlIndexer/src/resourceshtml/test.html");
		
	}
}
