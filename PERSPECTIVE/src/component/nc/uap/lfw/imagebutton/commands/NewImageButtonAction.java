package nc.uap.lfw.imagebutton.commands;

import nc.uap.lfw.image.ImageEditor;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NewImageButtonAction extends Action {

	private class AddGridCommand extends Command{
		public AddGridCommand(){
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
	public NewImageButtonAction() {
		super("新建图片按钮", PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, "新建图片按钮","新建图片按钮ID","", null);
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem image = (LFWWebComponentTreeItem)view.addImageButtonTreeNode(dirName);
					//打开image编辑器
					view.openImageButtonEditor(image);
				} catch (Exception e) {
					String title ="新建图片按钮";
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, "错误提示", "请输入图片按钮的名称!");
				return;
			}
			AddGridCommand cmd = new AddGridCommand();
			if(ImageEditor.getActiveEditor() != null)
				ImageEditor.getActiveEditor().executComand(cmd);
		}
		else return;
		
	}

}
