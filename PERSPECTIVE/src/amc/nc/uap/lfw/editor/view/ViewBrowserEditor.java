/**
 * 
 */
package nc.uap.lfw.editor.view;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.WidgetEditorInput;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.browser.MozillaBrowser;
import nc.uap.lfw.editor.common.editor.LFWBrowserEditor;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;

/**
 * @author chouhl
 *
 */
public class ViewBrowserEditor extends LFWBrowserEditor {
	
	public ViewBrowserEditor(){
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	public MozillaBrowser createMozillaBrowser(Composite parent){
		MozillaBrowser mozilla = new MozillaBrowser(parent, MozillaBrowser.VIEW_URL);
		mozilla.setTreeItem(((WidgetEditorInput)getEditorInput()).getPmTreeItem());
		mozilla.setEditor(this);
		return mozilla;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		WidgetEditorInput input = (WidgetEditorInput)this.getEditorInput();
		LFWWidgetTreeItem currentItem = (LFWWidgetTreeItem)input.getCurrentTreeItem();
		if(currentItem == null ||currentItem.isDisposed()){
			this.setDirtyFalse();
			MessageDialog.openError(null, M_editor.ViewBrowserEditor_0, M_editor.ViewBrowserEditor_1);
			return;
		}
		LFWPersTool.getTree().setSelection(currentItem);	
		LFWAMCConnector.updateViewSessionCache(getView(), getUimeta(), getElementMap());
		LFWAMCConnector.saveWidgetAndUIMetaFromSessionCache(getSessionId(), getPageMetaId(), getWidgetId(), getNodePath());

		currentItem.setWidget(getView());
		LfwWindow win = LFWPersTool.getCurrentPageMeta();
		win.removeView(getView().getId());
		win.addView(getView());
		LFWPersTool.getCurrentPageMetaTreeItem().setPm(win);
		
		getMozilla().changeSaveStatus();
		LFWPageMetaTreeItem pmItem = LFWPersTool.getCurrentPageMetaTreeItem();
		for(TreeItem subItem:pmItem.getItems()){
			if(subItem instanceof LFWWidgetTreeItem && ((LFWWidgetTreeItem)subItem).getWidget().getId().equals(getView().getId())){
				LFWPersTool.getTree().setSelection(subItem);
				break;
			}
		}
//		getMozilla().execute("saveForEclipse();");
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		setUimeta(LFWAMCPersTool.getCurrentUIMeta());
		setView((LfwView)((WidgetEditorInput)getEditorInput()).getWidget().clone());
		LfwWindow pageMeta = ((WidgetEditorInput)getEditorInput()).getPagemeta();
		String pageId = null;
		if(pageMeta.getComponentId()==null||pageMeta.getComponentId().equals("")||pageMeta.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT)) //$NON-NLS-1$
			pageId = pageMeta.getId();
		else pageId = pageMeta.getComponentId() + "." + pageMeta.getId(); //$NON-NLS-1$
		setPageMetaId(pageId);
		setWidgetId(((WidgetEditorInput)getEditorInput()).getWidget().getId());
	}

	public static ViewBrowserEditor getActiveEditor(){
		IEditorPart editor = LFWBrowserEditor.getActiveEditor();
		if(editor instanceof ViewBrowserEditor){
			return (ViewBrowserEditor)editor;
		}else {
			return null;
		}
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

}



