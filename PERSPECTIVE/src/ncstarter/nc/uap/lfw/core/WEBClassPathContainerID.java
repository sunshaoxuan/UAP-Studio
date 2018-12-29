package nc.uap.lfw.core;


import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public enum WEBClassPathContainerID 
{
	Ant_Library,
	Product_Common_Library,
	Middleware_Library,
	Framework_Library,
	Module_Public_Library,
	Module_Client_Library,
	Module_Private_Library,
	Module_Lang_Library;
//	Generated_EJB;
	public IPath getPath()
	{
		return new Path(WEBProjConstants.LFW_LIBRARY_CONTAINER_ID).append(name());
	}
}
