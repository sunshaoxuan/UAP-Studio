package nc.uap.lfw.editor.view;

import java.util.HashMap;
import java.util.Map;

/**
 * IE中UI设计器事件封装
 * 
 * @author licza
 * 
 */
public class EditorEvent {
	/**
	 * 事件来源
	 */
	private Map<String, String> source = null;
	/**
	 * 事件类型
	 */
	private String eventType;

	public Map<String, String> getSource() {
		if (source == null)
			source = new HashMap<String, String>();
		return source;
	}

	public void setSource(Map<String, String> source) {
		this.source = source;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public EditorEvent() {
		super();
	}

	public EditorEvent(Map<String, String> source, String eventType) {
		super();
		this.source = source;
		this.eventType = eventType;
	}

	public EditorEvent(String eventType) {
		super();
		this.eventType = eventType;
	}
	
}
