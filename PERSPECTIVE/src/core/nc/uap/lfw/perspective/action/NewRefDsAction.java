package nc.uap.lfw.perspective.action;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWDSTreeItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 引用数据集的创建
 * @author zhangxya
 *
 */
public class NewRefDsAction extends Action {
	private class AddDSCommand extends Command{
		public AddDSCommand(){
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

	public NewRefDsAction() {
		super(WEBPersConstants.NEW_REF_DATASET, PaletteImage.getCreateDsImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_REF_DATASET,"输入引用数据集名称","", null);
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWDSTreeItem ds = (LFWDSTreeItem)view.addRefDSTreeNode(dirName);
					//打开ds编辑器
					view.openDsEdit(ds);
				} catch (Exception e) {
					String title =WEBPersConstants.NEW_REF_DATASET;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, "错误提示", "请输入引用数据集的名称!");
				return;
			}
			AddDSCommand cmd = new AddDSCommand();
			if(DataSetEditor.getActiveEditor() != null)
				DataSetEditor.getActiveEditor().executComand(cmd);
		}
		else return;
	}

}
