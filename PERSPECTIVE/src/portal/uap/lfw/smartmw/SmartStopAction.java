package uap.lfw.smartmw;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class SmartStopAction extends Action implements IWorkbenchWindowActionDelegate{

	@Override
	public void run(IAction action) {
		StartEmbedTomcat.stop();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		
	}

}
