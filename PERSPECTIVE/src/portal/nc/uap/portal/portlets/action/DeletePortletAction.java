package nc.uap.portal.portlets.action;

import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * É¾³ýPortlet
 * 
 * @author dingrf
 *
 */
public class DeletePortletAction extends Action {
	public DeletePortletAction() {
		super(PortalProjConstants.DEL_PORTLET, PaletteImage.getDeleteImgDescriptor());
	}

	
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectPortletTreeNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =PortalProjConstants.DEL_PORTLET;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
}	
