package nc.uap.lfw.launcher;

import nc.uap.lfw.core.WEBProjConstants;
import nc.uap.lfw.internal.util.ProjCoreUtility;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

public class LFWMiddlewareLaunchShortcut extends LFWLaunchShortcut
{
	protected void configLaunchConfiguration(ILaunchConfigurationWorkingCopy wc)
	{
		ProjCoreUtility.configLaunchConfiguration(wc);
	}

	protected String getMainClass()
	{
		return WEBProjConstants.SERVER_MAIN_CLASS;
	}

	protected String getName()
	{
		return "_Server";
	}
}