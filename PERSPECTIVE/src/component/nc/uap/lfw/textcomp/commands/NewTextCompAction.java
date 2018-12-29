package nc.uap.lfw.textcomp.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_textcomp;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
import nc.uap.lfw.textcomp.TextCompEditor;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 新增textcomp命令
 * @author zhangxya
 *
 */
public class NewTextCompAction  extends Action {

	private class AddTextCompCommand extends Command{
		public AddTextCompCommand(){
			super(M_textcomp.NewTextCompAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewTextCompAction() {
		super(WEBPersConstants.NEW_TEXTCOMP, PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_TEXTCOMP,M_textcomp.NewTextCompAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem textcomp = (LFWWebComponentTreeItem)view.addTextCompTreeNode(dirName);
					//打开ds编辑器
					view.openTextEditor(textcomp);
				} catch (Exception e) {
					String title =WEBPersConstants.NEW_TEXTCOMP;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_textcomp.NewTextCompAction_3, M_textcomp.NewTextCompAction_4);
				return;
			}
			AddTextCompCommand cmd = new AddTextCompCommand();
			if(TextCompEditor.getActiveEditor() != null)
				TextCompEditor.getActiveEditor().executComand(cmd);
		}
		else return;
	}
	
}