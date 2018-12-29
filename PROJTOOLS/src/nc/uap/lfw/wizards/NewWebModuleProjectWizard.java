package nc.uap.lfw.wizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBProjConstants;
import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.internal.project.IProjectProvider;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * LFW项目向导
 * 
 * @since 1.6
 */

public class NewWebModuleProjectWizard extends Wizard implements
		IExecutableExtension, INewWizard {

	/** 向导图片 */
	private static final ImageDescriptor DESC_NEWPPRJ_WIZ = WEBProjPlugin
			.loadImage(WEBProjPlugin.ICONS_PATH, "newpprj_wiz.gif");

	/** 向导主页面 */
	protected NewWebModuleProjectPage fMainPage;

	/** 访问器路径设置页面 */
	protected NewModuleWebContextPage fNewModuleWebPage;

	/** 选择新旧版本页面 */
	protected NewWebModuleEditorPage fNewModuleEditorPage;

	protected IProjectProvider fProjectProvider;

	private IConfigurationElement fConfig;

	private final String targetFolder = "web";

	private IProject project;

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public NewWebModuleProjectWizard() {
		setDefaultPageImageDescriptor(DESC_NEWPPRJ_WIZ);
		setWindowTitle("Create New LFW Project");
		fProjectProvider = new LFWProjectProvider();
	}

	/**
	 * 创建向导页面
	 */
	@Override
	public void addPages() {
		// 主页面
		fMainPage = new NewWebModuleProjectPage("Create New LFW Project");
		addPage(fMainPage);
		((LFWProjectProvider) fProjectProvider).setFMainPage(fMainPage);
		// next页面，访问器路径设置
		addPathPage();
		// next页面，选择新旧版本
		// fNewModuleEditorPage = new
		// NewWebModuleEditorPage(WEBProjConstants.KEY_NEWPRJ_MAINPAGE_TITLE);
		// addPage(fNewModuleEditorPage);
	}
	
	 public boolean canFinish(){
		 if(this.getContainer().getCurrentPage()==fMainPage&&fMainPage.isPageComplete())
			 return true;
		 return false;
	 }

	protected void addPathPage() {
		fNewModuleWebPage = new NewModuleWebContextPage(
				WEBProjConstants.KEY_NEWPRJ_MAINPAGE_TITLE);
		addPage(fNewModuleWebPage);
	}

	/**
	 * 完成时执行动作
	 */
	@Override
	public boolean performFinish() {

		BasicNewProjectResourceWizard.updatePerspective(fConfig);
		try {
			performBasicOperation();
			perLfwOperation();
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
	protected void performBasicOperation() throws InvocationTargetException,
			InterruptedException {
		getContainer().run(false, true,
				new NewProjectCreationOperation(fProjectProvider));
	}

	/**
	 * LFW相关设置
	 */
	@SuppressWarnings("static-access")
	protected void perLfwOperation() {
		String context = "";
		/* 主项目 */
		if (fNewModuleWebPage.getMainRadio().isSelected())
			context = fNewModuleWebPage.getMainPath().getText();
		else
			context = fNewModuleWebPage.getPartPath().getText();
		final String docbase = fMainPage.getProjectHandle().getLocation()
				.toString()
				+ "/web";
		System.setProperty("context", context);
		System.setProperty("editorVersion", "NEW_VERSION");
		try {
			if (createLFWFolder(docbase) && addLfwWebContextToProperty()
					&& writeModuleToProperties()) {
				Thread.currentThread().sleep(500);
				project.refreshLocal(IProject.DEPTH_INFINITE, null);
			}
		} catch (Exception e) {
			WEBProjPlugin.getDefault().logError(e);
		}
	}

	/**
	 * 将func_module和billtype_module存入system.properties文件
	 * 
	 * @return
	 */
	private boolean writeModuleToProperties() {
		// 从属项目时，不生成system.properties
		if (fNewModuleWebPage.getPartRadio().isSelected()) {
			return true;
		}
		String filePath = project.getLocation().toString() + "/" + targetFolder
				+ "/WEB-INF/conf/system.properties";
		String initContent = "func_module=" + System.getProperty("context")
				+ "\r\n" + "billtype_module=" + System.getProperty("context")
				+ "\r\n" + "login.provider=" + WEBProjConstants.LOGIN_PROVIDER;
		try {
			FileUtilities.saveFile(filePath, initContent.getBytes());
		} catch (Exception e) {
			WEBProjPlugin.getDefault().logError(
					"保存system.properties出错：" + e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * 拷贝lfw中的文件到本地工程中
	 * 
	 * @param docbase
	 * @return
	 */
	private boolean createLFWFolder(String docbase) {
		if (project == null)
			project = fMainPage.getProjectHandle();
		setProject(project);
		IFolder toFolder = project.getFolder(targetFolder);
		if (toFolder.exists())
			if (!MessageDialog.openQuestion(
					JavaPlugin.getActiveWorkbenchShell(), "信息",
					"Lfw web文件夹已经存在！覆盖？"))
				return false;
		try {
			// 两种途径拷贝 lfw工程所需的文件
			// String sourcePath = ProjCoreUtility.getWebSourcePath();
			// if (fNewModuleWebPage.getMainRadio().isSelected()){
			// String[] copyPaths = WEBCopyPathConstants.getWebCopyPath();
			// String fromPath = null;
			// String toPath = null;
			// for (int i = 0; i < copyPaths.length; i++) {
			// fromPath = sourcePath + "/" + copyPaths[i];
			// if (copyPaths[i].equals("WEB-INF/web.xml.copy"))
			// toPath = project.getLocation().toString() + "/" + targetFolder +
			// "/" + "WEB-INF/web.xml";
			// else
			// toPath = project.getLocation().toString() + "/" + targetFolder +
			// "/" + copyPaths[i];
			// FileUtilities.copyFile(fromPath, toPath);
			// }
			// }

			// 拷贝webbase下copynodes的所有文件夹及文件
			// String toDir = project.getLocation().toString() + "/" +
			// targetFolder + "/html/nodes";
			// String copyfiles = sourcePath + "/" + "copynodes";
			// FileUtilities.copyFileFromDir(toDir, copyfiles);

			// //主项目
			// if (fNewModuleWebPage.getMainRadio().isSelected()) {
			// // 替换web.xml文件中内容
			// String webtoPath = project.getLocation().toString() + "/" +
			// targetFolder + "/WEB-INF";
			// String filePath = webtoPath + "/web.xml";
			// String fileContent =
			// FileUtilities.fetchFileContent(filePath,"UTF-8");
			// if (fileContent.indexOf("<param-value>/lfw2</param-value>") !=
			// -1)
			// fileContent =
			// fileContent.replace("<param-value>/lfw2</param-value>","<param-value>/"
			// + fNewModuleWebPage.getMainPath().getText() + "</param-value>");
			// else
			// fileContent =
			// fileContent.replace("<param-value>/lfw</param-value>",
			// "<param-value>/"
			// + fNewModuleWebPage.getMainPath().getText() + "</param-value>");
			// byte[] content = fileContent.getBytes();
			// FileUtilities.saveFile(filePath, content);
			// }
			// //从属项目
			// else if (fNewModuleWebPage.getPartRadio().isSelected()) {
			// final String mainContext = fNewModuleWebPage.getPartPath()
			// .getText();
			// final String webPath = fMainPage.getProjectHandle()
			// .getLocation().toString()
			// + "/web/WEB-INF/";
			// try {
			// // 删除web.xml
			// File webFile = new File(webPath + "web.xml");
			// webFile.delete();
			//
			// // 生成portal.part
			// writePart(webPath, fMainPage.getModuleName(), mainContext);
			// } catch (Exception e) {
			// WEBProjPlugin.getDefault().logError(e);
			// }
			// }
		} catch (Exception e) {
			WEBProjPlugin.getDefault().logError("拷贝LFW文件出错：" + e.getMessage(),
					e);
			return false;
		}
		return true;
	}

	/**
	 * 向工程里的.module_prj文件中添加module.webContext属性，便于取得lfwweb的context
	 * 
	 * @return
	 */
	private boolean addLfwWebContextToProperty() {
		// /* 从属项目 context 为空 */
		// if (System.getProperty("context").equals(""))
		// return true;
		IFile jfile = project.getFile(".module_prj");
		File file = new File(jfile.getLocation().toString());
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		String tempString = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			int line = 1;
			while ((tempString = reader.readLine()) != null) {
				String[] v = tempString.split("\\=");
				if (!v[0].equals("module.webContext")) {
					sb.append(tempString + "\n");
				}
				line++;
			}
			reader.close();
			String initContent = sb.toString() + "module.webContext="
					+ System.getProperty("context") + "\n"
					+ "module.editorVersion="
					+ System.getProperty("editorVersion");
			ByteArrayInputStream stream = new ByteArrayInputStream(
					initContent.getBytes("8859_1"));
			if (jfile.exists())
				jfile.setContents(stream, false, false, null);
			stream.close();
		} catch (Exception e) {
			WEBProjPlugin.getDefault().logError(
					"修改.module_prj出错：" + e.getMessage(), e);
			return false;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e);
			}
		}
		return true;
	}

	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		fConfig = config;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
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
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		buffer.append("\n<web-app>");
		buffer.append("\n	<context-param>");
		buffer.append("\n		<param-name>ctxPath</param-name>");
		buffer.append("\n		<param-value>/" + module + "</param-value>");
		buffer.append("\n	</context-param>\n");
		buffer.append("\n	<context-param>");
		buffer.append("\n		<param-name>modules</param-name>");
		buffer.append("\n		<param-value>" + module + "</param-value>");
		buffer.append("\n	</context-param>");
		buffer.append("\n</web-app>");
		File f = new File(webPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		FileUtilities.saveFile(webPath + "/" + fMainPage.getModuleName() + "."
				+ mainContext + ".part", buffer.toString().getBytes("UTF-8"));

	}

}
