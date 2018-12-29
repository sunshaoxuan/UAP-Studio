package nc.uap.lfw.ref.page;

import org.eclipse.jface.wizard.IWizardPage;

public interface INewRefWizardPage extends IWizardPage {

	/**
	 * 更新界面值到模型中。
	 */
	void updateModel();
	
}
