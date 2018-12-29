package nc.uap.lfw.template.mastersecondly;

import nc.lfw.design.view.LFWWfmConnector;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.lfw.template.AbstractTemplateFactory;
import nc.uap.lfw.template.NewTempleteWindowWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.graphics.Image;

public class MasterSecondlyFactory extends AbstractTemplateFactory {

	@Override
	public String getTemplateTitle() {
		return M_template.MasterSecondlyFactory_0;
	}

	@Override
	public int getPageCount() {
		return 3;
	}

	@Override
	public WizardPage initPage(int index) {
		WizardPage page = null;
		if (index == 0){
			page = new DoubleDsSelectPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_THDDPAGE_DESC));
		}
		else if (index == 1){
			page = new TempWinConfigPage(WEBPersConstants.KEY_TEMP_FORTHPAGE_DESC);
			
		}
		else if (index == 2){
			page = new TempGeneResultPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_RESULT_DESC));
			
		}
		return page;
	}

	@Override
	public Image getPreviewImage() {
		return ImageProvider.advquery;
	}

	@Override
	public boolean finish(NewTempleteWindowWizard wizard) {
		MasterSecondlyWindowAction masterSecondlyAction = new MasterSecondlyWindowAction(wizard);
		boolean flag = masterSecondlyAction.run();
		
		return flag;
	}

	@Override
	public void publish(String funcId, String appId) {
		//更新查询模板
		LFWWfmConnector.executeSql("UPDATE cp_query_template  set nodecode = '" + funcId + "' WHERE nodecode = '$tempcode_" + appId + "'"); 
		LFWWfmConnector.executeSql("UPDATE cp_print_template  set nodecode = '" + funcId + "' WHERE nodecode = '$tempcode_" + appId + "'");
	}



}
