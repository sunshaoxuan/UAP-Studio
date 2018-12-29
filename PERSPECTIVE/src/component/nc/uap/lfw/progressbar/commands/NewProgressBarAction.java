package nc.uap.lfw.progressbar.commands;

import nc.uap.lfw.button.ButtonEditor;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
import nc.uap.lfw.progressbar.ProgressBarEditor;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NewProgressBarAction extends Action {

	private class AddProgressbarCommand extends Command{
		public AddProgressbarCommand(){
			super("增加属性");
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewProgressBarAction() {
		super(WEBPersConstants.NEW_PROGRESSBAR, PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_PROGRESSBAR,"输入进度条ID","", null);
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem progressBar = (LFWWebComponentTreeItem)view.addProgressBarTreeNode(dirName);
					view.openProgressBarEditor(progressBar);
				} catch (Exception e) {
					String title = WEBPersConstants.NEW_PROGRESSBAR;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, "错误提示", "请输入进度条的ID!");
				return;
			}
			AddProgressbarCommand cmd = new AddProgressbarCommand();
			if(ProgressBarEditor.getActiveEditor() != null)
				ProgressBarEditor.getActiveEditor().executComand(cmd);
		}
		else return;
		
	}

}
