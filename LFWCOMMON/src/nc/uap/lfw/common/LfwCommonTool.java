package nc.uap.lfw.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nc.uap.lfw.plugin.common.CommonPlugin;
import nc.uap.plugin.studio.StudioUtil;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.filesystem.provider.FileInfo;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.ExternalPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import uap.lfw.lang.M_common;

/**
 * Perspective公用方法
 * @author zhangxya
 *
 */
@SuppressWarnings("restriction")
public class LfwCommonTool  {
	
	private static Boolean isSupportGDI = Boolean.FALSE;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String WEBCONTEXT = "module.webContext";
	public static String FIELD_EX_MODULES = "FIELD_EX_MODULES"; //$NON-NLS-1$
	/**
	 * 获取所有的根路径
	 * @return
	 */
	public static  String[] getAllRootPackage(IProject proj){
		String projName = proj.getName();
		JavaProject javaProj = (JavaProject) JavaCore.create(proj);
		IPackageFragmentRoot[] pfrs = null;;
		try {
			pfrs = javaProj.getAllPackageFragmentRoots();
		} 
		catch (JavaModelException e) {
			CommonPlugin.getPlugin().logError(e);
		}
		List<String> rootPackage = new ArrayList<String>(); 
		if(pfrs != null){
			for (int i = 0; i < pfrs.length; i++) {
				if(pfrs[i] instanceof JarPackageFragmentRoot || pfrs[i] instanceof ExternalPackageFragmentRoot)
					continue;
				else if(pfrs[i] instanceof PackageFragmentRoot){
					PackageFragmentRoot root = (PackageFragmentRoot) pfrs[i];
					String rootPath = root.getPath().toString();
					if(rootPath.indexOf(projName) != -1){
						String absPath = root.getPath().removeFirstSegments(1).makeRelative().toString();
						if(!absPath.startsWith("NC_HOME") && absPath.startsWith("src"))
							rootPackage.add(absPath);
					}
				}
				
			}
		}
		return (String[])rootPackage.toArray(new String[rootPackage.size()]);
	}
	
	
	public static String getModuleProperty(IProject project, String name){
		if(project==null)
			MessageDialog.openError(null, M_common.LfwCommonTool_0000/*错误*/, M_common.LfwCommonTool_0001/*请选中一个工程再执行操作*/);
		IFile jfile = project.getFile(".module_prj");
 		File file = new File(jfile.getLocation().toString());
 		InputStream input = null;
 		String returnName = null;
		if (file.exists()) {
			try {
				input = new FileInputStream(file);
				Properties prop = new Properties();
				prop.load(input);
				returnName =  prop.getProperty(name);
			} catch (Exception e) {
				CommonPlugin.getPlugin().logError(M_common.LfwCommonTool_0002/*读取module_prj文件出错!*/, e);
			}
			finally{
				try {
					if(input != null)
						input.close();
				} catch (IOException e) {
					//LfwLogger.error(e.getMessage(), e);
					CommonPlugin.getPlugin().logError(M_common.LfwCommonTool_0002/*读取module_prj文件出错!*/, e);
				}
			}
		}
		return returnName;
	}
	
	public static String updateModuleProperty(IProject project, String name, String value){
		IFile jfile = project.getFile(".module_prj");
 		File file = new File(jfile.getLocation().toString());
 		InputStream input = null;
 		OutputStream output = null;
 		String returnName = null;
		if (file.exists()) {
			try {
				checkOutFile(file.getPath());
				input = new FileInputStream(file);
				Properties prop = new Properties();
				prop.load(input);
				prop.put(name, value);
				
				output = new FileOutputStream(file);
				prop.store(output, "");
			} 
			catch (Exception e) {
				CommonPlugin.getPlugin().logError(M_common.LfwCommonTool_0002/*读取module_prj文件出错!*/, e);
			}
			finally{
				try {
					if(input != null)
						input.close();
				} catch (IOException e) {
					//LfwLogger.error(e.getMessage(), e);
					CommonPlugin.getPlugin().logError(M_common.LfwCommonTool_0002/*读取module_prj文件出错!*/, e);
				}
				
				try {
					if(output != null)
						output.close();
				} catch (IOException e) {
					//LfwLogger.error(e.getMessage(), e);
					CommonPlugin.getPlugin().logError(M_common.LfwCommonTool_0002/*读取module_prj文件出错!*/, e);
				}
			}
		}
		return returnName;
	}
	
