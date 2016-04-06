package it.uniroma3.agiw.progettohtmlindexer.indexer;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.tika.io.IOUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.google.gson.Gson;

import it.uniroma3.agiw.progettohtmlindexer.model.ParsedResult;

public class ElasticProva {
	

	public ElasticProva(){
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
	
	
	public void index() throws IOException, InterruptedException {
		
			final Client client = getClient();
			final String indexName = "agiw"; 
			final String documentType = "test";
	        
	        final IndicesExistsResponse res = 
	        		client.admin().indices().prepareExists(indexName).execute().actionGet();
	        
	        if (res.isExists()) {
	            final DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(indexName);
	            delIdx.execute().actionGet();
	        }

	        final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
			
	     // MAPPING 
	        final XContentBuilder mappingProva = jsonBuilder()
	        		.startObject()
	        			.startObject(documentType)
	        				.startObject("properties")
	        					.startObject("text")
	        					.field("type", "string")
	        					.field("analyzer", "My_analyzer")
	        					.endObject()
	        				.endObject()
	        			.endObject()
	        		.endObject();
	       
	        System.out.println(mappingProva.string());
	        createIndexRequestBuilder.addMapping(documentType, mappingProva);

	     // MAPPING DONE
	        
	     /* ANALYZER 
	      * NB:per ora ho usato filter, tokenizer, etc del codice da cui ho preso ispirazione 
	      * ma ce ne sono una valanga, bisogna decidere ql usare
	      */
	        
	        final XContentBuilder settingProva = XContentFactory.jsonBuilder()
	        		.startObject()
		        		.startObject("analysis")
		        			.startObject("filter")
		        				.startObject("my_filter")
		        					.field("type","asciifolding")
		        					.field("preserve_original", true)
		        				.endObject()
		        			.endObject()
			        		.startObject("analyzer")
				        		.startObject("My_analyzer")
					        		.field("tokenizer", "standard")
					        		.array("filter", "lowercase", "my_filter")
				        		.endObject()
			        		.endObject()
		        		.endObject()
	        		.endObject();
	        
	        System.out.println(settingProva.string());
	        createIndexRequestBuilder.setSettings(settingProva);
		// ANALYZER DONE
	        
	        final CreateIndexResponse createResponse = createIndexRequestBuilder.execute().actionGet();
			System.out.println(createResponse.toString());
	        
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, documentType);
	        
			String myString = "olà";
			String value = new String(myString.getBytes("ascii"), "ascii");
	        indexRequestBuilder.setSource("{\"text\":\""+value+"\"}");
	        IndexResponse response = indexRequestBuilder.execute().actionGet();	
	        
	        System.out.println(response.toString());
	     	   
	    	    }


	}


