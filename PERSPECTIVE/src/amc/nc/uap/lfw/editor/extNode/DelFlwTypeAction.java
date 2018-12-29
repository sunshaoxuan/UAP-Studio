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
import nc.uap.lfw.aciton.NCConnector;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWfmFlwTypeTreeItem;

/**
 * 
 * @author qinjianc
 *
 */
public class DelFlwTypeAction extends NodeAction{
	public DelFlwTypeAction() {
		super(WEBPersConstants.DEL_WFMTYPE);
	}

	public void run() {
		delFlwType();
	}
	private void delFlwType(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null){
			return;
		}
		try {
			Tree tree = LFWAMCPersTool.getTree();
			TreeItem[] selTIs = tree.getSelection();
			if(selTIs == null || selTIs.length == 0){
				MessageDialog.openInformation(null, M_editor.DelFlwTypeAction_0, M_editor.DelFlwTypeAction_1);
				return;
			}
			LFWWfmFlwTypeTreeItem selTI = (LFWWfmFlwTypeTreeItem)LFWPersTool.getCurrentTreeItem();
			if(selTI != null) {
				if(!MessageDialog.openConfirm(null, M_editor.DelFlwTypeAction_2, M_editor.DelFlwTypeAction_3 + selTI.getText() + M_editor.DelFlwTypeAction_4)){
					return;
				}
				if(selTI.getDef_pk()!=null) LFWWfmConnector.delProdef(selTI.getDef_pk());
				LFWWfmConnector.delFlwType(selTI.getType_pk());				
				
				((LFWWfmFlwTypeTreeItem) selTI).deleteNode();
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
			MainPlugin.getDefault().logError(M_editor.DelFlwTypeAction_5, e);
			MessageDialog.openError(null, WEBPersConstants.DEL_WFMTYPE, M_editor.DelFlwTypeAction_5);
		}
	}


}
