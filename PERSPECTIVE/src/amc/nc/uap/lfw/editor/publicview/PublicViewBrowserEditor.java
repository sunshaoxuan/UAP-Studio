/**
 * 
 */
package nc.uap.lfw.editor.publicview;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.WidgetEditorInput;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.browser.MozillaBrowser;
import nc.uap.lfw.editor.common.editor.LFWBrowserEditor;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

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
public class PublicViewBrowserEditor extends LFWBrowserEditor {
	
	public PublicViewBrowserEditor(){
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	@Override
	public MozillaBrowser createMozillaBrowser(Composite parent) {
		MozillaBrowser mozilla = new MozillaBrowser(parent, MozillaBrowser.PUBLIC_VIEW_URL);
		mozilla.setTreeItem(((PublicViewEditorInput)getEditorInput()).getDirTreeItem());
		mozilla.setEditor(this);
		return mozilla;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		PublicViewEditorInput input = (PublicViewEditorInput)this.getEditorInput();
		LFWWidgetTreeItem currentItem = (LFWWidgetTreeItem)input.getCurrentTreeItem();
		if(currentItem == null ||currentItem.isDisposed()){
			this.setDirtyFalse();
			MessageDialog.openError(null, M_editor.PublicViewBrowserEditor_0, M_editor.PublicViewBrowserEditor_1);
			return;
		}
		LFWPersTool.getTree().setSelection(currentItem);	
		LFWAMCConnector.updateViewSessionCache(getView(), getUimeta(), getElementMap());
		LFWAMCConnector.saveWidgetAndUIMetaFromSessionCache(getSessionId(), getPageMetaId(), getWidgetId(), getNodePath());
		currentItem.setWidget(getView());
		currentItem.removeAll();
		LFWExplorerTreeView.getLFWExploerTreeView(null).detalWidgetTreeItem(currentItem, currentItem.getFile(), getView());
		getMozilla().changeSaveStatus();
//		getMozilla().execute("saveForEclipse();");
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		setUimeta(LFWAMCPersTool.getCurrentUIMeta());
		setView((LfwView)((PublicViewEditorInput)getEditorInput()).getWidget().clone());
		setPageMetaId(WEBPersConstants.DEFAULT_WINDOW_ID);
		setWidgetId(((PublicViewEditorInput)getEditorInput()).getWidget().getId());
	}

	@Override
	protected void editMenuManager(IMenuManager manager) {
	}


	@Override
	protected LfwElementObjWithGraph getLeftElement() {
		return null;
	}

	@Override
	protected LfwElementObjWithGraph getTopElement() {
		return null;
	}
	
	public static PublicViewBrowserEditor getActiveEditor(){
		IEditorPart editor = LFWBrowserEditor.getActiveEditor();
		if(editor != null && editor instanceof PublicViewBrowserEditor){
			return (PublicViewBrowserEditor)editor;
		}else {
			return null;
		}
	}

}
