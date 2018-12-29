package nc.uap.lfw.perspective.action;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * É¾³ýdsÃüÁî
 * @author zhangxya
 *
 */
public class DeleteDsAction  extends NodeAction {
	
	public DeleteDsAction() { 
		setText(WEBPersConstants.DEL_DATASET);
		setToolTipText(WEBPersConstants.DEL_DATASET);
	}

	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectedDsNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =WEBPersConstants.DEL_DATASET;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	
}
