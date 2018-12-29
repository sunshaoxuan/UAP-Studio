package nc.uap.lfw.perspective.listener;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.LFWBasicElementObj;

/**
 * Listener图层
 * 
 * @author guoweic
 * 
 */
public abstract class LFWListenerGraph extends LFWBasicElementObj {

	private static final long serialVersionUID = -7092268685117883046L;
	public static final String PROP_CHILD_ADD = "prop_child_add";
	public static final String PROP_CHILD_REMOVE = "prop_child_remove";
	
//	// 该Listener所在的父对象类型
//	private String targetType = "";
	
	// 图层中包含元素对象数量
	public int elementsCount = 0;

//	private List<ListenerElementObj> jsListeners = new ArrayList<ListenerElementObj>();
//
//	public List<ListenerElementObj> getJsListeners() {
//		return jsListeners;
//	}
//
//	public void setJsListeners(List<ListenerElementObj> jsListeners) {
//		this.jsListeners = jsListeners;
//	}
//
//	public boolean addJsListener(ListenerElementObj jsListener) {
//		//jjsListener.setGraph(this);
//		boolean b = jsListeners.add(jsListener);
//		elementsCount++;
//		if (b) {
//			fireStructureChange(PROP_CHILD_ADD, jsListener);
//		}
//		return b;
//	}
//
//	public boolean removeJsListener(ListenerElementObj jsListener) {
//		boolean b = jsListeners.remove(jsListener);
//		elementsCount--;
//		jsListener.setGraph(null);
//		if (b) {
//			fireStructureChange(PROP_CHILD_REMOVE, jsListener);
//		}
//		return b;
//	}

//	public String getTargetType() {
//		return targetType;
//	}
//
//	public void setTargetType(String targetType) {
//		this.targetType = targetType;
//	}

}
