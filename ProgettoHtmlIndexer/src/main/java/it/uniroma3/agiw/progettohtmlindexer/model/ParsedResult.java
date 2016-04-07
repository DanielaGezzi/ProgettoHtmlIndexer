package it.uniroma3.agiw.progettohtmlindexer.model;

/**
 * Questa classe rappresenta la porzione di record che viene inviata all'indexer
 * 
 * @author JavaComeLava
 *
 */
public class ParsedResult {
	
	private String url;
	private String title;
	private String content;
	
	public ParsedResult(String url, String title, String content){
		this.content = content;
		this.url = url;
		this.title = title;
	}
	
	public ParsedResult(){
		
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
		
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "ParsedResult ["
				+ "url=" + url + 
				", title=" + title + 
				", content=" + content + 
				"]";
	}
}
