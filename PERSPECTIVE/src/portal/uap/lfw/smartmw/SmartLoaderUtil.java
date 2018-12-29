package uap.lfw.smartmw;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JOptionPane;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.lang.M_perspective;

import org.eclipse.core.resources.IProject;

public class SmartLoaderUtil {

	public URL[] getSmartURL() {
		List<URL> urls = new ArrayList<URL>();
		try {
			String uapHome = "";
			int limitNum = 0;
			while(uapHome.equals("")&&limitNum<15){
				uapHome = LfwCommonTool.getUapHome();
				Thread.sleep(1000);
				limitNum++;
			}
			MainPlugin.getDefault().logInfo("Uaphome:"+uapHome);
			List<String> pathSet = new ArrayList<String>();
			pathSet.addAll(getWebPublicJars(uapHome));
			pathSet.addAll(getSmartBasicJars(uapHome));
			String[] paths = pathSet.toArray(new String[0]);
			Arrays.sort(paths, new Comparator<String>(){
				@Override
				public int compare(String o1, String o2) {
					if(o1.endsWith(".jar")&&o2.endsWith(".jar")) //$NON-NLS-1$ //$NON-NLS-2$
						return 0;
					return o1.endsWith(".jar") ? 1 : -1; //$NON-NLS-1$
				}
			});
			for (String path : paths) {
				urls.add(new URL("file:///" + path)); //$NON-NLS-1$
			}
			List<String> wsClassPaths = getWorkspaceClasses();
			for (String path : wsClassPaths) {
				urls.add(new URL("file:///" + path)); //$NON-NLS-1$
			}
		} 
		catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		return urls.toArray(new URL[0]);
	}

