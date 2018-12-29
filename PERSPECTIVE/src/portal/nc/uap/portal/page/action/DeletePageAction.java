package nc.uap.portal.page.action;

import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * É¾³ýPageÒ³
 * 
 * @author dingrf
 *
 */
public class DeletePageAction extends Action {
	public DeletePageAction() {
		super(PortalProjConstants.DEL_PAGE, PaletteImage.getDeleteImgDescriptor());
	}

	
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectPageTreeNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =PortalProjConstants.DEL_PAGE;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
}	
