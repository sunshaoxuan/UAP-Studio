package nc.uap.portal.perspective.action;

import nc.lfw.editor.common.tools.CheckTools;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Ë¢ÐÂPortalExplorer
 * 
 * @author dingrf
 *
 */
public class RefreshPortalExplorerTreeAction  extends Action {

	public RefreshPortalExplorerTreeAction() {
		super(PortalProjConstants.REFRESH,PaletteImage.getRefreshImgDescriptor());
		setToolTipText(PortalProjConstants.REFRESH);
	}

	@Override
	public void run() {
		if(CheckTools.beforeRefresh())
			PortalExplorerTreeView.getPortalExploerTreeView(null).refreshTree();				
	}
}
