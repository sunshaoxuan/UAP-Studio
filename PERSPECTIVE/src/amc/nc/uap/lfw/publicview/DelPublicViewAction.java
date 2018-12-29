package nc.uap.lfw.publicview;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TreeItem;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.listener.FileUtil;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

public class DelPublicViewAction extends NodeAction{
	
	private LfwView widget = null;
	
	public DelPublicViewAction(){
		setText(WEBPersConstants.DEL_PUBLIC_VIEW);
	}
	public void run(){
		String msg = "PublicView";  //$NON-NLS-1$
		MessageDialog.openWarning(null, M_editor.DelPublicViewAction_1, M_editor.DelPublicViewAction_0); 
		if (MessageDialog.openConfirm(null, M_editor.DelPublicViewAction_2, M_editor.DelPublicViewAction_3 + msg + M_editor.DelPublicViewAction_4)) {   
			widget = LFWPersTool.getCurrentWidget();
			deleteView();
		}
	}
	
	private void deleteView(){
		String folderPath = LFWPersTool.getCurrentFolderPath();
		//String widgetPoolPath = folderPath + "\\web\\pagemeta\\public\\widgetpool\\" + widget.getId();
		FileUtil.deleteFile(folderPath);
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		view.closeOpenedEidtor(LFWPersTool.getCurrentTreeItem());
		try {
			LFWPersTool.getCurrentProject().refreshLocal(2, null);
		} catch (CoreException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		
		//É¾³ý´ËÊ÷½Úµã
		TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
		treeItem.dispose();
	}

}
