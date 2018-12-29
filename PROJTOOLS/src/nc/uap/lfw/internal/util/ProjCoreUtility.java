package nc.uap.lfw.internal.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.common.SpaceMode;
import nc.uap.lfw.core.WEBProjConstants;
import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.core.WebClassPathContainerID;
import nc.uap.lfw.internal.project.LFWClasspathContainer;
import nc.uap.lfw.internal.project.WEBProject;
import nc.uap.lfw.lang.M_internal;
import nc.uap.lfw.model.LFWLibraryLocation;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

public class ProjCoreUtility {
	public static IAccessRule[]	Fobidden	= new IAccessRule[] { JavaCore.newAccessRule(new Path("**/*"), IAccessRule.K_NON_ACCESSIBLE) }; //$NON-NLS-1$
	public static IAccessRule[]	Discouraged	= new IAccessRule[] { JavaCore.newAccessRule(new Path("**/*"), IAccessRule.K_DISCOURAGED) }; //$NON-NLS-1$
	public static IAccessRule[]	Accessible	= {};
	private static String idePath = Platform.getInstallLocation().getURL().getPath();

	public static void createProject(IProject project, IPath location, IProgressMonitor monitor) throws CoreException
	{
		if (!Platform.getLocation().equals(location))
		{
			IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
			desc.setLocation(location);
			project.create(desc, monitor);
		}
		else
			project.create(monitor);
	}


	public static IClasspathEntry[] getFullClasspathEntry(WebClassPathContainerID id) throws CoreException
	{
//		return getSimpleClasspathEntry(project, id);
//		WEBProject webProj = ProjCoreUtility.createLfwProject(project);
		
		
		switch (id)
		{
			case Module_Public_Library:
				return computeClasspathEntry(ClasspathComputer.computeStandCP(LfwCommonTool.getAccessibleModuleFolders()), Accessible);
			case Module_Client_Library:
				return computeClasspathEntry(ClasspathComputer.computeStandCP(getModuleAccessibleClientFolders()), null);
			case Module_Private_Library:
				return computeClasspathEntry(ClasspathComputer.computeStandCP(getModulePrivateFolders()), Fobidden);
			case Module_Lang_Library:
				LFWLibraryLocation[] libs = ClasspathComputer.computeJarsInPath(new IPath[]{getModulesLanglibFoder()});
				List<LFWLibraryLocation> libList = new ArrayList<LFWLibraryLocation>();
				IPath[] paths = LfwCommonTool.getAccessibleModuleFolders();
				String[] modules = getModules(paths);
				for (int i = 0; i < libs.length; i++) {
					LFWLibraryLocation lfwLibraryLocation = libs[i];
					if(exLangLib(modules, lfwLibraryLocation))
						continue;
					libList.add(libs[i]);
				}
				return computeClasspathEntry(libList.toArray(new LFWLibraryLocation[0]), Accessible);
			case Product_Common_Library:
				return computeClasspathEntry(ClasspathComputer.computeStandCP(new IPath[] {getNCHOME(), getExternalFoder()}), Accessible);
			case Framework_Library:
				return computeClasspathEntry(ClasspathComputer.computeJarsInPath(new IPath[]{getFrameworkFoder()}), Accessible);
//			case Generated_EJB:
//				return computeClasspathEntry(ClasspathComputer.computeJarsInPath(new IPath[]{mdeproject.getEjbFoder()}), Fobidden);
			case Middleware_Library:
				return computeClasspathEntry(ClasspathComputer.computeJarsInPath(new IPath[]{getMiddlewareFoder()}), Accessible);
			case Ant_Library:
				return computeClasspathEntry(ClasspathComputer.computeJarsInPath(new IPath[]{getAntFoder()}), Accessible);
			case Product_LIB_Library:
				if(LfwCommonTool.getSpaceMode().equals(SpaceMode.STANDALONG))
					return computeClasspathEntry(ClasspathComputer.computeJarsInPath(new IPath[]{getTomcatLibFolder()}), Accessible);
				else
					return new IClasspathEntry[0];
			default:
				return new IClasspathEntry[0];
		}
	}
	
