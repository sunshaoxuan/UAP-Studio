package nc.uap.lfw.refnoderel;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_refnoderel;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 创建参照关联关系
 * @author zhangxya
 *
 */
public class NewRefNodeRelAction extends Action {

	private class AddGridCommand extends Command{
		public AddGridCommand(){
			super(M_refnoderel.NewRefNodeRelAction_0);
		}
		
		public void execute(){
			redo();
		}

		public void redo(){
		}
		
		public void undo(){
		}
	}
	
	public NewRefNodeRelAction() {
		super(WEBPersConstants.NEW_REFNODE_REF, PaletteImage.getCreateGridImgDescriptor());
	}

	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_REFNODE_REF,M_refnoderel.NewRefNodeRelAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWRefNodeRelTreeItem refnodeRel = (LFWRefNodeRelTreeItem)view.addRefNodeRelation(dirName);
					//打开ds编辑器
					view.openRefNodeRelEditor(refnodeRel);
				} catch (Exception e) {
					String title =WEBPersConstants.NEW_REFNODE_REF;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_refnoderel.NewRefNodeRelAction_3, M_refnoderel.NewRefNodeRelAction_4);
				return;
			}
			AddGridCommand cmd = new AddGridCommand();
			if(RefnoderelEditor.getActiveEditor() != null)
				RefnoderelEditor.getActiveEditor().executComand(cmd);
		}
		else return;
	}

}
