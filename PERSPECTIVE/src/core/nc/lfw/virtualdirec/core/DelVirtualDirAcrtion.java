package nc.lfw.virtualdirec.core;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DelVirtualDirAcrtion extends Action {
	public DelVirtualDirAcrtion() {
		super("É¾³ý", PaletteImage.getDeleteImgDescriptor());
		setText(WEBPersConstants.DEL_VIRTUALDIR);
		setToolTipText(WEBPersConstants.DEL_VIRTUALDIR);
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectedTreeNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =WEBPersConstants.DEL_VIRTUALDIR;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	

}
