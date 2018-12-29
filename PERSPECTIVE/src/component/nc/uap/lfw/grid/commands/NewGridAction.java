package nc.uap.lfw.grid.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.grid.core.GridEditor;
import nc.uap.lfw.lang.M_grid;
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
 * 新建grid action
 * @author zhangxya
 *
 */
public class NewGridAction extends Action {

	private class AddGridCommand extends Command{
		public AddGridCommand(){
			super(M_grid.NewGridAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewGridAction() {
		super(WEBPersConstants.NEW_GRID, PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_GRID,M_grid.NewGridAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem grid = (LFWWebComponentTreeItem)view.addGridTreeNode(dirName);
					//打开ds编辑器
					view.openGridEditor(grid);
				} catch (Exception e) {
					String title = WEBPersConstants.NEW_GRID;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_grid.NewGridAction_3, M_grid.NewGridAction_4);
				return;
			}
			AddGridCommand cmd = new AddGridCommand();
			if(GridEditor.getActiveEditor() != null)
				GridEditor.getActiveEditor().executComand(cmd);
		}
		else return;
		
	}

}
