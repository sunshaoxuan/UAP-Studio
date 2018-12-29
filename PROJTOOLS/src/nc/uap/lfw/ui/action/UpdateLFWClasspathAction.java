package nc.uap.lfw.ui.action;

import nc.uap.lfw.internal.util.ProjCoreUtility;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class UpdateLFWClasspathAction implements IObjectActionDelegate
{
	private ISelection	fSelection;

	public void run(IAction action)
	{
		if (fSelection instanceof IStructuredSelection)
		{
			ProjCoreUtility.updateWorkspaceClasspath();
		}
	}

	public void selectionChanged(IAction action, ISelection selection)
	{
		this.fSelection = selection;
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
	}
}