package nc.uap.lfw.form.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.form.core.FormEditor;
import nc.uap.lfw.lang.M_form;
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
 * 新建表单命令
 * @author zhangxya
 *
 */
public class NewFormAction extends Action {

	private class AddFormCommand extends Command{
		public AddFormCommand(){
			super(M_form.NewFormAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewFormAction() {
		super(WEBPersConstants.NEW_FORM, PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_FORM,M_form.NewFormAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem form = (LFWWebComponentTreeItem)view.addFormTreeNode(dirName);
					//打开ds编辑器
					view.openFormEditor(form);
				} catch (Exception e) {
					String title =WEBPersConstants.NEW_FORM;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_form.NewFormAction_3, M_form.NewFormAction_4);
				return;
			}
			AddFormCommand cmd = new AddFormCommand();
			if(FormEditor.getActiveEditor() != null)
				FormEditor.getActiveEditor().executComand(cmd);
		}
		else return;
	}
	
}