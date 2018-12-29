package nc.uap.lfw.perspective.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.lang.M_perspective;
//import nc.uap.lfw.launcher.LFWMiddlewareLaunchShortcut;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate2;

public class LFWLaunchAction extends Action implements IWorkbenchWindowPulldownDelegate2{

	public LFWLaunchAction(){
		super(M_perspective.LFWLaunchAction_0);		
	}
	public void run(){
		IProject project = LFWPersTool.getCurrentProject();
		
		LFWMiddlewareLaunchShortcut laucher = new LFWMiddlewareLaunchShortcut(); 
		try {
			laucher.launch(project, "debug"); //$NON-NLS-1$
//			System.setProperty("lfw_sys_type", "MIDDLEWARE");
		} catch (CoreException e) {
			MainPlugin.getDefault().logError(e.getMessage());
		}

	}
	@Override
	public Menu getMenu(Control parent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
	}
	@Override
	public void run(IAction action) {
		IProject project = LFWPersTool.getCurrentProject();
		if(project==null){
			MessageDialog.openWarning(null, "提示", "请选中工程节点后再点击启动！");
		}
		LFWMiddlewareLaunchShortcut laucher = new LFWMiddlewareLaunchShortcut(); 
		try {
			laucher.launch(project, "debug"); //$NON-NLS-1$
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			MainPlugin.getDefault().logError(e.getMessage());
		}
	}
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Menu getMenu(Menu parent) {
		// TODO Auto-generated method stub
		return null;
	}
}
