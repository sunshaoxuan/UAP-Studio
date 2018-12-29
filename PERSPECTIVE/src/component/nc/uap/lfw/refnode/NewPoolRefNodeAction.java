package nc.uap.lfw.refnode;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_refnode;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWRefNodeTreeItem;
import nc.uap.lfw.refnode.core.PoolRefNodeDialog;
import nc.uap.lfw.refnode.core.RefNodeEditor;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 增加公共池中的refnode节点
 * @author zhangxya
 *
 */
public class NewPoolRefNodeAction extends Action {

	private class AddRefNodeCommand extends Command{
		public AddRefNodeCommand(){
			super(M_refnode.NewPoolRefNodeAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
		}
		
		public void undo() {
		}
		
	}
	public NewPoolRefNodeAction() {
		super(WEBPersConstants.NEW_PUBLIC_REFNODE, PaletteImage.getCreateTreeImgDescriptor());

	}

	/**
	 * 运行函数
	 */
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Tree tree = view.getTreeView().getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0){
			MessageDialog.openError(null, M_refnode.NewPoolRefNodeAction_1, M_refnode.NewPoolRefNodeAction_2);
			return;
		}
		TreeItem selTI = selTIs[0];
		String fullName = ""; //$NON-NLS-1$
		if(selTI instanceof LFWDirtoryTreeItem){
			LFWDirtoryTreeItem dir = (LFWDirtoryTreeItem) selTI;
			if(!dir.getType().equals(LFWDirtoryTreeItem.PARENT_PUB_REF_FOLDER)){
				fullName = selTI.getText();
				while (selTI.getParentItem() != null && (selTI instanceof LFWDirtoryTreeItem)) {
					LFWDirtoryTreeItem parent = (LFWDirtoryTreeItem)selTI.getParentItem();
					if ((parent.getText().equals(WEBPersConstants.NODEGROUP_CN) || parent.getText().equals(WEBPersConstants.PUBLIC_REFNODE)))
						break;
					fullName = parent.getFile().getName() + "."	+ fullName; //$NON-NLS-1$
					selTI = parent;
					
				}
			}
		}
		
		Shell shell = new Shell(Display.getCurrent());
		PoolRefNodeDialog input = new PoolRefNodeDialog(shell, WEBPersConstants.NEW_PUBLIC_REFNODE, fullName);
		int result = input.open();
		if (result == IDialogConstants.OK_ID){
			String dirName = input.getRefnodeId();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWRefNodeTreeItem refnode = (LFWRefNodeTreeItem)view.addPoolRefNode(dirName);
					view.openPoolRefNodeEdit(refnode);
				} catch (Exception e) {
					String title =WEBPersConstants.NEW_PUBLIC_REFNODE;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_refnode.NewPoolRefNodeAction_1, M_refnode.NewPoolRefNodeAction_6);
				return;
			}
			AddRefNodeCommand cmd = new AddRefNodeCommand();
			if(RefNodeEditor.getActiveEditor() != null)
				RefNodeEditor.getActiveEditor().executComand(cmd);
			}
			else return;
		
		
//		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
//		if(view == null)
//			return;
//		Shell shell = new Shell(Display.getCurrent());
//		InputDialog input = new InputDialog(shell, "新建公共参照","输入公共参照名称","", null);
//		if(input.open() == InputDialog.OK){
//			String dirName = input.getValue();
//			if(dirName != null && dirName.trim().length()>0){
//				dirName =dirName.trim();
//				try {
//					LFWRefNodeTreeItem refnode = (LFWRefNodeTreeItem)view.addPoolRefNode(dirName);
//					view.openPoolRefNodeEdit(refnode);
//				} catch (Exception e) {
//					String title ="新建参照";
//					String message = e.getMessage();
//					MessageDialog.openError(shell, title, message);
//				}
//			}
//			else {
//				MessageDialog.openError(shell, "错误提示", "请输入参照的ID!");
//				return;
//			}
//			AddRefNodeCommand cmd = new AddRefNodeCommand();
//			if(RefNodeEditor.getActiveEditor() != null)
//				RefNodeEditor.getActiveEditor().executComand(cmd);
//		}
//		else return;
		
	}
}
