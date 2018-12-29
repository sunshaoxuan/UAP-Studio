package nc.uap.lfw.build.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.lang.M_build;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.taskdefs.Expand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.util.IClassFileReader;
import org.eclipse.jdt.core.util.ISourceAttribute;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.corext.util.Resources;
import org.eclipse.jdt.internal.ui.IJavaStatusConstants;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jdt.ui.jarpackager.IJarBuilder;
import org.eclipse.jdt.ui.jarpackager.JarPackageData;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.yonyou.uap.studio.connection.ConnectionService;
import com.yonyou.uap.studio.connection.exception.ConnectionException;

/**
 * 出盘操作
 * 
 * @author qinjianc
 * 
 */

public class CreateDiskAction implements IObjectActionDelegate {

	private String location;
	private String dbType;
	private String dbUrl;
	private String dbName;
	private String userName;
	private String Passwd;
	static TreeItem item;
	private IProject currentProject;

	@Override
	public void run(IAction action) {
		currentProject = LFWPersTool.getCurrentProject();
		Job job = new Job(M_build.CreateDiskAction_0) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(M_build.CreateDiskAction_0, 5);
				File folderTemp = new File("C:/buildFolder"); //$NON-NLS-1$
				deleteFolder(folderTemp);
				folderTemp.mkdir();
				rebuildInstallTemp();
				monitor.worked(1);
				dbcreate();
				initscript();
				monitor.worked(1);
				antBuild();
				monitor.worked(1);
				jarcreate();
				monitor.worked(1);
				finish();
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

	/**
	 * 递归删除文件夹及其下的所有文件
	 * 
	 * @param folder
	 */
	private void deleteFolder(File folder) {
		if (folder.exists()) {
			if (folder.isFile()) {
				folder.delete();
			} else if (folder.isDirectory()) {
				File[] files = folder.listFiles();
				for (File file : files) {
					deleteFolder(file);
				}
				folder.delete();
			}
		}
	}

	/**
	 * 将安装模板从插件中导出，并解压。
	 */
	private void rebuildInstallTemp() {
		FileOutputStream install = null;
		try {
			IProject lfwproject = LFWPersTool.getCurrentProject();
			String moduleName = LfwCommonTool.getProjectModuleName(lfwproject);
			File jarFile = new File("C:/buildFolder/" + moduleName + ".zip"); //$NON-NLS-1$ //$NON-NLS-2$
			if (jarFile.exists())
				jarFile.delete();
			File folder = new File(location);
			if (!folder.exists())
				folder.mkdirs();
			for(File disk:folder.listFiles()){
				if(disk.getName().equals("uap_install")) //$NON-NLS-1$
					deleteFolder(disk);
			}
			String antPath = "installdisk/uap_install.zip"; //$NON-NLS-1$
			InputStream ins = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(antPath);
			install = new FileOutputStream("C:/buildFolder/uap_install.zip"); //$NON-NLS-1$
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
			install.close();
			ins.close();
			Project project = new Project();
			Expand expand = new Expand();
			expand.setProject(project);
			expand.setSrc(new File("C:/buildFolder/uap_install.zip")); //$NON-NLS-1$
			expand.setDest(new File(location));
			expand.setOverwrite(true);
			expand.execute();
		} catch (Exception e) {
			WEBPersPlugin.getDefault().logError(e);
		}
		finally{
			try {
				if(install != null)
					install.close();
			} catch (IOException e) {
				WEBPersPlugin.getDefault().logError(e);
			}
		}
	}

	/**
	 * 生成建库脚本
	 */
	private void dbcreate() {
		IProject project = LFWPersTool.getCurrentProject();
		String[] bcpNames = LfwCommonTool.getBCPNames(project);
		File dbcreateFolder = new File("C:/buildFolder/script"); //$NON-NLS-1$
		if(!dbcreateFolder.exists()) dbcreateFolder.mkdirs();
		for (String bcpName : bcpNames) {
			IFile folder = project.getFile(bcpName + "/build/pdm"); //$NON-NLS-1$
			File realFolder = folder.getLocation().toFile();
			if (realFolder != null && realFolder.exists()) {
				File sqlRoot = new File("C:/buildFolder/script/dbcreate"); //$NON-NLS-1$
				File[] files = realFolder.listFiles();
				for (File file : files) {
					IDbCreateService dbservice = new DbCreateServiceImpl();
					dbservice.geneSqlFile(file, true, sqlRoot);
				}
			}

		}
	}

	/**
	 * 生成数据库初始化脚本
	 */
	private void initscript() {

		// TreeItem[] children = item.getParentItem().getItems();
		// TreeItem[] pdmItems = children[0].getItems();
		String server = ""; //$NON-NLS-1$
		String driver = ""; //$NON-NLS-1$
		String port = ""; //$NON-NLS-1$
//		SDPConnection connection = null;
//		try {
//			if (dbType.equals("Sql Server 2008")) {
//				server = "jdbc:sqlserver://";
//				driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//				port = ":1433;DatabaseName=";
//			} else if (dbType.equals("ORACLE 11g")) {
//				server = "jdbc:oracle:thin:@";
//				driver = "oracle.jdbc.driver.OracleDriver";
//				port = ":1521:";
//			} else {
//				MainPlugin.getDefault().logInfo("database type not find");
//			}
//			Class.forName(driver).newInstance();
//			Connection con = DriverManager.getConnection(server + dbUrl + port + dbName,userName,Passwd);
//		}
//		catch(Exception e){
//			MainPlugin.getDefault().logError(e.getMessage());
//		}
//			UnitDataSource datasource = new UnitDataSource();
//			datasource.setDriverClassName(driver);
//			datasource.setDatabaseUrl(server + dbUrl + port + dbName);
//			datasource.setUser(userName);
//			datasource.setPassword(Passwd);
//
//			connection = new SDPConnection(false, datasource, new Thread());
//		} catch (SDPBuildException e) {
//			WEBProjPlugin.getDefault().logError(e);
//		}
//		Connection conn = connection.getPhysicalConnection();
		Connection conn = null;
		try {
			conn = ConnectionService.getConnection();
		} catch (ConnectionException e1) {
			MainPlugin.getDefault().logError(e1.getMessage(),e1);
		}

		IProject project = LFWPersTool.getCurrentProject();
		String[] bcpNames = LfwCommonTool.getBCPNames(project);
		DBRecordConfigUtil recordUtil = new DBRecordConfigUtil(true);
		File initFolder = new File("C:/buildFolder/init/"); //$NON-NLS-1$
		if(!initFolder.exists()) initFolder.mkdirs();
		for (String bcpName : bcpNames) {
			File scriptFile = project
					.getFile(bcpName + "/build/script/items.xml").getLocation() //$NON-NLS-1$
					.toFile();
			File mapFile = project.getFile(bcpName + "/build/module.map") //$NON-NLS-1$
					.getLocation().toFile();
			if (scriptFile != null && scriptFile.exists()) {
				Object node;
				try {
					node = recordUtil.getDBRecordItemDef(scriptFile.getPath());
					// File sqlFolder = new File(scriptFile.getParentFile(),
					// "/init/business");
					File sqlFolder = new File("C:/buildFolder/init/" + bcpName //$NON-NLS-1$
							+ "/business"); //$NON-NLS-1$
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
						DbRecordConfig conf = new DbRecordConfig();
						conf.setMainTableItem(mainRecordItem);
						conf.setCommonMultiTableFileName(mapFile
								.getParentFile().getPath() + "/common.map"); //$NON-NLS-1$
						conf.setModuleMultiTableFileName(mapFile.getPath());
						DbRecordSqlFileCfg sqlCfg = new DbRecordSqlFileCfg(
								sqlFolder.getPath(), mapFile.getPath(),
								mapFile.getPath());
						IDbRecordService recordService = new DbRecordServiceImpl();
						IDbRecordScript script = recordService
								.retrieveDBRecordScript(conf, conn);
						recordService.geneSqlFile(script, conn, sqlCfg, null);
					}

				} catch (Exception e) {
					WEBPersPlugin.getDefault().logError(e);
				}
			}
		}

	}

