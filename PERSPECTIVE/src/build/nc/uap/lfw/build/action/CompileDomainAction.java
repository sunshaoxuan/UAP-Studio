package nc.uap.lfw.build.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.common.XmlCommonTool;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.lang.M_build;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;

import org.eclipse.ant.internal.ui.launchConfigurations.AntLaunchShortcut;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchShortcut2;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@SuppressWarnings("restriction")

public class CompileDomainAction extends NodeAction{
	
	private ArrayList<String> selModuleList = new ArrayList<String>();

	private HashMap<String, ArrayList<String>> dependMapping = new HashMap<String, ArrayList<String>>();

	private String domain = null;
	
	private String type = null;
	
	private String selModule = null;
	
	public CompileDomainAction(){
		super(M_build.CompileDomainAction_0);
		this.domain = LFWPersTool.getCurrentTreeItem().getText();
	}
	
	public CompileDomainAction(LFWBasicTreeItem item){
		super(M_build.CompileDomainAction_0);
		this.domain = item.getText();
	}
	public void run(){
		compile();
	}
	public void compile(){
		CompileDomainDialog dialog = new CompileDomainDialog(getDomain(), M_build.CompileDomainAction_5);
		if(dialog.open()==IDialogConstants.OK_ID){
			type = dialog.getType();
			selModule = dialog.getSelModule();
			compileModules();
		}
	}
	
