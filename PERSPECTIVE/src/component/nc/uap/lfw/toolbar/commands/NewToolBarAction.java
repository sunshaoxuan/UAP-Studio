package nc.uap.lfw.toolbar.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_toolbar;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
import nc.uap.lfw.toolbar.ToolBarEditor;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 新建toolbar
 * @author zhangxya
 *
 */
public class NewToolBarAction extends Action {

	private class AddFormCommand extends Command{
		public AddFormCommand(){
			super(M_toolbar.NewToolBarAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewToolBarAction() {
		super(WEBPersConstants.NEW_TOOLBAR, PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_TOOLBAR,M_toolbar.NewToolBarAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem toolbar = (LFWWebComponentTreeItem)view.addToolbarTreeNode(dirName);
					//打开ds编辑器
					view.openToolbarEditor(toolbar);
				} catch (Exception e) {
					String title =WEBPersConstants.NEW_TOOLBAR;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_toolbar.NewToolBarAction_3, M_toolbar.NewToolBarAction_4);
				return;
			}
			AddFormCommand cmd = new AddFormCommand();
			if(ToolBarEditor.getActiveEditor() != null)
				ToolBarEditor.getActiveEditor().executComand(cmd);
		}
		else return;
	}
	
}