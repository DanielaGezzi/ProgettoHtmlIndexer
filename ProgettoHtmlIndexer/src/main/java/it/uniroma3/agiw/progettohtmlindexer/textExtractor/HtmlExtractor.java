package it.uniroma3.agiw.progettohtmlindexer.textExtractor;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Questa classe si occupa di estrarre il testo da un documento html
 * 
 * @author JavaComeLava
 *
 */

public class HtmlExtractor {
	
	/**
	 * Restituisce il contenuto testuale della pagina esclusi i link (mantenendo
	 * però il contenuto dei link href)
	 * 
	 * @param fileHtml -> file html
	 *            
	 * @return La stringa del contenuto senza i tag html e i link
	 */
	public String extractContentWithoutLink(File fileHtml){
		
		String content = "";
			/*dal file prendo il contenuto del tag <body> 
			 *rimuovo i link mantenendo però il contenuto dell href
			 *rimuovo i tag html
			 *salvo sotto forma di stringa e return
			  */
		
		return content;
	};
	
	/**
	 * Restuituisce il titolo del documento html
	 * 
	 * @param fileHtml -> file html
	 *           
	 * @return Una stringa rappresentante il titolo o null se il tag title non è
	 *         presente
	 */
	public String extractTitle(File fileHtml) {
		
		String title = "";
		/*dal file prendo il contenuto del tag <title> 
		 *salvo sotto forma di stringa e return
		  */
	
	return title;
		
	}
	
	
	
	
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
