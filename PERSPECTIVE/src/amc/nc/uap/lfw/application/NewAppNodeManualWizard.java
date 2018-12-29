/**
 * 手动发布应用
 */
package nc.uap.lfw.application;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * 手动发布应用
 * @author guomq1
 * 2012-8-14
 */
public class NewAppNodeManualWizard implements IExecutableExtension, INewWizard {

	public static final boolean OK = true;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#createPageControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPageControls(Composite pageContainer) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getContainer()
	 */
	@Override
	public IWizardContainer getContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getDefaultPageImage()
	 */
	@Override
	public Image getDefaultPageImage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getDialogSettings()
	 */
	@Override
	public IDialogSettings getDialogSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getPage(java.lang.String)
	 */
	@Override
	public IWizardPage getPage(String pageName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getPageCount()
	 */
	@Override
	public int getPageCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getPages()
	 */
	@Override
	public IWizardPage[] getPages() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getPreviousPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getStartingPage()
	 */
	@Override
	public IWizardPage getStartingPage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getTitleBarColor()
	 */
	@Override
	public RGB getTitleBarColor() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getWindowTitle()
	 */
	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#isHelpAvailable()
	 */
	@Override
	public boolean isHelpAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#needsPreviousAndNextButtons()
	 */
	@Override
	public boolean needsPreviousAndNextButtons() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#needsProgressMonitor()
	 */
	@Override
	public boolean needsProgressMonitor() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performCancel()
	 */
	@Override
	public boolean performCancel() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#setContainer(org.eclipse.jface.wizard.IWizardContainer)
	 */
	@Override
	public void setContainer(IWizardContainer wizardContainer) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 */
	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// TODO Auto-generated method stub

	}

}