	public static IPath[] getModuleAccessibleClientFolders() throws CoreException {
		ArrayList<IPath> list = new ArrayList<IPath>();
		IPath[] publicfolders = LfwCommonTool.getAccessibleModuleFolders();
		for (IPath folder : publicfolders) {
//			String name = folder.lastSegment();
			//if (name.startsWith("uap") || name.equals(getModuleName())) {
				list.add(folder.append("client"));
			//}
		}
		return list.toArray(new IPath[0]);
	}
	
	public static IClasspathEntry[] getClasspathEntry(WebClassPathContainerID id) throws CoreException
	{
//		WEBProject webProj = ProjCoreUtility.createLfwProject(project);
		int idebase = idePath.lastIndexOf("Platform");
		String pluginsPath = null;
		if(idebase != -1)
			pluginsPath = idePath.substring(1, idePath.lastIndexOf("Platform")) + "WEB/plugins";
		else
			pluginsPath = idePath.substring(1) + "WEB/plugins";
		try{
			switch (id)
			{
				case Module_Public_Library:
					return computeClasspathEntry(ClasspathComputer.computeStandCP(LfwCommonTool.getAccessibleModuleFolders()), Accessible);
				case Module_Client_Library:
					return computeClasspathEntry(ClasspathComputer.computeCPByList(new IPath[]{getNCHOME().append("modules").append("uap").append("client")}, new FileInputStream(new File(pluginsPath+"/uapclient-list.txt"))), Accessible);
				case Module_Private_Library:
					return getNullLibs();
				case Module_Lang_Library:
					return getNullLibs();
				case Product_Common_Library:
					return computeClasspathEntry(ClasspathComputer.computeCPByList(new IPath[] {getTomcatLibFolder(), getExternalFoder()}, new FileInputStream(new File(pluginsPath+"/common-list.txt"))), Accessible);
				case Framework_Library:
					return getNullLibs();
				case Middleware_Library:
					return computeClasspathEntry(ClasspathComputer.computeCPByList(new IPath[]{getMiddlewareFoder()}, new FileInputStream(new File(pluginsPath+"/middleware-list.txt"))), Accessible);
				case Ant_Library:
					return getNullLibs();
				case Product_LIB_Library:
					return getNullLibs();
				default:
					return getNullLibs();
			}
		}
		catch(Exception e){
			WEBProjPlugin.getDefault().logError(e.getMessage(),e);
		}
		return getNullLibs();
	}
	
	public static IPath getAntFoder() {
		return getNCHOME().append("ant");
	}


	public static IPath getModulesLanglibFoder() {
		return getNCHOME().append("langlib");
	}

	public static IPath getExternalFoder() {
		return getNCHOME().append("external");
	}

	public static IPath getEjbFoder() {
		return getNCHOME().append("ejb");
	}

	public static IPath getFrameworkFoder() {
		return getNCHOME().append("framework");
	}

	public static IPath getMiddlewareFoder() {
		return getNCHOME().append("middleware");
	}
	
	public static IPath getTomcatLibFolder() {
		return getNCHOME().append("lib");
	}
	
	public static IPath getNCHOME() {
		// return getProject().getFolder("NC_HOME");
		// return Path.fromOSString(getVariableManager().getValueVariable(
		// "FIELD_NC_HOME").getValue
		// ());
		return Path.fromOSString(LfwCommonTool.getUapHome());
	}
	
	private static IClasspathEntry[] getNullLibs() {
		return new IClasspathEntry[0];
	}

	private static String[] getModules(IPath[] paths) {
		String[] names = new String[paths.length];
		for (int i = 0; i < names.length; i++) {
			IPath path = paths[i];
			names[i] = path.segment(path.segmentCount() - 1);
		}
		return names;
	}

