package it.uniroma3.agiw.progettohtmlindexer.main;

import java.io.UnsupportedEncodingException;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;

import it.uniroma3.agiw.progettohtmlindexer.indexer.ElasticSearchIndexer;

public class mainsearch {
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		
		ElasticSearchIndexer esi = new ElasticSearchIndexer();
		Client client = esi.getClient();
		
		String myquery = "paolo merialdo";
		String query = new String(myquery.getBytes("UTF-8"), "UTF-8");

		
		SearchResponse response =//client.prepareSearch().execute().actionGet();
				client.prepareSearch("agiw")
				//.setTypes("html")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.multiMatchQuery("dell'invenno", "content", "title"))
				.setFrom(0).setSize(60).setExplain(true)
				.execute()
				.actionGet();
		
		System.out.println(response.toString());
		
	}
	

}
