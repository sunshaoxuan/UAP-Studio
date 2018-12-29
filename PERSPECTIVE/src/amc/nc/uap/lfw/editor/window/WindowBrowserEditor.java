/**
 * 
 */
package nc.uap.lfw.editor.window;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.PagemetaEditorInput;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.browser.MozillaBrowser;
import nc.uap.lfw.editor.common.editor.LFWBrowserEditor;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.palette.PaletteFactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;

/**
 * @author chouhl
 *
 */
public class WindowBrowserEditor extends LFWBrowserEditor{
	
	public WindowBrowserEditor(){
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	@Override
	public void setFocus() {
		super.setFocus();
		LFWPersTool.hideView(LFWTool.ID_LFW_VIEW_SHEET);
	}
	
	public MozillaBrowser createMozillaBrowser(Composite parent){
		MozillaBrowser mozilla = new MozillaBrowser(parent, MozillaBrowser.WINDOW_URL);
		mozilla.setTreeItem(((WindowEditorInput)getEditorInput()).getPmTreeItem());
		mozilla.setEditor(this);
		return mozilla;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		PagemetaEditorInput input = (PagemetaEditorInput)this.getEditorInput();
		LFWPageMetaTreeItem currentItem = (LFWPageMetaTreeItem)input.getCurrentTreeItem();
		if(currentItem == null ||currentItem.isDisposed()){
			this.setDirtyFalse();
			MessageDialog.openError(null, M_editor.WindowBrowserEditor_0, M_editor.WindowBrowserEditor_1);
			return;
		}
		LFWPersTool.getTree().setSelection(currentItem);	
		LFWAMCConnector.savePageMetaAndUIMetaFromSessionCache(getSessionId(), getPageMetaId(), getNodePath());
		getMozilla().changeSaveStatus();
//		getMozilla().execute("saveForEclipse();");
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		LfwWindow pageMeta = ((WindowEditorInput)getEditorInput()).getPagemeta();
		String pageId = null;
		if(pageMeta.getComponentId()==null||pageMeta.getComponentId().equals("")||pageMeta.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT)) //$NON-NLS-1$
			pageId = pageMeta.getId();
		else pageId = pageMeta.getComponentId() + "." + pageMeta.getId(); //$NON-NLS-1$
		setPageMetaId(pageId);
	}

	public static WindowBrowserEditor getActiveEditor(){
		IEditorPart editor = LFWBrowserEditor.getActiveEditor();
		if(editor != null && editor instanceof WindowBrowserEditor){
			return (WindowBrowserEditor)editor;
		}else {
			return null;
		}
	}

	@Override
	protected void editMenuManager(IMenuManager manager) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected LfwElementObjWithGraph getLeftElement() {
		return null;
	}

	@Override
	protected LfwElementObjWithGraph getTopElement() {
		return null;
	}

}
