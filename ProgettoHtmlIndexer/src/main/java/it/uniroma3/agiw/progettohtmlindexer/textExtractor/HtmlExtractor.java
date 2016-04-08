package it.uniroma3.agiw.progettohtmlindexer.textExtractor;

import java.io.IOException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import it.uniroma3.agiw.progettohtmlindexer.model.ParsedResult;

/**
 * Questa classe si occupa di estrarre il testo da un documento html
 * 
 * @author JavaComeLava
 *
 */

public class HtmlExtractor {
	
	/**
	 * Restituisce l'oggetto ParsedResult i cui campi sono l'url, il titolo ed il 
	 * content della pagina
	 * 
	 * @param url della pagina di interesse
	 *            
	 * @return L'oggetto ParsedResult
	 * @throws IOException 
	 */
	public ParsedResult extractContentWithoutLink(String url) throws IOException{

		ParsedResult result = null;
		System.out.println("url:"+url);
		try{
			Document doc = Jsoup.connect(url).validateTLSCertificates(false)
											 .followRedirects(true)
											 .ignoreHttpErrors(true)
											 .get();
			Elements title = doc.getElementsByTag("title");
			Elements content = doc.getElementsByTag("body");
			String encTitle = new String(title.text().getBytes("UTF-8"), "UTF-8");
			String encContent = new String(content.text().getBytes("UTF-8"), "UTF-8");
			String encUrl = new String(url.getBytes("UTF-8"), "UTF-8");
			
			System.out.println("TITLE : "+encTitle);
			System.out.println("URL : "+encUrl);
			System.out.println("CONTENT : "+encContent);
			
			result = new ParsedResult();
			result.setUrl(encUrl);
			result.setContent(encContent);
			result.setTitle(encTitle);
			
		}catch(HttpStatusException x){
			System.out.println(x.getStatusCode());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
}