package nc.uap.lfw.internal.core;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WebClassPathContainerID;
import nc.uap.lfw.internal.project.LFWClasspathContainer;
import nc.uap.lfw.internal.util.ProjCoreUtility;
import nc.uap.lfw.wizards.NewRefCodeProjectCreationOperation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class LFWClasspathInitializer extends ClasspathContainerInitializer
{
	public void initialize(IPath containerPath, IJavaProject javaProject) throws CoreException
	{
		if (javaProject != null)
		{
			String libname = containerPath.segment(1);
			WebClassPathContainerID  id = null;
			boolean isCodeRefProj = false;
			String refCodeProp = LfwCommonTool.getModuleProperty(javaProject.getProject(), NewRefCodeProjectCreationOperation.MODULE_REFCODE_PROPERTY);
			if(refCodeProp != null && refCodeProp.equals("true"))
				isCodeRefProj = true;
			try
			{
				id = WebClassPathContainerID.valueOf(libname);
				IClasspathEntry[] entries = null;
				if(isCodeRefProj)
					entries = ProjCoreUtility.getFullClasspathEntry(id);
				else
					entries = ProjCoreUtility.getClasspathEntry(id);
				LFWClasspathContainer container = new LFWClasspathContainer(id, entries);
				JavaCore.setClasspathContainer(container.getPath(), new IJavaProject[] { javaProject }, new IClasspathContainer[] { container }, null);
			}
			catch (IllegalArgumentException e)
			{
			}
		}
	}

	public Object getComparisonID(IPath containerPath, IJavaProject project)
	{
		return containerPath;
	}
}