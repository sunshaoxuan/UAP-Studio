package nc.uap.lfw.template;

import org.eclipse.jface.wizard.WizardPage;

public abstract class TempNextUsedWizardPage extends WizardPage{
	protected TempNextUsedWizardPage(String pageName){
		super(pageName);
	}
	protected abstract boolean nextButtonClick();
	protected abstract boolean previousButtonClick();

}
