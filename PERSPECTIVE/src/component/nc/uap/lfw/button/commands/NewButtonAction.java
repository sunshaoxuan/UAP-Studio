package nc.uap.lfw.button.commands;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.button.ButtonEditor;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_button;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 新建按钮命令
 * @author zhangxya
 *
 */
public class NewButtonAction extends Action {

	private class AddGridCommand extends Command{
		public AddGridCommand(){
			super(M_button.NewButtonAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewButtonAction() {
		super(WEBPersConstants.NEW_BUTTON, PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_BUTTON,M_button.NewButtonAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem button = (LFWWebComponentTreeItem)view.addButtonTreeNode(dirName);
					//打开ds编辑器
					view.openButtonEditor(button);
				} catch (Exception e) {
					MainPlugin.getDefault().logError(e.getMessage(), e);
					String title =WEBPersConstants.NEW_BUTTON;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_button.NewButtonAction_3, M_button.NewButtonAction_4);
				return;
			}
			AddGridCommand cmd = new AddGridCommand();
			if(ButtonEditor.getActiveEditor() != null)
				ButtonEditor.getActiveEditor().executComand(cmd);
		}
		else return;
		
	}

}
