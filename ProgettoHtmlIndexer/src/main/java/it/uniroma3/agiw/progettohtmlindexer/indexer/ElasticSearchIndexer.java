package it.uniroma3.agiw.progettohtmlindexer.indexer;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.net.InetAddress;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.google.gson.Gson;

import it.uniroma3.agiw.progettohtmlindexer.model.ParsedResult;

public class ElasticSearchIndexer {
	

	public ElasticSearchIndexer(){
		super();
	}
	
	private Client getClient(){
			
			Settings settings = Settings.settingsBuilder()
					.put("cluster.name", "elasticsearch").build();
			Client transportClient = TransportClient.builder()
					.settings(settings)
					.build();
			
			try{
				
				InetSocketTransportAddress inetSocketTransportAddress = 
						new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300);
				((TransportClient) transportClient).addTransportAddress(inetSocketTransportAddress);
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			
			return (Client) transportClient;
	}
	
	
	public void index(ParsedResult parsedRes) throws IOException, InterruptedException {
		
			final Client client = getClient();
			final String indexName = "agiw2"; 
			final String documentType = "html";
	        
	        final IndicesExistsResponse res = 
	        		client.admin().indices().prepareExists(indexName).execute().actionGet();
	        
	        if (!res.isExists()) {
	            
	        final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
			
	     // MAPPING 

	        final XContentBuilder mappingBuilder = jsonBuilder()
	        		.startObject()
	        			.startObject(documentType)
	        				.startObject("_timestamp")
	        					.field("enabled", true)
	        				.endObject()
	        				.startObject("properties")
		        				.startObject("url")
		        					.field("type", "string")
		        					.field("store","yes")
		        				.endObject()
		        				.startObject("title")
		        					.field("type", "string")
		        					.field("store", "no")
		        					.field("analyzer", "standard")
		        				.endObject()
		        				.startObject("content")
		        					.field("type", "string")
		        					.field("store", "no")
		        					.field("analyzer", "CustomAnalyzer")
		        				.endObject()
		        			.endObject()
	        			.endObject()
	                .endObject();
	        
	        System.out.println(mappingBuilder.string());
	        createIndexRequestBuilder.addMapping(documentType, mappingBuilder);

	     // MAPPING DONE
	        
	     /* ANALYZER 
	      * NB:per ora ho usato filter, tokenizer, etc del codice da cui ho preso ispirazione 
	      * ma ce ne sono una valanga, bisogna decidere ql usare
	      */
	        final XContentBuilder settingsBuilder = XContentFactory.jsonBuilder()
	        		.startObject()
	                .startObject("analysis")
	                	.startObject("char_filter") 
	                		.startObject("filter_html")
	                			.field("type", "html_strip") //rimuove tag html
	                		.endObject()
	                	.endObject()
	                    .startObject("filter")
	                        .startObject("filter_elision")
	                        	.field("type","elision")
	                        	.array("articles",
	                        			"c", "l", "all", "dall", "dell",
	                                    "nell", "sull", "coll", "pell",
	                                    "gl", "agl", "dagl", "degl", "negl",
	                                    "sugl", "un", "m", "t", "s", "v", "d")
	                        .endObject()
	                        .startObject("filter_stemmer")
	                            .field("type","porter_stem")
	                            .field("language","italian")
	                        .endObject()
	                        .startObject("filter_stop")
	                        	.field("type","stop")
	                        	.field("stopwords","_italian_")
	                        	.field("ignore_case", true)
                            .endObject()
                            .startObject("filter_worddelimiter")
                            	.field("type", "word_delimiter")
                            .endObject()
                            .startObject("filter_ngram")
                            	.field("type", "edgeNGram")
                            	.field("min_gram", 2)
                            	.field("max_gram", 20)
                            .endObject()
	                    .endObject()
	                    .startObject("tokenizer")
	                        .startObject("my_tokenizer")
	                            .field("type","ngram")
	                            .field("min_gram", 5)
	                            .field("max_gram", 20)
	                            .array("token_char", "letter", "digit")
	                        .endObject()
	                    .endObject()
	                    .startObject("analyzer")
	                        .startObject("CustomAnalyzer")
	                        	.field("type", "custom")
	                            .field("tokenizer","standard")
	                            .array("filter","standard","lowercase","asciifolding", "filter_ngram","filter_stemmer",
	                            		"filter_stop","filter_elision","filter_worddelimiter")
	                            .field("char_filter", "filter_html")
	                        .endObject()
	                    .endObject()
	                .endObject()
	            .endObject();
	        
	        System.out.println(settingsBuilder.string());
	        createIndexRequestBuilder.setSettings(settingsBuilder);
		// ANALYZER DONE
	    // CREATE INDEX   
	        final CreateIndexResponse createResponse = createIndexRequestBuilder.execute().actionGet();
			System.out.println(createResponse.toString());
		// DONE	
	        }
	       
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, documentType);
	        
			Gson input = new Gson();
			System.out.println(input.toJson(parsedRes, ParsedResult.class).toString());
	        indexRequestBuilder.setSource(input.toJson(parsedRes, ParsedResult.class).toString());
	        IndexResponse response = indexRequestBuilder.execute().actionGet();	
	        
	        System.out.println(response.toString());
	     	   
	    	    }


	}


