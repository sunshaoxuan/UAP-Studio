package nc.uap.lfw.launcher;

import nc.uap.lfw.core.WEBProjConstants;
import nc.uap.lfw.internal.util.ProjCoreUtility;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

public class LFWClientLaunchShortcut extends LFWLaunchShortcut{

	protected void configLaunchConfiguration(ILaunchConfigurationWorkingCopy wc)
	{
		ProjCoreUtility.configJStarterConfiguration(wc);
	}

	protected String getMainClass()
	{
		return WEBProjConstants.CLIENT_MAIN_CLASS;
	}

	protected String getName()
	{
		return "_JStarter";
	}
}
