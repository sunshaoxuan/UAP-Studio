/*
 * Created on 2005-8-11
 * @author ºÎ¹ÚÓî
 */
package nc.uap.portal.wizards;

import nc.uap.lfw.internal.project.IProjectProvider;
import nc.uap.lfw.internal.util.ProjCoreUtility;
import nc.uap.lfw.wizards.NewProjectCreationOperation;
import nc.uap.portal.core.PortalProjConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class PortalProjectCreationOperation extends NewProjectCreationOperation
{

	public PortalProjectCreationOperation(IProjectProvider provider)
	{
		super(provider);
	}

	protected IProject createProject() throws CoreException
	{
//		return super.createProject();
		IProject project = super.createProject();
		if (!project.hasNature(PortalProjConstants.PORTAL_MODULE_NATURE))
			ProjCoreUtility.addNatureToProject(project, PortalProjConstants.PORTAL_MODULE_NATURE, null);
		return project;
	}
}