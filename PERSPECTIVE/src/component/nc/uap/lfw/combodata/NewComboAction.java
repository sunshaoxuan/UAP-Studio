package nc.uap.lfw.combodata;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.combodata.core.ComboDataEditor;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_combodata;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWComboTreeItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 新建combo数据
 * @author zhangxya
 *
 */
public class NewComboAction extends Action {

	private class AddComboCommand extends Command{
		public AddComboCommand(){
			super(M_combodata.NewComboAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewComboAction() {
		super(WEBPersConstants.NEW_COMBO_DATASET, PaletteImage.getCreateTreeImgDescriptor());

	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_COMBO_DATASET,M_combodata.NewComboAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWComboTreeItem combo = (LFWComboTreeItem)view.addComboNode(dirName);
					//打开ds编辑器
					view.openComboEditor(combo);
				} catch (Exception e) {
					MainPlugin.getDefault().logError(e.getMessage(), e);
					String title =WEBPersConstants.NEW_COMBO_DATASET;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			AddComboCommand cmd = new AddComboCommand();
			if(ComboDataEditor.getActiveEditor() != null)
				ComboDataEditor.getActiveEditor().executComand(cmd);
		}
	else return;

	}
}
