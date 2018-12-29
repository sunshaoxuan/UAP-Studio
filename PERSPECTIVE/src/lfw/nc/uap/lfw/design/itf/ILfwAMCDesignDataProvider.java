/**
 * 
 */
package nc.uap.lfw.design.itf;

import java.util.Map;

import nc.uap.lfw.core.dev.LfwComponent;
import nc.uap.lfw.core.dev.LfwDevModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.AMCServiceObj;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * 
 * UAPWEB服务接口类
 * @author chouhl
 *
 */
public interface ILfwAMCDesignDataProvider {
	
	public void createApplication(String appId, String appName);
	
	public AMCServiceObj operateWebElementXML(AMCServiceObj amcServiceObj);
	
	public AMCServiceObj operateWebElement(AMCServiceObj amcServiceObj);
	
//	public AMCServiceObj operateVO(AMCServiceObj amcServiceObj);
		
	public Map<String, LfwComponent> getCacheComponentMap(String ctxPath, String moduleId, String businessCompId);
	
	public Map<String,LfwComponent> getCacheViewCompMap(String ctxPath, String moduleId, String businessCompId);
	
	public Map<String,Application> getApplicationMap(String ctxPath,String moduleId,String businessCompId);
	
	public Map<String,LfwView> getPublicViews(String ctxPath);	
//	public Map<String, LfwWindow> getWindowsByComponent(LfwComponent component);
//	
	public Application getApplicationById(String ctxPath, String appId);
	
	public LfwWindow getWindowById(String ctxPath, String windowId);
	
	public UIMeta getUImetaById(String ctxPath, String uiMetaId);
	
	public Map<String, LfwDevModel> getLfwDevModelCache(String ctxPath);
	
}
