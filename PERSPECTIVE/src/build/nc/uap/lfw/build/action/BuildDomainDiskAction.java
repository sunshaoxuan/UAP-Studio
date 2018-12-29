package nc.uap.lfw.build.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.dbcreate.impl.DbCreateServiceImpl;
import nc.uap.lfw.build.dbcreate.itf.IDbCreateService;
import nc.uap.lfw.build.dbrecord.impl.DbRecordServiceImpl;
import nc.uap.lfw.build.dbrecord.itf.DbRecordConfig;
import nc.uap.lfw.build.dbrecord.itf.DbRecordSqlFileCfg;
import nc.uap.lfw.build.dbrecord.itf.IDbRecordScript;
import nc.uap.lfw.build.dbrecord.itf.IDbRecordService;
import nc.uap.lfw.build.dbrecord.itf.MainTableRecordItem;
import nc.uap.lfw.build.pub.util.pdm.dbrecord.DBRecordConfigUtil;
import nc.uap.lfw.build.pub.util.pdm.vo.Item;
import nc.uap.lfw.build.pub.util.pdm.vo.ItemSet;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.common.XmlCommonTool;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.lang.M_build;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import com.yonyou.uap.studio.*;
import com.yonyou.uap.studio.connection.ConnectionService;
import com.yonyou.uap.studio.connection.exception.ConnectionException;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.taskdefs.Expand;
import org.eclipse.ant.internal.ui.launchConfigurations.AntLaunchShortcut;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BuildDomainDiskAction extends NodeAction implements IObjectActionDelegate {

	private ArrayList<String> selModuleList = new ArrayList<String>();

	private HashMap<String, ArrayList<String>> dependMapping = new HashMap<String, ArrayList<String>>();

	private String diskpath;

	private String nchome;

	private String domain = null;

	public BuildDomainDiskAction(LFWBasicTreeItem item) {
		super(M_build.BuildDomainDiskAction_0);
		this.domain = item.getText();
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void run() {
		buildDomainDisk();
	}

	@Override
	public void run(IAction action) {
		buildDomainDisk();
	}

	public void buildDomainDisk() {
		Shell shell = Display.getCurrent().getActiveShell();
		BuildDomainDiskDialog dialog = new BuildDomainDiskDialog(shell, M_build.BuildDomainDiskAction_0);
		if (dialog.open() == IDialogConstants.OK_ID) {
			String type = dialog.getType();
			diskpath = dialog.getDiskLocation();
			nchome = LfwCommonTool.getUapHome();
			if (type.equals("compile")) { //$NON-NLS-1$
				try {
					compileModules();
					MessageDialog.openInformation(null, M_build.BuildDomainDiskAction_1, M_build.BuildDomainDiskAction_2);
				} catch (Exception e) {
					MessageDialog.openError(null, M_build.BuildDomainDiskAction_3, M_build.BuildDomainDiskAction_4);
					MainPlugin.getDefault().logError(e.getMessage());
				}
			} else {
				Job job = new Job(M_build.CreateDiskAction_0) {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						try {
							// item =
							// (LFWBuildTreeItem)LFWPersTool.getCurrentTreeItem();
							String domain = getDomain();
							monitor.beginTask(M_build.CreateDiskAction_0, 5);
							File folderTemp = new File("C:/buildFolder"); //$NON-NLS-1$
							FileUtilities.deleteFile(folderTemp);
							folderTemp.mkdirs();
							rebuildInstallTemp();
							monitor.worked(1);
							compileModules();
							monitor.worked(1);
							dbcreate();
							initscript();
							monitor.worked(1);
							antBuild();
							monitor.worked(1);
							finish();
						} catch (Exception e) {
							MainPlugin.getDefault().logError(e.getMessage());
						}
						monitor.worked(1);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							WEBPersPlugin.getDefault().logError(e);
						}
						monitor.done();
						return Status.OK_STATUS;
					}

				};
				job.schedule();
			}
		}
	}

	/**
	 * 将安装模板从插件中导出，并解压。
	 */
	private void rebuildInstallTemp() {

		String domain = getDomain();
		File jarFile = new File("C:/buildFolder/uap_install.zip"); //$NON-NLS-1$ //$NON-NLS-2$
		if (jarFile.exists())
			jarFile.delete();
		File folder = new File(diskpath);
		if (!folder.exists())
			folder.mkdirs();
		for (File disk : folder.listFiles()) {
			if (disk.getName().equals("uap_install")) //$NON-NLS-1$
				FileUtilities.deleteFile(disk);
		}
		String antPath = "installdisk/uap_install.zip"; //$NON-NLS-1$	
		InputStream ins = null;
		FileOutputStream install = null;
		try {
			ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(antPath);
			install = new FileOutputStream(jarFile);
			int data = 0;
			byte[] b = new byte[1638400];
			while (true) {
				data = ins.read(b, 0, 1638400);
				if (data == -1) {
					break;
				}
				install.write(b, 0, data);
			}
			install.flush();
			Project project = new Project();
			Expand expand = new Expand();
			expand.setProject(project);
			expand.setSrc(jarFile); //$NON-NLS-1$
			expand.setDest(new File(diskpath));
			expand.setOverwrite(true);
			expand.execute();
			// FileUtilities.deleteFile(new File("C:/buildFolder"));
		} catch (Exception e) {
			WEBPersPlugin.getDefault().logError(e);
		} finally {

			try {
				if (ins != null)
					ins.close();
			} catch (IOException e) {
				MainPlugin.getDefault().logError(e);
			}
			try {
				if (install != null)
					install.close();
			} catch (IOException e) {
				MainPlugin.getDefault().logError(e);
			}
		}

	}

	/**
	 * 生成建库脚本
	 */
	private void dbcreate() {
		if (selModuleList.size() == 0) {
			JOptionPane.showMessageDialog(null,M_build.BuildDomainDiskAction_5);
			return;
		}
		IProject[] projects = LFWPersTool.getOpenedBcpJavaProjects();
		for (String moduleName : selModuleList) {
			IProject project = LFWPersTool.getProjectByName(projects, moduleName);
			String[] bcpNames = LfwCommonTool.getBCPNames(project);
			File dbcreateFolder = new File("C:/buildFolder/" + LFWPersTool.getProjectModuleName(project) + "/script"); //$NON-NLS-1$ //$NON-NLS-2$
			if (!dbcreateFolder.exists())
				dbcreateFolder.mkdirs();
			for (String bcpName : bcpNames) {
				IFile folder = project.getFile(bcpName + "/ncscript/conf/pdm"); //$NON-NLS-1$
				File realFolder = folder.getLocation().toFile();
				if (realFolder != null && realFolder.exists()) {
					File sqlRoot = new File(dbcreateFolder, "dbcreate"); //$NON-NLS-1$
					File[] files = realFolder.listFiles();
					for (File file : files) {
						IDbCreateService dbservice = new DbCreateServiceImpl();
						dbservice.geneSqlFile(file, true, sqlRoot);
					}
				} else
					MainPlugin.getDefault().logError(M_build.BuildDomainDiskAction_6);

			}
		}

	}

	/**
	 * 生成数据库初始化脚本
	 */
	private void initscript() {

		String server = ""; //$NON-NLS-1$
		String driver = ""; //$NON-NLS-1$
		String port = ""; //$NON-NLS-1$

		
		Connection conn = null;
		try {
			conn = ConnectionService.getConnection();
		} catch (ConnectionException e1) {
			MainPlugin.getDefault().logError(e1.getMessage(),e1);
		}

		if (selModuleList.size() == 0) {
			MessageDialog.openWarning(null, "提示", M_build.BuildDomainDiskAction_7); //$NON-NLS-1$
			return;
		}
		IProject[] projects = LFWPersTool.getOpenedBcpJavaProjects();
		for (String moduleName : selModuleList) {
			IProject project = LFWPersTool.getProjectByName(projects, moduleName);
			String[] bcpNames = LfwCommonTool.getBCPNames(project);
			DBRecordConfigUtil recordUtil = new DBRecordConfigUtil(true);
			File initFolder = new File("C:/buildFolder/" + LFWPersTool.getProjectModuleName(project) + "/init/"); //$NON-NLS-1$ //$NON-NLS-2$
			if (!initFolder.exists())
				initFolder.mkdirs();
			for (String bcpName : bcpNames) {
				File scriptFile = project.getFile(bcpName + "/ncscript/conf/init/items.xml").getLocation() //$NON-NLS-1$
						.toFile();
				File mapFile = project.getFile(bcpName + "/ncscript/conf/module.map") //$NON-NLS-1$
						.getLocation().toFile();
				if (scriptFile != null && scriptFile.exists()) {
					Object node;
					try {
						node = recordUtil.getDBRecordItemDef(scriptFile.getPath());
						File sqlFolder = new File(initFolder, bcpName + "/business"); //$NON-NLS-1$
						if (!sqlFolder.exists())
							sqlFolder.mkdirs();

						ItemSet itemSet = (ItemSet) node;
						Item[] items = itemSet.getItem();
						for (Item itemNode : items) {
							MainTableRecordItem mainRecordItem = new MainTableRecordItem();
							mainRecordItem.setItemKey(itemNode.getItemKey());
							mainRecordItem.setTableName(itemNode.getItemRule());
							mainRecordItem.setTableDesc(itemNode.getItemName());
							mainRecordItem.setGrpField(itemNode.getGrpField());
							mainRecordItem.setWhereCondition(itemNode.getFixedWhere());
							DbRecordConfig conf = new DbRecordConfig();
							conf.setMainTableItem(mainRecordItem);
							conf.setCommonMultiTableFileName(mapFile.getParentFile().getPath() + "/common.map"); //$NON-NLS-1$
							conf.setModuleMultiTableFileName(mapFile.getPath());
							DbRecordSqlFileCfg sqlCfg = new DbRecordSqlFileCfg(sqlFolder.getPath(), mapFile.getPath(), mapFile.getPath());
							IDbRecordService recordService = new DbRecordServiceImpl();
							IDbRecordScript script = recordService.retrieveDBRecordScript(conf, conn);
							recordService.geneSqlFile(script, conn, sqlCfg, null);
							
						}

					} catch (Exception e) {
						WEBPersPlugin.getDefault().logError(e);
					}
				} else
					MainPlugin.getDefault().logError(M_build.BuildDomainDiskAction_8);

			}
		}
	}

	/**
	 * @throws IOException
	 */
	private void compileModules() throws Exception {
		// File antFile = new File("C:/build.xml");
		String antpath = "templates/buildScript/build.xml"; //$NON-NLS-1$
		InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(antpath);
		String content = FileUtilities.fetchFileContent(ins, "GBK"); //$NON-NLS-1$
		ins.close();
		content = content.replace("${diskpath}", diskpath); //$NON-NLS-1$
		content = content.replace("${nchome}", nchome); //$NON-NLS-1$
		getDomainDetail();

		IProject[] projects = LFWPersTool.getOpenedBcpJavaProjects();
		Document currentdoc = null;

		File scriptFolder = new File("C:/antScript"); //$NON-NLS-1$
		if (scriptFolder.exists())
			FileUtilities.deleteFile(scriptFolder);
		scriptFolder.mkdirs();
		File buildFolder = new File("C:/buildFolder/"); //$NON-NLS-1$
		if (buildFolder.exists())
			FileUtilities.deleteFile(buildFolder);
		buildFolder.mkdirs();

		File compileFolder = new File("C:/tempcompile"); //$NON-NLS-1$
		if (compileFolder.exists())
			FileUtilities.deleteFile(compileFolder);
		File domainScript = new File(scriptFolder, "domain.xml"); //$NON-NLS-1$
		Document doc = XmlCommonTool.createDocument();
		Element rootNode = doc.createElement("project"); //$NON-NLS-1$
		rootNode.setAttribute("name", "domainCompileTask"); //$NON-NLS-1$ //$NON-NLS-2$
		rootNode.setAttribute("default", "compile"); //$NON-NLS-1$ //$NON-NLS-2$
		doc.appendChild(rootNode);
		Element targetNode = doc.createElement("target"); //$NON-NLS-1$
		targetNode.setAttribute("name", "compile"); //$NON-NLS-1$ //$NON-NLS-2$
		rootNode.appendChild(targetNode);

		for (String projectName : selModuleList) {
			// FileUtilities.saveFile(antFile, content, "GBK");
			// currentdoc = XmlCommonTool.parseXML(antFile);
			// // currentdoc = doc;
			// IProject selProject = LFWPersTool.getProjectByName(projects,
			// projectName);
			// currentdoc = createBuildAnt(selProject, currentdoc);
			// currentdoc = createDeployAnt(selProject, currentdoc);
			//
			// XmlCommonTool.documentToXml(currentdoc, antFile);
			// Project project = new Project();
			// DefaultLogger consoleLogger = new DefaultLogger();
			// consoleLogger.setErrorPrintStream(System.err);
			// consoleLogger.setOutputPrintStream(System.out);
			// consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
			// project.addBuildListener(consoleLogger);
			//
			// project.fireBuildStarted();
			// project.init();
			// ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
			// projectHelper.parse(project, antFile);
			//
			// project.executeTarget("deploy");
			// project.fireBuildFinished(null);

			File antFile = new File(scriptFolder, projectName + ".xml"); //$NON-NLS-1$
			FileUtilities.saveFile(antFile, content, "GBK"); //$NON-NLS-1$
			currentdoc = XmlCommonTool.parseXML(antFile);
			IProject selProject = LFWPersTool.getProjectByName(projects, projectName);
			currentdoc = createBuildAnt(selProject, currentdoc);
			currentdoc = createDeployAnt(selProject, currentdoc);

			XmlCommonTool.documentToXml(currentdoc, antFile);

			Element antNode = doc.createElement("ant"); //$NON-NLS-1$
			antNode.setAttribute("target", "deploy"); //$NON-NLS-1$ //$NON-NLS-2$
			antNode.setAttribute("antfile", projectName + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
			targetNode.appendChild(antNode);
		}

		XmlCommonTool.documentToXml(doc, domainScript);
		IPath path = new Path(domainScript.getAbsolutePath());
		ILaunchConfiguration configuration = AntLaunchShortcut.createDefaultLaunchConfiguration(path, null);
		ILaunchConfigurationWorkingCopy workingCopy = null;
        workingCopy = configuration.getWorkingCopy();
        workingCopy.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING,"UTF-8");  //$NON-NLS-1$
        workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xms64m -Xmx512m"); //$NON-NLS-1$
        ILaunch launch = workingCopy.launch("run", null);  //$NON-NLS-1$
        while(!launch.isTerminated()){
        	Thread.sleep(2000);
        }
        configuration.delete();
        workingCopy.delete();

	}

	public void getDomainDetail() {
		String domainId = getDomain();
		IPath workpath = ResourcesPlugin.getWorkspace().getRoot().getLocation().append("domain").append(domainId); //$NON-NLS-1$
		// File buildFile = new File("C:/setup.ini");
		File xmlFile = new File(workpath.toFile(), "domain.xml"); //$NON-NLS-1$
		if (xmlFile.exists()) {
			Document doc = XmlCommonTool.parseXML(xmlFile);
			NodeList list = doc.getElementsByTagName("project"); //$NON-NLS-1$
			for (int i = 0; i < list.getLength(); i++) {
				Element projectEle = (Element) list.item(i);
				String projectId = projectEle.getAttribute("id"); //$NON-NLS-1$
				selModuleList.add(projectId);
				NodeList dependlist = projectEle.getElementsByTagName("module"); //$NON-NLS-1$
				ArrayList<String> depends = new ArrayList();
				for (int j = 0; j < dependlist.getLength(); j++) {
					Element moduleEle = (Element) dependlist.item(j);
					String moduleId = moduleEle.getAttribute("id"); //$NON-NLS-1$
					depends.add(moduleId);
				}
				if (depends.size() > 0) {
					dependMapping.put(projectId, depends);
				}
			}
		} else {
			MessageDialog.openWarning(null, M_build.BuildDomainDiskAction_9, M_build.BuildDomainDiskAction_10);
		}
	}

	public Document createBuildAnt(IProject project, Document doc) {
		String moduleName = LFWPersTool.getProjectModuleName(project);

		ArrayList<String> dependPubJarList = new ArrayList<String>();
		ArrayList<String> dependJarList = new ArrayList<String>();
		IProject[] projects = LFWPersTool.getOpenedBcpJavaProjects();

		Element projNode = (Element) doc.getElementsByTagName("project").item(0); //$NON-NLS-1$
		Element pathNode = (Element) projNode.getElementsByTagName("path").item(0); //$NON-NLS-1$
		Element targetNode = (Element) projNode.getElementsByTagName("target").item(1); //$NON-NLS-1$
		Element deployNode = (Element) projNode.getElementsByTagName("target").item(2); //$NON-NLS-1$

		projNode.setAttribute("default", "deploy"); //$NON-NLS-1$ //$NON-NLS-2$
		ArrayList<String> dependModules = dependMapping.get(project.getName());
		if (dependModules != null && dependModules.size() > 0) {
			for (String module : dependModules) {
				Element filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
				filesetNode.setAttribute("dir", "${compiledir}/" + LFWPersTool.getProjectModuleName(LFWPersTool.getProjectByName(projects, module)) + "/lib"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				pathNode.appendChild(filesetNode);
				Element includeNode = doc.createElement("include"); //$NON-NLS-1$
				includeNode.setAttribute("name", "**/*.jar"); //$NON-NLS-1$ //$NON-NLS-2$
				filesetNode.appendChild(includeNode);
			}
		}
		Element propertyNode = doc.createElement("property"); //$NON-NLS-1$
		propertyNode.setAttribute("name", "base_" + moduleName); //$NON-NLS-1$ //$NON-NLS-2$
		propertyNode.setAttribute("value", project.getLocation().toOSString()); //$NON-NLS-1$
		projNode.insertBefore(propertyNode, pathNode);
		
		propertyNode = doc.createElement("property"); //$NON-NLS-1$
		propertyNode.setAttribute("name", "module"); //$NON-NLS-1$ //$NON-NLS-2$
		propertyNode.setAttribute("value", moduleName); //$NON-NLS-1$
		projNode.insertBefore(propertyNode, pathNode);

		propertyNode = doc.createElement("property"); //$NON-NLS-1$
		propertyNode.setAttribute("name", "tempdir"); //$NON-NLS-1$ //$NON-NLS-2$
		propertyNode.setAttribute("value", "${compiledir}/" + moduleName); //$NON-NLS-1$ //$NON-NLS-2$
		projNode.insertBefore(propertyNode, pathNode);

		String bcpNames[] = LfwCommonTool.getBCPNames(project);

		for (String bcp : bcpNames) {
			propertyNode = doc.createElement("property"); //$NON-NLS-1$
			propertyNode.setAttribute("name", bcp); //$NON-NLS-1$
			propertyNode.setAttribute("value", "${base_" + moduleName + "}\\" + bcp); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			projNode.insertBefore(propertyNode, pathNode);
			propertyNode = doc.createElement("property"); //$NON-NLS-1$
			propertyNode.setAttribute("name", bcp + "_src"); //$NON-NLS-1$ //$NON-NLS-2$
			propertyNode.setAttribute("value", "${" + bcp + "}\\src"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			projNode.insertBefore(propertyNode, pathNode);

			String depends = targetNode.getAttribute("depends"); //$NON-NLS-1$
			targetNode.setAttribute("depends", depends+",build_"+bcp); //$NON-NLS-1$ //$NON-NLS-2$
			
//			Element antNode = doc.createElement("ant");
//			antNode.setAttribute("target", "build_" + bcp);
//			targetNode.appendChild(antNode);

			Element bcpTargetNode = doc.createElement("target"); //$NON-NLS-1$
			bcpTargetNode.setAttribute("name", "build_" + bcp); //$NON-NLS-1$ //$NON-NLS-2$
			// projNode.appendChild(bcpTargetNode);
			projNode.insertBefore(bcpTargetNode, deployNode);

			Element mkNode = doc.createElement("mkdir"); //$NON-NLS-1$
			mkNode.setAttribute("dir", "${tempdir}/classes/" + bcp + "/public"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			bcpTargetNode.appendChild(mkNode);
			Element mksrcNode = doc.createElement("mkdir"); //$NON-NLS-1$
			mksrcNode.setAttribute("dir", "${tempdir}/src/" + bcp + "/public"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			bcpTargetNode.appendChild(mksrcNode);
			Element copyNode = doc.createElement("copydir"); //$NON-NLS-1$
			copyNode.setAttribute("dest", "${tempdir}/src/" + bcp + "/public"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			copyNode.setAttribute("src", "${" + bcp + "_src}/public"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			bcpTargetNode.appendChild(copyNode);

			File publicFolder = new File(project.getLocation().toOSString() + "/" + bcp + "/src/public"); //$NON-NLS-1$ //$NON-NLS-2$
			if (publicFolder.exists() && publicFolder.listFiles().length > 0) {
				Element javacNode = doc.createElement("javac"); //$NON-NLS-1$
				// javacNode.setAttribute("target", "1.6");
				// javacNode.setAttribute("fork", "true");
				// javacNode.setAttribute("includeantruntime", "no");
				javacNode.setAttribute("destdir", "${tempdir}/classes/" + bcp + "/public"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				javacNode.setAttribute("encoding", "GBK"); //$NON-NLS-1$ //$NON-NLS-2$
				javacNode.setAttribute("debug", "${compile_debug}"); //$NON-NLS-1$ //$NON-NLS-2$
//				javacNode.setAttribute("fork", "true");
//				javacNode.setAttribute("source", "1.6");
//				javacNode.setAttribute("target", "1.6");
//				javacNode.setAttribute("memoryinitialsize", "128m");
//				javacNode.setAttribute("memorymaximumsize", "800m");
				bcpTargetNode.appendChild(javacNode);
				Element srcNode = doc.createElement("src"); //$NON-NLS-1$
				srcNode.setAttribute("path", "${tempdir}/src/" + bcp + "/public"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				javacNode.appendChild(srcNode);

				// Element javatoolNode = doc.createElement("classpath");
				// javacNode.appendChild(javatoolNode);
				// Element toolJarNode = doc.createElement("pathelement");
				// toolJarNode.setAttribute("path",
				// "C:/Java/jdk1.6.0/lib/tools.jar");
				// javatoolNode.appendChild(toolJarNode);

				Element classpathNode = doc.createElement("classpath"); //$NON-NLS-1$
				classpathNode.setAttribute("refid", "depend_path"); //$NON-NLS-1$ //$NON-NLS-2$
				javacNode.appendChild(classpathNode);

				if (dependPubJarList.size() > 0) {
					for (String jar : dependPubJarList) {
						Element clsNode = doc.createElement("classpath"); //$NON-NLS-1$
						javacNode.insertBefore(clsNode, classpathNode);
						Element dependJarNode = doc.createElement("pathelement"); //$NON-NLS-1$
						dependJarNode.setAttribute("path", jar); //$NON-NLS-1$
						clsNode.appendChild(dependJarNode);
					}
				}
				
				Element jarNode = doc.createElement("jar"); //$NON-NLS-1$
				String jarpath = "${tempdir}/lib/pub" + moduleName + "_" + bcp + ".jar"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				jarNode.setAttribute("destfile", jarpath); //$NON-NLS-1$
				bcpTargetNode.appendChild(jarNode);
				dependPubJarList.add(jarpath);
				dependJarList.add(jarpath);

				Element filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
				filesetNode.setAttribute("dir", "${tempdir}/classes/" + bcp + "/public"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				jarNode.appendChild(filesetNode);
				filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
				filesetNode.setAttribute("dir", "${tempdir}/src/" + bcp + "/public"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				jarNode.appendChild(filesetNode);
				Element excludeNode = doc.createElement("exclude"); //$NON-NLS-1$
				excludeNode.setAttribute("name", "**/*.java"); //$NON-NLS-1$ //$NON-NLS-2$
				filesetNode.appendChild(excludeNode);
				
				Element jarSrcNode = doc.createElement("jar"); //$NON-NLS-1$
				String jarSrcpath = "${tempdir}/srclib/pub" + moduleName + "_" + bcp + "_src.jar"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				jarSrcNode.setAttribute("destfile", jarSrcpath); //$NON-NLS-1$
				bcpTargetNode.appendChild(jarSrcNode);
				
				Element filesetSrcNode = doc.createElement("fileset"); //$NON-NLS-1$
				filesetSrcNode.setAttribute("dir", "${tempdir}/src/" + bcp + "/public"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				jarSrcNode.appendChild(filesetSrcNode);
				
				String projPath = project.getLocation().toOSString();
				File portalspec = new File(projPath + "/" + bcp + "/src/portalspec"); //$NON-NLS-1$ //$NON-NLS-2$
				if (portalspec.exists()) {
					filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
					filesetNode.setAttribute("dir", "${" + bcp + "_src}/portalspec"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					jarNode.appendChild(filesetNode);
					Element includeNode = doc.createElement("include"); //$NON-NLS-1$
					includeNode.setAttribute("name", "**/**"); //$NON-NLS-1$ //$NON-NLS-2$
					filesetNode.appendChild(includeNode);
					excludeNode = doc.createElement("exclude"); //$NON-NLS-1$
					excludeNode.setAttribute("name", "**/.svn"); //$NON-NLS-1$ //$NON-NLS-2$
					filesetNode.appendChild(excludeNode);
				}
//				File resourceFolder = new File(projPath + "/" + bcp + "/resources");
//				if (resourceFolder.exists()) {
//					filesetNode = doc.createElement("fileset");
//					filesetNode.setAttribute("dir", "${" + bcp + "}/resources");
//					jarNode.appendChild(filesetNode);
//					Element includeNode = doc.createElement("include");
//					includeNode.setAttribute("name", "**/**");
//					filesetNode.appendChild(includeNode);
//					excludeNode = doc.createElement("exclude");
//					excludeNode.setAttribute("name", "**/.svn");
//					filesetNode.appendChild(excludeNode);
//				}
			}
			File privateFolder = new File(project.getLocation().toOSString() + "/" + bcp + "/src/private"); //$NON-NLS-1$ //$NON-NLS-2$
			if (privateFolder.exists() && privateFolder.listFiles().length > 0) {
				mkNode = doc.createElement("mkdir"); //$NON-NLS-1$
				mkNode.setAttribute("dir", "${tempdir}/classes/" + bcp + "/private"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				bcpTargetNode.appendChild(mkNode);
				
				mksrcNode = doc.createElement("mkdir"); //$NON-NLS-1$
				mksrcNode.setAttribute("dir", "${tempdir}/src/" + bcp + "/private"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				bcpTargetNode.appendChild(mksrcNode);

				copyNode = doc.createElement("copydir"); //$NON-NLS-1$
				copyNode.setAttribute("dest", "${tempdir}/src/" + bcp + "/private"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				copyNode.setAttribute("src", "${" + bcp + "_src}/private"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				bcpTargetNode.appendChild(copyNode);
				Element javacNode = doc.createElement("javac"); //$NON-NLS-1$
				javacNode.setAttribute("destdir", "${tempdir}/classes/" + bcp + "/private"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				javacNode.setAttribute("encoding", "GBK"); //$NON-NLS-1$ //$NON-NLS-2$
				javacNode.setAttribute("debug", "${compile_debug}"); //$NON-NLS-1$ //$NON-NLS-2$
//				javacNode.setAttribute("fork", "yes");
//				javacNode.setAttribute("source", "1.6");
//				javacNode.setAttribute("target", "1.6");
//				javacNode.setAttribute("memoryinitialsize", "128m");
//				javacNode.setAttribute("memorymaximumsize", "512m");			
				bcpTargetNode.appendChild(javacNode);
				Element srcNode = doc.createElement("src"); //$NON-NLS-1$
				srcNode.setAttribute("path", "${tempdir}/src/" + bcp + "/private"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				javacNode.appendChild(srcNode);

				Element classpathNode = doc.createElement("classpath"); //$NON-NLS-1$
				classpathNode.setAttribute("refid", "depend_path"); //$NON-NLS-1$ //$NON-NLS-2$
				javacNode.appendChild(classpathNode);
				if (dependJarList.size() > 0) {
					for (String jar : dependJarList) {
						Element clsNode = doc.createElement("classpath"); //$NON-NLS-1$
						javacNode.insertBefore(clsNode, classpathNode);
						Element dependJarNode = doc.createElement("pathelement"); //$NON-NLS-1$
						dependJarNode.setAttribute("path", jar); //$NON-NLS-1$
						clsNode.appendChild(dependJarNode);
					}
				}
				Element clsNode = doc.createElement("classpath"); //$NON-NLS-1$
				clsNode.setAttribute("refid", "private_depend_path"); //$NON-NLS-1$ //$NON-NLS-2$
				javacNode.insertBefore(clsNode, classpathNode);
				
				Element jarNode = doc.createElement("jar"); //$NON-NLS-1$
				String jarpath = "${tempdir}/lib/" + moduleName + "_" + bcp + ".jar"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				jarNode.setAttribute("destfile", jarpath); //$NON-NLS-1$
				bcpTargetNode.appendChild(jarNode);
				dependJarList.add(jarpath);

				Element filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
				filesetNode.setAttribute("dir", "${tempdir}/classes/" + bcp + "/private"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				jarNode.appendChild(filesetNode);
				filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
				filesetNode.setAttribute("dir", "${tempdir}/src/" + bcp + "/private"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				jarNode.appendChild(filesetNode);
				Element excludeNode = doc.createElement("exclude"); //$NON-NLS-1$
				excludeNode.setAttribute("name", "**/*.java"); //$NON-NLS-1$ //$NON-NLS-2$
				filesetNode.appendChild(excludeNode);
				
				Element jarSrcNode = doc.createElement("jar"); //$NON-NLS-1$
				String jarSrcpath = "${tempdir}/srclib/" + moduleName + "_" + bcp + "_src.jar"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				jarSrcNode.setAttribute("destfile", jarSrcpath); //$NON-NLS-1$
				bcpTargetNode.appendChild(jarSrcNode);
				Element filesetSrcNode = doc.createElement("fileset"); //$NON-NLS-1$
				filesetSrcNode.setAttribute("dir", "${tempdir}/src/" + bcp + "/private"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				jarSrcNode.appendChild(filesetSrcNode);
			}
		}

		return doc;
	}

	public Document createDeployAnt(IProject project, Document doc) {
		String moduleName = LFWPersTool.getProjectModuleName(project);
		Element projNode = (Element) doc.getElementsByTagName("project").item(0); //$NON-NLS-1$
		Element pathNode = (Element) projNode.getElementsByTagName("path").item(0); //$NON-NLS-1$
		Element propertyNode = doc.createElement("property"); //$NON-NLS-1$
		propertyNode.setAttribute("name", "mk_nc_home"); //$NON-NLS-1$ //$NON-NLS-2$
		propertyNode.setAttribute("value", "C:/buildFolder/" + moduleName + "/codeFolder"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		projNode.insertBefore(propertyNode, pathNode);

		propertyNode = doc.createElement("property"); //$NON-NLS-1$
		propertyNode.setAttribute("name", "langlib"); //$NON-NLS-1$ //$NON-NLS-2$
		propertyNode.setAttribute("value", "C:/buildFolder/" + moduleName + "/langFolder"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		projNode.insertBefore(propertyNode, pathNode);

		NodeList targetList = projNode.getElementsByTagName("target"); //$NON-NLS-1$
		Element targetNode = (Element) targetList.item(targetList.getLength() - 1);

		String bcpNames[] = LfwCommonTool.getBCPNames(project);
		// for(String bcp:bcpNames){
		// Element mkNode = doc.createElement("mkdir");
		// mkNode.setAttribute("dir", "${mk_nc_home}/modules/"+bcp+"/lib");
		// targetNode.appendChild(mkNode);
		// mkNode = doc.createElement("mkdir");
		// mkNode.setAttribute("dir",
		// "${mk_nc_home}/modules/"+bcp+"/META-INF/lib");
		// targetNode.appendChild(mkNode);
		// }
		for (String bcp : bcpNames) {
			Element copyNode = doc.createElement("copy"); //$NON-NLS-1$
			copyNode.setAttribute("todir", "${mk_nc_home}/hotwebs/portal"); //$NON-NLS-1$ //$NON-NLS-2$
			targetNode.appendChild(copyNode);
			Element filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
			filesetNode.setAttribute("dir", "${" + bcp + "}/web"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			copyNode.appendChild(filesetNode);
			Element excludeNode = doc.createElement("exclude"); //$NON-NLS-1$
			excludeNode.setAttribute("name", "**/WEB-INF/classes/**"); //$NON-NLS-1$ //$NON-NLS-2$
			filesetNode.appendChild(excludeNode);
			excludeNode = doc.createElement("exclude"); //$NON-NLS-1$
			excludeNode.setAttribute("name", "**/WEB-INF/lib/**"); //$NON-NLS-1$ //$NON-NLS-2$
			filesetNode.appendChild(excludeNode);
			excludeNode = doc.createElement("exclude"); //$NON-NLS-1$
			excludeNode.setAttribute("name", "html/**"); //$NON-NLS-1$ //$NON-NLS-2$
			filesetNode.appendChild(excludeNode);
			copyNode = doc.createElement("copy"); //$NON-NLS-1$
			copyNode.setAttribute("todir", "${mk_nc_home}/hotwebs/portal/sync/${module}/"+bcp+"/"); //$NON-NLS-1$ //$NON-NLS-2$
			targetNode.appendChild(copyNode);
			filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
			filesetNode.setAttribute("dir", "${" + bcp + "}/web/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			copyNode.appendChild(filesetNode);
			Element includeNode = doc.createElement("include"); //$NON-NLS-1$
			includeNode.setAttribute("name", "html/**"); //$NON-NLS-1$ //$NON-NLS-2$
			filesetNode.appendChild(includeNode);
		}
		Element copyNode = doc.createElement("copy"); //$NON-NLS-1$
		copyNode.setAttribute("todir", "${mk_nc_home}/modules/" + moduleName + "/lib"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		targetNode.appendChild(copyNode);
		Element filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
		filesetNode.setAttribute("dir", "${tempdir}/lib"); //$NON-NLS-1$ //$NON-NLS-2$
		copyNode.appendChild(filesetNode);
		Element includeNode = doc.createElement("include"); //$NON-NLS-1$
		includeNode.setAttribute("name", "pub*.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		filesetNode.appendChild(includeNode);
		Element fileseSrctNode = doc.createElement("fileset"); //$NON-NLS-1$
		fileseSrctNode.setAttribute("dir", "${tempdir}/srclib"); //$NON-NLS-1$ //$NON-NLS-2$
		copyNode.appendChild(fileseSrctNode);
		Element includeSrcNode = doc.createElement("include"); //$NON-NLS-1$
		includeSrcNode.setAttribute("name", "pub*_src.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		fileseSrctNode.appendChild(includeSrcNode);

		Element copyNode1 = doc.createElement("copy"); //$NON-NLS-1$
		copyNode1.setAttribute("todir", "${mk_nc_home}/modules/" + moduleName + "/META-INF"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		targetNode.appendChild(copyNode1);
		filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
		filesetNode.setAttribute("dir", "${base_" + moduleName + "}/META-INF"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		copyNode1.appendChild(filesetNode);
		includeNode = doc.createElement("include"); //$NON-NLS-1$
		includeNode.setAttribute("name", "**/**"); //$NON-NLS-1$ //$NON-NLS-2$
		filesetNode.appendChild(includeNode);
		

		copyNode = doc.createElement("copy"); //$NON-NLS-1$
		copyNode.setAttribute("todir", "${mk_nc_home}/modules/" + moduleName + "/META-INF/lib"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		targetNode.appendChild(copyNode);
		filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
		filesetNode.setAttribute("dir", "${tempdir}/lib"); //$NON-NLS-1$ //$NON-NLS-2$
		copyNode.appendChild(filesetNode);
		Element excludeNode = doc.createElement("exclude"); //$NON-NLS-1$
		excludeNode.setAttribute("name", "pub*.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		filesetNode.appendChild(excludeNode);
		fileseSrctNode = doc.createElement("fileset"); //$NON-NLS-1$
		fileseSrctNode.setAttribute("dir", "${tempdir}/srclib"); //$NON-NLS-1$ //$NON-NLS-2$
		copyNode.appendChild(fileseSrctNode);
		Element excludeSrcNode = doc.createElement("exclude"); //$NON-NLS-1$
		excludeSrcNode.setAttribute("name", "pub*_src.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		fileseSrctNode.appendChild(excludeSrcNode);

		for (String bcp : bcpNames) {
			String path = project.getLocation().toOSString() + "/" + bcp; //$NON-NLS-1$
			File folder = new File(path + "/META-INF"); //$NON-NLS-1$
			if (!folder.exists())
				continue;
			filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
			filesetNode.setAttribute("dir", "${" + bcp + "}/META-INF"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			copyNode1.appendChild(filesetNode);
			includeNode = doc.createElement("include"); //$NON-NLS-1$
			includeNode.setAttribute("name", "**/**"); //$NON-NLS-1$ //$NON-NLS-2$
			filesetNode.appendChild(includeNode);
		}
		for (String bcp : bcpNames) {
			String path = project.getLocation().toOSString() + "/" + bcp; //$NON-NLS-1$
			File folder = new File(path + "/resources"); //$NON-NLS-1$
			if (!folder.exists())
				continue;
			copyNode = doc.createElement("copy"); //$NON-NLS-1$
			copyNode.setAttribute("todir", "${mk_nc_home}/resources/"); //$NON-NLS-1$ //$NON-NLS-2$
			targetNode.appendChild(copyNode);
			filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
			filesetNode.setAttribute("dir", "${" + bcp + "}/resources"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			copyNode.appendChild(filesetNode);
			excludeNode = doc.createElement("exclude"); //$NON-NLS-1$
			excludeNode.setAttribute("name", "**/lang/**"); //$NON-NLS-1$ //$NON-NLS-2$
			filesetNode.appendChild(excludeNode);

			copyNode = doc.createElement("copy"); //$NON-NLS-1$
			copyNode.setAttribute("todir", "${langlib}/"+bcp+"/"); //$NON-NLS-1$ //$NON-NLS-2$
			targetNode.appendChild(copyNode);
			filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
			filesetNode.setAttribute("dir", "${" + bcp + "}/resources"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			copyNode.appendChild(filesetNode);
			includeNode = doc.createElement("include"); //$NON-NLS-1$
			includeNode.setAttribute("name", "**/*.properties"); //$NON-NLS-1$ //$NON-NLS-2$
			filesetNode.appendChild(includeNode);
		}

		return doc;
	}

	/**
	 * 执行ANT脚本打包操作
	 */
	private void antBuild() {
		String antPath = "nc/uap/lfw/build/antScript/build.xml"; //$NON-NLS-1$
		InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(antPath);
		Writer out = null;
//		Project project = new Project();
		try {
			String content = FileUtilities.fetchFileContent(ins, "UTF-8"); //$NON-NLS-1$
			IProject[] projects = LFWPersTool.getOpenedBcpJavaProjects();
			for (String projName : selModuleList) {
				Project project = new Project();
				String antText = content;
				IProject currentProject = LFWPersTool.getProjectByName(projects, projName);
				String moduleName = LfwCommonTool.getProjectModuleName(currentProject);
				File fileTemp = new File("C:/buildFolder/" + moduleName + "/build.xml"); //$NON-NLS-1$ //$NON-NLS-2$
				if (fileTemp.exists())
					fileTemp.delete();
				out = null;
				out = new FileWriter(fileTemp);
				antText = antText.replace("${buildFolder}", "C:/buildFolder/" + moduleName); //$NON-NLS-1$ //$NON-NLS-2$
				antText = antText.replace("${domain}", getDomain()); //$NON-NLS-1$
				antText = antText.replace("${moduleName}", moduleName); //$NON-NLS-1$
				out.write(antText);
				out.close();
				
//				Document currentdoc = XmlCommonTool.parseXML(fileTemp);
//				Element projNode = (Element) currentdoc.getElementsByTagName("project").item(0); 
//				Element buildNode = (Element) projNode.getElementsByTagName("target").item(0); 
//				File businessFolder = new File("C:/buildFolder/" + moduleName+"/businessFolder");
//				File dbcreateFolder = new File("C:/buildFolder/" + moduleName+"/dbcreateFolder");
//				if(businessFolder.exists()){
//					Element jarNode = currentdoc.createElement("jar");
//					jarNode.setAttribute("destfile", "C:/buildFolder/" + moduleName+"/businessFolder/business.jar");
//					jarNode.setAttribute("basedir", "C:/buildFolder/" + moduleName+"/businessFolder/");					
//					buildNode.appendChild(jarNode);
//				}
//				if(dbcreateFolder.exists()){
//					Element jarNode = currentdoc.createElement("jar");
//					jarNode.setAttribute("destfile", "C:/buildFolder/" + moduleName+"/dbcreateFolder/dbcreate.jar");
//					jarNode.setAttribute("basedir", "C:/buildFolder/" + moduleName+"/dbcreateFolder/");					
//					buildNode.appendChild(jarNode);
//				}
//				XmlCommonTool.documentToXml(currentdoc, fileTemp);
				
				project.fireBuildStarted();
				project.init();
				ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
				projectHelper.parse(project, fileTemp);
				//				project.setProperty("moduleName", moduleName); //$NON-NLS-1$
				project.executeTarget("build"); //$NON-NLS-1$
				project.fireBuildFinished(null);
			}

		} catch (Exception e) {
			MainPlugin.getDefault().logError(e);
		} finally {
			try {
				if (ins != null)
					ins.close();
			} catch (IOException e) {
				MainPlugin.getDefault().logError(e.getMessage());
			}
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				MainPlugin.getDefault().logError(e.getMessage());
			}
		}
	}

	/**
	 * 最后一步文件整合
	 */
	private void finish() {
		String antPath = "nc/uap/lfw/build/antScript/build.xml"; //$NON-NLS-1$
		InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(antPath);		
		try {
			String content = FileUtilities.fetchFileContent(ins, "UTF-8"); //$NON-NLS-1$
			IProject[] projects = LFWPersTool.getOpenedBcpJavaProjects();
			for (String projName : selModuleList) {
				String antText = content;
				IProject currentProject = LFWPersTool.getProjectByName(projects, projName);
				String moduleName = LfwCommonTool.getProjectModuleName(currentProject);
				File fileTemp = new File("C:/buildFolder/" + moduleName + "/build.xml"); //$NON-NLS-1$ //$NON-NLS-2$
//				if (fileTemp.exists())
//					fileTemp.delete();
//				Writer out = null;
//				try{
//					out = new FileWriter(fileTemp);
//					antText = antText.replace("${buildFolder}", "C:/buildFolder/" + moduleName); //$NON-NLS-1$ //$NON-NLS-2$
//					antText = antText.replace("${domain}", getDomain()); //$NON-NLS-1$
//					antText = antText.replace("${moduleName}", moduleName); //$NON-NLS-1$
//					out.write(antText);
//				}
//				catch(Exception e){
//					MainPlugin.getDefault().logError(e.getMessage());
//				}
//				finally{
//					if(out != null)
//						out.close();
//				}
				String bcpNames[] = LfwCommonTool.getBCPNames(currentProject);
				Document currentdoc = XmlCommonTool.parseXML(fileTemp);
				Element projNode = (Element) currentdoc.getElementsByTagName("project").item(0); 
				Element buildNode = (Element) projNode.getElementsByTagName("target").item(1); 
				File businessFolder = new File("C:/buildFolder/" + moduleName+"/businessFolder");
				File dbcreateFolder = new File("C:/buildFolder/" + moduleName+"/dbcreateFolder");
				if(businessFolder.exists()){
					Element jarNode = currentdoc.createElement("jar");
					jarNode.setAttribute("destfile", "C:/buildFolder/" + moduleName+"/businessFolder/business.jar");
					jarNode.setAttribute("basedir", "C:/buildFolder/" + moduleName+"/businessFolder/");					
					buildNode.appendChild(jarNode);
				}
				if(dbcreateFolder.exists()){
					Element jarNode = currentdoc.createElement("jar");
					jarNode.setAttribute("destfile", "C:/buildFolder/" + moduleName+"/dbcreateFolder/dbcreate.jar");
					jarNode.setAttribute("basedir", "C:/buildFolder/" + moduleName+"/dbcreateFolder/");					
					buildNode.appendChild(jarNode);
				}
				Element targetNode = (Element) projNode.getElementsByTagName("target").item(1); 
				for (String bcp : bcpNames) {
					String baseFolder = "C:/buildFolder/" + moduleName+"/langFolder/"+bcp;
					if(new File(baseFolder).exists()){
						Element jarNode = currentdoc.createElement("jar");
						jarNode.setAttribute("destfile", "C:/buildFolder/" + moduleName+"/codeFolder/langlib/" +moduleName+"_"+bcp+"_sim_langres.jar");
						jarNode.setAttribute("basedir", "C:/buildFolder/" + moduleName+"/langFolder/"+bcp);					
						targetNode.appendChild(jarNode);
					}
				}
				XmlCommonTool.documentToXml(currentdoc, fileTemp);
				Project project = new Project();
				project.fireBuildStarted();
				project.init();
				ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
				projectHelper.parse(project, fileTemp);

				String jartoLocation = null;

				jartoLocation = diskpath + "/uap_install/" + getDomain() + "/" + moduleName; //$NON-NLS-1$ //$NON-NLS-2$
				File moduleFolder = new File(jartoLocation);
				if (!moduleFolder.exists()) {
					moduleFolder.mkdirs();
				}

				FileUtilities.copyFile(currentProject.getLocation().toOSString() + "/setup.ini", jartoLocation + "/setup.ini"); //$NON-NLS-1$ //$NON-NLS-2$
				project.setProperty("diskLocation", jartoLocation); //$NON-NLS-1$
				project.executeTarget("finish"); //$NON-NLS-1$
				project.fireBuildFinished(null);
			}
			IPath workpath = ResourcesPlugin.getWorkspace().getRoot().getLocation().append("domain").append(getDomain()).append("setup.ini"); //$NON-NLS-1$ //$NON-NLS-2$
			FileUtilities.copyFile(workpath.toFile().getPath(), diskpath + "/uap_install/" + getDomain() + "/setup.ini"); //$NON-NLS-1$ //$NON-NLS-2$

		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage());
		}
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}
}
