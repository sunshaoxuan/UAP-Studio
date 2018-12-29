package nc.uap.lfw.perspective.action;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.internal.util.ProjCoreUtility;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

public class LFWMiddlewareLaunchShortcut extends LFWLaunchShortcut
{
	protected void configLaunchConfiguration(ILaunchConfigurationWorkingCopy wc)
	{
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, WEBPersConstants.SERVER_MAIN_CLASS);
//		String ncHome = toVarRepresentation(WEBProjConstants.FIELD_NC_HOME);
		String ncHomePath = LfwCommonTool.getUapHome();
		WEBPersPlugin.getVariableManager().getValueVariable(WEBPersConstants.FIELD_NC_HOME).setValue(ncHomePath);
		String ncHome = "${"+WEBPersConstants.FIELD_NC_HOME+"}";
		StringBuffer vmargs = new StringBuffer();
		vmargs.append("-Dnc.exclude.modules=").append("${"+WEBPersConstants.FIELD_EX_MODULES+"}").append(" ");
		vmargs.append("-Dnc.runMode=develop -Dnc.server.location=").append(ncHome).append(" ");
		vmargs.append("-DEJBConfigDir=").append(ncHome).append("/").append("ejbXMLs").append(" ");
		vmargs.append("-Dorg.owasp.esapi.resources=").append(ncHome).append("/ierp/bin/esapi").append(" ");
		vmargs.append("-DExtServiceConfigDir=").append(ncHome).append("/").append("ejbXMLs").append(" ");
		vmargs.append("-Duap.hotwebs=lfw,portal,fs").append(" ");
		vmargs.append("-Duap.disable.codescan=false").append(" ");
		vmargs.append("-Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl").append(" ");
		vmargs.append("-Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl").append(" ");
		vmargs.append("-Xms512M").append(" ");
		vmargs.append("-Xmx1024M").append(" ");
		vmargs.append("-XX:NewSize=96M").append(" ");
		vmargs.append("-XX:MaxPermSize=128M").append(" ");		
		//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER, MDEConstants.MW_CLASSPATH_PROVIDER);
		//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_SOURCE_PATH_PROVIDER, MDEConstants.NC_SOURCEPATH_PROVIDER);
		wc.setAttribute(IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, false);
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmargs.toString());
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, ncHome);
	}

	protected String getMainClass()
	{
		return WEBPersConstants.SERVER_MAIN_CLASS;
	}

	protected String getName()
	{
		return "_Server";
	}
}
