package nc.uap.lfw.refnode.core;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * É¾³ý²ÎÕÕ½Úµã
 * @author zhangxya
 *
 */
public class DeleteRefNodeAction extends NodeAction {
	
	public DeleteRefNodeAction() {
		setText(WEBPersConstants.DEL_REFNODE);;
		setToolTipText(WEBPersConstants.DEL_REFNODE);
	}

	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectedRefNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title = WEBPersConstants.DEL_REFNODE;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

}