	private static boolean exLangLib(String[] exs, LFWLibraryLocation lfwLibraryLocation) {
		String pre = lfwLibraryLocation.getLibPath().toPortableString();
		int index = pre.lastIndexOf("/");
		pre = pre.substring(index + 1);
		index = pre.indexOf("_");
		pre = pre.substring(0, index);
		for (int i = 0; i < exs.length; i++) {
			String ex = exs[i];
			if(ex.equals(pre))
				return false;
		}
		return true;
	}

	public static IPath[] getModulePrivateFolders() throws CoreException {
		IPath[] publicfolders = LfwCommonTool.getAccessibleModuleFolders();
		IPath[] subpublicfolders = new IPath[publicfolders.length];
		for (int i = 0; i < publicfolders.length; i++) {
			subpublicfolders[i] = publicfolders[i].append("META-INF");
		}
		return subpublicfolders;
	}

//	public static IPath[] getModuleDiscouragedClientFolders() throws CoreException {
//		ArrayList<IPath> list = new ArrayList<IPath>();
//		IPath[] publicfolders = LfwCommonTool.getAccessibleModuleFolders();
//		for (IPath folder : publicfolders) {
//			String name = folder.lastSegment();
//			if (!name.startsWith("uap") && !name.equals(getModuleName())) {
//				list.add(folder.append("client"));
//			}
//		}
//		return list.toArray(new IPath[0]);
//	}
	
	public static IClasspathEntry[] computeClasspathEntry(LFWLibraryLocation[] accessiblelibs, LFWLibraryLocation[] discouragedlibs, LFWLibraryLocation[] fobiddenlibs)
	throws CoreException
	{
		IClasspathEntry[] accessibleEntries = computeClasspathEntry(accessiblelibs, Accessible);
		IClasspathEntry[] discouragedEntries = computeClasspathEntry(discouragedlibs, Discouraged);
		IClasspathEntry[] fobiddenEntries = computeClasspathEntry(fobiddenlibs, Fobidden);
		IClasspathEntry[] allentries = new IClasspathEntry[accessibleEntries.length + discouragedEntries.length + fobiddenEntries.length];
		System.arraycopy(accessibleEntries, 0, allentries, 0, accessibleEntries.length);
		System.arraycopy(discouragedEntries, 0, allentries, accessibleEntries.length, discouragedEntries.length);
		System.arraycopy(fobiddenEntries, 0, allentries, accessibleEntries.length + discouragedEntries.length, fobiddenEntries.length);
		return allentries;
	}
	
	
	 public static IPath getModulesFolderPath()
    {
        return getNcHomeFolderPath().append("modules"); //$NON-NLS-1$
    }

	 public static IPath getNcHomeFolderPath()
    {
          return LfwCommonTool.getNCHomePath();
    }

	public static IClasspathEntry[] computeClasspathEntry(LFWLibraryLocation[] libs, IAccessRule[] rules) throws CoreException
	{
		ArrayList<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
		if (libs != null)
		{
			for (LFWLibraryLocation lib : libs)
			{
				IClasspathAttribute[] atts = new IClasspathAttribute[0];
				if (lib.getDocLocation() != null)
				{
					atts = new IClasspathAttribute[] { JavaCore
							.newClasspathAttribute(IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME, lib.getDocLocation()) };
				}
				IClasspathEntry entry = JavaCore.newLibraryEntry(lib.getLibPath(), lib.getSrcPath(), null, rules, atts, false);
				list.add(entry);
			}
		}
		return list.toArray(new IClasspathEntry[0]);
	}
	
	
	public static LFWClasspathContainer getLFWClasspathContainer(IPath path, IJavaProject javaProject)
	{
		try
		{
			return (LFWClasspathContainer) JavaCore.getClasspathContainer(path, javaProject);
		}
		catch (JavaModelException e)
		{
			return null;
		}
	}
	
	
//	public static void fileCopy(IFile form, IFile to) throws CoreException, IOException
//	{
//		InputStream in = form.getContents();
//		if (to.exists())
//		{
//			silentSetWriterable(to);
//			to.setContents(in, true, false, null);
//		}
//		else
//		{
//			to.create(in, true, null);
//		}
//		in.close();
//	}

