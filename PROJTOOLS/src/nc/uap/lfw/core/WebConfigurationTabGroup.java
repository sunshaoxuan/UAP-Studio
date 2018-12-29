package nc.uap.lfw.core;

import nc.uap.lfw.internal.util.ProjCoreUtility;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.internal.debug.ui.launcher.LocalJavaApplicationTabGroup;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

public class WebConfigurationTabGroup extends LocalJavaApplicationTabGroup
{
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration)
	{
		super.setDefaults(configuration);
		configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, ProjCoreUtility.toVarRepresentation(WEBProjConstants.FIELD_NC_HOME));
	}
}