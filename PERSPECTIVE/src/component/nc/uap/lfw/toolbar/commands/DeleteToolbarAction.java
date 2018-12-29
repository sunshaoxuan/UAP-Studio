package nc.uap.lfw.toolbar.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_toolbar;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * É¾³ý¹¤¾ßÌõ
 * @author zhangxya
 *
 */
public class DeleteToolbarAction  extends Action {
	public DeleteToolbarAction() {
		super(M_toolbar.DeleteToolbarAction_0, PaletteImage.getDeleteImgDescriptor());
		setText(WEBPersConstants.DEL_TOOLBAR);
		setToolTipText(WEBPersConstants.DEL_TOOLBAR);
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectedWebComponentNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =WEBPersConstants.DEL_TOOLBAR;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	

}
