package nc.uap.lfw.editor.common.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import nc.uap.lfw.editor.view.EditorEvent;

/**
 * IE中的事件与Eclipse交互的工具类
 * 
 * @author licza
 * 
 */
public class IEEventTranslateToolKit {
	
	public void dipatcher(EditorEvent event){
		
	}
	
	
	/**
	 * 将event上下文转换为编辑器事件
	 * 
	 * @param eventContext
	 * @return
	 */
	public static final EditorEvent decode(String eventContext) {
		/**
		 * 去除噪音
		 */
		if (eventContext == null || eventContext.isEmpty()
				|| !eventContext.startsWith("event:"))
			return null;
		/**
		 * 类型约定：字符串以,,,分割，第一个位置，存放事件类型，以后的值以键值对形式储存，以“：”分割键与值
		 * tijiao,,,key1:value1
		 */
		String context = eventContext.substring(6);
		if (context.length() <= 0)
			return null;
		String[] ctxs = context.split(",,,");
		EditorEvent event = new EditorEvent(ctxs[0]);
		if (ctxs.length > 1) {
			Map<String, String> source = new HashMap<String, String>();
			for (int i = 1; i < ctxs.length; i++) {
				String spi = ctxs[i];
				int pi = spi.indexOf(":");
				if (pi == -1 || pi == 0)
					continue;
				String key = spi.substring(0, pi);
				String val = spi.substring(pi + 1);
				source.put(key, val);
			}
			event.setSource(source);
		}
		return event;
	}

	/**
	 * 将编辑器事件转换为字符串
	 * 
	 * @param event
	 * @return
	 */
	public static final String encode(EditorEvent event) {
		if (event == null || event.getEventType() == null)
			return null;
		StringBuffer sb = new StringBuffer("event:");
		sb.append(event.getEventType());
		Map<String, String> source = event.getSource();
		if (source != null && !source.isEmpty()) {
			Set<Entry<String, String>> es = source.entrySet();
			for (Entry<String, String> ent : es) {
				sb.append(",,,");
				sb.append(ent.getKey());
				sb.append(":");
				sb.append(ent.getValue());
			}
		}
		return sb.toString();
	}
}
