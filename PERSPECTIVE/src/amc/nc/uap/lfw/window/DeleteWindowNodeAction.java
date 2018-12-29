/**
 * 
 */
package nc.uap.lfw.window;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.application.LFWApplicationTreeItem;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.DeleteAMCNodeAction;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.application.ApplicationEditor;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.editor.window.WindowConfigObj;
import nc.uap.lfw.lang.M_window;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * 
 * 删除Window节点类
 * @author chouhl
 *
 */
public class DeleteWindowNodeAction extends DeleteAMCNodeAction {

	public DeleteWindowNodeAction(){
		super(WEBPersConstants.DEL_WINDOW, WEBPersConstants.DEL_WINDOW);
	}
	
	public void run() {
		deleteWindow();
	}
	
	private void deleteWindow(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null){
			return;
		}
		try {
			Tree tree = LFWAMCPersTool.getTree();
			TreeItem[] selTIs = tree.getSelection();
			if(selTIs == null || selTIs.length == 0){
				MessageDialog.openInformation(null, M_window.DeleteWindowNodeAction_0, M_window.DeleteWindowNodeAction_1);
				return;
			}
			TreeItem selTI = selTIs[0];
			if(selTI instanceof ILFWTreeNode) {
				if(!MessageDialog.openConfirm(null, M_window.DeleteWindowNodeAction_2, M_window.DeleteWindowNodeAction_3 + selTI.getText() + M_window.DeleteWindowNodeAction_4)){
					return;
				}
				deleteBusiness();
				((ILFWTreeNode) selTI).deleteNode();
				IWorkbenchPage page = view.getViewSite().getPage();
				IEditorInput input = ((LFWBasicTreeItem) selTI).getEditorInput();
				if(input != null) {
					IEditorPart editor = page.findEditor(input);
					if (editor != null) {
						page.closeEditor(editor, false);
					}
				}
			}
		} catch (Exception e) {
			MainPlugin.getDefault().logError(M_window.DeleteWindowNodeAction_5, e);
			MessageDialog.openError(null, WEBPersConstants.DEL_WINDOW, M_window.DeleteWindowNodeAction_5);
		}
	}
	
	/**
	 * 删除业务
	 */
	private void deleteBusiness(){
		String windowId = LFWAMCPersTool.getCurrentWindowId();
		List<String> appIds = new ArrayList<String>();
		IWorkbenchWindow wbWindow = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage[] wbPages = wbWindow.getPages();
		if(wbPages != null && wbPages.length > 0){
			for(IWorkbenchPage wbPage : wbPages){
				IEditorReference[] editorRefs = wbPage.getEditorReferences();
				if(editorRefs != null && editorRefs.length > 0){
					for(IEditorReference editorRef : editorRefs){
						if(editorRef != null && editorRef.getEditor(true) instanceof ApplicationEditor){
							ApplicationEditor appEditor = (ApplicationEditor)editorRef.getEditor(true);
							List<WindowConfigObj> objList = appEditor.getGraph().getWindowCells();
							if(objList != null && objList.size() > 0){
								boolean isChanged = false;
								List<WindowConfig> windowList = new ArrayList<WindowConfig>();
								for(int i = objList.size() - 1; i >= 0; i--){
									if(objList.get(i).getWindowConfig().getId().equals(windowId)){
										appEditor.getGraph().removeWindowCell(objList.get(i));
										isChanged = true;
										continue;
									}
									windowList.add(objList.get(i).getWindowConfig());
								}
								if(isChanged){
									Application application = appEditor.getGraph().getApplication();
									application.setWindowList(windowList);
									if(windowId.equals(application.getDefaultWindowId())){
										application.setDefaultWindowId(""); //$NON-NLS-1$
									}
									appIds.add(application.getId());
									LFWAMCPersTool.saveApplication(application);
								}
							}
						}
					}
				}
			}
		}
		List<TreeItem> list = LFWAMCPersTool.getAllChildren(LFWAMCPersTool.getCurrentProject(), null, ILFWTreeNode.APPLICATION_FOLDER);
		if(list != null && list.size() > 0){
			for(TreeItem ti : list){
				if(ti instanceof LFWApplicationTreeItem){
					if(!appIds.contains(((LFWApplicationTreeItem) ti).getApplication().getId())){
						Application appConf = LFWAMCConnector.getApplication(LFWAMCPersTool.getCurrentFolderPath(ti), WEBPersConstants.AMC_APPLICATION_FILENAME);
						List<WindowConfig> windows = appConf.getWindowList();
						if(windows != null && windows.size() > 0){
							boolean isNeedToSave = false;
							for(int i=windows.size()-1; i>=0; i--){
								if(windowId.equals(windows.get(i).getId())){
									isNeedToSave = true;
									windows.remove(i);
								}
							}
							if(windowId.equals(appConf.getDefaultWindowId())){
								appConf.setDefaultWindowId(""); //$NON-NLS-1$
							}
							if(isNeedToSave){
								LFWSaveElementTool.saveApplication(appConf, LFWAMCPersTool.getCurrentFolderPath(ti));
							}
						}
					}					
				}
			}
		}
	}
	
}
