package nc.uap.lfw.wizards;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBProjConstants;
import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.internal.bcp.BCPManifest;
import nc.uap.lfw.internal.bcp.BusinessComponent;
import nc.uap.lfw.internal.util.BCPUtils;
import nc.uap.lfw.internal.util.ProjCoreUtility;
import nc.uap.lfw.lang.M_wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * BCP组件添加向导
 * 
 * @author qinjianc
 * 
 */
public class NewBcpModuleWizard extends Wizard implements IExecutableExtension,
		INewWizard {
	/** 向导图片 */
	private static final ImageDescriptor DESC_NEWPPRJ_WIZ = WEBProjPlugin
			.loadImage(WEBProjPlugin.ICONS_PATH, "bcp-icon.gif"); //$NON-NLS-1$
	private IProject project;

	/** 访问器路径设置页面 */
	protected NewBcpModulePage fNewBcpModulePage;

	public NewBcpModuleWizard() {
		setDefaultPageImageDescriptor(DESC_NEWPPRJ_WIZ);
		setWindowTitle(M_wizards.NewBcpModuleWizard_0);
	}
	public NewBcpModuleWizard(IProject pluginProject) {
		setDefaultPageImageDescriptor(DESC_NEWPPRJ_WIZ);
		setWindowTitle(M_wizards.NewBcpModuleWizard_1);
		this.project = pluginProject;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof IAdaptable) {
			project = (IProject) ((IAdaptable) firstElement)
					.getAdapter(IProject.class);
			if ((project == null) && (firstElement instanceof IJavaElement))
				project = ((IJavaElement) firstElement).getJavaProject()
						.getProject();
			// this.bcProject = CoreUtility.createBCProject(project);
		}
	}

	/**
	 * 创建向导页面
	 */
	@Override
	public void addPages() {
		if(project==null){
			MessageDialog.openError(null, "错误", "请在JAVA视图下选中一个工程或在LFW视图下右键点击工程创建组件");
			return;
		}
		fNewBcpModulePage = new NewBcpModulePage(
				WEBProjConstants.KEY_NEWPRJ_MAINPAGE_TITLE);
		fNewBcpModulePage.setProject(project);
		addPage(fNewBcpModulePage);
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean performFinish() {
		// Thread.currentThread().setContextClassLoader(XStream.class.getClassLoader());
		// XStream xstream = new XStream();
		String componentName = this.fNewBcpModulePage.getAddNewBCPComposite()
				.getBcpName();
		String componentdisplayName = this.fNewBcpModulePage
				.getAddNewBCPComposite().getBcpDisplay();
		if(componentdisplayName.equals("")||componentName.equals("")){
			MessageDialog.openInformation(null, "提示", "请输入完整的组件信息");
			return false;
		}
		boolean createDefaultFolders = this.fNewBcpModulePage
				.getAddNewBCPComposite().getSelection();
		BusinessComponent businessComponent = new BusinessComponent();
		businessComponent.setComponentName(componentName);
		businessComponent.setComponentDisplayName(componentdisplayName);
		try {
			if (createDefaultFolders) {
				IFile manifestFile = project.getFile("manifest.xml"); //$NON-NLS-1$
				BCPManifest bcpmanifest = (BCPManifest) BCPUtils
						.read(manifestFile);
				if (bcpmanifest == null)
					bcpmanifest = new BCPManifest();
				bcpmanifest.addBusinessComponent(businessComponent);
				BCPUtils.writeObjectXml2File(manifestFile, bcpmanifest,
						JavaPlugin.getActiveWorkbenchShell(), null);
				
				BCPUtils.createDefaultSourceableFolers(businessComponent);

				List allSourceFolderList = new ArrayList();
				allSourceFolderList.addAll(BCPUtils.checkBCFolder(project,
						null, businessComponent));
				if (!LfwCommonTool.getModuleProperty(project, "module.name") //$NON-NLS-1$
						.equals(LfwCommonTool.getModuleProperty(project,
								"module.webContext"))) { //$NON-NLS-1$
					String path = project.getLocation().toString() + "/" //$NON-NLS-1$
							+ businessComponent.getComponentName()
							+ "/web/WEB-INF/"; //$NON-NLS-1$
					File folder = new File(path);
					if (!folder.exists())
						folder.mkdirs();
					writePart(path, componentName,
							LfwCommonTool.getModuleProperty(project,
									"module.webContext")); //$NON-NLS-1$
					String metadatapath =project.getLocation().toString() + "/" //$NON-NLS-1$
					+ businessComponent.getComponentName()+"/METADATA/"; //$NON-NLS-1$
					File metadatafolder = new File(metadatapath);
					if(!metadatafolder.exists()){
						metadatafolder.mkdirs();
					}
				}
				project.refreshLocal(2, null);
				BCPUtils.rebuildBCPProject(project, allSourceFolderList, null);
				
				if (!project.hasNature(WEBProjConstants.BCP_MODULE_NATURE))
					ProjCoreUtility.addNatureToProject(project,
							WEBProjConstants.BCP_MODULE_NATURE, null);
//				if(!project.hasNature(WEBProjConstants.BCP_MDE_MODULE_NATURE))
//					ProjCoreUtility.addNatureToProject(project,
//							WEBProjConstants.BCP_MDE_MODULE_NATURE, null);

				return true;
			}
		} catch (Exception e) {
			WEBProjPlugin.getDefault().logError(e);
		}

		return false;

		// this.bcProject.getManifest().addBusinessComponent(businessComponent);
		// try
		// {
		// this.bcProject.save();
		// return true;
		// }
		// catch (CoreException e)
		// {
		// LogUtility.log(e);
		// }
		// catch (IOException e)
		// {
		// LogUtility.log(e);
		// }
		// return false;
	}

	/**
	 * 生成*.part
	 * 
	 * @param webPath
	 *            WEB-INF 路径
	 * @param module
	 *            模块名
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	protected void writePart(String webPath, String module, String mainContext)
			throws UnsupportedEncodingException, Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"); //$NON-NLS-1$
		buffer.append("\n<web-app>"); //$NON-NLS-1$
		buffer.append("\n	<context-param>"); //$NON-NLS-1$
		buffer.append("\n		<param-name>ctxPath</param-name>"); //$NON-NLS-1$
		buffer.append("\n		<param-value>/" + module + "</param-value>"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("\n	</context-param>\n"); //$NON-NLS-1$
		buffer.append("\n	<context-param>"); //$NON-NLS-1$
		buffer.append("\n		<param-name>modules</param-name>"); //$NON-NLS-1$
		buffer.append("\n		<param-value>" + module + "</param-value>"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("\n	</context-param>"); //$NON-NLS-1$
		buffer.append("\n</web-app>"); //$NON-NLS-1$
		File f = new File(webPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		FileUtilities.saveFile(webPath + "/" + module + "." + mainContext //$NON-NLS-1$ //$NON-NLS-2$
				+ ".part", buffer.toString().getBytes("UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$

	}

}