	/**
	 * checkout文件
	 */
	 public static void checkOutFile(String path){
			IPath ph = new Path(path);
			IFile ifile =  ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(ph);
			File filea = new File(path);
			//如果文件不可写，checkout,若果未连cc，变为可写
			IWorkbenchPart part = null;
			Shell shell = null;
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (page != null)
				part = page.getActivePart();
			if(part != null)
				shell = part.getSite().getShell();
			IStatus statu = ResourcesPlugin.getWorkspace().validateEdit(new IFile[]{ifile}, shell);
				if(!filea.canWrite() && !statu.isOK()){
				boolean isWritable = MessageDialog.openConfirm(null, M_common.LfwCommonTool_0003/*提示*/, M_common.LfwCommonTool_0004/*是否将文件变为可写?*/);
				if(isWritable){
					try {
						LfwCommonTool.silentSetWriterable(path);
					} catch (Exception e) {
						CommonPlugin.getPlugin().logError(e.getMessage());
						MessageDialog.openInformation(null, M_common.LfwCommonTool_0003/*提示*/, e.getMessage());
					}
				}
			}
		 }
	/**
	* checkout文件
	*/	 	
	 public static void checkOutFiles(IFile[] files){
		 	IWorkbenchPart part = null;
			Shell shell = null;
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (page != null)
				part = page.getActivePart();
			if(part != null)
				shell = part.getSite().getShell();
			IStatus statu = ResourcesPlugin.getWorkspace().validateEdit(files, shell);
				if(!statu.isOK()){
				boolean isWritable = MessageDialog.openConfirm(null, M_common.LfwCommonTool_0003/*提示*/, M_common.LfwCommonTool_0005/*是否将所有文件变为可写?*/);
				if(isWritable){
					try {
						for(IFile file:files){
							LfwCommonTool.silentSetWriterable(file.getLocation().toOSString());
						}						
					} catch (Exception e) {
						CommonPlugin.getPlugin().logError(e.getMessage());
						MessageDialog.openInformation(null, M_common.LfwCommonTool_0003/*提示*/, e.getMessage());
					}
				}
			}
	 }
		
	
	

	public static boolean isSupportGDI() {
		if (isSupportGDI == Boolean.FALSE) {
			try {
				System.loadLibrary("gdiplus");
				isSupportGDI = Boolean.TRUE;
			} catch (Throwable e) {
				isSupportGDI = Boolean.FALSE;
			}
		}
		return isSupportGDI.booleanValue();
	}

		
	/**
	 * 将文件变为可写
	 * @param filename
	 * @throws CoreException
	 */
	 public static void silentSetWriterable(String filename) throws CoreException {
        IFileInfo fileinfo = new FileInfo(filename);
        fileinfo.setAttribute(EFS.ATTRIBUTE_READ_ONLY, false);
        IFileSystem fs = EFS.getLocalFileSystem();
        IFileStore store = fs.fromLocalFile(new File(filename));
        store.putInfo(fileinfo, EFS.SET_ATTRIBUTES, null);
	 }
	
	
	
