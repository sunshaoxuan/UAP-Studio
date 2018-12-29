/**
 * 
 */
package nc.uap.lfw.perspective.action;

import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author chouhl
 * 2011-11-18
 */
public class DeleteWebComponentAction extends NodeAction {

	public DeleteWebComponentAction(String text){
		super(text, text);
	}
	
	@Override
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null){
				view.deleteSelectedWebComponentNode();
			}
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title = getText();
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	
}
