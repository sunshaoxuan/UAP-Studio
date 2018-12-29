package nc.uap.lfw.editor.extNode;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWfmCateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWfmFlwTypeTreeItem;

public class DelFlwCateAction extends NodeAction{
	public DelFlwCateAction(){
		super(WEBPersConstants.DEL_WFMCATE);
	}
	public void run() {
		delFlwCate();
	}
	private void delFlwCate(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null){
			return;
		}
		try {
			Tree tree = LFWAMCPersTool.getTree();
			TreeItem[] selTIs = tree.getSelection();
			if(selTIs == null || selTIs.length == 0){
				MessageDialog.openInformation(null, M_editor.DelFlwCateAction_0, M_editor.DelFlwCateAction_1);
				return;
			}
			LFWWfmCateTreeItem selTI = (LFWWfmCateTreeItem)LFWPersTool.getCurrentTreeItem();
			if(selTI != null) {
				if(!MessageDialog.openConfirm(null, M_editor.DelFlwCateAction_2, M_editor.DelFlwCateAction_3 + selTI.getText() + M_editor.DelFlwCateAction_4)){
					return;
				}
				if(selTI.getItemCount()==0){
					LFWWfmConnector.delFlwCate(selTI.getPk());
					selTI.deleteNode();
				}
				else MessageDialog.openWarning(null, WEBPersConstants.DEL_WFMCATE, M_editor.DelFlwCateAction_5);
				
				IWorkbenchPage page = view.getViewSite().getPage();
				IEditorInput input = selTI.getEditorInput();
				if(input != null) {
					IEditorPart editor = page.findEditor(input);
					if (editor != null) {
						page.closeEditor(editor, false);
					}
				}
			}
		} catch (Exception e) {
			MainPlugin.getDefault().logError(M_editor.DelFlwCateAction_6, e);
			MessageDialog.openError(null, WEBPersConstants.DEL_WFMCATE, M_editor.DelFlwCateAction_7);
		}
	}
}
