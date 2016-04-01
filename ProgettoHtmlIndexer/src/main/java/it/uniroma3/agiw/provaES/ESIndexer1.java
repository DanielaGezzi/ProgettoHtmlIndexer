package it.uniroma3.agiw.provaES;

import java.io.IOException;
import java.net.InetAddress;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;

public class ESIndexer1 {
	
	private static final String ID_NOT_FOUND = "<ID NOT FOUND>";
	
	//Create transport client
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
        // Create Index and set settings and mappings
        final String indexName = "test";
        final String documentType = "tweet";
        final String documentId = "1";
        final String fieldName = "foo";
        final String value = "bar";

        final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
        if (res.isExists()) {
            final DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(indexName);
            delIdx.execute().actionGet();
        }

        final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);

        // MAPPING GOES HERE

        final XContentBuilder mappingBuilder = jsonBuilder().startObject().startObject(documentType)
                .startObject("_ttl").field("enabled", "true").field("default", "1s").endObject().endObject()
                .endObject();
        System.out.println(mappingBuilder.string());
        createIndexRequestBuilder.addMapping(documentType, mappingBuilder);

        // MAPPING DONE
        createIndexRequestBuilder.execute().actionGet();

        // Add documents
        final IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, documentType, documentId);
        // build json object
        final XContentBuilder contentBuilder = jsonBuilder().startObject().prettyPrint();
        contentBuilder.field(fieldName, value);

        indexRequestBuilder.setSource(contentBuilder);
        indexRequestBuilder.execute().actionGet();

        // Get document
        System.out.println(getValue(client, indexName, documentType, documentId, fieldName));

        int idx = 0;
        while (true) {
            Thread.sleep(10000L);
            idx++;
            System.out.println(idx * 10 + " seconds passed");
            final String name = getValue(client, indexName, documentType, documentId, fieldName);
            if (ID_NOT_FOUND.equals(name)) {
                break;
            } else {
                // Try again
                System.out.println(name);
            }
        }
        System.out.println("Document was garbage collected");
    }

    protected static String getValue(final Client client, final String indexName, final String documentType,
            final String documentId, final String fieldName) {
        final GetRequestBuilder getRequestBuilder = client.prepareGet(indexName, documentType, documentId);
        getRequestBuilder.setFields(new String[] { fieldName });
        final GetResponse response2 = getRequestBuilder.execute().actionGet();
        if (response2.isExists()) {
            final String name = response2.getField(fieldName).getValue().toString();
            return name;
        } else {
            return ID_NOT_FOUND;
        }
    }

}
