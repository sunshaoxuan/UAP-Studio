package nc.uap.lfw.application;

import org.eclipse.jface.wizard.WizardDialog;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;

public class ExcelAppNodeAction extends NodeAction{

	public ExcelAppNodeAction(){
		super(WEBPersConstants.EXCEL_APPLICATION, WEBPersConstants.EXCEL_APPLICATION);
	}
	public void run() {
		WizardDialog excelDialog = new WizardDialog(null, new NewAppByExcelWizard());
		if(NewAppByExcelWizard.OK) 
			excelDialog.open();
	}
}
