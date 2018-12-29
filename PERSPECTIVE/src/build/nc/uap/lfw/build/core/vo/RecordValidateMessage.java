package nc.uap.lfw.build.core.vo;

public class RecordValidateMessage {
	
	/**
	 * ±êÌâ
	 */
	private String title;
	/**
	 * ÄÚÈİ
	 */
	private String content;
	
	public RecordValidateMessage() {

	}
	
	public RecordValidateMessage(String title, String content) {
		super();
		this.title = title;
		this.content = content;
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
		if(title.contains("´íÎó")){
			return "*************" + title + " : " +content;
		}else{
		    return title +" : " + content;
		}
	}

}
