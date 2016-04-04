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
//	private String language; vogliamo inserirlo?
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
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