	public static void addNatureToProject(IProject proj, String natureId, IProgressMonitor monitor) throws CoreException
	{
		IProjectDescription description = proj.getDescription();
		String[] prevNatures = description.getNatureIds();
		String[] newNatures = new String[prevNatures.length + 1];
		System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
		newNatures[prevNatures.length] = natureId;
		description.setNatureIds(newNatures);
		proj.setDescription(description, monitor);
	}

	public static void createFolder(IFolder folder) throws CoreException
	{
		if (!folder.exists())
		{
			IContainer parent = folder.getParent();
			if (parent instanceof IFolder)
			{
				createFolder((IFolder) parent);
			}
			folder.create(true, true, null);
		}
	}

	public static IClasspathEntry createJREEntry()
	{
		return JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER")); //$NON-NLS-1$
	}

	public static IClasspathEntry createSourceEntry(IProject project, String src, String output) throws CoreException
	{
		IFolder folder = project.getFolder(src);
		if (!folder.exists())
			ProjCoreUtility.createFolder(folder);
		folder = project.getFolder(output);
		if (!folder.exists())
			ProjCoreUtility.createFolder(folder);
		IPath path = project.getFullPath().append(src);
		IPath outPath = project.getFullPath().append(output);
		return JavaCore.newSourceEntry(path, new IPath[0], new IPath[0], outPath);
	}

	public static IClasspathEntry createSourceEntry(IProject project, String src) throws CoreException
	{
		IFolder folder = project.getFolder(src);
		if (!folder.exists())
			ProjCoreUtility.createFolder(folder);
		IPath path = project.getFullPath().append(src);
		return JavaCore.newSourceEntry(path, new IPath[0]);
	}
	
	public static IFolder creatFolderLink(IProject project, String folderName, final IPath linkPath) throws CoreException
	{
		IFolder folderHandle = createFolderHandle(project, folderName);
		if (!folderHandle.exists())
		{
			if (linkPath == null)
				folderHandle.create(false, true, null);
			else
				folderHandle.createLink(linkPath, IResource.ALLOW_MISSING_LOCAL, null);
		}
		else
		{
			if (!folderHandle.getRawLocation().equals(linkPath))
			{
				folderHandle.delete(true, null);
				folderHandle = creatFolderLink(project, folderName, linkPath);
			}
		}
		return folderHandle;
	}

	public static IFolder createFolderHandle(IProject project, String folderName)
	{
		IWorkspaceRoot workspaceRoot = project.getWorkspace().getRoot();
		IPath folderPath = project.getFullPath().append(folderName);
		IFolder folderHandle = workspaceRoot.getFolder(folderPath);
		return folderHandle;
	}

//	public static MDEClasspathContainer getMDEClasspathContainer(IPath path, IJavaProject javaProject)
//	{
//		try
//		{
//			return (MDEClasspathContainer) JavaCore.getClasspathContainer(path, javaProject);
//		}
//		catch (JavaModelException e)
//		{
//			return null;
//		}
//	}

	public static boolean isModuleProject(IProject project)
	{
		if (project.isOpen())
		{
			try
			{
				return project.hasNature(WEBProjConstants.MODULE_NATURE);
			}
			catch (CoreException e)
			{
			}
		}
		return false;
	}
	
