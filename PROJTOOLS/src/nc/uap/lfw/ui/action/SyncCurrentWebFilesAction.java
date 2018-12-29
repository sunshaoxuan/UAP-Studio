/**
 * 
 */
package nc.uap.lfw.ui.action;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.lang.M_action;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author guomq1
 * 
 */
public class SyncCurrentWebFilesAction implements IObjectActionDelegate {
	private ISelection fSelection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		if (fSelection instanceof IStructuredSelection) {
			Job job = new Job(M_action.SyncCurrentWebFilesAction_0) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					Object element = ((IStructuredSelection) fSelection).getFirstElement();
					if (element instanceof IProject) {
						IProject proj = (IProject) ((IStructuredSelection) fSelection).getFirstElement();
						SyncProjFilesJob.deleteProjectFiles(proj);
						boolean part = SyncProjFilesJob.syncProject(proj);
						if(part){
							List<IProject> list = new ArrayList<IProject>();
							list.add(proj);
							SyncProjFilesJob.mergeWebXml(list);
						}
						monitor.done();
					} else {
						WEBProjPlugin.getDefault().logError(
								M_action.SyncCurrentWebFilesAction_2);
					}
					return Status.OK_STATUS;
				}

			};
			job.schedule();

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.fSelection = selection;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.
	 * action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

}
