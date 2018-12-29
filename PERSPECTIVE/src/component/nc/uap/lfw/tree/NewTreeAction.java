package nc.uap.lfw.tree;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_tree;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
import nc.uap.lfw.tree.core.TreeEditor;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 新建树命令
 * @author zhangxya
 *
 */
public class NewTreeAction extends Action {

	private class AddTreeCommand extends Command{
		public AddTreeCommand(){
			super(M_tree.NewTreeAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewTreeAction() {
		super(WEBPersConstants.NEW_TREE, PaletteImage.getCreateTreeImgDescriptor());

	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_TREE,M_tree.NewTreeAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem tree = (LFWWebComponentTreeItem)view.addTreeNode(dirName);
					//打开ds编辑器
					view.openTreeEditor(tree);
				} catch (Exception e) {
					String title =WEBPersConstants.NEW_TREE;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_tree.NewTreeAction_3, M_tree.NewTreeAction_4);
				return;
			}
			AddTreeCommand cmd = new AddTreeCommand();
			if(TreeEditor.getActiveEditor() != null)
				TreeEditor.getActiveEditor().executComand(cmd);
		}
		else return;
		
	}

}