	public static IViewPart showView(String viewId) {
		IViewPart part = null;
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (page != null)
				part = page.showView(viewId, null, IWorkbenchPage.VIEW_VISIBLE);
		} 
		catch (PartInitException e) {
			CommonPlugin.getPlugin().logError(e.getMessage(), e);
		}
		return part;
	}
	
	
	

	public static String formatDateString(Date date) {
		if (date == null) {
			return "";
		}
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	public static Date parseStringToDate(String dateStr) {
		if (dateStr == null || dateStr.trim().length() == 0)
			return null;
		Date d = null;
		try {
			d = sdf.parse(dateStr);
		} catch (ParseException e) {
			CommonPlugin.getPlugin().logError(e);
		}
		return d;
	}

	public static String getWrokspaceDirPath() {
		IPath path = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		String strPath = path.toOSString();
		return strPath;
	}

	public static IProject[] getJavaProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		return projects;
	}
	
	/**
	 * 获取所有LFW工程
	 * @return
	 */
	public static IProject[] getLfwProjects(){
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<IProject> lfwProjects = new ArrayList<IProject>();
		for (int i = 0; i < projects.length; i++) {
			try {
				if(projects[i].hasNature(CommonConstants.MODULE_NATURE)){
					if(projects[i].getName().equals("CODE_PROJECT"))
						continue;
					lfwProjects.add(projects[i]);
				}
			} 
			catch (CoreException e) {
				CommonPlugin.getPlugin().logError(e);
				
			}
		}
		return lfwProjects.toArray(new IProject[lfwProjects.size()]);
	}
	
	public static String getLfwProjectCtx(IProject project) {
		return getModuleProperty(project, WEBCONTEXT);
	}
	
	
    public static String getUapHome()
    {
//    	if(LfwCommonTool.getSpaceMode().equals(SpaceMode.UAP))
    		return StudioUtil.getNCHome();
//    	else
//    		return TomcatUtil.getTomcatHome().toOSString();
    }
    
    public static IPath getNCHomePath()
    {
        return Path.fromOSString(LfwCommonTool.getUapHome());
    }
	
    public static IFolder getNCHomeFolder(){
    	try {
    		IProject[] projs = getLfwProjects();
    		IProject proj = projs[0];
    		IWorkspace workspace = ResourcesPlugin.getWorkspace();
    		IPathVariableManager pathMan = workspace.getPathVariableManager();
    		String name = "NC_HOME";
    		IPath value = new Path(getUapHome());
    		
    		//定义path变量，实现资源挂接
    		if(!pathMan.isDefined(name))
    			pathMan.setValue(name, value);
    		
    		IPath location = new Path(name);
    		IFolder link = proj.getFolder("NC_HOME_LINK");
			link.createLink(location, IResource.NONE, null);
			return link;
		} 
    	catch (CoreException e) {
			CommonPlugin.getPlugin().logError(e);
			return null;
		}

	}
    
