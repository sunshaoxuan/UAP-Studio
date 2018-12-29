package nc.uap.lfw.internal.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

public interface IWebProjectProvider
{
	public String getProjectName();

	public IProject getProject();

	public IPath getLocationPath();

	public String getModuleName();

	//public String getModuleDesc();
	//    
	//public int getModulePriority();
	public String getModuleConfig();

	public String getPublicOut();

	public String getPublicSrc();

	public String getPrivateSrc();

	public String getPrivateOut();

	//public String getGenOut();

	//public String getGenSrc();

	public String getTestSrc();

	public String getTestOut();

	public String getClientSrc();

	public String getClientOut();
	public String getResources();
	public String getResourcesOut();
}