package nc.uap.lfw.textcomp.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_textcomp;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DeleteTextCompAction extends Action {
	public DeleteTextCompAction() {
		super(M_textcomp.DeleteTextCompAction_0, PaletteImage.getDeleteImgDescriptor());
		setText(WEBPersConstants.DEL_TEXTCOMP);
		setToolTipText(WEBPersConstants.DEL_TEXTCOMP);
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectedWebComponentNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =WEBPersConstants.DEL_TEXTCOMP;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	

}
