package nc.uap.lfw.label.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_label;
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
public class DeleteLabelAction extends Action {
	public DeleteLabelAction() {
		super(M_label.DeleteLabelAction_0, PaletteImage.getDeleteImgDescriptor());
		setText(WEBPersConstants.DEL_LABEL);
		setToolTipText(WEBPersConstants.DEL_LABEL);
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectedWebComponentNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =WEBPersConstants.DEL_LABEL;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	

}
