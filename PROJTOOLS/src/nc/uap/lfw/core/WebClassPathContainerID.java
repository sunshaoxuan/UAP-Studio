package nc.uap.lfw.core;


import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public enum WebClassPathContainerID 
{
	Ant_Library,
	Product_Common_Library,
	Product_LIB_Library,
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
	
	public static WebClassPathContainerID[] valuesTomcat() {
		return new WebClassPathContainerID[]{Product_LIB_Library, Product_Common_Library, Module_Public_Library, Module_Client_Library, Module_Private_Library, Module_Lang_Library};
	}
	
	public static WebClassPathContainerID[] valuesUap() {
		return new WebClassPathContainerID[]{Ant_Library, Product_LIB_Library, Product_Common_Library, Middleware_Library, Framework_Library, Module_Public_Library, Module_Client_Library, Module_Private_Library, Module_Lang_Library};
	}
}
