package it.uniroma3.agiw.progettohtmlindexer.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import it.uniroma3.agiw.progettohtmlindexer.indexer.ElasticSearchIndexer;
import it.uniroma3.agiw.progettohtmlindexer.model.ParsedResult;
import it.uniroma3.agiw.progettohtmlindexer.textExtractor.HtmlExtractor;

public class ParserFromS3 {

	private static String bucketName = "prova-agiw";
	private static String prefix = "dataset0/"; //Abbiamo diversi dataset per ora
		
	public void Download() throws IOException, InterruptedException {
	    
        AWSCredentials credentials = new BasicAWSCredentials(
				"AKIAJIGUFYU6GAH3IOBQ", 
				"D9XT2gtzXmQHxPKPyw7EeLrNiG4ieXOOVnx+WC/g");
		
		AmazonS3 s3client = new AmazonS3Client(credentials);
		
		try{
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
					.withBucketName(bucketName)
	                .withPrefix(prefix);
	        ObjectListing objectListing;            
	        do{
	        	objectListing = s3client.listObjects(listObjectsRequest);
	        	for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
	        		parseS3Object(bucketName, objectSummary.getKey(),s3client);             
	        	}
	        	listObjectsRequest.setMarker(objectListing.getNextMarker());
	        }while (objectListing.isTruncated());
	        
		}catch(AmazonServiceException ase){
			System.out.println("Caught an AmazonServiceException, " +
	            			   "which means your request made it " +
	            			   "to Amazon S3, but was rejected with an error response " +
	                    	   "for some reason.");
	        System.out.println("Error Message:    " + ase.getMessage());
	        System.out.println("HTTP Status Code: " + ase.getStatusCode());
	        System.out.println("AWS Error Code:   " + ase.getErrorCode());
	        System.out.println("Error Type:       " + ase.getErrorType());
	        System.out.println("Request ID:       " + ase.getRequestId());
		
		}catch(AmazonClientException ace){
	        System.out.println("Caught an AmazonClientException, " +
	            			   "which means the client encountered " +
	            			   "an internal error while trying to communicate" +
	            			   " with S3, " +
	                    	   "such as not being able to access the network.");
	        System.out.println("Error Message: " + ace.getMessage());
	    }        
	}
		
	public static void parseS3Object(String bucket_name, String key_name,AmazonS3 connection) throws IOException, InterruptedException{

		S3Object object = connection.getObject(new GetObjectRequest(bucket_name, key_name));
		String url = object.getObjectMetadata().getUserMetaDataOf("URL");
		
		HtmlExtractor extractor = new HtmlExtractor();	
		ParsedResult result = extractor.extractContentWithoutLink(url);
		
		ElasticSearchIndexer indexer = new ElasticSearchIndexer();
		indexer.index(result);
	}
	
	public void parsePageFromUrl() throws IOException, InterruptedException{

		String url;
		HtmlExtractor extractor = new HtmlExtractor();	
		ElasticSearchIndexer indexer = new ElasticSearchIndexer();
		ParsedResult result = null;

		BufferedReader br = new BufferedReader(new FileReader("../ProgettoHtmlIndexer/src/listaResults.txt"));
    	while ((url = br.readLine()) != null) {
    		result = extractor.extractContentWithoutLink(url);
    		if(result != null){
    			indexer.index(result);}
    	}
    	br.close();	
	}
}