/**
 * TODO
 */
package nc.uap.lfw.template.mastersecondlyflow;


import nc.lfw.design.view.LFWWfmConnector;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_template;
import nc.uap.wfm.vo.WfmFlwCatVO;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * 创建模板window配置流程信息页面
 * @author guomq1
 * 2012-8-8
 */
public class TempFlowConfigPage extends WizardPage {
	
	private ComboDialogField flowType;
	private StringDialogField TypeName; 
	private StringDialogField TypeCode;
	 
//	private StringDialogField serviceClass;
	
	WfmFlwCatVO[] wfmFlwCatVos = null;
	private Application app = LFWAMCPersTool.getCurrentApplication();
	private String appid = app.getId();
	/**
	 * @param pageName
	 */
	public TempFlowConfigPage(String pageName) {
		super(pageName);
		setTitle(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_WINDOW_MAINPAGE_TITLE));
		setDescription(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_THDDPAGE_DESC));
		
	}

	public String getFlowCatPk(){
		String itemStr = flowType.getText();
		if (wfmFlwCatVos == null)
			return null;
		for (int i = 0; i < wfmFlwCatVos.length; i ++){
			if (itemStr.equals(wfmFlwCatVos[i].getCatname())){
				return wfmFlwCatVos[i].getPk_flwcat();
			}
		}
		return null;
	}
	
	public String getTypeName(){
		return TypeName.getText();
	}
	
	public String getTypeCode(){
		return TypeCode.getText();
	}
	
/*	public String getServiceClass(){
		return serviceClass.getText();
	}
*/
	 public IWizardPage getNextPage() {
    	if (getWizard() == null) {
    		return null;
    	}
		return getWizard().getPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_FLOWFORTHPAGE_DESC));
	
	 }

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout(3, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		
		wfmFlwCatVos = LFWWfmConnector.getWfmFlowCateQry();
		String[] wfmFlwCatVosStr = new String[wfmFlwCatVos.length];
		for(int i = 0;i<wfmFlwCatVos.length;i++){
		wfmFlwCatVosStr[i] = wfmFlwCatVos[i].getCatname();
		}
		flowType = new ComboDialogField(wfmFlwCatVos.length);
		flowType.setLabelText(M_template.TempFlowConfigPage_0);
		flowType.setItems(wfmFlwCatVosStr); 
		flowType.doFillIntoGrid(composite, 3);
		flowType.selectItem(M_template.TempFlowConfigPage_1);
		if(!M_template.TempFlowConfigPage_1.equals(flowType.getText())) flowType.selectItem(0);
		
		TypeName = new StringDialogField();
		TypeName.setLabelText(M_template.TempFlowConfigPage_2);
		TypeName.doFillIntoGrid(composite, 3);
		TypeName.setText(appid);
		
		TypeCode = new StringDialogField();
		TypeCode.setLabelText(M_template.TempFlowConfigPage_3);
		TypeCode.doFillIntoGrid(composite, 3);
		TypeCode.setText(appid);
		
	
/*		serviceClass = new StringDialogField();
		serviceClass.setLabelText("服务类");
		serviceClass.doFillIntoGrid(composite, 3);
		serviceClass.setText("nc.uap.wfm.dftimpl.DefaultFormOper");
*/
		setControl(composite);
	}
	


}


