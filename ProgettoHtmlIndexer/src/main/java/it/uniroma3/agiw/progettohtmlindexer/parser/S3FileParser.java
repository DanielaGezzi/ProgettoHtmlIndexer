package it.uniroma3.agiw.progettohtmlindexer.parser;

import it.uniroma3.agiw.progettohtmlindexer.indexer.ElasticSearchIndexer;

/**
 * Questa classe si occupa di eseguire il parsing dei file da Amazon
 * 
 * @author JavaComeLava
 *
 */
public class S3FileParser {
	
	/**
	 * Questo metodo si occupa del parsing di un singolo file
	 * 
	 * @param inputS3File -> File da parsare
	 * 
	 * @param elasticSeachIndexer
	 * 
	 */
	public void parseFile(String inputWarcFile, ElasticSearchIndexer ESIndexer) {
		
		/* Per ogni file S3 
		 * prendo il file html e faccio estrarre il contenuto dalla classe textExtractor
		 * prendo il metadato url
		 * creo un nuovo oggetto ParsedResult
		 * lo indicizzo
		 * */
		
	}

}
