package nc.uap.lfw.core.dev;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LfwDevNodes implements Serializable{
	private static final long serialVersionUID = -3307164827637737302L;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private Map<String, LfwComponent> componentMap;

	public Map<String, LfwComponent> getComponentMap() {
		return componentMap;
	}

	public void setComponentMap(Map<String, LfwComponent> componentMap) {
		this.componentMap = componentMap;
	}

	public void addLfwComponent(String componentId, LfwComponent component){
		if(componentMap == null)
			componentMap = new HashMap<String, LfwComponent>(); 
		this.componentMap.put(componentId, component);
	}
}
