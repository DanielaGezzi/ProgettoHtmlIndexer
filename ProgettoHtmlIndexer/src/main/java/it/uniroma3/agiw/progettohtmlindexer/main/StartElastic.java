package it.uniroma3.agiw.progettohtmlindexer.main;

import java.io.IOException;

import it.uniroma3.agiw.progettohtmlindexer.indexer.ElasticSearchIndexer;
import it.uniroma3.agiw.progettohtmlindexer.model.ParsedResult;
import it.uniroma3.agiw.progettohtmlindexer.parser.DownloadFromS3;

public class StartElastic {

	public static void main(String[] args) throws IOException, InterruptedException{
		DownloadFromS3 download = new DownloadFromS3();
		download.Download();
	}

	public void indexer(String content,String url,String query,String title) throws IOException, InterruptedException{
		
		ParsedResult result = new ParsedResult(content,url,query,title);
		ElasticSearchIndexer ESIndexer = new ElasticSearchIndexer();		
		ESIndexer.index(result);
		
//		ElasticSearchIndexer esi = new ElasticSearchIndexer();
//		ParsedResult pr = new ParsedResult();
//		
//		pr.setUrl("www.prova.it");
//		pr.setTitle("provolone della fattanza italiana");
//		
//		
//		String myString = "C'era una volta, nel cuor dell'inverno, mentre i fiocchi di neve cadevano dal cielo "
//				+ "come piume, una regina che cuciva, seduta accanto a una finestra dalla cornice di ebano. "
//				+ "E così, cucendo e alzando gli occhi per guardar la neve, si punse un dito, e caddero nella"
//				+ " neve tre gocce di sangue. Il rosso era così bello su quel candore, ch’ella pensò: "
//				+ "' Avessi una bambina bianca come la neve, rossa come il sangue e daincapelli neri come "
//				+ "il legno della finestra! ' Poco dopo diede alla luce una figlioletta bianca come la neve,"
//				+ " rossa come il sangue e dai capelli neri come l'ebano; e la chiamarono Biancaneve. "
//				+ "E quando nacque, la regina morì.";
//				
//				byte ptext[] = myString.getBytes();
//				String value = new String(ptext, "UTF-8");
//				
//		pr.setContent(value);
//		
//		
//		try {
//			esi.index(pr);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
	}
}
