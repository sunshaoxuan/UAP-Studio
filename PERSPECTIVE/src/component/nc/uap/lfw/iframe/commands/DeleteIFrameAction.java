package nc.uap.lfw.iframe.commands;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_iframe;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * …æ≥˝Iframe√¸¡Ó
 * @author zhangxya
 *
 */
public class DeleteIFrameAction  extends Action {
	public DeleteIFrameAction() {
		super(M_iframe.DeleteIFrameAction_0, PaletteImage.getDeleteImgDescriptor());
		setText(WEBPersConstants.DEL_IFRAME);
		setToolTipText(WEBPersConstants.DEL_IFRAME);
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectedWebComponentNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =WEBPersConstants.DEL_IFRAME;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	

}
