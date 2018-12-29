package nc.uap.lfw.core.dev;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.uimodel.Application;

/**
 * 开发模型中的app集合
 * 
 * @author zhangxya
 * 
 */
public class LfwDevApps implements Serializable {
	private static final long serialVersionUID = 7376272998695448583L;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private Map<String, Application> applicationMap;

	public Map<String, Application> getApplicationMap() {
		return applicationMap;
	}

	public void setApplicationMap(Map<String, Application> applicationMap) {
		this.applicationMap = applicationMap;
	}

	public void addApplication(String componentId, Application application) {
		if (applicationMap == null)
			applicationMap = new HashMap<String, Application>();
		this.applicationMap.put(componentId, application);
	}
}
