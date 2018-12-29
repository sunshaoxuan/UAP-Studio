package nc.uap.lfw.refnode;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_refnode;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWRefNodeTreeItem;
import nc.uap.lfw.refnode.core.RefNodeEditor;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 新建参照节点
 * @author zhangxya
 *
 */
public class NewRefnodeAction extends Action {

	private class AddComboCommand extends Command{
		public AddComboCommand(){
			super(M_refnode.NewRefnodeAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewRefnodeAction() {
		super(WEBPersConstants.NEW_REFNODE, PaletteImage.getCreateTreeImgDescriptor());

	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_REFNODE,M_refnode.NewRefnodeAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWRefNodeTreeItem refnode = (LFWRefNodeTreeItem)view.addRefNode(dirName);
					//打开refnode编辑器
					view.openRefNodeEditor(refnode);
				} catch (Exception e) {
					MainPlugin.getDefault().logError(e.getMessage(), e);
					String title = WEBPersConstants.NEW_REFNODE;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_refnode.NewRefnodeAction_3, M_refnode.NewRefnodeAction_4);
				return;
			}
			AddComboCommand cmd = new AddComboCommand();
			if(RefNodeEditor.getActiveEditor() != null)
				RefNodeEditor.getActiveEditor().executComand(cmd);
		}
		else return;
		
	}
}
