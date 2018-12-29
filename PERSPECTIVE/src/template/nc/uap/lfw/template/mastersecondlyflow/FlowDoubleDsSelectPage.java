package nc.uap.lfw.template.mastersecondlyflow;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.template.mastersecondly.DoubleDsSelectPage;

import org.eclipse.jface.wizard.IWizardPage;

/**
 * �����б���ѡ�����ӱ����ݼ����������
 * @author guomq1
 * 2012-8-1
 */
public class FlowDoubleDsSelectPage extends DoubleDsSelectPage {
	public FlowDoubleDsSelectPage(String pageName) {
		super(pageName);
	}

	@Override
	 public IWizardPage getNextPage() {
    	if (getWizard() == null) {
    		return null;
    	}
    	return getWizard().getPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_FLOWPAGE_DESC));	    	
	 }

}



