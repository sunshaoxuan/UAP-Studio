package nc.uap.lfw.template;

import java.awt.geom.CubicCurve2D;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class TemplateWizardDialog extends WizardDialog{

	public TemplateWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		setPageSize(800,350);
	}
	
	protected void buttonPressed(int buttonId){
		if(buttonId == IDialogConstants.NEXT_ID){
//			if(getCurrentPage() instanceof TempNextUsedWizardPage)
			if(nextButtonPressed()){
				super.buttonPressed(buttonId);
			}
		}
		else if(buttonId == IDialogConstants.BACK_ID){
			if(previousButtonPressed()){
				super.buttonPressed(buttonId);
			}
		}
		else{
			super.buttonPressed(buttonId);
		}
	}
	protected boolean nextButtonPressed(){
		IWizardPage currentPage = getWizard().getContainer().getCurrentPage();
		if(currentPage instanceof TempNextUsedWizardPage){
			return ((TempNextUsedWizardPage)currentPage).nextButtonClick();
		}
		return true;
	}
	protected boolean previousButtonPressed(){
		IWizardPage currentPage = getWizard().getContainer().getCurrentPage();
		if(currentPage instanceof TempNextUsedWizardPage){
			return ((TempNextUsedWizardPage)currentPage).previousButtonClick();
		}
		return true;
	}
}
