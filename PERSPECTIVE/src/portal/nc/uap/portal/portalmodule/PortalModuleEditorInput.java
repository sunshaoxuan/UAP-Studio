package nc.uap.portal.portalmodule;

import nc.uap.portal.core.PortalBaseEditorInput;
import nc.uap.portal.deploy.vo.PortalModule;
import nc.uap.portal.lang.M_portal;

/**
 * PortalModuleEditorInput
 * 
 * @author dingrf
 * 
 */
public class PortalModuleEditorInput extends PortalBaseEditorInput {

	private PortalModule portalModule;

	private String moduleName;
	
	public PortalModule getPortalModule() {
		return portalModule;
	}

	public void setPortalModule(PortalModule portalModule) {
		this.portalModule = portalModule;
	}

	
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public PortalModuleEditorInput(PortalModule portalModule,String ModuleName){
		this.portalModule = portalModule;
		this.moduleName = ModuleName;
	}
	
	public String getName() {
		return M_portal.PortalModuleEditorInput_0;
	}

	public String getToolTipText() {
		return M_portal.PortalModuleEditorInput_0;
	}

}
