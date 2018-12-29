package nc.lfw.editor.pagemeta;

import nc.lfw.editor.common.PagemetaEditorInput;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.window.WindowEditorInput;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

public class EditNodeAction extends NodeAction {
	
	public EditNodeAction() {
		setText(WEBPersConstants.EDIT_WINDOW);
		setToolTipText(WEBPersConstants.EDIT_WINDOW);
	}
	
	public void run() {
		openEditor();
	}
	
	private void openEditor() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		LfwWindow currentPagemeta = LFWPersTool.getCurrentPageMeta();
		if(view == null || currentPagemeta == null){
			throw new LfwRuntimeException("组件浏览器视图对象为空或当前PageMeta对象为空!");
		}
		PagemetaEditorInput editorinput = new WindowEditorInput(currentPagemeta);
	
		IWorkbenchPage workbenchPage = view.getViewSite().getPage();
		IEditorPart editor = null;
		IEditorReference[] editorRefs = workbenchPage.getEditorReferences();
		for (int i = 0; i < editorRefs.length; i++) {
			editor = editorRefs[i].getEditor(true);
			if (editor instanceof WindowEditor) {
				WindowEditor pmEditor = (WindowEditor)editor;
				LfwWindow pmOpened = ((PagemetaEditorInput)pmEditor.getEditorInput()).getPagemeta();
				if (currentPagemeta.getId().equals(pmOpened.getId())) {
					TreeItem ti = ((PagemetaEditorInput)pmEditor.getEditorInput()).getCurrentTreeItem();
					if(ti == null || ti.isDisposed()){
						workbenchPage.closeEditor(editor, false);
						editor = null;
					}
					break;
				}
			}
			editor = null;
		}
		LFWPersTool.getLFWViewSheet();
		if (editor != null)
			workbenchPage.bringToTop(editor);
		else {
			try {
				String editorid = "nc.lfw.editor.pagemeta.WindowEditor";
				LFWAMCPersTool.getCurrentPageMetaTreeItem().setEditorInput(editorinput);
				workbenchPage.openEditor(editorinput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(),e1);
			}
		}
	}
	
}
