package nc.uap.lfw.application;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.dialog.SimpleSWTBrowser;

/**
 * @author chouhl
 *
 */
public class AMCPublishNCAction extends NodeAction {
	
	public AMCPublishNCAction(String text){
		super(text, text);
//		ApplicationConf appConf = LFWAMCConnector.getApplication(LFWAMCPersTool.getCurrentFolderPath(), WEBProjConstants.AMC_APPLICATION_FILENAME);
//		if(appConf != null){
//			setNodeid(appConf.getDefaultWindowId());
//		}
	}

	public void run() {
		String url = "";
		if(getText().equals(WEBPersConstants.REGISTER_FUNCTION_NODE)){
			url = "http://localhost/portal/app/mockapp/cp_funcregister?nodecode=1019040201";
		}else if(getText().equals(WEBPersConstants.MANAGE_MENU_CATEGORY)){
			url = "http://localhost/portal/app/mockapp/cp_menucategory?nodecode=1019050101";
		}else if(getText().equals(WEBPersConstants.REGISTER_MENU)){
			url = "http://localhost/portal/app/mockapp/cp_menuitem?nodecode=1019050102";
		}else if(getText().equals(WEBPersConstants.REGISTER_FORM_TYPE)){
			url = "http://localhost/portal/app/mockapp.app/wfm_flowsetting?nodecode=1019030101";
		}
		SimpleSWTBrowser browser = new SimpleSWTBrowser(url);
		browser.openBrowser();
	}
	
}