	public static boolean isPortalProject(IProject project){
		if(project.isOpen()){
			try{
				return (project.hasNature(WEBProjConstants.MODULE_NATURE) && project.hasNature(WEBProjConstants.PORTAL_MODULE_NATURE));
			}catch(CoreException e){
				
			}
		}
		return false;
	}

//	public static void configLaunchConfiguration(ILaunchConfigurationWorkingCopy wc)
//	{
//		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, MDEConstants.SERVER_MAIN_CLASS);
//		String ncHome = toVarRepresentation(MDEConstants.FIELD_NC_HOME);
//		StringBuffer vmargs = new StringBuffer();
//		vmargs.append("-Dnc.exclude.modules=").append(toVarRepresentation(MDEConstants.FIELD_EX_MODULES)).append(" ");
//		vmargs.append("-Dnc.runMode=develop -Dnc.server.location=").append(ncHome).append(" ");
//		vmargs.append("-DEJBConfigDir=").append(ncHome).append("/").append("ejbXMLs").append(" ");
//		vmargs.append("-DExtServiceConfigDir=").append(ncHome).append("/").append("ejbXMLs");
//		//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER, MDEConstants.MW_CLASSPATH_PROVIDER);
//		//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_SOURCE_PATH_PROVIDER, MDEConstants.NC_SOURCEPATH_PROVIDER);
//		wc.setAttribute(IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, false);
//		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmargs.toString());
//		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, toVarRepresentation(MDEConstants.FIELD_NC_HOME));
//	}
//
//	public static void configJStarterConfiguration(ILaunchConfigurationWorkingCopy wc)
//	{
//		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, MDEConstants.JSTARTER_CLASS);
//		//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getJavaProject().getElementName());
//		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER, MDEConstants.MW_CLASSPATH_PROVIDER);
//		//		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_SOURCE_PATH_PROVIDER, MDEConstants.NC_SOURCEPATH_PROVIDER);
//		wc.setAttribute(IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, false);
//		StringBuffer vmargs = new StringBuffer();
//		vmargs.append("-Dnc.runMode=develop -Dnc.jstart.server=" +
//				toVarRepresentation(MDEConstants.FIELD_CLINET_IP) +
//				" -Dnc.jstart.port=" +
//				toVarRepresentation(MDEConstants.FIELD_CLINET_PORT));
//		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmargs.toString());
//		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, toVarRepresentation(MDEConstants.FIELD_NC_HOME));
//	}
	
//	public static void configLFWLaunchConfiguration(ILaunchConfigurationWorkingCopy wc)
//	{
//		IProject project = null;
//		try {
//			project = ResourcesPlugin.getWorkspace().getRoot().getProject(wc.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""));
//			
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, WEBProjConstants.LFW_SERVER_MAIN_CLASS);
//		String ncHome = toVarRepresentation(WEBProjConstants.FIELD_NC_HOME);
//		StringBuffer vmargs = new StringBuffer();
//		vmargs.append("-Dnc.runMode=develop -Dnc.server.location=").append(ncHome).append(" ");
//		vmargs.append("-DEJBConfigDir=").append(ncHome).append("/").append("ejbXMLs").append(" ");
//		vmargs.append("-DExtServiceConfigDir=").append(ncHome).append("/").append("ejbXMLs").append(" ");
//	
//		vmargs.append("-Dweb.context=" + LFWUtility.getContextFromResource(project));
//		
//		//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER, MDEConstants.MW_CLASSPATH_PROVIDER);
//		//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_SOURCE_PATH_PROVIDER, MDEConstants.NC_SOURCEPATH_PROVIDER);
//		wc.setAttribute(IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, false);
//		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmargs.toString());
//		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, toVarRepresentation(WEBProjConstants.FIELD_NC_HOME));
//	}

	public static String getPrefix(String fullName)
	{
		if (fullName == null)
			return null;
		int lastDotIndex = fullName.lastIndexOf('.');
		if (lastDotIndex > 0)
			return fullName.substring(0, lastDotIndex);
		else
			return fullName;
	}

	public static String getExtension(String fullName)
	{
		if (fullName == null)
			return null;
		int lastDotIndex = fullName.lastIndexOf('.');
		if (lastDotIndex > 0 && lastDotIndex != (fullName.length() - 1))
			return fullName.substring(lastDotIndex + 1);
		else
			return null;
	}

	public static void refreshWorkspace(IProgressMonitor monitor)
	{
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects)
		{
			try
			{
				if (project.isOpen() && project.hasNature(WEBProjConstants.MODULE_NATURE))
				{
					project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
				}
			}
			catch (CoreException e)
			{
				WEBProjPlugin.getDefault().logError(e);
			}
		}
	}

	public static void updateWorkspaceClasspath()
	{
		Job job = new Job(M_internal.ProjCoreUtility_1)
		{
			public IStatus run(IProgressMonitor monitor)
			{
				updateWorkspaceClasspath(monitor);
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	private static void updateWorkspaceClasspath(IProgressMonitor monitor)
	{
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects)
		{
			try
			{
				if (project.isOpen() && project.hasNature(WEBProjConstants.MODULE_NATURE))
				{
					monitor.beginTask("LFW", 100); //$NON-NLS-1$
					project.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 50));
					ClasspathComputer.updateClasspath(project, new SubProgressMonitor(monitor, 50));
//					updateLFWCopyFolder(project);
				}
			}
			catch (CoreException e)
			{
				WEBProjPlugin.getDefault().logError(e);
			}
		}
	}
	
	public static void configLaunchConfiguration(ILaunchConfigurationWorkingCopy wc){
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, WEBProjConstants.SERVER_MAIN_CLASS);
		String ncHomePath = LfwCommonTool.getUapHome();
		LfwCommonTool.getVariableManager().getValueVariable(WEBProjConstants.FIELD_NC_HOME).setValue(ncHomePath);
		String ncHome = toVarRepresentation(WEBProjConstants.FIELD_NC_HOME);
		StringBuffer vmargs = new StringBuffer();
		vmargs.append("-Dnc.exclude.modules=").append(toVarRepresentation(LfwCommonTool.FIELD_EX_MODULES)).append(" "); //$NON-NLS-1$ //$NON-NLS-2$
		vmargs.append("-Dnc.runMode=develop -Dnc.server.location=").append(ncHome).append(" "); //$NON-NLS-1$ //$NON-NLS-2$
		vmargs.append("-DEJBConfigDir=").append(ncHome).append("/").append("ejbXMLs").append(" "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		vmargs.append("-Dorg.owasp.esapi.resources=").append(ncHome).append("/ierp/bin/esapi").append(" ");
		vmargs.append("-DExtServiceConfigDir=").append(ncHome).append("/").append("ejbXMLs").append(" "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		vmargs.append("-Duap.hotwebs=lfw,portal,fs").append(" ");
		vmargs.append("-Duap.disable.codescan=false").append(" ");
		vmargs.append("-Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl").append(" ");
		vmargs.append("-Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl").append(" ");
		vmargs.append("-Xms512M").append(" "); //$NON-NLS-1$ //$NON-NLS-2$
		vmargs.append("-Xmx1024M").append(" "); //$NON-NLS-1$ //$NON-NLS-2$
		vmargs.append("-XX:NewSize=96M").append(" "); //$NON-NLS-1$ //$NON-NLS-2$
		vmargs.append("-XX:MaxPermSize=128M").append(" ");	 //$NON-NLS-1$ //$NON-NLS-2$
		//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER, MDEConstants.MW_CLASSPATH_PROVIDER);
		//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_SOURCE_PATH_PROVIDER, MDEConstants.NC_SOURCEPATH_PROVIDER);
		wc.setAttribute(IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, false);
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmargs.toString());
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, ncHome);
	}
	public static void configJStarterConfiguration(ILaunchConfigurationWorkingCopy wc){
	    wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "nc.starter.test.JStarter");
	    String ncHomePath = LfwCommonTool.getUapHome();
	    LfwCommonTool.getVariableManager().getValueVariable(WEBProjConstants.FIELD_NC_HOME).setValue(ncHomePath);

	    wc.setAttribute("org.eclipse.debug.ui.ATTR_LAUNCH_IN_BACKGROUND", false);
	    StringBuffer vmargs = new StringBuffer();
	    vmargs.append("-Dnc.runMode=develop -Dnc.jstart.server=" + 
	      toVarRepresentation("FIELD_CLINET_IP") + " -Dnc.jstart.port=" + 
	      toVarRepresentation("FIELD_CLINET_PORT")).append(" ");
	    vmargs.append("-Duap.hotwebs=lfw,portal").append(" ");
	    vmargs.append(" -Xms512M").append(" "); //$NON-NLS-1$ //$NON-NLS-2$
		vmargs.append("-Xmx1024M").append(" "); //$NON-NLS-1$ //$NON-NLS-2$
		vmargs.append("-XX:NewSize=96M").append(" "); //$NON-NLS-1$ //$NON-NLS-2$
		vmargs.append("-XX:MaxPermSize=128M").append(" ");	 //$NON-NLS-1$ //$NON-NLS-2$
	    wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, 
	      vmargs.toString());
	    wc.setAttribute(
	      IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, 
	      toVarRepresentation("FIELD_NC_HOME"));
	}

	public static void createBuildProperties(IProject project, Map<String, String> pairMap){
		IFile file = project.getFile(".module_prj"); //$NON-NLS-1$
		StringWriter swriter = new StringWriter();
		PrintWriter writer = new PrintWriter(swriter);
		Iterator<Entry<String, String>> it = pairMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, String> entry = it.next();
			writer.println(entry.getKey() + "=" + entry.getValue()); //$NON-NLS-1$
		}
		String initContent = swriter.getBuffer().toString();
		try
		{
			ByteArrayInputStream stream = new ByteArrayInputStream(initContent.getBytes("8859_1")); //$NON-NLS-1$
			if (file.exists())
			{
				file.setContents(stream, false, false, null);
			}
			else
			{
				file.create(stream, false, null);
			}
			stream.close();
		}
		catch (CoreException e)
		{
		}
		catch (IOException e)
		{
		}
	}

	public static void ceateInitManifest(IProject project, String moduleConfig, String moduleName) throws CoreException
	{
		IFolder folder = project.getFolder("META-INF"); //$NON-NLS-1$
		if (!folder.exists())
			createFolder(folder);
		if (moduleConfig != null)
		{
			IFile file = project.getFile("META-INF/" + moduleConfig); //$NON-NLS-1$
			StringWriter swriter = new StringWriter();
			PrintWriter writer = new PrintWriter(swriter);
			writer.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>"); //$NON-NLS-1$
			writer.print("<module"); //$NON-NLS-1$
			if (moduleName != null && "module.xml".equals(moduleConfig)) //$NON-NLS-1$
			{
				writer.print(" name=\"" + moduleName + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			}
			//        writer.print(" priority=\"" + fProjectProvider.getModulePriority() + "\"");
			writer.println(">"); //$NON-NLS-1$
			writer.println("    <public>"); //$NON-NLS-1$
			writer.println("    </public>"); //$NON-NLS-1$
			writer.println("    <private>"); //$NON-NLS-1$
			writer.println("    </private>"); //$NON-NLS-1$
			writer.println("</module>"); //$NON-NLS-1$
			String initContent = swriter.getBuffer().toString();
			try
			{
				ByteArrayInputStream stream = new ByteArrayInputStream(initContent.getBytes("8859_1")); //$NON-NLS-1$
				if (file.exists())
				{
					file.setContents(stream, false, false, null);
				}
				else
				{
					file.create(stream, false, null);
				}
				stream.close();
			}
			catch (CoreException e)
			{
			}
			catch (IOException e)
			{
			}
		}
	}

	public static WEBProject createLfwProject(IProject project)
	{
		try
		{
			if (!project.hasNature(WEBProjConstants.MODULE_NATURE))
			{
				throw new IllegalArgumentException(M_internal.ProjCoreUtility_3);
			}
		}
		catch (CoreException e)
		{
			throw new IllegalArgumentException(M_internal.ProjCoreUtility_4);
		}
		return new WEBProject(project);
	}
	
	public static String toVarRepresentation(String varName)
	{
		return "${" + varName + "}"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static IClasspathEntry createContainerClasspathEntry(WebClassPathContainerID id)
	{
		return JavaCore.newContainerEntry(new Path(WEBProjConstants.LFW_LIBRARY_CONTAINER_ID).append(id.name()), false);
	}

}