	private void compileModules() {
		
		String antpath = "templates/buildScript/build.xml"; //$NON-NLS-1$
		InputStream ins = null;
		try{			
		ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(antpath);
		String content = FileUtilities.fetchFileContent(ins, "GBK"); //$NON-NLS-1$
		content = content.replace("${nchome}", LfwCommonTool.getUapHome()); //$NON-NLS-1$
		getDomainDetail();	
		IProject[] projects = LFWPersTool.getOpenedBcpJavaProjects();
		Document currentdoc = null;
		if("all".equals(type)){ //$NON-NLS-1$
			File scriptFolder = new File("C:/antScript"); //$NON-NLS-1$
			if(scriptFolder.exists())
				FileUtilities.deleteFile(scriptFolder);
			scriptFolder.mkdirs();
			File buildFolder = new File("C:/buildFolder/"); //$NON-NLS-1$
			if(buildFolder.exists())
				FileUtilities.deleteFile(buildFolder);
			buildFolder.mkdirs();
			
			File compileFolder = new File("C:/tempcompile"); //$NON-NLS-1$
			if (compileFolder.exists())
				FileUtilities.deleteFile(compileFolder);
			File domainScript = new File(scriptFolder,"domain.xml"); //$NON-NLS-1$
			Document doc = XmlCommonTool.createDocument();
			Element rootNode = doc.createElement("project"); //$NON-NLS-1$
			rootNode.setAttribute("name", "domainCompileTask"); //$NON-NLS-1$ //$NON-NLS-2$
			rootNode.setAttribute("default", "compile"); //$NON-NLS-1$ //$NON-NLS-2$
			doc.appendChild(rootNode);
			Element targetNode = doc.createElement("target"); //$NON-NLS-1$
			targetNode.setAttribute("name", "compile"); //$NON-NLS-1$ //$NON-NLS-2$
			rootNode.appendChild(targetNode);
			
			for (String projectName : selModuleList) {
				File antFile = new File(scriptFolder,projectName+".xml"); //$NON-NLS-1$
				FileUtilities.saveFile(antFile, content, "GBK");		 //$NON-NLS-1$
		        currentdoc = XmlCommonTool.parseXML(antFile);
				IProject selProject = LFWPersTool.getProjectByName(projects, projectName);
				currentdoc = createBuildAnt(selProject, currentdoc);				
//				currentdoc = createDeployAnt(selProject, currentdoc);
				
				XmlCommonTool.documentToXml(currentdoc, antFile);
//				Project project = new Project();
//				DefaultLogger consoleLogger = new DefaultLogger();
//				
//				MessageConsole console = new MessageConsole("Console Name", null);
//				ConsolePlugin.getDefault().getConsoleManager().addConsoles(
//						new IConsole[]{console});
//				MessageConsoleStream consoleStream = console.newMessageStream();
//				ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console);
////				OutputStream out = ((MessageConsole)ConsolePlugin.getDefault().getConsoleManager().getConsoles()[0]).newMessageStream();
//				PrintStream print = new PrintStream(consoleStream);
//				consoleLogger.setOutputPrintStream(print);
//				consoleLogger.setErrorPrintStream(print);
//				consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
//				project.addBuildListener(consoleLogger);
//	
//				project.fireBuildStarted();
//				project.init();
//				ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
//				projectHelper.parse(project, antFile);
//	
//				project.executeTarget("deploy");
//				project.fireBuildFinished(null);
				Element antNode = doc.createElement("ant"); //$NON-NLS-1$
				antNode.setAttribute("target", "compile"); //$NON-NLS-1$ //$NON-NLS-2$
				antNode.setAttribute("antfile", projectName+".xml"); //$NON-NLS-1$ //$NON-NLS-2$
				targetNode.appendChild(antNode);
			}
			XmlCommonTool.documentToXml(doc, domainScript);
			IPath path = new Path(domainScript.getAbsolutePath());
//			IExtensionPoint extPoint = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.debug.ui.launchShortcuts");
//			if (extPoint != null) {
//				IExtension[] extensions = extPoint.getExtensions();
//				for (IExtension extension : extensions) {
//					ILaunchShortcut2 launch = (ILaunchShortcut2)extension.getConfigurationElements()[0].createExecutableExtension("class");
//					launch.get
//				}
//			}
			
			ILaunchConfiguration configuration = AntLaunchShortcut.createDefaultLaunchConfiguration(path, null);
			ILaunchConfigurationWorkingCopy workingCopy = null;
            workingCopy = configuration.getWorkingCopy();
            workingCopy.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING,"UTF-8");  //$NON-NLS-1$
            workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xms64m -Xmx512m"); //$NON-NLS-1$
            workingCopy.launch("run", null);  //$NON-NLS-1$
            configuration.delete();
            workingCopy.delete();
		}
		else{
			IProject selProject = LFWPersTool.getProjectByName(projects, selModule);
			String moduleName = LFWPersTool.getProjectModuleName(selProject);
			File compileFolder = new File("C:/tempcompile/"+moduleName); //$NON-NLS-1$
//			if(compileFolder.exists()){
//				FileUtilities.deleteFile(compileFolder);
//			}
			File scriptFolder = new File("C:/antScript"); //$NON-NLS-1$
			scriptFolder.mkdirs();
			File antFile = new File(scriptFolder,selProject.getName()+".xml"); //$NON-NLS-1$
			FileUtilities.saveFile(antFile, content, "GBK");		 //$NON-NLS-1$
	        currentdoc = XmlCommonTool.parseXML(antFile);
			currentdoc = createBuildAnt(selProject, currentdoc);			
//			currentdoc = createDeployAnt(selProject, currentdoc);
			XmlCommonTool.documentToXml(currentdoc, antFile);

			IPath path = new Path(antFile.getAbsolutePath());
			ILaunchConfiguration configuration = AntLaunchShortcut.createDefaultLaunchConfiguration(path, null);
			ILaunchConfigurationWorkingCopy workingCopy = null;
            workingCopy = configuration.getWorkingCopy();
            workingCopy.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING,"UTF-8");  //$NON-NLS-1$
            workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
            	     "-Xms64m -Xmx512m"); //$NON-NLS-1$
            	   workingCopy.launch("run", null); //$NON-NLS-1$
            workingCopy.launch("run", null);  //$NON-NLS-1$
            configuration.delete();
            workingCopy.delete();
		}
//		MessageDialog.openInformation(null, "成功", "编译成功，默认编译路径为C:\\tempcompile");
		MainPlugin.getDefault().logInfo(M_build.CompileDomainAction_1);
		}
		catch(Exception e){
//			MessageDialog.openError(null, "失败", "编译失败，请检查代码是否出错和依赖关系是否正确");
			MainPlugin.getDefault().logError(M_build.CompileDomainAction_2);
			MainPlugin.getDefault().logError(e.getMessage(),e);

		}
		finally{			
				try {
					if(ins != null)
						ins.close();
				} catch (IOException e) {
					MainPlugin.getDefault().logError(e.getMessage(),e);
				}
		}
