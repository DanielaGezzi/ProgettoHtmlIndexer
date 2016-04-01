package it.uniroma3.agiw.provaES;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

import org.apache.pdfbox.io.IOUtils;
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

public class HtmlIndexer {
	

	private static Client getClient(){
			
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
	
	public static void main(final String[] args) throws IOException, InterruptedException {
		
			final Client client = getClient();
			final String indexName = "agiwtest"; 
			final String documentType = "htmltest";
	        
	        final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
	        if (res.isExists()) {
	            final DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(indexName);
	            delIdx.execute().actionGet();
	        }

	        final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
			
	     // MAPPING 

	        final XContentBuilder mappingBuilder = jsonBuilder()
	        		.startObject()
	        			.startObject(documentType)
	        				/*.startObject("_ttl")
	        					.field("enabled", "true")
	        					.field("default", "1s")
	        				.endObject()*/
	        				.startObject("properties")
	        					.startObject("file")
	        						.field("type", "attachment")
	        						.startObject("fields")
		        						.startObject("content")
	    									.field("analyzer", "ShingleAnalyzer")
	    								.endObject()
	    								.startObject("name")
    										.field("store", "yes")
    									.endObject()
	        							.startObject("content_type")
	        								.field("store", "yes")
	        							.endObject()
	        						.endObject()
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
	                	.startObject("char_filter") //rimuove tag html
	                		.startObject("filter_html")
	                			.field("type", "html_strip")
	                		.endObject()
	                	.endObject()
	                    .startObject("filter")
	                        .startObject("filter_shingle")
	                            .field("type","shingle")
	                            .field("max_shingle_size",2)
	                            .field("min_shingle_size",2)
	                            .field("output_unigrams",false)
	                        .endObject()
	                        .startObject("filter_stemmer")
	                            .field("type","porter_stem")
	                            .field("language","italian")
	                        .endObject()
	                        .startObject("filter_stop")
	                        	.field("type","stop")
	                        	.field("stopwords","_italian_")
                            .endObject()
	                    .endObject()
	                    .startObject("tokenizer")
	                        .startObject("my_ngram_tokenizer")
	                            .field("type","nGram")
	                            .field("min_gram",1)
	                            .field("max_gram",1)
	                        .endObject()
	                    .endObject()
	                    .startObject("analyzer")
	                        .startObject("ShingleAnalyzer")
	                            .field("tokenizer","my_ngram_tokenizer")
	                            .array("filter","standard","lowercase","filter_stemmer","filter_shingle","filter_stop")
	                            .field("char_filter", "filter_html")
	                        .endObject()
	                    .endObject()
	                .endObject()
	            .endObject();
	        
	        System.out.println(settingsBuilder.string());
	        createIndexRequestBuilder.setSettings(settingsBuilder);
		// ANALYZER DONE
	        
	        final CreateIndexResponse createResponse = createIndexRequestBuilder.execute().actionGet();
			System.out.println(createResponse.toString());
	        
	    //ADD DOCUMENT
	        
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, documentType);
			
			InputStream is = new FileInputStream("../ProgettoHtmlIndexer/src/test.html");
			byte[] html = IOUtils.toByteArray(is);

	        BytesReference json = jsonBuilder()
	                 .startObject()
	                 	.startObject("file")
	                 		.field("_name", "testtest")
	                 		.field("_content", html)
	                 	.endObject()
	                 .endObject().bytes();
	        
	        indexRequestBuilder.setSource(json);
	        IndexResponse response = indexRequestBuilder.execute().actionGet();	
	        
	        System.out.println(response.toString());
	}
	

}