//    private static IStringVariableManager getVariableManager() {
//        return VariablesPlugin.getDefault().getStringVariableManager();
//    }
	/**
	 * 是否是bcp工程
	 * @param project
	 * @return
	 */
	public static boolean isBCPProject(IProject project){
		try {
			if(project.hasNature(CommonConstants.BCPMODULE_NATURE))
				return true;
		} 
		catch (CoreException e) {
			CommonPlugin.getPlugin().logError(e);
		}
		return false;
	}
	
	/**
	 * 获取所有打开的LFW工程
	 * @return
	 */
	public static IProject[] getOpenedLfwProjects() {
		IProject[] allProjects = getLfwProjects();
			//getJavaProjects();
		ArrayList<IProject> al = new ArrayList<IProject>();
		int count = allProjects == null ? 0 : allProjects.length;
		for (int i = 0; i < count; i++) {
			if (allProjects[i].isOpen()) {
				al.add(allProjects[i]);
			}
		}
		return al.toArray(new IProject[0]);
	}
	
	/**
	 * 获取UAPWEB工程
	 * @return
	 */
	public static IProject getUapWebProject() {
		IProject[] projects = getAllOpenedJavaProjects();
		for (int i = 0, n = projects.length; i < n; i++) {
			String moduleName = getProjectModuleName(projects[i]);
			if (moduleName != null && moduleName.equals(CommonConstants.UAPWEB_MODULE_NAME_57))
				return projects[i];
			if (moduleName != null && moduleName.equals(CommonConstants.UAPWEB_MODULE_NAME))
				return projects[i];
		}
		return null;
	}
	
	/**
	 * 获取所有打开的工程
	 * @return
	 */
	public static IProject[] getAllOpenedJavaProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			//getJavaProjects();
		ArrayList<IProject> al = new ArrayList<IProject>();
		int count = projects == null ? 0 : projects.length;
		for (int i = 0; i < count; i++) {
			if (projects[i].isOpen()) {
				al.add(projects[i]);
			}
		}
		return al.toArray(new IProject[0]);
	}
	/**
	 * 错误提示
	 * @param errMsg
	 */
	public static void showErrDlg(String errMsg) {
		if (errMsg != null) {
			Shell parent = new Shell(Display.getCurrent());
			MessageDialog.openError(parent, M_common.LfwCommonTool_0006/*错误提示*/, errMsg);
		}
	}
	
	/**
	 * 获取某项目的moduleName
	 * @param project
	 * @return
	 */
	public static String getProjectModuleName(IProject project) {
		String module = null;
		File f = project.getLocation().toFile();
		File moduleFile = new File(f, ".module_prj");
		if (moduleFile.exists()) {
			InputStream is = null;
			try {
				is = new FileInputStream(moduleFile);
				Properties prop = new Properties();
				prop.load(is);
				module = prop.getProperty("module.name");
			} 
			catch (Exception e) {
				CommonPlugin.getPlugin().logError(e);
			}
			finally{				
					try {
						if(is != null)
							is.close();
					} 
					catch (IOException e) {
						CommonPlugin.getPlugin().logError(e);
					}
			}
		}
		return module;
	}

	public static String[] getBCPNames(IProject project){
		try{
			
		    IFile manifestFile = project.getFile("/manifest.xml");
		    File mfile = new File(manifestFile.getLocation().toString());
		    if(mfile.exists()){
				Document doc = XmlUtility.getDocument(mfile); 
				NodeList nodeList =  doc.getElementsByTagName("BusinessComponet");
				String[] names = new String[nodeList.getLength()]; 
				for (int i = 0 ; i < nodeList.getLength(); i++){
					names[i]=((Element)nodeList.item(i)).getAttribute("name");
				}			
				return names;
		    }
		} catch (Exception e) {
			CommonPlugin.getPlugin().logError(e);
			return null;
		}
		return null;
	} 
	public static Map<String,String> getBCPDisplayNames(IProject project){
		try{
			Map<String,String> displayMap = new HashMap<String, String>();
		    IFile manifestFile = project.getFile("/manifest.xml");
		    File mfile = new File(manifestFile.getLocation().toString());
		    
			Document doc = XmlUtility.getDocument(mfile); 
			NodeList nodeList =  doc.getElementsByTagName("BusinessComponet");
			for (int i = 0 ; i < nodeList.getLength(); i++){
				String name =((Element)nodeList.item(i)).getAttribute("name");
				String displayName = ((Element)nodeList.item(i)).getAttribute("dispname");
				displayMap.put(name, displayName);
			}
			
			return displayMap;
		} catch (Exception e) {
			CommonPlugin.getPlugin().logError(e);
			return null;
		}
	} 
	
	public static SpaceMode getSpaceMode() {
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		File f = ws.getRoot().getLocation().append("spacemode.txt").toFile();
		SpaceMode mode = SpaceMode.UAP;
		if(!f.exists()){
			createSpaceModeFile(f);
		}
		else{
			mode = readeSpaceMode(f);
		}
		return mode;
	}


	private static SpaceMode readeSpaceMode(File f) {
		BufferedReader reader = null;
		try {
			InputStream input = new FileInputStream(f);
			reader = new BufferedReader(new InputStreamReader(input));
			String mode = reader.readLine();;
			if(mode != null && mode.equals("1"))
				return SpaceMode.STANDALONG;
			else
				return SpaceMode.UAP;
		} 
		catch (Exception e) {
			CommonPlugin.getPlugin().logError(e);
		}
		finally{
			try {
				if(reader != null)
					reader.close();
			}
			catch(Exception e1){
				CommonPlugin.getPlugin().logError(e1);
			}
		}
		return SpaceMode.UAP;
	}


	private static void createSpaceModeFile(File f) {
		Writer writer = null;
		try {
			OutputStream output = new FileOutputStream(f);
			writer = new OutputStreamWriter(output);
			writer.write("0");
		} 
		catch (Exception e) {
			CommonPlugin.getPlugin().logError(e);
		}
		finally{
			try {
				if(writer != null)
					writer.close();
			}
			catch(Exception e1){
				CommonPlugin.getPlugin().logError(e1);
			}
		}
	}


	public static String getHotwebs() {
		if(getSpaceMode().equals(SpaceMode.UAP))
			return getUapHome() + "/hotwebs/";
		else
			return getUapHome() + "/webapps";
	}
	
	public static String[] getExModuleNames() {
        return getVariableManager()
                        .getValueVariable(FIELD_EX_MODULES)
                        .getValue().split(","); //$NON-NLS-1$
    }
	
    public static IStringVariableManager getVariableManager() {
        return VariablesPlugin.getDefault().getStringVariableManager();
    }

    public static IPath[] getAccessibleModuleFolders() throws CoreException {
		//
		// IResource[] subdir = getModulesFoder().members();
		String[] names = LfwCommonTool.getExModuleNames();
		HashSet<String> exSet = new HashSet<String>();
		for (String name : names) {
			if(name == null || name.equals(""))
				continue;
			exSet.add(name);
		}
		// File[] subdir = modulefolder.listFiles(new FileFilter(exSet));
		File modulefolder = getModulesFoder().toFile();
		File[] subdir = modulefolder.listFiles();
		ArrayList<IPath> list = new ArrayList<IPath>();
		if (subdir != null) {
			for (File resource : subdir) {
				if (resource.isDirectory()
						&& !exSet.contains(resource.getName())) {
					list.add(getModulesFoder().append(resource.getName()));
				}
			}
		}
		return list.toArray(new IPath[0]);
	}
    
	public static IPath getModulesFoder() {
		return Path.fromOSString(getUapHome()).append("modules");
	}
	public static int convertStrToInt(String value){
		int intValue = 0;
		try{
			intValue = Integer.valueOf(value);
		}
		catch(NumberFormatException e){
			MessageDialog.openError(null, M_common.LfwCommonTool_0000/*错误*/, M_common.LfwCommonTool_0007/*请输入数字格式*/);
			return -10086;
		}
		return intValue;
	}
	public static boolean openFolder(File file) throws IOException {
		if (!file.exists()) {
			return false;
		}
		String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
		if (osName.indexOf("windows") != -1) { //$NON-NLS-1$
			exploreInWindowsExplorer(file);
			return true;
		} else if (osName.indexOf("mac") != -1) { //$NON-NLS-1$
			executeCommandForceDir("open", file); //$NON-NLS-1$
			return true;
		} else if (osName.indexOf("linux") != -1) { //$NON-NLS-1$
			return executeLinuxCommand(file);
		}
		return false;
	}
	/**
	 * 
	 * @param file
	 * @throws IOException
	 */
	private static void exploreInWindowsExplorer(File file) throws IOException {
		String command = "explorer.exe "; //$NON-NLS-1$
		if (file.isFile()) {
			command = "explorer.exe /SELECT,"; //$NON-NLS-1$
		}
		command = command + "\"" + file.getAbsolutePath() + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		Runtime.getRuntime().exec(command);
	}

	private static boolean executeLinuxCommand(File file) throws IOException {
		String desktop = System.getProperty("sun.desktop"); //$NON-NLS-1$
		if (desktop == null)
			desktop = ""; //$NON-NLS-1$
		desktop = desktop.toLowerCase();
		if (desktop.indexOf("gnome") != -1) //$NON-NLS-1$
			executeCommandForceDir("gnome-open", file); //$NON-NLS-1$
		else if (desktop.indexOf("konqueror") != -1 //$NON-NLS-1$
				|| desktop.indexOf("kde") != -1) { //$NON-NLS-1$
			executeCommandForceDir("konqueror", file); //$NON-NLS-1$
		} else {
			executeCommandForceDir("konqueror", file); //$NON-NLS-1$
		}
		return true;
	}

	private static void executeCommandForceDir(String baseCommand, File file)
			throws IOException {
		String forceDirectoryPath = file.getAbsolutePath();
		if (file.isFile()) {
			try {
				forceDirectoryPath = file.getParentFile().getCanonicalPath();
			} catch (IOException ex) {
				CommonPlugin.getPlugin().getLog().log(new Status(IStatus.ERROR,CommonPlugin.PLUGIN_ID,ex.getMessage(),ex));
			}
		}
		String args[] = { baseCommand, forceDirectoryPath };
		Runtime.getRuntime().exec(args);
	}
}
