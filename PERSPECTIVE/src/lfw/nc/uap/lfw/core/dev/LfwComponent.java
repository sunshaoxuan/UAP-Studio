package nc.uap.lfw.core.dev;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * LFW 业务组件
 */
public class LfwComponent extends ExtendAttributeSupport implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String pack;
	private LfwUIComponent uiComponent;
	
	private Map<String, LfwWindow> windowMap;
	private Map<String, UIMeta> uimetaMap;
	private Map<String, UIMeta> viewUimetaMap;
	private Map<String, LfwView> viewMap;
	
	public Map<String, UIMeta> getUimetaMap() {
		if(uimetaMap == null)
			uimetaMap = new HashMap<String, UIMeta>(4);
		return uimetaMap;
	}

	public void setUimetaMap(Map<String, UIMeta> uimetaMap) {
		this.uimetaMap = uimetaMap;
	}

	public Map<String, LfwWindow> getWindowMap() {
		if(windowMap == null)
			windowMap = new HashMap<String, LfwWindow>(4);
		return windowMap;
	}

	public void setWindowMap(Map<String, LfwWindow> windowMap) {
		this.windowMap = windowMap;
	}

	public void addLfwWindow(String windowId, LfwWindow window){
		if(windowMap == null)
			windowMap = new HashMap<String, LfwWindow>(); 
		String componentId = "";
		if(this.pack!=null&&!"".equals(this.pack)){
			componentId+=this.pack+".";
		}
		componentId+=this.id;
		window.setComponentId(componentId);
		this.windowMap.put(window.getFullId(), window);
	}
	
	public void removeLfwWindow(String windowId){
		String componentId = "";
		if(this.pack!=null&&!"".equals(this.pack)){
			componentId+=this.pack+".";
		}
		componentId+=this.id;
		if(componentId.equals("")||componentId.equals(LfwUIComponent.ANNOYUICOMPONENT)){
			this.windowMap.remove(windowId);
		}
		else
			this.windowMap.remove(componentId + "." + windowId);
	}
	
	
	public void addLfwView(String viewId,LfwView publicView){
		if(viewMap == null)
			viewMap = new HashMap<String, LfwView>();
		String componentId = "";
		if(this.pack!=null&&!"".equals(this.pack)){
			componentId+=this.pack+".";
		}
		componentId+=this.id;
		publicView.setComponentId(componentId);
		this.viewMap.put(viewId, publicView);
	}
	
	public void removeLfwView(String viewId){
		String componentId = "";
		if(this.pack!=null&&!"".equals(this.pack)){
			componentId+=this.pack+".";
		}
		componentId+=this.id;
		if(componentId.equals("")||componentId.equals(LfwUIComponent.ANNOYUICOMPONENT)){
			this.viewMap.remove(viewId);
		}
		else
			this.viewMap.remove(componentId + "." + viewId);
	}
	
	public void addUIMeta(String windowId, UIMeta uimeta){
		if(uimetaMap == null)
			uimetaMap = new HashMap<String, UIMeta>();
		this.uimetaMap.put(windowId, uimeta);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}
	
	public Object clone() {
		WebElement ele = (WebElement) super.clone();
		return ele;
	}

	public LfwUIComponent getUiComponent() {
		return uiComponent;
	}

	public void setUiComponent(LfwUIComponent uicomponent) {
		this.uiComponent = uicomponent;
	}
	
	public String getFullPath(){
		if(pack == null || pack.equals(""))
			return id;
		return pack + "." + id;
	}

	public Map<String, UIMeta> getViewUimetaMap() {
		if(viewUimetaMap == null)
			viewUimetaMap = new HashMap<String ,UIMeta>(4);
		return viewUimetaMap;
	}

	public void setViewUimetaMap(Map<String, UIMeta> viewUimetaMap) {
		this.viewUimetaMap = viewUimetaMap;
	}

	public Map<String, LfwView> getViewMap() {
		if(viewMap == null)
			viewMap = new HashMap<String, LfwView>(4);
		return viewMap;
	}

	public void setViewMap(Map<String, LfwView> viewMap) {
		this.viewMap = viewMap;
	}
}
