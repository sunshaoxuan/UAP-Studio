package nc.uap.lfw.pagination.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_listview;
import nc.uap.lfw.pagination.PaginationEditor;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NewPaginationAction extends Action{
	
	private class AddPaginationCommand extends Command{
		public AddPaginationCommand(){
			super(M_listview.NewListViewAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewPaginationAction() {
		super(WEBPersConstants.NEW_PAGINATION, PaletteImage.getCreatePaginationImgDescriptor());
	}
	public void run(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_PAGINATION,M_listview.NewPaginationAction_0,"", null);  //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem pagination = (LFWWebComponentTreeItem)view.addPaginationTreeNode(dirName);
					//´ò¿ªds±à¼­Æ÷
					view.openPaginationEditor(pagination);
				} catch (Exception e) {
					String title = M_listview.NewPaginationAction_1;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_listview.NewPaginationAction_2, M_listview.NewPaginationAction_3);
				return;
			}
			AddPaginationCommand cmd = new AddPaginationCommand();
			if(PaginationEditor.getActiveEditor() != null)
				PaginationEditor.getActiveEditor().executComand(cmd);
		}
		else return;		
	}
}
