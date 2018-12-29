/**
 * 
 */
package nc.uap.lfw.publicview;

import nc.lfw.editor.common.WidgetEditorInput;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

/**
 * @author chouhl
 *
 */
public class EditorPublicViewAction extends NodeAction {
	
	private static final String WIDGET = "widget_";

	private LFWWidgetTreeItem treeItem;
	
	public void setTreeItem(LFWWidgetTreeItem treeItem) {
		this.treeItem = treeItem;
	}
	
	public EditorPublicViewAction() {
		setText(WEBPersConstants.EDIT_PUBLIC_VIEW);
		setToolTipText(WEBPersConstants.EDIT_PUBLIC_VIEW);
	}
	
	public void run() {
		openEditor();
	}
	
	private void openEditor() {
		WidgetEditorInput editorinput = new WidgetEditorInput(treeItem.getWidget(), null);
		String editorid = WidgetEditor.class.getName();
		if (editorinput == null || editorid == null){
			return;
		}
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		IWorkbenchPage workbenchPage = view.getViewSite().getPage();
		LfwView widget = treeItem.getWidget();
		if (widget == null){
			widget = LFWPersTool.getCurrentWidget();
		}
		String editorInputId = view.getEditoridMap().get(WIDGET + widget.getId());
		if (editorInputId != null) {
			IEditorPart editor = null;
			IEditorReference[] iers = workbenchPage.getEditorReferences();
			for (int i = 0; i < iers.length; i++) {
				if(iers[i].getEditor(true) != null){
					IEditorInput editorInput = iers[i].getEditor(true).getEditorInput();
					if (editorInput instanceof WidgetEditorInput) {
						WidgetEditorInput wdEditorInput = (WidgetEditorInput)editorInput;
						LfwView wdnew = wdEditorInput.getWidget();
						if (widget.getId().equals(wdnew.getId())) {
							editor =  iers[i].getEditor(true);
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(editorinput, editorid);
				} catch (PartInitException e) {
					MainPlugin.getDefault().logError(e);
				}
			}
		}else {
			view.getEditoridMap().put(WIDGET + widget.getId(), WIDGET + widget.getId());
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(editorinput, editorid);
			} catch (PartInitException e) {
				MainPlugin.getDefault().logError(e);
			}
		}
		LFWAMCPersTool.getCurrentWidgetTreeItem().setEditorInput(editorinput);
	}
	
}
