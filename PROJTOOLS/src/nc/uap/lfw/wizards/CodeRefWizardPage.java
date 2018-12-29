package nc.uap.lfw.wizards;

import nc.uap.lfw.lang.M_wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CodeRefWizardPage extends WizardPage {

	protected CodeRefWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		Label label = new Label(parent, SWT.NULL);
		label.setText(M_wizards.CodeRefWizardPage_0);
		setControl(label);
	}

}
