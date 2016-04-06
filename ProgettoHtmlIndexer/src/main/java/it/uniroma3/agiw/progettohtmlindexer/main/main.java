package it.uniroma3.agiw.progettohtmlindexer.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import it.uniroma3.agiw.progettohtmlindexer.indexer.ElasticProva;
import it.uniroma3.agiw.progettohtmlindexer.indexer.ElasticSearchIndexer;
import it.uniroma3.agiw.progettohtmlindexer.model.ParsedResult;

public class main {

	public static void main(String[] args) throws UnsupportedEncodingException{
		
		ElasticSearchIndexer esi = new ElasticSearchIndexer();
		ParsedResult pr = new ParsedResult();
		
		pr.setUrl("www.prova.it");
		pr.setTitle("provolone della fattanza italiana");
		
		
		String myString = "C’era una volta, nel cuor dell’inverno, mentre i fiocchi di neve cadevano dal cielo "
				+ "come piume, una regina che cuciva, seduta accanto a una finestra dalla cornice di ebano. "
				+ "E così, cucendo e alzando gli occhi per guardar la neve, si punse un dito, e caddero nella"
				+ " neve tre gocce di sangue. Il rosso era così bello su quel candore, ch’ella pensò: "
				+ "‘ Avessi una bambina bianca come la neve, rossa come il sangue e daincapelli neri come "
				+ "il legno della finestra! ‘ Poco dopo diede alla luce una figlioletta bianca come la neve,"
				+ " rossa come il sangue e dai capelli neri come l’ebano; e la chiamarono Biancaneve. "
				+ "E quando nacque, la regina morì.";
				
				String value = new String(myString.getBytes("UTF-8"), "UTF-8");
				/*byte ptext[] = myString.getBytes();
				String value = new String(ptext, "UTF-8");
				System.out.println(value);*/
				
		pr.setContent(value);
		
		
		try {
			esi.index(pr);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		ElasticProva ep = new ElasticProva();
		try {
			ep.index();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

}
