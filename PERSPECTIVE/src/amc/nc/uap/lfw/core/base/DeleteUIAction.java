/**
 * 
 */
package nc.uap.lfw.core.base;

import java.io.File;
import java.util.List;

import nc.lfw.editor.common.PagemetaEditorInput;
import nc.lfw.editor.common.WidgetEditorInput;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.editor.common.editor.LFWBrowserEditor;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.editor.publicview.PublicViewBrowserEditor;
import nc.uap.lfw.editor.publicview.PublicViewEditorInput;
import nc.uap.lfw.editor.view.ViewBrowserEditor;
import nc.uap.lfw.editor.window.WindowBrowserEditor;
import nc.uap.lfw.lang.M_window;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author chouhl
 * 2011-11-22
 */
public class DeleteUIAction extends NodeAction {
	
	private IViewSite site;
	
	public DeleteUIAction() {
		super(WEBPersConstants.DEL_UI_GUIDE, WEBPersConstants.DEL_UI_GUIDE);
	}
	
	public void run() {
		boolean flag = MessageDialog.openConfirm(null ,M_window.DeleteUIAction_0, M_window.DeleteUIAction_1);
		if(flag){
			String folderPath = LFWPersTool.getCurrentFolderPath();
			String filePath = folderPath + File.separator + WEBPersConstants.AMC_UIMETA_FILENAME;
			File file = new File(filePath);
			//É¾³ýÎÄ¼þ
			if(file.exists()){
				FileUtilities.deleteFile(file);
			}
			LFWSaveElementTool.createUIMeta(folderPath);
			List<LFWBrowserEditor> webEditors = LFWTool.getAllWebEditors();
			if(webEditors != null && webEditors.size() > 0){
				for(LFWBrowserEditor webEditor : webEditors){
					if(webEditor instanceof ViewBrowserEditor && LFWAMCPersTool.getCurrentWidget() != null){
						IEditorInput input = ((ViewBrowserEditor)webEditor).getEditorInput();
						if(input instanceof WidgetEditorInput){
							LfwView widget = ((WidgetEditorInput)input).getWidget();
							if(widget != null && LFWAMCPersTool.getCurrentWidget().getId().equals(widget.getId())){
								closeEditor(input);
								break;
							}
						}
					}else if(webEditor instanceof PublicViewBrowserEditor && LFWAMCPersTool.getCurrentWidget() != null){
						IEditorInput input = ((PublicViewBrowserEditor)webEditor).getEditorInput();
						if(input instanceof PublicViewEditorInput){
							LfwView widget = ((PublicViewEditorInput)input).getWidget();
							if(widget != null && LFWAMCPersTool.getCurrentWidget().getId().equals(widget.getId())){
								closeEditor(input);
								break;
							}
						}
					}else if(webEditor instanceof WindowBrowserEditor && LFWAMCPersTool.getCurrentPageMeta() != null){
						IEditorInput input = ((WindowBrowserEditor)webEditor).getEditorInput();
						if(input instanceof PagemetaEditorInput){
							LfwWindow meta = ((PagemetaEditorInput)input).getPagemeta();
							if(meta != null && LFWAMCPersTool.getCurrentPageMeta().getId().equals(meta.getId())){
								closeEditor(input);
								break;
							}
						}
					}
				}
			}
			LFWAMCPersTool.refreshCurrentPorject();
		}
	}
	
	private void closeEditor(IEditorInput editorinput) {
		IWorkbenchPage workbenchPage = site.getPage();
		IEditorPart editor = workbenchPage.findEditor(editorinput);
		workbenchPage.closeEditor(editor, false);
	}
	
	public IViewSite getSite() {
		return site;
	}
	public void setSite(IViewSite site) {
		this.site = site;
	}
	
}
