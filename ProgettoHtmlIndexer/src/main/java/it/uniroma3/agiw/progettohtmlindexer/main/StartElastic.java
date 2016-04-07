package it.uniroma3.agiw.progettohtmlindexer.main;

import java.io.IOException;

import it.uniroma3.agiw.progettohtmlindexer.parser.ParserFromS3;

public class StartElastic {

	public static void main(String[] args) throws IOException, InterruptedException{
		ParserFromS3 download = new ParserFromS3();
//		download.Download();
		download.parsePageFromUrl();
	}	
}