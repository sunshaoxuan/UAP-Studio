package nc.uap.lfw.wizards;

import java.lang.reflect.InvocationTargetException;

import nc.uap.lfw.core.WEBProjPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public class NewCodeRefProjectWizard extends Wizard implements
		IExecutableExtension, INewWizard {
	/** 向导图片*/
	private static final ImageDescriptor DESC_NEWPPRJ_WIZ = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "newpprj_wiz.gif");
	private IConfigurationElement fConfig;
	public NewCodeRefProjectWizard(){
		setDefaultPageImageDescriptor(DESC_NEWPPRJ_WIZ);
		setWindowTitle("Create New LFW Project");
	}
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		fConfig = config;
	}

	/**
	 * 创建向导页面
	 */
	@Override
	public void addPages() {
		//主页面
		CodeRefWizardPage fMainPage = new CodeRefWizardPage("Create New LFW Project");
		addPage(fMainPage);
		// next页面，访问器路径设置
//		fNewModuleEditorPage = new NewWebModuleEditorPage(WEBProjConstants.KEY_NEWPRJ_MAINPAGE_TITLE);
//		addPage(fNewModuleEditorPage);
	}
	
	/**
	 * 完成时执行动作
	 */
	@Override
	public boolean performFinish() {
		BasicNewProjectResourceWizard.updatePerspective(fConfig);
		try {
			performBasicOperation();
		} catch (InvocationTargetException e) {
			WEBProjPlugin.getDefault().logError(e);
		} catch (InterruptedException e) {
			WEBProjPlugin.getDefault().logError(e);
		}
		return true;
	}

	/**
	 * 项目基本设置
	 * 
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	private void performBasicOperation() throws InvocationTargetException,InterruptedException {
		getContainer().run(false, true, new NewRefCodeProjectCreationOperation());
	}
}