	private IJarBuilder fJarBuilder;
	private JarPackageData fJarPackage;
	private JarPackageData[] fJarPackages;
	// private Shell fParentShell;
	private Map<String, ArrayList<IResource>> fJavaNameToClassFilesMap;
	private IContainer fClassFilesMapContainer;
	private Set<IContainer> fExportedClassContainers;

	// private MessageMultiStatus fStatus;
	// private boolean fFilesSaved;

	/**
	 * 将源码编译成jar包并导出
	 */
	private void jarcreate() {
		IProject iproject = LFWPersTool.getCurrentProject();
		String projectName = iproject.getName();
		IJavaProject project = JavaCore.create(
				ResourcesPlugin.getWorkspace().getRoot()).getJavaProject(
				projectName);
		String bcpNames[] = LfwCommonTool.getBCPNames(iproject);
		String moduleName = LfwCommonTool.getProjectModuleName(iproject);
		String rootpath = iproject.getLocation().toString();
		File codeFolder = new File("C:/buildFolder/codeFolder"); //$NON-NLS-1$
		if (codeFolder.exists())
			deleteFolder(codeFolder);
		codeFolder.mkdirs();
		try {
			for (String bcp : bcpNames) {

				File moduleFolder = new File(codeFolder, "/modules/" //$NON-NLS-1$
						+ moduleName);

				buildJar(iproject, project, bcp + "/src/public", moduleFolder //$NON-NLS-1$
						+ "/lib/pub" + moduleName + "_" + bcp + ".jar"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				buildJar(iproject, project, bcp + "/src/private", moduleFolder //$NON-NLS-1$
						+ "/META-INF/lib/" + moduleName + "_" + bcp + ".jar"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				buildJar(iproject, project, bcp + "/src/client", moduleFolder //$NON-NLS-1$
						+ "/client/lib/ui" + moduleName + "_" + bcp + ".jar"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				File resourceFolder = new File(rootpath, bcp + "/resources"); //$NON-NLS-1$
				if (resourceFolder.exists()) {
					File[] resourceFiles = resourceFolder.listFiles();
					for (File resource : resourceFiles) {
						if (!resource.getName().equals("lang")){ //$NON-NLS-1$
							if(resource.isDirectory()){
							FileUtilities.copyFileFromDir(codeFolder
									+ "/resources/" + resource.getName(), //$NON-NLS-1$
									resource.getPath());
							}
							else{
								String[] path = new String[1];
								path[0] = resource.getPath();
								FileUtilities.copyFileToDir(codeFolder
									+ "/resources/", path); //$NON-NLS-1$
							}
						}
						else {
							FileUtilities.copyFileFromDir(
									"C:/buildFolder/langFolder/" + bcp //$NON-NLS-1$
											+ "/lang/simpchn/", //$NON-NLS-1$
									resource.getPath() + "/simpchn"); //$NON-NLS-1$
							Project antProject = new Project();
							antProject.fireBuildStarted();
							antProject.init();
							ProjectHelper projectHelper = ProjectHelper
									.getProjectHelper();
							File fileTemp = new File("C:/buildFolder/build.xml"); //$NON-NLS-1$
							projectHelper.parse(antProject, fileTemp);
							antProject.setProperty("bcpName", bcp); //$NON-NLS-1$
							antProject.setProperty("moduleName", moduleName); //$NON-NLS-1$
							antProject.executeTarget("toJar"); //$NON-NLS-1$
							antProject.fireBuildFinished(null);
						}
					}
				}
				File webFolder = new File(rootpath, bcp + "/web"); //$NON-NLS-1$
				String webContext = LfwCommonTool.getModuleProperty(iproject,
						"module.webContext"); //$NON-NLS-1$
				if ("portal".equals(webContext)) { //$NON-NLS-1$
					FileUtilities.copyFileFromDir(codeFolder
							+ "/hotwebs/portal", webFolder.getPath()); //$NON-NLS-1$
				} else
					FileUtilities.copyFileFromDir(codeFolder + "/hotwebs/lfw", //$NON-NLS-1$
							webFolder.getPath());

				File metadataFolder = new File(rootpath, bcp + "/METADATA"); //$NON-NLS-1$
				FileUtilities.copyFileFromDir(moduleFolder + "/METADATA", //$NON-NLS-1$
						metadataFolder.getPath());
				File metainfFolder = new File(rootpath, bcp + "/META-INF"); //$NON-NLS-1$
				FileUtilities.copyFileFromDir(moduleFolder + "/META-INF", //$NON-NLS-1$
						metainfFolder.getPath());
				FileUtilities.copyFile(rootpath + "/META-INF/module.xml", //$NON-NLS-1$
						moduleFolder + "/META-INF/module.xml"); //$NON-NLS-1$

			}
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e);
		}
	}

	/**
	 * 从程序编译jar文件
	 * 
	 * @param iproject
	 * @param project
	 * @param srcpath
	 * @param transpath
	 */
	private void buildJar(IProject iproject, IJavaProject project,
			String srcpath, String transpath) {
		fJarPackage = new JarPackageData(); 
		IFolder ifile = iproject.getFolder(srcpath);
		Object[] elements = new Object[1];
		elements[0] = project.getPackageFragmentRoot(ifile);
		fJarPackage.setElements(elements);
		IPath path = new Path(transpath);
		fJarPackage.setJarLocation(path);
		fJarPackage.setOverwrite(true);
		fJarBuilder = fJarPackage.getJarBuilder();
		int separatorIndex = path.toString().lastIndexOf("/"); //$NON-NLS-1$
		File directory = new File(path.toString().substring(0, separatorIndex));
		if (!directory.exists())
			directory.mkdirs();

		try {
			fJarBuilder.open(fJarPackage, null, null);
		} catch (CoreException e1) {
			MainPlugin.getDefault().logError(e1.getMessage(),e1);
		}
		fExportedClassContainers = new HashSet<IContainer>(10);
		Set<IJavaProject> enclosingJavaProjects = new HashSet<IJavaProject>(10);
		int n = fJarPackage.getElements().length;
		for (int i = 0; i < n; i++) {
			Object element = fJarPackage.getElements()[i];
			try {
				exportElement(element);
			} catch (InterruptedException e) {
				WEBPersPlugin.getDefault().logError(e);
			}
		}

		try {
			if (fJarBuilder != null)
				fJarBuilder.close();
		} catch (CoreException ex) {
			WEBPersPlugin.getDefault().logError(ex);
		}
		// project.getp
	}

	private void exportElement(Object element) throws InterruptedException {
		int leadSegmentsToRemove = 1;
		IPackageFragmentRoot pkgRoot = null;
		boolean isInJavaProject = false;
		IResource resource = null;
		ITypeRoot typeRootElement = null;
		IJavaProject jProject = null;
		if (element instanceof IJavaElement) {
			isInJavaProject = true;
			IJavaElement je = (IJavaElement) element;
			if (!(je instanceof ITypeRoot)) {
				exportJavaElement(je);
				return;
			}
			typeRootElement = (ITypeRoot) je;
			jProject = typeRootElement.getJavaProject();
			pkgRoot = JavaModelUtil.getPackageFragmentRoot(je);
			resource = typeRootElement.getResource();
		} else if (element instanceof IResource) {
			resource = (IResource) element;
		} else {
			return;
		}

		if (!resource.isAccessible()) {
			// addWarning(Messages.format(JarPackagerMessages.JarFileExportOperation_resourceNotFound,
			// BasicElementLabels.getPathLabel(resource.getFullPath(), false)),
			// null);
			return;
		}

		// if (resource.getType() == IResource.FILE) {
		if (!isInJavaProject) {
			// check if it's a Java resource
			try {
				isInJavaProject = resource.getProject().hasNature(
						JavaCore.NATURE_ID);
			} catch (CoreException ex) {
				WEBPersPlugin.getDefault().logError(ex);
				return;
			}
			if (isInJavaProject) {
				IJavaElement je = JavaCore.create(resource);
				if (je instanceof ITypeRoot && je.exists()) {
					exportElement(je);
					return;
				}

				jProject = JavaCore.create(resource.getProject());
				try {
					IPackageFragment pkgFragment = jProject
							.findPackageFragment(resource.getFullPath()
									.removeLastSegments(1));
					if (pkgFragment != null)
						pkgRoot = JavaModelUtil
								.getPackageFragmentRoot(pkgFragment);
					else
						pkgRoot = findPackageFragmentRoot(jProject, resource
								.getFullPath().removeLastSegments(1));
				} catch (JavaModelException ex) {
					WEBPersPlugin.getDefault().logError(ex);
					return;
				}
			}
		}

		if (pkgRoot != null && jProject != null) {
			leadSegmentsToRemove = pkgRoot.getPath().segmentCount();
			boolean isOnBuildPath;
			isOnBuildPath = jProject.isOnClasspath(resource);
			if (!isOnBuildPath
					|| (mustUseSourceFolderHierarchy() && !pkgRoot
							.getElementName()
							.equals(IPackageFragmentRoot.DEFAULT_PACKAGEROOT_PATH)))
				leadSegmentsToRemove--;
		}

		IPath destinationPath = resource.getFullPath().removeFirstSegments(
				leadSegmentsToRemove);

		if (typeRootElement != null) {
			exportClassFiles(typeRootElement, destinationPath);
		}
	}

	private void exportJavaElement(IJavaElement je) throws InterruptedException {
		StandardJavaElementContentProvider fJavaElementContentProvider = new StandardJavaElementContentProvider();
		Object[] children = fJavaElementContentProvider.getChildren(je);
		for (int i = 0; i < children.length; i++)
			exportElement(children[i]);
	}

	private void exportClassFiles(ITypeRoot typeRootElement,
			IPath destinationPath) {
		// if (fJarPackage.areClassFilesExported()) {
		try {
			if (!typeRootElement.exists())
				return;

			// find corresponding file(s) on classpath and export
			Iterator<? extends IResource> iter = filesOnClasspath(
					typeRootElement, destinationPath);
			IPath baseDestinationPath = destinationPath.removeLastSegments(1);
			while (iter.hasNext()) {
				IFile file = (IFile) iter.next();
				IPath classFilePath = baseDestinationPath
						.append(file.getName());
				// progressMonitor.subTask(Messages.format(JarPackagerMessages.JarFileExportOperation_exporting,
				// BasicElementLabels.getPathLabel(classFilePath, false)));
				try {
					fJarBuilder.writeFile(file, classFilePath);
				} catch (CoreException ex) {
					WEBPersPlugin.getDefault().logError(ex);
				}
			}
		} catch (CoreException ex) {
			MainPlugin.getDefault().logError(ex);
		}
	}

	private Iterator<? extends IResource> filesOnClasspath(
			ITypeRoot typeRootElement, IPath pathInJar) throws CoreException {
		IFile file = (IFile) typeRootElement.getResource();
		IJavaProject javaProject = typeRootElement.getJavaProject();
		IPackageFragmentRoot pkgRoot = JavaModelUtil
				.getPackageFragmentRoot(typeRootElement);

		// Allow JAR Package to provide its own strategy
		IFile[] classFiles = fJarPackage.findClassfilesFor(file);
		if (classFiles != null)
			return Arrays.asList(classFiles).iterator();

		if (!isJavaFile(file))
			return Collections.EMPTY_LIST.iterator();

		IPath outputPath = null;
		if (pkgRoot != null) {
			IClasspathEntry cpEntry = pkgRoot.getRawClasspathEntry();
			if (cpEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE)
				outputPath = cpEntry.getOutputLocation();
		}
		if (outputPath == null)
			// Use default output location
			outputPath = javaProject.getOutputLocation();

		IContainer outputContainer;
		if (javaProject.getProject().getFullPath().equals(outputPath))
			outputContainer = javaProject.getProject();
		else {
			outputContainer = createFolderHandle(outputPath);
			if (outputContainer == null || !outputContainer.isAccessible()) {
				String msg = ""; //$NON-NLS-1$
				throw new CoreException(new Status(IStatus.ERROR,
						JavaPlugin.getPluginId(),
						IJavaStatusConstants.INTERNAL_ERROR, msg, null));
			}
		}

		// Java CU - search files with .class ending
		boolean hasErrors = false;
		boolean hasWarnings = false;
		boolean canBeExported = true;
		if (!canBeExported)
			return Collections.EMPTY_LIST.iterator();
		// reportPossibleCompileProblems(file, hasErrors, hasWarnings,
		// canBeExported);
		IContainer classContainer = outputContainer;
		if (pathInJar.segmentCount() > 1)
			classContainer = outputContainer.getFolder(pathInJar
					.removeLastSegments(1));

		if (fExportedClassContainers.contains(classContainer))
			return Collections.EMPTY_LIST.iterator();

		if (JavaCore.DO_NOT_GENERATE.equals(javaProject.getOption(
				JavaCore.COMPILER_SOURCE_FILE_ATTR, true))) {
			IRegion region = JavaCore.newRegion();
			region.add(typeRootElement);
			IResource[] generatedResources = JavaCore.getGeneratedResources(
					region, false);
			if (generatedResources.length > 0)
				return Arrays.asList(generatedResources).iterator();
			// give the old code a last chance
		}
		if (fClassFilesMapContainer == null
				|| !fClassFilesMapContainer.equals(classContainer)) {
			fJavaNameToClassFilesMap = buildJavaToClassMap(classContainer);
			if (fJavaNameToClassFilesMap == null) {
				// Could not fully build map. fallback is to export whole
				// directory
				String containerName = BasicElementLabels.getPathLabel(
						classContainer.getFullPath(), false);
				String msg = Messages
						.format("", //$NON-NLS-1$
								containerName);
				fExportedClassContainers.add(classContainer);
				return getClassesIn(classContainer);
			}
			fClassFilesMapContainer = classContainer;
		}
		ArrayList<IResource> classFileList = fJavaNameToClassFilesMap.get(file
				.getName());
		if (classFileList == null || classFileList.isEmpty()) {
			String msg = Messages
					.format("", //$NON-NLS-1$
							BasicElementLabels.getPathLabel(file.getFullPath(),
									false));
			throw new CoreException(new Status(IStatus.ERROR,
					JavaPlugin.getPluginId(),
					IJavaStatusConstants.INTERNAL_ERROR, msg, null));
		}
		return classFileList.iterator();
	}

	private Map<String, ArrayList<IResource>> buildJavaToClassMap(
			IContainer container) throws CoreException {
		if (container == null || !container.isAccessible())
			return new HashMap<String, ArrayList<IResource>>(0);
		/*
		 * XXX: Bug 6584: Need a way to get class files for a java file (or CU)
		 */
		IClassFileReader cfReader = null;
		IResource[] members = container.members();
		Map<String, ArrayList<IResource>> map = new HashMap<String, ArrayList<IResource>>(
				members.length);
		for (int i = 0; i < members.length; i++) {
			if (isClassFile(members[i])) {
				IFile classFile = (IFile) members[i];
				URI location = classFile.getLocationURI();
				if (location != null) {
					InputStream contents = null;
					try {
						// contents=
						// EFS.getStore(location).openInputStream(EFS.NONE,
						// monitor);
						contents = new FileInputStream(new File(
								location.getPath()));
						cfReader = ToolFactory
								.createDefaultClassFileReader(contents,
										IClassFileReader.CLASSFILE_ATTRIBUTES);
					} catch (Exception e) {
						WEBPersPlugin.getDefault().logError(e);
					} finally {
						try {
							if (contents != null)
								contents.close();
						} catch (IOException e) {
							throw new CoreException(
									new Status(
											IStatus.ERROR,
											JavaPlugin.getPluginId(),
											IStatus.ERROR,
											Messages.format(
													"", //$NON-NLS-1$
													BasicElementLabels
															.getURLPart(Resources
																	.getLocationString(classFile))),
											e));
						}
					}
					if (cfReader != null) {
						ISourceAttribute sourceAttribute = cfReader
								.getSourceFileAttribute();
						if (sourceAttribute == null) {
							/*
							 * Can't fully build the map because one or more
							 * class file does not contain the name of its
							 * source file.
							 */
							return null;
						}
						String javaName = new String(
								sourceAttribute.getSourceFileName());
						ArrayList<IResource> classFiles = map.get(javaName);
						if (classFiles == null) {
							classFiles = new ArrayList<IResource>(3);
							map.put(javaName, classFiles);
						}
						classFiles.add(classFile);
					}
				}
			}
		}
		return map;
	}

	private Iterator<IResource> getClassesIn(IContainer classContainer)
			throws CoreException {
		IResource[] resources = classContainer.members();
		List<IResource> files = new ArrayList<IResource>(resources.length);
		for (int i = 0; i < resources.length; i++)
			if (resources[i].getType() == IResource.FILE
					&& isClassFile(resources[i]))
				files.add(resources[i]);
		return files.iterator();
	}

	private IFolder createFolderHandle(IPath folderPath) {
		if (folderPath.isValidPath(folderPath.toString())
				&& folderPath.segmentCount() >= 2)
			return JavaPlugin.getWorkspace().getRoot().getFolder(folderPath);
		else
			return null;
	}

	private boolean isJavaFile(IResource file) {
		return file != null && file.getType() == IResource.FILE
				&& file.getFileExtension() != null
				&& JavaCore.isJavaLikeFileName(file.getName());
	}

	private boolean isClassFile(IResource file) {
		return file != null && file.getType() == IResource.FILE
				&& file.getFileExtension() != null
				&& file.getFileExtension().equalsIgnoreCase("class"); //$NON-NLS-1$
	}

	private IPackageFragmentRoot findPackageFragmentRoot(IJavaProject jProject,
			IPath path) throws JavaModelException {
		if (jProject == null || path == null || path.segmentCount() <= 0)
			return null;
		IPackageFragmentRoot pkgRoot = jProject.findPackageFragmentRoot(path);
		if (pkgRoot != null)
			return pkgRoot;
		else
			return findPackageFragmentRoot(jProject, path.removeLastSegments(1));
	}

	private boolean mustUseSourceFolderHierarchy() {
		return fJarPackage.useSourceFolderHierarchy()
				&& fJarPackage.areJavaFilesExported()
				&& !fJarPackage.areGeneratedFilesExported();
	}

	/**
	 * 执行ANT脚本打包操作
	 */
	private void antBuild() {
		File file = new File(""); //$NON-NLS-1$
		String antPath = "nc/uap/lfw/build/antScript/build.xml"; //$NON-NLS-1$
		InputStream ins = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(antPath);
		Project project = new Project();
		try {
			String antText = FileUtilities.fetchFileContent(ins, "UTF-8"); //$NON-NLS-1$
			File fileTemp = new File("C:/buildFolder/build.xml"); //$NON-NLS-1$
			if (fileTemp.exists())
				fileTemp.delete();
			Writer out = null;
			try{
				out = new FileWriter(fileTemp);
				out.write(antText);
			}catch(Exception e){
				MainPlugin.getDefault().logError(e);
			}
			if(out != null)
				out.close();
			project.fireBuildStarted();
			project.init();
			ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
			projectHelper.parse(project, fileTemp);
			String moduleName = LfwCommonTool.getProjectModuleName(currentProject);
			
			project.setProperty("moduleName", moduleName); //$NON-NLS-1$
			project.executeTarget("build"); //$NON-NLS-1$
			project.fireBuildFinished(null);

		} catch (Exception e) {
			MainPlugin.getDefault().logError(e);
		}
	}

	/**
	 * 最后一步文件整合
	 */
	private void finish() {
		try {
			Project project = new Project();
			project.fireBuildStarted();
			project.init();
			ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
			File fileTemp = new File("C:/buildFolder/build.xml"); //$NON-NLS-1$
			projectHelper.parse(project, fileTemp);
	
			IProject lfwproject = LFWPersTool.getCurrentProject();
			String moduleName = LfwCommonTool.getProjectModuleName(lfwproject);
			String jartoLocation = null;
			
			jartoLocation = location+"/uap_install/uap/"+moduleName; //$NON-NLS-1$
	//		for (File projectFolder : diskFolder.listFiles()) {
	////			if (projectFolder.getName().indexOf(moduleName) > -1) {
	//				for (File rightFolder : projectFolder.listFiles()) {
	//					if (rightFolder.getName().indexOf("uap") > -1) {
	//						jartoLocation = location + "/"
	//								+ projectFolder.getName() + "/"
	//								+ rightFolder.getName() + "/" + moduleName;
	//					}
	//				}
	////			}
	//		}
			File moduleFolder = new File(jartoLocation);
			if(!moduleFolder.exists()){
				moduleFolder.mkdirs();
			}
			
			FileUtilities.copyFile(LFWPersTool.getCurrentProject().getLocation().toOSString()+"/setup.ini", jartoLocation+"/setup.ini"); //$NON-NLS-1$ //$NON-NLS-2$
			project.setProperty("diskLocation", jartoLocation); //$NON-NLS-1$
			project.executeTarget("finish"); //$NON-NLS-1$
			project.fireBuildFinished(null);
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public static TreeItem getItem() {
		return item;
	}

	public static void setItem(TreeItem item) {
		CreateDiskAction.item = item;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return Passwd;
	}

	public void setPasswd(String passwd) {
		Passwd = passwd;
	}

}