	private List<String> getWebPublicJars(String uapHome) {
		List<String> list = new ArrayList<String>();
		String path = uapHome + "/modules/webrt/lib/"; //$NON-NLS-1$
		File dir = new File(path);
		if(!judgeWebExists(dir)){
			JOptionPane.showMessageDialog(null, M_perspective.SmartLoaderUtil_0);
			throw new LfwRuntimeException("lfw develepment support library doesn't exists!"); //$NON-NLS-1$
		}
		else{
			List<String> moduleList = new ArrayList<String>();
			String[] modules = {"webad","webbd","webdbl","webimp","webrt","websm"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			moduleList.addAll(Arrays.asList(modules));
			moduleList.add("baseapp");
			moduleList.add("pubapputil");
			addModulesToList(moduleList,list,uapHome);
		}
		
//		String[] dirs = new String[]{"webrt"};
//		for (int i = 0; i < dirs.length; i++) {
//			String dirName = dirs[i];
//			String path = uapHome + "/modules/" + dirName + "/lib/";
//			File dir = new File(path);
//			if(judgeIsSource(dir)){
//				list.addAll(getPathsFromSource(dir));
//			}
//			else{
//				list.addAll(getPathsFromDir(dir));
//			}
//		}
		return list;
	}


	
	private void addModulesToList(List<String> modules,List<String> list, String dir){
		for(String module:modules){
			String path = dir + "/modules/" + module; //$NON-NLS-1$
			File folder = new File(path);
			scanDir(list,folder);
		}
	}
	

	private void scanDir(List<String> list, File file) {
		if(file.isDirectory()&&file.listFiles().length==0)
			return;
		if(file.isFile()){
			if(file.getName().endsWith("jar")) //$NON-NLS-1$
				list.add(file.getPath());
			return;
		}
		if(file.isDirectory()&&file.listFiles().length>0){
			if(file.getName().equals("classes")){ //$NON-NLS-1$
				list.add(file.getPath()+"/"); //$NON-NLS-1$
			}
			for(File subFile:file.listFiles()){
				scanDir(list,subFile);
			}
		}
	}

	private List<String> getPathsFromDir(File dir) {
		List<String> path = new ArrayList<String>();
		if(dir.exists()&&dir.listFiles().length>0){
			for(File subJar:dir.listFiles()){
				if(subJar.getName().endsWith(".jar")) //$NON-NLS-1$
					path.add(subJar.getPath());
			}
		}
		return path;
	}

//	private List<String> getPathsFromSource() {
//		List<String> pathList = new ArrayList<String>();
//		String[] projs = System.getProperty(StartEmbedTomcat.DEV_LFW_PROJECTS).split(",");
//		for (int i = 0; i < projs.length; i++) {
//			String str = projs[i];
//			pathList.add(str + "/bin/");
//		}
//		return pathList;
//	}
	
//	public static String[] computeStandCP(IPath[] baseFolders) throws CoreException{
//		ArrayList<String> llist = new ArrayList<String>();
//		for (IPath folder : baseFolders){
//			IPath classes = folder.append("classes");
//			IPath resources = folder.append("resources");
//			if (classes!=null && classes.toFile().exists()){
//				llist.add(classes.toOSString());
//			}
//			if (resources!=null && resources.toFile().exists()){
//				llist.add(resources.toOSString());
//			}
//			IPath varclass = folder.append("var/classes/");
//			if (varclass!=null&& varclass.toFile().exists()){
//				llist.add(varclass.toOSString());
//			}
//			IPath extclass = folder.append("extension/classes");
//			if (extclass!=null && extclass.toFile().exists()){
//				llist.add(extclass.toOSString());
//			}
//			IPath libPath = folder.append("lib");
//			llist.addAll(Arrays.asList(computeJarsInPath(new IPath[] {libPath})));
//		}
//		return llist.toArray(new String[0]);
//	}

	private boolean judgeWebExists(File dir) {
		if(dir.exists() && dir.listFiles().length > 0){
			return true;
		}
			
//		if(dir.exists()&&dir.listFiles().length > 0){
//			return false;
//		}
//		else
//			return true;
		return false;
	}

	private List<String> getSmartBasicJars(String uapHome) {
		List<String> paths = new ArrayList<String>();
		
//		String basePath = "D:/views/views63/UAP_WEB_STUDIO6.3_dev/UAP6_WEB_VOB/UAP_WEB_STUDIO/PORTALPERPECTIVE";
		
//		paths.add(basePath + "/bin/");
//		paths.add(basePath + "/tomcat-embed/tomcat-embed-logging-log4j.jar");
//		paths.add(basePath + "/tomcat-embed/tomcat-embed-logging-juli.jar");
//		paths.add(basePath + "/tomcat-embed/tomcat-embed-jasper.jar");
//		paths.add(basePath + "/tomcat-embed/tomcat-embed-core.jar");
//		paths.add(basePath + "/tomcat-embed/tomcat-dbcp.jar");
//		paths.add(basePath + "/tomcat-embed/ecj-4.2.1.jar");
		
		
		String middleLib = uapHome + "/middleware";
		paths.add(middleLib + "/mw.jar");
		paths.add(middleLib + "/standard.jar");
		paths.add(middleLib + "/jstl.jar");
//		paths.add("d:/ncpatcher.jar");
//		paths.add("d:/smartmw.jar");
		
		String externalLib = uapHome + "/external/lib";
//		paths.add(externalLib + "/basic.jar");
//		paths.add(externalLib + "/commons-fileupload-1.2.1.jar");
		paths.add(externalLib + "/fwpub.jar");
//		paths.add(externalLib + "/log.jar");
//		paths.add(externalLib + "/log4j-1.2.15.jar");
//		paths.add(externalLib + "/portlet-api_2.0_spec-1.0.jar");
//		paths.add(externalLib + "/commons-digester3-3.2.jar");
//		paths.add(externalLib + "/commons-logging.jar");
//		paths.add(externalLib + "/commons-beanutils-core.jar");
//		paths.add(externalLib + "/commons-lang-2.1.jar");
////		paths.add(externalLib + "/commons-dbcp-1.2.1.jar");
//		paths.add(externalLib + "/commons-pool.jar");
//		paths.add(externalLib + "/commons-collections.jar");
		paths.add(externalLib + "/nccache.jar");
		paths.add(externalLib + "/bmcache.jar");
		
		paths.add(externalLib + "/spring-core.jar");
		paths.add(externalLib + "/spring-web.jar");
		paths.add(externalLib + "/spring-beans.jar");
		paths.add(externalLib + "/commons-io-1.3.1.jar");

		paths.add(externalLib + "/granite.jar");
		paths.add(externalLib + "/cglib-nodep-2.2.jar");
		paths.add(externalLib + "/dom4j-2.0.0-ALPHA1.jar");
		paths.add(externalLib + "/install.jar");
		paths.add(externalLib + "/log.jar");
		paths.add(externalLib + "/docx4j-2.7.0.jar");
		paths.add(externalLib + "/xalan-2.7.1.jar");
		paths.add(externalLib + "/serializer-2.7.1.jar");
		paths.add(externalLib + "/xpp3_min-1.1.3.4.O.jar");
		paths.add(externalLib + "/sfpub.jar");
		paths.add(externalLib + "/XStream_bin.jar");
		
		String lib = uapHome + "/lib";
		paths.add(lib + "/httpclient.jar");
		paths.add(lib + "/fwserver.jar");
		paths.add(lib + "/mxfw.jar");
		
//		String uapModuleLIb = uapHome + "/modules/uap/lib"; //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapjdbcframework.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapscheduleengine.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapformatmeta.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapncsfbase.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapuapsecurityII.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapmddb.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapmdbusi.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapformula.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapdbcache.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapcontextlang.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapuapmultilanguage.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapbusibean.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapmdbusi.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapbaseservice.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapappmeta.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapscheduleengine.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuaporg.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapappsfbase.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapbbd.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapUIFactoryII.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuaptemplate.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapeaa.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuap_wordgenerator.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapuapsfbase.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuaporgref.jar"); //$NON-NLS-1$
//		paths.add(uapModuleLIb + "/pubuapapporg.jar"); //$NON-NLS-1$
//		
//		
//		paths.add(uapHome + "/modules/uap/META-INF/lib" + "/uapmdbusi.jar"); //$NON-NLS-1$ //$NON-NLS-2$
//		paths.add(uapHome + "/modules/uap/META-INF/lib" + "/uapmddb.jar"); //$NON-NLS-1$ //$NON-NLS-2$
//		paths.add(uapHome + "/modules/uap/META-INF/lib" + "/uapjdbcframework.jar"); //$NON-NLS-1$ //$NON-NLS-2$
//		paths.add(uapHome + "/modules/uap/META-INF/lib" + "/uapuapmultilanguage.jar"); //$NON-NLS-1$ //$NON-NLS-2$
//		paths.add(uapHome + "/modules/uap/META-INF/lib" + "/uapscheduleengine.jar"); //$NON-NLS-1$ //$NON-NLS-2$
//		paths.add(uapHome + "/modules/uap/META-INF/lib" + "/uapdbcache.jar"); //$NON-NLS-1$ //$NON-NLS-2$
//		paths.add(uapHome + "/modules/uap/META-INF/lib" + "/uapscheduleengine.jar"); //$NON-NLS-1$ //$NON-NLS-2$
//		paths.add(uapHome + "/modules/uap/META-INF/lib" + "/uapappsfbase.jar"); //$NON-NLS-1$ //$NON-NLS-2$
//		paths.add(uapHome + "/modules/uap/META-INF/lib" + "/uapbbd.jar"); //$NON-NLS-1$ //$NON-NLS-2$
//		
//		paths.add(uapHome + "/modules/uap/client/lib" + "/uiuapncbean.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		
		File folder = new File(uapHome+"/modules/");
		List<String> moduleList = new ArrayList<String>();
		for(File module:folder.listFiles()){
			if(module.getName().startsWith("uap"))
				moduleList.add(module.getName());
			if(module.getName().startsWith("ria"))
				moduleList.add(module.getName());
			if(module.getName().startsWith("oa"))
				moduleList.add(module.getName());
		}
		
		addModulesToList(moduleList,paths,uapHome);
		
		paths.add(uapHome+"/resources/");
		
		String langLib = uapHome+"/langlib"; //$NON-NLS-1$
		paths.addAll(getPathsFromDir(new File(langLib)));
		
		
		String driverBase = uapHome + "/driver"; //$NON-NLS-1$
		File driverDir = new File(driverBase);
		File[] dirs = driverDir.listFiles();
		for (int i = 0; i < dirs.length; i++) {
			File dir = dirs[i];
			if(!dir.isDirectory())
				continue;
			File[] dfs = dir.listFiles();
			for (int j = 0; j < dfs.length; j++) {
				File file = dfs[j];
				String name = file.getName();
				if(name.endsWith(".jar")) //$NON-NLS-1$
					paths.add(driverBase + "/" + dir.getName() + "/" + name); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return paths;
	}
	
	private List<String> getWorkspaceClasses() {
		List<String> paths = new ArrayList<String>();
		IProject[] projects = LFWPersTool.getOpenedBcpJavaProjects();
		for(IProject project:projects){
//			String[] bcps = LfwCommonTool.getBCPNames(project);
//			for(String bcpName:bcps){
//				String path = project.getLocation().toString()+"/"+bcpName+"/classes/";
//				paths.add(path);
//			}
			String path = project.getLocation().toString()+"/bin/"; //$NON-NLS-1$
			paths.add(path);
			String[] bcpNames = LfwCommonTool.getBCPNames(project);
			if(bcpNames!=null&&bcpNames.length>0){
				for(String bcp:bcpNames){
					String bcpPath = project.getLocation().toString()+"/"+bcp+"/classes/";
					paths.add(bcpPath);
				}
			}
		}
		return paths;
	}
	
	
//	private static String[] computeJarsInPath(IPath[] paths) throws CoreException
//	{
//		final ArrayList<String> list = new ArrayList<String>();
//		for (final IPath path : paths)
//		{
//			File dir = path.toFile();
//			if (dir.exists() && dir.isDirectory()){
//				File[] subFiles = dir.listFiles();
//				for (int i = 0; i < subFiles.length; i++) {
//					File file = subFiles[i];
//					if(file.getName().endsWith(".jar"))
//						list.add(file.getAbsolutePath());
//				}
//			}
//		}
//		return list.toArray(new String[0]);
//	}
}
