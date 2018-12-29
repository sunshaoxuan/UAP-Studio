package nc.uap.lfw.iframe.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.iframe.IFrameEditor;
import nc.uap.lfw.lang.M_iframe;
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
 * 新建Iframe 命令
 * @author zhangxya
 *
 */
public class NewIFrameAction  extends Action {

	private class AddIFrameCommand extends Command{
		public AddIFrameCommand(){
			super(M_iframe.NewIFrameAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewIFrameAction() {
		super(WEBPersConstants.NEW_IFRAME, PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_IFRAME,M_iframe.NewIFrameAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem iframe = (LFWWebComponentTreeItem)view.addIFrameTreeNode(dirName);
					//打开ds编辑器
					view.openIFrameEditor(iframe);
				} catch (Exception e) {
					String title =WEBPersConstants.NEW_IFRAME;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_iframe.NewIFrameAction_3, M_iframe.NewIFrameAction_4);
				return;
			}
			AddIFrameCommand cmd = new AddIFrameCommand();
			if(IFrameEditor.getActiveEditor() != null)
				IFrameEditor.getActiveEditor().executComand(cmd);
		}
		else return;
	}

}
