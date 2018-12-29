package nc.uap.lfw.selfdefcomp.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_iframe;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
import nc.uap.lfw.selfdefcomp.core.SelfDefCompEditor;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NewSelfdefCompAction  extends Action {

	private class AddGridCommand extends Command{
		public AddGridCommand(){
			super(M_iframe.NewSelfdefCompAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewSelfdefCompAction() {
		super(WEBPersConstants.NEW_SELF_DEF_COMP, PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_SELF_DEF_COMP,M_iframe.NewSelfdefCompAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem selfdefComponent = (LFWWebComponentTreeItem)view.addSelfdefTreeNode(dirName);
					//´ò¿ªds±à¼­Æ÷
					view.openSelfdefEditor(selfdefComponent);
				} catch (Exception e) {
					String title =WEBPersConstants.NEW_SELF_DEF_COMP;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_iframe.NewSelfdefCompAction_2, M_iframe.NewSelfdefCompAction_3);
				return;
			}
			AddGridCommand cmd = new AddGridCommand();
			if(SelfDefCompEditor.getActiveEditor() != null)
				SelfDefCompEditor.getActiveEditor().executComand(cmd);
		}
		else return;
		
	}

}
