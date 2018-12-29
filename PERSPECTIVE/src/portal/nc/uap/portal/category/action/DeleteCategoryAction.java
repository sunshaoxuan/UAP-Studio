package nc.uap.portal.category.action;

import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
/**
 * É¾³ýportlet·ÖÀà
 * 
 * @author dingrf
 *
 */
public class DeleteCategoryAction extends Action {
	
	public DeleteCategoryAction() {
		super(PortalProjConstants.DEL_CATEGORY, PaletteImage.getDeleteImgDescriptor());
	}

	@Override
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectCategoryTreeNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =PortalProjConstants.DEL_CATEGORY;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
}	
