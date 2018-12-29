package nc.uap.lfw.perspective.action;

import nc.lfw.editor.common.tools.CheckTools;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

public class RefreshLFWExplorerTreeAction  extends Action {

	public RefreshLFWExplorerTreeAction() {
		super(WEBPersConstants.REFRESH,PaletteImage.getRefreshImgDescriptor());
		setToolTipText(WEBPersConstants.REFRESH);
	}

	@Override
	public void run() {
		if(CheckTools.beforeRefresh())
			LFWExplorerTreeView.getLFWExploerTreeView(null).refreshTree();
	}
}
