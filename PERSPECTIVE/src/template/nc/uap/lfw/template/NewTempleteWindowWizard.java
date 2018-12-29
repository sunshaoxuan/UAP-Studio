/**
 * 
 */
package nc.uap.lfw.template;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.application.ManualAppNodeAction;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.template.mastersecondly.TempGeneResultPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.progress.UIJob;

/**
 * @author guomq1
 * 
 */
public class NewTempleteWindowWizard extends Wizard implements
		IExecutableExtension, INewWizard {

	/** 向导图片 */
//	private static final ImageDescriptor DESC_NEWPPRJ_WIZ = WEBPersPlugin.loadImage(WEBPersPlugin.ICONS_PATH, "newpprj_wiz.gif");

	protected CreateTempWindowPage tempPage;

	private IConfigurationElement fConfig;
	private ShowFilesWillBeAddedPage showFilesWillBeAddedPage;

	private String appid = null;

	private int index = 0;

	private Map<String, ITemplatePageFactory> factoryMap = null;

	private boolean flag = false;
	
	private boolean jobsucess = false;
//	private static String BASE_PATH = "/web/html/nodes/";

	
	public NewTempleteWindowWizard(){
		super();
		setNeedsProgressMonitor(true);
	}
	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the showFilesWillBeAddedPage
	 */
	public ShowFilesWillBeAddedPage getShowFilesWillBeAddedPage() {
		return showFilesWillBeAddedPage;
	}

	/**
	 * @param showFilesWillBeAddedPage
	 *            the showFilesWillBeAddedPage to set
	 */
	public void setShowFilesWillBeAddedPage(
			ShowFilesWillBeAddedPage showFilesWillBeAddedPage) {
		this.showFilesWillBeAddedPage = showFilesWillBeAddedPage;
	}

	@Override
	public boolean performFinish() {
		
		final NewTempleteWindowWizard wizard = this;
		final String title = tempPage.getTempTitle();

//		IRunnableWithProgress op = new IRunnableWithProgress() {
//			
//			@Override
//			public void run(IProgressMonitor monitor) throws InvocationTargetException,
//					InterruptedException {
//				try{
//					monitor.beginTask("模式化生成中....", IProgressMonitor.UNKNOWN);
//					Display.getDefault().syncExec(new Runnable() {					
//					@Override
//					public void run() {
//						ITemplatePageFactory factory = factoryMap.get(title);
//						flag = factory.finish(wizard);		
//					}
//				});	
//				}catch(Exception e){
//					MainPlugin.getDefault().logError(e.getMessage(),e);
//				}
//				finally{
//					monitor.done();
//				}
//			}
//		};
//		try{
//			getContainer().run(true, false, op);
//		}
//		catch(Exception e){
//			return false;
//		}
//		return flag;
		if(MessageDialog.openConfirm(Display.getDefault().getActiveShell(), M_template.MasterSecondlyWindowAction_1, M_template.MasterSecondlyWindowAction_2)){
			ManualAppNodeAction manualAppNodeAction = new ManualAppNodeAction();
			manualAppNodeAction.run();
		}
		return true;
	}
	 public boolean performCancel(){		 
		 if (this.getContainer().getCurrentPage() == getPage(WEBPersConstants.KEY_TEMP_RESULT_DESC)&&!this.getContainer().getCurrentPage().isPageComplete()){
				return false;
			}
		 else
			 return true;
	 }
	

	/**
	 * 创建向导页面
	 */
	@Override
	public void addPages() {
		appid = LFWAMCPersTool.getCurrentApplication().getId();
		ArrayList<String> tempList = new ArrayList<String>();
		ArrayList<Image> imgList = new ArrayList<Image>();
		Map<String, WizardPage> firstPageMap = new HashMap<String, WizardPage>();
		factoryMap = new HashMap<String, ITemplatePageFactory>();
		try {
			IExtensionPoint extPoint = Platform.getExtensionRegistry().getExtensionPoint("com.yonyou.studio.web.perspective.template");
			if (extPoint != null) {
				IExtension[] extensions = extPoint.getExtensions();
				for (IExtension extension : extensions) {
					ITemplatePageFactory factory = (ITemplatePageFactory) extension.getConfigurationElements()[0].createExecutableExtension("class");
					factory.setAppId(appid);
					int num = factory.getPageCount();
					for (int i = 0; i < num; i++) {
						WizardPage page = factory.initPage(i);
						if (i == 0) {
							firstPageMap.put(factory.getTemplateTitle(), page);
						}
						addPage(page);
					}
					tempList.add(factory.getTemplateTitle());
					imgList.add(factory.getPreviewImage());
					factoryMap.put(factory.getTemplateTitle(), factory);
				}
			}
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage());
		}
		tempPage = new CreateTempWindowPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_WINDOW_MAINPAGE_TITLE));
		tempPage.setTemplateList(tempList);
		tempPage.setImageList(imgList);
		tempPage.setPageMap(firstPageMap);
		showFilesWillBeAddedPage = new ShowFilesWillBeAddedPage(WEBPersConstants.KEY_TEMP_FILESCONFIG_DESC);
		addPage(tempPage);
		addPage(showFilesWillBeAddedPage);

	}

	public IWizardPage getStartingPage() {
		return (IWizardPage) getPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_WINDOW_MAINPAGE_TITLE));
	}

	public boolean canFinish() {
		if (this.getContainer().getCurrentPage() == getPage(WEBPersConstants.KEY_TEMP_RESULT_DESC)&&this.getContainer().getCurrentPage().isPageComplete()){
			return true;
		}
		else
			return false;
	}

	@Override
	public void setInitializationData(IConfigurationElement config,String propertyName, Object data) throws CoreException {
		fConfig = config;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
	}

}
