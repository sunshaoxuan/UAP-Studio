package nc.uap.lfw.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.WEBProjConstants;
import nc.uap.lfw.core.WebClassPathContainerID;
import nc.uap.lfw.internal.util.ProjCoreUtility;
import nc.uap.lfw.lang.M_wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class NewRefCodeProjectCreationOperation extends WorkspaceModifyOperation{
	private static final String MODULE_NAME = "code_ref";
	private static final String MODULE_CONFIG = "module.xml";
	private static final String LFW_CODE_PROJECT = "CODE_PROJECT";
	public static final String MODULE_REFCODE_PROPERTY = "refcode_proj";
	public NewRefCodeProjectCreationOperation(){
	}

	protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException{
		monitor.beginTask(M_wizards.NewProjectCreationOperation_0, 5);
		monitor.subTask(M_wizards.NewProjectCreationOperation_1);
		IProject project = createProject();
		monitor.worked(1);
		monitor.subTask(M_wizards.NewProjectCreationOperation_2);
		computeInitClasspath(project);
		monitor.worked(1);
		monitor.subTask(M_wizards.NewProjectCreationOperation_3);
		Map<String, String> pairMap = new HashMap<String, String>();
		pairMap.put(WEBProjConstants.MODULE_NAME_PROPERTY, MODULE_NAME);
		pairMap.put(WEBProjConstants.MODULE_CONFIG_PROPERTY, MODULE_CONFIG);
		pairMap.put(MODULE_REFCODE_PROPERTY, "true");
		ProjCoreUtility.createBuildProperties(project, pairMap);
//		monitor.worked(1);
//		monitor.subTask(M_wizards.NewProjectCreationOperation_4);
//		ProjCoreUtility.ceateInitManifest(project, MODULE_CONFIG, MODULE_NAME);
		monitor.worked(1);
	}

	protected IProject createProject() throws CoreException
	{
		IPath path = ResourcesPlugin.getWorkspace().getRoot().getLocation().append("references").append(MODULE_NAME);
		File f = path.toFile();
		if(f.exists())
			f.delete();
		f.mkdirs();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(LFW_CODE_PROJECT);
		ProjCoreUtility.createProject(project, path, null);
		project.open(null);
		if (!project.hasNature(JavaCore.NATURE_ID)){
			ProjCoreUtility.addNatureToProject(project, JavaCore.NATURE_ID, null);
		}
		if (!project.hasNature(WEBProjConstants.MODULE_NATURE))
			ProjCoreUtility.addNatureToProject(project, WEBProjConstants.MODULE_NATURE, null);
		
		return project;
	}

	private void computeInitClasspath(IProject project) throws CoreException
	{
		List<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
		list.add(ProjCoreUtility.createJREEntry());
		for (WebClassPathContainerID id : WebClassPathContainerID.values()){
			list.add(ProjCoreUtility.createContainerClasspathEntry(id));	
		}
		IJavaProject javaProject = JavaCore.create(project);
		javaProject.setRawClasspath(list.toArray(new IClasspathEntry[0]), null);
	}

}