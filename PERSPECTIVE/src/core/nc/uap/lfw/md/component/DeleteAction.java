package nc.uap.lfw.md.component;

import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.lang.M_lfw_component;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import ncmdp.factory.ImageFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DeleteAction extends NodeAction{

	public DeleteAction(){
		super(M_lfw_component.DeleteAction_0, ImageFactory.getDeleteImageDescriptor());
	    setToolTipText(M_lfw_component.DeleteAction_0);
	}
	 public void run()
	 {
		 LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		    try {
		      if (view == null) return;
		      view.deleteSelectedTreeNode();
		    } catch (Exception e) {
		      Shell shell = new Shell(Display.getCurrent());
		      String title = M_lfw_component.DeleteAction_0;
		      String message = e.getMessage();
		      MessageDialog.openError(shell, title, message);
		    }
	 }
}
