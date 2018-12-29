package nc.uap.lfw.core.dev;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * lfw开发态整体的模型类
 * @author zhangxya
 *
 */
public class LfwDevModel implements Serializable{
	private static final long serialVersionUID = -1058007465540910504L;
	private String moduleId;
	private Map<String, LfwDevBusiComponent> lfwdevBusinessComMap;

	public Map<String, LfwDevBusiComponent> getLfwdevBusinessComMap() {
		return lfwdevBusinessComMap;
	}
	public void setLfwdevBusinessComMap(
			Map<String, LfwDevBusiComponent> lfwdevBusinessComMap) {
		this.lfwdevBusinessComMap = lfwdevBusinessComMap;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	
	public void addLfwDevBusinessComp(LfwDevBusiComponent businessComp){
		if(lfwdevBusinessComMap == null)
			lfwdevBusinessComMap = new HashMap<String, LfwDevBusiComponent>(); 
		this.lfwdevBusinessComMap.put(businessComp.getBusiComponentId(), businessComp);
	}
	
	public LfwDevBusiComponent getDevBusiComponentByBusiId(String businessCompId){
		if(lfwdevBusinessComMap == null)
			return null;
		else
			return lfwdevBusinessComMap.get(businessCompId);
	}
}
