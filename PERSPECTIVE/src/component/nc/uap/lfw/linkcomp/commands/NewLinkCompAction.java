package nc.uap.lfw.linkcomp.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_iframe;
import nc.uap.lfw.linkcomp.LinkCompEditor;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NewLinkCompAction extends Action {

	private class AddGridCommand extends Command{
		public AddGridCommand(){
			super(M_iframe.NewLinkCompAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewLinkCompAction() {
		super(WEBPersConstants.NEW_LINKCOMP, PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_LINKCOMP,M_iframe.NewLinkCompAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem linkcomp = (LFWWebComponentTreeItem)view.addLinkTreeNode(dirName);
					//´ò¿ªds±à¼­Æ÷
					view.openLinkEditor(linkcomp);
				} catch (Exception e) {
					String title =WEBPersConstants.NEW_LINKCOMP;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_iframe.NewLinkCompAction_3, M_iframe.NewLinkCompAction_4);
				return;
			}
			AddGridCommand cmd = new AddGridCommand();
			if(LinkCompEditor.getActiveEditor() != null)
				LinkCompEditor.getActiveEditor().executComand(cmd);
		}
		else return;
		
	}

}
