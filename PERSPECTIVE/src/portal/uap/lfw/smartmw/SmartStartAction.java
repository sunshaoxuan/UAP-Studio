package uap.lfw.smartmw;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class SmartStartAction extends Action implements IWorkbenchWindowActionDelegate{

	@Override
	public void run(IAction action) {
		StartEmbedTomcat.start();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void dispose() {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO �Զ����ɵķ������
		
	}

}