//		Job job = new Job("编译工程"){
//
//			@Override
//			protected IStatus run(IProgressMonitor monitor){
//				try{
//				monitor.beginTask(M_build.CreateDiskAction_0, 4);
//				File scriptFolder = new File("C:/antScript");
//				if(scriptFolder.exists())
//					FileUtilities.deleteFile(scriptFolder);
//				scriptFolder.mkdirs();
//				String antpath = "templates/buildScript/build.xml";
//				InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(antpath);
//				String content = FileUtilities.fetchFileContent(ins, "GBK");
//				content = content.replace("${nchome}", LfwCommonTool.getNCHome());
//				getDomainDetail();	
//				monitor.worked(1);
//				IProject[] projects = LFWPersTool.getOpenedBcpJavaProjects();
//				Document currentdoc = null;
//				if("all".equals(type)){
//					File compileFolder = new File("C:/tempcompile");
//					if (compileFolder.exists())
//						FileUtilities.deleteFile(compileFolder);
//					File domainScript = new File(scriptFolder,"domain.xml");
//					Document doc = XmlCommonTool.createDocument();
//					Element rootNode = doc.createElement("project");
//					rootNode.setAttribute("name", "domainCompileTask");
//					rootNode.setAttribute("default", "compile");
//					doc.appendChild(rootNode);
//					Element targetNode = doc.createElement("target");
//					targetNode.setAttribute("name", "compile");
//					rootNode.appendChild(targetNode);
//					
//					for (String projectName : selModuleList) {
//						File antFile = new File(scriptFolder,projectName+".xml");
//						FileUtilities.saveFile(antFile, content, "GBK");		
//				        currentdoc = XmlCommonTool.parseXML(antFile);
//				        monitor.worked(1);
//						IProject selProject = LFWPersTool.getProjectByName(projects, projectName);
//						currentdoc = createBuildAnt(selProject, currentdoc);
//						currentdoc = createDeployAnt(selProject, currentdoc);
//						monitor.worked(1);
//						XmlCommonTool.documentToXml(currentdoc, antFile);
////						Project project = new Project();
////						DefaultLogger consoleLogger = new DefaultLogger();
////						
////						MessageConsole console = new MessageConsole("Console Name", null);
////						ConsolePlugin.getDefault().getConsoleManager().addConsoles(
////								new IConsole[]{console});
////						MessageConsoleStream consoleStream = console.newMessageStream();
////						ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console);
//////						OutputStream out = ((MessageConsole)ConsolePlugin.getDefault().getConsoleManager().getConsoles()[0]).newMessageStream();
////						PrintStream print = new PrintStream(consoleStream);
////						consoleLogger.setOutputPrintStream(print);
////						consoleLogger.setErrorPrintStream(print);
////						consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
////						project.addBuildListener(consoleLogger);
////			
////						project.fireBuildStarted();
////						project.init();
////						ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
////						projectHelper.parse(project, antFile);
////			
////						project.executeTarget("deploy");
////						project.fireBuildFinished(null);
//						Element antNode = doc.createElement("ant");
//						antNode.setAttribute("target", "deploy");
//						antNode.setAttribute("antfile", projectName+".xml");
//						targetNode.appendChild(antNode);
//					}
//					XmlCommonTool.documentToXml(doc, domainScript);
//					IPath path = new Path(domainScript.getAbsolutePath());
//					ILaunchConfiguration configuration = AntLaunchShortcut.createDefaultLaunchConfiguration(path, null);
//					ILaunchConfigurationWorkingCopy workingCopy = null;
//		            workingCopy = configuration.getWorkingCopy();
//		            workingCopy.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING,"UTF-8"); 
//		            workingCopy.launch("run", null); 
//		            configuration.delete();
////		            workingCopy.delete();
//				}
//				else{
//					IProject selProject = LFWPersTool.getProjectByName(projects, selModule);
//					String moduleName = LFWPersTool.getProjectModuleName(selProject);
//					File compileFolder = new File("C:/tempcompile/"+moduleName);
//					if(compileFolder.exists()){
//						FileUtilities.deleteFile(compileFolder);
//					}
//					File antFile = new File(scriptFolder,selProject.getName()+".xml");
//					FileUtilities.saveFile(antFile, content, "GBK");		
//			        currentdoc = XmlCommonTool.parseXML(antFile);
//			        monitor.worked(1);
//					currentdoc = createBuildAnt(selProject, currentdoc);
//					currentdoc = createDeployAnt(selProject, currentdoc);
//					monitor.worked(1);
//					XmlCommonTool.documentToXml(currentdoc, antFile);
////					Project project = new Project();
////					DefaultLogger consoleLogger = new DefaultLogger();
////					consoleLogger.setErrorPrintStream(System.err);
////					consoleLogger.setOutputPrintStream(System.out);
////					consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
////					project.addBuildListener(consoleLogger);
////
////					project.fireBuildStarted();
////					project.init();
////					ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
////					projectHelper.parse(project, antFile);
////
////					project.executeTarget("deploy");
////					project.fireBuildFinished(null);
//					IPath path = new Path(antFile.getAbsolutePath());
//					ILaunchConfiguration configuration = AntLaunchShortcut.createDefaultLaunchConfiguration(path, null);
//					ILaunchConfigurationWorkingCopy workingCopy = null;
//		            workingCopy = configuration.getWorkingCopy();
//		            workingCopy.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING,"UTF-8"); 
//		            workingCopy.launch("run", null); 
//		            configuration.delete();
//				}
//				monitor.worked(1);
//				monitor.done();
////				MessageDialog.openInformation(null, "成功", "编译成功，默认编译路径为C:\\tempcompile");
//				MainPlugin.getDefault().logInfo("编译成功!默认编译路径为C:\\tempcompile");
//				return Status.OK_STATUS;
//				}
//				catch(Exception e){
////					MessageDialog.openError(null, "失败", "编译失败，请检查代码是否出错和依赖关系是否正确");
//					MainPlugin.getDefault().logError("编译失败，请检查代码是否出错和依赖关系是否正确");
//					MainPlugin.getDefault().logError(e.getMessage(),e);
//					return Status.CANCEL_STATUS;
//				}
//			}			
//		};
//		job.schedule();		
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

		projNode.setAttribute("default", "compile"); //$NON-NLS-1$ //$NON-NLS-2$
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
//				dependPubJarList.add(jarpath);
//				dependJarList.add(jarpath);
				
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
//				dependPubJarList.add(jarpath);
//				dependJarList.add(jarpath);
				
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
		propertyNode.setAttribute("value", "C:/buildFolder/"+moduleName+"/codeFolder"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		projNode.insertBefore(propertyNode, pathNode);
		
		propertyNode = doc.createElement("property"); //$NON-NLS-1$
		propertyNode.setAttribute("name", "langlib"); //$NON-NLS-1$ //$NON-NLS-2$
		propertyNode.setAttribute("value", "C:/buildFolder/"+moduleName+"/langFolder"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

		for (String bcp : bcpNames) {
			String path = project.getLocation().toOSString()+"/"+bcp; //$NON-NLS-1$
			File folder = new File(path+"/META-INF"); //$NON-NLS-1$
			if(!folder.exists())
				continue;
			filesetNode = doc.createElement("fileset"); //$NON-NLS-1$
			filesetNode.setAttribute("dir", "${" + bcp + "}/META-INF"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			copyNode1.appendChild(filesetNode);
			includeNode = doc.createElement("include"); //$NON-NLS-1$
			includeNode.setAttribute("name", "**/**"); //$NON-NLS-1$ //$NON-NLS-2$
			filesetNode.appendChild(includeNode);
		}
		for (String bcp : bcpNames) {
			String path = project.getLocation().toOSString()+"/"+bcp; //$NON-NLS-1$
			File folder = new File(path+"/resources"); //$NON-NLS-1$
			if(!folder.exists())
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
			copyNode.setAttribute("todir", "${langlib}/resources/"); //$NON-NLS-1$ //$NON-NLS-2$
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
			MessageDialog.openWarning(null, M_build.CompileDomainAction_3, M_build.CompileDomainAction_4);
		}
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
}
