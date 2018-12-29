package nc.uap.lfw.internal.util;

import java.util.ArrayList;

import nc.uap.lfw.core.WEBClassPathContainerID;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.internal.project.LFWClasspathContainer;
import nc.uap.lfw.internal.project.WEBProject;
import nc.uap.lfw.model.LibraryLocation;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
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
	public static IAccessRule[]	Fobidden	= new IAccessRule[] { JavaCore.newAccessRule(new Path("**/*"), IAccessRule.K_NON_ACCESSIBLE) };
	public static IAccessRule[]	Discouraged	= new IAccessRule[] { JavaCore.newAccessRule(new Path("**/*"), IAccessRule.K_DISCOURAGED) };
	public static IAccessRule[]	Accessible	= {};
	//private static final String targetFolder = "web";

	
	public static IClasspathEntry[] getClasspathEntry(IProject project, WEBClassPathContainerID id) throws CoreException
	{
		WEBProject mdeproject = ProjCoreUtility.createMDEProject(project);
		switch (id)
		{
			case Module_Public_Library:
				return computeClasspathEntry(ClasspathComputer.computeStandCP(mdeproject.getAccessibleModuleFolders()), Accessible);
			case Module_Client_Library:
				return computeClasspathEntry(ClasspathComputer.computeStandCP(mdeproject.getModuleAccessibleClientFolders()), ClasspathComputer
						.computeStandCP(mdeproject.getModuleDiscouragedClientFolders()), null);
			case Module_Private_Library:
				return computeClasspathEntry(ClasspathComputer.computeStandCP(mdeproject.getModulePrivateFolders()), Fobidden);
			case Module_Lang_Library:
				return computeClasspathEntry(ClasspathComputer.computeJarsInPath(mdeproject.getModulesLanglibFoder()), Accessible);
			case Product_Common_Library:
				return computeClasspathEntry(ClasspathComputer.computeStandCP(mdeproject.getNCHOME(), mdeproject.getExternalFoder()), Accessible);
			case Framework_Library:
				return computeClasspathEntry(ClasspathComputer.computeJarsInPath(mdeproject.getFrameworkFoder()), Accessible);
//			case Generated_EJB:
//				return computeClasspathEntry(ClasspathComputer.computeJarsInPath(mdeproject.getEjbFoder()), Fobidden);
			case Middleware_Library:
				return computeClasspathEntry(ClasspathComputer.computeJarsInPath(mdeproject.getMiddlewareFoder()), Accessible);
			case Ant_Library:
				return computeClasspathEntry(ClasspathComputer.computeJarsInPath(mdeproject.getAntFoder()), Accessible);
			default:
				return new IClasspathEntry[0];
		}
	}

	public static IClasspathEntry[] computeClasspathEntry(LibraryLocation[] accessiblelibs, LibraryLocation[] discouragedlibs, LibraryLocation[] fobiddenlibs)
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
	
	
	public static IClasspathEntry[] computeClasspathEntry(LibraryLocation[] libs, IAccessRule[] rules) throws CoreException
	{
		ArrayList<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
		if (libs != null)
		{
			for (LibraryLocation lib : libs)
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
		return JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER"));
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

	
	public static void configLaunchConfiguration(ILaunchConfigurationWorkingCopy wc)
	{
		//wc.setAttribute(IJavaLaunchConfigurationConstants., value)
//		String ncHome = toVarRepresentation(WEBProjConstants.FIELD_NC_HOME);
		StringBuffer vmargs = new StringBuffer();
		//-Dnc.runMode=develop -Dnc.jstart.server=${FIELD_CLINET_IP} -Dnc.jstart.port=${FIELD_CLINET_PORT}
//		vmargs.append("-Dnc.exclude.modules=").append(toVarRepresentation(WEBProjConstants.FIELD_EX_MODULES)).append(" ");
//		vmargs.append("-Dnc.runMode=develop -Dnc.server.location=").append(ncHome).append(" ");
//		vmargs.append("-DEJBConfigDir=").append(ncHome).append("/").append("ejbXMLs").append(" ");
//		vmargs.append("-DExtServiceConfigDir=").append(ncHome).append("/").append("ejbXMLs");
		//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER, MDEConstants.MW_CLASSPATH_PROVIDER);
		//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_SOURCE_PATH_PROVIDER, MDEConstants.NC_SOURCEPATH_PROVIDER);
		wc.setAttribute(IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, true);
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmargs.toString());
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, toVarRepresentation(WEBPersConstants.FIELD_NC_HOME));
	}

	


	private static WEBProject createMDEProject(IProject project)
	{
		try
		{
			if (!project.hasNature(WEBPersConstants.MODULE_NATURE))
			{
				throw new IllegalArgumentException("无效的参数,不是MDEProject");
			}
		}
		catch (CoreException e)
		{
			throw new IllegalArgumentException("无效的参数,不是MDEProject");
		}
		return new WEBProject(project);
	}

	public static String toVarRepresentation(String varName)
	{
		return "${" + varName + "}";
	}

}
