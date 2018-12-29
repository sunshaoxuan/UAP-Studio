package nc.uap.lfw.perspective.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.lang.M_perspective;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * debug/run LFW ÏîÄ¿
 * @author zhangxya
 *
 */
abstract public class LFWLaunchShortcut implements ILaunchShortcut
{
	public void launch(ISelection selection, String mode)
	{
		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection structSelection = (IStructuredSelection) selection;
			Object prjObject = structSelection.getFirstElement();
			if (prjObject instanceof IAdaptable)
			{
				IProject project = (IProject) ((IAdaptable) prjObject).getAdapter(IProject.class);
				try
				{
					if (project != null && project.hasNature(WEBPersConstants.MODULE_NATURE))
					{
						launch(project, mode);
					}
				}
				catch (CoreException e)
				{
					MainPlugin.getDefault().logError(e);
				}
			}
		}
	}

	public void launch(IEditorPart editor, String mode)
	{
	}

	public void launch(IProject project, String mode) throws CoreException
	{
		IJavaProject javaProject = JavaCore.create(project);
		ILaunchConfiguration config = findLaunchConfiguration(javaProject, mode);
		if (config != null)
		{
			DebugUITools.launch(config, mode);
		}
	}

	protected ILaunchConfigurationType getConfigurationType()
	{
		return getLaunchManager().getLaunchConfigurationType(WEBPersConstants.NC_LAUNCH_ID);
	}

	protected ILaunchManager getLaunchManager()
	{
		return DebugPlugin.getDefault().getLaunchManager();
	}

	protected ILaunchConfiguration findLaunchConfiguration(IJavaProject type, String mode)
	{
		ILaunchConfigurationType configType = getConfigurationType();
		List candidateConfigs = Collections.EMPTY_LIST;
		try
		{
			ILaunchConfiguration[] configs = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations(configType);
			candidateConfigs = new ArrayList(configs.length);
			for (int i = 0; i < configs.length; i++)
			{
				ILaunchConfiguration config = configs[i];
				if (config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "").equals(getMainClass())) { //$NON-NLS-1$
					if (config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "").equals(type.getElementName())) { //$NON-NLS-1$
						candidateConfigs.add(config);
					}
				}
			}
		}
		catch (CoreException e)
		{
			MainPlugin.getDefault().logError(e);
		}
		int candidateCount = candidateConfigs.size();
		if (candidateCount < 1)
		{
			return createConfiguration(type);
		}
		else if (candidateCount == 1)
		{
			return (ILaunchConfiguration) candidateConfigs.get(0);
		}
		else
		{
			ILaunchConfiguration config = chooseConfiguration(candidateConfigs, mode);
			if (config != null)
			{
				return config;
			}
		}
		return null;
	}

	private ILaunchConfiguration chooseConfiguration(List configList, String mode)
	{
		IDebugModelPresentation labelProvider = DebugUITools.newDebugModelPresentation();
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), labelProvider);
		dialog.setElements(configList.toArray());
		dialog.setTitle(M_perspective.LFWLaunchShortcut_0);
		if (mode.equals(ILaunchManager.DEBUG_MODE))
		{
			dialog.setMessage(M_perspective.LFWLaunchShortcut_1);
		}
		else
		{
			dialog.setMessage(M_perspective.LFWLaunchShortcut_2);
		}
		dialog.setMultipleSelection(false);
		int result = dialog.open();
		labelProvider.dispose();
		if (result == Window.OK)
		{
			return (ILaunchConfiguration) dialog.getFirstResult();
		}
		return null;
	}

	protected abstract void configLaunchConfiguration(ILaunchConfigurationWorkingCopy wc);

	protected abstract String getMainClass();

	private ILaunchConfiguration createConfiguration(IJavaProject javaProject)
	{
		ILaunchConfiguration config = null;
		try
		{
			ILaunchConfigurationType configType = getConfigurationType();
			ILaunchConfigurationWorkingCopy wc = configType.newInstance(null, getLaunchManager().generateUniqueLaunchConfigurationNameFrom(
					javaProject.getElementName())
					+ getName());
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getJavaProject().getElementName());
			configLaunchConfiguration(wc);
			//todo:
			config = wc.doSave();
		}
		catch (CoreException ce)
		{
			MainPlugin.getDefault().logError(ce);
		}
		return config;
	}

	protected abstract String getName();
}
