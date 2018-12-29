package nc.uap.portal.project;

import nc.uap.lfw.internal.project.WEBProject;
import nc.uap.portal.core.PortalProjConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * PortalÏîÄ¿
 * 
 * @author dingrf
 *
 */
public class PortalProject extends WEBProject{

	public PortalProject(){
	}

	public PortalProject(IProject project){
		super.setProject(project);
	}

	public void configure() throws CoreException{
		super.configure();
		addToBuildSpec(PortalProjConstants.PORTAL_MODULE_CONFIG_BUILDER_ID);
	}

	public void deconfigure() throws CoreException{
		super.configure();
		removeFromBuildSpec(PortalProjConstants.PORTAL_MODULE_CONFIG_BUILDER_ID);
	}
}
