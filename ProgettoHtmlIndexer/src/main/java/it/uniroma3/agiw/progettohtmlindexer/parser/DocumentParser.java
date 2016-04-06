package it.uniroma3.agiw.progettohtmlindexer.parser;

import java.io.IOException;

import it.uniroma3.agiw.progettohtmlindexer.indexer.ElasticSearchIndexer;
import it.uniroma3.agiw.progettohtmlindexer.model.ParsedResult;

/**
 * Questa classe si occupa di eseguire il parsing dei file da Amazon
 * 
 * @author JavaComeLava
 *
 */
public class DocumentParser {
	
	/**
	 * Questo metodo si occupa del parsing di un singolo documento
	 * 
	 * @param inputS3File -> Documento da parsare
	 * 
	 * @param elasticSeachIndexer
	 * @throws InterruptedException 
	 * @throws IOException 
	 * 
	 */
	public void parseFile(ParsedResult result) throws IOException, InterruptedException {
		ElasticSearchIndexer ESIndexer = new ElasticSearchIndexer();		
		ESIndexer.index(result);
	}

}
