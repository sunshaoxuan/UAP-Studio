package nc.uap.portal.skin.action;


import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 *  É¾³ýÑùÊ½
 *  
 * @author dingrf
 *
 */
public class DelSkinAction extends Action {

	public DelSkinAction() {
		super(PortalProjConstants.DEL_SKIN, PaletteImage.getDeleteImgDescriptor());
	}
	
	@Override
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectSkinTreeNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =PortalProjConstants.DEL_SKIN;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	
}
