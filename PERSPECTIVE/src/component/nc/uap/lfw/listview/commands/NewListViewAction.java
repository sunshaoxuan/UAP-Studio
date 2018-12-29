package nc.uap.lfw.listview.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_listview;
import nc.uap.lfw.listview.core.ListViewEditor;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NewListViewAction extends Action{
	private class AddListViewCommand extends Command{
		public AddListViewCommand(){
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
	public NewListViewAction() {
		super(WEBPersConstants.NEW_LISTVIEW, PaletteImage.getCreateListViewImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_LISTVIEW,M_listview.NewListViewAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem listview = (LFWWebComponentTreeItem)view.addListViewTreeNode(dirName);
					//´ò¿ªds±à¼­Æ÷
					view.openListViewEditor(listview);
				} catch (Exception e) {
					String title = WEBPersConstants.NEW_LISTVIEW;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_listview.NewListViewAction_3, M_listview.NewListViewAction_4);
				return;
			}
			AddListViewCommand cmd = new AddListViewCommand();
			if(ListViewEditor.getActiveEditor() != null)
				ListViewEditor.getActiveEditor().executComand(cmd);
		}
		else return;
		
	}
}
