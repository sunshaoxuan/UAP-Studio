package nc.uap.lfw.image.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * É¾³ýÍ¼Æ¬
 * @author zhangxya
 *
 */
public class DeleteImageAction extends Action {
	public DeleteImageAction() {
		super("É¾³ý", PaletteImage.getDeleteImgDescriptor());
		setText(WEBPersConstants.DEL_IMAGE);
		setToolTipText(WEBPersConstants.DEL_IMAGE);
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectedWebComponentNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =WEBPersConstants.DEL_IMAGE;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	

}
