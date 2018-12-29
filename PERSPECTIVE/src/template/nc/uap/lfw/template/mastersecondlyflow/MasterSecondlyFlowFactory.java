package nc.uap.lfw.template.mastersecondlyflow;

import nc.lfw.design.view.LFWWfmConnector;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.lfw.template.AbstractTemplateFactory;
import nc.uap.lfw.template.NewTempleteWindowWizard;
import nc.uap.lfw.template.mastersecondly.TempGeneResultPage;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.graphics.Image;

public class MasterSecondlyFlowFactory extends AbstractTemplateFactory {

	@Override
	public String getTemplateTitle() {
		return M_template.MasterSecondlyFlowFactory_0;
	}

	@Override
	public int getPageCount() {
		return 4;
	}

	@Override
	public WizardPage initPage(int index) {
		WizardPage page = null;
		if (index == 0){
			page = new FlowDoubleDsSelectPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_FLOW_THDDPAGE_DESC));
		}
		else if (index == 1){
			page = new TempFlowConfigPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_FLOWPAGE_DESC));						
		}
		else if (index == 2){
			page = new FlowTempWinConfigPage(WEBPersConstants.KEY_TEMP_FLOWFORTHPAGE_DESC);						
		}
		else if (index == 3){
			page = new TempGeneResultPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_RESULT_DESC));
			
		}
		return page;
	}

	@Override
	public Image getPreviewImage() {
		return ImageProvider.advflowquery;
	}

	@Override
	public boolean finish(NewTempleteWindowWizard wizard) {
		MasterSecondlyFlowWindowAction masterSecondlyAction = new MasterSecondlyFlowWindowAction(wizard);
		return masterSecondlyAction.run();
	}

	@Override
	public void publish(String funcId, String appId) {
		//更新查询模板
		LFWWfmConnector.executeSql("UPDATE cp_query_template  set nodecode = '" + funcId + "' WHERE nodecode = '$tempcode_" + appId + "'");
		LFWWfmConnector.executeSql("UPDATE cp_print_template  set nodecode = '" + funcId + "' WHERE nodecode = '$tempcode_" + appId + "'");  
	}

}
