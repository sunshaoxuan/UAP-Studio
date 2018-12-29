/**
 * 
 */
package nc.uap.lfw.window;

import nc.lfw.editor.common.PagemetaEditorInput;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.exception.LfwPluginException;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.editor.window.WindowBrowserEditor;
import nc.uap.lfw.editor.window.WindowEditorInput;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

/**
 * @author chouhl
 *
 */
public class WindowUIGuideAction extends NodeAction {

	public WindowUIGuideAction(){
		super(WEBPersConstants.UI_GUIDE);
	}
	
	@Override
	public void run() {
		openEditor();
	}
	
	public static void openEditor() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		LfwWindow currentPagemeta = LFWPersTool.getCurrentPageMeta();
		if(currentPagemeta == null || view == null){
			throw new LfwPluginException("当前PageMetaConfig对象为空或lfw浏览器视图对象为空!");
		}
		PagemetaEditorInput editorinput = new WindowEditorInput(currentPagemeta);
		IWorkbenchPage workbenchPage = view.getViewSite().getPage();
		IEditorReference[] ers = workbenchPage.getEditorReferences();
		IEditorPart editorPart = null;
		for (int i = 0; i < ers.length; i++) {
			editorPart = ers[i].getEditor(true);
			if(editorPart instanceof WindowBrowserEditor){
				WindowBrowserEditor wbEditor = (WindowBrowserEditor)editorPart;
				LfwWindow pmOpened = ((WindowEditorInput)wbEditor.getEditorInput()).getPagemeta();
				if (currentPagemeta.getId() != null && (currentPagemeta.getId().equals(pmOpened.getId()))) {
					break;
				}
			}
			editorPart = null;
		}
		if (editorPart != null)
			workbenchPage.bringToTop(editorPart);
		else {
			try {
				String editorid = "nc.uap.lfw.editor.window.WindowBrowserEditor";
				workbenchPage.openEditor(editorinput, editorid);
			} catch (PartInitException e) {
				MainPlugin.getDefault().logError(e.getMessage(), e);
			}
		}
	}
	
}
